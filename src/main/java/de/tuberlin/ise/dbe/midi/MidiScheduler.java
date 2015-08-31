/**
 * 
 */
package de.tuberlin.ise.dbe.midi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import de.tuberlin.ise.dbe.audiocues.AudioCuePlayer;
import de.tuberlin.ise.dbe.midi.devices.MidiOutput;
import de.tuberlin.ise.dbe.midi.instruments.Instrument;

/**
 * @author Dave
 *
 */
public class MidiScheduler {

	/** output device that is used */
	private final MidiOutput midioutput;

	/** the thread pool which is used for playback */
	private final ExecutorService threadPool = Executors.newCachedThreadPool();

	/**
	 * holds all events that are further in the future to avoid creating too
	 * many threads right away
	 */
	private final PriorityBlockingQueue<MidiNoteContainer> currentlyWaiting = new PriorityBlockingQueue<>();

	/**
	 * holds all events that are further in the future to avoid creating too
	 * many threads right away
	 */
	private final PriorityBlockingQueue<MidiNoteContainer> currentlyPlaying = new PriorityBlockingQueue<>();

	/**
	 * @param midioutput
	 */
	public MidiScheduler(MidiOutput midioutput) {
		super();
		this.midioutput = midioutput;
		threadPool.submit(new MidiNoteStarter());
		threadPool.submit(new MidiNoteStopper());
	}

	/**
	 * shuts down the underlying thread pool and terminates playback
	 */
	public void terminate() {
		threadPool.shutdown();
		midioutput.terminate();
	}

	/**
	 * plays the specified note
	 * 
	 * @param startTimestamp
	 *            when to play the note
	 * @param note
	 *            will be played
	 * 
	 */
	public void play(long startTimestamp, MidiSoundEvent event) {
		if (startTimestamp < System.currentTimeMillis() - 5)
			return; // ignore past events
		currentlyWaiting.add(new MidiNoteContainer(startTimestamp, event));
	}

	/**
	 * changes the sound of the specified channel to the specified instrument
	 * 
	 * @param instrument
	 * @param channel
	 */
	public void changeInstrument(Instrument instrument, int channel) {
		midioutput.setInstrument(channel, instrument);
	}

	private class MidiNoteStarter implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				MidiNoteContainer next = currentlyWaiting.peek();
				if (next == null) {
					try {
						Thread.sleep(3);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					continue;
				}
				next = currentlyWaiting.peek();
				if (System.currentTimeMillis() < next.timestamp) {
					try {
						Thread.sleep(Math.min(
								3,
								Math.max(
										0,
										next.timestamp
												- System.currentTimeMillis())));
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					continue;
				}
				next = currentlyWaiting.poll();
				if (next == null)
					continue;
				updateVolumeIfNecessary(next);
				if (next.note.volume <= 10)
					continue;
				midioutput.startNote(next.note.channel, next.note.tone,
						next.note.volume);
				long endTimestamp = next.note.durationInMs
						+ System.currentTimeMillis();
				// System.out.println("started: " + next.note.tone +
				// "(scheduled="+next.timestamp%1000+", actual="+System.currentTimeMillis()%1000+")");
				next.timestamp = endTimestamp;
				currentlyPlaying.add(next);
				// System.out.println("waiting notes:"+currentlyWaiting.size());
			}
		}

		/**
		 * this method is only needed for use with class {@link AudioCuePlayer}.
		 * Will poll for current metric scores and update the volume right
		 * before scheduling the midi event
		 * 
		 * @param next
		 */
		private void updateVolumeIfNecessary(MidiNoteContainer next) {
			if (next.note.dissonance && next.note.metricMonitor != null) {
				next.note.volume = (int) (next.note.metricMonitor
						.getCurrentMaximumScore() * 127 / 100.0);
				System.out.println("Dissonance volume: " + next.note.volume);
			}

		}
	}

	private class MidiNoteStopper implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				MidiNoteContainer next = currentlyPlaying.peek();
				if (next == null) {
					try {
						Thread.sleep(3);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					continue;
				}
				if (System.currentTimeMillis() < next.timestamp) {
					try {
						Thread.sleep(Math.min(
								3,
								Math.max(
										0,
										next.timestamp
												- System.currentTimeMillis())));
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					continue;
				}
				next = currentlyPlaying.poll();
				if (next == null)
					continue;
				midioutput.stopNote(next.note.channel, next.note.tone);
				// System.out.println("stop: " + next.note.tone +
				// "(scheduled="+next.timestamp%1000+", actual="+System.currentTimeMillis()%1000+")");
				// System.out.println("playing notes: "+
				// currentlyPlaying.size());
			}
		}

	}

	private static class MidiNoteContainer implements
			Comparable<MidiNoteContainer> {

		private Long timestamp;
		private MidiSoundEvent note;

		/**
		 * @param timestamp
		 * @param note
		 * @param channel
		 * @param volume
		 */
		public MidiNoteContainer(long timestamp, MidiSoundEvent note) {
			super();
			this.timestamp = timestamp;
			this.note = note;

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(MidiNoteContainer o) {
			return timestamp.compareTo(o.timestamp);
		}

	}

}
