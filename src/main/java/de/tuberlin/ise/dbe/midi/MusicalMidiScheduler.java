/**
 * 
 */
package de.tuberlin.ise.dbe.midi;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import de.tuberlin.ise.dbe.audiocues.AudioCuePlayer;
import de.tuberlin.ise.dbe.metrics.MetricMonitor;
import de.tuberlin.ise.dbe.midi.devices.MidiOutput;
import de.tuberlin.ise.dbe.midi.music.MidiNoteSequence;
import de.tuberlin.ise.dbe.midi.music.MidiTonePitch;
import de.tuberlin.ise.dbe.midi.music.Note;
import de.tuberlin.ise.dbe.midi.music.Tempo;

/**
 * @author Dave
 *
 */
public class MusicalMidiScheduler extends MidiScheduler {

	/** the playback tempo */
	private final Tempo tempo;

	/**
	 * timeSignature[0] is the counter, timeSignature[1] the denominator, i.e.,
	 * for a waltz it's usually 3 and 4 respectively. Default is 4/4.
	 */
	private int[] timeSignature = { 4, 4 };

	/** defines the start of playback, everything else is relative to this. */
	private long baseTimestamp;

	/**
	 * if playback is already in progress (true) or if events are only being
	 * scheduled for future playback (false)
	 */
	private boolean playbackIsStarted = false;

	/**
	 * holds all scheduled events while playback has not been started yet
	 */
	private final Set<MidiEvent> scheduledEvents = new HashSet<>();

	/**
	 * 
	 * @param midioutput
	 *            output device
	 * @param tempo
	 *            tempo for playback
	 * @param counter
	 *            counter of the time signature
	 * @param denominator
	 *            denominator of the time signature
	 */
	public MusicalMidiScheduler(MidiOutput midioutput, Tempo tempo,
			int counter, int denominator) {
		super(midioutput);
		this.tempo = tempo;
		updateTimeSignature(counter, denominator);
	}

	/**
	 * updates the time signature of this player. Values < 1 are ignored.
	 * 
	 * @param counter
	 *            e.g., for a waltz: 3
	 * @param denominator
	 *            e.g., for a waltz: 4
	 */
	private void updateTimeSignature(int counter, int denominator) {
		if (counter < 1 || denominator < 1)
			return;
		timeSignature[0] = counter;
		timeSignature[1] = denominator;
	}

	/**
	 * starts playback of all scheduled events in offset ms. New events can
	 * still be scheduled and are played back if possible.
	 * 
	 * @param offset
	 */
	public void startPlaybackNow(long offset) {
		synchronized (scheduledEvents) {
			playbackIsStarted = true;
			baseTimestamp = System.currentTimeMillis() + offset;
			for (MidiEvent me : scheduledEvents) {
				if(me.dissonance&&me.metricMonitor!=null){
					//for use with AudioCuePlayer only
					super.play(baseTimestamp + me.relativeTimestamp,
							new MidiSoundEvent(me.absoluteDuration, me.channel,
									me.volume, me.tone,me.dissonance,me.metricMonitor));
				}
				else super.play(baseTimestamp + me.relativeTimestamp,
						new MidiSoundEvent(me.absoluteDuration, me.channel,
								me.volume, me.tone));
			}
		}

	}

	/**
	 * schedules a new midi event
	 * 
	 * @param barNumber
	 *            the bar number where the note shall be played starting with 1
	 * @param denominatorOffset
	 *            the multiples of the denominator in the bar, e.g., for a 4/4
	 *            beat, a value of 3 will schedule the note to be played on the
	 *            "3"
	 * @param channel
	 *            midi channel to be used
	 * @param volume
	 *            volume of the tone
	 * @param note
	 *            the midi note which shall be played
	 */
	public void scheduleEvent(int barNumber, double denominatorOffset,
			int channel, int volume, Note note) {
		long relativeTimestamp = tempo.getAbsoluteDuration(1)
				* (barNumber - 1)
				+ tempo.getAbsoluteDuration(denominatorOffset
						* timeSignature[1]);
		long absoluteDuration = tempo.getAbsoluteDuration(note
				.getRelativeDuration());
		scheduleEvent(relativeTimestamp, channel, volume, absoluteDuration,
				note.getTone());
	}

	/**
	 * puts the specified midi event either into a queue pending playback later
	 * or sends it to MidiScheduler.play() directly.
	 * 
	 * @param relativeTimestamp
	 * @param channel
	 * @param volume
	 * @param absoluteDuration
	 * @param tone
	 */
	private void scheduleEvent(long relativeTimestamp, int channel, int volume,
			long absoluteDuration, MidiTonePitch tone) {
		if (playbackIsStarted) {
			super.play(baseTimestamp + relativeTimestamp, new MidiSoundEvent(
					absoluteDuration - 7, channel, volume, tone));
		} else {
			synchronized (scheduledEvents) {
				if (playbackIsStarted) {
					super.play(baseTimestamp + relativeTimestamp,
							new MidiSoundEvent(absoluteDuration - 7, channel,
									volume, tone));
				} else
					scheduledEvents.add(new MidiEvent(relativeTimestamp,
							absoluteDuration - 7, channel, volume, tone));
			}
		}
	}

	/**
	 * schedules an entire {@link MidiNoteSequence}
	 * 
	 * @param barNumber
	 *            the bar number where the note shall be played starting with 1
	 * @param denominatorOffset
	 *            the multiples of the denominator in the bar, e.g., for a 4/4
	 *            beat, a value of 3 will schedule the note to be played on the
	 *            "3"
	 * @param seq
	 *            the {@link MidiNoteSequence}
	 */
	public void scheduleEventSequence(int barNumber, double denominatorOffset,
			MidiNoteSequence seq) {
		long relativeSequenceStart = tempo.getAbsoluteDuration(1)
				* (barNumber - 1)
				+ tempo.getAbsoluteDuration(denominatorOffset
						* timeSignature[1]);
		for (Entry<Double, List<Note>> entry : seq.entrySet()) {
			long relativeStart = tempo.getAbsoluteDuration(entry.getKey())
					+ relativeSequenceStart;
			for (Note n : entry.getValue()) {
				scheduleEvent(relativeStart, seq.getChannel(), seq.getVolume(),
						tempo.getAbsoluteDuration(n.getRelativeDuration()),
						n.getTone());
			}
		}

	}

	/**
	 * this method is only needed for use with class {@link AudioCuePlayer}.
	 * 
	 * schedules an entire {@link MidiNoteSequence}
	 * 
	 * @param barNumber
	 *            the bar number where the note shall be played starting with 1
	 * @param denominatorOffset
	 *            the multiples of the denominator in the bar, e.g., for a 4/4
	 *            beat, a value of 3 will schedule the note to be played on the
	 *            "3"
	 * @param seq
	 *            the {@link MidiNoteSequence}
	 */
	public void scheduleDissonanceEventSequence(int barNumber,
			double denominatorOffset, MidiNoteSequence seq, MetricMonitor metricMonitor) {
		long relativeSequenceStart = tempo.getAbsoluteDuration(1)
				* (barNumber - 1)
				+ tempo.getAbsoluteDuration(denominatorOffset
						* timeSignature[1]);
		for (Entry<Double, List<Note>> entry : seq.entrySet()) {
			long relativeStart = tempo.getAbsoluteDuration(entry.getKey())
					+ relativeSequenceStart;
			for (Note n : entry.getValue()) {
				scheduleDissonanceEvent(relativeStart, seq.getChannel(),
						seq.getVolume(),
						tempo.getAbsoluteDuration(n.getRelativeDuration()),
						n.getTone(),metricMonitor);
			}
		}

	}

	/**
	 * this method is only needed for use with class {@link AudioCuePlayer}.
	 * 
	 * puts the specified midi event either into a queue pending playback later
	 * or sends it to MidiScheduler.play() directly.
	 * 
	 * @param relativeTimestamp
	 * @param channel
	 * @param volume
	 * @param absoluteDuration
	 * @param tone
	 */
	private void scheduleDissonanceEvent(long relativeTimestamp, int channel,
			int volume, long absoluteDuration, MidiTonePitch tone, MetricMonitor metricMonitor) {
		if (playbackIsStarted) {
			super.play(baseTimestamp + relativeTimestamp, new MidiSoundEvent(
					absoluteDuration - 7, channel, volume, tone,true,metricMonitor));
		} else {
			synchronized (scheduledEvents) {
				if (playbackIsStarted) {
					super.play(baseTimestamp + relativeTimestamp,
							new MidiSoundEvent(absoluteDuration - 7, channel,
									volume, tone,true,metricMonitor));
				} else
					scheduledEvents.add(new MidiEvent(relativeTimestamp,
							absoluteDuration - 7, channel, volume, tone,true,metricMonitor));
			}
		}
	}

	private class MidiEvent {
		private long relativeTimestamp;
		private long absoluteDuration;
		private int channel;
		private int volume;
		private MidiTonePitch tone;

		// for AudioCuePlayer only
		private boolean dissonance;
		private MetricMonitor metricMonitor;

		/**
		 * @param relativeTimestamp
		 * @param channel
		 * @param volume
		 * @param note
		 */
		public MidiEvent(long relativeTimestamp, long absoluteDuration,
				int channel, int volume, MidiTonePitch tone) {
			super();
			this.relativeTimestamp = relativeTimestamp;
			this.absoluteDuration = absoluteDuration;
			this.channel = channel;
			this.volume = volume;
			this.tone = tone;
		}

		/**
		 * @param relativeTimestamp
		 * @param absoluteDuration
		 * @param channel
		 * @param volume
		 * @param tone
		 * @param dissonance
		 * @param metricMonitor
		 */
		public MidiEvent(long relativeTimestamp, long absoluteDuration,
				int channel, int volume, MidiTonePitch tone,
				boolean dissonance, MetricMonitor metricMonitor) {
			super();
			this.relativeTimestamp = relativeTimestamp;
			this.absoluteDuration = absoluteDuration;
			this.channel = channel;
			this.volume = volume;
			this.tone = tone;
			this.dissonance = dissonance;
			this.metricMonitor = metricMonitor;
		}

	}

}
