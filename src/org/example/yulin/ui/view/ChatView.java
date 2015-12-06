package org.example.yulin.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.example.yulin.R;
import org.example.yulin.adapter.GridAdapter;
import org.example.yulin.adapter.GridAdapter.OnGridClickListener;
import org.example.yulin.bean.BaseItem;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.utils.SystemTool;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatView extends RelativeLayout implements OnClickListener,
		OnLongClickListener {
	public ChatView(Context context) {
		super(context);
		init(context);
	}

	public ChatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ChatView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public EditText setText(String text) {
		et_sendmessage.setText(text);
		return et_sendmessage;
	}

	private OnChatListener onChatListener;

	public void setOnChatListener(OnChatListener onChatListener) {
		this.onChatListener = onChatListener;
	}

	public interface OnChatListener {
		void send(String msg);

		void voice();
	}

	Button btn_set_mode_voice, btn_set_mode_keyboard, btn_more, btn_send;
	EditText et_sendmessage;
	RelativeLayout edittext_layout, ly_chat_voice;
	Context context;
	ViewPager viewpager_chat;

	private void init(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.layout_chat, this);
		btn_set_mode_voice = (Button) findViewById(R.id.btn_set_mode_voice);
		btn_set_mode_keyboard = (Button) findViewById(R.id.btn_set_mode_keyboard);
		et_sendmessage = (EditText) findViewById(R.id.et_sendmessage);
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
		ly_chat_voice = (RelativeLayout) findViewById(R.id.ly_chat_voice);
		btn_more = (Button) findViewById(R.id.btn_more);
		btn_send = (Button) findViewById(R.id.btn_send);
		viewpager_chat = (ViewPager) findViewById(R.id.viewpager_chat);
		btn_more.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		btn_set_mode_voice.setOnClickListener(this);
		btn_set_mode_keyboard.setOnClickListener(this);
		ly_chat_voice.setOnLongClickListener(this);
		initGrid();

		et_sendmessage.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (et_sendmessage.getText().length() == 0) {
					btn_more.setVisibility(View.VISIBLE);
					btn_send.setVisibility(View.GONE);
				} else {
					btn_more.setVisibility(View.GONE);
					btn_send.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	GridAdapter adapter;
	List<BaseItem> lists;

	private void initGrid() {
		lists = new ArrayList<BaseItem>();
		lists.add(new BaseItem(0, "打开百度官网", "查网站"));
		lists.add(new BaseItem(1, "我想看体育新闻", "看新闻"));
		lists.add(new BaseItem(2, "下载微信", "下软件"));
		lists.add(new BaseItem(3, "北京到拉萨的火车", "查列车"));
		adapter = new GridAdapter(context, lists, 6, 3);
		adapter.setOnGridClickListener(new OnGridClickListener() {

			@Override
			public void onItemClick(int i) {
				String sendString = lists.get(i).getResName();
				et_sendmessage.setText(sendString);
				et_sendmessage.setSelection(sendString.length());
				viewpager_chat.setVisibility(View.GONE);
			}

			@Override
			public View createView(LayoutInflater mInflater, int position,
					View convertView, ViewGroup parent) {
				BaseItem item = adapter.getLists().get(position);
				TextView textView = new TextView(getContext());
				textView.setGravity(Gravity.CENTER);
				textView.setTextSize(20);
				textView.setText(item.getType());
				return textView;
			}
		});
		viewpager_chat.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_set_mode_voice:// 语音
			switchView(btn_set_mode_voice, btn_set_mode_keyboard);
			switchView(ly_chat_voice, edittext_layout);
			viewpager_chat.setVisibility(View.GONE);
			break;
		case R.id.btn_set_mode_keyboard:// 键盘
			switchView(btn_set_mode_keyboard, btn_set_mode_voice);
			switchView(edittext_layout, ly_chat_voice);
			viewpager_chat.setVisibility(View.GONE);
			break;
		case R.id.btn_send:
			viewpager_chat.setVisibility(View.GONE);
			if (isInstance(onChatListener)) {
				if (SystemTool.checkNet(context)) {
					onChatListener.send(et_sendmessage.getText().toString());
				} else {
					GQZInject.toast(R.string.checkNet);
				}
			}
			break;
		case R.id.btn_more:
			if (viewpager_chat.getVisibility() == View.VISIBLE) {
				viewpager_chat.setVisibility(View.GONE);
			} else {
				viewpager_chat.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}
	}

	private boolean isInstance(Object obj) {
		if (obj != null) {
			return true;
		} else {
			Log.i(ChatView.class.getName(), obj + "no instance");
			return false;
		}
	}

	private void switchView(View itself, View view) {
		if (view.getVisibility() == View.VISIBLE) {
			itself.setVisibility(View.VISIBLE);
			view.setVisibility(View.GONE);
		} else {
			itself.setVisibility(View.GONE);
			view.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
		case R.id.ly_chat_voice:
			if (isInstance(onChatListener)) {
				onChatListener.voice();
			}
			break;

		default:
			break;
		}
		return false;
	}
}
