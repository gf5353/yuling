package org.mobile.gqz;

import java.io.File;
import java.lang.reflect.Method;

import org.example.yulin.ui.activity.SettingActivity.PkgSizeObserver;

import android.content.Context;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.os.Environment;

/** * 本应用数据清除管理器 */
public class GQZCache {
	static long cachesize; // 缓存大小
	static long datasize; // 数据大小
	static long codesize; // 应用程序大小
	static long totalsize; // 总大小

	public static void getCacheSize(Context context, PkgSizeObserver observer)
			throws Exception {
		// 全局变量，保存当前查询包得信息
		PackageManager manager = context.getPackageManager();
		try {
			// 通过反射机制获得该隐藏函数
			Method getPackageSizeInfo = manager.getClass().getMethod(
					"getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);
			// 调用该函数，并且给其分配参数 ，待调用流程完成后会回调PkgSizeObserver类的函数
			getPackageSizeInfo.invoke(manager, context.getPackageName(),
					observer);
		} catch (Exception ex) {
			GQZLog.debug("NoSuchMethodException");
			ex.printStackTrace();
			throw ex; // 抛出异常
		}
	}

	/** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	/** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/databases"));
	}

	/**
	 * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
	 * context
	 */
	public static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/shared_prefs"));
	}

	/** * 按名字清除本应用数据库 * * @param context * @param dbName */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	/** * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context */
	public static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	/**
	 * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
	 * context
	 */
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	/** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}

	/** * 清除本应用所有的数据 * * @param context * @param filepath */
	public static void cleanApplicationData(Context context, String... filepath) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabases(context);
		cleanSharedPreference(context);
		cleanFiles(context);
		for (String filePath : filepath) {
			cleanCustomCache(filePath);
		}
	}

	/** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}
}