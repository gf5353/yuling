package org.example.yulin.ui.jellyviewpager;

import java.util.List;

import org.example.yulin.R;
import org.example.yulin.bean.BaseItem;
import org.example.yulin.bean.ChatUser;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class JellyViewAdapter extends PagerAdapter {
	private List<BaseItem> lists;
	private LayoutInflater inflater;
	private OnJellyClickListener onJellyClickListener;

	public OnJellyClickListener getOnJellyClickListener() {
		return onJellyClickListener;
	}

	public void setOnJellyClickListener(
			OnJellyClickListener onJellyClickListener) {
		this.onJellyClickListener = onJellyClickListener;
	}

	public interface OnJellyClickListener {
		void left();

		void confirm(int position, ChatUser user);

		void rigth();
	}

	public JellyViewAdapter(Context context, List<BaseItem> lists) {
		inflater = LayoutInflater.from(context);
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	Button left, right, confirm;
	ImageView img_frag;
	TextView tv_frag;

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		final ChatUser item = (ChatUser) lists.get(position);
		View view = inflater.inflate(R.layout.frag_layout, null);
		left = (Button) view.findViewById(R.id.btn_frag_left);
		confirm = (Button) view.findViewById(R.id.btn_frag_confirm);
		right = (Button) view.findViewById(R.id.btn_frag_right);
		img_frag = (ImageView) view.findViewById(R.id.img_frag);
		tv_frag = (TextView) view.findViewById(R.id.tv_frag);
		container.addView(view);
		tv_frag.setText(item.getResName() + "(" + item.getLanguage() + ")");
		img_frag.setImageResource(item.getResId());
		if (position == 0) {
			left.setEnabled(false);
			left.setText("到底了");
		} else if (position == lists.size() - 1) {
			right.setEnabled(false);
			right.setText("到底了");
		} else {
			left.setEnabled(true);
			right.setEnabled(true);
			left.setText("上一位");
			right.setText("下一位");
		}
		if (onJellyClickListener != null) {
			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					onJellyClickListener.left();
				}
			});
			right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					onJellyClickListener.rigth();
				}
			});
			confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					onJellyClickListener.confirm(position, item);
				}
			});
		}
		return view;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
