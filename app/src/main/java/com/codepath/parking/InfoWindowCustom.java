package com.codepath.parking;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.parking.MyApp;
import com.codepath.parking.R;
import com.codepath.parking.data.network.model.parkinglocations.ParkingsModel;
import com.codepath.parking.data.network.service.IRequestInterface;
import com.codepath.parking.data.network.service.ServiceConnection;

import com.codepath.parking.ui.base.BaseFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by uchit on 18/02/2018.
 */

public class InfoWindowCustom implements GoogleMap.InfoWindowAdapter {

    private final View customContentsView;
    private final List<ParkingsModel> parkingSpotModel;

   /*    @BindView(R.id.info_window_cost) TextView cost;
 @BindView(R.id.info_window_name) TextView title;
    @BindView(R.id.info_window_min_time) TextView min_Time;
    @BindView(R.id.info_window_max_time) TextView max_Time;
    @BindView(R.id.info_window_isReserved) TextView isReserveed;
*/


    public InfoWindowCustom(List<ParkingsModel> parkingSpotModel) {

        customContentsView = LayoutInflater.from(MyApp.getInstance().getAppContext()).inflate(R.layout.info_window, null);
        this.parkingSpotModel = parkingSpotModel;
        //  ButterKnife.bind(customContentsView);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        TextView title = (TextView) customContentsView.findViewById(R.id.info_window_name);
        TextView cost = (TextView) customContentsView.findViewById(R.id.info_window_cost);
        TextView min_Time = (TextView) customContentsView.findViewById(R.id.info_window_min_time);
        TextView max_Time = (TextView) customContentsView.findViewById(R.id.info_window_max_time);
        TextView isReserveed = (TextView) customContentsView.findViewById(R.id.info_window_isReserved);

        title.setText(parkingSpotModel.get(Integer.parseInt(marker.getTitle())).getName() + " (" + parkingSpotModel.get(Integer.parseInt(marker.getTitle())).getId() + ")");
        if (parkingSpotModel.get(Integer.parseInt(marker.getTitle())).getIsReserved()) {
            isReserveed.setText("Reserved Until " + parkingSpotModel.get(Integer.parseInt(marker.getTitle())).getReservedUntil());
        } else {
            isReserveed.setText("Not Reserved, Tap to Reserve");
        }
        min_Time.setText(parkingSpotModel.get(Integer.parseInt(marker.getTitle())).getMinReserveTimeMins() + " minutes");
        max_Time.setText(parkingSpotModel.get(Integer.parseInt(marker.getTitle())).getMaxReserveTimeMins() + " minutes");
        cost.setText("$" + parkingSpotModel.get(Integer.parseInt(marker.getTitle())).getCostPerMinute() + " per minute");

        return customContentsView;
    }

}