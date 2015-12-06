package org.example.yulin.ui.view;

import org.example.yulin.R;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarPreference extends Preference {
	public SeekBarPreference(Context context) {
		super(context);
		init(context);
	}

	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	TextView tv_pre_name;
	SeekBar seekBar;

	private void init(Context context) {
		setLayoutResource(R.layout.preference_seekbar);
	}

	public void setName(String name) {
		tv_pre_name.setText(name);
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		tv_pre_name = (TextView) view.findViewById(R.id.tv_pre_name);
		seekBar = (SeekBar) view.findViewById(R.id.seek_pre);
	}
}
