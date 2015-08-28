/**
 * 
 */
package de.tuberlin.ise.dbe.midi.music;

/**
 * helps building simple {@link MidiNoteSequence} instances where notes are
 * played in strict sequence (one after the other), i.e., no note starts before
 * the previous note has ended
 * 
 * @author Dave
 *
 */
public class MidiNoteSequenceBuilder {

	/** object which is built */
	private final MidiNoteSequence sequence;

	/** determines the offset where the next note will be added */
	private double fractionCounter = 0;

	/**
	 * @param channel
	 * @param volume
	 */
	public MidiNoteSequenceBuilder(int channel, int volume) {
		this.sequence = new MidiNoteSequence(channel, volume);
	}

	/**
	 * appends the specified note
	 * 
	 * @param note
	 * @return this instance for method chaining
	 */
	public MidiNoteSequenceBuilder appendNote(Note note) {
		sequence.addNote(fractionCounter, note);
		fractionCounter += note.getRelativeDuration();
		return this;
	}

	/**
	 * appends the specified note
	 * 
	 * @param pitch
	 *            the pitch of the tone (e.g., C5 or F#4)
	 * @param noteValue
	 *            the length of the tone
	 * @param noOfDots
	 *            the number of dots behind the note value
	 * @return this instance for method chaining
	 */
	public MidiNoteSequenceBuilder appendNote(String pitch,
			NoteValue noteValue, int noOfDots) {
		return appendNote(pitch, noteValue.getRelativeDuration(noOfDots));
	}

	/**
	 * appends the specified note
	 * 
	 * @param pitch
	 *            the pitch of the tone (e.g., C5 or F#4)
	 * @param noteValue
	 *            the length of the tone
	 * @return this instance for method chaining
	 */
	public MidiNoteSequenceBuilder appendNote(String pitch, NoteValue noteValue) {
		return appendNote(pitch, noteValue.getRelativeDuration());
	}

	/**
	 * appends the specified note
	 * 
	 * @param pitch
	 *            the pitch of the tone (e.g., C5 or F#4)
	 * @param fractionOfAWholeNote
	 *            the length of the tone as a fraction of a whole (e.g., 1= 4
	 *            quarters = 1 whole note)
	 * @return this instance for method chaining
	 */
	public MidiNoteSequenceBuilder appendNote(String pitch,
			double fractionOfAWholeNote) {
		return appendNote(new Note(fractionOfAWholeNote,
				MidiTonePitch.getTone(pitch)));
	}

	/**
	 * appends the specified chord note
	 * 
	 * @param note
	 *            a chord note
	 * @return this instance for method chaining
	 */
	public MidiNoteSequenceBuilder appendChord(ChordNote note) {
		for (MidiTonePitch tone : note.getChord().getNotes()) {
			sequence.addNote(fractionCounter,
					new Note(note.getRelativeDuration(), tone));
		}
		fractionCounter += note.getRelativeDuration();
		return this;
	}

	/**
	 * appends the specified number of notes as a chord note
	 * 
	 * @param chord
	 *            a chord
	 * @param fractionOfAWholeNote
	 *            the length of the tone as a fraction of a whole (e.g., 1= 4
	 *            quarters = 1 whole note)
	 * @return this instance for method chaining
	 */
	public MidiNoteSequenceBuilder appendChord(Chord chord,
			double fractionOfAWholeNote) {
		return appendChord(new ChordNote(fractionOfAWholeNote, chord));
	}
	
	/**
	 * appends the specified number of notes as a chord note
	 * 
	 * @param chord
	 *            a chord
	 * @param fractionOfAWholeNote
	 *            the length of the tone as a fraction of a whole (e.g., 1= 4
	 *            quarters = 1 whole note)
	 * @return this instance for method chaining
	 */
	public MidiNoteSequenceBuilder appendChord(String chord,
			double fractionOfAWholeNote) {
		return appendChord(new ChordNote(fractionOfAWholeNote, new Chord(chord)));
	}

	/**
	 * appends the specified number of notes as a chord note
	 * 
	 * @param chord
	 *            a chord
	 * @param noteValue
	 *            the length of the tone
	 * @param noOfDots
	 *            the number of dots behind the note value
	 * @return this instance for method chaining
	 */
	public MidiNoteSequenceBuilder appendChord(Chord chord,
			NoteValue notevalue, int noOfDots) {
		return appendChord(new ChordNote(
				notevalue.getRelativeDuration(noOfDots), chord));
	}

	/**
	 * appends the specified number of notes as a chord note
	 * 
	 * @param chord
	 *            a chord
	 * @param noteValue
	 *            the length of the tone
	 * 
	 * @return this instance for method chaining
	 */
	public MidiNoteSequenceBuilder appendChord(Chord chord, NoteValue notevalue) {
		return appendChord(new ChordNote(notevalue.getRelativeDuration(), chord));
	}

	
	
	/**
	 * appends the specified number of notes as a chord note
	 * 
	 * @param note
	 * @return this instance for method chaining
	 */
	public MidiNoteSequenceBuilder appendChord(Note... notes) {
		for (Note note : notes)
			sequence.addNote(fractionCounter, note);
		fractionCounter += notes[0].getRelativeDuration();
		return this;
	}

	/**
	 * appends the specified rest length to the sequence (=nothing is played)
	 * 
	 * @param fractionOfAWhole
	 *            a value of 1 corresponds to a WHOLE note (= 4 quarters)
	 * @return this instance for method chaining
	 */
	public MidiNoteSequenceBuilder appendRest(double fractionOfAWhole) {
		fractionCounter += fractionOfAWhole;
		return this;
	}

	/**
	 * appends the specified rest length to the sequence (=nothing is played)
	 * 
	 * @param notevalue
	 *            the length of the rest
	 * @return this instance for method chaining
	 */
	public MidiNoteSequenceBuilder appendRest(NoteValue notevalue) {
		return appendRest(notevalue.getRelativeDuration());
	}

	/**
	 * 
	 * appends the specified rest length to the sequence (=nothing is played)
	 * 
	 * @param notevalue
	 *            the length of the rest
	 * @param noOfDots
	 *            the number of dots after the rest
	 * @return this instance for method chaining
	 */
	public MidiNoteSequenceBuilder appendRest(NoteValue notevalue, int noOfDots) {
		return appendRest(notevalue.getRelativeDuration(noOfDots));
	}

	/**
	 * 
	 * @return the final object built via this helper class.
	 */
	public MidiNoteSequence build() {
		return sequence.deepCopy();
	}

}
