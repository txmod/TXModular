// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSystem1GUI {		// system module 1 GUI

	classvar <w; // Window

	*makeWindow {
		var holdScreenHeight, holdScreenWidth;

		holdScreenHeight = Window.screenBounds.height;
		holdScreenWidth = Window.screenBounds.width;

		w = Window("TX Modular System",
			Rect(0, holdScreenHeight - TXSystem1.dataBank.mainWindowHeight - 30, TXSystem1.dataBank.mainWindowWidth,
				TXSystem1.dataBank.mainWindowHeight),
			scroll: true);
		w.front;
		//w.view.resize_(5);
		w.view.decorator = FlowLayout(w.view.bounds);
		w.view.background = TXSystem1.dataBank.windowColour;
		w.view.action = {arg view; TXSystem1.dataBank.windowVisibleOrigin = view.visibleOrigin;};
		w.view.mouseWheelAction = {arg view; TXSystem1.dataBank.windowVisibleOrigin = view.visibleOrigin;};
		w.alpha = TXSystem1.dataBank.windowAlpha;
		w.acceptsMouseOver = true;
		this.setWindowImage;

		w.onClose = {
			TXSystem1.closeSystem;
		};
	}

	*setWindowImage {
		if (GUI.current.asSymbol == \cocoa, {
			{	// if relevent, add background image
				if (TXSystem1.dataBank.imageFileName.notNil and: {TXSystem1.dataBank.displayModeIndex > 0}, {
					if (TXSystem1.dataBank.holdImage == nil, {
						TXSystem1.dataBank.holdImage = SCImage.open(TXPath.convert(TXSystem1.dataBank.imageFileName));
					});
					w.view.backgroundImage_(TXSystem1.dataBank.holdImage, TXSystem1.dataBank.displayModeIndex);
				}, {
					w.view.backgroundImage_(SCImage.new(1), 1);
				});
			}.defer;
		});
	}

	*windowToFront {
		{w.front}.defer;
	}


	*closeWindow {
		{
			if (w.notNil and: {w.isClosed.not}, {
				w.close;
			});
		}.defer;
	}

	*deferRemoveView {arg holdView;
		if (holdView.notNil, {
			if (holdView.notClosed, {
				holdView.visible_(false);
				holdView.focus(false);
				{holdView.remove}.defer(1);
			});
		});
	}
	*addImageDialog  {
		var holdString;
		// get path/filenames
		Dialog.openPanel({ arg path;
			var holdFile;
			holdFile = SCImage.open(path);
			if (holdFile.isNil, {
				TXInfoScreen.new(
					"Error: the following is not a valid image file:",
					arrInfoLines: [path]
				);
			},{
				//
				TXSystem1.dataBank.imageFileName = path;
				TXSystem1.dataBank.holdImage = holdFile;
				this.setWindowImage;
				TXSystem1.saveSystemSettings;
			});
			// recreate view
			TXSystem1.showView;
		}, nil, false);
	}

	/////////////////////////////////////

	*buildGUI {	// this creates the view
		var btnTitle, buttonLabels, sliderVol, btnVol;
		var btnHelp, btnOpenSystem, btnOpenRecent, btnSaveSystem, btnNewSystem, btnRebuildSystem, btnCloseSystem;
		var btnAllNotesOff, popNewModule, btnAddModule, btnFrontBack, frontText, backText;
		var btnBack, btnForward, btnClipboard, popupTools;
		var frontColour, backColour, frontTextColour, backTextColour, holdColor;
		var popMeters, holdMeter, txtArrow, btnTestNote, volumeSpec;

		// if not closing down update screen
		if (TXSystem1.dataBank.closingDown == false, {
			// wait for server sync
			// run Routine in AppClock
			Routine.run ({
				TXSystem1.server.sync;
				// clear array
				TXSystem1.clearScreenUpdFuncs;
				// deactivate any meter, midi and keydown functions
				TXChannelRouting.removeAllMeterResponders;
				TXFrontScreen.midiDeActivate;
				TXFrontScreen.keyDownDeActivate;
				// set globalKeyDownAction
				View.globalKeyDownAction = { arg view,char,modifiers,unicode,keycode;
					TXFrontScreen.runKeyDownActionFunctions (char,modifiers,unicode,keycode);
				};
				// clear boxes
				if (TXSystem1.dataBank.headerBox.notNil, {
					this.deferRemoveView(TXSystem1.dataBank.headerBox);
				});
				if (TXSystem1.dataBank.viewBox.notNil, {
					this.deferRemoveView(TXSystem1.dataBank.viewBox);
				});
				w.view.decorator.reset;
				w.refresh;
				// if showSystemControls is on create headerBox to display system controls
				if (TXSystem1.showSystemControls == 1, {
					// prepare to display header
					TXSystem1.dataBank.headerBox = CompositeView(w,Rect(0,0,1420,95));
					TXSystem1.dataBank.headerBox.decorator = FlowLayout(TXSystem1.dataBank.headerBox.bounds);

					// system title
					btnTitle = Button(TXSystem1.dataBank.headerBox,Rect(0,0,130,27));
					//btnTitle.font_(Font.new(Font.defaultSansFace,16));
					btnTitle.font_(Font.new("Helvetica", 16));
					btnTitle.states = [["TX Modular " ++ TXSystem1.dataBank.systemVersion, TXColor.sysGuiCol1,
						TXColor.white]];
					btnTitle.action = {arg view;
						var windowPoint = view.mapToGlobal(36 @ 27);
						var holdLeft = windowPoint.x;
						var holdTop = Window.screenBounds.height - TXHelpScreen.classData.defaultHeight - windowPoint.y;
						TXHelpScreen.openFile("TX_Links", inLeft: holdLeft, inTop: holdTop);
					};
					// Row of system buttons
					// button - help
					btnHelp = Button(TXSystem1.dataBank.headerBox, 36 @ 27);
					btnHelp.states = [["Help", TXColor.white, TXColor.sysHelpCol]];
					btnHelp.action = {arg view;
						var windowPoint = view.mapToGlobal(36 @ 27);
						TXHelpScreen.open(inLeft: windowPoint.x,
							inTop: Window.screenBounds.height - TXHelpScreen.classData.defaultHeight - windowPoint.y);
					};
					// button - Open file
					btnOpenSystem = Button(TXSystem1.dataBank.headerBox, 60 @ 27);
					btnOpenSystem.states = [["Open File", TXColor.white, TXColor.sysGuiCol2]];
					btnOpenSystem.action = {
						var newPath, newFile, newString, newData;
						if (TXSystem1.server.serverRunning.not, {TXSystem1.server.boot});
						Dialog.openPanel({ arg path;
							// reload system settings
							TXSystem1.loadSystemSettings;
							this.setWindowImage;
							TXSystem1.loadFileName(path);
						});
					};
					// button - Open recent
					btnOpenRecent = Button(TXSystem1.dataBank.headerBox, 76 @ 27);
					btnOpenRecent.states = [["Open Recent", TXColor.white, TXColor.sysGuiCol2]];
					btnOpenRecent.action = {arg view;
						var newPath, newFile, newString, newData;
						var windowPoint = view.mapToGlobal(76 @ 27);
						if (TXSystem1.server.serverRunning.not, {TXSystem1.server.boot});
						TXOpenFileScreen.new(TXSystem1, TXSystem1.dataBank.recentFileNames,
							inLeft: windowPoint.x,
							inTop: Window.screenBounds.height - TXOpenFileScreen.defaultSize.y - windowPoint.y);
					};
					// button - save file
					btnSaveSystem = Button(TXSystem1.dataBank.headerBox, 58 @ 27);
					btnSaveSystem.states = [["Save File", TXColor.white, TXColor.sysGuiCol2]];
					btnSaveSystem.action = {
						var newPath, newFile, newData;
						Dialog.savePanel({ arg path;
							newPath = path;
							// post message
							("TX Saving File: " ++ newPath).postln;
							newData = TXSystem1.saveData;
							newFile = File(newPath,"w");
							newFile << "#" <<< newData << "\n";
							//	use file as an io stream
							//	<<< means store the compile string of the object
							//	<< means store a print string of the object
							newFile.close;
							TXSystem1.dataBank.stTextFileName.string = "    File name: " ++ newPath;
							TXSystem1.dataBank.holdFileName = "    File name: " ++ newPath;
							w.front;
							TXSystem1.storeRecentFileName(newPath);
						});
					};
					// button - rebuild system
					btnRebuildSystem = Button(TXSystem1.dataBank.headerBox, 94 @ 27);
					btnRebuildSystem.states = [["Rebuild System", TXColor.white, TXColor.sysDeleteCol]];
					btnRebuildSystem.action = {
						TXSystem1.rebuildAllModules;
					};
					// button - clear system
					btnNewSystem = Button(TXSystem1.dataBank.headerBox, 86 @ 27);
					btnNewSystem.states = [["Clear System", TXColor.white, TXColor.sysDeleteCol]];
					btnNewSystem.action = {
						// confirm before action
						TXInfoScreen.newConfirmWindow(
							{
								TXSystem1.emptySystem;
								TXSystem1.loadSystemSettings;
								this.setWindowImage;
								TXSystem1.dataBank.holdFileName = " ";
								// reset layout positions of all busses
								TXSystem1.initBusPositions;
								// update view
								TXSystem1.showView;
							},
							"Are you sure you want to clear the system?"
						);
					};
					// button - Quit
					btnCloseSystem = Button(TXSystem1.dataBank.headerBox, 36 @ 27);
					btnCloseSystem.states = [["Quit", TXColor.white, TXColor.sysDeleteCol]];
					btnCloseSystem.action = {
						// confirm before action
						TXInfoScreen.newConfirmWindow(
							{
								// if standalone system then quit completely else just close TX
								if (TXSystem1.txStandAlone == 1, {
									TXSystem1.quitStandalone;
								},{
									{w.close;}.defer;
								});
							},
							"Are you sure you want to quit?"
						);
					};
					// button - sync start
					Button(TXSystem1.dataBank.headerBox, 64 @ 27)
					.states_([
						["Sync Start", TXColor.white, TXColor.sysGuiCol2]
					])
					.action_({|view|
						// run action function
						TXSystem1.syncStart;
					});
					// button - sync stop
					Button(TXSystem1.dataBank.headerBox, 64 @ 27)
					.states_([
						["Sync Stop", TXColor.white, TXColor.sysGuiCol2]
					])
					.action_({|view|
						// run action function
						TXSystem1.syncStop;
					});
					// button - stop all sequencers
					Button(TXSystem1.dataBank.headerBox, 54 @ 27)
					.states_([
						["Stop All", TXColor.white, TXColor.sysGuiCol2]
					])
					.action_({|view|
						// run action function
						TXSystem1.stopAllSyncModules;
						TXSystem1.showView;
					});
					// button - Panic! - all notes off
					btnAllNotesOff = Button(TXSystem1.dataBank.headerBox, 114 @ 27)
					.states_([["Panic! All Notes Off", TXColor.white, TXColor.sysDeleteCol]])
					.action_({ TXSystem1.allNotesOff; });

					// test note button
					btnTestNote = Button(TXSystem1.dataBank.headerBox, 84 @ 27);
					btnTestNote.states = [["Play Test Note", TXColor.white, TXColor.sysGuiCol2]];
					btnTestNote.action = {
						{ (EnvGen.kr(Env.sine(2,1), 1.0, doneAction: 2)
							* Saw.ar(440, 0.1))!8
						}.play;
					};
					TXSystem1.dataBank.headerBox.decorator.shift(20, 0);
					// text
					StaticText(TXSystem1.dataBank.headerBox, 30 @ 27)
					.string_("Vol")
					.background_(TXColor.sysGuiCol2)
					.stringColor_(TXColor.white)
					.font_(Font.new("Helvetica", 13))
					.align_('center');
					// volume slider
					volumeSpec = [ -inf, 6, \db].asSpec;
					sliderVol = Slider(TXSystem1.dataBank.headerBox, 138 @ 27)
					.background_(TXColor.sysGuiCol2)
					//	.align_(\right)
					.knobColor_(TXColor.white)
					.thumbSize_ (8)
					.value_(volumeSpec.unmap(TXSystem1.dataBank.volume))
					.action_({arg view;
						var holdVol;
						holdVol = volumeSpec.map(view.value);
						TXSystem1.dataBank.volume = holdVol;
						TXSystem1.server.volume = holdVol;
						holdVol = holdVol.round(0.1).asString ++ " dB";
						btnVol.states = [[holdVol, TXColor.white, TXColor.sysGuiCol2]];
					});
					// button
					btnVol = Button(TXSystem1.dataBank.headerBox, 50 @ 27);
					btnVol.states = [[TXSystem1.dataBank.volume.round(0.1).asString ++ " dB",
						TXColor.white, TXColor.sysGuiCol2]];
					btnVol.action = {
						TXSystem1.dataBank.volume = 0;
						TXSystem1.server.volume = 0;
						sliderVol.value_(volumeSpec.unmap(0));
						btnVol.states = [["0 dB", TXColor.white, TXColor.sysGuiCol2]];
					};
					// spacing
					TXSystem1.dataBank.headerBox.decorator.nextLine;
					TXSystem1.dataBank.headerBox.decorator.shift(0, 6);

					// history buttons
					if (TXSystem1.dataBank.historyIndex > 0, {
						holdColor = TXColor.white;
					},{
						holdColor = TXColor.grey(0.3);
					});
					// button
					btnBack = Button(TXSystem1.dataBank.headerBox, 18 @ 24);
					btnBack.states = [["<", holdColor, TXColor.sysGuiCol1]];
					btnBack.action = {TXSystem1.shiftHistory(-1);};
					if (TXSystem1.dataBank.historyIndex < (TXSystem1.dataBank.historyEvents.size - 1), {
						holdColor = TXColor.white;
					},{
						holdColor = TXColor.grey(0.3);
					});
					// button
					btnForward = Button(TXSystem1.dataBank.headerBox, 18 @ 24);
					btnForward.states = [[">", holdColor, TXColor.sysGuiCol1]];
					btnForward.action = {TXSystem1.shiftHistory(1);};

					TXSystem1.dataBank.headerBox.decorator.shift(20, 0);
					// text
					StaticText(TXSystem1.dataBank.headerBox, 64 @ 24)
					.string_("System:")
					.background_(TXColor.sysLabelBackground)
					.stringColor_(TXColor.white)
					//.font_(Font.new("Helvetica", 13))
					.align_('center');

					// display system buttons
					buttonLabels = ["Modules & Channels", "Signal Flow", "Sample Banks",
						"Loop Banks", "Notes & Options"];
					buttonLabels.do({arg item, i;
						var holdButton, holdBoxColour, holdTextColour;
						if (TXSystem1.showWindow == item.asSymbol, {
							//holdBoxColour = TXColor.sysGuiCol4;
							holdBoxColour = TXColor.sysGuiCol1;
							holdTextColour = TXColor.paleYellow2;
						},{
							holdBoxColour = TXColor.sysGuiCol1;
							holdTextColour = TXColor.white;
						});
						holdButton = Button(TXSystem1.dataBank.headerBox, 122 @ 24);
						holdButton.states = [[item, holdTextColour, holdBoxColour]];
						holdButton.action = {
							TXSystem1.showWindow = item.asSymbol;
							TXSystem1.showFrontScreen = false;
							TXSystem1.addHistoryEvent;
							TXSystem1.showView;
						};
					});
					// spacing
					TXSystem1.dataBank.headerBox.decorator.shift(20, 0);
					// text
					StaticText(TXSystem1.dataBank.headerBox, 70 @ 24)
					.string_("Interface:")
					.background_(TXColor.sysLabelBackground)
					.stringColor_(TXColor.white)
					//.font_(Font.new("Helvetica", 13))
					.align_('center');

					// display Interface buttons
					buttonLabels = ["Run Interface", "Design Interface"];
					buttonLabels.do({arg item, i;
						var holdButton, holdBoxColour, holdTextColour;
						if (TXSystem1.showWindow == item.asSymbol, {
							//holdBoxColour = TXColor.sysGuiCol4;
							holdBoxColour = TXColor.sysGuiCol1;
							holdTextColour = TXColor.paleYellow2;
						},{
							holdBoxColour = TXColor.sysGuiCol1;
							holdTextColour = TXColor.white;
						});
						holdButton = Button(TXSystem1.dataBank.headerBox, 122 @ 24);
						holdButton.states = [[item, holdTextColour, holdBoxColour]];
						holdButton.action = {
							TXSystem1.showWindow = item.asSymbol;
							TXSystem1.showFrontScreen = true;
							TXSystem1.addHistoryEvent;
							TXSystem1.showView;
						};
					});
					// spacing
					TXSystem1.dataBank.headerBox.decorator.shift(34, 0);

					// Clipboard button
					// btnClipboard = Button(TXSystem1.dataBank.headerBox, 112 @ 24);
					// btnClipboard.states = [["Open Clipboard", TXColor.white, TXColor.sysGuiCol2]];
					// btnClipboard.action = {TXClipboardWindow.showWindow;};

					// Tools
					popupTools = PopUpMenu(TXSystem1.dataBank.headerBox, 112 @ 24);
					popupTools.items = [ "Tool Windows...",
						"Clipboard",
						"MIDI Keyboard",
						"Colour Picker",
						"Curve Builder",
						"WaveTerrain Builder",
					];
					popupTools.background_(TXColor.sysGuiCol1);
					popupTools.stringColor_(Color.white);
					popupTools.action = { arg view;
						[ {nil},
							{TXClipboardWindow.showWindow},
							{TXMIDIKeyboardWindow.showWindow},
							{TXColor.showPicker},
							{TXCurveBuilder.showWindow},
							{TXWaveTerrainBuilder.showWindow},
						] [view.value].value;
						view.value = 0;
					};

					// spacing
					TXSystem1.dataBank.headerBox.decorator.nextLine;
					TXSystem1.dataBank.headerBox.decorator.shift(0, 4);
					//				if (GUI.current.asSymbol == \SwingGUI, {
					//					TXSystem1.dataBank.stTextFileName .font_(JFont("Gill Sans", 11));
					//				},{
					//					TXSystem1.dataBank.stTextFileName .font_(Font("Gill Sans", 11));
					//				});
					// static text - file name
					TXSystem1.dataBank.stTextFileName = StaticText(TXSystem1.dataBank.headerBox, 1256 @ 24);
					if (TXSystem1.dataBank.loadingDataFlag, {
						TXSystem1.dataBank.stTextFileName.string_("OPENING FILE ... ")
						.background_(TXColor.orange)
						.stringColor_(TXColor.white);
					},{
						TXSystem1.dataBank.stTextFileName.string_(TXSystem1.dataBank.holdFileName)
						.background_(TXColor.sysLabelBackground)
						.stringColor_(TXColor.white);
					});
				},{
					// system title
					btnTitle = Button(w,Rect(0,0,140,26))
					.font_(Font.new("Helvetica-Bold",16));
					btnTitle.states = [["TX Modular " ++ TXSystem1.dataBank.systemVersion, TXColor.sysGuiCol1,
						TXColor.white]];
					btnTitle.action = {
						"TX_Modular_Standalone_Links".openHelpFile;
					};
					// spacing
					w.view.decorator.shift(60, 0);
					// button - Quit
					btnCloseSystem = Button(w, 60 @ 27);
					btnCloseSystem.states = [["Quit", TXColor.white, TXColor.sysGuiCol1]];
					btnCloseSystem.action = {
						// confirm before action
						TXInfoScreen.newConfirmWindow(
							{
								// if standalone system then quit completely else just close TX
								if (TXSystem1.txStandAlone == 1, {
									TXSystem1.quitStandalone;
								},{
									{w.close;}.defer;
								});
							},
							"Are you sure you want to quit?"
						);
					};
					// spacing
					w.view.decorator.shift(60, 0);
					// text
					StaticText(w, Rect(0,0, 60, 27))
					.string_("Volume")
					.background_(TXColor.sysGuiCol1)
					.stringColor_(TXColor.white)
					.font_(Font.new("Helvetica", 13))
					.align_('center');
					// volume slider
					volumeSpec = [ -inf, 6, \db].asSpec;
					sliderVol = Slider(TXSystem1.dataBank.headerBox, 138 @ 27)
					.background_(TXColor.sysGuiCol2)
					//	.align_(\right)
					.knobColor_(TXColor.white)
					.thumbSize_ (6)
					.value_(volumeSpec.unmap(TXSystem1.dataBank.volume))
					.action_({arg view;
						var holdVol;
						holdVol = volumeSpec.map(view.value);
						TXSystem1.dataBank.volume = holdVol;
						TXSystem1.server.volume = holdVol;
						holdVol = holdVol.round(0.1).asString ++ " dB";
						btnVol.states = [[holdVol, TXColor.white, TXColor.sysGuiCol2]];
					});
					// button
					btnVol = Button(TXSystem1.dataBank.headerBox, 50 @ 27);
					btnVol.states = [[TXSystem1.dataBank.volume.round(0.1).asString ++ " dB",
						TXColor.white, TXColor.sysGuiCol2]];
					btnVol.action = {
						TXSystem1.dataBank.volume = 0;
						TXSystem1.server.volume = 0;
						sliderVol.value_(volumeSpec.unmap(0));
						btnVol.states = [["0 dB", TXColor.white, TXColor.sysGuiCol2]];
					};
				});	// end of headerBox creation

				// create viewBox to display selected window
				TXSystem1.dataBank.viewBox = CompositeView(w, Rect(0, 0, TXSystem1.dataBank.viewBoxWidth, TXSystem1.dataBank.viewBoxHeight)).resize_(5);
				if (TXSystem1.showFrontScreen == false, {
					TXSystem1.dataBank.viewBox.decorator = FlowLayout(TXSystem1.dataBank.viewBox.bounds);
				});
				if (TXSystem1.showWindow == 'Modules & Channels', {TXChannelRouting.makeGui(TXSystem1.dataBank.viewBox);});
				//	OLD:   if (TXSystem1.showWindow == 'Modules & Channels', {TXModGui.makeGui(TXSystem1.dataBank.viewBox);});
				//	OLD:   if (TXSystem1.showWindow == 'Sequencers', {TXSeqGui.makeGui(TXSystem1.dataBank.viewBox);});
				//	OLD:   if (TXSystem1.showWindow == 'Signal Flow', {TXSignalFlow.makeGui(TXSystem1.dataBank.viewBox);});
				if (TXSystem1.showWindow == 'Signal Flow', {TXChannelRouting.makeGui(TXSystem1.dataBank.viewBox, showSigFlow: true);});
				if (TXSystem1.showWindow == 'Sample Banks', {TXBankBuilder2.makeSampleGui(TXSystem1.dataBank.viewBox);});
				if (TXSystem1.showWindow == 'Loop Banks', {TXBankBuilder2.makeLoopGui(TXSystem1.dataBank.viewBox);});
				if (TXSystem1.showWindow == 'Notes & Options', {this.guiViewNotes});
				if (TXSystem1.showFrontScreen == true, {TXFrontScreen.makeGui(TXSystem1.dataBank.viewBox, TXSystem1.showWindow.asString)});

				/*// removed
				// make or close Gui Properties window
				if (TXSystem1.showFrontScreen == true
					and: (TXSystem1.showWindow == 'Design Interface')
					and: (TXFrontScreen.classData.showGuiProperties == true)
					, {
						TXFrontScreenGuiProperties.makeGui(TXSystem1);
				}, {
					TXFrontScreenGuiProperties.closeWindow;
				});*/

				w.front;
			}, clock: AppClock); // end of Routine.run
		}); // end of if
		// update visibleOrigin
		{w.view.visibleOrigin_(TXSystem1.dataBank.windowVisibleOrigin);}.defer(0.1);
	} // end of method buildGUI

	/////////////////////////////////////

	*guiViewNotes {
		var noteView, btnUpdateIP, btnDefault, screenColourBox, screenColourPopup, holdView;
		var arrAllSourceActionModules, arrAllSourceActionModNames, modulesScrollView, modulesBox, btnDelete;
		var modListBox, modListBoxWidth, modListBoxHeight;
		var displayModePopupView, displayModeNumberView, displayModeItems;
		var addImageButton, delImageButton, screenUpdRateSldr;
		var imageNameText;

		arrAllSourceActionModules = TXSystem1.arrSystemModules
		.select({ arg item, i;
			(item.class.moduleType == "source")
			or:
			(item.class.moduleType == "groupsource")
			or:
			(item.class.moduleType == "groupaction")
			or:
			(item.class.moduleType == "action")
			or:
			(item.class.moduleType == "insert") ;
		})
		.sort({ arg a, b;
			TXSystem1.adjustNameForSorting(a.instName) < TXSystem1.adjustNameForSorting(b.instName);
		});
		arrAllSourceActionModNames = arrAllSourceActionModules
		.collect({arg item, i;  item.instName; });
		//  spacer
		TXSystem1.dataBank.viewBox.decorator.shift(40,0);
		// create note fields with titles
		noteView =  CompositeView(TXSystem1.dataBank.viewBox,Rect(0,0,1200,800));
		noteView.decorator = FlowLayout(noteView.bounds);
		//  spacer
		noteView.decorator.shift(0,20);
		// text
		StaticText(noteView, 100 @ 30).string_("OPTIONS").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0, 10);

		/* IMAGE REMOVED FOR NOW
		// label - background image
		StaticText(noteView, Rect(0, 0, 120, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
		.align_(\right)
		.string_("Background Image" );
		// text - image file name
		imageNameText = StaticText(noteView, Rect(0, 0, 300, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
		.align_(\left);
		imageNameText.string = (TXSystem1.dataBank.imageFileName ? " ").keep(60);
		// button - add image
		addImageButton = Button(noteView, Rect(0, 0, 80, 20));
		addImageButton.states = [
			["Add Image", TXColor.white, TXColor.sysGuiCol1]
		];
		addImageButton.action = {this.addImageDialog;};
		// button - delete image
		delImageButton = Button(noteView, Rect(0, 0, 80, 20));
		delImageButton.states = [
			["Delete Image", TXColor.white, TXColor.black]
		];
		delImageButton.action = {
			TXSystem1.dataBank.imageFileName = nil;
			TXSystem1.dataBank.holdImage = nil;
			this.setWindowImage;
			imageNameText.string = " ";
			TXSystem1.saveSystemSettings;
		};
		// label -  image mode
		StaticText(noteView, Rect(0, 0, 90, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
		.align_(\right)
		.string_("Image mode" );
		//display mode
		displayModeItems = [
			"0 - off - image not shown, show window colour",
			"1 - fix left, fix top - default",
			"2 - tile horizontally, fix top",
			"3 - fix right, fix top",
			"4 - fix left, tile vertically",
			"5 - tile horizontally, tile vertically",
			"6 - fix right, tile vertically",
			"7 - fix left, fix bottom",
			"8 - tile horizontally, fix bottom",
			"9 - fix right, fix bottom",
			"10 - stretch horizontally & vertically to fit",
			"11 - center horizontally , center vertically & scale",
			"12 - center horizontally , fix top",
			"13 - center horizontally , fix bottom",
			"14 - fix left, center vertically",
			"15 - fix right, center vertically",
			"16 - center horizontally, center vertically - no scale",
		];
		// number box - display mode
		displayModeNumberView = NumberBox(noteView, Rect(0, 0, 20, 20))
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
		.scroll_(false)
		.action_({arg view;
			var holdValue;
			holdValue = view.value.clip(0,16);
			TXSystem1.dataBank.displayModeIndex = holdValue;
			TXSystem1.saveSystemSettings;
			this.setWindowImage;
			// update view
			TXSystem1.showView;
		});
		displayModeNumberView.value = TXSystem1.dataBank.displayModeIndex;
		// popup - display mode
		displayModePopupView = PopUpMenu(noteView, Rect(0, 0, 20, 20))
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
		.items_(displayModeItems)
		.action_({arg view;
			TXSystem1.dataBank.displayModeIndex = view.value;
			TXSystem1.saveSystemSettings;
			this.setWindowImage;
			// update view
			TXSystem1.showView;
		});
		displayModePopupView.value = TXSystem1.dataBank.displayModeIndex;
		*/

		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0, 10);
		// text
		StaticText(noteView, 120 @ 20)
		.string_("Window Colour")
		.align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		// screenColourbox
		screenColourBox = DragBoth.new(noteView, 34 @ 20);
		screenColourBox.background_(w.view.background);
		screenColourBox.beginDragAction_({ arg view, x, y;
			view.dragLabel_("Colour");
			screenColourBox.background;
		});
		screenColourBox.canReceiveDragHandler = {
			View.currentDrag.isKindOf( Color )
		};
		screenColourBox.receiveDragHandler = {
			var holdDragObject;
			holdDragObject = View.currentDrag;
			w.view.background = holdDragObject;
			TXSystem1.dataBank.windowColour = holdDragObject;
			screenColourBox.background_(holdDragObject);
			TXSystem1.saveSystemSettings;
			// update view
			w.refresh;
		};
		// button
		btnDefault = Button(noteView, 60 @ 20);
		btnDefault.states = [["Default", TXColor.white, TXColor.sysMainWindow]];
		btnDefault.action = {
			w.view.background = TXColor.sysMainWindow;
			screenColourBox.background_(w.view.background);
			TXSystem1.dataBank.windowColour = w.view.background;
			TXSystem1.saveSystemSettings;
		};

		// colourPickerButton
		Button(noteView, 60 @ 20)
		.states_([["Picker", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			TXColor.showPicker;
		});
		// popup - screenColour presets
		screenColourPopup = PopUpMenu(noteView, 140 @ 20)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
		.items_(["Presets"] ++ TXColor.colourNames)
		.action_({arg view;
			if (view.value > 0, {
				w.view.background =
				TXColor.perform(TXColor.colourNames.at(view.value - 1).asSymbol).copy;
				screenColourBox.background_(w.view.background);
				TXSystem1.dataBank.windowColour = w.view.background;
				TXSystem1.saveSystemSettings;
			});
		});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0, 10);
		// text
		StaticText(noteView, 140 @ 20)
		.string_("Window Transparancy")
		.align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		// buttons
		6.do({ arg i;
			var btn, col1, col2;
			if (TXSystem1.dataBank.windowAlpha == (1 - (0.1 * i)), {
				col2 = TXColor.white;
				col1 = TXColor.sysGuiCol1;
			},{
				col1 = TXColor.white;
				col2 = TXColor.sysGuiCol1;
			});
			btn = Button(noteView, 50 @ 20);
			btn.states = [[["0 %", "10 %", "20 %", "30 %", "40 %", "50 %"][i], col1, col2]];
			btn.action = {
				w.alpha = 1 - (0.1 * i);
				TXSystem1.dataBank.windowAlpha = 1 - (0.1 * i);
				TXSystem1.saveSystemSettings;
				TXSystem1.showView;
			};
		});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0, 10);
		// text
		StaticText(noteView, 360 @ 20)
		.string_("Ask for confirmation before deleting Modules and Channels?")
		.align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		// checkbox
		TXCheckBox(noteView, 70 @ 20, "", TXColor.sysGuiCol1, TXColor.grey(0.8),
			TXColor.white, TXColor.sysGuiCol1, 10)
		.action_({arg view; TXSystem1.dataBank.confirmDeletions = view.value.booleanValue;
			TXSystem1.saveSystemSettings;})
		.value_(TXSystem1.dataBank.confirmDeletions.binaryValue;);
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0, 10);
		// slider
		screenUpdRateSldr = TXSlider(noteView, 360 @ 20, "Screen update rate", ControlSpec(1, 20, step: 1),
			{arg view; TXSystem1.setScreenUpdateRate(view.value);},
			TXSystem1.dataBank.screenUpdateRate, labelWidth: 140, numberWidth: 40
		);
		screenUpdRateSldr.labelView.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		screenUpdRateSldr.labelView.align_(\center);
		screenUpdRateSldr.numberView.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1).normalColor_(TXColor.sysGuiCol1);
		// colourPickerButton
		Button(noteView, 60 @ 20)
		.states_([["Default", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			screenUpdRateSldr.valueAction_(5);
		});
		// checkbox
		/*TXCheckBox(noteView, 70 @ 20, "", TXColor.sysGuiCol1, TXColor.grey(0.8),
			TXColor.white, TXColor.sysGuiCol1, 10)
		.action_({arg view; TXSystem1.dataBank.confirmDeletions = view.value.booleanValue;
			TXSystem1.saveSystemSettings;})
		.value_(TXSystem1.dataBank.confirmDeletions.binaryValue;);*/

		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0, 40);
		// main title
		StaticText(noteView, 100 @ 30).string_("NOTES").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		//  display Network  info
		// OLD:
		// StaticText(noteView, 500 @ 20)
		// .string_("Network IP address for receiving OSC messages:  "
		// 	++ TXSystem1.dataBank.ipAddress.asString ++
		// "      Network Port:  " ++ NetAddr.langPort.asString)
		StaticText(noteView, 300 @ 20)
		.string_("Network Port for receiving OSC messages:  " ++ NetAddr.langPort.asString)
		.align_(\center)
		.background_(TXColor.sysChannelHighlight)
		.stringColor_(TXColor.sysGuiCol1);
		//	// button
		//	btnUpdateIP = Button(noteView, 140 @ 20);
		//	btnUpdateIP.states = [["Update Network Info", TXColor.white, TXColor.sysGuiCol1]];
		//	btnUpdateIP.action = {
		//		TXSystem1.updateIPAddress;
		//	};
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		//  display all notes
		StaticText(noteView, 60 @ 20).string_("Notes 1").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 20)
		.string_(TXSystem1.dataBank.notes0)
		.action_({arg view; TXSystem1.dataBank.notes0 = view.value;});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		// text
		StaticText(noteView, 60 @ 20).string_("Notes 2").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 20)
		.string_(TXSystem1.dataBank.notes1)
		.action_({arg view; TXSystem1.dataBank.notes1 = view.value;});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		// text
		StaticText(noteView, 60 @ 20).string_("Notes 3").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 20)
		.string_(TXSystem1.dataBank.notes2)
		.action_({arg view; TXSystem1.dataBank.notes2 = view.value;});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		// text
		StaticText(noteView, 60 @ 20).string_("Notes 4").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 20)
		.string_(TXSystem1.dataBank.notes3)
		.action_({arg view; TXSystem1.dataBank.notes3 = view.value;});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		// text
		StaticText(noteView, 60 @ 20).string_("Notes 5").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 20)
		.string_(TXSystem1.dataBank.notes4)
		.action_({arg view; TXSystem1.dataBank.notes4 = view.value;});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		// text
		StaticText(noteView, 60 @ 20).string_("Notes 6").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 20)
		.string_(TXSystem1.dataBank.notes5)
		.action_({arg view; TXSystem1.dataBank.notes5 = view.value;});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		// text
		StaticText(noteView, 60 @ 20).string_("Notes 7").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 20)
		.string_(TXSystem1.dataBank.notes6)
		.action_({arg view; TXSystem1.dataBank.notes6 = view.value;});
		//  spacer
		noteView.decorator.nextLine;
		noteView.decorator.shift(0,10);
		// text
		StaticText(noteView, 60 @ 20).string_("Notes 8").align_(\center)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		TextField(noteView, 700 @ 20)
		.string_(TXSystem1.dataBank.notes7)
		.action_({arg view; TXSystem1.dataBank.notes7 = view.value;});

		/* new code for multiple deletions. Not currently needed since moved to modules page

			//  decorator.reset
			noteView.decorator.reset;
			noteView.decorator.shift(800,20);

			// make box
			modListBoxWidth = 250;
			modListBoxHeight = 600;
			modListBox =  CompositeView(noteView, Rect(0,0, modListBoxWidth, modListBoxHeight));
			modListBox.background = TXColor.sysChannelAudio;
			modListBox.decorator = FlowLayout(modListBox.bounds);
			// Heading
			holdView = StaticText(modListBox, Rect(0,0, 168, 30));
			holdView.string = "All System Modules";
			holdView.stringColor_(TXColor.sysGuiCol4).background_(TXColor.white);
			holdView.setProperty(\align,\center);

			modListBox.decorator.nextLine;
			modListBox.decorator.shift(0, 10);

			modulesScrollView = ScrollView(modListBox, Rect(0,0, modListBoxWidth-8, modListBoxHeight-38))
			.hasBorder_(false).autoScrolls_(false);
			modulesScrollView.action = {
			arg view; TXSystem1.dataBank.modulesVisibleOrigin = view.visibleOrigin;
			};
			modulesBox = CompositeView(modulesScrollView,
			Rect(0,0, modListBoxWidth-14, (arrAllSourceActionModNames.size * 30).max(20)));
			modulesBox.decorator = FlowLayout(modulesBox.bounds);

			arrAllSourceActionModNames.do({arg item, i;
			var strModule, btnDelete;
			// button -  module
			strModule = StaticText(modulesBox, 140 @ 20);
			strModule.string = item;
			strModule.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			strModule.setProperty(\align,\center);
			// button -  delete
			btnDelete = Button(modulesBox, 24 @ 20);
			btnDelete.states = [["Del", TXColor.white, TXColor.sysDeleteCol]];
			btnDelete.action = {arrAllSourceActionModules.at(i).confirmDeleteModule; TXSystem1.showView;};
			});
			modulesScrollView.visibleOrigin = TXSystem1.dataBank.modulesVisibleOrigin;
		*/

	}  // end of method guiViewNotes

}
