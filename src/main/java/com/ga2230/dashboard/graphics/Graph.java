package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Broadcaster;
import com.ga2230.dashboard.communications.Communicator;
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

    public Graph() {
        XYDataset dataset = createDataset();
        chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.white);
        add(chartPanel);
        Communicator.pullListener.listen(thing -> {
            series.add(time, thing.getJSONObject("modules").getJSONObject("robotcdrive").getInt("left_encoder"));
            time++;
        });
    }

    private XYDataset createDataset() {

        series = new XYSeries("Values");
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "",
                "",
                "",
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
        Dimension dimension = new Dimension(width, height);
        chartPanel.setSize(dimension);
        chartPanel.setPreferredSize(dimension);
        chartPanel.setMinimumSize(dimension);
        chartPanel.setMaximumSize(dimension);
        super.setSize(width, height);
    }
}
