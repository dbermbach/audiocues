/**
 * 
 */
package de.tuberlin.ise.dbe.midi.devices;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

import de.tuberlin.ise.dbe.midi.instruments.Instrument;
import de.tuberlin.ise.dbe.midi.music.Chord;
import de.tuberlin.ise.dbe.midi.music.MidiTonePitch;

/**
 * @author Dave
 *
 */
public class MidiOutput {

	private Synthesizer synth;

	private MidiChannel[] channels;

	/**
	 * @throws Exception
	 * 
	 */
	public MidiOutput() throws Exception {
		synth = MidiSystem.getSynthesizer();
		synth.open();
		channels = synth.getChannels();
	}

	/**
	 * 
	 * @return the number of supported channels. Number 9 is reserved for drums.
	 */
	public int getNumberOfChannels() {
		return channels.length;
	}

	/**
	 * changes the instrument setting for the specified channel
	 * 
	 * @param channel
	 * @param instrument
	 */
	public void setInstrument(int channel, Instrument instrument) {
		channels[channel].programChange(instrument.getMidiCode());
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
		channels[channel].noteOn(midinote.midiValue(), volume);
	}

	/**
	 * stops playing the respective midi note on the specified channel
	 * 
	 * @param channel
	 * @param midinote
	 * 
	 */
	public void stopNote(int channel, MidiTonePitch midinote) {
		channels[channel].noteOff(midinote.midiValue());
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
		for (MidiChannel c : channels)
			c.allNotesOff();
		synth.close();
	}

}
