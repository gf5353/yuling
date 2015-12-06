package org.example.yulin.ui.activity;

import java.util.Date;

import org.example.yulin.R;
import org.example.yulin.bean.bmob.Opinion;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;
import org.mobile.gqz.ui.annotation.event.OnClick;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;

@ContentView(R.layout.activity_opinion)
public class OpinionActivity extends ActionActivity {
	@Override
	public void initWidget() {
		super.initWidget();
		setPageName("反馈意见");
		setActionBarName("意见反馈");
	}

	@ViewInject(R.id.et_email)
	EditText et_email;
	@ViewInject(R.id.et_opinion)
	EditText et_opinion;

	private String email, opinion;

	@OnClick(R.id.btn_opinion)
	public void onclick(View v) {
		switch (v.getId()) {
		case R.id.btn_opinion:// 提交意见
			email = et_email.getText().toString();
			opinion = et_opinion.getText().toString();
			if (!TextUtils.isEmpty(opinion)) {
				sava(email, opinion);
			} else {
				GQZInject.toast("请输入您的意见");
			}
			break;
		default:
			break;
		}
	}

	ProgressDialog pDialog;

	private void sava(String email2, String opinion2) {
		pDialog = GQZInject.getprogress(this, "正在提交，请稍后", false);
		Opinion opinion = new Opinion();
		opinion.setEmail(email2);
		opinion.setOpinion(opinion2);
		opinion.setDate(new BmobDate(new Date()));
		pDialog.show();
		opinion.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				GQZInject.toast("提交成功");
				pDialog.cancel();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				GQZInject.toast("提交失败：" + arg0 + "-" + arg1);
				pDialog.cancel();
			}
		});

	}
}
