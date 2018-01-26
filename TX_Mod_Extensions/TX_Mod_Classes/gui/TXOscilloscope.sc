// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXOscilloscope{
	classvar <>scopeWindow;
	classvar classData;

	*initClass {
		classData = ();
		classData.windowPosX = 0;
		classData.windowPosY = 500;
		classData.alwaysOnTop = 1;
	}


	*closeWindow {
		if (scopeWindow.notNil and: {scopeWindow.isClosed.not}, {
			classData.windowPosX = scopeWindow.bounds.left;
			classData.windowPosY = scopeWindow.bounds.top;
			if (classData.scope.notNil, {classData.scope.stop});
			scopeWindow.close;
		});
	}


	*rebuildWindow {arg nameString, server, busArray, scopeRate;
		this.closeWindow;
		{this.showWindow(nameString, server, busArray, scopeRate);}.defer(0.03);
	}

	*showWindow {arg nameString, server, busArray, scopeRate;
		var zoom, bufSize, widgetsView, alwaysOnTop, startBtn, stopBtn, scopeHoldView;
		var freqNumView, notePopup, labelView;

		if (scopeRate == 'audio', {
			zoom = 1;
			bufSize = 4096;
		}, {
			zoom = 0.05;
			bufSize = 4096 * 32;
		});
		busArray = busArray.asArray;

		if (scopeWindow.notNil and: {scopeWindow.isClosed.not}, {
			scopeWindow.close;
			0.1.wait;
		});
		scopeWindow = Window("Oscilloscope", Rect(classData.windowPosX, classData.windowPosY, 284, 330));
		scopeWindow.alwaysOnTop = classData.alwaysOnTop.asBoolean;
		nameString = nameString + ", Bus" + busArray;
		scopeWindow.name = "Oscilloscope:" + nameString;


		widgetsView = View.new(scopeWindow.view, Rect(0, 0, 600, 58));

		// CheckBox: window on top
		alwaysOnTop = TXCheckBox(widgetsView, Rect(4, 4, 100, 20), "window on top",
			TXColor.sysGuiCol1, TXColor.white, TXColor.white, TXColor.sysGuiCol1);
		alwaysOnTop.value = classData.alwaysOnTop;
		alwaysOnTop.action = {|view|
			classData.alwaysOnTop = view.value;
			scopeWindow.alwaysOnTop = classData.alwaysOnTop.asBoolean;
		};

		// help
		Button(widgetsView, Rect(118, 4, 40, 20))
		.states_([ ["help", TXColor.white, TXColor.sysHelpCol] ])
        .action_({arg view;
			var windowPoint = view.mapToGlobal(36 @ 27);
            TXHelpScreen.openFile("TX_Meters And Scopes", inLeft: windowPoint.x,
							inTop: Window.screenBounds.height - TXHelpScreen.classData.defaultHeight - windowPoint.y);
        });

		// stop/start buttons
		stopBtn = Button(widgetsView, Rect(172, 4, 50, 20))
		.states_([ ["stop", TXColor.white, TXColor.sysDeleteCol] ])
        .action_({ arg butt;
            classData.scope.stop;
        });
		startBtn = Button(widgetsView, Rect(226, 4, 50, 20))
		.states_([ ["start", TXColor.white, TXColor.sysGuiCol2] ])
        .action_({ arg butt;
            classData.scope.run;
        });

		labelView = StaticText(widgetsView, Rect(10, 32, 54, 20));
		labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		labelView.string = "cycle freq";
		labelView.align = \center;

		freqNumView = TXScrollNumBox(widgetsView, Rect(67, 32, 40, 20)).maxDecimals_(4);
		freqNumView.action = {arg view;
			var holdFreq = view.value;
			this.setScopeCycleFreq(holdFreq, server);
		};
		// x0.5/x2 buttons
		Button(widgetsView, Rect(110, 34, 30, 16))
		.states_([ ["x 0.5", TXColor.white, TXColor.sysGuiCol1] ])
        .action_({ arg butt;
            freqNumView.valueAction = freqNumView.value * 0.5;
        });
		Button(widgetsView, Rect(144, 34, 30, 16))
		.states_([ ["x 2", TXColor.white, TXColor.sysGuiCol1] ])
        .action_({ arg butt;
            freqNumView.valueAction = freqNumView.value * 2;
        });

		labelView = StaticText(widgetsView, Rect(180, 32, 50, 20));
		labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		labelView.string = "midi note";
		labelView.align = \center;

		notePopup = PopUpMenu(widgetsView, Rect(232, 32, 46, 20));
		notePopup.stringColor_(TXColor.black).background_(TXColor.white);
		notePopup.items = ["..."] ++ 103.collect({arg item; TXGetMidiNoteString(item + 24);});
		notePopup.action = {arg view;
			var holdFreq;
			if ((classData.scope.rate == 'audio') and: (notePopup.value > 0), {
				holdFreq = (view.value + 23).midicps;
				freqNumView.valueAction_(holdFreq);
			});
		};

		// scope
		scopeHoldView = View.new(scopeWindow.view, Rect(10, 60, 260, 260));
		scopeHoldView.resize_(5);

		classData.scope = Stethoscope.new(server, busArray.size, busArray.at(0), bufSize, zoom,
			scopeRate, scopeHoldView);
		classData.scope.view.resize = 5;
		classData.scope.scopeView.resize = 5;

		scopeWindow.front;
	}

	*setScopeCycleFreq {arg holdFreq, server;
		var holdRate, frameDur, targetDur;
		if (classData.scope.rate == 'audio', {
			holdRate = server.sampleRate;
		},{
			holdRate = server.sampleRate / 64;
		});
		frameDur = holdRate.reciprocal;
		targetDur = holdFreq.reciprocal;

		// set scope cycle
		classData.scope.cycle = targetDur / frameDur;
	}

}
