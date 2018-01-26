// Copyright (C) 2008  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXNumber {
	var <>labelView, <>numberView, <>controlSpec, <>action, <value;
	var <>round = 0.001;

	*new { arg window, dimensions, label, controlSpec, action, initVal,
			initAction=false, labelWidth=80, numberWidth = 80;
		^super.new.init(window, dimensions, label, controlSpec, action, initVal,
			initAction, labelWidth, numberWidth);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initVal,
			initAction, labelWidth, numberWidth;

		if (labelWidth > 0, {
			labelView = StaticText.new(window, labelWidth @ dimensions.y);
			labelView.string = label;
			labelView.align = \right;
		});

		controlSpec = argControlSpec.asSpec;
		initVal = initVal ? controlSpec.default;
		action = argAction;

		numberView = NumberBox.new(window, numberWidth @ dimensions.y, controlSpec).maxDecimals_(4);
		numberView.action = {
			this.valueAction_(numberView.value);
		};

		if (initAction) {
			this.valueAction = initVal;
		}{
			this.value = initVal;
		};
	}
	value_ { arg val;
		value = controlSpec.constrain(val);
		numberView.value = value.round(round);
	}
	valueAction_ { arg val;
		this.value_(val);
		this.doAction;
	}
	doAction { action.value(this) }

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

	visible { ^numberView.visible }
	visible_ { |bool| [labelView, numberView].do(_.visible_(bool)) }

	stringColor { ^numberView.stringColor }
	stringColor_ { |color|
		numberView.stringColor_(color);
		numberView.normalColor_(color);
		numberView.typingColor_(color);
	}

	typingColor { ^numberView.typingColor }
	typingColor_ { |color|  numberView.typingColor_(color)  }

	normalColor { ^numberView.normalColor }
	normalColor_ { |color|  numberView.normalColor_(color)  }

	enabled {  ^numberView.enabled }
	enabled_ { |bool| numberView.enabled_(bool) }

	remove { [labelView, numberView].do(_.remove) }

	hasFocus {
		^numberView.hasFocus;
	}
}
