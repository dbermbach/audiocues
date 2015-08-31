package de.tuberlin.ise.monitoring;

import java.util.Date;
import java.util.List;

import de.tuberlin.ise.monitoring.datastructures.TimestampedValue;

public abstract class AbstractMonitor {


	public abstract List<TimestampedValue<Double>> getMetrics(Date startTime,
			Date endTime);
			
}
