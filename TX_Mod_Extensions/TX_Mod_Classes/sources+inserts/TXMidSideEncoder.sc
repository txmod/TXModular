// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMidSideEncoder : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "M-S Encoder";
		classData.moduleRate = "audio";
		classData.moduleType = "source";
		classData.arrAudSCInBusSpecs = [
			["Audio in L + R", 2, "audioIn"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Left level", 1, "modLeftLvl", 0],
			["Right level", 1, "modRightLvl", 0]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Mid signal", [0]],
			["Side signal", [1]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["out", nil, 0],
			["audioIn", 0, 0],
			["leftLevel", 1.0, defLagTime],
			["rightLevel", 1.0, defLagTime],
			["modLeftLvl", 0, defLagTime],
			["modRightLvl", 0, defLagTime],
		];
		synthDefFunc = { arg out, audioIn, leftLevel, rightLevel, modLeftLvl=0, modRightLvl=0;
			var arrInputs, holdLeft, holdRight, holdMid, holdSide;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			arrInputs = InFeedback.ar(audioIn,2);
			holdLeft = arrInputs[0]  * (leftLevel + modLeftLvl).max(0).min(1);
			holdRight = arrInputs[1]  * (rightLevel + modRightLvl).max(0).min(1);
			holdMid = holdLeft + holdRight;
			holdSide = holdLeft - holdRight;
			Out.ar(out, startEnv * [holdMid, holdSide]);
		};
		guiSpecArray = [
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

