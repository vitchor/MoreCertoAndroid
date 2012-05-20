package com.br.morecerto.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.morecerto.R;
import com.br.morecerto.controller.utility.ImageCache;

public class IdearListAdapter extends BaseAdapter {
	
	public static final int UBER_DEFAULT_STYLE = 0;
	public static final int UBER_SUBTITLE_STYLE = 1;
	public static final int UBER_SUBTITLE_WITH_TAG_STYLE = 2;

	private ArrayList<IdearListItem> mItems;
	private HashMap<Long, IdearListItem> mItemsMap;
	private Drawable mDefaultLeft;
	private Drawable mDefaultRight;
	private Drawable mDefaultRightChecked;
	private LayoutInflater mInflater;
	private int mStyle = -1;
	private int rowHeight = -1;
	private boolean mIsBoldEnabled = true;
	private float titleSize = -1;
	private OnCheckedChangeListener mOnCheckedChangeListener;
	private final ImageCache mCache = new ImageCache();

	public IdearListAdapter(Context context, int style) {
		mInflater = LayoutInflater.from(context);
		mItems = new ArrayList<IdearListItem>();
		mItemsMap = new HashMap<Long, IdearListItem>();
		mStyle = style;
	}

	public void removeAll() {
		mItems.clear();
		mItemsMap.clear();
	}

	public void addItem(IdearListItem uberListItem) {
		mItems.add(uberListItem);
		mItemsMap.put(uberListItem.getId(), uberListItem);
	}

	public ArrayList<IdearListItem> getItems() {
		return mItems;
	}

	public float getTitleSize() {
		return titleSize;
	}

	public void setTitleSize(float titleSize) {
		this.titleSize = titleSize;
	}
	
	public int getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(int rowHeight) {
		this.rowHeight = rowHeight;
	}
	
	public boolean isBoldEnabled() {
		return mIsBoldEnabled;
	}

	public void setBoldEnabled(boolean isBoldEnabled) {
		this.mIsBoldEnabled = isBoldEnabled;
	}

	public Drawable getDefaultLeft() {
		return mDefaultLeft;
	}

	public void setDefaultLeft(Drawable defaultLeft) {
		this.mDefaultLeft = defaultLeft;
	}

	public Drawable getDefaultRight() {
		return mDefaultRight;
	}

	public void setDefaultRightChecked(Drawable defaultRightChecked) {
		this.mDefaultRightChecked = defaultRightChecked;
	}

	public Drawable getDefaultRightChecked() {
		return mDefaultRightChecked;
	}

	public void setDefaultRight(Drawable defaultRight) {
		this.mDefaultRight = defaultRight;
	}

	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		this.mOnCheckedChangeListener = listener;
	}

	public void putImageInCache(long id, Bitmap bitmap) {
		mCache.putImage(id, bitmap);
	}

	public Bitmap getCachedImage(long id) {
		return mCache.getImage(id);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UberHolderItem holder = null;
		final IdearListItem uberListItem = mItems.get(position);
		int style;
		if (uberListItem.getStyle() != -1) {
			style = uberListItem.getStyle();
		} else {
			style = mStyle;
		}
		if (convertView == null) {
			holder = new UberHolderItem();
			if (style == UBER_DEFAULT_STYLE) {
				convertView = mInflater.inflate(R.layout.uber_list_cell_default, parent, false);
				holder.subtitleView = null;
			} else if (style == UBER_SUBTITLE_STYLE) {
				convertView = mInflater.inflate(R.layout.uber_list_cell_subtitled, parent, false);
				holder.subtitleView = (TextView) convertView.findViewById(R.id.subtitle);
			} else if(style == UBER_SUBTITLE_WITH_TAG_STYLE) {
				convertView = mInflater.inflate(R.layout.uber_list_cell_subtitled_with_tag, parent, false);
				holder.subtitleView = (TextView) convertView.findViewById(R.id.subtitle);
				holder.tagTextView = (TextView) convertView.findViewById(R.id.tagMsg);
			}
			holder.leftImage = (ImageView) convertView.findViewById(R.id.left_image);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.right_check_box);
			holder.titleView = (TextView) convertView.findViewById(R.id.title);
			
		} else {
			holder = (UberHolderItem) convertView.getTag();
		}
		if (holder != null) {
			holder.id = uberListItem.getId();
			holder.titleView.setText(uberListItem.getTitle());
			holder.titleView.setTypeface(null, mIsBoldEnabled ? Typeface.BOLD : Typeface.NORMAL);
			if (holder.subtitleView != null) {
				holder.subtitleView.setText(uberListItem.getSubtitle());
			}
			if(holder.tagTextView != null){
				holder.tagTextView.setText(uberListItem.getStatusMsg());
			}
			// Left image
			final Drawable leftDrawable = uberListItem.getLeft();
			final Bitmap bitmap = mCache.getImage(uberListItem.getId());
			if (leftDrawable != null) {
				holder.leftImage.setImageDrawable(leftDrawable);
				holder.leftImage.setVisibility(View.VISIBLE);
			} else if (bitmap != null) {
				holder.leftImage.setImageBitmap(bitmap);
				holder.leftImage.setVisibility(View.VISIBLE);
			} else if (mDefaultLeft != null) {
				holder.leftImage.setImageDrawable(mDefaultLeft);
				holder.leftImage.setVisibility(View.VISIBLE);
			} else {
				holder.leftImage.setImageDrawable(null);
				holder.leftImage.setVisibility(View.GONE);
			}
			// Right image
			final Drawable rightDrawable = uberListItem.getRight();
			if (rightDrawable != null && mDefaultRightChecked != null) {
				// It's a checkable item.
				holder.checkBox.setButtonDrawable(rightDrawable);
				holder.checkBox.setVisibility(View.VISIBLE);
			} else if (rightDrawable != null) {
				// It's not checkable.
				holder.checkBox.setButtonDrawable(R.color.transparent);
				holder.checkBox.setCompoundDrawablesWithIntrinsicBounds(rightDrawable, null, null, null);
				holder.checkBox.setVisibility(View.VISIBLE);
			} else if (mDefaultRight != null && mDefaultRightChecked != null) {
				// It's a checkable item.
				holder.checkBox.setButtonDrawable(uberListItem.isSelected() ? mDefaultRightChecked : mDefaultRight);
				holder.checkBox.setVisibility(View.VISIBLE);
			} else if (mDefaultRight != null) {
				// It's not checkable.
				holder.checkBox.setButtonDrawable(R.color.transparent);
				holder.checkBox.setCompoundDrawablesWithIntrinsicBounds(mDefaultRight, null, null, null);
				holder.checkBox.setVisibility(View.VISIBLE);
			} else {
				holder.checkBox.setButtonDrawable(null);
				holder.checkBox.setVisibility(View.GONE);
			}
			//Change height
			if (rowHeight != -1) { 
				LayoutParams params = convertView.getLayoutParams();
				params.height = rowHeight;
				convertView.setLayoutParams(params);
			}
			//Change titletextSize
			if (titleSize != -1) { 
				holder.titleView.setTextSize(titleSize);
			}
			convertView.setTag(holder);
			holder.checkBox.setOnCheckedChangeListener(mOnCheckedChangeListener);
			holder.checkBox.setTag(uberListItem.getId());
		}
		return convertView;
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mItems.get(position).getId();
	}

	public IdearListItem getListItem(long id) {
		return mItemsMap.get(id);
	}

}