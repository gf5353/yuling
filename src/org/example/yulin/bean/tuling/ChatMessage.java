package org.example.yulin.bean.tuling;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessage {

	/**
	 * 消息类型
	 */
	private Type type;
	/**
	 * 消息内容
	 */
	private String msg;
	/**
	 * 日期
	 */
	private Date date;
	/**
	 * 日期的字符串格式
	 */
	private String dateStr;
	/**
	 * 发送人
	 */
	private String name;
	/**
	 * 发送人头像
	 */
	private String image;

	private String json;// 接受到的数据

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public enum Type {
		INPUT, OUTPUT
	}

	public ChatMessage() {
	}

	public ChatMessage(Type type, String msg, String name, String image) {
		super();
		this.type = type;
		this.msg = msg;
		this.name = name;
		this.image = image;
		setDate(new Date());
	}

	public ChatMessage(Type type, String msg, String name, String image,
			String json) {
		super();
		this.type = type;
		this.msg = msg;
		this.name = name;
		this.image = image;
		this.json = json;
		setDate(new Date());
	}

	public String getDateStr() {
		return dateStr;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.dateStr = df.format(date);

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
