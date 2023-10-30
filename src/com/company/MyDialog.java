package com.company;

import javax.swing.*;
import java.io.*;

class MyDialog extends JDialog {
    public MyDialog() throws IOException {
        File file = new File("Settings.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        WeatherParser parser = new WeatherParser();

        String readLine;
        String city = "";

        while ((readLine = bufferedReader.readLine()) != null) {
            if (readLine.contains("City")) {
                city = readLine.substring(6);
                bufferedReader.close();
                break;
            }
        }

        JButton button = new JButton("Change city");
        TextField changeCityField = new TextField();
        JLabel cityNow = new JLabel("City now:");
        JLabel cityNowText = new JLabel(city);

        changeCityField.setLabelText("City");

        button.setBounds(50, 110, 120, 30);
        changeCityField.setBounds(50, 50, 80, 50);
        cityNow.setBounds(50, 20, 100, 30);
        cityNowText.setBounds(105, 20, 100, 30);


        button.addActionListener(e -> {
            String city1 = changeCityField.getText();
            while (true) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    parser.parse(LinkBuilder.getParameters(city1, "1", "3dd7434243bb4e06933153229230509"));
                    cityNowText.setText(city1);
                    city1 = "City: " + city1;
                    writer.write(city1);
                    break;

                } catch (IOException ex) {
                    JTextField text = new JTextField("Таке місто не існує");
                    text.setBounds(50, 150, 120, 30);
                    add(text);
                    break;
                }
            }
        });

        setLayout(null);

        add(cityNow);

        add(button);

        add(changeCityField);

        add(cityNowText);

        setBounds(500, 500, 300, 300);

        repaint();
    }
}