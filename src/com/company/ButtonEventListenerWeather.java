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

    private final WeatherInfo o;
    private int count = -1;
    JFrame frame;
    List<JButton> createdButtons;
    List<JLabel> createdLabel;

    public void clearWindow() {
        for (JLabel label : createdLabel) {

            frame.getContentPane().remove(label);
        }
        createdLabel.clear();
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        clearWindow();

        for (JButton b : createdButtons) {
            b.setEnabled(true);
        }
        if (count != -1) {
            createdButtons.get(count).setEnabled(false);
        }
        List<String> text = new ArrayList<>();
        text.add("Temperature");
        text.add("Feels like");
        text.add("pressure");
        text.add("Humidity");
        text.add("wind(m/s)");
        text.add("chance of rain");

        Font font = new Font("Times new roman", Font.BOLD, 100);
        Font font2 = new Font("Times new roman", Font.BOLD, 15);

        JLabel date = new JLabel(o.date.substring(8, 10));
        JLabel maxtemp = new JLabel("Max temp :" + o.maxtemp);
        JLabel mintemp = new JLabel("Min temp :" + o.mintemp);
        LocalDate local = LocalDate.parse(o.date);
        JLabel day = new JLabel(String.valueOf(local.getDayOfWeek()));
        JLabel month = new JLabel(String.valueOf(local.getMonth()));

        maxtemp.setFont(font2);
        mintemp.setFont(font2);
        date.setFont(font);
        day.setFont(font2);
        month.setFont(font2);

        createdLabel.add(date);
        createdLabel.add(maxtemp);
        createdLabel.add(mintemp);
        createdLabel.add(day);
        createdLabel.add(month);

        frame.getContentPane().add(date);
        frame.getContentPane().add(maxtemp);
        frame.getContentPane().add(mintemp);
        frame.getContentPane().add(day);
        frame.getContentPane().add(month);

        date.setBounds(50, 180, 100, 140);
        mintemp.setBounds(60, 300, 120, 40);
        maxtemp.setBounds(60, 320, 120, 40);
        day.setBounds(60, 170, 120, 40);
        month.setBounds(60, 280, 120, 40);

        for (int j = 0; j < 6; j++) {
            JLabel label3 = new JLabel(text.get(j));
            label3.setBounds(180, 230 + j * 30, 100, 30);
            createdLabel.add(label3);
            frame.getContentPane().add(label3);
        }
        for (int i = 0; i <= 7; i++) {

            try {


                URL url = new URL(o.whp.get(i).URL);
                ImageIcon icon = new ImageIcon(url);
                JLabel textHour = new JLabel(o.whp.get(i).hour);
                JLabel iconLabel = new JLabel(icon);

                for (int j = 0; j < 6; j++) {
                    JLabel label3 = new JLabel(o.whp.get(i).outText(j));
                    label3.setBounds(300 + i * 50, 230 + j * 30, 40, 30);
                    createdLabel.add(label3);
                    frame.getContentPane().add(label3);
                }

                textHour.setBounds(300 + i * 50, 200, 40, 30);
                iconLabel.setBounds(280 + i * 50, 150, 64, 64);


                createdLabel.add(textHour);
                createdLabel.add(iconLabel);

                frame.getContentPane().add(textHour);
                frame.getContentPane().add(iconLabel);


            } catch (IOException ex) {
                System.out.println("error");
            }
        }
        frame.revalidate();
        frame.repaint();
    }

    public ButtonEventListenerWeather(JFrame jFrame, List<JButton> buttons, List<JLabel> labels, WeatherInfo o, int count) {
        createdButtons = buttons;
        createdLabel = labels;
        frame = jFrame;
        this.o = o;
        this.count = count;
    }

    public ButtonEventListenerWeather(JFrame jFrame, List<JButton> buttons, List<JLabel> labels, WeatherInfo o) {
        createdLabel = labels;
        createdButtons = buttons;
        frame = jFrame;
        this.o = o;
    }

}
