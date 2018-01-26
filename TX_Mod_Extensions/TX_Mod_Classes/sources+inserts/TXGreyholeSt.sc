// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXGreyholeSt : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Greyhole St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Reverb Time", 1, "modDelayTime", 0],
			["Size", 1, "modSize", 0],
			["Damping", 1, "modDamping", 0],
			["Shape", 1, "modShape", 0],
			["Feedback", 1, "modFeedback", 0],
			["Modulation Depth", 1, "modModulationDepth", 0],
			["Modulation Freq", 1, "modModulationFreq", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
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
		extraLatency = 0.2;	// allow extra time when recreating
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["delayTime", 0.47979, defLagTime],
			["delayTimeMin", 0.1, defLagTime],
			["delayTimeMax", 10.0, defLagTime],
			["size", 0.2, defLagTime],
			["sizeMin", 0.5, defLagTime],
			["sizeMax", 5.0, defLagTime],
			["damping", 0, defLagTime],
			["shape",0.707, defLagTime],
			["feedback", 0.9, defLagTime],
			["modulationDepth",0.1, defLagTime],
			["modulationFreq", 2.0, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modDelayTime", 0, defLagTime],
			["modSize", 0, defLagTime],
			["modDamping", 0, defLagTime],
			["modShape", 0, defLagTime],
			["modFeedback", 0, defLagTime],
			["modModulationDepth", 0, defLagTime],
			["modModulationFreq", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		synthDefFunc = {
			arg in, out, delayTime, delayTimeMin, delayTimeMax, size, sizeMin, sizeMax, damping, shape, feedback,
			modulationDepth, modulationFreq, wetDryMix,
			modDelayTime=0, modSize, modDamping=0, modShape=0, modModulationDepth=0, modModulationFreq=0,
			modFeedback=0, modWetDryMix=0;

			var input, outSound, roomSize, sumDelayTime, sumSize, sumDamping, mixCombined;
			var sumShape, sumModulationDepth, sumModulationFreq, sumFeedback;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = startEnv * TXClean.ar(InFeedback.ar(in, 2));
			sumDelayTime = delayTimeMin + ( (delayTimeMax-delayTimeMin) * (delayTime + modDelayTime).max(0).min(1) );
			sumSize = sizeMin + ((sizeMax - sizeMin) * (size + modSize).max(0).min(1));
			sumDamping = (damping + modDamping).max(0).min(1);
			sumShape = (shape + modShape).max(0).min(1);
			sumModulationDepth = (modulationDepth + modModulationDepth).max(0).min(1);
			sumModulationFreq = (modulationFreq + (modModulationFreq * 10)).max(0).min(10);
			sumFeedback = (feedback + modFeedback).max(0).min(1);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outSound = Greyhole.ar(input, sumDelayTime, sumDamping, sumSize, sumShape, sumFeedback,
				sumModulationDepth, sumModulationFreq,
			);
			outSound = (outSound  * mixCombined) + (input * (1-mixCombined));
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		guiSpecArray = [
			["DividingLine"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Reverb time", ControlSpec.new(0.1, 60), "delayTime",
				"delayTimeMin", "delayTimeMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Size", ControlSpec.new(0.5, 5), "size", "sizeMin", "sizeMax"],
			["DividingLine"],
			["SpacerLine", 6],
			["EZslider", "Damping", ControlSpec(0, 1), "damping"],
			["SpacerLine", 2],
			["EZslider", "Shape", ControlSpec(0, 1), "shape"],
			["SpacerLine", 2],
			["EZslider", "Feedback", ControlSpec(0, 1), "feedback"],
			["DividingLine"],
			["SpacerLine", 6],
			["EZslider", "Mod depth", ControlSpec(0, 1), "modulationDepth"],
			["SpacerLine", 2],
			["EZslider", "Mod freq", ControlSpec(0, 10), "modulationFreq"],
			["DividingLine"],
			["SpacerLine", 6],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

