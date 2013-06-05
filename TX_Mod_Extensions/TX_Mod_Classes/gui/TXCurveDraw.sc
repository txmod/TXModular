// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXCurveDraw {	// MultiSlider, popup and buttons for curve drawing with 5 user slots for saving curves
	var <>labelView, <>multiSliderView, <>popupView, <>action, <value, <arrSlotData;
	var linearArray, resetArray, tableSize, userView, gridRows, gridCols;

	*new { arg window, dimensions, label, action, initVal, initAction=false, labelWidth=80, initSlotVals,
			showPresets, curveWidth=257, curveHeight=257, resetAction="Ramp",
			gridRowsFunc, gridColsFunc, xLabel, yLabel;
		^super.new.init(window, dimensions, label, action, initVal, initAction, labelWidth, initSlotVals,
			showPresets, curveWidth, curveHeight, resetAction,
			gridRowsFunc, gridColsFunc, xLabel, yLabel);
	}
	init { arg window, dimensions, label, argAction, initVal, initAction, labelWidth, initSlotVals,
			showPresets, curveWidth, curveHeight, resetAction,
			gridRowsFunc, gridColsFunc, xLabel, yLabel;
		var arrGenFunctions, popItems, popAction, newArray, holdTop, holdLeft;

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
		if (resetAction == "Sine", {
			resetArray = Signal.sineFill(tableSize, [1.0],[1.5pi])
				.collect({arg item, i; (item.value + 1) * 0.5;});
		});
		initSlotVals = initSlotVals ? resetArray.dup(5);
		arrSlotData = initSlotVals;
		action = argAction;
		xLabel = (xLabel ? "Input").as(String);
		yLabel = (yLabel ? "Output").as(String);

		labelView = StaticText(window, labelWidth @ 20);
		labelView.string = label;
		labelView.align = \right;

		gridRows = gridRowsFunc.value ?? 2;
		gridCols = gridColsFunc.value ?? 2;

		// create grid
		userView = UserView(window, curveWidth @ curveHeight);
//		userView.background_(TXColor.sysModuleWindow);
		userView.drawFunc = {
			Pen.color = Color.white;
			gridRows.do({arg item, i;
				var holdHeight;
				holdHeight = ((curveHeight * (i + 1)) / gridRows).asInteger;
				Pen.line(0 @ holdHeight, curveWidth @ holdHeight);
			});
			gridCols.do({arg item, i;
				var holdWidth;
				holdWidth = ((curveWidth * (i + 1)) / gridCols).asInteger;
				Pen.line(holdWidth @ 0, holdWidth @ curveHeight);
			});
			Pen.stringAtPoint(yLabel, 18 @ (curveHeight/2)-10 );
			Pen.stringAtPoint(xLabel, curveWidth/2 @ curveHeight-18 );
			Pen.stroke
		};
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(0-curveWidth-6, 0);
		}, {
			window.decorator.shift(0-curveWidth-6, 0);
		});

		// create curve
		multiSliderView = MultiSliderView(window, curveWidth @ curveHeight);
		if (tableSize > 128, {multiSliderView.gap_(0)});
		multiSliderView.elasticMode(1);
		multiSliderView.valueThumbSize_(1);
		multiSliderView.indexThumbSize_(1);
		multiSliderView.drawLines_(true);
		multiSliderView.drawRects_(true);
		multiSliderView.background_(Color.new(1,1,1,0));
		multiSliderView.action = {
			value = multiSliderView.value;
			action.value(this);
		};
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
		.states_([["Invert", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.valueAction = 1 - this.value;
		});
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-54,26);
		}, {
			window.decorator.shift(-54,26);
		});
		// create button
		Button(window, 50 @ 20)
		.states_([["Reverse", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.valueAction = this.value.reverse;
		});
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-54,26);
		}, {
			window.decorator.shift(-54,26);
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
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-54,26);
		}, {
			window.decorator.shift(-54,26);
		});
		// create button
		Button(window, 50 @ 20)
		.states_([["Reset", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.reset;
		});
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-54,40);
		}, {
			window.decorator.shift(-54,40);
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
				window.view.decorator.shift(-108,26);
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
		if (showPresets == "LFO", {
			arrGenFunctions = [
				["Preset Curves ...", {this.value;}],
				["Sawtooth", linearArray],
				["Sine", {Signal.sineFill(tableSize, [1.0],[0.0]).collect({arg item, i; (item.value + 1) * 0.5;});}],
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
				["All Maximum - like an organ",{1 ! tableSize;}],
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
				["Preset Curves to Compress or Expand ...", {this.value;}],
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
				newArray = arrGenFunctions.at(view.value).at(1).value;
				this.valueAction = newArray;
			};
			popupView = TXPopup(window, 400 @ 20, "", popItems, popAction,
				0, false, labelWidth);
			popupView.popupMenuView.stringColor_(TXColour.black).background_(TXColor.white);		});

		if (initAction, {
			this.valueAction = initVal;
		}, {
			value = initVal;
			multiSliderView.value = value;
		});
	}
	value_ { arg argValue;
		multiSliderView.value = argValue;
		value = multiSliderView.value;
	}
	valueAction_ { arg argValue;
		multiSliderView.value = argValue;
		value = multiSliderView.value;
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
			value = initVal;
			multiSliderView.value = value;
		};
	}
	storeSlot { arg num;
		arrSlotData.put(num, this.value);
	}
	loadSlot { arg num;
		this.valueAction = arrSlotData.at(num);
	}
	mirrorInvert { arg arrCurveValues;
		var holdArray, holdSignal, newArrCurveValues;
		holdArray = arrCurveValues.deepCopy;
		holdArray.removeAt(0);
		holdArray = holdArray.clump(2).collect({arg item, i; item.sum/2;});
		holdArray = holdArray.copy.neg.reverse ++ [0] ++ holdArray;
		holdArray = (holdArray + 1) / 2;
		holdSignal = Signal.newFrom(holdArray);
		^Array.newFrom(holdSignal);
	}

}
