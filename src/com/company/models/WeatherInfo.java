package com.company.models;

import java.util.ArrayList;
import java.util.List;

public class WeatherInfo {
    public String date;
    public String weather;
    public float tempC;
    public String maxTemp;
    public String minTemp;
    public List<WeatherInfoPerHour> weatherInfoPerHours = new ArrayList<>();

    public WeatherInfo(String maxtemp,String mintemp,String date, String weather, float tempC, List<WeatherInfoPerHour> list) {
        this.maxTemp = maxtemp;
        this.minTemp = mintemp;
        this.date = date;
        this.weather = weather;
        this.tempC = tempC;
        this.weatherInfoPerHours = list;
    }
    public WeatherInfo() {
    }
}
