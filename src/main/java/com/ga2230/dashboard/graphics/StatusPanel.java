package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Connection;
import com.ga2230.dashboard.configuration.Configuration;
import com.ga2230.dashboard.configuration.StatusButtonConfiguration;
import com.ga2230.dashboard.telemetry.StatusHelper;
import com.ga2230.dashboard.telemetry.TelemetryHelper;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class StatusPanel extends Panel {

    private Connection actionConnection;

    private ArrayList<StatusButton> buttons;

    public StatusPanel() {

        actionConnection = Connection.openConnection(20, Connection.ConnectionType.QueuedExecution);

        buttons = new ArrayList<>();

        ArrayList<StatusButtonConfiguration> configurations = Configuration.load().getConfigurations();

        // Actual brain
        // setLayout(new GridLayout((configurations.size() + configurations.size() % 2) / 2, 2));

        // No brain
        setLayout(new GridLayout(0, 2));

        for (StatusButtonConfiguration configuration : configurations) {
            StatusButton button = new StatusButton(configuration);
            buttons.add(button);
            add(button);
        }
    }

    public class StatusButton extends JButton {

        private StatusButtonConfiguration configuration;

        public StatusButton(StatusButtonConfiguration configuration) {
            this.configuration = configuration;
            setText(configuration.getText());
            setFont(getFont().deriveFont(Font.BOLD, 25f));
            setContentAreaFilled(false);
            setOpaque(true);
            setBorder(new LineBorder(Color.ORANGE, 3));
            setBackground(Color.GRAY);
            setForeground(Color.WHITE);
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    queueAction();
                }
            });
            prepareUpdater();
        }

        public void prepareUpdater() {
            if (configuration.getStateFunction().length() > 0) {
                if (!configuration.getStateFunction().contains(">")) {
                    StatusHelper helper = new StatusHelper(2);
                    helper.configure(configuration.getStateFunction(), new StatusHelper.Callback() {
                        @Override
                        public void callback(boolean value) {
                            setBackground(value ? Color.GREEN : Color.RED);
                        }
                    });
                } else {
                    String[] moduleKey = configuration.getStateFunction().split(">", 2);
                    String[] keyValue = moduleKey[1].split("<", 2);
                    TelemetryHelper helper = new TelemetryHelper(2);
                    helper.configure(moduleKey[0], keyValue[0], new TelemetryHelper.Callback() {
                        @Override
                        public void callback(String value) {
                            setBackground(value.equals(keyValue[1]) ? Color.GREEN : Color.RED);
                        }
                    });
                }
            }
        }

        public void queueAction() {
            if (!configuration.getClickFunction().equals("")) {
                actionConnection.send(new Connection.Command(configuration.getClickFunction(), new Connection.Callback() {
                    @Override
                    public void callback(boolean finished, String result) {
                        JOptionPane.showMessageDialog(Frame.getFrame(), result, "Result", finished ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                    }
                }));
            }
        }

    }

}
