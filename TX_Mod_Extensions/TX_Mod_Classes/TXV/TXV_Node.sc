// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Node : TXV_Module {
	classvar <defaultName = "TXV Node";
	classvar <>arrInstances;

	var <>sourceTXVID, <>sourceModuleName, <>sourceOutputName, <>destinationTXVID, <>destinationModuleName, <>destinationParamName;
	var <>markedForDeletion = false;

	updateTXVNode {
		parentTXVSystem.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/setNodeConnections",
			[txvModuleID, sourceTXVID, sourceOutputName, destinationTXVID, destinationParamName]
		);
	}

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXModNode_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["bool_int", "/TXModNode_1/active", 1],
		[ "modParameterGroupFloat", "/TXModNode_1/modAmount", [
			["float", "/TXModNode_1/modAmount/fixedValue", 1, 0, 1],
			["float", "/TXModNode_1/modAmount/fixedModMix", 0, 0, 1],
			["bool_int", "/TXModNode_1/modAmount/useExtMod", 0],
			["float", "/TXModNode_1/modAmount/extModValue", 0, 0, 1],
			["float", "/TXModNode_1/modAmount/softMin", 0, 0, 1],
			["float", "/TXModNode_1/modAmount/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXModNode_1/negativeModulation", [
			["bool_int", "/TXModNode_1/negativeModulation/fixedValue", 0],
			["bool_int", "/TXModNode_1/negativeModulation/fixedModMix", 0],
			["bool_int", "/TXModNode_1/negativeModulation/useExtMod", 0],
			["float", "/TXModNode_1/negativeModulation/extModValue", 0, 0, 1],
		]],
	];}

	setSynthArgSpecsForGui {
		var holdSourceModules, holdSourceModuleIDs, holdDestModules, holdDestModuleIDs, holdSourceOutputNames, holdDestParamNames;
		holdSourceModules = parentTXVSystem.getValidTXVNodeSourceModules;
		holdDestModules = parentTXVSystem.getValidTXVNodeDestModules;
		holdSourceModuleIDs = holdSourceModules.collect({arg item, i; item.txvModuleID});
		holdDestModuleIDs = holdDestModules.collect({arg item, i; item.txvModuleID});
		holdSourceOutputNames = this.getValidTXVNodeSourceOutputNames;
		holdDestParamNames = this.getValidTXVNodeDestParamNames;
		// prepare SynthArgSpecs
		this.setSynthArgSpec("nodeSourceModuleIndex", holdSourceModuleIDs.indexOf(sourceTXVID) ? 0);
		this.setSynthArgSpec("nodeSourceOutputNameIndex", holdSourceOutputNames.indexOfEqual(sourceOutputName) ? 0);
		this.setSynthArgSpec("nodeDestModuleIndex", holdDestModuleIDs.indexOf(destinationTXVID) ? 0);
		this.setSynthArgSpec("nodeDestParamNameIndex", holdDestParamNames.indexOfEqual(destinationParamName) ? 0);
	}

	buildGuiSpecArray {
		// var holdSourceModules, holdSourceModuleNames, holdSourceOutputNames;
		// var holdDestModules, holdDestModuleNames, holdDestParamNames;
		var holdParent = this.getParentTXVSystem;
		if (holdParent.notNil, {
			this.setSynthArgSpecsForGui;
			//holdSourceModuleNames = parentTXVSystem.getValidTXVNodeSourceModuleNames;
			//holdSourceOutputNames = this.getValidTXVNodeSourceOutputNames;
			//holdDestModules = parentTXVSystem.getValidTXVNodeDestModules;
			//holdDestModuleNames = parentTXVSystem.getValidTXVNodeDestModuleNames;
			//holdDestParamNames = this.getValidTXVNodeDestParamNames;
			guiSpecArray = [
				["TXListViewAction", "source module", {parentTXVSystem.getValidTXVNodeSourceModuleNames},
					"nodeSourceModuleIndex",
					{ arg view; var holdModule;
						holdModule = parentTXVSystem.getValidTXVNodeSourceModules[view.value];
						sourceTXVID = holdModule.txvModuleID;
						sourceModuleName = holdModule.instName;
						sourceOutputName = this.getValidTXVNodeSourceOutputNames[0];
						this.updateTXVNode;
						this.buildGuiSpecArray;
						parentTXVSystem.buildGuiSpecArray;
						system.showView;
					}, 320, 120],
				["Spacer", 4],
				["TXListViewAction", "source name", {this.getValidTXVNodeSourceOutputNames},
					"nodeSourceOutputNameIndex",
					{ arg view;
						sourceOutputName = this.getValidTXVNodeSourceOutputNames[view.value];
						this.updateTXVNode;
						this.buildGuiSpecArray;
						parentTXVSystem.buildGuiSpecArray;
						system.showView;
				}, 400, 120],
				["SpacerLine", 4],
				["TXListViewAction", "destination module", {parentTXVSystem.getValidTXVNodeDestModuleNames},
					"nodeDestModuleIndex",
					{ arg view; var holdModule;
						holdModule = parentTXVSystem.getValidTXVNodeDestModules[view.value];
						destinationTXVID = holdModule.txvModuleID;
						destinationModuleName = holdModule.instName;
						destinationParamName = this.getValidTXVNodeDestParamNames[0];
						this.updateTXVNode;
						this.buildGuiSpecArray;
						parentTXVSystem.buildGuiSpecArray;
						system.showView;
				}, 320, 150],
				["Spacer", 4],
				["TXListViewAction", "destination name", {this.getValidTXVNodeDestParamNames},
					"nodeDestParamNameIndex",
					{ arg view;
						destinationParamName = this.getValidTXVNodeDestParamNames[view.value];
						this.updateTXVNode;
						this.buildGuiSpecArray;
						parentTXVSystem.buildGuiSpecArray;
						system.showView;
				}, 400, 150],
				["NextLine"],
				["TXV_GuiScroller", this.getTXVParameters,
					dictCurrentParameterVals,
					{arg argAddress, argValue; this.sendModuleParmToTXV( argAddress, argValue);},
					{arg view; holdScrollView = view;},
					{arg view; data['visibleOrigins'][displayOption.asSymbol] = view.visibleOrigin; },
					data['dictParameterGroups'],
					410,
				]
			];
		});
	}

	// override base class
	getExtraSynthArgSpecs {
		^[
			["nodeSourceModuleIndex", 0],
			["nodeSourceOutputNameIndex", 0],
			["nodeDestModuleIndex", 0],
			["nodeDestParamNameIndex", 0],
	]}


	getValidTXVNodeSourceOutputNames {
		// based on selected source module
		var holdModule;
		holdModule = parentTXVSystem.getModuleWithTXVID(sourceTXVID);
		if (holdModule.notNil, {
			^holdModule.getTXVOutputs;
		});
		^[];
	}

	getValidTXVNodeDestParamNames {
		// based on selected destination module
		var holdModule;
		holdModule = parentTXVSystem.getModuleOrNodeWithTXVID(destinationTXVID);
		if (holdModule.notNil, {
			^holdModule.getTXVSynthModParameters.collect({arg item, i;
				var holdArrStrings = item[1].split;
				holdArrStrings[holdArrStrings.size - 2]
			});
		});
		^[];
	}

	extraSaveData {
		^[dictCurrentParameterVals.asArgsArray, parentTXVSysModuleID, txvModuleID, txvModuleName, txvModuleType,
			canDraw, canDrawLayers, numModInputs, numModOutputs,
			sourceTXVID, sourceModuleName, sourceOutputName, destinationTXVID, destinationModuleName, destinationParamName];
	}

	loadExtraData {arg argData, isPresetData = false;  // override default method
		dictCurrentParameterVals = ();
		dictCurrentParameterVals.putPairs(argData.at(0));
		parentTXVSysModuleID = argData.at(1);
		txvModuleID = argData.at(2);
		txvModuleName = argData.at(3);
		txvModuleType = argData.at(4);
		canDraw = argData.at(5);
		canDrawLayers = argData.at(6);
		numModInputs = argData.at(7);
		numModOutputs = argData.at(8);
		sourceTXVID = argData.at(9);
		sourceModuleName = argData.at(10);
		sourceOutputName = argData.at(11);
		destinationTXVID = argData.at(12);
		destinationModuleName = argData.at(13);
		destinationParamName = argData.at(14);
		this.buildGuiSpecArray;
		this.setActionSpecs;
		this.oscActivate;
		this.registerWithParent;
		{
			this.sendCurrentValues;
			if (isPresetData == true, {
				this.updateTXVNode;
			});

		}.defer(0.5);
	}

	// override TXModuleBase class
	storePreset { arg argModule, presetNameString;
		if (presetNameString != "Default Settings", {
			super.storePreset(argModule, presetNameString);
		});
	}
}


