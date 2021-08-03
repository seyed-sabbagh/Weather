/*
 * *
 *  * Created by Seyed on 8/3/21, 6:23 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 8/3/21, 6:03 PM
 *
 */

package com.seyed.weather.Services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Build;
import android.widget.Toast;
import com.seyed.weather.Activities.SetupActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class UserLocationService extends Activity {

    private static UserLocationApiCallback callback;

    // User's location:
    private static Double lat;
    private static Double lon;
    private static String cityName;


    /*
    TODO: 1 - clean everything
          2 - it cannot determine location if before no apps have received data
          However here(SetupActivity) is a block for asking to turn on the location it won't help so i disabled it for not
     */

    public static void setCallback(UserLocationApiCallback notThisCallback) {
        callback = notThisCallback;
    }

    /**
     * The following function is getting User's location. (lon, lat)
     *
     * Algorithm:
     *      - If we have permission -> get location
     *      - If we do not have permission -> ask for it -> get location

     */
    public static void getUserLocation(final Context context, SetupActivity setupActivity) {
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            /** If we have permission -> get location */
            if (context.getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                // get the location
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                if (location != null) {
                                    lat = location.getLatitude();
                                    lon = location.getLongitude();

                                    // Decoding lat\lon coordinates to a city name
                                    Geocoder gcd = new Geocoder(context, Locale.getDefault());
                                    try {
                                        List<Address> addresses = gcd.getFromLocation(lat, lon, 1);
                                        cityName = addresses.get(0).getLocality();
                                        callback.displayLocation(cityName);


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, String.valueOf(e.getMessage()), Toast.LENGTH_LONG).show();
                                    }


                                } else {
                                    Toast.makeText(context, "No GPS or network signal please fill the location manually!", Toast.LENGTH_LONG).show();

                                }

                            }
                        });
            }
            else /** If we do not have permission -> ask for it -> get location */
            {
                setupActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                // get the location
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                if(location != null)
                                {
                                    lat = location.getLatitude();
                                    lon = location.getLongitude();

                                    // Decoding lat\lon coordinates to a city name
                                    Geocoder gcd = new Geocoder(context, Locale.getDefault());
                                    try {
                                        List<Address> addresses = gcd.getFromLocation(lat, lon, 1);
                                        cityName = addresses.get(0).getLocality();
                                        callback.displayLocation(cityName);


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, String.valueOf(e.getMessage()), Toast.LENGTH_LONG).show();
                                    }

                                }

                            }
                        });
            }
        }
    }


    public interface UserLocationApiCallback {
        void displayLocation(String cityName);
    }


}
