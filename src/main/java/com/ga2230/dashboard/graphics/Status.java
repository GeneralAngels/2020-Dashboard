package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Communicator;
import com.ga2230.dashboard.communications.Connection;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

public class Status extends Panel {

    private Connection connection;

    private JButton autonomousButton, reconnectButton;

    public Status() {

        connection = new Connection(2230, 1, true);
        connection.open();

        autonomousButton = new JButton("Upload new autonomous");
        autonomousButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent a) {
                // Choose a file
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("Shleam Script", "shleam", "2230"));
                chooser.setDialogType(JFileChooser.FILES_ONLY);
                chooser.showDialog(Frame.getFrame(), "Upload");
                // Make sure chosen file isn't null
                File file = chooser.getSelectedFile();
                if (file != null) {
                    // Try reading the file
                    try {
                        // Read the file
                        List<String> strings = Files.readAllLines(file.toPath());
                        // Rebuild the file
                        StringBuilder builder = new StringBuilder();
                        for (String s : strings) {
                            if (builder.length() > 0)
                                builder.append("\n");
                            builder.append(s);
                        }
                        // Encode the file
                        String command = "runtime load " + builder.toString();
                        String base64 = new String(Base64.getEncoder().encode(command.getBytes()));
                        // Upload the file
                        connection.send("base64:" + base64, new Connection.Callback() {
                            @Override
                            public void callback(boolean finished, String result) {
                                setStatus(finished, new String(Base64.getDecoder().decode(result.getBytes())));
                            }
                        });
                    } catch (IOException e) {
                        setStatus(false, e.toString());
                    }
                } else {
                    setStatus(false, "You must choose a file");
                }
            }
        });
        reconnectButton = new JButton("Reconnect");
        reconnectButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Communicator.reconnect();
            }
        });
        setLayout(new GridLayout(1, 2));
        add(reconnectButton);
        add(autonomousButton);
    }

    private void setStatus(boolean finished, String message) {
        JOptionPane.showMessageDialog(Frame.getFrame(), message, finished ? "Status report" : "Error report", finished ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }
}
