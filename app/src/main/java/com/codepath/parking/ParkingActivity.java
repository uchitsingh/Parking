package com.codepath.parking;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.parking.locationdetails.LocationDetailsFragement;
import com.codepath.parking.parkinglocations.ParkingLocationsFragment;

public class ParkingActivity extends AppCompatActivity {
    private static FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        fragmentManager = getSupportFragmentManager();
        if(savedInstanceState == null){
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, new ParkingLocationsFragment())
                    .commit();
        }

    }

    public static void displayLocationDetails(String id) {
        LocationDetailsFragement locationDetailsFragement = new LocationDetailsFragement();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        locationDetailsFragement.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, locationDetailsFragement)
                .addToBackStack(null)
                .commit();

    }
}
