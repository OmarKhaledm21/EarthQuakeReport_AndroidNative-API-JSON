package com.projects.earthquakeactivity;

import static com.projects.earthquakeactivity.EarthquakeActivity.LOG_TAG;

import android.util.Log;

import com.projects.earthquakeactivity.Model.Earthquake;

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
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QueryUtils {

    public static ArrayList<Earthquake> parseJSON(String jsonString){
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONArray featuresArray = root.getJSONArray("features");
            int n = featuresArray.length();
            for(int i=0; i<n; i++){
                final JSONObject featureObject = featuresArray.getJSONObject(i);
                final JSONObject prop = featureObject.getJSONObject("properties");

                earthquakes.add(new Earthquake(
                        prop.getDouble("mag"),
                        prop.getString("place"),
                        prop.getLong("time"),
                        prop.getString("url")
                ));

            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return earthquakes;
    }

    public static URL createURL(String data_url) {
        URL url = null;
        try {
            url = new URL(data_url);
        } catch (MalformedURLException exception) {
            Log.v(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.v(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.v(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                result.append(line);
                line = bufferedReader.readLine();
            }
        }
        return result.toString();
    }


}
