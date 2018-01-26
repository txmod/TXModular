// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMixerPan {
	var <>labelView, <>sliderView, <>numberView, <>controlSpec, <>action, <value;
	var <>round = 0.001;

	*new { arg window, dimensions, label, controlSpec, action, initVal,
			initAction=false;
		^super.new.init(window, dimensions, label, controlSpec, action, initVal,
			initAction);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initVal,
			initAction;
		var guiBox;

		// prepare to display header
		guiBox = CompositeView(window, Rect(0,0, dimensions.x, dimensions.y));
		guiBox.decorator = FlowLayout(guiBox.bounds);
		guiBox.decorator.gap = Point(0,4);

		controlSpec = argControlSpec.asSpec;
		initVal = initVal ? controlSpec.default;
		action = argAction;

		labelView = StaticText.new(guiBox, dimensions.x @ 20);
		guiBox.decorator.nextLine;
		sliderView = Slider.new(guiBox, dimensions.x @ 20);
		sliderView.thumbSize_(8).knobColor_(TXColour.white);
		guiBox.decorator.nextLine;
		numberView = TXScrollNumBox(guiBox, dimensions.x @ 20, controlSpec);
		labelView.string = label;
		labelView.align = \center;

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

	hasFocus {
		^sliderView.hasFocus || numberView.hasFocus;
	}

}
