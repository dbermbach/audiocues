/**
 * 
 */
package de.tuberlin.ise.dbe.midi.music;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Dave
 *
 */
public class MidiNoteSequence {

	/** midi channel number for this melody */
	private final int channel;

	/** the volume used during playback */
	private int volume;

	/**
	 * the note sequence of this melody. Key is the relative note length (i.e.,
	 * 1=WHOLE)= to the start of the sequence, values are all notes that happen
	 * at this specific offset
	 */
	private final Map<Double, List<Note>> notes = new TreeMap<>();

	/**
	 * @param channel
	 * @param volume
	 */
	public MidiNoteSequence(int channel, int volume) {
		super();
		this.channel = channel;
		this.volume = volume;
	}

	/**
	 * @return
	 * @see java.util.Map#entrySet()
	 */
	public Set<Entry<Double, List<Note>>> entrySet() {
		return this.notes.entrySet();
	}

	/**
	 * @return the channel
	 */
	public int getChannel() {
		return this.channel;
	}

	/**
	 * @return the volume
	 */
	public int getVolume() {
		return this.volume;
	}

	/**
	 * adds a note to this sequence
	 * 
	 * @param fractionOfAWholeOffset
	 *            the offset to the beginning of the sequence, e.g., a value of
	 *            5.0 would refer to the 2nd quarter of bar 2 in a 4/4 beat
	 * @param note
	 *            the note which shall be added
	 */
	public void addNote(double fractionOfAWholeOffset, Note note) {
		List<Note> list = notes.get(fractionOfAWholeOffset);
		if (list == null) {
			list = new ArrayList<>();
			notes.put(fractionOfAWholeOffset, list);
		}
		list.add(note);
	}

	/**
	 * adds a chord note to this sequence
	 * 
	 * @param fractionOfAWholeOffset
	 *            the offset to the beginning of the sequence, e.g., a value of
	 *            5.0 would refer to the 2nd quarter of bar 2 in a 4/4 beat
	 * @param note
	 *            the chord note which shall be added
	 */
	public void addChordNote(double fractionOfAWholeOffset, ChordNote note) {
		for (MidiTonePitch pitch : note.getChord())
			addNote(fractionOfAWholeOffset, new Note(
					note.getRelativeDuration(), pitch));
	}

	/**
	 * @param volume the volume to set
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	/**
	 * 
	 * @return a copy of this sequence (one layer deep)
	 */
	public MidiNoteSequence deepCopy(){
		MidiNoteSequence copy = new MidiNoteSequence(this.channel,this.volume);
		copy.notes.putAll(this.notes);		
		return copy;
	}

}
