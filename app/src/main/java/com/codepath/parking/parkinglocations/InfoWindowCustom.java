package com.codepath.parking.parkinglocations;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.parking.R;
import com.codepath.parking.data.network.model.parkinglocations.ParkingsModel;
import com.codepath.parking.data.network.service.IRequestInterface;
import com.codepath.parking.data.network.service.ServiceConnection;
import com.codepath.parking.locationdetails.ILocationDetailsMvpVIew;
import com.codepath.parking.ui.base.BaseFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by uchit on 18/02/2018.
 */

public class InfoWindowCustom implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    private Context context;
    private LayoutInflater inflater;
    private IRequestInterface iRequestInterface;
    private GoogleMap gmap;
    private TextView isReserveed;
    private int id ;

  /*  @BindView(R.id.info_window_name) TextView title;
    @BindView(R.id.info_window_cost) TextView cost;
    @BindView(R.id.info_window_min_time) TextView min_Time;
    @BindView(R.id.info_window_max_time) TextView max_Time;
    @BindView(R.id.info_window_isReserved) TextView isReserveed;
*/


    public InfoWindowCustom(Context context, GoogleMap gMap, View view) {
        this.context = context;
      //  this.iRequestInterface = iRequestInterface;
       this.iRequestInterface = ServiceConnection.getConnection();
       this.gmap = gmap;
       gMap.setOnInfoWindowClickListener(this);
       ButterKnife.bind(view);


    }

    @Override
    public View getInfoContents(Marker marker) {

/*
        inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.info_window, null);*/
        return  null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.info_window, null);
        TextView title = (TextView) v.findViewById(R.id.info_window_name);
        TextView cost = (TextView) v.findViewById(R.id.info_window_cost);
        TextView min_Time = (TextView) v.findViewById(R.id.info_window_min_time);
        TextView max_Time = (TextView) v.findViewById(R.id.info_window_max_time);

        isReserveed = (TextView) v.findViewById(R.id.info_window_isReserved);

        try {
            title.setText(marker.getTitle()); //marker configured to getId with marker getTitle method
            isReserveed.setText(marker.getSnippet()); //ocnfigured to get if the parking is booked

           this.id = Integer.parseInt(marker.getTitle());
            iRequestInterface.getLocationDetails(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ParkingsModel>() {
                        @Override
                        public void accept(ParkingsModel parkingsModel) throws Exception {
                            cost.setText(parkingsModel.getCostPerMinute());
                            min_Time.setText(parkingsModel.getMinReserveTimeMins());
                            max_Time.setText(parkingsModel.getMaxReserveTimeMins());


                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return v;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

        iRequestInterface.postReserveLocation(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ParkingsModel>() {
                    @Override
                    public void accept(ParkingsModel parkingsModel) throws Exception {
                    if(!parkingsModel.getIsReserved()){
                        Toast.makeText(context, "It has been Booked. ", Toast.LENGTH_SHORT).show();
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        isReserveed.setText("true");
                        parkingsModel.setIsReserved(true);

                 //
                     //   isReserveed.setText("true");
                      //  marker.setSnippet("false");

                    }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });


    }
}