// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
// Thanks to Josh Parmenter on the SuperCollider list for the AmpSim code

TXAmpSimSt : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Amp Sim St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Tube factor", 1, "modTube", 0],
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
		var controlSpec;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["tube", 15, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modTube", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		controlSpec = ControlSpec.new(1, 50);

		synthDefFunc = {
			arg in, out, tube, wetDryMix, modTube=0, modWetDryMix=0;
			var input, outSound, tubeCombined, mixCombined;
			var limit = 0.9;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in,2);

			tubeCombined = controlSpec.map (
				(controlSpec.unmap(tube) + modTube).max(0).min(1)
			);

			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);

			outSound = Limiter.ar((((input*tubeCombined).exp - (input* tubeCombined * -1.2).exp)/
				((input*tubeCombined).exp + (input * tubeCombined * -1.0).exp)) /tubeCombined,
			limit, 0.01);

			Out.ar(out, (startEnv * outSound * mixCombined) + (input * (1-mixCombined)));
		};
		guiSpecArray = [
			["EZslider", "Tube factor", controlSpec, "tube"],
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

