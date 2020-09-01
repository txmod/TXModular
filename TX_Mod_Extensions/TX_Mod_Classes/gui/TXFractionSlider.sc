// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFractionSlider {	// Fraction module with label & slider
	var <>labelView, <>labelView2, <>labelView3, <>numberViewX, <>numberViewY, <>numberView, <>sliderView;
	var <>controlSpec, <>action, <value;

	*new { arg argParent, dimensions, label, controlSpec, action, initVal,
		initAction=false, labelWidth=80, numberWidth=50, sliderWidth = 100;
		^super.new.init(argParent, dimensions, label, controlSpec, action, initVal,
			initAction, labelWidth, numberWidth, sliderWidth);
	}
	init { arg argParent, dimensions, label, argControlSpec, argAction, initVal,
		initAction, labelWidth, numberWidth, sliderWidth;
		var holdFraction;

		controlSpec = argControlSpec.asSpec;
		labelView = StaticText(argParent, labelWidth @ dimensions.y);
		labelView.string = label;
		labelView.align = \right;

		initVal = initVal ? 0;
		action = argAction;

		numberViewX = TXScrollNumBox(argParent, 24 @ dimensions.y).maxDecimals_(4);
		numberViewX.action = {
			// numberView.value = value = controlSpec.constrain(numberViewX.value / numberViewY.value);
			// action.value(this);
			numberView.valueAction = value = controlSpec.constrain(numberViewX.value / numberViewY.value);
		};

		labelView2 = StaticText(argParent, 10 @ dimensions.y)
		.string_("/");

		numberViewY = TXScrollNumBox(argParent, 24 @ dimensions.y).maxDecimals_(4);
		numberViewY.action = {
			// numberView.value = value = controlSpec.constrain(numberViewX.value / numberViewY.value);
			// action.value(this);
			numberView.valueAction = value = controlSpec.constrain(numberViewX.value / numberViewY.value);
		};

		labelView3 = StaticText(argParent, 10 @ dimensions.y)
		.string_("=");

		numberView = TXScrollNumBox(argParent, numberWidth @ dimensions.y).maxDecimals_(4);
		numberView.action = {
			numberView.value = value = controlSpec.constrain(numberView.value);
			sliderView.value = controlSpec.unmap(value);
			if (value == 0, {
				numberViewX.value = 0;
				numberViewY.value = 1;
			},{
				holdFraction = value.asFraction(100, false);
				numberViewX.value = holdFraction.at(0);
				numberViewY.value = holdFraction.at(1);
			});
			action.value(this);
		};

		sliderView = Slider(argParent, sliderWidth @ dimensions.y);
		sliderView.thumbSize_(8).knobColor_(TXColour.white);
		sliderView.action = {arg view;
			numberView.value = value = controlSpec.map(view.value);
			if (value == 0, {
				numberViewX.value = 0;
				numberViewY.value = 1;
			},{
				holdFraction = value.asFraction(100, false);
				numberViewX.value = holdFraction.at(0);
				numberViewY.value = holdFraction.at(1);
			});
			action.value(this);
		};

		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			numberView.value = value;
			sliderView.value = controlSpec.unmap(value);
			if (value == 0, {
				numberViewX.value = 0;
				numberViewY.value = 0;
			},{
				holdFraction = value.asFraction(100, false);
				numberViewX.value = holdFraction.at(0);
				numberViewY.value = holdFraction.at(1);
			});
		};
	}
	value_ { arg argVal;
		numberView.valueAction = argVal;
	}

	valueAction_  { arg argVal;
		numberView.valueAction = argVal;
	}
	valueNoAction_  { arg argVal;
		var holdFraction;
		numberView.value = value = controlSpec.constrain(argVal);
		sliderView.value = controlSpec.unmap(value);
		if (value == 0, {
			numberViewX.value = 0;
			numberViewY.value = 0;
		},{
			holdFraction = value.asFraction(100, false);
			numberViewX.value = holdFraction.at(0);
			numberViewY.value = holdFraction.at(1);
		});
	}
	// to do - check set method
	// set { arg label, argAction, initVal, initAction=false;
	// 	labelView.string = label;
	// 	action = argAction;
	// 	initVal = initVal ? 0;
	// 	if (initAction) {
	// 		this.value = initVal;
	// 	}{
	// 		value = initVal;
	// 		numberView.value = value;
	// 		numberViewX.value = numberView.value;
	// 		numberViewY.value = 1;
	// 	};
	// }

	hasFocus {
		^sliderView.hasFocus || numberView.hasFocus || numberViewX.hasFocus || numberViewY.hasFocus;
	}
}

