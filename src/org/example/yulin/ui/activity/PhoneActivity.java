package org.example.yulin.ui.activity;

import org.example.yulin.R;
import org.example.yulin.bean.juhe.Phone;
import org.example.yulin.util.JuHeUtil;
import org.example.yulin.util.JuHeUtil.JuHeListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;
import org.mobile.gqz.ui.annotation.event.OnClick;
import org.mobile.gqz.utils.SystemTool;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

/**
 * 手机归属地查询
 * 
 * @author Administrator
 *
 */
@ContentView(R.layout.activity_phone)
public class PhoneActivity extends ActionActivity implements JuHeListener {
	private String url = "http://apis.juhe.cn/mobile/get";

	@ViewInject(R.id.et_phone)
	EditText et_phone;
	@ViewInject(R.id.tv_phone)
	TextView tv_phone;

	@OnClick(value = { R.id.btn_phone })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_phone:// 1876167/ 0709
			String strPhone = et_phone.getText().toString();
			if (!TextUtils.isEmpty(strPhone) && strPhone.length() > 7
					&& strPhone.length() <= 11) {
				int phone = (int) (Long.valueOf(strPhone) / 10);
				Parameters params = new Parameters();
				params.add("phone", phone);
				p.show();
				JuHeUtil.sendMsg(11, url, JuheData.GET, params);
				SystemTool.closeKeybord(et_phone, this);//关闭软键盘
			} else {
				GQZInject.toast("请输入正确的手机号");
			}
			break;

		default:
			break;
		}

	};

	ProgressDialog p;

	@Override
	public void initWidget() {
		super.initWidget();
		setPageName("手机归属地查询");
		setActionBarName("手机归属地查询");
		JuHeUtil.getInstance(this).setJuHeListener(this);
		p = GQZInject.getprogress(this, "正在查询...", false);
	}

	@Override
	public void onSuccess(String result) {
		p.setTitle("查询完毕");
		try {
			JSONObject jsonObject = new JSONObject(result);
			Phone phone = new Phone(jsonObject);
			p.dismiss();
			tv_phone.setText(phone.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFail(int err, String reason) {
		p.dismiss();
		tv_phone.setText("");
		if (reason.equals("Empty")) {
			GQZInject.toast("号码不存在");
		} else {
			GQZInject.toast(reason);
		}

	}

}
