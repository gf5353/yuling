package org.example.yulin.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.example.yulin.AppConfig;
import org.example.yulin.Constants;
import org.example.yulin.R;
import org.example.yulin.ui.activity.CityActivity;
import org.json.JSONException;
import org.mobile.gqz.GQZHttp;
import org.mobile.gqz.GQZLog;
import org.mobile.gqz.GQZSharepf;
import org.mobile.gqz.exception.HttpException;
import org.mobile.gqz.http.ResponseInfo;
import org.mobile.gqz.http.callback.RequestCallBack;
import org.mobile.gqz.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public class DianPingUtil {
	private static DianPingUtil dianPingUtil;
	Context context;
	private static GQZHttp http;
	private static DianPingListener dianPingListener;

	public void setDianPingListener(DianPingListener dianPingListener) {
		this.dianPingListener = dianPingListener;
	}

	public static String[] types = new String[] { "美食", "小吃快餐", "休闲娱乐", "看电影",
			"景点郊游", "ktv", "团购", "订外卖", "足疗按摩" };
	public static int[] resIds = new int[] { R.drawable.food_u,
			R.drawable.snack_u, R.drawable.relax, R.drawable.film_u,
			R.drawable.scenic, R.drawable.leisure_u, R.drawable.groupon_u,
			R.drawable.takeaway_u, R.drawable.footer };

	public interface DianPingListener {
		/**
		 * 
		 * @param apiUrl
		 * @param result
		 * @throws JSONException
		 */
		void onSuccess(String apiUrl, String result);

		void onFailure(String apiUrl, Exception error, String msg);
	}

	private DianPingUtil(Context context) {
		this.context = context;
		http = new GQZHttp();
	}

	public static DianPingUtil getInstance(Context context) {
		if (dianPingUtil == null) {
			dianPingUtil = new DianPingUtil(context);
		}
		return dianPingUtil;
	}

	public static void startUrl(Context aty, String deal_id, String url) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("dianping://tuandeal?id="
							+ URLEncoder.encode(deal_id, "UTF-8")));
			aty.startActivity(intent);
		} catch (Exception e) {
			// 没有安装应用，默认打开HTML5站
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			aty.startActivity(intent);
		}
	}

	static String appKey = AppConfig.DianPingAppKey;
	static String secret = AppConfig.DianPingAppSecret;

	public static String doGet(final String apiUrl, Map<String, Object> paramMap) {
		String queryString = getQueryString(appKey, secret, paramMap);
		GQZLog.debug("dianping:"+queryString);
		try {
			queryString = URIUtil.encodeQuery(queryString, "UTF-8");
			GQZLog.debugLog(DianPingUtil.class.getCanonicalName(), queryString);
		} catch (URIException e) {
			e.printStackTrace();
		}
		if (dianPingListener != null) {
			http.send(HttpMethod.GET, apiUrl + "?" + queryString,
					new RequestCallBack<String>() {

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							dianPingListener.onSuccess(apiUrl,
									responseInfo.result);
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							dianPingListener.onFailure(apiUrl, error, msg);
						}
					});
		}
		return queryString;
	}

	/**
	 * 获取请求字符串
	 * 
	 * @param appKey
	 * @param secret
	 * @param paramMap
	 * @return
	 */
	public static String getQueryString(String appKey, String secret,
			Map<String, Object> paramMap) {
		String sign = sign(appKey, secret, paramMap);

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("appkey=").append(appKey).append("&sign=")
				.append(sign);
		for (Entry<String, Object> entry : paramMap.entrySet()) {
			stringBuilder.append('&').append(entry.getKey()).append('=')
					.append(entry.getValue());
		}
		String queryString = stringBuilder.toString();
		return queryString;
	}

	/**
	 * 获取请求字符串，参数值进行UTF-8处理
	 * 
	 * @param appKey
	 * @param secret
	 * @param paramMap
	 * @return
	 */
	public static String getUrlEncodedQueryString(String appKey, String secret,
			Map<String, Object> paramMap) {
		String sign = sign(appKey, secret, paramMap);

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("appkey=").append(appKey).append("&sign=")
				.append(sign);
		for (Entry<String, Object> entry : paramMap.entrySet()) {
			try {
				stringBuilder.append('&').append(entry.getKey()).append('=')
						.append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
		}
		String queryString = stringBuilder.toString();
		return queryString;
	}

	/**
	 * 签名
	 * 
	 * @param appKey
	 * @param secret
	 * @param paramMap
	 * @return
	 */
	public static String sign(String appKey, String secret,
			Map<String, Object> paramMap) {
		// 参数名排序
		String[] keyArray = paramMap.keySet().toArray(new String[0]);
		Arrays.sort(keyArray);

		// 拼接参数
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(appKey);
		for (String key : keyArray) {
			stringBuilder.append(key).append(paramMap.get(key));
		}

		stringBuilder.append(secret);
		String codes = stringBuilder.toString();

		// SHA-1签名
		// For Android
		String sign = new String(Hex.encodeHex(DigestUtils.sha(codes)))
				.toUpperCase();
		return sign;
	}

	/**
	 * 判断城市
	 * 
	 * @param aty
	 * @return
	 */
	public static String isCity(Activity aty) {
		String city = (String) GQZSharepf.get(Constants.SP_Address, "");
		if (TextUtils.isEmpty(city)) {
			aty.startActivity(new Intent(aty, CityActivity.class));
		}
		return city;
	}

	public static String page(String[] data, int page, int count) {
		int length = data.length;// 总长度
		int rLength = length - page * count;// 剩余长度

		GQZLog.debug("length:" + length + "rLength:" + rLength);
		String pages = null;
		if (rLength > 0) {
			pages = data[page * count];// 初始化第一条
			if (rLength >= count) {// 剩余条数大于刷新条数
				for (int i = page * count + 1; i < length
						&& i <= (page * count + count); i++) {
					pages += "," + data[i];
					GQZLog.debug("page:" + i);
				}
			} else {
				GQZLog.debug("setpage1:" + pages);
				for (int i = page * count + 1; i < rLength; i++) {
					pages += "," + data[i];
					GQZLog.debug("page:" + i);
				}
				GQZLog.debug("setpage2:" + pages);
			}

			return pages;
		} else {
			return null;// 到底部
		}
	}
}
