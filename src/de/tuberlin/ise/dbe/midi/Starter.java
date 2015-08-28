/**
 * 
 */
package de.tuberlin.ise.dbe.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

import de.tuberlin.ise.dbe.midi.devices.MidiOutput;
import de.tuberlin.ise.dbe.midi.instruments.Instruments;
import de.tuberlin.ise.dbe.midi.music.Chord;
import de.tuberlin.ise.dbe.midi.music.MidiNoteSequence;
import de.tuberlin.ise.dbe.midi.music.MidiNoteSequenceBuilder;
import de.tuberlin.ise.dbe.midi.music.NoteValue;
import de.tuberlin.ise.dbe.midi.music.Tempo;

/**
 * @author Dave
 *
 */
public class Starter {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		playScheduler();
	
	}

	/**
	 * @throws Exception
	 * 
	 */
	private static void playScheduler() throws Exception {
		MidiDevice device;
		for(MidiDevice.Info info: MidiSystem.getMidiDeviceInfo()){
			device = MidiSystem.getMidiDevice(info);
			System.out.print(device.getClass().getSimpleName());
			if(device instanceof Synthesizer) System.out.println(" is a synthie");
			System.out.println();
			System.out.println(info);
		}
		
		
		MusicalMidiScheduler scheduler = new MusicalMidiScheduler(
				new MidiOutput(), new Tempo(144), 4, 4);
		scheduler.changeInstrument(Instruments.Bass.Electric_Bass_finger, 1);
		scheduler.changeInstrument(Instruments.Organs.Rock_Organ, 0);
		MidiNoteSequence drums = getDrumSequence();
		MidiNoteSequence organ = getOrganSequence();
		MidiNoteSequence bass = getBassSequence();
		scheduler.scheduleEventSequence(1, 0, drums);
		scheduler.scheduleEventSequence(1, 0, organ);
		scheduler.scheduleEventSequence(1, 0, bass);
		scheduler.startPlaybackNow(500);
		scheduler.scheduleEventSequence(9, 0, drums);
		scheduler.scheduleEventSequence(9, 0, organ);
		scheduler.scheduleEventSequence(9, 0, bass);

		
//		Thread.sleep(40000);
//		scheduler.terminate();
	}

	/**
	 * @return
	 */
	private static MidiNoteSequence getBassSequence() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(1, 127);
		builder.appendNote("C3", NoteValue.QUARTER, 1)
				.appendNote("C3", NoteValue.HALF)
				.appendNote("B2", NoteValue.EIGHTH);
		builder.appendNote("A2", NoteValue.QUARTER, 1)
				.appendNote("A2", NoteValue.HALF)
				.appendNote("G2", NoteValue.EIGHTH);
		builder.appendNote("F2", NoteValue.QUARTER, 1)
				.appendNote("F2", NoteValue.HALF)
				.appendNote("F#2", NoteValue.EIGHTH);
		builder.appendNote("G2", NoteValue.QUARTER, 1)
				.appendNote("G2", NoteValue.QUARTER, 1)
				.appendNote("A2", NoteValue.EIGHTH)
				.appendNote("B2", NoteValue.EIGHTH);

		builder.appendNote("C3", NoteValue.QUARTER, 1)
				.appendNote("C3", NoteValue.HALF)
				.appendNote("B2", NoteValue.EIGHTH);
		builder.appendNote("A2", NoteValue.QUARTER, 1)
				.appendNote("A2", NoteValue.HALF)
				.appendNote("G2", NoteValue.EIGHTH);
		builder.appendNote("F2", NoteValue.QUARTER, 1)
				.appendNote("F2", NoteValue.HALF)
				.appendNote("F#2", NoteValue.EIGHTH);
		builder.appendNote("G2", NoteValue.QUARTER, 1)
				.appendNote("G2", NoteValue.QUARTER, 1)
				.appendNote("A2", NoteValue.EIGHTH)
				.appendNote("B2", NoteValue.EIGHTH);

		return builder.build();
	}

	/**
	 * @return
	 */
	private static MidiNoteSequence getOrganSequence() {
		MidiNoteSequenceBuilder builder = new MidiNoteSequenceBuilder(0, 80);
		builder.appendChord(new Chord("C5 E5 G5"), 1);
		builder.appendChord(new Chord("C5 E5 A5"), 1);
		builder.appendChord(new Chord("C5 F5 A5"), 1);
		builder.appendChord(new Chord("B4 D5 G5"), 1);

		builder.appendChord(new Chord("C5 E5 G5"), 1);
		builder.appendChord(new Chord("C5 E5 A5"), 1);
		builder.appendChord(new Chord("C5 F5 A5"), 1);
		builder.appendChord(new Chord("B4 D5 G5"), 1);
		return builder.build();
	}

	/**
	 * @return
	 */
	private static MidiNoteSequence getDrumSequence() {
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
		builder.appendChord(new Chord("C3 F#3 C#4"), NoteValue.EIGHTH);
		for (int i = 0; i < 6; i++) {
			if (i != 0)
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
