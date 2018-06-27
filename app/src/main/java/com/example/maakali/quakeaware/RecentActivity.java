package com.example.maakali.quakeaware;


import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RecentActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
    private ArrayAdapter<Earthquake> mAdpter;
    private static final String URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&limit=100";
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        frameLayout = (FrameLayout) findViewById(R.id.layout_progressBar1);
        frameLayout.setVisibility(View.VISIBLE);

        mAdpter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        ListView listView = (ListView) findViewById(R.id.list_view1);
        listView.setAdapter(mAdpter);

        LoaderManager loader = getLoaderManager();
        loader.initLoader(0, null, this);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this, URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        frameLayout.setVisibility(View.GONE);
        mAdpter.clear();

        if(data != null)
            mAdpter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mAdpter.clear();
    }
}
