package com.company.models;

import java.util.ArrayList;
import java.util.List;

public class WeatherInfoPerHour {
    public String hour;
    public String URL;
    public String tempC;
    public String feelsLike;
    public String pressure;
    public String humidity;
    public String wind;
    public String chan;

    List<String> weatherDetails = new ArrayList<>();

    public WeatherInfoPerHour(String hour, String URL, String tempC, String feelsLike, String pressure, String humidity, String wind, String chan) {
        this.hour = hour;
        this.URL = URL;
        this.tempC = tempC;
        this.feelsLike = feelsLike;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind = wind;
        this.chan = chan;
        weatherDetails.add(tempC);
        weatherDetails.add(feelsLike);
        weatherDetails.add(pressure);
        weatherDetails.add(humidity);
        weatherDetails.add(wind);
        weatherDetails.add(chan);
    }
    public List<String> getListOfWeatherDetails(){
        return weatherDetails;
    }
}