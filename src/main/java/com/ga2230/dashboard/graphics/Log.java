package com.ga2230.dashboard.graphics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ga2230.dashboard.communications.Broadcast;
import com.ga2230.dashboard.communications.Communicator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class Log extends Panel {

    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JButton switchButton, reconnectButton;
    private JPanel buttons;
    private boolean pushListen = false;

    public Log() {
        buttons = new JPanel();
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
        switchButton = new JButton("Switch to Push");
        reconnectButton = new JButton("Reconnect");
        textArea.setEditable(false);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setBackground(Color.BLACK);
        buttons.setLayout(new GridLayout(1, 2));
        buttons.add(reconnectButton);
        buttons.add(switchButton);
        add(buttons);
        add(scrollPane);
        switchButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pushListen = !pushListen;
                textArea.setText("Waiting for data...");
                switchButton.setText(pushListen ? "Switch to Pull" : "Switch to Push");
            }
        });
        reconnectButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Communicator.reconnect();
            }
        });
        Communicator.Topic woahTelemetry = new Communicator.Topic();
        woahTelemetry.setCommand("master telemetry");
        woahTelemetry.getBroadcast().listen(new Broadcast.Listener<String>() {
            @Override
            public void update(String thing) {
                textArea.setText(beautify(thing));
            }
        });
        woahTelemetry.begin(5);
    }

    private String beautify(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object obj = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            return json;
        }
    }

    @Override
    public void setSize(int width, int height) {
        Dimension buttonDimension = new Dimension(width - 6, height / 8);
        Dimension scrollDimension = new Dimension(width, height - buttonDimension.height - 14);
        textArea.setMinimumSize(scrollDimension);
        scrollPane.setPreferredSize(scrollDimension);
        scrollPane.setMinimumSize(scrollDimension);
        scrollPane.setMaximumSize(scrollDimension);
        buttons.setPreferredSize(buttonDimension);
        buttons.setMinimumSize(buttonDimension);
        buttons.setMaximumSize(buttonDimension);
        super.setSize(width, height);
    }
}
