package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Broadcaster;
import com.ga2230.dashboard.communications.Communicator;
import com.ga2230.dashboard.util.ModuleHelper;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class Graph extends Panel {

    private double time = 0.0;

    private XYDataset dataset;
    private XYSeries series;
    private JFreeChart chart;
    private ChartPanel chartPanel;

    private JSONObject full = new JSONObject();
    private String coordinates = "";

    public Graph() {
        XYDataset dataset = createDataset();
        chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.white);
        setBackground(Color.ORANGE);
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
        Double value = ModuleHelper.getDouble(coordinates, full);
        if (value != null)
            addValue(value);
        time++;
    }

    public void addValue(double value) {
        series.add(time, value);
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
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

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
        Dimension dimension = new Dimension(width - 6, height - 10);
        chartPanel.setSize(dimension);
        chartPanel.setPreferredSize(dimension);
        chartPanel.setMinimumSize(dimension);
        chartPanel.setMaximumSize(dimension);
        super.setSize(width, height);
    }
}
