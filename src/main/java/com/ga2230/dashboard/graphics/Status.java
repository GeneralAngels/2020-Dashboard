package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Communicator;
import com.ga2230.dashboard.util.ModuleHelper;

import javax.swing.*;
import java.awt.*;

public class Status extends Panel {

    private StatusField battery, gear;

    public Status() {
        battery = new StatusField("\uD83D\uDD0B");
        gear = new StatusField("\u2699");
        battery.setText("Unknown");
        gear.setText("Unknown");
        setLayout(new GridLayout(1, 2));
        add(battery);
        add(gear);
        Communicator.pushListener.listen(thing -> battery.setText(ModuleHelper.getDouble("battery", thing) + "%"));
        Communicator.pullListener.listen(thing -> gear.setText(ModuleHelper.getBoolean("modules->drive->values->gear", thing) ? "Power" : "Speed"));
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
