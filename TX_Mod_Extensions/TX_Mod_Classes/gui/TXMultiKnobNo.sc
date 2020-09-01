// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMultiKnobNo {
	var <>labelView, <>arrKnobViews, <>arrNumBoxViews, <>controlSpec, <>action, <value, <size;

	*new { arg window, dimensions, label, controlSpec, action, initVal,
			initAction=false, labelWidth=40, knobWidth = 50, numSpacers = 0;
		^super.new.init(window, dimensions, label, controlSpec, action, initVal,
			initAction, labelWidth, knobWidth, numSpacers);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initVal,
			initAction, labelWidth, knobWidth, numSpacers;
		var holdKnob, holdNumBox, startLeft;

		startLeft = window.decorator.left;

		labelView = StaticText(window, labelWidth @ dimensions.y);
		labelView.string = label;
		labelView.align = \right;

		controlSpec = argControlSpec.asSpec;
		initVal = initVal ? Array.fill(8, controlSpec.default);
		action = argAction;

		value = initVal;
		size = initVal.size;

		// add spacers
		(numSpacers ? 0).do({ arg i;
			StaticText(window, knobWidth @ knobWidth);
		});
		// add knobs
		size.do({ arg item, i;
			holdKnob = Knob(window, knobWidth @ knobWidth);
			holdKnob.action = { arg view;
				value[i] = controlSpec.map(view.value);
				arrNumBoxViews[i].value = value[i];
				action.value(this);
			};
			arrKnobViews = arrKnobViews.add(holdKnob);
			holdKnob.value = controlSpec.unmap(initVal.at(i));
		});

		window.decorator.nextLine.shift(startLeft + labelWidth + window.decorator.gap.x, 0);

		// add spacers
		(numSpacers ? 0).do({ arg i;
			StaticText(window, knobWidth @ 18);
		});
		// add numboxes
		size.do({ arg item, i;
			holdNumBox = NumberBox(window, knobWidth @ 18);
			holdNumBox.decimals = 3;
			holdNumBox.action = { arg view;
				view.value = controlSpec.constrain( view.value);
				value[i] = view.value;
				arrKnobViews[i].value = controlSpec.unmap(value[i]);
				action.value(this);
			};
			arrNumBoxViews = arrNumBoxViews.add(holdNumBox);
			holdNumBox.value = initVal.at(i);
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
		arrNumBoxViews.do({ arg item, i;
			if (argValue.at(i).notNil, {
				item.value = argValue.at(i);
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
		arrNumBoxViews.do({arg item;
			if (item.hasFocus, {
				^true;
			});
		});
		^false;
	}
}
