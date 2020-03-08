package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Communicator;
import com.ga2230.dashboard.communications.Global;
import com.ga2230.dashboard.configuration.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ButtonPanel extends Panel {

    private JButton autonomousButton, reconnectButton;

    public ButtonPanel() {
        autonomousButton = new JScriptButton(Global.ActionConnection, Configuration.getTeamNumber(), Frame.getInstance());
        reconnectButton = new JButton("Reconnect");
        reconnectButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Communicator.reconnect();
            }
        });
        setLayout(new GridLayout(1, 3));
        add(reconnectButton);
        add(new JIndicator(Global.TelemetryConnection));
        add(autonomousButton);
    }

    private void setStatus(boolean finished, String message) {
        JOptionPane.showMessageDialog(Frame.getInstance(), message, finished ? "Status report" : "Error report", finished ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }
}
