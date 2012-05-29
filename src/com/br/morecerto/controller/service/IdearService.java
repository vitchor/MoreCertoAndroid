package com.br.morecerto.controller.service;

import android.os.AsyncTask;

import com.br.morecerto.Commons;
import com.br.morecerto.controller.network.Downloader;
import com.br.morecerto.controller.network.IdearOnDownloadListener;
import com.br.morecerto.controller.network.OnDownloadListener;
import com.br.morecerto.controller.network.Response;
import com.br.morecerto.controller.network.UrlAddress;

public class IdearService {

	private Downloader mDownloader;
	private OnDownloadListener mDownloadListener;
	public static int REQUEST_NEAR_PLACES = 0;

	public void sendNearPlacesRequest(double lat, double lng, double radius, String type) {
		send(new UrlAddress("http://www.morecerto.com.br/realestates/get"), "", "lat=" + lat + "&lng=" + lng + "&radius=" + radius + "&type=" + type, null, REQUEST_NEAR_PLACES, Response.JSON_TYPE, null, Downloader.DOWNLOADER_NORMAL_PRIORITY, "POST");
	}

	private synchronized void send(UrlAddress urlAddress, String path, String postRequest, String contentType, int type, int responseType, Object tag, int priority, String requestMethod) {
		if (mDownloadListener != null) {
			if (mDownloader == null || mDownloader.getStatus() == AsyncTask.Status.FINISHED) {
				mDownloader = new Downloader();
				mDownloader.setLoggingMode(Commons.LOGS);
				mDownloader.setOnDownloadListener(mDownloadListener);
			}
			mDownloader.addRequest(urlAddress, path, postRequest, null, type, responseType, tag, priority, requestMethod);
		}
	}

	public void setOnDownloadListener(OnDownloadListener listener) {
		if (listener != null) {
			listener = IdearOnDownloadListener.getInstance(listener);
		}
		if (mDownloader != null) {
			mDownloader.setOnDownloadListener(listener);
		}
		mDownloadListener = listener;
	}

	public void cancelDownload() {
		if (mDownloader != null) {
			mDownloader.cancel(false);
		}
	}

	public void clearQueue() {
		if (mDownloader != null) {
			mDownloader.clearQueue();
		}
	}

}
