package de.tuberlin.ise.monitoring.monitors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;

import de.tuberlin.ise.monitoring.datastructures.TimestampedValue;

public class CloudWatchMonitor {

	AmazonCloudWatchClient cloudWatchClient = new AmazonCloudWatchClient();

	private List<TimestampedValue<Double>> getMetric(String namespace,
			String metricName, Dimension dim, Date startTime, Date endTime) {
		List<TimestampedValue<Double>> result = new ArrayList<TimestampedValue<Double>>();

		cloudWatchClient.withRegion(Regions.EU_CENTRAL_1); // Frankfurt

		ArrayList<String> statistics = new ArrayList<String>();
		statistics.add("Average");

		GetMetricStatisticsRequest metricReq = new GetMetricStatisticsRequest();
		metricReq.withNamespace(namespace).withMetricName(metricName)
				.withStartTime(startTime).withEndTime(endTime)
				.withStatistics(statistics).withDimensions().withPeriod(60);

		GetMetricStatisticsResult metricRes = cloudWatchClient
				.getMetricStatistics(metricReq);

		List<Datapoint> datapoints = metricRes.getDatapoints();
		for (Datapoint datapoint : datapoints) {
			result.add(new TimestampedValue<Double>(datapoint.getTimestamp(),
					datapoint.getAverage()));
		}

		return result;
	}

	/**
	 * Returns value average over 60 seconds of the specified metric in the time
	 * interval specified
	 */
	public List<TimestampedValue<Double>> getEC2Metric(EC2Metrics metric,
			String instanceId, long startTime, long endTime) {
		List<TimestampedValue<Double>> result = null;

		Dimension dim = new Dimension();
		dim.withName("InstanceId").withValue(instanceId);

		String apiString = "";
		switch (metric) {
		case CPU_UTIL: {
			apiString = "CPUUtilization";
			break;
		}
		case CPU_CREDIT_BALANCE:{
			apiString = "CPUCreditBalance";
			break;
		}
		case CPU_CREDIT_USAGE:{
			apiString = "CPUCreditUsage";
			break;
		}
		case DISK_WRITE_OPS:{
			apiString = "DiskWriteOps";
			break;
		}
		case DISK_WRITE_BYTES:{
			apiString = "DiskWriteBytes";
			break;
		}
		case DISK_READ_OPS:{
			apiString = "DiskReadOps";
			break;
		}
		case DISK_READ_BYTES:{
			apiString = "DiskReadBytes";
			break;
		}
		case NETWORK_IN:{
			apiString = "NetworkIn";
			break;
		}
		case NETWORK_OUT:{
			apiString = "NetworkOut";
			break;
		}
		case STATUS_CHECK_FAILED:{
			apiString = "StatusCheckFailed";
			break;
		}
		case STATUS_CHECK_FAILED_INSTANCE:{
			apiString = "StatusCheckFailed_Instance";
			break;
		}
		case STATUS_CHECK_FAILED_SYSTEM:{
			apiString = "StatusCheckFailed_System";
			break;
		}
		default:
			new Exception("Should never have happened");
		}

		result = getMetric("AWS/EC2", apiString, dim, new Date(startTime), new Date(endTime));

		return result;
	}
}
