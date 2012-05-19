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

import java.util.ArrayList;

public class UrlAddress {
	
	private ArrayList<String> mAddresses = new ArrayList<String>();
	public ArrayList<Integer> mRotationCodes = new ArrayList<Integer>();
	
	
	public UrlAddress() {
	}
	
	public UrlAddress(String server) {
		addAddress(server);
	}
	
	public UrlAddress(String host, int port) {
		addAddress(host, port);
	}
	
	public void addAddresses(ArrayList<String> servers) {
		mAddresses.addAll(servers);
	}
	
	public void addAddress(String server) {
		mAddresses.add(server);
	}
	
	public void addAddress(String host, int port) {
		mAddresses.add("http://" + host + ":" + port);
	}
	
	public int size() {
		return mAddresses.size();
	}
	
	public void clear() {
		mAddresses.clear();
	}
	
	public String getAddress(int index) {
		if (mAddresses.size() > 0) {
			return mAddresses.get(index);
		}
		return "";
	}
	
	public String getAddress() {
		return getAddress(0);
	}
	
	public void rotateAddress() {
		if (mAddresses.size() > 0) {
			mAddresses.add(mAddresses.remove(0));
		}
	}

	public void setRotationCodes(ArrayList<Integer> rotationCodes) {
		mRotationCodes = rotationCodes;
	}

	public boolean shouldRotateWithCode(int responseCode) {
		if(mRotationCodes != null){
			return mRotationCodes.contains(responseCode);
		}
		else{
			return false;
		}
	}
	
}
