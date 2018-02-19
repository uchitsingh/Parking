package com.codepath.parking.locationdetails;

import com.codepath.parking.data.network.model.parkinglocations.ParkingsModel;
import com.codepath.parking.ui.base.MvpView;

import java.util.List;

/**
 * Created by uchit on 18/02/2018.
 */

public interface ILocationDetailsMvpVIew extends MvpView {
    void onFetchDataProgress();
    void onFetchDataSuccess(ParkingsModel parkingsModel);
    void onFetchDataError(String error);
}
