package org.example.yulin.adapter;

import java.util.List;

import org.example.yulin.R;
import org.example.yulin.bean.BaseItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ToolsAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<BaseItem> lists;

	public ToolsAdapter(Context context, List<BaseItem> lists) {
		this.lists = lists;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	Holder holder;

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		BaseItem item = lists.get(arg0);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_grid_tools, null);
			holder = new Holder();
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.img = (ImageView) convertView.findViewById(R.id.img_grid_tools);
		holder.name = (TextView) convertView.findViewById(R.id.tv_grid_tools);
		holder.img.setImageResource(item.getResId());
		holder.name.setText(item.getResName());
		return convertView;
	}

	class Holder {
		ImageView img;
		TextView name;
	}
}
