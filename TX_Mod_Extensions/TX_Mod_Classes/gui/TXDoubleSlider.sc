// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXDoubleSlider {	// an adapted version of TXRangeSlider which has 2 sliders to select range - so easier to select reversed ranges
	var <>labelView, <>sliderView1, <>sliderView2, <>minNumberView, <>maxNumberView;
	var <controlSpec, <>action, <lo, <hi, <>round = 0.0001;

	*new { arg window, dimensions, label, controlSpec, action, initMinVal, initMaxVal,
			initAction=false, labelWidth=80, numberWidth = 120;
		^super.new.init(window, dimensions, label, controlSpec, action, initMinVal, initMaxVal,
			initAction, labelWidth, numberWidth);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initMinVal, initMaxVal,
			initAction, labelWidth, numberWidth;
		var height, spacingX, spacingY;

		if (window.class == Window, {
			spacingX = window.view.decorator.gap.x;
			spacingY = window.view.decorator.gap.y;
		}, {
			spacingX = window.decorator.gap.x;
			spacingY = window.decorator.gap.y;
		});
		height = dimensions.y;

		controlSpec = argControlSpec.asSpec;
		initMinVal = initMinVal ? controlSpec.minval;
		initMaxVal = initMaxVal ? controlSpec.maxval;

		action = argAction;

		labelView = StaticText(window, labelWidth @ height);
		labelView.string = label;
		labelView.align = \right;

		sliderView1 = Slider(window, (dimensions.x - labelWidth - numberWidth) @ (height-spacingY/2));
		sliderView1.action = {
			minNumberView.value = controlSpec.map(sliderView1.value);
			lo = minNumberView.value;
			action.value(this);
		};
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(-4 - (dimensions.x - labelWidth - numberWidth), (height-spacingY/2) + spacingY);
		}, {
			window.decorator.shift(-4 - (dimensions.x - labelWidth - numberWidth), (height-spacingY/2) + spacingY);
		});
		sliderView2 = Slider(window, (dimensions.x - labelWidth - numberWidth) @ (height-spacingY/2));
		sliderView2.action = {
			maxNumberView.value = controlSpec.map(sliderView2.value);
			hi = maxNumberView.value;
			action.value(this);
		};
		if (controlSpec.step != 0) {
			sliderView1.step = (controlSpec.step / (controlSpec.maxval - controlSpec.minval));
			sliderView2.step = sliderView1.step;
		};
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(0, 0 - (height-spacingY/2) - spacingY);
		}, {
			window.decorator.shift(0, 0 - (height-spacingY/2) - spacingY);
		});

		minNumberView = TXScrollNumBox(window, ((numberWidth/2) - spacingX).asInteger @ height, controlSpec).maxDecimals_(4);
		minNumberView.action = {
			minNumberView.value = controlSpec.constrain(minNumberView.value).round(round);
			lo = minNumberView.value;
			sliderView1.value = controlSpec.unmap(minNumberView.value);
			action.value(this);
		};

		maxNumberView = TXScrollNumBox(window, ((numberWidth-spacingX)/2).asInteger @ height, controlSpec).maxDecimals_(4);
		maxNumberView.action = {
			maxNumberView.value = controlSpec.constrain(maxNumberView.value).round(round);
			sliderView2.value = controlSpec.unmap(maxNumberView.value);
			hi = maxNumberView.value;
			action.value(this);
		};

		minNumberView.value = controlSpec.constrain(initMinVal).round(round);
		sliderView1.value = controlSpec.unmap(initMinVal);
		maxNumberView.value = controlSpec.constrain(initMaxVal).round(round);
		sliderView2.value = controlSpec.unmap(initMaxVal);
		lo = minNumberView.value;
		hi = maxNumberView.value;
		if (initAction) {
			action.value(this);
		};
	}

	value {
		^lo;
	}

	valueBoth {
		^[lo, hi];
	}

	range {
		^hi - lo;
	}

	value_ { arg value;
		lo = controlSpec.constrain(value);
		minNumberView.valueAction = lo.round(round);
	}

	valueBoth_ { arg valueArray;
		lo = controlSpec.constrain(valueArray.at(0));
		minNumberView.valueAction = lo.round(round);
		hi = controlSpec.constrain(valueArray.at(1));
		maxNumberView.valueAction = hi.round(round);
	}

	valueBothNoAction_  { arg valueArray;
			minNumberView.value = controlSpec.map(valueArray.at(0));
			maxNumberView.value = controlSpec.map(valueArray.at(1));
			lo = minNumberView.value;
			hi = maxNumberView.value;
	}
	controlSpec_ {arg argSpec;
		controlSpec = argSpec;
		TXScrollNumBox.updateNumberBoxFromSpec(minNumberView, argSpec);
		TXScrollNumBox.updateNumberBoxFromSpec(maxNumberView, argSpec);
	}

	lo_ {arg value;
		lo = controlSpec.constrain(value);
		minNumberView.valueAction = lo.round(round);
	}

	hi_ {  arg value;
		hi = controlSpec.constrain(value);
		maxNumberView.valueAction = hi.round(round);
	}

	range_ {arg value;
		hi = controlSpec.constrain(lo + value.abs);
		maxNumberView.valueAction = hi.round(round);
	}
	set { arg label, spec, argAction, initMinVal, initMaxVal, initAction=false;
		labelView.string = label;
		controlSpec = spec.asSpec;
		initMinVal =  initMinVal ? controlSpec.minval;
		initMaxVal =  initMaxVal ? controlSpec.maxval;

		action = argAction;

		minNumberView.value = controlSpec.constrain(initMinVal).round(round);
		sliderView1.value = controlSpec.unmap(initMinVal);
		maxNumberView.value = controlSpec.constrain(initMaxVal).round(round);
		sliderView2.value = controlSpec.unmap(initMaxVal);
		if (initAction) {
			action.value(this);
		};
	}
}



