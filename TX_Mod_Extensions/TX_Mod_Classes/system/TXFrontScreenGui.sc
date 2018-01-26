// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFrontScreenGui {	// Front Screen gui

	*makeGui{ arg window, viewMode, system, classData;

		var layoutView, arrLayoutXvals, arrLayoutYvals, arrWidgetTexts, addWidgetPopupView;
		var currWidgetIDPopupView, currWidgetNameText, currGridSizePopupView, arrGridSizes, gridAutoOptionPopupView;
		var snapSelectedButton, fitSelectedButton;
		var currWidgetHeight, currWidgetWidth;
		var boxFromTop, newWidgetHeight, newWidgetWidth;
		var notesBox, notesView, updateButton, colourPickerButton, boxColourBox;
		var layerBar, layerPopupView, layerNameText, syncMidiRemoteButton, allowUpdCheckbox;
		var snapshotPopupView, snapshotPopupWidth, snapshotPopupItems, snapshotNameText;
		var chkboxShowGUIProperties, limitWidgetUpdates;
		var arrLayerPopupItems, screenWidth, screenHeight, holdVertBarButtonData;
		var interfaceScrollView, interfaceBox, interfaceBoxBar, propertiesScrollView, propertiesBox, propertiesBoxBar;

		screenWidth = classData.screenWidths[classData.layerNo];
		screenHeight = classData.screenHeights[classData.layerNo];

		// deactivate any midi and keydown functions
		TXFrontScreen.midiDeActivate;
		TXFrontScreen.keyDownDeActivate;

		// // reset variables
		// this.unhighlightAllViews;

		// width & height choices
		//  ================== ================== ================== ================== ==================

		if (viewMode == "Run Interface", {
			// create layer bar
			layerBar = CompositeView(window, Rect(0, 0, 1300, 30));
			layerBar.decorator = FlowLayout(layerBar.bounds);
			boxFromTop = 30;

			// only display popups if system.showSystemControls is on
			if (system.showSystemControls == 1, {
				// popup - current layer
				arrLayerPopupItems = TXFrontScreen.arrLayers.collect({arg item, i;
					"screen " ++ (i + 1).asString ++ " - " ++ item.at(2)
				});
				layerPopupView = PopUpMenu(layerBar, Rect(0, 30, 290, 20))
				.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
				.items_(arrLayerPopupItems)
				.action_({arg view;
					TXFrontScreen.storeCurrLoadNewLayer(view.value);
					// update variables
					this.unhighlightAllViews;
					system.addHistoryEvent;
					// update view
					system.showView;
				});
				layerPopupView.value = classData.layerNo;
				// + - buttons
				Button(layerBar, Rect(0, 0, 16, 20))
				.states_([["+", TXColor.white, TXColor.sysGuiCol1]])
				.action_({arg view;
					layerPopupView.valueAction_((layerPopupView.value + 1).min(arrLayerPopupItems.size - 1));
				});
				Button(layerBar, Rect(0, 0, 16, 20))
				.states_([["-", TXColor.white, TXColor.sysGuiCol1]])
				.action_({arg view;
					layerPopupView.valueAction_((layerPopupView.value - 1).max(0));
				});

				// add spacer
				layerBar.decorator.shift(26, 0);

				syncMidiRemoteButton = 	Button(layerBar, Rect(0, 0, 156, 20))
				.states_([["Sync Midi Remote Devices", TXColor.white, TXColor.sysGuiCol1]])
				.action_({arg view;
					TXFrontScreen.syncMidiRemoteDevices;
					// update view
					system.showView;
				});
				// add spacer
				layerBar.decorator.shift(26, 0);

				/*
				====== NOT NEEDED FOR NOW:
				allowUpdCheckbox = 	TXCheckBox(layerBar, Rect(0, 0, 150, 20), "Allow live gui updates", TXColor.sysGuiCol1,
					TXColor.white, TXColor.white, TXColor.sysGuiCol1)
				.action_({arg view;
					TXFrontScreen.classData.allowFrontScreenUpdates = view.value.asBoolean;
				})
				.value_(TXFrontScreen.classData.allowFrontScreenUpdates.asInt);
				// add spacer
				layerBar.decorator.shift(26, 0);
				*/

				// // set popup width
				// if ( (system.dataBank.snapshotNo == 0) or: system.snapshotIsEmpty(system.dataBank.snapshotNo).not, {
				// 	snapshotPopupWidth = 424;
				// 	},{
				// 		snapshotPopupWidth = 246;
				// });
				// set popup items
				snapshotPopupItems = ["snapshots ..."]
				++ (1 .. 99).collect({arg item, i;
					var holdName;
					holdName = system.getSnapshotName(item);
					if ( (system.dataBank.snapshotNo == 0) or: system.snapshotIsEmpty(item), {
						if (holdName == "", {holdName = "Empty"});
					});
					"snapshot " ++ item.asString ++ ": " ++ holdName;
				});
				// popup - current snapshot
				snapshotPopupView = PopUpMenu(layerBar, Rect(0, 0, 350, 20))
				.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
				.items_(snapshotPopupItems)
				.action_({arg view;
					system.dataBank.snapshotNo = view.value;
					// update view
					system.showView;
				});
				snapshotPopupView.value = system.dataBank.snapshotNo;
				Button(layerBar, Rect(0, 0, 16, 20))
				.states_([["+", TXColor.white, TXColor.sysGuiCol1]])
				.action_({arg view;
					system.dataBank.snapshotNo = (system.dataBank.snapshotNo + 1).min(snapshotPopupItems.size - 1);
					// update view
					system.showView;
				});
				Button(layerBar, Rect(0, 0, 16, 20))
				.states_([["-", TXColor.white, TXColor.sysGuiCol1]])
				.action_({arg view;
					system.dataBank.snapshotNo = (system.dataBank.snapshotNo - 1).max(0);
					// update view
					system.showView;
				});

				// if a real snapshot is selected
				if (system.dataBank.snapshotNo > 0, {
					if (system.snapshotIsEmpty(system.dataBank.snapshotNo), {

						// text field - current snapshot name
						snapshotNameText = TextField(layerBar, Rect(0, 0, 220, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
						.align_(\left);

						// button - save snapshot
						Button(layerBar, Rect(0, 0, 90, 20))
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
						Button(layerBar, Rect(0, 0, 40, 20))
						.states_([["Load", TXColor.white, TXColor.sysGuiCol1]])
						.action_({
							// Load
							system.loadSnapshot(snapshotPopupView.value);
							// update view
							system.showView;
						});
						// button - Overwrite snapshot
						Button(layerBar, Rect(0, 0, 60, 20))
						.states_([["Overwrite", TXColor.white, TXColor.sysDeleteCol]])
						.action_({
							// Overwrite
							system.overwriteCurrentSnapshot;
							// update view
							system.showView;
						});
						// button - Delete snapshot
						Button(layerBar, Rect(0, 0, 50, 20))
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
			arrLayerPopupItems = TXFrontScreen.arrLayers.collect({arg item, i;
				"screen " ++ (i + 1).asString ++ " - " ++ item.at(2)
			});
			layerPopupView = PopUpMenu(layerBar, Rect(0, 30, 290, 20))
			.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
			.items_(arrLayerPopupItems)
			.action_({arg view;
				TXFrontScreen.storeCurrLoadNewLayer(view.value);
				// update variables
				this.unhighlightAllViews;
				system.addHistoryEvent;
				//	layerNameText.string = layerName;
				// update view
				system.showView;
			});
			layerPopupView.value = classData.layerNo;
			// + - buttons
			Button(layerBar, Rect(0, 0, 16, 20))
			.states_([["+", TXColor.white, TXColor.sysGuiCol1]])
			.action_({arg view;
				layerPopupView.valueAction_((layerPopupView.value + 1).min(arrLayerPopupItems.size - 1));
			});
			Button(layerBar, Rect(0, 0, 16, 20))
			.states_([["-", TXColor.white, TXColor.sysGuiCol1]])
			.action_({arg view;
				layerPopupView.valueAction_((layerPopupView.value - 1).max(0));
			});

			// add spacer
			layerBar.decorator.shift(20, 0);

			// text
			StaticText(layerBar, Rect(0, 0, 50, 20))
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
			.align_(\right)
			.string_("Grid size");

			// popup - current grid size
			arrGridSizes = [1,2,3,4,5,8,10,12,15,20,25,30];
			currGridSizePopupView = PopUpMenu(layerBar, Rect(0, 0, 46, 20))
			.background_(TXColor.white).stringColor_(TXColor.black)
			.items_(arrGridSizes.collect({arg item; item.asString}))
			.action_({arg view;
				classData.gridSize = arrGridSizes.at(view.value);
				// update view
				system.showView;
			});
			currGridSizePopupView.value = arrGridSizes.indexOf(classData.gridSize);

			// popup - auto snap to grid
			gridAutoOptionPopupView = PopUpMenu(layerBar, Rect(0, 0, 120, 20))
			.background_(TXColor.white).stringColor_(TXColor.black)
			.items_(["Auto off", "Auto snap to grid", "Auto fit to grid"])
			.action_({arg view;
				classData.gridAutoOption = ['Auto Off', 'Auto Snap', 'Auto Fit'][view.value];
			});
			gridAutoOptionPopupView.value = ['Auto Off', 'Auto Snap', 'Auto Fit'].indexOf(classData.gridAutoOption);

			snapSelectedButton = Button(layerBar, Rect(0, 0, 134, 20))
			.states_([ ["Snap selected widgets", TXColor.white, TXColor.sysGuiCol1] ]);
			snapSelectedButton.action = {arg view;
				TXFrontScreen.snapSelectedWidgetsToGrid;
				// update view
				system.showView;
			};

			fitSelectedButton = Button(layerBar, Rect(0, 0, 124, 20))
			.states_([ ["Fit selected widgets", TXColor.white, TXColor.sysGuiCol1] ]);
			fitSelectedButton.action = {arg view;
				TXFrontScreen.fitSelectedWidgetsToGrid;
				// update view
				system.showView;
			};

			// add spacer
			layerBar.decorator.shift(30, 0);

			// button - Copy selected widgets
			Button(layerBar, Rect(0, 0, 138, 20))
			.states_([["Copy selected widgets", TXColor.white, TXColor.sysGuiCol1]])
			.action_({
				TXFrontScreen.copySelectedWidgetsToClipboard;
			});
			// add spacer
			// layerBar.decorator.shift(6, 0);

			// button - paste widgets
			Button(layerBar, Rect(0, 0, 88, 20))
			.states_([["Paste widgets", TXColor.white, TXColor.sysGuiCol1]])
			.action_({
				// paste widgets
				TXFrontScreen.pasteWidgetsFromClipboard;
				// update variables
				this.setCurrentWidget(TXFrontScreen.arrWidgets.size - 1);
				// update view
				system.showView;
			});
			// add spacer
			// layerBar.decorator.shift(6, 0);

			// button - Delete widgets
			Button(layerBar, Rect(0, 0, 140, 20))
			.states_([["Delete selected widgets", TXColor.white, TXColor.sysDeleteCol]])
			.action_({
				// delete widget
				TXFrontScreen.deleteHighlitWidgets;
				// update variables
				this.unhighlightAllViews;
				// update view
				system.showView;
			});

			// check for empty TXFrontScreen.arrWidgets
			if (TXFrontScreen.arrWidgets.size == 0, {TXFrontScreen.initArrWidgets});

		});
		// end of if viewMode == "Design Interface" ================== ================== ==================

		// show all widgets
		if (viewMode == "Run Interface" or: (viewMode == "Design Interface"), {
			if (viewMode == "Design Interface", {
				// Note re: limitWidgetUpdates switch
				// if designing Interface, then certain Widgets need to be display only
				// this is to stop a bug when trying to drag widgets around the layout
				limitWidgetUpdates = true;
				// reset arrays
				classData.arrScrollViews = [];
				classData.arrUserViews = [];
				// add scrollview
				interfaceScrollView = ScrollView(window, Rect(4,boxFromTop, classData.designViewCol1Width, 612))
				.hasBorder_(false);
				interfaceScrollView.background = TXColor.sysModuleWindow;
				interfaceScrollView.action = {arg view; classData.interfaceVisibleOrigin = view.visibleOrigin; };
				// defer or else doesn't work
				{interfaceScrollView.visibleOrigin = classData.interfaceVisibleOrigin;}.defer(0.05);
				classData.arrScrollViews = classData.arrScrollViews.add(interfaceScrollView);
				interfaceBox = CompositeView(interfaceScrollView, Rect(0,0, screenWidth+4, screenHeight+84));
				interfaceBox.background = TXColor.sysModuleWindow;
				// draw screen
				classData.holdScreen = CompositeView
				(interfaceBox, Rect(0, 0, screenWidth, screenHeight))
				.background_(classData.screenColour);
				// if relevent, add background image
				{
					if (classData.imageFileNames[classData.layerNo].notNil, {
						if (classData.holdImages[classData.layerNo].isNil, {
							classData.holdImages[classData.layerNo] =
							Image.open(TXPath.convert(classData.imageFileNames[classData.layerNo]));
						});
						classData.holdScreen.backgroundImage_(classData.holdImages[classData.layerNo]);
					});
				}.defer;
				holdVertBarButtonData = [
					[Color.blue,
						{classData.designViewCol1Width = 200; system.showView;}],
					[TXColor.grey(0.4),
						{classData.designViewCol1Width = 500; system.showView;}],
					[TXColor.green,
						{classData.designViewCol1Width = 700; system.showView;}],
					[TXColor.blue2,
						{classData.designViewCol1Width = 900; system.showView;}],
					[TXColor.orange,
						{classData.designViewCol1Width = screenWidth+12; system.showView;}]
				];
				// add bar
				interfaceBoxBar = this.addVertBar(window, 'interfaceBoxBar',
					(4 + classData.designViewCol1Width), boxFromTop, classData, holdVertBarButtonData);
			},{
				limitWidgetUpdates = false;
				// draw screen
				classData.holdScreen = CompositeView
				(window, Rect(4, boxFromTop, screenWidth, screenHeight))
				.background_(classData.screenColour);
				// if relevent, add background image
				{
					if (classData.imageFileNames[classData.layerNo].notNil, {
						if (classData.holdImages[classData.layerNo].isNil, {
							classData.holdImages[classData.layerNo] =
							Image.open(TXPath.convert(classData.imageFileNames[classData.layerNo]));
						});
						classData.holdScreen.backgroundImage_(classData.holdImages[classData.layerNo]);
					});
				}.defer;
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
			layoutView = TXInterfaceLayoutView(interfaceBox, Rect(0, 0, screenWidth, screenHeight), TXFrontScreen.arrWidgets);
			layoutView.highlightActionFunc = {arg widget; TXFrontScreen.currWidgetInd = TXFrontScreen.arrWidgets.indexOf(widget);};
			layoutView.mouseUpActionFunc = {TXFrontScreen.checkFitWidgetsToGrid; system.showView;};
			layoutView.gridStep = classData.gridSize;

			// help texts
			StaticText(interfaceBox, Rect(0, 4 + screenHeight, screenWidth, 20))
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow)
			.align_(\left)
			.string_(" Click on a single widget and grab its corners to change its size. "
				++ "Use Shift key with mouse clicks to add to group of selected widgets. ");
			StaticText(interfaceBox, Rect(0, 28 + screenHeight, screenWidth, 20))
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow)
			.align_(\left)
			.string_(" Use Command key with mouse drag to clone selected widgets. ");

			/*	clipboards removed
			// text label for clipboard items
			StaticText(window, Rect(10 + screenWidth, 60, 80, 20))
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
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
			//TextField(window, Rect(10 + screenWidth, 180, 80, 20))
			//.action_({arg view; classData.clipboard4 = view.string;})
			//.value_(classData.clipboard4);
			*/

			// add scrollview
			propertiesScrollView = ScrollView(window, Rect(14 + classData.designViewCol1Width, boxFromTop, classData.designViewCol2Width, 612))
			.hasBorder_(false);
			propertiesScrollView.background = TXColor.sysModuleWindow;
			propertiesScrollView.action = {arg view; classData.propertiesVisibleOrigin = view.visibleOrigin; };
			// defer or else doesn't work
			{propertiesScrollView.visibleOrigin = classData.propertiesVisibleOrigin;}.defer(0.05);
			classData.arrScrollViews = classData.arrScrollViews.add(propertiesScrollView);
			propertiesBox = CompositeView(propertiesScrollView, Rect(0,0, 804, 2560));
			propertiesBox.background = TXColor.sysModuleWindow;
			propertiesBox.decorator = FlowLayout(propertiesBox.bounds);
			propertiesBox.decorator.margin.x = 0;
			propertiesBox.decorator.margin.y = 0;
			propertiesBox.decorator.reset;
			// draw properties
			TXFrontScreenGuiProperties.makeGui(system, propertiesBox);

			holdVertBarButtonData = [
				[Color.black,
					{classData.designViewCol2Width = 1; system.showView;}],
				[TXColor.blue,
					{classData.designViewCol2Width = 400; system.showView;}],
				[TXColor.blue2,
					{classData.designViewCol2Width = 600; system.showView;}],
				[TXColor.orange,
					{classData.designViewCol2Width = 804; system.showView;}]
			];
			// add bar
			propertiesBoxBar = this.addVertBar(window, 'propertiesBoxBar',
				(18 + classData.designViewCol1Width + classData.designViewCol2Width),
				boxFromTop, classData, holdVertBarButtonData);

		});
		// ================== ================== ==================


	} // end of class method makeGui

	*addVertBar {arg parent, name, posX, posY, classData, holdVertBarButtonData;
		var barHeight = 1000;
		var bar = UserView(parent, Rect(posX, posY, 9, barHeight)).background = TXColor.white;
		classData.arrUserViews = classData.arrUserViews.add(bar);
		bar.background_(Color.white);
		bar.mouseDownAction = { |view,x,y|
			classData.startPointX = x;
			classData.holdViewIndex = classData.arrUserViews.indexOf(view);
		};
		bar.mouseMoveAction_({arg view, x, y;
			var newPointX, shiftX, validMove, holdScrollView, holdUserView;
			newPointX = x;
			validMove = false;
			shiftX = (newPointX - classData.startPointX);
			holdUserView = classData.arrUserViews[classData.holdViewIndex];

			// if negative shift
			if (shiftX < 0, {
				if ( classData.holdViewIndex == 0 and:
					{((holdUserView.bounds.left + shiftX) > 10)},
					{
						validMove = true;
				});
				if ( classData.holdViewIndex > 0
					and:
					{((holdUserView.bounds.left -
						classData.arrUserViews[classData.holdViewIndex-1].bounds.left + shiftX)) > 16},
					{
						validMove = true;
				});
			},{
				// if positive shift
				if ( classData.holdViewIndex == 0 and:
					{(parent.bounds.width -
						(holdUserView.bounds.left + holdUserView.bounds.width + shiftX)) < 1},
					{
						validMove = false;
					},{
						validMove = true;
				});
			});
			if (validMove, {
				classData.arrScrollViews.do({ arg currScrollView, currScrollViewNo;
					if (currScrollViewNo == classData.holdViewIndex, {
						// set width  of current ScrollView bounds
						currScrollView.bounds = currScrollView.bounds.width_(
							(currScrollView.bounds.width + shiftX)
						);
						// set left  of current UserView bounds
						holdUserView = classData.arrUserViews[currScrollViewNo];
						holdUserView.bounds = holdUserView.bounds.left_(
							holdUserView.bounds.left + shiftX
						);
					});
					//for views to the right, move left of bounds
					if (currScrollViewNo > classData.holdViewIndex, {
						currScrollView.bounds = currScrollView.bounds.left_(
							currScrollView.bounds.left + shiftX
						);
						holdUserView = classData.arrUserViews[currScrollViewNo];
						holdUserView.bounds = holdUserView.bounds.left_(
							holdUserView.bounds.left + shiftX;
						);
					});
				});
				// adjust box size
				case
				{name == 'interfaceBoxBar'} {classData.designViewCol1Width = classData.designViewCol1Width + shiftX }
				{name == 'propertiesBoxBar'} {classData.designViewCol2Width = classData.designViewCol2Width + shiftX }
				;
			});
		});
		// draw buttons
		// e.g holdVertBarButtonData =  [ [Color.black, {dataBank.allModulesBoxWidth = 1; system.showView;}] ];
		holdVertBarButtonData.do({arg btnData, i;
			var holdButton, holdOffset;
			holdButton = UserView(bar, Rect(1, 7 + (i * 11), 7, 7));
			holdButton.background_(btnData[0].blend(Color.grey(0.6), 0.34));
			holdButton.mouseDownAction = { |view,x,y|
				btnData[1].value;
				true; // prevent passing mouseDown to parent
			};
			holdOffset = 610 - (11 * holdVertBarButtonData.size);
			holdButton = UserView(bar, Rect(1, holdOffset + (i * 11), 7, 7));
			holdButton.background_(btnData[0].blend(Color.grey(0.6), 0.34));
			holdButton.mouseDownAction = { |view,x,y|
				btnData[1].value;
				true; // prevent passing mouseDown to parent
			};
		});
		// return new bar
		^bar;
	}

	*setCurrentWidget{ arg val;
		TXFrontScreen.currWidgetInd = val;
		TXFrontScreen.arrWidgets[val].highlight = true;
	}

	*unhighlightAllViews {
		TXFrontScreen.arrWidgets.do({ |view|
			view.highlight = false;
		});
		TXFrontScreen.currWidgetInd = 0;
	}


}	// end of class
