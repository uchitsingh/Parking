package com.codepath.parking.data.network;

import com.codepath.parking.data.network.model.parkinglocations.ParkingsModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by uchit on 17/02/2018.
 */

public class AppDataManager implements IDataManager {

    private IApiHelper iApiHelper;

    public AppDataManager() {
    iApiHelper = new AppApiHelper();
    }


    @Override
    public Observable<List<ParkingsModel>> getParkingLocations() {
        return iApiHelper.getParkingLocations();
    }

    @Override
    public Observable<ParkingsModel> getLocationDetails(int id) {
        return iApiHelper.getLocationDetails(id);
    }


}
