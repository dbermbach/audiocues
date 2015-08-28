/**
 * 
 */
package de.tuberlin.ise.dbe.audiocues;

import de.tuberlin.ise.dbe.midi.music.Chord;
import de.tuberlin.ise.dbe.midi.music.MidiNoteSequence;
import de.tuberlin.ise.dbe.midi.music.MidiNoteSequenceBuilder;
import de.tuberlin.ise.dbe.midi.music.NoteValue;

/**
 * @author Dave
 *
 */
public class MusicGraphBuilder {
	
	public static MusicGraphNode buildGraph(){
		MusicGraphNode first = new MusicGraphNode(get4BarDissonanceOption1(), get4BarChordsOption1(),get4BarDrumSequence());
		MusicGraphNode second = new MusicGraphNode(get4BarDissonanceOption2(), get4BarChordsOption2(),get4BarDrumSequence());
		MusicGraphNode third = new MusicGraphNode(get4BarDissonanceOption3(), get4BarChordsOption3(),get4BarDrumSequence());
		first.connectTo(second);
		first.connectTo(third);
		second.connectTo(third);
		return first;
	}
	
	
	
	private static MidiNoteSequence get4BarChordsOption1(){
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(0, 100);
		builder.appendChord(new Chord("C5 E5 G5"), 1);
		builder.appendChord(new Chord("C5 E5 A5"), 1);
		builder.appendChord(new Chord("C5 F5 A5"), 1);
		builder.appendChord(new Chord("B4 D5 G5"), 1);
		return builder.build();
	}
	
	private static MidiNoteSequence get4BarChordsOption2(){
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(0, 100);
		builder.appendChord(new Chord("C#5 E#5 G#5"), 1);
		builder.appendChord(new Chord("C#5 E#5 A#5"), 1);
		builder.appendChord(new Chord("C#5 F#5 A#5"), 1);
		builder.appendChord(new Chord("B#4 D#5 G#5"), 1);
		return builder.build();
	}
	
	private static MidiNoteSequence get4BarChordsOption3(){
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(0, 100);
		builder.appendChord(new Chord("Cb5 Eb5 Gb5"), 1);
		builder.appendChord(new Chord("Cb5 Eb5 Ab5"), 1);
		builder.appendChord(new Chord("Cb5 Fb5 Ab5"), 1);
		builder.appendChord(new Chord("Bb4 Db5 Gb5"), 1);
		return builder.build();
	}
	
	private static MidiNoteSequence get4BarDissonanceOption1(){
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(0, 100);
		builder.appendChord(new Chord("C#6 F#5"), 1);
		builder.appendChord(new Chord("C#6 F#5"), 1);
		builder.appendChord(new Chord("C#6 F#5"), 1);
		builder.appendChord(new Chord("C#6 F#5"), 1);
		return builder.build();
	}
	private static MidiNoteSequence get4BarDissonanceOption2(){
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(0, 100);
		builder.appendChord(new Chord("D6 G5"), 1);
		builder.appendChord(new Chord("D6 G5"), 1);
		builder.appendChord(new Chord("D6 G5"), 1);
		builder.appendChord(new Chord("D6 G5"), 1);
		return builder.build();
	}
	
	private static MidiNoteSequence get4BarDissonanceOption3(){
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(0, 100);
		builder.appendChord(new Chord("C6 F5"), 1);
		builder.appendChord(new Chord("C6 F5"), 1);
		builder.appendChord(new Chord("C6 F5"), 1);
		builder.appendChord(new Chord("C6 F5"), 1);
		return builder.build();
	}

	private static MidiNoteSequence get4BarDrumSequence() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(9, 90);
		for (int i = 0; i < 6; i++) {
			builder.appendChord(new Chord("C3 F#3"), NoteValue.EIGHTH);
			builder.appendNote("F#3", NoteValue.EIGHTH);
			builder.appendChord(new Chord("D3 F#3"), NoteValue.EIGHTH);
			builder.appendNote("F#3", NoteValue.EIGHTH);
		}
		builder.appendChord(new Chord("C3 F#3"), NoteValue.EIGHTH);
		builder.appendNote("F#3", NoteValue.EIGHTH);
		builder.appendChord(new Chord("D3 F#3"), NoteValue.EIGHTH);
		builder.appendNote("F#3", NoteValue.EIGHTH);
		builder.appendChord(new Chord("C3 F#3"), NoteValue.EIGHTH);
		builder.appendChord(new Chord("D3 D4"), NoteValue.EIGHTH);
		builder.appendChord(new Chord("D3 C4"), NoteValue.EIGHTH);
		builder.appendChord(new Chord("D3 A4"), NoteValue.EIGHTH);
		return builder.build();
	}
	
}
