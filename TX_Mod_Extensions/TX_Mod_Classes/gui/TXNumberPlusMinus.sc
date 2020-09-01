// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXNumberPlusMinus {	// Number module with label and plus / minus buttons

	var <>labelView, <>numberView, <>buttonViewPlus, <>buttonViewMinus, <>controlSpec, <>action, <value;
	var <>round = 0.001;

	*new { arg window, dimensions, label, controlSpec, action, initVal,
			initAction=false, labelWidth=80, numberWidth = 80;
		^super.new.init(window, dimensions, label, controlSpec, action, initVal,
			initAction, labelWidth, numberWidth);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initVal,
			initAction, labelWidth, numberWidth;
		labelView = StaticText(window, labelWidth @ dimensions.y);
		labelView.string = label;
		labelView.align = \right;

		controlSpec = argControlSpec.asSpec;
		initVal = initVal ? controlSpec.default;
		action = argAction;

		numberView = TXScrollNumBox(window, numberWidth @ dimensions.y, controlSpec).maxDecimals_(4);
		numberView.action = {
			numberView.value = value = controlSpec.constrain(numberView.value);
			action.value(this);
		};
		// create plus and minus buttons
		buttonViewPlus = Button(window, 32 @ 20)
		.states_([["-", TXColor.white, TXColor.sysGuiCol1]])
		.action_({|view|
			numberView.valueAction = (this.value - 1);
		});
		buttonViewMinus = Button(window, 32 @ 20)
		.states_([["+", TXColor.white, TXColor.sysGuiCol1]])
		.action_({|view|
			numberView.valueAction = (this.value + 1);
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

	hasFocus {
		^numberView.hasFocus;
	}
}

