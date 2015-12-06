package org.example.yulin.bean.bmob;

import cn.bmob.v3.BmobObject;

public class BmobConfig extends BmobObject {

	private int main_tabs;
	private String url_pay;
	private int role_size;

	public int getRole_size() {
		return role_size;
	}

	public void setRole_size(int role_size) {
		this.role_size = role_size;
	}

	public String getUrl_pay() {
		return url_pay;
	}

	public void setUrl_pay(String url_pay) {
		this.url_pay = url_pay;
	}

	public int getMain_tabs() {
		return main_tabs;
	}

	public void setMain_tabs(int main_tabs) {
		this.main_tabs = main_tabs;
	}

}
