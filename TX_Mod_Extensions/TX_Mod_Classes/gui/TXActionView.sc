// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXActionView {	// self-building module popup, action popup, and value fields
	var <>labelView, <>labelView2, <>labelView3;
	var arrModules, <>modulePopup, arrActionItems, arrLegacyActionItems, <>actionPopup;
	var holdModuleID, holdModule, holdActionText, holdActionNoString, resetButton;
	var holdControlSpec1, holdControlSpec2, holdControlSpec3, holdControlSpec4, holdArrActionSpecs;
	var val1NumberBox, val1Slider, val2NumberBox, val3NumberBox, val4NumberBox, valPopup;
	var valCheckbox, valTextbox, holdArrActions, i;

	*new { arg argParent, dimensions, arrActions, actionIndex, labelWidth=80, system;
		^super.new.init(argParent, dimensions, arrActions, actionIndex, labelWidth, system);
	}
	init { arg argParent, dimensions, arrActions, actionIndex, labelWidth, system;

		holdArrActions = arrActions;
		i = actionIndex.asInteger;
		holdActionNoString = (actionIndex + 1).asString;

		// label
		labelView = StaticText(argParent, labelWidth @ dimensions.y);
		labelView.string = "Module " ++ holdActionNoString;
		labelView.align = \right;

		// popup - module
		arrModules = system.arrWidgetActionModules;
		modulePopup = PopUpMenu(argParent, Rect(0, 0, 150, 20))
			.background_(TXColor.white).stringColor_(TXColor.black)
			.items_(arrModules.collect({arg item, i; item.instName;}))
			.action_({arg view;
				var holdAction;
				holdArrActions.at(i)
					.put(0, arrModules.at(view.value).moduleID);
				holdArrActions.at(i).put(1, 0);
				if (holdArrActions.at(i).size<8, {
					holdAction = holdArrActions.at(i).deepCopy;
					holdAction = holdAction.addAll([nil, nil, nil, nil, nil, nil]);
					holdArrActions.put(i, holdAction.deepCopy);
				},{
					holdArrActions.at(i).put(7, " ");
				});
				// update view
				system.showView;
			});
		holdModuleID = holdArrActions.at(i).at(0);
		holdModule = system.getModuleFromID(holdModuleID);
		if (holdModule == 0, {holdModule = system});
		modulePopup.value = arrModules.indexOf(holdModule) ? 0;

		// create button
		resetButton = Button(argParent, 50 @ 20)
		.states_([["Reset", TXColor.white, TXColor.sysGuiCol1]])
		.action_({
			modulePopup.valueAction_(0);
			actionPopup.valueAction_(0);
		});

		// decorator next line
		if (argParent.class == Window, {
			argParent.view.decorator.nextLine;
		}, {
			argParent.decorator.nextLine;
		});
		// label
		labelView2 = StaticText(argParent, labelWidth @ dimensions.y);
		labelView2.string = "Action " ++ holdActionNoString;
		labelView2.align = \right;

		// popup - action
		holdArrActionSpecs = holdModule.arrActionSpecs;
		arrActionItems = holdArrActionSpecs
			.collect({arg item, i; item.actionName;});
		arrLegacyActionItems = holdArrActionSpecs .select({arg item, i; item.legacyType == 1})
			.collect({arg item, i; item.actionName;});

		actionPopup = PopUpMenu(argParent, Rect(0, 0, 300, 20))
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

		// decorator next line
		if (argParent.class == Window, {
			argParent.view.decorator.nextLine;
		}, {
			argParent.decorator.nextLine;
		});
		// label
		labelView3 = StaticText(argParent, labelWidth @ dimensions.y);
		labelView3.string = "Settings " ++ holdActionNoString;
		labelView3.align = \right;

		// check for no actions
		if (holdArrActionSpecs.size > 0, {
			// action widget, show value settings
			// if only 1 controlspec is given, then create slider
			if (holdModule.arrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.size == 1, {
			// slider - value 1
				holdControlSpec1 =
					holdModule.arrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.at(0);
				val1Slider = Slider(argParent, Rect(0, 0, 175, 20))
				.action_({arg view;
					holdArrActions.at(i)
						.put(2, holdControlSpec1.value.map(view.value));
					if (val1NumberBox.class.respondsTo('value'),
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
					val1NumberBox = TXScrollNumBox(argParent, Rect(0, 0, 55, 20))
					.maxDecimals_(4)
					.action_({arg view;
						view.value = holdControlSpec1.value.constrain(view.value);
						holdArrActions.at(i).put(2, view.value);
						if (val1Slider.class == Slider.redirectClass,
							{val1Slider.value = holdControlSpec1.value.unmap(view.value);})
					});
					TXScrollNumBox.updateNumberBoxFromSpec(val1NumberBox, holdControlSpec1);
					val1NumberBox.value = holdControlSpec1.value.constrain(
						holdArrActions.at(i).at(2) ? holdControlSpec1.value.default);
				});
			});
			// popup
			if (holdModule.arrActionSpecs.at(actionPopup.value).guiObjectType == \popup, {
				valPopup = PopUpMenu(argParent, Rect(0, 0, 250, 20))
					.stringColor_(TXColour.black).background_(TXColor.white);
				valPopup.items =
					holdModule.arrActionSpecs.at(actionPopup.value).getItemsFunction.value;
				valPopup.action = {arg view;
					holdArrActions.at(i).put(2, view.value);
				};
				valPopup.value = holdArrActions.at(i).at(2) ? 0;
			});

			// checkbox
			if (holdModule.arrActionSpecs.at(actionPopup.value).guiObjectType == \checkbox, {
				valCheckbox = TXCheckBox(argParent, Rect(0, 0, 60, 20),
					" ", TXColour.black, TXColor.white,
					TXColour.black, TXColor.white, 7);
				valCheckbox.action = {arg view;
					holdArrActions.at(i).put(2, view.value);
				};
				valCheckbox.value = holdArrActions.at(i).at(2) ? 0;
			});

			// textbox
			if (holdModule.arrActionSpecs.at(actionPopup.value).guiObjectType == \textedit, {
				valTextbox = TextField(argParent, Rect(0, 0, 250, 20),
					" ", TXColour.black, TXColor.white,
					TXColour.black, TXColor.white, 4);
				valTextbox.action = {arg view;
					holdArrActions.at(i).put(2, view.string);
				};
				if (holdArrActions.at(i).at(2).isString, {
					valTextbox.string = holdArrActions.at(i).at(2);
				},{
					valTextbox.string = " ";
				});
			});

			// if more than 1 control spec given, then create extra numberbox
			if (holdArrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.size > 1, {
			// numberbox - value 2
				holdControlSpec2 =
					holdArrActionSpecs.at(actionPopup.value).arrControlSpecFuncs.at(1);
				val2NumberBox = TXScrollNumBox(argParent, Rect(0, 0, 55, 20))
				.maxDecimals_(4)
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
				val3NumberBox = TXScrollNumBox(argParent, Rect(0, 0, 55, 20))
				.maxDecimals_(4)
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
				val4NumberBox = TXScrollNumBox(argParent, Rect(0, 0, 55, 20))
				.maxDecimals_(4)
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
}

}
