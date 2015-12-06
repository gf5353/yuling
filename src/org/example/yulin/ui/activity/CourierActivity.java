package org.example.yulin.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.example.yulin.R;
import org.example.yulin.adapter.CourierAdapter;
import org.example.yulin.bean.BaseItem;
import org.example.yulin.bean.juhe.Courier;
import org.example.yulin.bean.juhe.Trip;
import org.example.yulin.util.JuHeUtil;
import org.example.yulin.util.JuHeUtil.JuHeListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.GQZSharepf;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;
import org.mobile.gqz.ui.annotation.event.OnClick;
import org.mobile.gqz.utils.SystemTool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

@ContentView(R.layout.activity_courier)
public class CourierActivity extends ActionActivity {

	private String url = "http://v.juhe.cn/exp/index";
	@ViewInject(R.id.et_courier)
	EditText et_courier;
	@ViewInject(R.id.tv_courier_name)
	TextView tv_courier_name;
	@ViewInject(R.id.ly_courier)
	RelativeLayout ly_courier;
	@ViewInject(R.id.list_courier)
	ListView list_courier;

	List<BaseItem> lists;

	@OnClick(value = { R.id.btn_courier, R.id.ly_courier })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_courier://
			if (courier != null) {
				no = et_courier.getText().toString();
				if (!TextUtils.isEmpty(no)) {
					Parameters params = new Parameters();
					params.add("com", courier.getNo());
					params.add("no", no);
					p.show();
					SystemTool.closeKeybord(et_courier, this);// 关闭软键盘
					JuHeUtil.sendMsg(43, url, JuheData.GET, params)
							.setJuHeListener(new JuHeListener() {

								@Override
								public void onSuccess(String result) {
									GQZSharepf.put(
											CourierActivity.class.getName()
													+ "no", no);
									p.setTitle("查询完毕");
									try {
										JSONObject jsonObject = new JSONObject(
												result);
										jsonObject = jsonObject
												.getJSONObject("result");
										JSONArray jsonArray = jsonObject
												.getJSONArray("list");
										lists = new ArrayList<BaseItem>();
										int length = jsonArray.length();
										if (length > 0) {
											for (int i = 0; i < length; i++) {
												lists.add(new Trip(jsonArray
														.getJSONObject(i)));
											}
											list_courier
													.setAdapter(new CourierAdapter(
															getBaseContext(),
															lists));
										}
										p.dismiss();
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

				} else {
					GQZInject.toast("请输入正确的运单号");
				}
			} else {
				GQZInject.toast("请先选择快递公司");
			}
			break;
		case R.id.ly_courier:
			startActivityForResult(new Intent(this, SelectActivity.class), 1);
			break;

		default:
			break;
		}

	};

	ProgressDialog p;
	String no;

	@Override
	public void initData(Bundle savedInstanceState) {
		super.initData(savedInstanceState);
		setPageName("快递查询");
		setActionBarName("快递查询");
		JuHeUtil.getInstance(this);
		no = (String) GQZSharepf
				.get(CourierActivity.class.getName() + "no", "");
		if (!TextUtils.isEmpty(no)) {
			et_courier.setText(no);
			et_courier.setSelection(no.length());
		}
		getWindow()
				.setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
								| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
		p = GQZInject.getprogress(this, "正在加载...", true);
	}

	@Override
	public void initWidget() {
		super.initWidget();
	}

	Courier courier;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (data != null) {
				courier = (Courier) data.getSerializableExtra("courier");
				tv_courier_name.setText(courier.getCom() + "\t");
			}
			break;

		default:
			break;
		}

	}
}
