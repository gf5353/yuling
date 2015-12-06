package org.example.yulin.bean.tuling;

public class Software extends TuLing {
	String name;
	String count;// 下载量
	String detailurl;
	String icon;

	public Software() {
		super();
	}

	public Software(String name, String count, String detailurl, String icon) {
		super();
		this.name = name;
		this.count = count;
		this.detailurl = detailurl;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getDetailurl() {
		return detailurl;
	}

	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
