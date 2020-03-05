package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Communicator;
import com.ga2230.dashboard.communications.Connection;
import com.ga2230.dashboard.graphics.Panel;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class IndicatorPanel extends Panel {

    private Connection connection;

    private long lastTime = 0;
    private JLabel label;

    public IndicatorPanel() {
        label = new JLabel("Connecting...");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 20f));
        setLayout(new GridLayout());
        add(label);

        Communicator.TelemetryConnection.register(new Connection.Callback() {
            @Override
            public void callback(boolean finished, String result) {
                lastTime = System.currentTimeMillis();
            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if ((System.currentTimeMillis() - lastTime) > 5 * 1000) {
                    setStatus(false);
                } else {
                    setStatus(true);
                }
            }
        }, 1000, 1000);
    }

    private void setStatus(boolean connected) {
        label.setText(connected ? "Connected" : "Disconnected");
        setBackground(connected ? Color.GREEN : Color.RED);
    }

}
