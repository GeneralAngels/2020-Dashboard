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
        button = new JButton("Switch Source");
        textArea.setEditable(false);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(button);
        add(scrollPane);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pushListen = !pushListen;
            }
        });
        Communicator.pullListener.listen(thing -> {
            if (!pushListen) {
                textArea.setText(beautify(thing.toString()));
            }
        });
        Communicator.pushListener.listen(thing ->{
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
        Dimension scrollDimension = new Dimension(width, height);
        textArea.setMinimumSize(scrollDimension);
        scrollPane.setPreferredSize(scrollDimension);
        scrollPane.setMinimumSize(scrollDimension);
        scrollPane.setMaximumSize(scrollDimension);
        super.setSize(width, height);
    }
}
