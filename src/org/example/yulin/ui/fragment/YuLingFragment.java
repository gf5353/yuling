package org.example.yulin.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.example.yulin.Constants;
import org.example.yulin.R;
import org.example.yulin.bean.BaseItem;
import org.example.yulin.bean.ChatUser;
import org.example.yulin.ui.activity.ChatActivity;
import org.example.yulin.ui.jellyviewpager.JellyViewAdapter;
import org.example.yulin.ui.jellyviewpager.JellyViewAdapter.OnJellyClickListener;
import org.example.yulin.ui.jellyviewpager.JellyViewPager;
import org.mobile.gqz.ui.annotation.ViewInject;
import org.mobile.gqz.ui.fragment.GQZFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class YuLingFragment extends GQZFragment {
	@Override
	protected View inflaterView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {
		return inflater.inflate(R.layout.fragment_yuling, null);
	}

	@ViewInject(R.id.jellyViewPager)
	JellyViewPager jellyViewPager;

	List<BaseItem> lists;
	JellyViewAdapter viewAdapter;

	@Override
	protected void initWidget(View parentView) {
		super.initWidget(parentView);
		lists = new ArrayList<BaseItem>();
		for (int i = 0; i < Constants.role_size; i++) {
			ChatUser item = new ChatUser();
			item.setResId(R.drawable.ic_launcher);
			item.setResName(ChatUser.names[i]);
			lists.add(item);
		}
		viewAdapter = new JellyViewAdapter(getContext(), lists);
		viewAdapter.setOnJellyClickListener(new OnJellyClickListener() {

			@Override
			public void rigth() {
				jellyViewPager.showNext();
			}

			@Override
			public void left() {
				jellyViewPager.showPre();
			}

			@Override
			public void confirm(int position, ChatUser user) {
				Intent intent = new Intent(getActivity(), ChatActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", user);
				intent.putExtras(bundle);
				getActivity().startActivity(intent);
			}
		});
		jellyViewPager.setAdapter(viewAdapter);
	}

}
