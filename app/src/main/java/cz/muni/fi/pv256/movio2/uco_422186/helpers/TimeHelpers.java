package cz.muni.fi.pv256.movio2.uco_422186.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeHelpers {
    public static Date getCurrentTime() {
        Calendar cal = getCurrentCal();
        return cal.getTime();
    }

    public static Calendar getCurrentCal() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
    }

    public static String getNowReleaseDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(getCurrentTime());
    }

    public static String getEndReleaseDate() {
        Calendar cal = getCurrentCal();
        cal.add(Calendar.MONTH, 1);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }
}
