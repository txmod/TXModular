// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXDistortion3St : TXModuleBase {		// Distortion module

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Distortion St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["In Gain", 1, "modInGain", 0],
			["Depth", 1, "modDepth", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0],
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

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["inGain", ControlSpec(0, 10).unmap(1), defLagTime],
			["inGainMin", 0, defLagTime],
			["inGainMax", 10, defLagTime],
			["depth", 0.5, defLagTime],
			["outGain", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modInGain", 0, defLagTime],
			["modDepth", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0];
		arrOptionData = [TXDistort.arrOptionData];
		synthDefFunc = { arg in, out, inGain, inGainMin, inGainMax, depth, outGain, wetDryMix,
			modInGain=0, modDepth=0.0, modWetDryMix=0.0;
			var input, inGainSum, outFunction, outDistorted, outClean, depthCombined, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in,2);
			inGainSum = (inGain + modInGain).max(0).min(1).linlin(0, 1, inGainMin, inGainMax);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			depthCombined = (depth + modDepth).max(0).min(1);
			outFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			outDistorted = outGain * outFunction.value(inGainSum * input, depthCombined);
			Out.ar(out, (startEnv * outDistorted * mixCombined) + (input * (1-mixCombined)) );
		};
		guiSpecArray = [
			["SynthOptionListPlusMinus", "Distortion", arrOptionData, 0, 300, 170],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "In Gain", ControlSpec(0, 10), "inGain", "inGainMin", "inGainMax"],
			["SpacerLine", 4],
			["EZslider", "Depth", ControlSpec(0, 1), "depth"],
			["SpacerLine", 4],
			["EZslider", "Out Gain", ControlSpec(0, 1), "outGain"],
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

