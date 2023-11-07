package com.company;

import com.company.models.WeatherInfo;
import com.company.models.WeatherInfoPerHour;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class WeatherParser {


    public List<WeatherInfo> parseWeather(StringBuilder responseContent) {
        List<WeatherInfo> weatherList = new ArrayList<>();

        List<WeatherInfoPerHour> weatherDetails;

        String date;
        String weather;
        float tempC;
        String maxtemp;
        String mintemp;

        try {
            JsonObject json = JsonParser.parseString(responseContent.toString()).getAsJsonObject();
            JsonObject forecast = json.getAsJsonObject("forecast");
            JsonArray forecastArray = forecast.getAsJsonArray("forecastday");

            for (int i = 0; i < forecastArray.size(); i++) {

                JsonObject forecastGet = forecastArray.get(i).getAsJsonObject();
                JsonArray hours = forecastGet.getAsJsonArray("hour");
                JsonObject day = forecastGet.getAsJsonObject("day");
                JsonObject condition = day.getAsJsonObject("condition");


                date = forecastGet.get("date").getAsString();
                weather = condition.get("text").getAsString();
                tempC = day.get("avgtemp_c").getAsFloat();
                maxtemp = day.get("maxtemp_c").getAsString();
                mintemp = day.get("mintemp_c").getAsString();

                weatherDetails = parseWeatherPerEvery3Hour(hours);
;
                weatherList.add(new WeatherInfo(maxtemp, mintemp, date, weather, tempC, weatherDetails));
            }

        }catch (Exception ex) {
            String message = "Таке місто не існує";
            JOptionPane.showMessageDialog(null, message, "Output", JOptionPane.PLAIN_MESSAGE);
        }
        return weatherList;
    }


    public List<WeatherInfoPerHour> parseWeatherPerEvery3Hour(JsonArray array){
        List<WeatherInfoPerHour> weatherPerHourList = new ArrayList<>();

        for (int j = 1; j <= 24; j+=2) {

            JsonObject hours = array.get(j).getAsJsonObject();
            JsonObject condition2 = hours.getAsJsonObject("condition");

            String tempc = hours.get("temp_c").getAsString();
            String hour = (hours.get("time").getAsString()).substring((hours.get("time").getAsString()).lastIndexOf(" ") + 1);
            String url = "https:" + condition2.get("icon").getAsString();
            String feelslike = hours.get("feelslike_c").getAsString();
            String chance = hours.get("chance_of_rain").getAsString();
            String pressure = String.valueOf(hours.get("pressure_mb").getAsInt());
            String wind = String.valueOf(hours.get("wind_kph"));
            wind = wind.substring(0, wind.lastIndexOf("."));
            String humidity = hours.get("humidity").getAsString();
            weatherPerHourList.add(new WeatherInfoPerHour(hour, url, tempc, feelslike, pressure, humidity, wind, chance));
        }

        return weatherPerHourList;
    }
}
