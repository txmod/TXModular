// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXJPverbSt : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "JPverb St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Reverb Time", 1, "modReverbTime", 0],
			["Size", 1, "modSize", 0],
			["Reverb Damping", 1, "modDamping", 0],
			["Early Shape", 1, "modEarlyShape", 0],
			["Modulation Depth", 1, "modModulationDepth", 0],
			["Modulation Freq", 1, "modModulationFreq", 0],
			["Lows Level", 1, "modLowsLevel", 0],
			["Mids Level", 1, "modMidsLevel", 0],
			["Highs Level", 1, "modHighsLevel", 0],
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
			["reverbTime", 0.47979, defLagTime],
			["reverbTimeMin", 0.1, defLagTime],
			["reverbTimeMax", 10.0, defLagTime],
			["size", 0.2, defLagTime],
			["sizeMin", 0.5, defLagTime],
			["sizeMax", 5.0, defLagTime],
			["damping", 0, defLagTime],
			["earlyShape",0.707, defLagTime],
			["modulationDepth",0.1, defLagTime],
			["modulationFreq", 2.0, defLagTime],
			["lowsLevel", 1.0, defLagTime],
			["midsLevel", 1.0, defLagTime],
			["highsLevel", 1.0, defLagTime],
			["lowMidFreq", 500, defLagTime],
			["midHighFreq", 2000, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modReverbTime", 0, defLagTime],
			["modSize", 0, defLagTime],
			["modDamping", 0, defLagTime],
			["modEarlyShape", 0, defLagTime],
			["modModulationDepth", 0, defLagTime],
			["modModulationFreq", 0, defLagTime],
			["modLowsLevel", 0, defLagTime],
			["modMidsLevel", 0, defLagTime],
			["modHighsLevel", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		synthDefFunc = {
			arg in, out, reverbTime, reverbTimeMin, reverbTimeMax, size, sizeMin, sizeMax, damping, earlyShape,
			modulationDepth, modulationFreq, lowsLevel, midsLevel, highsLevel, lowMidFreq, midHighFreq, wetDryMix,
			modReverbTime=0, modSize=0, modDamping=0, modEarlyShape=0, modModulationDepth=0, modModulationFreq=0,
			modLowsLevel=0, modMidsLevel=0, modHighsLevel=0, modWetDryMix=0;

			var input, outSound, roomSize, revTime, sumSize, sumDamping, mixCombined;
			var sumEarlyShape, sumModulationDepth, sumModulationFreq, sumLowsLevel, sumMidsLevel, sumHighsLevel;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(InFeedback.ar(in, 2));
			revTime = reverbTimeMin + ( (reverbTimeMax-reverbTimeMin) * (reverbTime + modReverbTime).max(0).min(1) );
			sumSize = sizeMin + ((sizeMax - sizeMin) * (size + modSize).max(0).min(1));
			sumDamping = (damping + modDamping).max(0).min(1);
			sumEarlyShape = (earlyShape + modEarlyShape).max(0).min(1);
			sumModulationDepth = (modulationDepth + modModulationDepth).max(0).min(1);
			sumModulationFreq = (modulationFreq + (modModulationFreq * 10)).max(0).min(10);
			sumLowsLevel = (lowsLevel + modLowsLevel).max(0).min(1);
			sumMidsLevel = (midsLevel + modMidsLevel).max(0).min(1);
			sumHighsLevel = (highsLevel + modHighsLevel).max(0).min(1);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outSound = JPverb.ar(input, revTime, sumDamping, sumSize, sumEarlyShape,
				sumModulationDepth, sumModulationFreq, sumLowsLevel, sumMidsLevel, sumHighsLevel,
				lowMidFreq, midHighFreq
			);
			outSound = (outSound  * mixCombined) + (input * (1-mixCombined));
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		guiSpecArray = [
			["DividingLine"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Reverb time", ControlSpec.new(0.1, 60), "reverbTime",
				"reverbTimeMin", "reverbTimeMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Size", ControlSpec.new(0.5, 5), "size", "sizeMin", "sizeMax"],
			["DividingLine"],
			["SpacerLine", 6],
			["EZslider", "Damping", ControlSpec(0, 1), "damping"],
			["SpacerLine", 2],
			["EZslider", "Early shape", ControlSpec(0, 1), "earlyShape"],
			["DividingLine"],
			["SpacerLine", 6],
			["EZslider", "Mod depth", ControlSpec(0, 1), "modulationDepth"],
			["SpacerLine", 2],
			["EZslider", "Mod freq", ControlSpec(0, 10), "modulationFreq"],
			["DividingLine"],
			["SpacerLine", 2],
			["EZslider", "Lows Level", ControlSpec(0, 1), "lowsLevel"],
			["SpacerLine", 2],
			["EZslider", "Mids Level", ControlSpec(0, 1), "midsLevel"],
			["SpacerLine", 2],
			["EZslider", "Highs Level", ControlSpec(0, 1), "highsLevel"],
			["DividingLine"],
			["SpacerLine", 6],
			["EZslider", "Low/Mid Freq", ControlSpec(100, 6000) , "lowMidFreq"],
			["SpacerLine", 2],
			["EZslider", "Mid/High Freq", ControlSpec(1000, 6000), "midHighFreq"],
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

