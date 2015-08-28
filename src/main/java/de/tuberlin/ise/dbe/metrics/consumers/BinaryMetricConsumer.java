/**
 * 
 */
package de.tuberlin.ise.dbe.metrics.consumers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import de.tuberlin.ise.dbe.metrics.AbstractMetricConsumer;
import de.tuberlin.ise.dbe.metrics.MetricEvent;
import de.tuberlin.ise.dbe.metrics.MetricMonitor;

/**
 * @author Dave
 *
 */
public class BinaryMetricConsumer  extends AbstractMetricConsumer<Double> {

	private boolean dissonanceOn;
	
	/**
	 * @param pollInterval
	 */
	public BinaryMetricConsumer(long pollInterval, boolean dissonanceOn, MetricMonitor monitor) {
		super(pollInterval,Arrays.asList("binary dummy"),monitor);
		this.dissonanceOn = dissonanceOn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.tuberlin.ise.dbe.metrics.AbstractMetricConsumer#pollMetricProducer()
	 */
	@Override
	protected Collection<MetricEvent<Double>> pollMetricProducer() {
		MetricEvent<Double> event = new MetricEvent<Double>(
				System.currentTimeMillis(), dissonanceOn ?100.0:0, super.getMetricIdForDescription("binary dummy"));
		Collection<MetricEvent<Double>> result = new ArrayList<MetricEvent<Double>>();
		result.add(event);
		System.out.println("produced event: " + event);
		return result; 
	}
}
