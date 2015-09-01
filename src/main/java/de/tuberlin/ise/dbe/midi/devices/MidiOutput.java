/**
 * 
 */
package de.tuberlin.ise.dbe.midi.devices;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

import de.tuberlin.ise.dbe.midi.instruments.Instrument;
import de.tuberlin.ise.dbe.midi.music.Chord;
import de.tuberlin.ise.dbe.midi.music.MidiTonePitch;

/**
 * @author Dave
 *
 */
public class MidiOutput {

	private Synthesizer synth = null;
	private Receiver rec = null;
	private MidiDevice dev = null;

	private MidiChannel[] channels;

	/**
	 * Creates an instance of MidiOutput, which generates sound form midi events using the system's default Synthesizer.
	 * @throws Exception
	 * 
	 */
	public MidiOutput() throws Exception {
		this(MidiSystem.getSynthesizer());
	}

	/**
	 * Creates an instance of MidiOutput, which sends midi messages to the specified Receiver. 
	 * @param rec
	 * @throws Exception
	 */
	public MidiOutput(MidiDevice dev) throws Exception {
	    if(dev instanceof Synthesizer){
	    	synth = (Synthesizer)dev;
	    	synth.open();
	    	channels = synth.getChannels();
	    }
		else if (dev.getMaxReceivers() == 0){
			this.dev = dev;
			dev.open();
			rec = dev.getReceiver();
		}
		else{
			
		}
	}

	/**
	 * changes the instrument setting for the specified channel
	 * 
	 * @param channel
	 * @param instrument
	 */
	public void setInstrument(int channel, Instrument instrument) {
		if (synth != null) {
			channels[channel].programChange(instrument.getMidiCode());
		} else if (rec != null) {
			ShortMessage myMsg = new ShortMessage();
			try {
				myMsg.setMessage(ShortMessage.PROGRAM_CHANGE, channel,
						instrument.getMidiCode(), 0);
			} catch (InvalidMidiDataException e) {
				System.err.println("MidiOutput: Invalid Midi Message");
				e.printStackTrace();
			}
		}
	}

	/**
	 * starts playing the respective midi note on the specified channel in the
	 * specified volume
	 * 
	 * @param channel
	 * @param midinote
	 * @param volume
	 */
	public void startNote(int channel, MidiTonePitch midinote, int volume) {
		if (synth != null) {
			channels[channel].noteOn(midinote.midiValue(), volume);
		} else if (rec != null) {
			ShortMessage myMsg = new ShortMessage();
			try {
				myMsg.setMessage(ShortMessage.NOTE_ON, channel,
						midinote.midiValue(), volume);
			} catch (InvalidMidiDataException e) {
				System.err.println("MidiOutput: Invalid Midi Message");
				e.printStackTrace();
			}
		}
	}

	/**
	 * stops playing the respective midi note on the specified channel
	 * 
	 * @param channel
	 * @param midinote
	 * 
	 */
	public void stopNote(int channel, MidiTonePitch midinote) {
		if (synth != null) {
			channels[channel].noteOff(midinote.midiValue());
		} else if (rec != null) {
			ShortMessage myMsg = new ShortMessage();
			try {
				myMsg.setMessage(ShortMessage.NOTE_OFF, channel,
						midinote.midiValue(),0);
			} catch (InvalidMidiDataException e) {
				System.err.println("MidiOutput: Invalid Midi Message");
				e.printStackTrace();
			}
		}
	}

	/**
	 * starts playing all midi notes in the specified {@link Chord} on the
	 * specified channel in the specified volume
	 * 
	 * @param channel
	 * @param chord
	 * @param volume
	 */
	public void startChord(int channel, Chord chord, int volume) {
		for (MidiTonePitch mn : chord)
			startNote(channel, mn, volume);
	}

	/**
	 * stops playing all midi notes in the specified {@link Chord} on the
	 * specified channel in the specified volume
	 * 
	 * @param channel
	 * @param chord
	 * 
	 */
	public void stopChord(int channel, Chord chord) {
		for (MidiTonePitch mn : chord)
			stopNote(channel, mn);
	}

	/**
	 * terminates this {@link MidiOutput}
	 */
	public void terminate() {
		if (synth != null) {
			for (MidiChannel c : channels)
				c.allNotesOff();
			synth.close();
		} else if (dev != null) {
			dev.close();
		}
	}

}
