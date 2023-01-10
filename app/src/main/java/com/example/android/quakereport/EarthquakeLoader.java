package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class EarthquakeLoader extends AsyncTaskLoader {
    String murl;

    public EarthquakeLoader(Context context, String USGS_REQUEST_URL) {
        super(context);
         murl = USGS_REQUEST_URL;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {
        Log.e("loadinBackground", "Background thread is running");
        ArrayList <Earthquake> result;
        result = QueryUtils.fetchEarthquakeData(murl);
        return result;
    }
}
