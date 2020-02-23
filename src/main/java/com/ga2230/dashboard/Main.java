package com.ga2230.dashboard;

import com.ga2230.dashboard.graphics.Frame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        loadLicense();
        loadTheme();
        loadFrame();
    }

    private static void loadLicense() {
        System.setProperty("jxbrowser.license.key", "1BNDHFSC1FTIQN062EJO3ZSIX528RTPMK6L9V7HHKN6KRWKQDLC7D9FP6NC2BWEP6QMTCU");
    }

    private static void loadTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private static void loadFrame() {
        try {
            Frame frame = new Frame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
