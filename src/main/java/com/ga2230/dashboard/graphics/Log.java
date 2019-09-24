package com.ga2230.dashboard.graphics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ga2230.dashboard.communications.Broadcaster;
import com.ga2230.dashboard.communications.Communicator;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class Log extends Panel {

    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JButton button;
    private boolean pushListen = false;

    public Log() {
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
        button = new JButton("Switch to Push");
        textArea.setEditable(false);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setBackground(Color.BLACK);
        add(button);
        add(scrollPane);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pushListen = !pushListen;
                textArea.setText("Waiting for data...");
                button.setText(pushListen ? "Switch to Pull" : "Switch to Push");
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
        Dimension scrollDimension = new Dimension(width, height - buttonDimension.height - 10);
        textArea.setMinimumSize(scrollDimension);
        scrollPane.setPreferredSize(scrollDimension);
        scrollPane.setMinimumSize(scrollDimension);
        scrollPane.setMaximumSize(scrollDimension);
        button.setPreferredSize(buttonDimension);
        button.setMinimumSize(buttonDimension);
        button.setMaximumSize(buttonDimension);
        super.setSize(width, height);
    }
}
