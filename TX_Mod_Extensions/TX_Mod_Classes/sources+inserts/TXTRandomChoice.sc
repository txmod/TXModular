// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXTRandomChoice : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "T Random Choice";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Trigger", 1, "clockTrig", nil],
			["Reset", 1, "resetTrig", nil],
			["Value 1", 1, "modValue1", 0],
			["Value 2", 1, "modValue2", 0],
			["Value 3", 1, "modValue3", 0],
			["Value 4", 1, "modValue4", 0],
			["Value 5", 1, "modValue5", 0],
			["Value 6", 1, "modValue6", 0],
			["Value 7", 1, "modValue7", 0],
			["Value 8", 1, "modValue8", 0],
			["Value 9", 1, "modValue9", 0],
			["Value 10", 1, "modValue10", 0],
			["Value 11", 1, "modValue11", 0],
			["Value 12", 1, "modValue12", 0],
			["Value 13", 1, "modValue13", 0],
			["Value 14", 1, "modValue14", 0],
			["Value 15", 1, "modValue15", 0],
			["Value 16", 1, "modValue16", 0],
			["Weight 1", 1, "modWeight1", 0],
			["Weight 2", 1, "modWeight2", 0],
			["Weight 3", 1, "modWeight3", 0],
			["Weight 4", 1, "modWeight4", 0],
			["Weight 5", 1, "modWeight5", 0],
			["Weight 6", 1, "modWeight6", 0],
			["Weight 7", 1, "modWeight7", 0],
			["Weight 8", 1, "modWeight8", 0],
			["Weight 9", 1, "modWeight9", 0],
			["Weight 10", 1, "modWeight10", 0],
			["Weight 11", 1, "modWeight11", 0],
			["Weight 12", 1, "modWeight12", 0],
			["Weight 13", 1, "modWeight13", 0],
			["Weight 14", 1, "modWeight14", 0],
			["Weight 15", 1, "modWeight15", 0],
			["Weight 16", 1, "modWeight16", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.guiWidth = 1000;
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
			["triggerCount", 8, 0],
			["value1", 0, 0],
			["value2", 1 / 15, 0],
			["value3", 2 / 15, 0],
			["value4", 3 / 15, 0],
			["value5", 4 / 15, 0],
			["value6", 5 / 15, 0],
			["value7", 6 / 15, 0],
			["value8", 7 / 15, 0],
			["value9", 8 / 15, 0],
			["value10", 9 / 15, 0],
			["value11", 10 / 15, 0],
			["value12", 11 / 15, 0],
			["value13", 12 / 15, 0],
			["value14", 13 / 15, 0],
			["value15", 14 / 15, 0],
			["value16", 1, 0],
			["weight1", 0.5, 0],
			["weight2", 0.5, 0],
			["weight3", 0.5, 0],
			["weight4", 0.5, 0],
			["weight5", 0.5, 0],
			["weight6", 0.5, 0],
			["weight7", 0.5, 0],
			["weight8", 0.5, 0],
			["weight9", 0.5, 0],
			["weight10", 0.5, 0],
			["weight11", 0.5, 0],
			["weight12", 0.5, 0],
			["weight13", 0.5, 0],
			["weight14", 0.5, 0],
			["weight15", 0.5, 0],
			["weight16", 0.5, 0],
			["modValue1", 0, 0],
			["modValue2", 0, 0],
			["modValue3", 0, 0],
			["modValue4", 0, 0],
			["modValue5", 0, 0],
			["modValue6", 0, 0],
			["modValue7", 0, 0],
			["modValue8", 0, 0],
			["modValue9", 0, 0],
			["modValue10", 0, 0],
			["modValue11", 0, 0],
			["modValue12", 0, 0],
			["modValue13", 0, 0],
			["modValue14", 0, 0],
			["modValue15", 0, 0],
			["modValue16", 0, 0],
			["modWeight1", 0, 0],
			["modWeight2", 0, 0],
			["modWeight3", 0, 0],
			["modWeight4", 0, 0],
			["modWeight5", 0, 0],
			["modWeight6", 0, 0],
			["modWeight7", 0, 0],
			["modWeight8", 0, 0],
			["modWeight9", 0, 0],
			["modWeight10", 0, 0],
			["modWeight11", 0, 0],
			["modWeight12", 0, 0],
			["modWeight13", 0, 0],
			["modWeight14", 0, 0],
			["modWeight15", 0, 0],
			["modWeight16", 0, 0],
		];
		arrOptions = [0, 0, 1, 14, 0];
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
			// Choices
			[
				["2 Choices", 2],
				["3 Choices", 3],
				["4 Choices", 4],
				["5 Choices", 5],
				["6 Choices", 6],
				["7 Choices", 7],
				["8 Choices", 8],
				["9 Choices", 9],
				["10 Choices", 10],
				["11 Choices", 11],
				["12 Choices", 12],
				["13 Choices", 13],
				["14 Choices", 14],
				["15 Choices", 15],
				["16 Choices", 16],
			],
			// Random Function
			[
				["Weighted Random Choice - weights affect probability of choice", {arg arrValues, arrWeights, outLength;
					Dwrand(arrValues, arrWeights, outLength);
				}],
				["Random Choice - weights ignored", {arg arrValues, arrWeights, outLength;
					Drand(arrValues, outLength);
				}],
				["Exclusive Random Choice - weights ignored, never repeats the same choice twice",
					{arg arrValues, arrWeights, outLength;
					Dxrand(arrValues, outLength);
				}],
			],
		];
		synthDefFunc = { arg out,
			clockTrig = 0, t_userClockTrig = 0, resetTrig = 0, t_userResetTrig = 0, triggerCount,
			value1, value2, value3, value4, value5, value6, value7, value8,
			value9, value10, value11, value12, value13, value14, value15, value16,
			weight1, weight2, weight3, weight4, weight5, weight6, weight7, weight8,
			weight9, weight10, weight11, weight12, weight13, weight14, weight15, weight16,
			modValue1 = 0, modValue2 = 0, modValue3 = 0, modValue4 = 0,
			modValue5 = 0, modValue6 = 0, modValue7 = 0, modValue8 = 0,
			modValue9 = 0, modValue10 = 0, modValue11 = 0, modValue12 = 0,
			modValue13 = 0, modValue14 = 0, modValue15 = 0, modValue16 = 0,
			modWeight1 = 0, modWeight2 = 0, modWeight3 = 0, modWeight4 = 0,
			modWeight5 = 0, modWeight6 = 0, modWeight7 = 0, modWeight8 = 0,
			modWeight9 = 0, modWeight10 = 0, modWeight11 = 0, modWeight12 = 0,
			modWeight13 = 0, modWeight14 = 0, modWeight15 = 0, modWeight16 = 0;

			var outLength, holdClockTrig, holdResetTrig, outTrig, outDist;
			var choiceCount, arrAllValues, arrAllWeights, arrValues, arrWeights;
			var randFunction, outDemand, rangeFunction, outSignal;

			arrAllValues = [
				(value1 + modValue1),
				(value2 + modValue2),
				(value3 + modValue3),
				(value4 + modValue4),
				(value5 + modValue5),
				(value6 + modValue6),
				(value7 + modValue7),
				(value8 + modValue8),
				(value9 + modValue9),
				(value10 + modValue10),
				(value11 + modValue11),
				(value12 + modValue12),
				(value13 + modValue13),
				(value14 + modValue14),
				(value15 + modValue15),
				(value16 + modValue16),
			];
			arrAllWeights = [
				(weight1 + modWeight1),
				(weight2 + modWeight2),
				(weight3 + modWeight3),
				(weight4 + modWeight4),
				(weight5 + modWeight5),
				(weight6 + modWeight6),
				(weight7 + modWeight7),
				(weight8 + modWeight8),
				(weight9 + modWeight9),
				(weight10 + modWeight10),
				(weight11 + modWeight11),
				(weight12 + modWeight12),
				(weight13 + modWeight13),
				(weight14 + modWeight14),
				(weight15 + modWeight15),
				(weight16 + modWeight16),
			];
			choiceCount = this.getSynthOption(3);
			arrValues = arrAllValues.copy.keep(choiceCount).max(0).min(1);
			arrWeights = arrAllWeights.copy.keep(choiceCount).max(0.001).min(1);
			arrWeights = arrWeights * arrWeights.sum.reciprocal;  // normalise

			outLength = this.getSynthOption(1).value(triggerCount);
			holdClockTrig = TXClean.kr(clockTrig + t_userClockTrig);
			holdResetTrig = TXClean.kr(resetTrig + t_userResetTrig);
			outTrig = this.getSynthOption(2).value(holdClockTrig, holdResetTrig);
			randFunction = this.getSynthOption(4);
			outDemand = Demand.kr(outTrig, holdResetTrig,
				randFunction.value(arrValues, arrWeights, outLength)
			);
			rangeFunction = this.getSynthOption(0);
			outSignal = rangeFunction.value(outDemand);
			Out.kr(out, TXClean.kr(outSignal));
		};
		this.buildGuiSpecArray;
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

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Trigger", {this.moduleNode.set("t_userClockTrig", 1);},
				100, TXColor.white, TXColor.sysGuiCol2],
			["Spacer", 20],
			["ActionButton", "Reset", {this.moduleNode.set("t_userResetTrig", 1);},
				100, TXColor.white, TXColor.sysGuiCol1],
			["Spacer", 20],
			["SynthOptionCheckBox", " Trigger when reset", arrOptionData, 2, 160],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Length", arrOptionData, 1, 600],
			["SpacerLine", 4],
			["EZNumber", "Trigger count", ControlSpec(1, 100000, step: 1), "triggerCount",
				{if (arrOptions[1] == 1, {this.rebuildSynth;}); }],
			["DividingLine"],
			["SpacerLine", 6],
			["SynthOptionPopupPlusMinus", "Rand type", arrOptionData, 4, 600],
			["SpacerLine", 6],
			["SynthOptionPopupPlusMinus", "No Choices", arrOptionData, 3, 600, {this.buildGuiSpecArray; system.showViewIfModDisplay(this)}],
			["SpacerLine", 6],
			["TextBar", "Choices", 80],
			["TextBar", "1", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(1)],
			["TextBar", "2", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(2)],
			["TextBar", "3", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(3)],
			["TextBar", "4", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(4)],
			["TextBar", "5", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(5)],
			["TextBar", "6", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(6)],
			["TextBar", "7", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(7)],
			["TextBar", "8", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(8)],
			["TextBar", "9", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(9)],
			["TextBar", "10", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(10)],
			["TextBar", "11", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(11)],
			["TextBar", "12", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(12)],
			["TextBar", "13", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(13)],
			["TextBar", "14", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(14)],
			["TextBar", "15", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(15)],
			["TextBar", "16", 50, 20, TXColor.sysGuiCol1, this.getChoiceCol(16)],
			["SpacerLine", 2],
			["ActionButtonBig", "1->all",  {this.copy1ToAll([ "value1", "value2", "value3", "value4",
				"value5", "value6", "value7", "value8", "value9", "value10", "value11", "value12", "value13",
				"value14", "value15", "value16", ]);}, 36],
			["TXMultiKnobNo", "Value", [ "value1", "value2", "value3", "value4", "value5", "value6",
				"value7", "value8", "value9", "value10", "value11", "value12", "value13", "value14", "value15",
				"value16", ], 8, ControlSpec(0, 1)],
			["NextLine"],
			["ActionButtonBig", "1->all",  {this.copy1ToAll(["weight1", "weight2", "weight3", "weight4", "weight5",
				"weight6", "weight7", "weight8", "weight9", "weight10", "weight11", "weight12", "weight13",
				"weight14", "weight15", "weight16" ]);}, 36],
			["TXMultiKnobNo", "Weight", ["weight1", "weight2", "weight3", "weight4", "weight5", "weight6",
				"weight7", "weight8", "weight9", "weight10", "weight11", "weight12", "weight13", "weight14",
				"weight15", "weight16" ], 8, ControlSpec(0.001, 1)],
			["DividingLine"],
			["SpacerLine", 6],
			["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 0, 600],
		];
	}

	getChoiceCol { arg choiceNo;
		if (this.getSynthOption(3) < choiceNo, {
			^TXColor.grey(0.8);
		},{
			^TXColor.white;
		});
	}

	copy1ToAll {arg arrayStrings;
		var holdVal = this.getSynthArgSpec(arrayStrings[0]);
		arrayStrings.do({arg item, i;
			if (i > 0, {
				this.setSynthValue(item, holdVal);
			});
		});
		system.showView;
	}

}

