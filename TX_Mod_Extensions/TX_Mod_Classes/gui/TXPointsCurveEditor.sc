// Copyright (C) 2020 Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXPointsCurveEditor {
	classvar <>curveEditorWindow;
	classvar <classData;

	*initClass {
		classData = ();
		classData.gridRows = 30;
		classData.gridCols = 30;
		classData.alwaysOnTop = 1;
		classData.windowPosX = 400;
		classData.windowPosY = 400;
		classData.windowVisibleOrigin = Point.new(0,0);
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
		classData.options = ();
		classData.options.arrCurveTypes = [
			\autoCurve, \straightLine, \BezierCP1, \BezierCP1Smooth, \BezierCP1CP2,
		];
		// default curve
		classData.curveData = this.getDefaultCurve;

		classData.options.showGrid = true;
		classData.options.showPoints = true;
		classData.options.showPointNames = false;
		classData.options.lastPointKey = 'p8d';

		classData.curveViewData = ();
		classData.pointsCurvePresetData = ();
		classData.presetText = "";
		classData.presetIndex = 0;
		/*
		// not used now:
		classData.options.showCurveEditOptions = true;
		classData.options.curveEditOption = 0;
		// 0, show start curve only
		// 1, show end curve only
		// 2, show start curve with non-editable end curve behind it
		// 3, show end curve with non-editable main curve behind it
		*/

		classData.clipboardCurves = {this.getDefaultCurve;} ! 25;

		this.clearViewData;
		this.loadPresetDataFromFile;
	}
	// end of initClass

	*closeWindow {
		if (curveEditorWindow.notNil, {
			classData.windowPosX = curveEditorWindow.bounds.left;
			classData.windowPosY = curveEditorWindow.bounds.top;
			curveEditorWindow.close;
		});
		this.clearViewData;
	}

	*clearViewData {
		classData.viewBox = nil;
		classData.gridRowsView = nil;
		classData.gridColsView = nil;
		classData.curveView = nil;
	}

	*rebuildWindow {
		this.closeWindow;
		{this.showWindow;}.defer(0.03);
	}

	*openWithCurveData {arg argData;
		if (argData.label == "Points Curve Data", {
			// add options
			if (argData.options.notNil, {
				classData.options.putAll(argData.options);
			});
			// add curve data
			if (argData.curveData.notNil, {
				classData.curveData.putAll(argData.curveData);
			});
		});
		this.showWindow;
	}

	*setGridRows {arg val;
		classData.gridRows = val;
		this.updateGrids;
	}
	*setGridCols {arg val;
		classData.gridCols = val;
		this.updateGrids;
	}
	*setGridRowsCols {arg val;
		classData.gridRows = val;
		classData.gridCols = val;
		this.updateGrids;
	}
	*updateGrids {
		if (classData.gridRowsView.notNil, {
			classData.gridRowsView.value_(classData.gridRows);
		});
		if (classData.gridColsView.notNil, {
			classData.gridColsView.value_(classData.gridCols);
		});
		classData.curveView.setGridRowsCols(classData.gridRows, classData.gridCols);
	}

	*isValidImageFile {arg argPath;  // check argument is a valid path
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

	//======================

	*quantizeToGrid {arg inputArray, quantizeRows = true, quantizeCols = true;
		// var holdArray, holdSignal, outArray, holdCols;
		// var maxVal = inputArray.size;
		// holdArray = Array.newClear(maxVal);
		// if (quantizeCols, {
		// 	classData.gridCols.do({arg item;
		// 		var jump, startRange, endRange, meanVal;
		// 		jump = classData.gridCols.reciprocal;
		// 		startRange = (item * jump * maxVal).round(1);
		// 		endRange = ((item + 1) * jump * maxVal).round(1) - 1;
		// 		meanVal = inputArray.copyRange(startRange.asInteger, endRange.asInteger).mean;
		// 		if (quantizeRows, {
		// 			meanVal = meanVal.round(classData.gridRows.reciprocal);
		// 		});
		// 		holdArray[startRange.asInteger..endRange.asInteger] = meanVal;
		// 	});
		// 	},{
		// 		holdArray = inputArray.collect({arg item;
		// 			var outVal = item;
		// 			if (quantizeRows, {
		// 				outVal = outVal.round(classData.gridRows.reciprocal);
		// 			});
		// 			outVal;
		// 		});
		// });
		// holdSignal = Signal.newFrom(holdArray);
		// outArray = Array.newFrom(holdSignal);
		//^outArray;
	}

	//======================

	*updatecurveView {
		if (classData.curveView.notNil, {classData.curveView.value = classData.curveData;});
	}

	*getDefaultCurve {
		var curvePointData;
		curvePointData = (); // dict
		classData.pointSymbols.do({arg item, i;
			curvePointData[item] = Point(0.2 + (0.7666666 * i / (classData.pointSymbols.size - 1)),
				0.033333 + ((i % 6) * 0.033333));
		});
		curvePointData[\curveType] = \BezierCP1CP2;
		curvePointData[\numPoints] = 8;
		curvePointData[\autoClose] = false;
		curvePointData[\usePoint1ForAutoCurveStart] = false;
		curvePointData[\useLastPointForAutoCurveEnd] = false;
		^curvePointData;
	}

	*showWindow {
		var screenWidth = 1050;
		var screenHeight = 960;
		var scrollView, holdView;
		var arrGridPresetNames, arrGridPresetActions;
		var holdFont, gridSizeControlSpec;

		// Set Default Font
		holdFont = Font.default;
		Font.default = Font("Helvetica", 12);

		// create window if needed
		if (curveEditorWindow.isNil, {
			curveEditorWindow = Window.new(
				"Points Curve Editor",
				Rect(classData.windowPosX, classData.windowPosY, screenWidth, screenHeight),
				resizable: true, scroll: true
			).front;
			curveEditorWindow.view.action = {arg view;
				classData.windowVisibleOrigin = view.visibleOrigin;
			};
			curveEditorWindow.view.mouseWheelAction = {arg view;
				classData.windowVisibleOrigin = view.visibleOrigin;
			};
			curveEditorWindow.view.keyDownAction = { |view, char|
				case
				{char == $e} {classData.curveView.setEditMode('editPoints');}
				{char == $d} {classData.curveView.setEditMode('drawSeq');}
				{char == $s} {classData.curveView.setEditMode('shift');}
				{char == $x} {classData.curveView.setEditMode('x_zoom');}
				{char == $y} {classData.curveView.setEditMode('y_zoom');}
				{char == $z} {classData.curveView.setEditMode('zoom');}
				{char == $r} {classData.curveView.setEditMode('rotate');}
				{char == $g} {classData.curveView.toggleShowGrid;}
				{char == $p} {classData.curveView.toggleShowPoints;}
				{char == $n} {classData.curveView.toggleShowPointNames;}
				;
			};

		});

		// defer visible origin
		{curveEditorWindow.view.visibleOrigin = classData.windowVisibleOrigin;}.defer(0.2);

		//curveEditorWindow.view.hasHorizontalScroller = false;
		curveEditorWindow.view.background_(TXColor.grey(0.76));
		curveEditorWindow.onClose_({this.curveEditorWindow = nil;});
		curveEditorWindow.alwaysOnTop = classData.alwaysOnTop.asBoolean;

		// // add ScrollView
		// scrollView = ScrollView(curveEditorWindow, Rect(0, 0, screenWidth-15, screenHeight-10))
		// .hasBorder_(false);
		// scrollView.action = {classData.visibleOrigin = scrollView.visibleOrigin};
		// scrollView.hasHorizontalScroller = false;
		// scrollView.hasVerticalScroller = true;

		if (classData.viewBox.notNil, {
			TXSystem1GUI.deferRemoveView(classData.viewBox);
		});

		// make box to display
		classData.viewBox = CompositeView(curveEditorWindow, Rect(4, 4, 1400, 3000))
		.background_(TXColor.clear);
		classData.viewBox.decorator = FlowLayout(classData.viewBox.bounds);

		// set visibleOrigin
		if (classData.visibleOrigin.notNil, {scrollView.view.visibleOrigin = classData.visibleOrigin; });

		Button(classData.viewBox, 40 @ 20)
		.states_([
			["Help", TXColor.white, TXColor.sysHelpCol]
		])
		.action_({|view|
			TXHelpScreen.openFile("TX_Points Curve Editor");
		});

		classData.viewBox.decorator.shift(20,0);

		// CheckBox: "Window on top"
		holdView = TXCheckBox(classData.viewBox, Rect(0, 0, 120, 20), "window on top",
			TXColor.sysGuiCol1, TXColor.white, TXColor.white, TXColor.sysGuiCol1);
		holdView.value = classData.alwaysOnTop;
		holdView.action = {|view|
			classData.alwaysOnTop = view.value;
			curveEditorWindow.alwaysOnTop = classData.alwaysOnTop.asBoolean;
		};
		classData.viewBox.decorator.shift(20,0);

		// Drag box
		classData.curveDragView = DragBoth(classData.viewBox, 130 @ 26);
		classData.curveDragView.setBoth = false;
		classData.curveDragView.string = "drag & drop curve";
		classData.curveDragView.align = \center;
		classData.curveDragView.background_(TXColor.paleYellow);
		classData.curveDragView.stringColor_(TXColor.black);
		classData.curveDragView.canReceiveDragHandler = {View.currentDrag.class == Event;};
		classData.curveDragView.receiveDragHandler = {arg view;
			var dragObject = View.currentDrag;
			if (dragObject.notNil
				and: {dragObject.class == Event}
				and: {dragObject.label == "Points Curve Data"}
				, {
					classData.curveData = dragObject.curveData;
					this.showWindow;
			});
		};
		classData.curveDragView.beginDragAction = {arg view;
			classData.curveDragView.object = this.getDragObject;
			classData.curveDragView.dragLabel = "Points Curve Data";
			view.object;
		};

		// spacer & go to next line
		classData.viewBox.decorator.shift(0, 2).nextLine;

		// white line
		StaticText(classData.viewBox, 1000 @ 1).background_(TXColor.white);
		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);
		// preset name
		StaticText(classData.viewBox, 80 @ 20)
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
		.string_("Preset Name");

		// preset text field
		classData.presetTextField = TextField(classData.viewBox, 304 @ 20)
		.value_(classData.presetText)
		.action_({|view|
			classData.presetText = view.string;
		});
		// clear text
		holdView = Button(classData.viewBox, 20 @ 20)
		.states_([
			["x", TXColor.white, TXColor.sysDeleteCol]
		])
		.action_({|view|
			classData.presetText = "";
			classData.presetTextField.value_(classData.presetText);
		});
		classData.viewBox.decorator.shift(8,0);

		// save preset
		holdView = Button(classData.viewBox, 180 @ 20)
		.states_([
			["Save preset using Preset Name", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			this.savePreset(classData.presetText);
		});

		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);

		// popup: "Preset"
		classData.popupPreset = TXPopupPlusMinus(classData.viewBox, 460 @ 20, "Preset",
			this.getPresetNames,
			{arg view;
				classData.presetIndex = view.value;
			},
			initVal: classData.presetIndex;
		);
		classData.viewBox.decorator.shift(8,0);

		// "Load selected preset"
		holdView = Button(classData.viewBox, 140 @ 20)
		.states_([
			["Load selected preset", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			var holdText = this.getPresetNames[classData.popupPreset.value];
			this.loadPreset(holdText);
		});
		classData.viewBox.decorator.shift(8,0);

		// "Delete selected preset"
		holdView = Button(classData.viewBox, 140 @ 20)
		.states_([
			["Delete selected preset", TXColor.white, TXColor.sysDeleteCol]
		])
		.action_({|view|
			var holdText = this.getPresetNames[classData.popupPreset.value];
			this.deletePreset(holdText);
		});

		// spacer & next line
		classData.viewBox.decorator.shift(0, 4).nextLine;
		// white line
		StaticText(classData.viewBox, 1000 @ 1).background_(TXColor.white);
		// next line
		classData.viewBox.decorator.nextLine.shift(0, 10);

		// popup: "Curve Type"
		holdView = TXPopupPlusMinus(classData.viewBox, 320 @ 20, "Curve Type",
			["Auto Curve", "Straight Line", "Bezier - Control P1", "Bezier - Control P1 smoothed", "Bezier - Control P1 + P2"],
			{arg view;
				classData.curveData[\curveType] = classData.options.arrCurveTypes[view.value];
				this.showWindow;
			},
			initVal: classData.options.arrCurveTypes.indexOf(classData.curveData[\curveType]);
		);
		classData.viewBox.decorator.shift(4,0);

		// popup: "No. Points"
		holdView = TXPopupPlusMinus(classData.viewBox, 180 @ 20, "No. Points",
			["2", "3", "4", "5", "6", "7", "8"],
			{arg view;
				classData.curveData[\numPoints] = view.value + 2;
				this.showWindow;
			},
			initVal: classData.curveData[\numPoints] - 2;
		);
		classData.viewBox.decorator.shift(8,0);

		// CheckBox: "Auto Close"
		holdView = TXCheckBox.new(classData.viewBox, 90 @ 20, "Auto Close",
			TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
		holdView.value = classData.curveData[\autoClose].binaryValue;
		holdView.action = {arg view;
			classData.curveData[\autoClose] = view.value.asBoolean;
			this.showWindow;
		};
		classData.viewBox.decorator.shift(4,0);

		if (classData.curveData[\curveType] == \autoCurve, {
			// CheckBox: "Show auto curve start"
			holdView = TXCheckBox.new(classData.viewBox, 198 @ 20, "Use first draw point for AC Start",
				TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
			holdView.value = classData.curveData[\usePoint1ForAutoCurveStart].binaryValue;
			holdView.action = {arg view;
				classData.curveData[\usePoint1ForAutoCurveStart] = view.value.asBoolean;
				this.showWindow;
			};
			classData.viewBox.decorator.shift(4,0);

			// CheckBox: "auto curve end"
			holdView = TXCheckBox.new(classData.viewBox, 198 @ 20, "Use last draw point for AC End",
				TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
			holdView.value = classData.curveData[\useLastPointForAutoCurveEnd].binaryValue;
			holdView.action = {arg view;
				classData.curveData[\useLastPointForAutoCurveEnd] = view.value.asBoolean;
				this.showWindow;
			};
		});

		// spacer & next line
		classData.viewBox.decorator.shift(0, 4).nextLine;
		// white line
		StaticText(classData.viewBox, 1000 @ 1).background_(TXColor.white);
		// spacer & next line
		classData.viewBox.decorator.shift(0, 10).nextLine;

		// // CheckBox: "Show Points"
		// holdView = TXCheckBox.new(classData.viewBox, 110 @ 20, "Show Points",
		// TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
		// holdView.value = classData.options.showPoints.binaryValue;
		// holdView.action = {arg view;
		// 	classData.options.showPoints = view.value.asBoolean;
		// 	this.showWindow;
		// };
		//
		// classData.viewBox.decorator.shift(10,0);
		// // CheckBox: "Show Point Names"
		// holdView = TXCheckBox.new(classData.viewBox, 130 @ 20, "Show Point Names",
		// TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
		// holdView.value = classData.options.showPointNames.binaryValue;
		// holdView.action = {arg view;
		// 	classData.options.showPointNames = view.value.asBoolean;
		// 	this.showWindow;
		// };
		//
		// classData.viewBox.decorator.shift(10,0);
		// // CheckBox: "Show Grid"
		// holdView = TXCheckBox.new(classData.viewBox, 110 @ 20, "Show Grid",
		// TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
		// holdView.value = classData.options.showGrid.binaryValue;
		// holdView.action = {arg view;
		// 	classData.options.showGrid = view.value.asBoolean;
		// 	this.showWindow;
		// };
		//
		// classData.viewBox.decorator.shift(10,0);



		// spacer & go to next line
		//classData.viewBox.decorator.shift(0, 6).nextLine;

		/*
		// first 5 clipboardCurves
		// StaticText
		holdView = StaticText(classData.viewBox, 110 @ 20);
		holdView.string = "Drag & drop curves";
		holdView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// spacer & go to next line
		classData.viewBox.decorator.shift(0, 4).nextLine;
		5.do({arg i;
			// create curve
			var curveWidth = 80;
			var curveHeight = 60;
			var curveView = UserView(classData.viewBox, Rect(0, 0, curveWidth, curveHeight));
			curveView.background = TXColor.white;
			curveView.drawFunc = {
				var holdArray = classData.clipboardCurves[i];
				var holdArraySize = holdArray.size;
				var holdX, holdY;
				Pen.use {
					Pen.width = 1.0;
					Pen.strokeColor = TXColor.black.blend(TXColor.red, 0.35);
					Pen.beginPath;
					Pen.moveTo(0 @ (curveHeight * (1 - holdArray[0])));
					holdArray.do({arg item, i;
						if (i> 0, {
							Pen.lineTo((curveWidth * i/(holdArraySize - 1)) @ (curveHeight * (1 - holdArray[i])));
						});
					});
					Pen.stroke;
					Pen.strokeColor = TXColor.sysGuiCol1.blend(Color.grey, 0.5);
					holdArray.do({arg item, i;
						var p1, p2;
						p1 = (curveWidth * i/(holdArraySize - 1)) @ (curveHeight * (1 - holdArray[i]));
						p2 = (curveWidth * i/(holdArraySize - 1)) @ curveHeight;
						Pen.line(p1, p2);
						Pen.stroke;
					});
				};
			};
			curveView.refresh;
			// drag view
			holdView = DragBoth(curveView, Rect(0, 0, curveWidth, curveHeight));
			holdView.background = TXColor.white.alpha_(0);
			holdView.beginDragAction_({ arg view, x, y;
				view.dragLabel = "Curve (size: " ++ classData.clipboardCurves[i].size.asString ++")" ;
				classData.clipboardCurves[i];
			});
			holdView.canReceiveDragHandler = {
				View.currentDrag.isKindOf( Array ) and: {View.currentDrag[0].isKindOf( Number )};
			};
			holdView.receiveDragHandler = {arg view;
				var holdDragObject;
				holdDragObject = this.resampleArray(View.currentDrag, classData.curveSize);
				classData.clipboardCurves[i] = holdDragObject;
				curveView.refresh;
			};
		});
		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);
		*/

		arrGridPresetNames = ["Grid presets..", "6 x 6", "8 x 8", "9 x 9", "10 x 10", "12 x 12", "16 x 16", "20 x 20",
			"24 x 24", "30 x 30", "32 x 32", "40 x 40", "50 x 50", "60 x 60", "80 x 80", "90 x 90", "100 x 100"];
		arrGridPresetActions = [
			{},
			{this.setGridRowsCols(6);},
			{this.setGridRowsCols(8);},
			{this.setGridRowsCols(9);},
			{this.setGridRowsCols(10);},
			{this.setGridRowsCols(12);},
			{this.setGridRowsCols(16);},
			{this.setGridRowsCols(20);},
			{this.setGridRowsCols(24);},
			{this.setGridRowsCols(30);},
			{this.setGridRowsCols(32);},
			{this.setGridRowsCols(40);},
			{this.setGridRowsCols(50);},
			{this.setGridRowsCols(60);},
			{this.setGridRowsCols(80);},
			{this.setGridRowsCols(90);},
			{this.setGridRowsCols(100);},
		];

		// grid rows
		gridSizeControlSpec = ControlSpec.new(1, 128, step:1);
		classData.gridRowsView = TXNumber(classData.viewBox, 60 @ 20, "Grid Rows", gridSizeControlSpec,
			{arg view; this.setGridRows(view.value);}, classData.gridRows, numberWidth: 40);
		classData.gridRowsView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		Button(classData.viewBox, 14 @ 20)
		.states_([["+", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			classData.gridRowsView.valueAction_(gridSizeControlSpec.constrain(classData.gridRowsView.value + 1));
		});
		Button(classData.viewBox, 14 @ 20)
		.states_([["-", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			classData.gridRowsView.valueAction_(gridSizeControlSpec.constrain(classData.gridRowsView.value - 1));
		});
		// grid cols
		classData.gridColsView = TXNumber(classData.viewBox, 60 @ 20, "Grid Columns", gridSizeControlSpec,
			{arg view; this.setGridCols(view.value);}, classData.gridCols, numberWidth: 40);
		classData.gridColsView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		Button(classData.viewBox, 14 @ 20)
		.states_([["+", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			classData.gridColsView.valueAction_(gridSizeControlSpec.constrain(classData.gridColsView.value + 1));
		});
		Button(classData.viewBox, 14 @ 20)
		.states_([["-", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			classData.gridColsView.valueAction_(gridSizeControlSpec.constrain(classData.gridColsView.value - 1));
		});
		// Grid Presets
		holdView = TXPopup(classData.viewBox, 100@20, "", arrGridPresetNames,
			{ arg view; arrGridPresetActions[view.value].value; },0, labelWidth: 0);
		//holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);

		// shift
		classData.viewBox.decorator.shift(20,0);

		classData.imageDragView = DragSink(classData.viewBox, 200 @ 26);
		classData.imageDragView.setBoth = false;
		classData.imageDragView.string = "drop background PNG image file";
		classData.imageDragView.align = \center;
		classData.imageDragView.background_(TXColor.paleYellow);
		classData.imageDragView.stringColor_(TXColor.black);
		classData.imageDragView.canReceiveDragHandler = {View.currentDrag.isKindOf(String);};
		classData.imageDragView.receiveDragHandler = {arg view;
			var imageName, tempImage;
			var dragObject = View.currentDrag;
			if (dragObject.notNil
				and: {dragObject.class == String}, {
					imageName = dragObject;
					if (this.isValidImageFile(imageName), {
						classData.curveView.setBackgroundImage(imageName);
					});
			});
		};
		Button(classData.viewBox, 160 @ 20)
		.states_([["Remove background image", TXColor.white, TXColor.sysDeleteCol]])
		.action_({
			classData.curveView.setBackgroundImage(nil);
		});

		// shift
		classData.viewBox.decorator.shift(20,0);

		Button(classData.viewBox, 110 @ 20)
		.states_([["Print list of points", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.printListOfPoints;
		});

		// white line
		StaticText(classData.viewBox, 1000 @ 1).background_(TXColor.white);
		// next line
		classData.viewBox.decorator.nextLine.shift(0, 10);

		// curve
		classData.curveView = TXPointsCurveView.new(classData.viewBox, 900 @ 700, "Curve Edit Mode",
			{arg view; classData.curveData.putAll(view.value);},
			classData.curveData, curveWidth: 700, curveHeight: 700,  dataEvent: classData.curveViewData,
			optionsEvent: classData.options
		);

		this.updateGrids;

		/*
		// more clipboardCurves

		// next line & shift
		classData.viewBox.decorator.nextLine.shift(0, 6);
		// StaticText
		holdView = StaticText(classData.viewBox, 110 @ 20);
		holdView.string = "Drag & drop curves";
		holdView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);

		(5..24).do({arg i;
			// create curve
			var curveWidth = 80;
			var curveHeight = 60;
			var curveView = UserView(classData.viewBox, Rect(0, 0, curveWidth, curveHeight));
			curveView.background = TXColor.white;
			curveView.drawFunc = {
				var holdArray = classData.clipboardCurves[i];
				var holdArraySize = holdArray.size;
				var holdX, holdY;
				Pen.use {
					Pen.width = 1.0;
					Pen.strokeColor = TXColor.black.blend(TXColor.red, 0.35);
					Pen.beginPath;
					Pen.moveTo(0 @ (curveHeight * (1 - holdArray[0])));
					holdArray.do({arg item, i;
						if (i> 0, {
							Pen.lineTo((curveWidth * i/(holdArraySize - 1)) @ (curveHeight * (1 - holdArray[i])));
						});
					});
					Pen.stroke;
					Pen.strokeColor = TXColor.sysGuiCol1.blend(Color.grey, 0.5);
					holdArray.do({arg item, i;
						var p1, p2;
						p1 = (curveWidth * i/(holdArraySize - 1)) @ (curveHeight * (1 - holdArray[i]));
						p2 = (curveWidth * i/(holdArraySize - 1)) @ curveHeight;
						Pen.line(p1, p2);
						Pen.stroke;
					});
				};
			};
			curveView.refresh;
			// drag view
			holdView = DragBoth(curveView, Rect(0, 0, curveWidth, curveHeight));
			holdView.background = TXColor.white.alpha_(0);
			holdView.beginDragAction_({ arg view, x, y;
				view.dragLabel = "Curve (size: " ++ classData.clipboardCurves[i].size.asString ++")" ;
				classData.clipboardCurves[i];
			});
			holdView.canReceiveDragHandler = {
				View.currentDrag.isKindOf( Array ) and: {View.currentDrag[0].isKindOf( Number )};
			};
			holdView.receiveDragHandler = {arg view;
				var holdDragObject;
				holdDragObject = this.resampleArray(View.currentDrag, classData.curveSize);
				classData.clipboardCurves[i] = holdDragObject;
				curveView.refresh;
			};
			if (i == 14, {
				classData.viewBox.decorator.nextLine.shift(114, 0);
			});
		});
		*/

		curveEditorWindow.front;

		// reset Default Font
		Font.default = holdFont;

		curveEditorWindow.front;
		curveEditorWindow.view.focus(true);


	} // end of showWindow

	*savePreset {arg presetName;
		//("Saving Preset Name: " ++ presetName).postln;
		if (presetName != "", {

			classData.pointsCurvePresetData[presetName.asSymbol] = classData.curveData.deepCopy;
			// save to file
			this.savePresetDataToFile;
			this.showWindow;
		});
	}

	*loadPreset {arg presetName;
		var holdPreset = classData.pointsCurvePresetData[presetName.asSymbol];
		//("Loading Preset " ++ presetName).postln;
		if (holdPreset.notNil, {
			classData.curveData = holdPreset.deepCopy;
			this.showWindow;
		});
	}

	*deletePreset {arg presetName;
		classData.pointsCurvePresetData[presetName.asSymbol] = nil;
		classData.presetIndex = 0;
		// save to file
		this.savePresetDataToFile;
		this.showWindow;
	}

	*savePresetDataToFile {
		var holdFolder, holdFile, holdFileData, holdPath;
		holdFolder = PathName.new(Platform.userAppSupportDir +/+ "TXModular");
		holdPath = PathName.new(Platform.userAppSupportDir +/+ "TXModular/TXPointsCurvePresets.tx");
		// if directory doesn't exist, create it.
		if (holdFolder.isFolder.not, {
			holdFolder.fullPath.makeDir;
		});
		holdFileData = [
			"TX Modular System TXV Points Curve Preset Data",
			classData.pointsCurvePresetData,
		];
		holdFileData.writeArchive(holdPath.fullPath);
	}

	*loadPresetDataFromFile {
		var holdFolder, holdPath, holdFileData;
		holdFolder = PathName.new(Platform.userAppSupportDir +/+ "TXModular");
		holdPath = PathName.new(Platform.userAppSupportDir +/+ "TXModular" +/+ "TXPointsCurvePresets.tx");
		// if directory doesn't exist, create it.
		if (holdFolder.isFolder.not, {
			holdFolder.fullPath.makeDir;
		});
		holdFileData = Object.readArchive(holdPath.fullPath);
		if (holdFileData.notNil, {
			classData.pointsCurvePresetData = holdFileData[1];
		}, {
			classData.pointsCurvePresetData = ();
		});
	}

	// ------------
	// WARNING - this will delete all saved presets
	// class method
	*deleteAllPresetData {
		var emptyEvent = ();
		var holdFile, holdFileData;

		holdFile = PathName.new(Platform.userAppSupportDir +/+ "TXModular" +/+ "TXPointsCurvePresets.tx");
		// if file exists, delete it.
		if (File.exists(holdFile.fullPath),  {
			// ("Deleting " ++ holdFile.fullPath).postln;
			File.delete(holdFile.fullPath);
		});
		classData.pointsCurvePresetData = ();
		classData.presetIndex = 0;

		/* Using Archive
		Archive.global.put(\TXPointsCurvePresets, emptyEvent);
		*/

	}
	// ------------

	*printListOfPoints {
		"////////////////////////////".postln;
		"Curve Point Editor".postln;
		"List of points:".postln;
		classData.pointSymbols.do({arg item, i;
			// curvePointData[item] = Point(0.2 + (0.7666666 * i / (classData.pointSymbols.size - 1)), 0.033333 + ((i % 3) * 0.033333));
			//().postln;
			var point = classData.curveData[item];
			"===".postln;
			("-" + item.asString + "X:" + point.x.round(0.001).asString).postln;
			(" " + item.asString + "Y:" + point.y.round(0.001).asString).postln;
		});
		"////////////////////////////".postln;
	}

	*getPresetNames {
		var outArray = [];
		if (classData.pointsCurvePresetData.notNil, {
			outArray = classData.pointsCurvePresetData.keys.asArray.collect({arg item; item.asString}).sort({arg a, b;
			a.toLower < b.toLower});  // ignore case in sort
		});
		^outArray;
	}

	*getDragObject{
		var outData = ();
		outData.label = "Points Curve Data";
		// add point data
		outData.curveData = classData.curveData;
		^outData;
	}
}


