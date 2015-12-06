package org.mobile.gqz.ui.activity;

import org.mobile.gqz.GQZLog;
import org.mobile.gqz.ui.GQZActivityStack;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class GQZActivity extends BaseActivity {
	public String pageName = "Activity基类";

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	/**
	 * 当前Activity状态
	 */
	public static enum ActivityState {
		RESUME, PAUSE, STOP, DESTROY
	}

	public Activity aty;
	/** Activity状态 */
	public ActivityState activityState = ActivityState.DESTROY;

	/***************************************************************************
	 * 
	 * print Activity callback methods
	 * 
	 ***************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		aty = this;
		GQZActivityStack.create().addActivity(this);
		GQZLog.state(this.getClass().getName(), "---------onCreat ");
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		GQZLog.state(this.getClass().getName(), "---------onStart ");
	}

	@Override
	protected void onResume() {
		super.onResume();
		activityState = ActivityState.RESUME;
		GQZLog.state(this.getClass().getName(), "---------onResume ");
		MobclickAgent.onPageStart(pageName);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		activityState = ActivityState.PAUSE;
		GQZLog.state(this.getClass().getName(), "---------onPause ");
		MobclickAgent.onPageEnd(pageName);
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onResume();
		activityState = ActivityState.STOP;
		GQZLog.state(this.getClass().getName(), "---------onStop ");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		GQZLog.state(this.getClass().getName(), "---------onRestart ");
	}

	@Override
	protected void onDestroy() {
		activityState = ActivityState.DESTROY;
		GQZLog.state(this.getClass().getName(), "---------onDestroy ");
		super.onDestroy();
		GQZActivityStack.create().finishActivity(this);
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Class<?> cls) {
		showActivity(aty, cls);
		aty.finish();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Intent it) {
		showActivity(aty, it);
		aty.finish();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
		showActivity(aty, cls, extras);
		aty.finish();
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Intent it) {
		aty.startActivity(it);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
		Intent intent = new Intent();
		intent.putExtras(extras);
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}

	@Override
	public void setRootView() {

	}

	@Override
	public void showActivity(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}
}
