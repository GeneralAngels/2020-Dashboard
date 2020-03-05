package com.ga2230.dashboard.communications;

import com.ga2230.dashboard.graphics.Panel;

import javax.swing.*;
import java.awt.*;

public class CommunicationIndicator extends Panel {

    private Connection connection;

    public CommunicationIndicator() {
        JLabel label = new JLabel("Connecting...");
        setLayout(new GridLayout());
        add(label);

        connection = Connection.openConnection(4, Connection.ConnectionType.PeriodicExecution);

    }

}
