// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXEnv16Stage : TXModuleBase {

	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar	<guiWidth=900;
	classvar arrLevelSynthArgs, arrTimeSynthArgs;

	var holdNoStages;

*initClass{
	arrInstances = [];
	//	set class specific variables
	defaultName = "Env 16 stage";
	moduleRate = "control";
	moduleType = "groupsource";
	arrCtlSCInBusSpecs = [
	];
	noOutChannels = 1;
	arrOutBusSpecs = [
		["Out", [0]]
	];
}

*new{ arg argInstName;
	 ^super.new.init(argInstName);
}

init {arg argInstName;
	var arrStageNums;
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
		["envTotalTime", 3.0, \ir],
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
		["time1", 1.0, \ir],
		["time2", 1.0, \ir],
		["time3", 1.0, \ir],
		["time4", 0.0, \ir],
		["time5", 0.0, \ir],
		["time6", 0.0, \ir],
		["time7", 0.0, \ir],
		["time8", 0.0, \ir],
		["time9", 0.0, \ir],
		["time10", 0.0, \ir],
		["time11", 0.0, \ir],
		["time12", 0.0, \ir],
		["time13", 0.0, \ir],
		["time14", 0.0, \ir],
		["time15", 0.0, \ir],
		["time16", 0.0, \ir],
		["velocityScaling", 1, \ir],
  	];
	// synth options are: env. curve type, env. loop type, no. of env. stages, loop start stage, loop end stage
	arrOptions = [0, 0, 1, 1, 2];
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
	arrOptionData = [
		[
			["linear", 'linear'],
			["exponential", 'exponential'],
			["sine", 'sine'],
			["welch", 'welch'],
			["step", 'step'],
			["slope +10 ", 10],
			["slope +9 ", 9],
			["slope +8 ", 8],
			["slope +7 ", 7],
			["slope +6 ", 6],
			["slope +5 ", 5],
			["slope +4 ", 4],
			["slope +3 ", 3],
			["slope +2 ", 2],
			["slope +1 ", 1],
			["slope -1", -1],
			["slope -2 ", -2],
			["slope -3 ", -3],
			["slope -4 ", -4],
			["slope -5 ", -5],
			["slope -6 ", -6],
			["slope -7 ", -7],
			["slope -8 ", -8],
			["slope -9 ", -9],
			["slope -10 ", -10]
		],
		[
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
		arrStageNums.copyRange(2,15),
		arrStageNums,
		arrStageNums,
	];
	synthDefFunc = {
		arg out, gate, note, velocity, envTotalTime,
			level0, level1, level2, level3, level4, level5, level6, level7, level8,
			level9, level10, level11, level12, level13, level14, level15, level16,
			time0, time1, time2, time3, time4, time5, time6, time7, time8, time9, time10,
			time11, time12, time13, time14, time15, time16, velocityScaling;
		var arrLevels, arrTimes, envCurve, envFunction, outEnv;
		envCurve = this.getSynthOption(0);
		arrLevels = [level0, level1, level2, level3, level4, level5, level6, level7, level8,
				level9, level10, level11, level12, level13, level14, level15, level16]
				.copyRange(0, this.getSynthOption(2) -1);
		if (envCurve == 'exponential', {arrLevels = arrLevels.max(0.001);});
		arrTimes = [time1, time2, time3, time4, time5, time6, time7, time8, time9, time10,
				time11, time12, time13, time14, time15, time16]
				.copyRange(0, this.getSynthOption(2) -2);
		envFunction = this.getSynthOption(1);
		outEnv = EnvGen.kr(
			envFunction.value(arrLevels, arrTimes, envCurve, this.getSynthOption(3)-1, this.getSynthOption(4)-1),
			gate,
			doneAction: 2
		);
		// amplitude is vel *  0.007874 approx. == 1 / 127
		Out.kr(out, outEnv * ((velocity * 0.007874) + (1-velocityScaling)).min(1));
	};
	guiSpecArray = [
		["SynthOptionPopup", "No. Stages", arrOptionData, 2, 150, {
			this.spreadStageTimes;
			this.loopStageConform;
			this.resetHoldNoStages;
			system.showViewIfModDisplay(this);}],
		["Spacer", 4],
		["SynthOptionPopup", "Loop stage", arrOptionData, 3, 150, {
			this.loopStageConform;
			system.showViewIfModDisplay(this);}],
		["Spacer", 4],
		["SynthOptionPopup", "Release stage", arrOptionData, 4, 150, {
			this.loopStageConform;
			system.showViewIfModDisplay(this);}],
		["Spacer", 4],
		["ActionButton", "Trigger Envelope", {this.createSynthNote(60, 100, 1);},
			150, TXColor.white, TXColor.sysGuiCol2],
		["DividingLine"],
		["SynthOptionPopup", "Curve", arrOptionData, 0, 200, {system.showView;}],
		["Spacer", 4],
		["SynthOptionPopup", "Env. Type", arrOptionData, 1, 220],
		["Spacer", 4],
		["ActionButton", "Plot", {this.envPlot;}],
		["NextLine"],
		["TXEnvGui", arrLevelSynthArgs, arrTimeSynthArgs, "envTotalTime", {this.getSynthOption(2);},
			{system.showView;}, 200, 30],
		["NextLine"],
		["EZNumber", "Total Time", ControlSpec(0.01, 1600, \exp), "envTotalTime",
			{this.recalcStageTimes;
			this.rebuildSynth;
			system.showView;}],
		["DividingLine"],
		["MIDIListenCheckBox"],
		["NextLine"],
		["MIDIChannelSelector"],
		["NextLine"],
		["MIDINoteSelector"],
		["NextLine"],
		["MIDIVelSelector"],
		["DividingLine"],
		["TXCheckBox", "Scale level to velocity", "velocityScaling"],
		["Spacer", 4],
		["PolyphonySelector"],
		["DividingLine"],
	];
	arrActionSpecs = this.buildActionSpecs([
		["commandAction", "Trigger envelope", {this.createSynthNote(60, 100, 1);}],
		["commandAction", "Trigger envelope gate on only", {this.createSynthNote(60, 100, 0);}],
		["commandAction", "Trigger envelope gate off only", {this.releaseSynthGate;}],
		["commandAction", "Plot envelope", {this.envPlot;}],
		["TXEnvGui", arrLevelSynthArgs, arrTimeSynthArgs, "envTotalTime", {this.getSynthOption(2);},
			{system.showView;}],
		["SynthOptionPopup", "No. Stages", arrOptionData, 2, 150, {
			this.spreadStageTimes;
			this.loopStageConform;
			this.resetHoldNoStages;
			system.showViewIfModDisplay(this);}],
		["SynthOptionPopup", "Loop stage", arrOptionData, 3, 150, {
			this.loopStageConform;
			system.showViewIfModDisplay(this);}],
		["SynthOptionPopup", "Release stage", arrOptionData, 4, 150, {
			this.loopStageConform;
			system.showViewIfModDisplay(this);}],
		["SynthOptionPopup", "Curve", arrOptionData, 0, 200, {system.showView;}],
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

recalcStageTimes {
	var arrTimes, holdTotalTime, arrNewTimes;
	// get initial values
	arrTimes = arrTimeSynthArgs.collect({ arg item, i;
		this.getSynthArgSpec(item);
	});
	holdTotalTime = this.getSynthArgSpec("envTotalTime");
	arrNewTimes = arrTimes.normalizeSum * holdTotalTime;
	// set values
	arrTimeSynthArgs.do({ arg item, i;
		this.setSynthArgSpec(item, arrNewTimes.at(i));
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

spreadStageTimes{
	var arrTimes, holdTotalTime, timesNeeded, arrNewTimes;
	// get initial values
	arrTimes = arrTimeSynthArgs.collect({ arg item, i;
		this.getSynthArgSpec(item);
	});
	arrTimes = (arrTimes. keep(holdNoStages). normalizeSum) * holdNoStages;
	timesNeeded = (17-arrTimes.size);
	timesNeeded.do({ arg i; arrTimes = arrTimes.add(3/timesNeeded); });
	holdTotalTime = this.getSynthArgSpec("envTotalTime");
	arrNewTimes = arrTimes.normalizeSum * holdTotalTime;
	// set values
	arrTimeSynthArgs.do({ arg item, i;
		this.setSynthArgSpec(item, arrNewTimes.at(i));
	});
}

envPlot {
	var envCurve, arrLevels, arrTimes;
	envCurve = this.getSynthOption(0);
	arrLevels = arrLevelSynthArgs
		.copyRange(0, this.getSynthOption(2) -1)
		.collect({ arg item, i; this.getSynthArgSpec(item); });
	if (envCurve == 'exponential', {arrLevels = arrLevels.max(0.001);});
	arrTimes = arrTimeSynthArgs
		.copyRange(1, this.getSynthOption(2) -1)
		.collect({ arg item, i; this.getSynthArgSpec(item); });
	Env.new(arrLevels, arrTimes, envCurve).plot;
}


resetHoldNoStages {
	holdNoStages = this.getSynthOption(2) ;
}

}


