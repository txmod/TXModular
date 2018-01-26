// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMultiNumber {	// TXMultiNumber module with label
	var <>labelView, <>scrollView, <>arrNumberViews, <>controlSpec, <>action, <value, <size;

	*new { arg window, dimensions, label, controlSpec, action, initVal,
			initAction=false, labelWidth=80, numberWidth = 20, cloneButton=true,
			scrollInc, scrollViewWidth;
		^super.new.init(window, dimensions, label, controlSpec, action, initVal,
			initAction, labelWidth, numberWidth, cloneButton, scrollInc, scrollViewWidth);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initVal,
			initAction, labelWidth, numberWidth, cloneButton, scrollInc, scrollViewWidth;
		var holdNumberBox, scrollBox;
		labelView = StaticText(window, labelWidth @ dimensions.y);
		labelView.string = label;
		labelView.align = \right;

		controlSpec = argControlSpec.asSpec;
		initVal = initVal ? Array.fill(8, controlSpec.default);
		action = argAction;

		value = initVal;
		// size of array of number is derived from initVal size
		size = initVal.size;

		if (scrollViewWidth.notNil, {
			scrollView = ScrollView(window, Rect(0, 0, scrollViewWidth, dimensions.y))
				.hasBorder_(false);
			if (GUI.current.asSymbol == \cocoa, {
				scrollView.autoScrolls_(false);
			});
			scrollView.hasHorizontalScroller = false;
			scrollView.hasVerticalScroller = false;
			scrollBox = CompositeView(scrollView, Rect(0, 0, 4+(initVal.size * 24), dimensions.y));
			scrollBox.decorator = FlowLayout(scrollBox.bounds);
			scrollBox.decorator.margin.x = 0;
			scrollBox.decorator.margin.y = 0;
			scrollBox.decorator.reset;
		});

		size.do({ arg item, i;
			holdNumberBox = TXScrollNumBox(scrollBox?window, numberWidth @ dimensions.y, controlSpec).maxDecimals_(4);
			holdNumberBox .font_(Font.new("Gill Sans", 10));
			holdNumberBox.action = { arg view;
				view.value = controlSpec.constrain(view.value);
				value = arrNumberViews.collect({ arg item, i; item.value});
				action.value(this);
			};
			//if (scrollInc.notNil, {holdNumberBox.inc = scrollInc}); // no longer used
			arrNumberViews = arrNumberViews.add(holdNumberBox);
			holdNumberBox.value = initVal.at(i);
		});

		if (cloneButton == true, {
			Button(window, 50 @ 20)
			.states_([["clone 1", TXColor.white, TXColor.sysGuiCol1]])
			.action_({|view|
				arrNumberViews.do({ arg item, i;
					if (i > 0, {
						item.value = arrNumberViews.at(0).value;
					});
				});
				value = arrNumberViews.collect({ arg item, i; item.value});
				action.value(this);
			});
		});

		if (initAction) {
			action.value(this);
		};
	}
	value_ { arg argValue;
		arrNumberViews.do({ arg item, i;
			if (argValue.asArray.at(i).notNil, {
				item.value = argValue.at(i);
			});
		});
	}
	valueAction_ { arg argValue;
		arrNumberViews.do({ arg item, i;
			if (argValue.asArray.at(i).notNil, {
				item.value = argValue.at(i);
			});
		});
		action.value(this);
	}

	hasFocus {
		arrNumberViews.do({arg item;
			if (item.hasFocus, {
				^true;
			});
		});
		^false;
	}
}
