package org.example.yulin.bean.juhe;

import org.example.yulin.bean.BaseItem;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.backup.RestoreObserver;

public class Phone extends BaseItem {
	// {""result":{"province":"江苏","city":"南京","areacode":"025","zip":"210000","company":"中国移动","card":"移动187卡"},"error_code":0}
	private String province;// 省份
	private String city;// 城市
	private String areacode;// 区号
	private String zip;// 邮编
	private String company;// 所属
	private String card;// 号码段

	public Phone() {

	}

	public Phone(JSONObject result) throws JSONException {
		result = result.getJSONObject("result");
		setProvince(result.getString("province"));
		setCity(result.getString("city"));
		setAreacode(result.getString("areacode"));
		setZip(result.getString("zip"));
		setCompany(result.getString("company"));
		setCard(result.getString("card"));
	}

	@Override
	public String toString() {
		return "省份:" + province + "\n城市:" + city + "\n区号:" + areacode + "\n邮编:"
				+ zip + "\n" + company + "\n" + card;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

}
