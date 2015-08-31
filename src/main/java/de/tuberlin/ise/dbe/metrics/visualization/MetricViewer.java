/**
 * 
 */
package de.tuberlin.ise.dbe.metrics.visualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author Dave
 *
 */
public class MetricViewer {

	/** the time series of the original metric */
	private final XYSeries metricValues;

	/** the time series of the corresponding score values */
	private final XYSeries scoreValues;

	/** the chart itself */
	private final JFreeChart chart;

	/** the frame containing the chart */
	private final ChartFrame frame;
	
	private long start = -1;

	public MetricViewer(String windowTitle, String metricName, String scoreName) {
		metricValues = new XYSeries(metricName);
		scoreValues = new XYSeries(scoreName);
		XYSeriesCollection collection = new XYSeriesCollection();
		collection.addSeries(metricValues);
		collection.addSeries(scoreValues);
		chart = ChartFactory.createXYLineChart(null, "time [s]", null, collection);
		frame = new ChartFrame(windowTitle, chart);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * adds a value set for plotting
	 * 
	 * @param time
	 * @param metric
	 * @param score
	 */
	public void addValue(long time, double metric, int score) {
		if(start==-1){
			start = time;
		}
		metricValues.add((time-start)/1000.0, metric);
		scoreValues.add((time-start)/1000.0, score);
	}

	
}
