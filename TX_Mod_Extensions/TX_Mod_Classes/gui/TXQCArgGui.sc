// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXQCArgGui {
	var <>labelView, <>label2View, <>popupMenuView, <>textView, <>numberView, <>sliderView, <>minMaxTextView;
	var	<>minNumView, <>maxNumView, <>actionButtonView, colourPickerButtonView;
	var <>presetPopup, <>checkboxView, <>redNumView, <>greenNumView, <>blueNumView, <>alphaNumView, colourBoxView;
	var argDataTypeVal, string, numValue, activeValue, minValue, maxValue, holdStep;
	var redValue, greenValue, blueValue, alphaValue;
	var holdArrArgs, holdIndex, holdSetNumFunc, holdSetActiveFunc, setActiveFunc;


//	arrQCArgData = [0, "", 0, 1, 0.5, 0.5, 0.5, 1].dup(maxParameters);
	//  array of :  argDataType, argStringVal, argMin, argMax, argRed, argGreen, argBlue, argAlpha
	//  argDataType can be: [0.notPassed, 1.float, 2.integer, 3.string, 4.booleanNum(0/1), 5.colour(RGBA),
	//	6.Directory Name, 7.File Name],

// e.g. holdView = TXQCArgGui(w, viewWidth @ 20, label, getNumFunc, setNumFunc, setActiveFunc, arrArgs, argIndex, setArgValFunc
//	80, 60, 330);

	*new { arg window, dimensions, label, getNumFunc, setNumFunc, setActiveFunc, arrArgs, argIndex, setArgValFunc,
				labelWidth=160, numberWidth = 80, stringWidth=300;
		^super.new.init(window, dimensions, label, getNumFunc, setNumFunc, setActiveFunc, arrArgs, argIndex, setArgValFunc,
			 labelWidth, numberWidth, stringWidth);
	}

	init { arg window, dimensions, label, getNumFunc, setNumFunc, setActiveFunc, arrArgs, argIndex, setArgValFunc,
			 labelWidth, numberWidth, stringWidth;
		var holdColorSpec;

		holdIndex = argIndex;
		holdArrArgs = arrArgs;
		holdSetNumFunc = setNumFunc;
		holdSetActiveFunc = setActiveFunc;
		redValue = 255;
		greenValue = 255;
		blueValue = 255;
		alphaValue = 255;

		// Input no label
		labelView = StaticText(window, labelWidth @ dimensions.y);
		labelView.string = "Input no. " ++ (holdIndex + 1).asString;
		labelView.align = \left;

		// popup
		popupMenuView = PopUpMenu(window, 200 @ dimensions.y);
		//  argDataType can be: [0.notPassed, 1.float, 2.integer, 3.string, 4.booleanNum(0/1), 5.colour(RGBA),
		//	6.Directory Name, 7.File Name],
		popupMenuView.items = [
			"Type: Not used",
			"Type: Number - Float",
			"Type: Number - Integer",
			"Type: String",
			"Type: Boolean number - 0 or 1",
			"Type: Colour - R,G,B,A",
			"Type: Directory Name",
			"Type: File Name",
		];
		popupMenuView.action = {
			argDataTypeVal = popupMenuView.value;
			if (argDataTypeVal == 1, {
				numberView.value = this.currentControlSpec.constrain(numberView.value);
			});
			if (argDataTypeVal == 2, {
				numberView.value = this.currentControlSpec.constrain(numberView.value.round);
				minNumView.value = minNumView.value.round;
				maxNumView.value = maxNumView.value.round;
			});
			if (argDataTypeVal == 4, {
				numberView.value = numberView.value.round.max(0).min(1);
				minNumView.value = 0;
				maxNumView.value = 1;
			});
			if ( (argDataTypeVal == 1) or: (argDataTypeVal == 2) or: (argDataTypeVal == 4), {
				activeValue = 1;
			},{
				activeValue = 0;
			});
//			argDataTypeVal = popupMenuView.value;
			this.updateArrArgs;
			setArgValFunc.value;
			this.adjustVisibility;
			holdSetActiveFunc.value(activeValue);
		};

		// min max text
		minMaxTextView = StaticText(window, 50 @ dimensions.y);
		minMaxTextView.string = "Min/ Max";
		minMaxTextView.align = \right;

		// min number
		minNumView = TXScrollNumBox(window, 40 @ dimensions.y).maxDecimals_(4);
		minNumView.action = {
			if (argDataTypeVal == 2, {
				minNumView.value = minNumView.value.round;
			});
			minValue = minNumView.value;
			this.updateArrArgs;
			setArgValFunc.value;
			sliderView.doAction;
		};
		// max number
		maxNumView = TXScrollNumBox(window, 40 @ dimensions.y).maxDecimals_(4);
		maxNumView.action = {
			if (argDataTypeVal == 2, {
				maxNumView.value = maxNumView.value.round;
			});
			maxValue = maxNumView.value;
			this.updateArrArgs;
			setArgValFunc.value;
			sliderView.doAction;
		};
		// preset popup
		presetPopup = PopUpMenu(window, 15 @ dimensions.y);
		presetPopup.items = [
			"Select a preset",
			"Min & Max: 0 & 1",
			"Min & Max: 0 & 2",
			"Min & Max: 0 & 5",
			"Min & Max: 0 & 10",
			"Min & Max: 0 & 30",
			"Min & Max: 0 & 45",
			"Min & Max: 0 & 50",
			"Min & Max: 0 & 60",
			"Min & Max: 0 & 90",
			"Min & Max: 0 & 100",
			"Min & Max: 0 & 120",
			"Min & Max: 0 & 180",
			"Min & Max: 0 & 360",
			"Min & Max: 0 & 500",
			"Min & Max: 0 & 1000",
			"Min & Max: 0 & 5000",
			"Min & Max: -1 & 0",
			"Min & Max: -1 & 1",
			"Min & Max: -2 & 2",
			"Min & Max: -5 & 5",
			"Min & Max: -10 & 10",
			"Min & Max: -30 & 30",
			"Min & Max: -45 & 45",
			"Min & Max: -50 & 50",
			"Min & Max: -60 & 60",
			"Min & Max: -90 & 90",
			"Min & Max: -100 & 100",
			"Min & Max: -120 & 120",
			"Min & Max: -180 & 180",
			"Min & Max: -360 & 360",
			"Min & Max: -500 & 500",
			"Min & Max: -1000 & 1000",
			"Min & Max: -5000 & 5000",
		];
		presetPopup.action = { arg view;
			var arrMin, arrMax, argIndex;
			arrMin = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					 -1, -1, -2, -5, -10, -30, -45, -50, -60, -90, -100, -120, -180, -360, -500, -1000, -5000];
			arrMax = [1, 2, 5, 10, 30, 45, 50, 60, 90, 100, 120, 180, 360, 500, 1000, 5000,
					0, 1, 2, 5, 10, 30, 45, 50, 60, 90, 100, 120, 180, 360, 500, 1000, 5000];
			if (view.value > 0, {
				argIndex = view.value - 1;
				minNumView.valueAction = arrMin[argIndex];
				maxNumView.valueAction = arrMax[argIndex];
			});
		};

		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(160.neg, 0);
		}, {
			window.decorator.shift(160.neg, 0);
		});

		// File Open Button
		actionButtonView = Button(window, 110 @ dimensions.y)
			.states_([
				["Select a File", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				this.openFile;
			});

		// decorator next line
		if (window.class == Window, {
			window.view.decorator.nextLine;
		}, {
			window.decorator.nextLine;
		});
		// Input name label
		label2View = StaticText(window, labelWidth @ dimensions.y);
		label2View.string = label;
		label2View.align = \left;

		// number
		numberView = TXScrollNumBox(window, numberWidth @ dimensions.y).maxDecimals_(4);
		numberView.action = {
			if (argDataTypeVal == 2, {
				numberView.value = numberView.value.round;
			});
			numberView.value = numValue = this.currentControlSpec.constrain(numberView.value);
			sliderView.value = this.currentControlSpec.unmap(numberView.value);
			this.updateArrArgs;
			setArgValFunc.value;
		};
		// slider
		sliderView = Slider(window, 300 @ dimensions.y);
		sliderView.action = {
			numberView.value = numValue = this.currentControlSpec.map(sliderView.value);
			if (argDataTypeVal == 2, {
				numberView.value = numValue = this.currentControlSpec.constrain(numValue.round);
			});
			this.updateArrArgs;
			setArgValFunc.value;
		};
		// integer adjust
		if (argDataTypeVal == 2, {
			numberView.valueAction(numberView.value.round);
		});

		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift((300 + 4 + numberWidth + 4).neg, 0);
		}, {
			window.decorator.shift((300 + 4 + numberWidth + 4).neg, 0);
		});
		// text
		textView = TextField(window, stringWidth  @ dimensions.y);
		textView.action = {
			string = textView.string;
			this.updateArrArgs;
			setArgValFunc.value;
		};

		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift((stringWidth + 4).neg, 0);
		}, {
			window.decorator.shift((stringWidth + 4).neg, 0);
		});

		// checkbox
		checkboxView = Button(window, 120  @ dimensions.y);
		checkboxView.states = [
			["0 - False", TXColour.sysGuiCol1, TXColor.white],
			["1 - True", TXColor.white, TXColour.sysGuiCol1]
		];
		checkboxView.action = { |view|
			numValue = checkboxView.value;
			this.updateArrArgs;
			setArgValFunc.value;
			view.focus(false);
			window.refresh;
		};

		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift((120 + 4).neg, 0);
		}, {
			window.decorator.shift((120 + 4).neg, 0);
		});

		holdColorSpec = [0, 1].asSpec;
		// red number
		redNumView = TXScrollNumBox(window, 40 @ dimensions.y);
		redNumView.action = {
			redNumView.value  = holdColorSpec.constrain(redNumView.value);
			redValue = redNumView.value;
			this.updateArrArgs;
			setArgValFunc.value;
			this.resetColourBox;
		};
		// green number
		greenNumView = TXScrollNumBox(window, 40 @ dimensions.y);
		greenNumView.action = {
			greenNumView.value  = holdColorSpec.constrain(greenNumView.value);
			greenValue = greenNumView.value;
			this.updateArrArgs;
			setArgValFunc.value;
			this.resetColourBox;
		};
		// blue number
		blueNumView = TXScrollNumBox(window, 40 @ dimensions.y);
		blueNumView.action = {
			blueNumView.value  = holdColorSpec.constrain(blueNumView.value);
			blueValue = blueNumView.value;
			this.updateArrArgs;
			setArgValFunc.value;
			this.resetColourBox;
		};
		// alpha number
		alphaNumView = TXScrollNumBox(window, 40 @ dimensions.y);
		alphaNumView.action = {
			alphaNumView.value  = holdColorSpec.constrain(alphaNumView.value);
			alphaValue = alphaNumView.value;
			this.updateArrArgs;
			setArgValFunc.value;
			this.resetColourBox;
		};

		// colour box
		colourBoxView = DragBoth.new(window, 40 @ dimensions.y);
		this.resetColourBox;
		colourBoxView.beginDragAction_({ arg view, x, y;
			var holdColour;
			view.dragLabel_("Colour");
			holdColour = colourBoxView.background;
			// return colour
			holdColour;
	 	});
		colourBoxView.canReceiveDragHandler = {
			View.currentDrag.isKindOf( Color )
		};
		colourBoxView.receiveDragHandler = {
			var holdDragObject;
			holdDragObject = View.currentDrag;
			colourBoxView.background_(holdDragObject);
			this.setColour(holdDragObject, setArgValFunc);
		};

		// colourPickerButtonView
		colourPickerButtonView = Button(window, 120 @ 20)
		.states_([["Colour Picker", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			TXColour.showPicker;
		});

		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(0, 6);
		}, {
			window.decorator.shift(0, 6);
		});

		this.adjustVisibility;
	}

	valueAll_ { arg arrVals;

//	var <>labelView, <>popupMenuView, <>textView, <>numberView, <>sliderView, <>minNumView, <>maxNumView
//	var <>redNumView, <>greenNumView, <>blueNumView, <>alphaNumView;
//	var argDataTypeVal, string, numValue, minValue, maxValue, redValue, greenValue, blueValue, alphaValue;


		popupMenuView.value = argDataTypeVal = arrVals.at(0);
		textView.string = string = arrVals.at(1);
		minNumView.value = minValue = arrVals.at(2);
		maxNumView.value = maxValue = arrVals.at(3);
		redNumView.value = redValue = arrVals.at(4);
		greenNumView.value = greenValue = arrVals.at(5);
		blueNumView.value = blueValue = arrVals.at(6);
		alphaNumView.value = alphaValue = arrVals.at(7);
		numberView.value = numValue = this.currentControlSpec.constrain(arrVals.at(8));
		sliderView.value = this.currentControlSpec.unmap(numberView.value);
		checkboxView.value = numValue.max(0).min(1);
		// related updates
		this.resetColourBox;
		this.adjustVisibility;
	}

	valueAll{
		^[argDataTypeVal, string, minValue, maxValue,
			redValue, greenValue, blueValue, alphaValue, numValue];
	}

	setColour { arg argColour, setArgValFunc;
		if (argColour.notNil, {
			if (colourBoxView.notNil, {
				if (colourBoxView.notClosed, {
					colourBoxView.background = argColour;
				});
			});
			redValue = argColour.red;
			greenValue = argColour.green;
			blueValue = argColour.blue;
			alphaValue = argColour.alpha;
			this.updateArrArgs;
			setArgValFunc.value;
			redNumView.value = redValue;
			greenNumView.value = greenValue;
			blueNumView.value = blueValue;
			alphaNumView.value = alphaValue;
		});
	}

	resetColourBox {
		if (colourBoxView.notNil, {
			if (colourBoxView.notClosed, {
				colourBoxView.background = Color.new(redValue ? 0, greenValue ? 0, blueValue ? 0, alphaValue ? 1);
			});
		});
	}
	updateArrArgs {
		if (holdIndex.notNil, {
			holdArrArgs[holdIndex] = this.valueAll;
//			holdSetNumFunc.value(this.currentControlSpec.unmap(numValue));
		});
	}

	currentControlSpec {
		^[minValue, maxValue, \lin, holdStep ? 0].asSpec;
	}

	adjustVisibility {
	//  set all to false first
		textView.visible_(false);
		minMaxTextView.visible_(false);
		minNumView.visible_(false);
		maxNumView.visible_(false);
		actionButtonView.visible_(false);
		presetPopup.visible_(false);
		numberView.visible_(false);
		sliderView.visible_(false);
		checkboxView.visible_(false);
		redNumView.visible_(false);
		greenNumView.visible_(false);
		blueNumView.visible_(false);
		alphaNumView.visible_(false);
		colourBoxView.visible_(false);
		colourPickerButtonView.visible_(false);

	//  argDataType can be: [0.notPassed, 1.float, 2.integer, 3.string, 4.booleanNum(0/1), 5.colour(RGBA)],
		if (argDataTypeVal == 1, {
			minMaxTextView.visible_(true);
			minNumView.visible_(true);
			maxNumView.visible_(true);
			presetPopup.visible_(true);
			numberView.visible_(true);
			sliderView.visible_(true);
			holdStep = 0;
		});
		if (argDataTypeVal == 2, {
			minMaxTextView.visible_(true);
			minNumView.visible_(true);
			maxNumView.visible_(true);
			presetPopup.visible_(true);
			numberView.visible_(true);
			sliderView.visible_(true);
			holdStep = 1;
		});
		if (argDataTypeVal == 3, {
			textView.visible_(true);
			textView.canFocus_(true);
		});
		if (argDataTypeVal == 4, {
			checkboxView.visible_(true);
		});
		if (argDataTypeVal == 5, {
			redNumView.visible_(true);
			greenNumView.visible_(true);
			blueNumView.visible_(true);
			alphaNumView.visible_(true);
			colourBoxView.visible_(true);
			colourPickerButtonView.visible_(true);
		});
		if (argDataTypeVal == 6, {
			actionButtonView.visible_(true);
			textView.visible_(true);
//			textView.canFocus_(false);
		});
		if (argDataTypeVal == 7, {
			actionButtonView.visible_(true);
			textView.visible_(true);
//			textView.canFocus_(false);
		});
	}

	openFile {
		var firstPath;
		// get path/filename
		CocoaDialog.getPaths({ arg paths;
			firstPath = paths.at(0);
			if (argDataTypeVal == 6, {
				textView.string = firstPath.dirname;
				textView.doAction;
			});
			if (argDataTypeVal == 7, {
				textView.string = firstPath;
				textView.doAction;
			});
		});
	}

}
