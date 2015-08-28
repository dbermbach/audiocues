/**
 * 
 */
package de.tuberlin.ise.dbe.metrics.analyzers;

import de.tuberlin.ise.dbe.metrics.MetricEvent;
import de.tuberlin.ise.dbe.metrics.MetricEventAnalyzer;

/**
 * interpolates between upper and lower by using a function A*x² + B so that
 * f(lower)=0 and f(upper)=100
 * 
 * 
 * @author Dave
 *
 */
public class QuadraticMetricEventAnalyzer extends MetricEventAnalyzer {
	/**
	 * the metric value which shall be mapped to a criticality score of 0
	 */
	private final double lower;
	/**
	 * the metric value which shall be mapped to a criticality score of 100
	 */
	private final double upper;

	/** prefactor A in A*x² + B */
	private final double prefactorA;
	/** prefactor B in A*x² + B */
	private final double prefactorB;

	/**
	 * @param lower
	 *            the metric value which shall be mapped to a criticality score
	 *            of 0
	 * @param upper
	 *            the metric value which shall be mapped to a criticality score
	 *            of 100
	 */
	public QuadraticMetricEventAnalyzer(double lower, double upper) {
		super();
		this.lower = lower;
		this.upper = upper;
		prefactorA = -100 / (lower * lower - upper * upper);
		prefactorB = -1 * prefactorA * lower * lower;
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
		double value = event.metricValue.doubleValue();
		int res = (int) Math.round(prefactorA * value * value + prefactorB);
		if (res < 0)
			return 0;
		if (res > 100)
			return 100;
		return res;
	}

	
}
