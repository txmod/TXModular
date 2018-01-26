// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXReverbF : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "ReverbF";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Room size", 1, "modReverbTime", 0],
			["Damping", 1, "modDamping", 0],
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
			["reverbTime", 0.25, defLagTime],
			["reverbTimeMin", 0.1, defLagTime],
			["reverbTimeMax", 10.0, defLagTime],
			["damping", 0.9, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modReverbTime", 0, defLagTime],
			["modDamping", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		synthDefFunc = {
			arg in, out, reverbTime=0.25, reverbTimeMin=0.1, reverbTimeMax=10, damping, wetDryMix,
			modReverbTime=0, modDamping=0, modWetDryMix=0;

			var input, outSound, revTime, sumdamping, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = startEnv * TXClean.ar(InFeedback.ar(in,1));
			revTime = reverbTimeMin + ( (reverbTimeMax-reverbTimeMin) * (reverbTime + modReverbTime).max(0).min(1) );
			sumdamping = 1 - (damping + modDamping).max(0).min(1);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);

			outSound = FreeVerb.ar(input, mixCombined, revTime, sumdamping);

			outSound = (outSound  * mixCombined) + (input * (1-mixCombined));
			Out.ar(out, startEnv * TXClean.ar(outSound));
		};
		guiSpecArray = [
			["TXMinMaxSliderSplit", "Room size", ControlSpec.new(0, 1.0), "reverbTime", "reverbTimeMin", "reverbTimeMax"],
			["SpacerLine", 2],
			["EZsliderUnmapped", "Damping", ControlSpec(0, 1.0), "damping"],
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

