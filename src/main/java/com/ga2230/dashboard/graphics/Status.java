package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Broadcast;
import com.ga2230.dashboard.communications.Communicator;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.CRC32;

public class Status extends Panel {

    private static JFrame frame;

    public static void setFrame(JFrame frame) {
        Status.frame = frame;
    }

    private JButton autonomousButton, reconnectButton;

    public Status() {
        autonomousButton = new JButton("Upload new autonomous");
        autonomousButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("2230 Auto File", "2230");
                    chooser.setFileFilter(filter);
                    chooser.showDialog(Status.frame, "Upload");
                    File file = chooser.getSelectedFile();
                    if (file != null) {
                        String string = Files.readString(file.toPath());
                        string = string.replaceAll("\r", "");
                        // Base64
                        String base64 = new String(Base64.getEncoder().encode(string.getBytes()));
                        // CRC
                        CRC32 crc = new CRC32();
                        crc.update(base64.getBytes());
                        // Send it
                        Communicator.Topic topic = new Communicator.Topic();
                        topic.setCommand("autonomous load " + base64);
                        topic.getBroadcast().listen(new Broadcast.Listener<String>() {
                            @Override
                            public void update(String thing) {
                                if (frame != null) {
                                    JOptionPane.showMessageDialog(frame, thing.equals(String.valueOf(crc.getValue())) ? "Autonomous loaded" : "Not loaded!", "Auto load state", thing.equals(String.valueOf(crc.getValue())) ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });
                        topic.single(5);
                    }
                } catch (Exception eh) {
                    eh.printStackTrace();
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

    private class StatusField extends JLabel {

        private String icon;

        public StatusField(String icon) {
            this.icon = icon;
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Frame.FONT_SIZE));
            setOpaque(false);
        }

        @Override
        public void setText(String text) {
            super.setText(icon + " " + text);
        }

        public void setSize(int width, int height) {
            Dimension dimension = new Dimension(width, height);
            setPreferredSize(dimension);
            setMinimumSize(dimension);
            setMaximumSize(dimension);
        }
    }
}
