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
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonNode extends DataNode {

	private String mName;
	private Object mValue;

	public JsonNode(String name, Object value) {
		mName = name;
		mValue = value;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public DataNode getNode(String name) {
		DataNode dataNode = null;
		try {
			if (mValue instanceof JSONObject) {
				dataNode = new JsonNode(name, ((JSONObject) mValue).get(name));
			}
		} catch (JSONException e) {
			dataNode = null;
		}
		return dataNode;
	}

	@Override
	public ArrayList<DataNode> getChildren() {
		final ArrayList<DataNode> dataNodes = new ArrayList<DataNode>();
		if (mValue instanceof JSONArray) {
			dataNodes.addAll(getArray());
		} else if (mValue instanceof JSONObject) {
			final JSONObject jsonObject = (JSONObject) mValue;
			final Iterator<?> keys = jsonObject.keys();
			while (keys.hasNext()) {
				final String key = (String) keys.next();
				final DataNode node = getNode(key);
				if (node != null) {
					dataNodes.add(node);
				}
			}
		}
		return dataNodes;
	}

	@Override
	public ArrayList<DataNode> getArray() {
		final ArrayList<DataNode> dataNodes = new ArrayList<DataNode>();
		try {
			if (mValue instanceof JSONArray) {
				final JSONArray jsonArray = (JSONArray) mValue;
				for (int i = 0; i < jsonArray.length(); ++i) {
					dataNodes.add(new JsonNode("", jsonArray.get(i)));
				}
			}
		} catch (JSONException e) {
			// Nothing else to do
		}
		return dataNodes;
	}

	@Override
	public String getString(String defaultString) {
		if (mValue != null) {
			return mValue.toString();
		}
		return defaultString;
	}

	@Override
	public int getInt(int defaultInt) {
		if (mValue instanceof Integer) {
			return ((Integer) mValue).intValue();
		}
		return defaultInt;
	}

	@Override
	public double getDouble(double defaultDouble) {
		if (mValue instanceof Double) {
			return ((Double) mValue).doubleValue();
		} else if (mValue instanceof Integer) {
			return ((Integer) mValue).doubleValue();
		}
		return defaultDouble;
	}

	@Override
	public Long getLong(Long defaultLong) {
		if (mValue instanceof Long) {
			return ((Long) mValue).longValue();
		} else if (mValue instanceof Integer) {
			return ((Integer) mValue).longValue();
		}
		return defaultLong;
	}

}
