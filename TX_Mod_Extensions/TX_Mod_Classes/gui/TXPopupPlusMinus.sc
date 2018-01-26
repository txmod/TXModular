// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXPopupPlusMinus {	// popup module with label and plus/minus buttons
	var <>labelView, <>popupMenuView, <>btnPlus, <>btnMinus, <>action, <value;

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
		if (initVal > (items.size - 1), {
			initVal = 0;
		});
		action = argAction;

		popupMenuView = PopUpMenu(argParent,
			(dimensions.x-labelWidth-40-(spacingX*3)) @ dimensions.y);
		popupMenuView.items = items;
		popupMenuView.action = {
			value = popupMenuView.value;
			action.value(this);
		};

		btnPlus = Button(argParent, 20 @ 20)
			.states_([["+", TXColor.white, TXColor.sysGuiCol1]])
			.action_({|view|
				popupMenuView.valueAction = (popupMenuView.value + 1).min(popupMenuView.items.size-1);
			});

		btnMinus = Button(argParent, 20 @ 20)
			.states_([["-", TXColor.white, TXColor.sysGuiCol1]])
			.action_({|view|
				popupMenuView.valueAction = (popupMenuView.value - 1).max(0);
			});


		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			popupMenuView.value = value;
		};
	}
	value_ { arg argVal;
		popupMenuView.valueAction = argVal.min(popupMenuView.items.size-1);
	}

	valueAction_  { arg argVal;
		popupMenuView.valueAction = argVal.min(popupMenuView.items.size-1);
	}
	valueNoAction_  { arg argVal;
		popupMenuView.value = argVal.min(popupMenuView.items.size-1);
	}
	set { arg label, argAction, initVal, initAction=false;
		labelView.string = label;
		action = argAction;
		initVal = initVal ? 0;
		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			popupMenuView.value = value;
		};
	}

	hasFocus {
		^popupMenuView.hasFocus || btnPlus.hasFocus || btnMinus.hasFocus;
	}

}
