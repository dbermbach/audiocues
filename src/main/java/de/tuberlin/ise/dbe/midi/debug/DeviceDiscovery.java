package de.tuberlin.ise.dbe.midi.debug;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

// For debugging and testing purposes only.
public class DeviceDiscovery {

	private static MidiDevice device;

	/**
	 * 
	 * Prints a list of available midi devices
	 * 
	 * @param args
	 * @author Jacob
	 */
	public static void main(String[] args) {
		Info[] info = MidiSystem.getMidiDeviceInfo();
		for (Info i : info) {
			System.out.println("Device Name: " + i.getName());
			System.out.println("Description: " + i.getDescription());
			System.out.println("Vendor: " + i.getVendor());
			System.out.println("--------------------------------------");
		}

		try {
			device = MidiSystem.getMidiDevice(info[2]);

			if (!(device instanceof Sequencer)
					&& !(device instanceof Synthesizer)
					&& !(device.getMaxReceivers() == 0)) {
				// we're now sure that device represents a MIDI port with a
				// receiver
				if (!(device.isOpen())) {
					device.open();
				}
				Receiver r = device.getReceiver();

				ShortMessage myMsg = new ShortMessage();
				// Start playing the note Middle C (60),
				// moderately loud (velocity = 93).
				myMsg.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
				long timeStamp = -1;
				r.send(myMsg, timeStamp);
			} else {
				System.err
						.println("The device selected is not of the correct type.");
			}
		} catch (MidiUnavailableException e) {
			System.err.println("Unable to obtain midi device.");
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			System.err.println("Invalid Midi Message.");
			e.printStackTrace();
		}

		device.close();

	}

}
