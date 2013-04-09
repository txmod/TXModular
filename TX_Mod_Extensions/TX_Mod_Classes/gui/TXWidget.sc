// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXWidget {

	classvar <>system;				// system class
	classvar <>holdNextWidgetID;	// used to give each Widget a unique ID

	var height, <>heightMin=2, <>heightMax=2000, width, <>widthMin=2, <>widthMax=2000;
	var <>layoutX, <>layoutY;
	var <>widgetID, <background, <guiObjectType;
	var <arrViews;
	var <>highlight = false;

//	var onScreen=false;	// not sure if needed in new design?

	*initClass{
		// initialise class variables
		this.resetWidgetID;
	}
	*resetWidgetID{
		// starting ID is 1
		holdNextWidgetID = 1;
	}
	*nextWidgetID {
		var outWidgetID;
		outWidgetID = holdNextWidgetID;
		 holdNextWidgetID = holdNextWidgetID + 1;
		^outWidgetID;
	}
	*new { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		^super.new.init(argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs);
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		//	assign variables
		layoutX = argLayoutX ? 1;
		layoutY = argLayoutY ? 1;
		height = (argHeight ? 20).max(heightMin).min(heightMax);
		width = (argWidth ? 150).max(widthMin).min(widthMax);
		this.newWidgetID;
		background = TXColour.white;
	}
	newWidgetID{
		//	create widgetID
		widgetID = this.class.nextWidgetID;
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		// this method should be overridden in subclasses
		//     arrViews = arrViews.add(XXXXXX);
		//     add action here to build views
		// re: limitWidgetUpdates switch
		// if designing Interface, then certain Widgets need to be display only
		// this is to stop a selection bug when trying to drag widgets around the layout
	}
	updateViews {
		//
		//	N.B. NOT SURE IF STILL NEEDED IN NEW DESIGN?
		//
		//	 add action here to update views with data
		//    & refresh if the widget is on the screen:
		//      if (onScreen == true, {});
	}
	height{
		^height.round(1);
	}
	width{
		^width.round(1);
	}
	height_ { arg argHeight, screenHeight = 550;
		height = argHeight.min(heightMax).min(screenHeight-layoutY).max(heightMin);
		this.updateViews;
	}
	width_ { arg argWidth, screenWidth = 1000;
		width = argWidth.min(widthMax).min(screenWidth-layoutX).max(widthMin);
		this.updateViews;
	}
	fromLeft { arg screenWidth;
		^(layoutX * (screenWidth - width)).round(1);
	}
	fromTop { arg screenHeight;
		^((1- layoutY) * (screenHeight - height)).round(1);
	}
	fromLeft_ { arg argFromLeft, screenWidth;
		layoutX = argFromLeft / (screenWidth - width);
		layoutX = layoutX.max(0).min(1);
		this.updateViews;
	}
	fromTop_ { arg argFromTop, screenHeight;
		layoutY = 1 - (argFromTop /  (screenHeight - height));
		layoutY = layoutY.max(0).min(1);
		this.updateViews;
	}
	bounds { arg layoutWidth, layoutHeight;
		^Rect(this.fromLeft(layoutWidth), this.fromTop(layoutHeight), this.width, this.height);
	}
	bounds_ { arg r, layoutWidth, layoutHeight;
		 this.fromLeft_( r.left, layoutWidth);
		 this.fromTop_(r.top, layoutHeight);
		 this.width_(r.width, layoutWidth);
		 this.height_(r.height, layoutHeight);
	}
	fitToGrid {arg screenGridSize, screenWidth, screenHeight;
		this.fromLeft_(this.fromLeft(screenWidth).round(screenGridSize), screenWidth);
		this.fromTop_(this.fromTop(screenHeight).round(screenGridSize), screenHeight);
	}
	background_ { arg argColor;
		background = argColor;
		this.updateViews;
	}
	backgroundAsArgs {
		^background.storeArgs;
	}
	backgroundAsArgs_ { arg arrArgs;
		background = Color.fromArray(arrArgs);
	}

	properties {
		^#[\height, \width, \layoutX, \layoutY, \widgetID, \backgroundAsArgs]
	}
	getPropertyList {
		^this.properties.collect({ arg name;
			[name, this.perform(name)]
		});
	}
	getTemplatePropertyList {
		var templateProps;
		templateProps = this.properties;
		templateProps.removeAll([\arrActions, \arrActions2, \pressureActions,
			\tiltXActions, \tiltYActions, \mouseDownActions, \mouseDragActions,
			\mouseUpActions, \mouseDoubleClickActions]);
		^templateProps.collect({ arg name;
			[name, this.perform(name)]
		});
	}
	setPropertyList { arg list;
		list.do({ arg item;
			var name, value;
			#name, value = item;
			this.tryPerform(name.asSetter, value);
		});
	}
}

TXWLabelBox : TXWidget {

	classvar <widgetName;

	var  <string, <stringColor, <fontSize, <font;

	*initClass{
		// initialise class variables
		widgetName = "Label Box";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		var arrDefaults;
		//	call super.init
		super.init(argLayoutX, argLayoutY, argHeight, argWidth);
		//	assign variables
		arrDefaults = ["", TXColour.black, "Arial", 12];
		arrExtraArgs = arrExtraArgs ? arrDefaults;
		string = arrExtraArgs.at(0) ? arrDefaults.at(0);
		stringColor = arrExtraArgs.at(1) ? arrDefaults.at(1);
		font = arrExtraArgs.at(2) ? arrDefaults.at(2);
		fontSize = arrExtraArgs.at(3) ? arrDefaults.at(3);
	}
	string_ { arg argString;
		string = argString;
		this.updateViews;
	}
	stringColor_ { arg argStringColor;
		stringColor = argStringColor;
		this.updateViews;
	}
	stringColorAsArgs {
		^stringColor.storeArgs;
	}
	stringColorAsArgs_ { arg arrArgs;
		stringColor = Color.fromArray(arrArgs);
	}
	font_ { arg argFont;
		font = argFont;
		this.updateViews;
	}
	fontSize_ { arg argFontSize;
		fontSize = argFontSize;
		this.updateViews;
	}
	properties {
		^super.properties ++ #[\string, \stringColorAsArgs, \font, \fontSize]
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdView, holdFromLeft, holdFromTop;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height);
		holdView = StaticText(window,
			Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height));
		holdView.string = string;
		holdView.stringColor = stringColor;
		holdView.background = background;
		holdView.font = Font(font, fontSize);
		holdView.align = \centre;
		//	add to arrViews
		arrViews = arrViews.add(holdView);
//		onScreen = true;
	}
}

TXWNotesBox : TXWLabelBox {

	classvar <widgetName;

//	var  <string, <stringColor, <fontSize, <font;

	*initClass{
		// initialise class variables
		widgetName = "Notes Box";
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdView, holdFromLeft, holdFromTop;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height);
		if (limitWidgetUpdates == true, {
			holdView = TXDisplayTextNum(window,
				Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height));
		},{
			holdView = TextView(window,
				Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height))
				.enterInterpretsSelection_(false)
				.canFocus_(false);
		});
		holdView.background = background;
		holdView.stringColor = stringColor;
		holdView.font = Font(font, fontSize);
		holdView.string = string;
		//	add to arrViews
		arrViews = arrViews.add(holdView);
//		onScreen = true;
	}
}

TXWActionButton : TXWLabelBox {

	classvar <widgetName;

	var <>arrActions, <>midiListen, <>midiNote, <>midiMinChannel, <>midiMaxChannel, midiNoteResponder;
	var <>keyListen, <>keyChar, <arrActions2, <>showActions6to10;
	var arrDefaults;

	*initClass{
		// initialise class variables
		widgetName = "Action Button";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		//	assign variables
		arrDefaults = [ [99,0,0,0,0,0,0, nil].dup(5), [99,0,0,0,0,0,0, nil].dup(5) ];
		arrExtraArgs = arrExtraArgs ? arrDefaults;
		arrActions = arrExtraArgs.at(0) ? arrDefaults.at(0);
		arrActions2 = arrExtraArgs.at(1) ? arrDefaults.at(1);
		showActions6to10 = 0;
		argHeight = argHeight ? 20;
		argWidth = argWidth ? 80;
		//	call super.init
		super.init(argLayoutX, argLayoutY, argHeight, argWidth);
		background = TXColour.blue;
		stringColor = TXColour.white;
		midiListen = 0;
		midiNote = 0;
		midiMinChannel = 1;
		midiMaxChannel = 16;
	}
	properties {
		^super.properties ++ #[\arrActions, \midiListen, \midiNote, \midiMinChannel,
			\midiMaxChannel, \keyListen, \keyChar, \arrActions2
			]
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdView, holdFromLeft, holdFromTop;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height);
		holdView = Button(window,
			Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height));
		holdView.states = [[string, stringColor, background]];
		holdView.action = {this.performCommandActions};
		holdView.font = Font(font, fontSize);
		arrViews = arrViews.add(holdView);
		this.midiNoteActivate;
		this.keyActivate;
//		onScreen = true;
	}
	performCommandActions {
		(arrActions ++ arrActions2).do({ arg item, i;
			var holdModuleID, holdModule, holdActionInd, holdArrActionItems, holdActionText,
				holdAction, holdVal1, holdVal2, holdVal3, holdVal4;
			holdModuleID = item.at(0);
			holdActionInd = item.at(1);
			holdVal1 = item.at(2);
			holdVal2 = item.at(3);
			holdVal3 = item.at(4);
			holdVal4 = item.at(5);
			holdActionText = item.at(7);
			holdModule = system.getModuleFromID(holdModuleID);
			if (holdModule != 0, {
				holdArrActionItems = holdModule.arrActionSpecs.collect({arg item, i; item.actionName;});
				// if text found, match action string with text, else use numerical value
				if (holdActionText.notNil, {
					holdActionInd = holdArrActionItems.indexOfEqual(holdActionText) ? holdActionInd;
					holdAction = holdModule.arrActionSpecs.at(holdActionInd);
				},{
					// if text not found, use number but only select older actions with legacyType == 1
					holdAction = holdModule.arrActionSpecs
						.select({arg item, i; item.legacyType == 1}).at(holdActionInd);
				});
				// if holdAction still not found, default to first action
				if (holdAction.isNil, {
					holdAction = holdModule.arrActionSpecs.at(0);
				});
				// if action type is commandAction then value it with arguments
				if (holdAction.actionType == \commandAction, {
					holdAction.actionFunction.value(holdVal1, holdVal2, holdVal3, holdVal4);
				});
				// if action type is valueAction then value it with arguments
				if (holdAction.actionType == \valueAction, {
					holdAction.setValueFunction.value(holdVal1, holdVal2, holdVal3, holdVal4);
				});
			});
		});
		//	gui update
		system.flagGuiUpd;
	}
	midiNoteActivate {
		//	stop any previous Responder
		this.midiNoteDeactivate;
		//	start Responder
		if (midiListen == 1, {
			midiNoteResponder = NoteOnResponder ({  |src, chan, note, vel|
				//  if channel and note match run the actions
			 	if ( ((chan >= (midiMinChannel-1))
			 		and: (chan <= (midiMaxChannel-1))
			 		and: (note == midiNote)),
			 		{
			 		this.performCommandActions;
			 	});
			});
			TXFrontScreen.registerMidiResponder(midiNoteResponder);
		});
	}
	midiNoteDeactivate {
		//	stop responding to midi.
	 	if (midiNoteResponder.class == NoteOnResponder, {
	 		midiNoteResponder.remove;
	 	});
	}
	keyActivate {
		//	if turned on, then activate keyDownAction
		if ((keyListen == 1) and: (keyChar != ""), {
			TXFrontScreen.addKeyDownActionFunction(
				{arg argChar;
					if (argChar.asString == keyChar, {this.performCommandActions; });
				 });
		});
	}
	arrActions2_ { arg argArrActions2;
		//	nil check for older versions which only had arrActions
		arrActions2 = argArrActions2 ? arrDefaults.at(1);
	}


}

TXWSlider : TXWidget {

	classvar <widgetName;

	var <>arrActions, <knobColour, <>thumbSize, <>viewHolder;
	var <>midiListen, <>midiCCNo, <>midiMinChannel, <>midiMaxChannel, midiCCNoResponder;

	*initClass{
		// initialise class variables
		widgetName = "Slider H";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		var arrDefaults;
		//	assign variables
		arrDefaults = [ [[99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil],
			[99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil]] , TXColour.blue, 12];
		arrExtraArgs = arrExtraArgs ? arrDefaults;
		arrActions = arrExtraArgs.at(0) ? arrDefaults.at(0);
		knobColour = arrExtraArgs.at(1) ? arrDefaults.at(1);
		thumbSize = arrExtraArgs.at(2) ? arrDefaults.at(2);
		argHeight = argHeight ? 20;
		argWidth = argWidth ? 150;
		guiObjectType = \number;
		//	call super.init
		super.init(argLayoutX, argLayoutY, argHeight, argWidth);
		midiListen = 0;
		midiCCNo = 0;
		midiMinChannel = 1;
		midiMaxChannel = 16;
	}
	knobColour_ { arg argColor;
		knobColour = argColor;
		this.updateViews;
	}
	knobColourAsArgs {
		^knobColour.storeArgs;
	}
	knobColourAsArgs_ { arg arrArgs;
		knobColour = Color.fromArray(arrArgs);
	}
	properties {
		^super.properties ++ #[\arrActions, \knobColourAsArgs, \thumbSize, \midiListen,
			\midiCCNo, \midiMinChannel, \midiMaxChannel]
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdView, holdFromLeft, holdFromTop, holdControlSpec;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height);
		holdView = Slider(window,
			Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height));
		holdControlSpec = this.getControlSpec;
		if (holdControlSpec.step != 0) {
			holdView.step = (holdControlSpec.step / (holdControlSpec.maxval - holdControlSpec.minval));
		};
		holdView.background = background;
		holdView.knobColor = knobColour;
		holdView.thumbSize = thumbSize;
		holdView.action = {arg view; this.performValueActions(holdControlSpec.map(view.value))};
		holdView.value = holdControlSpec.unmap(this.getValue ? 0);
		arrViews = arrViews.add(holdView);
		// keep view in variable
		viewHolder = holdView;
		// activate midi
		this.midiCCNoActivate;
//		onScreen = true;
		// add screen update function
		system.addScreenUpdFunc(
			[holdView],
			{ arg argArray;
				var holdView = argArray.at(0), holdVal;
				holdVal = this.getValue;
				if (holdVal.notNil, {
					holdView.value = this.getControlSpec.unmap(holdVal);
				});
			}
		);
	}
	getValue { arg argArrActions;
		var holdModuleID, holdModule, holdActionInd, holdActionSpecs, holdArrActionItems,
			holdActionText, holdAction, holdValue, holdVal2, holdVal3, holdVal4;
		// use arrActions if argument is nil
		argArrActions = argArrActions ? arrActions;
		holdModuleID = argArrActions.at(0).at(0);
		holdActionInd = argArrActions.at(0).at(1);
		holdVal2 = argArrActions.at(0).at(3);
		holdVal3 = argArrActions.at(0).at(4);
		holdVal4 = argArrActions.at(0).at(5);
		holdActionText = argArrActions.at(0).at(7);
		holdModule = system.getModuleFromID(holdModuleID);
		if (holdModule != 0, {
			holdActionSpecs = holdModule.arrActionSpecs
				.select({arg action, i; action.actionType == \valueAction; })
				.select({arg action, i; action.guiObjectType == this.guiObjectType});
			// if text found, match action string with text, else use numerical value
			holdArrActionItems = holdActionSpecs.collect({arg item, i; item.actionName;});
			if (holdActionText.notNil, {
				holdActionInd = holdArrActionItems.indexOfEqual(holdActionText) ? holdActionInd;
				holdAction = holdModule.arrActionSpecs
					.select({arg action, i; action.actionType == \valueAction; })
					.select({arg action, i; action.guiObjectType == this.guiObjectType})
					.at(holdActionInd);
			},{
				// if text not found, use number but only select older actions with legacyType == 1
				holdAction = holdModule.arrActionSpecs
					.select({arg item, i; item.legacyType == 1})
					.select({arg action, i; action.actionType == \valueAction; })
					.select({arg action, i; action.guiObjectType == this.guiObjectType})
					.at(holdActionInd);
			});
			if (holdAction.notNil, {holdValue = holdAction.getValueFunction.value(holdVal2, holdVal3, holdVal4);});
		});
		^holdValue;
	}
	getControlSpec {  arg argArrActions;
		var holdModuleID, holdModule, holdActionInd, holdAction, holdControlSpec;
		// use arrActions if argument is nil
		argArrActions = argArrActions ? arrActions;
		holdModuleID = argArrActions.at(0).at(0);
		holdActionInd = argArrActions.at(0).at(1);
		holdModule = system.getModuleFromID(holdModuleID);
		if (holdModule != 0, {
			holdAction = holdModule.arrActionSpecs
				.select({arg action, i; action.actionType == \valueAction; })
				.select({arg action, i; action.guiObjectType == this.guiObjectType})
				.at(holdActionInd);
			if (holdAction.notNil, {holdControlSpec = holdAction.arrControlSpecFuncs.at(0).value;});
		});
		^(holdControlSpec ? nil.asSpec);
	}
	performValueActions { arg argValue, argArrActions;
		// use arrActions if argument is nil
		argArrActions = argArrActions ? arrActions;
		argArrActions.do({ arg item, i;
			var holdModuleID, holdModule, holdActionInd, holdArrActionItems, holdActionText, holdAction,
				holdVal1, holdVal2, holdVal3, holdVal4, holdActionSpecs;
			holdModuleID = item.at(0);
			holdActionInd = item.at(1);
			holdVal1 = argValue ?? item.at(2);
			holdVal2 = item.at(3);
			holdVal3 = item.at(4);
			holdVal4 = item.at(5);
			holdActionText = item.at(7);
			holdModule = system.getModuleFromID(holdModuleID);
			if (holdModule != 0, {
				holdActionSpecs = holdModule.arrActionSpecs
					.select({arg action, i; action.actionType == \valueAction; })
					.select({arg action, i; action.guiObjectType == this.guiObjectType});
				if (holdActionSpecs.size > 0, {
					holdArrActionItems = holdActionSpecs.collect({arg item, i; item.actionName;});
					// if text found, match action string with text
					if (holdActionText.notNil, {
						holdActionInd = holdArrActionItems.indexOfEqual(holdActionText) ? holdActionInd;
						holdAction = holdActionSpecs.at(holdActionInd);
					},{
						// if text not found, use number but only select older actions with legacyType == 1
						holdAction = holdActionSpecs
							.select({arg item, i; item.legacyType == 1}).at(holdActionInd);
					});
					// if holdAction still not found, default to first action
					if (holdAction.isNil, {
						holdAction = holdActionSpecs.at(0);
					});
					// value valueAction  with arguments
					holdAction.setValueFunction.value(holdVal1, holdVal2, holdVal3, holdVal4);
				});
			});
		});
		//	gui update
		system.flagGuiUpd;
	}
	midiCCNoActivate {
		//	stop any previous Responder
		this.midiCCNoDeactivate;
		//	start Responder
		if (midiListen == 1, {
			midiCCNoResponder = CCResponder({ |src, chan, num, val|
				var holdCtlVal;
				//  if channel and note match run the actions
			 	if ( ((chan >= (midiMinChannel-1))
			 		and: (chan <= (midiMaxChannel-1))
			 		and: (num == midiCCNo)),
		 		{
		 			if (viewHolder.notNil, {
			 			if (viewHolder.notClosed, {
			 				holdCtlVal = val;
			 				{viewHolder.valueAction_(holdCtlVal / 127);}.defer;
			 			});
		 			});
			 	});
			});
			TXFrontScreen.registerMidiResponder(midiCCNoResponder);
		});
	}
	midiCCNoDeactivate {
		//	stop responding to midi.
	 	if (midiCCNoResponder.class == CCResponder, {
	 		midiCCNoResponder.remove;
	 	});
	}
}

TXWSliderV : TXWSlider {

	classvar <widgetName;

	*initClass{
		// initialise class variables
		widgetName = "Slider V";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		argHeight = argHeight ? 20;
		argWidth = argWidth ? 150;
		//	call super.init with height and width swapped
		super.init(argLayoutX, argLayoutY, argWidth, argHeight, arrExtraArgs);
	}
}

TXW2DSlider : TXWSlider {

	classvar <widgetName;

	var <>arrActions2, <>midiCCNo2, <>showYAxis;

	*initClass{
		// initialise class variables
		widgetName = "2-D Slider";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		var arrDefaults;
		//	assign variables
		arrDefaults = [ [99,0,0,0,0,0,0, nil].dup(5), TXColour.blue, 12, [99,0,0,0,0,0,0, nil].dup(5)];
		arrExtraArgs = arrExtraArgs ? arrDefaults;
		arrActions = arrExtraArgs.at(0) ? arrDefaults.at(0);
		knobColour = arrExtraArgs.at(1) ? arrDefaults.at(1);
		thumbSize = arrExtraArgs.at(2) ? arrDefaults.at(2);
		arrActions2 = arrExtraArgs.at(3) ? arrDefaults.at(3);
		argHeight = argHeight ? 100;
		argWidth = argWidth ? 100;
		guiObjectType = \number;
		midiCCNo2 = 1;
		showYAxis = 0;
		//	call super.init
		super.init(argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs);
	}
	properties {
		^super.properties ++ #[\arrActions2, \midiCCNo2];
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdView, holdFromLeft, holdFromTop, holdControlSpec, holdControlSpec2;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height);
		holdView = Slider2D(window,
			Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height));
		holdView.background = background;
		holdView.knobColor = knobColour;
		holdControlSpec = this.getControlSpec(arrActions);
		holdControlSpec2 = this.getControlSpec(arrActions2);
		holdView.action = {arg view;
			this.performValueActions(holdControlSpec.map(view.x), arrActions);
			this.performValueActions(holdControlSpec2.map(view.y), arrActions2);
		};
		holdView.x = holdControlSpec.unmap(this.getValue(arrActions) ? 0);
		holdView.y = holdControlSpec2.unmap(this.getValue(arrActions2) ? 0);
		arrViews = arrViews.add(holdView);
		// keep view in variable
		viewHolder = holdView;
		// activate midi
		this.midiCCNoActivate;
//		onScreen = true;
		// add screen update function
		system.addScreenUpdFunc(
			[holdView],
			{ arg argArray;
				var holdView = argArray.at(0), holdVal, holdVal2;
				holdVal = this.getValue(arrActions);
				holdVal2 = this.getValue(arrActions2);
				if (holdVal.notNil, {
					holdView.x = this.getControlSpec.unmap(holdVal);
				});
				if (holdVal2.notNil, {
					holdView.y = this.getControlSpec2.unmap(holdVal2);
				});
			}
		);
	}
	getControlSpec2 {  arg argArrActions2;
		var holdModuleID, holdModule, holdActionInd, holdAction, holdControlSpec2;
		// use arrActions if argument is nil
		argArrActions2 = argArrActions2 ? arrActions2;
		holdModuleID = argArrActions2.at(0).at(0);
		holdActionInd = argArrActions2.at(0).at(1);
		holdModule = system.getModuleFromID(holdModuleID);
		if (holdModule != 0, {
			holdAction = holdModule.arrActionSpecs
				.select({arg action, i; action.actionType == \valueAction; })
				.select({arg action, i; action.guiObjectType == this.guiObjectType})
				.at(holdActionInd);
			if (holdAction.notNil, {holdControlSpec2 = holdAction.arrControlSpecFuncs.at(0).value;});
		});
		^(holdControlSpec2 ? nil.asSpec);
	}
	midiCCNoActivate {
		//	stop any previous Responder
		this.midiCCNoDeactivate;
		//	start Responder
		if (midiListen == 1, {
			midiCCNoResponder = CCResponder({ |src, chan, num, val|
				var holdCtlVal, holdCtlVal2;
				//  if channel and note match run the actions
			 	if ( (chan >= (midiMinChannel-1))
			 		and: (chan <= (midiMaxChannel-1)),
		 		{
		 			if (viewHolder.notNil, {
			 			if (viewHolder.notClosed, {
		 					if ( ((num == midiCCNo)), {
				 				holdCtlVal = val;
				 				{viewHolder.activey_(holdCtlVal / 127);}.defer;
				 			});
		 					if ( ((num == midiCCNo2)), {
				 				holdCtlVal2 = val;
				 				{viewHolder.activex_(holdCtlVal2 / 127);}.defer;
				 			});
			 			});
		 			});
			 	});
			});
			TXFrontScreen.registerMidiResponder(midiCCNoResponder);
		});
	}

}

TXW2DTablet : TXWSlider {

	classvar <widgetName;

	var <>arrActions2, <>midiCCNo2, <>showActionIndex;
	var <>pressureActions, <>tiltXActions, <>tiltYActions, <>mouseDownActions,
		<>mouseDragActions, <>mouseUpActions, <>mouseDoubleClickActions;


	*initClass{
		// initialise class variables
		widgetName = "2-D Tablet";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		var arrDefaults, arrDefaultActions;
		//	assign variables
		arrDefaultActions = [99,0,0,0,0,0,0, nil].dup(5);
		arrDefaults = [ arrDefaultActions.deepCopy, TXColour.blue, 12, arrDefaultActions.deepCopy,
			arrDefaultActions.deepCopy, arrDefaultActions.deepCopy, arrDefaultActions.deepCopy,
			arrDefaultActions.deepCopy, arrDefaultActions.deepCopy,
			arrDefaultActions.deepCopy, arrDefaultActions.deepCopy
		];
		arrExtraArgs = arrExtraArgs ? arrDefaults;
		arrActions = arrExtraArgs.at(0) ? arrDefaults.at(0);
		knobColour = arrExtraArgs.at(1) ? arrDefaults.at(1);
		thumbSize = arrExtraArgs.at(2) ? arrDefaults.at(2);
		arrActions2 = arrExtraArgs.at(3) ? arrDefaults.at(3);
		pressureActions = arrExtraArgs.at(4) ? arrDefaults.at(4);
		tiltXActions = arrExtraArgs.at(5) ? arrDefaults.at(5);
		tiltYActions = arrExtraArgs.at(6) ? arrDefaults.at(6);
		mouseDownActions = arrExtraArgs.at(7) ? arrDefaults.at(7);
		mouseDragActions = arrExtraArgs.at(8) ? arrDefaults.at(8);
		mouseUpActions = arrExtraArgs.at(9) ? arrDefaults.at(9);
		mouseDoubleClickActions = arrExtraArgs.at(10) ? arrDefaults.at(10);

		argHeight = argHeight ? 100;
		argWidth = argWidth ? 100;
		guiObjectType = \number;
		midiCCNo2 = 1;
		showActionIndex = 0;
		//	call super.init
		super.init(argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs);
	}
	properties {
		^super.properties ++ #[\arrActions2, \midiCCNo2, \pressureActions, \tiltXActions, \tiltYActions,
			\mouseDownActions, \mouseDragActions, \mouseUpActions, \mouseDoubleClickActions];
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdView, holdFromLeft, holdFromTop, holdControlSpec, holdControlSpec2;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height);
		holdView = TabletSlider2D(window,
			Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height));
		holdView.background = background;
		holdView.knobColor = knobColour;
		holdControlSpec = this.getControlSpec(arrActions);
		holdControlSpec2 = this.getControlSpec(arrActions2);
		holdView.action = { arg view,x,y,pressure,tiltx,tilty,deviceID,
				buttonNumber,clickCount,absoluteZ,rotation;
			this.performValueActions(holdControlSpec.map(view.x), arrActions);
			this.performValueActions(holdControlSpec2.map(view.y), arrActions2);
			this.performValueActions((pressure ? 0).clip(0,1), pressureActions);
//			this.performValueActions((tiltx ? 0 * 500 + 0.5).clip(0,1), tiltXActions);
//			this.performValueActions((tilty ? 0 * 500 + 0.5).clip(0,1), tiltYActions);
			this.performValueActions((tiltx ? 0 + 0.5).clip(0,1), tiltXActions);
			this.performValueActions((tilty ? 0 + 0.5).clip(0,1), tiltYActions);
			this.performCommandActions(mouseDragActions);
		};
		holdView.mouseDownAction = { arg view,x,y,pressure,tiltx,tilty,deviceID,
				buttonNumber,clickCount,absoluteZ,rotation;
			this.performValueActions(holdControlSpec.map(view.x), arrActions);
			this.performValueActions(holdControlSpec2.map(view.y), arrActions2);
			this.performValueActions((pressure ? 0).clip(0,1), pressureActions);
//			this.performValueActions((tiltx ? 0 * 500 + 0.5).clip(0,1), tiltXActions);
//			this.performValueActions((tilty ? 0 * 500 + 0.5).clip(0,1), tiltYActions);
			this.performValueActions((tiltx ? 0 + 0.5).clip(0,1), tiltXActions);
			this.performValueActions((tilty ? 0 + 0.5).clip(0,1), tiltYActions);
			this.performCommandActions(mouseDownActions);
			if (clickCount == 2, {this.performCommandActions(mouseDoubleClickActions) });
		};
		holdView.mouseUpAction = { arg view,x,y,pressure,tiltx,tilty,deviceID,
				buttonNumber,clickCount,absoluteZ,rotation;
			this.performValueActions(holdControlSpec.map(view.x), arrActions);
			this.performValueActions(holdControlSpec2.map(view.y), arrActions2);
			this.performValueActions((pressure ? 0).clip(0,1), pressureActions);
//			this.performValueActions((tiltx ? 0 * 500 + 0.5).clip(0,1), tiltXActions);
//			this.performValueActions((tilty ? 0 * 500 + 0.5).clip(0,1), tiltYActions);
			this.performValueActions((tiltx ? 0 + 0.5).clip(0,1), tiltXActions);
			this.performValueActions((tilty ? 0 + 0.5).clip(0,1), tiltYActions);
			this.performCommandActions(mouseUpActions);
		};
		holdView.x = holdControlSpec.unmap(this.getValue(arrActions) ? 0);
		holdView.y = holdControlSpec2.unmap(this.getValue(arrActions2) ? 0);
		arrViews = arrViews.add(holdView);
		// keep view in variable
		viewHolder = holdView;
		// activate midi
		this.midiCCNoActivate;
//		onScreen = true;
		// add screen update function
		system.addScreenUpdFunc(
			[holdView],
			{ arg argArray;
				var holdView = argArray.at(0), holdVal, holdVal2;
				holdVal = this.getValue(arrActions);
				holdVal2 = this.getValue(arrActions2);
				if (holdVal.notNil, {
					holdView.x = this.getControlSpec.unmap(holdVal);
				});
				if (holdVal2.notNil, {
					holdView.y = this.getControlSpec2.unmap(holdVal2);
				});
			}
		);
	}
	getControlSpec2 {  arg argArrActions2;
		var holdModuleID, holdModule, holdActionInd, holdAction, holdControlSpec2;
		// use arrActions if argument is nil
		argArrActions2 = argArrActions2 ? arrActions2;
		holdModuleID = argArrActions2.at(0).at(0);
		holdActionInd = argArrActions2.at(0).at(1);
		holdModule = system.getModuleFromID(holdModuleID);
		if (holdModule != 0, {
			holdAction = holdModule.arrActionSpecs
				.select({arg action, i; action.actionType == \valueAction; })
				.select({arg action, i; action.guiObjectType == this.guiObjectType})
				.at(holdActionInd);
			if (holdAction.notNil, {holdControlSpec2 = holdAction.arrControlSpecFuncs.at(0).value;});
		});
		^(holdControlSpec2 ? nil.asSpec);
	}
	midiCCNoActivate {
		//	stop any previous Responder
		this.midiCCNoDeactivate;
		//	start Responder
		if (midiListen == 1, {
			midiCCNoResponder = CCResponder({ |src, chan, num, val|
				var holdCtlVal, holdCtlVal2;
				//  if channel and note match run the actions
			 	if ( (chan >= (midiMinChannel-1))
			 		and: (chan <= (midiMaxChannel-1)),
		 		{
		 			if (viewHolder.notNil, {
			 			if (viewHolder.notClosed, {
		 					if ( ((num == midiCCNo)), {
				 				holdCtlVal = val;
				 				{viewHolder.activey_(holdCtlVal / 127);}.defer;
				 			});
		 					if ( ((num == midiCCNo2)), {
				 				holdCtlVal2 = val;
				 				{viewHolder.activex_(holdCtlVal2 / 127);}.defer;
				 			});
			 			});
		 			});
			 	});
			});
			TXFrontScreen.registerMidiResponder(midiCCNoResponder);
		});
	}
	performCommandActions { arg argArrActions;
		argArrActions.do({ arg item, i;
			var holdModuleID, holdModule, holdActionInd, holdArrActionItems, holdActionText,
				holdAction, holdVal1, holdVal2, holdVal3, holdVal4;
			holdModuleID = item.at(0);
			holdActionInd = item.at(1);
			holdVal1 = item.at(2);
			holdVal2 = item.at(3);
			holdVal3 = item.at(4);
			holdVal4 = item.at(5);
			holdActionText = item.at(7);
			holdModule = system.getModuleFromID(holdModuleID);
			if (holdModule != 0, {
				holdArrActionItems = holdModule.arrActionSpecs.collect({arg item, i; item.actionName;});
				// if text found, match action string with text, else use numerical value
					if (holdActionText.notNil, {
						holdActionInd = holdArrActionItems.indexOfEqual(holdActionText) ? holdActionInd;
						holdAction = holdModule.arrActionSpecs.at(holdActionInd);
					},{
						// if text not found, use number but only select older actions with legacyType == 1
						holdAction = holdModule.arrActionSpecs.select({arg item, i; item.legacyType == 1}).at(holdActionInd);
					});
				// if action type is commandAction then value it with arguments
				if (holdAction.actionType == \commandAction, {
					holdAction.actionFunction.value(holdVal1, holdVal2, holdVal3, holdVal4);
				});
				// if action type is valueAction then value it with arguments
				if (holdAction.actionType == \valueAction, {
					holdAction.setValueFunction.value(holdVal1, holdVal2, holdVal3, holdVal4);
				});
			});
		});
		//	gui update
		system.flagGuiUpd;
	}
}

TXWNumberBox : TXWSlider {

	classvar <widgetName;

	var  <stringColor, <fontSize, <font;

	*initClass{
		// initialise class variables
		widgetName = "Number Box";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		var arrDefaults;
		//	assign variables
		arrDefaults = [ [[99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil],
			[99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil]],
			TXColour.black, "Arial", 12 ];
		arrExtraArgs = arrExtraArgs ? arrDefaults;
		arrActions = arrExtraArgs.at(0) ? arrDefaults.at(0);
		stringColor = arrExtraArgs.at(1) ? arrDefaults.at(1);
		font = arrExtraArgs.at(2) ? arrDefaults.at(2);
		fontSize = arrExtraArgs.at(3) ? arrDefaults.at(3);
		argHeight = argHeight ? 20;
		argWidth = argWidth ? 80;
		guiObjectType = \number;
		//	call super.init
		super.init(argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs);
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdView, holdFromLeft, holdFromTop, holdControlSpec;
		holdControlSpec = this.getControlSpec;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height);
		if (limitWidgetUpdates == true, {
			holdView = TXDisplayTextNum( window,
				Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height)
			);
		},{
			holdView = TXScrollNumBox( window,
				Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height),
				holdControlSpec
			).maxDecimals_(4);
		});
		holdView.background = background;
		holdView.stringColor = stringColor;
		holdView.font = Font(font, fontSize);
		holdView.action = {arg view;
			view.value = holdControlSpec.constrain(view.value).round(0.001);
			this.performValueActions(view.value)
		};
		holdView.value = (this.getValue ? 0).round(0.001);
		arrViews = arrViews.add(holdView);
//		onScreen = true;
		// add screen update function
		system.addScreenUpdFunc(
			[holdView],
			{ arg argArray;
				var holdView = argArray.at(0), holdVal;
				holdVal = this.getValue;
				if (holdVal.notNil, {
					holdView.value = holdVal.round(0.0001);
				});
			}
		);
	}
	stringColor_ { arg argStringColor;
		stringColor = argStringColor;
		this.updateViews;
	}
	stringColorAsArgs {
		^stringColor.storeArgs;
	}
	stringColorAsArgs_ { arg arrArgs;
		stringColor = Color.fromArray(arrArgs);
	}
	font_ { arg argFont;
		font = argFont;
		this.updateViews;
	}
	fontSize_ { arg argFontSize;
		fontSize = argFontSize;
		this.updateViews;
	}
	properties {
		^super.properties ++ #[\stringColorAsArgs, \font, \fontSize]
	}
}
TXWSliderNo : TXWNumberBox {

	classvar <widgetName;

	var  <>numberSize;

	*initClass{
		// initialise class variables
		widgetName = "Slider Number H";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		var arrDefaults;
		//	assign variables
		arrDefaults = [ [[99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil],
			[99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil]],
			TXColour.blue, "Arial", 12, 40, 12 ];
		arrExtraArgs = arrExtraArgs ? arrDefaults;
		arrActions = arrExtraArgs.at(0) ? arrDefaults.at(0);
		knobColour = arrExtraArgs.at(1) ? arrDefaults.at(1);
		font = arrExtraArgs.at(2) ? arrDefaults.at(2);
		fontSize = arrExtraArgs.at(3) ? arrDefaults.at(3);
		numberSize = arrExtraArgs.at(4) ? arrDefaults.at(4);
		argHeight = argHeight ? 20;
		argWidth = argWidth ? 150;
		guiObjectType = \number;
		//	call super.init
		super.init(argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs);
		thumbSize = arrExtraArgs.at(5) ? arrDefaults.at(5);
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdSliderView, holdNumberView, holdFromLeft, holdFromTop, holdControlSpec;
		var holdNumberWidth, holdSliderWidth;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height);
		holdNumberWidth = numberSize.max(0).min(width);
		holdSliderWidth = width - holdNumberWidth.max(0) - 2;
		// slider
		holdSliderView = Slider(window,
			Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, holdSliderWidth, height));
		holdControlSpec = this.getControlSpec;
		if (holdControlSpec.step != 0) {
			holdSliderView.step = (holdControlSpec.step / (holdControlSpec.maxval - holdControlSpec.minval));
		};
		holdSliderView.background = background;
		holdSliderView.knobColor = knobColour;
		holdSliderView.thumbSize = thumbSize;
		holdSliderView.action = {arg view;
			this.performValueActions(holdControlSpec.map(view.value));
			// update numberview
			holdNumberView.value = holdControlSpec.map(view.value).round(0.001);
		};
		holdSliderView.value = holdControlSpec.unmap(this.getValue ? 0);
		arrViews = arrViews.add(holdSliderView);
		// keep view in variable
		viewHolder = holdSliderView;
		// activate midi
		this.midiCCNoActivate;
		// numberbox
		holdControlSpec = this.getControlSpec;
		if (limitWidgetUpdates == true, {
			holdNumberView = TXDisplayTextNum(
				window,
				Rect(offsetLeft + holdFromLeft + holdSliderWidth + 2, offsetTop + holdFromTop,
					holdNumberWidth, height)
			);
		},{
			holdNumberView = TXScrollNumBox(
				window,
				Rect(offsetLeft + holdFromLeft + holdSliderWidth + 2, offsetTop + holdFromTop,
					holdNumberWidth, height),
					holdControlSpec
			).maxDecimals_(4);
		});
		holdNumberView.stringColor = stringColor;
		holdNumberView.font = Font(font, fontSize);
		holdNumberView.background = background;
		holdNumberView.action = {arg view;
			view.value = holdControlSpec.constrain(view.value).round(0.001);
			this.performValueActions(view.value);
			// update sliderview
			holdSliderView.value = holdControlSpec.unmap(view.value);
		};
		holdNumberView.value = (this.getValue ? 0).round(0.001);
		arrViews = arrViews.add(holdNumberView);
//		onScreen = true;
		// add screen update function
		system.addScreenUpdFunc(
			[holdSliderView, holdNumberView],
			{ arg argArray;
				var holdSliderView = argArray.at(0);
				var holdNumberView = argArray.at(1);
				var holdVal;
				holdVal = this.getValue;
				if (holdVal.notNil, {
					holdSliderView.value = this.getControlSpec.unmap(holdVal);
					holdNumberView.value = holdVal.round(0.0001);
				});
			}
		);
	}
	properties {
		^super.properties ++ #[\numberSize]
	}
}
TXWSliderNoV : TXWNumberBox {

	classvar <widgetName;

	var  <>numberSize;

	*initClass{
		// initialise class variables
		widgetName = "Slider Number V";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		var arrDefaults;
		//	assign variables
		arrDefaults = [ [[99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil],
			[99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil]],
			TXColour.blue, "Arial", 12, 20, 12 ];
		arrExtraArgs = arrExtraArgs ? arrDefaults;
		arrActions = arrExtraArgs.at(0) ? arrDefaults.at(0);
		knobColour = arrExtraArgs.at(1) ? arrDefaults.at(1);
		font = arrExtraArgs.at(2) ? arrDefaults.at(2);
		fontSize = arrExtraArgs.at(3) ? arrDefaults.at(3);
		numberSize = arrExtraArgs.at(4) ? arrDefaults.at(4);
		guiObjectType = \number;
		argHeight = argHeight ? 20;
		argWidth = argWidth ? 150;
		//	call super.init with height and width swapped
		super.init(argLayoutX, argLayoutY, argWidth, argHeight, arrExtraArgs);
		thumbSize = arrExtraArgs.at(5) ? arrDefaults.at(5);
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdSliderView, holdNumberView, holdFromLeft, holdFromTop, holdControlSpec;
		var holdNumberHeight, holdSliderHeight;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height) - 2;
		holdNumberHeight = numberSize.max(0).min(height);
		holdSliderHeight = height - holdNumberHeight.max(0);
		// slider
		holdSliderView = Slider(window,
			Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, holdSliderHeight));
		holdControlSpec = this.getControlSpec;
		if (holdControlSpec.step != 0) {
			holdSliderView.step = (holdControlSpec.step / (holdControlSpec.maxval - holdControlSpec.minval));
		};
		holdSliderView.background = background;
		holdSliderView.knobColor = knobColour;
		holdSliderView.thumbSize = thumbSize;
		holdSliderView.action = {arg view;
			this.performValueActions(holdControlSpec.map(view.value));
			// update numberview
			holdNumberView.value = holdControlSpec.map(view.value).round(0.001);
		};
		holdSliderView.value = holdControlSpec.unmap(this.getValue ? 0);
		arrViews = arrViews.add(holdSliderView);
		// keep view in variable
		viewHolder = holdSliderView;
		// activate midi
		this.midiCCNoActivate;
		// numberbox
		holdControlSpec = this.getControlSpec;
		if (limitWidgetUpdates == true, {
			holdNumberView = TXDisplayTextNum(
				window,
				Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop + holdSliderHeight + 2,
					width, holdNumberHeight)
			);
		},{
			holdNumberView = TXScrollNumBox(
				window,
				Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop + holdSliderHeight + 2,
					width, holdNumberHeight),
				holdControlSpec
			).maxDecimals_(4);
		});
		holdNumberView.stringColor = stringColor;
		holdNumberView.font = Font(font, fontSize);
		holdNumberView.background = background;
		holdNumberView.action = {arg view;
			view.value = holdControlSpec.constrain(view.value).round(0.001);
			this.performValueActions(view.value);
			// update sliderview
			holdSliderView.value = holdControlSpec.unmap(view.value);
		};
		holdNumberView.value = (this.getValue ? 0).round(0.001);
		arrViews = arrViews.add(holdNumberView);
//		onScreen = true;
		// add screen update function
		system.addScreenUpdFunc(
			[holdSliderView, holdNumberView],
			{ arg argArray;
				var holdSliderView = argArray.at(0);
				var holdNumberView = argArray.at(1);
				var holdVal;
				holdVal = this.getValue;
				if (holdVal.notNil, {
					holdSliderView.value = this.getControlSpec.unmap(holdVal);
					holdNumberView.value = holdVal.round(0.0001);
				});
			}
		);
	}
	properties {
		^super.properties ++ #[\numberSize]
	}
}
TXWPopup : TXWNumberBox {

	classvar <widgetName;

	*initClass{
		// initialise class variables
		widgetName = "Popup";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		//	assign variables
		argWidth = argWidth ? 150;
		//	call super.init
		super.init(argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs);
		guiObjectType = \popup;
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdView, holdFromLeft, holdFromTop, holdItems;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height);
		holdView = PopUpMenu(window,
			Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height));
		holdView.stringColor = stringColor;
		holdView.font = Font(font, fontSize);
		holdView.background = background;
		holdItems = this.getItemsFunction;
		if (holdItems.notNil, {holdView.items = holdItems;});
		holdView.action = {arg view;
			this.performValueActions(view.value)
		};
		holdView.value = this.getValue ? 0;
		arrViews = arrViews.add(holdView);
//		onScreen = true;
		// add screen update function
		system.addScreenUpdFunc(
			[holdView],
			{ arg argArray;
				var holdView = argArray.at(0);
				holdView.value = this.getValue ? 0;
			}
		);
	}
	getItemsFunction {
		var holdModuleID, holdModule, holdActionInd, holdAction, holdItems;
		holdModuleID = arrActions.at(0).at(0);
		holdActionInd = arrActions.at(0).at(1);
		holdModule = system.getModuleFromID(holdModuleID);
		if (holdModule != 0, {
			holdAction = holdModule.arrActionSpecs
				.select({arg action, i; action.actionType == \valueAction; })
				.select({arg action, i; action.guiObjectType == this.guiObjectType})
				.at(holdActionInd);
			if (holdAction.notNil, {holdItems = holdAction.getItemsFunction.value;});
		});
		^holdItems;
	}
}

TXWCheckBox : TXWNumberBox {

	classvar <widgetName;

	var  <string, <>colourReverse;

	*initClass{
		// initialise class variables
		widgetName = "Check Box";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		var arrDefaults;
		//	assign variables
		arrDefaults = [ [[99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil],
			[99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil]],
			TXColour.black, "Arial", 12, "", 1];
		arrExtraArgs = arrExtraArgs ? arrDefaults;
		arrActions = arrExtraArgs.at(0) ? arrDefaults.at(0);
		stringColor = arrExtraArgs.at(1) ? arrDefaults.at(1);
		font = arrExtraArgs.at(2) ? arrDefaults.at(2);
		fontSize = arrExtraArgs.at(3) ? arrDefaults.at(3);
		string = arrExtraArgs.at(4) ? arrDefaults.at(4);
		colourReverse = arrExtraArgs.at(5) ? arrDefaults.at(5);
		argHeight = argHeight ? 20;
		argWidth = argWidth ? 150;
		//	call super.init
		super.init(argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs);
		guiObjectType = \checkbox;
	}
	string_ { arg argString;
		string = argString;
		this.updateViews;
	}
	properties {
		^super.properties ++ #[\string, \colourReverse]
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdView, holdFromLeft, holdFromTop, holdItems, holdStringColor2, holdBackground2;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height);
		if (colourReverse == 1, {
			holdStringColor2 = background;
			holdBackground2 = stringColor;
		},{
			holdStringColor2 = stringColor;
			holdBackground2 = background;
		});
		holdView = TXCheckBox(window,
			Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height),
			string, stringColor, background, holdStringColor2, holdBackground2);
		holdView.buttonView.font = Font(font, fontSize);
		holdView.action = {arg view;
			this.performValueActions(view.value)
		};
		holdView.value = this.getValue ? 0;
		arrViews = arrViews.add(holdView);
//		onScreen = true;
		// add screen update function
		system.addScreenUpdFunc(
			[holdView],
			{ arg argArray;
				var holdView = argArray.at(0), holdVal;
				holdVal = this.getValue;
				if (holdVal.notNil, {
					holdView.value = holdVal;
				});
			}
		);
	}
	getActionName {
		var holdModuleID, holdModule, holdActionInd, holdAction, holdActionName;
		holdModuleID = arrActions.at(0).at(0);
		holdActionInd = arrActions.at(0).at(1);
		holdModule = system.getModuleFromID(holdModuleID);
		if (holdModule != 0, {
			holdAction = holdModule.arrActionSpecs
				.select({arg action, i; action.actionType == \valueAction; })
				.select({arg action, i; action.guiObjectType == this.guiObjectType})
				.at(holdActionInd);
			if (holdAction.notNil, {holdActionName = holdAction.actionName;});
		});
		^(holdActionName ? "");
	}
}

TXWKnob : TXWSlider {

	classvar <widgetName;

	*initClass{
		// initialise class variables
		widgetName = "Knob";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		// force height to be same as width
		argHeight = argWidth ? 40;
		argWidth = argWidth ? 40;
		super.init(argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs);
		// swap default colours
		knobColour = TXColour.white;
		background = TXColour.blue;
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdView, holdFromLeft, holdFromTop, holdControlSpec;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height);
		holdView = Knob(window,
			Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height));
		holdView.color = [knobColour, knobColour, background, background];
		holdControlSpec = this.getControlSpec;
		if (holdControlSpec.step != 0) {
			holdView.step = (holdControlSpec.step / (holdControlSpec.maxval - holdControlSpec.minval));
		};
		holdView.action = {arg view; this.performValueActions(holdControlSpec.map(view.value))};
		holdView.value = holdControlSpec.unmap(this.getValue ? 0);
		arrViews = arrViews.add(holdView);
		// keep view in variable
		viewHolder = holdView;
		// activate midi
		this.midiCCNoActivate;
//		onScreen = true;
		// add screen update function
		system.addScreenUpdFunc(
			[holdView],
			{ arg argArray;
				var holdView = argArray.at(0), holdVal;
				holdVal = this.getValue;
				if (holdVal.notNil, {
					holdView.value = this.getControlSpec.unmap(holdVal);
				});
			}
		);
	}
	height_ { arg argHeight, screenHeight = 550;
		// force height & width to be same
		width = argHeight.min(heightMax).min(screenHeight-layoutX).max(heightMin);
		height = width;
		this.updateViews;
	}
	width_ { arg argWidth, screenWidth = 1000;
		// force height & width to be same
		width = argWidth.min(widthMax).min(screenWidth-layoutX).max(widthMin);
		height = width;
		this.updateViews;
	}
}

TXWTextEditBox : TXWNumberBox {

	classvar <widgetName;

	*initClass{
		// initialise class variables
		widgetName = "Text Edit Box";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		var arrDefaults;
		//	assign variables
		arrDefaults = [ [[99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil],
			[99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil]],
			TXColour.black, "Arial", 12 ];
		arrExtraArgs = arrExtraArgs ? arrDefaults;
		arrActions = arrExtraArgs.at(0) ? arrDefaults.at(0);
		stringColor = arrExtraArgs.at(1) ? arrDefaults.at(1);
		font = arrExtraArgs.at(2) ? arrDefaults.at(2);
		fontSize = arrExtraArgs.at(3) ? arrDefaults.at(3);
		argHeight = argHeight ? 20;
		argWidth = argWidth ? 150;
		//	call super.init
		super.init(argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs);
		guiObjectType = \textedit;
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdView, holdFromLeft, holdFromTop, holdControlSpec;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height);
		if (limitWidgetUpdates == true, {
			holdView = TXDisplayTextNum(window,
				Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height));
		},{
			holdView = TextField(window,
				Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height));
		});
		holdView.background = background;
		holdView.stringColor = stringColor;
		holdView.font = Font(font, fontSize);
		holdView.action = {arg view;
			this.performValueActions(view.string)
		};
		holdView.string = this.getString ? "";
		arrViews = arrViews.add(holdView);
//		onScreen = true;
		// add screen update function
		system.addScreenUpdFunc(
			[holdView],
			{ arg argArray;
				var holdView = argArray.at(0);
				holdView.string = this.getString ? "";
			}
		);
	}
	getString {
		var holdModuleID, holdModule, holdActionInd, holdAction, holdString;
		holdModuleID = arrActions.at(0).at(0);
		holdActionInd = arrActions.at(0).at(1);
		holdModule = system.getModuleFromID(holdModuleID);
		if (holdModule != 0, {
			holdAction = holdModule.arrActionSpecs
				.select({arg action, i; action.actionType == \valueAction; })
				.select({arg action, i; action.guiObjectType == this.guiObjectType})
				.at(holdActionInd);
			if (holdAction.notNil, {holdString = holdAction.getValueFunction.value;});
		});
		^holdString;
	}
}

TXWTextDisplayBox : TXWNumberBox {

	classvar <widgetName;

	*initClass{
		// initialise class variables
		widgetName = "Text Display Box";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		var arrDefaults;
		//	assign variables
		arrDefaults = [ [[99,0,0,0,0,0,0, nil]],
			TXColour.black, "Arial", 12 ];
		arrExtraArgs = arrExtraArgs ? arrDefaults;
		arrActions = arrExtraArgs.at(0) ? arrDefaults.at(0);
		stringColor = arrExtraArgs.at(1) ? arrDefaults.at(1);
		font = arrExtraArgs.at(2) ? arrDefaults.at(2);
		fontSize = arrExtraArgs.at(3) ? arrDefaults.at(3);
		argHeight = argHeight ? 20;
		argWidth = argWidth ? 150;
		//	call super.init
		super.init(argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs);
		guiObjectType = \text;
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdView, holdFromLeft, holdFromTop, holdControlSpec;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height);
		if (limitWidgetUpdates == true, {
			holdView = TXDisplayTextNum(window,
				Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height));
		},{
			holdView = TextView(window,
				Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height));
		});
		holdView.background = background;
		holdView.stringColor = stringColor;
		holdView.font = Font(font, fontSize);
		holdView.string = this.getString ? "";
		holdView.canFocus = false;
		// call initial action function passing view
		this.initAction.value(holdView);
		arrViews = arrViews.add(holdView);
//		onScreen = true;
		// add screen update function
		system.addScreenUpdFunc(
			[holdView],
			{ arg argArray;
				var holdView = argArray.at(0);
				holdView.string = this.getString ? "";
			}
		);
	}
	getString {
		var holdModuleID, holdModule, holdActionInd, holdAction, holdString;
		holdModuleID = arrActions.at(0).at(0);
		holdActionInd = arrActions.at(0).at(1);
		holdModule = system.getModuleFromID(holdModuleID);
		if (holdModule != 0, {
			holdAction = holdModule.arrActionSpecs
				.select({arg action, i; action.actionType == \valueAction; })
				.select({arg action, i; action.guiObjectType == this.guiObjectType})
				.at(holdActionInd);
			if (holdAction.notNil, {holdString = holdAction.getValueFunction.value;});
		});

		^holdString;
	}
	initAction {
		var holdModuleID, holdModule, holdActionInd, holdAction, holdInitAction;
		holdModuleID = arrActions.at(0).at(0);
		holdActionInd = arrActions.at(0).at(1);
		holdModule = system.getModuleFromID(holdModuleID);
		if (holdModule != 0, {
			holdAction = holdModule.arrActionSpecs
				.select({arg action, i; action.actionType == \valueAction; })
				.select({arg action, i; action.guiObjectType == this.guiObjectType})
				.at(holdActionInd);
			if (holdAction.notNil, {holdInitAction = holdAction.initActionFunc;});
		});
		^holdInitAction;
	}

}

TXWIPAddress : TXWNumberBox {

	classvar <widgetName;

	*initClass{
		// initialise class variables
		widgetName = "IP Address";
	}
	init { arg argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs;
		var arrDefaults;
		//	assign variables
		arrDefaults = [ [[99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil],
			[99,0,0,0,0,0,0, nil], [99,0,0,0,0,0,0, nil]],
			TXColour.black, "Arial", 12];
		arrExtraArgs = arrExtraArgs ? arrDefaults;
		arrActions = arrExtraArgs.at(0) ? arrDefaults.at(0);
		stringColor = arrExtraArgs.at(1) ? arrDefaults.at(1);
		font = arrExtraArgs.at(2) ? arrDefaults.at(2);
		fontSize = arrExtraArgs.at(3) ? arrDefaults.at(3);
		argHeight = argHeight ? 20;
		argWidth = argWidth ? 260;
		widthMin = 260;
		//	call super.init
		super.init(argLayoutX, argLayoutY, argHeight, argWidth, arrExtraArgs);
		guiObjectType = \ipaddress;
	}
	buildGui { arg window, offsetLeft, offsetTop, screenWidth, screenHeight, limitWidgetUpdates;
		var holdView, holdFromLeft, holdFromTop, holdControlSpec;
		holdFromLeft = layoutX * (screenWidth - width);
		holdFromTop = (1- layoutY) * (screenHeight - height);
		holdView = TXNetAddress(window,
			Rect(offsetLeft + holdFromLeft, offsetTop + holdFromTop, width, height),
			labelWidth: 0, showPresets: false, displayOnly: limitWidgetUpdates);
		holdView.stringColor = stringColor;
		holdView.font = Font(font, fontSize);
		holdView.background = background;
		holdView.action = {arg view;
			this.performValueActions(view.string)
		};
		holdView.stringNoAction = this.getString ? "0.0.0.0";
		arrViews = arrViews.add(holdView);
//		onScreen = true;
		// add screen update function
		system.addScreenUpdFunc(
			[holdView],
			{ arg argArray;
				var holdView = argArray.at(0);
				holdView.stringNoAction = this.getString ? "";
			}
		);
	}
	getString {
		var holdModuleID, holdModule, holdActionInd, holdAction, holdString;
		holdModuleID = arrActions.at(0).at(0);
		holdActionInd = arrActions.at(0).at(1);
		holdModule = system.getModuleFromID(holdModuleID);
		if (holdModule != 0, {
			holdAction = holdModule.arrActionSpecs
				.select({arg action, i; action.actionType == \valueAction; })
				.select({arg action, i; action.guiObjectType == this.guiObjectType})
				.at(holdActionInd);
			if (holdAction.notNil, {holdString = holdAction.getValueFunction.value;});
		});
		^holdString;
	}
}
