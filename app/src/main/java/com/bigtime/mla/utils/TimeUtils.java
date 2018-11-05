package com.bigtime.mla.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shijil Kadambath on 11/5/18.
 * Email: shijilkadambath@gmail.com
 */
public class TimeUtils {


    public static String formatDateChatHeader(Date date) {
        try {
            Calendar rightNow = Calendar.getInstance();
            int day = rightNow.get(Calendar.DAY_OF_YEAR);
            int year = rightNow.get(Calendar.YEAR);
            rightNow.setTimeInMillis(date.getTime());

            int dateDay = rightNow.get(Calendar.DAY_OF_YEAR);
            int dateYear = rightNow.get(Calendar.YEAR);

            if (dateDay == day && year == dateYear) {
                return "Today";
            } else if (dateDay + 1 == day && year == dateYear) {
                return "Yesterday";
            } else {// if (year == dateYear) {
                //return formatDate(rightNow.getTime(),"dd MMM");
                return formatDate(rightNow.getTime(), "dd MMM yyyy");
            } /*else {
                return formatDate(rightNow.getTime(),"dd MMM yy");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "LOC_ERR";
    }
    private static String formatDate(Date date, String formate) {
        DateFormat df = new SimpleDateFormat(formate);
        return df.format(date);
    }

    public static Date getDateFromString(String date){
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        // val date:Date = formatDate.parse(program!!.date)

    }
}
