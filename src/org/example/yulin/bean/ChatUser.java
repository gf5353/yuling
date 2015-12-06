package org.example.yulin.bean;

import org.example.yulin.R;
import org.mobile.gqz.GQZLog;

import android.os.Parcel;

/**
 * 聊天对象
 * 
 * @author Administrator
 *
 */
public class ChatUser extends BaseItem {
	private String attribute;// 属性
	private String parameter;// 参数
	private String language;// 语言

	private int resIds[] = new int[] { R.drawable.icon_head_xiaoyan,
			R.drawable.icon_head_xiaomei, R.drawable.icon_head_xiaoli,
			R.drawable.icon_head_xiaorong, R.drawable.icon_head_xiaohui,
			R.drawable.icon_head_xiaokun, R.drawable.icon_head_xiaoqiang,
			R.drawable.icon_head_xiaoying, R.drawable.icon_head_xiaoxin };

	private String[] langages = new String[] { "普通话", "粤语", "台湾话", "四川话",
			"东北话", "河南话", "湖南话", "陕西话", "普通话" };

	public static String[] names = new String[] { "小燕", "小梅", "小莉", "小蓉", "小荟",
			"小坤", "小强", "小莹", "小新" };
	private String[] parames = new String[] { "xiaoyan", "vixm", "vixl",
			"vixr", "vixyun", "vixk", "vixqa", "vixying", "vixx" };

	private String[] attributes = new String[] { "青年女声", "青年女声", "青年女声",
			"青年女声", "青年女声", "青年男声", "青年男声", "青年女声", "童年男声" };

	@Override
	public void setResName(String resName) {
		super.setResName(resName);
		int i = 0;
		for (; i < names.length; i++) {
			if (resName.equals(names[i])) {
				setParameter(parames[i]);
				setLanguage(langages[i]);
				setAttribute(attributes[i]);
				setResId(resIds[i]);
				break;
			}
		}
		if (i == names.length) {
			GQZLog.debug("语音名称超出范围");
		}
	}

	@Override
	public String toString() {
		return "ChatUser [attribute=" + attribute + ", parameter=" + parameter
				+ ", language=" + language + "]";
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		super.writeToParcel(dest, flags);
//		dest.writeString(getAttribute());
//		dest.writeString(getLanguage());
//		dest.writeString(getParameter());
//	}
}
