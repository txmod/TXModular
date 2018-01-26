// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXScaleQuantise : TXModuleBase {

	classvar <>classData;

	var ratioView;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Scale Quantise";
		classData.moduleRate = "control";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Quantise", 1, "modQuantise", 0],
			["Steps", 1, "modSteps", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var recip127 = 127.reciprocal;
		var holdArrTuningSemis = TXIntonation.arrTuningSemitones;
		var arrScaleNotes = TXScale.arrScaleNotes;
		var arrScaleNames = TXScale.arrScaleNames.collect({arg item, i;
				item.asString ++ " - " ++ arrScaleNotes.at(i).size ++ " notes per oct";
			});

		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["quantise", 1, 0],
			["midiNoteMin", 0],
			["midiNoteMax", 127],
			["modQuantise", 0, 0],
];
		arrOptions = [0, 91, 0, 0]; // defaults:  key:C, scale:Major, tuning: equal temperament, input range:0-1
		arrOptionData = [
			// keys
			["C", "C# / Db", "D", "D# / Eb", "E", "F", "F# / Gb", "G", "G# / Ab", "A", "A# / Bb", "B"]
			.collect({arg item, i;
				[item, i];
			})
			,
			// scales
			arrScaleNames.collect({arg item, i;
				[item, arrScaleNotes[i]];
			})
			,
			// tunings
			TXIntonation.arrTuningNames.collect({arg item, i;
				[item, holdArrTuningSemis[i]];
			})
			,
			[// input ranges
				["Positive only: 0 to 1", [0, 1]],
				["Positive & Negative: -1 to 1", [-1, 1]],
				["Positive & Negative: -0.5 to 0.5", [-0.5, 0.5]],
				["Negative only: -1 to 0", [-1, 0]],
			],
		];
		synthDefFunc = { arg in, out, quantise, midiNoteMin, midiNoteMax, modQuantise = 0;

			var inSignal, outNorm, lookupIndex, quantiseSum, outQuant, outSignal;
			var keyInt = this.getSynthOption(0);
			var arrScaleDegrees = this.getSynthOption(1);
			var arrTuningSemis = this.getSynthOption(2);
			var minMaxRange = this.getSynthOption(3);
			var midiLookupBuf = this.getMidiLookupArray(keyInt, arrScaleDegrees, arrTuningSemis).as(LocalBuf);

			inSignal = TXClean.kr(In.kr(in,1));
			outNorm = inSignal.linlin(minMaxRange[0], minMaxRange[1], 0, 1, clip:'minmax');
			// signal as quantised midi note
			lookupIndex = (outNorm * 127).linlin(0, 127, midiNoteMin, midiNoteMax).round;
			// quantise
			quantiseSum = (quantise + modQuantise).max(0).min(1);
			outQuant = Index.kr(midiLookupBuf, lookupIndex);
			outQuant = outQuant * recip127;
			outSignal = (quantiseSum * outQuant) + ((1 - quantiseSum) * inSignal);
			Out.kr(out, TXClean.kr(outSignal));
		};
		guiSpecArray = [
			["TXCheckBox", "Quantise", "quantise", nil, 70],
			["Spacer", 8],
			["SynthOptionPopupPlusMinus", "Input range", arrOptionData, 3, 364],
			["SpacerLine", 4],
			["SynthOptionListPlusMinus", "Key", arrOptionData, 0, nil, 60],
			["SpacerLine", 4],
			["SynthOptionListPlusMinus", "Scale", arrOptionData, 1, nil, 180],
			["SpacerLine", 4],
			["SynthOptionListPlusMinus", "Intonation", arrOptionData, 2,
				nil, 90, {arg view; this.updateIntString(view.value)}],
			["TXStaticText", "Note ratios",
				{TXIntonation.arrScalesText.at(arrOptions.at(2));},
				{arg view; ratioView = view}],
			["SpacerLine", 4],
			["TXNoteRangeSlider", "Note range", "midiNoteMin", "midiNoteMax", nil, true],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	updateIntString{arg argIndex;
		if (ratioView.notNil, {
			if (ratioView.notClosed, {
				ratioView.string = TXIntonation.arrScalesText.at(argIndex);
			});
		});
	}
	getMidiLookupArray {arg keyInt, arrScaleDegrees, arrTuningSemis;
		//build a key/scale corrected midi note lookup array
		var arrIndices, arrTunedScaleDegrees;
		var arrScaleNotes, arrKeyNotes;
		arrTunedScaleDegrees = arrTuningSemis[arrScaleDegrees];
		arrIndices = (0..139); // extra octave required for transpose
		arrScaleNotes = arrIndices.nearestInScale(arrTunedScaleDegrees);
		// transpose to key and down 12
		arrKeyNotes = arrScaleNotes + keyInt - 12;
		// keep all in midi range 0-127
		arrKeyNotes = arrKeyNotes.select({arg item, i; (item >= 0) and: {(item <= 127)}});
		^arrKeyNotes;
	}

}

