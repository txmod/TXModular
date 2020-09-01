// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXNormalizer : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Normalizer";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["input gain", 1, "modInLevel", 0],
			["level", 1, "modOutLevel", 0],
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
			["inLevel", 1, defLagTime],
			["outLevel", 0.5, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modInLevel", 0, defLagTime],
			["modOutLevel", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [2];
		arrOptionData = [ [
			["2 ms", 0.002],
			["6 ms", 0.006],
			["10 ms - default", 0.01],
			["20 ms", 0.02],
		] ];
		synthDefFunc = { arg in, out, inLevel, outLevel, wetDryMix, modInLevel = 0, modOutLevel = 0, modWetDryMix = 0.0;
			var input, inDelayed, outLevelSum, lookahead, lookahead2, outNormalizer, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in,1);
			outLevelSum = (outLevel + modOutLevel).max(0).min(1);
			lookahead = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			lookahead2 = 2 * lookahead;
			outNormalizer = Normalizer.ar(inLevel * input, outLevelSum, lookahead);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			// delay input by lookahead2 time
			inDelayed = DelayN.ar(input, lookahead2, lookahead2);
			Out.ar(out, startEnv * ((outNormalizer * mixCombined) + (inDelayed * (1-mixCombined))));
		};
		guiSpecArray = [
			["SynthOptionPopupPlusMinus", "Lookahead", arrOptionData, 0, 260],
			["SpacerLine", 4],
			["EZslider", "Input Gain", ControlSpec(0, 3), "inLevel"],
			["SpacerLine", 4],
			["EZslider", "Level", ControlSpec(0, 1), "outLevel"],
			["SpacerLine", 4],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

