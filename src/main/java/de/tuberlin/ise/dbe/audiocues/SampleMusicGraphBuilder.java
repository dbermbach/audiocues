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
public class SampleMusicGraphBuilder {
	/*
	 * channels:
	 * 0=>Pad
	 * 1=>Bass
	 * 2=>Guitar
	 * 3=>Melody
	 * 9=>Drums
	 */

	public static MusicGraphNode buildGraph() {
		MusicGraphNode[] intro = buildIntro();
		MusicGraphNode allWithMelodyEnd = getAllInParallelWithMelodyEnd();
		MusicGraphNode allWithoutMelodyEnd = getAllInParallelWithoutMelodyEnd();
		MusicGraphNode allNoMelody = getAllInParallelNoMelody();
		MusicGraphNode bridgeNoMelody = getBridgeWithoutMelody();
		MusicGraphNode bridgeMelodyNoEnd = getBridgeWithMelodyNoEnd();
		MusicGraphNode bridgeMelodyWithEnd = getBridgeWithMelodyWithEnd();
		
		intro[1].connectTo(allNoMelody);
		intro[1].connectTo(allWithMelodyEnd);
		intro[1].connectTo(allWithoutMelodyEnd);
		intro[1].connectTo(bridgeMelodyWithEnd);
		intro[1].connectTo(bridgeMelodyNoEnd);
		bridgeMelodyWithEnd.connectTo(bridgeMelodyNoEnd);
		allWithMelodyEnd.connectTo(allWithoutMelodyEnd);
		allWithoutMelodyEnd.connectTo(bridgeNoMelody);
		MusicGraphNode [] otherIntro = buildIntro();
		allWithoutMelodyEnd.connectTo(otherIntro[0]);
		otherIntro[1].connectTo(allWithMelodyEnd);
		
		
		
		return intro[0];
	}

	public static MusicGraphNode getAllInParallelWithMelodyEnd() {
		return new MusicGraphNode(getDissonanceSequence(), getPad(), getBass(),
				getBassDrum(), getHiHat(), getSnare(), getWoodblock(),
				getGuitarPicks(),getMelody());
	}
	
	public static MusicGraphNode getAllInParallelWithoutMelodyEnd() {
		return new MusicGraphNode(getDissonanceSequence(), getPad(), getBass(),
				getBassDrum(), getHiHat(), getSnare(), getWoodblock(),
				getGuitarPicks(),getMelodyWithoutEnd());
	}

	public static MusicGraphNode getAllInParallelNoMelody() {
		return new MusicGraphNode(getDissonanceSequence(), getPad(), getBass(),
				getBassDrum(), getHiHat(), getSnare(), getWoodblock(),
				getGuitarPicks());
	}
	

	public static MusicGraphNode getBridgeWithoutMelody() {
		return new MusicGraphNode(getDissonanceSequence(), getPad(),
				getBassAlternative(), getGuitarPicks(),getBassDrum());
	}
	
	public static MusicGraphNode getBridgeWithMelodyNoEnd() {
		return new MusicGraphNode(getDissonanceSequence(), getPad(),
				getBassAlternative(), getGuitarPicks(),getBassDrum(),getMelodyWithoutEnd());
	}
	
	public static MusicGraphNode getBridgeWithMelodyWithEnd() {
		return new MusicGraphNode(getDissonanceSequence(), getPad(),
				getBassAlternative(), getGuitarPicks(),getBassDrum(),getMelody());
	}

	public static MusicGraphNode[] buildIntro() {
		MusicGraphNode[] introNodes = new MusicGraphNode[9];
		introNodes[0] = new MusicGraphNode(getDissonanceSequence(), getPad(),
				getBassDrumFirstHalfMuted());
		introNodes[1] = new MusicGraphNode(getDissonanceSequence(), getPad(),
				getHiHat(), getBassDrum(), getWoodblockFirstHalfMuted());
		introNodes[2] = new MusicGraphNode(getDissonanceSequence(), getPad(),
				getBass(), getBassDrum(), getHiHat(), getWoodblock());
		introNodes[3] = new MusicGraphNode(getDissonanceSequence(), getPad(),
				getBass(), getBassDrum(), getHiHat(), getWoodblock(),
				getSnare());
		introNodes[4] = new MusicGraphNode(getDissonanceSequence(), getPad(),
				getBass(), getBassDrum(), getHiHat(), getWoodblock(),
				getSnare(), getGuitarPicks());
		introNodes[5] = getAllInParallelNoMelody();
		introNodes[6] = getAllInParallelWithMelodyEnd();
		introNodes[7] = getAllInParallelWithoutMelodyEnd();
		introNodes[8] = getAllInParallelNoMelody();

		for (int i = 0; i < introNodes.length - 1; i++) {
			introNodes[i].connectTo(introNodes[i + 1]);
		}
		MusicGraphNode[] res = new MusicGraphNode[2];
		res[0] = introNodes[0];
		res[1] = introNodes[introNodes.length - 1];
		return res;
	}

	private static MidiNoteSequence getPad() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(0, 100);
		builder.appendChord("A4 C5 E5 G5", 2); // Fmaj7
		builder.appendChord("G4 B4 D5 G5", 2); // Cmaj79
		builder.appendChord("A4 C5 E5 G5", 2);// Fmaj7
		builder.appendChord("G4 B4 D5 G5", 2);// Cmaj79
		builder.appendChord("A4 C5 E5 G5", 2);// Fmaj7
		builder.appendChord("G4 B4 D5 G5", 2);// Cmaj79
		builder.appendChord("F4 A4 C5 D5 F5", 2); // Dm7
		builder.appendChord("G4 A4 B4 D5 G5", 1); // Gadd9
		builder.appendChord("G4 A4 B4 D5 G5", 1); // E11
		return builder.build();
	}

	private static MidiNoteSequence getBassHalfs() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(1, 100);
		for (int i = 0; i < 4; i++)
			builder.appendNote("F3", 0.5);
		for (int i = 0; i < 4; i++)
			builder.appendNote("C3", 0.5);
		for (int i = 0; i < 4; i++)
			builder.appendNote("F3", 0.5);
		for (int i = 0; i < 4; i++)
			builder.appendNote("C3", 0.5);
		for (int i = 0; i < 4; i++)
			builder.appendNote("F3", 0.5);
		for (int i = 0; i < 4; i++)
			builder.appendNote("C3", 0.5);
		for (int i = 0; i < 4; i++)
			builder.appendNote("D3", 0.5);
		builder.appendNote("G3", 0.5);
		builder.appendNote("G3", 0.5);
		builder.appendNote("E3", 0.5);
		builder.appendNote("E3", 0.5);
		return builder.build();
	}

	private static MidiNoteSequence getBass() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(1, 100);
		builder.appendNote("F3", 0.75);
		builder.appendNote("F3", 1.25);
		builder.appendNote("C3", 0.75);
		builder.appendNote("C3", 1.25);
		builder.appendNote("F3", 0.75);
		builder.appendNote("F3", 1.25);
		builder.appendNote("C3", 0.75);
		builder.appendNote("C3", 1.25);
		builder.appendNote("F3", 0.75);
		builder.appendNote("F3", 1.25);
		builder.appendNote("C3", 0.75);
		builder.appendNote("C3", 1.25);

		builder.appendNote("D3", 0.75);
		builder.appendNote("D3", 1.25);
		builder.appendNote("G3", 0.75);
		builder.appendNote("E3", 1.25);

		return builder.build();
	}

	private static MidiNoteSequence getBassAlternative() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(1, 100);
		builder.appendNote("F3", 0.75);
		builder.appendNote("F3", 0.75);
		builder.appendNote("F3", 0.5);
		builder.appendNote("C3", 0.75);
		builder.appendNote("C3", 0.75);
		builder.appendNote("C3", 0.5);
		builder.appendNote("F3", 0.75);
		builder.appendNote("F3", 0.75);
		builder.appendNote("F3", 0.5);
		builder.appendNote("C3", 0.75);
		builder.appendNote("C3", 0.75);
		builder.appendNote("C3", 0.5);
		builder.appendNote("F3", 0.75);
		builder.appendNote("F3", 0.75);
		builder.appendNote("F3", 0.5);
		builder.appendNote("C3", 0.75);
		builder.appendNote("C3", 0.75);
		builder.appendNote("C3", 0.5);

		builder.appendNote("D3", 0.75);
		builder.appendNote("D3", 0.75);
		builder.appendNote("D3", 0.5);
		builder.appendNote("G3", 0.75);
		builder.appendNote("E3", 0.75);
		builder.appendNote("E3", 0.50);

		return builder.build();
	}

	private static MidiNoteSequence getMelody(){
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(3, 100);
		builder.appendChord("E6 C6 A5", 0.25); //first bar
		builder.appendChord("C6 A5", 0.125);
		builder.appendNote("A5", 1);
		builder.appendNote("A5", 0.125);
		builder.appendNote("A5", 0.125);
		builder.appendNote("A5", 0.125);
		builder.appendNote("C6", 0.125);
		builder.appendNote("E6", 0.125);
		
		builder.appendChord("D6 B5 G5", 0.75); //second bar
		builder.appendNote("B5", 0.125);
		builder.appendNote("G5", 0.4375);
		builder.appendNote("G5", 0.0625);
		builder.appendNote("B5", 0.0625);
		builder.appendNote("D6", 0.0625);
		builder.appendNote("G6", 0.125);
		builder.appendNote("G6", 0.125);
		builder.appendNote("D6", 0.125);
		builder.appendNote("B5", 0.125);
		
		builder.appendChord("E6 C6 A5", 0.25); //third bar
		builder.appendChord("C6 A5", 0.125);
		builder.appendNote("A5", 1);
		builder.appendNote("A5", 0.125);
		builder.appendNote("A5", 0.125);
		builder.appendNote("A5", 0.125);
		builder.appendNote("C6", 0.125);
		builder.appendNote("E6", 0.125);
		
		builder.appendChord("D6 B5 G5", 0.75); //fourth bar
		builder.appendNote("B5", 0.125);
		builder.appendNote("G5", 0.4375);
		builder.appendNote("G5", 0.0625);
		builder.appendNote("B5", 0.0625);
		builder.appendNote("D6", 0.0625);
		builder.appendNote("G6", 0.125);
		builder.appendNote("G6", 0.125);
		builder.appendNote("D6", 0.125);
		builder.appendNote("B5", 0.125);
		
		builder.appendChord("A6 E6 C6", 0.25); //fifth bar
		builder.appendChord("E6 C6", 0.125);
		builder.appendNote("C6", 0.75);
		builder.appendNote("A5", 0.125);
		builder.appendNote("C6", 0.125);
		builder.appendNote("E6", 0.125);
		builder.appendChord("A6 E6 C6", 0.125);
		builder.appendChord("A6 E6 C6", 0.125);
		builder.appendNote("E6", 0.125);
		builder.appendNote("C6", 0.125);
		
		builder.appendChord("G6 D6 B5", 0.75); //fourth bar
		builder.appendNote("D6", 0.125);
		builder.appendNote("B5", 0.25);
		builder.appendNote("G5", 0.25);
		builder.appendChord("D5 G5", 0.125);
		builder.appendNote("G5", 0.125);
		builder.appendNote("G5", 0.125);
		builder.appendNote("F5", 0.125);
		builder.appendNote("E5", 0.125);
		
		builder.appendChord("F5 C5 D5", 0.75); //fifth bar
		builder.appendChord("A5 F5 C5 D5", 0.75);
		builder.appendNote("A5", 0.125);
		builder.appendNote("A5", 0.125);
		builder.appendNote("G5", 0.125);
		builder.appendNote("F5", 0.125);
		
		builder.appendNote("G5",0.75); //sixth bar
		builder.appendNote("A5",0.125);
		builder.appendNote("B5",0.5);
		builder.appendNote("B5",0.125);
		builder.appendNote("C6",0.125);
		builder.appendNote("B5",0.125);
		builder.appendNote("C6",0.125);
		builder.appendNote("D6",0.125);
		
		return builder.build();
	}
	
	
	private static MidiNoteSequence getMelodyWithoutEnd(){
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(3, 100);
		builder.appendChord("E6 C6 A5", 0.25); //first bar
		builder.appendChord("C6 A5", 0.125);
		builder.appendNote("A5", 1);
		builder.appendNote("A5", 0.125);
		builder.appendNote("A5", 0.125);
		builder.appendNote("A5", 0.125);
		builder.appendNote("C6", 0.125);
		builder.appendNote("E6", 0.125);
		
		builder.appendChord("D6 B5 G5", 0.75); //second bar
		builder.appendNote("B5", 0.125);
		builder.appendNote("G5", 0.4375);
		builder.appendNote("G5", 0.0625);
		builder.appendNote("B5", 0.0625);
		builder.appendNote("D6", 0.0625);
		builder.appendNote("G6", 0.125);
		builder.appendNote("G6", 0.125);
		builder.appendNote("D6", 0.125);
		builder.appendNote("B5", 0.125);
		
		builder.appendChord("E6 C6 A5", 0.25); //third bar
		builder.appendChord("C6 A5", 0.125);
		builder.appendNote("A5", 1);
		builder.appendNote("A5", 0.125);
		builder.appendNote("A5", 0.125);
		builder.appendNote("A5", 0.125);
		builder.appendNote("C6", 0.125);
		builder.appendNote("E6", 0.125);
		
		builder.appendChord("D6 B5 G5", 0.75); //fourth bar
		builder.appendNote("B5", 0.125);
		builder.appendNote("G5", 0.4375);
		builder.appendNote("G5", 0.0625);
		builder.appendNote("B5", 0.0625);
		builder.appendNote("D6", 0.0625);
		builder.appendNote("G6", 0.125);
		builder.appendNote("G6", 0.125);
		builder.appendNote("D6", 0.125);
		builder.appendNote("B5", 0.125);
		
		builder.appendChord("A6 E6 C6", 0.25); //fifth bar
		builder.appendChord("E6 C6", 0.125);
		builder.appendNote("C6", 0.75);
		builder.appendNote("A5", 0.125);
		builder.appendNote("C6", 0.125);
		builder.appendNote("E6", 0.125);
		builder.appendChord("A6 E6 C6", 0.125);
		builder.appendChord("A6 E6 C6", 0.125);
		builder.appendNote("E6", 0.125);
		builder.appendNote("C6", 0.125);
		
		builder.appendChord("G6 D6 B5", 0.75); //fourth bar
		builder.appendNote("D6", 0.125);
		builder.appendNote("B5", 0.25);
		builder.appendNote("G5", 0.25);
		builder.appendChord("D5 G5", 0.125);
		builder.appendNote("G5", 0.125);
		builder.appendNote("G5", 0.125);
		builder.appendNote("F5", 0.125);
		builder.appendNote("E5", 0.125);
		
		builder.appendChord("F5 C5 D5", 0.75); //fifth bar
		builder.appendChord("A5 F5 C5 D5", 0.75);
		builder.appendNote("A5", 0.125);
		builder.appendNote("A5", 0.125);
		builder.appendNote("G5", 0.125);
		builder.appendNote("F5", 0.125);
		
		builder.appendNote("G5",0.75); //sixth bar
		builder.appendNote("A5",0.125);
		builder.appendNote("B5",1.125);
				
		return builder.build();
	}
	
	private static MidiNoteSequence getBassDrum() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(9, 100);
		for (int i = 0; i < 32; i++)
			builder.appendNote("C3", 0.5);

		return builder.build();
	}

	private static MidiNoteSequence getHiHat() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(9, 100);
		for (int i = 0; i < 64; i++) {
			builder.appendRest(0.125);
			builder.appendNote("F#3", 0.125);
		}

		return builder.build();
	}

	private static MidiNoteSequence getBassDrumFirstHalfMuted() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(9, 80);
		builder.appendRest(8);
		for (int i = 0; i < 16; i++) {
			builder.appendNote("C3", 0.25);
			builder.appendRest(0.25);
		}

		return builder.build();
	}

	private static MidiNoteSequence getSnare() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(9, 100);
		for (int i = 0; i < 32; i++) {
			builder.appendRest(0.25);
			builder.appendNote("D3", 0.25);
		}

		return builder.build();
	}

	private static MidiNoteSequence getWoodblockFirstHalfMuted() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(9, 40);
		builder.appendRest(8);
		for (int i = 0; i < 16; i++) {
			builder.appendRest(0.125);
			builder.appendNote("E6", 0.125);
			builder.appendNote("F6", 0.125);
			builder.appendRest(0.125);
		}

		return builder.build();
	}

	private static MidiNoteSequence getWoodblock() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(9, 40);
		for (int i = 0; i < 32; i++) {
			builder.appendRest(0.125);
			builder.appendNote("E6", 0.125);
			builder.appendNote("F6", 0.125);
			builder.appendRest(0.125);
		}

		return builder.build();
	}

	private static MidiNoteSequence getGuitarPicks() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(2, 80);
		for (int i = 0; i < 14; i++) {
			builder.appendNote("G5", 0.375);
			builder.appendNote("G5", 0.375);
			builder.appendNote("G5", 0.25);
		}
		builder.appendNote("F5", 0.375);
		builder.appendNote("F5", 0.375);
		builder.appendNote("F5", 0.25);
		builder.appendNote("G5", 0.375);
		builder.appendNote("G5", 0.375);
		builder.appendNote("G5", 0.25);

		return builder.build();
	}

	private static MidiNoteSequence getDissonanceSequence() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(0, 100);
		builder.appendChord("C#6 F#5", 2);
		builder.appendChord("C#6 G#5", 2);
		builder.appendChord("C#6 F#5", 2);
		builder.appendChord("C#6 G#5", 2);
		builder.appendChord("C#6 F#5", 2);
		builder.appendChord("C#6 G#5", 2);
		builder.appendChord("D#6 A#5", 2);
		builder.appendChord("D#6 G#5", 2);
		
		
		return builder.build();
	}

}
