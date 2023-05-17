package ru.tnkv.ecapp.utils;

import android.icu.util.Calendar;
import android.text.format.DateFormat;

import java.util.Locale;

public class DateConverter {
    public static String getHumanDate(long timestamp) {
        // Конвертирую unixtime в читаемую дату
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000);
        return DateFormat.format("dd-MM-yyyy H:m", cal.getTime()).toString();
    }
}
