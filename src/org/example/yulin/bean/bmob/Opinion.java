package org.example.yulin.bean.bmob;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * 反馈意见
 * 
 * @author admin
 *
 */
public class Opinion extends BmobObject {
	private String email;
	private String opinion;
	private BmobDate date;

	public Opinion() {
		setTableName("t_opinion");
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public BmobDate getDate() {
		return date;
	}

	public void setDate(BmobDate date) {
		this.date = date;
	}

}
