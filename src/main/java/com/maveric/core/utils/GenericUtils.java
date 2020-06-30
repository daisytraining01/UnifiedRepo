package com.maveric.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GenericUtils {
    private static DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss_a");

    public static String getTimeStamp() {
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }


}
