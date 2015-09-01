/**
 * 
 */
package de.tuberlin.ise.dbe.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.tuberlin.ise.dbe.metrics.visualization.MetricViewer;

/**
 * receives metric data (in-order, at-most-once delivery) from implementations
 * of {@link AbstractMetricConsumer}, analyzes that data and triggers actions
 * where necessary
 * 
 * @author Dave
 *
 */
public class MetricMonitor implements Runnable {

	/** can be retrieved by metric consumers */
	private static Integer nextUnusedMetricId = 0;

	/** holds a description for each metric Id */
	private final Map<Integer, String> metricDescriptions = new ConcurrentHashMap<>();

	/** holds the analyzers per metric id */
	private final Map<Integer, MetricEventAnalyzer> analyzers = new ConcurrentHashMap<>();

	/** all instances that retrieve measurement data from the monitoring system */
	private final List<AbstractMetricConsumer<? extends Number>> metricConsumers = new ArrayList<>();

	/** plots the current metric and score values */
	private final Map<Integer, MetricViewer> metricViewers = new ConcurrentHashMap<>();

	/**
	 * the current score for a given metric, i.e., key is metric id, value is a
	 * score
	 */
	private final Map<Integer, Integer> latestScore = new ConcurrentHashMap<>();

	/** thread pool */
	private final ExecutorService threadPool = Executors.newCachedThreadPool();

	/** terminates when set to false */
	private boolean running = true;

	/**
	 * starts this monitor
	 */
	public void start() {
		threadPool.submit(this);
	}

	/**
	 * terminates this monitor
	 */
	public void terminate() {
		running = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		while (running) {
			// will crash if no analyzers are there, but that's fine...
			int metricId = analyzers.entrySet().iterator().next().getKey();
			MetricEventAnalyzer analyzer = analyzers.get(metricId);
			int score;
			for (AbstractMetricConsumer<? extends Number> consumer : metricConsumers) {
				for (MetricEvent<? extends Number> event : consumer
						.getMetricEvents()) {
					if (event.metricId != metricId) {
						metricId = event.metricId;
						analyzer = analyzers.get(metricId);
					}
					if (analyzer != null) {
						score = analyzer.assessCriticality(event);
						latestScore.put(event.metricId, score);
						metricViewers.get(metricId).addValue(event.timestamp, event.metricValue.doubleValue(), score);
					}
				}
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * 
	 * @return the (current) maximum criticality score over all metrics
	 */
	public int getCurrentMaximumScore() {
		int max = 0;
		for (int val : latestScore.values()) {
			if (val > max)
				max = val;
		}
		System.out.println("current max score: " + max);
		return max;
	}

	/**
	 * adds an analyzer for the specified metric id
	 * 
	 * @param metricId
	 * @param analyzer
	 */
	public void addAnalyzer(Integer metricId, MetricEventAnalyzer analyzer) {
		this.analyzers.put(metricId, analyzer);
	}

	/**
	 * adds a new metric consumer
	 * 
	 * @param consumer
	 */
	public void addMetricConsumer(
			AbstractMetricConsumer<? extends Number> consumer) {
		this.metricConsumers.add(consumer);
		this.threadPool.submit(consumer);
	}

	/**
	 * registers a new metric
	 * 
	 * @param description
	 *            a human-readable description of the metric
	 * @return the corresponding metric id
	 */
	public int registerNewMetric(String description) {
		int id;
		synchronized (nextUnusedMetricId) {
			id = nextUnusedMetricId++;
		}
		metricDescriptions.put(id, description);
		MetricViewer viewer = new MetricViewer("MetricMonitor", description,
				description + "_score");
		metricViewers.put(id, viewer);

		System.out.println("Registered metric \"" + description
				+ "\" for metric id " + id);
		return id;
	}
}
