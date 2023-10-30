package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LinkBuilder {

    public static StringBuilder getParameters(String city, String timeKey, String key) throws IOException {
        String link = "https://api.weatherapi.com/v1";
        StringBuilder responseContent = new StringBuilder();
        String readLine;
        city = city.replace(" ", "");
        link += "/forecast.json" + "?key=" + key + "&q=" + city + "&days=" + timeKey + "&lang=uk";
        URL url = new URL(link);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        while ((readLine = reader.readLine()) != null) {
            responseContent.append(readLine);
        }
        return responseContent;
    }
}
