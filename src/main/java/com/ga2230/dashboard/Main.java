package com.ga2230.dashboard;

import com.ga2230.dashboard.graphics.Frame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        loadLicense();
        loadTheme();
        if (args.length > 0) {
            loadFrame(args[0].equals("stream"));
        }else{
            loadFrame(false);
        }
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

    private static void loadFrame(boolean type) {
        Frame frame = new Frame(type);
    }

}
