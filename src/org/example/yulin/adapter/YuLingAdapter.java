package org.example.yulin.adapter;

import java.util.List;

import org.example.yulin.R;
import org.example.yulin.bean.BaseItem;
import org.example.yulin.bean.Holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class YuLingAdapter extends ParentAdapter {
	private int mHidePosition = -1;

	public YuLingAdapter(Context context, List<BaseItem> lists) {
		setContext(context);
		setLists(lists);
	}

	Holder holder;

	@Override
	public View createView(int position, View convertView, ViewGroup parent) {
		BaseItem item = lists.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_grid_yuling, null);
			holder = new Holder();
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.img1 = (ImageView) convertView
				.findViewById(R.id.img_grid_yuling);
		holder.tv1 = (TextView) convertView.findViewById(R.id.tv_grid_yuling);
		holder.img1.setImageResource(item.getResId());
		holder.tv1.setText(item.getResName());
		return convertView;
	}

}
