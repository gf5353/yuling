package org.example.yulin.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseItem implements Serializable {
	private int resId;
	private String resName;
	private String type;
	private String serializable;

	public String getSerializable() {
		return toString();
	}

	public void setSerializable(String serializable) {
		this.serializable = serializable;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BaseItem() {

	}

	public BaseItem(JSONObject json) {
	}

	public BaseItem(int resId, String resName, String type) {
		super();
		this.resId = resId;
		this.resName = resName;
		this.type = type;
	}

	public BaseItem(int resId, String resName) {
		super();
		this.resId = resId;
		this.resName = resName;
	}

	public String[] toList(JSONArray array) throws JSONException {
		String[] list = new String[array.length()];
		for (int i = 0; i < array.length(); i++) {
			list[i] = array.getString(i);
		}
		return list;
	}

	public <T> List<T> toLists(JSONArray array, Class<T> c)
			throws JSONException {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < array.length(); i++) {
			list.add((T) new BaseItem(array.getJSONObject(i)));
		}
		return list;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	// @Override
	// public int describeContents() {
	// return 0;
	// }
	//
	// @Override
	// public void writeToParcel(Parcel dest, int flags) {
	// dest.writeString(getResName());
	// dest.writeInt(getResId());
	// }
}
