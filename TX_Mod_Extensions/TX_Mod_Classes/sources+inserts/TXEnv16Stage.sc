// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXEnv16Stage : TXModuleBase {

	classvar <>classData;

	classvar arrLevelSynthArgs, arrTimeSynthArgs;

	var holdNoStages;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Env 16 stage";
		classData.moduleRate = "control";
		classData.moduleType = "groupsource";
		classData.arrCtlSCInBusSpecs = [
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Env Out", [0]],
			["Env End Trig", [1]],
		];
		classData.guiWidth = 900;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var arrStageNums, arrCurveOptionData;
		//	set  class specific instance variables
		arrLevelSynthArgs = 	["level0", "level1", "level2", "level3", "level4", "level5", "level6", "level7", "level8",
			"level9", "level10", "level11", "level12", "level13", "level14", "level15", "level16"];
		arrTimeSynthArgs = ["time0", "time1", "time2", "time3", "time4", "time5", "time6", "time7", "time8",
			"time9", "time10", "time11", "time12", "time13", "time14", "time15", "time16"];
		arrSynthArgSpecs = [
			["out", 0, 0],
			["gate", 1, 0],
			["note", 0, \ir],
			["velocity", 0, \ir],
			["envTotalTime", 1.75, \ir],
			["level0", 0.0, \ir],
			["level1", 1.0, \ir],
			["level2", 1.0, \ir],
			["level3", 0.0, \ir],
			["level4", 0.0, \ir],
			["level5", 0.0, \ir],
			["level6", 0.0, \ir],
			["level7", 0.0, \ir],
			["level8", 0.0, \ir],
			["level9", 0.0, \ir],
			["level10", 0.0, \ir],
			["level11", 0.0, \ir],
			["level12", 0.0, \ir],
			["level13", 0.0, \ir],
			["level14", 0.0, \ir],
			["level15", 0.0, \ir],
			["level16", 0.0, \ir],
			["time0", 0.0, \ir],
			["time1", 0.25, \ir],
			["time2", 0.25, \ir],
			["time3", 0.25, \ir],
			["time4", 0.25, \ir],
			["time5", 0.25, \ir],
			["time6", 0.25, \ir],
			["time7", 0.25, \ir],
			["time8", 0.25, \ir],
			["time9", 0.25, \ir],
			["time10", 0.25, \ir],
			["time11", 0.25, \ir],
			["time12", 0.25, \ir],
			["time13", 0.25, \ir],
			["time14", 0.25, \ir],
			["time15", 0.25, \ir],
			["time16", 0.25, \ir],
			["velocityScaling", 1, \ir],
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
		arrCurveOptionData = [
			["lin", 'linear'],
			["exp", 'exponential'],
			["sine", 'sine'],
			["welch", 'welch'],
			["step", 'step'],
			["+10 ", 10],
			["+9 ", 9],
			["+8 ", 8],
			["+7 ", 7],
			["+6 ", 6],
			["+5 ", 5],
			["+4 ", 4],
			["+3 ", 3],
			["+2 ", 2],
			["+1 ", 1],
			["-1", -1],
			["-2 ", -2],
			["-3 ", -3],
			["-4 ", -4],
			["-5 ", -5],
			["-6 ", -6],
			["-7 ", -7],
			["-8 ", -8],
			["-9 ", -9],
			["-10 ", -10],
			["hold", 'hold'],
			["sqr", 'sqr'],
			["cub", 'cub'],
		];
		arrStageNums = [
			["1", 1],
			["2", 2],
			["3", 3],
			["4", 4],
			["5", 5],
			["6", 6],
			["7", 7],
			["8", 8],
			["9", 9],
			["10", 10],
			["11", 11],
			["12", 12],
			["13", 13],
			["14", 14],
			["15", 15],
			["16", 16],
		];
		// synth options are: env. curve type, env. loop type, no. of env. stages, loop start stage, loop end stage,
		//       use curve 1 for all, curves 2,3,4,5,6,7,8,9,10,11,12,13,14
		arrOptions = [0, 0, 5, 1, 2, 1] ++ (0 ! 14);
		arrOptionData = [
			 // ind0 - env. curve 1
			arrCurveOptionData,
			[ // ind1 - env. loop type
				["Sustain Looped",
					{arg arrLevels, arrTimes, curve, startLoop, release;
						if (startLoop == release, {
							Env.new(arrLevels, arrTimes, curve, release, nil);
						},{
							Env.new(arrLevels, arrTimes, curve, release, startLoop);
						});
					}
				],
				["Sustain Unlooped",
					{arg arrLevels, arrTimes, curve, startLoop, release;
						Env.new(arrLevels, arrTimes, curve, release, nil);
					}
				],
				["Fixed Length",
					{arg arrLevels, arrTimes, curve, startLoop, release;
						Env.new(arrLevels, arrTimes, curve, nil, nil);
					}
				]
			],
			// ind2 - env. stages,
			arrStageNums.copyRange(2,15),
			// ind3 - loop start stage
			arrStageNums,
			// ind4 - loop end stage
			arrStageNums,
			[ // ind5 - use curve 1 for all stages
				["Off", 0],
				["On", 1],
			],
			 // ind6 - env. curve 2
			arrCurveOptionData,
			 // ind7 - env. curve 3
			arrCurveOptionData,
			 // ind8 - env. curve 4
			arrCurveOptionData,
			 // ind9 - env. curve 5
			arrCurveOptionData,
			 // ind10 - env. curve 6
			arrCurveOptionData,
			 // ind11 - env. curve 7
			arrCurveOptionData,
			 // ind12 - env. curve 8
			arrCurveOptionData,
			 // ind13 - env. curve 9
			arrCurveOptionData,
			 // ind14 - env. curve 10
			arrCurveOptionData,
			 // ind15 - env. curve 11
			arrCurveOptionData,
			 // ind16 - env. curve 12
			arrCurveOptionData,
			 // ind17 - env. curve 13
			arrCurveOptionData,
			 // ind18 - env. curve 14
			arrCurveOptionData,
			 // ind19 - env. curve 15
			arrCurveOptionData,
		];
		synthDefFunc = {
			arg out, gate, note, velocity, envTotalTime,
			level0, level1, level2, level3, level4, level5, level6, level7, level8,
			level9, level10, level11, level12, level13, level14, level15, level16,
			time0, time1, time2, time3, time4, time5, time6, time7, time8, time9, time10,
			time11, time12, time13, time14, time15, time16, velocityScaling;
			var arrLevels, arrTimes, arrEnvCurves, envFunction, outEnv, envEnd;
			arrEnvCurves = this.getArrCurves;
			arrLevels = [level0, level1, level2, level3, level4, level5, level6, level7, level8,
				level9, level10, level11, level12, level13, level14, level15, level16]
			.copyRange(0, this.getSynthOption(2) -1);
			arrTimes = [time1, time2, time3, time4, time5, time6, time7, time8, time9, time10,
				time11, time12, time13, time14, time15, time16]
			.copyRange(0, this.getSynthOption(2) -2);
			envFunction = this.getSynthOption(1);
			outEnv = EnvGen.kr(
				envFunction.value(arrLevels + 0.001, arrTimes, arrEnvCurves, this.getSynthOption(3)-1, this.getSynthOption(4)-1),
				gate,
				doneAction: 2
			);
			envEnd = Trig1.kr(Done.kr(outEnv), ControlDur.ir);
			// outEnv - 0.001 : remove exponential offset in env function
			Out.kr(out, [(outEnv - 0.001) * ((velocity * 0.007874) + (1-velocityScaling)).min(1), envEnd]);
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Trigger envelope 1 sec", {this.createSynthNote(60, 100, 1);}],
			["commandAction", "Trigger envelope gate on only", {this.createSynthNote(60, 100, 0);}],
			["commandAction", "Trigger envelope gate off only", {this.releaseSynthGate;}],
			["SynthOptionPopup", "No. Stages", arrOptionData, 2, 150, {
				this.updateEnvTotalTime;
				this.loopStageConform;
				this.resetHoldNoStages;
				this.buildGuiSpecArray;
				system.showViewIfModDisplay(this);}],
			["SynthOptionPopup", "Loop stage", arrOptionData, 3, 150, {
				this.loopStageConform;
				system.showViewIfModDisplay(this);}],
			["SynthOptionPopup", "Release st", arrOptionData, 4, 150, {
				this.loopStageConform;
				system.showViewIfModDisplay(this);}],
			["SynthOptionPopup", "Curves", arrOptionData, 0, 129, {system.showView;}],
			["SynthOptionPopup", "Curve 2", arrOptionData, 6, 45, {system.showView;}, 0], // zero text width
			["SynthOptionPopup", "Curve 3", arrOptionData, 7, 45, {system.showView;}, 0], // zero text width
			["SynthOptionPopup", "Curve 4", arrOptionData, 8, 45, {system.showView;}, 0], // zero text width
			["SynthOptionPopup", "Curve 5", arrOptionData, 9, 45, {system.showView;}, 0], // zero text width
			["SynthOptionPopup", "Curve 6", arrOptionData, 10, 45, {system.showView;}, 0], // zero text width
			["SynthOptionPopup", "Curve 7", arrOptionData, 11, 45, {system.showView;}, 0], // zero text width
			["SynthOptionPopup", "Curve 8", arrOptionData, 12, 45, {system.showView;}, 0], // zero text width
			["SynthOptionPopup", "Curve 9", arrOptionData, 13, 45, {system.showView;}, 0], // zero text width
			["SynthOptionPopup", "Curve 10", arrOptionData, 14, 45, {system.showView;}, 0], // zero text width
			["SynthOptionPopup", "Curve 11", arrOptionData, 15, 45, {system.showView;}, 0], // zero text width
			["SynthOptionPopup", "Curve 12", arrOptionData, 16, 45, {system.showView;}, 0], // zero text width
			["SynthOptionPopup", "Curve 13", arrOptionData, 17, 45, {system.showView;}, 0], // zero text width
			["SynthOptionPopup", "Curve 14", arrOptionData, 18, 45, {system.showView;}, 0], // zero text width
			["SynthOptionPopup", "Curve 15", arrOptionData, 19, 45, {system.showView;}, 0], // zero text width
			["SynthOptionPopup", "Env. Type", arrOptionData, 1, 200],
			["EZNumber", "Total Time", ControlSpec(0.01, 1600, \exp), "envTotalTime",{
				this.recalcStageTimes;
				this.rebuildSynth;
				system.showViewIfModDisplay(this);}],
			["MIDIListenCheckBox"],
			["MIDIChannelSelector"],
			["MIDINoteSelector"],
			["MIDIVelSelector"],
			["TXCheckBox", "Scale level to velocity", "velocityScaling"],
			["PolyphonySelector"],
		]);

		//	use base class initialise
		this.baseInit(this, argInstName);
		this.setMonophonic;	// monophonic by default
		this.resetHoldNoStages;	// initialise
		this.midiNoteInit;
		//	load the synthdef and create the Group for synths to belong to
		this.loadDefAndMakeGroup;
	}

	buildGuiSpecArray {
		var arrGridPresetNames, arrGridPresetActions;
		arrGridPresetNames = ["1 x 1", "2 x 2", "3 x 3", "4 x 4", "5 x 5", "6 x 6", "7 x 7", "8 x 8", "9 x 9",
			"10 x 10", "11 x 11", "12 x 12", "13 x 13", "14 x 14", "15 x 15", "16 x 16", "24 x 24", "32 x 32"];
		arrGridPresetActions = [
			{this.setSynthArgSpec("gridRows", 1); this.setSynthArgSpec("gridCols", 1); },
			{this.setSynthArgSpec("gridRows", 2); this.setSynthArgSpec("gridCols", 2); },
			{this.setSynthArgSpec("gridRows", 3); this.setSynthArgSpec("gridCols", 3); },
			{this.setSynthArgSpec("gridRows", 4); this.setSynthArgSpec("gridCols", 4); },
			{this.setSynthArgSpec("gridRows", 5); this.setSynthArgSpec("gridCols", 5); },
			{this.setSynthArgSpec("gridRows", 6); this.setSynthArgSpec("gridCols", 6); },
			{this.setSynthArgSpec("gridRows", 7); this.setSynthArgSpec("gridCols", 7); },
			{this.setSynthArgSpec("gridRows", 8); this.setSynthArgSpec("gridCols", 8); },
			{this.setSynthArgSpec("gridRows", 9); this.setSynthArgSpec("gridCols", 9); },
			{this.setSynthArgSpec("gridRows", 10); this.setSynthArgSpec("gridCols", 10); },
			{this.setSynthArgSpec("gridRows", 11); this.setSynthArgSpec("gridCols", 11); },
			{this.setSynthArgSpec("gridRows", 12); this.setSynthArgSpec("gridCols", 12); },
			{this.setSynthArgSpec("gridRows", 13); this.setSynthArgSpec("gridCols", 13); },
			{this.setSynthArgSpec("gridRows", 14); this.setSynthArgSpec("gridCols", 14); },
			{this.setSynthArgSpec("gridRows", 15); this.setSynthArgSpec("gridCols", 15); },
			{this.setSynthArgSpec("gridRows", 16); this.setSynthArgSpec("gridCols", 16); },
			{this.setSynthArgSpec("gridRows", 24); this.setSynthArgSpec("gridCols", 24); },
			{this.setSynthArgSpec("gridRows", 32); this.setSynthArgSpec("gridCols", 32); },
		];
		guiSpecArray = [
			["SynthOptionPopup", "No. Stages", arrOptionData, 2, 130, {
				this.updateEnvTotalTime;
				this.loopStageConform;
				this.resetHoldNoStages;
				this.buildGuiSpecArray;
				system.showViewIfModDisplay(this);}],
			["Spacer", 20],
			["SynthOptionPopup", "Loop stage", arrOptionData, 3, 130, {
				this.loopStageConform;
				system.showViewIfModDisplay(this);}],
			["Spacer", 20],
			["SynthOptionPopup", "Release st", arrOptionData, 4, 130, {
				this.loopStageConform;
				system.showViewIfModDisplay(this);}],
			["Spacer", 20],
			["SynthOptionPopup", "Env. Type", arrOptionData, 1, 220],
			["Spacer", 20],
			["ActionButton", "Trigger Envelope 1 sec", {this.createSynthNote(60, 100, 1);},
				140, TXColor.white, TXColor.sysGuiCol2],
			["SpacerLine", 2],
			["TXEnvGui", arrLevelSynthArgs, arrTimeSynthArgs, "envTotalTime", {this.getSynthOption(2);},
				{system.showView;}, 200, 45, {this.getArrCurves}, {this.getSynthArgSpec("gridRows")},
				{this.getSynthArgSpec("gridCols")}],
			["NextLine"],
			["SynthOptionPopup", "Curves", arrOptionData, 0, 129, {system.showView;}],
		]
		++ (
			[
				["SynthOptionPopup", "Curve 2", arrOptionData, 6, 45, {system.showView;}, 0], // zero text width
				["SynthOptionPopup", "Curve 3", arrOptionData, 7, 45, {system.showView;}, 0], // zero text width
				["SynthOptionPopup", "Curve 4", arrOptionData, 8, 45, {system.showView;}, 0], // zero text width
				["SynthOptionPopup", "Curve 5", arrOptionData, 9, 45, {system.showView;}, 0], // zero text width
				["SynthOptionPopup", "Curve 6", arrOptionData, 10, 45, {system.showView;}, 0], // zero text width
				["SynthOptionPopup", "Curve 7", arrOptionData, 11, 45, {system.showView;}, 0], // zero text width
				["SynthOptionPopup", "Curve 8", arrOptionData, 12, 45, {system.showView;}, 0], // zero text width
				["SynthOptionPopup", "Curve 9", arrOptionData, 13, 45, {system.showView;}, 0], // zero text width
				["SynthOptionPopup", "Curve 10", arrOptionData, 14, 45, {system.showView;}, 0], // zero text width
				["SynthOptionPopup", "Curve 11", arrOptionData, 15, 45, {system.showView;}, 0], // zero text width
				["SynthOptionPopup", "Curve 12", arrOptionData, 16, 45, {system.showView;}, 0], // zero text width
				["SynthOptionPopup", "Curve 13", arrOptionData, 17, 45, {system.showView;}, 0], // zero text width
				["SynthOptionPopup", "Curve 14", arrOptionData, 18, 45, {system.showView;}, 0], // zero text width
				["SynthOptionPopup", "Curve 15", arrOptionData, 19, 45, {system.showView;}, 0], // zero text width
			].keep(arrOptions[2] + 1)
		)
		++[
			["SpacerLine", 2],
			["EZNumber", "Total Time", ControlSpec(0.01, 1600, \exp), "envTotalTime",
				{this.recalcStageTimes;
					this.rebuildSynth;
					system.showView;}],
			["Spacer", 10],
			["SynthOptionCheckBox", "Use Curve 1 for all stages", arrOptionData, 5, 200, {system.showView;}],
			["Spacer", 10],
			["EZNumber", "Grid rows", ControlSpec(1, 128), "gridRows", {system.showView}],
			["EZNumber", "Grid columns", ControlSpec(1, 128), "gridCols", {system.showView}],
			["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 184],
			["DividingLine"],
			["SpacerLine", 2],
			["MIDIListenCheckBox"],
			["NextLine"],
			["MIDIChannelSelector"],
			["NextLine"],
			["MIDINoteSelector"],
			["NextLine"],
			["MIDIVelSelector"],
			["DividingLine"],
			["SpacerLine", 2],
			["TXCheckBox", "Scale level to velocity", "velocityScaling"],
			["Spacer", 12],
			["PolyphonySelector"],
		];
	}
	recalcStageTimes {
		var numStages, arrTimes, holdTotalTime, arrNewTimes;
		// get initial values
		numStages= this.getSynthOption(2);
		arrTimes = arrTimeSynthArgs.collect({ arg item, i;
			this.getSynthArgSpec(item);
		});
		holdTotalTime = this.getSynthArgSpec("envTotalTime");
		arrNewTimes = arrTimes.keep(numStages).normalizeSum * holdTotalTime;
		// set values
		arrTimeSynthArgs.do({ arg item, i;
			this.setSynthArgSpec(item, arrNewTimes.at(i) ? arrTimes.at(i));
		});
	}

	loopStageConform { // make sure loop stages are valid
		var holdVal;
		holdVal = this.getSynthOption(2);
		if (this.getSynthOption(3) > (holdVal-1), {
			this.setSynthOption(3, (holdVal-1));
		});
		if (this.getSynthOption(4) > (holdVal-1), {
			this.setSynthOption(4, (holdVal-1));
		});
	}

	updateEnvTotalTime {
		var numStages, arrTimes, holdTotalTime;
		numStages= this.getSynthOption(2);
		arrTimes = arrTimeSynthArgs.collect({ arg item, i;
			this.getSynthArgSpec(item);
		});
		holdTotalTime = arrTimes.keep(numStages).sum;
		this.setSynthArgSpec("envTotalTime", holdTotalTime);
	}

	envPlot {
		var arrEnvCurves, arrLevels, arrTimes;
		arrEnvCurves = this.getArrCurves;
		arrLevels = arrLevelSynthArgs
		.copyRange(0, this.getSynthOption(2) -1)
		.collect({ arg item, i; this.getSynthArgSpec(item); });
		if (arrEnvCurves.asArray.indexOfEqual('exponential').notNil, {arrLevels = arrLevels.max(0.001);});
		arrTimes = arrTimeSynthArgs
		.copyRange(1, this.getSynthOption(2) -1)
		.collect({ arg item, i; this.getSynthArgSpec(item); });
		Env.new(arrLevels, arrTimes, arrEnvCurves).plot;
	}

	resetHoldNoStages {
		holdNoStages = this.getSynthOption(2) ;
	}

	getArrCurves {
		var arrCurves;
		if (this.getSynthOption(5) == 1, {
			arrCurves = this.getSynthOption(0); // curve 1
		}, {
			arrCurves = [0, 6, 7, 8, 9, 10, 11 ,12 ,13 ,14, 15, 16, 17, 18, 19]
			.collect({arg item, i;
				this.getSynthOption(item); // curves 1-15
			});
		});
		^arrCurves;
	}

	// override default method
	loadExtraData {arg argData, isPresetData = false;
		this.buildGuiSpecArray;
	}

}


