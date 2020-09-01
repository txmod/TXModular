// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMultiCheckBox {	// TXMultiCheckBox module with label
	var <>labelView, <>arrCheckboxViews, <>action, <value, <size;

	*new { arg window, dimensions, label, action, initVal,
			initAction=false, labelWidth=80, numberWidth = 20;
		^super.new.init(window, dimensions, label, action, initVal,
			initAction, labelWidth, numberWidth);
	}
	init { arg window, dimensions, label, argAction, initVal,
			initAction, labelWidth, numberWidth;
		var holdCheckBox;
		labelView = StaticText(window, labelWidth @ dimensions.y);
		labelView.string = label;
		labelView.align = \right;

		initVal = initVal ? Array.fill(8, 0);
		action = argAction;

		value = initVal;
		size = initVal.size;
		size.do({ arg item, i;
			holdCheckBox = TXCheckBox(window, numberWidth @ dimensions.y, onOffTextType: 1);
			holdCheckBox.action = { arg view;
				value = arrCheckboxViews.collect({ arg item, i; item.value});
				action.value(this);
			};
			arrCheckboxViews = arrCheckboxViews.add(holdCheckBox);
			holdCheckBox.value = initVal.at(i);
		});

		if (initAction) {
			action.value(this);
		};
	}
	value_ { arg argValue;
		arrCheckboxViews.do({ arg item, i;
			if (argValue.at(i).notNil, {
				item.value = argValue.at(i);
			});
		});
	}
	valueAction_ { arg argValue;
		arrCheckboxViews.do({ arg item, i;
			if (argValue.at(i).notNil, {
				item.value = argValue.at(i);
			});
		});
		action.value(this);
	}

	hasFocus {
		arrCheckboxViews.do({arg item;
			if (item.hasFocus, {
				^true;
			});
		});
		^false;
	}

}

