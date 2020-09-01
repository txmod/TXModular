// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXTransientShapeSt : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Transient Shape St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Slope Freq", 1, "modSlopeFreq", 0],
			["Attack", 1, "modAttack", 0],
			["Sustain", 1, "modSustain", 0],
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
			["slopeFreq", ControlSpec(60, 5000, \exp).unmap(500), defLagTime],
			["slopeFreqMin", 60, defLagTime],
			["slopeFreqMax", 5000, defLagTime],
			["attack", 0.5, defLagTime],
			["sustain", 0.5, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modSlopeFreq", 0, defLagTime],
			["modAttack", 0, defLagTime],
			["modSustain", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		synthDefFunc = { arg in, out, slopeFreq, slopeFreqMin, slopeFreqMax,
			attack, sustain, wetDryMix,
			modSlopeFreq=0, modAttack=0, modSustain=0, modWetDryMix=0;

			var input, slopeFreqSum, outSlew, outAttack, outSustain;
			var	attackCombined, sustainCombined, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in,2);
			slopeFreqSum = ( (slopeFreqMax/slopeFreqMin)
				** ((slopeFreq + modSlopeFreq).max(0.001).min(1)) ) * slopeFreqMin;
			attackCombined = (attack + modAttack).max(0).min(1);
			sustainCombined = (sustain + modSustain).max(0).min(1);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outSlew = Slew.ar(input, slopeFreqSum, slopeFreqSum);
			outAttack = attackCombined * (input - outSlew);
			outSustain = sustainCombined * outSlew;
			Out.ar(out, startEnv * (((outAttack + outSustain) * mixCombined) + (input * (1-mixCombined))));
		};
		guiSpecArray = [
			["TXMinMaxSliderSplit", "Divide freq", ControlSpec(20, 20000, \exponential),
				"slopeFreq", "slopeFreqMin", "slopeFreqMax"],
			["SpacerLine", 4],
			["EZslider", "Attack level", ControlSpec(0, 1), "attack"],
			["SpacerLine", 4],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain"],
			["SpacerLine", 4],
			["ActionButton", "Reset all controls", {
				this.setSynthValue("slopeFreq", ControlSpec(60, 5000, \exp).unmap(500));
				this.setSynthValue("slopeFreqMin", 60);
				this.setSynthValue("slopeFreqMax", 5000);
				this.setSynthValue("attack", 0.5);
				this.setSynthValue("sustain", 0.5);
				system.flagGuiUpd;
			}, 200],
			["SpacerLine", 10],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

