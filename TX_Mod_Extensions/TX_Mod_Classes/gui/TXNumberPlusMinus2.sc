// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXNumberPlusMinus2 {
	// Number module with label and plus / minus buttons
	// version 2 adds arrPlusMinusValues as argument

	var <>labelView, <>numberView, <>arrButtonViews, <>controlSpec, <>action, <value;
	var <>round = 0.001;

	*new { arg window, dimensions, label, controlSpec, action, initVal,
			initAction=false, labelWidth=80, numberWidth=80, arrPlusMinusValues, boolScrolling;
		^super.new.init(window, dimensions, label, controlSpec, action, initVal,
			initAction, labelWidth, numberWidth, arrPlusMinusValues, boolScrolling);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initVal,
			initAction, labelWidth, numberWidth, arrPlusMinusValues, boolScrolling = 1;
		labelWidth = labelWidth ?? 80;
		numberWidth = numberWidth ?? 80;
		labelView = StaticText(window, labelWidth @ dimensions.y);
		labelView.string = label;
		labelView.align = \right;

		controlSpec = argControlSpec.asSpec;
		initVal = initVal ? controlSpec.default;
		action = argAction;

		numberView = TXScrollNumBox(window, numberWidth @ dimensions.y).maxDecimals_(4);
		if (boolScrolling == false, {numberView.scroll = false});
		numberView.action = {
			numberView.value = value = controlSpec.constrain(numberView.value);
			action.value(this);
		};
		// create plus and minus buttons
		arrPlusMinusValues = arrPlusMinusValues ? [-1,1];
		arrPlusMinusValues.do({arg item, i;
			var holdString;
			if (item.isPositive, {
				holdString = "+" ++ item.asString;
			}, {
				holdString = item.asString;
			});
			arrButtonViews = arrButtonViews.add(
				Button(window, 32 @ 20)
				.states_([[holdString, TXColor.white, TXColor.sysGuiCol1]])
				.action_({|view|
					numberView.valueAction = (numberView.value + item);
				});
			);
		});

		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			numberView.value = value.round(round);
		};
	}
	value_ { arg value; numberView.valueAction = value }
	set { arg label, spec, argAction, initVal, initAction=false;
		labelView.string = label;
		controlSpec = spec.asSpec;
		action = argAction;
		initVal = initVal ? controlSpec.default;
		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			numberView.value = value.round(round);
		};
	}
}

