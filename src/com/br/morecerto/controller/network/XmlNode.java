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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.br.morecerto.controller.utilities.NumberUtils;

public class XmlNode extends DataNode {
	
	private Node mNode;
	
	public XmlNode(Node node) {
		mNode = node;
	}

	@Override
	public String getName() {
		return mNode.getNodeName();
	}

	@Override
	public DataNode getNode(String name) {
		final ArrayList<DataNode> dataNodes = getArray();
		for (DataNode dataNode : dataNodes) {
			if (dataNode.getName().equals(name)) {
				return dataNode;
			}
		}
		return null;
	}
	
	@Override
	public ArrayList<DataNode> getChildren() {
		return getArray();
	}

	@Override
	public ArrayList<DataNode> getArray() {
		final ArrayList<DataNode> dataNodes = new ArrayList<DataNode>();
		final NodeList nodes = mNode.getChildNodes();
		for (int i = 0; i < nodes.getLength(); ++i) {
			dataNodes.add(new XmlNode(nodes.item(i)));
		}
		return dataNodes;
	}

	@Override
	public String getString(String defaultString) {
		if (mNode.hasChildNodes()) {
			return mNode.getFirstChild().getNodeValue();
		}
		return defaultString;
	}

	@Override
	public int getInt(int defaultInt) {
		if (mNode.hasChildNodes()) {
			final Integer intValue = NumberUtils.parseInteger(mNode.getFirstChild().getNodeValue());
			if (intValue != null) {
				return intValue;
			}
		}
		return defaultInt;
	}

	@Override
	public double getDouble(double defaultDouble) {
		if (mNode.hasChildNodes()) {
			final Double doubleValue = NumberUtils.parseDouble(mNode.getFirstChild().getNodeValue());
			if (doubleValue != null) {
				return doubleValue;
			}
		}
		return defaultDouble;
	}
	
	@Override
	public Long getLong(Long defaultLong) {
		if (mNode.hasChildNodes()) {
			final Long longValue = NumberUtils.parseLong(mNode.getFirstChild().getNodeValue());
			if (longValue != null) {
				return longValue;
			}
		}
		return defaultLong;
	}
	
	@Override
	public String getAttribute(String attributeName, String defaultValue) {
		if (mNode.hasAttributes()) {
			final Node attributeNode = mNode.getAttributes().getNamedItem(attributeName);
			if (attributeNode != null) {
				return attributeNode.getNodeValue();
			}
		}
		return defaultValue;
	}
	
}
