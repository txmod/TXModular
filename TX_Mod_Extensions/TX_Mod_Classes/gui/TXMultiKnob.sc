// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMultiKnob {
	var <>labelView, <>arrKnobViews, <>controlSpec, <>action, <value, <size;

	*new { arg window, dimensions, label, controlSpec, action, initVal,
			initAction=false, labelWidth=40, knobWidth = 50;
		^super.new.init(window, dimensions, label, controlSpec, action, initVal,
			initAction, labelWidth, knobWidth);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initVal,
			initAction, labelWidth, knobWidth;
		var holdKnob;
		labelView = StaticText(window, labelWidth @ dimensions.y);
		labelView.string = label;
		labelView.align = \right;

		controlSpec = argControlSpec.asSpec;
		initVal = initVal ? Array.fill(8, controlSpec.default);
		action = argAction;

		value = initVal;
		size = initVal.size;
		size.do({ arg item, i;
			holdKnob = Knob(window, knobWidth @ knobWidth);
			holdKnob.action = { arg view;
				value[i] = controlSpec.map(view.value);
				action.value(this);
			};
			arrKnobViews = arrKnobViews.add(holdKnob);
			holdKnob.value = controlSpec.unmap(initVal.at(i));
		});

		if (initAction) {
			action.value(this);
		};
	}
	value_ { arg argValue;
		value = argValue;
		arrKnobViews.do({ arg item, i;
			if (argValue.at(i).notNil, {
				item.value = controlSpec.unmap(argValue.at(i));
			});
		});
	}
	valueAction_ { arg argValue;
		this.value_(argValue);
		action.value(this);
	}

	hasFocus {
		arrKnobViews.do({arg item;
			if (item.hasFocus, {
				^true;
			});
		});
		^false;
	}

}
