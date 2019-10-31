package com.ga2230.dashboard.graphics;

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

public class Graph extends Panel {

    private XYDataset dataset;
    private XYSeries series;
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private JPanel sourcePanel;
    private JTextField ySource, xSource;

    private JSONObject full = new JSONObject();
    private String yCoordinates = "pull->zero";
    private String xCoordinates = "pull->time";

    public Graph() {
        ySource = new JTextField(yCoordinates);
        xSource = new JTextField(xCoordinates);
        ySource.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                yCoordinates = ySource.getText();
                clearChart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                yCoordinates = ySource.getText();
                clearChart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                yCoordinates = ySource.getText();
                clearChart();
            }
        });
        xSource.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                xCoordinates = xSource.getText();
                clearChart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                xCoordinates = xSource.getText();
                clearChart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                xCoordinates = xSource.getText();
                clearChart();
            }
        });
        chart = createChart(createDataset());
        chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.white);
        sourcePanel = new JPanel(new GridLayout(1, 2));
        setBackground(Color.ORANGE);
        sourcePanel.add(ySource);
        sourcePanel.add(xSource);
        add(sourcePanel);
        add(chartPanel);
        Communicator.pullListener.listen(thing -> {
            full.put("pull", thing);
            update();
        });
        Communicator.pushListener.listen(thing -> {
            full.put("push", thing);
            update();
        });
    }

    private void update() {
        series.add(ModuleHelper.getDouble(xCoordinates, full), ModuleHelper.getDouble(yCoordinates, full));
    }

    public void clearChart() {
        series.clear();
    }

    private XYDataset createDataset() {

        series = new XYSeries("Values");
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

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
        renderer.setDefaultShapesVisible(false);

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
        Dimension sourceDimention = new Dimension(width - 6, height / 8);
        Dimension dimension = new Dimension(width - 6, height - 14 - sourceDimention.height);
        chartPanel.setSize(dimension);
        chartPanel.setPreferredSize(dimension);
        chartPanel.setMinimumSize(dimension);
        chartPanel.setMaximumSize(dimension);
        sourcePanel.setPreferredSize(sourceDimention);
        sourcePanel.setMinimumSize(sourceDimention);
        sourcePanel.setMaximumSize(sourceDimention);
        super.setSize(width, height);
    }
}
