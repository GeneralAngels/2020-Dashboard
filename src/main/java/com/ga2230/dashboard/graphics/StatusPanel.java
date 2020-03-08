package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Connection;
import com.ga2230.dashboard.communications.Global;
import com.ga2230.dashboard.configuration.Configuration;
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

        ArrayList<StatusButton.Configuration> configurations = Configuration.getConfigurations();

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
                String[] moduleKey = configuration.getStateFunction().split(">", 2);
                String[] keyValue = moduleKey[1].split("<", 2);
                Global.TelemetryConnection.register(new Connection.Callback() {
                    @Override
                    public void callback(boolean finished, String result) {
                        boolean success = TelemetryParser.findString(moduleKey[0], moduleKey[1]).equals(keyValue[1]);
                        setBackground(success ? Color.GREEN : Color.RED);
                    }
                });
            }
        }

        public void queueAction() {
            if (!configuration.getClickFunction().equals("")) {
                Global.ActionConnection.send(new Connection.Command(configuration.getClickFunction(), (finished, result) -> JOptionPane.showMessageDialog(Frame.getInstance(), result, "Result", finished ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE)));
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
