// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMultiTextBox {	// TXMultiTextBox module with label
	var <>labelView, <>scrollView, <>arrTextViews, <strings, <size;

	*new { arg window, dimensions, label, initStrings,
			labelWidth=80, textWidth = 20, scrollViewWidth, scrollViewAction;
		^super.new.init(window, dimensions, label, initStrings,
			labelWidth, textWidth, scrollViewWidth, scrollViewAction);
	}
	init { arg window, dimensions, label, initStrings,
			labelWidth, textWidth, scrollViewWidth, scrollViewAction;
		var holdTextBox, scrollBox, extraHeight;
		labelView = StaticText(window, labelWidth @ dimensions.y);
		labelView.string = label;
		labelView.align = \right;

		initStrings = initStrings ? Array.fill(8, " ");

		strings = initStrings;
		size = initStrings.size;
		if (scrollViewWidth.notNil, {
			if (scrollViewAction.notNil, {
				extraHeight = 18;
			},{
				extraHeight = 0;
			});
			scrollView = ScrollView(window, Rect(0, 0, scrollViewWidth, dimensions.y+extraHeight))
				.hasBorder_(false);
			if (GUI.current.asSymbol == \cocoa, {
				scrollView.autoScrolls_(false);
			});
			scrollView.hasVerticalScroller = false;
			if (scrollViewAction.notNil, {
				scrollView.hasHorizontalScroller = true;
				scrollView.action = scrollViewAction;
			},{
				scrollView.hasHorizontalScroller = false;
			});
			scrollBox = CompositeView(scrollView, Rect(0, 0, 4+(size * (textWidth+4)), dimensions.y+extraHeight));
			scrollBox.decorator = FlowLayout(scrollBox.bounds);
			scrollBox.decorator.margin.x = 0;
			scrollBox.decorator.margin.y = 0;
			scrollBox.decorator.reset;
		});
		size.do({ arg item, i;
			holdTextBox = StaticText(scrollBox?window, textWidth @ dimensions.y);
			holdTextBox.align_(\center);
			holdTextBox .font_(Font.new("Gill Sans", 10));
			holdTextBox.string = initStrings.at(i).asString;
			arrTextViews = arrTextViews.add(holdTextBox);
		});

	}
	strings_ { arg argValue;
		arrTextViews.do({ arg item, i;
			if (argValue.at(i).notNil, {
				item.string = argValue.at(i);
			});
		});
	}
}
