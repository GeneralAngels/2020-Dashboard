package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Broadcast;
import com.ga2230.dashboard.communications.Communicator;

import javax.swing.*;
import java.awt.*;

public class Status extends Panel {

    private StatusField laptopBattery, robotBattery;

    public Status() {
        laptopBattery = new StatusField("\uD83D\uDD0B(\uD83D\uDCBB)");
        robotBattery = new StatusField("\uD83D\uDD0B(\uD83E\uDD16)");
        laptopBattery.setText("-1%");
        robotBattery.setText("-1%");
        setLayout(new GridLayout(1, 2));
        add(laptopBattery);
        add(robotBattery);
        Communicator.Topic updateTopic = new Communicator.Topic();
        updateTopic.getBroadcast().listen(new Broadcast.Listener<String>() {
            @Override
            public void update(String thing) {
                try {
                    String[] split = thing.split(" ");
                    laptopBattery.setText(split[0] + "%");
                    robotBattery.setText(split[1] + "%");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        updateTopic.setCommand("batteries percentage");
        updateTopic.begin(0.2);
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
