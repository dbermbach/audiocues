package de.tuberlin.ise.monitoring.monitors;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import de.tuberlin.ise.monitoring.datastructures.TimestampedValue;
import de.tuberlin.ise.monitoring.generators.Distribution;

public class MonitorTest {
	public static void main(String[] args) throws ParseException {
		//CloudWatchMonitor cwm = new CloudWatchMonitor();


		Date endTime = new Date();
		Date startTime = new Date(endTime.getTime() - 3 *60* 1000); // 1 minute ago
		
		MockMonitor mm = new MockMonitor(startTime, endTime, Distribution.SIM_CHAINED);

		// List<TimestampedValue<Double>> data = cwm.getEC2Metric(
		// EC2Metrics.CPU_UTIL, "i-d1205f10", startTime, endTime);

		
		List<TimestampedValue<Double>> data = mm.getMetrics(new Date(endTime.getTime() - 3 *60* 1000), endTime);

		for (TimestampedValue<Double> d : data) {
			System.out.println(d);
		}
	}
}
