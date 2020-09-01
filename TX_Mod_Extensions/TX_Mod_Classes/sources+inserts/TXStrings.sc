// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXStrings : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var midiNoteOnFunc;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Strings";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Trigger", 1, "extTrigger", nil],
			["Input gain", 1, "modInGain", 0],
			["Pitch bend", 1, "modPitchBend", 0],
			["Max delay", 1, "modMaxDelay", 0],
			["Random delay", 1, "modRandDelay", 0],
			["Max decay", 1, "modMaxDecay", 0],
			["Freq 1", 1, "modFreq1", 0],
			["Amp 1", 1, "modAmp1", 0],
			["Filter 1", 1, "modFilter1", 0],
			["Delay 1", 1, "modDelay1", 0],
			["Decay 1", 1, "modDecay1", 0],
			["Freq 2", 1, "modFreq2", 0],
			["Amp 2", 1, "modAmp2", 0],
			["Filter 2", 1, "modFilter2", 0],
			["Delay 2", 1, "modDelay2", 0],
			["Decay 2", 1, "modDecay2", 0],
			["Freq 3", 1, "modFreq3", 0],
			["Amp 3", 1, "modAmp3", 0],
			["Filter 3", 1, "modFilter3", 0],
			["Delay 3", 1, "modDelay3", 0],
			["Decay 3", 1, "modDecay3", 0],
			["Freq 4", 1, "modFreq4", 0],
			["Amp 4", 1, "modAmp4", 0],
			["Filter 4", 1, "modFilter4", 0],
			["Delay 4", 1, "modDelay4", 0],
			["Decay 4", 1, "modDecay4", 0],
			["Freq 5", 1, "modFreq5", 0],
			["Amp 5", 1, "modAmp5", 0],
			["Filter 5", 1, "modFilter5", 0],
			["Delay 5", 1, "modDelay5", 0],
			["Decay 5", 1, "modDecay5", 0],
			["Freq 6", 1, "modFreq6", 0],
			["Amp 6", 1, "modAmp6", 0],
			["Filter 6", 1, "modFilter6", 0],
			["Delay 6", 1, "modDelay6", 0],
			["Decay 6", 1, "modDecay6", 0],
			["Freq 7", 1, "modFreq7", 0],
			["Amp 7", 1, "modAmp7", 0],
			["Filter 7", 1, "modFilter7", 0],
			["Delay 7", 1, "modDelay7", 0],
			["Decay 7", 1, "modDecay7", 0],
			["Freq 8", 1, "modFreq8", 0],
			["Amp 8", 1, "modAmp8", 0],
			["Filter 8", 1, "modFilter8", 0],
			["Delay 8", 1, "modDelay8", 0],
			["Decay 8", 1, "modDecay8", 0],
			["Output gain", 1, "modOutGain", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.guiWidth = 700;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showGain";
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["t_userTrig", 0, 0],
			["extTrigger", 0, 0],
			["inGain", 1, defLagTime],
			["freqMin", 40, defLagTime],
			["freqMax", 100, defLagTime],
			["quantise", 0, defLagTime],
			["pitchBend", 0.5, defLagTime],
			["pitchBendMin", -12, defLagTime],
			["pitchBendMax", 12, defLagTime],
			["maxDelay", 0.5, defLagTime],
			["maxDelayMin", 0.01, defLagTime],
			["maxDelayMax", 1, defLagTime],
			["randDelay", 0, defLagTime],
			["maxDecay", 0.7, defLagTime],
			["maxDecayMin", 0.01, defLagTime],
			["maxDecayMax", 10, defLagTime],
			["freq1", 0.3, defLagTime],
			["amp1", 0.25, defLagTime],
			["filter1", 0.05, defLagTime],
			["delay1", 0, defLagTime],
			["decay1", 0.75, defLagTime],
			["freq2", 0.35, defLagTime],
			["amp2", 0.25, defLagTime],
			["filter2", 0.05, defLagTime],
			["delay2", 0, defLagTime],
			["decay2", 0.75, defLagTime],
			["freq3", 0.4, defLagTime],
			["amp3", 0.25, defLagTime],
			["filter3", 0.05, defLagTime],
			["delay3", 0, defLagTime],
			["decay3", 0.75, defLagTime],
			["freq4", 0.45, defLagTime],
			["amp4", 0.25, defLagTime],
			["filter4", 0.05, defLagTime],
			["delay4", 0, defLagTime],
			["decay4", 0.75, defLagTime],
			["freq5", 0.5, defLagTime],
			["amp5", 0.25, defLagTime],
			["filter5", 0.05, defLagTime],
			["delay5", 0, defLagTime],
			["decay5", 0.75, defLagTime],
			["freq6", 0.55, defLagTime],
			["amp6", 0.25, defLagTime],
			["filter6", 0.05, defLagTime],
			["delay6", 0, defLagTime],
			["decay6", 0.75, defLagTime],
			["freq7", 0.6, defLagTime],
			["amp7", 0.25, defLagTime],
			["filter7", 0.05, defLagTime],
			["delay7", 0, defLagTime],
			["decay7", 0.75, defLagTime],
			["freq8", 0.65, defLagTime],
			["amp8", 0.25, defLagTime],
			["filter8", 0.05, defLagTime],
			["delay8", 0, defLagTime],
			["decay8", 0.75, defLagTime],
			["outGain", 1, defLagTime],
			["modInGain", 0, defLagTime],
			["modPitchBend", 0, defLagTime],
			["modMaxDelay", 0, defLagTime],
			["modRandDelay", 0, defLagTime],
			["modMaxDecay", 0, defLagTime],
			["modFreq1", 0, defLagTime],
			["modAmp1", 0, defLagTime],
			["modFilter1", 0, defLagTime],
			["modDelay1", 0, defLagTime],
			["modDecay1", 0, defLagTime],
			["modFreq2", 0, defLagTime],
			["modAmp2", 0, defLagTime],
			["modFilter2", 0, defLagTime],
			["modDelay2", 0, defLagTime],
			["modDecay2", 0, defLagTime],
			["modFreq3", 0, defLagTime],
			["modAmp3", 0, defLagTime],
			["modFilter3", 0, defLagTime],
			["modDelay3", 0, defLagTime],
			["modDecay3", 0, defLagTime],
			["modFreq4", 0, defLagTime],
			["modAmp4", 0, defLagTime],
			["modFilter4", 0, defLagTime],
			["modDelay4", 0, defLagTime],
			["modDecay4", 0, defLagTime],
			["modFreq5", 0, defLagTime],
			["modAmp5", 0, defLagTime],
			["modFilter5", 0, defLagTime],
			["modDelay5", 0, defLagTime],
			["modDecay5", 0, defLagTime],
			["modFreq6", 0, defLagTime],
			["modAmp6", 0, defLagTime],
			["modFilter6", 0, defLagTime],
			["modDelay6", 0, defLagTime],
			["modDecay6", 0, defLagTime],
			["modFreq7", 0, defLagTime],
			["modAmp7", 0, defLagTime],
			["modFilter7", 0, defLagTime],
			["modDelay7", 0, defLagTime],
			["modDecay7", 0, defLagTime],
			["modFreq8", 0, defLagTime],
			["modAmp8", 0, defLagTime],
			["modFilter8", 0, defLagTime],
			["modDelay8", 0, defLagTime],
			["modDecay8", 0, defLagTime],
			["modOutGain", 0, defLagTime],

			// N.B. the args below aren't used in the synthdef, just stored here for convenience

			["midiLearn", 0],
			["midiLearnStep", 1],
		];
		arrOptions = [3, 0, 2]; // no strings, quantise: off, level control: limiter
		arrOptionData = [
			[
				["1 String", 1],
				["2 Strings", 2],
				["3 Strings", 3],
				["4 Strings", 4],
				["5 Strings", 5],
				["6 Strings", 6],
				["7 Strings", 7],
				["8 Strings", 8],
			],
			[ // quantise
				["Off", {arg in; in;}],
				["On - frequencies will be quantised to MIDI notes", {arg in; in.round;}],
			],
			TXLevelControl.arrOptionData,
		];
		synthDefFunc = { arg in, out, t_userTrig, extTrigger, inGain, freqMin, freqMax, quantise,
			pitchBend, pitchBendMin, pitchBendMax,
			maxDelay, maxDelayMin, maxDelayMax, randDelay,
			maxDecay, maxDecayMin, maxDecayMax,
			freq1, amp1, filter1, delay1, decay1, freq2, amp2, filter2, delay2, decay2,
			freq3, amp3, filter3, delay3, decay3, freq4, amp4, filter4, delay4, decay4,
			freq5, amp5, filter5, delay5, decay5, freq6, amp6, filter6, delay6, decay6,
			freq7, amp7, filter7, delay7, decay7, freq8, amp8, filter8, delay8, decay8,
			outGain,
			modInGain = 0, modPitchBend = 0, modMaxDelay = 0, modRandDelay = 0, modMaxDecay = 0,
			modFreq1 = 0, modAmp1 = 0, modFilter1 = 0, modDelay1 = 0, modDecay1 = 0,
			modFreq2 = 0, modAmp2 = 0, modFilter2 = 0, modDelay2 = 0, modDecay2 = 0,
			modFreq3 = 0, modAmp3 = 0, modFilter3 = 0, modDelay3 = 0, modDecay3 = 0,
			modFreq4 = 0, modAmp4 = 0, modFilter4 = 0, modDelay4 = 0, modDecay4 = 0,
			modFreq5 = 0, modAmp5 = 0, modFilter5 = 0, modDelay5 = 0, modDecay5 = 0,
			modFreq6 = 0, modAmp6 = 0, modFilter6 = 0, modDelay6 = 0, modDecay6 = 0,
			modFreq7 = 0, modAmp7 = 0, modFilter7 = 0, modDelay7 = 0, modDecay7 = 0,
			modFreq8 = 0, modAmp8 = 0, modFilter8 = 0, modDelay8 = 0, modDecay8 = 0,
			modOutGain = 0;
			var inGainSum, input, stringCount, holdSignal, quantiseFunc, levelControlFunc, delayFunc;
			var pitchBendSum, maxDelaySum, maxDecaySum, outGainSum, outSound;
			var holdTrig, holdDelayRand;
			var arrAllFreqs, arrAllModFreqs, arrAllAmps, arrAllFilters, arrAllDelays, arrAllDecays;
			var arrFreqs, arrFreqsQuant, arrAmps, arrFilters, arrDelays, arrDecays, arrStrings;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			holdTrig = t_userTrig + extTrigger;
			inGainSum = (inGain + modInGain).max(0).min(2);
			holdDelayRand = (randDelay + modMaxDelay).max(0).min(1) * 0.5; // scale to 0.5
			input = TXClean.ar(startEnv * InFeedback.ar(in,1));
			outGainSum = (outGain + modOutGain).max(0).min(1);
			arrAllAmps = [
				(amp1 + modAmp1),
				(amp2 + modAmp2),
				(amp3 + modAmp3),
				(amp4 + modAmp4),
				(amp5 + modAmp5),
				(amp6 + modAmp6),
				(amp7 + modAmp7),
				(amp8 + modAmp8),
			];
			arrAllFilters = [
				(filter1 + modFilter1),
				(filter2 + modFilter2),
				(filter3 + modFilter3),
				(filter4 + modFilter4),
				(filter5 + modFilter5),
				(filter6 + modFilter6),
				(filter7 + modFilter7),
				(filter8 + modFilter8),
			];
			arrAllDelays = [
				(delay1 + modDelay1),
				(delay2 + modDelay2),
				(delay3 + modDelay3),
				(delay4 + modDelay4),
				(delay5 + modDelay5),
				(delay6 + modDelay6),
				(delay7 + modDelay7),
				(delay8 + modDelay8),
			];
			arrAllDecays = [
				(decay1 + modDecay1),
				(decay2 + modDecay2),
				(decay3 + modDecay3),
				(decay4 + modDecay4),
				(decay5 + modDecay5),
				(decay6 + modDecay6),
				(decay7 + modDecay7),
				(decay8 + modDecay8),
			];
			stringCount = this.getSynthOption(0);
			quantiseFunc = this.getSynthOption(1);
			levelControlFunc = this.getSynthOption(2);
			pitchBendSum = (pitchBend + modPitchBend).max(0).min(1).linlin(0, 1, pitchBendMin, pitchBendMax);
			maxDelaySum = (maxDelay + modMaxDelay).max(0).min(1).linlin(0, 1, maxDelayMin, maxDelayMax);
			maxDecaySum = (maxDecay + modMaxDecay).max(0).min(1).linlin(0, 1, maxDecayMin, maxDecayMax);
			arrAllFreqs = [freq1 , freq2 , freq3 , freq4 , freq5 , freq6 , freq7 , freq8];
			arrAllModFreqs = [modFreq1 , modFreq2 , modFreq3 , modFreq4 , modFreq5 , modFreq6 , modFreq7 , modFreq8];
			arrFreqs = stringCount.collect({arg i;
				((arrAllFreqs[i] * 127) + (arrAllModFreqs[i] * (freqMax - freqMin))).max(freqMin).min(freqMax);
			});
			// arrFreqsQuant = quantiseFunc.value(arrFreqs.linlin(0, 1, freqMin, freqMax)) + pitchBendSum; // OLD
			arrFreqsQuant = quantiseFunc.value(arrFreqs) + pitchBendSum;

			arrAmps = arrAllAmps.copy.keep(stringCount).max(0).min(1);
			arrFilters = arrAllFilters.copy.keep(stringCount).max(0).min(1).linlin(0, 1, 0, 0.95);
			arrDelays = arrAllDelays.copy.keep(stringCount).max(0).min(1);
			arrDecays = arrAllDecays.copy.keep(stringCount).max(0).min(1).linlin(0, 1, -1, 1);
			arrStrings = stringCount.collect({arg i;
				arrAmps[i] *
				//Pluck.ar (in: 0, trig: 1, maxdelaytime: 0.2, delaytime: 0.2, decaytime: 1, coef: 0.5)
				Pluck.ar (input * inGainSum,
					TDelay.kr(holdTrig,
						TRand.kr(1 - holdDelayRand, 1 + holdDelayRand, holdTrig) * arrDelays[i] * maxDelaySum),
					0.2,
					arrFreqsQuant[i].midicps.reciprocal,
					arrDecays[i] * maxDecaySum,
					arrFilters[i]);
			});
			holdSignal = Mix.new(arrStrings);
			outSound = holdSignal * outGainSum;
			Out.ar(out, startEnv * levelControlFunc.value(outSound, input));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Trigger", {this.moduleNode.set("t_userTrig", 1);}],
			["EZslider", "In gain", ControlSpec(0, 2), "inGain"],
			["TXMinMaxSliderSplit", "Pitch bend", ControlSpec(-48, 48), "pitchBend",
				"pitchBendMin", "pitchBendMax"],
			["TXMinMaxSliderSplit", "Max delay", ControlSpec(0.01, 10), "maxDelay",
				"maxDelayMin", "maxDelayMax"],
			["EZslider", "Random delay", ControlSpec(0, 1), "randDelay"],
			["TXMinMaxSliderSplit", "Max decay", ControlSpec(0.01, 60), "maxDecay",
				"maxDecayMin", "maxDecayMax"],
			["EZslider", "Out gain", ControlSpec(0, 1), "outGain"],
			["SynthOptionPopupPlusMinus", "Level control", arrOptionData, 2],
			["SynthOptionPopup", "Complexity", arrOptionData, 0],
		]
		++ ["freq1", "freq2", "freq3", "freq4", "freq5", "freq6", "freq7", "freq8",
		].collect({arg item, i;
			var specFunc, getValFunc, setValFunc;
			specFunc = {ControlSpec(this.getSynthArgSpec("freqMin").midicps,
				this.getSynthArgSpec("freqMax").midicps, 'exp')};
			getValFunc = {
				var spec = ControlSpec(this.getSynthArgSpec("freqMin"), this.getSynthArgSpec("freqMax"));
				var holdVal = this.getSynthArgSpec(item);
				(spec.constrain(holdVal * 127)).midicps;
			};
			setValFunc = {arg argVal;
				var holdVal = argVal.cpsmidi / 127;
				this.setSynthValue(item, holdVal);
			};
			/*
			Note valueActionNumber is used here to get specific behaviour for freqs
			*/
			["valueActionNumber", "Set " ++ item, [specFunc], getValFunc, setValFunc]
		})
		++ ["filter1", "filter2", "filter3", "filter4", "filter5", "filter6", "filter7", "filter8",
		].collect({arg item, i;
			["EZslider", item, ControlSpec(0, 1), item]
		})
		++ ["delay1", "delay2", "delay3", "delay4", "delay5", "delay6", "delay7", "delay8",
		].collect({arg item, i;
			["EZsliderUnmapped", item, {ControlSpec(0, this.getSynthArgSpec("maxDelay").linlin(0, 1,
				this.getSynthArgSpec("maxDelayMin"), this.getSynthArgSpec("maxDelayMax")))}, item]
		})
		++ ["decay1", "decay2", "decay3", "decay4", "decay5", "decay6", "decay7", "decay8",
		].collect({arg item, i;
			["EZsliderUnmapped", item, {
				var maxDec = this.getSynthArgSpec("maxDecay").linlin(0, 1,
					this.getSynthArgSpec("maxDecayMin"), this.getSynthArgSpec("maxDecayMax"));
				ControlSpec(maxDec.neg, maxDec)}, item]
		})
		++ ["amp1", "amp2", "amp3", "amp4", "amp5", "amp6", "amp7", "amp8",
		].collect({arg item, i;
			["EZslider", item, ControlSpec(0, 1), item]
		})
		++ [
			["TXNoteRangeSlider", "Freq range", "freqMin", "freqMax", {}, true],
			["SynthOptionPopup", "Quantise", arrOptionData, 1],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	buildGuiSpecArray {
		var holdFreqMin = this.getSynthArgSpec("freqMin");
		var holdFreqMax = this.getSynthArgSpec("freqMax");
		var holdMaxDelay = this.getSynthArgSpec("maxDelay").linlin(0, 1,
			this.getSynthArgSpec("maxDelayMin"), this.getSynthArgSpec("maxDelayMax"));
		var holdMaxDecay = this.getSynthArgSpec("maxDecay").linlin(0, 1,
			this.getSynthArgSpec("maxDecayMin"), this.getSynthArgSpec("maxDecayMax"));

		guiSpecArray = [
			["ActionButton", "Gain", {displayOption = "showGain";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showGain")],
			["Spacer", 3],
			["ActionButton", "Parameters", {displayOption = "showParameters"; this.buildGuiSpecArray;
				system.showView;}, 100, TXColor.white, this.getButtonColour(displayOption == "showParameters")],
			["Spacer", 3],
			["ActionButton", "Strings", {displayOption = "showStrings";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showStrings")],
			["Spacer", 60],
			["ActionButton", "Trigger", {this.moduleNode.set("t_userTrig", 1);},
				100, TXColor.white, TXColor.sysGuiCol2],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 2],
		];

		if (displayOption == "showGain", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["EZslider", "In gain", ControlSpec(0, 2), "inGain"],
				["SpacerLine", 20],
				["EZslider", "Out gain", ControlSpec(0, 1), "outGain"],
				["SpacerLine", 20],
				["SynthOptionPopupPlusMinus", "Level control", arrOptionData, 2],
			];
		});
		if (displayOption == "showParameters", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["TXNoteRangeSlider", "Freq range", "freqMin", "freqMax", {}, true],
				["SpacerLine", 10],
				["SynthOptionPopupPlusMinus", "Quantise", arrOptionData, 1],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Pitch bend", ControlSpec(-48, 48), "pitchBend",
					"pitchBendMin", "pitchBendMax"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Max delay", ControlSpec(0.01, 10), "maxDelay",
					"maxDelayMin", "maxDelayMax"],
				["SpacerLine", 10],
				["EZslider", "Random delay", ControlSpec(0, 1), "randDelay"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Max decay", ControlSpec(0.01, 60), "maxDecay",
					"maxDecayMin", "maxDecayMax"],

			];
		});
		if (displayOption == "showStrings", {
			guiSpecArray = guiSpecArray ++[
				["NextLine"],
				["SynthOptionPopupPlusMinus", "Complexity", arrOptionData, 0, 210,
					{this.buildGuiSpecArray; system.showViewIfModDisplay(this)}],
				["Spacer", 20],
				["TXCheckBox", "MIDI learn notes & amps", "midiLearn", {arg view; this.setMidiLearnFunc(view.value);}, 180],
				["EZNumber", "Next String", ControlSpec(1, 8, step: 1), "midiLearnStep"],
				["ActionButton", "Set Next String: 1", {this.setSynthArgSpec("midiLearnStep", 1); system.showView;}, 100],
				["SpacerLine", 2],
				["TextBar", "Strings", 80],
				["TextBar", "1", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(1)],
				["TextBar", "2", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(2)],
				["TextBar", "3", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(3)],
				["TextBar", "4", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(4)],
				["TextBar", "5", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(5)],
				["TextBar", "6", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(6)],
				["TextBar", "7", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(7)],
				["TextBar", "8", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(8)],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["freq1", "freq2", "freq3", "freq4",
					"freq5", "freq6", "freq7", "freq8", ]);}, 36],
				["TXMultiKnobMidiNote", "Freq: midi note", ["freq1", "freq2", "freq3", "freq4",
					"freq5", "freq6", "freq7", "freq8", ], 8, ControlSpec(holdFreqMin / 127, holdFreqMax / 127)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["filter1", "filter2", "filter3", "filter4",
					"filter5", "filter6", "filter7", "filter8", ]);}, 36],
				["TXMultiKnobNo", "Filter", ["filter1", "filter2", "filter3", "filter4",
					"filter5", "filter6", "filter7", "filter8", ], 8, ControlSpec(0, 1)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["delay1", "delay2", "delay3", "delay4",
					"delay5", "delay6", "delay7", "delay8", ]);}, 36],
				["TXMultiKnobNoUnmap", "Delay", ["delay1", "delay2", "delay3", "delay4",
					"delay5", "delay6", "delay7", "delay8", ], 8, ControlSpec(0, holdMaxDelay)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["decay1", "decay2", "decay3", "decay4",
					"decay5", "decay6", "decay7", "decay8", ]);}, 36],
				["TXMultiKnobNoUnmap", "Decay -/+", ["decay1", "decay2", "decay3", "decay4",
					"decay5", "decay6", "decay7", "decay8", ], 8, ControlSpec(holdMaxDecay.neg, holdMaxDecay)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["amp1", "amp2", "amp3", "amp4",
					"amp5", "amp6", "amp7", "amp8", ]);}, 36],
				["TXMultiKnobNo", "Amp", ["amp1", "amp2", "amp3", "amp4",
					"amp5", "amp6", "amp7", "amp8", ], 8, ControlSpec(0, 1)],
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

	getLayerCol { arg layerNo;
		if (this.getSynthOption(0) < layerNo, {
			^TXColor.grey(0.8);
		},{
			^TXColor.white;
		});
	}

	copy1ToAll {arg arrayStrings;
		var holdVal = this.getSynthArgSpec(arrayStrings[0]);
		arrayStrings.do({arg item, i;
			if (i > 0, {
				this.setSynthValue(item, holdVal);
			});
		});
		system.showView;
	}

	setMidiLearnFunc { arg argSwitch = 0;
		var arrFreqStrings = ["freq1", "freq2", "freq3", "freq4",
					"freq5", "freq6", "freq7", "freq8", ];
		var arrAmpStrings = ["amp1", "amp2", "amp3", "amp4",
					"amp5", "amp6", "amp7", "amp8", ];
		// stop any old funcs
		this.stopMidiLearn;
		// if requested start midi
		if (argSwitch == 1, {
			midiNoteOnFunc = MIDIFunc.noteOn({ |vel, num, chan|
				var stepNo;
				if (vel > 0, {
					stepNo = this.getSynthArgSpec("midiLearnStep");
					// store freq and amp
					this.setSynthValue(arrFreqStrings[stepNo - 1], num / 127);
					this.setSynthValue(arrAmpStrings[stepNo - 1], vel / 127);
					// go to next step
					this.setSynthArgSpec("midiLearnStep", stepNo + 1);
					if ((stepNo + 1) > 8, {
						this.stopMidiLearn;
						this.setSynthArgSpec("midiLearn", 0);
						this.setSynthArgSpec("midiLearnStep", 1);
					});
					// refresh view
					system.showView;
				});
			});
		}, {
			// refresh view
			system.showViewIfModDisplay(this);
		});
	}

	stopMidiLearn {
		// remove any old funcs
		if (midiNoteOnFunc.notNil, {
			midiNoteOnFunc.free;
			midiNoteOnFunc = nil;
		});
	}

	deleteModuleExtraActions { // override base module
		this.stopMidiLearn;
	}

}

