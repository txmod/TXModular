// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXListViewLabel {	// listview module with label
	var <>labelView, <>listView, <>action, <value;

	*new { arg argParent, dimensions, label, items, action, initVal,
			initAction=false, labelWidth=80;
		^super.new.init(argParent, dimensions, label, items, action, initVal,
			initAction, labelWidth);
	}
	init { arg argParent, dimensions, label, items, argAction, initVal,
			initAction, labelWidth;
		labelView = StaticText(argParent, labelWidth @ dimensions.y);
		labelView.string = label;
		labelView.align = \right;

		initVal = initVal ? 0;
		action = argAction;

		listView = TXListView(argParent, (dimensions.x - (labelWidth+4)) @ dimensions.y);
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

