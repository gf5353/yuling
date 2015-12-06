package org.example.yulin.ui.view;

import java.text.ParseException;
import java.util.Date;

import org.example.yulin.Constants;
import org.example.yulin.R;
import org.example.yulin.bean.bmob.BUser;
import org.example.yulin.util.TimeUtil;
import org.mobile.gqz.GQZBitmap;
import org.mobile.gqz.GQZLog;
import org.mobile.gqz.GQZSharepf;
import org.mobile.gqz.bitmap.BitmapDisplayConfig;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import a.This;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MenuLayout extends LinearLayout {

	public MenuLayout(Context context) {
		super(context);
		init(context);
	}

	public MenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MenuLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	LinearLayout ly_menu;
	ListView list_menu;
	static TextView tv_menu_name, tv_menu_day;
	static RoundedCornerImageView img_menu_head;

	public void setOnClickListener(OnClickListener listener) {
		ly_menu.setOnClickListener(listener);
	}

	public void setOnHeadClickListener(OnClickListener listener) {
		img_menu_head.setOnClickListener(listener);
	}

	public ListView setAdapter(BaseAdapter adapter) {
		list_menu.setAdapter(adapter);
		return list_menu;
	}

	static GQZBitmap gBitmap;
	private static Context context;

	private void init(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.layout_menu, this);
		gBitmap = new GQZBitmap(context);
		ly_menu = (LinearLayout) findViewById(R.id.ly_menu);
		list_menu = (ListView) findViewById(R.id.list_menu);
		tv_menu_name = (TextView) findViewById(R.id.tv_menu_name);
		tv_menu_day = (TextView) findViewById(R.id.tv_menu_day);
		img_menu_head = (RoundedCornerImageView) findViewById(R.id.img_menu_head);
		changeData();
	}

	private static BUser userInfo;

	public static void changeData() {
		String nickName = (String) GQZSharepf.get(Constants.SP_NickName, "");
		String headUrl = (String) GQZSharepf.get(Constants.SP_HeadUrl, "");
		userInfo = BmobUser.getCurrentUser(context, BUser.class);
		if (userInfo != null) {
			String date = userInfo.getCreatedAt();// 2015-02-03 10:48:11
			GQZLog.debug("CreatedAt:" + date + "----" + userInfo.getUsername());
			Date installation = TimeUtil.stringToDate(date,
					TimeUtil.FORMAT_DATE2_TIME);
			GQZSharepf.put(Constants.SP_Installation_Time,
					TimeUtil.dateToString(installation, TimeUtil.FORMAT_DATE));
			try {
				int days = TimeUtil.daysBetween(installation, new Date()) + 1;
				tv_menu_day.setText("欢迎您来到的第" + days + "天");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (!nickName.equals(userInfo.getNickname())
					|| !headUrl.equals(userInfo.getUrl_head())) {
				BUser user = userInfo;
				user.setUrl_head(headUrl);
				user.setNickname(nickName);
				user.update(context);
			}
		} else {
			String date = (String) GQZSharepf.get(
					Constants.SP_Installation_Time, "");
			if (TextUtils.isEmpty(date)) {
				date = TimeUtil.dateToString(new Date(), TimeUtil.FORMAT_DATE);
				GQZSharepf.put(Constants.SP_Installation_Time, date);
			}
			Date installation = TimeUtil.stringToDate(date,
					TimeUtil.FORMAT_DATE);
			try {
				int days = TimeUtil.daysBetween(installation, new Date()) + 1;
				tv_menu_day.setText("欢迎您来到的第" + days + "天");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (!TextUtils.isEmpty(nickName) && !TextUtils.isEmpty(headUrl)) {
			tv_menu_name.setText(nickName);
			// gBitmap.display(img_menu_head, headUrl);
			gBitmap.display(img_menu_head, headUrl, new BitmapDisplayConfig());
		}
	}
}
