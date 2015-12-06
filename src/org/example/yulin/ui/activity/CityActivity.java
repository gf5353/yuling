package org.example.yulin.ui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import org.example.yulin.App;
import org.example.yulin.AppConfig;
import org.example.yulin.Constants;
import org.example.yulin.R;
import org.example.yulin.ui.view.city.CharacterParser;
import org.example.yulin.ui.view.city.MyLetterAlistView;
import org.example.yulin.ui.view.city.MyLetterAlistView.OnTouchingLetterChangedListener;
import org.example.yulin.ui.view.city.PinyinComparator;
import org.example.yulin.ui.view.city.SortAdapter;
import org.example.yulin.ui.view.city.SortModel;
import org.example.yulin.util.ACache;
import org.example.yulin.util.DianPingUtil;
import org.example.yulin.util.DianPingUtil.DianPingListener;
import org.example.yulin.util.MapUtil;
import org.example.yulin.util.MapUtil.OnMapLocationListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.GQZSharepf;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;
import org.mobile.gqz.utils.StringUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;

@ContentView(R.layout.activity_city)
public class CityActivity extends ActionActivity implements DianPingListener,
		OnMapLocationListener {

	// 城市列表
	@ViewInject(R.id.country_lvcountry)
	private ListView sortListView;
	// 右侧A-Z字母列表
	@ViewInject(R.id.cityLetterListView)
	private MyLetterAlistView letterListView;
	// dialog text
	private TextView overlay;
	// 估计是弹出dialog线程
	private OverlayThread overlayThread;
	// 城市Adapter
	private SortAdapter adapter;
	private Handler handler;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private SearchView searchView;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.city, menu);
		// 获取SearchView对象
		searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		searchView.setIconifiedByDefault(true);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				return true;
			}

			@Override
			public boolean onQueryTextChange(String arg0) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(arg0);
				return true;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_positioning:
			GQZInject.toast("正在定位");
			MapUtil.startLocation();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	String address;

	@Override
	public void initData(Bundle savedInstanceState) {
		super.initData(savedInstanceState);
		setPageName("城市选择");
		address = (String) GQZSharepf.get(Constants.SP_Address, "");
		if (!TextUtils.isEmpty(address)) {
			setActionBarName(address);
		} else {
			setActionBarName("选择城市");
		}
		DianPingUtil.getInstance(this).setDianPingListener(this);
	}

	ProgressDialog pDialog;
	ACache acache;
	String cityData = null;

	@Override
	public void initWidget() {
		super.initWidget();
		acache = ACache.get(this);
		MapUtil.getInstance(App.getContext()).setOnMapLocationListener(this);

		pDialog = GQZInject.getprogress(this, "正在载入..", true);
		handler = new Handler();
		overlayThread = new OverlayThread();
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		// 根据拼音排序
		pinyinComparator = new PinyinComparator();
		letterListView
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				if (adapter != null) {
					GQZSharepf.put(Constants.SP_Address,
							((SortModel) adapter.getItem(position)).getName());
					finish();
				}
			}
		});
		Map<String, Object> paramMap = new HashMap<String, Object>();
		pDialog.show();
		cityData = acache.getAsString(Constants.Cache_City_Data);
		if (TextUtils.isEmpty(cityData)) {
			DianPingUtil.doGet(AppConfig.DianPingUrlGetCitys, paramMap);
		} else {
			onSuccess(AppConfig.DianPingUrlGetCitys, cityData);
		}

	}

	// 右侧A-Z字母监听
	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			if (adapter != null) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
					overlay.setText(SourceDateList.get(position)
							.getSortLetters());
					overlay.setVisibility(View.VISIBLE);
					handler.removeCallbacks(overlayThread);
					// 延迟一秒后执行，让overlay为不可见
					handler.postDelayed(overlayThread, 1500);
				}
			}
		}
	}

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.item_city_overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	// 设置overlay不可见
	private class OverlayThread implements Runnable {

		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			// 汉字转换成拼音
			String pinyin = HanyuToPinyin(date[i]); // characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 汉字转拼音的方法
	 * 
	 * @param name
	 *            汉字
	 * @return 拼音
	 */
	private String HanyuToPinyin(String name) {
		String pinyinName = "";
		char[] nameChar = name.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(
							nameChar[i], defaultFormat)[0];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return pinyinName;
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		if (adapter != null) {
			List<SortModel> filterDateList = new ArrayList<SortModel>();
			if (TextUtils.isEmpty(filterStr)) {
				filterDateList = SourceDateList;
			} else {
				filterDateList.clear();
				for (SortModel sortModel : SourceDateList) {
					String name = sortModel.getName();
					if (name.indexOf(filterStr.toString()) != -1
							|| characterParser.getSelling(name).startsWith(
									filterStr.toString())) {
						filterDateList.add(sortModel);
					}
				}
			}
			// 根据a-z进行排序
			Collections.sort(filterDateList, pinyinComparator);
			adapter.updateListView(filterDateList);
		}
	}

	String[] data;

	@Override
	public void onSuccess(String apiurl, String responseInfo) {
		pDialog.dismiss();
		try {
			if (TextUtils.isEmpty(String.valueOf(GQZSharepf.get(
					Constants.SP_Address, "")))) {
				MapUtil.startLocation();
			}
			if (cityData == null) {
				acache.put(Constants.Cache_City_Data, responseInfo);
			}
			JSONObject jsonObject = new JSONObject(responseInfo);
			if (jsonObject.getString("status").equals("OK")) {
				JSONArray jsonArray = jsonObject.getJSONArray("cities");
				data = new String[jsonArray.length()];
				for (int i = 0; i < jsonArray.length(); i++) {
					data[i] = jsonArray.getString(i);
				}
				SourceDateList = filledData(data);
				// 根据a-z进行排序源数据
				Collections.sort(SourceDateList, pinyinComparator);
				adapter = new SortAdapter(this, SourceDateList);
				sortListView.setAdapter(adapter);
				initOverlay();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFailure(String apiurl, Exception error, String msg) {
		pDialog.dismiss();
		GQZInject.toast("网络不好请重试");
	}

	@Override
	public boolean onLocation(BDLocation location, LatLng latLng) {
		String city = location.getCity();
		if (!TextUtils.isEmpty(city)) {
			String cityString = StringUtils.containsAny(city, data);
			if (!TextUtils.isEmpty(cityString)) {
				setActionBarName("当前定位城市:" + cityString);
				GQZSharepf.put(Constants.SP_Address, cityString);
			} else {
				GQZInject.toast("定位失败");
			}
		} else {
			GQZInject.toast("定位失败");
		}
		return true;
	}
}
