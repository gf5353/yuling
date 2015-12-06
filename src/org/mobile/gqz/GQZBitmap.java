/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mobile.gqz;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;

import org.mobile.gqz.bitmap.BitmapCacheListener;
import org.mobile.gqz.bitmap.BitmapCommonUtils;
import org.mobile.gqz.bitmap.BitmapDisplayConfig;
import org.mobile.gqz.bitmap.BitmapGlobalConfig;
import org.mobile.gqz.bitmap.callback.BitmapLoadCallBack;
import org.mobile.gqz.bitmap.callback.BitmapLoadFrom;
import org.mobile.gqz.bitmap.callback.DefaultBitmapLoadCallBack;
import org.mobile.gqz.bitmap.core.AsyncDrawable;
import org.mobile.gqz.bitmap.core.BitmapSize;
import org.mobile.gqz.bitmap.download.Downloader;
import org.mobile.gqz.cache.FileNameGenerator;
import org.mobile.gqz.task.PriorityAsyncTask;
import org.mobile.gqz.task.PriorityExecutor;
import org.mobile.gqz.task.TaskHandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;

public class GQZBitmap implements TaskHandler {

	private boolean pauseTask = false;
	private boolean cancelAllTask = false;
	private final Object pauseTaskLock = new Object();

	private Context context;
	private BitmapGlobalConfig globalConfig;
	private BitmapDisplayConfig defaultDisplayConfig;

	// //function//////

	/**
	 * 把bitmap转换成base64
	 * 
	 * @param bitmap
	 * @param bitmapQuality
	 * @return
	 */
	public static String getBase64FromBitmap(Bitmap bitmap, int bitmapQuality) {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, bitmapQuality, bStream);
		byte[] bytes = bStream.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	/**
	 * 把base64转换成bitmap
	 * 
	 * @param string
	 * @return
	 */
	public static Bitmap getBitmapFromBase64(String string) {
		byte[] bitmapArray = null;
		try {
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BitmapFactory
				.decodeByteArray(bitmapArray, 0, bitmapArray.length);
	}

	/**
	 * 缩放/裁剪图片
	 * 
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	// ///////////////////////////////////////////// create
	// ///////////////////////////////////////////////////
	public GQZBitmap(Context context) {
		this(context, null);
	}

	public GQZBitmap(Context context, String diskCachePath) {
		if (context == null) {
			throw new IllegalArgumentException("context may not be null");
		}

		this.context = context.getApplicationContext();
		globalConfig = BitmapGlobalConfig.getInstance(this.context,
				diskCachePath);
		defaultDisplayConfig = new BitmapDisplayConfig();
	}

	public GQZBitmap(Context context, String diskCachePath, int memoryCacheSize) {
		this(context, diskCachePath);
		globalConfig.setMemoryCacheSize(memoryCacheSize);
	}

	public GQZBitmap(Context context, String diskCachePath,
			int memoryCacheSize, int diskCacheSize) {
		this(context, diskCachePath);
		globalConfig.setMemoryCacheSize(memoryCacheSize);
		globalConfig.setDiskCacheSize(diskCacheSize);
	}

	public GQZBitmap(Context context, String diskCachePath,
			float memoryCachePercent) {
		this(context, diskCachePath);
		globalConfig.setMemCacheSizePercent(memoryCachePercent);
	}

	public GQZBitmap(Context context, String diskCachePath,
			float memoryCachePercent, int diskCacheSize) {
		this(context, diskCachePath);
		globalConfig.setMemCacheSizePercent(memoryCachePercent);
		globalConfig.setDiskCacheSize(diskCacheSize);
	}

	// ////////////////////////////////////// config
	// ////////////////////////////////////////////////////////////////////

	public GQZBitmap configDefaultLoadingImage(Drawable drawable) {
		defaultDisplayConfig.setLoadingDrawable(drawable);
		return this;
	}

	public GQZBitmap configDefaultLoadingImage(int resId) {
		defaultDisplayConfig.setLoadingDrawable(context.getResources()
				.getDrawable(resId));
		return this;
	}

	public GQZBitmap configDefaultLoadingImage(Bitmap bitmap) {
		defaultDisplayConfig.setLoadingDrawable(new BitmapDrawable(context
				.getResources(), bitmap));
		return this;
	}

	public GQZBitmap configDefaultLoadFailedImage(Drawable drawable) {
		defaultDisplayConfig.setLoadFailedDrawable(drawable);
		return this;
	}

	public GQZBitmap configDefaultLoadFailedImage(int resId) {
		defaultDisplayConfig.setLoadFailedDrawable(context.getResources()
				.getDrawable(resId));
		return this;
	}

	public GQZBitmap configDefaultLoadFailedImage(Bitmap bitmap) {
		defaultDisplayConfig.setLoadFailedDrawable(new BitmapDrawable(context
				.getResources(), bitmap));
		return this;
	}

	public GQZBitmap configDefaultBitmapMaxSize(int maxWidth, int maxHeight) {
		defaultDisplayConfig.setBitmapMaxSize(new BitmapSize(maxWidth,
				maxHeight));
		return this;
	}

	public GQZBitmap configDefaultBitmapMaxSize(BitmapSize maxSize) {
		defaultDisplayConfig.setBitmapMaxSize(maxSize);
		return this;
	}

	public GQZBitmap configDefaultImageLoadAnimation(Animation animation) {
		defaultDisplayConfig.setAnimation(animation);
		return this;
	}

	public GQZBitmap configDefaultAutoRotation(boolean autoRotation) {
		defaultDisplayConfig.setAutoRotation(autoRotation);
		return this;
	}

	public GQZBitmap configDefaultShowOriginal(boolean showOriginal) {
		defaultDisplayConfig.setShowOriginal(showOriginal);
		return this;
	}

	public GQZBitmap configDefaultBitmapConfig(Bitmap.Config config) {
		defaultDisplayConfig.setBitmapConfig(config);
		return this;
	}

	public GQZBitmap configDefaultDisplayConfig(
			BitmapDisplayConfig displayConfig) {
		defaultDisplayConfig = displayConfig;
		return this;
	}

	public GQZBitmap configDownloader(Downloader downloader) {
		globalConfig.setDownloader(downloader);
		return this;
	}

	public GQZBitmap configDefaultCacheExpiry(long defaultExpiry) {
		globalConfig.setDefaultCacheExpiry(defaultExpiry);
		return this;
	}

	public GQZBitmap configDefaultConnectTimeout(int connectTimeout) {
		globalConfig.setDefaultConnectTimeout(connectTimeout);
		return this;
	}

	public GQZBitmap configDefaultReadTimeout(int readTimeout) {
		globalConfig.setDefaultReadTimeout(readTimeout);
		return this;
	}

	public GQZBitmap configThreadPoolSize(int threadPoolSize) {
		globalConfig.setThreadPoolSize(threadPoolSize);
		return this;
	}

	public GQZBitmap configMemoryCacheEnabled(boolean enabled) {
		globalConfig.setMemoryCacheEnabled(enabled);
		return this;
	}

	public GQZBitmap configDiskCacheEnabled(boolean enabled) {
		globalConfig.setDiskCacheEnabled(enabled);
		return this;
	}

	public GQZBitmap configDiskCacheFileNameGenerator(
			FileNameGenerator fileNameGenerator) {
		globalConfig.setFileNameGenerator(fileNameGenerator);
		return this;
	}

	public GQZBitmap configBitmapCacheListener(BitmapCacheListener listener) {
		globalConfig.setBitmapCacheListener(listener);
		return this;
	}

	// //////////////////////// display ////////////////////////////////////

	public <T extends View> void display(T container, String uri) {
		display(container, uri, null, null);
	}

	public <T extends View> void display(T container, String uri,
			BitmapDisplayConfig displayConfig) {
		display(container, uri, displayConfig, null);
	}

	public <T extends View> void display(T container, String uri,
			BitmapLoadCallBack<T> callBack) {
		display(container, uri, null, callBack);
	}

	public <T extends View> void display(T container, String uri,
			BitmapDisplayConfig displayConfig, BitmapLoadCallBack<T> callBack) {
		if (container == null) {
			return;
		}

		if (callBack == null) {
			callBack = new DefaultBitmapLoadCallBack<T>();
		}

		if (displayConfig == null || displayConfig == defaultDisplayConfig) {
			displayConfig = defaultDisplayConfig.cloneNew();
		}

		// Optimize Max Size
		BitmapSize size = displayConfig.getBitmapMaxSize();
		displayConfig.setBitmapMaxSize(BitmapCommonUtils.optimizeMaxSizeByView(
				container, size.getWidth(), size.getHeight()));

		container.clearAnimation();

		if (TextUtils.isEmpty(uri)) {
			callBack.onLoadFailed(container, uri,
					displayConfig.getLoadFailedDrawable());
			return;
		}

		// start loading
		callBack.onPreLoad(container, uri, displayConfig);

		// find bitmap from mem cache.
		Bitmap bitmap = globalConfig.getBitmapCache().getBitmapFromMemCache(
				uri, displayConfig);

		if (bitmap != null) {
			callBack.onLoadStarted(container, uri, displayConfig);
			callBack.onLoadCompleted(container, uri, bitmap, displayConfig,
					BitmapLoadFrom.MEMORY_CACHE);
		} else if (!bitmapLoadTaskExist(container, uri, callBack)) {

			final BitmapLoadTask<T> loadTask = new BitmapLoadTask<T>(container,
					uri, displayConfig, callBack);

			// get executor
			PriorityExecutor executor = globalConfig.getBitmapLoadExecutor();
			File diskCacheFile = this.getBitmapFileFromDiskCache(uri);
			boolean diskCacheExist = diskCacheFile != null
					&& diskCacheFile.exists();
			if (diskCacheExist && executor.isBusy()) {
				executor = globalConfig.getDiskCacheExecutor();
			}
			// set loading image
			Drawable loadingDrawable = displayConfig.getLoadingDrawable();
			callBack.setDrawable(container, new AsyncDrawable<T>(
					loadingDrawable, loadTask));

			loadTask.setPriority(displayConfig.getPriority());
			loadTask.executeOnExecutor(executor);
		}
	}

	// ///////////////////////////////////////////// cache
	// /////////////////////////////////////////////////////////////////

	public void clearCache() {
		globalConfig.clearCache();
	}

	public void clearMemoryCache() {
		globalConfig.clearMemoryCache();
	}

	public void clearDiskCache() {
		globalConfig.clearDiskCache();
	}

	public void clearCache(String uri) {
		globalConfig.clearCache(uri);
	}

	public void clearMemoryCache(String uri) {
		globalConfig.clearMemoryCache(uri);
	}

	public void clearDiskCache(String uri) {
		globalConfig.clearDiskCache(uri);
	}

	public void flushCache() {
		globalConfig.flushCache();
	}

	public void closeCache() {
		globalConfig.closeCache();
	}

	public File getBitmapFileFromDiskCache(String uri) {
		return globalConfig.getBitmapCache().getBitmapFileFromDiskCache(uri);
	}

	public Bitmap getBitmapFromMemCache(String uri, BitmapDisplayConfig config) {
		if (config == null) {
			config = defaultDisplayConfig;
		}
		return globalConfig.getBitmapCache().getBitmapFromMemCache(uri, config);
	}

	// //////////////////////////////////////// tasks
	// //////////////////////////////////////////////////////////////////////

	@Override
	public boolean supportPause() {
		return true;
	}

	@Override
	public boolean supportResume() {
		return true;
	}

	@Override
	public boolean supportCancel() {
		return true;
	}

	@Override
	public void pause() {
		pauseTask = true;
		flushCache();
	}

	@Override
	public void resume() {
		pauseTask = false;
		synchronized (pauseTaskLock) {
			pauseTaskLock.notifyAll();
		}
	}

	@Override
	public void cancel() {
		pauseTask = true;
		cancelAllTask = true;
		synchronized (pauseTaskLock) {
			pauseTaskLock.notifyAll();
		}
	}

	@Override
	public boolean isPaused() {
		return pauseTask;
	}

	@Override
	public boolean isCancelled() {
		return cancelAllTask;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	private static <T extends View> BitmapLoadTask<T> getBitmapTaskFromContainer(
			T container, BitmapLoadCallBack<T> callBack) {
		if (container != null) {
			final Drawable drawable = callBack.getDrawable(container);
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable<T> asyncDrawable = (AsyncDrawable<T>) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	private static <T extends View> boolean bitmapLoadTaskExist(T container,
			String uri, BitmapLoadCallBack<T> callBack) {
		final BitmapLoadTask<T> oldLoadTask = getBitmapTaskFromContainer(
				container, callBack);

		if (oldLoadTask != null) {
			final String oldUrl = oldLoadTask.uri;
			if (TextUtils.isEmpty(oldUrl) || !oldUrl.equals(uri)) {
				oldLoadTask.cancel(true);
			} else {
				return true;
			}
		}
		return false;
	}

	public class BitmapLoadTask<T extends View> extends
			PriorityAsyncTask<Object, Object, Bitmap> {
		private final String uri;
		private final WeakReference<T> containerReference;
		private final BitmapLoadCallBack<T> callBack;
		private final BitmapDisplayConfig displayConfig;

		private BitmapLoadFrom from = BitmapLoadFrom.DISK_CACHE;

		public BitmapLoadTask(T container, String uri,
				BitmapDisplayConfig config, BitmapLoadCallBack<T> callBack) {
			if (container == null || uri == null || config == null
					|| callBack == null) {
				throw new IllegalArgumentException("args may not be null");
			}

			this.containerReference = new WeakReference<T>(container);
			this.callBack = callBack;
			this.uri = uri;
			this.displayConfig = config;
		}

		@Override
		protected Bitmap doInBackground(Object... params) {

			synchronized (pauseTaskLock) {
				while (pauseTask && !this.isCancelled()) {
					try {
						pauseTaskLock.wait();
						if (cancelAllTask) {
							return null;
						}
					} catch (Throwable e) {
					}
				}
			}

			Bitmap bitmap = null;

			// get cache from disk cache
			if (!this.isCancelled() && this.getTargetContainer() != null) {
				this.publishProgress(PROGRESS_LOAD_STARTED);
				bitmap = globalConfig.getBitmapCache().getBitmapFromDiskCache(
						uri, displayConfig);
			}

			// download image
			if (bitmap == null && !this.isCancelled()
					&& this.getTargetContainer() != null) {
				bitmap = globalConfig.getBitmapCache().downloadBitmap(uri,
						displayConfig, this);
				from = BitmapLoadFrom.URI;
			}

			return bitmap;
		}

		public void updateProgress(long total, long current) {
			this.publishProgress(PROGRESS_LOADING, total, current);
		}

		private static final int PROGRESS_LOAD_STARTED = 0;
		private static final int PROGRESS_LOADING = 1;

		@Override
		protected void onProgressUpdate(Object... values) {
			if (values == null || values.length == 0)
				return;

			final T container = this.getTargetContainer();
			if (container == null)
				return;

			switch ((Integer) values[0]) {
			case PROGRESS_LOAD_STARTED:
				callBack.onLoadStarted(container, uri, displayConfig);
				break;
			case PROGRESS_LOADING:
				if (values.length != 3)
					return;
				callBack.onLoading(container, uri, displayConfig,
						(Long) values[1], (Long) values[2]);
				break;
			default:
				break;
			}
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			final T container = this.getTargetContainer();
			if (container != null) {
				if (bitmap != null) {
					callBack.onLoadCompleted(container, this.uri, bitmap,
							displayConfig, from);
				} else {
					callBack.onLoadFailed(container, this.uri,
							displayConfig.getLoadFailedDrawable());
				}
			}
		}

		@Override
		protected void onCancelled(Bitmap bitmap) {
			synchronized (pauseTaskLock) {
				pauseTaskLock.notifyAll();
			}
		}

		public T getTargetContainer() {
			final T container = containerReference.get();
			final BitmapLoadTask<T> bitmapWorkerTask = getBitmapTaskFromContainer(
					container, callBack);

			if (this == bitmapWorkerTask) {
				return container;
			}

			return null;
		}
	}
}
