// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXTextBox {
	var <>labelView, <>textView, <>action, <string;

	*new { arg window, dimensions, label, action, initVal,
			initAction=false, labelWidth=80, textWidth;
		^super.new.init(window, dimensions, label, action, initVal,
			initAction, labelWidth, textWidth);
	}
	init { arg window, dimensions, label, argAction, initVal,
			initAction, labelWidth, textWidth;
		if (labelWidth > 0, {
			labelView = StaticText(window, labelWidth @ dimensions.y);
			labelView.string = label;
			labelView.align = \right;
		});

		initVal = initVal ? " ";
		action = argAction;

		textWidth = textWidth ? (dimensions.x - labelWidth - 4);
		textView = TextField(window, textWidth @ dimensions.y);
		textView.action = {
			string = textView.string;
			action.value(this);
		};

		if (initAction) {
			this.string = initVal;
		}{
			string = initVal;
			textView.string = string;
		};
	}
	value {^string}
	value_ { arg argString; textView.string = string = argString;}
	valueAction_ { arg argString;
		textView.string = string = argString;
		action.value(this);
	}
	string_ { arg argString; textView.string = string = argString;}
}
