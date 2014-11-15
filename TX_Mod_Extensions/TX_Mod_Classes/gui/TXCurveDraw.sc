// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXCurveDraw {	// MultiSlider, popup and buttons for curve drawing with 5 user slots for saving curves
	var <>labelView, <>dragView, <>userView, <>multiSliderView, <>popupView, <>popupView2, <>action, <value, <arrSlotData;
	var linearArray, resetArray, tableSize, gridRows, gridCols;

	*new { arg window, dimensions, label, action, initVal, initAction=false, labelWidth=80, initSlotVals,
			showPresets, curveWidth=270, curveHeight=257, resetAction="Ramp",
			gridRowsFunc, gridColsFunc, xLabel, yLabel, curveThumbSize=1, showBuilderButton = true;
		^super.new.init(window, dimensions, label, action, initVal, initAction, labelWidth, initSlotVals,
			showPresets, curveWidth, curveHeight, resetAction,
			gridRowsFunc, gridColsFunc, xLabel, yLabel, curveThumbSize, showBuilderButton);
	}
	init { arg window, dimensions, label, argAction, initVal, initAction, labelWidth, initSlotVals,
			showPresets, curveWidth, curveHeight, resetAction,
			gridRowsFunc, gridColsFunc, xLabel, yLabel, curveThumbSize, showBuilderButton;
		var arrGenFunctions, arrModifyFunctions, popItems, popAction, newArray, holdTop, holdLeft, userViewWidth, userViewHeight;

		initVal = initVal ? Array.newClear(128).seriesFill(0, 1/127);
		tableSize = initVal.size;
		linearArray = Array.newClear(tableSize).seriesFill(0, 1 / (tableSize - 1));
		resetArray = linearArray;
		if (resetAction == "Ramp", {
			resetArray = linearArray;
		});
		if (resetAction == "Zero", {
			resetArray = Array.newClear(tableSize).fill(0);
		});
		if (resetAction == "Max", {
			resetArray = Array.newClear(tableSize).fill(1);
		});
		if (resetAction == "Sine", {
			resetArray = Signal.sineFill(tableSize, [1.0],[1.5pi])
				.collect({arg item, i; (item.value + 1) * 0.5;});
		});
		initSlotVals = initSlotVals ? resetArray.dup(5);
		arrSlotData = initSlotVals;
		action = argAction;
		xLabel = (xLabel ? "Input").as(String);
		yLabel = (yLabel ? "Output").as(String);
		gridRows = gridRowsFunc.value ?? 2;
		gridCols = gridColsFunc.value ?? 2;
		userViewWidth = curveWidth-4;
		userViewHeight = curveHeight - 4;

		// label
		labelView = StaticText(window, labelWidth @ 20);
		labelView.string = label;
		labelView.align = \right;

		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(0-labelWidth-4, 40);
		}, {
			window.decorator.shift(0-labelWidth-4, 40);
		});

		// Drag box
		dragView = DragBoth(window, labelWidth @ 20);
		dragView.setBoth = false;
		dragView.string = "Drag + Drop";
		dragView.align = \right;
		dragView.background_(TXColor.paleYellow);
		dragView.stringColor_(TXColor.sysGuiCol1);
		dragView.canReceiveDragHandler = {View.currentDrag.isKindOf(Array);};
		dragView.receiveDragHandler = {arg view;
			var newArray;
			newArray = this.resampleArray(View.currentDrag.value, tableSize);
			multiSliderView.valueAction_(newArray);
			value = newArray;
			action.value(this);
			//view.focus(false);
		};
		dragView.object = initVal;

		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(0, -40);
		}, {
			window.decorator.shift(0, -40);
		});

		// create grid
		userView = UserView(window, userViewWidth @ userViewHeight);
//		userView.background_(TXColor.sysModuleWindow);
		userView.drawFunc = {
			Pen.color = TXColor.white;
			gridRows.do({arg item, i;
				var holdHeight;
				holdHeight = ((userViewHeight * (i + 1)) / gridRows).asInteger;
				Pen.line(0 @ holdHeight, userViewWidth @ holdHeight);
			});
			gridCols.do({arg item, i;
				var holdWidth;
				holdWidth = ((userViewWidth * (i + 1)) / gridCols).asInteger;
				Pen.line(holdWidth @ 0, holdWidth @ userViewHeight);
			});
			Pen.stringAtPoint(yLabel, 18 @ (userViewHeight/2)-10 );
			Pen.stringAtPoint(xLabel, userViewWidth/2 @ userViewHeight-18 );
			Pen.stroke
		};
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(0-userViewWidth-6, 0);
		}, {
			window.decorator.shift(0-userViewWidth-6, 0);
		});

		// create curve
		multiSliderView = MultiSliderView(window, curveWidth @ curveHeight);
		//multiSliderView.gap_(0);
		if (tableSize > 128, {multiSliderView.gap_(0)});
		multiSliderView.elasticMode(1);
		multiSliderView.valueThumbSize_(curveThumbSize);
		multiSliderView.indexThumbSize_(curveThumbSize);
		multiSliderView.drawLines_(true);
		multiSliderView.drawRects_(true);
		multiSliderView.background_(Color.new(1,1,1,0));
		// multiSliderView.action = {
		multiSliderView.mouseUpAction = {
			value = multiSliderView.value;
			dragView.object = multiSliderView.value;
			action.value(this);
		};
		// multiSliderView.canReceiveDragHandler = {View.currentDrag.isKindOf(Array);};
		multiSliderView.canReceiveDragHandler = false;
		// multiSliderView.receiveDragHandler = {arg view;
		// 	var newArray;
		// 	newArray = this.resampleArray(View.currentDrag.value, tableSize);
		// 	view.valueAction_(newArray);
		// 	view.focus(false);
		// };

		// decorator store settings
		if (window.class == Window, {
			holdTop = window.view.decorator.top;
			holdLeft = window.view.decorator.left;
		}, {
			holdTop = window.decorator.top;
			holdLeft = window.decorator.left;
		});

		// create button
		Button(window, 50 @ 20)
		.states_([["Reverse", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.valueAction = this.value.reverse;
		});
		// create button
		Button(window, 60 @ 20)
		.states_([["Invert", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.valueAction = 1 - this.value;
		});
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-118,26);
		}, {
			window.decorator.shift(-118,26);
		});
		// create button
		Button(window, 50 @ 20)
		.states_([["Mirror", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.valueAction = this.mirror(this.value);
		});
		// create button
		Button(window, 60 @ 20)
		.states_([["Mirror+Inv", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.valueAction = this.mirrorInvert(this.value);
		});
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-118,26);
		}, {
			window.decorator.shift(-118,26);
		});
		// create button
		Button(window, 50 @ 20)
		.states_([["Smooth", TXColor.white, TXColor.sysGuiCol1]])
		.action_({ var inArr, outArr;
			inArr = this.value;
			inArr.size.do({arg item, i;
				if ((i > 0) and: (i < (inArr.size-1)), {
		//			outArr = outArr.add((inArr.at(i) + inArr.at(i-1)) / 2);
					outArr = outArr.add((inArr.at(i) + inArr.at(i-1)+ inArr.at(i+1)) / 3);
				},{
					outArr = outArr.add(inArr.at(i));
				});
			});
			this.valueAction = outArr;
		});
		// create button
		Button(window, 60 @ 20)
		.states_([["Smooth x 5", TXColor.white, TXColor.sysGuiCol1]])
		.action_({ var inArr, outArr;
			inArr = this.value;
			5.do({arg stage, stageNo;
				outArr = [];
				inArr.size.do({arg item, i;
					if ((i > 0) and: (i < (inArr.size-1)), {
						//			outArr = outArr.add((inArr.at(i) + inArr.at(i-1)) / 2);
						outArr = outArr.add((inArr.at(i) + inArr.at(i-1)+ inArr.at(i+1)) / 3);
						},{
							outArr = outArr.add(inArr.at(i));
					});
				});
				inArr = outArr;
			});
			this.valueAction = outArr;
		});
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-118,26);
		}, {
			window.decorator.shift(-118,26);
		});
		// create button
		Button(window, 50 @ 20)
		.states_([["Reset", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.reset;
		});
		if (showBuilderButton == true, {
			// create button
			Button(window, 60 @ 20)
			.states_([["Builder", TXColor.paleYellow, TXColor.sysGuiCol1]])
			.action_({
				TXCurveBuilder.openWithCurve(this.value);
			});
			// decorator shift
			if (window.class == Window, {
				window.view.decorator.shift(-64,0);
				}, {
					window.decorator.shift(-64,0);
			});
		});
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-54,30);
		}, {
			window.decorator.shift(-54,30);
		});
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
			if (window.class == Window, {
				window.view.decorator.shift(-118,26);
			}, {
				window.decorator.shift(-108,26);
			});
		});
		// decorator reset
		if (window.class == Window, {
			window.view.decorator.top = holdTop;
			window.view.decorator.left = holdLeft;
		}, {
			window.decorator.top = holdTop;
			window.decorator.left = holdLeft;
		});

		// decorator next line
		if (window.class == Window, {
			window.view.decorator.nextLine;
		}, {
			window.decorator.nextLine;
		});

		if (showPresets == "Warp", {
			arrGenFunctions = [
				["Preset Curves ...", {this.value;}],
				["Linear", linearArray],
				["Sine", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, \sin).map(item); });}],
				["Exponential", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0.0001, 1, \exp).map(item).linlin(0.0001, 1, 0, 1)});}],
				["-Exponential", {linearArray.deepCopy.collect({arg item, i; 1-ControlSpec(0.0001, 1, \exp).map(1-item).linlin(0.0001, 1, 0, 1)});}],
				["Cosine", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, \cos).map(item); });}],
				["Curve -8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -8).map(item); });}],
				["Curve -7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -7).map(item); });}],
				["Curve -6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -6).map(item); });}],
				["Curve -5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -5).map(item); });}],
				["Curve -4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -4).map(item); });}],
				["Curve -3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -3).map(item); });}],
				["Curve -2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -2).map(item); });}],
				["Curve -1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -1).map(item); });}],
				["Curve +1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 1).map(item); });}],
				["Curve +2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 2).map(item); });}],
				["Curve +3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 3).map(item); });}],
				["Curve +4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 4).map(item); });}],
				["Curve +5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 5).map(item); });}],
				["Curve +6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 6).map(item); });}],
				["Curve +7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 7).map(item); });}],
				["Curve +8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 8).map(item); });}],
				["Sine Cycle Phase 0 deg.", {Signal.sineFill(tableSize, [1.0],[0.0]).collect({arg item, i; (item.value + 1) * 0.5;});}],
				["Sine Cycle Phase 90 deg.", {Signal.sineFill(tableSize, [1.0],[0.5pi]).collect({arg item, i; (item.value + 1) * 0.5;});}],
				["Sine Cycle Phase 180 deg.", {Signal.sineFill(tableSize, [1.0],[pi]).collect({arg item, i; (item.value + 1) * 0.5;});}],
				["Sine Cycle Phase 270 deg.", {Signal.sineFill(tableSize, [1.0],[1.5pi]).collect({arg item, i; (item.value + 1) * 0.5;});}],
				["Double Ramp", {var ramp, rampSize, outArr;
					rampSize = (tableSize/2).asInteger;
					ramp = Array.newClear(rampSize).seriesFill(0, 1/(rampSize-1));
					outArr = (ramp ++ ramp ++ [0, 0, 0, 0]).keep(tableSize);
				}],
				["Triple Ramp", {var ramp, rampSize, outArr;
					rampSize = (tableSize/3).asInteger;
					ramp = Array.newClear(rampSize).seriesFill(0, 1/(rampSize-1));
					outArr = (ramp ++ ramp ++ ramp ++ [0, 0, 0, 0]).keep(tableSize);
				}],
				["Quadruple Ramp", {var ramp, rampSize, outArr;
					rampSize = (tableSize/4).asInteger;
					ramp = Array.newClear(rampSize).seriesFill(0, 1/(rampSize-1));
					outArr = (ramp ++ ramp ++ ramp ++ ramp ++ [0, 0, 0, 0]).keep(tableSize);
				}],
			];
		});
		if (showPresets == "Curve", {
			arrGenFunctions = [
				["Preset Curves ...", {this.value;}],
				["All Maximum",{1 ! tableSize;}],
				["All 0.5",{0.5 ! tableSize;}],
				["All Minimum",{0 ! tableSize;}],
				["Ramp", linearArray],
				["Sine", {Signal.sineFill(tableSize, [1.0],[0.0]).collect({arg item, i; (item.value + 1) * 0.5;});}],
				["Exponential", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0.0001, 1, \exp).map(item).linlin(0.0001, 1, 0, 1)});}],
				["-Exponential", {linearArray.deepCopy.collect({arg item, i; 1-ControlSpec(0.0001, 1, \exp).map(1-item).linlin(0.0001, 1, 0, 1)});}],
				["Triangle", { var holdArr;
					holdArr = Array.newClear(tableSize/2).seriesFill(0, 2 / (tableSize - 1));
					holdArr ++ holdArr.copy.reverse;}],
				["Curve -8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -8).map(item); });}],
				["Curve -7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -7).map(item); });}],
				["Curve -6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -6).map(item); });}],
				["Curve -5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -5).map(item); });}],
				["Curve -4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -4).map(item); });}],
				["Curve -3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -3).map(item); });}],
				["Curve -2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -2).map(item); });}],
				["Curve -1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -1).map(item); });}],
				["Curve +1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 1).map(item); });}],
				["Curve +2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 2).map(item); });}],
				["Curve +3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 3).map(item); });}],
				["Curve +4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 4).map(item); });}],
				["Curve +5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 5).map(item); });}],
				["Curve +6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 6).map(item); });}],
				["Curve +7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 7).map(item); });}],
				["Curve +8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 8).map(item); });}],
			];
		});
		if (showPresets == "LFO", {
			arrGenFunctions = [
				["Preset Curves ...", {this.value;}],
				["Sawtooth", linearArray],
				["Sine", {Signal.sineFill(tableSize, [1.0],[0.0]).collect({arg item, i; (item.value + 1) * 0.5;});}],
				["Exponential", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0.0001, 1, \exp).map(item).linlin(0.0001, 1, 0, 1)});}],
				["-Exponential", {linearArray.deepCopy.collect({arg item, i; 1-ControlSpec(0.0001, 1, \exp).map(1-item).linlin(0.0001, 1, 0, 1)});}],
				["Triangle", { var holdArr;
					holdArr = Array.newClear(tableSize/2).seriesFill(0, 2 / (tableSize - 1));
					holdArr ++ holdArr.copy.reverse;}],
				["Curve -8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -8).map(item); });}],
				["Curve -7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -7).map(item); });}],
				["Curve -6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -6).map(item); });}],
				["Curve -5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -5).map(item); });}],
				["Curve -4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -4).map(item); });}],
				["Curve -3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -3).map(item); });}],
				["Curve -2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -2).map(item); });}],
				["Curve -1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -1).map(item); });}],
				["Curve +1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 1).map(item); });}],
				["Curve +2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 2).map(item); });}],
				["Curve +3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 3).map(item); });}],
				["Curve +4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 4).map(item); });}],
				["Curve +5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 5).map(item); });}],
				["Curve +6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 6).map(item); });}],
				["Curve +7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 7).map(item); });}],
				["Curve +8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 8).map(item); });}],
			];
		});
		if (showPresets == "Velocity", {
			arrGenFunctions = [
				["Preset Curves ...", {this.value;}],
				["All Maximum, like an organ",{1 ! tableSize;}],
				["Heavy 8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -8).map(item); });}],
				["Heavy 7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -7).map(item); });}],
				["Heavy 6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -6).map(item); });}],
				["Heavy 5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -5).map(item); });}],
				["Heavy 4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -4).map(item); });}],
				["Heavy 3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -3).map(item); });}],
				["Heavy 2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -2).map(item); });}],
				["Heavy 1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -1).map(item); });}],
				["Linear", {linearArray.deepCopy;}],
				["Light 1", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 1).map(item); });}],
				["Light 2", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 2).map(item); });}],
				["Light 3", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 3).map(item); });}],
				["Light 4", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 4).map(item); });}],
				["Light 5", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 5).map(item); });}],
				["Light 6", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 6).map(item); });}],
				["Light 7", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 7).map(item); });}],
				["Light 8", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 8).map(item); });}],
				["Custom 1",{TXCustomVelCurves.arrCurves[0];}],
				["Custom 2",{TXCustomVelCurves.arrCurves[1];}],
				["Custom 3",{TXCustomVelCurves.arrCurves[2];}],
				["Custom 4",{TXCustomVelCurves.arrCurves[3];}],
				["Custom 5",{TXCustomVelCurves.arrCurves[4];}],
				["Custom 6",{TXCustomVelCurves.arrCurves[5];}],
				["Custom 7",{TXCustomVelCurves.arrCurves[6];}],
				["Custom 8",{TXCustomVelCurves.arrCurves[7];}],
			];
		});
		if (showPresets == "Waveshaper", {
			arrGenFunctions = [
				["Preset Curves ...", {this.value;}],
				["Linear", linearArray],
				["Sine-shaped Compress", {linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, \cos).map(item); });}],
				["Compress 8", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -8).map(item); });)}],
				["Compress 7", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -7).map(item); });)}],
				["Compress 6", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -6).map(item); });)}],
				["Compress 5", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -5).map(item); });)}],
				["Compress 4", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -4).map(item); });)}],
				["Compress 3", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -3).map(item); });)}],
				["Compress 2", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -2).map(item); });)}],
				["Compress 1", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, -1).map(item); });)}],
				["Expand 1", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 1).map(item); });)}],
				["Expand 2", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 2).map(item); });)}],
				["Expand 3", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 3).map(item); });)}],
				["Expand 4", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 4).map(item); });)}],
				["Expand 5", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 5).map(item); });)}],
				["Expand 6", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 6).map(item); });)}],
				["Expand 7", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 7).map(item); });)}],
				["Expand 8", {this.mirrorInvert(
					linearArray.deepCopy.collect({arg item, i; ControlSpec(0, 1, 8).map(item); });)}],
			];
		});

		if (arrGenFunctions.notNil, {
			popItems = arrGenFunctions.collect({arg item, i; item.at(0);});
			popAction = {arg view;
				if (view.value > 0, {
					newArray = arrGenFunctions.at(view.value).at(1).value;
					this.valueAction = newArray;
				});
			};
			popupView = TXPopup(window, labelWidth + 180 @ 20, "", popItems, popAction,
				0, false, labelWidth);
			popupView.popupMenuView.stringColor_(TXColour.black).background_(TXColor.white);
		});

		// more actions:
		arrModifyFunctions = 	[
			["Modify curve ...", {}],
			[ "normalize", {this.valueAction = this.value.normalize;}],
			[ "double copy", {this.valueAction = this.doubleCopy(this.value);}],
			[ "triple copy", {this.valueAction = this.tripleCopy(this.value);}],
			[ "phase shift 25% = 1/4 = 90 deg", {this.valueAction = this.shiftPhase(this.value, 0.25);}],
			[ "phase shift 33.33% = 1/3 = 120 deg", {this.valueAction = this.shiftPhase(this.value, 0.33333);}],
			[ "phase shift 50% = 1/2 = 180 deg", {this.valueAction = this.shiftPhase(this.value, 0.5);}],
			[ "phase shift 66.66% = 2/3 = 240 deg", {this.valueAction = this.shiftPhase(this.value, 0.66666);}],
			[ "phase shift 75% = 3/4 = 270 deg", {this.valueAction = this.shiftPhase(this.value, 0.75);}],
		];
		popItems = arrModifyFunctions.collect({arg item, i; item.at(0);});
		popAction = {arg view;
			arrModifyFunctions.at(view.value).at(1).value;
			popupView2.popupMenuView.value = 0; // reset popup
			//{popupView2.popupMenuView.value = 0;}.defer(0.3); // reset popup
		};
		popupView2 = TXPopup(window, 210 @ 20, "", popItems, popAction,
			0, false, 4);
		popupView2.popupMenuView.stringColor_(TXColour.black).background_(TXColor.white);

		if (initAction, {
			this.valueAction = initVal;
		}, {
			this.value = initVal;
		});
	}
	value_ { arg argValue;
		multiSliderView.value = argValue;
		value = multiSliderView.value;
		dragView.object = multiSliderView.value;
	}
	valueAction_ { arg argValue;
		multiSliderView.value = argValue;
		value = multiSliderView.value;
		dragView.object = multiSliderView.value;
		action.value(this);
	}
	reset {
		this.valueAction = resetArray;
		if (popupView.notNil, {popupView.value = 0});
	}
	set { arg label, argAction, initVal, initAction=false;
		labelView.string = label;
		action = argAction;
		initVal = initVal ? resetArray;
		if (initAction) {
			this.valueAction = initVal;
		}{
			this.value = initVal;
		};
	}
	storeSlot { arg num;
		arrSlotData.put(num, this.value);
	}
	loadSlot { arg num;
		this.valueAction = this.resampleArray(arrSlotData.at(num), tableSize);
	}
	mirror { arg arrCurveValues;
		var outArray, startVal, midArray, endVal, holdSignal;
		outArray = arrCurveValues.deepCopy;
		startVal = outArray.first.asArray;
		midArray = outArray.drop(2).drop(-2);
		midArray = midArray.clump(2).collect({arg item, i; item.sum/2;});
		endVal = outArray.last.asArray;
		outArray = startVal ++ midArray ++ endVal;
		outArray = outArray ++ outArray.copy.reverse;
		holdSignal = Signal.newFrom(outArray);
		^Array.newFrom(holdSignal);
	}
	mirrorInvert { arg arrCurveValues;
		var outArray, startVal, midArray, endVal, holdSignal;
		outArray = arrCurveValues.deepCopy;
		startVal = outArray.first.asArray;
		midArray = outArray.drop(2).drop(-2);
		midArray = midArray.clump(2).collect({arg item, i; item.sum/2;});
		endVal = outArray.last.asArray;
		outArray = startVal ++ midArray ++ endVal;
		//outArray = (outArray ++ outArray.copy.reverse.neg).normalize(0,1);
		outArray = ((outArray - 1) ++ (outArray.copy.reverse.neg + 1)).normalize(0,1);
		holdSignal = Signal.newFrom(outArray);
		^Array.newFrom(holdSignal);
	}

	doubleCopy { arg arrCurveValues;
		// var copyArray, outArray, startVal, midArray, endVal, holdSignal;
		// copyArray = arrCurveValues.deepCopy;
		// startVal = copyArray.first.asArray;
		// midArray = copyArray.drop(2).drop(-2);
		// midArray = midArray.clump(2).collect({arg item, i; item.sum/2;});
		// endVal = copyArray.last.asArray;
		// outArray = startVal ++ midArray ++ endVal;
		// outArray = outArray ++ outArray;
		// holdSignal = Signal.newFrom(outArray);
		// ^Array.newFrom(holdSignal);
		var outArray;
		outArray = arrCurveValues.size.collect({arg i; arrCurveValues.wrapAt(i * 2); });
		^outArray;
	}
	tripleCopy { arg arrCurveValues;
		// var copyArray, outArray, startVal, midArray, endVal, holdSignal;
		// copyArray = arrCurveValues.deepCopy;
		// startVal = copyArray.first.asArray;
		// midArray = copyArray.drop(3).drop(-3);
		// midArray = midArray.clump(3).collect({arg item, i; item.sum/item.size;});
		// endVal = copyArray.last.asArray;
		// outArray = startVal ++ midArray ++ endVal;
		// outArray = outArray ++ outArray ++ outArray;
		// holdSignal = Signal.newFrom(outArray);
		// ^Array.newFrom(holdSignal);
		var outArray;
		outArray = arrCurveValues.size.collect({arg i; arrCurveValues.wrapAt(i * 3); });
		^outArray;
	}
	shiftPhase { arg arrCurveValues, shiftPhaseAmount;
		var outArray, startIndex, midArray, holdSignal;
		if ((shiftPhaseAmount > 0) && (shiftPhaseAmount < 1), {
			midArray = arrCurveValues.deepCopy;
			startIndex = (shiftPhaseAmount * (arrCurveValues.size-1)).asInteger;
			outArray = midArray[startIndex..] ++ midArray[..(startIndex-1)];
			holdSignal = Signal.newFrom(outArray);
			^Array.newFrom(holdSignal);
			// ^outArray;
		},{
			^arrCurveValues;
		});
	}

	setGridRowsCols { arg rows, cols;
		gridRows = rows;
		gridCols = cols;
		userView.refresh;
	}

	resampleArray {arg inArray, newSize;
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

}
