// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFreqScope{
	classvar <>scopeWindow;
	classvar classData;

	var <scope, <window;

	*initClass {
		classData = ();
		classData.windowPosX = 0;
		classData.windowPosY = 100;
		classData.alwaysOnTop = 1;
		classData.scopeOpen = false;
	}


	*closeWindow {
		if (scopeWindow.notNil and: {scopeWindow.isClosed.not}, {
			classData.windowPosX = scopeWindow.bounds.left;
			classData.windowPosY = scopeWindow.bounds.top;
			scopeWindow.close;
		});
		//this.clearViews;
	}


	*rebuildWindow {arg nameString, server, busArray;
		this.closeWindow;
		{this.showWindow(nameString, server, busArray);}.defer(0.1);
	}
	*showWindow {arg nameString, server, busArray;

		var width=522, height=300, busNum, scopeColor, bgColor;
		var rect, scopeRect, windowRect, cope, pad, font, freqLabel, freqLabelDist, dbLabel, dbLabelDist;
		var setFreqLabelVals, setDBLabelVals, scopeView, alwaysOnTop;
		var nyquistKHz;
		busNum = busArray.asArray[0].asControlInput;
		if(classData.scopeOpen != true, { // block the stacking up of scope windows
			//make scope

			scopeColor = scopeColor ?? { Color.new255(255, 218, 000) };

			classData.scopeOpen = true;

			server = server ? Server.default;

			rect = Rect(0, 0, width, height);
			pad = [30, 48, 14, 10]; // l,r,t,b
			scopeRect = Rect(10, 30, width + pad[0] + pad[1] + 4, height + pad[2] + pad[3] + 4);
			font = Font.monospace(9);
			freqLabel = Array.newClear(12);
			freqLabelDist = rect.width/(freqLabel.size-1);
			dbLabel = Array.newClear(17);
			dbLabelDist = rect.height/(dbLabel.size-1);

			nyquistKHz = server.sampleRate;
			if( (nyquistKHz == 0) || nyquistKHz.isNil, {
				nyquistKHz = 22.05 // best guess?
			},{
				nyquistKHz = nyquistKHz * 0.0005;
			});

			setFreqLabelVals = { arg mode, bufsize;
				var kfreq, factor, halfSize;

				factor = 1/(freqLabel.size-1);
				halfSize = bufsize * 0.5;

				freqLabel.size.do({ arg i;
					if(mode == 1, {
						kfreq = (halfSize.pow(i * factor) - 1)/(halfSize-1) * nyquistKHz;
					},{
						kfreq = i * factor * nyquistKHz;
					});

					if(kfreq > 1.0, {
						freqLabel[i].string_( kfreq.asString.keep(4) ++ "k" )
					},{
						freqLabel[i].string_( (kfreq*1000).asInteger.asString)
					});
				});
			};

			setDBLabelVals = { arg db;
				dbLabel.size.do({ arg i;
					dbLabel[i].string = (i * db/(dbLabel.size-1)).asInteger.neg.asString;
				});
			};

			scopeWindow = Window("Freq Analyzer (mono): " ++ nameString,
				Rect(classData.windowPosX, classData.windowPosY, scopeRect.width +30, scopeRect.height +50), false);

			// CheckBox: "window on top"
			alwaysOnTop = TXCheckBox(scopeWindow.view, Rect(10, 4, 100, 20), "window on top",
				TXColor.sysGuiCol1, TXColor.white, TXColor.white, TXColor.sysGuiCol1);
			alwaysOnTop.value = classData.alwaysOnTop;
			alwaysOnTop.action = {|view|
				classData.alwaysOnTop = view.value;
				scopeWindow.alwaysOnTop = classData.alwaysOnTop.asBoolean;
			};
			scopeWindow.alwaysOnTop = classData.alwaysOnTop.asBoolean;

			// help
			Button(scopeWindow.view, Rect(130, 4, 40, 20))
			.states_([ ["help", TXColor.white, TXColor.sysHelpCol] ])
			.action_({arg view;
				var windowPoint = view.mapToGlobal(36 @ 27);
				TXHelpScreen.openFile("TX_Meters And Scopes", inLeft: windowPoint.x,
					inTop: Window.screenBounds.height - TXHelpScreen.classData.defaultHeight - windowPoint.y);
			});

			// scope
			scopeView = View.new(scopeWindow.view, scopeRect);

			freqLabel.size.do({ arg i;
				freqLabel[i] = StaticText(scopeView,
					Rect(pad[0] - (freqLabelDist*0.5) + (i*freqLabelDist), pad[2] - 10, freqLabelDist, 10))
				.font_(font)
				.align_(\center)
				;
				StaticText(scopeView, Rect(pad[0] + (i*freqLabelDist), pad[2], 1, rect.height))
					.string_("")
				;
			});

			dbLabel.size.do({ arg i;
				dbLabel[i] = StaticText(scopeView, Rect(0, pad[2] + (i*dbLabelDist), pad[0], 10))
					.font_(font)
					.align_(\left)
				;
				StaticText(scopeView, Rect(pad[0], dbLabel[i].bounds.top, rect.width, 1))
					.string_("")
				;
			});

			classData.scope = FreqScopeView(scopeView, rect.moveBy(pad[0], pad[2]), server);
			classData.scope.xZoom_((classData.scope.bufSize*0.25) / width);

			setFreqLabelVals.value(classData.scope.freqMode, 2048);
			setDBLabelVals.value(classData.scope.dbRange);

			Button(scopeView, Rect(pad[0] + rect.width, pad[2], pad[1], 16))
				.states_([["stop", Color.white, Color.green(0.5)], ["start", Color.white, Color.red(0.5)]])
				.action_({ arg view;
					if(view.value == 0, {
						classData.scope.active_(true);
					},{
						classData.scope.active_(false);
					});
				})
				.font_(font)
				.canFocus_(false)
			;

			StaticText(scopeView, Rect(pad[0] + rect.width, pad[2]+20, pad[1], 10))
				.string_("BusIn")
				.font_(font)
			;

			NumberBox(scopeView, Rect(pad[0] + rect.width, pad[2]+30, pad[1], 14))
				.action_({ arg view;
					view.value_(view.value.asInteger.clip(0, server.options.numAudioBusChannels));
					classData.scope.inBus_(view.value);
				})
				.value_(busNum)
				.font_(font)
			;

			StaticText(scopeView, Rect(pad[0] + rect.width, pad[2]+48, pad[1], 10))
				.string_("FrqScl")
				.font_(font)
			;
			PopUpMenu(scopeView, Rect(pad[0] + rect.width, pad[2]+58, pad[1], 16))
				.items_(["lin", "log"])
				.action_({ arg view;
					classData.scope.freqMode_(view.value);
					setFreqLabelVals.value(classData.scope.freqMode, 2048);
				})
				.canFocus_(false)
				.font_(font)
				.valueAction_(1)
			;

			StaticText(scopeView, Rect(pad[0] + rect.width, pad[2]+76, pad[1], 10))
				.string_("dbCut")
				.font_(font)
			;
			PopUpMenu(scopeView, Rect(pad[0] + rect.width, pad[2]+86, pad[1], 16))
				.items_(Array.series(12, 12, 12).collect({ arg item; item.asString }))
				.action_({ arg view;
					classData.scope.dbRange_((view.value + 1) * 12);
					setDBLabelVals.value(classData.scope.dbRange);
				})
				.canFocus_(false)
				.font_(font)
				.value_(7)
			;

			classData.scope
				.inBus_(busNum)
				.active_(true)
				.style_(1)
				.waveColors_([scopeColor.alpha_(1)])
				.canFocus_(false)
			;

			if (bgColor.notNil) {
				classData.scope.background_(bgColor)
			};

			scopeWindow.onClose_({
				classData.scope.kill;
				classData.scopeOpen = false;
			}).front;
		});
	}
}
