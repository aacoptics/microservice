package com.aacoptics.wlg.dashboard.util;

public class DateUtil {
    public static String formatSeconds(int seconds) {
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            int second = seconds % 60;
            int min = seconds / 60;
            timeStr = min + "分" + second + "秒";
            if (min > 60) {
                min = (seconds / 60) % 60;
                int hour = (seconds / 60) / 60;
                timeStr = hour + "小时" + min + "分" + second + "秒";
            }
        }
        return timeStr;
    }
}
