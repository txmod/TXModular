// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXOSCTrigActions {

	classvar actionClipboard;

	var <>arrOSCTrigActions, <>action, <>scrollView, actionCount, system, nextStepIDFunc,
			getCurrentStepIDAction, setCurrentStepIDAction, addResponderAction, removeResponderAction,
			defaultOSCTrigAction, holdBackgroundBox;
	var	arrModules, arrModuleNames, actionData, actionArrays;

	*new { arg argSystem, argParent, dimensions, argArrOSCTrigActions, argAction, argNextStepIDFunc,
			scrollViewAction, getCurrentStepIDAction, setCurrentStepIDAction,
			addResponderAction, removeResponderAction;
		^super.new.init(argSystem, argParent, dimensions, argArrOSCTrigActions, argAction, argNextStepIDFunc,
			scrollViewAction, getCurrentStepIDAction, setCurrentStepIDAction,
			addResponderAction, removeResponderAction);
	}
	init { arg argSystem, argParent, dimensions, argArrOSCTrigActions, argAction, argNextStepIDFunc,
			scrollViewAction, argGetCurrentStepIDAction, argSetCurrentStepIDAction,
			argAddResponderAction, argRemoveResponderAction;

		var holdView, holdActionText, prevOSCString, scrollBox, holdParent, btnAddNew;

		defaultOSCTrigAction = [99,0,0,0,0,0,0, nil, "/example/text", 0, 0, 0, 0];
		// for reference:
		// oscTrigAction.at(0) is ModuleID
		// oscTrigAction.at(1) is Action Index
		// oscTrigAction.at(2) is value 1
		// oscTrigAction.at(3) is value 2
		// oscTrigAction.at(4) is value 3
		// oscTrigAction.at(5) is value 4
		// oscTrigAction.at(6) is stepID
		// oscTrigAction.at(7) is Action Text
		// oscTrigAction.at(8) is OSC String
		// oscTrigAction.at(9) is Active
		// oscTrigAction.at(10) is Triggering Type
		// oscTrigAction.at(11) is Use Args
		// oscTrigAction.at(12) is First Arg

		arrOSCTrigActions = argArrOSCTrigActions;
		action = argAction;
		system = argSystem;
		nextStepIDFunc = argNextStepIDFunc;
		getCurrentStepIDAction = argGetCurrentStepIDAction;
		setCurrentStepIDAction = argSetCurrentStepIDAction;
		addResponderAction = argAddResponderAction;
		removeResponderAction = argRemoveResponderAction;

		arrModules = system.arrWidgetActionModules;
		arrModuleNames = arrModules.collect({arg item, i; item.instName;});
		actionData = ();

		// button - add new
		btnAddNew = Button(argParent, 12 @ 20)
			.states_([
				["+", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				var holdNewOSCTrigAction;
				holdNewOSCTrigAction = this.addOSCTrigAction("/");
				this.setCurrentStepID(holdNewOSCTrigAction.at(6));
				// update view
				system.showView;
			});

		// text label
		StaticText(argParent, Rect(0, 0, 303, 20))
			.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
			.align_(\centre)
			.string_("add del    OSC string (sorted alphabetically)           on-off");

		// text label
		StaticText(argParent, Rect(0, 0, 194, 20))
			.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
			.align_(\centre)
			.string_("trigger type             use args from no");

		// text label
		StaticText(argParent, Rect(0, 0, 153, 20))
			.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
			.align_(\centre)
			.string_("module");

		// text label
		holdView = StaticText(argParent, Rect(0, 0, 285, 20))
			.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
			.align_(\centre)
			.string_("action");

		// text label
		StaticText(argParent, Rect(0, 0, 200, 20))
			.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
			.align_(\centre)
			.string_("value settings");

		prevOSCString = "";
		if (scrollViewAction.notNil, {
			// add ScrollView
			scrollView = ScrollView(argParent, Rect(0, 0, dimensions.x, dimensions.y))
				.hasBorder_(false);
			scrollView.background_(TXColor.sysModuleWindow);
			scrollView.action = scrollViewAction;
			if (GUI.current.asSymbol == \cocoa, {
				scrollView.autoScrolls_(false);
			});
			scrollView.hasHorizontalScroller = false;
			scrollView.hasVerticalScroller = true;
			scrollBox = CompositeView(scrollView, Rect(0, 0, 100 + dimensions.x,
				150 + (arrOSCTrigActions.size * 24)));
			scrollBox.decorator = FlowLayout(scrollBox.bounds);
			scrollBox.decorator.margin.x = 0;
			scrollBox.decorator.margin.y = 0;
			scrollBox.decorator.reset;
		});
		holdParent = scrollBox ? argParent;
		// display action steps

		arrOSCTrigActions.size.do({ arg item, i;
			var arrActionItems, arrLegacyActionItems;
			var holdModuleID, holdModule;
			var holdControlSpec1, holdControlSpec2, holdControlSpec3, holdControlSpec4, holdArrActionSpecs;
			var btnAdd, btnDelete, chkboxActive, txtOSCString, popupTrigType;
//			var chkboxUpd, popupStep, labelStepID;
//			var numboxMins, numboxSecs, numboxBars, numboxBeats;
//			var chkboxOnOff, numboxProb, popupProb, btnCopy, btnPaste;
			var moduleView, moduleActionView;
			var val1NumberBox, val1Slider, val2NumberBox, val3NumberBox, val4NumberBox, valPopup;
			var valCheckbox, valTextbox, chkboxUseArgs, popupFirstArg;
			var holdOSCTrigAction, isCurrentStep;

			holdOSCTrigAction = arrOSCTrigActions.at(item);

			// go to next line
			holdParent.decorator.nextLine;

			// if OSC String is different to previous step draw a line
			if (holdOSCTrigAction.at(8) != prevOSCString, {
				holdParent.decorator.shift(0,2);
				StaticText(holdParent, 1200 @ 1).background_(TXColor.white);
				holdParent.decorator.nextLine;
				holdParent.decorator.shift(0,2);
			});
			prevOSCString = holdOSCTrigAction.at(8);
			// if current step highlight with box
			if (this.getCurrentStepID == holdOSCTrigAction.at(6), {
				isCurrentStep = true;
				// shift decorator
				holdParent.decorator.shift(0, -4);
				// draw a background box
				holdBackgroundBox = StaticText(holdParent, dimensions.x @ 124);
				holdBackgroundBox.background_(TXColor.sysViewHighlight);
				// shift decorator back
				holdParent.decorator.nextLine;
				holdParent.decorator.shift(0, -124);
			},{
				isCurrentStep = false;
			});

			// button - highlight
			Button(holdParent, 8 @ 20)
			.states_([
				["", TXColor.white, TXColor.sysViewHighlight]
			])
			.action_({|view|
				if (isCurrentStep, {
					this.setCurrentStepID(0);
				},{
					this.setCurrentStepID(holdOSCTrigAction.at(6));
				});
				// update view
				system.showView;
			});
			// button - add
			btnAdd = Button(holdParent, 12 @ 20)
				.states_([
					["+", TXColor.white, TXColor.sysGuiCol1]
				])
				.action_({|view|
					var holdNewOSCTrigAction;
					holdNewOSCTrigAction = this.addOSCTrigAction(holdOSCTrigAction.at(8));
					this.setCurrentStepID(holdNewOSCTrigAction.at(6));
					// update view
					system.showView;
				});

			// button - delete
			btnDelete = Button(holdParent, 12 @ 20)
				.states_([
					["-", TXColor.white, TXColor.sysDeleteCol]
				])
				.action_({|view|
					this.setCurrentStepID(-1);
					this.deleteOSCTrigAction(holdOSCTrigAction);
					// update view
					system.showView;
				});

			// text box - OSC String
			txtOSCString = TextField(holdParent, Rect(0, 0, 246, 20));
			txtOSCString.action = {arg view;
				var holdString;
				holdString = view.string;
				if ( (holdString == "") or: (holdString == " "), {
					holdString = "/";
				});
				this.setCurrentStepID(holdOSCTrigAction.at(6));
				holdOSCTrigAction[8] = holdString;
				this.sortArrOSCTrigActions;
				// if active then recreate responder
				if (holdOSCTrigAction[9] == 1, {
					addResponderAction.value(holdOSCTrigAction);
				});
				this.update;
				// update view
				system.showView;
			};
			txtOSCString.string =  holdOSCTrigAction[8];

			// checkbox - active
			chkboxActive = TXCheckBox(holdParent, 23 @ 20, "", TXColor.sysGuiCol1, TXColour.white,
				TXColor.white, TXColor.sysGuiCol1)
				.value_ (holdOSCTrigAction.at(9))
				.action_ {|view|
					this.setCurrentStepID(holdOSCTrigAction.at(6));
					holdOSCTrigAction.put(9, view.value);
					if (view.value == 1, {
						addResponderAction.value(holdOSCTrigAction);
					},{
						removeResponderAction.value(holdOSCTrigAction);
					});
				};

			// popup - trip type
			popupTrigType = PopUpMenu(holdParent, Rect(0, 0, 130, 20))
				.background_(TXColor.white).stringColor_(TXColor.black)
				.items_(["Always trigger", "Trigger if arg=1", "Trigger if arg=0"])
				.action_({arg view;
					var holdAction;
					this.setCurrentStepID(holdOSCTrigAction.at(6));
					arrOSCTrigActions.at(i).put(10, view.value);
					// update view
					system.showView;
				});
			popupTrigType.value =  holdOSCTrigAction[10];


			// checkbox - Use Args
			chkboxUseArgs = TXCheckBox(holdParent, 23 @ 20, "", TXColor.sysGuiCol1, TXColour.white,
				TXColor.white, TXColor.sysGuiCol1)
				.value_ (holdOSCTrigAction.at(11))
				.action_ {|view|
					this.setCurrentStepID(holdOSCTrigAction.at(6));
					holdOSCTrigAction.put(11, view.value);
				};
			chkboxUseArgs.value = holdOSCTrigAction[11];

			// // popup - first arg
			// popupFirstArg = PopUpMenu(holdParent, Rect(0, 0, 30, 20))
			// .background_(TXColor.white).stringColor_(TXColor.black)
			// .items_(16.collect({ arg item, i; (item+1).asString; }))
			// .action_({arg view;
			// 	var holdAction;
			// 	this.setCurrentStepID(holdOSCTrigAction.at(6));
			// 	arrOSCTrigActions.at(i).put(12, view.value);
			// 	// update view
			// 	system.showView;
			// });
			// popupFirstArg.value =  holdOSCTrigAction[12];

			// NumberBox - first arg
			popupFirstArg = NumberBox(holdParent, Rect(0, 0, 30, 20))
			.background_(TXColor.white).stringColor_(TXColor.black)
			.clipLo_(1).clipHi_(16)
			.action_({arg view;
				var holdAction;
				this.setCurrentStepID(holdOSCTrigAction.at(6));
				arrOSCTrigActions.at(i).put(12, view.value-1);
				// update view
				system.showView;
			});
			popupFirstArg.value =  holdOSCTrigAction[12] + 1;

			// TXListView/Popup - module
			if (isCurrentStep, {
				moduleView = TXListView(holdParent, Rect(0, 0, 155, 116));
			},{
				moduleView = PopUpMenu(holdParent, Rect(0, 0, 155, 20));
			});
			moduleView.background_(TXColor.white).stringColor_(TXColor.black);
			moduleView.items_(arrModuleNames);
			moduleView.action_({arg view;
				var holdAction;
				arrOSCTrigActions.at(i).put(0, arrModules.at(view.value).moduleID);
				arrOSCTrigActions.at(i).put(1, 0);
				arrOSCTrigActions.at(i).put(7, nil);
				{
					// update view
					this.setCurrentStepID(holdOSCTrigAction.at(6));
					system.showView;
				}.defer(0.02);
			});
			//moduleView.beginDragAction = {arg view; nil;}; // disable drag - can cause crashes
			holdModuleID = arrOSCTrigActions.at(i).at(0);
			holdModule = system.getModuleFromID(holdModuleID);
			if (holdModule == 0, {holdModule = system});
			moduleView.value =  arrModules.indexOf(holdModule) ? 0;

			// TXListView/Popup - action
			actionArrays = this.getActionArrays(holdModule);
			holdArrActionSpecs = actionArrays[0];
			arrActionItems = actionArrays[1];
			arrLegacyActionItems = actionArrays[2];
			if (isCurrentStep, {
				moduleActionView = TXListView(holdParent, Rect(0, 0, 285, 116));
			},{
				moduleActionView = PopUpMenu(holdParent, Rect(0, 0, 285, 20));
			});
			moduleActionView.background_(TXColor.white).stringColor_(TXColor.black);
			moduleActionView.items_(arrActionItems);
			moduleActionView.action_({arg view;
				var holdAction;
				// popup value and text are stored
				arrOSCTrigActions.at(i).put(1, view.value);
				if (arrOSCTrigActions.at(i).size<8, {
					holdAction = arrOSCTrigActions.at(i).deepCopy;
					holdAction = holdAction.addAll([nil, nil, nil, nil, nil, nil]);
					arrOSCTrigActions.put(i, holdAction.deepCopy);
				});
				arrOSCTrigActions.at(i).put(7, arrActionItems.at(view.value));
				// default argument values are stored
				// arg 1
				if (holdModule.arrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 0, {
					arrOSCTrigActions.at(i).put(2,
						holdModule.arrActionSpecs.at(moduleActionView.value)
						.arrControlSpecFuncs.at(0).value.default);
				});
				// arg 2
				if (holdModule.arrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 1, {
					arrOSCTrigActions.at(i).put(3,
						holdModule.arrActionSpecs.at(moduleActionView.value)
						.arrControlSpecFuncs.at(1).value.default);
				});
				// arg 3
				if (holdModule.arrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 2, {
					arrOSCTrigActions.at(i).put(4,
						holdModule.arrActionSpecs.at(moduleActionView.value)
						.arrControlSpecFuncs.at(2).value.default);
				});
				// arg 4
				if (holdModule.arrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 3, {
					arrOSCTrigActions.at(i).put(5,
						holdModule.arrActionSpecs.at(moduleActionView.value)
						.arrControlSpecFuncs.at(3).value.default);
				});
				{
					// update view
					this.setCurrentStepID(holdOSCTrigAction.at(6));
					system.showView;
				}.defer(0.02);
			});
			// moduleActionView.beginDragAction = {arg view; nil;}; // disable drag - can cause crashes
			// if text found, match action string with text, else use numerical value
			if (arrOSCTrigActions.at(i).at(7).notNil, {
				moduleActionView.value = arrActionItems.indexOfEqual(arrOSCTrigActions.at(i).at(7)) ? 0;
			},{
				// legacy code
				holdActionText = arrLegacyActionItems.at(arrOSCTrigActions.at(i).at(1) ? 0);
				moduleActionView.value = arrActionItems.indexOfEqual(holdActionText) ? 0;
			});

			// show value settings
			// if only 1 controlspec is given, then create slider
			if (holdModule.arrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size == 1, {
			// slider - value 1
				holdControlSpec1 =
					holdModule.arrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.at(0);
				val1Slider = Slider(holdParent, Rect(0, 0, 145, 20))
				.action_({arg view;
					arrOSCTrigActions.at(i)
						.put(2, holdControlSpec1.value.map(view.value));
					if (val1NumberBox.class.respondsTo('value'),
						{val1NumberBox.value = holdControlSpec1.value.map(view.value);})
				})
				.mouseUpAction_({arg view;
					this.setCurrentStepID(holdOSCTrigAction.at(6));
				});
				if (holdControlSpec1.value.step != 0, {
					val1Slider.step = (holdControlSpec1.value.step
						/ (holdControlSpec1.value.maxval - holdControlSpec1.value.minval));
				});
				val1Slider.value = holdControlSpec1.value.unmap(
					arrOSCTrigActions.at(i).at(2) ? 0);
			});
			// if object type is number
			if (holdModule.arrActionSpecs.at(moduleActionView.value).guiObjectType == \number, {
				// if at least 1 controlspec is given, then create numberbox
				if (holdModule.arrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 0, {
					holdControlSpec1 =
					holdModule.arrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.at(0);
					val1NumberBox = NumberBox(holdParent, Rect(0, 0, 55, 20), holdControlSpec1)
					.maxDecimals_(4)
					.action_({arg view;
						this.setCurrentStepID(holdOSCTrigAction.at(6));
						view.value = holdControlSpec1.value.constrain(view.value);
						arrOSCTrigActions.at(i).put(2, view.value);
						if (val1Slider.class == Slider,
							{val1Slider.value = holdControlSpec1.value.unmap(view.value);})
					});
					val1NumberBox.value = holdControlSpec1.value.constrain(
						arrOSCTrigActions.at(i).at(2) ? holdControlSpec1.value.default);
				})
			});
			// popup
			if (holdModule.arrActionSpecs.at(moduleActionView.value).guiObjectType == \popup, {
				valPopup = PopUpMenu(holdParent, Rect(0, 0, 240, 20))
				.stringColor_(TXColour.black).background_(TXColor.white);
				valPopup.items =
				holdModule.arrActionSpecs.at(moduleActionView.value).getItemsFunction.value;
				valPopup.action = {arg view;
					this.setCurrentStepID(holdOSCTrigAction.at(6));
					arrOSCTrigActions.at(i).put(2, view.value);
				};
				valPopup.value = arrOSCTrigActions.at(i).at(2) ? 0;
			});

			// checkbox
			if (holdModule.arrActionSpecs.at(moduleActionView.value).guiObjectType == \checkbox, {
				valCheckbox = TXCheckBox(holdParent, Rect(0, 0, 60, 20),
					" ", TXColour.black, TXColor.white,
					TXColour.black, TXColor.white, 7);
				valCheckbox.action = {arg view;
					this.setCurrentStepID(holdOSCTrigAction.at(6));
					arrOSCTrigActions.at(i).put(2, view.value);
				};
				valCheckbox.value = arrOSCTrigActions.at(i).at(2) ? 0;
			});

			// textbox
			if (holdModule.arrActionSpecs.at(moduleActionView.value).guiObjectType == \textedit, {
				valTextbox = TextField(holdParent, Rect(0, 0, 240, 20),
					" ", TXColour.black, TXColor.white,
					TXColour.black, TXColor.white, 4);
				valTextbox.action = {arg view;
					this.setCurrentStepID(holdOSCTrigAction.at(6));
					arrOSCTrigActions.at(i).put(2, view.string);
				};
				valTextbox.string = arrOSCTrigActions.at(i).at(2) ? "";
			});

			// if more than 1 control spec given, then create extra numberbox
			if (holdArrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 1, {
			// numberbox - value 2
				holdControlSpec2 =
					holdArrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.at(1);
				val2NumberBox = NumberBox(holdParent, Rect(0, 0, 55, 20), holdControlSpec2)
				.maxDecimals_(4)
				.action_({arg view;
					this.setCurrentStepID(holdOSCTrigAction.at(6));
					view.value = holdControlSpec2.value.constrain(view.value);
					arrOSCTrigActions.at(i).put(3, view.value);
				});
				if (arrOSCTrigActions.at(i).at(3).notNil, {
					val2NumberBox.value = holdControlSpec2.value.constrain(
						arrOSCTrigActions.at(i).at(3));
					arrOSCTrigActions.at(i).put(3, val2NumberBox.value);
				},{
					val2NumberBox.value = holdControlSpec2.default;
					arrOSCTrigActions.at(i).put(3, holdControlSpec2.default);
				});
			});
			// numberbox - value 3
			// if more than 2 controlspecs given, then create extra numberbox
			if (holdArrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 2, {
				holdControlSpec3 =
					holdArrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.at(2);
				val3NumberBox = NumberBox(holdParent, Rect(0, 0, 55, 20), holdControlSpec3)
				.maxDecimals_(4)
				.action_({arg view;
					this.setCurrentStepID(holdOSCTrigAction.at(6));
					view.value = holdControlSpec3.value.constrain(view.value);
					arrOSCTrigActions.at(i).put(4, view.value);
				});
				if (arrOSCTrigActions.at(i).at(4).notNil, {
					val3NumberBox.value = holdControlSpec3.value.constrain(
						arrOSCTrigActions.at(i).at(4));
					arrOSCTrigActions.at(i).put(4, val3NumberBox.value);
				},{
					val3NumberBox.value = holdControlSpec3.default;
					arrOSCTrigActions.at(i).put(4, holdControlSpec3.default);
				});
			});
			// numberbox - value 4
			// if more than 3 controlspecs given, then create extra numberbox
			if (holdArrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 3, {
				holdControlSpec4 =
					holdArrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.at(3);
				val4NumberBox = NumberBox(holdParent, Rect(0, 0, 55, 20), holdControlSpec4)
				.maxDecimals_(4)
				.action_({arg view;
					this.setCurrentStepID(holdOSCTrigAction.at(6));
					view.value = holdControlSpec4.value.constrain(view.value);
					arrOSCTrigActions.at(i).put(5, view.value);
				});
				if (arrOSCTrigActions.at(i).at(5).notNil, {
					val4NumberBox.value = holdControlSpec4.value.constrain(
						arrOSCTrigActions.at(i).at(5));
					arrOSCTrigActions.at(i).put(5, val4NumberBox.value);
				},{
					val4NumberBox.value = holdControlSpec4.default;
					arrOSCTrigActions.at(i).put(5, holdControlSpec4.default);
				});
			});

		}); // end of arrOSCTrigActions.size.do

		// draw final line
		holdParent.decorator.nextLine;
		holdParent.decorator.shift(0,2);
		StaticText(holdParent, 1200 @ 1).background_(TXColor.white);
		holdParent.decorator.nextLine;
		holdParent.decorator.shift(0,2);
		// dummy text as spacer
		StaticText(holdParent, Rect(0, 0, 20, 20));
	}

	update { // copies arrOSCTrigActions back to original source
		action.value(arrOSCTrigActions);
	}

	deleteOSCTrigAction { arg argOSCTrigAction;
		// if active then remove responder
		if (argOSCTrigAction[9] == 1, {
			removeResponderAction.value(argOSCTrigAction);
		});
		arrOSCTrigActions.remove(argOSCTrigAction);
		this.update;
	}

	addOSCTrigAction { arg argOSCString;
		var newOSCTrigAction;
		newOSCTrigAction = defaultOSCTrigAction.deepCopy
			.put(6, nextStepIDFunc.value)
			.put(8, argOSCString);
		arrOSCTrigActions = arrOSCTrigActions.add(newOSCTrigAction);
		this.sortArrOSCTrigActions;
		this.update;
		^newOSCTrigAction;
	}

	getActionArrays { arg argModule;
		var holdID, arrActionSpecs, arrActionItems, arrLegacyActionItems;
		holdID = argModule.moduleID;
		if (actionData[holdID].isNil, {
			arrActionSpecs = argModule.arrActionSpecs;
			arrActionItems = arrActionSpecs
				.collect({arg item, i; item.actionName;});
			arrLegacyActionItems = arrActionSpecs .select({arg item, i; item.legacyType == 1})
				.collect({arg item, i; item.actionName;});
			actionData[holdID] = [arrActionSpecs, arrActionItems, arrLegacyActionItems];
		});
		^actionData[holdID];
	}

	getCurrentStepID {
		^getCurrentStepIDAction.value;
	}

	setCurrentStepID {arg stepID;
		if (getCurrentStepIDAction.value != stepID, {
			setCurrentStepIDAction.value(stepID);
			// update view
			system.showView;
		});
	}

	sortArrOSCTrigActions {
		// sort by osc string and stepID
		arrOSCTrigActions = arrOSCTrigActions.sort({ arg a, b; (a[8]++a[6]) < (b[8]++b[6]) });
	}
}
