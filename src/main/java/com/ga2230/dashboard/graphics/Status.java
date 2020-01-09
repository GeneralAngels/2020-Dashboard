package com.ga2230.dashboard.graphics;

import javax.swing.*;
import java.awt.*;

public class Status extends Panel {

    private StatusField laptopBattery, robotBattery;

    public Status() {
        laptopBattery = new StatusField("\uD83D\uDD0B");
        robotBattery = new StatusField("\uD83D\uDD0B");
        laptopBattery.setText("-1%");
        robotBattery.setText("-1%");
        setLayout(new GridLayout(1, 2));
        add(laptopBattery);
        add(robotBattery);
    }

    private class StatusField extends JLabel {

        private String icon;

        public StatusField(String icon) {
            this.icon = icon;
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Frame.FONT_SIZE));
            setOpaque(false);
        }

        @Override
        public void setText(String text) {
            super.setText(icon + " " + text);
        }

        public void setSize(int width, int height) {
            Dimension dimension = new Dimension(width, height);
            setPreferredSize(dimension);
            setMinimumSize(dimension);
            setMaximumSize(dimension);
        }
    }
}
