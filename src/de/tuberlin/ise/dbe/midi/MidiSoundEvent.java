/**
 * 
 */
package de.tuberlin.ise.dbe.midi;

import de.tuberlin.ise.dbe.midi.music.MidiTonePitch;

/**
 * @author Dave
 *
 */
public class MidiSoundEvent {

	/** absolute length of the note */
	public final long durationInMs;

	/** number of the midi channel */
	public final int channel;

	/** volume of the note */
	public final int volume;

	/** defines the tone pitch of the event */
	public final MidiTonePitch tone;

	/**
	 * @param durationInMs absolute length of the note
	 * @param channel number of the midi channel
	 * @param volume volume of the note
	 * @param tone defines the tone pitch of the event
	 */
	public MidiSoundEvent(long durationInMs, int channel, int volume,
			MidiTonePitch tone) {
		super();
		this.durationInMs = durationInMs;
		this.channel = channel;
		this.volume = volume;
		this.tone = tone;
	}

}
