package org.example.yulin.adapter;

import java.util.List;

import org.example.yulin.bean.BaseItem;
import org.example.yulin.bean.Holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public abstract class ParentAdapter extends BaseAdapter {
	public List<BaseItem> lists;
	public Context context;
	public Holder holder;

	public LayoutInflater mInflater;

	public List<BaseItem> getLists() {
		return lists;
	}

	public void setLists(List<BaseItem> lists) {
		this.lists = lists;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	public abstract View createView(int position, View convertView,
			ViewGroup parent);

	public ParentAdapter() {
		super();
	}

	public ParentAdapter(List<BaseItem> lists, Context context) {
		super();
		this.lists = lists;
		this.context = context;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createView(position, convertView, parent);
	}

	// public class Holder {
	// public TextView tv1, tv2, tv3,tv4,tv5,tv6;
	// public ImageView img1, img2, img3;
	// }

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

}
