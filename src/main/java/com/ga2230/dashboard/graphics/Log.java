package com.ga2230.dashboard.graphics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ga2230.dashboard.communications.Communicator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Log extends Panel {

    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JButton switchButton, reconnectButton;
    private boolean pushListen = false;

    public Log() {
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
        switchButton = new JButton("Switch to Push");
        reconnectButton = new JButton("Reconnect");
        textArea.setEditable(false);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setBackground(Color.BLACK);
        add(reconnectButton);
        add(switchButton);
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
        Communicator.pullListener.listen(thing -> {
            if (!pushListen) {
                textArea.setText(beautify(thing.toString()));
            }
        });
        Communicator.pushListener.listen(thing -> {
            if (pushListen) {
                textArea.setText(beautify(thing.toString()));
            }
        });
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
        Dimension scrollDimension = new Dimension(width, height - buttonDimension.height * 2 - 14);
        textArea.setMinimumSize(scrollDimension);
        scrollPane.setPreferredSize(scrollDimension);
        scrollPane.setMinimumSize(scrollDimension);
        scrollPane.setMaximumSize(scrollDimension);
        switchButton.setPreferredSize(buttonDimension);
        switchButton.setMinimumSize(buttonDimension);
        switchButton.setMaximumSize(buttonDimension);
        reconnectButton.setPreferredSize(buttonDimension);
        reconnectButton.setMinimumSize(buttonDimension);
        reconnectButton.setMaximumSize(buttonDimension);
        super.setSize(width, height);
    }
}
