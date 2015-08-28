package de.tuberlin.ise.monitoring.monitors;

import java.util.ArrayList;
import java.util.List;

/** Supported Metrics */
public enum EC2Metrics {
	CPU_UTIL, CPU_CREDIT_USAGE, CPU_CREDIT_BALANCE, DISK_WRITE_OPS, DISK_WRITE_BYTES, DISK_READ_OPS, DISK_READ_BYTES, NETWORK_IN, NETWORK_OUT, STATUS_CHECK_FAILED, STATUS_CHECK_FAILED_INSTANCE, STATUS_CHECK_FAILED_SYSTEM;

	/**
	 * @param prefix
	 * @return a list of string containing the result of this.values() using the
	 *         parameter prefix as prefix of each entry
	 */
	public static List<String> getStringValuesWithPrefix(String prefix) {
		List<String> res = new ArrayList<String>();
		for (EC2Metrics m : values())
			res.add(prefix + m);
		return res;
	}
}
