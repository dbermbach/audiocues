/**
 * 
 */
package de.tuberlin.ise.dbe.audiocues;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

import de.tuberlin.ise.dbe.metrics.MetricMonitor;
import de.tuberlin.ise.dbe.metrics.analyzers.LinearMetricEventAnalyzer;
import de.tuberlin.ise.dbe.metrics.consumers.MockMonitorConsumer;
import de.tuberlin.ise.dbe.midi.instruments.Instruments;
import de.tuberlin.ise.monitoring.generators.Distribution;

/**
 * @author Dave
 *
 */
public class AudioCueStarter {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Select midi device:
		MidiDevice device = determineMidiDevice();

		// Initiate and start playback on the selected device.
		MetricMonitor metricMonitor = new MetricMonitor();
		metricMonitor.addAnalyzer(0, new LinearMetricEventAnalyzer(-10, 100));
		// metricMonitor.addAnalyzer(0, new QuadraticMetricEventAnalyzer(-10,
		// 100));
		// metricMonitor.addMetricConsumer(new BinaryMetricConsumer(5000,
		// true, metricMonitor));
		metricMonitor.addMetricConsumer(new MockMonitorConsumer(1000,
				Distribution.SIM_CHAINED, metricMonitor));
		metricMonitor.start();

		MusicGraphNode startNode = SampleMusicGraphBuilderDeterministic
				.buildGraph();
		AudioCuePlayer player = new AudioCuePlayer(2, startNode, 138,
				metricMonitor, device);
		player.changeInstrument(0, Instruments.Synth.Pad_2_warm);
		player.changeInstrument(1, Instruments.Synth.Lead_8_bass);
		player.changeInstrument(2, Instruments.Guitars.Electric_Guitar_muted);
		player.changeInstrument(3, Instruments.Pianos.Acoustic_Grand_Piano);
		new Thread(player).start();
	}

	private static MidiDevice determineMidiDevice()
			throws MidiUnavailableException, IOException {
		MidiDevice result = null;

		// generate list of compatible devices
		List<MidiDevice> deviceList = new ArrayList<>();
		Info[] info = MidiSystem.getMidiDeviceInfo();
		for (Info i : info) {
			MidiDevice device = MidiSystem.getMidiDevice(i);
			if (!(device instanceof Sequencer)
					&& !(device.getMaxReceivers() == 0)) {
				// we're now sure that device represents a MIDI port with a
				// receiver or a synthesizer
				deviceList.add(device);
			}
		}

		// List compatible devices
		int counter = 1;
		System.out.println("Compatible Devices:");
		System.out.println("--------------------------------------");
		for (MidiDevice dev : deviceList) {
			Info i = dev.getDeviceInfo();
			System.out.println("Device Id: " + counter++);
			System.out.println("Device Name: " + i.getName());
			System.out.println("Description: " + i.getDescription());
			System.out.println("Vendor: " + i.getVendor());
			System.out.println("--------------------------------------");
		}
		// Request user input (device id) and return the device thereby
		// identified
		Scanner in = new Scanner(System.in);
		System.out
				.print("Please enter the Device Id of the device you want to use: ");
		int id = in.nextInt();
		in.close();
		try {
			result = deviceList.get(id - 1);
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Invalid Device Id.");
			System.exit(0);
		}
		return result;
	}

}
