// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMinMaxFreqNoteSldr {
	var <>labelView, labelView2, <>sliderView, <>numberView, <>notePopup, <>rangeView, <>minNumberView, <>maxNumberView, <>presetPopupView;
	var <>controlSpec, controlSpec2, <>action, viewValue, <>round = 0.001;

	// controlSpec2 is only used internally and it's min & max are decided by minNumberView & maxNumberView

	*new { arg window, dimensions, label, controlSpec, action, initVal,
		initAction=false, labelWidth=80, numberWidth = 120, arrRangePresets;
		^super.new.init(window, dimensions, label, controlSpec, action, initVal,
			initAction, labelWidth, numberWidth, arrRangePresets);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initVal,
		initAction, labelWidth, numberWidth, arrRangePresets;
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
		sliderView.action = {
			viewValue = controlSpec2.map(sliderView.value);
			numberView.value = viewValue.round(round);
			action.value(this);
		};
		if (controlSpec2.step != 0) {
			sliderView.step = (controlSpec2.step / (controlSpec2.maxval - controlSpec2.minval));
		};

		numberView = TXScrollNumBox(window, ((numberWidth - spacingX)/2).asInteger @ height);
		numberView.action = {
			numberView.value = viewValue = controlSpec.constrain(numberView.value);
			if (numberView.value < minNumberView.value, {
				controlSpec2.minval = numberView.value;
				minNumberView.value = numberView.value;
				TXScrollNumBox.updateNumberBoxFromSpec(numberView, controlSpec2);
			});
			if (numberView.value > maxNumberView.value, {
				controlSpec2.maxval = numberView.value;
				maxNumberView.value = numberView.value;
				TXScrollNumBox.updateNumberBoxFromSpec(numberView, controlSpec2);
			});
			rangeView.lo = controlSpec.unmap(minNumberView.value);
			rangeView.hi = controlSpec.unmap(maxNumberView.value);

			sliderView.value = controlSpec2.unmap(viewValue);
			action.value(this);
		};

		notePopup = PopUpMenu(window, ((numberWidth - spacingX)/2).asInteger @ height)
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		notePopup.items = ["notes"]
		++ 103.collect({arg item; TXGetMidiNoteString(item + 24);});
		notePopup.action = {
			if (notePopup.value > 0, {
				minNumberView.valueAction = 0.midicps;
				maxNumberView.valueAction = 127.midicps;
				numberView.valueAction = (notePopup.value + 23).midicps;
			});
		};

		// decorator next line
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
			minNumberView.value = controlSpec.map(rangeView.lo).round(round);
			controlSpec2.minval = minNumberView.value;
			maxNumberView.value = controlSpec.map(rangeView.hi).round(round);
			controlSpec2.maxval = maxNumberView.value;
			TXScrollNumBox.updateNumberBoxFromSpec(numberView, controlSpec2);			sliderView.doAction;
		};
		if (controlSpec.step != 0) {
			rangeView.step = (controlSpec.step / (controlSpec.maxval - controlSpec.minval));
		};
		rangeView.lo = controlSpec.minval;
		rangeView.hi = controlSpec.maxval;

		minNumberView = TXScrollNumBox(window, ((numberWidth - spacingX)/2).asInteger @ (height * 0.8),
			controlSpec);
		minNumberView.action = {
			minNumberView.value = controlSpec.constrain(minNumberView.value).round(round);
			rangeView.lo = controlSpec.unmap(minNumberView.value);
			controlSpec2.minval = minNumberView.value;
			TXScrollNumBox.updateNumberBoxFromSpec(numberView, controlSpec2);
			viewValue = controlSpec2.constrain(viewValue);
			sliderView.value = controlSpec2.unmap(viewValue);
			numberView.value = viewValue.round(round);
			action.value(this);
		};
		minNumberView.value = controlSpec.minval;

		maxNumberView = TXScrollNumBox(window, ((numberWidth - spacingX)/2).asInteger @ (height * 0.8),
			controlSpec);
		maxNumberView.action = {
			maxNumberView.value = controlSpec.constrain(maxNumberView.value).round(round);
			rangeView.hi = controlSpec.unmap(maxNumberView.value);
			controlSpec2.maxval = maxNumberView.value;
			TXScrollNumBox.updateNumberBoxFromSpec(numberView, controlSpec2);
			viewValue = controlSpec2.constrain(viewValue);
			sliderView.value = controlSpec2.unmap(viewValue);
			numberView.value = viewValue.round(round);
			action.value(this);
		};
		maxNumberView.value = controlSpec.maxval;

		if (arrRangePresets.notNil, {

			// decorator next line & shift
			if (window.class == Window, {
				window.view.decorator.nextLine;
				window.view.decorator.shift(labelWidth + spacingX, 0);
			}, {
				window.decorator.nextLine;
				window.decorator.shift(labelWidth + spacingX, 0);
			});

			presetPopupView = PopUpMenu(window, (dimensions.x - labelWidth - spacingX) @ (height * 0.8))
			.background_(TXColor.paleBlue2)
			.items_(arrRangePresets.collect({arg item, i; item.at(0);}))
			.action_({ arg view;
				var arrMinMax;
				arrMinMax = arrRangePresets.at(view.value).at(1);
				this.valueSplitAction = [this.valueSplit.at(0), arrMinMax.at(0),
					arrMinMax.at(1)];
				view.value = 0;
			});
		});

		if (initAction) {
			this.value = initVal;
		}{
			viewValue = initVal;
			sliderView.value = controlSpec2.unmap(viewValue);
			numberView.value = viewValue.round(round);
		};
	}

	value {
		^viewValue;
	}

	value_ { arg value;
		numberView.valueAction = value;
	}

	valueNoAction_  { arg value;
		numberView.value = value;
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
		TXScrollNumBox.updateNumberBoxFromSpec(numberView, controlSpec2);
		sliderView.value = valueArray.at(0) ? 0;
		viewValue = controlSpec2.map(sliderView.value);
		numberView.value = viewValue.round(round);
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
		TXScrollNumBox.updateNumberBoxFromSpec(numberView, controlSpec2);

		action = argAction;

		if (initAction) {
			this.value = initVal;
		}{
			viewValue = initVal;
			sliderView.value = controlSpec2.unmap(viewValue);
			numberView.value = viewValue.round(round);
		};
	}
}
