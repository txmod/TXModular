// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMidiNoteRange {
	var <>labelView, <>rangeView, <>minNoteView, <>maxNoteView;
	var <>controlSpec, <>action, <lo, <hi, <>round = 0.0001;

	*new { arg window, dimensions, label, controlSpec, action, initMinVal, initMaxVal,
			initAction=false, labelWidth=80, numberWidth = 120, showButtons = false;
		^super.new.init(window, dimensions, label, controlSpec, action, initMinVal, initMaxVal,
			initAction, labelWidth, numberWidth, showButtons);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initMinVal, initMaxVal,
			initAction, labelWidth, numberWidth, showButtons;
		var height, spacingX, arrButtonStrings, arrButtonActions, arrRangePresets;

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

		rangeView = RangeSlider(window, (dimensions.x - labelWidth - numberWidth - spacingX - 4) @ height );
		rangeView.action = {
			minNoteView.value = controlSpec.map(rangeView.lo);
			maxNoteView.value = controlSpec.map(rangeView.hi);
			lo = minNoteView.value;
			hi = maxNoteView.value;
			action.value(this);
		};

		minNoteView = PopUpMenu(window, (numberWidth * 0.6 @ height))
				.background_(Color.white)
				.items_(TXGetMidiNoteString.arrAllNoteNumberNames)
				.action_({
					lo = minNoteView.value;
					rangeView.lo = controlSpec.unmap(minNoteView.value);
					action.value(this);
				});

		maxNoteView = PopUpMenu(window, (numberWidth * 0.6 @ height))
				.background_(Color.white)
				.items_(TXGetMidiNoteString.arrAllNoteNumberNames)
				.action_({
					hi = maxNoteView.value;
					rangeView.hi = controlSpec.unmap(maxNoteView.value);
					action.value(this);
				});

		if (controlSpec.step != 0) {
			rangeView.step = (controlSpec.step / (controlSpec.maxval - controlSpec.minval));
		};

		arrButtonStrings = ["<-12", "<-1", "1->", "12->", "-12", "-1","+1", "+12"];
		arrButtonActions = [
			{this.valueBoth = this.valueBoth - 12;},
			{this.valueBoth = this.valueBoth - 1;},
			{this.valueBoth = this.valueBoth + 1;},
			{this.valueBoth = this.valueBoth + 12;},
			{this.range = (this.range - 12).max(0);},
			{this.range = (this.range - 1).max(0);},
			{this.range = (this.range + 1).max(0);},
			{this.range = (this.range + 12).max(0);},
		];
		arrRangePresets = [
			["Presets: ", {}],
			["2 Octaves, C0 - C2", {this.valueBoth = [24, 48];}],
			["2 Octaves, C1 - C3", {this.valueBoth = [36, 60];}],
			["2 Octaves, C2 - C4", {this.valueBoth = [48, 72];}],
			["3 Octaves, C0 - C3", {this.valueBoth = [24, 60];}],
			["3 Octaves, C1 - C4", {this.valueBoth = [36, 72];}],
			["3 Octaves, C2 - C5", {this.valueBoth = [48, 84];}],
			["4 Octaves, C0 - C4", {this.valueBoth = [24, 72];}],
			["4 Octaves, C1 - C5", {this.valueBoth = [36, 84];}],
			["4 Octaves, C2 - C6", {this.valueBoth = [48, 96];}],
			["Piano 21 - 108", {this.valueBoth = [21, 108];}],
			["Bass Guitar 24 - 60", {this.valueBoth = [24, 60];}],
			["Harp 24 - 103", {this.valueBoth = [24, 103];}],
			["Double Bass 28 - 67", {this.valueBoth = [28, 67];}],
			["Harpsichord 29 - 89", {this.valueBoth = [29, 89];}],
			["Bassoon 34 - 75", {this.valueBoth = [34, 75];}],
			["French Horn 34 - 77", {this.valueBoth = [34, 77];}],
			["Organ 36 - 96", {this.valueBoth = [36, 96];}],
			["Cello 36 - 76", {this.valueBoth = [36, 76];}],
			["Timpani 40 - 55", {this.valueBoth = [40, 55];}],
			["Guitar 40 - 76", {this.valueBoth = [40, 76];}],
			["Male Voice 41 - 72", {this.valueBoth = [41, 72];}],
			["Clarinet 50 - 94", {this.valueBoth = [50, 94];}],
			["Vibraphone 53 - 89", {this.valueBoth = [53, 89];}],
			["Female Voice 52 - 84", {this.valueBoth = [52, 84];}],
			["Violin 55 - 103", {this.valueBoth = [55, 103];}],
			["Trumpet 55 - 82", {this.valueBoth = [55, 82];}],
			["Oboe 58 - 91", {this.valueBoth = [58, 91];}],
			["Flute 60 - 96", {this.valueBoth = [60, 96];}],
			["Piccolo 74 - 102", {this.valueBoth = [74, 102];}],
		];
		if (showButtons==true, {
			StaticText(window, (80 @ height));
			arrButtonStrings.size.do({ arg i;
				Button(window, 35 @ 20)
				.states_([[arrButtonStrings.at(i), TXColor.white, TXColor.sysGuiCol1]])
				.action_(arrButtonActions.at(i))

			});
			window.asView.decorator.nextLine;
			window.asView.decorator.shift(labelWidth + spacingX, 0);

			PopUpMenu(window, ((dimensions.x - labelWidth - spacingX) @ height))
				.background_(Color.white)
				.items_(arrRangePresets.collect({arg item, i; item.at(0);}))
				.action_({ arg view;
					arrRangePresets.at(view.value).at(1).value;
					view.value = 0;
				});
		});

		minNoteView.value = controlSpec.constrain(initMinVal).round(round);
		rangeView.lo = controlSpec.unmap(initMinVal);
		maxNoteView.value = controlSpec.constrain(initMaxVal).round(round);
		rangeView.hi = controlSpec.unmap(initMaxVal);
		lo = minNoteView.value;
		hi = maxNoteView.value;
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
		minNoteView.valueAction = lo.round(round);
	}

	valueBoth_ { arg valueArray;
		lo = controlSpec.constrain(valueArray.at(0));
		minNoteView.valueAction = lo.round(round);
		hi = controlSpec.constrain(valueArray.at(1));
		maxNoteView.valueAction = hi.round(round);
	}

	valueBothNoAction_  { arg valueArray;
			minNoteView.value = controlSpec.map(valueArray.at(0));
			maxNoteView.value = controlSpec.map(valueArray.at(1));
			lo = minNoteView.value;
			hi = maxNoteView.value;
	}

	lo_ {arg value;
		lo = controlSpec.constrain(value);
		minNoteView.valueAction = lo.round(round);
	}

	hi_ {  arg value;
		hi = controlSpec.constrain(value);
		maxNoteView.valueAction = hi.round(round);
	}

	range_ {arg value;
		hi = controlSpec.constrain(lo + value.abs);
		maxNoteView.valueAction = hi.round(round);
	}
	set { arg label, spec, argAction, initMinVal, initMaxVal, initAction=false;
		labelView.string = label;
		controlSpec = spec.asSpec;
		initMinVal =  initMinVal ? controlSpec.minval;
		initMaxVal =  initMaxVal ? controlSpec.maxval;

		action = argAction;

		minNoteView.value = controlSpec.constrain(initMinVal).round(round);
		rangeView.lo = controlSpec.unmap(initMinVal);
		maxNoteView.value = controlSpec.constrain(initMaxVal).round(round);
		rangeView.hi = controlSpec.unmap(initMaxVal);
		if (initAction) {
			action.value(this);
		};
	}
}
