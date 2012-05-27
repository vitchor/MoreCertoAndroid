package com.br.morecerto.view;

import com.br.morecerto.controller.utility.ILocation;
import com.google.android.maps.GeoPoint;

public class OverlayItem extends com.google.android.maps.OverlayItem implements ILocation {

	private Object tag = null;
	private boolean mIsClickable = false;
	private boolean mIsBubbleClickable = false;
	private GeoPoint mPoint;
	
	private String mSubtitle;
	private String mImageUrl;
	

	public String getSubtitle() {
		return mSubtitle;
	}
	
	public String getImageUrl() {
		return mImageUrl;
	}
	
	public OverlayItem(GeoPoint point, String title, String snippet) {
		super(point, title, snippet);
		this.mPoint = point;
	}

	public OverlayItem(GeoPoint point, String title, String snippet, Object tag) {
		super(point, title, snippet);
		this.tag = tag;
		this.mPoint = point;
	}

	public OverlayItem(GeoPoint point, String address, String imageUrl, String subtitle, String id) {
		super(point, address, address);
		this.tag = id;
		this.mPoint = point;
		this.mImageUrl = imageUrl;
		this.mSubtitle = subtitle;
		this.mImageUrl= imageUrl;
	}

	@Override
	public GeoPoint getPoint() {
		return mPoint;
	}

	public void setPoint(GeoPoint point) {
		this.mPoint = point;
	}

	public boolean isClickable() {
		return mIsClickable;
	}

	public void setClickable(boolean isClickable) {
		this.mIsClickable = isClickable;
	}

	public boolean isBubbleClickable() {
		return mIsBubbleClickable;
	}

	public void setBubbleClickable(boolean isClickable) {
		this.mIsBubbleClickable = isClickable;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	public Object getTag() {
		return tag;
	}

	@Override
	public int getLatitude() {
		return mPoint.getLatitudeE6();
	}

	@Override
	public int getLongitude() {
		return mPoint.getLongitudeE6();
	}
}