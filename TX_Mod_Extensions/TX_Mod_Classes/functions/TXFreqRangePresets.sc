// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXFreqRangePresets {

	*initClass{
		//
		// set class specific variables
		//
	}

	*arrFreqRanges {
		^ [
			["Range presets: ", [0.midicps, 127.midicps]],
			["MIDI Note Range 8.17 - 12543 hz", [0.midicps, 127.midicps]],
			["Full Audible range 40 - 20k hz", [40, 20000]],
			["Wide range 40 - 8k hz", [40, 8000]],
			["Low range 40 - 250 hz", [40, 250]],
			["Mid range 100 - 2k hz", [100, 2000]],
			["High range 1k - 6k hz", [1000, 6000]],

			["2 Octaves, C0 - C2", [24.midicps, 48.midicps]],
			["2 Octaves, C1 - C3", [36.midicps, 60.midicps]],
			["2 Octaves, C2 - C4", [48.midicps, 72.midicps]],
			["3 Octaves, C0 - C3", [24.midicps, 60.midicps]],
			["3 Octaves, C1 - C4", [36.midicps, 72.midicps]],
			["3 Octaves, C2 - C5", [48.midicps, 84.midicps]],
			["4 Octaves, C0 - C4", [24.midicps, 72.midicps]],
			["4 Octaves, C1 - C5", [36.midicps, 84.midicps]],
			["4 Octaves, C2 - C6", [48.midicps, 96.midicps]],

			[ "Bass Guitar C0 - C3", [ 24.midicps, 60.midicps]],
			[ "Bassoon A#0 - D#4", [ 34.midicps, 75.midicps]],
			[ "Cello C1 - E4", [ 36.midicps, 76.midicps]],
			[ "Clarinet D2 - A#5", [ 50.midicps, 94.midicps]],
			[ "Double Bass E0 - G3", [ 28.midicps, 67.midicps]],
			[ "Flute C3 - C6", [ 60.midicps, 96.midicps]],
			[ "French Horn A#0 - F4", [ 34.midicps, 77.midicps]],
			[ "Guitar E1 - E4", [ 40.midicps, 76.midicps]],
			[ "Harp C0 - G6", [ 24.midicps, 103.midicps]],
			[ "Harpsichord F0 - F5", [ 29.midicps, 89.midicps] ],
			[ "Oboe A#2 - G5", [ 58.midicps, 91.midicps]],
			[ "Organ C1 - C6", [ 36.midicps, 96.midicps]],
			[ "Piano A-1 - C7", [ 21.midicps, 108.midicps]],
			[ "Piccolo D4 - F#6", [ 74.midicps, 102.midicps]],
			[ "Timpani E1 - G2", [ 40.midicps, 55.midicps]],
			[ "Trumpet G2 - A#4", [ 55.midicps, 82.midicps]],
			[ "Vibraphone F2 - F5", [ 53.midicps, 89.midicps]],
			[ "Violin G2 - G6", [ 55.midicps, 103.midicps]],
			[ "Voice Female E2 - C5", [ 52.midicps, 84.midicps]],
			[ "Voice Male F1 - C4", [ 41.midicps, 72.midicps]],
		];
	}

	*arrMidiNoteRanges {
		^ [
			["Range presets: ", [0, 127]],
			["MIDI Note Range 8.17 - 12543 hz", [0, 127]],
			["Full Audible range 40 - 20k hz", [40, 20000]],
			["Wide range 40 - 8k hz", [40, 8000]],
			["Low range 40 - 250 hz", [40, 250]],
			["Mid range 100 - 2k hz", [100, 2000]],
			["High range 1k - 6k hz", [1000, 6000]],

			["2 Octaves, C0 - C2", [24, 48]],
			["2 Octaves, C1 - C3", [36, 60]],
			["2 Octaves, C2 - C4", [48, 72]],
			["3 Octaves, C0 - C3", [24, 60]],
			["3 Octaves, C1 - C4", [36, 72]],
			["3 Octaves, C2 - C5", [48, 84]],
			["4 Octaves, C0 - C4", [24, 72]],
			["4 Octaves, C1 - C5", [36, 84]],
			["4 Octaves, C2 - C6", [48, 96]],

			[ "Bass Guitar C0 - C3", [ 24, 60]],
			[ "Bassoon A#0 - D#4", [ 34, 75]],
			[ "Cello C1 - E4", [ 36, 76]],
			[ "Clarinet D2 - A#5", [ 50, 94]],
			[ "Double Bass E0 - G3", [ 28, 67]],
			[ "Flute C3 - C6", [ 60, 96]],
			[ "French Horn A#0 - F4", [ 34, 77]],
			[ "Guitar E1 - E4", [ 40, 76]],
			[ "Harp C0 - G6", [ 24, 103]],
			[ "Harpsichord F0 - F5", [ 29, 89] ],
			[ "Oboe A#2 - G5", [ 58, 91]],
			[ "Organ C1 - C6", [ 36, 96]],
			[ "Piano A-1 - C7", [ 21, 108]],
			[ "Piccolo D4 - F#6", [ 74, 102]],
			[ "Timpani E1 - G2", [ 40, 55]],
			[ "Trumpet G2 - A#4", [ 55, 82]],
			[ "Vibraphone F2 - F5", [ 53, 89]],
			[ "Violin G2 - G6", [ 55, 103]],
			[ "Voice Female E2 - C5", [ 52, 84]],
			[ "Voice Male F1 - C4", [ 41, 72]],
		];
	}

}
