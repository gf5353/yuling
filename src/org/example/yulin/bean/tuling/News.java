package org.example.yulin.bean.tuling;

public class News extends TuLing {
	private String article;// 标题
	private String source;// 来源
	private String detailurl;// 详情地址
	private String icon;// 图标地址

	public News() {
		super();
	}

	public News(String article, String source, String detailurl, String icon) {
		super();
		this.article = article;
		this.source = source;
		this.detailurl = detailurl;
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "News [标题=" + article + ", 来源=" + source
				+ ", 详情地址=" + detailurl + ", 图标地址=" + icon + "]";
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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
