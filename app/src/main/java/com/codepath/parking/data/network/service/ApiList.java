package com.codepath.parking.data.network.service;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by uchit on 16/02/2018.
 */

public class ApiList {
    public static  final String BASE_URL="http://ridecellparking.herokuapp.com/api/v1/";
    public static  final String PARKING_LOCATIONS_LIST = "parkinglocations"; //file stored on the server

    //LocationDetails
    public static final String LOCATION_DETAILS_LIST = "http://ridecellparking.herokuapp.com/api/v1/parkinglocations/{id}";
    public static final String RESERVE_LOCATION = "http://ridecellparking.herokuapp.com/api/v1/parkinglocations/{id}/reserve";

    public static final LatLng USER_LOCATION = new LatLng(37.7749,-122.4194);

}
