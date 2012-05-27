package com.br.morecerto.view;

public class IdearToolbarItem {
	
	public static int GRAY_BUTTON = 0;
	public static int BLUE_BUTTON = 1;
	
	private int mId;
	private int mStyle;
	
	public IdearToolbarItem(int id) {
		this(id, GRAY_BUTTON);
	}
	
	public IdearToolbarItem(int id, int style) {
		mId = id;
		mStyle = style;
	}
	
	public void setId(int id) {
		mId = id;
	}
	
	public int getId() {
		return mId;
	}
	
	public void setStyle(int style) {
		mStyle = style;
	}
	
	public int getStyle() {
		return mStyle;
	}

}
