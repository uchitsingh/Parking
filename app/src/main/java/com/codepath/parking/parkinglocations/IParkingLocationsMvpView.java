package com.codepath.parking.parkinglocations;

import com.codepath.parking.data.network.model.parkinglocations.ParkingsModel;
import com.codepath.parking.ui.base.MvpView;

import java.util.List;

/**
 * Created by uchit on 17/02/2018.
 */

public interface IParkingLocationsMvpView extends MvpView {
    void onFetchDataProgress();
    void onFetchDataSuccess(List<ParkingsModel> parkingsModelList);
    void onFetchDataError(String error);


}
