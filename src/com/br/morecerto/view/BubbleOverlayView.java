package com.br.morecerto.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.br.morecerto.R;
import com.google.android.maps.OverlayItem;

public class BubbleOverlayView<Item extends OverlayItem> extends FrameLayout {

	private TextView mTitle;

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
		setClickable(isClickable);
	}

	public void setTitle(String title) {
		mTitle.setVisibility(VISIBLE);
		mTitle.setText(title);
	}
}