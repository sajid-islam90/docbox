package com.example.sajid.myapplication.util;

import android.content.Context;

import com.example.sajid.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nevermore on 10/24/2015.
 */

public class calendar_descriptor {

    public static String[] dates;
    public static String[] inventory;
    public static boolean[] dates_colors;
    public static boolean[] stopsellFlags;
    public static int months_added = 0;
    public static int total_cells = 35;
    public static int cells_to_be_added=0;
    public static int number_last_days_last_month;
    public static int number_of_days_from_last;

    public static Date startDate = new Date(), endDate = new Date();

    public static int getTotalDays() {
        Calendar cd = Calendar.getInstance();

        cd.setTime(startDate);
        cd.set(Calendar.DAY_OF_MONTH, 1);
        startDate = cd.getTime();
        cd.add(Calendar.MONTH, 1);
        endDate = cd.getTime();
        cd.add(Calendar.MONTH, -1);
        int first_day_of_month = cd.get(Calendar.DAY_OF_WEEK);
        int total_prev_month_days = first_day_of_month-1;
        number_last_days_last_month=total_prev_month_days;
        int last_day_of_this_month = cd.getActualMaximum(Calendar.DAY_OF_MONTH);
        int totaldays = total_prev_month_days+last_day_of_this_month;
        if(totaldays>35){
            totaldays = 42;
        }
        else if(totaldays<=28){
            totaldays = 28;
        }
        else{
            totaldays = 35;
        }
        number_of_days_from_last = (last_day_of_this_month+total_prev_month_days);
        return totaldays;
    }

    public static String[] getDates(int totalDays) {
        String[] s = new String[totalDays];
        calendar_descriptor.dates_colors = new boolean[totalDays];
        Calendar cal = Calendar.getInstance();
        if(startDate!=null) {
            cal.setTime(startDate);
        }
        //  cal.add(Calendar.MONTH, calendar_descriptor.months_added);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int first_day_of_month = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.MONTH, -1);
        // cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.MONTH)) ;
        System.out.print(cal.getTime());
        int last_day_of_last_month = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.add(Calendar.MONTH, 1);
        int i=1;
        int no_of_days_from_last_month = first_day_of_month-1;
        for( i=0;i<no_of_days_from_last_month;i++){
            s[i] = last_day_of_last_month-first_day_of_month+2+i+"";
            calendar_descriptor.dates_colors[i] = true;
        }
        int last_day_of_this_month = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int count =  1;
        while(i<totalDays )
        {

            if(i>last_day_of_this_month+first_day_of_month-2 && count == 1){
                s[i] = 1+"";
                count = i-1;
                calendar_descriptor.dates_colors[i] = true;
            }
            else if(i>last_day_of_this_month && count!=1){
                s[i] = i-count +"";
                calendar_descriptor.dates_colors[i]  = true;
            }
            else {
                s[i] = i + 2 - first_day_of_month + "";
                calendar_descriptor.dates_colors[i]  = false;
            }
            i++;
        }

        dates = s;
        return s;
    }
}