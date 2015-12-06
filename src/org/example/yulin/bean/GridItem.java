package org.example.yulin.bean;

public class GridItem extends BaseItem {
	int className;

	public GridItem() {
		super();
	}

	public GridItem(int resId, String resName, int className) {
		super(resId, resName);
		this.className = className;
	}

	public int getClassName() {
		return className;
	}

	public void setClassName(int className) {
		this.className = className;
	}

}
