/**
 * 
 */
package de.tuberlin.ise.dbe.metrics.analyzers;

import de.tuberlin.ise.dbe.metrics.MetricEvent;
import de.tuberlin.ise.dbe.metrics.MetricEventAnalyzer;

/**
 * 
 * 
 * 
 * @author Dave
 *
 */
public class LinearMetricEventAnalyzer extends MetricEventAnalyzer {
	/**
	 * the metric value which shall be mapped to a criticality score of 0
	 */
	private final double lower;
	/**
	 * the metric value which shall be mapped to a criticality score of 100
	 */
	private final double upper;

	/**
	 * @param lower
	 *            the metric value which shall be mapped to a criticality score
	 *            of 0
	 * @param upper
	 *            the metric value which shall be mapped to a criticality score
	 *            of 100
	 */
	public LinearMetricEventAnalyzer(double lower, double upper) {
		super();
		this.lower = lower;
		this.upper = upper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.tuberlin.ise.dbe.metrics.MetricEventAnalyzer#assessCriticality(de.
	 * tuberlin.ise.dbe.metrics.MetricEvent)
	 */
	@Override
	protected int assessCriticality(MetricEvent<? extends Number> event) {
		double width = Math.abs(upper - lower), value = event.metricValue
				.doubleValue(), diff = event.metricValue.doubleValue() - lower;
		if (lower < upper) {
			if (value <= lower)
				return 0;
			else if (value >= upper)
				return 100;
			else
				return (int) Math.round(100 * diff / width);
		} else if (upper < lower) {
			if (value >= lower)
				return 0;
			else if (value <= upper)
				return 100;
			else
				return (int) Math.round(-100 * diff / width);
		} else
			throw new RuntimeException(
					"Upper and lower criticality threshold must not be identical");

	}

}
