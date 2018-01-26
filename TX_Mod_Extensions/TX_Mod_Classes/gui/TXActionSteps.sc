// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXActionSteps {

	classvar actionClipboard;

	var <>arrActionSteps, <>action, <>firstActionInd, <>scrollView, actionCount, system,
	defaultActionStep, bpm, beatsPerBar, nextStepIDFunc,
	holdBackgroundBox, getCurrentStepIDAction, setCurrentStepIDAction,
	arrNumboxMins, arrNumboxSecs;

	*new { arg argSystem, argParent, dimensions, argArrActionSteps, argAction, argFirstActionInd,
		argBpm, argBeatsPerBar, argNextStepIDFunc, scrollViewAction, getCurrentStepIDAction,
		setCurrentStepIDAction;
		^super.new.init(argSystem, argParent, dimensions, argArrActionSteps, argAction, argFirstActionInd,
			argBpm, argBeatsPerBar, argNextStepIDFunc, scrollViewAction, getCurrentStepIDAction,
			setCurrentStepIDAction);
	}
	init { arg argSystem, argParent, dimensions, argArrActionSteps, argAction, argFirstActionInd,
		argBpm, argBeatsPerBar, argNextStepIDFunc, scrollViewAction, argGetCurrentStepIDAction,
		argSetCurrentStepIDAction;

		var holdView, holdActionText, holdStepTime, scrollBox, holdParent;

		defaultActionStep = [99,0,0,0,0,0,0, nil, 0, 0.0, 1, 100, 1, 1001];
		// actionStep.at(0) is ModuleID
		// actionStep.at(1) is Action Index
		// actionStep.at(2) is moduleID
		// actionStep.at(3) is moduleID
		// actionStep.at(4) is moduleID
		// actionStep.at(5) is not used
		// actionStep.at(6) is not used
		// actionStep.at(7) is Action Text
		// actionStep.at(8) is Update switch
		// actionStep.at(9) is Time
		// actionStep.at(10) is On switch
		// actionStep.at(11) is Probablity
		// actionStep.at(12) is Step No.
		// actionStep.at(13) is Step ID

		arrNumboxMins = [];
		arrNumboxSecs = [];
		arrActionSteps = argArrActionSteps;
		action = argAction;
		firstActionInd = (argFirstActionInd ?? 0).min(arrActionSteps.size-1).max(0);
		system = argSystem;
		bpm = argBpm;
		beatsPerBar = argBeatsPerBar;
		nextStepIDFunc = argNextStepIDFunc;
		getCurrentStepIDAction = argGetCurrentStepIDAction;
		setCurrentStepIDAction = argSetCurrentStepIDAction;

		// text label
		StaticText(argParent, Rect(0, 0, 145, 20))
		.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
		.align_(\left)
		.string_("ed add del   step no   ID ");

		// text label
		StaticText(argParent, Rect(0, 0, 140, 20))
		.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
		.align_(\left)
		.string_("mins   secs   bars   beats");

		// text label
		StaticText(argParent, Rect(0, 0, 116, 20))
		.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
		.align_(\left)
		.string_(" on  prob upd cpy pst");

		// text label
		StaticText(argParent, Rect(0, 0, 140, 20))
		.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
		.align_(\centre)
		.string_("module");

		// text label
		holdView = StaticText(argParent, Rect(0, 0, 250, 20))
		.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
		.align_(\centre)
		.string_("action");

		// text label
		StaticText(argParent, Rect(0, 0, 245, 20))
		.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
		.align_(\centre)
		.string_("value settings");

		holdStepTime = -1;
		actionCount = (arrActionSteps.size - firstActionInd).min(17);
		if (scrollViewAction.notNil, {
			// reset variables
			firstActionInd = 0;
			actionCount = arrActionSteps.size;
			// add ScrollView
			scrollView = ScrollView(argParent, Rect(0, 0, dimensions.x, dimensions.y))
			.hasBorder_(false);
			if (GUI.current.asSymbol == \cocoa, {
				scrollView.autoScrolls_(false);
			});
			scrollView.background_(TXColor.sysModuleWindow);
			scrollView.action = scrollViewAction;
			scrollView.hasHorizontalScroller = false;
			scrollView.hasVerticalScroller = true;
			scrollBox = CompositeView(scrollView, Rect(0, 0, dimensions.x, 220 + (actionCount * 33)));
			scrollBox.decorator = FlowLayout(scrollBox.bounds);
			scrollBox.decorator.margin.x = 0;
			scrollBox.decorator.margin.y = 0;
			scrollBox.decorator.reset;
		});
		holdParent = scrollBox ? argParent;
		// display action steps
		actionCount.do({ arg item, i;
			var arrModules, arrActionItems, arrLegacyActionItems;
			var holdModuleID, holdModule;
			var holdControlSpec1, holdControlSpec2, holdControlSpec3, holdControlSpec4, holdArrActionSpecs;
			var btnAdd, btnDelete, chkboxUpd, popupStep, labelStepID;
			var numboxMins, numboxSecs, numboxBars, numboxBeats;
			var chkboxOnOff, numboxProb, popupProb, btnCopy, btnPaste, moduleView, moduleActionView;
			var val1NumberBox, val1Slider, val2NumberBox, val3NumberBox, val4NumberBox, valPopup;
			var valCheckbox, valTextbox, holdArrActions, holdActionStep, isCurrentStep;

			holdArrActions = arrActionSteps.copyRange(firstActionInd, arrActionSteps.size-1);
			holdActionStep = holdArrActions.at(item);

			// go to next line
			holdParent.decorator.nextLine;

			// if time is different to previous step draw a line
			if (holdActionStep.at(9) != holdStepTime, {
				holdParent.decorator.shift(0,2);
				StaticText(holdParent, 1060 @ 1).background_(TXColor.white);
				holdParent.decorator.nextLine;
				holdParent.decorator.shift(0,2);
			});
			holdStepTime = holdActionStep.at(9);
			// if current step highlight with box
			if (this.getCurrentStepID == holdActionStep.at(13), {
				isCurrentStep = true;
				// shift decorator
				holdParent.decorator.shift(0, -4);
				// draw a background box
				holdBackgroundBox = StaticText(holdParent, 1058 @ 208);
				holdBackgroundBox.background_(TXColor.sysViewHighlight);
				// shift decorator back
				holdParent.decorator.nextLine;
				holdParent.decorator.shift(0, -208);
			},{
				isCurrentStep = false;
			});

			// button - highlight
			Button(holdParent, 12 @ 20)
			.states_([
				["", TXColor.white, TXColor.sysViewHighlight]
			])
			.action_({|view|
				if (isCurrentStep, {
					this.setCurrentStepID(0);
				},{
					this.setCurrentStepID(holdActionStep.at(13));
				});
				// update view
				system.showView;
			});

			// button - add
			btnAdd = Button(holdParent, 17 @ 20)
			.states_([
				["a", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				var holdNewActionStep;
				holdNewActionStep = this.addActionStep(holdActionStep.at(12), holdActionStep.at(9));
				this.setCurrentStepID(holdNewActionStep.at(13));
				// update view
				system.showView;
			});

			// button - delete
			btnDelete = Button(holdParent, 17 @ 20)
			.states_([
				["d", TXColor.white, TXColor.sysDeleteCol]
			])
			.action_({|view|
				this.setCurrentStepID(-1);
				this.deleteActionStep(holdActionStep);
				// update view
				system.showView;
			});

			//			// label - step
			//			labelStep = StaticText(holdParent, 40 @ 20)
			//				.string_ (holdActionStep.at(12).asString)
			//				.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
			//				.align_(\center);

			// popup - step
			popupStep = PopUpMenu(holdParent, 46 @ 20)
			.background_(TXColor.paleVioletRed).stringColor_(TXColor.black)
			.items_(
				[holdActionStep.at(12).asString]
				++ (1 .. arrActionSteps.size).collect({ arg item, i; ">" ++ item.asString;});
			)
			.action_({arg view;
				var oldStepNo, newStepNo, newTime;
				this.setCurrentStepID(holdActionStep.at(13));
				oldStepNo = holdActionStep.at(12);
				if (view.value > oldStepNo, {
					newStepNo = view.value + 0.5;
				},{
					newStepNo = view.value - 0.5;
				});
				newTime = arrActionSteps.at(view.value-1).at(9);
				holdActionStep.put(12, newStepNo).put(9, newTime);
				this.sortByStepNo;
				// update view
				system.showView;
			});

			//  text label - stepID
			labelStepID = StaticText(holdParent, Rect(0, 0, 30, 20))
			.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
			.align_(\centre)
			.string_((holdActionStep.at(13) ? " ").asString);

			// number box - minutes
			numboxMins = NumberBox(holdParent, 30 @ 20)
			.maxDecimals_(4)
			.background_(TXColor.paleTurquoise)
			.action_ {|view|
				this.setCurrentStepID(holdActionStep.at(13));
				view.value = view.value.max(0).min(100000);
				this.setBeatsFromTime(holdActionStep, numboxMins, numboxSecs);
				this.sortByTime;
				// update view
				system.showView;
			};
			// add to array
			arrNumboxMins = arrNumboxMins.add(numboxMins);

			// number box - seconds
			numboxSecs = NumberBox(holdParent, 35 @ 20)
			.maxDecimals_(4)
			.background_(TXColor.paleTurquoise)
			.action_ {|view|
				this.setCurrentStepID(holdActionStep.at(13));
				view.value = view.value.max(0).min(59);
				this.setBeatsFromTime(holdActionStep, numboxMins, numboxSecs);
				this.sortByTime;
				// update view
				system.showView;
			};
			// add to array
			arrNumboxSecs = arrNumboxSecs.add(numboxSecs);

			// number box - bars
			numboxBars = NumberBox(holdParent, 30 @ 20)
			.maxDecimals_(4)
			.background_(TXColor.paleGreen)
			.action_ {|view|
				this.setCurrentStepID(holdActionStep.at(13));
				view.value = view.value.max(0).min(100000);
				this.setTimeFromBeats(holdActionStep, numboxBars, numboxBeats);
				this.sortByTime;
				// update view
				system.showView;
			};
			// number box - beats
			numboxBeats = NumberBox(holdParent, 35 @ 20)
			.maxDecimals_(4)
			.background_(TXColor.paleGreen)
			.action_ {|view|
				this.setCurrentStepID(holdActionStep.at(13));
				view.value = view.value.max(1).min(beatsPerBar);
				this.setTimeFromBeats(holdActionStep, numboxBars, numboxBeats);
				this.sortByTime;
				// update view
				system.showView;
			};

			// call method to set mins, secs, bars, beats
			this.initTimeAndBeats(holdActionStep, numboxMins, numboxSecs, numboxBars, numboxBeats);

			// checkbox - on/off
			chkboxOnOff = TXCheckBox(holdParent, 20 @ 20, "", TXColor.sysGuiCol1, TXColour.white,
				TXColor.white, TXColor.sysGuiCol1)
			.value_ (holdActionStep.at(10))
			.action_ {|view|
				this.setCurrentStepID(holdActionStep.at(13));
				holdActionStep.put(10, view.value);
			};

			// number box - probability
			numboxProb = NumberBox(holdParent, 25 @ 20, [0, 100].asSpec)
			.value_ (holdActionStep.at(11))
			.action_ {|view|
				this.setCurrentStepID(holdActionStep.at(13));
				view.value = view.value.max(0).min(100);
				holdActionStep.put(11, view.value);
				if (holdActionStep.at(11) == 100, {
					numboxProb.background_(TXColor.paleYellow2)
				}, {
					numboxProb.background_(TXColor.paleYellow.blend(Color.white, 0.5))
				});
			};
			if (holdActionStep.at(11) == 100, {numboxProb.background_(TXColor.paleYellow2)},
				{numboxProb.background_(TXColor.paleYellow.blend(Color.white, 0.5))});

			// // popup - probability
			// popupProb = PopUpMenu(holdParent, 15 @ 20)
			// .background_(TXColor.paleYellow).stringColor_(TXColor.black)
			// .items_(
			// 	["select ..."] ++
			// 	[100, 90, 80, 75, 70, 66.6, 60, 50, 40, 33.3, 30, 25, 20, 10]
			// 	.collect({arg item, i; item.asString;});
			// )
			// .action_({arg view;
			// 	this.setCurrentStepID(holdActionStep.at(13));
			// 	if (view.value > 0, {
			// 		numboxProb.valueAction =
			// 		[100, 90, 80, 75, 70, 66.6, 60, 50, 40, 33.3, 30, 25, 20, 10]
			// 		.at(view.value - 1);
			// 	});
			// });

			// checkbox - update
			chkboxUpd = TXCheckBox(holdParent, 20 @ 20, "", TXColor.sysGuiCol1, TXColour.white,
				TXColor.white, TXColor.sysGuiCol1)
			.value_ (holdActionStep.at(8))
			.action_ {|view|
				this.setCurrentStepID(holdActionStep.at(13));
				holdActionStep.put(8, view.value);
			};

			// button - copy settings
			btnCopy = Button(holdParent, 20 @ 20)
			.states_([
				["C", TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				//this.setCurrentStepID(holdActionStep.at(13));
				actionClipboard = holdActionStep.copyRange(0, 7);
			});

			// button - paste settings
			btnPaste = Button(holdParent, 20 @ 20)
			.states_([
				["P", TXColor.white, TXColor.sysGuiCol2]
			])
			.action_({|view|
				this.setCurrentStepID(holdActionStep.at(13));
				// action function
				if (actionClipboard.notNil, {
					8.do({ arg item, i;
						holdActionStep.put(item, actionClipboard.at(item))
					});
					// update view
					system.showView;
				});
			});

			// ListView/Popup - module
			arrModules = system.arrWidgetActionModules;
			if (isCurrentStep, {
				moduleView = TXListView(holdParent, Rect(0, 0, 140, 200));
			},{
				moduleView = PopUpMenu(holdParent, Rect(0, 0, 140, 20));
			});
			moduleView.background_(TXColor.white).stringColor_(TXColor.black);
			moduleView.items_(arrModules.collect({arg item, i; item.instName;}));
			moduleView.action_({arg view;
				var holdAction;
				holdArrActions.at(i).put(0, arrModules.at(view.value).moduleID);
				holdArrActions.at(i).put(1, 0);
				holdArrActions.at(i).put(7, nil);
				{
					// update view
					this.setCurrentStepID(holdActionStep.at(13));
					system.showView;
				}.defer(0.02);
			});
			//moduleView.beginDragAction = {arg view; nil;}; // disable drag - can cause crashes
			holdModuleID = holdArrActions.at(i).at(0);
			holdModule = system.getModuleFromID(holdModuleID);
			if (holdModule == 0, {holdModule = system});
			moduleView.value =  arrModules.indexOf(holdModule) ? 0;

			// ListView/Popup - action
			holdArrActionSpecs = holdModule.arrActionSpecs;
			arrActionItems = holdArrActionSpecs
			.collect({arg item, i; item.actionName;});
			arrLegacyActionItems = holdArrActionSpecs .select({arg item, i; item.legacyType == 1})
			.collect({arg item, i; item.actionName;});
			if (isCurrentStep, {
				moduleActionView = TXListView(holdParent, Rect(0, 0, 250, 200));
			},{
				moduleActionView = PopUpMenu(holdParent, Rect(0, 0, 250, 20));
			});
			moduleActionView.background_(TXColor.white).stringColor_(TXColor.black)
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
				if (holdModule.arrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 0, {
					holdArrActions.at(i).put(2,
						holdModule.arrActionSpecs.at(moduleActionView.value)
						.arrControlSpecFuncs.at(0).value.default);
				});
				// arg 2
				if (holdModule.arrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 1, {
					holdArrActions.at(i).put(3,
						holdModule.arrActionSpecs.at(moduleActionView.value)
						.arrControlSpecFuncs.at(1).value.default);
				});
				// arg 3
				if (holdModule.arrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 2, {
					holdArrActions.at(i).put(4,
						holdModule.arrActionSpecs.at(moduleActionView.value)
						.arrControlSpecFuncs.at(2).value.default);
				});
				// arg 4
				if (holdModule.arrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 3, {
					holdArrActions.at(i).put(5,
						holdModule.arrActionSpecs.at(moduleActionView.value)
						.arrControlSpecFuncs.at(3).value.default);
				});
				{
					// update view
					this.setCurrentStepID(holdActionStep.at(13));
					system.showView;
				}.defer(0.02);
			});
			//moduleActionView.beginDragAction = {arg view; nil;}; // disable drag - can cause crashes
			// if text found, match action string with text, else use numerical value
			if (holdArrActions.at(i).at(7).notNil, {
				moduleActionView.value = arrActionItems.indexOfEqual(holdArrActions.at(i).at(7)) ? 0;
			},{
				holdActionText = arrLegacyActionItems.at(holdArrActions.at(i).at(1) ? 0);
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
					holdArrActions.at(i)
					.put(2, holdControlSpec1.value.map(view.value));
					if (val1NumberBox.class.respondsTo('value'),
						{val1NumberBox.value = holdControlSpec1.value.map(view.value);})
				})
				.mouseUpAction_({arg view;
					this.setCurrentStepID(holdActionStep.at(13));
				});
				if (holdControlSpec1.value.step != 0, {
					val1Slider.step = (holdControlSpec1.value.step
						/ (holdControlSpec1.value.maxval - holdControlSpec1.value.minval));
				});
				val1Slider.value = holdControlSpec1.value.unmap(
					holdArrActions.at(i).at(2) ? 0);
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
						this.setCurrentStepID(holdActionStep.at(13));
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
			if (holdModule.arrActionSpecs.at(moduleActionView.value).guiObjectType == \popup, {
				valPopup = PopUpMenu(holdParent, Rect(0, 0, 240, 20))
				.stringColor_(TXColour.black).background_(TXColor.white);
				valPopup.items =
				holdModule.arrActionSpecs.at(moduleActionView.value).getItemsFunction.value;
				valPopup.action = {arg view;
					this.setCurrentStepID(holdActionStep.at(13));
					holdArrActions.at(i).put(2, view.value);
				};
				valPopup.value = holdArrActions.at(i).at(2) ? 0;
			});

			// checkbox
			if (holdModule.arrActionSpecs.at(moduleActionView.value).guiObjectType == \checkbox, {
				valCheckbox = TXCheckBox(holdParent, Rect(0, 0, 60, 20),
					" ", TXColour.black, TXColor.white,
					TXColour.black, TXColor.white, 7);
				valCheckbox.action = {arg view;
					this.setCurrentStepID(holdActionStep.at(13));
					holdArrActions.at(i).put(2, view.value);
				};
				valCheckbox.value = holdArrActions.at(i).at(2) ? 0;
			});

			// textbox
			if (holdModule.arrActionSpecs.at(moduleActionView.value).guiObjectType == \textedit, {
				valTextbox = TextField(holdParent, Rect(0, 0, 240, 20),
					" ", TXColour.black, TXColor.white,
					TXColour.black, TXColor.white, 4);
				valTextbox.action = {arg view;
					this.setCurrentStepID(holdActionStep.at(13));
					holdArrActions.at(i).put(2, view.string);
				};
				valTextbox.string = holdArrActions.at(i).at(2) ? "";
			});

			// if more than 1 control spec given, then create extra numberbox
			if (holdArrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 1, {
				// numberbox - value 2
				holdControlSpec2 =
				holdArrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.at(1);
				val2NumberBox = NumberBox(holdParent, Rect(0, 0, 55, 20), holdControlSpec2)
				.maxDecimals_(4)
				.action_({arg view;
					this.setCurrentStepID(holdActionStep.at(13));
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
			if (holdArrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 2, {
				holdControlSpec3 =
				holdArrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.at(2);
				val3NumberBox = NumberBox(holdParent, Rect(0, 0, 55, 20), holdControlSpec3)
				.maxDecimals_(4)
				.action_({arg view;
					this.setCurrentStepID(holdActionStep.at(13));
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
			if (holdArrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.size > 3, {
				holdControlSpec4 =
				holdArrActionSpecs.at(moduleActionView.value).arrControlSpecFuncs.at(3);
				val4NumberBox = NumberBox(holdParent, Rect(0, 0, 55, 20), holdControlSpec4)
				.maxDecimals_(4)
				.action_({arg view;
					this.setCurrentStepID(holdActionStep.at(13));
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

		}); // end of actionCount.do

		// draw final line
		holdParent.decorator.nextLine;
		holdParent.decorator.shift(0,2);
		StaticText(holdParent, 1060 @ 1).background_(TXColor.white);
		holdParent.decorator.nextLine;
		holdParent.decorator.shift(0,2);
		// dummy test as spacer
		StaticText(holdParent, Rect(0, 0, 20, 20));
	}

	update { // copies arrActionSteps back to original source
		action.value(arrActionSteps);
	}

	sortByStepNo {
		arrActionSteps = arrActionSteps.sort({ arg a, b; a.at(12) <= b.at(12) });
		this.renumberSteps;
	}

	sortByTime {
		arrActionSteps = arrActionSteps.sort({ arg a, b; a.at(9) <= b.at(9) });
		this.renumberSteps;
	}

	renumberSteps {
		arrActionSteps = arrActionSteps.collect({ arg item, i; item.put(12, i+1); });
	}

	initTimeAndBeats { arg argActionStep, numboxMins, numboxSecs, numboxBars, numboxBeats;
		var holdTotalSecs, holdTotalBeats;
		holdTotalSecs = argActionStep.at(9);
		numboxMins.value = holdTotalSecs div: 60;
		numboxSecs.value = holdTotalSecs % 60;
		holdTotalBeats = (holdTotalSecs * (bpm / 60)).round(0.001);
		numboxBars.value = holdTotalBeats div: beatsPerBar;
		numboxBeats.value = (holdTotalBeats % beatsPerBar) + 1;
	}

	setTimeFromBeats {	arg argActionStep, numboxBars, numboxBeats;
		var holdTotalSecs, holdTotalBeats;
		holdTotalBeats = (numboxBars.value * beatsPerBar) + (numboxBeats.value - 1);
		holdTotalSecs = holdTotalBeats / (bpm / 60);
		argActionStep.put(9, holdTotalSecs);
		this.update;
	}

	setBeatsFromTime {	arg argActionStep, numboxMins, numboxSecs;
		var holdTotalSecs;
		holdTotalSecs = (numboxMins.value * 60) + numboxSecs.value;
		argActionStep.put(9, holdTotalSecs);
		this.update;
	}

	deleteActionStep { arg argActionStep;
		arrActionSteps.remove(argActionStep);
		if (arrActionSteps.size == 0, {
			this.addActionStep;
		},{
			this.renumberSteps;
			this.update;
		});
	}

	addActionStep { arg argStepNo = 0, argTime = 0;
		var newActionStep;
		newActionStep = defaultActionStep.deepCopy
		.put(9, argTime)
		.put(12, argStepNo + 0.5)
		.put(13, nextStepIDFunc.value);
		arrActionSteps = arrActionSteps.add(newActionStep);
		this.sortByStepNo;
		this.update;
		^newActionStep;
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
	updateTimesFromNew { arg argArrActionSteps;
		arrActionSteps = argArrActionSteps;
		arrActionSteps.do({arg argActionStep, i;
			var holdTotalSecs;
			holdTotalSecs = argActionStep.at(9);
			arrNumboxMins[i].value = holdTotalSecs div: 60;
			arrNumboxSecs[i].value = holdTotalSecs % 60;
		});
	}

}
