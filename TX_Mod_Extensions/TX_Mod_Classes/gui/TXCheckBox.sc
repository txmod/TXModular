// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXCheckBox {

	var <>buttonView, <>action, <value=0, <parent;
	var <>offText, <>onText, <onOffTextType;

	*new{arg argParent, bounds, text, offStringColor, offBackground, onStringColor, onBackground, onOffTextType;
		^super.new.init(argParent, bounds, text, offStringColor, offBackground, onStringColor, onBackground, onOffTextType);
	}

	init {arg argParent, bounds, text, offStringColor, offBackground, onStringColor, onBackground, argOnOffTextType;

		this.onOffTextType_(argOnOffTextType ? 0);

		// assign parent
		parent = argParent;
		// create button
		buttonView = Button(argParent, bounds);
		buttonView.states = [
			[offText ++ (text ? ""), offStringColor, offBackground],
			[onText ++ (text ? ""), onStringColor, onBackground]
		];
		buttonView.action = { |view|
			value = buttonView.value;
			action.value(this);
			//view.focus(false);
			//argParent.refresh;
		};
	}

	onOffTextType_ {arg type = 0;
		// create on and off text
		onOffTextType = type.asInt;
		[
			{offText = "[  ] "; onText =  "[X] "; }, // 0
			{offText = " "; onText = "X"; }, // 1
			{offText = "[OFF] "; onText = "[ON] "; }, // 2
			{offText = "[Off] "; onText = "[On] "; }, // 3
			{offText = "-OFF- "; onText = "-ON- "; }, // 4
			{offText = "(OFF) "; onText = "(ON) "; }, // 5
			{offText = "[-] "; onText = "[+] "; },// 6
			{offText = "OFF "; onText = "ON "; }, // 7
			{offText = "0 - OFF"; onText = "1 - ON"; }, // 8
			{offText = "0 - False"; onText = "1 - True"; }, // 9
			{offText = "NO "; onText = "YES "; }, // 10
			{offText = "[NO] "; onText = "[YES] "; }, // 11
			{offText = ""; onText = ""; }, // 12 - just let colour change indicated state
		].at(onOffTextType).value;
	}

	// pass unknown calls to buttonView
	doesNotUnderstand {arg selector ... args;
		buttonView.performList(selector, args);
	}

	hasFocus {
		^buttonView.hasFocus;
	}
}
