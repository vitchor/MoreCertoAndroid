/**
    UberAndroidNetwork: A JSON/XML network abstraction layer
    Copyright (c) 2011 by Jordan Bonnet, Uber Technologies

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
 */

package com.br.morecerto.controller.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class Downloader extends AsyncTask<Object, Object, Object> {

	private static final int DONE = 0;
	private static final int PRE_LOAD = 1;
	private static final int ERROR = 2;

	public final static int DOWNLOADER_NORMAL_PRIORITY = 0;
	public final static int DOWNLOADER_RETRY_LOW_PRIORITY = 1;
	public final static int DOWNLOADER_HIGH_PRIORITY = 2;

	private boolean mIsInLoggingMode = false;
	private boolean mIsConnected = true;
	private boolean mIsProgressUpdated = false;
	private OnDownloadListener mDownloadListener = null;
	private final Vector<Request> mRequestQueue = new Vector<Request>();
	private HttpURLConnection mConnection = null;
	private Bundle headerParams;

	public Downloader() {
		super();
	}

	public void clearQueue() {
		mRequestQueue.removeAllElements();
	}
	
	public Downloader(HttpURLConnection connection) {
		super();
		mConnection = connection;
	}

	public void setLoggingMode(boolean loggingMode) {
		mIsInLoggingMode = loggingMode;
	}

	public void setOnDownloadListener(OnDownloadListener listener) {
		mDownloadListener = listener;
	}

	public void addDownload(UrlAddress urlAddress, int type, int responseType, int priority) {
		addDownload(urlAddress, type, responseType, priority, null);
	}

	public void addDownload(UrlAddress urlAddress, int type, int responseType, int priority, Object tag) {
		final Request request = new Request(urlAddress, "", "GET", null, null, responseType, type, tag, priority);
		addDownload(request);
	}

	public void addGet(UrlAddress urlAddress, HashMap<String, Object> params, int type, int responseType, int priority) {
		addGet(urlAddress, toQueryString(params), type, responseType, priority);
	}

	public void addGet(UrlAddress urlAddress, String path, int type, int responseType, int priority) {
		final Request request = new Request(urlAddress, path, "GET", null, null, responseType, type, null, priority);
		addDownload(request);
	}

	public void addHead(UrlAddress urlAddress, int type, int responseType, int priority) {
		final Request request = new Request(urlAddress, "", "HEAD", null, null, responseType, type, null, priority);
		addDownload(request);
	}

	public void addPost(UrlAddress urlAddress, String path, String postRequest, String contentType, int type, int responseType, Object tag, int priority) {
		addRequest(urlAddress, path, postRequest, contentType, type, responseType, tag, priority, "POST");
	}

	public void addRequest(UrlAddress urlAddress, String path, String postRequest, String contentType, int type, int responseType, Object tag, int priority, String requestMethod) {
		final Request request = new Request(urlAddress, path, requestMethod, postRequest.getBytes(), contentType, responseType, type, tag, priority);
		addDownload(request);
	}

	public void addRequest(Request request) {
		if (request != null) {
			addDownload(request);
		}
	}

	private void addDownload(Request request) {
		if (mIsConnected) {
			mRequestQueue.add(request);
			if (getStatus() == AsyncTask.Status.PENDING) {
				mIsProgressUpdated = false;
				execute();
			}
		}
	}

	private void trustCertificate() {
		try {
			final SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { new IdearTrustManager() }, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		} catch (Exception e) {
			// Pretty fucked here
		}
	}

	/**
	 * This is a *smart* helper method which, given a request, manages to create
	 * the right kind of http connection.
	 * 
	 * @param request
	 * @return the connection object
	 * @throws IOException
	 */
	private HttpURLConnection connect(Request request) throws IOException {
		if (mConnection != null) {
			return mConnection;
		} else {
			HttpURLConnection connection = null;
			final UrlAddress urlAddress = request.getUrlAddress();
			if (urlAddress != null) {
				final URL url = new URL(urlAddress.getAddress() + request.getPath());
				final String protocol = url.getProtocol();
				if (protocol.equals("http")) {
					connection = (HttpURLConnection) url.openConnection();
				} else if (protocol.equals("https")) {
					trustCertificate();
					final HttpsURLConnection sslConnection = (HttpsURLConnection) url.openConnection();
					sslConnection.setHostnameVerifier(new IdearHostnameVerifier());
					connection = sslConnection;
				}
				if (connection != null) {
					if (headerParams != null) {
						for (String key : headerParams.keySet()) {
							final Object param = headerParams.get(key);
							if (param != null) {
								if (param instanceof String) {
									connection.addRequestProperty(key, (String) param);
								} else {
									connection.addRequestProperty(key, param.toString());
								}
							}
						}
					}
					final String method = request.getRequestMethod();
					connection.setRequestMethod(method);
					if (request.getContentType() != null) {
						connection.setRequestProperty("Content-Type", request.getContentType());
					}
					if (!(method.equals("DELETE") || method.equals("GET")) && request.getBody() != null) {
						connection.setDoOutput(true);
						connection.setDoInput(true);
						connection.setRequestProperty("Content-length", String.valueOf(request.getBody().length));
						final DataOutputStream output = new DataOutputStream(connection.getOutputStream());
						output.write(request.getBody());
					} else {
						connection.connect();
					}
				}
			}
			return connection;
		}
	}

	public void setHeaderParams(Bundle headerParams) {
		this.headerParams = headerParams;
	}

	/**
	 * This is the core task of the downloader. 1. Pop the next download item 2.
	 * Create the appropriate connection 3. Handle the response 4. Loop back
	 * until there aren't any items left
	 */
	@Override
	protected Object doInBackground(Object... params) {
		// Algorithm
		while (!isCancelled()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// Nothing to do
			}
			if (mRequestQueue.isEmpty()) {
				if (!mIsProgressUpdated) {
					// Don't kill the task until the UI has been updated.
					continue;
				} else {
					// Exit
					break;
				}
			} else {
				final Request request = mRequestQueue.get(0);
				if (request.isFirstAttempt()) {
					resetRequestAttemptCount(request);
					publishProgress(PRE_LOAD, request.getType());
					request.setFirstAttempt(false);
				}
				long timeInMs = 0;
				if (mIsInLoggingMode) {
					Log.v("Uber", "**************** Uber REQUEST ****************");
					Log.v("Uber", "Uber METHOD: " + request.getRequestMethod());
					Log.v("Uber", "Uber REQUEST URL: " + request.getUrlAddress().getAddress() + request.getPath());
					Log.v("Uber", "Uber REQUEST BODY: " + request.getBody());
					timeInMs = System.currentTimeMillis();
				}
				try {
					mIsProgressUpdated = false;
					final HttpURLConnection connection = connect(request);
					if (mIsInLoggingMode) {
						final double timing = (double) (System.currentTimeMillis() - timeInMs) / 1000;
						Log.v("Uber", "***************** Uber RESPONSE ******************");
						Log.v("Uber", "Uber REQUEST URL: " + request.getUrlAddress().getAddress() + request.getPath());
						Log.v("Uber", "Uber TIMING: " + timing);
						Log.v("Uber", "Uber REQUEST ATTEMPT: " + request.getAttemptCount());
					}
					if (connection != null) {
						request.setResponseCode(connection.getResponseCode());
						if (mIsInLoggingMode) {
							Log.v("Uber", "Uber RESPONSE CODE: " + request.getResponseCode());
						}
						if (request.getResponseCode() >= 0) {
							InputStream responseStream;
							if (request.getResponseCode() == 200) {
								responseStream = connection.getInputStream();
							} else {
								responseStream = connection.getErrorStream();
							}
							final String contentEnconding = connection.getContentEncoding();
							if (contentEnconding != null && contentEnconding.equalsIgnoreCase("gzip")) {
								responseStream = new GZIPInputStream(responseStream);
							}
							onServerResponse(request, responseStream, connection);
						} else {
							onNetworkError(request);
						}
					} else {
						onNetworkError(request);
					}
				} catch (Exception exception) {
					if (exception instanceof ConnectException) {
						// Server is down.
						rotateAddress(request);
					} else if (exception instanceof UnknownHostException || exception instanceof SocketTimeoutException) {
						// Internet connection is down
						onNetworkError(request, exception);
					} else {
						// Don't know why it's here
						onNetworkError(request, exception);
					}
				}
			}
		}
		return null;
	}

	private void resetRequestAttemptCount(Request request) {
		if (request.getPriority() == DOWNLOADER_RETRY_LOW_PRIORITY) {
			// Setting limit to low priority retry.
			request.setAttemptCount(20);
		} else {
			if (request.getProtocol().equals("https")) {
				request.setAttemptCount(2);
			} else if (request.getProtocol().equals("http")) {
				request.setAttemptCount(1);
			}

			if (request.getPriority() == DOWNLOADER_HIGH_PRIORITY) {
				request.setAttemptCount(request.getAttemptCount() * 3);
			}
		}
	}

	private void onServerResponse(Request request, InputStream responseStream, HttpURLConnection connection) throws ResponseException {
		mIsConnected = true;
		if (request.getUrlAddress().shouldRotateWithCode(request.getResponseCode())) {
			// This error code means we should try another server.
			rotateAddress(request);
		} else {
			// Everything looks good here. The response can still have an error
			// code,
			// but it is handled by the server, so we consider it DONE.
			final Response response = Response.create(request, responseStream, connection);
			mRequestQueue.remove(0);
			publishProgress(DONE, response);
		}
	}

	private void onNetworkError(Request request, Exception exception) {
		request.setAttemptCount(request.getAttemptCount() - 1);
		if (request.getAttemptCount() == 0) {
			mIsConnected = false;
			mRequestQueue.remove(0);
			publishProgress(ERROR, request, exception);
		}
	}

	private void onNetworkError(Request request) {
		onNetworkError(request, null);
	}

	private void rotateAddress(Request request) {
		request.getUrlAddress().rotateAddress();
		request.setRotationCount(request.getRotationCount() - 1);
		if (request.getRotationCount() == 0) {
			// Actually we've already tried all of our servers,
			// so remove this download item and publish a little error.
			publishProgress(ERROR, request, new Exception("Rotate the server as much as we could"));
			mRequestQueue.remove(0);
		} else {
			// We still have some servers to try, so let's use the next
			// one and see if it works any better for our current download item.
			// WTF: We need to send SSH requests twice because of some pipe
			// errors.
			resetRequestAttemptCount(request);

		}
	}

	@Override
	protected void onProgressUpdate(Object... params) {
		if (params.length >= 2) {
			final int retCode = ((Integer) params[0]).intValue();
			if (mDownloadListener != null) {
				if (retCode == PRE_LOAD) {
					mDownloadListener.onPreLoad(((Integer) params[1]).intValue());
				} else if (retCode == ERROR) {
					Request request = (Request) params[1];
					if (request != null) {
						mDownloadListener.onError(request.getType(), request, (Exception) params[2]);
					} else {
						mDownloadListener.onError(-1, null, null);
					}
				} else if (retCode == DONE) {
					final Response response = (Response) params[1];
					if (response != null) {
						mDownloadListener.onLoad(response);
					}
				}
			}
			if (retCode != PRE_LOAD) {
				mIsProgressUpdated = true;
			}
		}
	}

	@Override
	protected void onCancelled() {
		if (mDownloadListener != null) {
			mDownloadListener.onCancel();
		}
	}

	public static String toQueryString(HashMap<String, Object> params) {
		final Object[] keys = params.keySet().toArray();
		String paramsPath = "?";
		final int size = keys.length;
		for (int i = 0; i < size; ++i) {
			final String key = (String) keys[i];
			Object value = params.get(key);
			if (key instanceof String) {
				paramsPath += key + "=" + value;
			} else {
				paramsPath += key + "=" + value.toString();
			}
			if (i < (size - 1)) {
				paramsPath += "&";
			}
		}
		return paramsPath;
	}
}