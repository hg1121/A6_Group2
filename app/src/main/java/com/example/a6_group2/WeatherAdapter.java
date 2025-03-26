package com.example.a6_group2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private List<Weather> weatherList = new ArrayList<>();

    public void setWeatherData(Weather weather) {
        this.weatherList.clear();
        this.weatherList.add(weather);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        holder.bind(weather);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        private TextView cityName, temperature, description, humidity;
        private ImageView weatherIcon;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.city_name);
            temperature = itemView.findViewById(R.id.temperature);
            description = itemView.findViewById(R.id.weather_description);
            humidity = itemView.findViewById(R.id.humidity);
            weatherIcon = itemView.findViewById(R.id.weather_icon);
        }

        void bind(Weather weather) {
            cityName.setText(weather.getCityName());
            temperature.setText(String.format("%.1f°C", weather.getMain().getTemperature()));
            humidity.setText(String.format("Humidity: %d%%", weather.getMain().getHumidity()));
            
            if (!weather.getWeather().isEmpty()) {
                Weather.WeatherInfo weatherInfo = weather.getWeather().get(0);
                description.setText(weatherInfo.getDescription());
                
                // Load weather icon
                String iconUrl = String.format("https://openweathermap.org/img/w/%s.png", 
                    weatherInfo.getIcon());
                Picasso.get().load(iconUrl).into(weatherIcon);
            }
        }
    }
} 