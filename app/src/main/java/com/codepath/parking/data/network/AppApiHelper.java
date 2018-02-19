package com.codepath.parking.data.network;

import com.codepath.parking.data.network.model.parkinglocations.ParkingsModel;
import com.codepath.parking.data.network.service.IRequestInterface;
import com.codepath.parking.data.network.service.ServiceConnection;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by uchit on 17/02/2018.
 */

public class AppApiHelper implements IApiHelper{

    private IRequestInterface iRequestInterface;

    public AppApiHelper() {
        iRequestInterface = ServiceConnection.getConnection();
    }

    @Override
    public Observable<List<ParkingsModel>> getParkingLocations() {
        return iRequestInterface.getParkingLocations();
    }

    @Override
    public Observable<List<ParkingsModel>> getLocationDetails() {
        return iRequestInterface.getLocationDetails();
    }

    @Override
    public Observable<ParkingsModel> postReserveLocation(int id) {
        return iRequestInterface.postReserveLocation(id);
    }


}
