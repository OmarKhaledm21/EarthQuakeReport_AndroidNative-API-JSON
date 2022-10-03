package com.example.earthquakereportapp;


import android.net.DnsResolver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.earthquakereportapp.Models.EarthQuakeJsonModel;
import com.example.earthquakereportapp.Models.Earthquake;
import com.example.earthquakereportapp.Models.Feature;
import com.example.earthquakereportapp.Models.Properties;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        TextView emptyView = (TextView) findViewById(R.id.empty_view);
        emptyView.setText(R.string.no_earthquakes);
        earthquakeListView.setEmptyView(emptyView);
        getData();

    }

    public void updateUI(ArrayList<Earthquake> earthquakes) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb);
        progressBar.setVisibility(View.GONE);
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);

        earthquakeListView.setAdapter(adapter);
    }

    public void getData() {
        Log.v("TRIAL", "START");
        final String BASE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApiEndpointInterface apiService =
                retrofit.create(MyApiEndpointInterface.class);
        //query?format=geojson&starttime=2014-01-01&endtime=2014-01-02&limit=2
        Call<EarthQuakeJsonModel> call = apiService.getEarthQuakes("geojson", 4.9, 12);
        call.enqueue(new Callback<EarthQuakeJsonModel>() {
            @Override
            public void onResponse(Call<EarthQuakeJsonModel> call, Response<EarthQuakeJsonModel> response) {
                int statusCode = response.code();
                EarthQuakeJsonModel example = response.body();
                Log.v("TRIAL", response.toString());
                ArrayList<Earthquake> earthquakes = new ArrayList<>();
                List<Feature> features = example.getFeatures();
                for (int i = 0; i < features.size(); i++) {
                    Properties properties = features.get(i).getProperties();
                    Earthquake earthquake = new Earthquake();
                    earthquake.setMagnitude(properties.getMag());
                    earthquake.setUrl(properties.getUrl());
                    earthquake.setLocation(properties.getPlace());
                    earthquake.setTimeInMilliseconds(properties.getTime());

                    earthquakes.add(earthquake);
                }

                Log.v("TRIAL", "DONE");

                updateUI(earthquakes);

            }

            @Override
            public void onFailure(Call<EarthQuakeJsonModel> call, Throwable t) {
                // Log error here since request failed
                Log.v("TRIAL", "ERROR");
            }
        });

    }

}