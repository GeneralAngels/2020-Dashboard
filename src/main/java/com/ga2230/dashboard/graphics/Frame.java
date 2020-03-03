package com.ga2230.dashboard.graphics;

import javafx.scene.layout.Pane;

import javax.swing.*;

public class Frame extends JFrame {

    private static Frame instance;

    public static Frame getFrame() {
        return instance;
    }

    private static final int WINDOW_HEIGHT = 528;
    private static final int WINDOW_WIDTH = 1366;

    private static final int INNER_PANE_WIDTH = (WINDOW_WIDTH / 2) - 8;
    private static final int INNER_PANE_HEIGHT = (WINDOW_HEIGHT);

    static final int FONT_SIZE = 25;

    private JPanel panel;

    private JTabbedPane analyticsSwitcher, liveSwitcher;

    public Frame() {
        this.loadPanel();
        this.loadLive();
        this.loadAnalytics();
    }

    public void display(){
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setVisible(true);
        instance = this;
    }

    private void loadPanel() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        setContentPane(panel);
    }

    private void loadLive() {
        liveSwitcher = new JTabbedPane();
        this.loadStream();
        this.loadPath();
        panel.add(liveSwitcher);
    }

    private void loadStream() {
        StreamView stream = new StreamView();
        // Size
        stream.setSize(INNER_PANE_WIDTH, INNER_PANE_HEIGHT);
        // Add tab
        liveSwitcher.add("Stream", stream);
        //stream.play();
    }

    private void loadPath() {
        PathView pathView = new PathView();
        // Size
        pathView.setSize(INNER_PANE_WIDTH, INNER_PANE_HEIGHT);
        // Add tab
        liveSwitcher.add("Trajectory", pathView);
    }

    private void loadAnalytics() {
        analyticsSwitcher = new JTabbedPane();
        this.loadSimplified();
        this.loadAdvanced();
        panel.add(analyticsSwitcher);
    }

    private void loadAdvanced() {
        Panel advanced = new Panel();
        advanced.setLayout(new BoxLayout(advanced, BoxLayout.Y_AXIS));
        // Load csv
        CSVPanel csvPanel = new CSVPanel();
        csvPanel.setSize(INNER_PANE_WIDTH, INNER_PANE_HEIGHT / 15);
        // Load graph
        GraphPanel graphPanel = new GraphPanel();
        graphPanel.setSize(INNER_PANE_WIDTH, (int) (INNER_PANE_HEIGHT / 1.9));
        // Load log
        LogPanel logPanel = new LogPanel();
        logPanel.setSize(INNER_PANE_WIDTH, (int) (INNER_PANE_HEIGHT / 2.9));
        // Size
        // Add all
        advanced.add(csvPanel);
        advanced.add(graphPanel);
        advanced.add(logPanel);
        // Add tab
        analyticsSwitcher.add("Advanced", advanced);
    }

    private void loadSimplified() {
        Panel simpilfied = new Panel();
        simpilfied.setLayout(new BoxLayout(simpilfied, BoxLayout.Y_AXIS));
        // Status panel
        StatusPanel status = new StatusPanel();
        ButtonPanel buttons = new ButtonPanel();
        // Size
        buttons.setSize(INNER_PANE_WIDTH, (int) (INNER_PANE_HEIGHT * 0.1));
        // Add all
        simpilfied.add(status);
        simpilfied.add(buttons);
        // Add tab
        analyticsSwitcher.add("Simple", simpilfied);
    }
}
