package com.company;

import com.company.models.WeatherInfo;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;
import java.time.LocalDate;


public class SimpleWeatherGUI extends JFrame {
    private final JButton button = new JButton("Weather Report");
    private final TextField inputCity = new TextField();
    private final TextField inputDays = new TextField();
    Font fontCity = new Font("Times new roman", Font.BOLD, 80);
    List<JButton> createdButtons = new ArrayList<>();
    List<JLabel> createdLabel = new ArrayList<>();

    public SimpleWeatherGUI() throws IOException {

        super("SimpleWeatherGUI");
        URL imageUrl = new URL("https://cdn.weatherapi.com/weather/64x64/day/113.png");
        ImageIcon image = new ImageIcon(imageUrl);
        this.setIconImage(image.getImage());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        inputCity.setLabelText("City");
        inputDays.setLabelText("Days");
        File file = new File("Settings.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        WeatherParser parser = new WeatherParser();

        String readLine;
        String city = "";

        while ((readLine = bufferedReader.readLine()) != null) {
            if (readLine.contains("City")) {
                city = readLine.substring(6);
                break;
            }
        }

        List<WeatherInfo> objects = parser.parse(LinkBuilder.getParameters(city, "1", "3dd7434243bb4e06933153229230509"));
        ButtonEventListenerWeather belw = new ButtonEventListenerWeather(objects.get(0));

        belw.actionPerformed(null);

        URL url = new URL("https://th.bing.com/th/id/OIP.bcoOPeHwEPWzLWzNQjJlrAHaHa?pid=ImgDet&rs=1");
        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newImg);

        JMenuBar jMenuBar = new JMenuBar();
        JMenu menu = new JMenu();
        menu.setIcon(newIcon);
        JMenuItem settings = new JMenuItem("Settings");
        settings.addActionListener(e -> {
            try {
                MyDialog myDialog = new MyDialog();
                myDialog.setVisible(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        menu.add(settings);
        jMenuBar.add(menu);
        this.setJMenuBar(jMenuBar);

        this.setBounds(dimension.width / 2 - 100, dimension.height / 2 - 100, 800, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout());

        final JLabel cityText = new JLabel();
        cityText.setText(city);
        createdLabel.add(cityText);


        inputCity.setBounds(200, 0, 100, 40);
        inputDays.setBounds(400, 0, 100, 40);
        button.setBounds(250, 50, 200, 20);
        cityText.setBounds(250, 80, 200, 70);
        cityText.setFont(fontCity);

        button.addActionListener(new ButtonEventListener());

        container.add(inputCity);
        container.add(inputDays);
        container.add(button);
        container.add(cityText);

        setLayout(null);
    }

    class ButtonEventListener implements ActionListener {
        String key = "3dd7434243bb4e06933153229230509";
        WeatherParser weatherParser = new WeatherParser();

        public void actionPerformed(ActionEvent e) {
            clearWindow();
            List<WeatherInfo> list;

            String message;

            String city = inputCity.getText();

            String days = inputDays.getText();

            while (true) {

                try {
                    try {
                        if (Integer.parseInt(days) > 3) {

                            message = "Число повинно бути менше або дорівнювати 3";
                            JOptionPane.showMessageDialog(null, message, "Output", JOptionPane.PLAIN_MESSAGE);
                            break;

                        } else if (Integer.parseInt(days) <= 0) {

                            message = "Число не повинно дорівнювати 0 або бути відємним";
                            JOptionPane.showMessageDialog(null, message, "Output", JOptionPane.PLAIN_MESSAGE);
                            break;

                        }
                    } catch (NumberFormatException ne) {
                        if (days.equals("")) {
                            message = "Поле 'Days' не повинно бути пустим";
                        } else {
                            message = "Ви ввели не число в поле 'Days'";
                        }
                        JOptionPane.showMessageDialog(null, message, "Output", JOptionPane.PLAIN_MESSAGE);
                        break;
                    }
                    list = weatherParser.parse(LinkBuilder.getParameters(city, days, key));
                    for (WeatherInfo o : list) {
                        Button button = new Button(o.date.substring(5, 7) + "-" + o.date.substring(8, 10));
                        createdButtons.add(button);
                        button.addActionListener(new ButtonEventListenerWeather(o, createdButtons.size() - 1));
                        button.setBounds(150 + createdButtons.size() * 80, 80, 80, 20);
                        getContentPane().add(button);
                    }
                    revalidate();
                    repaint();

                } catch (IOException ex) {
                    message = "Таке місто не існує";
                    JOptionPane.showMessageDialog(null, message, "Output", JOptionPane.PLAIN_MESSAGE);
                    break;
                }
                break;
            }
        }

        public void clearWindow() {
            for (JButton jButton : createdButtons) {

                getContentPane().remove(jButton);
            }
            for (JLabel label : createdLabel) {

                getContentPane().remove(label);
            }
            createdLabel.clear();
            createdButtons.clear();
            revalidate();
            repaint();
        }

        public void clearWindow(String l) {
            for (JLabel label : createdLabel) {

                getContentPane().remove(label);
            }
            createdLabel.clear();
            revalidate();
            repaint();
        }
    }

    class ButtonEventListenerWeather implements ActionListener {

        private final WeatherInfo o;
        private int count = -1;

        @Override
        public void actionPerformed(ActionEvent e) {
            ButtonEventListener buttonEventListener = new ButtonEventListener();
            buttonEventListener.clearWindow("");

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

            getContentPane().add(date);
            getContentPane().add(maxtemp);
            getContentPane().add(mintemp);
            getContentPane().add(day);
            getContentPane().add(month);

            date.setBounds(50, 180, 100, 140);
            mintemp.setBounds(60, 300, 120, 40);
            maxtemp.setBounds(60, 320, 120, 40);
            day.setBounds(60, 170, 120, 40);
            month.setBounds(60, 280, 120, 40);

            for (int j = 0; j < 6; j++) {
                JLabel label3 = new JLabel(text.get(j));
                label3.setBounds(180, 230 + j * 30, 100, 30);
                createdLabel.add(label3);
                getContentPane().add(label3);
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
                        getContentPane().add(label3);
                    }

                    textHour.setBounds(300 + i * 50, 200, 40, 30);
                    iconLabel.setBounds(280 + i * 50, 150, 64, 64);


                    createdLabel.add(textHour);
                    createdLabel.add(iconLabel);

                    getContentPane().add(textHour);
                    getContentPane().add(iconLabel);


                } catch (IOException ex) {
                    System.out.println("error");
                }
            }
            revalidate();
            repaint();
        }

        public ButtonEventListenerWeather(WeatherInfo o, int count) {
            this.o = o;
            this.count = count;
        }

        public ButtonEventListenerWeather(WeatherInfo o) {
            this.o = o;
        }
    }
}
