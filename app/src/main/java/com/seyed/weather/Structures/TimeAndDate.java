/*
 * *
 *  * Created by Seyed on 8/3/21, 6:23 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 8/3/21, 6:03 PM
 *
 */

package com.seyed.weather.Structures;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * CLASS FOR MANAGING TIME AND DATE FORMATS
 *
 * NOTE:
 * I am proud of this file's structure.
 */

public class TimeAndDate {

    /**
     * 1 - After the first API call we SET the "timezone_offset" and "currentDateAndTime".
     * 2 - We GET back ready "dateFormat" and "timeFormat" with CORRECT OFFSET.
     * ...
     * 2.1 - "currentDateAndTime" here is not in the deal, we are just storing it here.
     */

    private static int timezone_offset;            // Depends on lon\lat from the API call, used for setting the proper currentDateAndTime ↓
    private static Long currentDateAndTime;        // Time of app refresh (last API call)
    private static SimpleDateFormat dateFormat;    // "Tuesday, Feb 16"
    private static SimpleDateFormat timeFormat;    // "6:25"


    public int getTimezone_offset() {
        return timezone_offset;
    }

    public void setTimezone_offset(int timezone_offset) {
        this.timezone_offset = timezone_offset;
    }

    public static Long getCurrentDateAndTime() {
        return currentDateAndTime;
    }

    public void setCurrentDateAndTime(Long currentDateAndTime) {
        this.currentDateAndTime = currentDateAndTime;
    }

    /**
     * @return date format like "Tuesday, Feb 16" + a proper offset
     */
    public SimpleDateFormat getDateFormat() {
        dateFormat = new SimpleDateFormat("EEEE, MMM d", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone(String.valueOf("GMT+" + timezone_offset)));
        return dateFormat;
    }

    /**
     * @return time format like "6:27" + a proper offset
     */
    public SimpleDateFormat getTimeFormat() {
        timeFormat = new SimpleDateFormat("H:mm", Locale.ENGLISH);
        timeFormat.setTimeZone(TimeZone.getTimeZone(String.valueOf("GMT+" + timezone_offset)));
        return timeFormat;
    }


}
