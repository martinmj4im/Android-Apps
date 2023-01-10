package com.example.android.quakereport;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.graphics.drawable.GradientDrawable;

public class QuakeAdapter extends ArrayAdapter<Earthquake> {

    public QuakeAdapter(Activity context, ArrayList<Earthquake> earthquakes){
        super(context,0, earthquakes);
    }

    @NonNull
    @Override
    public  View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         if (convertView == null){
             convertView = LayoutInflater.from(getContext()).inflate
                     (R.layout.list_item,parent,false);
         }

         Earthquake current_quake = getItem(position);

        double current_mag = current_quake.getMag();
        DecimalFormat formatter = new DecimalFormat("0.0");
        String output_mag = formatter.format(current_mag);

        TextView mag = (TextView) convertView.findViewById(R.id.mag);
        mag.setText(output_mag);

        GradientDrawable magCircle = (GradientDrawable) mag.getBackground();
        int magColor = getMagColor(current_quake.getMag());
        magCircle.setColor(magColor);


        String[] parts;
        String locationOffset;
        String primaryLocation;

        String fullLocation = current_quake.getLocation();
        if (fullLocation.contains("of")){
             parts = fullLocation.split("(?<=of )");
             locationOffset = parts[0];
             primaryLocation = parts[1];}
        else {
            locationOffset = "Near the";
            primaryLocation = fullLocation;
        }


        TextView location = (TextView) convertView.findViewById(R.id.locationOffset);
        location.setText(locationOffset);

        TextView Location = (TextView) convertView.findViewById(R.id.Primarylocation);
        Location.setText(primaryLocation);

        Date dateObject = new Date(current_quake.gettimeInMilli());
        String formattedDate = formatDate(dateObject);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        date.setText(formattedDate);

        Date timeObject = new Date(current_quake.gettimeInMilli());
        String formattedTime = formatTime(timeObject);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.time);
        timeTextView.setText(formattedTime);


        return convertView;
    }

    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date timeObject){
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        return timeFormat.format(timeObject);
    }

    private int getMagColor(double mag){
        int magColor;
       int formatted_mag = (int) Math.floor(mag);
        switch (formatted_mag){
            case 0:
            case 1:
                magColor = R.color.magnitude1;
                break;
            case 2:
                magColor = R.color.magnitude2;
                break;
            case 3:
                magColor = R.color.magnitude3;
                break;
            case 4:
                magColor = R.color.magnitude4;
                break;
            case 5:
                magColor = R.color.magnitude5;
                break;
            case 6:
                magColor = R.color.magnitude6;
                break;
            case 7:
                magColor = R.color.magnitude7;
                break;
            case 8:
                magColor = R.color.magnitude8;
                break;
            case 9:
                magColor = R.color.magnitude9;
                break;
            default:
                magColor = R.color.magnitude10plus;
        }

    return ContextCompat.getColor(getContext(),magColor);
    }
}
