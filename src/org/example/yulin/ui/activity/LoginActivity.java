package org.example.yulin.ui.activity;

import org.example.yulin.Constants;
import org.example.yulin.R;
import org.example.yulin.bean.bmob.BUser;
import org.example.yulin.bean.sina.User;
import org.example.yulin.ui.view.MenuLayout;
import org.example.yulin.util.QQUtil;
import org.example.yulin.util.QQUtil.QQInfoListener;
import org.example.yulin.util.SinaUtil;
import org.example.yulin.util.SinaUtil.SinaInfoListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.GQZLog;
import org.mobile.gqz.GQZSharepf;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.event.OnClick;

import com.umeng.analytics.MobclickAgent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.SaveListener;

@ContentView(R.layout.activity_login)
public class LoginActivity extends ActionActivity {

	ProgressDialog pDialog;

	@Override
	public void initWidget() {
		super.initWidget();
		setPageName("登录");
		setActionBarName("登录");
		pDialog = GQZInject.getprogress(this, "正在登录，请稍后...", false);
	}

	String nickname, url_head;

	@SuppressWarnings("static-access")
	@OnClick(value = { R.id.rippleView1, R.id.btn_sina })
	public void onclick(View v) {
		switch (v.getId()) {
		case R.id.rippleView1:
			pDialog.show();
			QQUtil.loginInstance(this).setQqListener(new QQInfoListener() {

				@Override
				public void returnQQInfo(JSONObject login, JSONObject info) {
					try {
						nickname = info.getString("nickname");
						url_head = info.getString("figureurl_qq_2");
						String openId = login.getString("openid");
						sava(openId, nickname, url_head);
						regist(openId);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onCancel() {
					pDialog.dismiss();
				}
			});
			break;
		case R.id.btn_sina:
			pDialog.show();
			SinaUtil.loginInstance(this).setSinaInfoListener(
					new SinaInfoListener() {

						@Override
						public void returnSinaInfo(User user) {
							GQZLog.debug(user.toString());
							nickname = user.screen_name;
							url_head = user.avatar_large;
							sava(user.id, nickname, url_head);
							regist(user.id);
						}

						@Override
						public void onCancel() {
							pDialog.dismiss();
						}
					});
			break;
		default:
			break;
		}
	}

	private void sava(String openId, String name, String headUrl) {
		GQZSharepf.put(Constants.SP_OpenId, openId);
		GQZSharepf.put(Constants.SP_NickName, name);
		GQZSharepf.put(Constants.SP_HeadUrl, headUrl);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (SinaUtil.mSsoHandler != null) {
			SinaUtil.mSsoHandler.authorizeCallBack(requestCode, resultCode,
					data);
		}
	}

	private void regist(final String openId) {
		BUser user = new BUser();
		user.setUsername(openId);
		user.setPassword(openId);
		user.setNickname(nickname);
		user.setUrl_head(url_head);
		if (Constants.latLng != null) {
			user.setGpsAdd(new BmobGeoPoint(Constants.latLng.longitude,
					Constants.latLng.latitude));
		}
		user.signUp(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// 统计注册人数
				MobclickAgent.onEvent(LoginActivity.this, "registered");
				login(openId);

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				login(openId);
			}
		});
	}

	private void login(final String openId) {
		BUser user = new BUser();
		user.setUsername(openId);
		user.setPassword(openId);
		user.login(this, new SaveListener() {

			@Override
			public void onSuccess() {
//				GQZInject.toast("登录成功");
				finish();
				pDialog.dismiss();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				finish();
				pDialog.dismiss();
			}
		});

	}

	@Override
	protected void onStop() {
		super.onStop();
		MenuLayout.changeData();
	}
}
