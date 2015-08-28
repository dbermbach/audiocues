/**
 * 
 */
package de.tuberlin.ise.dbe.midi.music;

/**
 * A {@link Note} is a  midi note that contains information on
 * tone pitch (from {@link MidiTonePitch} instances) as well as the relative length of
 * that tone ..
 * 
 * @author Dave
 *
 */
public class Note {

	/**
	 * length of the note in fractions of a whole note, e.g., 0.25 is a quarter note.
	 */
	private final double relativeDuration;

	/**
	 * tone pitch of the note
	 */
	private final MidiTonePitch tone;

	/**
	 * 
	 * @param fractionOfWholeNote
	 *            the fraction of a whole note, i.e., 1 corresponds to a whole
	 *            note, 0.25 to a quarter, ...
	 * 
	 * @param tone
	 *            defines the pitch of the tone
	 */
	public Note(double fractionOfWholeNote,  MidiTonePitch tone) {
		this.tone = tone;
		this.relativeDuration = fractionOfWholeNote;
	}

	/**
	 * 
	 * @param noteValue
	 *            a {@link NoteValue} (e.g., QUARTER, WHOLE, ...)
	 *
	 * 
	 * @param tone
	 *            defines the pitch of the tone
	 */
	public Note(NoteValue noteValue, MidiTonePitch tone) {
		this(noteValue.getRelativeDuration(), tone);
	}

	/**
	 * 
	 * @param noteValue
	 *            a {@link NoteValue} (e.g., QUARTER, WHOLE, ...)
	 * @param noOfDots
	 *            a non-negative number of dots which follow the noteValue
	 *            (e.g., double-dotted quarter node)
	 *
	 * @param tone
	 *            defines the pitch of the tone
	 */
	public Note(NoteValue noteValue, int noOfDots, Tempo tempo,
			MidiTonePitch tone) {
		this(noteValue.getRelativeDuration(noOfDots), tone);
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
	public MidiTonePitch getTone() {
		return this.tone;
	}


	
	

}
