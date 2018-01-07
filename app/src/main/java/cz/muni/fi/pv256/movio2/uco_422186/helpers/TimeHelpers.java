package cz.muni.fi.pv256.movio2.uco_422186.helpers;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeHelpers {
    public static Date getCurrentTime() {
        Calendar cal = getCurrentCal();
        return cal.getTime();
    }

    private static Calendar getCurrentCal() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
    }

    public static String getNowReleaseDate() {
        return formatDateForRequest(getCurrentTime());
    }

    public static String getEndReleaseDate() {
        Calendar cal = getCurrentCal();
        cal.add(Calendar.MONTH, 1);
        return formatDateForRequest(cal.getTime());
    }

    public static String getWeekFromNowDate() {
        Calendar cal = getCurrentCal();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        return formatDateForRequest(cal.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    private static String formatDateForRequest(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String formatDateForDetailView(long time) {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(time));
    }
}
