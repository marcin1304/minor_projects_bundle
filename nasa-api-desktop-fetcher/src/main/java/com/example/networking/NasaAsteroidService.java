package com.example.networking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.Map;

public interface NasaAsteroidService {
    @GET("/neo/rest/v1/feed")
    public Call<Map<String, Object>> getAsteroidCollection(
            @Query("start_date") String startDate,
            @Query("end_date") String endDate,
            @Query("api_key") String apiKey);
}
