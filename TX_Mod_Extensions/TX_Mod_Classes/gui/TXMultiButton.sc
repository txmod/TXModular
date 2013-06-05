// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMultiButton {
	var <>scrollView;

	*new { arg window, dimensions, arrStrings, action, rowHeight, stringColor, background;
		^super.new.init(window, dimensions, arrStrings, action, rowHeight, stringColor, background);
	}
	init { arg window, dimensions, arrStrings, argAction, rowHeight, stringColor, background;
		var scrollBox, compositeViewHeight;

		rowHeight = rowHeight ? 80;
		stringColor = stringColor ? TXColor.white;
		background = background ? TXColor.sysGuiCol1;
		compositeViewHeight = (rowHeight + 4) * arrStrings.size;

		scrollView = ScrollView(window, Rect(0, 0, dimensions.x, dimensions.y))
				.hasBorder_(true);
		scrollView.autohidesScrollers = true;
		scrollView.hasVerticalScroller = true;
		scrollView.hasHorizontalScroller = false;
		scrollBox = CompositeView(scrollView, Rect(0, 0, dimensions.x - 20, compositeViewHeight));
		scrollBox.decorator = FlowLayout(scrollBox.bounds);
		scrollBox.decorator.margin.x = 0;
		scrollBox.decorator.margin.y = 0;
		scrollBox.decorator.reset;

		arrStrings.do({ arg item, i;
			var but, txt;
			// Replacing Button with UserView to allow left justified
			// but = Button(scrollBox, (dimensions.x - 20) @ rowHeight);
			// but.states_([[item, stringColor, background]]);
			// but.action_({argAction.value(i);});
			but = UserView(scrollBox, (dimensions.x - 20) @ rowHeight);
			but.background_(background);
			but.mouseDownAction_({argAction.value(i);});
			but.drawFunc = {
				Pen.fillColor = Color.black;
				Pen.stringAtPoint(item, Point(4, 4));
			}
		});
	}
}
