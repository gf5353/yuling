package org.example.yulin.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.GQZLog;

import android.content.Context;

import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

public class JuHeUtil {
	private JuHeUtil() {

	}

	private JuHeUtil(Context context) {
		this.context = context;
	}

	public static JuHeListener juHeListener;

	public void setJuHeListener(JuHeListener juHeListener) {
		this.juHeListener = juHeListener;
	}

	public interface JuHeListener {
		void onSuccess(String result);

		/**
		 * 
		 * @param err
		 *            错误码
		 * @param reason
		 *            原因
		 */
		void onFail(int err, String reason);

	}

	private Context context;
	private static JuHeUtil juhe = null;

	public static JuHeUtil getInstance(Context context) {
		if (juhe == null) {
			juhe = new JuHeUtil(context);
		}
		return juhe;
	}

	public static JuHeUtil sendMsg(int did, String uri, String method,
			Parameters params) {
		JuheData.executeWithAPI(did, uri, method, params, new DataCallBack() {

			/**
			 * @param err
			 *            错误码,0为成功
			 * @param reason
			 *            原因
			 * @param result
			 *            数据
			 */
			@Override
			public void resultLoaded(int err, String reason, String result) {
				if (juHeListener != null) {
					if (err == 0) {// 成功
						try {
							GQZLog.debug(result);
							JSONObject jsonObject = new JSONObject(result);
							if (jsonObject.getString("resultcode")
									.equals("200")) {
								juHeListener.onSuccess(result);
							} else {
								juHeListener.onFail(err,
										jsonObject.getString("reason"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {// 失败
						juHeListener.onFail(err, reason);
					}
				}
			}
		});
		return juhe;
	}
}
