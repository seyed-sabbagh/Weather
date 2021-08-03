/*
 * *
 *  * Created by Seyed on 8/3/21, 6:23 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 8/3/21, 6:03 PM
 *
 */

package com.seyed.weather.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.seyed.weather.R;
import com.seyed.weather.Services.UserLocationService;
import com.seyed.weather.databinding.ActivitySetupBinding;
import static com.seyed.weather.Services.UserPreferencesService.PREF_FILE;

import static android.location.LocationManager.*;

public class SetupActivity extends AppCompatActivity implements UserLocationService.UserLocationApiCallback {

    ActivitySetupBinding binding;
   // LocationManager locationManager;
    //String latitude, longitude;
    //private static final int REQUEST_LOCATION = 1;

    public void requestLocation(Context context)
    {
        //GPS Stuff
        UserLocationService.getUserLocation(context, this);
        UserLocationService.setCallback(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupBinding.inflate(getLayoutInflater());
        final View view = binding.getRoot();
        setContentView(view);



        binding.clPageB.setVisibility(View.VISIBLE);
        binding.clPageB.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        binding.clPageC.setVisibility(View.GONE);
        binding.clPageD.setVisibility(View.GONE);
        binding.clPageA.setVisibility(View.GONE);
        binding.clPrivacy.setVisibility(View.GONE);



        binding.btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Do GPS stuff
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(GPS_PROVIDER)) {
                    requestLocation(getApplicationContext());
                } else {
                    Toast.makeText(SetupActivity.this, "No GPS or network signal please fill the location manually!", Toast.LENGTH_SHORT).show();
                    //OnGPS();
                }

//                binding.clPrivacy.setVisibility(View.VISIBLE);
//                binding.btnFine.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        binding.clPrivacy.setVisibility(View.GONE);
//
//                    }
//                });
            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = String.valueOf(binding.etCity.getText());

                if(binding.etCity.getText().length() == 0) {
                    Toast.makeText(SetupActivity.this, "Maybe write city first?", Toast.LENGTH_SHORT).show();
                } else {
                    getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("CITY", city).apply();
                    binding.clPageB.setVisibility(View.GONE);
                    binding.clPageC.setVisibility(View.VISIBLE);
                    // animateI();
                    binding.clPageC.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right));
                }

            }
        });

        binding.btnMetric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("UNITS", "metric").apply();
                binding.clPageC.setVisibility(View.GONE);
                binding.clPageD.setVisibility(View.VISIBLE);
                //animateII();
                binding.clPageD.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right));

            }
        });

        binding.btnImperial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("UNITS", "imperial").apply();
                binding.clPageC.setVisibility(View.GONE);
                binding.clPageD.setVisibility(View.VISIBLE);
                //animateII();
                binding.clPageD.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right));

            }
        });

        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openMain();
                finish();
            }
        });


    }

    public void openMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void displayLocation(String cityName) {
        getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("CITY", cityName).apply();
        binding.clPageB.setVisibility(View.GONE);
        binding.clPageC.setVisibility(View.VISIBLE);
        //animateI();
        binding.clPageC.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right));
    }




    /////////
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        } else {
//            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (locationGPS != null) {
//                double lat = locationGPS.getLatitude();
//                double longi = locationGPS.getLongitude();
//                latitude = String.valueOf(lat);
//                longitude = String.valueOf(longi);
//
//                // Decoding lat\lon coordinates to a city name
//                Geocoder gcd = new Geocoder(this, Locale.getDefault());
//                try {
//                    List<Address> addresses = gcd.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
//                    String cityName = addresses.get(0).getLocality();
//                    //callback.displayLocation(cityName);
//                    Toast.makeText(this, "Ur loc is " + cityName, Toast.LENGTH_SHORT).show();
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}