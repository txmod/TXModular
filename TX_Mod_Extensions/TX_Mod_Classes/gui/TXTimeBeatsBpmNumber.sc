// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXTimeBeatsBpmNumber {	// Time and equivalent beats at a bpm
	var <>labelView, <>labelView2, <>labelView3, <>labelView4, <>numberViewBeats, <>numberViewBPM,
			<>numberView, <>action, <value, <>controlSpec;

	*new { arg argParent, dimensions, label, argControlSpec, action, initVal,
			initAction=false, labelWidth=80, numberWidth=50;
		^super.new.init(argParent, dimensions, label, argControlSpec, action, initVal,
			initAction, labelWidth, numberWidth);
	}
	init { arg argParent, dimensions, label, argControlSpec, argAction, initVal,
			initAction, labelWidth, numberWidth;

		controlSpec = argControlSpec;
		labelView = StaticText(argParent, labelWidth @ dimensions.y);
		labelView.string = label ? "Time";
		labelView.align = \right;

		initVal = initVal ? 0;
		action = argAction;

		numberView = TXScrollNumBox(argParent, numberWidth @ dimensions.y).maxDecimals_(4);
		numberView.action = {
			numberView.value = value = controlSpec.constrain(numberView.value);
			numberViewBeats.value = numberView.value * (numberViewBPM.value / 60);
			action.value(this);
		};

		labelView2 = StaticText(argParent, 50 @ dimensions.y)
			.string_("secs =");

		numberViewBeats = TXScrollNumBox(argParent, numberWidth @ dimensions.y).maxDecimals_(4);
		numberViewBeats.action = {
			numberView.value = value = controlSpec.constrain(numberViewBeats.value * (60 / numberViewBPM.value));
			action.value(this);
		};

		labelView3 = StaticText(argParent, 50 @ dimensions.y)
			.string_("beats at:");

		numberViewBPM = TXScrollNumBox(argParent, numberWidth @ dimensions.y).maxDecimals_(4);
		numberViewBPM.action = {
			numberView.value = value = controlSpec.constrain(numberViewBeats.value * (60 / numberViewBPM.value));
			action.value(this);
		};

		labelView4 = StaticText(argParent, 40 @ dimensions.y)
			.string_("BPM");

		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			numberView.value = value;
			numberViewBeats.value = numberView.value;
			numberViewBPM.value = 60;
		};
	}
	value_ { arg argVal;
		numberView.valueAction = argVal;
	}

	valueAction_  { arg argVal;
		numberView.valueAction = argVal;
	}
	valueNoAction_  { arg argVal;
			numberView.value = value = controlSpec.constrain(argVal);
			numberViewBeats.value = numberView.value * (numberViewBPM.value / 60);
	}
	set { arg label, argAction, initVal, initAction=false;
		labelView.string = label;
		action = argAction;
		initVal = initVal ? 0;
		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			numberView.value = value;
			numberViewBeats.value = numberView.value;
			numberViewBPM.value = 60;
		};
	}

	hasFocus {
		^numberViewBeats.hasFocus || numberViewBPM.hasFocus || numberView.hasFocus;
	}
}

