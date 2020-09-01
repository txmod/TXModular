
TXV_BuildActionSpecs {

	*from { arg argModule;
		var dictCurrVals, sendParameterFunc;
		var holdArrTXVSpecs, arrActionSpecs;

		holdArrTXVSpecs = [];
		dictCurrVals = argModule.dictCurrentParameterVals;
		sendParameterFunc = {arg argAddress, argValue; argModule.sendModuleParmToTXV( argAddress, argValue);};

		argModule.getTXVParameters.do({ arg item, i;
			var holdVal;
			var holdFixedValueUnmapped, holdFixedValue, holdAction, holdString;
			var labelStrings, holdPopupItemsFunc, holdControlSpecFunc, holdSpec;
			var getValueFunction, setValueFunction;

			var holdAddress = item[1];
			var holdAddressSplit = item[1].split;
			var holdAddressSymbol = holdAddress.asSymbol;
			var holdIndent = (holdAddressSplit.size - 3) * 40;
			var holdName = holdAddressSplit.last;
			//var holdNameSymbol = holdName.asSymbol;
			var fixedValueIndex = 0;

			//--------------------- CASE
			case

			{item[0] == "modParameterGroupInt"} {
				holdFixedValue = dictCurrVals[(holdAddress ++ "/fixedValue").asSymbol];
				labelStrings = item[2][fixedValueIndex][5];

				// add spec
				if (labelStrings.notNil, {
					// TXV_Popup
					// arguments - index1 is text, index2 is getValueFunction, index3 is setValueFunction
					// index 4 is controlSpecFunc, index 5 is arrItemsFunc
					// e.g. ["TXV_Popup", holdName, getValueFunction, setValueFunction, holdControlSpecFunc, arrItemsFunc]
					getValueFunction = {dictCurrVals[(holdAddress ++ "/fixedValue").asSymbol]};
					setValueFunction = {arg valArg;
						dictCurrVals[(holdAddress ++ "/fixedValue").asSymbol] = valArg.asInteger;
						sendParameterFunc.value(holdAddress ++ "/fixedValue", valArg.asInteger);
					};
					holdControlSpecFunc = {
						var holdSoftMin = dictCurrVals[(holdAddress ++ "/softMin").asSymbol];
						var holdSoftMax = dictCurrVals[(holdAddress ++ "/softMax").asSymbol];
						ControlSpec(holdSoftMin, holdSoftMax, 'lin', step: 1);
					};
					holdPopupItemsFunc = {
						var holdSoftMin = dictCurrVals[(holdAddress ++ "/softMin").asSymbol];
						var holdSoftMax = dictCurrVals[(holdAddress ++ "/softMax").asSymbol];
						labelStrings[holdSoftMin.asInteger .. holdSoftMax.asInteger];
					};
					holdSpec = ["TXV_Popup", holdName, getValueFunction, setValueFunction, holdControlSpecFunc, holdPopupItemsFunc];
				}, {
					// TXV_Slider
					// arguments - index1 is checkbox text, index2 is getValueFunction, index3 is setValueFunction
					// index 4 is controlSpecFunc
					// e.g. ["TXV_Slider", holdName, getValueFunction, setValueFunction, holdControlSpecFunc]
					getValueFunction = {dictCurrVals[(holdAddress ++ "/fixedValue").asSymbol]};
					setValueFunction = {arg valArg;
						dictCurrVals[(holdAddress ++ "/fixedValue").asSymbol] = valArg.asInteger;
						sendParameterFunc.value(holdAddress ++ "/fixedValue", valArg.asInteger);
					};
					holdControlSpecFunc = {
						var holdSoftMin = dictCurrVals[(holdAddress ++ "/softMin").asSymbol];
						var holdSoftMax = dictCurrVals[(holdAddress ++ "/softMax").asSymbol];
						ControlSpec(holdSoftMin, holdSoftMax, 'lin', step: 1);
					};
					holdSpec = ["TXV_Slider", holdName, getValueFunction, setValueFunction, holdControlSpecFunc];
				});
				holdArrTXVSpecs = holdArrTXVSpecs.add(holdSpec);
			}

			//---------------------

			{item[0] == "modParameterGroupFloat"} {
				// TXV_Slider
				// arguments - index1 is checkbox text, index2 is getValueFunction, index3 is setValueFunction
				// index 4 is controlSpecFunc
				// e.g. ["TXV_Slider", holdName, getValueFunction, setValueFunction, holdControlSpecFunc]
				getValueFunction = {dictCurrVals[(holdAddress ++ "/fixedValue").asSymbol]};
				setValueFunction = {arg valArg;
						dictCurrVals[(holdAddress ++ "/fixedValue").asSymbol] = valArg;
						sendParameterFunc.value(holdAddress ++ "/fixedValue", valArg);
					};
				holdControlSpecFunc = {
					var holdSoftMin = dictCurrVals[(holdAddress ++ "/softMin").asSymbol];
					var holdSoftMax = dictCurrVals[(holdAddress ++ "/softMax").asSymbol];
					ControlSpec(holdSoftMin, holdSoftMax, 'lin', step: 0);
				};
				holdSpec = ["TXV_Slider", holdName, getValueFunction, setValueFunction, holdControlSpecFunc];
				holdArrTXVSpecs = holdArrTXVSpecs.add(holdSpec);
			}
			//---------------------

			{item[0] == "modParameterGroupBool"} {
				// TXV_CheckBox
				// arguments- index1 is checkbox text, index2 is getValueFunction, index3 is setValueFunction
				// e.g. ["TXV_CheckBox", holdName, getValueFunction, setValueFunction]
				getValueFunction = {dictCurrVals[(holdAddress ++ "/fixedValue").asSymbol]};
				setValueFunction = {arg valArg;
						dictCurrVals[(holdAddress ++ "/fixedValue").asSymbol] = valArg.asInteger;
						sendParameterFunc.value(holdAddress ++ "/fixedValue", valArg.asInteger);
					};
				holdControlSpecFunc = ControlSpec(0, 1, 'lin', step: 1);
				holdSpec = ["TXV_CheckBox", holdName, getValueFunction, setValueFunction];
				holdArrTXVSpecs = holdArrTXVSpecs.add(holdSpec);
			}
			//---------------------

			{item[0] == "int"} {
				labelStrings = item[5];
				// add spec
				if (labelStrings.notNil, {
					// TXV_Popup
					// arguments - index1 is text, index2 is getValueFunction, index3 is setValueFunction
					// index 4 is controlSpecFunc, index 5 is arrItemsFunc
					// e.g. ["TXV_Popup", holdName, getValueFunction, setValueFunction, holdControlSpecFunc, arrItemsFunc]
					getValueFunction = {dictCurrVals[holdAddressSymbol]};
					setValueFunction = {arg valArg;
						dictCurrVals[holdAddressSymbol] = valArg.asInteger;
						sendParameterFunc.value(holdAddress, valArg.asInteger);
					};
					holdControlSpecFunc = ControlSpec(item[3], item[4], 'lin', step: 1);
					holdSpec = ["TXV_Popup", holdName, getValueFunction, setValueFunction, holdControlSpecFunc, labelStrings];
				}, {
					// TXV_Slider
					// arguments - index1 is checkbox text, index2 is getValueFunction, index3 is setValueFunction
					// index 4 is controlSpecFunc
					// e.g. ["TXV_Slider", holdName, getValueFunction, setValueFunction, holdControlSpecFunc]
					getValueFunction = {dictCurrVals[holdAddressSymbol]};
					setValueFunction = {arg valArg;
						dictCurrVals[holdAddressSymbol] = valArg.asInteger;
						sendParameterFunc.value(holdAddress, valArg.asInteger);
					};
					holdControlSpecFunc = ControlSpec(item[3], item[4], 'lin', step: 1);
					holdSpec = ["TXV_Slider", holdName, getValueFunction, setValueFunction, holdControlSpecFunc];
				});
				holdArrTXVSpecs = holdArrTXVSpecs.add(holdSpec);
			}
			//---------------------

			{item[0] == "float"} {
				// TXV_Slider
				// arguments - index1 is checkbox text, index2 is getValueFunction, index3 is setValueFunction
				// index 4 is controlSpecFunc
				// e.g. ["TXV_Slider", holdName, getValueFunction, setValueFunction, holdControlSpecFunc]
				getValueFunction = {dictCurrVals[holdAddressSymbol]};
				setValueFunction = {arg valArg;
						dictCurrVals[holdAddressSymbol] = valArg;
						sendParameterFunc.value(holdAddress, valArg);
					};
				holdControlSpecFunc = ControlSpec(item[3], item[4], 'lin', step: 0);
				holdSpec = ["TXV_Slider", holdName, getValueFunction, setValueFunction, holdControlSpecFunc];
				holdArrTXVSpecs = holdArrTXVSpecs.add(holdSpec);
			}
			//---------------------

			{item[0] == "bool_int"} {
				// TXV_CheckBox
				// arguments- index1 is checkbox text, index2 is getValueFunction, index3 is setValueFunction
				// e.g. ["TXV_CheckBox", holdName, getValueFunction, setValueFunction]
				getValueFunction = {dictCurrVals[holdAddressSymbol]};
				setValueFunction = {arg valArg;
						dictCurrVals[holdAddressSymbol] = valArg.asInteger;
						sendParameterFunc.value(holdAddress, valArg.asInteger);
					};
				holdControlSpecFunc = ControlSpec(0, 1, 'lin', step: 1);
				holdSpec = ["TXV_CheckBox", holdName, getValueFunction, setValueFunction];
				holdArrTXVSpecs = holdArrTXVSpecs.add(holdSpec);
			}
			//---------------------

			{item[0] == "string"} {
				// TXV_TextBox
				// arguments- index1 is row label, index2 is getValueFunction, index3 is setValueFunction
				// e.g. ["TXV_TextBox", holdName, getValueFunction, setValueFunction]
				getValueFunction = {dictCurrVals[holdAddressSymbol]};
				setValueFunction = {arg valArg;
						dictCurrVals[holdAddressSymbol] = valArg;
						sendParameterFunc.value(holdAddress, valArg);
					};
				holdSpec = ["TXV_TextBox", holdName, getValueFunction, setValueFunction];
				holdArrTXVSpecs = holdArrTXVSpecs.add(holdSpec);
							}
			//---------------------

			{item[0].keep(10) == "assetSlot/"} {
				var typeIndex;
				case
				{item[0] == "assetSlot/image"} {
					typeIndex= 0;
				}
				{item[0] == "assetSlot/movie"} {
					typeIndex= 1;
				}
				{item[0] == "assetSlot/svg"} {
					typeIndex= 2;
				}
				{item[0] == "assetSlot/cube"} {
					typeIndex= 3;
				}
				{item[0] == "assetSlot/3DModel"} {
					typeIndex= 4;
				}
				{item[0] == "assetSlot/font"} {
					typeIndex= 5;
				}
				;

				// add Bank spec
				// TXV_Popup
				// arguments - index1 is text, index2 is getValueFunction, index3 is setValueFunction
				// index 4 is controlSpecFunc, index 5 is arrItemsFunc
				// e.g. ["TXV_Popup", holdName, getValueFunction, setValueFunction, holdControlSpecFunc, arrItemsFunc]
				getValueFunction = {
					var selectedAssets, holdArrAssetIDs;
					dictCurrVals[holdAddressSymbol][1];
				};
				setValueFunction = {arg valArg;
					var assetID, bankNo;
					var selectedAssets, holdArrAssetIDs;
					bankNo = valArg.asInteger;
					assetID = 0;
					dictCurrVals[holdAddressSymbol] = [assetID, bankNo];
					sendParameterFunc.value(holdAddress, [assetID, bankNo]);
					// update view if needed
					argModule.class.system.showViewIfRunInterface;
					argModule.class.system.showViewIfModDisplay(argModule);
				};
				holdControlSpecFunc = {
					ControlSpec(0, 30, 'lin', step: 1);
				};
				holdPopupItemsFunc = {
					var selectedBankNames = argModule.parentTXVSystem.arrAssetBankNames[typeIndex];
					selectedBankNames.collect({arg bankName, i; (i + 1).asString ++ ":" + bankName;})
				};
				holdSpec = ["TXV_Popup", this.convertAssetToBankName(holdName), getValueFunction, setValueFunction,
					holdControlSpecFunc, holdPopupItemsFunc];
				holdArrTXVSpecs = holdArrTXVSpecs.add(holdSpec);

				// add Asset spec
				// TXV_Popup
				// arguments - index1 is text, index2 is getValueFunction, index3 is setValueFunction
				// index 4 is controlSpecFunc, index 5 is arrItemsFunc
				// e.g. ["TXV_Popup", holdName, getValueFunction, setValueFunction, holdControlSpecFunc, arrItemsFunc]
				getValueFunction = {
					var selectedAssets, holdArrAssetIDs;
					// asset: [assetType, assetTypeString, assetID, assetFilename, fileExists, assetBankNo]
					// select specific assets only
					selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
						item[0] == typeIndex and: {(item[5] ? 0) == (dictCurrVals[holdAddressSymbol][1] ? 0)};});
					holdArrAssetIDs = [0]; // default is 0
					selectedAssets.do({arg item, i;
						holdArrAssetIDs = holdArrAssetIDs.add(item[2]);
					});
					holdArrAssetIDs.indexOf(dictCurrVals[holdAddressSymbol][0]);
				};
				setValueFunction = {arg valArg;
					var assetID, bankNo;
					var selectedAssets, holdArrAssetIDs;

					// asset: [assetType, assetTypeString, assetID, assetFilename, fileExists, assetBankNo]
					// select specific assets only
					selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
						item[0] == typeIndex and: {(item[5] ? 0) == (dictCurrVals[holdAddressSymbol][1] ? 0)};});
					holdArrAssetIDs = [0]; // default is 0
					selectedAssets.do({arg item, i;
						holdArrAssetIDs = holdArrAssetIDs.add(item[2]);
					});
					assetID = holdArrAssetIDs[valArg.asInteger];
					bankNo = dictCurrVals[holdAddressSymbol][1];
					dictCurrVals[holdAddressSymbol] = [assetID, bankNo];
					sendParameterFunc.value(holdAddress, [assetID, bankNo]);
				};
				holdControlSpecFunc = {
					var selectedAssets, holdArrAssetIDsCount;
					// asset: [assetType, assetTypeString, assetID, assetFilename, fileExists, assetBankNo]
					// select specific assets only
					selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
						item[0] == typeIndex and: {(item[5] ? 0) == (dictCurrVals[holdAddressSymbol][1] ? 0)};});
					holdArrAssetIDsCount = 0; // default is 0
					selectedAssets.do({arg item, i;
						holdArrAssetIDsCount = holdArrAssetIDsCount + 1;
					});
					ControlSpec(0, holdArrAssetIDsCount - 1, 'lin', step: 1);
				};
				holdPopupItemsFunc = {
					var selectedAssets, holdItems;
					// asset: [assetType, assetTypeString, assetID, assetFilename, fileExists, assetBankNo]
					// select specific assets only
					selectedAssets = argModule.parentTXVSystem.arrTXVAssets.select({arg item, i;
						item[0] == typeIndex and: {(item[5] ? 0) == (dictCurrVals[holdAddressSymbol][1] ? 0)};});
					holdItems = ["empty"];
					selectedAssets.do({arg item, i;
						holdItems = holdItems.add(item[3]);
					});
					holdItems;
				};
				holdSpec = ["TXV_Popup", holdName, getValueFunction, setValueFunction, holdControlSpecFunc, holdPopupItemsFunc];
				holdArrTXVSpecs = holdArrTXVSpecs.add(holdSpec);
			}
			//---------------------

			{item[0] == "extImageModuleSlot"} {

				// add spec
				// TXV_Popup
				// arguments - index1 is text, index2 is getValueFunction, index3 is setValueFunction
				// index 4 is controlSpecFunc, index 5 is arrItemsFunc
				// e.g. ["TXV_Popup", holdName, getValueFunction, setValueFunction, holdControlSpecFunc, arrItemsFunc]
				getValueFunction = {
					var selectedModules, holdArrModuleIDs, holdItems;
					selectedModules = argModule.parentTXVSystem.arrTXVModules.select({arg item, i;
						(item.moduleID != argModule.moduleID) && (item.hasTexture == 1);
					});
					holdArrModuleIDs = [0]; // default is 0
					holdItems = ["empty"];
					selectedModules.do({arg item, i;
						holdArrModuleIDs = holdArrModuleIDs.add(item.txvModuleID);
						holdItems = holdItems.add(item.instName);
					});
					holdArrModuleIDs.indexOf(dictCurrVals[holdAddressSymbol]);
				};
				setValueFunction = {arg valArg;
					var moduleID;
					var selectedModules, holdArrModuleIDs;
					selectedModules = argModule.parentTXVSystem.arrTXVModules.select({arg item, i;
						(item.moduleID != argModule.moduleID) && (item.hasTexture == 1);
					});
					holdArrModuleIDs = [0]; // default is 0
					selectedModules.do({arg item, i;
						holdArrModuleIDs = holdArrModuleIDs.add(item.txvModuleID);
					});
					moduleID = holdArrModuleIDs[valArg.asInteger];
					dictCurrVals[holdAddressSymbol] = moduleID.asInteger;
					sendParameterFunc.value(holdAddress, moduleID.asInteger);
				};
				holdControlSpecFunc = {
					var selectedModules, holdModulesCount;
					selectedModules = argModule.parentTXVSystem.arrTXVModules.select({arg item, i;
						(item.moduleID != argModule.moduleID) && (item.hasTexture == 1);
					});
					holdModulesCount = 0; // default is 0
					selectedModules.do({arg item, i;
						holdModulesCount = holdModulesCount + 1;
					});
					ControlSpec(0, holdModulesCount - 1, 'lin', step: 1);
				};
				holdPopupItemsFunc = {
					var selectedModules, holdItems;
					selectedModules = argModule.parentTXVSystem.arrTXVModules.select({arg item, i;
						(item.moduleID != argModule.moduleID) && (item.hasTexture == 1);
					});
					holdItems = ["empty"];
					selectedModules.do({arg item, i;
						holdItems = holdItems.add(item.instName);
					});
					holdItems;
				};
				holdSpec = ["TXV_Popup", holdName, getValueFunction, setValueFunction, holdControlSpecFunc, holdPopupItemsFunc];
				holdArrTXVSpecs = holdArrTXVSpecs.add(holdSpec);
			}
			; // end of case ...

		});  // end of .do ...

		// return converted array
		^arrActionSpecs = TXV_BuildActionSpecs.asActionSpecs(argModule, holdArrTXVSpecs);
	}

	//======================================================


	*asActionSpecs {arg argModule, arrTXVSpecs;
		var holdAction, holdActionFunc, holdControlSpecFunc;
		var arrActionSpecs;
		var actionCounter = 0;
		var holdDummyAction = TXAction.new(\commandAction, "...", {});

		arrActionSpecs = [holdDummyAction];

		arrTXVSpecs.do({ arg item, i;

			// TXV_Slider
			// arguments - index1 is checkbox text, index2 is getValueFunction, index3 is setValueFunction
			// index 4 is controlSpecFunc
			// e.g. ["TXV_Slider", holdName, getValueFunction, setValueFunction, holdControlSpecFunc]
			if (item.at(0) == "TXV_Slider", {
				var holdName = item.at(1);
				var holdGetValueFunction = item.at(2);
				var holdSetValueFunction = item.at(3);
				var holdControlSpecFunc = item.at(4);

				// add value action
				holdAction = TXAction.new(\valueAction, "Set " ++ holdName);
				holdAction.getValueFunction = holdGetValueFunction;
				holdAction.setValueFunction = holdSetValueFunction;
				holdAction.arrControlSpecFuncs = [holdControlSpecFunc];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add unmapped value action
				holdAction = TXAction.new(\valueAction, "Set " ++ holdName ++ " unmapped");
				holdAction.getValueFunction = holdGetValueFunction;
				holdAction.setValueFunction = {arg argValue;
					var holdValUnmapped = ControlSpec(0,1).constrain(argValue);
					var holdControlSpec = holdControlSpecFunc.value;
					holdSetValueFunction.value(holdControlSpec.map(holdValUnmapped));
				};
				holdAction.arrControlSpecFuncs = [ControlSpec(0,1);];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to randomize value
				holdActionFunc = {
					//  update with randomized value
					var holdValUnmapped = 1.0.rand;
					var holdControlSpec = holdControlSpecFunc.value;
					holdSetValueFunction.value(holdControlSpec.map(holdValUnmapped));
				};
				holdAction = TXAction.new(\commandAction, "Randomize " ++ holdName, holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to randomize w. depth
				holdActionFunc = { arg val1;
					// get old value
					var holdOldVal = holdGetValueFunction.value;
					//  update with randomized value
					var holdValUnmapped = 1.0.rand;
					var holdControlSpec = holdControlSpecFunc.value;
					var newVal = holdControlSpec.map(holdValUnmapped).blend(holdOldVal, 1 - val1);
					holdSetValueFunction.value(holdControlSpec.constrain(newVal));
				};
				holdAction = TXAction.new(\commandAction, "Randomize w. depth " ++ holdName, holdActionFunc);
				holdAction.arrControlSpecFuncs = [ControlSpec(0,1, default: 0.25);];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to add to value
				holdActionFunc = { arg val1;
					var holdOldVal, holdCurVal;
					// get old value
					holdOldVal = holdGetValueFunction.value;
					//  update with new value
					holdCurVal = holdControlSpecFunc.value.constrain(holdCurVal + val1);
					holdSetValueFunction.value(holdCurVal);
				};
				holdAction = TXAction.new(\commandAction, "Add to " ++ holdName, holdActionFunc);
				holdAction.arrControlSpecFuncs = [holdControlSpecFunc];
				holdAction.arrControlSpecFuncs = [{ControlSpec(0, holdControlSpecFunc.value.range, \lin, 0, 1.min(holdControlSpecFunc.value.maxval))}];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to subtract from value
				holdActionFunc = { arg val1;
					var holdOldVal, holdCurVal;
					// get old value
					holdOldVal = holdGetValueFunction.value;
					//  update with new value
					holdCurVal = holdControlSpecFunc.value.constrain(holdCurVal - val1);
					holdSetValueFunction.value(holdCurVal);
				};
				holdAction = TXAction.new(\commandAction, "Subtract from " ++ holdName, holdActionFunc);
				holdAction.arrControlSpecFuncs = [{ControlSpec(0, holdControlSpecFunc.value.range, \lin, 0, 1.min(holdControlSpecFunc.value.maxval))}];
				holdAction.guiObjectType = \number;
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});


			// TXV_Popup,
			// arguments - index1 is checkbox text, index2 is getValueFunction, index3 is setValueFunction
			// index 4 is controlSpecFunc, index 5 is arrItemsFunc
			// e.g. ["TXV_Popup", holdName, getValueFunction, setValueFunction, holdControlSpecFunc, holdPopupItemsFunc]
			if (item.at(0) == "TXV_Popup", {
				var holdName = item.at(1);
				var holdGetValueFunction = item.at(2);
				var holdSetValueFunction = item.at(3);
				var holdControlSpecFunc = item.at(4);
				var holdPopupItemsFunc = item.at(5);

				holdAction = TXAction.new(\valueAction, "Set " ++ holdName);
				holdAction.getValueFunction = holdGetValueFunction;
				holdAction.setValueFunction = holdSetValueFunction;
				holdAction.guiObjectType = \popup;
				holdAction.getItemsFunction = holdPopupItemsFunc;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to increment value
				holdActionFunc = {
					var holdCurVal, maxVal;
					maxVal = holdControlSpecFunc.value.maxval;
					// get current val and update with new value
					holdCurVal = holdGetValueFunction.value;
					holdSetValueFunction.value((holdCurVal + 1).min(maxVal));
				};
				holdAction = TXAction.new(\commandAction, "Next " ++ holdName, holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to decrement value
				holdActionFunc = {
					var holdCurVal, minVal;
					minVal = holdControlSpecFunc.value.minval;
					// get current val and update with new value
					holdCurVal = holdGetValueFunction.value;
					holdSetValueFunction.value((holdCurVal - 1).max(minVal).asInt);
				};
				holdAction = TXAction.new(\commandAction, "Previous " ++ holdName, holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to randomize value
				holdActionFunc = {
					var holdCurVal, minVal, maxVal;
					//  update with randomized value
					minVal = holdControlSpecFunc.value.minval;
					maxVal = holdControlSpecFunc.value.maxval;
					holdCurVal = minVal + maxVal.asInt.rand;
					holdSetValueFunction.value(holdCurVal.asInt);
				};
				holdAction = TXAction.new(\commandAction, "Randomize " ++ holdName, holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// TXV_CheckBox
			// arguments- index1 is checkbox text, index2 is getValueFunction, index3 is setValueFunction
			// e.g. ["TXV_CheckBox", holdName, getValueFunction, setValueFunction]
			if (item.at(0) == "TXV_CheckBox", {
				var holdName = item.at(1);
				var holdGetValueFunction = item.at(2);
				var holdSetValueFunction = item.at(3);

				// add \valueAction to set value
				holdAction = TXAction.new(\valueAction, "Set " ++ holdName);
				holdAction.getValueFunction = item.at(2);
				holdAction.setValueFunction = item.at(3);
				holdAction.guiObjectType = \checkbox;
				arrActionSpecs = arrActionSpecs.add(holdAction);
				// add commandAction to toggle value
				holdActionFunc = {
					var holdCurVal;
					// get current val and update with opposite value
					holdCurVal = holdGetValueFunction.value;
					holdSetValueFunction.value(1 - holdCurVal);
				};
				holdAction = TXAction.new(\commandAction, "Toggle " ++ holdName, holdActionFunc);
				arrActionSpecs = arrActionSpecs.add(holdAction);
			});

			// TXV_TextBox
			// arguments- index1 is row label, index2 is getValueFunction, index3 is setValueFunction
			// e.g. ["TXV_TextBox", holdName, getValueFunction, setValueFunction]
			if (item.at(0) == "TXV_TextBox", {
				var holdName = item.at(1);
				var holdGetValueFunction = item.at(2);
				var holdSetValueFunction = item.at(3);

				holdAction = TXAction.new(\valueAction, "Set " ++ holdName ? "Text");
				holdAction.getValueFunction = holdGetValueFunction;
				holdAction.setValueFunction = holdSetValueFunction;
				holdAction.guiObjectType = \textedit;
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

		});  // end of .do ...

		^(arrActionSpecs ++ TXBuildActions.from(argModule, [])); // return specs with defaults added
	}

	*convertAssetToBankName {arg argName;
		// replace text Asset with Bank
		var outName = argName.keep(argName.size - 5) ++ "Bank";
		^outName;
	}
}

