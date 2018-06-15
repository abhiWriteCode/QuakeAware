package com.example.maakali.quakeaware;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;


import java.util.ArrayList;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    public EarthquakeAdapter(Context context, ArrayList<Earthquake> earthQuakes) {
        super(context,0, earthQuakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        // Check if the existing view is being reuse, otherwise inflate the view
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent,
                    false);

        Earthquake currentEarthQuake = getItem(position);

        TextView amplitude = (TextView) listItemView.findViewById(R.id.amplitude);
        amplitude.setText(String.valueOf(currentEarthQuake.getAmplitude()));

        TextView locationDirection = (TextView) listItemView.findViewById(R.id.location_direction);
        locationDirection.setText(currentEarthQuake.getLocationDirection());

        TextView city = (TextView) listItemView.findViewById(R.id.exact_location);
        city.setText(currentEarthQuake.getExactLocation());

        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(currentEarthQuake.getDateFormat());

        TextView time = (TextView) listItemView.findViewById(R.id.time);
        time.setText(currentEarthQuake.getTimeFormat());

        GradientDrawable amplitudeCircle = (GradientDrawable) amplitude.getBackground();
        int amplitudeBGColor = 0 ;
        int switchCase = (int) Math.floor(currentEarthQuake.getAmplitude());

        switch (switchCase) {
            case 0:
            case 1: amplitudeBGColor = ContextCompat.getColor(getContext(), R.color.magnitude1); break;
            case 2: amplitudeBGColor = ContextCompat.getColor(getContext(), R.color.magnitude2); break;
            case 3: amplitudeBGColor = ContextCompat.getColor(getContext(), R.color.magnitude3); break;
            case 4: amplitudeBGColor = ContextCompat.getColor(getContext(), R.color.magnitude4); break;
            case 5: amplitudeBGColor = ContextCompat.getColor(getContext(), R.color.magnitude5); break;
            case 6: amplitudeBGColor = ContextCompat.getColor(getContext(), R.color.magnitude6); break;
            case 7: amplitudeBGColor = ContextCompat.getColor(getContext(), R.color.magnitude7); break;
            case 8: amplitudeBGColor = ContextCompat.getColor(getContext(), R.color.magnitude8); break;
            case 9: amplitudeBGColor = ContextCompat.getColor(getContext(), R.color.magnitude9); break;
            case 10: amplitudeBGColor = ContextCompat.getColor(getContext(), R.color.magnitude10plus); break;
        }
        amplitudeCircle.setColor(amplitudeBGColor);

        return listItemView;
    }
}
