package org.example.yulin.adapter;

import java.util.List;

import org.example.yulin.R;
import org.example.yulin.bean.BaseItem;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.example.yulin.bean.Holder;
import org.example.yulin.bean.dianping.Businesses;
import org.example.yulin.ui.activity.MapForBussinessActivity;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.GQZLog;
import org.mobile.gqz.ui.activity.GQZActivity;

public class BussinessAdapter extends ParentAdapter {

	public BussinessAdapter(Context context, List<BaseItem> lists) {
		setContext(context);
		GQZLog.debug(lists.size() + "sieze");
		setLists(lists);
	}

	Holder holder;

	@Override
	public View createView(int position, View convertView, ViewGroup parent) {
		Businesses item = (Businesses) lists.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_business, null);
			holder = new Holder();
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.ly1 = (LinearLayout) convertView.findViewById(R.id.ly_business);
		holder.tv1 = (TextView) convertView
				.findViewById(R.id.tv_bussiness_name);
		holder.tv2 = (TextView) convertView
				.findViewById(R.id.tv_bussiness_city);
		holder.tv3 = (TextView) convertView
				.findViewById(R.id.tv_bussiness_address);
		holder.img1 = (ImageView) convertView.findViewById(R.id.img_bussiness);
		holder.tv1.setText(item.getName());
		holder.tv2.setText(item.getCity());
		holder.tv3.setText(item.getAddress());
		MyClickListener listener = new MyClickListener(item);
		holder.img1.setOnClickListener(listener);
		holder.ly1.setOnClickListener(listener);
		return convertView;
	}

	class MyClickListener implements OnClickListener {
		Businesses item;

		MyClickListener(Businesses item) {
			this.item = item;
		}

		Intent intent;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_bussiness:
				intent = new Intent(getContext(), MapForBussinessActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("bussiness", item);
				intent.putExtras(bundle);
				getContext().startActivity(intent);
				break;
			case R.id.ly_business:
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item
						.getH5_url()));
				getContext().startActivity(intent);
				break;
			default:
				break;
			}

		}

	}

}
