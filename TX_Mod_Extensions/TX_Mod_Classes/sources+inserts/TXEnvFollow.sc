// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXEnvFollow : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Env Follow";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrAudSCInBusSpecs = [
			["Side-chain", 1, "insidechain"],
		];
		classData.arrCtlSCInBusSpecs = [
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
			["insidechain", 0, 0],
			["out", 0, 0],
			["wetDryMix", 1.0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		synthDefFunc = { arg in, insidechain, out, wetDryMix, modWetDryMix=0;
			var input, sidechain, outSound, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			input = TXClean.ar(InFeedback.ar(in,1));
			sidechain = TXClean.ar(InFeedback.ar(insidechain,1));
			outSound = (Balance.ar(input, sidechain) * mixCombined)
			+ (input * (1-mixCombined));
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		guiSpecArray = [
			["WetDryMixSlider"]
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

