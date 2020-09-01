// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXAudioIn4 : TXModuleBase {		// Audio In module

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Audio Inputs";
		classData.moduleRate = "audio";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Left level", 1, "modLeftLvl", 0],
			["Right level", 1, "modRightLvl", 0]
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
			["out", nil, 0],
			["leftLevel", 1.0, defLagTime],
			["rightLevel", 1.0, defLagTime],
			["modLeftLvl", 0, defLagTime],
			["modRightLvl", 0, defLagTime],
		];
		arrOptions = [0];
		arrOptionData = [
			[	["Audio Inputs: 1 & 2", {SoundIn.ar([0, 1]); }],
				["Audio Inputs: 3 & 4", {SoundIn.ar([2, 3]); }],
				["Audio Inputs: 5 & 6", {SoundIn.ar([4, 5]); }],
				["Audio Inputs: 7 & 8", {SoundIn.ar([6, 7]); }],
				["Audio Inputs: 9 & 10", {SoundIn.ar([8, 9]); }],
				["Audio Inputs: 11 & 12", {SoundIn.ar([10, 11]); }],
				["Audio Inputs: 13 & 14", {SoundIn.ar([12, 13]); }],
				["Audio Inputs: 15 & 16", {SoundIn.ar([14, 15]); }],
			],
		];
		synthDefFunc = { arg out, leftLevel, rightLevel, modLeftLvl=0, modRightLvl=0;
			var inputArr;
			var startEnv = TXEnvPresets.startEnvFunc.value;
			inputArr = arrOptionData.at(0).at(arrOptions.at(0)).at(1).value;
			Out.ar(out, startEnv *inputArr.at(0) * (leftLevel + modLeftLvl).max(0).min(1) );
			Out.ar(out+1, startEnv * inputArr.at(1) * (rightLevel + modRightLvl).max(0).min(1) );
		};
		guiSpecArray = [
			["SynthOptionPopup", "Inputs", arrOptionData, 0],
			["SpacerLine", 4],
			["EZSlider", "Left level", \unipolar,"leftLevel"],
			["SpacerLine", 4],
			["EZSlider", "Right level", \unipolar,"rightLevel"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

