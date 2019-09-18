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
        System.setProperty("jxbrowser.license.key", "6P830J66YAA1OJQ0NORQ4PMBMFZBYBWPRVUU4LOR2HSOYEZDKNTFZUHUJ7WDHP007MJ0");
    }

    private static void loadTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private static void loadFrame() {
        Frame frame = new Frame();
    }

}
