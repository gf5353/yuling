package org.mobile.gqz.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import org.mobile.gqz.exception.callback.CrashHandlerListener;

import android.content.Context;
import android.util.Log;

/**
 * 捕获全局异常,因为有的异常我们捕获不到
 * 
 * @author river
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	private final static String TAG = "mCrashHandler";
	private static CrashHandler mCrashHandler;


	private CrashHandlerListener listener;
	public Context context;

	public void setCrashHandlerListener(CrashHandlerListener listener) {
		this.listener = listener;
	}

	public void instance(Context context) {
		this.context = context;
	}

	/**
	 * 同步方法，以免单例多线程环境下出现异常
	 * 
	 * @return
	 */
	public synchronized static CrashHandler onCreate() {
		if (mCrashHandler == null) {
			mCrashHandler = new CrashHandler();
			Thread.setDefaultUncaughtExceptionHandler(mCrashHandler);
		}
		return mCrashHandler;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (listener != null) {
			if (context != null) {
				listener.tryDo(thread, ex, context);
			}
		}
	}
}
