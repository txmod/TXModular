// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// a very simpe class to display a number or string but not allow editing
// useful as display-only replacement that still responds to value and string in a similar way to editable views

TXDisplayTextNum {
	var <>textView, <value, background;
	var <>round = 0.001;

	*new { arg window, dimensions;
		^super.new.init(window, dimensions);
	}
	init { arg window, dimensions;

		textView = StaticText.new(window, dimensions);
	}
	value_ { arg val;
		textView.string = val.round(round).asString;
	}
	action{
		// dummy method
	}
	action_{
		// dummy method
	}
	valueAction_{ arg val;
		textView.string = val.round(round).asString;
	}

	stringColor {
		^textView.stringColor;
	}
	stringColor_ { arg color;
		textView.stringColor_(color);
	}

	string{
		textView.string;
	}
	string_{arg str;
		textView.string_(str);
	}

	font{
		textView.font;
	}
	font_{arg f;
		textView.font_(f);
	}

	background_ {arg bg;
		textView.background_(bg);
	}
	background {
		^textView.background;
	}

	visible { ^textView.visible }
	visible_ { |bool| textView.visible_(bool) }

	enabled {  ^textView.enabled }
	enabled_ { |bool| textView.enabled_(bool) }

	remove { textView.remove }

	canFocus {
		^false;
	}
	canFocus_ {
		// dummy method
	}

	isClosed {
		^textView.isClosed;
	}
	notClosed {
		^textView.notClosed;
	}
}
