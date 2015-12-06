package org.example.yulin.bean.dianping;

import org.example.yulin.bean.BaseItem;
import org.json.JSONException;
import org.json.JSONObject;

public class Restrictions extends BaseItem {
	private int is_reservation_required;// 是否需要预约，0：不是，1：是
	private int is_refundable;// 是否支持随时退款，0：不是，1：是
	private String special_tips;// 特别提示(一般为团购的限制信息)

	public Restrictions(JSONObject restrictions) throws JSONException {
		setIs_reservation_required(restrictions
				.getInt("is_reservation_required"));
		setIs_refundable(restrictions.getInt("is_refundable"));
		setSpecial_tips(restrictions.getString("special_tips"));
	}

	@Override
	public String toString() {
		return "Restrictions [is_reservation_required="
				+ is_reservation_required + ", is_refundable=" + is_refundable
				+ ", special_tips=" + special_tips + "]";
	}

	public int getIs_reservation_required() {
		return is_reservation_required;
	}

	public void setIs_reservation_required(int is_reservation_required) {
		this.is_reservation_required = is_reservation_required;
	}

	public int getIs_refundable() {
		return is_refundable;
	}

	public void setIs_refundable(int is_refundable) {
		this.is_refundable = is_refundable;
	}

	public String getSpecial_tips() {
		return special_tips;
	}

	public void setSpecial_tips(String special_tips) {
		this.special_tips = special_tips;
	}

}
