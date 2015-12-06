package org.example.yulin.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.example.yulin.adapter.SelectAdapter;
import org.example.yulin.bean.BaseItem;
import org.example.yulin.bean.juhe.Courier;
import org.example.yulin.util.JuHeUtil;
import org.example.yulin.util.JuHeUtil.JuHeListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.gqz.GQZInject;

import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SelectActivity extends ActionActivity implements
		OnItemClickListener {

	ListView listView;
	ProgressDialog p;
	private String url_com = "http://v.juhe.cn/exp/com";// 快递公司列表
	List<BaseItem> lists = new ArrayList<BaseItem>();

	@Override
	public void initData(Bundle savedInstanceState) {
		super.initData(savedInstanceState);
		setPageName("快递选择");
		setActionBarName("选择快递公司");
		listView = new ListView(this);
		listView.setOnItemClickListener(this);
		setContentView(listView);
		JuHeUtil.getInstance(this);
		p = GQZInject.getprogress(this, "正在加载...", false);
		Parameters params = new Parameters();
		p.show();
		JuHeUtil.sendMsg(43, url_com, JuheData.GET, params).setJuHeListener(
				new JuHeListener() {

					@Override
					public void onSuccess(String result) {
						try {
							JSONObject jsonObject = new JSONObject(result);
							JSONArray jsonArray = jsonObject
									.getJSONArray("result");
							int length = jsonArray.length();
							if (length > 0) {
								for (int i = 0; i < length; i++) {
									Courier courier = new Courier(jsonArray
											.getJSONObject(i));
									lists.add(courier);
								}
								listView.setAdapter(new SelectAdapter(
										getBaseContext(), lists));
								p.dismiss();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFail(int err, String reason) {
						p.dismiss();
						GQZInject.toast(reason);
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Courier courier = (Courier) lists.get(arg2);
		Intent data = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("courier", courier);
		data.putExtras(bundle);
		setResult(RESULT_OK, data);
		finish();
	}

}
