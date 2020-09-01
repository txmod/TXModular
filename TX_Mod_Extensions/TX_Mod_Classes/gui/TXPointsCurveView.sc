// Copyright (C) 2019  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXPointsCurveView {	// for curve drawing with 2-8 points
	classvar <>classData;
	var <>localData, <>localOptions;
	var <>action, value;
	var <labelView, <dragView, <userView, <infoView;
	var <seqRestartButton, <seqNextPopup, <quantisePopup, <editModePopup, <editModeListView;
	var <drawModeBtn, <histNextBtn, <histPrevBtn, <showPointNamesBtn, <showImageBtn, <realignBtn;
	var gridRows, gridCols, holdDrawCurve, isDrawing, resetCurve, userViewWidth, userViewHeight;
	var tempData, holdMouseData, holdLastDrawPoint, holdLastX, holdLastY, holdView, showGridBtn, showPointsBtn;
	var validPointKeys, curveKeyGroups, lineKeyGroups, localKeyGroups, controlPointGroups, dragPointKey;

	*initClass{
		classData = ();
		classData.pointSymbols = [
			'acstart',
			'p1d',
			'p2c1', 'p2c2', 'p2d',
			'p3c1', 'p3c2', 'p3d',
			'p4c1', 'p4c2', 'p4d',
			'p5c1', 'p5c2', 'p5d',
			'p6c1', 'p6c2', 'p6d',
			'p7c1', 'p7c2', 'p7d',
			'p8c1', 'p8c2', 'p8d',
			'acend',
		];
		classData.drawPointKeys = [
			'p1d',
			'p2d',
			'p3d',
			'p4d',
			'p5d',
			'p6d',
			'p7d',
			'p8d',
		];
	}

	*new { arg window, dimensions, label, action, initVal, initAction=false, labelWidth=124,
		showPresets, curveWidth=260, curveHeight=260, dataEvent, optionsEvent, backingImageFileName;

		^super.new.init(window, dimensions, label, action, initVal, initAction, labelWidth,
			showPresets, curveWidth, curveHeight, dataEvent, optionsEvent, backingImageFileName);
	}

	init { arg window, dimensions, label, argAction, initVal, initAction, labelWidth,
		showPresets, curveWidth, curveHeight, dataEvent, optionsEvent, backingImageFileName;

		var arrGenFunctions, arrModifyFunctions, popItems, popAction;
		var holdTop, holdLeft, columnView, curveBackground;
		var dragStartPoint, dragEndPoint;

		// init
		tempData = ();
		localData = ();
		holdMouseData = ();
		holdMouseData.scaledXPos = 0.0;
		holdMouseData.scaledYPos = 0.0;
		holdMouseData.xPos = 0.0;
		holdMouseData.yPos = 0.0;
		holdMouseData.mouseIsDown = false;


		// use local data if Event passed
		if (dataEvent.notNil and: {dataEvent.class == Event}, {
			localData = dataEvent;
		});
		// add default data if empty
		localData.editMode = 'editPoints'; // ['editPoints', 'drawSeq', 'move', 'x_zoom', 'y_zoom', 'zoom', 'rotate']
		localData.maxHistorySize = localData.maxHistorySize ? 100;
		localData.curveHistory = localData.curveHistory ? [];
		localData.curveHistIndex = localData.curveHistIndex ? 0;
		localData.gridRows = localData.gridRows ? 2;
		localData.gridCols = localData.gridCols ? 2;
		localData.lastPointKey = localData.lastPointKey ? 'p8d';
		localData.currSeqStepIndex = 0;

		// use options if Event passed
		if (optionsEvent.notNil and: {optionsEvent.class == Event}, {
			localOptions = optionsEvent;
		});
		// add default data if empty
		// arrcurveTypes = [\autoCurve, \straightLine, \BezierCP1, \BezierCP1Smooth, \BezierCP1CP2, ]
		localOptions.showGrid = localOptions.showGrid ? true;
		localOptions.showPoints = localOptions.showPoints ? true;
		localOptions.showPointNames = localOptions.showPointNames ? false;
		localOptions.autoQuantise = localOptions.autoQuantise ? false;
		localOptions.lockEndToStart = localOptions.lockEndToStart ? false;
		localOptions.showImage = localOptions.showImage ? true;

		resetCurve = this.getDefaultCurve;
		localData.arrSlotData = localData.arrSlotData ? ({this.getDefaultCurve} ! 5);
		action = argAction;
		gridRows = localData.gridRows ? 2;
		gridCols = localData.gridCols ? 2;
		userViewWidth = curveWidth - 4;
		userViewHeight = curveHeight - 4;
		isDrawing = false;
		curveBackground = Color.grey(0.7);
		this.setBackgroundImage(backingImageFileName);

		// label
		labelView = StaticText(window, labelWidth @ 20);
		labelView.string = label;
		labelView.stringColor = TXColor.sysGuiCol1;
		labelView.background = TXColor.white;
		labelView.align = \right;

		/* OLD
		// info text
		infoView = StaticText(window, userViewWidth @ 20);
		infoView.string = "";
		infoView.stringColor = TXColor.white;
		// infoView.stringColor = TXColor.grey1;
		// infoView.background = TXColor.white;
		// infoView.background = TXColor.sysGuiCol1.blend(Color.white, 0.9);
		//infoView.background = TXColor.paleTurquoise;
		infoView.background = TXColor.sysHelpCol.blend(TXColor.grey7, 0.5);
		infoView.align = \center;
		this.updateInfoView;
		*/

		// Edit Mode Popup
		editModePopup = PopUpMenu(window, userViewWidth @ 20);
		// editModePopup.background = TXColor.sysHelpCol.blend(TXColor.grey6, 0.4);
		// editModePopup.stringColor = TXColor.white;
		editModePopup.background = Color.new255(255, 169, 100).blend(TXColor.white, 0.8);
		editModePopup.stringColor = TXColor.black;
		editModePopup.items_([
			"edit points - click to drag each point. Shift + drag to move linked points together. Ctrl + drag to align opposite control points",
			"draw points in sequence - click and drag each point into position sequentially",
			"move all points - click and drag to move",
			"x-zoom all points - click and drag to zoom horizontally",
			"y-zoom all points - click and drag to zoom vertically",
			"zoom all points - click and drag to zoom. press shift key to zoom x & y axes independently",
			"rotate all points - click and drag to rotate. press shift key to rotate in steps of 5 degrees",
		]);
		editModePopup.value_(['editPoints', 'drawSeq', 'move', 'x_zoom', 'y_zoom', 'zoom', 'rotate'].indexOf(localData.editMode));
		editModePopup.action_({arg view;
			localData.editMode = ['editPoints', 'drawSeq','move', 'x_zoom', 'y_zoom', 'zoom', 'rotate'][view.value];
			this.showHide_SeqViews;
			userView.refresh;
			// this.updateInfoView;
			editModeListView.value = view.value;
		});
		editModePopup.keyDownAction = { |view, char|
			case
			{char == $e} {this.setEditMode('editPoints');}
			{char == $d} {this.setEditMode('drawSeq');}
			{char == $m} {this.setEditMode('move');}
			{char == $x} {this.setEditMode('x_zoom');}
			{char == $y} {this.setEditMode('y_zoom');}
			{char == $z} {this.setEditMode('zoom');}
			{char == $r} {this.setEditMode('rotate');}
			{char == $g} {this.toggleShowGrid; true;}
			{char == $p} {this.toggleShowPoints; true;}
			{char == $n} {this.toggleShowPointNames; true}
			;
		};

		// decorator next line
		window.asView.decorator.nextLine;

		// columnView
		columnView = CompositeView(window, labelWidth @ userViewHeight);
		columnView.decorator = FlowLayout(columnView.bounds);
		columnView.decorator.margin = Point(0,0);
		columnView.decorator.gap.x = 2;
		columnView.decorator.gap.y = 2;
		columnView.decorator.reset;

		columnView.decorator.shift(0,6);

		// <> buttons
		histPrevBtn = Button(columnView, (labelWidth * 0.5) -1 @ 16)
		.states_([["< undo", TXColor.paleYellow, TXColor.sysGuiCol1]])
		.action_({
			this.loadCurveHistoryPrev;
		});
		histNextBtn = Button(columnView, (labelWidth * 0.5) -1 @ 16)
		.states_([["redo >", TXColor.paleYellow, TXColor.sysGuiCol1]])
		.action_({
			this.loadCurveHistoryNext;
		});
		columnView.decorator.nextLine.shift(0,4);
		this.coloriseHistoryButtons;

		/*
		// removed because doesn't update curve type, num point, auto-close, etc.
		// Drag box
		dragView = DragBoth(columnView, labelWidth @ 26);
		dragView.setBoth = false;
		dragView.string = "drag & drop curve";
		dragView.align = \center;
		dragView.background_(TXColor.paleYellow);
		dragView.stringColor_(TXColor.sysGuiCol1);
		dragView.canReceiveDragHandler = {View.currentDrag.class == Event;};
		dragView.receiveDragHandler = {arg view;
			var dragObject = View.currentDrag;
			if (dragObject.notNil
				and: {dragObject.class == Event}
				and: {dragObject.acstart.notNil}
				, {
					this.valueAction_(dragObject);
			});
		};
		dragView.beginDragAction = {arg view;
			dragView.object = this.getDragObject;
			dragView.dragLabel = "Points Curve Data" ;
			view.object;
		};
		*/

		columnView.decorator.shift(0,4);

		/*
		=========  NOTES:  =================

		The following drawModeBtn should be changed to editMode ListBox
		editMode: ['dragPoints', 'shiftPoints', 'scalePoints', 'rotatePoints']

		// draw mode
		drawModeBtn = Button(columnView, labelWidth @ 20)
		.states_([
			["< Draw Points", TXColor.white, TXColor.sysGuiCol1],
			["< Draw Lines", TXColor.white, TXColor.sysGuiCol1]])
		.action_({arg view;
			if (view.value == 0, {
				this.setDrawMode('point');
			},{
				this.setDrawMode('line');
			});
		});
		if (this.getDrawMode == 'line', {
			drawModeBtn.value = 1;
		},{
			drawModeBtn.value = 0;
		});
		*/

		// Edit Mode ListView
		editModeListView = ListView(columnView, labelWidth @ 90);
		editModeListView.background = Color.new255(255, 169, 100).blend(TXColor.white, 0.9);
		editModeListView.stringColor = TXColor.black;
		editModeListView.items_(["e - edit points", "d - draw points in seq", "m - move all points",
			"x - x-zoom all points", "y - y-zoom all points", "z - zoom all points", "r - rotate all points"]);
		editModeListView.value_(['editPoints', 'drawSeq', 'move', 'x_zoom', 'y_zoom', 'zoom', 'rotate'].indexOf(localData.editMode));
		editModeListView.action_({arg view;
			localData.editMode = ['editPoints', 'drawSeq','move', 'x_zoom', 'y_zoom', 'zoom', 'rotate'][view.value];
			this.showHide_SeqViews;
			userView.refresh;
			// this.updateInfoView;
			editModePopup.value = view.value;
		});
		editModeListView.keyDownAction = { |view, char|
			case
			{char == $e} {this.setEditMode('editPoints');}
			{char == $d} {this.setEditMode('drawSeq');}
			{char == $m} {this.setEditMode('move');}
			{char == $x} {this.setEditMode('x_zoom');}
			{char == $y} {this.setEditMode('y_zoom');}
			{char == $z} {this.setEditMode('zoom');}
			{char == $r} {this.setEditMode('rotate');}
			{char == $g} {this.toggleShowGrid; true;}
			{char == $p} {this.toggleShowPoints; true;}
			{char == $n} {this.toggleShowPointNames; true}
			;
		};
		columnView.decorator.shift(0,4);

		// Sequence Next
		seqNextPopup = PopUpMenu(columnView, labelWidth @ 20);
		seqNextPopup.action_({arg view;
			localData.currSeqStepIndex = view.value;
		});

		// seq reset
		seqRestartButton = Button(columnView, labelWidth @ 20)
		.states_([["restart draw seq ", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.resetSequence;
		});
		// show/hide view
		this.showHide_SeqViews;

		columnView.decorator.shift(0,8);

		// CheckBox: "lock end to start"
		TXCheckBox.new(columnView, labelWidth @ 20, "lock end to start",
			TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1)
		.value_(localOptions.lockEndToStart.binaryValue)
		.action = {arg view;
			localOptions.lockEndToStart = view.value.asBoolean;
			this.checkLockEndToStart;
			this.updateValidPointKeys;
			userView.refresh;
		};
		columnView.decorator.shift(0,8);

		// quantise
		TXCheckBox.new(columnView, labelWidth @ 20, "auto quantise x,y",
			TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1)
		.value_(localOptions.autoQuantise.binaryValue)
		.action = {arg view;
			localOptions.autoQuantise = view.value.asBoolean;
			if (localOptions.autoQuantise, {
				this.quantiseCurve(true, true);
			});
			userView.refresh;
		};
		columnView.decorator.shift(0,4);
		Button(columnView, labelWidth @ 20)
		.states_([["quantise x,y", TXColor.white, TXColor.sysDeleteCol]])
		.action_({
			this.quantiseCurve(true, true);
		});
		columnView.decorator.shift(0,4);
		Button(columnView, labelWidth @ 20)
		.states_([["quantise X", TXColor.white, TXColor.sysDeleteCol]])
		.action_({
			this.quantiseCurve(true, false);
		});
		columnView.decorator.shift(0,4);
		Button(columnView, labelWidth @ 20)
		.states_([["quantise Y", TXColor.white, TXColor.sysDeleteCol]])
		.action_({
			this.quantiseCurve(false, true);
		});
		columnView.decorator.shift(0,4);


		// create ScaledUserView
		userView = ScaledUserViewContainer(window, userViewWidth @ userViewHeight,
			Rect(-0.005,-0.005,1.005,1.005)
			//Rect(0, 0, 1, 1)
		);
		userView.view.background_(curveBackground);
		userView.drawFunc = {arg view;
			var fixWidth, fixWidth2, fixHeight, fixHeight2, lastIndex;
			var pt1, pt2, holdArray;

			// show image
			if (localOptions.backgroundImage.notNil and: {localOptions.showImage}, {
				localOptions.backgroundImage.drawInRect(Rect(0, 0, 1, 1));
				//localOptions.backgroundImage.drawAtPoint(0 @ 0);
				Pen.color = TXColor.grey(0.7).alpha_(0.5);
				Pen.addRect(Rect(0, 0, 1, 1));
				Pen.fill;
			});
			// create grid
			if (localOptions.showGrid, {
				fixWidth = view.pixelScale.x;
				fixWidth2 = fixWidth * 2;
				fixHeight = view.pixelScale.y;
				fixHeight2 = fixHeight * 2;
				lastIndex = value.size - 1;
				Pen.color = TXColor.white;
				gridRows.do({arg item, i;
					var holdHeight;
					holdHeight = ((i + 1)) / gridRows;
					Pen.addRect(Rect(0, holdHeight, 1, fixHeight));
				});
				gridCols.do({arg item, i;
					var holdWidth;
					holdWidth = ((i + 1)) / gridCols;
					Pen.addRect(Rect(holdWidth, 0, fixWidth, 1));
				});
				Pen.fill;
			});
			// draw curves
			this.drawCurves(view);
		};
		userView.unscaledDrawFunc = {arg view;
			//var lastIndex = value.size - 1;
			//var pt1, pt2, startX, startY, endX, endY, holdArray;
			var xOffset = 0, yOffset = 0, pointString, point;
			// draw mouse point
			if (holdMouseData.mouseIsDown, {
				Pen.color = TXColor.blue;
				if (holdMouseData.xPos > (userViewWidth * 0.5), {
					xOffset = -110;
				}, {
					xOffset = 10;
				});
				if (holdMouseData.yPos < (userViewHeight * 0.5), {
					yOffset = -25;
				}, {
					yOffset = 20;
				});
				if ((localData.editMode == 'editPoints') or: (localData.editMode == 'drawSeq'), {
					pointString = "x: " ++ holdMouseData.scaledXPos.round(0.001).asString ++ "  y: "
					++ holdMouseData.scaledYPos.round(0.001).asString;
					Pen.stringAtPoint(pointString,
						(holdMouseData.xPos + xOffset) @ (userViewHeight - holdMouseData.yPos + yOffset) );
				});
				if (localData.editMode == 'x_zoom', {
					pointString = "x-zoom: " ++ (tempData.scale ? 1).round(0.001).asString;
					Pen.stringAtPoint(pointString,
						(holdMouseData.xPos + xOffset) @ (userViewHeight - holdMouseData.yPos + yOffset) );
				});
				if (localData.editMode == 'y_zoom', {
					pointString = "y-zoom: " ++ (tempData.scale ? 1).round(0.001).asString;
					Pen.stringAtPoint(pointString,
						(holdMouseData.xPos + xOffset) @ (userViewHeight - holdMouseData.yPos + yOffset) );
				});
				if (localData.editMode == 'zoom', {
					if (holdMouseData.isShift, {
						pointString = "zoom x: " ++ (tempData.scaleX ? 1).round(0.001).asString
						++  "   zoom y: " ++ (tempData.scaleY ? 1).round(0.001).asString;
					}, {
						pointString = "zoom: " ++ (tempData.scaleX ? 1).round(0.001).asString;
					});
					Pen.stringAtPoint(pointString,
						(holdMouseData.xPos + xOffset) @ (userViewHeight - holdMouseData.yPos + yOffset) );
				});
				if (localData.editMode == 'rotate', {
					pointString = "rotate: " ++ (tempData.rotate ? 0).round(0.01).asString ++ " degrees";
					Pen.stringAtPoint(pointString,
						(holdMouseData.xPos + xOffset) @ (userViewHeight - holdMouseData.yPos + yOffset) );
				});
			});

			// draw point names
			if (localOptions.showPoints and: localOptions.showPointNames, {
				yOffset = 0;
				validPointKeys.do({arg key, i;
					point = view.translateScale(value[key]);
					if (point.x > (userViewWidth * 0.5), {
						xOffset = -33;
					}, {
						xOffset = 10;
					});
					if (point.y < (userViewHeight * 0.5), {
						yOffset = 2;
					}, {
						yOffset = -10;
					});
					pointString = key.asString;
					if (classData.drawPointKeys.indexOf(key).notNil, {
						Pen.color = Color.blue(0.5);
					}, {
						Pen.color = Color.green(0.5);
					});
					Pen.stringAtPoint(pointString,
						(point.x + xOffset) @ (point.y + yOffset) );
				});
			}, {
				if ( ((localData.editMode == 'editPoints') or: (localData.editMode == 'drawSeq'))and: {dragPointKey.notNil}, {
					point = view.translateScale(value[dragPointKey]);
					if (point.x > (userViewWidth * 0.5), {
						xOffset = -33;
					}, {
						xOffset = 10;
					});
					if (point.y < (userViewHeight * 0.5), {
						yOffset = 2;
					}, {
						yOffset = -10;
					});
					pointString = dragPointKey.asString;
					Pen.color = Color.blue;
					Pen.stringAtPoint(pointString,
						(point.x + xOffset) @ (point.y + yOffset) );

				});
			});
		};
		userView.mouseDownAction = { |v, sx, sy, m, x, y, inside|
			sx = sx.max(0).min(1);
			sy = sy.max(0).min(1);
			holdMouseData.scaledXPos = sx;
			holdMouseData.scaledYPos = sy;
			holdMouseData.xPos = x.max(0).min(userViewWidth);
			holdMouseData.yPos = userViewHeight - y.max(0).min(userViewHeight);
			holdMouseData.mouseIsDown = true;
			holdMouseData.isShift = m.isShift;
			holdMouseData.isCtrl = m.isCtrl;
			holdLastX = sx;
			holdLastY = sy;
			this.applyMouseDown(v, sx, sy);
			userView.refresh;
		};
		userView.mouseMoveAction = { |v, sx, sy, m, x, y, inside|
			sx = sx.max(0).min(1);
			sy = sy.max(0).min(1);
			holdMouseData.scaledXPos = sx;
			holdMouseData.scaledYPos = sy;
			holdMouseData.xPos = x.max(0).min(userViewWidth);
			holdMouseData.yPos = userViewHeight - y.max(0).min(userViewHeight);
			holdMouseData.isShift = m.isShift;
			holdMouseData.isCtrl = m.isCtrl;
			this.applyMouseMove(v, sx, sy);
			holdLastX = sx;
			holdLastY = sy;
			userView.refresh;
		};
		userView.mouseUpAction = { |v, sx, sy, m, x, y, inside|
			sx = sx.max(0).min(1);
			sy = sy.max(0).min(1);
			holdMouseData.mouseIsDown = false;
			holdMouseData.isShift = false;
			holdMouseData.isCtrl = false;
			this.applyMouseUp(v, sx, sy);
		};
		userView.keyDownAction = { |view, char|
			case
			{char == $e} {this.setEditMode('editPoints');}
			{char == $d} {this.setEditMode('drawSeq');}
			{char == $m} {this.setEditMode('move');}
			{char == $x} {this.setEditMode('x_zoom');}
			{char == $y} {this.setEditMode('y_zoom');}
			{char == $z} {this.setEditMode('zoom');}
			{char == $r} {this.setEditMode('rotate');}
			{char == $g} {this.toggleShowGrid; true;}
			{char == $p} {this.toggleShowPoints; true;}
			{char == $n} {this.toggleShowPointNames; true}
			;
		};

		// decorator store settings
			holdTop = window.asView.decorator.top;
			holdLeft = window.asView.decorator.left;

		// CheckBox: "Show Grid"
		showGridBtn = TXCheckBox.new(window, 140 @ 20, "g - show grid",
			TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
		showGridBtn.value = localOptions.showGrid.binaryValue;
		showGridBtn.action = {arg view;
			localOptions.showGrid = view.value.asBoolean;
			userView.refresh;
		};
		window.asView.decorator.shift(-144, 28);

		// CheckBox: "Show Points"
		showPointsBtn = TXCheckBox.new(window, 140 @ 20, "p - show points",
			TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
		showPointsBtn.value = localOptions.showPoints.binaryValue;
		showPointsBtn.action = {arg view;
			localOptions.showPoints = view.value.asBoolean;
			if (localOptions.showPoints, {
				showPointNamesBtn.visible = true;
			}, {
				showPointNamesBtn.visible = false;
			});
			userView.refresh;
		};
		window.asView.decorator.shift(-144, 28);

		// CheckBox: "Show Point Names"
		showPointNamesBtn = TXCheckBox.new(window, 140 @ 20, "n - show point names",
			TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
		showPointNamesBtn.value = localOptions.showPointNames.binaryValue;
		showPointNamesBtn.action = {arg view;
			localOptions.showPointNames = view.value.asBoolean;
			userView.refresh;
		};
		if (localOptions.showPoints.not, {
			showPointNamesBtn.visible = false;
		});
		window.asView.decorator.shift(-144, 28);

		// CheckBox: "Show Image"
		showImageBtn = TXCheckBox.new(window, 140 @ 20, "show image",
			TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
		showImageBtn.value = localOptions.showImage.binaryValue;
		showImageBtn.action = {arg view;
			localOptions.showImage = view.value.asBoolean;
			userView.refresh;
		};
		if (localOptions.imageFileName.isNil, {
			showImageBtn.visible = false;
		});
		window.asView.decorator.shift(-144, 38);

		// create button
		Button(window, 120 @ 20)
		.states_([["reset to default curve", TXColor.white, TXColor.sysDeleteCol]])
		.action_({
			this.reset;
		});
		// decorator shift
		window.asView.decorator.shift(-124, 28);

		// create button
		realignBtn = Button(window, 120 @ 20)
		.states_([["realign control points", TXColor.white, TXColor.sysDeleteCol]])
		.action_({
			this.realignControlPoints;
		});
		// decorator shift
		window.asView.decorator.shift(-124, 28);

		// create button
		Button(window, 120 @ 20)
		.states_([["zoom out", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			userView.scale_([1, 1]);
			userView.move_([0.5, 0.5]);
		});
		// decorator shift
		window.asView.decorator.shift(-124, 38);
		// create slot load and save buttons
		5.do({ arg i;
			Button(window, 50 @ 20)
			.states_([["Load  " ++ (i+1).asString, TXColor.white, TXColor.sysGuiCol1]])
			.action_({
				this.loadSlot(i);
			});
			Button(window, 50 @ 20)
			.states_([["Store " ++ (i+1).asString, TXColor.white, TXColor.sysGuiCol2]])
			.action_({
				this.storeSlot(i);
			});
			// decorator shift
			window.asView.decorator.shift(-108, 28);
		});
		window.asView.decorator.top = holdTop;
		window.asView.decorator.left = holdLeft;

		// decorator next line
		window.asView.decorator.nextLine;

		if (initAction, {
			this.valueAction_(initVal);
		}, {
			this.value_(initVal);
		});
		// init
		// show/hide view
		this.showHide_realignBtn;
		this.updateValidPointKeys;
		this.update_seqNextPopup;
		this.resetSequence;
	}  // end of init

	value {
		var outVal = value.deepCopy;
		^outVal;
	}
	value_ { arg argValue;
		var holdVal = argValue.deepCopy;
		// add default data if empty
		holdVal.curveType = holdVal.curveType ? \BezierCP1CP2;
		holdVal.autoClose = holdVal.autoClose ? false;
		holdVal.numPoints = holdVal.numPoints ? 8;
		holdVal.usePoint1ForAutoCurveStart = holdVal.usePoint1ForAutoCurveStart ? false;
		holdVal.useLastPointForAutoCurveEnd = holdVal.useLastPointForAutoCurveEnd ? false;
		if (value != holdVal, {
			this.curveChanged(holdVal);
			this.addToHistory(holdVal);
			userView.refresh;
		});
	}
	valueAction_ { arg argValue;
		this.value_(argValue);
		action.value(this);
	}

	reset {
		this.valueAction = resetCurve;
		this.checkLockEndToStart;
		this.resetSequence;
	}

	realignControlPoints {
		var holdData = value.deepCopy;
		curveKeyGroups.do({arg group, i;
			var d1 = holdData[group.first];
			var d2 = holdData[group.last];
			if (group.size == 3, {
				holdData[group[1]] = d1.blend(d2, 0.5);
			}, {
				holdData[group[1]] = d1.blend(d2, 1/3);
				holdData[group[2]] = d1.blend(d2, 2/3);
			});
		});
		this.valueAction = holdData;
	}

	resetSequence {
		localData.currSeqStepIndex = 0;
		if (seqNextPopup.notNil, {
			seqNextPopup.value_(0);
		});
	}

	setEditMode {arg argMode;
		var arrEditModes = ['editPoints', 'drawSeq','move', 'x_zoom', 'y_zoom', 'zoom', 'rotate'];
		var holdInd = arrEditModes.indexOf(argMode);
		if (holdInd.notNil, {
			localData.editMode = argMode;
			this.showHide_SeqViews;
			userView.refresh;
			editModeListView.valueAction = holdInd;
		});
	}
	toggleShowGrid {
		if (localOptions.showGrid, {
			localOptions.showGrid = false;
		}, {
			localOptions.showGrid = true;
		});
		showGridBtn.value = localOptions.showGrid.binaryValue;
		userView.refresh;
	}

	toggleShowPoints {
		if (localOptions.showPoints, {
			localOptions.showPoints = false;
			showPointNamesBtn.visible = false;
		}, {
			localOptions.showPoints = true;
			showPointNamesBtn.visible = true;
		});
		showPointsBtn.value = localOptions.showPoints.binaryValue;
		userView.refresh;
	}

	toggleShowPointNames {
		if (showPointNamesBtn.buttonView.visible, {
			if (localOptions.showPointNames, {
				localOptions.showPointNames = false;
			}, {
				localOptions.showPointNames = true;
			});
			showPointNamesBtn.value = localOptions.showPointNames.binaryValue;
			userView.refresh;
		});
	}

	/* OLD
	updateInfoView {
		case
		{localData.editMode == 'editPoints'} {
			infoView.string =
			"Edit Points - Click to drag each point. Shift + Drag to move linked points together. Ctrl + Drag to align opposite control points";}
		{localData.editMode == 'drawSeq'} {
			infoView.string = "Draw Points In Sequence - Click and drag each point into position sequentially";}
		{localData.editMode == 'move'} {
			infoView.string = "Move All Points - Click and drag to move";}
		{localData.editMode == 'x_zoom'} {
			infoView.string = "X-Zoom All Points - Click and drag to zoom horizontally";}
		{localData.editMode == 'y_zoom'} {
			infoView.string = "Y-Zoom All Points - Click and drag to zoom vertically";}
		{localData.editMode == 'zoom'} {
			infoView.string = "Zoom All Points - Click and drag to zoom. Press Shift key to zoom X & Y axes independently";}
		{localData.editMode == 'rotate'} {
			infoView.string = "Rotate All Points - Click and drag to rotate. Press Shift key to rotate in steps of 5 degrees";}
		;
	}
	*/

	quantiseCurve {arg quantX = true, quantY = true;
		var holdData = value.deepCopy;
		validPointKeys.do({arg key, i;
			var point = holdData[key];
			if (quantX, {
				point.x = point.x.round(gridCols.reciprocal);
			});
			if (quantY, {
				point.y = point.y.round(gridRows.reciprocal);
			});
		});
		this.valueAction = holdData;
	}

	set { arg label, argAction, initVal, initAction=false;
		labelView.string = label;
		action = argAction;
		if (initAction) {
			this.valueAction_(initVal);
		}{
			this.value(initVal);
		};
	}
	curveChanged {arg curvePointData;
		value = curvePointData;
		// set drag object
		if (dragView.notNil, {
			dragView.object = value;
		});
	}
	addToHistory {arg curvePointData;
		var holdData = curvePointData.deepCopy;
		// if data is different add to history
		if (localData.curveHistory[localData.curveHistIndex] != holdData, {
			// truncate hist at current index
			localData.curveHistory = localData.curveHistory.keep(localData.curveHistIndex + 1);
			localData.curveHistory = localData.curveHistory.add(holdData);
			localData.curveHistory = localData.curveHistory.keep(0 - localData.maxHistorySize);
			localData.curveHistIndex = localData.curveHistory.size - 1;
			this.coloriseHistoryButtons;
		});
	}
	loadCurveHistoryPrev {
		var holdVal;
		if (localData.curveHistIndex > 0, {
			localData.curveHistIndex = localData.curveHistIndex - 1;
			holdVal = localData.curveHistory[localData.curveHistIndex].deepCopy;
			this.curveChanged(holdVal);
			userView.refresh;
			this.coloriseHistoryButtons;
			action.value(this);
		});
	}
	loadCurveHistoryNext {
		var holdVal;
		if (localData.curveHistIndex < (localData.curveHistory.size - 1), {
			localData.curveHistIndex = localData.curveHistIndex + 1;
			holdVal = localData.curveHistory[localData.curveHistIndex].deepCopy;
			this.curveChanged(holdVal);
			userView.refresh;
			this.coloriseHistoryButtons;
			action.value(this);
		});
	}
	coloriseHistoryButtons {
		// .states_([["< undo", TXColor.paleYellow, TXColor.sysGuiCol1]])
		// .states_([["redo >", TXColor.paleYellow, TXColor.sysGuiCol1]])
		if (histPrevBtn.notNil, {
			if (localData.curveHistIndex > 0, {
				histPrevBtn.states_([["< undo", TXColor.paleYellow, TXColor.sysGuiCol1]])
			},{
				histPrevBtn.states_([["< undo", TXColor.grey(0.7), TXColor.sysGuiCol1]])
			});
			if (localData.curveHistIndex < (localData.curveHistory.size - 1), {
				histNextBtn.states_([["redo >", TXColor.paleYellow, TXColor.sysGuiCol1]])
			},{
				histNextBtn.states_([["redo >", TXColor.grey(0.7), TXColor.sysGuiCol1]])
			});
		});
	}

	storeSlot { arg num;
		localData.arrSlotData.put(num, this.value);
	}
	loadSlot { arg num;
		this.valueAction = localData.arrSlotData.at(num);
	}

	setGridRowsCols { arg rows, cols;
		gridRows = rows;
		gridCols = cols;
		userView.refresh;
	}

	showHide_SeqViews {
		if (localData.editMode == 'drawSeq', {
			seqNextPopup.visible_(true);
			seqRestartButton.visible_(true);
		}, {
			seqNextPopup.visible_(false);
			seqRestartButton.visible_(false);
		});
	}

	showHide_realignBtn {
		if (value.curveType.asString.keep(3) == "Bez", {
			realignBtn.visible_(true);
		}, {
			realignBtn.visible_(false);
		});
	}

	checkLockEndToStart {
		var lastPointKey, newData;
		if (localOptions.lockEndToStart, {
			lastPointKey = curveKeyGroups.last.last;
			newData = this.value;
			newData[lastPointKey] = newData['p1d'].deepCopy;
			this.valueAction = newData;
		});
	}

	setBackgroundImage {arg argImageName;
		if (argImageName.notNil and: {this.isValidImageFile(argImageName)}, {
			localOptions.imageFileName = argImageName;
			localOptions.backgroundImage = Image.open(localOptions.imageFileName);
			if (userView.notNil, {
				userView.refresh;
				showImageBtn.visible = true;
			});
		}, {
			localOptions.imageFileName = nil;
			localOptions.backgroundImage = nil;
			if (userView.notNil, {
				userView.refresh;
				showImageBtn.visible = false;
			});
		});
	}

	// mouse actions
	applyMouseDown {arg view, sx, sy;
		var i, found, holdGroup, key, point, dist;
		tempData.mouseDownPoint = Point(sx, sy);
		tempData.hasChanged = false;
		case
		{localData.editMode == 'editPoints'} {
			// find point from   validPointKeys
			i = 0;
			found = false;
			while ({(found == false) and: {i < validPointKeys.size}}, {
				key = validPointKeys[i];
				point = value[key];
				dist = point.dist(tempData.mouseDownPoint);
				if (dist < 0.006, {
					dragPointKey = key;
					found = true;
				});
				i = i + 1;
			});
			if (dragPointKey.notNil, {
				// find local group
				if (localKeyGroups.notNil, {
					i = 0;
					found = false;
					while ( {(found == false) and: {i < localKeyGroups.size}}, {
						holdGroup = localKeyGroups[i];
						if (holdGroup.indexOf(dragPointKey).notNil, {
							tempData.localKeyGroup =  holdGroup.deepCopy;
							found = true;
						});
						i = i + 1;
					});
				});
				// find align group
				if (value.curveType == \BezierCP1CP2 and: {dragPointKey.asString[2] == $c}, {
					i = 0;
					found = false;
					while ({(found == false) and: {i < localKeyGroups.size}}, {
						holdGroup = localKeyGroups[i];
						if (holdGroup.indexOf(dragPointKey).notNil, {
							found = true;
							if (holdGroup.size > 2, {
								tempData.alignGroup =  holdGroup.deepCopy;
							});
						});
						i = i + 1;
					});
				});
				tempData.startPoints = value.deepCopy;
			});

		}
		{localData.editMode == 'drawSeq'} {
			point = value[validPointKeys[localData.currSeqStepIndex]];
			point.x = sx;
			point.y = sy;
			tempData.hasChanged = true;
			this.checkLockEndToStart;
			dragPointKey = validPointKeys[localData.currSeqStepIndex];
		}
		{localData.editMode == 'move'} {
			tempData.mouseDownCurveData = ();
			validPointKeys.do({arg key, i;
				tempData.mouseDownCurveData[key] = value[key].copy;
			});
		}
		{localData.editMode == 'x_zoom' or: (localData.editMode == 'y_zoom') or: (localData.editMode == 'zoom')} {
			tempData.mouseDownCurveData = ();
			validPointKeys.do({arg key, i;
				tempData.mouseDownCurveData[key] = value[key].copy;
			});
			tempData.mouseDownCurveData.values.do({arg point, i;
				if (i == 0, {
					tempData.minX = point.x;
					tempData.maxX = point.x;
					tempData.minY = point.y;
					tempData.maxY = point.y;
				}, {
					tempData.minX = tempData.minX.min(point.x);
					tempData.maxX = tempData.maxX.max(point.x);
					tempData.minY = tempData.minY.min(point.y);
					tempData.maxY = tempData.maxY.max(point.y);
				});
			});
			tempData.centrePoint = Point(
				tempData.minX + ((tempData.maxX - tempData.minX) * 0.5),
				tempData.minY + ((tempData.maxY - tempData.minY) * 0.5)
			);
		}
		{localData.editMode == 'rotate'} {
			tempData.mouseDownCurveData = ();
			validPointKeys.do({arg key, i;
				tempData.mouseDownCurveData[key] = value[key].copy;
			});
			tempData.mouseDownCurveData.values.do({arg point, i;
				if (i == 0, {
					tempData.minX = point.x;
					tempData.maxX = point.x;
					tempData.minY = point.y;
					tempData.maxY = point.y;
				}, {
					tempData.minX = tempData.minX.min(point.x);
					tempData.maxX = tempData.maxX.max(point.x);
					tempData.minY = tempData.minY.min(point.y);
					tempData.maxY = tempData.maxY.max(point.y);
				});
			});
			tempData.centrePoint = Point(
				tempData.minX + ((tempData.maxX - tempData.minX) * 0.5),
				tempData.minY + ((tempData.maxY - tempData.minY) * 0.5)
			);
		};
	}

	applyMouseMove {arg view, sx, sy;
		var point, shiftPoint, scaledPoint, rotatedPoint, startPoint;
		var newPoint, oppPointKey, dragPoint, oppPoint, centerPoint;
		case
		{localData.editMode == 'editPoints'} {
			// move point x/y
			if (dragPointKey.notNil, {
				// shift edits local group
				if (holdMouseData.isShift and: {tempData.localKeyGroup.notNil}, {
					tempData.dragAlign = false;
					tempData.keyGroup =  tempData.localKeyGroup;
				}, {
					tempData.keyGroup = [dragPointKey];
					// ctrl aligns matching control points to smooth curve joins
					if (holdMouseData.isCtrl and: {tempData.alignGroup.notNil}, {
							tempData.dragAlign = true;
						}, {
							tempData.dragAlign = false;
					});
				});
				tempData.keyGroup.do({arg key, i;
					startPoint = tempData.startPoints[tempData.keyGroup[i]];
					point = value[key];
					point.x = (startPoint.x + (sx - tempData.mouseDownPoint.x)).clip(0, 1);
					point.y = (startPoint.y + (sy - tempData.mouseDownPoint.y)).clip(0, 1);
				});
				// align opposite point if needed
				if (tempData.dragAlign, {
					if (tempData.alignGroup[0] == dragPointKey, {
						oppPointKey = tempData.alignGroup[2];
						oppPoint = tempData.startPoints[tempData.alignGroup[2]];
					}, {
						oppPointKey = tempData.alignGroup[0];
						oppPoint = tempData.startPoints[tempData.alignGroup[0]];
					});
					dragPoint = value[dragPointKey];
					centerPoint = tempData.startPoints[tempData.alignGroup[1]];
					newPoint = centerPoint - dragPoint;
					newPoint.rho = centerPoint.dist(oppPoint);
					newPoint = centerPoint + newPoint;
					value[oppPointKey] = newPoint.clip(0, 1);
				});
				tempData.hasChanged = true;
				this.checkLockEndToStart;
			});
		}
		{localData.editMode == 'drawSeq'} {
			point = value[validPointKeys[localData.currSeqStepIndex]];
			point.x = sx;
			point.y = sy;
			tempData.hasChanged = true;
			this.checkLockEndToStart;
		}
		{localData.editMode == 'move'} {
			tempData.mouseDownCurveData.pairsDo({arg key, point;
				newPoint = Point(
					(point.x + (sx - tempData.mouseDownPoint.x)).clip(0, 1),
					(point.y + (sy - tempData.mouseDownPoint.y)).clip(0, 1)
				);
				value[key] = newPoint;
				action.value(this);
				tempData.hasChanged = true;
			});
		}
		{localData.editMode == 'x_zoom'} {
			tempData.scaleX = 1 + (((sx - tempData.mouseDownPoint.x) - (sy - tempData.mouseDownPoint.y)) * 2);
			tempData.mouseDownCurveData.pairsDo({arg key, point;
				shiftPoint = point - tempData.centrePoint;
				scaledPoint = shiftPoint;
				scaledPoint.x = scaledPoint.x * tempData.scaleX;
				newPoint = (scaledPoint + tempData.centrePoint).clip(0, 1);
				value[key] = newPoint;
				action.value(this);
				tempData.hasChanged = true;
			});
		}
		{localData.editMode == 'y_zoom'} {
			tempData.scaleY = 1 + (((sx - tempData.mouseDownPoint.x) - (sy - tempData.mouseDownPoint.y)) * 2);
			tempData.mouseDownCurveData.pairsDo({arg key, point;
				shiftPoint = point - tempData.centrePoint;
				scaledPoint = shiftPoint;
				scaledPoint.y = scaledPoint.y * tempData.scaleY;
				newPoint = (scaledPoint + tempData.centrePoint).clip(0, 1);
				value[key] = newPoint;
				action.value(this);
				tempData.hasChanged = true;
			});
		}
		{localData.editMode == 'zoom'} {
			if (holdMouseData.isShift, {
				tempData.scaleX = 1 + ((sx - tempData.mouseDownPoint.x) * 2);
				tempData.scaleY = 1 - ((sy - tempData.mouseDownPoint.y) * 2);
			}, {
				tempData.scaleX = 1 + (((sx - tempData.mouseDownPoint.x) - (sy - tempData.mouseDownPoint.y)) * 2);
				tempData.scaleY = tempData.scaleX;
			});
			tempData.mouseDownCurveData.pairsDo({arg key, point;
				shiftPoint = point - tempData.centrePoint;
				scaledPoint = shiftPoint;
				scaledPoint.x = scaledPoint.x * tempData.scaleX;
				scaledPoint.y = scaledPoint.y * tempData.scaleY;
				newPoint = (scaledPoint + tempData.centrePoint).clip(0, 1);
				value[key] = newPoint;
				action.value(this);
				tempData.hasChanged = true;
			});
		}
		{localData.editMode == 'rotate'} {
			tempData.rotate = 360 * ((sx - tempData.mouseDownPoint.x) - (sy - tempData.mouseDownPoint.y));
			if (holdMouseData.isShift, {
				tempData.rotate = tempData.rotate.round(5);
			});
			tempData.mouseDownCurveData.pairsDo({arg key, point;
				var holdRot;
				shiftPoint = point - tempData.centrePoint;
				rotatedPoint = shiftPoint.rotate(tempData.rotate.degrad);
				newPoint = (rotatedPoint + tempData.centrePoint).clip(0, 1);
				value[key] = newPoint;
				action.value(this);
				tempData.hasChanged = true;
			});
		};
	}

	applyMouseUp {arg view, sx, sy;
		var newPoint, lastIndex;
		// quantise if needed
		if (tempData.hasChanged and: localOptions.autoQuantise, {
			this.quantiseCurve(true, true);
		});
		this.addToHistory(value);
		action.value(this);
		dragPointKey = nil;
		if (localData.editMode == 'drawSeq', {
			if (localOptions.lockEndToStart, {
				lastIndex = validPointKeys.size - 2;
			}, {
				lastIndex = validPointKeys.size - 1;
			});
			if (localData.currSeqStepIndex == lastIndex, {
				this.setEditMode('editPoints')
			});
			// increment & refresh popup
			localData.currSeqStepIndex = (localData.currSeqStepIndex + 1).min(validPointKeys.size - 1);
			seqNextPopup.value_(localData.currSeqStepIndex);
		});
		tempData = ();
	}

	/* removed for now - drag/drop not being used
	getDragObject{
		var outData = ();
		outData.label = "Points Curve Data";
		// add point data
		outData.curveData = this.value;
		// add options
		outData.options = localOptions.deepCopy;
		^outData;
	}
	*/

	getDefaultCurve {
		var curvePointData;
		curvePointData = (); // dict
		classData.pointSymbols.do({arg item, i;
			curvePointData[item] = Point(0.2 + (0.7666666 * i / (classData.pointSymbols.size - 1)),
				0.033333 + ((i % 6) * 0.033333));
		});
		^curvePointData;
	}

	update_seqNextPopup {
		seqNextPopup.items_(validPointKeys.collect({arg item; "next point: " ++ item.asString;}));
		seqNextPopup.value_(localData.currSeqStepIndex);
	}

	isValidImageFile {arg argPath;  // check argument is a valid path
		var holdPathName, errorMessage;
		holdPathName = PathName.new(argPath.asString).absolutePath;
		if (holdPathName.extension.icontainsStringAt(0, "png"), {
			if (File.exists(holdPathName).not, {
				errorMessage = "Error: File not found: " ++ argPath;
			});
		}, {
			errorMessage = "Error: Not a PNG image filename:  " ++ argPath;
		});
		if (errorMessage.notNil, {
			TXInfoScreen(errorMessage);
			^false;
		});
		^true;
	}

	drawCurves {arg view;
		var curveData = this.value;
		var pixelSize = userViewWidth.reciprocal;
		var pointArray, holdSpline, holdPoint;
		Pen.use {
			Pen.width = 2 * min(view.pixelScale.x, view.pixelScale.y);
			if (localOptions.showPoints, {
				// draw control point lines
				Pen.color = Color(0.4, 0.6, 0.4);
				lineKeyGroups.do({arg group, i;
					Pen.line(curveData[group[0]], curveData[group[1]]);
					Pen.stroke;
				});
			});
			// draw curves
			Pen.color = TXColor.orange2; //.blend(Color.black, 0.1);
			case
			{value.curveType == \autoCurve}   {
				pointArray = validPointKeys.collect({arg key;
					curveData[key];
				});
				if (value.usePoint1ForAutoCurveStart.not, {
					pointArray = pointArray.keep(1 - pointArray.size);
				});
				if (value.useLastPointForAutoCurveEnd.not, {
					pointArray = pointArray.keep(pointArray.size - 1);
				});
				if (value.autoClose, {
					pointArray = pointArray.add(pointArray[0]);
				});
				holdSpline = PolySpline(*pointArray);
				// make CUBIC
				holdSpline.cubicControls(1/3); // cubic (1/3 is default)
				// holdSpline.cubicControls(1/6);
				// holdSpline.cubicControls(1/1.5);
				//holdSpline.bSplineControls(4); // default 4
				// adjust stray points
				holdSpline.control2[holdSpline.points.size - 1] =  holdSpline.points[holdSpline.points.size - 1];
				holdSpline.control1[holdSpline.points.size - 1] = holdSpline.points[holdSpline.points.size - 1];
				// adjust start/end
				if (value.autoClose, {
					holdSpline.control1[0] = holdSpline.points[0]
					    .blend(holdSpline.points[holdSpline.points.size - 1] /*.mirrorTo(holdSpline.points[0])*/, 0.2);
					holdSpline.control2[holdSpline.points.size - 2] = holdSpline.points[holdSpline.points.size - 1]
						.blend(holdSpline.points[1].mirrorTo(holdSpline.points[0]), 0.2);
				}, {
					if (value.usePoint1ForAutoCurveStart.not, {
						holdPoint = curveData['acstart'];
						// NEEDS TO BE MIRRORED
						holdSpline.control1[0] = holdPoint.mirrorTo(holdSpline.points[0]);
					}, {
						holdSpline.control1[0] = holdSpline.points[0];
					});
					if (value.useLastPointForAutoCurveEnd.not, {
						holdPoint = curveData['acend'];
						// NEEDS TO BE MIRRORED
						holdSpline.control2[holdSpline.points.size - 2] = holdPoint.mirrorTo(holdSpline.points[holdSpline.points.size - 1]);
					}, {
						holdSpline.control2[holdSpline.points.size - 2] = holdSpline.points[holdSpline.points.size - 1];
					});
				});
				holdSpline.stroke;
				// if (value.autoClose, {
				// 	Pen.line(pointArray[0], pointArray.last);
				// 	Pen.stroke;
				// });
			}
			{value.curveType == \straightLine}   {
				curveKeyGroups.do({arg group, i;
					Pen.line(curveData[group[0]], curveData[group[1]]);
					Pen.stroke;
				});
			}
			{value.curveType == \BezierCP1}   {
				curveKeyGroups.do({arg group, i;
					Pen.moveTo(curveData[group[0]]);
					Pen.quadCurveTo(curveData[group[2]], curveData[group[1]]);
					Pen.stroke;
				});
			}
			{value.curveType == \BezierCP1Smooth}   {
				curveKeyGroups.do({arg group, i;
					var holdPoint;
					if (i < (curveKeyGroups.size - 1), {
						holdPoint = (2 * curveData[curveKeyGroups[i + 1][0]]) - curveData[curveKeyGroups[i + 1][1]];
					}, {
						holdPoint = curveData[group[2]];
					});
					Pen.moveTo(curveData[group[0]]);
					Pen.curveTo(curveData[group[2]], curveData[group[1]], holdPoint);
					Pen.stroke;
				});
			}
			{value.curveType == \BezierCP1CP2}   {
				curveKeyGroups.do({arg group, i;
					Pen.moveTo(curveData[group[0]]);
					Pen.curveTo(curveData[group[3]], curveData[group[1]], curveData[group[2]]);
					Pen.stroke;
				});
			};
			if (value.autoClose and: {value.curveType != \autoCurve}, {
				Pen.line(curveData[localData.lastPointKey], curveData['p1d']);
				Pen.stroke;
			});
			if (localOptions.showPoints, {
				// draw points
				validPointKeys.do({arg key, i;
					var point = curveData[key];
					if (localData.editMode == 'editPoints', {
						if (classData.drawPointKeys.indexOf(key).notNil, {
							//Pen.color = Color(0, 0.3, 0.8);
							Pen.color = Color.blue(0.8);
						}, {
							Pen.color = Color.green(0.6);
						});
					}, {
						if (classData.drawPointKeys.indexOf(key).notNil, {
							//Pen.color = Color.blue(0.3);
							Pen.color = Color(0.5, 0.5, 0.7);
						}, {
							//Pen.color = Color.green(0.3);
							Pen.color = Color(0.5, 0.7, 0.5);
						});
					});
					Pen.addRect(Rect(point.x - (3 * pixelSize), point.y - (3 * pixelSize), 6 * pixelSize, 6 * pixelSize));
					Pen.stroke;
					});
				});
			};
			}  // end of drawCurves

	updateValidPointKeys {
		var cp1Needed, cp2Needed, holdCurveGroup, newGroup;
		// curve types:\autoCurve, \straightLine, \BezierCP1, \BezierCP1Smooth, \BezierCP1CP2,
		// points:
		// 	'acstart',
		// 	'p1d',
		// 	'p2c1', 'p2c2', 'p2d',
		// 	'p3c1', 'p3c2', 'p3d',
		// 	'p4c1', 'p4c2', 'p4d',
		// 	'p5c1', 'p5c2', 'p5d',
		// 	'p6c1', 'p6c2', 'p6d',
		// 	'p7c1', 'p7c2', 'p7d',
		// 	'p8c1', 'p8c2', 'p8d',
		// 'acend'
		//
		validPointKeys = [];
		curveKeyGroups = [];
		lineKeyGroups = [];
		localData.lastPointKey = switch (value.numPoints,
			2, 'p2d', 3, 'p3d', 4, 'p4d', 5, 'p5d', 6, 'p6d', 7, 'p7d', 8, 'p8d');
		case
		{value.curveType == \autoCurve}   {
			cp1Needed = false;
			cp2Needed = false;
		}
		{value.curveType == \straightLine}   {
			cp1Needed = false;
			cp2Needed = false;
		}
		{value.curveType == \BezierCP1}   {
			cp1Needed = true;
			cp2Needed = false;
		}
		{value.curveType == \BezierCP1Smooth}   {
			cp1Needed = true;
			cp2Needed = false;
		}
		{value.curveType == \BezierCP1CP2}   {
			cp1Needed = true;
			cp2Needed = true;
		};
		// add valid points & groups
		if (value.curveType == \autoCurve
			and: {value.usePoint1ForAutoCurveStart.not}, {
				validPointKeys = validPointKeys.add('acstart');
				lineKeyGroups = lineKeyGroups.add(['acstart', 'p1d']);
		});
		// add point 1-2 group
		holdCurveGroup = ['p1d'];
		validPointKeys = validPointKeys.add('p1d');
		if (cp1Needed, {
			validPointKeys = validPointKeys.add('p2c1');
			holdCurveGroup = holdCurveGroup.add('p2c1');
			lineKeyGroups = lineKeyGroups.add(['p1d', 'p2c1']);
			if (value.curveType == \BezierCP1, {
				lineKeyGroups = lineKeyGroups.add(['p2c1', 'p2d']);
			});
		});
		if (cp2Needed, {
			validPointKeys = validPointKeys.add('p2c2');
			holdCurveGroup = holdCurveGroup.add('p2c2');
			lineKeyGroups = lineKeyGroups.add(['p2c2', 'p2d']);
		});
		validPointKeys = validPointKeys.add('p2d');
		holdCurveGroup = holdCurveGroup.add('p2d');
		curveKeyGroups = curveKeyGroups.add(holdCurveGroup.deepCopy);
		// other point groups if needed
		if (value.numPoints > 2, {
			holdCurveGroup = ['p2d'];
			if (cp1Needed, {
				validPointKeys = validPointKeys.add('p3c1');
				holdCurveGroup = holdCurveGroup.add('p3c1');
				lineKeyGroups = lineKeyGroups.add(['p2d', 'p3c1']);
				if (value.curveType == \BezierCP1, {
					lineKeyGroups = lineKeyGroups.add(['p3c1', 'p3d']);
				});
			});
			if (cp2Needed, {
				validPointKeys = validPointKeys.add('p3c2');
				holdCurveGroup = holdCurveGroup.add('p3c2');
				lineKeyGroups = lineKeyGroups.add(['p3c2', 'p3d']);
			});
			validPointKeys = validPointKeys.add('p3d');
			holdCurveGroup = holdCurveGroup.add('p3d');
			curveKeyGroups = curveKeyGroups.add(holdCurveGroup.deepCopy);
		});
		if (value.numPoints > 3, {
			holdCurveGroup = ['p3d'];
			if (cp1Needed, {
				validPointKeys = validPointKeys.add('p4c1');
				holdCurveGroup = holdCurveGroup.add('p4c1');
				lineKeyGroups = lineKeyGroups.add(['p3d', 'p4c1']);
				if (value.curveType == \BezierCP1, {
					lineKeyGroups = lineKeyGroups.add(['p4c1', 'p4d']);
				});
			});
			if (cp2Needed, {
				validPointKeys = validPointKeys.add('p4c2');
				holdCurveGroup = holdCurveGroup.add('p4c2');
				lineKeyGroups = lineKeyGroups.add(['p4c2', 'p4d']);
			});
			validPointKeys = validPointKeys.add('p4d');
			holdCurveGroup = holdCurveGroup.add('p4d');
			curveKeyGroups = curveKeyGroups.add(holdCurveGroup.deepCopy);
		});
		if (value.numPoints > 4, {
			holdCurveGroup = ['p4d'];
			if (cp1Needed, {
				validPointKeys = validPointKeys.add('p5c1');
				holdCurveGroup = holdCurveGroup.add('p5c1');
				lineKeyGroups = lineKeyGroups.add(['p4d', 'p5c1']);
				if (value.curveType == \BezierCP1, {
					lineKeyGroups = lineKeyGroups.add(['p5c1', 'p5d']);
				});
			});
			if (cp2Needed, {
				validPointKeys = validPointKeys.add('p5c2');
				holdCurveGroup = holdCurveGroup.add('p5c2');
				lineKeyGroups = lineKeyGroups.add(['p5c2', 'p5d']);
			});
			validPointKeys = validPointKeys.add('p5d');
			holdCurveGroup = holdCurveGroup.add('p5d');
			curveKeyGroups = curveKeyGroups.add(holdCurveGroup.deepCopy);
		});
		if (value.numPoints > 5, {
			holdCurveGroup = ['p5d'];
			if (cp1Needed, {
				validPointKeys = validPointKeys.add('p6c1');
				holdCurveGroup = holdCurveGroup.add('p6c1');
				lineKeyGroups = lineKeyGroups.add(['p5d', 'p6c1']);
				if (value.curveType == \BezierCP1, {
					lineKeyGroups = lineKeyGroups.add(['p6c1', 'p6d']);
				});
			});
			if (cp2Needed, {
				validPointKeys = validPointKeys.add('p6c2');
				holdCurveGroup = holdCurveGroup.add('p6c2');
				lineKeyGroups = lineKeyGroups.add(['p6c2', 'p6d']);
			});
			validPointKeys = validPointKeys.add('p6d');
			holdCurveGroup = holdCurveGroup.add('p6d');
			curveKeyGroups = curveKeyGroups.add(holdCurveGroup.deepCopy);
		});
		if (value.numPoints > 6, {
			holdCurveGroup = ['p6d'];
			if (cp1Needed, {
				validPointKeys = validPointKeys.add('p7c1');
				holdCurveGroup = holdCurveGroup.add('p7c1');
				lineKeyGroups = lineKeyGroups.add(['p6d', 'p7c1']);
				if (value.curveType == \BezierCP1, {
					lineKeyGroups = lineKeyGroups.add(['p7c1', 'p7d']);
				});
			});
			if (cp2Needed, {
				validPointKeys = validPointKeys.add('p7c2');
				holdCurveGroup = holdCurveGroup.add('p7c2');
				lineKeyGroups = lineKeyGroups.add(['p7c2', 'p7d']);
			});
			validPointKeys = validPointKeys.add('p7d');
			holdCurveGroup = holdCurveGroup.add('p7d');
			curveKeyGroups = curveKeyGroups.add(holdCurveGroup.deepCopy);
		});
		if (value.numPoints > 7, {
			holdCurveGroup = ['p7d'];
			if (cp1Needed, {
				validPointKeys = validPointKeys.add('p8c1');
				holdCurveGroup = holdCurveGroup.add('p8c1');
				lineKeyGroups = lineKeyGroups.add(['p7d', 'p8c1']);
				if (value.curveType == \BezierCP1, {
					lineKeyGroups = lineKeyGroups.add(['p8c1', 'p8d']);
				});
			});
			if (cp2Needed, {
				validPointKeys = validPointKeys.add('p8c2');
				holdCurveGroup = holdCurveGroup.add('p8c2');
				lineKeyGroups = lineKeyGroups.add(['p8c2', 'p8d']);
			});
			validPointKeys = validPointKeys.add('p8d');
			holdCurveGroup = holdCurveGroup.add('p8d');
			curveKeyGroups = curveKeyGroups.add(holdCurveGroup.deepCopy);
		});
		// AutoCurveEnd
		if (value.curveType == \autoCurve
			and: {value.useLastPointForAutoCurveEnd.not}, {
				validPointKeys = validPointKeys.add('acend');
				lineKeyGroups = lineKeyGroups.add([localData.lastPointKey, 'acend']);
		});
		// build local Groups
		if (value.curveType == \autoCurve, {
			newGroup = ['p1d'];
			if (value.usePoint1ForAutoCurveStart.not, {
				newGroup = newGroup.add('acstart');
			});
			localKeyGroups = [newGroup];
			curveKeyGroups.do({arg group, i;
				newGroup = [group.last];
				localKeyGroups = localKeyGroups.add(newGroup);
			});
			if (value.useLastPointForAutoCurveEnd.not, {
				localKeyGroups[localKeyGroups.size - 1] = localKeyGroups[localKeyGroups.size - 1].add('acend');
			});
		}, {
			// if not \autocurve
			if (cp1Needed, {  // check if has control points
				localKeyGroups = [['p1d', 'p2c1']];
				curveKeyGroups.do({arg item, i;
					newGroup = [];
					if (cp2Needed, {
						newGroup = newGroup.add(item[2]);
					});
					newGroup = newGroup.add(item.last);
					if (i < (curveKeyGroups.size - 1), {
						newGroup = newGroup.add(curveKeyGroups[i + 1][1]);
					});
					localKeyGroups = localKeyGroups.add(newGroup);
				});
			},{
				localKeyGroups = nil;
			});
		});
		// check lock
		if (localOptions.lockEndToStart, {
			// join groups in specific order
			localKeyGroups[0] = localKeyGroups.last[0].asArray ++ localKeyGroups[0] ++ localKeyGroups.last[1].asArray;
			localKeyGroups.pop;
		});
		/*
		// build controlPointGroups
		if (value.curveType == \BezierCP1CP2, {
			controlPointGroups = [];
			localKeyGroups.do({arg lkgroup;
				var cpgroup = lkgroup.select({arg item; item.asString[2] == "c";});
				if (cpgroup.size > 1, {
					controlPointGroups = controlPointGroups.add(cpgroup);
				});
			});
		});
		*/
		this.resetSequence;

}  // end of updateValidPointKeys

}
