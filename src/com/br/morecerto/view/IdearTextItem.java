package com.br.morecerto.view;

public class IdearTextItem extends IdearToolbarItem {
	
	private String mTitle;
	
	public IdearTextItem(int id, String title, int style) {
		super(id, style);
		mTitle = title;
	}

	public IdearTextItem(int id, String title) {
		super(id);
		mTitle = title;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	
	public String getTitle() {
		return mTitle;
	}

}
