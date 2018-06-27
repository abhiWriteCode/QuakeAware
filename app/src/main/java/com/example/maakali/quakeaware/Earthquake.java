package com.example.maakali.quakeaware;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Earthquake {
    private double mAmplitude;
    private String exactLocation, locationDirection, dateFormat, timeFormat, mUrl;

    public Earthquake(double amplitude, String location, long UNIXTime, String url) {
        mAmplitude = amplitude;
        mUrl = url;

        setLocations(location);
        setDate(UNIXTime);
        setTime(UNIXTime);
    }

    public double getAmplitude() {
        return mAmplitude;
    }

    public String getLocationDirection() {
        return locationDirection;
    }

    public String getExactLocation() {
        return exactLocation;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public String getUrl() {
        return mUrl;
    }

    private void setDate(long UNIXTime) {
        Date date = new Date(UNIXTime);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        dateFormat = dateFormatter.format(date);
    }

    private void setTime(long UNIXTime) {
        Date date = new Date(UNIXTime);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm a");
        timeFormat = dateFormatter.format(date);
    }

    private void setLocations(String location) {
        String ld=null, el=null; // ld = locationDirection , el = exactLocation
        if(location.contains(" of ")){
            String[] strs = location.split(" of ");
            ld = strs[0] + " of ";
            el = strs[1];
        }
        else{
            String[] strs = location.split(" ");
            int l = strs.length;
            ld = "Near to" ;
            el = strs[l-2]+" "+strs[l-1];
        }
        setLocationDirection(ld);
        setExactLocation(el);
    }

    private void setExactLocation(String el) {
        exactLocation = el;
    }

    private void setLocationDirection(String ld) {
        locationDirection = ld;
    }
}
