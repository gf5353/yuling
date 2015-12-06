package org.mobile.gqz.ui.activity;

import org.mobile.gqz.exception.CrashHandler;
import org.mobile.gqz.ui.fragment.GQZFragment;
import org.mobile.gqz.ui.impl.I_Activity;
import org.mobile.gqz.ui.impl.I_BroadcastReg;
import org.mobile.gqz.ui.impl.I_SkipActivity;
import org.mobile.gqz.utils.ViewUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class BaseActivity extends Activity implements OnClickListener,
		I_BroadcastReg, I_Activity, I_SkipActivity {
	public static final int WHICH_MSG = 0X37210;

	/**
	 * 一个私有回调类，线程中初始化数据完成后的回调
	 */
	private interface ThreadDataCallBack {
		void onSuccess();
	}

	private static ThreadDataCallBack callback;

	// 当线程中初始化的数据初始化完成后，调用回调方法
	private static Handler threadHandle = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == WHICH_MSG) {
				callback.onSuccess();
			}
		};
	};

	/**
	 * 如果调用了initDataFromThread()，则当数据初始化完成后将回调该方法。
	 */
	protected void threadDataInited() {
	}

	/**
	 * 在线程中初始化数据，注意不能在这里执行UI操作
	 */
	@Override
	public void initDataFromThread() {
		callback = new ThreadDataCallBack() {
			@Override
			public void onSuccess() {
				threadDataInited();
			}
		};
	}

	@Override
	public void initData(Bundle savedInstanceState) {
	}

	@Override
	public void initWidget() {
	}

	// 仅仅是为了代码整洁点
	private void initializer(Bundle savedInstanceState) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				initDataFromThread();
				threadHandle.sendEmptyMessage(WHICH_MSG);
			}
		}).start();
		initData(savedInstanceState);
		initWidget();
	}

	/** listened widget's click method */
	@Override
	public void widgetClick(View v) {
	}

	@Override
	public void onClick(View v) {
		widgetClick(v);
	}

	@Override
	public void registerBroadcast() {
	}

	@Override
	public void unRegisterBroadcast() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CrashHandler.onCreate().instance(this);// 捕获可能发生的异常
		setRootView(); // 必须放在annotate之前调用
		ViewUtils.inject(this);
		initializer(savedInstanceState);
		registerBroadcast();
	}

	@Override
	protected void onDestroy() {
		unRegisterBroadcast();
		super.onDestroy();
	}

	/**
	 * 用Fragment替换视图
	 * 
	 * @param resView
	 *            将要被替换掉的视图
	 * @param targetFragment
	 *            用来替换的Fragment
	 */
	public void changeFragment(int resView, GQZFragment targetFragment) {
//		FragmentTransaction transaction =getFragmentManager()
//				.beginTransaction();
//		transaction.replace(resView, targetFragment, targetFragment.getClass()
//				.getName());
//		transaction.commit();
	}
}
