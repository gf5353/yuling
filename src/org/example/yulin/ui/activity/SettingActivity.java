package org.example.yulin.ui.activity;

import org.example.yulin.R;
import org.example.yulin.ui.view.SeekBarPreference;
import org.mobile.gqz.GQZCache;
import org.mobile.gqz.GQZInject;

import com.umeng.analytics.MobclickAgent;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.text.format.Formatter;
import android.view.MenuItem;

public class SettingActivity extends PreferenceActivity implements
		OnPreferenceClickListener, OnPreferenceChangeListener {
	private CheckBoxPreference play_voice;// 自动播放语灵
	private Preference clean_cache;// 缓存
	PreferenceScreen screen;
	public String pageName = "设置页面";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);
		setContentView(R.layout.activity_setting);
		addPreferencesFromResource(R.xml.setting);
		observer = new PkgSizeObserver();
		try {
			GQZCache.getCacheSize(this, observer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		initPreference();
	}

	PkgSizeObserver observer;
	SeekBarPreference play_tone;// 音调

	private void initPreference() {
		// 得到我们的存储Preferences值的对象，然后对其进行相应操作
		SharedPreferences shp = PreferenceManager
				.getDefaultSharedPreferences(this);
		// screen = getPreferenceScreen();
		PreferenceCategory set_yuling = (PreferenceCategory) findPreference("set_yuling");
		set_yuling.setOrderingAsAdded(true);

		play_voice = new CheckBoxPreference(this);
		play_voice.setKey("play_voice");
		play_voice.setTitle("机器人自动播放");
		play_voice.setSummaryOff("关闭");
		play_voice.setSummaryOn("打开");
		play_voice.setDefaultValue(false);
		set_yuling.addPreference(play_voice);
		// play_tone = new SeekBarPreference(this);
		// // play_tone.setName("音调");
		// set_yuling.addPreference(play_tone);
		play_voice.setOnPreferenceClickListener(this);
		play_voice.setOnPreferenceChangeListener(this);

		clean_cache = findPreference("clean_cache");

		clean_cache.setOnPreferenceClickListener(this);
	}

	// 点击按钮时，对控件进行的一些操作
	private void operatePreference(Preference preference) {
		if (preference == play_voice) { // 点击了 自动播放
		} else if (preference == clean_cache) {// 清理缓存
			GQZCache.cleanInternalCache(this);

			try {
				GQZCache.getCacheSize(this, observer);
				GQZInject.toast("下次重启后生效");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 当Preference的值发生改变时触发该事件，true则以新值更新控件的状态，false则do noting
	@Override
	public boolean onPreferenceChange(Preference preference, Object object) {
		if (preference == play_voice) {
			return true;
		}

		return false;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {

		String key = preference.getKey();
		// 对控件进行操作
		operatePreference(preference);

		return false;
	}

	// aidl文件形成的Bindler机制服务类
	public class PkgSizeObserver extends IPackageStatsObserver.Stub {
		/***
		 * 回调函数，
		 * 
		 * @param pStatus
		 *            ,返回数据封装在PackageStats对象中
		 * @param succeeded
		 *            代表回调成功
		 */
		@Override
		public void onGetStatsCompleted(final PackageStats pStats,
				boolean succeeded) throws RemoteException {
			if (succeeded) {

				runOnUiThread(new Runnable() {
					public void run() {
						clean_cache.setSummary("缓存大小:"
								+ formateFileSize(pStats.cacheSize));
					}
				});
			}

			// cachesize = pStats.cacheSize; // 缓存大小
			// datasize = pStats.dataSize; // 数据大小
			// codesize = pStats.codeSize; // 应用程序大小
			// totalsize = cachesize + datasize + codesize;
			// Log.i(TAG, "cachesize--->" + cachesize + " datasize---->"
			// + datasize + " codeSize---->" + codesize);
		}
	}

	// 系统函数，字符串转换 long -String (kb)
	private String formateFileSize(long size) {
		return Formatter.formatFileSize(this, size);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(pageName);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(pageName);
		MobclickAgent.onPause(this);
	}
}
