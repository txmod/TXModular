// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXBracketEQ : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Bracket EQ";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Low-cut Freq", 1, "modLoFreq", 0],
			["High-cut Freq", 1, "modHiFreq", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.freqSpec = ControlSpec(20, 20000, \exponential);
		classData.arrFreqRangePresets = [
			["Min-Max Presets: ", [20, 20000]],
			["Maximum Range 20 - 20k hz", [20, 20000]],
			["Sub & Bass 20 - 250 hz", [20, 250]],
			["Sub-Bass 20 - 60 hz", [20, 60]],
			["Bass 60 - 250 hz", [60, 250]],
			["Mid Range 250 - 2k hz", [250, 2000]],
			["High Mids 2k - 6k hz", [2000, 6000]],
			["High Range 6k - 20k hz", [6000, 20000]],
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName, arrPresets;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["loFreq", 0, defLagTime],
			["loFreqMin", 20, defLagTime],
			["loFreqMax", 20000, defLagTime],
			["hiFreq", 1, defLagTime],
			["hiFreqMin", 20, defLagTime],
			["hiFreqMax", 20000, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modLoFreq", 0, defLagTime],
			["modHiFreq", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [1];
		arrOptionData = [
			[ // bracket eq
				["Off  (no EQ)", { arg input, loFreq, loFreqMin, loFreqMax, modLoFreq, hiFreq, hiFreqMin, hiFreqMax, modHiFreq; input;}],
				["12db Low-Cut & Hi-Cut", { arg input, loFreq, loFreqMin, loFreqMax, modLoFreq, hiFreq, hiFreqMin, hiFreqMax, modHiFreq;
					var sumLoFreq = (loFreq + modLoFreq).max(0).min(1).linexp(0, 1, loFreqMin, loFreqMax);
					var sumHiFreq = (hiFreq + modHiFreq).max(0).min(1).linexp(0, 1, hiFreqMin, hiFreqMax);
					var chain = HPF.ar(input, sumLoFreq);
					chain = LPF.ar(chain, sumHiFreq);
				}],
				["24db Low-Cut & Hi-Cut", { arg input, loFreq, loFreqMin, loFreqMax, modLoFreq, hiFreq, hiFreqMin, hiFreqMax, modHiFreq;
					var sumLoFreq = (loFreq + modLoFreq).max(0).min(1).linexp(0, 1, loFreqMin, loFreqMax);
					var sumHiFreq = (hiFreq + modHiFreq).max(0).min(1).linexp(0, 1, hiFreqMin, hiFreqMax);
					var chain = HPF.ar(HPF.ar(input, sumLoFreq), sumLoFreq);
					chain = LPF.ar(LPF.ar(chain, sumHiFreq), sumHiFreq);
				}],
				["12db Low-Cut Only", { arg input, loFreq, loFreqMin, loFreqMax, modLoFreq, hiFreq, hiFreqMin, hiFreqMax, modHiFreq;
					var sumLoFreq = (loFreq + modLoFreq).max(0).min(1).linexp(0, 1, loFreqMin, loFreqMax);
					HPF.ar(input, sumLoFreq);
				}],
				["24db Low-Cut Only", { arg input, loFreq, loFreqMin, loFreqMax, modLoFreq, hiFreq, hiFreqMin, hiFreqMax, modHiFreq;
					var sumLoFreq = (loFreq + modLoFreq).max(0).min(1).linexp(0, 1, loFreqMin, loFreqMax);
					HPF.ar(HPF.ar(input, sumLoFreq), sumLoFreq);
				}],
				["12db High-Cut Only", { arg input, loFreq, loFreqMin, loFreqMax, modLoFreq, hiFreq, hiFreqMin, hiFreqMax, modHiFreq;
					var sumHiFreq = (hiFreq + modHiFreq).max(0).min(1).linexp(0, 1, hiFreqMin, hiFreqMax);
					LPF.ar(input, sumHiFreq);
				}],
				["24db High-Cut Only", { arg input, loFreq, loFreqMin, loFreqMax, modLoFreq, hiFreq, hiFreqMin, hiFreqMax, modHiFreq;
					var sumHiFreq = (hiFreq + modHiFreq).max(0).min(1).linexp(0, 1, hiFreqMin, hiFreqMax);
					LPF.ar(LPF.ar(input, sumHiFreq), sumHiFreq);
				}],
			],
		];
		synthDefFunc = { arg in, out, loFreq, loFreqMin, loFreqMax, hiFreq, hiFreqMin, hiFreqMax, wetDryMix,
			modLoFreq=0, modHiFreq=0, modWetDryMix=0;
			var input, outSound, mixCombined, eqFunc;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in,1);
			eqFunc = this.getSynthOption(0);
			outSound = eqFunc.value(input, loFreq, loFreqMin, loFreqMax, modLoFreq, hiFreq, hiFreqMin, hiFreqMax, modHiFreq);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			Out.ar(out, (startEnv * outSound * mixCombined) + (input * (1-mixCombined)));
		};
		guiSpecArray = [
			["SynthOptionListPlusMinus", "Bracket EQ", arrOptionData, 0, nil, 100],
			["SpacerLine", 10],
			["TXMinMaxFreqNoteSldr", "Low-cut Freq", classData.freqSpec,
				"loFreq", "loFreqMin", "loFreqMax", nil, classData.arrFreqRangePresets],
			["SpacerLine", 10],
			["TXMinMaxFreqNoteSldr", "High-cut Freq", classData.freqSpec,
				"hiFreq", "hiFreqMin", "hiFreqMax", nil, classData.arrFreqRangePresets],
			["SpacerLine", 10],
			["WetDryMixSlider"]
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

