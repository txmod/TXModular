// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFreqShift : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Freq Shift";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrAudSCInBusSpecs = [
			["FM Input", 1, "sideChain"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Freq Shift", 1, "modFreqShift", 0],
			["FM Depth", 1, "modFMDepth", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.freqShiftSpec = ControlSpec(-5000, 5000);
		classData.fmDepthSpec = ControlSpec(0, 5);
		classData.shiftRangePresets = [
			["Range presets: ", [0, 100]],
			["0 to 5000 Hz", [0, 5000]],
			["0 to 1000 Hz", [0, 1000]],
			["0 to 500 Hz", [0, 500]],
			["0 to 100 Hz", [0, 100]],
			["0 to 50 Hz", [0, 50]],
			["0 to 10 Hz", [0, 10]],
			["0 to 5 Hz", [0, 5]],
			["0 to 2 Hz", [0, 2]],
			["-5000 to 5000 Hz", [-5000, 5000]],
			["-1000 to 1000 Hz", [-1000, 1000]],
			["-500 to 500 Hz", [-500, 500]],
			["-100 to 100 Hz", [-100, 100]],
			["-50 to 50 Hz", [-50, 50]],
			["-10 to 10 Hz", [-10, 10]],
			["-5 to 5 Hz", [-5, 5]],
			["-2 to 2 Hz", [-2, 2]],
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		arrSynthArgSpecs = [
			["in", 0, 0],
			["sideChain", 0, 0],
			["out", 0, 0],
			["freqShift", 0.5, defLagTime],
			["freqShiftMin", -1000, defLagTime],
			["freqShiftMax", 1000, defLagTime],
			["fmDepth", 0.5, defLagTime],
			["fmDepthMin", 0, defLagTime],
			["fmDepthMax", 2, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modFreqShift", 0, defLagTime],
			["modFMDepth", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0];
		arrOptionData = [
			[
				["Freq modulation off, ignore side-chain audio input",
					{arg sideChain; 0;}],
				["Freq modulation on, use side-chain audio input",
					{arg sideChain;  TXClean.ar(InFeedback.ar(sideChain, 1));}],
			],
		];
		synthDefFunc = {
			arg in, sideChain, out, freqShift, freqShiftMin, freqShiftMax,
			fmDepth, fmDepthMin, fmDepthMax, wetDryMix,
			modFreqShift = 0, modFMDepth = 0, modWetDryMix = 0;

			var input, sideChainSig, sFreqShift, sfmDepth, sPhase, outSound, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(startEnv * InFeedback.ar(in, 1));
			sideChainSig = this.getSynthOption(0).value(sideChain);
			sfmDepth = (fmDepth + modFMDepth).max(0).min(1).linlin(0, 1,
				fmDepthMin, fmDepthMax);
			sPhase = (sfmDepth * sideChainSig).fold(0, 1) * 2pi;
			sFreqShift = (freqShift + modFreqShift).max(0).min(1).linlin(0, 1, freqShiftMin, freqShiftMax);
			outSound = FreqShift.ar(input, sFreqShift, sPhase);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			Out.ar(out, (startEnv * outSound * mixCombined) + (input * (1-mixCombined)));
		};
		guiSpecArray = [
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Freq shift", classData.freqShiftSpec, "freqShift", "freqShiftMin", "freqShiftMax",
				nil, classData.shiftRangePresets],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 10],
			["SynthOptionPopupPlusMinus", "FM type", arrOptionData, 0, 450],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "FM depth", classData.fmDepthSpec,
				"fmDepth", "fmDepthMin", "fmDepthMax"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 10],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

