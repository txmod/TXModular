// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXWaveform5St : TXModuleBase {

	classvar <>classData;

	var noteListTextView;
	var displayOption;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Waveform St";
		classData.moduleRate = "audio";
		classData.moduleType = "source";
		classData.arrAudSCInBusSpecs = [
			["FM input", 1, "inmodulator"],
		];
		classData.arrCtlSCInBusSpecs = [
			["Modify 1", 1, "modChange1", 0],
			["Modify 2", 1, "modChange2", 0],
			["Frequency", 1, "modFreq", 0],
			["Beats frequency", 1, "modBeatsFreq", 0],
			["Note select", 1, "modFreqSelector", 0],
			["Smoothtime 1", 1, "modLag", 0],
			["Smoothtime 2", 1, "modLag2", 0],
			["FM depth", 1, "modFMDepth", 0],
			["FM freq", 1, "modFMFreq", 0],
			["FM ratio", 1, "modFMRatio", 0],
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.timeSpec = ControlSpec(0.01, 30, \exp, 0, 1, units: " secs");
		classData.freqSpec = ControlSpec(0.midicps, 20000, \exponential);
		classData.fmFreqSpec = ControlSpec(0.01, 20000, \exponential);
		classData.beatsFreqSpec = ControlSpec(0,100);
		classData.fmRatioSpec = ControlSpec(0.01, 10);
		classData.fmDepthSpec = ControlSpec(0, 4);
		classData.guiWidth = 540;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		displayOption = "showWaveform";
		arrSynthArgSpecs = [
			["inmodulator", 0, 0],
			["out", 0, 0],
			["freq", 0.5, defLagTime],
			["freqMin", 0.midicps, defLagTime],
			["freqMax", 127.midicps, defLagTime],
			["beatsFreq", 0.2, defLagTime],
			["beatsFreqMin", 0, defLagTime],
			["beatsFreqMax", 5, defLagTime],
			["change1", 0.5, defLagTime],
			["change1Min", 0, defLagTime],
			["change1Max", 1, defLagTime],
			["change2", 0.5, defLagTime],
			["change2Min", 0, defLagTime],
			["change2Max", 1, defLagTime],
			["level", 0.5, defLagTime],
			["i_noteListTypeInd",12, \ir],
			["i_noteListRoot", 0, \ir],
			["i_noteListMin", 36, \ir],
			["i_noteListMax", 72, \ir],
			["i_noteListSize", 1, \ir],
			["freqSelector", 0.5, defLagTime],
			["lag", 0.5, defLagTime],
			["lagMin", 0.01, defLagTime],
			["lagMax", 1, defLagTime],
			["lag2", 0.5, defLagTime],
			["lag2Min", 0.01, defLagTime],
			["lag2Max", 1, defLagTime],
			["fmDepth", ControlSpec(0, 4).unmap(1), defLagTime],
			["fmDepthMin", 0, defLagTime],
			["fmDepthMax", 4, defLagTime],
			["fmFreq", 0.5, defLagTime],
			["fmFreqMin", 0.midicps, defLagTime],
			["fmFreqMax", 127.midicps, defLagTime],
			["fmRatio", ControlSpec(0.01, 10).unmap(1), defLagTime],
			["fmRatioMin", 0.01, defLagTime],
			["fmRatioMax", 10, defLagTime],
			["modFreq", 0, defLagTime],
			["modBeatsFreq", 0, defLagTime],
			["modChange1", 0, defLagTime],
			["modChange2", 0, defLagTime],
			["modFreqSelector", 0, defLagTime],
			["modLag", 0, defLagTime],
			["modLag2", 0, defLagTime],
			["modFMDepth", 0, defLagTime],
			["modFMFreq", 0, defLagTime],
			["modFMRatio", 0, defLagTime],
		];
		arrOptions = [0, 0, 0, 0, 0];
		arrOptionData = [
			// ind 0
			TXWaveForm.arrOptionData,
			// ind 1
			[
				["Off", {arg freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector;
					//( (freqMax/freqMin) ** ((freq + modFreq).max(0.0001).min(1)) ) * freqMin;
					(freq + modFreq).max(0).min(1).linexp(0, 1, freqMin, freqMax)
				}],
				["On", {arg freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector;
					var holdArray;
					// convert to cps
					holdArray = this.getNoteArray.midicps;
					Select.kr( (((freqSelector + modFreqSelector).max(0).min(1)) * holdArray.size), holdArray);
				}],
			],
			// ind 2
			[
				["None",
					{arg input, lagtime; input;}
				],
				["Linear - use time 1 for up and down smoothing",
					{arg input, lagtime; Ramp.kr(input, lagtime); }
				],
				["Exponential 1 - use time 1 for up and down smoothing",
					{arg input, lagtime; Lag.kr(input, lagtime); }
				],
				["Exponential 2 - use time 1 for up and down smoothing",
					{arg input, lagtime; Lag2.kr(input, lagtime); }
				],
				["Exponential 3 - use time 1 for up and down smoothing",
					{arg input, lagtime; Lag3.kr(input, lagtime); }
				],
				["Exponential 1 - use time 1 for up, time 2 for down smoothing",
					{arg input, lagtime, lagtime2; LagUD.kr(input, lagtime, lagtime2); }
				],
				["Exponential 2 - use time 1 for up, time 2 for down smoothing",
					{arg input, lagtime, lagtime2; Lag2UD.kr(input, lagtime, lagtime2); }
				],
				["Exponential 3 - use time 1 for up, time 2 for down smoothing",
					{arg input, lagtime, lagtime2; Lag3UD.kr(input, lagtime, lagtime2); }
				],
			],
			// ind 3
			 // amp compensation
			TXAmpComp.arrOptionData,
			// ind 4 Frequency Modulation
			[
				["Off - no Frequency Modulation (FM)", {1;}],
				["External- use FM audio input & FM depth",
					{arg inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth;
						var fmDepthSum = (fmDepth + modFMDepth).max(0).min(1).linlin(0, 1, fmDepthMin, fmDepthMax);
						2 ** (fmDepthSum * InFeedback.ar(inmodulator,1));
					}
				],
				["Internal - use sine wave, FM freq & FM depth",
					{arg inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth, fmFreq, fmFreqMin, fmFreqMax,  modFMFreq,
						fmRatio, fmRatioMin, fmRatioMax, modFMRatio, baseFreq;
						var fmDepthSum = (fmDepth + modFMDepth).max(0).min(1).linlin(0, 1, fmDepthMin, fmDepthMax);
						var fmFreqSum = (fmFreq + modFMFreq).max(0).min(1).linexp(0, 1, fmFreqMin, fmFreqMax);
						var fmWave = SinOsc.ar(fmFreqSum);
						2 ** (fmDepthSum * fmWave);
					}
				],
				["Internal - use sine wave, [FM ratio x main frequency] & FM depth",
					{arg inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth, fmFreq, fmFreqMin, fmFreqMax,  modFMFreq,
						fmRatio, fmRatioMin, fmRatioMax, modFMRatio, baseFreq;
						var fmDepthSum = (fmDepth + modFMDepth).max(0).min(1).linlin(0, 1, fmDepthMin, fmDepthMax);
						var fmRatioSum = (fmRatio + modFMRatio).max(0).min(1).linlin(0, 1, fmRatioMin, fmRatioMax);
						var fmWave = SinOsc.ar(fmRatioSum * baseFreq);
						2 ** (fmDepthSum * fmWave);
					}
				],
			],
		];
		synthDefFunc = { arg inmodulator, out, freq, freqMin, freqMax, beatsFreq, beatsFreqMin, beatsFreqMax,
			change1, change1Min, change1Max, change2, change2Min, change2Max, level,
			i_noteListTypeInd, i_noteListRoot, i_noteListMin,  i_noteListMax, i_noteListSize, freqSelector,
			lag, lagMin, lagMax, lag2, lag2Min, lag2Max, fmDepth, fmDepthMin, fmDepthMax,
			fmFreq, fmFreqMin, fmFreqMax, fmRatio, fmRatioMin, fmRatioMax,
			modFreq = 0, modBeatsFreq = 0, modChange1 = 0, modChange2 = 0, modFreqSelector = 0,
			modLag = 0, modLag2 = 0, modFMDepth=0, modFMFreq=0, modFMRatio=0;

			var outFreqFunc, outFreq, outBeatsFreq, outFreqLag, waveFunction, outChange1, outChange2,
			lagtime, lagtime2, freqModFunc, fmOut, outLagFunction, outVol, ampCompFunction, outAmpComp;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			waveFunction = this.getSynthOption(0);
			outFreqFunc = this.getSynthOption(1);
			outFreq = outFreqFunc.value(freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector);
			outBeatsFreq = 0.5 * (beatsFreqMin + ((beatsFreqMax - beatsFreqMin) * (beatsFreq + modBeatsFreq).max(0).min(1)));
			outChange1 = change1Min + ((change1Max - change1Min) * (change1 + modChange1).max(0).min(1));
			outChange2 = change2Min + ((change2Max - change2Min) * (change2 + modChange2).max(0).min(1));
			lagtime = ( (lagMax/lagMin) ** ((lag + modLag).max(0.001).min(1)) ) * lagMin;
			lagtime2 = ( (lag2Max/lag2Min) ** ((lag2 + modLag2).max(0.001).min(1)) ) * lag2Min;
			outLagFunction = this.getSynthOption(2);
			outFreqLag = outLagFunction.value(outFreq, lagtime, lagtime2);
			ampCompFunction = this.getSynthOption(3);
			outAmpComp = ampCompFunction.value(outFreq);
			outVol = startEnv * level * outAmpComp;
			freqModFunc = this.getSynthOption(4);
			fmOut = freqModFunc.value(inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth, fmFreq, fmFreqMin,
				fmFreqMax,  modFMFreq, fmRatio, fmRatioMin, fmRatioMax, modFMRatio, outFreqLag);
			// use TXClean to stop blowups
			Out.ar(out, [
				TXClean.ar(outVol *
					waveFunction.value(((outFreqLag + outBeatsFreq) * fmOut).max(0.midicps), outChange1, outChange2)),
				TXClean.ar(outVol *
					waveFunction.value(((outFreqLag - outBeatsFreq) * fmOut).max(0.midicps), outChange1, outChange2)),
			]);
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["SynthOptionListPlusMinus", "Waveform", arrOptionData, 0, nil,  160,{system.flagGuiUpd}],
			["TextViewDisplay", {TXWaveForm.arrDescriptions.at(arrOptions.at(0).asInteger);}],
			["TXMinMaxSliderSplit", "Modify 1", \unipolar, "change1", "change1Min", "change1Max"],
			["TXMinMaxSliderSplit", "Modify 2", \unipolar, "change2", "change2Min", "change2Max"],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
			["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 3],
			["TXMinMaxFreqNoteSldr", "Freq", classData.freqSpec,
				"freq", "freqMin", "freqMax", nil, TXFreqRangePresets.arrFreqRanges],
			["SynthOptionCheckBox", "Use note list instead of variable frequency", arrOptionData, 1, 400],
			["EZslider", "Note select", ControlSpec(0, 1), "freqSelector"],
			["TXPopupAction", "Chord/ scale", TXScale.arrScaleNames, "i_noteListTypeInd", {this.updateSynth;}, 400],
			["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
				"i_noteListRoot", {this.updateSynth;}, 140],
			["TXNoteRangeSlider", "Note range", "i_noteListMin", "i_noteListMax", {this.updateSynth;}, true],
			["TXStaticText", "Note count", {this.getNoteTotalText}, {arg view; noteListTextView = view;}],
			["TXMinMaxSliderSplit", "Beats Freq", classData.beatsFreqSpec,
				"beatsFreq", "beatsFreqMin", "beatsFreqMax"],
			["SynthOptionPopupPlusMinus", "Smoothing", arrOptionData, 2],
			["TXMinMaxSliderSplit", "Time 1", classData.timeSpec , "lag", "lagMin", "lagMax"],
			["TXMinMaxSliderSplit", "Time 2", classData.timeSpec , "lag2", "lag2Min", "lag2Max"],
			["SynthOptionListPlusMinus", "Freq modulation", arrOptionData, 4],
			["TXMinMaxSliderSplit", "FM depth", classData.fmDepthSpec,
				"fmDepth", "fmDepthMin", "fmDepthMax",],
			["TXMinMaxSliderSplit", "FM freq", classData.fmFreqSpec,
				"fmFreq", "fmFreqMin", "fmFreqMax",],
			["TXMinMaxSliderSplit", "FM ratio", classData.fmRatioSpec,
				"fmRatio", "fmRatioMin", "fmRatioMax",],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
		this.getNoteArray; // initialise
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Waveform", {displayOption = "showWaveform";
				this.buildGuiSpecArray; system.showView;}, 110,
			TXColor.white, this.getButtonColour(displayOption == "showWaveform")],
			["Spacer", 3],
			["ActionButton", "Frequency", {displayOption = "showFreq";
				this.buildGuiSpecArray; system.showView;}, 110,
			TXColor.white, this.getButtonColour(displayOption == "showFreq")],
			["Spacer", 3],
			["ActionButton", "Freq Smoothing", {displayOption = "showSmoothing";
				this.buildGuiSpecArray; system.showView;}, 110,
			TXColor.white, this.getButtonColour(displayOption == "showSmoothing")],
			["Spacer", 3],
			["ActionButton", "FM", {displayOption = "showFM";
				this.buildGuiSpecArray; system.showView;}, 110,
			TXColor.white, this.getButtonColour(displayOption == "showFM")],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 2],
		];

		if (displayOption == "showWaveform", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Waveform", arrOptionData, 0,  nil, 160, {system.flagGuiUpd}],
				["TextViewDisplay", {TXWaveForm.arrDescriptions.at(arrOptions.at(0).asInteger);}],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Modify 1", \unipolar, "change1", "change1Min", "change1Max"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Modify 2", \unipolar, "change2", "change2Min", "change2Max"],
				["SpacerLine", 6],
				["EZslider", "Level", ControlSpec(0, 1), "level"],
				["SpacerLine", 6],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 3],
			];
		});
		if (displayOption == "showFreq", {
			guiSpecArray = guiSpecArray ++[
				["TXMinMaxFreqNoteSldr", "Freq", classData.freqSpec,
					"freq", "freqMin", "freqMax", nil, TXFreqRangePresets.arrFreqRanges],
				["DividingLine"],
				["SpacerLine", 2],
				["SynthOptionCheckBox", "Use note list instead of variable frequency", arrOptionData, 1, 400],
				["SpacerLine", 2],
				["TXListViewActionPlusMinus", "Chord/ scale", TXScale.arrScaleNames, "i_noteListTypeInd",
					{this.updateSynth;}, 400, 120],
				["SpacerLine", 2],
				["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
					"i_noteListRoot", {this.updateSynth;}, 140],
				["SpacerLine", 2],
				["TXNoteRangeSlider", "Note range", "i_noteListMin", "i_noteListMax", {this.updateSynth;}, true],
				["SpacerLine", 2],
				["TXStaticText", "Note count", {this.getNoteTotalText}, {arg view; noteListTextView = view;}],
				["SpacerLine", 2],
				["EZslider", "Note select", ControlSpec(0, 1), "freqSelector"],
				["DividingLine"],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Beats Freq", classData.beatsFreqSpec,
					"beatsFreq", "beatsFreqMin", "beatsFreqMax"],
			];
		});
		if (displayOption == "showSmoothing", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Smoothing", arrOptionData, 2],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Time 1", classData.timeSpec , "lag", "lagMin", "lagMax"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Time 2", classData.timeSpec , "lag2", "lag2Min", "lag2Max"],
			];
		});
		if (displayOption == "showFM", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Freq modulation", arrOptionData, 4],
				["SpacerLine", 6],
				["TXMinMaxFreqNoteSldr", "FM freq", classData.fmFreqSpec,
					"fmFreq", "fmFreqMin", "fmFreqMax", nil, TXFreqRangePresets.arrFreqRanges],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "FM ratio", classData.fmRatioSpec,
					"fmRatio", "fmRatioMin", "fmRatioMax",],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "FM depth", classData.fmDepthSpec,
					"fmDepth", "fmDepthMin", "fmDepthMax",],
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
		13.do({arg octave;
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

