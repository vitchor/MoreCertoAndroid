package com.br.morecerto.model;

import java.util.ArrayList;

import com.br.morecerto.controller.network.DataNode;
import com.br.morecerto.controller.network.Response;

public class Realstate {

	public static ArrayList<Realstate> mRealstates;
	
	public static String KIND_APT="apt";
	public static String KIND_KIT="kit";
	public static String KIND_ROOM="room";
	public static String KIND_HOUSE="house";
	
	public static String TYPE_RENT = "rent";
	public static String TYPE_BUY = "buy";
	
	public double bankRating;
	public double storeRating;
	public double barRating;
	public double gasStationRating;
	public double restaurantRating;
	public double marketRating;
	public double healthRating;

	public double lat;
	public double lng;

	public double preco;

	public String agency;
	public String agencyUrl;

	public String imageUrl;

	public String kind;
	public String type;
	
	public String district;
	public String city;
	public String state;
	
	public String address;
	
	public int getRating() {
		return (int) Math.round((bankRating + storeRating + barRating + gasStationRating + restaurantRating + restaurantRating + healthRating) / 7);
	}
	
	public static ArrayList<Realstate> updateWithResponse(Response response) {

		final ArrayList<Realstate> realstates = new ArrayList<Realstate>();

		final DataNode node = response.getDataNode();

		if (node != null) {
			ArrayList<DataNode> realstatesNodes = node.getArray();

			for (DataNode realstateNode : realstatesNodes) {
				
				final Realstate realstate = new Realstate();
				
				realstate.bankRating = Double.parseDouble(realstateNode.findString("bank", "0"));
				realstate.storeRating = Double.parseDouble(realstateNode.findString("store", "0"));
				realstate.barRating = Double.parseDouble(realstateNode.findString("bar", "0"));
				realstate.gasStationRating = Double.parseDouble(realstateNode.findString("gas_station", "0"));
				realstate.restaurantRating = Double.parseDouble(realstateNode.findString("restaurant", "0"));
				realstate.restaurantRating = Double.parseDouble(realstateNode.findString("market", "0"));
				realstate.healthRating = Double.parseDouble(realstateNode.findString("health", "0"));

				realstate.lat = Double.parseDouble(realstateNode.findString("lat", "0"));
				realstate.lng = Double.parseDouble(realstateNode.findString("lng", "0"));

				realstate.preco = Double.parseDouble(realstateNode.findString("preco", "0"));

				realstate.agency = realstateNode.findString("agency", null);
				realstate.agencyUrl = realstateNode.findString("agencyUrl", null);
				
				realstate.kind = realstateNode.findString("kind", null);
				realstate.type = realstateNode.findString("type", null);
				
				realstate.district = realstateNode.findString("district", null);
				realstate.state = realstateNode.findString("state", null);
				
				realstate.address = realstateNode.findString("state", null);

				if (realstateNode.findString("cached_image", "0").equals("0")) {
					final String idrealestates = realstateNode.findString("idrealestates", "");
					if (!(idrealestates.equals(""))) {
						realstate.imageUrl = "http://www.morecerto.com.br/uploads/cache/" + idrealestates + ".jpg";
					}

				} else {
					realstate.imageUrl = realstateNode.findString("thumb", "");
				}
				
				realstates.add(realstate);

			}
		}

		mRealstates = realstates;
		
		return realstates;
	}
}
