package com.codepath.parking;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.codepath.parking.data.network.AppDataManager;
import com.codepath.parking.data.network.model.parkinglocations.ParkingsModel;
import com.codepath.parking.data.network.service.ApiList;
import com.codepath.parking.ui.parkinglocations.IParkingLocationsMvpView;
import com.codepath.parking.ui.parkinglocations.ParkingLocationsPresenterIml;
import com.codepath.parking.ui.base.BaseFragment;
import com.codepath.parking.ui.utils.rx.AppSchedulerProvider;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParkingLocationsFragment extends BaseFragment implements IParkingLocationsMvpView {

    private GoogleMap mMap;
    @BindView(R.id.map)
    MapView mapView;

    private ParkingLocationsPresenterIml<ParkingLocationsFragment> parkingLocationsPresenterIml;

    //  private SwipeRefreshLayout refreshLayout;
    //   @BindView(R.id.map)   MapFragment mapFragment;
    public ParkingLocationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_locations_maps, container, false);
        //    refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

   /*    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               callService();
           }
       });*/

        parkingLocationsPresenterIml = new ParkingLocationsPresenterIml<>(new AppDataManager(), new AppSchedulerProvider(), new CompositeDisposable());
        parkingLocationsPresenterIml.onAttach(this);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.i("WEEK3ASSIGNMENT", "Map is ready");
                //  parkingLocationsPresenterIml.onViewPrepared();
                callService();
                mMap = googleMap;
                mMap.getUiSettings().setMapToolbarEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ApiList.USER_LOCATION, 20));
            }
        });
        ;
     /*   ImageView compass = (ImageView)mapView.findViewWithTag("GoogleMapCompass");
        compass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                parkingLocationsPresenterIml.loadParkingLocations2();
            }
        });
*/
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

        mMap.setInfoWindowAdapter(new InfoWindowCustom(parkingsModelList));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i("WEEK3ASSIGNMENT", "Clicked on info window for " + parkingsModelList.get(Integer.parseInt(marker.getTitle())).getId());
                //      parkingLocationsPresenterIml.onViewPrepared(parkingsModelList.get(Integer.parseInt(marker.getTitle())).getId());
                parkingLocationsPresenterIml.loadParkingLocations(parkingsModelList.get(Integer.parseInt(marker.getTitle())).getId());


                parkingLocationsPresenterIml.loadParkingLocations();
            }
        });

        mMap.clear();


        for (int i = 0; i < parkingsModelList.size(); i++) {
            // LatLng tmp_Position = new LatLng(Double.valueOf(parkingsModelList.get(i).getLat()), Double.valueOf(parkingsModelList.get(i).getLng()));

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(parkingsModelList.get(i).getLat()), Double.parseDouble(parkingsModelList.get(i).getLng())))
                    .title(String.valueOf(i))
                    .icon(BitmapDescriptorFactory.defaultMarker((parkingsModelList.get(i).getIsReserved()) ? BitmapDescriptorFactory.HUE_RED : BitmapDescriptorFactory.HUE_GREEN)));
        }

        //  refreshLayout.setRefreshing(false);
        hideLoading();
    }

    @Override
    public void onFetchDataSucces(ParkingsModel parkingModel) {
        Log.i("WEEK3ASSIGNMENT", parkingModel.getName() + " (" + parkingModel.getId() + ") successfully reserved");
        Snackbar.make(getView(), "You have reserved " + parkingModel.getName() + " until " + parkingModel.getReservedUntil(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onFetchDataError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        showMessage(error);
        // refreshLayout.setRefreshing(false);
        hideLoading();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

}
