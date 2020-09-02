// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFrontScreenGuiProperties {	// Front Screen gui

	*makeGui{ arg system, parent;

		var classData, arrSizes, arrGridSizes, screenWidth, screenHeight, holdActionType;
		var totalHighlighted, layoutView, arrLayoutXvals, arrLayoutYvals;
		var layerBar, layerPopupView, layerNameText;
		var replaceLayerActions, replaceLayerPopupView;
		var currWidgetProperties, propertiesBox;
		var screenColourPopup, screenColourRed, screenColourGreen, screenColourBlue;
		var screenColourAlpha, screenColourBox;
		var notesView, updateButton, colourPickerButton, boxColourBox;
		var screenWidthBox, screenHeightBox, offsetHeight;
		var imageNameText, addImageButton, delImageButton;
		var displayModePopupView, displayModeNumberView, displayModeItems;
		var currWidgetIDPopupView, currWidgetNameText;
		var currWidgetHeight, currWidgetWidth, currWidgetFromLeft, currWidgetFromTop;
		var backgroundName, backgroundPopup, backgroundRed, backgroundGreen, backgroundBlue, backgroundAlpha;
		var colourRevCheckbox, colourSwapButton, clearTextButton, knobColourBox, stringColorBox;
		var stringColorRed, stringColorGreen, stringColorBlue, stringColorAlpha;
		var knobColourRed, knobColourGreen, knobColourBlue, knobColourAlpha, knobWidthBox, numberSizeBox;
		var rotateButton, labelText, fontPopup, fontSizeBox, holdView, currGridSizePopupView;
		var midiListenCheckbox, midiNoBox, midiNotePopup, midiCCNoBox, midiCCNoBox2, midiLearnCheckbox;
		var midiRemoteSyncCheckbox, midiPortPopup, midiChannelMinPopup, midiChannelMaxPopup, keyListenCheckbox, keyTextField;
		var copyPropertiesButton, pastePropertiesButton, midiTypePopup, holdCurrentWidget, holdPortNames;

		classData = TXFrontScreen.classData;
		arrSizes = TXFrontScreen.classData.arrSizes;
		screenWidth = classData.screenWidths[classData.layerNo];
		screenHeight = classData.screenHeights[classData.layerNo];
		holdActionType = "valueAction";
		TXFrontScreen.currWidgetInd = TXFrontScreen.currWidgetInd ? 0;
		if (TXFrontScreen.arrWidgets.at(TXFrontScreen.currWidgetInd).class == TXWActionButton
			or: (TXFrontScreen.arrWidgets.at(TXFrontScreen.currWidgetInd).class == TXW2DTablet
				and: {TXFrontScreen.arrWidgets.at(TXFrontScreen.currWidgetInd).showActionIndex > 4})
			, {
				holdActionType = "commandAction";
		});

		// set default if nil
		if (classData.newWidgetFromModule.isNil, {
			classData.newWidgetFromModule = false;
		});

		////////////////////////////////////////////////////////////////////////////////

		// show widget properties for editing
		currWidgetProperties = TXFrontScreen.arrWidgets.at(TXFrontScreen.currWidgetInd).properties;

		// make box to display
		propertiesBox = CompositeView(parent, Rect(4, 4, 800, 2500))
		.background_(TXColor.grey(0.76));
		propertiesBox.decorator = FlowLayout(propertiesBox.bounds);

		// spacer & go to next line
		propertiesBox.decorator.shift(0, 4).nextLine;

		// text label
		holdView = StaticText(propertiesBox, Rect(0, 0, 120, 20))
		.background_(TXColor.sysGuiCol1)
		.align_(\center)
		.string_("Add New Widget");
		if (classData.newWidgetFromModule == true, {
			holdView.stringColor_(TXColor.white);
			holdView.mouseDownAction_({
				classData.newWidgetFromModule = false;
				system.showView;
			});
		},{
			holdView.stringColor_(TXColor.paleYellow2);
		});
		holdView = StaticText(propertiesBox, Rect(0, 0, 180, 20))
		.background_(TXColor.sysGuiCol1)
		.align_(\center)
		.string_("Import Widget from Module");
		if (classData.newWidgetFromModule == false, {
			holdView.stringColor_(TXColor.white);
			holdView.mouseDownAction_({
				classData.newWidgetFromModule = true;
				system.showView;
			});
		},{
			// add spacer & text
			holdView.stringColor_(TXColor.paleYellow2);
			propertiesBox.decorator.shift(10,0);
			StaticText(propertiesBox, Rect(0, 0, 180, 20))
			.stringColor_(TXColor.sysGuiCol1)
			.background_(TXColor.white)
			.align_(\center)
			.string_("Select Module and Parameter:");
		});

		// add spacer
		propertiesBox.decorator.shift(40, 0);

		// spacer
		propertiesBox.decorator.nextLine.shift(0, 6);

		// add new widget gui
		this.drawNewWidgets(system, classData, propertiesBox);

		// spacer & go to next line
		propertiesBox.decorator.shift(0, 4).nextLine;

		// line
		StaticText(propertiesBox, Rect(0, 0, 700, 2))
		.background_(TXColor.white);

		// spacer & go to next line
		propertiesBox.decorator.shift(0, 8).nextLine;

		// text label
		StaticText(propertiesBox, Rect(0, 0, 100, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow)
		.align_(\center)
		.string_("Current Screen");

		// popup - current layer
		layerPopupView = PopUpMenu(propertiesBox, Rect(0, 0, 100, 20))
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
		.items_((1 .. 20).collect({arg item, i; "Screen " ++ item.asString}))
		.action_({arg view;
			TXFrontScreen.storeCurrLoadNewLayer(view.value);
			// update variables
			TXFrontScreenGui.unhighlightAllViews;
			system.addHistoryEvent;
			// update view
			system.showView;
		});
		layerPopupView.value = classData.layerNo;

		// add spacer
		propertiesBox.decorator.shift(10, 0);

		// label -  name
		StaticText(propertiesBox, Rect(0, 0, 50, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
		.align_(\right)
		.string_("Name: " );

		// text - current layer name
		layerNameText = TextField(propertiesBox, Rect(0, 0, 250, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
		.align_(\left)
		.action_({arg view;
			classData.layerName = view.string;
			TXFrontScreen.storeCurrentLayer;
		});
		layerNameText.string = classData.layerName ;

		// spacer & go to next line
		propertiesBox.decorator.nextLine.shift(0, 6);

		// popup - replace layer
		replaceLayerActions =
		[
			["Copy or load widgets or another screen... ", {}],
			["Copy selected widgets to clipboard", {TXFrontScreen.copySelectedWidgetsToClipboard}],
			["Paste widgets from clipboard", {TXFrontScreen.pasteWidgetsFromClipboard}],
		]
		++ (1 .. 20).collect({arg item, i;
			["Copy Screen " ++ item.asString, {TXFrontScreen.overwriteCurrFromLayer(item-1);}];
		})
		++ [
			["Load screen template from disk", {TXFrontScreen.loadScreenTemplate; }],
			["Save screen template to disk", {TXFrontScreen.saveScreenTemplate}],
			// ["Load screen template from clipboard", {TXFrontScreen.loadScreenTemplateFromClipboard; }],
			// ["Save screen template to clipboard", {TXFrontScreen.saveScreenTemplateToClipboard}],
			// ["Merge widgets from clipboard", {TXFrontScreen.mergeWidgetsFromClipboard}],
		];
		replaceLayerPopupView = PopUpMenu(propertiesBox, Rect(0, 0, 260, 20))
		.background_(TXColor.sysGuiCol2).stringColor_(TXColor.white)
		.items_(replaceLayerActions.collect({arg item, i; item.at(0)}))
		.action_({arg view;
			replaceLayerActions.at(view.value).at(1).value;
			// update variables
			TXFrontScreenGui.unhighlightAllViews;
			// update view
			system.showView;
		});
		replaceLayerPopupView.value = 0;

		// spacer & go to next line
		propertiesBox.decorator.nextLine.shift(0, 6);

		// text
		StaticText(propertiesBox, Rect(0, 0, 100, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
		.align_(\left)
		.string_("Screen width");

		// numberbox - screen width
		screenWidthBox = NumberBox(propertiesBox, Rect(0, 0, 35, 20))
		.scroll_(false)
		.action_({arg view;
			TXFrontScreen.setScreenWidth(view.value);
			// update view
			system.showView;
		});
		screenWidthBox.value = screenWidth;

		// popup - width choices
		PopUpMenu(propertiesBox, Rect(0, 0, 70, 20))
		.items_(classData.arrScreenSizes.collect({arg item, i; item.asString});)
		.background_(TXColor.white)
		.action_({arg view;
			if (view.value > 0, {screenWidthBox.valueAction = classData.arrScreenSizes.at(view.value);});
		});

		// text
		StaticText(propertiesBox, Rect(0, 0, 100, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
		.align_(\left)
		.string_("Screen height");

		// numberbox - screen height
		screenHeightBox = NumberBox(propertiesBox, Rect(0, 0, 35, 20))
		.scroll_(false)
		.action_({arg view;
			TXFrontScreen.setScreenHeight(view.value);
			// update view
			system.showView;
		});
		screenHeightBox.value = screenHeight;

		// popup - height choices
		PopUpMenu(propertiesBox, Rect(0, 0, 70, 20))
		.items_(classData.arrScreenSizes.collect({arg item, i; item.asString});)
		.background_(TXColor.white)
		.action_({arg view;
			if (view.value > 0, {
				screenHeightBox.valueAction = classData.arrScreenSizes.at(view.value);
			});
		});

		// spacer & go to next line
		propertiesBox.decorator.shift(0, 6).nextLine;

		// label - background image
		StaticText(propertiesBox, Rect(0, 0, 100, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
		.align_(\left)
		.string_("Screen image");
		// text - image file name
		imageNameText = StaticText(propertiesBox, Rect(0, 0, 340, 30))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
		.align_(\left);
		imageNameText.string = (classData.imageFileNames[classData.layerNo] ? " ").keep(60);

		// button - add image
		addImageButton = Button(propertiesBox, Rect(0, 0, 90, 20));
		addImageButton.states = [
			["Add PNG Image", TXColor.white, TXColor.sysGuiCol1]
		];
		addImageButton.action = {TXFrontScreen.addImageDialog;};

		// button - delete image
		delImageButton = Button(propertiesBox, Rect(0, 0, 50, 20));
		delImageButton.states = [
			["Delete", TXColor.white, TXColor.sysDeleteCol]
		];
		delImageButton.action = {
			classData.imageFileNames[classData.layerNo] = nil;
			classData.holdImages[classData.layerNo] = nil;
			// recreate view
			system.showView;
		};

		// spacer & go to next line
		propertiesBox.decorator.nextLine.shift(0, 6);

		/*
		REMOVED

		// label -  image mode
		StaticText(propertiesBox, Rect(0, 0, 100, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
		.align_(\left)
		.string_("Image mode");
		//display mode
		displayModeItems = [
		"0 - off - image not shown, screen colour is used",
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
		// popup - display mode
		displayModePopupView = PopUpMenu(propertiesBox, Rect(0, 0, 340, 20))
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
		.items_(displayModeItems)
		.action_({arg view;
		classData.displayModeIndices[classData.layerNo] = view.value;
		// update view
		system.showView;
		});
		displayModePopupView.value = classData.displayModeIndices[classData.layerNo];

		// spacer & go to next line
		propertiesBox.decorator.nextLine.shift(0, 6);

		*/

		// text label
		StaticText(propertiesBox, Rect(0, 0, 100, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
		.align_(\left)
		.string_("Screen colour");

		// screenColourbox
		screenColourBox = DragBoth.new(propertiesBox, Rect(0, 0, 35, 20));
		screenColourBox.background_(classData.screenColour);
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
			classData.screenColour = holdDragObject;
			// update view
			system.showView;
		};

		// colourPickerButton
		colourPickerButton = Button(propertiesBox, 45 @ 20)
		.states_([["Picker", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			TXColor.showPicker;
		});
		// popup - screenColour presets
		screenColourPopup = PopUpMenu(propertiesBox, Rect(0, 0, 110, 20))
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
		.items_(["presets"] ++ TXColor.colourNames)
		.action_({arg view;
			if (view.value > 0, {
				classData.screenColour =
				TXColor.perform(TXColor.colourNames.at(view.value - 1).asSymbol).copy;
				// update view
				system.showView;
			});
		});

		// text label
		StaticText(propertiesBox, Rect(0, 0, 20, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.pink4)
		.align_(\center)
		.string_("R");

		// numberbox - screenColour red
		screenColourRed = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
		.scroll_(false)
		.action_({arg view;
			view.value = view.value.max(0).min(255);
			classData.screenColour.red = view.value /255;
			// update view
			system.showView;
		});
		screenColourRed.value = (classData.screenColour.red * 255).round;
		// text label
		StaticText(propertiesBox, Rect(0, 0, 20, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleGreen)
		.align_(\center)
		.string_("G");

		// numberbox - screenColour green
		screenColourGreen = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
		.scroll_(false)
		.action_({arg view;
			view.value = view.value.max(0).min(255);
			classData.screenColour.green = view.value /255;
			// update view
			system.showView;
		});
		screenColourGreen.value = (classData.screenColour.green * 255).round;
		// text label
		StaticText(propertiesBox, Rect(0, 0, 20, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue)
		.align_(\center)
		.string_("B");
		// numberbox - screenColour blue
		screenColourBlue = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
		.scroll_(false)
		.action_({arg view;
			view.value = view.value.max(0).min(255);
			classData.screenColour.blue = view.value /255;
			// update view
			system.showView;
		});
		screenColourBlue.value = (classData.screenColour.blue * 255).round;
		// text label
		StaticText(propertiesBox, Rect(0, 0, 35, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
		.align_(\left)
		.string_("Alpha");
		// numberbox - screenColour alpha
		screenColourAlpha = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
		.scroll_(false)
		.action_({arg view;
			view.value = view.value.max(0).min(255);
			classData.screenColour.alpha = view.value /255;
			// update view
			system.showView;
		});
		screenColourAlpha.value = (classData.screenColour.alpha * 255).round;

		// spacer & go to next line
		propertiesBox.decorator.shift(0, 4).nextLine;

		// line
		StaticText(propertiesBox, Rect(0, 0, 700, 2))
		.background_(TXColor.white);

		// spacer & go to next line
		propertiesBox.decorator.shift(0, 8).nextLine;

		// only display current widget if not more than 1 selected
		totalHighlighted = TXFrontScreen.arrWidgets.select({arg item, i; item.highlight == true;}).size;
		if (totalHighlighted == 0, {
			propertiesBox.decorator.shift(0, 8).nextLine;
			// text label
			StaticText(propertiesBox, Rect(0, 0, 134, 30))
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow)
			.align_(\center)
			.string_("0 widgets selected");
		}, {
			if (totalHighlighted > 1, {
				propertiesBox.decorator.shift(0, 8).nextLine;
				// text label
				StaticText(propertiesBox, Rect(0, 0, 134, 30))
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow)
				.align_(\center)
				.string_(totalHighlighted.asString ++ " widgets selected");
			}, {
				if (totalHighlighted == 1, {
					holdCurrentWidget = TXFrontScreen.arrWidgets.at(TXFrontScreen.currWidgetInd);
					// text label
					StaticText(propertiesBox, Rect(0, 0, 100, 20))
					.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow)
					.align_(\center)
					.string_("Current Widget");
					// popup - current widget id
					currWidgetIDPopupView = PopUpMenu(propertiesBox, Rect(0, 0, 70, 20))
					.background_(TXColor.white).stringColor_(TXColor.black)
					.items_(TXFrontScreen.arrWidgets.collect({arg item, i; "W " ++ item.widgetID.asString}))
					.action_({arg view;
						TXFrontScreenGui.unhighlightAllViews;
						TXFrontScreen.setCurrentWidget(view.value);
						currWidgetNameText.string = TXFrontScreen.arrWidgets.at(view.value).class.widgetName;
						// update view
						system.showView;
					});
					currWidgetIDPopupView.value = TXFrontScreen.currWidgetInd;
					// add spacer
					propertiesBox.decorator.shift(10, 0);

					// label -  Type
					StaticText(propertiesBox, Rect(0, 0, 50, 20))
					.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
					.align_(\right)
					.string_("Type: " );
					// text - current module name
					currWidgetNameText = StaticText(propertiesBox, Rect(0, 0, 160, 20))
					.stringColor_(TXColor.black).background_(TXColor.white)
					.align_(\left);
					currWidgetNameText.string = holdCurrentWidget.class.widgetName;

					/*
					// button - Delete widget
					Button(propertiesBox, Rect(0, 0, 100, 20))
					.states_([["Delete widget", TXColor.white, TXColor.sysDeleteCol]])
					.action_({
					// delete widget
					TXFrontScreen.deleteWidgetAtInd(TXFrontScreen.currWidgetInd);
					// update variables
					TXFrontScreenGui.unhighlightAllViews;
					// update view
					system.showView;
					});
					*/

					// spacer & go to next line
					propertiesBox.decorator.nextLine.shift(0, 6);
					// copy properties button
					copyPropertiesButton = Button(propertiesBox, Rect(0, 0, 180, 20))
					.states_([["Copy all widget properties",
						TXColor.white, TXColor.sysGuiCol2]]);
					copyPropertiesButton.action = {arg view;
						classData.holdWidgetClass =
						holdCurrentWidget.class.asSymbol;
						classData.holdWidgetPropertyList =
						holdCurrentWidget.getPropertyList;
					};
					// paste properties button
					pastePropertiesButton = Button(propertiesBox, Rect(0, 0, 180, 20))
					.states_([["Paste all widget properties",
						TXColor.white, TXColor.sysGuiCol2]]);
					pastePropertiesButton.action = {arg view;
						var selectedPropertyList;
						// only paste relevent properties
						if (classData.holdWidgetClass.notNil, {
							// if classes match, copy all properties, else only display properties
							if (classData.holdWidgetClass ==
								holdCurrentWidget.class.asSymbol,
								{
									selectedPropertyList = classData.holdWidgetPropertyList.select({arg item, i;
										TXFrontScreen.copyAllProperties.includes(item.at(0));
									});
								},{
									selectedPropertyList = classData.holdWidgetPropertyList.select({arg item, i;
										TXFrontScreen.copyDisplayProperties.includes(item.at(0));
									});
							});
							holdCurrentWidget
							.setPropertyList(selectedPropertyList.deepCopy);
						});
						// update view
						system.showView;
					};
					// ==========================================================================================

					// spacer & go to next line
					propertiesBox.decorator.nextLine.shift(0, 6);

					// text label
					StaticText(propertiesBox, Rect(0, 0, 100, 20))
					.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
					.align_(\left)
					.string_("Widget size");

					// text label
					StaticText(propertiesBox, Rect(0, 0, 50, 20))
					.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
					.align_(\left)
					.string_("Width");

					// numberbox - current module width
					currWidgetWidth = NumberBox(propertiesBox, Rect(0, 0, 70, 20))
					.scroll_(false)
					.action_({arg view;
						view.value = view.value
						.max(holdCurrentWidget.widthMin)
						.min(holdCurrentWidget.widthMax);
						holdCurrentWidget.width_(view.value, screenWidth);
						TXFrontScreen.checkFitWidgetsToGrid;
						// update view
						system.showView;
					});
					currWidgetWidth.value = holdCurrentWidget.width;

					// popup - width choices
					PopUpMenu(propertiesBox, Rect(0, 0, 70, 20))
					.items_(arrSizes.collect({arg item, i; item.asString});)
					.background_(TXColor.white)
					.action_({arg view;
						if (view.value > 0, {currWidgetWidth.valueAction = arrSizes.at(view.value);});
					});

					// text label
					StaticText(propertiesBox, Rect(0, 0, 50, 20))
					.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
					.align_(\left)
					.string_("Height");

					// numberbox - current module height
					currWidgetHeight = NumberBox(propertiesBox, Rect(0, 0, 35, 20))
					.scroll_(false)
					.action_({arg view;
						view.value = view.value
						.max(holdCurrentWidget.heightMin)
						.min(holdCurrentWidget.heightMax);
						holdCurrentWidget.height_(view.value, screenHeight);
						TXFrontScreen.checkFitWidgetsToGrid;
						// update view
						system.showView;
					});
					currWidgetHeight.value = holdCurrentWidget.height;

					// popup - height choices
					PopUpMenu(propertiesBox, Rect(0, 0, 70, 20))
					.items_(arrSizes.collect({arg item, i; item.asString});)
					.background_(TXColor.white)
					.action_({arg view;
						if (view.value > 0, {currWidgetHeight.valueAction = arrSizes.at(view.value);});
					});

					// button swap width and height
					rotateButton = Button(propertiesBox, Rect(0, 0, 120, 20))
					.states_([["Swap width & height",
						TXColor.white, TXColor.sysGuiCol1]]);
					rotateButton.action = {arg view;
						var holdHeight, holdWidth;
						holdHeight = holdCurrentWidget.width;
						holdWidth = holdCurrentWidget.height;
						holdCurrentWidget.height_(holdHeight);
						holdCurrentWidget.width_(holdWidth);
						// update view
						system.showView;
					};

					// spacer & go to next line
					propertiesBox.decorator.nextLine.shift(0, 6);

					// text label
					StaticText(propertiesBox, Rect(0, 0, 100, 20))
					.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
					.align_(\left)
					.string_("Widget position");

					// text label
					StaticText(propertiesBox, Rect(0, 0, 60, 20))
					.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
					.align_(\left)
					.string_("From left");

					// numberbox - current module from left
					currWidgetFromLeft = NumberBox(propertiesBox, Rect(0, 0, 35, 20))
					.scroll_(false)
					.action_({arg view;
						view.value = view.value.max(0)
						.min(screenWidth - holdCurrentWidget.width);
						holdCurrentWidget.fromLeft_(view.value, screenWidth);
						// update view
						system.showView;
					});
					currWidgetFromLeft.value =
					holdCurrentWidget.fromLeft(screenWidth).asInteger;

					// text label
					StaticText(propertiesBox, Rect(0, 0, 60, 20))
					.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
					.align_(\left)
					.string_("From top");

					// numberbox - current module from top
					currWidgetFromTop = NumberBox(propertiesBox, Rect(0, 0, 35, 20))
					.scroll_(false)
					.action_({arg view;
						view.value = view.value.max(0)
						.min(screenHeight - holdCurrentWidget.height);
						holdCurrentWidget.fromTop_(view.value, screenHeight);
						// update view
						system.showView;
					});
					currWidgetFromTop.value =
					holdCurrentWidget.fromTop(screenHeight).asInteger;

					// spacer & go to next line
					propertiesBox.decorator.nextLine.shift(0, 6);

					// set label text
					if ((holdCurrentWidget.class == TXWKnob), {
						backgroundName = "Level colour"
					},{
						backgroundName = "Box colour"
					});
					// text label
					StaticText(propertiesBox, Rect(0, 0, 100, 20))
					.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
					.align_(\left)
					.string_(backgroundName);
					// boxColourBox
					boxColourBox = DragBoth.new(propertiesBox, Rect(0, 0, 40, 20));
					boxColourBox.background_(holdCurrentWidget.background);
					boxColourBox.beginDragAction_({ arg view, x, y;
						view.dragLabel_("Colour");
						boxColourBox.background;
					});
					boxColourBox.canReceiveDragHandler = {
						View.currentDrag.isKindOf( Color )
					};
					boxColourBox.receiveDragHandler = {
						var holdDragObject;
						holdDragObject = View.currentDrag;
						boxColourBox.background_(holdDragObject);
						backgroundRed.value = (boxColourBox.background.red * 255).round;
						backgroundGreen.value = (boxColourBox.background.green * 255).round;
						backgroundBlue.value = (boxColourBox.background.blue * 255).round;
						holdCurrentWidget.background = holdDragObject;
						// update view
						system.showView;
					};

					// colourPickerButton
					colourPickerButton = Button(propertiesBox, 45 @ 20)
					.states_([["Picker", TXColor.white, TXColor.sysGuiCol1]])
					.action_({
						TXColor.showPicker;
					});

					// popup - background presets
					backgroundPopup = PopUpMenu(propertiesBox, Rect(0, 0, 110, 20))
					.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
					.items_(["presets"] ++ TXColor.colourNames)
					.action_({arg view;
						if (view.value > 0, {
							holdCurrentWidget.background =
							TXColor.perform(TXColor.colourNames.at(view.value - 1).asSymbol).copy;
							// update view
							system.showView;
						});
					});

					// text label
					StaticText(propertiesBox, Rect(0, 0, 20, 20))
					.stringColor_(TXColor.sysGuiCol1).background_(TXColor.pink4)
					.align_(\center)
					.string_("R");

					// numberbox - background red
					backgroundRed = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
					.scroll_(false)
					.action_({arg view;
						view.value = view.value.max(0).min(255);
						holdCurrentWidget.background.red =
						view.value /255;
						// update view
						system.showView;
					});
					backgroundRed.value =
					(holdCurrentWidget.background.red * 255).round;
					// text label
					StaticText(propertiesBox, Rect(0, 0, 20, 20))
					.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleGreen)
					.align_(\center)
					.string_("G");

					// numberbox - background green
					backgroundGreen = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
					.scroll_(false)
					.action_({arg view;
						view.value = view.value.max(0).min(255);
						holdCurrentWidget.background.green = view.value /255;
						// update view
						system.showView;
					});
					backgroundGreen.value =
					(holdCurrentWidget.background.green * 255).round;
					// text label
					StaticText(propertiesBox, Rect(0, 0, 20, 20))
					.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue)
					.align_(\center)
					.string_("B");
					// numberbox - background blue
					backgroundBlue = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
					.scroll_(false)
					.action_({arg view;
						view.value = view.value.max(0).min(255);
						holdCurrentWidget.background.blue = view.value /255;
						// update view
						system.showView;
					});
					backgroundBlue.value =
					(holdCurrentWidget.background.blue * 255).round;
					// text label
					StaticText(propertiesBox, Rect(0, 0, 35, 20))
					.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
					.align_(\left)
					.string_("Alpha");
					// numberbox - background alpha
					backgroundAlpha = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
					.scroll_(false)
					.action_({arg view;
						view.value = view.value.max(0).min(255);
						holdCurrentWidget.background.alpha = view.value /255;
						// update view
						system.showView;
					});
					backgroundAlpha.value =
					(holdCurrentWidget.background.alpha * 255).round;

					// ==========================================================================================

					// check properties
					if ((holdCurrentWidget.class == TXWSlider)
						or: (holdCurrentWidget.class == TXWSliderV)
						or: (holdCurrentWidget.class == TXWSliderNo)
						or: (holdCurrentWidget.class == TXWSliderNoV)
						or: (holdCurrentWidget.class == TXWKnob)
						or: (holdCurrentWidget.class == TXW2DSlider)
						or: (holdCurrentWidget.class == TXW2DTablet)
						,{
							// spacer & next line
							propertiesBox.decorator.nextLine.shift(0, 6);

							// text label
							StaticText(propertiesBox, Rect(0, 0, 100, 20))
							.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
							.align_(\left)
							.string_("Knob colour");
							// knobColourBox
							knobColourBox = DragBoth.new(propertiesBox, Rect(0, 0, 40, 20));
							knobColourBox.background_(holdCurrentWidget.knobColour);
							knobColourBox.beginDragAction_({ arg view, x, y;
								view.dragLabel_("Colour");
								knobColourBox.background;
							});
							knobColourBox.canReceiveDragHandler = {
								View.currentDrag.isKindOf( Color )
							};
							knobColourBox.receiveDragHandler = {
								var holdDragObject;
								holdDragObject = View.currentDrag;
								knobColourBox.background_(holdDragObject);
								knobColourRed.value = (knobColourBox.background.red * 255).round;
								knobColourGreen.value = (knobColourBox.background.green * 255).round;
								knobColourBlue.value = (knobColourBox.background.blue * 255).round;
								holdCurrentWidget.knobColour = holdDragObject;
								// update view
								system.showView;
							};

							// colourPickerButton
							colourPickerButton = Button(propertiesBox, 45 @ 20)
							.states_([["Picker", TXColor.white, TXColor.sysGuiCol1]])
							.action_({
								TXColor.showPicker;
							});

							// popup - background presets
							backgroundPopup = PopUpMenu(propertiesBox, Rect(0, 0, 110, 20))
							.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
							.items_(["presets"] ++ TXColor.colourNames)
							.action_({arg view;
								if (view.value > 0, {
									holdCurrentWidget.knobColour =
									TXColor.perform(TXColor.colourNames.at(view.value - 1).asSymbol).copy;
									// update view
									system.showView;
								});
							});

							// text label
							StaticText(propertiesBox, Rect(0, 0, 20, 20))
							.stringColor_(TXColor.sysGuiCol1).background_(TXColor.pink4)
							.align_(\center)
							.string_("R");

							// numberbox - knobColour red
							knobColourRed = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
							.scroll_(false)
							.action_({arg view;
								view.value = view.value.max(0).min(255);
								holdCurrentWidget.knobColour.red = view.value /255;
								// update view
								system.showView;
							});
							knobColourRed.value =
							(holdCurrentWidget.knobColour.red * 255).round;
							// text label
							StaticText(propertiesBox, Rect(0, 0, 20, 20))
							.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleGreen)
							.align_(\center)
							.string_("G");

							// numberbox - knobColour green
							knobColourGreen = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
							.scroll_(false)
							.action_({arg view;
								view.value = view.value.max(0).min(255);
								holdCurrentWidget.knobColour.green = view.value /255;
								// update view
								system.showView;
							});
							knobColourGreen.value =
							(holdCurrentWidget.knobColour.green * 255).round;
							// text label
							StaticText(propertiesBox, Rect(0, 0, 20, 20))
							.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue)
							.align_(\center)
							.string_("B");
							// numberbox - knobColour blue
							knobColourBlue = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
							.scroll_(false)
							.action_({arg view;
								view.value = view.value.max(0).min(255);
								holdCurrentWidget.knobColour.blue = view.value /255;
								// update view
								system.showView;
							});
							knobColourBlue.value =
							(holdCurrentWidget.knobColour.blue * 255).round;
							// text label
							StaticText(propertiesBox, Rect(0, 0, 35, 20))
							.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
							.align_(\left)
							.string_("Alpha");
							// numberbox - knobColour alpha
							knobColourAlpha = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
							.scroll_(false)
							.action_({arg view;
								view.value = view.value.max(0).min(255);
								holdCurrentWidget.knobColour.alpha = view.value /255;
								// update view
								system.showView;
							});
							knobColourAlpha.value =
							(holdCurrentWidget.knobColour.alpha * 255).round;

							// go to next line
							propertiesBox.decorator.nextLine;

							// colour swap button
							colourSwapButton = Button(propertiesBox, Rect(0, 0, 240, 20))
							.states_([["Swap knob colour and box colour",
								TXColor.white, TXColor.sysGuiCol1]]);
							colourSwapButton.action = {arg view;
								var holdKnobColor2, holdBackground2;
								holdKnobColor2 = holdCurrentWidget.background;
								holdBackground2 = holdCurrentWidget.knobColour;
								holdCurrentWidget.knobColour = holdKnobColor2;
								holdCurrentWidget.background = holdBackground2;
								// update view
								system.showView;
							};
							// check properties
							if ((holdCurrentWidget.class != TXWKnob), {
								// spacer & go to next line
								propertiesBox.decorator.nextLine.shift(0, 6);

								// text label
								StaticText(propertiesBox, Rect(0, 0, 100, 20))
								.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
								.align_(\left)
								.string_("Knob size");

								// numberbox - knob width
								knobWidthBox = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
								.scroll_(false)
								.action_({arg view;
									view.value = view.value.max(0).min(600);
									holdCurrentWidget.thumbSize = view.value;
									// update view
									system.showView;
								});
								knobWidthBox.value = holdCurrentWidget.thumbSize;
							});

							// check properties
							if ((holdCurrentWidget.class == TXWSliderNo)
								or: (holdCurrentWidget.class == TXWSliderNoV)
								, {

									// spacer
									propertiesBox.decorator.shift(10, 0);

									// text label
									StaticText(propertiesBox, Rect(0, 0, 70, 20))
									.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
									.align_(\left)
									.string_("Number size");

									// numberbox - number size
									numberSizeBox = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
									.scroll_(false)
									.action_({arg view;
										view.value = view.value.max(0).min(600);
										holdCurrentWidget.numberSize = view.value;
										// update view
										system.showView;
									});
									numberSizeBox.value = holdCurrentWidget.numberSize ;

							});
					});

					// ==========================================================================================

					// check properties
					if (holdCurrentWidget.properties.includes(\string) and:
						(holdCurrentWidget.class != TXWNotesBox), {

							// spacer & go to next line
							propertiesBox.decorator.nextLine.shift(0, 6);
							// text label
							StaticText(propertiesBox, Rect(0, 0, 120, 20))
							.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
							.align_(\left)
							.string_("Text to be displayed");
							// text box
							labelText = TextField(propertiesBox, Rect(0, 0, 340, 20))
							.action_({arg view;
								holdCurrentWidget.string = view.string;
								// update view
								system.showView;
							})
							.align_(\left);
							labelText.string = holdCurrentWidget.string;
							// clear text button
							clearTextButton = Button(propertiesBox, Rect(0, 0, 60, 20))
							.states_([["Clear text",
								TXColor.white, TXColor.sysDeleteCol]])
							.action = {arg view;
								holdCurrentWidget.string = "";
								// update view
								system.showView;
							};
					});

					// ==========================================================================================

					// check properties
					if (holdCurrentWidget.properties.includes(\stringColorAsArgs), {

						// spacer & go to next line
						propertiesBox.decorator.nextLine.shift(0, 6);
						// text label
						StaticText(propertiesBox, Rect(0, 0, 100, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
						.align_(\left)
						.string_("Text colour");
						// stringColorBox
						stringColorBox = DragBoth.new(propertiesBox, Rect(0, 0, 40, 20));
						stringColorBox.background_(holdCurrentWidget.stringColor);
						stringColorBox.beginDragAction_({ arg view, x, y;
							view.dragLabel_("Colour");
							stringColorBox.background;
						});
						stringColorBox.canReceiveDragHandler = {
							View.currentDrag.isKindOf( Color )
						};
						stringColorBox.receiveDragHandler = {
							var holdDragObject;
							holdDragObject = View.currentDrag;
							stringColorBox.background_(holdDragObject);
							holdCurrentWidget.stringColor = holdDragObject;
							// update view
							system.showView;
						};

						// colourPickerButton
						colourPickerButton = Button(propertiesBox, 45 @ 20)
						.states_([["Picker", TXColor.white, TXColor.sysGuiCol1]])
						.action_({
							TXColor.showPicker;
						});

						// popup - background presets
						backgroundPopup = PopUpMenu(propertiesBox, Rect(0, 0, 110, 20))
						.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
						.items_(["presets"] ++ TXColor.colourNames)
						.action_({arg view;
							if (view.value > 0, {
								holdCurrentWidget.stringColor =
								TXColor.perform(TXColor.colourNames.at(view.value - 1).asSymbol).copy;
								// update view
								system.showView;
							});
						});

						// text label
						StaticText(propertiesBox, Rect(0, 0, 20, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.pink4)
						.align_(\center)
						.string_("R");

						// numberbox - stringColor red
						stringColorRed = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
						.scroll_(false)
						.action_({arg view;
							view.value = view.value.max(0).min(255);
							holdCurrentWidget.stringColor.red = view.value /255;
							// update view
							system.showView;
						});
						stringColorRed.value =
						(holdCurrentWidget.stringColor.red * 255).round;
						// text label
						StaticText(propertiesBox, Rect(0, 0, 20, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleGreen)
						.align_(\center)
						.string_("G");

						// numberbox - stringColor green
						stringColorGreen = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
						.scroll_(false)
						.action_({arg view;
							view.value = view.value.max(0).min(255);
							holdCurrentWidget.stringColor.green = view.value /255;
							// update view
							system.showView;
						});
						stringColorGreen.value =
						(holdCurrentWidget.stringColor.green * 255).round;
						// text label
						StaticText(propertiesBox, Rect(0, 0, 20, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue)
						.align_(\center)
						.string_("B");
						// numberbox - stringColor blue
						stringColorBlue = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
						.scroll_(false)
						.action_({arg view;
							view.value = view.value.max(0).min(255);
							holdCurrentWidget.stringColor.blue = view.value /255;
							// update view
							system.showView;
						});
						stringColorBlue.value =
						(holdCurrentWidget.stringColor.blue * 255).round;
						// text label
						StaticText(propertiesBox, Rect(0, 0, 35, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
						.align_(\left)
						.string_("Alpha");
						// numberbox - stringColor alpha
						stringColorAlpha = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
						.scroll_(false)
						.action_({arg view;
							view.value = view.value.max(0).min(255);
							holdCurrentWidget.stringColor.alpha = view.value /255;
							// update view
							system.showView;
						});
						stringColorAlpha.value = (TXFrontScreen.arrWidgets
							.at(TXFrontScreen.currWidgetInd).stringColor.alpha * 255).round;

						// go to next line
						propertiesBox.decorator.nextLine;

						// colour swap button
						colourSwapButton = Button(propertiesBox, Rect(0, 0, 240, 20))
						.states_([["Swap text colour and box colour",
							TXColor.white, TXColor.sysGuiCol2]]);
						colourSwapButton.action = {arg view;
							var holdStringColor2, holdBackground2;
							holdStringColor2 = holdCurrentWidget.background;
							holdBackground2 = holdCurrentWidget.stringColor;
							holdCurrentWidget.stringColor = holdStringColor2;
							holdCurrentWidget.background = holdBackground2;
							// update view
							system.showView;
						};

						if (holdCurrentWidget.class == TXWCheckBox, {
							// checkbox reverse colours
							colourRevCheckbox = TXCheckBox(propertiesBox, Rect(0, 0, 240, 20),
								"Reverse colours when switched on",
								TXColor.sysGuiCol1, TXColor.white,
								TXColor.white, TXColor.sysGuiCol2);
							colourRevCheckbox.action = {arg view;
								holdCurrentWidget.colourReverse = view.value;
							};
							colourRevCheckbox.value =
							holdCurrentWidget.colourReverse ? 0;

						});

						// spacer & go to next line
						propertiesBox.decorator.nextLine.shift(0, 6);

						// text label
						StaticText(propertiesBox, Rect(0, 0, 100, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
						.align_(\left)
						.string_("Font");

						// popup - font
						fontPopup = PopUpMenu(propertiesBox, Rect(105, 30, 140, 20))
						.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
						.items_(classData.arrFonts)
						.action_({arg view;
							if (view.value > 0, {
								holdCurrentWidget.font =
								classData.arrFonts.at(view.value);
								// update view
								system.showView;
							});
						});
						fontPopup.value =
						classData.arrFonts.indexOf(holdCurrentWidget.font) ? 0;

						// numberbox - font size
						fontSizeBox = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
						.scroll_(false)
						.action_({arg view;
							view.value = view.value.max(2).min(200);
							holdCurrentWidget.fontSize = view.value;
							// update view
							system.showView;
						});
						fontSizeBox.value = holdCurrentWidget.fontSize;

						if (holdCurrentWidget.class == TXWLabelBox, {
							// spacer & go to next line
							propertiesBox.decorator.nextLine.shift(0, 6);
							// text label
							StaticText(propertiesBox, Rect(0, 0, 100, 20))
							.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
							.align_(\left)
							.string_("Align");

							// popup - align
							holdView = PopUpMenu(propertiesBox, Rect(105, 30, 140, 20))
							.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1)
							.items_(["left", "right", "center"])
							.action_({arg view;
								holdCurrentWidget.align = ['left', 'right', 'center'].at(view.value);
								// update view
								system.showView;
							});
							holdView.value =
							['left', 'right', 'center'].indexOf(holdCurrentWidget.align) ? 0;
						});

					});

					// ==========================================================================================

					if (holdCurrentWidget.class == TXWActionButton, {

						// midi note trigger

						// spacer & go to next line
						propertiesBox.decorator.nextLine.shift(0, 6);
						// text label
						StaticText(propertiesBox, Rect(0, 0, 100, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
						.align_(\left)
						.string_("Midi Trigger");

						// checkbox midi Listen
						midiListenCheckbox = TXCheckBox(propertiesBox, Rect(0, 0, 40, 20),
							" ", TXColor.black, TXColor.white,
							TXColor.black, TXColor.yellow, 4);
						midiListenCheckbox.action = {arg view;
							holdCurrentWidget.midiListen = view.value;
						};
						midiListenCheckbox.value = holdCurrentWidget.midiListen ? 0;

						// text label
						StaticText(propertiesBox, Rect(0, 0, 30, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
						.align_(\left)
						.string_("Type");

						// PopUp - midi type
						midiTypePopup = PopUpMenu(propertiesBox, Rect(0, 0, 90, 20))
						.items_(["Note", "Controller"])
						.background_(TXColor.white)
						.action_({arg view;
							if (view.value == 0, {
								holdCurrentWidget.midiType = \note;
							},{
								holdCurrentWidget.midiType = \controller;
							});
							holdCurrentWidget.midiActivate;
							// update view
							system.showView;
						});
						if (holdCurrentWidget.midiType == \note, {
							midiTypePopup.value = 0;
						},{
							midiTypePopup.value = 1;
						});

						// text label
						StaticText(propertiesBox, Rect(0, 0, 30, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
						.align_(\left)
						.string_("No");

						// numberbox - midi note or controller no
						midiNoBox = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
						.scroll_(false)
						.action_({arg view;
							view.value = view.value.max(0).min(127);
							holdCurrentWidget.midiNote = view.value;
							if (holdCurrentWidget.midiType == \note, {
								midiNotePopup.value = view.value;
							});
							// update view
							system.showView;
						});
						midiNoBox.value = holdCurrentWidget.midiNote ? 0;
						if (holdCurrentWidget.midiType == \note, {
							// popup - midi note text
							midiNotePopup = PopUpMenu(propertiesBox, Rect(0, 0, 60, 20))
							.items_((0 .. 127).collect({arg item, i; TXGetMidiNoteString.new(item)});)
							.background_(TXColor.white)
							.action_({arg view;
								midiNoBox.valueAction = view.value;
							});
							midiNotePopup.value = holdCurrentWidget.midiNote ? 0;
						});

						holdPortNames = ["Any Port"]
						++ MIDIClient.sources.collect({arg item, i;
							// remove any brackets from string
							(item.device.asString + item.name.asString).replace("(", "").replace(")", "")
						});

						// text label
						StaticText(propertiesBox, Rect(0, 0, 40, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
						.align_(\left)
						.string_("Port");
						// PopUp - midi port
						midiPortPopup = PopUpMenu(propertiesBox, Rect(0, 0, 140, 20))
						.items_(holdPortNames)
						.background_(TXColor.white)
						.action_({arg view;
							if (view.value == 0, {
								holdCurrentWidget.midiPortUid = nil;
								holdCurrentWidget.midiDeviceName = nil;
								holdCurrentWidget.midiPortName = nil;
							},{
								holdCurrentWidget.midiPortUid = MIDIClient.sources[view.value - 1].uid;
								holdCurrentWidget.midiDeviceName =  MIDIClient.sources[view.value - 1].device;
								holdCurrentWidget.midiPortName =  MIDIClient.sources[view.value - 1].name;
							});
							// update view
							system.showView;
						});
						MIDIClient.sources.do({ arg item, i;
							if (MIDIClient.sources[i].uid == holdCurrentWidget.midiPortUid, {
								midiPortPopup.value = i + 1;
							});
						});

						// text label
						StaticText(propertiesBox, Rect(0, 0, 60, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
						.align_(\left)
						.string_("Channels");
						// PopUp - midi channel min
						midiChannelMinPopup = PopUpMenu(propertiesBox, Rect(0, 0, 40, 20))
						.items_((1..16).collect({arg item, i; item.asString}))
						.background_(TXColor.white)
						.action_({arg view;
							holdCurrentWidget.midiMinChannel = view.value + 1;
							// update view
							system.showView;
						});
						midiChannelMinPopup.value = (holdCurrentWidget.midiMinChannel ? 1) - 1;
						// text label
						StaticText(propertiesBox, Rect(0, 0, 20, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
						.align_(\center)
						.string_("to");

						// PopUp - midi channel max
						midiChannelMaxPopup = PopUpMenu(propertiesBox, Rect(0, 0, 40, 20))
						.items_((1..16).collect({arg item, i; item.asString}))
						.background_(TXColor.white)
						.action_({arg view;
							holdCurrentWidget.midiMaxChannel = view.value + 1;
							// update viewport
							system.showView;
						});
						midiChannelMaxPopup.value =
						(holdCurrentWidget.midiMaxChannel ? 1) - 1;

						// spacer
						propertiesBox.decorator.shift(10, 0);

						// checkbox Midi Note Learn
						midiLearnCheckbox = TXCheckBox(propertiesBox, Rect(0, 0, 100, 20),
							"MIDI Learn", TXColor.sysGuiCol1, TXColor.white,
							TXColor.white, TXColor.sysGuiCol1, 0);
						midiLearnCheckbox.action = {arg view;
							var midiNoteResponder, midiCCResponder;
							if (view.value == 1, {
								TXFrontScreen.midiDeActivate;
								classData.midiLearnHoldVal = nil;
								midiNoteResponder = MIDIFunc.noteOn({  |vel, note, chan, src|
									var holdMIDIEndPoint, index;
									//  set min/max channel and note no
									holdCurrentWidget.midiMinChannel = chan + 1;
									holdCurrentWidget.midiMaxChannel = chan + 1;
									holdCurrentWidget.midiNote = note;
									holdCurrentWidget.midiType = \note;
									holdCurrentWidget.midiListen = 1;
									holdCurrentWidget.midiPortUid = src;
									holdCurrentWidget.midiDeviceName = nil;
									holdCurrentWidget.midiPortName = nil;
									index = 0;
									while ( {holdMIDIEndPoint.isNil && (index < MIDIClient.sources.size)}, {
										if (MIDIClient.sources[index].uid == src, {
											holdMIDIEndPoint = MIDIClient.sources[index];
											holdCurrentWidget.midiDeviceName = holdMIDIEndPoint.device;
											holdCurrentWidget.midiPortName = holdMIDIEndPoint.name;
										});
										index = index + 1;
									} );
									// stop responders
									midiNoteResponder.free;
									midiCCResponder.free;
									TXFrontScreen.midiDeActivate;
									// update view
									system.showView;
								});
								TXFrontScreen.registerMidiResponder(midiNoteResponder);
								//
								midiCCResponder = MIDIFunc.cc({  |val, num, chan, src|
									var holdMIDIEndPoint, index;
									//  set min/max channel and note no
									holdCurrentWidget.midiMinChannel = chan + 1;
									holdCurrentWidget.midiMaxChannel = chan + 1;
									holdCurrentWidget.midiNote = num;
									holdCurrentWidget.midiType = \controller;
									holdCurrentWidget.midiListen = 1;
									holdCurrentWidget.midiPortUid = src;
									holdCurrentWidget.midiDeviceName = nil;
									holdCurrentWidget.midiPortName = nil;
									index = 0;
									while ( {holdMIDIEndPoint.isNil && (index < MIDIClient.sources.size)}, {
										if (MIDIClient.sources[index].uid == src, {
											holdMIDIEndPoint = MIDIClient.sources[index];
											holdCurrentWidget.midiDeviceName = holdMIDIEndPoint.device;
											holdCurrentWidget.midiPortName = holdMIDIEndPoint.name;
										});
										index = index + 1;
									} );
									// stop responders
									midiCCResponder.free;
									midiNoteResponder.free;
									TXFrontScreen.midiDeActivate;
									// update view
									system.showView;
								});
								TXFrontScreen.registerMidiResponder(midiCCResponder);
							},{
								// stop responders
								midiNoteResponder.free;
								midiCCResponder.free;
								TXFrontScreen.midiDeActivate;
								classData.midiLearnHoldVal = nil;
							});
						};

						// key down trigger

						// spacer & go to next line
						propertiesBox.decorator.nextLine.shift(0, 6);
						// text label
						StaticText(propertiesBox, Rect(0, 0, 100, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
						.align_(\left)
						.string_("Key Trigger");

						// checkbox key Listen
						keyListenCheckbox = TXCheckBox(propertiesBox, Rect(0, 0, 40, 20),
							" ", TXColor.black, TXColor.white,
							TXColor.black, TXColor.yellow, 4);
						keyListenCheckbox.action = {arg view;
							holdCurrentWidget.keyListen = view.value;
						};
						keyListenCheckbox.value = holdCurrentWidget.keyListen ? 0;

						// text label
						StaticText(propertiesBox, Rect(0, 0, 30, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
						.align_(\left)
						.string_("Key");

						// text field - key
						keyTextField = TextField(propertiesBox, Rect(0, 0, 28, 20))
						.action_({arg view;
							if (view.string.size == 0, {view.string = "";});
							view.string = view.string.at(0);
							holdCurrentWidget.keyChar = view.string;
							// update view
							system.showView;
						});
						keyTextField.string = holdCurrentWidget.keyChar ? "";

					});

					// ==========================================================================================

					// check properties
					if ((holdCurrentWidget.class == TXWSlider)
						or: (holdCurrentWidget.class == TXWKnob)
						or: (holdCurrentWidget.class == TXWSliderV)
						or: (holdCurrentWidget.class == TXWSliderNo)
						or: (holdCurrentWidget.class == TXWSliderNoV)
						or: (holdCurrentWidget.class == TXW2DSlider)
						or: (holdCurrentWidget.class == TXW2DTablet)
						or: (holdCurrentWidget.class == TXWNumberBox)
						or: (holdCurrentWidget.class == TXWPopup)
						or: (holdCurrentWidget.class == TXWPopupPlusMinus)
						or: (holdCurrentWidget.class == TXWList)
						or: (holdCurrentWidget.class == TXWListPlusMinus)
						or: (holdCurrentWidget.class == TXWCheckBox)
						,{
							// spacer & go to next line
							propertiesBox.decorator.nextLine.shift(0, 6);
							// text label
							StaticText(propertiesBox, Rect(0, 0, 100, 20))
							.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
							.align_(\left)
							.string_("Midi listen");

							// checkbox midi Listen
							midiListenCheckbox = TXCheckBox(propertiesBox, Rect(0, 0, 35, 20),
								" ", TXColor.black, TXColor.white,
								TXColor.black, TXColor.yellow, 4);
							midiListenCheckbox.action = {arg view;
								holdCurrentWidget.midiListen = view.value;
							};
							midiListenCheckbox.value = holdCurrentWidget.midiListen ? 0;

							if (holdCurrentWidget.class == TXW2DSlider
								or: (holdCurrentWidget.class == TXW2DTablet) ,{
									// text label
									StaticText(propertiesBox, Rect(0, 0, 80, 20))
									.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
									.align_(\left)
									.string_("Controller nos");

									// numberbox - midi controller no
									midiCCNoBox = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
									.scroll_(false)
									.action_({arg view;
										view.value = view.value.max(0).min(127);
										holdCurrentWidget.midiCCNo = view.value;
										// update view
										system.showView;
									});
									midiCCNoBox.value = holdCurrentWidget.midiCCNo ? 0;
									// numberbox - midi controller no 2
									midiCCNoBox2 = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
									.scroll_(false)
									.action_({arg view;
										view.value = view.value.max(0).min(127);
										holdCurrentWidget.midiCCNo2 = view.value;
										// update view
										system.showView;
									});
									midiCCNoBox2.value = holdCurrentWidget.midiCCNo2 ? 0;
								},{
									// text label
									StaticText(propertiesBox, Rect(0, 0, 80, 20))
									.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
									.align_(\left)
									.string_("Controller no");

									// numberbox - midi controller no
									midiCCNoBox = NumberBox(propertiesBox, Rect(0, 0, 28, 20))
									.scroll_(false)
									.action_({arg view;
										view.value = view.value.max(0).min(127);
										holdCurrentWidget.midiCCNo = view.value;
										// update view
										system.showView;
									});
									midiCCNoBox.value = holdCurrentWidget.midiCCNo ? 0;
							});

							holdPortNames = ["Any Port"]
							++ MIDIClient.sources.collect({arg item, i;
								// remove any brackets from string
								(item.device.asString + item.name.asString).replace("(", "").replace(")", "")
							});

							// text label
							StaticText(propertiesBox, Rect(0, 0, 40, 20))
							.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
							.align_(\left)
							.string_("Port");
							// PopUp - midi port
							midiPortPopup = PopUpMenu(propertiesBox, Rect(0, 0, 200, 20))
							.items_(holdPortNames)
							.background_(TXColor.white)
							.action_({arg view;
								if (view.value == 0, {
									holdCurrentWidget.midiPortUid = nil;
									holdCurrentWidget.midiDeviceName = nil;
									holdCurrentWidget.midiPortName = nil;
								},{
									holdCurrentWidget.midiPortUid = MIDIClient.sources[view.value - 1].uid;
									holdCurrentWidget.midiDeviceName =  MIDIClient.sources[view.value - 1].device;
									holdCurrentWidget.midiPortName =  MIDIClient.sources[view.value - 1].name;
								});
								// update view
								system.showView;
							});
							MIDIClient.sources.do({ arg item, i;
								if (MIDIClient.sources[i].uid == holdCurrentWidget.midiPortUid, {
									midiPortPopup.value = i + 1;
								});
							});

							// text label
							StaticText(propertiesBox, Rect(0, 0, 60, 20))
							.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
							.align_(\left)
							.string_("Channels");

							// numberbox - midi channel min
							midiChannelMinPopup = PopUpMenu(propertiesBox, Rect(0, 0, 40, 20))
							.items_((1..16).collect({arg item, i; item.asString}))
							.background_(TXColor.white)
							.action_({arg view;
								holdCurrentWidget.midiMinChannel = view.value + 1;
								// update view
								system.showView;
							});
							midiChannelMinPopup.value =
							(holdCurrentWidget.midiMinChannel ? 1) - 1;
							// text label
							StaticText(propertiesBox, Rect(0, 0, 20, 20))
							.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white)
							.align_(\center)
							.string_("to");

							// numberbox - midi channel max
							midiChannelMaxPopup = PopUpMenu(propertiesBox, Rect(0, 0, 40, 20))
							.items_((1..16).collect({arg item, i; item.asString}))
							.background_(TXColor.white)
							.action_({arg view;
								holdCurrentWidget.midiMaxChannel = view.value + 1;
								// update view
								system.showView;
							});
							midiChannelMaxPopup.value =
							(holdCurrentWidget.midiMaxChannel ? 16) - 1;

							// spacer
							propertiesBox.decorator.nextLine;

							// checkbox Midi CC Learn
							midiLearnCheckbox = TXCheckBox(propertiesBox, Rect(0, 0, 90, 20),
								"MIDI Learn", TXColor.sysGuiCol1, TXColor.white,
								TXColor.white, TXColor.sysGuiCol1, 0);
							midiLearnCheckbox.action = {arg view;
								var midiCCNoResponder;
								if (view.value == 1, {
									TXFrontScreen.midiDeActivate;
									classData.midiLearnHoldVal = nil;
									// If 2D, then allow for learning 2 controllers
									if (holdCurrentWidget.class == TXW2DSlider
										or: (holdCurrentWidget.class == TXW2DTablet) ,{
											midiCCNoResponder = MIDIFunc.cc({  |val, num, chan, src|
												var holdMIDIEndPoint, index;
												if (classData.midiLearnHoldVal.isNil, {
													classData.midiLearnHoldVal = num;
												},{
													// if new number, assign CCNos
													if (num != classData.midiLearnHoldVal, {
														midiCCNoResponder.free;
														holdCurrentWidget.midiCCNo = classData.midiLearnHoldVal;
														holdCurrentWidget.midiCCNo2 = num;
														//  set min/max channel and controller no
														holdCurrentWidget.midiMinChannel = chan + 1;
														holdCurrentWidget.midiMaxChannel = chan + 1;
														holdCurrentWidget.midiListen = 1;
														holdCurrentWidget.midiPortUid = src;
														holdCurrentWidget.midiDeviceName = nil;
														holdCurrentWidget.midiPortName = nil;
														index = 0;
														while ( {holdMIDIEndPoint.isNil && (index < MIDIClient.sources.size)}, {
															if (MIDIClient.sources[index].uid == src, {
																holdMIDIEndPoint = MIDIClient.sources[index];
																holdCurrentWidget.midiDeviceName = holdMIDIEndPoint.device;
																holdCurrentWidget.midiPortName = holdMIDIEndPoint.name;
															});
															index = index + 1;
														} );
														// update view
														system.showView;
													});
												});
											});
										},{
											midiCCNoResponder = MIDIFunc.cc({  |val, num, chan, src|
												var holdMIDIEndPoint, index;
												//  set min/max channel and controller no
												holdCurrentWidget.midiMinChannel = chan + 1;
												holdCurrentWidget.midiMaxChannel = chan + 1;
												holdCurrentWidget.midiCCNo = num;
												holdCurrentWidget.midiListen = 1;
												holdCurrentWidget.midiPortUid = src;
												holdCurrentWidget.midiDeviceName = nil;
												holdCurrentWidget.midiPortName = nil;
												index = 0;
												while ( {holdMIDIEndPoint.isNil && (index < MIDIClient.sources.size)}, {
													if (MIDIClient.sources[index].uid == src, {
														holdMIDIEndPoint = MIDIClient.sources[index];
														holdCurrentWidget.midiDeviceName = holdMIDIEndPoint.device;
														holdCurrentWidget.midiPortName = holdMIDIEndPoint.name;
													});
													index = index + 1;
												} );
												// stop responder
												midiCCNoResponder.free;
												// update view
												system.showView;
											});
									});
									TXFrontScreen.registerMidiResponder(midiCCNoResponder);
								},{
									// stop responder
									midiCCNoResponder.free;
									TXFrontScreen.midiDeActivate;
									classData.midiLearnHoldVal = nil;
								});
							};
							// checkbox midi Remote Sync
							midiRemoteSyncCheckbox = TXCheckBox(propertiesBox, Rect(0, 0, 130, 20),
								"MIDI Remote Sync", TXColor.sysGuiCol2, TXColor.white,
								TXColor.white, TXColor.sysGuiCol2, 0);
							midiRemoteSyncCheckbox.action = {arg view;
								holdCurrentWidget.midiRemoteSync = view.value;
							};
							midiRemoteSyncCheckbox.value = holdCurrentWidget.midiRemoteSync ? 0;
					});

					// ==========================================================================================

					// check properties
					if (holdCurrentWidget.class == TXWNotesBox, {

						// spacer & go to next line
						propertiesBox.decorator.nextLine.shift(0, 6);

						// text label
						StaticText(propertiesBox, Rect(0, 0, 400, 20))
						.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
						.align_(\center)
						.string_("Enter notes text below, then press button to store notes:");

						// update button
						updateButton = Button(propertiesBox, Rect(0, 0, 100, 20))
						.states_([ ["Store Notes", TXColor.white, TXColor.sysEditCol] ]);
						updateButton.action = {arg view;
							holdCurrentWidget.string = notesView.string;
							// update view
							system.showView;
						};

						// go to next line
						propertiesBox.decorator.nextLine;

						// notes
						notesView = TextView(propertiesBox,Rect(0,0, 500,200))
						.hasVerticalScroller_(true)
						.background_(Color.white)
						.enterInterpretsSelection_(false)
						.string_(holdCurrentWidget.string)
						;
					});
					// ==========================================================================================

					if (holdCurrentWidget.properties.includes(\arrActions), {
						this.drawActions(system, holdActionType, propertiesBox, holdCurrentWidget);
					});  //end of if ...\arrActions
					// ==========================================================================================

					// spacer & go to next line
					propertiesBox.decorator.shift(0, 4).nextLine;

					// // line
					// StaticText(propertiesBox, Rect(0, 0, 700, 2))
					// .background_(TXColor.white);

					// spacer & go to next line
					propertiesBox.decorator.shift(0, 4).nextLine;
				});
			});
		}); // end of if (totalHighlighted >1

	} // end of class method makeGui

	*drawNewWidgets{ arg system, classData, propertiesBox;
		var arrModules, holdView, importModuleView, importValueActionView, importModule;
		var importModuleActionSpecs, importAction, importClassDict;
		if (classData.newWidgetFromModule, {
			// popup - module
			arrModules = system.arrWidgetValueActionModules;
			importModule = TXFrontScreen.system.getModuleFromID(classData.importModuleID);
			if (importModule == 0, {
				importModule = system;
			});
			importModuleView = TXListView(propertiesBox, Rect(0, 0, 150, 150))
			.background_(TXColor.white).stringColor_(TXColor.black)
			.items_(arrModules.collect({arg item, i; item.instDisplayName;}))
			.action_({arg view;
				classData.importModuleID = arrModules.at(view.value).moduleID;
				classData.importValueActionInd = 0; // reset index
				// update view
				system.showView;
			});
			importModuleView.value = arrModules.indexOf(importModule) ? 0;

			importModuleActionSpecs = importModule.arrActionSpecs
			.select({arg action, i;
				(action.actionType == \valueAction);
			});
			importAction = importModuleActionSpecs[classData.importValueActionInd];

			// popup - action
			importValueActionView = TXListView(propertiesBox, Rect(0, 0, 300, 150))
			.background_(TXColor.white).stringColor_(TXColor.black)
			.items_(importModuleActionSpecs.collect({arg item, i; item.actionName;}))
			.action_({arg view;
				classData.importValueActionInd = view.value;
				// update view
				system.showView;
			});
			importValueActionView.value = classData.importValueActionInd ? 0;

			propertiesBox.decorator.nextLine.shift(0,4);

			// ignore dummy actions
			if (importAction.actionName != "...", {
				// text label
				StaticText(propertiesBox, Rect(0, 0, 100, 20))
				.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow)
				.align_(\left)
				.string_("drag widget to add");

				// label box
				holdView = DragSource(propertiesBox, Rect(0, 0, 100, 20)).align_(\center);
				holdView.string = TXWLabelBox.widgetName;
				holdView.stringColor_(TXColor.paleYellow).background_(TXColor.sysGuiCol1);
				holdView.dragLabel = "Add Widget: " ++ TXWLabelBox.widgetName;
				holdView.beginDragAction_({
					var newWidget = TXWLabelBox.new(0, 0, TXWLabelBox.defaultHeight, TXWLabelBox.defaultWidth);
					newWidget.string_(this.removeGetSetString(importAction.actionName));
					newWidget;
				});
				// build  dict for different widget types
				importClassDict = ();
				importClassDict.number = [[
					TXWKnob,
					TXWNumberBox,
					TXWSlider,
					TXWSliderV,
					TXWSliderNo,
					TXWSliderNoV,
				], {arg newWidget;
					var holdAction = newWidget.arrActions[0];
					var holdArrActionNames = importModule.arrActionSpecs
					.select({arg action, i;
						(action.actionType == \valueAction)
						and: (action.guiObjectType == \number);
					})
					.collect({arg item, i; item.actionName;});
					// initialise
					newWidget.arrActions[0][0] = classData.importModuleID;
					//newWidget.arrActions[0][1] = classData.importValueActionInd;
					newWidget.arrActions[0][1] = holdArrActionNames.indexOfEqual(importAction.actionName);
					newWidget.arrActions[0][7] = importAction.actionName;
					// default argument values are stored
					// arg 1
					if (importAction.arrControlSpecFuncs.size > 0, {
						newWidget.arrActions[0][2] = importAction.arrControlSpecFuncs.at(0).value.default;
					});
					// arg 2
					if (importAction.arrControlSpecFuncs.size > 1, {
						newWidget.arrActions[0][3] = importAction.arrControlSpecFuncs.at(1).value.default;
					});
					// arg 3
					if (importAction.arrControlSpecFuncs.size > 2, {
						newWidget.arrActions[0][4] = importAction.arrControlSpecFuncs.at(2).value.default;
					});
				}];
				importClassDict.popup = [[
					TXWPopup,
					TXWPopupPlusMinus,
					TXWList,
					TXWListPlusMinus,
				], {arg newWidget;
					var holdAction = newWidget.arrActions[0];
					var holdArrActionNames = importModule.arrActionSpecs
					.select({arg action, i;
						(action.actionType == \valueAction)
						and: (action.guiObjectType == \popup);
					})
					.collect({arg item, i; item.actionName;});
					// initialise
					newWidget.arrActions[0][0] = classData.importModuleID;
					newWidget.arrActions[0][1] = holdArrActionNames.indexOfEqual(importAction.actionName);
					newWidget.arrActions[0][7] = importAction.actionName;
				}];
				importClassDict.checkbox = [[
					TXWCheckBox,
				], {arg newWidget;
					var holdAction = newWidget.arrActions[0];
					var holdArrActionNames = importModule.arrActionSpecs
					.select({arg action, i;
						(action.actionType == \valueAction)
						and: (action.guiObjectType == \checkbox);
					})
					.collect({arg item, i; item.actionName;});
					// initialise
					newWidget.arrActions[0][0] = classData.importModuleID;
					newWidget.arrActions[0][1] = holdArrActionNames.indexOfEqual(importAction.actionName);
					newWidget.arrActions[0][7] = importAction.actionName;
					newWidget.string_(this.removeGetSetString(importAction.actionName));
				}];
				importClassDict.text = [[
					TXWTextDisplayBox,
				], {arg newWidget;
					var holdAction = newWidget.arrActions[0];
					var holdArrActionNames = importModule.arrActionSpecs
					.select({arg action, i;
						(action.actionType == \valueAction)
						and: (action.guiObjectType == \text);
					})
					.collect({arg item, i; item.actionName;});
					// initialise
					newWidget.arrActions[0][0] = classData.importModuleID;
					newWidget.arrActions[0][1] = holdArrActionNames.indexOfEqual(importAction.actionName);
					newWidget.arrActions[0][7] = importAction.actionName;
				}];
				importClassDict.textedit = [[
					TXWTextEditBox,
				], {arg newWidget;
					var holdAction = newWidget.arrActions[0];
					var holdArrActionNames = importModule.arrActionSpecs
					.select({arg action, i;
						(action.actionType == \valueAction)
						and: (action.guiObjectType == \textedit);
					})
					.collect({arg item, i; item.actionName;});
					// initialise
					newWidget.arrActions[0][0] = classData.importModuleID;
					newWidget.arrActions[0][1] = holdArrActionNames.indexOfEqual(importAction.actionName);
					newWidget.arrActions[0][7] = importAction.actionName;
				}];
				importClassDict.ipaddress = [[
					TXWIPAddress,
				], {arg newWidget;
					var holdAction = newWidget.arrActions[0];
					var holdArrActionNames = importModule.arrActionSpecs
					.select({arg action, i;
						(action.actionType == \valueAction)
						and: (action.guiObjectType == \ipaddress);
					})
					.collect({arg item, i; item.actionName;});
					// initialise
					newWidget.arrActions[0][0] = classData.importModuleID;
					newWidget.arrActions[0][1] = holdArrActionNames.indexOfEqual(importAction.actionName);
					newWidget.arrActions[0][7] = importAction.actionName;
				}];
				// widgets
				if (importClassDict[importAction.guiObjectType].notNil, {
					importClassDict[importAction.guiObjectType][0].do({arg item, i;
						if (i == 3, {
							propertiesBox.decorator.nextLine;
						});
						holdView = DragSource(propertiesBox, Rect(0, 0, 100, 20)).align_(\center);
						//holdView.object = { Synth(\default) };
						holdView.string = item.widgetName;
						holdView.stringColor_(TXColor.paleYellow).background_(TXColor.sysGuiCol1);
						holdView.dragLabel = "Add Widget: " ++ item.widgetName;
						holdView.beginDragAction_({
							var newWidget = item.new(0, 0, item.defaultHeight, item.defaultWidth);
							// run widget init action
							importClassDict[importAction.guiObjectType][1].value(newWidget);
							newWidget; // return new widget as dragged object
						});
					});
				});
			});
		},{
			// text label
			StaticText(propertiesBox, Rect(0, 0, 100, 20))
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleYellow)
			.align_(\left)
			.string_("drag widget to add");

			// show all possible widgets
			classData.arrWidgetClasses.do({arg item, i;
				if ((i + 1) % 5 == 0, {
					propertiesBox.decorator.nextLine;
				});
				holdView = DragSource(propertiesBox, Rect(0, 0, 100, 20)).align_(\center);
				//holdView.object = { Synth(\default) };
				holdView.string = item.widgetName;
				holdView.stringColor_(TXColor.paleYellow).background_(TXColor.sysGuiCol1);
				holdView.dragLabel = "Add Widget: " ++ item.widgetName;
				holdView.beginDragAction_({item.new(0, 0, item.defaultHeight, item.defaultWidth);});
			});
		});
	} // end of class method makeGui

	*drawActions{ arg system, holdActionType, propertiesBox, holdCurrentWidget;
		var actionCount, selectActionsButton, holdView, holdActionText;
		// spacer & go to next line
		propertiesBox.decorator.nextLine.shift(0, 6);

		// text label
		StaticText(propertiesBox, Rect(0, 0, 100, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
		.align_(\left)
		.string_("Widget Actions:");

		// spacer
		propertiesBox.decorator.shift(10, 0);

		if (holdCurrentWidget.class == TXW2DSlider, {
			// x-y axis button
			selectActionsButton = PopUpMenu(propertiesBox, Rect(0, 0, 140, 20))
			.stringColor_(TXColor.white).background_(TXColor.sysGuiCol1)
			.items_(["x-axis", "y-axis",
			]);
			selectActionsButton.action = {arg view;
				holdCurrentWidget.showYAxis = view.value;
				// update view
				system.showView;
			};
			selectActionsButton.value = holdCurrentWidget.showYAxis ? 0;
			// + - buttons
			Button(propertiesBox, Rect(0, 0, 16, 20))
			.states_([["+", TXColor.white, TXColor.sysGuiCol1]])
			.action_({
				if (selectActionsButton.value == 0, {selectActionsButton.valueAction = 1});
			});
			Button(propertiesBox, Rect(0, 0, 16, 20))
			.states_([["-", TXColor.white, TXColor.sysGuiCol1]])
			.action_({
				if (selectActionsButton.value == 1, {selectActionsButton.valueAction = 0});
			});
		},{
			if (holdCurrentWidget.class == TXW2DTablet, {
				// x-y axis button
				selectActionsButton = PopUpMenu(propertiesBox, Rect(0, 0, 140, 20))
				.stringColor_(TXColor.white).background_(TXColor.sysGuiCol1)
				.items_(["x-axis", "y-axis", "pressure", "tilt x", "tilt y", "mouse-down",
					"mouse-drag", "mouse-up", "double-click",
				]);
				selectActionsButton.action = {arg view;
					holdCurrentWidget.showActionIndex =
					view.value;
					// update view
					system.showView;
				};
				selectActionsButton.value = holdCurrentWidget.showActionIndex ? 0;
			});
		});

		// next line
		propertiesBox.decorator.nextLine;

		// text label
		StaticText(propertiesBox, Rect(0, 0, 150, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
		.align_(\center)
		.string_("Module");
		// text label
		holdView = StaticText(propertiesBox, Rect(0, 0, 300, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
		.align_(\center);
		if (holdActionType == "commandAction", {
			holdView.string_("Action");
		},{
			holdView.string_("Parameter to update");
		});

		// text label
		holdView = StaticText(propertiesBox, Rect(0, 0, 250, 20))
		.stringColor_(TXColor.sysGuiCol1).background_(TXColor.paleBlue2)
		.align_(\center);
		if (holdActionType == "commandAction", {
			holdView.string_("Value settings");
		},{
			holdView.string_("Extra parameter settings");
		});

		// up to 10 actions can be defined
		if (holdCurrentWidget.class == TXWTextDisplayBox, {
			actionCount = 1;
		},{
			if (holdCurrentWidget.class == TXWActionButton, {
				actionCount = 10;
			},{
				actionCount = 5;
			});
		});
		actionCount.do({ arg item, i;
			var arrModules, modulePopup, arrActionItems, arrLegacyActionItems, actionPopup;
			var holdModuleID, holdModule;
			var holdControlSpec1, holdControlSpec2, holdControlSpec3, holdControlSpec4, holdArrActionSpecs;
			var val1NumberBox, val1Slider, val2NumberBox, val3NumberBox, val4NumberBox, valPopup;
			var valCheckbox, valTextbox, valipaddress, holdArrActions;

			if (holdCurrentWidget.class == TXWActionButton, {
				holdArrActions = holdCurrentWidget.arrActions
				++ holdCurrentWidget.arrActions2;
			},{
				holdArrActions = holdCurrentWidget.arrActions;
			});

			if (holdCurrentWidget.class == TXW2DSlider, {
				if (holdCurrentWidget.showYAxis == 1, {
					holdArrActions = holdCurrentWidget.arrActions2;
				});
			});
			if (holdCurrentWidget.class == TXW2DTablet, {
				if (holdCurrentWidget.showActionIndex == 1, {
					holdArrActions =
					holdCurrentWidget.arrActions2;
				});
				if (holdCurrentWidget.showActionIndex == 2, {
					holdArrActions =
					holdCurrentWidget.pressureActions;
				});
				if (holdCurrentWidget.showActionIndex == 3, {
					holdArrActions =
					holdCurrentWidget.tiltXActions;
				});
				if (holdCurrentWidget.showActionIndex == 4, {
					holdArrActions =
					holdCurrentWidget.tiltYActions;
				});
				if (holdCurrentWidget.showActionIndex == 5, {
					holdArrActions =
					holdCurrentWidget.mouseDownActions;
				});
				if (holdCurrentWidget.showActionIndex == 6, {
					holdArrActions =
					holdCurrentWidget.mouseDragActions;
				});
				if (holdCurrentWidget.showActionIndex == 7, {
					holdArrActions =
					holdCurrentWidget.mouseUpActions;
				});
				if (holdCurrentWidget.showActionIndex == 8, {
					holdArrActions =
					holdCurrentWidget.mouseDoubleClickActions;
				});
			});

			// go to next line
			propertiesBox.decorator.nextLine;

			// update variables
			if (holdActionType == "commandAction", {
				arrModules = system.arrWidgetActionModules;
			},{
				arrModules = system.arrWidgetValueActionModules;
			});
			holdModuleID = holdArrActions.at(i).at(0);
			holdModule = system.getModuleFromID(holdModuleID);
			if (holdModule == 0, {holdModule = system});
			if (holdActionType == "commandAction", {
				holdArrActionSpecs = holdModule.arrActionSpecs;
				arrActionItems = holdArrActionSpecs
				.collect({arg item, i; item.actionName;});
				arrLegacyActionItems = holdArrActionSpecs .select({arg item, i; item.legacyType == 1})
				.collect({arg item, i; item.actionName;});

			},{
				holdArrActionSpecs = holdModule.arrActionSpecs
				.select({arg action, i;
					(action.actionType == \valueAction)
					and: (action.guiObjectType ==
						holdCurrentWidget.guiObjectType);});
				arrActionItems =
				holdArrActionSpecs.collect({arg item, i; item.actionName;});
				arrLegacyActionItems =
				holdModule.arrActionSpecs .select({arg item, i; item.legacyType == 1})
				.select({arg action, i;
					(action.actionType == \valueAction) and:
					(action.guiObjectType ==
						holdCurrentWidget.guiObjectType);
				})
				.collect({arg item, i; item.actionName;});
			});

			// popup - module
			modulePopup = TXListView(propertiesBox, Rect(0, 0, 150, 150))
			.background_(TXColor.white).stringColor_(TXColor.black)
			.items_(arrModules.collect({arg item, i; item.instDisplayName;}))
			.action_({arg view;
				var holdAction;
				holdArrActions.at(i).put(0, arrModules.at(view.value).moduleID);
				// reset action index and text
				holdArrActions.at(i).put(1, 0);
				if (holdArrActions.at(i).size < 8, {
					holdAction = holdArrActions.at(i).deepCopy;
					holdAction = holdAction.addAll([nil, nil, nil, nil, nil, nil]);
					holdArrActions.put(i, holdAction.deepCopy);
				},{
					holdArrActions.at(i).put(7, arrActionItems.at(0));
				});
				// update view
				system.showView;
			});
			modulePopup.value = arrModules.indexOf(holdModule) ? 0;

			// popup - action
			actionPopup = TXListView(propertiesBox, Rect(0, 0, 300, 150))
			.background_(TXColor.white).stringColor_(TXColor.black)
			.items_(arrActionItems)
			.action_({arg view;
				var holdAction;
				// popup value and text are stored
				holdArrActions.at(i).put(1, view.value);
				if (holdArrActions.at(i).size<8, {
					holdAction = holdArrActions.at(i).deepCopy;
					holdAction = holdAction.addAll([nil, nil, nil, nil, nil, nil]);
					holdArrActions.put(i, holdAction.deepCopy);
				});
				holdArrActions.at(i).put(7, arrActionItems.at(view.value));
				// default argument values are stored
				// arg 1
				if (holdModule.arrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.size > 0, {
					holdArrActions.at(i).put(2,
						holdModule.arrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.at(0).value.default);
				});
				// arg 2
				if (holdModule.arrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.size > 1, {
					holdArrActions.at(i).put(3,
						holdModule.arrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.at(1).value.default);
				});
				// arg 3
				if (holdModule.arrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.size > 2, {
					holdArrActions.at(i).put(4,
						holdModule.arrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.at(2).value.default);
				});
				// update view
				system.showView;
			});
			// if text found, match action string with text, else use numerical value
			if (holdArrActions.at(i).at(7).notNil, {
				actionPopup.value = arrActionItems.indexOfEqual(holdArrActions.at(i).at(7)) ? 0;
			},{
				holdActionText = arrLegacyActionItems.at(holdArrActions.at(i).at(1) ? 0);
				actionPopup.value = arrActionItems.indexOfEqual(holdActionText) ? 0;
			});

			// check for no actions
			if (holdArrActionSpecs.size > 0, {
				// action widget, show value settings
				if (holdCurrentWidget.class == TXWActionButton
					or: (holdCurrentWidget.class == TXW2DTablet
						and: {holdCurrentWidget.showActionIndex > 4}),
					{
						// if only 1 controlspec is given, then create slider
						if (holdModule.arrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.size == 1, {
							// slider - value 1
							holdControlSpec1 =
							holdModule.arrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.at(0);
							val1Slider = Slider(propertiesBox, Rect(0, 0, 175, 20))
							.action_({arg view;
								holdArrActions.at(i)
								.put(2, holdControlSpec1.value.map(view.value));
								if (val1NumberBox.class == NumberBox,
									{val1NumberBox.value = holdControlSpec1.value.map(view.value);})
							});
							if (holdControlSpec1.value.step != 0, {
								val1Slider.step = (holdControlSpec1.value.step
									/ (holdControlSpec1.value.maxval - holdControlSpec1.value.minval));
							});
							val1Slider.value = holdControlSpec1.value.unmap(
								holdArrActions.at(i).at(2) ? 0);
						});
						// if object type is number
						if (holdModule.arrActionSpecs.at(actionPopup.value).guiObjectType == \number, {
							// if at least 1 controlspec is given, then create numberbox
							if (holdModule.arrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.size > 0, {
								holdControlSpec1 =
								holdModule.arrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.at(0);
								val1NumberBox = NumberBox(propertiesBox, Rect(0, 0, 60, 20))
								.scroll_(false)
								.maxDecimals_(4)
								.action_({arg view;
									view.value = holdControlSpec1.value.constrain(view.value);
									holdArrActions.at(i).put(2, view.value);
									if (val1Slider.class == Slider,
										{val1Slider.value = holdControlSpec1.value.unmap(view.value);})
								});
								val1NumberBox.value = holdControlSpec1.value.constrain(
									holdArrActions.at(i).at(2) ? holdControlSpec1.value.default);
							});
						});
						// popup
						if (holdModule.arrActionSpecs.at(actionPopup.value).guiObjectType == \popup, {
							//valPopup = PopUpMenu(propertiesBox, Rect(0, 0, 250, 20))
							valPopup = TXListView(propertiesBox, Rect(0, 0, 250, 150))
							.stringColor_(TXColor.black).background_(TXColor.white);
							valPopup.items =
							holdModule.arrActionSpecs.at(actionPopup.value).getItemsFunction.value;
							valPopup.action = {arg view;
								holdArrActions.at(i).put(2, view.value);
							};
							valPopup.value = holdArrActions.at(i).at(2) ? 0;
						});

						// checkbox
						if (holdModule.arrActionSpecs.at(actionPopup.value).guiObjectType == \checkbox, {
							valCheckbox = TXCheckBox(propertiesBox, Rect(0, 0, 60, 20),
								" ", TXColor.black, TXColor.white,
								TXColor.black, TXColor.white, 7);
							valCheckbox.action = {arg view;
								holdArrActions.at(i).put(2, view.value);
							};
							valCheckbox.value = holdArrActions.at(i).at(2) ? 0;
						});

						// textbox
						if (holdModule.arrActionSpecs.at(actionPopup.value).guiObjectType == \textedit, {
							valTextbox = TextField(propertiesBox, Rect(0, 0, 250, 20),
								" ", TXColor.black, TXColor.white,
								TXColor.black, TXColor.white, 4);
							valTextbox.action = {arg view;
								holdArrActions.at(i).put(2, view.string);
							};
							valTextbox.string = holdArrActions.at(i).at(2) ? " ";
						});

						// ipaddress
						if (holdModule.arrActionSpecs.at(actionPopup.value).guiObjectType == \ipaddress, {
							valipaddress = TXNetAddress(propertiesBox, Rect(0, 0, 250, 20),
								labelWidth: 0, showPresets: false);
							valipaddress.action = {arg view;
								holdArrActions.at(i).put(2, view.string);
							};
							valipaddress.string = holdArrActions.at(i).at(2) ? "0.0.0.0";
						});
				}); // end of if ...TXWActionButton
				// if more than 1 control spec given, then create extra numberbox
				if (holdArrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.size > 1, {
					// numberbox - value 2
					holdControlSpec2 = holdArrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.at(1);
					val2NumberBox = NumberBox(propertiesBox, Rect(0, 0, 60, 20))
					.scroll_(false)
					.action_({arg view;
						view.value = holdControlSpec2.value.constrain(view.value);
						holdArrActions.at(i).put(3, view.value);
					});
					if (holdArrActions.at(i).at(3).notNil, {
						val2NumberBox.value = holdControlSpec2.value.constrain(
							holdArrActions.at(i).at(3));
						holdArrActions.at(i).put(3, val2NumberBox.value);
					},{
						val2NumberBox.value = holdControlSpec2.default;
						holdArrActions.at(i).put(3, holdControlSpec2.default);
					});
				});
				// numberbox - value 3
				// if more than 2 controlspecs given, then create extra numberbox
				if (holdArrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.size > 2, {
					holdControlSpec3 =
					holdArrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.at(2);
					val3NumberBox = NumberBox(propertiesBox, Rect(0, 0, 60, 20))
					.scroll_(false)
					.action_({arg view;
						view.value = holdControlSpec3.value.constrain(view.value);
						holdArrActions.at(i).put(4, view.value);
					});
					if (holdArrActions.at(i).at(4).notNil, {
						val3NumberBox.value = holdControlSpec3.value.constrain(
							holdArrActions.at(i).at(4));
						holdArrActions.at(i).put(4, val3NumberBox.value);
					},{
						val3NumberBox.value = holdControlSpec3.default;
						holdArrActions.at(i).put(4, holdControlSpec3.default);
					});
				});
				// numberbox - value 4
				// if more than 3 controlspecs given, then create extra numberbox
				if (holdArrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.size > 3, {
					holdControlSpec4 =
					holdArrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.at(3);
					val4NumberBox = NumberBox(propertiesBox, Rect(0, 0, 60, 20))
					.scroll_(false)
					.action_({arg view;
						view.value = holdControlSpec4.value.constrain(view.value);
						holdArrActions.at(i).put(5, view.value);
					});
					if (holdArrActions.at(i).at(5).notNil, {
						val4NumberBox.value = holdControlSpec4.value.constrain(
							holdArrActions.at(i).at(5));
						holdArrActions.at(i).put(5, val4NumberBox.value);
					},{
						val4NumberBox.value = holdControlSpec4.default;
						holdArrActions.at(i).put(5, holdControlSpec4.default);
					});
				});
			}); // end of if holdArrActionSpecs.size > 0
		}); // end of actionCount.do

	} // end of class method drawActions

	*removeGetSetString {arg inputString;
		var outString = inputString.copy;
		if ((outString.keep(3) == "Get") or: (outString.keep(3) == "Set"), {
			outString = outString.keep(4 - inputString.size);
		});
		^outString;
	}

}	// end of class
