// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXPulseDivider : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Pulse Divider";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Trigger", 1, "clockTrig", nil],
			["Reset", 1, "resetTrig", nil],
			["Probability", 1, "modProbability", 0],
		];
		classData.noOutChannels = 7;
		classData.arrOutBusSpecs = [
			["Div 2", [0]],
			["Div 3", [1]],
			["Div 4", [2]],
			["Div 5", [3]],
			["Div 6", [4]],
			["Div 7", [5]],
			["Div 8", [6]],
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["out", 0, 0],
			["clockTrig", 0, 0],
			["t_userClockTrig", 0, 0],
			["resetTrig", 0, 0],
			["t_userResetTrig", 0, 0],
			["probability", 1, 0],
			["modProbability", 0, 0],
		];
		arrOptions = [0, 1];
		arrOptionData = [
			// output range
			[	["Positive only: 0 to 1", {arg input; input}],
				["Positive & Negative: -1 to 1", {arg input; (input * 2) - 1}],
				["Positive & Negative: -0.5 to 0.5", {arg input; input - 0.5}],
			],
			// Trigger when reset
			[
				["Trigger when reset - OFF", {arg trig, reset; trig;}],
				["Trigger when reset - ON", {arg trig, reset; trig + reset;}],
			],
		];
		synthDefFunc = {
			arg out, clockTrig = 0, t_userClockTrig = 0, resetTrig = 0, t_userResetTrig = 0, probability, modProbability = 0;
			var arrClockSeqVals, rangeFunction, arrClocks;
			var holdClockTrig, holdResetTrig, outTrig, probabilitySum, probabilityOut;
			var arrSeqArrays = [
				[ 0, 1, ],
				[ 0, 1, 0 ],
				[ 0, 1, 0, 0 ],
				[ 0, 1, 0, 0, 0 ],
				[ 0, 1, 0, 0, 0, 0 ],
				[ 0, 1, 0, 0, 0, 0, 0 ],
				[ 0, 1, 0, 0, 0, 0, 0, 0 ],
			];
			probabilitySum = (probability + modProbability).max(0).min(1);
			probabilityOut = (Demand.kr(clockTrig, 0, Dwhite(-0.499, 0.499, inf)) + probabilitySum).round.clip;
			holdClockTrig = TXClean.kr(clockTrig + t_userClockTrig).min(probabilityOut);
			holdResetTrig = TXClean.kr(resetTrig + t_userResetTrig);
			outTrig = this.getSynthOption(1).value(holdClockTrig, holdResetTrig);
			arrClockSeqVals = arrSeqArrays.collect({arg item;
				Demand.kr(outTrig, holdResetTrig, Dseq(item, inf));
			});
			rangeFunction = this.getSynthOption(0);
			arrClocks = arrClockSeqVals.collect({arg item;
				var outClock, outRange;
				outClock = Trig1.kr(item, ControlDur.ir);
				outRange = rangeFunction.value(outClock);
			});
			Out.kr(out, TXClean.kr(arrClocks));
		};
		guiSpecArray = [
			["SpacerLine", 10],
			["ActionButton", "Trigger Pulse", {this.moduleNode.set("t_userClockTrig", 1);},
				120, TXColor.white, TXColor.sysGuiCol2],
			["Spacer", 10],
			["ActionButton", "Reset Dividers", {this.moduleNode.set("t_userResetTrig", 1);},
				120, TXColor.white, TXColor.sysGuiCol2],
			["Spacer", 10],
			["SynthOptionCheckBox", " Trigger when reset", arrOptionData, 1, 160],
			["SpacerLine", 10],
			["EZslider", "Probability", ControlSpec(0, 1), "probability"],
			["SpacerLine", 10],
			["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 0],
		];
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Trigger Pulse", {this.moduleNode.set("t_userClockTrig", 1);}],
			["commandAction", "Reset Dividers", {this.moduleNode.set("t_userResetTrig", 1);}],
		]
		++ guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}
}

