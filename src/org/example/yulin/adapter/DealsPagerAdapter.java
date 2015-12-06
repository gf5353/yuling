package org.example.yulin.adapter;

import java.util.List;

import org.example.yulin.bean.BaseItem;
import org.mobile.gqz.GQZBitmap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class DealsPagerAdapter extends VPagerAdapter {
	private GQZBitmap bitmap;

	public DealsPagerAdapter(List<BaseItem> lists, Context context) {
		setLists(lists);
		setContext(context);
		bitmap = new GQZBitmap(context);
	}

	@Override
	public View getViewGroup(ViewGroup container, int position) {
		BaseItem item = getLists().get(position);
		ImageView img = new ImageView(getContext());
		container.addView(img);
		bitmap.display(img, item.getResName());
		return img;
	}

	@Override
	public View getView(View view, int position) {
		return null;
	}

}
