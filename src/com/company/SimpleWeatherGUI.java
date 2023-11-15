package com.company;

import com.company.models.WeatherInfo;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;


public class SimpleWeatherGUI extends JFrame {

    private final JButton weatherReportButton = new JButton("Weather Report");
    private final TextField inputCity = new TextField();
    private final TextField inputDays = new TextField();

    Font fontCity = new Font("Times new roman", Font.BOLD, 80);

    List<JButton> createdButtons = new ArrayList<>();
    List<JLabel> createdLabels = new ArrayList<>();

    public SimpleWeatherGUI() throws IOException {

        super("SimpleWeatherGUI");
        URL imageUrl = new URL("https://cdn.weatherapi.com/weather/64x64/day/113.png");
        ImageIcon image = new ImageIcon(imageUrl);
        this.setIconImage(image.getImage());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();

        Container container = this.getContentPane();

        inputCity.setjFrame(this);

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
        String link = LinkBuilder.buildLink(city, "1", "3dd7434243bb4e06933153229230509");
        StringBuilder response = ResponseContentGetter.getResponseContent(link);
        List<WeatherInfo> weatherList = parser.parseWeather(response);
        ButtonEventListenerWeather belw = new ButtonEventListenerWeather(this, createdButtons, createdLabels, weatherList.get(0));
        belw.actionPerformed(null);


        //Create settings icon
        URL settingsImageUrl = new URL("https://th.bing.com/th/id/OIP.bcoOPeHwEPWzLWzNQjJlrAHaHa?pid=ImgDet&rs=1");
        ImageIcon settingsIcon = new ImageIcon(settingsImageUrl);
        Image settingsImage = settingsIcon.getImage();
        Image finalImage = settingsImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(finalImage);

        //Create menu bar
        JMenuBar jMenuBar = new JMenuBar();
        JMenu menu = new JMenu();
        menu.setIcon(newIcon);
        JMenuItem settings = new JMenuItem("Settings");
        settings.addActionListener(e -> {
            SettingsDialog myDialog = new SettingsDialog(finalImage);
            myDialog.setVisible(true);
        });


        menu.add(settings);
        jMenuBar.add(menu);
        this.setJMenuBar(jMenuBar);

        this.setBounds(dimension.width / 2 - 100, dimension.height / 2 - 100, 800, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        container.setLayout(new GridLayout());


        final JLabel cityText = new JLabel();
        cityText.setText(city);
        createdLabels.add(cityText);


        inputCity.setBounds(200, 0, 100, 40);
        inputDays.setBounds(400, 0, 100, 40);
        weatherReportButton.setBounds(250, 50, 200, 20);
        cityText.setBounds(250, 80, 200, 70);
        cityText.setFont(fontCity);

        weatherReportButton.addActionListener(new ButtonEventListener(this));

        container.add(inputCity);
        container.add(inputDays);
        container.add(weatherReportButton);
        container.add(cityText);

        setLayout(null);
    }

    public void clearWindow() {
        for (JButton jButton : createdButtons) {
            getContentPane().remove(jButton);
        }
        for (JLabel label : createdLabels) {
            getContentPane().remove(label);
        }
        createdLabels.clear();
        createdButtons.clear();
        revalidate();
        repaint();
    }

    class ButtonEventListener implements ActionListener {

        String key = "3dd7434243bb4e06933153229230509";
        WeatherParser weatherParser = new WeatherParser();
        JFrame jFrame;

        public ButtonEventListener(JFrame jFrame) {
            this.jFrame = jFrame;
        }

        public void actionPerformed(ActionEvent e) {
            clearWindow();
            List<WeatherInfo> weatherInfo;

            String message;

            String city = inputCity.getText();

            String days = inputDays.getText();

            while (true) {

                if (!days.matches("-?\\d+")) {
                    message = "Поле days повинно містити числа";
                    JOptionPane.showMessageDialog(null, message, "Output", JOptionPane.PLAIN_MESSAGE);
                    break;
                }

                if (Integer.parseInt(days) > 3 && Integer.parseInt(days) <= 0) {
                    message = "Число повинно бути в діапазоні від 0 до 3 включно";
                    JOptionPane.showMessageDialog(null, message, "Output", JOptionPane.PLAIN_MESSAGE);
                    break;
                }
                String APIlink = LinkBuilder.buildLink(city, days, key);
                StringBuilder response = ResponseContentGetter.getResponseContent(APIlink);
                weatherInfo = weatherParser.parseWeather(response);

                for (WeatherInfo weather : weatherInfo) {
                    Button button = new Button(weather.date.substring(5, 7) + "-" + weather.date.substring(8, 10));
                    createdButtons.add(button);
                    button.addActionListener(new ButtonEventListenerWeather(jFrame, createdButtons, createdLabels, weather, createdButtons.size() - 1));
                    button.setBounds(150 + createdButtons.size() * 80, 80, 80, 20);
                    getContentPane().add(button);
                }
                revalidate();
                repaint();
                break;
            }
        }


    }
}
