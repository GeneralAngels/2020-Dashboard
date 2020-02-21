package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Communicator;

import javax.swing.*;

public class Frame extends JFrame {

    private static final int WINDOW_HEIGHT = 528;
    private static final int WINDOW_WIDTH = 1366;
    static final int FONT_SIZE = 25;

    private JPanel panel;

    private Panel analytics;
    private Status status;
    private Graph graph;
    private CSV csv;
    private Log log;
    private Stream stream;

    public Frame() {
        loadPanel();
        loadPath();
//        loadStream();
        loadAnalytics();
        loadFrame();
        Communicator.setFrame(this);
        Status.setFrame(this);
//        stream.play();
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

    private void loadStream(){
        stream = new Stream();
        stream.setSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT);
        panel.add(stream);
    }

    private void loadPath() {
        Path placeholder = new Path();
        placeholder.setSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT);
        panel.add(placeholder);
    }

    private void loadAnalytics() {
        analytics = new Panel();
        analytics.setLayout(new BoxLayout(analytics, BoxLayout.Y_AXIS));
        loadCSV();
        loadGraph();
        loadLog();
        loadStatus();
        analytics.setSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT);
        panel.add(analytics);
    }

    private void loadStatus() {
        status = new Status();
//        status.setSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 16);
        analytics.add(status);
    }

    private void loadCSV() {
        csv = new CSV();
        csv.setSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 15);
        analytics.add(csv);
    }

    private void loadGraph() {
        graph = new Graph();
        graph.setSize(WINDOW_WIDTH / 2, (int) (WINDOW_HEIGHT / 1.9));
        analytics.add(graph);
    }

    private void loadLog() {
        log = new Log();
        log.setSize(WINDOW_WIDTH / 2, (int) (WINDOW_HEIGHT / 3));
        analytics.add(log);
    }



}
