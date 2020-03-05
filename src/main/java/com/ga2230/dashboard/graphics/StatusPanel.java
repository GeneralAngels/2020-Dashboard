package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Communicator;
import com.ga2230.dashboard.communications.Connection;
import com.ga2230.dashboard.telemetry.StatusHelper;
import com.ga2230.dashboard.telemetry.TelemetryHelper;
import com.ga2230.dashboard.telemetry.TelemetryParser;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StatusPanel extends Panel {

    private ArrayList<StatusButton> buttons;

    public StatusPanel() {

        buttons = new ArrayList<>();

        ArrayList<StatusButton.Configuration> configurations = com.ga2230.dashboard.configuration.Configuration.load().getConfigurations();

        // Actual brain
        // setLayout(new GridLayout((configurations.size() + configurations.size() % 2) / 2, 2));

        // No brain
        setLayout(new GridLayout(0, 2));

        for (StatusButton.Configuration configuration : configurations) {
            StatusButton button = new StatusButton(configuration);
            buttons.add(button);
            add(button);
        }
    }

    public static class StatusButton extends JButton {

        private Configuration configuration;

        public StatusButton(Configuration configuration) {
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
                    Communicator.TelemetryConnection.register(new Connection.Callback() {
                        @Override
                        public void callback(boolean finished, String result) {
                        }
                    });
                }
            }
        }

        public void queueAction() {
            if (!configuration.getClickFunction().equals("")) {
                Communicator.ActionConnection.send(new Connection.Command(configuration.getClickFunction(), (finished, result) -> JOptionPane.showMessageDialog(Frame.getFrame(), result, "Result", finished ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE)));
            }
        }

        public static class Configuration {

            private String text;
            private String stateFunction;
            private String clickFunction;

            public Configuration(String text, String stateFunction, String clickFunction) {
                this.text = text;
                this.stateFunction = stateFunction;
                this.clickFunction = clickFunction;
            }

            public String getText() {
                return text;
            }

            public String getStateFunction() {
                return stateFunction;
            }

            public String getClickFunction() {
                return clickFunction;
            }
        }

    }

}
