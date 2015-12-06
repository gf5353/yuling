package org.example.yulin.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.yulin.bean.BaseItem;
import org.example.yulin.bean.Holder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * viewpager嵌套gridview
 * 
 * @author admin
 * 
 */
public class GridAdapter extends VPagerAdapter {

	private Map<Integer, GridView> gridMap;
	int pageSize = 8;
	int numColumns = 4;

	private OnGridClickListener onGridClickListener;

	public OnGridClickListener getOnGridClickListener() {
		return onGridClickListener;
	}

	public void setOnGridClickListener(OnGridClickListener onGridClickListener) {
		this.onGridClickListener = onGridClickListener;
	}

	public interface OnGridClickListener {
		View createView(LayoutInflater mInflater, int position,
				View convertView, ViewGroup parent);

		void onItemClick(int i);

	}

	public GridAdapter(Context context, List<BaseItem> lists, int pageSize,
			int numColumns) {
		setContext(context);
		this.pageSize = pageSize;
		this.numColumns = numColumns;
		setLists(lists);
	}

	@Override
	public void setLists(List<BaseItem> lists) {
		super.setLists(lists);
		gridMap = new HashMap<Integer, GridView>();
		int PageCount = (int) Math.ceil(lists.size() / pageSize);
		for (int i = 0; i <= PageCount; i++) {
			GridView appPage = new GridView(getContext());
			GridItemAdapter adapter = new GridItemAdapter(getContext(), lists,
					i);
			appPage.setAdapter(adapter);
			appPage.setNumColumns(numColumns);
			appPage.setHorizontalSpacing(10);
			appPage.setVerticalSpacing(10);
			appPage.setOnItemClickListener(adapter);
			appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
			gridMap.put(i, appPage);
		}
	}

	@Override
	public int getCount() {
		return getGridMap().size();
	}

	@Override
	public View getViewGroup(ViewGroup container, int position) {
		View view = gridMap.get(position);
		container.addView(view);
		return view;
	}

	@Override
	public View getView(View container, int position) {
		return null;
	}

	public Map<Integer, GridView> getGridMap() {
		return gridMap;
	}

	public void setGridMap(Map<Integer, GridView> gridMap) {
		this.gridMap = gridMap;
	}

	class GridItemAdapter extends ParentAdapter implements OnItemClickListener {
		Holder holder;
		private int page;

		public int getPage() {
			return page;
		}

		public void setPage(int page) {
			this.page = page;
		}

		public GridItemAdapter(Context context, List<BaseItem> dataList,
				int page) {
			setContext(context);
			setPage(page);
			lists = new ArrayList<BaseItem>();
			// 根据当前页计算装载的应用，每页只装载16个
			int i = page * pageSize;// 当前页的其实位置
			int iEnd = i + pageSize;// 所有数据的结束位置
			while ((i < dataList.size()) && (i < iEnd)) {
				lists.add(dataList.get(i++));
			}
			setLists(lists);
		}

		@Override
		public View createView(int position, View convertView, ViewGroup parent) {
			if (onGridClickListener != null) {
				convertView = onGridClickListener.createView(mInflater, page
						* pageSize + position, convertView, parent);
			}
			return convertView;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (onGridClickListener != null) {
				int index = getPage() * pageSize + arg2;
				onGridClickListener.onItemClick(index);
			}
		}
	}

}
