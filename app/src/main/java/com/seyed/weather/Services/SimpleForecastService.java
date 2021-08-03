/*
 * *
 *  * Created by Seyed on 8/3/21, 6:23 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 8/3/21, 6:03 PM
 *
 */

package com.seyed.weather.Services;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SimpleForecastService {


    public static apiCallback callback;
    public static String temp, desc, min_max, theme_tomorrow, temp_tomorrow, temp_today, wind, main;

    public static void setCallback(apiCallback newCallback) {
        callback = newCallback;
    }

    public static void getSimpleForecaast(final Context context, final String city, String units) {

        String lat = null, lon = null;

        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            address = coder.getFromLocationName(city, 5);
            if (address == null) {

            }
            Address location = address.get(0);
            lat = String.valueOf(location.getLatitude());
            lon = String.valueOf(location.getLongitude());


        } catch (Exception ex) {

            Toast.makeText(context, "Converting lat\\lon, wait..", Toast.LENGTH_SHORT);
            ex.printStackTrace();
        }


        String URL = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&exclude=minutely&appid=ace729200f31ff6473436ef39ad854ea&units=" + units + "&lang=en";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    temp = String.valueOf(response.getJSONObject("current").getInt("temp") + "°");
                    desc = String.valueOf(response.getJSONArray("daily").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("description"));
                    min_max = String.valueOf("Low " + response.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getInt("min") + "°, High " + response.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getInt("max")+"°");

                    //check if the next day rain is expected
                    theme_tomorrow = response.getJSONArray("daily").getJSONObject(1).getJSONArray("weather").getJSONObject(0).getString("main");

                    temp_today = String.valueOf(response.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getInt("day"));
                    temp_tomorrow = String.valueOf(response.getJSONArray("daily").getJSONObject(1).getJSONObject("temp").getInt("day"));

                    wind = String.valueOf(response.getJSONArray("daily").getJSONObject(0).getInt("wind_speed")+" kmh");

                    main = response.getJSONArray("daily").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main");


                    callback.displayResult(temp, desc, context, city, min_max, theme_tomorrow, temp_tomorrow, temp_today, wind, main);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jor);
    }

    public interface apiCallback {
        void displayResult(String temp, String desc,
                           Context context, String city,
                           String min_max, String theme_tomorrow,
                           String temp_tomorrow, String temp_today,
                           String wind, String main);
    }
}
