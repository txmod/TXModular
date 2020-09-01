// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXDisintegratorSt : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Disintegrator St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Probablility", 1, "modProb", 0],
			["Multiplier", 1, "modMult", 0],
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
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["prob", 0.5, defLagTime],
			["probMin", 0, defLagTime],
			["probMax", 1.0, defLagTime],
			["mult", 0, defLagTime],
			["multMin", -1.0, defLagTime],
			["multMax", 1.0, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modProb", 0, defLagTime],
			["modMult", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];

		synthDefFunc = {
			arg in, out, prob, probMin, probMax, mult, multMin, multMax,
			wetDryMix, modProb=0, modMult=0, modWetDryMix=0;
			var input, outSound, probVal, multVal, mixCombined;
			var limit = 0.9;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = startEnv * InFeedback.ar(in,2);

			probVal = probMin + ( (probMax-probMin) * (prob + modProb).max(0).min(1) );
			multVal = multMin + ( (multMax-multMin) * (mult + modMult).max(-1).min(1) );

			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);

			outSound = Disintegrator.ar(input, probVal, multVal);

			outSound = LeakDC.ar(outSound, 0.995);

			Out.ar(out, (startEnv * outSound * mixCombined) + (input * (1-mixCombined)));
		};
		guiSpecArray = [
			["TXMinMaxSliderSplit", "Probability", ControlSpec.new(0.0, 1.0, \lin ),
				"prob", "probMin", "probMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Multiply", ControlSpec.new(-1.0, 1.0, \lin ),
				"mult", "multMin", "multMax"],
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

