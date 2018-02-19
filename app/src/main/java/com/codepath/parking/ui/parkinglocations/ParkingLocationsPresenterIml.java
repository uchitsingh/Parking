package com.codepath.parking.ui.parkinglocations;

import com.codepath.parking.data.network.IDataManager;
import com.codepath.parking.data.network.model.parkinglocations.ParkingsModel;
import com.codepath.parking.ui.base.BasePresenter;
import com.codepath.parking.ui.utils.rx.SchedulerProvider;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by uchit on 17/02/2018.
 * 1.extends , initialize constructor
 * 2.implemet , override method
 */

public class ParkingLocationsPresenterIml <V extends IParkingLocationsMvpView> extends BasePresenter<V> implements IParkingLocationsMvpPresenter<V> {

    public ParkingLocationsPresenterIml(IDataManager IDataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(IDataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void loadParkingLocations() {
        getCompositeDisposable().add(getIDataManager().getParkingLocations()
          .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<List<ParkingsModel>>() {
                    @Override
                    public void accept(List<ParkingsModel> parkingsModelsList) throws Exception {

                        getMvpView().onFetchDataSuccess(parkingsModelsList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getMvpView().onFetchDataError(throwable.getMessage());
                    }
                }));

        getMvpView().onFetchDataProgress();



    }

    @Override
    public void loadParkingLocations(int id) {
        getCompositeDisposable().add(getIDataManager().postReserveLocation(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ParkingsModel>() {
                    @Override
                    public void accept(ParkingsModel parkingsModel) throws Exception {
                        getMvpView().onFetchDataSucces(parkingsModel);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getMvpView().onFetchDataError(throwable.getMessage());
                    }
                })
        );
        getMvpView().onFetchDataProgress();

    }

    @Override
    public void loadParkingLocations2() {
   //     getCompositeDisposable().add(getIDataManager().ge)
        getCompositeDisposable().add(getIDataManager().getLocationDetails()
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ParkingsModel>>() {
                    @Override
                    public void accept(List<ParkingsModel> parkingsModelList) throws Exception {
                    getMvpView().onFetchDataSuccess(parkingsModelList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getMvpView().onFetchDataError(throwable.getMessage());
                    }
                })
        );
        getMvpView().onFetchDataProgress();
    }


}
