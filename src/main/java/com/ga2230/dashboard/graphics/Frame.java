package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Communicator;

import javax.swing.*;

public abstract class Frame {

    private static final int WINDOW_HEIGHT = 528;
    private static final int WINDOW_WIDTH = 1366;

    private static final int INNER_PANE_WIDTH = (WINDOW_WIDTH / 2) - 8;
    private static final int INNER_PANE_HEIGHT = (WINDOW_HEIGHT);

    private static JFrame frame;

    private static JPanel panel;

    private static JTabbedPane analyticsSwitcher, liveSwitcher;

    public static JFrame getInstance() {
        return frame;
    }

    public static void initialize() {
        frame = new JFrame();
        loadPanel();
        loadLive();
        loadAnalytics();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setVisible(true);
        Communicator.setFrame(frame);
    }

    private static void loadPanel() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        frame.setContentPane(panel);
    }

    private static void loadLive() {
        liveSwitcher = new JTabbedPane();
        loadStream();
        loadPath();
        panel.add(liveSwitcher);
    }

    private static void loadStream() {
        StreamView stream = new StreamView();
        // Size
        stream.setSize(INNER_PANE_WIDTH, INNER_PANE_HEIGHT);
        // Add tab
        liveSwitcher.add("Stream", stream);
        //stream.play();
    }

    private static void loadPath() {
        PathView pathView = new PathView();
        // Size
        pathView.setSize(INNER_PANE_WIDTH, INNER_PANE_HEIGHT);
        // Add tab
        liveSwitcher.add("Path", pathView);
    }

    private static void loadAnalytics() {
        analyticsSwitcher = new JTabbedPane();
        loadSimplified();
        loadAdvanced();
        panel.add(analyticsSwitcher);
    }

    private static void loadAdvanced() {
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

    private static void loadSimplified() {
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
