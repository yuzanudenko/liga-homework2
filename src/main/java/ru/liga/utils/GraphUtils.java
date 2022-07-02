package ru.liga.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import ru.liga.model.Currency;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static ru.liga.config.Constants.GRAPH_FORMATTER;

public class GraphUtils {

    private final TimeSeriesCollection dataset;

    /**
     * Creates a new demo instance.
     */
    public GraphUtils() {
        this.dataset = new TimeSeriesCollection();
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset the dataset.
     * @return The chart.
     */
    private JFreeChart createChart(TimeSeriesCollection dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                null,       // title
                "Next days",     // x-axis label
                "RUB",           // y-axis label
                dataset          // data
        );

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint(Color.gray);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(false);

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(GRAPH_FORMATTER);

        XYLineAndShapeRenderer rend = (XYLineAndShapeRenderer) plot.getRenderer();
        rend.setDefaultShapesVisible(true);
        rend.setDefaultShapesFilled(true);
        rend.setDrawSeriesLineAsPath(true);
        return chart;
    }

    /**
     * Returns a sample dataset.
     */
    public void setData(List<Currency> currencyList, String currencyName) {
        TimeSeries series = new TimeSeries(currencyName);

        for (Currency cur : currencyList) {
            LocalDate date = cur.getDate();
            series.add(new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear()), cur.getRate());
        }

        dataset.addSeries(series);
    }

    public File getCurrencyRatesAsGraph() {
        JFreeChart chart = createChart(dataset);
        File lineChart = new File("lineChart.png");
        try {
            ImageIO.write(chart.createBufferedImage(1500, 750), "png", lineChart);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lineChart;
    }
}