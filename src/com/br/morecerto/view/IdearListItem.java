package com.br.morecerto.view;

import android.graphics.drawable.Drawable;

public class IdearListItem {

	private long mId;
	private String mTitle;
	private String mSubtitle;
	private Drawable mLeft;
	private Drawable mRight;
	private boolean mIsSelected;
	private Object mTag;
	private String mDefaultMessage;
	private int mStyle = -1;

	public IdearListItem(long id, String title) {
		super();
		this.mId = id;
		this.mTitle = title;
		this.mIsSelected = false;
	}
	
	public IdearListItem(long id, String title, int style) {
		super();
		this.mId = id;
		this.mTitle = title;
		this.mStyle= style;
		this.mIsSelected = false;
	}
	
	public boolean equals(Object element) {
		if (element instanceof IdearListItem) {
			return mId == ((IdearListItem) element).mId;
		}
		return false;
	}

	public int getStyle(){
		return mStyle;
	}
	
	public long getId() {
		return mId;
	}

	public void setId(long id) {
		this.mId = id;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getSubtitle() {
		return mSubtitle;
	}

	public void setSubtitle(String subtitle) {
		this.mSubtitle = subtitle;
	}

	public Drawable getLeft() {
		return mLeft;
	}

	public void setLeft(Drawable left) {
		this.mLeft = left;
	}

	public Drawable getRight() {
		return mRight;
	}

	public void setRight(Drawable right) {
		this.mRight = right;
	}
	
	public void setSelected(boolean select) {
		this.mIsSelected = select;
	}
	
	public boolean isSelected() {
		return mIsSelected;
	}
	
	public void setTag(Object tag) {
		this.mTag = tag;
	}
	
	public Object getTag() {
		return mTag;
	}

	public String getStatusMsg() {
		// TODO return "DEFAULT" or ""
		return mDefaultMessage;
	}
	
	public void setStatusMsg(String defaultMessage) {
		this.mDefaultMessage = defaultMessage;
		// TODO return "DEFAULT" or ""
	}

}
