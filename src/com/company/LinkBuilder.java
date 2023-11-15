package com.company;

public class LinkBuilder {

    public static String buildLink(String city, String timeKey, String key){
        String link = "https://api.weatherapi.com/v1";
        String readLine;
        city = city.replace(" ", "");
        link += "/forecast.json" + "?key=" + key + "&q=" + city + "&days=" + timeKey + "&lang=uk";

        return link;
    }
}
