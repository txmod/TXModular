// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFileRecorder2 : TXModuleBase {		// File Recorder module

	classvar <>classData;

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
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "File Recorder";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		// coding note re: arrCtlSCInBusSpecs
		// modulation is set on by default so modulation works straight away in current synth
		classData.arrCtlSCInBusSpecs = [
			["Recording Level", 1, "modRecLevel", 1],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrBufferSpecs = [ ["bufnumOutBuffer", 65536, 1] ];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	*syncStartAllRecorders {
		classData.arrInstances.do({ arg item, i;
			item.syncStartRecorder;
		});
	}

	*syncStopAllRecorders {
		classData.arrInstances.do({ arg item, i;
			item.syncStopRecorder;
		});
	}

	*stopAllRecorders {
		classData.arrInstances.do({ arg item, i;
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
			["note", 0, \ir],			// not used
			["velocity", 0, \ir],		// not used
			["bufnumOutBuffer", 0, 0],
			["syncStart", 0, \ir],
			["syncStop", 0, \ir],
			["recordFormat", 0, \ir],
			["recordTime", 0, \ir],
			["recLevel", 1, defLagTime],
			["outLevel", 0, defLagTime],
			["modRecLevel", 0, defLagTime],
		];
		synthDefFunc = {
			arg in, out, gate, note, velocity, bufnumOutBuffer, syncStart, syncStop,
			recordFormat, recordTime, recLevel, outLevel, modRecLevel=0;
			var input, outEnv, outSound, levelCombined;
			outEnv = EnvGen.kr(
				Env.new([0,1,1,0],[0.001, 1, 0],'exp', 2),
				gate,
				doneAction: 2
			);
			input = InFeedback.ar(in,1);
			levelCombined = (recLevel + modRecLevel).max(0).min(1);
			outSound = outEnv * input * levelCombined;
			DiskOut.ar(bufnumOutBuffer, outSound);
			// send input to bus for monitoring of recorded signal
			Out.ar(out, outSound * outLevel);
		};
		guiSpecArray = [
			["TXPopupAction", "Record format",
				["16-bit aiff", "24-bit aiff", "32-bit float aiff", "16-bit wav", "24-bit wav", "32-bit float wav"], "recordFormat"],
			["SpacerLine", 4],
			["TXTimeBeatsBpmNumber", "Record time", ControlSpec(0, 100000), "recordTime"],
			["SpacerLine", 4],
			["EZSlider", "Record level", \unipolar, "recLevel"],
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
			["EZSlider", "Out level", \unipolar, "outLevel"],
		];
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Select record file", {this.prepareRecorder}],
			["commandAction", "Start recording", {this.startRecorder}],
			["commandAction", "Stop recording", {this.stopRecorder}],
			["TXPopupAction", "Record format",
				["16-bit aiff", "24-bit aiff", "32-bit float aiff", "16-bit wav", "24-bit wav", "32-bit float wav"],
				"recordFormat"
			],
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
			["EZSlider", "Out level", \unipolar, "outLevel"],
		]);
		recordStatus = " Select file to record to";
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make buffers, load the synthdef and create the group
		this.makeBuffersAndGroup(classData.arrBufferSpecs);
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

