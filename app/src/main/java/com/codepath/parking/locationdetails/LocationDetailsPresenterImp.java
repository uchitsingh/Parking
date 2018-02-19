package com.codepath.parking.locationdetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.codepath.parking.R;
import com.codepath.parking.data.network.IDataManager;
import com.codepath.parking.data.network.model.parkinglocations.ParkingsModel;
import com.codepath.parking.ui.base.BasePresenter;
import com.codepath.parking.ui.utils.rx.SchedulerProvider;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by uchit on 18/02/2018.
 */

public class LocationDetailsPresenterImp <V extends ILocationDetailsMvpVIew> extends BasePresenter<V> implements ILocationDetailsMvpPresenter<V> {

    public LocationDetailsPresenterImp(IDataManager IDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(IDataManager, schedulerProvider, compositeDisposable);

    }

    @Override
    public void loadLocationDetails(int id ) {
        LayoutInflater inflater;
        getCompositeDisposable().add(getIDataManager().getLocationDetails(id)
        .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<ParkingsModel>() {
                    @Override
                    public void accept(ParkingsModel parkingsModel) throws Exception {
                        getMvpView().onFetchDataSuccess(parkingsModel);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getMvpView().onFetchDataError(throwable.getMessage());
                    }
                }));
      //  getMvpView().onFetchDataProgress();


    }

}
