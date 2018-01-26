// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXNoiseWhitePink : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Noise White-Pink";
		classData.moduleRate = "audio";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["White - Pink", 1, "modMix", 0]
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
			["out", 0, 0],
			["mix", 0, defLagTime],
			["modMix", 0, defLagTime],
		];
		synthDefFunc = { arg out, mix=0, modMix = 0;
			var outSound;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			outSound = WhiteNoise.ar(1-(mix + modMix).max(0).min(1),
				PinkNoise.ar( (mix + modMix).max(0).min(1) ) );
			Out.ar(out, startEnv * outSound);
		};
		guiSpecArray = [
			["EZslider", "White-Pink", \unipolar, "mix"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

