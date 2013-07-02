// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFrontScreenGui {	// Front Screen gui

*makeGui{ arg window, viewMode, system, classData;

	var layoutView, arrLayoutXvals, arrLayoutYvals, arrWidgetTexts, addWidgetPopupView;
	var currWidgetIDPopupView, currWidgetNameText, currGridSizePopupView;
	var currWidgetHeight, currWidgetWidth;
	var boxFromTop, newWidgetHeight, newWidgetWidth;
	var notesBox, notesView, updateButton, colourPickerButton, boxColourBox;
	var layerBar, layerPopupView, layerNameText;
	var snapshotPopupView, snapshotPopupWidth, snapshotPopupItems, snapshotNameText;
	var chkboxShowGUIProperties, limitWidgetUpdates;
	var screenWidth, screenHeight;

	screenWidth = classData.screenWidths[classData.layerNo];
	screenHeight = classData.screenHeights[classData.layerNo];

	// deactivate any midi and keydown functions
	TXFrontScreen.midiDeActivate;
	TXFrontScreen.keyDownDeActivate;

	// set variables
	this.setCurrentWidget(TXFrontScreen.currWidgetInd ? 0);

	// width & height choices
//  ================== ================== ================== ================== ==================

	if (viewMode == "Run Interface", {
		// create layer bar
		layerBar = CompositeView(window, Rect(0, 0, 1200, 30));
		layerBar.decorator = FlowLayout(layerBar.bounds);
		boxFromTop = 30;

		// only display popups if system.showSystemControls is on
		if (system.showSystemControls == 1, {
			// popup - current layer
			layerPopupView = PopUpMenu(layerBar, Rect(0, 30, 380, 20))
				.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
				.items_(
					TXFrontScreen.arrLayers.collect({arg item, i;
						 "Screen " ++ (i + 1).asString ++ " - " ++ item.at(2)
					})
				)
				.action_({arg view;
					TXFrontScreen.storeCurrLoadNewLayer(view.value);
					// update variables
					this.setCurrentWidget(0);
					system.addHistoryEvent;
					// update view
					system.showView;
				});
			layerPopupView.value = classData.layerNo;

			// add spacer
			layerBar.decorator.shift(40, 0);

			// set popup width
			if ( (system.snapshotNo == 0) or: system.snapshotIsEmpty(system.snapshotNo).not, {
				snapshotPopupWidth = 424;
			},{
				snapshotPopupWidth = 246;
			});
			// set popup items
			snapshotPopupItems = ["Snapshots ..."]
					++ (1 .. 99).collect({arg item, i;
				 	var holdName;
				 	holdName = system.getSnapshotName(item);
				 	if ( (system.snapshotNo == 0) or: system.snapshotIsEmpty(item), {
					 	if (holdName == "", {holdName = "Empty"});
					});
				 	"Snapshot " ++ item.asString ++ ": " ++ holdName;
				 });
			// popup - current snapshot
			snapshotPopupView = PopUpMenu(layerBar, Rect(0, 0, snapshotPopupWidth, 20))
				.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
				.items_(snapshotPopupItems)
				.action_({arg view;
					system.snapshotNo = view.value;
					// update view
					system.showView;
				});
			snapshotPopupView.value = system.snapshotNo;

			// if a real snapshot is selected
			if (system.snapshotNo > 0, {
				if (system.snapshotIsEmpty(system.snapshotNo), {

					// text field - current snapshot name
					snapshotNameText = TextField(layerBar, Rect(0, 0, 300, 20))
						.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
						.align_(\left);

					// button - save snapshot
					Button(layerBar, Rect(0, 0, 120, 20))
						.states_([["Save snapshot", TXColor.white, TXColor.sysGuiCol1]])
						.action_({
							var holdName;
							holdName = snapshotNameText.string;
							// save snapshot
							system.saveCurrentSnapshot(holdName);
							// update view
							system.showView;
						});
				},{
					// button - Load snapshot
					Button(layerBar, Rect(0, 0, 80, 20))
						.states_([["Load", TXColor.white, TXColor.sysGuiCol1]])
						.action_({
							// Load
							system.loadSnapshot(snapshotPopupView.value);
							// update view
							system.showView;
						});
					// button - Overwrite snapshot
					Button(layerBar, Rect(0, 0, 80, 20))
						.states_([["Overwrite", TXColor.white, TXColor.sysDeleteCol]])
						.action_({
							// Overwrite
							system.overwriteCurrentSnapshot;
							// update view
							system.showView;
						});
					// button - Delete snapshot
					Button(layerBar, Rect(0, 0, 80, 20))
						.states_([["Delete", TXColor.white, TXColor.sysDeleteCol]])
						.action_({
							// delete
							system.deleteCurrentSnapshot;
							// update view
							system.showView;
						});
				});
			});
		});
	});


//  ================== ================== ================== ================== ==================

	if (viewMode == "Design Interface", {
		boxFromTop = 30;
		// create layer bar
		layerBar = CompositeView(window, Rect(0, 0, 1400, 30));
		layerBar.decorator = FlowLayout(layerBar.bounds);

			// popup - current layer
			layerPopupView = PopUpMenu(layerBar, Rect(0, 30, 360, 20))
				.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
				.items_(
					TXFrontScreen.arrLayers.collect({arg item, i;
						 "Screen " ++ (i + 1).asString ++ " - " ++ item.at(2)
					})
				)
				.action_({arg view;
					TXFrontScreen.storeCurrLoadNewLayer(view.value);
					// update variables
					this.setCurrentWidget(0);
					system.addHistoryEvent;
//					layerNameText.string = layerName;
					// update view
					system.showView;
				});
			layerPopupView.value = classData.layerNo;

		// add spacer
		layerBar.decorator.shift(20, 0);

		// text
		StaticText(layerBar, Rect(0, 0, 50, 20))
			.stringColor_(TXColour.sysGuiCol1).background_(TXColor.paleBlue2)
			.align_(\right)
			.string_("Grid size");

		// popup - current grid size
		currGridSizePopupView = PopUpMenu(layerBar, Rect(0, 0, 46, 20))
			.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
			.items_([1,3,5,10,20,30,40,60,80,100].collect({arg item; item.asString}))
			.action_({arg view;
				classData.gridSize = [1,3,5,10,20,30,40,60,80,100].at(view.value);
				TXFrontScreen.fitAllWidgetsToGrid;
				// update view
				system.showView;
			});
		currGridSizePopupView.value = [1,3,5,10,20,30,40,60,80,100].indexOf(classData.gridSize);

		// add spacer
		layerBar.decorator.shift(20, 0);

		// button - Add new Source widget
		Button(layerBar, Rect(0, 0, 95, 20))
			.states_([["Add widget", TXColor.white, TXColor.sysGuiCol1]])
			.action_({
				// add widget
				TXFrontScreen.arrWidgets = TXFrontScreen.arrWidgets.add(classData.arrWidgetClasses.at(addWidgetPopupView.value)
					.new(nil, nil, classData.holdWidgetHeight, classData.holdWidgetWidth));
				// update variables
				this.unhighlightAllViews;
				this.setCurrentWidget(TXFrontScreen.arrWidgets.size - 1);
				// update view
				system.showView;
			});

		// popup - new widget
		arrWidgetTexts = classData.arrWidgetClasses.collect({arg item, i; item.widgetName});
		addWidgetPopupView = PopUpMenu(layerBar, Rect(0, 0, 120, 20))
			.background_(TXColor.paleYellow2).stringColor_(TXColor.sysGuiCol1)
			.items_(arrWidgetTexts)
			.action_({arg view;
				classData.currAddWidgetInd = view.value;
				addWidgetPopupView.dragLabel_(arrWidgetTexts[addWidgetPopupView.value.asInteger]);
			})
			.beginDragAction_({classData.arrWidgetClasses[addWidgetPopupView.value]
				.new(nil, nil, classData.holdWidgetHeight, classData.holdWidgetWidth);})
		;
		addWidgetPopupView.value = classData.currAddWidgetInd;
		addWidgetPopupView.dragLabel_(arrWidgetTexts[addWidgetPopupView.value.asInteger]);

		// text
		StaticText(layerBar, Rect(0, 0, 15, 20))
			.stringColor_(TXColour.sysGuiCol1).background_(TXColor.paleBlue2)
			.align_(\center)
			.string_("W");

		// numberbox - add widget width
		newWidgetWidth = NumberBox(layerBar, Rect(0, 0, 35, 20))
			.scroll_(false)
			.action_({arg view;
				classData.holdWidgetWidth = view.value;
			});
		newWidgetWidth.value = classData.holdWidgetWidth;

		// popup - width choices
		PopUpMenu(layerBar, Rect(0, 0, 65, 20))
			.items_(classData.arrSizes.collect({arg item, i; item.asString});)
			.background_(TXColor.white)
			.action_({arg view;
				if (view.value > 0, {newWidgetWidth.valueAction = classData.arrSizes.at(view.value);});
			});

		// text
		StaticText(layerBar, Rect(0, 0, 15, 20))
			.stringColor_(TXColour.sysGuiCol1).background_(TXColor.paleBlue2)
			.align_(\center)
			.string_("H");

		// numberbox - add widget height
		newWidgetHeight = NumberBox(layerBar, Rect(0, 0, 35, 20))
				.scroll_(false)
				.action_({arg view;
				classData.holdWidgetHeight = view.value;
				TXFrontScreen.fitAllWidgetsToGrid;
				// update view
				system.showView;
			});
		newWidgetHeight.value = classData.holdWidgetHeight;

		// popup - height choices
		PopUpMenu(layerBar, Rect(0, 0, 65, 20))
			.items_(classData.arrSizes.collect({arg item, i; item.asString});)
			.background_(TXColor.white)
			.action_({arg view;
				if (view.value > 0, {
					newWidgetHeight.valueAction = classData.arrSizes.at(view.value);
				});
			});

		// add spacer
		layerBar.decorator.shift(20, 0);

		// checkbox - show gui properties
		chkboxShowGUIProperties = TXCheckBox(layerBar, Rect(0, 0, 150, 20), "Show GUI Properties",
			TXColor.sysGuiCol1, TXColour.grey(0.8), TXColor.white, TXColor.sysGuiCol1)
			.action_({arg view;
				// make or close Gui Properties window
				if (view.value == 1, {
					classData.showGuiProperties = true;
					TXFrontScreenGuiProperties.makeGui(system);
				}, {
					classData.showGuiProperties = false;
					TXFrontScreenGuiProperties.closeWindow;
				});
			});
		if (classData.showGuiProperties == true, {
			chkboxShowGUIProperties.value = 1;
		}, {
			chkboxShowGUIProperties.value = 0;
		});
		// add spacer
		layerBar.decorator.shift(20, 0);

		// button - Delete widgets
		Button(layerBar, Rect(0, 0, 140, 20))
			.states_([["Delete selected widgets", TXColor.white, TXColor.sysDeleteCol]])
			.action_({
				// delete widget
				TXFrontScreen.deleteHighlitWidgets;
				// update variables
				this.setCurrentWidget(0);
				// update view
				system.showView;
			});

		// check for empty TXFrontScreen.arrWidgets
		if (TXFrontScreen.arrWidgets.size == 0, {TXFrontScreen.initArrWidgets});

	});
	// end of if viewMode == "Design Interface" ================== ================== ==================

	// show all widgets
	if (viewMode == "Run Interface" or: (viewMode == "Design Interface"), {
		// draw screen
		classData.holdScreen = CompositeView
			(window, Rect(4, boxFromTop, screenWidth, screenHeight))
			.background_(classData.screenColour);
		// if relevent, add background image
		{if (GUI.current.asSymbol == \cocoa, {
			if (classData.imageFileNames[classData.layerNo].notNil
				and: {classData.displayModeIndices[classData.layerNo] > 0}, {
					if (classData.holdImages[classData.layerNo].isNil, {
						classData.holdImages[classData.layerNo] =
							SCImage.open(TXPath.convert(classData.imageFileNames[classData.layerNo]));
					});
				classData.holdScreen.backgroundImage_(
					classData.holdImages[classData.layerNo],
					classData.displayModeIndices[classData.layerNo]
				);
			});
		});
		}.defer;
		// re: limitWidgetUpdates switch
		// if designing Interface, then certain Widgets need to be display only
		// this is to stop a bug when trying to drag widgets around the layout
		if (viewMode == "Design Interface", {
			limitWidgetUpdates = true;
		},{
			limitWidgetUpdates = false;
		});
		// build gui for all widgets
		TXFrontScreen.arrWidgets.do({ arg item, i;
			item.buildGui(classData.holdScreen, 0, 0, screenWidth,
				screenHeight, limitWidgetUpdates);
		});
	});
	// ================== ================== ==================

	if (viewMode == "Design Interface", {

		// create TXInterfaceLayoutView to overlay widgets
		layoutView = TXInterfaceLayoutView(window,
			Rect(4, boxFromTop, screenWidth, screenHeight), TXFrontScreen.arrWidgets);
		layoutView.highlightActionFunc =
			{arg widget; TXFrontScreen.currWidgetInd = TXFrontScreen.arrWidgets.indexOf(widget);};
		layoutView.mouseUpActionFunc = {system.showView};
		layoutView.gridStep = classData.gridSize;

		// help texts
		StaticText(window, Rect(4, 40 + screenHeight, 1000, 20))
			.stringColor_(TXColour.sysGuiCol1).background_(TXColor.paleYellow)
			.align_(\left)
			.string_(" Click on a single widget and grab its corners to change its size. "
				++ "Use Shift key with mouse clicks to add to group of currently selected widgets. ");
		StaticText(window, Rect(4, 65 + screenHeight, 1000, 20))
			.stringColor_(TXColour.sysGuiCol1).background_(TXColor.paleYellow)
			.align_(\left)
			.string_(" Use Command key with mouse drag to add a widget from the yellow popup menu above, "
				++ "or to clone currently selected widgets. ");

		// text label for clipboard items
		StaticText(window, Rect(10 + screenWidth, 60, 80, 20))
			.stringColor_(TXColour.sysGuiCol1).background_(TXColor.paleBlue2)
			.align_(\left)
			.string_("Clipboards");
		// number boxes
		 NumberBox(window, Rect(10 + screenWidth, 90, 80, 20))
			.action_({arg view; classData.clipboard1 = view.value;})
			.value_(classData.clipboard1);
		 NumberBox(window, Rect(10 + screenWidth, 120, 80, 20))
			.action_({arg view; classData.clipboard2 = view.value;})
			.value_(classData.clipboard2);
		 NumberBox(window, Rect(10 + screenWidth, 150, 80, 20))
			.action_({arg view; classData.clipboard3 = view.value;})
			.value_(classData.clipboard3);
//		 TextField(window, Rect(10 + screenWidth, 180, 80, 20))
//			.action_({arg view; classData.clipboard4 = view.string;})
//			.value_(classData.clipboard4);
		});
	// ================== ================== ==================


} // end of class method makeGui

*setCurrentWidget{ arg val;
	TXFrontScreen.currWidgetInd = val;
	TXFrontScreen.arrWidgets[val].highlight = true;
}

*unhighlightAllViews {
	TXFrontScreen.arrWidgets.do({ |view|
		view.highlight = false;
	})
}


}	// end of class
