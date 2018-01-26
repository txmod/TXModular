// Copyright (C) 2009  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TX2DSlider {	// an adapted version of TXDoubleSlider
	var <>labelView, <>labelView2, <>labelView3, <>hold2DSlider, <>numberView1, <>numberView2;
	var <controlSpec, <>action, <valX, <valY, <>round = 0.0001;
	var <>xSlider, <>ySlider; // these are optional and may be nil

	*new { arg window, dimensions, label, controlSpec, action, initXVal, initYVal,
		initAction=false, labelWidth=80, numberWidth = 120, showXYSliders = false;
		^super.new.init(window, dimensions, label, controlSpec, action, initXVal, initYVal,
			initAction, labelWidth, numberWidth, showXYSliders);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initXVal, initYVal,
		initAction, labelWidth, numberWidth, showXYSliders;
		var startPosLeft, spacingX, spacingY, slider2DHeight;
		var slider2DWidth, sliderXHeight, sliderYWidth;

		if (window.class == Window, {
			spacingX = window.view.decorator.gap.x;
			spacingY = window.view.decorator.gap.y;
		}, {
			spacingX = window.decorator.gap.x;
			spacingY = window.decorator.gap.y;
		});
		if (showXYSliders == true, {
			sliderXHeight = 12;
			sliderYWidth = 12;
			slider2DHeight = dimensions.y - sliderXHeight - spacingY;
			slider2DWidth = dimensions.x - labelWidth - spacingX - sliderYWidth - spacingX;
		}, {
			sliderXHeight = 0;
			sliderYWidth = 0;
			slider2DHeight = dimensions.y;
			slider2DWidth = dimensions.x - labelWidth - spacingX;
		});


		controlSpec = argControlSpec.asSpec;

		action = argAction;

		labelView = StaticText(window, labelWidth @ 20);
		labelView.string = label;
		labelView.align = \right;

		startPosLeft = window.asView.decorator.left;

		hold2DSlider = Slider2D(window, slider2DWidth @ slider2DHeight);
		hold2DSlider.knobColor_(Color.white).background_(TXColour.sysGuiCol1);
		hold2DSlider.action = {
			this.value = [controlSpec.map(hold2DSlider.x), controlSpec.map(hold2DSlider.y)];
			action.value(this);
		};
		if (showXYSliders == true, {
			ySlider = Slider(window, sliderYWidth @ slider2DHeight);
			ySlider.action = {arg view;
				valY = controlSpec.map(view.value);
				this.update2DSlider;
				this.updateNumberBoxes;
				action.value(this);
			};
			ySlider.thumbSize_(16).knobColor_(Color.white).background_(TXColour.sysGuiCol1);
		});

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
			this.updateXYSliders;
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
			valY = numberView2.value;
			this.update2DSlider;
			this.updateXYSliders;
			action.value(this);
		};

		// decorator shift
		if (window.class == Window, {
			window.view.decorator.shift(spacingX.neg-numberWidth, (slider2DHeight-124).neg);
		}, {
			window.decorator.shift(spacingX.neg-numberWidth, (slider2DHeight-124).neg);
		});

		if (showXYSliders == true, {
			if (window.class == Window, {
				window.view.decorator.nextLine.shift(startPosLeft - spacingX);
			}, {
				window.decorator.nextLine.shift(startPosLeft - spacingX);
			});
			xSlider = Slider(window, slider2DWidth @ sliderXHeight);
			xSlider.action = {arg view;
				valX = controlSpec.map(view.value);
				this.update2DSlider;
				this.updateNumberBoxes;
				action.value(this);
			};
			xSlider.thumbSize_(16).knobColor_(Color.white).background_(TXColour.sysGuiCol1);
		});

		numberView1.value = controlSpec.constrain(initXVal).round(round);
		numberView2.value = controlSpec.constrain(initYVal).round(round);
		valX = numberView1.value;
		valY = numberView2.value;
		this.update2DSlider;
		this.updateXYSliders;
		if (initAction.notNil) {
			action.value(this);
		};
	}
	update2DSlider {
		hold2DSlider.setXY(valX, valY);
	}
	updateXYSliders {
		if (xSlider.notNil, {
			xSlider.value = valX;
			ySlider.value = valY;
		});
	}
	updateNumberBoxes {
		numberView1.value = valX.round(round);
		numberView2.value = valY.round(round);
	}
	value {
		^[valX, valY];
	}
	value_ { arg valueArray;
		numberView1.value = controlSpec.map(valueArray.at(0));
		numberView2.value = controlSpec.map(valueArray.at(1));
		valX = numberView1.value;
		valY = numberView2.value;
		this.updateXYSliders;
	}
	valueAction_ { arg valueArray;
		valX = controlSpec.constrain(valueArray.at(0));
		numberView1.valueAction = valX.round(round);
		valY = controlSpec.constrain(valueArray.at(1));
		numberView2.valueAction = valY.round(round);
		this.updateXYSliders;
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
		this.updateXYSliders;
		if (initAction) {
			action.value(this);
		};
	}

	hasFocus {
		^hold2DSlider.hasFocus || numberView1.hasFocus || numberView2.hasFocus;
	}
}
