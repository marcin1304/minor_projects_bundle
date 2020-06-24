package com.example.networking;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AsteroidApiData {
    private final Integer elementCount;
    private final Map<String, ArrayList<Asteroid>> dateAsteroidsMap;

    public Integer getElementCount() {
        return elementCount;
    }

    public Map<String, ArrayList<Asteroid>> getDateAsteroidsMap() {
        return dateAsteroidsMap;
    }

    public AsteroidApiData(String startDate, String endDate) throws IOException {
        Map<String, Object> rawJsonMap = getRawJsonMap(startDate, endDate);
        elementCount = initializeElementCount(rawJsonMap);
        dateAsteroidsMap = initializeDateAsteroidsMap(rawJsonMap);
    }

    private Map<String, Object> getRawJsonMap(String startDate, String endDate) throws IOException {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DataNasaApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        NasaAsteroidService service = retrofit.create(NasaAsteroidService.class);
        Call<Map<String, Object>> callSync = service.getAsteroidCollection(startDate, endDate, DataNasaApi.KEY);
        Response<Map<String, Object>> response = callSync.execute();

        return response.body();
    }

    private Integer initializeElementCount(Map<String, Object> rawJsonMap) {
        return ((Double) rawJsonMap.get("element_count")).intValue();
    }

    private Map<String, ArrayList<Asteroid>> initializeDateAsteroidsMap(Map<String, Object> rawJsonMap) {
        Map<String, Object> nearEarthObjects = (Map<String, Object>) rawJsonMap.get("near_earth_objects");
        Map<String, ArrayList<Asteroid>> localDateAsteroidsMap = new HashMap<>();
        nearEarthObjects.forEach((k, v) -> {
            ArrayList<Asteroid> localAsteroids = new ArrayList<>();
            ((ArrayList<Object>) v).forEach(e -> localAsteroids.add(new Asteroid((Map<String, Object>) e)));
            localDateAsteroidsMap.put(k, localAsteroids);
        }
        );
        return localDateAsteroidsMap;
    }
}
