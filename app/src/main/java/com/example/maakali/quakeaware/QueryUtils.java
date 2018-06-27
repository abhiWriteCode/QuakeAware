package com.example.maakali.quakeaware;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public final class QueryUtils {
    /** Sample JSON response for a USGS query */
    //private static final String URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    private QueryUtils() {}


    public static ArrayList<Earthquake> extractEarthquakes(String urlString) {
        if(urlString==null || TextUtils.isEmpty(urlString) )
            return null;

        URL url =  createUrl(urlString);

        String json = "";
        try {
            json = makeRequest(url);
        }
        catch (Exception e) {}

        return extractEarthquakeFromRawJson(json);
    }

    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        }
        catch (MalformedURLException e) {
            return null;
        }
        return url;
    }

    private static String makeRequest(URL url){
        String json = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                json = readInputStream(inputStream);
            }
        }
        catch (IOException e) {Log.e(QueryUtils.class.getSimpleName(), "I/O Exception Occurs");}
        finally {
            try {
                if(urlConnection != null)
                    urlConnection.disconnect();
                if(inputStream != null)
                    inputStream.close();
            }
            catch (IOException e){}
        }

        return json; // return raw json
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        if(inputStream != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String s = in.readLine();
                while (s != null) {
                    sb.append(s);
                    s = in.readLine();
                }

                in.close();
            } catch (IOException e) {}
        }

        return sb.toString();
    }

    private static ArrayList<Earthquake> extractEarthquakeFromRawJson(String rawJson) {
        ArrayList<Earthquake> earthQuakes = new ArrayList<>();
        double mag;
        String place, previousPlace="";
        long time;
        String url;

//        root json-|
//                  |_features (json array)-|
//                                          |_each element in array-|
//                                                                  |_properties (json object)-|
//                                                                                             |_mag
//                                                                                             |_place
//                                                                                             |_time
//                                                                                             |_url

        try {
            JSONObject jsonRootObject = new JSONObject(rawJson);
            JSONArray feacturesArray = jsonRootObject.getJSONArray("features");

            for(int i=0; i<feacturesArray.length(); i++) {
                JSONObject currentEarthquakes = feacturesArray.getJSONObject(i);
                JSONObject propertiesObj = currentEarthquakes.getJSONObject("properties");

                mag = propertiesObj.getDouble("mag");
                place = propertiesObj.getString("place");
                time = propertiesObj.getLong("time");
                url = propertiesObj.getString("url");

                if(isSameAreaOrLocation(place, previousPlace, i) && (mag >= 0)) {
                    earthQuakes.add(new Earthquake(mag, place, time, url));
                    previousPlace = place;
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return earthQuakes;
    }

    private static boolean isSameAreaOrLocation(String place, String previousPlace, int i) {
        if(TextUtils.isEmpty(place) || TextUtils.isEmpty(previousPlace))
            return true;

        if(place.contains(" of ") && previousPlace.contains(" of ")) {
            String[] arr1 = place.split(" of ");
            String[] arr2 = previousPlace.split(" of ");

            Log.i("place : " + i + " ==>> ", arr1[1]);
            Log.i("previous place : ", arr2[1]);

            return !arr1[1].equals(arr2[1]);
        }
        return false;
    }

}
