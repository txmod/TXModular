// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXDistortion2 : TXModuleBase {		// Distortion module

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Distortion";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Depth", 1, "modDepth", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
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
			["depth", 0.5, defLagTime],
			["outGain", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modDepth", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0];
		arrOptionData = [TXDistort.arrOptionData];
		synthDefFunc = { arg in, out, inGain, depth, outGain, wetDryMix, modDepth=0.0, modWetDryMix=0.0;
			var input, outFunction, outDistorted, outClean, depthCombined, mixCombined;
			input = InFeedback.ar(in,1);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			depthCombined = (depth + modDepth).max(0).min(1);
			outFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			outDistorted = outGain * outFunction.value(inGain * input, depthCombined);
			Out.ar(out, (outDistorted * mixCombined) + (input * (1-mixCombined)) );
		};
		guiSpecArray = [
			["SynthOptionPopup", "Distortion", arrOptionData, 0],
			["SpacerLine", 4],
			["EZslider", "In Gain", ControlSpec(0, 10), "inGain"],
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

