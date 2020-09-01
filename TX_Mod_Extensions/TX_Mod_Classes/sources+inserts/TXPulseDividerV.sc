// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXPulseDividerV : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Pulse DividerV";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Trigger", 1, "clockTrig", nil],
			["Reset", 1, "resetTrig", nil],
			["Probability", 1, "modProbability", 0],
			["Div A", 1, "modDivOutA", 0],
			["Div B", 1, "modDivOutB", 0],
			["Div C", 1, "modDivOutC", 0],
			["Div D", 1, "modDivOutD", 0],
			["Offset A", 1, "modOffsetOutA", 0],
			["Offset B", 1, "modOffsetOutB", 0],
			["Offset C", 1, "modOffsetOutC", 0],
			["Offset D", 1, "modOffsetOutD", 0],
		];
		classData.noOutChannels = 4;
		classData.arrOutBusSpecs = [
			["Out A Div X", [0]],
			["Out B Div X", [1]],
			["Out C Div 2X", [2]],
			["Out D Div 3X", [3]],
		];
		classData.guiWidth = 650;
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
			["divOutA", 0, 0],
			["divOutAMin", 2, 0],
			["divOutAMax", 8, 0],
			["divOutB", 0, 0],
			["divOutBMin", 2, 0],
			["divOutBMax", 8, 0],
			["divOutC", 0, 0],
			["divOutCMin", 2, 0],
			["divOutCMax", 24, 0],
			["divOutD", 0, 0],
			["divOutDMin", 3, 0],
			["divOutDMax", 24, 0],
			["offsetOutA", 0, 0],
			["offsetOutAMin", 0, 0],
			["offsetOutAMax", 8, 0],
			["offsetOutB", 0, 0],
			["offsetOutBMin", 0, 0],
			["offsetOutBMax", 8, 0],
			["offsetOutC", 0, 0],
			["offsetOutCMin", 0, 0],
			["offsetOutCMax", 8, 0],
			["offsetOutD", 0, 0],
			["offsetOutDMin", 0, 0],
			["offsetOutDMax", 8, 0],
			["modProbability", 0, 0],
			["modDivOutA", 0, 0],
			["modDivOutB", 0, 0],
			["modDivOutC", 0, 0],
			["modDivOutD", 0, 0],
			["modOffsetOutA", 0, 0],
			["modOffsetOutB", 0, 0],
			["modOffsetOutC", 0, 0],
			["modOffsetOutD", 0, 0],
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
			arg out, clockTrig = 0, t_userClockTrig = 0, resetTrig = 0, t_userResetTrig = 0, probability,
			divOutA, divOutAMin, divOutAMax, divOutB, divOutBMin, divOutBMax,
			divOutC, divOutCMin, divOutCMax, divOutD, divOutDMin, divOutDMax,
			offsetOutA, offsetOutAMin, offsetOutAMax, offsetOutB, offsetOutBMin, offsetOutBMax,
			offsetOutC, offsetOutCMin, offsetOutCMax, offsetOutD, offsetOutDMin, offsetOutDMax,
			modProbability = 0,
			modDivOutA = 0, modDivOutB = 0, modDivOutC = 0, modDivOutD = 0,
			modOffsetOutA = 0, modOffsetOutB = 0, modOffsetOutC = 0, modOffsetOutD = 0;
			var arrClockTrigVals, rangeFunction, arrClocks;
			var holdClockTrig, holdResetTrig, outTrig, holdCount, probabilitySum, probabilityOut;
			var divOutASum, divOutBSum, divOutCSum, divOutDSum;
			var offsetOutASum, offsetOutBSum, offsetOutCSum, offsetOutDSum;
			rangeFunction = this.getSynthOption(0);
			probabilitySum = (probability + modProbability).max(0).min(1);
			probabilityOut = (Demand.kr(clockTrig, 0, Dwhite(-0.499, 0.499, inf)) + probabilitySum).round.clip;
			holdClockTrig = TXClean.kr(clockTrig + t_userClockTrig).min(probabilityOut);
			holdResetTrig = TXClean.kr(resetTrig + t_userResetTrig);
			outTrig = this.getSynthOption(1).value(holdClockTrig, holdResetTrig);
			divOutASum = (divOutA + modDivOutA).max(0).min(1).linlin(0, 1, divOutAMin, divOutAMax).round;
			divOutBSum = (divOutB + modDivOutB).max(0).min(1).linlin(0, 1, divOutBMin, divOutBMax).round;
			divOutCSum = (divOutC + modDivOutC).max(0).min(1).linlin(0, 1, divOutCMin, divOutCMax).round;
			divOutDSum = (divOutD + modDivOutD).max(0).min(1).linlin(0, 1, divOutDMin, divOutDMax).round;
			offsetOutASum = (offsetOutA + modOffsetOutA).max(0).min(1).linlin(0, 1, offsetOutAMin, offsetOutAMax).round;
			offsetOutBSum = (offsetOutB + modOffsetOutB).max(0).min(1).linlin(0, 1, offsetOutBMin, offsetOutBMax).round;
			offsetOutCSum = (offsetOutC + modOffsetOutC).max(0).min(1).linlin(0, 1, offsetOutCMin, offsetOutCMax).round;
			offsetOutDSum = (offsetOutD + modOffsetOutD).max(0).min(1).linlin(0, 1, offsetOutDMin, offsetOutDMax).round;
			holdCount = Demand.kr(outTrig, holdResetTrig, Dseries(0, 1, inf));
			arrClockTrigVals = [divOutASum, divOutBSum, divOutCSum, divOutDSum].collect({arg item, i;
				 Latch.kr(
					((holdCount  + [offsetOutASum, offsetOutBSum, offsetOutCSum, offsetOutDSum][i])
					% item),
					outTrig);
			});
			arrClocks = arrClockTrigVals.collect({arg item;
				var outClock, outRange;
				outClock = Trig1.kr(item, ControlDur.ir);
				outRange = rangeFunction.value(outClock);
			});
			Out.kr(out, TXClean.kr(arrClocks));
		};
		guiSpecTitleArray = [
			["TitleBar"],
			["HelpButton"],
			["RunPauseButton"],
			["DeleteModuleButton"],
			["RebuildModuleButton"],
			["HideModuleButton"],
			["NextLine"],
			["ModuleActionPopup", 310],
			["ModuleInfoTxt", 310],
			["SpacerLine", 2],
		];
		guiSpecArray = [
			["ActionButton", "Trigger Pulse", {this.moduleNode.set("t_userClockTrig", 1);},
				110, TXColor.white, TXColor.sysGuiCol2],
			["Spacer", 10],
			["ActionButton", "Reset Dividers", {this.moduleNode.set("t_userResetTrig", 1);},
				110, TXColor.white, TXColor.sysGuiCol2],
			["Spacer", 10],
			["SynthOptionCheckBox", " Trigger when reset", arrOptionData, 1, 160],
			["SpacerLine", 1],
			["EZslider", "Probability", ControlSpec(0, 1), "probability", {}, 350],
			["DividingLine"],
			["SpacerLine", 1],
			["TXMinMaxSliderSplit", "Div A", ControlSpec(2, 24, step: 1), "divOutA",
				"divOutAMin", "divOutAMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Div B", ControlSpec(2, 24, step: 1), "divOutB",
				"divOutBMin", "divOutBMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Div C (2 X)", ControlSpec(2, 24, step: 2), "divOutC",
				"divOutCMin", "divOutCMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Div D (3 X)", ControlSpec(3, 24, step: 3), "divOutD",
				"divOutDMin", "divOutDMax"],
			["DividingLine"],
			["SpacerLine", 1],
			["TXMinMaxSliderSplit", "Offset A", ControlSpec(0, 24, step: 1), "offsetOutA",
				"offsetOutAMin", "offsetOutAMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Offset B", ControlSpec(0, 24, step: 1), "offsetOutB",
				"offsetOutBMin", "offsetOutBMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Offset C", ControlSpec(0, 24, step: 1), "offsetOutC",
				"offsetOutCMin", "offsetOutCMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Offset D", ControlSpec(0, 24, step: 1), "offsetOutD",
				"offsetOutDMin", "offsetOutDMax"],
			["DividingLine"],
			["SpacerLine", 1],
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

