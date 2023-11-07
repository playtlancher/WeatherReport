package com.company;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;


public class CityChecker {
    public static boolean check(StringBuilder responseContent){
        try {
            JsonObject json = JsonParser.parseString(responseContent.toString()).getAsJsonObject();
        }catch (Exception ex){
            String message = "Таке місто не існує";
            JOptionPane.showMessageDialog(null, message, "Output", JOptionPane.PLAIN_MESSAGE);
            return true;
        }
        return false;
    }
}
