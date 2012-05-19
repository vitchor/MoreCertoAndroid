package com.br.morecerto.controller.utilities;


public class GeoUtil {

	// Values in meters.
	private static final int EARTH_RADIUS = 6366000;
	public static final int MAX_DISTANCE_BETWEEN_POINTS = 30;

	public static double calculateDistance(ILocation startLocation, ILocation endLocation) {
		final double startLat = toDegree(startLocation.getLatitude());
		final double endLat = toDegree(endLocation.getLatitude());
		final double startLng = toDegree(startLocation.getLongitude());
		final double endLng = toDegree(endLocation.getLongitude());
		final double dLat = Math.toRadians(endLat - startLat);
		final double dLng = Math.toRadians(endLng - startLng);
		final double equationVar1 = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		final double equationVar2 = 2 * Math.atan2(Math.sqrt(equationVar1), Math.sqrt(1 - equationVar1));
		return EARTH_RADIUS * equationVar2;
	}

	public static int toMicroDegree(double degree) {
		return (int) (degree * 1E6);
	}

	public static double toDegree(int microDegree) {
		return ((double) microDegree) / 1E6;
	}
}