/**
 * 
 */
package de.tuberlin.ise.dbe.metrics.consumers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.amazonaws.regions.Region;

import de.tuberlin.ise.dbe.metrics.AbstractMetricConsumer;
import de.tuberlin.ise.dbe.metrics.MetricEvent;
import de.tuberlin.ise.dbe.metrics.MetricMonitor;
import de.tuberlin.ise.monitoring.datastructures.TimestampedValue;
import de.tuberlin.ise.monitoring.monitors.CloudWatchMonitor;
import de.tuberlin.ise.monitoring.monitors.EC2Metrics;

/**
 * @author Dave
 *
 */
public class CloudWatchEC2Consumer extends AbstractMetricConsumer<Double> {

	/** connection to aws cloud watch */
	private CloudWatchMonitor cloudWatch;

	/** id of the instance that is monitored */
	private String instanceId;

	/** region where the instance is running */
	private Region awsRegion;

	/** asserts that values for each time range are only returned once */
	private long latestReturnedTimestamp;

	/**
	 * @param pollInterval
	 */
	public CloudWatchEC2Consumer(long pollInterval, Region awsRegion,
			String instanceId, MetricMonitor monitor) {
		super(pollInterval, EC2Metrics.getStringValuesWithPrefix(awsRegion
				+ "." + instanceId + "."), monitor);
		cloudWatch = new CloudWatchMonitor();
		this.instanceId = instanceId;
		this.awsRegion = awsRegion;
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
		for (EC2Metrics metric : EC2Metrics.values()) {
			List<TimestampedValue<Double>> resp = cloudWatch.getEC2Metric(
					metric, instanceId, latestReturnedTimestamp, end);
			for (TimestampedValue<Double> val : resp)
				result.add(new MetricEvent<Double>(val.getDate().getTime(), val
						.getValue(), super.getMetricIdForDescription(awsRegion
						+ "." + instanceId + "." + metric)));
		}
		latestReturnedTimestamp = end;
		return result;
	}
}
