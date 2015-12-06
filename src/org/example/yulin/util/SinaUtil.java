package org.example.yulin.util;

import org.example.yulin.AppConfig;
import org.example.yulin.bean.sina.User;
import org.example.yulin.bean.sina.UsersAPI;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.ui.activity.GQZActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class SinaUtil {
	private SinaUtil() {

	}

	public interface SinaInfoListener {
		void returnSinaInfo(User user);
		void onCancel();
	}

	private static SinaInfoListener sinaInfoListener;

	public void setSinaInfoListener(SinaInfoListener sinaInfoListener) {
		this.sinaInfoListener = sinaInfoListener;
	}

	private static SinaUtil sinaUtil;
	private static AuthInfo mAuthInfo;

	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	private static Oauth2AccessToken mAccessToken;

	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	public static SsoHandler mSsoHandler;
	/** 用户信息接口 */
	private static UsersAPI mUsersAPI;

	public static SinaUtil loginInstance(final Context context) {
		if (sinaUtil == null) {
			sinaUtil = new SinaUtil();
		}
		// 创建微博实例
		// mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY,
		// Constants.REDIRECT_URL, Constants.SCOPE);
		// 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
		mAuthInfo = new AuthInfo(context, AppConfig.SinaAppKEY,
				AppConfig.REDIRECT_URL, AppConfig.SCOPE);
		mSsoHandler = new SsoHandler((GQZActivity) context, mAuthInfo);

		mSsoHandler.authorize(new WeiboAuthListener() {

			@Override
			public void onWeiboException(WeiboException e) {
				GQZInject.toast(e.getMessage());
			}

			@Override
			public void onComplete(Bundle values) {
				// 从 Bundle 中解析 Token
				mAccessToken = Oauth2AccessToken.parseAccessToken(values);

				if (mAccessToken.isSessionValid() && sinaInfoListener != null) {

					// 获取用户信息接口
					mUsersAPI = new UsersAPI(context, AppConfig.SinaAppKEY,
							mAccessToken);

					long uid = Long.parseLong(mAccessToken.getUid());
					mUsersAPI.show(uid, new RequestListener() {

						@Override
						public void onWeiboException(WeiboException e) {
							GQZInject.toast(e.getMessage());
						}

						@Override
						public void onComplete(String response) {
							sinaInfoListener.returnSinaInfo(User
									.parse(response));
						}
					});
				} else {
					// 以下几种情况，您会收到 Code：
					// 1. 当您未在平台上注册的应用程序的包名与签名时；
					// 2. 当您注册的应用程序包名与签名不正确时；
					// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
					String code = values.getString("code");
					String message = null;
					if (!TextUtils.isEmpty(code)) {
						message = message + "\nObtained the code: " + code;
					}
					Toast.makeText(context, message, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onCancel() {
				if (sinaInfoListener!=null) {
					sinaInfoListener.onCancel();
				}
//				GQZInject.toast("onCancel");
			}
		});
		return sinaUtil;
	}
}
