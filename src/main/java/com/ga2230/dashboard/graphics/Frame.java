package com.ga2230.dashboard.graphics;

import javax.swing.*;

public class Frame extends JFrame {

    private static final int WINDOW_HEIGHT = 528;
    private static final int WINDOW_WIDTH = 1366;

    private JPanel panel;

    private Panel analytics;
    private Camera camera;
    private Status status;
    private Graph graph;
    private CSV csv;
    private Log log;

    public Frame() {
        loadPanel();
        loadCamera();
        loadAnalytics();
        loadFrame();
    }

    private void loadFrame() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setVisible(true);
    }

    private void loadPanel() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        setContentPane(panel);
    }

    private void loadCamera() {
        camera = new Camera("");
        camera.setSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT);
        panel.add(camera);
    }

    private void loadAnalytics() {
        analytics = new Panel();
        analytics.setLayout(new BoxLayout(analytics, BoxLayout.Y_AXIS));
//        loadStatus();
//        loadCSV();
//        loadGraph();
        loadLog();
        analytics.setSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT);
        panel.add(analytics);
    }

    private void loadStatus() {
        status = new Status();
        status.setSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 5);
        analytics.add(status);
    }

    private void loadCSV() {
        csv = new CSV();
        csv.setSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 5);
        analytics.add(csv);
    }

    private void loadGraph() {
        graph = new Graph();
        graph.setSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT);
        analytics.add(graph);
    }

    private void loadLog() {
        log = new Log();
        log.setSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT);
        analytics.add(log);
    }

}
