package com.codepath.parking.parkinglocations;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.codepath.parking.R;
import com.codepath.parking.data.network.AppDataManager;
import com.codepath.parking.data.network.model.parkinglocations.ParkingsModel;
import com.codepath.parking.data.network.service.IRequestInterface;
import com.codepath.parking.data.network.service.ServiceConnection;
import com.codepath.parking.locationdetails.LocationDetailsFragement;
import com.codepath.parking.ui.base.BaseFragment;
import com.codepath.parking.ui.utils.rx.AppSchedulerProvider;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParkingLocationsFragment extends BaseFragment implements IParkingLocationsMvpView, OnMapReadyCallback{

    private GoogleMap mMap;

    private ParkingLocationsPresenterIml<ParkingLocationsFragment> parkingLocationsPresenterIml;
    private View targetView;
  //  private SwipeRefreshLayout refreshLayout;
 //   @BindView(R.id.map)   MapFragment mapFragment;
    public ParkingLocationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_locations_maps, null, false);
       // setupMap();
      //    refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        // Inflate the layout for this fragment
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
      SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        parkingLocationsPresenterIml = new ParkingLocationsPresenterIml<>(new AppDataManager(), new AppSchedulerProvider(), new CompositeDisposable());
        parkingLocationsPresenterIml.onAttach(this);


        return view;
        // return inflater.inflate(R.layout.fragment_parking_locations_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.targetView = view;
        //ButterKnife.bind(view);

        //setupMap();
        callService();

   /*    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               callService();
           }
       });*/


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupMap();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
               mMap.setInfoWindowAdapter(new InfoWindowCustom(getContext(),mMap,targetView));
                return false;
            }
        });


    }

    public void callService() {
        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnectedToInternet) throws Exception {
                        if (isConnectedToInternet) {
                            showLoading();
                            parkingLocationsPresenterIml.loadParkingLocations();
                            Toast.makeText(getContext(), "Conection", Toast.LENGTH_SHORT).show();

                        } else {
                         //   refreshLayout.setRefreshing(false);
                            hideLoading();
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
    public void onFetchDataSuccess(List<ParkingsModel> parkingsModelList) {

        try {
            for (int i = 0; i < parkingsModelList.size(); i++) {
              //  if (!parkingsModelList.get(i).getIsReserved()) { //only get the items thar are not reserved

                    LatLng tmp_Position = new LatLng(Double.valueOf(parkingsModelList.get(i).getLat()), Double.valueOf(parkingsModelList.get(i).getLng()));
                    //mMap.addMarker(new MarkerOptions().position(tmp_Position).title(parkingsModels.get(i).getName()));


                if (!parkingsModelList.get(i).getIsReserved()) {

                    mMap.addMarker(new MarkerOptions()
                                    //   .icon(bitmapDescriptorFromVector(getApplicationContext(), icon,
                                    ///      Color.parseColor(AqiUtils.getColorHexByDangerIndex(danger))))

                                    .position(tmp_Position)
                                    .title(String.valueOf(parkingsModelList.get(i).getId()))
                                    .snippet("" +parkingsModelList.get(i).getIsReserved())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            /*.snippet("#" + parkingsModelList.get(i).getId()
                                          + "\nNot Reserved, Tap To Reserve\n"
                                          + "Reservation Information\n"
                                          + "Minimum Time: " + parkingsModelList.get(i).getMinReserveTimeMins()
                                          + "\nMaximum Time: " + parkingsModelList.get(i).getMaxReserveTimeMins()
                                          + "\nCost: +" +parkingsModelList.get(i).getCostPerMinute()

                            )*/
                    );

                }else{
                    mMap.addMarker(new MarkerOptions()
                            //   .icon(bitmapDescriptorFromVector(getApplicationContext(), icon,
                            ///      Color.parseColor(AqiUtils.getColorHexByDangerIndex(danger))))

                            .position(tmp_Position)
                            .title(String.valueOf(parkingsModelList.get(i).getId()))
                            .snippet("" +parkingsModelList.get(i).getIsReserved())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(tmp_Position));
                    //    refreshLayout.setRefreshing(false);
                //    showLoading();

               // }
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
      //  refreshLayout.setRefreshing(false);
        hideLoading();


    }

    @Override
    public void onFetchDataError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        showMessage(error);
       // refreshLayout.setRefreshing(false);
        hideLoading();
    }
//    @SuppressLint("MissingPermission") // Location permissions checked in MainActivity
    private void setupMap() {
        // Set map visuals
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMaxZoomPreference(28f);
        mMap.setMinZoomPreference(2f);
     //   mMap.setMyLocationEnabled(true);
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setTiltGesturesEnabled(false);
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setMyLocationButtonEnabled(true);

    /*    // Assign MapView as map interaction listener
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
*/

        // Default camera position of London
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(new LatLng(37.7749,122.4194)).build()));


    }

}
