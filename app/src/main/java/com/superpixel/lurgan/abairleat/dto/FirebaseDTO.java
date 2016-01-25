package com.superpixel.lurgan.abairleat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Martin on 1/25/16.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class FirebaseDTO {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    abstract HashMap<String, Object> toMap();

    public static String formatDate(Date date) {
        if(date == null) return null;

        return dateFormat.format(date);
    }

    public static Date deserializeDateString(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
