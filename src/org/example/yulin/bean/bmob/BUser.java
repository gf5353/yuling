package org.example.yulin.bean.bmob;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class BUser extends BmobUser {
	private String nickname;
	private String url_head;
	private double longitude;// 经度
	private double latitude;// 纬度

	private BmobGeoPoint gpsAdd;

	public BmobGeoPoint getGpsAdd() {
		return gpsAdd;
	}

	public void setGpsAdd(BmobGeoPoint gpsAdd) {
		this.gpsAdd = gpsAdd;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUrl_head() {
		return url_head;
	}

	public void setUrl_head(String url_head) {
		this.url_head = url_head;
	}

}
