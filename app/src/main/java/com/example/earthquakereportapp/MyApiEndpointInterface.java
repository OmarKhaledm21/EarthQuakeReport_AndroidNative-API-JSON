package com.example.earthquakereportapp;


import com.example.earthquakereportapp.Models.EarthQuakeJsonModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyApiEndpointInterface {
    //query?format=geojson&starttime=2014-01-01&endtime=2014-01-02&minmagnitude=5
    @GET("query")
    Call<EarthQuakeJsonModel> getEarthQuakes(@Query("format") String format,
                                             @Query("minmagnitude")Double minmagnitude,
                                             @Query("limit")Integer limit);
}
