package com.example.networking;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NasaPhotoService {
    @GET("planetary/apod?api_key=" + DataNasaApi.KEY)
    public Call<NasaPhoto> getPhotoUrl();
}