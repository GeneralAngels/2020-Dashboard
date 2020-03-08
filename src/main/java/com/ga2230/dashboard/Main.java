package com.ga2230.dashboard;

import com.ga2230.dashboard.communications.Communicator;
import com.ga2230.dashboard.configuration.Configuration;
import com.ga2230.dashboard.graphics.Frame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        loadTheme();
        loadConfiguration();
        loadFrame();
    }

    private static void loadConfiguration() {
        Configuration.initialize();
        Communicator.setTeamNumber(Configuration.getTeamNumber());
    }

    private static void loadTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private static void loadFrame() {
        Frame.initialize();
    }

}
