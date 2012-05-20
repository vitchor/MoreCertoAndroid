package com.br.morecerto.controller;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.br.morecerto.R;
import com.br.morecerto.controller.network.DataNode;
import com.br.morecerto.controller.network.OnDownloadListener;
import com.br.morecerto.controller.network.Request;
import com.br.morecerto.controller.network.Response;
import com.br.morecerto.controller.service.GoogleService;
import com.br.morecerto.controller.utility.AnimationUtil;
import com.br.morecerto.view.IdearListAdapter;
import com.br.morecerto.view.IdearListItem;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class Map extends MapActivity implements TextWatcher, OnDownloadListener, OnFocusChangeListener, OnClickListener {

	// Views
	private EditText mSearchField;
	private MapView mapView;
	private ListView mListView;
	private TextView mMsgTextView;
	private ProgressBar mSpinner;
	private Button mSearchbutton;
	private LinearLayout mSearchMessageWrapper;
	private LinearLayout mSearchListWrapper;

	// Controllers
	private GoogleService mGoogleService;
	private Runnable mRunnable;
	private Handler mRequestSender;
	private IdearListAdapter mListAdapter;

	// Models
	private ArrayList<DataNode> mSearchResult = new ArrayList<DataNode>();
	
	//Others
	private android.view.ViewGroup.LayoutParams mSearchFieldParams;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mSearchField = (EditText) findViewById(R.id.search_field);
		mSearchField.addTextChangedListener(this);
		mSearchField.setOnFocusChangeListener(this);

		mSearchMessageWrapper = (LinearLayout) findViewById(R.id.search_message_layout_wrapper);

		mSearchListWrapper = (LinearLayout) findViewById(R.id.search_list_wrapper);

		mMsgTextView = (TextView) findViewById(R.id.msg_text);
		mSpinner = (ProgressBar) findViewById(R.id.spinner);

		mGoogleService = new GoogleService();
		mGoogleService.setOnDownloadListener(this);

		mRequestSender = new Handler();

		mapView = (MapView) findViewById(R.id.map_view);

		mListAdapter = new IdearListAdapter(this, IdearListAdapter.UBER_DEFAULT_STYLE);
		mListAdapter.setRowHeight(45);

		mListView = (ListView) findViewById(R.id.list_view);
		mListView.setAdapter(mListAdapter);

		mSearchField.setHint(getResources().getString(R.string.serch_field_msg));

		mSearchbutton = (Button) findViewById(R.id.search_button);
		mSearchbutton.setOnClickListener(this);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence sequence, int start, int before, int count) {
		mGoogleService.cancelDownload();

		if (mRunnable != null) {
			mRequestSender.removeCallbacks(mRunnable);
		}

		if (!sequence.toString().equals("")) {
			mRunnable = new Runnable() {
				@Override
				public void run() {
					mGoogleService.sendGeocodeRequest(Map.this.mapView.getMapCenter(), mSearchField.getText().toString());
					showLoading();
				}
			};
			mRequestSender.postDelayed(mRunnable, 500);

		} else {
			mSearchResult.clear();
			mMsgTextView.setText(getResources().getString(R.string.type_to_start_search));
			updateList();
		}

	}

	@Override
	public void onPreLoad(int type) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLoad(Response response) {
		hideLoading();
		final DataNode data = response.getDataNode();
		if (data != null) {
			final DataNode results = data.getNode("results");
			if (results != null) {
				ArrayList<DataNode> resultsArray = results.getArray();
				int size = resultsArray.size();
				if (size == 0) {
					if (mSearchField.getText().toString().equals("")) {
						mMsgTextView.setText("");
					} else {
						mMsgTextView.setText(getResources().getString(R.string.no_results));
					}
				} else {
					mMsgTextView.setText(getResources().getString(R.string.matches_found, size, size == 1 ? "" : "s"));
				}
				mSearchResult = resultsArray;
				updateList();
			}
		}

	}

	@Override
	public void onError(int type, Request request, Exception exception) {
		Log.i("GOOGLE", "Got Error!");
	}

	@Override
	public void onCancel() {

	}

	private void updateList() {
		if (mSearchResult != null) {

			mListAdapter.removeAll();

			int i = 0;
			for (DataNode dataNode : mSearchResult) {
				final IdearListItem item = new IdearListItem(i, dataNode.findString("formatted_address", ""));
				mListAdapter.addItem(item);
				i++;
			}
		}
		if (mSearchResult.size() > 3) {
			mListView.getLayoutParams().height = mListAdapter.getRowHeight() * 3;
		} else {
			mListView.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
		}

		if (mSearchResult.size() > 0) {
			AnimationUtil.fadeIn(this, mSearchListWrapper);
		} else {
			AnimationUtil.fadeOut(this, mSearchListWrapper, View.GONE);

			mListAdapter.notifyDataSetChanged();
			mListView.invalidate();
			onContentChanged();
		}
	}

	protected void showLoading() {
		mSpinner.setVisibility(View.VISIBLE);
		mMsgTextView.setText(getResources().getString(R.string.loading));
	}

	private void hideLoading() {
		mSpinner.setVisibility(View.GONE);
	}

	@Override
	public void onFocusChange(View view, boolean arg1) {
		if (view == mSearchField) {

			if (mSearchField.getLayoutParams().width != android.view.ViewGroup.LayoutParams.FILL_PARENT) {
				mSearchField.setLayoutParams(mSearchFieldParams);
			}

			final Animation shrink = AnimationUtils.loadAnimation(this, R.anim.shrink);
			mSearchField.startAnimation(shrink);

			slideInRight(mSearchbutton);
			slideInTop(mSearchMessageWrapper);

		} else {

		}
	}

	
	private void slideInTop(View view) {
		if (view != null && view.getVisibility() != View.VISIBLE) {

			Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
			animation.setStartOffset(300);
			view.startAnimation(animation);
			view.setVisibility(View.VISIBLE);
		}
	}

	private void slideOutTop(View view) {
		if (view != null && view.getVisibility() == View.VISIBLE) {
			Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_top);
			animation.setStartOffset(300);
			view.startAnimation(animation);
			view.setVisibility(View.INVISIBLE);
		}
	}

	private void slideInRight(View view) {
		if (view != null && view.getVisibility() != View.VISIBLE) {
			Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
			animation.setStartOffset(50);
			view.startAnimation(animation);
			view.setVisibility(View.VISIBLE);
		}
	}

	private void slideOutRight(View view) {
		if (view != null && view.getVisibility() == View.VISIBLE) {
			Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
			view.startAnimation(animation);
			view.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		mSearchField.clearFocus();
		mSearchField.setText("");
		mSearchField.setLayoutParams(new LinearLayout.LayoutParams(mSearchField.getWidth(), LayoutParams.FILL_PARENT));
		mSearchField.invalidate();

		slideOutRight(mSearchbutton);

		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		
		mSearchFieldParams = mSearchField.getLayoutParams();
		mSearchField.setLayoutParams(new LinearLayout.LayoutParams(width - 10, LayoutParams.FILL_PARENT));

		Animation grow = AnimationUtils.loadAnimation(this, R.anim.grow);
		grow.setStartOffset(50);
		mSearchField.startAnimation(grow);

		slideOutTop(mSearchMessageWrapper);
		AnimationUtil.fadeOut(this, mSearchListWrapper, View.GONE);
		mSearchField.invalidate();

	}

}