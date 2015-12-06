package org.example.yulin.adapter;

import java.util.List;

import org.example.yulin.Constants;
import org.example.yulin.R;
import org.example.yulin.bean.tuling.ChatMessage;
import org.example.yulin.bean.tuling.ChatMessage.Type;
import org.example.yulin.ui.activity.ListActivity;
import org.example.yulin.ui.view.RoundedCornerImageView;
import org.example.yulin.util.IflytekUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.gqz.GQZBitmap;
import org.mobile.gqz.GQZSharepf;

import com.baidu.platform.comapi.map.p;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChatMessageAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<ChatMessage> mDatas;
	Context context;
	GQZBitmap gqzBitmap;
	private IflytekUtil ifly;
	private int headId = 0;
	public boolean paly = true;

	public boolean isPaly() {
		return paly;
	}

	public void setPaly(boolean paly) {
		this.paly = paly;
	}

	public ChatMessageAdapter(Context context, List<ChatMessage> datas,
			IflytekUtil ifly, int headId) {
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
		this.context = context;
		this.ifly = ifly;
		gqzBitmap = new GQZBitmap(context);
		this.headId = headId;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 接受到消息为1，发送消息为0
	 */
	@Override
	public int getItemViewType(int position) {
		ChatMessage msg = mDatas.get(position);
		return msg.getType() == Type.INPUT ? 1 : 0;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	public void setChatFromHead(int resId) {
		viewHolder.imageView.setImageResource(resId);
	}

	ViewHolder viewHolder = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChatMessage chatMessage = mDatas.get(position);

		// if (convertView == null) {
		viewHolder = new ViewHolder();
		if (chatMessage.getType() == Type.INPUT) {
			convertView = mInflater.inflate(R.layout.main_chat_from_msg,
					parent, false);
			viewHolder.createDate = (TextView) convertView
					.findViewById(R.id.chat_from_createDate);
			viewHolder.imageView = (RoundedCornerImageView) convertView
					.findViewById(R.id.chat_from_icon);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.chat_from_content);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.chat_from_name);
			viewHolder.imageView.setImageResource(headId);
			if ((Boolean) GQZSharepf.get(Constants.SP_Play_Voice, false)) {// 自动播放
				if (position == mDatas.size() - 1 && paly) {
					ifly.speak(chatMessage.getMsg());
					paly = false;
				}
			}
			convertView.setTag(viewHolder);
			String json = chatMessage.getJson();
			if (!TextUtils.isEmpty(json)) {
				try {
					parsingJson(new JSONObject(json), convertView, viewHolder);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		} else {
			convertView = mInflater.inflate(R.layout.main_chat_send_msg, null);
			viewHolder.createDate = (TextView) convertView
					.findViewById(R.id.chat_send_createDate);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.chat_send_content);
			viewHolder.imageView = (RoundedCornerImageView) convertView
					.findViewById(R.id.chat_send_icon);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.chat_send_name);
			convertView.setTag(viewHolder);
		}
		// } else {
		// viewHolder = (ViewHolder) convertView.getTag();
		// }
		viewHolder.content.setText(chatMessage.getMsg());
		viewHolder.createDate.setText(chatMessage.getDateStr());
		String nameStr = chatMessage.getName();
		String imageStr = chatMessage.getImage();
		if (!TextUtils.isEmpty(nameStr)) {
			viewHolder.name.setText(nameStr);
		}
		if (!TextUtils.isEmpty(imageStr)) {
			gqzBitmap.display(viewHolder.imageView, imageStr);
		}
		listener = new AdapterOnclickListener(chatMessage.getMsg());
		viewHolder.imageView.setOnClickListener(listener);
		return convertView;
	}

	AdapterOnclickListener listener;

	class AdapterOnclickListener implements OnClickListener {
		String text;

		AdapterOnclickListener(String text) {
			this.text = text;
		}

		@Override
		public void onClick(View v) {
			ifly.speak(text);
		}

	}

	private void parsingJson(final JSONObject json, View convertView,
			ViewHolder viewHolder) throws JSONException {
		int code = json.getInt("code");
		switch (code) {
		case 100000:// 文字类
			// text;
			break;
		case 200000:// 链接类
			final String url = json.getString("url");
			viewHolder.url = (TextView) convertView
					.findViewById(R.id.chat_from_url);
			viewHolder.url.setVisibility(View.VISIBLE);
			viewHolder.url.setText(url);
			viewHolder.url.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(url));
					context.startActivity(rateIntent);
				}
			});
			break;
		case 302000:// 新闻
		case 304000:// 软件下载
		case 305000:// 列车
		case 306000:// 航班
		case 308000:// 电影
		case 309000:// 酒店
		case 311000:// 价格
			viewHolder.content.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ListActivity.class);
					intent.putExtra("json", json.toString());
					context.startActivity(intent);
				}
			});
			break;
		default:
			break;
		}
	}

	private class ViewHolder {
		public TextView createDate;
		public TextView name;
		public TextView content;
		public TextView url;
		public ListView listView;
		RoundedCornerImageView imageView;
	}

}
