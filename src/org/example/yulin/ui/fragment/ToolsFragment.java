package org.example.yulin.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.example.yulin.AppConfig;
import org.example.yulin.R;
import org.example.yulin.adapter.ToolsAdapter;
import org.example.yulin.bean.BaseItem;
import org.example.yulin.ui.activity.BulkActivity;
import org.example.yulin.ui.activity.CourierActivity;
import org.example.yulin.ui.activity.IdentityActivity;
import org.example.yulin.ui.activity.PhoneActivity;
import org.mobile.gqz.ui.annotation.ViewInject;
import org.mobile.gqz.ui.fragment.GQZFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.phkg.b.BannerView;

public class ToolsFragment extends GQZFragment implements OnItemClickListener {

	@Override
	protected View inflaterView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {
		return inflater.inflate(R.layout.fragment_tools, null);
	}

	@ViewInject(R.id.banner)
	BannerView banner;
	@ViewInject(R.id.grid_tools)
	GridView grid_tools;
	BaseAdapter adapter;
	List<BaseItem> lists;

	@Override
	protected void initData() {
		super.initData();
		lists = new ArrayList<BaseItem>();
		lists.add(new BaseItem(R.drawable.ic_launcher, "吃喝玩乐"));
		lists.add(new BaseItem(R.drawable.ic_launcher, "手机号码归属地"));
		lists.add(new BaseItem(R.drawable.ic_launcher, "身份证归属地查询"));
		lists.add(new BaseItem(R.drawable.ic_launcher, "快递查询"));
	}

	@Override
	protected void initWidget(View parentView) {
		super.initWidget(parentView);
		banner.showBanner(AppConfig.KuGuoId, AppConfig.KuGuoChannelId);
		adapter = new ToolsAdapter(getContext(), lists);
		grid_tools.setAdapter(adapter);
		grid_tools.setOnItemClickListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (banner != null) {
			banner.finishBanner();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg2) {
		case 0:
			context.startActivity(new Intent(context, BulkActivity.class));
			break;
		case 1:
			context.startActivity(new Intent(context, PhoneActivity.class));
			break;
		case 2:
			context.startActivity(new Intent(context, IdentityActivity.class));
			break;
		case 3:
			context.startActivity(new Intent(context,CourierActivity.class));
			break;
		default:
			break;
		}

	}
}
