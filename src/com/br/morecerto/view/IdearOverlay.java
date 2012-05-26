package com.br.morecerto.view;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

public class IdearOverlay extends ItemizedOverlay<OverlayItem> {

	private final ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private OnFocusListener mListener;
	private int mFocusedIndex = -1;

	public IdearOverlay(Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
		// WTF: Crash without this line
		invalidate();
	}

	public void setOnFocusListener(OnFocusListener listener) {
		mListener = listener;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean pShadow) {
		// Draw without shadow
		super.draw(canvas, mapView, false);
	}

	@Override
	protected boolean onTap(int index) {
		if (mOverlays.size() - 1 >= index ) {
			final OverlayItem item = mOverlays.get(index);
			mFocusedIndex = index;
			return item.isClickable();
		} else {
			return false;
		}
	}

	@Override
	public boolean onTap(GeoPoint point, MapView mapView) {
		if (mapView instanceof IdearMapView) {
			final IdearMapView uberMapView = ((IdearMapView) mapView);
			if (super.onTap(point, mapView) && mFocusedIndex != -1) {
				final OverlayItem item = mOverlays.get(mFocusedIndex);
				if (mListener != null && item != null) {
					mListener.onOverlayItemClick(item);
				}
				return true;
			} else {
				uberMapView.hideBubble();
				return false;
			}
		} else {
			return false;
		}
	}

	public ArrayList<OverlayItem> getItems() {
		return mOverlays;
	}

	public void setItems(ArrayList<OverlayItem> overlays) {
		mOverlays.clear();
		mOverlays.addAll(overlays);
	}

	public void addItem(OverlayItem item) {
		mOverlays.add(item);
	}

	public void addItem(int latitudeE6, int longitudeE6) {
		addItem(new GeoPoint(latitudeE6, longitudeE6));
	}

	public void addItem(GeoPoint point) {
		addItem(new OverlayItem(point, "", ""));
	}

	public void removeAllItems() {
		mOverlays.clear();
	}

	public void invalidate() {
		setLastFocusedIndex(-1);
		populate();
	}

}