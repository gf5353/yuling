package org.example.yulin.adapter;

import java.util.ArrayList;
import java.util.List;

import org.example.yulin.bean.BaseItem;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class VPagerAdapter extends PagerAdapter {
	private List<BaseItem> lists = new ArrayList<BaseItem>();
	private Context context;
	public LayoutInflater inflater;

	public abstract View getViewGroup(ViewGroup container, int position);

	public abstract View getView(View container, int position);

	public VPagerAdapter() {

	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return getViewGroup(container, position);
	}

	@Override
	public Object instantiateItem(View container, int position) {
		return getView(container, position);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	/**
	 * get set
	 * 
	 * @return
	 */
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
		inflater = LayoutInflater.from(context);
	}
}
