package com.br.morecerto.model;

public class UserRankings {

	public static int bankRating = 50;
	public static int storeRating = 50;
	public static int barRating = 50;
	public static int gasStationRating = 50;
	public static int restaurantRating = 50;
	public static int marketRating = 50;
	public static int healthRating = 50;
	public static int priceRating = 50;

	public static int getPriceRating() {
		return priceRating;
	}

	public static int getSum() {
		return bankRating + storeRating + barRating + gasStationRating + restaurantRating + marketRating + healthRating + priceRating;
	}

	public static void clearRanking() {
		bankRating = 50;
		storeRating = 50;
		barRating = 50;
		gasStationRating = 50;
		restaurantRating = 50;
		marketRating = 50;
		healthRating = 50;
		priceRating = 50;
	}

	public static void setPriceRating(Integer priceRating) {
		UserRankings.priceRating = priceRating;
	}

	public int getBankRating() {
		return bankRating;
	}

	public void setBankRating(Integer bankRating) {
		UserRankings.bankRating = bankRating;
	}

	public int getStoreRating() {
		return storeRating;
	}

	public void setStoreRating(Integer storeRating) {
		UserRankings.storeRating = storeRating;
	}

	public int getBarRating() {
		return barRating;
	}

	public void setBarRating(Integer barRating) {
		UserRankings.barRating = barRating;
	}

	public int getGasStationRating() {
		return gasStationRating;
	}

	public void setGasStationRating(Integer gasStationRating) {
		UserRankings.gasStationRating = gasStationRating;
	}

	public int getRestaurantRating() {
		return restaurantRating;
	}

	public void setRestaurantRating(Integer restaurantRating) {
		UserRankings.restaurantRating = restaurantRating;
	}

	public int getMarketRating() {
		return marketRating;
	}

	public void setMarketRating(Integer marketRating) {
		UserRankings.marketRating = marketRating;
	}

	public int getHealthRating() {
		return healthRating;
	}

	public void setHealthRating(Integer healthRating) {
		UserRankings.healthRating = healthRating;
	}

}
