// Copyright (C) 2017Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXConvertToControl : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Convert To Control";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrAudSCInBusSpecs = [
			["Audio In", 1, "audioIn"]
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
			["audioIn", 0, 0],
			["outRangeMin", -1, 0],
			["outRangeMax", 1, 0],
		];
		synthDefFunc = {
			arg out, audioIn, outRangeMin, outRangeMax;
			var in, converted;
			in = TXClean.ar(InFeedback.ar(audioIn,1));
			converted = A2K.kr(in).linlin(-1, 1, outRangeMin, outRangeMax);
			Out.kr(out, converted);
		};
		guiSpecArray = [
			["SpacerLine", 10],
			["TXRangeSlider", "Output range", [-1, 1].asSpec, "outRangeMin", "outRangeMax", nil,
				[
					["Presets: ", {}],
					["0 to +1", [0, 1]],
					["-0.5 to +0.5", [-0.5, 0.5]],
					["-1 to +1 (default)", [-1, 1]],
				]
			],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

