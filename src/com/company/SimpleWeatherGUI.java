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

        List<WeatherInfo> objects = parser.parse(LinkBuilder.getParameters(city, "1", "3dd7434243bb4e06933153229230509"));
        ButtonEventListenerWeather belw = new ButtonEventListenerWeather(this, createdButtons, createdLabel, objects.get(0));

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


        container.setLayout(new GridLayout());


        final JLabel cityText = new JLabel();
        cityText.setText(city);
        createdLabel.add(cityText);


        inputCity.setBounds(200, 0, 100, 40);
        inputDays.setBounds(400, 0, 100, 40);
        button.setBounds(250, 50, 200, 20);
        cityText.setBounds(250, 80, 200, 70);
        cityText.setFont(fontCity);

        button.addActionListener(new ButtonEventListener(this));

        container.add(inputCity);
        container.add(inputDays);
        container.add(button);
        container.add(cityText);

        setLayout(null);
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

    class ButtonEventListener implements ActionListener {
        String key = "3dd7434243bb4e06933153229230509";
        WeatherParser weatherParser = new WeatherParser();

        JFrame jFrame;

        public ButtonEventListener(JFrame jFrame) {
            this.jFrame = jFrame;
        }

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
                        button.addActionListener(new ButtonEventListenerWeather(jFrame, createdButtons, createdLabel, o, createdButtons.size() - 1));
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

    }

}
