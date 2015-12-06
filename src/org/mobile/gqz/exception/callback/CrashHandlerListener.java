package org.mobile.gqz.exception.callback;

import android.content.Context;

public interface CrashHandlerListener {
	/**
	 * 捕获异常所做的事情
	 * 
	 * @param thread
	 * @param ex
	 * @param context
	 *            必须是当前的上下文才能触发ui操作
	 */
	void tryDo(Thread thread, Throwable ex, Context context);
}
