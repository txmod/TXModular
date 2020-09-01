// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMultiKnobMidiNote {
	var <>labelView, <>arrKnobViews, <>arrNumBoxViews, <>arrPopups, <>controlSpec, <>action, <value, <size;
	var midiNoteLo, midiNoteHi;

	*new { arg window, dimensions, label, controlSpec, action, initVal,
			initAction=false, labelWidth=40, knobWidth = 50;
		^super.new.init(window, dimensions, label, controlSpec, action, initVal,
			initAction, labelWidth, knobWidth);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initVal,
			initAction, labelWidth, knobWidth, startLeft;
		var holdKnob, holdNumBox, holdPopup, holdPopupLabels;

		startLeft = window.decorator.left;

		labelView = StaticText(window, labelWidth @ dimensions.y);
		labelView.string = label;
		labelView.align = \right;

		controlSpec = argControlSpec.asSpec;
		initVal = initVal ? Array.fill(8, controlSpec.default);
		action = argAction;

		value = initVal;
		size = initVal.size;

		midiNoteLo = (controlSpec.clipLo * 127).roundUp;
		midiNoteHi = (controlSpec.clipHi * 127).asInt;
		holdPopupLabels = (midiNoteLo..midiNoteHi).collect({arg item, i;
			TXGetMidiNoteString(item);
		});

		// add knobs
		size.do({ arg item, i;
			holdKnob = Knob(window, knobWidth @ knobWidth);
			holdKnob.action = { arg view;
				value[i] = controlSpec.map(view.value);
				arrNumBoxViews[i].value = (value[i] * 127).midicps;
				arrPopups[i].value = (value[i] * 127).round.clip(midiNoteLo, midiNoteHi) - midiNoteLo;
				action.value(this);
			};
			arrKnobViews = arrKnobViews.add(holdKnob);
		});


		window.decorator.nextLine.shift(startLeft + labelWidth + window.decorator.gap.x, 0);

		// add numboxes
		size.do({ arg item, i;
			holdNumBox = NumberBox(window, knobWidth @ 18);
			holdNumBox.decimals = 3;
			holdNumBox.action = { arg view;
				view.value = view.value.clip(midiNoteLo.midicps, midiNoteHi.midicps);
				value[i] = view.value.cpsmidi * 127.reciprocal;
				arrKnobViews[i].value = controlSpec.unmap(value[i]);
				arrPopups[i].value = (value[i] * 127).round.clip(midiNoteLo, midiNoteHi) - midiNoteLo;
				action.value(this);
			};
			arrNumBoxViews = arrNumBoxViews.add(holdNumBox);
		});

		window.decorator.nextLine.shift(startLeft + labelWidth + window.decorator.gap.x, 0);

		// add Popups
		size.do({ arg item, i;
			holdPopup = PopUpMenu(window, knobWidth @ 18);
			holdPopup.items = holdPopupLabels;
			holdPopup.action = { arg view;
				value[i] = (midiNoteLo + view.value) * 127.reciprocal;
				arrKnobViews[i].value = controlSpec.unmap(value[i]);
				arrNumBoxViews[i].value = (value[i] * 127).midicps;
				action.value(this);
			};
			arrPopups = arrPopups.add(holdPopup);
		});

		this.value_(initVal);

		if (initAction) {
			action.value(this);
		};
	}
	value_ { arg argValue;
		value = argValue;
		arrKnobViews.do({ arg item, i;
			if (value[i].notNil, {
				item.value = controlSpec.unmap(value[i]);
			});
		});
		arrNumBoxViews.do({ arg item, i;
			if (value[i].notNil, {
				item.value = (value[i] * 127).clip(midiNoteLo, midiNoteHi).midicps;
			});
		});
		arrPopups.do({ arg item, i;
			if (value[i].notNil, {
				item.value = (value[i] * 127).round.clip(midiNoteLo, midiNoteHi) - midiNoteLo;
			});
		});
	}
	valueAction_ { arg argValue;
		this.value_(argValue);
		action.value(this);
	}

	hasFocus {
		arrKnobViews.do({arg item;
			if (item.hasFocus, {
				^true;
			});
		});
		arrNumBoxViews.do({arg item;
			if (item.hasFocus, {
				^true;
			});
		});
		arrPopups.do({arg item;
			if (item.hasFocus, {
				^true;
			});
		});
		^false;
	}

}
