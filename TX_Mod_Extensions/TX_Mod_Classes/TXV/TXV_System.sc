// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_System : TXV_Module {

	/*
	//////////////////////////////////////////////////////////////
	//
	// Notes:
	//
	// OSC Message format sending to TXV:
	// [string address, int systemCode, int msgSeqNo, float msgTime, string systemData1, string systemData2,  ...dataArgs]
	// NOTE: systemData1 & systemData2 - are not currently used but may be in future.

	// set up an initial handshake between TXV and TX
	// respond to these replies from TXV by creating TXV modules:

	// OSC reply msg format when module/node/drawConnection added:
	//  msg: ["/TX_Modular_System/OSCReply", msgSeqNo, "/TX_Modular_System/TXSystem_1/addModule",
	//      "Confirmation: Module Added",
	//      int replyToMsgSeqNo, string sendAddress, string reply, int moduleID, string moduleName,
	//      int canDraw, int canDrawLayers, int numModInputs, int numModOutputs]
	// EXAMPLES:
	// msg: [ /TX_Modular_System/OSCReply, 1, 900, /TX_Modular_System/TXSystem_1/ping, Ping Back ]
	// msg: [ /TX_Modular_System/OSCReply, 2, 901, /TX_Modular_System/TXSystem_1/systemData, Confirmation: System Data, 1, TXSystem, TXSystem_1, 1, 1, 0, 0 ]
	// msg: [ /TX_Modular_System/OSCReply, 3, 0, /TX_Modular_System/TXSystem_1/addModule, Confirmation: Module Added, 2, TXLFO, TXLFO_1, 0, 0, 8, 1 ]
	// msg: [ /TX_Modular_System/OSCReply, 6, 3, /TX_Modular_System/TXSystem_1/addNode, Confirmation: Node Added, 5, TXModNode, TXModNode_1, 0, 0, 2, 0 ]
	// msg: [ /TX_Modular_System/OSCReply, 8, 5, /TX_Modular_System/TXSystem_1/addDrawConnection, Confirmation: Draw Connection Added, 7, TXDrawConnection, TXDrawConnection_1, 0, 0, 0, 0 ]

	//////////////////////////////////////////////////////////////

	// TXV System - test code to build simple system
	(
	//OSCFunc.trace(true); // Turn posting on
	//OSCFunc.trace(false); // Turn posting off

	b = NetAddr.new("127.0.0.1", 1999);    // create the NetAddr

		b.sendMsg("/TX_Modular_System/TXSystem_1/ping", 1000000, 900, Process.elapsedTime, "", "");
	b.sendMsg("/TX_Modular_System/TXSystem_1/systemData", 1000000, 901, Process.elapsedTime, "", "");

	b.sendMsg("/TX_Modular_System/TXSystem_1/addModule", 1000000, 0, Process.elapsedTime, "", "", "TXLFO");
	b.sendMsg("/TX_Modular_System/TXSystem_1/addModule", 1000000, 1, Process.elapsedTime, "", "", "TXLFO");
	b.sendMsg("/TX_Modular_System/TXSystem_1/addModule", 1000000, 2, Process.elapsedTime, "", "", "TXDrawCircles");
	b.sendMsg("/TX_Modular_System/TXSystem_1/addNode", 1000000, 3, Process.elapsedTime, "", "", 2, "out", 4, "positionX", 1.0, 0, 1);
	b.sendMsg("/TX_Modular_System/TXSystem_1/addNode", 1000000, 4, Process.elapsedTime, "", "", 3, "out", 4, "positionY", 1.0, 0, 1);
	b.sendMsg("/TX_Modular_System/TXSystem_1/addDrawConnection", 1000000, 5, Process.elapsedTime, "", "", 4, 1, 1);

	//b.sendMsg("/TX_Modular_System/TXSystem_1/printModuleParameters", 1000000, 6, Process.elapsedTime, "", "", 1);
	b.sendMsg("/TX_Modular_System/TXSystem_1/printModuleParameters", 1000000, 7, Process.elapsedTime, "", "", 2);
	// b.sendMsg("/TX_Modular_System/TXSystem_1/printModuleParameters", 1000000, 8, Process.elapsedTime, "", "", 3);
	b.sendMsg("/TX_Modular_System/TXSystem_1/printModuleParameters", 1000000, 9, Process.elapsedTime, "", "", 4);
	b.sendMsg("/TX_Modular_System/TXSystem_1/printModuleParameters", 1000000, 10, Process.elapsedTime, "", "", 5);
	b.sendMsg("/TX_Modular_System/TXSystem_1/printModuleParameters", 1000000, 11, Process.elapsedTime, "", "", 7);

	// txv setModuleParameter message: (moduleID, parameterNameString, newValue)
	b.sendMsg("/TX_Modular_System/TXSystem_1/setModuleParameter", 1000000, 101, Process.elapsedTime, "", "", 2, "/TXLFO_1/bpm/fixedValue", 26.0);
	b.sendMsg("/TX_Modular_System/TXSystem_1/setModuleParameter", 1000000, 102, Process.elapsedTime, "", "", 3, "/TXLFO_2/bpm/fixedValue", 36.0);
	)
	/////////////////////////////////////

	//  PRINT SC GUI CODE FOR MODULES:::
	(
	 // create the NetAddr
	b = NetAddr.new("127.0.0.1", 1999);

	 // delete previous modules
	b.sendMsg("/TX_Modular_System/TXSystem_1/deleteAllModules", 1000000, 100, Process.elapsedTime, "", "");

	//  PRINT SC GUI CODE FOR SYSTEM::::
	//b.sendMsg("/TX_Modular_System/TXSystem_1/printModuleInputsOutputs", 1000000, 101, Process.elapsedTime, "", "", 1);

	//  PRINT SC GUI CODE FOR INDIVIDUAL MODULES:::
	//b.sendMsg("/TX_Modular_System/TXSystem_1/addModule", 1000000, 101, Process.elapsedTime, "", "", "TXSmoothMulti", 2);
	//b.sendMsg("/TX_Modular_System/TXSystem_1/addModule", 1000000, 101, Process.elapsedTime, "", "", "TXColorize", 2);
	b.sendMsg("/TX_Modular_System/TXSystem_1/addModule", 1000000, 101, Process.elapsedTime, "", "", "TXRenderImage", 2);

	// PRINT DETAILS
	b.sendMsg("/TX_Modular_System/TXSystem_1/printModuleInputsOutputs", 1000000, 102, 2, Process.elapsedTime, "", "");
	)

	*/

	// ==========================================================
	/* 	LIST OF METHODS - the following code generates the list:
	(
		var a, b;
		a = TXV_System.methods;
		b = a.collect ({ arg item, i; item.name.asString;});
		b.sort.do ({ arg item, i; ("// " ++ item).postln;});
		" ".postln;
	)
	*/
	// ==========================================================
	// LIST OF METHODS
	// ==========================================================
	// addCommandToTXVQueue
	// addTXVAsset
	// addTXVDrawConnection
	// addTXVModule
	// addTXVNode
	// allowNextCommandInTXVQueue
	// arrAddressData
	// arrTXVAssets
	// arrTXVModules
	// buildArrNetAddresses
	// buildGuiSpecArray
	// deleteModuleExtraActions
	// deleteSelectedAsset
	// deleteTXVModule
	// deregisterChildModule
	// editSelectedDrawConnection
	// editSelectedModule
	// editSelectedNode
	// emptyTXVQueue
	// extraSaveData
	// finishRebuilding
	// getButtonColour
	// getDrawConnSourceDestName
	// getDrawConnectionWithTXVID
	// getExtraSynthArgSpecs
	// getInitialDisplayOption
	// getModuleOrNodeWithTXVID
	// getModuleWithTXVID
	// getNextOscMsgNo
	// getNodeSourceDestName
	// getNodeWithTXVID
	// getSortedTXVDrawConnectionNames
	// getSortedTXVDrawConnections
	// getSortedTXVModuleNames
	// getSortedTXVModules
	// getSortedTXVNodeNames
	// getSortedTXVNodes
	// getTXVOutputs
	// getTXVParameters
	// connectToTXVSystem
	// getValidTXVDrawDestModuleNames
	// getValidTXVDrawDestinations
	// getValidTXVDrawSourceOutputNames
	// getValidTXVDrawSources
	// getValidTXVNodeDestModuleNames
	// getValidTXVNodeDestModules
	// getValidTXVNodeDestParamNames
	// getValidTXVNodeSourceModuleNames
	// getValidTXVNodeSourceModules
	// getValidTXVNodeSourceOutputNames
	// initExtraActions
	// loadArrAddressData
	// loadExtraData
	// oscActivateExtraActions
	// oscDeactivateExtraActions
	// processDragAssets
	// rebuildGuiSpecArrays
	// rebuildTXVSystem
	// registerChildModule
	// resetTXVSystem
	// sendCommandToTXV
	// sendMsgToTXV
	// sendParameterToTXV
	// setActionSpecs
	// setFullScreen
	// systemData
	// systemData_
	// txvOSCReplyProcess
	// updateScreen



	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar <guiWidth = 1070;

	// var <>parentTXVSysModuleID;
	// var <>txvModuleID;
	// var <>txvModuleName;
	// var <>txvModuleType;
	// var <>canDraw = false;
	// var <>canDrawLayers = true;
	// var <>numModInputs = 0;
	// var <>numModOutputs = 0;

	var <>systemData;
	var txvSystemFound = false;

	//	var	oscControlResp;
	var txvOSCReplyResp;
	var	sendTrigID;
	var	holdPortNames;
	var	holdMIDIOutPort, holdMIDIDeviceName, holdMIDIPortName;
	var	displayOption, displayModulesGroup = "All";
	var displayModulesGpNames, displayModulesGpIndices;
	var	arrNetAddresses;
	var txvSystemCode = 1000000;
	var nextOscMsgNo = 0;

	var dictOscCommandsSent;  // commands are add/delete modules/change structure, not parameter changes
	var txvCommandQueue;
	var txvCommandQueueCondition;
	var <arrTXVModules;
	var arrTXVNodes;
	var arrTXVDrawConnections;
	var <arrTXVAssets;
	var <arrAssetBankNames;
	var currentAssetType, holdBankIndexView, holdAssetBankNameView, dictAssetClipboards;
	var <>runningStatus;
	var <>runningStatusView;
	var arrOscRepliesReceived;
	var <>txvMessagesView;
	// Note - txvSystemData1 & txvSystemData2 are not currently used
	var txvSystemData1 = "";
	var txvSystemData2 = "";
	var txvAppPath = "", txvAppFound = false;

	*initClass{
		arrInstances = [];
		//	set class specific variables
		defaultName = "TXV System";
		moduleRate = "control";
		moduleType = "action";
		noInChannels = 0;
		noOutChannels = 0;
		arrOutBusSpecs = [];
	}

	// *new{ arg argInstName;
	// 	^super.new.init(argInstName);
	// }

	*addressesCopyAll { arg argData;
		arrInstances.do({arg module, i;
			module.loadArrAddressData(argData);
		});
	}

	////////////////////////////////////////////////
	// OLD CODE
	/*
	init {
	// create unique id
	sendTrigID = UniqueID.next;
	arrSynthArgSpecs = [
	["out", 0, 0],
	["on", 0, 0],
	// ["controlValue", 0, 0],
	// ["controlValMin", 0, 0],
	// ["controlValMax", 1, 0],
	// ["modControlVal", 0, 0],

	// N.B. the args below aren't used in the synthdef, just stored as synth args for convenience
	// ["oscString", "/example/text"],
	];
	arrOptions = [1, 0];
	arrOptionData = [
	[	["10 times per second", 10],
	["20 times per second - default", 20],
	["40 times per second", 40],
	["80 times per second", 80],
	["5 times per second", 5],
	["once every second", 1],
	["once every 2 seconds", 0.5],
	["once every 4 seconds", 0.25],
	],
	[	["Number: Float", {arg argNum; argNum}],
	["Number: Integer - floats are rounded to nearest integer", {arg argNum; argNum.round}],
	],
	];
	synthDefFunc = { arg out, on, controlValue, controlValMin, controlValMax, modControlVal;
	var trig, trig2, sumControl, dataRate, numberFunc;
	sumControl = controlValMin + ((controlValMax - controlValMin) * (controlValue + modControlVal).max(0).min(1));
	// select datarate based on arrOptions
	dataRate = this.getSynthOption(0);
	// trigger current value to be sent every sec and when value changes
	trig = Trig.kr((1 - Impulse.kr(dataRate)) * HPZ1.kr(sumControl).abs, 0.005);
	trig2 = Impulse.kr(1);
	numberFunc = this.getSynthOption(1);
	SendTrig.kr(trig + trig2 * on, sendTrigID, numberFunc.value(sumControl));
	// Note this synth doesn't need to write to the output bus
	};
	this.buildGuiSpecArray;
	arrActionSpecs = this.buildActionSpecs([

	// OLD CODE:
	// ["TXTextBox", "OSC String", "oscString"],
	// ["SynthOptionPopup", "Data rate", arrOptionData, 0],
	// ["TXMinMaxSliderSplit", "Value", ControlSpec(-1000000.0, 1000000.0), "controlValue",
	// 	"controlValMin", "controlValMax", nil,
	// 	[["Presets:", []], ["0 / 1", [0,1]], ["0 / 127", [0, 127]],
	// ["0 / 255", [0, 255]], ["-1  /  1", [-1, 1]], ]],
	// ["SynthOptionPopup", "Output type", arrOptionData, 1],
	// ["TXCheckBox", "Active", "on"],

	//	["EZNumber", "OSC Latency", ControlSpec(0, 1), "latency"],
	["TXNetAddress","Address 1", "i_address1", {this.buildArrNetAddresses;}],
	["EZNumber", "Port 1", ControlSpec(0, 99999, 'lin', 1), "i_port1"],
	["TXTextBox","Notes", "i_notes1"],
	["TXCheckBox", "Activate", "i_activate1"],
	["TXNetAddress","Address 2", "i_address2", {this.buildArrNetAddresses;}],
	["EZNumber", "Port 2", ControlSpec(0, 99999, 'lin', 1), "i_port2"],
	["TXTextBox","Notes", "i_notes2"],
	["TXCheckBox", "Activate", "i_activate2"],
	["TXNetAddress","Address 3", "i_address3", {this.buildArrNetAddresses;}],
	["EZNumber", "Port 3", ControlSpec(0, 99999, 'lin', 1), "i_port3"],
	["TXTextBox","Notes", "i_notes3"],
	["TXCheckBox", "Activate", "i_activate3"],
	["TXNetAddress","Address 4", "i_address4", {this.buildArrNetAddresses;}],
	["EZNumber", "Port 4", ControlSpec(0, 99999, 'lin', 1), "i_port4"],
	["TXTextBox","Notes", "i_notes4"],
	["TXCheckBox", "Activate", "i_activate4"],
	]);

	//	use base class initialise
	this.baseInit(this, argInstName);
	this.buildArrNetAddresses;
	this.oscActivate;
	//	load the synthdef and create the synth
	this.loadAndMakeSynth;
	}
	*/
	////////////////////////////////////////////////

	// add text array to ..."maxWidthHeightRule":
	// , ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]

	// add text array to ..."drawLayersRule/fixedValue":
	// , ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]


	getTXVParameters{ ^[
		/* ---------- TXV Settable Parameter list for: TXSystem_1 ---------- */
		/* GROUP:     [groupType, address, parameters] */
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		[ "modParameterGroupBool", "/TXSystem_1/drawActive", [
			["bool_int", "/TXSystem_1/drawActive/fixedValue", 1],
			["bool_int", "/TXSystem_1/drawActive/fixedModMix", 0],
			["bool_int", "/TXSystem_1/drawActive/useExtMod", 0],
			["float", "/TXSystem_1/drawActive/extModValue", 0, 0, 1],
		]],
		/* Hidden for now - not used
		["int", "/TXSystem_1/maxWidthHeightRule", 0, 0, 5, ["Use Screen Width & Height", "Square - Use Max of Screen Width & Height", "Square - Use Min of Screen Width & Height", "Square - Use Screen Width", "Square - Use Screen Height", "Use Custom Size", ]],
		["int", "/TXSystem_1/customMaxWidth", 1024, 1, 4096],
		["int", "/TXSystem_1/customMaxHeight", 768, 1, 4096],
		*/
		[ "modParameterGroupInt", "/TXSystem_1/drawLayersRule", [
			["int", "/TXSystem_1/drawLayersRule/fixedValue", 0, 0, 26, ["All layers except Layer 20", "Layer 1", "Layer 2", "Layer 3", "Layer 4", "Layer 5", "Layer 6", "Layer 7", "Layer 8", "Layer 9", "Layer 10", "Layer 11", "Layer 12", "Layer 13", "Layer 14", "Layer 15", "Layer 16", "Layer 17", "Layer 18", "Layer 19", "Layer 20", "Layers 1 - 5", "Layers 6 - 10", "Layers 11 - 15", "Layers 16 - 20", "Layers 1 - 10", "Layers 11 - 20"]],
			["float", "/TXSystem_1/drawLayersRule/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSystem_1/drawLayersRule/useExtMod", 0],
			["float", "/TXSystem_1/drawLayersRule/extModValue", 0, 0, 1],
			["int", "/TXSystem_1/drawLayersRule/softMin", 0, 0, 26],
			["int", "/TXSystem_1/drawLayersRule/softMax", 26, 0, 26],
		]],
		[ "modParameterGroupBool", "/TXSystem_1/freezeSystem", [
			["bool_int", "/TXSystem_1/freezeSystem/fixedValue", 0],
			["bool_int", "/TXSystem_1/freezeSystem/fixedModMix", 0],
			["bool_int", "/TXSystem_1/freezeSystem/useExtMod", 0],
			["float", "/TXSystem_1/freezeSystem/extModValue", 0, 0, 1],
		]],
		["float", "/TXSystem_1/oscLatency", 0, 0, 30],
		["bool_int", "/TXSystem_1/showFullScreen", 0],
		["int", "/TXSystem_1/windowSizeMode", 0, 0, 6, ["CustomWindowSize", "480 x 360", "858 x 480", "1280 x 720", "1920 x 1080", "2560 x 1440", "3840 x 2160", ]],
		["int", "/TXSystem_1/customWindowWidth", 1024, 1, 5000],
		["int", "/TXSystem_1/customWindowHeight", 768, 1, 5000],
		["bool_int", "/TXSystem_1/antiAliasing", 1],
		["bool_int", "/TXSystem_1/neverDropFrames", 1],
		["bool_int", "/TXSystem_1/showFrameRate", 0],
		["int", "/TXSystem_1/targetFrameRate", 30, 1, 100],
		["float", "/TXSystem_1/alphaThreshold", 0, 0, 0.05],
		[ "modParameterGroupBool", "/TXSystem_1/resetClocks", [
			["bool_int", "/TXSystem_1/resetClocks/fixedValue", 0],
			["bool_int", "/TXSystem_1/resetClocks/fixedModMix", 0],
			["bool_int", "/TXSystem_1/resetClocks/useExtMod", 0],
			["float", "/TXSystem_1/resetClocks/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSystem_1/masterBpm", [
			["float", "/TXSystem_1/masterBpm/fixedValue", 120, 1, 1000],
			["float", "/TXSystem_1/masterBpm/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSystem_1/masterBpm/useExtMod", 0],
			["float", "/TXSystem_1/masterBpm/extModValue", 0, 0, 1],
			["float", "/TXSystem_1/masterBpm/softMin", 1, 1, 1000],
			["float", "/TXSystem_1/masterBpm/softMax", 1000, 1, 1000],
		]],
		[ "modParameterGroupBool", "/TXSystem_1/showBackground", [
			["bool_int", "/TXSystem_1/showBackground/fixedValue", 1],
			["bool_int", "/TXSystem_1/showBackground/fixedModMix", 0],
			["bool_int", "/TXSystem_1/showBackground/useExtMod", 0],
			["float", "/TXSystem_1/showBackground/extModValue", 0, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSystem_1/backgroundHue", [
			["float", "/TXSystem_1/backgroundHue/fixedValue", 0, 0, 1],
			["float", "/TXSystem_1/backgroundHue/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSystem_1/backgroundHue/useExtMod", 0],
			["float", "/TXSystem_1/backgroundHue/extModValue", 0, 0, 1],
			["float", "/TXSystem_1/backgroundHue/softMin", 0, 0, 1],
			["float", "/TXSystem_1/backgroundHue/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSystem_1/backgroundSaturation", [
			["float", "/TXSystem_1/backgroundSaturation/fixedValue", 0.5, 0, 1],
			["float", "/TXSystem_1/backgroundSaturation/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSystem_1/backgroundSaturation/useExtMod", 0],
			["float", "/TXSystem_1/backgroundSaturation/extModValue", 0, 0, 1],
			["float", "/TXSystem_1/backgroundSaturation/softMin", 0, 0, 1],
			["float", "/TXSystem_1/backgroundSaturation/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSystem_1/backgroundBrightness", [
			["float", "/TXSystem_1/backgroundBrightness/fixedValue", 0, 0, 1],
			["float", "/TXSystem_1/backgroundBrightness/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSystem_1/backgroundBrightness/useExtMod", 0],
			["float", "/TXSystem_1/backgroundBrightness/extModValue", 0, 0, 1],
			["float", "/TXSystem_1/backgroundBrightness/softMin", 0, 0, 1],
			["float", "/TXSystem_1/backgroundBrightness/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupFloat", "/TXSystem_1/backgroundAlpha", [
			["float", "/TXSystem_1/backgroundAlpha/fixedValue", 1, 0, 1],
			["float", "/TXSystem_1/backgroundAlpha/fixedModMix", 0, 0, 1],
			["bool_int", "/TXSystem_1/backgroundAlpha/useExtMod", 0],
			["float", "/TXSystem_1/backgroundAlpha/extModValue", 0, 0, 1],
			["float", "/TXSystem_1/backgroundAlpha/softMin", 0, 0, 1],
			["float", "/TXSystem_1/backgroundAlpha/softMax", 1, 0, 1],
		]],
		[ "modParameterGroupBool", "/TXSystem_1/backgroundHSBAsRGB", [
			["bool_int", "/TXSystem_1/backgroundHSBAsRGB/fixedValue", 0],
			["bool_int", "/TXSystem_1/backgroundHSBAsRGB/fixedModMix", 0],
			["bool_int", "/TXSystem_1/backgroundHSBAsRGB/useExtMod", 0],
			["float", "/TXSystem_1/backgroundHSBAsRGB/extModValue", 0, 0, 1],
		]],
	];}

	getTXVOutputs{ ^[
		/* ---------- TXV Outputs list for: TXSystem_1 ---------- */
		"mouseButtonPressed",
		"mouseDraggedX",
		"mouseDraggedY",
		"mouseMovedX",
		"mouseMovedY",
		"mouseMovedOrDraggedX",
		"mouseMovedOrDraggedY",
	];}

	// ============================================= //

	// override base class
	initExtraActions {
		parentTXVSystem = this;
		this.buildArrNetAddresses;
		canDraw = false; // override
		numModOutputs = 0;
		txvModuleType = "TXSystem";
		txvAppPath = this.class.filenameSymbol.asString.asPathName.pathOnly ++ "TXV_App/TXV.app";
		txvAppFound = File.exists(txvAppPath);
		dictOscCommandsSent = ();
		dictAssetClipboards = MultiLevelIdentityDictionary.new;
		txvCommandQueue = List[];
		txvCommandQueueCondition = Condition.new;
		arrTXVModules = [];
		arrTXVNodes = [];
		arrTXVDrawConnections = [];
		arrTXVAssets = [];  // format: [assetType, assetTypeString, assetID, assetFilename, fileExists, bankNo]
		systemData = ();
		systemData.rebuildingStatus = false;
		systemData.txvModules = [
			// format: [TXV Class, TXM Name]
			// not including Nodes or Draw Connections
			// put in alpha order by 2nd array item
			// e.g.["TXyyy","TXV yyy"],
			["TXDraw3DModel", "TXV 3D Model"],
			//["TXDraw3DWorld", "TXV 3D World"], // OLD
			["TXBlend","TXV Blend"],
			["TXBloom","TXV Bloom"],
			["TXBlur","TXV Blur"],
			["TXDrawBSurfaces", "TXV BSurfaces"],
			["TXDrawBSurfaces2", "TXV BSurfaces2"],
			["TXDrawBSurfacesX6", "TXV BSurfacesX6"],
			["TXColorControls","TXV Color Controls"],
			["TXColorCurves","TXV Color Curves"],
			["TXColorize","TXV Colorize"],
			["TXColorSampler","TXV Color Sampler"],
			["TXConvolution","TXV Convolution"],
			//["TXDrawCircles", "TXV Circles"],  // OLD
			// ["TXDraw2DCurves", "TXV Curves2D"],  // OLD
			// ["TXDraw2DDelayCurves", "TXV Delay Curves2D"],  // OLD
			["TXDrawCurves", "TXV Curves"],
			["TXDrawDelayCurves", "TXV Delay Curves"],
			["TXEnvMap","TXV EnvMap"],
			["TXFeedback","TXV Feedback"],
			["TXFindEdges","TXV Find Edges"],
			["TXDrawFluid","TXV Fluid"],
			["TXDrawFractal","TXV Fractal"],
			["TXDrawFractal3D","TXV Fractal3D"],
			["TXDrawFractalDelay","TXV FractalDelay"],
			["TXDrawFractalDelay3D","TXV FractalDelay3D"],
			["TXGenerator","TXV Generator"],
			["TXGlow","TXV Glow"],
			["TXDrawGradient","TXV Gradient"],
			["TXDrawImage","TXV Image"],
			["TXKaleidoscope","TXV Kaleidoscope"],
			// ["TXLeapMotion","TXV Leap Motion"],  // not working
			["TXLevels","TXV Levels"],
			["TXLFO","TXV LFO"],
			["TXLFO2D","TXV LFO2D"],
			["TXLFO3D","TXV LFO3D"],
			["TXLumaKey","TXV LumaKey"],
			//["TXLUT","TXV LUT Slow"],  // removed - too slow
			["TXLUTFast","TXV LUT"],
			// ["TXMeshDistort","TXV Mesh Distort"],       // not working
			// ["TXMeshDistortHemi","TXV Mesh Dis Hemi"],  // not working
			["TXDrawMovie","TXV Movie"],
			// ["TXDrawMovieOSX","TXV MovieOSX"],  // not working with OpenGL3 renderer
			["TXDrawParticles", "TXV Particles"],
			["TXDrawParticles3D", "TXV Particles3D"],
			["TXOSCControlIn","TXV OSC ControlIn"],
			["TXOSCControlIn2D","TXV OSC ControlIn2D"],
			["TXOSCControlIn3D","TXV OSC ControlIn3D"],
			["TXPerlin","TXV Perlin"],
			["TXPixelate","TXV Pixelate"],
			["TXRenderImage","TXV Render Image"],
			["TXSampleHold","TXV Sample & Hold"],
			["TXDrawScene","TXV Scene"],
			["TXDraw2DShape", "TXV Shape2D"],
			["TXDraw2DShapeMulti", "TXV Shape2D Multi"],
			["TXDrawShape3D", "TXV Shape3D"],
			["TXDrawShape3DMulti", "TXV Shape3D Multi"],
			["TXSlideShow","TXV Slide Show"],
			["TXSmooth","TXV Smooth"],
			["TXSmoothMulti","TXV SmoothMulti"],
			["TXSoftThreshold","TXV Soft Threshold"],
			["TXStepSequencer","TXV Step Sequencer"],
			["TXDrawSVG","TXV SVG"],
			// ["TXTablet","TXV Tablet"],  // not working
			["TXDrawText3D","TXV Text3D"],
			["TXThreshold","TXV Threshold"],
			["TXTileMask","TXV Tile & Mask"],
			["TXToggle","TXV Toggle"],
			["TXDrawTransform", "TXV Transform"],
			["TXDrawTransform3D", "TXV Transform3D"],
			["TXDrawTransform3DDelay", "TXV Transf3D Delay"],
			["TXDrawTransform3DMulti", "TXV Transf3D Multi"],
			["TXDrawVideoCam", "TXV VideoCam"],
			["TXWallpaper","TXV Wallpaper"],
			["TXWarp","TXV Warp"],
			["TXWarpMorph","TXV WarpMorph"],
			["TXWaveTerrain","TXV Wave Terrain"],
			["TXWobble","TXV Wobble"],
		];
		systemData.validTXVModuleTypes = systemData.txvModules.collect({arg item, i; item[0]});
		systemData.validTXVModuleNames = systemData.txvModules.collect({arg item, i; item[1]});
		this.resetArrAssetBankNames;

		//	set  class specific instance variables
		displayOption = "showSystem";
		guiSpecTitleArray = [
			["TitleBar"],
			["ActionButton", "Help", {
				var holdPath = this.class.filenameSymbol.asString.asPathName;
				var txvHelpPath = holdPath.pathOnly ++ "TXV HelpFiles/TX_"
					++ this.class.defaultName ++".html";
				TXHelpScreen.openFilePath(txvHelpPath);
			}, 40, TXColor.white, TXColor.sysHelpCol],
			// ["RunPauseButton"],
			["DeleteModuleButton"],
			["RebuildModuleButton"],
			["HideModuleButton"],
			["LegacyModuleText"],
			["Spacer", 10],
		];
		// if TXV app is found, add start button
		if (txvAppFound, {
			guiSpecTitleArray = guiSpecTitleArray ++ [
				["ActionButton", "Start TXV",
					{this.startTXVSystem},
					60, TXColor.white, TXColor.green.blend(TXColor.grey4)],
			];
		});

		guiSpecTitleArray = guiSpecTitleArray ++ [
			["ActionButton", "Connect to TXV",
				{this.connectToTXVSystem; this.buildGuiSpecArray; system.showView;},
				100, TXColor.white, TXColor.sysGuiCol3],
			["ActionButton", "Rebuild TXV System",
				{this.rebuildTXVSystem; this.buildGuiSpecArray; system.showView;},
				120, TXColor.white, TXColor.sysGuiCol3],
			["TXStaticText", "Status", {this.runningStatus},
				{arg view; if ((view.class == TextView) || (view.class == TextField), {runningStatusView = view},
					{runningStatusView = view.textView}); }, 280, 50, TXColor.paleYellow],
			["ActionButton", "Kill TXV",
				{this.killTXVSystem; },
				60, TXColor.white, TXColor.sysDeleteCol],
			["NextLine"],
			["ModuleActionPopup", 320],
			["ModuleInfoTxt", 420],
			["SpacerLine", 1],
		];

		this.updateRunningStatus(" Not connected");
		// communicate with TXV to get data
		// {this.connectToTXVSystem;}.defer(1); removed for now
	}

	// ============================================= //

	// override base class
	getExtraSynthArgSpecs {
		^[
			["latency", 0.2],
			["selectedModuleIndex", 0],
			["moduleSortOrder", 0],
			["addModuleTypeIndex", 0],

			["selectedNodeIndex", 0],
			["nodeSortOrder", 0],
			["nodeSourceModuleIndex", 0],
			["nodeSourceOutputNameIndex", 0],
			["nodeDestModuleIndex", 0],
			["nodeDestParamNameIndex", 0],

			["selectedDrawConnectionIndex", 0],
			["drawConnectionSortOrder", 0],
			["drawConnectionSourceIndex", 0],
			["drawConnectionDestIndex", 0],
			["drawConnectionLayerNo", 1],
			["selectedImageConnectionIndex", 0],

			["assetType", 0],
			["assetFileName", ""],
			["selectedAssetIndex", 0],
			["assetBankName", ""],
			["selectedAssetBankNo", 0],

			["i_address1", "127.0.0.1"],
			["i_port1", 1999],
			["i_notes1", ""],
			["i_activate1", 1],
			["i_address2", "0.0.0.0"],
			["i_port2", 1999],
			["i_notes2", ""],
			["i_activate2", 0],
			["i_address3", "0.0.0.0"],
			["i_port3", 1999],
			["i_notes3", ""],
			["i_activate3", 0],
			["i_address4", "0.0.0.0"],
			["i_port4", 1999],
			["i_notes4", ""],
			["i_activate4", 0],
		];
	}


	////////////////////////////////////////////////

	buildGuiSpecArray {
		var displayAssetType, holdAssetFileName, holdArrBankFileNames, holdPasteAssetColor, holdPasteAssetBankColor;
		var allModuleNames, drawSourceNames, imageFXNames, drawDestinationNames, moduleSourceNames;
		drawSourceNames = [
			"TXV 3D Model",
			"TXV BSurfaces",
			"TXV BSurfaces2",
			"TXV BSurfacesX6",
			"TXV Curves",
			"TXV Delay Curves",
			"TXV Fluid",
			"TXV Generator",
			"TXV Gradient",
			"TXV Image",
			"TXV Movie",
			// "TXV MovieOSX",  // NOTE TXDrawMovieOSX causes crashes with OpenGL3 renderer
			"TXV Particles",
			"TXV Particles3D",
			"TXV Shape2D",
			"TXV Shape2D Multi",
			"TXV Shape3D",
			"TXV Shape3D Multi",
			"TXV Slide Show",
			"TXV SVG",
			"TXV Text3D",
			"TXV VideoCam",
			"TXV Wave Terrain",
		];
		imageFXNames = [
			"TXV Blend",
			"TXV Bloom",
			"TXV Blur",
			"TXV Color Controls",
			"TXV Color Curves",
			"TXV Colorize",
			"TXV Convolution",
			"TXV Find Edges",
			"TXV Glow",
			"TXV Kaleidoscope",
			"TXV Levels",
			"TXV LumaKey",
			"TXV LUT",
			"TXV Pixelate",
			"TXV Soft Threshold",
			"TXV Threshold",
			"TXV Tile & Mask",
			"TXV Wallpaper",
			"TXV Wobble",
		];
		drawDestinationNames = [
			"TXV EnvMap",
			"TXV Feedback",
			"TXV Fractal",
			"TXV Fractal3D",
			"TXV FractalDelay",
			"TXV FractalDelay3D",
			// "TXV Mesh Distort", // doesn't work
			// "TXV Mesh Dis Hemi",  // doesn't work
			"TXV Render Image",
			"TXV Scene",
			"TXV Transform",
			"TXV Transform3D",
			"TXV Transf3D Delay",
			"TXV Transf3D Multi",
		];
		moduleSourceNames = [
			"TXV Color Sampler",
			// "TXV Leap Motion",  // doesn't work
			"TXV LFO",
			"TXV LFO2D",
			"TXV LFO3D",
			"TXV OSC ControlIn",
			"TXV OSC ControlIn2D",
			"TXV OSC ControlIn3D",
			"TXV Perlin",
			"TXV Sample & Hold",
			"TXV Smooth",
			"TXV SmoothMulti",
			"TXV Step Sequencer",
			// "TXV Tablet",  // doesn't work
			"TXV Toggle",
			"TXV Warp",
			"TXV WarpMorph",
		];
		// All, DrawSources, ImageFX, DrawDestinations, ModulationSources
		// drawSourceNames, imageFXNames, drawDestinationNames, moduleSourceNames;
		allModuleNames = systemData.validTXVModuleNames;
		case
		{displayModulesGroup == "All"} {displayModulesGpNames = allModuleNames;}
		{displayModulesGroup == "DrawSources"} {displayModulesGpNames = drawSourceNames;}
		{displayModulesGroup == "ImageFX"} {displayModulesGpNames = imageFXNames;}
		{displayModulesGroup == "DrawDestinations"} {displayModulesGpNames = drawDestinationNames;}
		{displayModulesGroup == "ModulationSources"} {displayModulesGpNames = moduleSourceNames;}
		; // end of case
		displayModulesGpIndices= [];
		allModuleNames.do({arg item, i;
			if (displayModulesGpNames.indexOfEqual(item).notNil, {
				displayModulesGpIndices	= displayModulesGpIndices.add(i);
			});
		});

		guiSpecArray = [
			["ActionButton", "TXV System", {displayOption = "showSystem";
				this.buildGuiSpecArray; system.showView;}, 90,
			TXColor.white, this.getButtonColour(displayOption == "showSystem")],
			["Spacer", 3],
			["ActionButton", "Messages", {displayOption = "showMessages";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showMessages")],
			["Spacer", 3],
			["ActionButton", "Draw & Background", {displayOption = "showDrawBackground";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showDrawBackground")],
			["Spacer", 3],
			["ActionButton", "TXV Modules", {displayOption = "showModules";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showModules")],
			["Spacer", 3],
			["ActionButton", "Modulation Nodes", {displayOption = "showNodes";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showNodes")],
			["Spacer", 3],
			["ActionButton", "Draw Connections", {displayOption = "showDrawConnections";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showDrawConnections")],
			["Spacer", 3],
			["ActionButton", "Image Connections", {displayOption = "showImageConnections";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showImageConnections")],
			["Spacer", 3],
			["ActionButton", "Assets", {displayOption = "showAssets";
				this.buildGuiSpecArray; system.showView;}, 60,
			TXColor.white, this.getButtonColour(displayOption == "showAssets")],
			["Spacer", 3],
			["ActionButton", "OSC Address", {displayOption = "showOSCAddress";
				this.buildGuiSpecArray; system.showView;}, 90,
			TXColor.white, this.getButtonColour(displayOption == "showOSCAddress")],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showSystem", {
			// latency removed for now
			/*
			guiSpecArray = guiSpecArray ++ [
			["NextLine"],
			["EZslider", "messageLatencySecs", [0, 10].asSpec, "latency", {}, 500, 200]
			];
			*/

			guiSpecArray = guiSpecArray ++[
				//["SpacerLine", 6],
				["TXV_GuiScroller", this.getTXVParameters.reject({arg parameter;
					var parmName = parameter[1].split.last;
					parmName.containsi("draw")
					|| parmName.containsi("background")
					|| parmName.containsi("maxWidthHeightRule")
					|| parmName.containsi("customMax")
					;}),
				dictCurrentParameterVals,
				{arg argAddress, argValue; this.sendParameterToTXV(txvModuleID, argAddress, argValue);},
				{arg view; holdScrollView = view;},
				{arg view; data['visibleOrigins'][displayOption.asSymbol] = view.visibleOrigin; },
				data['dictParameterGroups'],
				470, // height
				],
				//["SpacerLine", 20],
			];
		});
		if (displayOption == "showMessages", {
			guiSpecArray = guiSpecArray ++[
				// ["TXStaticText", "Messages Received ", {this.getTxvMessages},
				// 	{arg view; if ((view.class == TextView) || (view.class == TextField), {txvMessagesView = view},
				// {txvMessagesView = view.textView}); }, 800, 130, TXColor.white, 480],
				["TextViewDisplay", {this.getTxvMessages}, 800, 480, "Messages Received"],
				["ActionButtonBig", "Clear",
					{this.clearTxvMessages; system.showView;},
					80, TXColor.white, TXColor.sysDeleteCol],
			];
		});
		if (displayOption == "showDrawBackground", {
			guiSpecArray = guiSpecArray ++[
				["TXV_GuiScroller", this.getTXVParameters.select({arg parameter; var parmName = parameter[1].split.last;
					parmName.containsi("Draw")
					|| parmName.containsi("background")
					|| parmName.containsi("maxWidthHeightRule")
					|| parmName.containsi("customMax")
					;}),
				dictCurrentParameterVals,
				{arg argAddress, argValue; this.sendParameterToTXV(txvModuleID, argAddress, argValue);},
				{arg view; holdScrollView = view;},
				{arg view; data['visibleOrigins'][displayOption.asSymbol] = view.visibleOrigin; },
				data['dictParameterGroups'],
				470, // height
				],
				//["SpacerLine", 20],
			];
		});
		if (displayOption == "showModules", {
			guiSpecArray = guiSpecArray ++[
				// All, DrawSources, ImageFX, DrawDestinations, ModulationSources
				["TextBarLeft", "Display Group", 80],
				["ActionButton", "All Modules", {displayModulesGroup = "All";
					this.buildGuiSpecArray; system.showView;}, 90,
				TXColor.white, this.getButtonColour(displayModulesGroup == "All")],
				["Spacer", 3],
				["ActionButton", "Draw Sources", {displayModulesGroup = "DrawSources";
					this.buildGuiSpecArray; system.showView;}, 90,
				TXColor.white, this.getButtonColour(displayModulesGroup == "DrawSources")],
				["Spacer", 3],
				["ActionButton", "Image FX", {displayModulesGroup = "ImageFX";
					this.buildGuiSpecArray; system.showView;}, 90,
				TXColor.white, this.getButtonColour(displayModulesGroup == "ImageFX")],
				["Spacer", 3],
				["ActionButton", "Draw Destinations", {displayModulesGroup = "DrawDestinations";
					this.buildGuiSpecArray; system.showView;}, 110,
				TXColor.white, this.getButtonColour(displayModulesGroup == "DrawDestinations")],
				["Spacer", 3],
				["ActionButton", "Modulation Sources", {displayModulesGroup = "ModulationSources";
					this.buildGuiSpecArray; system.showView;}, 120,
				TXColor.white, this.getButtonColour(displayModulesGroup == "ModulationSources")],
				["SpacerLine", 4],
				["TXListViewActionPlusMinus", "Add module", displayModulesGpNames,
					"addModuleTypeIndex",
					{ arg view; }, 292, 430],
				["ShiftDecorator", -43, 40],
				["ActionButtonBig", "Add Selected Module", {this.addTXVModule}, 140, TXColor.white, TXColor.sysGuiCol1],
				["ShiftDecorator", 0, -40],
				["Spacer", 48],
				["TXListViewActionPlusMinus", "Edit module", this.getSortedTXVModuleNames,
					"selectedModuleIndex",
					{ arg view; /*this.editSelectedModule(view.value);*/ }, 292, 430],
				["ShiftDecorator", -43, 40],
				["ActionButtonBig", "Edit Selected Module", {this.editSelectedModule}, 140,
					TXColor.white, TXColor.sysGuiCol2],
				["ShiftDecorator", 0, -40],
				["NextLine"],
				["Spacer", 446],
				["TXPopupActionPlusMinus", "display order", ["build order", "alphabetical"],
					"moduleSortOrder", { arg view; this.buildGuiSpecArray; system.showView; }, 276],
				["NextLine"],
			];
		});
		if (displayOption == "showNodes", {
			guiSpecArray = guiSpecArray ++[
				["TXListViewActionPlusMinus", "source module", ["select..."] ++ this.getValidTXVNodeSourceModuleNames,
					"nodeSourceModuleIndex",
					{ arg view; this.setSynthArgSpec("nodeSourceOutputNameIndex", 0);
						this.buildGuiSpecArray; system.showView; }, 320, 60],
				["Spacer", 4],
				["TXListViewActionPlusMinus", "source name", ["select..."] ++ this.getValidTXVNodeSourceOutputNames,
					"nodeSourceOutputNameIndex",
					{ arg view; }, 400, 60],
				["NextLine"],
				["TXListViewActionPlusMinus", "destination module", ["select..."] ++ this.getValidTXVNodeDestModuleNames,
					"nodeDestModuleIndex",
					{ arg view; this.setSynthArgSpec("nodeDestParamNameIndex", 0);
						this.buildGuiSpecArray; system.showView; }, 320, 150],
				["Spacer", 4],
				["TXListViewActionPlusMinus", "destination name", ["select..."] ++ this.getValidTXVNodeDestParamNames,
					"nodeDestParamNameIndex",
					{ arg view; }, 400, 150],
				["Spacer", 10],
				["ActionButtonBig", "Add Node", {this.addTXVNode}, 70, TXColor.white, TXColor.sysGuiCol1],
				["SpacerLine", 10],
				["TXListViewActionPlusMinus", "TXV Nodes", {this.getSortedTXVNodeNames},
					"selectedNodeIndex",
					{ arg view; /* this.editSelectedNode(view.value);*/ }, 960, 220],
				["ShiftDecorator", -43, 40],
				["ActionButtonBig", "Edit Selected Node", {this.editSelectedNode}, 120,
					TXColor.white, TXColor.sysGuiCol2],
				["ShiftDecorator", 0, -40],
				["NextLine"],
				["TXPopupActionPlusMinus", "display order",
					["build order", "source order", "destination order"],
					"nodeSortOrder", { arg view; this.buildGuiSpecArray; system.showView; }, 270],
				["NextLine"],
			];
		});
		if (displayOption == "showDrawConnections", {
			guiSpecArray = guiSpecArray ++[
				["TXListViewActionPlusMinus", "source", ["select..."] ++ this.getValidTXVDrawSourceOutputNames,
					"drawConnectionSourceIndex",
					{ arg view;
						var destinationIndex, drawConnectionSourceID, drawConnectionDestinationID;
						destinationIndex = this.getSynthArgSpec("drawConnectionDestIndex");
						if ((destinationIndex > 0) && (view.value > 0), {
							drawConnectionSourceID = this.getValidTXVDrawSources[view.value - 1];
							drawConnectionDestinationID = this.getValidTXVDrawDestinations[destinationIndex - 1];
							// testing xxx - following is clumsy, not needed for now
							// //  Check Draw Connections - must not allow self-connection
							// if (drawConnectionSourceID == drawConnectionDestinationID, {
							// 	this.setSynthArgSpec("drawConnectionDestIndex", 0);
							// });
						});
						this.buildGuiSpecArray;
						system.showView;
				}, 300, 120],
				["Spacer", 4],
				["TXListViewActionPlusMinus", "destination", ["select..."] ++ this.getValidTXVDrawDestModuleNames,
					"drawConnectionDestIndex",
					{ arg view;
						var sourceIndex, drawConnectionSourceID, drawConnectionDestinationID;
						sourceIndex = this.getSynthArgSpec("drawConnectionSourceIndex");
						if ((sourceIndex > 0) && (view.value > 0), {
							drawConnectionSourceID = this.getValidTXVDrawSources[sourceIndex - 1];
							drawConnectionDestinationID = this.getValidTXVDrawDestinations[view.value - 1];
							// testing xxx - following is clumsy, not needed for now
							// //  Check Draw Connections - must not allow self-connection
							// if (drawConnectionSourceID == drawConnectionDestinationID, {
							// 	this.setSynthArgSpec("drawConnectionSourceIndex", 0);
							// });
						});
						this.buildGuiSpecArray;
						system.showView;
				}, 300, 120],
				["ShiftDecorator", 0, 44],
				["TXNumberPlusMinus", "layer no.",
					ControlSpec(1, 100, 'lin', step: 1), "drawConnectionLayerNo", nil, nil, nil, 50],
				["ShiftDecorator", 20, 0],
				["ActionButtonBig", "Add Draw Connection", {this.addTXVDrawConnection}, 140,
					TXColor.white, TXColor.sysGuiCol1],
				["ShiftDecorator", 0, -44],
				["SpacerLine", 6],
				["TXListViewActionPlusMinus", "TXV Draw Connections",
					{this.getSortedTXVDrawConnectionNames}, "selectedDrawConnectionIndex",
					{ arg view; /* this.editSelectedDrawConnection(view.value); */ }, 800, 320],
				["ShiftDecorator", -43, 40],
				["ActionButtonBig", "Edit Selected Draw Connection", {this.editSelectedDrawConnection}, 180,
					TXColor.white, TXColor.sysGuiCol2],
				["ShiftDecorator", 0, -40],
				["NextLine"],
				["TXPopupActionPlusMinus", "display order",
					["build order", "source order", "destination order"],
					"drawConnectionSortOrder", { arg view; this.buildGuiSpecArray; system.showView; }, 270],
				["NextLine"],
			];
		});
		if (displayOption == "showImageConnections", {
			guiSpecArray = guiSpecArray ++[
				["TXListViewActionPlusMinus", "TXV Image Connections",
					{this.getTXVImageConnectionNames}, "selectedImageConnectionIndex",
					{ arg view; /* this.editSelectedImageConnection(view.value); */ }, 800, 480],
				["ShiftDecorator", -43, 40],
				["ActionButtonBig", "Edit Selected Module", {this.editSelectedImageConnection}, 140,
					TXColor.white, TXColor.sysGuiCol2],
				["ShiftDecorator", 0, -40],
				["NextLine"],
			];
		});
		if (displayOption == "showAssets", {
			currentAssetType = this.getSynthArgSpec("assetType");
			displayAssetType = ["Image", "Movie", "SVG", "Cube", "3D model", "Font"].at(currentAssetType);
			holdAssetFileName = dictAssetClipboards.at(currentAssetType.asSymbol, 'copyAsset');
			if (holdAssetFileName.notNil and: {holdAssetFileName != ""}, {
				holdPasteAssetColor = TXColor.white;
			}, {
				holdPasteAssetColor = TXColor.grey6;
			});
			holdArrBankFileNames = dictAssetClipboards.at(currentAssetType.asSymbol, 'copyAssetBank');
			if (holdArrBankFileNames.notNil and: {holdArrBankFileNames != ""}, {
				holdPasteAssetBankColor = TXColor.white;
			}, {
				holdPasteAssetBankColor = TXColor.grey6;
			});

			guiSpecArray = guiSpecArray ++[
				// ["TXPopupActionPlusMinus", "add asset type",
				// 	["image", "movie", "svg", "cube", "3DModel", "font"], "assetType",{ arg view;}, 300],
				// 	["Spacer", 3],
				["TextBar", "Asset type", 80],
				// assetType - use index of ["image", "movie", "svg", "cube", "3DModel", "font"]
				["ActionButton", "Image", {this.setSynthArgSpec("assetType", 0);
					this.resetAssetEditParms;
					this.buildGuiSpecArray; system.showView;}, 80,
				TXColor.white, this.getButtonColour(currentAssetType == 0)],
				["ActionButton", "Movie", {this.setSynthArgSpec("assetType", 1);
					this.resetAssetEditParms;
					this.buildGuiSpecArray; system.showView;}, 80,
				TXColor.white, this.getButtonColour(currentAssetType == 1)],
				["ActionButton", "SVG", {this.setSynthArgSpec("assetType", 2);
					this.resetAssetEditParms;
					this.buildGuiSpecArray; system.showView;}, 80,
				TXColor.white, this.getButtonColour(currentAssetType == 2)],
				["ActionButton", "Cube (LUT)", {this.setSynthArgSpec("assetType", 3);
					this.resetAssetEditParms;
					this.buildGuiSpecArray; system.showView;}, 80,
				TXColor.white, this.getButtonColour(currentAssetType == 3)],
				["ActionButton", "3D Model", {this.setSynthArgSpec("assetType", 4);
					this.resetAssetEditParms;
					this.buildGuiSpecArray; system.showView;}, 80,
				TXColor.white, this.getButtonColour(currentAssetType == 4)],
				["ActionButton", "Font", {this.setSynthArgSpec("assetType", 5);
					this.resetAssetEditParms;
					this.buildGuiSpecArray; system.showView;}, 80,
				TXColor.white, this.getButtonColour(currentAssetType == 5)],
				["Spacer", 3],
				["DragSink", "set Asset type & Bank, then drag one or more asset files here to add",
					{ View.currentDrag.isArray},
					{arg v;
						v.object = View.currentDrag; this.processDragAssets(v.object);
				}, false, 408, 28],
				["SpacerLine", 4],
				["TXPopupActionPlusMinus", displayAssetType + "bank",
					arrAssetBankNames[currentAssetType].collect({arg item, i;
						(i + 1).asString ++ ":" + item;}), "selectedAssetBankNo",
					{this.buildGuiSpecArray; system.showView;}, 350, nil, {arg view; holdBankIndexView = view}],
				["Spacer", 3],
				["TXTextBox", displayAssetType ++ " bank name", "assetBankName",
					nil, 300, 130,
					{arg view; holdAssetBankNameView = view;}],
				["ActionButton", "x",
					{holdAssetBankNameView.valueAction_("");},
					20, TXColor.white, TXColor.sysDeleteCol],
				["ActionButton", "Save" + displayAssetType + "bank name",
					{this.saveAssetBankName; this.buildGuiSpecArray; system.showView;},
					180, TXColor.white, TXColor.sysGuiCol1],
				["SpacerLine", 4],
				["TXTextBox", displayAssetType ++ " file", "assetFileName", nil, 500,],
				["ActionButton", "Add " ++ displayAssetType ++ " file", {this.addTXVAsset}, 140,
					TXColor.white, TXColor.sysGuiCol1],
				["NextLine"],
				["TXListViewActionPlusMinus", displayAssetType ++ " assets",
					//[assetType, assetTypeString, assetID, assetFilename, fileExists, assetBankNo]
					arrTXVAssets.select({arg item, i; item[0] == currentAssetType
						and: {(item[5] ? 0) == this.getSynthArgSpec("selectedAssetBankNo")};}
					).collect({arg item, i;
						var err = "  ";
						if (item[4] == 0) {err = "  NOT FOUND: "};
						(i + 1).asString ++ ": " ++ err ++ item[3].asString.keep(-200);
					}),
					"selectedAssetIndex", { arg view;  /* no extra action */}, 920, 380],
				["ShiftDecorator", -48, 60],
				["ActionButton", "Copy selected" + displayAssetType, {this.copySelectedAsset}, 176,
					TXColor.white, TXColor.sysGuiCol1],
				["ShiftDecorator", -180, 32],
				["ActionButton", "Paste" + displayAssetType, {this.pasteAsset}, 176,
					holdPasteAssetColor, TXColor.sysGuiCol1],
				["ShiftDecorator", -180, 32],
				["ActionButton", "Remove selected" + displayAssetType, {this.deleteSelectedAsset}, 176,
					TXColor.white, TXColor.sysDeleteCol],
				["ShiftDecorator", -180, 98],
				["ActionButton", "Copy whole" + displayAssetType + "bank", {this.copyAssetBank}, 176,
					TXColor.white, TXColor.sysGuiCol2, 28],
				["ShiftDecorator", -180, 40],
				["ActionButton", "Paste whole" + displayAssetType + "bank", {this.pasteAssetBank}, 176,
					holdPasteAssetBankColor, TXColor.sysGuiCol2, 28],
				["ShiftDecorator", -180, 40],
				["ActionButton", "Empty whole" + displayAssetType + "bank", {this.deleteAssetBank}, 176,
					TXColor.white, TXColor.sysDeleteCol, 28],
				["NextLine"],
			];
		});

		if (displayOption == "showOSCAddress", {
			guiSpecArray = guiSpecArray ++[
				// ONLY USE 1 ADDRESS FOR NOW
				["TXNetAddress","OSC Address", "i_address1", {this.buildArrNetAddresses;}, 100, 600],
				["SpacerLine", 6],
				["EZNumber", "Port", ControlSpec(0, 99999, 'lin', 1), "i_port1"],
				["ActionButton", "default port", {this.setSynthArgSpec("i_port1", 1999); system.flagGuiUpd;},
					100, TXColor.white, TXColor.sysGuiCol2],
				["Spacer", 20],
				["TXCheckBox", "Activate", "i_activate1", {this.buildArrNetAddresses;}],
				["SpacerLine", 6],
				["TXTextBox","Notes", "i_notes1", nil, 380],

				/* ONLY SHOW 1 ADDRESS
				["TXNetAddress","Address 1", "i_address1", {this.buildArrNetAddresses;}, 380],
				["NextLine"],
				["EZNumber", "Port 1", ControlSpec(0, 99999, 'lin', 1), "i_port1"],
				["ActionButton", "default port", {this.setSynthArgSpec("i_port1", 1999); system.flagGuiUpd;},
					100, TXColor.white, TXColor.sysGuiCol2],
				["Spacer"],
				["TXCheckBox", "Activate", "i_activate1", {this.buildArrNetAddresses;}],
				["NextLine"],
				["TXTextBox","Notes", "i_notes1", nil, 380],
				["DividingLine"],
				["SpacerLine", 2],
				["TXNetAddress","Address 2", "i_address2", {this.buildArrNetAddresses;}, 380],
				["NextLine"],
				["EZNumber", "Port 2", ControlSpec(0, 99999, 'lin', 1), "i_port2"],
				["ActionButton", "default port", {this.setSynthArgSpec("i_port2", 1999); system.flagGuiUpd;},
					100, TXColor.white, TXColor.sysGuiCol2],
				["Spacer"],
				["TXCheckBox", "Activate", "i_activate2", {this.buildArrNetAddresses;}],
				["NextLine"],
				["TXTextBox","Notes", "i_notes2", nil, 380],
				["DividingLine"],
				["SpacerLine", 2],
				["TXNetAddress","Address 3", "i_address3", {this.buildArrNetAddresses;}, 380],
				["NextLine"],
				["EZNumber", "Port 3", ControlSpec(0, 99999, 'lin', 1), "i_port3"],
				["ActionButton", "default port", {this.setSynthArgSpec("i_port3", 1999); system.flagGuiUpd;},
					100, TXColor.white, TXColor.sysGuiCol2],
				["Spacer"],
				["TXCheckBox", "Activate", "i_activate3", {this.buildArrNetAddresses;}],
				["NextLine"],
				["TXTextBox","Notes", "i_notes3", nil, 380],
				["DividingLine"],
				["SpacerLine", 2],
				["TXNetAddress","Address 4", "i_address4", {this.buildArrNetAddresses;}, 380],
				["NextLine"],
				["EZNumber", "Port 4", ControlSpec(0, 99999, 'lin', 1), "i_port4"],
				["ActionButton", "default port", {this.setSynthArgSpec("i_port4", 1999); system.flagGuiUpd;},
					100, TXColor.white, TXColor.sysGuiCol2],
				["Spacer"],
				["TXCheckBox", "Activate", "i_activate4", {this.buildArrNetAddresses;}],
				["NextLine"],
				["TXTextBox","Notes", "i_notes4", nil, 380],
				["DividingLine"],
				*/
			];
		});
	}   // end of buildGuiSpecArray

	getButtonColour { arg colour2Boolean;
		if (colour2Boolean == true, {
			^TXColor.sysGuiCol4;
		},{
			^TXColor.sysGuiCol1;
		});
	}

	updateRunningStatus { arg argStatus;
		runningStatus = argStatus;
		{
			if (runningStatusView.notNil, {
				if (runningStatusView.notClosed, {
					runningStatusView.string = runningStatus;
				});
			});
		}.defer;
	}

	registerOscReply { arg replyString;
		// add replyString to array
		// arrOscRepliesReceived = arrOscRepliesReceived.asArray.add(replyString ++ "\n").keep(-38);
		arrOscRepliesReceived = arrOscRepliesReceived.asArray.add(replyString ++ "\n");
		{
			if (txvMessagesView.notNil, {
				if (txvMessagesView.notClosed, {
					txvMessagesView.string = this.getTxvMessages;
				});
			});
		}.defer;
	}

	getTxvMessages {
		var outString = "".catList(arrOscRepliesReceived);
		^outString;
	}

	clearTxvMessages {
		arrOscRepliesReceived = [];
	}

	////////////////////////////////////////////////

	oscActivateExtraActions {
		// listen to txv replies
		txvOSCReplyResp = OSCFunc.newMatching(
			{|msg, time, addr, recvPort| this.txvOSCReplyProcess(msg, time);},
			'/TX_Modular_System/OSCReply'
		);

	}

	oscDeactivateExtraActions {
		//	remove responders
		//		oscControlResp.remove;
		txvOSCReplyResp.free;
	}

	////////////////////////////////////////////////

	getModuleWithTXVID {arg argTXVID;
		if (argTXVID == this.txvModuleID, {
			^this;
		});
		arrTXVModules.do({arg item, i;
			if (item.txvModuleID == argTXVID, {
				^item;
			});
		});
		^nil;
	}
	getModuleOrNodeWithTXVID {arg argTXVID;
		if (argTXVID == this.txvModuleID, {
			^this;
		});
		(arrTXVModules ++ arrTXVNodes).do({arg item, i;
			if (item.txvModuleID == argTXVID, {
				^item;
			});
		});
		^nil;
	}
	getNodeWithTXVID {arg argTXVID;
		arrTXVNodes.do({arg item, i;
			if (item.txvModuleID == argTXVID, {
				^item;
			});
		});
		^nil;
	}
	getDrawConnectionWithTXVID {arg argTXVID;
		arrTXVDrawConnections.do({arg item, i;
			if (item.txvModuleID == argTXVID, {
				^item;
			});
		});
		^nil;
	}

	getNodeSourceDestName {arg argTXVID;
		var holdModule;
		// check in all arrays
		holdModule = this.getModuleWithTXVID(argTXVID);
		if (holdModule.notNil, {
			^holdModule.txvModuleName;
		});
		holdModule = this.getNodeWithTXVID(argTXVID);
		if (holdModule.notNil, {
			^holdModule.txvModuleName;
		});
		holdModule = this.getDrawConnectionWithTXVID(argTXVID);
		if (holdModule.notNil, {
			^holdModule.txvModuleName;
		});
		("ERROR: TXV_System.getNodeSourceDestName - TXVID not found: " ++ argTXVID.asString).postln;
		^"";
	}

	getDrawConnSourceDestName {arg argTXVID;
		var holdModule;
		// system can be draw conn dest
		if (argTXVID == this.txvModuleID, {
			^this.txvModuleName;
		});
		holdModule = this.getModuleWithTXVID(argTXVID);
		if (holdModule.notNil, {
			^holdModule.txvModuleName;
		});
		("ERROR: TXV_System.getDrawConnSourceDestName - TXVID not found: " ++ argTXVID.asString).postln;
		^"";
	}

	////////////////////////////////////////////////

	registerChildModule {arg argModule;
		// add to relevant array
		if (argModule.class == TXV_Node, {
			if (arrTXVNodes.indexOf(argModule).isNil, {
				arrTXVNodes = arrTXVNodes.add(argModule);
			});
		}, {
			if (argModule.class == TXV_DrawConnection, {
				if (arrTXVDrawConnections.indexOf(argModule).isNil, {
					arrTXVDrawConnections = arrTXVDrawConnections.add(argModule);
				});
			}, {
				if (arrTXVModules.indexOf(argModule).isNil, {
					arrTXVModules = arrTXVModules.add(argModule);
				});
			});
		});
		this.rebuildGuiSpecArrays;
	}

	deregisterChildModule {arg argModule;
		/* OLD CODE
		var holdID = argModule.txvModuleID;
		var arrNodesToBeDeleted, arrDrawConnectionsToBeDeleted;

		// Delete any nodes that use this
		//if (argModule.class != TXV_Node, {
			arrNodesToBeDeleted = [];
			arrTXVNodes.do({arg item, i;
				if ((item.sourceTXVID == holdID) || (item.destinationTXVID == holdID), {
					arrNodesToBeDeleted = arrNodesToBeDeleted.add(item);
				});
			});
			arrNodesToBeDeleted.do({arg item, i;
				arrTXVNodes.remove(item);
				item.deleteModule;
			});
		//});
		// Delete any draw connections that use this
		if ((argModule.class != TXV_Node) && (argModule.class != TXV_DrawConnection), {
			arrDrawConnectionsToBeDeleted = [];
			arrTXVDrawConnections.do({arg item, i;
				if ((item.sourceTXVID == holdID) || (item.destinationTXVID == holdID), {
					arrDrawConnectionsToBeDeleted = arrDrawConnectionsToBeDeleted.add(item);
				});
			});
			arrDrawConnectionsToBeDeleted.do({arg item, i;
				arrTXVDrawConnections.remove(item);
				item.deleteModule;
			});
		});
		*/

		// send TXV message to delete child,  remove from relevant array
		if (argModule.class == TXV_Node, {
			this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/deleteNode", [argModule.txvModuleID]);
			arrTXVNodes.remove(argModule);
		}, {
			if (argModule.class == TXV_DrawConnection, {
				this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/deleteDrawConnection", [argModule.txvModuleID]);
				arrTXVDrawConnections.remove(argModule);
			}, {
				this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/deleteModule", [argModule.txvModuleID]);
				arrTXVModules.remove(argModule);
			});
		});
		this.rebuildGuiSpecArrays;
	}

	deleteTXVModule{arg argModule;
		var holdID = argModule.txvModuleID;
		var arrNodesToBeDeleted, arrDrawConnectionsToBeDeleted;
		var recursiveNodeFunc;
		// Delete any nodes that use this
		arrNodesToBeDeleted = [];
		// recursive func
		recursiveNodeFunc = {arg argID, argDeleteArray;
			var outArray = argDeleteArray.copy;
			arrTXVNodes.do({arg item, i;
				if (item.markedForDeletion.not && (item.destinationTXVID == argID), {
					item.markedForDeletion = true;
					outArray = outArray.add(item);
					outArray = recursiveNodeFunc.value(item.txvModuleID, outArray);
				});
			});
			outArray; // return array
		};
		// if a node, recursively delete modulating nodes
		if (argModule.class == TXV_Node, {
			arrNodesToBeDeleted = recursiveNodeFunc.value(holdID, arrNodesToBeDeleted);
			// reverse order so modulating nodes are deleted before modulated nodes
			arrNodesToBeDeleted = arrNodesToBeDeleted.reverse;
		},
		{ // if not node
			if (argModule.class != TXV_DrawConnection, {
				arrTXVNodes.do({arg item, i;
					if ((item.sourceTXVID == holdID) || (item.destinationTXVID == holdID), {
						arrNodesToBeDeleted = arrNodesToBeDeleted.add(item);
					});
				});
			});
		});
		arrNodesToBeDeleted.do({arg item, i;
			item.finalDelete;
		});
		// Delete any draw connections that use this
		if ((argModule.class != TXV_Node) && (argModule.class != TXV_DrawConnection), {
			arrDrawConnectionsToBeDeleted = [];
			arrTXVDrawConnections.do({arg item, i;
				if ((item.sourceTXVID == holdID) || (item.destinationTXVID == holdID), {
					arrDrawConnectionsToBeDeleted = arrDrawConnectionsToBeDeleted.add(item);
				});
			});
			arrDrawConnectionsToBeDeleted.do({arg item, i;
				item.finalDelete;
			});
		});
		// delete module itself
		argModule.finalDelete;
	}

	rebuildGuiSpecArrays {
		this.buildGuiSpecArray;
		arrTXVNodes.do({arg item, i;
			item.buildGuiSpecArray;
		});
		arrTXVDrawConnections.do({arg item, i;
			item.buildGuiSpecArray;
		});
	}

	////////////////////////////////////////////////

	// Respond to these replies from TXV by creating TXV modules:
	// msg: [ /TX_Modular_System/OSCReply, 1, 900, /TX_Modular_System/TXSystem_1/ping, Ping Back ]
	// msg: [ /TX_Modular_System/OSCReply, 2, 901, /TX_Modular_System/TXSystem_1/systemData, Confirmation: System Data, 1, TXSystem, TXSystem_1, 1, 1, 0, 0 ]
	// msg: [ /TX_Modular_System/OSCReply, 3, 0, /TX_Modular_System/TXSystem_1/addModule, Confirmation: Module Added, 2, TXLFO, TXLFO_1, 0, 0, 8, 1 ]
	// msg: [ /TX_Modular_System/OSCReply, 6, 3, /TX_Modular_System/TXSystem_1/addNode, Confirmation: Node Added, 5, TXModNode, TXModNode_1, 0, 0, 2, 0 ]
	// msg: [ /TX_Modular_System/OSCReply, 8, 5, /TX_Modular_System/TXSystem_1/addDrawConnection, Confirmation: Draw Connection Added, 7, TXDrawConnection, TXDrawConnection_1, 0, 0, 0, 0 ]

	txvOSCReplyProcess { arg msg, time;
		var holdTXVID, newClass, newClassString, newModule;
		// set parentTXVSysModuleID & moduleID & moduleName when system/module created
		var replyToMsgSeqNo = msg[2];
		var reply = msg[4].asString;
		var replyString = reply;
		var originalMsg = dictOscCommandsSent[replyToMsgSeqNo];

		// // testing XXX
		// "__TXV_System.txvOSCReplyProcess received:  ".post;
		// [time, msg].postcs;
		// ["txvOSCReplyProcess - replyToMsgSeqNo.class", replyToMsgSeqNo.class].postln;

		// remove originalMsg once replied to
		dictOscCommandsSent[replyToMsgSeqNo] = nil;
		// only process if valid Reply
		if (originalMsg.notNil, {
			{ // fork
				case
				{reply.keep(5) == "Error"} {
					"Error: TXV System replied with error message: ---".postln;
					msg.postln;
					//this.updateRunningStatus(" TXV Error: " ++ msg);
				}
				{reply == "Sync Start Received"} {
					//"TXV Sync Start Received".postln;
				}
				{reply == "Sync Stop Received"} {
					//"TXV Sync Stop Received".postln;
				}
				{reply == "Sync Received"} {
					//"TXV Sync Received".postln;
				}
				{reply == "Ping Back"} {
					//"TXV Ping Back".postln;
				}
				{reply == "Confirmation: TXV has been stopped and closed."} {
					this.updateRunningStatus(" Not connected");
					txvSystemFound = false;
				}
				{reply == "Confirmation: System Data"} {
					txvSystemFound = true;
					parentTXVSysModuleID = this.moduleID;
					parentTXVSystem = this;
					txvModuleID = msg[5];
					txvModuleName = msg[7];
					// canDraw = msg[8]; // ignore
					// canDrawLayers = msg[9]; // ignore
					numModInputs = msg[10];
					// numModOutputs = msg[11]; // ignore
					this.setFullScreen(false);
					this.sendCurrentValues;
					this.buildGuiSpecArray;
					this.updateScreen;
					this.updateRunningStatus(" TXV System connected");
				}
				{reply == "Confirmation: Add Asset"} {
					var assetType, assetTypeString, assetID, assetFilename, fileExists, assetBankNo, holdIndex;
					// Reply msg format:
					//   ["/TX_Modular_System/OSCReply", int msgSeqNo, int replyToMsgSeqNo, string sendAddress,
					//       string reply, int assetType, string  assetTypeString, int assetID, string fileName,
					//       bool fileExists, int assetBankNo
					//       ]
					assetType = msg[5];
					assetTypeString = msg[6];
					assetID = msg[7];
					assetFilename = msg[8];
					fileExists = msg[9];
					assetBankNo = msg[10];
					holdIndex = -1;
					arrTXVAssets.do({arg item, i;
						if ((item[0] == assetType) && (item[2] == assetID), {holdIndex = i;});
					});
					// if asset found then update it
					if (holdIndex != -1, {
						arrTXVAssets[holdIndex][4] = fileExists;
					}, {
						// else add to array and sort
						arrTXVAssets = arrTXVAssets.add([assetType, assetTypeString, assetID, assetFilename,
							fileExists, assetBankNo]);
						//arrTXVAssets.sort({arg a, b; (a[0] <= b[0]) && (a[2] <= b[2]);});
						arrTXVAssets.sort({arg a, b; ((a[0] * 10000) + a[2]) <= ((b[0] * 10000) + b[2]);});
					});
					this.buildGuiSpecArray;
					this.updateScreen;
				}
				{reply == "Confirmation: Module Added"} {
					// Reply msg format when module/node/drawConnection added:
					//   ["/TX_Modular_System/OSCReply", int msgSeqNo, int replyToMsgSeqNo, string sendAddress,
					//      string reply, int moduleID, string moduleType, string moduleName,
					//      int canDraw, int canDrawLayers, int numModInputs, int numModOutputs]
					//
					holdTXVID = msg[5];
					// make sure returned ID isn't systemID
					if (holdTXVID == this.txvModuleID, {
						("ERROR: TXV_System.txvOSCReplyProcess - 'Confirmation: Module Added' id is same as TXV_System ID: "
							++ holdTXVID.asString).postln;
					}, {
						// ok to use
						newModule = this.getModuleWithTXVID(holdTXVID);
						if (newModule.isNil, {
							// if it doesn't exist, add it
							newClassString  = msg[6].asString.insert(2,"V_");
							newClass = newClassString.asSymbol.asClass;
							newModule = system.addModule(newClass);
						});
						newModule.parentTXVSysModuleID = this.moduleID;
						newModule.parentTXVSystem = this;
						newModule.txvModuleID = holdTXVID;
						newModule.txvModuleType = msg[6].asString;
						newModule.txvModuleName = msg[7].asString;
						newModule.canDraw = msg[8];
						newModule.canDrawLayers = msg[9];
						newModule.numModInputs = msg[10];
						newModule.numModOutputs = msg[11];
						newModule.hasTexture = msg[12];
						this.registerChildModule(newModule);
						newModule.buildGuiSpecArray;
						newModule.sendCurrentValues;
						this.buildGuiSpecArray;
						this.updateScreen;
						replyString = replyString ++ " " ++ newModule.txvModuleName;
					});
				}
				{reply == "Confirmation: Node Added"} {
					holdTXVID = msg[5];
					newModule = this.getNodeWithTXVID(holdTXVID);
					if (newModule.isNil, {
						// if it doesn't exist, add it
						newModule = system.addModule(TXV_Node);
					});
					newModule.parentTXVSysModuleID = this.moduleID;
					newModule.parentTXVSystem = this;
					newModule.txvModuleID = holdTXVID;
					newModule.txvModuleType = msg[6].asString;
					newModule.txvModuleName = msg[7].asString;
					newModule.canDraw = msg[8];
					newModule.canDrawLayers = msg[9];
					newModule.numModInputs = msg[10];
					newModule.numModOutputs = msg[11];
					newModule.sourceTXVID = originalMsg[6];
					newModule.sourceModuleName = this.getNodeSourceDestName(newModule.sourceTXVID);
					newModule.sourceOutputName = originalMsg[7];
					newModule.destinationTXVID = originalMsg[8];
					newModule.destinationModuleName = this.getNodeSourceDestName(newModule.destinationTXVID);
					newModule.destinationParamName = originalMsg[9];
					this.registerChildModule(newModule);
					newModule.sendCurrentValues;
					this.buildGuiSpecArray;
					this.updateScreen;
					replyString = replyString ++ " " ++ newModule.txvModuleName;
				}
				{reply == "Confirmation: Draw Connection Added"} {
					holdTXVID = msg[5];
					newModule = this.getDrawConnectionWithTXVID(holdTXVID);
					if (newModule.isNil, {
						// if it doesn't exist, add it
						newModule = system.addModule(TXV_DrawConnection);
					});
					newModule.parentTXVSysModuleID = this.moduleID;
					newModule.parentTXVSystem = this;
					newModule.txvModuleID = holdTXVID;
					newModule.txvModuleType = msg[6].asString;
					newModule.txvModuleName = msg[7].asString;
					newModule.canDraw = msg[8];
					newModule.canDrawLayers = msg[9];
					newModule.numModInputs = msg[10];
					newModule.numModOutputs = msg[11];
					newModule.sourceTXVID = originalMsg[6];
					newModule.sourceModuleName = this.getDrawConnSourceDestName(newModule.sourceTXVID);
					newModule.destinationTXVID = originalMsg[7];
					newModule.destinationModuleName = this.getDrawConnSourceDestName(newModule.destinationTXVID);
					newModule.dictCurrentParameterVals["/TXDrawConnection_1/layerNo".asSymbol] = originalMsg[8];
					this.registerChildModule(newModule);
					newModule.sendCurrentValues;
					this.buildGuiSpecArray;
					this.updateScreen;
					replyString = replyString ++ " " ++ newModule.txvModuleName;
				}
				{reply == "Confirmation: Deleted All Modules"} {
				}
				; // end of case
				this.registerOscReply(replyString);
				if (reply.keep(5) != "Sync", {
					// after processing reply send next command
					this.allowNextCommandInTXVQueue;
				});
				0.01.wait; // wait to allow other tasks
			}.fork;
		}, {
			// else show error
			("ERROR: TXV_System.txvOSCReplyProcess - recieved reply to invalid command message no." ++ replyToMsgSeqNo.asString ).postln;
			msg.postln;
		}); // end of if valid Reply
	}

	////////////////////////////

	sendMsgToTXV { arg arrArgs;
		var bundleArr, holdTimestamp;
		var holdCommand = arrArgs[0].split.last;
		// only send if link established or systemData request
		if (txvSystemFound or: (holdCommand == "sync") or: (holdCommand == "systemData"), {
			// get latency
			holdTimestamp = this.getSynthArgSpec("latency");
			bundleArr = [holdTimestamp] ++ [arrArgs];
			// Format:[address, txvSystemCode, msgSeqNo, ...]
			//  e.g. ("/TX_Modular_System/TXSystem_1/setModuleParameter", 1000000, 10, 2, "/TXLFO_1/bpm/fixedValue", 26.0);
			arrNetAddresses.do({arg item, i;
				//item.sendMsg(*arrArgs);
				item.sendBundle(*bundleArr); //  as bundle
			});

			// // testing xxx
			// "___testing xxx - TXV_System.sendMsgToTXV - msg:  ".post;
			// arrArgs.postln;
		});
	}

	sendParameterToTXV{ arg argtxvModuleID, argParameterAddress, argValue;
		// Format: [sting commandAddress, int systemCode, int msgSeqNo, float msgTime, string systemData1, string systemData2,  ...dataArgs]
		// Format:["/TX_Modular_System/TXSystem_1/setModuleParameter", int systemCode, int msgSeqNo, float msgTime, string systemData1, string systemData2, txvModuleID, address, value]
		var holdAddress, holdParmAddress, arrNewArgs;
		var parmAddrSplit = argParameterAddress.asString.split;
		var holdVal;
		// check if asset
		if (parmAddrSplit[0] == "assetSlot", {
			holdAddress = "/TX_Modular_System/TXSystem_1/setModuleAsset";
		}, {
			if (parmAddrSplit[0] == "extImageModuleSlot", {
				holdAddress = "/TX_Modular_System/TXSystem_1/setModuleExtImageModule";
			}, {
				if (argValue.asArray.size == 256, {
					holdAddress = "/TX_Modular_System/TXSystem_1/setModuleParameterFloat256";
				}, {
					holdAddress = "/TX_Modular_System/TXSystem_1/setModuleParameter";
				});
			});
		});
		if (argValue.isString, {holdVal = [argValue]}, {holdVal = argValue.asArray});

		arrNewArgs = [holdAddress, txvSystemCode, this.getNextOscMsgNo, Process.elapsedTime,
			txvSystemData1, txvSystemData2, argtxvModuleID, argParameterAddress] ++ holdVal;
		this.sendMsgToTXV(arrNewArgs);

		// // testing xxx
		// "___testing xxx - TXV_System.sendParameterToTXV - msg args: ".post;
		// arrNewArgs.postln;
	}

	addCommandToTXVQueue{ arg argCommandAddress, arrOtherArgs;
		var holdCommand;
		txvCommandQueue.addFirst([argCommandAddress, arrOtherArgs]);
		// if 1st command, create routine
		if (txvCommandQueue.size == 1, {
			txvCommandQueueCondition = Condition.new;
			fork {while ( {txvCommandQueue.size > 0}, {
				// send command whenever condition allows
				holdCommand = txvCommandQueue.pop;
				if (holdCommand[0] == "runAFunction", {
					// allows a function to be run in the queue
					holdCommand[1].value;
				}, {
					// else send to TXV & wait
					this.sendCommandToTXV(holdCommand[0], holdCommand[1]);
					txvCommandQueueCondition.test = false;
					txvCommandQueueCondition.wait;
				});
			}
			)};
		});
	}

	allowNextCommandInTXVQueue {
		txvCommandQueueCondition.test = true;
		txvCommandQueueCondition.signal;
	}

	emptyTXVQueue {
		txvCommandQueue = List[];
		txvCommandQueueCondition.test = true;
		txvCommandQueueCondition.signal;
	}

	sendCommandToTXV{ arg argCommandAddress, arrOtherArgs;
		// Format: [sting commandAddress, int systemCode, int msgSeqNo, float msgTime, string systemData1, string systemData2,  ...dataArgs]
		// examples:
		// b.sendMsg("/TX_Modular_System/TXSystem_1/ping", 1000000, 900, Process.elapsedTime, "", "");
		// b.sendMsg("/TX_Modular_System/TXSystem_1/systemData", 1000000, 901, Process.elapsedTime, "", "");
		// b.sendMsg("/TX_Modular_System/TXSystem_1/freezeSystem", 1000000, 902, Process.elapsedTime, "", "");
		// b.sendMsg("/TX_Modular_System/TXSystem_1/unfreezeSystem", 1000000, 903, Process.elapsedTime, "", "");
		// b.sendMsg("/TX_Modular_System/TXSystem_1/deleteAllModules", 1000000, 904, Process.elapsedTime, "", "");
		// b.sendMsg("/TX_Modular_System/TXSystem_1/resetAllTime", 1000000, 905, Process.elapsedTime, "", "");
		// b.sendMsg("/TX_Modular_System/TXSystem_1/addModule", 1000000, 0, Process.elapsedTime, "", "", "TXLFO");
		// b.sendMsg("/TX_Modular_System/TXSystem_1/addNode", 1000000, 3, Process.elapsedTime, "", "", 2, "out", 4, "positionX", 1.0, 0, 1);
		// b.sendMsg("/TX_Modular_System/TXSystem_1/addDrawConnection", 1000000, 5, Process.elapsedTime, "", "", 4, 1, 1);

		var arrNewArgs, holdMsgNo;
		holdMsgNo = this.getNextOscMsgNo;
		if (arrOtherArgs.isNil, {
			arrOtherArgs = [];
		});
		arrNewArgs = [argCommandAddress, txvSystemCode, holdMsgNo, Process.elapsedTime,
			txvSystemData1, txvSystemData2, ] ++ arrOtherArgs;
		// store command and send
		dictOscCommandsSent[holdMsgNo] = arrNewArgs;
		this.sendMsgToTXV(arrNewArgs);

		// // testing xxx
		// "testing xxx - sendCommandToTXV: ".post;
		// arrNewArgs.postln;
	}

	connectToTXVSystem {
		this.emptyTXVQueue;
		this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/systemData", [NetAddr.langPort]);
		this.updateRunningStatus(" TXV connection - waiting for reply");
		this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/syncStart");
		10.do({
			this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/sync");
		});
		this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/syncStop");
	}

	resetTXVSystem {
		this.emptyTXVQueue;
		//  deleteAllModules & reset time
		this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/deleteAllModules");
		this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/resetAllTime");
		this.resetArrAssetBankNames;
	}

	startTXVSystem {
		if (txvAppFound, {
			("Starting TXV.app at path: " ++ txvAppPath).postln;
			// unix command to open TXV
			("open " ++ txvAppPath).unixCmd({
				this.updateRunningStatus(" TXV started");
				{
					1.wait;
					this.connectToTXVSystem;
				}.fork;
			});
		});
	}

	killTXVSystem {
		if (txvSystemFound, {
			this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/killTXV");
		});
	}

	rebuildTXVSystem {
		this.connectToTXVSystem;
		//  freeze & reset TXV
		systemData.rebuildingStatus = true;
		this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/freezeSystem");
		this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/deleteAllModules");
		this.addCommandToTXVQueue("runAFunction", {
			this.updateRunningStatus(" TXV System rebuild started...");
			system.showView;
		});
		this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/setIgnoreOscLatency", [1]);
		// this.updateRunningStatus(" TXV System rebuild started...");
		this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/resetAllTime");
		// rebuild in order all assets, modules, nodes, draw connections
		arrTXVAssets.do({arg item, i;
			// asset: [assetType, assetTypeString, assetID, assetFilename, fileExists, assetBankNo]
			this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/addAsset",
				[item[0], item[3], item[2], item[5]]);
		});
		arrTXVModules.do({arg item, i;
			this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/addModule", [item.txvModuleType, item.txvModuleID]);
		});
		arrTXVNodes.do({arg item, i;
			this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/addNode",
				[item.sourceTXVID, item.sourceOutputName, item.destinationTXVID, item.destinationParamName,
					1.0, 0, 1, item.txvModuleID]);
		});
		arrTXVDrawConnections.do({arg item, i;
			var drawConnectionLayerNo;
			drawConnectionLayerNo = item.dictCurrentParameterVals['/TXDrawConnection_1/layerNo'];
			this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/addDrawConnection",
				[item.sourceTXVID, item.destinationTXVID, drawConnectionLayerNo, item.txvModuleID]);
		});

		// unfreeze
		this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/unfreezeSystem");
		//this.sendCurrentValues;
		this.addCommandToTXVQueue("runAFunction", {this.finishRebuilding;});
		this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/setIgnoreOscLatency", [0]);
		system.showView;
	}

	finishRebuilding {
		{   // deferred
			systemData.rebuildingStatus = false;
			this.updateRunningStatus(" TXV System rebuild completed");
			this.buildGuiSpecArray;
			system.showView;
		}.defer(0.5);
	}

	updateScreen {
		if (systemData.rebuildingStatus == false, {
			system.showView;
		});
	}
	////////////////////////////

	addTXVModule {arg newModuleTXVID = -1;
		var typeIndex = displayModulesGpIndices[this.getSynthArgSpec("addModuleTypeIndex")];
		if (txvSystemFound, {
			this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/addModule",
				[systemData.validTXVModuleTypes[typeIndex], newModuleTXVID]);
		}, {
			TXInfoScreen.new(
				"Warning: you need to connect to the TXV app before adding a new module",
				winColour: TXColor.orange
			);
		});
	}

	addTXVNode {arg newModuleTXVID = -1;
		var nodeSourceModuleIndex = this.getSynthArgSpec("nodeSourceModuleIndex");
		var nodeSourceOutputNameIndex = this.getSynthArgSpec("nodeSourceOutputNameIndex");
		var nodeDestModuleIndex = this.getSynthArgSpec("nodeDestModuleIndex");
		var nodeDestParamNameIndex = this.getSynthArgSpec("nodeDestParamNameIndex");
		if (txvSystemFound, {
			if ((nodeSourceModuleIndex > 0) && (nodeSourceOutputNameIndex > 0)
				&& (nodeDestModuleIndex > 0) && (nodeDestParamNameIndex > 0), {
					var sourceModule = this.getValidTXVNodeSourceModules[nodeSourceModuleIndex - 1];
					var sourceTXVID = sourceModule.txvModuleID;
					var sourceModuleName = this.getValidTXVNodeSourceOutputNames[nodeSourceOutputNameIndex - 1];
					var destinationModule = this.getValidTXVNodeDestModules[nodeDestModuleIndex - 1];
					var destinationTXVID = destinationModule.txvModuleID;
					var destinationModuleName = this.getValidTXVNodeDestParamNames[nodeDestParamNameIndex - 1];
					this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/addNode",
						[sourceTXVID, sourceModuleName, destinationTXVID, destinationModuleName, 1.0, 0, 1, newModuleTXVID]);
			});
		}, {
			TXInfoScreen.new(
				"Warning: you need to connect to the TXV app before adding a new Node",
				winColour: TXColor.orange
			);
		});
	}

	addTXVDrawConnection {arg newModuleTXVID = -1;
		var sourceIndex = this.getSynthArgSpec("drawConnectionSourceIndex");
		var destinationIndex = this.getSynthArgSpec("drawConnectionDestIndex");
		if (txvSystemFound, {
			if ((sourceIndex > 0) && (destinationIndex > 0), {
				var drawConnectionLayerNo = this.getSynthArgSpec("drawConnectionLayerNo").asInteger;
				var source = this.getValidTXVDrawSources[sourceIndex - 1];
				var sourceTXVID = source.txvModuleID;
				var destination = this.getValidTXVDrawDestinations[destinationIndex - 1];
				var destinationTXVID = destination.txvModuleID;
				this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/addDrawConnection",
					[sourceTXVID, destinationTXVID, drawConnectionLayerNo, newModuleTXVID]);
			});
		}, {
			TXInfoScreen.new(
				"Warning: you need to connect to the TXV app before adding a new Draw Connection",
				winColour: TXColor.orange
			);
		});
	}

	////////////////////////////

	addTXVAsset {arg newAssetID = -1, newAssetBankNo, newFileName;
		var assetBankNo = newAssetBankNo ? this.getSynthArgSpec("selectedAssetBankNo");
		var assetType = this.getSynthArgSpec("assetType");
		var assetFileName = newFileName ? this.getSynthArgSpec("assetFileName");
		if (txvSystemFound, {
			this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/addAsset",
				[assetType, assetFileName, newAssetID, assetBankNo]);
			// clear string
			this.setSynthArgSpec("assetFileName", "");
			system.showView;
		}, {
			TXInfoScreen.new(
				"Warning: you need to connect to the TXV app before adding assets",
				winColour: TXColor.orange
			);
		});
	}

	processDragAssets { arg argInput;
		var holdArray;
		var assetType = this.getSynthArgSpec("assetType");
		var assetBankNo = this.getSynthArgSpec("selectedAssetBankNo");
		if (txvSystemFound, {
			if (argInput.isString, {
				holdArray = [argInput];
			}, {
				holdArray = argInput;
			});
			holdArray.do({arg item, i;
				this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/addAsset",
					[assetType, item, -1, assetBankNo]);
			});
		}, {
			TXInfoScreen.new(
				"Warning: you need to connect to the TXV app before adding assets",
				winColour: TXColor.orange
			);
		});
	}

	copySelectedAsset{
		var assetBankNo = this.getSynthArgSpec("selectedAssetBankNo");
		var assetType = this.getSynthArgSpec("assetType");
		var localIndex = this.getSynthArgSpec("selectedAssetIndex");
		var asset, assetFileName;
		// asset: [assetType, assetTypeString, assetID, assetFilename, fileExists, assetBankNo]
		asset = arrTXVAssets.select({arg item, i;
			item[0] == assetType;
		}).at(localIndex);
		assetFileName = asset[3];
		dictAssetClipboards.put(assetType.asSymbol, 'copyAsset', assetFileName);
		this.buildGuiSpecArray;
		system.showView;
	}

	pasteAsset {
		var assetType = this.getSynthArgSpec("assetType");
		var assetBankNo = this.getSynthArgSpec("selectedAssetBankNo");
		var assetFileName = dictAssetClipboards.at(assetType.asSymbol, 'copyAsset');
		if (txvSystemFound, {
			if (assetFileName.notNil and: {assetFileName != ""}, {
				this.addTXVAsset(-1, assetBankNo, assetFileName);
			});
		}, {
			TXInfoScreen.new(
				"Warning: you need to connect to the TXV app before adding assets",
				winColour: TXColor.orange
			);
		});
	}

	deleteSelectedAsset {
		var localIndex, assetIndex, asset, assetType, assetID;
		if (txvSystemFound, {
			localIndex = this.getSynthArgSpec("selectedAssetIndex");
			// asset: [assetType, assetTypeString, assetID, assetFilename, fileExists, assetBankNo]
			asset = arrTXVAssets.select({arg item, i; item[0] == this.getSynthArgSpec("assetType");}).at(localIndex);
			if (asset.notNil, {
				assetType = asset[0];
				assetID = asset[2];
				this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/deleteAsset",
					[assetType, assetID,]);
				assetIndex = arrTXVAssets.indexOf(asset);
				arrTXVAssets.removeAt(assetIndex);
				this.buildGuiSpecArray;
				system.showView;
			});
		}, {
			TXInfoScreen.new(
				"Warning: you need to connect to the TXV app before deleting assets",
				winColour: TXColor.orange
			);
		});
	}

	copyAssetBank {
		var assetBankNo = this.getSynthArgSpec("selectedAssetBankNo");
		var assetType = this.getSynthArgSpec("assetType");
		var arrBankFileNames = [];
		arrTXVAssets.do({arg item, i;
			if (item[0] == assetType and: {item[5] == assetBankNo}, {
				arrBankFileNames = arrBankFileNames.add(item[3]);
			});
		});
		// asset: [assetType, assetTypeString, assetID, assetFilename, fileExists, assetBankNo]
		dictAssetClipboards.put(assetType.asSymbol, 'copyAssetBank', arrBankFileNames);
		this.buildGuiSpecArray;
		system.showView;
	}

	pasteAssetBank {
		var assetType = this.getSynthArgSpec("assetType");
		var assetBankNo = this.getSynthArgSpec("selectedAssetBankNo");
		var holdArrBankFileNames = dictAssetClipboards.at(assetType.asSymbol, 'copyAssetBank');
		if (txvSystemFound, {
			if (holdArrBankFileNames.notNil and: {holdArrBankFileNames.isArray}, {
				holdArrBankFileNames.do({arg assetFileName;
					this.addTXVAsset(-1, assetBankNo, assetFileName);
				});
			});
		});
	}

	deleteAssetBank {
		var assetBankNo = this.getSynthArgSpec("selectedAssetBankNo");
		var assetType = this.getSynthArgSpec("assetType");
		var selectedAssets;
		if (txvSystemFound, {
			selectedAssets = arrTXVAssets.select({arg item, i;
				item[0] == assetType and: {item[5] == assetBankNo};
			});
			selectedAssets.do({arg asset;
				// asset: [assetType, assetTypeString, assetID, assetFilename, fileExists, assetBankNo]
				var assetID = asset[2];
				var assetIndex = arrTXVAssets.indexOf(asset);
				this.addCommandToTXVQueue("/TX_Modular_System/TXSystem_1/deleteAsset",
					[assetType, assetID,]);
				arrTXVAssets.removeAt(assetIndex);
			});
			this.buildGuiSpecArray;
			system.showView;
		});
	}

	saveAssetBankName {
		var name = holdAssetBankNameView.string;
		var assetType = this.getSynthArgSpec("assetType");
		var assetBankNo = this.getSynthArgSpec("selectedAssetBankNo");
		arrAssetBankNames[currentAssetType][assetBankNo] = name;
	}

	resetArrAssetBankNames {
		arrAssetBankNames = ("" ! 30) ! 6;
	}

	////////////////////////////

	editSelectedModule {arg argSelection;
		var holdIndex = argSelection ? (this.getSynthArgSpec("selectedModuleIndex"));
		TXChannelRouting.display(this.getSortedTXVModules[holdIndex]);
	}

	editSelectedNode {arg argSelection;
		var holdIndex = argSelection ? (this.getSynthArgSpec("selectedNodeIndex"));
		TXChannelRouting.display(this.getSortedTXVNodes[holdIndex]);
	}

	editSelectedDrawConnection {arg argSelection;
		var holdIndex = argSelection ? (this.getSynthArgSpec("selectedDrawConnectionIndex"));
		TXChannelRouting.display(this.getSortedTXVDrawConnections[holdIndex]);
	}

	editSelectedImageConnection {
		var holdIndex = this.getSynthArgSpec("selectedImageConnectionIndex");
		TXChannelRouting.display(this.getTXVImageConnectionModules[holdIndex]);
	}


	////////////////////////////

	getSortedTXVModules {
		var arrSortedModules;
		arrSortedModules = arrTXVModules.copy;
		if (this.getSynthArgSpec("moduleSortOrder") == 1, {
			arrSortedModules.sort({arg a, b; a.instName < b.instName;});
		});
		^arrSortedModules;
	}

	getSortedTXVModuleNames {
		// returns arr based on moduleSortOrder: 0 - Build Order/ 1 - Alphabetical Order
		var arrNames;
		arrNames = this.getSortedTXVModules.collect({arg item, i; item.instName;});
		^arrNames;
	}

	getSortedTXVNodes {
		var arrSortedNodes;
		arrSortedNodes = arrTXVNodes.copy;
		if (this.getSynthArgSpec("nodeSortOrder") == 1, {
			arrSortedNodes.sort({ arg a, b;
				var aComp, bComp;
				aComp= a.sourceModuleName ++ a.sourceOutputName ++ a.destinationModuleName ++ a.destinationParamName;
				bComp = b.sourceModuleName ++ b.sourceOutputName ++ b.destinationModuleName ++ b.destinationParamName;
				aComp < bComp;
			});
		}, {
			if (this.getSynthArgSpec("nodeSortOrder") == 2, {
				arrSortedNodes.sort({ arg a, b;
					var aComp, bComp;
					aComp = a.destinationModuleName ++ a.destinationParamName
						++ a.sourceModuleName ++ a.sourceOutputName;
					bComp = b.destinationModuleName ++ b.destinationParamName
						++ b.sourceModuleName ++ b.sourceOutputName;
					aComp < bComp;
				});
			});
		});
		^arrSortedNodes;
	}

	/*getTXVParameters{ ^[
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
	];}*/
	getSortedTXVNodeNames {
		//  returns arr based on nodeSortOrder: 0 Build Order/ 1 Source Module Order/ 2 Destination Module Order
		var arrNames;
		arrNames = this.getSortedTXVNodes.collect({arg item, i;
			var activeString, modNegativeString, modAmount, modAmountString, sourceName, destName;
			if (item.dictCurrentParameterVals['/TXModNode_1/active'] == 1, {
				activeString = "[x]active";
			}, {
				activeString = "[  ]active";
			});
			if (item.dictCurrentParameterVals['/TXModNode_1/negativeModulation/fixedValue'] == 1, {
				modNegativeString = "[x] neg";
			}, {
				modNegativeString = "[  ] neg";
			});
			modAmountString = item.dictCurrentParameterVals['/TXModNode_1/modAmount/fixedValue'].linlin(0, 1,
				item.dictCurrentParameterVals['/TXModNode_1/modAmount/softMin'],
				item.dictCurrentParameterVals['/TXModNode_1/modAmount/softMax']
			).round(0.001).asString;
			if (modAmountString.size == 1, {
				modAmountString = (modAmountString ++ ".000");
			},{
				modAmountString = (modAmountString ++ "000").keep(5);
			});
			sourceName = this.getModuleWithTXVID(item.sourceTXVID).instName;
			destName = this.getModuleOrNodeWithTXVID(item.destinationTXVID).instName;
			item.instName ++ "   " ++ activeString ++ "   " ++ modNegativeString
			++ "   mod: " ++ modAmountString
			++ "   source: " ++ sourceName ++ " " ++ item.sourceOutputName
			++ "   dest: "  ++ destName ++ " " ++ item.destinationParamName
		});
		^arrNames;
	}

	getSortedTXVDrawConnections {
		var arrSortedDrawConnections;
		arrSortedDrawConnections = arrTXVDrawConnections.copy;
		// sort order options: ["build order", "source order", "destination order"]
		// if "source order"
		if (this.getSynthArgSpec("drawConnectionSortOrder") == 1, {
			arrSortedDrawConnections.sort({ arg a, b;
				var holdBool;
				if (a.sourceModuleName.asString == b.sourceModuleName.asString, {
					holdBool = a.destinationModuleName.asString < b.destinationModuleName.asString;
				},{
					holdBool = a.sourceModuleName.asString < b.sourceModuleName.asString;
				});
				holdBool;
			});
		}, {
			// if "destination order"
			if (this.getSynthArgSpec("drawConnectionSortOrder") == 2, {
				arrSortedDrawConnections.sort({arg a, b;
					var holdBool;
					if (a.destinationModuleName.asString == b.destinationModuleName.asString, {
						holdBool = (a.dictCurrentParameterVals['/TXDrawConnection_1/layerNo']
							< b.dictCurrentParameterVals['/TXDrawConnection_1/layerNo']);
						//holdBool = a.sourceModuleName.asString < b.sourceModuleName.asString;
					},{
						holdBool = a.destinationModuleName.asString < b.destinationModuleName.asString;
					});
					holdBool;
				});
			});
		});
		^arrSortedDrawConnections;
	}

	getSortedTXVDrawConnectionNames {
		//  returns arr based on drawConnectionSortOrder: 0 Build Order / 1 Source Module Order/ 2 Destination Module Layer Order
		var arrNames;
		arrNames = this.getSortedTXVDrawConnections.collect({arg item, i;
			var sourceName, destName;
			sourceName = this.getModuleWithTXVID(item.sourceTXVID).instName;
			destName = this.getModuleWithTXVID(item.destinationTXVID).instName;
			item.instName ++ ",   source: " ++ sourceName  ++
			",   destination: "  ++ destName ++
			",   layer: "  ++ item.dictCurrentParameterVals['/TXDrawConnection_1/layerNo'].asString
		});
		^arrNames;
	}

	getTXVImageConnectionNames {
		var arrNames = [];
		arrTXVModules.do({arg module, i;
			module.data['externalImageModuleSlots'].asArray.do({arg slot, i;
				var extModuleID, extModuleName, outName;
				extModuleID = module.dictCurrentParameterVals[slot.asSymbol];
				if (extModuleID == 0, {
					extModuleName = "[empty]";
				}, {
					extModuleName = this.getModuleWithTXVID(extModuleID).instName;
				});
				outName = module.instName ++ ",  parameter: " ++ slot.split.last ++ " - " ++ extModuleName;
				arrNames = arrNames.add(outName);
			});
		});
		^arrNames;
	}

	getTXVImageConnectionModules {
		var arrModules = [];
		arrTXVModules.do({arg module, i;
			module.data['externalImageModuleSlots'].asArray.do({arg slot, i;
				arrModules = arrModules.add(module);
			});
		});
		^arrModules;
	}

	////////////////////////////

	getValidTXVNodeSourceModules {
		var newArr = arrTXVModules.select({arg item, i; item.numModOutputs > 0});
		newArr = newArr.add(this); // add system
		^newArr.sort({ arg a, b; a.instName < b.instName });
	}

	getValidTXVNodeSourceModuleNames {
		^this.getValidTXVNodeSourceModules.collect({arg item; item.instName});
	}

	getValidTXVNodeSourceOutputNames {
		// based on selected source module
		var holdIndex, holdModule;
		holdIndex = this.getSynthArgSpec("nodeSourceModuleIndex");
		if (holdIndex > 0, {
			var holdModule = this.getValidTXVNodeSourceModules[holdIndex - 1];
			if (holdModule.notNil, {
				^holdModule.getTXVOutputs;
			});
		});
		^[];
	}

	getValidTXVNodeDestModules {
		var newArr = arrTXVModules.select({arg item, i; item.numModInputs > 0});
		newArr = (newArr.add(this)).sort({ arg a, b; a.instName < b.instName });
		newArr = newArr ++ arrTXVNodes;
		^newArr;
	}

	getValidTXVNodeDestModuleNames {
		^this.getValidTXVNodeDestModules.collect({arg item; item.instName});
	}

	getValidTXVNodeDestParamNames {
		// based on selected destination module
		var holdIndex, holdModule;
		holdIndex = this.getSynthArgSpec("nodeDestModuleIndex");
		if (holdIndex > 0, {
			holdModule = this.getValidTXVNodeDestModules[holdIndex - 1];
			if (holdModule.notNil, {
				^holdModule.getTXVSynthModParameters.collect({arg item, i;
					var holdArrStrings = item[1].split;
					holdArrStrings[holdArrStrings.size - 2]
				});
			});
		});
		^[];
	}

	getValidTXVDrawSources {
		^arrTXVModules.select({arg item, i; item.canDraw == 1});
	}

	getValidTXVDrawSourceOutputNames {
		^this.getValidTXVDrawSources.collect({arg item; item.instName});
	}

	getValidTXVDrawDestinations {
		^([this] ++ arrTXVModules.select({arg item, i; item.canDrawLayers == 1}));
	}

	getValidTXVDrawDestModuleNames {
		^this.getValidTXVDrawDestinations.collect({arg item; item.instName;});
	}

	resetAssetEditParms {
		this.setSynthArgSpec("selectedAssetIndex", 0);
		this.setSynthArgSpec("selectedAssetBankNo", 0);
		this.setSynthArgSpec("assetBankName", "");
	}

	////////////////////////////

	// override as needed
	getInitialDisplayOption {
		^"showSystem";
	}

	getNextOscMsgNo {
		var holdNo = nextOscMsgNo;
		nextOscMsgNo = nextOscMsgNo + 1;
		^holdNo;
	}

	buildArrNetAddresses{
		var holdAddress, holdPort;
		arrNetAddresses = [];
		4.do({arg item, i;
			if (this.getSynthArgSpec("i_activate" ++ (i+1).asString) == 1, {
				holdAddress = this.getSynthArgSpec("i_address" ++ (i+1).asString);
				holdPort = this.getSynthArgSpec("i_port" ++ (i+1).asString);
				arrNetAddresses = arrNetAddresses.add(NetAddr(holdAddress, holdPort));
			});
		});
	}

	arrAddressData {
		^[ "i_address1", "i_port1", "i_notes1", "i_activate1",
			"i_address2", "i_port2", "i_notes2", "i_activate2",
			"i_address3", "i_port3", "i_notes3", "i_activate3",
			"i_address4", "i_port4", "i_notes4", "i_activate4"
		].collect ({arg item, i; this.getSynthArgSpec(item);});
	}

	loadArrAddressData { arg argData;
		[ "i_address1", "i_port1", "i_notes1", "i_activate1",
			"i_address2", "i_port2", "i_notes2", "i_activate2",
			"i_address3", "i_port3", "i_notes3", "i_activate3",
			"i_address4", "i_port4", "i_notes4", "i_activate4"
		].do ({arg item, i; this.setSynthValue(item, argData[i]);});
		this.buildArrNetAddresses;
	}

	extraSaveData {
		^[parentTXVSysModuleID, txvModuleID, txvModuleName, dictCurrentParameterVals.asArgsArray,
			arrTXVAssets, arrAssetBankNames];
	}

	loadExtraData {arg argData, isPresetData = false;  // override default method
		parentTXVSysModuleID = argData.at(0);
		txvModuleID = argData.at(1);
		txvModuleName = argData.at(2);
		dictCurrentParameterVals.putPairs(argData.at(3) ? []);
		// adjust assets adding bankNo if missing
		dictCurrentParameterVals.keysDo({arg key;
			var partKey = key.asString.keep("assetSlot/".size);
			var holdVal;
			if (partKey == "assetSlot/", {
				holdVal = dictCurrentParameterVals[key];
				if (holdVal.isArray.not, {
					holdVal = [holdVal, 0];
				});
			});
		});
		if (isPresetData != true, {
			// adjust assets
			arrTXVAssets = argData.at(4) ? [];
			arrTXVAssets.do({arg asset, i;
				if (asset[5].isNil, {
					asset = asset.add(0);	 // default bankno 0
				});
			});
			// bankNames
			if (argData.at(5).notNil, {
				arrAssetBankNames = argData.at(5);
				// legacy - adjust longer arrays
				arrAssetBankNames.size.do({arg i;
					arrAssetBankNames[i] = arrAssetBankNames[i].asArray.keep(30);
				});
			}, {
				this.resetArrAssetBankNames;
			});
		});
		displayOption = "showSystem";
		this.resetAssetEditParms;
		this.buildGuiSpecArray;
		this.setActionSpecs;
		this.oscActivate;
		this.buildArrNetAddresses;
		{ // defer
			this.setFullScreen(false);
			this.sendCurrentValues;
		}.defer(0.5);
	}

	setFullScreen {arg boolVal = true;
		this.sendParameterToTXV(this.txvModuleID, "/TXSystem_1/showFullScreen", boolVal.asInt);
	}

	deleteModuleExtraActions {
		var toBeDeleted;
		//	remove responders
		this.oscDeactivate;
		// delete all owned modules & reset TXV
		toBeDeleted = [];
		arrTXVDrawConnections.do({arg item, i;
			toBeDeleted =  toBeDeleted.add(item);
		});
		arrTXVNodes.do({arg item, i;
			toBeDeleted =  toBeDeleted.add(item);
		});
		arrTXVModules.do({arg item, i;
			toBeDeleted =  toBeDeleted.add(item);
		});
		toBeDeleted.do({arg item, i;
			item.deleteModule;
		});
		this.resetTXVSystem;
	}

	// override base class
	setActionSpecs {
		var holdDummyAction = TXAction.new(\commandAction, "...", {});
		var holdStartTXVAction = TXAction.new(\commandAction, "Start TXV", {this.startTXVSystem});
		var holdKillTXVAction = TXAction.new(\commandAction, "Kill TXV", {this.killTXVSystem});
		var holdRebuildTXVAction = TXAction.new(\commandAction, "Rebuild TXV system", {this.rebuildTXVSystem});
		arrActionSpecs = [
			holdDummyAction,
			holdStartTXVAction,
			holdKillTXVAction,
			holdRebuildTXVAction,
		]
		++ TXV_BuildActionSpecs.from(this);
	}

}

