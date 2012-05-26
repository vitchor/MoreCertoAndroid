package com.br.morecerto.controller.network;

import java.util.HashMap;
import java.util.Map;

import com.br.morecerto.controller.utility.Util;

public class IdearOnDownloadListener implements OnDownloadListener {

	private OnDownloadListener mOnDownloadListener;

	public static OnDownloadListener getInstance(OnDownloadListener onDownloadListener) {
		return new IdearOnDownloadListener(onDownloadListener);
	}

	public IdearOnDownloadListener(OnDownloadListener onDownloadListener) {
		this.mOnDownloadListener = onDownloadListener;
	}

	@Override
	public void onPreLoad(int type) {
		mOnDownloadListener.onPreLoad(type);
	}

	@Override
	public void onLoad(Response response) {
		mOnDownloadListener.onLoad(response);
	}

	@Override
	public void onError(int type, Request request, Exception exception) {
		final Map<String, String> params = new HashMap<String, String>();
		if (request != null) {
			params.put("request_type", request.getType() + "");
			if (request.getUrlAddress() != null) {
				params.put("request_url", request.getUrlAddress().getAddress() + request.getPath());
			}
			params.put("request_rotation_count", request.getRotationCount() + "");
			params.put("response_code", request.getResponseCode() + "");
		}
		if (exception != null) {
			params.put("message", exception.getMessage());
			params.put("stack_trace", Util.stackTraceStringValue(exception));
		}
		mOnDownloadListener.onError(type, request, exception);
	}

	@Override
	public void onCancel() {
		mOnDownloadListener.onCancel();
	}

}