package com.example.a6_group2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("weather")
    Call<Weather> getCurrentWeather(
        @Query("q") String city,
        @Query("appid") String apiKey,
        @Query("units") String units
    );
} 