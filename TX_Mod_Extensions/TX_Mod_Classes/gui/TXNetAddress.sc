// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXNetAddress {
	classvar ipAddressClipboard, ipAddressClipboard2, ipAddressClipboard3, ipAddressClipboard4,
		ipAddressClipboard5;
	var <>labelView, <>numberView1, <>numberView2, <>numberView3, <>numberView4;
	var arrPresets, <>presetPopupView;
	var <>action, <string, controlSpec;

	*new { arg window, dimensions, label, action, initVal,
			initAction=false, labelWidth=80, textWidth = 250, showPresets = true, displayOnly = false;
		^super.new.init(window, dimensions, label, action, initVal,
			initAction, labelWidth, textWidth, showPresets, displayOnly);
	}
	init { arg window, dimensions, label, argAction, initVal,
			initAction, labelWidth, textWidth, showPresets, displayOnly;
		var holdBox, numWidth;
		dimensions = dimensions.asRect;
		holdBox = CompositeView (window, dimensions);
		holdBox.decorator = FlowLayout(dimensions).margin_(0@0);
		numWidth = ((dimensions.width - labelWidth - 258 - (showPresets.binaryValue * 70))
			/ 4).max(25);

		labelView = StaticText(holdBox, labelWidth @ dimensions.height);
		labelView.string = label;
		labelView.align = \right;

		if (initVal == "", {initVal = "0.0.0.0"});
		if (initVal == " ", {initVal = "0.0.0.0"});
		if (initVal.isNil, {initVal = "0.0.0.0"});
		action = argAction;
		controlSpec = ControlSpec(0, 255, step:1);

		if (displayOnly == true, {
			numberView1 = TXDisplayNumBox(holdBox, numWidth @ dimensions.height);
			StaticText(holdBox, 10 @ dimensions.height).string_(" .");
			numberView2 = TXDisplayNumBox(holdBox, numWidth @ dimensions.height);
			StaticText(holdBox, 10 @ dimensions.height).string_(" .");
			numberView3 = TXDisplayNumBox(holdBox, numWidth @ dimensions.height);
			StaticText(holdBox, 10 @ dimensions.height).string_(" .");
			numberView4 = TXDisplayNumBox(holdBox, numWidth @ dimensions.height);
		},{
			numberView1 = TXScrollNumBox(holdBox, numWidth @ dimensions.height);
			numberView1.action = {
				numberView1.value = controlSpec.constrain(numberView1.value);
				this.makeString;
				action.value(this);
			};
			StaticText(holdBox, 10 @ dimensions.height).string_(" .");
			numberView2 = TXScrollNumBox(holdBox, numWidth @ dimensions.height);
			numberView2.action = {
				numberView2.value = controlSpec.constrain(numberView2.value);
				this.makeString;
				action.value(this);
			};
			StaticText(holdBox, 10 @ dimensions.height).string_(" .");
			numberView3 = TXScrollNumBox(holdBox, numWidth @ dimensions.height);
			numberView3.action = {
				numberView3.value = controlSpec.constrain(numberView3.value);
				this.makeString;
				action.value(this);
			};
			StaticText(holdBox, 10 @ dimensions.height).string_(" .");
			numberView4 = TXScrollNumBox(holdBox, numWidth @ dimensions.height);
			numberView4.action = {
				numberView4.value = controlSpec.constrain(numberView4.value);
				this.makeString;
				action.value(this);
			};
		});

		if (showPresets == true, {
			StaticText(holdBox, 20 @ dimensions.height);
			arrPresets = [
				["Do... ", {}],
				["Reset address: 0.0.0.0 ", {this.string_("0.0.0.0");}],
				["Loopback to this computer: 127.0.0.1 ", {this.string_("127.0.0.1");}],
				["Copy to IP address clipboard 1", {ipAddressClipboard = string;}],
				["Paste from IP address clipboard 1",
					{if (ipAddressClipboard.notNil, {this.string_(ipAddressClipboard); }); }],
				["Copy to IP address clipboard 2", {ipAddressClipboard2 = string;}],
				["Paste from IP address clipboard 2",
					{if (ipAddressClipboard.notNil, {this.string_(ipAddressClipboard2); }); }],
				["Copy to IP address clipboard 3", {ipAddressClipboard3 = string;}],
				["Paste from IP address clipboard 3",
					{if (ipAddressClipboard.notNil, {this.string_(ipAddressClipboard3); }); }],
				["Copy to IP address clipboard 4", {ipAddressClipboard4 = string;}],
				["Paste from IP address clipboard 4",
					{if (ipAddressClipboard.notNil, {this.string_(ipAddressClipboard4); }); }],
				["Copy to IP address clipboard 5", {ipAddressClipboard5 = string;}],
				["Paste from IP address clipboard 5",
					{if (ipAddressClipboard.notNil, {this.string_(ipAddressClipboard5); }); }],
			];
			presetPopupView = PopUpMenu(holdBox, 242 @ dimensions.height)
				.background_(Color.white)
				.items_(arrPresets.collect({arg item, i; item.at(0);}))
				.action_({ arg view;
					arrPresets.at(view.value).at(1).value;
					view.value = 0;
				});
		});

		if (initAction) {
			this.string = initVal;
		}{
			string = initVal;
		};
		this.setNumberViews;
	}

	string_ { arg argString;
		string = argString.asString;
		this.setNumberViews;
		action.value(this);
	}

	stringNoAction_ { arg argString;
		string = argString.asString;
		this.setNumberViews;
	}

	makeString{
		string = numberView1.value.asString ++ "." ++ numberView2.value.asString
			++ "." ++ numberView3.value.asString ++ "." ++ numberView4.value.asString;
	}

	setNumberViews{
		var arrNumbers;
		arrNumbers = string.split($.);
		numberView1.value = (arrNumbers.at(0) ? 0).asInteger;
		numberView2.value = (arrNumbers.at(1) ? 0).asInteger;
		numberView3.value = (arrNumbers.at(2) ? 0).asInteger;
		numberView4.value = (arrNumbers.at(3) ? 0).asInteger;
	}
	stringColor{
		^numberView1.stringColor;
	}

	stringColor_{ arg argColor;
		numberView1.stringColor = argColor;
		numberView2.stringColor = argColor;
		numberView3.stringColor = argColor;
		numberView4.stringColor = argColor;
	}

	font{
		^numberView1.font;
	}

	font_{ arg argFont;
		numberView1.font = argFont;
		numberView2.font = argFont;
		numberView3.font = argFont;
		numberView4.font = argFont;
	}
	background{
		^numberView1.background;
	}

	background_{ arg argColor;
		numberView1.background = argColor;
		numberView2.background = argColor;
		numberView3.background = argColor;
		numberView4.background = argColor;
	}

	remove{
		numberView1.remove;
		numberView2.remove;
		numberView3.remove;
		numberView4.remove;
	}
}
