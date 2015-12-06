package org.example.yulin.util;

import org.example.yulin.AppConfig;
import org.json.JSONObject;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.GQZLog;
import org.mobile.gqz.ui.activity.GQZActivity;

import android.content.Context;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQUtil {
	public static QQAuth mQQAuth;
	private static Tencent mTencent;
	private static UserInfo mInfo;

	public interface QQInfoListener {

		/**
		 * 
		 * @param login
		 *            {"ret":0, "pay_token":"FA27224BB2C812B60A2256AE8DEFF1EE"
		 *            ,"pf":"desktop_m_qq-10000144-android-2002-",
		 *            "query_authority_cost":734, "authority_cost":-79852190,
		 *            "openid":"535400F2B680BBB3AAD0CAA96F0400EA",
		 *            "expires_in":7776000,
		 *            "pfkey":"3a634f3161be4b249b55193d28c2b8b7", "msg":"",
		 *            "access_token":"D9C92DA0ACAE63422F096EC40D0B5A43",
		 *            "login_cost":1637}
		 * @param info
		 *            {"is_yellow_year_vip":"0", "ret":0, "figureurl_qq_1":
		 *            "http:\/\/q.qlogo.cn\/qqapp\/1103994876\/535400F2B680BBB3AAD0CAA96F0400EA\/40"
		 *            , "figureurl_qq_2":
		 *            "http:\/\/q.qlogo.cn\/qqapp\/1103994876\/535400F2B680BBB3AAD0CAA96F0400EA\/100"
		 *            , "nickname":"I Believe ❤ゞ", "yellow_vip_level":"0",
		 *            "is_lost":0, "msg":"", "city":"南京", "figureurl_1":
		 *            "http:\/\/qzapp.qlogo.cn\/qzapp\/1103994876\/535400F2B680BBB3AAD0CAA96F0400EA\/50"
		 *            , "vip":"0", "level":"0", "figureurl_2":
		 *            "http:\/\/qzapp.qlogo.cn\/qzapp\/1103994876\/535400F2B680BBB3AAD0CAA96F0400EA\/100"
		 *            , "province":"江苏", "is_yellow_vip":"0", "gender":"男"
		 *            ,"figureurl":
		 *            "http:\/\/qzapp.qlogo.cn\/qzapp\/1103994876\/535400F2B680BBB3AAD0CAA96F0400EA\/30"
		 *            }
		 */
		void returnQQInfo(JSONObject login, JSONObject info);

		void onCancel();
	}

	public static QQInfoListener qqListener;
	public static QQUtil qqUtil;

	public static void setQqListener(QQInfoListener qqListener) {
		QQUtil.qqListener = qqListener;
	}

	public static QQUtil loginInstance(final Context context) {
		qqUtil = new QQUtil();
		mQQAuth = QQAuth.createInstance(AppConfig.QQAppId, context);
		mTencent = Tencent.createInstance(AppConfig.QQAppId, context);
		if (mTencent != null) {
			mTencent.setOpenId(AppConfig.QQAppId);
			mTencent.login((GQZActivity) context, "all", new IUiListener() {

				@Override
				public void onError(UiError arg0) {
					if (qqListener != null) {
						qqListener.onCancel();
					}
				}

				@Override
				public void onComplete(final Object rsp) {
					if (QQUtil.qqListener != null) {
						mInfo = new UserInfo(context, mQQAuth.getQQToken());
						mInfo.getUserInfo(new IUiListener() {

							@Override
							public void onError(UiError arg0) {
								if (qqListener != null) {
									qqListener.onCancel();
								}
							}

							@Override
							public void onComplete(Object response) {
								QQUtil.qqListener
										.returnQQInfo((JSONObject) rsp,
												(JSONObject) response);
							}

							@Override
							public void onCancel() {
								if (qqListener != null) {
									qqListener.onCancel();
								}
							}
						});
					}
				}

				@Override
				public void onCancel() {
					if (qqListener != null) {
						qqListener.onCancel();
					}
				}
			});
		}
		return qqUtil;
	}

}
