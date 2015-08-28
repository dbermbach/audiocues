/**
 * 
 */
package de.tuberlin.ise.dbe.midi.music;

/**
 * This class does the same as {@link Note} but for {@link Chord} objects
 * instead of {@link MidiTonePitch} instances, i.e., for multiple parallel midi
 * tones.
 * 
 * @author Dave
 *
 */
public class ChordNote {

	/**
	 * length of the chord in fractions of a whole note, e.g., 0.25 is a quarter note.
	 */
	private final double relativeDuration;


	/**
	 * tone pitch of the note
	 */
	private final Chord chord;

	
	/**
	 * 
	 * @param fractionOfWholeNote
	 *            the fraction of a whole note, i.e., 1 corresponds to a whole
	 *            note, 0.25 to a quarter, ...
	 * 
	 *
	 * @param chord
	 *            defines the pitch of the chord
	 */
	public ChordNote(double fractionOfWholeNote,  Chord chord) {
		this.chord = chord;
		this.relativeDuration = fractionOfWholeNote;
	}

	/**
	 * 
	 * @param noteValue
	 *            a {@link NoteValue} (e.g., QUARTER, WHOLE, ...)
	 *
	 * @param chord
	 *            defines the pitch of the chord
	 */
	public ChordNote(NoteValue noteValue, Tempo tempo, Chord chord) {
		this(noteValue.getRelativeDuration(),chord);
	}

	/**
	 * 
	 * @param noteValue
	 *            a {@link NoteValue} (e.g., QUARTER, WHOLE, ...)
	 * @param noOfDots
	 *            a non-negative number of dots which follow the noteValue
	 *            (e.g., double-dotted quarter node)
	 *
	 * @param chord
	 *            defines the pitch of the chord
	 */
	public ChordNote(NoteValue noteValue, int noOfDots,
			Chord chord) {
		this(noteValue.getRelativeDuration(noOfDots), chord);
	}

	/**
	 * @return the duration
	 */
	public double getRelativeDuration() {
		return this.relativeDuration;
	}

	/**
	 * @return the tone
	 */
	public Chord getChord() {
		return this.chord;
	}

	
	
	
}
