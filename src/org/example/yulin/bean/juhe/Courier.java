package org.example.yulin.bean.juhe;

import org.example.yulin.bean.BaseItem;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 快递公司列表
 * 
 * @author Administrator
 *
 */
public class Courier extends BaseItem {
	private String com;// 公司名
	private String no;// 公司编号

	public Courier(JSONObject result) throws JSONException {
		setCom(result.getString("com"));
		setNo(result.getString("no"));
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
}
