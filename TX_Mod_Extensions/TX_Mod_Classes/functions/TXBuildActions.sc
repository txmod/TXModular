// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXBuildActions {		// Action Builder

	// this class builds actions from an array of specs which are mostly specific types of guiSpec
	// the only exception is the type "commandAction" which acts as the equivalent of guiSpec's actionButton

	*from { arg argModule, argArrSpecs;
		var arrActionSpecs, holdAction, holdActionFunc, holdGetItemsFunction, holdControlSpecFunc, actionCounter;

		// add initial dummy action
		argArrSpecs = [["commandAction", "..."]] ++ argArrSpecs;
		// add title items (or defaults if nil) to argArrSpecs
		if (argModule.guiSpecTitleArray.notNil, {
			argArrSpecs = argArrSpecs ++ argModule.guiSpecTitleArray;
		}, {
			// add defaults
			argArrSpecs = argArrSpecs ++ [
				["RunPauseButton"],
				["RebuildModuleButton"],
				["ModuleInfoTxt"],
			];
		});

		// if not the system module, add presets
		if (argModule.moduleID != 99, {
			argArrSpecs = argArrSpecs ++ [
				["TXPopupAction", "Load Preset :",
					//OLD {["Load Preset..."] ++ argModule.arrPresets.collect ({ arg item, i; (i+1).asString +": "+ item[0]; })},
					{["Load Preset..."] ++ 99.collect ({ arg i; (i).asString; })},
					"dummyName",
					{ arg view;  if (view.value > 0, {
						argModule.loadPreset(argModule, view.value-1);
						TXSystem1.showViewIfRunInterface;
					});
					}
				],
			];
		});

		// initialise variable
		actionCounter = 0;

		// build array of TXActions based on argArrSpecs
		argArrSpecs.do({ arg item, i;

			// Note: Action.legacyType is a variable that is used as a fix for systems saved in version 0.10.6,
			//    where action text was not saved for widgets. Only older actions are set "legacyType = 1"
			//    if any new actions are added, they should NOT have "legacyType = 1",
			//    just leave it to default to 0 in class TXAction

			// commandAction
			// arguments- index1 is action name, index2 is action function,
			//   index3 is optional array of controlspec functions
			//   index4 is optional guiObjectType
			//   index5 is optional getItemsFunction
			if (item.at(0) == "commandAction", {
				holdAction = TXAction.new(\commandAction, item.at(1), item.at(2));
				holdAction.legacyType = 1;
				if (item.at(3).notNil, {
					holdAction.arrControlSpecFuncs = item.at(3);
				});
				if (item.at(4).notNil, {
					holdAction.guiObjectType = item.at(4);
				});
				if (item.at(5).notNil, {
					holdAction.getItemsFunction = item.at(5);
				});
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// valueActionNumber
			// arguments- index1 is action name, index2 is array of controlspec functions,
			//   index3 is get value function, index4 is set value function,
			if (item.at(0) == "valueActionNumber", {
				holdAction = TXAction.new(\valueAction, item.at(1));
				holdAction.arrControlSpecFuncs = item.at(2);
				holdAction.getValueFunction = item.at(3);
				holdAction.setValueFunction = item.at(4);
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// valueActionCheckBox
			// arguments- index1 is action name, index2 is array of controlspec functions,
			//   index3 is get value function, index4 is set value function,
			if (item.at(0) == "valueActionCheckBox", {
				holdAction = TXAction.new(\valueAction, item.at(1));
				holdAction.arrControlSpecFuncs = item.at(2);
				holdAction.getValueFunction = item.at(3);
				holdAction.setValueFunction = item.at(4);
				holdAction.guiObjectType = \checkbox;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// EmptyValueAction
			// arguments- index1 is guiObjectType
			if (item.at(0) == "EmptyValueAction", {
				holdAction = TXAction.new(\valueAction, "...");
				holdAction.legacyType = 1;
				holdAction.arrControlSpecFuncs = [];
				holdAction.guiObjectType = item.at(1);
				holdAction.getItemsFunction = {["..."]};
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// WetDryMixSlider
			// N.B. no arguments - assumes synth has argument "wetDryMix"
			if (item.at(0) == "WetDryMixSlider", {
				holdAction = TXAction.new(\valueAction, "Set Dry-Wet Mix");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.getSynthArgSpec("wetDryMix")};
				holdAction.setValueFunction = {arg argValue; argModule.setSynthValue("wetDryMix", argValue)};
				holdAction.arrControlSpecFuncs = [ControlSpec(0, 1);];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// Transpose
			// no arguments - assumes synth has argument "transpose"
			if (item.at(0) == "Transpose", {
				holdAction = TXAction.new(\valueAction, "Set Transpose semitones");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.getSynthArgSpec("transpose")};
				holdAction.setValueFunction = {arg argValue; argModule.setSynthValue("transpose", argValue)};
				holdAction.arrControlSpecFuncs = [ControlSpec(-127, 127, default: 0);];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// TXMinMaxSliderSplit & TXTimeBpmMinMaxSldr - separate values are given for each of the 3 gui objects
			// arguments- index1 is slider text, index2 is controlSpec,
			//   index3/4/5 are synth arg names to  be updated for slider, min & max
			// 	index6 is an optional ACTION function to be valued in views action function
			// e.g. ["TXMinMaxSliderSplit", "Freq", \freq, "freq", "freqMin", "freqMax"]
			if (	(item.at(0) == "TXMinMaxSliderSplit")
				or: (item.at(0) == "TXTimeBpmMinMaxSldr")
				or: (item.at(0) == "TXMinMaxFreqNoteSldr")
				or: (item.at(0) == "TXFreqBpmMinMaxSldr")
				,
				{
					// add value action
					holdAction = TXAction.new(\valueAction, "Set " ++ item.at(1));
					holdAction.legacyType = 1;
					holdControlSpecFunc = {
						item.at(2).value.asSpec.deepCopy
						.maxval_(argModule.getSynthArgSpec(item.at(5)))
						.minval_(argModule.getSynthArgSpec(item.at(4)))
					};
					holdAction.getValueFunction = {
						item.at(2).value.asSpec.deepCopy
						.maxval_(argModule.getSynthArgSpec(item.at(5)))
						.minval_(argModule.getSynthArgSpec(item.at(4)))
						.map(argModule.getSynthArgSpec(item.at(3)))};
					holdAction.setValueFunction = {arg argValue;
						argModule.setSynthValue(item.at(3),
							item.at(2).value.asSpec.deepCopy
							.maxval_(argModule.getSynthArgSpec(item.at(5)))
							.minval_(argModule.getSynthArgSpec(item.at(4)))
							.unmap(item.at(2).value.asSpec.deepCopy
								.maxval_(argModule.getSynthArgSpec(item.at(5)))
								.minval_(argModule.getSynthArgSpec(item.at(4)))
								.constrain(argValue)));
						// run action function passing it value as arg
						item.at(6).value(argValue);
					};
					holdAction.arrControlSpecFuncs = [holdControlSpecFunc];
					holdAction.guiObjectType = \number;
					arrActionSpecs = arrActionSpecs.add(holdAction);
					// add unmapped value action
					holdAction = TXAction.new(\valueAction, "Set " ++ item.at(1) ++ " unmapped");
					holdAction.legacyType = 1;
					holdAction.getValueFunction = {argModule.getSynthArgSpec(item.at(3))};
					holdAction.setValueFunction = {arg argValue;
						argModule.setSynthValue(item.at(3),
							ControlSpec(0,1).constrain(argValue));
						// run action function passing it value as arg
						item.at(6).value(item.at(2).value.asSpec.deepCopy
							.maxval_(argModule.getSynthArgSpec(item.at(5)))
							.minval_(argModule.getSynthArgSpec(item.at(4)))
							.map(ControlSpec(0,1).constrain(argValue)));
					};
					holdAction.arrControlSpecFuncs = [ControlSpec(0,1);];
					holdAction.guiObjectType = \number;
					arrActionSpecs = arrActionSpecs.add(holdAction);
					// add commandAction to randomize value
					holdActionFunc = {
						var holdCurVal;
						//  update with randomized value
						holdCurVal = 1.0.rand;
						argModule.setSynthValue(item.at(3), holdCurVal);
						// run action function passing it randomized value as arg
						item.at(6).value(holdCurVal);
					};
					holdAction = TXAction.new(\commandAction, "Randomize " ++ item.at(1), holdActionFunc);
					arrActionSpecs = arrActionSpecs.add(holdAction);
					// add commandAction to randomize w. depth
					holdActionFunc = { arg val1;
						// get old value
						var holdControlSpec, holdOldVal, newVal;
						holdControlSpec = item.at(2).value.asSpec.deepCopy
						.maxval_(argModule.getSynthArgSpec(item.at(5)))
						.minval_(argModule.getSynthArgSpec(item.at(4)));
						holdOldVal = holdControlSpec.map(argModule.getSynthArgSpec(item.at(3)));
						//  update with randomized value mixed with old value
						newVal = holdControlSpec.unmap(holdControlSpec.constrain(
							holdControlSpec.map(1.0.rand).blend(holdOldVal, 1 - val1)));
						argModule.setSynthValue(item.at(3), newVal);
						// run action function passing it value as arg
						item.at(6).value(newVal);
					};
					holdAction = TXAction.new(\commandAction, "Randomize w. depth " ++ item.at(1), holdActionFunc);
					holdAction.arrControlSpecFuncs = [ControlSpec(0,1, default: 0.25);];
					holdAction.guiObjectType = \number;
					arrActionSpecs = arrActionSpecs.add(holdAction);
					// add commandAction to add to value
					holdActionFunc = { arg val1;
						var holdOldVal, holdCurVal;
						// get old value
						holdOldVal = item.at(2).value.asSpec.deepCopy
						.maxval_(argModule.getSynthArgSpec(item.at(5)))
						.minval_(argModule.getSynthArgSpec(item.at(4)))
						.map(argModule.getSynthArgSpec(item.at(3)));
						//  update with new value
						holdCurVal = holdOldVal + val1;
						argModule.setSynthValue(item.at(3),
							item.at(2).value.asSpec.deepCopy
							.maxval_(argModule.getSynthArgSpec(item.at(5)))
							.minval_(argModule.getSynthArgSpec(item.at(4)))
							.unmap(item.at(2).value.asSpec.deepCopy
								.maxval_(argModule.getSynthArgSpec(item.at(5)))
								.minval_(argModule.getSynthArgSpec(item.at(4)))
								.constrain(holdCurVal)));
						// run action function passing it value as arg
						item.at(6).value(holdCurVal);
					};
					holdAction = TXAction.new(\commandAction, "Add to " ++ item.at(1), holdActionFunc);
					holdAction.arrControlSpecFuncs = [{ControlSpec(0, item.at(2).value.asSpec.range, \lin, 0, 1.min(item.at(2).value.asSpec.maxval))}];
					holdAction.guiObjectType = \number;
					arrActionSpecs = arrActionSpecs.add(holdAction);
					// add commandAction to subtract from value
					holdActionFunc = { arg val1;
						var holdOldVal, holdCurVal;
						// get old value
						holdOldVal = item.at(2).value.asSpec.deepCopy
						.maxval_(argModule.getSynthArgSpec(item.at(5)))
						.minval_(argModule.getSynthArgSpec(item.at(4)))
						.map(argModule.getSynthArgSpec(item.at(3)));
						//  update with new value
						holdCurVal = holdOldVal - val1;
						argModule.setSynthValue(item.at(3),
							item.at(2).value.asSpec.deepCopy
							.maxval_(argModule.getSynthArgSpec(item.at(5)))
							.minval_(argModule.getSynthArgSpec(item.at(4)))
							.unmap(item.at(2).value.asSpec.deepCopy
								.maxval_(argModule.getSynthArgSpec(item.at(5)))
								.minval_(argModule.getSynthArgSpec(item.at(4)))
								.constrain(holdCurVal)));
						// run action function passing it value as arg
						item.at(6).value(holdCurVal);
					};
					holdAction = TXAction.new(\commandAction, "Subtract from " ++ item.at(1), holdActionFunc);
					holdAction.arrControlSpecFuncs = [{ControlSpec(0, item.at(2).value.asSpec.range, \lin, 0, 1.min(item.at(2).value.asSpec.maxval))}];
					holdAction.guiObjectType = \number;
					arrActionSpecs = arrActionSpecs.add(holdAction);
					// add commandAction to multiply by value
					holdActionFunc = { arg val1;
						var holdOldVal, holdCurVal;
						// get old value
						holdOldVal = item.at(2).value.asSpec.deepCopy
						.maxval_(argModule.getSynthArgSpec(item.at(5)))
						.minval_(argModule.getSynthArgSpec(item.at(4)))
						.map(argModule.getSynthArgSpec(item.at(3)));
						//  update with new value
						holdCurVal = holdOldVal * val1;
						argModule.setSynthValue(item.at(3),
							item.at(2).value.asSpec.deepCopy
							.maxval_(argModule.getSynthArgSpec(item.at(5)))
							.minval_(argModule.getSynthArgSpec(item.at(4)))
							.unmap(item.at(2).value.asSpec.deepCopy
								.maxval_(argModule.getSynthArgSpec(item.at(5)))
								.minval_(argModule.getSynthArgSpec(item.at(4)))
								.constrain(holdCurVal)));
						// run action function passing it value as arg
						item.at(6).value(holdCurVal);
					};
					holdAction = TXAction.new(\commandAction, "Multiply " ++ item.at(1), holdActionFunc);
					holdAction.arrControlSpecFuncs = [{ControlSpec(0.01, 100, \lin, 0, 1)}];
					holdAction.guiObjectType = \number;
					arrActionSpecs = arrActionSpecs.add(holdAction);
					// add commandAction to divide by  value
					holdActionFunc = { arg val1;
						var holdOldVal, holdCurVal;
						// get old value
						holdOldVal = item.at(2).value.asSpec.deepCopy
						.maxval_(argModule.getSynthArgSpec(item.at(5)))
						.minval_(argModule.getSynthArgSpec(item.at(4)))
						.map(argModule.getSynthArgSpec(item.at(3)));
						//  update with new value
						if (val1 != 0, {holdCurVal = holdOldVal / val1}, {holdCurVal = 0});
						argModule.setSynthValue(item.at(3),
							item.at(2).value.asSpec.deepCopy
							.maxval_(argModule.getSynthArgSpec(item.at(5)))
							.minval_(argModule.getSynthArgSpec(item.at(4)))
							.unmap(item.at(2).value.asSpec.deepCopy
								.maxval_(argModule.getSynthArgSpec(item.at(5)))
								.minval_(argModule.getSynthArgSpec(item.at(4)))
								.constrain(holdCurVal)));
						// run action function passing it value as arg
						item.at(6).value(holdCurVal);
					};
					holdAction = TXAction.new(\commandAction, "Divide " ++ item.at(1), holdActionFunc);
					holdAction.arrControlSpecFuncs = [{ControlSpec(0.01, 100, \lin, 0, 1)}];
					holdAction.guiObjectType = \number;
					arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// EZslider or EZSlider or EZNumber or TXNumberPlusMinus or TXNumberPlusMinus2 or TXFraction/TXFractionSlider
			// arguments- index1 is slider text, index2 is controlSpec, index3 is synth arg name to be updated,
			// 	index4 is an optional ACTION function to be valued in views action
			// e.g. ["EZslider", "Volume", \amp, "vol"]
			if ((item.at(0) == "EZslider")
				or: (item.at(0) == "EZSlider")
				or: (item.at(0) == "EZNumber")
				or: (item.at(0) == "TXNumberPlusMinus")
				or: (item.at(0) == "TXNumberPlusMinus2")
				or: (item.at(0) == "TXFraction")
				or: (item.at(0) == "TXFractionSlider"), {
					// add value action
					holdAction = TXAction.new(\valueAction, "Set " ++ item.at(1));
					holdAction.legacyType = 1;
					holdControlSpecFunc = {item.at(2).value.asSpec.deepCopy};
					holdAction.getValueFunction = {argModule.getSynthArgSpec(item.at(3))};
					holdAction.setValueFunction = {arg argValue;
						argModule.setSynthValue(item.at(3),
							item.at(2).value.asSpec.constrain(argValue));
						// run action function passing it value as arg
						item.at(4).value(argValue);
					};
					holdAction.arrControlSpecFuncs = [holdControlSpecFunc];
					holdAction.guiObjectType = \number;
					arrActionSpecs = arrActionSpecs.add(holdAction);
					// add unmapped value action
					holdAction = TXAction.new(\valueAction, "Set " ++ item.at(1) ++ " unmapped");
					holdAction.legacyType = 1;
					holdAction.getValueFunction = {item.at(2).value.asSpec.unmap(argModule.getSynthArgSpec(item.at(3)))};
					holdAction.setValueFunction = {arg argValue;
						argModule.setSynthValue(item.at(3),
							item.at(2).value.asSpec.map(ControlSpec(0,1).constrain(argValue)));
						// run action function passing it value as arg
						item.at(4).value(item.at(2).value.asSpec.map(ControlSpec(0,1).constrain(argValue)));
					};
					holdAction.arrControlSpecFuncs = [ControlSpec(0,1);];
					holdAction.guiObjectType = \number;
					arrActionSpecs = arrActionSpecs.add(holdAction);
					// add commandAction to randomize value
					holdActionFunc = {
						var holdCurVal;
						//  update with randomized value
						holdCurVal = 1.0.rand;
						argModule.setSynthValue(item.at(3),
							item.at(2).value.asSpec.map(holdCurVal));
						// run action function passing it randomized value as arg
						item.at(4).value(item.at(2).value.asSpec.map(holdCurVal));
					};
					holdAction = TXAction.new(\commandAction, "Randomize " ++ item.at(1), holdActionFunc);
					arrActionSpecs = arrActionSpecs.add(holdAction);

					// add commandAction to randomize w. depth
					holdActionFunc = { arg val1;
						// get old value
						var holdOldVal, holdControlSpec, newVal;
						holdOldVal = argModule.getSynthArgSpec(item.at(3));
						//  update with randomized value mixed with old value
						holdControlSpec = item.at(2).value.asSpec.deepCopy;
						newVal = holdControlSpec.constrain(
							holdControlSpec.map(1.0.rand).blend(holdOldVal, 1 - val1)
						);
						argModule.setSynthValue(item.at(3), newVal);
						// run action function passing it randomized value as arg
						item.at(4).value(newVal);
					};
					holdAction = TXAction.new(\commandAction, "Randomize w. depth " ++ item.at(1), holdActionFunc);
					holdAction.arrControlSpecFuncs = [ControlSpec(0,1, default: 0.25);];
					holdAction.guiObjectType = \number;
					arrActionSpecs = arrActionSpecs.add(holdAction);

					// add commandAction to add to value
					holdActionFunc = { arg val1;
						var holdOldVal, holdCurVal;
						// get old value
						holdOldVal = argModule.getSynthArgSpec(item.at(3));
						//  update with new value
						holdCurVal = holdOldVal + val1;
						argModule.setSynthValue(item.at(3),
							item.at(2).value.asSpec.constrain(holdCurVal));
						// run action function passing it value as arg
						item.at(4).value(item.at(2).value.asSpec.constrain(holdCurVal));
					};
					holdAction = TXAction.new(\commandAction, "Add to " ++ item.at(1), holdActionFunc);
					holdAction.arrControlSpecFuncs = [{ControlSpec(0, item.at(2).value.asSpec.range, \lin, 0, 1.min(item.at(2).value.asSpec.maxval))}];
					holdAction.guiObjectType = \number;
					arrActionSpecs = arrActionSpecs.add(holdAction);
					// add commandAction to subtract from value
					holdActionFunc = { arg val1;
						var holdOldVal, holdCurVal;
						// get old value
						holdOldVal = argModule.getSynthArgSpec(item.at(3));
						//  update with new value
						holdCurVal = holdOldVal - val1;
						argModule.setSynthValue(item.at(3),
							item.at(2).value.asSpec.constrain(holdCurVal));
						// run action function passing it value as arg
						item.at(4).value(item.at(2).value.asSpec.constrain(holdCurVal));
					};
					holdAction = TXAction.new(\commandAction, "Subtract from " ++ item.at(1), holdActionFunc);
					holdAction.arrControlSpecFuncs = [{ControlSpec(0, item.at(2).value.asSpec.range, \lin, 0, 1.min(item.at(2).value.asSpec.maxval))}];
					holdAction.guiObjectType = \number;
					arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// EZsliderUnmapped - same as EZslider but returns unmapped value (range 0-1) of slider
			// arguments- index1 is slider text, index2 is controlSpec function,
			// index3 is synth arg name to be updated,
			// 	index4 is an optional ACTION function to be valued in views action
			// e.g. ["EZsliderUnmapped", "Attack", ControlSpec(0, 5), "attack"]
			if (item.at(0) == "EZsliderUnmapped", {
				// add value action
				holdAction = TXAction.new(\valueAction, "Set " ++ item.at(1));
				holdAction.legacyType = 1;
				holdControlSpecFunc = {item.at(2).value.asSpec.deepCopy};
				holdAction.getValueFunction = {item.at(2).value.asSpec.map(argModule.getSynthArgSpec(item.at(3)))};
				holdAction.setValueFunction = {arg argValue;
					argModule.setSynthValue(item.at(3),
						item.at(2).value.asSpec.unmap(item.at(2).value.asSpec.constrain(argValue)));
					// run action function passing it value as arg
					item.at(4).value(item.at(2).value.asSpec.unmap(item.at(2).value.asSpec.constrain(argValue)));
				};
				holdAction.arrControlSpecFuncs = [holdControlSpecFunc];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add unmapped value action
				holdAction = TXAction.new(\valueAction, "Set " ++ item.at(1) ++ " unmapped");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.getSynthArgSpec(item.at(3))};
				holdAction.setValueFunction = {arg argValue;
					argModule.setSynthValue(item.at(3),
						ControlSpec(0,1).constrain(argValue));
					// run action function passing it value as arg
					item.at(4).value(ControlSpec(0,1).constrain(argValue));
				};
				holdAction.arrControlSpecFuncs = [ControlSpec(0,1);];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to randomize value
				holdActionFunc = {
					var holdCurVal;
					//  update with randomized value
					holdCurVal = 1.0.rand;
					argModule.setSynthValue(item.at(3), holdCurVal);
					// run action function passing it randomized value as arg
					item.at(4).value(holdCurVal);
				};
				holdAction = TXAction.new(\commandAction, "Randomize " ++ item.at(1), holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to randomize w. depth
				holdActionFunc = {arg val1;
					var holdOldVal = argModule.getSynthArgSpec(item.at(3));
					var holdCurVal;
					//  update with randomized value
					holdCurVal = 1.0.rand;
					argModule.setSynthValue(item.at(3), holdCurVal.blend(holdOldVal, 1 - val1));
					// run action function passing it randomized value as arg
					item.at(4).value(holdCurVal);
				};
				holdAction.arrControlSpecFuncs = [ControlSpec(0,1, default: 0.25)];
				holdAction.guiObjectType = \number;
				holdAction = TXAction.new(\commandAction, "Randomize w. depth " ++ item.at(1), holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// TXPopupAction, TXPopupActionPlusMinus, TXListViewAction TXListViewActionPlusMinus
			// arguments- index1 is text, index2 is items array (function or value),
			//	index3 is synth arg name to be updated, index4 is optional popup action,
			// 	index5 is the optional width,
			// e.g. ["TXPopupAction", "Sample", holdSampleFileNames, "sampleNo",
			//		{ arg view; this.loadSample(view.value); }]
			if ((item.at(0) == "TXPopupAction") or: (item.at(0) == "TXPopupActionPlusMinus")
				or: (item.at(0) == "TXListViewAction") or: (item.at(0) == "TXListViewActionPlusMinus"), {
					holdAction = TXAction.new(\valueAction, "Set " ++ item.at(1));
					holdAction.legacyType = 1;
					holdAction.getValueFunction = {argModule.getSynthArgSpec(item.at(3))};
					holdAction.setValueFunction = {arg argValue;
						argModule.setSynthValue(item.at(3), argValue);
						// run action function passing it value as arg
						item.at(4).value(argValue);
					};
					holdAction.guiObjectType = \popup;
					holdAction.getItemsFunction = {item.at(2).value};
					arrActionSpecs = arrActionSpecs.add(holdAction);
					// add commandAction to increment value
					holdActionFunc = {
						var holdCurVal, maxVal;
						maxVal = item.at(2).value.size - 1;
						// get current val and update with new value
						holdCurVal = argModule.getSynthArgSpec(item.at(3));
						argModule.setSynthValue(item.at(3), (holdCurVal + 1).min(maxVal));
						// run action function passing it value as arg
						item.at(4).value((holdCurVal + 1).min(maxVal));
					};
					holdAction = TXAction.new(\commandAction, "Next " ++ item.at(1), holdActionFunc);
					arrActionSpecs = arrActionSpecs.add(holdAction);
					// add commandAction to decrement value
					holdActionFunc = {
						var holdCurVal;
						// get current val and update with new value
						holdCurVal = argModule.getSynthArgSpec(item.at(3));
						argModule.setSynthValue(item.at(3), (holdCurVal - 1).max(0));
						// run action function passing it value as arg
						item.at(4).value((holdCurVal - 1).max(0));
					};
					holdAction = TXAction.new(\commandAction, "Previous " ++ item.at(1), holdActionFunc);
					arrActionSpecs = arrActionSpecs.add(holdAction);
					// add commandAction to randomize value
					holdActionFunc = {
						var holdCurVal;
						//  update with randomized value
						holdCurVal = item.at(2).value.size.rand;
						argModule.setSynthValue(item.at(3), holdCurVal);
						// run action function passing it randomized value as arg
						item.at(4).value(holdCurVal);
					};
					holdAction = TXAction.new(\commandAction, "Randomize " ++ item.at(1), holdActionFunc);
					arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// SynthOptionPopup SynthOptionPopupPlusMinus SynthOptionList SynthOptionListPlusMinus
			// NOTE - this will automatically rebuild the synth once a synth option has been changed
			// arguments- index1 is text, index2 is arrOptionData,
			// index3 is the index of arrOptions and arrOptionData to use,
			// index4 is the width (optional and ignored here), index5 is optional popup action
			if ( (item.at(0) == "SynthOptionPopup") or: (item.at(0) == "SynthOptionPopupPlusMinus")
				 or: (item.at(0) == "SynthOptionList") or: (item.at(0) == "SynthOptionListPlusMinus"), {
				holdAction = TXAction.new(\valueAction, "Set " ++ item.at(1));
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.arrOptions.at(item.at(3))};
				holdAction.setValueFunction = {arg argValue;
					argModule.arrOptions.put(item.at(3), argValue);
					// run action function passing it value as arg
					item.at(5).value(argValue);
					// set function must rebuild synth as synth options have changed
					argModule.rebuildSynth;
				};
				holdAction.guiObjectType = \popup;
				holdAction.getItemsFunction = {item.at(2).at(item.at(3)).collect({arg item, i; item.at(0)})};
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to increment value
				//			holdGetItemsFunction = {item.at(2).at(item.at(3)).collect({arg item, i; item.at(0)})};
				holdActionFunc = {
					var holdCurVal, maxVal;
					maxVal = item.at(2).at(item.at(3)).collect({arg item, i; item.at(0)}).size - 1;
					// get current val and update with new value
					holdCurVal = argModule.arrOptions.at(item.at(3));
					argModule.arrOptions.put(item.at(3), (holdCurVal + 1).min(maxVal));
					// run action function passing it value as arg
					item.at(5).value((holdCurVal + 1).min(maxVal));
					// must rebuild synth as synth options have changed
					argModule.rebuildSynth;
				};
				holdAction = TXAction.new(\commandAction, "Next " ++ item.at(1), holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to decrement value
				holdActionFunc = {
					var holdCurVal;
					holdCurVal = argModule.arrOptions.at(item.at(3));
					argModule.arrOptions.put(item.at(3), (holdCurVal - 1).max(0));
					// run action function passing it value as arg
					item.at(5).value((holdCurVal - 1).max(0));
					// must rebuild synth as synth options have changed
					argModule.rebuildSynth;
				};
				holdAction = TXAction.new(\commandAction, "Previous " ++ item.at(1), holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to randomize value
				holdActionFunc = {
					var holdCurVal;
					// randomize value from item array size
					holdCurVal = item.at(2).at(item.at(3)).collect({arg item, i; item.at(0)}).size.rand;
					argModule.arrOptions.put(item.at(3), holdCurVal);
					// run action function passing it randomized value as arg
					item.at(5).value(holdCurVal);
					// must rebuild synth as synth options have changed
					argModule.rebuildSynth;
				};
				holdAction = TXAction.new(\commandAction, "randomize " ++ item.at(1), holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// SynthOptionCheckBox
			// arguments- index1 is checkbox text, index2 is synth arg name to be updated,
			// 	index3 is an optional ACTION function to be valued in views action, index 4 is optional width
			// e.g. ["SynthOptionCheckBox", "Frequency Modulation", arrOptionData, 3]
			if (item.at(0) == "SynthOptionCheckBox", {
				holdAction = TXAction.new(\valueAction, "Set " ++ item.at(1));
				holdAction.legacyType = 0;
				holdAction.getValueFunction = {argModule.arrOptions.at(item.at(3))};
				holdAction.setValueFunction = {arg argValue;
					argModule.arrOptions.put(item.at(3), argValue);
					// run action function passing it value as arg
					item.at(3).value(argValue);
					// must rebuild synth as synth options have changed
					argModule.rebuildSynth;
				};
				holdAction.guiObjectType = \checkbox;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to toggle value
				holdActionFunc = {
					var holdCurVal;
					// get current val and update with opposite value
					holdCurVal = argModule.arrOptions.at(item.at(3));
					argModule.arrOptions.put(item.at(3), (1 - holdCurVal));
					// run action function passing it value as arg
					item.at(3).value((1 - holdCurVal));
					// must rebuild synth as synth options have changed
					argModule.rebuildSynth;
				};
				holdAction = TXAction.new(\commandAction, "Toggle " ++ item.at(1), holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// TXCheckBox
			// arguments- index1 is checkbox text, index2 is synth arg name to be updated,
			// 	index3 is an optional ACTION function to be valued in views action, index 4 is optional width
			// e.g. ["TXCheckBox", "Loop", "loop"]
			if (item.at(0) == "TXCheckBox", {
				holdAction = TXAction.new(\valueAction, "Set " ++ item.at(1));
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.getSynthArgSpec(item.at(2))};
				holdAction.setValueFunction = {arg argValue;
					argModule.setSynthValue(item.at(2), argValue);
					// run action function passing it value as arg
					item.at(3).value(argValue);
				};
				holdAction.guiObjectType = \checkbox;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to toggle value
				holdActionFunc = {
					var holdCurVal;
					// get current val and update with opposite value
					holdCurVal = argModule.getSynthArgSpec(item.at(2));
					argModule.setSynthValue(item.at(2), (1 - holdCurVal));
					// run action function passing it value as arg
					item.at(3).value((1 - holdCurVal));
				};
				holdAction = TXAction.new(\commandAction, "Toggle " ++ item.at(1), holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// SeqSyncStartCheckBox
			// N.B. no arguments
			if (item.at(0) == "SeqSyncStartCheckBox", {
				holdAction = TXAction.new(\valueAction, "Set Sync Start");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.getSynthArgSpec("syncStart")};
				holdAction.setValueFunction = {arg argValue;
					argModule.setSynthArgSpec("syncStart", argValue);
				};
				holdAction.guiObjectType = \checkbox;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// SeqSyncStartCheckBox
			// N.B. no arguments
			if (item.at(0) == "SeqSyncStopCheckBox", {
				holdAction = TXAction.new(\valueAction, "Set Sync Stop");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.getSynthArgSpec("syncStop")};
				holdAction.setValueFunction = {arg argValue;
					argModule.setSynthArgSpec("syncStop", argValue);
				};
				holdAction.guiObjectType = \checkbox;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// TXRangeSlider
			// arguments- index1 is slider text, index2 is controlSpec function,
			//   index3/4 are synth arg names to be updated,
			// 	index5 is an optional ACTION function to be valued in views action
			// e.g. ["TXRangeSlider", "Volume", \amp, "volMin", "volMax"]
			if (item.at(0) == "TXRangeSlider", {
				// add value action
				holdAction = TXAction.new(\valueAction, "Set " ++ item.at(1) ++ " Min");
				holdAction.legacyType = 1;
				holdControlSpecFunc = {item.at(2).value.asSpec.deepCopy};
				holdAction.getValueFunction = {argModule.getSynthArgSpec(item.at(3))};
				holdAction.setValueFunction = {arg argValue;
					argModule.setSynthValue(item.at(3),
						item.at(2).value.asSpec.constrain(argValue));
					// run action function passing it value as arg
					item.at(5).value(argValue);
				};
				holdAction.arrControlSpecFuncs = [holdControlSpecFunc];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add value action
				holdAction = TXAction.new(\valueAction, "Set " ++ item.at(1) ++ " Max");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.getSynthArgSpec(item.at(4))};
				holdAction.setValueFunction = {arg argValue;
					argModule.setSynthValue(item.at(4),
						item.at(2).value.asSpec.constrain(argValue));
					// run action function passing it value as arg
					item.at(5).value(argValue);
				};
				holdAction.arrControlSpecFuncs = [holdControlSpecFunc];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// SeqPlayRange
			// arguments- index1/index2 are synth arg names to be updated for start step & end step,
			// index3 is optional synth arg name to be updated for autoloop - if nil, loop checkbox not shown
			// index4 is max no. of steps,
			// index5 is optional text for label
			// index6 is an optional ACTION function to be valued in views action
			// index7 is an optional false which sets the view's rangeview .enabled to false
			// e.g. ["SeqPlayRange", "seqStartStep", "seqEndStep", "seqAutoLoop", 16]
			if (item.at(0) == "SeqPlayRange", {
				// add value action
				holdAction = TXAction.new(\valueAction, "Set Play Range " ++ " Min");
				holdAction.legacyType = 1;
				holdControlSpecFunc = {ControlSpec(1, item.at(4), step: 1)};
				holdAction.getValueFunction = {argModule.getSynthArgSpec(item.at(1))};
				holdAction.setValueFunction = {arg argValue;
					argModule.setSynthValue(item.at(1),
						ControlSpec(1, item.at(4), step: 1).constrain(argValue));
					// run action function passing it value as arg
					item.at(6).value(argValue);
				};
				holdAction.arrControlSpecFuncs = [holdControlSpecFunc];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add value action
				holdAction = TXAction.new(\valueAction, "Set Play Range " ++ " Max");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.getSynthArgSpec(item.at(2))};
				holdAction.setValueFunction = {arg argValue;
					argModule.setSynthValue(item.at(2),
						ControlSpec(1, item.at(4), step: 1).constrain(argValue));
					// run action function passing it value as arg
					item.at(6).value(argValue);
				};
				holdAction.arrControlSpecFuncs = [holdControlSpecFunc];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// MIDIChannelSelector
			// N.B. no arguments
			if (item.at(0) == "MIDIChannelSelector", {
				holdAction = TXAction.new(\valueAction, "Set Midi Chan Min");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.midiMinChannel;};
				holdAction.setValueFunction = {arg argValue; argModule.midiMinChannel = argValue;};
				holdAction.arrControlSpecFuncs = [ControlSpec(1, 16, step: 1);];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				holdAction = TXAction.new(\valueAction, "Set Midi Chan Max");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.midiMaxChannel;};
				holdAction.setValueFunction = {arg argValue; argModule.midiMaxChannel = argValue;};
				holdAction.arrControlSpecFuncs = [ControlSpec(1, 16, step: 1);];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// MIDINoteSelector
			// N.B. no arguments
			if (item.at(0) == "MIDINoteSelector", {
				holdAction = TXAction.new(\valueAction, "Set MidiNote Range Min");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.midiMinNoteNo;};
				holdAction.setValueFunction = {arg argValue; argModule.midiMinNoteNo = argValue;};
				holdAction.arrControlSpecFuncs = [ControlSpec(0, 127, step: 1);];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				holdAction = TXAction.new(\valueAction, "Set Midi Note Range Max");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.midiMaxNoteNo;};
				holdAction.setValueFunction = {arg argValue; argModule.midiMaxNoteNo = argValue;};
				holdAction.arrControlSpecFuncs = [ControlSpec(0, 127, step: 1);];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// MIDIVelSelector
			// N.B. no arguments
			if (item.at(0) == "MIDIVelSelector", {
				holdAction = TXAction.new(\valueAction, "Set Midi Vel Range Min");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.midiMinVel;};
				holdAction.setValueFunction = {arg argValue; argModule.midiMinVel = argValue;};
				holdAction.arrControlSpecFuncs = [ControlSpec(0, 127, step: 1);];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				holdAction = TXAction.new(\valueAction, "Set Midi Vel Range Max");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.midiMaxVel;};
				holdAction.setValueFunction = {arg argValue; argModule.midiMaxVel = argValue;};
				holdAction.arrControlSpecFuncs = [ControlSpec(0, 127, step: 1);];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// MIDIListenCheckBox
			// N.B. no arguments
			if (item.at(0) == "MIDIListenCheckBox", {
				holdAction = TXAction.new(\valueAction, "Set MIDI Listen");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.midiListen};
				holdAction.setValueFunction = {arg argValue; argModule.setMidiListen(argValue)};
				holdAction.guiObjectType = \checkbox;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// MIDISoloControllerSelector
			// N.B. no arguments
			if (item.at(0) == "MIDISoloControllerSelector", {
				holdAction = TXAction.new(\valueAction, "Set Controller No");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.midiMinControlNo};
				holdAction.setValueFunction = {arg argValue;
					argModule.midiMinControlNo = ControlSpec(0, 127, step: 1).constrain(argValue);
					argModule.midiMaxControlNo = ControlSpec(0, 127, step: 1).constrain(argValue);
				};
				holdAction.guiObjectType = \number;
				holdAction.arrControlSpecFuncs = [ControlSpec(0, 127, step: 1);];
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// MIDISoloChannelSelector
			// N.B. no arguments
			if (item.at(0) == "MIDISoloChannelSelector", {
				holdAction = TXAction.new(\valueAction, "Set Midi Channel");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.midiMinChannel};
				holdAction.setValueFunction = {arg argValue;
					argModule.midiMinChannel = ControlSpec(0, 16, step: 1).constrain(argValue);
					argModule.midiMaxChannel = ControlSpec(0, 16, step: 1).constrain(argValue);
				};
				holdAction.guiObjectType = \number;
				holdAction.arrControlSpecFuncs = [ControlSpec(0, 16, step: 1);];
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// MIDIOutPortSelector
			// N.B. no arguments
			if (item.at(0) == "MIDIOutPortSelector", {
				holdAction = TXAction.new(\valueAction, "Set Midi Port");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.midiOutPort};
				holdAction.setValueFunction = {arg argValue;
					argModule.midiOutPort = ControlSpec(0, 8, step: 1).constrain(argValue);
					// activate port
					argModule.midiPortActivate;
				};
				holdAction.guiObjectType = \number;
				holdAction.arrControlSpecFuncs = [ControlSpec(0, 8, step: 1);];
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// PolyphonySelector
			// N.B. no arguments
			if (item.at(0) == "PolyphonySelector", {
				holdAction = TXAction.new(\valueAction, "Set Polyphony");
				holdAction.getValueFunction = {argModule.groupPolyphony};
				holdAction.setValueFunction = {arg argValue;
					argModule.groupPolyphony = ControlSpec(1, 64, step: 1).constrain(argValue);
				};
				holdAction.guiObjectType = \number;
				holdAction.arrControlSpecFuncs = [ControlSpec(1, 64, step: 1);];
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// TXStaticText
			// arguments- index1 is row label, index2 is initial value (function or value),
			//	index3 is optional function to be valued with view as arg
			//	(e.g. for storing view to variable in module),
			// e.g. ["TXStaticText", "Record status", {recordStatus}, {arg view; recordStatusView = view}]
			if (item.at(0) == "TXStaticText", {
				holdAction = TXAction.new(\valueAction, "Get " ++ item.at(1));
				holdAction.legacyType = 1;
				holdAction.getValueFunction = item.at(2);
				holdAction.setValueFunction = {};
				// run action function passing it value as arg
				holdAction.initActionFunc = item.at(3);
				holdAction.guiObjectType = \text;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// TextViewDisplay
			//  for displaying text only - not editable
			// uneditable textview with default width set to viewWidth and height to 100
			// arguments- index1 is text, index2 is optional width
			// index3 is optional height
			// index4 is optional label
			// e.g. ["TextViewDisplay", "This module is sdfjdsklfkjk", 300, 100, "Notes"]
			if (item.at(0) == "TextViewDisplay", {
				holdAction = TXAction.new(\valueAction, "Get " ++ (item.at(4) ? "Text"));
				holdAction.getValueFunction = item.at(1);
				holdAction.setValueFunction = {};
				// run action function passing it value as arg
				holdAction.guiObjectType = \text;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// TXTextBox
			// arguments- index1 is row label, index2 is synth arg name to be updated,
			if (item.at(0) == "TXTextBox", {
				holdAction = TXAction.new(\valueAction, "Set " ++ item.at(1) ? "Text");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.getSynthArgSpec(item.at(2))};
				holdAction.setValueFunction = {arg argString;
					// store current data to synthArgSpecs
					argModule.setSynthArgSpec(item.at(2), argString);
				};
				holdAction.guiObjectType = \textedit;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// ModuleInfoTxt
			// arguments- index1 is row label, index2 is synth arg name to be updated,
			if (item.at(0) == "ModuleInfoTxt", {
				holdAction = TXAction.new(\valueAction, "Set Module Comments");
				holdAction.getValueFunction = {argModule.moduleInfoTxt};
				holdAction.setValueFunction = {arg argString;
					argModule.moduleInfoTxt = argString;
				};
				holdAction.guiObjectType = \textedit;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// TXNetAddress
			// arguments- index1 is row label, index2 is synth arg name to be updated,
			if (item.at(0) == "TXNetAddress", {
				holdAction = TXAction.new(\valueAction, "Set " ++ item.at(1));
				holdAction.getValueFunction = {argModule.getSynthArgSpec(item.at(2))};
				holdAction.setValueFunction = {arg argString;
					// store current data to synthArgSpecs
					argModule.setSynthArgSpec(item.at(2), argString);
				};
				holdAction.guiObjectType = \ipaddress;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// OSCString
			// N.B. no arguments
			if (item.at(0) == "OSCString", {
				holdAction = TXAction.new(\valueAction, "Set OSC String");
				holdAction.legacyType = 1;
				holdAction.getValueFunction = {argModule.getSynthArgSpec("OSCString")};
				holdAction.setValueFunction = {arg argString;
					// set current value in module
					argModule.oscString = argString;
					// store current data to synthArgSpecs
					argModule.setSynthArgSpec("OSCString", argString);
					// activate osc responder
					argModule.oscControlActivate;
				};
				holdAction.guiObjectType = \textedit;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// TestNoteVals
			// N.B. no arguments
			if (item.at(0) == "TestNoteVals", {
				// add commandAction to play note
				holdActionFunc = {arg val1, val2, val3;
					//  play note
					argModule.createSynthNote(val1, val2, val3);
				};
				holdAction = TXAction.new(\commandAction, "Play note: midi note + velocity + gate time ", holdActionFunc);
				holdAction.arrControlSpecFuncs =
				[\midi.asSpec.deepCopy.default_(60),
					\midi.asSpec.deepCopy.default_(100),
					ControlSpec(0.1, 20, default: 1)
				];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to Set note on
				holdActionFunc = {arg val1, val2;
					//  Set note on
					argModule.createSynthNote(val1, val2, 0);
				};
				holdAction = TXAction.new(\commandAction, "Set note on: midi note + velocity ", holdActionFunc);
				holdAction.arrControlSpecFuncs =
				[\midi.asSpec.deepCopy.default_(60),
					\midi.asSpec.deepCopy.default_(100),
				];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to Set note off
				holdActionFunc = {arg val1;
					//  Set note off
					argModule.releaseSynthGate(val1);
				};
				holdAction = TXAction.new(\commandAction, "Set note off: midi note ", holdActionFunc);
				holdAction.arrControlSpecFuncs =
				[\midi.asSpec.deepCopy.default_(60),
				];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// for Quartz Composer =======================================================================================
			// TXQCArgGui
			// arguments:
			// 	index1 is text
			//	index2 is synth arg name to be updated for the number
			//	index3 is synth arg name to be updated for the active number setting
			// 	index4 is array of all module arguments
			// 	index5 is argument index no
			// 	index6 is set argument value function
			// e.g. ["TXQCArgGui", "Particle Hue", "p003", "i_active003", arrQCArgData, 4, setArgValFunc],
			if (item.at(0) == "TXQCArgGui", {

				// argDataType can be: [0.notPassed, 1.float, 2.integer, 3.string, 4.booleanNum(0/1), 5.colour(HSBA)],

				//	RUN UPDATE ARG FUNCTION WHEN SENDING DATA

				// add value action
				holdAction = TXAction.new(\valueAction, "Set number " ++ item.at(1));
				holdControlSpecFunc = {
					var holdMin, holdMax, holdStep, holdArgDataTypeVal;
					holdMin = item.at(4).at(item.at(5)).at(2);
					holdMax = item.at(4).at(item.at(5)).at(3);
					holdArgDataTypeVal = item.at(4).at(item.at(5)).at(0);
					if (holdArgDataTypeVal == 2, {holdStep = 1;},{holdStep = 0;});
					ControlSpec(holdMin, holdMax, \lin, holdStep);
				};
				holdAction.getValueFunction = {
					var holdMin, holdMax, holdNum;
					holdMin = item.at(4).at(item.at(5)).at(2);
					holdMax = item.at(4).at(item.at(5)).at(3);
					holdNum = argModule.getSynthArgSpec(item.at(2));
					ControlSpec(holdMin, holdMax).map(holdNum);
				};
				holdAction.setValueFunction = {arg argValue;
					var holdMin, holdMax, holdStep, holdControlSpec;
					var holdArgDataTypeVal, holdNum;
					holdArgDataTypeVal = item.at(4).at(item.at(5)).at(0);
					// if correct arg data type
					if ( (holdArgDataTypeVal == 1) or: (holdArgDataTypeVal == 2) or: (holdArgDataTypeVal == 4), {
						holdMin = item.at(4).at(item.at(5)).at(2);
						holdMax = item.at(4).at(item.at(5)).at(3);
						holdControlSpec = ControlSpec(holdMin, holdMax);
						item.at(4).at(item.at(5)).put(8, argValue);
						argModule.setSynthValue(
							item.at(2),
							holdControlSpec.unmap(holdControlSpec.constrain(argValue))
						);
					});
				};
				holdAction.arrControlSpecFuncs = [holdControlSpecFunc];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);

				// add unmapped value action
				holdAction = TXAction.new(\valueAction, "Set number " ++ item.at(1) ++ " unmapped");
				holdAction.getValueFunction = {argModule.getSynthArgSpec(item.at(2))};
				holdAction.setValueFunction = {arg argValue;
					var holdMin, holdMax, holdControlSpec;
					var holdArgDataTypeVal;
					holdArgDataTypeVal = item.at(4).at(item.at(5)).at(0);
					// if correct arg data type
					if ( (holdArgDataTypeVal == 1) or: (holdArgDataTypeVal == 2) or: (holdArgDataTypeVal == 4), {
						holdMin = item.at(4).at(item.at(5)).at(2);
						holdMax = item.at(4).at(item.at(5)).at(3);
						holdControlSpec = ControlSpec(holdMin, holdMax);
						item.at(4).at(item.at(5)).put(8, holdControlSpec.map(argValue));
						argModule.setSynthValue(item.at(2), ControlSpec(0,1).constrain(argValue));
					});
				};
				holdAction.arrControlSpecFuncs = [ControlSpec(0,1);];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);

				// add commandAction to randomize value
				holdActionFunc = {
					var holdMin, holdMax, holdControlSpec;
					var holdCurVal;
					var holdArgDataTypeVal;
					holdArgDataTypeVal = item.at(4).at(item.at(5)).at(0);
					// if correct arg data type
					if ( (holdArgDataTypeVal == 1) or: (holdArgDataTypeVal == 2) or: (holdArgDataTypeVal == 4), {
						//  update with randomized value
						holdCurVal = 1.0.rand;
						holdMin = item.at(4).at(item.at(5)).at(2);
						holdMax = item.at(4).at(item.at(5)).at(3);
						holdControlSpec = ControlSpec(holdMin, holdMax);
						item.at(4).at(item.at(5)).put(8, holdControlSpec.map(holdCurVal));
						argModule.setSynthValue(item.at(2), holdCurVal);
					});
				};
				holdAction = TXAction.new(\commandAction, "Randomize number " ++ item.at(1), holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);

				// add commandAction to add to value
				holdActionFunc = { arg val1;
					var holdOldVal, holdCurVal;
					var holdMin, holdMax, holdControlSpec;
					var holdArgDataTypeVal;
					holdArgDataTypeVal = item.at(4).at(item.at(5)).at(0);
					// if correct arg data type
					if ( (holdArgDataTypeVal == 1) or: (holdArgDataTypeVal == 2) or: (holdArgDataTypeVal == 4), {
						holdMin = item.at(4).at(item.at(5)).at(2);
						holdMax = item.at(4).at(item.at(5)).at(3);
						holdControlSpec = ControlSpec(holdMin, holdMax);
						// get old value
						holdOldVal = holdControlSpec.map(argModule.getSynthArgSpec(item.at(2)));
						//  update with new value
						holdCurVal = holdOldVal + val1;
						item.at(4).at(item.at(5)).put(8, holdCurVal);
						argModule.setSynthValue(item.at(2), holdControlSpec.unmap(holdControlSpec.constrain(holdCurVal)));
					});
				};
				holdAction = TXAction.new(\commandAction, "Add to number " ++ item.at(1), holdActionFunc);
				holdAction.arrControlSpecFuncs = [{ControlSpec(0.001, 10000, \exp, 0, 1)}];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);

				// add commandAction to subtract from value
				holdActionFunc = { arg val1;
					var holdOldVal, holdCurVal;
					var holdMin, holdMax, holdControlSpec;
					var holdArgDataTypeVal;
					holdArgDataTypeVal = item.at(4).at(item.at(5)).at(0);
					// if correct arg data type
					if ( (holdArgDataTypeVal == 1) or: (holdArgDataTypeVal == 2) or: (holdArgDataTypeVal == 4), {
						holdMin = item.at(4).at(item.at(5)).at(2);
						holdMax = item.at(4).at(item.at(5)).at(3);
						holdControlSpec = ControlSpec(holdMin, holdMax);
						// get old value
						holdOldVal = holdControlSpec.map(argModule.getSynthArgSpec(item.at(2)));
						//  update with new value
						holdCurVal = holdOldVal - val1;
						item.at(4).at(item.at(5)).put(8, holdCurVal);
						argModule.setSynthValue(item.at(2), holdControlSpec.unmap(holdControlSpec.constrain(holdCurVal)));
					});
				};
				holdAction = TXAction.new(\commandAction, "Subtract from number " ++ item.at(1), holdActionFunc);
				holdAction.arrControlSpecFuncs = [{ControlSpec(0.001, 10000, \exp, 0, 1)}];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);

				// add valueAction to set String
				holdAction = TXAction.new(\valueAction, "Set string " ++ item.at(1));
				holdAction.getValueFunction = {item.at(4).at(item.at(5)).at(1)};
				holdAction.setValueFunction = {arg argString;
					var holdArgDataTypeVal;
					holdArgDataTypeVal = item.at(4).at(item.at(5)).at(0);
					// if correct arg data type
					if ( holdArgDataTypeVal == 3, {
						item.at(4).at(item.at(5)).put(1, argString);
						{item.at(6).value;}.defer(0.1);
					});
				};
				holdAction.guiObjectType = \textedit;
				arrActionSpecs = arrActionSpecs.add(holdAction);

				// add commandAction to set colour
				holdActionFunc = {arg val1, val2, val3, val4;
					var holdArgDataTypeVal;
					holdArgDataTypeVal = item.at(4).at(item.at(5)).at(0);
					// if correct arg data type
					if ( holdArgDataTypeVal == 5, {
						item.at(4).at(item.at(5)).put(4, val1);
						item.at(4).at(item.at(5)).put(5, val2);
						item.at(4).at(item.at(5)).put(6, val3);
						item.at(4).at(item.at(5)).put(7, val4);
						{item.at(6).value;}.defer(0.1);
					});
				};
				holdAction = TXAction.new(\commandAction, "Set colour[RGBA] " ++ item.at(1), holdActionFunc);
				holdAction.arrControlSpecFuncs = [ControlSpec(0,1), ControlSpec(0,1), ControlSpec(0,1), ControlSpec(0,1)];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			//=========================================================================================================

			// RunPauseButton
			if (item.at(0) == "RunPauseButton", {
				// add commandAction to pause module
				holdActionFunc = {
					argModule.pauseAction;
				};
				holdAction = TXAction.new(\commandAction, "Pause module", holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to unpause module
				holdActionFunc = {
					argModule.runAction;
				};
				holdAction = TXAction.new(\commandAction, "Unpause module", holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to toggle value
				holdActionFunc = {
					// take action depending on moduleNodeStatus
					if (argModule.moduleNodeStatus == "running", {
						argModule.pauseAction;
					}, {
						argModule.runAction;
					});
				};
				holdAction = TXAction.new(\commandAction, "Toggle Pause-Unpause module", holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// RebuildModuleButton
			if (item.at(0) == "RebuildModuleButton", {
				// add commandAction to rebuild module
				holdActionFunc = {
					argModule.rebuildSynth;
				};
				holdAction = TXAction.new(\commandAction, "Rebuild module", holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// LoadModulePreset
			if (item.at(0) == "LoadModulePreset", {
				// add commandAction to LoadModulePreset
				holdActionFunc = {
					argModule.rebuildSynth;
				};
				holdAction = TXAction.new(\commandAction, "Load preset", holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// Add a final dummy commandAction that acts as a line separator in action popups
			if (arrActionSpecs.size > actionCounter, {
				holdActionFunc = {};
				holdAction = TXAction.new(\commandAction, "---", holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});
			// set variable
			actionCounter = arrActionSpecs.size;

		}); // end of argArrSpecs.do

		// return built array
		^arrActionSpecs;
	}

}
