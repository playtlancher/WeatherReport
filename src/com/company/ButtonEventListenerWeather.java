package com.company;

import com.company.models.WeatherInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ButtonEventListenerWeather implements ActionListener {

    private final WeatherInfo weatherInfo;
    private int count = -1;
    JFrame frame;
    List<JButton> createdButtons;
    List<JLabel> createdLabel;

    public void removeLabels() {
        for (JLabel label : createdLabel) {

            frame.getContentPane().remove(label);
        }
        createdLabel.clear();
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        removeLabels();

        for (JButton b : createdButtons) {
            b.setEnabled(true);
        }

        if (count != -1) {
            createdButtons.get(count).setEnabled(false);
        }
        List<String> labelText = new ArrayList<>();
        labelText.add("Temperature");
        labelText.add("Feels like");
        labelText.add("pressure");
        labelText.add("Humidity");
        labelText.add("wind(m/s)");
        labelText.add("chance of rain");

        Font font = new Font("Times new roman", Font.BOLD, 100);
        Font font2 = new Font("Times new roman", Font.BOLD, 15);

        JLabel date = new JLabel(weatherInfo.date.substring(8, 10));
        JLabel maxTemp = new JLabel("Max temp :" + weatherInfo.maxTemp);
        JLabel minTemp = new JLabel("Min temp :" + weatherInfo.minTemp);
        LocalDate local = LocalDate.parse(weatherInfo.date);
        JLabel day = new JLabel(String.valueOf(local.getDayOfWeek()));
        JLabel month = new JLabel(String.valueOf(local.getMonth()));

        maxTemp.setFont(font2);
        minTemp.setFont(font2);
        date.setFont(font);
        day.setFont(font2);
        month.setFont(font2);

        createdLabel.add(date);
        createdLabel.add(maxTemp);
        createdLabel.add(minTemp);
        createdLabel.add(day);
        createdLabel.add(month);

        frame.add(date);
        frame.add(maxTemp);
        frame.add(minTemp);
        frame.add(day);
        frame.add(month);

        date.setBounds(50, 180, 100, 140);
        minTemp.setBounds(60, 300, 120, 40);
        maxTemp.setBounds(60, 320, 120, 40);
        day.setBounds(60, 170, 120, 40);
        month.setBounds(60, 280, 120, 40);

        for (int j = 0; j < 6; j++) {
            JLabel label3 = new JLabel(labelText.get(j));
            label3.setBounds(180, 230 + j * 30, 100, 30);
            createdLabel.add(label3);
            frame.add(label3);
        }
        for (int i = 0; i <= 7; i++) {

            try {


                URL url = new URL(weatherInfo.weatherInfoPerHours.get(i).URL);
                ImageIcon icon = new ImageIcon(url);
                JLabel textHour = new JLabel(weatherInfo.weatherInfoPerHours.get(i).hour);
                JLabel iconLabel = new JLabel(icon);
                List<String> weatherDetails = weatherInfo.weatherInfoPerHours.get(i).getListOfWeatherDetails();
                for (int j = 0; j < 6; j++) {
                    JLabel label3 = new JLabel(weatherDetails.get(j));
                    label3.setBounds(300 + i * 50, 230 + j * 30, 40, 30);
                    createdLabel.add(label3);
                    frame.getContentPane().add(label3);
                }

                textHour.setBounds(300 + i * 50, 200, 40, 30);
                iconLabel.setBounds(280 + i * 50, 150, 64, 64);


                createdLabel.add(textHour);
                createdLabel.add(iconLabel);

                frame.add(textHour);
                frame.add(iconLabel);


            } catch (IOException ex) {
                System.out.println("error");
            }
        }
        frame.revalidate();
        frame.repaint();
    }

    public ButtonEventListenerWeather(JFrame jFrame, List<JButton> buttons, List<JLabel> labels, WeatherInfo weatherInfo, int count) {
        createdButtons = buttons;
        createdLabel = labels;
        frame = jFrame;
        this.weatherInfo = weatherInfo;
        this.count = count;
    }

    public ButtonEventListenerWeather(JFrame jFrame, List<JButton> buttons, List<JLabel> labels, WeatherInfo weatherInfo) {
        createdLabel = labels;
        createdButtons = buttons;
        frame = jFrame;
        this.weatherInfo = weatherInfo;
    }

}
