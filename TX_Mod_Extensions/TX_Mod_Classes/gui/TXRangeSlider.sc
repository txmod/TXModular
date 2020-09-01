// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXRangeSlider {
	var <>labelView, <>rangeView, <>minNumberView, <>maxNumberView, <>presetPopupView;
	var <>controlSpec, <>action, <lo, <hi, <>round = 0.0001;

	*new { arg window, dimensions, label, controlSpec, action, initMinVal, initMaxVal,
			initAction=false, labelWidth=80, numberWidth = 120, arrPresets, boolScroll=true;
		^super.new.init(window, dimensions, label, controlSpec, action, initMinVal, initMaxVal,
			initAction, labelWidth, numberWidth, arrPresets, boolScroll);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initMinVal, initMaxVal,
			initAction, labelWidth, numberWidth, arrPresets, boolScroll;
		var height, spacingX;

		if (window.class == Window, {
			spacingX = window.view.decorator.gap.x;
		}, {
			spacingX = window.decorator.gap.x;
		});

		height = dimensions.y;

		controlSpec = argControlSpec.asSpec;
		initMinVal = initMinVal ? controlSpec.minval;
		initMaxVal = initMaxVal ? controlSpec.maxval;

		action = argAction;

		labelView = StaticText(window, labelWidth @ height);
		labelView.string = label;
		labelView.align = \right;

		rangeView = RangeSlider(window, (dimensions.x - labelWidth - numberWidth - (2 * spacingX)) @ height );
		rangeView.background_(TXColor.sysGuiCol1).knobColor_(TXColor.grey(0.75));
		rangeView.action = { arg view;
			minNumberView.value = controlSpec.map(rangeView.lo);
			maxNumberView.value = controlSpec.map(rangeView.hi);
			lo = minNumberView.value;
			hi = maxNumberView.value;
			action.value(this);
		};

		minNumberView = TXScrollNumBox(window, ((numberWidth - spacingX)/2).asInteger @ height, controlSpec).maxDecimals_(4);
		if (boolScroll==false, {minNumberView.scroll = false});
		minNumberView.action = {
			minNumberView.value = controlSpec.constrain(minNumberView.value).round(round);
			lo = minNumberView.value;
			rangeView.lo = controlSpec.unmap(minNumberView.value);
			action.value(this);
		};

		maxNumberView = TXScrollNumBox(window, ((numberWidth - spacingX)/2).asInteger @ height, controlSpec).maxDecimals_(4);
		if (boolScroll==false, {maxNumberView.scroll = false});
		maxNumberView.action = {
			maxNumberView.value = controlSpec.constrain(maxNumberView.value).round(round);
			rangeView.hi = controlSpec.unmap(maxNumberView.value);
			hi = maxNumberView.value;
			action.value(this);
		};

		if (controlSpec.step != 0) {
			rangeView.step = (controlSpec.step / (controlSpec.maxval - controlSpec.minval));
		};

		if (arrPresets.notNil, {

			// decorator next line & shift
			if (window.class == Window, {
				window.view.decorator.nextLine;
				window.view.decorator.shift(labelWidth + spacingX, 0);
			}, {
				window.decorator.nextLine;
				window.decorator.shift(labelWidth + spacingX, 0);
			});

			presetPopupView = PopUpMenu(window, (dimensions.x - labelWidth - spacingX) @ (height * 0.8))
						.background_(Color.white)
						.items_(arrPresets.collect({arg item, i; item.at(0);}))
						.action_({ arg view;
							this.valueBoth = arrPresets.at(view.value).at(1);
							view.value = 0;
						});
		});

		minNumberView.value = controlSpec.constrain(initMinVal).round(round);
		rangeView.lo = controlSpec.unmap(initMinVal);
		maxNumberView.value = controlSpec.constrain(initMaxVal).round(round);
		rangeView.hi = controlSpec.unmap(initMaxVal);
		lo = minNumberView.value;
		hi = maxNumberView.value;
		if (initAction) {
			action.value(this);
		};
	}

	makeRangePositive {
		var holdMin, holdMaX;
		if (this.range < 0, {
			holdMin = this.minVal;
			holdMaX = this.maxVal;
			this.valueBothNoAction_([holdMin, holdMaX]);
		});
	}

	value {
		^lo;
	}

	valueBoth {
		^[lo, hi];
	}

	minVal {
		^min(lo, hi);
	}

	maxVal {
		^max(lo, hi);
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
			hi = controlSpec.constrain(valueArray.at(1));
			minNumberView.value = lo;
			maxNumberView.value = hi;
			rangeView.lo = controlSpec.unmap(lo);
			rangeView.hi = controlSpec.unmap(hi);
			action.value(this);
	}

	valueBothNoAction_  { arg valueArray;
			lo = controlSpec.constrain(valueArray.at(0));
			hi = controlSpec.constrain(valueArray.at(1));
			minNumberView.value = lo;
			maxNumberView.value = hi;
			rangeView.lo = controlSpec.unmap(lo);
			rangeView.hi = controlSpec.unmap(hi);
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
		rangeView.lo = controlSpec.unmap(initMinVal);
		maxNumberView.value = controlSpec.constrain(initMaxVal).round(round);
		rangeView.hi = controlSpec.unmap(initMaxVal);
		if (initAction) {
			action.value(this);
		};
	}

	hasFocus {
		^rangeView.hasFocus || minNumberView.hasFocus || maxNumberView.hasFocus;
	}
}



