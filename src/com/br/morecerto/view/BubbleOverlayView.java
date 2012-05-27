package com.br.morecerto.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.br.morecerto.R;
import com.br.morecerto.controller.network.OnDownloadListener;
import com.br.morecerto.controller.network.Request;
import com.br.morecerto.controller.network.Response;
import com.google.android.maps.OverlayItem;

public class BubbleOverlayView<Item extends OverlayItem> extends FrameLayout implements OnDownloadListener{

	private TextView mTitle;
	private TextView mSubtitle;
	private ImageView mImage;

	public BubbleOverlayView(Context context, boolean isClickable) {
		super(context);
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view;
		LinearLayout layout = new LinearLayout(context);
		
		if (isClickable) {
			view = inflater.inflate(R.layout.bubble_clickable_overlay, layout);
		} else {
			view = inflater.inflate(R.layout.bubble_overlay, layout);
		}
		final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.NO_GRAVITY;
		layout.setVisibility(VISIBLE);
		addView(layout, params);
		
		mTitle = (TextView) view.findViewById(R.id.bubble_title);
		mSubtitle = (TextView) view.findViewById(R.id.bubble_subtitle);
		mImage = (ImageView) view.findViewById(R.id.image_view);
		
		setClickable(isClickable);
	}

	public void setTitle(String title) {
		mTitle.setVisibility(VISIBLE);
		mTitle.setText(title);
	}
	
	public void setSubtitle(String subtitle) {
		mSubtitle.setVisibility(VISIBLE);
		mSubtitle.setText(subtitle);
	}

	@Override
	public void onPreLoad(int type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoad(Response response) {
		
		Bitmap image = response.getBitmap();
		if (image != null){
			mImage.setImageBitmap(image);
		}
		
	}

	@Override
	public void onError(int type, Request request, Exception exception) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}
}