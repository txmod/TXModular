// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXTNoiseRing : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "T Noise Ring";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Trigger", 1, "clockTrig", nil],
			["Reset", 1, "resetTrig", nil],
			["Change Prob", 1, "modChangeProb", 0],
			["High Prob", 1, "modHighProb", 0],
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
		autoModOptions = false;
		arrSynthArgSpecs = [
			["out", 0, 0],
			["clockTrig", 0, 0],
			["t_userClockTrig", 0, 0],
			["resetTrig", 0, 0],
			["t_userResetTrig", 0, 0],
			["changeProb", 0.5, defLagTime],
			["changeProbMin", 0, defLagTime],
			["changeProbMax", 1, defLagTime],
			["highProb", 0.5, defLagTime],
			["highProbMin", 0, defLagTime],
			["highProbMax", 1, defLagTime],
			["modChangeProb", 0, 0],
			["modHighProb", 0, 0],
		];
		arrOptions = [0, 7, 1];
		arrOptionData = [
			// output range
			[
				["Positive only: 0 to 1", {arg input; input;}],
				["Positive & Negative: -1 to 1", {arg input; input.linlin(0, 1, -1, 1)}],
				["Positive & Negative: -0.5 to 0.5", {arg input; input.linlin(0, 1, -0.5, 0.5)}],
			],
			// bits
			[
				["1 bit", 1],
				["2 bits", 2],
				["3 bits", 3],
				["4 bits", 4],
				["5 bits", 5],
				["6 bits", 6],
				["7 bits", 7],
				["8 bits", 8],
				["9 bits", 9],
				["10 bits", 10],
				["11 bits", 11],
				["12 bits", 12],
				["13 bits", 13],
				["14 bits", 14],
				["15 bits", 15],
				["16 bits", 16],
				["17 bits", 17],
				["18 bits", 18],
				["19 bits", 19],
				["20 bits", 20],
				["21 bits", 21],
				["22 bits", 22],
				["23 bits", 23],
				["24 bits", 24],
				["25 bits", 25],
				["26 bits", 26],
				["27 bits", 27],
				["28 bits", 28],
				["29 bits", 29],
				["30 bits", 30],
				["31 bits", 31],
				["32 bits", 32],
			],
			// Trigger when reset
			[
				["Trigger when reset - OFF", {arg trig, reset; trig;}],
				["Trigger when reset - ON", {arg trig, reset; trig + reset;}],
			],
		];
		synthDefFunc = { arg out,
			clockTrig = 0, t_userClockTrig = 0, resetTrig = 0, t_userResetTrig = 0,
			changeProb, changeProbMin, changeProbMax, highProb, highProbMin, highProbMax,
			modChangeProb = 0, modHighProb = 0;

			var changeProbSum, highProbSum, numBits, outDemand;
			var rangeFunction, outSignal;
			var holdClockTrig, holdResetTrig, outTrig;

			changeProbSum = (changeProb + modChangeProb).max(0).min(1)
				.linlin(0, 1, changeProbMin, changeProbMax);
			highProbSum = (highProb + modHighProb).max(0).min(1)
				.linlin(0, 1, highProbMin, highProbMax);
			numBits = this.getSynthOption(1);

			holdClockTrig = TXClean.kr(clockTrig + t_userClockTrig);
			holdResetTrig = TXClean.kr(resetTrig + t_userResetTrig);
			outTrig = this.getSynthOption(2).value(holdClockTrig, holdResetTrig);
			outDemand = Demand.kr(outTrig, holdResetTrig,
				DNoiseRing(changeProbSum, highProbSum, numBits: numBits)
			);
			outDemand = outDemand * ((2**numBits) - 1).reciprocal; // normalise

			rangeFunction = this.getSynthOption(0);
			outSignal = rangeFunction.value(outDemand);
			Out.kr(out, TXClean.kr(outSignal));
		};
		guiSpecArray = [
			["ActionButton", "Trigger", {this.moduleNode.set("t_userClockTrig", 1);},
				120, TXColor.white, TXColor.sysGuiCol2],
			["Spacer", 20],
			["ActionButton", "Reset", {this.moduleNode.set("t_userResetTrig", 1);},
				120, TXColor.white, TXColor.sysGuiCol1],
			["Spacer", 20],
			["SynthOptionCheckBox", " Trigger when reset", arrOptionData, 2, 160],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Bits", arrOptionData, 1],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Change prob", ControlSpec(0, 1), "changeProb", "changeProbMin", "changeProbMax"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "High prob", ControlSpec(0, 1), "highProb", "highProbMin", "highProbMax"],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 0],
		];
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Trigger", {this.moduleNode.set("t_userClockTrig", 1);}],
			["commandAction", "Reset", {this.moduleNode.set("t_userResetTrig", 1);}],
		]
		++ guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

