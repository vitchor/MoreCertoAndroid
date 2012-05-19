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

public interface OnDownloadListener {
	
	/**
	 * Called before starting the download. You might want to show a loading dialog 
	 * here for example.
	 * @param type the type of the request
	 */
	public void onPreLoad(int type);
	
	/**
	 * Called when the download succesfully finished. You can browse a structured 
	 * response (eg. JSON or XML) using response.getDataNode(), get a raw string using
	 * response.getDataString(), or get an image using response.getBitmap().
	 * @param response the response object
	 */
	public void onLoad(Response response);
	
	/**
	 * Called if the download could not connect to the server, got an error code from
	 * the server, or could not parse the response.
	 * @param type the type of the request
	 */
	public void onError(int type, Request request, Exception exception);
	
	/**
	 * Called if the download was cancelled.
	 */
	public void onCancel();
	
}
