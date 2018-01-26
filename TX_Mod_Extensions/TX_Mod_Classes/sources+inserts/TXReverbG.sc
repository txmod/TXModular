// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXReverbG : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "ReverbG";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Reverb Time", 1, "modReverbTime", 0],
			["Input Damping", 1, "modInDamping", 0],
			["Reverb Damping", 1, "modDamping", 0],
			["Input Level", 1, "modInLevel", 0],
			["Early Level", 1, "modEarlyLevel", 0],
			["Tail Level", 1, "modTailLevel", 0],
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
		extraLatency = 0.2;	// allow extra time when recreating
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["reverbTime", 0.47979, defLagTime],
			["reverbTimeMin", 0.1, defLagTime],
			["reverbTimeMax", 10.0, defLagTime],
			["inDamping", 0.19, defLagTime],
			["damping", 0.59, defLagTime],
			["inLevel", -3.dbamp, defLagTime],
			["earlyLevel", -9.dbamp, defLagTime],
			["tailLevel", -11.dbamp, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modReverbTime", 0, defLagTime],
			["modInDamping", 0, defLagTime],
			["modDamping", 0, defLagTime],
			["modInLevel", 0, defLagTime],
			["modEarlyLevel", 0, defLagTime],
			["modTailLevel", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [4];
		arrOptionData = [
			[
				["2 square metres", [2]],
				["5 square metres", [5]],
				["16 square metres", [16]],
				["36 square metres", [36]],
				["80 square metres", [80]],
				["120 square metres", [120]],
				["240 square metres", [240]],
				["300 square metres", [300]],
			];
		];
		synthDefFunc = {
			arg in, out,
			reverbTime, reverbTimeMin, reverbTimeMax, inDamping, damping, inLevel, earlyLevel, tailLevel, wetDryMix,
			modReverbTime=0, modInDamping, modDamping=0,
			modInLevel=0, modEarlyLevel=0, modTailLevel=0, modWetDryMix=0;

			var input, outSound, roomSize, revTime, sumInDamping, sumDamping, mixCombined,
			sumInLevel, sumEarlyLevel, sumTailLevel;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = startEnv * TXClean.ar(InFeedback.ar(in,1));
			roomSize = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			revTime = reverbTimeMin + ( (reverbTimeMax-reverbTimeMin) * (reverbTime + modReverbTime).max(0).min(1) );
			sumInDamping = (inDamping + modInDamping).max(0).min(1);
			sumDamping = 1 - (damping + modDamping).max(0).min(1);
			sumInLevel = 1 - (inLevel + modInLevel).max(0).min(0.999);
			sumEarlyLevel = 1 - (earlyLevel + modEarlyLevel).max(0).min(0.999);
			sumTailLevel = 1 - (tailLevel + modTailLevel).max(0).min(0.999);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outSound = 0.02 * GVerb.ar(input, roomSize, revTime, sumDamping, sumInDamping, 15,
				sumInLevel.ampdb, sumEarlyLevel.ampdb, sumTailLevel.ampdb);
			outSound = (outSound  * mixCombined) + (input * (1-mixCombined));
			Out.ar(out, startEnv * TXClean.ar(outSound));
		};
		guiSpecArray = [
			["DividingLine"],
			["SpacerLine", 2],
			["TXPresetPopup", "Presets",
				TXReverbGPresets.arrPresets(this).collect({arg item, i; item.at(0)}),
				TXReverbGPresets.arrPresets(this).collect({arg item, i; item.at(1)})
			],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["SynthOptionPopupPlusMinus", "Room size", arrOptionData, 0],
			["TXMinMaxSliderSplit", "Reverb time", ControlSpec.new(0, 100), "reverbTime", "reverbTimeMin", "reverbTimeMax"],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["EZslider", "Input damp", ControlSpec(0, 1), "damping"],
			["EZslider", "Reverb damp", ControlSpec(0, 1), "inDamping"],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["EZslider", "Input level", ControlSpec(0, 1), "inLevel"],
			["EZslider", "Early refl's", ControlSpec(0, 1), "earlyLevel"],
			["EZslider", "Reverb tail", ControlSpec(0, 1), "tailLevel"],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

