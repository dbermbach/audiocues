/**
 * 
 */
package de.tuberlin.ise.dbe.midi.music;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link MidiTonePitch}s are immutable objects
 * 
 * @author Dave
 *
 */
public class MidiTonePitch {

	/** the midi tone value of this instance */
	private final int tone;

	/** object pool to avoid constantly creating the same instance */
	private static final Map<Integer, MidiTonePitch> tones = new HashMap<>();

	static {
		for (int i = 0; i < 128; i++) {
			tones.put(i, new MidiTonePitch(i));
		}
	}

	/**
	 * 
	 * @param tone
	 * @return an immutable {@link MidiTonePitch} for the respective
	 *         {@link TonePitch} object
	 */
	public static MidiTonePitch getTone(TonePitch tone) {
		return tones.get(tone.value());
	}

	/**
	 * parses Strings like C5, C#6, Cb4 and returns the corresponding
	 * {@link MidiTonePitch}. C5 corresponds to midi tone 60, so anything outside the
	 * interval C0 to G10 will throw a RuntimeException. The same goes for wrong
	 * input format in the parameter string.
	 * 
	 * @param tone
	 * @return
	 */
	public static MidiTonePitch getTone(String tone) {
		char[] chars = tone.toCharArray();
		TonePitch n = TonePitch.valueOf("" + chars[0]);
		int change = 0;
		int counter = 1;

		if (chars[counter] == 'b') {
			change = -1;
			counter++;
		} else if (chars[counter] == '#') {
			change = +1;
			counter++;
		}
		int octave = 0;
		if (chars.length == 2) {
			octave = Integer.parseInt("" + chars[1]);
		} else if (chars.length == 3) {
			if (counter == 1) {
				octave = Integer.parseInt(chars[1] + "" + chars[2]);
			} else if (counter == 2) {
				octave = Integer.parseInt("" + chars[2]);
			}
		} else if (chars.length == 4) {
			octave = Integer.parseInt(chars[2] + "" + chars[3]);
		}
		return getTone(n).transpose(change).changeOctave(octave - 5);

	}

	/**
	 * not visible outside this class as objects are statically created and
	 * pooled
	 * 
	 * @param tone
	 */
	private MidiTonePitch(int tone) {
		this.tone = tone;
	}

	/**
	 * 
	 * @return a {@link MidiTonePitch} one semi-tone lower. Will be null if the
	 *         resulting midi number is not in [0,127].
	 */
	public MidiTonePitch flat() {
		return tones.get(this.tone - 1);
	}

	/**
	 * 
	 * @return a {@link MidiTonePitch} one semi-tone higher. Will be null if the
	 *         resulting midi number is not in [0,127].
	 */
	public MidiTonePitch sharp() {
		return tones.get(this.tone + 1);
	}

	/**
	 * transposes the {@link MidiTonePitch} by the respective number of semi-tones
	 * (positive = higher, negative = lower)
	 * 
	 * @param semitones
	 * @return a {@link MidiTonePitch} changed according to parameter semitones. Will
	 *         be null if the resulting midi number is not in [0,127].
	 */
	public MidiTonePitch transpose(int semitones) {
		return tones.get(this.tone + semitones);
	}

	/**
	 * transposes the {@link MidiTonePitch} by the respective number of octaves
	 * (positive = higher, negative = lower)
	 * 
	 * @param octaves
	 * @return a {@link MidiTonePitch} changed according to parameter octaves. Will
	 *         be null if the resulting midi number is not in [0,127].
	 */
	public MidiTonePitch changeOctave(int octaves) {
		return transpose(octaves * 12);
	}

	/**
	 * 
	 * @return the midi tone value of this instance
	 */
	public int midiValue() {
		return this.tone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return tone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MidiTonePitch))
			return false;
		return ((MidiTonePitch) obj) == this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return TonePitch.values()[this.tone % 12].toString() + (this.tone / 12);
	}

	public static void main(String[] args) {
		MidiTonePitch n = getTone(TonePitch.C);
		System.out.println(n);
		System.out.println(n.transpose(-3));
		System.out.println(n.flat().flat().sharp());
		System.out.println(n.changeOctave(-2));
		System.out.println(getTone(TonePitch.G).changeOctave(5));
		System.out.println(getTone("Ab3"));
		System.out.println(n.midiValue());
	}

}
