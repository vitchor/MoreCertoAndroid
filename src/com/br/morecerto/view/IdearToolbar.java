package com.br.morecerto.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.morecerto.R;
import com.br.morecerto.controller.utility.AnimationUtil;

public class IdearToolbar extends LinearLayout implements OnTouchListener, OnClickListener {

	// UI
	private TextView mTitleTextView;
	private Button mLeftTextButton;
	private ImageButton mLeftImageButton;
	private Button mRightTextButton;
	private ImageButton mRightImageButton;
	private ProgressBar mSpinner;

	// Data binding
	private IdearToolbarItem mLeftItem;
	private IdearToolbarItem mRightItem;
	private OnToolbarListener mListener;

	public IdearToolbar(Context context) {
		super(context);
		init(context);
	}

	public IdearToolbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.idear_toolbar, this);
		mTitleTextView = (TextView) view.findViewById(R.id.title);
		view.findViewById(R.id.left_side).setOnTouchListener(this);
		mLeftTextButton = (Button) view.findViewById(R.id.left_text_button);
		mLeftTextButton.setOnClickListener(this);
		mLeftImageButton = (ImageButton) view.findViewById(R.id.left_image_button);
		mLeftImageButton.setOnClickListener(this);
		view.findViewById(R.id.right_side).setOnTouchListener(this);
		mRightTextButton = (Button) view.findViewById(R.id.right_text_button);
		mRightTextButton.setOnClickListener(this);
		mRightImageButton = (ImageButton) view.findViewById(R.id.right_image_button);
		mRightImageButton.setOnClickListener(this);
		mSpinner = (ProgressBar) view.findViewById(R.id.spinner);
	}

	public void setOnToolbarListener(OnToolbarListener listener) {
		mListener = listener;

	}

	public void setLeftItem(IdearToolbarItem item) {
		mLeftItem = item;
		boolean animate = mLeftItem != item;
		hideView(mLeftImageButton, animate);
		hideView(mLeftTextButton, animate);
		if (mLeftItem instanceof IdearTextItem) {
			showTextButton(mLeftTextButton, (IdearTextItem) mLeftItem, animate);
		} else if (mLeftItem instanceof IdearImageItem) {
			showImageButton(mLeftImageButton, (IdearImageItem) mLeftItem, animate);
		}
	}

	public void setRightItem(IdearToolbarItem item) {
		mRightItem = item;
		boolean animate = mRightItem != item;
		hideView(mRightImageButton, animate);
		hideView(mRightTextButton, animate);
		if (mRightItem instanceof IdearTextItem) {
			showTextButton(mRightTextButton, (IdearTextItem) mRightItem, animate);
		} else if (mRightItem instanceof IdearImageItem) {
			showImageButton(mRightImageButton, (IdearImageItem) mRightItem, animate);
		}
	}

	public void setTitle(String title) {
		mTitleTextView.setText(title);
	}

	public void startSpinner() {
		final Context context = getContext();
		AnimationUtil.fadeOut(context, mRightTextButton, View.INVISIBLE);
		AnimationUtil.fadeOut(context, mRightImageButton, View.INVISIBLE);
		AnimationUtil.fadeIn(context, mSpinner);
	}

	public void stopSpinner() {
		AnimationUtil.fadeOut(getContext(), mSpinner, View.INVISIBLE);
		if (mRightItem instanceof IdearTextItem) {
			showTextButton(mRightTextButton, (IdearTextItem) mRightItem, true);
		} else if (mRightItem instanceof IdearImageItem) {
			showImageButton(mRightImageButton, (IdearImageItem) mRightItem, true);
		}
	}

	private void showTextButton(Button textButton, IdearTextItem item, boolean animate) {
		if (item.getStyle() == IdearToolbarItem.BLUE_BUTTON) {
			textButton.setBackgroundResource(R.drawable.idear_toolbar_blue_button);
		} else if (item.getStyle() == IdearToolbarItem.GRAY_BUTTON) {
			textButton.setBackgroundResource(R.drawable.toolbar_button);
		}
		textButton.setText(item.getTitle());
		showView(textButton, animate);
	}

	private void showImageButton(ImageButton imageButton, IdearImageItem item, boolean animate) {
		if (item.getStyle() == IdearToolbarItem.BLUE_BUTTON) {
			imageButton.setBackgroundResource(R.drawable.idear_toolbar_blue_button);
		} else if (item.getStyle() == IdearToolbarItem.GRAY_BUTTON) {
			imageButton.setBackgroundResource(R.drawable.toolbar_button);
		}
		imageButton.setImageDrawable(item.getDrawable());
		imageButton.setScaleType(ScaleType.CENTER_INSIDE);
		showView(imageButton, animate);
	}

	private void showView(View view, boolean animate) {
		if (animate) {
			AnimationUtil.fadeIn(getContext(), view);
		} else {
			view.setVisibility(View.VISIBLE);
		}
	}

	private void hideView(View view, boolean animate) {
		if (animate) {
			AnimationUtil.fadeOut(getContext(), view, View.INVISIBLE);
		} else {
			view.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (view.getId() == R.id.left_side) {
			View leftButton = null;
			if (mLeftTextButton.getVisibility() == View.VISIBLE) {
				leftButton = mLeftTextButton;
			} else if (mLeftImageButton.getVisibility() == View.VISIBLE) {
				leftButton = mLeftImageButton;
			}
			if (leftButton != null) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					leftButton.setPressed(true);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					leftButton.setPressed(false);
					leftButton.performClick();
				} else if (event.getAction() != MotionEvent.ACTION_MOVE) {
					leftButton.setPressed(false);
				}
			}
		} else if (view.getId() == R.id.right_side) {
			View rightButton = null;
			if (mRightTextButton.getVisibility() == View.VISIBLE) {
				rightButton = mRightTextButton;
			} else if (mRightImageButton.getVisibility() == View.VISIBLE) {
				rightButton = mRightImageButton;
			}
			if (rightButton != null) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					rightButton.setPressed(true);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					rightButton.setPressed(false);
					rightButton.performClick();
				} else if (event.getAction() != MotionEvent.ACTION_MOVE) {
					rightButton.setPressed(false);
				}
			}
		}
		return true;
	}

	@Override
	public void onClick(View view) {
		if (mListener != null) {
			if (view == mLeftImageButton || view == mLeftTextButton) {
				mListener.onToolbarItemClick(mLeftItem);
			} else if (view == mRightImageButton || view == mRightTextButton) {
				mListener.onToolbarItemClick(mRightItem);
			}
		}
	}
}