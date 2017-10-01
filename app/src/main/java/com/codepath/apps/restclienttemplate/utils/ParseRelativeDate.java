package com.codepath.apps.restclienttemplate.utils;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Weeks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by tiago on 9/29/17.
 */

public class ParseRelativeDate {

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {

        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        String relativeDate = "";

        try {
            DateTime createdDateTime = new DateTime(sf.parse(rawJsonDate).getTime());
            DateTime currentDateTime = new DateTime();

            int secondDifference = Seconds.secondsBetween(createdDateTime.toLocalDateTime(),
                    currentDateTime.toLocalDateTime()).getSeconds();
            if (secondDifference < 60) {
                relativeDate = Integer.toString(secondDifference) + "s";
            } else if (Minutes.minutesBetween(createdDateTime.toLocalDateTime(),
                    currentDateTime.toLocalDateTime()).getMinutes() < 60) {
                relativeDate = Integer.toString(Minutes.minutesBetween(createdDateTime.toLocalDateTime(), currentDateTime.toLocalDateTime()).getMinutes())+"m";
            } else if (Hours.hoursBetween(createdDateTime.toLocalDateTime(),
                    currentDateTime.toLocalDateTime()).getHours() < 24) {
                relativeDate = Integer.toString(Hours.hoursBetween(createdDateTime.toLocalDateTime(),currentDateTime.toLocalDateTime()).getHours())+"h";
            } else if (Months.monthsBetween(createdDateTime.toLocalDateTime(),
                    currentDateTime.toLocalDateTime()).getMonths() < 1) {
                relativeDate = Integer.toString(Weeks.weeksBetween(
                        createdDateTime.toLocalDateTime(), currentDateTime.toLocalDateTime()).getWeeks()) + "w";
            } else {
                relativeDate = Integer.toString(Months.monthsBetween(createdDateTime.toLocalDateTime(), currentDateTime.toLocalDateTime()).getMonths())+"M";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
