// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXPolyNoteTrigger : TXModuleBase {

	classvar <>classData;

	var <>noteOutModule1;
	var <>noteOutModule2;
	var <>noteOutModule3;
	var <>noteOutModule4;
	var <>noteOutModule5;
	var <>noteOutModule6;
	var	oscResponder, storedNote, isWaitingToGate;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "PolyNote Trigger";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Gate", 1, "modGate", 0],
			["Fixed Time", 1, "modFixedTime", 0],
			["Note", 1, "modNote", 0],
			["Velocity", 1, "modVelocity", 0],
		];
		classData.noOutChannels = 3;
		classData.arrOutBusSpecs = [
			["Gate", [0]],
			["Note", [1]],
			["Velocity", [2]],
		];
		classData.timeSpec = ControlSpec(0.01, 20);
		classData.noteSpec = ControlSpec(0, 127, 'lin', step: 1);
		classData.velocitySpec = ControlSpec(0, 127, 'lin', step: 1);
		classData.guiWidth = 600;
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
		//	set  class specific instance variables
		moduleNodeStatus = "running";
		isWaitingToGate = false;

		arrSynthArgSpecs = [
			["out", 0, 0],
			["gate", 0, 0],
			["useFixedTime", 0, 0],
			["fixedTime", ControlSpec(0.01, 5).unmap(0.5), 0],
			["fixedTimeMin", 0.01, 0],
			["fixedTimeMax", 5, 0],
			["note", classData.noteSpec.unmap(60), 0],
			["noteMin", 0, 0],
			["noteMax", 127, 0],
			["velocity", classData.velocitySpec.unmap(100), 0],
			["velocityMin", 0, 0],
			["velocityMax", 127, 0],
			["modGate", 0, 0],
			["modUseFixedTime", 0, 0],
			["modFixedTime", 0, 0],
			["modNote", 0, 0],
			["modVelocity", 0, 0],
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["noteOutModuleID1", nil],
			["noteOutModuleID2", nil],
			["noteOutModuleID3", nil],
			["noteOutModuleID4", nil],
			["noteOutModuleID5", nil],
			["noteOutModuleID6", nil],
		];
		synthDefFunc = {arg out, gate, useFixedTime, fixedTime, fixedTimeMin, fixedTimeMax,
			note, noteMin, noteMax, velocity, velocityMin, velocityMax,
			modGate = 0, modUseFixedTime = 0, modFixedTime = 0, modNote = 0, modVelocity = 0;

			var gateSum = (gate + modGate).max(0).min(1);
			var useFixedTimeSum = (useFixedTime + modUseFixedTime).max(0).min(1);
			var fixedTimeSum = LinLin.kr((fixedTime + modFixedTime).max(0).min(1), 0, 1,
				fixedTimeMin, fixedTimeMax).max(0.001);
			var noteSum = LinLin.kr((note + modNote).max(0).min(1), 0, 1, noteMin, noteMax);
			var velocitySum = LinLin.kr((velocity + modVelocity).max(0).min(1), 0, 1, velocityMin, velocityMax);
			var noteOnTrig = Trig1.kr(gateSum, 0.01);
			var noteOffTrig = Trig1.kr((1 - gateSum), 0.01);

			// gate on
			SendReply.kr(noteOnTrig, '/gate', [useFixedTimeSum * fixedTimeSum, noteSum, velocitySum], 1);
			// gate off
			SendReply.kr(noteOffTrig, '/gate', [0], 0);

			Out.kr(out, [gateSum, noteSum / 127, velocitySum / 127]);
		};

		guiSpecArray = [
			["NextLine"],
			["TextBarLeft", "Modules to be triggered:", 140],
			["NextLine"],
			["SeqSelect3GroupModules", "noteOutModule1", "noteOutModule2", "noteOutModule3",
				"noteOutModuleID1", "noteOutModuleID2", "noteOutModuleID3"],
			["SpacerLine", 2],
			["SeqSelect3GroupModules", "noteOutModule4", "noteOutModule5", "noteOutModule6",
				"noteOutModuleID4", "noteOutModuleID5", "noteOutModuleID6"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 10],
			["TXCheckBox", "Gate", "gate"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 10],
			["TXCheckBox", "Use Fixed Time", "useFixedTime"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Fixed Time", classData.timeSpec, "fixedTime", "fixedTimeMin", "fixedTimeMax"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 10],
			["TXMinMaxMidiNoteSldr", "Note", classData.noteSpec, "note", "noteMin", "noteMax", nil, TXFreqRangePresets.arrMidiNoteRanges],
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "Velocity", classData.velocitySpec, "velocity", "velocityMin", "velocityMax"],
			["SpacerLine", 2],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
		this.oscControlActivate;
	}

	// override default method
	loadExtraData {arg argData, isPresetData = false;
		var holdGate = this.getSynthArgSpec("gate");
		if (holdGate == 1 and: (isPresetData == false), {
			isWaitingToGate = true;
			this.setSynthValue("gate", 0);
		});
		{this.oscControlActivate;}.defer(2);
	}

	oscControlActivate {
		//	stop any previous responder
		this.oscControlDeactivate;
		oscResponder = OSCFunc({ arg msg, time, addr,  recvPort;
			//		For testing  - post details
			//		"xxx : ".postln;
			//		[time, msg].postln;
			if (moduleNode.notNil, {
				// check nodeID
				if (msg.at(1) == moduleNode.nodeID, {
					// run actions
					this.performActions(msg);
				});
			});
		}, '/gate', system.server.addr);
	}

	oscControlDeactivate {
		if (oscResponder.notNil, {
			oscResponder.free;
		});
	}

	arrActiveModules {
		var arrModules;
		// retrigger modules
		arrModules = [noteOutModule1, noteOutModule2, noteOutModule3, noteOutModule4, noteOutModule5, noteOutModule6,
		].select({ arg item, i; item.notNil and: {item != 0}});
		^arrModules;
	}

	performActions {arg msg;
		var fixedTime, note, velocity;
		var seqLatencyOn = 0;
		// gate on
		if (msg[2] == 1, {
			fixedTime = msg[3];
			note = msg[4];
			if (fixedTime == 0, {
				storedNote = note;
			}, {
				storedNote = nil;
			});
			velocity = msg[5];
			// for all active modules create note
			this.arrActiveModules.do({ arg item, i;
				item.createSynthNote(note, velocity, fixedTime, seqLatencyOn);
			});
		});
		// gate off
		if (msg[2] == 0, {
			if (storedNote.notNil, {
				// for all active modules release note
				this.arrActiveModules.do({ arg item, i;
					item.releaseSynthGate(storedNote, seqLatencyOn);
				});
				storedNote = nil;
			});
		});
	}

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
		// only turn gate on when modules have been restored
		if (isWaitingToGate, {
			isWaitingToGate = false;
			this.setSynthValue("gate", 1);
		});
	}

}

