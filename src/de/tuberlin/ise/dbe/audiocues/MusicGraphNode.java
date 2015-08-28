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
public class MusicGraphNode {

	/** the {@link MidiNoteSequence}s of this node */
	private final List<MidiNoteSequence> sequences = new ArrayList<>();

	/** all connections of this node */
	private final List<MusicGraphNode> connections = new ArrayList<>();

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
	 *            all {@link MidiNoteSequence}s of this node of this node
	 * 
	 */
	public MusicGraphNode(MidiNoteSequence dissonanceSequence,
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
	 *            all {@link MidiNoteSequence}s of this node of this node
	 */
	public MusicGraphNode(MidiNoteSequence dissonanceSequence,
			List<MidiNoteSequence> sequences) {
		super();
		this.sequences.addAll(sequences);
		this.dissonanceSequence = dissonanceSequence;
	}

	/**
	 * adds a connection from this node to the parameter node (on both ends)
	 * 
	 * @param node
	 */
	public void connectTo(MusicGraphNode node) {
		connections.add(node);
		node.connections.add(this);
	}

	/**
	 * 
	 * @return null if this node has no connections. Otherwise returns a random
	 *         connection
	 */
	public MusicGraphNode getRandomConnection() {
		int rand = (int) (Math.random() * connections.size());
		if (connections.size() > 0)
			return connections.get(rand);
		else
			return null;
	}

	/**
	 * 
	 * @param notThisOne
	 * @return null if this node has no connections. Otherwise returns a random
	 *         connection which is not (!) the parameter notThisOne.
	 */
	public MusicGraphNode getRandomConnection(MusicGraphNode notThisOne) {
		if(notThisOne==null) return getRandomConnection();
		MusicGraphNode result;
		while ((result = getRandomConnection()) == notThisOne)
			;
		return result;
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

}
