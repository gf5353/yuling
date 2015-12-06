package org.example.yulin.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.example.yulin.R;
import org.example.yulin.adapter.BussinessAdapter;
import org.example.yulin.adapter.DealsPagerAdapter;
import org.example.yulin.bean.BaseItem;
import org.example.yulin.bean.dianping.Businesses;
import org.example.yulin.bean.dianping.Deals;
import org.example.yulin.bean.dianping.Restrictions;
import org.example.yulin.ui.view.GScrollView;
import org.example.yulin.ui.view.GScrollView.OnPullListener;
import org.mobile.gqz.GQZLog;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@ContentView(R.layout.activity_deals)
public class DealsActivity extends ActionActivity implements OnPullListener {

	Deals deals;
	@ViewInject(R.id.scroll_deals)
	GScrollView scroll_deals;
	@ViewInject(R.id.ly_deals)
	LinearLayout ly_deals;
	private FrameLayout contentLayout;

	@Override
	public void initData(Bundle savedInstanceState) {
		super.initData(savedInstanceState);
		setPageName("商品详情");
		deals = (Deals) getIntent().getSerializableExtra("deals");
	}

	List<BaseItem> imgs;

	private void initViewPager() {
		imgs = new ArrayList<BaseItem>();
		if (deals.getMore_image_urls() != null) {// 多组图片时
			for (int i = 0; i < deals.getMore_image_urls().length; i++) {
				imgs.add(new BaseItem(i, deals.getMore_image_urls()[i]));
			}
		} else {// 没有图片组时
			GQZLog.debug(deals.getImage_url());
			imgs.add(new BaseItem(0, deals.getImage_url()));
		}

		viewpager_deals.setAdapter(new DealsPagerAdapter(imgs, this));
		tv_deals_description.setText(deals.getDescription());
		tv_viewpager_num.setText("1/" + imgs.size());
		viewpager_deals.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				tv_viewpager_num.setText(position + 1 + "/" + imgs.size());
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	ViewPager viewpager_deals;
	TextView tv_viewpager_num, tv_deals_description, tv_deals_price,
			tv_deals_price_old, tv_deals_price_down, tv_deals_price_old_down,
			tv_deals_num, tv_deals_overdue, tv_deals_anytime,
			tv_deals_appointment, tv_deals_special_tips;
	LinearLayout ly_deals_up, ly_deals_down, ly_deals_refund;
	Button btn_deals_down;
	ListView list_deals;

	private void findViewById(FrameLayout main) {
		viewpager_deals = (ViewPager) main.findViewById(R.id.viewpager_deals);
		tv_viewpager_num = (TextView) main.findViewById(R.id.tv_viewpager_num);
		tv_deals_description = (TextView) main
				.findViewById(R.id.tv_deals_description);
		ly_deals_up = (LinearLayout) main.findViewById(R.id.ly_deals_up);
		ly_deals_down = (LinearLayout) main.findViewById(R.id.ly_deals_down);
		ly_deals_refund = (LinearLayout) main
				.findViewById(R.id.ly_deals_refund);
		tv_deals_price = (TextView) main.findViewById(R.id.tv_deals_price);
		tv_deals_price_old = (TextView) main
				.findViewById(R.id.tv_deals_price_old);
		tv_deals_price_down = (TextView) main
				.findViewById(R.id.tv_deals_price_down);
		tv_deals_price_old_down = (TextView) main
				.findViewById(R.id.tv_deals_price_old_down);
		tv_deals_num = (TextView) main.findViewById(R.id.tv_deals_num);
		tv_deals_overdue = (TextView) main.findViewById(R.id.tv_deals_overdue);// 过期退
		tv_deals_anytime = (TextView) main.findViewById(R.id.tv_deals_anytime);// 随时退
		tv_deals_appointment = (TextView) main
				.findViewById(R.id.tv_deals_appointment);// 免预约
		tv_deals_special_tips = (TextView) main
				.findViewById(R.id.tv_deals_special_tips);// 购买须知

		list_deals = (ListView) main.findViewById(R.id.list_deals_businesses);

		main.findViewById(R.id.btn_deals_down).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						showActivity(deals.getDeal_h5_url());
					}
				});
	}

	@Override
	public void initWidget() {
		super.initWidget();
		contentLayout = (FrameLayout) getLayoutInflater().inflate(
				R.layout.layout_deals, null);
		findViewById(contentLayout);
		ly_deals.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						onScroll(scroll_deals.getScrollY());
					}
				});
		scroll_deals.addBodyView(contentLayout);
		scroll_deals.setOnPullListener(this);
		if (deals != null) {
			setActionBarName(deals.getTitle());
			initViewPager();
			tv_deals_price_down.setText("￥" + deals.getCurrent_price());
			tv_deals_price.setText("￥" + deals.getCurrent_price());

			tv_deals_price_old_down.setText("￥" + deals.getList_price());
			tv_deals_price_old_down.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG); // 中间横线
			tv_deals_price_old.setText("￥" + deals.getList_price());
			tv_deals_price_old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中间横线

			tv_deals_num.setText("已售" + deals.getPurchase_count());
			Restrictions restrictions = deals.getRestrictions();
			if (restrictions != null) {
				ly_deals_refund.setVisibility(View.VISIBLE);
				if (restrictions.getIs_reservation_required() == 1) {// 需要预约
					tv_deals_appointment.setVisibility(View.GONE);
				}
				if (restrictions.getIs_refundable() == 0) {// 不支持随时退
					tv_deals_anytime.setVisibility(View.GONE);
				}
				tv_deals_special_tips.setText(restrictions.getSpecial_tips());
			}
			List<Businesses> list = deals.getBusinesses();
			List<BaseItem> lists = new ArrayList<BaseItem>();
			for (int i = 0; i < list.size(); i++) {
				lists.add(list.get(i));
			}
			BussinessAdapter adapter = new BussinessAdapter(this, lists);
			list_deals.setAdapter(adapter);
			adapter.setListViewHeightBasedOnChildren(list_deals);
			scroll_deals.post(new Runnable() {

				@Override
				public void run() {
					scroll_deals.scrollTo(0, 0);
				}
			});
		}
	}

	@Override
	public void refresh() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				initViewPager();
				scroll_deals.setheaderViewReset();
			}
		}, 2000);
	}

	@Override
	public void loadMore() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				scroll_deals.setfooterViewReset();
			}
		}, 2000);
	}

	@Override
	public void onScroll(int scrollY) {
		int top = Math.max(scrollY, ly_deals_up.getTop());
		ly_deals_down.layout(0, top, ly_deals_down.getWidth(), top
				+ ly_deals_down.getHeight());
	}
}
