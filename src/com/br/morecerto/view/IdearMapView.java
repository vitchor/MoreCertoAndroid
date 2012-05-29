package com.br.morecerto.view;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.br.morecerto.controller.network.Downloader;
import com.br.morecerto.controller.network.OnDownloadListener;
import com.br.morecerto.controller.network.Response;
import com.br.morecerto.controller.network.UrlAddress;
import com.br.morecerto.controller.utility.ILocation;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class IdearMapView extends MapView implements OnClickListener {

	private BubbleOverlayView<OverlayItem> mBubbleOverlayView;
	private OnBubbleClickListener mListener;
	private Downloader mDownloader;

	public IdearMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void showBubble(String title, String snippet, String subtitle, String imageUrl, GeoPoint point, Object tag, boolean isClickable) {
		if (title != null && !title.equals("") && point != null) {

			if (mDownloader == null) {
				mDownloader = new Downloader();
			} else {
				mDownloader.cancel(true);
				mDownloader.clearQueue();
			}
			
			removeAllViews();
			mBubbleOverlayView = new BubbleOverlayView<OverlayItem>(getContext(), isClickable);
			MapView.LayoutParams params = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, point, MapView.LayoutParams.BOTTOM_CENTER);
			params.mode = MapView.LayoutParams.MODE_MAP;
			addView(mBubbleOverlayView, params);
			
			mBubbleOverlayView.setSnippet(snippet);
			mBubbleOverlayView.setTitle(title);
			mBubbleOverlayView.setSubtitle(subtitle);
			mBubbleOverlayView.setTag(tag);
			if (isClickable) {
				mBubbleOverlayView.setOnClickListener(this);
			}

			if (imageUrl != null) {
				loadImage(imageUrl, mBubbleOverlayView);
			}

		}
	}

	public void showBubble(OverlayItem item) {
		if (item != null) {
			showBubble(item.getTitle(), item.getSnippet(), item.getSubtitle(), item.getImageUrl(), item.getPoint(), item.getTag(), item.isBubbleClickable());
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

	private void loadImage(String pictureUrl, OnDownloadListener listener) {
		if (pictureUrl != null) {
			if (mDownloader == null || mDownloader.getStatus() == AsyncTask.Status.FINISHED) {
				mDownloader = new Downloader();
			}
			mDownloader.setOnDownloadListener(listener);

			mDownloader.addDownload(new UrlAddress(pictureUrl), 0, Response.IMAGE_TYPE, Downloader.DOWNLOADER_HIGH_PRIORITY);
		}
	}

}