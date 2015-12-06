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

package org.mobile.gqz.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.mobile.gqz.GQZLog;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.EventListenerManager;
import org.mobile.gqz.ui.annotation.PreferenceInject;
import org.mobile.gqz.ui.annotation.ResInject;
import org.mobile.gqz.ui.annotation.ResLoader;
import org.mobile.gqz.ui.annotation.ViewFinder;
import org.mobile.gqz.ui.annotation.ViewInject;
import org.mobile.gqz.ui.annotation.ViewInjectInfo;
import org.mobile.gqz.ui.annotation.event.EventBase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.view.View;

public class ViewUtils {

	private ViewUtils() {
	}

	// 屏幕方法
	/**
	 * 截图
	 * 
	 * @param v
	 *            需要进行截图的控件
	 * @return 该控件截图的Bitmap对象。
	 */
	public static Bitmap captureView(View v) {
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		return v.getDrawingCache();
	}

	/**
	 * 创建快捷方式
	 * 
	 * @param cxt
	 *            Context
	 * @param icon
	 *            快捷方式图标
	 * @param title
	 *            快捷方式标题
	 * @param cls
	 *            要启动的类
	 */
	public void createDeskShortCut(Context cxt, int icon, String title,
			Class<?> cls) {
		// 创建快捷方式的Intent
		Intent shortcutIntent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重复创建
		shortcutIntent.putExtra("duplicate", false);
		// 需要现实的名称
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		// 快捷图片
		Parcelable ico = Intent.ShortcutIconResource.fromContext(
				cxt.getApplicationContext(), icon);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, ico);
		Intent intent = new Intent(cxt, cls);
		// 下面两个属性是为了当应用程序卸载时桌面上的快捷方式会删除
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		// 点击快捷图片，运行的程序主入口
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// 发送广播。OK
		cxt.sendBroadcast(shortcutIntent);
	}

	// 注解方法

	public static void inject(View view) {
		injectObject(view, new ViewFinder(view));
	}

	public static void inject(Activity activity) {
		injectObject(activity, new ViewFinder(activity));
	}

	public static void inject(PreferenceActivity preferenceActivity) {
		injectObject(preferenceActivity, new ViewFinder(preferenceActivity));
	}

	public static void inject(Object handler, View view) {
		injectObject(handler, new ViewFinder(view));
	}

	public static void inject(Object handler, Activity activity) {
		injectObject(handler, new ViewFinder(activity));
	}

	public static void inject(Object handler, PreferenceGroup preferenceGroup) {
		injectObject(handler, new ViewFinder(preferenceGroup));
	}

	public static void inject(Object handler,
			PreferenceActivity preferenceActivity) {
		injectObject(handler, new ViewFinder(preferenceActivity));
	}

	@SuppressWarnings("ConstantConditions")
	private static void injectObject(Object handler, ViewFinder finder) {

		Class<?> handlerType = handler.getClass();

		// inject ContentView
		ContentView contentView = handlerType.getAnnotation(ContentView.class);
		if (contentView != null) {
			try {
				Method setContentViewMethod = handlerType.getMethod(
						"setContentView", int.class);
				setContentViewMethod.invoke(handler, contentView.value());
			} catch (Throwable e) {
				GQZLog.debug(e.getMessage(), e);
			}
		}

		// inject view
		Field[] fields = handlerType.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				ViewInject viewInject = field.getAnnotation(ViewInject.class);
				if (viewInject != null) {
					try {
						View view = finder.findViewById(viewInject.value(),
								viewInject.parentId());
						if (view != null) {
							field.setAccessible(true);
							field.set(handler, view);
						}
					} catch (Throwable e) {
						GQZLog.debug(e.getMessage(), e);
					}
				} else {
					ResInject resInject = field.getAnnotation(ResInject.class);
					if (resInject != null) {
						try {
							Object res = ResLoader.loadRes(resInject.type(),
									finder.getContext(), resInject.id());
							if (res != null) {
								field.setAccessible(true);
								field.set(handler, res);
							}
						} catch (Throwable e) {
							GQZLog.debug(e.getMessage(), e);
						}
					} else {
						PreferenceInject preferenceInject = field
								.getAnnotation(PreferenceInject.class);
						if (preferenceInject != null) {
							try {
								Preference preference = finder
										.findPreference(preferenceInject
												.value());
								if (preference != null) {
									field.setAccessible(true);
									field.set(handler, preference);
								}
							} catch (Throwable e) {
								GQZLog.debug(e.getMessage(), e);
							}
						}
					}
				}
			}
		}

		// inject event
		Method[] methods = handlerType.getDeclaredMethods();
		if (methods != null && methods.length > 0) {
			for (Method method : methods) {
				Annotation[] annotations = method.getDeclaredAnnotations();
				if (annotations != null && annotations.length > 0) {
					for (Annotation annotation : annotations) {
						Class<?> annType = annotation.annotationType();
						if (annType.getAnnotation(EventBase.class) != null) {
							method.setAccessible(true);
							try {
								// ProGuard：-keep class * extends
								// java.lang.annotation.Annotation { *; }
								Method valueMethod = annType
										.getDeclaredMethod("value");
								Method parentIdMethod = null;
								try {
									parentIdMethod = annType
											.getDeclaredMethod("parentId");
								} catch (Throwable e) {
								}
								Object values = valueMethod.invoke(annotation);
								Object parentIds = parentIdMethod == null ? null
										: parentIdMethod.invoke(annotation);
								int parentIdsLen = parentIds == null ? 0
										: Array.getLength(parentIds);
								int len = Array.getLength(values);
								for (int i = 0; i < len; i++) {
									ViewInjectInfo info = new ViewInjectInfo();
									info.value = Array.get(values, i);
									info.parentId = parentIdsLen > i ? (Integer) Array
											.get(parentIds, i) : 0;
									EventListenerManager.addEventMethod(finder,
											info, annotation, handler, method);
								}
							} catch (Throwable e) {
								GQZLog.debug(e.getMessage(), e);
							}
						}
					}
				}
			}
		}
	}

}
