// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXTRandom : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "T Random";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Trigger", 1, "clockTrig", nil],
			["Reset", 1, "resetTrig", nil],
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
			["triggerCount", 8, defLagTime],
		];
		arrOptions = [0, 0, 1];
		arrOptionData = [
			// output range
			[
				["Positive only: 0 to 1", {arg input; input;}],
				["Positive & Negative: -1 to 1", {arg input; input.linlin(0, 1, -1, 1)}],
				["Positive & Negative: -0.5 to 0.5", {arg input; input.linlin(0, 1, -0.5, 0.5)}],
			],
			// Length
			[
				["Infinite - ignore trigger count", {arg count; inf;}],
				["Limited until reset - use trigger count", {arg count; count;}],
			],
			// Trigger when reset
			[
				["Trigger when reset - OFF", {arg trig, reset; trig;}],
				["Trigger when reset - ON", {arg trig, reset; trig + reset;}],
			],
		];
		synthDefFunc = { arg out,
			clockTrig = 0, t_userClockTrig = 0, resetTrig = 0, t_userResetTrig = 0, triggerCount;

			var outLength, holdClockTrig, holdResetTrig, outTrig;
			var outDemand, rangeFunction, outSignal;

			outLength = this.getSynthOption(1).value(triggerCount);
			holdClockTrig = TXClean.kr(clockTrig + t_userClockTrig);
			holdResetTrig = TXClean.kr(resetTrig + t_userResetTrig);
			outTrig = this.getSynthOption(2).value(holdClockTrig, holdResetTrig);
			outDemand = Demand.kr(outTrig, holdResetTrig,
				Dwhite(0, 1, outLength)
			);

			rangeFunction = this.getSynthOption(0);
			outSignal = rangeFunction.value(outDemand);
			Out.kr(out, TXClean.kr(outSignal));
		};
		guiSpecArray = [
			["ActionButton", "Trigger", {this.moduleNode.set("t_userClockTrig", 1);},
				100, TXColor.white, TXColor.sysGuiCol2],
			["Spacer", 20],
			["ActionButton", "Reset", {this.moduleNode.set("t_userResetTrig", 1);},
				100, TXColor.white, TXColor.sysGuiCol1],
			["Spacer", 20],
			["SynthOptionCheckBox", " Trigger when reset", arrOptionData, 2, 160],
			["SpacerLine", 10],
			["SynthOptionPopupPlusMinus", "Length", arrOptionData, 1],
			["SpacerLine", 10],
			["EZNumber", "Trigger count", ControlSpec(1, 100000, step: 1), "triggerCount",
				{if (arrOptions[1] == 1, {this.rebuildSynth;}); }],
			["DividingLine"],
			["SpacerLine", 10],
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

