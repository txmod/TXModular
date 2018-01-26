// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXIntonation {		// Intonation module

	classvar classData;

	*initClass{
		//
		// set class specific variables
		//
		classData = ();
		classData.arrJustScales = [
			[1, 16/15, 9/8, 6/5, 5/4, 4/3, 7/5, 3/2, 8/5, 5/3, 16/9, 15/8],
			[1, 16/15, 9/8, 6/5, 5/4, 4/3, 7/5, 3/2, 8/5, 5/3, 7/4, 15/8],
			[1, 16/15, 9/8, 7/6, 5/4, 4/3, 7/5, 3/2, 14/9, 5/3, 7/4, 15/8],
			[1, 16/15, 9/8, 6/5, 9/7, 4/3, 7/5, 3/2, 8/5, 5/3, 9/5, 15/8],
			[1, 16/15, 8/7, 6/5, 9/7, 4/3, 10/7, 3/2, 8/5, 12/7, 16/9, 15/8],
			[1, 16/15, 10/9, 6/5, 5/4, 4/3, 7/5, 3/2, 14/9, 5/3, 16/9, 15/8],
			[1, 16/15, 9/8, 6/5, 5/4, 4/3, 11/8, 3/2, 8/5, 5/3, 7/4, 15/8],
			[1, 16/15, 9/8, 6/5, 5/4, 4/3, 45/32, 3/2, 8/5, 5/3, 16/9, 15/8],
		];
		classData.scTuningNames = [ // tunings with 12 notes per octave
			'kirnberger', 'lu', 'mean4', 'mean5', 'mean6', 'pythagorean',
			'reinhard', 'sept1', 'sept2', 'vallotti', 'wcHarm',
			'wcSJ', 'werckmeister', 'young'];
		// note using double func {{}} because inside dict
		classData.noteFunc = {{arg note, key, scale;
			var fundNote, scaleNote;
			//	scale = array of 12 fractions (corresponding to semitones)
			//	key = 0-11, is root note of scale
			// fundNote is first note of scale in relevent octave
			fundNote = (12 * floor((note - key)/12)) + key;
			// scaleNote is no. of notes along from first note in scale
			scaleNote = (note - fundNote);
			// output frequency
			fundNote.midicps * Select.kr(scaleNote, scale);
		}};

	}

	*arrTuningSemitones{
		if (classData.arrTuningSemitones.isNil, {
			classData.arrTuningSemitones = [Tuning.at('et12').semitones]
			++ classData.arrJustScales.collect({arg item;
				Tuning.new(item.ratiomidi).semitones;
			})
			++ classData.scTuningNames.collect({arg item;
				Tuning.at(item).semitones;
			});
		});
		^classData.arrTuningSemitones;
	}

	*arrTuningNames{
		if (classData.arrTuningNames.isNil, {
			classData.arrTuningNames = ["equal temperament (default)"]
			++ classData.arrJustScales.collect({arg item, i;
				"just intonation " ++ (i+1).asString;
			})
			++ classData.scTuningNames.collect({arg item;
				item.asString;
			});
		});
		^classData.arrTuningNames;
	}

	*arrScalesText{
		if (classData.arrScalesText.isNil, {
			classData.arrScalesText = [
				"semitones spaced equally (using 12th root of 2)",
				"1 16/15 9/8 6/5 5/4 4/3 7/5 3/2 8/5 5/3 16/9 15/8",
				"1 16/15 9/8 6/5 5/4 4/3 7/5 3/2 8/5 5/3 7/4 15/8",
				"1 16/15 9/8 7/6 5/4 4/3 7/5 3/2 14/9 5/3 7/4 15/8",
				"1 16/15 9/8 6/5 9/7 4/3 7/5 3/2 8/5 5/3 9/5 15/8",
				"1 16/15 8/7 6/5 9/7 4/3 10/7 3/2 8/5 12/7 16/9 15/8",
				"1 16/15 10/9 6/5 5/4 4/3 7/5 3/2 14/9 5/3 16/9 15/8",
				"1 16/15 9/8 6/5 5/4 4/3 11/8 3/2 8/5 5/3 16/9 15/8",
				"1 16/15 9/8 6/5 5/4 4/3 45/32 3/2 8/5 5/3 16/9 15/8",
			]
			++ classData.scTuningNames.collect({arg item;
				var holdString = "1 ", fract;
				Tuning.at(item).ratios.do({arg item, i;
					if (i != 0, {
						fract = item.asFraction(100, false);
						holdString =  holdString ++ fract[0] ++ "/" ++ fract[1] ++ " ";
					});
				});
				holdString;
			});
		});
		^classData.arrScalesText;
	}

	*arrOptionData {
		^ [
			["equal temperament",
				{arg note, key; note.midicps; }
			],
			["just intonation - set 1",
				{arg note, key; classData.noteFunc.value(note, key, classData.arrJustScales[0]); }
			],
			["just intonation - set 2",
				{arg note, key; classData.noteFunc.value(note, key, classData.arrJustScales[1]); }
			],
			["just intonation - set 3",
				{arg note, key; classData.noteFunc.value(note, key, classData.arrJustScales[2]); }
			],
			["just intonation - set 4",
				{arg note, key; classData.noteFunc.value(note, key, classData.arrJustScales[3]); }
			],
			["just intonation - set 5",
				{arg note, key; classData.noteFunc.value(note, key, classData.arrJustScales[4]); }
			],
			["just intonation - set 6",
				{arg note, key; classData.noteFunc.value(note, key, classData.arrJustScales[5]); }
			],
			["just intonation - set 7",
				{arg note, key; classData.noteFunc.value(note, key, classData.arrJustScales[6]); }
			],
			["just intonation - set 8",
				{arg note, key; classData.noteFunc.value(note, key, classData.arrJustScales[7]); }
			],
			[classData.scTuningNames[0].asString,
				{arg note, key; classData.noteFunc.value(note, key, Tuning.at(classData.scTuningNames[0]).ratios); }
			],
			[classData.scTuningNames[1].asString,
				{arg note, key; classData.noteFunc.value(note, key, Tuning.at(classData.scTuningNames[1]).ratios); }
			],
			[classData.scTuningNames[2].asString,
				{arg note, key; classData.noteFunc.value(note, key, Tuning.at(classData.scTuningNames[2]).ratios); }
			],
			[classData.scTuningNames[3].asString,
				{arg note, key; classData.noteFunc.value(note, key, Tuning.at(classData.scTuningNames[3]).ratios); }
			],
			[classData.scTuningNames[4].asString,
				{arg note, key; classData.noteFunc.value(note, key, Tuning.at(classData.scTuningNames[4]).ratios); }
			],
			[classData.scTuningNames[5].asString,
				{arg note, key; classData.noteFunc.value(note, key, Tuning.at(classData.scTuningNames[5]).ratios); }
			],
			[classData.scTuningNames[6].asString,
				{arg note, key; classData.noteFunc.value(note, key, Tuning.at(classData.scTuningNames[6]).ratios); }
			],
			[classData.scTuningNames[7].asString,
				{arg note, key; classData.noteFunc.value(note, key, Tuning.at(classData.scTuningNames[7]).ratios); }
			],
			[classData.scTuningNames[8].asString,
				{arg note, key; classData.noteFunc.value(note, key, Tuning.at(classData.scTuningNames[8]).ratios); }
			],
			[classData.scTuningNames[9].asString,
				{arg note, key; classData.noteFunc.value(note, key, Tuning.at(classData.scTuningNames[9]).ratios); }
			],
			[classData.scTuningNames[10].asString,
				{arg note, key; classData.noteFunc.value(note, key, Tuning.at(classData.scTuningNames[10]).ratios); }
			],
			[classData.scTuningNames[11].asString,
				{arg note, key; classData.noteFunc.value(note, key, Tuning.at(classData.scTuningNames[11]).ratios); }
			],
			[classData.scTuningNames[12].asString,
				{arg note, key; classData.noteFunc.value(note, key, Tuning.at(classData.scTuningNames[12]).ratios); }
			],
			[classData.scTuningNames[13].asString,
				{arg note, key; classData.noteFunc.value(note, key, Tuning.at(classData.scTuningNames[13]).ratios); }
			],
		];
	}

}


