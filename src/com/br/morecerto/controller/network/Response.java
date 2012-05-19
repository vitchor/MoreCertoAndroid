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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import android.graphics.Bitmap;

public class Response {

	public final static int XML_TYPE = 600;
	public final static int JSON_TYPE = 601;
	public final static int IMAGE_TYPE = 602;
	public final static int NO_TYPE = 603;

	private String mStringData;
	private long mLastModified;
	private int mResponseCode = -1;
	private Request mRequest;

	public static Response create(Request request, InputStream data, HttpURLConnection connection) throws ResponseException {
		Response response = null;
		data.mark(100000);
		if (data != null) {
			String stringData = null;
			int responseType = request.getResponseType();
			if (responseType == XML_TYPE) {
				try {
					response = new XmlResponse(data);
				} catch (ResponseException e) {
					throw new ResponseException("Could not parse Xml.");
				}
			} else if (responseType == JSON_TYPE) {
				try {
					stringData = streamToString(data);
					response = new JsonResponse(stringData);
				} catch (IOException e) {
					throw new ResponseException("Could not convert stream to string for JSON response.");
				}
			} else if (responseType == IMAGE_TYPE) {
				response = new ImageResponse(connection, request);
			} else {
				response = new Response();
			}
			if (response != null) {
				if (stringData == null) {
					stringData = streamToString(data);
				}
				response.setRequest(request);
				response.setStringData(stringData);
				response.setLastModified(connection.getLastModified());
				response.setResponseCode(request.getResponseCode());
			}
		}
		return response;
	}

	private void setRequest(Request request) {
		mRequest = request;
	}

	public Request getRequest() {
		return mRequest;
	}

	private void setStringData(String stringData) {
		mStringData = stringData;
	}

	private void setLastModified(long lastModified) {
		mLastModified = lastModified;
	}

	public String getStringData() {
		return mStringData;
	}

	public long getLastModified() {
		return mLastModified;
	}

	public DataNode getDataNode() {
		return null;
	}

	public Bitmap getBitmap() {
		return null;
	}

	public static String streamToString(InputStream stream) {
		final char[] buffer = new char[0x10000];
		final StringBuilder stringBuilder = new StringBuilder();
		try {
			if (stream != null && stream.markSupported() && stream.available() == 0) {
				stream.reset();
			}
			final InputStreamReader isr = new InputStreamReader(stream, "UTF-8");
			int read;
			while ((read = isr.read(buffer, 0, buffer.length)) != -1) {
				stringBuilder.append(buffer, 0, read);
			}
		} catch (IOException e) {
			//Nothing to do here
		}
		return stringBuilder.toString();
	}

	public void setResponseCode(int responseCode) {
		mResponseCode = responseCode;
	}

	public int getResponseCode() {
		return mResponseCode;
	}

}
