package de.tuberlin.ise.monitoring.monitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.tuberlin.ise.monitoring.datastructures.TimestampedValue;
import de.tuberlin.ise.monitoring.generators.Distribution;
import de.tuberlin.ise.monitoring.generators.LocalGenerator;

public class MockMonitor {

	private Queue<TimestampedValue<Double>> q;

	public MockMonitor(Date startTime, Date endTime, Distribution distribution) {
		LocalGenerator gen = new LocalGenerator();
		gen.generate(startTime, endTime, 500, distribution);
		q = gen.getMetricsQueue();
	}

	public List<TimestampedValue<Double>> getMetrics(Date startTime,
			Date endTime) {
		List<TimestampedValue<Double>> result = new ArrayList<TimestampedValue<Double>>();
		Queue<TimestampedValue<Double>> tempQ = new LinkedList<>();

		while (q.peek() != null) {
			TimestampedValue<Double> temp = q.poll();
			if (temp.getDate().before(startTime)
					|| temp.getDate().after(endTime)) {
				tempQ.add(temp);
			} else {
				result.add(temp);
			}
		}
		Collections.sort(result);
		q = tempQ;

		return result;
	}
}
