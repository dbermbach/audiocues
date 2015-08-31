/**
 * 
 */
package de.tuberlin.ise.dbe.midi;

import de.tuberlin.ise.dbe.metrics.MetricMonitor;
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
	public int volume;

	/** defines the tone pitch of the event */
	public final MidiTonePitch tone;

	/**
	 * optional field, true marks that this event is part of an audiocue
	 * dissonance
	 */
	public boolean dissonance;

	/** necessary field when the dissonance information shall be leveraged */
	public MetricMonitor metricMonitor;

	/**
	 * @param durationInMs
	 *            absolute length of the note
	 * @param channel
	 *            number of the midi channel
	 * @param volume
	 *            volume of the note
	 * @param tone
	 *            defines the tone pitch of the event
	 */
	public MidiSoundEvent(long durationInMs, int channel, int volume,
			MidiTonePitch tone) {
		super();
		this.durationInMs = durationInMs;
		this.channel = channel;
		this.volume = volume;
		this.tone = tone;
	}

	/**
	 *
	 * @param durationInMs
	 *            absolute length of the note
	 * @param channel
	 *            number of the midi channel
	 * @param volume
	 *            volume of the note
	 * @param tone
	 *            defines the tone pitch of the event
	 * @param dissonance
	 *            optional field, true marks that this event is part of an
	 *            audiocue dissonance
	 * @param metricMonitor
	 *            the {@link MetricMonitor} that provides dissonance volume
	 *            information
	 */
	public MidiSoundEvent(long durationInMs, int channel, int volume,
			MidiTonePitch tone, boolean dissonance, MetricMonitor metricMonitor) {
		super();
		this.durationInMs = durationInMs;
		this.channel = channel;
		this.volume = volume;
		this.tone = tone;
		this.dissonance = dissonance;
		this.metricMonitor = metricMonitor;
	}

}
