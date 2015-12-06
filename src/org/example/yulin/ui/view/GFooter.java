package org.example.yulin.ui.view;

import org.example.yulin.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @since July 4, 2013
 * @author kzhang
 */
public class GFooter extends LinearLayout {

	private ProgressBar progressBar;
	private TextView tvRefresh;

	public GFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GFooter(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.g_footer, this);

		init(context);
	}

	private void init(Context context) {
		progressBar = (ProgressBar) findViewById(R.id.xlistview_footer_progressbar);
		tvRefresh = (TextView) findViewById(R.id.xlistview_footer_hint_textview);
	}

	/**
	 * 上拉加载更多
	 */
	public int setStartLoad() {
		progressBar.setVisibility(View.GONE);
		tvRefresh.setText(R.string.xlistview_footer_hint_normal);

		return GScrollView.PULL_UP_STATE;
	}

	/**
	 * 松开手加载更多
	 */
	public int releaseLoad() {
		progressBar.setVisibility(View.GONE);
		tvRefresh.setText(R.string.xlistview_footer_hint_ready);
		return GScrollView.RELEASE_TO_LOADING;
	}

	/**
	 * 正在加载更多
	 */
	public int setLoading() {
		progressBar.setVisibility(View.VISIBLE);
		tvRefresh.setText(R.string.xlistview_header_hint_loading);
		return GScrollView.LOADING;
	}

	/**
	 * 设置 View 高度
	 * 
	 * @param presetHeight
	 *            原始高度
	 * @param currentHeight
	 *            当前高度
	 */
	public int setPadding(int presetHeight, int paddingHeight) {
		this.setPadding(0, 0, 0, paddingHeight);

		// 初始化箭头状态向上
		if (paddingHeight <= presetHeight) {
			return setStartLoad();
		} else { // 改变按钮状态向下
			return releaseLoad();
		}
	}

	/**
	 * 初始化 FootView PaddingButtom
	 */
	public void setPaddingButtom() {
		this.setPadding(0, 0, 0, 0);
	}

	/**
	 * 显示加载更多按钮
	 */
	public void show() {
		this.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏加载更多按钮
	 */
	public void hide() {
		this.setVisibility(View.GONE);
	}

}
