package com.company;


import javax.swing.*;
import java.awt.*;
import java.io.*;

class SettingsDialog extends JDialog {

    File file = new File("Settings.txt");

    public SettingsDialog() {
    }

    public SettingsDialog(Image settingsImage) {

        String startCity = readCity();

        setTitle("Settings");

        setIconImage(settingsImage);

        WeatherParser parser = new WeatherParser();


        //Create stuff and set stuff position
        JButton changeCityButton = new JButton("Change city");
        TextField changeCityField = new TextField();
        JLabel cityNow = new JLabel("City now:");
        JLabel cityNowText = new JLabel(startCity);
        JLabel errorLabel = new JLabel("Таке місто не існує");

        changeCityField.setLabelText("City");

        changeCityButton.setBounds(50, 110, 120, 30);
        changeCityField.setBounds(50, 50, 80, 50);
        cityNow.setBounds(50, 20, 100, 30);
        cityNowText.setBounds(105, 20, 100, 30);
        errorLabel.setBounds(50, 150, 120, 30);

        //Button event
        changeCityButton.addActionListener(e -> {
            String newCity = changeCityField.getText();

            while (true) {

                boolean check = CityChecker.check(LinkBuilder.buildLink(newCity, "1", "3dd7434243bb4e06933153229230509"));
                if (check){break;}

                cityNowText.setText(newCity);
                newCity = "City: " + newCity;

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(newCity);
                } catch (IOException ignored) {}
                break;
            }
        });

        setLayout(null);

        add(cityNow);

        add(changeCityButton);

        add(changeCityField);

        add(cityNowText);

        setBounds(500, 500, 300, 300);

        repaint();
    }
    public String readCity(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String readLine;

            while ((readLine = bufferedReader.readLine()) != null) {
                if (readLine.contains("City")) {
                    readLine = readLine.substring(6);
                    bufferedReader.close();
                    break;
                }
            }
            return readLine;
        }catch (IOException ignored){}
        return null;
    }
}