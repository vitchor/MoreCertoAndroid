package com.br.morecerto.model;

import java.util.ArrayList;

import android.util.Log;

import com.br.morecerto.controller.network.DataNode;
import com.br.morecerto.controller.network.Response;

public class Realstate {

	public static ArrayList<Realstate> mRealstates;

	public static String KIND_APT = "apt";
	public static String KIND_KIT = "kit";
	public static String KIND_ROOM = "room";
	public static String KIND_HOUSE = "house";

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

	public double price;

	public String agency;
	public String agencyUrl;

	public String imageUrl;

	public String kind;
	public String type;

	public String district;
	public String city;
	public String state;

	public String address;
	
	public static double biggestPrice = 1.0;

	public String id;

	public int getRating() {
		return (int) Math.round((bankRating * UserRankings.bankRating + storeRating * UserRankings.storeRating + barRating * UserRankings.barRating + gasStationRating * UserRankings.gasStationRating + restaurantRating * UserRankings.restaurantRating + restaurantRating * UserRankings.restaurantRating + healthRating * UserRankings.healthRating + getPriceRating() * UserRankings.priceRating) / UserRankings.getSum());
	}

	private double getPriceRating() {
		return 100.0 - (price * 100.0 / biggestPrice);
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

				realstate.price = Double.parseDouble(realstateNode.findString("price", "0"));
				if (biggestPrice < realstate.price) {
					biggestPrice = realstate.price;
				}
				realstate.agency = realstateNode.findString("agency", null);
				realstate.agencyUrl = realstateNode.findString("url", null);

				realstate.kind = realstateNode.findString("kind", null);
				realstate.type = realstateNode.findString("type", null);

				realstate.district = realstateNode.findString("district", null);
				realstate.state = realstateNode.findString("state", null);

				realstate.address = realstateNode.findString("address", null);

				realstate.id = realstateNode.findString("idrealestates", "");

				if (realstateNode.findString("cached_image", "0").equals("0")) {
					if (!(realstate.id.equals(""))) {
						realstate.imageUrl = "http://www.morecerto.com.br/uploads/cache/" + realstate.id + ".jpg";
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
