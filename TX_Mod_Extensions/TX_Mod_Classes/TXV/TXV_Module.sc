// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXV_Module : TXModuleBase {	// remote for TXV

	classvar <>classData;

	classvar <defaultName = "TXV Module"; // change in subclasses

	var <>parentTXVSysModuleID;
	var <>parentTXVSystem;
	var <>txvModuleID;
	var <>txvModuleName;
	var <>txvModuleType;
	var <>canDraw;
	var <>canDrawLayers;
	var <>numModInputs;
	var <>numModOutputs;
	var <>hasTexture;
	var <>data;
	var <>dictCurrentParameterVals;

	var	arrPResponders;
	//	var arrTXVGuiSpecs;
	var arrSendTrigIDs;
	var arrSynthDefFuncs;
	var arrSynths;
	var displayOption;
	var subSectDisplayOptions;
	var holdScrollView;
	var mainParametersButtonNeeded = true;
	var mainParameters;
	var parameterSections;
	var parameterSubs;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		//classData.defaultName = "TXV Module"; // change in subclasses
		classData.moduleRate = "control";
		classData.moduleType = "action";
		classData.noInChannels = 0;
		classData.arrAudSCInBusSpecs = [];
		classData.arrCtlSCInBusSpecs = [];
		classData.noOutChannels = 0;
		classData.arrOutBusSpecs = [];
		classData.guiWidth = 1070;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	getTXVParameters{ ^[
		// Set in subclasses
	];}

	//
	getTXVOutputs{ ^[
		// Set in subclasses
	];}

	// can override in subclasses
	getTXVParameterSectionSpecs {
		var outArray;
		//  FORMAT WITH SUB-SECTIONS USING $ AS SUB-SECTION SEPARATOR
		outArray = [
			// format: ["Example Section Name$Example Sub-Section Name", nameValidityFunction]
			["Draw$Active, Size, Alpha", {arg parameterName;
				var arrNames = ["drawActive", "drawLayersRule", "maxWidthHeightRule",
					"customMaxWidth", "customMaxHeight","customMaxDepth",
					"useSampleSizeForDrawSize", "constrainToEdges","alphaThreshold", "alpha",
					"drawAlpha", "drawWidth", "drawHeight", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Scale", {arg parameterName;
				var arrNames = ["scaleX", "scaleY", "scaleZ", "useScaleXForScaleY", "useScaleXForScaleZ" ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
			["Draw$Position, Anchor, Rotate", {arg parameterName;
				var arrNames = ["drawPosX", "drawPosY", "drawPosZ", "positionX", "positionY", "positionZ",
					"useSamplePosForDrawPos","anchorX", "anchorY", "anchorZ", "rotate", "rotateX", "rotateY", "rotateZ",
					"rotateMultiply", ];
				arrNames.indexOfEqual(parameterName).notNil;
			}],
		];
		^outArray;
	}

	getTXVSynthModParameters{ arg arrParms = [];
		var holdArr = [];
		var groupString = "modParameterGroup";
		if (arrParms == [], {
			arrParms = this.getTXVParameters;
		});
		arrParms.do({ arg item, i;
			if (item[0].keep((groupString.size)) == groupString){
				holdArr = holdArr.add(item[2][3]);
			};
		});
		^holdArr;
	}

	getTXVParameterActionSpecs{ arg arrParms = [];
		var holdArr = [];
		var groupString = "modParameterGroup";
		if (arrParms == [], {
			arrParms = this.getTXVParameters;
		});
		arrParms.do({ arg item, i;
			if (item[0].keep((groupString.size)) == groupString){
				holdArr = holdArr.add(item[2][3]);
			};
		});
		^holdArr;
	}

	getTXVParameterGroups{ arg arrParms = [];
		var holdDict = ();
		var groupString = "groupStart";
		if (arrParms == [], {
			arrParms = this.getTXVParameters;
		});
		arrParms.do({ arg item, i;
			if (item[0].keep((groupString.size)) == groupString){
				holdDict[item[1].asSymbol] = false;
			};
		});
		^holdDict;
	}

	init {arg argInstName;
		var groupFound;
		data = ();
		// data['arrParameters'] = this.getTXVParameters;
		data['arrSynthModParameters'] = this.getTXVSynthModParameters;
		data['dictParameterGroups'] = this.getTXVParameterGroups;
		data['arrModOutputs'] = this.getTXVOutputs;
		data['numParameters'] = this.getTXVParameters.size;
		data['arrModPStrings'] = data['numParameters'].collect({arg item; "modp" ++ ("00" ++ item.asString).keep(-3)});
		//		classData.arrCtlSCInBusSpecs = data['arrModPStrings'].deepCopy.collect({arg item, i; [item.asString, 1, item, 0]});
		data['visibleOrigins'] = ();
		this.getTXVParameterSectionSpecs.do({arg currentSpec, i;
			data['visibleOrigins'][currentSpec[0].asSymbol] = Point.new(0,0);
		});
		// extract subsection names
		parameterSubs = ();
		this.getTXVParameterSectionSpecs.do({arg currentSpec, i;
			var groupName, splitName;
			groupName = currentSpec[0];
			// add subsection if found
			if (groupName.indexOf($$).notNil, {
				splitName = groupName.split($$);
				parameterSubs[splitName[0].asSymbol] = parameterSubs[splitName[0].asSymbol].add(splitName[1]);
			});
		});
		subSectDisplayOptions = ();
		parameterSubs.pairsDo({arg key, value;
			subSectDisplayOptions[key] = value.first;
		});
		// extract parameters separately into groups
		mainParameters = [];
		parameterSections = ();
		this.getTXVParameters.do({arg currentParameter, i;
			// store any externalImageModuleSlots
			if (currentParameter[0] == "extImageModuleSlot", {
				data['externalImageModuleSlots'] = data['externalImageModuleSlots'].asArray.add(currentParameter[1].copy);
			});
			groupFound = false;
			this.getTXVParameterSectionSpecs.do({arg currentSpec, i;
				var nameValidityFunction, parameterName, groupName, splitName;
				if (groupFound == false, {
					parameterName = currentParameter[1].split.last;
					nameValidityFunction = currentSpec[1];
					if (nameValidityFunction.value(parameterName), {
						groupName = currentSpec[0];
						parameterSections[groupName.asSymbol] = parameterSections[groupName.asSymbol].add(currentParameter);
						groupFound = true;
					});
				});
			});
			if (groupFound == false, {
				mainParameters = mainParameters.add(currentParameter);
			});
		});

		classData.arrCtlSCInBusSpecs = data['arrSynthModParameters'].collect({arg item, i;
			var arrStrings, holdNameString;
			arrStrings = item[1].split; // split address
			holdNameString = arrStrings[arrStrings.size-2];
			// e.g.	["Delay", 1, "modDelay", 0],
			[holdNameString, 1, data['arrModPStrings'][i].asString, 0];
		});
		// create module instance version of arrCtlSCInBusSpecs
		myArrCtlSCInBusSpecs = classData.arrCtlSCInBusSpecs.deepCopy;

		//	set  class specific instance variables

		// create unique Trigger ids
		arrSendTrigIDs = [];
		data['numParameters'].do({arg item, i;
			arrSendTrigIDs = arrSendTrigIDs.add(UniqueID.next);
		});

		// create dict of current parameter vals
		dictCurrentParameterVals = ();
		this.extractCurrValsFrom(this.getTXVParameters);

		arrSynthArgSpecs = [] ++ data['arrModPStrings'].collect({arg item, i; [item, 0, 0]});

		// add any extra specs in subclasses
		arrSynthArgSpecs = arrSynthArgSpecs ++ this.getExtraSynthArgSpecs;

		// divide into multiple synthdeffuncs to overcome 256 limit
		data['maxSynthDefItems'] = 50;  // keep synthdefs small enough to send
		data['numSynthDefs'] = (data['arrModPStrings'].size / data['maxSynthDefItems']).roundUp.asInteger;
		arrSynthDefFuncs = data['numSynthDefs'].collect({arg synthDefIndex;
			var startIndex = synthDefIndex * data['maxSynthDefItems'];
			var endIndex = ((synthDefIndex + 1) * data['maxSynthDefItems']).min(data['arrModPStrings'].size - 1);
			var outSynthDefFunc = {
				arg out;

				var arrModParmArgs;
				var arrTrigs, arrSendTrigs, imp, refreshImp;

				arrModParmArgs = data['arrModPStrings'][startIndex..endIndex].collect({arg item, i;
					NamedControl.kr(item.asSymbol, 0, 0);
				});

				// // testing xxx
				// "------- TXV_Module.init   - arrModParmArgs.size".postln;
				// arrModParmArgs.size.postln;

				// try lower data rates for less cpu load
				// imp = (1 - Impulse.kr(60));
				imp = (1 - Impulse.kr(30));
				// imp = (1 - Impulse.kr(20));
				// imp = (1 - Impulse.kr(10));

				arrTrigs = arrModParmArgs.collect({arg item;
					Trig.kr(imp * HPZ1.kr(item.max(0).min(1)).abs, 0.02);
				});
				// testing without refreshImp
				// refreshImp = Impulse.kr(0.1); // refresh every 10 secs
				arrSendTrigs = arrTrigs.collect({arg item, i;
					//SendTrig.kr(refreshImp + item, arrSendTrigIDs[i + startIndex], arrModParmArgs[i]);
					SendTrig.kr(item, arrSendTrigIDs[i + startIndex], arrModParmArgs[i]);
				});
			}; // End of synth def function
			outSynthDefFunc;
		});

		guiSpecTitleArray = [
			["TitleBar"],
			["ActionButton", "Help", {
				var holdPath = this.class.filenameSymbol.asString.asPathName;
				var txvHelpPath = holdPath.pathOnly ++ "TXV HelpFiles/TX_" ++ this.class.defaultName ++".html";
				TXHelpScreen.openFilePath(txvHelpPath);
			}, 40, TXColor.white, TXColor.sysHelpCol],
			// ["RunPauseButton"],
			["DeleteModuleButton"],
			["RebuildModuleButton"],
			["HideModuleButton"],
			["LegacyModuleText"],
			["ActionButton", "Show parent TXV System", {TXChannelRouting.display(parentTXVSystem);}, 154,
				TXColor.white, TXColor.sysGuiCol2],
			["NextLine"],
			["ModuleActionPopup", 340],
			["ModuleInfoTxt", 420],
			["SpacerLine", 1],
		];
		this.setActionSpecs;
		this.initialiseCurveData;
		this.initExtraActions;
		displayOption = this.getInitialDisplayOption;
		this.buildGuiSpecArray;

		// base initialise
		this.baseInit(this, argInstName);

		//	load the synthdef and create the synths
		// this.loadAndMakeSynth;  // OLD
		this.makeGroupAndSynths;   // NEW
		this.oscActivate;
	}

	/* NEW METHODS >>>>>>>>  ===============*/

	// modified from TXModuleBase methods
	makeGroupAndSynths {
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// make group
			this.makeGroup;
			// pause
			system.server.sync;
			// load synthdef and make synths
			this.loadSynthDef;
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
	}

	// modified from TXModuleBase - load multiple synthdefs
	loadSynthDef {
		// check for empty array
		if (arrSynthDefFuncs.asArray.size > 0, {
			Routine.run {
				var holdModCondition, synthNode;

				// add condition to load queue
				holdModCondition = system.holdLoadQueue.addCondition;
				// pause
				holdModCondition.wait;
				// pause
				system.server.sync;

				// make synthdefs
				data['numSynthDefs'].do({arg synthDefIndex;
					var startIndex = synthDefIndex * data['maxSynthDefItems'];
					var endIndex = ((synthDefIndex + 1) * data['maxSynthDefItems']).min(data['arrModPStrings'].size - 1);
					var mySynthDefName = synthDefName ++ "_" ++ synthDefIndex;
					// create arrays for mapping synth arguments to busses
					// this.class.arrAudSCInBusSpecs.do({ arg item, ind;
					// 	arrAudSCInBusMappings = arrAudSCInBusMappings.add(item.at(2));// synth arg index no
					// 	arrAudSCInBusMappings = arrAudSCInBusMappings.add(arrAudSCInBusses.at(ind).index);// bus index no
					// });
					this.myArrCtlSCInBusSpecs.asArray[startIndex..endIndex].do({ arg item, ind;
						if (item.at(3) != 0, {
							// create arrays for mapping synth arguments to busses
							arrCtlSCInBusMappings = arrCtlSCInBusMappings.add(item.at(2));// synth arg index no
							arrCtlSCInBusMappings = arrCtlSCInBusMappings
							.add("c" ++ arrCtlSCInBusses.at(ind + startIndex).index.asString);// bus index no
						});
					});
					// //	set and update synthDefRates based on module options
					// synthDefRates = arrSynthArgSpecs.collect({arg item, i; item.at(2)});
					// synthDefRates = synthDefRates ++ [0, 0, 0, 0, 0, 0, 0];   // add dummy values for safety

					// using add instead of send
					//	send the SynthDef
					// SynthDef(instName, synthDefFunc, synthDefRates).send(system.server);
					//	add the SynthDef
					//SynthDef(mySynthDefName, synthDefFunc, synthDefRates).add;
					SynthDef(mySynthDefName, arrSynthDefFuncs[synthDefIndex]).add;
				});
				// pause
				system.server.sync;

				// automatically make synths when synthdef loads
				this.makeAllSynths;
				// pause
				system.server.sync;

				// remove condition from load queue
				system.holdLoadQueue.removeCondition(holdModCondition);
			};
		});
	}

	// modified from TXModuleBase
	rebuildSynth { 	// note this method is overridden in some modules
		this.loadSynthDef;
	}


	// ======== MODIFIED  FROM TXModule.makeSynth ============
	makeAllSynths {
		var arrSynthArgs;
		// build arrSynthArgs
		arrSynthArgs = [];
		arrSynthArgSpecs.do({ arg item, i;
			arrSynthArgs = arrSynthArgs.addAll([item.at(0), item.at(1)]);
		});
		// add any side chain input bus mappings
		arrSynthArgs = arrSynthArgs
		++ arrCtlSCInBusMappings
		//++ arrAudSCInBusMappings  // audio not needed
		;
		// // if relevent, add output bus mapping
		// if (outBus.notNil, {
		// 	arrSynthArgs = arrSynthArgs ++ [\out, outBus.index];
		// });
		// // add any  input bus mappings
		// if (inputBusses.notNil, {
		// 	arrSynthArgs = arrSynthArgs ++ [\in, inputBusses.at(0)];
		// });
		// add any buffer number assignments
		// buffers.do({ arg item, i;
		// 	var spec = this.class.arrBufferSpecs.at(i);
		// 	if (spec.notNil, {
		// 		arrSynthArgs = arrSynthArgs.addAll([spec.at(0), item.bufnum]);
		// 	});
		// });

		// allow for system to sync
		Routine.run {
			var holdModCondition, synthNode;

			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			// pause
			system.server.sync;

			// free any old synths first
			arrSynths.asArray.do({arg item;
				item.free;
			});
			arrSynths = [];

			// make synths
			data['numSynthDefs'].do({arg index;
				var mySynthDefName = synthDefName ++ "_" ++ index;
				if (system.autoRun == true, {
					//	load and create running synth on server
					synthNode = Synth.new(mySynthDefName,
						arrSynthArgs,
						moduleNode,
						\addToTail ;
					);
					arrSynths = arrSynths.add(synthNode);
					// set synth status
					moduleNodeStatus =  "running";
				}, {
					//	load and create paused synth on server
					synthNode = Synth.newPaused(mySynthDefName,
						arrSynthArgs,
						moduleNode,
						\addToTail ;
					);
					arrSynths = arrSynths.add(synthNode);
					// set module node status
					moduleNodeStatus =  "paused";
				});
			});

			// pause
			system.server.sync;

			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
	}

	// override as needed
	initExtraActions {
		//
	}

	// override as needed
	getExtraSynthArgSpecs {
		^[];
	}

	// override as needed
	getDefaultParamSectionName {
		^"Main";
	}

	// override as needed
	getInitialDisplayOption {
		var defaultName = this.getDefaultParamSectionName;
		var subSectName = subSectDisplayOptions[defaultName.asSymbol];
		displayOption = defaultName;
		if (subSectName.notNil, {
			displayOption = displayOption ++ "$" ++ subSectDisplayOptions[defaultName.asSymbol];
		});
		^displayOption;
	}

	extractCurrValsFrom {arg parmArray;
		parmArray.do ({arg item, i;
			case
			/* PARAMETER: [parameterType, address, val, ?min, ?max] */
			// for valid parameters, add value to dict with address as key
			{item[0].keep(10) == "assetSlot/"} {dictCurrentParameterVals[item[1].asSymbol] = [item[2], item[3] ? 0]}
			{item[0] == "extImageModuleSlot"} {dictCurrentParameterVals[item[1].asSymbol] = item[2]}
			{item[0] == "int"} {dictCurrentParameterVals[item[1].asSymbol] = item[2]}
			{item[0] == "float"} {dictCurrentParameterVals[item[1].asSymbol] = item[2]}
			{item[0] == "float256"} {dictCurrentParameterVals[item[1].asSymbol] = item[2..(2+255)]}
			{item[0] == "bool_int"} {dictCurrentParameterVals[item[1].asSymbol] = item[2]}
			{item[0] == "string"} {dictCurrentParameterVals[item[1].asSymbol] = item[2]}
			/* GROUP:     [groupType, address, parameters] */
			// for groups, recursively run this function
			{item[2].isArray} {this.extractCurrValsFrom(item[2])}
		});
	}

	//////////////

	buildGuiSpecArray {
		var arrSectionNames = [];
		var arrSubsectionNames = [];
		var arrSectionWidths = [];
		var arrSubsectionWidths = [];
		var scrollerHeight = 470;
		var displayOptFirst = displayOption.split($$).first;

		// // extract parameters separately into groups
		// this.getTXVParameters.do({arg currentParameter, i;
		// 	var groupFound = false;
		// 	this.getTXVParameterSectionSpecs.do({arg currentSpec, i;
		// 		var parameterName;
		// 		var groupName, splitName;
		// 		var nameValidityFunction = currentSpec[1];
		// 		if (groupFound == false, {
		// 			parameterName = currentParameter[1].split.last;
		// 			if (nameValidityFunction.value(parameterName), {
		// 				groupName = currentSpec[0];
		// 				// add subsection if found
		// 				if (groupName.indexOf($$).notNil, {
		// 					splitName = groupName.split($$);
		// 					parameterSubs[splitName[0].asSymbol] = parameterSubs[splitName[0].asSymbol].add(splitName[1]);
		// 				});
		// 				parameterSections[groupName.asSymbol] = parameterSections[groupName.asSymbol].add(currentParameter);
		// 				groupFound = true;
		// 			});
		// 		});
		// 	});
		// 	if (groupFound == false, {
		// 		mainParameters = mainParameters.add(currentParameter);
		// 	});
		// });

		guiSpecArray = [];
		if (parameterSections.size > 0, {
			if (mainParameters.size > 0, {
				mainParametersButtonNeeded = true;
				guiSpecArray = guiSpecArray ++[
					["ActionButton", this.getDefaultParamSectionName, {displayOption = this.getDefaultParamSectionName;
						this.buildGuiSpecArray; system.showView;}, this.getStringWidth(this.getDefaultParamSectionName),
					TXColor.white, this.getSectionButtonColour(displayOption == this.getDefaultParamSectionName)],
					["Spacer", 3],
				];
			}, {
				mainParametersButtonNeeded = false;
			});
			this.getTXVParameterSectionSpecs.do({arg item, i;
				var holdName = item[0].split($$).first;
				if (arrSectionNames.indexOfEqual(holdName).isNil, {
					arrSectionNames = arrSectionNames.add(holdName);
				});
			});
			arrSectionNames.do({arg sectName;
				var holdWidth = this.getStringWidth(sectName);
				arrSectionWidths = arrSectionWidths.add(holdWidth);
				guiSpecArray = guiSpecArray ++[
					["ActionButton", sectName, {
						var subSectName = subSectDisplayOptions[sectName.asSymbol];
						displayOption = sectName;
						if (subSectName.notNil, {
							displayOption = displayOption ++ "$" ++ subSectDisplayOptions[sectName.asSymbol];
						});
						this.buildGuiSpecArray; system.showView;}, holdWidth,
					TXColor.white, this.getSectionButtonColour(displayOptFirst == sectName)],
					["Spacer", 3],
				];
			});
		});
		// add extra menu items for subclasses
		guiSpecArray = guiSpecArray ++ this.getExtraSectionItems;

		arrSubsectionNames = parameterSubs[displayOptFirst.asSymbol].asArray;
		if (arrSubsectionNames.size > 0, {
			guiSpecArray = guiSpecArray ++[
				["DividingLine"],
				["SpacerLine", 2],
			];
			arrSubsectionNames.do({arg subSectName;
				var holdWidth = this.getStringWidth(subSectName);
				arrSubsectionWidths = arrSubsectionWidths.add(holdWidth);
				guiSpecArray = guiSpecArray ++[
					["ActionButton", subSectName, {
						displayOption = displayOptFirst ++ "$" ++ subSectName;
						subSectDisplayOptions[displayOptFirst.asSymbol] = subSectName;
						this.buildGuiSpecArray; system.showView;
					}, holdWidth, TXColor.white,
					this.getSubSectButtonColour(subSectDisplayOptions[displayOptFirst.asSymbol] == subSectName)],
					["Spacer", 3],
				];
			});
			guiSpecArray = guiSpecArray ++ this.getExtraSubSectionItems;
		});

		if ((parameterSections.size > 0) || (arrSubsectionNames.size > 0), {
			guiSpecArray = guiSpecArray ++[
				["DividingLine"],
				["SpacerLine", 4],
			];
		});

		scrollerHeight = this.getScrollerHeight(arrSectionWidths, arrSubsectionWidths);

		if (displayOption == this.getDefaultParamSectionName, {
			guiSpecArray = guiSpecArray ++[
				["TXV_GuiScroller", mainParameters,
					dictCurrentParameterVals,
					{arg argAddress, argValue; this.sendModuleParmToTXV( argAddress, argValue);},
					{arg view; holdScrollView = view;},
					{arg view; data['visibleOrigins'][displayOption.asSymbol] = view.visibleOrigin; },
					data['dictParameterGroups'],
					scrollerHeight,
				],
				["SpacerLine", 1],
			];
		}, {
			if (parameterSections[displayOption.asSymbol].notNil, {
				guiSpecArray = guiSpecArray ++[
					["TXV_GuiScroller", parameterSections[displayOption.asSymbol],
						dictCurrentParameterVals,
						{arg argAddress, argValue; this.sendModuleParmToTXV( argAddress, argValue);},
						{arg view; holdScrollView = view;},
						{arg view; data['visibleOrigins'][displayOption.asSymbol] = view.visibleOrigin; },
						data['dictParameterGroups'],
						scrollerHeight,
					],
					["SpacerLine", 1],
				];
			});
		});
		// get extra actions if needed for subclasses
		guiSpecArray = guiSpecArray ++ this.getExtraGuiBuildActions;
	}

	getExtraSectionItems {   // overide as needed
		^[];  // default is empty
		/*
		// Example:
		^[
			["ActionButton", "XXX", {displayOption = "showXXX";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getSectionButtonColour(displayOption == "showXXX")],
			["Spacer", 3],
		];
		*/
	}

	getExtraGuiBuildActions {   // overide as needed
		var outArray = [];  // default is empty
		/*
		// Example:
		if (displayOption == "showXXX", {
			outArray = [
				["TXTextBox","Preset name", "i_layerA_presetText", nil, 350],
				["Spacer", 3],
			];
		});
		*/
		^outArray;
	}

	getExtraSubSectionItems {   // overide as needed
		var outArray = [];  // default is empty
		/*
		// Example:
		if (displayOption.split($$).first == "XXX", {
			outArray = [
				["ActionButton", "XXX", {displayOption = "XXX$showXXX";
					this.buildGuiSpecArray; system.showView;}, 80,
				TXColor.white, this.getSectionButtonColour(displayOption == "XXX$showXXX")],
				["Spacer", 3],
			];
		});
		*/
		^outArray;
	}


	getSectionButtonColour { arg colour2Boolean;
		if (colour2Boolean == true, {
			^TXColor.sysGuiCol4;
		},{
			^TXColor.sysGuiCol1;
		});
	}

	getSubSectButtonColour { arg colour2Boolean;
		if (colour2Boolean == true, {
			^TXColor.sysGuiCol4;
		},{
			//^TXColor.sysGuiCol2;
			^TXColor.sysGuiCol1;
		});
	}

	getStringWidth {arg stringArg;
		var outsize;
		var numCaps = 0;
		stringArg.do({arg char;
			if (char.isUpper, {
				numCaps = numCaps + 1;
			});
		});
		outsize = 5.5 * ((numCaps * 0.5) + stringArg.size + 4);
		outsize = outsize.max(60);
		^outsize;
	}

	getScrollerHeight {arg arrSectionWidths, arrSubsectionWidths, gap = 4;
		var scrollerHeight, buttonRows, maxRowWidth, currentRowWidth;

		maxRowWidth = classData.guiWidth - 12;
		buttonRows = 0;
		currentRowWidth = 0;
		arrSectionWidths.do({arg item, i;
			var itemWidth = item + gap;
			if ((currentRowWidth + itemWidth) > maxRowWidth, {
				buttonRows = buttonRows + 1;
				currentRowWidth = 0;
			});
			currentRowWidth = currentRowWidth + itemWidth;
		});
		if (currentRowWidth > 0, {
			buttonRows = buttonRows + 1;
			currentRowWidth = 0;
		});
		arrSubsectionWidths.do({arg item, i;
			var itemWidth = item + gap;
			if ((currentRowWidth + itemWidth) > maxRowWidth, {
				buttonRows = buttonRows + 1;
				currentRowWidth = 0;
			});
			currentRowWidth = currentRowWidth + itemWidth;
		});
		if (currentRowWidth > 0, {
			buttonRows = buttonRows + 1;
		});
		scrollerHeight = 500 - (buttonRows * 24);
		^scrollerHeight;
	}

	///////////////

	getParentTXVSystem {
		if (parentTXVSystem.isNil or: (parentTXVSystem == 0), {
			parentTXVSystem = system.getModuleFromID(parentTXVSysModuleID);
			if (parentTXVSystem == 0, {
				parentTXVSystem = nil;
			});
		});
		^parentTXVSystem;
	}

	registerWithParent {
		var parent = this.getParentTXVSystem;
		if (parent.notNil && (parent != 0), {
			parent.registerChildModule(this);
		}, {
			"TXV_Module::registerWithParent - ERROR: parent not found.============".postln;
		});
	}

	deregisterWithParent {
		var parent = this.getParentTXVSystem;
		if (parent.notNil && (parent != 0), {
			parent.deregisterChildModule(this);
		}, {
			"TXV_Module::deregisterWithParent - ERROR: parent not found.".postln;
		});
	}

	///////////////

	/*
	// OLD:
	oscActivate {
		var newResp, holdAddress;
		{ // defer to allow for building
			//	remove any previous responders
			this.oscDeactivate;
			// add new responders for arrSynthModParameters
			data['arrSynthModParameters'].do({arg item, i;
				newResp = OSCFunc({arg msg, time, addr,  recvPort;
					holdAddress = data['arrSynthModParameters'][i][1];
					("TXV_Module.oscActivate address:  " ++ holdAddress).postln;

					if (msg[2] == arrSendTrigIDs[i],{
						/* PARAMETER: [parameterType, address, val, ?min, ?max] */
						holdAddress = data['arrSynthModParameters'][i][1];
						parentTXVSystem.sendParameterToTXV(txvModuleID, holdAddress, msg[3]);
					});
				}, '/tr', system.server.addr);
				arrPResponders = arrPResponders.add(newResp);
			});
			this.oscActivateExtraActions;
		}.defer(0.5);
	}
	*/
	// NEW: - more effecient - only 1 OSCFunc
	oscActivate {
		var newResp;
		{ // defer to allow for building
			//	remove any previous responder
			this.oscDeactivate;
			// add new responder

			// data['arrSynthModParameters'].do({arg item, i;
			// });
			newResp = OSCFunc({arg msg, time, addr,  recvPort;
				var holdAddress;
				var holdIndex = arrSendTrigIDs.indexOf(msg[2]);
				if (holdIndex.notNil, {
					holdAddress = data['arrSynthModParameters'][holdIndex][1];
					parentTXVSystem.sendParameterToTXV(txvModuleID, holdAddress, msg[3]);
				});
			}, '/tr', system.server.addr);
			arrPResponders = arrPResponders.add(newResp);
			this.oscActivateExtraActions;
		}.defer(0.5);
	}

	oscActivateExtraActions {
		//	can override in subclasses
	}

	oscDeactivate {
		//	remove responders
		arrPResponders.do({arg item, i;
			item.free;
		});
		this.oscDeactivateExtraActions;
	}
	oscDeactivateExtraActions {
		//	can override in subclasses
	}

	///////////////

	sendModuleParmToTXV {arg argAddress, argValue;
		parentTXVSystem.sendParameterToTXV(txvModuleID, argAddress.asString, argValue);

		// // testing xxx - print sent parameters
		// "TXV_Module :: sendModuleParmToTXV(txvModuleID,  argAddress, argValue)".postln;
		// ("txvModuleID:  " ++ txvModuleID ++ "   argAddress:  " ++ argAddress ++
		// "   argValue:  " ++ argValue.asString).postln;
	}

	sendCurrentValues {
		if (parentTXVSystem.notNil && (parentTXVSystem != 0), {
			dictCurrentParameterVals.keysValuesDo({arg key, value;
				parentTXVSystem.sendParameterToTXV(txvModuleID, key.asString, value);
			});
		});
	}

	/*	// TESTING XXX

	// OLD CODE:

	runAction {
	super.runAction;
	if (txvFileName != "", {
	holdTXVView.start;
	});
	}

	pauseAction {
	super.pauseAction;
	if (txvFileName != "", {
	holdTXVView.stop;
	});
	}
	*/

	//////////////

	openGui{ arg argParent; 			 // override base class
		//	use base class initialise
		this.baseOpenGui(this, argParent);
		if (holdScrollView.notNil, {
			{holdScrollView.visibleOrigin = (data['visibleOrigins'][displayOption.asSymbol]);
			}.defer(0.05)
		});
	}

	extraSaveData {
		^[dictCurrentParameterVals.asArgsArray, parentTXVSysModuleID, txvModuleID, txvModuleName, txvModuleType,
			canDraw, canDrawLayers, numModInputs, numModOutputs, hasTexture, this.extraSubModuleData];
	}

	// override base class
	loadPresetData { arg argPresetData;
		this.loadData(argPresetData, true);
	}

	loadExtraData {arg argData, isPresetData = false;  // override base class
		var partKey, partKeySize;
		argData.at(0).asArray.pairsDo({arg key, val;
			if (dictCurrentParameterVals.at(key).notNil, {
				// adjust asset data - bank is missing in older files
				partKeySize = "assetSlot/".size;
				partKey = key.asString.deepCopy.keep(partKeySize);
				if (partKey == "assetSlot/" and: {val.isArray.not}, {
					dictCurrentParameterVals.put(key, [val, 0]);
				}, {
					dictCurrentParameterVals.put(key, val);
				});
			});
		});
		if (isPresetData == false, {
			parentTXVSysModuleID = argData.at(1);
			parentTXVSystem = system.getModuleFromID(parentTXVSysModuleID);
			txvModuleID = argData.at(2);
			txvModuleName = argData.at(3);
			txvModuleType = this.moduleTypeLegacyCheck(argData.at(4));
			canDraw = argData.at(5);
			canDrawLayers = argData.at(6);
			numModInputs = argData.at(7);
			numModOutputs = argData.at(8);
			hasTexture = argData.at(9) ? 0;
		});
		this.loadExtraSubModuleData(argData.at(10) ? [], isPresetData);

		if (isPresetData == false, {
			displayOption = this.getInitialDisplayOption;
			this.buildGuiSpecArray;
			this.setActionSpecs;
			this.oscActivate;
			this.registerWithParent;
		});
		{this.sendCurrentValues;}.defer(0.5);
	}

	moduleTypeLegacyCheck {arg argType;
		var outType = argType;
		case
		{argType == "TXDraw3DShape"} {outType =  "TXDrawShape3D"}
		{argType == "TXDraw3DShapeMulti"} {outType =  "TXDrawShape3DMulti"}
		;
		^outType;
	}

	// override as needed
	extraSubModuleData {
		^[];
	}

	// override as needed
	loadExtraSubModuleData {arg argData, isPresetData = false;
		//
	}

	initialiseCurveData {
		data.dictCurveDataEvents = ();
		this.getTXVParameters.do({arg item, i;
			if (item[0] == "float256", {
				data.dictCurveDataEvents[item[1].asSymbol] = ();
				data.dictCurveDataEvents[item[1].asSymbol].gridRows = 2;
				data.dictCurveDataEvents[item[1].asSymbol].gridCols = 2;
			});
		});
	}

	pasteFromClipboard { arg includePresets = true; // override base class
		var holdData;
		holdData = system.getModuleClipboard(this.class).deepCopy;
		// remove arrPresets from data if required
		if (includePresets == false, {holdData[13] = nil});
		if (holdData.notNil, {this.loadPresetData(holdData)});
	}

	deleteModule {   // override base class
		// parent txv system manages deletions
		parentTXVSystem.deleteTXVModule(this);
	}

	finalDelete {
		// this is only called by parent txv system
		super.deleteModule;
	}

	deleteModuleExtraActions {
		//	remove responders
		this.oscDeactivate;
		this.deregisterWithParent;
	}

	// override as needed
	setActionSpecs {
		arrActionSpecs = TXV_BuildActionSpecs.from(this);
	}

	//////////////

	restoreDefaultParameter {arg parameterName;
		var parm;
		parm = this.getTXVParameters.select({arg item, i; item[1] == parameterName}).first;
		case
		/* PARAMETER: [parameterType, address, val, ?min, ?max] */
		// for valid parameters, add value to dict with address as key
		{parm[0].keep(10) == "assetSlot/"} {this.setDefaultParameter(parameterName, [0, 0]);}
		{parm[0] == "extImageModuleSlot"} {this.setDefaultParameter(parameterName, 0);}
		{parm[0] == "int"} {this.setDefaultParameter(parameterName, parm[2].asInteger);}
		{parm[0] == "float"} {this.setDefaultParameter(parameterName, parm[2]);}
		{parm[0] == "float256"} {this.setDefaultParameter(parameterName, parm[2..(2+255)]);}
		{parm[0] == "bool_int"} {this.setDefaultParameter(parameterName, parm[2].asInteger);}
		{parm[0] == "string"} {this.setDefaultParameter(parameterName, parm[2]);}
		/* GROUP:     [groupType, address, parameters] */
		// for groups, recursively run this function
		{parm[2].isArray} {
			parm[2].do({arg item, i;
				case
				/* PARAMETER: [parameterType, address, val, ?min, ?max] */
				// for valid parameters, add value to dict with address as key
				{item[0] == "int"} {this.setDefaultParameter(item[1], item[2]);}
				{item[0] == "float"} {this.setDefaultParameter(item[1], item[2]);}
				{item[0] == "bool_int"} {this.setDefaultParameter(item[1], item[2]);}
				;
			});
		}
		;
		system.showView;
	}

	setDefaultParameter {arg argAddress, argValue;
		this.dictCurrentParameterVals[argAddress.asSymbol] = argValue;
		this.sendModuleParmToTXV( argAddress, argValue);
	}

}
