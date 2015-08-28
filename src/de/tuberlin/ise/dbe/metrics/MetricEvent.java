/**
 * 
 */
package de.tuberlin.ise.dbe.metrics;

/**
 * captures timestamp-value tuples
 * 
 * @author Dave
 *
 */
public class MetricEvent<T extends Number> implements Comparable<MetricEvent<T>>{

	/**timestamp of the event*/
	public final long timestamp;
	
	/** actual value for the metric*/
	public final T metricValue;

	/**identifies the metric this event belongs to*/
	public final int metricId;
	
	

	/**
	 * @param timestamp
	 * @param metricValue
	 * @param metricId
	 */
	public MetricEvent(long timestamp, T metricValue, int metricId) {
		super();
		this.timestamp = timestamp;
		this.metricValue = metricValue;
		this.metricId = metricId;
	}



	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MetricEvent<T> o) {
		return new Long(this.timestamp).compareTo(o.timestamp);
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MetricEvent [timestamp=" + this.timestamp + ", metricValue="
				+ this.metricValue + ", metricId=" + this.metricId + "]";
	}
	
	
	
}
