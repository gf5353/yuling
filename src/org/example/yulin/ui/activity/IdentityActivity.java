package org.example.yulin.ui.activity;

import org.example.yulin.R;
import org.example.yulin.bean.juhe.Identity;
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
 * 身份证查询
 * 
 * @author admin
 *
 */
@ContentView(R.layout.activity_identity)
public class IdentityActivity extends ActionActivity implements JuHeListener {
	private String url = "http://apis.juhe.cn/idcard/index";

	@ViewInject(R.id.et_identity)
	EditText et_identity;
	@ViewInject(R.id.tv_identity)
	TextView tv_identity;

	@OnClick(value = { R.id.btn_identity })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_identity:
			String cardno = et_identity.getText().toString();
			if (!TextUtils.isEmpty(cardno)) {
				Parameters params = new Parameters();
				params.add("cardno", cardno);
				p.show();
				juhe.sendMsg(38, url, JuheData.GET, params);
				SystemTool.closeKeybord(et_identity, this);// 关闭软键盘
			} else {
				GQZInject.toast("请输入正确的身份证号");
			}
			break;

		default:
			break;
		}

	};

	JuHeUtil juhe;
	ProgressDialog p;

	@Override
	public void initWidget() {
		super.initWidget();
		setPageName("身份证查询");
		setActionBarName("身份证归属地查询");
		juhe = JuHeUtil.getInstance(this);
		juhe.setJuHeListener(this);
		p = GQZInject.getprogress(this, "正在查询...", false);
	}

	@Override
	public void onSuccess(String result) {
		p.setTitle("查询完毕");
		try {
			Identity identity = new Identity(new JSONObject(result));
			p.dismiss();
			tv_identity.setText(identity.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onFail(int err, String reason) {
		p.dismiss();
		tv_identity.setText("");
		GQZInject.toast(reason);
	}
}
