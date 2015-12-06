package org.mobile.gqz.ui.fragment;

import org.mobile.gqz.exception.CrashHandler;
import org.mobile.gqz.utils.ViewUtils;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment implements OnClickListener {

	public static final int WHICH_MSG = 0X37211;
	
	public void doRequest(int position){
		
	}

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

	protected abstract View inflaterView(LayoutInflater inflater,
			ViewGroup container, Bundle bundle);

	/**
	 * initialization widget, you should look like parentView.findviewbyid(id);
	 * call method
	 * 
	 * @param parentView
	 */
	protected void initWidget(View parentView) {
	}

	/** initialization data */
	protected void initData() {
	}

	/**
	 * initialization data. And this method run in background thread, so you
	 * shouldn't change ui<br>
	 * on initializated, will call threadDataInited();
	 */
	protected void initDataFromThread() {
		callback = new ThreadDataCallBack() {
			@Override
			public void onSuccess() {
				threadDataInited();
			}
		};
	}

	/**
	 * 如果调用了initDataFromThread()，则当数据初始化完成后将回调该方法。
	 */
	protected void threadDataInited() {
	}

	/** widget click method */
	protected void widgetClick(View v) {
	}

	@Override
	public void onClick(View v) {
		widgetClick(v);
	}

	public Context context;

	public Context getContext() {
		return context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		CrashHandler.onCreate().instance(context);// 捕获可能发生的异常
		View view = inflaterView(inflater, container, savedInstanceState);
		ViewUtils.inject(this, view);
		initData();
		initWidget(view);
		new Thread(new Runnable() {
			@Override
			public void run() {
				initDataFromThread();
				threadHandle.sendEmptyMessage(WHICH_MSG);
			}
		}).start();
		return view;
	}
}
