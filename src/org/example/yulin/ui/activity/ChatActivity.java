package org.example.yulin.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.example.yulin.Constants;
import org.example.yulin.R;
import org.example.yulin.adapter.ChatMessageAdapter;
import org.example.yulin.bean.ChatUser;
import org.example.yulin.bean.tuling.ChatMessage;
import org.example.yulin.bean.tuling.ChatMessage.Type;
import org.example.yulin.ui.view.ChatView;
import org.example.yulin.ui.view.ChatView.OnChatListener;
import org.example.yulin.util.IflytekUtil;
import org.example.yulin.util.IflytekUtil.IflytekuListener;
import org.example.yulin.util.TuLingUtil;
import org.example.yulin.util.TuLingUtil.OnTuLingListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.GQZSharepf;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;

import android.os.Bundle;
import android.widget.ListView;

import com.iflytek.cloud.SpeechUtility;

@ContentView(R.layout.activity_chat)
public class ChatActivity extends ActionActivity implements OnChatListener,
		IflytekuListener, OnTuLingListener {
	@ViewInject(R.id.chat)
	ChatView chat;
	@ViewInject(R.id.message_list)
	ListView message_list;

	List<ChatMessage> lists = new ArrayList<ChatMessage>();
	IflytekUtil ifly;
	ChatMessageAdapter adapter;
	private TuLingUtil tuLingUtil;
	ChatUser user;

	@Override
	public void initData(Bundle savedInstanceState) {
		super.initData(savedInstanceState);
		setPageName("聊天页面");
		user = (ChatUser) getIntent().getSerializableExtra("user");
		setActionBarName(user.getResName());
		lists.add(new ChatMessage(Type.INPUT, "你好，我是" + user.getResName()
				+ "，很高兴为您服务", user.getResName(), ""));
		ifly = IflytekUtil.getInstance().initIflytek(this);
		ifly.setVoicer(user.getParameter());
		ifly.setIflytekuListener(this);
		tuLingUtil = TuLingUtil.getInstance(this).setLingListener(this);
		adapter = new ChatMessageAdapter(this, lists, ifly, user.getResId());
	}

	@Override
	public void initWidget() {
		super.initWidget();
		chat.setOnChatListener(this);
		message_list.setAdapter(adapter);
	}

	@Override
	public void send(String msg) {
		if (isSend) {
			tuLingUtil.sendMsg(msg);
			lists.add(new ChatMessage(Type.OUTPUT, msg, String
					.valueOf(GQZSharepf.get(Constants.SP_NickName, "")), String
					.valueOf(GQZSharepf.get(Constants.SP_HeadUrl, ""))));
			adapter.notifyDataSetChanged();
			message_list.setSelection(adapter.getCount());
			chat.setText("");
			isSend = false;
		} else {
			GQZInject.toast("网络异常！");
		}
	}

	boolean isSend = true;// 是否允许继续发送

	@Override
	public void voice() {
		if (isSend) {
			ifly.show();
			isSend = false;
		} else {
			GQZInject.toast("网络异常！");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		SpeechUtility.getUtility().checkServiceInstalled();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ifly.onDestroy();
	}

	@Override
	public void onResult(String result) {
		chat.setText(result).setSelection(result.length());
		tuLingUtil.sendMsg(result);
		lists.add(new ChatMessage(Type.OUTPUT, result, String
				.valueOf(GQZSharepf.get(Constants.SP_NickName, "")), String
				.valueOf(GQZSharepf.get(Constants.SP_HeadUrl, ""))));
		adapter.notifyDataSetChanged();
		message_list.setSelection(adapter.getCount());
		chat.setText("");
	}

	JSONObject jsonObject;

	@Override
	public void onReceive(String msg) {
		adapter.setPaly(true);
		try {
			jsonObject = new JSONObject(msg);
			lists.add(new ChatMessage(Type.INPUT, jsonObject.getString("text"),
					user.getResName(), "", msg));
			adapter.notifyDataSetChanged();
			message_list.setSelection(adapter.getCount());
			chat.setText("");
			isSend = true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
