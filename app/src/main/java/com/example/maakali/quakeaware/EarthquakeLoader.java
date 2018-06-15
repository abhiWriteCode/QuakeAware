package com.example.maakali.quakeaware;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public final class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        List<Earthquake> list = QueryUtils.extractEarthquakes(mUrl);
        return list;
    }
}
