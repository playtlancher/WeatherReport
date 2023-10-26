package com.company.models;

import java.util.ArrayList;
import java.util.List;

public class WeatherInfo {
    public String date;
    public String weather;
    public float tempC;

    public String name;
    public String city;
    public String maxtemp;
    public String mintemp;
    public List<Weatherinfoperhour> whp = new ArrayList<>();

    public WeatherInfo(String maxtemp,String mintemp,String date, String weather, float tempC, String name, String city, List<Weatherinfoperhour> list) {
        this.maxtemp = maxtemp;
        this.mintemp = mintemp;
        this.date = date;
        this.weather = weather;
        this.tempC = tempC;
        this.name = name;
        this.city = city;
        this.whp = list;
    }
    public WeatherInfo() {
    }

    public static class Weatherinfoperhour {
        public String hour;
        public String URL;
        public String tempC;
        public String feelslike;
        public String pressure;
        public String humidity;
        public String wind;
        public String chan;

        List <String> allText = new ArrayList<>();

        public Weatherinfoperhour(String hour, String URL, String tempC, String feelslike, String pressure, String humidity, String wind, String chan) {
            this.hour = hour;
            this.URL = URL;
            this.tempC = tempC;
            this.feelslike = feelslike;
            this.pressure = pressure;
            this.humidity = humidity;
            this.wind = wind;
            this.chan = chan;
            allText.add(tempC);
            allText.add(feelslike);
            allText.add(pressure);
            allText.add(humidity);
            allText.add(wind);
            allText.add(chan);
        }
        public String outText(int i){
            return allText.get(i);
        }
    }
}
