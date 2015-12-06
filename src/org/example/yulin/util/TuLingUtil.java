package org.example.yulin.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.example.yulin.AppConfig;
import org.example.yulin.R;
import org.example.yulin.bean.tuling.ChatMessage;
import org.example.yulin.bean.tuling.News;
import org.example.yulin.bean.tuling.TuLing;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.gqz.GQZHttp;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.exception.HttpException;
import org.mobile.gqz.http.ResponseInfo;
import org.mobile.gqz.http.callback.RequestCallBack;
import org.mobile.gqz.http.client.HttpRequest.HttpMethod;
import org.mobile.gqz.utils.SystemTool;

import android.content.Context;

public class TuLingUtil {

	private TuLingUtil() {

	}

	private Context context;

	private TuLingUtil(Context context) {
		this.context = context;
		http = new GQZHttp();
	}

	public interface OnTuLingListener {
		/**
		 * 图灵机器人返回数据
		 * 
		 * @param msg
		 */
		void onReceive(String msg);
	}

	private static OnTuLingListener lingListener;

	public TuLingUtil setLingListener(OnTuLingListener lingListener) {
		this.lingListener = lingListener;
		return tuLingUtil;
	}

	private static TuLingUtil tuLingUtil;

	public static TuLingUtil getInstance(Context context) {
		if (tuLingUtil == null) {
			tuLingUtil = new TuLingUtil(context);
		}
		return tuLingUtil;
	}

	private ChatMessage msgMessage = null;

	public void sendMsg(String msg) {
		if (SystemTool.checkNet(context)) {
			String url = setParams(msg);
			doGet(url);
		} else {
			GQZInject.toast(R.string.checkNet);
		}
	}

	/**
	 * 拼接Url
	 * 
	 * @param msg
	 * @return
	 */
	private static String setParams(String msg) {
		try {
			msg = URLEncoder.encode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return AppConfig.TuLingUrl + "?key=" + AppConfig.TuLingApiKey
				+ "&info=" + msg;
	}

	private static GQZHttp http;

	private static void doGet(String url) {
		http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (lingListener != null) {
					lingListener.onReceive(responseInfo.result);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				GQZInject.toast(R.string.checkNet);
			}
		});
	}

}
