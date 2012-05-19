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
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class ImageResponse extends Response {

	private Bitmap mBitmap;

	private static int MAX_HEIGHT = 100;
	private static int MAX_WIDTH = 100;

	public ImageResponse(HttpURLConnection connection, Request request) throws ResponseException {
		try {

			final Options options = getBitmapFactoryOptions(connection, request);

			mBitmap = createBitmap(options, request);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Options getBitmapFactoryOptions(HttpURLConnection connection, Request request) throws IOException, ResponseException {

		// Create options to download only the meta data of the image.
		final Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		// Download meta data.
		InputStream is = connection.getInputStream();

		// Make sure the picture size information is valid 
		if (options.outHeight * options.outWidth >= MAX_HEIGHT * MAX_WIDTH * 2) {

			// Find out if we should resize it based on width or height
			final Boolean scaleByHeight = Math.abs(options.outHeight - MAX_HEIGHT) >= Math.abs(options.outWidth - MAX_WIDTH);

			// Calculate the factor of resizing
			final double sampleSize = scaleByHeight ? options.outHeight / MAX_HEIGHT : options.outWidth / MAX_WIDTH;

			// Set sample size on options
			options.inSampleSize = (int) Math.pow(2d, Math.floor(Math.log(sampleSize) / Math.log(2d)));
		}

		// Set options to download all image.
		options.inJustDecodeBounds = false;

		// Input stream is consumed so it's closed an connection set to null.
		is.close();
		connection = null;

		if (options.outWidth == -1 || options.outHeight == -1) {
			throw new ResponseException("Bitmap meta data has invalid output width and height");
		}

		return options;
	}

	private Bitmap createBitmap(BitmapFactory.Options options, Request request) throws IOException, ResponseException {

		HttpURLConnection connection = null;

		final UrlAddress urlAddress = request.getUrlAddress();

		if (urlAddress != null) {

			final URL url = new URL(urlAddress.getAddress() + request.getPath());

			final String protocol = url.getProtocol();
			if (protocol.equals("http")) {

				connection = (HttpURLConnection) url.openConnection();
			} else if (protocol.equals("https")) {

				try {
					final SSLContext sslContext = SSLContext.getInstance("TLS");
					sslContext.init(null, new TrustManager[] { new IdearTrustManager() }, new java.security.SecureRandom());
					HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
				} catch (Exception e) {
					e.printStackTrace();
				}

				final HttpsURLConnection sslConnection = (HttpsURLConnection) url.openConnection();
				sslConnection.setHostnameVerifier(new IdearHostnameVerifier());

				connection = sslConnection;
			}
			if (connection != null) {

				if (request.getContentType() != null) {
					connection.setRequestProperty("Content-Type", request.getContentType());
				}

				connection.connect();
			}
		}

		InputStream is = connection.getInputStream();

		is = connection.getInputStream();
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);

		is.close();

		if (bitmap == null) {
			throw new ResponseException("Couldn't create image decoding InputStream");
		}

		return bitmap;
	}

	@Override
	public Bitmap getBitmap() {
		return mBitmap;
	}

}
