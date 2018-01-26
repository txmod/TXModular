// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMultiTrackSeq : TXModuleBase {

	// related classes: TXMultiTrackTools TXMultiTrackView TXTrackView
	/*
	NOTE - the following code generates the list below:
	(
	"// ==== Class Methods ====================== ".postln;
	a = TXMultiTrackSeq.class.methods;
	b = a.collect ({ arg item, i; item.name.asString;});
	b.sort.do ({ arg item, i; ("// " ++ item).postln;});
	" ".postln;
	"// ==== Instance Methods ====================== ".postln;
	a = TXMultiTrackSeq.methods;
	b = a.collect ({ arg item, i; item.name.asString;});
	b.sort.do ({ arg item, i; ("// " ++ item).postln;});
	"// ================================================".postln;
	)
	*/
	// ==== Class Methods ======================
	// classData
	// classData_
	// initClass
	// new
	// restoreAllOutputs
	// stopAllSequencers
	// syncStartAllSequencers
	// syncStopAllSequencers

	// ==== Instance Methods ======================
	// actionTapTempo
	// addMarkerAtCurrentPosition
	// addMarkerAtPosition
	// addTrack
	// adjustLoopEnd
	// arrTracks
	// arrTracks_
	// bpmUpdated
	// buildGuiSpecArray
	// calculateEndTime
	// changeTrackIndex
	// checkDeletions
	// currentPlayTime
	// currentPlayTime_
	// dataBank
	// dataBank_
	// deleteMarkerAtCurrentPosition
	// deleteMarkerAtPosition
	// deleteModuleExtraActions
	// deleteTrack
	// duplicateTrack
	// extraSaveData
	// getNextStepID
	// getTrackFromID
	// goToPosition
	// goToStartPos
	// infLoopCheck
	// init
	// initMidiPort
	// jumpToNextMarker
	// jumpToPreviousMarker
	// loadExtraData
	// midiLearnTrackID
	// midiLearnTrackID_
	// oscControlActivate
	// oscControlDeactivate
	// rebuildArrMarkerTimes
	// rebuildMidiOutPorts
	// refreshCurrentTimeViews
	// refreshScaledUserViews
	// registerCurrentTimeView
	// renumberTracks
	// restartSequencerFromTime
	// restoreAllTrackMonitorFuncs
	// restoreTrackMonitorRecordFuncs
	// runningStatus
	// runningStatusView
	// runningStatusView_
	// runningStatus_
	// scaledUserViews
	// scaledUserViews_
	// scheduleNextSection
	// scrollToCurrentPlayTime
	// sendMIDIMessage
	// seqClock
	// seqCurrentStep
	// seqRunning
	// setDefaultActionVariables
	// setMidiLearnFunc
	// setModulationInputVal
	// setSelectionEnd
	// setSelectionStart
	// buildTrackMonitorFuncs
	// buildTrackRecordFuncs
	// setTrackSelectionEnd
	// setTrackSelectionStart
	// shiftPosition
	// shiftSelectionStartEnd
	// showTrack
	// startNewLoop
	// startNewSeqClock
	// startSequencer
	// stopMidiLearn
	// stopRecording
	// stopSequencer
	// syncStartSequencer
	// syncStopSequencer
	// toolFuncs
	// toolFuncs_
	// updateCurrentTime
	// updateEndTime
	// updateRunningStatus
	// updateScaledUserViews
	// useTapTempo
	// ================================================

	classvar <>classData;

	var <>arrTracks;
	var <>scaledUserViews;
	var <>runningStatus;
	var <>runningStatusView;
	var <>displayTime;
	var <>displayTimeView;
	var <>toolFuncs;
	var <>showMultitrackProcesses;
	var arrMidiOutPorts;
	var currentSeqClock;
	var <seqCurrentStep;
	var <seqRunning = false;
	var <>currentPlayTime;
	var arrCurrentTimeViews;
	var extraDeltaTime;
	var holdSeqClockSecs;
	var oldClock;
	var infLoopCount = 0;
	var holdVisibleOrigin;
	var holdTapTime, newTapTime;
	var currentStepID;
	var midiMonitorControllerFuncs;
	var midiMonitorNoteOnFuncs;
	var midiMonitorNoteOffFuncs;
	var midiRecordControllerFuncs;
	var midiRecordNoteOnFuncs;
	var midiRecordNoteOffFuncs;
	var midiLearnFunc;
	var	modulationInputMonitorFuncs;
	var	modulationInputRecordFuncs;
	var	directInputMonitorFuncs;
	var	directInputRecordFuncs;
	var <>midiLearnTrackID;
	var recordedControllerVals, recordedNoteOns, recordedNoteOnOffs;
	var currentScheduleTime;
	var seqStopTimeBeats;
	var <>dataBank;
	var holdModule;

	*initClass {
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "MultiTrack Seq";
		classData.moduleRate = "control";
		classData.moduleType = "action";
		classData.arrCtlSCInBusSpecs = [
			["Modulation 1", 1, "mod01", 0],
			["Modulation 2", 1, "mod02", 0],
			["Modulation 3", 1, "mod03", 0],
			["Modulation 4", 1, "mod04", 0],
			["Modulation 5", 1, "mod05", 0],
			["Modulation 6", 1, "mod06", 0],
			["Modulation 7", 1, "mod07", 0],
			["Modulation 8", 1, "mod08", 0],
			["Modulation 9", 1, "mod09", 0],
			["Modulation 10", 1, "mod10", 0],
			["Modulation 11", 1, "mod11", 0],
			["Modulation 12", 1, "mod12", 0],
			["Modulation 13", 1, "mod13", 0],
			["Modulation 14", 1, "mod14", 0],
			["Modulation 15", 1, "mod15", 0],
			["Modulation 16", 1, "mod16", 0],
		];
		classData.noInChannels = 0;
		classData.noOutChannels = 0;
		classData.guiWidth = 1080;
		classData.defaultActionStep = [99,0,0,0,0,0,0, nil, 0, 0.0, 1, 100, 1, 1001, 0];
		// actionStep.at(0) is ModuleID
		// actionStep.at(1) is Action Index
		// actionStep.at(2) is value 1
		// actionStep.at(3) is value 2
		// actionStep.at(4) is value 3
		// actionStep.at(5) is value 4
		// actionStep.at(6) is not used
		// actionStep.at(7) is Action Text
		// actionStep.at(8) is Select switch
		// actionStep.at(9) is Time
		// actionStep.at(10) is On switch
		// actionStep.at(11) is Probablity
		// actionStep.at(12) is Step No.
		// actionStep.at(13) is Step ID
		// actionStep.at(14) is Track No.
		classData.defaultTrack = ();
		classData.defaultTrack.trackType = 'Note';  // 'Note'/'Controller'/    TODO:/'Action'
		classData.defaultTrack.trackID = nil;  // automatically allocated on creation
		classData.defaultTrack.trackNo = nil;  // automatically allocated on creation
		classData.defaultTrack.name = "";
		classData.defaultTrack.selected = 0;
		classData.defaultTrack.mute = 0;
		classData.defaultTrack.offset = 0;
		classData.defaultTrack.monitorInput = 0; // for Note & Controller tracks
		classData.defaultTrack.recordArmed = 0; // for Note & Controller tracks
		classData.defaultTrack.midiPort = 0; // for Note & Controller tracks
		classData.defaultTrack.midiChannelMin  = 0; // for Note & Controller tracks
		classData.defaultTrack.midiChannelMax  = 15; // for Note & Controller tracks
		classData.defaultTrack.midiControllerNo = 0; // for Controller tracks
		classData.defaultTrack.midiKeybScroll = 0.5; // for Note tracks
		classData.defaultTrack.velocityScale = 1; // for Note tracks
		classData.defaultTrack.transpose = 0; // for Note tracks
		classData.defaultTrack.output1Active = 0; // for Note & Controller tracks
		classData.defaultTrack.output1ModuleID = 0; // for Note & Controller tracks
		classData.defaultTrack.output1ParameterName = ""; // for Controller tracks
		classData.defaultTrack.output2Active = 0; // for Note & Controller tracks
		classData.defaultTrack.output2ModuleID = 0; // for Note & Controller tracks
		classData.defaultTrack.output2ParameterName = ""; // for Controller tracks
		classData.defaultTrack.output3Active = 0; // for Note & Controller tracks
		classData.defaultTrack.output3ModuleID = 0; // for Note & Controller tracks
		classData.defaultTrack.output3ParameterName = ""; // for Controller tracks
		classData.defaultTrack.output4Active = 0; // for Note & Controller tracks
		classData.defaultTrack.output4ModuleID = 0; // for Note & Controller tracks
		classData.defaultTrack.output4ParameterName = ""; // for Controller tracks
		classData.defaultTrack.output5Active = 0; // for Note & Controller tracks
		classData.defaultTrack.output5ModuleID = 0; // for Note & Controller tracks
		classData.defaultTrack.output5ParameterName = ""; // for Controller tracks
		classData.defaultTrack.output6Active = 0; // for Note & Controller tracks
		classData.defaultTrack.output6ModuleID = 0; // for Note & Controller tracks
		classData.defaultTrack.output6ParameterName = ""; // for Controller tracks
	}

	*new { arg argInstName;
		^super.new.init(argInstName);
	}

	*restoreAllOutputs {
		// empty
	}

	*syncStartAllSequencers {
		classData.arrInstances.do({ arg item, i;
			item.syncStartSequencer;
		});
	}

	*syncStopAllSequencers {
		classData.arrInstances.do({ arg item, i;
			item.syncStopSequencer;
		});
	}

	*stopAllSequencers {
		classData.arrInstances.do({ arg item, i;
			item.stopSequencer;
		});
	}

	////////////////////////////////////

	init {arg argInstName;
		holdModule = this;
		toolFuncs = TXMultiTrackTools.new(this, system);
		arrTracks = []; // empty array of tracks
		midiMonitorControllerFuncs = ();
		midiMonitorNoteOnFuncs = ();
		midiMonitorNoteOffFuncs = ();
		midiRecordControllerFuncs = ();
		midiRecordNoteOnFuncs = ();
		midiRecordNoteOffFuncs = ();
		modulationInputMonitorFuncs = ();
		modulationInputRecordFuncs = ();
		directInputMonitorFuncs = ();
		directInputRecordFuncs = ();
		holdVisibleOrigin = Point.new(0,0);
		recordedControllerVals = ();
		recordedNoteOns = ();
		recordedNoteOnOffs = ();
		seqStopTimeBeats = 0;
		scaledUserViews = ();
		currentPlayTime = 0;
		arrMidiOutPorts = ();
		displayTime = " 1 : 1";
		showMultitrackProcesses = 0;

		dataBank = ();
		dataBank.deltaTimeBeats = 4;
		dataBank.arrMarkers = ();
		dataBank.arrMarkerTimes = [];
		dataBank.numModulationInputs = 16;
		dataBank.arrSendTrigIDs = {UniqueID.next} ! dataBank.numModulationInputs;
		dataBank.arrModulationInputVals = 0 ! dataBank.numModulationInputs;

		arrSynthArgSpecs = [
			["mod01", 0, defLagTime],
			["mod02", 0, defLagTime],
			["mod03", 0, defLagTime],
			["mod04", 0, defLagTime],
			["mod05", 0, defLagTime],
			["mod06", 0, defLagTime],
			["mod07", 0, defLagTime],
			["mod08", 0, defLagTime],
			["mod09", 0, defLagTime],
			["mod10", 0, defLagTime],
			["mod11", 0, defLagTime],
			["mod12", 0, defLagTime],
			["mod13", 0, defLagTime],
			["mod14", 0, defLagTime],
			["mod15", 0, defLagTime],
			["mod16", 0, defLagTime],

			//	n.b. using arrSynthArgSpecs just as a place to store the following variables for use with guiSpecArray
			//  it takes advantage of the  gui objects saving values to arrSynthArgSpecs as well as it being already
			//   saved and loaded with other data
			["syncStart", 1],
			["syncRecord", 0],
			["syncStop", 1],
			["muteSeq", 0],
			["timeLock", 1],
			["bpm", 120],
			["oldBpm", 120],
			["beatsPerBar", 4],
			["multiSelectionStart", 0],
			["multiSelectionEnd", 0],
			["loop", 0],
			["loopStart", 16],
			["loopEnd", 24],
			["holdNextStepID", 1003],
			["autoTapTempo", 0],
			["trackDisplayIndex", 0],
			["newTrackTypeIndex",0],
			["holdNextTrackID", 1001],
			["startPosBeats", 0],
			["stopPosBeats", 40000],
			["displayRangeStart", 1],
			["displayRangeEnd", 17],
			["maxDisplayBars", 16],
			["seqLastEventEndTime", 0],
			["snapToGrid", 1],
			["playFromStart", 1],
			["snapBeats", 1],
			["actionIndex", 0],
			["actionVar1", 0],
			["actionVar2", 0],
			["dragDrawsMultiPoints", 1],
		];
		arrOptions = [2];
		arrOptionData = [
			[	["60 times per second", 60],
				["30 times per second", 30],
				["20 times per second - default", 20],
				["10 times per second", 10],
				["5 times per second", 5],
				["once every second", 1],
				["once every 2 seconds", 0.5],
				["once every 4 seconds", 0.25],
			],
		];
		synthDefFunc = { arg mod01=0, mod02=0, mod03=0, mod04=0, mod05=0, mod06=0, mod07=0, mod08=0, mod09=0, mod10=0, mod11=0,
			mod12=0, mod13=0, mod14=0, mod15=0, mod16=0;
			// select datarate based on arrOptions
			var dataRate = this.getSynthOption(0);
			var invImpulse = 1 - Impulse.kr(dataRate);
			// trigger current value to be sent when values change
			[mod01, mod02, mod03, mod04, mod05, mod06, mod07, mod08, mod09, mod10, mod11,
				mod12, mod13, mod14, mod15, mod16].collect({arg item, i;
				var holdVal = item.max(0).min(1);
				var trig = Trig.kr(invImpulse * HPZ1.kr(holdVal).abs, 0.02);
				SendTrig.kr(trig, dataBank.arrSendTrigIDs[i], holdVal);
			});
		};

		seqCurrentStep = 0;
		seqRunning = false;
		this.updateRunningStatus('STOPPED');

		guiSpecTitleArray = [
			["TitleBar"],
			["Spacer", 3],
			["HelpButton"],
			["Spacer", 3],
			["DeleteModuleButton"],
			["Spacer", 3],
			["ActionButton", "Rewind", {this.stopSequencer; this.goToStartPos}, 50, nil, TXColor.sysGuiCol2],
			["Spacer", 3],
			["ActionButton", "Play", {this.startSequencer}, 40, nil, TXColor.sysGuiCol2],
			["Spacer", 3],
			["ActionButton", "Stop", {this.stopSequencer}, 40, nil, TXColor.sysGuiCol2],
			["Spacer", 3],
			["ActionButton", "Record", {this.startSequencer(startRecording: true)}, 50, nil, TXColor.sysRecordCol],
			["Spacer", 3],
			["TXStaticText", "Status", {this.runningStatus},
				{arg view; if ((view.class == TextView) || (view.class == TextField), {runningStatusView = view},
					{runningStatusView = view.textView}); }, 120, 40, TXColor.paleYellow],
			["Spacer", 3],
			["SeqSyncStartCheckBox"],
			["Spacer", 3],
			["TXCheckBox", "Sync Record", "syncRecord", nil, 90, 20, 0, TXColor.white, TXColor.sysRecordCol],
			["Spacer", 3],
			["SeqSyncStopCheckBox"],
			["Spacer", 3],
			["TXCheckBox", "Mute all tracks", "muteSeq", nil, 100, 20, 0, TXColor.white, TXColor.sysGuiCol2],
			["Spacer", 3],
			["HideModuleButton"],
			["NextLine"],
			["ModuleActionPopup", 500],
			["ModuleInfoTxt", 500],
			["SpacerLine", 2],
		];
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			// commandAction
			// arguments- index1 is action name, index2 is action function,
			//   index3 is optional array of controlspec functions
			//   index4 is optional guiObjectType
			//   index5 is optional getItemsFunction
			["TXStaticText", "Time display", {this.displayTime},
				{arg view; if ((view.class == TextView) || (view.class == TextField), {displayTimeView = view},
					{displayTimeView = view.textView}); }, 120, 40, TXColor.white],
			["TXStaticText", "MultiTrack Selection Start", {this.displayMultiSelectionStart},
				{arg view; }, 120, 40, TXColor.white],
			["TXStaticText", "MultiTrack Selection End", {this.displayMultiSelectionEnd},
				{arg view; }, 120, 40, TXColor.white],
			["TXStaticText", "Loop Start", {this.displayLoopStart},
				{arg view; }, 120, 40, TXColor.white],
			["TXStaticText", "Loop End", {this.displayLoopEnd},
				{arg view; }, 120, 40, TXColor.white],
			["commandAction", "Start Sequencer Play", {this.startSequencer;}],
			["commandAction", "Start Sequencer Record", {this.startSequencer(startRecording: true);}],
			["commandAction", "Stop Sequencer", {this.stopSequencer;}],
			["commandAction", "Stop Seq & Go to Start", {this.stopSequencer; this.goToStartPos;}],
			["commandAction", "Stop Seq & Go to Position (bars/beats)",
				{arg val1,val2; this.stopSequencer; this.goToPosition(val1 - 1, val2 - 1);},
				[ControlSpec(1, 10000, default: 1), ControlSpec(-10, 10000, default: 1)],
				\number
			],
			["commandAction", "Stop Seq & Shift Position (bars/beats)",
				{arg val1,val2; this.stopSequencer; this.shiftPosition(val1,val2);},
				[ControlSpec(-10000, 10000, default: 1), ControlSpec(-10000, 10000, default: 0)],
				\number
			],
			["commandAction", "Stop Seq & Go to previous marker", {this.stopSequencer; this.jumpToPreviousMarker;}],
			["commandAction", "Stop Seq & Go to next marker", {this.stopSequencer; this.jumpToNextMarker;}],
			["commandAction", "Add marker at current position", {this.addMarkerAtCurrentPosition;}],
			["commandAction", "Delete marker at current position", {this.deleteMarkerAtCurrentPosition;}],
			["TXCheckBox", "Always Play From Start", "playFromStart", nil, 160],
			//	["SeqSyncStartCheckBox"],
			["TXCheckBox", "Mute all tracks", "muteSeq", nil, 450],
			["EZNumber", "BPM", ControlSpec(1, 999), "bpm", {arg view; this.bpmUpdated(view.value); }],
			["commandAction", "Tap Tempo", {this.actionTapTempo;}],
			["TXCheckBox", "Auto copy tap tempo", "autoTapTempo", nil, 180],
			["EZNumber", "Beats per bar", ControlSpec(1, 999), "beatsPerBar", {this.updateEndTime; system.showView(this);}],
			// ["TXStaticText", "Status", {this.runningStatus},
			// 	{arg view; if ((view.class == TextView) || (view.class == TextField), {runningStatusView = view},
			// {runningStatusView = view.textView}); }, 150],
			["commandAction", "Set MultiTrack Selection Start (bars/beats)",
				{arg val1,val2; this.setSelectionStart(val1 - 1, val2 - 1);},
				[ControlSpec(1, 10000, default: 1), ControlSpec(1, 10000, default: 0)],
				\number
			],
			["commandAction", "Set MultiTrack Selection End (bars/beats)",
				{arg val1,val2; this.setSelectionEnd(val1 - 1, val2 - 1);},
				[ControlSpec(1, 10000, default: 2), ControlSpec(1, 10000, default: 1)],
				\number
			],
			["commandAction", "Shift MultiTrack Selection (bars/beats)",
				{arg val1,val2; this.shiftSelectionStartEnd(val1, val2);},
				[ControlSpec(-10000, 10000, default: 1), ControlSpec(-10000, 10000, default: 0)],
				\number
			],
			["commandAction", "Set Loop Start (bars/beats)",
				{arg val1,val2; this.setLoopStart(val1 - 1, val2 - 1);},
				[ControlSpec(1, 10000, default: 1), ControlSpec(1, 10000, default: 0)],
				\number
			],
			["commandAction", "Set Loop End (bars/beats)",
				{arg val1,val2; this.setLoopEnd(val1 - 1, val2 - 1);},
				[ControlSpec(1, 10000, default: 2), ControlSpec(1, 10000, default: 1)],
				\number
			],
			["commandAction", "Shift Loop (bars/beats)",
				{arg val1,val2; this.shiftLoopStartEnd(val1, val2);},
				[ControlSpec(-10000, 10000, default: 1), ControlSpec(-10000, 10000, default: 0)],
				\number
			],
			["commandAction", "Select all tracks", {
				toolFuncs.selectAllTracks(1);
				system.showView;
			}],
			["commandAction", "Unselect all tracks", {
				toolFuncs.selectAllTracks(0);
				system.showView;
			}],
			["commandAction", "Set monitor off for selected tracks", {
				toolFuncs.setMonitorForSelTracks(0);
				system.showView;
			}],
			["commandAction", "Set monitor on for selected tracks", {
				toolFuncs.setMonitorForSelTracks(1);
				system.showView;
			}],
			["commandAction", "Set record off for selected tracks", {
				toolFuncs.setRecordForSelTracks(0);
				system.showView;
			}],
			["commandAction", "Set record on for selected tracks", {
				toolFuncs.setRecordForSelTracks(1);
				system.showView;
			}],
			["commandAction", "Set mute off for selected tracks", {
				toolFuncs.muteSelectedTracks(0);
				system.showView;
			}],
			["commandAction", "Set mute on for selected tracks", {
				toolFuncs.muteSelectedTracks(1);
				system.showView;
			}],
			["commandAction", "Set mute off for all tracks", {
				toolFuncs.muteAllTracks(0);
				system.showView;
			}],
			["commandAction", "Set mute on for all tracks", {
				toolFuncs.muteAllTracks(1);
				system.showView;
			}],
			["commandAction", "Undo edit for selected tracks", {
				toolFuncs.undoEditForSelTracks;
				system.showView;
			}],
			["commandAction", "Redo edit for selected tracks", {
				toolFuncs.redoEditForSelTracks;
				system.showView;
			}],
			["commandAction", "Insert output 1 val at sel start in sel ctrl tracks", {
				toolFuncs.writeKeyframeInSelCtrlTracks(true);
				system.flagGuiUpd;
			}],
			["commandAction", "Overwrite selection with output 1 val in sel ctrl tracks", {
				toolFuncs.writeKeyframeInSelCtrlTracks(false);
				system.flagGuiUpd;
			}],
			["commandAction", "Output vals at current play time for sel ctrl tracks", {
				toolFuncs.outputCurrValInSelCtrlTracks;
				system.flagGuiUpd;
			}],
			["commandAction", "Set direct inputs to output 1 vals in sel ctrl tracks", {
				toolFuncs.setDirectInputToOut1ValInSelCtrlTracks;
			}],

			// track actions follow:
			["commandAction", "Set Track Select (select/trackID)",
				{arg val1, val2;
					var holdTrack = arrTracks.detect({arg item, i; item.trackID == val2;});
					if (holdTrack.notNil, {
						holdTrack.selected = val1;
					});
				},
				[ControlSpec(0, 1, step: 1), ControlSpec(1000, 5000, default: 1000)],
				\checkbox
			],
			["commandAction", "Set Track Monitor (monitor/trackID)",
				{arg val1, val2;
					var holdTrack = arrTracks.detect({arg item, i; item.trackID == val2;});
					if (holdTrack.notNil, {
						holdTrack.monitorInput = val1;
						this.buildTrackMonitorFuncs(holdTrack, val1.asBoolean);
					});
				},
				[ControlSpec(0, 1, step: 1), ControlSpec(1000, 5000, default: 1000)],
				\checkbox
			],
			["commandAction", "Set Track Record (record/trackID)",
				{arg val1, val2;
					var holdTrack = arrTracks.detect({arg item, i; item.trackID == val2;});
					if (holdTrack.notNil, {
						holdTrack.recordArmed = val1;
						this.buildTrackRecordFuncs(holdTrack, val1.asBoolean);
					});
				},
				[ControlSpec(0, 1, step: 1), ControlSpec(1000, 5000, default: 1000)],
				\checkbox
			],
			["commandAction", "Set Track Mute (mute/trackID)",
				{arg val1, val2;
					var holdTrack = arrTracks.detect({arg item, i; item.trackID == val2;});
					if (holdTrack.notNil, {
						holdTrack.mute = val1;
						system.showViewIfModDisplay(this);
					});
				},
				[ControlSpec(0, 1, step: 1), ControlSpec(1000, 5000, default: 1000)],
				\checkbox
			],
			["commandAction", "Set Track Offset (offset/trackID)",
				{arg val1, val2;
					var holdTrack = arrTracks.detect({arg item, i; item.trackID == val2;});
					if (holdTrack.notNil, {
						holdTrack.offset = val1;
					});
				},
				[ControlSpec(0, 1), ControlSpec(1000, 5000, default: 1000)],
				\number
			],
			["commandAction", "Set Track Selection Start (trackID/bars/beats)",
				{arg val1, val2, val3; this.setTrackSelectionStart(val1, val2 - 1, val3 - 1);},
				[ControlSpec(1000, 5000, default: 1000), ControlSpec(1, 10000, default: 1),
					ControlSpec(1, 10000, default: 1)],
				\number
			],
			["commandAction", "Set Track Selection End (trackID/bars/beats)",
				{arg val1, val2, val3; this.setTrackSelectionEnd(val1, val2 - 1, val3 - 1);},
				[ControlSpec(1000, 5000, default: 1000), ControlSpec(1, 10000, default: 2),
					ControlSpec(1, 10000, default: 1)],
				\number
			],
		]
		++ 100.collect({arg index;
			var actionString;
			actionString = "Set TrackID " ++ (1001 + index) ++" Direct Input";
			/* "valueActionNumber"
			// arguments- index1 is action name, index2 is array of controlspec functions,
			//   index3 is get value function, index4 is set value function,
			*/
			["valueActionNumber", actionString, [ControlSpec(0, 1)],
				{var outVal = 0;
					var holdTrack = arrTracks.detect({arg item, i; item.trackID == (1001 + index);});
					if (holdTrack.notNil, {
						outVal = holdTrack.directInputVal;
					});
					outVal;
				},
				{arg val1; this.setDirectInputVal(arrTracks.detect({arg item, i; item.trackID == (1001 + index);}), val1)},
			]
		});
		);
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.oscControlActivate;
		this.startNewSeqClock();
		this.loadAndMakeSynth;
	}
	////////////////////////////////////

	buildGuiSpecArray {
		var arrTrackNames, displayTrack, holdTrack;
		// init
		arrTrackNames = ["All tracks"] ++ arrTracks.collect({arg track, i; (i+1).asString ++ ": " ++ track.name;});
		displayTrack = this.getSynthArgSpec("trackDisplayIndex");

		guiSpecArray = [
			["TXPopupActionPlusMinus", "Track", arrTrackNames, "trackDisplayIndex",
				{ arg view; this.setSynthArgSpec("trackDisplayIndex", view.value); this.buildGuiSpecArray; system.showView(this);}, 226, 36],
			["ActionButton", "<All Tracks",
				{this.setSynthArgSpec("trackDisplayIndex", 0); this.buildGuiSpecArray; system.showView(this);},
				66, TXColor.white, TXColor.sysGuiCol1],
			["Spacer", 20],
			// action tracks removed for now
			//["ActionButton", "Add Track:", {this.addTrack;}, 60],
			//["TXPopupActionPlusMinus", "Type", ["Note", "Controller", "Action"], "newTrackTypeIndex", nil, 164, 34],
			//["TXPopupActionPlusMinus", "Type", ["Note", "Controller"], "newTrackTypeIndex", nil, 164, 34],
			["ActionButton", "Add Note Track", {this.addTrack('Note');}, 100],
			["ActionButton", "Add Controller Track", {this.addTrack('Controller');}, 120],
			["Spacer", 20],
			["EZNumber", "BPM", ControlSpec(1, 999), "bpm",
				{arg view; this.bpmUpdated(view.value); system.showView(this);}, 50, 50],
			["TapTempoButton", {arg argTempo; this.useTapTempo(argTempo); }, 120],
			["TXCheckBox", "Auto copy", "autoTapTempo", nil, 80],
			["Spacer", 20],
			["EZNumber", "Beats per bar", ControlSpec(1, 999), "beatsPerBar", {this.updateEndTime; system.showView(this);}],
			["NextLine"],
			["NextLine"],
			//["SpacerLine", 1],
			//["DividingLine", 1070],
		];

		if (displayTrack == 0, {
			// show multi track
			guiSpecArray = guiSpecArray ++[
				["TXMultiTrackView", this, {arg view; },
					{this.getSynthArgSpec("bpm");},
					{this.getSynthArgSpec("beatsPerBar");},
					{arg view; if (holdVisibleOrigin.notNil, {{view.visibleOrigin = holdVisibleOrigin}.defer(0.1);});},
					{arg view; holdVisibleOrigin = view.visibleOrigin; },
					nil,
					{arg view; dataBank.holdMultiTrackView = view;}
				]
			]
		}, {
			// edit a track
			holdTrack = arrTracks[displayTrack-1];

			// show Note or Controller track
			if ((holdTrack.trackType == 'Note') || (holdTrack.trackType == 'Controller'), {
				guiSpecArray = guiSpecArray ++[
					["TXTrackView", holdTrack, this,
						{arg view;  },
						{arg view;  },
						{arg view; dataBank.holdTrackView = view;}
					],
				];
			});
			/* FOR FUTURE:
			// show Action track
			if (holdTrack.trackType  == 'Action', {
			guiSpecArray = guiSpecArray ++[
			["TXActionSteps", {holdTrack.arrActionSteps;},
			{arg argArrActionSteps;  holdTrack.arrActionSteps = argArrActionSteps;},
			{this.getSynthArgSpec("displayFirstStep");},
			{this.getSynthArgSpec("bpm");},
			{this.getSynthArgSpec("beatsPerBar");},
			{this.getNextStepID;},
			{arg view; if (holdVisibleOrigin.notNil, {{view.visibleOrigin = holdVisibleOrigin}.defer(0.1);});},
			{arg view; holdVisibleOrigin = view.visibleOrigin; },
			{currentStepID;},
			{arg stepID;  currentStepID = stepID;}
			],
			];
			});
			*/
		});
	} // end of buildGuiSpecArray
	////////////////////////////////////

	oscControlActivate {
		//	remove any previous responder and add new
		this.oscControlDeactivate;
		dataBank.oscControlResp = OSCFunc({arg msg, time, addr,  recvPort;
			dataBank.arrSendTrigIDs.do({ arg argID, i;
				if (msg[2] == argID,{
					this.setModulationInputVal(i, msg[3]);
				});
			});
		}, '/tr', system.server.addr);
	}

	oscControlDeactivate {
		//	remove responder
		if (dataBank.oscControlResp.notNil, {
			dataBank.oscControlResp.free;
			dataBank.oscControlResp = nil;
		});
	}
	setModulationInputVal {arg argIndex, argVal;
		if (argIndex >= 0 and: (argIndex < dataBank.numModulationInputs), {
			dataBank.arrModulationInputVals[argIndex] = argVal;
			modulationInputMonitorFuncs.keysValuesDo({arg key, func; func.value(argIndex, argVal); });
			modulationInputRecordFuncs.keysValuesDo({arg key, func; func.value(argIndex, argVal); });
		});
	}

	setDirectInputVal {arg argTrack, argVal;
		if (argTrack.notNil, {
			argVal = argVal ? 0;
			argTrack.directInputVal = argVal;
			directInputMonitorFuncs[argTrack.trackID].value(argVal);
			directInputRecordFuncs[argTrack.trackID].value(argVal);
			this.updateDirectInputText(argTrack, argVal);
		});
	}

	updateDirectInputText {arg argTrack, argVal;
		var displayTrack = this.getSynthArgSpec("trackDisplayIndex");
		var holdTrack = arrTracks[displayTrack-1];
		var holdSpecs, holdItem, holdVal, holdString;
		if (argTrack == holdTrack and: {dataBank.directInputText.notNil} and: {dataBank.directInputText.notClosed}, {
			// [holdControlSpec, holdPopupItems, holdGuiObjectType]
			holdSpecs = toolFuncs.getOut1SpecsForTrack(argTrack);
			if (holdSpecs[0].isKindOf(ControlSpec), {
				holdVal = holdSpecs[0].map(argVal ? 0).round(0.0001);
				holdString = holdVal.asString;
				if (holdSpecs[1].notNil, {
					holdItem = holdSpecs[1][holdVal.asInteger];
					holdString = holdString ++ ": " ++ holdItem;
				});
				if (holdSpecs[2] == \checkbox, {
					if (holdVal == 0, {
						holdString = holdString ++ ": Off";
					},{
						holdString = holdString ++ ": On";
					});
				});
			}, {
				holdString = "";
			});
			{dataBank.directInputText.string = "Output Module 1 value: " ++ holdString}.defer;
		});
	}

	addTrack {arg argType;
		var holdTrack;
		holdTrack = classData.defaultTrack.deepCopy;
		// if type not passed, used index
		if (argType.class == Symbol, {
			holdTrack.trackType = argType;
		},{
			holdTrack.trackType = ['Note', 'Controller', /*'Action'*/].at(this.getSynthArgSpec("newTrackTypeIndex"));
		});
		if (holdTrack.trackType == 'Note', {
			holdTrack.arrNotes = [];
			holdTrack.arrEditNotes = [];
			holdTrack.displayMode = 'inputsOutputs'; // options: 'inputsOutputs'/'drawNotes'/'editNotes'
			holdTrack.inputType = 'Midi'; // options: 'Midi' only (currently)
			holdTrack.showProcesses = 0;
			holdTrack.processIndex = 0;
			holdTrack.processVar1 = 1;
			holdTrack.processVar2 = 1;
			holdTrack.snapNoteStartToGrid = 0;
			holdTrack.snapNoteEndToGrid = 0;
			holdTrack.snapSelection = 0;
			holdTrack.snapBeats = 0.25;
			holdTrack.showSnapGrid = 1;
			holdTrack.showVelocityTrack = 1;
			holdTrack.previousShowVelocity = 0;
			holdTrack.selectionStart = 0;
			holdTrack.selectionEnd = 0;
			holdTrack.selectionIndices = [];
			holdTrack.autoSelectNotes = false;
			holdTrack.defaultVelocity = 100;
			holdTrack.visibleOrigin = 0;
			holdTrack.outputMidiActive = 0;
			holdTrack.midiDeviceName = nil;
			holdTrack.midiPortInd = 0;
			holdTrack.midiPortName = nil;
			holdTrack.outputMidiChannel = 0;
			holdTrack.outputMidiLoopback = 0;
		});
		if (holdTrack.trackType == 'Controller', {
			holdTrack.arrControllerVals = [];
			holdTrack.arrEditControllerVals = [];
			holdTrack.displayMode = 'inputsOutputs'; // options: 'inputsOutputs'/'drawPoints'/'drawLine'/'editSelection'
			holdTrack.inputType = 'Midi'; // options: 'Midi'/'Modulation Input'/'Direct Input'
			holdTrack.modulationInputIndex = 0;
			holdTrack.directInputVal = 0;
			holdTrack.showProcesses = 0;
			holdTrack.processIndex = 0;
			holdTrack.processVar1 = 1;
			holdTrack.processVar2 = 1;
			holdTrack.snapToGrid = 0;
			holdTrack.snapBeats = 1/16;
			holdTrack.showSnapGrid = 1;
			holdTrack.quantizeValue = 0;
			holdTrack.quantizeValueSteps = 2;
			holdTrack.selectionStart = 0;
			holdTrack.selectionEnd = 0;
			holdTrack.selectionIndices = [];
			holdTrack.lineWarpIndex = 0;
			holdTrack.useBendData = 0;
			holdTrack.outputMidiActive = 0;
			holdTrack.midiDeviceName = nil;
			holdTrack.midiPortInd = 0;
			holdTrack.midiPortName = nil;
			holdTrack.outputMidiChannel = 0;
			holdTrack.outputMidiControllerNo = 0;
			holdTrack.outputMidiPitchBend = 0;
			holdTrack.outputMidiLoopback = 0;
		});
		// if (holdTrack.trackType == 'Action', {
		// 	holdTrack.arrActionSteps = [];
		// 	holdTrack.displayMode = 'editActions'; // options: 'editActions'
		// });
		holdTrack.arrTimes = [];
		holdTrack.lastEventEndTime = 0;
		// assign & increment holdNextTrackID
		holdTrack.trackID = this.getSynthArgSpec("holdNextTrackID");
		this.setSynthArgSpec("holdNextTrackID", holdTrack.trackID + 1);
		holdTrack.trackNo = arrTracks.size + 1;
		arrTracks = arrTracks.add(holdTrack);
		this.setSynthArgSpec("trackDisplayIndex", 0); // show multitrack view
		this.buildGuiSpecArray;
		system.showView(this);
		^holdTrack;
	}

	duplicateTrack {arg track;
		var holdTrack;
		holdTrack = track.deepCopy;
		// assign & increment holdNextTrackID
		holdTrack.trackID = this.getSynthArgSpec("holdNextTrackID");
		this.setSynthArgSpec("holdNextTrackID", holdTrack.trackID + 1);
		holdTrack.trackNo = arrTracks.size + 1;
		holdTrack.monitorInput = 0;   // turn of monitor & record
		holdTrack.recordArmed = 0;
		arrTracks = arrTracks.add(holdTrack);
		// force track to be after copied track
		this.changeTrackIndex(holdTrack, track.trackNo);
		this.buildGuiSpecArray;
	}

	getNextStepID {
		var outStepID;
		outStepID = this.getSynthArgSpec("holdNextStepID");
		this.setSynthArgSpec("holdNextStepID", outStepID + 1);
		^outStepID;
	}

	////////////////////////////////////

	goToStartPos {
		this.updateCurrentTime(this.getSynthArgSpec("startPosBeats"));
	}

	goToPosition {arg bars = 1, beats = 1;
		var beatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var newPos = (bars * beatsPerBar) + beats;
		this.updateCurrentTime(newPos.max(0));
	}

	shiftPosition {arg bars = 1, beats = 1;
		var beatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var newPos = currentPlayTime + (bars * beatsPerBar) + beats;
		this.updateCurrentTime(newPos.max(0));
	}

	setLoopStart {arg bars = 1, beats = 1;
		var oldLoopStart = this.getSynthArgSpec("loopStartStart");
		var oldLoopEnd = this.getSynthArgSpec("loopEnd");
		var beatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var newPos = ((bars * beatsPerBar) + beats).max(0);
		if (newPos <= oldLoopEnd, {
			holdModule.setSynthArgSpec("loopStart", newPos);
		}, {
			holdModule.setSynthArgSpec("loopStart", oldLoopEnd);
			holdModule.setSynthArgSpec("loopEnd", newPos);
		});
	}

	setLoopEnd {arg bars = 1, beats = 1;
		var oldLoopStart = this.getSynthArgSpec("loopStart");
		var oldLoopEnd = this.getSynthArgSpec("loopEnd");
		var beatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var newPos = ((bars * beatsPerBar) + beats).max(0);
		if (newPos >= oldLoopStart, {
			holdModule.setSynthArgSpec("loopEnd", newPos);
		}, {
			holdModule.setSynthArgSpec("loopStart", newPos);
			holdModule.setSynthArgSpec("loopEnd", oldLoopStart);
		});
	}

	shiftLoopStartEnd {arg bars = 1, beats = 0;
		var oldLoopStart = this.getSynthArgSpec("loopStart");
		var oldLoopEnd = this.getSynthArgSpec("loopEnd");
		var beatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var shiftBeats = ((bars * beatsPerBar) + beats).max(oldLoopStart.neg);
		holdModule.setSynthArgSpec("loopStart", oldLoopStart + shiftBeats);
		holdModule.setSynthArgSpec("loopEnd", oldLoopEnd + shiftBeats);
	}

	setSelectionStart {arg bars = 1, beats = 1;
		var oldSelStart = this.getSynthArgSpec("multiSelectionStart");
		var oldSelEnd = this.getSynthArgSpec("multiSelectionEnd");
		var beatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var newPos = ((bars * beatsPerBar) + beats).max(0);
		if (newPos <= oldSelEnd, {
			holdModule.setSynthArgSpec("multiSelectionStart", newPos);
		}, {
			holdModule.setSynthArgSpec("multiSelectionStart", oldSelEnd);
			holdModule.setSynthArgSpec("multiSelectionEnd", newPos);
		});
	}

	setSelectionEnd {arg bars = 1, beats = 1;
		var oldSelStart = this.getSynthArgSpec("multiSelectionStart");
		var oldSelEnd = this.getSynthArgSpec("multiSelectionEnd");
		var beatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var newPos = ((bars * beatsPerBar) + beats).max(0);
		if (newPos >= oldSelStart, {
			holdModule.setSynthArgSpec("multiSelectionEnd", newPos);
		}, {
			holdModule.setSynthArgSpec("multiSelectionStart", newPos);
			holdModule.setSynthArgSpec("multiSelectionEnd", oldSelStart);
		});
	}

	shiftSelectionStartEnd {arg bars = 1, beats = 0;
		var oldSelStart = this.getSynthArgSpec("multiSelectionStart");
		var oldSelEnd = this.getSynthArgSpec("multiSelectionEnd");
		var beatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var shiftBeats = ((bars * beatsPerBar) + beats).max(oldSelStart.neg);
		holdModule.setSynthArgSpec("multiSelectionStart", oldSelStart + shiftBeats);
		holdModule.setSynthArgSpec("multiSelectionEnd", oldSelEnd + shiftBeats);
	}

	setTrackSelectionStart {arg trackID = 0,  bars = 1, beats = 1;
		var beatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var newPos = ((bars * beatsPerBar) + beats).max(0);
		var holdTrack = arrTracks.detect({arg item, i; trackID == item.trackID;});
		var oldSelStart, oldSelEnd;
		if (holdTrack.notNil, {
			oldSelStart = holdTrack.selectionStart;
			oldSelEnd = holdTrack.selectionEnd;
			if (newPos <= oldSelEnd, {
				holdTrack.selectionStart = newPos;
			}, {
				holdTrack.selectionStart = oldSelEnd;
				holdTrack.selectionEnd = newPos;
			});
		});
	}

	setTrackSelectionEnd {arg trackID = 0,  bars = 1, beats = 1;
		var beatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var newPos = ((bars * beatsPerBar) + beats).max(0);
		var holdTrack = arrTracks.detect({arg item, i; trackID == item.trackID;});
		var oldSelStart, oldSelEnd;
		if (holdTrack.notNil, {
			oldSelStart = holdTrack.selectionStart;
			oldSelEnd = holdTrack.selectionEnd;
			if (newPos >= oldSelStart, {
				holdTrack.selectionEnd = newPos;
			}, {
				holdTrack.selectionStart = newPos;
				holdTrack.selectionEnd = oldSelStart;
			});
		});
	}

	///////////////////////////////////

	seqClock {
		^currentSeqClock;
	}

	startNewSeqClock {arg startTime = nil;
		var lastTime;
		if (startTime.isNil, {
			startTime = this.getSynthArgSpec("startPosBeats");
		});
		currentSeqClock = TempoClock.new (this.getSynthArgSpec("bpm")/60, startTime, queueSize: 256 * 1024);
		// add regular update function
		currentSeqClock.schedAbs(currentSeqClock.beats,{arg beats, time, clock;
			var returnVal;
			if (runningStatus != 'STOPPED', {
				holdModule.updateCurrentTime(beats);
				returnVal = 0.25 * holdModule.getSynthArgSpec("bpm")/60; // run every quarter beat
			},{
				returnVal = nil;
			});
			returnVal;
		});
	}

	startNewLoop {
		var holdLoopStart = this.getSynthArgSpec("loopStart");
		oldClock = currentSeqClock;
		oldClock.clear;
		SystemClock.schedAbs( thisThread.seconds + 0.02,{
			oldClock.stop;
		});
		this.startNewSeqClock(startTime: holdLoopStart);
		// start new seq at loopStart
		this.startSequencer(startRecording:false, startTime: holdLoopStart);
	}

	restartSequencerFromTime {arg argTime;
		currentSeqClock.stop;
		this.startNewSeqClock(startTime: argTime);
		// start new seq at argTime
		this.startSequencer(startRecording:false, startTime: argTime);
	}

	startSequencer {arg startRecording = false, startTime = nil;
		if (deletedStatus != true, {
			if ((runningStatus == 'STOPPED'), {
				this.updateRunningStatus('PLAYING');
			});
			// check if recording
			if (startRecording, {
				this.updateRunningStatus('RECORDING');
				// activate each track
				arrTracks.do({arg track, item;
					if (track.recordArmed.asBoolean, {
						this.buildTrackRecordFuncs(track, true);
					});
				});
			});
			// start clock from play position if not already running
			if (seqRunning != true, {
				seqRunning = true;
				if (this.getSynthArgSpec("playFromStart") == 1, {
					currentScheduleTime = this.getSynthArgSpec("startPosBeats");
				}, {
					currentScheduleTime = currentPlayTime;
				});
				this.startNewSeqClock(startTime: currentScheduleTime);
			});
			// overwrite currentScheduleTime if passed as arg
			if (startTime.notNil, {
				currentScheduleTime = startTime;
			});
			this.seqClock.schedAbs(this.seqClock.beats,{
				var returnVal;
				var stopPos;
				stopPos = min(holdModule.getSynthArgSpec("stopPosBeats"), holdModule.getSynthArgSpec("seqLastEventEndTime"));
				//var loopOn = holdModule.getSynthArgSpec("loop");
				// schedule at least once so controller track lookback runs
				holdModule.scheduleNextSection;
				// stop if after last event and Playing, but not Recording
				if ((runningStatus == 'PLAYING') && (holdModule.seqClock.beats > stopPos), {
					holdModule.stopSequencer;
					returnVal = nil;
				}, {
					// else reschedule every deltaTimeBeats
					returnVal = dataBank.deltaTimeBeats;
				});
				returnVal;
			});
		});
	}

	scheduleNextSection {
		// -schedule events for next 2 x deltaTimeBeats
		// -re-schedule again every deltaTimeBeats beats, so always deltaTimeBeats+  ahead
		var nextScheduleTime, endScheduleTime, holdLoopEnd;
		var stopPos = this.getSynthArgSpec("stopPosBeats");
		nextScheduleTime = currentScheduleTime + dataBank.deltaTimeBeats;
		endScheduleTime = currentScheduleTime + (dataBank.deltaTimeBeats * 2);
		// check for loop
		if (this.getSynthArgSpec("loop").asBoolean == true, {
			holdLoopEnd = this.getSynthArgSpec("loopEnd");
			if ((holdLoopEnd >= currentScheduleTime) &&( holdLoopEnd < nextScheduleTime) && ( holdLoopEnd <= stopPos), {
				// schedule new loop
				if (runningStatus != 'STOPPED', {
					this.seqClock.schedAbs(holdLoopEnd,{
						holdModule.startNewLoop;
						nil; // return nil so no reschedule
					});
				});
			});
			if ((holdLoopEnd >= currentScheduleTime) &&( holdLoopEnd < endScheduleTime) && ( holdLoopEnd <= stopPos), {
				endScheduleTime = holdLoopEnd;
			});
		});
		// schedule stop if needed
		if (stopPos < endScheduleTime, {
			endScheduleTime = stopPos;
			if (runningStatus != 'STOPPED', {
				this.seqClock.schedAbs(endScheduleTime,{
					holdModule.stopSequencer;
					nil; // return nil so no reschedulebeatDur
				});
			});
		});
		arrTracks.do({arg track, item;
			var schedTimeBeats, holdIndex;
			var trackOffset = track.offset;
			// 6 outputs
			6.do({arg count;
				var numString = (count + 1).asString;
				var moduleIDKey = ("output" ++ numString ++ "ModuleID").asSymbol;
				var outputActiveKey = ("output" ++ numString ++ "Active").asSymbol;
				var parameterNameKey = ("output" ++ numString ++ "ParameterName").asSymbol;
				var isActive, isFirstEvent;
				if (count == 0, {
					isActive = (track[outputActiveKey] == 1) || (track.outputMidiActive == 1);
				},{
					isActive = (track[outputActiveKey] == 1);
				});
				if ((isActive == true), {
					// note
					if (track.trackType == 'Note', {
						if (track.arrNotes.size > 0, {
							if (track.arrNotes.last[0] >= currentScheduleTime, {
								// get start index
								holdIndex = track.arrTimes.indexOf(currentScheduleTime);
								if (holdIndex.isNil, {
									holdIndex = track.arrTimes.indexInBetween(currentScheduleTime).roundUp;
								});
								while( {(holdIndex < track.arrNotes.size)
									and: {track.arrNotes[holdIndex][0] < endScheduleTime}}, {
									var holdEvent;
									holdEvent = track.arrNotes[holdIndex];
									// check is running
									if (runningStatus != 'STOPPED', {
										this.seqClock.schedAbs(holdEvent[0] + trackOffset,{arg beats, seconds, clock;
											var holdNote, holdVel, holdGateTime;
											// check mute
											if (track.mute.asBoolean != true, {
												holdNote = holdEvent[1] + track.transpose;
												holdVel = holdEvent[2] * track.velocityScale;
												holdGateTime = holdEvent[3];
												// play note
												if ((track[moduleIDKey] != 0) && (track[outputActiveKey] == 1), {
													toolFuncs.performAction (track[moduleIDKey], 0,
														"Play note: midi note + velocity + gate time ", holdNote, holdVel,
														holdGateTime * holdModule.seqClock.beatDur);
												});
												// output midi
												if ((count == 0) && (track.outputMidiActive == 1), {  // don't do multiple times!
													holdModule.sendMIDIMessage(track, 'Note', outNote: holdNote, outVel: holdVel,
														gateTime: holdGateTime * holdModule.seqClock.beatDur);
												});
											});
											// return nil so no reschedule
											nil;
										});
									});
									holdIndex = holdIndex + 1;
								});
							});
						});
					});
					// Controller
					if (track.trackType == 'Controller', {
						if (track.arrControllerVals.size > 0, {
							// get start index
							holdIndex = track.arrTimes.indexOf(currentScheduleTime);
							if (holdIndex.isNil, {
								// get preceding event
								holdIndex = max(track.arrTimes.indexInBetween(currentScheduleTime).floor, 0);
							});
							isFirstEvent = true;
							// schedule events for next 16 beats
							while( {(holdIndex < track.arrControllerVals.size) and:
								{track.arrControllerVals[holdIndex][0] < endScheduleTime}}, {
								var holdEvent, holdTime;
								holdEvent = track.arrControllerVals[holdIndex];
								if (isFirstEvent, {
									holdTime = currentScheduleTime;
									isFirstEvent = false;
								}, {
									holdTime = holdEvent[0];
								});
								if (runningStatus != 'STOPPED', {
									this.seqClock.schedAbs(holdTime + trackOffset,{
										var holdVal;
										// check mute
										if (track.mute.asBoolean != true, {
											holdVal = holdEvent[1];
											// don't play controller events if recording
											if (((runningStatus != 'RECORDING') || (track.recordArmed.asBoolean != true)), {
												if ((track[moduleIDKey] != 0) && (track[outputActiveKey] == 1), {
													toolFuncs.performAction (track[moduleIDKey], 0, track[parameterNameKey],
														holdVal, mapValues: true);
												});
												// output midi
												if ((count == 0) && (track.outputMidiActive == 1), {  // only do once
													if (track.outputMidiPitchBend == 1, {
														holdModule.sendMIDIMessage(track, 'Pitchbend', outControlVal: holdVal);
													},{
														holdModule.sendMIDIMessage(track, 'Controller', outControlVal: holdVal);
													});
												});
											});
										});
										// return nil so no reschedule
										nil;
									});
								});
								holdIndex = holdIndex + 1;
							});
						});
					});

				});
			}); // end of 6.do

			/* TESTING XXX - ADD ACTION TRACK CODE HERE =============================
			if (track.trackType == 'Action', {
			//track.arrActionSteps = [];
			});
			*/

		});
		// update time
		currentScheduleTime = endScheduleTime;
	}

	stopSequencer {
		// stop tempo clocks
		if (seqRunning == true, {
			seqStopTimeBeats = this.seqClock.beats;
			currentPlayTime = seqStopTimeBeats;
			this.refreshCurrentTimeViews;
			currentSeqClock.stop;
			seqRunning = false;
		});
		if (runningStatus == 'RECORDING', {
			this.stopRecording(seqStopTimeBeats);
		});
		this.updateRunningStatus('STOPPED');
	}

	stopRecording { arg endRecTime;
		var endTime;
		// reset var
		endTime = 0;
		arrTracks.do({arg track, i;
			var holdGroupEvent, holdNoteOn, holdNoteStartTime, holdNoteLength, holdNoteNum, holdVel;
			// deactivate track
			if (track.recordArmed.asBoolean == true, {
				this.buildTrackRecordFuncs(track, false);
			});
			if (track.trackType == 'Controller', {
				if (recordedControllerVals[track.trackID].asArray.size > 0, {
					toolFuncs.doWithHistory(track, {
						toolFuncs.mergeNewControllerVals(track, recordedControllerVals[track.trackID], endRecTime);
						recordedControllerVals[track.trackID] = [];
					});
				});
			});
			if (track.trackType == 'Note', {
				// end any unfinished notes
				holdGroupEvent = recordedNoteOns[track.trackID];
				if (holdGroupEvent.notNil, {
					holdGroupEvent.keysDo({arg key, i;
						// get relevant noteOn
						holdNoteOn = holdGroupEvent[key];
						holdNoteStartTime = holdNoteOn[0];
						holdNoteLength = max((seqStopTimeBeats - (system.seqLatency * 2)) - holdNoteOn[0], 0.01);
						holdNoteNum = holdNoteOn[1];
						holdVel = holdNoteOn[2];
						// add noteOnOff - format [StartTime, NoteNum, Vel, NoteLength, StopTime]
						recordedNoteOnOffs[track.trackID] =
						recordedNoteOnOffs[track.trackID].asArray
						.add([holdNoteStartTime, holdNoteNum, holdVel, holdNoteLength, holdNoteStartTime + holdNoteLength]);
						// remove noteOn
						holdGroupEvent[key] = nil;
					});
				});
				// merge & sort events, rebuild arrTimes & clear record array
				if (recordedNoteOnOffs[track.trackID].asArray.size > 0, {
					toolFuncs.doWithHistory(track, {
						toolFuncs.mergeNewNoteOnOffs(track, recordedNoteOnOffs[track.trackID]);
						recordedNoteOnOffs[track.trackID] = [];
					});
				});
			});
			// keep track of last time for all tracks
			endTime = max(endTime, track.lastEventEndTime ? 0);
		});
		// update
		this.updateEndTime(endTime);
		system.showView(this);
	}

	calculateEndTime {
		var loopEndTime = this.getSynthArgSpec("loopEnd");
		var multiSelectionEndTime = this.getSynthArgSpec("multiSelectionEnd");
		var endTime = loopEndTime;
		endTime = max(endTime, multiSelectionEndTime);
		arrTracks.do({arg track, i;
			endTime = max(endTime, track.lastEventEndTime ? 0);
		});
		this.setSynthArgSpec("seqLastEventEndTime", endTime);
	}

	updateEndTime {arg argEndTime;
		var oldMaxBars, newMaxBars, endTime, bpb;
		oldMaxBars = this.getSynthArgSpec("maxDisplayBars");
		endTime = this.getSynthArgSpec("seqLastEventEndTime");
		if (argEndTime.notNil, {
			endTime = max(endTime, argEndTime);
			this.setSynthArgSpec("seqLastEventEndTime", endTime);
		});
		bpb = this.getSynthArgSpec("beatsPerBar");
		newMaxBars = (endTime / bpb).roundUp;
		// add extra;
		newMaxBars = max(newMaxBars + 8, 16);
		newMaxBars = max(newMaxBars, oldMaxBars);
		this.setSynthArgSpec("maxDisplayBars", newMaxBars);
		// check display range
		this.setSynthArgSpec("displayRangeEnd", min(this.getSynthArgSpec("displayRangeEnd"), newMaxBars+1));
		this.setSynthArgSpec("displayRangeStart", min(this.getSynthArgSpec("displayRangeStart"), newMaxBars));
		if (oldMaxBars != newMaxBars, {
			system.showView(this);
		});
	}

	infLoopCheck {
		var infLoopFound;
		infLoopFound = false;
		if (holdSeqClockSecs == this.seqClock.seconds, {
			infLoopCount = infLoopCount + 1;
		},{
			holdSeqClockSecs = this.seqClock.seconds;
			infLoopCount = 0;
		});
		if (infLoopCount > 100, {
			this.stopSequencer;
			TXInfoScreen.new("WARNING: " ++ this.moduleNameinstName ++
				" has been stopped because an infinite loop has been found.  " ++
				" Please check logic before running again."
			);
			infLoopFound = true;
		});
		^infLoopFound;
	}

	syncStartSequencer {
		// if syncStart is 1 then start sequencer
		if (this.getSynthArgSpec("syncRecord") == 1, {
			this.startSequencer(startRecording: true);
			system.showViewIfModDisplay(this);
		},{
			if (this.getSynthArgSpec("syncStart") == 1, {
				this.startSequencer;
				system.showViewIfModDisplay(this);
			});
		});
	}

	syncStopSequencer {
		// if syncStop is 1 then stop sequencer
		if (this.getSynthArgSpec("syncStop") == 1, {
			this.stopSequencer;
		});
	}

	updateRunningStatus { arg argStatus;
		runningStatus = argStatus;
		{
			if (runningStatusView.notNil, {
				if (runningStatusView.notClosed, {
					runningStatusView.string = " " ++ runningStatus.asString; // space out
				});
			});
		}.defer;
	}

	////////////////////////////////////

	bpmUpdated { arg argBpm;
		if (runningStatus != 'STOPPED', {
			currentSeqClock.tempo = argBpm/60;
		});
		system.flagGuiIfModDisplay(this);
	}

	checkDeletions {
		// 	dummy method - not used
	}

	deleteModuleExtraActions { // override base module
		arrTracks.do({arg track, i;
			this.buildTrackMonitorFuncs(track, false);
			this.buildTrackRecordFuncs(track, false);
		});
		this.stopMidiLearn;
		this.oscControlDeactivate;
	}

	////////////////////////////////////

	extraSaveData {
		var arrTracksAsPairs, arrMarkersAsPairs;
		var arrData;
		arrTracksAsPairs = arrTracks.collect({arg track, i;
			track.asKeyValuePairs;
		});
		arrMarkersAsPairs = dataBank.arrMarkers.asKeyValuePairs;
		arrData = [arrTracksAsPairs, arrMarkersAsPairs];
		^arrData;
	}

	loadExtraData {arg argData, isPresetData = false;
		var arrTracksAsPairs, arrMarkersAsPairs;
		this.stopSequencer;
		arrTracksAsPairs = argData[0];
		arrTracks = arrTracksAsPairs.collect({arg item, i;
			Event.newFrom(item);
		});
		arrMarkersAsPairs = argData[1] ? [];
		dataBank.arrMarkers = Event.newFrom(arrMarkersAsPairs);
		this.rebuildArrMarkerTimes;
		this.buildGuiSpecArray;
		this.restoreAllTrackMonitorFuncs;
		this.rebuildMidiOutPorts;
	}

	////////////////////////////////////

	useTapTempo {arg argTempo;
		var autoBPM;
		autoBPM = this.getSynthArgSpec("autoTapTempo");
		if (autoBPM == 1,{
			if ((argTempo >= 1) and: (argTempo <= 999),{
				this.setSynthArgSpec("bpm", argTempo);
				this.bpmUpdated(argTempo);
				system.flagGuiIfModDisplay(this);
			});
		});
	}

	actionTapTempo {	// tap tempo function used by module action
		var holdBPM;
		if (newTapTime.isNil, {
			newTapTime = Main.elapsedTime
		}, {
			holdTapTime = Main.elapsedTime;
			holdBPM = 60 / (holdTapTime - newTapTime);
			newTapTime = holdTapTime;
			this.useTapTempo(holdBPM);
		});
	}

	////////////////////////////////////

	stopMidiLearn {
		// remove any old funcs
		if (midiLearnFunc.notNil, {
			midiLearnFunc.free;
			midiLearnFunc = nil;
		});
		midiLearnTrackID = nil;
	}

	setMidiLearnFunc { arg track, isActive;
		// remove any old funcs
		this.stopMidiLearn;
		// add new funcs
		if (isActive == true, {
			midiLearnTrackID = track.trackID;
			if (track.trackType == 'Controller', {
				if (track.useBendData == 1, {
					midiLearnFunc = MIDIFunc.bend({ |val, chan|
						this.stopMidiLearn;
						track.midiChannelMin  = chan;
						track.midiChannelMax  = chan;
						system.showView(this);
					});
				},{
					midiLearnFunc = MIDIFunc.cc({ |val, num, chan|
						this.stopMidiLearn;
						track.midiChannelMin  = chan;
						track.midiChannelMax  = chan;
						track.midiControllerNo = num;
						system.showView(this);
					});
				});
			});
			if (track.trackType == 'Note', {
				midiLearnFunc = MIDIFunc.noteOn({ |vel, num, chan|
					this.stopMidiLearn;
					track.midiChannelMin  = chan;
					track.midiChannelMax  = chan;
					system.showView(this);
				});
			});
		});
	}

	////////////////////////////////////

	updateCurrentTime { arg newTime = 0;
		var holdBeatsPerBar, holdCurrTimeSoloBeats, holdCurrTimeBars, holdString;
		currentPlayTime = newTime.max(0);
		// defer gui update
		{
			if (displayTimeView.notNil, {
				if (displayTimeView.notClosed, {
					holdBeatsPerBar = this.getSynthArgSpec("beatsPerBar");
					holdCurrTimeBars = (this.currentPlayTime / holdBeatsPerBar).floor;
					holdCurrTimeSoloBeats = (this.currentPlayTime - (holdCurrTimeBars * holdBeatsPerBar)).round(0.01);
					displayTime = " " ++ (1 + holdCurrTimeBars) ++ " : " ++ (1 + holdCurrTimeSoloBeats);
					displayTimeView.string = displayTime;
				});
			});
			if (arrCurrentTimeViews.asArray[0].notNil, {
				if (arrCurrentTimeViews[0].view.notClosed, {
					holdBeatsPerBar = this.getSynthArgSpec("beatsPerBar");
					if ((currentPlayTime >= ((this.getSynthArgSpec("displayRangeStart")-1) * holdBeatsPerBar))
						&& (currentPlayTime < ((this.getSynthArgSpec("displayRangeEnd")-1) * holdBeatsPerBar)), {
							this.refreshCurrentTimeViews;
						}, {
							this.scrollToCurrentPlayTime;
							this.updateScaledUserViews;
							this.refreshCurrentTimeViews;
					});
				});
			});
		}.defer;
	}

	registerCurrentTimeView { arg view, clearFirst = false;
		if (clearFirst, {
			arrCurrentTimeViews = [];
		});
		arrCurrentTimeViews = arrCurrentTimeViews.add(view);
	}

	refreshCurrentTimeViews {
		{ // defer
			if (arrCurrentTimeViews.asArray[0].notNil, {
				if (arrCurrentTimeViews[0].view.notClosed, {
					arrCurrentTimeViews.do({arg item; item.refresh;});
				});
			});
			if (dataBank.holdTrackView.notNil, {
				dataBank.holdTrackView.updateCurrentPlayTimeText;
				dataBank.holdTrackView.updateCurrentMarkerLabel;
			});
			if (dataBank.holdMultiTrackView.notNil, {
				dataBank.holdMultiTrackView.updateCurrentPlayTimeText;
				dataBank.holdMultiTrackView.updateCurrentMarkerLabel;
			});
		}.defer;
	}

	displayMultiSelectionStart {
		var holdTime = this.getSynthArgSpec("multiSelectionStart");
		var holdBeatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var holdTimeBars = (holdTime / holdBeatsPerBar).floor;
		var holdTimeSoloBeats = (holdTime - (holdTimeBars * holdBeatsPerBar)).round(0.01);
		var outString = " " ++ (1 + holdTimeBars) ++ " : " ++ (1 + holdTimeSoloBeats);
		^outString;
	}

	displayMultiSelectionEnd {
		var holdTime = this.getSynthArgSpec("multiSelectionEnd");
		var holdBeatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var holdTimeBars = (holdTime / holdBeatsPerBar).floor;
		var holdTimeSoloBeats = (holdTime - (holdTimeBars * holdBeatsPerBar)).round(0.01);
		var outString = " " ++ (1 + holdTimeBars) ++ " : " ++ (1 + holdTimeSoloBeats);
		^outString;
	}

	displayLoopStart {
		var holdTime = this.getSynthArgSpec("loopStart");
		var holdBeatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var holdTimeBars = (holdTime / holdBeatsPerBar).floor;
		var holdTimeSoloBeats = (holdTime - (holdTimeBars * holdBeatsPerBar)).round(0.01);
		var outString = " " ++ (1 + holdTimeBars) ++ " : " ++ (1 + holdTimeSoloBeats);
		^outString;
	}

	displayLoopEnd {
		var holdTime = this.getSynthArgSpec("loopEnd");
		var holdBeatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var holdTimeBars = (holdTime / holdBeatsPerBar).floor;
		var holdTimeSoloBeats = (holdTime - (holdTimeBars * holdBeatsPerBar)).round(0.01);
		var outString = " " ++ (1 + holdTimeBars) ++ " : " ++ (1 + holdTimeSoloBeats);
		^outString;
	}

	//////////

	setDefaultActionVariables {
		var arrProcessFuncs = toolFuncs.getMultiTrackProcesses;
		var holdProcess = arrProcessFuncs[this.getSynthArgSpec("actionIndex")];
		if (holdProcess[2][0].notNil, {
			if (holdProcess[3][0].class == ControlSpec, {
				this.setSynthArgSpec("actionVar1", holdProcess[3][0].default);
			});
		});
		if (holdProcess[2][1].notNil, {
			if (holdProcess[3][1].class == ControlSpec, {
				this.setSynthArgSpec("actionVar2", holdProcess[3][1].default);
			});
		});

	}

	////////////////////////////////////

	addMarkerAtPosition { arg argPos, argText = "";
		var holdBeatsPerBar, holdTimeBars, holdTimeSoloBeats;
		dataBank.arrMarkers[argPos] = argText;
		this.rebuildArrMarkerTimes;
	}

	addMarkerAtCurrentPosition { arg argText = "";
		this.addMarkerAtPosition(currentPlayTime, argText);
	}

	deleteMarkerAtPosition {arg argPos;
		dataBank.arrMarkers[argPos] = nil;
		this.rebuildArrMarkerTimes;
	}

	deleteMarkerAtCurrentPosition {
		this.deleteMarkerAtPosition(currentPlayTime);
	}

	rebuildArrMarkerTimes {
		dataBank.arrMarkerTimes = dataBank.arrMarkers.keys.asArray.sort;
	}

	jumpToPreviousMarker {
		var holdTime, newTime, holdIndex;
		if (dataBank.arrMarkerTimes.asArray.size > 0, {
			holdTime = dataBank.arrMarkerTimes.nearestTo(currentPlayTime);
			if (holdTime < currentPlayTime, {
				newTime = holdTime;
			}, {
				holdIndex = dataBank.arrMarkerTimes.indexOf(holdTime);
				if (holdIndex > 0, {
					newTime = dataBank.arrMarkerTimes[holdIndex - 1];
				});
			});
			if (newTime.notNil, {
				case
				{runningStatus == 'STOPPED'} {
					this.updateCurrentTime(newTime);
				}
				{runningStatus == 'PLAYING'} {
					this.restartSequencerFromTime(newTime);
				}
				{runningStatus == 'RECORDING'} {
					this.restartSequencerFromTime(newTime);
				}
				;
				this.scrollToCurrentPlayTime;
			});
		});
	}

	jumpToNextMarker {
		var holdTime, newTime, holdIndex;
		if (dataBank.arrMarkerTimes.asArray.size > 0, {
			holdTime = dataBank.arrMarkerTimes.nearestTo(currentPlayTime);
			if (holdTime > currentPlayTime, {
				newTime = holdTime;
			}, {
				holdIndex = dataBank.arrMarkerTimes.indexOf(holdTime);
				if (holdIndex < (dataBank.arrMarkerTimes.size - 1), {
					newTime = dataBank.arrMarkerTimes[holdIndex + 1];
				});
			});
			if (newTime.notNil, {
				case
				{runningStatus == 'STOPPED'} {
					this.updateCurrentTime(newTime);
				}
				{runningStatus == 'PLAYING'} {
					this.restartSequencerFromTime(newTime);
				}
				{runningStatus == 'RECORDING'} {
					this.restartSequencerFromTime(newTime);
				}
				;
				this.scrollToCurrentPlayTime;
			});
		});
	}


	////////////////////////////////////

	/* track parameters ==================================================
	classData.defaultTrack = ();
	classData.defaultTrack.trackType = 'Note';  // 'Note'/'Controller'    TBC:/'Action'
	classData.defaultTrack.trackID = nil;  // automatically allocated on creation
	classData.defaultTrack.trackNo = nil;  // automatically allocated on creation
	classData.defaultTrack.name = "";
	classData.defaultTrack.mute = 0;
	classData.defaultTrack.offset = 0;
	classData.defaultTrack.monitorInput = 0; // for Note & Controller tracks
	classData.defaultTrack.recordArmed = 0; // for Note & Controller tracks
	classData.defaultTrack.midiPort = 0; // for Note & Controller tracks
	classData.defaultTrack.midiChannelMin  = 0; // for Note & Controller tracks
	classData.defaultTrack.midiChannelMax  = 15; // for Note & Controller tracks
	classData.defaultTrack.midiControllerNo = 0; // for Controller tracks
	classData.defaultTrack.midiKeybScroll = 0.5; // for Note tracks
	classData.defaultTrack.velocityScale = 1; // for Note tracks
	classData.defaultTrack.transpose = 0; // for Note tracks
	classData.defaultTrack.output1Active = 0; // for Note & Controller tracks
	classData.defaultTrack.output1ModuleID = 0; // for Note & Controller tracks
	classData.defaultTrack.output1ParameterName = ""; // for Controller tracks
	classData.defaultTrack.output2Active = 0; // for Note & Controller tracks
	classData.defaultTrack.output2ModuleID = 0; // for Note & Controller tracks
	classData.defaultTrack.output2ParameterName = ""; // for Controller tracks
	classData.defaultTrack.output3Active = 0; // for Note & Controller tracks
	classData.defaultTrack.output3ModuleID = 0; // for Note & Controller tracks
	classData.defaultTrack.output3ParameterName = ""; // for Controller tracks
	classData.defaultTrack.output4Active = 0; // for Note & Controller tracks
	classData.defaultTrack.output4ModuleID = 0; // for Note & Controller tracks
	classData.defaultTrack.output4ParameterName = ""; // for Controller tracks
	classData.defaultTrack.output5Active = 0; // for Note & Controller tracks
	classData.defaultTrack.output5ModuleID = 0; // for Note & Controller tracks
	classData.defaultTrack.output5ParameterName = ""; // for Controller tracks
	classData.defaultTrack.output6Active = 0; // for Note & Controller tracks
	classData.defaultTrack.output6ModuleID = 0; // for Note & Controller tracks
	classData.defaultTrack.output6ParameterName = ""; // for Controller tracks
	*/

	////////////////////////////////////

	showTrack {arg trackIndex;
		this.setSynthArgSpec("trackDisplayIndex", trackIndex+1);
		this.buildGuiSpecArray;
		system.showView(this);
	}

	deleteTrack {arg trackIndex;
		this.stopMidiLearn;
		this.buildTrackMonitorFuncs(arrTracks[trackIndex], false);
		this.buildTrackRecordFuncs(arrTracks[trackIndex], false);
		arrTracks.removeAt(trackIndex);
		this.renumberTracks;
		this.setSynthArgSpec("trackDisplayIndex", 0);
		this.buildGuiSpecArray;
		system.showView(this);
	}

	renumberTracks {
		arrTracks.do({arg track, i;
			track.trackNo = i + 1;
		});
	}

	changeTrackIndex { arg track, newIndex;
		var oldIndex = track.trackNo - 1;
		arrTracks.removeAt(oldIndex);
		arrTracks.insert(newIndex, track);
		this.renumberTracks;
	}

	setTrackInputType {arg argTrack, argType;
		if (argTrack.notNil, {
			// set
			argTrack.inputType = argType;
			// restore funcs
			this.restoreTrackMonitorRecordFuncs(argTrack);
		});
	}

	setTrackOutputActive {arg argTrack, argOutputActiveKey, argVal;
		if (argTrack.notNil, {
			// set
			argTrack[argOutputActiveKey] = argVal;
			// restore funcs
			this.restoreTrackMonitorRecordFuncs(argTrack);
		});
	}

	buildTrackMonitorFuncs { arg track, isActive;
		// remove any old funcs
		if (track.trackType == 'Controller', {
			if (midiMonitorControllerFuncs[track.trackID].notNil, {
				midiMonitorControllerFuncs[track.trackID].free;
				midiMonitorControllerFuncs[track.trackID] = nil;
			});
			if (modulationInputMonitorFuncs[track.trackID].notNil, {
				modulationInputMonitorFuncs[track.trackID] = nil;
			});
			if (directInputMonitorFuncs[track.trackID].notNil, {
				directInputMonitorFuncs[track.trackID] = nil;
			});
		});
		if (track.trackType == 'Note', {
			if (midiMonitorNoteOnFuncs[track.trackID].notNil, {
				midiMonitorNoteOnFuncs[track.trackID].free;
				midiMonitorNoteOnFuncs[track.trackID] = nil;
			});
			if (midiMonitorNoteOffFuncs[track.trackID].notNil, {
				midiMonitorNoteOffFuncs[track.trackID].free;
				midiMonitorNoteOffFuncs[track.trackID] = nil;
			});
		});
		// add relevent functions
		if (isActive == true, {
			if (track.trackType == 'Controller', {
				// check inputType
				if (track.inputType.isNil, {track.inputType = 'Midi'});
				if (track.inputType == 'Midi', {
					// check if bend
					if (track.useBendData == 1, {
						midiMonitorControllerFuncs[track.trackID] = MIDIFunc.bend({ |val, chan|
							// check channel range
							if (chan >= (min(track.midiChannelMin, track.midiChannelMax))
								&& (chan <= (max(track.midiChannelMin, track.midiChannelMax))), {
									6.do({arg count;
										var numString = (count + 1).asString;
										var moduleIDKey = ("output" ++ numString ++ "ModuleID").asSymbol;
										var outputActiveKey = ("output" ++ numString ++ "Active").asSymbol;
										var parameterNameKey = ("output" ++ numString ++ "ParameterName").asSymbol;
										if ((track[moduleIDKey] != 0) && (track[outputActiveKey] == 1), {
											toolFuncs.performAction (track[moduleIDKey], 0, track[parameterNameKey], val/16383,
												ignoreLatency:true, mapValues: true);
										});
									});
							});
						});
					},{
						midiMonitorControllerFuncs[track.trackID] = MIDIFunc.cc(
							{ |val, num, chan|
								// check channel range
								if (chan >= (min(track.midiChannelMin, track.midiChannelMax))
									&& (chan <= (max(track.midiChannelMin, track.midiChannelMax))), {
										6.do({arg count;
											var numString = (count + 1).asString;
											var moduleIDKey = ("output" ++ numString ++ "ModuleID").asSymbol;
											var outputActiveKey = ("output" ++ numString ++ "Active").asSymbol;
											var parameterNameKey = ("output" ++ numString ++ "ParameterName").asSymbol;
											if ((track[moduleIDKey] != 0) && (track[outputActiveKey] == 1), {
												toolFuncs.performAction (track[moduleIDKey], 0, track[parameterNameKey], val/127,
													ignoreLatency:true, mapValues: true);
											});
										});
								});
								// TESTING XXX - PRINT
								//"MONITOR - CONTROLLER: val % @ cc no %\n".postf(val, num);
							},
							track.midiControllerNo // only responds to midiControllerNo
						);
					});
				});
				if (track.inputType == 'Modulation Input', {
					modulationInputMonitorFuncs[track.trackID] = { arg argIndex, argVal;
						// check index
						if (track.modulationInputIndex == argIndex, {
							6.do({arg count;
								var numString = (count + 1).asString;
								var moduleIDKey = ("output" ++ numString ++ "ModuleID").asSymbol;
								var outputActiveKey = ("output" ++ numString ++ "Active").asSymbol;
								var parameterNameKey = ("output" ++ numString ++ "ParameterName").asSymbol;
								if ((track[moduleIDKey] != 0) && (track[outputActiveKey] == 1), {
									toolFuncs.performAction (track[moduleIDKey], 0, track[parameterNameKey], argVal,
										ignoreLatency:true, mapValues: true);
								});
							});
						});
					};
				});
				if (track.inputType == 'Direct Input', {
					directInputMonitorFuncs[track.trackID] = { arg argVal;
						6.do({arg count;
							var numString = (count + 1).asString;
							var moduleIDKey = ("output" ++ numString ++ "ModuleID").asSymbol;
							var outputActiveKey = ("output" ++ numString ++ "Active").asSymbol;
							var parameterNameKey = ("output" ++ numString ++ "ParameterName").asSymbol;
							if ((track[moduleIDKey] != 0) && (track[outputActiveKey] == 1), {
								toolFuncs.performAction (track[moduleIDKey], 0, track[parameterNameKey], argVal,
									ignoreLatency:true, mapValues: true);
							});
						});
					};
				});
			});  // end of if (track.trackType == 'Controller'
			//
			if (track.trackType == 'Note', {
				midiMonitorNoteOnFuncs[track.trackID] = MIDIFunc.noteOn({ |vel, num, chan|
					// check channel range
					if (chan >= (min(track.midiChannelMin, track.midiChannelMax))
						&& (chan <= (max(track.midiChannelMin, track.midiChannelMax))), {
							6.do({arg count;
								var numString = (count + 1).asString;
								var moduleIDKey = ("output" ++ numString ++ "ModuleID").asSymbol;
								var outputActiveKey = ("output" ++ numString ++ "Active").asSymbol;
								if ((track[moduleIDKey] != 0) && (track[outputActiveKey] == 1), {
									toolFuncs.performAction (track[moduleIDKey], 0,
										"Set note on: midi note + velocity ", num, vel, ignoreLatency:true);
								});
							});
					});
					// TESTING XXX - PRINT
					//"MONITOR - NOTE ON: note % @ velocity %\n".postf(num, vel);
				});
				midiMonitorNoteOffFuncs[track.trackID] = MIDIFunc.noteOff({ |vel, num, chan|
					// check channel range
					if (chan >= (min(track.midiChannelMin, track.midiChannelMax))
						&& (chan <= (max(track.midiChannelMin, track.midiChannelMax))), {
							6.do({arg count;
								var numString = (count + 1).asString;
								var moduleIDKey = ("output" ++ numString ++ "ModuleID").asSymbol;
								var outputActiveKey = ("output" ++ numString ++ "Active").asSymbol;
								if ((track[moduleIDKey] != 0) && (track[outputActiveKey] == 1), {
									toolFuncs.performAction (track[moduleIDKey], 0,
										"Set note off: midi note ", num, ignoreLatency:true);
								});
							});
					});
					// TESTING XXX - PRINT
					//"MONITOR - NOTE OFF: note % @ velocity %\n".postf(num, vel);
				});
			});
		});
	}

	buildTrackRecordFuncs { arg track, isActive;
		// remove any old funcs
		if (track.trackType == 'Controller', {
			if (midiRecordControllerFuncs[track.trackID].notNil, {
				midiRecordControllerFuncs[track.trackID].free;
				midiRecordControllerFuncs[track.trackID] = nil;
			});
			if (modulationInputRecordFuncs[track.trackID].notNil, {
				modulationInputRecordFuncs[track.trackID] = nil;
			});
			if (directInputRecordFuncs[track.trackID].notNil, {
				directInputRecordFuncs[track.trackID] = nil;
			});
		});
		if (track.trackType == 'Note', {
			if (midiRecordNoteOnFuncs[track.trackID].notNil, {
				midiRecordNoteOnFuncs[track.trackID].free;
				midiRecordNoteOnFuncs[track.trackID] = nil;
			});
			if (midiRecordNoteOffFuncs[track.trackID].notNil, {
				midiRecordNoteOffFuncs[track.trackID].free;
				midiRecordNoteOffFuncs[track.trackID] = nil;
			});
		});
		// add relevent functions
		if (isActive == true, {
			if (track.trackType == 'Controller', {
				// check inputType
				if (track.inputType.isNil, {track.inputType = 'Midi'});
				if (track.inputType == 'Midi', {
					// check if bend
					if (track.useBendData == 1, {
						midiRecordControllerFuncs[track.trackID] = MIDIFunc.bend({ |val, chan|
							if (runningStatus == 'RECORDING', {
								// check channel range
								if (chan >= (min(track.midiChannelMin, track.midiChannelMax))
									&& (chan <= (max(track.midiChannelMin, track.midiChannelMax))), {
										// store val
										recordedControllerVals[track.trackID] = recordedControllerVals[track.trackID].asArray
										.add([(this.seqClock.beats - (system.seqLatency * 2)), val/16383]);
								});
								// TESTING XXX - PRINT
								//"RECORD - CONTROLLER: val % @ cc no %\n".postf(val, num);
							});
						},
						track.midiControllerNo
						);
					},{
						midiRecordControllerFuncs[track.trackID] = MIDIFunc.cc(
							{arg val, num, chan;
								if (runningStatus == 'RECORDING', {
									// check channel range
									if (chan >= (min(track.midiChannelMin, track.midiChannelMax))
										&& (chan <= (max(track.midiChannelMin, track.midiChannelMax))), {
											// store val
											recordedControllerVals[track.trackID] = recordedControllerVals[track.trackID].asArray
											.add([(this.seqClock.beats - (system.seqLatency * 2)), val/127]);
									});
									// TESTING XXX - PRINT
									//"RECORD - CONTROLLER: val % @ cc no %\n".postf(val, num);
								});
							},
							track.midiControllerNo
						);
					});
				});
				if (track.inputType == 'Modulation Input', {
					modulationInputRecordFuncs[track.trackID] = { arg argIndex, argVal;
						// check index
						if (track.modulationInputIndex == argIndex, {
							if (runningStatus == 'RECORDING', {
								// store val
								recordedControllerVals[track.trackID] = recordedControllerVals[track.trackID].asArray
								.add([(this.seqClock.beats - (system.seqLatency * 2)), argVal]);
							});
						});
					};
				});
				if (track.inputType == 'Direct Input', {
					directInputRecordFuncs[track.trackID] = { arg argVal;
						if (runningStatus == 'RECORDING', {
							// store val
							recordedControllerVals[track.trackID] = recordedControllerVals[track.trackID].asArray
							.add([(this.seqClock.beats - (system.seqLatency * 2)), argVal]);
						});
					};
				});
			});
			if (track.trackType == 'Note', {
				midiRecordNoteOnFuncs[track.trackID] = MIDIFunc.noteOn(
					{arg vel, num, chan;
						if (runningStatus == 'RECORDING', {
							// check channel range
							if (chan >= (min(track.midiChannelMin, track.midiChannelMax))
								&& (chan <= (max(track.midiChannelMin, track.midiChannelMax))), {
									// add group event for track
									if (recordedNoteOns[track.trackID].isNil, {
										recordedNoteOns[track.trackID] = ();
									});
									// add noteOn event
									recordedNoteOns[track.trackID][num] = [(this.seqClock.beats - (system.seqLatency * 2)), num, vel];
							});
							// TESTING XXX - PRINT
							//"RECORD - NOTE ON: note % @ velocity %\n".postf(num, vel);
						});
				});
				midiRecordNoteOffFuncs[track.trackID] = MIDIFunc.noteOff(
					{arg vel, num, chan;
						var holdNoteOn, holdNoteLength, holdNoteTimeBeats, holdVel;
						if (runningStatus == 'RECORDING', {
							// check channel range
							if (chan >= (min(track.midiChannelMin, track.midiChannelMax))
								&& (chan <= (max(track.midiChannelMin, track.midiChannelMax))), {
									// get relevant noteOn
									if (recordedNoteOns[track.trackID].notNil, {
										holdNoteOn = recordedNoteOns[track.trackID][num];
										if (holdNoteOn.notNil, {
											holdNoteTimeBeats = holdNoteOn[0];
											holdNoteLength = max((this.seqClock.beats - (system.seqLatency * 2)) - holdNoteOn[0], 0.01);
											holdVel = holdNoteOn[2];
											// add noteOnOff - format [StartTime, NoteNum, Vel, NoteLength, StopTime]
											recordedNoteOnOffs[track.trackID] = recordedNoteOnOffs[track.trackID].asArray
											.add([holdNoteTimeBeats, num, holdVel, holdNoteLength, holdNoteTimeBeats + holdNoteLength]);
											// remove noteOn
											recordedNoteOns[track.trackID][num] = nil;
										});
									});
							});
							// TESTING XXX - PRINT
							//"RECORD - NOTE OFF: note % @ velocity %\n".postf(num, vel);
						});
				});
			});
		});
	}

	restoreTrackMonitorRecordFuncs {arg track;
		this.buildTrackMonitorFuncs(track, track.monitorInput.asBoolean);
		this.buildTrackRecordFuncs(track, track.recordArmed.asBoolean);
	}

	restoreAllTrackMonitorFuncs {
		arrTracks.do({arg track, i;
			this.buildTrackMonitorFuncs(track, track.monitorInput.asBoolean);
		});
	}

	////////////////////////////////////

	scrollToCurrentPlayTime {
		var hold_beatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var hold_displayRangeStart = (this.getSynthArgSpec("displayRangeStart")-1) * hold_beatsPerBar;
		var hold_displayRangeEnd = (this.getSynthArgSpec("displayRangeEnd")-1) * hold_beatsPerBar;
		var offsetBeats, offsetBars, endMax;
		if ((this.currentPlayTime < hold_displayRangeStart) || (this.currentPlayTime >= hold_displayRangeEnd), {
			offsetBeats = (this.currentPlayTime.round - 1) - hold_displayRangeStart;
			offsetBars = (offsetBeats / hold_beatsPerBar).round;
			this.setSynthArgSpec("displayRangeStart", this.getSynthArgSpec("displayRangeStart") + offsetBars);
			this.setSynthArgSpec("displayRangeEnd", this.getSynthArgSpec("displayRangeEnd") + offsetBars);
			endMax = (this.getSynthArgSpec("displayRangeEnd") + 1).ceil;
			endMax = max(endMax, (this.currentPlayTime.ceil/ hold_beatsPerBar) + 16);
			this.setSynthArgSpec("maxDisplayBars", max(this.getSynthArgSpec("maxDisplayBars"), endMax));
			this.updateScaledUserViews;
			if (dataBank.holdMultiTrackView.notNil, {
				dataBank.holdMultiTrackView.refreshBarTextView;
				dataBank.holdMultiTrackView.updateDisplayRange;
			});
			if (dataBank.holdTrackView.notNil, {
				dataBank.holdTrackView.refreshBarTextView;
				dataBank.holdTrackView.updateDisplayRange;
			});
		});
	}

	updateScaledUserViews {
		var holdScaleX, holdMoveX;
		var hold_beatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var hold_displayRangeStart = (this.getSynthArgSpec("displayRangeStart")-1) * hold_beatsPerBar;
		var hold_displayRangeEnd = (this.getSynthArgSpec("displayRangeEnd")-1) * hold_beatsPerBar;
		// defer gui update
		{
			scaledUserViews.do({arg item, i;
				// can be single or array
				item.asArray.do({arg view, i;
					var holdRect;
					if (view.notNil and: {view.view.notClosed}, {
						holdRect = view.viewRect;
						holdRect.width = hold_displayRangeEnd - hold_displayRangeStart;
						holdRect.left = hold_displayRangeStart;
						view.viewRect_(holdRect);
					});
				});
			});
		}.defer;
	}

	refreshScaledUserViews {
		// defer gui update
		{
			scaledUserViews.do({arg item, i;
				// can be single or array
				item.asArray.do({arg view, i;
					view.refresh;
				});
			});
		}.defer;
	}

	adjustLoopEnd {
		var holdBeatsPerBar = this.getSynthArgSpec("beatsPerBar");
		var holdStart = this.getSynthArgSpec("loopStart");
		var holdEnd = this.getSynthArgSpec("loopEnd");
		if (holdEnd < (holdStart+2), {
			this.setSynthArgSpec("loopEnd", holdStart + 2);
		});
		this.calculateEndTime;
		this.updateEndTime;
	}

	////////////////////////////////////

	rebuildMidiOutPorts {
		arrTracks.do({arg track, i;
			// if names given, find correct port from names
			if (track.midiDeviceName.notNil and: track.midiPortName.notNil, {
				// default to 0 in case device/port names not found
				track.midiPortInd = 0;
				MIDIClient.destinations.do({arg item, i;
					if ( (track.midiDeviceName == item.device) and: (track.midiPortName == item.name), {
						track.midiPortInd = i + 1;
					});
				});
			});
			this.initMidiPort(track);
		});
	}

	initMidiPort { arg track;
		track.midiPortInd = track.midiPortInd ? 0;
		if (track.midiPortInd == 0, {
			arrMidiOutPorts[track] = nil;
			track.midiDeviceName = nil;
			track.midiPortName = nil;
		},{
			arrMidiOutPorts[track] = MIDIOut(track.midiPortInd - 1, MIDIClient.destinations[track.midiPortInd - 1].uid);
			track.midiDeviceName =  MIDIClient.destinations[track.midiPortInd - 1].device;
			track.midiPortName =  MIDIClient.destinations[track.midiPortInd - 1].name;
			// minimise MIDI out latency
			arrMidiOutPorts[track].latency = 0;
		});
	}

	sendMIDIMessage { arg track, messageType, outNote, outVel, gateTime, outControlVal;
		if (track.outputMidiLoopback == 1, {
			// Note
			if (messageType == 'Note', {
				MIDIIn.doNoteOnAction(0, track.outputMidiChannel.asInteger, outNote.asInteger, outVel);
				SystemClock.sched(gateTime,{
					MIDIIn.doNoteOffAction(0, track.outputMidiChannel.asInteger, outNote.asInteger, 0);
					nil;
				});
			});
			// Controller
			if (messageType == 'Controller', {
				MIDIIn.doControlAction(0, track.outputMidiChannel.asInteger, track.outputMidiControllerNo.asInteger, outControlVal * 128);
			});
			// Pitchbend
			if (messageType == 'Pitchbend', {
				MIDIIn.doBendAction(0, track.outputMidiChannel.asInteger, outControlVal * 16383);
			});
		});
		if (track.midiPortInd > 0, {
			// Note
			if (messageType == 'Note', {
				arrMidiOutPorts[track].noteOn(track.outputMidiChannel, outNote, outVel);
				SystemClock.sched(gateTime,{
					if (arrMidiOutPorts[track].notNil, {
						arrMidiOutPorts[track].noteOff(track.outputMidiChannel, outNote, 0);
					});
					nil;
				});
			});
			// Controller
			if (messageType == 'Controller', {
				arrMidiOutPorts[track].control(track.outputMidiChannel.asInteger, track.outputMidiControllerNo.asInteger,
					outControlVal * 128);
			});
			// Pitchbend
			if (messageType == 'Pitchbend', {
				arrMidiOutPorts[track].bend(track.outputMidiChannel.asInteger, outControlVal * 16383);
			});
		});
	}

}
