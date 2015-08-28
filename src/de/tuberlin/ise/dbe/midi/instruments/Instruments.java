/**
 * 
 */
package de.tuberlin.ise.dbe.midi.instruments;


/**
 * @author Dave
 *
 */
public class Instruments {

	public static enum Pianos implements Instrument {
		Acoustic_Grand_Piano, Bright_Acoustic_Piano, Electric_Grand_Piano, Honky_tonk_Piano, Electric_Piano_1, Electric_Piano_2, Harpsichord, Clavinet;

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tuberlin.ise.dbe.midi.music.Instrument#getMidiCode()
		 */
		@Override
		public int getMidiCode() {
			return this.ordinal() ;
		}
	}

	public static enum Percussions implements Instrument {
		Celesta, Glockenspiel, Music_Box, Vibraphone, Marimba, Xylophone, Tubular_Bells, Dulcimer;
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tuberlin.ise.dbe.midi.music.Instrument#getMidiCode()
		 */
		@Override
		public int getMidiCode() {
			return this.ordinal() + 8;
		}
	}

	public static enum Organs implements Instrument {
		Drawbar_Organ, Percussive_Organ, Rock_Organ, Church_Organ, Reed_Organ, Accordion, Harmonica, Tango_Accordion;
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tuberlin.ise.dbe.midi.music.Instrument#getMidiCode()
		 */
		@Override
		public int getMidiCode() {
			return this.ordinal() + 16;
		}
	}

	public static enum Guitars implements Instrument {
		Acoustic_Guitar_nylon, Acoustic_Guitar_steel, Electric_Guitar_jazz, Electric_Guitar_clean, Electric_Guitar_muted, Overdriven_Guitar, Distortion_Guitar, Guitar_harmonics;
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tuberlin.ise.dbe.midi.music.Instrument#getMidiCode()
		 */
		@Override
		public int getMidiCode() {
			return this.ordinal() + 24;
		}
	}

	public static enum Bass implements Instrument {
		Acoustic_Bass, Electric_Bass_finger, Electric_Bass_pick, Fretless_Bass, Slap_Bass_1, Slap_Bass_2, Synth_Bass_1, Synth_Bass_2;
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tuberlin.ise.dbe.midi.music.Instrument#getMidiCode()
		 */
		@Override
		public int getMidiCode() {
			return this.ordinal() + 32;
		}
	}

	public static enum ClassicOrchestra implements Instrument {
		Violin, Viola, Cello, Contrabass, Tremolo_Strings, Pizzicato_Strings, Orchestral_Harp, Timpani;
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tuberlin.ise.dbe.midi.music.Instrument#getMidiCode()
		 */
		@Override
		public int getMidiCode() {
			return this.ordinal() + 40;
		}
	}

	public static enum Ensembles implements Instrument {
		String_Ensemble_1, String_Ensemble_2, Synth_Strings_1, Synth_Strings_2, Choir_Aahs, Voice_Oohs, Synth_Voice, Orchestra_Hit;
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tuberlin.ise.dbe.midi.music.Instrument#getMidiCode()
		 */
		@Override
		public int getMidiCode() {
			return this.ordinal() + 48;
		}
	}

	public static enum Brass implements Instrument {
		Trumpet, Trombone, Tuba, Muted_Trumpet, French_Horn, Brass_Section, Synth_Brass_1, Synth_Brass_2;
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tuberlin.ise.dbe.midi.music.Instrument#getMidiCode()
		 */
		@Override
		public int getMidiCode() {
			return this.ordinal() + 56;
		}
	}

	public static enum Reeds implements Instrument {
		Soprano_Sax, Alto_Sax, Tenor_Sax, Baritone_Sax, Oboe, English_Horn, Bassoon, Clarinet;
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tuberlin.ise.dbe.midi.music.Instrument#getMidiCode()
		 */
		@Override
		public int getMidiCode() {
			return this.ordinal() + 64;
		}
	}

	public static enum Flutes implements Instrument {
		Piccolo, Flute, Recorder, Pan_Flute, Blown_Bottle, Shakuhachi, Whistle, Ocarina;
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tuberlin.ise.dbe.midi.music.Instrument#getMidiCode()
		 */
		@Override
		public int getMidiCode() {
			return this.ordinal() + 72;
		}
	}

	public static enum Synth implements Instrument {
		Lead_1_square, Lead_2_sawtooth, Lead_3_calliope, Lead_4_chiff, Lead_5_charang, Lead_6_voice, Lead_7_fifths, Lead_8_bass, Pad_1_new, Pad_2_warm, Pad_3_polysynth, Pad_4_choir, Pad_5_bowed, Pad_6_metallic, Pad_7_halo, Pad_8_sweep, FX_1_rain, FX_2_soundtrack, FX_3_crystal, FX_4_atmosphere, FX_5_brightness, FX_6_goblins, FX_7_echoes, FX_8_sci_fi;
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tuberlin.ise.dbe.midi.music.Instrument#getMidiCode()
		 */
		@Override
		public int getMidiCode() {
			return this.ordinal() + 80;
		}
	}

	public static enum Ethnic implements Instrument {
		Sitar, Banjo, Shamisen, Koto, Kalimba, Bag_pipe, Fiddle, Shanai;
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tuberlin.ise.dbe.midi.music.Instrument#getMidiCode()
		 */
		@Override
		public int getMidiCode() {
			return this.ordinal() + 104;
		}
	}

	public static enum MelodicDrums implements Instrument {
		Tinkle_Bell, Agogo, Steel_Drums, Woodblock, Taiko_Drum, Melodic_Tom, Synth_Drum;
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tuberlin.ise.dbe.midi.music.Instrument#getMidiCode()
		 */
		@Override
		public int getMidiCode() {
			return this.ordinal() + 112;
		}
	}

	public static enum Effects implements Instrument {
		Reverse_Cymbal, Guitar_Fret_Noise, Breath_Noise, Seashore, Bird_Tweet, Telephone_Ring, Helicopter, Applause, Gunshot;
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tuberlin.ise.dbe.midi.music.Instrument#getMidiCode()
		 */
		@Override
		public int getMidiCode() {
			return this.ordinal() + 119;
		}
	}

}
