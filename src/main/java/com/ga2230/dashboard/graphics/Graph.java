package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Broadcast;
import com.ga2230.dashboard.communications.Communicator;
import com.ga2230.dashboard.util.ModuleHelper;
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

public class Graph extends Panel {

    private XYDataset dataset;
    private XYSeries logSeries, pointSeries, userSeries;
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private JPanel uiPanel;
    private JButton addPoint, clearAll;
    private JTextField realtimeX, realtimeY;

    private Communicator.Topic xTopic, yTopic;

    private String xCoordinates = "master>time";
    private String yCoordinates = "master>time";
    private String lastX, lastY;

    public Graph() {
        realtimeY = new JTextField(yCoordinates);
        realtimeX = new JTextField(xCoordinates);
        addPoint = new JButton("Add Point");
        clearAll = new JButton("Clear All");
        realtimeY.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                yCoordinates = realtimeY.getText();
                clearChart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                yCoordinates = realtimeY.getText();
                clearChart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                yCoordinates = realtimeY.getText();
                clearChart();
            }
        });
        realtimeX.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                xCoordinates = realtimeX.getText();
                clearChart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                xCoordinates = realtimeX.getText();
                clearChart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                xCoordinates = realtimeX.getText();
                clearChart();
            }
        });
        clearAll.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAll();
            }
        });
        addPoint.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = JOptionPane.showInputDialog("Add point (x,y) or (0 0)", "0,0");
                String[] commaCords = result.split(",");
                String[] spaceCords = result.split(" ");
                if (commaCords.length == 2) {
                    try {
                        double x = Double.parseDouble(commaCords[0].trim());
                        double y = Double.parseDouble(commaCords[1].trim());
                        userSeries.add(x, y);
                    } catch (Exception e1) {

                    }
                } else if (spaceCords.length == 2) {
                    try {
                        double x = Double.parseDouble(spaceCords[0].trim());
                        double y = Double.parseDouble(spaceCords[1].trim());
                        userSeries.add(x, y);
                    } catch (Exception e1) {

                    }
                }
            }
        });
        chart = createChart(createDataset());
        chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.white);
        uiPanel = new JPanel(new GridLayout(2, 2));
        setBackground(Color.ORANGE);
        uiPanel.add(realtimeX);
        uiPanel.add(realtimeY);
        uiPanel.add(addPoint);
        uiPanel.add(clearAll);
        add(uiPanel);
        add(chartPanel);
        xTopic = new Communicator.Topic();
        xTopic.getBroadcast().listen(thing -> {
            lastX = thing;
            Graph.this.update();
        });
        xTopic.begin(5);
        yTopic = new Communicator.Topic();
        yTopic.getBroadcast().listen(thing -> {
            lastY = thing;
            Graph.this.update();
        });
        yTopic.begin(5);
    }

    private void update() {
        try {
            if (lastX != null &&
                    lastY != null) {
                JSONObject x = new JSONObject(lastX);
                JSONObject y = new JSONObject(lastY);
                String[] xKeys = xCoordinates.split(">");
                String[] yKeys = yCoordinates.split(">");
                if (xKeys.length > 1 &&
                        yKeys.length > 1) {
                    if (x.has(xKeys[1]) &&
                            y.has(yKeys[1])) {
                        logSeries.add(x.getDouble(xKeys[1]), y.getDouble(yKeys[1]));
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public void clearChart() {
        xTopic.setCommand(xCoordinates.split(">")[0] + " json");
        yTopic.setCommand(yCoordinates.split(">")[0] + " json");
        logSeries.clear();
    }

    public void clearAll() {
        logSeries.clear();
        pointSeries.clear();
        userSeries.clear();
    }

    private XYDataset createDataset() {

        logSeries = new XYSeries("Data");
        pointSeries = new XYSeries("Point");
        userSeries = new XYSeries("User");
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(logSeries);
        dataset.addSeries(pointSeries);
        dataset.addSeries(userSeries);

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
        //
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesShape(1, new Rectangle(7, 7));
        renderer.setSeriesStroke(1, new BasicStroke(0));
        renderer.setSeriesShapesVisible(1, true);
        //
        renderer.setSeriesPaint(2, Color.GREEN);
        renderer.setSeriesShape(2, new Rectangle(7, 7));
        renderer.setSeriesStroke(2, new BasicStroke(0));
        renderer.setSeriesShapesVisible(2, true);
//        renderer.setDefaultShapesVisible(false);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);
        return chart;

    }

    @Override
    public void setSize(int width, int height) {
        Dimension sourceDimention = new Dimension(width - 6, height / 5);
        Dimension dimension = new Dimension(width - 6, height - 14 - sourceDimention.height);
        chartPanel.setSize(dimension);
        chartPanel.setPreferredSize(dimension);
        chartPanel.setMinimumSize(dimension);
        chartPanel.setMaximumSize(dimension);
        uiPanel.setPreferredSize(sourceDimention);
        uiPanel.setMinimumSize(sourceDimention);
        uiPanel.setMaximumSize(sourceDimention);
        super.setSize(width, height);
    }
}
