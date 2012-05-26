package com.br.morecerto.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.br.morecerto.controller.utility.ILocation;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class IdearMapView extends MapView implements OnClickListener {

	private BubbleOverlayView<OverlayItem> mBubbleOverlayView;
	private OnBubbleClickListener mListener;

	public IdearMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void showBubble(String title, GeoPoint point, Object tag, boolean isClickable) {
		if (title != null && !title.equals("") && point != null) {
			removeAllViews();
			mBubbleOverlayView = new BubbleOverlayView<OverlayItem>(getContext(), isClickable);
			MapView.LayoutParams params = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, point, MapView.LayoutParams.BOTTOM_CENTER);
			params.mode = MapView.LayoutParams.MODE_MAP;
			addView(mBubbleOverlayView, params);
			mBubbleOverlayView.setTitle(title);
			mBubbleOverlayView.setTag(tag);
			if (isClickable) {
				mBubbleOverlayView.setOnClickListener(this);
			}
		}
	}

	public void showBubble(OverlayItem item) {
		if (item != null) {
			showBubble(item.getTitle(), item.getPoint(), item.getTag(), item.isBubbleClickable());
		}
	}

	public void hideBubble() {
		removeAllViews();
	}

	public void cleanBubble() {
		if (mBubbleOverlayView != null) {
			mBubbleOverlayView.setVisibility(View.INVISIBLE);
			mBubbleOverlayView = null;
		}
	}

	public void setOnBubbleOverlayViewClickListener(OnBubbleClickListener listener) {
		mListener = listener;
	}

	@Override
	public void onClick(View view) {
		if (view instanceof BubbleOverlayView<?>) {
			mListener.onBubbleClick((BubbleOverlayView<?>) view);
		}
	}

	@Override
	public void invalidate() {
		if (mBubbleOverlayView != null) {
			mBubbleOverlayView.invalidate();
		}
		super.invalidate();
	}
	
	public ILocation getMapCenterLocation() {
		final GeoPoint point = getMapCenter();
		final ILocation iLocation = new ILocation() {
			@Override
			public int getLongitude() {
				return point.getLongitudeE6();
			}

			@Override
			public int getLatitude() {
				return point.getLatitudeE6();
			}
		};
		return iLocation;
	}

}