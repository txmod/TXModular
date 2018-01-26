// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXLimiter : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Limiter";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["input gain", 1, "modInGain", 0],
			["threshold", 1, "modThreshold", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0],
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
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["inGain", 1, defLagTime],
			["threshold", 0.5, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modInGain", 0, defLagTime],
			["modThreshold", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [2];
		arrOptionData = [ [
			["2 ms", 0.002],
			["6 ms", 0.006],
			["10 ms - default", 0.01],
			["20 ms", 0.02],
		] ];
		synthDefFunc = { arg in, out, inGain, threshold, wetDryMix, modInGain = 0.0, modThreshold = 0.0, modWetDryMix = 0.0;
			var input, inDelayed, thresholdSum, lookahead, lookahead2, outLimiter, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = startEnv * InFeedback.ar(in,1);
			thresholdSum = (threshold + modThreshold).max(0).min(1);
			lookahead = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			lookahead2 = 2 * lookahead;
			outLimiter = Limiter.ar(inGain * input, thresholdSum, lookahead);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			// delay input by lookahead2 time
			inDelayed = DelayN.ar(input, lookahead2, lookahead2);
			Out.ar(out, (startEnv * outLimiter * mixCombined) + (inDelayed * (1-mixCombined)) );
		};
		guiSpecArray = [
			["SynthOptionPopupPlusMinus", "Lookahead", arrOptionData, 0, 260],
			["SpacerLine", 10],
			["EZslider", "In Gain", ControlSpec(0, 3), "inGain"],
			["SpacerLine", 10],
			["EZslider", "Threshold", ControlSpec(0, 1), "threshold"],
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

