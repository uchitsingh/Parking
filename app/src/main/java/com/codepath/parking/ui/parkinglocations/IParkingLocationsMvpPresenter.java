package com.codepath.parking.ui.parkinglocations;

import com.codepath.parking.ui.base.MvpPresenter;

/**
 * Created by uchit on 17/02/2018.
 */

public interface IParkingLocationsMvpPresenter<V extends IParkingLocationsMvpView> extends MvpPresenter<V> {
    void loadParkingLocations();
    void loadParkingLocations(int id);
    void loadParkingLocations2();

}
