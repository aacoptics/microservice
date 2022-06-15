package com.aacoptics.pack.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
        int listSize = list.size();
        int page = (listSize + (pageSize -1)) / pageSize;
        List<List<T>> listArray = new ArrayList<List<T>>();
        for (int i = 0; i < page; i++) {
            List<T> subList = new ArrayList<T>();
            for (int j = 0; j < listSize; j++) {
                int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize;
                if (pageIndex == (i+1)) {
                    subList.add(list.get(j));
                }
                if ((j+1) == (j+1)*pageSize) {
                    break;
                }
            }
            listArray.add(subList);
        }
        return listArray;
    }
}