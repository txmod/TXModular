// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXGaussClock : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Gauss Clock";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Play", 1, "modPlay", 0],
			["BPM", 1, "modClockBPM", 0],
			["Multiply BPM", 1, "modMultiplyBPM", 0],
			["Jitter", 1, "modJitter", 0],
			["Probability", 1, "modProbability", 0],
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
			["play", 1, 0],
			["t_userClockTrig", 0, 0],
			["clockBPM", 0.3333333, 0], 		// default is set for 120 bpm
			["clockBPMMin", 30, 0],
			["clockBPMMax", 300, 0],
			["multiplyBPM", 0],
			["multiplyBPMMin", 1],
			["multiplyBPMMax", 16],
			["jitter", 0.3, 0],
			["jitterMin", 0],
			["jitterMax", 1],
			["probability", 1, 0],
			["modClockBPM", 0, 0],
			["modMultiplyBPM", 0, 0],
			["modJitter", 0, 0],
			["modProbability", 0, 0],
			["modPlay", 0, 0],
		];
		arrOptions = [0];
		arrOptionData = [
			[	["Positive only: 0 to 1", {arg input; input}],
				["Positive & Negative: -1 to 1", {arg input; (input * 2) - 1}],
				["Positive & Negative: -0.5 to 0.5", {arg input; input - 0.5}],
			],
		];

		synthDefFunc = { arg out, play, t_userClockTrig = 0, clockBPM, clockBPMMin, clockBPMMax,
			multiplyBPM, multiplyBPMMin, multiplyBPMMax, jitter, jitterMin, jitterMax, probability,
			modClockBPM = 0, modMultiplyBPM = 0, modJitter = 0, modProbability = 0, modPlay = 0;

			var outClock, rangeFunction, outSignal, clockFreq;
			var jitterSum, probabilitySum, outProbability, clockBPMSum, multiplyBPMSum, playSum;

			clockBPMSum = (clockBPM + modClockBPM).max(0).min(1).linlin(0, 1, clockBPMMin, clockBPMMax);
			multiplyBPMSum = (multiplyBPM + modMultiplyBPM).max(0).min(1)
				.linlin(0, 1, multiplyBPMMin, multiplyBPMMax).round;
			clockFreq = clockBPMSum *  multiplyBPMSum / 60;
			jitterSum = (jitter + modJitter).max(0).min(1).linlin(0, 1, jitterMin, jitterMax);
			probabilitySum = (probability + modProbability).max(0).min(1);
			playSum = (play + modPlay).max(0).min(1).round;
			rangeFunction = this.getSynthOption(0);

			outClock = GaussTrig.kr(clockFreq, jitterSum.min(0.999)).min(playSum);
			outProbability = (Demand.kr(outClock, 0, Dwhite(-0.499, 0.499, inf)) + probabilitySum).round.clip;
			outSignal = rangeFunction.value(t_userClockTrig + outClock.min(outProbability));
			Out.kr(out, TXClean.kr(outSignal));
		};
		guiSpecArray = [
			["TXCheckBox", "Play", "play", {}, 120],
			["Spacer", 10],
			["ActionButton", "Trigger Pulse", {this.moduleNode.set("t_userClockTrig", 1);},
				120, TXColor.white, TXColor.sysGuiCol2],
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "BPM", ControlSpec(1, 900), "clockBPM", "clockBPMMin", "clockBPMMax"],
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "Multiply BPM", ControlSpec(1, 16, step: 1), "multiplyBPM",
				"multiplyBPMMin", "multiplyBPMMax"],
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "Jitter", ControlSpec(0, 1), "jitter", "jitterMin", "jitterMax"],
			["SpacerLine", 10],
			["EZslider", "Probability", ControlSpec(0, 1), "probability"],
			["SpacerLine", 10],
			["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 0],
		];
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Trigger Pulse", {this.moduleNode.set("t_userClockTrig", 1);}],
		]
		++ guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

