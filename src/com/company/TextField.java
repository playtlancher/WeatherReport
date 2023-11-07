package com.company;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.geonames.*;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class TextField extends JTextField {

    private final Animator animator;
    private float location;
    private boolean show;
    private boolean animateHinText = true;
    private Color lineColor = new Color(3, 155, 216);
    private String labelText = "WORK!!!";
    private boolean mouseOver = false;
    private JFrame jFrame;
    private Font font = new Font("New Times Roman",Font.BOLD,12);

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public String getLabelText() {
        return labelText;
    }


    public TextField() {
        setFont(font);
        setBorder(new EmptyBorder(20, 3, 10, 3));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                mouseOver = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseOver = false;
                repaint();
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                showing(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                showing(true);
            }
        });

        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                location = fraction;
                repaint();
            }

            @Override
            public void begin() {
                animateHinText = getText().equals("");
            }
        };
        animator = new Animator(300, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
    }

    private void showing(boolean action) {
        if (animator.isRunning()) {
            animator.stop();
        } else {
            location = 1;
        }
        animator.setStartFraction(1f - location);
        show = action;
        location = 1f - location;
        animator.start();
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        int width = getWidth();
        int height = getHeight();
        if (mouseOver) {
            g2.setColor(lineColor);
        } else {
            g2.setColor(new Color(150, 150, 150));
        }

        g2.fillRect(2, height - 1, width - 4, 1);
        createHintText(g2);
        createLineStyle(g2);
        g2.dispose();
    }

    private void createHintText(Graphics2D g2) {
        Insets in = getInsets();
        g2.setColor(new Color(150, 150, 150));
        FontMetrics ft = g2.getFontMetrics();
        Rectangle2D r2 = ft.getStringBounds(labelText, g2);
        double height = getHeight() - in.top - in.bottom;
        double textY = (height - r2.getHeight()) / 2;
        double size;
        if (animateHinText) {
            if (show) {
                size = 18 * (1 - location);
            } else {
                size = 18 * location;
            }
        } else {
            size = 18;
        }
        g2.drawString(labelText, in.right, (int) (in.top + textY + ft.getAscent() - size));
    }

    private void createLineStyle(Graphics2D g2) {
        if (isFocusOwner()) {
            double width = getWidth() - 4;
            int height = getHeight();
            g2.setColor(lineColor);
            double size;
            if (show) {
                size = width * (1 - location);
            } else {
                size = width * location;
            }
            double x = (width - size) / 2;
            g2.fillRect((int) (x * 2), height - 2, (int) size + 2, 2);
        }
    }

    @Override
    public void setText(String string) {
        if (!getText().equals("")) {
            showing(string.equals(""));
        }
        super.setText(string);
    }

    public void suggestion() {
        WebService.setUserName("razanka");
        ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();

        try {
            searchCriteria.setFeatureClass(FeatureClass.fromValue("P"));
            searchCriteria.setCountryCode("UA");
            searchCriteria.setNameStartsWith(getText());
            searchCriteria.setMaxRows(1000);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }

        List<String> suggestions = new ArrayList<>();

        try {
            ToponymSearchResult searchResult = WebService.search(searchCriteria);

            for (Toponym toponym : searchResult.getToponyms()) {
                suggestions.add(toponym.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel suggestionPanel = new JPanel();
        suggestionPanel.setLayout(new BorderLayout());
        suggestionPanel.setVisible(false);

        JList<String> suggestionList = new JList<>(suggestions.toArray(new String[0]));
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestionList.setVisibleRowCount(5);

        JScrollPane scrollPane = new JScrollPane(suggestionList);
        suggestionPanel.add(scrollPane);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    String text = getText().toLowerCase();
                    List<String> filteredSuggestions = new ArrayList<>();
                    for (String suggestion : suggestions) {
                        if (suggestion.toLowerCase().startsWith(text)) {
                            filteredSuggestions.add(suggestion);
                        }
                    }
                    if (filteredSuggestions.isEmpty()) {
                        suggestionPanel.setVisible(false);
                    } else {
                        suggestionList.setListData(filteredSuggestions.toArray(new String[0]));
                        suggestionPanel.setVisible(true);
                    }
                });
            }
        });

        suggestionList.addListSelectionListener(e -> {
            if (!suggestionList.isSelectionEmpty()) {
                String selectedSuggestion = suggestionList.getSelectedValue();
                this.setText(selectedSuggestion);
                suggestionPanel.setVisible(false);
            }
        });

        suggestionPanel.setBounds(200, 40, 170, 100);
        addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                if(!getText().equals("")) {
                    suggestionPanel.setVisible(true);
                    revalidate();
                    repaint();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                suggestionPanel.setVisible(false);
            }
        });
        jFrame.add(suggestionPanel);
    }

    public void setjFrame(JFrame jFrame) {
        this.jFrame = jFrame;
        suggestion();
    }
}