package com.ga2230.dashboard.graphics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ga2230.dashboard.communications.Communicator;
import com.ga2230.dashboard.communications.Connection;
import com.ga2230.dashboard.communications.Global;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends Panel {

    private JTextArea textArea;
    private JScrollPane scrollPane;

    public LogPanel() {
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);

        textArea.setEditable(false);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setBackground(Color.BLACK);
        add(scrollPane);

        Global.TelemetryConnection.register(new Connection.Callback() {
            @Override
            public void callback(boolean finished, String result) {
                textArea.setForeground(finished ? Color.GREEN : Color.RED);
                textArea.setText(beautify(result));
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
        Dimension scrollDimension = new Dimension(width, height - 8);
        textArea.setMinimumSize(scrollDimension);
        scrollPane.setPreferredSize(scrollDimension);
        scrollPane.setMinimumSize(scrollDimension);
        scrollPane.setMaximumSize(scrollDimension);
        super.setSize(width, height);
    }
}
