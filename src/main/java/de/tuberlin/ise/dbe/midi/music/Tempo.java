/**
 * 
 */
package de.tuberlin.ise.dbe.midi.music;

/**
 * @author Dave
 *
 */
public class Tempo {

	/** beats per minute */
	private int bpm;

	/**
	 * @param bpm
	 *            tempo in beats (quarters) per minute
	 */
	public Tempo(int bpm) {
		super();
		this.bpm = bpm;
	}

	/**
	 * 
	 * @param note
	 *            a {@link NoteValue} instance
	 * @return a duration in ms which the specified parameter corresponds to
	 *         based on the initial bpm setting of this instance
	 */
	public long getAbsoluteDuration(NoteValue note) {
		return getAbsoluteDuration(note.getRelativeDuration());
	}

	/**
	 * transforms fractions of whole notes (=4 quarters) into ms
	 * 
	 * @param note
	 *            the base note
	 * @param noOfDots
	 *            the (non-negative) number of dots following the note
	 * @return a duration in ms which the specified parameter corresponds to
	 *         based on the initial bpm setting of this instance
	 */
	public long getAbsoluteDuration(NoteValue note, int noOfDots) {
		return getAbsoluteDuration(note.getRelativeDuration(noOfDots));
	}

	/**
	 * transforms fractions of whole notes (=4 quarters) into ms
	 * 
	 * 
	 * @param fraction
	 *            the percentage of a whole note (=2 halfs). May be larger than
	 *            1.
	 * @return a duration in ms which the specified parameter corresponds to
	 *         based on the initial bpm setting of this instance
	 */
	public long getAbsoluteDuration(double fraction) {
		return (long) ((60.0 / bpm) * fraction * 4 * 1000);

	}

	

}
