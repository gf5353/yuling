package org.example.yulin.util;

import org.example.yulin.Constants;
import org.mobile.gqz.GQZBitmap;

import android.content.Context;
import android.os.Environment;

/**
 * Author: wyouflf Date: 13-11-12 Time: 上午10:24
 */
public class BitmapHelp {
	private BitmapHelp() {
	}

	private static GQZBitmap bitmapUtils;
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
	private static String diskCachePath = getSDCardPath() + Constants.Sp_Name
			+ "/cache/thumbnails";

	/**
	 * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
	 *
	 * @param appContext
	 *            application context
	 * @return
	 */
	public static GQZBitmap getBitmapUtils(Context appContext) {
		if (bitmapUtils == null) {
			bitmapUtils = new GQZBitmap(appContext, diskCachePath,
					DISK_CACHE_SIZE);
		}
		return bitmapUtils;
	}

	/**
	 * 判断SD卡是否可用
	 */
	private static boolean sdCardIsExit() {
		return Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取SD卡路径
	 */
	public static String getSDCardPath() {
		if (sdCardIsExit()) {
			return Environment.getExternalStorageDirectory().toString() + "/";
		}
		return null;
	}
}
