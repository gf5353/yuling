package org.example.yulin.ui.activity;

import org.example.yulin.R;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

@ContentView(R.layout.activity_web)
public class WebActivity extends ActionActivity {
	@ViewInject(R.id.webView)
	WebView webView;
	Intent intent;
	Bundle bundle;
	String name, url;

	@Override
	public void initData(Bundle savedInstanceState) {
		super.initData(savedInstanceState);
		intent = getIntent();
		bundle = intent.getExtras();
		name = bundle.getString("name");
		setActionBarName(name);
		url = bundle.getString("url");
		// 设置WebView属性，能够执行Javascript脚本
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);
	}
}
