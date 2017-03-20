package com.xmnjm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mandy.huang
 */
public class DateUtils {
    public static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";

    /**
     * 取date+days
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date parse(String date, String formatPattern) {
        if (null == date) {
            return null;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatPattern);
            return format.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
