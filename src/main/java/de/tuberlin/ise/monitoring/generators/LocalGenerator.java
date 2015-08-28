package de.tuberlin.ise.monitoring.generators;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import de.tuberlin.ise.monitoring.datastructures.TimestampedValue;

public class LocalGenerator {

	private Queue<TimestampedValue<Double>> metricsQueue;
	private Thread generatorThread = null;
	private Date startTime;
	private Date endTime;
	private int intervalInMilliseconds;
	private Distribution distribution;

	public LocalGenerator() {
		metricsQueue = new LinkedList<>();
	}

	public LocalGenerator(Queue<TimestampedValue<Double>> q) {
		metricsQueue = q;
	}

	public void generate(Date startTime, Date endTime,
			int intervalInMilliseconds, Distribution distribution) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.intervalInMilliseconds = intervalInMilliseconds;
		this.distribution = distribution;
		startGeneration();
		try {
			generatorThread.join();
		} catch (InterruptedException e) {
			generatorThread.interrupt();
			Thread.currentThread().interrupt();
		}

	}

	public Queue<TimestampedValue<Double>> getMetricsQueue() {
		return metricsQueue;
	}

	private void startGeneration() {
		Runnable r = null;
		if (generatorThread == null) {
			switch (distribution) {
			case EQUAL: {
				r = new EqualDistributionGenerator();
				break;
			}
			case NORMAL: {
				r = new NormalDistributionGenerator();
				break;
			}
			case SINE: {
				r = new SineDistributionGenerator();
				break;
			}
			case SIM1: {
				r = new Simulation1Generator();
				break;
			}
			case SIM2: {
				r = new Simulation2Generator();
				break;
			}
			case SIM3: {
				r = new Simulation3Generator();
				break;
			}
			case SIM_CHAINED: {
				r = new SimulationChainGenerator();
				break;
			}
			default: {
				throw new RuntimeException("Should never happen!");
			}
			}
		}
		generatorThread = new Thread(r);
		generatorThread.start();
	}

	// This class' run method generates equally distributed random metric values
	// in interval [0,1) and puts them into
	// the metricsQueue.
	class EqualDistributionGenerator implements Runnable {

		@Override
		public void run() {
			long current = startTime.getTime();
			while (current < endTime.getTime()) {
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), Math.random()));
				current += intervalInMilliseconds;
			}
		}
	}

	// This class' run method generates normally distributed random metric
	// values with mean 0 and standard deviation 1. Values are put into
	// the metricsQueue.
	class NormalDistributionGenerator implements Runnable {

		@Override
		public void run() {
			Random rand = new Random();
			long current = startTime.getTime();
			while (current < endTime.getTime()) {
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), rand.nextGaussian()));
				current += intervalInMilliseconds;
			}
		}
	}

	// This class' run method generates values [-1,1] based on a sine function.
	// Values are put into
	// the metricsQueue.
	class SineDistributionGenerator implements Runnable {

		@Override
		public void run() {
			long current = startTime.getTime();
			while (current < endTime.getTime()) {
				double normalizedInput = ((current - (double) startTime
						.getTime())
						/ (double) (endTime.getTime() - startTime.getTime())
						* Math.PI * 2);
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), Math.sin(normalizedInput)));
				current += intervalInMilliseconds;
			}
		}
	}

	class Simulation1Generator implements Runnable {

		@Override
		public void run() {
			long current = startTime.getTime();
			long timeinterval = endTime.getTime() - startTime.getTime();
			long sectionCount = 5;
			long sectionlength = timeinterval / sectionCount;
			// section 1
			while (current < startTime.getTime() + 1 * sectionlength) {
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), -10d));
				current += intervalInMilliseconds;
			}
			// section 2
			while (current < startTime.getTime() + 2 * sectionlength) {
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), 60d));
				current += intervalInMilliseconds;
			}
			// section 3
			while (current < startTime.getTime() + 3 * sectionlength) {
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), 30d));
				current += intervalInMilliseconds;
			}
			// section 4
			while (current < startTime.getTime() + 4 * sectionlength) {
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), 110d));
				current += intervalInMilliseconds;
			}
			// section 5
			while (current < endTime.getTime()) {
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), 0d));
				current += intervalInMilliseconds;
			}
		}
	}

	class Simulation2Generator implements Runnable {

		@Override
		public void run() {
			long timeinterval = endTime.getTime() - startTime.getTime();
			long current = startTime.getTime();

			while (current < endTime.getTime()) {
				double x = (double) (current - startTime.getTime())
						/ (double) timeinterval * 5; // normalization to [0,5]
				// -21.25x^4 + 162.5x3 - 388.75x2 + 317.5x - 10
				double y = -21.25 * Math.pow(x, 4) + 162.5 * Math.pow(x, 3)
						- 388.75 * Math.pow(x, 2) + 317.5 * x - 10;
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), y));
				current += intervalInMilliseconds;
			}
		}
	}

	class Simulation3Generator implements Runnable {

		@Override
		public void run() {
			long timeinterval = endTime.getTime() - startTime.getTime();
			long current = startTime.getTime();

			while (current < endTime.getTime()) {
				double x = (double) (current - startTime.getTime())
						/ (double) timeinterval * 5; // normalization to [0,5]
				// -7.5x3 + 27.143x2 + 13.929x - 2.7143
				double y = -7.5 * Math.pow(x, 3) + 27.143 * Math.pow(x, 2)
						+ 13.929 * x - 2.7143;
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), y));
				current += intervalInMilliseconds;
			}
		}
	}

	// chains simulations 1-3 with a short break in between.
	class SimulationChainGenerator implements Runnable {

		@Override
		public void run() {
			long current = startTime.getTime();
			long timeinterval = endTime.getTime() - startTime.getTime();
			long simulationlength = timeinterval / 4;
			long pauselength = timeinterval / 8;
			long sectionCount = 5;
			long sectionlength = simulationlength / sectionCount;

			// Simulation 1
			// section 1
			System.out.println("LocalGenerator: Simulation 1 (" + Math.round(simulationlength/1000) + "s)");
			while (current < startTime.getTime() + 1 * sectionlength) {
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), -10d));
				current += intervalInMilliseconds;
			}
			// section 2
			while (current < startTime.getTime() + 2 * sectionlength) {
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), 60d));
				current += intervalInMilliseconds;
			}
			// section 3
			while (current < startTime.getTime() + 3 * sectionlength) {
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), 30d));
				current += intervalInMilliseconds;
			}
			// section 4
			while (current < startTime.getTime() + 4 * sectionlength) {
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), 110d));
				current += intervalInMilliseconds;
			}
			// section 5
			while (current < startTime.getTime() + 5 * sectionlength) {
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), 0d));
				current += intervalInMilliseconds;
			}

			// Pause 1 of 2
			System.out.println("LocalGenerator: Pause 1 (" + Math.round(pauselength/1000) + "s)");
			while (current < startTime.getTime() + simulationlength
					+ pauselength) {
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), 0d));
				current += intervalInMilliseconds;
			}

			// Simulation 2
			System.out.println("LocalGenerator: Simulation 2 (" + Math.round(simulationlength/1000) + "s)");
			while (current < startTime.getTime() + 2 * simulationlength
					+ pauselength) {
				double x = (double) (current - startTime.getTime() + 2
						* simulationlength + pauselength)
						/ (double) simulationlength * 5; // normalization to
															// [0,5]
				// -21.25x^4 + 162.5x3 - 388.75x2 + 317.5x - 10
				double y = -21.25 * Math.pow(x, 4) + 162.5 * Math.pow(x, 3)
						- 388.75 * Math.pow(x, 2) + 317.5 * x - 10;
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), y));
				current += intervalInMilliseconds;
			}

			// Pause 2 of 2
			System.out.println("LocalGenerator: Pause 2 (" + Math.round(pauselength/1000) + "s)");
			while (current < startTime.getTime() + 2 * simulationlength + 2
					* pauselength) {
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), 0d));
				current += intervalInMilliseconds;
			}

			// Simulation 3
			System.out.println("LocalGenerator: Simulation 3 (" + Math.round(simulationlength/1000) + "s)");
			while (current < endTime.getTime()) {
				double x = (double) (current - startTime.getTime() + 2
						* simulationlength + 2 * pauselength)
						/ (double) simulationlength * 5; // normalization to [0,5]
				// -7.5x3 + 27.143x2 + 13.929x - 2.7143
				double y = -7.5 * Math.pow(x, 3) + 27.143 * Math.pow(x, 2)
						+ 13.929 * x - 2.7143;
				metricsQueue.add(new TimestampedValue<Double>(
						new Date(current), y));
				current += intervalInMilliseconds;
			}

		}
	}

}