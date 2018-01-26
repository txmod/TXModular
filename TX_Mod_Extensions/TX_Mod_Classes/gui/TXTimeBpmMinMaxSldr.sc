// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXTimeBpmMinMaxSldr {
	var <>labelView, <>labelView2, <>sliderView, <>numberView, <>bpmNumberView, <>rangeView, <>minNumberView, <>maxNumberView;
	var <>controlSpec, controlSpec2, <>action, viewValue, <>round = 0.0001;

	// controlSpec2 is only used internally and it's min & max are decided by minNumberView & maxNumberView

	*new { arg window, dimensions, label, controlSpec, action, initVal,
			initAction=false, labelWidth=80, numberWidth = 120;
		^super.new.init(window, dimensions, label, controlSpec, action, initVal,
			initAction, labelWidth, numberWidth);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initVal,
			initAction, labelWidth, numberWidth;
		var height, spacingX, spacingY;

		if (window.class == Window, {
			spacingX = window.view.decorator.gap.x;
			spacingY = window.view.decorator.gap.y;
		}, {
			spacingX = window.decorator.gap.x;
			spacingY = window.decorator.gap.y;
		});
		height = ( (dimensions.y - spacingY) / 2).asInteger;

		labelView = StaticText(window, labelWidth @ height);
		labelView.string = label;
		labelView.align = \right;

		controlSpec = argControlSpec.asSpec;
		initVal = initVal ? controlSpec.default;
		controlSpec2 = controlSpec.deepCopy.asSpec;

		action = argAction;

		sliderView = Slider(window, (dimensions.x - labelWidth - numberWidth - (2 * spacingX)) @ height);
		sliderView.thumbSize_(8).knobColor_(Color.white);
		sliderView.action = {
			viewValue = controlSpec2.map(sliderView.value);
			numberView.value = viewValue.round(round);
			bpmNumberView.value = 60000 / viewValue;
			action.value(this);
		};
		if (controlSpec2.step != 0) {
			sliderView.step = (controlSpec2.step / (controlSpec2.maxval - controlSpec2.minval));
		};

		numberView = TXScrollNumBox(window, ((numberWidth - spacingX)/2).asInteger @ height,
			controlSpec2).maxDecimals_(4);
		numberView.action = {
			numberView.value = viewValue = controlSpec.constrain(numberView.value);
			bpmNumberView.value = 60000 / viewValue;
			if (numberView.value < minNumberView.value, {
				controlSpec2.minval = numberView.value;
				minNumberView.value = numberView.value;
			});
			if (numberView.value > maxNumberView.value, {
				controlSpec2.maxval = numberView.value;
				maxNumberView.value = numberView.value;
			});
			rangeView.lo = controlSpec.unmap(minNumberView.value);
			rangeView.hi = controlSpec.unmap(maxNumberView.value);

			sliderView.value = controlSpec2.unmap(viewValue);
			action.value(this);
		};

		bpmNumberView = TXScrollNumBox(window, ((numberWidth - spacingX)/2).asInteger @ height).maxDecimals_(4);
		bpmNumberView.action = {
			bpmNumberView.value = 60000 / controlSpec2.constrain(60000 / bpmNumberView.value);
			numberView.valueAction = 60000 / bpmNumberView.value;
		};

		// decorator next line & shift
			if (window.class == Window, {
				window.view.decorator.nextLine;
			}, {
				window.decorator.nextLine;
			});

		labelView2 = StaticText(window, labelWidth @ height);
		labelView2.string = "Min - Max";
		labelView2.align = \right;

		rangeView = RangeSlider(window,
			(dimensions.x - labelWidth - numberWidth - (2 * spacingX)) @ (height * 0.8) );
		rangeView.action = {
			minNumberView.value = controlSpec.map(rangeView.lo);
			controlSpec2.minval = minNumberView.value;
			maxNumberView.value = controlSpec.map(rangeView.hi);
			controlSpec2.maxval = maxNumberView.value;
			sliderView.valueAction = controlSpec2.unmap(viewValue);
		};
		if (controlSpec.step != 0) {
			rangeView.step = (controlSpec.step / (controlSpec.maxval - controlSpec.minval));
		};
		rangeView.lo = controlSpec.minval;
		rangeView.hi = controlSpec.maxval;

		minNumberView = TXScrollNumBox(window,
			((numberWidth - spacingX)/2).asInteger @ (height * 0.8),
			controlSpec).maxDecimals_(4);
		minNumberView.action = {
			minNumberView.value = controlSpec.constrain(minNumberView.value).round(round);
			rangeView.lo = controlSpec.unmap(minNumberView.value);
			controlSpec2.minval = minNumberView.value;
			viewValue = controlSpec2.constrain(viewValue);
			sliderView.value = controlSpec2.unmap(viewValue);
			numberView.value = viewValue.round(round);
			bpmNumberView.value = 60000 / viewValue;
			action.value(this);
		};
		minNumberView.value = controlSpec.minval;

		maxNumberView = TXScrollNumBox(window,
			((numberWidth - spacingX)/2).asInteger @ (height * 0.8),
			controlSpec).maxDecimals_(4);
		maxNumberView.action = {
			maxNumberView.value = controlSpec.constrain(maxNumberView.value).round(round);
			rangeView.hi = controlSpec.unmap(maxNumberView.value);
			controlSpec2.maxval = maxNumberView.value;
			viewValue = controlSpec2.constrain(viewValue);
			sliderView.value = controlSpec2.unmap(viewValue);
			numberView.value = viewValue.round(round);
			bpmNumberView.value = 60000 / viewValue;
			action.value(this);
		};
		maxNumberView.value = controlSpec.maxval;

		if (initAction) {
			this.value = initVal;
		}{
			viewValue = initVal;
			sliderView.value = controlSpec2.unmap(viewValue);
			numberView.value = viewValue.round(round);
			bpmNumberView.value = 60000 / viewValue;
		};
	}

	value {
		^viewValue;
	}

	value_ { arg value;
		numberView.valueAction = value;
	}

	valueSplit {
		^[sliderView.value, minNumberView.value, maxNumberView.value];
	}

	valueSplit_ { arg valueArray;
		minNumberView.value = valueArray.at(1) ? 0;
		minNumberView.value = controlSpec.constrain(minNumberView.value).round(round);
		rangeView.lo = controlSpec.unmap(minNumberView.value);
		controlSpec2.minval = minNumberView.value;
		maxNumberView.value = valueArray.at(2) ? 0;
		maxNumberView.value = controlSpec.constrain(maxNumberView.value).round(round);
		rangeView.hi = controlSpec.unmap(maxNumberView.value);
		controlSpec2.maxval = maxNumberView.value;
		sliderView.value = valueArray.at(0) ? 0;
		viewValue = controlSpec2.map(sliderView.value);
		numberView.value = viewValue.round(round);
		bpmNumberView.value = 60000 / viewValue;
	}

	valueSplitAction_ { arg valueArray;
		this.valueSplit_(valueArray);
		action.value(this);
	}


	set { arg label, spec, argAction, initVal, initMinVal, initMaxVal, initAction=false;
		labelView.string = label;
		controlSpec = spec.asSpec;
		initVal = initVal ? controlSpec.default;
		initMinVal =  initMinVal ? controlSpec.minval;
		initMaxVal =  initMaxVal ? controlSpec.maxval;
		controlSpec2 = controlSpec.deepCopy.asSpec;
		controlSpec2.minval = initMinVal;
		controlSpec2.maxval = initMaxVal;

		action = argAction;

		if (initAction) {
			this.value = initVal;
		}{
			viewValue = initVal;
			sliderView.value = controlSpec2.unmap(viewValue);
			numberView.value = viewValue.round(round);
			bpmNumberView.value = 60000 / viewValue;
		};
	}

	hasFocus {
		^sliderView.hasFocus || numberView.hasFocus || bpmNumberView.hasFocus || rangeView.hasFocus
		|| minNumberView.hasFocus || maxNumberView.hasFocus;
	}
}
