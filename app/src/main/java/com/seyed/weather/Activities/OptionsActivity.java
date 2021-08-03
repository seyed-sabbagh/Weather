/*
 * *
 *  * Created by Seyed on 8/3/21, 6:23 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 8/3/21, 6:03 PM
 *
 */

package com.seyed.weather.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.seyed.weather.Services.UserPreferencesService;
import com.seyed.weather.R;

import static com.seyed.weather.Services.UserPreferencesService.PREF_FILE;

public class OptionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageButton btn_back_from_options;
    //public static final String PREF_FILE = "new9SHARED_PREF";

    /**
     * So here on OptionsActivity start we update UI using userPrefs file;
     *
     * On BackToMain click we save NEW userPrefs to a file and open MAinActivity.
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);


        // Define "preferences" as our file with user settings
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);

        // Check whether to display meteorologists pane or not
        if(sharedPreferences.getString("CHARACTERS_SET", "false").equals("true")) {
            findViewById(R.id.cl_characters).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.cl_characters).setVisibility(View.GONE);
        }

        // Display content of pref file in edit text field
        UserPreferencesService.load_UI(this);

        // For Units Spinner
        final Spinner spinner = (Spinner) findViewById(R.id.s_units);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Dispaly pre-selected unit from prefs
        //spinner.setSelection(adapter.getPosition(String.valueOf(this.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).getString("UNITS", "DEFAULT"))));
        spinner.setSelection(adapter.getPosition(String.valueOf(this.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).getString("UNITS", "DEFAULT"))));

        spinner.setOnItemSelectedListener(this);


        /* BackToMain */
        btn_back_from_options = findViewById((R.id.btn_back_from_options));
        btn_back_from_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserPreferencesService.setUserPrefs(OptionsActivity.this);
                openMain();
            }
        });

        Button btn_policy = findViewById(R.id.btn_privacy);
        btn_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://sites.google.com/view/weatherprivacy/home"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button btn_apply_options = findViewById(R.id.btn_apply_options);
        final EditText et_city = findViewById(R.id.et_location);
        btn_apply_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCity = String.valueOf(et_city.getText());
                getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("CITY", newCity).apply();
            }
        });

        CheckBox cb_daily = findViewById(R.id.cb_daily);
        cb_daily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if(isChecked) {
                    getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("DAILY", "true").apply();
                } else {
                    getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("DAILY", "false").apply();
                }
            }
        });
        CheckBox cb_rain = findViewById(R.id.cb_rain);
        cb_rain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if(isChecked) {
                    getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("RAIN", "true").apply();
                } else {
                    getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("RAIN", "false").apply();
                }
            }
        });
        CheckBox cb_temp = findViewById(R.id.cb_temp);
        cb_temp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if(isChecked) {
                    getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("TEMP", "true").apply();
                } else {
                    getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("TEMP", "false").apply();
                }
            }
        });
        CheckBox cb_sound = findViewById(R.id.cb_sound);
        cb_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if(isChecked) {
                    getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("SOUND", "true").apply();
                } else {
                    getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("SOUND", "false").apply();
                }
            }
        });
        CheckBox cb_widgets = findViewById(R.id.cb_widgets);
        cb_widgets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if(isChecked) {
                    getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("WIDGETS", "true").apply();
                } else {
                    getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("WIDGETS", "false").apply();
                }
            }
        });

        final RadioGroup rg_character = (RadioGroup) findViewById(R.id.rg_character);
        rg_character.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int id = rg_character.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.rb_gnome:
                        getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("THEME", "gnome").apply();
                        break;
                    case R.id.rb_alien:
                        getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("THEME", "alien").apply();
                        break;
                }
            }
        });

    }





    public void openMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        String selected = parent.getItemAtPosition(position).toString();

        if(selected.equals("Metric"))
        {
            // Do Preferences stuff
            getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("UNITS", "metric").apply();
        }
        if(selected.equals("Imperial"))
        {
            // Do Preferences stuff
            getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().putString("UNITS", "imperial").apply();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}