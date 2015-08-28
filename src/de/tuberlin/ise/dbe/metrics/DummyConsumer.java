/**
 * 
 */
package de.tuberlin.ise.dbe.metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Dave
 *
 */
public class DummyConsumer extends AbstractMetricConsumer<Double> {

	/**
	 * @param pollInterval
	 */
	public DummyConsumer(long pollInterval, MetricMonitor monitor) {
		super(pollInterval, Arrays.asList("dummy"), monitor);
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
				System.currentTimeMillis(), Math.random() < 0.5 ? 0
						: Math.random() * 100,
				super.getMetricIdForDescription("dummy"));
		Collection<MetricEvent<Double>> result = new ArrayList<MetricEvent<Double>>();
		result.add(event);
		System.out.println("next one:" + event);
		return result;
	}

}
