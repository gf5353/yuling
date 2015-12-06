package org.example.yulin.bean.juhe;

import org.example.yulin.bean.BaseItem;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 快递行程
 * 
 * @author Administrator
 *
 */
public class Trip extends BaseItem {
	private String datetime;// 时间
	private String remark;// 描述
	private String zone;// 区域

	public Trip(JSONObject jsonObject) throws JSONException {
		setDatetime(jsonObject.getString("datetime"));
		setRemark(jsonObject.getString("remark"));
		setZone(jsonObject.getString("zone"));

	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

}
