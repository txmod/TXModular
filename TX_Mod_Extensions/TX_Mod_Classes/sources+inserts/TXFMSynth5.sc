// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFMSynth5 : TXModuleBase {

	classvar <>classData;

	var displayOption;
	var displayOperator, displayVelCurve, displayNoteCurve, displayEnv;
	var ratioView;
	var	envView, opEnvView1, opEnvView2;
	var <>testMIDINote = 69;
	var <>testMIDIVel = 100;
	var <>testMIDITime = 1;
	var	arrEnvPresetNames, arrEnvPresetActions;
	var arrRatioProcs, arrFreqProcs;
	var	linearCurve, flat50Curve, velCurveIndex, curveDataEvent, arrVelocityCurves, arrSlotData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.guiWidth = 610;
		classData.arrInstances = [];
		classData.defaultName = "FM Synth";
		classData.moduleRate = "audio";
		classData.moduleType = "groupsource";
		classData.arrCtlSCInBusSpecs = [
			["Pitch bend", 1, "modPitchbend", 0],
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
			["Op1 Level", 1, "modOutLevelOp1", 0],
			["Op2 Level", 1, "modOutLevelOp2", 0],
			["Op3 Level", 1, "modOutLevelOp3", 0],
			["Op4 Level", 1, "modOutLevelOp4", 0],
			["Op5 Level", 1, "modOutLevelOp5", 0],
			["Op6 Level", 1, "modOutLevelOp6", 0],
			["Vol Env Delay", 1, "modDelay", 0],
			["Vol Env Attack", 1, "modAttack", 0],
			["Vol Env Decay", 1, "modDecay", 0],
			["Vol Env Sustain level", 1, "modSustain", 0],
			["Vol Env Sustain level 2", 1, "modSustain2", 0],
			["Vol Env Sustain time", 1, "modSustainTime", 0],
			["Vol Env Release", 1, "modRelease", 0],
			["Op 1 Env Amount", 1, "modEnvAmountOp1", 0],
			["Op 1 Env Delay", 1, "modDelayOp1", 0],
			["Op 1 Env Attack", 1, "modAttackOp1", 0],
			["Op 1 Env Decay", 1, "modDecayOp1", 0],
			["Op 1 Env Sustain level", 1, "modSustainOp1", 0],
			["Op 1 Env Sustain level 2", 1, "modSustain2Op1", 0],
			["Op 1 Env Sustain time", 1, "modSustainTimeOp1", 0],
			["Op 1 Env Release", 1, "modReleaseOp1", 0],
			["Op 2 Env Amount", 1, "modEnvAmountOp2", 0],
			["Op 2 Env Delay", 1, "modDelayOp2", 0],
			["Op 2 Env Attack", 1, "modAttackOp2", 0],
			["Op 2 Env Decay", 1, "modDecayOp2", 0],
			["Op 2 Env Sustain level", 1, "modSustainOp2", 0],
			["Op 2 Env Sustain level 2", 1, "modSustain2Op2", 0],
			["Op 2 Env Sustain time", 1, "modSustainTimeOp2", 0],
			//["Op 2 Env Release", 1, "modReleaseOp2", 0],  // removed
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.timeSpec = ControlSpec(0.01, 20);
		classData.ratioSpec = ControlSpec(0.01, 16, \amp);
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
		classData.clipBoardNoteCurve = 1 ! 72;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showOperators";
		displayOperator = "showAllOps";
		displayVelCurve = "velCurveOp1";
		velCurveIndex = 0;
		curveDataEvent = ();
		displayNoteCurve = "noteCurveOp1";
		displayEnv = "showVolEnv";
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
		];  // end of arrRatioProcs = [
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
			[	"Randomise all frequencies, MIDI note range: C1 - C3",
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
			[	"Randomise all frequencies, MIDI note range: C2 - C5",
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
			[	"Randomise all frequencies, MIDI note range: C1 - C6",
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
		]; // end of arrFreqProcs = [
		arrSynthArgSpecs = [
			["out", 0, 0],
			["gate", 1, 0],
			["note", 0, 0],
			["velocity", 0, 0],
			["keytrack", 1, \ir],
			["transpose", 0, \ir],
			["pitchOffset", 0, \ir],
			["pitchbend", 0.5, defLagTime],
			["pitchbendMin", -2, defLagTime],
			["pitchbendMax", 2, defLagTime],
			["level", 0.5, defLagTime],

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

			["noteLevelOp1", 1, \ir],
			["noteLevelOp2", 1, \ir],
			["noteLevelOp3", 1, \ir],
			["noteLevelOp4", 1, \ir],
			["noteLevelOp5", 1, \ir],
			["noteLevelOp6", 1, \ir],

			["outLevelOp1", 1, defLagTime],
			["outLevelOp2", 0, defLagTime],
			["outLevelOp3", 0, defLagTime],
			["outLevelOp4", 0, defLagTime],
			["outLevelOp5", 0, defLagTime],
			["outLevelOp6", 0, defLagTime],

			["envtime", 0, \ir],
			["delay", 0, \ir],
			["attack", 0.005, \ir],
			["attackMin", 0, \ir],
			["attackMax", 5, \ir],
			["decay", 0.05, \ir],
			["decayMin", 0, \ir],
			["decayMax", 5, \ir],
			["sustain", 1, \ir],
			["sustain2", 1, \ir],
			["sustainTime", 0.2, \ir],
			["sustainTimeMin", 0, \ir],
			["sustainTimeMax", 5, \ir],
			["release", 0.01, \ir],
			["releaseMin", 0, \ir],
			["releaseMax", 5, \ir],

			["envtimeOp1", 0, \ir],
			["envAmountOp1", 1, \ir],
			["delayOp1", 0, \ir],
			["attackOp1", 0.005, \ir],
			["attackMinOp1", 0, \ir],
			["attackMaxOp1", 5, \ir],
			["decayOp1", 0.05, \ir],
			["decayMinOp1", 0, \ir],
			["decayMaxOp1", 5, \ir],
			["sustainOp1", 1, \ir],
			["sustain2Op1", 1, \ir],
			["sustainTimeOp1", 0.2, \ir],
			["sustainTimeMinOp1", 0, \ir],
			["sustainTimeMaxOp1", 5, \ir],
			["releaseOp1", 0.01, \ir],
			["releaseMinOp1", 0, \ir],
			["releaseMaxOp1", 5, \ir],
			["envtimeOp2", 0, \ir],
			["envAmountOp2", 1, \ir],
			["delayOp2", 0, \ir],
			["attackOp2", 0.005, \ir],
			["attackMinOp2", 0, \ir],
			["attackMaxOp2", 5, \ir],
			["decayOp2", 0.05, \ir],
			["decayMinOp2", 0, \ir],
			["decayMaxOp2", 5, \ir],
			["sustainOp2", 1, \ir],
			["sustain2Op2", 1, \ir],
			["sustainTimeOp2", 0.2, \ir],
			["sustainTimeMinOp2", 0, \ir],
			["sustainTimeMaxOp2", 5, \ir],
			["releaseOp2", 0.01, \ir],
			["releaseMinOp2", 0, \ir],
			["releaseMaxOp2", 5, \ir],

			["intKey", 0, \ir],

			["modPitchbend", 0, defLagTime],
			["modDelay", 0, \ir],
			["modAttack", 0, \ir],
			["modDecay", 0, \ir],
			["modSustain", 0, \ir],
			["modSustain2", 0, \ir],
			["modSustainTime", 0, \ir],
			["modRelease", 0, \ir],
			["modEnvAmountOp1", 0, \ir],
			["modDelayOp1", 0, \ir],
			["modAttackOp1", 0, \ir],
			["modDecayOp1", 0, \ir],
			["modSustainOp1", 0, \ir],
			["modSustain2Op1", 0, \ir],
			["modSustainTimeOp1", 0, \ir],
			["modReleaseOp1", 0, \ir],
			["modEnvAmountOp2", 0, \ir],
			["modDelayOp2", 0, \ir],
			["modAttackOp2", 0, \ir],
			["modDecayOp2", 0, \ir],
			["modSustainOp2", 0, \ir],
			["modSustain2Op2", 0, \ir],
			["modSustainTimeOp2", 0, \ir],
			//["modReleaseOp2", 0, \ir],  // removed

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
			["noteCurveOp1", 1 ! 72],
			["noteCurveOp2", 1 ! 72],
			["noteCurveOp3", 1 ! 72],
			["noteCurveOp4", 1 ! 72],
			["noteCurveOp5", 1 ! 72],
			["noteCurveOp6", 1 ! 72],
			["gridRows", 2],
			["gridCols", 2],
		];
		arrOptions = 0 ! 22;
		arrOptionData = [
			TXIntonation.arrOptionData,  // 0
			[  // 1
				["Off", { 0; }],
				["On", TXLFO.lfoFunction],
			],
			TXLFO.arrOptionData,  // 2
			TXLFO.arrLFOOutputRanges,  // 3
			[  // 4
				["Off", { 0; }],
				["On", TXLFO.lfoFunction],
			],
			TXLFO.arrOptionData,  // 5
			TXLFO.arrLFOOutputRanges,  // 6
			// amp env
			TXEnvLookup.arrDADSSRSlopeOptionData,  // 7
			TXEnvLookup.arrDADSSRSustainOptionData,  // 8
			// op env 1
			TXEnvLookup.arrDADSSRSlopeOptionData,  // 9
			TXEnvLookup.arrDADSSRSustainOptionData,  // 10
			[  // 11
				["Off", { 0; }],
				["On", {arg envFunction, del, att, dec, sus, sus2, sustime, rel, envCurve, gate;
					EnvGen.ar(
						envFunction.value(del, att, dec, sus, sus2, sustime, rel, envCurve),
						gate,
						doneAction: 0
					);
				}],
			],
			// op env 2
			TXEnvLookup.arrDADSSRSlopeOptionData,  // 12
			TXEnvLookup.arrDADSSRSustainOptionData,  // 13
			[  // 14
				["Off", { 0; }],
				["On", {arg envFunction, del, att, dec, sus, sus2, sustime, rel, envCurve, gate;
					EnvGen.ar(
						envFunction.value(del, att, dec, sus, sus2, sustime, rel, envCurve),
						gate,
						doneAction: 0
					);
				}],
			],
			// 15 amp compensation
			TXAmpComp.arrOptionData,
			// 16 - Op1 use fixed freq
			classData.fixedFreqOptionData,
			// 17 - Op2 use fixed freq
			classData.fixedFreqOptionData,
			// 18 - Op3 use fixed freq
			classData.fixedFreqOptionData,
			// 19 - Op4 use fixed freq
			classData.fixedFreqOptionData,
			// 20 - Op5 use fixed freq
			classData.fixedFreqOptionData,
			// 21 - Op6 use fixed freq
			classData.fixedFreqOptionData,
		];
		synthDefFunc = {
			arg out, gate, note, velocity, keytrack, transpose,
			pitchOffset, pitchbend, pitchbendMin, pitchbendMax,
			level,

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

			noteLevelOp1, noteLevelOp2, noteLevelOp3, noteLevelOp4, noteLevelOp5, noteLevelOp6,
			outLevelOp1, outLevelOp2, outLevelOp3, outLevelOp4, outLevelOp5, outLevelOp6,

			envtime, delay, attack, attackMin, attackMax, decay, decayMin, decayMax, sustain, sustain2,
			sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax,

			envtimeOp1, envAmountOp1, delayOp1, attackOp1, attackMinOp1, attackMaxOp1,
			decayOp1, decayMinOp1, decayMaxOp1, sustainOp1, sustain2Op1, sustainTimeOp1,
			sustainTimeMinOp1, sustainTimeMaxOp1, releaseOp1, releaseMinOp1, releaseMaxOp1,
			envtimeOp2, envAmountOp2, delayOp2, attackOp2, attackMinOp2, attackMaxOp2,
			decayOp2, decayMinOp2, decayMaxOp2, sustainOp2, sustain2Op2, sustainTimeOp2,
			sustainTimeMinOp2, sustainTimeMaxOp2, releaseOp2, releaseMinOp2, releaseMaxOp2,

			intKey,

			modPitchbend=0,
			modDelay=0, modAttack=0, modDecay=0, modSustain=0, modSustain2=0, modSustainTime=0, modRelease=0,
			modEnvAmountOp1=0, modDelayOp1=0, modAttackOp1=0, modDecayOp1=0, modSustainOp1=0, modSustain2Op1=0,
			modSustainTimeOp1=0, modReleaseOp1=0,
			modEnvAmountOp2=0, modDelayOp2=0, modAttackOp2=0, modDecayOp2=0, modSustainOp2=0, modSustain2Op2=0,
			modSustainTimeOp2=0,
			//modReleaseOp2=0,  // removed

			modOp1FreqRatio=0, modOp1FixedFreq=0, modOp1Detune=0, modOp1Phase=0, modOp1Amp=0,
			modOp2FreqRatio=0, modOp2FixedFreq=0, modOp2Detune=0, modOp2Phase=0, modOp2Amp=0,
			modOp3FreqRatio=0, modOp3FixedFreq=0, modOp3Detune=0, modOp3Phase=0, modOp3Amp=0,
			modOp4FreqRatio=0, modOp4FixedFreq=0, modOp4Detune=0, modOp4Phase=0, modOp4Amp=0,
			modOp5FreqRatio=0, modOp5FixedFreq=0, modOp5Detune=0, modOp5Phase=0, modOp5Amp=0,
			modOp6FreqRatio=0, modOp6FixedFreq=0, modOp6Detune=0, modOp6Phase=0, modOp6Amp=0,
			modOutLevelOp1=0, modOutLevelOp2=0, modOutLevelOp3=0,  modOutLevelOp4=0, modOutLevelOp5=0, modOutLevelOp6=0
			;

			var outEnv, envFunction, envCurve,
			outEnvOp1, envFunctionOp1, envCurveOp1, envGenFunctionOp1,
			outEnvOp2, envFunctionOp2, envCurveOp2, envGenFunctionOp2,
			intonationFunc, outFreq, pbend,
			arrFMCtls, arrFMIndices, arrFMLevels, outFM,
			op1FreqFunc, op2FreqFunc, op3FreqFunc, op4FreqFunc, op5FreqFunc, op6FreqFunc,
			op1Det, op2Det, op3Det, op4Det, op5Det, op6Det,
			del, att, dec, sus, sus2, sustime, rel,
			holdEnvAmountOp1, delOp1, attOp1, decOp1, susOp1, sus2Op1, sustimeOp1, relOp1,
			holdEnvAmountOp2, delOp2, attOp2, decOp2, susOp2, sus2Op2, sustimeOp2, relOp2,
			sumVelocity, outSound, ampCompFunction, outAmpComp;

			del = (delay + modDelay).max(0).min(1);
			att = (attackMin + ((attackMax - attackMin) * (attack + modAttack))).max(0.01).min(20);
			dec = (decayMin + ((decayMax - decayMin) * (decay + modDecay))).max(0.01).min(20);
			sus = (sustain + modSustain).max(0).min(1);
			sus2 = (sustain2 + modSustain2).max(0).min(1);
			sustime = (sustainTimeMin +
				((sustainTimeMax - sustainTimeMin) * (sustainTime + modSustainTime))).max(0.01).min(20);
			rel = (releaseMin + ((releaseMax - releaseMin) * (release + modRelease))).max(0.01).min(20);
			envCurve = this.getSynthOption(7);
			envFunction = this.getSynthOption(8);
			outEnv = EnvGen.ar(
				envFunction.value(del, att, dec, sus, sus2, sustime, rel, envCurve),
				gate,
				doneAction: 2
			);

			holdEnvAmountOp1 = (envAmountOp1 + modEnvAmountOp1).max(-1).min(1);
			delOp1 = (delayOp1 + modDelayOp1).max(0).min(1);
			attOp1 = (attackMinOp1 + ((attackMaxOp1 - attackMinOp1) * (attackOp1 + modAttackOp1))).max(0.01).min(20);
			decOp1 = (decayMinOp1 + ((decayMaxOp1 - decayMinOp1) * (decayOp1 + modDecayOp1))).max(0.01).min(20);
			susOp1 = (sustainOp1 + modSustainOp1).max(0).min(1);
			sus2Op1 = (sustain2Op1 + modSustain2Op1).max(0).min(1);
			sustimeOp1 = (sustainTimeMinOp1 +
				((sustainTimeMaxOp1 - sustainTimeMinOp1) * (sustainTimeOp1 + modSustainTimeOp1))).max(0.01).min(20);
			relOp1 = (releaseMinOp1 + ((releaseMaxOp1 - releaseMinOp1) * (releaseOp1 + modReleaseOp1))).max(0.01).min(20);
			envCurveOp1 = this.getSynthOption(9);
			envFunctionOp1 = this.getSynthOption(10);
			envGenFunctionOp1 = this.getSynthOption(11);
			outEnvOp1 = holdEnvAmountOp1 * envGenFunctionOp1.value(envFunctionOp1, delOp1, attOp1, decOp1, susOp1, sus2Op1,
				sustimeOp1, relOp1, envCurveOp1, gate);

			holdEnvAmountOp2 = (envAmountOp2 + modEnvAmountOp2).max(-1).min(1);
			delOp2 = (delayOp2 + modDelayOp2).max(0).min(1);
			attOp2 = (attackMinOp2 + ((attackMaxOp2 - attackMinOp2) * (attackOp2 + modAttackOp2))).max(0.01).min(20);
			decOp2 = (decayMinOp2 + ((decayMaxOp2 - decayMinOp2) * (decayOp2 + modDecayOp2))).max(0.01).min(20);
			susOp2 = (sustainOp2 + modSustainOp2).max(0).min(1);
			sus2Op2 = (sustain2Op2 + modSustain2Op2).max(0).min(1);
			sustimeOp2 = (sustainTimeMinOp2 +
				((sustainTimeMaxOp2 - sustainTimeMinOp2) * (sustainTimeOp2 + modSustainTimeOp2))).max(0.01).min(20);
			relOp2 = (releaseMinOp2 + ((releaseMaxOp2 - releaseMinOp2) * (releaseOp2 /* + modReleaseOp2*/ ))).max(0.01).min(20);
			envCurveOp2 = this.getSynthOption(12);
			envFunctionOp2 = this.getSynthOption(13);
			envGenFunctionOp2 = this.getSynthOption(14);
			outEnvOp2 = holdEnvAmountOp2 * envGenFunctionOp2.value(envFunctionOp2, delOp2, attOp2, decOp2, susOp2, sus2Op2,
				sustimeOp2, relOp2, envCurveOp2, gate);

			pbend = pitchbendMin + ((pitchbendMax - pitchbendMin)
				* (pitchbend + modPitchbend).max(0).min(1));

			intonationFunc = this.getSynthOption(0);
			outFreq = (2 ** ((pitchOffset + pbend) /12)) * ((intonationFunc.value((note + transpose), intKey) * keytrack)
				+ ((48 + transpose).midicps * (1-keytrack)));

			op1FreqFunc = this.getSynthOption(16);
			op2FreqFunc = this.getSynthOption(17);
			op3FreqFunc = this.getSynthOption(18);
			op4FreqFunc = this.getSynthOption(19);
			op5FreqFunc = this.getSynthOption(20);
			op6FreqFunc = this.getSynthOption(21);
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
					(modOp1Phase + op1Phase).max(0).min(1) * 2pi,
					noteLevelOp1 * (modOp1Amp + op1Amp + outEnvOp1).max(0).min(1),],
				[ op2Det * op2FreqFunc.value(op2FreqRatio, modOp2FreqRatio, op2FixedFreq, modOp2FixedFreq, outFreq),
					(modOp2Phase + op2Phase).max(0).min(1) * 2pi,
					noteLevelOp2 * (modOp2Amp + op2Amp + outEnvOp2).max(0).min(1),],
				[ op3Det * op3FreqFunc.value(op3FreqRatio, modOp3FreqRatio, op3FixedFreq, modOp3FixedFreq, outFreq),
					(modOp3Phase + op3Phase).max(0).min(1) * 2pi, noteLevelOp3 * (modOp3Amp + op3Amp).max(0).min(1),],
				[ op4Det * op4FreqFunc.value(op4FreqRatio, modOp4FreqRatio, op4FixedFreq, modOp4FixedFreq, outFreq),
					(modOp4Phase + op4Phase).max(0).min(1) * 2pi, noteLevelOp4 * (modOp4Amp + op4Amp).max(0).min(1),],
				[ op5Det * op5FreqFunc.value(op5FreqRatio, modOp5FreqRatio, op5FixedFreq, modOp5FixedFreq, outFreq),
					(modOp5Phase + op5Phase).max(0).min(1) * 2pi, noteLevelOp5 * (modOp5Amp + op5Amp).max(0).min(1),],
				[ op6Det * op6FreqFunc.value(op6FreqRatio, modOp6FreqRatio, op6FixedFreq, modOp6FixedFreq, outFreq),
					(modOp6Phase + op6Phase).max(0).min(1) * 2pi, noteLevelOp6 * (modOp6Amp + op6Amp).max(0).min(1),],
			];
			arrFMIndices = [
				[mIndFM_11, mIndFM_12, mIndFM_13, mIndFM_14, mIndFM_15, mIndFM_16,],
				[mIndFM_21, mIndFM_22, mIndFM_23, mIndFM_24, mIndFM_25, mIndFM_26,],
				[mIndFM_31, mIndFM_32, mIndFM_33, mIndFM_34, mIndFM_35, mIndFM_36,],
				[mIndFM_41, mIndFM_42, mIndFM_43, mIndFM_44, mIndFM_45, mIndFM_46,],
				[mIndFM_51, mIndFM_52, mIndFM_53, mIndFM_54, mIndFM_55, mIndFM_56,],
				[mIndFM_61, mIndFM_62, mIndFM_63, mIndFM_64, mIndFM_65, mIndFM_66,],
			]
			* 2pi;	// phase modulation is in radians so: * 2pi

			arrFMLevels = [(outLevelOp1 + modOutLevelOp1).max(0).min(1), (outLevelOp2 + modOutLevelOp2).max(0).min(1),
				(outLevelOp3 + modOutLevelOp3).max(0).min(1), (outLevelOp4 + modOutLevelOp4).max(0).min(1),
				(outLevelOp5 + modOutLevelOp5).max(0).min(1), (outLevelOp6 + modOutLevelOp6).max(0).min(1),
			];

			outFM = Mix.new(FM7.ar(arrFMCtls, arrFMIndices) * arrFMLevels);
			sumVelocity = ((velocity * 0.007874)).max(0).min(1);
			// amplitude is vel *  0.007874 approx. == 1 / 127
			ampCompFunction = this.getSynthOption(15);
			outAmpComp = ampCompFunction.value(outFreq);
			outSound = outEnv * outFM * outAmpComp * level * sumVelocity;
			Out.ar(out, TXClean.ar(LeakDC.ar(outSound)));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TestNoteVals"],
			["SynthOptionCheckBox", "Op 1 Use Fixed Freq", arrOptionData, 16, 160],
			["TXFractionSlider", "Op 1 Freq ratio", classData.ratioSpec, "op1FreqRatio", nil, 300],
			["EZsliderUnmapped", "Op 1 Fixed freq", classData.fixedFreqSpec, "op1FixedFreq", nil, 456],
			["EZsliderUnmapped", "Op 1 Detune", \bipolar.asSpec, "op1Detune"],
			["EZslider", "Op 1 Phase", \unipolar.asSpec, "op1Phase"],
			["EZslider", "Op 1 Amplitude", \unipolar.asSpec, "op1Amp"],
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
			["EZslider", "Op 6 Mod Op 1", \unipolar.asSpec, "mIndFM_61"],
			["EZslider", "Op 6 Mod Op 2", \unipolar.asSpec, "mIndFM_62"],
			["EZslider", "Op 6 Mod Op 3", \unipolar.asSpec, "mIndFM_63"],
			["EZslider", "Op 6 Mod Op 4", \unipolar.asSpec, "mIndFM_64"],
			["EZslider", "Op 6 Mod Op 5", \unipolar.asSpec, "mIndFM_65"],
			["EZslider", "Op 6 Mod Op 6", \unipolar.asSpec, "mIndFM_66"],
			["EZslider", "Op 6 Level", \unipolar.asSpec, "outLevelOp6"],
			["EZslider", "Main level", \unipolar.asSpec, "level"],
			["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 15],
			["MIDIListenCheckBox"],
			["MIDIChannelSelector"],
			["MIDINoteSelector"],
			["MIDIVelSelector"],
			["TXCheckBox", "Keyboard tracking", "keytrack"],
			["Transpose"],
			["TXMinMaxSliderSplit", "Pitch bend",
			ControlSpec(-48, 48), "pitchbend", "pitchbendMin", "pitchbendMax"],
			["PolyphonySelector"],
			["SynthOptionPopup", "Intonation", arrOptionData, 0, 250,
			{arg view; this.updateIntString(view.value)}],
			["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F",
			"F#", "G", "G#", "A", "A#", "B"], "intKey", nil, 130],
			["TXStaticText", "Note ratios",
				{TXIntonation.arrScalesText.at(arrOptions.at(1));},
			{arg view; ratioView = view}],
			["TXEnvPlot", {this.envViewValues;}, {arg view; envView = view;}],
			["EZslider", "Vol Env Pre-Delay", \unipolar.asSpec, "delay", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Vol Env Attack", classData.timeSpec, "attack", "attackMin", "attackMax",
			{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Vol Env Decay", classData.timeSpec, "decay", "decayMin", "decayMax",
			{{this.updateEnvView;}.defer;}],
			["EZslider", "Vol Env Sustain level", \unipolar.asSpec, "sustain", {{this.updateEnvView;}.defer;}],
			["EZslider", "Vol Env Sustain level 2", \unipolar.asSpec, "sustain2", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Vol Env Sustain time", classData.timeSpec, "sustainTime", "sustainTimeMin",
			"sustainTimeMax",{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Vol Env Release", classData.timeSpec, "release", "releaseMin", "releaseMax",
			{{this.updateEnvView;}.defer;}],
			["SynthOptionPopup", "Vol Env Curve", arrOptionData, 7, 150, {system.showView;}],
			["SynthOptionPopup", "Vol Env Type", arrOptionData, 8, 300],
			["SynthOptionCheckBox", "Op 1 Env on/off", arrOptionData, 11, 250],
			["EZslider", "Op 1 Env amount", ControlSpec(-1, 1), "envAmountOp1", nil, 260],
			["TXEnvPlot", {this.opEnvViewValues(1);}, {arg view; opEnvView1 = view;}],
			["EZslider", "Op 1 Pre-Delay", \unipolar.asSpec, "delayOp1", {{this.updateOpEnvView(1);}.defer;}],
			["TXMinMaxSliderSplit", "Op 1 Attack", classData.timeSpec, "attackOp1", "attackMinOp1", "attackMaxOp1",
			{{this.updateOpEnvView(1);}.defer;}],
			["TXMinMaxSliderSplit", "Op 1 Decay", classData.timeSpec, "decayOp1", "decayMinOp1", "decayMaxOp1",
			{{this.updateOpEnvView(1);}.defer;}],
			["EZslider", "Op 1 Sustain level", \unipolar.asSpec, "sustainOp1", {{this.updateOpEnvView(1);}.defer;}],
			["EZslider", "Op 1 Sustain level 2", \unipolar.asSpec, "sustain2Op1", {{this.updateOpEnvView(1);}.defer;}],
			["TXMinMaxSliderSplit", "Op 1 Sustain time", classData.timeSpec, "sustainTimeOp1", "sustainTimeMinOp1",
			"sustainTimeMaxOp1",{{this.updateOpEnvView(1);}.defer;}],
			["TXMinMaxSliderSplit", "Op 1 Release", classData.timeSpec, "releaseOp1", "releaseMinOp1", "releaseMaxOp1",
			{{this.updateOpEnvView(1);}.defer;}],
			["SynthOptionPopup", "Op 1 Curve", arrOptionData, 9, 150, {system.showView;}],
			["SynthOptionPopup", "Op 1 Env Type", arrOptionData, 10, 300],
			["SynthOptionCheckBox", "Op 2 Env on/off", arrOptionData, 14, 250],
			["EZslider", "Op 1 Env amount", ControlSpec(-1, 1), "envAmountOp2", nil, 260],
			["TXEnvPlot", {this.opEnvViewValues(2);}, {arg view; opEnvView2 = view;}],
			["EZslider", "Op 2 Pre-Delay", \unipolar.asSpec, "delayOp2", {{this.updateOpEnvView(2);}.defer;}],
			["TXMinMaxSliderSplit", "Op 2 Attack", classData.timeSpec, "attackOp2", "attackMinOp2", "attackMaxOp2",
			{{this.updateOpEnvView(2);}.defer;}],
			["TXMinMaxSliderSplit", "Op 2 Decay", classData.timeSpec, "decayOp2", "decayMinOp2", "decayMaxOp2",
			{{this.updateOpEnvView(2);}.defer;}],
			["EZslider", "Op 2 Sustain level", \unipolar.asSpec, "sustainOp2", {{this.updateOpEnvView(2);}.defer;}],
			["EZslider", "Op 2 Sustain level 2", \unipolar.asSpec, "sustain2Op2", {{this.updateOpEnvView(2);}.defer;}],
			["TXMinMaxSliderSplit", "Op 2 Sustain time", classData.timeSpec, "sustainTimeOp2", "sustainTimeMinOp2",
			"sustainTimeMaxOp2",{{this.updateOpEnvView(2);}.defer;}],
			["TXMinMaxSliderSplit", "Op 2 Release", classData.timeSpec, "releaseOp2", "releaseMinOp2", "releaseMaxOp2",
			{{this.updateOpEnvView(2);}.defer;}],
			["SynthOptionPopup", "Op 2 Curve", arrOptionData, 12, 150, {system.showView;}],
			["SynthOptionPopup", "Op 2 Env Type", arrOptionData, 13, 300],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.midiNoteInit;
		//	load the synthdef and create the Group for synths to belong to
		this.loadDefAndMakeGroup;
		//	initialise slots to max curves
		linearCurve = Array.newClear(128).seriesFill(0, 1/127);
		flat50Curve = 0.5 ! 128;
		arrVelocityCurves = flat50Curve.copy.dup(6) ++ [linearCurve];
		arrSlotData = linearCurve.copy.dup(5);
		//	overwrite default preset
		this.overwritePreset(this, "Default Settings", 0);
	}

	buildGuiSpecArray {
		var	arrGridPresetNames, arrGridPresetActions, holdCurveResetType;
		var midiNoteNames = ["notes"] ++ 103.collect({arg item; TXGetMidiNoteString(item + 24);});

		arrGridPresetNames = ["1 x 1", "2 x 2", "3 x 3", "4 x 4", "5 x 5", "6 x 6", "8 x 8", "9 x 9",
			"10 x 10", "12 x 12", "16 x 16", "24 x 24", "32 x 32"];
		arrGridPresetActions = [
			{this.setSynthArgSpec("gridRows", 1); this.setSynthArgSpec("gridCols", 1); },
			{this.setSynthArgSpec("gridRows", 2); this.setSynthArgSpec("gridCols", 2); },
			{this.setSynthArgSpec("gridRows", 3); this.setSynthArgSpec("gridCols", 3); },
			{this.setSynthArgSpec("gridRows", 4); this.setSynthArgSpec("gridCols", 4); },
			{this.setSynthArgSpec("gridRows", 5); this.setSynthArgSpec("gridCols", 5); },
			{this.setSynthArgSpec("gridRows", 6); this.setSynthArgSpec("gridCols", 6); },
			{this.setSynthArgSpec("gridRows", 8); this.setSynthArgSpec("gridCols", 8); },
			{this.setSynthArgSpec("gridRows", 9); this.setSynthArgSpec("gridCols", 9); },
			{this.setSynthArgSpec("gridRows", 10); this.setSynthArgSpec("gridCols", 10); },
			{this.setSynthArgSpec("gridRows", 12); this.setSynthArgSpec("gridCols", 12); },
			{this.setSynthArgSpec("gridRows", 16); this.setSynthArgSpec("gridCols", 16); },
			{this.setSynthArgSpec("gridRows", 24); this.setSynthArgSpec("gridCols", 24); },
			{this.setSynthArgSpec("gridRows", 32); this.setSynthArgSpec("gridCols", 32); },
		];
		guiSpecArray = [
			["ActionButton", "Operators", {displayOption = "showOperators";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showOperators")],
			["Spacer", 3],
			["ActionButton", "Velocity curves", {displayOption = "showVelocityCurve";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showVelocityCurve")],
			["Spacer", 3],
			["ActionButton", "Note levels", {displayOption = "showNoteCurve";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showNoteCurve")],
			["Spacer", 3],
			["ActionButton", "MIDI/ Note", {displayOption = "showMIDI";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
			["Spacer", 3],
			["ActionButton", "Envelopes", {displayOption = "showEnv";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showEnv")],
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
					["SynthOptionCheckBox", "Use Fixed Freq", arrOptionData, 16, 160],
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
					["SynthOptionCheckBox", "Use Fixed Freq", arrOptionData, 17, 160],
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
					["TXFractionSlider", "Freq ratio", classData.ratioSpec, "op3FreqRatio", nil, 300],
					["SynthOptionCheckBox", "Use Fixed Freq", arrOptionData, 18, 160],
					["NextLine"],
					["EZsliderUnmapped", "Fixed freq", classData.fixedFreqSpec, "op3FixedFreq", nil, 456],
					["TextBar", "Use fixed freq", 80, nil, nil, nil, \right],
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
					["SynthOptionCheckBox", "Use Fixed Freq", arrOptionData, 19, 160],
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
					["SynthOptionCheckBox", "Use Fixed Freq", arrOptionData, 20, 160],
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
					["SynthOptionCheckBox", "Use Fixed Freq", arrOptionData, 21, 160],
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
					["SynthOptionCheckBox", "", arrOptionData, 16, 60],
					["SynthOptionCheckBox", "", arrOptionData, 17, 60],
					["SynthOptionCheckBox", "", arrOptionData, 18, 60],
					["SynthOptionCheckBox", "", arrOptionData, 19, 60],
					["SynthOptionCheckBox", "", arrOptionData, 20, 60],
					["SynthOptionCheckBox", "", arrOptionData, 21, 60],
					["ActionPopup", ["actions...", "all off", "all on", "random", ], {arg view;
						case { view.value == 1 }   {
							arrOptions[16] = 0;
							arrOptions[17] = 0;
							arrOptions[18] = 0;
							arrOptions[19] = 0;
							arrOptions[20] = 0;
							arrOptions[21] = 0;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 2 }   {
							arrOptions[16] = 1;
							arrOptions[17] = 1;
							arrOptions[18] = 1;
							arrOptions[19] = 1;
							arrOptions[20] = 1;
							arrOptions[21] = 1;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 3 }   {
							arrOptions[16] = [0,1].choose;
							arrOptions[17] = [0,1].choose;
							arrOptions[18] = [0,1].choose;
							arrOptions[19] = [0,1].choose;
							arrOptions[20] = [0,1].choose;
							arrOptions[21] = [0,1].choose;
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
					["SynthOptionCheckBox", "", arrOptionData, 16, 60],
					["SynthOptionCheckBox", "", arrOptionData, 17, 60],
					["SynthOptionCheckBox", "", arrOptionData, 18, 60],
					["SynthOptionCheckBox", "", arrOptionData, 19, 60],
					["SynthOptionCheckBox", "", arrOptionData, 20, 60],
					["SynthOptionCheckBox", "", arrOptionData, 21, 60],
					["ActionPopup", ["actions...", "all off", "all on", "random", ], {arg view;
						case { view.value == 1 }   {
							arrOptions[16] = 0;
							arrOptions[17] = 0;
							arrOptions[18] = 0;
							arrOptions[19] = 0;
							arrOptions[20] = 0;
							arrOptions[21] = 0;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 2 }   {
							arrOptions[16] = 1;
							arrOptions[17] = 1;
							arrOptions[18] = 1;
							arrOptions[19] = 1;
							arrOptions[20] = 1;
							arrOptions[21] = 1;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 3 }   {
							arrOptions[16] = [0,1].choose;
							arrOptions[17] = [0,1].choose;
							arrOptions[18] = [0,1].choose;
							arrOptions[19] = [0,1].choose;
							arrOptions[20] = [0,1].choose;
							arrOptions[21] = [0,1].choose;
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
					["SynthOptionCheckBox", "", arrOptionData, 16, 60],
					["SynthOptionCheckBox", "", arrOptionData, 17, 60],
					["SynthOptionCheckBox", "", arrOptionData, 18, 60],
					["SynthOptionCheckBox", "", arrOptionData, 19, 60],
					["SynthOptionCheckBox", "", arrOptionData, 20, 60],
					["SynthOptionCheckBox", "", arrOptionData, 21, 60],
					["ActionPopup", ["actions...", "all off", "all on", "random", ], {arg view;
						case { view.value == 1 }   {
							arrOptions[16] = 0;
							arrOptions[17] = 0;
							arrOptions[18] = 0;
							arrOptions[19] = 0;
							arrOptions[20] = 0;
							arrOptions[21] = 0;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 2 }   {
							arrOptions[16] = 1;
							arrOptions[17] = 1;
							arrOptions[18] = 1;
							arrOptions[19] = 1;
							arrOptions[20] = 1;
							arrOptions[21] = 1;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 3 }   {
							arrOptions[16] = [0,1].choose;
							arrOptions[17] = [0,1].choose;
							arrOptions[18] = [0,1].choose;
							arrOptions[19] = [0,1].choose;
							arrOptions[20] = [0,1].choose;
							arrOptions[21] = [0,1].choose;
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
					["SynthOptionCheckBox", "", arrOptionData, 16, 60],
					["SynthOptionCheckBox", "", arrOptionData, 17, 60],
					["SynthOptionCheckBox", "", arrOptionData, 18, 60],
					["SynthOptionCheckBox", "", arrOptionData, 19, 60],
					["SynthOptionCheckBox", "", arrOptionData, 20, 60],
					["SynthOptionCheckBox", "", arrOptionData, 21, 60],
					["ActionPopup", ["actions...", "all off", "all on", "random", ], {arg view;
						case { view.value == 1 }   {
							arrOptions[16] = 0;
							arrOptions[17] = 0;
							arrOptions[18] = 0;
							arrOptions[19] = 0;
							arrOptions[20] = 0;
							arrOptions[21] = 0;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 2 }   {
							arrOptions[16] = 1;
							arrOptions[17] = 1;
							arrOptions[18] = 1;
							arrOptions[19] = 1;
							arrOptions[20] = 1;
							arrOptions[21] = 1;
							this.rebuildSynth;
							system.showView;
						}
						{ view.value == 3 }   {
							arrOptions[16] = [0,1].choose;
							arrOptions[17] = [0,1].choose;
							arrOptions[18] = [0,1].choose;
							arrOptions[19] = [0,1].choose;
							arrOptions[20] = [0,1].choose;
							arrOptions[21] = [0,1].choose;
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
					["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 15],
				];
			});
		}); // end:  if (displayOption == "showOperators", {
		if (displayOption == "showMIDI", {
			guiSpecArray = guiSpecArray ++[
				["MIDIListenCheckBox"],
				["NextLine"],
				["MIDIChannelSelector"],
				["NextLine"],
				["MIDINoteSelector"],
				["NextLine"],
				["MIDIVelSelector"],
				["DividingLine"],
				["TXCheckBox", "Keyboard tracking", "keytrack"],
				["DividingLine"],
				["Transpose"],
				["DividingLine"],
				["TXMinMaxSliderSplit", "Pitch bend", ControlSpec(-48, 48), "pitchbend",
					"pitchbendMin", "pitchbendMax", nil,
					[	["Bend Range Presets: ", [-2, 2]], ["Range -1 to 1", [-1, 1]], ["Range -2 to 2", [-2, 2]],
						["Range -7 to 7", [-7, 7]], ["Range -12 to 12", [-12, 12]],
						["Range -24 to 24", [-24, 24]], ["Range -48 to 48", [-48, 48]] ] ],
				["DividingLine"],
				["PolyphonySelector"],
				["DividingLine"],
				["SynthOptionPopupPlusMinus", "Intonation", arrOptionData, 0, 300,
					{arg view; this.updateIntString(view.value)}],
				["Spacer", 10],
				["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F",
					"F#", "G", "G#", "A", "A#", "B"], "intKey", nil, 120],
				["NextLine"],
				["TXStaticText", "Note ratios",
					{TXIntonation.arrScalesText.at(arrOptions.at(0));},
					{arg view; ratioView = view}],
				["DividingLine"],
				["MIDIKeyboard", {arg note; this.createSynthNote(note, testMIDIVel, 0);},
					6, 50, nil, 24, {arg note; this.releaseSynthGate(note);}, "Notes: C0 - B6"],
			];
		});
		if (displayOption == "showEnv", {
			guiSpecArray = guiSpecArray ++[
				["ActionButton", "Volume Env", {displayEnv = "showVolEnv";
					this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayEnv == "showVolEnv")],
				["Spacer", 3],
				["ActionButton", "Operator 1 Env", {displayEnv = "showOp1";
					this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayEnv == "showOp1")],
				["Spacer", 3],
				["ActionButton", "Operator 2 Env", {displayEnv = "showOp2";
					this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayEnv == "showOp2")],
				["SpacerLine", 2],
			];
			if (displayEnv == "showOp1", {
				guiSpecArray = guiSpecArray ++[
					["SynthOptionCheckBox", "Operator 1 Env", arrOptionData, 11, 130],
					["Spacer", 10],
					["EZslider", "Env amount", ControlSpec(-1, 1), "envAmountOp1", nil, 310],
					["SpacerLine", 1],
					["TXPresetPopup", "Env presets",
						TXEnvPresets.arrEnvPresetsSfx(this, 9,10, "Op1").collect({arg item, i; item.at(0)}),
						TXEnvPresets.arrEnvPresetsSfx(this, 9,10, "Op1").collect({arg item, i; item.at(1)})
					],
					["TXEnvPlot", {this.opEnvViewValues(1);}, {arg view; opEnvView1 = view;}],
					["NextLine"],
					["EZslider", "Pre-Delay", \unipolar.asSpec, "delayOp1", {{this.updateOpEnvView(1);}.defer;}],
					["NextLine"],
					["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attackOp1", "attackMinOp1", "attackMaxOp1",
						{{this.updateOpEnvView(1);}.defer;}],
					["SpacerLine", 1],
					["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decayOp1", "decayMinOp1", "decayMaxOp1",
						{{this.updateOpEnvView(1);}.defer;}],
					["SpacerLine", 1],
					["EZslider", "Sustain level", \unipolar.asSpec, "sustainOp1", {{this.updateOpEnvView(1);}.defer;}],
					["NextLine"],
					["EZslider", "Sustain level 2", \unipolar.asSpec, "sustain2Op1", {{this.updateOpEnvView(1);}.defer;}],
					["NextLine"],
					["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTimeOp1", "sustainTimeMinOp1",
						"sustainTimeMaxOp1",{{this.updateOpEnvView(1);}.defer;}],
					["SpacerLine", 1],
					["TXMinMaxSliderSplit", "Release", classData.timeSpec, "releaseOp1", "releaseMinOp1", "releaseMaxOp1",
						{{this.updateOpEnvView(1);}.defer;}],
					["NextLine"],
					["SynthOptionPopup", "Curve", arrOptionData, 9, 240, {system.showView;}],
					["Spacer", 20],
					["SynthOptionPopup", "Env Type", arrOptionData, 10, 300],
				];
			});
			if (displayEnv == "showOp2", {
				guiSpecArray = guiSpecArray ++[
					["SynthOptionCheckBox", "Operator 2 Env", arrOptionData, 14, 130],
					["Spacer", 10],
					["EZslider", "Env amount", ControlSpec(-1, 1), "envAmountOp2", nil, 310],
					["SpacerLine", 1],
					["TXPresetPopup", "Env presets",
						TXEnvPresets.arrEnvPresetsSfx(this, 12,13, "Op2").collect({arg item, i; item.at(0)}),
						TXEnvPresets.arrEnvPresetsSfx(this, 12,13, "Op2").collect({arg item, i; item.at(1)})
					],
					["TXEnvPlot", {this.opEnvViewValues(2);}, {arg view; opEnvView2 = view;}],
					["NextLine"],
					["EZslider", "Pre-Delay", \unipolar.asSpec, "delayOp2", {{this.updateOpEnvView(2);}.defer;}],
					["NextLine"],
					["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attackOp2", "attackMinOp2", "attackMaxOp2",
						{{this.updateOpEnvView(2);}.defer;}],
					["SpacerLine", 1],
					["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decayOp2", "decayMinOp2", "decayMaxOp2",
						{{this.updateOpEnvView(2);}.defer;}],
					["SpacerLine", 1],
					["EZslider", "Sustain level", \unipolar.asSpec, "sustainOp2", {{this.updateOpEnvView(2);}.defer;}],
					["NextLine"],
					["EZslider", "Sustain level 2", \unipolar.asSpec, "sustain2Op2", {{this.updateOpEnvView(2);}.defer;}],
					["NextLine"],
					["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTimeOp2", "sustainTimeMinOp2",
						"sustainTimeMaxOp2",{{this.updateOpEnvView(2);}.defer;}],
					["SpacerLine", 1],
					["TXMinMaxSliderSplit", "Release", classData.timeSpec, "releaseOp2", "releaseMinOp2", "releaseMaxOp2",
						{{this.updateOpEnvView(2);}.defer;}],
					["NextLine"],
					["SynthOptionPopup", "Curve", arrOptionData, 12, 240, {system.showView;}],
					["Spacer", 20],
					["SynthOptionPopup", "Env Type", arrOptionData, 13, 300],
				];
			});
			if (displayEnv == "showVolEnv", {
				guiSpecArray = guiSpecArray ++[
					["TXPresetPopup", "Env presets",
						TXEnvPresets.arrEnvPresets(this, 7,8).collect({arg item, i; item.at(0)}),
						TXEnvPresets.arrEnvPresets(this, 7,8).collect({arg item, i; item.at(1)})
					],
					["TXEnvPlot", {this.envViewValues;}, {arg view; envView = view;}],
					["NextLine"],
					["EZslider", "Pre-Delay", \unipolar.asSpec, "delay", {{this.updateEnvView;}.defer;}],
					["SpacerLine", 2],
					["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax",
						{{this.updateEnvView;}.defer;}],
					["SpacerLine", 2],
					["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decay", "decayMin", "decayMax",
						{{this.updateEnvView;}.defer;}],
					["SpacerLine", 2],
					["EZslider", "Sustain level", \unipolar.asSpec, "sustain", {{this.updateEnvView;}.defer;}],
					["SpacerLine", 2],
					["EZslider", "Sustain level 2", \unipolar.asSpec, "sustain2", {{this.updateEnvView;}.defer;}],
					["SpacerLine", 2],
					["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTime", "sustainTimeMin",
						"sustainTimeMax",{{this.updateEnvView;}.defer;}],
					["SpacerLine", 2],
					["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax",
						{{this.updateEnvView;}.defer;}],
					["SpacerLine", 2],
					["SynthOptionPopup", "Curve", arrOptionData, 7, 240, {system.showView;}],
					["Spacer", 20],
					["SynthOptionPopup", "Env Type", arrOptionData, 8, 300],
				];
			});
		});
		if (displayOption == "showNoteCurve", {
			guiSpecArray = guiSpecArray ++[
				["ActionButton", "Op 1", {displayNoteCurve = "noteCurveOp1";
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayNoteCurve == "noteCurveOp1")],
				["ActionButton", "Op 2", {displayNoteCurve = "noteCurveOp2";
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayNoteCurve == "noteCurveOp2")],
				["ActionButton", "Op 3", {displayNoteCurve = "noteCurveOp3";
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayNoteCurve == "noteCurveOp3")],
				["ActionButton", "Op 4", {displayNoteCurve = "noteCurveOp4";
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayNoteCurve == "noteCurveOp4")],
				["ActionButton", "Op 5", {displayNoteCurve = "noteCurveOp5";
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayNoteCurve == "noteCurveOp5")],
				["ActionButton", "Op 6", {displayNoteCurve = "noteCurveOp6";
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayNoteCurve == "noteCurveOp6")],
				["SpacerLine", 6],
				//["TextBarLeft", "Note levels:", 90],
				["ActionButton", "Copy curve",
					{classData.clipBoardNoteCurve = this.getSynthArgSpec(displayNoteCurve).deepCopy}, 90],
				["ActionButton", "Paste curve",
					{this.setSynthArgSpec(displayNoteCurve,
						classData.clipBoardNoteCurve.deepCopy); system.showView; }, 90],
				["ActionButton", "Smooth curve",
					{var inCurve, outCurve; inCurve = this.getSynthArgSpec(displayNoteCurve).deepCopy;
						outCurve = [];
						inCurve.size.do({arg item, i;
							if ((i > 0) and: (i < (inCurve.size-1)), {
								outCurve = outCurve.add((inCurve.at(i) + inCurve.at(i-1)+ inCurve.at(i+1)) / 3);
							},{
								outCurve = outCurve.add(inCurve.at(i));
							});
						});
						this.setSynthArgSpec(displayNoteCurve, outCurve);
						system.showView;},
					90, nil, TXColor.sysGuiCol2],
				["ActionButton", "Invert curve",
					{var curve; curve = this.getSynthArgSpec(displayNoteCurve).deepCopy;
						this.setSynthArgSpec(displayNoteCurve, 1 - curve);
						system.showView;},
					90, nil, TXColor.sysGuiCol2],
				["ActionButton", "Random curve",
					{this.setSynthArgSpec(displayNoteCurve, Array.rand(72, 0.0, 1.0););
						system.showView;},
					90, nil, TXColor.sysGuiCol2],
				["ActionButton", "Default curve",
					{this.setSynthArgSpec(displayNoteCurve, 1 ! 72);
						system.showView; },
					90, nil, TXColor.sysDeleteCol],
				["NextLine"],
				["TXMultiSlider", "Level", ControlSpec(0, 1), displayNoteCurve,
					72, nil, 160, nil, nil, 0, 8, 6, 7],
				["NextLine"],
				["Spacer", 0],
				["MIDIKeyboard", {arg note; this.createSynthNote(note, testMIDIVel, 0);},
					6, 50, 578, 24, {arg note; this.midiNoteRelease(note);},
					"Notes: C0 - B6"],
				["SpacerLine", 2],
			];
		});
		if (displayOption == "showVelocityCurve", {
			if (displayVelCurve == "velCurveOut", {
				holdCurveResetType = "Ramp";
			},{
				holdCurveResetType = "Flat50";
			});
			guiSpecArray = guiSpecArray ++[
				["ActionButton", "Op 1", {displayVelCurve = "velCurveOp1"; velCurveIndex = 0;
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayVelCurve == "velCurveOp1")],
				["ActionButton", "Op 2", {displayVelCurve = "velCurveOp2"; velCurveIndex = 1;
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayVelCurve == "velCurveOp2")],
				["ActionButton", "Op 3", {displayVelCurve = "velCurveOp3"; velCurveIndex = 2;
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayVelCurve == "velCurveOp3")],
				["ActionButton", "Op 4", {displayVelCurve = "velCurveOp4"; velCurveIndex = 3;
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayVelCurve == "velCurveOp4")],
				["ActionButton", "Op 5", {displayVelCurve = "velCurveOp5"; velCurveIndex = 4;
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayVelCurve == "velCurveOp5")],
				["ActionButton", "Op 6", {displayVelCurve = "velCurveOp6"; velCurveIndex = 5;
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayVelCurve == "velCurveOp6")],
				["ActionButton", "Output", {displayVelCurve = "velCurveOut"; velCurveIndex = 6;
					this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayVelCurve == "velCurveOut")],
				["SpacerLine", 6],
				["TXCurveDraw", "Vel curve", {arrVelocityCurves[velCurveIndex]},
					{arg view; arrVelocityCurves[velCurveIndex] = view.value; arrSlotData = view.arrSlotData;},
					{arrSlotData}, "Velocity", nil, nil, holdCurveResetType, "gridRows", "gridCols",
					"input level", "output level", curveDataEvent],
				["SpacerLine", 2],
				["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 99), "gridRows", {system.showView}, nil, nil, 40],
				["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 99), "gridCols", {system.showView}, nil, nil, 40],
				["NextLine"],
				["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 200],
				["SpacerLine", 2],
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

	extraSaveData { // override default method
		^[testMIDINote, testMIDIVel, testMIDITime, arrVelocityCurves, arrSlotData];
	}

	loadExtraData {arg argData;  // override default method
		testMIDINote = argData.at(0);
		testMIDIVel = argData.at(1);
		testMIDITime = argData.at(2);
		arrVelocityCurves = argData.at(3);
		arrSlotData = argData.at(4);
		// amend older data
		if (system.dataBank.savedSystemRevision < 1004, {
			this.setSynthValue("sustain2", this.getSynthArgSpec("sustain"));
			this.setSynthValue("sustain2Op1", this.getSynthArgSpec("sustainOp1"));
			this.setSynthValue("sustain2Op2", this.getSynthArgSpec("sustainOp2"));
		});
	}

	updateIntString{arg argIndex;
		if (ratioView.notNil, {
			if (ratioView.notClosed, {
				ratioView.string = TXIntonation.arrScalesText.at(argIndex);
			});
		});
	}

	envViewValues {
		var attack, attackMin, attackMax, decay, decayMin, decayMax, sustain;
		var sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax;
		var del, att, dec, sus, sus2, sustime, rel, envCurve;

		del = this.getSynthArgSpec("delay");
		attack = this.getSynthArgSpec("attack");
		attackMin = this.getSynthArgSpec("attackMin");
		attackMax = this.getSynthArgSpec("attackMax");
		att = attackMin + ((attackMax - attackMin) * attack);
		decay = this.getSynthArgSpec("decay");
		decayMin = this.getSynthArgSpec("decayMin");
		decayMax = this.getSynthArgSpec("decayMax");
		dec = decayMin + ((decayMax - decayMin) * decay);
		sus = this.getSynthArgSpec("sustain");
		sus2 = this.getSynthArgSpec("sustain2");
		sustainTime = this.getSynthArgSpec("sustainTime");
		sustainTimeMin = this.getSynthArgSpec("sustainTimeMin");
		sustainTimeMax = this.getSynthArgSpec("sustainTimeMax");
		sustime = sustainTimeMin + ((sustainTimeMax - sustainTimeMin) * sustainTime);
		release = this.getSynthArgSpec("release");
		releaseMin = this.getSynthArgSpec("releaseMin");
		releaseMax = this.getSynthArgSpec("releaseMax");
		rel = releaseMin + ((releaseMax - releaseMin) * release);
		envCurve = this.getSynthOption(7);
		^[[0, 0, 1, sus, sus2, 0].max(0.001), [del, att, dec, sustime, rel], envCurve];
	}

	opEnvViewValues {arg opNo;
		var attack, attackMin, attackMax, decay, decayMin, decayMax, sustain;
		var sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax;
		var del, att, dec, sus, sus2, sustime, rel, envCurve;

		del = this.getSynthArgSpec("delayOp" ++ opNo.asString);
		attack = this.getSynthArgSpec("attackOp" ++ opNo.asString);
		attackMin = this.getSynthArgSpec("attackMinOp" ++ opNo.asString);
		attackMax = this.getSynthArgSpec("attackMaxOp" ++ opNo.asString);
		att = attackMin + ((attackMax - attackMin) * attack);
		decay = this.getSynthArgSpec("decayOp" ++ opNo.asString);
		decayMin = this.getSynthArgSpec("decayMinOp" ++ opNo.asString);
		decayMax = this.getSynthArgSpec("decayMaxOp" ++ opNo.asString);
		dec = decayMin + ((decayMax - decayMin) * decay);
		sus = this.getSynthArgSpec("sustainOp" ++ opNo.asString);
		sus2 = this.getSynthArgSpec("sustain2Op" ++ opNo.asString);
		sustainTime = this.getSynthArgSpec("sustainTimeOp" ++ opNo.asString);
		sustainTimeMin = this.getSynthArgSpec("sustainTimeMinOp" ++ opNo.asString);
		sustainTimeMax = this.getSynthArgSpec("sustainTimeMaxOp" ++ opNo.asString);
		sustime = sustainTimeMin + ((sustainTimeMax - sustainTimeMin) * sustainTime);
		release = this.getSynthArgSpec("releaseOp" ++ opNo.asString);
		releaseMin = this.getSynthArgSpec("releaseMinOp" ++ opNo.asString);
		releaseMax = this.getSynthArgSpec("releaseMaxOp" ++ opNo.asString);
		rel = releaseMin + ((releaseMax - releaseMin) * release);
		envCurve = this.getSynthOption([9, 12][opNo-1]);
		^[[0, 0, 1, sus, sus2, 0].max(0.001), [del, att, dec, sustime, rel], envCurve];
	}

	updateEnvView {
		if (envView.respondsTo('notClosed'), {
			if (envView.notClosed, {
				envView.value = this.envViewValues;
			});
		});
	}

	updateOpEnvView {arg opNo;
		var arrViews;
		arrViews = [opEnvView1, opEnvView2];
		if (arrViews.at(opNo - 1).class.notNil, {
			if (arrViews.at(opNo - 1).notClosed, {
				arrViews.at(opNo - 1).value = this.opEnvViewValues(opNo);
			});
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

	// overwrite base class to set note levels first
	createSynthNote { arg note, vel, argEnvTime=1, seqLatencyOn=1, argNoteDetune=0;
		var noteIndex;
		// adjust noteIndex to note range C0-B6 for table lookups
		noteIndex = (note - 24).max(0).min(71);
		// set note levels
		this.setSynthArgSpec("noteLevelOp1", 2 * arrVelocityCurves[0][vel] * this.getSynthArgSpec("noteCurveOp1")[noteIndex]);
		this.setSynthArgSpec("noteLevelOp2", 2 * arrVelocityCurves[1][vel] * this.getSynthArgSpec("noteCurveOp2")[noteIndex]);
		this.setSynthArgSpec("noteLevelOp3", 2 * arrVelocityCurves[2][vel] * this.getSynthArgSpec("noteCurveOp3")[noteIndex]);
		this.setSynthArgSpec("noteLevelOp4", 2 * arrVelocityCurves[3][vel] * this.getSynthArgSpec("noteCurveOp4")[noteIndex]);
		this.setSynthArgSpec("noteLevelOp5", 2 * arrVelocityCurves[4][vel] * this.getSynthArgSpec("noteCurveOp5")[noteIndex]);
		this.setSynthArgSpec("noteLevelOp6", 2 * arrVelocityCurves[5][vel] * this.getSynthArgSpec("noteCurveOp6")[noteIndex]);
		// create note
		super.createSynthNote(note, arrVelocityCurves[6][vel] * 127, argEnvTime, seqLatencyOn, argNoteDetune);
	}

}

