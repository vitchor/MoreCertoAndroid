package com.br.morecerto.controller.service;

import java.net.URLEncoder;
import java.util.HashMap;

import android.os.AsyncTask;

import com.br.morecerto.Commons;
import com.br.morecerto.controller.network.Downloader;
import com.br.morecerto.controller.network.OnDownloadListener;
import com.br.morecerto.controller.network.Response;
import com.br.morecerto.controller.network.UrlAddress;
import com.br.morecerto.controller.utility.GeoUtil;
import com.br.morecerto.controller.utility.Util;
import com.google.android.maps.GeoPoint;

public class GoogleService {

	private static final String GOOGLE_API_URL = "http://maps.googleapis.com";
	private static final String GOOGLE_API_PATH = "/maps/api/geocode/json";
	public static final int REQUEST_GEOCODE = 3121;
	private Downloader mDownloader;
	private OnDownloadListener mDownloadListener;

	public void setOnDownloadListener(OnDownloadListener listener) {
		mDownloadListener = listener;
		if (mDownloader != null) {
			mDownloader.setOnDownloadListener(listener);
		}
	}

	public void cancelDownload() {
		if (mDownloader != null) {
			mDownloader.cancel(true);
			mDownloader = null;
		}
	}

	public void sendGeocodeRequest(GeoPoint position, String geoAddress) {
		if (mDownloadListener != null) {
			final double latitude = GeoUtil.toDegree(position.getLatitudeE6());
			final double longitude = GeoUtil.toDegree(position.getLongitudeE6());
			String paramsPath = GOOGLE_API_PATH;
			paramsPath += "?address=" + URLEncoder.encode(geoAddress+ "..., BR");
			paramsPath += "&bounds=" + URLEncoder.encode((latitude - 0.1) + "," + (longitude - 0.1) + "|" + (latitude + 0.1) + "," + (longitude + 0.1));
			paramsPath += "&sensor=true";
			paramsPath += "&language=pt-BR";
			paramsPath += "&region=br";
			sendRequest(GOOGLE_API_URL, paramsPath, REQUEST_GEOCODE, Response.JSON_TYPE);
		}
	}

	public void sendGeocodeRequest(GeoPoint position) {
		if (mDownloadListener != null) {
			String paramsPath = GOOGLE_API_PATH;
			paramsPath += "?latlng=" + GeoUtil.toDegree(position.getLatitudeE6()) + "," + GeoUtil.toDegree(position.getLongitudeE6());
			paramsPath += "&client=" + Commons.GOOGLE_MAPS_CLIENT_ID;
			paramsPath += "&sensor=false&oe=utf8";
			paramsPath += "&signature=" + Util.signGoogleUrl(paramsPath, Commons.GOOGLE_MAPS_API_KEY);
			sendRequest(GOOGLE_API_URL, paramsPath, REQUEST_GEOCODE, Response.JSON_TYPE);
		}
	}

	public void sendRequest(String url, HashMap<String, Object> params, int type, int responseType) {
		sendRequest(url, Downloader.toQueryString(params), type, responseType);
	}

	public void sendRequest(String url, String paramsPath, int type, int responseType) {
		if (mDownloader == null || mDownloader.getStatus() == AsyncTask.Status.FINISHED) {
			mDownloader = new Downloader();
			mDownloader.setOnDownloadListener(mDownloadListener);
		}
		mDownloader.addGet(new UrlAddress(url), paramsPath, type, responseType, Downloader.DOWNLOADER_NORMAL_PRIORITY);
	}
}
