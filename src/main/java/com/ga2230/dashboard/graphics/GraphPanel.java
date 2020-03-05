package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Communicator;
import com.ga2230.dashboard.communications.Connection;
import com.ga2230.dashboard.telemetry.TelemetryParser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GraphPanel extends Panel {

    private XYSeries logSeries;
    private ChartPanel chartPanel;
    private JPanel UIPanel;

    private String parsedModule = "robot", parsedValue = "time";

    public GraphPanel() {
        Communicator.TelemetryConnection.register(new Connection.Callback() {
            @Override
            public void callback(boolean finished, String result) {
                if (finished) {
                    update();
                }
            }
        });

        JFreeChart chart = createChart(createDataset());
        chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);

        UIPanel = new JPanel(new GridLayout(1, 2));
        JButton clearAll = new JButton("Clear All");
        JTextField coordinatesField = new JTextField(parsedModule + ">" + parsedValue);

        coordinatesField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                String[] split = coordinatesField.getText().split(">", 2);
                if (split.length == 2) {
                    parsedModule = split[0];
                    parsedValue = split[1];
                }
                clear();
            }
        });
        clearAll.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        UIPanel.add(coordinatesField);
        UIPanel.add(clearAll);

        add(UIPanel);
        add(chartPanel);

        setBackground(Color.ORANGE);

        clear();
    }

    private void update() {
        try {
            logSeries.add(TelemetryParser.findDouble("robot", "time"), TelemetryParser.findDouble(parsedModule, parsedValue));
        } catch (Exception ignored) {
        }
    }

    public void clear() {
        logSeries.clear();
    }

    private XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(logSeries = new XYSeries("Data"));
        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                null,
                null,
                null,
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(2f));
        renderer.setSeriesShapesVisible(0, false);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);
        return chart;
    }

    @Override
    public void setSize(int width, int height) {
        Dimension sourceDimention = new Dimension(width - 6, height / 8);
        Dimension dimension = new Dimension(width - 6, height - 14 - sourceDimention.height);
        chartPanel.setSize(dimension);
        chartPanel.setPreferredSize(dimension);
        chartPanel.setMinimumSize(dimension);
        chartPanel.setMaximumSize(dimension);
        UIPanel.setPreferredSize(sourceDimention);
        UIPanel.setMinimumSize(sourceDimention);
        UIPanel.setMaximumSize(sourceDimention);
        super.setSize(width, height);
    }
}
