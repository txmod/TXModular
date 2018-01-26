// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXInfoScreen {		// Information Screen module
	var w;

	*new{ arg message = "ERROR", showCloseBtn=1 , winColour, inLeft, inTop, arrInfoLines;
		^super.new.makeWindow(message, showCloseBtn, winColour, inLeft, inTop, arrInfoLines);
	}

	*newConfirmWindow {arg argConfirmedAction, message = "Confirm", winColour, inLeft, inTop;
		^super.new.makeConfirmWindow(argConfirmedAction, message, winColour, inLeft, inTop);
	}

	makeWindow{ arg message, showCloseBtn, winColour, inLeft, inTop, arrInfoLines;
		var button, arrInfoLinesHeight, holdHeight, windowPoint;
		{
			arrInfoLinesHeight = arrInfoLines.size * 24;
			holdHeight = 150 + arrInfoLinesHeight;
			if (inLeft == nil or: (inTop == nil), {
				if (TXSystem1GUI.w.notNil and: {TXSystem1GUI.w.view.notClosed}, {
					windowPoint = TXSystem1GUI.w.view.mapToGlobal(0 @ 40);
					inLeft = windowPoint.x;
					inTop = Window.screenBounds.height - holdHeight - windowPoint.y ;
				},{
					inLeft = 20;
					inTop = 500;
				});
			});
			winColour = winColour ? TXColour.red;
			w = Window("Information", Rect(inLeft, inTop, 900, holdHeight));
			w.front;
			w.alwaysOnTop_(true);
			w.view.decorator = FlowLayout(w.view.bounds);
			w.view.background = winColour;
			w.view.decorator.shift(30,30);
			StaticText(w, 760 @ 40)
			.string_(message)
			.background_(TXColour.white);
			arrInfoLines.do({ arg item, i;
				// only for first 20 items
				if (i < 20, {
					StaticText(w, 760 @ 20)
					.string_(item)
					.background_(TXColour.white);
				});
			});
			if (showCloseBtn==1, {
				w.view.decorator.nextLine;
				w.view.decorator.shift(30,30);
				button = Button(w, 100 @ 30)
				.states = [["Close", TXColor.white, TXColor.black]];
				button.action = {this.close};
			});
		}.defer(0.1);
	}

	close {		//	close window
		// OLD if (w.isClosed.not, {w.close;});
		// NEW:
		if (w.isClosed.not, {{w.close}.defer;});
	}

	makeConfirmWindow{ arg argConfirmedAction, message, winColour, inLeft, inTop;
		var btnConfirm, btnCancel, windowPoint;
		{
			if (inLeft == nil or: (inTop == nil), {
				if (TXSystem1GUI.w.notNil and: {TXSystem1GUI.w.view.notClosed}, {
					windowPoint = TXSystem1GUI.w.view.mapToGlobal(0 @ 40);
					inLeft = windowPoint.x;
					inTop = Window.screenBounds.height - 150 - windowPoint.y;
				},{
					inLeft = 20;
					inTop = 500;
				});
			});
			winColour = winColour ? TXColour.sysGuiCol3;
			w = Window("Information", Rect(inLeft, inTop, 900, 150));
			w.front;
			w.alwaysOnTop_(true);
			w.view.decorator = FlowLayout(w.view.bounds);
			w.view.background = winColour;
			w.view.decorator.shift(30,30);
			StaticText(w, 760 @ 40)
			.string_(message)
			.background_(TXColour.white);

			// confirm button
			w.view.decorator.nextLine;
			w.view.decorator.shift(30,30);
			btnConfirm = Button(w, 80 @ 30)
			.states = [["Confirm", TXColor.white, TXColor.black]];
			btnConfirm.action = { this.close; argConfirmedAction.value;};

			// cancel button
			w.view.decorator.shift(30, 0);
			btnCancel = Button(w, 80 @ 30)
			.states = [["Cancel", TXColor.white, TXColor.grey]];
			btnCancel.action = {this.close;};
		}.defer(0.1);
	}

}
