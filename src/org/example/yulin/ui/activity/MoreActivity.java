package org.example.yulin.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.yulin.AppConfig;
import org.example.yulin.Constants;
import org.example.yulin.R;
import org.example.yulin.adapter.ParentAdapter;
import org.example.yulin.adapter.TodayGoodsAdapter;
import org.example.yulin.bean.BaseItem;
import org.example.yulin.bean.dianping.Deals;
import org.example.yulin.ui.refresh.PullToRefreshView;
import org.example.yulin.ui.refresh.PullToRefreshView.OnFooterRefreshListener;
import org.example.yulin.ui.refresh.PullToRefreshView.OnHeaderRefreshListener;
import org.example.yulin.util.DianPingUtil;
import org.example.yulin.util.DianPingUtil.DianPingListener;
import org.example.yulin.util.TimeUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 今日更多
 * 
 * @author admin
 * 
 */
@ContentView(R.layout.activity_more)
public class MoreActivity extends ActionActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener, DianPingListener,
		OnItemClickListener {
	@ViewInject(R.id.list_more)
	ListView list_more;
	@ViewInject(R.id.pull_more)
	PullToRefreshView pull_more;

	@Override
	public void initData(Bundle savedInstanceState) {
		super.initData(savedInstanceState);
		setPageName("进入更多");
		DianPingUtil.getInstance(this).setDianPingListener(this);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		setActionBarName("今日更多");
		pull_more.setOnHeaderRefreshListener(this);
		pull_more.setOnFooterRefreshListener(this);
		updata(page++);
	}

	int page = 0;// 初始页
	TodayGoodsAdapter adapter = null;

	private void updata(int i) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String StrPage = DianPingUtil.page(Constants.dianpingdayIds, i, 4);
		if (!TextUtils.isEmpty(StrPage)) {
			paramMap.put("deal_ids", StrPage);
			// 加载今日数据
			DianPingUtil.doGet(AppConfig.DianPingUrlGet_Batch_Deals_By_Id,
					paramMap);
		} else {
			page--;
			GQZInject.toast("到底了");
		}

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		pull_more.postDelayed(new Runnable() {

			@Override
			public void run() {
				updata(page++);
				pull_more.onFooterRefreshComplete();
			}
		}, 1000);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		pull_more.postDelayed(new Runnable() {

			@Override
			public void run() {
				page = 0;
				updata(page++);
				pull_more.onHeaderRefreshComplete(TimeUtil
						.getCurrentTime(TimeUtil.FORMAT_DATE2_TIME));
				pull_more.onHeaderRefreshComplete();
			}
		}, 1000);
	}

	private List<BaseItem> deals = null;

	@Override
	public void onSuccess(String apiUrl, String result) {
		try {
			JSONObject daJsonObject = new JSONObject(result);
			JSONArray array = daJsonObject.getJSONArray("deals");
			if (adapter == null || page == 1) {
				deals = new ArrayList<BaseItem>();
				for (int i = 0; i < array.length(); i++) {
					deals.add(new Deals(array.getJSONObject(i)));
				}
				adapter = new TodayGoodsAdapter(this, deals);
				list_more.setAdapter(adapter);
				list_more.setOnItemClickListener(this);
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
