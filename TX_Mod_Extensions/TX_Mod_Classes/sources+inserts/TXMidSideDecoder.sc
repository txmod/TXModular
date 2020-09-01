// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMidSideDecoder : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "M-S Decoder";
		classData.moduleRate = "audio";
		classData.moduleType = "source";
		classData.arrAudSCInBusSpecs = [
			["Mid signal", 1, "audioInMid"],
			["Side signal", 1, "audioInSide"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Mid level", 1, "modMidLvl", 0],
			["Side level", 1, "modSideLvl", 0]
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
			["audioInMid", 0, 0],
			["audioInSide", 0, 0],
			["midLvl", 1.0, defLagTime],
			["sideLvl", 1.0, defLagTime],
			["modMidLvl", 0, defLagTime],
			["modSideLvl", 0, defLagTime],
		];
		synthDefFunc = { arg out, audioInMid, audioInSide, midLvl, sideLvl, modMidLvl=0, modSideLvl=0;
			var holdLeft, holdRight, holdMid, holdSide;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			holdMid = InFeedback.ar(audioInMid,1) * (midLvl + modMidLvl).max(0).min(1);
			holdSide = InFeedback.ar(audioInSide,1) * (sideLvl + modSideLvl).max(0).min(1);
			holdLeft = holdMid + holdSide;
			holdRight = holdMid - holdSide;
			Out.ar(out, startEnv * [holdLeft, holdRight]);
		};
		guiSpecArray = [
			["SpacerLine", 4],
			["EZSlider", "Mid level", \unipolar,"midLvl"],
			["SpacerLine", 4],
			["EZSlider", "Side level", \unipolar,"sideLvl"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

