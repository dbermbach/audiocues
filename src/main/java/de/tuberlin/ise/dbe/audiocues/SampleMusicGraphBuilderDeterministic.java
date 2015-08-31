/**
 * 
 */
package de.tuberlin.ise.dbe.audiocues;

import de.tuberlin.ise.dbe.midi.music.MidiNoteSequence;
import de.tuberlin.ise.dbe.midi.music.MidiNoteSequenceBuilder;

/**
 * <b>Please, note that while the software parts of this project are free for
 * use by anyone as long as the origin is named, this does not apply to the
 * music described through the sourcecode in this class. Any use of this music
 * in either its original or an adapted form are prohibited without the explicit
 * composer's consent. (c) David Bermbach 2015</b>
 * 
 * 
 * 
 * @author Dave
 *
 */
public class SampleMusicGraphBuilderDeterministic {
	/*
	 * channels: 0=>Pad 1=>Bass 2=>Guitar 3=>Melody 9=>Drums
	 */

	public static MusicGraphNode buildGraph() {
		MusicGraphNode[] intro = buildIntro();
		MusicGraphNode allWithMelodyEnd = getAllInParallelWithMelodyEnd(),allWithMelodyEnd2 = getAllInParallelWithMelodyEnd(),allWithMelodyEnd3 = getAllInParallelWithMelodyEnd(),allWithMelodyEnd4 = getAllInParallelWithMelodyEnd();
		MusicGraphNode allWithoutMelodyEnd = getAllInParallelWithoutMelodyEnd(),allWithoutMelodyEnd2 = getAllInParallelWithoutMelodyEnd();
		MusicGraphNode allNoMelody = getAllInParallelNoMelody(),allNoMelody2 = getAllInParallelNoMelody(),allNoMelody3 = getAllInParallelNoMelody();
		MusicGraphNode bridgeNoMelody = getBridgeWithoutMelody();
		MusicGraphNode bridgeMelodyNoEnd = getBridgeWithMelodyNoEnd();
		MusicGraphNode bridgeMelodyWithEnd = getBridgeWithMelodyWithEnd();

		intro[1].connectTo(allNoMelody, true);
		allNoMelody.connectTo(allWithMelodyEnd, true);
		allWithMelodyEnd.connectTo(allWithoutMelodyEnd, true);
		allWithoutMelodyEnd.connectTo(allNoMelody2, true);
		allNoMelody2.connectTo(allWithMelodyEnd2, true);
		allWithMelodyEnd2.connectTo(allWithoutMelodyEnd2, true);
		allWithoutMelodyEnd2.connectTo(bridgeNoMelody, true);
		bridgeNoMelody.connectTo(bridgeMelodyWithEnd, true);
		bridgeMelodyWithEnd.connectTo(bridgeMelodyNoEnd, true);
		bridgeMelodyNoEnd.connectTo(allNoMelody3, true);
		allNoMelody3.connectTo(allWithMelodyEnd3, true);
		allWithMelodyEnd3.connectTo(allWithMelodyEnd4, false); //while(true) ;)
		
		return intro[0];
	}

	public static MusicGraphNode getAllInParallelWithMelodyEnd() {
		return buildNode("all, w/o melody", Parts.PAD, Parts.BASSDRUM_PART2,
				Parts.BASSDRUM_PART1, Parts.HIHAT, Parts.WOODBLOCK_PART2,
				Parts.WOODBLOCK_PART1, Parts.BASS, Parts.SNARE, Parts.GUITAR,
				Parts.MELODY_W_END);
	}

	public static MusicGraphNode getAllInParallelWithoutMelodyEnd() {
		return buildNode("all, w/o melody", Parts.PAD, Parts.BASSDRUM_PART2,
				Parts.BASSDRUM_PART1, Parts.HIHAT, Parts.WOODBLOCK_PART2,
				Parts.WOODBLOCK_PART1, Parts.BASS, Parts.SNARE, Parts.GUITAR,
				Parts.MELODY_WO_END);
	}

	public static MusicGraphNode getAllInParallelNoMelody() {
		return buildNode("all, w/o melody", Parts.PAD, Parts.BASSDRUM_PART2,
				Parts.BASSDRUM_PART1, Parts.HIHAT, Parts.WOODBLOCK_PART2,
				Parts.WOODBLOCK_PART1, Parts.BASS, Parts.SNARE, Parts.GUITAR);
	}

	public static MusicGraphNode getBridgeWithoutMelody() {
		return buildNode("bridge: pad, alt. bass, git, bd", Parts.PAD,
				Parts.BASSDRUM_PART2, Parts.BASSDRUM_PART1, Parts.GUITAR,
				Parts.BASS_ALTERNATE);
	}

	public static MusicGraphNode getBridgeWithMelodyNoEnd() {
		return buildNode("bridge: pad, alt. bass, git, bd, melody w/o end",
				Parts.PAD, Parts.BASSDRUM_PART2, Parts.BASSDRUM_PART1,
				Parts.GUITAR, Parts.BASS_ALTERNATE, Parts.MELODY_WO_END);
	}

	public static MusicGraphNode getBridgeWithMelodyWithEnd() {
		return buildNode("bridge: pad, alt. bass, git, bd, melody w/ end",
				Parts.PAD, Parts.BASSDRUM_PART2, Parts.BASSDRUM_PART1,
				Parts.GUITAR, Parts.BASS_ALTERNATE, Parts.MELODY_W_END);
	}

	public static MusicGraphNode[] buildIntro() {
		MusicGraphNode[] introNodes = new MusicGraphNode[9];
		introNodes[0] = buildNode("pad, bd 2nd half", Parts.PAD,
				Parts.BASSDRUM_PART2);
		introNodes[1] = buildNode("pad, bd, hh, woodblock 2nd half", Parts.PAD,
				Parts.BASSDRUM_PART2, Parts.BASSDRUM_PART1, Parts.HIHAT,
				Parts.WOODBLOCK_PART2);
		introNodes[2] = buildNode("pad, bd, hh, woodblock, bass", Parts.PAD,
				Parts.BASSDRUM_PART2, Parts.BASSDRUM_PART1, Parts.HIHAT,
				Parts.WOODBLOCK_PART2, Parts.WOODBLOCK_PART1, Parts.BASS);
		introNodes[3] = buildNode("pad, bd, sn, hh, woodblock, bass",
				Parts.PAD, Parts.BASSDRUM_PART2, Parts.BASSDRUM_PART1,
				Parts.HIHAT, Parts.WOODBLOCK_PART2, Parts.WOODBLOCK_PART1,
				Parts.BASS, Parts.SNARE);
		introNodes[4] = buildNode("all, w/o melody", Parts.PAD,
				Parts.BASSDRUM_PART2, Parts.BASSDRUM_PART1, Parts.HIHAT,
				Parts.WOODBLOCK_PART2, Parts.WOODBLOCK_PART1, Parts.BASS,
				Parts.SNARE, Parts.GUITAR);
		introNodes[5] = getAllInParallelNoMelody();
		introNodes[6] = getAllInParallelWithMelodyEnd();
		introNodes[7] = getAllInParallelWithoutMelodyEnd();
		introNodes[8] = getAllInParallelNoMelody();

		for (int i = 0; i < introNodes.length - 1; i++) {
			introNodes[i].connectTo(introNodes[i + 1], true);
		}
		MusicGraphNode[] res = new MusicGraphNode[2];
		res[0] = introNodes[0];
		res[1] = introNodes[introNodes.length - 1];
		return res;
	}

	private static MusicGraphNode buildNode(String name, Parts... parts) {
		MusicGraphNode result = new MusicGraphNode(name);
		for (int i = 0; i < 8; i++) {
			MusicGraphNodeEntry entry = new MusicGraphNodeEntry(
					getDissonanceSequence(i));
			result.appendEntry(entry);
			for (Parts p : parts) {
				switch (p) {
				case PAD:
					entry.addSequence(getPad(i));
					break;
				case BASSDRUM_PART1:
					if (i < 4)
						entry.addSequence(getBassDrum());
					break;
				case BASSDRUM_PART2:
					if (i > 3)
						entry.addSequence(getBassDrum());
					break;
				case HIHAT:
					entry.addSequence(getHiHat());
					break;
				case WOODBLOCK_PART1:
					if (i < 4)
						entry.addSequence(getWoodblock());
					break;
				case WOODBLOCK_PART2:
					if (i > 3)
						entry.addSequence(getWoodblock());
					break;
				case SNARE:
					entry.addSequence(getSnare());
					break;
				case GUITAR:
					entry.addSequence(getGuitarPicks(i));
					break;
				case MELODY_W_END:
					entry.addSequence(getMelody(i));
					break;
				case MELODY_WO_END:
					entry.addSequence(getMelodyWithoutEnd(i));
					break;
				case BASS:
					entry.addSequence(getBass(i));
					break;
				case BASS_ALTERNATE:
					entry.addSequence(getBassAlternative(i));
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * 
	 * @return a 2 bar sequence out of a 16 bar pattern
	 */
	private static MidiNoteSequence getPad(int part) {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(0, 100);
		switch (part) {
		case 0:
		case 2:
		case 4:// Fmaj7
			builder.appendChord("A4 C5 E5 G5", 2);
			break;
		case 1:
		case 3:
		case 5: // Cmaj79
			builder.appendChord("G4 B4 D5 G5", 2);
			break;
		case 6: // Dm7
			builder.appendChord("F4 A4 C5 D5 F5", 2);
			break;
		case 7:// Gadd9 => E11
			builder.appendChord("G4 A4 B4 D5 G5", 1);
			builder.appendChord("G4 A4 B4 D5 G5", 1);
			break;
		default:
			throw new RuntimeException(
					"Only part numbers between 0 and 7 are supported.");
		}
		return builder.build();
	}

	/**
	 * 
	 * 
	 * @return a 2 bar sequence out of a 16 bar pattern
	 */
	private static MidiNoteSequence getBass(int part) {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(1, 100);
		switch (part) {
		case 0:
		case 2:
		case 4:
			builder.appendNote("F3", 0.75);
			builder.appendNote("F3", 1.25);
			break;
		case 1:
		case 3:
		case 5:
			builder.appendNote("C3", 0.75);
			builder.appendNote("C3", 1.25);
			break;
		case 6:
			builder.appendNote("D3", 0.75);
			builder.appendNote("D3", 1.25);
			break;
		case 7:
			builder.appendNote("G3", 0.75);
			builder.appendNote("E3", 1.25);
			break;
		default:
			throw new RuntimeException(
					"Only part numbers between 0 and 7 are supported.");
		}
		return builder.build();
	}

	/**
	 * 
	 * 
	 * @return a 2 bar sequence out of a 16 bar pattern
	 */
	private static MidiNoteSequence getBassAlternative(int part) {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(1, 100);
		switch (part) {
		case 0:
		case 2:
		case 4:
			builder.appendNote("F3", 0.75);
			builder.appendNote("F3", 0.75);
			builder.appendNote("F3", 0.5);
			break;
		case 1:
		case 3:
		case 5:
			builder.appendNote("C3", 0.75);
			builder.appendNote("C3", 0.75);
			builder.appendNote("C3", 0.5);
			break;
		case 6:
			builder.appendNote("D3", 0.75);
			builder.appendNote("D3", 0.75);
			builder.appendNote("D3", 0.5);
			break;
		case 7:
			builder.appendNote("G3", 0.75);
			builder.appendNote("E3", 0.75);
			builder.appendNote("E3", 0.50);
			break;
		default:
			throw new RuntimeException(
					"Only part numbers between 0 and 7 are supported.");
		}
		return builder.build();
	}

	/**
	 * 
	 * 
	 * @return a 2 bar sequence out of a 16 bar pattern
	 */
	private static MidiNoteSequence getMelody(int part) {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(3, 100);
		switch (part) {
		case 0:
			builder.appendChord("E6 C6 A5", 0.25); // first bar
			builder.appendChord("C6 A5", 0.125);
			builder.appendNote("A5", 1);
			builder.appendNote("A5", 0.125);
			builder.appendNote("A5", 0.125);
			builder.appendNote("A5", 0.125);
			builder.appendNote("C6", 0.125);
			builder.appendNote("E6", 0.125);
			break;
		case 1:

			builder.appendChord("D6 B5 G5", 0.75); // second bar
			builder.appendNote("B5", 0.125);
			builder.appendNote("G5", 0.4375);
			builder.appendNote("G5", 0.0625);
			builder.appendNote("B5", 0.0625);
			builder.appendNote("D6", 0.0625);
			builder.appendNote("G6", 0.125);
			builder.appendNote("G6", 0.125);
			builder.appendNote("D6", 0.125);
			builder.appendNote("B5", 0.125);
			break;

		case 2:
			builder.appendChord("E6 C6 A5", 0.25); // third bar
			builder.appendChord("C6 A5", 0.125);
			builder.appendNote("A5", 1);
			builder.appendNote("A5", 0.125);
			builder.appendNote("A5", 0.125);
			builder.appendNote("A5", 0.125);
			builder.appendNote("C6", 0.125);
			builder.appendNote("E6", 0.125);
			break;

		case 3:

			builder.appendChord("D6 B5 G5", 0.75); // fourth bar
			builder.appendNote("B5", 0.125);
			builder.appendNote("G5", 0.4375);
			builder.appendNote("G5", 0.0625);
			builder.appendNote("B5", 0.0625);
			builder.appendNote("D6", 0.0625);
			builder.appendNote("G6", 0.125);
			builder.appendNote("G6", 0.125);
			builder.appendNote("D6", 0.125);
			builder.appendNote("B5", 0.125);
			break;

		case 4:
			builder.appendChord("A6 E6 C6", 0.25); // fifth bar
			builder.appendChord("E6 C6", 0.125);
			builder.appendNote("C6", 0.75);
			builder.appendNote("A5", 0.125);
			builder.appendNote("C6", 0.125);
			builder.appendNote("E6", 0.125);
			builder.appendChord("A6 E6 C6", 0.125);
			builder.appendChord("A6 E6 C6", 0.125);
			builder.appendNote("E6", 0.125);
			builder.appendNote("C6", 0.125);
			break;
		case 5:
			builder.appendChord("G6 D6 B5", 0.75); // fourth bar
			builder.appendNote("D6", 0.125);
			builder.appendNote("B5", 0.25);
			builder.appendNote("G5", 0.25);
			builder.appendChord("D5 G5", 0.125);
			builder.appendNote("G5", 0.125);
			builder.appendNote("G5", 0.125);
			builder.appendNote("F5", 0.125);
			builder.appendNote("E5", 0.125);
			break;
		case 6:
			builder.appendChord("F5 C5 D5", 0.75); // fifth bar
			builder.appendChord("A5 F5 C5 D5", 0.75);
			builder.appendNote("A5", 0.125);
			builder.appendNote("A5", 0.125);
			builder.appendNote("G5", 0.125);
			builder.appendNote("F5", 0.125);
			break;
		case 7:
			builder.appendNote("G5", 0.75); // sixth bar
			builder.appendNote("A5", 0.125);
			builder.appendNote("B5", 0.5);
			builder.appendNote("B5", 0.125);
			builder.appendNote("C6", 0.125);
			builder.appendNote("B5", 0.125);
			builder.appendNote("C6", 0.125);
			builder.appendNote("D6", 0.125);
			break;
		default:
			throw new RuntimeException(
					"Only part numbers between 0 and 7 are supported.");

		}
		return builder.build();
	}

	/**
	 * 
	 * 
	 * @return a 2 bar sequence out of a 16 bar pattern
	 */
	private static MidiNoteSequence getMelodyWithoutEnd(int part) {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(3, 100);
		switch (part) {
		case 0:
			builder.appendChord("E6 C6 A5", 0.25); // first bar
			builder.appendChord("C6 A5", 0.125);
			builder.appendNote("A5", 1);
			builder.appendNote("A5", 0.125);
			builder.appendNote("A5", 0.125);
			builder.appendNote("A5", 0.125);
			builder.appendNote("C6", 0.125);
			builder.appendNote("E6", 0.125);
			break;
		case 1:

			builder.appendChord("D6 B5 G5", 0.75); // second bar
			builder.appendNote("B5", 0.125);
			builder.appendNote("G5", 0.4375);
			builder.appendNote("G5", 0.0625);
			builder.appendNote("B5", 0.0625);
			builder.appendNote("D6", 0.0625);
			builder.appendNote("G6", 0.125);
			builder.appendNote("G6", 0.125);
			builder.appendNote("D6", 0.125);
			builder.appendNote("B5", 0.125);
			break;

		case 2:
			builder.appendChord("E6 C6 A5", 0.25); // third bar
			builder.appendChord("C6 A5", 0.125);
			builder.appendNote("A5", 1);
			builder.appendNote("A5", 0.125);
			builder.appendNote("A5", 0.125);
			builder.appendNote("A5", 0.125);
			builder.appendNote("C6", 0.125);
			builder.appendNote("E6", 0.125);
			break;

		case 3:

			builder.appendChord("D6 B5 G5", 0.75); // fourth bar
			builder.appendNote("B5", 0.125);
			builder.appendNote("G5", 0.4375);
			builder.appendNote("G5", 0.0625);
			builder.appendNote("B5", 0.0625);
			builder.appendNote("D6", 0.0625);
			builder.appendNote("G6", 0.125);
			builder.appendNote("G6", 0.125);
			builder.appendNote("D6", 0.125);
			builder.appendNote("B5", 0.125);
			break;

		case 4:
			builder.appendChord("A6 E6 C6", 0.25); // fifth bar
			builder.appendChord("E6 C6", 0.125);
			builder.appendNote("C6", 0.75);
			builder.appendNote("A5", 0.125);
			builder.appendNote("C6", 0.125);
			builder.appendNote("E6", 0.125);
			builder.appendChord("A6 E6 C6", 0.125);
			builder.appendChord("A6 E6 C6", 0.125);
			builder.appendNote("E6", 0.125);
			builder.appendNote("C6", 0.125);
			break;
		case 5:
			builder.appendChord("G6 D6 B5", 0.75); // fourth bar
			builder.appendNote("D6", 0.125);
			builder.appendNote("B5", 0.25);
			builder.appendNote("G5", 0.25);
			builder.appendChord("D5 G5", 0.125);
			builder.appendNote("G5", 0.125);
			builder.appendNote("G5", 0.125);
			builder.appendNote("F5", 0.125);
			builder.appendNote("E5", 0.125);
			break;
		case 6:
			builder.appendChord("F5 C5 D5", 0.75); // fifth bar
			builder.appendChord("A5 F5 C5 D5", 0.75);
			builder.appendNote("A5", 0.125);
			builder.appendNote("A5", 0.125);
			builder.appendNote("G5", 0.125);
			builder.appendNote("F5", 0.125);
			break;
		case 7:
			builder.appendNote("G5", 0.75); // sixth bar
			builder.appendNote("A5", 0.125);
			builder.appendNote("B5", 1.125);
			break;
		default:
			throw new RuntimeException(
					"Only part numbers between 0 and 7 are supported.");

		}
		return builder.build();
	}

	/**
	 * 
	 * 
	 * @return a 2 bar sequence out of a 16 bar pattern
	 */
	private static MidiNoteSequence getBassDrum() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(9, 100);
		for (int i = 0; i < 4; i++)
			builder.appendNote("C3", 0.5);

		return builder.build();
	}

	/**
	 * 
	 * 
	 * @return a 2 bar sequence out of a 16 bar pattern
	 */
	private static MidiNoteSequence getHiHat() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(9, 100);
		for (int i = 0; i < 8; i++) {
			builder.appendRest(0.125);
			builder.appendNote("F#3", 0.125);
		}

		return builder.build();
	}

	/**
	 * 
	 * 
	 * @return a 2 bar sequence out of a 16 bar pattern
	 */
	private static MidiNoteSequence getSnare() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(9, 100);
		for (int i = 0; i < 4; i++) {
			builder.appendRest(0.25);
			builder.appendNote("D3", 0.25);
		}

		return builder.build();
	}

	/**
	 * 
	 * 
	 * @return a 2 bar sequence out of a 16 bar pattern
	 */
	private static MidiNoteSequence getWoodblock() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(9, 40);
		for (int i = 0; i < 4; i++) {
			builder.appendRest(0.125);
			builder.appendNote("E6", 0.125);
			builder.appendNote("F6", 0.125);
			builder.appendRest(0.125);
		}
		return builder.build();
	}

	/**
	 * 
	 * @param part
	 * @return a 2 bar sequence out of a 16 bar pattern
	 */
	private static MidiNoteSequence getGuitarPicks(int part) {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(2, 80);
		switch (part) {
		case 6:
			builder.appendNote("F5", 0.375);
			builder.appendNote("F5", 0.375);
			builder.appendNote("F5", 0.25);
			builder.appendNote("F5", 0.375);
			builder.appendNote("F5", 0.375);
			builder.appendNote("F5", 0.25);
			break;
		default:
			builder.appendNote("G5", 0.375);
			builder.appendNote("G5", 0.375);
			builder.appendNote("G5", 0.25);
			builder.appendNote("G5", 0.375);
			builder.appendNote("G5", 0.375);
			builder.appendNote("G5", 0.25);
			break;
		}
		return builder.build();
	}

	/**
	 * 
	 * @param part
	 * @return a 2 bar sequence out of a 16 bar pattern
	 */
	private static MidiNoteSequence getDissonanceSequence(int part) {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(0, 100);
		switch (part) {
		case 0:
		case 2:
		case 4:
			for(int i=0;i<4;i++)
			builder.appendChord("C#6 F#5", 0.5);
			break;
		case 1:
		case 3:
		case 5:
			for(int i=0;i<4;i++)
			builder.appendChord("C#6 G#5", 0.5);
			break;
		case 6:
			for(int i=0;i<4;i++)
			builder.appendChord("D#6 A#5", 0.5);
			break;
		case 7:
			for(int i=0;i<4;i++)
			builder.appendChord("D#6 G#5", 0.5);
			break;
		default:
			throw new RuntimeException(
					"Only part numbers between 0 and 7 are supported.");
		}
		return builder.build();
	}

	private static enum Parts {
		PAD, BASS, BASS_ALTERNATE, MELODY_W_END, MELODY_WO_END, HIHAT, SNARE, BASSDRUM_PART1, BASSDRUM_PART2, WOODBLOCK_PART1, WOODBLOCK_PART2, GUITAR;
	}
}
