/**
 * 
 */
package de.tuberlin.ise.dbe.metrics.consumers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import de.tuberlin.ise.dbe.metrics.AbstractMetricConsumer;
import de.tuberlin.ise.dbe.metrics.MetricEvent;
import de.tuberlin.ise.dbe.metrics.MetricMonitor;
import de.tuberlin.ise.monitoring.datastructures.TimestampedValue;
import de.tuberlin.ise.monitoring.generators.Distribution;
import de.tuberlin.ise.monitoring.monitors.MockMonitor;

/**
 * @author Dave
 *
 */
public class MockMonitorConsumer extends AbstractMetricConsumer<Double> {
	
	private final int SIMULATION_DURATION_IN_SECONDS = 3*60;

	/** asserts that values for each time range are only returned once */
	private long latestReturnedTimestamp = new Date().getTime();

	/** generates the metric input */
	private MockMonitor mockMonitor;

	/**
	 * @param pollInterval
	 */
	public MockMonitorConsumer(long pollInterval, Distribution distribution,
			MetricMonitor monitor) {
		super(pollInterval, Arrays.asList("localgenerator_output"), monitor);
		this.mockMonitor = new MockMonitor(new Date(), new Date(
				new Date().getTime() + SIMULATION_DURATION_IN_SECONDS * 1000), distribution);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.tuberlin.ise.dbe.metrics.AbstractMetricConsumer#pollMetricProducer()
	 */
	@Override
	protected Collection<MetricEvent<Double>> pollMetricProducer() {
		List<MetricEvent<Double>> result = new ArrayList<>();
		long end = System.currentTimeMillis();
		List<TimestampedValue<Double>> resp = mockMonitor.getMetrics(new Date(
				latestReturnedTimestamp), new Date(end));
		for (TimestampedValue<Double> val : resp)
			result.add(new MetricEvent<Double>(val.getDate().getTime(), val
					.getValue(), super
					.getMetricIdForDescription("localgenerator_output")));
		latestReturnedTimestamp = end;
		System.out.println("Received Events: " + result);
		return result;
	}
}
