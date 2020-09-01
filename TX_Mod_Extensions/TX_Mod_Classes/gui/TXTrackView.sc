// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// ========================================================================
/* 	NOTE - the following code generates the list below:
(
"// ==== Instance Methods ====================== ".postln;
a = TXTrackView.methods;
b = a.collect ({ arg item, i; item.name.asString;});
b.sort.do ({ arg item, i; ("// " ++ item).postln;});
"// ================================================".postln;
)
*/
// ==== Instance Methods ======================
// createViewVelocityEvents
// drawControllerGui
// drawInputsOutputs
// drawNoteGui
// drawSelectionBar
// getBarsBeatsString
// init
// refreshBarTextView
// scrollView
// scrollView_
// setOffsetViewBackCol
// updateCurrentMarkerLabel
// updateCurrentPlayTime
// updateCurrentPlayTimeText
// updateCurrentPlayTimeViews
// updateDisplayRange
// updateLoopText
// updateMouseTextTime
// updateMouseTextTimeControl
// updateMouseTextTimeNote
// updateMouseTextTimeVel
// updateSelectedTotalText
// updateSelectionText
// updateSingleEventParameters
// updateStartPos
// updateStartPosText
// updateStopPos
// updateStopPosText
// ================================================

TXTrackView {  // used by TXMultiTrackSeq

	// NOTE EVENT FORMAT [startTime, noteNum, vel, noteLength, endTime]
	// CONTROLLER EVENT FORMAT [startTime, val]

	var <>scrollView, system;
	var holdBackgroundBox, holdTrack, holdModule, holdParent;
	var scrollBox, midiKeyboard,  midiKeybScrollSlider, totalTextView, newNoteEvent, mouseTextView;
	var numboxStartPosBeats, numboxStartPosBars, numboxStopPosBeats, numboxStopPosBars;
	var barTextView, popupNoteTrigOutModule, popupNoteTrigOutParameter;
	var numboxSelectionStartBars, numboxSelectionStartBeats, numboxSelectionEndBars, numboxSelectionEndBeats;
	var numboxLoopStartBars, numboxLoopStartBeats, numboxLoopEndBars, numboxLoopEndBeats;
	var viewSelectionBar, viewTrackEvents, viewEditEvents, viewVelocityEvents, controllerValRangeView;
	var singleNoteMidiNoteView, singleNoteOnBarsView, singleNoteOnBeatsView;
	var singleNoteOffBarsView, singleNoteOffBeatsView, singleNoteVelView;
	var singleEventContainer, singleEventTimeBarsView, singleEventTimeBeatsView, singleEventValView, singleEventOut1ValView;
	var holdView, directInputSlider, displayRangeSlider, holdValue, holdActionText, holdStepTime, scrollBox;
	var popupTrack, labelTrackID, btnEdit, btnDelete, btnShowProcesses;
	var textboxName, labelboxType;
	var processSelectView, arrProcessStrings, holdProcess, popupLineShapes, popupInputType;
	var chkboxMute, chkboxMon, chkboxRec, numboxOffset;
	var numboxMidiPort, rangeMidiChannels, numboxMidiControllerNo, chkboxMidiLearn, chkboxUseBendData;
	var numboxVelocityScale, numboxTranspose;
	var arrGroupModules, arrValueModules;
	var holdArrActionSpecs, holdArrActionSpecNames, tempModule, holdMaxDisplayBeats, holdOut1Specs, holdOut1ControlSpec;
	var newNoteData, holdPortNames, holdPopupStrings, holdAction;
	var arrSnapBeatStrings, arrSnapBeatVals, dragMode, dragIndex;
	var holdDecLeft, holdDecTop, holdDragSelectionStart, holdNoteViewHeight, holdCtrlViewHeight, holdSelectedEventIndex;
	var selectionBoxStartX, selectionBoxStartY, selectionBoxEndX, selectionBoxEndY, tabBox, tabBoxWidth;
	var lineStartX, lineStartY, lineEndX, lineEndY;
	var oldScaledX, oldScaledY, holdDragEnabled, scrollViewAction, scaledUserViewWidth;
	var numboxCurrentPlayTimeBars, numboxCurrentPlayTimeBeats, nearestX, oldDragMarkerPos;
	var btnAddMarker, btnDeleteMarker, btnPrevMarker, btnNextMarker, textboxMarkerLabel;

	*new {arg argSystem, argParent, dimensions, argTrack, argModule, argScrollViewAction;
		^super.new.init(argSystem, argParent, dimensions, argTrack, argModule, argScrollViewAction);
	}

	init {arg argSystem, argParent, dimensions, argTrack, argModule, argScrollViewAction;
		lineStartX = -1;
		lineStartY = 0;
		lineEndX = -1;
		lineEndY = 0;
		oldScaledX = 0;
		oldScaledY = 0;
		holdDragEnabled = false;
		system = argSystem;
		holdParent = argParent;
		holdModule = argModule;
		holdTrack = argTrack;
		scrollViewAction = argScrollViewAction;
		scaledUserViewWidth = 1050;
		holdOut1Specs = holdModule.toolFuncs.getOut1SpecsForTrack(holdTrack);
		holdOut1ControlSpec = holdOut1Specs[0];

		if ((holdTrack.trackType == 'Note') || (holdTrack.trackType == 'Controller'), {
			holdParent.decorator.shift(0, 2);

			// Play From Start
			holdView = TXCheckBox(holdParent, 110 @ 20, "Play From Start", TXColor.sysGuiCol1, TXColor.grey(0.9),
				TXColor.white, TXColor.sysGuiCol1)
			.value_(holdModule.getSynthArgSpec("playFromStart"))
			.action_({|view|
				holdModule.setSynthArgSpec("playFromStart", view.value);
			});

			// Start position
			numboxStartPosBars = TXNumber(holdParent, 70 @ 20, "Start", ControlSpec(1, 10000),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
					+ (numboxStartPosBeats.value - 1);
					holdModule.setSynthArgSpec("startPosBeats", totalBeats);
					system.showView;
				},
				// initial value
				1,
				false, 26, 40
			);
			numboxStartPosBars.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			numboxStartPosBars.numberView.normalColor_(TXColor.sysGuiCol1).background_(TXColor.white);

			numboxStartPosBeats = TXNumber(holdParent, 50 @ 20, ": ",
				ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (numboxStartPosBars.value - 1)) + (view.value - 1);
					holdModule.setSynthArgSpec("startPosBeats", totalBeats);
					system.showView;
				},
				// initial value
				1 ,
				false, 10, 34
			);
			numboxStartPosBeats.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			numboxStartPosBeats.numberView.normalColor_(TXColor.sysGuiCol1).background_(TXColor.white);

			this.updateStartPosText; // set initial values

			// Stop position
			numboxStopPosBars = TXNumber(holdParent, 70 @ 20, "Stop", ControlSpec(1, 10000),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
					+ (numboxStopPosBeats.value - 1);
					totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
					holdModule.setSynthArgSpec("stopPosBeats", totalBeats);
					system.showView;
				},
				// initial value
				1,
				false, 26, 40
			);
			numboxStopPosBars.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			numboxStopPosBars.numberView.normalColor_(TXColor.sysGuiCol1).background_(TXColor.white);

			numboxStopPosBeats = TXNumber(holdParent, 50 @ 20, ": ",
				ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (numboxStopPosBars.value - 1)) + (view.value - 1);
					totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
					holdModule.setSynthArgSpec("stopPosBeats", totalBeats);
					system.showView;
				},
				// initial value
				1 ,
				false, 10, 34
			);
			numboxStopPosBeats.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			numboxStopPosBeats.numberView.normalColor_(TXColor.sysGuiCol1).background_(TXColor.white);

			this.updateStopPosText; // set initial values

			holdParent.decorator.shift(18, 0);

			// Selection
			numboxSelectionStartBars = TXNumber(holdParent, 120 @ 20, "Selection Start", ControlSpec(1, 10000, step: 1),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
					+ (numboxSelectionStartBeats.value - 1);
					//holdTrack.selectionStart = totalBeats;
					holdModule.setTrackSelectionStart(holdTrack.trackID, 0, totalBeats);
					system.showView;
				},
				// initial value
				1,
				false, 80, 34
			);
			numboxSelectionStartBars.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);
			numboxSelectionStartBars.numberView.normalColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

			numboxSelectionStartBeats = TXNumber(holdParent, 88 @ 20, ": ",
				ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (numboxSelectionStartBars.value - 1)) + (view.value - 1);
					//holdTrack.selectionStart = totalBeats;
					holdModule.setTrackSelectionStart(holdTrack.trackID, 0, totalBeats);
					system.showView;
				},
				// initial value
				1 ,
				false, 10, 34
			);
			numboxSelectionStartBeats.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);
			numboxSelectionStartBeats.numberView.normalColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

			numboxSelectionEndBars = TXNumber(holdParent, 46 @ 20, "End", ControlSpec(1, 10000, step: 1),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
					+ (numboxSelectionEndBeats.value - 1);
					//holdTrack.selectionEnd = totalBeats;
					holdModule.setTrackSelectionEnd(holdTrack.trackID, 0, totalBeats);
					system.showView;
				},
				// initial value
				1,
				false, 24, 34
			);
			numboxSelectionEndBars.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);
			numboxSelectionEndBars.numberView.normalColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

			numboxSelectionEndBeats = TXNumber(holdParent, 8 @ 20, ": ",
				ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (numboxSelectionEndBars.value - 1)) + (view.value - 1);
					//holdTrack.selectionEnd = totalBeats;
					holdModule.setTrackSelectionEnd(holdTrack.trackID, 0, totalBeats);
					system.showView;
				},
				// initial value
				1 ,
				false, 10, 34
			);
			numboxSelectionEndBeats.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);
			numboxSelectionEndBeats.numberView.normalColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

			this.updateSelectionText; // set initial values

			holdParent.decorator.shift(18, 0);

			// Loop
			holdView = TXCheckBox(holdParent, 52 @ 20, "Loop", TXColor.green, TXColor.paleGreen2,
				TXColor.paleGreen2, TXColor.sysGuiCol2)
			.value_(holdModule.getSynthArgSpec("loop"))
			.action_({|view|
				holdModule.setSynthArgSpec("loop", view.value);
				system.showView(this);
			});

			numboxLoopStartBars = TXNumber(holdParent, 80 @ 20, "Start", ControlSpec(1, 10000, step: 1),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
					+ (numboxLoopStartBeats.value - 1);
					holdModule.setSynthArgSpec("loopStart", totalBeats);
					holdModule.adjustLoopEnd;
					system.showView;
				},
				// initial value
				1,
				false, 34, 34
			);
			numboxLoopStartBars.labelView.stringColor_(TXColor.green).background_(TXColor.paleGreen2);
			numboxLoopStartBars.numberView.normalColor_(TXColor.green).background_(TXColor.paleGreen2);

			numboxLoopStartBeats = TXNumber(holdParent, 88 @ 20, ": ",
				ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (numboxLoopStartBars.value - 1)) + (view.value - 1);
					holdModule.setSynthArgSpec("loopStart", totalBeats);
					holdModule.adjustLoopEnd;
					system.showView;
				},
				// initial value
				1 ,
				false, 10, 34
			);
			numboxLoopStartBeats.labelView.stringColor_(TXColor.green).background_(TXColor.paleGreen2);
			numboxLoopStartBeats.numberView.normalColor_(TXColor.green).background_(TXColor.paleGreen2);

			numboxLoopEndBars = TXNumber(holdParent, 46 @ 20, "End", ControlSpec(1, 10000, step: 1),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
					+ (numboxLoopEndBeats.value - 1);
					holdModule.setSynthArgSpec("loopEnd", totalBeats);
					holdModule.adjustLoopEnd;
					system.showView;
				},
				// initial value
				1,
				false, 24, 34
			);
			numboxLoopEndBars.labelView.stringColor_(TXColor.green).background_(TXColor.paleGreen2);
			numboxLoopEndBars.numberView.normalColor_(TXColor.green).background_(TXColor.paleGreen2);

			numboxLoopEndBeats = TXNumber(holdParent, 8 @ 20, ": ",
				ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (numboxLoopEndBars.value - 1)) + (view.value - 1);
					holdModule.setSynthArgSpec("loopEnd", totalBeats);
					holdModule.adjustLoopEnd;
					system.showView;
				},
				// initial value
				1 ,
				false, 10, 34
			);
			numboxLoopEndBeats.labelView.stringColor_(TXColor.green).background_(TXColor.paleGreen2);
			numboxLoopEndBeats.numberView.normalColor_(TXColor.green).background_(TXColor.paleGreen2);

			this.updateLoopText; // set initial values

			// white line
			//StaticText(holdParent, 1070 @ 1).background_(TXColor.white);
			holdParent.decorator.nextLine.shift(0, 4);

			if (holdTrack.trackType == 'Note', {
				tabBoxWidth = 276;
			});
			if (holdTrack.trackType == 'Controller', {
				tabBoxWidth = 370;
			});
			tabBox = UserView(holdParent, tabBoxWidth @ 30).background_(TXColor.grey(0.9));
			// restore decorator position
			holdParent.decorator.shift(tabBoxWidth.neg, 4);

			// Inputs/Outputs btn
			holdView  = Button(holdParent, 100 @ 20)
			.action_({|view|
				holdTrack.displayMode = 'inputsOutputs';
				system.showView;
			});
			if (holdTrack.displayMode == 'inputsOutputs', {
				holdView.states_([ ["Inputs & Outputs", TXColor.paleYellow2, TXColor.sysGuiCol2] ]);
			},{
				holdView.states_([ ["Inputs & Outputs", TXColor.white, TXColor.sysGuiCol2] ]);
			});

			if (holdTrack.trackType == 'Note', {
				holdView  = Button(holdParent, 80 @ 20)
				.action_({|view|
					holdTrack.displayMode = 'drawNotes';
					holdModule.toolFuncs.unselectAllNotes(holdTrack);
					system.showView;
				});
				if (holdTrack.displayMode == 'drawNotes', {
					holdView.states_([ ["Draw Notes", TXColor.paleYellow2, TXColor.sysGuiCol2] ]);
				},{
					holdView.states_([ ["Draw Notes", TXColor.white, TXColor.sysGuiCol2] ]);
				});

				// Edit Notes btn
				holdView  = Button(holdParent, 80 @ 20)
				.action_({|view|
					holdTrack.displayMode = 'editNotes';
					system.showView;
				});
				if (holdTrack.displayMode == 'editNotes', {
					holdView.states_([ ["Edit Notes", TXColor.paleYellow2, TXColor.sysGuiCol2] ]);
				},{
					holdView.states_([ ["Edit Notes", TXColor.white, TXColor.sysGuiCol2] ]);
				});

				holdParent.decorator.shift(32, 0);
			});

			if (holdTrack.trackType == 'Controller', {
				// Draw Points btn
				holdView  = Button(holdParent, 80 @ 20)
				.action_({|view|
					holdTrack.displayMode = 'drawPoints';
					system.showView;
				});
				if (holdTrack.displayMode == 'drawPoints', {
					holdView.states_([ ["Draw Points", TXColor.paleYellow2, TXColor.sysGuiCol2] ]);
				},{
					holdView.states_([ ["Draw Points", TXColor.white, TXColor.sysGuiCol2] ]);
				});

				// Draw Line btn
				holdView  = Button(holdParent, 80 @ 20)
				.action_({|view|
					holdTrack.displayMode = 'drawLine';
					system.showView;
				});
				if (holdTrack.displayMode == 'drawLine', {
					holdView.states_([ ["Draw Line", TXColor.paleYellow2, TXColor.sysGuiCol2] ]);
				},{
					holdView.states_([ ["Draw Line", TXColor.white, TXColor.sysGuiCol2] ]);
				});

				// Edit Selection btn
				holdView  = Button(holdParent, 90 @ 20)
				.action_({|view|
					holdTrack.displayMode = 'editSelection';
					system.showView;
				});
				if (holdTrack.displayMode == 'editSelection', {
					holdView.states_([ ["Edit Selection", TXColor.paleYellow2, TXColor.sysGuiCol2] ]);
				},{
					holdView.states_([ ["Edit Selection", TXColor.white, TXColor.sysGuiCol2] ]);
				});

				holdParent.decorator.shift(30, 0);
			});
		});
		// track no
		holdView = StaticText(holdParent, 55 @ 20)
		.background_(TXColor.white).stringColor_(TXColor.black)
		.align_(\centre)
		.string_("Track " ++ holdTrack.trackNo.asString);

		// name
		textboxName = TextField(holdParent, Rect(0, 0, 130, 20),
			" ", TXColor.black, TXColor.white,
			TXColor.black, TXColor.white, 4);
		textboxName.action = {arg view;
			holdTrack.name = view.string;
			holdModule.buildGuiSpecArray;
			system.showView;
		};
		textboxName.string = holdTrack.name;

		// track type
		labelboxType = StaticText(holdParent, Rect(0, 0, 56, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.grey7)
		.align_(\centre)
		.string_(holdTrack.trackType.asString);

		//  TrackID
		labelTrackID = StaticText(holdParent, 44 @ 20)
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.grey7)
		.align_(\centre)
		.string_("id:" ++ holdTrack.trackID.asString);

		// delete btn
		btnDelete  = Button(holdParent, 50 @ 20)
		.states_([
			["Del track", TXColor.white, TXColor.sysDeleteCol]
		])
		.action_({|view|
			var holdNewActionStep;
			holdModule.deleteTrack(argModule.arrTracks.indexOf(holdTrack));
		});

		holdParent.decorator.shift(20, 0);

		if ((holdTrack.trackType == 'Note') || (holdTrack.trackType == 'Controller'), {
			// monitor
			chkboxMon = TXCheckBox(holdParent, 70 @ 20, "Monitor", TXColor.sysGuiCol2, TXColor.grey(0.9),
				TXColor.white, TXColor.sysGuiCol1)
			.value_(holdTrack.monitorInput)
			.action_({|view|
				holdTrack.monitorInput = view.value;
				holdModule.buildTrackMonitorFuncs(holdTrack, view.value.asBoolean);
			});

			// record
			chkboxRec = TXCheckBox(holdParent, 70 @ 20, "Record", TXColor.sysRecordCol, TXColor.grey(0.9),
				TXColor.white, TXColor.sysRecordCol)
			.value_(holdTrack.recordArmed)
			.action_({|view|
				holdTrack.recordArmed = view.value;
				holdModule.buildTrackRecordFuncs(holdTrack, view.value.asBoolean);
			});
		}, {
			// spacer
			holdParent.decorator.shift(108);
		});

		// title
		labelboxType = StaticText(holdParent, Rect(0, 0, 34, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
		.align_(\right)
		.string_("Offset");

		// offset
		numboxOffset = NumberBox(holdParent, 50 @ 20)
		.maxDecimals_(4)
		.background_(TXColor.white)
		.value_(holdTrack.offset)
		.action_({|view|
			holdTrack.offset = view.value;
			this.setOffsetViewBackCol(view, holdTrack.offset);
		});
		this.setOffsetViewBackCol(numboxOffset, holdTrack.offset);

		// mute
		chkboxMute = TXCheckBox(holdParent, 50 @ 20, "Mute", TXColor.sysDeleteCol, TXColor.grey(0.9),
			TXColor.white, TXColor.sysDeleteCol)
		.value_(holdTrack.mute)
		.action_({|view|
			holdTrack.mute = view.value;
			system.showView;
		});

		// draw white line
		holdParent.decorator.shift(0, -8);
		StaticText(holdParent, 1070 @ 1).background_(TXColor.white);
		holdParent.decorator.nextLine.shift(0, 4);

		if (holdTrack.displayMode == 'inputsOutputs', {
			this.drawInputsOutputs;
		},{

			if (holdTrack.trackType == 'Controller', {
				// snapToGrid
				TXCheckBox(holdParent, 110 @ 20, "Snap To Grid", TXColor.sysGuiCol2, TXColor.grey(0.9),
					TXColor.white, TXColor.sysGuiCol1)
				.value_(holdTrack.snapToGrid)
				.action_({|view|
					holdTrack.snapToGrid = view.value;
				});
			});

			if (holdTrack.trackType == 'Note', {
				// snapNoteStartToGrid
				TXCheckBox(holdParent, 110 @ 20, "Snap Note Start", TXColor.sysGuiCol2, TXColor.grey(0.9),
					TXColor.white, TXColor.sysGuiCol1)
				.value_(holdTrack.snapNoteStartToGrid)
				.action_({|view|
					holdTrack.snapNoteStartToGrid = view.value;
				});
				// snapNoteEndToGrid
				TXCheckBox(holdParent, 110 @ 20, "Snap Note End", TXColor.sysGuiCol2, TXColor.grey(0.9),
					TXColor.white, TXColor.sysGuiCol1)
				.value_(holdTrack.snapNoteEndToGrid)
				.action_({|view|
					holdTrack.snapNoteEndToGrid = view.value;
				});
				// snapSelection
				TXCheckBox(holdParent, 110 @ 20, "Snap Selection", TXColor.sysGuiCol2, TXColor.grey(0.9),
					TXColor.white, TXColor.sysGuiCol1)
				.value_(holdTrack.snapSelection)
				.action_({|view|
					holdTrack.snapSelection = view.value;
				});
			});

			holdParent.decorator.shift(20, 0);

			// snapBeats
			arrSnapBeatVals = [1,1/2,1/3,1/4,1/5,1/6,1/7,1/8,1/9, 1/10, 1/12, 1/14, 1/16, 1/20, 1/24, 1/30, 1/32];
			arrSnapBeatStrings = ["1","1/2","1/3","1/4","1/5","1/6","1/7","1/8",
				"1/9","1/10","1/12","1/14","1/16","1/20","1/24","1/30","1/32"];
			holdView = TXPopupPlusMinus(holdParent, 190 @ 20, "Snap Beats", arrSnapBeatStrings, {arg view;
				holdTrack.snapBeats = arrSnapBeatVals[view.value];
				// update view
				system.showView;
			}, arrSnapBeatVals.indexOf(holdTrack.snapBeats), false, 70);
			holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			holdView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);

			if (holdTrack.trackType == 'Controller', {
				this.drawControllerGui;
			});

			if (holdTrack.trackType == 'Note', {
				this.drawNoteGui;
			});

			// save & register scaledUserViews
			holdModule.scaledUserViews = (); // empty first since only current track needed
			holdModule.scaledUserViews[holdTrack.trackID] = [viewTrackEvents];
			holdModule.registerCurrentTimeView(viewTrackEvents);
			if (viewEditEvents.notNil and: {viewEditEvents.view.notClosed}, {
				holdModule.scaledUserViews[holdTrack.trackID] = holdModule.scaledUserViews[holdTrack.trackID].add(viewEditEvents);
			});
			if (viewVelocityEvents.notNil and: {viewVelocityEvents.view.notClosed}, {
				holdModule.scaledUserViews[holdTrack.trackID] = holdModule.scaledUserViews[holdTrack.trackID].add(viewVelocityEvents);
				holdModule.registerCurrentTimeView(viewVelocityEvents);
			});
			if (viewSelectionBar.notNil and: {viewSelectionBar.view.notClosed}, {
				holdModule.scaledUserViews[holdTrack.trackID] = holdModule.scaledUserViews[holdTrack.trackID].add(viewSelectionBar);
			});
			holdModule.updateScaledUserViews;

		}); // end of:   if (holdTrack.displayMode == 'inputsOutputs', {

	} // end of init

	////////////////////

	drawInputsOutputs {
		var arrInputTypeStrings;
		if (holdTrack.inputType.isNil, { holdTrack.inputType = 'Midi'});
		if ((holdTrack.trackType == 'Note') || (holdTrack.trackType == 'Controller'), {

			// StaticText(holdParent, 1070 @ 1).background_(TXColor.white); // white line
			holdParent.decorator.nextLine.shift(0, 4);

			StaticText(holdParent, Rect(0, 0, 52, 20))
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow)
			.align_(\centre)
			.string_("Inputs:");

			holdParent.decorator.nextLine.shift(0, 10);

			// input type popup
			if (holdTrack.trackType == 'Note', {
				StaticText(holdParent, Rect(0, 0, 100, 20))
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
				.align_(\right)
				.string_("Input Type:");
				StaticText(holdParent, Rect(0, 0, 220, 20))
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
				.align_(\left)
				.string_(" Midi (only option for Note Tracks)");
			});
			if (holdTrack.trackType == 'Controller', {
				arrInputTypeStrings =["Midi", "Modulation Input", "Direct Input"];
				popupInputType = TXPopupPlusMinus(holdParent, 300 @ 20, "Input Select",
					arrInputTypeStrings,
					{arg view;
						holdModule.setTrackInputType(holdTrack, ['Midi','Modulation Input', 'Direct Input'].at(view.value));
						system.showView;},
					['Midi','Modulation Input', 'Direct Input'].indexOf(holdTrack.inputType), false, 100
				);
				popupInputType.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
				popupInputType.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
			});

			holdParent.decorator.nextLine.shift(0, 10);

			if (holdTrack.inputType == 'Midi', {
				// MidiLearn;
				chkboxMidiLearn = TXCheckBox(holdParent, 100 @ 20, "Midi Learn", TXColor.sysGuiCol1, TXColor.grey(0.9),
					TXColor.white, TXColor.sysGuiCol1)
				.value_((holdTrack.trackID == holdModule.midiLearnTrackID).asInteger)
				.action_({|view|
					holdModule.setMidiLearnFunc(holdTrack, view.value.asBoolean);
				});

				holdParent.decorator.shift(20, 0);

				/*   TESTING XXX MIDI PORT NOT NEEDED FOR NOW
				// label
				labelboxType = StaticText(holdParent, Rect(0, 0, 80, 20))
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
				.align_(\right)
				.string_("Midi Port");

				// MidiPort
				numboxMidiPort = NumberBox(holdParent, 50 @ 20)
				.background_(TXColor.white)
				.value_(holdTrack.midiPort)
				.action_({|view|
				holdTrack.midiPort = view.value;
				});

				holdParent.decorator.shift(20, 0);

				*/

				// MidiChannels - display as 1-16, store as 0-15
				rangeMidiChannels = TXRangeSlider(holdParent, 320 @ 20, "Midi Channels", ControlSpec(1,16, step:1),
					{|view|
						holdTrack.midiChannelMin = view.lo - 1;
						holdTrack.midiChannelMax = view.hi - 1;
					},
					holdTrack.midiChannelMin + 1, holdTrack.midiChannelMax + 1
				);
				rangeMidiChannels.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
				rangeMidiChannels.rangeView.background_(TXColor.sysGuiCol1);

				holdParent.decorator.shift(20, 0);
			});
		});
		if (holdTrack.trackType == 'Note', {
				holdParent.decorator.nextLine.shift(0, 20);
		});
		if (holdTrack.trackType == 'Controller', {
			if (holdTrack.inputType == 'Midi', {
				// label
				labelboxType = StaticText(holdParent, Rect(0, 0, 100, 20))
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
				.align_(\right)
				.string_("Midi Controller No");

				// MidiControllerNo
				numboxMidiControllerNo = NumberBox(holdParent, 50 @ 20)
				.background_(TXColor.white)
				.value_(holdTrack.midiControllerNo)
				.action_({|view|
					holdTrack.midiControllerNo = view.value;
					holdModule.restoreTrackMonitorRecordFuncs(holdTrack);
				});

				holdParent.decorator.shift(20, 0);

				// Use Pitch Bend Data
				chkboxUseBendData = TXCheckBox(holdParent, 180 @ 20, "Use MIDI Pitch Bend Instead", TXColor.sysGuiCol1, TXColor.grey(0.9),
					TXColor.white, TXColor.sysGuiCol1)
				.value_(holdTrack.useBendData)
				.action_({|view|
					holdTrack.useBendData = view.value;
					holdModule.restoreTrackMonitorRecordFuncs(holdTrack);
				});
				holdParent.decorator.nextLine.shift(0, 20);
			});
			if (holdTrack.inputType == 'Modulation Input', {
				// Modulation No
				holdView = TXSlider(holdParent, 300 @ 20, "Modulation No" , ControlSpec.new(1,16, step:1),
					{arg view; holdTrack.modulationInputIndex = view.value - 1;
						holdModule.setModulationInputVal(view.value);},
					(holdTrack.modulationInputIndex ? 0) + 1, false, 100, 40);
				holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
				holdView.sliderView.background_(TXColor.sysGuiCol1);

				holdParent.decorator.shift(20, 0);
				// Data rate
				holdView = TXPopupPlusMinus(holdParent, 460 @ 20, "MultiTrack Modulation Sample Rate",
					holdModule.arrOptionData[0].collect({arg item; item[0];}),
					{arg view; holdModule.arrOptions[0] = view.value; holdModule.rebuildSynth;},
					holdModule.arrOptions[0], false, 200
				);
				holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
				holdView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);

				holdParent.decorator.nextLine.shift(0, 20);
			});

			if (holdTrack.inputType == 'Direct Input', {
				// input val
				directInputSlider = TXSlider(holdParent, 300 @ 20, "Direct Input" , ControlSpec.new(0, 1),
					{arg view; holdModule.setDirectInputVal(holdTrack, view.value);},
					holdTrack.directInputVal ? 0, false, 100, 40);
				directInputSlider.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
				directInputSlider.sliderView.background_(TXColor.sysGuiCol1);
				// add screen update function
				system.addScreenUpdFunc(
					[directInputSlider, holdTrack],
					{ arg argArray;
						var argView = argArray.at(0), argTrack = argArray.at(1);
						argView.numberView.value_(argTrack.directInputVal.round(0.001));
						argView.sliderView.value = argView.controlSpec.unmap(argTrack.directInputVal);
					}
				);
				holdParent.decorator.shift(20, 0);
				// label directInputText
				holdModule.dataBank.directInputText = StaticText(holdParent, Rect(0, 0, 460, 20))
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.grey(0.9))
				.align_(\left);
				// initialise string
				holdModule.updateDirectInputText(holdTrack, holdTrack.directInputVal);

				// sync btn
				Button(holdParent, 300 @ 20)
				.states_([
					["Set direct input to current value of output 1", TXColor.white, TXColor.sysGuiCol1]
				])
				.action_({|view|
					var holdNewActionStep;
					directInputSlider.valueAction = holdModule.toolFuncs.getCurrentOut1ValueForTrack(holdTrack);
				});

			});
		});

		holdParent.decorator.nextLine.shift(0, 20);

		StaticText(holdParent, Rect(0, 0, 52, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow)
		.align_(\centre)
		.string_("Outputs:");

		holdParent.decorator.nextLine.shift(0, 10);

		if (holdTrack.trackType == 'Note', {
			// label
			labelboxType = StaticText(holdParent, Rect(0, 0, 80, 20))
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
			.align_(\right)
			.string_("Velocity Scale");

			// VelocityScale
			numboxVelocityScale = NumberBox(holdParent, 50 @ 20)
			.background_(TXColor.white)
			.value_(holdTrack.velocityScale)
			.action_({|view|
				view.value = view.value.min(5).max(0.01);
				holdTrack.velocityScale = view.value;
			});

			holdParent.decorator.shift(20, 0);

			// label
			labelboxType = StaticText(holdParent, Rect(0, 0, 80, 20))
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
			.align_(\right)
			.string_("Transpose");

			// Transpose
			numboxTranspose = NumberBox(holdParent, 50 @ 20)
			.background_(TXColor.white)
			.value_(holdTrack.transpose)
			.action_({|view|
				view.value = view.value.min(127).max(-127);
				holdTrack.transpose = view.value;
			});

			holdParent.decorator.nextLine.shift(0, 10);

			// 6 outputs
			6.do({arg count;
				var numString = (count + 1).asString;
				var outputActiveKey = ("output" ++ numString ++ "Active").asSymbol;
				var moduleIDKey = ("output" ++ numString ++ "ModuleID").asSymbol;

				// Active
				TXCheckBox(holdParent, 60 @ 20, "Active", TXColor.sysGuiCol1, TXColor.grey(0.9),
					TXColor.white, TXColor.sysGuiCol1)
				.value_(holdTrack[outputActiveKey])
				.action_({|view|
					holdModule.setTrackOutputActive(holdTrack, outputActiveKey, view.value);
				});

				holdParent.decorator.shift(20, 0);

				// label
				labelboxType = StaticText(holdParent, Rect(0, 0, 100, 20))
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
				.align_(\right)
				.string_("Output Module " ++ numString);

				arrGroupModules = system.arrSystemModules
				.select({ arg item, i;
					(item != holdModule) &&
					(item.class.moduleType == "groupsource" or: (item.class.moduleType == "groupaction"));
				});

				// TriggerOutput
				popupNoteTrigOutModule = PopUpMenu(holdParent, 160 @ 20)
				.background_(TXColor.white).stringColor_(TXColor.black)
				.items_(
					["select ..."]
					++ arrGroupModules.collect({ arg item, i; item.instDisplayName;});
				)
				.action_({arg view;
					if (view.value == 0, {
						holdTrack[moduleIDKey] = 0;
					}, {
						holdTrack[moduleIDKey] = arrGroupModules[view.value-1].moduleID;
					});
				});
				tempModule = system.getModuleFromID(holdTrack[moduleIDKey]);
				if (tempModule == 0, {
					popupNoteTrigOutModule.value = 0;
				},{
					popupNoteTrigOutModule.value = 1 + (arrGroupModules.indexOf(tempModule) ? 0);
				});

				holdParent.decorator.nextLine;
			});

		});

		if (holdTrack.trackType == 'Controller', {
			// 6 outputs
			6.do({arg count;
				var numString = (count + 1).asString;
				var outputActiveKey = ("output" ++ numString ++ "Active").asSymbol;
				var moduleIDKey = ("output" ++ numString ++ "ModuleID").asSymbol;
				var parameterNameKey = ("output" ++ numString ++ "ParameterName").asSymbol;

				// Active
				TXCheckBox(holdParent, 60 @ 20, "Active", TXColor.sysGuiCol1, TXColor.grey(0.9),
					TXColor.white, TXColor.sysGuiCol1)
				.value_(holdTrack[outputActiveKey])
				.action_({|view|
					holdTrack[outputActiveKey] = view.value;
				});

				holdParent.decorator.shift(20, 0);

				// label
				labelboxType = StaticText(holdParent, Rect(0, 0, 100, 20))
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
				.align_(\right)
				.string_("Output Module " ++ numString);

				arrValueModules = system.arrWidgetValueActionModules;

				// Module
				popupNoteTrigOutModule = PopUpMenu(holdParent, 160 @ 20)
				.background_(TXColor.white).stringColor_(TXColor.black)
				.items_(
					arrValueModules.collect({ arg item, i; item.instDisplayName;});
				)
				.action_({arg view;
					holdTrack[moduleIDKey] = arrValueModules[view.value].moduleID;
					holdArrActionSpecs = holdModule.toolFuncs.getControllerActionSpecsFromOutModuleId(holdTrack, holdTrack[moduleIDKey]);
					if (holdArrActionSpecs[0].notNil, {
						holdTrack[parameterNameKey] = holdArrActionSpecs[0].actionName;
					}, {
						holdTrack[parameterNameKey] = "";
					});
					holdModule.restoreTrackMonitorRecordFuncs(holdTrack);
					system.showView;
				});
				tempModule = system.getModuleFromID(holdTrack[moduleIDKey]);
				if (tempModule == 0, {
					popupNoteTrigOutModule.value = 0;
				},{
					popupNoteTrigOutModule.value = arrValueModules.indexOf(tempModule) ? 0;
				});

				holdParent.decorator.shift(20, 0);

				// label
				labelboxType = StaticText(holdParent, Rect(0, 0, 90, 20))
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
				.align_(\right)
				.string_("Parameter");

				holdArrActionSpecs = holdModule.toolFuncs.getControllerActionSpecsFromOutModuleId(holdTrack, holdTrack[moduleIDKey]);
				holdArrActionSpecNames = holdArrActionSpecs.asArray.collect({ arg item, i; item.actionName;});

				// Parameter
				popupNoteTrigOutParameter = PopUpMenu(holdParent, 300 @ 20)
				.background_(TXColor.white).stringColor_(TXColor.black)
				.items_(holdArrActionSpecNames)
				.action_({arg view;
					var holdArrActionSpecs = holdModule.toolFuncs.getControllerActionSpecsFromOutModuleId(holdTrack, holdTrack[moduleIDKey]);
					holdTrack[parameterNameKey] = holdArrActionSpecs[view.value].actionName;
					holdModule.restoreTrackMonitorRecordFuncs(holdTrack);
					system.showView;
				});
				popupNoteTrigOutParameter.value = holdArrActionSpecNames.indexOfEqual(holdTrack[parameterNameKey]) ? 0;

				holdParent.decorator.nextLine;
			});  // end of: 6.do
		});  // end of:  if (holdTrack.trackType == 'Controller', {

		holdParent.decorator.nextLine.shift(0, 10);

		// Active
		TXCheckBox(holdParent, 60 @ 20, "Active", TXColor.sysGuiCol1, TXColor.grey(0.9),
			TXColor.white, TXColor.sysGuiCol1)
		.value_(holdTrack.outputMidiActive)
		.action_({|view|
			holdTrack.outputMidiActive = view.value;
		});

		holdParent.decorator.shift(20, 0);

		// label
		labelboxType = StaticText(holdParent, Rect(0, 0, 100, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
		.align_(\right)
		.string_("Midi Output Port");

		holdPortNames = ["Unconnected"]
		++ MIDIClient.destinations.collect({arg item, i;
			// remove any brackets from string
			(item.device.asString + item.name.asString).replace("(", "").replace(")", "")
		});

		// midi port
		PopUpMenu(holdParent, 160 @ 20)
		.background_(TXColor.white).stringColor_(TXColor.black)
		.items_(holdPortNames)
		.value_(holdTrack.midiPortInd)
		.action_({arg view;
			holdTrack.midiPortInd = view.value;
			holdModule.initMidiPort(holdTrack);
		});

		holdParent.decorator.shift(20, 0);

		// Midi Loopback
		TXCheckBox(holdParent, 120 @ 20, "Midi Loopback", TXColor.sysGuiCol1, TXColor.grey(0.9),
			TXColor.white, TXColor.sysGuiCol1)
		.value_(holdTrack.outputMidiLoopback)
		.action_({|view|
			holdTrack.outputMidiLoopback = view.value;
		});

		holdParent.decorator.nextLine.shift(84, 2);

		// Midi Channel
		holdView = TXSlider(holdParent, 264 @ 20, "Midi Channel" , ControlSpec.new(1,16, step:1),
			{arg view; holdTrack.outputMidiChannel = view.value - 1}, (holdTrack.outputMidiChannel ? 0) + 1, false, 100, 40);
		holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		holdView.sliderView.background_(TXColor.sysGuiCol1);

		holdParent.decorator.shift(20, 0);

		if (holdTrack.trackType == 'Controller', {
			// Midi ControllerNo
			holdView = TXSlider(holdParent, 280 @ 20, "Midi Controller" , ControlSpec.new(0, 127, step:1),
				{arg view; holdTrack.outputMidiControllerNo = view.value}, holdTrack.outputMidiControllerNo, false, 100, 40);
			holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			holdView.sliderView.background_(TXColor.sysGuiCol1);

			holdParent.decorator.shift(20, 0);

			// Midi PitchBend
			TXCheckBox(holdParent, 140 @ 20, "Send As Pitch Bend ", TXColor.sysGuiCol1, TXColor.grey(0.9),
				TXColor.white, TXColor.sysGuiCol1)
			.value_(holdTrack.outputMidiPitchBend)
			.action_({|view|
				holdTrack.outputMidiPitchBend = view.value;
			});
		});

	}

	////////////////////

	drawControllerGui {

		holdParent.decorator.shift(20, 0);

		// Quantize Value
		TXCheckBox(holdParent, 120 @ 20, "Quantize Value", TXColor.sysGuiCol1, TXColor.grey(0.9),
			TXColor.white, TXColor.sysGuiCol1)
		.value_(holdTrack.quantizeValue)
		.action_({|view| holdTrack.quantizeValue = view.value; });

		holdParent.decorator.shift(20, 0);

		// Quantize Value Steps
		holdView = TXNumber(holdParent, 150 @ 20, "Quantize Steps", ControlSpec(1, 10000, step: 1),
			{|view| holdTrack.quantizeValueSteps = view.value; system.showView;},
			// get initial value
			holdTrack.quantizeValueSteps,
			false, 90, 56
		);
		holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);

		holdParent.decorator.shift(20, 0);

		// showSnapGrid
		TXCheckBox(holdParent, 120 @ 20, "Show Snap Grid", TXColor.sysGuiCol2, TXColor.grey(0.9),
			TXColor.white, TXColor.sysGuiCol1)
		.value_(holdTrack.showSnapGrid)
		.action_({|view|
			holdTrack.showSnapGrid = view.value;
			system.showView;
		});

		// if (holdTrack.displayMode == 'editSelection', {
		// 	// Show Processes
		// 	TXCheckBox(holdParent, 140 @ 20, "Show Processes", TXColor.sysGuiCol1, TXColor.grey(0.9),
		// 	TXColor.white, TXColor.sysGuiCol1)
		// 	.value_(holdTrack.showProcesses)
		// 	.action_({|view|
		// 		holdTrack.showProcesses = view.value;
		// 		system.showView;
		// 	});
		// });

		holdParent.decorator.nextLine.shift(0, 3);

		// ========================
		// Current PlayTime
		numboxCurrentPlayTimeBars = TXNumber(holdParent, 70 @ 20, "Time", ControlSpec(1, 10000, step: 1),
			{|view|
				var totalBeats;
				totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
				+ (numboxCurrentPlayTimeBeats.value - 1);
				totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
				this.updateCurrentPlayTime(totalBeats);
				system.showView;
			},
			// initial value
			1,
			false, 26, 34
		);
		numboxCurrentPlayTimeBars.labelView.stringColor_(TXColor.sysRecordCol).background_(TXColor.white);
		numboxCurrentPlayTimeBars.numberView.stringColor_(TXColor.sysRecordCol);
		numboxCurrentPlayTimeBars.numberView.normalColor_(TXColor.sysRecordCol);

		numboxCurrentPlayTimeBeats = TXNumber(holdParent, 40 @ 20, ": ",
			ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
			{|view|
				var totalBeats;
				totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (numboxCurrentPlayTimeBars.value - 1)) + (view.value - 1);
				totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
				this.updateCurrentPlayTime(totalBeats);
				system.showView;
			},
			// initial value
			1 ,
			false, 10, 30
		);
		numboxCurrentPlayTimeBeats.labelView.stringColor_(TXColor.sysRecordCol).background_(TXColor.white);
		numboxCurrentPlayTimeBeats.numberView.stringColor_(TXColor.sysRecordCol);
		numboxCurrentPlayTimeBeats.numberView.normalColor_(TXColor.sysRecordCol);

		// prev marker btn
		btnPrevMarker  = Button(holdParent, 13 @ 20)
		.states_([
			["<", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			var holdNewActionStep;
			holdModule.jumpToPreviousMarker;
			system.showView;
		});

		// next marker btn
		btnNextMarker  = Button(holdParent, 13 @ 20)
		.states_([
			[">", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			var holdNewActionStep;
			holdModule.jumpToNextMarker;
			system.showView;
		});

		numboxCurrentPlayTimeBeats.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		this.updateCurrentPlayTimeText; // set initial values

		// add marker btn
		btnAddMarker  = Button(holdParent, 66 @ 20)
		.states_([
			["Add Marker", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			var holdNewActionStep;
			holdModule.addMarkerAtCurrentPosition(textboxMarkerLabel.string);
			system.showView;
		});

		// name
		textboxMarkerLabel = TextField(holdParent, Rect(0, 0, 80, 20),
			" ", TXColor.black, TXColor.white,
			TXColor.black, TXColor.white, 4);
		textboxMarkerLabel.action = {arg view;
			//holdMarkerLabelString = view.string;
		};
		this.updateCurrentMarkerLabel; // set initial string

		// delete marker btn
		btnDeleteMarker  = Button(holdParent, 24 @ 20)
		.states_([
			["Del", TXColor.white, TXColor.sysDeleteCol]
		])
		.action_({|view|
			var holdNewActionStep;
			holdModule.deleteMarkerAtCurrentPosition;
			system.showView;
		});

		/////////

		holdParent.decorator.shift(20);

		displayRangeSlider = TXRangeSlider(holdParent, 630 @ 20, "Bars",
			ControlSpec(1, holdModule.getSynthArgSpec("maxDisplayBars") + 1),
			{arg view;
				var loVal = min(view.lo, view.hi);
				var hiVal = max(view.lo, view.hi);
				var maxBars = holdModule.getSynthArgSpec("maxDisplayBars") + 1;
				var minDisplaySize = 1 / holdModule.getSynthArgSpec("beatsPerBar");
				hiVal = max(hiVal, loVal + minDisplaySize);
				if (hiVal > maxBars, {
					hiVal = maxBars;
					loVal = hiVal - minDisplaySize;
				});
				holdModule.setSynthArgSpec("displayRangeStart", loVal);
				holdModule.setSynthArgSpec("displayRangeEnd", hiVal);
				holdModule.updateScaledUserViews;
				barTextView.refresh;
			},
			holdModule.getSynthArgSpec("displayRangeStart"), holdModule.getSynthArgSpec("displayRangeEnd"),
			labelWidth: 27
		);
		displayRangeSlider.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		displayRangeSlider.rangeView.background_(TXColor.sysGuiCol1);

		// max zoom out time
		Button(holdParent, 30 @ 20)
		.states_([
			["max", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			displayRangeSlider.valueBoth_([0 , holdModule.getSynthArgSpec("maxDisplayBars") + 1]);
		});

		holdView =  NumberBox(holdParent, 44 @ 20)
		.maxDecimals_(4)
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
		.value_(holdModule.getSynthArgSpec("maxDisplayBars") + 1) // add 1 for display
		.action_({|view|
			var change = 0;
			var lo = holdModule.getSynthArgSpec("displayRangeStart");
			var hi = holdModule.getSynthArgSpec("displayRangeEnd");
			var holdVal = view.value.max(2);
			holdModule.setSynthArgSpec("maxDisplayBars", holdVal - 1); // subtract 1 when stored
			if (hi > holdVal, {
				change = hi - holdVal;
				holdModule.setSynthArgSpec("displayRangeStart", (lo - change).max(1));
				holdModule.setSynthArgSpec("displayRangeEnd", (hi - change));
			});
			system.showView;
		});

		holdParent.decorator.nextLine.shift(0, 3);

		// store decorator position
		holdDecLeft = holdParent.decorator.left;
		holdDecTop = holdParent.decorator.top;

		this.drawSelectionBar(scaledUserViewWidth);

		holdView  = Button(holdParent, 16 @ 20)
		.states_([ ["|", TXColor.white, TXColor.sysGuiCol1] ])
		.action_({|view|
			holdTrack.valueRangeMin = 0;
			holdTrack.valueRangeMax = 1;
			system.showView;
		});

		holdParent.decorator.nextLine;

		// store decorator position
		holdDecLeft = holdParent.decorator.left;
		holdDecTop = holdParent.decorator.top;

		if ((holdTrack.displayMode == 'editSelection') and: (holdTrack.showProcesses == 1), {
			holdCtrlViewHeight = 200;
		},{
			holdCtrlViewHeight = 300;
		});
		// track events
		holdMaxDisplayBeats = holdModule.getSynthArgSpec("maxDisplayBars") * holdModule.getSynthArgSpec("beatsPerBar");
		viewTrackEvents = ScaledUserView( holdParent, scaledUserViewWidth @ holdCtrlViewHeight, Rect(0,  0, holdMaxDisplayBeats, 127));
		if (holdTrack.mute.asBoolean, {
			viewTrackEvents.background = TXColor.white.blend(TXColor.brown, 0.1);
		}, {
			viewTrackEvents.background = TXColor.white;
		});
		//viewTrackEvents.view.resize = 5;
		viewTrackEvents.refresh;
		viewTrackEvents.gridMode = [\lines, \lines];
		viewTrackEvents.gridLines = [ holdModule.getSynthArgSpec("maxDisplayBars"), 0 ];  // number of lines for H and V
		viewTrackEvents.gridSpacingV = 0;
		viewTrackEvents.drawFunc = {arg view;
			var fixWidth = view.pixelScale.x;
			var fixWidth2 = fixWidth * 2;
			var fixHeight = view.pixelScale.y;
			var holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
			var holdMaxDisplayBeats = holdModule.getSynthArgSpec("maxDisplayBars") * holdBeatsPerBar;
			var holdDisplayRangeStart = (holdModule.getSynthArgSpec("displayRangeStart")-1) * holdBeatsPerBar;
			var holdDisplayRangeEnd = (holdModule.getSynthArgSpec("displayRangeEnd")-1) * holdBeatsPerBar;
			// draw snap grid
			if (holdTrack.showSnapGrid.asBoolean and: {(((holdDisplayRangeEnd - holdDisplayRangeStart) / holdTrack.snapBeats) < 300)}, {
				(holdMaxDisplayBeats/ holdTrack.snapBeats).floor.do({arg i;
					var posX = i * holdTrack.snapBeats;
					// if inside view then draw
					if ((posX >= holdDisplayRangeStart) && (posX < holdDisplayRangeEnd), {
						// if bar or beat draw thicker line
						if ((posX % holdBeatsPerBar) == 0, {
							Pen.width = fixWidth2;
						},{
							if ((posX % 1) == 0, {
								Pen.width = fixWidth;
							},{
								Pen.width = fixWidth * 0.5;
							});
						});
						Pen.strokeColor = TXColor.grey(0.75);
						Pen.line(posX@0, posX@127).stroke;
					});
				});
			});

			// draw quantizeValueSteps
			if (holdTrack.quantizeValueSteps > 1, {
				Pen.strokeColor = TXColor.grey(0.9);
				Pen.width = fixHeight;
				holdTrack.quantizeValueSteps.do({arg i;
					var holdY = 127 - (127 * (i / holdTrack.quantizeValueSteps));
					Pen.line(0@holdY, holdMaxDisplayBeats@holdY).stroke;
				});
			});
			// draw  current play time box
			Pen.fillColor = Color.red.alpha_(0.75);
			Pen.addRect(Rect(holdModule.currentPlayTime - fixWidth, 0, fixWidth2, 127 ));
			Pen.fill;
			// draw all controller events
			holdTrack.arrControllerVals.do({arg holdControllerEvent, i;
				var startTime, endTime, controlVal;
				controlVal = 127 - (holdControllerEvent[1] * 127);
				// // if first event, add extra line from start
				// if (i==0, {
				// 	startTime = 0;
				// 	endTime = holdControllerEvent[0];
				// 	Pen.strokeColor = Color.black;
				// 	Pen.width = 1;
				// 	Pen.line(startTime@controlVal, endTime@controlVal).stroke;
				// });
				startTime = holdControllerEvent[0];
				// if last item, draw to end
				if (i == (holdTrack.arrControllerVals.size - 1), {
					endTime = holdMaxDisplayBeats;
				}, {
					endTime = holdTrack.arrControllerVals[i+1][0];
				});
				if ((endTime > holdDisplayRangeStart) && (startTime < holdDisplayRangeEnd), {
					// draw rect
					//Pen.fillColor = Color.grey(0.85, 0.5);
					Pen.fillColor = TXColor.sysGuiCol1.copy.alpha_(0.3);
					Pen.fillRect(Rect(startTime, controlVal, endTime-startTime, 127-controlVal));
					// draw lines
					// Pen.strokeColor = TXColor.sysGuiCol1;
					// Pen.width = fixWidth * 0.5;
					// Pen.line(startTime@controlVal, startTime@127).stroke;
					// Pen.line(endTime@controlVal, endTime@127).stroke;
					// Pen.width = fixHeight;
					// Pen.line(startTime@controlVal, endTime@controlVal).stroke;
					//Pen.width = fixHeight * 2;
					//Pen.line(startTime@controlVal, (startTime + fixWidth2)@controlVal).stroke;
					Pen.fillColor = TXColor.sysGuiCol1;
					Pen.addOval(Rect.aboutPoint(startTime@controlVal, fixWidth * 1.5,  fixHeight * 1.5)).fill;
				});
			});
		};

		viewTrackEvents.unscaledDrawFunc = {
			var holdVal, holdArrSteps;
			var holdMin = holdTrack.valueRangeMin ? 0;
			var holdMax = holdTrack.valueRangeMax ? 1;
			var out1String, minOut1String, maxOut1String;
			// show min & max
			Pen.color = TXColor.sysGuiCol2;
			if (holdOut1ControlSpec.isNil, {
				minOut1String = "";
				maxOut1String = "";
			}, {
				minOut1String = "  (out1: " ++ holdOut1ControlSpec.map(holdMin).round(0.001).asString ++ ")";
				maxOut1String = "  (out1: " ++ holdOut1ControlSpec.map(holdMax).round(0.001).asString ++ ")";
			});
			Pen.stringLeftJustIn( "min: " ++ holdMin.round(0.001).asString ++ minOut1String, Rect(4, holdCtrlViewHeight - 20, 200, 20));
			Pen.stringLeftJustIn( "max: " ++ holdMax.round(0.001).asString ++ maxOut1String, Rect(4, 0, 200, 20));
			holdArrSteps = [];
			(holdTrack.quantizeValueSteps - 1).do({arg i;
				var proportion = ((i + 1) / holdTrack.quantizeValueSteps);
				var heightProportion = 1 - ((proportion - holdMin) / (holdMax - holdMin));
				holdArrSteps = holdArrSteps.add([proportion, heightProportion]);
			});
			holdArrSteps = holdArrSteps.select({arg item, i;
				(item[1] > 0.07) && (item[1] < 0.93);
			});
			if (holdArrSteps.size > 10, {
				var keepCount = (holdArrSteps.size / 10).ceil;
				holdArrSteps = holdArrSteps.select({arg item, i;
					(i % keepCount) == 0;
				});
			});
			holdArrSteps.do({arg item, i;
				if (holdOut1ControlSpec.isNil, {
					out1String = "";
				}, {
					out1String = "  (out1: " ++ holdOut1ControlSpec.map(item[0]).round(0.001).asString ++ ")";
				});
				Pen.stringLeftJustIn(item[0].round(0.001).asString ++ out1String, Rect(4, (item[1] * holdCtrlViewHeight)-14, 200, 20));
			});
		};

		// restore decorator position
		holdParent.decorator.left = holdDecLeft;
		holdParent.decorator.top = holdDecTop;

		// edit view
		viewEditEvents = ScaledUserView( holdParent, scaledUserViewWidth @ holdCtrlViewHeight, Rect(0,  0, holdMaxDisplayBeats, 127) );
		viewEditEvents.background =(TXColor.white.alpha_(0));
		//viewEditEvents.view.resize = 5;
		viewEditEvents.refresh;
		if (holdTrack.displayMode.isNil, {
			holdTrack.displayMode = 'drawPoints';
		});
		if (holdTrack.displayMode == 'drawPoints', {
			viewEditEvents.mouseDownAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				this.updateMouseTextTimeControl(scaledX, scaledY);
				holdTrack.arrEditControllerVals = [[scaledX, 1 - (scaledY/127)]];
			};
			viewEditEvents.mouseMoveAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var holdDragDrawsMulti;
				if (isInside, {
					this.updateMouseTextTimeControl(scaledX, scaledY);
					holdDragDrawsMulti = holdModule.getSynthArgSpec("dragDrawsMultiPoints");
					if (holdDragDrawsMulti == 1, {
						holdTrack.arrEditControllerVals = holdTrack.arrEditControllerVals.add([scaledX, 1 - (scaledY/127)]);
					}, {
						holdTrack.arrEditControllerVals = [[scaledX, 1 - (scaledY/127)]];
					});
				});
			};
			viewEditEvents.mouseUpAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var holdDict;
				holdDict = ();
				if (isInside, {
					holdTrack.arrEditControllerVals = holdTrack.arrEditControllerVals.add([scaledX, 1 - (scaledY/127)]);
				});
				if (holdTrack.arrEditControllerVals.size > 0, {
					// remove time duplicates by using dict & sorting
					holdTrack.arrEditControllerVals.do({arg item, i;
						holdDict[item[0]] = item;
					});
					holdTrack.arrEditControllerVals = holdDict.asArray.sort({arg x, y; x[0] < y[0]});
					// save history with edit
					holdModule.toolFuncs.doWithHistory(holdTrack, {
						// snap edits
						holdModule.toolFuncs.snapEditControllerValsToGrid(holdTrack, holdTrack.snapToGrid, holdTrack.quantizeValue);
						// set Selection start/end
						holdModule.toolFuncs.setSelectionFromEditControllerVals(holdTrack);
						// merge & clear edits
						holdModule.toolFuncs.mergeEditControllerVals(holdTrack);
					});
					viewTrackEvents.refresh; // refresh other views
					viewSelectionBar.refresh;
					this.updateSelectionText;
				});
			};
			viewEditEvents.drawFunc = {arg view;
				var fixWidth = view.pixelScale.x;
				var fixWidth2 = fixWidth * 2;
				var fixHeight = view.pixelScale.y;
				var startTime, endTime, controlVal;
				var selectStart = holdTrack.selectionStart ? 0;
				var selectEnd = holdTrack.selectionEnd ? 0;
				// draw yellow selection box
				Pen.strokeColor = Color.yellow.alpha_(0.5);
				Pen.fillColor = Color.yellow.alpha_(0.2);
				Pen.width = fixWidth;
				Pen.addRect(Rect(selectStart, 0, selectEnd - selectStart, 127, ));
				Pen.fillStroke;
				// draw all edit controller events
				// if alone draw dot
				if (holdTrack.arrEditControllerVals.size == 1, {
					startTime = holdTrack.arrEditControllerVals[0][0];
					controlVal = 127 - (holdTrack.arrEditControllerVals[0][1] * 127);
					// draw lines
					Pen.strokeColor = Color.red;
					Pen.width = fixWidth;
					Pen.line(startTime@controlVal, startTime@127).stroke;
					Pen.fillColor = TXColor.red;
					Pen.addOval(Rect.aboutPoint(startTime@controlVal, fixWidth * 1.5,  fixHeight * 1.5)).fill;
				},{
					holdTrack.arrEditControllerVals.do({arg holdControllerEvent, i;
						controlVal = 127 - (holdControllerEvent[1] * 127);
						startTime = holdTrack.arrEditControllerVals[i][0];
						// if not last item, draw lines
						if (i != (holdTrack.arrEditControllerVals.size - 1), {
							endTime = holdTrack.arrEditControllerVals[i+1][0];
							// draw lines
							Pen.strokeColor = Color.red;
							Pen.width = fixWidth * 0.5;
							Pen.line(startTime@controlVal, startTime@127).stroke;
							Pen.line(endTime@controlVal, endTime@127).stroke;
							Pen.strokeColor = Color.red;
							Pen.width = fixHeight * 0.5;
							Pen.line(startTime@controlVal, endTime@controlVal).stroke;
							Pen.fillColor = TXColor.red;
							Pen.addOval(Rect.aboutPoint(startTime@controlVal, fixWidth * 1.5,  fixHeight * 1.5)).fill;
						});
					});
				});
			};
		});  // end of if (holdTrack.displayMode == 'drawPoints', {

		if (holdTrack.displayMode == 'drawLine', {
			viewEditEvents.mouseDownAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				this.updateMouseTextTimeControl(scaledX, scaledY);
				lineStartX = scaledX;
				lineStartY = scaledY;
				lineEndX = scaledX;
				lineEndY = scaledY;
			};
			viewEditEvents.mouseMoveAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				if (isInside, {
					this.updateMouseTextTimeControl(scaledX, scaledY);
					lineEndX = scaledX;
					lineEndY = scaledY;
				});
			};
			viewEditEvents.mouseUpAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var numSteps, holdLength, holdFrac, holdWarp, holdX, holdY;
				holdLength = abs(lineEndX - lineStartX);
				// if valid length, add series of points
				if (holdLength > 0, {
					if (isInside, {
						lineEndX = scaledX;
						lineEndY = scaledY;
					});
					numSteps = holdLength / (holdTrack.snapBeats ? 0.25);
					// clear before adding
					holdTrack.arrEditControllerVals = [];
					numSteps.floor.do({arg i;
						holdFrac = i / (numSteps.ceil - 1);
						holdWarp = TXLineWarp.getLineWarpVal(holdFrac, holdTrack.lineWarpIndex);  // modify with warp
						holdX = lineStartX + ((lineEndX - lineStartX) * holdFrac);
						holdY = lineStartY + ((lineEndY - lineStartY) * holdWarp);
						holdTrack.arrEditControllerVals = holdTrack.arrEditControllerVals.add([holdX, 1 - (holdY/127)]);
					});
					// add final event
					holdTrack.arrEditControllerVals = holdTrack.arrEditControllerVals.add([lineEndX, 1 - (lineEndY/127)]);
					// reverse if needed
					if (lineEndX < lineStartX, {
						holdTrack.arrEditControllerVals = holdTrack.arrEditControllerVals.reverse;
					});
					// save history with edit
					holdModule.toolFuncs.doWithHistory(holdTrack, {
						// snap edits
						holdModule.toolFuncs.snapEditControllerValsToGrid(holdTrack, holdTrack.snapToGrid, holdTrack.quantizeValue);
						// set Selection start/end
						holdModule.toolFuncs.setSelectionFromEditControllerVals(holdTrack);
						// merge & clear edits
						holdModule.toolFuncs.mergeEditControllerVals(holdTrack);
					});
					// refresh other views
					viewTrackEvents.refresh;
					viewSelectionBar.refresh;
					this.updateSelectionText;
				});
				lineStartX = -1;
				lineStartY = 0;
				lineEndX = -1;
				lineEndY = 0;
			};
			viewEditEvents.drawFunc = {arg view;
				var fixWidth = view.pixelScale.x;
				var fixWidth2 = fixWidth * 2;
				var fixHeight = view.pixelScale.y;
				var holdStart = holdTrack.selectionStart ? 0;
				var holdEnd = holdTrack.selectionEnd ? 0;
				// draw selection box
				Pen.strokeColor = Color.yellow.alpha_(0.5);
				Pen.fillColor = Color.yellow.alpha_(0.2);
				Pen.width = fixWidth;
				Pen.addRect(Rect(holdStart, 0, holdEnd - holdStart, 127, ));
				Pen.fillStroke;
				// draw lines
				Pen.strokeColor = Color.red;
				Pen.width = fixWidth;
				Pen.line(lineStartX@lineStartY, lineStartX@127).stroke;
				Pen.line(lineEndX@lineEndY, lineEndX@127).stroke;
				Pen.width = fixWidth2;
				Pen.line(lineStartX@lineStartY, lineEndX@lineEndY).stroke;
				Pen.fillColor = TXColor.red;
				Pen.addOval(Rect.aboutPoint(lineStartX@lineStartY, fixWidth * 1.5,  fixHeight * 1.5)).fill;
			};
		}); // end of if (holdTrack.displayMode == 'drawLine', {

		if (holdTrack.displayMode == 'editSelection', {
			viewEditEvents.mouseDownAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var startGap, endGap;
				this.updateMouseTextTimeControl(scaledX, scaledY);
				// modifiers:  shift: 131072   command: 1048576
				// if command pressed then save start pos
				if (mod == 1048576, {
					// if within boundaries then drag selection
					if ((scaledX >= holdTrack.selectionStart) && (scaledX <= holdTrack.selectionEnd), {
						holdDragEnabled = true;
						holdDragSelectionStart = scaledX;
					});
				}, {
					// else if shift pressed then alter existing selection start or end
					if (mod == 131072, {
						startGap = abs(scaledX - holdTrack.selectionStart);
						endGap = abs(scaledX - holdTrack.selectionEnd);
						if (startGap < endGap, {
							holdTrack.selectionStart = scaledX;
						}, {
							holdTrack.selectionEnd = scaledX;
						});
					}, {
						// else reset start & end
						holdTrack.selectionStart = scaledX;
						holdTrack.selectionEnd = scaledX;
					});
					this.updateSelectionText;
				});
			};
			viewEditEvents.mouseMoveAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var shiftX, holdMaxVal, startGap, endGap;
				if (isInside, {
					this.updateMouseTextTimeControl(scaledX, scaledY);
					// if command pressed then drag selection
					if (mod == 1048576, {
						// if within boundaries then drag selection
						if (holdDragEnabled && (scaledX >= holdTrack.selectionStart)
							&& (scaledX <= holdTrack.selectionEnd), {
								shiftX = scaledX - holdDragSelectionStart;
								holdDragSelectionStart = scaledX;
								holdMaxVal = holdModule.getSynthArgSpec("maxDisplayBars") *
								holdModule.getSynthArgSpec("beatsPerBar");
								holdTrack.selectionStart = (holdTrack.selectionStart + shiftX).clip(0, holdMaxVal);
								holdTrack.selectionEnd = (holdTrack.selectionEnd + shiftX).clip(0, holdMaxVal);
						});
					}, {
						// else if shift pressed then alter existing selection start or end
						if (mod == 131072, {
							startGap = abs(scaledX - holdTrack.selectionStart);
							endGap = abs(scaledX - holdTrack.selectionEnd);
							if (startGap < endGap, {
								holdTrack.selectionStart = scaledX;
							}, {
								holdTrack.selectionEnd = scaledX;
							});
						}, {
							if (scaledX > holdTrack.selectionStart, {
								holdTrack.selectionEnd = scaledX;
							},{
								holdTrack.selectionStart = scaledX;
							});
						});
					});
					this.updateSelectionText;
					this.updateSingleEventParameters;
				});
			};
			viewEditEvents.mouseUpAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				holdDragEnabled = false;
				// if command pressed then ignore
				if (isInside && {mod != 1048576}, {
					if (scaledX > holdTrack.selectionStart, {
						holdTrack.selectionEnd = scaledX;
					},{
						holdTrack.selectionStart = scaledX;
					});
				});
				holdModule.toolFuncs.snapSelectionToGrid(holdTrack);
				holdModule.toolFuncs.updateControllerSelectionIndices(holdTrack);
				viewSelectionBar.refresh;
				this.updateSelectionText;
				this.updateSelectedTotalText;
			};
			viewEditEvents.drawFunc = {arg view;
				var fixWidth = view.pixelScale.x;
				var fixWidth2 = fixWidth * 2;
				var fixHeight = view.pixelScale.y;
				var holdArrEditControllerVals, startTime, endTime ,controlVal;
				var holdStart = holdTrack.selectionStart ? 0;
				var holdEnd = holdTrack.selectionEnd ? 0;
				var holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
				var holdDisplayRangeStart = (holdModule.getSynthArgSpec("displayRangeStart")-1) * holdBeatsPerBar;
				var holdDisplayRangeEnd = (holdModule.getSynthArgSpec("displayRangeEnd")-1) * holdBeatsPerBar;
				// draw selection box
				Pen.strokeColor = Color.yellow.alpha_(0.5);
				Pen.fillColor = Color.yellow.alpha_(0.2);
				Pen.width = fixWidth;
				Pen.addRect(Rect(holdStart, 0, holdEnd - holdStart, 127, ));
				Pen.fillStroke;
				// draw all edit controller events
				holdArrEditControllerVals = holdTrack.selectionIndices.collect({arg ind, i;
					holdTrack.arrControllerVals[ind];
				});
				holdArrEditControllerVals.do({arg holdControllerEvent, i;
					controlVal = 127 - (holdControllerEvent[1] * 127);
					startTime = holdControllerEvent[0];
					if (holdArrEditControllerVals.size > 0, {
						if (i == (holdArrEditControllerVals.size - 1), {
							endTime = startTime;
						}, {
							endTime = holdArrEditControllerVals[i+1][0];
						});
						if ((endTime > holdDisplayRangeStart) && (startTime < holdDisplayRangeEnd), {
							// draw lines
							Pen.strokeColor = Color.red;
							Pen.width = fixWidth * 0.5;
							Pen.line(startTime@controlVal, startTime@127).stroke;
							Pen.line(endTime@controlVal, endTime@127).stroke;
							Pen.width = fixHeight;
							Pen.line(startTime@controlVal, endTime@controlVal).stroke;
							Pen.width = fixHeight * 2;
							Pen.line(startTime@controlVal, (startTime + 0.02)@controlVal).stroke;
							Pen.fillColor = TXColor.red;
							Pen.addOval(Rect.aboutPoint(startTime@controlVal, fixWidth * 1.5,  fixHeight * 1.5)).fill;
						});
					});
				});
			};

		}); // end of if (holdTrack.displayMode == 'editSelection', {

		holdParent.decorator.shift(-2,0);

		controllerValRangeView = RangeSlider(holdParent, 20 @ holdCtrlViewHeight)
		.background_(TXColor.sysGuiCol1).knobColor_(TXColor.grey(0.75))
		.lo_(holdTrack.valueRangeMin ? 0)
		.range_((holdTrack.valueRangeMax ? 1) - (holdTrack.valueRangeMin ? 0))
		.action_({ |slider|
			var holdRect;
			holdTrack.valueRangeMin = min(slider.lo, 0.95);
			holdTrack.valueRangeMax = slider.hi;
			if ((holdTrack.valueRangeMax - holdTrack.valueRangeMin) < 0.05, {
				holdTrack.valueRangeMax = holdTrack.valueRangeMin + 0.05;
			});
			[viewTrackEvents, viewEditEvents].do( {arg view;
				holdRect = view.viewRect;
				holdRect.top = (1 - holdTrack.valueRangeMax) * 127;
				holdRect.height = (holdTrack.valueRangeMax - holdTrack.valueRangeMin) * 127;
				view.viewRect_(holdRect);
			});
		});
		// defer update
		{controllerValRangeView.doAction;}.defer(0.03);

		holdParent.decorator.nextLine;

		// store decorator position
		holdDecLeft = holdParent.decorator.left;
		holdDecTop = holdParent.decorator.top;

		mouseTextView = StaticText(holdParent, 350 @ 20);
		mouseTextView.string = "Mouse: ...";
		mouseTextView.stringColor_(TXColor.sysGuiCol2).background_(TXColor.paleYellow);
		mouseTextView.align_(\left);

		// singleEventParameters
		if (holdTrack.displayMode == 'editSelection', {
			// restore decorator position
			holdParent.decorator.reset;
			holdParent.decorator.left = holdDecLeft;
			holdParent.decorator.top = holdDecTop;

			singleEventContainer = UserView(holdParent, 350 @ 20).background_(TXColor.paleYellow);
			singleEventContainer.addFlowLayout.gap_(0@0).margin_(0@0).reset;

			singleEventTimeBarsView = TXNumber(singleEventContainer, 106 @ 20, "Event Time", ControlSpec(1, 10000, step: 1),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
					+ (singleEventTimeBeatsView.value - 1);
					holdModule.toolFuncs.setSingleEditEventTime(holdTrack, totalBeats);
					system.showView;
				},
				// initial value
				1,
				false, 70, 34
			);
			singleEventTimeBarsView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

			singleEventTimeBeatsView = TXNumber(singleEventContainer, 50 @ 20, ": ",
				ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (singleEventTimeBarsView.value - 1)) + (view.value - 1);
					holdModule.toolFuncs.setSingleEditEventTime(holdTrack, totalBeats);
					system.showView;
				},
				// initial value
				1 ,
				false, 10, 34
			);
			singleEventTimeBeatsView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);


			singleEventValView = TXNumber(singleEventContainer, 78 @ 20, "Value",
				ControlSpec(0, 1),
				{|view|
					var holdVal;
					holdVal = view.value;
					holdModule.toolFuncs.setSingleEditEventVal(holdTrack, holdVal);
					system.showView;
				},
				// initial value
				1 ,
				false, 34, 40
			);
			singleEventValView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

			if (holdOut1ControlSpec.notNil, {
				singleEventOut1ValView = TXNumber(singleEventContainer, 104 @ 20, "Out1 Val",
					holdOut1ControlSpec,
					{|view|
						var holdVal;
						holdVal = view.value;
						holdModule.toolFuncs.setSingleEditEventVal(holdTrack, holdOut1ControlSpec.unmap(holdVal));
						system.showView;
					},
					// initial value
					1 ,
					false, 50, 50
				);
				singleEventOut1ValView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);
			});

			this.updateSingleEventParameters;


		});  // end of if (holdTrack.displayMode == 'editSelection', {

		holdParent.decorator.shift(20,0);

		// Undo Edit btn
		Button(holdParent, 70 @ 20)
		.states_([
			["< Undo Edit", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			holdModule.toolFuncs.undoEdit(holdTrack);
		});

		// Redo Edit btn
		Button(holdParent, 70 @ 20)
		.states_([
			["> Redo Edit", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			holdModule.toolFuncs.redoEdit(holdTrack);
		});

		holdParent.decorator.shift(30, 0);

		// Select None btn
		Button(holdParent, 74 @ 20)
		.states_([
			["Select None", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			holdTrack.selectionStart = 0;
			holdTrack.selectionEnd = 0;
			holdModule.toolFuncs.updateControllerSelectionIndices(holdTrack);
			system.showView;
		});

		// Select All btn
		Button(holdParent, 64 @ 20)
		.states_([
			["Select All", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			holdTrack.selectionStart = 0;
			holdTrack.selectionEnd = holdModule.getSynthArgSpec("maxDisplayBars") * holdModule.getSynthArgSpec("beatsPerBar");
			holdModule.toolFuncs.updateControllerSelectionIndices(holdTrack);
			system.showView;
		});

		// Zoom to Selection  btn
		Button(holdParent, 110 @ 20)
		.states_([
			["Zoom to Selection", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			var holdStart = holdTrack.selectionStart;
			var holdEnd = holdTrack.selectionEnd;
			if ( (holdEnd - holdStart) < 0.25, {
				holdEnd = holdStart + 0.25;
			});
			holdStart = holdStart /  holdModule.getSynthArgSpec("beatsPerBar");
			holdEnd = min((holdEnd  + 0.5) /  holdModule.getSynthArgSpec("beatsPerBar"), holdModule.getSynthArgSpec("maxDisplayBars"));
			holdModule.setSynthArgSpec("displayRangeStart", holdStart + 1);
			holdModule.setSynthArgSpec("displayRangeEnd", holdEnd + 1);
			system.showView;
		});

		if (holdTrack.displayMode == 'drawPoints', {
			holdParent.decorator.nextLine.shift(0, 4);
			// Mouse drag draws multiple points
			TXCheckBox(holdParent, 210 @ 20, "Mouse drag draws multiple points", TXColor.sysGuiCol1, TXColor.grey(0.9),
				TXColor.white, TXColor.sysGuiCol1)
			.value_(holdModule.getSynthArgSpec("dragDrawsMultiPoints"))
			.action_({|view| holdModule.setSynthArgSpec("dragDrawsMultiPoints", view.value) ;});
		});

		if (holdTrack.displayMode == 'drawLine', {
			holdParent.decorator.nextLine.shift(0, 4);

			// LineShapes popup
			popupLineShapes = TXPopupPlusMinus(holdParent, 300 @ 20, "Line Shape", TXLineWarp.getLineWarpStrings,
				{arg view;holdTrack.lineWarpIndex = view.value;}, holdTrack.lineWarpIndex ? 0);
			popupLineShapes.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			popupLineShapes.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
		});

		if (holdTrack.displayMode == 'editSelection', {
			holdParent.decorator.shift(30, 0);

			// Copy btn
			Button(holdParent, 54 @ 20)
			.states_([
				["Copy", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				holdModule.toolFuncs.makeCopyControllerData(holdTrack);
				system.showView;
			});

			// Delete btn
			Button(holdParent, 56 @ 20)
			.states_([
				["Delete", TXColor.white, TXColor.sysDeleteCol]
			])
			.action_({|view|
				// save history with edit
				holdModule.toolFuncs.doWithHistory(holdTrack, {
					holdModule.toolFuncs.deleteSelectedControllerVals(holdTrack);
				});
				system.showView;
			});

			totalTextView = StaticText(holdParent, 94 @ 20)
			.string_("selected " ++ holdTrack.selectionIndices.size.asString)
			.align_(\center)
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);

			holdParent.decorator.nextLine;

			holdTrack.processIndex = holdTrack.processIndex ? 0;
			holdTrack.processVar1 = holdTrack.processVar1 ? 1;
			holdTrack.processVar2 = holdTrack.processVar2 ? 1;

			// draw white line
			StaticText(holdParent, 1070 @ 1).background_(TXColor.white);
			holdParent.decorator.nextLine;

			// Show Processes btn
			btnShowProcesses = Button(holdParent, 17 @ 15)
			.states_([
				["^", TXColor.white, TXColor.sysGuiCol2],
				["v", TXColor.white, TXColor.sysGuiCol2]
			])
			.value_(holdTrack.showProcesses)
			.action_({|view|
				holdTrack.showProcesses = view.value;
				system.showView;
			});

			// Processes
			arrProcessStrings = holdModule.toolFuncs.getControllerProcessStrings;
			if (holdTrack.showProcesses == 1, {
				processSelectView = TXListViewLabelPlusMinus(holdParent, 460 @ 120, "Process", arrProcessStrings, {arg view;
					holdModule.toolFuncs.setControllerProcessIndex(holdTrack, view.value);
					system.showView;
				}, holdTrack.processIndex, false, 50);
			}, {
				processSelectView = TXPopupPlusMinus(holdParent, 460 @ 20, "Process", arrProcessStrings, {arg view;
					holdModule.toolFuncs.setControllerProcessIndex(holdTrack, view.value);
					system.showView;
				}, holdTrack.processIndex, false, 50);
				processSelectView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
			});
			processSelectView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);

			// Run Process btn
			Button(holdParent, 80 @ 20)
			.states_([
				["Run Process", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				// save history with edit
				holdModule.toolFuncs.doWithHistory(holdTrack, {
					holdModule.toolFuncs.runControllerProcess(holdTrack, holdTrack.processIndex);
				});
				system.showView;
			});

			// add sliders or popup if needed
			holdProcess = holdModule.toolFuncs.getControllerProcesses[holdTrack.processIndex];
			if (holdProcess[2][0].notNil, {
				holdParent.decorator.shift(8, 0);
				if (holdProcess[3][0].class == ControlSpec, {
					holdView = TXSlider(holdParent, 240 @ 20, holdProcess[2][0], holdProcess[3][0],
						{arg view; holdTrack.processVar1 = view.value}, holdTrack.processVar1, false, 90, 40);
					holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
					holdView.sliderView.background_(TXColor.sysGuiCol1);
				});
				if (holdProcess[3][0].isArray, {
					if (holdTrack.showProcesses == 1, {
						holdView = TXListViewLabelPlusMinus(holdParent, 240 @ 120, holdProcess[2][0], holdProcess[3][0],
							{arg view; holdTrack.processVar1 = view.value}, holdTrack.processVar1.asInteger, false, 46);
					}, {
						holdView = TXPopupPlusMinus(holdParent, 240 @ 20, holdProcess[2][0], holdProcess[3][0],
							{arg view; holdTrack.processVar1 = view.value}, holdTrack.processVar1.asInteger, false, 46);
						holdView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
					});
					holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
				});
			});
			if (holdProcess[2][1].notNil, {
				holdParent.decorator.shift(8, 0);
				if (holdProcess[3][1].class == ControlSpec, {
					holdView = TXSlider(holdParent, 240 @ 20, holdProcess[2][1], holdProcess[3][1],
						{arg view; holdTrack.processVar2 = view.value}, holdTrack.processVar2, false, 90, 40);
					holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
					holdView.sliderView.background_(TXColor.sysGuiCol1);
				});
				if (holdProcess[3][1].isArray, {
					if (holdTrack.showProcesses == 1, {
						holdView = TXListViewLabelPlusMinus(holdParent, 240 @ 120, holdProcess[2][1], holdProcess[3][1],
							{arg view; holdTrack.processVar2 = view.value}, holdTrack.processVar2.asInteger, false, 46);
					}, {
						holdView = TXPopupPlusMinus(holdParent, 240 @ 20, holdProcess[2][1], holdProcess[3][1],
							{arg view; holdTrack.processVar2 = view.value}, holdTrack.processVar2.asInteger, false, 46);
						holdView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
					});
					holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
				});
			});

		}); // end of if (holdTrack.displayMode == 'editSelection',

	}  // end of drawControllerGui

	////////////////////

	drawNoteGui {

		holdParent.decorator.shift(20, 0);

		// showSnapGrid
		TXCheckBox(holdParent, 120 @ 20, "Show Snap Grid", TXColor.sysGuiCol2, TXColor.grey(0.9),
			TXColor.white, TXColor.sysGuiCol1)
		.value_(holdTrack.showSnapGrid)
		.action_({|view|
			holdTrack.showSnapGrid = view.value;
			system.showView;
		});

		// Show Velocity Track
		TXCheckBox(holdParent, 140 @ 20, "Show Velocity Track", TXColor.sysGuiCol1, TXColor.grey(0.9),
			TXColor.white, TXColor.sysGuiCol1)
		.value_(holdTrack.showVelocityTrack)
		.action_({|view|
			holdTrack.showVelocityTrack = view.value;
			if (view.value == 1, {
				holdTrack.showProcesses = 0;
			});
			system.showView;
		});

		// if (holdTrack.displayMode == 'editNotes', {
		// 	// Show Processes
		// 	TXCheckBox(holdParent, 140 @ 20, "Show Processes", TXColor.sysGuiCol1, TXColor.grey(0.9),
		// 	TXColor.white, TXColor.sysGuiCol1)
		// 	.value_(holdTrack.showProcesses)
		// 	.action_({|view|
		// 		holdTrack.showProcesses = view.value;
		// 		if (view.value == 1, {
		// 			holdTrack.showVelocityTrack = 0;
		// 		});
		// 		system.showView;
		// 	});
		// });

		holdParent.decorator.nextLine.shift(0, 3);

		// ========================
		// Current PlayTime
		numboxCurrentPlayTimeBars = TXNumber(holdParent, 70 @ 20, "Time", ControlSpec(1, 10000, step: 1),
			{|view|
				var totalBeats;
				totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
				+ (numboxCurrentPlayTimeBeats.value - 1);
				totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
				this.updateCurrentPlayTime(totalBeats);
				system.showView;
			},
			// initial value
			1,
			false, 26, 34
		);
		numboxCurrentPlayTimeBars.labelView.stringColor_(TXColor.sysRecordCol).background_(TXColor.white);
		numboxCurrentPlayTimeBars.numberView.stringColor_(TXColor.sysRecordCol);
		numboxCurrentPlayTimeBars.numberView.normalColor_(TXColor.sysRecordCol);

		numboxCurrentPlayTimeBeats = TXNumber(holdParent, 40 @ 20, ": ",
			ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
			{|view|
				var totalBeats;
				totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (numboxCurrentPlayTimeBars.value - 1)) + (view.value - 1);
				totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
				this.updateCurrentPlayTime(totalBeats);
				system.showView;
			},
			// initial value
			1 ,
			false, 10, 30
		);
		numboxCurrentPlayTimeBeats.labelView.stringColor_(TXColor.sysRecordCol).background_(TXColor.white);
		numboxCurrentPlayTimeBeats.numberView.stringColor_(TXColor.sysRecordCol);
		numboxCurrentPlayTimeBeats.numberView.normalColor_(TXColor.sysRecordCol);

		// prev marker btn
		btnPrevMarker  = Button(holdParent, 13 @ 20)
		.states_([
			["<", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			var holdNewActionStep;
			holdModule.jumpToPreviousMarker;
			system.showView;
		});

		// next marker btn
		btnNextMarker  = Button(holdParent, 13 @ 20)
		.states_([
			[">", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			var holdNewActionStep;
			holdModule.jumpToNextMarker;
			system.showView;
		});

		numboxCurrentPlayTimeBeats.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		this.updateCurrentPlayTimeText; // set initial values

		// add marker btn
		btnAddMarker  = Button(holdParent, 66 @ 20)
		.states_([
			["Add Marker", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			var holdNewActionStep;
			holdModule.addMarkerAtCurrentPosition(textboxMarkerLabel.string);
			system.showView;
		});

		// name
		textboxMarkerLabel = TextField(holdParent, Rect(0, 0, 80, 20),
			" ", TXColor.black, TXColor.white,
			TXColor.black, TXColor.white, 4);
		textboxMarkerLabel.action = {arg view;
			//holdMarkerLabelString = view.string;
		};
		this.updateCurrentMarkerLabel; // set initial string

		// delete marker btn
		btnDeleteMarker  = Button(holdParent, 24 @ 20)
		.states_([
			["Del", TXColor.white, TXColor.sysDeleteCol]
		])
		.action_({|view|
			var holdNewActionStep;
			holdModule.deleteMarkerAtCurrentPosition;
			system.showView;
		});

		/////////

		holdParent.decorator.shift(20);

		displayRangeSlider = TXRangeSlider(holdParent, 630 @ 20, "Bars",
			ControlSpec(1, holdModule.getSynthArgSpec("maxDisplayBars") + 1),
			{arg view;
				var loVal = min(view.lo, view.hi);
				var hiVal = max(view.lo, view.hi);
				var maxBars = holdModule.getSynthArgSpec("maxDisplayBars") + 1;
				var minDisplaySize = 1 / holdModule.getSynthArgSpec("beatsPerBar");
				hiVal = max(hiVal, loVal + minDisplaySize);
				if (hiVal > maxBars, {
					hiVal = maxBars;
					loVal = hiVal - minDisplaySize;
				});
				holdModule.setSynthArgSpec("displayRangeStart", loVal);
				holdModule.setSynthArgSpec("displayRangeEnd", hiVal);
				holdModule.updateScaledUserViews;
				barTextView.refresh;
			},
			holdModule.getSynthArgSpec("displayRangeStart"), holdModule.getSynthArgSpec("displayRangeEnd"),
			labelWidth: 27
		);
		displayRangeSlider.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);

		// max zoom out time
		Button(holdParent, 30 @ 20)
		.states_([
			["max", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			displayRangeSlider.valueBoth_([0 , holdModule.getSynthArgSpec("maxDisplayBars") + 1]);
		});

		holdView =  NumberBox(holdParent, 44 @ 20)
		.maxDecimals_(4)
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
		.value_(holdModule.getSynthArgSpec("maxDisplayBars") + 1) // add 1 for display
		.action_({|view|
			var change = 0;
			var lo = holdModule.getSynthArgSpec("displayRangeStart");
			var hi = holdModule.getSynthArgSpec("displayRangeEnd");
			var holdVal = view.value.max(2);
			holdModule.setSynthArgSpec("maxDisplayBars", holdVal - 1); // subtract 1 when stored
			if (hi > holdVal, {
				change = hi - holdVal;
				holdModule.setSynthArgSpec("displayRangeStart", (lo - change).max(1));
				holdModule.setSynthArgSpec("displayRangeEnd", (hi - change));
			});
			system.showView;
		});

		holdParent.decorator.nextLine.shift(0, 3);

		// Auto Select Notes
		holdView = TXCheckBox(holdParent, 78 @ 20, "auto select", TXColor.sysGuiCol2, TXColor.grey(0.9),
			TXColor.white, TXColor.sysGuiCol1)
		.value_(holdTrack.autoSelectNotes)
		.action_({|view|
			holdTrack.autoSelectNotes = view.value;
			if (view.value == 1, {
				// set arrSelectionIndices and arrEditNotes based on selection range
				holdModule.toolFuncs.rebuildEditNotesFromSelectionStartEnd(holdTrack);
				holdModule.toolFuncs.rebuildSelectionIndicesFromEditNotes(holdTrack);
				viewEditEvents.refresh;
				if (viewVelocityEvents.notNil, {viewVelocityEvents.refresh;});
				this.updateSelectedTotalText;
			});
		});
		if (holdTrack.displayMode != 'editNotes', { // hide if not editNotes'
			holdView.visible_(false);
		});
		holdDecLeft = holdParent.decorator.shift(-2,0);

		// store decorator position
		holdDecLeft = holdParent.decorator.left;
		holdDecTop = holdParent.decorator.top;

		this.drawSelectionBar(966, {// selectionChangeAction
			if ((holdTrack.autoSelectNotes.asBoolean == true) and: {holdTrack.displayMode == 'editNotes'}, {
				// set arrSelectionIndices and arrEditNotes based on selection range
				holdModule.toolFuncs.rebuildEditNotesFromSelectionStartEnd(holdTrack);
				holdModule.toolFuncs.rebuildSelectionIndicesFromEditNotes(holdTrack);
				viewEditEvents.refresh;
				if (viewVelocityEvents.notNil, {viewVelocityEvents.refresh;});
				this.updateSelectedTotalText;
			},{
				holdModule.toolFuncs.adjustEditNotesToSelection(holdTrack);
			});
			viewEditEvents.refresh;
			if (viewVelocityEvents.notNil, {viewVelocityEvents.refresh;});
			this.updateSelectionText;
		});

		holdParent.decorator.nextLine;

		if ((holdTrack.showVelocityTrack == 1) or: ((holdTrack.displayMode == 'editNotes') and: (holdTrack.showProcesses == 1)), {
			holdNoteViewHeight = 200;
		},{
			holdNoteViewHeight = 300;
		});
		// add ScrollView
		scrollView = ScrollView(holdParent, Rect(0, 0, 78, holdNoteViewHeight)).hasBorder_(false);
		scrollView.hasHorizontalScroller = false;
		scrollView.hasVerticalScroller = false;
		scrollBox = CompositeView(scrollView, Rect(0, 0, 78, 910));
		scrollView.visibleOrigin = Point.new(0, (holdTrack.midiKeybScroll ? 0.5) * (900 - holdNoteViewHeight));
		scrollView.action = scrollViewAction;
		// create midiKeyboard
		midiKeyboard = TXMIDIKeyboard.new(scrollBox, Rect(0, 0, 78, 900), 10, 48, horizontal: false);
		// add note numbers to keyboard
		holdParent.decorator.shift(-2, 0);
		10.do({ arg item;
			StaticText(scrollBox, Rect(0, ((item+1) * 90)-(90/8)-5, 70, 20))
			.align_(\right)
			.string_("C " ++ (7-item).asString);
		});

		// store decorator position
		holdDecLeft = holdParent.decorator.left;
		holdDecTop = holdParent.decorator.top;

		// track events
		holdMaxDisplayBeats = holdModule.getSynthArgSpec("maxDisplayBars") * holdModule.getSynthArgSpec("beatsPerBar");
		viewTrackEvents = ScaledUserView( holdParent, 966 @ holdNoteViewHeight, Rect(0, 0, holdMaxDisplayBeats, 120) );
		if (holdTrack.mute.asBoolean, {
			viewTrackEvents.background = TXColor.white.blend(TXColor.brown, 0.1);
		}, {
			viewTrackEvents.background = TXColor.white;
		});
		//viewTrackEvents.view.resize = 5;
		viewTrackEvents.scaleV = 900 / holdNoteViewHeight;
		viewTrackEvents.moveV = holdTrack.midiKeybScroll;
		viewTrackEvents.gridMode = [\lines, \lines];
		viewTrackEvents.gridLines = [ holdModule.getSynthArgSpec("maxDisplayBars"), 13 ];  // number of lines for H and V
		viewTrackEvents.gridSpacingV = 0;
		viewTrackEvents.view.mouseWheelAction = {arg view, x, y, modifiers, xDelta, yDelta;
			//("yDelta : " ++ yDelta).postln;
			midiKeybScrollSlider.valueAction = (midiKeybScrollSlider.value + (yDelta * 0.003));
			true; // stops mouseWheelAction being passed to parent view
		};
		viewTrackEvents.refresh;
		// save view
		viewTrackEvents.drawFunc = {arg view;
			var fixWidth = view.pixelScale.x;
			var fixWidth2 = fixWidth * 2;
			var fixHeight = view.pixelScale.y;
			var holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
			var holdMaxDisplayBeats = holdModule.getSynthArgSpec("maxDisplayBars") * holdBeatsPerBar;
			var holdDisplayRangeStart = (holdModule.getSynthArgSpec("displayRangeStart")-1) * holdBeatsPerBar;
			var holdDisplayRangeEnd = (holdModule.getSynthArgSpec("displayRangeEnd")-1) * holdBeatsPerBar;
			var arr12Note;
			arr12Note= [0,1,0,1,0,0,1,0,1,0,1,0];
			// draw grey sharp/flat lines
			128.do({arg item, i;
				if (arr12Note.wrapAt(i+4) == 1, {
					Pen.fillColor = Color.grey(0.8);
					Pen.fillRect(Rect(0, 127-i, holdMaxDisplayBeats, 1));
				});
			});
			// draw snap grid
			if (holdTrack.showSnapGrid.asBoolean and: {(((holdDisplayRangeEnd - holdDisplayRangeStart) / holdTrack.snapBeats) < 300)}, {
				(holdMaxDisplayBeats/ holdTrack.snapBeats).floor.do({arg i;
					var posX = i * holdTrack.snapBeats;
					// if inside view then draw
					if ((posX >= holdDisplayRangeStart) && (posX < holdDisplayRangeEnd), {
						// if bar or beat draw thicker line
						if ((posX % holdBeatsPerBar) == 0, {
							Pen.width = fixWidth2;// bar line
						},{
							if ((posX % 1) == 0, {
								Pen.width = fixWidth;  // beat line
							},{
								Pen.width = fixWidth * 0.5;  // snap line
							});
						});
						Pen.strokeColor = TXColor.grey(0.75);
						Pen.line(posX@0, posX@120).stroke;
					});
				});
			});
			// draw  current play time box
			Pen.fillColor = Color.red.alpha_(0.75);
			Pen.addRect(Rect(holdModule.currentPlayTime - fixWidth, 0, fixWidth2, 127 ));
			Pen.fill;
			// draw all Note events
			holdTrack.arrNotes.do({arg holdNoteEvent, i;
				var startTime, endTime, playTime, noteVal, vel;
				noteVal = holdNoteEvent[1];
				startTime = holdNoteEvent[0];
				endTime = holdNoteEvent[4];
				playTime = max(endTime - startTime, 0.01);
				if ((endTime > holdDisplayRangeStart) && (startTime < holdDisplayRangeEnd), {
					vel = holdNoteEvent[2]/127;
					if (vel < 0.5, {
						Pen.fillColor = TXColor.green(0.5).blend(TXColor.yellow2, vel * 2);
					},{
						Pen.fillColor = TXColor.yellow2.blend(TXColor.red, (vel * 2) - 1);
					});
					//Pen.line(startTime@noteVal, endTime@noteVal).stroke;
					Pen.fillRect(Rect(startTime, 119-noteVal, playTime, 1));
				});
			});
		};
		// restore decorator position
		holdParent.decorator.left = holdDecLeft;
		holdParent.decorator.top = holdDecTop;

		// edit view
		viewEditEvents = ScaledUserView( holdParent, 966 @ holdNoteViewHeight, Rect(0, 0, holdMaxDisplayBeats, 120) );
		viewEditEvents.background =(TXColor.white.alpha_(0));
		//viewEditEvents.view.resize = 5;
		viewEditEvents.scaleV = 900 / holdNoteViewHeight;
		viewEditEvents.moveV = holdTrack.midiKeybScroll;
		viewEditEvents.view.mouseWheelAction = {arg view, x, y, modifiers, xDelta, yDelta;
			//("yDelta : " ++ yDelta).postln;
			midiKeybScrollSlider.valueAction = (midiKeybScrollSlider.value + (yDelta * 0.003));
			true; // stops mouseWheelAction being passed to parent view
		};
		viewEditEvents.refresh;
		if (holdTrack.displayMode.isNil, {
			holdTrack.displayMode = 'drawNotes';
		});
		if (holdTrack.displayMode == 'drawNotes', {
			viewEditEvents.mouseDownAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var holdTime, holdNote, holdNoteEventData;
				this.updateMouseTextTimeNote(scaledX, scaledY);
				oldScaledX = scaledX;
				oldScaledY = scaledY;
				holdTime = scaledX;
				holdNote = (120 - scaledY).floor;
				if (holdTrack.selectionIndices.asArray.size > 0, {
					holdNoteEventData = holdTrack.arrNotes.at(holdTrack.selectionIndices[0]);
				});
				// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
				if (holdNoteEventData.notNil, {
					// if noteNum matches
					if (holdNoteEventData[1] == holdNote, {
						// if mouse is over currently selected note start
						if (abs(holdNoteEventData[0] - holdTime) < 0.05, {
							dragMode = 'dragStartEndTimeVel';
							holdModule.toolFuncs.storePreEditHistory(holdTrack);
						}, {
							// else if mouse is over currently selected note end
							if (abs(holdNoteEventData[4] - holdTime) < 0.05, {
								dragMode = 'dragEndTime';
								holdModule.toolFuncs.storePreEditHistory(holdTrack);
							}, {
								// else if mouse is over currently selected note box
								if ((holdTime > holdNoteEventData[0]) && (holdTime < holdNoteEventData[4]), {
									dragMode = 'dragStartEndTimePitch';
									holdModule.toolFuncs.storePreEditHistory(holdTrack);
								});
							});
						});
					});
				});
				// else add note
				if (dragMode.isNil, {
					dragMode = 'dragAddNote';
					// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
					newNoteData = [holdTime, holdNote, holdTrack.defaultVelocity, 0, holdTime];
				});
			};
			viewEditEvents.mouseMoveAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var deltaX, deltaY;
				if (isInside, {
					this.updateMouseTextTimeNote(scaledX, scaledY);
					deltaX = scaledX - oldScaledX;
					deltaY = scaledY - oldScaledY;
					oldScaledX = scaledX;
					oldScaledY = scaledY;
					case {dragMode == 'dragStartEndTimeVel';} {
						// set start time, end time & vel
						holdTrack.arrEditNotes[0][0] = (holdTrack.arrEditNotes[0][0] + deltaX).max(0);
						holdTrack.arrEditNotes[0][4] = (holdTrack.arrEditNotes[0][4] + deltaX).max(0);
						holdTrack.arrEditNotes[0][2] = (holdTrack.arrEditNotes[0][2] - (deltaY*5)).clip(0, 127);
					}
					{dragMode == 'dragEndTime';} {
						// set end time & play time
						holdTrack.arrEditNotes[0][4] = (holdTrack.arrEditNotes[0][4] + deltaX).max(0);
						holdTrack.arrEditNotes[0][3] = (holdTrack.arrEditNotes[0][4] - holdTrack.arrEditNotes[0][0]).max(0.01);
					}
					{dragMode == 'dragStartEndTimePitch';} {
						// set start time, end time & noteNum
						holdTrack.arrEditNotes[0][0] = (holdTrack.arrEditNotes[0][0] + deltaX).max(0);
						holdTrack.arrEditNotes[0][4] = (holdTrack.arrEditNotes[0][4] + deltaX).max(0);
						holdTrack.arrEditNotes[0][1] = (120 - scaledY).floor;
					}
					{dragMode == 'dragAddNote';} {
						// set end time & play time
						newNoteData[4] = (newNoteData[4] + deltaX).max(0);
						newNoteData[3] = (newNoteData[4] - newNoteData[0]).max(0.01);
						newNoteData[2] = (newNoteData[2] - (deltaY * 10)).clip(0, 127);
					}
					;
				});
				if (viewVelocityEvents.notNil, {viewVelocityEvents.refresh;});
			};
			viewEditEvents.mouseUpAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var holdLo, holdHi;
				if (dragMode == 'dragAddNote', {
					// save history with edit
					holdModule.toolFuncs.doWithHistory(holdTrack, {
						// adjust start/end
						if (newNoteData[4] < newNoteData[0], {
							holdLo = newNoteData[4];
							holdHi = newNoteData[0];
							newNoteData = [holdLo, newNoteData[1], newNoteData[2], (holdHi - holdLo).max(0.01), holdHi];
						});
						// add newNoteData to arrEditNotes
						holdTrack.arrEditNotes = [newNoteData.deepCopy];
						// snap edits
						holdModule.toolFuncs.snapEditNotesToGrid(holdTrack);
						// update selection
						holdModule.toolFuncs.setSelectionFromEditNotes(holdTrack);
						// merge & clear edits
						holdModule.toolFuncs.mergeNewNoteOnOffs(holdTrack, holdTrack.arrEditNotes);
						holdTrack.selectionIndices = [holdTrack.arrNotes.indexOfEqual(holdTrack.arrEditNotes[0]) ? 0];
					});
				},{
					// else add save update
					if (holdTrack.arrEditNotes.size > 0, {
						// snap edits
						holdModule.toolFuncs.snapEditNotesToGrid(holdTrack);
						// update selection
						holdModule.toolFuncs.setSelectionFromEditNotes(holdTrack);
						// merge edits
						holdModule.toolFuncs.mergeEditNotes(holdTrack);
						// save history after edit
						holdModule.toolFuncs.storePostEditHistory(holdTrack);
						viewTrackEvents.refresh; // refresh other view
						viewSelectionBar.refresh;
					});
				});
				// reset drag mode
				dragMode = nil;
				// refresh other views
				viewTrackEvents.refresh;
				viewSelectionBar.refresh;
				this.updateSelectionText;
				this.updateSelectedTotalText;
				if (viewVelocityEvents.notNil, {viewVelocityEvents.refresh;});
			};
			viewEditEvents.drawFunc = {arg view;
				var fixWidth = view.pixelScale.x;
				var fixWidth2 = fixWidth * 2;
				var fixHeight = view.pixelScale.y;
				var drawEvents, boxX, boxY, boxW, boxH;
				var holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
				var holdDisplayRangeStart = (holdModule.getSynthArgSpec("displayRangeStart")-1) * holdBeatsPerBar;
				var holdDisplayRangeEnd = (holdModule.getSynthArgSpec("displayRangeEnd")-1) * holdBeatsPerBar;
				var holdSelectionIndices;
				if (dragMode == 'dragAddNote', {
					drawEvents = [newNoteData];
				}, {
					holdSelectionIndices = holdTrack.selectionIndices ? [];
					holdSelectionIndices = holdSelectionIndices.select({arg item; item.notNil});
					drawEvents = holdTrack.arrNotes.at(holdSelectionIndices);
					// draw yellow selection box
					boxX = holdTrack.selectionStart;
					boxY = 0;
					boxW = (holdTrack.selectionEnd - holdTrack.selectionStart);
					boxH = 120;
					Pen.strokeColor = Color.yellow.alpha_(0.8);
					Pen.fillColor = Color.yellow.alpha_(0.2);
					Pen.width = fixWidth;
					Pen.addRect(Rect(boxX, boxY, boxW, boxH));
					Pen.fillStroke;
				});
				// draw events
				drawEvents.do({arg holdNoteEvent, i;
					var startTime, endTime, playTime, noteVal, vel;
					startTime = holdNoteEvent[0];
					noteVal = holdNoteEvent[1];
					vel = holdNoteEvent[2]/127;
					playTime =  holdNoteEvent[3];
					endTime = holdNoteEvent[4];
					if ((endTime > holdDisplayRangeStart) && (startTime < holdDisplayRangeEnd), {
						Pen.width = fixWidth;
						if (vel < 0.5, {
							Pen.fillColor = TXColor.green(0.5).blend(TXColor.yellow2, vel * 2).blend(TXColor.white, 0.5);
						},{
							Pen.fillColor = TXColor.yellow2.blend(TXColor.red, (vel * 2) - 1).blend(TXColor.white, 0.5);
						});
						Pen.fillRect(Rect(startTime, 119-noteVal, playTime, 1));
						Pen.fillColor = Color.black;
						Pen.fillRect(Rect(startTime-fixWidth, 119-noteVal, fixWidth2, 1));
						Pen.fillColor = TXColor.red;
						Pen.fillRect(Rect(endTime-fixWidth, 119-noteVal, fixWidth2, 1));
					});
				});

			};
		});  // end of if (holdTrack.displayMode == 'drawNotes', {

		if (holdTrack.displayMode == 'editNotes', {
			viewEditEvents.mouseDownAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var holdTime, holdNote, holdSearchIndex, holdNoteEventData;
				this.updateMouseTextTimeNote(scaledX, scaledY);
				oldScaledX = scaledX;
				oldScaledY = scaledY;
				holdTime = scaledX;
				holdNote = (120 - scaledY).floor;
				dragMode = nil;
				if (holdTrack.selectionIndices.asArray.size > 0, {
					holdSearchIndex = 0;
					dragIndex = nil;
					while ({ //testFunc
						dragIndex.isNil && (holdSearchIndex < holdTrack.arrEditNotes.size);
					}, { //bodyFunc
						holdNoteEventData = holdTrack.arrEditNotes[holdSearchIndex];
						// if noteNum matches
						if (holdNoteEventData[1] == holdNote, {
							// if mouse is over note start
							if (abs(holdNoteEventData[0] - holdTime) < 0.05, {
								dragMode = 'dragStartEndTimeVel';
								dragIndex = holdSearchIndex;
								holdModule.toolFuncs.storePreEditHistory(holdTrack);
							}, {
								// else if mouse is over note end
								if (abs(holdNoteEventData[4] - holdTime) < 0.05, {
									dragMode = 'dragEndTime';
									dragIndex = holdSearchIndex;
									holdModule.toolFuncs.storePreEditHistory(holdTrack);
								}, {
									// else if mouse is over note box
									if ((holdTime > holdNoteEventData[0]) && (holdTime < holdNoteEventData[4]), {
										dragMode = 'dragStartEndTimePitch';
										dragIndex = holdSearchIndex;
										holdModule.toolFuncs.storePreEditHistory(holdTrack);
									});
								});
							});
						});
						// increment
						holdSearchIndex = holdSearchIndex + 1;
					});
				});
				// if not set, mode is dragSelectBox
				if (dragMode.isNil, {
					dragMode = 'dragSelectBox';
					selectionBoxStartX  = scaledX;
					selectionBoxStartY  = scaledY;
					selectionBoxEndX  = scaledX;
					selectionBoxEndY  = scaledY;
				});
			};
			viewEditEvents.mouseMoveAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var deltaX, deltaY, holdNoteEventData, scaleLength, scaleVel, deltaPitch;
				deltaX = scaledX - oldScaledX;
				deltaY = scaledY - oldScaledY;
				oldScaledX = scaledX;
				oldScaledY = scaledY;
				// only velocity can be dragged outside the view
				if (dragMode == 'dragStartEndTimeVel', {
					holdNoteEventData = holdTrack.arrEditNotes[dragIndex];
					scaleVel = (holdNoteEventData[2] - (deltaY * 10)).clip(0, 127) / holdNoteEventData[2];
					holdModule.toolFuncs.modifyEditNotesVel(holdTrack, scaleVel);
				});
				if (isInside, {
					this.updateMouseTextTimeNote(scaledX, scaledY);
					case {dragMode == 'dragStartEndTimeVel';} {
						// set start / end time
						holdModule.toolFuncs.modifyEditNotesStartEnd(holdTrack, deltaX);
					}
					{dragMode == 'dragEndTime';} {
						// set end time & play time
						holdNoteEventData = holdTrack.arrEditNotes[dragIndex];
						scaleLength = (holdNoteEventData[3] + deltaX).max(0.01) / holdNoteEventData[3];
						holdModule.toolFuncs.modifyEditNotesLength(holdTrack, scaleLength);
					}
					{dragMode == 'dragStartEndTimePitch';} {
						// set start time, end time & noteNum
						holdNoteEventData = holdTrack.arrEditNotes[dragIndex];
						deltaPitch = (120 - scaledY).floor - holdNoteEventData[1];
						holdModule.toolFuncs.modifyEditNotesStartEndPitch(holdTrack, deltaX, deltaPitch);
					}
					{dragMode == 'dragSelectBox';} {
						// set end time & note
						selectionBoxEndX  = scaledX;
						selectionBoxEndY  = scaledY;
					}
					;
				});
				if (viewVelocityEvents.notNil, {viewVelocityEvents.refresh;});
				this.updateSingleEventParameters;
			};
			viewEditEvents.mouseUpAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var newSelectionIndices, scaledXLo, scaledXHi, scaledYLo, scaledYHi, holdSet;
				if (dragMode == 'dragSelectBox', {
					newSelectionIndices = [];
					scaledXLo = min(selectionBoxStartX,selectionBoxEndX);
					scaledXHi = max(selectionBoxStartX,selectionBoxEndX);
					scaledYLo = min(selectionBoxStartY,selectionBoxEndY);
					scaledYHi = max(selectionBoxStartY,selectionBoxEndY);
					holdTrack.selectionStart = scaledXLo;
					holdTrack.selectionEnd = scaledXHi;
					holdTrack.arrNotes.do({arg noteData, i;
						var rect1, rect2;
						// select events that fall into selection box
						// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
						if ((noteData[1] <= ((120 - scaledYLo).floor)) && (noteData[1] >= ((120 - scaledYHi).floor))
							&& (noteData[0] < scaledXHi) && (noteData[4] > scaledXLo), {
								newSelectionIndices = newSelectionIndices.add(i);
						});
					});
					// if shift pressed then add to current selection, else replace
					if (mod == 131072, {
						holdSet = newSelectionIndices.asSet;
						holdSet = holdSet.addAll(holdTrack.selectionIndices);
						holdTrack.selectionIndices = holdSet.asArray.sort;
					},{
						holdTrack.selectionIndices = newSelectionIndices;
					});
					holdModule.toolFuncs.rebuildEditNotesFromSelectionIndices(holdTrack);
					// update selection
					holdModule.toolFuncs.adjustSelectionToEditNotes(holdTrack);
				}, {
					// if not dragSelectBox, save updates
					if ((dragMode == 'dragStartEndTimeVel')
						|| (dragMode == 'dragEndTime')
						|| (dragMode == 'dragStartEndTimePitch') , {
							if (holdTrack.arrEditNotes.size > 0, {
								// snap edits
								holdModule.toolFuncs.snapEditNotesToGrid(holdTrack);
								// update selection
								holdModule.toolFuncs.adjustSelectionToEditNotes(holdTrack);
								// merge edits
								holdModule.toolFuncs.mergeEditNotes(holdTrack);
								// save history after edit
								holdModule.toolFuncs.storePostEditHistory(holdTrack);
								viewTrackEvents.refresh; // refresh other view
								viewSelectionBar.refresh;
							});
					});
				});
				// reset vars
				dragMode = nil;
				selectionBoxStartX = -1;
				selectionBoxStartY = -1;
				selectionBoxEndX = -1;
				selectionBoxEndY = -1;
				// refresh other views
				this.updateSelectionText;
				this.updateSelectedTotalText;
				if (viewVelocityEvents.notNil, {viewVelocityEvents.refresh;});
				viewSelectionBar.refresh;
			};
			viewEditEvents.drawFunc = {arg view;
				var fixWidth = view.pixelScale.x;
				var fixWidth2 = fixWidth * 2;
				var fixHeight = view.pixelScale.y;
				var boxX, boxY, boxW, boxH;
				var holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
				var holdDisplayRangeStart = (holdModule.getSynthArgSpec("displayRangeStart")-1) * holdBeatsPerBar;
				var holdDisplayRangeEnd = (holdModule.getSynthArgSpec("displayRangeEnd")-1) * holdBeatsPerBar;
				var holdSelectionIndices;
				if (dragMode == 'dragSelectBox', {
					boxX = selectionBoxStartX;
					boxY = selectionBoxStartY;
					boxW = (selectionBoxEndX - selectionBoxStartX);
					boxH = (selectionBoxEndY - selectionBoxStartY);
					Pen.strokeColor = TXColor.orange;
				},{
					boxX = holdTrack.selectionStart;
					boxY = 0;
					boxW = (holdTrack.selectionEnd - holdTrack.selectionStart);
					boxH = 120;
					Pen.strokeColor = Color.yellow.alpha_(0.8);
				});
				// draw yellow selection box
				Pen.fillColor = Color.yellow.alpha_(0.2);
				Pen.width = fixWidth;
				Pen.addRect(Rect(boxX, boxY, boxW, boxH));
				Pen.fillStroke;
				holdSelectionIndices = holdTrack.selectionIndices ? [];
				holdSelectionIndices = holdSelectionIndices.select({arg item; item.notNil});
				// draw all selected note events
				holdTrack.arrNotes.at(holdSelectionIndices).do({arg holdNoteEvent, i;
					var startTime, endTime, playTime, noteVal, vel;
					startTime = holdNoteEvent[0];
					noteVal = holdNoteEvent[1];
					vel = holdNoteEvent[2]/127;
					playTime = max(holdNoteEvent[3], 0.01);
					endTime = startTime + playTime;
					if ((endTime > holdDisplayRangeStart) && (startTime < holdDisplayRangeEnd), {
						Pen.width = fixWidth;
						if (vel < 0.5, {
							Pen.fillColor = TXColor.green(0.5).blend(TXColor.yellow2, vel * 2).blend(TXColor.white, 0.5);
						},{
							Pen.fillColor = TXColor.yellow2.blend(TXColor.red, (vel * 2) - 1).blend(TXColor.white, 0.5);
						});
						Pen.fillRect(Rect(startTime, 119-noteVal, playTime, 1));
						Pen.fillColor = Color.black;
						Pen.fillRect(Rect(startTime-fixWidth, 119-noteVal, fixWidth2, 1));
						Pen.fillColor = TXColor.red;
						Pen.fillRect(Rect(endTime-fixWidth, 119-noteVal, fixWidth2, 1));
					});
				});

			};
		});  // end of if (holdTrack.displayMode == 'editNotes', {

		// Slider
		midiKeybScrollSlider = Slider.new(holdParent, Rect(0, 0, 20, holdNoteViewHeight));
		midiKeybScrollSlider.background = TXColor.sysGuiCol1;
		midiKeybScrollSlider.action = {arg view;
			var holdVal = 1 - view.value;
			holdTrack.midiKeybScroll = holdVal;
			viewTrackEvents.moveV = holdVal;
			viewEditEvents.moveV = holdVal;
			scrollView.visibleOrigin = Point.new(0, holdVal * (900 - holdNoteViewHeight));
		};
		// defer
		{
			midiKeybScrollSlider.valueAction = 1 - holdTrack.midiKeybScroll;
			// scrollView.action = {arg view;
			// 	var holdVal = view.visibleOrigin * (1 / (900 - holdNoteViewHeight));
			// 	holdTrack.midiKeybScroll = holdVal;
			// 	//viewTrackEvents.moveV = holdVal;
			// 	midiKeybScrollSlider.value = 1 - holdVal;
			// };

		}.defer(0.1);

		if (holdTrack.showVelocityTrack == 1, {
			this.createViewVelocityEvents;
		});

		holdParent.decorator.nextLine;

		// store decorator position
		holdDecLeft = holdParent.decorator.left;
		holdDecTop = holdParent.decorator.top;

		mouseTextView = StaticText(holdParent, 410 @ 20);
		mouseTextView.string = "Mouse: ...";
		mouseTextView.stringColor_(TXColor.sysGuiCol2).background_(TXColor.paleYellow);
		mouseTextView.align_(\left);

		// singleEventParameters
		if (holdTrack.displayMode == 'editNotes', {
			// restore decorator position
			//holdParent.decorator.reset;
			holdParent.decorator.left = holdDecLeft;
			holdParent.decorator.top = holdDecTop;

			singleEventContainer = UserView(holdParent, 410 @ 20).background_(TXColor.paleYellow);
			singleEventContainer.addFlowLayout.gap_(0@0).margin_(0@0).reset;

			holdPopupStrings = 128.collect({arg item; TXGetMidiNoteString(item);});
			singleNoteMidiNoteView = TXPopup(singleEventContainer, 100 @ 20, "Note", holdPopupStrings, {|view|
				holdModule.toolFuncs.setSingleEditNoteNum(holdTrack, view.value);
				system.showView;
			},
			// initial value
			0, false, 40);
			singleNoteMidiNoteView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

			singleNoteOnBarsView = TXNumber(singleEventContainer, 70 @ 20, "Start", ControlSpec(1, 10000, step: 1),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
					+ (singleNoteOnBeatsView.value - 1);
					holdModule.toolFuncs.setSingleEditNoteOn(holdTrack, totalBeats);
					system.showView;
				},
				// initial value
				1,
				false, 34, 34
			);
			singleNoteOnBarsView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

			singleNoteOnBeatsView = TXNumber(singleEventContainer, 50 @ 20, ": ",
				ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (singleNoteOnBarsView.value - 1)) + (view.value - 1);
					holdModule.toolFuncs.setSingleEditNoteOn(holdTrack, totalBeats);
					system.showView;
				},
				// initial value
				1 ,
				false, 10, 34
			);
			singleNoteOnBeatsView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

			singleNoteOffBarsView = TXNumber(singleEventContainer, 66 @ 20, "End", ControlSpec(1, 10000, step: 1),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
					+ (singleNoteOffBeatsView.value - 1);
					holdModule.toolFuncs.setSingleEditNoteOff(holdTrack, totalBeats);
					system.showView;
				},
				// initial value
				1,
				false, 30, 34
			);
			singleNoteOffBarsView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

			singleNoteOffBeatsView = TXNumber(singleEventContainer, 50 @ 20, ": ",
				ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
				{|view|
					var totalBeats;
					totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (singleNoteOffBarsView.value - 1)) + (view.value - 1);
					holdModule.toolFuncs.setSingleEditNoteOff(holdTrack, totalBeats);
					system.showView;
				},
				// initial value
				1 ,
				false, 10, 34
			);
			singleNoteOffBeatsView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);


			singleNoteVelView = TXNumber(singleEventContainer, 74 @ 20, "Vel",
				ControlSpec(0, 100),
				{|view|
					var holdVel;
					holdVel = view.value;
					holdModule.toolFuncs.setSingleEditNoteVel(holdTrack, holdVel * 1.27);
					system.showView;
				},
				// initial value
				1 ,
				false, 30, 34
			);
			singleNoteVelView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

			this.updateSingleEventParameters;

		}); // end of if (holdTrack.displayMode == 'editSelection', {


		holdParent.decorator.shift(20,0);

		// Undo Edit btn
		Button(holdParent, 68 @ 20)
		.states_([
			["< Undo Edit", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			holdModule.toolFuncs.undoEdit(holdTrack);
		});

		// Redo Edit btn
		Button(holdParent, 68 @ 20)
		.states_([
			["> Redo Edit", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			holdModule.toolFuncs.redoEdit(holdTrack);
		});

		holdParent.decorator.shift(20, 0);

		// Select None btn
		Button(holdParent, 74 @ 20)
		.states_([
			["Select None", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			holdModule.toolFuncs.unselectAllNotes(holdTrack);
			system.showView;
		});

		// Select All btn
		Button(holdParent, 64 @ 20)
		.states_([
			["Select All", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			holdTrack.selectionStart = 0;
			holdTrack.selectionEnd = holdModule.getSynthArgSpec("maxDisplayBars") * holdModule.getSynthArgSpec("beatsPerBar");
			holdModule.toolFuncs.selectAllNotes(holdTrack);
			system.showView;
		});

		// Zoom to Selection  btn
		Button(holdParent, 110 @ 20)
		.states_([
			["Zoom to Selection", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			var holdStart = holdTrack.selectionStart;
			var holdEnd = holdTrack.selectionEnd;
			if ( (holdEnd - holdStart) < 0.25, {
				holdEnd = holdStart + 0.25;
			});
			holdStart = holdStart /  holdModule.getSynthArgSpec("beatsPerBar");
			holdEnd = min((holdEnd  + 0.5) /  holdModule.getSynthArgSpec("beatsPerBar"), holdModule.getSynthArgSpec("maxDisplayBars"));
			holdModule.setSynthArgSpec("displayRangeStart", holdStart + 1);
			holdModule.setSynthArgSpec("displayRangeEnd", holdEnd + 1);
			system.showView;
		});

		if (holdTrack.displayMode == 'editNotes', {
			holdParent.decorator.shift(20, 0);

			// Copy btn
			Button(holdParent, 40 @ 20)
			.states_([
				["Copy", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				holdModule.toolFuncs.makeCopyNoteData(holdTrack);
				system.showView;
			});

			// Deleted  btn
			Button(holdParent, 50 @ 20)
			.states_([
				["Delete", TXColor.white, TXColor.sysDeleteCol]
			])
			.action_({|view|
				// save history with edit
				holdModule.toolFuncs.doWithHistory(holdTrack, {
					holdModule.toolFuncs.deleteSelectedNoteVals(holdTrack);
				});
				system.showView;
			});

			totalTextView = StaticText(holdParent, 94 @ 20)
			.string_("selected " ++ holdTrack.selectionIndices.size.asString)
			.align_(\center)
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);

			holdParent.decorator.nextLine;

			holdTrack.processIndex = holdTrack.processIndex ? 0;
			holdTrack.processVar1 = holdTrack.processVar1 ? 1;
			holdTrack.processVar2 = holdTrack.processVar2 ? 1;

			// draw white line
			StaticText(holdParent, 1070 @ 1).background_(TXColor.white);
			holdParent.decorator.nextLine;

			// Show Processes btn
			btnShowProcesses = Button(holdParent, 17 @ 15)
			.states_([
				["^", TXColor.white, TXColor.sysGuiCol2],
				["v", TXColor.white, TXColor.sysGuiCol2]
			])
			.value_(holdTrack.showProcesses)
			.action_({|view|
				var keepShowVel = holdTrack.showVelocityTrack;
				holdTrack.showProcesses = view.value;
				if (view.value == 0, {
					if (holdTrack.previousShowVelocity == 1, {
						holdTrack.showVelocityTrack = 1;
					});
				}, {
					holdTrack.showVelocityTrack = 0;
				});
				holdTrack.previousShowVelocity = keepShowVel;
				system.showView;
			});

			// Processes
			arrProcessStrings = holdModule.toolFuncs.getNoteProcessStrings;
			if (holdTrack.showProcesses == 1, {
				processSelectView = TXListViewLabelPlusMinus(holdParent, 446 @ 120, "Process", arrProcessStrings, {arg view;
					holdModule.toolFuncs.setNoteProcessIndex(holdTrack, view.value);
					system.showView;
				}, holdTrack.processIndex, false, 50);
			}, {
				processSelectView = TXPopupPlusMinus(holdParent, 446 @ 20, "Process", arrProcessStrings, {arg view;
					holdModule.toolFuncs.setNoteProcessIndex(holdTrack, view.value);
					system.showView;
				}, holdTrack.processIndex, false, 50);
				processSelectView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
			});
			processSelectView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);

			// Run Process btn
			Button(holdParent, 80 @ 20)
			.states_([
				["Run Process", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				// save history with edit
				holdModule.toolFuncs.doWithHistory(holdTrack, {
					holdModule.toolFuncs.runNoteProcess(holdTrack, holdTrack.processIndex);
				});
				system.showView;
			});

			// add sliders if relevent, based on presence of ControlSpecs
			holdProcess = holdModule.toolFuncs.getNoteProcesses[holdTrack.processIndex];
			if (holdProcess[2][0].notNil, {
				holdParent.decorator.shift(12, 0);
				holdView = TXSlider(holdParent, 240 @ 20, holdProcess[2][0], holdProcess[3][0],
					{arg view; holdTrack.processVar1 = view.value}, holdTrack.processVar1, false, 90, 40);
				holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
				holdView.sliderView.background_(TXColor.sysGuiCol1);
			});
			if (holdProcess[2][1].notNil, {
				holdParent.decorator.shift(12, 0);
				// variable 2
				holdView = TXSlider(holdParent, 240 @ 20, holdProcess[2][1], holdProcess[3][1],
					{arg view; holdTrack.processVar2 = view.value}, holdTrack.processVar2, false, 90, 40);
				holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
				holdView.sliderView.background_(TXColor.sysGuiCol1);
			});
		}); // end of if (holdTrack.displayMode == 'editNotes',

	}  // end of drawNoteGui

	createViewVelocityEvents {
		holdParent.decorator.nextLine.shift(80,0);

		// velocity events
		holdMaxDisplayBeats = holdModule.getSynthArgSpec("maxDisplayBars") * holdModule.getSynthArgSpec("beatsPerBar");
		viewVelocityEvents = ScaledUserView( holdParent, 966 @ 96, Rect(0,0,holdMaxDisplayBeats, 127) );
		viewVelocityEvents.background =(TXColor.white);
		//viewVelocityEvents.view.resize = 5;
		viewVelocityEvents.scaleV = 1;
		//viewVelocityEvents.moveV = 0;
		viewVelocityEvents.gridMode = [\lines, \lines];
		viewVelocityEvents.gridLines = [ holdModule.getSynthArgSpec("maxDisplayBars"), 0 ];  // no lines for H & V
		viewVelocityEvents.refresh;
		if (holdTrack.displayMode == 'drawNotes', {
			viewVelocityEvents.mouseDownAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var holdTime, holdVel, holdNoteEventData;
				this.updateMouseTextTimeVel(scaledX, scaledY);
				// reset mode
				dragMode = nil;
				holdModule.toolFuncs.resetSelection(holdTrack);
				if (holdTrack.selectionIndices.asArray.size > 0, {
					holdNoteEventData = holdTrack.arrNotes[holdTrack.selectionIndices[0]];
					// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
					// if time & velocity roughly matches
					if ((((holdNoteEventData[0] - scaledX).abs / view.pixelScale.x) < 3)
						&& ((holdNoteEventData[2] - (127 - scaledY) < 5)), {
							dragMode = 'dragVelocity';
							holdModule.toolFuncs.storePreEditHistory(holdTrack);
					});
				});
			};
			viewVelocityEvents.mouseMoveAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var holdNoteEventData, scaleVel;
				if (isInside, {
					this.updateMouseTextTimeVel(scaledX, scaledY);
					if (dragMode == 'dragVelocity', {
						holdTrack.arrEditNotes[0].put(2, (127 - scaledY));
						viewEditEvents.refresh;
					});
				});
			};
			viewVelocityEvents.mouseUpAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var holdDict;
				holdDict = ();
				if (holdTrack.arrEditNotes.size > 0, {
					// set Selection start/end
					holdModule.toolFuncs.setSelectionFromEditControllerVals(holdTrack);
					// merge  edits
					holdModule.toolFuncs.mergeEditNotes(holdTrack);
					// save history after edit
					holdModule.toolFuncs.storePostEditHistory(holdTrack);
					// refresh other views
					viewTrackEvents.refresh;
					viewEditEvents.refresh;
					viewSelectionBar.refresh;
				});
			};
		});
		if (holdTrack.displayMode == 'editNotes', {
			viewVelocityEvents.mouseDownAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var holdTime, holdVel, holdSearchIndex, holdNoteEventData;
				this.updateMouseTextTimeVel(scaledX, scaledY);
				oldScaledX = scaledX;
				oldScaledY = scaledY;
				holdTime = scaledX;
				holdVel = 127 - scaledY;
				dragMode = nil;
				if (holdTrack.selectionIndices.asArray.size > 0, {
					holdSearchIndex = 0;
					dragIndex = nil;
					while ({ //testFunc
						dragIndex.isNil && (holdSearchIndex < holdTrack.arrEditNotes.size);
					}, { //bodyFunc
						holdNoteEventData = holdTrack.arrEditNotes[holdSearchIndex];
						// if time & velocity roughly matches
						if ((((holdNoteEventData[0] - scaledX).abs / view.pixelScale.x) < 3)
							&& ((holdNoteEventData[2] - (127 - scaledY) < 5)), {
								dragIndex = holdSearchIndex;
								dragMode = 'dragVelocity';
								holdModule.toolFuncs.storePreEditHistory(holdTrack);
						});
						// increment
						holdSearchIndex = holdSearchIndex + 1;
					});
				});
				if ((mod != 131072) && (dragMode != 'dragVelocity'), {
					holdModule.toolFuncs.resetSelection(holdTrack);
				});
				// if not set, mode is dragSelectBox
				if (dragMode.isNil, {
					dragMode = 'dragSelectVelocityBox';
					selectionBoxStartX  = scaledX;
					selectionBoxStartY  = scaledY;
					selectionBoxEndX  = scaledX;
					selectionBoxEndY  = scaledY;
				});
			};
			viewVelocityEvents.mouseMoveAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var holdVel, scaleVel;
				if (isInside, {
					this.updateMouseTextTimeVel(scaledX, scaledY);
				}, {
					scaledX = scaledX.clip(viewVelocityEvents.viewRect.left,
						viewVelocityEvents.viewRect.left+viewVelocityEvents.viewRect.width);
				});
				case {dragMode == 'dragVelocity';} {
					holdVel = holdTrack.arrEditNotes[dragIndex][2];
					scaleVel =  (127 - scaledY.clip(0, 127)) / holdVel.max(1);  // max(1) prevents divide by zero
					holdModule.toolFuncs.modifyEditNotesVel(holdTrack, scaleVel);
					viewEditEvents.refresh;
					this.updateSingleEventParameters;
				}
				{dragMode == 'dragSelectVelocityBox';} {
					// set end time & note
					selectionBoxEndX  = scaledX;
					selectionBoxEndY  = scaledY.clip(0, 127);
				}
				;
			};
			viewVelocityEvents.mouseUpAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
				var newSelectionIndices, scaledXLo, scaledXHi, scaledYLo, scaledYHi, holdSet;
				if (dragMode == 'dragSelectVelocityBox', {
					newSelectionIndices = [];
					scaledXLo = min(selectionBoxStartX,selectionBoxEndX);
					scaledXHi = max(selectionBoxStartX,selectionBoxEndX);
					scaledYLo = min(selectionBoxStartY,selectionBoxEndY);
					scaledYHi = max(selectionBoxStartY,selectionBoxEndY);
					holdTrack.arrNotes.do({arg noteData, i;
						var rect1, rect2;
						// select events that fall into selection box
						// NOTE EVENT FORMAT[startTime, noteNum, vel, noteLength, endTime]
						if ((noteData[2] <= (127 - scaledYLo)) && (noteData[2] >= (127 - scaledYHi))
							&& (noteData[0] <= scaledXHi) && (noteData[0] >= scaledXLo), {
								newSelectionIndices = newSelectionIndices.add(i);
						});
					});
					// if shift pressed then add to current selection, else replace
					if (mod == 131072, {
						holdSet = newSelectionIndices.asSet;
						holdSet = holdSet.addAll(holdTrack.selectionIndices);
						holdTrack.selectionIndices = holdSet.asArray.sort;
					},{
						holdTrack.selectionIndices = newSelectionIndices;
					});
					holdModule.toolFuncs.rebuildEditNotesFromSelectionIndices(holdTrack);
					if (mod == 131072, {
						// adjust selection
						holdModule.toolFuncs.adjustSelectionToEditNotes(holdTrack);
					},{
						// set selection
						holdModule.toolFuncs.setSelectionFromEditNotes(holdTrack);
					});
				}, {
					// else if dragVelocity save updates
					if ((dragMode == 'dragVelocity'), {
						if (holdTrack.arrEditNotes.size > 0, {
							// update selection
							holdModule.toolFuncs.adjustSelectionToEditNotes(holdTrack);
							// merge  edits
							holdModule.toolFuncs.mergeEditNotes(holdTrack);
							// save history after edit
							holdModule.toolFuncs.storePostEditHistory(holdTrack);
						});
					});
				});
				// refresh other views
				viewTrackEvents.refresh;
				viewEditEvents.refresh;
				viewSelectionBar.refresh;
				this.updateSelectionText;
				this.updateSelectedTotalText;
				// reset vars
				dragMode = nil;
				selectionBoxStartX = -1;
				selectionBoxStartY = -1;
				selectionBoxEndX = -1;
				selectionBoxEndY = -1;
			};
		});
		viewVelocityEvents.drawFunc = {arg view;
			var fixWidth = view.pixelScale.x;
			var fixWidth2 = fixWidth * 2;
			var fixHeight = view.pixelScale.y;
			var boxX, boxY, boxW, boxH, drawEvents;
			var holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
			var holdDisplayRangeStart = (holdModule.getSynthArgSpec("displayRangeStart")-1) * holdBeatsPerBar;
			var holdDisplayRangeEnd = (holdModule.getSynthArgSpec("displayRangeEnd")-1) * holdBeatsPerBar;
			var holdSelectionIndices;
			// draw yellow selection box
			if (dragMode == 'dragSelectVelocityBox', {
				boxX = selectionBoxStartX;
				boxY = selectionBoxStartY;
				boxW = (selectionBoxEndX - selectionBoxStartX);
				boxH = (selectionBoxEndY - selectionBoxStartY);
				Pen.strokeColor = TXColor.orange;
			},{
				boxX = holdTrack.selectionStart;
				boxY = 0;
				boxW = (holdTrack.selectionEnd - holdTrack.selectionStart);
				boxH = 127;
				Pen.strokeColor = Color.yellow.alpha_(0.8);
			});
			Pen.fillColor = Color.yellow.alpha_(0.2);
			Pen.width = fixWidth;
			Pen.addRect(Rect(boxX, boxY, boxW, boxH));
			Pen.fillStroke;
			// draw  current play time box
			Pen.fillColor = Color.red.alpha_(0.75);
			Pen.addRect(Rect(holdModule.currentPlayTime - fixWidth, 0, fixWidth2, 127 ));
			Pen.fill;
			// draw all Velocity events
			holdTrack.arrNotes.do({arg holdNoteEvent, i;
				var startTime, vel, mix;
				startTime = holdNoteEvent[0];
				if ((startTime >= holdDisplayRangeStart) && (startTime < holdDisplayRangeEnd), {
					Pen.width = fixWidth2;
					vel = holdNoteEvent[2];
					if (vel < 63.5, {
						mix = vel * (2 / 127);
						Pen.color = TXColor.green.blend(TXColor.yellow2, mix).alpha_(0.5);
					},{
						mix = (vel-63.5) * (2 / 127);
						Pen.color = TXColor.yellow2.blend(TXColor.red, mix).alpha_(0.5);
					});
					Pen.line(startTime@(127 - vel), startTime@127).stroke;
					Pen.addOval(Rect.aboutPoint(startTime@(127 - vel), fixWidth * 3, fixHeight * 3)).fill;
				});
			});
			if (dragMode == 'dragAddNote', {
				drawEvents = [newNoteData];
			}, {
				holdSelectionIndices = holdTrack.selectionIndices ? [];
				holdSelectionIndices = holdSelectionIndices.select({arg item; item.notNil});
				drawEvents = holdTrack.arrNotes.at(holdSelectionIndices);
			});
			// draw selected events
			drawEvents.do({arg holdNoteEvent, i;
				var startTime, vel, mix;
				startTime = holdNoteEvent[0];
				if ((startTime >= holdDisplayRangeStart) && (startTime < holdDisplayRangeEnd), {
					Pen.width = fixWidth2;
					vel = holdNoteEvent[2];
					if (vel < 63.5, {
						mix = vel * (2 / 127);
						Pen.color = TXColor.green.blend(TXColor.yellow2, mix).blend(TXColor.white, 0.5).alpha_(0.5);
					},{
						mix = (vel-63.5) * (2 / 127);
						Pen.color = TXColor.yellow2.blend(TXColor.red, mix).blend(TXColor.white, 0.5).alpha_(0.5);
					});
					Pen.line(startTime@(127 - vel), startTime@127).stroke;
					Pen.addOval(Rect.aboutPoint(startTime@(127 - vel), fixWidth * 3, fixHeight * 3)).fill;
					Pen.fillColor = Color.black;
					Pen.fillRect(Rect(startTime-fixWidth, 125 - vel, fixWidth2, 4));
				});
			});
		};
	}  // end of createViewVelocityEvents

	////////////////////////////////////

	drawSelectionBar {arg viewWidth, selectionChangeAction;
		// Bar text behind selection bar
		barTextView = UserView( holdParent, viewWidth @ 20);
		barTextView.background = TXColor.white;
		barTextView.drawFunc = {arg view;
			var holdPosX, holdLoopPosX;
			if (viewSelectionBar.notNil, {
				Pen.font = Font.defaultSansFace;
				Pen.color = TXColor.grey;
				holdModule.getSynthArgSpec("maxDisplayBars").floor.do({arg i;
					holdPosX = viewSelectionBar.convertFwd( i * holdModule.getSynthArgSpec("beatsPerBar"), 0 )[0];
					if ((holdPosX >= 0) && (holdPosX < viewWidth), {
						Pen.stringInRect( " " ++ (i + 1).asString, Rect(holdPosX,10,40,20), color: TXColor.grey);
					});
				});
				holdPosX = viewSelectionBar.convertFwd(holdModule.getSynthArgSpec("startPosBeats"), 0 )[0];
				Pen.stringInRect( " Start", Rect(holdPosX,1,60,20), color: TXColor.sysGuiCol1);
				holdPosX = viewSelectionBar.convertFwd(holdModule.getSynthArgSpec("stopPosBeats"), 0 )[0];
				Pen.stringInRect( " Stop", Rect(holdPosX,1,60,20), color: TXColor.sysGuiCol1);
				holdLoopPosX = holdModule.getSynthArgSpec("loopStart");
				holdPosX = viewSelectionBar.convertFwd(holdLoopPosX, 0 )[0];
				if (holdModule.getSynthArgSpec("loop").asBoolean == true, {
					Pen.stringInRect( " Loop", Rect(holdPosX,1,60,20), color: TXColor.green);
				});
				Pen.color = TXColor.black;
				// draw all marker texts
				holdModule.dataBank.arrMarkers.pairsDo({arg argPos, argText;
					var posX, text, holdBeatsPerBar, holdTimeBars, holdTimeSoloBeats;
					posX = viewSelectionBar.convertFwd(argPos, 0)[0];
					if (argText == "", {
						holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
						holdTimeBars = (argPos / holdBeatsPerBar).floor;
						holdTimeSoloBeats = argPos - (holdTimeBars * holdBeatsPerBar);
						argText = (1 + holdTimeBars).asString ++ ":" ++ (1 + holdTimeSoloBeats.round(0.01)).asString;
					});
					Pen.stringInRect(argText, Rect(posX + 4, 1, 100, 20), color: TXColor.black);
				});
			});
		};
		{barTextView.refresh;}.defer(0.1);

		// selection bar
		holdMaxDisplayBeats = holdModule.getSynthArgSpec("maxDisplayBars") * holdModule.getSynthArgSpec("beatsPerBar");
		viewSelectionBar = ScaledUserView( barTextView, viewWidth @ 20, Rect(0,0,holdMaxDisplayBeats, 10), );
		viewSelectionBar.background =(TXColor.white.alpha_(0));
		viewSelectionBar.refresh;
		viewSelectionBar.gridMode = [\lines, \lines];
		viewSelectionBar.gridLines = [ 0, 0 ];  // number of lines for H and V
		viewSelectionBar.gridSpacingH = 0;
		viewSelectionBar.gridSpacingV = 0;
		holdModule.registerCurrentTimeView(viewSelectionBar, clearFirst: true); // register for time updates
		viewSelectionBar.drawFunc = {arg view;
			var fixWidth = view.pixelScale.x;
			var fixWidth2 = fixWidth * 2;
			var fixHeight = view.pixelScale.y;
			var holdStartPos, holdStopPos;
			var holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
			var holdMaxDisplayBeats = holdModule.getSynthArgSpec("maxDisplayBars") * holdBeatsPerBar;
			var holdDisplayRangeStart = (holdModule.getSynthArgSpec("displayRangeStart")-1) * holdBeatsPerBar;
			var holdDisplayRangeEnd = (holdModule.getSynthArgSpec("displayRangeEnd")-1) * holdBeatsPerBar;
			var holdLoopStart, holdLoopEnd, holdLoopBoxHeight;
			// draw snap grid
			(holdMaxDisplayBeats/ holdTrack.snapBeats).floor.do({arg i;
				var posX = i * holdTrack.snapBeats;
				// if inside view then draw
				if ((posX >= holdDisplayRangeStart) && (posX < holdDisplayRangeEnd), {
					Pen.strokeColor = TXColor.grey;
					// if bar or beat draw longer thicker line
					if ((posX % holdBeatsPerBar) == 0, {
						Pen.width = fixWidth2;
						Pen.line(posX@0, posX@10).stroke;
					},{
						if ((posX % 1) == 0, {
							Pen.width = fixWidth;
							Pen.line(posX@0, posX@6).stroke;
						},{
							if (holdTrack.showSnapGrid.asBoolean
								and: {(((holdDisplayRangeEnd - holdDisplayRangeStart) / holdTrack.snapBeats) < 300)}, {
									Pen.width = fixWidth * 0.5;
									Pen.line(posX@0, posX@4).stroke;
							});
						});
					});
				});
			});
			if (holdModule.getSynthArgSpec("loop").asBoolean == true, {
				holdLoopBoxHeight = 5;
			}, {
				holdLoopBoxHeight = 2;
			});
			holdLoopStart = holdModule.getSynthArgSpec("loopStart");
			holdLoopEnd = holdModule.getSynthArgSpec("loopEnd");
			// draw green loop box
			Pen.strokeColor = Color.green.alpha_(0.5);
			Pen.fillColor = Color.green.alpha_(0.2);
			Pen.width = fixWidth;
			Pen.addRect(Rect(holdLoopStart, 0, holdLoopEnd - holdLoopStart, holdLoopBoxHeight));
			Pen.fillStroke;
			// draw green loop start/end
			Pen.fillColor = TXColor.green;
			Pen.addRect(Rect(holdLoopStart - fixWidth, 0, fixWidth2, 5 ));
			Pen.addRect(Rect(holdLoopEnd - fixWidth, 0, fixWidth2, 5 ));
			Pen.fill;
			// draw yellow selection box
			Pen.strokeColor = Color.yellow.alpha_(0.5);
			Pen.fillColor = Color.yellow.alpha_(0.2);
			Pen.width = fixWidth;
			Pen.addRect(Rect(holdTrack.selectionStart, 6, holdTrack.selectionEnd - holdTrack.selectionStart, 10, ));
			Pen.fillStroke;
			// draw orange selection start/end
			Pen.fillColor = TXColor.orange;
			Pen.addRect(Rect(holdTrack.selectionStart - fixWidth, 6, fixWidth2, 10 ));
			Pen.addRect(Rect(holdTrack.selectionEnd - fixWidth, 6, fixWidth2, 10 ));
			Pen.fill;
			// draw  play start pos box
			holdStartPos = holdModule.getSynthArgSpec("startPosBeats");
			holdStopPos = holdModule.getSynthArgSpec("stopPosBeats");
			Pen.fillColor = Color.blue;
			Pen.addRect(Rect(holdStartPos - fixWidth2, 0, fixWidth, 5, ));
			Pen.addRect(Rect(holdStartPos + fixWidth, 0, fixWidth, 5, ));
			Pen.addRect(Rect(holdStartPos + fixWidth, 0, max(0.25, holdStopPos - holdStartPos), fixHeight, ));
			Pen.fill;
			Pen.fillColor = Color.red.alpha_(0.5);
			Pen.addRect(Rect(holdStartPos - fixWidth, 0, fixWidth2, 5, ));
			Pen.fill;
			// draw  stop pos box
			Pen.fillColor = Color.blue;
			Pen.addRect(Rect(holdStopPos - fixWidth2, 0, fixWidth, 5, ));
			Pen.addRect(Rect(holdStopPos + fixWidth, 0, fixWidth, 5, ));
			Pen.fill;
			Pen.fillColor = Color.black.alpha_(0.6);
			Pen.addRect(Rect(holdStopPos - fixWidth, 0, fixWidth2, 5, ));
			Pen.fill;
			// draw all markers
			holdModule.dataBank.arrMarkers.pairsDo({arg time, string;
				// draw  current play time box
				Pen.fillColor = TXColor.black;
				Pen.addRect(Rect(time - fixWidth, 0, fixWidth2, 20 ));
				Pen.fill;
			});
			// draw  current play time box
			holdStartPos = holdModule.currentPlayTime;
			Pen.fillColor = Color.red.alpha_(0.75);
			Pen.addRect(Rect(holdStartPos - fixWidth, 0, fixWidth2, 10 ));
			Pen.fill;
		};
		viewSelectionBar.mouseDownAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
			var startPos = holdModule.getSynthArgSpec("startPosBeats");
			var stopPos = holdModule.getSynthArgSpec("stopPosBeats");
			this.updateMouseTextTime(scaledX);
			dragMode = nil;
			if (mod == 1048576 , {   // command button
				if ((abs(holdModule.getSynthArgSpec("loopStart") - scaledX) < 0.05) && (scaledY < 6), {
					dragMode = 'dragLoopStart';
				},{
					if ((abs(holdModule.getSynthArgSpec("loopEnd") - scaledX) < 0.05) && (scaledY < 6), {
						dragMode = 'dragLoopEnd';
					},{
						holdModule.setSynthArgSpec("loopStart", scaledX);
						holdModule.setSynthArgSpec("loopEnd", scaledX);
						dragMode = 'dragLoopEnd';
					});
				});
				barTextView.refresh;
			},{
				if ((mod == 131072) , {   // shift button
					if ((abs(holdTrack.selectionStart - scaledX) < 0.05) && (scaledY > 5), {
						dragMode = 'dragSelectionStart';
					},{
						if ((abs(holdTrack.selectionEnd - scaledX) < 0.05) && (scaledY > 5), {
							dragMode = 'dragSelectionEnd';
						},{
							holdTrack.selectionStart = scaledX;
							holdTrack.selectionEnd = scaledX;
							dragMode = 'dragSelectionEnd';
						});
					});
					this.updateSelectionText;
				},{
					// check markers
					if (holdModule.dataBank.arrMarkerTimes.asArray.size > 0, {
						nearestX = holdModule.dataBank.arrMarkerTimes.nearestTo(scaledX);
						if ((abs(nearestX - scaledX) < 0.1) , {
							dragMode = 'dragMarkerPos';
							oldDragMarkerPos = nearestX;
							this.updateCurrentPlayTimeViews;
						});
					});
					if (dragMode.isNil, {
						if (((abs(startPos - scaledX) < 0.05) && (scaledY < 6)), {
							dragMode = 'dragStartPos';
							this.updateStartPos(scaledX);
							barTextView.refresh;
						},{
							if (((abs(stopPos - scaledX) < 0.05) && (scaledY < 6)), {
								dragMode = 'dragStopPos';
								this.updateStopPos(scaledX);
								barTextView.refresh;
							},{
								this.updateCurrentPlayTime(scaledX);
								dragMode = 'dragCurrentPlayTime';
							});
							holdModule.refreshScaledUserViews;
						});
					});
				});
			});
		};
		viewSelectionBar.mouseMoveAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
			var holdText;
			if (isInside == false, {
				scaledX = scaledX.clip(viewSelectionBar.viewRect.left,
					viewSelectionBar.viewRect.left+viewSelectionBar.viewRect.width);
			});
			this.updateMouseTextTime(scaledX);
			case  {dragMode == 'dragMarkerPos'}   {
				holdText = holdModule.dataBank.arrMarkers[oldDragMarkerPos];
				holdModule.deleteMarkerAtPosition(oldDragMarkerPos);
				oldDragMarkerPos = scaledX;
				holdModule.addMarkerAtPosition(scaledX, holdText);
				this.updateCurrentPlayTime(scaledX);
				this.updateCurrentPlayTimeViews;
				barTextView.refresh;
			}
			{dragMode == 'dragStartPos'}   {
				this.updateStartPos(scaledX);
				barTextView.refresh;
			}
			{dragMode == 'dragStopPos'}   {
				this.updateStopPos(scaledX);
				barTextView.refresh;
			}
			{dragMode == 'dragSelectionStart'}   {
				if (scaledX < holdTrack.selectionEnd, {
					holdTrack.selectionStart = scaledX;
				},{
					holdTrack.selectionEnd = scaledX;
					dragMode = 'dragSelectionEnd';
				});
				this.updateSelectionText;
				this.updateSelectedTotalText;
				viewEditEvents.refresh;
				if (viewVelocityEvents.notNil, {viewVelocityEvents.refresh;});
			}
			{dragMode == 'dragSelectionEnd'}   {
				if (scaledX > holdTrack.selectionStart, {
					holdTrack.selectionEnd = scaledX;
				},{
					holdTrack.selectionStart = scaledX;
					dragMode = 'dragSelectionStart';
				});
				this.updateSelectionText;
				this.updateSelectedTotalText;
				viewEditEvents.refresh;
				if (viewVelocityEvents.notNil, {viewVelocityEvents.refresh;});
			}
			{dragMode == 'dragLoopStart'}   {
				if (scaledX < holdModule.getSynthArgSpec("loopEnd"), {
					holdModule.setSynthArgSpec("loopStart", scaledX);
				},{
					holdModule.setSynthArgSpec("loopEnd", scaledX);
					dragMode = 'dragLoopEnd';
				});
				this.updateLoopText;
				barTextView.refresh;
			}
			{dragMode == 'dragLoopEnd'}   {
				if (scaledX > holdModule.getSynthArgSpec("loopStart"), {
					holdModule.setSynthArgSpec("loopEnd", scaledX);
				},{
					holdModule.setSynthArgSpec("loopStart", scaledX);
					dragMode = 'dragLoopStart';
				});
				this.updateLoopText;
				barTextView.refresh;
			}
			{dragMode == 'dragCurrentPlayTime'}   {
				this.updateCurrentPlayTime(scaledX);
				this.updateCurrentPlayTimeViews;
			}
			;
		};
		viewSelectionBar.mouseUpAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
			var holdTimeSnapped, holdText;
			var selectionChanged = false;
			if (isInside == false, {
				scaledX = scaledX.clip(viewSelectionBar.viewRect.left,
					viewSelectionBar.viewRect.left+viewSelectionBar.viewRect.width);
			});
			this.updateMouseTextTime(scaledX);
			holdTimeSnapped = holdModule.toolFuncs.getSelectionCheckSnap(holdTrack, scaledX);
			case  {dragMode == 'dragMarkerPos'}   {
				holdText = holdModule.dataBank.arrMarkers[oldDragMarkerPos];
				holdModule.deleteMarkerAtPosition(oldDragMarkerPos);
				holdModule.addMarkerAtPosition(holdTimeSnapped, holdText);
				this.updateCurrentPlayTime(holdTimeSnapped);
				this.updateCurrentPlayTimeViews;
				barTextView.refresh;
			}
			{dragMode == 'dragStartPos'}   {
				this.updateStartPos(holdTimeSnapped);
				barTextView.refresh;
			}
			{dragMode == 'dragStopPos'}   {
				this.updateStopPos(scaledX);
				barTextView.refresh;
			}
			{dragMode == 'dragSelectionStart'}   {
				if (scaledX < holdTrack.selectionEnd, {
					holdTrack.selectionStart = holdTimeSnapped;
					holdTrack.selectionEnd = holdModule.toolFuncs.getSelectionCheckSnap(holdTrack, holdTrack.selectionEnd);
				},{
					holdTrack.selectionEnd = holdTimeSnapped;
					holdTrack.selectionStart = holdModule.toolFuncs.getSelectionCheckSnap(holdTrack, holdTrack.selectionStart);
				});
				holdModule.toolFuncs.updateControllerSelectionIndices(holdTrack);
				this.updateSelectionText;
				this.updateSelectedTotalText;
				viewEditEvents.refresh;
				if (viewVelocityEvents.notNil, {viewVelocityEvents.refresh;});
				selectionChanged = true;
			}
			{dragMode == 'dragSelectionEnd'}   {
				if (scaledX > holdTrack.selectionStart, {
					holdTrack.selectionEnd = holdTimeSnapped;
					holdTrack.selectionStart = holdModule.toolFuncs.getSelectionCheckSnap(holdTrack, holdTrack.selectionStart);
				},{
					holdTrack.selectionStart = holdTimeSnapped;
					holdTrack.selectionEnd = holdModule.toolFuncs.getSelectionCheckSnap(holdTrack, holdTrack.selectionEnd);
				});
				holdModule.toolFuncs.updateControllerSelectionIndices(holdTrack);
				this.updateSelectionText;
				this.updateSelectedTotalText;
				viewEditEvents.refresh;
				if (viewVelocityEvents.notNil, {viewVelocityEvents.refresh;});
				selectionChanged = true;
			}
			{dragMode == 'dragLoopStart'}   {
				if (scaledX < holdModule.getSynthArgSpec("loopEnd"), {
					holdModule.setSynthArgSpec("loopStart", holdTimeSnapped);
					holdModule.setSynthArgSpec("loopEnd", holdModule.toolFuncs.getSelectionCheckSnap(holdTrack,
						holdModule.getSynthArgSpec("loopEnd")));
				},{
					holdModule.setSynthArgSpec("loopEnd", holdTimeSnapped);
					holdModule.setSynthArgSpec("loopStart", holdModule.toolFuncs.getSelectionCheckSnap(holdTrack,
						holdModule.getSynthArgSpec("loopStart")));
				});
				holdModule.adjustLoopEnd;
				this.updateLoopText;
				barTextView.refresh;
			}
			{dragMode == 'dragLoopEnd'}   {
				if (scaledX > holdModule.getSynthArgSpec("loopStart"), {
					holdModule.setSynthArgSpec("loopEnd", holdTimeSnapped);
					holdModule.setSynthArgSpec("loopStart", holdModule.toolFuncs.getSelectionCheckSnap(holdTrack,
						holdModule.getSynthArgSpec("loopStart")));
				},{
					holdModule.setSynthArgSpec("loopStart", holdTimeSnapped);
					holdModule.setSynthArgSpec("loopEnd", holdModule.toolFuncs.getSelectionCheckSnap(holdTrack,
						holdModule.getSynthArgSpec("loopEnd")));
				});
				holdModule.adjustLoopEnd;
				this.updateLoopText;
				barTextView.refresh;
			}
			{dragMode == 'dragCurrentPlayTime'}   {
				this.updateCurrentPlayTime(holdTimeSnapped);
				this.updateCurrentPlayTimeViews;
			}
			;
			if (selectionChanged == true and: {selectionChangeAction.notNil}, {
				selectionChangeAction.value;
			});
			dragMode = nil;
		};
	}  // end of drawSelectionBar

	////////////////////////////////////

	updateSelectedTotalText {
		if (totalTextView.notNil, {
			if (totalTextView.notClosed, {
				totalTextView.string = "selected " ++ holdTrack.selectionIndices.size.asString;
			});
		});
		this.updateSingleEventParameters;
	}

	updateSingleEventParameters {
		var holdEvent, holdBeatsPerBar, holdStartBeats, holdStartSoloBeats, holdStartBars;
		var holdEndBeats, holdEndSoloBeats, holdEndBars;
		// if only 1 event visible then show edit parameters else hide
		if (holdTrack.selectionIndices.size == 1, {
			if (singleEventContainer.notNil, {
				if (singleEventContainer.notClosed, {
					singleEventContainer.visible = true;
					if (holdTrack.trackType == 'Note', {
						holdEvent = holdTrack.arrEditNotes[0];
						holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");

						singleNoteMidiNoteView.valueNoAction = holdEvent[1];
						singleNoteVelView.value = (holdEvent[2] / 1.27).round;

						holdStartBeats = holdEvent[0];
						holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
						holdStartBars = (holdStartBeats / holdBeatsPerBar).floor;
						holdStartSoloBeats = holdStartBeats - (holdStartBars * holdBeatsPerBar);
						singleNoteOnBarsView.value = 1+ holdStartBars;
						singleNoteOnBeatsView.value = 1+ holdStartSoloBeats;

						holdEndBeats = holdEvent[4];
						holdEndBars = (holdEndBeats / holdBeatsPerBar).floor;
						holdEndSoloBeats = holdEndBeats - (holdEndBars * holdBeatsPerBar);
						singleNoteOffBarsView.value = 1+ holdEndBars;
						singleNoteOffBeatsView.value = 1+ holdEndSoloBeats;
					});
					if (holdTrack.trackType == 'Controller', {
						holdEvent = holdTrack.arrControllerVals[holdTrack.selectionIndices[0]];
						holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");

						holdStartBeats = holdEvent[0];
						holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
						holdStartBars = (holdStartBeats / holdBeatsPerBar).floor;
						holdStartSoloBeats = holdStartBeats - (holdStartBars * holdBeatsPerBar);
						singleEventTimeBarsView.value = 1+ holdStartBars;
						singleEventTimeBeatsView.value = 1+ holdStartSoloBeats;

						singleEventValView.value = holdEvent[1];

						if (singleEventOut1ValView.notNil, {
							if (holdOut1ControlSpec.notNil, {
								if (singleEventOut1ValView.numberView.notClosed, {
									singleEventOut1ValView.visible = true;
									singleEventOut1ValView.value = holdOut1ControlSpec.map(holdEvent[1]);
								});
							}, {
								if (singleEventOut1ValView.numberView.notClosed, {
									singleEventOut1ValView.visible = false;
								});
							});
						});
					});
				});
			});
		},{
			if (singleEventContainer.notNil, {
				if (singleEventContainer.notClosed, {
					singleEventContainer.visible = false;
				});
			});
		});
	}

	updateMouseTextTime {arg valX;
		var text, timeString;
		if (mouseTextView.notNil, {
			if (mouseTextView.notClosed, {
				timeString = this.getBarsBeatsString(valX);
				text = "Mouse:  " ++ timeString;
				mouseTextView.string = text;
			});
		});
	}

	updateMouseTextTimeControl {arg valX, valY;
		var text, timeString, controlVal, out1String;
		if (mouseTextView.notNil, {
			if (mouseTextView.notClosed, {
				timeString = this.getBarsBeatsString(valX);
				controlVal = (127 - valY) / 127;
				text = "Mouse:  " ++ timeString ++ ",  Value: " ++ controlVal.round(0.001);
				if (holdOut1ControlSpec.isNil, {
					out1String = "";
				}, {
					out1String = "  (out1: " ++ holdOut1ControlSpec.map(controlVal).round(0.001).asString ++ ")";
				});
				mouseTextView.string = text ++ out1String;
			});
		});
	}

	updateMouseTextTimeNote {arg valX, valY;
		var text, timeString, note;
		if (mouseTextView.notNil, {
			if (mouseTextView.notClosed, {
				timeString = this.getBarsBeatsString(valX);
				note = TXGetMidiNoteString.new((120 - valY).floor);
				text = "Mouse:  " ++ timeString ++ ",  Note: " ++ note;
				mouseTextView.string = text;
			});
		});
	}

	updateMouseTextTimeVel {arg valX, valY;
		var text, timeString, vel;
		if (mouseTextView.notNil, {
			if (mouseTextView.notClosed, {
				timeString = this.getBarsBeatsString(valX);
				vel = (127 - valY) / 1.27;
				text = "Mouse:  " ++ timeString ++ ",  Vel: " ++ vel.round ++ ",  (midiVel: " ++ (vel*1.27).round ++ ")";
				mouseTextView.string = text;
			});
		});
	}

	getBarsBeatsString {arg valX;
		var text, bars, beats, beatsPerBar;
		beatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
		bars = (valX / beatsPerBar).floor;
		beats = valX - (bars * beatsPerBar);
		text = "Bars: " ++ (bars + 1) ++ ",  Beats: " ++ (beats + 1).round(0.001);
		^text;
	}

	updateCurrentPlayTime { arg pos;
		holdModule.currentPlayTime = pos;
		this.updateCurrentPlayTimeText;
		this.updateCurrentMarkerLabel;
	}

	updateCurrentPlayTimeText {
		var holdCurrTimeSoloBeats, holdCurrTimeBars, holdBeatsPerBar;
		if (numboxCurrentPlayTimeBars.notNil, {
			if (numboxCurrentPlayTimeBars.labelView.notClosed, {
				holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
				holdCurrTimeBars = (holdModule.currentPlayTime / holdBeatsPerBar).floor;
				holdCurrTimeSoloBeats = holdModule.currentPlayTime - (holdCurrTimeBars * holdBeatsPerBar);
				numboxCurrentPlayTimeBars.value = 1+ holdCurrTimeBars;
				numboxCurrentPlayTimeBeats.value = (1+ holdCurrTimeSoloBeats).round(0.01);
			});
		});
	}

	updateCurrentMarkerLabel {
		if (textboxMarkerLabel.notNil, {
			if (textboxMarkerLabel.notClosed, {
				textboxMarkerLabel.string = holdModule.dataBank.arrMarkers[holdModule.currentPlayTime] ? "";
			});
		});
	}


	updateStartPos { arg pos;
		var holdStopPosBeats;
		holdModule.setSynthArgSpec("startPosBeats", pos);
		this.updateStartPosText;
		holdStopPosBeats = holdModule.getSynthArgSpec("stopPosBeats");
		if (holdStopPosBeats < (pos + 1), {
			this.updateStopPos(pos + 1);
		});
	}

	updateStartPosText {
		var holdStartPosBeats, holdStartPosSoloBeats, holdStartPosBars, holdBeatsPerBar;
		holdStartPosBeats = holdModule.getSynthArgSpec("startPosBeats");
		holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
		holdStartPosBars = (holdStartPosBeats / holdBeatsPerBar).floor;
		holdStartPosSoloBeats = holdStartPosBeats - (holdStartPosBars * holdBeatsPerBar);
		if (numboxStartPosBars.notNil, {
			if (numboxStartPosBars.labelView.notClosed, {
				numboxStartPosBars.value = 1+ holdStartPosBars;
			});
		});
		if (numboxStartPosBeats.notNil, {
			if (numboxStartPosBeats.labelView.notClosed, {
				numboxStartPosBeats.value = 1+ holdStartPosSoloBeats;
			});
		});
	}

	updateStopPos { arg pos;
		var holdStartPosBeats = holdModule.getSynthArgSpec("startPosBeats");
		pos = max(holdStartPosBeats + 1, pos);
		holdModule.setSynthArgSpec("stopPosBeats", pos);
		this.updateStopPosText;
	}

	updateStopPosText {
		var holdStopPosBeats, holdStopPosSoloBeats, holdStopPosBars, holdBeatsPerBar;
		holdStopPosBeats = holdModule.getSynthArgSpec("stopPosBeats");
		holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
		holdStopPosBars = (holdStopPosBeats / holdBeatsPerBar).floor;
		holdStopPosSoloBeats = holdStopPosBeats - (holdStopPosBars * holdBeatsPerBar);
		if (numboxStopPosBars.notNil, {
			if (numboxStopPosBars.labelView.notClosed, {
				numboxStopPosBars.value = 1+ holdStopPosBars;
			});
		});
		if (numboxStopPosBeats.notNil, {
			if (numboxStopPosBeats.labelView.notClosed, {
				numboxStopPosBeats.value = 1+ holdStopPosSoloBeats;
			});
		});
	}

	updateLoopText {
		var holdBeatsPerBar, holdLoopStartBeats, holdLoopStartSoloBeats, holdLoopStartBars;
		var holdLoopEndBeats, holdLoopEndSoloBeats, holdLoopEndBars;
		holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
		holdLoopStartBeats = holdModule.getSynthArgSpec("loopStart");
		holdLoopStartBars = (holdLoopStartBeats / holdBeatsPerBar).floor;
		holdLoopStartSoloBeats = holdLoopStartBeats - (holdLoopStartBars * holdBeatsPerBar);
		holdLoopEndBeats = holdModule.getSynthArgSpec("loopEnd");
		holdLoopEndBars = (holdLoopEndBeats / holdBeatsPerBar).floor;
		holdLoopEndSoloBeats = holdLoopEndBeats - (holdLoopEndBars * holdBeatsPerBar);
		if (numboxLoopStartBars.notNil, {
			if (numboxLoopStartBars.labelView.notClosed, {
				numboxLoopStartBars.value = 1+ holdLoopStartBars;
			});
		});
		if (numboxLoopStartBeats.notNil, {
			if (numboxLoopStartBeats.labelView.notClosed, {
				numboxLoopStartBeats.value = 1+ holdLoopStartSoloBeats;
			});
		});
		if (numboxLoopEndBars.notNil, {
			if (numboxLoopEndBars.labelView.notClosed, {
				numboxLoopEndBars.value = 1+ holdLoopEndBars;
			});
		});
		if (numboxLoopEndBeats.notNil, {
			if (numboxLoopEndBeats.labelView.notClosed, {
				numboxLoopEndBeats.value = 1+ holdLoopEndSoloBeats;
			});
		});
	}

	updateSelectionText {
		var holdBeatsPerBar, holdSelectionStartBeats, holdSelectionStartSoloBeats, holdSelectionStartBars;
		var holdSelectionEndBeats, holdSelectionEndSoloBeats, holdSelectionEndBars;
		holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
		holdSelectionStartBeats = holdTrack.selectionStart;
		holdSelectionStartBars = (holdSelectionStartBeats / holdBeatsPerBar).floor;
		holdSelectionStartSoloBeats = holdSelectionStartBeats - (holdSelectionStartBars * holdBeatsPerBar);
		holdSelectionEndBeats = holdTrack.selectionEnd;
		holdSelectionEndBars = (holdSelectionEndBeats / holdBeatsPerBar).floor;
		holdSelectionEndSoloBeats = holdSelectionEndBeats - (holdSelectionEndBars * holdBeatsPerBar);
		if (numboxSelectionStartBars.notNil, {
			if (numboxSelectionStartBars.labelView.notClosed, {
				numboxSelectionStartBars.value = 1+ holdSelectionStartBars;
			});
		});
		if (numboxSelectionStartBeats.notNil, {
			if (numboxSelectionStartBeats.labelView.notClosed, {
				numboxSelectionStartBeats.value = 1+ holdSelectionStartSoloBeats;
			});
		});
		if (numboxSelectionEndBars.notNil, {
			if (numboxSelectionEndBars.labelView.notClosed, {
				numboxSelectionEndBars.value = 1+ holdSelectionEndBars;
			});
		});
		if (numboxSelectionEndBeats.notNil, {
			if (numboxSelectionEndBeats.labelView.notClosed, {
				numboxSelectionEndBeats.value = 1+ holdSelectionEndSoloBeats;
			});
		});
	}

	updateCurrentPlayTimeViews {
		holdModule.refreshCurrentTimeViews;
		this.updateCurrentPlayTimeText;
	}

	refreshBarTextView {
		if (barTextView.notNil, {
			if (barTextView.notClosed, {
				barTextView.refresh;
			});
		});
	}

	updateDisplayRange {
		if (displayRangeSlider.notNil, {
			displayRangeSlider.valueBoth = [holdModule.getSynthArgSpec("displayRangeStart"), holdModule.getSynthArgSpec("displayRangeEnd")];
		});
	}

	setOffsetViewBackCol {arg inView, inOffset;
		if (inOffset == 0, {
			inView.background_(TXColor.white);
		}, {
			inView.background_(TXColor.paleTurquoise);
		});
	}

}
