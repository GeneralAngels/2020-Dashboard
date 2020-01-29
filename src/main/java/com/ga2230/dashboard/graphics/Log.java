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

    public Log() {
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);

        textArea.setEditable(false);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setBackground(Color.BLACK);
        add(scrollPane);

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
        Dimension scrollDimension = new Dimension(width, height - 8);
        textArea.setMinimumSize(scrollDimension);
        scrollPane.setPreferredSize(scrollDimension);
        scrollPane.setMinimumSize(scrollDimension);
        scrollPane.setMaximumSize(scrollDimension);
        super.setSize(width, height);
    }
}
