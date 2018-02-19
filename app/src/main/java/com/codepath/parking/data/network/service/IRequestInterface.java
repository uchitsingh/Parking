package com.codepath.parking.data.network.service;


import com.codepath.parking.data.network.model.parkinglocations.ParkingsModel;
import com.google.android.gms.common.api.Api;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by uchit on 16/02/2018.
 */

public interface IRequestInterface {

    @GET(ApiList.PARKING_LOCATIONS_LIST)
    Observable<List<ParkingsModel>> getParkingLocations();

    @GET(ApiList.LOCATION_DETAILS_LIST)
    Observable<ParkingsModel> getLocationDetails(@Path("id") int id);

    @POST(ApiList.RESERVE_LOCATION)
    Observable<ParkingsModel>  postReserveLocation(@Path("id") int id);

}
