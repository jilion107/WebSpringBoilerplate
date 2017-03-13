package com.xmnjm.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author mandy.huang
 */
public class DateUtils {

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
}
