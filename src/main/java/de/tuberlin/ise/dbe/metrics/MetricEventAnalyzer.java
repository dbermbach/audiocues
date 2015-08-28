/**
 * 
 */
package de.tuberlin.ise.dbe.metrics;

/**
 * @author Dave
 *
 */
public abstract class MetricEventAnalyzer {

	/**
	 * determines based on a {@link MetricEvent} a score of criticality which is
	 * an int value from the interval [0;100] where 0 is uncritical and 100 is
	 * highly critical. Implementations may also consider past events in their
	 * analysis.
	 * 
	 * @param event
	 *            a {@link MetricEvent} which shall be analyzed
	 * @return an int value from the interval [0;100] describing the criticality
	 *         of that event
	 */
	protected abstract int assessCriticality(MetricEvent<? extends Number> event);

}
