// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXAmpCompSt  : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Amp Comp St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Freq", 1, "modFreq", 0]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName, arrPresets;
		//	set  class specific instance variables
		arrOptions = [5];
		arrOptionData = [
			// amp compensation
			TXAmpComp.arrOptionData,
		];
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["freq", 0.5, defLagTime],
			["freqMin", 0.midicps, defLagTime],
			["freqMax", 127.midicps, defLagTime],
			["modFreq", 0, defLagTime],
		];
		synthDefFunc = { arg in, out, freq, freqMin, freqMax, modFreq=0;
			var input, outFreqSum, ampCompFunction, outAmpComp;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in, 2);
			outFreqSum = (freq + modFreq).max(0).min(1).linexp(0, 1, freqMin, freqMax);
			ampCompFunction = this.getSynthOption(0);
			outAmpComp = ampCompFunction.value(outFreqSum);
			Out.ar(out, startEnv * input * outAmpComp);
		};
		guiSpecArray = [
			["SynthOptionListPlusMinus", "Amp Comp", arrOptionData, 0, nil, 150],
			["SpacerLine", 6],
			["TXMinMaxFreqNoteSldr", "Freq", ControlSpec(0.midicps, 20000, \exponential),
				"freq", "freqMin", "freqMax", nil, TXAmpComp.arrFreqRanges],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

	