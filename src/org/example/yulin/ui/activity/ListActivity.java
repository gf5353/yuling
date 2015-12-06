package org.example.yulin.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.example.yulin.R;
import org.example.yulin.adapter.ChatListAdapter;
import org.example.yulin.bean.tuling.News;
import org.example.yulin.bean.tuling.Software;
import org.example.yulin.bean.tuling.Train;
import org.example.yulin.bean.tuling.TuLing;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@ContentView(R.layout.activity_news)
public class ListActivity extends ActionActivity {
	@ViewInject(R.id.listView)
	ListView listView;
	Intent intent;
	String json;

	@Override
	public void initData(Bundle savedInstanceState) {
		super.initData(savedInstanceState);
		setPageName("图灵列表");
		intent = getIntent();
		json = intent.getStringExtra("json");
		try {
			lTuLings = parsingJson(json);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		GQZInject.toast(lTuLings.size());
	}

	@Override
	public void initWidget() {
		super.initWidget();
	}

	JSONObject jsonObject;
	private static TuLing tLing;
	private static List<TuLing> lTuLings;
	private ChatListAdapter adapter;
	JSONArray jsonArray;

	private List<TuLing> parsingJson(String json) throws JSONException {
		jsonObject = new JSONObject(json);
		lTuLings = new ArrayList<TuLing>();
		int code = jsonObject.getInt("code");
		switch (code) {
		case 100000:// 文字类
			break;
		case 200000:// 链接类
			break;
		case 302000:// 新闻
			setActionBarName("新闻");
			jsonArray = jsonObject.getJSONArray("list");
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				tLing = new News(jsonObject.getString("article"),
						jsonObject.getString("source"),
						jsonObject.getString("detailurl"),
						jsonObject.getString("icon"));
				lTuLings.add(tLing);
			}
			adapter = new ChatListAdapter(this, lTuLings);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					News news = (News) lTuLings.get(arg2);
					Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(news.getDetailurl()));
					startActivity(rateIntent);
				}
			});
			break;
		case 304000:// 软件下载
			setActionBarName("软件下载");
			jsonArray = jsonObject.getJSONArray("list");
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				tLing = new Software(jsonObject.getString("name"),
						jsonObject.getString("count"),
						jsonObject.getString("detailurl"),
						jsonObject.getString("icon"));
				lTuLings.add(tLing);
			}
			adapter = new ChatListAdapter(this, lTuLings);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Software software = (Software) lTuLings.get(arg2);
					Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(software.getDetailurl()));
					startActivity(rateIntent);
				}
			});
			break;
		case 305000:// 列车
			setActionBarName("列车时刻表");
			jsonArray = jsonObject.getJSONArray("list");
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				tLing = new Train(jsonObject.getString("trainnum"),
						jsonObject.getString("start"),
						jsonObject.getString("terminal"),
						jsonObject.getString("starttime"),
						jsonObject.getString("endtime"),
						jsonObject.getString("detailurl"),
						jsonObject.getString("icon"));
				lTuLings.add(tLing);
			}
			adapter = new ChatListAdapter(this, lTuLings);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Train software = (Train) lTuLings.get(arg2);
					Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(software.getDetailurl()));
					startActivity(rateIntent);
				}
			});
			break;
		case 306000:// 航班
			break;
		case 308000:// 电影
			break;
		case 309000:// 酒店
			break;
		case 311000:// 价格
			break;
		default:
			break;
		}
		return lTuLings;
	}

}
