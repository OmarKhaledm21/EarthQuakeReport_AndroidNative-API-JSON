package com.projects.earthquakeactivity;

import com.projects.earthquakeactivity.Model.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


}
