// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXEQCurveDraw {	// MultiSlider and buttons for curve drawing with 5 user slots for saving curves
	var <>labelView, <>multiSliderView, <>displaySlider, <>action, <value, <arrSlotData, linearArray, userView;

	*new { arg window, dimensions, label, action, initVal, initAction=false, labelWidth=80, initSlotVals, numSlots, arrSlotFreqs;
		^super.new.init(window, dimensions, label, action, initVal, initAction, labelWidth, initSlotVals, numSlots, arrSlotFreqs);
	}
	init { arg window, dimensions, label, argAction, initVal, initAction, labelWidth, initSlotVals, numSlots, arrSlotFreqs;
		var curveWidth;

		linearArray = Array.fill(numSlots, 0.5);

		labelView = StaticText(window, labelWidth @ 20);
		labelView.string = label;
		labelView.align = \right;

		initVal = initVal ? linearArray;
		initSlotVals = initSlotVals ? linearArray.dup(5);
		arrSlotData = initSlotVals;
		action = argAction;
		curveWidth = (numSlots * 9);

		// create grid
		userView = UserView(window, curveWidth @ 200);
//		userView.background_(TXColor.sysModuleWindow);
		userView.drawFunc = {
			if (arrSlotFreqs.notNil, {
				arrSlotFreqs.do({arg item, i;
					var fromLeft;
					Pen.strokeColor = Color.white;
					Pen.fillColor = Color.white;
					Pen.font = Font( "Helvetica", 10 );
					if ( (i % 4) == 0, {
						fromLeft = (i * 9) + 5;
						Pen.line(fromLeft @ 0, fromLeft @ 200);
						Pen.stringAtPoint(item.asString, fromLeft @ 180 );
					});
					Pen.stroke;
				});
			});
		};
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(0-curveWidth-4, 0);
		}, {
			window.decorator.shift(0-curveWidth-4, 0);
		});

		// create curve
		multiSliderView = MultiSliderView(window, curveWidth @ 200);
		multiSliderView.valueThumbSize_(2);
		multiSliderView.indexThumbSize_(8);
		multiSliderView.drawLines_(true);
		multiSliderView.drawRects_(true);
		multiSliderView.isFilled_(true);
		multiSliderView.action = {
			value = multiSliderView.value;
			action.value(this);
		};

		// create button
		Button(window, 50 @ 20)
		.states_([["Reset", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			this.reset;
		});
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-54,50);
		}, {
			window.decorator.shift(-54,50);
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
				window.view.decorator.shift(-108,30);
			}, {
				window.decorator.shift(-108,30);
			});
		});
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(0,-305);
			window.view.decorator.nextLine;
		}, {
			window.decorator.shift(0,-305);
			window.decorator.nextLine;
		});

		// middle line
		StaticText(window, 80 @ 2).background_(TXColor.sysGuiCol1);

		// decorator shift
		if (window.class == Window, {
			window.view.decorator.nextLine;
			window.view.decorator.shift(0,100);
		}, {
			window.decorator.nextLine;
			window.decorator.shift(0,100);
		});

		// display freq slider
		if (arrSlotFreqs.notNil, {
			displaySlider = EZSlider(window, (curveWidth + labelWidth + 4 + 64 ) @ 20,
				"Display Freq", \freq.asSpec.minval_(arrSlotFreqs.sort.first).maxval_(arrSlotFreqs.sort.last),
				{displaySlider.value = displaySlider.value.nearestInList(arrSlotFreqs)},
				0, false, labelWidth, 60
			);
			displaySlider.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.grey6);
			displaySlider.numberView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.grey6);
			displaySlider.sliderView.background_(TXColor.grey6);
			displaySlider.sliderView.thumbSize_(10);
		});

		if (initAction, {
			this.value = initVal;
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
		this.value = linearArray;
	}
	set { arg label, argAction, initVal, initAction=false;
		labelView.string = label;
		action = argAction;
		initVal = initVal ? linearArray;
		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			multiSliderView.value = value;
		};
	}
	storeSlot { arg num;
		arrSlotData.put(num, this.value);
	}
	loadSlot { arg num;
		this.value = arrSlotData.at(num);
	}
}
