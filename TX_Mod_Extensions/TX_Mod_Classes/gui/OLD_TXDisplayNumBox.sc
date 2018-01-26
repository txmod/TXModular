// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// a very simpe class to display a number but not allow editing
// useful as display-only replacement for numberbox that still responds to value in a similar way

TXDisplayNumBox {
	var <>staticTextView, <value, background;
	var <>round = 0.001;

	*new { arg window, dimensions;
		^super.new.init(window, dimensions);
	}
	init { arg window, dimensions;

		staticTextView = StaticText.new(window, dimensions);
	}
	value_ { arg val;
		staticTextView.string = val.round(round).asString;
	}
	action{
		// dummy method
	}
	action_{
		// dummy method
	}

	stringColor {
		^staticTextView.stringColor;
	}
	stringColor_ { arg color;
		staticTextView.stringColor = color;
	}

	string{
		staticTextView.string;
	}
	string_{arg str;
		staticTextView.string_(str);
	}

	font{
		staticTextView.font;
	}
	font_{arg f;
		staticTextView.font_(f);
	}

	background_ {arg bg;
		staticTextView.background_(bg);
	}
	background {
		^staticTextView.background;
	}

	visible { ^staticTextView.visible }
	visible_ { |bool| staticTextView.visible_(bool) }

	enabled {  ^staticTextView.enabled }
	enabled_ { |bool| staticTextView.enabled_(bool) }

	remove { staticTextView.remove }

	canFocus {
		^false;
	}
	canFocus_ {
		// dummy method
	}
}
