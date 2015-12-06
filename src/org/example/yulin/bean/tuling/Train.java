package org.example.yulin.bean.tuling;

public class Train extends TuLing {
	String trainnum;// 车次
	String start;// 起始站
	String terminal;// 到达站
	String starttime;// 开车时间
	String endtime;// 到达时间
	String detailurl;// 详情地址
	String icon;// 图标地址

	public Train() {
		super();
	}

	public Train(String trainnum, String start, String terminal,
			String starttime, String endtime, String detailurl, String icon) {
		super();
		this.trainnum = trainnum;
		this.start = start;
		this.terminal = terminal;
		this.starttime = starttime;
		this.endtime = endtime;
		this.detailurl = detailurl;
		this.icon = icon;
	}

	public String getTrainnum() {
		return trainnum;
	}

	public void setTrainnum(String trainnum) {
		this.trainnum = trainnum;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
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
