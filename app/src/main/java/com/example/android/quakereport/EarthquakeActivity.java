/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    TextView emptyView;
    ProgressBar loading_spinner;
    TextView noInternet;
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        noInternet = (TextView) findViewById(R.id.no_internet);
        loading_spinner = (ProgressBar) findViewById(R.id.loading_spinner);

        /** Check if the user has internet conncetion**/
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            // Perform the HTTP request for earthquake data and process the response.
            getLoaderManager().initLoader(0,null,this).forceLoad();
            Log.e("onCreate", "get loader manager. initiate loader");
        }
        else {
            loading_spinner.setVisibility(View.GONE);
            noInternet.setText("No Internet Connection");
        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent settingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUi(Object earthquakes){

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
         emptyView = (TextView) findViewById(R.id.empty_layout);
         loading_spinner = (ProgressBar) findViewById(R.id.loading_spinner);


        // Create a new {@link ArrayAdapter} of earthquakes
         final QuakeAdapter adapter = new QuakeAdapter(this, (ArrayList<Earthquake>) earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
        earthquakeListView.setEmptyView(emptyView);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake clicked_earthquake = adapter.getItem(position);
                String url = clicked_earthquake.getUrl();
                Intent openWeb = new Intent(Intent.ACTION_VIEW);
                openWeb.setData(Uri.parse(url));
                startActivity(openWeb);
            }
        };

        earthquakeListView.setOnItemClickListener(itemClickListener);

    }


    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this,uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader loader, Object data) {

        updateUi(data);
//        or do adapter.addAll(data); to send the earthquake list to the adapter
        emptyView.setText(R.string.no_earthquakes);
        loading_spinner.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);
        Log.e("onLoadFinished", "Loader is finished");
        ;
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.e("onLoaderReset", "Loader has reset");
        updateUi(new ArrayList<Earthquake>());
    }


}
