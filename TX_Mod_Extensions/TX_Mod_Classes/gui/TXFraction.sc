// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFraction {	// Fraction module with label
	var <>labelView, <>labelView2, <>labelView3, <>numberViewX, <>numberViewY, <>numberView, <>action, <value;

	*new { arg argParent, dimensions, label, controlSpec, action, initVal,
			initAction=false, labelWidth=80, numberWidth=50;
		^super.new.init(argParent, dimensions, label, controlSpec, action, initVal,
			initAction, labelWidth, numberWidth);
	}
	init { arg argParent, dimensions, label, controlSpec, argAction, initVal,
			initAction, labelWidth, numberWidth;
		var holdFraction;

		controlSpec = controlSpec.asSpec;
		labelView = StaticText(argParent, labelWidth @ dimensions.y);
		labelView.string = label;
		labelView.align = \right;

		initVal = initVal ? 0;
		action = argAction;

		numberViewX = TXScrollNumBox(argParent, 24 @ dimensions.y).maxDecimals_(4);
		numberViewX.action = {
			numberView.value = value = controlSpec.constrain(numberViewX.value / numberViewY.value);
			action.value(this);
		};

		labelView2 = StaticText(argParent, 10 @ dimensions.y)
			.string_("/");

		numberViewY = TXScrollNumBox(argParent, 24 @ dimensions.y).maxDecimals_(4);
		numberViewY.action = {
			numberView.value = value = controlSpec.constrain(numberViewX.value / numberViewY.value);
			action.value(this);
		};

		labelView3 = StaticText(argParent, 10 @ dimensions.y)
			.string_("=");

		numberView = TXScrollNumBox(argParent, numberWidth @ dimensions.y).maxDecimals_(4);
		numberView.action = {
			numberView.value = value = controlSpec.constrain(numberView.value);
			if (value == 0, {
				numberViewX.value = 0;
				numberViewY.value = 0;
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
		numberView.value = value = numberView.controlSpec.constrain(argVal);
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
	set { arg label, argAction, initVal, initAction=false;
		labelView.string = label;
		action = argAction;
		initVal = initVal ? 0;
		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			numberView.value = value;
			numberViewX.value = numberView.value;
			numberViewY.value = 1;
		};
	}
}

