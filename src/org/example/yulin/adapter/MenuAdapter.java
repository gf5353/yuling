package org.example.yulin.adapter;

import org.example.yulin.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter {
	private int[] imgId;
	private String[] texts;
	LayoutInflater inflater;

	public MenuAdapter(Context context, int[] imgId, String[] texts) {
		this.imgId = imgId;
		this.texts = texts;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return imgId.length;
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
	public View getView(int arg0, View convertView, ViewGroup v) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_menu, null);
			holder = new Holder();
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.img = (ImageView) convertView.findViewById(R.id.img_menu);
		holder.name = (TextView) convertView.findViewById(R.id.tv_menu);
		holder.img.setImageResource(imgId[arg0]);
		holder.name.setText(texts[arg0]);
		return convertView;
	}

	class Holder {
		ImageView img;
		TextView name;
	}
}
