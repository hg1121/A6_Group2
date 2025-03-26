package com.example.a6_group2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import java.io.IOException;



public class WeatherActivity extends AppCompatActivity {
    private EditText cityInput;
    private RecyclerView weatherRecyclerView;
    private WeatherAdapter weatherAdapter;
    private TextView weatherLoadingText;
    private static final String WEATHER_API_KEY = "6af456e54cbb3ee9b606557b2376824f";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private Handler handler = new Handler();
    private int dotCount = 0;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityInput = findViewById(R.id.city_input);
        Button getWeatherBtn = findViewById(R.id.get_weather_btn);
        weatherRecyclerView = findViewById(R.id.weather_recycler_view);
        weatherLoadingText = findViewById(R.id.weather_loading_text);

        // Setup RecyclerView
        weatherAdapter = new WeatherAdapter();
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weatherRecyclerView.setAdapter(weatherAdapter);

        // Add logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);

        getWeatherBtn.setOnClickListener(v -> {
            String city = cityInput.getText().toString().trim();
            if (city.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                return;
            }

            startLoadingAnimation();

            // Log the API call details
            Log.d("WeatherAPI", "Making API call for city: " + city);
            Log.d("WeatherAPI", "Using API key: " + WEATHER_API_KEY);

            Call<Weather> call = weatherApi.getCurrentWeather(city, WEATHER_API_KEY, "metric");
            call.enqueue(new Callback<Weather>() {
                @Override
                public void onResponse(Call<Weather> call, Response<Weather> response) {
                    stopLoadingAnimation();
                    if (response.isSuccessful() && response.body() != null) {
                        Weather weather = response.body();
                        weatherAdapter.setWeatherData(weather);
                        Log.d("WeatherAPI", "Success: " + weather.getCityName());
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("WeatherAPI", "Error body: " + errorBody);
                            Toast.makeText(WeatherActivity.this,
                                "Error: " + errorBody, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Log.e("WeatherAPI", "Error reading error body", e);
                            Toast.makeText(WeatherActivity.this,
                                "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Weather> call, Throwable t) {
                    Log.e("WeatherAPI", "Network error", t);
                    Toast.makeText(WeatherActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void startLoadingAnimation() {
        isLoading = true;
        weatherLoadingText.setVisibility(View.VISIBLE);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!isLoading) return;

                StringBuilder dots = new StringBuilder();
                for (int i = 0; i < dotCount; i++) dots.append(".");
                weatherLoadingText.setText("Loading" + dots);
                dotCount = (dotCount + 1) % 4;

                handler.postDelayed(this, 500);
            }
        });
    }

    private void stopLoadingAnimation() {
        isLoading = false;
        weatherLoadingText.setVisibility(View.GONE);
    }
}