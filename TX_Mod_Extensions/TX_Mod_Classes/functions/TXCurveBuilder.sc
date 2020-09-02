// Copyright (C) 2016 Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXCurveBuilder {
	classvar <>curveBuilderWindow;
	classvar classData;

	*initClass {
		classData = ();
		//classData.curveSize = 256;
		classData.curveSize = 700;
		classData.gridRows = 8;
		classData.gridCols = 8;
		classData.curveHeight = 230;
		classData.arrSlotData = Array.newClear(classData.curveSize).seriesFill(0, 1 / (classData.curveSize-1)) ! 5;
		classData.curve1 = Array.newClear(classData.curveSize).seriesFill(0, 1 / (classData.curveSize-1));
		classData.curve2 = Array.newClear(classData.curveSize).seriesFill(0, 1 / (classData.curveSize-1));
		classData.curve3 = Array.newClear(classData.curveSize).seriesFill(0, 1 / (classData.curveSize-1));
		classData.curve4 = Array.newClear(classData.curveSize).seriesFill(0, 1 / (classData.curveSize-1));
		classData.clipboardCurves = Array.newClear(classData.curveSize).seriesFill(0, (classData.curveSize - 1).reciprocal) ! 25;
		classData.arrCurve1Parms = ();
		classData.arrCurve2Parms = ();
		classData.arrCurve3Parms = ();
		classData.arrCurve4Parms = ();
		classData.autoResizeCurve1 = true;
		classData.showCurve1ParameterFX = true;
		classData.showCurve1And2FX = false;
		classData.showFXMixCurve = false;
		classData.showEnvelopeControls = false;
		classData.fxActiveRangeMin = 0;
		classData.fxActiveRangeMax = 1;
		classData.parameter1 = 0.0;
		classData.parameter2Min = 0.0;
		classData.parameter2Max = 1.0;
		classData.fxParm1Parm2Index = 0;
		classData.fxCurve1Curve2Index = 0;
		classData.fxMix = 1;
		classData.windowPosX = 400;
		classData.windowPosY = 400;
		classData.windowVisibleOrigin = Point.new(0,0);
		//classData.envelope = Env.adsr(attackTime: 0.1, decayTime: 0.2, sustainLevel: 0.5, releaseTime: 0.6);
		classData.defaultEnvelope = Env.new(
			[0, 1.0, 0.25, 0],
			[0.1, 0.1, 0.1],
			-4.0,
			nil, nil
		);
		classData.envelope = classData.defaultEnvelope.copy;
		classData.alwaysOnTop = 1;
		classData.autoRunFX = 1;
		classData.lockZoom = true;
		classData.defaultSpec = [0,1].asSpec;
		this.clearViews;
	}

	*closeWindow {
		if (curveBuilderWindow.notNil, {
			classData.windowPosX = curveBuilderWindow.bounds.left;
			classData.windowPosY = curveBuilderWindow.bounds.top;
			curveBuilderWindow.close;
		});
		this.clearViews;
	}

	*clearViews {
		classData.viewBox = nil;
		classData.gridRowsView = nil;
		classData.gridColsView = nil;
		classData.curveHeightView = nil;
		classData.curve1View = nil;
		classData.curve2View = nil;
		classData.curve3View = nil;
		classData.curve4View = nil;
	}

	*rebuildWindow {
		this.closeWindow;
		{this.showWindow;}.defer(0.03);
	}

	*openWithCurve {arg curve;
		this.curve1Changed(curve);
		this.setCurveSize(curve.size);
		this.showWindow;
	}

	*setCurveSize {arg argSize;
		classData.curveSize = argSize;
		// change all curves
		classData.curve1 = this.resampleArray(classData.curve1, argSize);
		classData.curve2 = this.resampleArray(classData.curve2, argSize);
		classData.curve3 = this.resampleArray(classData.curve3, argSize);
		classData.curve4 = this.resampleArray(classData.curve4, argSize);
		// clipboardCurves
		classData.clipboardCurves.size.do({arg i;
			classData.clipboardCurves[i] = this.resampleArray(classData.clipboardCurves[i], argSize);
		});
		this.showWindow;
	}

	*resampleArray {arg inArray, newSize;
		var oldSize = inArray.size;
		var outArray;
		if (inArray.size == newSize, {
			outArray = inArray;
		}, {
			outArray = newSize.asInteger.collect({ arg i;
				inArray.blendAt(((oldSize-1) * i / (newSize-1)));
			});
		});
		^outArray;
	}

	*setCurveHeight {arg val;
		classData.curveHeight = val;
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
		classData.curve1View.setGridRowsCols(classData.gridRows, classData.gridCols);
		if (classData.showCurve1And2FX, {
			classData.curve2View.setGridRowsCols(classData.gridRows, classData.gridCols);
		});
		if (classData.curve3View.notNil, {
			classData.curve3View.setGridRowsCols(classData.gridRows, classData.gridCols);
		});
		classData.curve4View.setGridRowsCols(classData.gridRows, classData.gridCols);
	}

	//======================

	*quantizeToGrid {arg inputArray, quantizeRows = true, quantizeCols = true;
		var holdArray, holdSignal, outArray, holdCols;
		var maxVal = inputArray.size;
		holdArray = Array.newClear(maxVal);
		if (quantizeCols, {
			classData.gridCols.do({arg item;
				var jump, startRange, endRange, meanVal;
				jump = classData.gridCols.reciprocal;
				startRange = (item * jump * maxVal).round(1);
				endRange = ((item + 1) * jump * maxVal).round(1) - 1;
				meanVal = inputArray.copyRange(startRange.asInteger, endRange.asInteger).mean;
				if (quantizeRows, {
					meanVal = meanVal.round(classData.gridRows.reciprocal);
				});
				holdArray[startRange.asInteger..endRange.asInteger] = meanVal;
			});
		},{
			holdArray = inputArray.collect({arg item;
				var outVal = item;
				if (quantizeRows, {
					outVal = outVal.round(classData.gridRows.reciprocal);
				});
				outVal;
			});
		});
		holdSignal = Signal.newFrom(holdArray);
		outArray = Array.newFrom(holdSignal);
		^outArray;
	}
	*quantizeCurve1 {arg quantizeRows = true, quantizeCols = true;
		this.curve1Changed(this.quantizeToGrid(classData.curve1, quantizeRows, quantizeCols));
		this.updateCurve1View;
	}
	*quantizeCurve2 {arg quantizeRows = true, quantizeCols = true;
		this.curve2Changed(this.quantizeToGrid(classData.curve2, quantizeRows, quantizeCols));
		this.updateCurve2View;
	}
	*quantizeCurve3 {arg quantizeRows = true, quantizeCols = true;
		this.curve3Changed(this.quantizeToGrid(classData.curve3, quantizeRows, quantizeCols));
		this.updateCurve3View;
	}
	*quantizeCurve4 {arg quantizeRows = true, quantizeCols = true;
		this.curve4Changed(this.quantizeToGrid(classData.curve4, quantizeRows, quantizeCols));
		this.updateCurve4View;
	}

	//======================

	*updateCurve1View {
		classData.curve1View.value = classData.curve1;
	}
	*updateCurve2View {
		if (classData.curve2View.notNil, {classData.curve2View.value = classData.curve2;});
	}
	*updateCurve3View {
		classData.curve3View.value = classData.curve3;
	}
	*updateCurve4View {
		classData.curve4View.value = classData.curve4;
	}

	//======================

	// store curve vals
	*curve1Changed {arg newArray;
		classData.curve1 = newArray;
	}
	*curve2Changed {arg newArray;
		classData.curve2 = newArray;
	}
	*curve3Changed {arg newArray;
		classData.curve3 = newArray;
	}
	*curve4Changed {arg newArray;
		classData.curve4 = newArray;
	}

	//======================


	*showWindow {
		var screenWidth = 1000;
		var screenHeight = 1000;
		var scrollView, holdView;
		var curve1Width, curve2Width, curve3Width, curve4Width;
		var arrGridPresetNames, arrGridPresetActions;
		var holdFont, gridSizeControlSpec;
		var activeRangeSlider, parm1Slider, parm2RangeSlider, fxMixSlider;

		// Set Default Font
		holdFont = Font.default;
		Font.default = Font("Helvetica", 12);

		// create window if needed
		if (curveBuilderWindow.isNil, {
			curveBuilderWindow = Window.new(
				"Curve Builder - drag & drop curves",
				Rect(classData.windowPosX, classData.windowPosY, screenWidth, screenHeight),
				resizable: true, scroll: true
			).front;
			curveBuilderWindow.view.action = {arg view; classData.windowVisibleOrigin = view.visibleOrigin;};
			curveBuilderWindow.view.mouseWheelAction = {arg view; classData.windowVisibleOrigin = view.visibleOrigin;};
		});
		curveBuilderWindow.front;

		// defer visible origin
		{curveBuilderWindow.view.visibleOrigin = classData.windowVisibleOrigin;}.defer(0.2);

		//curveBuilderWindow.view.hasHorizontalScroller = false;
		curveBuilderWindow.view.background_(TXColor.grey(0.76));
		curveBuilderWindow.onClose_({this.curveBuilderWindow = nil;});
		curveBuilderWindow.alwaysOnTop = classData.alwaysOnTop.asBoolean;

		// // add ScrollView
		// scrollView = ScrollView(curveBuilderWindow, Rect(0, 0, screenWidth-15, screenHeight-10))
		// .hasBorder_(false);
		// scrollView.action = {classData.visibleOrigin = scrollView.visibleOrigin};
		// scrollView.hasHorizontalScroller = false;
		// scrollView.hasVerticalScroller = true;

		if (classData.viewBox.notNil, {
			TXSystem1GUI.deferRemoveView(classData.viewBox);
		});

		// make box to display
		classData.viewBox = CompositeView(curveBuilderWindow, Rect(4, 4, 1400, 3000))
		.background_(TXColor.clear);
		classData.viewBox.decorator = FlowLayout(classData.viewBox.bounds);

		// set visibleOrigin
		if (classData.visibleOrigin.notNil, {scrollView.view.visibleOrigin = classData.visibleOrigin; });

		Button(classData.viewBox, 40 @ 20)
		.states_([
			["Help", TXColor.white, TXColor.sysHelpCol]
		])
		.action_({|view|
			TXHelpScreen.openFile("TX_Curve Builder");
		});

		classData.viewBox.decorator.shift(20,0);

		// CheckBox: "Window on top"
		holdView = TXCheckBox(classData.viewBox, Rect(0, 0, 120, 20), "window on top",
			TXColor.sysGuiCol1, TXColor.white, TXColor.white, TXColor.sysGuiCol1);
		holdView.value = classData.alwaysOnTop;
		holdView.action = {|view|
			classData.alwaysOnTop = view.value;
			curveBuilderWindow.alwaysOnTop = classData.alwaysOnTop.asBoolean;
		};
		classData.viewBox.decorator.shift(20,0);

		// // CheckBox: "Show Envelope"
		// holdView = TXCheckBox.new(classData.viewBox, 130 @ 20, "Show Envelope",
		// TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
		// holdView.value = classData.showEnvelopeControls;
		// holdView.action = {arg view;
		// 	classData.showEnvelopeControls = view.value.asBoolean;
		// 	this.showWindow;
		// };
		// classData.viewBox.decorator.shift(20,0);

		// // CheckBox: "Show Curve 1 Parameter FX"
		// holdView = TXCheckBox.new(classData.viewBox, 190 @ 20, "Show Curve 1 Parameter FX",
		// TXColor.sysGuiCol2, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol2);
		// holdView.value = classData.showCurve1ParameterFX;
		// holdView.action = {arg view;
		// 	classData.showCurve1And2FX = false;
		// 	classData.showCurve1ParameterFX = view.value.asBoolean;
		// 	this.showWindow;
		// };
		// classData.viewBox.decorator.shift(20,0);
		//
		// // CheckBox: "Show Curve 1 & 2 FX"
		// holdView = TXCheckBox.new(classData.viewBox, 160 @ 20, "Show Curve 1 & 2 FX",
		// TXColor.sysGuiCol2, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol2);
		// holdView.value = classData.showCurve1And2FX;
		// holdView.action = {arg view;
		// 	classData.showCurve1ParameterFX = false;
		// 	classData.showCurve1And2FX = view.value.asBoolean;
		// 	this.showWindow;
		// };
		// classData.viewBox.decorator.shift(20,0);
		//
		// // CheckBox: "Show FX Mix Curve 3"
		// holdView = TXCheckBox.new(classData.viewBox, 190 @ 20, "Show FX Mix Curve 3",
		// TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
		// holdView.value = classData.showFXMixCurve;
		// holdView.action = {arg view;
		// 	classData.showFXMixCurve = view.value.asBoolean;
		// 	this.checkAutorun;
		// 	this.showWindow;
		// };
		// classData.viewBox.decorator.shift(20,0);

		arrGridPresetNames = ["Grid presets..", "1 x 1", "2 x 2", "3 x 3", "4 x 4", "5 x 5", "6 x 6", "8 x 8", "9 x 9",
			"10 x 10", "12 x 12", "16 x 16", "20 x 20", "24 x 24", "30 x 30", "32 x 32"];
		arrGridPresetActions = [
			{},
			{this.setGridRowsCols(1);},
			{this.setGridRowsCols(2);},
			{this.setGridRowsCols(3);},
			{this.setGridRowsCols(4);},
			{this.setGridRowsCols(5);},
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
		];

		// spacer & go to next line
		classData.viewBox.decorator.shift(0, 6).nextLine;
		// NumberBox: "Curve Size" (will resize all curves)
		holdView = TXNumber(classData.viewBox, 60 @ 20, "Curve size", ControlSpec.new(32, 1024, step:1),
			{arg view; this.setCurveSize(view.value);}, classData.curveSize, numberWidth: 40);
		holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// StaticText: "Note: All curves will be resampled to be the same size as curve 1"
		holdView = StaticText(classData.viewBox, 366 @ 20);
		holdView.string = "Note: All curves will be resampled to be the same size as Curve 1";
		holdView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		holdView.setProperty(\align,\left);
		// space
		classData.viewBox.decorator.shift(146, 0);
		// StaticText
		holdView = StaticText(classData.viewBox, 110 @ 20);
		holdView.string = "Drag & drop curves";
		holdView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// spacer & go to next line
		classData.viewBox.decorator.shift(0, 4).nextLine;
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
		// space
		classData.viewBox.decorator.shift(70,0);
		// first 5 clipboardCurves
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
		classData.viewBox.decorator.nextLine;
		classData.viewBox.decorator.shift(0, -36);
		classData.curveHeightView = TXNumber(classData.viewBox, 60 @ 20, "Curve Height", ControlSpec.new(230, 700, step:1),
			{arg view; this.setCurveHeight(view.value);}, classData.curveHeight, numberWidth: 40);
		classData.curveHeightView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// Buttons
		Button(classData.viewBox, 40 @ 20)
		.states_([
			["230", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			this.setCurveHeight(230);
		});
		Button(classData.viewBox, 40 @ 20)
		.states_([
			["256", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			this.setCurveHeight(256);
		});
		Button(classData.viewBox, 40 @ 20)
		.states_([
			["300", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			this.setCurveHeight(300);
		});
		Button(classData.viewBox, 40 @ 20)
		.states_([
			["400", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			this.setCurveHeight(400);
		});
		Button(classData.viewBox, 40 @ 20)
		.states_([
			["500", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			this.setCurveHeight(500);
		});
		classData.viewBox.decorator.shift(0, 6);

		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);
		// line
		StaticText(classData.viewBox, 1000 @ 2).background_(TXColor.white);
		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);

		// CheckBox: "Show Envelope"
		holdView = TXCheckBox.new(classData.viewBox, 130 @ 20, "Show Envelope",
			TXColor.sysGuiCol2, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol2);
		holdView.value = classData.showEnvelopeControls;
		holdView.action = {arg view;
			classData.showEnvelopeControls = view.value.asBoolean;
			this.showWindow;
		};

		if (classData.showEnvelopeControls == true, {
			classData.viewBox.decorator.nextLine.shift(0, 2);

			holdView = StaticText(classData.viewBox, 120 @ 20);
			holdView.string = "Envelope";
			holdView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			holdView.align = \right;

			// Env view
			classData.envelope = classData.envelope.duration_(0.8);
			classData.envView = TXEnvScaleView(classData.viewBox,
				(classData.curveSize.max(350) + 50) @ (classData.curveHeight + 40), classData.envelope);
			// classData.envView.userView.mouseUpAction = {|view|
			// 	var tempEnv;
			// 	{
			// 		tempEnv= classData.envView.env.copy.duration_(0.8);
			// 		classData.envelope = tempEnv;
			// 		//classData.envView.env_(tempEnv, true);
			// 		classData.envView.maxRange_(1, true);
			// 	}.defer(0.1);
			// };
			{
				classData.envView.lockZoom_(classData.lockZoom.asBoolean);
				classData.envView.showTopRow_(false);
				classData.envView.showBottomRow_(false);
				classData.envView.maxRange_(1.0, true);
			}.defer(0.05);

			// next line
			classData.viewBox.decorator.nextLine;

			// Button: Default Envelope
			Button(classData.viewBox, 120 @ 20)
			.states_([
				["Default Envelope", TXColor.white, TXColor.sysDeleteCol]
			])
			.action_({|view|
				classData.envelope = classData.defaultEnvelope.copy;
				this.rebuildWindow;
			});

			holdView = StaticText(classData.viewBox, 180 @ 20);
			holdView.string = "Ctl & Click to add/remove nodes";
			holdView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			holdView.align = \center;

			// checkbox: lock zoom
			holdView = TXCheckBox.new(classData.viewBox, 90 @ 20, "Lock Zoom",
				TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol2);
			holdView.value = classData.lockZoom.asInt;
			holdView.action = {arg view;
				classData.envView.lockZoom_(view.value.asBoolean);
				classData.lockZoom = view.value.asBoolean;
			};

			// Button: reset zoom
			Button(classData.viewBox, 70 @ 20)
			.states_([
				["Reset Zoom", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				classData.envView.horzGridDist_(10);
				classData.envView.minRange_(0,false);
				classData.envView.maxRange_(1,false);
				classData.envView.scaleResponsiveness_(3);
				this.rebuildWindow;
			});

			classData.viewBox.decorator.shift(20, 0);
			//  Button: Copy to Curve 1
			Button(classData.viewBox, 96 @ 20)
			.states_([
				["Copy to Curve 1", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				var newArray = classData.envelope.discretize(classData.curve1.size).as(Array);
				this.curve1Changed(newArray);
				this.updateCurve1View;
			});
			//  Button: Copy to Curve 2
			Button(classData.viewBox, 96 @ 20)
			.states_([
				["Copy to Curve 2", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				var newArray = classData.envelope.discretize(classData.curve2.size).as(Array);
				this.curve2Changed(newArray);
				this.updateCurve2View;
			});
			//  Button: Copy to Curve 3
			Button(classData.viewBox, 96 @ 20)
			.states_([
				["Copy to Curve 3", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				var newArray = classData.envelope.discretize(classData.curve3.size).as(Array);
				this.curve3Changed(newArray);
				this.updateCurve3View;
			});
			//  Button: Copy to Curve 4
			Button(classData.viewBox, 96 @ 20)
			.states_([
				["Copy to Curve 4", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				var newArray = classData.envelope.discretize(classData.curve4.size).as(Array);
				this.curve4Changed(newArray);
				this.updateCurve4View;
			});

		}); // end of if (classData.showEnvelopeControls == true

		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);
		// line
		StaticText(classData.viewBox, 1000 @ 2).background_(TXColor.white);
		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);

		// spacer
		//classData.viewBox.decorator.shift(124, 0);
		// CheckBox
		holdView = TXCheckBox.new(classData.viewBox, 400 @ 20, "Auto-resize all curves when Curve 1 receives a dragged curve",
			TXColor.sysGuiCol1, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
		holdView.value = classData.autoResizeCurve1;
		holdView.action = {arg view;
			classData.autoResizeCurve1 = (view.value).asBoolean;
		};
		// next line
		classData.viewBox.decorator.nextLine;
		// Curve: "Curve 1"
		if (classData.curveSize > 128, {
			classData.curveViewWidth = 8 + classData.curveSize;
		}, {
			classData.curveViewWidth = 8 + (classData.curveSize * 2);
		});
		classData.curve1View = TXCurveDraw(classData.viewBox, 750 @ classData.curveHeight, "Curve 1",
			{|view|
				this.curve1Changed(view.value);
				this.checkAutorun;
			},
			// get initial value
			classData.curve1,
			labelWidth: 120,
			initSlotVals: classData.arrSlotData,
			showPresets: "Curve",
			curveWidth: classData.curveViewWidth,
			curveHeight: classData.curveHeight,
			resetAction: "ramp",
			gridRowsFunc: {classData.gridRows},
			gridColsFunc: {classData.gridCols},
			xLabel: "x axis",
			yLabel: "y axis",
			showBuilderButton: false,
			dataEvent: classData.arrCurve1Parms
		);
		classData.curve1View.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		//	classData.curve1View.multiSliderView.isFilled_(true).fillColor_(TXColor.sysGuiCol1);
		//classData.curve1View.multiSliderView.strokeColor_(TXColor.sysGuiCol1);
		classData.curve1View.dragView.receiveDragHandler = {arg view;
			var newArray;
			var dragArray = View.currentDrag.value;
			if (classData.autoResizeCurve1, {
				// resize
				this.curve1Changed(dragArray);
				if (classData.curveSize != dragArray.size, {
					this.setCurveSize(dragArray.size);
				}, {
					this.updateCurve1View;
				});
			}, {
				newArray = this.resampleArray(dragArray, classData.curveSize);
				this.curve1Changed(newArray);
				this.updateCurve1View;
			});
			this.checkAutorun;
			view.focus(false);
		};

		// spacer
		classData.viewBox.decorator.shift(20, 0);
		// Button
		Button(classData.viewBox, 110 @ 20)
		.states_([
			["Quantize to Grid", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			this.quantizeCurve1(quantizeRows: true, quantizeCols: true);
			this.checkAutorun;
		});
		// Button
		Button(classData.viewBox, 110 @ 20)
		.states_([
			["Quantize Rows", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			this.quantizeCurve1(quantizeRows: true, quantizeCols: false);
			this.checkAutorun;
		});
		// Button
		Button(classData.viewBox, 110 @ 20)
		.states_([
			["Quantize Columns", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			this.quantizeCurve1(quantizeRows: false, quantizeCols: true);
			this.checkAutorun;
		});

		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);
		// line
		StaticText(classData.viewBox, 1000 @ 2).background_(TXColor.white);
		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);

		// // CheckBox: "Show Curve 1 Parameter FX"
		// holdView = TXCheckBox.new(classData.viewBox, 190 @ 20, "Show Curve 1 Parameter FX",
		// TXColor.sysGuiCol2, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol2);
		// holdView.value = classData.showCurve1ParameterFX;
		// holdView.action = {arg view;
		// 	classData.showCurve1And2FX = false;
		// 	classData.showCurve1ParameterFX = view.value.asBoolean;
		// 	this.showWindow;
		// };
		// classData.viewBox.decorator.shift(20,0);
		//
		// // CheckBox: "Show Curve 1 & 2 FX"
		// holdView = TXCheckBox.new(classData.viewBox, 160 @ 20, "Show Curve 1 & 2 FX",
		// TXColor.sysGuiCol2, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol2);
		// holdView.value = classData.showCurve1And2FX;
		// holdView.action = {arg view;
		// 	classData.showCurve1ParameterFX = false;
		// 	classData.showCurve1And2FX = view.value.asBoolean;
		// 	this.showWindow;
		// };

		// Curve 1 Parameter FX
		holdView = StaticText(classData.viewBox, Rect(0, 0, 150, 20))
		.background_(TXColor.sysGuiCol1)
		.align_(\center)
		.string_("Curve 1 Parameter FX");
		if (classData.showCurve1ParameterFX == false, {
			holdView.stringColor_(TXColor.white);
			holdView.mouseDownAction_({
				classData.showCurve1And2FX = false;
				classData.showCurve1ParameterFX = true;
				this.showWindow;
			});
		},{
			// holdView.background_(TXColor.sysGuiCol4);
			holdView.stringColor_(TXColor.paleYellow2);
		});
		classData.viewBox.decorator.shift(10,0);

		//Curve 1 & 2 FX
		holdView = StaticText(classData.viewBox, Rect(0, 0, 120, 20))
		.background_(TXColor.sysGuiCol1)
		.align_(\center)
		.string_("Curve 1 & 2 FX");
		if (classData.showCurve1And2FX == false, {
			holdView.stringColor_(TXColor.white);
			holdView.mouseDownAction_({
				classData.showCurve1And2FX = true;
				classData.showCurve1ParameterFX = false;
				this.showWindow;
			});
		},{
			// holdView.background_(TXColor.sysGuiCol4);
			holdView.stringColor_(TXColor.paleYellow2);
		});
		classData.viewBox.decorator.shift(30,0);

		// CheckBox: "Use Curve 3 for FX Mix"
		holdView = TXCheckBox.new(classData.viewBox, 170 @ 20, "Use Curve 3 for FX Mix",
			TXColor.sysGuiCol2, TXColor.grey(0.8), TXColor.white, TXColor.sysGuiCol2);
		holdView.value = classData.showFXMixCurve;
		holdView.action = {arg view;
			classData.showFXMixCurve = view.value.asBoolean;
			this.checkAutorun;
			this.showWindow;
		};
		classData.viewBox.decorator.nextLine.shift(0, 6);

		if (classData.showCurve1ParameterFX == true, {
			// Popup: "Parameter FX"
			holdView = TXPopupPlusMinus(classData.viewBox,600@20, "Curve 1 Parameter FX",
				this.getParm1Parm2Actions.collect({arg item; item[0];}),
				{ arg view; classData.fxParm1Parm2Index = view.value; this.parameterFXCheckAutorun;},
				classData.fxParm1Parm2Index, labelWidth: 120);
			holdView.labelView.stringColor_(TXColor.black).background_(TXColor.white);
			holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			// spacer
			classData.viewBox.decorator.shift(10, 0);
			// Button: Run Selected Action - followed by clamp to range 0-1
			Button(classData.viewBox, 170 @ 20)
			.states_([
				["Run & put result into Curve 4", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				this.runParameterFX;
			});
			// spacer
			//classData.viewBox.decorator.shift(20, 0);
			// Checkbox: autoRunFX
			holdView = TXCheckBox(classData.viewBox, Rect(0, 0, 150, 20),
				"Auto-run with changes", TXColor.sysGuiCol2, TXColor.white,
				TXColor.white, TXColor.sysGuiCol2);
			holdView.value = classData.autoRunFX;
			holdView.action = {|view|
				classData.autoRunFX = view.value;
				this.parameterFXCheckAutorun;
			};
			// next line
			classData.viewBox.decorator.nextLine.shift(0, 6);

			// Range: FXActiveRange - default 0-1, min-max 0-1 - which columns of curve are affected.
			activeRangeSlider = TXRangeSlider(classData.viewBox, 246 + classData.curveViewWidth @ 20,
				"FX Active Range", classData.defaultSpec,
				{arg view; classData.fxActiveRangeMin = view.lo; classData.fxActiveRangeMax = view.hi;},
				classData.fxActiveRangeMin, classData.fxActiveRangeMax, labelWidth: 110);
			activeRangeSlider.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			activeRangeSlider.rangeView.background_(TXColor.sysGuiCol1);
			activeRangeSlider.rangeView.mouseUpAction = {this.checkAutorun;};
			activeRangeSlider.minNumberView.action = {arg view;
				view.value = classData.defaultSpec.constrain(view.value);
				classData.fxActiveRangeMin = view.value;
				activeRangeSlider.rangeView.lo = view.value;
				this.checkAutorun;
			};
			activeRangeSlider.maxNumberView.action = {arg view;
				view.value = classData.defaultSpec.constrain(view.value);
				classData.fxActiveRangeMax = view.value;
				activeRangeSlider.rangeView.hi = view.value;
				this.checkAutorun;
			};
			// next line
			classData.viewBox.decorator.nextLine.shift(0, 6);

			// Slider: Parameter 1 (min-max 0-1)
			parm1Slider = TXSlider(classData.viewBox, 600 @ 20, "Parameter 1, value", classData.defaultSpec,
				{arg view; classData.parameter1 = view.value;}, classData.parameter1, labelWidth:120 );
			parm1Slider.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			parm1Slider.sliderView.background_(TXColor.sysGuiCol1);
			parm1Slider.sliderView.mouseUpAction = {this.parameterFXCheckAutorun;};
			parm1Slider.numberView.action = {arg view;
				view.value = classData.defaultSpec.constrain(view.value);
				classData.parameter1 = view.value;
				parm1Slider.sliderView.value = view.value;
				this.parameterFXCheckAutorun;
			};
			// next line
			classData.viewBox.decorator.nextLine.shift(0, 6);
			// Range: Parameter 2 (min-max 0-1)
			parm2RangeSlider = TXRangeSlider(classData.viewBox, 600 @ 20, "Parameter 2, range",classData.defaultSpec,
				{arg view; classData.parameter2Min = view.lo; classData.parameter2Max = view.hi;},
				classData.parameter2Min, classData.parameter2Max, labelWidth:120);
			parm2RangeSlider.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			parm2RangeSlider.rangeView.background_(TXColor.sysGuiCol1);
			parm2RangeSlider.rangeView.mouseUpAction = {this.parameterFXCheckAutorun;};
			parm2RangeSlider.minNumberView.action = {arg view;
				view.value = classData.defaultSpec.constrain(view.value);
				classData.parameter2Min = view.value;
				parm2RangeSlider.rangeView.lo = view.value;
				this.parameterFXCheckAutorun;};
			parm2RangeSlider.maxNumberView.action = {arg view;
				view.value = classData.defaultSpec.constrain(view.value);
				classData.parameter2Max = view.value;
				parm2RangeSlider.rangeView.hi = view.value;
				this.parameterFXCheckAutorun;};
		});

		if (classData.showCurve1And2FX == true, {
			//  Popup : "Curve 1 & 2 FX"
			holdView = TXPopupPlusMinus(classData.viewBox,600@20, "Curve 1 & 2 FX",
				this.getCurve1Curve2Actions.collect({arg item; item[0];}),
				{ arg view; classData.fxCurve1Curve2Index = view.value; this.curve2FXCheckAutorun;},
				classData.fxCurve1Curve2Index, labelWidth: 120);
			holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			// spacer
			classData.viewBox.decorator.shift(20, 0);
			//  Button: Run selected Curve 1 & 2 FX
			Button(classData.viewBox, 170 @ 20)
			.states_([
				["Run & put result into Curve 4", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				this.runCurve2FX;
			});
			// spacer
			// classData.viewBox.decorator.shift(20, 0);
			// Checkbox: autoRunFX
			holdView = TXCheckBox(classData.viewBox, Rect(0, 0, 150, 20),
				"Auto-run with changes", TXColor.sysGuiCol2, TXColor.white,
				TXColor.white, TXColor.sysGuiCol2);
			holdView.value = classData.autoRunFX;
			holdView.action = {|view|
				classData.autoRunFX = view.value;
				this.curve2FXCheckAutorun;
			};

			// next line
			classData.viewBox.decorator.nextLine.shift(0, 6);

			// Range: FXActiveRange - default 0-1, min-max 0-1 - which columns of curve are affected.
			activeRangeSlider = TXRangeSlider(classData.viewBox, 246 + classData.curveViewWidth @ 20,
				"FX Active Range", classData.defaultSpec,
				{arg view; classData.fxActiveRangeMin = view.lo; classData.fxActiveRangeMax = view.hi;},
				classData.fxActiveRangeMin, classData.fxActiveRangeMax, labelWidth: 110);
			activeRangeSlider.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			activeRangeSlider.rangeView.background_(TXColor.sysGuiCol1);
			activeRangeSlider.rangeView.mouseUpAction = {this.checkAutorun;};
			activeRangeSlider.minNumberView.action = {arg view;
				view.value = classData.defaultSpec.constrain(view.value);
				classData.fxActiveRangeMin = view.value;
				activeRangeSlider.rangeView.lo = view.value;
				this.checkAutorun;
			};
			activeRangeSlider.maxNumberView.action = {arg view;
				view.value = classData.defaultSpec.constrain(view.value);
				classData.fxActiveRangeMax = view.value;
				activeRangeSlider.rangeView.hi = view.value;
				this.checkAutorun;
			};
			// next line
			classData.viewBox.decorator.nextLine.shift(0, 6);

			// Curve: "Curve 2 - FX"
			classData.curve2View = TXCurveDraw(classData.viewBox, 750 @ classData.curveHeight, "Curve 2",
				{|view|
					this.curve2Changed(view.value);
					this.curve2FXCheckAutorun;
				},
				// get initial value
				classData.curve2,
				labelWidth: 120,
				initSlotVals: classData.arrSlotData,
				showPresets: "Curve",
				curveWidth: classData.curveViewWidth,
				curveHeight: classData.curveHeight,
				resetAction: "ramp",
				gridRowsFunc: {classData.gridRows},
				gridColsFunc: {classData.gridCols},
				xLabel: "x axis",
				yLabel: "y axis",
				showBuilderButton: false,
				dataEvent: classData.arrCurve2Parms
			);
			classData.curve2View.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			//	classData.curve2View.multiSliderView.isFilled_(true).fillColor_(TXColor.sysGuiCol1);
			//classData.curve2View.multiSliderView.strokeColor_(TXColor.sysGuiCol1);
			classData.curve2View.dragView.receiveDragHandler = {arg view;
				var newArray;
				var dragArray = View.currentDrag.value;
				newArray = this.resampleArray(dragArray, classData.curveSize);
				this.curve2Changed(newArray);
				this.updateCurve2View;
				this.curve2FXCheckAutorun;
				view.focus(false);
			};

			// spacer
			classData.viewBox.decorator.shift(20, 0);
			// Button:
			Button(classData.viewBox, 110 @ 20)
			.states_([
				["Quantize to Grid", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				// run action function
				this.quantizeCurve2(quantizeRows: true, quantizeCols: true);
				this.curve2FXCheckAutorun;
			});
			// Button
			Button(classData.viewBox, 110 @ 20)
			.states_([
				["Quantize Rows", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				// run action function
				this.quantizeCurve2(quantizeRows: true, quantizeCols: false);
				this.curve2FXCheckAutorun;
			});
			// Button
			Button(classData.viewBox, 110 @ 20)
			.states_([
				["Quantize Columns", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				// run action function
				this.quantizeCurve2(quantizeRows: false, quantizeCols: true);
				this.curve2FXCheckAutorun;
			});

		});

		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);

		if (classData.showFXMixCurve, {
			// Curve: "Curve 3 - FX Mix"
			classData.curve3View = TXCurveDraw(classData.viewBox, 750 @ classData.curveHeight,
				"Curve 3 - FX Mix",
				{|view|
					this.curve3Changed(view.value);
					this.checkAutorun;
				},
				// get initial value
				classData.curve3,
				labelWidth: 120,
				initSlotVals: classData.arrSlotData,
				showPresets: "Curve",
				curveWidth: classData.curveViewWidth,
				curveHeight: 230,
				resetAction: "ramp",
				gridRowsFunc: {classData.gridRows},
				gridColsFunc: {classData.gridCols},
				xLabel: "x axis",
				yLabel: "y axis",
				showBuilderButton: false,
				dataEvent: classData.arrCurve3Parms
			);
			classData.curve3View.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			//	classData.curve3View.multiSliderView.isFilled_(true).fillColor_(TXColor.sysGuiCol1);
			//classData.curve3View.multiSliderView.strokeColor_(TXColor.sysGuiCol1);
			classData.curve3View.dragView.receiveDragHandler = {arg view;
				var newArray;
				var dragArray = View.currentDrag.value;
				newArray = this.resampleArray(dragArray, classData.curveSize);
				this.curve3Changed(newArray);
				this.updateCurve3View;
				view.focus(false);
				this.checkAutorun;
			};

			// spacer
			classData.viewBox.decorator.shift(20, 0);
			// Button
			Button(classData.viewBox, 110 @ 20)
			.states_([
				["Quantize to Grid", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				// run action function
				this.quantizeCurve3(quantizeRows: true, quantizeCols: true);
			});
			// Button
			Button(classData.viewBox, 110 @ 20)
			.states_([
				["Quantize Rows", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				// run action function
				this.quantizeCurve3(quantizeRows: true, quantizeCols: false);
			});
			// Button
			Button(classData.viewBox, 110 @ 20)
			.states_([
				["Quantize Columns", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				// run action function
				this.quantizeCurve3(quantizeRows: false, quantizeCols: true);
			});

		}, {
			// Slider: FX Mix (min-max 0-1)
			fxMixSlider = TXSlider(classData.viewBox, 600 @ 20, "FX Mix", classData.defaultSpec,
				{arg view; classData.fxMix = view.value; }, classData.fxMix, labelWidth:120 );
			fxMixSlider.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			fxMixSlider.sliderView.background_(TXColor.sysGuiCol1);
			fxMixSlider.sliderView.mouseUpAction = {this.checkAutorun;};
			fxMixSlider.numberView.action = {arg view;
				view.value = classData.defaultSpec.constrain(view.value);
				classData.fxMix = view.value;
				fxMixSlider.sliderView.value = view.value;
				this.checkAutorun;};
		});

		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);

		// line
		StaticText(classData.viewBox, 1000 @ 2).background_(TXColor.white);

		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);

		// shift
		classData.viewBox.decorator.shift(124, 0);
		// Button: "Copy Curve 4 to Curve 1"
		Button(classData.viewBox, 160 @ 20)
		.states_([
			["Copy Curve 4 to Curve 1", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			this.curve1Changed(classData.curve4);
			this.updateCurve1View;
		});
		if (classData.showCurve1And2FX == true, {
			// shift
			classData.viewBox.decorator.shift(20, 0);
			// Button: "Copy Curve 3 to Curve 2"
			Button(classData.viewBox, 160 @ 20)
			.states_([
				["Copy Curve 4 to Curve 2", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				// run action function
				this.curve2Changed(classData.curve4);
				this.updateCurve2View;
			});
		});

		// next line
		classData.viewBox.decorator.nextLine;
		// Curve: "Curve 4 - FX Result"
		classData.curve4View = TXCurveDraw(classData.viewBox, 750 @ classData.curveHeight,
			"Curve 4 - FX Result",
			{|view|
				this.curve4Changed(view.value);
			},
			// get initial value
			classData.curve4,
			labelWidth: 120,
			initSlotVals: classData.arrSlotData,
			showPresets: "Curve",
			curveWidth: classData.curveViewWidth,
			curveHeight: classData.curveHeight,
			resetAction: "ramp",
			gridRowsFunc: {classData.gridRows},
			gridColsFunc: {classData.gridCols},
			xLabel: "x axis",
			yLabel: "y axis",
			showBuilderButton: false,
			dataEvent: classData.arrCurve4Parms
		);
		classData.curve4View.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		//	classData.curve4View.multiSliderView.isFilled_(true).fillColor_(TXColor.sysGuiCol1);
		//classData.curve4View.multiSliderView.strokeColor_(TXColor.sysGuiCol1);
		classData.curve4View.dragView.receiveDragHandler = {arg view;
			var newArray;
			var dragArray = View.currentDrag.value;
			newArray = this.resampleArray(dragArray, classData.curveSize);
			this.curve4Changed(newArray);
			this.updateCurve4View;
			view.focus(false);
		};

		// spacer
		classData.viewBox.decorator.shift(20, 0);
		// Button
		Button(classData.viewBox, 110 @ 20)
		.states_([
			["Quantize to Grid", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			this.quantizeCurve4(quantizeRows: true, quantizeCols: true);
		});
		// Button
		Button(classData.viewBox, 110 @ 20)
		.states_([
			["Quantize Rows", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			this.quantizeCurve4(quantizeRows: true, quantizeCols: false);
		});
		// Button
		Button(classData.viewBox, 110 @ 20)
		.states_([
			["Quantize Columns", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			this.quantizeCurve4(quantizeRows: false, quantizeCols: true);
		});

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


		curveBuilderWindow.front;

		// reset Default Font
		Font.default = holdFont;

	}

	// classData.parameter1 = 0;
	// classData.parameter2Min = 0;
	// classData.parameter2Max = 1;
	// classData.fxParm1Parm2Index = 0;
	// classData.fxCurve1Curve2Index

	*getParm1Parm2Actions {
		^[
			["select ...", {arg inputArray, mix; inputArray;}],
			["randomise all values within range Parameter 2", {arg inputArray, mix;
				this.fxBlend(inputArray, inputArray.size.collect({arg i;
					rrand(classData.parameter2Min, classData.parameter2Max);}), mix);
			}],
			["clamp all values to Parameter 2 range", {arg inputArray, mix;
				this.fxBlend(inputArray, inputArray.max(classData.parameter2Min).min(classData.parameter2Max), mix);
			}],
			["scale all values to Parameter 2 range", {arg inputArray, mix;
				this.fxBlend(inputArray,
					classData.parameter2Min + (inputArray * (classData.parameter2Max-classData.parameter2Min)), mix);
			}],
			["phase shift to the right by Parameter 1", {arg inputArray, mix;
				this.fxBlend(inputArray, inputArray.collect({arg item, i;
					inputArray.wrapAt(i + ((inputArray.size - 1) * classData.parameter1).asInteger)}), mix);
			}],
			["phase shift to the left by Parameter 1", {arg inputArray, mix;
				this.fxBlend(inputArray, inputArray.collect({arg item, i;
					inputArray.wrapAt(i - ((inputArray.size - 1) * classData.parameter1).asInteger)}), mix);
			}],
			["multiply all values by Parameter 1", {arg inputArray, mix;
				this.fxBlend(inputArray, (inputArray * classData.parameter1).max(0).min(1), mix);
			}],
			["divide all values by Parameter 1", {arg inputArray, mix;
				this.fxBlend(inputArray, (inputArray / max(classData.parameter1, 0.0001) ).max(0).min(1), mix);
			}],
			["add Parameter 1 to all values", {arg inputArray, mix;
				this.fxBlend(inputArray, (inputArray + classData.parameter1).max(0).min(1), mix);
			}],
			["subtract Parameter 1 from all values", {arg inputArray, mix;
				this.fxBlend(inputArray, (inputArray - classData.parameter1).max(0).min(1), mix);
			}],
		];
	}

	*getCurve1Curve2Actions {
		^[
			["select ...", {arg inputArray, mix; inputArray;}],
			["mix curves 1 -> 2", {arg inputArray1, inputArray2, mix;
				this.fxBlend(inputArray1, inputArray2, mix);
			}],
			["multiply curves 1 * 2", {arg inputArray1, inputArray2, mix;
				this.fxBlend(inputArray1, (inputArray1 * inputArray2).max(0).min(1), mix);
			}],
			["divide curves 1 / 2", {arg inputArray1, inputArray2, mix;
				this.fxBlend(inputArray1, (inputArray1 / inputArray2).max(0).min(1), mix);
			}],
			["add curves 1 + 2", {arg inputArray1, inputArray2, mix;
				this.fxBlend(inputArray1, (inputArray1 + inputArray2).max(0).min(1), mix);
			}],
			["subtract curves 1 - 2", {arg inputArray1, inputArray2, mix;
				this.fxBlend(inputArray1, (inputArray1 - inputArray2).max(0).min(1), mix);
			}],
			["use curve1 as index into curve2", {arg inputArray1, inputArray2, mix;
				var newArray = inputArray1.collect({arg item, i;
					var holdIndex = (item * (inputArray2.size - 1)).round(1);
					inputArray2[holdIndex];
				});
				this.fxBlend(inputArray1, newArray, mix);
			}],
			["use curve2 as index into curve1", {arg inputArray1, inputArray2, mix;
				var newArray = inputArray2.collect({arg item, i;
					var holdIndex = (item * (inputArray1.size - 1)).round(1);
					inputArray1[holdIndex];
				});
				this.fxBlend(inputArray1, newArray, mix);
			}],
		];
	}

	*fxBlend {arg input1, input2, mix;
		// only use active fx range
		var minIndex, maxIndex, newArray2, outArray;
		input1 = input1.as(FloatArray);
		input2 = input2.as(FloatArray);
		minIndex = (classData.fxActiveRangeMin * (input1.size - 1)).round(1);
		maxIndex = (classData.fxActiveRangeMax * (input1.size - 1)).round(1);
		newArray2 = input2.collect({arg item, i;
			var outVal;
			if (i < minIndex or: (i > maxIndex), {
				outVal = input1[i];
			}, {
				outVal = item;
			});
			outVal;
		});
		if (mix.asArray.size > 1, {
			outArray = input1.collect({arg item, i;
				blend(item, newArray2[i], mix[i]);
			});
		}, {
			outArray = blend(input1, newArray2, mix);
		});
		^outArray;
	}

	*runParameterFX {
		var function, newArray, holdMix;
		if (classData.fxParm1Parm2Index > 0, {
			function = this.getParm1Parm2Actions[classData.fxParm1Parm2Index][1];
			if (classData.showFXMixCurve, {
				holdMix = classData.curve3;
			}, {
				holdMix = classData.fxMix;
			});
			newArray = function.value(classData.curve1, holdMix);
			this.curve4Changed(newArray);
			this.updateCurve4View;
		});
	}

	*runCurve2FX {
		var function, newArray, holdMix;
		if (classData.fxCurve1Curve2Index > 0, {
			function = this.getCurve1Curve2Actions[classData.fxCurve1Curve2Index][1];
			if (classData.showFXMixCurve, {
				holdMix = classData.curve3;
			}, {
				holdMix = classData.fxMix;
			});
			newArray = function.value(classData.curve1, classData.curve2, holdMix);
			this.curve4Changed(newArray);
			this.updateCurve4View;
		});
	}

	*parameterFXCheckAutorun{
		if (classData.autoRunFX == 1, {
			this.runParameterFX;
		});
	}
	*curve2FXCheckAutorun{
		if (classData.autoRunFX == 1, {
			this.runCurve2FX;
		});
	}
	*checkAutorun{
		if (classData.showCurve1ParameterFX == true, {
			this.parameterFXCheckAutorun;
		});
		if (classData.showCurve1And2FX == true, {
			this.curve2FXCheckAutorun;
		});
	}

}

