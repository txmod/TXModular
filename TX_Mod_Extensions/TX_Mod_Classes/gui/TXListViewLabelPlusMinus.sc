// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXListViewLabelPlusMinus {	// listview module with label and plus/minus buttons
	var <>labelView, <>listView, <>btnPlus, <>btnMinus, <>action, <value;

	*new { arg argParent, dimensions, label, items, action, initVal,
			initAction=false, labelWidth=80;
		^super.new.init(argParent, dimensions, label, items, action, initVal,
			initAction, labelWidth);
	}
	init { arg argParent, dimensions, label, items, argAction, initVal,
			initAction, labelWidth;
		var spacingX, spacingY;
		if (argParent.class == Window, {
			spacingX = argParent.view.decorator.gap.x;
			spacingY = argParent.view.decorator.gap.y;
		}, {
			spacingX = argParent.decorator.gap.x;
			spacingY = argParent.decorator.gap.y;
		});
		labelView = StaticText(argParent, labelWidth @ dimensions.y);
		labelView.string = label;
		labelView.stringColor = TXColor.sysGuiCol1;
		labelView.background = TXColor.white;
		labelView.align = \right;

		initVal = initVal ? 0;
		action = argAction;

		listView = TXListView(argParent,
			(dimensions.x-labelWidth-40-(spacingX*3)) @ dimensions.y);
		listView.items = items;
		listView.action = {
			value = listView.value;
			action.value(this);
		};
		listView.receiveDragHandler =  {
			var holdDrag = View.currentDrag.asInteger;
			if (holdDrag.isNumber and: {(holdDrag >= 0) && (holdDrag < items.size)}, {
				listView.valueAction = holdDrag;
			});
		};

		btnPlus = Button(argParent, 20 @ 20)
			.states_([["+", TXColor.white, TXColor.sysGuiCol1]])
			.action_({|view|
				listView.valueAction = (listView.value + 1).min(listView.items.size-1);
			});

		btnMinus = Button(argParent, 20 @ 20)
			.states_([["-", TXColor.white, TXColor.sysGuiCol1]])
			.action_({|view|
				listView.valueAction = (listView.value - 1).max(0);
			});

		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			listView.value = value;
		};
	}
	value_ { arg argVal;
		listView.valueAction = argVal;
	}

	valueAction_  { arg argVal;
		listView.valueAction = argVal;
	}
	valueNoAction_  { arg argVal;
		listView.value = argVal;
	}
	set { arg label, argAction, initVal, initAction=false;
		labelView.string = label;
		action = argAction;
		initVal = initVal ? 0;
		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			listView.value = value;
		};
	}
	hasFocus {
		^listView.hasFocus;
	}
}

