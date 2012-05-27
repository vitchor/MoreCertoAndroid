package com.br.morecerto.view;

import android.graphics.drawable.Drawable;

public class IdearImageItem extends IdearToolbarItem {
	
	private Drawable mDrawable;
	
	public IdearImageItem(int id, Drawable drawable, int style) {
		super(id, style);
		mDrawable = drawable;
	}

	public IdearImageItem(int id, Drawable drawable) {
		super(id);
		mDrawable = drawable;
	}
	
	public void setDrawable(Drawable drawable) {
		mDrawable = drawable;
	}
	
	public Drawable getDrawable() {
		return mDrawable;
	}

}
