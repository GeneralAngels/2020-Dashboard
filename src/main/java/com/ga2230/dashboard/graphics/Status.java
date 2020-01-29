package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Broadcast;
import com.ga2230.dashboard.communications.Communicator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Status extends Panel {

    private JButton switchButton, reconnectButton;
    public Status() {
        reconnectButton = new JButton("Reconnect");
        reconnectButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Communicator.reconnect();
            }
        });
        setLayout(new GridLayout(1, 1));
        add(reconnectButton);
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
