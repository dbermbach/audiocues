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
 * Each node contains a number of MusicGraphNodeEntry instances which are played
 * sequentially and which in turn contain a collection of
 * {@link MidiNoteSequence}s that are played in parallel.
 * 
 * @author Dave
 *
 */
public class MusicGraphNode {

	/** the {@link MusicGraphNodeEntry}s of this node */
	private final List<MusicGraphNodeEntry> entries = new ArrayList<>();

	/** all connections of this path */
	private final List<MusicGraphNode> connections = new ArrayList<>();

	/**
	 * can be printed during playback to identify the current position in the
	 * graph
	 */
	private final String nodeName;

	/**
	 * @param nodeName
	 *            can be printed during playback to identify the current
	 *            position in the graph
	 * 
	 * @param entries
	 *            all {@link MusicGraphNode}s of this path
	 * 
	 */
	public MusicGraphNode(String nodeName, MusicGraphNodeEntry... entries) {
		this(nodeName);
		for (MusicGraphNodeEntry entry : entries) {
			this.entries.add(entry);
		}
	}
	
	/**
	 * @param nodeName
	 *            can be printed during playback to identify the current
	 *            position in the graph
	 *
	 * make sure that you add MusicGraphNodeEntry instances later. 
	 */
	public MusicGraphNode(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * adds another entry to this node
	 * 
	 * @param entry
	 */
	public void appendEntry(MusicGraphNodeEntry entry) {
		entries.add(entry);
	}

	/**
	 * adds a connection from this path to the parameter path (on both ends)
	 * 
	 * @param oneSided
	 *            if true the specified path will be added as connection to this
	 *            path but not the other way around, i.e., playback may lead
	 *            from this path to the other but not in the opposite direction
	 * @param node
	 *            the node which shall be connected to this
	 *            {@link MusicGraphNode}
	 */
	public void connectTo(MusicGraphNode node, boolean oneSided) {
		connections.add(node);
		if (!oneSided)
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
	 *         connection which is not (!) the parameter notThisOne. If there is
	 *         only one known connection, that connection will be returned
	 *         (ignoring the parameter).
	 */
	public MusicGraphNode getRandomConnection(MusicGraphNode notThisOne) {
		if (connections.size() == 0)
			return null;
		if (notThisOne == null)
			return getRandomConnection();
		if (connections.size() == 1)
			return connections.get(0);
		MusicGraphNode result;
		while ((result = getRandomConnection()) == notThisOne)
			;
		return result;
	}

	/**
	 * @return the pathName
	 */
	public String getNodeName() {
		return this.nodeName;
	}

	/**
	 * @return the entries
	 */
	public List<MusicGraphNodeEntry> getEntries() {
		return this.entries;
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int getNumberOfEntries() {
		return this.entries.size();
	}

}
