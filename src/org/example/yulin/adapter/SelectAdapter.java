package org.example.yulin.adapter;

import java.util.List;

import org.example.yulin.R;
import org.example.yulin.bean.BaseItem;
import org.example.yulin.bean.Holder;
import org.example.yulin.bean.juhe.Courier;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SelectAdapter extends ParentAdapter {
	public SelectAdapter(Context context, List<BaseItem> lists) {
		setContext(context);
		setLists(lists);
	}

	@Override
	public View createView(int position, View convertView, ViewGroup parent) {
		Courier item = (Courier) getLists().get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_select, null);
			holder = new Holder();
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.tv1 = (TextView) convertView.findViewById(R.id.tv_item_select);
		holder.tv1.setText(item.getCom());
		return convertView;
	}

}
