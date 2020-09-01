// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXRingMod2 : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "RingMod";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrAudSCInBusSpecs = [
			["Modulator", 1, "inmodulator"],
		];
		classData.arrCtlSCInBusSpecs = [
			["Diode Mix", 1, "modDiodeMix", 0],
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
			["inmodulator", 0, 0],
			["out", 0, 0],
			["diodeMix", 0.0, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modDiodeMix", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		synthDefFunc = { arg in, inmodulator, out, diodeMix, wetDryMix, modDiodeMix=0, modWetDryMix=0;
			var input, inMod, digiSound, diodeSound, mixSound, outSound, sumDiodeMix, sumWetDryMix;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			sumDiodeMix = (diodeMix + modDiodeMix).max(0).min(1);
			sumWetDryMix = (wetDryMix + modWetDryMix).max(0).min(1);
			input = TXClean.ar(InFeedback.ar(in, 1));
			inMod = TXClean.ar(InFeedback.ar(inmodulator, 1));
			// ring modulate 2 inputs - input bus * modulator bus
			digiSound = input * inMod;
			diodeSound = DiodeRingMod.ar(input, inMod);
			mixSound = (digiSound * (1 - sumDiodeMix)) + (diodeSound * sumDiodeMix);
			outSound = (input * (1 - sumWetDryMix) + (mixSound * sumWetDryMix));
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		guiSpecArray = [
			["SpacerLine", 6],
			["EZslider", "Diode mix", ControlSpec(0, 1), "diodeMix"],
			["SpacerLine", 6],
			["WetDryMixSlider"]
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

