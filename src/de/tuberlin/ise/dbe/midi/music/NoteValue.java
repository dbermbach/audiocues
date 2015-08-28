/**
 * 
 */
package de.tuberlin.ise.dbe.midi.music;

/**
 * @author Dave
 *
 */
public enum NoteValue {

	// don't change the order in here or introduce any intermediate values
	WHOLE, HALF, QUARTER, EIGHTH, SIXTEENTH, THIRTY_SECOND, SIXTY_FOURTH;

	/**
	 * translates ints into NoteValue objects: 1 => WHOLE, 2 => HALF, 4 =>
	 * QUARTER, 8 => EIGHTH, ...
	 * 
	 * @param denominator
	 * @return null if a value that has no mapping is passed.
	 */
	public static NoteValue getNoteValue(int denominator) {
		switch (denominator) {
		case 1:
			return WHOLE;
		case 2:
			return HALF;
		case 4:
			return QUARTER;
		case 8:
			return EIGHTH;
		case 16:
			return SIXTEENTH;
		case 32:
			return THIRTY_SECOND;
		case 64:
			return SIXTY_FOURTH;
		default:
			return null;
		}
	}

	/**
	 * calculates the note duration as a fraction of a whole note, i.e., 1 =
	 * four quarter notes
	 * 
	 * 
	 * 
	 * @param noOfDots
	 * @return the relative duration of this {@link NoteValue} as a fraction of
	 *         a whole note, i.e., a value of 1 corresponds to four quarter
	 *         notes. Adds the specified number of dots to the note value. E.g.,
	 *         1 dot for HALF corresponds to a returned value of
	 *         (0.5+0.25)=0.75.
	 */
	public double getRelativeDuration(int noOfDots) {
		if (noOfDots < 0)
			throw new RuntimeException(
					"The number of dots must be greater than or equal to zero.");
		return getRelativeDuration() * (2 - (1.0 / (Math.pow(2, noOfDots))));
	}

	/**
	 * calculates the note duration as a fraction of a whole note, i.e., 1 =
	 * four quarter notes
	 * 
	 * 
	 * 
	 * @return the relative duration of this {@link NoteValue} as a fraction of
	 *         a whole note, i.e., a value of 1 corresponds to four quarter
	 *         notes.
	 */
	public double getRelativeDuration() {
		return Math.pow(0.5, this.ordinal());
	}

	/**
	 * 
	 * @param values
	 * @return the sum of the relative durations of the specified note values
	 */
	public static double getRelativeDuration(NoteValue... values) {
		double res = 0;
		for (NoteValue nv : values)
			res += nv.getRelativeDuration();
		return res;
	}

}
