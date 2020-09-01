// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXDCRemoveSt : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "DC Remove St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
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

	init {arg argInstName, arrPresets;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["wetDryMix", 1.0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		synthDefFunc = { arg in, out, wetDryMix, modWetDryMix = 0;
			var input, outSound, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			input = InFeedback.ar(in,2);
			outSound = LeakDC.ar(input, 0.995);
			Out.ar(out, (startEnv * outSound * mixCombined) + (input * (1-mixCombined)));
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

