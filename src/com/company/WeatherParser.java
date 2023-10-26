package com.company;

import com.company.models.WeatherInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeatherParser {


    public List<WeatherInfo> parse(StringBuilder responseContent) throws IOException {
        List<WeatherInfo> list = new ArrayList<>();

        String date;
        String weather;
        String name;
        String city;
        float tempC;
        String maxtemp;
        String mintemp;

        JsonObject json = JsonParser.parseString(responseContent.toString()).getAsJsonObject();
        JsonObject forecast = json.getAsJsonObject("forecast");
        JsonArray forecastArray = forecast.getAsJsonArray("forecastday");
        JsonObject location = json.getAsJsonObject("location");


        for (int i = 0; i < forecastArray.size(); i++) {
            List<WeatherInfo.Weatherinfoperhour> listPH = new ArrayList<>();

            JsonObject forecastGet = forecastArray.get(i).getAsJsonObject();
            JsonArray array = forecastGet.getAsJsonArray("hour");
            JsonObject day = forecastGet.getAsJsonObject("day");
            JsonObject condition = day.getAsJsonObject("condition");


            date = forecastGet.get("date").getAsString();
            weather = condition.get("text").getAsString();
            tempC = day.get("avgtemp_c").getAsFloat();
            city = location.get("name").getAsString();
            name = location.get("country").getAsString();
            maxtemp = day.get("maxtemp_c").getAsString();
            mintemp = day.get("mintemp_c").getAsString();

            for (int j = 0; j <= 7; j++) {
                JsonObject hours = array.get(j * 3).getAsJsonObject();
                JsonObject condition2 = hours.getAsJsonObject("condition");

                String tempc = hours.get("temp_c").getAsString();
                String hour = (hours.get("time").getAsString()).substring((hours.get("time").getAsString()).lastIndexOf(" ") + 1);
                String url = "https:" + condition2.get("icon").getAsString();
                String feelslike = hours.get("feelslike_c").getAsString();
                String chance = hours.get("chance_of_rain").getAsString();
                String pressure = String.valueOf(hours.get("pressure_mb").getAsInt());
                String wind = String.valueOf(hours.get("wind_kph"));
                String wing = wind.substring(0,wind.lastIndexOf(".")+1);
                String humidity = hours.get("humidity").getAsString();

                listPH.add(new WeatherInfo.Weatherinfoperhour(hour, url, tempc, feelslike, pressure, humidity, wind, chance));
            }
            list.add(new WeatherInfo(maxtemp, mintemp, date, weather, tempC, name, city, listPH));
        }
        return list;
    }
}
