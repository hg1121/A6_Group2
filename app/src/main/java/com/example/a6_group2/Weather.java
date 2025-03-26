package com.example.a6_group2;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Weather {
    public static class WeatherMain {
        @SerializedName("temp")
        private double temperature;
        @SerializedName("feels_like")
        private double feelsLike;
        @SerializedName("humidity")
        private int humidity;
        @SerializedName("pressure")
        private int pressure;

        public double getTemperature() { return temperature; }
        public double getFeelsLike() { return feelsLike; }
        public int getHumidity() { return humidity; }
        public int getPressure() { return pressure; }
    }

    public static class WeatherInfo {
        @SerializedName("main")
        private String main;
        @SerializedName("description")
        private String description;
        @SerializedName("icon")
        private String icon;

        public String getMain() { return main; }
        public String getDescription() { return description; }
        public String getIcon() { return icon; }
    }

    @SerializedName("main")
    private WeatherMain main;
    
    @SerializedName("weather")
    private List<WeatherInfo> weather;
    
    @SerializedName("name")
    private String cityName;

    public WeatherMain getMain() { return main; }
    public List<WeatherInfo> getWeather() { return weather; }
    public String getCityName() { return cityName; }
}