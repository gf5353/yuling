package org.mobile.gqz.ui.fragment;

import org.mobile.gqz.GQZLog;

import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

public abstract class GQZFragment extends BaseFragment {
	public String pageName = "Fragment基类";

	/***************************************************************************
	 * 
	 * print Fragment callback methods
	 * 
	 ***************************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GQZLog.state(this.getClass().getName(), "---------onCreateView ");
	}

	@Override
	public void onResume() {
		super.onResume();
		GQZLog.state(this.getClass().getName(), "---------onResume ");
		MobclickAgent.onPageStart(pageName); // 统计页面
	}

	@Override
	public void onStart() {
		super.onStart();
		GQZLog.state(this.getClass().getName(), "---------onStart ");
	}

	@Override
	public void onPause() {
		super.onPause();
		GQZLog.state(this.getClass().getName(), "---------onPause ");
		MobclickAgent.onPageEnd(pageName);
	}

	@Override
	public void onStop() {
		GQZLog.state(this.getClass().getName(), "---------onStop ");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		GQZLog.state(this.getClass().getName(), "---------onDestroy ");
		super.onDestroyView();
	}

}
