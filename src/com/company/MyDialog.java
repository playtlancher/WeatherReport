package com.company;

import javax.swing.*;
import java.io.*;

class MyDialog extends JDialog {
    public MyDialog() throws IOException {
        File file = new File("Settings.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));


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

        changeCityField.setLabelText("Enter city");

        button.setBounds(50, 110, 120, 30);
        changeCityField.setBounds(50, 50, 80, 50);
        cityNow.setBounds(50, 20, 100, 30);
        cityNowText.setBounds(105, 20, 100, 30);


        button.addActionListener(e -> {
            String city1 = changeCityField.getText();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                city1 = "City: " + city1;
                writer.write(city1);
                dispose();

            } catch (IOException ex) {
                String message = "Таке місто не існує";
                JOptionPane.showMessageDialog(null, message, "Output", JOptionPane.PLAIN_MESSAGE);
            }
        });

        setLayout(null);

        add(cityNow);

        add(button);

        add(changeCityField);

        add(cityNowText);

        setBounds(500, 500, 200, 200);

    }
}