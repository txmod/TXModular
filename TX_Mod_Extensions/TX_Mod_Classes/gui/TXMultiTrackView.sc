// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// ========================================================================
/* 	NOTE - the following code generates the list below:
(
"// ==== Instance Methods ====================== ".postln;
a = TXMultiTrackView.methods;
b = a.collect ({ arg item, i; item.name.asString;});
b.sort.do ({ arg item, i; ("// " ++ item).postln;});
"// ================================================".postln;
)
*/
// ==== Instance Methods ======================
// action
// action_
// barTextView
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
// updateSelectionText
// updateStartPos
// updateStartPosText
// updateStopPos
// updateStopPosText
// ================================================

TXMultiTrackView {

	var <>action, <>scrollView, <barTextView, system;
	var bpm, beatsPerBar, holdBackgroundBox, arrTracks, holdModule;
	var displayRangeSlider, viewSelectionBar;
	var numboxBeatsPerBar, numboxStartPosBeats, numboxStartPosBars, numboxStopPosBeats, numboxStopPosBars;
	var numboxSelectionStartBars, numboxSelectionStartBeats, numboxSelectionEndBars, numboxSelectionEndBeats;
	var numboxLoopStartBars, numboxLoopStartBeats, numboxLoopEndBars, numboxLoopEndBeats;
	var numboxCurrentPlayTimeBars, numboxCurrentPlayTimeBeats;
	var btnAddMarker, btnDeleteMarker, btnPrevMarker, btnNextMarker, btnShowProcesses;
	var viewMarkers, scrollViewHeight, textboxMarkerLabel;
	var scaledUserViewWidth = 718;
	var trackParametersWidth = 328;

	*new {arg argSystem, argParent, dimensions, argModule, argAction, argBpm, argBeatsPerBar, scrollViewAction, initAction;
		^super.new.init(argSystem, argParent, dimensions, argModule, argAction, argBpm, argBeatsPerBar, scrollViewAction, initAction);
	}

	init {arg argSystem, argParent, dimensions, argModule, argAction, argBpm, argBeatsPerBar, scrollViewAction, initAction;

		var holdView, holdProcessText, holdStepTime, scrollBox;
		var holdParent, holdMaxDisplayBeats;
		var holdDecLeft, holdDecTop, dragMode, arrSnapBeatVals, arrSnapBeatStrings, oldDragMarkerPos;
		var arrProcessFuncs, arrProcessStrings, holdProcess;

		action = argAction;
		system = argSystem;
		bpm = argBpm;
		beatsPerBar = argBeatsPerBar;
		holdModule = argModule;
		arrTracks = argModule.arrTracks;
		holdModule.scaledUserViews = ();

		initAction.value(this);

		argParent.decorator.shift(0, 2);

		// Play From Start
		holdView = TXCheckBox(argParent, 110 @ 20, "Play From Start", TXColor.sysGuiCol1, TXColor.grey(0.9),
			TXColor.white, TXColor.sysGuiCol1)
		.value_(holdModule.getSynthArgSpec("playFromStart"))
		.action_({|view|
			holdModule.setSynthArgSpec("playFromStart", view.value);
		});

		// Start position
		numboxStartPosBars = TXNumber(argParent, 70 @ 20, "Start", ControlSpec(1, 10000),
			{|view|
				var totalBeats;
				totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
				+ (numboxStartPosBeats.value - 1);
				totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
				holdModule.setSynthArgSpec("startPosBeats", totalBeats);
				system.showView;
			},
			// initial value
			1,
			false, 26, 40
		);
		numboxStartPosBars.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		numboxStartPosBars.numberView.normalColor_(TXColor.sysGuiCol1).background_(TXColor.white);

		numboxStartPosBeats = TXNumber(argParent, 50 @ 20, ": ",
			ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
			{|view|
				var totalBeats;
				totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (numboxStartPosBars.value - 1)) + (view.value - 1);
				totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
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
		numboxStopPosBars = TXNumber(argParent, 70 @ 20, "Stop", ControlSpec(1, 10000),
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

		numboxStopPosBeats = TXNumber(argParent, 50 @ 20, ": ",
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

		argParent.decorator.shift(18, 0);

		// Selection
		numboxSelectionStartBars = TXNumber(argParent, 120 @ 20, "Selection Start", ControlSpec(1, 10000, step: 1),
			{|view|
				var totalBeats;
				totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
				+ (numboxSelectionStartBeats.value - 1);
				totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
				// holdModule.setSynthArgSpec("multiSelectionStart", totalBeats);
				holdModule.setSelectionStart(0, totalBeats);
				system.showView;
			},
			// initial value
			1,
			false, 80, 34
		);
		numboxSelectionStartBars.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);
		numboxSelectionStartBars.numberView.normalColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

		numboxSelectionStartBeats = TXNumber(argParent, 88 @ 20, ": ",
			ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
			{|view|
				var totalBeats;
				totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (numboxSelectionStartBars.value - 1)) + (view.value - 1);
				totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
				// holdModule.setSynthArgSpec("multiSelectionStart", totalBeats);
				holdModule.setSelectionStart(0, totalBeats);
				system.showView;
			},
			// initial value
			1 ,
			false, 10, 34
		);
		numboxSelectionStartBeats.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);
		numboxSelectionStartBeats.numberView.normalColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

		numboxSelectionEndBars = TXNumber(argParent, 46 @ 20, "End", ControlSpec(1, 10000, step: 1),
			{|view|
				var totalBeats;
				totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
				+ (numboxSelectionEndBeats.value - 1);
				totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
				// holdModule.setSynthArgSpec("multiSelectionEnd", totalBeats);
				holdModule.setSelectionEnd(0, totalBeats);
				system.showView;
			},
			// initial value
			1,
			false, 24, 34
		);
		numboxSelectionEndBars.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);
		numboxSelectionEndBars.numberView.normalColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

		numboxSelectionEndBeats = TXNumber(argParent, 50 @ 20, ": ",
			ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
			{|view|
				var totalBeats;
				totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (numboxSelectionEndBars.value - 1)) + (view.value - 1);
				totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
				// holdModule.setSynthArgSpec("multiSelectionEnd", totalBeats);
				holdModule.setSelectionEnd(0, totalBeats);
				system.showView;
			},
			// initial value
			1 ,
			false, 10, 34
		);
		numboxSelectionEndBeats.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);
		numboxSelectionEndBeats.numberView.normalColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow);

		this.updateSelectionText; // set initial values

		argParent.decorator.shift(18, 0);

		// Loop
		holdView = TXCheckBox(argParent, 52 @ 20, "Loop", TXColor.green, TXColor.paleGreen2,
			TXColor.paleGreen2, TXColor.sysGuiCol2)
		.value_(holdModule.getSynthArgSpec("loop"))
		.action_({|view|
			holdModule.setSynthArgSpec("loop", view.value);
			system.showView(this);
		});

		numboxLoopStartBars = TXNumber(argParent, 70 @ 20, "Start", ControlSpec(1, 10000, step: 1),
			{|view|
				var totalBeats;
				totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
				+ (numboxLoopStartBeats.value - 1);
				totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
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

		numboxLoopStartBeats = TXNumber(argParent, 50 @ 20, ": ",
			ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
			{|view|
				var totalBeats;
				totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (numboxLoopStartBars.value - 1)) + (view.value - 1);
				totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
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

		numboxLoopEndBars = TXNumber(argParent, 46 @ 20, "End", ControlSpec(1, 10000, step: 1),
			{|view|
				var totalBeats;
				totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (view.value - 1))
				+ (numboxLoopEndBeats.value - 1);
				totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
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

		numboxLoopEndBeats = TXNumber(argParent, 8 @ 20, ": ",
			ControlSpec(1, holdModule.getSynthArgSpec("beatsPerBar") + 0.999),
			{|view|
				var totalBeats;
				totalBeats = (holdModule.getSynthArgSpec("beatsPerBar") * (numboxLoopEndBars.value - 1)) + (view.value - 1);
				totalBeats = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, totalBeats);
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

		// draw white line
		// argParent.decorator.nextLine;
		// StaticText(argParent, 1070 @ 1).background_(TXColor.white);
		argParent.decorator.shift(0, 6);

		// snapToGrid
		TXCheckBox(argParent, 110 @ 20, "Snap To Grid", TXColor.sysGuiCol2, TXColor.grey(0.9),
			TXColor.white, TXColor.sysGuiCol1)
		.value_(holdModule.getSynthArgSpec("snapToGrid"))
		.action_({|view|
			holdModule.setSynthArgSpec("snapToGrid", view.value);
		});

		// snapBeats
		arrSnapBeatVals = [1,1/2,1/3,1/4,1/5,1/6,1/7,1/8,1/9, 1/10, 1/12, 1/14, 1/16, 1/20, 1/24, 1/30, 1/32];
		arrSnapBeatStrings = ["1","1/2","1/3","1/4","1/5","1/6","1/7","1/8",
			"1/9","1/10","1/12","1/14","1/16","1/20","1/24","1/30","1/32"];
		holdView = TXPopupPlusMinus(argParent, 174 @ 20, "Snap Beats", arrSnapBeatStrings, {arg view;
			holdModule.setSynthArgSpec("snapBeats", arrSnapBeatVals[view.value]);
			// update view
			system.showView;
		}, arrSnapBeatVals.indexOf(holdModule.getSynthArgSpec("snapBeats")), false, 70);
		holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		holdView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);

		argParent.decorator.shift(6, 0);

		// display range
		displayRangeSlider = TXRangeSlider(argParent, 684 @ 20, "Bars", ControlSpec(1, holdModule.getSynthArgSpec("maxDisplayBars") + 1),
			{arg view;
				var loVal = min(view.lo, view.hi);
				var hiVal = max(view.lo, view.hi);
				var maxBars = holdModule.getSynthArgSpec("maxDisplayBars") + 1;
				var minDisplaySize = 1 / holdModule.getSynthArgSpec("beatsPerBar");
				// {   view.lo = loVal;
				// 	view.hi = hiVal;
				// }.defer(0.5);
				hiVal = max(hiVal, loVal + minDisplaySize);
				if (hiVal > maxBars, {
					holdModule.setSynthArgSpec("maxDisplayBars", hiVal.ceil);
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
		Button(argParent, 30 @ 20)
		.states_([
			["max", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			displayRangeSlider.valueBoth_([0 , holdModule.getSynthArgSpec("maxDisplayBars") + 1]);
		});

		holdView =  NumberBox(argParent, 44 @ 20)
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

		argParent.decorator.nextLine.shift(0, 3);

		// ========================
		// Current PlayTime
		numboxCurrentPlayTimeBars = TXNumber(argParent, 70 @ 20, "Time", ControlSpec(1, 10000, step: 1),
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

		numboxCurrentPlayTimeBeats = TXNumber(argParent, 40 @ 20, ": ",
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
		btnPrevMarker  = Button(argParent, 13 @ 20)
		.states_([
			["<", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			var holdNewActionStep;
			holdModule.jumpToPreviousMarker;
			system.showView;
		});

		// next marker btn
		btnNextMarker  = Button(argParent, 13 @ 20)
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
		btnAddMarker  = Button(argParent, 66 @ 20)
		.states_([
			["Add Marker", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			var holdNewActionStep;
			holdModule.addMarkerAtCurrentPosition(textboxMarkerLabel.string);
			system.showView;
		});

		// name
		textboxMarkerLabel = TextField(argParent, Rect(0, 0, 80, 20),
			" ", TXColor.black, TXColor.white,
			TXColor.black, TXColor.white, 4);
		textboxMarkerLabel.action = {arg view;
			//holdMarkerLabelString = view.string;
		};
		this.updateCurrentMarkerLabel; // set initial string

		// delete marker btn
		btnDeleteMarker  = Button(argParent, 24 @ 20)
		.states_([
			["Del", TXColor.white, TXColor.sysDeleteCol]
		])
		.action_({|view|
			var holdNewActionStep;
			holdModule.deleteMarkerAtCurrentPosition;
			system.showView;
		});

		// store decorator position
		holdDecLeft = argParent.decorator.left;
		holdDecTop = argParent.decorator.top;

		// Bar text behind selection bar
		barTextView = UserView( argParent, scaledUserViewWidth @ 20);
		barTextView.background =(TXColor.white);
		barTextView.drawFunc = {arg view;
			var holdPosX, holdLoopPosX;
			if (viewSelectionBar.notNil, {
				Pen.font = Font.defaultSansFace;
				Pen.color = TXColor.grey;
				holdModule.getSynthArgSpec("maxDisplayBars").floor.do({arg i;
					holdPosX = viewSelectionBar.convertFwd( i * holdModule.getSynthArgSpec("beatsPerBar"), 0 )[0];
					if ((holdPosX >= 0) && (holdPosX < 720), {
						Pen.stringInRect( " " ++ (i + 1).asString, Rect(holdPosX,10,40,20));
					});
				});
				Pen.color = TXColor.sysGuiCol1;
				holdPosX = viewSelectionBar.convertFwd(holdModule.getSynthArgSpec("startPosBeats"), 0 )[0];
				Pen.stringInRect( " Start", Rect(holdPosX,1,60,20));
				holdPosX = viewSelectionBar.convertFwd(holdModule.getSynthArgSpec("stopPosBeats"), 0 )[0];
				Pen.stringInRect( " Stop", Rect(holdPosX,1,60,20));
				holdLoopPosX = holdModule.getSynthArgSpec("loopStart");
				holdPosX = viewSelectionBar.convertFwd(holdLoopPosX, 0 )[0];
				if (holdModule.getSynthArgSpec("loop").asBoolean == true, {
					Pen.color = TXColor.green;
					Pen.stringInRect( " Loop", Rect(holdPosX,1,60,20));
				});
			});
		};
		{barTextView.refresh;}.defer(0.1);

		// restore decorator position
		argParent.decorator.left = holdDecLeft;
		argParent.decorator.top = holdDecTop;

		// selection bar
		holdMaxDisplayBeats = holdModule.getSynthArgSpec("maxDisplayBars") * holdModule.getSynthArgSpec("beatsPerBar");
		viewSelectionBar = ScaledUserView( argParent, scaledUserViewWidth @ 20, Rect(0,0,holdMaxDisplayBeats, 10), );
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
			var holdSelectionStart = holdModule.getSynthArgSpec("multiSelectionStart");
			var holdSelectionEnd = holdModule.getSynthArgSpec("multiSelectionEnd");
			var holdLoopStart, holdLoopEnd, holdLoopBoxHeight;
			// draw beat grid
			holdMaxDisplayBeats.floor.do({arg i;
				var posX = i;
				// if inside view then draw
				if (posX >= viewSelectionBar.viewRect.left
					&& (posX <= (viewSelectionBar.viewRect.left + viewSelectionBar.viewRect.width)), {
						Pen.strokeColor = TXColor.grey;
						// if bar  draw longer thicker line
						if ((posX % holdBeatsPerBar) == 0, {
							Pen.width = fixWidth2;
							Pen.line(posX@0, posX@10).stroke;
						},{
							Pen.width = fixWidth;
							Pen.line(posX@0, posX@6).stroke;
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
			Pen.strokeColor =TXColor.yellow.alpha_(0.5);
			Pen.fillColor = TXColor.yellow.alpha_(0.2);
			Pen.width = fixWidth;
			Pen.addRect(Rect(holdSelectionStart, 6, holdSelectionEnd - holdSelectionStart, 5, ));
			Pen.fillStroke;
			// draw orange selection start/end
			Pen.fillColor = TXColor.yellow;
			Pen.addRect(Rect(holdSelectionStart - fixWidth, 6, fixWidth2, 5 ));
			Pen.addRect(Rect(holdSelectionEnd - fixWidth, 6, fixWidth2, 5 ));
			Pen.fill;
			// draw start pos box
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
			dragMode = nil;
			if (((abs(startPos - scaledX) < 0.05) && (scaledY < 6)) , {  // shift button
				dragMode = 'dragStartPos';
				this.updateStartPos(scaledX);
				barTextView.refresh;
			},{
				if (((abs(stopPos - scaledX) < 0.05) && (scaledY < 6)), {
					dragMode = 'dragStopPos';
					this.updateStopPos(scaledX);
					barTextView.refresh;
				},{
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
						viewSelectionBar.refresh;
					},{
						if ((mod == 131072) , {   // shift button
							if ((abs(holdModule.getSynthArgSpec("multiSelectionStart") - scaledX) < 0.05) && (scaledY > 5), {
								dragMode = 'dragSelectionStart';
							},{
								if ((abs(holdModule.getSynthArgSpec("multiSelectionEnd") - scaledX) < 0.05) && (scaledY > 5), {
									dragMode = 'dragSelectionEnd';
								},{
									holdModule.setSynthArgSpec("multiSelectionStart", scaledX);
									holdModule.setSynthArgSpec("multiSelectionEnd", scaledX);
									dragMode = 'dragSelectionEnd';
								});
							});
						},{
							this.updateCurrentPlayTime(scaledX);
							dragMode = 'dragCurrentPlayTime';
						});
						this.updateSelectionText;
						holdModule.refreshScaledUserViews;
					});
				});
			});
		};
		viewSelectionBar.mouseMoveAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
			if (isInside == false, {
				scaledX = scaledX.clip(viewSelectionBar.viewRect.left,
					viewSelectionBar.viewRect.left+viewSelectionBar.viewRect.width);
			});
			case  {dragMode == 'dragStartPos'}   {
				this.updateStartPos(scaledX);
				barTextView.refresh;
			}
			{dragMode == 'dragStopPos'}   {
				this.updateStopPos(scaledX);
				barTextView.refresh;
			}
			{dragMode == 'dragSelectionStart'}   {
				if (scaledX < holdModule.getSynthArgSpec("multiSelectionEnd"), {
					holdModule.setSynthArgSpec("multiSelectionStart", scaledX);
				},{
					holdModule.setSynthArgSpec("multiSelectionEnd", scaledX);
					dragMode = 'dragSelectionEnd';
				});
				this.updateSelectionText;
				holdModule.refreshScaledUserViews;
			}
			{dragMode == 'dragSelectionEnd'}   {
				if (scaledX > holdModule.getSynthArgSpec("multiSelectionStart"), {
					holdModule.setSynthArgSpec("multiSelectionEnd", scaledX);
				},{
					holdModule.setSynthArgSpec("multiSelectionStart", scaledX);
					dragMode = 'dragSelectionStart';
				});
				this.updateSelectionText;
				holdModule.refreshScaledUserViews;
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
			var holdTimeSnapped;
			if (isInside == false, {
				scaledX = scaledX.clip(viewSelectionBar.viewRect.left,
					viewSelectionBar.viewRect.left+viewSelectionBar.viewRect.width);
			});
			holdTimeSnapped = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, scaledX);
			case  {dragMode == 'dragStartPos'}   {
				this.updateStartPos(holdTimeSnapped);
				barTextView.refresh;
			}
			{dragMode == 'dragStopPos'}   {
				this.updateStopPos(scaledX);
				barTextView.refresh;
			}
			{dragMode == 'dragSelectionStart'}   {
				if (scaledX < holdModule.getSynthArgSpec("multiSelectionEnd"), {
					holdModule.setSynthArgSpec("multiSelectionStart", holdTimeSnapped);
					holdModule.setSynthArgSpec("multiSelectionEnd",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("multiSelectionEnd")));
				},{
					holdModule.setSynthArgSpec("multiSelectionEnd", holdTimeSnapped);
					holdModule.setSynthArgSpec("multiSelectionStart",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("multiSelectionStart")));
				});
				this.updateSelectionText;
				holdModule.refreshScaledUserViews;
			}
			{dragMode == 'dragSelectionEnd'}   {
				if (scaledX > holdModule.getSynthArgSpec("multiSelectionStart"), {
					holdModule.setSynthArgSpec("multiSelectionEnd", holdTimeSnapped);
					holdModule.setSynthArgSpec("multiSelectionStart",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("multiSelectionStart")));
				},{
					holdModule.setSynthArgSpec("multiSelectionStart", holdTimeSnapped);
					holdModule.setSynthArgSpec("multiSelectionEnd",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("multiSelectionEnd")));
				});
				this.updateSelectionText;
				holdModule.refreshScaledUserViews;
			}
			{dragMode == 'dragLoopStart'}   {
				if (scaledX < holdModule.getSynthArgSpec("loopEnd"), {
					holdModule.setSynthArgSpec("loopStart", holdTimeSnapped);
					holdModule.setSynthArgSpec("loopEnd",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("loopEnd")));
				},{
					holdModule.setSynthArgSpec("loopEnd", holdTimeSnapped);
					holdModule.setSynthArgSpec("loopStart",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("loopStart")));
				});
				this.updateLoopText;
				barTextView.refresh;
			}
			{dragMode == 'dragLoopEnd'}   {
				if (scaledX > holdModule.getSynthArgSpec("loopStart"), {
					holdModule.setSynthArgSpec("loopEnd", holdTimeSnapped);
					holdModule.setSynthArgSpec("loopStart",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("loopStart")));
				},{
					holdModule.setSynthArgSpec("loopStart", holdTimeSnapped);
					holdModule.setSynthArgSpec("loopEnd",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("loopEnd")));
				});
				this.updateLoopText;
				barTextView.refresh;
			}
			{dragMode == 'dragCurrentPlayTime'}   {
				this.updateCurrentPlayTime(holdTimeSnapped);
				this.updateCurrentPlayTimeViews;
			}
			;
			dragMode = nil;
		};

		argParent.decorator.nextLine.shift(0,2);


		// ========================
		// select all btn
		Button(argParent, 59 @ 18)
		.states_([ ["Select all", TXColor.white, TXColor.sysGuiCol2] ])
		.action_({|view|
			holdModule.toolFuncs.selectAllTracks(1);
			system.showView;
		});

		// unselect all btn
		Button(argParent, 71 @ 18)
		.states_([ ["Unselect all", TXColor.white, TXColor.sysGuiCol2] ])
		.action_({|view|
			holdModule.toolFuncs.selectAllTracks(0);
			system.showView;
		});

		// mute all btn
		Button(argParent, 54 @ 18)
		.states_([ ["Mute all", TXColor.white, TXColor.sysGuiCol2] ])
		.action_({|view|
			holdModule.toolFuncs.muteAllTracks(1);
			system.showView;
		});

		// unmute all btn
		Button(argParent, 66 @ 18)
		.states_([ ["Unmute all", TXColor.white, TXColor.sysGuiCol2] ])
		.action_({|view|
			holdModule.toolFuncs.muteAllTracks(0);
			system.showView;
		});

		// unhide all btn
		Button(argParent, 62 @ 18)
		.states_([ ["Unhide all", TXColor.white, TXColor.sysGuiCol2] ])
		.action_({|view|
			holdModule.toolFuncs.unhideAllTracks;
			system.showView;
		});

		argParent.decorator.shift(0,-4);

		// Markers
		holdMaxDisplayBeats = holdModule.getSynthArgSpec("maxDisplayBars") * beatsPerBar;
		viewMarkers = ScaledUserView( argParent, scaledUserViewWidth @ 20, Rect(0,0,holdMaxDisplayBeats, 1) );
		viewMarkers.background = TXColor.white;
		//viewMarkers.view.resize = 5;
		viewMarkers.drawFunc = {arg view;
			var fixWidth = view.pixelScale.x;
			var fixWidth2 = fixWidth * 2;
			//var fixHeight = view.pixelScale.y;
			var holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
			var holdMaxDisplayBeats = holdModule.getSynthArgSpec("maxDisplayBars") * holdBeatsPerBar;
			var holdSelectionStart = holdModule.getSynthArgSpec("multiSelectionStart");
			var holdSelectionEnd = holdModule.getSynthArgSpec("multiSelectionEnd");
			// // draw beat grid
			// holdMaxDisplayBeats.floor.do({arg i;
			// 	var posX = i;
			// 	// if inside view then draw
			// 	if (posX >= viewMarkers.viewRect.left
			// 		&& (posX <= (viewMarkers.viewRect.left + viewMarkers.viewRect.width)), {
			// 			Pen.strokeColor = TXColor.sysGuiCol1;
			// 			// if bar  draw longer thicker line
			// 			if ((posX % holdBeatsPerBar) == 0, {
			// 				Pen.width = fixWidth2;
			// 				},{
			// 					Pen.width = fixWidth;
			// 			});
			// 			Pen.line(posX@0, posX@20).stroke;
			// 	});
			// });
			// draw yellow selection box
			Pen.strokeColor = TXColor.yellow.alpha_(0.5);
			Pen.fillColor = TXColor.yellow.alpha_(0.2);
			Pen.width = fixWidth;
			Pen.addRect(Rect(holdSelectionStart, 0, holdSelectionEnd - holdSelectionStart, 20, ));
			Pen.fillStroke;
			// draw all markers
			holdModule.dataBank.arrMarkers.pairsDo({arg time, string;
				Pen.fillColor = TXColor.black;
				Pen.addRect(Rect(time - fixWidth, 0, fixWidth2, 20 ));
				Pen.fill;
			});
			// draw  current play time box
			Pen.fillColor = Color.red.alpha_(0.75);
			Pen.addRect(Rect(holdModule.currentPlayTime - fixWidth, 0, fixWidth2, 20 ));
			Pen.fill;
		};
		// marker text - in unscaled draw func
		viewMarkers.unscaledDrawFunc = {arg view;
			var posX, text, holdBeatsPerBar, holdTimeBars, holdTimeSoloBeats;
			// draw all markers
			holdModule.dataBank.arrMarkers.pairsDo({arg argPos, argText;
				posX = view.translateScale( Point(argPos, 0)).x;
				// if (posX >= viewMarkers.viewRect.left
				// 	&& (posX <= (viewMarkers.viewRect.left + viewMarkers.viewRect.width)), {
				if (argText == "", {
					holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
					holdTimeBars = (argPos / holdBeatsPerBar).floor;
					holdTimeSoloBeats = argPos - (holdTimeBars * holdBeatsPerBar);
					argText = (1 + holdTimeBars).asString ++ ":" ++ (1 + holdTimeSoloBeats.round(0.01)).asString;
				});
				Pen.stringLeftJustIn(argText, Rect(posX + 4, 0, 100, 20), color: TXColor.black);
				// });
			});
			Pen.stroke;
		};
		viewMarkers.mouseDownAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
			var holdPosX;
			var nearestX;
			dragMode = nil;
			oldDragMarkerPos = nil;
			if (holdModule.dataBank.arrMarkerTimes.asArray.size > 0, {
				nearestX = holdModule.dataBank.arrMarkerTimes.nearestTo(scaledX);
				if ((abs(nearestX - scaledX) < 0.1) , {
					dragMode = 'dragMarkerPos';
					oldDragMarkerPos = nearestX;
					this.updateCurrentPlayTime(scaledX);
					this.updateCurrentPlayTimeViews;
				});
			});
			if (dragMode.isNil, {
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
							this.updateLoopText;
						});
					});
					barTextView.refresh;
					viewSelectionBar.refresh;
				},{
					if ((mod == 131072) , {   // shift button
						if ((abs(holdModule.getSynthArgSpec("multiSelectionStart") - scaledX) < 0.05) && (scaledY > 5), {
							dragMode = 'dragSelectionStart';
						},{
							if ((abs(holdModule.getSynthArgSpec("multiSelectionEnd") - scaledX) < 0.05) && (scaledY > 5), {
								dragMode = 'dragSelectionEnd';
							},{
								holdModule.setSynthArgSpec("multiSelectionStart", scaledX);
								holdModule.setSynthArgSpec("multiSelectionEnd", scaledX);
								dragMode = 'dragSelectionEnd';
							});
						});
					},{
						this.updateCurrentPlayTime(scaledX);
						dragMode = 'dragCurrentPlayTime';
					});
					this.updateSelectionText;
					holdModule.refreshScaledUserViews;
				});
			});
		};
		viewMarkers.mouseMoveAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
			var holdText;
			if (isInside == false, {
				scaledX = scaledX.clip(viewMarkers.viewRect.left,
					viewMarkers.viewRect.left+viewMarkers.viewRect.width);
			});
			case  {dragMode == 'dragMarkerPos'}   {
				holdText = holdModule.dataBank.arrMarkers[oldDragMarkerPos];
				holdModule.deleteMarkerAtPosition(oldDragMarkerPos);
				oldDragMarkerPos = scaledX;
				holdModule.addMarkerAtPosition(scaledX, holdText);
				this.updateCurrentPlayTime(scaledX);
				this.updateCurrentPlayTimeViews;
			}
			{dragMode == 'dragSelectionStart'}   {
				if (scaledX < holdModule.getSynthArgSpec("multiSelectionEnd"), {
					holdModule.setSynthArgSpec("multiSelectionStart", scaledX);
				},{
					holdModule.setSynthArgSpec("multiSelectionEnd", scaledX);
					dragMode = 'dragSelectionEnd';
				});
				this.updateSelectionText;
				holdModule.refreshScaledUserViews;
			}
			{dragMode == 'dragSelectionEnd'}   {
				if (scaledX > holdModule.getSynthArgSpec("multiSelectionStart"), {
					holdModule.setSynthArgSpec("multiSelectionEnd", scaledX);
				},{
					holdModule.setSynthArgSpec("multiSelectionStart", scaledX);
					dragMode = 'dragSelectionStart';
				});
				this.updateSelectionText;
				holdModule.refreshScaledUserViews;
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
				viewSelectionBar.refresh;
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
				viewSelectionBar.refresh;
			}
			{dragMode == 'dragCurrentPlayTime'}   {
				this.updateCurrentPlayTime(scaledX);
				this.updateCurrentPlayTimeViews;
			}
			;
		};
		viewMarkers.mouseUpAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
			var holdTimeSnapped, holdText;
			if (isInside == false, {
				scaledX = scaledX.clip(viewMarkers.viewRect.left,
					viewMarkers.viewRect.left+viewMarkers.viewRect.width);
			});
			holdTimeSnapped = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, scaledX);
			case  {dragMode == 'dragMarkerPos'}   {
				holdText = holdModule.dataBank.arrMarkers[oldDragMarkerPos];
				holdModule.deleteMarkerAtPosition(oldDragMarkerPos);
				holdModule.addMarkerAtPosition(holdTimeSnapped, holdText);
				this.updateCurrentPlayTime(holdTimeSnapped);
				this.updateCurrentPlayTimeViews;
			}
			{dragMode == 'dragSelectionStart'}   {
				if (scaledX < holdModule.getSynthArgSpec("multiSelectionEnd"), {
					holdModule.setSynthArgSpec("multiSelectionStart", holdTimeSnapped);
					holdModule.setSynthArgSpec("multiSelectionEnd",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("multiSelectionEnd")));
				},{
					holdModule.setSynthArgSpec("multiSelectionEnd", holdTimeSnapped);
					holdModule.setSynthArgSpec("multiSelectionStart",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("multiSelectionStart")));
				});
				this.updateSelectionText;
				holdModule.refreshScaledUserViews;
			}
			{dragMode == 'dragSelectionEnd'}   {
				if (scaledX > holdModule.getSynthArgSpec("multiSelectionStart"), {
					holdModule.setSynthArgSpec("multiSelectionEnd", holdTimeSnapped);
					holdModule.setSynthArgSpec("multiSelectionStart",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("multiSelectionStart")));
				},{
					holdModule.setSynthArgSpec("multiSelectionStart", holdTimeSnapped);
					holdModule.setSynthArgSpec("multiSelectionEnd",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("multiSelectionEnd")));
				});
				this.updateSelectionText;
				holdModule.refreshScaledUserViews;
			}
			{dragMode == 'dragLoopStart'}   {
				if (scaledX < holdModule.getSynthArgSpec("loopEnd"), {
					holdModule.setSynthArgSpec("loopStart", holdTimeSnapped);
					holdModule.setSynthArgSpec("loopEnd",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("loopEnd")));
				},{
					holdModule.setSynthArgSpec("loopEnd", holdTimeSnapped);
					holdModule.setSynthArgSpec("loopStart",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("loopStart")));
				});
				this.updateLoopText;
				barTextView.refresh;
			}
			{dragMode == 'dragLoopEnd'}   {
				if (scaledX > holdModule.getSynthArgSpec("loopStart"), {
					holdModule.setSynthArgSpec("loopEnd", holdTimeSnapped);
					holdModule.setSynthArgSpec("loopStart",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("loopStart")));
				},{
					holdModule.setSynthArgSpec("loopStart", holdTimeSnapped);
					holdModule.setSynthArgSpec("loopEnd",
						holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("loopEnd")));
				});
				this.updateLoopText;
				barTextView.refresh;
			}
			{dragMode == 'dragCurrentPlayTime'}   {
				this.updateCurrentPlayTime(holdTimeSnapped);
				this.updateCurrentPlayTimeViews;
			}
			;
			dragMode = nil;
			oldDragMarkerPos = nil;
		};
		viewMarkers.refresh;
		// save views
		holdModule.scaledUserViews[999] = viewMarkers;
		holdModule.registerCurrentTimeView(viewMarkers);

		// spacer
		argParent.decorator.nextLine;
		StaticText(argParent, 1070 @ 1).background_(TXColor.white);
		argParent.decorator.nextLine;

		if (holdModule.showMultitrackProcesses == 1, {
			scrollViewHeight = dimensions.y - 70 - 108;
		}, {
			scrollViewHeight = dimensions.y - 70;
		});

		if (scrollViewAction.notNil, {
			// add ScrollView
			scrollView = ScrollView(argParent, Rect(0, 0, dimensions.x, scrollViewHeight))
			.hasBorder_(false);
			scrollView.background_(TXColor.sysChannelControl);
			scrollView.action = scrollViewAction;
			scrollView.hasHorizontalScroller = false;
			scrollView.hasVerticalScroller = true;
			scrollBox = CompositeView(scrollView, Rect(0, 0, dimensions.x, 60 + (arrTracks.size * 52)));
			scrollBox.decorator = FlowLayout(scrollBox.bounds);
			scrollBox.decorator.margin.x = 0;
			scrollBox.decorator.margin.y = 0;
			scrollBox.decorator.reset;
		});
		holdParent = scrollBox ? argParent;
		//holdParent.decorator.shift(0, 2);

		if (viewSelectionBar.notNil and: {viewSelectionBar.view.notClosed}, {
			holdModule.scaledUserViews[0] = [viewSelectionBar];
		});

		// display tracks
		arrTracks.do({ arg track, trackIndex;
			var popupTrack, labelTrackID, btnEdit, btnDelete, viewTrackEvents, viewTrackOverlay, textboxName, labelboxType;
			var chkboxMute, chkboxMon, chkboxRec, numboxOffset, holdParametersLeft, holdParametersTop;


			// if not hidden, show parameters
			if (track.hidden != true, {

				// store decorator position
				holdParametersLeft = holdParent.decorator.left;
				holdParametersTop = holdParent.decorator.top;
				// parameters box
				holdView = StaticText(holdParent, Rect(0, 0, trackParametersWidth, 48));
				if (track.selected.asBoolean, {
					holdView.background_(Color.white.blend(Color.yellow, 0.33));
				},{
					holdView.background_(Color.white);
				});
				if (track.mute.asBoolean, {
					holdView.background_(holdView.background.blend(TXColor.black, 0.25));
				});
				// restore decorator position
				holdParent.decorator.reset;
				holdParent.decorator.left_(holdParametersLeft);
				holdParent.decorator.top_(holdParametersTop);

				//  hide button
				holdView  = Button(holdParent, 10 @ 12)
				.states_([ ["-", TXColor.white, TXColor.sysGuiCol1] ])
				.action_({|view|
					track.hidden = true;
					system.showView;
				});

				holdParent.decorator.shift(-2, 2);

				// track no
				popupTrack = PopUpMenu(holdParent, 50 @ 20)
				.background_(TXColor.white).stringColor_(TXColor.black)
				.items_(
					["tr " ++ track.trackNo.asString]
					++ (1 .. arrTracks.size).collect({ arg item, i; ">" ++ item.asString;});
				)
				.action_({arg view;
					holdModule.changeTrackIndex(track, view.value - 1);
					// update view
					holdModule.buildGuiSpecArray;
					system.showView;
				});

				// name
				textboxName = TextField(holdParent, Rect(0, 0, 100, 20),
					" ", TXColor.black, TXColor.white,
					TXColor.black, TXColor.white, 4);
				textboxName.action = {arg view;
					track.name = view.string;
					holdModule.buildGuiSpecArray;
					system.showView;
				};
				textboxName.string = track.name;

				// track type
				labelboxType = StaticText(holdParent, Rect(0, 0, 52, 20))
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.grey7)
				.align_(\centre)
				.string_(track.trackType.asString);

				//  TrackID
				labelTrackID = StaticText(holdParent, 40 @ 20)
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.grey7)
				.align_(\centre)
				.string_("id:" ++ track.trackID.asString);

				// edit btn
				btnEdit = Button(holdParent, 30 @ 20)
				.states_([
					["Edit", TXColor.white, TXColor.sysGuiCol1]
				])
				.action_({|view|
					var holdNewActionStep;
					holdModule.showTrack(trackIndex);
				});

				// delete btn
				btnDelete  = Button(holdParent, 24 @ 20)
				.states_([
					["Del", TXColor.white, TXColor.sysDeleteCol]
				])
				.action_({|view|
					var holdNewActionStep;
					holdModule.deleteTrack(trackIndex);
				});

				// go to next line
				holdParent.decorator.nextLine.shift(12,0);

				// selected
				TXCheckBox(holdParent, 40 @ 20, "Sel", TXColor.sysEditCol, TXColor.grey(0.9),
					TXColor.white, TXColor.sysEditCol)
				.value_(track.selected ? 0)
				.action_({|view|
					track.selected = view.value;
					system.showView;
				});

				if ((track.trackType == 'Note') || (track.trackType == 'Controller'), {
					// monitor
					chkboxMon = TXCheckBox(holdParent, 45 @ 20, "Mon", TXColor.sysGuiCol2, TXColor.grey(0.9),
						TXColor.white, TXColor.sysGuiCol2)
					.value_(track.monitorInput)
					.action_({|view|
						track.monitorInput = view.value;
						holdModule.buildTrackMonitorFuncs(track, view.value.asBoolean);
					});

					// record
					chkboxRec = TXCheckBox(holdParent, 45 @ 20, "Rec", TXColor.sysRecordCol, TXColor.grey(0.9),
						TXColor.white, TXColor.sysRecordCol)
					.value_(track.recordArmed)
					.action_({|view|
						track.recordArmed = view.value;
						holdModule.buildTrackRecordFuncs(track, view.value.asBoolean);
					});
				}, {
					// add space for other track types
					holdParent.decorator.shift(94);
				});

				// title
				labelboxType = StaticText(holdParent, Rect(0, 0, 36, 20))
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
				.align_(\right)
				.string_("Offset");

				// offset
				numboxOffset = NumberBox(holdParent, 54 @ 20)
				.maxDecimals_(4)
				.background_(TXColor.white)
				.value_(track.offset)
				.action_({|view|
					track.offset = view.value;
					this.setOffsetViewBackCol(view, track.offset);
				});
				this.setOffsetViewBackCol(numboxOffset, track.offset);

				// mute
				chkboxMute = TXCheckBox(holdParent, 48 @ 20, "Mute", TXColor.sysDeleteCol, TXColor.grey(0.9),
					TXColor.white, TXColor.sysDeleteCol)
				.value_(track.mute)
				.action_({|view|
					track.mute = view.value;
					system.showView;
				});

				// Duplicate
				Button(holdParent, 24 @ 20)
				.states_([
					["Dup", TXColor.white, TXColor.sysGuiCol2]
				])
				.action_({|view|
					holdModule.duplicateTrack(track);
					holdModule.buildGuiSpecArray;
					system.showView;
				});

				// decorator shift
				holdParent.decorator.shift(0, -24);

				// track events
				holdMaxDisplayBeats = holdModule.getSynthArgSpec("maxDisplayBars") * beatsPerBar;
				viewTrackEvents = ScaledUserView( holdParent, scaledUserViewWidth @ 44, Rect(0,0,holdMaxDisplayBeats, 1) );
				if (track.mute.asBoolean, {
					viewTrackEvents.background = TXColor.white.blend(TXColor.black, 0.25);
				}, {
					viewTrackEvents.background = TXColor.white;
				});
				//viewTrackEvents.view.resize = 5;
				viewTrackEvents.refresh;
				viewTrackOverlay = ScaledUserView(viewTrackEvents.view, scaledUserViewWidth @ 44, Rect(0,0,holdMaxDisplayBeats, 1) );
				viewTrackOverlay.background = TXColor.white.alpha(0);
				// save views
				holdModule.scaledUserViews[track.trackID] = [viewTrackEvents, viewTrackOverlay];
				holdModule.registerCurrentTimeView(viewTrackOverlay);

				viewTrackOverlay.drawFunc = {arg view;
					var fixWidth = view.pixelScale.x;
					var fixWidth2 = fixWidth * 2;
					var selectStart, selectEnd;
					// draw  current play time box
					Pen.fillColor = Color.red.alpha_(0.75);
					Pen.addRect(Rect(holdModule.currentPlayTime - fixWidth, 0, fixWidth2, 127 ));
					Pen.fill;
					// draw yellow selection box
					if (track.selected == 1, {
						selectStart = holdModule.getSynthArgSpec("multiSelectionStart") ? 0;
						selectEnd = holdModule.getSynthArgSpec("multiSelectionEnd") ? 0;
						Pen.strokeColor = Color.yellow.alpha_(0.5);
						Pen.fillColor = Color.yellow.alpha_(0.2);
						Pen.width = 0.01;
						Pen.addRect(Rect(selectStart, 0, selectEnd - selectStart, 127, ));
						Pen.fillStroke;
					});
				};
				viewTrackOverlay.mouseDownAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
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
						viewSelectionBar.refresh;
					},{
						if ((mod == 131072) , {   // shift button
							if ((abs(holdModule.getSynthArgSpec("multiSelectionStart") - scaledX) < 0.05) && (scaledY > 5), {
								dragMode = 'dragSelectionStart';
							},{
								if ((abs(holdModule.getSynthArgSpec("multiSelectionEnd") - scaledX) < 0.05) && (scaledY > 5), {
									dragMode = 'dragSelectionEnd';
								},{
									holdModule.setSynthArgSpec("multiSelectionStart", scaledX);
									holdModule.setSynthArgSpec("multiSelectionEnd", scaledX);
									dragMode = 'dragSelectionEnd';
								});
							});
						},{
							this.updateCurrentPlayTime(scaledX);
							dragMode = 'dragCurrentPlayTime';
						});
						this.updateSelectionText;
						holdModule.refreshScaledUserViews;
					});
				};
				viewTrackOverlay.mouseMoveAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
					if (isInside == false, {
						scaledX = scaledX.clip(viewTrackOverlay.viewRect.left,
							viewTrackOverlay.viewRect.left+viewTrackOverlay.viewRect.width);
					});
					case {dragMode == 'dragSelectionStart'}   {
						if (scaledX < holdModule.getSynthArgSpec("multiSelectionEnd"), {
							holdModule.setSynthArgSpec("multiSelectionStart", scaledX);
						},{
							holdModule.setSynthArgSpec("multiSelectionEnd", scaledX);
							dragMode = 'dragSelectionEnd';
						});
						this.updateSelectionText;
						holdModule.refreshScaledUserViews;
					}
					{dragMode == 'dragSelectionEnd'}   {
						if (scaledX > holdModule.getSynthArgSpec("multiSelectionStart"), {
							holdModule.setSynthArgSpec("multiSelectionEnd", scaledX);
						},{
							holdModule.setSynthArgSpec("multiSelectionStart", scaledX);
							dragMode = 'dragSelectionStart';
						});
						this.updateSelectionText;
						holdModule.refreshScaledUserViews;
					}
					{dragMode == 'dragLoopStart'}   {
						if (scaledX < holdModule.getSynthArgSpec("loopEnd"), {
							holdModule.setSynthArgSpec("loopStart", scaledX);
						},{
							holdModule.setSynthArgSpec("loopEnd", scaledX);
							dragMode = 'dragLoopEnd';
						});
						this.updateLoopText;
						this.updateCurrentPlayTimeViews;
					}
					{dragMode == 'dragLoopEnd'}   {
						if (scaledX > holdModule.getSynthArgSpec("loopStart"), {
							holdModule.setSynthArgSpec("loopEnd", scaledX);
						},{
							holdModule.setSynthArgSpec("loopStart", scaledX);
							dragMode = 'dragLoopStart';
						});
						this.updateLoopText;
						this.updateCurrentPlayTimeViews;
					}
					{dragMode == 'dragCurrentPlayTime'}   {
						this.updateCurrentPlayTime(scaledX);
						this.updateCurrentPlayTimeViews;
					}
					;
				};
				viewTrackOverlay.mouseUpAction = {arg view, scaledX, scaledY, mod, x, y, isInside;
					var holdTimeSnapped;
					if (isInside == false, {
						scaledX = scaledX.clip(viewTrackOverlay.viewRect.left,
							viewTrackOverlay.viewRect.left+viewTrackOverlay.viewRect.width);
					});
					holdTimeSnapped = holdModule.toolFuncs.getSelectionCheckSnap(holdModule, scaledX);
					case {dragMode == 'dragSelectionStart'}   {
						if (scaledX < holdModule.getSynthArgSpec("multiSelectionEnd"), {
							holdModule.setSynthArgSpec("multiSelectionStart", holdTimeSnapped);
							holdModule.setSynthArgSpec("multiSelectionEnd",
								holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("multiSelectionEnd")));
						},{
							holdModule.setSynthArgSpec("multiSelectionEnd", holdTimeSnapped);
							holdModule.setSynthArgSpec("multiSelectionStart",
								holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("multiSelectionStart")));
						});
						this.updateSelectionText;
						holdModule.refreshScaledUserViews;
					}
					{dragMode == 'dragSelectionEnd'}   {
						if (scaledX > holdModule.getSynthArgSpec("multiSelectionStart"), {
							holdModule.setSynthArgSpec("multiSelectionEnd", holdTimeSnapped);
							holdModule.setSynthArgSpec("multiSelectionStart",
								holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("multiSelectionStart")));
						},{
							holdModule.setSynthArgSpec("multiSelectionStart", holdTimeSnapped);
							holdModule.setSynthArgSpec("multiSelectionEnd",
								holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("multiSelectionEnd")));
						});
						this.updateSelectionText;
						holdModule.refreshScaledUserViews;
					}
					{dragMode == 'dragLoopStart'}   {
						if (scaledX < holdModule.getSynthArgSpec("loopEnd"), {
							holdModule.setSynthArgSpec("loopStart", holdTimeSnapped);
							holdModule.setSynthArgSpec("loopEnd",
								holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("loopEnd")));
						},{
							holdModule.setSynthArgSpec("loopEnd", holdTimeSnapped);
							holdModule.setSynthArgSpec("loopStart",
								holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("loopStart")));
						});
						this.updateLoopText;
						this.updateCurrentPlayTimeViews;
					}
					{dragMode == 'dragLoopEnd'}   {
						if (scaledX > holdModule.getSynthArgSpec("loopStart"), {
							holdModule.setSynthArgSpec("loopEnd", holdTimeSnapped);
							holdModule.setSynthArgSpec("loopStart",
								holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("loopStart")));
						},{
							holdModule.setSynthArgSpec("loopStart", holdTimeSnapped);
							holdModule.setSynthArgSpec("loopEnd",
								holdModule.toolFuncs.getSelectionCheckSnap(holdModule, holdModule.getSynthArgSpec("loopEnd")));
						});
						this.updateLoopText;
						this.updateCurrentPlayTimeViews;
					}
					{dragMode == 'dragCurrentPlayTime'}   {
						this.updateCurrentPlayTime(holdTimeSnapped);
						this.updateCurrentPlayTimeViews;
					}
					;
					dragMode = nil;
				};

				if (track.trackType == 'Controller', {
					viewTrackEvents.gridMode = [\lines, \lines];
					viewTrackEvents.gridLines = [ holdModule.getSynthArgSpec("maxDisplayBars"), 0 ];  // number of lines for H and V
					viewTrackEvents.drawFunc = {arg view;
						var holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
						var holdDisplayRangeStart = (holdModule.getSynthArgSpec("displayRangeStart")-1) * holdBeatsPerBar;
						var holdDisplayRangeEnd = (holdModule.getSynthArgSpec("displayRangeEnd")-1) * holdBeatsPerBar;
						// draw all controller events
						track.arrControllerVals.do({arg holdControllerEvent, i;
							var startTime, endTime, controlVal;
							var fixWidth = view.pixelScale.x;
							var fixHeight = view.pixelScale.y;
							controlVal = 1 - holdControllerEvent[1];
							// // if first event, add extra line from start
							// if (i==0, {
							// 	startTime = 0;
							// 	endTime = holdControllerEvent[0];
							// 	Pen.width = 0.03;
							// 	Pen.line(startTime@0, endTime@0).stroke;
							// });
							startTime = holdControllerEvent[0];
							// if last item, draw to end
							if (i == (track.arrControllerVals.size - 1), {
								endTime = holdMaxDisplayBeats;
							}, {
								endTime = track.arrControllerVals[i+1][0];
							});
							if ((endTime > holdDisplayRangeStart) && (startTime < holdDisplayRangeEnd), {
								// draw rect
								//Pen.fillColor = Color.grey(0.85, 0.5);
								Pen.fillColor = TXColor.sysGuiCol1.copy.alpha_(0.3);
								Pen.fillRect(Rect(startTime, controlVal, endTime-startTime, 127-controlVal));
								// draw lines
								Pen.strokeColor = TXColor.sysGuiCol1.alpha(0.5);
								Pen.width = 0.003;
								// Pen.line(startTime@controlVal, startTime@127).stroke;
								// Pen.line(endTime@controlVal, endTime@127).stroke;
								// Pen.strokeColor = TXColor.sysGuiCol1;
								// Pen.width = 0.03;
								// Pen.line(startTime@controlVal, endTime@controlVal).stroke;
								Pen.fillColor = TXColor.sysGuiCol1;
								Pen.addOval(Rect(startTime@controlVal, fixWidth,  fixHeight)).fill;
							});
						});
					};
				});
				if (track.trackType == 'Note', {
					viewTrackEvents.gridMode = [\lines, \lines];
					viewTrackEvents.gridLines = [ holdModule.getSynthArgSpec("maxDisplayBars"), 0 ];  // number of lines for H and V
					viewTrackEvents.drawFunc = {arg view;
						var selectStart, selectEnd;
						var holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
						var holdDisplayRangeStart = (holdModule.getSynthArgSpec("displayRangeStart")-1) * holdBeatsPerBar;
						var holdDisplayRangeEnd = (holdModule.getSynthArgSpec("displayRangeEnd")-1) * holdBeatsPerBar;
						// draw all Note events
						track.arrNotes.do({arg holdNoteEvent, i;
							var startTime, endTime, noteVal, vel;
							noteVal = 1 - (holdNoteEvent[1] / 127);
							startTime = holdNoteEvent[0];
							endTime = holdNoteEvent[4];
							if ((endTime > holdDisplayRangeStart) && (startTime < holdDisplayRangeEnd), {
								// draw line
								vel = holdNoteEvent[2]/127;
								if (vel < 0.5, {
									Pen.strokeColor = TXColor.green(0.5).blend(TXColor.yellow, vel * 2);
								},{
									Pen.strokeColor = TXColor.yellow.blend(TXColor.red, (vel * 2) - 1);
								});
								Pen.width = 0.03;
								Pen.line(startTime@noteVal, endTime@noteVal).stroke;
							});
						});
					};
				});

			},{ // if hidden
				// store decorator position
				holdParametersLeft = holdParent.decorator.left;
				holdParametersTop = holdParent.decorator.top;

				// parameters box
				holdView = StaticText(holdParent, Rect(0, 0, trackParametersWidth, 8));
				if (track.selected.asBoolean, {
					holdView.background_(Color.white.blend(Color.yellow, 0.2));
				},{
					holdView.background_(Color.white);
				});
				if (track.mute.asBoolean, {
					holdView.background_(holdView.background.blend(TXColor.black, 0.35));
				});
				// events box
				// holdView = StaticText(holdParent, Rect(0, 0, scaledUserViewWidth, 6));
				// if (track.mute.asBoolean, {
				// 	holdView.background_(TXColor.white.blend(TXColor.black, 0.35));
				// 	}, {
				// 		holdView.background_(TXColor.white);
				// });
				// if (track.selected.asBoolean, {
				// 	holdView.background_(holdView.background.blend(Color.yellow,0.2));
				// });

				holdView = ScaledUserView( holdParent, scaledUserViewWidth @ 8, Rect(0,0,holdMaxDisplayBeats, 1) );
				if (track.mute.asBoolean, {
					holdView.background = TXColor.grey(0.6);
				}, {
					holdView.background = TXColor.white;
				});
				holdMaxDisplayBeats = holdModule.getSynthArgSpec("maxDisplayBars") * beatsPerBar;
				holdView.drawFunc = {arg view;
					var selectStart = holdModule.getSynthArgSpec("multiSelectionStart") ? 0;
					var selectEnd = holdModule.getSynthArgSpec("multiSelectionEnd") ? 0;
					// draw yellow selection box
					if (track.selected == 1, {
						selectStart = holdModule.getSynthArgSpec("multiSelectionStart") ? 0;
						selectEnd = holdModule.getSynthArgSpec("multiSelectionEnd") ? 0;
						Pen.strokeColor = Color.yellow.alpha_(0.5);
						Pen.fillColor = Color.yellow.alpha_(0.2);
						Pen.width = 0.01;
						Pen.addRect(Rect(selectStart, 0, selectEnd - selectStart, 1, ));
						Pen.fillStroke;
					});
				};
				holdView.refresh;
				// save view
				holdModule.scaledUserViews[track.trackID] = holdView;

				// restore decorator position
				holdParent.decorator.reset;
				holdParent.decorator.left_(holdParametersLeft);
				holdParent.decorator.top_(holdParametersTop);

				//unhide button
				holdView  = Button(holdParent, 10 @ 12)
				.states_([ ["+", TXColor.white, TXColor.sysGuiCol1] ])
				.action_({|view|
					track.hidden = false;
					system.showView;
				});

				holdParent.decorator.shift(10,2);

				if (track.selected.asBoolean, {
					holdView = StaticText(holdParent, 25 @ 4).background_(TXColor.sysEditCol);
				}, {
					holdParent.decorator.shift(29, 0);
				});

				holdParent.decorator.shift(17,0);

				if (track.monitorInput.asBoolean, {
					holdView = StaticText(holdParent, 25 @ 4).background_(TXColor.sysGuiCol2.blend(TXColor.black,0.3));
				}, {
					holdParent.decorator.shift(29, 0);
				});

				holdParent.decorator.shift(20,0);

				if (track.recordArmed.asBoolean, {
					holdView = StaticText(holdParent, 25 @ 4).background_(TXColor.sysRecordCol);
				}, {
					holdParent.decorator.shift(29, 0);
				});

				holdParent.decorator.shift(19 + 40 + 58, 0);

				if (track.mute.asBoolean, {
					holdView = StaticText(holdParent, 28 @ 4).background_(TXColor.sysDeleteCol);
				}, {
					holdParent.decorator.shift(32, 0);
				});

				holdParent.decorator.nextLine.shift(0, -10);

			});  // end of if hidden

			// spacer
			holdParent.decorator.nextLine.shift(0, 2);

		}); // end of arrTracks.do

		argParent.decorator.nextLine;

		// draw white line
		StaticText(argParent, 1070 @ 1).background_(TXColor.white);
		argParent.decorator.shift(-1074, 2);

		// Show Processes btn
		btnShowProcesses = Button(argParent, 17 @ 15)
		.states_([
			["^", TXColor.white, TXColor.sysGuiCol2],
			["v", TXColor.white, TXColor.sysGuiCol2]
		])
		.value_(holdModule.showMultitrackProcesses)
		.action_({|view|
			holdModule.showMultitrackProcesses = view.value;
			system.showView;
		});

		argParent.decorator.nextLine;

		// MultiTrack Processes
		arrProcessFuncs = holdModule.toolFuncs.getMultiTrackProcesses;
		arrProcessStrings = arrProcessFuncs.collect({arg item; item[0]});
		if (holdModule.showMultitrackProcesses == 1, {
			holdView = TXListViewLabelPlusMinus(argParent, 530 @ 128, "Process", arrProcessStrings,
				{arg view; holdModule.setSynthArgSpec("actionIndex", view.value);
					holdModule.setDefaultActionVariables;
					system.showView;},
				0, false, 46);
		}, {
			holdView = TXPopupPlusMinus(argParent, 530 @ 20, "Process", arrProcessStrings,
				{arg view; holdModule.setSynthArgSpec("actionIndex", view.value);
					holdModule.setDefaultActionVariables;
					system.showView;},
				0, false, 46);
			holdView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
		});
		holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		holdView.valueNoAction_(holdModule.getSynthArgSpec("actionIndex"));

		// Run Process btn
		Button(argParent, 80 @ 20)
		.states_([
			["Run Process", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			arrProcessFuncs[holdModule.getSynthArgSpec("actionIndex")][1].value;
			system.showView;
		});

		argParent.decorator.shift(6, 0);

		// add sliders or popup if needed
		holdProcess = arrProcessFuncs[holdModule.getSynthArgSpec("actionIndex")];
		if (holdProcess[2][0].notNil, {
			argParent.decorator.shift(6, 0);
			if (holdProcess[3][0].class == ControlSpec, {
				holdView = TXSlider(argParent, 240 @ 20, holdProcess[2][0], holdProcess[3][0],
					{arg view; holdModule.setSynthArgSpec("actionVar1", view.value)},
					holdModule.getSynthArgSpec("actionVar1"), false, 90, 40);
				holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			});
			if (holdProcess[3][0].isArray, {
				if (holdModule.showMultitrackProcesses == 1, {
					holdView = TXListViewLabelPlusMinus(argParent, 215 @ 128, holdProcess[2][0], holdProcess[3][0],
						{arg view; holdModule.setSynthArgSpec("actionVar1", view.value)},
						holdModule.getSynthArgSpec("actionVar1"), false, 46);
				}, {
					holdView = TXPopupPlusMinus(argParent, 215 @ 20, holdProcess[2][0], holdProcess[3][0],
						{arg view; holdModule.setSynthArgSpec("actionVar1", view.value)},
						holdModule.getSynthArgSpec("actionVar1"), false, 46);
					holdView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
				});
				holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			});
		});
		if (holdProcess[2][1].notNil, {
			argParent.decorator.shift(6, 0);
			if (holdProcess[3][1].class == ControlSpec, {
				holdView = TXSlider(argParent, 240 @ 20, holdProcess[2][1], holdProcess[3][1],
					{arg view; holdModule.setSynthArgSpec("actionVar2", view.value)},
					holdModule.getSynthArgSpec("actionVar2"), false, 90, 40);
				holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			});
			if (holdProcess[3][1].isArray, {
				if (holdModule.showMultitrackProcesses == 1, {
					holdView = TXListViewLabelPlusMinus(argParent, 215 @ 128, holdProcess[2][1], holdProcess[3][1],
						{arg view; holdModule.setSynthArgSpec("actionVar2", view.value)},
						holdModule.getSynthArgSpec("actionVar2"), false, 46);
				}, {
					holdView = TXPopupPlusMinus(argParent, 215 @ 20, holdProcess[2][1], holdProcess[3][1],
						{arg view; holdModule.setSynthArgSpec("actionVar2", view.value)},
						holdModule.getSynthArgSpec("actionVar2"), false, 46);
					holdView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
				});
				holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			});
		});

		holdModule.updateScaledUserViews;

	} // end of init

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
		if (numboxStartPosBars.notNil, {
			if (numboxStartPosBars.labelView.notClosed, {
				holdStartPosBeats = holdModule.getSynthArgSpec("startPosBeats");
				holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
				holdStartPosBars = (holdStartPosBeats / holdBeatsPerBar).floor;
				holdStartPosSoloBeats = holdStartPosBeats - (holdStartPosBars * holdBeatsPerBar);
				numboxStartPosBars.value = 1+ holdStartPosBars;
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
		if (numboxStopPosBars.notNil, {
			if (numboxStopPosBars.labelView.notClosed, {
				holdStopPosBeats = holdModule.getSynthArgSpec("stopPosBeats");
				holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
				holdStopPosBars = (holdStopPosBeats / holdBeatsPerBar).floor;
				holdStopPosSoloBeats = holdStopPosBeats - (holdStopPosBars * holdBeatsPerBar);
				numboxStopPosBars.value = 1+ holdStopPosBars;
				numboxStopPosBeats.value = 1+ holdStopPosSoloBeats;
			});
		});
	}

	updateLoopText {
		var holdBeatsPerBar, holdLoopStartBeats, holdLoopStartSoloBeats, holdLoopStartBars;
		var holdLoopEndBeats, holdLoopEndSoloBeats, holdLoopEndBars;
		if (numboxLoopStartBars.notNil, {
			if (numboxLoopStartBars.labelView.notClosed, {
				holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
				holdLoopStartBeats = holdModule.getSynthArgSpec("loopStart");
				holdLoopStartBars = (holdLoopStartBeats / holdBeatsPerBar).floor;
				holdLoopStartSoloBeats = holdLoopStartBeats - (holdLoopStartBars * holdBeatsPerBar);
				holdLoopEndBeats = holdModule.getSynthArgSpec("loopEnd");
				holdLoopEndBars = (holdLoopEndBeats / holdBeatsPerBar).floor;
				holdLoopEndSoloBeats = holdLoopEndBeats - (holdLoopEndBars * holdBeatsPerBar);
				numboxLoopStartBars.value = 1+ holdLoopStartBars;
				numboxLoopStartBeats.value = 1+ holdLoopStartSoloBeats;
				numboxLoopEndBars.value = 1+ holdLoopEndBars;
				numboxLoopEndBeats.value = 1+ holdLoopEndSoloBeats;
			});
		});
	}

	updateSelectionText {
		var holdBeatsPerBar, holdSelectionStartBeats, holdSelectionStartSoloBeats, holdSelectionStartBars;
		var holdSelectionEndBeats, holdSelectionEndSoloBeats, holdSelectionEndBars;
		if (numboxSelectionStartBars.notNil, {
			if (numboxSelectionStartBars.labelView.notClosed, {
				holdBeatsPerBar = holdModule.getSynthArgSpec("beatsPerBar");
				holdSelectionStartBeats = holdModule.getSynthArgSpec("multiSelectionStart");
				holdSelectionStartBars = (holdSelectionStartBeats / holdBeatsPerBar).floor;
				holdSelectionStartSoloBeats = holdSelectionStartBeats - (holdSelectionStartBars * holdBeatsPerBar);
				holdSelectionEndBeats = holdModule.getSynthArgSpec("multiSelectionEnd");
				holdSelectionEndBars = (holdSelectionEndBeats / holdBeatsPerBar).floor;
				holdSelectionEndSoloBeats = holdSelectionEndBeats - (holdSelectionEndBars * holdBeatsPerBar);
				numboxSelectionStartBars.value = 1+ holdSelectionStartBars;
				numboxSelectionStartBeats.value = 1+ holdSelectionStartSoloBeats;
				numboxSelectionEndBars.value = 1+ holdSelectionEndBars;
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
		var holdStart = holdModule.getSynthArgSpec("displayRangeStart");
		var holdEnd = holdModule.getSynthArgSpec("displayRangeEnd");
		displayRangeSlider.controlSpec = ControlSpec(1, holdModule.getSynthArgSpec("maxDisplayBars") + 1);
		displayRangeSlider.valueBoth = [holdStart,holdEnd];
	}

	setOffsetViewBackCol {arg inView, inOffset;
		if (inOffset == 0, {
			inView.background_(TXColor.white);
		}, {
			inView.background_(TXColor.paleTurquoise);
		});
	}

}
