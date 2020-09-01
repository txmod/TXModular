// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_DrawConnection : TXV_Module {
	classvar <defaultName = "TXV DrawConnection";
	classvar <>arrInstances;

	var <>sourceTXVID, <>sourceModuleName, <>destinationTXVID, <>destinationModuleName;

	updateTXVDrawConnection {
		parentTXVSystem.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/setDrawConnection",
			[txvModuleID, sourceTXVID, destinationTXVID, dictCurrentParameterVals["/TXDrawConnection_1/layerNo".asSymbol],
				dictCurrentParameterVals["/TXDrawConnection_1/active".asSymbol]]);
	}

	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXDrawConnection_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		["int", "/TXDrawConnection_1/layerNo", 1, 1, 20],
		["bool_int", "/TXDrawConnection_1/active", 1],
	];}

	setSynthArgSpecsForGui {
		var holdSourceModules, holdSourceModuleIDs, holdDestModules, holdDestModuleIDs;
		holdSourceModules = parentTXVSystem.getValidTXVDrawSources;
		holdDestModules = parentTXVSystem.getValidTXVDrawDestinations;
		holdSourceModuleIDs = holdSourceModules.collect({arg item, i; item.txvModuleID});
		holdDestModuleIDs = holdDestModules.collect({arg item, i; item.txvModuleID});
		this.setSynthArgSpec("drawConnectionSourceIndex", holdSourceModuleIDs.indexOf(sourceTXVID) ? 0);
		this.setSynthArgSpec("drawConnectionDestIndex", holdDestModuleIDs.indexOf(destinationTXVID) ? 0);
	}

	buildGuiSpecArray {
		if (parentTXVSystem.notNil, {
			this.setSynthArgSpecsForGui;
			guiSpecArray = [
				["TXListViewAction", "source", {parentTXVSystem.getValidTXVDrawSourceOutputNames},
					"drawConnectionSourceIndex",
					{ arg view;
						var holdModule;
						holdModule = parentTXVSystem.getValidTXVDrawSources[view.value];
						sourceTXVID = holdModule.txvModuleID;
						sourceModuleName = holdModule.instName;
						this.updateTXVDrawConnection;
						this.buildGuiSpecArray;
						parentTXVSystem.buildGuiSpecArray;
						system.showView;
					}, 400, 150],
				["Spacer", 4],
				["TXListViewAction", "destination", {parentTXVSystem.getValidTXVDrawDestModuleNames}, "drawConnectionDestIndex",
					{ arg view;
						var holdModule;
						holdModule = parentTXVSystem.getValidTXVDrawDestinations[view.value];
						destinationTXVID = holdModule.txvModuleID;
						destinationModuleName = holdModule.instName;
						this.updateTXVDrawConnection;
						this.buildGuiSpecArray;
						parentTXVSystem.buildGuiSpecArray;
						system.showView;
					}, 400, 150],
				["NextLine"],
				["TXV_GuiScroller", this.getTXVParameters,
					dictCurrentParameterVals,
					{arg argAddress, argValue;
						this.sendModuleParmToTXV( argAddress, argValue);
						parentTXVSystem.buildGuiSpecArray;
					},
					{arg view; holdScrollView = view;},
					{arg view; data['visibleOrigins'][displayOption.asSymbol] = view.visibleOrigin; },
					data['dictParameterGroups'],
					250,
				]
			];
		});
	}

	// override base class
	getExtraSynthArgSpecs {
		^[
			["drawConnectionSourceIndex", 0],
			["drawConnectionDestIndex", 0],
		];
	}

	extraSaveData {
		^[dictCurrentParameterVals.asArgsArray, parentTXVSysModuleID, txvModuleID, txvModuleName, txvModuleType,
			canDraw, canDrawLayers, numModInputs, numModOutputs,
		sourceTXVID, sourceModuleName, destinationTXVID, destinationModuleName];
	}

	loadExtraData {arg argData, isPresetData = false;  // override base class
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
		destinationTXVID = argData.at(11);
		destinationModuleName = argData.at(12);
		this.buildGuiSpecArray;
		this.setActionSpecs;
		this.oscActivate;
		this.registerWithParent;
		{
			this.sendCurrentValues;
			if (isPresetData == true, {
				this.updateTXVDrawConnection;
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


