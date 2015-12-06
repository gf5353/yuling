package org.example.yulin.bean.dianping;

import org.example.yulin.bean.BaseItem;
import org.json.JSONException;
import org.json.JSONObject;

public class Businesses extends BaseItem {
	private int id;// 商户ID
	private String address;// 商户地址
	private double latitude;// 商户纬度
	private double longitude;// 商户经度
	private String name;// 商户名
	private String h5_url;// 商户页链接
	private String url;// 商户页链接
	private String city;// 城市

	public Businesses() {
		super();
		setSerializable(Businesses.class.getName());
	}

	public Businesses(JSONObject businesses) throws JSONException {
		setId(businesses.getInt("id"));
		if (businesses.has("address")) {
			setAddress(businesses.getString("address"));
		}
		setLatitude(businesses.getDouble("latitude"));
		setLongitude(businesses.getDouble("longitude"));
		setName(businesses.getString("name"));
		setH5_url(businesses.getString("h5_url"));
		setUrl(businesses.getString("h5_url"));
		setCity(businesses.getString("city"));
		setSerializable(Businesses.class.getName());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getH5_url() {
		return h5_url;
	}

	public void setH5_url(String h5_url) {
		this.h5_url = h5_url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
