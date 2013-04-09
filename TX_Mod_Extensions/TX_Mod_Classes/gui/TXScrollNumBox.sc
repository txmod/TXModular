// blackrain at realizedsound dot net
// mod from the original concept by thor and cylob / original behaviour from sc2 by James McCartney
// fix key modidiers bug and horizontral scroll action by Stephan Wittwer 08/2006
// handle a nil value by Wouter Snoei 08/2006
// and made GUI cross platformable by ixi 03/2008
//
// modified to fit into TX Modular system by Paul Miller 07/2008
//	 - action function only run on mouseup,
//	 - controlspec can be passed to methods initSNBox & updateSpec to control scroll increment settings


// NB this is now based on NumberBox
TXScrollNumBox : ViewRedirect {
	*key { ^\numberBox }
	*updateNumberBoxFromSpec { arg numberBox, spec;
		var holdRange;
		if (spec.class == ControlSpec, {
			numberBox.clipLo = spec.minval;
			numberBox.clipHi = spec.maxval;
			holdRange = (numberBox.clipHi - numberBox.clipLo).abs;
			if (holdRange <= 10, {
				numberBox.scroll_step = numberBox.step = 0.1;
			});
			if ((holdRange > 10) and: (holdRange <= 50), {
				numberBox.scroll_step = numberBox.step = 1.0;
			});
			if ((holdRange > 50) and: (holdRange <= 200), {
				numberBox.scroll_step = numberBox.step = 1.0;
			});
			if (holdRange > 200, {
				numberBox.scroll_step = numberBox.step = 10.0;
			});
		});
	}
	// updateSpec { arg spec; }
}

/* Removed old version:
	updateSpec{arg spec;
		var holdRange;
		if (spec.class == ControlSpec, {
			clipLo = spec.minval;
			clipHi = spec.maxval;
			holdRange = (clipHi - clipLo).abs;
			if (holdRange <= 10, {
				inc=0.1; shift_step=0.01; ctrl_step=1.0;
			});
			if ((holdRange > 10) and: (holdRange <= 50), {
				inc=1.0; shift_step=0.1; ctrl_step=5.0;
			});
			if ((holdRange > 50) and: (holdRange <= 200), {
				inc=1.0; shift_step=0.1; ctrl_step=10.0;
			});
			if (holdRange > 200, {
				inc=10.0; shift_step=1; ctrl_step=50.0;
			});
		});
	}




TXScrollNumBox {
	var <>clipLo = -inf, <>clipHi = inf, hit, <>inc=1.0, <>scroll=true, <>shift_step=0.1, <>ctrl_step=10.0;
	var box, object, holdBoxObj, changed = false;
	*viewClass { ^NumberBox }
	*new {arg parent, bounds, spec;
		^super.new.initSNBox(parent, bounds, spec);

	}
	initSNBox { arg parent, bounds, spec;
		this.updateSpec(spec.value);
		hit = Point(0,0);
		box = NumberBox .new(parent, bounds)
			.scroll_(false)
			.mouseDownAction_({ arg me, x, y, modifiers, buttonNumber, clickCount;
//				[me, x, y, modifiers].postln;
//				holdBoxObj = box.object;
				changed = false;
				hit = Point(x,y);
				if (scroll == true, {
					case
						{ modifiers & 131072 == 131072 }
							{ inc = shift_step }
						{ modifiers & 262144 == 262144 }
							{ inc = ctrl_step };
				});
			})
			.mouseMoveAction_({ arg me, x, y, modifiers;
				var direction;
//				[me, x, y, modifiers].postln;
				if (scroll == true, {
					changed = true;
					direction = 1.0;
						// horizontal or vertical scrolling:
					if ( (x - hit.x) < 0 or: { (y - hit.y) > 0 }) { direction = -1.0; };

//					box.valueAction = (box.value + (inc * box.step * direction));
					box.value = (box.value + (inc * box.step * direction)).clip(clipLo, clipHi);
					hit = Point(x, y);
				});
			})
			.mouseUpAction_({ arg me, x, y, modifiers;
//				if (holdBoxObj != box.object, { box.doAction });
				if (changed == true, { box.doAction; changed = false;});

/* coding note - to get this to work, I had to extend SCNumberBox:mouseUp to execute the mouseUpAction - see file extSCNumberBox.sc */

			});
	}
	updateSpec{arg spec;
		var holdRange;
		if (spec.class == ControlSpec, {
			clipLo = spec.minval;
			clipHi = spec.maxval;
			holdRange = (clipHi - clipLo).abs;
			if (holdRange <= 10, {
				inc=0.1; shift_step=0.01; ctrl_step=1.0;
			});
			if ((holdRange > 10) and: (holdRange <= 50), {
				inc=1.0; shift_step=0.1; ctrl_step=5.0;
			});
			if ((holdRange > 50) and: (holdRange <= 200), {
				inc=1.0; shift_step=0.1; ctrl_step=10.0;
			});
			if (holdRange > 200, {
				inc=10.0; shift_step=1; ctrl_step=50.0;
			});
		});
	}
	align_{arg a;
		box.align_(a);
	}
	value_ { arg val;
//		box.keyString = nil;		box.object = val !? { val.clip(clipLo, clipHi) };
		box.string = box.object.asString;
	}
	focusColor_ {arg color;
		box.focusColor_(color);
	}
	focusColor {
		^box.focusColor;
	}
	background_ {arg bg;
		box.background_(bg);
	}
	background {
		^box.background;
	}
	stringColor_ {arg color;
		box.stringColor_(color);
	}
	stringColor {
		^box.stringColor;
	}
	step_{arg st;
		box.step_(st);
	}
	font{
		^box.font;
	}
	font_{arg f;
		box.font_(f);
	}
	action_{arg act;
		box.action_(act);
	}
	keyDownAction_ {arg act;
		box.keyDownAction_(act);
	}
	value {
		^box.value;
	}
	valueAction_ { arg val;
		var prev;
		prev = box.object;
		box.value = val !? { val.clip(clipLo, clipHi) };
		if (object != prev, { box.doAction });
	}
	canFocus {
		^box.getProperty(\canFocus)
	}
	canFocus_ { arg bool;
		box.setProperty(\canFocus, bool)
	}
	visible {
		^box.visible;
	}
	visible_ { arg bool;
		box.visible_(bool);
	}
	remove {
		^box.remove;
	}
	enabled {
		^box.enabled;
	}
	enabled_ { arg bool;
		box.enabled_(bool);
	}
	typingColor { ^box.typingColor }
	typingColor_ { |color|  box.typingColor_(color)  }

	normalColor { ^box.normalColor }
	normalColor_ { |color|  box.normalColor_(color)  }

	controlSpec {
		^ControlSpec(clipLo, clipHi);
	}

	controlSpec_ {arg spec;
		this.updateSpec(spec);
	}
}

*/