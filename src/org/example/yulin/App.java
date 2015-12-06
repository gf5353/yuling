package org.example.yulin;

import org.example.yulin.util.BitmapHelp;
import org.example.yulin.util.Json;
import org.example.yulin.util.MapUtil;
import org.mobile.gqz.GQZBitmap;
import org.mobile.gqz.GQZLog;
import org.mobile.gqz.GQZSharepf;
import org.mobile.gqz.exception.CrashHandler;
import org.mobile.gqz.exception.callback.CrashHandlerListener;
import org.mobile.gqz.utils.SystemTool;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Looper;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.update.UpdateResponse;

import com.iflytek.cloud.SpeechUtility;
import com.umeng.analytics.MobclickAgent;

public class App extends Application implements CrashHandlerListener {
	public static Context context;

	public static Context getContext() {
		return context;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Json.getInstance();
		context = getApplicationContext();
		initBitmap();
		// 注册异常捕获器
		CrashHandler.onCreate().setCrashHandlerListener(this);
		// 实例化
		GQZSharepf.getInstance(getBaseContext());
		// Bmob
		initBmob();
		// 讯飞
		initIflytek();
		// 聚合数据
		initJuHe();
		// 百度
		initBaiDu();
		// 友盟统计
		initUmeng();
		GQZLog.openDebutLog(false);
		GQZLog.openActivityState(false);
	}

	private void initUmeng() {
		MobclickAgent.setDebugMode(false);
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。

	}

	GQZBitmap bitmap;

	private void initBitmap() {
		bitmap = BitmapHelp.getBitmapUtils(getApplicationContext());
	}

	private void initBaiDu() {
		com.baidu.mapapi.SDKInitializer.initialize(this);
		MapUtil.getInstance(getApplicationContext());
	}

	private void initJuHe() {
		com.thinkland.sdk.android.SDKInitializer
				.initialize(getApplicationContext());
	}

	private void initIflytek() {
		// 应用程序入口处调用,避免手机内存过小，杀死后台进程,造成SpeechUtility对象为null
		// 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
		// 参数间使用“,”分隔。
		// 设置你申请的应用appid
		SpeechUtility.createUtility(this, "appid=" + AppConfig.XunfeiAppId);
	}

	UpdateResponse ur;

	private void initBmob() {
		Bmob.initialize(this, AppConfig.BmobAppId);
		// 初始化建表操作
		// BmobUpdateAgent.initAppVersion(this);

		// 使用推送服务时的初始化操作
		BmobInstallation.getCurrentInstallation(this).save();
		// 启动推送服务
		BmobPush.startWork(this, AppConfig.BmobAppId);
	}

	@Override
	public void tryDo(Thread thread, Throwable ex, Context context) {
//		showDialog(SystemTool.getSystemError(context, ex), context);
		MobclickAgent.onKillProcess(context);
		 System.exit(0);
	}

	private void showDialog(final String string, final Context context) {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();

				new AlertDialog.Builder(context).setTitle("大爷我崩溃了...")
						.setCancelable(false).setMessage(string)
						.setNeutralButton("我知道了", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MobclickAgent.onKillProcess(context);
								System.exit(0);
							}

						}).create().show();
				Looper.loop();

			}
		}.start();
	}
}
