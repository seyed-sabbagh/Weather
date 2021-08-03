/*
 * *
 *  * Created by Seyed on 8/3/21, 6:23 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 8/3/21, 6:03 PM
 *
 */

package com.seyed.weather.Features;

import android.text.Html;
import android.text.Spanned;
import com.seyed.weather.R;

import java.net.URLDecoder;
import java.util.Random;

public class ThemePicker {

    public static int widget;

    public static int notification_ico;
    public static Spanned emoji;

    public static int app_theme;
    public static String day_hint;


    public void pickTheme(String main, String who) {



        String[] cloudy = {"Without clouds could be better","Just a bit of clouds","You won't need the sunglasses"};
        String[] clear = {"The weather's just perfect!","Take ur bf and let's walk a bit","Some vitamin D3 won't hurt"};
        String[] rain = {"Take ur umbrella with you!","Life ain't perfect","You'd better play LoL inside"};
        String[] snow = {"All the weather outside is frightful..","Kurwa, it's getting slippery!","Maybe sledges?"};
        String[] other = {"I can't even analyze it","No comments"};


        switch(main) {

            case "Clouds":

                if(who.equals("Widget_B")) {
                    widget = R.drawable.b_clouds;
                }
                if(who.equals("Widget_C")) {
                    widget = R.drawable.a_clouds;
                }
                if(who.equals("Widget_D")) {
                    widget = R.drawable.c_clouds;
                }
                notification_ico = R.drawable.clouds_icon;
                emoji = Html.fromHtml(URLDecoder.decode("CLOUDS"));
                app_theme = R.drawable.theme_clouds;
                day_hint = cloudy[new Random().nextInt(cloudy.length-1)];

                break;
            case "Clear":


               // if()
                if(who.equals("Widget_B")) {
                    widget = R.drawable.b_clouds;
                }
                if(who.equals("Widget_C")) {
                    widget = R.drawable.a_clouds;
                }
                if(who.equals("Widget_D")) {
                    widget = R.drawable.c_clouds;
                }
                notification_ico = R.drawable.clouds_icon;
                emoji = Html.fromHtml(URLDecoder.decode("CLOUDS"));
                app_theme = R.drawable.theme_clouds;
                day_hint = cloudy[new Random().nextInt(cloudy.length-1)];
                break;

//            case "Rain":
//                views.setImageViewResource(R.id.iv_ico, R.drawable.b_rain);
//                break;
//            case "Snow":
//                views.setImageViewResource(R.id.iv_ico, R.drawable.b_snow);
//                break;
//            default:
//                views.setImageViewResource(R.id.iv_ico, R.drawable.b_clouds);
        }

    }

}
