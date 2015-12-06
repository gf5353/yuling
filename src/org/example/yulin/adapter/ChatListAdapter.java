package org.example.yulin.adapter;

import java.util.List;

import org.example.yulin.R;
import org.example.yulin.bean.tuling.News;
import org.example.yulin.bean.tuling.Software;
import org.example.yulin.bean.tuling.Train;
import org.example.yulin.bean.tuling.TuLing;
import org.mobile.gqz.GQZBitmap;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	Context context;
	List<TuLing> lists;
	GQZBitmap gqzBitmap;

	public ChatListAdapter(Context context, List<TuLing> lists) {
		mInflater = LayoutInflater.from(context);
		this.context = context;
		this.lists = lists;
		gqzBitmap = new GQZBitmap(context);
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

	ViewHolder viewHolder = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TuLing tuLing = lists.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			if (tuLing instanceof News) {// 新闻
				convertView = mInflater.inflate(R.layout.item_chat_news,
						parent, false);
			} else if (tuLing instanceof Software) {// 软件
				convertView = mInflater.inflate(R.layout.item_chat_news,
						parent, false);
			} else if (tuLing instanceof Train) {// 列车
				convertView = mInflater.inflate(R.layout.item_chat_train,
						parent, false);
			}
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (tuLing instanceof News) {
			News news = (News) tuLing;
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.tv_news_article);
			viewHolder.source = (TextView) convertView
					.findViewById(R.id.tv_news_source);
			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.img_news);
			viewHolder.title.setText(news.getArticle());
			viewHolder.source.setText("来源：" + news.getSource());
			gqzBitmap.display(viewHolder.icon, news.getIcon());
		} else if (tuLing instanceof Software) {
			Software software = (Software) tuLing;
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.tv_news_article);
			viewHolder.source = (TextView) convertView
					.findViewById(R.id.tv_news_source);
			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.img_news);
			viewHolder.title.setText(software.getName());
			viewHolder.source.setText(software.getCount());
			gqzBitmap.display(viewHolder.icon, software.getIcon());
		} else if (tuLing instanceof Train) {
			Train train = (Train) tuLing;
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.tv_train_trainnum);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.tv_train_time);
			viewHolder.source = (TextView) convertView
					.findViewById(R.id.tv_train_site);
			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.img_train);

			viewHolder.title.setText(train.getTrainnum());// 车次
			viewHolder.source.setText("起始站—终点站:" + train.getStart() + "~"
					+ train.getTerminal());// 站点
			viewHolder.time.setText("开车时间—到达时间:" + train.getStarttime() + "~"
					+ train.getEndtime());// 时间
			if (!TextUtils.isEmpty(train.getIcon())) {
				gqzBitmap.display(viewHolder.icon, train.getIcon());
			} else {
				viewHolder.icon.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	private class ViewHolder {
		TextView title;
		TextView source;
		TextView time;
		ImageView icon;
	}
}
