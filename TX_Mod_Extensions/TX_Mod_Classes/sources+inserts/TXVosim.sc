// Copyright (C) 2008  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXVosim : TXModuleBase {

	classvar <>classData;

	var	noteListTextView;
	var	displayOption, holdLagControlSpec;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Vosim";
		classData.moduleRate = "audio";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Formant freq", 1, "modFormantFreq", 0],
			["Instability", 1, "modInstability", 0],
			["Wave cycles", 1, "modCycles", 0],
			["Decay factor", 1, "modDecayFactor", 0],
			["Frequency", 1, "modFreq", 0],
			["Note select", 1, "modFreqSelector", 0],
			["Smoothtime 1", 1, "modLag", 0],
			["Smoothtime 2", 1, "modLag2", 0]
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
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		displayOption = "showVosim";
		holdLagControlSpec = ControlSpec(0.01, 30, \exp, 0, 1, units: " secs");
		arrSynthArgSpecs = [
			["out", 0, 0],
			["formantFreq", 0.5, defLagTime],
			["formantFreqMin", 0.midicps, defLagTime],
			["formantFreqMax", 127.midicps, defLagTime],
			["instability", 0, defLagTime],
			["instabilityMin", 0, defLagTime],
			["instabilityMax", 1, defLagTime],
			["cycles", 2/9, defLagTime],
			["cyclesMin", 1, defLagTime],
			["cyclesMax", 10, defLagTime],
			["decayFactor", 0.99, defLagTime],
			["decayFactorMin", 0, defLagTime],
			["decayFactorMax", 1, defLagTime],
			["level", 0.5, defLagTime],
			["i_noteListTypeInd",12, \ir],
			["i_noteListRoot", 0, \ir],
			["i_noteListMin", 36, \ir],
			["i_noteListMax", 72, \ir],
			["i_noteListSize", 1, \ir],
			["freq", 0.25, defLagTime],
			["freqMin", 0.midicps, defLagTime],
			["freqMax", 127.midicps, defLagTime],
			["freqSelector", 0.5, defLagTime],
			["lag", 0.5, defLagTime],
			["lagMin", 0.01, defLagTime],
			["lagMax", 1, defLagTime],
			["lag2", 0.5, defLagTime],
			["lag2Min", 0.01, defLagTime],
			["lag2Max", 1, defLagTime],
			["modFormantFreq", 0, defLagTime],
			["modInstability", 0, defLagTime],
			["modCycles", 0, defLagTime],
			["modDecayFactor", 0, defLagTime],
			["modFreq", 0, defLagTime],
			["modFreqSelector", 0, defLagTime],
			["modLag", 0, defLagTime],
			["modLag2", 0, defLagTime],
		];
		arrOptions = [0, 0, 0];
		arrOptionData = [
			[
				["Off", {arg freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector;
					( (freqMax/freqMin) ** ((freq + modFreq).max(0.001).min(1)) ) * freqMin;
				}],
				["On", {arg freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector;
					var holdArray;
					// convert to cps
					holdArray = this.getNoteArray.midicps;
					Select.kr( (((freqSelector + modFreqSelector).max(0).min(1)) * holdArray.size), holdArray);
				}],
			],
			[
				["None",
					{arg input, lagtime; input;}
				],
				["Linear - use time 1 for up and down smoothing",
					{arg input, lagtime; Ramp.kr(input, lagtime); }
				],
				["Exp 1 - use time 1 for up and down smoothing",
					{arg input, lagtime; Lag.kr(input, lagtime); }
				],
				["Exp 2 - use time 1 for up and down smoothing",
					{arg input, lagtime; Lag2.kr(input, lagtime); }
				],
				["Exp 3 - use time 1 for up and down smoothing",
					{arg input, lagtime; Lag3.kr(input, lagtime); }
				],
				["Exp 1 - use time 1 for up, time 2 for down smoothing",
					{arg input, lagtime, lagtime2; LagUD.kr(input, lagtime, lagtime2); }
				],
				["Exp 2 - use time 1 for up, time 2 for down smoothing",
					{arg input, lagtime, lagtime2; Lag2UD.kr(input, lagtime, lagtime2); }
				],
				["Exp 3 - use time 1 for up, time 2 for down smoothing",
					{arg input, lagtime, lagtime2; Lag3UD.kr(input, lagtime, lagtime2); }
				],
			],
			 // amp compensation
			TXAmpComp.arrOptionData,
		];
		synthDefFunc = {
			arg out, formantFreq, formantFreqMin, formantFreqMax, instability, instabilityMin, instabilityMax,
			cycles, cyclesMin, cyclesMax, decayFactor, decayFactorMin, decayFactorMax, level,
			i_noteListTypeInd, i_noteListRoot, i_noteListMin,  i_noteListMax, i_noteListSize,
			freq, freqMin, freqMax, freqSelector,
			lag, lagMin, lagMax, lag2, lag2Min, lag2Max,
			modFormantFreq=0, modInstability=0, modCycles=0, modDecayFactor=0, modFreq=0, modFreqSelector=0, modLag=0, modLag2=0;

			var outFreqFunc, outFreq, outFreqLag, outFormantFreq, outFormantFreqFunc, outInstability, outCycles, outDecayFactor,
			lagtime, lagtime2, outLagFunction, outVol, ampCompFunction, outAmpComp;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			outFreqFunc = this.getSynthOption(0);
			outFreq = outFreqFunc.value(freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector);
			outFormantFreqFunc = {arg formantFreq, formantFreqMin, formantFreqMax, modFormantFreq;
				( (formantFreqMax/formantFreqMin) ** ((formantFreq + modFormantFreq).max(0.001).min(1)) ) * formantFreqMin;};
			outFormantFreq = outFormantFreqFunc.value(formantFreq, formantFreqMin, formantFreqMax, modFormantFreq);
			outInstability = instabilityMin + ((instabilityMax - instabilityMin) * (instability + modInstability).max(0).min(1));
			outCycles = cyclesMin + ((cyclesMax - cyclesMin) * (cycles + modCycles).max(0).min(1));
			outDecayFactor = decayFactorMin + ((decayFactorMax - decayFactorMin) * (decayFactor + modDecayFactor).max(0).min(1));
			lagtime = ( (lagMax/lagMin) ** ((lag + modLag).max(0.001).min(1)) ) * lagMin;
			lagtime2 = ( (lag2Max/lag2Min) ** ((lag2 + modLag2).max(0.001).min(1)) ) * lag2Min;
			outLagFunction = this.getSynthOption(1);
			outFreqLag = outLagFunction.value(outFreq, lagtime, lagtime2);
			ampCompFunction = this.getSynthOption(2);
			outAmpComp = ampCompFunction.value(outFreq);
			// use TXClean to stop blowups
			outVol = startEnv * level * outAmpComp;
			Out.ar(out, TXClean.ar(outVol *
				{VOSIM.ar(GaussTrig.ar(outFreqLag, outInstability), outFormantFreq, outCycles, outDecayFactor)}));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TXMinMaxFreqNoteSldr", "Formant freq", ControlSpec(0.midicps, 20000, \exponential),
				"formantFreq", "formantFreqMin", "formantFreqMax", nil, TXFreqRangePresets.arrFreqRanges],
			["TXMinMaxSliderSplit", "Instability", \unipolar, "instability", "instabilityMin", "instabilityMax"],
			["TXMinMaxSliderSplit", "Wave cycles", ControlSpec(1, 10), "cycles", "cyclesMin", "cyclesMax"],
			["TXMinMaxSliderSplit", "Decay Factor", \unipolar, "decayFactor", "decayFactorMin", "decayFactorMax"],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
			["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 2],
			["TXMinMaxFreqNoteSldr", "Freq", ControlSpec(0.midicps, 20000, \exponential),
				"freq", "freqMin", "freqMax", nil, TXFreqRangePresets.arrFreqRanges],
			["SynthOptionCheckBox", "Use note list instead of variable frequency", arrOptionData, 0, 400],
			["EZslider", "Note select", ControlSpec(0, 1), "freqSelector"],
			["TXPopupAction", "Chord/ scale", TXScale.arrScaleNames, "i_noteListTypeInd", {this.updateSynth;}, 400],
			["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
				"i_noteListRoot", {this.updateSynth;}, 140],
			["TXNoteRangeSlider", "Note range", "i_noteListMin", "i_noteListMax", {this.updateSynth;}, true],
			["TXStaticText", "Note count", {this.getNoteTotalText}, {arg view; noteListTextView = view;}],
			["SynthOptionPopupPlusMinus", "Smoothing", arrOptionData, 1],
			["TXMinMaxSliderSplit", "Time 1", holdLagControlSpec, "lag", "lagMin", "lagMax"],
			["TXMinMaxSliderSplit", "Time 2", holdLagControlSpec, "lag2", "lag2Min", "lag2Max"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
		this.getNoteArray; // initialise
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Vosim", {displayOption = "showVosim";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showVosim")],
			["Spacer", 3],
			["ActionButton", "Frequency", {displayOption = "showFreq";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showFreq")],
			["Spacer", 3],
			["ActionButton", "Freq Smoothing", {displayOption = "showSmoothing";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showSmoothing")],
			["Spacer", 3],
			["DividingLine"],
			["SpacerLine", 6],
		];

		if (displayOption == "showVosim", {
			guiSpecArray = guiSpecArray ++[
				["TXMinMaxFreqNoteSldr", "Formant freq", ControlSpec(0.midicps, 20000, \exponential),
					"formantFreq", "formantFreqMin", "formantFreqMax", nil, TXFreqRangePresets.arrFreqRanges],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Instability", \unipolar, "instability", "instabilityMin", "instabilityMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Wave cycles", ControlSpec(1, 10), "cycles", "cyclesMin", "cyclesMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Decay Factor", \unipolar, "decayFactor", "decayFactorMin", "decayFactorMax"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0, 1), "level"],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 2],
			];
		});
		if (displayOption == "showFreq", {
			guiSpecArray = guiSpecArray ++[
				["TXMinMaxFreqNoteSldr", "Freq", ControlSpec(0.midicps, 20000, \exponential),
					"freq", "freqMin", "freqMax", nil, TXFreqRangePresets.arrFreqRanges],
				["DividingLine"],
				["SpacerLine", 2],
				["SynthOptionCheckBox", "Use note list instead of variable frequency", arrOptionData, 0, 400],
				["SpacerLine", 2],
				["TXListViewActionPlusMinus", "Chord/ scale", TXScale.arrScaleNames, "i_noteListTypeInd",
					{this.updateSynth;}, 400],
				["SpacerLine", 2],
				["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
					"i_noteListRoot", {this.updateSynth;}, 140],
				["SpacerLine", 2],
				["TXNoteRangeSlider", "Note range", "i_noteListMin", "i_noteListMax", {this.updateSynth;}, true],
				["SpacerLine", 2],
				["TXStaticText", "Note count", {this.getNoteTotalText}, {arg view; noteListTextView = view;}],
				["SpacerLine", 2],
				["EZslider", "Note select", ControlSpec(0, 1), "freqSelector"],
			];
		});
		if (displayOption == "showSmoothing", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Smoothing", arrOptionData, 1],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Time 1", holdLagControlSpec, "lag", "lagMin", "lagMax"],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Time 2", holdLagControlSpec, "lag2", "lag2Min", "lag2Max"],
			];
		});
	}

	getButtonColour { arg colour2Boolean;
		if (colour2Boolean == true, {
			^TXColor.sysGuiCol4;
		},{
			^TXColor.sysGuiCol1;
		});
	}

	getNoteArray {
		var arrScaleSpec, scaleRoot, noteMin, noteMax, arrScaleNotes;
		// Generate array of notes from chord, mode, scale
		arrScaleSpec = TXScale.arrScaleNotes.at(this.getSynthArgSpec("i_noteListTypeInd"));
		scaleRoot = this.getSynthArgSpec("i_noteListRoot");
		noteMin = this.getSynthArgSpec("i_noteListMin");
		noteMax = this.getSynthArgSpec("i_noteListMax");
		arrScaleNotes = [];
		11.do({arg octave;
			arrScaleSpec.do({arg item, i;
				arrScaleNotes = arrScaleNotes.add((octave * 12) + scaleRoot + item);
			});
		});
		arrScaleNotes = arrScaleNotes.select({arg item, i; ((item >= noteMin) and: (item <= noteMax)); });
		this.setSynthArgSpec("i_noteListSize", arrScaleNotes.size);
		if (arrScaleNotes.size == 0, {
			arrScaleNotes = [noteMin];
		});
		^arrScaleNotes;
	}

	getNoteTotalText {
		var noteListSize, outText;
		noteListSize = this.getSynthArgSpec("i_noteListSize");
		if (noteListSize == 0, {
			outText = "ERROR: No notes in note list - need to widen range ";
		}, {
			outText = "Total no. of notes:  " ++ noteListSize.asString;
		});
		^outText;
	}

	updateSynth {
		this.getNoteArray;
		this.rebuildSynth;
		if (noteListTextView.notNil, {
			{noteListTextView.string = this.getNoteTotalText;}.defer();
		});
	}

	loadExtraData {arg argData, isPresetData = false;
		var oldInd, newInd;
		// adjust older systems
		if (system.dataBank.savedSystemRevision < 1002, {
			oldInd = this.getSynthArgSpec("i_noteListTypeInd");
			newInd = TXScale.convertPreRev1002Ind(oldInd);
			this.setSynthArgSpec("i_noteListTypeInd", newInd);
		});
	}
}
