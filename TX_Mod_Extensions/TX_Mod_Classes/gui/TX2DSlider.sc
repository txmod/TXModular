// Copyright (C) 2009  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TX2DSlider {	// an adapted version of TXDoubleSlider
	var <>labelView, labelView2, labelView3, <>hold2DSlider, <>numberView1, <>numberView2;
	var <controlSpec, <>action, <valX, <valY, <>round = 0.0001;

	*new { arg window, dimensions, label, controlSpec, action, initXVal, initYVal,
			initAction=false, labelWidth=80, numberWidth = 120;
		^super.new.init(window, dimensions, label, controlSpec, action, initXVal, initYVal,
			initAction, labelWidth, numberWidth);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initXVal, initYVal,
			initAction, labelWidth, numberWidth;
		var height, width, sliderWidth, spacingX, spacingY;

		if (window.class == Window, {
			spacingX = window.view.decorator.gap.x;
			spacingY = window.view.decorator.gap.y;
		}, {
			spacingX = window.decorator.gap.x;
			spacingY = window.decorator.gap.y;
		});
		height = dimensions.y;
		sliderWidth = dimensions.x - labelWidth - spacingX;


		controlSpec = argControlSpec.asSpec;

		action = argAction;

		labelView = StaticText(window, labelWidth @ 20);
		labelView.string = label;
		labelView.align = \right;

		hold2DSlider = Slider2D(window, sliderWidth @ height);
		hold2DSlider.action = {
			this.value = [controlSpec.map(hold2DSlider.x), controlSpec.map(hold2DSlider.y)];
			action.value(this);
		};

		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(spacingX.neg-dimensions.x, 30);
		}, {
			window.decorator.shift(spacingX.neg-dimensions.x, 30);
		});

		labelView2 = StaticText(window, labelWidth @ 20);
		labelView2.string = "X value";
		labelView2.align = \center;

		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(spacingX.neg-labelWidth, 24);
		}, {
			window.decorator.shift(spacingX.neg-labelWidth, 24);
		});

		numberView1 = TXScrollNumBox(window, numberWidth.asInteger @ 20, controlSpec).maxDecimals_(4);
		numberView1.action = {
			numberView1.value = controlSpec.constrain(numberView1.value).round(round);
			valX = numberView1.value;
			this.update2DSlider;
			action.value(this);
		};
		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(spacingX.neg-numberWidth, 30);
		}, {
			window.decorator.shift(spacingX.neg-numberWidth, 30);
		});


		labelView3 = StaticText(window, labelWidth @ 20);
		labelView3.string = "Y value";
		labelView3.align = \center;

		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(spacingX.neg-labelWidth, 24);
		}, {
			window.decorator.shift(spacingX.neg-labelWidth, 24);
		});

		numberView2 = TXScrollNumBox(window, numberWidth.asInteger @ 20, controlSpec).maxDecimals_(4);
		numberView2.action = {
			numberView2.value = controlSpec.constrain(numberView2.value).round(round);
			this.update2DSlider;
			valY = numberView2.value;
			action.value(this);
		};

		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(spacingX.neg-numberWidth, (height-144).neg);
		}, {
			window.decorator.shift(spacingX.neg-numberWidth, (height-144).neg);
		});

		numberView1.value = controlSpec.constrain(initXVal).round(round);
		numberView2.value = controlSpec.constrain(initYVal).round(round);
		valX = numberView1.value;
		valY = numberView2.value;
		this.update2DSlider;
		if (initAction.notNil) {
			action.value(this);
		};
	}
	update2DSlider {
		hold2DSlider.setXY(valX, valY);
	}
	value {
		^[valX, valY];
	}
	value_ { arg valueArray;
			numberView1.value = controlSpec.map(valueArray.at(0));
			numberView2.value = controlSpec.map(valueArray.at(1));
			valX = numberView1.value;
			valY = numberView2.value;
	}
	valueAction_ { arg valueArray;
		valX = controlSpec.constrain(valueArray.at(0));
		numberView1.valueAction = valX.round(round);
		valY = controlSpec.constrain(valueArray.at(1));
		numberView2.valueAction = valY.round(round);
	}

	controlSpec_ {arg argSpec;
		controlSpec = argSpec;
		TXScrollNumBox.updateNumberBoxFromSpec(numberView1, argSpec);
		TXScrollNumBox.updateNumberBoxFromSpec(numberView2, argSpec);
	}

	set { arg label, spec, argAction, initXVal, initYVal, initAction=false;
		labelView.string = label;
		controlSpec = spec.asSpec;
		initXVal =  initXVal;
		initYVal =  initYVal;

		action = argAction;

		numberView1.value = controlSpec.constrain(initXVal).round(round);
		numberView2.value = controlSpec.constrain(initYVal).round(round);
		valX = numberView1.value;
		valY = numberView2.value;
		this.update2DSlider;
		if (initAction) {
			action.value(this);
		};
	}
}
