// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
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
		classData.maxHistorySize = 100;
		classData.arrSlotData = Array.newClear(classData.curveSize).seriesFill(0, 1 / (classData.curveSize-1)) ! 5;
		classData.curve1 = Array.newClear(classData.curveSize).seriesFill(0, 1 / (classData.curveSize-1));
		classData.curve2 = Array.newClear(classData.curveSize).seriesFill(0, 1 / (classData.curveSize-1));
		classData.curve3 = Array.newClear(classData.curveSize).seriesFill(0, 1 / (classData.curveSize-1));
		classData.curve1History = [classData.curve1];
		classData.curve2History = [classData.curve2];
		classData.curve3History = [classData.curve3];
		classData.curve1HistIndex = 0;
		classData.curve2HistIndex = 0;
		classData.curve3HistIndex = 0;
		classData.autoResizeCurve1 = true;
		classData.showCurve1ParameterFX = true;
		classData.showCurve1And2FX = false;
		classData.showEnvelopeControls = false;
		classData.fxActiveRangeMin = 0;
		classData.fxActiveRangeMax = 1;
		classData.parameter1 = 0;
		classData.parameter2Min = 0;
		classData.parameter2Max = 1;
		classData.fxParm1Parm2Index = 0;
		classData.fxParm1Parm2Mix = 1;
		classData.fxCurve1Curve2Index = 0;
		classData.fxCurve1Curve2Mix = 1;
		classData.windowVisibleOrigin = Point.new(0,0);
		classData.envelope = Env.adsr(attackTime: 0.1, decayTime: 0.2, sustainLevel: 0.5, releaseTime: 0.6);
		this.clearViews;
	}

	*closeWindow {
		if (curveBuilderWindow.notNil, {
			curveBuilderWindow.close;
		});
		this.clearViews;
	}

	*clearViews {
		classData.viewBox = nil;
		classData.gridRowsView = nil;
		classData.gridColsView = nil;
		classData.curveHeightView = nil;
		classData.curveView1 = nil;
		classData.curveView2 = nil;
		classData.curveView3 = nil;
	}

	*rebuildWindow {
		this.closeWindow;
		{this.showWindow;}.defer(0.1);
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
		classData.curveView1.setGridRowsCols(classData.gridRows, classData.gridCols);
		classData.curveView2.setGridRowsCols(classData.gridRows, classData.gridCols);
		classData.curveView3.setGridRowsCols(classData.gridRows, classData.gridCols);
	}

	//======================

	*quantizeToGrid {arg inputArray;
		var holdArray, holdSignal, outArray;
		var maxVal = inputArray.size;
		holdArray = Array.newClear(maxVal);
		classData.gridCols.do({arg item, i;
			var jump, startRange, endRange, meanVal;
			jump = classData.gridCols.reciprocal;
			startRange = (item * jump * maxVal).round(1);
			endRange = ((item + 1) * jump * maxVal).round(1) - 1;
			meanVal = inputArray.copyRange(startRange.asInteger, endRange.asInteger).mean;
			holdArray[startRange.asInteger..endRange.asInteger] = meanVal.round(classData.gridRows.reciprocal);
		});
		holdSignal = Signal.newFrom(holdArray);
		outArray = Array.newFrom(holdSignal);
		^outArray;
	}
	*quantizeCurve1 {
		this.curve1Changed(this.quantizeToGrid(classData.curve1));
		this.updateCurveView1;
	}
	*quantizeCurve2 {
		this.curve2Changed(this.quantizeToGrid(classData.curve2));
		this.updateCurveView2;
	}
	*quantizeCurve3 {
		this.curve3Changed(this.quantizeToGrid(classData.curve3));
		this.updateCurveView3;
	}

	//======================

	*updateCurveView1 {
		classData.curveView1.value = classData.curve1;
	}
	*updateCurveView2 {
		if (classData.curveView2.notNil, {classData.curveView2.value = classData.curve2;});
	}
	*updateCurveView3 {
		classData.curveView3.value = classData.curve3;
	}

	//======================

	// store curve vals & history
	*curve1Changed {arg newArray;
		classData.curve1 = newArray;
		// truncate at current index
		classData.curve1History = classData.curve1History.keep(classData.curve1HistIndex + 1);
		// add to history
		classData.curve1History = classData.curve1History.add(newArray);
		classData.curve1History = classData.curve1History.keep(0-(classData.maxHistorySize));
		classData.curve1HistIndex = classData.curve1History.size - 1;
	}
	*curve2Changed {arg newArray;
		classData.curve2 = newArray;
		// truncate at current index
		classData.curve2History = classData.curve2History.keep(classData.curve2HistIndex + 1);
		// add to history
		classData.curve2History = classData.curve2History.add(newArray);
		classData.curve2History = classData.curve2History.keep(0-(classData.maxHistorySize));
		classData.curve2HistIndex = classData.curve2History.size - 1;
	}
	*curve3Changed {arg newArray;
		classData.curve3 = newArray;
		// truncate at current index
		classData.curve3History = classData.curve3History.keep(classData.curve3HistIndex + 1);
		// add to history
		classData.curve3History = classData.curve3History.add(newArray);
		classData.curve3History = classData.curve3History.keep(0-(classData.maxHistorySize));
		classData.curve3HistIndex = classData.curve3History.size - 1;
	}

	//======================

	*loadCurve1HistoryPrev {
		if (classData.curve1HistIndex > 0, {
			classData.curve1HistIndex = classData.curve1HistIndex - 1;
			classData.curve1 = this.resampleArray(classData.curve1History[classData.curve1HistIndex], classData.curveSize);
			this.updateCurveView1;
		});
	}
	*loadCurve1HistoryNext {
		if (classData.curve1HistIndex < (classData.curve1History.size - 1), {
			classData.curve1HistIndex = classData.curve1HistIndex + 1;
			classData.curve1 = this.resampleArray(classData.curve1History[classData.curve1HistIndex], classData.curveSize);
			this.updateCurveView1;
		});
	}
	*loadCurve2HistoryPrev {
		if (classData.curve2HistIndex > 0, {
			classData.curve2HistIndex = classData.curve2HistIndex - 1;
			classData.curve2 = this.resampleArray(classData.curve2History[classData.curve2HistIndex], classData.curveSize);
			this.updateCurveView2;
		});
	}
	*loadCurve2HistoryNext {
		if (classData.curve2HistIndex < (classData.curve2History.size - 1), {
			classData.curve2HistIndex = classData.curve2HistIndex + 1;
			classData.curve2 = this.resampleArray(classData.curve2History[classData.curve2HistIndex], classData.curveSize);
			this.updateCurveView2;
		});
	}
	*loadCurve3HistoryPrev {
		if (classData.curve3HistIndex > 0, {
			classData.curve3HistIndex = classData.curve3HistIndex - 1;
			classData.curve3 = this.resampleArray(classData.curve3History[classData.curve3HistIndex], classData.curveSize);
			this.updateCurveView3;
		});
	}
	*loadCurve3HistoryNext {
		if (classData.curve3HistIndex < (classData.curve3History.size - 1), {
			classData.curve3HistIndex = classData.curve3HistIndex + 1;
			classData.curve3 = this.resampleArray(classData.curve3History[classData.curve3HistIndex], classData.curveSize);
			this.updateCurveView3;
		});
	}

	//======================

	*showWindow {
		var screenWidth = 1000;
		var screenHeight = 1000;
		var scrollView, holdView;
		var curve1Width, curve2Width, curve3Width;
		var arrGridPresetNames, arrGridPresetActions;
		var holdFont;

		// Set Default Font
		holdFont = Font.default;
		Font.default =  Font("Helvetica", 12);

		// create window if needed
		if (curveBuilderWindow.isNil, {
			curveBuilderWindow = Window.new(
				"Curve Builder - drag & drop curves",
				Rect(400, 400, screenWidth, screenHeight),
				resizable: true, scroll: true
			).front;
			curveBuilderWindow.view.action = {arg view; classData.windowVisibleOrigin = view.visibleOrigin;};
			curveBuilderWindow.view.mouseWheelAction = {arg view; classData.windowVisibleOrigin = view.visibleOrigin;};
		});
		// defer visible origin
		{curveBuilderWindow.view.visibleOrigin = classData.windowVisibleOrigin;}.defer(0.1);

		//curveBuilderWindow.view.hasHorizontalScroller = false;
		curveBuilderWindow.view.background_(TXColour.sysMainWindow);
		curveBuilderWindow.onClose_({this.curveBuilderWindow = nil;});
		// NOTE: SC Bug - if alwaysOnTop_ then dragging doesn't work
		//curveBuilderWindow.alwaysOnTop_(true);

		// // add ScrollView
		// scrollView = ScrollView(curveBuilderWindow, Rect(0, 0, screenWidth-15, screenHeight-10))
		// .hasBorder_(false);
		// scrollView.action = {classData.visibleOrigin = scrollView.visibleOrigin};
		// scrollView.hasHorizontalScroller = false;
		// scrollView.hasVerticalScroller = true;

		if (classData.viewBox.notNil, {
			TXSystem1.deferRemoveView(classData.viewBox);
		});

		// make box to display
		classData.viewBox = CompositeView(curveBuilderWindow, Rect(4, 4, 1400, 2100))
		.background_(TXColor.clear);
		classData.viewBox.decorator = FlowLayout(classData.viewBox.bounds);

		// set visibleOrigin
		if (classData.visibleOrigin.notNil, {scrollView.view.visibleOrigin = classData.visibleOrigin; });

		// CheckBox: "Show Envelope"
		holdView = TXCheckBox.new(classData.viewBox, 200 @ 20, "Show Envelope",
			TXColor.sysGuiCol1, TXColour.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
		holdView.value = classData.showEnvelopeControls;
		holdView.action = {arg view;
			classData.showEnvelopeControls = view.value.asBoolean;
			this.showWindow;
		};

		// CheckBox: "Show Curve 1 Parameter FX"
		holdView = TXCheckBox.new(classData.viewBox, 200 @ 20, "Show Curve 1 Parameter FX",
			TXColor.sysGuiCol2, TXColour.grey(0.8), TXColor.white, TXColor.sysGuiCol2);
		holdView.value = classData.showCurve1ParameterFX;
		holdView.action = {arg view;
			classData.showCurve1And2FX = false;
			classData.showCurve1ParameterFX = view.value.asBoolean;
			this.showWindow;
		};

		// CheckBox: "Show Curve 1 & 2"
		holdView = TXCheckBox.new(classData.viewBox, 200 @ 20, "Show Curve 1 & 2 FX",
			TXColor.sysGuiCol2, TXColour.grey(0.8), TXColor.white, TXColor.sysGuiCol2);
		holdView.value = classData.showCurve1And2FX;
		holdView.action = {arg view;
			classData.showCurve1ParameterFX = false;
			classData.showCurve1And2FX = view.value.asBoolean;
			this.showWindow;
		};

		arrGridPresetNames = ["Select preset...", "1 x 1", "2 x 2", "3 x 3", "4 x 4", "5 x 5", "6 x 6", "8 x 8", "9 x 9",
			"10 x 10", "12 x 12", "16 x 16", "24 x 24", "32 x 32"];
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
			{this.setGridRowsCols(24);},
			{this.setGridRowsCols(32);},
		];

		// spacer & go to next line
		classData.viewBox.decorator.shift(0, 4).nextLine;
		// NumberBox: "Curve Size" (will resize all curves)
		holdView = TXNumber(classData.viewBox, 60 @ 20, "Curve size", ControlSpec.new(32, 1024, step:1),
			{arg view; this.setCurveSize(view.value);}, classData.curveSize);
		holdView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		// StaticText:  "Note: All curves will be resized to be the same as curve 1"
		holdView = StaticText(classData.viewBox, 400 @ 20);
		holdView.string = "Note: All curves will be resized to be the same as curve 1";
		holdView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		holdView.setProperty(\align,\left);
		// next line
		classData.viewBox.decorator.nextLine;

		classData.gridRowsView = TXNumber(classData.viewBox, 60 @ 20, "Grid Rows", ControlSpec.new(1, 64, step:1),
			{arg view; this.setGridRows(view.value);}, classData.gridRows);
		classData.gridRowsView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		classData.gridColsView = TXNumber(classData.viewBox, 60 @ 20, "Grid Columns", ControlSpec.new(1, 64, step:1),
			{arg view; this.setGridCols(view.value);}, classData.gridCols);
		classData.gridColsView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		//arrGridPresetNames, arrGridPresetActions
		holdView = TXPopup(classData.viewBox,220@20, "Grid Presets", arrGridPresetNames,
			{ arg view; arrGridPresetActions[view.value].value; },0, labelWidth: 80);
		holdView.labelView.stringColor_(TXColour.black).background_(TXColor.white);
		holdView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		// next line
		classData.viewBox.decorator.nextLine;
		classData.curveHeightView = TXNumber(classData.viewBox, 60 @ 20, "Curve Height", ControlSpec.new(230, 700, step:1),
			{arg view; this.setCurveHeight(view.value);}, classData.curveHeight);
		classData.curveHeightView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		// Buttons
		Button(classData.viewBox, 80 @ 20)
		.states_([
			["230", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			this.setCurveHeight(230);
		});
		Button(classData.viewBox, 80 @ 20)
		.states_([
			["300", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			this.setCurveHeight(300);
		});
		Button(classData.viewBox, 80 @ 20)
		.states_([
			["400", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			this.setCurveHeight(400);
		});
		Button(classData.viewBox, 80 @ 20)
		.states_([
			["500", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			this.setCurveHeight(500);
		});

		if (classData.showEnvelopeControls == true, {

			// next line
			classData.viewBox.decorator.nextLine.shift(0, 6);

			// line
			StaticText(classData.viewBox, 1000 @ 2).background_(TXColor.white);

			// next line
			classData.viewBox.decorator.nextLine.shift(0, 6);

			holdView = StaticText(classData.viewBox, 120 @ 20);
			holdView.string = "Envelope";
			holdView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
			holdView.align = \right;

			// Env view
			classData.envelope = classData.envelope.duration_(0.8);
			classData.envView = TXEnvScaleView(classData.viewBox, (classData.curveSize + 50) @ (classData.curveHeight + 40), classData.envelope);
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
				classData.envView.maxRange_(1.0, true);
			}.defer(0.2);

			// next line
			classData.viewBox.decorator.nextLine.shift(124, 0);

			holdView = StaticText(classData.viewBox, 200 @ 20);
			holdView.string = "Ctl & Click to add/remove nodes";
			holdView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
			holdView.align = \center;

			//classData.viewBox.decorator.shift(20, 0);

			//    Button: Copy Envelope into Curve 1
			Button(classData.viewBox, 180 @ 20)
			.states_([
				["Copy Envelope into Curve 1", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				var newArray = classData.envelope.discretize(classData.curve1.size);
				this.curve1Changed(newArray);
				this.updateCurveView1;
			});
			//    Button: Copy Envelope into Curve 2
			Button(classData.viewBox, 180 @ 20)
			.states_([
				["Copy Envelope into Curve 2", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				var newArray = classData.envelope.discretize(classData.curve2.size);
				this.curve2Changed(newArray);
				this.updateCurveView2;
			});
			//    Button: Copy Envelope into Curve 3
			Button(classData.viewBox, 180 @ 20)
			.states_([
				["Copy Envelope into Curve 3", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				var newArray = classData.envelope.discretize(classData.curve3.size);
				this.curve3Changed(newArray);
				this.updateCurveView3;
			});

		}); // end of if (classData.showEnvelopeControls == true

		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);

		// line
		StaticText(classData.viewBox, 1000 @ 2).background_(TXColor.white);

		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);

		// Text & Buttons: < > - Curve 1 history backwards & forwards
		holdView = StaticText(classData.viewBox, 120 @ 20);
		holdView.string = "Curve 1 History";
		holdView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		holdView.align = \right;
		Button(classData.viewBox, 20 @ 20)
		.states_([
			["<", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			this.loadCurve1HistoryPrev;
		});
		Button(classData.viewBox, 20 @ 20)
		.states_([
			[">", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			this.loadCurve1HistoryNext;
		});
		// spacer
		classData.viewBox.decorator.shift(20, 0);
		// CheckBox: "Auto-resize Curve 1 when receiving a dragged curve" - Default ON.
		holdView = TXCheckBox.new(classData.viewBox, 400 @ 20, "Auto-resize Curve 1 when receiving a dragged curve",
			TXColor.sysGuiCol1, TXColour.grey(0.8), TXColor.white, TXColor.sysGuiCol1);
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
		classData.curveView1 = TXCurveDraw(classData.viewBox, 750 @ classData.curveHeight, "Curve 1",
			{|view|
				this.curve1Changed(view.value);
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
			showBuilderButton: false
		);
		classData.curveView1.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		//	classData.curveView1.multiSliderView.isFilled_(true).fillColor_(TXColor.sysGuiCol1);
		classData.curveView1.multiSliderView.strokeColor_(TXColour.sysGuiCol1);
		classData.curveView1.dragView.receiveDragHandler = {arg view;
			var newArray;
			var dragArray = View.currentDrag.value;
			if (classData.autoResizeCurve1, {
				// resize
				this.curve1Changed(dragArray);
				if (classData.curveSize != dragArray.size, {
					this.setCurveSize(dragArray.size);
					}, {
						this.updateCurveView1;
				});
				}, {
					newArray = this.resampleArray(dragArray, classData.curveSize);
					this.curve1Changed(newArray);
					this.updateCurveView1;
			});
			view.focus(false);
		};

		// spacer
		classData.viewBox.decorator.shift(20, 0);
		// Button
		Button(classData.viewBox, 140 @ 20)
		.states_([
			["Quantize to Grid", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			this.quantizeCurve1;
		});

		// next line
		classData.viewBox.decorator.nextLine.shift(0, 12);
		//    Range: FXActiveRange - default 0-1, min-max 0-1 - which columns of curve are affected.
		holdView = TXRangeSlider(classData.viewBox, 256 + classData.curveSize @ 20, "FX Active Range", [0,1].asSpec, {arg view;
			classData.fxActiveRangeMin = view.lo; classData.fxActiveRangeMax = view.hi;
		}, classData.fxActiveRangeMin, classData.fxActiveRangeMax, labelWidth: 120);
		holdView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);

		if (classData.showCurve1ParameterFX == true, {

			// next line
			classData.viewBox.decorator.nextLine.shift(0, 6);

			// line
			StaticText(classData.viewBox, 1000 @ 2).background_(TXColor.white);

			// next line
			classData.viewBox.decorator.nextLine.shift(0, 6);

			// Popup: "Parameter FX"
			holdView = TXPopupPlusMinus(classData.viewBox,600@20, "Parameter FX",
				this.getArrayParm1Parm2Actions.collect({arg item; item[0];}),
				{ arg view; classData.fxParm1Parm2Index = view.value; },0, labelWidth: 120);
			holdView.labelView.stringColor_(TXColour.black).background_(TXColor.white);
			holdView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
			// spacer
			classData.viewBox.decorator.shift(20, 0);
			// Button: Run Selected Action - followed by clamp to range 0-1
			Button(classData.viewBox, 310 @ 20)
			.states_([
				["Run selected FX on Curve 1 and put result into Curve 3", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				// run action function
				var function = this.getArrayParm1Parm2Actions[classData.fxParm1Parm2Index][1];
				var newArray = function.value(classData.curve1, classData.fxParm1Parm2Mix);
				this.curve3Changed(newArray);
				this.updateCurveView3;
			});
			// next line
			classData.viewBox.decorator.nextLine;
			// Slider: Parameter 1  (min-max 0-1)
			holdView = TXSlider(classData.viewBox, 600 @ 20, "Parameter 1", [0,1].asSpec,
				{arg view; classData.parameter1 = view.value;}, classData.parameter1, labelWidth:120 );
			holdView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
			// next line
			classData.viewBox.decorator.nextLine;
			// Range: Parameter 2 (min-max 0-1)
			holdView = TXRangeSlider(classData.viewBox, 600 @ 20, "Parameter 2",[0,1].asSpec,
				{arg view; classData.parameter2Min = view.lo; classData.parameter2Max = view.hi;},
				classData.parameter2Min, classData.parameter2Max, labelWidth:120);
			holdView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
			// next line
			classData.viewBox.decorator.nextLine;
			// Slider: FX Mix  (min-max 0-1)
			holdView = TXSlider(classData.viewBox, 600 @ 20, "FX Mix", [0,1].asSpec,
				{arg view; classData.fxParm1Parm2Mix = view.value;}, classData.fxParm1Parm2Mix, labelWidth:120 );
			holdView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		});

		if (classData.showCurve1And2FX == true, {

			// next line
			classData.viewBox.decorator.nextLine.shift(0, 6);

			// line
			StaticText(classData.viewBox, 1000 @ 2).background_(TXColor.white);

			// next line
			classData.viewBox.decorator.nextLine.shift(0, 6);

			//    Text & Buttons: < > - Curve 2 history backwards & forwards
			holdView = StaticText(classData.viewBox, 120 @ 20);
			holdView.string = "Curve 2 History";
			holdView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
			holdView.align = \right;
			Button(classData.viewBox, 20 @ 20)
			.states_([
				["<", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				this.loadCurve2HistoryPrev;
			});
			Button(classData.viewBox, 20 @ 20)
			.states_([
				[">", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				this.loadCurve2HistoryNext;
			});

			// next line
			classData.viewBox.decorator.nextLine;
			// Curve: "Curve 2 - FX"
			classData.curveView2 = TXCurveDraw(classData.viewBox, 750 @ classData.curveHeight, "Curve 2",
				{|view|
					this.curve2Changed(view.value);
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
				showBuilderButton: false
			);
			classData.curveView2.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
			//	classData.curveView2.multiSliderView.isFilled_(true).fillColor_(TXColor.sysGuiCol1);
			classData.curveView2.multiSliderView.strokeColor_(TXColour.sysGuiCol1);
			classData.curveView2.dragView.receiveDragHandler = {arg view;
				var newArray;
				var dragArray = View.currentDrag.value;
				newArray = this.resampleArray(dragArray, classData.curveSize);
				this.curve2Changed(newArray);
				this.updateCurveView2;
				view.focus(false);
			};

			// spacer
			classData.viewBox.decorator.shift(20, 0);
			// Button:
			Button(classData.viewBox, 140 @ 20)
			.states_([
				["Quantize to Grid", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				// run action function
				this.quantizeCurve2;
			});

			// next line
			classData.viewBox.decorator.nextLine.shift(0, 12);

			//    Popup : "Curve 1 & 2 FX"
			holdView = TXPopupPlusMinus(classData.viewBox,600@20, "Curve 1 & 2 FX", this.getArrayCurve1Curve2Actions.collect({arg item; item[0];}),
				{ arg view; classData.fxCurve1Curve2Index = view.value; },0, labelWidth: 120);
			holdView.labelView.stringColor_(TXColour.black).background_(TXColor.white);
			// spacer
			classData.viewBox.decorator.shift(20, 0);
			//    Button: Run selected Curve 1 & 2 FX
			Button(classData.viewBox, 320 @ 20)
			.states_([
				["Run selected Curve 1 & 2 FX and put result into Curve 3", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				// run action function
				var function = this.getArrayCurve1Curve2Actions[classData.fxCurve1Curve2Index][1];
				var newArray = function.value(classData.curve1, classData.curve2, classData.fxCurve1Curve2Mix);
				this.curve3Changed(newArray);
				this.updateCurveView3;
			});

			// next line
			classData.viewBox.decorator.nextLine;
			// Slider: FX Mix  (min-max 0-1)
			holdView = TXSlider(classData.viewBox, 600 @ 20, "FX Mix", [0,1].asSpec,
				{arg view; classData.fxCurve1Curve2Mix = view.value;}, classData.fxCurve1Curve2Mix, labelWidth:120 );
			holdView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		});


			// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);

		// line
		StaticText(classData.viewBox, 1000 @ 2).background_(TXColor.white);

		// next line
		classData.viewBox.decorator.nextLine.shift(0, 6);

		// Text & Buttons: < > - Curve 3 history backwards & forwards
		holdView = StaticText(classData.viewBox, 120 @ 20);
		holdView.string = "Curve 3 History";
		holdView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		holdView.align = \right;
		Button(classData.viewBox, 20 @ 20)
		.states_([
			["<", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			this.loadCurve3HistoryPrev;
		});
		Button(classData.viewBox, 20 @ 20)
		.states_([
			[">", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			this.loadCurve3HistoryNext;
		});

		// shift
		classData.viewBox.decorator.shift(20, 0);
		// Button: "Copy Curve 3 to Curve 1"
		Button(classData.viewBox, 200 @ 20)
		.states_([
			["Copy Curve 3 to Curve 1", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			this.curve1Changed(classData.curve3);
			this.updateCurveView1;
		});
		// Button: "Copy Curve 3 to Curve 2"
		Button(classData.viewBox, 200 @ 20)
		.states_([
			["Copy Curve 3 to Curve 2", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			this.curve2Changed(classData.curve3);
			this.updateCurveView2;
		});

		// next line
		classData.viewBox.decorator.nextLine;
		// Curve: "Curve 3 - FX Result"- read only - can only be dragged from
		classData.curveView3 = TXCurveDraw(classData.viewBox, 750 @ classData.curveHeight, "Curve 3 - FX Result",
			{|view|
				this.curve3Changed(view.value);
			},
			// get initial value
			classData.curve3,
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
			showBuilderButton: false
		);
		classData.curveView3.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		//	classData.curveView3.multiSliderView.isFilled_(true).fillColor_(TXColor.sysGuiCol1);
		classData.curveView3.multiSliderView.strokeColor_(TXColour.sysGuiCol1);
		classData.curveView3.dragView.receiveDragHandler = {arg view;
			var newArray;
			var dragArray = View.currentDrag.value;
			newArray = this.resampleArray(dragArray, classData.curveSize);
			this.curve3Changed(newArray);
			this.updateCurveView3;
			view.focus(false);
		};

		// spacer
		classData.viewBox.decorator.shift(20, 0);
		// Button
		Button(classData.viewBox, 140 @ 20)
		.states_([
			["Quantize to Grid", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			this.quantizeCurve3;
		});

		// next line
		classData.viewBox.decorator.nextLine;
		// CurveStore1-20 - small views (read only - can be dragged to & from)

		curveBuilderWindow.front;

		// reset Default Font
		Font.default = holdFont;

	}

	// classData.parameter1 = 0;
	// classData.parameter2Min = 0;
	// classData.parameter2Max = 1;
	// classData.fxParm1Parm2Index = 0;
	// classData.fxCurve1Curve2Index

	*getArrayParm1Parm2Actions {
		^[
			["select ...", {arg inputArray, mix; inputArray;}],
			["randomise all values within range Parameter 2", {arg inputArray, mix;
				this.fxBlend(inputArray, inputArray.size.collect({arg i; rrand(classData.parameter2Min, classData.parameter2Max);}), mix);
			}],
			["clamp all values to Parameter 2 range", {arg inputArray, mix;
				this.fxBlend(inputArray, inputArray.max(classData.parameter2Min).min(classData.parameter2Max), mix);
			}],
			["scale all values to Parameter 2 range", {arg inputArray, mix;
				this.fxBlend(inputArray, classData.parameter2Min + (inputArray * (classData.parameter2Max-classData.parameter2Min)), mix);
			}],
			["phase shift to the right by Parameter 1", {arg inputArray, mix;
				this.fxBlend(inputArray, inputArray.collect({arg item, i; inputArray.wrapAt(i + ((inputArray.size - 1) * classData.parameter1).asInteger)}), mix);
			}],
			["phase shift to the left by Parameter 1", {arg inputArray, mix;
				this.fxBlend(inputArray, inputArray.collect({arg item, i; inputArray.wrapAt(i - ((inputArray.size - 1) * classData.parameter1).asInteger)}), mix);
			}],
			["multiply all values by Parameter 1", {arg inputArray, mix;
				this.fxBlend(inputArray, (inputArray * classData.parameter1).max(0).min(1), mix);
			}],
			["divide all values by Parameter 1", {arg inputArray, mix;
				this.fxBlend(inputArray, (inputArray / max(classData.parameter1, 0.0001) ).max(0).min(1), mix);
			}],
			["add Parameter 1  to all values", {arg inputArray, mix;
				this.fxBlend(inputArray, (inputArray + classData.parameter1).max(0).min(1), mix);
			}],
			["subtract Parameter 1 from all values", {arg inputArray, mix;
				this.fxBlend(inputArray, (inputArray - classData.parameter1).max(0).min(1), mix);
			}],
		];
	}

	*getArrayCurve1Curve2Actions {
		^[
			["select ...", {arg inputArray1, inputArray2, mix;
				this.fxBlend(inputArray1, inputArray2, mix);
			}],
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
			["use curve2 as index into curve1", {arg inputArray1, inputArray2, mix;
				var newArray2 = inputArray2.collect({arg item, i;
					var holdIndex = (item * (inputArray1.size - 1)).round(1);
					inputArray1[holdIndex];
				});
				this.fxBlend(inputArray1, newArray2, mix);
			}],
		];
	}

	*fxBlend {arg input1, input2, mix;
		// only use  active fx range
		var minIndex, maxIndex, newArray2, outArray;
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
		outArray = blend(input1, newArray2, mix);
		^outArray;
	}
}
