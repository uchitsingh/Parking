package com.codepath.parking.locationdetails;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.parking.R;
import com.codepath.parking.data.network.AppDataManager;
import com.codepath.parking.data.network.model.parkinglocations.ParkingsModel;
import com.codepath.parking.ui.base.BaseFragment;
import com.codepath.parking.ui.utils.rx.AppSchedulerProvider;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationDetailsFragement extends BaseFragment implements ILocationDetailsMvpVIew,  GoogleMap.InfoWindowAdapter {
    TextView title;
    TextView subtitle;
    LayoutInflater inflater;
    private int id ;
 //   private GoogleMap mMap;
    private LocationDetailsPresenterImp<LocationDetailsFragement> locationDetailsPresenterImp;
    public LocationDetailsFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parking_locations_maps, null, false);
       // SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
               // .findFragmentById(R.id.map);
      //  mapFragment.getMapAsync(this);

        locationDetailsPresenterImp = new LocationDetailsPresenterImp<>(new AppDataManager(), new AppSchedulerProvider(), new CompositeDisposable());
        locationDetailsPresenterImp.onAttach(this);
       return view;
      //  return inflater.inflate(R.layout.fragment_location_details_fragement, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callService();

    }

    private void callService() {
        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnectedToNetwork) throws Exception {
                        if(isConnectedToNetwork){
                          //  showLoading();
                          //  String id = getArguments().getString("id");
                            locationDetailsPresenterImp.loadLocationDetails(id);
                            Toast.makeText(getContext(), "Conection", Toast.LENGTH_SHORT).show();

                        }else{
                          //  hideLoading();
                            Toast.makeText(getContext(), "No Conection", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onFetchDataProgress() {
        Toast.makeText(getActivity(), "Loading, Please Wait", Toast.LENGTH_SHORT).show();
        Log.i("runningProgress", "running");
        showLoading();
    }

    @Override
    public void onFetchDataSuccess(ParkingsModel parkingsModel) {

    //    int id = Integer.parseInt(getArguments().getString("id"));
        //LatLng tmp_Position = new LatLng(Double.valueOf(parkingsModel.getLat()), Double.valueOf(parkingsModel.getLng()));

  /*      mMap.addMarker(new MarkerOptions()
                        //   .icon(bitmapDescriptorFromVector(getApplicationContext(), icon,
                        ///      Color.parseColor(AqiUtils.getColorHexByDangerIndex(danger))))
                        .position(tmp_Position)
                        .title(String.valueOf(parkingsModel.getName()))
                        .snippet("" +parkingsModel.getIsReserved())
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tmp_Position));

                hideLoading();*/

        title.setText(parkingsModel.getId());
        subtitle.setText(parkingsModel.getCostPerMinute());

    }

    @Override
    public void onFetchDataError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        showMessage(error);
        // refreshLayout.setRefreshing(false);
        hideLoading();
    }



    @Override
    public View getInfoWindow(Marker marker) {
        inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // R.layout.echo_info_window is a layout in my
        // res/layout folder. You can provide your own
        View v = inflater.inflate(R.layout.info_window, null);
        this.id = Integer.parseInt( marker.getId());
        TextView title = (TextView) v.findViewById(R.id.info_window_name);
        TextView subtitle = (TextView) v.findViewById(R.id.info_window_cost);



        //int id = Integer.parseInt(marker.getId());
        //  TextView title = (TextView) v.findViewById(R.id.info_window_title);
        // title.setText();

        //  title.setText();

        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
