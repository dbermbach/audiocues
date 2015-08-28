/**
 * 
 */
package de.tuberlin.ise.dbe.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * this class polls metric values and delivers them in-order to the
 * {@link MetricMonitor}
 * 
 * @author Dave
 *
 */
public abstract class AbstractMetricConsumer<T extends Number> implements
		Runnable {

	/** holds all metricId-description pairs this consumer monitors */
	private final Map<Integer, String> ownMetrics;

	/**
	 * all metric events which have been polled from the producer and that are
	 * waiting for retrieval by the {@link MetricMonitor}
	 */
	private Queue<MetricEvent<T>> waitingEvents = new PriorityBlockingQueue<MetricEvent<T>>();

	/**
	 * highest timestamp that was already forwarded, asserts the at-most-once
	 * delivery of items. Key is the metric ID, value the timestamp.
	 */
	private Map<Integer, Long> highestPublishedTimestamps = new HashMap<Integer, Long>();

	/** interval between poll calls in ms */
	private long pollInterval = 1000;

	/** will terminate if set to false */
	private volatile boolean running = true;

	/**
	 * @param pollInterval
	 */
	public AbstractMetricConsumer(long pollInterval,
			List<String> metricDescriptions, MetricMonitor monitor) {
		super();
		this.pollInterval = pollInterval;
		ownMetrics = new HashMap<>();
		for (String descr : metricDescriptions) {
			ownMetrics.put(monitor.registerNewMetric(descr), descr);
		}
	}

	/**
	 * terminates this metric consumer
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
		Collection<MetricEvent<T>> nextValues;
		Long highestTimestamp;
		while (!Thread.currentThread().isInterrupted() && running) {

			nextValues = pollMetricProducer();
			for (MetricEvent<T> event : nextValues) {
				highestTimestamp = highestPublishedTimestamps
						.get(event.metricId);
				if (highestTimestamp == null
						|| highestTimestamp < event.timestamp) {

					highestPublishedTimestamps.put(event.metricId,
							event.timestamp);

					waitingEvents.add(event);

				}

			}
			try {
				Thread.sleep(pollInterval);
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("Metric Consumer " + this.getClass().getSimpleName()
				+ " terminated.");
	}

	/**
	 * 
	 * @return a chronologically ordered sequence of metric events that have not
	 *         been returned before.
	 */
	public List<MetricEvent<? extends Number>> getMetricEvents() {
		List<MetricEvent<? extends Number>> result = new ArrayList<MetricEvent<? extends Number>>(
				waitingEvents.size());
		result.addAll(waitingEvents);
		waitingEvents.removeAll(result);
		return result;
	}

	/**
	 * polls the corresponding metric producer and returns all retrieved metric
	 * events. May poll several metric producer at once as long as the metric
	 * IDs differ.
	 * 
	 * @return a collection of {@link MetricEvent}s
	 */
	protected abstract Collection<MetricEvent<T>> pollMetricProducer();

	/**
	 * @return the ownMetrics
	 */
	public Map<Integer, String> getOwnMetrics() {
		return new HashMap<>(this.ownMetrics);
	}

	/**
	 * 
	 * @param descr
	 * @return the corresponding id for descr if such a mapping exists (null
	 *         otherwise)
	 */
	public Integer getMetricIdForDescription(String descr) {
		for (Entry<Integer, String> entry : ownMetrics.entrySet()) {
			if (entry.getValue().equals(descr))
				return entry.getKey();
		}
		return null;
	}

}
