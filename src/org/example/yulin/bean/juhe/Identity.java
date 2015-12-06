package org.example.yulin.bean.juhe;

import org.example.yulin.bean.BaseItem;
import org.json.JSONException;
import org.json.JSONObject;

public class Identity extends BaseItem {
	// "result":{"area":"江苏省南通市海门市","sex":"男","birthday":"1993年10月16日","verify":""},"error_code":0}
	private String area;// 籍贯
	private String sex;// 性别
	private String birthday;// 生日
	private String verify;// 验证

	public Identity() {

	}

	public Identity(JSONObject result) throws JSONException {
		result = result.getJSONObject("result");
		setArea(result.getString("area"));
		setSex(result.getString("sex"));
		setBirthday(result.getString("birthday"));
		setVerify(result.getString("verify"));
	}

	@Override
	public String toString() {
		return "籍贯:" + area + "\n性别:" + sex + "\n出生年月:" + birthday + "\n"
				+ verify;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

}
