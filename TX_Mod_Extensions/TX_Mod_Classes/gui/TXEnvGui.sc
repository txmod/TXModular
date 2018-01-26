// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXEnvGui {
	var <>envView, <>stageLabelView, <>levelsView, <>timesView, <>stageTimeCtrlSpec, <>action, <value, <size;
	var plotView, arrCurves, grid;

	*new { arg window, dimensions, stageTimeCtrlSpec, labelStringColor, labelBackColor, action, initVal,
		initAction=false, labelWidth=80, stepWidth = 20, arrCurves, gridRows, gridCols;
		^super.new.init(window, dimensions, stageTimeCtrlSpec, labelStringColor, labelBackColor,
			action, initVal, initAction, labelWidth, stepWidth, arrCurves, gridRows, gridCols);
	}
	init { arg window, dimensions, argStageTimeCtrlSpec, labelStringColor, labelBackColor,
		argAction, initVal, initAction, labelWidth, stepWidth, argArrCurves, gridRows, gridCols;
		var holdLeft, holdTop;
		stageTimeCtrlSpec = (argStageTimeCtrlSpec ? [0,10]).asSpec;
		initVal = initVal ? [[0,1,1,0],[0,1,1,1]];
		action = argAction;
		// value consists of an array containing arrays of levels and times
		value = initVal;
		// size is derived from initVal size
		size = initVal.at(0).size;
		arrCurves = argArrCurves;
		grid = (gridCols ? 8).reciprocal @ (gridRows ? 8).reciprocal;

		holdLeft = window.asView.decorator.left;
		holdTop = window.asView.decorator.top;

		window.asView.decorator.shift(5, 5);
		// create view
		plotView = MultiSliderView(window, Rect(0, 0, dimensions.x - 10, dimensions.y - 10));
		plotView.thumbSize = 1;
		plotView.elasticMode = 1;
		plotView.drawLines = true;
		plotView.drawRects = false;
		plotView.isFilled = true;
		plotView.fillColor = Color.grey(0.6);
		plotView.background = Color.white;
		plotView.readOnly = true;

		window.asView.decorator.left = holdLeft;
		window.asView.decorator.top = holdTop;

		// create view
		envView = EnvelopeView(window, Rect(0, 0, dimensions.x, dimensions.y))
		.thumbSize_(14)
		.step_(0)
		.drawLines_(true)
		.drawRects_(true)
		.strokeColor_(Color.grey(0.4))
		.fillColor_(Color.white)
		.background_(Color.white.alpha_(0.05))
		.selectionColor_(Color.red)
		.gridOn_(true)
		.grid_(grid);
		envView.mouseUpAction = { arg view;
			var stageUpdated, arrViewLevels, arrViewTimes, arrTimes, adjustedTime;
			stageUpdated = view.index;
			// don't carry out action if nothing changed
			if (stageUpdated.isPositive, {
				arrViewLevels = view.value.at(1);
				arrViewTimes = view.value.at(0);
				// don't allow times of first and last stages to be altered
				arrViewTimes.put(0, 0);
				arrViewTimes.put(size-1, 1);
				// make sure no stage is before previous or after next
				if ((stageUpdated != 0) and: (stageUpdated != (size-1)), {
					adjustedTime = arrViewTimes.at(stageUpdated)
					.max(arrViewTimes.at(stageUpdated-1))
					.min(arrViewTimes.at(stageUpdated+1));
					arrViewTimes.put(stageUpdated, adjustedTime);
				});
				arrTimes =  arrViewTimes.collect({ arg item, i;
					if (i == 0, {
						item;
					},{
						item - arrViewTimes.at(i-1);
					});
				});
				value = [arrViewLevels, arrTimes * this.totalTime].asFloat;
				this.updateEnvView;
				action.value(this);
			});
		};
		// set env view values
		this.updateEnvView;
		// number each point
		size.do({arg i;
			envView.setString(i, (i + 1).asString);
		});
		// decorator next line
		window.asView.decorator.nextLine;
		// create view
		stageLabelView = TXMultiTextBox(window, dimensions.x @ 20,  "Stages", Array.series(size, 1),
			textWidth: stepWidth);
		stageLabelView.labelView.stringColor_(labelStringColor).background_(labelBackColor);
		// decorator next line
		window.asView.decorator.nextLine;
		// create view
		levelsView = TXMultiNumber(window, dimensions.x @ 20,  "Levels", ControlSpec(0, 100),
			{ arg view;
				value.put(0, view.value / 100);
				this.updateEnvView;
				action.value(this);
			},
			value.at(0) * 100,
			numberWidth: stepWidth,
			cloneButton: false
		);
		levelsView.labelView.stringColor_(labelStringColor).background_(labelBackColor);

		// decorator next line
		window.asView.decorator.nextLine;
		// create view
		timesView = TXMultiNumber(window, dimensions.x @ 20,  "Times", ControlSpec(0, 100),
			{ arg view;
				// force first time to be 0
				view.value.put(0, 0);
				value.put(1, view.value);
				this.updateEnvView;
				action.value(this);
			},
			value.at(1),
			numberWidth: stepWidth,
			cloneButton: false
		);
		timesView.labelView.stringColor_(labelStringColor).background_(labelBackColor);
		timesView.arrNumberViews[0].enabled_(false).background_(Color.grey(0.8)); // disable first time

		// check for initial action
		if (initAction) {
			action.value(this);
		};
	}
	value_ { arg argValue;
		value = argValue;
		this.updateEnvView;
	}
	valueAction_ { arg argValue;
		this.value_(argValue);
		action.value(this);
	}
	updateEnvView {
		var arrTimesNorm, arrTimesNormedSummed;
		arrTimesNorm = value.at(1).normalizeSum;
		size.do({ arg i;
			arrTimesNormedSummed = arrTimesNormedSummed.add(arrTimesNorm.copyRange(0, i).sum);
		});
		envView.value = [arrTimesNormedSummed, value.at(0)].asFloat;
		this.updatePlotView;
	}
	updatePlotView {
		var arrLevels, arrTimes, env;
		arrLevels = value.at(0).max(0.001);
		arrTimes = value.at(1).keep(1 - arrLevels.size);
		env = Env(arrLevels, arrTimes, arrCurves ? 'lin');
		plotView.value = env.discretize(1024);
	}
	totalTime {
		^value.at(1).sum;
	}
	hasFocus {
		^levelsView.hasFocus || timesView.hasFocus;
	}

}
