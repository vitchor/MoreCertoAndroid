package com.br.morecerto.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.br.morecerto.R;
import com.br.morecerto.controller.network.DataNode;
import com.br.morecerto.controller.network.OnDownloadListener;
import com.br.morecerto.controller.network.Request;
import com.br.morecerto.controller.network.Response;
import com.br.morecerto.controller.service.GoogleService;
import com.br.morecerto.controller.service.IdearService;
import com.br.morecerto.controller.utility.AnimationUtil;
import com.br.morecerto.controller.utility.GeoUtil;
import com.br.morecerto.controller.utility.NumberIcon;
import com.br.morecerto.model.Realstate;
import com.br.morecerto.model.UserRankings;
import com.br.morecerto.view.IdearListAdapter;
import com.br.morecerto.view.IdearListItem;
import com.br.morecerto.view.IdearMapView;
import com.br.morecerto.view.IdearOverlay;
import com.br.morecerto.view.IdearTextItem;
import com.br.morecerto.view.IdearToolbar;
import com.br.morecerto.view.IdearToolbarItem;
import com.br.morecerto.view.OnFocusListener;
import com.br.morecerto.view.OnToolbarListener;
import com.br.morecerto.view.OverlayItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.Overlay;

public class Map extends MapActivity implements TextWatcher, OnDownloadListener, OnFocusChangeListener, OnClickListener, OnTouchListener, OnItemClickListener, OnFocusListener, OnToolbarListener, OnCheckedChangeListener, OnGestureListener, OnDoubleTapListener {

	private static int MORE_INFORMATION = 1;

	// Views
	private EditText mSearchField;
	private IdearMapView mMapView;
	private ListView mListView;
	private TextView mMsgTextView;
	private ProgressBar mSpinner;
	private Button mSearchbutton;
	private LinearLayout mSearchMessageWrapper;
	private LinearLayout mSearchListWrapper;
	private IdearToolbar mToolbar;

	// Controllers
	private GoogleService mGoogleService;
	private Runnable mRunnable;
	private Handler mRequestSender;
	private IdearListAdapter mListAdapter;

	// Models
	private ArrayList<DataNode> mSearchResult = new ArrayList<DataNode>();
	private String mSearchType = Realstate.TYPE_RENT;

	// Others
	private android.view.ViewGroup.LayoutParams mSearchFieldParams;
	private IdearService mIdearService;

	private View mSlidersView;

	private Location mActuallocation;

	private ArrayList<Realstate> mActualRealstates;

	private RadioGroup mRadioGroup;

	private GestureDetector mMapGestureDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViewById(R.id.search_view).bringToFront();
		mToolbar = (IdearToolbar) findViewById(R.id.toolbar);
		mToolbar.setOnToolbarListener(this);
		IdearTextItem item = new IdearTextItem(MORE_INFORMATION, "Busca Avanada");
		item.setStyle(IdearToolbarItem.BLUE_BUTTON);
		mToolbar.setRightItem(item);

		mMapView = (IdearMapView) findViewById(R.id.map_view);
		mMapView.setOnTouchListener(this);

		mMapGestureDetector = new GestureDetector(this, this);
		mMapGestureDetector.setOnDoubleTapListener(this);

		mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
		mRadioGroup.setOnCheckedChangeListener(this);

		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		mActuallocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (mActuallocation != null) {
			mMapView.getController().setCenter(new GeoPoint(GeoUtil.toMicroDegree(mActuallocation.getLatitude()), GeoUtil.toMicroDegree(mActuallocation.getLongitude())));
			mMapView.getController().setZoom(16);
		}

		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				if (mActuallocation == null || mActuallocation.distanceTo(location) > 300) {
					mMapView.getController().setCenter(new GeoPoint(GeoUtil.toMicroDegree(location.getLatitude()), GeoUtil.toMicroDegree(location.getLongitude())));
					mMapView.getController().setZoom(16);
				}

				final LocationManager locationManager = (LocationManager) Map.this.getSystemService(Context.LOCATION_SERVICE);
				locationManager.removeUpdates(this);
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		try {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mIdearService = new IdearService();
		mIdearService.setOnDownloadListener(this);
		final GeoPoint point = mMapView.getMapCenter();

		mIdearService.sendNearPlacesRequest(GeoUtil.toDegree(point.getLatitudeE6()), GeoUtil.toDegree(point.getLongitudeE6()), 1, mSearchType);

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

		mListAdapter = new IdearListAdapter(this, IdearListAdapter.IDEAR_DEFAULT_STYLE);
		mListAdapter.setRowHeight(45);

		mListView = (ListView) findViewById(R.id.list_view);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(this);

		mSearchField.setHint(getResources().getString(R.string.serch_field_msg));

		mSearchbutton = (Button) findViewById(R.id.search_button);
		mSearchbutton.setOnClickListener(this);

		mSlidersView = ((ViewStub) findViewById(R.id.help_list_stub)).inflate();
		mSlidersView.setVisibility(View.GONE);
		mSlidersView.bringToFront();

		setRankListeners();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		UserRankings.clearRanking();

		mIdearService.setOnDownloadListener(null);
		mIdearService.cancelDownload();

		mGoogleService.setOnDownloadListener(null);
		mGoogleService.cancelDownload();
	}

	private void setRankListeners() {

		@SuppressWarnings("rawtypes")
		Class[] parameterTypes = new Class[1];
		parameterTypes[0] = Integer.class;
		Method method;
		SeekBar bar;
		try {

			method = UserRankings.class.getMethod("setBarRating", parameterTypes);
			bar = (SeekBar) mSlidersView.findViewById(R.id.bar_rank);
			connectBarToRank(bar, method);

			method = UserRankings.class.getMethod("setBankRating", parameterTypes);
			bar = (SeekBar) mSlidersView.findViewById(R.id.bank_rank);
			connectBarToRank(bar, method);

			method = UserRankings.class.getMethod("setHealthRating", parameterTypes);
			bar = (SeekBar) mSlidersView.findViewById(R.id.hospital_rank);
			connectBarToRank(bar, method);

			method = UserRankings.class.getMethod("setStoreRating", parameterTypes);
			bar = (SeekBar) mSlidersView.findViewById(R.id.store_rank);
			connectBarToRank(bar, method);

			method = UserRankings.class.getMethod("setRestaurantRating", parameterTypes);
			bar = (SeekBar) mSlidersView.findViewById(R.id.restaurants_rank);
			connectBarToRank(bar, method);

			method = UserRankings.class.getMethod("setMarketRating", parameterTypes);
			bar = (SeekBar) mSlidersView.findViewById(R.id.supermarket_rank);
			connectBarToRank(bar, method);

			method = UserRankings.class.getMethod("setGasStationRating", parameterTypes);
			bar = (SeekBar) mSlidersView.findViewById(R.id.gas_station_rank);
			connectBarToRank(bar, method);

			method = UserRankings.class.getMethod("setPriceRating", parameterTypes);
			bar = (SeekBar) mSlidersView.findViewById(R.id.price_rank);
			connectBarToRank(bar, method);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void connectBarToRank(SeekBar bar, final Method method) {
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (progress == 0) {
					progress = 1;
				}

				Object[] parameters = new Object[1];
				parameters[0] = new Integer(progress);
				Object obj = new UserRankings();
				try {
					method.invoke(obj, parameters);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

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
					mGoogleService.sendGeocodeRequest(Map.this.mMapView.getMapCenter(), mSearchField.getText().toString());
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
		Log.i("GOOGLE", "PRELOAD");
	}

	@Override
	public void onLoad(Response response) {
		Log.i("GOOGLE", "LOAD");
		final int type = response.getRequest().getType();

		if (type == IdearService.REQUEST_NEAR_PLACES) {
			mActualRealstates = Realstate.updateWithResponse(response);

			updateMap(mActualRealstates);

		} else {

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

	}

	private void updateMap(ArrayList<Realstate> realstates) {

		List<Overlay> mapOverlays = mMapView.getOverlays();
		mapOverlays.clear();
		if (realstates != null) {
			for (Realstate realstate : realstates) {
				if (realstates.size() > 0) {

					mapOverlays = mMapView.getOverlays();
					IdearOverlay mOverlay = new IdearOverlay(NumberIcon.getIcon(this, realstate.getRating()));
					// mOverlay.
					mOverlay.setOnFocusListener(this);
					mapOverlays.add(mOverlay);

					final OverlayItem item = new OverlayItem(new GeoPoint(GeoUtil.toMicroDegree(realstate.lat), GeoUtil.toMicroDegree(realstate.lng)), realstate.address, realstate.agencyUrl, realstate.id);
					item.setClickable(true);
					mOverlay.addItem(item);

					mOverlay.invalidate();
					mMapView.invalidate();

				}
			}
		}
	}

	@Override
	public void onError(int type, Request request, Exception exception) {
		Log.i("GOOGLE", "ERROR");
		Log.i("GOOGLE", "type: " + type);
		if (exception != null){
			Log.i("GOOGLE", "exception: " + exception.getStackTrace().toString());
		}
	}

	@Override
	public void onCancel() {
		Log.i("GOOGLE", "CANCEL");
	}

	private void updateList() {
		if (mSearchResult != null) {

			mListAdapter = new IdearListAdapter(this, IdearListAdapter.IDEAR_DEFAULT_STYLE);
			mListAdapter.setRowHeight(45);

			int i = 0;
			for (DataNode dataNode : mSearchResult) {
				final IdearListItem item = new IdearListItem(i, dataNode.findString("formatted_address", ""));
				mListAdapter.addItem(item);
				i++;
			}

			mListView.setAdapter(mListAdapter);
		}
		if (mSearchResult.size() > 3) {
			mListView.getLayoutParams().height = mListAdapter.getRowHeight() * 3;
		} else {
			mListView.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
		}

		if (mSearchResult.size() > 0) {
			AnimationUtil.executeAnimation(this, R.anim.fade_in, mSearchListWrapper, View.VISIBLE);
		} else {
			AnimationUtil.executeAnimation(this, R.anim.fade_out, mSearchListWrapper, View.GONE);

			mListAdapter.notifyDataSetInvalidated();
			mListView.invalidate();
			// mListView.
			// onContentChanged();
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

			AnimationUtil.executeAnimation(this, R.anim.shrink, mSearchField, View.VISIBLE);
			AnimationUtil.executeAnimation(this, R.anim.slide_in_right, mSearchbutton, View.VISIBLE);
			AnimationUtil.executeAnimation(this, R.anim.slide_out_top, mRadioGroup, View.GONE, 300);
			AnimationUtil.executeAnimation(this, R.anim.slide_in_top, mSearchMessageWrapper, View.VISIBLE, 600);

		} else {

		}
	}

	@Override
	public void onClick(View view) {
		hideSearchOptions();

	}

	private void hideSearchOptions() {
		mSearchField.clearFocus();
		mSearchField.setText("");
		mSearchField.setLayoutParams(new LinearLayout.LayoutParams(mSearchField.getWidth(), LayoutParams.FILL_PARENT));
		mSearchField.invalidate();

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mSearchField.getWindowToken(), 0);

		AnimationUtil.executeAnimation(this, R.anim.slide_out_right, mSearchbutton, View.GONE);

		// Calculate window size - padding
		final int width = getWindowManager().getDefaultDisplay().getWidth() - 10;

		// Store initial params
		mSearchFieldParams = mSearchField.getLayoutParams();

		mSearchField.setLayoutParams(new LinearLayout.LayoutParams(width, LayoutParams.FILL_PARENT));

		AnimationUtil.executeAnimation(this, R.anim.grow, mSearchField, View.VISIBLE, 100);
		AnimationUtil.executeAnimation(this, R.anim.slide_out_top, mSearchMessageWrapper, View.GONE, 300);
		AnimationUtil.executeAnimation(this, R.anim.slide_in_top, mRadioGroup, View.VISIBLE, 600);
		AnimationUtil.executeAnimation(this, R.anim.fade_out, mSearchListWrapper, View.GONE);
		mSearchField.invalidate();

		mListAdapter.removeAll();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			final GeoPoint point = mMapView.getMapCenter();
			sendNearPlacesRequest(point);
		}
		return mMapGestureDetector.onTouchEvent(event);

	}

	private void sendNearPlacesRequest(GeoPoint point) {
		mIdearService.clearQueue();
		mIdearService.sendNearPlacesRequest(GeoUtil.toDegree(point.getLatitudeE6()), GeoUtil.toDegree(point.getLongitudeE6()), 1, mSearchType);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		final DataNode selectedAddress = mSearchResult.get(position);
		if (selectedAddress != null) {
			final DataNode geometryNode = selectedAddress.getNode("geometry");
			if (geometryNode != null) {
				final DataNode locationNode = geometryNode.getNode("location");

				GeoPoint point = new GeoPoint(GeoUtil.toMicroDegree(locationNode.findDouble("lat", 0.0)), GeoUtil.toMicroDegree(locationNode.findDouble("lng", 0.0)));
				mMapView.getController().animateTo(point);

				hideSearchOptions();

				sendNearPlacesRequest(point);
			}
		}

	}

	@Override
	public void onOverlayItemClick(OverlayItem item) {
		if (item.isClickable()) {
			// mMapView.getController().setCenter(item.getPoint());
			// mMapView.getController().setZoom(18);
			showBubble(item);
		}

	}

	void showBubble(OverlayItem item) {
		if (item != null) {
			item = updateLocationInfoOnItem(item);
			mMapView.showBubble(item);
		}
	}

	private OverlayItem updateLocationInfoOnItem(OverlayItem item) {
		if (item != null && item.getTag() != null) {

			ArrayList<Realstate> realstates = Realstate.mRealstates;
			for (Realstate realstate : realstates) {
				if (realstate.id.equals(item.getTag())) {
					Log.i("GOOGLE", realstate.imageUrl);
					item = new OverlayItem(item.getPoint(), realstate.address, "R$" + (int) realstate.price + ".00", realstate.imageUrl, realstate.agencyUrl, realstate.id);
					item.setClickable(true);
					// item.setBubbleClickable(true);
					break;
				}
			}

		}
		return item;
	}

	@Override
	public void onToolbarItemClick(View view) {
		ToggleButton button = (ToggleButton) view;
		if (button.isChecked()) {
			// AnimationUtil.fadeIn(this, mSlidersView);
			mSlidersView.setVisibility(View.VISIBLE);
		} else {
			// AnimationUtil.fadeOut(this, mSlidersView, View.GONE);
			updateMap(mActualRealstates);
			mSlidersView.setVisibility(View.GONE);
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		final String newSearchType;
		if (checkedId == R.id.rent_radio_button) {
			newSearchType = Realstate.TYPE_RENT;
		} else {
			newSearchType = Realstate.TYPE_BUY;
		}

		if (!newSearchType.equals(mSearchType)) {
			mSearchType = newSearchType;

			final GeoPoint point = mMapView.getMapCenter();
			mIdearService.sendNearPlacesRequest(GeoUtil.toDegree(point.getLatitudeE6()), GeoUtil.toDegree(point.getLongitudeE6()), 1, mSearchType);

			List<Overlay> mapOverlays = mMapView.getOverlays();
			mapOverlays.clear();

			mMapView.invalidate();

		}

	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		mMapView.getController().zoomInFixing((int) e.getX(), (int) e.getY());
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}