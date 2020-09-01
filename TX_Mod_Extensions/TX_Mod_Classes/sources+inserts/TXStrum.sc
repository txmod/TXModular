// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXStrum : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var	oscResponder;
	//var  arrActions;

	var arrScaleNotes;
	var <>noteOutModule1;
	var <>noteOutModule2;
	var <>noteOutModule3;
	var <>noteOutModule4;
	var <>noteOutModule5;
	var <>noteOutModule6;
	var	<>testMIDIVel = 100;
	var midiControlResp;
	var holdPortNames, holdMIDIOutPort, holdMIDIDeviceName, holdMIDIPortName;
	var noteListTextView;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Strum";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Active", 1, "modActive", 0],
			["Strum Mode", 1, "modStrumMode"],
			["Strum Position", 1, "modPosition"],
			["Smooth Time 1", 1, "modLag", 0],
			["Smooth Time 2", 1, "modLag2", 0],
			["Velocity Min", 1, "modVelocity", 0],
			["Random Vel", 1, "modRandVel", 0],
			["Gate Time", 1, "modEnvTime", 0],
			["Transpose", 1, "modTranspose", 0],
		];
		classData.noOutChannels = 4;
		classData.arrOutBusSpecs = [
			["Trigger Out", [0]],
			["Note Out", [1]],
			["Velocity Out", [2]],
			["Position Out", [3]],
		];
		classData.arrBufferSpecs = [ ["bufnumScaleNotes", 128, 1] ];
		classData.timeSpec = ControlSpec(0.01, 20, 'exp');
		classData.transposeSpec = ControlSpec(-48, 48);
		classData.guiWidth = 720;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	*restoreAllOutputs {
		classData.arrInstances.do({ arg item, i;
			item.restoreOutputs;
		});
	}

	init {arg argInstName;
		var holdControlSpec;
		//	set  class specific instance variables
		displayOption = "showOutputs";
		//arrActions = [99,0,0,0,0,0,0, nil].dup(10);
		arrSynthArgSpecs = [
			["out", 0, 0],
			["bufnumScaleNotes", 0, \ir],
			["active", 1, 0, 0],
			["strumMode", 0, 0],
			["strumModeMin", 1, 0],
			["strumModeMax", 3, 0],
			["position", 0, 0],
			["lag", 0.5, 0],
			["lagMin", 0.01, 0],
			["lagMax", 1, 0],
			["lag2", 0.5, 0],
			["lag2Min", 0.01, 0],
			["lag2Max", 1, 0],
			["noteListSize", 1, 0],
			["velocity", 0.75, 0],
			["randVel", 0.0, 0],
			["envTime", ControlSpec(0.01, 1, 'exp').unmap(0.1), 0],
			["envTimeMin", 0.01, 0],
			["envTimeMax", 1, 0],
			["transpose", 0.5, 0],
			["transposeMin", -12, 0],
			["transposeMax", 12, 0],
			["modActive", 0, 0],
			["modStrumMode", 0, 0],
			["modPosition", 0, 0],
			["modLag", 0, 0],
			["modLag2", 0, 0],
			["modVelocity", 0, 0],
			["modRandVel", 0, 0],
			["modEnvTime", 0, 0],
			["modTranspose", 0, 0],

			// N.B. the args below aren't used in the synthdef, just stored here for convenience

			["useCustomScale", 0, 0],
			["key", 0, 0],
			["scale", 12, 0],
			["noteListMin", 48, 0],
			["noteListMax", 72, 0],

			["midiPort", 0, 0],
			["midiLoopBack", 0, 0],
			["midiChannel", 1, 0],
			["midiNoteOn", 0, 0],

			["noteOutModuleID1", nil],
			["noteOutModuleID2", nil],
			["noteOutModuleID3", nil],
			["noteOutModuleID4", nil],
			["noteOutModuleID5", nil],
			["noteOutModuleID6", nil],
		];
		arrOptions = [2, 1];
		arrOptionData = [
			[
				// smoothing
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
			[
				// Quantise Transpose
				["Quantise off", {arg inval; inval} ],
				["Quantise transpose to whole semitones", {arg inval; inval.round} ],
				["Quantise transpose to whole octaves (12 semitones)", {arg inval; inval.round(12)} ],
			],
		];
		synthDefFunc = { arg out, bufnumScaleNotes, active, strumMode, strumModeMin, strumModeMax, position,
			lag, lagMin, lagMax, lag2, lag2Min, lag2Max,
			noteListSize, velocity, randVel, envTime, envTimeMin, envTimeMax,
			transpose, transposeMin, transposeMax,
			modActive = 0, modStrumMode = 0, modPosition = 0, modLag = 0, modLag2 = 0, modVelocity = 0, modRandVel = 0,
			modEnvTime = 0, modTranspose = 0;

			var activeSum, strumModeSum, positionSum, posSmooth, posScaled, posScaledMod, lagtime, lagtime2;
			var velSum, randVelSum, outVel, envTimeSum, signalChange, transposeFunction, transposeSum, outTranspose;
			var upStrum, downStrum, outTrig, noteIndex, scaleNote, outNote, outLagFunction;

			activeSum = noteListSize.min(1) * (active + modActive).max(0).min(1).round;
			strumModeSum = (strumMode + modStrumMode).max(0).min(1).linlin(0, 1, strumModeMin, strumModeMax).round;
			positionSum = (position + modPosition).max(0).min(1);
			lagtime = (lag + modLag).max(0).min(1).linexp(0, 1, lagMin, lagMax);
			lagtime2 = (lag2 + modLag2).max(0).min(1).linexp(0, 1, lag2Min, lag2Max);
			outLagFunction = this.getSynthOption(0);
			posSmooth = outLagFunction.value(positionSum, lagtime, lagtime2);
			posScaled = posSmooth.clip * noteListSize;
			posScaledMod = posScaled.mod(1.0);

			signalChange = activeSum * HPZ1.kr(posSmooth);
			upStrum = (strumModeSum < 3) * (signalChange > 0);
			downStrum = strumModeSum.mod(2.0) * (signalChange < 0);
			outTrig = (upStrum * Trig1.kr(posScaledMod - 0.5, 0.01))
				+ (downStrum * Trig1.kr(0.5 - posScaledMod, 0.01));

			noteIndex = posScaled.floor;
			scaleNote= Demand.kr(outTrig, 0, Dbufrd(bufnumScaleNotes, noteIndex));
			transposeFunction = this.getSynthOption(1);
			transposeSum = (transpose + modTranspose).max(0).min(1).linlin(0, 1, transposeMin, transposeMax);
			outTranspose = transposeFunction.value(transposeSum);
			outNote = (scaleNote + outTranspose).clip(0, 127);

			velSum = (velocity + modVelocity).max(0).min(1);
			randVelSum = (randVel + modRandVel).max(0).min(1);
			outVel = velSum + ((1 - velSum) *  TRand.kr(0, randVelSum, outTrig));

			envTimeSum = (envTime + modEnvTime).max(0).min(1).linexp(1, 0, envTimeMin, envTimeMax);

			//SendTrig.kr(outTrig, noteIndex, outVel);
			SendReply.kr(outTrig, '/strum', [outNote, outVel, envTimeSum]);

			Out.kr(out, [outTrig, outNote/127, outVel, posSmooth]);
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TXMinMaxSliderSplit", "Strum mode", ControlSpec(1, 3, step: 1) , "strumMode", "strumModeMin", "strumModeMax"],
			["EZslider", "Strum position", ControlSpec(0, 1), "position"],
			["SynthOptionPopupPlusMinus", "Smoothing", arrOptionData, 0],
			["TXMinMaxSliderSplit", "Smooth Time 1", classData.timeSpec , "lag", "lagMin", "lagMax"],
			["TXMinMaxSliderSplit", "Smooth Time 2", classData.timeSpec , "lag2", "lag2Min", "lag2Max"],
			["EZslider", "Velocity min", ControlSpec(0, 1), "velocity"],
			["EZslider", "Random vel", ControlSpec(0, 1), "randVel"],
			["TXMinMaxSliderSplit", "Gate Time", classData.timeSpec , "envTime", "envTimeMin", "envTimeMax"],
			["TXCheckBox", "Use Custom Scale", "useCustomScale", {this.updateNoteList;}, 140],
			["TXPopupActionPlusMinus", "Key / root", ["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
				"key", {this.updateNoteList;}, 200],
			["TXListViewActionPlusMinus", "Chord/ scale", TXScale.arrScaleNames, "scale",
				{this.updateNoteList;}, 300, 130],
			["TXNoteRangeSlider", "Note range", "noteListMin", "noteListMax", {this.updateNoteList;}, true],
			["TXMinMaxSliderSplit", "Transpose", classData.transposeSpec , "transpose", "transposeMin", "transposeMax"],
			["SynthOptionPopupPlusMinus", "Quantise transpose", arrOptionData, 1, 400],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make the buffer, load the synthdef and create the synth
		this.makeBuffersAndSynth(classData.arrBufferSpecs);
		this.oscControlActivate;
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// buffer store
			this.getNoteArray;
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Outputs", {displayOption = "showOutputs";
				this.buildGuiSpecArray; system.showView;}, 80,
				TXColor.white, this.getButtonColour(displayOption == "showOutputs")],
			["Spacer", 3],
			["ActionButton", "Strum", {displayOption = "showStrum";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showStrum")],
			["Spacer", 3],
			["ActionButton", "Scale & Transpose", {displayOption = "showScale";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showScale")],
			["Spacer", 30],
			["TXStaticText", "Notes in scale", {this.getNoteTotalText}, {arg view; noteListTextView = view;}, 160],
			["Spacer", 30],
			["TXCheckBox", "Active", "active", nil, 80],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showOutputs", {
			holdPortNames = ["Unconnected - select MIDI Port"]
			++ MIDIClient.destinations.collect({arg item, i;
				// remove any brackets from string
				(item.device.asString + item.name.asString).replace("(", "").replace(")", "")
			});
			guiSpecArray = guiSpecArray ++[
				["TextBarLeft", "Modules to be triggered:", 140],
				["SpacerLine", 6],
				["SeqSelect3GroupModules", "noteOutModule1", "noteOutModule2", "noteOutModule3",
					"noteOutModuleID1", "noteOutModuleID2", "noteOutModuleID3"],
				["SpacerLine", 6],
				["SeqSelect3GroupModules", "noteOutModule4", "noteOutModule5", "noteOutModule6",
					"noteOutModuleID4", "noteOutModuleID5", "noteOutModuleID6"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXCheckBox", "Output MIDI notes", "midiNoteOn", nil, 140],
				["SpacerLine", 6],
				["TXPopupActionPlusMinus", "MIDI Port", holdPortNames, "midiPort",
					{ arg view; this.initMidiPort(view.value); }, 300],
				["Spacer", 10],
				["TXCheckBox", "MIDI Loop-Back", "midiLoopBack", nil, 120],
				["SpacerLine", 4],
				["EZSlider", "MIDI Channel", ControlSpec(1, 16, step: 1), "midiChannel", nil, 510],
			];
		});
		if (displayOption == "showStrum", {
			guiSpecArray = guiSpecArray ++[
				["TXStaticText", "Strum mode", "1. Up & Down  2. Up Only  3. Down Only", {}, 340],
				["Spacer", 20],
				["TXMinMaxSliderSplit", "Strum mode", ControlSpec(1, 3, step: 1),
					"strumMode", "strumModeMin", "strumModeMax", nil, nil, nil, nil, 300],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Strum position", ControlSpec(0, 1), "position"],
				["SpacerLine", 6],
				["SynthOptionListPlusMinus", "Smoothing", arrOptionData, 0, 500],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Smooth time 1", classData.timeSpec , "lag", "lagMin", "lagMax"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Smooth time 2", classData.timeSpec , "lag2", "lag2Min", "lag2Max"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Velocity min", ControlSpec(0, 1), "velocity"],
				["SpacerLine", 6],
				["EZslider", "Random vel", ControlSpec(0, 1), "randVel"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Gate Time", classData.timeSpec , "envTime", "envTimeMin", "envTimeMax"],
			];
		});
		if (displayOption == "showScale", {
			guiSpecArray = guiSpecArray ++[
				["MIDIKeyboard", {arg note; this.toggleNoteInScale(note);},
					7, 80, 710, 24, {arg note;}, "C0 - B7", false, nil, nil, {arrScaleNotes}],
				["SpacerLine", 4],
				["TXCheckBox", "Use Custom Scale", "useCustomScale", {this.updateNoteList;}, 140],
				["Spacer", 3],
				["TXStaticText", "Customise: ",
					"If Use Custom Scale is on, click or drag on keyboard notes to add or remove from scale",
					{}, 560],
				["SpacerLine", 6],
				["TXPopupActionPlusMinus", "Key / root", ["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
					"key", {this.updateNoteList;}, 200],
				["Spacer", 28],
				["TXListViewActionPlusMinus", "Chord/ scale", TXScale.arrScaleNames, "scale",
					{this.updateNoteList;}, 400, 110],
				["SpacerLine", 6],
				["TXNoteRangeSlider", "Note range", "noteListMin", "noteListMax", {this.updateNoteList;}, true],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Transpose", classData.transposeSpec , "transpose", "transposeMin", "transposeMax"],
				["SpacerLine", 6],
				["SynthOptionPopupPlusMinus", "Quantise tsp", arrOptionData, 1, 450],
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

	////////////////////////////////////

	toggleNoteInScale {arg note;
		// if using custom scale
		if (this.getSynthArgSpec("useCustomScale") == 1, {
			// if note not found, add
			if (arrScaleNotes.indexOf(note).isNil, {
				arrScaleNotes = arrScaleNotes.add(note);
				arrScaleNotes.sort;
			}, {
				// else remove
				arrScaleNotes.remove(note);
			});
			this.updateNoteList;
		});
	}

	updateNoteList {
		this.getNoteArray;
		system.flagGuiIfModDisplay(this);
	}

	getNoteArray {
		var arrScaleSpec, scaleRoot, noteMin, noteMax;

		// if using set scale, generate notes
		if (this.getSynthArgSpec("useCustomScale") == 0, {
			// Generate array of notes from chord, mode, scale
			arrScaleSpec = TXScale.arrScaleNotes.at(this.getSynthArgSpec("scale"));
			scaleRoot = this.getSynthArgSpec("key");
			noteMin = this.getSynthArgSpec("noteListMin");
			noteMax = this.getSynthArgSpec("noteListMax");
			arrScaleNotes = [];
			13.do({arg octave;
				arrScaleSpec.do({arg item, i;
					arrScaleNotes = arrScaleNotes.add((octave * 12) + scaleRoot + item);
				});
			});
			arrScaleNotes = arrScaleNotes.select({arg item, i; ((item >= noteMin) and: (item <= noteMax)); });
		});
		// update synth & buffer
		this.setSynthValue("noteListSize", arrScaleNotes.size);
		buffers.at(0).sendCollection(arrScaleNotes);

		^arrScaleNotes;
	}

	getNoteTotalText {
		var noteListSize, outText;
		noteListSize = this.getSynthArgSpec("noteListSize");
		if (noteListSize == 0, {
			outText = "ERROR: No notes in scale";
		}, {
			outText = "  " ++ noteListSize.asString;
		});
		^outText;
	}

	arrActiveModules {
		var arrModules;
		arrModules = [noteOutModule1, noteOutModule2, noteOutModule3, noteOutModule4, noteOutModule5, noteOutModule6,
		].select({ arg item, i; item.notNil and: {item != 0}});
		^arrModules;
	}

	playNote {arg outNote, vel, envTime;
		var outVel;
		outVel = vel * 127;
		this.arrActiveModules.do({ arg module, i;
			// create note in module
			module.createSynthNote(outNote, outVel, envTime);
		});
		// output midi
		this.sendMIDINoteOn(outNote, outVel, envTime);
	}

	////////////////////////////////////

	oscControlActivate {
		//	stop any previous responder
		this.oscControlDeactivate;
		oscResponder = OSCFunc({ arg msg, time, addr,  recvPort;
			var note, vel, envTime;
			// For testing  - post details
			// "OSC time, msg : ".postln;
			// [time, msg].postln;
			if (moduleNode.notNil, {
				// check nodeID
				if (msg.at(1) == moduleNode.nodeID, {
					note = msg.at(3);
					vel = msg.at(4);
					envTime = msg.at(5);
					this.playNote(note, vel, envTime);
				});
			});
		}, '/strum', system.server.addr);
	}

	oscControlDeactivate {
		if (oscResponder.notNil, {
			oscResponder.free;
		});
	}

	////////////////////////////////////

	restoreOutputs {
		var holdID;
		holdID = this.getSynthArgSpec("noteOutModuleID1");
		if (holdID.notNil, {noteOutModule1 = system.getModuleFromID(holdID)}, {noteOutModule1 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleID2");
		if (holdID.notNil, {noteOutModule2 = system.getModuleFromID(holdID)}, {noteOutModule2 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleID3");
		if (holdID.notNil, {noteOutModule3 = system.getModuleFromID(holdID)}, {noteOutModule3 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleID4");
		if (holdID.notNil, {noteOutModule4 = system.getModuleFromID(holdID)}, {noteOutModule4 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleID5");
		if (holdID.notNil, {noteOutModule5 = system.getModuleFromID(holdID)}, {noteOutModule5 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleID6");
		if (holdID.notNil, {noteOutModule6 = system.getModuleFromID(holdID)}, {noteOutModule6 = nil});
	}

	checkDeletions {
		// check if any note out modules are going to be deleted - if so remove them as outputs
		if (noteOutModule1.notNil, {
			if (noteOutModule1.deletedStatus == true, {
				noteOutModule1 = nil;
				this.setSynthArgSpec("noteOutModuleID1", nil);
			});
		});
		if (noteOutModule2.notNil, {
			if (noteOutModule2.deletedStatus == true, {
				noteOutModule2 = nil;
				this.setSynthArgSpec("noteOutModuleID2", nil);
			});
		});
		if (noteOutModule3.notNil, {
			if (noteOutModule3.deletedStatus == true, {
				noteOutModule3 = nil;
				this.setSynthArgSpec("noteOutModuleID3", nil);
			});
		});
		if (noteOutModule4.notNil, {
			if (noteOutModule4.deletedStatus == true, {
				noteOutModule4 = nil;
				this.setSynthArgSpec("noteOutModuleID4", nil);
			});
		});
		if (noteOutModule5.notNil, {
			if (noteOutModule5.deletedStatus == true, {
				noteOutModule5 = nil;
				this.setSynthArgSpec("noteOutModuleID5", nil);
			});
		});
		if (noteOutModule6.notNil, {
			if (noteOutModule6.deletedStatus == true, {
				noteOutModule6 = nil;
				this.setSynthArgSpec("noteOutModuleID6", nil);
			});
		});
	}

	////////////////////////////////////

	extraSaveData {
		^[holdMIDIDeviceName, holdMIDIPortName, arrScaleNotes];
	}

	loadExtraData {arg argData = [];
		var portIndex;
		portIndex = this.getSynthArgSpec("midiPort");
		holdMIDIDeviceName = argData.at(0) ? nil;
		holdMIDIPortName = argData.at(1) ? nil;
		arrScaleNotes =  argData.at(2) ? [];
		// if names given, find correct port from names
		if ( holdMIDIDeviceName.notNil and: holdMIDIPortName.notNil, {
			// default to 0 in case device/port names not found
			portIndex = 0;
			this.setSynthArgSpec("midiPort", 0);
			MIDIClient.destinations.do({arg item, i;
				if ( (holdMIDIDeviceName == item.device) and: (holdMIDIPortName == item.name), {
					portIndex = i + 1;
					this.setSynthArgSpec("midiPort", portIndex);
				});
			});
		});
		this.initMidiPort(portIndex);
		this.restoreOutputs;
		this.buildGuiSpecArray;
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;

			// wait for sync before
			this.updateNoteList;
			this.oscControlActivate;

			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
	}

	deleteModuleExtraActions {
		this.oscControlDeactivate;
	}

	////////////////////////////////////

	initMidiPort { arg portInd = 0;
		if (portInd == 0, {
			holdMIDIOutPort = nil;
			holdMIDIDeviceName = nil;
			holdMIDIPortName = nil;
		},{
			holdMIDIOutPort = MIDIOut(portInd - 1, MIDIClient.destinations[portInd - 1].uid);
			holdMIDIDeviceName =  MIDIClient.destinations[portInd - 1].device;
			holdMIDIPortName =  MIDIClient.destinations[portInd - 1].name;
			// minimise MIDI out latency
			holdMIDIOutPort.latency = 0;
		});
	}

	sendMIDINoteOn { arg outNote, outVel, gateTime, delay = 0;
		var portInd, channel, midiLoopBack, midiNoteOn;
		midiNoteOn = this.getSynthArgSpec("midiNoteOn");
		if (midiNoteOn == 1, {
			portInd = this.getSynthArgSpec("midiPort");
			midiLoopBack = this.getSynthArgSpec("midiLoopBack");
			channel = this.getSynthArgSpec("midiChannel") - 1;
			if (midiLoopBack == 1, {
				SystemClock.sched( delay,{
					MIDIIn.doNoteOnAction(0, channel, outNote, outVel);
					nil;
				});
				if (gateTime > 0, {
					SystemClock.sched( delay + gateTime,{
						MIDIIn.doNoteOffAction(0, channel, outNote, 0);
						nil;
					});
				});
			});
			if (portInd > 0, {
				// "Note On & Off"
				SystemClock.sched( delay,{
					holdMIDIOutPort.noteOn(channel, outNote, outVel);
					nil;
				});
				if (gateTime > 0, {
					SystemClock.sched( delay + gateTime,{
						holdMIDIOutPort.noteOff(channel, outNote, 0);
						nil;
					});
				});
			});
		});
	}

	sendMIDINoteOff { arg outNote, outVel, gateTime, delay = 0;
		var portInd, channel, midiLoopBack, midiNoteOn;
		midiNoteOn = this.getSynthArgSpec("midiNoteOn");
		if (midiNoteOn == 1, {
			portInd = this.getSynthArgSpec("midiPort");
			midiLoopBack = this.getSynthArgSpec("midiLoopBack");
			channel = this.getSynthArgSpec("midiChannel") - 1;
			if (midiLoopBack == 1, {
				SystemClock.sched( delay,{
					MIDIIn.doNoteOnAction(0, channel, outNote, outVel);
					nil;
				});
				if (gateTime > 0, {
					SystemClock.sched( delay + gateTime,{
						MIDIIn.doNoteOffAction(0, channel, outNote, 0);
						nil;
					});
				});
			});
			if (portInd > 0, {
				// "Note On & Off"
				SystemClock.sched( delay,{
					holdMIDIOutPort.noteOn(channel, outNote, outVel);
					nil;
				});
				if (gateTime > 0, {
					SystemClock.sched( delay + gateTime,{
						holdMIDIOutPort.noteOff(channel, outNote, 0);
						nil;
					});
				});
			});
		});
	}

}

