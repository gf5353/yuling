package org.example.yulin.ui.activity;

import java.util.List;

import org.example.yulin.Constants;
import org.example.yulin.R;
import org.example.yulin.bean.bmob.BUser;
import org.example.yulin.bean.bmob.BmobConfig;
import org.example.yulin.util.DianPingUtil;
import org.example.yulin.util.MapUtil;
import org.example.yulin.util.MapUtil.OnMapLocationListener;
import org.mobile.gqz.GQZLog;
import org.mobile.gqz.GQZSharepf;
import org.mobile.gqz.ui.activity.GQZActivity;
import org.mobile.gqz.ui.annotation.ContentView;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

@ContentView(R.layout.activity_loading)
public class LoadingActivity extends GQZActivity {

	DianPingUtil dianping;
	ActionBar actionbar;

	@Override
	public void initData(Bundle savedInstanceState) {
		super.initData(savedInstanceState);
		setPageName("程序加载页面");
		actionbar = getActionBar();
		actionbar.setTitle("正在载入...");
		initUmeng();
		loadConfig();
		login();// 登录
		// 定位
		MapUtil.getInstance(getApplicationContext()).setOnMapLocationListener(
				new OnMapLocationListener() {

					@Override
					public boolean onLocation(BDLocation location, LatLng latLng) {
						Constants.latLng = latLng;
						if (userInfo != null) {
							BUser user = userInfo;
							user.setLatitude(latLng.latitude);
							user.setLongitude(latLng.longitude);
							user.setGpsAdd(new BmobGeoPoint(latLng.longitude,
									latLng.latitude));
							user.update(getBaseContext(),
									userInfo.getObjectId(),
									new UpdateListener() {

										@Override
										public void onSuccess() {
											GQZLog.debug("更新成功");
											showActivity(LoadingActivity.this,
													MainActivity.class);
											finish();
										}

										@Override
										public void onFailure(int arg0,
												String arg1) {
											GQZLog.debug("更新失败" + arg0 + arg1);
											showActivity(LoadingActivity.this,
													MainActivity.class);
											finish();
										}
									});

						} else {
							showActivity(LoadingActivity.this,
									MainActivity.class);
							finish();
						}
						return true;
					}
				});
	}

	private void loadConfig() {
		BmobQuery<BmobConfig> query = new BmobQuery<BmobConfig>();
		query.findObjects(this, new FindListener<BmobConfig>() {

			@Override
			public void onSuccess(List<BmobConfig> configs) {
				BmobConfig config = configs.get(0);
				Constants.Tabs = config.getMain_tabs();
				Constants.role_size = config.getRole_size();
				String pay_url = config.getUrl_pay();
				if (!TextUtils.isEmpty(pay_url)) {
					Constants.url_pay = pay_url;
				}
			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});

	}

	private void initUmeng() {
		MobclickAgent.openActivityDurationTrack(false);// 禁止默认的页面统计方式，这样将不会再自动统计Activity。
		MobclickAgent.setAutoLocation(true);
		// MobclickAgent.setSessionContinueMillis(1000);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		// 您需要在程序的入口 Activity 中添加
		MobclickAgent.updateOnlineConfig(this);
		/** 设置是否对日志信息进行加密, 默认false(不加密). */
		AnalyticsConfig.enableEncrypt(true);
	}

	BUser userInfo;

	private void login() {
		String openId = (String) GQZSharepf.get(Constants.SP_OpenId, "");
		if (!TextUtils.isEmpty(openId)) {
			BUser user = new BUser();
			user.setUsername(openId);
			user.setPassword(openId);
			user.login(this, new SaveListener() {

				@Override
				public void onSuccess() {
					userInfo = BmobUser.getCurrentUser(getBaseContext(),
							BUser.class);
					GQZLog.debug("登录成功");
					MapUtil.startLocation();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					GQZLog.debug("登录失败" + arg1);
					showActivity(LoadingActivity.this, MainActivity.class);
					finish();
				}
			});
		} else {
			MapUtil.startLocation();
		}
	}
}
