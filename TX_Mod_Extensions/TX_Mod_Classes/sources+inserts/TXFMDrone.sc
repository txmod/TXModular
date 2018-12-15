// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFMDrone : TXModuleBase {

	classvar <>classData;

	var	noteListTextView;
	var	displayOption;
	var displayOperator;
	var ratioView;
	var arrRatioProcs, arrFreqProcs;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.guiWidth = 570;
		classData.arrInstances = [];
		classData.defaultName = "FM Drone";
		classData.moduleRate = "audio";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Base Frequency", 1, "modFreq", 0],
			["Note select", 1, "modFreqSelector", 0],
			["Smoothtime 1", 1, "modLag", 0],
			["Smoothtime 2", 1, "modLag2", 0],
			["Op1 Freq Ratio", 1, "modOp1FreqRatio", 0],
			["Op1 Fixed Freq", 1, "modOp1FixedFreq", 0],
			["Op1 Detune", 1, "modOp1Detune", 0],
			["Op1 Phase",1, "modOp1Phase", 0],
			["Op1 Amp",1, "modOp1Amp", 0],
			["Op2 Freq Ratio", 1, "modOp2FreqRatio", 0],
			["Op2 Fixed Freq", 1, "modOp2FixedFreq", 0],
			["Op2 Detune", 1, "modOp2Detune", 0],
			["Op2 Phase",1, "modOp2Phase", 0],
			["Op2 Amp",1, "modOp2Amp", 0],
			["Op3 Freq Ratio", 1, "modOp3FreqRatio", 0],
			["Op3 Fixed Freq", 1, "modOp3FixedFreq", 0],
			["Op3 Detune", 1, "modOp3Detune", 0],
			["Op3 Phase",1, "modOp3Phase", 0],
			["Op3 Amp",1, "modOp3Amp", 0],
			["Op4 Freq Ratio", 1, "modOp4FreqRatio", 0],
			["Op4 Fixed Freq", 1, "modOp4FixedFreq", 0],
			["Op4 Detune", 1, "modOp4Detune", 0],
			["Op4 Phase",1, "modOp4Phase", 0],
			["Op4 Amp",1, "modOp4Amp", 0],
			["Op5 Freq Ratio", 1, "modOp5FreqRatio", 0],
			["Op5 Fixed Freq", 1, "modOp5FixedFreq", 0],
			["Op5 Detune", 1, "modOp5Detune", 0],
			["Op5 Phase",1, "modOp5Phase", 0],
			["Op5 Amp",1, "modOp5Amp", 0],
			["Op6 Freq Ratio", 1, "modOp6FreqRatio", 0],
			["Op6 Fixed Freq", 1, "modOp6FixedFreq", 0],
			["Op6 Detune", 1, "modOp6Detune", 0],
			["Op6 Phase",1, "modOp6Phase", 0],
			["Op6 Amp",1, "modOp6Amp", 0],
			["Mod Ind 1 -> 1", 1, "modMIndFM_11", 0],
			["Mod Ind 2 -> 1", 1, "modMIndFM_12", 0],
			["Mod Ind 3 -> 1", 1, "modMIndFM_13", 0],
			["Mod Ind 4 -> 1", 1, "modMIndFM_14", 0],
			["Mod Ind 5 -> 1", 1, "modMIndFM_15", 0],
			["Mod Ind 6 -> 1", 1, "modMIndFM_16", 0],
			["Mod Ind 1 -> 2", 1, "modMIndFM_21", 0],
			["Mod Ind 2 -> 2", 1, "modMIndFM_22", 0],
			["Mod Ind 3 -> 2", 1, "modMIndFM_23", 0],
			["Mod Ind 4 -> 2", 1, "modMIndFM_24", 0],
			["Mod Ind 5 -> 2", 1, "modMIndFM_25", 0],
			["Mod Ind 6 -> 2", 1, "modMIndFM_26", 0],
			["Mod Ind 1 -> 3", 1, "modMIndFM_31", 0],
			["Mod Ind 2 -> 3", 1, "modMIndFM_32", 0],
			["Mod Ind 3 -> 3", 1, "modMIndFM_33", 0],
			["Mod Ind 4 -> 3", 1, "modMIndFM_34", 0],
			["Mod Ind 5 -> 3", 1, "modMIndFM_35", 0],
			["Mod Ind 6 -> 3", 1, "modMIndFM_36", 0],
			["Mod Ind 1 -> 4", 1, "modMIndFM_41", 0],
			["Mod Ind 2 -> 4", 1, "modMIndFM_42", 0],
			["Mod Ind 3 -> 4", 1, "modMIndFM_43", 0],
			["Mod Ind 4 -> 4", 1, "modMIndFM_44", 0],
			["Mod Ind 5 -> 4", 1, "modMIndFM_45", 0],
			["Mod Ind 6 -> 4", 1, "modMIndFM_46", 0],
			["Mod Ind 1 -> 5", 1, "modMIndFM_51", 0],
			["Mod Ind 2 -> 5", 1, "modMIndFM_52", 0],
			["Mod Ind 3 -> 5", 1, "modMIndFM_53", 0],
			["Mod Ind 4 -> 5", 1, "modMIndFM_54", 0],
			["Mod Ind 5 -> 5", 1, "modMIndFM_55", 0],
			["Mod Ind 6 -> 5", 1, "modMIndFM_56", 0],
			["Mod Ind 1 -> 6", 1, "modMIndFM_61", 0],
			["Mod Ind 2 -> 6", 1, "modMIndFM_62", 0],
			["Mod Ind 3 -> 6", 1, "modMIndFM_63", 0],
			["Mod Ind 4 -> 6", 1, "modMIndFM_64", 0],
			["Mod Ind 5 -> 6", 1, "modMIndFM_65", 0],
			["Mod Ind 6 -> 6", 1, "modMIndFM_66", 0],
			["Op1 Level", 1, "modOutLevelOp1", 0],
			["Op2 Level", 1, "modOutLevelOp2", 0],
			["Op3 Level", 1, "modOutLevelOp3", 0],
			["Op4 Level", 1, "modOutLevelOp4", 0],
			["Op5 Level", 1, "modOutLevelOp5", 0],
			["Op6 Level", 1, "modOutLevelOp6", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.lagSpec = ControlSpec(0.01, 30, \exp, 0, 1, units: " secs");
		classData.ratioSpec = ControlSpec(0.01, 16, 'amp');
		classData.fixedFreqSpec = ControlSpec(0.1, 10000, \exp);
		classData.fixedFreqOptionData = [
			["Off", {arg freqRatio, modFreqRatio, fixedFreq, modFixedFreq, noteFreq;
				//noteFreq * classData.fixedFreqSpec.constrain(freqRatio + classData.fixedFreqSpec.map(modFreqRatio));
				((16 ** modFreqRatio) * freqRatio) * noteFreq;
			} ],
			["On", {arg freqRatio, modFreqRatio, fixedFreq, modFixedFreq, noteFreq;
				classData.fixedFreqSpec.map((fixedFreq + modFixedFreq).max(0).min(1));
			} ],
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showOperators";
		displayOperator = "showAllOps";
		arrRatioProcs = [
			[	"Set to ratios: 1, 2, 3, 4, 1/2, 3/2 (default values)",
				{
					this.setSynthValue("op1FreqRatio", 1);
					this.setSynthValue("op2FreqRatio", 2);
					this.setSynthValue("op3FreqRatio", 3);
					this.setSynthValue("op4FreqRatio", 4);
					this.setSynthValue("op5FreqRatio", 0.5);
					this.setSynthValue("op6FreqRatio", 1.5);
				},
			],
			[	"Scramble order of ratios",
				{
					var arrVals;
					arrVals = [
						this.getSynthArgSpec("op1FreqRatio"),
						this.getSynthArgSpec("op2FreqRatio"),
						this.getSynthArgSpec("op3FreqRatio"),
						this.getSynthArgSpec("op4FreqRatio"),
						this.getSynthArgSpec("op5FreqRatio"),
						this.getSynthArgSpec("op6FreqRatio"),
					];
					arrVals = arrVals.scramble;
					this.setSynthValue("op1FreqRatio", arrVals[0]);
					this.setSynthValue("op2FreqRatio", arrVals[1]);
					this.setSynthValue("op3FreqRatio", arrVals[2]);
					this.setSynthValue("op4FreqRatio", arrVals[3]);
					this.setSynthValue("op5FreqRatio", arrVals[4]);
					this.setSynthValue("op6FreqRatio", arrVals[5]);
				},
			],
			[	"Randomise all ratios to simple ratios: [1-4] / [1-2]",
				{
					var arrVals;
					arrVals = ({ (1 + 4.rand) / (1 + 2.rand) } ! 6).value;
					this.setSynthValue("op1FreqRatio", arrVals[0]);
					this.setSynthValue("op2FreqRatio", arrVals[1]);
					this.setSynthValue("op3FreqRatio", arrVals[2]);
					this.setSynthValue("op4FreqRatio", arrVals[3]);
					this.setSynthValue("op5FreqRatio", arrVals[4]);
					this.setSynthValue("op6FreqRatio", arrVals[5]);
				},
			],
			[	"Randomise all ratios to simple ratios: [1-6] / [1-3]",
				{
					var arrVals;
					arrVals = ({ (1 + 6.rand) / (1 + 3.rand) } ! 6).value;
					this.setSynthValue("op1FreqRatio", arrVals[0]);
					this.setSynthValue("op2FreqRatio", arrVals[1]);
					this.setSynthValue("op3FreqRatio", arrVals[2]);
					this.setSynthValue("op4FreqRatio", arrVals[3]);
					this.setSynthValue("op5FreqRatio", arrVals[4]);
					this.setSynthValue("op6FreqRatio", arrVals[5]);
				},
			],
			[	"Randomise all ratios to simple ratios: [1-8] / [1-4]",
				{
					var arrVals;
					arrVals = ({ (1 + 8.rand) / (1 + 4.rand) } ! 6).value;
					this.setSynthValue("op1FreqRatio", arrVals[0]);
					this.setSynthValue("op2FreqRatio", arrVals[1]);
					this.setSynthValue("op3FreqRatio", arrVals[2]);
					this.setSynthValue("op4FreqRatio", arrVals[3]);
					this.setSynthValue("op5FreqRatio", arrVals[4]);
					this.setSynthValue("op6FreqRatio", arrVals[5]);
				},
			],
			[	"Randomise all ratios to simple ratios: [1-16] / [1-6]",
				{
					var arrVals;
					arrVals = ({ (1 + 16.rand) / (1 + 6.rand) } ! 6).value;
					this.setSynthValue("op1FreqRatio", arrVals[0]);
					this.setSynthValue("op2FreqRatio", arrVals[1]);
					this.setSynthValue("op3FreqRatio", arrVals[2]);
					this.setSynthValue("op4FreqRatio", arrVals[3]);
					this.setSynthValue("op5FreqRatio", arrVals[4]);
					this.setSynthValue("op6FreqRatio", arrVals[5]);
				},
			],
			[	"Randomise all ratios, range: 0.01 - 16",
				{
					var arrVals;
					arrVals = ({ 0.01.exprand(16.00) } ! 6).value;
					this.setSynthValue("op1FreqRatio", arrVals[0]);
					this.setSynthValue("op2FreqRatio", arrVals[1]);
					this.setSynthValue("op3FreqRatio", arrVals[2]);
					this.setSynthValue("op4FreqRatio", arrVals[3]);
					this.setSynthValue("op5FreqRatio", arrVals[4]);
					this.setSynthValue("op6FreqRatio", arrVals[5]);
				},
			],
			[	"Randomise all ratios, range: 0.25 - 4",
				{
					var arrVals;
					arrVals = ({ 0.25.exprand(4.00) } ! 6).value;
					this.setSynthValue("op1FreqRatio", arrVals[0]);
					this.setSynthValue("op2FreqRatio", arrVals[1]);
					this.setSynthValue("op3FreqRatio", arrVals[2]);
					this.setSynthValue("op4FreqRatio", arrVals[3]);
					this.setSynthValue("op5FreqRatio", arrVals[4]);
					this.setSynthValue("op6FreqRatio", arrVals[5]);
				},
			],
			[	"Randomise all ratios, range: 0.5 - 2",
				{
					var arrVals;
					arrVals = ({ 0.5.exprand(2.00) } ! 6).value;
					this.setSynthValue("op1FreqRatio", arrVals[0]);
					this.setSynthValue("op2FreqRatio", arrVals[1]);
					this.setSynthValue("op3FreqRatio", arrVals[2]);
					this.setSynthValue("op4FreqRatio", arrVals[3]);
					this.setSynthValue("op5FreqRatio", arrVals[4]);
					this.setSynthValue("op6FreqRatio", arrVals[5]);
				},
			],
		];
		arrFreqProcs = [
			[	"Set all frequencies to MIDI note 60, Middle C (default)",
				{
					this.setSynthValue("op1FixedFreq", classData.fixedFreqSpec.unmap(60.midicps));
					this.setSynthValue("op2FixedFreq", classData.fixedFreqSpec.unmap(60.midicps));
					this.setSynthValue("op3FixedFreq", classData.fixedFreqSpec.unmap(60.midicps));
					this.setSynthValue("op4FixedFreq", classData.fixedFreqSpec.unmap(60.midicps));
					this.setSynthValue("op5FixedFreq", classData.fixedFreqSpec.unmap(60.midicps));
					this.setSynthValue("op6FixedFreq", classData.fixedFreqSpec.unmap(60.midicps));
				},
			],
			[	"Scramble order of frequencies",
				{
					var arrVals;
					arrVals = [
						this.getSynthArgSpec("op1FixedFreq"),
						this.getSynthArgSpec("op2FixedFreq"),
						this.getSynthArgSpec("op3FixedFreq"),
						this.getSynthArgSpec("op4FixedFreq"),
						this.getSynthArgSpec("op5FixedFreq"),
						this.getSynthArgSpec("op6FixedFreq"),
					];
					arrVals = arrVals.scramble;
					this.setSynthValue("op1FixedFreq", arrVals[0]);
					this.setSynthValue("op2FixedFreq", arrVals[1]);
					this.setSynthValue("op3FixedFreq", arrVals[2]);
					this.setSynthValue("op4FixedFreq", arrVals[3]);
					this.setSynthValue("op5FixedFreq", arrVals[4]);
					this.setSynthValue("op6FixedFreq", arrVals[5]);
				},
			],
			[	"Randomise all frequencies, range: 0.1 - 4 hz",
				{
					var arrVals;
					arrVals = ({ classData.fixedFreqSpec.unmap(0.1.exprand(4.0) )} ! 6).value;
					this.setSynthValue("op1FixedFreq", arrVals[0]);
					this.setSynthValue("op2FixedFreq", arrVals[1]);
					this.setSynthValue("op3FixedFreq", arrVals[2]);
					this.setSynthValue("op4FixedFreq", arrVals[3]);
					this.setSynthValue("op5FixedFreq", arrVals[4]);
					this.setSynthValue("op6FixedFreq", arrVals[5]);
				},
			],
			[	"Randomise all frequencies, range: 0.1 - 100 hz",
				{
					var arrVals;
					arrVals = ({ classData.fixedFreqSpec.unmap(0.1.exprand(100.0) )} ! 6).value;
					this.setSynthValue("op1FixedFreq", arrVals[0]);
					this.setSynthValue("op2FixedFreq", arrVals[1]);
					this.setSynthValue("op3FixedFreq", arrVals[2]);
					this.setSynthValue("op4FixedFreq", arrVals[3]);
					this.setSynthValue("op5FixedFreq", arrVals[4]);
					this.setSynthValue("op6FixedFreq", arrVals[5]);
				},
			],
			[	"Randomise all frequencies, range: 0.1 - 1000 hz",
				{
					var arrVals;
					arrVals = ({ classData.fixedFreqSpec.unmap(0.1.exprand(1000.0) )} ! 6).value;
					this.setSynthValue("op1FixedFreq", arrVals[0]);
					this.setSynthValue("op2FixedFreq", arrVals[1]);
					this.setSynthValue("op3FixedFreq", arrVals[2]);
					this.setSynthValue("op4FixedFreq", arrVals[3]);
					this.setSynthValue("op5FixedFreq", arrVals[4]);
					this.setSynthValue("op6FixedFreq", arrVals[5]);
				},
			],
			[	"Randomise all frequencies, range: 0.1 - 10000 hz",
				{
					var arrVals;
					arrVals = ({ classData.fixedFreqSpec.unmap(0.1.exprand(10000.0) )} ! 6).value;
					this.setSynthValue("op1FixedFreq", arrVals[0]);
					this.setSynthValue("op2FixedFreq", arrVals[1]);
					this.setSynthValue("op3FixedFreq", arrVals[2]);
					this.setSynthValue("op4FixedFreq", arrVals[3]);
					this.setSynthValue("op5FixedFreq", arrVals[4]);
					this.setSynthValue("op6FixedFreq", arrVals[5]);
				},
			],
			[	"Randomise all frequencies to MIDI notes: C1 - C3",
				{
					var arrVals;
					arrVals = ({ classData.fixedFreqSpec.unmap((24.rrand(48)).midicps )} ! 6).value;
					this.setSynthValue("op1FixedFreq", arrVals[0]);
					this.setSynthValue("op2FixedFreq", arrVals[1]);
					this.setSynthValue("op3FixedFreq", arrVals[2]);
					this.setSynthValue("op4FixedFreq", arrVals[3]);
					this.setSynthValue("op5FixedFreq", arrVals[4]);
					this.setSynthValue("op6FixedFreq", arrVals[5]);
				},
			],
			[	"Randomise all frequencies to MIDI notes: C2 - C5",
				{
					var arrVals;
					arrVals = ({ classData.fixedFreqSpec.unmap((36.rrand(72)).midicps )} ! 6).value;
					this.setSynthValue("op1FixedFreq", arrVals[0]);
					this.setSynthValue("op2FixedFreq", arrVals[1]);
					this.setSynthValue("op3FixedFreq", arrVals[2]);
					this.setSynthValue("op4FixedFreq", arrVals[3]);
					this.setSynthValue("op5FixedFreq", arrVals[4]);
					this.setSynthValue("op6FixedFreq", arrVals[5]);
				},
			],
			[	"Randomise all frequencies to MIDI notes: C1 - C6",
				{
					var arrVals;
					arrVals = ({ classData.fixedFreqSpec.unmap((24.rrand(84)).midicps )} ! 6).value;
					this.setSynthValue("op1FixedFreq", arrVals[0]);
					this.setSynthValue("op2FixedFreq", arrVals[1]);
					this.setSynthValue("op3FixedFreq", arrVals[2]);
					this.setSynthValue("op4FixedFreq", arrVals[3]);
					this.setSynthValue("op5FixedFreq", arrVals[4]);
					this.setSynthValue("op6FixedFreq", arrVals[5]);
				},
			],
		];
		arrSynthArgSpecs = [
			["out", 0, 0],

			["freq", 0.5, defLagTime],
			["freqMin", 0.midicps, defLagTime],
			["freqMax", 127.midicps, defLagTime],
			["level", 0.5, defLagTime],
			["i_noteListTypeInd",12, \ir],
			["i_noteListRoot", 0, \ir],
			["i_noteListMin", 36, \ir],
			["i_noteListMax", 72, \ir],
			["i_noteListSize", 1, \ir],
			["freqSelector", 0.5, defLagTime],
			["lag", 0.5, defLagTime],
			["lagMin", 0.01, defLagTime],
			["lagMax", 1, defLagTime],
			["lag2", 0.5, defLagTime],
			["lag2Min", 0.01, defLagTime],
			["lag2Max", 1, defLagTime],

			["op1FreqRatio", 1, defLagTime],
			["op1FixedFreq", classData.fixedFreqSpec.unmap(60.midicps), defLagTime],
			["op1Detune", 0.5, defLagTime],
			["op1Phase", 0, defLagTime],
			["op1Amp", 0.5, defLagTime],
			["op2FreqRatio", 2, defLagTime],
			["op2FixedFreq", classData.fixedFreqSpec.unmap(60.midicps), defLagTime],
			["op2Detune", 0.5, defLagTime],
			["op2Phase", 0, defLagTime],
			["op2Amp", 0.5, defLagTime],
			["op3FreqRatio", 3, defLagTime],
			["op3FixedFreq", classData.fixedFreqSpec.unmap(60.midicps), defLagTime],
			["op3Detune", 0.5, defLagTime],
			["op3Phase", 0, defLagTime],
			["op3Amp", 0.5, defLagTime],
			["op4FreqRatio", 4, defLagTime],
			["op4FixedFreq", classData.fixedFreqSpec.unmap(60.midicps), defLagTime],
			["op4Detune", 0.5, defLagTime],
			["op4Phase", 0, defLagTime],
			["op4Amp", 0.5, defLagTime],
			["op5FreqRatio", 0.5, defLagTime],
			["op5FixedFreq", classData.fixedFreqSpec.unmap(60.midicps), defLagTime],
			["op5Detune", 0.5, defLagTime],
			["op5Phase", 0, defLagTime],
			["op5Amp", 0.5, defLagTime],
			["op6FreqRatio", 1.5, defLagTime],
			["op6FixedFreq", classData.fixedFreqSpec.unmap(60.midicps), defLagTime],
			["op6Detune", 0.5, defLagTime],
			["op6Phase", 0, defLagTime],
			["op6Amp", 0.5, defLagTime],

			["mIndFM_11", 0, defLagTime],
			["mIndFM_12", 0.5, defLagTime],
			["mIndFM_13", 0.25, defLagTime],
			["mIndFM_14", 0, defLagTime],
			["mIndFM_15", 0, defLagTime],
			["mIndFM_16", 0, defLagTime],
			["mIndFM_21", 0, defLagTime],
			["mIndFM_22", 0, defLagTime],
			["mIndFM_23", 0, defLagTime],
			["mIndFM_24", 0, defLagTime],
			["mIndFM_25", 0, defLagTime],
			["mIndFM_26", 0, defLagTime],
			["mIndFM_31", 0, defLagTime],
			["mIndFM_32", 0, defLagTime],
			["mIndFM_33", 0, defLagTime],
			["mIndFM_34", 0, defLagTime],
			["mIndFM_35", 0, defLagTime],
			["mIndFM_36", 0, defLagTime],
			["mIndFM_41", 0, defLagTime],
			["mIndFM_42", 0, defLagTime],
			["mIndFM_43", 0, defLagTime],
			["mIndFM_44", 0, defLagTime],
			["mIndFM_45", 0, defLagTime],
			["mIndFM_46", 0, defLagTime],
			["mIndFM_51", 0, defLagTime],
			["mIndFM_52", 0, defLagTime],
			["mIndFM_53", 0, defLagTime],
			["mIndFM_54", 0, defLagTime],
			["mIndFM_55", 0, defLagTime],
			["mIndFM_56", 0, defLagTime],
			["mIndFM_61", 0, defLagTime],
			["mIndFM_62", 0, defLagTime],
			["mIndFM_63", 0, defLagTime],
			["mIndFM_64", 0, defLagTime],
			["mIndFM_65", 0, defLagTime],
			["mIndFM_66", 0, defLagTime],

			["outLevelOp1", 1, defLagTime],
			["outLevelOp2", 0, defLagTime],
			["outLevelOp3", 0, defLagTime],
			["outLevelOp4", 0, defLagTime],
			["outLevelOp5", 0, defLagTime],
			["outLevelOp6", 0, defLagTime],

			["modFreq", 0, defLagTime],
			["modFreqSelector", 0, defLagTime],
			["modLag", 0, defLagTime],
			["modLag2", 0, defLagTime],

			["modOp1FreqRatio", 0, defLagTime],
			["modOp1FixedFreq", 0, defLagTime],
			["modOp1Detune", 0, defLagTime],
			["modOp1Phase", 0, defLagTime],
			["modOp1Amp", 0, defLagTime],
			["modOp2FreqRatio", 0, defLagTime],
			["modOp2FixedFreq", 0, defLagTime],
			["modOp2Detune", 0, defLagTime],
			["modOp2Phase", 0, defLagTime],
			["modOp2Amp", 0, defLagTime],
			["modOp3FreqRatio", 0, defLagTime],
			["modOp3FixedFreq", 0, defLagTime],
			["modOp3Detune", 0, defLagTime],
			["modOp3Phase", 0, defLagTime],
			["modOp3Amp", 0, defLagTime],
			["modOp4FreqRatio", 0, defLagTime],
			["modOp4FixedFreq", 0, defLagTime],
			["modOp4Detune", 0, defLagTime],
			["modOp4Phase", 0, defLagTime],
			["modOp4Amp", 0, defLagTime],
			["modOp5FreqRatio", 0, defLagTime],
			["modOp5FixedFreq", 0, defLagTime],
			["modOp5Detune", 0, defLagTime],
			["modOp5Phase", 0, defLagTime],
			["modOp5Amp", 0, defLagTime],
			["modOp6FreqRatio", 0, defLagTime],
			["modOp6FixedFreq", 0, defLagTime],
			["modOp6Detune", 0, defLagTime],
			["modOp6Phase", 0, defLagTime],
			["modOp6Amp", 0, defLagTime],
			["modMIndFM_11", 0, defLagTime],
			["modMIndFM_12", 0, defLagTime],
			["modMIndFM_13", 0, defLagTime],
			["modMIndFM_14", 0, defLagTime],
			["modMIndFM_15", 0, defLagTime],
			["modMIndFM_16", 0, defLagTime],
			["modMIndFM_21", 0, defLagTime],
			["modMIndFM_22", 0, defLagTime],
			["modMIndFM_23", 0, defLagTime],
			["modMIndFM_24", 0, defLagTime],
			["modMIndFM_25", 0, defLagTime],
			["modMIndFM_26", 0, defLagTime],
			["modMIndFM_31", 0, defLagTime],
			["modMIndFM_32", 0, defLagTime],
			["modMIndFM_33", 0, defLagTime],
			["modMIndFM_34", 0, defLagTime],
			["modMIndFM_35", 0, defLagTime],
			["modMIndFM_36", 0, defLagTime],
			["modMIndFM_41", 0, defLagTime],
			["modMIndFM_42", 0, defLagTime],
			["modMIndFM_43", 0, defLagTime],
			["modMIndFM_44", 0, defLagTime],
			["modMIndFM_45", 0, defLagTime],
			["modMIndFM_46", 0, defLagTime],
			["modMIndFM_51", 0, defLagTime],
			["modMIndFM_52", 0, defLagTime],
			["modMIndFM_53", 0, defLagTime],
			["modMIndFM_54", 0, defLagTime],
			["modMIndFM_55", 0, defLagTime],
			["modMIndFM_56", 0, defLagTime],
			["modMIndFM_61", 0, defLagTime],
			["modMIndFM_62", 0, defLagTime],
			["modMIndFM_63", 0, defLagTime],
			["modMIndFM_64", 0, defLagTime],
			["modMIndFM_65", 0, defLagTime],
			["modMIndFM_66", 0, defLagTime],
			["modOutLevelOp1", 0, defLagTime],
			["modOutLevelOp2", 0, defLagTime],
			["modOutLevelOp3", 0, defLagTime],
			["modOutLevelOp4", 0, defLagTime],
			["modOutLevelOp5", 0, defLagTime],
			["modOutLevelOp6", 0, defLagTime],

			// N.B. the args below not used in  synthdef, just stored here for convenience
			["algorithm", 0],
			["algoFeedback", 0.2],
			["ratioProcess", 0],
			["freqProcess", 0],
		];
		arrOptions = 0 ! 9;
		arrOptionData = [
			[ //0 freq function
				["Off", {arg freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector;
					//( (freqMax/freqMin) ** ((freq + modFreq).max(0.0001).min(1)) ) * freqMin;
					(freq + modFreq).max(0).min(1).linexp(0, 1, freqMin, freqMax)
				}],
				["On", {arg freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector;
					var holdArray;
					// convert to cps
					holdArray = this.getNoteArray.midicps;
					Select.kr( (((freqSelector + modFreqSelector).max(0).min(1)) * holdArray.size), holdArray);
				}],
			],
			[// 1 lag function
				["None",
					{arg input, lagtime; input;}
				],
				["Linear - use time 1 for up and down smoothing",
					{arg input, lagtime; Ramp.kr(input, lagtime); }
				],
				["Exponential 1 - use time 1 for up and down smoothing",
					{arg input, lagtime; Lag.kr(input, lagtime); }
				],
				["Exponential 2 - use time 1 for up and down smoothing",
					{arg input, lagtime; Lag2.kr(input, lagtime); }
				],
				["Exponential 3 - use time 1 for up and down smoothing",
					{arg input, lagtime; Lag3.kr(input, lagtime); }
				],
				["Exponential 1 - use time 1 for up, time 2 for down smoothing",
					{arg input, lagtime, lagtime2; LagUD.kr(input, lagtime, lagtime2); }
				],
				["Exponential 2 - use time 1 for up, time 2 for down smoothing",
					{arg input, lagtime, lagtime2; Lag2UD.kr(input, lagtime, lagtime2); }
				],
				["Exponential 3 - use time 1 for up, time 2 for down smoothing",
					{arg input, lagtime, lagtime2; Lag3UD.kr(input, lagtime, lagtime2); }
				],
			],
			// 2 amp compensation
			TXAmpComp.arrOptionData,
			// 3 - Op1 use fixed freq
			classData.fixedFreqOptionData,
			// 4 - Op2 use fixed freq
			classData.fixedFreqOptionData,
			// 5 - Op3 use fixed freq
			classData.fixedFreqOptionData,
			// 6 - Op4 use fixed freq
			classData.fixedFreqOptionData,
			// 7 - Op5 use fixed freq
			classData.fixedFreqOptionData,
			// 8 - Op6 use fixed freq
			classData.fixedFreqOptionData,
		];
		synthDefFunc = {
			arg out,
			freq, freqMin, freqMax,
			level, i_noteListTypeInd, i_noteListRoot, i_noteListMin,  i_noteListMax, i_noteListSize, freqSelector,
			lag, lagMin, lagMax, lag2, lag2Min, lag2Max,

			op1FreqRatio, op1FixedFreq, op1Detune, op1Phase, op1Amp,
			op2FreqRatio, op2FixedFreq, op2Detune, op2Phase, op2Amp,
			op3FreqRatio, op3FixedFreq, op3Detune, op3Phase, op3Amp,
			op4FreqRatio, op4FixedFreq, op4Detune, op4Phase, op4Amp,
			op5FreqRatio, op5FixedFreq, op5Detune, op5Phase, op5Amp,
			op6FreqRatio, op6FixedFreq, op6Detune, op6Phase, op6Amp,

			mIndFM_11, mIndFM_12, mIndFM_13, mIndFM_14, mIndFM_15, mIndFM_16,
			mIndFM_21, mIndFM_22, mIndFM_23, mIndFM_24, mIndFM_25, mIndFM_26,
			mIndFM_31, mIndFM_32, mIndFM_33, mIndFM_34, mIndFM_35, mIndFM_36,
			mIndFM_41, mIndFM_42, mIndFM_43, mIndFM_44, mIndFM_45, mIndFM_46,
			mIndFM_51, mIndFM_52, mIndFM_53, mIndFM_54, mIndFM_55, mIndFM_56,
			mIndFM_61, mIndFM_62, mIndFM_63, mIndFM_64, mIndFM_65, mIndFM_66,

			outLevelOp1, outLevelOp2, outLevelOp3, outLevelOp4, outLevelOp5, outLevelOp6,

			modFreq = 0, modFreqSelector = 0, modLag = 0, modLag2 = 0,

			modOp1FreqRatio=0, modOp1FixedFreq=0, modOp1Detune=0, modOp1Phase=0, modOp1Amp=0,
			modOp2FreqRatio=0, modOp2FixedFreq=0, modOp2Detune=0, modOp2Phase=0, modOp2Amp=0,
			modOp3FreqRatio=0, modOp3FixedFreq=0, modOp3Detune=0, modOp3Phase=0, modOp3Amp=0,
			modOp4FreqRatio=0, modOp4FixedFreq=0, modOp4Detune=0, modOp4Phase=0, modOp4Amp=0,
			modOp5FreqRatio=0, modOp5FixedFreq=0, modOp5Detune=0, modOp5Phase=0, modOp5Amp=0,
			modOp6FreqRatio=0, modOp6FixedFreq=0, modOp6Detune=0, modOp6Phase=0, modOp6Amp=0,

			modMIndFM_11=0, modMIndFM_12=0, modMIndFM_13=0, modMIndFM_14=0, modMIndFM_15=0, modMIndFM_16=0,
			modMIndFM_21=0, modMIndFM_22=0, modMIndFM_23=0, modMIndFM_24=0, modMIndFM_25=0, modMIndFM_26=0,
			modMIndFM_31=0, modMIndFM_32=0, modMIndFM_33=0, modMIndFM_34=0, modMIndFM_35=0, modMIndFM_36=0,
			modMIndFM_41=0, modMIndFM_42=0, modMIndFM_43=0, modMIndFM_44=0, modMIndFM_45=0, modMIndFM_46=0,
			modMIndFM_51=0, modMIndFM_52=0, modMIndFM_53=0, modMIndFM_54=0, modMIndFM_55=0, modMIndFM_56=0,
			modMIndFM_61=0, modMIndFM_62=0, modMIndFM_63=0, modMIndFM_64=0, modMIndFM_65=0, modMIndFM_66=0,

			modOutLevelOp1=0, modOutLevelOp2=0, modOutLevelOp3=0,  modOutLevelOp4=0, modOutLevelOp5=0, modOutLevelOp6=0;

			var arrFMCtls, arrFMIndices, arrFMLevels, outFM, outSound;
			var op1FreqFunc, op2FreqFunc, op3FreqFunc, op4FreqFunc, op5FreqFunc, op6FreqFunc;
			var op1Det, op2Det, op3Det, op4Det, op5Det, op6Det;
			var outFreqFunc, outFreq, outFreqLag, lagtime, lagtime2;
			var outLagFunction, outVol, ampCompFunction, outAmpComp;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			outFreqFunc = this.getSynthOption(0);
			outFreq = outFreqFunc.value(freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector);
			lagtime = ( (lagMax/lagMin) ** ((lag + modLag).max(0.001).min(1)) ) * lagMin;
			lagtime2 = ( (lag2Max/lag2Min) ** ((lag2 + modLag2).max(0.001).min(1)) ) * lag2Min;
			outLagFunction = this.getSynthOption(1);
			outFreqLag = outLagFunction.value(outFreq, lagtime, lagtime2);

			op1FreqFunc  = this.getSynthOption(3);
			op2FreqFunc  = this.getSynthOption(4);
			op3FreqFunc  = this.getSynthOption(5);
			op4FreqFunc  = this.getSynthOption(6);
			op5FreqFunc  = this.getSynthOption(7);
			op6FreqFunc  = this.getSynthOption(8);
			// detune ratios range roughly -1 to +1 semitones
			// op1Det = (op1Detune + modOp1Detune).linlin(0, 1, 0.94, 1.06);
			// op2Det = (op2Detune + modOp2Detune).linlin(0, 1, 0.94, 1.06);
			// op3Det = (op3Detune + modOp3Detune).linlin(0, 1, 0.94, 1.06);
			// op4Det = (op4Detune + modOp4Detune).linlin(0, 1, 0.94, 1.06);
			// op5Det = (op5Detune + modOp5Detune).linlin(0, 1, 0.94, 1.06);
			// op6Det = (op5Detune + modOp5Detune).linlin(0, 1, 0.94, 1.06);
			op1Det = ((op1Detune + modOp1Detune).madd(2, -1).clip(-1,1).squared * 0.06) + 1;
			op2Det = ((op2Detune + modOp2Detune).madd(2, -1).clip(-1,1).squared * 0.06) + 1;
			op3Det = ((op3Detune + modOp3Detune).madd(2, -1).clip(-1,1).squared * 0.06) + 1;
			op4Det = ((op4Detune + modOp4Detune).madd(2, -1).clip(-1,1).squared * 0.06) + 1;
			op5Det = ((op5Detune + modOp5Detune).madd(2, -1).clip(-1,1).squared * 0.06) + 1;
			op6Det = ((op5Detune + modOp5Detune).madd(2, -1).clip(-1,1).squared * 0.06) + 1;

			arrFMCtls = [
				[ op1Det * op1FreqFunc.value(op1FreqRatio, modOp1FreqRatio, op1FixedFreq, modOp1FixedFreq, outFreq),
					(modOp1Phase + op1Phase).max(0).min(1) * 2pi,(modOp1Amp + op1Amp).max(0).min(1),],
				[ op2Det * op2FreqFunc.value(op2FreqRatio, modOp2FreqRatio, op2FixedFreq, modOp2FixedFreq, outFreq),
					(modOp2Phase + op2Phase).max(0).min(1) * 2pi,(modOp2Amp + op2Amp).max(0).min(1),],
				[ op3Det * op3FreqFunc.value(op3FreqRatio, modOp3FreqRatio, op3FixedFreq, modOp3FixedFreq, outFreq),
					(modOp3Phase + op3Phase).max(0).min(1) * 2pi,(modOp3Amp + op3Amp).max(0).min(1),],
				[ op4Det * op4FreqFunc.value(op4FreqRatio, modOp4FreqRatio, op4FixedFreq, modOp4FixedFreq, outFreq),
					(modOp4Phase + op4Phase).max(0).min(1) * 2pi,(modOp4Amp + op4Amp).max(0).min(1),],
				[ op5Det * op5FreqFunc.value(op5FreqRatio, modOp5FreqRatio, op5FixedFreq, modOp5FixedFreq, outFreq),
					(modOp5Phase + op5Phase).max(0).min(1) * 2pi,(modOp5Amp + op5Amp).max(0).min(1),],
				[ op6Det * op6FreqFunc.value(op6FreqRatio, modOp6FreqRatio, op6FixedFreq, modOp6FixedFreq, outFreq),
					(modOp6Phase + op6Phase).max(0).min(1) * 2pi,(modOp6Amp + op6Amp).max(0).min(1),],
			];
			arrFMIndices = [
				[(mIndFM_11 + modMIndFM_11).max(0).min(1), (mIndFM_12 + modMIndFM_12).max(0).min(1),
					(mIndFM_13 + modMIndFM_13).max(0).min(1), (mIndFM_14 + modMIndFM_14).max(0).min(1),
					(mIndFM_15 + modMIndFM_15).max(0).min(1), (mIndFM_16 + modMIndFM_16).max(0).min(1), ],
				[(mIndFM_21 + modMIndFM_21).max(0).min(1), (mIndFM_22 + modMIndFM_22).max(0).min(1),
					(mIndFM_23 + modMIndFM_23).max(0).min(1), (mIndFM_24 + modMIndFM_24).max(0).min(1),
					(mIndFM_25 + modMIndFM_25).max(0).min(1), (mIndFM_26 + modMIndFM_26).max(0).min(1), ],
				[(mIndFM_31 + modMIndFM_31).max(0).min(1), (mIndFM_32 + modMIndFM_32).max(0).min(1),
					(mIndFM_33 + modMIndFM_33).max(0).min(1), (mIndFM_34 + modMIndFM_34).max(0).min(1),
					(mIndFM_35 + modMIndFM_35).max(0).min(1), (mIndFM_36 + modMIndFM_36).max(0).min(1), ],
				[(mIndFM_41 + modMIndFM_41).max(0).min(1), (mIndFM_42 + modMIndFM_42).max(0).min(1),
					(mIndFM_43 + modMIndFM_43).max(0).min(1), (mIndFM_44 + modMIndFM_44).max(0).min(1),
					(mIndFM_45 + modMIndFM_45).max(0).min(1), (mIndFM_46 + modMIndFM_46).max(0).min(1), ],
				[(mIndFM_51 + modMIndFM_51).max(0).min(1), (mIndFM_52 + modMIndFM_52).max(0).min(1),
					(mIndFM_53 + modMIndFM_53).max(0).min(1), (mIndFM_54 + modMIndFM_54).max(0).min(1),
					(mIndFM_55 + modMIndFM_55).max(0).min(1), (mIndFM_56 + modMIndFM_56).max(0).min(1), ],
				[(mIndFM_61 + modMIndFM_61).max(0).min(1), (mIndFM_62 + modMIndFM_62).max(0).min(1),
					(mIndFM_63 + modMIndFM_63).max(0).min(1), (mIndFM_64 + modMIndFM_64).max(0).min(1),
					(mIndFM_65 + modMIndFM_65).max(0).min(1), (mIndFM_66 + modMIndFM_66).max(0).min(1), ],
			]
			* 2pi;	// phase modulation is in radians so: * 2pi

			arrFMLevels = [(outLevelOp1 + modOutLevelOp1).max(0).min(1), (outLevelOp2 + modOutLevelOp2).max(0).min(1),
				(outLevelOp3 + modOutLevelOp3).max(0).min(1), (outLevelOp4 + modOutLevelOp4).max(0).min(1),
				(outLevelOp5 + modOutLevelOp5).max(0).min(1), (outLevelOp6 + modOutLevelOp6).max(0).min(1),
			];

			outFM = Mix.new(FM7.ar(arrFMCtls, arrFMIndices) * arrFMLevels);
			ampCompFunction = this.getSynthOption(2);
			outAmpComp = ampCompFunction.value(outFreqLag);
			outSound = startEnv * outFM * outAmpComp * level;
			Out.ar(out, TXClean.ar(LeakDC.ar(outSound)));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["SynthOptionCheckBox", "Op 1 Use Fixed Freq", arrOptionData, 16, 160],
			["TXFractionSlider", "Op 1 Freq ratio", classData.ratioSpec, "op1FreqRatio", nil, 300],
			["EZsliderUnmapped", "Op 1 Fixed freq", classData.fixedFreqSpec, "op1FixedFreq", nil, 456],
			["EZsliderUnmapped", "Op 1 Detune", \bipolar.asSpec, "op1Detune"],
			["EZslider", "Op 1 Phase", \unipolar.asSpec, "op1Phase"],
			["EZslider", "Op 1 Amplitude", \unipolar.asSpec, "op1Amp"],
			["SpacerLine", 4],
			["EZslider", "Op 1 Mod Op 1", \unipolar.asSpec, "mIndFM_11"],
			["EZslider", "Op 1 Mod Op 2", \unipolar.asSpec, "mIndFM_12"],
			["EZslider", "Op 1 Mod Op 3", \unipolar.asSpec, "mIndFM_13"],
			["EZslider", "Op 1 Mod Op 4", \unipolar.asSpec, "mIndFM_14"],
			["EZslider", "Op 1 Mod Op 5", \unipolar.asSpec, "mIndFM_15"],
			["EZslider", "Op 1 Mod Op 6", \unipolar.asSpec, "mIndFM_16"],
			["EZslider", "Op 1 Level", \unipolar.asSpec, "outLevelOp1"],
			["SynthOptionCheckBox", "Op 2 Use Fixed Freq", arrOptionData, 16, 160],
			["TXFractionSlider", "Op 2 Freq ratio", classData.ratioSpec, "op2FreqRatio", nil, 300],
			["EZsliderUnmapped", "Op 2 Fixed freq", classData.fixedFreqSpec, "op2FixedFreq", nil, 456],
			["EZsliderUnmapped", "Op 2 Detune", \bipolar.asSpec, "op2Detune"],
			["EZslider", "Op 2 Phase", \unipolar.asSpec, "op2Phase"],
			["EZslider", "Op 2 Amplitude", \unipolar.asSpec, "op2Amp"],
			["SpacerLine", 4],
			["EZslider", "Op 2 Mod Op 1", \unipolar.asSpec, "mIndFM_21"],
			["EZslider", "Op 2 Mod Op 2", \unipolar.asSpec, "mIndFM_22"],
			["EZslider", "Op 2 Mod Op 3", \unipolar.asSpec, "mIndFM_23"],
			["EZslider", "Op 2 Mod Op 4", \unipolar.asSpec, "mIndFM_24"],
			["EZslider", "Op 2 Mod Op 5", \unipolar.asSpec, "mIndFM_25"],
			["EZslider", "Op 2 Mod Op 6", \unipolar.asSpec, "mIndFM_26"],
			["EZslider", "Op 2 Level", \unipolar.asSpec, "outLevelOp2"],
			["SynthOptionCheckBox", "Op 3 Use Fixed Freq", arrOptionData, 16, 160],
			["TXFractionSlider", "Op 3 Freq ratio", classData.ratioSpec, "op3FreqRatio", nil, 300],
			["EZsliderUnmapped", "Op 3 Fixed freq", classData.fixedFreqSpec, "op3FixedFreq", nil, 456],
			["EZsliderUnmapped", "Op 3 Detune", \bipolar.asSpec, "op3Detune"],
			["EZslider", "Op 3 Phase", \unipolar.asSpec, "op3Phase"],
			["EZslider", "Op 3 Amplitude", \unipolar.asSpec, "op3Amp"],
			["SpacerLine", 4],
			["EZslider", "Op 3 Mod Op 1", \unipolar.asSpec, "mIndFM_31"],
			["EZslider", "Op 3 Mod Op 2", \unipolar.asSpec, "mIndFM_32"],
			["EZslider", "Op 3 Mod Op 3", \unipolar.asSpec, "mIndFM_33"],
			["EZslider", "Op 3 Mod Op 4", \unipolar.asSpec, "mIndFM_34"],
			["EZslider", "Op 3 Mod Op 5", \unipolar.asSpec, "mIndFM_35"],
			["EZslider", "Op 3 Mod Op 6", \unipolar.asSpec, "mIndFM_36"],
			["EZslider", "Op 3 Level", \unipolar.asSpec, "outLevelOp3"],
			["SynthOptionCheckBox", "Op 4 Use Fixed Freq", arrOptionData, 16, 160],
			["TXFractionSlider", "Op 4 Freq ratio", classData.ratioSpec, "op4FreqRatio", nil, 300],
			["EZsliderUnmapped", "Op 4 Fixed freq", classData.fixedFreqSpec, "op4FixedFreq", nil, 456],
			["EZsliderUnmapped", "Op 4 Detune", \bipolar.asSpec, "op4Detune"],
			["EZslider", "Op 4 Phase", \unipolar.asSpec, "op4Phase"],
			["EZslider", "Op 4 Amplitude", \unipolar.asSpec, "op4Amp"],
			["SpacerLine", 4],
			["EZslider", "Op 4 Mod Op 1", \unipolar.asSpec, "mIndFM_41"],
			["EZslider", "Op 4 Mod Op 2", \unipolar.asSpec, "mIndFM_42"],
			["EZslider", "Op 4 Mod Op 3", \unipolar.asSpec, "mIndFM_43"],
			["EZslider", "Op 4 Mod Op 4", \unipolar.asSpec, "mIndFM_44"],
			["EZslider", "Op 4 Mod Op 5", \unipolar.asSpec, "mIndFM_45"],
			["EZslider", "Op 4 Mod Op 6", \unipolar.asSpec, "mIndFM_46"],
			["EZslider", "Op 4 Level", \unipolar.asSpec, "outLevelOp4"],
			["SynthOptionCheckBox", "Op 5 Use Fixed Freq", arrOptionData, 16, 160],
			["TXFractionSlider", "Op 5 Freq ratio", classData.ratioSpec, "op5FreqRatio", nil, 300],
			["EZsliderUnmapped", "Op 5 Fixed freq", classData.fixedFreqSpec, "op5FixedFreq", nil, 456],
			["EZsliderUnmapped", "Op 5 Detune", \bipolar.asSpec, "op5Detune"],
			["EZslider", "Op 5 Phase", \unipolar.asSpec, "op5Phase"],
			["EZslider", "Op 5 Amplitude", \unipolar.asSpec, "op5Amp"],
			["SpacerLine", 4],
			["EZslider", "Op 5 Mod Op 1", \unipolar.asSpec, "mIndFM_51"],
			["EZslider", "Op 5 Mod Op 2", \unipolar.asSpec, "mIndFM_52"],
			["EZslider", "Op 5 Mod Op 3", \unipolar.asSpec, "mIndFM_53"],
			["EZslider", "Op 5 Mod Op 4", \unipolar.asSpec, "mIndFM_54"],
			["EZslider", "Op 5 Mod Op 5", \unipolar.asSpec, "mIndFM_55"],
			["EZslider", "Op 5 Mod Op 6", \unipolar.asSpec, "mIndFM_56"],
			["EZslider", "Op 5 Level", \unipolar.asSpec, "outLevelOp5"],
			["SynthOptionCheckBox", "Op 6 Use Fixed Freq", arrOptionData, 16, 160],
			["TXFractionSlider", "Op 6 Freq ratio", classData.ratioSpec, "op6FreqRatio", nil, 300],
			["EZsliderUnmapped", "Op 6 Fixed freq", classData.fixedFreqSpec, "op6FixedFreq", nil, 456],
			["EZsliderUnmapped", "Op 6 Detune", \bipolar.asSpec, "op6Detune"],
			["EZslider", "Op 6 Phase", \unipolar.asSpec, "op6Phase"],
			["EZslider", "Op 6 Amplitude", \unipolar.asSpec, "op6Amp"],
			["SpacerLine", 4],
			["EZslider", "Op 6 Mod Op 1", \unipolar.asSpec, "mIndFM_61"],
			["EZslider", "Op 6 Mod Op 2", \unipolar.asSpec, "mIndFM_62"],
			["EZslider", "Op 6 Mod Op 3", \unipolar.asSpec, "mIndFM_63"],
			["EZslider", "Op 6 Mod Op 4", \unipolar.asSpec, "mIndFM_64"],
			["EZslider", "Op 6 Mod Op 5", \unipolar.asSpec, "mIndFM_65"],
			["EZslider", "Op 6 Mod Op 6", \unipolar.asSpec, "mIndFM_66"],
			["EZslider", "Op 6 Level", \unipolar.asSpec, "outLevelOp6"],
			["EZslider", "Main level", \unipolar.asSpec, "level"],
			["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 2],

			["TXMinMaxFreqNoteSldr", "Base Freq", ControlSpec(0.midicps, 20000, \exponential),
			"freq", "freqMin", "freqMax", nil, TXFreqRangePresets.arrFreqRanges],
			["SynthOptionCheckBox", "Use note list instead of variable frequency", arrOptionData, 0, 400],
			["EZslider", "Note select", \unipolar.asSpec, "freqSelector"],
			["TXPopupAction", "Chord/ scale", TXScale.arrScaleNames, "i_noteListTypeInd", {this.updateSynth;}, 400],
			["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
			"i_noteListRoot", {this.updateSynth;}, 140],
			["TXNoteRangeSlider", "Note range", "i_noteListMin", "i_noteListMax", {this.updateSynth;}, true],
			["TXStaticText", "Note count", {this.getNoteTotalText}, {arg view; noteListTextView = view;}],
			["SynthOptionPopupPlusMinus", "Smoothing", arrOptionData, 1],
			["TXMinMaxSliderSplit", "Time 1", classData.lagSpec, "lag", "lagMin", "lagMax"],
			["TXMinMaxSliderSplit", "Time 2", classData.lagSpec, "lag2", "lag2Min", "lag2Max"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
		this.getNoteArray; // initialise
	}

	buildGuiSpecArray {
		var midiNoteNames = ["notes"] ++ 103.collect({arg item; TXGetMidiNoteString(item + 24);});
		guiSpecArray = [
			["ActionButton", "Operators", {displayOption = "showOperators";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showOperators")],
			["Spacer", 3],
			["ActionButton", "Base Frequency", {displayOption = "showFreq";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showFreq")],
			["Spacer", 3],
			["ActionButton", "Base Freq Smooth", {displayOption = "showSmoothing";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showSmoothing")],
			["DividingLine"],
			["SpacerLine", 2],
		];

		if (displayOption == "showOperators", {
			guiSpecArray = guiSpecArray ++[
				["ActionButton", "All 1", {displayOperator = "showAllOps";
					this.buildGuiSpecArray; system.showView;}, 38,
				TXColor.white, this.getButtonColour(displayOperator == "showAllOps")],
				["ActionButton", "All 2", {displayOperator = "showAllOps2";
					this.buildGuiSpecArray; system.showView;}, 38,
				TXColor.white, this.getButtonColour(displayOperator == "showAllOps2")],
				["ActionButton", "Op 1", {displayOperator = "showOp1";
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayOperator == "showOp1")],
				["ActionButton", "Op 2", {displayOperator = "showOp2";
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayOperator == "showOp2")],
				["ActionButton", "Op 3", {displayOperator = "showOp3";
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayOperator == "showOp3")],
				["ActionButton", "Op 4", {displayOperator = "showOp4";
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayOperator == "showOp4")],
				["ActionButton", "Op 5", {displayOperator = "showOp5";
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayOperator == "showOp5")],
				["ActionButton", "Op 6", {displayOperator = "showOp6";
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayOperator == "showOp6")],
				["ActionButton", "Ratios", {displayOperator = "showAllOpsRatio";
					this.buildGuiSpecArray; system.showView;}, 38,
				TXColor.white, this.getButtonColour(displayOperator == "showAllOpsRatio")],
				["ActionButton", "Freqs", {displayOperator = "showAllOpsFFreq";
					this.buildGuiSpecArray; system.showView;}, 38,
				TXColor.white, this.getButtonColour(displayOperator == "showAllOpsFFreq")],
				["SpacerLine", 1],
			];

			if (displayOperator == "showOp1", {
				guiSpecArray = guiSpecArray ++[
					["TextBar", "Use fixed freq", 80, nil, nil, nil, \right],
					["SynthOptionCheckBox", "Use Fixed Freq", arrOptionData, 3, 160],
					["NextLine"],
					["TXFractionSlider", "Freq ratio", classData.ratioSpec, "op1FreqRatio", nil, 300],
					["EZsliderUnmapped", "Fixed freq", classData.fixedFreqSpec, "op1FixedFreq", nil, 456],
					["ActionPopup", midiNoteNames, {arg view; if (view.value > 0, {
						this.setSynthValue("op1FixedFreq", classData.fixedFreqSpec.unmap((view.value + 23).midicps));
						system.showView;
					});}, 60],
					["EZsliderUnmapped", "Detune", \bipolar.asSpec, "op1Detune"],
					["EZslider", "Phase", \unipolar.asSpec, "op1Phase"],
					["EZslider", "Amplitude", \unipolar.asSpec, "op1Amp"],
					["SpacerLine", 2],
					["EZslider", "Mod Op 1", \unipolar.asSpec, "mIndFM_11"],
					["EZslider", "Mod Op 2", \unipolar.asSpec, "mIndFM_12"],
					["EZslider", "Mod Op 3", \unipolar.asSpec, "mIndFM_13"],
					["EZslider", "Mod Op 4", \unipolar.asSpec, "mIndFM_14"],
					["EZslider", "Mod Op 5", \unipolar.asSpec, "mIndFM_15"],
					["EZslider", "Mod Op 6", \unipolar.asSpec, "mIndFM_16"],
					["SpacerLine", 2],
					["EZslider", "Op Level", \unipolar.asSpec, "outLevelOp1"],
				];
			});
			if (displayOperator == "showOp2", {
				guiSpecArray = guiSpecArray ++[
					["TextBar", "Use fixed freq", 80, nil, nil, nil, \right],
					["SynthOptionCheckBox", "Use Fixed Freq", arrOptionData, 4, 160],
					["NextLine"],
					["TXFractionSlider", "Freq ratio", classData.ratioSpec, "op2FreqRatio", nil, 300],
					["EZsliderUnmapped", "Fixed freq", classData.fixedFreqSpec, "op2FixedFreq", nil, 456],
					["ActionPopup", midiNoteNames, {arg view; if (view.value > 0, {
						this.setSynthValue("op2FixedFreq", classData.fixedFreqSpec.unmap((view.value + 23).midicps));
						system.showView;
					});}, 60],
					["EZsliderUnmapped", "Detune", \bipolar.asSpec, "op2Detune"],
					["EZslider", "Phase", \unipolar.asSpec, "op2Phase"],
					["EZslider", "Amplitude", \unipolar.asSpec, "op2Amp"],
					["SpacerLine", 2],
					["EZslider", "Mod Op 1", \unipolar.asSpec, "mIndFM_21"],
					["EZslider", "Mod Op 2", \unipolar.asSpec, "mIndFM_22"],
					["EZslider", "Mod Op 3", \unipolar.asSpec, "mIndFM_23"],
					["EZslider", "Mod Op 4", \unipolar.asSpec, "mIndFM_24"],
					["EZslider", "Mod Op 5", \unipolar.asSpec, "mIndFM_25"],
					["EZslider", "Mod Op 6", \unipolar.asSpec, "mIndFM_26"],
					["SpacerLine", 2],
					["EZslider", "Op Level", \unipolar.asSpec, "outLevelOp2"],
				];
			});
			if (displayOperator == "showOp3", {
				guiSpecArray = guiSpecArray ++[
					["TextBar", "Use fixed freq", 80, nil, nil, nil, \right],
					["SynthOptionCheckBox", "Use Fixed Freq", arrOptionData, 5, 160],
					["NextLine"],
					["TXFractionSlider", "Freq ratio", classData.ratioSpec, "op3FreqRatio", nil, 300],
					["EZsliderUnmapped", "Fixed freq", classData.fixedFreqSpec, "op3FixedFreq", nil, 456],
					["ActionPopup", midiNoteNames, {arg view; if (view.value > 0, {
						this.setSynthValue("op3FixedFreq", classData.fixedFreqSpec.unmap((view.value + 23).midicps));
						system.showView;
					});}, 60],
					["EZsliderUnmapped", "Detune", \bipolar.asSpec, "op3Detune"],
					["EZslider", "Phase", \unipolar.asSpec, "op3Phase"],
					["EZslider", "Amplitude", \unipolar.asSpec, "op3Amp"],
					["SpacerLine", 2],
					["EZslider", "Mod Op 1", \unipolar.asSpec, "mIndFM_31"],
					["EZslider", "Mod Op 2", \unipolar.asSpec, "mIndFM_32"],
					["EZslider", "Mod Op 3", \unipolar.asSpec, "mIndFM_33"],
					["EZslider", "Mod Op 4", \unipolar.asSpec, "mIndFM_34"],
					["EZslider", "Mod Op 5", \unipolar.asSpec, "mIndFM_35"],
					["EZslider", "Mod Op 6", \unipolar.asSpec, "mIndFM_36"],
					["SpacerLine", 2],
					["EZslider", "Op Level", \unipolar.asSpec, "outLevelOp3"],
				];
			});
			if (displayOperator == "showOp4", {
				guiSpecArray = guiSpecArray ++[
					["TextBar", "Use fixed freq", 80, nil, nil, nil, \right],
					["SynthOptionCheckBox", "Use Fixed Freq", arrOptionData, 6, 160],
					["NextLine"],
					["TXFractionSlider", "Freq ratio", classData.ratioSpec, "op4FreqRatio", nil, 300],
					["EZsliderUnmapped", "Fixed freq", classData.fixedFreqSpec, "op4FixedFreq", nil, 456],
					["ActionPopup", midiNoteNames, {arg view; if (view.value > 0, {
						this.setSynthValue("op4FixedFreq", classData.fixedFreqSpec.unmap((view.value + 23).midicps));
						system.showView;
					});}, 60],
					["EZsliderUnmapped", "Detune", \bipolar.asSpec, "op4Detune"],
					["EZslider", "Phase", \unipolar.asSpec, "op4Phase"],
					["EZslider", "Amplitude", \unipolar.asSpec, "op4Amp"],
					["SpacerLine", 2],
					["EZslider", "Mod Op 1", \unipolar.asSpec, "mIndFM_41"],
					["EZslider", "Mod Op 2", \unipolar.asSpec, "mIndFM_42"],
					["EZslider", "Mod Op 3", \unipolar.asSpec, "mIndFM_43"],
					["EZslider", "Mod Op 4", \unipolar.asSpec, "mIndFM_44"],
					["EZslider", "Mod Op 5", \unipolar.asSpec, "mIndFM_45"],
					["EZslider", "Mod Op 6", \unipolar.asSpec, "mIndFM_46"],
					["SpacerLine", 2],
					["EZslider", "Op Level", \unipolar.asSpec, "outLevelOp4"],
				];
			});
			if (displayOperator == "showOp5", {
				guiSpecArray = guiSpecArray ++[
					["TextBar", "Use fixed freq", 80, nil, nil, nil, \right],
					["SynthOptionCheckBox", "Use Fixed Freq", arrOptionData, 7, 160],
					["NextLine"],
					["TXFractionSlider", "Freq ratio", classData.ratioSpec, "op5FreqRatio", nil, 300],
					["EZsliderUnmapped", "Fixed freq", classData.fixedFreqSpec, "op5FixedFreq", nil, 456],
					["ActionPopup", midiNoteNames, {arg view; if (view.value > 0, {
						this.setSynthValue("op5FixedFreq", classData.fixedFreqSpec.unmap((view.value + 23).midicps));
						system.showView;
					});}, 60],
					["EZsliderUnmapped", "Detune", \bipolar.asSpec, "op5Detune"],
					["EZslider", "Phase", \unipolar.asSpec, "op5Phase"],
					["EZslider", "Amplitude", \unipolar.asSpec, "op5Amp"],
					["SpacerLine", 2],
					["EZslider", "Mod Op 1", \unipolar.asSpec, "mIndFM_51"],
					["EZslider", "Mod Op 2", \unipolar.asSpec, "mIndFM_52"],
					["EZslider", "Mod Op 3", \unipolar.asSpec, "mIndFM_53"],
					["EZslider", "Mod Op 4", \unipolar.asSpec, "mIndFM_54"],
					["EZslider", "Mod Op 5", \unipolar.asSpec, "mIndFM_55"],
					["EZslider", "Mod Op 6", \unipolar.asSpec, "mIndFM_56"],
					["SpacerLine", 2],
					["EZslider", "Op Level", \unipolar.asSpec, "outLevelOp5"],
				];
			});
			if (displayOperator == "showOp6", {
				guiSpecArray = guiSpecArray ++[
					["TextBar", "Use fixed freq", 80, nil, nil, nil, \right],
					["SynthOptionCheckBox", "Use Fixed Freq", arrOptionData, 8, 160],
					["NextLine"],
					["TXFractionSlider", "Freq ratio", classData.ratioSpec, "op6FreqRatio", nil, 300],
					["EZsliderUnmapped", "Fixed freq", classData.fixedFreqSpec, "op6FixedFreq", nil, 456],
					["ActionPopup", midiNoteNames, {arg view; if (view.value > 0, {
						this.setSynthValue("op6FixedFreq", classData.fixedFreqSpec.unmap((view.value + 23).midicps));
						system.showView;
					});}, 60],
					["EZsliderUnmapped", "Detune", \bipolar.asSpec, "op6Detune"],
					["EZslider", "Phase", \unipolar.asSpec, "op6Phase"],
					["EZslider", "Amplitude", \unipolar.asSpec, "op6Amp"],
					["SpacerLine", 2],
					["EZslider", "Mod Op 1", \unipolar.asSpec, "mIndFM_61"],
					["EZslider", "Mod Op 2", \unipolar.asSpec, "mIndFM_62"],
					["EZslider", "Mod Op 3", \unipolar.asSpec, "mIndFM_63"],
					["EZslider", "Mod Op 4", \unipolar.asSpec, "mIndFM_64"],
					["EZslider", "Mod Op 5", \unipolar.asSpec, "mIndFM_65"],
					["EZslider", "Mod Op 6", \unipolar.asSpec, "mIndFM_66"],
					["SpacerLine", 2],
					["EZslider", "Op Level", \unipolar.asSpec, "outLevelOp6"],
				];
			});
			if (displayOperator == "showAllOps", {
				guiSpecArray = guiSpecArray ++[
					["TextBar", "Use fixed freq", 80, nil, nil, nil, \right],
					["SynthOptionCheckBox", "", arrOptionData, 3, 60],
					["SynthOptionCheckBox", "", arrOptionData, 4, 60],
					["SynthOptionCheckBox", "", arrOptionData, 5, 60],
					["SynthOptionCheckBox", "", arrOptionData, 6, 60],
					["SynthOptionCheckBox", "", arrOptionData, 7, 60],
					["SynthOptionCheckBox", "", arrOptionData, 8, 60],
					["ActionPopup", ["actions...", "all off", "all on", "random", ], {arg view;
						case { view.value == 1 }   {
							arrOptions[3] = 0;
							arrOptions[4] = 0;
							arrOptions[5] = 0;
							arrOptions[6] = 0;
							arrOptions[7] = 0;
							arrOptions[8] = 0;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 2 }   {
							arrOptions[3] = 1;
							arrOptions[4] = 1;
							arrOptions[5] = 1;
							arrOptions[6] = 1;
							arrOptions[7] = 1;
							arrOptions[8] = 1;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 3 }   {
							arrOptions[3] = [0,1].choose;
							arrOptions[4] = [0,1].choose;
							arrOptions[5] = [0,1].choose;
							arrOptions[6] = [0,1].choose;
							arrOptions[7] = [0,1].choose;
							arrOptions[8] = [0,1].choose;
							this.rebuildSynth;
							system.showView;
						};
					}, 80],
					["NextLine"],
					["EZNumber", "Freq ratio", classData.ratioSpec, "op1FreqRatio", nil, nil, nil, 0.1],
					["TXScrollNumBox", classData.ratioSpec, "op2FreqRatio", nil, 60, 0.1],
					["TXScrollNumBox", classData.ratioSpec, "op3FreqRatio", nil, 60, 0.1],
					["TXScrollNumBox", classData.ratioSpec, "op4FreqRatio", nil, 60, 0.1],
					["TXScrollNumBox", classData.ratioSpec, "op5FreqRatio", nil, 60, 0.1],
					["TXScrollNumBox", classData.ratioSpec, "op6FreqRatio", nil, 60, 0.1],
					["NextLine"],
					["TextBar", "Fixed freq", 80, nil, nil, nil, \right],
					["TXScrollNumBoxUnmapped", classData.fixedFreqSpec, "op1FixedFreq", nil, 60, 0.1],
					["TXScrollNumBoxUnmapped", classData.fixedFreqSpec, "op2FixedFreq", nil, 60, 0.1],
					["TXScrollNumBoxUnmapped", classData.fixedFreqSpec, "op3FixedFreq", nil, 60, 0.1],
					["TXScrollNumBoxUnmapped", classData.fixedFreqSpec, "op4FixedFreq", nil, 60, 0.1],
					["TXScrollNumBoxUnmapped", classData.fixedFreqSpec, "op5FixedFreq", nil, 60, 0.1],
					["TXScrollNumBoxUnmapped", classData.fixedFreqSpec, "op6FixedFreq", nil, 60, 0.1],
					["NextLine"],
					["TextBar", "Detune", 80, nil, nil, nil, \right],
					["TXScrollNumBoxUnmapped", \bipolar.asSpec, "op1Detune", nil, 60, 0.1],
					["TXScrollNumBoxUnmapped", \bipolar.asSpec, "op2Detune", nil, 60, 0.1],
					["TXScrollNumBoxUnmapped", \bipolar.asSpec, "op3Detune", nil, 60, 0.1],
					["TXScrollNumBoxUnmapped", \bipolar.asSpec, "op4Detune", nil, 60, 0.1],
					["TXScrollNumBoxUnmapped", \bipolar.asSpec, "op5Detune", nil, 60, 0.1],
					["TXScrollNumBoxUnmapped", \bipolar.asSpec, "op6Detune", nil, 60, 0.1],
					["ActionPopup", ["actions...", "all 0", "rand lo", "rand hi", ], {arg view;
						case { view.value == 1 }   {
							this.setSynthValue("op1Detune", 0.5);
							this.setSynthValue("op2Detune", 0.5);
							this.setSynthValue("op3Detune", 0.5);
							this.setSynthValue("op4Detune", 0.5);
							this.setSynthValue("op5Detune", 0.5);
							this.setSynthValue("op6Detune", 0.5);
							system.showView;
						}
						{ view.value == 2 }   {
							this.setSynthValue("op1Detune", gauss(0.0, 0.1).linlin(-1, 1, 0, 1));
							this.setSynthValue("op2Detune", gauss(0.0, 0.1).linlin(-1, 1, 0, 1));
							this.setSynthValue("op3Detune", gauss(0.0, 0.1).linlin(-1, 1, 0, 1));
							this.setSynthValue("op4Detune", gauss(0.0, 0.1).linlin(-1, 1, 0, 1));
							this.setSynthValue("op5Detune", gauss(0.0, 0.1).linlin(-1, 1, 0, 1));
							this.setSynthValue("op6Detune", gauss(0.0, 0.1).linlin(-1, 1, 0, 1));
							system.showView;
						}
						{ view.value == 3 }   {
							this.setSynthValue("op1Detune", gauss(0.0, 0.3).linlin(-1, 1, 0, 1));
							this.setSynthValue("op2Detune", gauss(0.0, 0.3).linlin(-1, 1, 0, 1));
							this.setSynthValue("op3Detune", gauss(0.0, 0.3).linlin(-1, 1, 0, 1));
							this.setSynthValue("op4Detune", gauss(0.0, 0.3).linlin(-1, 1, 0, 1));
							this.setSynthValue("op5Detune", gauss(0.0, 0.3).linlin(-1, 1, 0, 1));
							this.setSynthValue("op6Detune", gauss(0.0, 0.3).linlin(-1, 1, 0, 1));
							system.showView;
						};
					}, 80],
					["NextLine"],
					["EZNumber", "Phase", \unipolar.asSpec, "op1Phase", nil, nil, nil, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "op2Phase", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "op3Phase", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "op4Phase", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "op5Phase", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "op6Phase", nil, 60, 0.05],
					["ActionPopup", ["actions...", "all 0", "all 0.5", "random", ], {arg view;
						case { view.value == 1 }   {
							this.setSynthValue("op1Phase", 0);
							this.setSynthValue("op2Phase", 0);
							this.setSynthValue("op3Phase", 0);
							this.setSynthValue("op4Phase", 0);
							this.setSynthValue("op5Phase", 0);
							this.setSynthValue("op6Phase", 0);
							system.showView;
						}
						{ view.value == 2 }   {
							this.setSynthValue("op1Phase", 0.5);
							this.setSynthValue("op2Phase", 0.5);
							this.setSynthValue("op3Phase", 0.5);
							this.setSynthValue("op4Phase", 0.5);
							this.setSynthValue("op5Phase", 0.5);
							this.setSynthValue("op6Phase", 0.5);
							system.showView;
						}
						{ view.value == 3 }   {
							this.setSynthValue("op1Phase", rrand(0.0, 1.0));
							this.setSynthValue("op2Phase", rrand(0.0, 1.0));
							this.setSynthValue("op3Phase", rrand(0.0, 1.0));
							this.setSynthValue("op4Phase", rrand(0.0, 1.0));
							this.setSynthValue("op5Phase", rrand(0.0, 1.0));
							this.setSynthValue("op6Phase", rrand(0.0, 1.0));
							system.showView;
						};
					}, 80],
					["NextLine"],
					["EZNumber", "Amplitude", \unipolar.asSpec, "op1Amp", nil, nil, nil, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "op2Amp", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "op3Amp", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "op4Amp", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "op5Amp", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "op6Amp", nil, 60, 0.05],
					["ActionPopup", ["actions...", "all 0", "all 0.5", "all 1", "random", ], {arg view;
						case { view.value == 1 }   {
							this.setSynthValue("op1Amp", 0);
							this.setSynthValue("op2Amp", 0);
							this.setSynthValue("op3Amp", 0);
							this.setSynthValue("op4Amp", 0);
							this.setSynthValue("op5Amp", 0);
							this.setSynthValue("op6Amp", 0);
							system.showView;
						}
						{ view.value == 2 }   {
							this.setSynthValue("op1Amp", 0.5);
							this.setSynthValue("op2Amp", 0.5);
							this.setSynthValue("op3Amp", 0.5);
							this.setSynthValue("op4Amp", 0.5);
							this.setSynthValue("op5Amp", 0.5);
							this.setSynthValue("op6Amp", 0.5);
							system.showView;
						}
						{ view.value == 3 }   {
							this.setSynthValue("op1Amp", 1);
							this.setSynthValue("op2Amp", 1);
							this.setSynthValue("op3Amp", 1);
							this.setSynthValue("op4Amp", 1);
							this.setSynthValue("op5Amp", 1);
							this.setSynthValue("op6Amp", 1);
							system.showView;
						}
						{ view.value == 4 }   {
							this.setSynthValue("op1Amp", rrand(0.0, 1.0));
							this.setSynthValue("op2Amp", rrand(0.0, 1.0));
							this.setSynthValue("op3Amp", rrand(0.0, 1.0));
							this.setSynthValue("op4Amp", rrand(0.0, 1.0));
							this.setSynthValue("op5Amp", rrand(0.0, 1.0));
							this.setSynthValue("op6Amp", rrand(0.0, 1.0));
							system.showView;
						};
					}, 80],
					["SpacerLine", 2],
					["EZNumber", "Mod Op 1", \unipolar.asSpec, "mIndFM_11", nil, nil, nil, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_21", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_31", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_41", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_51", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_61", nil, 60, 0.05],
					["NextLine"],
					["EZNumber", "Mod Op 2", \unipolar.asSpec, "mIndFM_12", nil, nil, nil, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_22", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_32", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_42", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_52", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_62", nil, 60, 0.05],
					["NextLine"],
					["EZNumber", "Mod Op 3", \unipolar.asSpec, "mIndFM_13", nil, nil, nil, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_23", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_33", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_43", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_53", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_63", nil, 60, 0.05],
					["NextLine"],
					["EZNumber", "Mod Op 4", \unipolar.asSpec, "mIndFM_14", nil, nil, nil, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_24", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_34", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_44", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_54", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_64", nil, 60, 0.05],
					["NextLine"],
					["EZNumber", "Mod Op 5", \unipolar.asSpec, "mIndFM_15", nil, nil, nil, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_25", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_35", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_45", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_55", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_65", nil, 60, 0.05],
					["NextLine"],
					["EZNumber", "Mod Op 6", \unipolar.asSpec, "mIndFM_16", nil, nil, nil, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_26", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_36", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_46", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_56", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "mIndFM_66", nil, 60, 0.05],
					["SpacerLine", 2],
					["EZNumber", "Op Level", \unipolar.asSpec, "outLevelOp1", nil, nil, nil, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "outLevelOp2", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "outLevelOp3", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "outLevelOp4", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "outLevelOp5", nil, 60, 0.05],
					["TXScrollNumBox", \unipolar.asSpec, "outLevelOp6", nil, 60, 0.05],
				];
			});
			if (displayOperator == "showAllOps2", {
				guiSpecArray = guiSpecArray ++[
					["TextBar", "Use fixed freq", 80, nil, nil, nil, \right],
					["SynthOptionCheckBox", "", arrOptionData, 3, 60],
					["SynthOptionCheckBox", "", arrOptionData, 4, 60],
					["SynthOptionCheckBox", "", arrOptionData, 5, 60],
					["SynthOptionCheckBox", "", arrOptionData, 6, 60],
					["SynthOptionCheckBox", "", arrOptionData, 7, 60],
					["SynthOptionCheckBox", "", arrOptionData, 8, 60],
					["ActionPopup", ["actions...", "all off", "all on", "random", ], {arg view;
						case { view.value == 1 }   {
							arrOptions[3] = 0;
							arrOptions[4] = 0;
							arrOptions[5] = 0;
							arrOptions[6] = 0;
							arrOptions[7] = 0;
							arrOptions[8] = 0;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 2 }   {
							arrOptions[3] = 1;
							arrOptions[4] = 1;
							arrOptions[5] = 1;
							arrOptions[6] = 1;
							arrOptions[7] = 1;
							arrOptions[8] = 1;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 3 }   {
							arrOptions[3] = [0,1].choose;
							arrOptions[4] = [0,1].choose;
							arrOptions[5] = [0,1].choose;
							arrOptions[6] = [0,1].choose;
							arrOptions[7] = [0,1].choose;
							arrOptions[8] = [0,1].choose;
							this.rebuildSynth;
							system.showView;
						};
					}, 80],
					["NextLine"],
					["TextBar", "Freq ratio", 80, nil, nil, nil, \right],
					["TXCompactSlider", classData.ratioSpec, "op1FreqRatio", nil, 60],
					["TXCompactSlider", classData.ratioSpec, "op2FreqRatio", nil, 60],
					["TXCompactSlider", classData.ratioSpec, "op3FreqRatio", nil, 60],
					["TXCompactSlider", classData.ratioSpec, "op4FreqRatio", nil, 60],
					["TXCompactSlider", classData.ratioSpec, "op5FreqRatio", nil, 60],
					["TXCompactSlider", classData.ratioSpec, "op6FreqRatio", nil, 60],
					["NextLine"],
					["TextBar", "Fixed freq", 80, nil, nil, nil, \right],
					["TXCompactSliderUnmapped", classData.fixedFreqSpec, "op1FixedFreq", nil, 60],
					["TXCompactSliderUnmapped", classData.fixedFreqSpec, "op2FixedFreq", nil, 60],
					["TXCompactSliderUnmapped", classData.fixedFreqSpec, "op3FixedFreq", nil, 60],
					["TXCompactSliderUnmapped", classData.fixedFreqSpec, "op4FixedFreq", nil, 60],
					["TXCompactSliderUnmapped", classData.fixedFreqSpec, "op5FixedFreq", nil, 60],
					["TXCompactSliderUnmapped", classData.fixedFreqSpec, "op6FixedFreq", nil, 60],
					["NextLine"],
					["TextBar", "Detune", 80, nil, nil, nil, \right],
					["TXCompactSliderUnmapped", \bipolar.asSpec, "op1Detune", nil, 60],
					["TXCompactSliderUnmapped", \bipolar.asSpec, "op2Detune", nil, 60],
					["TXCompactSliderUnmapped", \bipolar.asSpec, "op3Detune", nil, 60],
					["TXCompactSliderUnmapped", \bipolar.asSpec, "op4Detune", nil, 60],
					["TXCompactSliderUnmapped", \bipolar.asSpec, "op5Detune", nil, 60],
					["TXCompactSliderUnmapped", \bipolar.asSpec, "op6Detune", nil, 60],
					["ActionPopup", ["actions...", "all 0", "rand lo", "rand hi", ], {arg view;
						case { view.value == 1 }   {
							this.setSynthValue("op1Detune", 0.5);
							this.setSynthValue("op2Detune", 0.5);
							this.setSynthValue("op3Detune", 0.5);
							this.setSynthValue("op4Detune", 0.5);
							this.setSynthValue("op5Detune", 0.5);
							this.setSynthValue("op6Detune", 0.5);
							system.showView;
						}
						{ view.value == 2 }   {
							this.setSynthValue("op1Detune", gauss(0.0, 0.1).linlin(-1, 1, 0, 1));
							this.setSynthValue("op2Detune", gauss(0.0, 0.1).linlin(-1, 1, 0, 1));
							this.setSynthValue("op3Detune", gauss(0.0, 0.1).linlin(-1, 1, 0, 1));
							this.setSynthValue("op4Detune", gauss(0.0, 0.1).linlin(-1, 1, 0, 1));
							this.setSynthValue("op5Detune", gauss(0.0, 0.1).linlin(-1, 1, 0, 1));
							this.setSynthValue("op6Detune", gauss(0.0, 0.1).linlin(-1, 1, 0, 1));
							system.showView;
						}
						{ view.value == 3 }   {
							this.setSynthValue("op1Detune", gauss(0.0, 0.3).linlin(-1, 1, 0, 1));
							this.setSynthValue("op2Detune", gauss(0.0, 0.3).linlin(-1, 1, 0, 1));
							this.setSynthValue("op3Detune", gauss(0.0, 0.3).linlin(-1, 1, 0, 1));
							this.setSynthValue("op4Detune", gauss(0.0, 0.3).linlin(-1, 1, 0, 1));
							this.setSynthValue("op5Detune", gauss(0.0, 0.3).linlin(-1, 1, 0, 1));
							this.setSynthValue("op6Detune", gauss(0.0, 0.3).linlin(-1, 1, 0, 1));
							system.showView;
						};
					}, 80],
					["NextLine"],
					["TextBar", "Phase", 80, nil, nil, nil, \right],
					["TXCompactSlider", \unipolar.asSpec, "op1Phase", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "op2Phase", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "op3Phase", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "op4Phase", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "op5Phase", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "op6Phase", nil, 60],
					["ActionPopup", ["actions...", "all 0", "all 0.5", "random", ], {arg view;
						case { view.value == 1 }   {
							this.setSynthValue("op1Phase", 0);
							this.setSynthValue("op2Phase", 0);
							this.setSynthValue("op3Phase", 0);
							this.setSynthValue("op4Phase", 0);
							this.setSynthValue("op5Phase", 0);
							this.setSynthValue("op6Phase", 0);
							system.showView;
						}
						{ view.value == 2 }   {
							this.setSynthValue("op1Phase", 0.5);
							this.setSynthValue("op2Phase", 0.5);
							this.setSynthValue("op3Phase", 0.5);
							this.setSynthValue("op4Phase", 0.5);
							this.setSynthValue("op5Phase", 0.5);
							this.setSynthValue("op6Phase", 0.5);
							system.showView;
						}
						{ view.value == 3 }   {
							this.setSynthValue("op1Phase", rrand(0.0, 1.0));
							this.setSynthValue("op2Phase", rrand(0.0, 1.0));
							this.setSynthValue("op3Phase", rrand(0.0, 1.0));
							this.setSynthValue("op4Phase", rrand(0.0, 1.0));
							this.setSynthValue("op5Phase", rrand(0.0, 1.0));
							this.setSynthValue("op6Phase", rrand(0.0, 1.0));
							system.showView;
						};
					}, 80],
					["NextLine"],
					["TextBar", "Amplitude", 80, nil, nil, nil, \right],
					["TXCompactSlider", \unipolar.asSpec, "op1Amp", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "op2Amp", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "op3Amp", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "op4Amp", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "op5Amp", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "op6Amp", nil, 60],
					["ActionPopup", ["actions...", "all 0", "all 0.5", "all 1", "random", ], {arg view;
						case { view.value == 1 }   {
							this.setSynthValue("op1Amp", 0);
							this.setSynthValue("op2Amp", 0);
							this.setSynthValue("op3Amp", 0);
							this.setSynthValue("op4Amp", 0);
							this.setSynthValue("op5Amp", 0);
							this.setSynthValue("op6Amp", 0);
							system.showView;
						}
						{ view.value == 2 }   {
							this.setSynthValue("op1Amp", 0.5);
							this.setSynthValue("op2Amp", 0.5);
							this.setSynthValue("op3Amp", 0.5);
							this.setSynthValue("op4Amp", 0.5);
							this.setSynthValue("op5Amp", 0.5);
							this.setSynthValue("op6Amp", 0.5);
							system.showView;
						}
						{ view.value == 3 }   {
							this.setSynthValue("op1Amp", 1);
							this.setSynthValue("op2Amp", 1);
							this.setSynthValue("op3Amp", 1);
							this.setSynthValue("op4Amp", 1);
							this.setSynthValue("op5Amp", 1);
							this.setSynthValue("op6Amp", 1);
							system.showView;
						}
						{ view.value == 4 }   {
							this.setSynthValue("op1Amp", rrand(0.0, 1.0));
							this.setSynthValue("op2Amp", rrand(0.0, 1.0));
							this.setSynthValue("op3Amp", rrand(0.0, 1.0));
							this.setSynthValue("op4Amp", rrand(0.0, 1.0));
							this.setSynthValue("op5Amp", rrand(0.0, 1.0));
							this.setSynthValue("op6Amp", rrand(0.0, 1.0));
							system.showView;
						};
					}, 80],
					["SpacerLine", 2],
					["TextBar", "Mod Op 1", 80, nil, nil, nil, \right],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_11", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_21", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_31", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_41", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_51", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_61", nil, 60],
					["NextLine"],
					["TextBar", "Mod Op 2", 80, nil, nil, nil, \right],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_12", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_22", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_32", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_42", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_52", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_62", nil, 60],
					["NextLine"],
					["TextBar", "Mod Op 3", 80, nil, nil, nil, \right],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_13", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_23", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_33", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_43", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_53", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_63", nil, 60],
					["NextLine"],
					["TextBar", "Mod Op 4", 80, nil, nil, nil, \right],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_14", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_24", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_34", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_44", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_54", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_64", nil, 60],
					["NextLine"],
					["TextBar", "Mod Op 5", 80, nil, nil, nil, \right],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_15", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_25", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_35", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_45", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_55", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_65", nil, 60],
					["NextLine"],
					["TextBar", "Mod Op 6", 80, nil, nil, nil, \right],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_16", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_26", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_36", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_46", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_56", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "mIndFM_66", nil, 60],
					["SpacerLine", 2],
					["TextBar", "Op Level", 80, nil, nil, nil, \right],
					["TXCompactSlider", \unipolar.asSpec, "outLevelOp1", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "outLevelOp2", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "outLevelOp3", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "outLevelOp4", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "outLevelOp5", nil, 60],
					["TXCompactSlider", \unipolar.asSpec, "outLevelOp6", nil, 60],
				];
			});
			if (displayOperator == "showAllOpsRatio", {
				guiSpecArray = guiSpecArray ++[
					["TextBar", "Use fixed freq", 80, nil, nil, nil, \right],
					["SynthOptionCheckBox", "", arrOptionData, 3, 60],
					["SynthOptionCheckBox", "", arrOptionData, 4, 60],
					["SynthOptionCheckBox", "", arrOptionData, 5, 60],
					["SynthOptionCheckBox", "", arrOptionData, 6, 60],
					["SynthOptionCheckBox", "", arrOptionData, 7, 60],
					["SynthOptionCheckBox", "", arrOptionData, 8, 60],
					["ActionPopup", ["actions...", "all off", "all on", "random", ], {arg view;
						case { view.value == 1 }   {
							arrOptions[3] = 0;
							arrOptions[4] = 0;
							arrOptions[5] = 0;
							arrOptions[6] = 0;
							arrOptions[7] = 0;
							arrOptions[8] = 0;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 2 }   {
							arrOptions[3] = 1;
							arrOptions[4] = 1;
							arrOptions[5] = 1;
							arrOptions[6] = 1;
							arrOptions[7] = 1;
							arrOptions[8] = 1;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 3 }   {
							arrOptions[3] = [0,1].choose;
							arrOptions[4] = [0,1].choose;
							arrOptions[5] = [0,1].choose;
							arrOptions[6] = [0,1].choose;
							arrOptions[7] = [0,1].choose;
							arrOptions[8] = [0,1].choose;
							this.rebuildSynth;
							system.showView;
						};
					}, 80],
					["SpacerLine", 6],
					["TXFractionSlider", "Op 1 freq ratio", classData.ratioSpec, "op1FreqRatio", nil, 300],
					["SpacerLine", 2],
					["TXFractionSlider", "Op 2 freq ratio", classData.ratioSpec, "op2FreqRatio", nil, 300],
					["SpacerLine", 2],
					["TXFractionSlider", "Op 3 freq ratio", classData.ratioSpec, "op3FreqRatio", nil, 300],
					["SpacerLine", 2],
					["TXFractionSlider", "Op 4 freq ratio", classData.ratioSpec, "op4FreqRatio", nil, 300],
					["SpacerLine", 2],
					["TXFractionSlider", "Op 5 freq ratio", classData.ratioSpec, "op5FreqRatio", nil, 300],
					["SpacerLine", 2],
					["TXFractionSlider", "Op 6 freq ratio", classData.ratioSpec, "op6FreqRatio", nil, 300],
					["DividingLine"],
					["SpacerLine", 6],
					["TXPopupActionPlusMinus", "Process",
						arrRatioProcs.collect({arg item, i; item.at(0)}), "ratioProcess", ],
					["Spacer", 80],
					["ActionButton", "Run Process",
						{arg view; arrRatioProcs[this.getSynthArgSpec("ratioProcess")][1].value; system.showView;},
						110, TXColor.white, TXColor.sysGuiCol2
					],
				];
			});
			if (displayOperator == "showAllOpsFFreq", {
				guiSpecArray = guiSpecArray ++[
					["TextBar", "Use fixed freq", 80, nil, nil, nil, \right],
					["SynthOptionCheckBox", "", arrOptionData, 3, 60],
					["SynthOptionCheckBox", "", arrOptionData, 4, 60],
					["SynthOptionCheckBox", "", arrOptionData, 5, 60],
					["SynthOptionCheckBox", "", arrOptionData, 6, 60],
					["SynthOptionCheckBox", "", arrOptionData, 7, 60],
					["SynthOptionCheckBox", "", arrOptionData, 8, 60],
					["ActionPopup", ["actions...", "all off", "all on", "random", ], {arg view;
						case { view.value == 1 }   {
							arrOptions[3] = 0;
							arrOptions[4] = 0;
							arrOptions[5] = 0;
							arrOptions[6] = 0;
							arrOptions[7] = 0;
							arrOptions[8] = 0;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 2 }   {
							arrOptions[3] = 1;
							arrOptions[4] = 1;
							arrOptions[5] = 1;
							arrOptions[6] = 1;
							arrOptions[7] = 1;
							arrOptions[8] = 1;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 3 }   {
							arrOptions[3] = [0,1].choose;
							arrOptions[4] = [0,1].choose;
							arrOptions[5] = [0,1].choose;
							arrOptions[6] = [0,1].choose;
							arrOptions[7] = [0,1].choose;
							arrOptions[8] = [0,1].choose;
							this.rebuildSynth;
							system.showView;
						};
					}, 80],
					["SpacerLine", 6],
					["EZsliderUnmapped", "Op 1 fixed freq", classData.fixedFreqSpec, "op1FixedFreq", nil, 456],
					["ActionPopup", midiNoteNames, {arg view; if (view.value > 0, {
						this.setSynthValue("op1FixedFreq", classData.fixedFreqSpec.unmap((view.value + 23).midicps));
						system.showView;
					});}, 60],
					["SpacerLine", 2],
					["EZsliderUnmapped", "Op 2 fixed freq", classData.fixedFreqSpec, "op2FixedFreq", nil, 456],
					["ActionPopup", midiNoteNames, {arg view; if (view.value > 0, {
						this.setSynthValue("op2FixedFreq", classData.fixedFreqSpec.unmap((view.value + 23).midicps));
						system.showView;
					});}, 60],
					["SpacerLine", 2],
					["EZsliderUnmapped", "Op 3 fixed freq", classData.fixedFreqSpec, "op3FixedFreq", nil, 456],
					["ActionPopup", midiNoteNames, {arg view; if (view.value > 0, {
						this.setSynthValue("op3FixedFreq", classData.fixedFreqSpec.unmap((view.value + 23).midicps));
						system.showView;
					});}, 60],
					["SpacerLine", 2],
					["EZsliderUnmapped", "Op 4 fixed freq", classData.fixedFreqSpec, "op4FixedFreq", nil, 456],
					["ActionPopup", midiNoteNames, {arg view; if (view.value > 0, {
						this.setSynthValue("op4FixedFreq", classData.fixedFreqSpec.unmap((view.value + 23).midicps));
						system.showView;
					});}, 60],
					["SpacerLine", 2],
					["EZsliderUnmapped", "Op 5 fixed freq", classData.fixedFreqSpec, "op5FixedFreq", nil, 456],
					["ActionPopup", midiNoteNames, {arg view; if (view.value > 0, {
						this.setSynthValue("op5FixedFreq", classData.fixedFreqSpec.unmap((view.value + 23).midicps));
						system.showView;
					});}, 60],
					["SpacerLine", 2],
					["EZsliderUnmapped", "Op 6 fixed freq", classData.fixedFreqSpec, "op6FixedFreq", nil, 456],
					["ActionPopup", midiNoteNames, {arg view; if (view.value > 0, {
						this.setSynthValue("op6FixedFreq", classData.fixedFreqSpec.unmap((view.value + 23).midicps));
						system.showView;
					});}, 60],
					["DividingLine"],
					["SpacerLine", 6],
					["TXPopupActionPlusMinus", "Process",
						arrFreqProcs.collect({arg item, i; item.at(0)}), "freqProcess", ],
					["Spacer", 80],
					["ActionButton", "Run Process",
						{arg view; arrFreqProcs[this.getSynthArgSpec("freqProcess")][1].value; system.showView;},
						110, TXColor.white, TXColor.sysGuiCol2
					],
				];
			});
			// Algorithm
			if ((displayOperator == "showAllOps") or: (displayOperator == "showAllOps2"), {
				guiSpecArray = guiSpecArray ++[
					["DividingLine"],
					["SpacerLine", 2],
					["TXPopupActionPlusMinus", "Algorithm",
						TXFMPresets.arrFMPresets.collect({arg item; item[0]}), "algorithm", ],
					["EZslider", "Algo feedback", \unipolar.asSpec, "algoFeedback", nil, 340],
					["Spacer", 10],
					["ActionButton", "Load Algorithm", {this.loadAlgorithm;}, 110, TXColor.white, TXColor.sysGuiCol2],
					// main level
					["DividingLine"],
					["SpacerLine", 2],
					["EZslider", "Main level", \unipolar.asSpec, "level"],
					["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 2],
				];
			});
		});  // end of if (displayOption == "showOperators"...

		if (displayOption == "showFreq", {
			guiSpecArray = guiSpecArray ++[
				["TXMinMaxFreqNoteSldr", "Base Freq", ControlSpec(0.midicps, 20000, \exponential),
					"freq", "freqMin", "freqMax", nil, TXFreqRangePresets.arrFreqRanges],
				["DividingLine"],
				["SpacerLine", 2],
				["SynthOptionCheckBox", "Use note list instead of variable frequency", arrOptionData, 0, 400],
				["SpacerLine", 2],
				["TXListViewActionPlusMinus", "Chord/ scale", TXScale.arrScaleNames, "i_noteListTypeInd",
					{this.updateSynth;}, 400],
				["SpacerLine", 2],
				["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
					"i_noteListRoot", {this.updateSynth;}, 140],
				["SpacerLine", 2],
				["TXNoteRangeSlider", "Note range", "i_noteListMin", "i_noteListMax", {this.updateSynth;}, true],
				["SpacerLine", 2],
				["TXStaticText", "Note count", {this.getNoteTotalText}, {arg view; noteListTextView = view;}],
				["SpacerLine", 2],
				["EZslider", "Note select", \unipolar.asSpec, "freqSelector"],
			];
		});
		if (displayOption == "showSmoothing", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Smoothing", arrOptionData, 1],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Time 1", classData.lagSpec, "lag", "lagMin", "lagMax"],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Time 2", classData.lagSpec, "lag2", "lag2Min", "lag2Max"],
			];
		});
	}

	getButtonColour { arg colour2Boolean;
		if (colour2Boolean == true, {
			^TXColor.sysGuiCol4;
		},{
			^TXColor.sysGuiCol1;
		});
	}

	getNoteArray {
		var arrScaleSpec, scaleRoot, noteMin, noteMax, arrScaleNotes;
		// Generate array of notes from chord, mode, scale
		arrScaleSpec = TXScale.arrScaleNotes.at(this.getSynthArgSpec("i_noteListTypeInd"));
		scaleRoot = this.getSynthArgSpec("i_noteListRoot");
		noteMin = this.getSynthArgSpec("i_noteListMin");
		noteMax = this.getSynthArgSpec("i_noteListMax");
		arrScaleNotes = [];
		13.do({arg octave;
			arrScaleSpec.do({arg item, i;
				arrScaleNotes = arrScaleNotes.add((octave * 12) + scaleRoot + item);
			});
		});
		arrScaleNotes = arrScaleNotes.select({arg item, i; ((item >= noteMin) and: (item <= noteMax)); });
		this.setSynthArgSpec("i_noteListSize", arrScaleNotes.size);
		if (arrScaleNotes.size == 0, {
			arrScaleNotes = [noteMin];
		});
		^arrScaleNotes;
	}

	getNoteTotalText {
		var noteListSize, outText;
		noteListSize = this.getSynthArgSpec("i_noteListSize");
		if (noteListSize == 0, {
			outText = "ERROR: No notes in note list - need to widen range ";
		}, {
			outText = "Total no. of notes:  " ++ noteListSize.asString;
		});
		^outText;
	}

	updateSynth {
		this.getNoteArray;
		this.rebuildSynth;
		if (noteListTextView.notNil, {
			{noteListTextView.string = this.getNoteTotalText;}.defer();
		});
	}

	loadAlgorithm {
		var algoIndex = this.getSynthArgSpec("algorithm");
		var algoFeedback = this.getSynthArgSpec("algoFeedback");
		var presetData = TXFMPresets.arrFMPresets[algoIndex][1].value(algoFeedback);
		var arrNames = [
			["mIndFM_11","mIndFM_12","mIndFM_13","mIndFM_14","mIndFM_15","mIndFM_16","outLevelOp1",],
			["mIndFM_21","mIndFM_22","mIndFM_23","mIndFM_24","mIndFM_25","mIndFM_26","outLevelOp2",],
			["mIndFM_31","mIndFM_32","mIndFM_33","mIndFM_34","mIndFM_35","mIndFM_36","outLevelOp3",],
			["mIndFM_41","mIndFM_42","mIndFM_43","mIndFM_44","mIndFM_45","mIndFM_46","outLevelOp4",],
			["mIndFM_51","mIndFM_52","mIndFM_53","mIndFM_54","mIndFM_55","mIndFM_56","outLevelOp5",],
			["mIndFM_61","mIndFM_62","mIndFM_63","mIndFM_64","mIndFM_65","mIndFM_66","outLevelOp6",],
		];
		6.do({arg op;
			var opnames = arrNames[op];
			var opvalues = presetData[op];
			opnames.do({arg name, i;
				this.setSynthValue(name, opvalues[i]);
			});
		});
		system.showViewIfModDisplay(this);
	}

}

