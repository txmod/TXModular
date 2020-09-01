// Copyright (C) 2015  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXGuiBuild2 {		// Gui builder for modules - called by TXModuleBase:baseOpenGui

	// ========================================================================
	/* 	NOTE - the following code generates the list below:
	(
	a = TXGuiBuild2.class.methods;
	b = a.collect ({ arg item, i; item.name.asString;});
	c = b.select ({ arg item, i; item.keep(3) == "gui";});
	d = c.collect ({ arg item, i; item.copyToEnd(3);});
	d.sort.do ({ arg item, i; ("// " ++ item).postln;});
	" ".postln;
	)
	*/
	// ========================================================================

	//  TXGuiBuild2 can respond to the following guiSpecArray items:

	// ==========================================================================================
	// INDEX (these are all now class methods with the prefix "gui", such as "guiActionButton"
	// ==========================================================================================
	// ActionButton
	// ActionButtonBig
	// ActionButtonDark
	// ActionButtonDarkBig
	// ActionPopup
	// allNotesOffButton
	// DeleteModuleButton
	// DragSink
	// DragDrop
	// DividingLine
	// EZNumber
	// EZNumberMapped
	// TXScrollNumBox
	// TXScrollNumBoxUnmapped
	// EZslider
	// EZSlider
	// EZsliderUnmapped
	// HelpButton
	// HideModuleButton
	// ImageBox
	// LegacyModuleText
	// MIDIChannelSelector
	// MIDIKeyboard
	// MIDIListenCheckBox
	// MIDINote
	// MidiNoteRow
	// MIDINoteSelector
	// MidiNoteText
	// MIDIOutPortSelector
	// MIDISoloChannelSelector
	// MIDISoloControllerSelector
	// MIDIVelSelector
	// ModMatrixRow
	// ModulationOptions
	// ModuleActionPopup
	// ModuleInfoTxt
	// NextLine
	// NoteRangeSelector
	// OpenFileButton
	// OSCString
	// PolyphonySelector
	// RebuildModuleButton
	// RefreshButton
	// RefreshButtonBig
	// RunPauseButton
	// SeqNavigationButtons
	// SeqPlayRange
	// SeqScrollStep
	// SeqSelect3GroupModules
	// SeqSelectChainStep
	// SeqSelectFirstDisplayStep
	// SeqStepNoTxt
	// SeqSyncStartCheckBox
	// SeqSyncStopCheckBox
	// ShiftDecorator
	// Spacer
	// SpacerLine
	// SynthOptionCheckBox
	// SynthOptionList
	// SynthOptionListPlusMinus
	// SynthOptionPopup
	// SynthOptionPopupPlusMinus
	// TapTempoButton
	// TestLoopVals
	// TestNoteVals
	// TextBar
	// TextBarLeft
	// TextViewCompile
	// TextViewDisplay
	// TitleBar
	// Transpose
	// TX2DSlider
	// TXActionSteps
	// TXActionView
	// TXCheckBox
	// TXCompactSlider
	// TXCompactSliderUnmapped
	// TXCurveDraw
	// TXDoubleSlider
	// TXEnvDisplay
	// TXEnvPlot
	// TXEnvGui
	// TXEQCurveDraw
	// TXFraction
	// TXFractionSlider
	// TXFreqBpmMinMaxSldr
	// TXGridColourTarget
	// TXGridColourZone
	// TXGridGreyTarget
	// TXGridGreyZone
	// TXListViewAction
	// TXListViewActionPlusMinus
	// TXMidiNoteKeybGrid
	// TXMidiNoteSliderSplit
	// TXMinMaxSlider
	// TXMinMaxSliderSplit
	// TXMultiButton
	// TXMultiCheckBox
	// TXMultiKnob
	// TXMultiKnobMidiNote
	// TXMultiKnobNo
	// TXMultiKnobNoUnmap
	// TXMultiNumber
	// TXMultiSlider
	// TXMultiSliderNo
	// TXMultiSwitch
	// TXMultiTrackView
	// TXNetAddress
	// TXNoteRangeSlider
	// TXNumberPlusMinus
	// TXNumOrString
	// TXOSCTrigActions
	// TXPopupAction
	// TXPopupActionPlusMinus
	// TXPresetPopup
	// TXQCArgGui
	// TXQCArgGuiScroller
	// TXRangeSlider
	// TXSlotGui
	// TXSoundFileViewFraction
	// TXSoundFileViewRange
	// TXStaticText
	// TXTextBox
	// TXTimeBeatsBpmNumber
	// TXTimeBpmMinMaxSldr
	// TXTrackView
	// TXV_GuiScroller
	// TXWaveTableSpecs
	// WetDryMixSlider

	// define class variables
	classvar <>system;
	classvar <>classData;
	classvar argModule;

	*initClass{
		//	set class specific variables
		classData = ();
	}

	*new{ arg inModule, argParent;
		var rateColour;

		// set variables
		argModule = inModule;
		if (argModule.class.moduleRate == "control", {
			rateColour = TXColor.sysChannelControl;
		},{
			rateColour = TXColor.sysChannelAudio;
		});
		classData.arrGroupModules = system.arrSystemModules
		.select({ arg item, i; item.class.moduleType == "groupsource" or: (item.class.moduleType == "groupaction"); });
		argModule.arrControls = [];
		classData.viewWidth = argModule.class.guiWidth-50;
		// if parent is passed, then view gets created on parent's window. else make own window
		if (argParent.notNil) {
			// ignore module's height & use channel height
			classData.win =  CompositeView(argParent,Rect(0,0, argModule.class.guiWidth.max(500), 595));
			classData.win.decorator = FlowLayout(classData.win.bounds);
			//classData.win.decorator.shift(6,10);
			//classData.win.decorator.margin = Point(10,10);
			classData.win.background = rateColour;


		}{
			// if window not passed as arg make new one
			classData.win = Window(argModule.instName, Rect(100, 100, argModule.class.guiWidth, 570));
			classData.win.front;
			classData.win.view.decorator = FlowLayout(classData.win.view.bounds);
			classData.win.view.background = rateColour;
		};

		// add title items (or defaults if nil) to classData.guiSpecArray
		if (argModule.guiSpecTitleArray.notNil, {
			classData.guiSpecArray = argModule.guiSpecTitleArray ++ argModule.guiSpecArray;
		}, {
			// add defaults
			classData.guiSpecArray = [
				["TitleBar"],
				["HelpButton"],
				["RunPauseButton"],
				["DeleteModuleButton"],
				["RebuildModuleButton"],
				["HideModuleButton"],
				["LegacyModuleText"],
				["NextLine"],
				["ModuleActionPopup"],
				["NextLine"],
				["ModuleInfoTxt"],
				["SpacerLine", 2],
			]
			++ argModule.guiSpecArray;
		});
		// check whether to add "ModulationOptions"
		classData.holdVal = argModule.myArrCtlSCInBusSpecs
		.select({ arg item, i; item.at(3).notNil; });  // select items with optional controls
		if ((argModule.autoModOptions == true) and: (classData.holdVal.size > 0), {
			classData.guiSpecArray = classData.guiSpecArray
			++ [
				["ModulationOptions"]
			];
		});

		// Build GUI from classData.guiSpecArray
		classData.guiSpecArray.do({ arg item, i;
			var holdMethod;
			holdMethod = ("gui" ++ item.at(0)).asSymbol;
			this.perform(holdMethod, item, classData.win);
		});    // end of classData.guiSpecArray.do

		^classData.win;	// return classData.win
	}

	*nextline { arg w;
		classData.win.asView.decorator.nextLine;
	}

	// NextLine
	// arguments- index1 is optional action to run after nextline is executed
	*guiNextLine { arg item, w;
		this.nextline(w);
		if (item.at(1).notNil, {item.at(1).value;});
	}

	// ShiftDecorator
	// arguments- index1 is shiftHorizontal, index2 is shiftVertical
	// e.g. ["ShiftDecorator", 10, 30],
	*guiShiftDecorator { arg item, w;
		classData.holdVal = item.at(1) ? 0;
		classData.holdVal2 = item.at(2) ? 0;
		classData.win.asView.decorator.shift(classData.holdVal, classData.holdVal2);
	}

	// Spacer	 - defaults to width 40 if item.at(1) is nil
	*guiSpacer { arg item, w;
		classData.holdVal = item.at(1) ? 40;
		StaticText(w, classData.holdVal @ 20);
	}

	// SpacerLine	 - defaults to height 10 if item.at(1) is nil
	*guiSpacerLine { arg item, w;
		classData.holdVal = item.at(1) ? 10;
		classData.win.asView.decorator.nextLine;
		StaticText(w, 20 @ classData.holdVal);
		classData.win.asView.decorator.nextLine;
	}

	// DividingLine	 - defaults Width , and height
	*guiDividingLine { arg item, w;
		classData.holdVal = item.at(1) ? classData.viewWidth.max(480);
		classData.holdVal2 = item.at(2) ? 1;
		classData.win.asView.decorator.nextLine;
		StaticText(w, classData.holdVal @ classData.holdVal2).background_(TXColor.white);
	}

	// DragSink
	// arguments- index1 is string to display,
	// index2 is canReceiveDragHandler function
	// 	index3 is receiveDragHandler function
	// index 4 is optional showDraggedContent (default false)
	// index 5 is optional width (default 200)
	// index 6 is optional height (default 20)
	// index 7 is optional background color
	// index 8 is optional string color
	// index 9 is optional string alignment (default \center)
	*guiDragSink { arg item, w;
		// if (w.class == Window, {
		// 	classData.win.view.decorator.nextLine;
		// 	}, {
		// 		classData.win.decorator.nextLine;
		// });
		classData.holdView = DragSink(w, Rect(10, 70, (item[5] ? 200), (item[6] ? 20))).align_(item[9] ? \center);
		classData.holdView.string = item[1];
		classData.holdView.canReceiveDragHandler = item[2];
		classData.holdView.receiveDragHandler = item[3];
		classData.holdView.setBoth = item[4] ? false;
		classData.holdView.background_(item[7] ? TXColor.paleYellow);
		classData.holdView.stringColor_(item[8] ? TXColor.black);
	}

	// DragDrop
	// arguments- index1 is string to display,
	// index2 is canReceiveDragHandler function
	// 	index3 is receiveDragHandler function
	// index 4 is optional showDraggedContent (default false)
	// index 5 is optional width (default 100)
	// index 6 is optional height (default 20)
	// index 7 is optional begin drag action function
	// index 8 is optional drag label
	// index 9 is optional background color
	// index 10 is optional string color
	// index 11 is optional string alignment
	*guiDragDrop { arg item, w;
		classData.holdView = DragBoth(w, (item[5] ? 100) @ (item[6] ? 20));
		classData.holdView.string = item[1];
		classData.holdView.canReceiveDragHandler = item[2];
		classData.holdView.receiveDragHandler = item[3];
		classData.holdView.setBoth = item[4] ? false;
		classData.holdView.beginDragAction = item[7];
		classData.holdView.dragLabel = item[8];
		classData.holdView.background_(item[9] ? TXColor.paleYellow);
		classData.holdView.stringColor_(item[10] ? TXColor.black);
		classData.holdView.align = item[11] ? \center;
	}

	// ImageBox
	// arguments- index1 is string to display,
	// index 1 is image function
	// index 2 is optional width function (default 200)
	// index 3 is optional height function (default 200)
	*guiImageBox { arg item, w;
		// if (w.class == Window, {
		// 	classData.win.view.decorator.nextLine;
		// 	}, {
		// 		classData.win.decorator.nextLine;
		// });
		classData.holdView = UserView(w, Rect(0, 0, (item[2].value ? 200), (item[3].value ? 200)));
		classData.holdView.drawFunc = {arg view;
			var img = item[1].value;
			img.drawInRect(Rect(0, 0, (item[2].value ? 200), (item[3].value ? 200)));
		};
		classData.holdView.refresh;
	}

	// TitleBar
	*guiTitleBar { arg item, w;
		//		classData.holdView = StaticText(w, 150 @ 30);
		//		classData.holdView.string = argModule.instName;
		//		classData.holdView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.sysModuleName);
		//		classData.holdView.setProperty(\align,\center);

		classData.holdView = PopUpMenu(w, 170 @ 27);
		classData.holdView.items = system.arrSystemModules.copy
		.sort({ arg a, b; a.instSortingName < b.instSortingName })
		.collect({arg item, i; item.instDisplayName;});
		classData.holdView.stringColor_(TXColor.black).background_(TXColor.sysModuleName);
		classData.holdView.action = { arg view;
			// change current display module to new one
			TXChannelRouting.displayModule =
			system.arrSystemModules.copy
			.sort({ arg a, b; a.instSortingName < b.instSortingName }).at(view.value);
			system.addHistoryEvent; // add to history
			// refresh screen
			system.showView;
		};
		classData.holdView.value = system.arrSystemModules.copy
		.sort({ arg a, b; a.instSortingName < b.instSortingName })
		.indexOf(TXChannelRouting.displayModule)
		? 0;
	}

	// TextBar
	// center justified with default width & height of 80 & 20
	// arguments- index1 is text, index2 is optional width, index3 is optional height
	// index 4 is optional string colour, index 5 is optional background colour
	// index 6 is optional alignment (e.g. \left, \right)
	// e.g. ["TextBar", "Note - delay times shown in ms and bpm", 250]
	*guiTextBar { arg item, w;
		classData.holdView = StaticText(w, (item.at(2) ? 80) @ (item.at(3) ? 20));
		classData.holdView.string = item.at(1).value;
		classData.holdView.stringColor_(item.at(4) ? TXColor.sysGuiCol1)
		.background_(item.at(5) ? TXColor.white);
		classData.holdView.align_(item.at(6) ? \center);
	}

	// TextBarLeft
	// left justified version of TextBar with default width set to classData.viewWidth
	// arguments- index1 is text, index2 is optional width, index3 is optional height
	// index 4 is optional background colour
	// e.g. ["TextBarLeft", "Note - delay times shown in ms and bpm", 250]
	*guiTextBarLeft { arg item, w;
		classData.holdView = StaticText(w, (item.at(2) ? classData.viewWidth) @ (item.at(3) ? 20));
		classData.holdView.string = item.at(1).value;
		classData.holdView.stringColor_(TXColor.sysGuiCol1).background_(item.at(4) ? TXColor.white);
		classData.holdView.setProperty(\align,\left);
	}

	// TextViewDisplay
	//  for displaying text only - not editable
	// uneditable textview with default width set to classData.viewWidth and height to 100
	// arguments- index1 is text, index2 is optional width
	// index3 is optional height
	// index4 is optional label (this is used only by TXBuildActions)
	// e.g. ["TextViewDisplay", "This module is sdfjdsklfkjk", 300, 100, "Notes"]
	*guiTextViewDisplay { arg item, w;
		classData.holdView = TextView(w, (item.at(2) ? classData.viewWidth) @ (item.at(3) ? 100));
		classData.holdView.string = item.at(1).value;
		classData.holdView.stringColor_(TXColor.black);
		classData.holdView.background_(TXColor.white);
		classData.holdView.font_(Font.new("Helvetica",12));
		classData.holdView.canFocus = false;
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0);
				argView.string = item.at(1).value;
			}
		);
	}

	// TextViewCompile
	//  for editing text with default width set to classData.viewWidth and height to 100
	// index1 is text function,
	// index2 is function to run when code is edited
	// index3 is optional width
	// index4 is optional height
	// index5 is optional text for evaluation button
	// index6 is optional width of evaluation button
	// index7 is optional function to be valued with view as argument (e.g. for storing view to variable in module),
	// e.g. ["TextViewCompile", getTextFunc, setTextFunc, 300, 200]
	*guiTextViewCompile { arg item, w;
		var holdView;
		holdView = TextView(w, (item.at(3) ? classData.viewWidth) @ (item.at(4)-24 ? 100));
		holdView.string = item.at(1).value;
		holdView.stringColor_(TXColor.black);
		holdView.background_(TXColor.white);
		holdView.font_(Font.new("Helvetica",12));
		holdView.usesTabToFocusNextView = false;
		holdView.enterInterpretsSelection = false;
		holdView.hasVerticalScroller = true;
		holdView.hasHorizontalScroller = true;
		holdView.autohidesScrollers = true;
		holdView.syntaxColorize;
		holdView.tabWidth = 20;
		holdView.action_({|view|
			holdView.syntaxColorize;
			//"testing xxx - in TXGuiBuild2.guiTextViewCompile: ----".postln;
		});
		// value function
		if (item.at(7).notNil, {
			item.at(7).value(holdView);
		});
		// button to evaluate text
		Button(w, item.at(6) ? 130 @ 20)
		.states_([
			[item.at(5) ? "Evaluate text", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			item.at(2).value(holdView.string);
			holdView.syntaxColorize;
		});
		// add screen update function
		system.addScreenUpdFunc(
			[holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.string = item.at(1).value;
			}
		);
	}

	// HelpButton
	*guiHelpButton { arg item, w;
		Button(w, 40 @ 20)
		.states_([
			["Help", TXColor.white, TXColor.sysHelpCol]
		])
		.action_({|view|
			var windowPoint = view.mapToGlobal(36 @ 27);
			// adjust height relative to window size, 600 is height of help screen
			argModule.openHelp(inLeft: windowPoint.x, inTop: Window.screenBounds.height - 600 - windowPoint.y);
		})
	}

	// HideModuleButton
	*guiHideModuleButton { arg item, w;

		/* REMOVED FOR NOW
		Button(w, 40 @ 20)
		.states_([
		["Hide", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
		TXChannelRouting.hideModuleBox;
		})
		*/
	}

	// LegacyModuleText
	*guiLegacyModuleText { arg item, w;
		if (argModule.legacyModule == true, {
			classData.holdView = StaticText(w, 280 @ 18);
			classData.holdView.string = " * this is an earlier version of this type of module";
			classData.holdView.stringColor_(TXColor.sysGuiCol1)
			.background_(TXColor.white);
			classData.holdView.align_(\left);
		});
	}

	// DeleteModuleButton
	*guiDeleteModuleButton { arg item, w;
		Button(w, 50 @ 20)
		.states_([
			["Delete", TXColor.white, TXColor.sysDeleteCol]
		])
		.action_({|view|
			var windowPoint = view.mapToGlobal(50 @ 20);
			argModule.confirmDeleteModule(windowPoint.x,
				Window.screenBounds.height - 150 - windowPoint.y);
			// recreate view
			system.showView;
		})
	}

	// RebuildModuleButton
	*guiRebuildModuleButton { arg item, w;
		Button(w, 50 @ 20)
		.states_([
			["Rebuild", TXColor.white, TXColor.sysDeleteCol]
		])
		.action_({|view|
			argModule.rebuildSynth;
			argModule.moduleNodeStatus = "running";
			// recreate view
			system.showView;
		})
	}

	// ModuleActionPopup
	// arguments- index1 is popup width
	*guiModuleActionPopup { arg item, w;
		var totPresets;
		totPresets = argModule.arrPresets.size;
		classData.holdView = PopUpMenu(w, (item[1] ? classData.viewWidth) @ 20);
		classData.holdView.items = [
			"Presets",
			"-",
		]
		++ argModule.arrPresets.asCollection.collect({arg item, i;
			"Load Preset " ++ i.asString ++ ": " ++ item[0];
		})
		++ [
			"-",
			"Store settings as new Preset " ++ totPresets.asString,
			"-",
		]
		++ argModule.arrPresets.asCollection.select({arg item, i; i >0})
		.collect({arg item, i; "Overwrite Preset " ++ (i+1).asString;
		})
		++ [
			"-",
			"Copy settings to module clipboard",
			"Paste settings from module clipboard",
			"Paste settings from module clipboard, excluding presets",
			"-",
			"Save settings to a file",
			"Load settings from a file",
			"Load settings from a file, excluding presets",
			"-",
			"Delete ALL stored presets",
		]
		;
		classData.holdView.stringColor_(TXColor.black).background_(TXColor.white);
		classData.holdView.action = { arg view;
			if (view.value > 1 and: (view.value <= (1 + totPresets)), {
				argModule.loadPreset(argModule, view.value - 2);
			});
			if (view.value == (3 + totPresets),
				{argModule.storePreset(argModule, argModule.moduleInfoTxt);});
			if (view.value > (4 + totPresets) and: (view.value <= (3 + (totPresets * 2))), {
				argModule.overwritePreset(argModule, argModule.moduleInfoTxt,
					view.value - (4 + totPresets));
			});
			if (view.value == (5 + (totPresets * 2)), {argModule.copyToClipboard;});
			if (view.value == (6 + (totPresets * 2)), {argModule.pasteFromClipboard;});
			if (view.value == (7 + (totPresets * 2)), {argModule.pasteFromClipboard(false);});
			if (view.value == (9 + (totPresets * 2)), {argModule.savePresetFile;});
			if (view.value == (10 + (totPresets * 2)), {argModule.openPresetFile;});
			if (view.value == (11 + (totPresets * 2)), {argModule.openPresetFile(false);});
			if (view.value == (13 + (totPresets * 2)), {argModule.deleteAllStoredPresets;});
			view.value = 0;	// reset
			// recreate view
			system.showView;
		};
	}

	// ActionPopup
	// arguments- index1 is items array (function or value),
	// index2 is action function,
	// index3 is optional width (default view width)
	// index4 is optional text color,
	// index5 is optional background color,
	// index6 is optional initial value function
	// e.g. ["ActionPopup", arrItems, {arg view; this.runAction(view.value);}, 200]
	*guiActionPopup { arg item, w;
		classData.holdView = PopUpMenu(w, (item.at(3) ?? classData.viewWidth) @ 20);
		classData.holdView.items = item.at(1);
		classData.holdView.stringColor_(item.at(4) ?? TXColor.black).background_(item.at(5) ?? TXColor.white);
		classData.holdView.value = item.at(6) ? 0;
		classData.holdView.action = { arg view;
			// run action function passing it view as arg
			item.at(2).value(view);
			view.value = 0;	// reset
		};
	}

	// ActionButton
	// arguments- index1 is button text, index2 is action function, index3 is optional width
	// index4 is optional text color, index5 is optional background color,
	// index6 is optional height (default 20)
	// e.g. ["ActionButton", "Start", {this.startSequencer}, 100]
	*guiActionButton { arg item, w;
		Button(w, item.at(3) ? 80 @ (item.at(6) ? 20))
		.states_([
			[item.at(1), item.at(4) ? TXColor.white, item.at(5) ? TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			item.at(2).value;
		})
	}

	// ActionButtonBig - as ActionButton but bigger!
	// arguments- index1 is button text, index2 is action function, index3 is optional width
	// index4 is optional text color, index5 is optionalbackground color,
	// e.g. ["ActionButtonBig", "Start", {this.startSequencer}]
	*guiActionButtonBig { arg item, w;
		Button(w, item.at(3) ? 80 @ 30)
		.states_([
			[item.at(1), item.at(4) ? TXColor.white, item.at(5) ? TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			item.at(2).value;
		})
	}

	// ActionButtonDark
	// arguments- index1 is button text, index2 is action function, index3 is optional width
	// e.g. ["ActionButtonDark", "Start", {this.startSequencer}]
	*guiActionButtonDark { arg item, w;
		Button(w, item.at(3) ? 80 @ 20)
		.states_([
			[item.at(1), TXColor.white, TXColor.sysDeleteCol]
		])
		.action_({|view|
			// run action function
			item.at(2).value;
		})
	}

	// ActionButtonDarkBig - as ActionButtonDark but bigger!
	// arguments- index1 is button text, index2 is action function, index3 is optional width
	// e.g. ["ActionButtonDarkBig", "Start", {this.startSequencer}]
	*guiActionButtonDarkBig { arg item, w;
		Button(w, item.at(3) ? 80 @ 30)
		.states_([
			[item.at(1), TXColor.white, TXColor.sysDeleteCol]
		])
		.action_({|view|
			// run action function
			item.at(2).value;
		})
	}

	// OpenFileButton
	// arguments- index1 is action function, index2 is initial filename function
	// e.g. ["OpenFileButton", {arg argPath;  argPath.postln;  argPath;}, {sampleFileName}]
	*guiOpenFileButton { arg item, w;
		classData.holdView = TXFileOpen(w, classData.viewWidth @ 20, "Open new file", item.at(1), item.at(2).value);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
	}

	// RunPauseButton
	*guiRunPauseButton { arg item, w;
		var holdStatus;
		if (argModule.moduleNodeStatus == "running", {
			holdStatus = 1;
		}, {
			holdStatus = 0;
		});
		classData.holdView = Button(w, 50 @ 20)
		.states_([
			["Run", TXColor.white, TXColor.sysGuiCol1],
			["Pause", TXColor.white, TXColor.sysDeleteCol]
		])
		.action_({|view|
			if (view.value == 1, {
				argModule.runAction;
			});
			if (view.value == 0, {
				argModule.pauseAction;
			});
		})
		//			.value_(argModule.class.system.autoRun.binaryValue );
		.value_(holdStatus);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
	}

	// allNotesOffButton
	*guiallNotesOffButton { arg item, w;
		argModule.arrControls = argModule.arrControls.add(
			Button(w, 150 @ 20)
			.states_([
				["PANIC! - All Notes Off", TXColor.white, TXColor.sysDeleteCol]
			])
			.action_({|view|
				// run method
				argModule.allNotesOff;
			});
		);
	}

	// RefreshButton
	// no arguments
	*guiRefreshButton { arg item, w;
		Button(w, 80 @ 20)
		.states_([
			["Refresh", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			view.focus(false);
			w.refresh;
		})
	}

	// RefreshButtonBig
	// no arguments
	*guiRefreshButtonBig { arg item, w;
		Button(w, 80 @ 30)
		.states_([
			["Refresh", TXColor.white, TXColor.sysGuiCol1]
		])
		.action_({|view|
			// run action function
			view.focus(false);
			w.refresh;
		})
	}

	// TapTempoButton
	// arguments- index1 is optional action function that is passed the measured tempo
	// index2 is optional width
	*guiTapTempoButton { arg item, w;
		var currentTime, meanBPM;
		Button(w, (item.at(2) ? 160) @ 20)
		.states_([
			["Tap tempo: ", TXColor.white, TXColor.sysGuiCol1]
		])
		.mouseDownAction_({|view|
			var timeDiff, currBPM;
			currentTime = Main.elapsedTime;
			if (classData.lastTapTime.isNil, {
				view.states_([["Tap tempo: ", TXColor.white, TXColor.sysGuiCol1]]);
			}, {
				timeDiff = currentTime - classData.lastTapTime;
				if (timeDiff > 5, {    // reset if gap is longer than 5 seconds
					classData.arrBPMs = [];
					view.states_([["Tap tempo: ", TXColor.white, TXColor.sysGuiCol1]]);
				}, {
					currBPM = (60 / timeDiff);
					classData.arrBPMs = classData.arrBPMs.asArray.add(currBPM).keep(-7);  // use last 7 bpms (= 8 taps)
					meanBPM = (classData.arrBPMs.sum * classData.arrBPMs.size.reciprocal).round(0.01);
					view.states_([
						["Tempo: " ++ meanBPM ++ "  bpm",
							TXColor.white, TXColor.sysGuiCol1]
					]);
					view.beginDragAction = { view.dragLabel ="BPM: " ++ meanBPM; meanBPM};
					// if action function passed then value it
					if (item.at(1).notNil, {
						// run action function passing it tap tempo as arg
						item.at(1).value(meanBPM);
					});
				});
			});
			classData.lastTapTime = currentTime;
		});
	}

	// TXTrackView
	// arguments- index1 is track value function,
	//	index2 is module
	//	index3 is optional ScrollView init action
	// 	index4 is optional ScrollView update action
	// 	index5 is initial action function
	// e.g. ["TXTrackView", currentTrack, currentModule,
	//		{arg view; holdScrollView = view;},
	//		{arg view; holdVisibleOrigin = view.visibleOrigin; }
	//	],
	// 	TXTrackView *new {arg argSystem, argParent, dimensions, argTrack, argModule, scrollViewAction;

	*guiTXTrackView { arg item, w;
		var extraWidth = 0;
		if (item.at(7).notNil, {extraWidth = 40;});
		classData.holdView = TXTrackView(system, w, (classData.viewWidth + extraWidth) @ 425, item.at(1).value, item.at(2),
			item.at(4));
		if (item.at(3).notNil, {
			item.at(3).value(classData.holdView.scrollView);
		});
		if (item.at(5).notNil, {
			item.at(5).value(classData.holdView);
		});
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				// not used currently
			}
		);
	}	// end of TXTrackView

	// Transpose
	// no arguments - assumes synth has argument "transpose"
	*guiTranspose { arg item, w;
		classData.holdView = TXTransposeKey(w, classData.viewWidth @ 20, "Transpose",
			{|view|
				// set current value on synth
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set("transpose", view.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec("transpose", view.value);
			},
			argModule.getSynthArgSpec("transpose")  // get starting value
		);
		// set controlspec variables
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView3.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.value_(argModule.getSynthArgSpec("transpose"));
			}
		);
	}

	// ModulationOptions
	*guiModulationOptions { arg item, w;
		// initialise variable
		classData.holdVal = (classData.viewWidth - 10) /2;
		// add title
		StaticText(w, classData.holdVal @ 18)
		.string_("Modulation options:")
		.align_(\centre)
		.stringColor_(TXColor.white).background_(TXColor.sysGuiCol2);
		// new line
		this.nextline(w);
		// add line for each option
		argModule.myArrCtlSCInBusSpecs.do({ arg item, i;
			//   arrAudSCInBusSpecs/arrCtlSCInBusSpecs - these consist of an array of arrays,
			//    	each with: ["Bus Name Text", no. channels, "synth arg string", optionDefault]
			// only show optional busses
			if (item.at(3).notNil, {
				// TXCheckBox: arg argParent, bounds, text, offStringColor, offBackground,
				// onStringColor, onBackground, onOffTextType=0;
				classData.holdView = TXCheckBox(w, classData.holdVal @ 18, item.at(0), TXColor.sysGuiCol1, TXColor.white,
					TXColor.white, TXColor.sysGuiCol1);
				classData.holdView.value =  item.at(3);
				classData.holdView.action = {|view|
					// store current data to myArrCtlSCInBusSpecs
					argModule.myArrCtlSCInBusSpecs.at (i).put(3, view.value);
					// if option turned off, check for invalid channels
					if (view.value == 0, {
						system.checkChannelsDest(argModule, i);
					});
					// rebuild synth
					argModule.rebuildSynth;
				};
				argModule.arrControls = argModule.arrControls.add(classData.holdView);
			});
		});
	}

	// EZNumber
	// arguments- index1 is text, index2 is controlSpec, index3 is synth arg name to be updated,
	// 	index4 is an optional ACTION function to be valued in views action
	// index 5/6 are the text and number widths
	// index 7 is an optional scroll step size
	// e.g. ["EZNumber", "Speed factor", ControlSpec(-5, 5, 'lin', 1, 0), "speedFactor", {"Action".postln;}]
	*guiEZNumber { arg item, w;
		classData.holdView = TXNumber(w, classData.viewWidth @ 20, item.at(1), item.at(2).value,
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), view.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(3)),
			false, item.at(5) ? 80, item.at(6) ? 60, item.at(7)
		)
		.round_(0.001);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.numberView.scroll_step = classData.holdView.numberView.step = item.at(7) ? 1;

		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.numberView.value_(argMod.getSynthArgSpec(item.at(3)).round(0.001););
			}
		);
	}

	// EZNumberMapped - same as EZNumber but uses ControlSpec to map/unmapped value so stored range is 0-1.
	// arguments- index1 is number text, index2 is controlSpec, index3 is synth arg name to be updated,
	// 	index4 is an optional ACTION function to be valued in views action
	// index 5/6 are the text and number widths
	// index 7 is an optional scroll step size
	// e.g. ["EZNumberMapped", "BPM", ControlSpec(1, 120, 'lin', 1, 0), "seqBPM"]
	*guiEZNumberMapped { arg item, w;
		var holdControlSpec;
		holdControlSpec = item.at(2).value;
		classData.holdView = TXNumber(w, classData.viewWidth @ 20, item.at(1), holdControlSpec,
			{|view|
				// set unmapped value value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), holdControlSpec.unmap(view.value));
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), holdControlSpec.unmap(view.value));
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			holdControlSpec.map(argModule.getSynthArgSpec(item.at(3))),
			false, item.at(5) ? 80, item.at(6) ? 60, item.at(7)
		)
		.round_(0.001);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.numberView.scroll_step = classData.holdView.numberView.step = item.at(7) ? 1;
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.numberView.value_(
					holdControlSpec.map(argMod.getSynthArgSpec(item.at(3))).round(0.001);
				);
			}
		);
	}

	// TXScrollNumBox
	// arguments- index1 is controlSpec, index2 is synth arg name to be updated,
	// 	index3 is an optional ACTION function to be valued in views action
	// index 4 is an optional number width
	// index 5 is an optional scroll step size (default 1)
	// e.g. ["TXScrollNumBox", ControlSpec(-5, 5, 'lin', 1, 0), "speedFactor"]
	*guiTXScrollNumBox { arg item, w;
		classData.holdView = TXNumber(w, classData.viewWidth @ 20, "", item.at(1).value,
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(2), view.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), view.value);
				// if action function passed then value it
				if (item.at(3).notNil, {
					// run action function passing it view as arg
					item.at(3).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(2)),
			false, 0, item.at(4) ? 60
		)
		.round_(0.001);
		classData.holdView.numberView.scroll_step = classData.holdView.numberView.step = item.at(5) ? 1;
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.numberView.value_(argMod.getSynthArgSpec(item.at(2)).round(0.001););
			}
		);
	}

	// TXScrollNumBoxUnmapped - same as TXScrollNumBox but uses ControlSpec to map/unmapped value so stored range is 0-1.
	// arguments- index1 is controlSpec, index2 is synth arg name to be updated,
	// 	index3 is an optional ACTION function to be valued in views action
	// index 4 is an optional number width
	// index 5 is an optional scroll step size (default 1)
	// e.g. ["TXScrollNumBox", ControlSpec(-5, 5, 'lin', 1, 0), "speedFactor"]
	*guiTXScrollNumBoxUnmapped { arg item, w;
		var holdControlSpec;
		holdControlSpec = item.at(1).value;
		classData.holdView = TXNumber(w, classData.viewWidth @ 20, "", holdControlSpec,
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(2), holdControlSpec.unmap(view.value));
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), holdControlSpec.unmap(view.value));
				// if action function passed then value it
				if (item.at(3).notNil, {
					// run action function passing it view as arg
					item.at(3).value(view);
				});
			},
			// get starting value
			holdControlSpec.map(argModule.getSynthArgSpec(item.at(2))),
			false, 0, item.at(4) ? 60
		)
		.round_(0.001);
		classData.holdView.numberView.scroll_step = classData.holdView.numberView.step = item.at(5) ? 1;
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.numberView.value_(holdControlSpec.map(argMod.getSynthArgSpec(item.at(2))).round(0.001));
			}
		);
	}

	// TXNumberPlusMinus
	// arguments- index1 is view text, index2 is controlSpec, index3 is synth arg name to be updated,
	// 	index4 is an optional ACTION function to be valued in views action
	// 	index5 is an optional array of numbers to be put on plus/minus buttons next to numberbox
	// 	index6 is an optional width of label
	// 	index7 is an optional width of numberbox
	// 	index8 is an optional boolean for allowing scrolling (default true)
	// 	index9 is an optional colour of text and numberbox backgound
	// e.g. ["TXNumberPlusMinus", "Speed factor", ControlSpec(-5, 5, 'lin', 1, 0), "speedFactor"]
	*guiTXNumberPlusMinus { arg item, w;
		classData.holdView = TXNumberPlusMinus2(w, classData.viewWidth @ 20, item.at(1), item.at(2),
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), view.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(3)),
			false,
			item.at(6),
			item.at(7),
			item.at(5),
			item.at(8)
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1)
		.background_(item.at(9) ? TXColor.white);
		classData.holdView.numberView.background_(item.at(9) ? TXColor.white);
		classData.holdView.round_(0.001);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.numberView.value_(argMod.getSynthArgSpec(item.at(3)).round(0.001););
			}
		);
	}

	// EZslider
	// arguments- index1 is slider text, index2 is controlSpec, index3 is synth arg name to be updated,
	// 	index4 is an optional ACTION function to be valued in views action
	// 	index5 is an optional width
	// 	index6 is an optional label width
	// 	index7 is an optional number width
	// e.g. ["EZslider", "Volume", \amp, "vol"]
	*guiEZslider { arg item, w;
		classData.holdView = TXSlider(w, (item.at(5) ?? classData.viewWidth) @ 20, item.at(1), item.at(2).value,
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), view.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(3)),
			false, item.at(6) ? 80, item.at(7) ? 60
		)
		.round_(0.001);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.numberView.value_(argMod.getSynthArgSpec(item.at(3)).round(0.001););
				argView.sliderView.value = argView.controlSpec.unmap(argMod.getSynthArgSpec(item.at(3)));
			}
		);
	}
	// allow for upper case "S" in "EZSlider"
	*guiEZSlider { arg item, w;
		this.guiEZslider(item, w);
	}

	// EZsliderUnmapped - same as EZslider but returns unmapped value (range 0-1) of slider
	// arguments- index1 is slider text, index2 is controlSpec function, index3 is synth arg name to be updated,
	// 	index4 is an optional ACTION function to be valued in views action
	// 	index5 is an optional view width
	// e.g. ["EZsliderUnmapped", "Attack", ControlSpec(0, 5), "attack"]
	*guiEZsliderUnmapped { arg item, w;
		var holdControlSpec;
		holdControlSpec = item.at(2).value ? [0, 1].asSpec;
		classData.holdView = TXSlider(w, (item.at(5) ?? classData.viewWidth) @ 20, item.at(1), holdControlSpec,
			{|view|
				// set unmapped sliderView.value on node
				if (argModule.moduleNode.notNil, {
					// here unmapped view.sliderView.value is set
					argModule.moduleNode.set(item.at(3), view.sliderView.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.sliderView.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			holdControlSpec.map(argModule.getSynthArgSpec(item.at(3))),
			false, 80, 60
		)
		.round_(0.001);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.numberView.value_(holdControlSpec.map(argMod.getSynthArgSpec(item.at(3))).round(0.001););
				argView.sliderView.value = item.at(3);
			}
		);
	}

	// MixerLevel
	// arguments- index1 is slider text, index2 is controlSpec, index3 is synth arg name to be updated,
	// 	index4 is an optional ACTION function to be valued in views action
	// 	index5 is an optional view width
	// 	index6 is an optional view height
	// e.g. ["MixerLevel", "Level1", \amp, "level1"]
	*guiMixerLevel { arg item, w;
		classData.holdView = TXMixerLevel(w, (item.at(5) ? 70) @ (item.at(6) ? 200), item.at(1),item.at(2),
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), view.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(3)),
			false
		)
		.round_(0.001);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.numberView.value_(argMod.getSynthArgSpec(item.at(3)).round(0.001););
				argView.sliderView.value = argView.controlSpec.unmap(argMod.getSynthArgSpec(item.at(3)));
			}
		);
	}

	// MixerPan
	// arguments- index1 is slider text, index2 is controlSpec, index3 is synth arg name to be updated,
	// 	index4 is an optional ACTION function to be valued in views action
	// 	index5 is an optional view width
	// 	index6 is an optional view height
	// e.g. ["MixerPan", "Level1", \amp, "level1"]
	*guiMixerPan { arg item, w;
		classData.holdView = TXMixerPan(w, (item.at(5) ? 70) @ (item.at(6) ? 80), item.at(1),item.at(2),
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), view.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(3)),
			false
		)
		.round_(0.001);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.numberView.value_(argMod.getSynthArgSpec(item.at(3)).round(0.001););
				argView.sliderView.value =
				argView.controlSpec.unmap(argMod.getSynthArgSpec(item.at(3)));
			}
		);
	}

	// WetDryMixSlider - assumes synth has argument "wetDryMix"
	// arguments- index1 is optional width
	*guiWetDryMixSlider { arg item, w;
		classData.holdView = TXSlider(w, (item[1] ? classData.viewWidth) @ 20, "Dry-Wet Mix",  \unipolar,
			{|view|
				// set current value on synth
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set("wetDryMix", view.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec("wetDryMix", view.value);
			},
			// get starting value
			argModule.getSynthArgSpec("wetDryMix"),
			false, 80, 60
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.value_(argMod.getSynthArgSpec("wetDryMix"));
			}
		);
	}

	// TXCheckBox
	// arguments- index1 is checkbox text, index2 is synth arg name to be updated,
	// 	index3 is an optional ACTION function to be valued in views action,
	// index 4 is optional width (default 150),
	// index 5 is optional height (default 20),
	// index 6 is optional checkbox Text Type (default 0)
	// index 7 is an optional stringColor
	// index 8 is an optional background
	// e.g. ["TXCheckBox", "Loop", "loop", {some action}, 150, 20]
	*guiTXCheckBox { arg item, w;
		classData.holdView = TXCheckBox(w, (item.at(4) ? 150) @ (item.at(5) ? 20),
			item.at(1), item.at(8) ? TXColor.sysGuiCol1, TXColor.grey(0.9),
			item.at(7) ? TXColor.white, item.at(8) ? TXColor.sysGuiCol1, item.at(6) ? 0);
		classData.holdView.action = {|view|
			// set current value on node
			if (argModule.moduleNode.notNil, {
				argModule.moduleNode.set(item.at(2), view.value);
			});
			// store current data to synthArgSpecs
			argModule.setSynthArgSpec(item.at(2), view.value);
			// if action function passed then value it
			if (item.at(3).notNil, {
				// run action function passing it view as arg
				item.at(3).value(view);
			});
		};
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// get starting value
		classData.holdView.value =  argModule.getSynthArgSpec(item.at(2));
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.value_(argMod.getSynthArgSpec(item.at(2)));
			}
		);
	}

	// TXFraction
	// arguments- index1 is  text, index2 is controlSpec, index3 is synth arg name to be updated,
	// 	index4 is an optional ACTION function to be valued in views action
	// e.g. ["TXFraction", "Start", ControlSpec(0, 1), "start"],
	*guiTXFraction { arg item, w;
		classData.holdView = TXFraction(w, classData.viewWidth @ 20, item.at(1),item.at(2),
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), view.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(3))
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView3.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueNoAction_(argMod.getSynthArgSpec(item.at(3)));
			}
		);
	}

	// TXFractionSlider
	// arguments- index1 is  text, index2 is controlSpec, index3 is synth arg name to be updated,
	// 	index4 is an optional ACTION function to be valued in views action, index5 is slider width
	// e.g. ["TXFractionSlider", "Start", ControlSpec(0, 1), "start"],
	*guiTXFractionSlider { arg item, w;
		classData.holdView = TXFractionSlider(w, classData.viewWidth @ 20, item.at(1),item.at(2),
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), view.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(3)),
			sliderWidth: item[5] ? 230;
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView3.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueNoAction_(argMod.getSynthArgSpec(item.at(3)));
			}
		);
	}

	// TXTimeBeatsBpmNumber
	// arguments- index1 is text, index2 is controlSpec, index3 is synth arg name to be updated,
	// 	index4 is an optional ACTION function to be valued in views action
	// e.g. ["TXTimeBeatsBpmNumber", "Record Time", ControlSpec(0, 100000), "recordTime"],
	*guiTXTimeBeatsBpmNumber { arg item, w;
		classData.holdView = TXTimeBeatsBpmNumber(w, classData.viewWidth @ 20, item.at(1), item.at(2),
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), view.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(3))
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView3.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView4.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueNoAction_(argMod.getSynthArgSpec(item.at(3)));
			}
		);
	}

	// TXRangeSlider
	// arguments- index1 is slider text, index2 is controlSpec function, index3/4 are synth arg names to be updated,
	// 	index5 is an optional ACTION function to be valued in views action
	// 	index6 is an optional range preset array in the form: [["Presets:", []], ["1-16", [1,16]], ["17-32", [17,32]], ]
	// 	index7 is an optional width
	// 	index8 is an optional background color
	// e.g. ["TXRangeSlider", "Volume", \amp, "volMin", "volMax"]
	*guiTXRangeSlider { arg item, w;
		classData.holdView = TXRangeSlider(w, item.at(7) ? classData.viewWidth @ 20, item.at(1).value, item.at(2).value,
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), view.lo);
					argModule.moduleNode.set(item.at(4), view.hi);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.lo);
				argModule.setSynthArgSpec(item.at(4), view.hi);
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
			},
			// get starting values
			argModule.getSynthArgSpec(item.at(3)),
			argModule.getSynthArgSpec(item.at(4)),
			false, 80, 120,
			item.at(6) // presets
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.rangeView.background_(item.at(8) ? TXColor.sysGuiCol1);

		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueBothNoAction_([argMod.getSynthArgSpec(item.at(3)), argMod.getSynthArgSpec(item.at(4))]);
			}
		);
	}

	// TXNoteRangeSlider
	// arguments- index1 is slider text, index2/3 are synth arg names to be updated,
	// 	index4 is an optional ACTION function to be valued in views action
	// 	index5 is an optional true/false to indicate whether to show buttons on view
	// 	index6 is an optional width
	// e.g. ["TXNoteRangeSlider", "Note range", "procRandNoteMin", "procRandNoteMax"]
	*guiTXNoteRangeSlider { arg item, w;
		classData.holdView = TXMidiNoteRange(w, (item.at(6) ? classData.viewWidth) @ 20, item.at(1).value,  ControlSpec(0, 127, step: 1),
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(2), view.lo);
					argModule.moduleNode.set(item.at(3), view.hi);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), view.lo);
				argModule.setSynthArgSpec(item.at(3), view.hi);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting values
			argModule.getSynthArgSpec(item.at(2)),
			argModule.getSynthArgSpec(item.at(3)),
			showButtons: item.at(5) ? false
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.rangeView.background_(TXColor.sysGuiCol1).knobColor_(TXColor.grey(0.75));
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueBothNoAction_([argMod.getSynthArgSpec(item.at(2)),
					argMod.getSynthArgSpec(item.at(3))]);
			}
		);
	}

	// TXCompactSlider
	// arguments- index1 is controlSpec, index2 is synth arg name to be updated,
	// 	index3 is an optional ACTION function to be valued in views action
	// 	index4 is an optional width
	// 	index5 is an optional thumbSize
	// e.g. ["TXCompactSlider", \amp, "vol"]
	*guiTXCompactSlider { arg item, w;
		var spec = item.at(1);
		classData.holdView = Slider(w, (item.at(4) ?? classData.viewWidth) @ 20)
		.action_({|view|
			// set current value on node
			if (argModule.moduleNode.notNil, {
				argModule.moduleNode.set(item.at(2), spec.map(view.value));
			});
			// store current data to synthArgSpecs
			argModule.setSynthArgSpec(item.at(2), spec.map(view.value));
			// if action function passed then value it
			if (item.at(3).notNil, {
				// run action function passing it view as arg
				item.at(3).value(view);
			});
		})
		.background_(TXColor.sysGuiCol1)
		.knobColor_(TXColor.white)
		.thumbSize_(item.at(5) ? 5)
		.value_(spec.unmap(argModule.getSynthArgSpec(item.at(2))));
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.value = spec.unmap(argMod.getSynthArgSpec(item.at(2)));
			}
		);
	}

	// TXCompactSliderUnmapped - same as TXCompactSlider but stored range is 0-1.
	// arguments- index1 is controlSpec, index2 is synth arg name to be updated,
	// 	index3 is an optional ACTION function to be valued in views action
	// 	index4 is an optional width
	// 	index5 is an optional thumbSize
	// e.g. ["TXCompactSliderUnmapped", \amp, "vol"]
	*guiTXCompactSliderUnmapped { arg item, w;
		var spec = item.at(1);
		classData.holdView = Slider(w, (item.at(4) ?? classData.viewWidth) @ 20)
		.action_({|view|
			// set current value on node
			if (argModule.moduleNode.notNil, {
				argModule.moduleNode.set(item.at(2), view.value);
			});
			// store current data to synthArgSpecs
			argModule.setSynthArgSpec(item.at(2), view.value);
			// if action function passed then value it
			if (item.at(3).notNil, {
				// run action function passing it view as arg
				item.at(3).value(view);
			});
		})
		.background_(TXColor.sysGuiCol1)
		.knobColor_(TXColor.white)
		.thumbSize_(item.at(5) ? 5)
		.value_(argModule.getSynthArgSpec(item.at(2)));
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.value = (argMod.getSynthArgSpec(item.at(2)));
			}
		);
	}

	// TXDoubleSlider
	// arguments- index1 is slider text, index2 is controlSpec function, index3/4 are synth arg names to be updated,
	// 	index5 is an optional ACTION function to be valued in views action
	// e.g. ["TXDoubleSlider", "Volume", \amp, "volMin", "volMax"]
	*guiTXDoubleSlider { arg item, w;
		classData.holdView = TXDoubleSlider(w, classData.viewWidth @ 20, item.at(1).value, item.at(2),
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), view.lo);
					argModule.moduleNode.set(item.at(4), view.hi);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.lo);
				argModule.setSynthArgSpec(item.at(4), view.hi);
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
			},
			// get starting values
			argModule.getSynthArgSpec(item.at(3)),
			argModule.getSynthArgSpec(item.at(4))
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.sliderView1.background_(TXColor.sysGuiCol1);
		classData.holdView.sliderView2.background_(TXColor.sysGuiCol1);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueBothNoAction_([argMod.getSynthArgSpec(item.at(3)), argMod.getSynthArgSpec(item.at(4))]);
			}
		);
	}

	// TX2DSlider
	// arguments- index1 is slider text, index2 is controlSpec function, index3/4 are synth arg names to be updated,
	// 	index5 is an optional ACTION function to be valued in views action
	//   index 6 is an optional height
	//   index 7 is an optional width
	//   index 8 is an optional true/false show X & Y sliders
	// e.g. ["TX2DSlider", "X-Y Morph", ControlSpec(0, 1), "sliderXVal", "sliderYVal", nil, 200]
	*guiTX2DSlider { arg item, w;
		classData.holdView = TX2DSlider(w, item.at(7) ? classData.viewWidth @ (item.at(6) ? 100), item.at(1), item.at(2),
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), view.valX);
					argModule.moduleNode.set(item.at(4), view.valY);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.valX);
				argModule.setSynthArgSpec(item.at(4), view.valY);
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
			},
			// get starting values
			argModule.getSynthArgSpec(item.at(3)),
			argModule.getSynthArgSpec(item.at(4)),
			nil, 80, 80,
			item.at(8) ? false
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.grey(0.9));
		classData.holdView.labelView3.stringColor_(TXColor.sysGuiCol1).background_(TXColor.grey(0.9));
		classData.holdView.hold2DSlider.background_(TXColor.sysGuiCol1);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.value_([argMod.getSynthArgSpec(item.at(3)), argMod.getSynthArgSpec(item.at(4))]);
			}
		);
	}

	// MIDIChannelSelector
	//  arguments- index1 1 is an optional width
	*guiMIDIChannelSelector { arg item, w;
		classData.holdView = TXRangeSlider(w, item.at(1) ? classData.viewWidth @ 20, "Midi channels",
			ControlSpec(1, 16, step: 1),
			{|view|
				// set current value on synth
				argModule.midiMinChannel = view.lo;
				argModule.midiMaxChannel = view.hi;
			},
			// get starting values
			argModule.midiMinChannel,
			argModule.midiMaxChannel
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueBothNoAction_([argMod.midiMinChannel, argMod.midiMaxChannel]);
			}
		);
	}

	// NoteRangeSelector
	// arguments- index1 is title text, index2/3 are synth arg names to be updated for min and max values,
	//  index4 is an optional width
	*guiNoteRangeSelector { arg item, w;
		classData.holdView = TXMidiNoteRange(w, item.at(4) ? classData.viewWidth @ 20, item.at(1),
			ControlSpec(0, 127, step: 1),
			{|view|
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), view.lo);
				argModule.setSynthArgSpec(item.at(3), view.hi);
			},
			// get starting values
			argModule.getSynthArgSpec(item.at(2)),
			argModule.getSynthArgSpec(item.at(3))
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueBothNoAction_([argMod.getSynthArgSpec(item.at(2)), argMod.getSynthArgSpec(item.at(3))]);
			}
		);
	}

	// MIDINoteSelector
	//  arguments- index1 1 is an optional width
	*guiMIDINoteSelector { arg item, w;
		classData.holdView = TXMidiNoteRange(w, item.at(1) ? classData.viewWidth @ 20, "Note range",
			ControlSpec(0, 127, step: 1),
			{|view|
				// set current value on synth
				argModule.midiMinNoteNo = view.lo;
				argModule.midiMaxNoteNo = view.hi;
			},
			// get starting values
			argModule.midiMinNoteNo,
			argModule.midiMaxNoteNo
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueBothNoAction_([argMod.midiMinNoteNo, argMod.midiMaxNoteNo]);
			}
		);
	}

	// MIDIVelSelector
	//  arguments- index1 1 is an optional width
	*guiMIDIVelSelector { arg item, w;
		classData.holdView = TXRangeSlider(w, item.at(1) ? classData.viewWidth @ 20, "Vel range",
			ControlSpec(0, 127, step: 1),
			{|view|
				// set current value on synth
				argModule.midiMinVel = view.lo;
				argModule.midiMaxVel = view.hi;
			},
			// get starting values
			argModule.midiMinVel,
			argModule.midiMaxVel
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueBothNoAction_([argMod.midiMinVel, argMod.midiMaxVel]);
			}
		);
	}

	// MIDIListenCheckBox
	// N.B. no arguments
	*guiMIDIListenCheckBox { arg item, w;
		classData.holdView = TXCheckBox(w, (150) @ 20, "Midi listen", TXColor.sysGuiCol1, TXColor.grey(0.9),
			TXColor.white, TXColor.sysGuiCol1);
		classData.holdView.action = {|view|
			// set current value
			argModule.setMidiListen(view.value);
		};
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// get starting value
		classData.holdView.value =  argModule.midiListen;
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.value_(argMod.midiListen);
			}
		);
	}

	// MIDISoloControllerSelector
	// N.B. no arguments
	*guiMIDISoloControllerSelector { arg item, w;
		classData.holdView = TXNumber(w, classData.viewWidth @ 20, "Controller no.",  ControlSpec(0, 127, step: 1),
			{|view|
				// set current value
				argModule.midiMinControlNo = view.value;
				argModule.midiMaxControlNo = view.value;
			},
			// get starting value
			argModule.midiMinControlNo
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.numberView.value_(argMod.midiMinControlNo);
			}
		);
	}

	// MIDISoloChannelSelector
	// N.B. no arguments
	*guiMIDISoloChannelSelector { arg item, w;
		classData.holdView = TXNumber(w, classData.viewWidth @ 20, "Channel",  ControlSpec(0, 16, step: 1),
			{|view|
				// set current value
				argModule.midiMinChannel = view.value;
				argModule.midiMaxChannel = view.value;
			},
			// get starting value
			argModule.midiMinChannel
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.numberView.value_(argMod.midiMinChannel);
			}
		);
	}

	// MIDIOutPortSelector
	// N.B. no arguments
	*guiMIDIOutPortSelector { arg item, w;
		classData.holdView = TXNumber(w, classData.viewWidth @ 20, "Midi Port",  ControlSpec(0, MIDIClient.destinations.size-1, step: 1),
			{|view|
				// set current value
				argModule.midiOutPort = view.value;
				// activate port
				argModule.midiPortActivate;
			},
			// get starting value
			argModule.midiOutPort
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.numberView.value_(argMod.midiOutPort);
			}
		);
	}

	// PolyphonySelector
	// N.B. no arguments
	*guiPolyphonySelector { arg item, w;
		classData.holdView = TXNumber(w, classData.viewWidth @ 20, "Polyphony",  ControlSpec(1, 64, step: 1),
			{|view|
				// set current value
				argModule.groupPolyphony = view.value;
			},
			// get starting value
			argModule.groupPolyphony
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.numberView.value_(argMod.groupPolyphony);
			}
		);
	}

	// TXStaticText
	// arguments- index1 is row label, index2 is initial value (function or value),
	//	index3 is optional function to be valued with view as argument (e.g. for storing view to variable in module),
	// index 4 is optional width of the text+label (defaults to view width)
	// index 5 is optional width of the label only
	// index 6 is optional background colour
	// index 7 is optional height of text
	// e.g. ["TXStaticText", "Record status", {recordStatus}, {arg view; recordStatusView = view}]
	*guiTXStaticText { arg item, w;
		classData.holdView = TXStaticText(w, (item.at(4) ? classData.viewWidth) @ (item.at(7) ? 20), item.at(1), item.at(2).value,
			item.at(5) ? 80 );
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(item.at(6) ? TXColor.white);
		classData.holdView.textView.stringColor_(TXColor.black).background_(item.at(6) ? TXColor.white);
		// value function
		if (item.at(3).notNil, {
			item.at(3).value(classData.holdView);
		});
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView],
			{ arg argArray;
				var argView = argArray.at(0);
				argView.textView.string_(item.at(2).value);
			}
		);
	}

	// TXTextBox
	// arguments- index1 is row label, index2 is synth arg name to be updated,
	// 	index3 is an optional ACTION function to be valued in views action,
	// 	index4 is optional text width, index5 is optional label width
	// index6 is optional function to be valued with view as argument (e.g. for storing view to variable in module),
	// e.g. ["TXTextBox", "Text", "textString"]
	*guiTXTextBox { arg item, w;
		var holdWidth;
		holdWidth = (item.at(5) ? 80) + 4 + (item.at(4) ? 100);
		classData.holdView = TXTextBox(w, holdWidth @ 20, item.at(1),
			{|view|
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), view.string);
				// if action function passed then value it
				if (item.at(3).notNil, {
					// run action function passing it view as arg
					item.at(3).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(2)),
			false, item.at(5) ? 80, item.at(4)
		);
		// value function
		if (item.at(6).notNil, {
			item.at(6).value(classData.holdView);
		});
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		if (classData.holdView.labelView.notNil, {
			classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		});
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.string_(argMod.getSynthArgSpec(item.at(2)));
			}
		);
	}

	// OSCString
	// N.B. no arguments
	*guiOSCString { arg item, w;
		classData.holdView = TXTextBox(w, classData.viewWidth @ 20, "OSC String.",
			{|view|
				// set current value in module
				argModule.oscString = view.string;
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec("OSCString", view.string);
				// activate osc responder
				argModule.oscControlActivate;
			},
			// get starting value
			argModule.getSynthArgSpec("OSCString")
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.string_(argMod.getSynthArgSpec("OSCString"));
			}
		);
	}

	// ModuleInfoTxt
	// arguments- index1 is width of text
	*guiModuleInfoTxt { arg item, w;
		classData.holdView = TXTextBox(w, (item.at(1) ? classData.viewWidth) @ 20, "Comments",
			{|view|
				// set current value in module
				argModule.moduleInfoTxt = view.string;
			},
			// get starting value
			argModule.moduleInfoTxt;
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
	}

	// TXNetAddress
	// arguments- index1 is row label, index2 is synth arg name to be updated,
	// 	index3 is an optional ACTION function to be valued in views action,
	// 	index4 is optional text width
	// 	index5 is optional overall width - default classData.viewWidth
	// e.g. ["TXNetAddress", "Text", "textString"]
	*guiTXNetAddress { arg item, w;
		classData.holdView = TXNetAddress(w, (item.at(5) ? classData.viewWidth) @ 20, item.at(1),
			{|view|
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), view.string);
				// if action function passed then value it
				if (item.at(3).notNil, {
					// run action function passing it view as arg
					item.at(3).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(2)),
			false, 80, item.at(4)
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.stringNoAction_(argMod.getSynthArgSpec(item.at(2)));
			}
		);
	}

	// TXPopupAction
	// arguments- index1 is text, index2 is items array (function or value),
	//	index3 is synth arg name to be updated, index4 is optional popup action,
	//   index5 is the optional width,
	//   index6 is the optional label width,
	// e.g. ["TXPopupAction", "Sample", holdSampleFileNames, "sampleNo", { arg view; this.loadSample(view.value); }]
	*guiTXPopupAction { arg item, w;
		// TXPopup.new  arg argParent, dimensions, label, items, action, initVal,
		//   initAction=false, labelWidth=80;
		classData.holdView = TXPopup(w, (item.at(5) ?? classData.viewWidth) @ 20, item.at(1), item.at(2).value,
			{|view|
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(3)),
			false,
			item.at(6) ? 80
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		if (item.at(6) != 0, {
			classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		});
		classData.holdView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueNoAction_(argMod.getSynthArgSpec(item.at(3)));
			}
		);
	}

	// TXPopupActionPlusMinus
	// arguments- index1 is text, index2 is items array (function or value),
	//	index3 is synth arg name to be updated, index4 is optional popup action,
	// index5 is the optional width,
	// index6 is the optional label width,
	// index7 is optional function to be valued with view as argument (e.g. for storing view to variable in module),
	// e.g. ["TXPopupActionPlusMinus", "Sample", holdSampleFileNames, "sampleNo", { arg view; this.loadSample(view.value); }]
	*guiTXPopupActionPlusMinus { arg item, w;
		// TXPopup.new  arg argParent, dimensions, label, items, action, initVal,
		//   initAction=false, labelWidth=80;
		classData.holdView = TXPopupPlusMinus(w, (item.at(5) ?? classData.viewWidth) @ 20, item.at(1), item.at(2).value,
			{|view|
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(3)),
			false,
			item.at(6) ? 80
		);
		// value function
		if (item.at(7).notNil, {
			item.at(7).value(classData.holdView);
		});
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueNoAction_(argMod.getSynthArgSpec(item.at(3)));
			}
		);
	}

	// TXPresetPopup
	// arguments- index1 is text, index2 is preset names array (function or value),
	//	index3 is preset actions array (function or value), index4 is the optional width,
	//	index5 is optional final action function
	//	index6 is optional first item text (default: "Select preset...")
	// e.g. ["TXPresetPopup", "Env presets", arrPresetNames, arrPresetActions]
	*guiTXPresetPopup { arg item, w;
		// TXPopup.new  arg argParent, dimensions, label, items, action, initVal,
		//   initAction=false, labelWidth=80;
		classData.holdView = TXPopup(w, (item.at(4) ?? classData.viewWidth) @ 20, item.at(1),
			// add first item in popup
			[item.at(6) ?? "Select preset..."] ++ item.at(2).value,
			{|view|
				// if not first item in popup
				if (view.value > 0, {
					// value selected preset from preset actions array
					item.at(3).value.at(view.value-1).value;
				});
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
				// recreate view
				system.showView;
			},
			0
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
	}

	// TXListViewAction
	// arguments- index1 is text, index2 is items array (function or value),
	//	index3 is synth arg name to be updated, index4 is optional popup ListView action,
	//	index5 is the optional width, index6 is the optional height,
	// e.g. ["TXListViewAction", "Sample", holdSampleFileNames, "sampleNo", { arg view; this.loadSample(view.value); }]
	*guiTXListViewAction { arg item, w;
		// ListView.new  arg argParent, dimensions, label, items, action, initVal,
		//   initAction=false, labelWidth=80;
		classData.holdView = TXListViewLabel(w, (item.at(5) ?? classData.viewWidth) @ (item.at(6) ?? 80), item.at(1), item.at(2).value,
			{|view|
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(3))
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.listView.stringColor_(TXColor.black).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueNoAction_(argMod.getSynthArgSpec(item.at(3)));
			}
		);
	}

		// TXListViewActionPlusMinus
	// arguments- index1 is text, index2 is items array (function or value),
	//	index3 is synth arg name to be updated, index4 is optional popup ListView action,
	//	index5 is the optional width, index6 is the optional height,
	// e.g. ["TXListViewActionPlusMinus", "Sample", holdSampleFileNames, "sampleNo", { arg view; this.loadSample(view.value); }]
	*guiTXListViewActionPlusMinus { arg item, w;
		// ListView.new  arg argParent, dimensions, label, items, action, initVal,
		//   initAction=false, labelWidth=80;
		classData.holdView = TXListViewLabelPlusMinus(w, (item.at(5) ?? classData.viewWidth) @ (item.at(6) ?? 80), item.at(1), item.at(2).value,
			{|view|
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(3))
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.listView.stringColor_(TXColor.black).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueNoAction_(argMod.getSynthArgSpec(item.at(3)));
			}
		);
	}

	// SynthOptionCheckBox
	// arguments- index1 is checkbox text,
	// index2 is arrOptionData, index3 is the index of arrOptions and arrOptionData to use,
	// index4 is the width (optional), index5 is optional checkbox action
	// index6 is optional onOffTextType
	// e.g. ["SynthOptionCheckBox", "Filter", arrOptionData, 0, 250]
	*guiSynthOptionCheckBox { arg item, w;
		classData.holdView = TXCheckBox(w, (item.at(4) ? 350) @ 20, "*"++item.at(1), TXColor.sysGuiCol1, TXColor.grey(0.9),
			TXColor.white, TXColor.sysGuiCol1, item.at(6) ? 2);
		classData.holdView.action = {|view|
			// store current data to arrOptions
			argModule.arrOptions.put(item.at(3), view.value);
			// if action function passed then value it
			if (item.at(5).notNil, {
				// run action function passing it view as arg
				item.at(5).value(view);
			});
			// rebuild synth
			argModule.rebuildSynth;
		};
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// get starting value
		classData.holdView.value =  argModule.arrOptions.at(item.at(3));
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.value_(argMod.arrOptions.at(item.at(3)));
			}
		);
	}

	// SynthOptionPopup
	// NOTE - this will automatically rebuild the synth once a synth option has been changed
	// arguments- index1 is text, index2 is arrOptionData, index3 is the index of arrOptions and arrOptionData to use,
	//    index4 is the optional width, index5 is optional popup action
	//    index 6 - is optional text width
	// e.g.    ["SynthOptionPopup", "Waveform", arrOptionData, 0, nil, {system.flagGuiUpd}],
	*guiSynthOptionPopup { arg item, w;
		// TXPopup.new  arg argParent, dimensions, label, items, action, initVal,
		//   initAction=false, labelWidth=80;
		classData.holdView = TXPopup(w, item.at(4) ? classData.viewWidth @ 20, "*"++item.at(1),
			item.at(2).at(item.at(3)).collect({arg item, i; item.at(0)}),
			{|view|
				// store current data to arrOptions
				argModule.arrOptions.put(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
				// rebuild synth
				argModule.rebuildSynth;
			},
			// get starting value
			argModule.arrOptions.at(item.at(3)),
			nil,
			item.at(6)?80
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		if (classData.holdView.labelView.notNil, {
			classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
		});
		classData.holdView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueNoAction_(argMod.arrOptions.at(item.at(3)));
			}
		);
	}

	// SynthOptionPopupPlusMinus _ same as SynthOptionPopup but with plus minus buttons
	// NOTE - this will automatically rebuild the synth once a synth option has been changed
	// arguments- index1 is text, index2 is arrOptionData, index3 is the index of arrOptions and arrOptionData to use,
	//    index4 is the optional width, index5 is optional popup action
	// e.g.    ["SynthOptionPopupPlusMinus", "Waveform", arrOptionData, 0, nil, {system.flagGuiUpd}],
	*guiSynthOptionPopupPlusMinus { arg item, w;
		classData.holdView = TXPopupPlusMinus(w, item.at(4) ? classData.viewWidth @ 20, "*"++item.at(1),
			item.at(2).at(item.at(3)).collect({arg item, i; item.at(0)}),
			{|view|
				// store current data to arrOptions
				argModule.arrOptions.put(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
				// rebuild synth
				argModule.rebuildSynth;
			},
			// get starting value
			argModule.arrOptions.at(item.at(3))
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueNoAction_(argMod.arrOptions.at(item.at(3)));
			}
		);
	}

	// SynthOptionList
	// NOTE - this will automatically rebuild the synth once a synth option has been changed
	// arguments- index1 is text, index2 is arrOptionData, index3 is the index of arrOptions and arrOptionData to use,
	//    index4 is the optional width, index5 is the optional height, index6 is optional action
	// e.g.    ["SynthOptionList", "Waveform", arrOptionData, 0, nil, nil, {system.flagGuiUpd}],
	*guiSynthOptionList { arg item, w;
		// TXListViewLabel.new(argParent, dimensions, label, items, action, initVal,initAction=false, labelWidth=80;
		classData.holdView = TXListViewLabel(w, (item.at(4) ? classData.viewWidth) @ (item.at(5) ? 80), "*"++item.at(1),
			item.at(2).at(item.at(3)).collect({arg item, i; item.at(0)}),
			{|view|
				// store current data to arrOptions
				argModule.arrOptions.put(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(6).notNil, {
					// run action function passing it view as arg
					item.at(6).value(view);
				});
				// rebuild synth
				argModule.rebuildSynth;
			},
			// get starting value
			argModule.arrOptions.at(item.at(3))
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.listView.stringColor_(TXColor.black).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueNoAction_(argMod.arrOptions.at(item.at(3)));
			}
		);
	}

	// SynthOptionListPlusMinus
	// NOTE - this will automatically rebuild the synth once a synth option has been changed
	// arguments- index1 is text, index2 is arrOptionData, index3 is the index of arrOptions and arrOptionData to use,
	//    index4 is the optional width, index5 is the optional height, index6 is optional action
	// e.g.    ["SynthOptionListPlusMinus", "Waveform", arrOptionData, 0, nil, nil, {system.flagGuiUpd}],
	*guiSynthOptionListPlusMinus { arg item, w;
		// TXListViewLabel.new(argParent, dimensions, label, items, action, initVal,initAction=false, labelWidth=80;
		classData.holdView = TXListViewLabelPlusMinus(w, (item.at(4) ? classData.viewWidth) @ (item.at(5) ? 80), "*"++item.at(1),
			item.at(2).at(item.at(3)).collect({arg item, i; item.at(0)}),
			{|view|
				// store current data to arrOptions
				argModule.arrOptions.put(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(6).notNil, {
					// run action function passing it view as arg
					item.at(6).value(view);
				});
				// rebuild synth
				argModule.rebuildSynth;
			},
			// get starting value
			argModule.arrOptions.at(item.at(3))
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.listView.stringColor_(TXColor.black).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueNoAction_(argMod.arrOptions.at(item.at(3)));
			}
		);
	}

	// TXMinMaxSlider
	// arguments- index1 is slider text, index2 is controlSpec, index3 is synth arg name to be updated
	// 	index4 is an optional ACTION function to be valued in views action function
	// e.g. ["TXMinMaxSlider", "BPM", ControlSpec(1, 999), "seqBPM"]
	*guiTXMinMaxSlider { arg item, w;
		classData.holdView = TXMinMaxSlider(w, classData.viewWidth @ 44, item.at(1), item.at(2),
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), view.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			}
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.value_(argModule.getSynthArgSpec(item.at(3)));
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.grey(0.9));
		classData.holdView.minNumberView.stringColor_(TXColor.black).background_(TXColor.grey(0.9));
		classData.holdView.maxNumberView.stringColor_(TXColor.black).background_(TXColor.grey(0.9));
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		classData.holdView.rangeView.background_(TXColor.sysGuiCol1.blend(TXColor.grey(0.55), 0.75)).knobColor_(TXColor.grey(0.75));
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueNoAction_(argMod.getSynthArgSpec(item.at(3)));
			}
		);
	}

	// TXMinMaxSliderSplit - separate values are given for each of the 3 gui objects
	// arguments- index1 is slider text, index2 is controlSpec, index3/4/5 are synth arg names to
	//	be updated for slider, min & max
	// 	index6 is an optional ACTION function to be valued in views action function
	// 	index7 is an optional preset array in the form:
	//		[["Presets:", []], ["1-16", [1,16]], ["17-32", [17,32]], ]
	// 	index 8 is optional label width, index 9 is optional number width,
	// 	index 10 is optional overall width,
	// e.g. ["TXMinMaxSliderSplit", "Freq", \freq, "freq", "freqMin", "freqMax"]
	*guiTXMinMaxSliderSplit { arg item, w;
		classData.holdView = TXMinMaxSlider(w, (item.at(10) ? classData.viewWidth) @ 44, item.at(1), item.at(2),
			{|view|
				var holdValueSplit;
				holdValueSplit = view.valueSplit;
				// set current value on synth
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), holdValueSplit.at(0),
						item.at(4), holdValueSplit.at(1), item.at(5), holdValueSplit.at(2) );
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), holdValueSplit.at(0));
				argModule.setSynthArgSpec(item.at(4), holdValueSplit.at(1));
				argModule.setSynthArgSpec(item.at(5), holdValueSplit.at(2));
				// if action function passed then value it
				if (item.at(6).notNil, {
					// run action function passing it view as arg
					item.at(6).value(view);
				});
			},
			nil, false, item.at(8) ? 80, item.at(9) ? 120,
			item.at(7)
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.valueSplit_([
			argModule.getSynthArgSpec(item.at(3)),
			argModule.getSynthArgSpec(item.at(4)),
			argModule.getSynthArgSpec(item.at(5))
		]);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.grey(0.9));
		classData.holdView.minNumberView.stringColor_(TXColor.black).background_(TXColor.grey(0.9));
		classData.holdView.maxNumberView.stringColor_(TXColor.black).background_(TXColor.grey(0.9));
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		classData.holdView.rangeView.background_(TXColor.sysGuiCol1.blend(TXColor.grey(0.55), 0.75)).knobColor_(TXColor.grey(0.75));
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueSplit_([
					argMod.getSynthArgSpec(item.at(3)),
					argMod.getSynthArgSpec(item.at(4)),
					argMod.getSynthArgSpec(item.at(5))
				]);
			}
		);
	}

	// TXMinMaxMidiNoteSldr - separate values are given for each of the 3 gui objects
	// arguments- index1 is slider text, index2 is controlSpec, index3/4/5 are synth arg names to
	//	be updated for slider, min & max
	// 	index6 is an optional ACTION function to be valued in views action function
	// 	index7 is an optional preset array in the form:
	//		[["Presets:", []], ["1-16", [1,16]], ["17-32", [17,32]], ]
	// 	index 8 is optional label width, index 9 is optional number width,
	// 	index 10 is optional overall width,
	// e.g. ["TXMinMaxMidiNoteSldr", "Note", classData.noteSpec, "note", "noteMin", "noteMax"]
	*guiTXMinMaxMidiNoteSldr { arg item, w;
		classData.holdView = TXMinMaxMidiNoteSldr(w, (item.at(10) ? classData.viewWidth) @ 44, item.at(1), item.at(2),
			{|view|
				var holdValueSplit;
				holdValueSplit = view.valueSplit;
				// set current value on synth
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), holdValueSplit.at(0),
						item.at(4), holdValueSplit.at(1), item.at(5), holdValueSplit.at(2) );
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), holdValueSplit.at(0));
				argModule.setSynthArgSpec(item.at(4), holdValueSplit.at(1));
				argModule.setSynthArgSpec(item.at(5), holdValueSplit.at(2));
				// if action function passed then value it
				if (item.at(6).notNil, {
					// run action function passing it view as arg
					item.at(6).value(view);
				});
			},
			nil, false, item.at(8) ? 80, item.at(9) ? 120,
			item.at(7)
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.valueSplit_([
			argModule.getSynthArgSpec(item.at(3)),
			argModule.getSynthArgSpec(item.at(4)),
			argModule.getSynthArgSpec(item.at(5))
		]);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.grey(0.9));
		classData.holdView.minNumberView.stringColor_(TXColor.black).background_(TXColor.grey(0.9));
		classData.holdView.maxNumberView.stringColor_(TXColor.black).background_(TXColor.grey(0.9));
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		classData.holdView.rangeView.background_(TXColor.sysGuiCol1.blend(TXColor.grey(0.55), 0.75)).knobColor_(TXColor.grey(0.75));
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueSplit_([
					argMod.getSynthArgSpec(item.at(3)),
					argMod.getSynthArgSpec(item.at(4)),
					argMod.getSynthArgSpec(item.at(5))
				]);
			}
		);
	}

	// TXMinMaxFreqNoteSldr - separate values are given for each of the 3 gui objects
	// arguments- index1 is slider text, index2 is controlSpec, index3/4/5 are synth arg names to
	//	be updated for slider, min & max
	// 	index6 is an optional ACTION function to be valued in views action function
	// 	index7 is an optional preset array in the form: [["Presets:", []], ["1-16", [1,16]], ["17-32", [17,32]], ]
	//  index8 is an optional width
	// e.g. ["TXMinMaxFreqNoteSldr", "Flange freq", holdControlSpec,"freq", "freqMin", "freqMax", nil, arrTimeRanges]
	*guiTXMinMaxFreqNoteSldr { arg item, w;
		classData.holdView = TXMinMaxFreqNoteSldr(w, item.at(8) ? classData.viewWidth @ 44, item.at(1), item.at(2),
			{|view|
				var holdValueSplit;
				holdValueSplit = view.valueSplit;
				// set current value on synth
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), holdValueSplit.at(0), item.at(4), holdValueSplit.at(1),
						item.at(5), holdValueSplit.at(2) );
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), holdValueSplit.at(0));
				argModule.setSynthArgSpec(item.at(4), holdValueSplit.at(1));
				argModule.setSynthArgSpec(item.at(5), holdValueSplit.at(2));
				// if action function passed then value it
				if (item.at(6).notNil, {
					// run action function passing it view as arg
					item.at(6).value(view);
				});
			},
			nil, false, 80, 120,
			item.at(7)
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.valueSplit_([
			argModule.getSynthArgSpec(item.at(3)),
			argModule.getSynthArgSpec(item.at(4)),
			argModule.getSynthArgSpec(item.at(5))
		]);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.grey(0.9));
		classData.holdView.minNumberView.stringColor_(TXColor.black).background_(TXColor.grey(0.9));
		classData.holdView.maxNumberView.stringColor_(TXColor.black).background_(TXColor.grey(0.9));
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		classData.holdView.rangeView.background_(TXColor.sysGuiCol1.blend(TXColor.grey(0.55), 0.75)).knobColor_(TXColor.grey(0.75));
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueSplit_([
					argMod.getSynthArgSpec(item.at(3)),
					argMod.getSynthArgSpec(item.at(4)),
					argMod.getSynthArgSpec(item.at(5))
				]);
			}
		);
	}

	// TXTimeBpmMinMaxSldr - separate values are given for each of the 3 gui objects
	// arguments- index1 is slider text, index2 is controlSpec, index3/4/5 are synth arg names to
	//	be updated for slider, min & max
	// 	index6 is an optional ACTION function to be valued in views action function
	// e.g. ["TXTimeBpmMinMaxSldr", "Delay ms/bpm", holdControlSpec, "delay", "delayMin", "delayMax"]
	*guiTXTimeBpmMinMaxSldr { arg item, w;
		classData.holdView = TXTimeBpmMinMaxSldr(w, classData.viewWidth @ 44, item.at(1), item.at(2),
			{|view|
				var holdValueSplit;
				holdValueSplit = view.valueSplit;
				// set current value on synth
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), holdValueSplit.at(0), item.at(4), holdValueSplit.at(1),
						item.at(5), holdValueSplit.at(2) );
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), holdValueSplit.at(0));
				argModule.setSynthArgSpec(item.at(4), holdValueSplit.at(1));
				argModule.setSynthArgSpec(item.at(5), holdValueSplit.at(2));
				// if action function passed then value it
				if (item.at(6).notNil, {
					// run action function passing it view as arg
					item.at(6).value(view);
				});
			}
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.valueSplit_([
			argModule.getSynthArgSpec(item.at(3)),
			argModule.getSynthArgSpec(item.at(4)),
			argModule.getSynthArgSpec(item.at(5))
		]);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.grey(0.9));
		classData.holdView.minNumberView.stringColor_(TXColor.black).background_(TXColor.grey(0.9));
		classData.holdView.maxNumberView.stringColor_(TXColor.black).background_(TXColor.grey(0.9));
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		classData.holdView.bpmNumberView.background_(TXColor.paleBlue2);
		classData.holdView.rangeView.background_(TXColor.sysGuiCol1.blend(TXColor.grey(0.55), 0.75)).knobColor_(TXColor.grey(0.75));
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueSplit_([
					argMod.getSynthArgSpec(item.at(3)),
					argMod.getSynthArgSpec(item.at(4)),
					argMod.getSynthArgSpec(item.at(5))
				]);
			}
		);
	}

	// TXFreqBpmMinMaxSldr - separate values are given for each of the 3 gui objects
	// arguments- index1 is slider text, index2 is controlSpec, index3/4/5 are synth arg names to
	//	be updated for slider, min & max
	// 	index6 is an optional ACTION function to be valued in views action function
	// 	index7 is an optional preset array in the form:
	//		[["Presets:", []], ["1-16", [1,16]], ["17-32", [17,32]], ]
	// 	index 8 is optional label width, index 9 is optional number width,
	// 	index 10 is optional overall width,
	// e.g. ["TXFreqBpmMinMaxSldr", "Freq cps/bpm", holdControlSpec, "freq", "freqMin", "freqMax"]
	*guiTXFreqBpmMinMaxSldr { arg item, w;
		classData.holdView = TXFreqBpmMinMaxSldr(w, (item.at(10) ? classData.viewWidth) @ 44, item.at(1), item.at(2),
			{|view|
				var holdValueSplit;
				holdValueSplit = view.valueSplit;
				// set current value on synth
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), holdValueSplit.at(0), item.at(4), holdValueSplit.at(1),
						item.at(5), holdValueSplit.at(2) );
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), holdValueSplit.at(0));
				argModule.setSynthArgSpec(item.at(4), holdValueSplit.at(1));
				argModule.setSynthArgSpec(item.at(5), holdValueSplit.at(2));
				// if action function passed then value it
				if (item.at(6).notNil, {
					// run action function passing it view as arg
					item.at(6).value(view);
				});
			},
			nil, false, item.at(8) ? 80, item.at(9) ? 120,
			item.at(7)
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.valueSplit_([
			argModule.getSynthArgSpec(item.at(3)),
			argModule.getSynthArgSpec(item.at(4)),
			argModule.getSynthArgSpec(item.at(5))
		]);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.grey(0.9));
		classData.holdView.minNumberView.stringColor_(TXColor.black).background_(TXColor.grey(0.9));
		classData.holdView.maxNumberView.stringColor_(TXColor.black).background_(TXColor.grey(0.9));
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		classData.holdView.bpmNumberView.background_(TXColor.paleBlue2);
		classData.holdView.rangeView.background_(TXColor.sysGuiCol1.blend(TXColor.grey(0.55), 0.75)).knobColor_(TXColor.grey(0.75));
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueSplit_([
					argMod.getSynthArgSpec(item.at(3)),
					argMod.getSynthArgSpec(item.at(4)),
					argMod.getSynthArgSpec(item.at(5))
				]);
			}
		);
	}

	// TXMultiButton
	// arguments- index1 is an array of textStrings
	// index2 is ACTION function which is passed arg of indexNo of button pressed
	// index3 is an optional width (default classData.viewWidth)
	// index4 is an optional height (default 80)
	// index5 is an optional height of rows (default 24)
	// index6 is an optional stringColor
	// index7 is an optional background
	*guiTXMultiButton { arg item, w;
		var holdWidth, holdHeight, holdButtonWidth, holdRowHeight;
		holdWidth =  item.at(3) ? classData.viewWidth;
		holdHeight = item.at(4) ? 24;
		holdRowHeight = item.at(5) ? 24;
		this.nextline(w);
		classData.holdView = TXMultiButton(w, holdWidth @ holdHeight, item.at(1).value, item.at(2), holdRowHeight, item.at(6), item.at(7));
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
	}

	// TXMultiSlider
	// arguments- index1 is row text, index2 is controlSpec, index3 is synth arg name to be updated,
	// index4 in no. items in row function, index5 is an optional ACTION function to be valued in views action
	// index6 is an optional height
	// index7 is an optional orientation string ("Vertical" or "Horizontal"-default)
	// index8 is an optional value for showing the Clone1 button (1 means show it)
	// index9 is an optional label width
	// index10 is an optional width for each slider
	// index11/12 are gridRows/Cols for putting a grid behind view.
	// e.g. ["TXMultiSlider", "Velocity", ControlSpec(0, 100), "arrVelocities", 16]
	*guiTXMultiSlider { arg item, w;
		var holdHeight;
		holdHeight = item.at(6) ? 44;
		this.nextline(w);
		classData.holdView = TXMultiSlider(w, ((item.at(4).value * (item.at(10) ? 24)) + (item.at(9) ? 80) + 12) @ holdHeight,
			item.at(1).value, item.at(2),
			{|view|
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
			},
			// get initial value
			argModule.getSynthArgSpec(item.at(3)) ? Array.fill( (item.at(4).value ? 8), item.at(2).default),
			labelWidth: item.at(9) ? 80, showClone1: item.at(8),
			gridRows: item.at(11), gridCols: item.at(12)
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.multiSliderView.isFilled_(true).fillColor_(TXColor.sysGuiCol1.copy.alpha_(0.5));
		classData.holdView.multiSliderView.valueThumbSize_(1);
		if (item.at(7) == "Vertical", {
			classData.holdView.multiSliderView.indexIsHorizontal_(false);
			classData.holdView.multiSliderView.indexThumbSize_((holdHeight/ (item.at(4).value ? 8) - 1));
			classData.holdView.multiSliderView.gap_(1);
		},{
			classData.holdView.multiSliderView.indexThumbSize_((item.at(10)  ? 24) - 2);
			classData.holdView.multiSliderView.gap_(2);
		});
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.value_(argMod.getSynthArgSpec(item.at(3)) ?
					Array.fill( (item.at(4).value ? 8), item.at(2).default));
			}
		);
	}

	// TXMultiSliderNo
	// arguments- index1 is row text, index2 is controlSpec, index3 is synth arg name to be updated with view value,
	// index4 in no. items in row function, index5 is an optional ACTION function to be valued in views action
	// index6 is optional synth arg name where a show/hide varible is kept - if argument is
	//    present a +/- button will be used to show/hide multislider.
	// index7 is optional synth arg name where index of first item to be displayed is stored
	// index8 is optional scroll increment
	// index9 is optional height (of both multislider and multinumber0)
	// index10 is optional ScrollView init action,
	// e.g. ["TXMultiSliderNo", "Velocity", ControlSpec(0, 100), "arrVelocities", 16, nil, "showVelocityBars"]
	*guiTXMultiSliderNo { arg item, w;
		var viewWidth;
		this.nextline(w);
		classData.holdView = nil;
		//  set classData.holdStartIndex
		if (item.at(7).notNil, {
			classData.holdStartIndex = argModule.getSynthArgSpec(item.at(7));
		},{
			classData.holdStartIndex = 0;
		});
		if (item.at(10).notNil, {
			viewWidth = 580;
		});
		// if set to hide multislider
		if (item.at(6).notNil, {
			if (argModule.getSynthArgSpec(item.at(6)) == 0, {
				classData.holdView = TXMultiNumber(w, viewWidth @ 20, item.at(1).value, item.at(2),
					{|view|
						var holdArr;
						// get initial value
						holdArr = argModule.getSynthArgSpec(item.at(3));
						view.value.do({ arg val, ind;
							holdArr = holdArr.put(classData.holdStartIndex + ind, val);
						});
						// store current data to synthArgSpecs
						argModule.setSynthArgSpec(item.at(3), holdArr);
						// if action function passed then value it
						if (item.at(5).notNil, {
							// run action function passing it view as arg
							item.at(5).value(view);
						});
					},
					// get initial value & restrict to display range range
					argModule.getSynthArgSpec(item.at(3))
					.copyRange(classData.holdStartIndex,classData.holdStartIndex+item.at(4).value -1),

					scrollInc: item.at(8), scrollViewWidth: viewWidth
				);
				if (item.at(10).notNil, {
					item.at(10).value(classData.holdView.scrollView);
				});
				argModule.arrControls = argModule.arrControls.add(classData.holdView);

				// add screen update function
				system.addScreenUpdFunc(
					[classData.holdView, argModule],
					{ arg argArray;
						var argView = argArray.at(0), argMod = argArray.at(1), holdStartIndex;
						//  set holdStartIndex
						if (item.at(7).notNil, {
							holdStartIndex = argMod.getSynthArgSpec(item.at(7));
						},{
							holdStartIndex = 0;
						});
						//  set value
						argView.value_(argModule.getSynthArgSpec(item.at(3))
							.copyRange(holdStartIndex, holdStartIndex + item.at(4).value - 1));
					}
				);
				classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
				// button to show multislider
				Button(w, 20 @ 20)
				.states_([
					["+", TXColor.white, TXColor.sysGuiCol1]
				])
				.action_({|view|
					argModule.setSynthArgSpec(item.at(6), 1);
					// recreate view
					system.showView;
				})
			});
		});
		if (classData.holdView.isNil, {
			classData.holdView = TXMultiSliderNo(w, ((item.at(4).value * 24)+78) @ ((item.at(9) ? 124) - 24),
				item.at(1).value, item.at(2),
				{|view|
					var holdArr;
					// get initial value
					holdArr = argModule.getSynthArgSpec(item.at(3));
					view.value.do({ arg val, ind;
						holdArr.put(classData.holdStartIndex + ind, val);
					});
					// store current data to synthArgSpecs
					argModule.setSynthArgSpec(item.at(3), holdArr);
					// if action function passed then value it
					if (item.at(5).notNil, {
						// run action function passing it view as arg
						item.at(5).value(view);
					});
				},
				// get initial value & restrict to display range range
				argModule.getSynthArgSpec(item.at(3)) .copyRange(classData.holdStartIndex,classData.holdStartIndex + item.at(4).value -1),
				labelWidth: 78,
				scrollViewWidth: viewWidth
			);
			if (item.at(10).notNil, {
				item.at(10).value(classData.holdView.scrollView);
				item.at(10).value(classData.holdView.scrollView2);
			});
			argModule.arrControls = argModule.arrControls.add(classData.holdView);
			// add screen update function
			system.addScreenUpdFunc(
				[classData.holdView, argModule],
				{ arg argArray;
					var argView = argArray.at(0), argMod = argArray.at(1), holdStartIndex;
					//  set holdStartIndex
					if (item.at(7).notNil, {
						holdStartIndex = argMod.getSynthArgSpec(item.at(7));
					},{
						holdStartIndex = 0;
					});
					//  set value
					argView.valueNoAction_(argMod.getSynthArgSpec(item.at(3))
						.copyRange(holdStartIndex, holdStartIndex + item.at(4).value - 1));
				}
			);
			classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			classData.holdView.multiSliderView.isFilled_(true).fillColor_(TXColor.sysGuiCol1);
			classData.holdView.multiSliderView.valueThumbSize_(1);
			classData.holdView.multiSliderView.indexThumbSize_(19);
			classData.holdView.multiSliderView.gap_(5);
			// button to hide multislider
			Button(w, 20 @ 20)
			.states_([
				["-", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				argModule.setSynthArgSpec(item.at(6), 0);
				// recreate view
				system.showView;
			})
		});
	}

	// TXMultiSliderNoGroup
	// (note: this version uses an array of synth arg names)
	// arguments- index1 is row text, index2 is controlSpec,
	// index3 is an array of synth arg name to be updated with view values,
	// index4 is an optional ACTION function to be valued in views action
	// index5 is optional height (of multislider and multinumber together)
	// index6 is optional width
	// e.g. ["TXMultiSliderNoGroup", "Levels", ControlSpec(0, 100), ["level1", "level2", "level3"], nil, 100]
	*guiTXMultiSliderNoGroup { arg item, w;
		var arrSynthArgNames;
		arrSynthArgNames = item.at(3);
		this.nextline(w);
		classData.holdView = TXMultiSliderNo(w, item.at(6) ?? classData.viewWidth  @ ((item.at(5) ? 124) - 24),
			item.at(1), item.at(2),
			{|view|
				var arrVals;
				arrVals = view.value;
				// store current data to synthArgSpecs
				arrSynthArgNames.do({ arg string, ind;
					argModule.setSynthArgSpec(string, arrVals[ind]);
				});
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get initial values
			arrSynthArgNames.collect({ arg string, ind;
				argModule.getSynthArgSpec(string);
			});
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				//  set values
				argView.valueNoAction_(
					arrSynthArgNames.collect({ arg string, ind;
						argMod.getSynthArgSpec(string);
					});
				);
			}
		);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.multiSliderView.isFilled_(true).fillColor_(TXColor.sysGuiCol1);
		classData.holdView.multiSliderView.valueThumbSize_(1);
		classData.holdView.multiSliderView.indexThumbSize_(
			(( (item.at(6) ?? classData.viewWidth  ) - 80) / item.at(3).size ) - 4
		);
		classData.holdView.multiSliderView.elasticMode_(1);
		classData.holdView.multiSliderView.gap_(5);
	}

	//TXMultiSwitch - this is a modified version of TXMultiSlider
	// arguments- index1 is row text, index2 is synth arg name to be updated,
	// index3 in no. items in row function, index4 is an optional ACTION function to be valued in views action
	// index5 is optional synth arg name where index of first item to be displayed is stored
	// index6 is optional ScrollView init action,
	// index7 is optional label width,
	// e.g. ["TXMultiSwitch", "Step on/off", "arrOnOffSteps", 16]
	*guiTXMultiSwitch { arg item, w;
		var viewWidth;
		this.nextline(w);
		//  set classData.holdStartIndex
		if (item.at(5).notNil, {
			classData.holdStartIndex = argModule.getSynthArgSpec(item.at(5));
		},{
			classData.holdStartIndex = 0;
		});
		if (item.at(6).notNil, {
			viewWidth = 580;
		});
		classData.holdView = TXMultiSlider(w, ((item.at(3).value * 24)+78) @ 24, item.at(1), ControlSpec(0, 1, step: 1),
			{|view|
				var holdArr;
				// get initial value
				holdArr = argModule.getSynthArgSpec(item.at(2));
				view.value.do({ arg val, ind;
					holdArr.put(classData.holdStartIndex + ind, val);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), holdArr);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get initial value & restrict to display range range
			argModule.getSynthArgSpec(item.at(2)) .copyRange(classData.holdStartIndex,classData.holdStartIndex + item.at(3).value -1),
			labelWidth: 78,
			scrollViewWidth: viewWidth
		);
		if (item.at(6).notNil, {
			item.at(6).value(classData.holdView.scrollView);
		});
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.multiSliderView.isFilled_(true).fillColor_(TXColor.sysGuiCol1);
		// classData.holdView.multiSliderView.valueThumbSize_(0.1);
		// classData.holdView.multiSliderView.indexThumbSize_(23);
		// classData.holdView.multiSliderView.gap_(1);
		classData.holdView.multiSliderView.valueThumbSize_(1);
		classData.holdView.multiSliderView.indexThumbSize_(19);
		classData.holdView.multiSliderView.gap_(5);
		classData.holdView.multiSliderView.step_(1);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1), holdStartIndex;
				//  set holdStartIndex
				if (item.at(7).notNil, {
					holdStartIndex = argMod.getSynthArgSpec(item.at(7));
				},{
					holdStartIndex = 0;
				});
				//  set value
				argView.value_(argMod.getSynthArgSpec(item.at(2))
					.copyRange(holdStartIndex, holdStartIndex + item.at(3).value -1));
			}
		);
	}

	// TXMultiTrackView
	// arguments- index1 is module,
	//	index2 is action function
	// 	index3 is a bpm value function
	// 	index4 is a beats per bar value function
	// 	index5 is optional ScrollView init action
	// 	index6 is optional ScrollView update action
	// 	index7 is optional height
	// 	index8 is initial action function
	// e.g. ["TXMultiTrackView", arrTracks, {arg view;},
	//		{this.getSynthArgSpec("bpm");},
	//		{this.getSynthArgSpec("beatsPerBar");},
	//		{arg view; holdScrollView = view;},
	//		{arg view; holdVisibleOrigin = view.visibleOrigin; }
	//	],
	// ======= *new {arg argSystem, argParent, dimensions, argArrTracks, argAction, argBpm, argBeatsPerBar, scrollViewAction;
	*guiTXMultiTrackView { arg item, w;
		var extraWidth = 0;
		if (item.at(6).notNil, {extraWidth = 40;});
		classData.holdView = TXMultiTrackView(system, w, (classData.viewWidth + extraWidth) @ (item.at(7) ? 412), item.at(1),
			item.at(2), item.at(3).value, item.at(4).value, item.at(6), item.at(8));
		if (item.at(5).notNil, {
			item.at(5).value(classData.holdView.scrollView);
		});
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				// not used currently
			}
		);
	}	// end of TXMultiTrackView


	// TXMultiNumber
	// arguments- index1 is row text, index2 is controlSpec, index3 is synth arg name to be updated,
	// index4 in no. items in row function, index5 is an optional ACTION function to be valued in views action
	// index6 is optional synth arg name where index of first item to be displayed is stored
	// index7 is optional ScrollView init action,
	// e.g. ["TXMultiNumber", "Rand octave", ControlSpec(1, 9, step: 1), "arrRandOctaves", 16]
	*guiTXMultiNumber { arg item, w;
		var viewWidth;
		this.nextline(w);
		//  set classData.holdStartIndex
		if (item.at(6).notNil, {
			classData.holdStartIndex = argModule.getSynthArgSpec(item.at(6));
		},{
			classData.holdStartIndex = 0;
		});
		if (item.at(7).notNil, {
			viewWidth = 580;
		});
		classData.holdView = TXMultiNumber(w, viewWidth @ 20, item.at(1), item.at(2),
			{|view|
				var holdArr;
				// get initial value
				holdArr = argModule.getSynthArgSpec(item.at(3));
				view.value.do({ arg val, ind;
					holdArr.put(classData.holdStartIndex + ind, val);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), holdArr);
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
			},
			// get initial value & restrict to display range range
			argModule.getSynthArgSpec(item.at(3))
			.copyRange(classData.holdStartIndex,classData.holdStartIndex + item.at(4).value -1),
			scrollViewWidth: viewWidth
		);
		if (item.at(7).notNil, {
			item.at(7).value(classData.holdView.scrollView);
		});
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1), holdStartIndex;
				//  set holdStartIndex
				if (item.at(6).notNil, {
					holdStartIndex = argMod.getSynthArgSpec(item.at(6));
				},{
					holdStartIndex = 0;
				});
				//  set value
				argView.value_(argMod.getSynthArgSpec(item.at(3))
					.copyRange(holdStartIndex,holdStartIndex + item.at(4).value -1));
			}
		);
	}

	// TXMultiCheckBox
	// arguments- index1 is row text, index2 is synth arg name to be updated, index3 in no. items in row function
	// 	index4 is an optional ACTION function to be valued in views action
	// e.g. ["TXMultiCheckBox", "Step on/off", "arrOnOffSteps", 16]
	*guiTXMultiCheckBox { arg item, w;
		this.nextline(w);
		classData.holdView = TXMultiCheckBox(w, classData.viewWidth @ 20, item.at(1),
			{|view|
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), view.value);
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get initial value
			argModule.getSynthArgSpec(item.at(2))
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.value_(argMod.getSynthArgSpec(item.at(2)));
			}
		);
	}

	// TXMultiKnob
	// NOTE: TXMultiKnob (and variations below) is called differently to other multiXXX types since index 2 is an array of
	//       synth arg names, not just a single one. This is to avoid arrays as syth args.
	// arguments- index1 is row text,
	//   index2 is array of synth arg names to be updated,
	//   index3 in no. items in row function
	// 	index4 is an optional ControlSpec
	// 	index5 is an optional ACTION function to be valued in views action
	// e.g. ["TXMultiKnob", "Input 1", ["In1Out1", "In1Out2", "In1Out3", "In1Out4"], 4]
	*guiTXMultiKnob { arg item, w;
		var holdWidth;
		holdWidth = 84 + (54 * item.at(3));
		classData.holdView = TXMultiKnob(w, holdWidth @ 40, item.at(1),
			item.at(4) ? ControlSpec.new,
			{|view|
				var arrVals;
				arrVals = view.value;
				// store current data to synthArgSpecs
				item.at(2).do({arg string, i;
					argModule.setSynthValue(string, arrVals[i]);
				});
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
			},
			// get initial values
			item.at(2).collect({arg string, i;
				argModule.getSynthArgSpec(string);
			});
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.value_(
					item.at(2).collect({arg string, i;
						argMod.getSynthArgSpec(string);
					});
				);
			}
		);
	}

	// TXMultiKnobNo
	// arguments- index1 is row text,
	//   index2 is array of synth arg names to be updated,
	//   index3 in no. items in row function
	// 	index4 is an optional ControlSpec
	// 	index5 is an optional ACTION function to be valued in views action
	// 	index6 is an optional number of spacer gaps before first knob, default 0
	// e.g. ["TXMultiKnobNo", "Input 1", ["In1Out1", "In1Out2", "In1Out3", "In1Out4"], 4]
	*guiTXMultiKnobNo { arg item, w;
		var holdWidth;
		holdWidth = 84 + (54 * item.at(3));
		classData.holdView = TXMultiKnobNo(w, holdWidth @ 40, item.at(1),
			item.at(4) ? ControlSpec.new,
			{|view|
				var arrVals;
				arrVals = view.value;
				// store current data to synthArgSpecs
				item.at(2).do({arg string, i;
					argModule.setSynthValue(string, arrVals[i]);
				});
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
			},
			// get initial values
			item.at(2).collect({arg string, i;
				argModule.getSynthArgSpec(string);
			}),
			numSpacers: (item.at(6) ? 0)
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.value_(
					item.at(2).collect({arg string, i;
						argMod.getSynthArgSpec(string);
					});
				);
			}
		);
	}

	// TXMultiKnobNoUnmap - value are displayed mapped but stored as unmapped
	// arguments- index1 is row text,
	//   index2 is array of synth arg names to be updated,
	//   index3 in no. items in row function
	// 	index4 is an optional ControlSpec
	// 	index5 is an optional ACTION function to be valued in views action
	// 	index6 is an optional number of spacer gaps before first knob, default 0
	// 	index7 is an optional knob width
	// e.g. ["TXMultiKnobNoUnmap", "Input 1", ["In1Out1", "In1Out2", "In1Out3", "In1Out4"], 4]
	*guiTXMultiKnobNoUnmap { arg item, w;
		var holdSpec = item.at(4) ? ControlSpec.new;
		var knobWidth = item.at(7) ? 50;
		var holdWidth = 84 + ((knobWidth + 4) * item.at(3));
		classData.holdView = TXMultiKnobNo(w, holdWidth @ 40, item.at(1), holdSpec,
			{|view|
				var arrVals;
				arrVals = view.value;
				// store current data to synthArgSpecs
				item.at(2).do({arg string, i;
					argModule.setSynthValue(string, holdSpec.unmap(arrVals[i]));
				});
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
			},
			// get initial values
			item.at(2).collect({arg string, i;
				holdSpec.map(argModule.getSynthArgSpec(string));
			}),
			numSpacers: (item.at(6) ? 0),
			knobWidth: knobWidth
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.value_(
					item.at(2).collect({arg string, i;
						holdSpec.map(argMod.getSynthArgSpec(string));
					});
				);
			}
		);
	}

	// TXMultiKnobMidiNote
	// arguments- index1 is row text,
	//   index2 is array of synth arg names to be updated,
	//   index3 in no. items in row function
	// 	index4 is an optional ControlSpec
	// 	index5 is an optional ACTION function to be valued in views action
	// e.g. ["TXMultiKnobMidiNote", "Input 1", ["In1Out1", "In1Out2", "In1Out3", "In1Out4"], 4]
	*guiTXMultiKnobMidiNote { arg item, w;
		var holdWidth;
		holdWidth = 84 + (54 * item.at(3));
		classData.holdView = TXMultiKnobMidiNote(w, holdWidth @ 40, item.at(1),
			item.at(4) ? ControlSpec.new,
			{|view|
				var arrVals;
				arrVals = view.value;
				// store current data to synthArgSpecs
				item.at(2).do({arg string, i;
					argModule.setSynthValue(string, arrVals[i]);
				});
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
			},
			// get initial values
			item.at(2).collect({arg string, i;
				argModule.getSynthArgSpec(string);
			});
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.value_(
					item.at(2).collect({arg string, i;
						argMod.getSynthArgSpec(string);
					});
				);
			}
		);
	}

	// TXCurveDraw
	// arguments- index1 is text, index2 is initial value array function,
	// 	index3 is an optional ACTION function to be valued in views action
	// 	index4 is an optional value array function for initial slots data (see TXCurveDraw for details)
	// 	index5 is an optional string for which kind of presets to show (e.g. "Warp")
	// 	index6 is an optional curve width
	// 	index7 is an optional curve height
	// 	index8 is an optional string for reset action (e.g. "Ramp", or "Zero")
	// 	index9 is an optional synth arg name for no. of grid rows (1-99)
	// 	index10 is an optional synth arg name for no. of grid columns (1-99)
	// 	index11 is an optional label for the x-axis
	// 	index12 is an optional label for the y-axis
	// 	index13 is an optional Event function for local data storage
	// 	index14 is an optional drawVertical (default false)
	// e.g. ["TXCurveDraw", "Warp curve", {arrCurveValues}, {arg view; arrCurveValues = view.value; this.bufferStore(view.value);}],

	*guiTXCurveDraw { arg item, w;
		this.nextline(w);
		classData.holdView = TXCurveDraw(w, classData.viewWidth @ 300, item.at(1).value,
			{|view|
				// if action function passed then value it
				if (item.at(3).notNil, {
					// run action function passing it view as arg
					item.at(3).value(view);
				});
			},
			// get initial value
			item.at(2).value,
			initSlotVals: item.at(4).value,
			showPresets: item.at(5),
			curveWidth: item.at(6) ? 263,
			curveHeight: item.at(7) ? 263,
			resetAction: item.at(8),
			gridRowsFunc: {argModule.getSynthArgSpec(item.at(9))},
			gridColsFunc: {argModule.getSynthArgSpec(item.at(10))},
			xLabel: item.at(11),
			yLabel: item.at(12),
			dataEvent: item.at(13).value,
			drawVertical: item.at(14)
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		//	classData.holdView.multiSliderView.isFilled_(true).fillColor_(TXColor.sysGuiCol1);
		//classData.holdView.multiSliderView.strokeColor_(TXColor.sysGuiCol1);
	}

	// TXEQCurveDraw
	// arguments- index1 is text, index2 is initial value array function,
	// 	index3 is an optional ACTION function to be valued in views action
	// 	index4 is an optional value array function for initial slots data (see TXEQCurveDraw for details)
	// 	index5 is an optional array of valid frequencies to be displayed
	// e.g. ["TXEQCurveDraw", "EQ curve", {arrCurveValues}, {arg view; arrCurveValues = view.value; this.bufferStore(view.value);}],

	*guiTXEQCurveDraw { arg item, w;
		this.nextline(w);
		classData.holdView = TXEQCurveDraw(w, classData.viewWidth @ 300, item.at(1),
			{|view|
				// if action function passed then value it
				if (item.at(3).notNil, {
					// run action function passing it view as arg
					item.at(3).value(view);
				});
			},
			// get initial value
			item.at(2).value,
			initSlotVals: item.at(4).value,
			numSlots: item.at(2).value.size,
			arrSlotFreqs: item.at(5)
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		//	classData.holdView.multiSliderView.isFilled_(true).fillColor_(TXColor.sysGuiCol1);
		classData.holdView.multiSliderView.strokeColor_(TXColor.sysGuiCol1);
	}

	// TXWaveTableSpecs
	// arguments- index1 is text, index2 is initial value array function,
	// 	index3 is an optional ACTION function to be valued in views action
	// 	index4 is an optional value array function for initial slots data (see TXCurveDraw for details)
	// 	index5 is an optional function or value of max no of harmonices (default 32)
	//	index6 is an optional function or value 0/1 to show the wavetable processes gui (default 1 to show)
	//	index7 is an optional height, index8 is an optional dataEvent function
	// e.g. ["TXWaveTableSpecs", "Wavetables", {arrWaveSpecs},
	//		{arg view; arrWaveSpecs = view.value; arrSlotData = view.arrSlotData; this.updateBuffers(view.value);},
	//		{arrSlotData}],

	*guiTXWaveTableSpecs { arg item, w;
		//this.nextline(w);
		classData.holdView = TXWaveTableSpecs(w, classData.viewWidth @ (item.at(7) ? 300), item.at(1),
			{|view|
				// if action function passed then value it
				if (item.at(3).notNil, {
					// run action function passing it view as arg
					item.at(3).value(view);
				});
			},
			// get initial value
			item.at(2).value,
			initSlotVals: item.at(4).value,
			argMaxNoHarmonics: (item.at(5).value ? 32),
			argShowProcesses: (item.at(6).value ? 1),
			argDataEvent: (item.at(8).value),
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		//	classData.holdView.multiSliderView.isFilled_(true).fillColor_(TXColor.sysGuiCol1);
		classData.holdView.multiSliderView.strokeColor_(TXColor.sysGuiCol1);
	}

	// MIDINote
	// arguments- index1 is text, index2 is synth arg name to be updated,
	// index3 is an optional ACTION function to be valued in views action
	*guiMIDINote { arg item, w;
		classData.holdView = TXNumber(w, classData.viewWidth @ 20, item.at(1), ControlSpec(0, 127, step: 1),
			{|view|
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), view.value);
				// update string text
				classData.holdNoteString.string = TXGetMidiNoteString(classData.holdView.value);
				// if action function passed then value it
				if (item.at(3).notNil, {
					// run action function passing it view as arg
					item.at(3).value(view);
				});
			},
			// get initial value
			argModule.getSynthArgSpec(item.at(2)),
			false, 80, 44
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// create text string for note base
		classData.holdNoteString = StaticText(w, 44 @ 20)
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdNoteString.string = TXGetMidiNoteString(classData.holdView.value);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.numberView.value_(argMod.getSynthArgSpec(item.at(2)));
			}
		);
	}

	// MidiNoteRow
	// arguments- index1/2 is synth arg names to be updated for note base and array of note shifts,
	// index3 in no. items in row function
	// index4 is optional synth arg name where index of first item to be displayed is stored
	// index5 is an optional ACTION function to be valued in views action
	// e.g. ["MidiNoteRow", "seqNoteBase", "arrNotes", 16]
	*guiMidiNoteRow { arg item, w;
		// start on new line
		this.nextline(w);
		// create row for note shift steps
		classData.holdNoteTexts = TXMultiTextBox(w, classData.viewWidth @ 20, "Note text", Array.fill(item.at(3).value," "));
		classData.holdNoteTexts.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdNoteTexts.arrTextViews.do({ arg item, i;
			item.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		});
		// start on new line
		this.nextline(w);
		//  set classData.holdStartIndex
		if (item.at(4).notNil, {
			classData.holdStartIndex = argModule.getSynthArgSpec(item.at(4));
		},{
			classData.holdStartIndex = 0;
		});
		// create row for note shift steps
		classData.holdNoteShiftRow = TXMultiNumber(w, classData.viewWidth @ 20, "Note val", ControlSpec(-127, 127, step: 1, default: 0),
			{|view|
				var holdArr;
				// get initial value
				holdArr = argModule.getSynthArgSpec(item.at(2));
				view.value.do({ arg val, ind;
					holdArr.put(classData.holdStartIndex + ind, val);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), holdArr);
				// update string texts
				classData.holdNoteTexts.strings = classData.holdNoteShiftRow.value.collect({ arg item, i;
					TXGetMidiNoteString(classData.holdNoteBase.value + item.value);
				});
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
			},
			// get initial value & restrict to display range range
			argModule.getSynthArgSpec(item.at(2)) .copyRange(classData.holdStartIndex,classData.holdStartIndex + item.at(3).value -1),
			// set scroll increment to 1
			scrollInc: 1
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdNoteShiftRow);
		classData.holdNoteShiftRow.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// start on new line
		this.nextline(w);
		// create row for note base
		classData.holdNoteBase = TXNumber(w, classData.viewWidth @ 20, "Note base", ControlSpec(0, 127, step: 1),
			{|view|
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(1), view.value);
				// update string text
				classData.holdNoteString.string = TXGetMidiNoteString(classData.holdNoteBase.value);
				// update string texts
				classData.holdNoteTexts.strings = classData.holdNoteShiftRow.value.collect({ arg item, i;
					TXGetMidiNoteString(classData.holdNoteBase.value + item.value);
				});
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
			},
			// get initial value
			argModule.getSynthArgSpec(item.at(1)),
			false, 80, 44
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdNoteBase);
		classData.holdNoteBase.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// create text string for note base
		classData.holdNoteString = StaticText(w, 44 @ 20)
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdNoteString.string = TXGetMidiNoteString(classData.holdNoteBase.value);
		// create buttons to move note base up/down 1/12
		Button(w, 32 @ 20)
		.states_([["-1", TXColor.white, TXColor.sysGuiCol1]])
		.action_({|view|
			classData.holdNoteBase.valueAction = (classData.holdNoteBase.value - 1).max(0).min(127);
		});
		Button(w, 32 @ 20)
		.states_([["+1", TXColor.white, TXColor.sysGuiCol1]])
		.action_({|view|
			classData.holdNoteBase.valueAction = (classData.holdNoteBase.value + 1).max(0).min(127);
		});
		Button(w, 32 @ 20)
		.states_([["-12", TXColor.white, TXColor.sysGuiCol1]])
		.action_({|view|
			classData.holdNoteBase.valueAction = (classData.holdNoteBase.value - 12).max(0).min(127);
		});
		Button(w, 32 @ 20)
		.states_([["+12", TXColor.white, TXColor.sysGuiCol1]])
		.action_({|view|
			classData.holdNoteBase.valueAction = (classData.holdNoteBase.value + 12).max(0).min(127);
		});
		// create text string for note base
		classData.holdNoteTexts.strings = classData.holdNoteShiftRow.value.collect({ arg item, i;
			TXGetMidiNoteString(classData.holdNoteBase.value + item);
		});
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdNoteShiftRow, classData.holdNoteBase, classData.holdNoteString, classData.holdNoteTexts, argModule],
			{ arg argArray;
				var holdNoteShiftRow = argArray.at(0), holdNoteBase = argArray.at(1), holdNoteString = argArray.at(2),
				holdNoteTexts = argArray.at(3), module = argArray.at(4), holdStartIndex, holdVal, holdVal2;
				//  set holdStartIndex
				if (item.at(4).notNil, {
					holdStartIndex = module.getSynthArgSpec(item.at(4));
				},{
					holdStartIndex = 0;
				});
				// get values for note shift steps & restrict to display range
				holdVal = module.getSynthArgSpec(item.at(2))
				.copyRange(holdStartIndex,holdStartIndex + item.at(3).value -1);
				// set values for note shift steps & restrict to display range
				holdNoteShiftRow.value_(holdVal);
				// get value for note base
				holdVal2 = module.getSynthArgSpec(item.at(1));
				// set values for note base & string
				holdNoteBase.value_(holdVal2);
				holdNoteString.string = TXGetMidiNoteString(holdVal2);
				// create text string for notes
				holdNoteTexts.strings = holdVal.collect({ arg item, i;
					TXGetMidiNoteString(holdVal2 + item);
				});
			}
		);
	}

	// MidiNoteText
	// this is used to display midi notes as text
	// arguments- index1/2 is synth arg names to be used for note base and array of note shifts,
	// index3 in no. items in row function
	// index4 is optional synth arg name where index of first item to be displayed is stored
	// index5 is an optional function valued to give label text
	// index6/7 are optional synth arg names for start/end steps of play range (notes in range are highlighted)
	// index8 is optional ScrollView init action,
	// e.g. ["MidiNoteRow", "seqNoteBase", "arrNotes", 16]
	*guiMidiNoteText { arg item, w;
		var holdColour, holdFirstStep, holdLastStep, holdScrollWidth;
		if (item.at(5).notNil, {holdColour = TXColor.paleYellow2}, {holdColour = TXColor.white});
		// start on new line
		this.nextline(w);
		//  set classData.holdStartIndex
		if (item.at(4).notNil, {
			classData.holdStartIndex = argModule.getSynthArgSpec(item.at(4));
		},{
			classData.holdStartIndex = 0;
		});
		if (item.at(8).notNil, {
			holdScrollWidth = 580;
		});
		// create row for note shift steps
		classData.holdNoteTexts = TXMultiTextBox(w, classData.viewWidth @ 20, item.at(5).value ? "Note text",
			Array.fill(item.at(3).value," "), scrollViewWidth: holdScrollWidth);
		classData.holdNoteTexts.labelView.stringColor_(TXColor.sysGuiCol1).background_(holdColour);
		classData.holdNoteTexts.arrTextViews.do({ arg item, i;
			item.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		});
		if (item.at(8).notNil, {
			item.at(8).value(classData.holdNoteTexts.scrollView);
		});
		// get values for note shift steps & restrict to display range
		classData.holdVal = argModule.getSynthArgSpec(item.at(2))
		.copyRange(classData.holdStartIndex,classData.holdStartIndex + item.at(3).value -1);
		// get value for note base
		classData.holdVal2 = argModule.getSynthArgSpec(item.at(1));
		// create text string for note base
		classData.holdNoteTexts.strings = classData.holdVal.collect({ arg item, i;
			TXGetMidiNoteString(classData.holdVal2 + item);
		});
		// highlight text that falls within play range
		if (item.at(6).notNil and: item.at(7).notNil, {
			holdFirstStep = argModule.getSynthArgSpec(item.at(6)).min(argModule.getSynthArgSpec(item.at(7)));
			holdLastStep = argModule.getSynthArgSpec(item.at(6)).max(argModule.getSynthArgSpec(item.at(7)));
			classData.holdNoteTexts.arrTextViews.do({ arg item, i;
				if ( ((i+1) >= (holdFirstStep - classData.holdStartIndex) ) and: ((i+1) <= (holdLastStep - classData.holdStartIndex) ), {
					item.background_(TXColor.paleYellow2);
					item.refresh;
				},{
					item.background_(TXColor.grey6);
					item.refresh;
				});
			});
		});

		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdNoteTexts, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1), holdStartIndex, holdVal, holdVal2;
				//  set holdStartIndex
				if (item.at(4).notNil, {
					holdStartIndex = argMod.getSynthArgSpec(item.at(4));
				},{
					holdStartIndex = 0;
				});
				// get values for note shift steps & restrict to display range
				holdVal = argMod.getSynthArgSpec(item.at(2)) .copyRange(holdStartIndex,holdStartIndex + item.at(3).value -1);
				// get value for note base
				holdVal2 = argMod.getSynthArgSpec(item.at(1));
				// create text string for notes
				argView.strings = holdVal.collect({ arg item, i;
					TXGetMidiNoteString(holdVal2 + item);
				});
				classData.holdNoteTexts.labelView.string = item.at(5).value ? "Note text";
				// highlight text that falls within play range
				if (item.at(6).notNil and: item.at(7).notNil, {
					holdFirstStep = argMod.getSynthArgSpec(item.at(6)).min(argMod.getSynthArgSpec(item.at(7)));
					holdLastStep = argMod.getSynthArgSpec(item.at(6)).max(argMod.getSynthArgSpec(item.at(7)));
					classData.holdNoteTexts.arrTextViews.do({ arg item, i;
						if ( ((i+1) >= (holdFirstStep - holdStartIndex) ) and: ((i+1) <= (holdLastStep - holdStartIndex) ), {
							item.background_(TXColor.paleYellow2);
							item.refresh;
						},{
							item.background_(TXColor.grey6);
							item.refresh;
						});
					});
				});
			}
		);
	}

	// TXMidiNoteKeybGrid
	// arguments- index1/2 is synth arg names to be updated for note base and array of note shifts,
	// index3 in no. items in row function
	// index4 is optional synth arg name where index of first item to be displayed is stored
	// index5 is an optional ACTION function to be valued in views action
	// index6 is a function to get the display octave, index7 is a function to set the display octave
	// index8 is a function to get the number of octave on the keyboard
	// index9 is an optional array of parameter names followed by index 10/11 which are functions to
	//    get and set the parameter display index
	// index12 is optional ScrollView init action
	// index13 is optional ScrollView update action
	// e.g. ["TXMidiNoteKeybGrid", "seqNoteBase", "arrNotes", 16....]

	*guiTXMidiNoteKeybGrid { arg item, w;
		var holdMidiNoteKeybGrid, holdScrollWidth, holdNoteBase, holdNoteString, holdNoteTexts;
		// start on new line
		this.nextline(w);

		//  set classData.holdStartIndex
		if (item.at(4).notNil, {
			classData.holdStartIndex = argModule.getSynthArgSpec(item.at(4));
		},{
			classData.holdStartIndex = 0;
		});
		if (item.at(12).notNil, {
			holdScrollWidth = 580;
		});

		// create keyboard grid for note steps
		holdMidiNoteKeybGrid = TXMidiNoteKeybGrid(w, classData.viewWidth+18 @ (108 * item.at(8)),
			{|view|
				var holdArr, holdValue;
				// get initial value
				holdArr = argModule.getSynthArgSpec(item.at(2));
				holdValue = view.value - argModule.getSynthArgSpec(item.at(1));
				holdValue.do({ arg val, ind;
					holdArr.put(classData.holdStartIndex + ind, val);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), holdArr);
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
			},
			// add note base value to initial value & restrict to display range range
			argModule.getSynthArgSpec(item.at(1)) +
			argModule.getSynthArgSpec(item.at(2))
			.copyRange(classData.holdStartIndex,classData.holdStartIndex + item.at(3).value -1),
			system: system,
			getOctaveFunction: item.at(6),
			setOctaveFunction: item.at(7),
			numKeybOctaves: item.at(8),
			arrParmNames: item.at(9),
			getParmIndexFunction: item.at(10),
			setParmIndexFunction: item.at(11),
			scrollViewAction: item.at(13),
			scrollViewWidth: holdScrollWidth
		);
		if (item.at(12).notNil, {
			item.at(12).value([holdMidiNoteKeybGrid.scrollView, holdMidiNoteKeybGrid.scrollView2]);
		});
		argModule.arrControls = argModule.arrControls.add(holdMidiNoteKeybGrid);

		// add screen update function
		system.addScreenUpdFunc(
			[holdMidiNoteKeybGrid, holdNoteBase, holdNoteString, holdNoteTexts, argModule],
			{ arg argArray;
				var holdMidiNoteKeybGrid = argArray.at(0),
				holdNoteBase = argArray.at(1),
				holdNoteString = argArray.at(2),
				holdNoteTexts = argArray.at(3),
				module = argArray.at(4),
				holdStartIndex, holdVal, holdVal2;
				//  set holdStartIndex
				if (item.at(4).notNil, {
					holdStartIndex = module.getSynthArgSpec(item.at(4));
				},{
					holdStartIndex = 0;
				});
				// get values for note shift steps & restrict to display range
				holdVal = module.getSynthArgSpec(item.at(2))
				.copyRange(holdStartIndex,holdStartIndex + item.at(3).value -1);
				// get value for note base
				holdVal2 = module.getSynthArgSpec(item.at(1));
				// set values for holdMidiNoteKeybGrid
				holdMidiNoteKeybGrid.value_(holdVal + holdVal2);
			}
		);
	}

	// SeqPlayRange
	// arguments- index1/index2 are synth arg names to be updated for start step & end step,
	// index3 is optional synth arg name to be updated for autoloop - if nil, loop checkbox not shown
	// index4 is max no. of steps,
	// index5 is optional text for label
	// index6 is an optional ACTION function to be valued in views action
	// index7 is an optional false which sets the view's rangeview .enabled to false
	// index8 is an optional preset array in the form: [["Presets:", []], ["1-16", [1,16]], ["17-32", [17,32]], ]
	// index9 is an optional false which sets numberbox scrolling to false
	// index10 is an optional width
	// index11 is an optional ACTION function to be valued in loop checkbox action
	// e.g. ["SeqPlayRange", "seqStartStep", "seqEndStep", "seqAutoLoop", 16]
	*guiSeqPlayRange{arg item, w;
		var loopAllowance = 0;
		if (item.at(3).notNil, {
			loopAllowance = 30;
		});
		// add rangeslider
		classData.holdSeqRangeView = TXRangeSlider(w, ((item.at(10) ? classData.viewWidth) - loopAllowance) @ 20, item.at(5) ? "Play range",
			ControlSpec(1, item.at(4), step: 1),
			{|view|
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(1), min(view.hi, view.lo));
				argModule.setSynthArgSpec(item.at(2), max(view.hi, view.lo));
				// if action function passed then value it
				if (item.at(6).notNil, {
					// run action function passing it view as arg
					item.at(6).value(view);
				});
			},
			// get initial values
			argModule.getSynthArgSpec(item.at(1)),
			argModule.getSynthArgSpec(item.at(2)),
			false, 80, 120,
			item.at(8), // presets
			item.at(9) ? true
		);
		classData.holdSeqRangeView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// disable rangeView if requested
		if (item.at(7) == false, {classData.holdSeqRangeView.rangeView.enabled_(false);});
		argModule.arrControls = argModule.arrControls.add(classData.holdSeqRangeView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdSeqRangeView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.valueBothNoAction_([argMod.getSynthArgSpec(item.at(1)),
					argMod.getSynthArgSpec(item.at(2))]);
			}
		);

		// if arg index 3 not nil, add loop checkbox
		if (item.at(3).notNil, {
			classData.holdView2 = TXCheckBox(w, 54 @ 20, "Loop", TXColor.sysGuiCol1,
				TXColor.grey(0.9), TXColor.white, TXColor.sysGuiCol1);
			classData.holdView2.action = {|view|
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// if action function passed then value it
				if (item.at(11).notNil, {
					// run action function passing it view as arg
					item.at(11).value(view);
				});
			};
			argModule.arrControls = argModule.arrControls.add(classData.holdView2);
			// get initial value
			classData.holdView2.value = argModule.getSynthArgSpec(item.at(3));
			// add screen update function
			system.addScreenUpdFunc(
				[classData.holdView2, argModule],
				{ arg argArray;
					var argView = argArray.at(0), argMod = argArray.at(1);
					argView.value_(argMod.getSynthArgSpec(item.at(3)));
				}
			);
		});
	}

	// SeqStepNoTxt
	// arguments- index1 is no. items in row function,
	// 	index2 is an optional text label
	// e.g. ["SeqStepNoTxt", 16, "Step"]
	*guiSeqStepNoTxt { arg item, w;
		this.nextline(w);
		classData.holdView = TXMultiTextBox(w, classData.viewWidth @ 20, item.at(0) ? "Step", Array.series(item.at(1).value,1) );
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
	}

	// SeqScrollStep
	// arguments- index1 is total no. of steps,
	// index2 is ScrollView init action, index3 is ScrollView update action,
	// index4/5 are optional synth arg names for start/end steps of play range (notes in range are highlighted)
	// e.g. ["SeqScrollStep", 64, {arg view; this.addScrollViewH(view);},
	//		{arg view; this.updateScrollOrigin(view.visibleOrigin)}]
	*guiSeqScrollStep { arg item, w;
		var holdFirstStep, holdLastStep;
		this.nextline(w);
		classData.holdView = TXMultiTextBox(w, classData.viewWidth @ 20, "Step", Array.series(item.at(1), 1),
			scrollViewWidth: 580, scrollViewAction: item.at(3));
		item.at(2).value(classData.holdView.scrollView);
		// highlight text that falls within play range
		if (item.at(4).notNil and: item.at(5).notNil, {
			holdFirstStep = argModule.getSynthArgSpec(item.at(4)).min(argModule.getSynthArgSpec(item.at(5)));
			holdLastStep = argModule.getSynthArgSpec(item.at(4)).max(argModule.getSynthArgSpec(item.at(5)));
			classData.holdView.arrTextViews.do({ arg item, i;
				if ( ((i+1) >= (holdFirstStep) ) and: ((i+1) <= (holdLastStep) ), {
					item.background_(TXColor.paleYellow2);
					item.refresh;
				},{
					item.background_(TXColor.grey6);
					item.refresh;
				});
			});
		});
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1), holdFirstStep, holdLastStep;
				// highlight text that falls within play range
				if (item.at(4).notNil and: item.at(5).notNil, {
					holdFirstStep = argMod.getSynthArgSpec(item.at(4)).min(argMod.getSynthArgSpec(item.at(5)));
					holdLastStep = argMod.getSynthArgSpec(item.at(4)).max(argMod.getSynthArgSpec(item.at(5)));
					argView.arrTextViews.do({ arg item, i;
						if ( ((i+1) >= (holdFirstStep) ) and: ((i+1) <= (holdLastStep) ), {
							item.background_(TXColor.paleYellow2);
							item.refresh;
						},{
							item.background_(TXColor.grey6);
							item.refresh;
						});
					});
				});
			}
		);
	}

	// SeqSelectFirstDisplayStep
	// arguments- index1 is no. items to display in row,
	// index2 synth arg name to be updated with index of first item in row,
	// index3 is total no. of steps,
	// e.g. ["SeqSelectFirstDisplayStep", 16, "displayFirstStep", 64]
	*guiSeqSelectFirstDisplayStep { arg item, w;
		this.nextline(w);
		// get initial value
		classData.holdVal = argModule.getSynthArgSpec(item.at(2)) + 1;  // add 1 for display so step no.s start from 1
		classData.holdView = TXMultiTextBox(w, classData.viewWidth @ 20, "Step", Array.series(item.at(1), classData.holdVal););
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// create buttons to move start step up/down 16
		Button(w, 18 @ 20)
		.states_([["<<", TXColor.white, TXColor.sysGuiCol1]])
		.action_({|view|
			var holdVal, outval;
			holdVal = argModule.getSynthArgSpec(item.at(2));
			outval = (holdVal - item.at(1)) .max(0);
			// store current data to synthArgSpecs
			argModule.setSynthArgSpec(item.at(2), outval);
			// recreate view
			system.showView;
		});
		Button(w, 18 @ 20)
		.states_([["<", TXColor.white, TXColor.sysGuiCol1]])
		.action_({|view|
			var holdVal, outval;
			holdVal = argModule.getSynthArgSpec(item.at(2));
			outval = (holdVal - 1) .max(0);
			// store current data to synthArgSpecs
			argModule.setSynthArgSpec(item.at(2), outval);
			// recreate view
			system.showView;
		});
		Button(w, 18 @ 20)
		.states_([[">", TXColor.white, TXColor.sysGuiCol1]])
		.action_({|view|
			var holdVal, outval;
			holdVal = argModule.getSynthArgSpec(item.at(2));
			outval = (holdVal + 1) .min(item.at(3)-item.at(1)) .max(0);
			// store current data to synthArgSpecs
			argModule.setSynthArgSpec(item.at(2), outval);
			// recreate view
			system.showView;
		});
		Button(w, 18 @ 20)
		.states_([[">>", TXColor.white, TXColor.sysGuiCol1]])
		.action_({|view|
			var holdVal, outval;
			holdVal = argModule.getSynthArgSpec(item.at(2));
			outval = (holdVal + item.at(1)) .min(item.at(3)-item.at(1)) .max(0);
			// store current data to synthArgSpecs
			argModule.setSynthArgSpec(item.at(2), outval);
			// recreate view
			system.showView;
		});
	}

	// SeqSelectChainStep
	// arguments- index1 is max no. items to display in row,
	// index2 is synth arg name for index of first item in row,
	// index3 is is synth arg name for current chain step no.
	// index4/5 are synth arg names for step no.s of chain start and chain end,
	// index6 is is synth arg name for chain slot array
	// index7 is an optional ACTION function to be valued in views action
	// e.g. ["SeqSelectChainStep", 16, "displayFirstChainStep", "chainCurrentStep", "displayFirstChainStep",
	//			"chainCurrentStep", "chainStartStep", "chainEndStep", "arrChainSlots"
	*guiSeqSelectChainStep { arg item, w;
		this.nextline(w);
		// get initial value
		classData.holdView = TXChainStepGui(w, classData.viewWidth @ 20, nil, nil,
			item.at(1),
			{|view|
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				argModule.setSynthArgSpec(item.at(6), view.arrChainSlots);
				argModule.setSynthArgSpec(item.at(2), view.firstItemInd);
				// if action function passed then value it
				if (item.at(7).notNil, {
					// run action function passing it view as arg
					item.at(7).value(view);
				});
			},
			argModule.getSynthArgSpec(item.at(3)),
			argModule.getSynthArgSpec(item.at(6)),
			argModule.getSynthArgSpec(item.at(4)),
			argModule.getSynthArgSpec(item.at(5)),
			argModule.getSynthArgSpec(item.at(2)),
			false,
			80,
			true,
			TXColor.paleYellow2
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow2);
	}

	// SeqSelect3GroupModules
	// arguments- index1/index2/index3 are variable names to hold 3 note output modules,
	// arguments- index4/index5/index6 are synth arg names to be updated with 3  module indexe no.s,
	// arguments - index7 is optional string name - default is "modules"
	// e.g. ["SeqSelect3GroupModules", noteOutModule1, noteOutModule2, noteOutModule3],
	*guiSeqSelect3GroupModules { arg item, w;
		var arrValidModules;

		classData.labelView = StaticText(w, 80 @ 20).stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.labelView.string = item.at(7) ? "Modules";
		classData.labelView.align = \right;
		// to prevent infinite loops, current module is not a valid output module
		arrValidModules = classData.arrGroupModules.select({arg item; item != argModule;});

		// create 3 popups
		3.do({ arg number, i;
			var holdModule;
			// allow for less than 3 popups if variable name isNil
			if (item.at(i+1).notNil, {
				classData.holdView = PopUpMenu(w, 150 @ 20).stringColor_(TXColor.white).background_(TXColor.sysGuiCol1);
				classData.holdView.items = ["..."]
				++ arrValidModules.collect({arg item; item.instDisplayName;});
				classData.holdView.action = { |view|
					// check for 0 value meaning no module selected
					if (view.value > 0, {
						// set outputmodule variable
						argModule.perform((item.at(i+1) ++ "_").asSymbol, arrValidModules.at(view.value - 1));
						// store current data to synthArgSpecs
						argModule.setSynthArgSpec(item.at(i+4), arrValidModules.at(view.value - 1).moduleID);
					}, {
						// set outputmodule variable
						argModule.perform((item.at(i+1) ++ "_").asSymbol, nil);
						// store current data to synthArgSpecs
						argModule.setSynthArgSpec(item.at(i+4), nil);
					});
					w.refresh;
				};
				classData.holdVal = argModule.getSynthArgSpec(item.at(i+4));
				if (classData.holdVal.isNil, {
					// set starting value
					classData.holdView.value = 0;
				}, {
					holdModule = system.getModuleFromID(classData.holdVal);
					if (holdModule != 0, {
						// get instance name of note out module
						classData.holdVal2 = holdModule.instName;
						// set starting value
						classData.holdView.value = 1 +
						(arrValidModules.collect({arg item, i; item.instName; }).indexOf(classData.holdVal2) ? 0);
					});
				});
				argModule.arrControls = argModule.arrControls.add(classData.holdView);
			});
		});
	}

	// SeqSyncStartCheckBox
	*guiSeqSyncStartCheckBox { arg item, w;
		// N.B. no arguments - assumes arrSynthArgSpecs contains "syncStart"
		classData.holdView = TXCheckBox(w, 90 @ 20, "Sync Start", TXColor.sysGuiCol1, TXColor.grey(0.9),
			TXColor.white, TXColor.sysGuiCol1);
		classData.holdView.action = {|view|
			// store current data to synthArgSpecs
			argModule.setSynthArgSpec("syncStart", view.value);
		};
		// assumes arrSynthArgSpecs contains entry for "syncStart"
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.value =  argModule.getSynthArgSpec("syncStart");
	}

	// SeqSyncStopCheckBox
	*guiSeqSyncStopCheckBox { arg item, w;
		// N.B. no arguments - assumes arrSynthArgSpecs contains "syncStop"
		classData.holdView = TXCheckBox(w, 90 @ 20, "Sync Stop", TXColor.sysGuiCol1, TXColor.grey(0.9),
			TXColor.white, TXColor.sysGuiCol1);
		classData.holdView.action = {|view|
			// store current data to synthArgSpecs
			argModule.setSynthArgSpec("syncStop", view.value);
		};
		// assumes arrSynthArgSpecs contains entry for "syncStop"
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.value =  argModule.getSynthArgSpec("syncStop");
	}

	// TXSlotGui
	// arguments- index1 is a function to get array of data slots to be addressed by gui objects,
	//	index2/3/4/5/6/7 are action functions for get slot no, store slot no, get slot data, store slot data,
	//		get next slot no, store next slot no
	// e.g. ["TXSlotGui", arrSlots, {this.getSynthArgSpec("slotNo")}, {arg slotNo; this.setSynthArgSpec("slotNo", slotNo)},
	//		{this.getSlotData}, {arg slotData; this.setSlotData(slotData)}],
	*guiTXSlotGui { arg item, w;
		classData.holdView = TXSlotGui(w, 90 @ 20, "Pattern slots", item.at(1).value,
			[item.at(3), item.at(4), item.at(5), item.at(6), item.at(7)], item.at(2).value);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView3.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView4.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
	}

	// TXEnvGui
	// This module is for displaying and updating an envelope
	// arguments:
	// index1 is array of synth arg names for env levels to be updated,
	// index2 is array of synth arg names for env times to be updated,
	// index3 is synth arg name for total envelope time
	// index4 is no. items in row function
	// index5 is an optional ACTION function to be valued in views action
	// index6 is an optional height for the gui
	// index7 is an optional width for the envelope step time boxes
	// index8 is an optional array of curves function, index9/10 are optional gridRows/gridCols
	// e.g. ["TXEnvGui", arrLevelSynthArgs, arrTimeSynthArgs, "envTotalTime", {this.getSynthArgSpec("numStages")}]
	*guiTXEnvGui { arg item, w;
		classData.holdVal = item.at(1).collect({ arg item, i;
			argModule.getSynthArgSpec(item);
		});
		classData.holdVal2 = item.at(2).collect({ arg item, i;
			argModule.getSynthArgSpec(item);
		});
		classData.holdView = TXEnvGui(w, classData.viewWidth @ (item.at(6) ? 100), ControlSpec(0, 100),
			TXColor.sysGuiCol1, TXColor.white,
			// view action
			{|view|
				var holdArr, holdArr2;
				// get initial values
				holdArr = item.at(1).collect({ arg item, i;
					argModule.getSynthArgSpec(item);
				});
				holdArr2 = item.at(2).collect({ arg item, i;
					argModule.getSynthArgSpec(item);
				});
				// update arrays with view values
				view.value.at(0).do({ arg val, ind;
					holdArr.put(ind, val);
				});
				view.value.at(1).do({ arg val, ind;
					holdArr2.put(ind, val);
				});
				// store current data to synthArgSpecs
				item.at(1).do({ arg item, i;
					argModule.setSynthValue(item, holdArr.at(i));
				});
				item.at(2).do({ arg item, i;
					argModule.setSynthValue(item, holdArr2.at(i));
				});
				// store total to synthArgSpecs
				argModule.setSynthValue(item.at(3), holdArr2.keep(item.at(4).value).sum);
				// if action function passed then value it
				if (item.at(5).notNil, {
					// run action function passing it view as arg
					item.at(5).value(view);
				});
			},
			// starting value array
			[classData.holdVal.copyRange(0, item.at(4).value -1), classData.holdVal2.copyRange(0, item.at(4).value -1)],
			stepWidth: item.at(7),
			arrCurves: item.at(8).value,
			gridRows: item.at(9).value,
			gridCols: item.at(10).value,
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
	}

	// TXEnvDisplay
	// This module is for displaying an envelope
	// arguments:
	// index1 is function to give  initial value array of view
	// index2 is an inital ACTION function to be valued with view as argument
	// index3 is an optional curves array function
	// e.g. ["TXEnvDisplay", arrLevelSynthArgs, arrTimeSynthArgs, "envTotalTime", {this.getSynthArgSpec("numStages")}]
	*guiTXEnvDisplay { arg item, w;
		classData.holdView = EnvelopeView(w, classData.viewWidth @ 80)
		.thumbSize_(1)
		.drawRects_(false)
		.drawLines_(true)
		.style_(\dots)
		.fillColor_(Color.grey(0.8))
		.selectionColor_(Color.white)
		.value_(item.at(1).value);
					// label each point
					["-", "P", "A", "D", "S", "R"].do({arg item, i;
						classData.holdView.setString(i, item);
					});
		6.do({arg i;
			classData.holdView.setEditable(i, false);
		});
		classData.holdView.fillColor_(Color.grey(0.8));
		// set val
		classData.holdView.curves_(item.at(3).value ? 'lin');
		classData.holdView.value_(item.at(1).value);
		// run initial action
		item.at(2).value(classData.holdView);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
	}

	// TXEnvPlot
	// This module is for plotting an envelope
	// arguments:
	// index1 is function to give initial value array of view: [arrLevels, arrTimes, arrCurves]
	// index2 is an inital ACTION function to be valued with view as argument
	// e.g. ["TXEnvPlot", {this.envViewValues;}, {arg view; envView = view;}]
	*guiTXEnvPlot { arg item, w;
		classData.holdView = TXEnvPlot(w, classData.viewWidth @ 80);
		// set val
		classData.holdView.set_(item.at(1).value);
		// run initial action
		item.at(2).value(classData.holdView);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
	}

	// TXSoundFileView
	// arguments- index1 is filename path function,
	// 	index2 - NO LONGER USED is an optional function to get sampleData - NO LONGER USED
	// 	index3 - NO LONGER USED is an optional function to set sampleData - NO LONGER USED
	// 	index4 is an optional ACTION function to be valued in views action
	// 	index5 is an optional function (val 0 or 1) for whether to display the file or not (to save CPU if not needed)
	// 	index6 is an optional height
	// e.g. ["TXSoundFileView", {sampleFileName},]
	*guiTXSoundFileView { arg item, w;
		classData.holdSFView = TXSoundFile(w, 450 @ (item.at(6) ?? 150),
			{|view|
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting values
			0,
			1,
			false,
			item.at(1).value,
			item.at(5).value,
			item.at(2),
			item.at(3)
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdSFView);
		// next line
		this.nextline(w);
	}	// end of TXSoundFileView

	// TXSoundFileViewRange
	// arguments- index1 is filename path function, index2/3 are synth arg names to be updated,
	// 	index4 is an optional ACTION function to be valued in views action
	// 	index5 is an optional function (val 0 or 1) for whether to display the file or not (to save CPU if not needed)
	// 	index6 is an optional height
	// 	index7 - NO LONGER USED: is an optional function to get sampleData - NO LONGER USED
	// 	index8 - NO LONGER USED: is an optional function to set sampleData - NO LONGER USED
	// 	index9 is an optional width
	// e.g. ["TXSoundFileViewRange", {sampleFileName}, "start", "end"]
	*guiTXSoundFileViewRange { arg item, w;
		classData.holdSFView = TXSoundFile(w, (item.at(9) ?? 450) @ (item.at(6) ?? 150),
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(2), view.lo);
					argModule.moduleNode.set(item.at(3), view.hi);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), view.lo);
				argModule.setSynthArgSpec(item.at(3), view.hi);
				// store values to TXRangeSlider
				classData.holdRangeView.valueBoth = [view.lo, view.hi];
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting values
			argModule.getSynthArgSpec(item.at(2)),
			argModule.getSynthArgSpec(item.at(3)),
			false,
			item.at(1).value,
			item.at(5).value,
			item.at(7),
			item.at(8)
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdSFView);
		// next line
		this.nextline(w);
		// TXRangeSlider
		classData.holdRangeView = TXRangeSlider(w, classData.viewWidth @ 20, "Play range", nil,
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(2), view.lo);
					argModule.moduleNode.set(item.at(3), view.hi);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), view.lo);
				argModule.setSynthArgSpec(item.at(3), view.hi);
				// store values to TXSoundFile
				classData.holdSFView.valueBoth = [view.lo, view.hi];
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting values
			argModule.getSynthArgSpec(item.at(2)),
			argModule.getSynthArgSpec(item.at(3))
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdRangeView);
		classData.holdRangeView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
	}	// end of TXSoundFileViewRange

	// TXSoundFileViewFraction
	// arguments- index1 is filename path function, index2/3 are synth arg names to be updated,
	// 	index4 is an optional ACTION function to be valued in views action
	// 	index5 is an optional function (val. 0 or 1) for whether to
	//     display the file or not (to save CPU if not needed)
	// 	index6 is an optional function to get sampleData
	// 	index7 is an optional function to set sampleData
	// 	index8 is an optional width
	// e.g. ["TXSoundFileViewFraction", {loopFileName}, "start", "end"]
	*guiTXSoundFileViewFraction { arg item, w;
		classData.holdSFView = TXSoundFile(w, item.at(8) ? 450 @ 150,
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(2), view.lo);
					argModule.moduleNode.set(item.at(3), view.hi);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), view.lo);
				argModule.setSynthArgSpec(item.at(3), view.hi);
				// store values to TXRangeSlider
				classData.holdTXFraction1.value = view.lo;
				classData.holdTXFraction2.value = view.hi;
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting values
			argModule.getSynthArgSpec(item.at(2)),
			argModule.getSynthArgSpec(item.at(3)),
			false,
			item.at(1).value,
			item.at(5).value,
			item.at(6),
			item.at(7)
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdSFView);
		// next line
		this.nextline(w);
		// TXFraction1
		classData.holdTXFraction1 = TXFraction(w, classData.viewWidth @ 20, "Start", nil,
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(2), view.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), view.value);
				// store both lo & hi values to TXSoundFile
				classData.holdSFView.lo = view.value;
				classData.holdSFView.hi = argModule.getSynthArgSpec(item.at(3));
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(2))
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdTXFraction1);
		classData.holdTXFraction1.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdTXFraction1.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdTXFraction1.labelView3.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// TXFraction2
		classData.holdTXFraction2 = TXFraction(w, classData.viewWidth @ 20, "End", nil,
			{|view|
				// set current value on node
				if (argModule.moduleNode.notNil, {
					argModule.moduleNode.set(item.at(3), view.value);
				});
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(3), view.value);
				// store both lo & hi values to TXSoundFile
				classData.holdSFView.lo = argModule.getSynthArgSpec(item.at(2));
				classData.holdSFView.hi = view.value;
				// if action function passed then value it
				if (item.at(4).notNil, {
					// run action function passing it view as arg
					item.at(4).value(view);
				});
			},
			// get starting value
			argModule.getSynthArgSpec(item.at(3))
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdTXFraction2);
		classData.holdTXFraction2.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdTXFraction2.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdTXFraction2.labelView3.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
	}	// end of TXSoundFileViewFraction

	// TestNoteVals
	// N.B. no arguments - assumes synth has variables testMIDINote, testMIDIVel, testMIDITime
	*guiTestNoteVals { arg item, w;
		classData.holdView = StaticText(w, 150 @ 20);
		classData.holdView.string = "Test Note Settings:";
		classData.holdView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.setProperty(\align,\center);
		// start on new line
		this.nextline(w);
		classData.holdNoteNo = TXNumber(w, classData.viewWidth @ 20, "Test Note", ControlSpec(0, 127, step: 1),
			{|view|
				// store current data
				argModule.testMIDINote = view.value;
				// update string text
				classData.holdNoteString.string = TXGetMidiNoteString(view.value);
			},
			// get initial value
			argModule.testMIDINote,
			false, 80, 44
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdNoteNo);
		classData.holdNoteNo.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// create text string for note
		classData.holdNoteString = StaticText(w, 44 @ 20)
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdNoteString.string = TXGetMidiNoteString(classData.holdNoteNo.value);
		// create buttons to move note up/down 1/12
		Button(w, 32 @ 20)
		.states_([["-1", TXColor.white, TXColor.sysGuiCol1]])
		.action_({|view|
			classData.holdNoteNo.value = (classData.holdNoteNo.value - 1).max(0).min(127);
		});
		Button(w, 32 @ 20)
		.states_([["+1", TXColor.white, TXColor.sysGuiCol1]])
		.action_({|view|
			classData.holdNoteNo.value = (classData.holdNoteNo.value + 1).max(0).min(127);
		});
		Button(w, 32 @ 20)
		.states_([["-12", TXColor.white, TXColor.sysGuiCol1]])
		.action_({|view|
			classData.holdNoteNo.value = (classData.holdNoteNo.value - 12).max(0).min(127);
		});
		Button(w, 32 @ 20)
		.states_([["+12", TXColor.white, TXColor.sysGuiCol1]])
		.action_({|view|
			classData.holdNoteNo.value = (classData.holdNoteNo.value + 12).max(0).min(127);
		});
		// start on new line
		this.nextline(w);
		classData.holdView = TXSlider(w, classData.viewWidth @ 20, "Velocity",  ControlSpec(0, 127, step: 1),
			{|view|
				// store current data
				argModule.testMIDIVel = view.value;
			},
			// get starting value
			argModule.testMIDIVel,
			false, 80, 60
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		// start on new line
		this.nextline(w);
		classData.holdView = TXSlider(w, classData.viewWidth @ 20, "Time",  ControlSpec(0.1, 20, 'db'),
			{|view|
				// store current data
				argModule.testMIDITime = view.value;
			},
			// get starting value
			argModule.testMIDITime,
			false, 80, 60
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
	}	// end of TestNoteVals

	// TestLoopVals
	// N.B. no arguments - assumes synth has variables testMIDINote, testMIDIVel, testMIDITime
	*guiTestLoopVals { arg item, w;
		classData.holdView = StaticText(w, 150 @ 20);
		classData.holdView.string = "Test Loop Settings:";
		classData.holdView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.setProperty(\align,\center);
		// start on new line
		this.nextline(w);
		classData.holdView = TXSlider(w, classData.viewWidth @ 20, "Velocity",  ControlSpec(0, 127, step: 1),
			{|view|
				// store current data
				argModule.testMIDIVel = view.value;
			},
			// get starting value
			argModule.testMIDIVel,
			false, 80, 60
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		// start on new line
		this.nextline(w);
		classData.holdView = TXSlider(w, classData.viewWidth @ 20, "Time",  ControlSpec(0.1, 20, 'db'),
			{|view|
				// store current data
				argModule.testMIDITime = view.value;
			},
			// get starting value
			argModule.testMIDITime,
			false, 80, 60
		);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
	}	// end of TestLoopVals

	// TXActionSteps
	// arguments- index1 is array of action steps value function,
	//	index2 is action function
	// 	index3 is a first display index function
	// 	index4 is a bpm value function
	// 	index5 is a beats per bar value function
	// 	index6 is next step id function
	// 	index7 is optional ScrollView init action
	// 	index8 is optional ScrollView update action
	// 	index9 is optional getCurrentStepID action
	// 	index10 is optional setCurrentStepID action
	// e.g. ["TXActionSteps2", {this.getSynthArgSpec("arrActionSteps");},
	//		{arg argArrActionSteps;  this.setSynthArgSpec("arrActionSteps", argArrActionSteps);},
	//		{this.getSynthArgSpec("displayFirstStep");},
	//		{this.getSynthArgSpec("bpm");},
	//		{this.getSynthArgSpec("beatsPerBar");},
	//		{this.getNextStepID;},
	//		{arg view; holdScrollView = view;},
	//		{arg view; holdVisibleOrigin = view.visibleOrigin; }
	//	],
	*guiTXActionSteps { arg item, w;
		var extraWidth = 0;
		if (item.at(8).notNil, {extraWidth = 40;});
		classData.holdView = TXActionSteps(system, w, (classData.viewWidth + extraWidth) @ 425, item.at(1).value, item.at(2),
			item.at(3).value, item.at(4).value, item.at(5).value, item.at(6), item.at(8), item.at(9), item.at(10));
		if (item.at(7).notNil, {
			item.at(7).value(classData.holdView.scrollView);
		});
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				// this only updates step times which can change when bpm changes
				argView.updateTimesFromNew(item.at(1).value);
			}
		);
	}	// end of TXActionSteps

	// TXOSCTrigActions
	// arguments- index1 is array of OSCTrig actions value function,
	//	index2 is action function
	// 	index3 is next step id function
	// 	index4 is optional ScrollView init action
	// 	index5 is optional ScrollView update action
	// 	index6 is optional get Current StepID Action,
	// 	index7 is optional set Current StepID Action
	// 	index8 is optional add Responder Action,
	// 	index9 is optional remove Responder Action
	// e.g. ["TXOSCTrigActions", {this.getSynthArgSpec("arrOSCTrigActions");},
	//		{arg argArrOSCTrigActions;  this.setSynthArgSpec("arrOSCTrigActions", argArrOSCTrigActions);},
	//		{arg view; holdScrollView = view;},
	//		{arg view; holdVisibleOrigin = view.visibleOrigin; } ],
	*guiTXOSCTrigActions { arg item, w;
		var extraWidth = 0;
		classData.holdView = TXOSCTrigActions(system, w, (classData.viewWidth + extraWidth) @ 386, item.at(1).value, item.at(2),
			item.at(3), item.at(5), item.at(6), item.at(7), item.at(8), item.at(9));
		if (item.at(4).notNil, {
			item.at(4).value(classData.holdView.scrollView);
		});
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		//		// add screen update function
		//		system.addScreenUpdFunc(
		//			[classData.holdView, argModule],
		//			{ arg argArray;
		//				var argView = argArray.at(0), argMod = argArray.at(1);
		//				// update rows
		//						argView.value_(item.at(1).value);
		//			}
		//		);
	}	// end of TXOSCTrigActions

	// SeqNavigationButtons
	// arguments- index1 is total no. of steps function, index2 synth arg name to be updated with index of first item in row,
	// e.g. ["SeqNavigationButtons", 64, "displayFirstStep"]
	*guiSeqNavigationButtons { arg item, w;
		// get initial value
		classData.holdVal = argModule.getSynthArgSpec(item.at(2)) + 1;  // add 1 for display so step no.s start from 1
		// navigation buttons
		[ ["<<<", -10], ["<<", -5], ["<", -1], [">", 1], [">>", 5] , [">>>", 10] ].do({ arg arrData, i;
			Button(w, 28 @ 24)
			.states_([[arrData.at(0), TXColor.white, TXColor.sysGuiCol1]])
			.action_({
				var holdVal;
				// adjust start step
				holdVal = argModule.getSynthArgSpec(item.at(2));
				holdVal = (holdVal + arrData.at(1)).max(0).min(item.at(1).value - 1);
				// store current data to synthArgSpecs
				argModule.setSynthArgSpec(item.at(2), holdVal);
				// update view
				system.showView;
			});
		});
	}

	// TXActionView
	// arguments- index1 is action array function, index2 is index of action to be edited function,
	// e.g. ["TXActionView", arrActions, 6],

	*guiTXActionView { arg item, w;
		this.nextline(w);
		classData.holdView = TXActionView(w, classData.viewWidth @ 20, item.at(1).value, item.at(2).value, 80, system);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.labelView3.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
	}

	// TXGridGreyZone
	// arguments- index1 is text, index2 is get snapshot array function,
	// 	index3 is synth arg name to be updated for the active grid
	// 	index4 is no. of rows, index5 is no. of columns
	// e.g. ["TXGridGreyZone", "Video Grid", {gridGrey.deepCopy}, "arrActiveGridCells", 8, 8],
	*guiTXGridGreyZone { arg item, w;
		var label, getSnapArrFunc, getActiveGridFunc, setActiveGridFunc, holdRows, holdCols;
		this.nextline(w);
		label = item.at(1);
		getSnapArrFunc = item.at(2);
		getActiveGridFunc = {argModule.getSynthArgSpec(item.at(3))};
		setActiveGridFunc = {arg argGrid; argModule.setSynthArgSpec(item.at(3), argGrid)};
		holdRows = item.at(4);
		holdCols = item.at(5);
		classData.holdView = TXGridGrey(w, classData.viewWidth @ 20, item.at(1), "Zone", holdRows, holdCols,
			getSnapArrFunc, getActiveGridFunc, setActiveGridFunc, 80, system);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView],
			{ arg argArray;
				var argView = argArray.at(0);
				argView.setActiveGrid_(getActiveGridFunc.value);
			}
		);
	}

	// TXGridGreyTarget
	// arguments- index1 is text, index2 is snapshot array function,
	// 	index3 is synth arg name to be updated for the target grey
	// 	index4 is no. of rows, index5 is no. of columns
	// e.g. ["TXGridGreyZone", "Video Grid", {gridGrey.deepCopy}, "arrActiveGridCells", 8, 8],
	*guiTXGridGreyTarget { arg item, w;
		var label, getSnapArrFunc, getTargetFunc, setTargetFunc, holdRows, holdCols;
		this.nextline(w);
		label = item.at(1);
		getSnapArrFunc = item.at(2);
		getTargetFunc = {argModule.getSynthArgSpec(item.at(3))};
		setTargetFunc = {arg argGrid; argModule.setSynthArgSpec(item.at(3), argGrid)};
		holdRows = item.at(4);
		holdCols = item.at(5);
		classData.holdView = TXGridGrey(w, classData.viewWidth @ 20, item.at(1), "Target", holdRows, holdCols,
			getSnapArrFunc, getTargetFunc, setTargetFunc, 80, system);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView],
			{ arg argArray;
				var argView = argArray.at(0);
				argView.setTarget(getTargetFunc.value);
			}
		);
	}

	// TXGridColourZone
	// arguments- index1 is text, index2 is get snapshot array function,
	// 	index3 is synth arg name to be updated for the active grid
	// 	index4 is no. of rows, index5 is no. of columns
	// e.g. ["TXGridColourZone", "Video Grid", {gridColour.deepCopy}, "arrActiveGridCells", 8, 8],
	*guiTXGridColourZone { arg item, w;
		var label, getSnapArrFunc, getActiveGridFunc, setActiveGridFunc, holdRows, holdCols;
		this.nextline(w);
		label = item.at(1);
		getSnapArrFunc = item.at(2);
		getActiveGridFunc = {argModule.getSynthArgSpec(item.at(3))};
		setActiveGridFunc = {arg argGrid; argModule.setSynthArgSpec(item.at(3), argGrid)};
		holdRows = item.at(4);
		holdCols = item.at(5);
		classData.holdView = TXGridColour(w, classData.viewWidth @ 20, item.at(1), "Zone", holdRows, holdCols,
			getSnapArrFunc, getActiveGridFunc, setActiveGridFunc, 80, system);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView],
			{ arg argArray;
				var argView = argArray.at(0);
				argView.setActiveGrid_(getActiveGridFunc.value);
			}
		);
	}

	// TXGridColourTarget
	// arguments- index1 is text, index2 is snapshot array function,
	// 	index3 is synth arg name to be updated for the target Colour
	// 	index4 is no. of rows, index5 is no. of columns
	// e.g. ["TXGridColourZone", "Video Grid", {gridColour.deepCopy}, "arrActiveGridCells", 8, 8],
	*guiTXGridColourTarget { arg item, w;
		var label, getSnapArrFunc, getTargetFunc, setTargetFunc, holdRows, holdCols;
		this.nextline(w);
		label = item.at(1);
		getSnapArrFunc = item.at(2);
		getTargetFunc = {argModule.getSynthArgSpec(item.at(3))};
		setTargetFunc = {arg argGrid; argModule.setSynthArgSpec(item.at(3), argGrid)};
		holdRows = item.at(4);
		holdCols = item.at(5);
		classData.holdView = TXGridColour(w, classData.viewWidth @ 20, item.at(1), "Target", holdRows, holdCols,
			getSnapArrFunc, getTargetFunc, setTargetFunc, 80, system);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView],
			{ arg argArray;
				var argView = argArray.at(0);
				argView.setTarget(getTargetFunc.value);
			}
		);
	}

	// TXNumOrString
	// arguments- index1 is text,
	// 	index2 is synth arg name to be updated for the type - 0=number, 1=string
	// 	index3 is synth arg name to be updated for the number
	// 	index4 is synth arg name to be updated for the string
	// 	index5 is the optional ControlSpec function
	// e.g. ["TXNumOrString", "Argument", "argType1", "argNumVal1", "argStringVal1", ControlSpec(0, 999999, step:1)],
	*guiTXNumOrString { arg item, w;
		var label, getTypeFunc, setTypeFunc, getNumFunc, setNumFunc, getStringFunc, setStringFunc, controlSpec;
		this.nextline(w);
		label = item.at(1);
		getTypeFunc = {argModule.getSynthArgSpec(item.at(2))};
		setTypeFunc = {arg argVal; argModule.setSynthArgSpec(item.at(2), argVal)};
		getNumFunc = {argModule.getSynthArgSpec(item.at(3))};
		setNumFunc = {arg argVal; argModule.setSynthArgSpec(item.at(3), argVal)};
		getStringFunc = {argModule.getSynthArgSpec(item.at(4))};
		setStringFunc = {arg argVal; argModule.setSynthArgSpec(item.at(4), argVal)};
		controlSpec = item.at(5).value;
		classData.holdView = TXNumOrString(w, classData.viewWidth @ 20, item.at(1), controlSpec, getTypeFunc, setTypeFunc,
			getNumFunc, setNumFunc, getStringFunc, setStringFunc, 80, 60, 330);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.popupMenuView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.valueAll_([getTypeFunc.value, getNumFunc.value, getStringFunc.value]);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView],
			{ arg argArray;
				var argView = argArray.at(0);
				argView.valueAll_([getTypeFunc.value, getNumFunc.value, getStringFunc.value]);
			}
		);
	}


	// TXQCArgGui
	// arguments:
	// 	index1 is text
	//	index2 is synth arg name to be updated for the number
	//	index3 is synth arg name to be updated for the active number setting
	// 	index4 is array of all module arguments
	// 	index5 is argument index no
	// 	index6 is set argument value function
	// e.g. ["TXQCArgGui", "Particle Hue", "p003", "i_activep003", arrQCArgData, 4, setArgValFunc],
	*guiTXQCArgGui { arg item, w;
		var label, getNumFunc, setNumFunc, setActiveFunc, arrArgs, argIndex, arrValsFunc, setArgValFunc;
		this.nextline(w);
		label = item.at(1);
		getNumFunc = {
			argModule.getSynthArgSpec(item.at(2));
		};
		setNumFunc = {arg argVal;
			argModule.setSynthValue(item.at(2), argVal);
		};
		setActiveFunc = {arg argVal;
			argModule.setSynthValue(item.at(3), argVal);
			// rebuild synth and activate osc
			{argModule.rebuildSynth;}.defer(0.2);
			{argModule.oscActivate;}.defer(0.2);
		};
		arrArgs = item.at(4);
		argIndex = item.at(5);
		setArgValFunc = item.at(6);
		//  arrQCArgData = [0, "", 0, 1, 0.5, 0.5, 0.5, 1].dup(maxParameters);
		//  array of :  argDataType, argStringVal, argMin, argMax, argHue, argSaturation, argBrightness, argAlpha
		//  argDataType can be: [0.notPassed, 1.float, 2.integer, 3.string, 4.booleanNum(0/1), 5.colour(HSBA),
		//	6.Directory Name, 7.File Name],
		arrValsFunc = {
			var holdArgs, holdMin, holdMax, holdNum;
			holdArgs = arrArgs.at(argIndex);
			holdMin = holdArgs.at(2);
			holdMax = holdArgs.at(3);
			holdNum = argModule.getSynthArgSpec(item.at(2));
			holdArgs ++ (holdMin + (holdNum * (holdMax - holdMin)));
		};
		classData.holdView = TXQCArgGui(w, classData.viewWidth @ 20, label, getNumFunc, setNumFunc, setActiveFunc,
			arrArgs, argIndex, setArgValFunc, 110, 60, 330);
		classData.holdView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.label2View.stringColor_(TXColor.sysGuiCol1).background_(TXColor.grey(0.85));
		classData.holdView.popupMenuView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.presetPopup.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.valueAll_(arrValsFunc.value);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView],
			{ arg argArray;
				var argView = argArray.at(0);
				argView.valueAll_(arrValsFunc.value);
			}
		);
	}

	// TXQCArgGuiScroller
	// arguments:
	// 	index1 is array of labels
	//	index2 is array of synth arg names to be updated for the number
	//	index3 is array of synth arg names to be updated for the active number setting
	// 	index4 is array of module arguments
	// 	index5 is set argument value function
	// 	index6 is ScrollView init action
	// 	index7 is ScrollView update action
	// e.g. ["TXQCArgGuiScroller", {arrInputs}, {arrPStrings}, {arrActivePStrings}, {arrQCArgData},
	//		{arg item; this.sendArgValue(item);},
	//		{arg view; holdScrollView = view;},
	//		{arg view; holdVisibleOrigin = view.visibleOrigin; },
	//	],
	*guiTXQCArgGuiScroller {arg item, w;
		var extraWidth = 40;
		classData.holdView = TXQCArgGuiScroller(system, w, (classData.viewWidth + extraWidth) @ 425, argModule, item.at(1).value,
			item.at(2).value, item.at(3).value, item.at(4).value, item.at(5), item.at(7));
		if (item.at(6).notNil, {
			item.at(6).value(classData.holdView.scrollView);
		});
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.update(item.at(4).value, item.at(2).value, argMod);
			}
		);
	}

	// TXV_GuiScroller
	// arguments:
	// 	index1 is array of parameters
	//	index2 is dictionary of current values to write to
	// 	index3 is sendParameter action
	// 	index4 is ScrollView init action
	// 	index5 is ScrollView update action
	// 	index6 is dictParameterGroups
	// 	index7 is optional height
	// e.g. ["TXV_GuiScroller", {data["arrParameters"]},
	//		{dictCurrentParameterVals},
	//		{arg argAddress, argValue; this.sendParameterToTXV( argAddress, argValue);},
	//		{arg view; holdScrollView = view;},
	//		{arg view; holdVisibleOrigin = view.visibleOrigin; },
	//	],
	*guiTXV_GuiScroller {arg item, w;
		var extraWidth = 40;
		classData.holdView = TXV_GuiScroller(system, w, (classData.viewWidth + extraWidth) @ (item.at(7) ? 500), argModule, item.at(1),
			item.at(2), item.at(3), item.at(5), item.at(6));
		if (item.at(4).notNil, {
			item.at(4).value(classData.holdView.scrollView);
		});
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView, argModule],
			{ arg argArray;
				var argView = argArray.at(0), argMod = argArray.at(1);
				argView.update(argMod, item.at(1),
					item.at(2), item.at(3), item.at(5), item.at(6));
			}
		);
	}

	// ModMatrixRow
	// arguments:
	// 	index1 is arrMMSourceNames
	//	index2 is arrMMDestNames
	//	index3 is synth arg name to be updated for the source index
	// 	index4 is synth arg name to be updated for the dest index
	// 	index5 is synth arg name to be updated for the modulation amount
	// e.g. ["ModMatrixRow", arrMMSourceNames, arrMMDestNames, "i_Source0", "i_Dest0", "mmValue0"],
	*guiModMatrixRow { arg item, w;
		var arrMMSourceNames, arrMMDestNames, getSourceFunc, setSourceFunc, getDestFunc, setDestFunc,
		getMMValueFunc, setMMValueFunc;
		arrMMSourceNames = item.at(1);
		arrMMDestNames = item.at(2);
		getSourceFunc = {argModule.getSynthArgSpec(item.at(3))};
		setSourceFunc = {arg argVal;
			argModule.setSynthValue(item.at(3), argVal);
			// rebuild synth
			argModule.rebuildSynth;
		};
		getDestFunc = {argModule.getSynthArgSpec(item.at(4))};
		setDestFunc = {arg argVal;
			argModule.setSynthValue(item.at(4), argVal);
			// rebuild synth
			argModule.rebuildSynth;
		};
		getMMValueFunc = {argModule.getSynthArgSpec(item.at(5))};
		setMMValueFunc = {arg argVal; argModule.setSynthValue(item.at(5), argVal)};
		classData.holdView = ModMatrixRow(w, classData.viewWidth @ 20, arrMMSourceNames, arrMMDestNames, getSourceFunc, setSourceFunc,
			getDestFunc, setDestFunc, getMMValueFunc, setMMValueFunc);
		classData.holdView.popupMenuView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.popupMenuView2.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		classData.holdView.valueAll_([getSourceFunc.value, getDestFunc.value, getMMValueFunc.value]);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView],
			{ arg argArray;
				var argView = argArray.at(0);
				argView.valueAll_([getSourceFunc.value, getDestFunc.value, getMMValueFunc.value]);
			}
		);
	}

	// ModMatrixRowScale
	// arguments:
	// 	index1 is arrMMSourceNames
	//	index2 is arrMMDestNames
	//	index3 is synth arg name to be updated for the source index
	// 	index4 is synth arg name to be updated for the dest index
	// 	index5 is synth arg name to be updated for the modulation amount
	// 	index6 is arrMMScaleNames
	//	index7 is synth arg name to be updated for the scale index
	//  index8 is optional slider width
	// e.g. ["ModMatrixRow", arrMMSourceNames, arrMMDestNames, "i_Source0", "i_Dest0", "mmValue0", arrMMScaleNames, "i_Scale0", ],
	*guiModMatrixRowScale { arg item, w;
		var arrMMSourceNames, arrMMDestNames, getSourceFunc, setSourceFunc, getDestFunc, setDestFunc,
		getMMValueFunc, setMMValueFunc, arrMMScaleNames, getScaleFunc, setScaleFunc;
		arrMMSourceNames = item.at(1);
		arrMMDestNames = item.at(2);
		getSourceFunc = {argModule.getSynthArgSpec(item.at(3))};
		setSourceFunc = {arg argVal;
			argModule.setSynthValue(item.at(3), argVal);
			// rebuild synth
			argModule.rebuildSynth;
		};
		getDestFunc = {argModule.getSynthArgSpec(item.at(4))};
		setDestFunc = {arg argVal;
			argModule.setSynthValue(item.at(4), argVal);
			// rebuild synth
			argModule.rebuildSynth;
		};
		getMMValueFunc = {argModule.getSynthArgSpec(item.at(5))};
		setMMValueFunc = {arg argVal; argModule.setSynthValue(item.at(5), argVal)};
		arrMMScaleNames = item.at(6);
		getScaleFunc = {argModule.getSynthArgSpec(item.at(7))};
		setScaleFunc = {arg argVal;
			argModule.setSynthValue(item.at(7), argVal);
			// rebuild synth
			argModule.rebuildSynth;
		};

		classData.holdView = ModMatrixRowScale(w, classData.viewWidth @ 20, arrMMSourceNames, arrMMDestNames, getSourceFunc,
			setSourceFunc, getDestFunc, setDestFunc, getMMValueFunc, setMMValueFunc, arrMMScaleNames, getScaleFunc,
			setScaleFunc, item.at(8) ? 115);
		classData.holdView.popupMenuView.stringColor_(TXColor.black).background_(TXColor.white);
		classData.holdView.popupMenuView2.stringColor_(TXColor.black).background_(TXColor.white);
		classData.holdView.popupMenuView3.stringColor_(TXColor.black).background_(TXColor.white);
		classData.holdView.sliderView.background_(TXColor.sysGuiCol1);
		classData.holdView.valueAll_([getSourceFunc.value, getDestFunc.value, getMMValueFunc.value, getScaleFunc.value]);
		argModule.arrControls = argModule.arrControls.add(classData.holdView);
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView],
			{ arg argArray;
				var argView = argArray.at(0);
				argView.valueAll_([getSourceFunc.value, getDestFunc.value, getMMValueFunc.value, getScaleFunc.value]);
			}
		);
	}

	// MIDIKeyboard
	// index1 is note play function to be valued with note as argument
	// index2 is the optional number of octaves to be shown on the keyboard
	// index3 is the optional height of the keyboard
	// index4 is the optional width of the keyboard
	// index5 is the optional lowest midi note of the keyboard
	// index6 is the optional note stop function to be valued with note as argument
	// index7 is the optional label string default: "Notes: C1 - B6"
	// index8 is the optional boolean: Show velocity slider, default: true
	// index9 is the optional function for scale semitones to be highlit
	//   - e.g. for major scale: [ 0, 2, 4, 5, 7, 9, 11 ], default [0]
	// index10 is the optional function for key 0-11, 0 = key C (default), 11 == B, that is used with the scale
	// index11 is the optional function for an array of notes to be highlit
	//   - e.g. custom scale which isn't the same in each octave


	*guiMIDIKeyboard { arg item, w;
		var sliderView, holdPos, holdMarginX, scaleFunc, keyFunc, noteArrayFunc;
		holdPos = classData.win.asView.decorator.left;
		holdMarginX = classData.win.asView.decorator.gap.x;
		// Midi Keyboard
		classData.holdView = TXMIDIKeyboard.new(w, Rect(0, 0, item.at(4) ? classData.viewWidth, item.at(3) ? 60),
			item.at(2) ? 4, item.at(5) ? 48);
		classData.holdView.keyDownAction_(item.at(1));
		classData.holdView.keyUpAction_(item.at(6));
		classData.holdView.keyTrackAction_({arg newNote, oldNote;
			item.at(6).value(oldNote);
			item.at(1).value(newNote);
		});
		scaleFunc = item.at(9);
		keyFunc = item.at(10);
		noteArrayFunc = item.at(11);
		if (scaleFunc.notNil, {
			// show scale
			classData.holdView.showScale (scaleFunc.value, keyFunc.value ? 0, TXColor.yellow);
		});
		if (noteArrayFunc.notNil, {
			// show notes
			noteArrayFunc.value.do({arg note, i;
				classData.holdView.setColor(note.asInteger, TXColor.yellow);
			});
		});
		// show middle C
		classData.holdView.setColor(60, classData.holdView.getColor(60).blend(TXColor.red, 0.5));
		argModule.arrControls = argModule.arrControls.add(classData.holdView);

		// new line & shift
		this.nextline(w);
		classData.win.asView.decorator.shift(holdPos - holdMarginX, 0);

		// label
		StaticText(w, 160 @ 20)
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
		.string_(item.at(7) ? "Notes: C1 - B6")
		.align_('center');
		// if note off action not given then display Note Time slider
		if (item.at(6).isNil, {
			sliderView = TXSlider(w, 180 @ 20, "Note time",  ControlSpec(0.1, 20, 'db'),
				{|view|
					// store current data
					argModule.testMIDITime = view.value;
				},
				// get starting value
				argModule.testMIDITime,
				false, 60, 30
			);
			argModule.arrControls = argModule.arrControls.add(sliderView);
			sliderView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		});
		if (item.at(8).isNil or: (item.at(8) == true), {
			// Velocity slider
			sliderView = TXSlider(w, 180 @ 20, "Velocity",  ControlSpec(0, 127, step: 1),
				{|view|
					// store current data
					argModule.testMIDIVel = view.value;
				},
				// get starting value
				argModule.testMIDIVel,
				false, 60, 30
			);
			argModule.arrControls = argModule.arrControls.add(sliderView);
			sliderView.labelView.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		});
		// add screen update function
		system.addScreenUpdFunc(
			[classData.holdView],
			{ arg argArray;
				var argView = argArray.at(0);
				if (scaleFunc.notNil or: noteArrayFunc.notNil, {
					argView.clear;
					if (scaleFunc.notNil, {
						// show scale
						argView.showScale (scaleFunc.value, keyFunc.value ? 0, TXColor.yellow);
					});
					if (noteArrayFunc.notNil, {
						// show notes
						noteArrayFunc.value.do({arg note, i;
							argView.setColor(note.asInteger, TXColor.yellow);
						});
					});
					// show middle C
					argView.setColor(60, argView.getColor(60).blend(TXColor.red, 0.5));
				});
			}
		);
	}

}
