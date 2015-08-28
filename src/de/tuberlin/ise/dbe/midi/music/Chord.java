/**
 * 
 */
package de.tuberlin.ise.dbe.midi.music;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Dave
 *
 */
public class Chord implements Iterable<MidiTonePitch> {

	/** all notes of this chord */
	private final Set<MidiTonePitch> notes = new HashSet<>();

	/**
	 * 
	 * @param midiNotes
	 *            all midi notes for this chord
	 */
	public Chord(MidiTonePitch... midiNotes) {
		for (MidiTonePitch m : midiNotes)
			notes.add(m);
	}

	/**
	 * convenience constructor which accepts whitespace-separated midi notes in a string, e.g., "C5 F5 Bb5"
	 * @param notes
	 */
	public Chord(String notes) {
		String[] splits = notes.split(" ");
		for (String spl : splits)
			this.notes.add(MidiTonePitch.getTone(spl));
	}

	/**
	 * 
	 * @param notes
	 *            a collection of notes in this chord
	 */
	public Chord(Collection<? extends MidiTonePitch> notes) {
		this.notes.addAll(notes);
	}

	/**
	 * @return
	 * @see java.util.Set#iterator()
	 */
	public Iterator<MidiTonePitch> iterator() {
		return this.notes.iterator();
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean addNote(MidiTonePitch e) {
		return this.notes.add(e);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	public boolean addNotes(Collection<? extends MidiTonePitch> c) {
		return this.notes.addAll(c);
	}

	/**
	 * transposes all {@link MidiTonePitch}s in this chord by the respective number
	 * of semi-tones (positive = higher, negative = lower)
	 * 
	 * @param semitones
	 * @return a new Chord where all {@link MidiTonePitch}s have been changed
	 *         according to parameter semitones. Will be null if the resulting
	 *         midi number is not in [0,127].
	 */
	public Chord transpose(int semitones) {
		HashSet<MidiTonePitch> notes = new HashSet<>();
		for (MidiTonePitch mn : this.notes) {
			MidiTonePitch newone = mn.transpose(semitones);
			if (newone == null)
				return null;
			notes.add(newone);
		}
		return new Chord(notes);
	}

	/**
	 * @return the notes
	 */
	public Set<MidiTonePitch> getNotes() {
		return new HashSet<>(this.notes);
	}

}
