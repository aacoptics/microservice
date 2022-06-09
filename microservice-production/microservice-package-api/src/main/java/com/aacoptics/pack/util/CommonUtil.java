package com.aacoptics.pack.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonUtil {

    public static String getWeekNo(String time, int startDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(startDate);
        calendar.setTime(date);
        int i=calendar.get(Calendar.WEEK_OF_YEAR);
        return String.valueOf(i);
    }

    public static String flushLeft(String c, int length, String target) {
        StringBuilder cs = new StringBuilder();
        if(target.length() < length){
            for(int i = 0; i < length - target.length(); i++){
                cs.insert(0, c);
            }
            target = cs + target;
        }
        return  target;
    }
}