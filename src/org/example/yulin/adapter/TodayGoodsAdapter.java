package org.example.yulin.adapter;

import java.util.List;

import org.example.yulin.R;
import org.example.yulin.bean.BaseItem;
import org.example.yulin.bean.Holder;
import org.example.yulin.bean.dianping.Deals;
import org.mobile.gqz.GQZBitmap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TodayGoodsAdapter extends ParentAdapter {
	GQZBitmap gqzBitmap;

	public TodayGoodsAdapter(Context context, List<BaseItem> lists) {
		setLists(lists);
		setContext(context);
		gqzBitmap = new GQZBitmap(context);
		gqzBitmap.configDefaultLoadingImage(R.drawable.img_null);
		gqzBitmap.configDefaultLoadFailedImage(R.drawable.img_null);
	}

	Holder holder;

	@Override
	public View createView(int position, View convertView, ViewGroup parent) {
		Deals deals = (Deals) lists.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_today_goods, null);
			holder = new Holder();
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.img1 = (ImageView) convertView
				.findViewById(R.id.img_today_goods);
		holder.img2 = (ImageView) convertView.findViewById(R.id.img_today_type);
		holder.tv1 = (TextView) convertView
				.findViewById(R.id.tv_today_goods_title);
		holder.tv2 = (TextView) convertView
				.findViewById(R.id.tv_today_goods_price);
		holder.tv3 = (TextView) convertView
				.findViewById(R.id.tv_today_goods_details);
		holder.tv4 = (TextView) convertView
				.findViewById(R.id.tv_today_goods_sold_num);

		holder.tv1.setText(deals.getTitle());
		holder.tv2.setText("￥" + deals.getCurrent_price());
		holder.tv3.setText(deals.getDescription());
		holder.tv4.setText("已售:" + deals.getPurchase_count());
		if (deals.getIs_popular() == 1) {// 免预约
			holder.img2.setBackgroundResource(R.drawable.home_icon_hot);
		}
		gqzBitmap.display(holder.img1, deals.getS_image_url());
		return convertView;
	}

}
