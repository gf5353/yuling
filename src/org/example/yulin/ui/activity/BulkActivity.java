package org.example.yulin.ui.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.yulin.AppConfig;
import org.example.yulin.Constants;
import org.example.yulin.R;
import org.example.yulin.adapter.GridAdapter;
import org.example.yulin.adapter.GridAdapter.OnGridClickListener;
import org.example.yulin.adapter.ParentAdapter;
import org.example.yulin.adapter.TodayGoodsAdapter;
import org.example.yulin.bean.BaseItem;
import org.example.yulin.bean.GridItem;
import org.example.yulin.bean.Holder;
import org.example.yulin.bean.dianping.Deals;
import org.example.yulin.ui.gridview.PageControl;
import org.example.yulin.util.ACache;
import org.example.yulin.util.DianPingUtil;
import org.example.yulin.util.DianPingUtil.DianPingListener;
import org.example.yulin.util.Json;
import org.example.yulin.util.TimeUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.GQZSharepf;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;
import org.mobile.gqz.ui.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

/**
 * 团购
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.activity_bulk)
public class BulkActivity extends ActionActivity implements DianPingListener,
		OnItemClickListener {

	@ViewInject(R.id.viewpager_bulk)
	private ViewPager viewPager;
	@ViewInject(R.id.viewGroup)
	private ViewGroup group;
	@ViewInject(R.id.list_bulk)
	ListView list_bulk;
	TodayGoodsAdapter listAdapter;
	PageControl pageControl;
	private ACache aCache;
	@ViewInject(R.id.scroll_bulk)
	ScrollView scroll_bulk;
 
	@ViewInject(R.id.p_bulk)
	ProgressBar p_bulk;

	@OnClick(value = { R.id.btn_bulk_more })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_bulk_more:
			if (Constants.dianpingdayIds != null) {
				showActivity(this, MoreActivity.class);
			}
			break;
		default:
			break;
		}
	};

	@Override
	public void initData(Bundle savedInstanceState) {
		super.initData(savedInstanceState);
		setPageName("团购");
		aCache = ACache.get(this);
		DianPingUtil.getInstance(this).setDianPingListener(this);
		setActionBarName("吃喝玩乐");
	}

	private boolean refash = true;

	@Override
	public void initWidget() {
		super.initWidget();
		scroll_bulk.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (listAdapter != null && refash) {
					refash = false;
					setListViewHeightBasedOnChildren(list_bulk);
				}
				return false;
			}
		});
		initGridData();
		initList();
	}

	String dateKey;// 当日key
	String dateString = null;

	private void initList() {
		dateKey = GQZSharepf.get(Constants.SP_Address, "")
				+ Constants.Cache_Date_Key;// 当日key
		dateString = aCache.getAsString(dateKey);
		if (TextUtils.isEmpty(dateString)) {// 当日数据不存在时
			// 获取每日新增团购id
			String city = (String) GQZSharepf.get(Constants.SP_Address, "");
			if (!TextUtils.isEmpty(city)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("city", city);
				paramMap.put("date",
						TimeUtil.dateToString(new Date(), TimeUtil.FORMAT_DATE));
				DianPingUtil.doGet(AppConfig.DianPingUrlGet_Daily_Wew_Id_List,
						paramMap);
			}
		} else {
			onSuccess(AppConfig.DianPingUrlGet_Daily_Wew_Id_List, dateString);
		}
	}

	GridAdapter adapter;

	private void initGridData() {
		List<BaseItem> lists = new ArrayList<BaseItem>();
		for (int i = 0; i < DianPingUtil.types.length; i++) {
			lists.add(new GridItem(DianPingUtil.resIds[i],
					DianPingUtil.types[i], i));
		}
		adapter = new GridAdapter(this, lists, 8, 4);
		adapter.setOnGridClickListener(new OnGridClickListener() {

			@Override
			public void onItemClick(int i) {
				Intent intent = new Intent(BulkActivity.this,
						SearchActivity.class);
				intent.putExtra("type", adapter.getLists().get(i).getResName());
				startActivity(intent);
			}

			Holder holder;

			@Override
			public View createView(LayoutInflater mInflater, int position,
					View convertView, ViewGroup parent) {
				GridItem item = (GridItem) adapter.getLists().get(position);
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.item_grid, null);
					holder = new Holder();
					convertView.setTag(holder);
				} else {
					holder = (Holder) convertView.getTag();
				}
				holder.img1 = (ImageView) convertView
						.findViewById(R.id.img_item_grid);
				holder.tv1 = (TextView) convertView
						.findViewById(R.id.tv_item_grid);

				holder.img1.setImageResource(item.getResId());
				holder.tv1.setText(item.getResName());
				return convertView;
			}
		});
		viewPager.setAdapter(adapter);
	}

	private List<BaseItem> deals = null;

	@Override
	public void onSuccess(String apiurl, String responseInfo) {
		if (apiurl.equals(AppConfig.DianPingUrlGet_Batch_Deals_By_Id)) {
			p_bulk.setVisibility(View.GONE);
			try {
				JSONObject daJsonObject = new JSONObject(responseInfo);
				JSONArray array = daJsonObject.getJSONArray("deals");
				int length = array.length();
				if (length > 0) {
					deals = new ArrayList<BaseItem>();
					for (int i = 0; i < array.length(); i++) {
						deals.add(new Deals(array.getJSONObject(i)));
					}
					listAdapter = new TodayGoodsAdapter(this, deals);
					list_bulk.setAdapter(listAdapter);
					list_bulk.setOnItemClickListener(this);
					scroll_bulk.smoothScrollTo(0, 0);
					setListViewHeightBasedOnChildren(list_bulk);
				} else {
					GQZInject.toast("该城市暂无新单");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (apiurl.equals(AppConfig.DianPingUrlGet_Daily_Wew_Id_List)) {
			if (TextUtils.isEmpty(dateString)) {
				aCache.put(dateKey, responseInfo, ACache.TIME_DAY);// 保存一天
			}
			try {
				Constants.dianpingdayIds = Json.arrayToStrings(new JSONObject(
						responseInfo).getJSONArray("id_list"));
				String page = DianPingUtil.page(Constants.dianpingdayIds, 0, 4);
				if (!TextUtils.isEmpty(page)) {
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("deal_ids", page);
					// 加载今日数据
					DianPingUtil.doGet(
							AppConfig.DianPingUrlGet_Batch_Deals_By_Id,
							paramMap);
				} else {
					GQZInject.toast("没有数据了");
					p_bulk.setVisibility(View.GONE);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onFailure(String apiurl, Exception error, String msg) {
		GQZInject.toast(msg);
	}

	private SearchView searchView;
	MenuItem item;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.city, menu);
		item = menu.findItem(R.id.menu_positioning);
		String city = (String) GQZSharepf.get(Constants.SP_Address, "");
		if (TextUtils.isEmpty(city)) {
			item.setTitle("选择城市");
		} else {
			item.setTitle(city);
		}

		// 获取SearchView对象
		searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		
		searchView.setIconifiedByDefault(true);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String type) {
				Intent intent = new Intent(BulkActivity.this,
						SearchActivity.class);
				intent.putExtra("type", type);
				startActivity(intent);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String arg0) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				// filterData(arg0);
				return true;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_positioning:
			startActivityForResult(new Intent(this, CityActivity.class), 1);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (item != null) {
				String city = (String) GQZSharepf.get(Constants.SP_Address, "");
				if (TextUtils.isEmpty(city)) {
					item.setTitle("选择城市");
				} else {
					item.setTitle(city);
				}
			}
			recreate();
			break;
		default:
			break;
		}
	}

	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
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
