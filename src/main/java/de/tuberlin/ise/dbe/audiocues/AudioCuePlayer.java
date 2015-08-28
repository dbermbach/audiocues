/**
 * 
 */
package de.tuberlin.ise.dbe.audiocues;

import de.tuberlin.ise.dbe.metrics.MetricMonitor;
import de.tuberlin.ise.dbe.midi.MusicalMidiScheduler;
import de.tuberlin.ise.dbe.midi.devices.MidiOutput;
import de.tuberlin.ise.dbe.midi.instruments.Instrument;
import de.tuberlin.ise.dbe.midi.music.MidiNoteSequence;
import de.tuberlin.ise.dbe.midi.music.Tempo;

/**
 * @author Dave
 *
 */
public class AudioCuePlayer implements Runnable {

	/**
	 * the length of the {@link MidiNoteSequence}s of all {@link MusicGraphNode}
	 * s in whole notes.
	 */
	private final int sequenceLength;

	/** the last node that was scheduled for playback */
	private MusicGraphNode lastNode;

	/** the scheduler used for output */
	private MusicalMidiScheduler scheduler;

	/**
	 * provides the metric scores that determine the volume of the dissonance
	 * tones
	 */
	private final MetricMonitor metricMonitor;

	/** determines how often this player wakes up to schedule the next sequence */
	private long checkInterval;

	/** player terminates if set to false */
	private boolean running = true;

	/**
	 * 
	 * @param sequenceLength
	 *            the static length of all midinotesequences in whole notes
	 *            (i.e., the sequence length in a {@link MusicGraphNode})
	 * @param startNode
	 *            first node for playback
	 * @param tempo
	 *            tempo in bpm
	 * @throws Exception
	 *             if creation of {@link MidiOutput} device fails
	 */
	public AudioCuePlayer(int sequenceLength, MusicGraphNode startNode,
			int tempo, MetricMonitor metricMonitor) throws Exception {
		this.sequenceLength = sequenceLength;
		this.lastNode = startNode;
		this.scheduler = new MusicalMidiScheduler(new MidiOutput(), new Tempo(
				tempo), 4, 4);
		this.metricMonitor = metricMonitor;
		this.checkInterval = new Tempo(tempo)
				.getAbsoluteDuration(sequenceLength);
	}

	/**
	 * changes the sound of the specified channel to the specified instrument
	 * 
	 * @param channel
	 * @param instrument
	 */
	public void changeInstrument(int channel, Instrument instrument) {
		scheduler.changeInstrument(instrument, channel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		scheduler.startPlaybackNow(500);
		// play first node
		MusicGraphNode currentNode = lastNode;
		System.out.println("Scheduling node: " + currentNode.getNodeName());
		int nextBarOffset = playNode(currentNode, 1, true);
		// play second node
		currentNode = lastNode.getRandomConnection();
		System.out.println("Scheduling node: " + currentNode.getNodeName());
		nextBarOffset = playNode(currentNode, nextBarOffset, false);
		// play all other nodes
		while (running) {
			if (currentNode == null)
				break;
			MusicGraphNode temp = currentNode.getRandomConnection(lastNode);
			lastNode = currentNode;
			currentNode = temp;
			System.out.println("Scheduling node: " + currentNode.getNodeName());
			nextBarOffset = playNode(currentNode, nextBarOffset, false);
		}

	}

	/**
	 * plays all entries of the specified node
	 * 
	 * @param node
	 *            the node which shall be scheduled for playback
	 * @param nextBarOffset
	 *            the bar for which the first entry in the node shall be
	 *            scheduled
	 * @param shortSleepOnFirst
	 *            if true the method will only sleep for 70% of the time after
	 *            scheduling the first entry
	 * @return the next bar offset for the next invocation
	 */
	public int playNode(MusicGraphNode node, int nextBarOffset,
			boolean shortSleepOnFirst) {
		int entryCounter = 0;
		int noOfEntries = node.getNumberOfEntries();
		MusicGraphNodeEntry currentEntry;
		while (entryCounter < noOfEntries) {
			currentEntry = node.getEntries().get(entryCounter);
			schedulePlayback(nextBarOffset, currentEntry);
			if (shortSleepOnFirst)
				sleep(0.7 * this.checkInterval);
			else
				sleep(this.checkInterval);
			nextBarOffset += sequenceLength;
			entryCounter++;
		}
		return nextBarOffset;
	}

	/**
	 * schedules all {@link MidiNoteSequence}s from the specified
	 * {@link MusicGraphNode} for playback at the specified bar offset
	 * 
	 * @param barOffset
	 * @param currentNode
	 */
	private void schedulePlayback(int barOffset, MusicGraphNodeEntry currentNode) {
		if (currentNode == null)
			return;
		for (MidiNoteSequence seq : currentNode.getSequences())
			scheduler.scheduleEventSequence(barOffset, 0, seq);
		MidiNoteSequence dissonance = currentNode.getDissonanceSequence();
		int dissVolume = (int) (metricMonitor.getCurrentMaximumScore() * 127.0 / 100.0);
		dissonance.setVolume(dissVolume);
		System.out.println("dissonance volume: " + dissVolume);
		if (dissVolume > 10)
			scheduler.scheduleEventSequence(barOffset, 0, dissonance);
	}

	/**
	 * terminates the {@link AudioCuePlayer}
	 */
	public void terminate() {
		running = false;
	}

	/**
	 * sleeps for the specified time in ms. InterruptedExceptions are caught and
	 * the current thread is interrupted again.
	 * 
	 * @param ms
	 *            duration
	 */
	private void sleep(double ms) {
		try {
			Thread.sleep((long) ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}
