/*
 * *
 *  * Created by Seyed on 8/3/21, 6:23 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 8/3/21, 6:03 PM
 *
 */

package com.seyed.weather.Activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.seyed.weather.R;
import com.seyed.weather.Services.ForecastService;
import com.seyed.weather.Services.ReminderBroadcast;
import com.seyed.weather.Services.UserPreferencesService;
import com.seyed.weather.Structures.TimeAndDate;
import com.seyed.weather.databinding.ActivityMainBinding;
import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;



/* This class updates User interface
 *
 * NOTE:
 * This class ONLY updates User interface */



public class MainActivity extends AppCompatActivity /*implements UserLocationService.UserLocationCallback*/ {


    /** Opens Options Activity on button click **/
    public void openOptions() {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    //For enabling binding: Declare binding object from binding class
    ActivityMainBinding binding;
    Dialog dialog;

    private void displayWidgets() {
        if(UserPreferencesService.getWidgets(this).equals("true")) {
            binding.clWidgets.setVisibility(View.VISIBLE);
        } else {
            binding.clWidgets.setVisibility(View.GONE);
        }

        binding.btnCollapseWidgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.clWidgets.setVisibility(View.GONE);
                UserPreferencesService.setWidgets(MainActivity.this, "false");
            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void checkViktor() {
//        if(UserPreferencesService.getCharactersSettingsVisibility(this).equals("true")) {
//            findViewById(R.id.cl_characters).setVisibility(View.VISIBLE);
//        } else {
//            findViewById(R.id.cl_characters).setVisibility(View.GONE);
//        }


        // After Double tap on Zing a Viktor dialog will appear:
        dialog = new Dialog(this);
        binding.ivTheme.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {

                    if(UserPreferencesService.isViktorUnlocked(MainActivity.this).equals("false")) {
                        showViktorDialog();
                        UserPreferencesService.setViktorUnlocked(MainActivity.this, "true");
                    }
                    return super.onDoubleTap(e);
                }
            });

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });


    }

    private static void createNotificationChannel(MainActivity mainActivity) {


        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "mainChannel";
            String description = "mainChannelDescription";
            // Check from prefs if we want notification sound
            int importance;
            if(UserPreferencesService.isSound(mainActivity).equals("true")) {
                importance = NotificationManager.IMPORTANCE_DEFAULT;
            } else {
                importance = NotificationManager.IMPORTANCE_LOW;
            }
            NotificationChannel channel = new NotificationChannel("alaska", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = mainActivity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void callNotificationGenerator(){
        Intent aalarmIntent = new Intent(MainActivity.this, ReminderBroadcast.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, aalarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    /* Calls SetupActivity if we do not have city name and units specified or weather forecast otherwise*/
    private void prefsGetter(){

        // Checks if on app start we have city and units set. If no - open SetupActivity
        if (UserPreferencesService.getPrefCity(this) == "DEFAULT"
                || UserPreferencesService.getPrefUnits(this) == "DEFAULT") {
            Intent intent = new Intent(this, SetupActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Pass some parameters here to getForecast ↓
            ForecastService.getForecast(this, UserPreferencesService.getPrefCity(this), UserPreferencesService.getPrefUnits(this));
        }

    }

    /* Shows and hides Viktor dialog */
    private void showViktorDialog() {
        dialog.setContentView(R.layout.surprize_mf_a);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //stuff for rendering gif:
        ImageView sup_a = dialog.findViewById(R.id.iv_sup_a);
        Glide.with(MainActivity.this)
                .load(R.drawable.sup_a)
                .into(sup_a);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //animation

        // close button click
        dialog.findViewById(R.id.btn_tryem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
                dialog.dismiss();
            }
        });

        dialog.show();
        UserPreferencesService.setPrefTheme(this, "alien");
        //UserPreferencesService.setCharactersSettingsVisibility(this, "true");
        ForecastService.getForecast(this, UserPreferencesService.getPrefCity(this), UserPreferencesService.getPrefUnits(this));
    }



    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //2 more lines for enabling binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.shimmer.startShimmer();

        // Calls the Notification Generator every day at 8 AM
        createNotificationChannel(this);
        callNotificationGenerator();

        // Calls weather forecast or SetupActivity
        prefsGetter();

        // Check if we should show widgets pane
        displayWidgets();


        //Check if we should show you character settings, viktor congrats pane on DC etc.
        checkViktor();



        /** =============== ↑ IMPORTANT ZONE ↑ =======================================================*/
        /* For button OPTIONS */
        ImageButton btn_options = findViewById((R.id.btn_options));
        btn_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openOptions();
                finish();
            }
        });



        final RadioGroup group = (RadioGroup) findViewById(R.id.rg_dayOfTheWeek);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                final Vibrator vibrator = (Vibrator)getSystemService(MainActivity.VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE));

                int id = group.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.rb_Mon:
                        try {
                            loadUI(0);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        binding.rbMon.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation));
                        break;
                    case R.id.rb_Tue:
                        try {
                            loadUI(1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        binding.rbTue.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation));
                        break;
                    case R.id.rb_Wed:
                        try {
                            loadUI(2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        binding.rbWed.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation));
                        break;
                    case R.id.rb_Thr:
                        try {
                            loadUI(3);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        binding.rbThr.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation));
                        break;
                    case R.id.rb_Fri:
                        try {
                            loadUI(4);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        binding.rbFri.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation));
                        break;
                    case R.id.rb_Sat:
                        try {
                            loadUI(5);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        binding.rbSat.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation));
                        break;
                    case R.id.rb_Sun:
                        try {
                            loadUI(6);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        binding.rbSun.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation));
                        break;
                }
            }
        });

        final SwipeRefreshLayout refreshLayout = findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                /** 2nd GETFORECAST() call out of 2 **/
                ForecastService.getForecast(MainActivity.this, UserPreferencesService.getPrefCity(MainActivity.this), UserPreferencesService.getPrefUnits(MainActivity.this));
                refreshLayout.setRefreshing(false);
            }
        });

        // Horizontal scroll control arrow
        binding.ibScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.ibScroll.getRotation() == -90)
                {
                    binding.horizontalScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
                }
                else
                {
                    binding.horizontalScrollView.fullScroll(ScrollView.FOCUS_LEFT);
                }
            }
        });


        binding.horizontalScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //binding.latlon.setText(String.valueOf(scrollX));

                if(scrollX == 0)
                {
                    binding.ibScroll.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation));
                    binding.ibScroll.setRotation(-90);
                }
                if(scrollX == binding.horizontalScrollView.getChildAt(0).getWidth() - binding.horizontalScrollView.getWidth())
                {
                    binding.ibScroll.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation));
                    binding.ibScroll.setRotation(90);
                }
            }
        });

        // Horizontal scroll control arrow (FOR WIDGETS)
        binding.ibScrollB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.ibScrollB.getRotation() == -90)
                {
                    binding.horizontalScrollView2.fullScroll(ScrollView.FOCUS_RIGHT);
                }
                else
                {
                    binding.horizontalScrollView2.fullScroll(ScrollView.FOCUS_LEFT);
                }
            }
        });
        binding.horizontalScrollView2.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
               // binding.widggggg.setText(String.valueOf(scrollX));

                if(scrollX == 0)
                {
                    binding.ibScrollB.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation));
                    binding.ibScrollB.setRotation(-90);
                }
                if(scrollX == binding.horizontalScrollView2.getChildAt(0).getWidth() - binding.horizontalScrollView2.getWidth())
                {
                    binding.ibScrollB.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation));
                    binding.ibScrollB.setRotation(90);
                }
            }
        });

        // BEST HEADER ANIMATION IN THE WORLD
        binding.scrollView2.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                binding.clHeader.setAlpha((float)scrollY/100);
                binding.tvRegion.setAlpha((float)(100-scrollY)/100);

                int textSize = 24 * (100- scrollY)/100;

                binding.tvDay.setTextSize(textSize);
                if(textSize < 15)
                {
                    binding.tvDay.setTextSize(15);
                }
                if(textSize >= 24)
                {
                    binding.tvDay.setTextSize(24);
                }

            }
        });

    }


    /** Assigns right M T W T F S S sequence to RB-group + checks the 0th day **/
    public void updateButtonPanel() {

        RadioButton daysRadioButtons[] = {
                binding.rbMon,
                binding.rbTue,
                binding.rbWed,
                binding.rbThr,
                binding.rbFri,
                binding.rbSat,
                binding.rbSun
        };

        binding.rbMon.setChecked(true);

        for(int i=0; i<=6; i++)
        {
            daysRadioButtons[i].setText(String.valueOf(new ForecastService().arr_date[i].charAt(0)));
        }
    }


    /** Sets background Zing image and description label */
    public  void themePicker(int dayIndex) throws ParseException {
        TextView tv_myWeatherDescription = findViewById(R.id.tv_hint);
        ImageView iv_theme = findViewById(R.id.iv_theme);

        tv_myWeatherDescription.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        switch(new ForecastService().arr_theme[dayIndex]) {

            case "Clouds":
                if(UserPreferencesService.getPrefTheme(this).equals("gnome")) {
                    tv_myWeatherDescription.setText("Just a bit of clouds");
                    iv_theme.setImageResource(R.drawable.theme_clouds);
                } else {
                    tv_myWeatherDescription.setText("Clouds, u know");
                    iv_theme.setImageResource(R.drawable.theme_clouds_vik);
                }
                break;
            case "Clear":


                /** CHECKING WHETHER WE SHOULD PUT MOON OR THE SUN IMAGE
                 * so listen, we put sun if the curent tme is between sunrise and sunset, so at the day time
                 * so current time is after (>) sunrise and before (<) sunset
                 *
                 * if you think this if statement is complicated you are correct
                 */
                if(dayIndex == 0)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    if(sdf.parse(new TimeAndDate().getTimeFormat().format(Calendar.getInstance().getTime())).getTime() < sdf.parse(new ForecastService().arr_sunset[dayIndex]).getTime() && sdf.parse(new TimeAndDate().getTimeFormat().format(Calendar.getInstance().getTime())).getTime() > sdf.parse(new ForecastService().arr_sunrise[dayIndex]).getTime())
                    {
                        if(UserPreferencesService.getPrefTheme(this).equals("gnome")) {
                            tv_myWeatherDescription.setText("The weather's just perfect!");
                            iv_theme.setImageResource(R.drawable.theme_sun);
                        } else {
                            tv_myWeatherDescription.setText("Go grab some taco with bros");
                            iv_theme.setImageResource(R.drawable.theme_sun_vik);
                        }
                    } else {
                        if(UserPreferencesService.getPrefTheme(this).equals("gnome")) {
                            tv_myWeatherDescription.setText("Pretty cool night!");
                            iv_theme.setImageResource(R.drawable.theme_moon);
                        } else {
                            tv_myWeatherDescription.setText("Pretty clear sky");
                            iv_theme.setImageResource(R.drawable.theme_moon_vik);
                        }

                    }
                } else {
                    if(UserPreferencesService.getPrefTheme(this).equals("gnome")) {
                        tv_myWeatherDescription.setText("The weather's just perfect!");
                        iv_theme.setImageResource(R.drawable.theme_sun);
                    } else {
                        tv_myWeatherDescription.setText("Go grab some taco with bros");
                        iv_theme.setImageResource(R.drawable.theme_sun_vik);
                    }
                }

                break;
            case "Rain":
                if(UserPreferencesService.getPrefTheme(this).equals("gnome")) {
                    tv_myWeatherDescription.setText("Take ur umbrella with you!");
                    iv_theme.setImageResource(R.drawable.theme_rain);
                } else {
                    tv_myWeatherDescription.setText("Quite wet here, u know");
                    iv_theme.setImageResource(R.drawable.theme_rain_vik);
                }

                break;
            case "Snow":
                tv_myWeatherDescription.setText("All the weather outside is frightful..");
                iv_theme.setImageResource(R.drawable.theme_snow);
                break;
            default:
                tv_myWeatherDescription.setText("It's kinda strange outside");
                iv_theme.setImageResource(R.drawable.theme_clouds);
        }
    }


    /**
     * Fills all labels using all the data arrays
     * 1 - This function takes day index from pressed button (0-6) or from loadApp()(0)
     * 2 - DECLARE and INITIALIZE LOCAL text labels with data from WeatherData
     *
     * NOTE:
     * In order to avoid confusion, text labels will be initialized as they appear
     * on the app screen (one after another).
     */
    public void updateData(int dayIndex)
    {
        // Tuesday, Feb 13
        binding.tvDay.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        binding.tvDay.setText(new ForecastService().arr_date[dayIndex]);

        // Gliwice at 10:53
        binding.tvRegion.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        binding.tvRegion.setText(StringUtils.capitalize(UserPreferencesService.getPrefCity(this)) + " at " + String.valueOf(new TimeAndDate().getTimeFormat().format(new TimeAndDate().getCurrentDateAndTime())));

        //  -9°C
        binding.tvTemperature.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        if (dayIndex == 0) {
            if (UserPreferencesService.getPrefUnits(this).equals("metric"))
            {
                binding.tvTemperature.setText(new ForecastService().today_temperature + "°C");
            }
            if (UserPreferencesService.getPrefUnits(this).equals("imperial"))
            {
                binding.tvTemperature.setText(new ForecastService().today_temperature + "°F");
            }

        } else {
            if (UserPreferencesService.getPrefUnits(this).equals("metric"))
            {
                binding.tvTemperature.setText(new ForecastService().arr_temperature[dayIndex] + "°C");
            }
            if (UserPreferencesService.getPrefUnits(this).equals("imperial"))
            {
                binding.tvTemperature.setText(new ForecastService().arr_temperature[dayIndex] + "°F");
            }
        }

        //TODO: I create a separate object WeatherData for every property. it
        // will be better to instantiate one obj and use its properties
        //-3°...0°
        binding.tvMinMax.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        binding.tvMinMax.setText(new ForecastService().arr_min_max[dayIndex]);

        // Feels like -5°, scattered clouds
        binding.tvDetails.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        binding.tvDetails.setText(new ForecastService().arr_details[dayIndex]);

        // POP
        binding.tvPop.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        binding.tvPop.setText(new ForecastService().arr_pop[dayIndex]);

        // Wind
        binding.tvWind.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        binding.tvWind.setText(new ForecastService().arr_wind[dayIndex]);

        // Humidity
        binding.tvHumidity.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        binding.tvHumidity.setText(new ForecastService().arr_humidity[dayIndex]);





        // UV
        binding.tvUvi.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        binding.tvUvi.setText(new ForecastService().arr_uvi[dayIndex]);

        // Pressure
        binding.tvPressure.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        binding.tvPressure.setText(new ForecastService().arr_pressure[dayIndex]);

        // Clouds
        binding.tvClouds.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        binding.tvClouds.setText(new ForecastService().arr_clouds[dayIndex]);

        // Sunrise
        binding.tvSunrise.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        binding.tvSunrise.setText(new ForecastService().arr_sunrise[dayIndex]);

        // Sunset
        binding.tvSunset.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
        binding.tvSunset.setText(new ForecastService().arr_sunset[dayIndex]);


        /**  =================================================================================================== **/
        /**                INSIDE THE DAY PANELS                                                                 **/
        /**  =================================================================================================== **/

        TextView timeColVals[] = {
                binding.tvTime0, binding.tvTime1, binding.tvTime2, binding.tvTime3, binding.tvTime4, binding.tvTime5,
                binding.tvTime6, binding.tvTime7, binding.tvTime8, binding.tvTime9, binding.tvTime10, binding.tvTime11
        };

        TextView temperatureColVals[] = {
                binding.tvTemp0, binding.tvTemp1, binding.tvTemp2, binding.tvTemp3, binding.tvTemp4, binding.tvTemp5,
                binding.tvTemp6, binding.tvTemp7, binding.tvTemp8, binding.tvTemp9, binding.tvTemp10, binding.tvTemp11
        };

        TextView pressureColVals[] = {
                binding.tvPressure0, binding.tvPressure1, binding.tvPressure2, binding.tvPressure3, binding.tvPressure4,
                binding.tvPressure5, binding.tvPressure6, binding.tvPressure7, binding.tvPressure8, binding.tvPressure9,
                binding.tvPressure10, binding.tvPressure11
        };

        ImageView icosColVals[] = {
                binding.ivIco0, binding.ivIco1, binding.ivIco2, binding.ivIco3, binding.ivIco4, binding.ivIco5,
                binding.ivIco6, binding.ivIco7, binding.ivIco8, binding.ivIco9, binding.ivIco10, binding.ivIco11
        };

        /**
         * Now it gets serious.
         * if the day is TODAY (index 0), we have to:
         *      - use variable "today_temperature" as it's more precise.
         *      - set BIG "Inside the day panel" as it's more precise.
         *
         * if the day is any other (indices 1-6):
         *      - use "arr_temperature" as we have access to it.
         *      - use SMALL "inside the day panel" as we have access only to it.
         */

        if (dayIndex == 0)
        {
            //binding.tvTemperature.setText(new ForecastService().today_temperature + "°C");

            // Activate all 4 columns of big panel
            binding.llTime.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
            binding.llTime.setVisibility(View.VISIBLE);

            binding.llTemp.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
            binding.llTemp.setVisibility(View.VISIBLE);

            binding.llIcos.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
            binding.llIcos.setVisibility(View.VISIBLE);

            binding.llPres.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
            binding.llPres.setVisibility(View.VISIBLE);

            // Deactivate small panel
            binding.llInsideTheDayShortened.setVisibility(View.GONE);

            /**
             *  Attention!
             *  here we assign each 12 values for each of 4 data columns:
             *          - Time column vals with time array;
             *          - Temperature column vals with temp array;
             *          - pressure - with pressure;
             *          BUT!
             *          - Column "icos" we assign depending on arr_descript, thus SWITCH.
             *          **/
            for(int i=0; i<=11; i++) {
                timeColVals[i].setText(new ForecastService().arr_time[i]);
                temperatureColVals[i].setText(new ForecastService().arr_temp[i]);
                pressureColVals[i].setText(new ForecastService().arr_pres[i]);


                switch (new ForecastService().arr_descript[i]) {
                    case "Clouds":
                        icosColVals[i].setImageResource(R.drawable.clouds_icon);
                        break;
                    case "Clear":
                        icosColVals[i].setImageResource(R.drawable.sun_icon);
                        break;
                    case "Rain":
                        icosColVals[i].setImageResource(R.drawable.rain_icon);
                        break;
                    case "Snow":
                        icosColVals[i].setImageResource(R.drawable.snow_icon);
                        break;
                    default:
                        icosColVals[i].setImageResource(R.drawable.clouds_icon);
                }
            }
        }
        else //  IF any other day (indexes (indices) 0-6):
        {
            // Temperature we get from array
            //binding.tvTemperature.setText(new ForecastService().arr_temperature[dayIndex] + "°C");

            // Deactivate all 4 cols of big panel
            binding.llTime.setVisibility(View.GONE);
            binding.llTemp.setVisibility(View.GONE);
            binding.llIcos.setVisibility(View.GONE);
            binding.llPres.setVisibility(View.GONE);

            // Activate small panel
            binding.llInsideTheDayShortened.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
            binding.llInsideTheDayShortened.setVisibility(View.VISIBLE);

            // Assign values to SMALL panel:
            binding.tvTempMorning.setText(new ForecastService().arr_morning[dayIndex]);
            binding.tvTempAfternoon.setText(new ForecastService().arr_afternoon[dayIndex]);
            binding.tvTempEvening.setText(new ForecastService().arr_eve[dayIndex]);
            binding.tvTempNight.setText(new ForecastService().arr_night[dayIndex]);

        }
        /**  =================================================================================================== **/
        /**               END OF INSIDE THE DAY PANELS                                                           **/
        /**  =================================================================================================== **/


    }


    /** Updates UI on RESTART **/
    public void reloadUI() throws ParseException {

        /**
         * Runs ONLY on app START or RESTART. (getForecast() calls it)
         *
         * 1 - updates all text labels with data for the CURRENT DAY.
         * 2 - updates background image of Zing with weather condition for the CURRENT DAY.
         * 3 - updates bottom button panel setting the CURRENT day letter first in a row. (Also highlights it)
         */

        updateData(0);

        themePicker(0);

        updateButtonPanel();
    }


    /**  Updates UI ANYTIME **/
    public void loadUI(int dayIndex) throws ParseException {

        /**
         * Runs EVERY TIME you change the day. (called after button clicks)
         *
         * 1 - updates all text labels with data for the CHOSEN DAY.
         * 2 - updates background image of Zing with weather condition for the CHOSEN DAY.
         */

        updateData(dayIndex);

        themePicker(dayIndex);

    }

}