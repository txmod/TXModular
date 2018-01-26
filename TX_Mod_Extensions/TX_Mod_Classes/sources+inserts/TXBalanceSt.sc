// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXBalanceSt : TXModuleBase {		// Balance module

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Balance St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrAudSCInBusSpecs = [
			["Side-chain", 1, "sideChain"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Target Level", 1, "modTarget", 0],
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
			["sideChain", 0, 0],
			["out", 0, 0],
			["target", 0.25, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modTarget", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0];
		arrOptionData = [
			[
				["Use Target level (ignore Side-chain audio input)",
					{arg sideChain, target, modTarget; ((target + modTarget).max(0).min(1)).squared;}],
				["Use Side-chain audio input (ignore Target level)",
					{arg sideChain, target, modTarget;  TXClean.ar(InFeedback.ar(sideChain, 1));}],
			],
		];
		synthDefFunc = {
			arg in, sideChain, out, target, wetDryMix, modTarget = 0, modWetDryMix = 0;
			var input, matchLevel, outSound, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(startEnv * InFeedback.ar(in, 2));
			matchLevel = this.getSynthOption(0).value(sideChain, target, modTarget);
			outSound = Balance.ar(input, matchLevel);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			Out.ar(out, (startEnv * outSound * mixCombined) + (input * (1-mixCombined)));
		};
		guiSpecArray = [
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Level follow", arrOptionData, 0, 450],
			["SpacerLine", 6],
			["EZslider", "Target level", \linear.asSpec, "target"],
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

