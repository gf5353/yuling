package org.example.yulin.adapter;

import java.util.List;

import org.example.yulin.R;
import org.example.yulin.bean.BaseItem;
import org.example.yulin.bean.Holder;
import org.example.yulin.bean.juhe.Trip;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CourierAdapter extends ParentAdapter {
	public CourierAdapter(Context context, List<BaseItem> lists) {
		setContext(context);
		setLists(lists);
	}

	@Override
	public View createView(int position, View convertView, ViewGroup parent) {
		Trip item = (Trip) getLists().get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_courier, null);
			holder = new Holder();
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.tv1 = (TextView) convertView.findViewById(R.id.tv_courier_time);
		holder.tv2=(TextView) convertView.findViewById(R.id.tv_courier_address);
		holder.tv3 = (TextView) convertView
				.findViewById(R.id.tv_courier_remark);
		holder.tv1.setText(item.getDatetime());
		holder.tv2.setText(item.getZone());
		holder.tv3.setText(item.getRemark());
		return convertView;
	}

}
