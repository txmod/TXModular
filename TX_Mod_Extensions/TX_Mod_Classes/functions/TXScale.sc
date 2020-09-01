// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// These scales are mostly taken from the class Scale.sc by Lance Putnam

TXScale {

	classvar classData;

	*initClass{
		classData = ();
		classData.arrScalesSpecs = [
			// intervals
			["interval: 1 Semitone - Minor 2nd", [1, 11]],
			["interval: 2 Semitones - Major 2nd", [2, 10]],
			["interval: 3 Semitones - Minor 3rd", [3, 9]],
			["interval: 4 Semitones - Major 3rd", [4, 8]],
			["interval: 5 Semitones - Perfect 4th", [5, 7]],
			["interval: 6 Semitones - Tritone", [6, 6]],
			["interval: 7 Semitones - Perfect 5th", [7, 5]],
			["interval: 8 Semitones - Minor 6th", [8, 4]],
			["interval: 9 Semitones - Major 6th", [9, 3]],
			["interval: 10 Semitones - Minor 7th", [10, 2]],
			["interval: 11 Semitones - Major 7th", [11, 1]],
			["interval: 12 Semitones - Octave", [12]],

			// chords
			["chord: Major", [4, 3, 5]],
			["chord: Major 2 (9)", [ 2, 2, 3, 5 ] ],
			["chord: Major 4 (11)", [ 4, 1, 2, 5 ] ],
			["chord: Major 6 (13)", [ 4, 3, 2, 3 ] ],
			["chord: Dominant 7", [4, 3, 4, 1]],
			["chord: Major 7", [4, 3, 3, 2]],
			["chord: Minor", [3, 4, 5]],
			["chord: Minor 2 (9)", [ 2, 1, 4, 5 ] ],
			["chord: Minor 4 (11)", [ 3, 2, 2, 5 ] ],
			["chord: Minor 6 (13)", [ 3, 4, 1, 4 ] ],
			["chord: Minor 7", [3, 4, 3, 2]],
			["chord: Minor Major 7", [3, 4, 4, 1]],
			["chord: Minor 7 b5", [3, 3, 4, 2]],
			["chord: Diminished", [3, 3, 3, 3]],
			["chord: Major 7 augmented 5", [4, 4, 2, 2]],
			["chord: Suspended 2", [ 2, 5, 5 ] ],
			["chord: Suspended 4", [ 5, 2, 5 ] ],
			["chord: Dominant 7 sus 4", [5, 2, 4, 1]],

			// modes
			["mode: Ionian", [2,2,1,2,2,2,1]],
			["mode: Dorian", [2,1,2,2,2,1,2]],
			["mode: Phrygian", [1,2,2,2,1,2,2]],
			["mode: Lydian", [2,2,2,1,2,2,1]],
			["mode: Mixolydian", [2,2,1,2,2,1,2]],
			["mode: Aeolian", [2,1,2,2,1,2,2]],
			["mode: Locrian", [1,2,2,1,2,2,2]],
			["mode: Ionian 5", [2,2,3,2,3]],
			["mode: Dorian 5", [2,1,4,2,3]],
			["mode: Phrygian 5", [1,2,4,1,4]],
			["mode: Lydian 5", [2,2,3,2,3]],
			["mode: Mixolydian 5", [2,2,3,2,3]],
			["mode: Aeolian 5", [2,1,4,1,4]],
			["mode: Locrian 5", [1,2,3,2,4]],

			// scales
			[ "scale: Ahirbhairav", [ 1, 3, 1, 2, 2, 1 ] ],
			[ "scale: Augmented", [ 3, 1, 2, 1, 3, 1 ] ],
			[ "scale: Augmented 2", [ 1, 3, 1, 3, 1 ] ],
			[ "scale: Balinese 1", [ 2, 2, 3, 2, 3 ] ],
			[ "scale: Bartok", [ 2, 2, 1, 2, 1, 2 ] ],
			[ "scale: Bhairav", [ 1, 3, 1, 2, 1, 3 ] ],
			[ "scale: Blues", [ 3, 1, 1, 2, 2, 1, 2 ] ],
			[ "scale: Chinese", [ 4, 2, 1, 4 ] ],
			[ "scale: Chinese 1", [ 2, 2, 3, 2, 3 ] ],
			[ "scale: Chinese 2", [ 4, 2, 1, 4, 1 ] ],
			[ "scale: Chromatic", [ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 ] ],
			[ "scale: Diminished", [ 2, 1, 2, 1, 2, 1, 2, 1 ] ],
			[ "scale: Double Harmonic", [ 1, 3, 1, 2, 1, 3, 1 ] ],
			[ "scale: Egyptian", [ 2, 3, 2, 3, 2 ] ],
			[ "scale: Enigmatic", [ 1, 3, 2, 2, 2, 1, 1 ] ],
			[ "scale: Ethiopian", [ 2, 2, 1, 2, 1, 3, 1 ] ],
			[ "scale: Flamenco", [ 1, 2, 1, 1, 2, 1, 2, 2 ] ],
			[ "scale: Gong", [ 2, 2, 3, 2 ] ],
			[ "scale: Hex Aeolian", [ 3, 2, 2, 1, 2 ] ],
			[ "scale: Hex Dorian", [ 2, 1, 2, 2, 3 ] ],
			[ "scale: Hex Major 6", [ 2, 2, 1, 2, 2 ] ],
			[ "scale: Hex Major 7", [ 2, 2, 3, 2, 2 ] ],
			[ "scale: Hex Phrygian", [ 1, 2, 2, 3, 2 ] ],
			[ "scale: Hex Sus", [ 2, 3, 2, 2, 1 ] ],
			[ "scale: Hindu", [ 2, 2, 1, 2, 1, 2, 2 ] ],
			[ "scale: Hirajoshi", [ 2, 2, 3, 2, 3 ] ],
			[ "scale: Hungarian Gypsy", [ 2, 1, 3, 1, 1, 2, 2 ] ],
			[ "scale: Hungarian Major", [ 3, 1, 2, 1, 2, 1, 2 ] ],
			[ "scale: Hungarian Minor", [ 2, 1, 3, 1, 1, 3 ] ],
			[ "scale: Indian", [ 1, 2, 1, 3, 2, 2 ] ],
			[ "scale: Iwato", [ 1, 4, 1, 4, 2 ] ],
			[ "scale: Japanese", [ 1, 4, 2, 1, 4 ] ],
			[ "scale: Javanese", [ 1, 2, 2, 2, 2, 1, 2 ] ],
			[ "scale: Jiao", [ 3, 2, 3, 2 ] ],
			[ "scale: Kumai", [ 2, 1, 4, 2 ] ],
			[ "scale: Leading Whole Tone", [ 2, 2, 2, 2, 2, 1 ] ],
			[ "scale: Locrian Major", [ 2, 2, 1, 1, 2, 2, 2 ] ],
			[ "scale: Locrian Natural 2", [ 2, 1, 2, 1, 2, 2, 2 ] ],
			[ "scale: Locrian Super", [ 1, 2, 1, 2, 2, 2, 2 ] ],
			[ "scale: Locrian Ultra", [ 1, 2, 1, 2, 2, 1, 3 ] ],
			[ "scale: Locrian Major", [ 2, 2, 1, 1, 2, 2 ] ],
			[ "scale: Lydian Aug", [ 2, 2, 2, 2, 1, 1, 2 ] ],
			[ "scale: Lydian Dom", [ 2, 2, 2, 1, 2, 1, 2 ] ],
			[ "scale: Lydian Minor", [ 2, 2, 2, 1, 1, 2 ] ],
			[ "scale: Major", [ 2, 2, 1, 2, 2, 2, 1 ] ],
			[ "scale: Major 5", [ 2, 2, 3, 2, 3 ] ],
			[ "scale: Major Harmonic", [ 2, 2, 1, 2, 1, 3 ] ],
			[ "scale: Major Melodic", [ 2, 2, 1, 2, 1, 2 ] ],
			[ "scale: Major Pentatonic", [ 2, 2, 3, 2 ] ],
			[ "scale: Marva", [ 1, 3, 2, 1, 2, 2, 1 ] ],
			[ "scale: Minor Natural", [ 2, 1, 2, 2, 1, 2, 2 ] ],
			[ "scale: Minor 5", [ 3, 2, 2, 3, 2 ] ],
			[ "scale: Minor Harmonic", [ 2, 1, 2, 2, 1, 3 ] ],
			[ "scale: Minor Melodic Ascending", [ 2, 1, 2, 2, 2, 2 ] ],
			[ "scale: Minor Melodic Descending", [ 2, 1, 2, 2, 1, 2 ] ],
			[ "scale: Minor Pentatonic", [ 3, 2, 2, 3 ] ],
			[ "scale: Mixolydian Aug", [ 2, 2, 1, 3, 1, 1, 2 ] ],
			[ "scale: Neapolitan Major", [ 1, 2, 2, 2, 2, 2 ] ],
			[ "scale: Neapolitan Minor", [ 1, 2, 2, 2, 1, 3 ] ],
			[ "scale: Oriental", [ 1, 3, 1, 1, 3, 1, 2 ] ],
			[ "scale: Pelog", [ 1, 2, 4, 3, 2 ] ],
			[ "scale: Persian", [ 1, 3, 1, 1, 2, 3, 1 ] ],
			[ "scale: Phrygian Major", [ 1, 3, 1, 2, 1, 2, 2 ] ],
			[ "scale: Prometheus", [ 2, 2, 2, 5 ] ],
			[ "scale: Purvi", [ 1, 3, 2, 1, 1, 3 ] ],
			[ "scale: Ritusen", [ 2, 3, 2, 2 ] ],
			[ "scale: Romanian", [ 2, 1, 3, 1, 2, 1, 2 ] ],
			[ "scale: Romanian Minor", [ 2, 1, 3, 1, 2, 1 ] ],
			[ "scale: Scriabin", [ 1, 3, 3, 2 ] ],
			[ "scale: Semitone 3", [ 3, 3, 3, 3 ] ],
			[ "scale: Semitone 4", [ 4, 4, 4 ] ],
			[ "scale: Shang", [ 2, 3, 2, 3 ] ],
			[ "scale: Spanish", [ 1, 3, 1, 2, 1, 2 ] ],
			[ "scale: Spanish 8", [ 1, 2, 1, 1, 1, 2, 2, 2 ] ],
			[ "scale: Super Locrian", [ 1, 2, 1, 2, 2, 2 ] ],
			[ "scale: Symmetrical", [ 1, 2, 1, 2, 1, 2, 1, 2 ] ],
			[ "scale: Todi", [ 1, 2, 3, 1, 1, 3, 1 ] ],
			[ "scale: Whole Tone", [ 2, 2, 2, 2, 2, 2 ] ],
			[ "scale: Whole Tone Leading", [ 2, 2, 2, 2, 2, 1, 1 ] ],
			[ "scale: Yu", [ 3, 2, 2, 3 ] ],
			[ "scale: Zhi", [ 2, 3, 2, 2 ] ],
		];
		classData.arrScaleNames = classData.arrScalesSpecs.collect({ arg item, i;
			item.at(0);
		});
		classData.arrScaleNotes = classData.arrScalesSpecs.collect({ arg item, i;
			var arrNotes;
			// add a leading zero and integrate values
			arrNotes = item.at(1).deepCopy;
			([0] ++ arrNotes).integrate.select({arg item; item <12;}); // ignore >= 12
		});
	}

	*arrScaleNames {
		^classData.arrScaleNames;
	}

	*arrScaleNotes {
		^classData.arrScaleNotes;
	}

	*convertPreRev1002Ind {arg oldIndex;
		// old array used before dataBank.systemRevision = 1002
		var arr_OLD_ScaleNotes = [
			// intervals
			["interval: 1 semitone - minor 2nd", [1, 11]],
			["interval: 2 semitones - major 2nd", [2, 10]],
			["interval: 3 semitones - minor 3rd", [3, 9]],
			["interval: 4 semitones - major 3rd", [4, 8]],
			["interval: 5 semitones - perfect 4th", [5, 7]],
			["interval: 6 semitones - tritone", [6, 6]],
			["interval: 7 semitones - perfect 5th", [7, 5]],
			["interval: 8 semitones - minor 6th", [8, 4]],
			["interval: 9 semitones - major 6th", [9, 3]],
			["interval: 10 semitones - minor 7th", [10, 2]],
			["interval: 11 semitones - major 7th", [11, 1]],
			["interval: 12 semitones - octave", [12]],

			// chords
			["chord: major", [4,3,5]],
			["chord: dominant 7", [4,3,4,1]],
			["chord: major 7", [4,3,3,2]],
			["chord: minor", [3,4,5]],
			["chord: minor 7", [3,4,3,2]],
			["chord: minor major 7", [3,4,4,1]],
			["chord: minor 7 b5", [3,3,4,2]],
			["chord: diminished", [3,3,3,3]],

			// modes
			["mode: ionian", [2,2,1,2,2,2,1]],
			["mode: dorian", [2,1,2,2,2,1,2]],
			["mode: phrygian", [1,2,2,2,1,2,2]],
			["mode: lydian", [2,2,2,1,2,2,1]],
			["mode: mixolydian", [2,2,1,2,2,1,2]],
			["mode: aeolian", [2,1,2,2,1,2,2]],
			["mode: locrian", [1,2,2,1,2,2,2]],
			["mode: ionian 5", [2,2,3,2,3]],
			["mode: dorian 5", [2,1,4,2,3]],
			["mode: phrygian 5", [1,2,4,1,4]],
			["mode: lydian 5", [2,2,3,2,3]],
			["mode: mixolydian 5", [2,2,3,2,3]],
			["mode: aeolian 5", [2,1,4,1,4]],
			["mode: locrian 5", [1,2,3,2,4]],

			// scales
			["scale: augmented", [3,1,2,1,3,1]],
			["scale: balinese 1", [2,2,3,2,3]],
			["scale: blues", [3,1,1,2,2,1,2]],
			["scale: chinese 1", [2,2,3,2,3]],
			["scale: chinese 2", [4,2,1,4,1]],
			["scale: chromatic", [1,1,1,1,1,1,1,1,1,1,1,1]],
			["scale: diminished", [2,1,2,1,2,1,2,1]],
			["scale: enigmatic", [1,3,2,2,2,1,1]],
			["scale: double harmonic", [1,3,1,2,1,3,1]],
			["scale: ethiopian 1", [2,2,1,2,1,3,1]],
			["scale: flamenco", [1,2,1,1,2,1,2,2]],
			["scale: egyptian", [2,3,2,3,2]],
			["scale: hindu", [2,2,1,2,1,2,2]],
			["scale: hirajoshi", [2,2,3,2,3]],  // japanese
			["scale: hungarian gypsy", [2,1,3,1,1,2,2]],
			["scale: hungarian major", [3,1,2,1,2,1,2]],
			["scale: hungarian minor", [2,1,3,1,1,3,1]],
			["scale: indian", [1,2,1,3,2,2]],
			["scale: iwato", [1,4,1,4,2]], // japanese
			["scale: japanese", [1,4,2,1,4]],
			["scale: javanese", [1,2,2,2,2,1,2]],
			["scale: locrian major", [2,2,1,1,2,2,2]],
			["scale: locrian natural 2", [2,1,2,1,2,2,2]],
			["scale: locrian super", [1,2,1,2,2,2,2]],
			["scale: locrian ultra", [1,2,1,2,2,1,3]],
			["scale: lydian minor", [2,2,2,1,1,2,2]],
			["scale: lydian dom", [2,2,2,1,2,1,2]],
			["scale: lydian aug", [2,2,2,2,1,1,2]],
			["scale: major", [2,2,1,2,2,2,1]],
			["scale: major 5", [2,2,3,2,3]],
			["scale: major harmonic", [2,2,1,3,1,2,1]],
			["scale: marva", [1,3,2,1,2,2,1]],	// indian
			["scale: minor natural", [2,1,2,2,1,2,2]], // natural
			["scale: minor 5", [3,2,2,3,2]],
			["scale: minor harmonic", [2,1,2,2,1,3,1]],
			["scale: minor melodic ascending", [2,1,2,2,1,3,1]],
			["scale: minor melodic descending", [2,1,2,2,1,2,2]],
			["scale: mixolydian aug", [2,2,1,3,1,1,2]],
			["scale: neapolitan major", [1,2,2,2,2,2,1]],
			["scale: neapolitan minor", [1,2,2,2,1,3,1]],
			["scale: oriental", [1,3,1,1,3,1,2]],
			["scale: pelog", [1,2,4,3,2]], // balinese
			["scale: persian", [1,3,1,1,2,3,1]],
			["scale: phrygian major", [1,3,1,2,1,2,2]],
			["scale: romanian", [2,1,3,1,2,1,2]],
			["scale: semitone 3", [3,3,3,3]],
			["scale: semitone 4", [4,4,4]],
			["scale: spanish 8", [1,2,1,1,1,2,2,2]],
			["scale: symmetrical", [1,2,1,2,1,2,1,2]],
			["scale: todi", [1,2,3,1,1,3,1]], // indian
			["scale: whole tone", [2,2,2,2,2,2]],
			["scale: whole tone leading", [2,2,2,2,2,1,1]],
		];
		var count = 0;
		var oldName = arr_OLD_ScaleNotes[oldIndex][0];
		var newIndex = classData.arrScaleNames.detectIndex({arg item, i;
			item.containsi(oldName);
		});
		if (newIndex.isNil, {
			newIndex = 0;
			"Error: TXScale.convertPreRev1002Ind - invalid index".postln;
		});
		^newIndex;
} }

