// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFMSynth4 : TXModuleBase {

	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar	<guiWidth=500;
	classvar	timeSpec;
	classvar	arrFreqRangePresets;

	var 	displayOption;
	var 	displayOperator, displayEnv;
	var 	ratioView;
	var	envView, opEnvView1, opEnvView2;
	var 	<>testMIDINote = 69;
	var 	<>testMIDIVel = 100;
	var 	<>testMIDITime = 1;
	var	arrEnvPresetNames, arrEnvPresetActions;

*initClass{
	arrInstances = [];
	//	set class specific variables
	defaultName = "FM Synth";
	moduleRate = "audio";
	moduleType = "groupsource";
	arrCtlSCInBusSpecs = [
		["Pitch bend", 1, "modPitchbend", 0],
		["Op1 Freq Ratio", 1, "mmOp1FreqRatio", 0],
		["Op1 Phase",1, "mmOp1Phase", 0],
		["Op1 Amp",1, "mmOp1Amp", 0],
		["Op2 Freq Ratio", 1, "mmOp2FreqRatio", 0],
		["Op2 Phase",1, "mmOp2Phase", 0],
		["Op2 Amp",1, "mmOp2Amp", 0],
		["Op3 Freq Ratio", 1, "mmOp3FreqRatio", 0],
		["Op3 Phase",1, "mmOp3Phase", 0],
		["Op3 Amp",1, "mmOp3Amp", 0],
		["Op4 Freq Ratio", 1, "mmOp4FreqRatio", 0],
		["Op4 Phase",1, "mmOp4Phase", 0],
		["Op4 Amp",1, "mmOp4Amp", 0],
		["Op5 Freq Ratio", 1, "mmOp5FreqRatio", 0],
		["Op5 Phase",1, "mmOp5Phase", 0],
		["Op5 Amp",1, "mmOp5Amp", 0],
		["Op6 Freq Ratio", 1, "mmOp6FreqRatio", 0],
		["Op6 Phase",1, "mmOp6Phase", 0],
		["Op6 Amp",1, "mmOp6Amp", 0],
		["Mod Ind 1 -> 1", 1, "mmModFM_11", 0],
		["Mod Ind 2 -> 1", 1, "mmModFM_12", 0],
		["Mod Ind 3 -> 1", 1, "mmModFM_13", 0],
		["Mod Ind 4 -> 1", 1, "mmModFM_14", 0],
		["Mod Ind 5 -> 1", 1, "mmModFM_15", 0],
		["Mod Ind 6 -> 1", 1, "mmModFM_16", 0],
		["Mod Ind 1 -> 2", 1, "mmModFM_21", 0],
		["Mod Ind 2 -> 2", 1, "mmModFM_22", 0],
		["Mod Ind 3 -> 2", 1, "mmModFM_23", 0],
		["Mod Ind 4 -> 2", 1, "mmModFM_24", 0],
		["Mod Ind 5 -> 2", 1, "mmModFM_25", 0],
		["Mod Ind 6 -> 2", 1, "mmModFM_26", 0],
		["Mod Ind 1 -> 3", 1, "mmModFM_31", 0],
		["Mod Ind 2 -> 3", 1, "mmModFM_32", 0],
		["Mod Ind 3 -> 3", 1, "mmModFM_33", 0],
		["Mod Ind 4 -> 3", 1, "mmModFM_34", 0],
		["Mod Ind 5 -> 3", 1, "mmModFM_35", 0],
		["Mod Ind 6 -> 3", 1, "mmModFM_36", 0],
		["Mod Ind 1 -> 4", 1, "mmModFM_41", 0],
		["Mod Ind 2 -> 4", 1, "mmModFM_42", 0],
		["Mod Ind 3 -> 4", 1, "mmModFM_43", 0],
		["Mod Ind 4 -> 4", 1, "mmModFM_44", 0],
		["Mod Ind 5 -> 4", 1, "mmModFM_45", 0],
		["Mod Ind 6 -> 4", 1, "mmModFM_46", 0],
		["Mod Ind 1 -> 5", 1, "mmModFM_51", 0],
		["Mod Ind 2 -> 5", 1, "mmModFM_52", 0],
		["Mod Ind 3 -> 5", 1, "mmModFM_53", 0],
		["Mod Ind 4 -> 5", 1, "mmModFM_54", 0],
		["Mod Ind 5 -> 5", 1, "mmModFM_55", 0],
		["Mod Ind 6 -> 5", 1, "mmModFM_56", 0],
		["Mod Ind 1 -> 6", 1, "mmModFM_61", 0],
		["Mod Ind 2 -> 6", 1, "mmModFM_62", 0],
		["Mod Ind 3 -> 6", 1, "mmModFM_63", 0],
		["Mod Ind 4 -> 6", 1, "mmModFM_64", 0],
		["Mod Ind 5 -> 6", 1, "mmModFM_65", 0],
		["Mod Ind 6 -> 6", 1, "mmModFM_66", 0],
		["Vol Env Delay", 1, "modDelay", 0],
		["Vol Env Attack", 1, "modAttack", 0],
		["Vol Env Decay", 1, "modDecay", 0],
		["Vol Env Sustain level", 1, "modSustain", 0],
		["Vol Env Sustain time", 1, "modSustainTime", 0],
		["Vol Env Release", 1, "modRelease", 0],
		["Op 1 Env Amount", 1, "modEnvAmountOp1", 0],
		["Op 1 Env Delay", 1, "modDelayOp1", 0],
		["Op 1 Env Attack", 1, "modAttackOp1", 0],
		["Op 1 Env Decay", 1, "modDecayOp1", 0],
		["Op 1 Env Sustain level", 1, "modSustainOp1", 0],
		["Op 1 Env Sustain time", 1, "modSustainTimeOp1", 0],
		["Op 1 Env Release", 1, "modReleaseOp1", 0],
		["Op 2 Env Amount", 1, "modEnvAmountOp2", 0],
		["Op 2 Env Delay", 1, "modDelayOp2", 0],
		["Op 2 Env Attack", 1, "modAttackOp2", 0],
		["Op 2 Env Decay", 1, "modDecayOp2", 0],
		["Op 2 Env Sustain level", 1, "modSustainOp2", 0],
		["Op 2 Env Sustain time", 1, "modSustainTimeOp2", 0],
		["Op 2 Env Release", 1, "modReleaseOp2", 0],

	];
	noOutChannels = 1;
	arrOutBusSpecs = [
		["Out", [0]]
	];
	timeSpec = ControlSpec(0.001, 20);

}

*new{ arg argInstName;
	 ^super.new.init(argInstName);
}

init {arg argInstName;
	//	set  class specific instance variables
	displayOption = "showOperators";
	displayOperator = "showOp1";
	displayEnv = "showVolEnv";
	arrSynthArgSpecs = [
		["out", 0, 0],
		["gate", 1, 0],
		["note", 0, 0],
		["velocity", 0, 0],
		["keytrack", 1, \ir],
		["transpose", 0, \ir],
		["pitchbend", 0.5, defLagTime],
		["pitchbendMin", -2, defLagTime],
		["pitchbendMax", 2, defLagTime],
		["level", 0.5, defLagTime],

		["op1FreqRatio", 1, defLagTime],
		["op1Phase", 0, defLagTime],
		["op1Amp", 0.5, defLagTime],
		["op2FreqRatio", 2, defLagTime],
		["op2Phase", 0, defLagTime],
		["op2Amp", 0.5, defLagTime],
		["op3FreqRatio", 3, defLagTime],
		["op3Phase", 0, defLagTime],
		["op3Amp", 0.5, defLagTime],
		["op4FreqRatio", 4, defLagTime],
		["op4Phase", 0, defLagTime],
		["op4Amp", 0.5, defLagTime],
		["op5FreqRatio", 0.5, defLagTime],
		["op5Phase", 0, defLagTime],
		["op5Amp", 0.5, defLagTime],
		["op6FreqRatio", 1.001, defLagTime],
		["op6Phase", 0, defLagTime],
		["op6Amp", 0.5, defLagTime],
		["modFM_11", 0, defLagTime],
		["modFM_12", 0.5, defLagTime],
		["modFM_13", 0.25, defLagTime],
		["modFM_14", 0, defLagTime],
		["modFM_15", 0, defLagTime],
		["modFM_16", 0, defLagTime],
		["modFM_21", 0, defLagTime],
		["modFM_22", 0, defLagTime],
		["modFM_23", 0, defLagTime],
		["modFM_24", 0, defLagTime],
		["modFM_25", 0, defLagTime],
		["modFM_26", 0, defLagTime],
		["modFM_31", 0, defLagTime],
		["modFM_32", 0, defLagTime],
		["modFM_33", 0, defLagTime],
		["modFM_34", 0, defLagTime],
		["modFM_35", 0, defLagTime],
		["modFM_36", 0, defLagTime],
		["modFM_41", 0, defLagTime],
		["modFM_42", 0, defLagTime],
		["modFM_43", 0, defLagTime],
		["modFM_44", 0, defLagTime],
		["modFM_45", 0, defLagTime],
		["modFM_46", 0, defLagTime],
		["modFM_51", 0, defLagTime],
		["modFM_52", 0, defLagTime],
		["modFM_53", 0, defLagTime],
		["modFM_54", 0, defLagTime],
		["modFM_55", 0, defLagTime],
		["modFM_56", 0, defLagTime],
		["modFM_61", 0, defLagTime],
		["modFM_62", 0, defLagTime],
		["modFM_63", 0, defLagTime],
		["modFM_64", 0, defLagTime],
		["modFM_65", 0, defLagTime],
		["modFM_66", 0, defLagTime],

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
		["modSustainTime", 0, \ir],
		["modRelease", 0, \ir],
		["modEnvAmountOp1", 0, \ir],
		["modDelayOp1", 0, \ir],
		["modAttackOp1", 0, \ir],
		["modDecayOp1", 0, \ir],
		["modSustainOp1", 0, \ir],
		["modSustainTimeOp1", 0, \ir],
		["modReleaseOp1", 0, \ir],
		["modEnvAmountOp2", 0, \ir],
		["modDelayOp2", 0, \ir],
		["modAttackOp2", 0, \ir],
		["modDecayOp2", 0, \ir],
		["modSustainOp2", 0, \ir],
		["modSustainTimeOp2", 0, \ir],
		["modReleaseOp2", 0, \ir],

		["mmOp0FreqRatio", 0, defLagTime],
		["mmOp0Phase", 0, defLagTime],
		["mmOp0Amp", 0, defLagTime],
		["mmOp2FreqRatio", 0, defLagTime],
		["mmOp2Phase", 0, defLagTime],
		["mmOp2Amp", 0, defLagTime],
		["mmOp3FreqRatio", 0, defLagTime],
		["mmOp3Phase", 0, defLagTime],
		["mmOp3Amp", 0, defLagTime],
		["mmOp4FreqRatio", 0, defLagTime],
		["mmOp4Phase", 0, defLagTime],
		["mmOp4Amp", 0, defLagTime],
		["mmOp5FreqRatio", 0, defLagTime],
		["mmOp5Phase", 0, defLagTime],
		["mmOp5Amp", 0, defLagTime],
		["mmOp6FreqRatio", 0, defLagTime],
		["mmOp6Phase", 0, defLagTime],
		["mmOp6Amp", 0, defLagTime],
		["mmModFM_11", 0, defLagTime],
		["mmModFM_12", 0, defLagTime],
		["mmModFM_13", 0, defLagTime],
		["mmModFM_14", 0, defLagTime],
		["mmModFM_15", 0, defLagTime],
		["mmModFM_16", 0, defLagTime],
		["mmModFM_21", 0, defLagTime],
		["mmModFM_22", 0, defLagTime],
		["mmModFM_23", 0, defLagTime],
		["mmModFM_24", 0, defLagTime],
		["mmModFM_25", 0, defLagTime],
		["mmModFM_26", 0, defLagTime],
		["mmModFM_31", 0, defLagTime],
		["mmModFM_32", 0, defLagTime],
		["mmModFM_33", 0, defLagTime],
		["mmModFM_34", 0, defLagTime],
		["mmModFM_35", 0, defLagTime],
		["mmModFM_36", 0, defLagTime],
		["mmModFM_41", 0, defLagTime],
		["mmModFM_42", 0, defLagTime],
		["mmModFM_43", 0, defLagTime],
		["mmModFM_44", 0, defLagTime],
		["mmModFM_45", 0, defLagTime],
		["mmModFM_46", 0, defLagTime],
		["mmModFM_51", 0, defLagTime],
		["mmModFM_52", 0, defLagTime],
		["mmModFM_53", 0, defLagTime],
		["mmModFM_54", 0, defLagTime],
		["mmModFM_55", 0, defLagTime],
		["mmModFM_56", 0, defLagTime],
		["mmModFM_61", 0, defLagTime],
		["mmModFM_62", 0, defLagTime],
		["mmModFM_63", 0, defLagTime],
		["mmModFM_64", 0, defLagTime],
		["mmModFM_65", 0, defLagTime],
		["mmModFM_66", 0, defLagTime],
	];
	arrOptions = 0 ! 15;
	arrOptionData = [
		TXIntonation.arrOptionData,
		[
			["Off", { 0; }],
			["On", TXLFO.lfoFunction],
		],
		TXLFO.arrOptionData,
		TXLFO.arrLFOOutputRanges,
		[
			["Off", { 0; }],
			["On", TXLFO.lfoFunction],
		],
		TXLFO.arrOptionData,
		TXLFO.arrLFOOutputRanges,
		// amp env
		TXEnvLookup.arrSlopeOptionData,
		TXEnvLookup.arrSustainOptionData,
		// op env 1
		TXEnvLookup.arrSlopeOptionData,
		TXEnvLookup.arrSustainOptionData,
		[	["Off", { 0; }],
			["On", {arg envFunction, del, att, dec, sus, sustime, rel, envCurve, gate;
				EnvGen.ar(
					envFunction.value(del, att, dec, sus, sustime, rel, envCurve),
					gate,
					doneAction: 0
				);
			}],
		],
		// op env 2
		TXEnvLookup.arrSlopeOptionData,
		TXEnvLookup.arrSustainOptionData,
		[	["Off", { 0; }],
			["On", {arg envFunction, del, att, dec, sus, sustime, rel, envCurve, gate;
				EnvGen.ar(
					envFunction.value(del, att, dec, sus, sustime, rel, envCurve),
					gate,
					doneAction: 0
				);
			}],
		],
	];
	synthDefFunc = {
		arg out, gate, note, velocity, keytrack, transpose, pitchbend, pitchbendMin, pitchbendMax,
			level,

			op1FreqRatio, op1Phase, op1Amp,
			op2FreqRatio, op2Phase, op2Amp,
			op3FreqRatio, op3Phase, op3Amp,
			op4FreqRatio, op4Phase, op4Amp,
			op5FreqRatio, op5Phase, op5Amp,
			op6FreqRatio, op6Phase, op6Amp,
			modFM_11, modFM_12, modFM_13, modFM_14, modFM_15, modFM_16,
			modFM_21, modFM_22, modFM_23, modFM_24, modFM_25, modFM_26,
			modFM_31, modFM_32, modFM_33, modFM_34, modFM_35, modFM_36,
			modFM_41, modFM_42, modFM_43, modFM_44, modFM_45, modFM_46,
			modFM_51, modFM_52, modFM_53, modFM_54, modFM_55, modFM_56,
			modFM_61, modFM_62, modFM_63, modFM_64, modFM_65, modFM_66,
			outLevelOp1, outLevelOp2, outLevelOp3, outLevelOp4, outLevelOp5, outLevelOp6,

			envtime, delay, attack, attackMin, attackMax, decay, decayMin, decayMax, sustain,
			sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax,

			envtimeOp1, envAmountOp1, delayOp1, attackOp1, attackMinOp1, attackMaxOp1,
			decayOp1, decayMinOp1, decayMaxOp1, sustainOp1, sustainTimeOp1,
			sustainTimeMinOp1, sustainTimeMaxOp1, releaseOp1, releaseMinOp1, releaseMaxOp1,
			envtimeOp2, envAmountOp2, delayOp2, attackOp2, attackMinOp2, attackMaxOp2,
			decayOp2, decayMinOp2, decayMaxOp2, sustainOp2, sustainTimeOp2,
			sustainTimeMinOp2, sustainTimeMaxOp2, releaseOp2, releaseMinOp2, releaseMaxOp2,

			intKey,

			modPitchbend,
			modDelay, modAttack, modDecay, modSustain, modSustainTime, modRelease,
			modEnvAmountOp1, modDelayOp1, modAttackOp1, modDecayOp1, modSustainOp1,
			modSustainTimeOp1, modReleaseOp1,
			modEnvAmountOp2, modDelayOp2, modAttackOp2, modDecayOp2, modSustainOp2,
			modSustainTimeOp2, modReleaseOp2,

			mmOp1FreqRatio, mmOp1Phase, mmOp1Amp,
			mmOp2FreqRatio, mmOp2Phase, mmOp2Amp,
			mmOp3FreqRatio, mmOp3Phase, mmOp3Amp,
			mmOp4FreqRatio, mmOp4Phase, mmOp4Amp,
			mmOp5FreqRatio, mmOp5Phase, mmOp5Amp,
			mmOp6FreqRatio, mmOp6Phase, mmOp6Amp,
			mmModFM_11, mmModFM_12, mmModFM_13, mmModFM_14, mmModFM_15, mmModFM_16,
			mmModFM_21, mmModFM_22, mmModFM_23, mmModFM_24, mmModFM_25, mmModFM_26,
			mmModFM_31, mmModFM_32, mmModFM_33, mmModFM_34, mmModFM_35, mmModFM_36,
			mmModFM_41, mmModFM_42, mmModFM_43, mmModFM_44, mmModFM_45, mmModFM_46,
			mmModFM_51, mmModFM_52, mmModFM_53, mmModFM_54, mmModFM_55, mmModFM_56,
			mmModFM_61, mmModFM_62, mmModFM_63, mmModFM_64, mmModFM_65, mmModFM_66;

		var outEnv, envFunction, envCurve,
			outEnvOp1, envFunctionOp1, envCurveOp1, envGenFunctionOp1,
			outEnvOp2, envFunctionOp2, envCurveOp2, envGenFunctionOp2,
			intonationFunc, outFreq, pbend,
			arrFMCtls, arrFMMods, arrFMLevels, outFM,
			del, att, dec, sus, sustime, rel,
			holdEnvAmountOp1, delOp1, attOp1, decOp1, susOp1, sustimeOp1, relOp1,
			holdEnvAmountOp2, delOp2, attOp2, decOp2, susOp2, sustimeOp2, relOp2,
			sumVelocity, outSound;

		del = (delay + modDelay).max(0).min(1);
		att = (attackMin + ((attackMax - attackMin) * (attack + modAttack))).max(0.001).min(20);
		dec = (decayMin + ((decayMax - decayMin) * (decay + modDecay))).max(0.001).min(20);
		sus = (sustain + modSustain).max(0).min(1);
		sustime = (sustainTimeMin +
			((sustainTimeMax - sustainTimeMin) * (sustainTime + modSustainTime))).max(0.001).min(20);
		rel = (releaseMin + ((releaseMax - releaseMin) * (release + modRelease))).max(0.001).min(20);
		envCurve = this.getSynthOption(7);
		envFunction = this.getSynthOption(8);
		outEnv = EnvGen.ar(
			envFunction.value(del, att, dec, sus, sustime, rel, envCurve),
			gate,
			doneAction: 2
		);

		holdEnvAmountOp1 = (envAmountOp1 + modEnvAmountOp1).max(-1).min(1);
		delOp1 = (delayOp1 + modDelayOp1).max(0).min(1);
		attOp1 = (attackMinOp1 + ((attackMaxOp1 - attackMinOp1) * (attackOp1 + modAttackOp1))).max(0.001).min(20);
		decOp1 = (decayMinOp1 + ((decayMaxOp1 - decayMinOp1) * (decayOp1 + modDecayOp1))).max(0.001).min(20);
		susOp1 = (sustainOp1 + modSustainOp1).max(0).min(1);
		sustimeOp1 = (sustainTimeMinOp1 +
			((sustainTimeMaxOp1 - sustainTimeMinOp1) * (sustainTimeOp1 + modSustainTimeOp1))).max(0.001).min(20);
		relOp1 = (releaseMinOp1 + ((releaseMaxOp1 - releaseMinOp1) * (releaseOp1 + modReleaseOp1))).max(0.001).min(20);
		envCurveOp1 = this.getSynthOption(9);
		envFunctionOp1 = this.getSynthOption(10);
		envGenFunctionOp1 = this.getSynthOption(11);
		outEnvOp1 = holdEnvAmountOp1 * envGenFunctionOp1.value(envFunctionOp1, delOp1, attOp1, decOp1, susOp1,
			sustimeOp1, relOp1, envCurveOp1, gate);

		holdEnvAmountOp2 = (envAmountOp2 + modEnvAmountOp2).max(-1).min(1);
		delOp2 = (delayOp2 + modDelayOp2).max(0).min(1);
		attOp2 = (attackMinOp2 + ((attackMaxOp2 - attackMinOp2) * (attackOp2 + modAttackOp2))).max(0.001).min(20);
		decOp2 = (decayMinOp2 + ((decayMaxOp2 - decayMinOp2) * (decayOp2 + modDecayOp2))).max(0.001).min(20);
		susOp2 = (sustainOp2 + modSustainOp2).max(0).min(1);
		sustimeOp2 = (sustainTimeMinOp2 +
			((sustainTimeMaxOp2 - sustainTimeMinOp2) * (sustainTimeOp2 + modSustainTimeOp2))).max(0.001).min(20);
		relOp2 = (releaseMinOp2 + ((releaseMaxOp2 - releaseMinOp2) * (releaseOp2 + modReleaseOp2))).max(0.001).min(20);
		envCurveOp2 = this.getSynthOption(12);
		envFunctionOp2 = this.getSynthOption(13);
		envGenFunctionOp2 = this.getSynthOption(14);
		outEnvOp2 = holdEnvAmountOp2 * envGenFunctionOp2.value(envFunctionOp2, delOp2, attOp2, decOp2, susOp2,
			sustimeOp2, relOp2, envCurveOp2, gate);

		pbend = pitchbendMin + ((pitchbendMax - pitchbendMin)
				* (pitchbend + modPitchbend).max(0).min(1));

		intonationFunc = this.getSynthOption(0);
		outFreq = (2 ** (pbend /12)) * ((intonationFunc.value((note + transpose), intKey) * keytrack)
			+ ((48 + transpose).midicps * (1-keytrack)));
		arrFMCtls = [
			[ ((16 ** mmOp1FreqRatio) * op1FreqRatio) * outFreq,
			(mmOp1Phase + op1Phase).max(0).min(1) * 2pi,(mmOp1Amp + op1Amp + outEnvOp1).max(0).min(1),],
			[ ((16 ** mmOp2FreqRatio) * op2FreqRatio) * outFreq,
			(mmOp2Phase + op2Phase).max(0).min(1) * 2pi,(mmOp2Amp + op2Amp + outEnvOp2).max(0).min(1),],
			[ ((16 ** mmOp3FreqRatio) * op3FreqRatio) * outFreq,
			(mmOp3Phase + op3Phase).max(0).min(1) * 2pi,(mmOp3Amp + op3Amp).max(0).min(1),],
			[ ((16 ** mmOp4FreqRatio) * op4FreqRatio) * outFreq,
			(mmOp4Phase + op4Phase).max(0).min(1) * 2pi,(mmOp4Amp + op4Amp).max(0).min(1),],
			[ ((16 ** mmOp5FreqRatio) * op5FreqRatio) * outFreq,
			(mmOp5Phase + op5Phase).max(0).min(1) * 2pi,(mmOp5Amp + op5Amp).max(0).min(1),],
			[ ((16 ** mmOp6FreqRatio) * op6FreqRatio) * outFreq,
			(mmOp6Phase + op6Phase).max(0).min(1) * 2pi,(mmOp6Amp + op6Amp).max(0).min(1),],
		];
		arrFMMods = [
			[(modFM_11 + mmModFM_11).max(0).min(1), (modFM_12 + mmModFM_12).max(0).min(1),
				(modFM_13 + mmModFM_13).max(0).min(1), (modFM_14 + mmModFM_14).max(0).min(1),
				(modFM_15 + mmModFM_15).max(0).min(1), (modFM_16 + mmModFM_16).max(0).min(1), ],
			[(modFM_21 + mmModFM_21).max(0).min(1), (modFM_22 + mmModFM_22).max(0).min(1),
				(modFM_23 + mmModFM_23).max(0).min(1), (modFM_24 + mmModFM_24).max(0).min(1),
				(modFM_25 + mmModFM_25).max(0).min(1), (modFM_26 + mmModFM_26).max(0).min(1), ],
			[(modFM_31 + mmModFM_31).max(0).min(1), (modFM_32 + mmModFM_32).max(0).min(1),
				(modFM_33 + mmModFM_33).max(0).min(1), (modFM_34 + mmModFM_34).max(0).min(1),
				(modFM_35 + mmModFM_35).max(0).min(1), (modFM_36 + mmModFM_36).max(0).min(1), ],
			[(modFM_41 + mmModFM_41).max(0).min(1), (modFM_42 + mmModFM_42).max(0).min(1),
				(modFM_43 + mmModFM_43).max(0).min(1), (modFM_44 + mmModFM_44).max(0).min(1),
				(modFM_45 + mmModFM_45).max(0).min(1), (modFM_46 + mmModFM_46).max(0).min(1), ],
			[(modFM_51 + mmModFM_51).max(0).min(1), (modFM_52 + mmModFM_52).max(0).min(1),
				(modFM_53 + mmModFM_53).max(0).min(1), (modFM_54 + mmModFM_54).max(0).min(1),
				(modFM_55 + mmModFM_55).max(0).min(1), (modFM_56 + mmModFM_56).max(0).min(1), ],
			[(modFM_61 + mmModFM_61).max(0).min(1), (modFM_62 + mmModFM_62).max(0).min(1),
				(modFM_63 + mmModFM_63).max(0).min(1), (modFM_64 + mmModFM_64).max(0).min(1),
				(modFM_65 + mmModFM_65).max(0).min(1), (modFM_66 + mmModFM_66).max(0).min(1), ],
		]
		 * 2pi;	// phase modulation is in radians so: * 2pi

		arrFMLevels = [outLevelOp1, outLevelOp2, outLevelOp3, outLevelOp4, outLevelOp5, outLevelOp6];

		outFM = Mix.new(FM7.ar(arrFMCtls, arrFMMods) * arrFMLevels);

		sumVelocity = ((velocity * 0.007874)).max(0).min(1);
		// amplitude is vel *  0.007874 approx. == 1 / 127

		outSound = outEnv * outFM * level * sumVelocity;
		Out.ar(out, TXClean.ar(outSound));
	};
	this.buildGuiSpecArray;
	arrActionSpecs = this.buildActionSpecs([
		["TestNoteVals"],
		["EZslider", "Op 1 Freq ratio", ControlSpec(0.001, 16, 'amp'), "op1FreqRatio"],
		["EZslider", "Op 1 Phase", ControlSpec(0,1), "op1Phase"],
		["EZslider", "Op 1 Amplitude", ControlSpec(0,1), "op1Amp"],
		["SpacerLine", 4],
		["EZslider", "Op 1 Mod Op 1", ControlSpec(0,1), "modFM_11"],
		["EZslider", "Op 1 Mod Op 2", ControlSpec(0,1), "modFM_12"],
		["EZslider", "Op 1 Mod Op 3", ControlSpec(0,1), "modFM_13"],
		["EZslider", "Op 1 Mod Op 4", ControlSpec(0,1), "modFM_14"],
		["EZslider", "Op 1 Mod Op 5", ControlSpec(0,1), "modFM_15"],
		["EZslider", "Op 1 Mod Op 6", ControlSpec(0,1), "modFM_16"],
		["EZslider", "Op 1 Level", ControlSpec(0,1), "outLevelOp1"],
		["EZslider", "Op 2 Freq ratio", ControlSpec(0.001, 16, 'amp'), "op2FreqRatio"],
		["EZslider", "Op 2 Phase", ControlSpec(0,1), "op2Phase"],
		["EZslider", "Op 2 Amplitude", ControlSpec(0,1), "op2Amp"],
		["SpacerLine", 4],
		["EZslider", "Op 2 Mod Op 1", ControlSpec(0,1), "modFM_21"],
		["EZslider", "Op 2 Mod Op 2", ControlSpec(0,1), "modFM_22"],
		["EZslider", "Op 2 Mod Op 3", ControlSpec(0,1), "modFM_23"],
		["EZslider", "Op 2 Mod Op 4", ControlSpec(0,1), "modFM_24"],
		["EZslider", "Op 2 Mod Op 5", ControlSpec(0,1), "modFM_25"],
		["EZslider", "Op 2 Mod Op 6", ControlSpec(0,1), "modFM_26"],
		["EZslider", "Op 2 Level", ControlSpec(0,1), "outLevelOp2"],
		["EZslider", "Op 3 Freq ratio", ControlSpec(0.001, 16, 'amp'), "op3FreqRatio"],
		["EZslider", "Op 3 Phase", ControlSpec(0,1), "op3Phase"],
		["EZslider", "Op 3 Amplitude", ControlSpec(0,1), "op3Amp"],
		["SpacerLine", 4],
		["EZslider", "Op 3 Mod Op 1", ControlSpec(0,1), "modFM_31"],
		["EZslider", "Op 3 Mod Op 2", ControlSpec(0,1), "modFM_32"],
		["EZslider", "Op 3 Mod Op 3", ControlSpec(0,1), "modFM_33"],
		["EZslider", "Op 3 Mod Op 4", ControlSpec(0,1), "modFM_34"],
		["EZslider", "Op 3 Mod Op 5", ControlSpec(0,1), "modFM_35"],
		["EZslider", "Op 3 Mod Op 6", ControlSpec(0,1), "modFM_36"],
		["EZslider", "Op 3 Level", ControlSpec(0,1), "outLevelOp3"],
		["EZslider", "Op 4 Freq ratio", ControlSpec(0.001, 16, 'amp'), "op4FreqRatio"],
		["EZslider", "Op 4 Phase", ControlSpec(0,1), "op4Phase"],
		["EZslider", "Op 4 Amplitude", ControlSpec(0,1), "op4Amp"],
		["SpacerLine", 4],
		["EZslider", "Op 4 Mod Op 1", ControlSpec(0,1), "modFM_41"],
		["EZslider", "Op 4 Mod Op 2", ControlSpec(0,1), "modFM_42"],
		["EZslider", "Op 4 Mod Op 3", ControlSpec(0,1), "modFM_43"],
		["EZslider", "Op 4 Mod Op 4", ControlSpec(0,1), "modFM_44"],
		["EZslider", "Op 4 Mod Op 5", ControlSpec(0,1), "modFM_45"],
		["EZslider", "Op 4 Mod Op 6", ControlSpec(0,1), "modFM_46"],
		["EZslider", "Op 4 Level", ControlSpec(0,1), "outLevelOp4"],
		["EZslider", "Op 5 Freq ratio", ControlSpec(0.001, 16, 'amp'), "op5FreqRatio"],
		["EZslider", "Op 5 Phase", ControlSpec(0,1), "op5Phase"],
		["EZslider", "Op 5 Amplitude", ControlSpec(0,1), "op5Amp"],
		["SpacerLine", 4],
		["EZslider", "Op 5 Mod Op 1", ControlSpec(0,1), "modFM_51"],
		["EZslider", "Op 5 Mod Op 2", ControlSpec(0,1), "modFM_52"],
		["EZslider", "Op 5 Mod Op 3", ControlSpec(0,1), "modFM_53"],
		["EZslider", "Op 5 Mod Op 4", ControlSpec(0,1), "modFM_54"],
		["EZslider", "Op 5 Mod Op 5", ControlSpec(0,1), "modFM_55"],
		["EZslider", "Op 5 Mod Op 6", ControlSpec(0,1), "modFM_56"],
		["EZslider", "Op 5 Level", ControlSpec(0,1), "outLevelOp5"],
		["EZslider", "Op 6 Freq ratio", ControlSpec(0.001, 16, 'amp'), "op6FreqRatio"],
		["EZslider", "Op 6 Phase", ControlSpec(0,1), "op6Phase"],
		["EZslider", "Op 6 Amplitude", ControlSpec(0,1), "op6Amp"],
		["SpacerLine", 4],
		["EZslider", "Op 6 Mod Op 1", ControlSpec(0,1), "modFM_61"],
		["EZslider", "Op 6 Mod Op 2", ControlSpec(0,1), "modFM_62"],
		["EZslider", "Op 6 Mod Op 3", ControlSpec(0,1), "modFM_63"],
		["EZslider", "Op 6 Mod Op 4", ControlSpec(0,1), "modFM_64"],
		["EZslider", "Op 6 Mod Op 5", ControlSpec(0,1), "modFM_65"],
		["EZslider", "Op 6 Mod Op 6", ControlSpec(0,1), "modFM_66"],
		["EZslider", "Op 6 Level", ControlSpec(0,1), "outLevelOp6"],

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
		["TXMinMaxSliderSplit", "Pitch bend",
			ControlSpec(-48, 48), "pitchbend", "pitchbendMin", "pitchbendMax"],
		["DividingLine"],
		["PolyphonySelector"],
		["DividingLine"],
		["SynthOptionPopup", "Intonation", arrOptionData, 0, 250,
			{arg view; this.updateIntString(view.value)}],
		["Spacer", 10],
		["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F",
			"F#", "G", "G#", "A", "A#", "B"], "intKey", nil, 130],
		["NextLine"],
		["TXStaticText", "Note ratios",
			{TXIntonation.arrScalesText.at(arrOptions.at(1));},
			{arg view; ratioView = view}],
		["DividingLine"],
		["MIDIKeyboard", {arg note; this.createSynthNote(note, testMIDIVel, 0);},
				5, 60, nil, 36, {arg note; this.releaseSynthGate(note);}],

		["TXPresetPopup", "Vol Env presets",
			TXEnvPresets.arrEnvPresets(this, 7,8).collect({arg item, i; item.at(0)}),
			TXEnvPresets.arrEnvPresets(this, 7,8).collect({arg item, i; item.at(1)})
		],
		["TXEnvDisplay", {this.envViewValues;}, {arg view; envView = view;}],
		["NextLine"],
		["EZslider", "Vol Env Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView;}.defer;}],
		["TXMinMaxSliderSplit", "Vol Env Attack", timeSpec, "attack", "attackMin", "attackMax",
			{{this.updateEnvView;}.defer;}],
		["TXMinMaxSliderSplit", "Vol Env Decay", timeSpec, "decay", "decayMin", "decayMax",
			{{this.updateEnvView;}.defer;}],
		["EZslider", "Vol Env Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView;}.defer;}],
		["TXMinMaxSliderSplit", "Vol Env Sustain time", timeSpec, "sustainTime", "sustainTimeMin",
			"sustainTimeMax",{{this.updateEnvView;}.defer;}],
		["TXMinMaxSliderSplit", "Vol Env Release", timeSpec, "release", "releaseMin", "releaseMax",
			{{this.updateEnvView;}.defer;}],
		["NextLine"],
		["SynthOptionPopup", "Vol Env Curve", arrOptionData, 7, 150, {system.showView;}],
		["SynthOptionPopup", "Vol Env Type", arrOptionData, 8, 180],
		["Spacer", 4],
		["ActionButton", "Vol Env Plot", {this.envPlot;}],
		["SynthOptionCheckBox", "Op 1 Env on/off", arrOptionData, 11, 250],
		["Spacer", 4],
		["EZslider", "Env amount", ControlSpec(-1, 1), "envAmountOp1", nil, 260],
		["NextLine"],
		["TXPresetPopup", "Op 1 Env presets",
			TXEnvPresets.arrEnvPresetsSfx(this, 9,10, "Op1").collect({arg item, i; item.at(0)}),
			TXEnvPresets.arrEnvPresetsSfx(this, 9,10, "Op1").collect({arg item, i; item.at(1)})
		],
		["TXEnvDisplay", {this.opEnvViewValues(1);}, {arg view; opEnvView1 = view;}],
		["NextLine"],
		["EZslider", "Op 1 Pre-Delay", ControlSpec(0,1), "delayOp1", {{this.updateOpEnvView(1);}.defer;}],
		["TXMinMaxSliderSplit", "Op 1 Attack", timeSpec, "attackOp1", "attackMinOp1", "attackMaxOp1",
			{{this.updateOpEnvView(1);}.defer;}],
		["TXMinMaxSliderSplit", "Op 1 Decay", timeSpec, "decayOp1", "decayMinOp1", "decayMaxOp1",
			{{this.updateOpEnvView(1);}.defer;}],
		["EZslider", "Op 1 Sustain level", ControlSpec(0, 1), "sustainOp1", {{this.updateOpEnvView(1);}.defer;}],
		["TXMinMaxSliderSplit", "Op 1 Sustain time", timeSpec, "sustainTimeOp1", "sustainTimeMinOp1",
			"sustainTimeMaxOp1",{{this.updateOpEnvView(1);}.defer;}],
		["TXMinMaxSliderSplit", "Op 1 Release", timeSpec, "releaseOp1", "releaseMinOp1", "releaseMaxOp1",
			{{this.updateOpEnvView(1);}.defer;}],
		["NextLine"],
		["SynthOptionPopup", "Op 1 Curve", arrOptionData, 9, 150, {system.showView;}],
		["SynthOptionPopup", "Op 1 Env Type", arrOptionData, 10, 180],
		["SynthOptionCheckBox", "Op 2 Env on/off", arrOptionData, 14, 250],
		["Spacer", 4],
		["EZslider", "Env amount", ControlSpec(-1, 1), "envAmountOp2", nil, 260],
		["NextLine"],
		["TXPresetPopup", "Op 2 Env presets",
			TXEnvPresets.arrEnvPresetsSfx(this, 12,13, "Op2").collect({arg item, i; item.at(0)}),
			TXEnvPresets.arrEnvPresetsSfx(this, 12,13, "Op2").collect({arg item, i; item.at(1)})
		],
		["TXEnvDisplay", {this.opEnvViewValues(2);}, {arg view; opEnvView2 = view;}],
		["NextLine"],
		["EZslider", "Op 2 Pre-Delay", ControlSpec(0,1), "delayOp2", {{this.updateOpEnvView(2);}.defer;}],
		["TXMinMaxSliderSplit", "Op 2 Attack", timeSpec, "attackOp2", "attackMinOp2", "attackMaxOp2",
			{{this.updateOpEnvView(2);}.defer;}],
		["TXMinMaxSliderSplit", "Op 2 Decay", timeSpec, "decayOp2", "decayMinOp2", "decayMaxOp2",
			{{this.updateOpEnvView(2);}.defer;}],
		["EZslider", "Op 2 Sustain level", ControlSpec(0, 1), "sustainOp2", {{this.updateOpEnvView(2);}.defer;}],
		["TXMinMaxSliderSplit", "Op 2 Sustain time", timeSpec, "sustainTimeOp2", "sustainTimeMinOp2",
			"sustainTimeMaxOp2",{{this.updateOpEnvView(2);}.defer;}],
		["TXMinMaxSliderSplit", "Op 2 Release", timeSpec, "releaseOp2", "releaseMinOp2", "releaseMaxOp2",
			{{this.updateOpEnvView(2);}.defer;}],
		["NextLine"],
		["SynthOptionPopup", "Op 2 Curve", arrOptionData, 12, 150, {system.showView;}],
		["SynthOptionPopup", "Op 2 Env Type", arrOptionData, 13, 180],
	]);
	//	use base class initialise
	this.baseInit(this, argInstName);
	this.midiNoteInit;
	//	load the synthdef and create the Group for synths to belong to
	this.loadDefAndMakeGroup;
}

buildGuiSpecArray {
	guiSpecArray = [
		["ActionButton", "Operators", {displayOption = "showOperators";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showOperators")],
		["Spacer", 3],
		["ActionButton", "MIDI/ Note", {displayOption = "showMIDI";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
		["Spacer", 3],
		["ActionButton", "Envelopes", {displayOption = "showEnv";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showEnv")],
		["DividingLine"],
		["SpacerLine", 6],
	];

	if (displayOption == "showOperators", {
		guiSpecArray = guiSpecArray ++[

			["EZslider", "Main level", ControlSpec(0, 1), "level"],
			["SpacerLine", 4],

			["ActionButton", "Op 1", {displayOperator = "showOp1";
				this.buildGuiSpecArray; system.showView;}, 30,
				TXColor.white, this.getButtonColour(displayOperator == "showOp1")],
			["Spacer", 3],
			["ActionButton", "Op 2", {displayOperator = "showOp2";
				this.buildGuiSpecArray; system.showView;}, 30,
				TXColor.white, this.getButtonColour(displayOperator == "showOp2")],
			["Spacer", 3],
			["ActionButton", "Op 3", {displayOperator = "showOp3";
				this.buildGuiSpecArray; system.showView;}, 30,
				TXColor.white, this.getButtonColour(displayOperator == "showOp3")],
			["Spacer", 3],
			["ActionButton", "Op 4", {displayOperator = "showOp4";
				this.buildGuiSpecArray; system.showView;}, 30,
				TXColor.white, this.getButtonColour(displayOperator == "showOp4")],
			["Spacer", 3],
			["ActionButton", "Op 5", {displayOperator = "showOp5";
				this.buildGuiSpecArray; system.showView;}, 30,
				TXColor.white, this.getButtonColour(displayOperator == "showOp5")],
			["Spacer", 3],
			["ActionButton", "Op 6", {displayOperator = "showOp6";
				this.buildGuiSpecArray; system.showView;}, 30,
				TXColor.white, this.getButtonColour(displayOperator == "showOp6")],
			["Spacer", 3],
			["ActionButton", "All Ops", {displayOperator = "showAllOps";
				this.buildGuiSpecArray; system.showView;}, 50,
				TXColor.white, this.getButtonColour(displayOperator == "showAllOps")],
			["SpacerLine", 4],
		];

		if (displayOperator == "showOp1", {
			guiSpecArray = guiSpecArray ++[
				["EZslider", "Freq ratio", ControlSpec(0.001, 16, 'amp'), "op1FreqRatio"],
				["EZslider", "Phase", ControlSpec(0,1), "op1Phase"],
				["EZslider", "Amplitude", ControlSpec(0,1), "op1Amp"],
				["SpacerLine", 4],
				["EZslider", "Mod Op 1", ControlSpec(0,1), "modFM_11"],
				["EZslider", "Mod Op 2", ControlSpec(0,1), "modFM_12"],
				["EZslider", "Mod Op 3", ControlSpec(0,1), "modFM_13"],
				["EZslider", "Mod Op 4", ControlSpec(0,1), "modFM_14"],
				["EZslider", "Mod Op 5", ControlSpec(0,1), "modFM_15"],
				["EZslider", "Mod Op 6", ControlSpec(0,1), "modFM_16"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0,1), "outLevelOp1"],
			];
		});
		if (displayOperator == "showOp2", {
			guiSpecArray = guiSpecArray ++[
				["EZslider", "Freq ratio", ControlSpec(0.001, 16, 'amp'), "op2FreqRatio"],
				["EZslider", "Phase", ControlSpec(0,1), "op2Phase"],
				["EZslider", "Amplitude", ControlSpec(0,1), "op2Amp"],
				["SpacerLine", 4],
				["EZslider", "Mod Op 1", ControlSpec(0,1), "modFM_21"],
				["EZslider", "Mod Op 2", ControlSpec(0,1), "modFM_22"],
				["EZslider", "Mod Op 3", ControlSpec(0,1), "modFM_23"],
				["EZslider", "Mod Op 4", ControlSpec(0,1), "modFM_24"],
				["EZslider", "Mod Op 5", ControlSpec(0,1), "modFM_25"],
				["EZslider", "Mod Op 6", ControlSpec(0,1), "modFM_26"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0,1), "outLevelOp2"],
			];
		});
		if (displayOperator == "showOp3", {
			guiSpecArray = guiSpecArray ++[
				["EZslider", "Freq ratio", ControlSpec(0.001, 16, 'amp'), "op3FreqRatio"],
				["EZslider", "Phase", ControlSpec(0,1), "op3Phase"],
				["EZslider", "Amplitude", ControlSpec(0,1), "op3Amp"],
				["SpacerLine", 4],
				["EZslider", "Mod Op 1", ControlSpec(0,1), "modFM_31"],
				["EZslider", "Mod Op 2", ControlSpec(0,1), "modFM_32"],
				["EZslider", "Mod Op 3", ControlSpec(0,1), "modFM_33"],
				["EZslider", "Mod Op 4", ControlSpec(0,1), "modFM_34"],
				["EZslider", "Mod Op 5", ControlSpec(0,1), "modFM_35"],
				["EZslider", "Mod Op 6", ControlSpec(0,1), "modFM_36"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0,1), "outLevelOp3"],
			];
		});
		if (displayOperator == "showOp4", {
			guiSpecArray = guiSpecArray ++[
				["EZslider", "Freq ratio", ControlSpec(0.001, 16, 'amp'), "op4FreqRatio"],
				["EZslider", "Phase", ControlSpec(0,1), "op4Phase"],
				["EZslider", "Amplitude", ControlSpec(0,1), "op4Amp"],
				["SpacerLine", 4],
				["EZslider", "Mod Op 1", ControlSpec(0,1), "modFM_41"],
				["EZslider", "Mod Op 2", ControlSpec(0,1), "modFM_42"],
				["EZslider", "Mod Op 3", ControlSpec(0,1), "modFM_43"],
				["EZslider", "Mod Op 4", ControlSpec(0,1), "modFM_44"],
				["EZslider", "Mod Op 5", ControlSpec(0,1), "modFM_45"],
				["EZslider", "Mod Op 6", ControlSpec(0,1), "modFM_46"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0,1), "outLevelOp4"],
			];
		});
		if (displayOperator == "showOp5", {
			guiSpecArray = guiSpecArray ++[
				["EZslider", "Freq ratio", ControlSpec(0.001, 16, 'amp'), "op5FreqRatio"],
				["EZslider", "Phase", ControlSpec(0,1), "op5Phase"],
				["EZslider", "Amplitude", ControlSpec(0,1), "op5Amp"],
				["SpacerLine", 4],
				["EZslider", "Mod Op 1", ControlSpec(0,1), "modFM_51"],
				["EZslider", "Mod Op 2", ControlSpec(0,1), "modFM_52"],
				["EZslider", "Mod Op 3", ControlSpec(0,1), "modFM_53"],
				["EZslider", "Mod Op 4", ControlSpec(0,1), "modFM_54"],
				["EZslider", "Mod Op 5", ControlSpec(0,1), "modFM_55"],
				["EZslider", "Mod Op 6", ControlSpec(0,1), "modFM_56"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0,1), "outLevelOp5"],
			];
		});
		if (displayOperator == "showOp6", {
			guiSpecArray = guiSpecArray ++[
				["EZslider", "Freq ratio", ControlSpec(0.001, 16, 'amp'), "op6FreqRatio"],
				["EZslider", "Phase", ControlSpec(0,1), "op6Phase"],
				["EZslider", "Amplitude", ControlSpec(0,1), "op6Amp"],
				["SpacerLine", 4],
				["EZslider", "Mod Op 1", ControlSpec(0,1), "modFM_61"],
				["EZslider", "Mod Op 2", ControlSpec(0,1), "modFM_62"],
				["EZslider", "Mod Op 3", ControlSpec(0,1), "modFM_63"],
				["EZslider", "Mod Op 4", ControlSpec(0,1), "modFM_64"],
				["EZslider", "Mod Op 5", ControlSpec(0,1), "modFM_65"],
				["EZslider", "Mod Op 6", ControlSpec(0,1), "modFM_66"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0,1), "outLevelOp6"],
			];
		});
		if (displayOperator == "showAllOps", {
			guiSpecArray = guiSpecArray ++[

				["EZNumber", "Freq ratio", ControlSpec(0.001, 16, 'amp'), "op1FreqRatio", nil, nil, nil, 0.1],
				["TXScrollNumBox", ControlSpec(0.001, 16, 'amp'), "op2FreqRatio", nil, 60, 0.1],
				["TXScrollNumBox", ControlSpec(0.001, 16, 'amp'), "op3FreqRatio", nil, 60, 0.1],
				["TXScrollNumBox", ControlSpec(0.001, 16, 'amp'), "op4FreqRatio", nil, 60, 0.1],
				["TXScrollNumBox", ControlSpec(0.001, 16, 'amp'), "op5FreqRatio", nil, 60, 0.1],
				["TXScrollNumBox", ControlSpec(0.001, 16, 'amp'), "op6FreqRatio", nil, 60, 0.1],
				["EZNumber", "Phase", ControlSpec(0,1), "op1Phase", nil, nil, nil, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "op2Phase", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "op3Phase", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "op4Phase", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "op5Phase", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "op6Phase", nil, 60, 0.05],
				["EZNumber", "Amplitude", ControlSpec(0,1), "op1Amp", nil, nil, nil, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "op2Amp", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "op3Amp", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "op4Amp", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "op5Amp", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "op6Amp", nil, 60, 0.05],
				["SpacerLine", 4],
				["EZNumber", "Mod Op 1", ControlSpec(0,1), "modFM_11", nil, nil, nil, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_21", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_31", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_41", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_51", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_61", nil, 60, 0.05],
				["EZNumber", "Mod Op 2", ControlSpec(0,1), "modFM_12", nil, nil, nil, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_22", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_32", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_42", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_52", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_62", nil, 60, 0.05],
				["EZNumber", "Mod Op 3", ControlSpec(0,1), "modFM_13", nil, nil, nil, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_23", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_33", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_43", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_53", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_63", nil, 60, 0.05],
				["EZNumber", "Mod Op 4", ControlSpec(0,1), "modFM_14", nil, nil, nil, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_24", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_34", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_44", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_54", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_64", nil, 60, 0.05],
				["EZNumber", "Mod Op 5", ControlSpec(0,1), "modFM_15", nil, nil, nil, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_25", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_35", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_45", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_55", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_65", nil, 60, 0.05],
				["EZNumber", "Mod Op 6", ControlSpec(0,1), "modFM_16", nil, nil, nil, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_26", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_36", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_46", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_56", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "modFM_66", nil, 60, 0.05],
				["SpacerLine", 4],
				["EZNumber", "Level", ControlSpec(0,1), "outLevelOp1", nil, nil, nil, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "outLevelOp2", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "outLevelOp3", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "outLevelOp4", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "outLevelOp5", nil, 60, 0.05],
				["TXScrollNumBox", ControlSpec(0,1), "outLevelOp6", nil, 60, 0.05],
			];
		});
	});

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
				5, 60, nil, 36, {arg note; this.releaseSynthGate(note);}],
		];
	});
	if (displayOption == "showEnv", {
		guiSpecArray = guiSpecArray ++[
			["ActionButton", "Vol Env", {displayEnv = "showVolEnv";
				this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayEnv == "showVolEnv")],
			["Spacer", 3],
			["ActionButton", "Op 1 Env", {displayEnv = "showOp1";
				this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayEnv == "showOp1")],
			["Spacer", 3],
			["ActionButton", "Op 2 Env", {displayEnv = "showOp2";
				this.buildGuiSpecArray; system.showView;}, 60,
				TXColor.white, this.getButtonColour(displayEnv == "showOp2")],
			["SpacerLine", 3],
		];

		if (displayEnv == "showOp1", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionCheckBox", "Op 1 Env", arrOptionData, 11, 110],
				["Spacer", 4],
				["EZslider", "Env amount", ControlSpec(-1, 1), "envAmountOp1", nil, 260],
				["NextLine"],
				["TXPresetPopup", "Env presets",
					TXEnvPresets.arrEnvPresetsSfx(this, 9,10, "Op1").collect({arg item, i; item.at(0)}),
					TXEnvPresets.arrEnvPresetsSfx(this, 9,10, "Op1").collect({arg item, i; item.at(1)})
				],
				["TXEnvDisplay", {this.opEnvViewValues(1);}, {arg view; opEnvView1 = view;}],
				["NextLine"],
				["EZslider", "Pre-Delay", ControlSpec(0,1), "delayOp1", {{this.updateOpEnvView(1);}.defer;}],
				["TXMinMaxSliderSplit", "Attack", timeSpec, "attackOp1", "attackMinOp1", "attackMaxOp1",
					{{this.updateOpEnvView(1);}.defer;}],
				["TXMinMaxSliderSplit", "Decay", timeSpec, "decayOp1", "decayMinOp1", "decayMaxOp1",
					{{this.updateOpEnvView(1);}.defer;}],
				["EZslider", "Sustain level", ControlSpec(0, 1), "sustainOp1", {{this.updateOpEnvView(1);}.defer;}],
				["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTimeOp1", "sustainTimeMinOp1",
					"sustainTimeMaxOp1",{{this.updateOpEnvView(1);}.defer;}],
				["TXMinMaxSliderSplit", "Release", timeSpec, "releaseOp1", "releaseMinOp1", "releaseMaxOp1",
					{{this.updateOpEnvView(1);}.defer;}],
				["NextLine"],
				["SynthOptionPopup", "Curve", arrOptionData, 9, 150, {system.showView;}],
				["SynthOptionPopup", "Env Type", arrOptionData, 10, 180],
			];
		});
		if (displayEnv == "showOp2", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionCheckBox", "Op 2 Env", arrOptionData, 14, 110],
				["Spacer", 4],
				["EZslider", "Env amount", ControlSpec(-1, 1), "envAmountOp2", nil, 260],
				["NextLine"],
				["TXPresetPopup", "Env presets",
					TXEnvPresets.arrEnvPresetsSfx(this, 12,13, "Op2").collect({arg item, i; item.at(0)}),
					TXEnvPresets.arrEnvPresetsSfx(this, 12,13, "Op2").collect({arg item, i; item.at(1)})
				],
				["TXEnvDisplay", {this.opEnvViewValues(2);}, {arg view; opEnvView2 = view;}],
				["NextLine"],
				["EZslider", "Pre-Delay", ControlSpec(0,1), "delayOp2", {{this.updateOpEnvView(2);}.defer;}],
				["TXMinMaxSliderSplit", "Attack", timeSpec, "attackOp2", "attackMinOp2", "attackMaxOp2",
					{{this.updateOpEnvView(2);}.defer;}],
				["TXMinMaxSliderSplit", "Decay", timeSpec, "decayOp2", "decayMinOp2", "decayMaxOp2",
					{{this.updateOpEnvView(2);}.defer;}],
				["EZslider", "Sustain level", ControlSpec(0, 1), "sustainOp2", {{this.updateOpEnvView(2);}.defer;}],
				["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTimeOp2", "sustainTimeMinOp2",
					"sustainTimeMaxOp2",{{this.updateOpEnvView(2);}.defer;}],
				["TXMinMaxSliderSplit", "Release", timeSpec, "releaseOp2", "releaseMinOp2", "releaseMaxOp2",
					{{this.updateOpEnvView(2);}.defer;}],
				["NextLine"],
				["SynthOptionPopup", "Curve", arrOptionData, 12, 150, {system.showView;}],
				["SynthOptionPopup", "Env Type", arrOptionData, 13, 180],
			];
		});
		if (displayEnv == "showVolEnv", {
			guiSpecArray = guiSpecArray ++[
				["TXPresetPopup", "Env presets",
					TXEnvPresets.arrEnvPresets(this, 7,8).collect({arg item, i; item.at(0)}),
					TXEnvPresets.arrEnvPresets(this, 7,8).collect({arg item, i; item.at(1)})
				],
				["TXEnvDisplay", {this.envViewValues;}, {arg view; envView = view;}],
				["NextLine"],
				["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView;}.defer;}],
				["TXMinMaxSliderSplit", "Attack", timeSpec, "attack", "attackMin", "attackMax",
					{{this.updateEnvView;}.defer;}],
				["TXMinMaxSliderSplit", "Decay", timeSpec, "decay", "decayMin", "decayMax",
					{{this.updateEnvView;}.defer;}],
				["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView;}.defer;}],
				["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime", "sustainTimeMin",
					"sustainTimeMax",{{this.updateEnvView;}.defer;}],
				["TXMinMaxSliderSplit", "Release", timeSpec, "release", "releaseMin", "releaseMax",
					{{this.updateEnvView;}.defer;}],
				["NextLine"],
				["SynthOptionPopup", "Curve", arrOptionData, 7, 150, {system.showView;}],
				["SynthOptionPopup", "Env Type", arrOptionData, 8, 180],
				["Spacer", 4],
				["ActionButton", "Plot", {this.envPlot;}],
			];
		});
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
	^[testMIDINote, testMIDIVel, testMIDITime];
}

loadExtraData {arg argData;  // override default method
	testMIDINote = argData.at(0);
	testMIDIVel = argData.at(1);
	testMIDITime = argData.at(2);
}

updateIntString{arg argIndex;
	if (ratioView.notNil, {
		if (ratioView.notClosed, {
			ratioView.string = TXIntonation.arrScalesText.at(argIndex);
		});
	});
}

envPlot {
	var del, att, dec, sus, sustime, rel, envCurve;
	del = this.getSynthArgSpec("delay");
	att = this.getSynthArgSpec("attack");
	dec = this.getSynthArgSpec("decay");
	sus = this.getSynthArgSpec("sustain");
	sustime = this.getSynthArgSpec("sustainTime");
	rel = this.getSynthArgSpec("release");
	envCurve = this.getSynthOption(7);
	Env.new([0, 0, 1, sus, sus, 0], [del, att, dec, sustime, rel], envCurve, nil).plot;
}

envViewValues {
	var attack, attackMin, attackMax, decay, decayMin, decayMax, sustain;
	var sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax;
	var del, att, dec, sus, sustime, rel;
	var arrTimesNorm, arrTimesNormedSummed;

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
	sustainTime = this.getSynthArgSpec("sustainTime");
	sustainTimeMin = this.getSynthArgSpec("sustainTimeMin");
	sustainTimeMax = this.getSynthArgSpec("sustainTimeMax");
	sustime = sustainTimeMin + ((sustainTimeMax - sustainTimeMin) * sustainTime);
	release = this.getSynthArgSpec("release");
	releaseMin = this.getSynthArgSpec("releaseMin");
	releaseMax = this.getSynthArgSpec("releaseMax");
	rel = releaseMin + ((releaseMax - releaseMin) * release);

	arrTimesNorm = [0, del, att, dec, sustime, rel].normalizeSum;
	arrTimesNorm.size.do({ arg i;
		arrTimesNormedSummed = arrTimesNormedSummed.add(arrTimesNorm.copyRange(0, i).sum);
	});
	^[arrTimesNormedSummed, [0, 0, 1, sus, sus, 0]].asFloat;
}

opEnvViewValues {arg opNo;
	var attack, attackMin, attackMax, decay, decayMin, decayMax, sustain;
	var sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax;
	var del, att, dec, sus, sustime, rel;
	var arrTimesNorm, arrTimesNormedSummed;

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
	sustainTime = this.getSynthArgSpec("sustainTimeOp" ++ opNo.asString);
	sustainTimeMin = this.getSynthArgSpec("sustainTimeMinOp" ++ opNo.asString);
	sustainTimeMax = this.getSynthArgSpec("sustainTimeMaxOp" ++ opNo.asString);
	sustime = sustainTimeMin + ((sustainTimeMax - sustainTimeMin) * sustainTime);
	release = this.getSynthArgSpec("releaseOp" ++ opNo.asString);
	releaseMin = this.getSynthArgSpec("releaseMinOp" ++ opNo.asString);
	releaseMax = this.getSynthArgSpec("releaseMaxOp" ++ opNo.asString);
	rel = releaseMin + ((releaseMax - releaseMin) * release);

	arrTimesNorm = [0, del, att, dec, sustime, rel].normalizeSum;
	arrTimesNorm.size.do({ arg i;
		arrTimesNormedSummed = arrTimesNormedSummed.add(arrTimesNorm.copyRange(0, i).sum);
	});
	^[arrTimesNormedSummed, [0, 0, 1, sus, sus, 0]].asFloat;
}

updateEnvView {
	if (envView.class == EnvelopeView, {
		if (envView.notClosed, {
			6.do({arg i;
				envView.setEditable(i, true);
			});
			envView.value = this.envViewValues;
			6.do({arg i;
				envView.setEditable(i, false);
			});
		});
	});
}


updateOpEnvView {arg opNo;
	var arrViews;
	arrViews = [opEnvView1, opEnvView2];
	if (arrViews.at(opNo - 1).class == EnvelopeView, {
		if (arrViews.at(opNo - 1).notClosed, {
			6.do({arg i;
				arrViews.at(opNo - 1).setEditable(i, true);
			});
			arrViews.at(opNo - 1).value = this.opEnvViewValues(opNo);
			6.do({arg i;
				arrViews.at(opNo - 1).setEditable(i, false);
			});
		});
	});
}

}

