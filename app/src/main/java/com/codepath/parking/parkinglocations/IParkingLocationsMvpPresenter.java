package com.codepath.parking.parkinglocations;

import com.codepath.parking.ui.base.MvpPresenter;

/**
 * Created by uchit on 17/02/2018.
 */

public interface IParkingLocationsMvpPresenter<V extends IParkingLocationsMvpView> extends MvpPresenter<V> {
    void loadParkingLocations();

}
