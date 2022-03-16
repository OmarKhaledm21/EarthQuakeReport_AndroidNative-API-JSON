package com.projects.earthquakeactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.projects.earthquakeactivity.Model.Earthquake;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
    public  final String DATA_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=25";

    public final static String LOG_TAG = "EQA_RES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        Bundle bundle = new Bundle();
        bundle.putString("URL", DATA_URL);
        getSupportLoaderManager().initLoader(1, bundle, this).forceLoad();

    }

    public void updateUI(ArrayList<Earthquake> earthquakes) {
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);

        earthquakeListView.setAdapter(adapter);
    }

    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        String url=null;
        if(args!=null){
            url = args.getString("URL");

        }
        return new EarthquakeAsyncTaskLoader(this,url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> data) {
        if (data != null) {
            updateUI((ArrayList<Earthquake>) data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {

    }
}

class EarthquakeAsyncTaskLoader extends AsyncTaskLoader<List<Earthquake>> {
    private String url;

    public EarthquakeAsyncTaskLoader(@NonNull Context context,String url) {
        super(context);
        this.url = url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        List<Earthquake> list = null;
        try {
            URL url = QueryUtils.createURL(this.url);
            String response = QueryUtils.makeHttpRequest(url);
            list = QueryUtils.parseJSON(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
