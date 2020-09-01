// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXGain : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Gain";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["output gain", 1, "modOutGain", 0]
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
		var arrPresets;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["outGain", 1.0, defLagTime],
			["outGainMin", 0.0, defLagTime],
			["outGainMax", 1.0, defLagTime],
			["phaseInvert", 0, defLagTime],
			["modOutGain", 0, defLagTime],
		];
		synthDefFunc = { arg in, out, outGain, outGainMin, outGainMax, phaseInvert, modOutGain=0;
			var input, outGainCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in,1) * (1 - (2 * phaseInvert));
			outGainCombined = outGainMin + ( (outGainMax - outGainMin) * (outGain + modOutGain).max(0).min(1));
			Out.ar(out, startEnv * input * outGainCombined);
		};
		arrPresets = [
			["Presets: ", [0, 1]],
			["0 - 1", [0, 1]],
			["0 - 2", [0, 2]],
			["0 - 5", [0, 5]],
			["0 - 10", [0, 10]],
			["1 - 2", [1, 2]],
			["1 - 5", [1, 5]],
			["1 - 10", [1, 10]],
		];
		guiSpecArray = [
			["TXMinMaxSliderSplit", "Out gain", ControlSpec(0, 10),
				"outGain", "outGainMin", "outGainMax", nil, arrPresets],
			["SpacerLine", 4],
			["TextBar", "Phase ", nil, nil, nil, nil, \right],
			["TXCheckBox", "Invert", "phaseInvert"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

