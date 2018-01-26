// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXStereoWidth : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Stereo Width";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Width L", 1, "modPanL", 0],
			["Width R", 1, "modPanR", 0],
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
			["panL", 0, defLagTime],
			["panR", 1, defLagTime],
			["modPanL", 0, defLagTime],
			["modPanR", 0, defLagTime],
		];
		synthDefFunc = {
			arg in, out, panL, panR, modPanL=0, modPanR=0;
			var inputL, inputR, outSound, sumPanL, sumPanR;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			inputL = InFeedback.ar(in,1);
			inputR = InFeedback.ar(in+1,1);
			sumPanL = (panL + modPanL).max(0).min(1);
			sumPanR = (panR + modPanR).max(0).min(1);
			outSound = Mix.new([Pan2.ar(inputL, sumPanL.madd(2,-1)), Pan2.ar(inputR, sumPanR.madd(2,-1))]);
			Out.ar(out, startEnv * outSound);
		};
		guiSpecArray = [
			["TXRangeSlider", "Width", ControlSpec(0, 1), "panL", "panR", nil, [
				["Presets:", []], ["Full Stereo", [0, 1]], ["Half Stereo", [0.25, 0.75]], ["Centre Only", [0.5, 0.5]],
				["Left Half", [0, 0.5]], ["Left Bias", [0, 0.75]], ["Right Bias", [0.25, 1]], ["Right Half", [0.5, 1]]
			]]
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

