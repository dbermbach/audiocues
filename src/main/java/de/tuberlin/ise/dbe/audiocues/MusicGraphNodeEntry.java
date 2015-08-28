/**
 * 
 */
package de.tuberlin.ise.dbe.audiocues;

import java.util.ArrayList;
import java.util.List;

import de.tuberlin.ise.dbe.midi.music.MidiNoteSequence;

/**
 * instances of {@link MusicGraphNode} form a graph without sinks so that the
 * graph can be iterated forever without ever going backwards on a connection.
 * Each node contains a number of {@link MidiNoteSequence}s that can be played
 * by an iterator.
 * 
 * @author Dave
 *
 */
public class MusicGraphNodeEntry {

	/** the {@link MidiNoteSequence}s of this entry */
	private final List<MidiNoteSequence> sequences = new ArrayList<>();

	

	
	/**
	 * a collection of strong dissonances compared to the other sequences of
	 * this node
	 */
	private final MidiNoteSequence dissonanceSequence;

	/**
	 * @param dissonanceSequence
	 *            a sequence of strong dissonances compared to parameter
	 *            sequences
	 * @param sequences
	 *            all {@link MidiNoteSequence}s of this node of this entry
	 * 
	 */
	public MusicGraphNodeEntry(MidiNoteSequence dissonanceSequence,
			MidiNoteSequence... sequences) {
		super();
		for (MidiNoteSequence pt : sequences) {
			this.sequences.add(pt);
		}
		this.dissonanceSequence = dissonanceSequence;
	}

	/**
	 * @param dissonanceSequence
	 *            a sequence of strong dissonances compared to parameter
	 *            sequences
	 * @param sequences
	 *            all {@link MidiNoteSequence}s of this node of this entry
	 */
	public MusicGraphNodeEntry(MidiNoteSequence dissonanceSequence,
			List<MidiNoteSequence> sequences) {
		super();
		this.sequences.addAll(sequences);
		this.dissonanceSequence = dissonanceSequence;
	}
	/**
	 * @param dissonanceSequence
	 *            a sequence of strong dissonances compared to parameter
	 *            sequences
	 *
	 */
	public MusicGraphNodeEntry(MidiNoteSequence dissonanceSequence) {
		super();
		this.dissonanceSequence = dissonanceSequence;
	}

	
	/**
	 * @return the sequences
	 */
	public List<MidiNoteSequence> getSequences() {
		return this.sequences;
	}

	/**
	 * @return the dissonanceSequence
	 */
	public MidiNoteSequence getDissonanceSequence() {
		return this.dissonanceSequence;
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean addSequence(MidiNoteSequence e) {
		return this.sequences.add(e);
	}

}
