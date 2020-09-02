// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMembrane : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Membrane";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Input Gain", 1, "modInGain", 0],
			["Tension", 1, "modTension", 0],
			["Loss", 1, "modLoss", 0],
			["Output Gain", 1, "modOutGain", 0],
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
			["out", 0, 0],
			["inGain", 1, defLagTime],
			["tension", 0.05, defLagTime],
			["tensionMin", 0.01, defLagTime],
			["tensionMax", 0.1, defLagTime],
			["loss", 0.9, defLagTime],
			["lossMin", 0.5, defLagTime],
			["lossMax", 1.0, defLagTime],
			["outGain", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modInGain", 0, defLagTime],
			["modTension", 0, defLagTime],
			["modLoss", 0, defLagTime],
			["modOutGain", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0];
		arrOptionData = [
			[
				["Hexagon (uses less CPU)", {arg input, tension, loss; MembraneHexagon.ar(input, tension, loss);}],
				["Circle (uses more CPU)", {arg input, tension, loss; MembraneCircle.ar(input, tension, loss)}],
			]
		];
		synthDefFunc = { arg in, out, inGain, tension, tensionMin, tensionMax, loss, lossMin, lossMax, outGain, wetDryMix,
			modInGain = 0, modTension = 0, modLoss = 0, modOutGain = 0, modWetDryMix = 0;

			var inGainSum, input, tensionSum, lossSum, outGainSum, holdFunction, controlSignal, mixSum, outSound;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			inGainSum = (inGain + modInGain).max(0).min(2);
			input = TXClean.ar(startEnv * InFeedback.ar(in,1));
			tensionSum = tensionMin + ((tensionMax-tensionMin) * (tension + modTension).max(0).min(1));
			lossSum = lossMin + ((lossMax-lossMin) * (loss + modLoss).max(0).min(1));
			outGainSum = (outGain + modOutGain).max(0).min(1);

			// holdFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			holdFunction = this.getSynthOption(0);
			controlSignal = holdFunction.value(input * inGainSum, tensionSum,  (lossSum * 0.01) + 0.99);
			mixSum = (wetDryMix + modWetDryMix).max(0).min(1);
			outSound = controlSignal * outGainSum;
			Out.ar(out, startEnv * ((outSound * mixSum) + (input * (1-mixSum))));
		};
		guiSpecArray = [
			["SpacerLine", 2],
			["SynthOptionPopupPlusMinus", "Shape", arrOptionData, 0],
			["SpacerLine", 20],
			["EZslider", "In gain", ControlSpec(0, 2), "inGain"],
			["SpacerLine", 20],
			["TXMinMaxSliderSplit", "Tension", ControlSpec(0.002, 0.5, \exp),
				"tension", "tensionMin", "tensionMax"],
			["SpacerLine", 20],
			["TXMinMaxSliderSplit", "Loss", ControlSpec(0, 1, \lin),
				"loss", "lossMin", "lossMax"],
			["SpacerLine", 20],
			["EZslider", "Out gain", ControlSpec(0, 1), "outGain"],
			["SpacerLine", 20],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

