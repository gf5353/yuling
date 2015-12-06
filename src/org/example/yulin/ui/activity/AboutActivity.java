package org.example.yulin.ui.activity;

import org.example.yulin.AppConfig;
import org.example.yulin.R;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;
import org.mobile.gqz.ui.annotation.event.OnClick;
import org.mobile.gqz.utils.SystemTool;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

@ContentView(R.layout.activity_about)
public class AboutActivity extends ActionActivity {
	@ViewInject(R.id.tv_detection)
	TextView tv_detection;

	@ViewInject(R.id.tv_version)
	TextView tv_version;

	@Override
	public void initWidget() {
		super.initWidget();
		setPageName("关于页面");
		setActionBarName("关于我们");
		tv_version.setText(SystemTool.getVersionName(this));
		BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				if (updateStatus == UpdateStatus.Yes) {
					ur = updateInfo;
				} else if (updateStatus == UpdateStatus.IGNORED) {// 新增忽略版本更新
					GQZInject.toast("该版本已经被忽略更新");
				} else if (updateStatus == UpdateStatus.No) {
					tv_detection.setText("已是最新版本");
				}
			}
		});
	}

	UpdateResponse ur;

	@OnClick(value = { R.id.ly_update, R.id.ly_homepage })
	public void onclick(View v) {
		switch (v.getId()) {
		case R.id.ly_update:
			// 手动检查更新
			BmobUpdateAgent.forceUpdate(this);
			break;
		case R.id.ly_homepage:
			String appUrl = AppConfig.YuLingUrl;
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl));
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
