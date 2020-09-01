// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXEnvPlot {
	var <>multiSliderView, arrLevels, arrTimes, arrCurves;

	*new { arg window, dimensions, label, initVals,
			labelWidth=80, textWidth;
		^super.new.init(window, dimensions, label, initVals,
			labelWidth, textWidth);
	}
	init { arg window, dimensions, label, initVals,
			labelWidth, textWidth;
		var spacingX, defaultVals;
		if (window.class == Window, {
			spacingX = window.view.decorator.gap.x;
		}, {
			spacingX = window.decorator.gap.x;
		});
		multiSliderView = MultiSliderView(window, dimensions.x @ dimensions.y);
		multiSliderView.thumbSize = 1;
		multiSliderView.elasticMode = 1;
		multiSliderView.drawLines = true;
		multiSliderView.drawRects = false;
		multiSliderView.isFilled = true;
		multiSliderView.fillColor = Color.grey(0.85);
		multiSliderView.readOnly = true;
		defaultVals = [[0, 1.0, 0.0], [0.5, 0.5]];
		initVals = initVals ? defaultVals;
		this.set_(initVals);
	}
	value_ { arg argArray; // same as set
		this.set_(argArray);
	}
	set_ { arg argArray;
		arrLevels = argArray[0];
		arrTimes = argArray[1];
		arrCurves = argArray[2];
		this.updateViewFromVals;
	}
	updateViewFromVals {
		var env;
		env = Env(arrLevels, arrTimes, arrCurves ? 'lin', nil);
		multiSliderView.value = env.discretize(1024);
	}
	isClosed { ^multiSliderView.isClosed }
	notClosed { ^multiSliderView.notClosed }
	setEditable { } // dummy method, not used in this class
}




