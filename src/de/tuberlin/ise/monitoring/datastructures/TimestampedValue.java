package de.tuberlin.ise.monitoring.datastructures;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/** utility class representing (timestamp,value) pairs */
public class TimestampedValue<T> implements Comparable<Object> {
	private Date date;
	private T value;

	public TimestampedValue(Date date, T value) {
		this.date = date;
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public String toString() {
		DateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		return "(" + formatter.format(date) + " - " + value + ")";
	}

	@SuppressWarnings("unchecked")
	@Override
	/** ordered by date, values are ignored*/
	public int compareTo(Object o) {
		long thisTime = this.date.getTime();
		long anotherTime = ((TimestampedValue<T>) o).getDate().getTime();
		return (thisTime < anotherTime ? -1 : (thisTime == anotherTime ? 0 : 1));
	}
}
