package com.service.dullegil.geofencing;

import java.util.HashMap;

import com.google.android.gms.maps.model.LatLng;

public final class Constants {

	private Constants() {
	}

//	public static final String PACKAGE_NAME = "com.google.android.gms.location.sample.geofencing";
	public static final String PACKAGE_NAME = "com.service.dullegil.geofencing";

	public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

	public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

	/**
	 * Used to set an expiration time for a geofence. After this amount of time
	 * Location Services stops tracking the geofence.
	 */
	public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
	/**
	 * For this sample, geofences expire after twelve hours.
	 */
	public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;

	// 내가 설정하는 Geofencing의 반경
	// public static final float GEOFENCE_RADIUS_IN_METERS = 1609; // 1 mile,
	// 1.6 km
	public static final float GEOFENCE_RADIUS_IN_METERS = 80; // 50m

	/**
	 * Map for storing information about airports in the San Francisco bay area.
	 */
	// Hashmap에 저장하는 NFC의 위도,경도의 위치
	public static final HashMap<String, LatLng> BAY_AREA_LANDMARKS = new HashMap<String, LatLng>();

	static {
		// stamp1
		BAY_AREA_LANDMARKS.put("stamp1", new LatLng(37.545753, 126.88189));
		// stamp2
		BAY_AREA_LANDMARKS.put("stamp2", new LatLng(37.546453, 126.880673));
	}

}
