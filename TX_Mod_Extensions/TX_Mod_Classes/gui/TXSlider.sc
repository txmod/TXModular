// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSlider {
	var <>labelView, <>sliderView, <>numberView, <>controlSpec, <>action, <value;
	var <>round = 0.001;

	*new { arg window, dimensions, label, controlSpec, action, initVal,
			initAction=false, labelWidth=80, numberWidth = 80;
		^super.new.init(window, dimensions, label, controlSpec, action, initVal,
			initAction, labelWidth, numberWidth);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initVal,
			initAction, labelWidth, numberWidth;
		var	decorator = window.asView.tryPerform(\decorator),
			gap = decorator.tryPerform(\gap);

		gap.notNil.if({
			(dimensions = dimensions.copy).x_(dimensions.x - (2*gap.x));
		});

		controlSpec = argControlSpec.asSpec;
		initVal = initVal ? controlSpec.default;
		action = argAction;

		labelView = StaticText.new(window, labelWidth @ dimensions.y);

		sliderView = Slider.new(window, (dimensions.x - labelWidth - numberWidth) @ dimensions.y);
		sliderView.thumbSize_(8).knobColor_(TXColour.white);

		numberView = TXScrollNumBox(window, numberWidth @ dimensions.y, controlSpec).maxDecimals_(4);
		labelView.string = label;
		labelView.align = \right;

		sliderView.action = {
			this.valueAction_(controlSpec.map(sliderView.value));
		};
		if (controlSpec.step != 0) {
			sliderView.step = (controlSpec.step / (controlSpec.maxval - controlSpec.minval));
		};

		sliderView.receiveDragHandler = { arg slider;
			slider.valueAction = controlSpec.unmap(View.currentDrag);
		};

		sliderView.beginDragAction = { arg slider;
			controlSpec.map(slider.value)
		};

		numberView.action = { this.valueAction_(numberView.value) };

		if (initAction) {
			this.valueAction_(initVal);
		}{
			this.value_(initVal);
		};
	}

	value_ { arg val;
		value = controlSpec.constrain(val);
		numberView.value = value.round(round);
		sliderView.value = controlSpec.unmap(value);
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
			sliderView.value = controlSpec.unmap(value);
			numberView.value = value.round(round);
		};
	}

	visible { ^sliderView.visible }
	visible_ { |bool| [labelView, sliderView, numberView].do(_.visible_(bool)) }

	enabled {  ^sliderView.enabled }
	enabled_ { |bool| [sliderView, numberView].do(_.enabled_(bool)) }

	remove { [labelView, sliderView, numberView].do(_.remove) }

	hasFocus {
		^sliderView.hasFocus || numberView.hasFocus;
	}
}



