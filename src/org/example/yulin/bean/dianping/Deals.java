package org.example.yulin.bean.dianping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.yulin.bean.BaseItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Deals extends BaseItem {
	private String deal_id;// 团购单ID
	private String title;// 标题
	private String city;// 城市
	private String details;// 详情
	private int is_popular;// 是否为热门团购，0：不是，1：是
	private String description;// 团购描述
	private String purchase_deadline;// 团购单的截止购买日期
	private int purchase_count;// 团购当前已购买数
	private double list_price;// 团购包含商品原价值
	private String notice;// 重要通知(一般为团购信息的临时变更)
	private String s_image_url;// 小尺寸团购图片链接，最大图片尺寸160×100
	private String image_url;// 团购图片链接，最大图片尺寸450×280
	private double current_price;// 团购价格
	private double commission_ratio;// 当前团单的佣金比例
	private String publish_date;// 团购发布上线日期
	private String deal_url;// 团购Web页面链接，适用于网页应用
	private String deal_h5_url;// 团购HTML5页面链接，适用于移动应用和联网车载应用

	private String[] more_image_urls;// 更多大尺寸图片
	private String[] more_s_image_urls;// 更多小尺寸图片
	private String[] categories;// 团购所属分类
	private String[] regions;// 团购适用商户所在商区

	private Restrictions restrictions;// 团购限制条件
	private List<Businesses> businesses;// 团购所适用的商户列表

	public Deals(JSONObject deals) {
		try {
			setDeal_id(deals.getString("deal_id"));
			setTitle(deals.getString("title"));
			setCity(deals.getString("city"));
			if (deals.has("details")) {// 团购详情
				setDetails(deals.getString("details"));
			}
			if (deals.has("is_popular")) {// 是否为热门团购，0：不是，1：是
				setIs_popular(deals.getInt("is_popular"));
			}

			setDescription(deals.getString("description"));
			setPurchase_deadline(deals.getString("purchase_deadline"));
			setPurchase_count(deals.getInt("purchase_count"));
			setList_price(deals.getDouble("list_price"));
			if (deals.has("notice")) {
				setNotice(deals.getString("notice"));
			}
			setImage_url(deals.getString("image_url"));
			setS_image_url(deals.getString("s_image_url"));
			setCurrent_price(deals.getDouble("current_price"));
			setCommission_ratio(deals.getDouble("commission_ratio"));
			setPublish_date(deals.getString("publish_date"));
			setDeal_url(deals.getString("deal_url"));
			setDeal_h5_url(deals.getString("deal_h5_url"));
			if (deals.has("more_image_urls")) {
				setMore_image_urls(deals.getJSONArray("more_image_urls"));
			}
			if (deals.has("more_s_image_urls")) {
				setMore_s_image_urls(deals.getJSONArray("more_s_image_urls"));
			}
			setCategories(deals.getJSONArray("categories"));
			setRegions(deals.getJSONArray("regions"));
			if (deals.has("restrictions")) {
				setRestrictions(deals.getJSONObject("restrictions"));
			}
			setBusinesses(deals.getJSONArray("businesses"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "Deals [deal_id=" + deal_id + ", title=" + title + ", city="
				+ city + ", details=" + details + ", is_popular=" + is_popular
				+ ", description=" + description + ", purchase_deadline="
				+ purchase_deadline + ", purchase_count=" + purchase_count
				+ ", list_price=" + list_price + ", notice=" + notice
				+ ", s_image_url=" + s_image_url + ", image_url=" + image_url
				+ ", current_price=" + current_price + ", commission_ratio="
				+ commission_ratio + ", publish_date=" + publish_date
				+ ", deal_url=" + deal_url + ", deal_h5_url=" + deal_h5_url
				+ ", more_image_urls=" + Arrays.toString(more_image_urls)
				+ ", more_s_image_urls=" + Arrays.toString(more_s_image_urls)
				+ ", categories=" + Arrays.toString(categories) + ", regions="
				+ Arrays.toString(regions) + "]";
	}

	public String getDeal_id() {
		return deal_id;
	}

	public void setDeal_id(String deal_id) {
		this.deal_id = deal_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public int getIs_popular() {
		return is_popular;
	}

	public void setIs_popular(int is_popular) {
		this.is_popular = is_popular;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPurchase_deadline() {
		return purchase_deadline;
	}

	public void setPurchase_deadline(String purchase_deadline) {
		this.purchase_deadline = purchase_deadline;
	}

	public int getPurchase_count() {
		return purchase_count;
	}

	public void setPurchase_count(int purchase_count) {
		this.purchase_count = purchase_count;
	}

	public double getList_price() {
		return list_price;
	}

	public void setList_price(double list_price) {
		this.list_price = list_price;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getS_image_url() {
		return s_image_url;
	}

	public void setS_image_url(String s_image_url) {
		this.s_image_url = s_image_url;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public double getCurrent_price() {
		return current_price;
	}

	public void setCurrent_price(double current_price) {
		this.current_price = current_price;
	}

	public double getCommission_ratio() {
		return commission_ratio;
	}

	public void setCommission_ratio(double commission_ratio) {
		this.commission_ratio = commission_ratio;
	}

	public String getPublish_date() {
		return publish_date;
	}

	public void setPublish_date(String publish_date) {
		this.publish_date = publish_date;
	}

	public String getDeal_url() {
		return deal_url;
	}

	public void setDeal_url(String deal_url) {
		this.deal_url = deal_url;
	}

	public String getDeal_h5_url() {
		return deal_h5_url;
	}

	public void setDeal_h5_url(String deal_h5_url) {
		this.deal_h5_url = deal_h5_url;
	}

	public String[] getMore_image_urls() {
		return more_image_urls;
	}

	public void setMore_image_urls(JSONArray more_image_urls)
			throws JSONException {
		this.more_image_urls = toList(more_image_urls);
	}

	public String[] getMore_s_image_urls() {
		return more_s_image_urls;
	}

	public void setMore_s_image_urls(JSONArray more_s_image_urls)
			throws JSONException {
		this.more_s_image_urls = toList(more_s_image_urls);
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(JSONArray categories) throws JSONException {
		this.categories = toList(categories);
	}

	public String[] getRegions() {
		return regions;
	}

	public void setRegions(JSONArray regions) throws JSONException {
		this.regions = toList(regions);
	}

	public Restrictions getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(JSONObject restrictions) throws JSONException {
		this.restrictions = new Restrictions(restrictions);
	}

	public List<Businesses> getBusinesses() {
		return businesses;
	}

	public void setBusinesses(JSONArray businesses) throws JSONException {
		List<Businesses> list = new ArrayList<Businesses>();
		for (int i = 0; i < businesses.length(); i++) {
			list.add(new Businesses(businesses.getJSONObject(i)));
		}
		this.businesses = list;
	}
}
