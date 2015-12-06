package org.example.yulin.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.yulin.AppConfig;
import org.example.yulin.R;
import org.example.yulin.adapter.ParentAdapter;
import org.example.yulin.adapter.TodayGoodsAdapter;
import org.example.yulin.bean.BaseItem;
import org.example.yulin.bean.dianping.Deals;
import org.example.yulin.ui.refresh.PullToRefreshView;
import org.example.yulin.ui.refresh.PullToRefreshView.OnFooterRefreshListener;
import org.example.yulin.ui.refresh.PullToRefreshView.OnHeaderRefreshListener;
import org.example.yulin.util.DianPingUtil;
import org.example.yulin.util.TimeUtil;
import org.example.yulin.util.DianPingUtil.DianPingListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.GQZLog;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@ContentView(R.layout.activity_search)
public class SearchActivity extends ActionActivity implements DianPingListener,
		OnHeaderRefreshListener, OnFooterRefreshListener, OnItemClickListener {
	ProgressDialog p;
	String type;
	@ViewInject(R.id.pull_search)
	PullToRefreshView pull_search;
	@ViewInject(R.id.list_search)
	ListView list_search;

	@Override
	public void initData(Bundle savedInstanceState) {
		super.initData(savedInstanceState);
		setPageName("团购搜索");
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		if (!TextUtils.isEmpty(type)) {
			setActionBarName(type);
			DianPingUtil.getInstance(this).setDianPingListener(this);
			p = GQZInject.getprogress(this, "正在加载..", true);
			p.show();
			sendSearch(page);
		}
	}

	@Override
	public void initWidget() {
		super.initWidget();
		pull_search.setOnHeaderRefreshListener(this);
		pull_search.setOnFooterRefreshListener(this);
	}

	int page = 1;

	private void sendSearch(int i) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String city = DianPingUtil.isCity(this);
		if (!TextUtils.isEmpty(city)) {
			paramMap.put("city", city);
			// paramMap.put("destination_city", city);
			paramMap.put("keyword", type);
			paramMap.put("page", i);
			// paramMap.put("latitude", Constants.latLng.latitude);
			// paramMap.put("longitude", Constants.latLng.longitude);
			// paramMap.put("radius", 5000);//搜索半径，单位为米，最小值1，最大值5000，如不传入默认为1000
			paramMap.put("sort", 4);// 结果排序，1:默认，2:价格低优先，3:价格高优先，4:购买人数多优先，5:最新发布优先，6:即将结束优先，7:离经纬度坐标距离近优先
			paramMap.put("limit", 10);// 每页返回的团单结果条目数上限，最小值1，最大值40，如不传入默认为20
			DianPingUtil.doGet(AppConfig.DianPingUrlGet_Find_Deals, paramMap);
		}
	}

	private List<BaseItem> deals = null;
	TodayGoodsAdapter adapter = null;

	@Override
	public void onSuccess(String apiUrl, String result) {
		GQZLog.debug(result);
		if (p.isShowing()) {
			p.dismiss();
		}
		pull_search.setVisibility(View.VISIBLE);
		try {
			JSONObject daJsonObject = new JSONObject(result);
			JSONArray array = daJsonObject.getJSONArray("deals");
			if (adapter == null || page == 2) {
				deals = new ArrayList<BaseItem>();
				for (int i = 0; i < array.length(); i++) {
					deals.add(new Deals(array.getJSONObject(i)));
				}
				adapter = new TodayGoodsAdapter(this, deals);
				list_search.setAdapter(adapter);
				list_search.setOnItemClickListener(this);
			} else {
				for (int i = 0; i < array.length(); i++) {
					deals.add(new Deals(array.getJSONObject(i)));
				}
				adapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFailure(String apiUrl, Exception error, String msg) {
		GQZInject.toast(msg);
		p.dismiss();
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		pull_search.postDelayed(new Runnable() {

			@Override
			public void run() {
				sendSearch(page++);
				pull_search.onFooterRefreshComplete();
			}
		}, 1000);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		pull_search.postDelayed(new Runnable() {

			@Override
			public void run() {
				page = 1;
				sendSearch(page++);
				pull_search.onHeaderRefreshComplete(TimeUtil
						.getCurrentTime(TimeUtil.FORMAT_DATE2_TIME));
				pull_search.onHeaderRefreshComplete();
			}
		}, 1000);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Deals deals = (Deals) ((ParentAdapter) arg0.getAdapter()).getLists()
				.get(arg2);
		Intent intent = new Intent(this, DealsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("deals", deals);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
