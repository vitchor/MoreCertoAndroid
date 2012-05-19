package com.br.morecerto.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.br.morecerto.R;
import com.google.android.maps.MapActivity;

public class Map extends MapActivity implements TextWatcher {
	
	private EditText mSearchField;
	//private GoogleService mGoogleService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mSearchField = (EditText) findViewById(R.id.search_field);
		mSearchField.addTextChangedListener(this);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		/*mGoogleService.cancelDownload();
		if (mRunnable != null) {
			mRequestSender.removeCallbacks(mRunnable);
		}
		if (!sequence.toString().equals("")) {
			mRunnable = new Runnable() {
				@Override
				public void run() {
					mGoogleService.sendGeocodeRequest(mPosition, mSearchField.getText().toString());
					showLoading();
				}
			};
			mRequestSender.postDelayed(mRunnable, 1500);
		} else {
			mMsgTextView.setText(UberActivity.getStringValue(R.string.enter_pickup_address, this));
			mSearchResult.clear();
			updateList();
		}*/
		
	}
}