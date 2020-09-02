// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
// Transpose module made from 2 number boxes
TXTransposeKey {
	var <>labelView, <>labelView2, <>labelView3, <>numberViewOctave, <>numberViewSemitone, <>sliderViewSemitone, <>controlSpec, <>action;
	var <>round = 1;

	*new { arg window, dimensions, label, action, initVal,
			initAction=false, labelWidth=80, numberWidth = 57;
		^super.new.init(window, dimensions, label, action, initVal,
			initAction, labelWidth, numberWidth);
	}
	init { arg window, dimensions, label, argAction, initVal,
			initAction, labelWidth, numberWidth;
		labelView = StaticText(window, labelWidth @ dimensions.y);
		labelView.string = label ? "Transpose";
		labelView.align = \right;

		controlSpec = ControlSpec(-127, 127, default: 0);
		initVal = initVal ? controlSpec.default;
		action = argAction;

		labelView2 = StaticText(window, 56 @ dimensions.y);
		labelView2.string = "semitones";
		labelView2.align = \right;

		numberViewSemitone = TXScrollNumBox(window, numberWidth @ dimensions.y);
		numberViewSemitone.action = {
			numberViewOctave.value = numberViewOctave.value.max(-10).min(10);
			sliderViewSemitone.value = numberViewSemitone.value.abs - numberViewSemitone.value.abs.asInteger;
			action.value(this);
		};

		sliderViewSemitone = Slider(window, 80 @ dimensions.y);
		sliderViewSemitone.action = {arg view;
			view.value = view.value.min(0.99);
				if (numberViewSemitone.value.isNegative, {
					numberViewSemitone.value = numberViewSemitone.value.asInteger - view.value;
				},{
					numberViewSemitone.value = numberViewSemitone.value.asInteger + view.value;
				});
				action.value(this);
		};
		labelView3 = StaticText(window, 50 @ dimensions.y);
		labelView3.string = "octaves";
		labelView3.align = \right;

		numberViewOctave = TXScrollNumBox(window, numberWidth @ dimensions.y);
		numberViewOctave.action = {
			numberViewOctave.value = numberViewOctave.value.max(-10).min(10);
			action.value(this);
		};


		if (initAction) {
			this.valueAction = initVal;
		}{
			this.value = initVal;
		};
	}
	value_ { arg value;
		value = controlSpec.constrain(value);
		numberViewOctave.value = (value / 12).asInteger;
		if (value.isNegative and: ((value % 12) > 0), {numberViewOctave.value = numberViewOctave.value -1});
		numberViewSemitone.value = (value % 12);
		if (numberViewSemitone.value.isNegative, {
			sliderViewSemitone.value = (numberViewSemitone.value.asInteger - numberViewSemitone.value).min(0.99999);
		},{
			sliderViewSemitone.value = (numberViewSemitone.value - numberViewSemitone.value.asInteger).min(0.99999);
		});
	}
	valueAction_ { arg value;
		value = controlSpec.constrain(value);
		numberViewOctave.value = (value / 12).asInteger;
		if (value.isNegative, {numberViewOctave.value = numberViewOctave.value -1});
		numberViewSemitone.valueAction = (value % 12);
		if (numberViewSemitone.value.isNegative, {
			sliderViewSemitone.value = (numberViewSemitone.value.asInteger - numberViewSemitone.value).min(0.99999);
		},{
			sliderViewSemitone.value = (numberViewSemitone.value - numberViewSemitone.value.asInteger).min(0.99999);
		});
	}
	value {
		^(numberViewOctave.value * 12) + numberViewSemitone.value;
	}
	set { arg label, argAction, initVal, initAction=false;
		labelView.string = label;
		action = argAction;
		initVal = initVal ? controlSpec.default;
		if (initAction) {
			this.valueAction = initVal;
		}{
			this.value = initVal;
		};
	}

	hasFocus {
		^numberViewOctave.hasFocus || numberViewSemitone.hasFocus || sliderViewSemitone.hasFocus;
	}
}

