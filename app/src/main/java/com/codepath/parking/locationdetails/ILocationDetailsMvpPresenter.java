package com.codepath.parking.locationdetails;

import com.codepath.parking.parkinglocations.IParkingLocationsMvpView;
import com.codepath.parking.ui.base.MvpPresenter;

/**
 * Created by uchit on 18/02/2018.
 */

public interface ILocationDetailsMvpPresenter<V extends ILocationDetailsMvpVIew> extends MvpPresenter<V> {
    void loadLocationDetails(int id );

}
