// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFrontScreen {

// Also see: TXFrontScreenGui

classvar	<>system;	    			// system
classvar <>arrWidgets;
classvar <>currWidgetInd;
classvar <>arrLayers;
classvar arrKeyDownFunctions;
classvar arrMidiRoutines;
classvar arrMidiResponders;
classvar <classData;

*initClass{
	// initialise class variables
	classData = ();
	classData.arrWidgetClasses = [          // <--------- list of  widget classes
		TXWActionButton,
		TXWCheckBox,
		TXWIPAddress,
		TXWKnob,
		TXWLabelBox,
		TXWNotesBox,
		TXWNumberBox,
		TXWPopup,
		TXWSlider,
		TXWSliderV,
		TXWSliderNo,
		TXWSliderNoV,
		TXW2DSlider,
		// TXW2DTablet,
		TXWTextDisplayBox,
		TXWTextEditBox,
	];
	// initialise
	classData.holdWidgetWidth = 100;
	classData.holdWidgetHeight = 20;
	classData.imageFileNames = ();
	classData.holdImages = ();
	classData.displayModeIndices = 1 ! 20;
	classData.layerNo = 0;
	classData.layerName = " ";
	classData.clipboard1 = 0;
	classData.clipboard2 = 0;
	classData.clipboard3 = 0;
	classData.clipboard4 = "(text)";
	classData.showGuiProperties = true;
	classData.arrSizes = ["presets", 1, 2, 3, 4, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90,
		100, 110, 120, 130, 140, 150, 160, 170, 180, 190,
		200, 210, 220, 230, 240, 250, 260, 270, 280, 290,
		300, 320, 340, 360, 380, 400, 420, 440, 460, 480, 500];
	classData.arrScreenSizes = ["presets",  480, 500, 550, 600, 640, 700, 720, 768, 800, 900, 1000, 1024,
		1100, 1152, 1200, 1280, 1300, 1400, 1440, 1500, 1600, 1700, 1800, 1900, 2000 ];


	classData.currAddWidgetInd = 0;
	currWidgetInd = 0;
	this.initArrLayers;
	this.initArrWidgets;
	arrMidiRoutines = [];
	arrMidiResponders = [];
	classData.screenWidths = 1000 ! 20;
	classData.screenHeights = 550 ! 20;
	classData.gridSize = 10;
	classData.lockWidgets = 1;
	classData.screenColour = TXColor.sysInterface.copy;
	classData.arrFonts = ["Arial", "Arial-Black", "AmericanTypewriter", "AndaleMono", "Baskerville", "BigCaslon",
		 "BrushScriptMT", "Cochin", "ComicSansMS", "Copperplate",  "Courier", "CourierNewPS", "Didot",
		 "Futura", "Georgia", "GillSans", "Helvetica", "HelveticaNeue", "Herculanum", "Impact",
		 "MarkerFelt-Thin", "MarkerFelt-Wide", "Monaco", "Optima", "Palatino", "Papyrus", "Symbol",
		 "TechnoRegular", "Times", "TimesNewRomanPS", "TrebuchetMS","Verdana", "VT100", "VT100-Bold",
		 "Webdings", "ZapfDingbatsITC", "Zapfino"
	];
	arrKeyDownFunctions = [];
} // end of class method initClass

*initArrWidgets{
	TXWidget.resetWidgetID;
	// default is 1 textbox
	arrWidgets = [TXWLabelBox.new(0.05, 0.95, 40, 250, ["Screen 1", nil, nil, 16])];
}

*initArrLayers{
	var holdArrWidgets, holdScreenColour, holdLayer;
	arrLayers = [];
	20.do({ arg item, i;
		TXWidget.resetWidgetID;
		holdScreenColour = TXColor.sysInterface.copy;
		// default is 1 textbox
		holdArrWidgets = [TXWLabelBox.new(0.05, 0.95, 40, 250,
			["Screen " ++ (i+1).asString, nil, nil, 16])];
		holdLayer = [
			holdArrWidgets.collect({arg item, i; [item.class.asSymbol, item.getPropertyList]; }),
			holdScreenColour.asArray,
			" ",
			TXWidget.holdNextWidgetID
		];
		arrLayers = arrLayers.add(holdLayer);
	});
}

*saveData{
	^[
		TXWidget.holdNextWidgetID,
		arrWidgets.collect({arg item, i; [item.class.asSymbol, item.getPropertyList]; }),
		classData.screenColour.asArray,
		arrLayers,
		classData.layerNo,
		classData.layerName,
		classData.gridSize,
		classData.screenWidths,
		classData.screenHeights,
		classData.imageFileNames.getPairs,
		classData.displayModeIndices,
	];
}

*templateSaveData{
	^[
		"TXScreenTemplateSaveData",
		[
			TXWidget.holdNextWidgetID,
			arrWidgets.collect({arg item, i; [item.class.asSymbol, item.getTemplatePropertyList]; }),
			classData.screenColour.asArray,
			nil,
			nil,
			classData.layerName,
			classData.gridSize,
			classData.screenWidths[classData.layerNo],
			classData.screenHeights[classData.layerNo],
			nil,
			nil,
		]
	];
}

*loadData{ arg arrData;
	if (arrData.isNil, {^0});
//	arrData = arrData.deepCopy;
	// create widgets
	arrWidgets = arrData.at(1).collect({arg item, i; item.at(0).asClass.new; });
	// load data
	arrWidgets.do({arg item, i; item.setPropertyList(arrData.at(1).at(i).at(1)); });
	// set data
	TXWidget.holdNextWidgetID = arrData.at(0);
	if (arrData.at(2).notNil, {classData.screenColour = Color.fromArray(arrData.at(2));});
	if (arrData.at(4).notNil, {classData.layerNo = arrData.at(4).deepCopy; });
	if (classData.layerNo.isNil or: classData.layerNo.isInteger.not, {classData.layerNo = 0;});
	if (arrData.at(3).notNil, {
		arrLayers = arrData.at(3).deepCopy;
	},{
		this.storeCurrentLayer;   // if nil, store current layer from arrWidgets
	});
	if (arrData.at(5).notNil,
		{classData.layerName = arrData.at(5).copy; },
		{classData.layerName = " ";});
	if (arrData.at(6).notNil,
		{classData.gridSize = arrData.at(6).copy; },
		{classData.gridSize = 10;});
	if (arrData.at(7).notNil, {
		if (arrData.at(7).isArray, {
			classData.screenWidths = arrData.at(7).copy;
		}, {
			classData.screenWidths = arrData.at(7).copy ! 20;
		});
	}, {
		classData.screenWidths = 1000 ! 20;
	});
	if (arrData.at(8).notNil, {
		if (arrData.at(8).isArray, {
			classData.screenHeights = arrData.at(8).copy;
		}, {
			classData.screenHeights = arrData.at(8).copy ! 20;
		});
	}, {
		classData.screenHeights = 550 ! 20;
	});
	if (arrData.at(9).notNil,
		{classData.imageFileNames = (); classData.imageFileNames.putPairs(arrData.at(9).deepCopy); },
		{classData.imageFileNames = ();}
	);
	if (arrData.at(10).notNil, {classData.displayModeIndices = arrData.at(10).deepCopy; },
		{classData.displayModeIndices = 1 ! 20});

	^arrWidgets.size;
}

*templateLoadData { arg arrData;
	// error check
	if (arrData.class != Array, {
		TXInfoScreen.new("Error: invalid data. cannot load.");
		^0;
	});
	if (arrData.at(0) != "TXScreenTemplateSaveData", {
		TXInfoScreen.new("Error: File is not a Screen Template. Cannot load.");
		^0;
	});
	this.loadData(arrData.at(1));
}
*storeCurrentLayer {
	var holdLayer;
	holdLayer = [
		arrWidgets.collect({arg item, i; [item.class.asSymbol, item.getPropertyList]; }),
		classData.screenColour.asArray,
		classData.layerName,
		TXWidget.holdNextWidgetID
	];
	// store layer
	arrLayers.put(classData.layerNo, holdLayer.deepCopy);
}

*loadLayer { arg argLayerNo;
	var holdLayer;
	// deactivate any midi and keydown functions
	this.midiDeActivate;
	this.keyDownDeActivate;
	// load new layer
	classData.layerNo = argLayerNo;
	holdLayer = arrLayers.at(argLayerNo).deepCopy;
	// create widgets
	arrWidgets = holdLayer.at(0).collect({arg item, i; item.at(0).asClass.new; });
	// load data
	arrWidgets.do({arg item, i; item.setPropertyList(holdLayer.at(0).at(i).at(1)); });
	classData.screenColour = Color.fromArray(holdLayer.at(1));
	classData.layerName = holdLayer.at(2).copy;
	TXWidget.holdNextWidgetID = holdLayer.at(3).copy;
}

*getLayerName { arg argLayerNo;
	var holdLayer, layerName;
	holdLayer = arrLayers.at(argLayerNo);
	layerName = holdLayer.at(2).copy
	^layerName;
}

*storeCurrLoadNewLayer { arg newLayerNo;
	this.storeCurrentLayer;
	classData.layerNo = newLayerNo;
	this.loadLayer(classData.layerNo);
	// update variable
	currWidgetInd = 0;
}
*storeCurrLoadNextLayer {
	var newLayerNo;
	newLayerNo = (classData.layerNo + 1);
	if (newLayerNo < arrLayers.size, {this.storeCurrLoadNewLayer(newLayerNo);});
}
*storeCurrLoadPrevLayer {
	var newLayerNo;
	newLayerNo = (classData.layerNo - 1);
	if (newLayerNo > -1,  {this.storeCurrLoadNewLayer(newLayerNo);});
}

*overwriteCurrFromLayer { arg newLayerNo;
	var holdLayerNo;
	holdLayerNo = classData.layerNo;
	this.loadLayer(newLayerNo);
	classData.layerNo = holdLayerNo;
	this.storeCurrentLayer;
	// update variable
	currWidgetInd = 0;
}

*copyDisplayProperties {
	^#[\height, \width, \backgroundAsArgs,
	\string, \stringColorAsArgs, \font, \fontSize,
	\knobColourAsArgs, \thumbSize, \colourReverse
	];
}

*copyAllProperties {
	^this.copyDisplayProperties ++
	#[\arrActions,
	\midiListen, \midiNote, \midiCCNo, \midiMinChannel, \midiMaxChannel,
	\keyListen, \keyChar, \arrActions2, \midiCCNo2,
	];
}

*checkDeletions{
	arrWidgets.do({ arg item, i;
		var holdModuleID, holdModule;
		if (item.properties.includes(\arrActions), {
			item.arrActions.do({ arg action, i;
				holdModuleID = action.at(0);
				holdModule = system.getModuleFromID(holdModuleID);
				if ((holdModule != 0) and: (holdModuleID != 99), {
					// if module is being deleted, reset widget action
					if ((holdModule.toBeDeletedStatus == true) or:(holdModule.deletedStatus == true), {
						action.put(0, 99);
						action.put(1, 0);
						if (action.size<8, {action = action.addAll([nil, nil, nil, nil, nil, nil])
						},{
							action.put(7, nil);
						});
					});
				});
			});
		});
		if (item.properties.includes(\arrActions2), {
			item.arrActions2.do({ arg action, i;
				holdModuleID = action.at(0);
				holdModule = system.getModuleFromID(holdModuleID);
				if ((holdModule != 0) and: (holdModuleID != 99), {
					// if module is being deleted, reset widget action
					if ((holdModule.toBeDeletedStatus == true) or:(holdModule.deletedStatus == true), {
						action.put(0, 99);
						action.put(1, 0);
						if (action.size<8, {action = action.addAll([nil, nil, nil, nil, nil, nil])
						},{
							action.put(7, nil);
						});
					});
				});
			});
		});
	});
}

*registerMidiRoutine {arg argRoutine;
	// add to array
	arrMidiRoutines = arrMidiRoutines.add(argRoutine);
}

*registerMidiResponder {arg argResponder;
	// add to array
	arrMidiResponders = arrMidiResponders.add(argResponder);
}

*midiDeActivate{
	// stop all midi routines
	arrMidiRoutines.do({ arg item, i;
		item.stop;
	});
	arrMidiRoutines = [];
	// stop all midi Responders
	arrMidiResponders.do({ arg item, i;
		item.remove;
	});
	arrMidiResponders = [];
}

*addKeyDownActionFunction { arg argFunction;
	// add function to array
	arrKeyDownFunctions = arrKeyDownFunctions.add(argFunction);
}

*keyDownDeActivate {
	// remove all functions
	arrKeyDownFunctions = [];
}

*runKeyDownActionFunctions { arg char, modifiers, unicode, keycode;
	// run all functions
	arrKeyDownFunctions.do({arg item, i; item.value(char, modifiers, unicode, keycode)});
}

*fitAllWidgetsToGrid{
	// set all widgets
	arrWidgets.do({ arg item, i;
		item.fitToGrid(classData.gridSize, classData.screenWidths[classData.layerNo],
			classData.screenHeights[classData.layerNo]);
	});
}

*loadScreenTemplate {
	var newPath, newFile, newData;
	CocoaDialog.getPaths({ arg paths;
		newPath = paths.at(0);
		newFile = File(newPath,"r");
		newData = thisProcess.interpreter.executeFile(newPath);
		newFile.close;
		this.templateLoadData(newData);
		// update view
		system.showView;
	});
}

*saveScreenTemplate {
	var newPath, newFile, newData;
	CocoaDialog.savePanel({ arg path;
		newPath = path;
		newData = this.templateSaveData;
		newFile = File(newPath,"w");
		newFile << "#" <<< newData << "\n";
		//	use file as an io stream
		//	<<< means store the compile string of the object
		//	<< means store a print string of the object
		newFile.close;
	});
}

*deleteWidgetAtInd { arg currWidgetInd;
	arrWidgets.removeAt(currWidgetInd);
	if (arrWidgets.size == 0, {
		this.initArrWidgets;
	});
}

*deleteHighlitWidgets {
	var holdInds = [];
	arrWidgets.do({arg item, ind;
		if (item.highlight == true, {
			holdInds = holdInds.add(ind);
		});
	});
	holdInds.sort.reverse.do({arg holdInd;
		this.deleteWidgetAtInd(holdInd);
	});
}

*resetCurrWidgetInd {
	currWidgetInd = arrWidgets.size - 1;
	// update view
	system.flagGuiUpd;
}

*makeGui{ arg window, viewMode;
	TXFrontScreenGui.makeGui(window, viewMode, system, classData);
}


*addImageDialog  {
	var holdString;
		// get path/filenames
		Dialog.getPaths({ arg paths;
			var holdFile;
			holdFile = SCImage.open(paths[0]);
			if (holdFile.isNil, {
				TXInfoScreen.new(
					"Error: the following is not a valid image files:",
					arrInfoLines: [paths[0]]
				);
			},{
				//
				classData.imageFileNames[classData.layerNo] = paths[0];
				classData.holdImages[classData.layerNo] = holdFile;
			});
			// recreate view
			system.showView;
		}, nil, false);
}



*setScreenWidth{ arg newWidth;
	var oldWidth;
	oldWidth = classData.screenWidths[classData.layerNo];
	newWidth = newWidth.max(100).min(20000);
	TXFrontScreen.arrWidgets.do({ arg item, i;
		var holdVal;
		holdVal = item.fromLeft(oldWidth);
		item.fromLeft_(holdVal, newWidth);
	});
	classData.screenWidths[classData.layerNo] = newWidth ? 1000;
}

*setScreenHeight{ arg newHeight;
	var oldHeight;
	oldHeight = classData.screenHeights[classData.layerNo];
	newHeight = newHeight.max(100).min(20000);
	TXFrontScreen.arrWidgets.do({ arg item, i;
		var holdVal;
		holdVal = item.fromTop(oldHeight);
		item.fromTop_(holdVal, newHeight);
	});
	classData.screenHeights[classData.layerNo] = newHeight ? 550;
}


}	// end of class
