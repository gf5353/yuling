package org.example.yulin.ui.activity;

import gqzf.gqza;

import org.example.yulin.AppConfig;
import org.example.yulin.Constants;
import org.example.yulin.R;
import org.example.yulin.adapter.MenuAdapter;
import org.example.yulin.ui.fragment.ToolsFragment;
import org.example.yulin.ui.fragment.YuLingFragment;
import org.example.yulin.ui.view.MenuLayout;
import org.example.yulin.ui.viewpager.indicator.FixedIndicatorView;
import org.example.yulin.ui.viewpager.indicator.IndicatorViewPager;
import org.example.yulin.ui.viewpager.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import org.example.yulin.ui.viewpager.indicator.IndicatorViewPager.IndicatorPagerAdapter;
import org.example.yulin.ui.viewpager.indicator.slidebar.ColorBar;
import org.example.yulin.ui.viewpager.transition.OnTransitionTextListener;
import org.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import org.ikimuhendis.ldrawer.DrawerArrowDrawable;
import org.mobile.gqz.GQZSharepf;
import org.mobile.gqz.ui.activity.GQZActivity;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;
import org.mobile.gqz.ui.fragment.GQZFragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import cn.bmob.v3.update.BmobUpdateAgent;

@ContentView(R.layout.activity_main)
public class MainActivity extends GQZActivity implements OnItemClickListener {
	@ViewInject(R.id.drawer_layout)
	private DrawerLayout mDrawerLayout;
	@ViewInject(R.id.navdrawer)
	private MenuLayout mDrawerList;
	@ViewInject(R.id.pager)
	ViewPager mViewPager;
	@ViewInject(R.id.tabmain_indicator)
	FixedIndicatorView indicator;

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerArrowDrawable drawerArrow;

	private void initActionBar() {
		setPageName("主界面");
		BmobUpdateAgent.setUpdateOnlyWifi(false);
		// 自动更新方法通常可以放在应用的启动页
		BmobUpdateAgent.update(this);
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		drawerArrow = new DrawerArrowDrawable(this) {
			@Override
			public boolean isLayoutRtl() {
				return false;
			}
		};
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				drawerArrow, R.string.drawer_open, R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();
		mDrawerList.setAdapter(
				new MenuAdapter(this, new int[] { R.drawable.ic_launcher,
						R.drawable.ic_launcher, R.drawable.ic_launcher,
						R.drawable.ic_launcher, R.drawable.ic_launcher,
						R.drawable.ic_launcher }, new String[] { "周边", "分享",
						"设置", "反馈意见", "请多支持 +_+", "关于我们" }))
				.setOnItemClickListener(this);
		mDrawerList.setOnHeadClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showActivity(MainActivity.this, LoginActivity.class);
				changeDrawer();// 针对原型来
			}
		});
		// mDrawerList.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// showActivity(MainActivity.this, CityActivity.class);
		// }
		// });
	}

	Intent intent;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg2) {
		case 0:
			intent = new Intent(this, NearActivity.class);
			startActivity(intent);
			break;
		case 1:
			// 分享的intent
			intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, "好友分享");
			// 自动添加的发送的具体信息
			intent.putExtra(Intent.EXTRA_TEXT, "我正在使用语灵，你也加入吧！！"
					+ AppConfig.YuLingUrl);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(Intent.createChooser(intent, getTitle()));
			break;
		case 2:
			showActivity(this, SettingActivity.class);
			break;
		case 3:
			showActivity(this, OpinionActivity.class);
			break;
		case 4:
			showActivity(Constants.url_pay);
			break;
		case 5:
			showActivity(this, AboutActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public void initWidget() {
		super.initWidget();
		initActionBar();
		initTab();
		String address = (String) GQZSharepf.get(Constants.SP_Address, "");
		if (TextUtils.isEmpty(address)) {
			showActivity(this, CityActivity.class);
		} else {

		}

	}

	private static final int DEFAULT_OFFSCREEN_PAGES = 2;
	private IndicatorViewPager indicatorViewPager;
	private LayoutInflater inflate;

	private void initTab() {
		indicator.setScrollBar(new ColorBar(getApplicationContext(),
				Color.WHITE, 5));
		float unSelectSize = 16;
		float selectSize = unSelectSize * 1.2f;
		Resources res = getResources();
		int selectColor = res.getColor(android.R.color.white);
		int unSelectColor = res.getColor(android.R.color.white);

		indicator.setOnTransitionListener(new OnTransitionTextListener(
				selectSize, unSelectSize, selectColor, unSelectColor));
		mViewPager.setOffscreenPageLimit(3);

		indicatorViewPager = new IndicatorViewPager(indicator, mViewPager);
		inflate = LayoutInflater.from(getApplicationContext());
		indicatorViewPager.setAdapter(adapter);
	}

	private void changeDrawer() {
		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			mDrawerLayout.openDrawer(mDrawerList);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			changeDrawer();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			changeDrawer();
			break;
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			// 发起推送广告请求

			// Ckm pm = Ckm.getInstance(this);// AppConfig.KuGuoId, "k-appoole"

			gqza g = gqza.getInstance();
			g.setId(this, AppConfig.KuGuoId, AppConfig.KuGuoChannelId);// cooid,channeiId/f946b3d4086249a6968aabec7c752027/
			g.receiveMessage(this, true);

			// // 设置cooId
			// pm.setCooId(this, AppConfig.KuGuoId);//
			// // 设置channelId
			// pm.setChannelId(this, AppConfig.KuGuoChannelId);
			// // 接收push
			// pm.receiveMessage(this, false);
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mDrawerList.changeData();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private IndicatorPagerAdapter adapter = new IndicatorFragmentPagerAdapter(
			getFragmentManager()) {

		@Override
		public int getCount() {
			return Constants.Tabs;
		}

		@Override
		public View getViewForTab(int position, View convertView,
				ViewGroup container) {
			if (convertView == null) {
				convertView = inflate.inflate(R.layout.tab_top, container,
						false);
				convertView.setBackgroundResource(R.color.actionbar_color);
			}
			TextView textView = (TextView) convertView;
			switch (position) {
			case 0:
				textView.setText("选择你的聊友");
				break;
			case 1:
				textView.setText("工具箱");
				break;

			default:
				break;
			}
			return convertView;
		}

		@Override
		public void PageSelected(int position) {
			if (bulk != null) {
				bulk.doRequest(position);
			}
		}

		GQZFragment yuling, tools, bulk;

		@Override
		public Fragment getFragmentForPage(int position) {
			switch (position) {
			case 0:
				if (yuling == null) {
					yuling = new YuLingFragment();
				}
				return yuling;
			case 1:
				if (tools == null) {
					tools = new ToolsFragment();
				}
				return tools;
			}
			return yuling;
		}

	};

}
