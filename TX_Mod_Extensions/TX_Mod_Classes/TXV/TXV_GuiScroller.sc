// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_GuiScroller {
	var <>arrControls, <>scrollView, system, holdBackgroundBox, holdArrGuis, holdArrAssetIDs;

	*new { arg argSystem, argParent, dimensions, argModule, argArrParameters, argDictCurrVals,
		argSendParameterFunc, scrollViewAction, dictParameterGroups;
		^super.new.init(argSystem, argParent, dimensions, argModule, argArrParameters, argDictCurrVals,
			argSendParameterFunc, scrollViewAction, dictParameterGroups);
	}
	init { arg argSystem, argParent, dimensions, argModule, argArrParameters, argDictCurrVals,
		argSendParameterFunc, scrollViewAction, dictParameterGroups;
		var holdView, holdItems, scrollBox, totalHeight, keptParameters, hiddenGroups;
		var dictViews = ();
		holdArrAssetIDs = ();

		system = argSystem;
		hiddenGroups = [];
		dictParameterGroups.select({arg item, i; item == false;}).keysDo({arg item, i;
			hiddenGroups = hiddenGroups.add(item.asString);
		});

		// reset variables
		holdArrGuis = ();
		totalHeight = 0;
		keptParameters = argArrParameters.select({arg item, i;
			var keep = true;
			// ignore parameter if group is off
			if ((item[0] != "groupStart") and: (item[1].split.size > 2), {
				// if any of the hidden group names match the start of the address
				hiddenGroups.do({arg groupName, i;
					if (item[1].keep(groupName.size) == groupName, {
						keep = false;
					});
				});
			});
			if (keep, {
				if (item[0].keep("modParameterGroup".size) == "modParameterGroup", {
					totalHeight = totalHeight + 60;
				}, {
					if(item[0] == "float256", {
						totalHeight = totalHeight + 350;
					}, {
						totalHeight = totalHeight + 38;
					});
				});
			});
			keep;
		});
		// add ScrollView
		scrollView = ScrollView(argParent, Rect(0, 0, dimensions.x, dimensions.y))
		.hasBorder_(false);
		if (GUI.current.asSymbol == \cocoa, {
			scrollView.autoScrolls_(false);
		});
		scrollView.background_(TXColour.sysChannelControl);
		scrollView.action = scrollViewAction;
		scrollView.hasHorizontalScroller = false;
		scrollView.hasVerticalScroller = true;
		scrollBox = CompositeView(scrollView, Rect(0, 0, dimensions.x, 20 + totalHeight));
		scrollBox.background_(TXColour.sysChannelControl);

		// TESTING XXX - WITHOUT SCROLLVIEW
		// argParent.bounds.height = 120 + totalHeight;
		// scrollBox = CompositeView(argParent, Rect(0, 0, dimensions.x, 20 + totalHeight));

		scrollBox.decorator = FlowLayout(scrollBox.bounds);
		scrollBox.decorator.margin.x = 0;
		scrollBox.decorator.margin.y = 0;
		scrollBox.decorator.reset;

		keptParameters.do({ arg item, i;
			var holdVal, holdSoftMin, holdSoftMax, holdHardMin, holdHardMax;
			var holdFixedValueUnmapped, holdFixedValue, holdFixedModMix, holdUseExtMod, holdAction, holdString;
			var holdAddress = item[1];
			var holdAddressSymbol = holdAddress.asSymbol;
			var holdAddressSplit = item[1].split;
			var holdIndent = (holdAddressSplit.size - 3) * 40;
			var holdName = holdAddressSplit.last;
			var holdNameSymbol = holdName.asSymbol;
			var fixedValueIndex = 0;
			var softMinIndex = 4;
			var labelStrings, labelView, popupView;
			var defaultValue;
			var arrGridPresetNames, arrGridPresetActions;

			//scrollBox.decorator.shift(holdIndent, 2);

			case

			{item[0] == "groupStart"} {
				holdView = StaticText(scrollBox, 300 @ 20);
				holdView.string = holdName;
				holdView.stringColor_(TXColour.white).background_(TXColor.sysGuiCol1);
				holdView.align_(\left);
				holdArrGuis.put(holdAddressSymbol, holdView);

				holdView = Button(scrollBox, 20 @ 20);
				if (hiddenGroups.indexOfEqual(item[1]).notNil, {
					holdView.states = [
						["+", TXColor.white, TXColor.sysGuiCol1]
					];
					holdView.action = {|view|
						dictParameterGroups[item[1].asSymbol] = true;
						argSystem.showView;
					};
					}, {
						holdView.states = [
							["-", TXColor.white, TXColor.sysGuiCol1]
						];
						holdView.action = {|view|
							dictParameterGroups[item[1].asSymbol] = false;
							argSystem.showView;
						};
				});
			}

			{item[0] == "modParameterGroupInt"} {
				var holdMMSliderView, popupView;
				holdFixedValue = argDictCurrVals[(holdAddress ++ "/fixedValue").asSymbol];
				holdFixedModMix = argDictCurrVals[(holdAddress ++ "/fixedModMix").asSymbol];
				holdUseExtMod = argDictCurrVals[(holdAddress ++ "/useExtMod").asSymbol];
				holdSoftMin = argDictCurrVals[(holdAddress ++ "/softMin").asSymbol];
				holdSoftMax = argDictCurrVals[(holdAddress ++ "/softMax").asSymbol];
				holdFixedValueUnmapped = ControlSpec(holdSoftMin, holdSoftMax, 'lin', step: 1).unmap(holdFixedValue);
				holdHardMin = item[2][softMinIndex][3];
				holdHardMax = item[2][softMinIndex][4];
				labelStrings = item[2][fixedValueIndex][5];
				holdAction = {arg view;
					var arrVals = view.valueSplit;
					var valMapped = ControlSpec(arrVals[1], arrVals[2], 'lin', step: 1).map(arrVals[0]);
					argDictCurrVals[(holdAddress ++ "/fixedValue").asSymbol] = valMapped.asInteger;
					argSendParameterFunc.value(holdAddress ++ "/fixedValue", valMapped.asInteger);
					argDictCurrVals[(holdAddress ++ "/softMin").asSymbol] = arrVals[1].asInteger;
					argSendParameterFunc.value(holdAddress ++ "/softMin", arrVals[1].asInteger);
					argDictCurrVals[(holdAddress ++ "/softMax").asSymbol] = arrVals[2].asInteger;
					argSendParameterFunc.value(holdAddress ++ "/softMax", arrVals[2].asInteger);
					// if (labelView.notNil, {
					// 	labelView.string = labelStrings[valMapped.asInteger - holdHardMin];
					// });
					if (popupView.notNil, {
						popupView.items = labelStrings[arrVals[1].asInteger - holdHardMin .. arrVals[2].asInteger - holdHardMin];
						popupView.value = (valMapped - arrVals[1]).asInteger;
					});
				};
				holdMMSliderView = TXMinMaxSlider(scrollBox, 550 @ 44, holdName,
						ControlSpec(holdHardMin, holdHardMax, 'lin', step: 1), holdAction, labelWidth:200);
				holdMMSliderView.valueSplit_([holdFixedValueUnmapped, holdSoftMin, holdSoftMax]);
				//holdMMSliderView.valueNoAction_(holdFixedValue);
				holdMMSliderView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdMMSliderView.labelView2.stringColor_(TXColour.sysGuiCol1).background_(TXColor.grey(0.9));
				holdMMSliderView.minNumberView.stringColor_(TXColour.black).background_(TXColor.grey(0.9));
				holdMMSliderView.maxNumberView.stringColor_(TXColour.black).background_(TXColor.grey(0.9));
				holdMMSliderView.sliderView.background_(TXColour.sysGuiCol1);
				holdMMSliderView.rangeView.background_(TXColour.sysGuiCol1.blend(TXColor.grey(0.55), 0.75));
				holdArrGuis.put((holdAddress ++ "/fixedValue").asSymbol, holdMMSliderView);

				if (labelStrings.notNil, {
					// shift
					scrollBox.decorator.shift(0, -24);
					// labelView = StaticText(scrollBox, 250 @ 20).stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
					// labelView.string = labelStrings[holdFixedValue.asInteger - holdHardMin];
					// labelView.align = \left;
					popupView = PopUpMenu(scrollBox, 436 @ 20).stringColor_(TXColour.grey(0.1)).background_(TXColor.paleYellow);
					popupView.items = labelStrings[(holdSoftMin - holdHardMin) .. (holdSoftMax - holdHardMin)];
					popupView.action = {arg view;
						holdMMSliderView.value = view.value + holdMMSliderView.minNumberView.value;
					};
					popupView.value = holdFixedValue.asInteger - holdSoftMin;
					holdArrGuis.put((holdAddress ++ "/popup").asSymbol, popupView);
					// create plus and minus buttons
					[1,-1].do({arg item, i;
						var holdString;
						if (item.isPositive, {
							holdString = "+";
							}, {
							holdString = "-";
						});
						Button(scrollBox, 18 @ 20)
						.states_([[holdString, TXColor.white, TXColor.sysGuiCol1]])
						.action_({|view|
							popupView.valueAction = (popupView.value + item).max(0).min(popupView.items.size - 1);
						});
					});
					// shift back
					scrollBox.decorator.shift(0, 24);
				});

				scrollBox.decorator.nextLine;
				// scrollBox.decorator.shift(holdIndent, 0);
				scrollBox.decorator.shift(holdIndent + 552, -24);

				// default button
				Button(scrollBox, 40 @ 20)
				.states_([ ["default", TXColor.white, TXColor.sysGuiCol2] ])
				.action_({|view| argModule.restoreDefaultParameter(holdAddress); });

				holdView = TXSlider(scrollBox, 280 @ 20, "modulation mix", ControlSpec(0, 1),
					{arg view; argDictCurrVals[(holdAddress ++ "/fixedModMix").asSymbol] = view.value;
						argSendParameterFunc.value(holdAddress ++ "/fixedModMix", view.value);
						if (view.value > 0, {
							view.numberView.background_(TXColor.paleGreen2);
						}, {
							view.numberView.background_(TXColor.grey7);
						});
					},
					initVal: holdFixedModMix, labelWidth:90, numberWidth: 40);
				holdView.labelView.stringColor_(TXColour.sysGuiCol2).background_(TXColor.grey(0.85));
				holdView.sliderView.background_(TXColour.sysGuiCol1);
				if (holdView.value > 0, {
					holdView.numberView.background_(TXColor.paleGreen2);
				}, {
					holdView.numberView.background_(TXColor.grey7);
				});
				holdArrGuis.put((holdAddress ++ "/fixedModMix").asSymbol, holdView);

				holdView = TXCheckBox(scrollBox, 154 @ 20, "use external modulation",  TXColor.sysGuiCol1, TXColour.grey(0.8),
				TXColor.white, TXColor.sysGuiCol1);
				holdView.action = {arg view; var val;
					val = view.value.asInteger;
					argDictCurrVals[(holdAddress ++ "/useExtMod").asSymbol] = val;
					argSendParameterFunc.value(holdAddress ++ "/useExtMod", val);
				};
				if (holdView.notNil, {
						holdView.value = holdUseExtMod.asBoolean;
					});
				holdArrGuis.put((holdAddress ++ "/useExtMod").asSymbol, holdView);
			}

			{item[0] == "modParameterGroupFloat"} {
				holdFixedValue = argDictCurrVals[(holdAddress ++ "/fixedValue").asSymbol];
				holdFixedModMix = argDictCurrVals[(holdAddress ++ "/fixedModMix").asSymbol];
				holdUseExtMod = argDictCurrVals[(holdAddress ++ "/useExtMod").asSymbol];
				holdSoftMin = argDictCurrVals[(holdAddress ++ "/softMin").asSymbol];
				holdSoftMax = argDictCurrVals[(holdAddress ++ "/softMax").asSymbol];
				holdFixedValueUnmapped = ControlSpec(holdSoftMin, holdSoftMax, 'lin').unmap(holdFixedValue);
				holdHardMin = item[2][softMinIndex][3];
				holdHardMax = item[2][softMinIndex][4];
				holdAction = {arg view;
					var arrVals = view.valueSplit;
					var valMapped = ControlSpec(arrVals[1], arrVals[2], 'lin').map(arrVals[0]);
					argDictCurrVals[(holdAddress ++ "/fixedValue").asSymbol] = valMapped;
					argSendParameterFunc.value(holdAddress ++ "/fixedValue", valMapped);
					argDictCurrVals[(holdAddress ++ "/softMin").asSymbol] = arrVals[1];
					argSendParameterFunc.value(holdAddress ++ "/softMin", arrVals[1]);
					argDictCurrVals[(holdAddress ++ "/softMax").asSymbol] = arrVals[2];
					argSendParameterFunc.value(holdAddress ++ "/softMax", arrVals[2]);
				};
				holdView = TXMinMaxSlider(scrollBox, 550 @ 44, holdName,
						ControlSpec(holdHardMin, holdHardMax, 'lin'), holdAction, labelWidth:200);
				holdView.valueSplit_([holdFixedValueUnmapped, holdSoftMin, holdSoftMax]);
				//holdView.valueNoAction_(holdFixedValue);
				holdView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdView.labelView2.stringColor_(TXColour.sysGuiCol1).background_(TXColor.grey(0.9));
				holdView.minNumberView.stringColor_(TXColour.black).background_(TXColor.grey(0.9));
				holdView.maxNumberView.stringColor_(TXColour.black).background_(TXColor.grey(0.9));
				holdView.sliderView.background_(TXColour.sysGuiCol1);
				holdView.rangeView.background_(TXColour.sysGuiCol1.blend(TXColor.grey(0.55), 0.75));
				holdArrGuis.put((holdAddress ++ "/fixedValue").asSymbol, holdView);

				scrollBox.decorator.nextLine;
				// scrollBox.decorator.shift(holdIndent, 0);
				scrollBox.decorator.shift(holdIndent + 552, -24);

				// default button
				Button(scrollBox, 40 @ 20)
				.states_([ ["default", TXColor.white, TXColor.sysGuiCol2] ])
				.action_({|view| argModule.restoreDefaultParameter(holdAddress); });

				holdView = TXSlider(scrollBox, 280 @ 20, "modulation mix", ControlSpec(0, 1),
					{arg view; argDictCurrVals[(holdAddress ++ "/fixedModMix").asSymbol] = view.value;
						argSendParameterFunc.value(holdAddress ++ "/fixedModMix", view.value);
						if (view.value > 0, {
							view.numberView.background_(TXColor.paleGreen2);
						}, {
							view.numberView.background_(TXColor.grey7);
						});
					},
					initVal: holdFixedModMix, labelWidth:90, numberWidth: 40);
				holdView.labelView.stringColor_(TXColour.sysGuiCol2).background_(TXColor.grey(0.85));
				holdView.sliderView.background_(TXColour.sysGuiCol1);
				if (holdView.value > 0, {
					holdView.numberView.background_(TXColor.paleGreen2);
				}, {
					holdView.numberView.background_(TXColor.grey7);
				});
				holdArrGuis.put((holdAddress ++ "/fixedModMix").asSymbol, holdView);

				holdView = TXCheckBox(scrollBox, 154 @ 20, "use external modulation",  TXColor.sysGuiCol1, TXColour.grey(0.8),
				TXColor.white, TXColor.sysGuiCol1);
				holdView.action = {arg view; var val;
					val = view.value.asInteger;
					argDictCurrVals[(holdAddress ++ "/useExtMod").asSymbol] = val;
					argSendParameterFunc.value(holdAddress ++ "/useExtMod", val);
				};
				if (holdView.notNil, {
						holdView.value = holdUseExtMod.asBoolean;
					});
				holdArrGuis.put((holdAddress ++ "/useExtMod").asSymbol, holdView);
			}

			{item[0] == "modParameterGroupBool"} {
				holdFixedValue = argDictCurrVals[(holdAddress ++ "/fixedValue").asSymbol];
				holdFixedModMix = argDictCurrVals[(holdAddress ++ "/fixedModMix").asSymbol];
				holdUseExtMod = argDictCurrVals[(holdAddress ++ "/useExtMod").asSymbol];
				// label
				StaticText.new(scrollBox, 200 @ 20)
					.stringColor_(TXColor.grey(0.1))
					.backColor_(TXColor.white)
					.string_(holdName)
					.align_(\right);

				holdView = TXCheckBox(scrollBox, 344 @ 20, holdName, TXColor.sysGuiCol1, TXColour.grey(0.8),
					TXColor.white, TXColor.sysGuiCol1);
					holdView.action = {arg view; var val;
						val = view.value.asInteger;
						argDictCurrVals[(holdAddress ++ "/fixedValue").asSymbol] = val;
						argSendParameterFunc.value(holdAddress ++ "/fixedValue", val);
					};
				if (holdView.notNil, {
						holdView.value = holdFixedValue.asBoolean;
					});
				holdArrGuis.put((holdAddress ++ "/fixedValue").asSymbol, holdView);

				// default button
				Button(scrollBox, 40 @ 20)
				.states_([ ["default", TXColor.white, TXColor.sysGuiCol2] ])
				.action_({|view| argModule.restoreDefaultParameter(holdAddress); });

				holdView = TXCheckBox(scrollBox, 280 @ 20, "modulation mix", TXColor.sysGuiCol2, TXColour.grey(0.8),
					TXColor.white, TXColor.sysGuiCol2);
				holdView.action = {arg view; var val;
					val = view.value.asInteger;
					argDictCurrVals[(holdAddress ++ "/fixedModMix").asSymbol] = val;
						argSendParameterFunc.value(holdAddress ++ "/fixedModMix", val);
				};
				if (holdView.notNil, {
						holdView.value = holdFixedModMix.asBoolean;
					});
				holdArrGuis.put((holdAddress ++ "/fixedModMix").asSymbol, holdView);

				holdView = TXCheckBox(scrollBox, 154 @ 20, "use external modulation",  TXColor.sysGuiCol1, TXColour.grey(0.8),
				TXColor.white, TXColor.sysGuiCol1);
				holdView.action = {arg view; var val;
					val = view.value.asInteger;
					argDictCurrVals[(holdAddress ++ "/useExtMod").asSymbol] = val;
					argSendParameterFunc.value(holdAddress ++ "/useExtMod", val);
				};
				if (holdView.notNil, {
						holdView.value = holdUseExtMod.asBoolean;
					});
				holdArrGuis.put((holdAddress ++ "/useExtMod").asSymbol, holdView);
			}

			{item[0] == "int"} {
				var holdSliderView, popupView;
				holdSliderView = TXSlider(scrollBox, 550 @ 20, holdName, ControlSpec(item[3], item[4], 'lin', step: 1),
					{arg view;
						argDictCurrVals[holdAddressSymbol] = view.value.asInteger;
						argSendParameterFunc.value(holdAddress, view.value.asInteger);
						// if (labelView.notNil, {
						// 	labelView.string = labelStrings[view.value.asInteger - item[3]];
						// });
						if (popupView.notNil, {
							popupView.items = labelStrings;
							popupView.value = (view.value - item[3]).asInteger;
						});
					},
					initVal: argDictCurrVals[holdAddressSymbol], labelWidth:200);
				holdSliderView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdSliderView.sliderView.background_(TXColour.sysGuiCol1);
				holdArrGuis.put(holdAddressSymbol, holdSliderView);
				// default button
				Button(scrollBox, 40 @ 20)
				.states_([ ["default", TXColor.white, TXColor.sysGuiCol2] ])
				.action_({|view| argModule.restoreDefaultParameter(holdAddress); });

				labelStrings = item[5];
				if (labelStrings.notNil, {
					// labelView = StaticText(scrollBox, 250 @ 20).stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
					// labelView.string = labelStrings[item[3].asInteger - item[3]]; // adjust to minVal
					// labelView.align = \left;
					popupView = PopUpMenu(scrollBox, 392 @ 20).stringColor_(TXColour.grey(0.1)).background_(TXColor.paleYellow);
					popupView.items = labelStrings;
					popupView.value = argDictCurrVals[holdAddressSymbol].asInteger - item[3].asInteger;
					popupView.action = {arg view;
						holdSliderView.valueAction = view.value + item[3];
					};
					// create plus and minus buttons
					[-1,1].do({arg item, i;
						var holdString;
						if (item.isPositive, {
							holdString = "+";
							}, {
							holdString = "-";
						});
						Button(scrollBox, 18 @ 20)
						.states_([[holdString, TXColor.white, TXColor.sysGuiCol1]])
						.action_({|view|
							popupView.valueAction = (popupView.value + item).max(0).min(popupView.items.size - 1);
						});
					});
					holdArrGuis.put((holdAddress ++ "/popup").asSymbol, popupView);
				});
			}

			{item[0] == "float"} {
				holdView = TXSlider(scrollBox, 550 @ 20, holdName, ControlSpec(item[3], item[4], 'lin'),
					{arg view; argDictCurrVals[holdAddressSymbol] = view.value;
							argSendParameterFunc.value(holdAddress, view.value)},
					initVal: argDictCurrVals[holdAddressSymbol], labelWidth:200);
				holdView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdView.sliderView.background_(TXColour.sysGuiCol1);
				holdArrGuis.put(holdAddressSymbol, holdView);

							// default button
				Button(scrollBox, 40 @ 20)
				.states_([ ["default", TXColor.white, TXColor.sysGuiCol2] ])
				.action_({|view| argModule.restoreDefaultParameter(holdAddress); });
			}

			{item[0] == "float256"} {
				// TXCurveDraw::new { arg window, dimensions, label, action, initVal,
				// initAction=false, labelWidth=80, initSlotVals,
				// showPresets, curveWidth=270, curveHeight=257, resetAction="Ramp",
				// gridRowsFunc, gridColsFunc, xLabel, yLabel;
				var holdCurveDraw;
				holdCurveDraw = holdView = TXCurveDraw.new(scrollBox, 550 @ 300, holdName,
					{arg view; var holdArray = view.value.round(0.001);
						argDictCurrVals[holdAddressSymbol] = holdArray;
						argSendParameterFunc.value(holdAddress, holdArray);
						argModule.arrSlotData = view.arrSlotData;
					},
					argDictCurrVals[holdAddressSymbol], false, 200, argModule.arrSlotData,
					"Curve", 519, 263, "Max",
					argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows,
					argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols,
					"position", "value", curveThumbSize:2, dataEvent: argModule.data.dictCurveDataEvents[item[1].asSymbol];
				);
				holdView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put(holdAddressSymbol, holdView);

				// ["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 99), "gridRows", {system.showView}],
				// ["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 99), "gridCols", {system.showView}],
				// ["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 200],
				// ["ActionButton", "Quantise to grid", {this.quantiseToGrid}, 130],

				scrollBox.decorator.nextLine;
				scrollBox.decorator.shift(204, 0);

								// default button
				Button(scrollBox, 40 @ 20)
				.states_([ ["default", TXColor.white, TXColor.sysGuiCol2] ])
				.action_({|view| argModule.restoreDefaultParameter(holdAddress); });

				holdView = TXNumberPlusMinus2(scrollBox, 150 @ 20, "Grid rows", ControlSpec(1, 99),
					{|view|
						// store current data to synthArgSpecs
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows = view.value;
						argSystem.showView;
					},
					// get starting value
					argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows,
					false, 80, 40
				);
				argModule.arrControls = argModule.arrControls.add(holdView);
				holdView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdView.round_(0.001);

				scrollBox.decorator.shift(10,0);
				holdView = TXNumberPlusMinus2(scrollBox, 150 @ 20, "Grid columns", ControlSpec(1, 99),
					{|view|
						// store current data to synthArgSpecs
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols = view.value;
						argSystem.showView;
					},
					// get starting value
					argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols,
					false, 80, 40
				);
				argModule.arrControls = argModule.arrControls.add(holdView);
				holdView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdView.round_(0.001);

				arrGridPresetNames = ["1 x 1", "2 x 2", "3 x 3", "4 x 4", "5 x 5", "6 x 6", "8 x 8", "9 x 9",
					"10 x 10", "12 x 12", "16 x 16", "24 x 24", "32 x 32"];
				arrGridPresetActions = [
					{argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows =  1;
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols = 1; },
					{argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows =  2;
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols = 2; },
					{argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows =  3;
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols = 3; },
					{argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows =  4;
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols = 4; },
					{argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows =  5;
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols = 5; },
					{argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows =  6;
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols = 6; },
					{argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows =  8;
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols = 8; },
					{argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows =  9;
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols = 9; },
					{argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows =  10;
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols = 10; },
					{argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows =  12;
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols = 12; },
					{argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows =  16;
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols = 16; },
					{argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows =  24;
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols = 24; },
					{argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows =  32;
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols = 32; },
				];
				scrollBox.decorator.shift(10,0);
				holdView = TXPopup(scrollBox, 200 @ 20, "Grid presets",
					// add first item in popup
					["Select preset..."] ++ arrGridPresetNames,
					{|view|
						// if not first item in popup
						if (view.value > 0, {
							// value selected preset from preset actions array
							arrGridPresetActions.at(view.value-1).value;
						});
						// recreate view
						argSystem.showView;
					},
					0 // initial val
				);
				argModule.arrControls = argModule.arrControls.add(holdView);
				holdView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdView.popupMenuView.stringColor_(TXColour.black).background_(TXColor.white);

				scrollBox.decorator.shift(10,0);
				holdView = Button(scrollBox, 130 @ 20)
				.states_([
					["Quantise to grid", TXColor.white, TXColor.sysGuiCol1]
				])
				.action_({
					holdCurveDraw.valueAction = this.quantiseToGrid(holdCurveDraw.value,
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridRows,
						argModule.data.dictCurveDataEvents[item[1].asSymbol].gridCols);
					//argSystem.showView;
				});
			}

			{item[0] == "bool_int"} {
				// label
				StaticText.new(scrollBox, 200 @ 20)
					.stringColor_(TXColor.grey(0.1))
					.backColor_(TXColor.white)
					.string_(holdName)
					.align_(\right);
				holdView = TXCheckBox(scrollBox, 344 @ 20, holdName,  TXColor.sysGuiCol1, TXColour.grey(0.8),
					TXColor.white, TXColor.sysGuiCol1);
				holdView.action = {arg view; var val;
					val = view.value.asInteger;
					argDictCurrVals[holdAddressSymbol] = val;
					argSendParameterFunc.value(holdAddress, val);
				};
				holdVal = argDictCurrVals[holdAddressSymbol];
				if (holdView.notNil, {
						holdView.value = holdVal.asBoolean;
					});
				holdArrGuis.put(holdAddressSymbol, holdView);

				// default button
				Button(scrollBox, 40 @ 20)
				.states_([ ["default", TXColor.white, TXColor.sysGuiCol2] ])
				.action_({|view| argModule.restoreDefaultParameter(holdAddress); });
			}
			{item[0] == "string"} {
				holdView = TXTextBox(scrollBox, 990 @ 20, holdName,
					{arg view; argDictCurrVals[holdAddressSymbol] = view.string;
							argSendParameterFunc.value(holdAddress, view.string)},
					labelWidth:200
				);
				holdView.string = argDictCurrVals[holdAddressSymbol];
				holdView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put(holdAddressSymbol, holdView);

				// default button
				Button(scrollBox, 40 @ 20)
				.states_([ ["default", TXColor.white, TXColor.sysGuiCol2] ])
				.action_({|view| argModule.restoreDefaultParameter(holdAddress); });
			}
			{item[0] == "assetSlot/image"} {
				var selectedAssets, selectedBankNames, holdItems, holdInitVal;
				// asset: [assetType, assetTypeString, assetID, assetFilename, fileExists, assetBankNo]
				// select image assets only
				selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
					item[0] == 0 and: {(item[5] ? 0) == argDictCurrVals[holdAddressSymbol][1]};
				});
				selectedBankNames = argModule.parentTXVSystem.arrAssetBankNames[0];
				// bank
				dictViews.imageBankView = TXPopupPlusMinus(scrollBox, 990 @ 20, this.convertAssetToBankName(holdName),
					selectedBankNames.collect({arg bankName, i; (i + 1).asString ++ ":" + bankName;}),
					{arg view;
						var assetID = 0;  // reset asset to zero if bank changes
						var assetBank = view.value;
						argDictCurrVals[holdAddressSymbol] = [assetID, assetBank];
						argSendParameterFunc.value(holdAddress, [assetID, assetBank]);
						argModule.class.system.showView;
					},
					// init val
					argDictCurrVals[holdAddressSymbol][1], labelWidth: 200
				);
				holdArrAssetIDs[holdNameSymbol] = [0]; // default is 0
				holdItems = ["empty"];
				selectedAssets.do({arg item, i;
					holdArrAssetIDs[holdNameSymbol] = holdArrAssetIDs[holdNameSymbol].add(item[2]);
					holdItems = holdItems.add((i + 1).asString ++ ": " ++ item[3]);
				});
				dictViews.imageBankView .labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put((holdAddress ++ "/Bank").asSymbol, dictViews.imageBankView );
				// asset
				dictViews.imageAssetView = TXPopupPlusMinus(scrollBox, 990 @ 20, holdName, holdItems,
					{arg view;
						var assetID = holdArrAssetIDs[holdNameSymbol][view.value];
						var assetBank = dictViews.imageBankView.value;
						argDictCurrVals[holdAddressSymbol] = [assetID, assetBank];
						argSendParameterFunc.value(holdAddress, [assetID, assetBank]);
					},
					// init val
					holdArrAssetIDs[holdNameSymbol].indexOf(argDictCurrVals[holdAddressSymbol][0]), labelWidth: 200
				);
				dictViews.imageAssetView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put((holdAddress ++ "/Asset").asSymbol, dictViews.imageAssetView);
				// default button
				Button(scrollBox, 40 @ 20)
				.states_([ ["default", TXColor.white, TXColor.sysGuiCol2] ])
				.action_({|view| argModule.restoreDefaultParameter(holdAddress); });
			}
			{item[0] == "assetSlot/movie"} {
				var selectedAssets, selectedBankNames, holdItems, holdInitVal;
				// select movie assets only
				selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
					item[0] == 1 and: {(item[5] ? 0) == argDictCurrVals[holdAddressSymbol][1]};
				});
				selectedBankNames = argModule.parentTXVSystem.arrAssetBankNames[1];
				// bank
				dictViews.movieBankView = TXPopupPlusMinus(scrollBox, 990 @ 20, this.convertAssetToBankName(holdName),
					selectedBankNames.collect({arg item, i; (i + 1).asString ++ ":" + item;}),
					{arg view;
						var assetID = 0;  // reset asset to zero if bank changes
						var assetBank = view.value;
						argDictCurrVals[holdAddressSymbol] = [assetID, assetBank];
						argSendParameterFunc.value(holdAddress, [assetID, assetBank]);
						argModule.class.system.showView;
					},
					// init val
					argDictCurrVals[holdAddressSymbol][1], labelWidth: 200
				);
				holdArrAssetIDs[holdNameSymbol] = [0]; // default is 0
				holdItems = ["empty"];
				selectedAssets.do({arg item, i;
					holdArrAssetIDs[holdNameSymbol] = holdArrAssetIDs[holdNameSymbol].add(item[2]);
					holdItems = holdItems.add(item[3]);
				});
				dictViews.movieBankView .labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put((holdAddress ++ "/Bank").asSymbol, dictViews.movieBankView );
				// asset
				dictViews.movieAssetView = TXPopupPlusMinus(scrollBox, 990 @ 20, holdName, holdItems,
					{arg view;
						var assetID = holdArrAssetIDs[holdNameSymbol][view.value];
						var assetBank = dictViews.movieBankView.value;
						argDictCurrVals[holdAddressSymbol] = [assetID, assetBank];
						argSendParameterFunc.value(holdAddress, [assetID, assetBank]);
					},
					// init val
					holdArrAssetIDs[holdNameSymbol].indexOf(argDictCurrVals[holdAddressSymbol][0]), labelWidth: 200
				);
				dictViews.movieAssetView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put((holdAddress ++ "/Asset").asSymbol, dictViews.movieAssetView);
			}
			{item[0] == "assetSlot/svg"} {
				var selectedAssets, selectedBankNames, holdItems, holdInitVal;
				// select svg assets only
				selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
					item[0] == 2 and: {(item[5] ? 0) == argDictCurrVals[holdAddressSymbol][1]};
				});
				selectedBankNames = argModule.parentTXVSystem.arrAssetBankNames[2];
				// bank
				dictViews.svgBankView = TXPopupPlusMinus(scrollBox, 990 @ 20, this.convertAssetToBankName(holdName),
					selectedBankNames.collect({arg item, i; (i + 1).asString ++ ":" + item;}),
					{arg view;
						var assetID = 0;  // reset asset to zero if bank changes
						var assetBank = view.value;
						argDictCurrVals[holdAddressSymbol] = [assetID, assetBank];
						argSendParameterFunc.value(holdAddress, [assetID, assetBank]);
						argModule.class.system.showView;
					},
					// init val
					argDictCurrVals[holdAddressSymbol][1], labelWidth: 200
				);
				holdArrAssetIDs[holdNameSymbol] = [0]; // default is 0
				holdItems = ["empty"];
				selectedAssets.do({arg item, i;
					holdArrAssetIDs[holdNameSymbol] = holdArrAssetIDs[holdNameSymbol].add(item[2]);
					holdItems = holdItems.add(item[3]);
				});
				dictViews.svgBankView .labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put((holdAddress ++ "/Bank").asSymbol, dictViews.svgBankView );
				// asset
				dictViews.svgAssetView = TXPopupPlusMinus(scrollBox, 990 @ 20, holdName, holdItems,
					{arg view;
						var assetID = holdArrAssetIDs[holdNameSymbol][view.value];
						var assetBank = dictViews.svgBankView.value;
						argDictCurrVals[holdAddressSymbol] = [assetID, assetBank];
						argSendParameterFunc.value(holdAddress, [assetID, assetBank]);
					},
					// init val
					holdArrAssetIDs[holdNameSymbol].indexOf(argDictCurrVals[holdAddressSymbol][0]), labelWidth: 200
				);
				dictViews.svgAssetView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put((holdAddress ++ "/Asset").asSymbol, dictViews.svgAssetView);
			}
			{item[0] == "assetSlot/cube"} {
				var selectedAssets, selectedBankNames, holdItems, holdInitVal;
				// select cube assets only
				selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
					item[0] == 3 and: {(item[5] ? 0) == argDictCurrVals[holdAddressSymbol][1]};
				});
				selectedBankNames = argModule.parentTXVSystem.arrAssetBankNames[3];
				// bank
				dictViews.cubeBankView = TXPopupPlusMinus(scrollBox, 990 @ 20, this.convertAssetToBankName(holdName),
					selectedBankNames.collect({arg item, i; (i + 1).asString ++ ":" + item;}),
					{arg view;
						var assetID = 0;  // reset asset to zero if bank changes
						var assetBank = view.value;
						argDictCurrVals[holdAddressSymbol] = [assetID, assetBank];
						argSendParameterFunc.value(holdAddress, [assetID, assetBank]);
						argModule.class.system.showView;
					},
					// init val
					argDictCurrVals[holdAddressSymbol][1], labelWidth: 200
				);
				holdArrAssetIDs[holdNameSymbol] = [0]; // default is 0
				holdItems = ["empty"];
				selectedAssets.do({arg item, i;
					holdArrAssetIDs[holdNameSymbol] = holdArrAssetIDs[holdNameSymbol].add(item[2]);
					holdItems = holdItems.add(item[3]);
				});
				dictViews.cubeBankView .labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put((holdAddress ++ "/Bank").asSymbol, dictViews.cubeBankView );
				// asset
				dictViews.cubeAssetView = TXPopupPlusMinus(scrollBox, 990 @ 20, holdName, holdItems,
					{arg view;
						var assetID = holdArrAssetIDs[holdNameSymbol][view.value];
						var assetBank = view.value;
						argDictCurrVals[holdAddressSymbol] = [assetID, assetBank];
						argSendParameterFunc.value(holdAddress, [assetID, assetBank]);
					},
					// init val
					holdArrAssetIDs[holdNameSymbol].indexOf(argDictCurrVals[holdAddressSymbol][0]), labelWidth: 200
				);
				dictViews.cubeAssetView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put((holdAddress ++ "/Asset").asSymbol, dictViews.cubeAssetView);
			}
			{item[0] == "assetSlot/3DModel"} {
				var selectedAssets, selectedBankNames, holdItems, holdInitVal;
				// select 3DModel assets only
				selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
					item[0] == 4 and: {(item[5] ? 0) == argDictCurrVals[holdAddressSymbol][1]};
				});
				selectedBankNames = argModule.parentTXVSystem.arrAssetBankNames[4];
				// bank
				dictViews.a3DModelBankView = TXPopupPlusMinus(scrollBox, 990 @ 20, this.convertAssetToBankName(holdName),
					selectedBankNames.collect({arg item, i; (i + 1).asString ++ ":" + item;}),
					{arg view;
						var assetID = 0;  // reset asset to zero if bank changes
						var assetBank = view.value;
						argDictCurrVals[holdAddressSymbol] = [assetID, assetBank];
						argSendParameterFunc.value(holdAddress, [assetID, assetBank]);
						argModule.class.system.showView;
					},
					// init val
					argDictCurrVals[holdAddressSymbol][1], labelWidth: 200
				);
				holdArrAssetIDs[holdNameSymbol] = [0]; // default is 0
				holdItems = ["empty"];
				selectedAssets.do({arg item, i;
					holdArrAssetIDs[holdNameSymbol] = holdArrAssetIDs[holdNameSymbol].add(item[2]);
					holdItems = holdItems.add(item[3]);
				});
				dictViews.a3DModelBankView .labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put((holdAddress ++ "/Bank").asSymbol, dictViews.a3DModelBankView );
				// asset
				dictViews.a3DModelAssetView = TXPopupPlusMinus(scrollBox, 990 @ 20, holdName, holdItems,
					{arg view;
						var assetID = holdArrAssetIDs[holdNameSymbol][view.value];
						var assetBank = dictViews.a3DModelBankView.value;
						argDictCurrVals[holdAddressSymbol] = [assetID, assetBank];
						argSendParameterFunc.value(holdAddress, [assetID, assetBank]);
					},
					// init val
					holdArrAssetIDs[holdNameSymbol].indexOf(argDictCurrVals[holdAddressSymbol][0]), labelWidth: 200
				);
				dictViews.a3DModelAssetView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put((holdAddress ++ "/Asset").asSymbol, dictViews.a3DModelAssetView);
			}
			{item[0] == "assetSlot/font"} {
				var selectedAssets, selectedBankNames, holdItems, holdInitVal;
				// select font assets only
				selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
					item[0] == 5 and: {(item[5] ? 0) == argDictCurrVals[holdAddressSymbol][1]};
				});
				selectedBankNames = argModule.parentTXVSystem.arrAssetBankNames[5];
				// bank
				dictViews.fontBankView = TXPopupPlusMinus(scrollBox, 990 @ 20, this.convertAssetToBankName(holdName),
					selectedBankNames.collect({arg item, i; (i + 1).asString ++ ":" + item;}),
					{arg view;
						var assetID = 0;  // reset asset to zero if bank changes
						var assetBank = view.value;
						argDictCurrVals[holdAddressSymbol] = [assetID, assetBank];
						argSendParameterFunc.value(holdAddress, [assetID, assetBank]);
						argModule.class.system.showView;
					},
					// init val
					argDictCurrVals[holdAddressSymbol][1], labelWidth: 200
				);
				holdArrAssetIDs[holdNameSymbol] = [0]; // default is 0
				holdItems = ["empty"];
				selectedAssets.do({arg item, i;
					holdArrAssetIDs[holdNameSymbol] = holdArrAssetIDs[holdNameSymbol].add(item[2]);
					holdItems = holdItems.add(item[3]);
				});
				dictViews.fontBankView .labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put((holdAddress ++ "/Bank").asSymbol, dictViews.fontBankView );
				// asset
				dictViews.fontAssetView = TXPopupPlusMinus(scrollBox, 990 @ 20, holdName, holdItems,
					{arg view;
						var assetID = holdArrAssetIDs[holdNameSymbol][view.value];
						var assetBank = dictViews.fontBankView.value;
						argDictCurrVals[holdAddressSymbol] = [assetID, assetBank];
						argSendParameterFunc.value(holdAddress, [assetID, assetBank]);
					},
					// init val
					holdArrAssetIDs[holdNameSymbol].indexOf(argDictCurrVals[holdAddressSymbol][0]), labelWidth: 200
				);
				dictViews.fontAssetView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put((holdAddress ++ "/Asset").asSymbol, dictViews.fontAssetView);
			}
			{item[0] == "extImageModuleSlot"} {
				var selectedModules, holdArrModuleIDs, holdItems;
				// select modules with images excluding current module
				selectedModules = argModule.parentTXVSystem.arrTXVModules.select({arg item, i;
					(item.moduleID != argModule.moduleID) && (item.hasTexture == 1);
				});
				holdArrModuleIDs = [0]; // default is 0
				holdItems = ["empty"];
				selectedModules.do({arg item, i;
					holdArrModuleIDs = holdArrModuleIDs.add(item.txvModuleID);
					holdItems = holdItems.add(item.instName);
				});
				holdView = TXPopupPlusMinus(scrollBox, 990 @ 20, holdName, holdItems,
					{arg view;
						var moduleID = holdArrModuleIDs[view.value];
						argDictCurrVals[holdAddressSymbol] = moduleID;
						argSendParameterFunc.value(holdAddress, moduleID);
					},
					holdArrModuleIDs.indexOf(argDictCurrVals[holdAddressSymbol]), labelWidth: 200
				);
				holdView.labelView.stringColor_(TXColour.grey(0.1)).background_(TXColor.white);
				holdArrGuis.put(holdAddressSymbol, holdView);
				// default button
				Button(scrollBox, 40 @ 20)
				.states_([ ["default", TXColor.white, TXColor.sysGuiCol2] ])
				.action_({|view| argModule.restoreDefaultParameter(holdAddress); });
			}
			; // end of case ...

			////////

			// draw line
			scrollBox.decorator.nextLine;
			scrollBox.decorator.shift(0,2);
			View(scrollBox, (scrollBox.bounds.width - 4) @ 1).background_(TXColor.white);
			scrollBox.decorator.nextLine;
			scrollBox.decorator.shift(0,4);
		});  // end of keptParameters.do ...

		// dummy text as spacer
		StaticText(scrollBox, Rect(0, 0, 20, 20));
	}

	update { arg argModule, argArrParameters, argDictCurrVals, argSendParameterFunc, scrollViewAction, dictParameterGroups;

		var holdView, holdItems, keptParameters, hiddenGroups;
		//var scrollBox, totalHeight, arrGridPresetNames, arrGridPresetActions;

		hiddenGroups = [];
		dictParameterGroups.select({arg item, i; item == false;}).keysDo({arg item, i;
			hiddenGroups = hiddenGroups.add(item.asString);
		});

		keptParameters = argArrParameters.select({arg item, i;
			var keep = true;
			// ignore parameter if group is off
			if ((item[0] != "groupStart") and: (item[1].split.size > 2), {
				// if any of the hidden group names match the start of the address
				hiddenGroups.do({arg groupName, i;
					if (item[1].keep(groupName.size) == groupName, {
						keep = false;
					});
				});
			});
			keep;
		});

		keptParameters.do({ arg item, i;
			var holdVal, holdSoftMin, holdSoftMax, holdHardMin, holdHardMax;
			var holdFixedValueUnmapped, holdFixedValue, holdFixedModMix, holdUseExtMod, holdAction, holdString;
			var holdAddress = item[1];
			var holdAddressSplit = item[1].split;
			var holdAddressSymbol = holdAddress.asSymbol;
			var holdIndent = (holdAddressSplit.size - 3) * 40;
			var holdName = holdAddressSplit.last;
			var holdNameSymbol = holdName.asSymbol;
			var fixedValueIndex = 0;
			var softMinIndex = 4;
			var labelStrings, labelView, popupView;
			var defaultValue;

			case

			{item[0] == "groupStart"} {
			}

			{item[0] == "modParameterGroupInt"} {
				holdFixedValue = argDictCurrVals[(holdAddress ++ "/fixedValue").asSymbol];
				holdFixedModMix = argDictCurrVals[(holdAddress ++ "/fixedModMix").asSymbol];
				holdUseExtMod = argDictCurrVals[(holdAddress ++ "/useExtMod").asSymbol];
				holdSoftMin = argDictCurrVals[(holdAddress ++ "/softMin").asSymbol];
				holdSoftMax = argDictCurrVals[(holdAddress ++ "/softMax").asSymbol];
				holdFixedValueUnmapped = ControlSpec(holdSoftMin, holdSoftMax, 'lin', step: 1).unmap(holdFixedValue);
				labelStrings = item[2][fixedValueIndex][5];

				holdView = holdArrGuis.at((holdAddress ++ "/fixedValue").asSymbol);
				holdView.valueSplit_([holdFixedValueUnmapped, holdSoftMin, holdSoftMax]);

				if (labelStrings.notNil, {
					holdView = holdArrGuis.at((holdAddress ++ "/popup").asSymbol);
					if (holdView.notNil, {
						holdView.value = holdFixedValue.asInteger - holdSoftMin;
					});
				});

				holdView = holdArrGuis.at((holdAddress ++ "/fixedModMix").asSymbol);
				if (holdView.notNil, {
					holdView.value = holdFixedModMix;
				});

				holdView = holdArrGuis.at((holdAddress ++ "/useExtMod").asSymbol);
				if (holdView.notNil, {
					holdView.value = holdUseExtMod.asBoolean;
				});
			}

			{item[0] == "modParameterGroupFloat"} {
				holdFixedValue = argDictCurrVals[(holdAddress ++ "/fixedValue").asSymbol];
				holdFixedModMix = argDictCurrVals[(holdAddress ++ "/fixedModMix").asSymbol];
				holdUseExtMod = argDictCurrVals[(holdAddress ++ "/useExtMod").asSymbol];
				holdSoftMin = argDictCurrVals[(holdAddress ++ "/softMin").asSymbol];
				holdSoftMax = argDictCurrVals[(holdAddress ++ "/softMax").asSymbol];
				holdFixedValueUnmapped = ControlSpec(holdSoftMin, holdSoftMax, 'lin').unmap(holdFixedValue);

				holdView = holdArrGuis.at((holdAddress ++ "/fixedValue").asSymbol);
				holdView.valueSplit_([holdFixedValueUnmapped, holdSoftMin, holdSoftMax]);

				holdView = holdArrGuis.at((holdAddress ++ "/fixedModMix").asSymbol);
				if (holdView.notNil, {
					holdView.value = holdFixedModMix;
				});

				holdView = holdArrGuis.at((holdAddress ++ "/useExtMod").asSymbol);
				if (holdView.notNil, {
					holdView.value = holdUseExtMod.asBoolean;
				});
			}

			{item[0] == "modParameterGroupBool"} {
				holdFixedValue = argDictCurrVals[(holdAddress ++ "/fixedValue").asSymbol];
				holdFixedModMix = argDictCurrVals[(holdAddress ++ "/fixedModMix").asSymbol];
				holdUseExtMod = argDictCurrVals[(holdAddress ++ "/useExtMod").asSymbol];

				holdView = holdArrGuis.at((holdAddress ++ "/fixedValue").asSymbol);
				if (holdView.notNil, {
					holdView.value = holdFixedValue.asBoolean;
				});

				holdView = holdArrGuis.at((holdAddress ++ "/fixedModMix").asSymbol);
				if (holdView.notNil, {
					holdView.value = holdFixedModMix.asBoolean;
				});

				holdView = holdArrGuis.at((holdAddress ++ "/useExtMod").asSymbol);
				if (holdView.notNil, {
					holdView.value = holdUseExtMod.asBoolean;
				});
			}

			{item[0] == "int"} {
				holdView = holdArrGuis.at(holdAddressSymbol);
				if (holdView.notNil, {
					holdView.value = argDictCurrVals[holdAddressSymbol];
				});
				labelStrings = item[5];
				if (labelStrings.notNil, {
					holdView = holdArrGuis.at((holdAddress ++ "/popup").asSymbol);
					if (holdView.notNil, {
						holdView.value = argDictCurrVals[holdAddressSymbol].asInteger - item[3].asInteger;
					});
				});
			}
			{item[0] == "float"} {
				holdView = holdArrGuis.at(holdAddressSymbol);
				if (holdView.notNil, {
					holdView.value = argDictCurrVals[holdAddressSymbol];
				});
			}

			{item[0] == "float256"} {
				holdView = holdArrGuis.at(holdAddressSymbol);
				if (holdView.notNil, {
					holdView.value = argDictCurrVals[holdAddressSymbol];
				});
			}

			{item[0] == "bool_int"} {
				holdView = holdArrGuis.at(holdAddressSymbol);
				if (holdView.notNil, {
					holdView.value = argDictCurrVals[holdAddressSymbol].asBoolean;
				});
			}
			{item[0] == "string"} {
				holdView = holdArrGuis.at(holdAddressSymbol);
				holdView.string = argDictCurrVals[holdAddressSymbol];
			}
			{item[0] == "assetSlot/image"} {
				var selectedAssets, holdArrAssetIDs;
				// select image assets only
				selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
					item[0] == 0 and: {(item[5] ? 0) == argDictCurrVals[holdAddressSymbol][1]};
				});
				// bank
				holdView = holdArrGuis.at((holdAddress ++ "/Bank").asSymbol);
				if (holdView.notNil, {
					holdView.value = argDictCurrVals[holdAddressSymbol][1];
				});
				// asset
				holdView = holdArrGuis.at((holdAddress ++ "/Asset").asSymbol);
				if (holdView.notNil, {
					holdView.value = holdArrAssetIDs[holdNameSymbol].indexOf(argDictCurrVals[holdAddressSymbol][0]);
				});
			}
			{item[0] == "assetSlot/movie"} {
				var selectedAssets, holdArrAssetIDs;
				// select movie assets only
				selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
					item[0] == 1 and: {(item[5] ? 0) == argDictCurrVals[holdAddressSymbol][1]};
				});
				// bank
				holdView = holdArrGuis.at((holdAddress ++ "/Bank").asSymbol);
				if (holdView.notNil, {
					holdView.value = argDictCurrVals[holdAddressSymbol][1];
				});
				// asset
				holdView = holdArrGuis.at((holdAddress ++ "/Asset").asSymbol);
				if (holdView.notNil, {
					holdView.value = holdArrAssetIDs[holdNameSymbol].indexOf(argDictCurrVals[holdAddressSymbol][0]);
				});
			}
			{item[0] == "assetSlot/svg"} {
				var selectedAssets, holdArrAssetIDs;
				// select svg assets only
				selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
					item[0] == 2 and: {(item[5] ? 0) == argDictCurrVals[holdAddressSymbol][1]};
				});
				// bank
				holdView = holdArrGuis.at((holdAddress ++ "/Bank").asSymbol);
				if (holdView.notNil, {
					holdView.value = argDictCurrVals[holdAddressSymbol][1];
				});
				// asset
				holdView = holdArrGuis.at((holdAddress ++ "/Asset").asSymbol);
				if (holdView.notNil, {
					holdView.value = holdArrAssetIDs[holdNameSymbol].indexOf(argDictCurrVals[holdAddressSymbol][0]);
				});
			}
			{item[0] == "assetSlot/cube"} {
				var selectedAssets, holdArrAssetIDs;
				// select cube assets only
				selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
					item[0] == 3 and: {(item[5] ? 0) == argDictCurrVals[holdAddressSymbol][1]};
				});
				// bank
				holdView = holdArrGuis.at((holdAddress ++ "/Bank").asSymbol);
				if (holdView.notNil, {
					holdView.value = argDictCurrVals[holdAddressSymbol][1];
				});
				// asset
				holdView = holdArrGuis.at((holdAddress ++ "/Asset").asSymbol);
				if (holdView.notNil, {
					holdView.value = holdArrAssetIDs[holdNameSymbol].indexOf(argDictCurrVals[holdAddressSymbol][0]);
				});
			}
			{item[0] == "assetSlot/3DModel"} {
				var selectedAssets, holdArrAssetIDs;
				// select 3DModel assets only
				selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
					item[0] == 4 and: {(item[5] ? 0) == argDictCurrVals[holdAddressSymbol][1]};
				});
				// bank
				holdView = holdArrGuis.at((holdAddress ++ "/Bank").asSymbol);
				if (holdView.notNil, {
					holdView.value = argDictCurrVals[holdAddressSymbol][1];
				});
				// asset
				holdView = holdArrGuis.at((holdAddress ++ "/Asset").asSymbol);
				if (holdView.notNil, {
					holdView.value = holdArrAssetIDs[holdNameSymbol].indexOf(argDictCurrVals[holdAddressSymbol][0]);
				});
			}
			{item[0] == "assetSlot/font"} {
				var selectedAssets, holdArrAssetIDs;
				// select font assets only
				selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
					item[0] == 5 and: {(item[5] ? 0) == argDictCurrVals[holdAddressSymbol][1]};
				});
				// bank
				holdView = holdArrGuis.at((holdAddress ++ "/Bank").asSymbol);
				if (holdView.notNil, {
					holdView.value = argDictCurrVals[holdAddressSymbol][1];
				});
				// asset
				holdView = holdArrGuis.at((holdAddress ++ "/Asset").asSymbol);
				if (holdView.notNil, {
					holdView.value = holdArrAssetIDs[holdNameSymbol].indexOf(argDictCurrVals[holdAddressSymbol][0]);
				});
			}

			////////

			{item[0] == "extImageModuleSlot"} {
				var selectedModules, holdArrModuleIDs;
				holdView = holdArrGuis.at(holdAddressSymbol);
				// select modules with
				selectedModules = argModule.parentTXVSystem.arrTXVModules.select({arg item, i;
					(item.moduleID != argModule.moduleID) && (item.hasTexture == 1);
				});
				holdArrModuleIDs = [0]; // default is 0
				selectedModules.do({arg item, i;
					holdArrModuleIDs = holdArrModuleIDs.add(item.txvModuleID);
				});
				if (holdView.notNil, {
					holdView.value = holdArrModuleIDs.indexOf(argDictCurrVals[holdAddressSymbol])
				});
			}
			; // end of case ...
		});  // end of keptParameters.do ...
	}

	quantiseToGrid {arg inputArray, rows, cols;
		var newArray, holdSignal, arrCurveValues;
		var maxVal = inputArray.size;
		newArray = Array.newClear(maxVal);
		cols.do({arg item, i;
			var jump, startRange, endRange, meanVal;
			jump = cols.reciprocal;
			startRange = (item * jump * maxVal).round(1);
			endRange = ((item + 1) * jump * maxVal).round(1) - 1;
			meanVal = inputArray.copyRange(startRange.asInteger, endRange.asInteger).mean;
			newArray[startRange.asInteger..endRange.asInteger] = meanVal.round(rows.reciprocal);
		});
		holdSignal = Signal.newFrom(newArray);
		arrCurveValues = Array.newFrom(holdSignal);
		^arrCurveValues;
	}

	convertAssetToBankName {arg argName;
		// replace text Asset with Bank
		var outName = argName.keep(argName.size - 5) ++ "Bank";
		^outName;
	}
}
