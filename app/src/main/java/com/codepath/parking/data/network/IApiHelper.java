package com.codepath.parking.data.network;

import com.codepath.parking.data.network.model.parkinglocations.ParkingsModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Path;

/**
 * Created by uchit on 17/02/2018.
 */

public interface IApiHelper {

    Observable<List<ParkingsModel>> getParkingLocations();
    Observable<ParkingsModel> getLocationDetails(@Path("id") int id);


}
