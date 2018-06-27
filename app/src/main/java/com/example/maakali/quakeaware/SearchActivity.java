package com.example.maakali.quakeaware;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity /*implements LoaderManager.LoaderCallbacks<List<Earthquake>>*/ {
    private EditText startDate, endDate;
    private static final String apiURL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&limit=5000";
    private int mYear, mMonth, mDate;
    private static String mURL;
    Spinner spinner;
    LinearLayout searchFrame;
    FrameLayout progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        startDate = (EditText) findViewById(R.id.start_date);
        endDate = (EditText) findViewById(R.id.end_date);
        spinner = (Spinner) findViewById(R.id.spinner);

        setValuesInSpinner();

        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDate = calendar.get(Calendar.DAY_OF_MONTH);

        searchFrame = (LinearLayout) findViewById(R.id.search_frame);
        progressBarLayout = (FrameLayout) findViewById(R.id.layout_progressBar2);
        searchFrame.setVisibility(View.GONE);
    }

    private void setValuesInSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.magnitude_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void startDatePicker(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startDate.setText(year + "-" + (month+1) + "-" + dayOfMonth);
            }
        }, mYear, mMonth, mDate);
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.show();
    }

    public void endDatePicker(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDate.setText(year + "-" + (month+1) + "-" + dayOfMonth);
            }
        }, mYear, mMonth, mDate);
        dialog.getDatePicker().setMaxDate(new Date().getTime());
//        dialog.getDatePicker().setMinDate(new Date().getTime());
        dialog.show();
    }

    public void goForSearch(View view) {
        String startDateText = startDate.getText().toString();
        String endDateText = endDate.getText().toString();
        if((startDateText.equals("") || TextUtils.isEmpty(startDateText)) ||
                (endDateText.equals("") || TextUtils.isEmpty(endDateText))) {
            Toast.makeText(this, "Fill the Required Fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mURL = setURL(startDateText, endDateText);

        searchFrame.setVisibility(View.VISIBLE);
        progressBarLayout.setVisibility(View.VISIBLE);

        new EarthquakeAsynTask().execute(mURL);
    }

    private String setURL(String startDateText, String endDateText) {
        String minMag = spinner.getSelectedItem().toString();
        int mag = (int) Math.floor(Double.parseDouble(minMag));
        minMag = String.valueOf(mag);

        StringBuilder url = new StringBuilder(apiURL);
        url.append("&starttime=").append(startDate.getText().toString()).
                append("&endtime=").append(endDate.getText().toString()).
                append("&minmagnitude=").append(minMag);

        return url.toString();
    }

    private class EarthquakeAsynTask extends AsyncTask<String, Void, ArrayList<Earthquake>> {
        ArrayList<Earthquake> list = null;

        @Override
        protected ArrayList<Earthquake> doInBackground(String... urls) {
            if(urls.length < 1 || urls[0] == null)
                return null;

            list = QueryUtils.extractEarthquakes(urls[0]);
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Earthquake> earthquakes) {;
            progressBarLayout.setVisibility(View.GONE);

            ArrayAdapter<Earthquake> mAdpter = new EarthquakeAdapter(getBaseContext(), list);
            ListView listView = (ListView) findViewById(R.id.list_view2);
            listView.setAdapter(mAdpter);
        }

    }
}
