// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFileRecorder2St : TXModuleBase {		// File Recorder module - stereo version

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
	classvar	<arrBufferSpecs;

	var	<>currentFileNameView;
	var	<>previousFileNameView;
	var	<>recordStatusView;
	var	<>currentFileName = "";
	var	<>previousFileName = "";
	var	<>recordStatus;
	var	<>recordClockView;
	var	<>recordClockRoutine;
	var	<>recordClock = 0;

*initClass{
	arrInstances = [];
	//	set class specific variables
	defaultName = "File Recorder St";
	moduleRate = "audio";
	moduleType = "insert";
	noInChannels = 2;
	// coding note re: arrCtlSCInBusSpecs
	// modulation is set on by default so modulation works straight away in current synth
	arrCtlSCInBusSpecs = [
		["Left rec level", 1, "modLeftLvl", 1],
		["Right rec level", 1, "modRightLvl", 1]
	];
	noOutChannels = 2;
	arrOutBusSpecs = [
		["Out L + R", [0,1]],
		["Out L only", [0]],
		["Out R only", [1]]
	];
	arrBufferSpecs = [ ["bufnumOutBuffer", 65536, 2] ];
}

*new{ arg argInstName;
	 ^super.new.init(argInstName);
}

*syncStartAllRecorders {
	 arrInstances.do({ arg item, i;
	 	item.syncStartRecorder;
	 });
}

*syncStopAllRecorders {
	 arrInstances.do({ arg item, i;
	 	item.syncStopRecorder;
	 });
}

*stopAllRecorders {
	 arrInstances.do({ arg item, i;
	 	item.stopRecorder;
	 });
}
init {arg argInstName;
	var holdControlSpec, holdControlSpec2;
	//	set  class specific instance variables
	arrSynthArgSpecs = [
		["in", 0, 0],
		["out", 0, 0],
		["gate", 1, 0],
		["note", 0, \ir],
		["velocity", 0, \ir],
		["bufnumOutBuffer", 0, 0],
		["syncStart", 0, \ir],
		["syncStop", 0, \ir],
		["recordFormat", 0, \ir],
		["recordTime", 0, \ir],
		["leftLevel", 1.0, defLagTime],
		["rightLevel", 1.0, defLagTime],
		["modLeftLvl", 0, defLagTime],
		["modRightLvl", 0, defLagTime],
	];
	synthDefFunc = {
		arg in, out, gate, note, velocity, bufnumOutBuffer, syncStart, syncStop,
			recordFormat, recordTime, leftLevel, rightLevel, modLeftLvl, modRightLvl;
		var inputL, inputR, outEnv, outSound, levelCombined;
		outEnv = EnvGen.kr(
			Env.new([0,1,1,0],[0.001, 1, 0],'exp', 2),
			gate,
			doneAction: 2
		);
		inputL = InFeedback.ar(in,1);
		inputR = InFeedback.ar(in+1,1);
		levelCombined = [(leftLevel + modLeftLvl).max(0).min(1), (rightLevel + modRightLvl).max(0).min(1)];
		outSound = outEnv * [inputL, inputR] * levelCombined;
		DiskOut.ar(bufnumOutBuffer, outSound);
// suppress for now because of feedback
//		// send output to bus as well as disk to allow for monitoring of recorded signal
//		Out.ar(out, outSound);
	};
	guiSpecArray = [
		["TXPopupAction", "Record format",
			["16-bit aiff", "24-bit aiff", "32-bit float aiff", "16-bit wav", "24-bit wav", "32-bit float wav"], "recordFormat"],
		["SpacerLine", 4],
		["TXTimeBeatsBpmNumber", "Record time", ControlSpec(0, 100000), "recordTime"],
		["SpacerLine", 4],
		["EZSlider", "Left rec level", \unipolar, "leftLevel"],
		["EZSlider", "Right rec level", \unipolar, "rightLevel"],
		["SpacerLine", 4],
		["ActionButtonBig", "Select file", {this.prepareRecorder}],
		["Spacer", 3],
		["ActionButtonBig", "Start rec", {this.startRecorder}],
		["Spacer", 3],
		["ActionButtonBig", "Stop rec", {this.stopRecorder}],
		["Spacer", 3],
		["SeqSyncStartCheckBox"],
		["SeqSyncStopCheckBox"],
		["SpacerLine", 4],
		["TXStaticText", "Record status", {this.recordStatus}, {arg view; recordStatusView = view.textView}],
		["TXStaticText", "Record clock", {this.recordClock.round(0.01).asString}, {arg view; recordClockView = view.textView}],
		["SpacerLine", 4],
		["TXStaticText", "Current file", {this.currentFileName.keep(-50)}, {arg view; currentFileNameView = view.textView}],
		["TXStaticText", "Previous file", {this.previousFileName.keep(-50)}, {arg view; previousFileNameView = view.textView}],
	];
	arrActionSpecs = this.buildActionSpecs([
		["commandAction", "Select record file", {this.prepareRecorder}],
		["commandAction", "Start recording", {this.startRecorder}],
		["commandAction", "Stop recording", {this.stopRecorder}],
		["TXPopupAction", "Record format",
			["16-bit aiff", "24-bit aiff", "32-bit float aiff", "16-bit wav", "24-bit wav", "32-bit float wav"], "recordFormat"],
		["TXTimeBeatsBpmNumber", "Record time", ControlSpec(0, 100000), "recordTime"],
		["EZSlider", "Record level", \unipolar, "recLevel"],
		["SeqSyncStartCheckBox"],
		["SeqSyncStopCheckBox"],
		["TXStaticText", "Record status", {this.recordStatus}, {arg view; recordStatusView = view}],
		["TXStaticText", "Record clock", {this.recordClock.round(0.01).asString}, {arg view; recordClockView = view.textView}],
		["TXStaticText", "Current file", {this.currentFileName.keep(-50)},
			{arg view; currentFileNameView = view}],
		["TXStaticText", "Previous file", {this.previousFileName.keep(-50)},
			{arg view; previousFileNameView = view}],
	]);
	recordStatus = " Select file to record to";
	//	use base class initialise
	this.baseInit(this, argInstName);
	//	make buffers, load the synthdef and create the group
	this.makeBuffersAndGroup(arrBufferSpecs);
}

syncStartRecorder {
	// if syncStart is 1 then start Recorder
	if (this.getSynthArgSpec("syncStart") == 1, {
		this.startRecorder;
	});
}

syncStopRecorder {
	// if syncStop is 1 then stop Recorder
	if (this.getSynthArgSpec("syncStop") == 1, {
		this.stopRecorder;
	});
}

prepareRecorder {
	var recFormatNo, recHeaderFormat, recSampleFormat;
	// if recording is already prepared, or in progress then reset
	if (recordStatus !== " Select file to record to", {
		this.stopRecorder;
	});
	// set recording format variables
	recFormatNo = this.getSynthArgSpec("recordFormat");
	recHeaderFormat = ["aiff", "aiff", "aiff", "wav", "wav", "wav"].at(recFormatNo);
	recSampleFormat = ["int16", "int16", "float32", "int16", "int24", "float32"].at(recFormatNo);
	// create new file
	Dialog.savePanel({ arg path;
		// create an output file for  buffer, leave it open
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			buffers.at(0).write(path, recHeaderFormat, recSampleFormat, 0, 0, true);
			{	this.updateFilenames(path);
				this.updateRecordStatus(" Ready to Record");
				recordClock = 0;
				this.updateRecordClock;
			}.defer;
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
	});
}

startRecorder {
	if (recordStatus == " Ready to Record", {
		this.createSynthNote(0, 0, 0);
		this.updateRecordStatus(" RECORDING IN PROGRESS...");
	 	// routine to update record time
		recordClockRoutine = Routine {
			system.latency.wait;
			loop {
				0.1.wait;
				recordClock = recordClock + 0.1;
				{this.updateRecordClock}.defer;
			}
		}.play;
	 	// if record time > 0 stop recorder after record time
		if (this.getSynthArgSpec("recordTime") > 0, {
			// allow for latency
			SystemClock.sched(this.getSynthArgSpec("recordTime") + system.latency, {
				{this.stopRecorder}.defer;
				nil
			});
		});
	});
}

stopRecorder {
	// close output file
	if (moduleNode.notNil, {moduleNode.release(0);});
	buffers.at(0).close;
	this.updateFilenames("");
	this.updateRecordStatus(" Select file to record to");
	recordClockRoutine.stop;
}

updateRecordStatus { arg argRecStatus;
	recordStatus = argRecStatus;
	{
		if (recordStatusView.notNil, {
			if (recordStatusView.notClosed, {recordStatusView.string = argRecStatus;});
		});
	}.defer;
}

updateRecordClock {
	{
		if (recordClockView.notNil, {
			if (recordClockView.notClosed, {recordClockView.string = recordClock.asString;});
		});
	}.defer;
}

updateFilenames { arg argcurrentFileName;
	if (currentFileName.size > 0, {
		previousFileName = currentFileName;
	});
	currentFileName = argcurrentFileName;
	{
		if (previousFileNameView.notNil, {
			if (previousFileNameView.notClosed, {previousFileNameView.string = previousFileName.keep(-50);});
		});
		if (currentFileNameView.notNil, {
			if (currentFileNameView.notClosed, {currentFileNameView.string = currentFileName.keep(-50);});
		});
	}.defer;
}


}

