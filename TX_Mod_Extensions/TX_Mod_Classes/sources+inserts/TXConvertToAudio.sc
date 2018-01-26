// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXConvertToAudio : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Convert To Audio";
		classData.moduleRate = "audio";
		classData.moduleType = "source";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Control In", 1, "controlInput", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName, arrPresets;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["out", 0, 0],
			["controlInput", 0, defLagTime],
			["inputRangeMin", -1, 0],
			["inputRangeMax", 1, 0],
		];
		arrOptions = [1];
		arrOptionData = [
			[
				["Off", {arg input; input;}],
				["On", {arg input; LeakDC.ar(input, 0.995);}],
			];
		];
		synthDefFunc = { arg out, controlInput, inputRangeMin, inputRangeMax;
			var input, converted, dcFunction, afterDC;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			converted = K2A.ar(controlInput.linlin(inputRangeMin, inputRangeMax, -1, 1, ));
			dcFunction = this.getSynthOption(0);
			afterDC = dcFunction.value(converted);
			Out.ar(out, startEnv *afterDC);
		};
		guiSpecArray = [
			["SpacerLine", 10],
			["TXRangeSlider", "Input range", [-1, 1].asSpec, "inputRangeMin", "inputRangeMax", nil,
				[
					["Presets: ", {}],
					["0 to +1", [0, 1]],
					["-0.5 to +0.5", [-0.5, 0.5]],
					["-1 to +1 (default)", [-1, 1]],
				]
			],
			["SpacerLine", 10],
			["SynthOptionCheckBox", "Remove DC", arrOptionData, 0, 200],

		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

