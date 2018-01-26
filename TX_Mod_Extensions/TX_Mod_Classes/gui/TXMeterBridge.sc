// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMeterBridge {
	classvar <>classData;

	*initClass{
		// initialise class variables
		classData = ();
		classData.arrMeters = [];
		classData.window = nil;
		classData.windowLeft = 10;
		classData.windowTop = 500;
		classData.alwaysOnTop = true;
		classData.windowCol = Color.black.alpha_(0.4);
	}

	*addNewMeter{ arg index, server, addAction, rect, label, meterRate;
		var newMeter = TXMeter.new(index, server, addAction, rect, label, meterRate, buildGui: false);
		classData.arrMeters = classData.arrMeters.add(newMeter);
		this.buildGui;
	}

	*addInputMeter{ arg index, server, rect, label;  // input index 0 : same as AudioIn(1)
		var newMeter = TXMeter.input(index, server, rect, label, buildGui: false);
		classData.arrMeters = classData.arrMeters.add(newMeter);
		this.buildGui;
	}

	*addOutputMeter{ arg index, server, rect, label;
		var newMeter = TXMeter.output(index, server, rect, label, buildGui: false);
		classData.arrMeters = classData.arrMeters.add(newMeter);
		this.buildGui;
	}

	*removeAllMeters {
		classData.arrMeters.do( { arg meter, i;
			meter.free;
		});
		classData.arrMeters = [];
	}

	*freezeAllMeters {
		classData.arrMeters.do( { arg meter, i;
			meter.freeAllResponders;
		});
	}

	*unfreezeAllMeters {
		classData.arrMeters.do( { arg meter, i;
			meter.rebuildAllResponders;
		});
	}

	*close{
		this.removeAllMeters;
		if (classData.window.notNil and: {classData.window.view.notClosed}, {
			classData.window.close;
		});
		classData.window = nil;
	}

	*buildGui{arg argparent;
		var parent, windowRect, holdView, windowWidth = 0;

		if (classData.window.notNil, {
			if (classData.window.view.isClosed, {
				classData.window = nil;
			},{
				classData.window.view.removeAll;
			});
		});
		if (argparent.notNil, {
			parent = argparent;
		},{
			// if no parent passed, create window
			windowWidth = 10;
			classData.arrMeters.do( { arg meter, i;
				windowWidth = windowWidth + 4 + ((meter.size * 30) + 30).max(108);
			});
			windowRect = Rect(classData.windowLeft, classData.windowTop, windowWidth.max(380), 290);

			if (classData.window.isNil, {
				classData.window = Window("Meter Bridge", windowRect, resizable: true);
				classData.window.view.decorator = FlowLayout(classData.window.view.bounds);

				classData.window.alwaysOnTop = classData.alwaysOnTop;
				classData.window.view.background = classData.windowCol;
				classData.window.alpha = 1.0;
				classData.window.onClose = {
					this.removeAllMeters;
				};
			}, {
				classData.window.setInnerExtent(windowWidth.max(380), 290);
				classData.window.view.decorator = FlowLayout(windowRect);
			});
			parent = classData.window.view;

			holdView = TXCheckBox(classData.window, Rect(0, 0, 110, 20),
				"window on top", TXColor.grey(0.65), TXColour.white, TXColor.white, TXColor.grey(0.65));
			holdView.value =  classData.alwaysOnTop;
			holdView.action = {|view|
				classData.alwaysOnTop = view.value.asBoolean;
				classData.window.alwaysOnTop = classData.alwaysOnTop;
			};

			holdView = TXCheckBox(classData.window, Rect(0, 0, 70, 20),
				"freeze", TXColor.sysGuiCol1, TXColour.white, TXColor.white, TXColor.sysGuiCol1);
			holdView.value =  0;
			holdView.action = {|view|
				if (view.value == 1, {
					this.freezeAllMeters;
				}, {
					this.unfreezeAllMeters;
				});
			};

			holdView = PopUpMenu(classData.window, Rect(0, 0, 120, 20));
			holdView.items = ["colour: half-grey", "colour: grey", "colour: white", "colour: black", "colour: clear"];
			holdView.background_(TXColor.grey(0.85));
			holdView.stringColor_(TXColor.grey(0.65));
			holdView.action = { arg view;
				classData.windowCol = [Color.black.alpha_(0.4), Color.grey, Color.white, Color.black, Color.clear][view.value];
				classData.window.view.background = classData.windowCol;
			};
			holdView.value = [Color.black.alpha_(0.4), Color.grey, Color.white, Color.black, Color.clear].indexOfEqual(classData.windowCol) ? 0;

			classData.window.view.decorator.nextLine;
			{classData.window.front}.defer(0.1);
		});

		// StaticText(parent, Rect( 10, 5, 100, 15))
		// .string_(label).stringColor_(Color.black).align_(\left);

		classData.arrMeters.do( { arg meter, i;
			var holdView = CompositeView(parent, Rect( 0, 0, ((meter.size * 30) + 30).max(108), 220));
			meter.buildGui(holdView, 160);
		});
		if (classData.arrMeters.size > 1, {
			parent.decorator.nextLine;
			classData.arrMeters.do( { arg meter, i;
				var holdView = CompositeView(parent, Rect( 0, 0, ((meter.size * 30) + 30).max(108), 24));
				holdView.decorator = FlowLayout(holdView.bounds);
				holdView.decorator.shift(4, 0);
				Button(holdView, 16 @ 20)
				.states_([["<", TXColor.white, TXColor.sysGuiCol1]])
				.action_({
					this.moveMeterLeft(i);
				});
				Button(holdView, 16 @ 20)
				.states_([[">", TXColor.white, TXColor.sysGuiCol1]])
				.action_({
					this.moveMeterRight(i);
				});
				Button(holdView, 26 @ 20)
				.states_([["Del", TXColor.white, TXColor.sysDeleteCol]])
				.action_({
					classData.arrMeters(i).free;
					classData.arrMeters.removeAt(i);
					// recreate view
					this.buildGui;
				});
			});
		});
	}

	*moveMeterLeft{arg argIndex;
		if (argIndex > 0, {
			classData.arrMeters.swap(argIndex - 1, argIndex);
			// recreate view
			this.buildGui;
		});
	}

	*moveMeterRight{arg argIndex;
		if (argIndex < (classData.arrMeters.size - 1), {
			classData.arrMeters.swap(argIndex, argIndex + 1);
			// recreate view
			this.buildGui;
		});
	}


}
