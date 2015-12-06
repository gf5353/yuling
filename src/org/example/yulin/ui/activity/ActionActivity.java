package org.example.yulin.ui.activity;

import org.mobile.gqz.ui.activity.GQZActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * 带有actionbar的activity
 * 
 * @author Administrator
 *
 */
public class ActionActivity extends GQZActivity {
	public ActionBar actionbar;

	@Override
	public void initData(Bundle savedInstanceState) {
		super.initData(savedInstanceState);
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);
	}

	@Override
	public void initWidget() {
		super.initWidget();

	}

	public void setActionBarName(String name) {
		if (actionbar != null) {
			actionbar.setTitle(name);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
