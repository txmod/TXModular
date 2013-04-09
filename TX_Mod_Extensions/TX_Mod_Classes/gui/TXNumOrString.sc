// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXNumOrString {
	var <>labelView, <>numberView, <>controlSpec, <>popupMenuView, <>textView, argTypeVal, numValue, string;

// e.g.	holdView = TXNumOrString(w, viewWidth @ 20, item.at(1), controlSpec, getTypeFunc, setTypeFunc,
//			getNumFunc, setNumFunc, getStringFunc, setStringFunc, 80);

	*new { arg window, dimensions, label, controlSpec, getTypeFunc, setTypeFunc,
			getNumFunc, setNumFunc, getStringFunc, setStringFunc, labelWidth=80, numberWidth = 80, stringWidth=300;
		^super.new.init(window, dimensions, label, controlSpec, getTypeFunc, setTypeFunc,
			getNumFunc, setNumFunc, getStringFunc, setStringFunc, labelWidth, numberWidth, stringWidth);
	}
	init { arg window, dimensions, label, argControlSpec, getTypeFunc, setTypeFunc,
			getNumFunc, setNumFunc, getStringFunc, setStringFunc, labelWidth, numberWidth, stringWidth;

		labelView = StaticText(window, labelWidth @ dimensions.y);
		labelView.string = label;
		labelView.align = \right;

		// popup
		popupMenuView = PopUpMenu(window, 250 @ dimensions.y);
		popupMenuView.items = ["Argument type: String", "Argument type: Number - Float",
			"Argument type: Number - Integer"];
		popupMenuView.action = {
			argTypeVal = popupMenuView.value;
			setTypeFunc.value(popupMenuView.value);
			if (argTypeVal == 2, {
				numberView.value = numberView.value.asInteger;
			});
			this.adjustVisibility;
		};
		// decorator next line
		if (window.class == Window, {
			window.view.decorator.nextLine;
		}, {
			window.decorator.nextLine;
		});
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(labelWidth + 4, 0);
		}, {
			window.decorator.shift(labelWidth + 4, 0);
		});
		// number
		controlSpec = argControlSpec.asSpec;
		numberView = TXScrollNumBox(window, numberWidth @ dimensions.y, controlSpec).maxDecimals_(4);
		numberView.action = {
			if (argTypeVal == 2, {
				numberView.value = numberView.value.asInteger;
			});
			numberView.value = numValue = controlSpec.constrain(numberView.value);
			setNumFunc.value(numberView.value);
		};

		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-64, 0);
		}, {
			window.decorator.shift(-64, 0);
		});
		// text
		textView = TextField(window, stringWidth  @ dimensions.y);
		textView.action = {
			string = textView.string;
			setStringFunc.value(textView.string);
		};
		this.adjustVisibility;
	}
	adjustVisibility {
		if (argTypeVal == 0, {
			numberView.visible_(false);
			textView.visible_(true);
		}, {
			numberView.visible_(true);
			textView.visible_(false);
		});
	}
	valueAll_ { arg arrVals;
		popupMenuView.value = argTypeVal = arrVals.at(0);
		numberView.value = numValue = controlSpec.constrain(arrVals.at(1));
		textView.string = string = arrVals.at(2);
		this.adjustVisibility;
	}
	valueAll{ arg argType, argNum, argString;
		^[argTypeVal, numValue, string];
	}
}



