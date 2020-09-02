// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

/*


     NOTE - THIS IS OLD CODE - MAY NOT NEED TO HAVE TXVModule, just TXVSystem, TXVLFO, ... etc.



/* ---------- TXV Settable Parameter list for: TXLFO_1 ---------- */
/* GROUP:     [groupType, address, parameters] */
/* PARAMETER: [parameterType, address, val, ?min, ?max] */
[ "modParameterGroupInt", "/TXLFO_1/lfoType", [
    ["int", "/TXLFO_1/lfoType/fixedValue", 0, 0, 12],
    ["float", "/TXLFO_1/lfoType/fixedModMix", 0, 0, 1],
    ["bool_int", "/TXLFO_1/lfoType/useExtMod", 0],
    ["float", "/TXLFO_1/lfoType/extModValue", 0, 0, 1],
    ["int", "/TXLFO_1/lfoType/softMin", 0, 0, 12],
    ["int", "/TXLFO_1/lfoType/softMax", 12, 0, 12],
]],
[ "modParameterGroupFloat", "/TXLFO_1/bpm", [
    ["float", "/TXLFO_1/bpm/fixedValue", 120, 0.1, 200],
    ["float", "/TXLFO_1/bpm/fixedModMix", 0, 0, 1],
    ["bool_int", "/TXLFO_1/bpm/useExtMod", 0],
    ["float", "/TXLFO_1/bpm/extModValue", 0, 0, 1],
    ["float", "/TXLFO_1/bpm/softMin", 0.1, 0.001, 600],
    ["float", "/TXLFO_1/bpm/softMax", 200, 0.001, 600],
]],
[ "modParameterGroupFloat", "/TXLFO_1/cycleTimeBeats", [
    ["float", "/TXLFO_1/cycleTimeBeats/fixedValue", 1, 1, 64],
    ["float", "/TXLFO_1/cycleTimeBeats/fixedModMix", 0, 0, 1],
    ["bool_int", "/TXLFO_1/cycleTimeBeats/useExtMod", 0],
    ["float", "/TXLFO_1/cycleTimeBeats/extModValue", 0, 0, 1],
    ["float", "/TXLFO_1/cycleTimeBeats/softMin", 1, 0.01, 1000],
    ["float", "/TXLFO_1/cycleTimeBeats/softMax", 64, 0.01, 1000],
]],
[ "modParameterGroupFloat", "/TXLFO_1/cycleTimeDivisor", [
    ["float", "/TXLFO_1/cycleTimeDivisor/fixedValue", 1, 1, 64],
    ["float", "/TXLFO_1/cycleTimeDivisor/fixedModMix", 0, 0, 1],
    ["bool_int", "/TXLFO_1/cycleTimeDivisor/useExtMod", 0],
    ["float", "/TXLFO_1/cycleTimeDivisor/extModValue", 0, 0, 1],
    ["float", "/TXLFO_1/cycleTimeDivisor/softMin", 1, 0.01, 1000],
    ["float", "/TXLFO_1/cycleTimeDivisor/softMax", 64, 0.01, 1000],
]],
[ "modParameterGroupFloat", "/TXLFO_1/phaseOffset", [
    ["float", "/TXLFO_1/phaseOffset/fixedValue", 0, 0, 1],
    ["float", "/TXLFO_1/phaseOffset/fixedModMix", 0, 0, 1],
    ["bool_int", "/TXLFO_1/phaseOffset/useExtMod", 0],
    ["float", "/TXLFO_1/phaseOffset/extModValue", 0, 0, 1],
    ["float", "/TXLFO_1/phaseOffset/softMin", 0, 0, 1],
    ["float", "/TXLFO_1/phaseOffset/softMax", 1, 0, 1],
]],
[ "modParameterGroupFloat", "/TXLFO_1/pulseWidth", [
    ["float", "/TXLFO_1/pulseWidth/fixedValue", 0.5, 0, 1],
    ["float", "/TXLFO_1/pulseWidth/fixedModMix", 0, 0, 1],
    ["bool_int", "/TXLFO_1/pulseWidth/useExtMod", 0],
    ["float", "/TXLFO_1/pulseWidth/extModValue", 0, 0, 1],
    ["float", "/TXLFO_1/pulseWidth/softMin", 0, 0, 1],
    ["float", "/TXLFO_1/pulseWidth/softMax", 1, 0, 1],
]],
[ "modParameterGroupBool", "/TXLFO_1/useMasterBpm", [
    ["bool_int", "/TXLFO_1/useMasterBpm/fixedValue", 0],
    ["bool_int", "/TXLFO_1/useMasterBpm/fixedModMix", 0],
    ["bool_int", "/TXLFO_1/useMasterBpm/useExtMod", 0],
    ["float", "/TXLFO_1/useMasterBpm/extModValue", 0, 0, 1],
]],
[ "modParameterGroupBool", "/TXLFO_1/frozen", [
    ["bool_int", "/TXLFO_1/frozen/fixedValue", 0],
    ["bool_int", "/TXLFO_1/frozen/fixedModMix", 0],
    ["bool_int", "/TXLFO_1/frozen/useExtMod", 0],
    ["float", "/TXLFO_1/frozen/extModValue", 0, 0, 1],
]],
/* ---------- TXV Settable Parameter list for: TXDrawCircles_1 ---------- */
/* GROUP:     [groupType, address, parameters] */
/* PARAMETER: [parameterType, address, val, ?min, ?max] */
[ "modParameterGroupFloat", "/TXDrawCircles_1/size", [
    ["float", "/TXDrawCircles_1/size/fixedValue", 0.2, 0, 1],
    ["float", "/TXDrawCircles_1/size/fixedModMix", 0, 0, 1],
    ["bool_int", "/TXDrawCircles_1/size/useExtMod", 0],
    ["float", "/TXDrawCircles_1/size/extModValue", 0, 0, 1],
    ["float", "/TXDrawCircles_1/size/softMin", 0, 0, 1],
    ["float", "/TXDrawCircles_1/size/softMax", 1, 0, 1],
]],
[ "modParameterGroupInt", "/TXDrawCircles_1/numCircles", [
    ["int", "/TXDrawCircles_1/numCircles/fixedValue", 4, 1, 20],
    ["float", "/TXDrawCircles_1/numCircles/fixedModMix", 0, 0, 1],
    ["bool_int", "/TXDrawCircles_1/numCircles/useExtMod", 0],
    ["float", "/TXDrawCircles_1/numCircles/extModValue", 0, 0, 1],
    ["int", "/TXDrawCircles_1/numCircles/softMin", 1, 1, 1000],
    ["int", "/TXDrawCircles_1/numCircles/softMax", 20, 1, 1000],
]],
[ "modParameterGroupFloat", "/TXDrawCircles_1/positionX", [
    ["float", "/TXDrawCircles_1/positionX/fixedValue", 0.5, 0, 1],
    ["float", "/TXDrawCircles_1/positionX/fixedModMix", 0.5, 0, 1],
    ["bool_int", "/TXDrawCircles_1/positionX/useExtMod", 0],
    ["float", "/TXDrawCircles_1/positionX/extModValue", 0, 0, 1],
    ["float", "/TXDrawCircles_1/positionX/softMin", 0, 0, 1],
    ["float", "/TXDrawCircles_1/positionX/softMax", 1, 0, 1],
]],
[ "modParameterGroupFloat", "/TXDrawCircles_1/positionY", [
    ["float", "/TXDrawCircles_1/positionY/fixedValue", 0.5, 0, 1],
    ["float", "/TXDrawCircles_1/positionY/fixedModMix", 0.5, 0, 1],
    ["bool_int", "/TXDrawCircles_1/positionY/useExtMod", 0],
    ["float", "/TXDrawCircles_1/positionY/extModValue", 0, 0, 1],
    ["float", "/TXDrawCircles_1/positionY/softMin", 0, 0, 1],
    ["float", "/TXDrawCircles_1/positionY/softMax", 1, 0, 1],
]],
[ "modParameterGroupFloat", "/TXDrawCircles_1/colorH", [
    ["float", "/TXDrawCircles_1/colorH/fixedValue", 0.5, 0, 1],
    ["float", "/TXDrawCircles_1/colorH/fixedModMix", 0, 0, 1],
    ["bool_int", "/TXDrawCircles_1/colorH/useExtMod", 0],
    ["float", "/TXDrawCircles_1/colorH/extModValue", 0, 0, 1],
    ["float", "/TXDrawCircles_1/colorH/softMin", 0, 0, 1],
    ["float", "/TXDrawCircles_1/colorH/softMax", 1, 0, 1],
]],
[ "modParameterGroupFloat", "/TXDrawCircles_1/colorS", [
    ["float", "/TXDrawCircles_1/colorS/fixedValue", 0.6, 0, 1],
    ["float", "/TXDrawCircles_1/colorS/fixedModMix", 0, 0, 1],
    ["bool_int", "/TXDrawCircles_1/colorS/useExtMod", 0],
    ["float", "/TXDrawCircles_1/colorS/extModValue", 0, 0, 1],
    ["float", "/TXDrawCircles_1/colorS/softMin", 0, 0, 1],
    ["float", "/TXDrawCircles_1/colorS/softMax", 1, 0, 1],
]],
[ "modParameterGroupFloat", "/TXDrawCircles_1/colorB", [
    ["float", "/TXDrawCircles_1/colorB/fixedValue", 0.5, 0, 1],
    ["float", "/TXDrawCircles_1/colorB/fixedModMix", 0, 0, 1],
    ["bool_int", "/TXDrawCircles_1/colorB/useExtMod", 0],
    ["float", "/TXDrawCircles_1/colorB/extModValue", 0, 0, 1],
    ["float", "/TXDrawCircles_1/colorB/softMin", 0, 0, 1],
    ["float", "/TXDrawCircles_1/colorB/softMax", 1, 0, 1],
]],
[ "modParameterGroupFloat", "/TXDrawCircles_1/colorA", [
    ["float", "/TXDrawCircles_1/colorA/fixedValue", 1, 0, 1],
    ["float", "/TXDrawCircles_1/colorA/fixedModMix", 0, 0, 1],
    ["bool_int", "/TXDrawCircles_1/colorA/useExtMod", 0],
    ["float", "/TXDrawCircles_1/colorA/extModValue", 0, 0, 1],
    ["float", "/TXDrawCircles_1/colorA/softMin", 0, 0, 1],
    ["float", "/TXDrawCircles_1/colorA/softMax", 1, 0, 1],
]],

*/

TXV_Module_OLD : TXModuleBase {	// remote for TXV
	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar <guiWidth=500;
	classvar data;

	// testing
	classvar arrPStrings, arrModPStrings, arrActivePStrings;
	classvar maxParameters = 80;	// allow for 80 input parameters for a TXV module

	var	<>txvFileNameView;
	var	<>txvFileName = "";
	var	arrPResponders;
	var	holdTXVWindow, holdTXVView;
	var arrSendTrigIDs, arrTXVArgData, arrNumArgNames;
	var displayOption;
	var holdVisibleOrigin;
	var holdScrollView;

	// testing
	var arrInputs;
	var	holdQCWindow, holdQCView;

	*getTXVParameters{
		[
			/* ---------- TXV Settable Parameter list for: TXLFO_1 ---------- */
			/* GROUP:     [groupType, groupName, parameters] */
			/* PARAMETER: [parameterType, address, val, ?min, ?max] */
			[ "modParameterGroupInt", "lfoType", [
				["int", "/TXLFO_1/lfoType/fixedValue", 0, 0, 12],
				["int", "/TXLFO_1/lfoType/softMin", 0, 0, 12],
				["int", "/TXLFO_1/lfoType/softMax", 12, 0, 12],
				["float", "/TXLFO_1/lfoType/fixedModMix", 0, 0, 1],
			]],
			[ "modParameterGroupFloat", "bpm", [
				["float", "/TXLFO_1/bpm/fixedValue", 120, 0.1, 200],
				["float", "/TXLFO_1/bpm/softMin", 0.1, 0.001, 600],
				["float", "/TXLFO_1/bpm/softMax", 200, 0.001, 600],
				["float", "/TXLFO_1/bpm/fixedModMix", 0, 0, 1],
			]],
			[ "modParameterGroupFloat", "cycleTimeBeats", [
				["float", "/TXLFO_1/cycleTimeBeats/fixedValue", 1, 1, 64],
				["float", "/TXLFO_1/cycleTimeBeats/softMin", 1, 0.01, 1000],
				["float", "/TXLFO_1/cycleTimeBeats/softMax", 64, 0.01, 1000],
				["float", "/TXLFO_1/cycleTimeBeats/fixedModMix", 0, 0, 1],
			]],
			[ "modParameterGroupFloat", "cycleTimeDivisor", [
				["float", "/TXLFO_1/cycleTimeDivisor/fixedValue", 1, 1, 64],
				["float", "/TXLFO_1/cycleTimeDivisor/softMin", 1, 0.01, 1000],
				["float", "/TXLFO_1/cycleTimeDivisor/softMax", 64, 0.01, 1000],
				["float", "/TXLFO_1/cycleTimeDivisor/fixedModMix", 0, 0, 1],
			]],
			[ "modParameterGroupFloat", "phaseOffset", [
				["float", "/TXLFO_1/phaseOffset/fixedValue", 0, 0, 1],
				["float", "/TXLFO_1/phaseOffset/softMin", 0, 0, 1],
				["float", "/TXLFO_1/phaseOffset/softMax", 1, 0, 1],
				["float", "/TXLFO_1/phaseOffset/fixedModMix", 0, 0, 1],
			]],
			[ "modParameterGroupFloat", "pulseWidth", [
				["float", "/TXLFO_1/pulseWidth/fixedValue", 0.5, 0, 1],
				["float", "/TXLFO_1/pulseWidth/softMin", 0, 0, 1],
				["float", "/TXLFO_1/pulseWidth/softMax", 1, 0, 1],
				["float", "/TXLFO_1/pulseWidth/fixedModMix", 0, 0, 1],
			]],
			[ "modParameterGroupBool", "useMasterBpm", [
				["bool_int", "/TXLFO_1/useMasterBpm/fixedValue", 0],
				["bool_int", "/TXLFO_1/useMasterBpm/fixedModMix", 0],
			]],
			[ "modParameterGroupBool", "frozen", [
				["bool_int", "/TXLFO_1/frozen/fixedValue", 0],
				["bool_int", "/TXLFO_1/frozen/fixedModMix", 0],
			]],
		];
	}

	*initClass{
		arrInstances = [];
		data = ();
		//	set class specific variables
		defaultName = "TXV Module";
		moduleRate = "control";
		moduleType = "action";
		noInChannels = 0;
		noOutChannels = 0;
		arrOutBusSpecs = [];

		data["arrParameters"] = this.getTXVParameters;
		data["arrTXVArgData"] = [
			//  argDataType can be: [0.float, 1.integer, 2.booleanNum(0/1)],  3.string,
			//  array of :  argDataType, argMin, argMax, argNumVal, argStringVal
			// e.g. [0, 0, 1, 0.5, ""],
		];

		data["maxParameters"] = data["arrParameters"].size;

		data["arrPStrings"] = data["maxParameters"].collect({arg item; "p" ++ ("00" ++ item.asString).keep(-3)});
		data["arrModPStrings"] = arrPStrings.deepCopy.collect({arg item, i; "mod" ++ item});
		data["arrActivePStrings"] = arrPStrings.deepCopy.collect({arg item, i; "i_active" ++ item});
		arrCtlSCInBusSpecs = data["arrModPStrings"].deepCopy.collect({arg item, i; [item.asString, 1, item, 0]});

		// CODE FROM TXQCPlayer4:::::::::
		/*
		this.buildGuiSpecArray;
		this.setActionSpecs;
		// replace names in arrCtlSCInBusChoices
		arrInputs.do({arg item, i;
			var holdString, holdInputVal;
			holdInputVal = holdQCView.getInputValue(item);
			// only use if Float /Integer /True /False
			if ((holdInputVal.class == Float) or: (holdInputVal.class == Integer)
				or:(holdInputVal.class == True) or: (holdInputVal.class == False) , {
					holdString = item.asString;
				}, {
					holdString = "(not used"; // ) dummy close bracket
			});
			this.arrCtlSCInBusChoices.at(i).put(0, holdString);
		});
		*/
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showGlobalSettings";
		arrInputs = [];
		holdVisibleOrigin = Point.new(0,0);

		arrSendTrigIDs = [];
		// create unique ids
		data["maxParameters"].do({arg item, i;
			arrSendTrigIDs = arrSendTrigIDs.add(UniqueID.next);
		});
		arrSynthArgSpecs =
		[	["out", 0, 0],
		]
		++ data["arrActivePStrings"].collect({arg item, i; [item, 0, 0]})
		++ data["arrPStrings"].collect({arg item, i; [item, 0, 0]})
		++ data["arrModPStrings"].collect({arg item, i; [item, 0, 0]});

		synthDefFunc = {
			arg out,
			i_activep000, i_activep001, i_activep002, i_activep003, i_activep004, i_activep005, i_activep006, i_activep007, i_activep008, i_activep009, i_activep010, i_activep011, i_activep012, i_activep013, i_activep014, i_activep015, i_activep016, i_activep017, i_activep018, i_activep019, i_activep020, i_activep021, i_activep022, i_activep023, i_activep024, i_activep025, i_activep026, i_activep027, i_activep028, i_activep029, i_activep030, i_activep031, i_activep032, i_activep033, i_activep034, i_activep035, i_activep036, i_activep037, i_activep038, i_activep039, i_activep040, i_activep041, i_activep042, i_activep043, i_activep044, i_activep045, i_activep046, i_activep047, i_activep048, i_activep049, i_activep050, i_activep051, i_activep052, i_activep053, i_activep054, i_activep055, i_activep056, i_activep057, i_activep058, i_activep059, i_activep060, i_activep061, i_activep062, i_activep063, i_activep064, i_activep065, i_activep066, i_activep067, i_activep068, i_activep069, i_activep070, i_activep071, i_activep072, i_activep073, i_activep074, i_activep075, i_activep076, i_activep077, i_activep078, i_activep079,
			p000, p001, p002, p003, p004, p005, p006, p007, p008, p009, p010, p011, p012, p013, p014, p015, p016, p017, p018, p019, p020, p021, p022, p023, p024, p025, p026, p027, p028, p029, p030, p031, p032, p033, p034, p035, p036, p037, p038, p039, p040, p041, p042, p043, p044, p045, p046, p047, p048, p049, p050, p051, p052, p053, p054, p055, p056, p057, p058, p059, p060, p061, p062, p063, p064, p065, p066, p067, p068, p069, p070, p071, p072, p073, p074, p075, p076, p077, p078, p079,
			modp000, modp001, modp002, modp003, modp004, modp005, modp006, modp007, modp008, modp009, modp010, modp011, modp012, modp013, modp014, modp015, modp016, modp017, modp018, modp019, modp020, modp021, modp022, modp023, modp024, modp025, modp026, modp027, modp028, modp029, modp030, modp031, modp032, modp033, modp034, modp035, modp036, modp037, modp038, modp039, modp040, modp041, modp042, modp043, modp044, modp045, modp046, modp047, modp048, modp049, modp050, modp051, modp052, modp053, modp054, modp055, modp056,  modp057, modp058, modp059, modp060, modp061, modp062, modp063, modp064, modp065, modp066, modp067, modp068, modp069, modp070, modp071, modp072, modp073, modp074, modp075, modp076, modp077, modp078, modp079;

			var arrParmArgs, arrModParmArgs, arrActiveArgs;
			var arrParmSums, arrTrigs, arrSendTrigs, imp;

			arrParmArgs = [
				p000, p001, p002, p003, p004, p005, p006, p007, p008, p009, p010, p011, p012, p013, p014, p015, p016, p017, p018, p019, p020, p021, p022, p023, p024, p025, p026, p027, p028, p029, p030, p031, p032, p033, p034, p035, p036, p037, p038, p039, p040, p041, p042, p043, p044, p045, p046, p047, p048, p049, p050, p051, p052, p053, p054, p055, p056, p057, p058, p059, p060, p061, p062, p063, p064, p065, p066, p067, p068, p069, p070, p071, p072, p073, p074, p075, p076, p077, p078, p079];

			arrModParmArgs = [
				modp000, modp001, modp002, modp003, modp004, modp005, modp006, modp007, modp008, modp009, modp010, modp011, modp012, modp013, modp014, modp015, modp016, modp017, modp018, modp019, modp020, modp021, modp022, modp023, modp024, modp025, modp026, modp027, modp028, modp029, modp030, modp031, modp032, modp033, modp034, modp035, modp036, modp037, modp038, modp039, modp040, modp041, modp042, modp043, modp044, modp045, modp046, modp047, modp048, modp049, modp050, modp051, modp052, modp053, modp054, modp055, modp056,  modp057, modp058, modp059, modp060, modp061, modp062, modp063, modp064, modp065, modp066, modp067, modp068, modp069, modp070, modp071, modp072, modp073, modp074, modp075, modp076, modp077, modp078, modp079];

			arrActiveArgs = [
				i_activep000, i_activep001, i_activep002, i_activep003, i_activep004, i_activep005, i_activep006, i_activep007, i_activep008, i_activep009, i_activep010, i_activep011, i_activep012, i_activep013, i_activep014, i_activep015, i_activep016, i_activep017, i_activep018, i_activep019, i_activep020, i_activep021, i_activep022, i_activep023, i_activep024, i_activep025, i_activep026, i_activep027, i_activep028, i_activep029, i_activep030, i_activep031, i_activep032, i_activep033, i_activep034, i_activep035, i_activep036, i_activep037, i_activep038, i_activep039, i_activep040, i_activep041, i_activep042, i_activep043, i_activep044, i_activep045, i_activep046, i_activep047, i_activep048, i_activep049, i_activep050, i_activep051, i_activep052, i_activep053, i_activep054, i_activep055, i_activep056, i_activep057, i_activep058, i_activep059, i_activep060, i_activep061, i_activep062, i_activep063, i_activep064, i_activep065, i_activep066, i_activep067, i_activep068, i_activep069, i_activep070, i_activep071, i_activep072, i_activep073, i_activep074, i_activep075, i_activep076, i_activep077, i_activep078, i_activep079];

			arrParmSums = arrParmArgs.collect({arg item, i;
				arrActiveArgs.at(i) * (item + arrModParmArgs.at(i)).max(0).min(1);
			});

			// testing - try lower data rate for lower cpu load on complex quartz comps
			imp = (1 - Impulse.kr(30));
			//		imp = (1 - Impulse.kr(20));
			//		imp = (1 - Impulse.kr(10));

			arrTrigs = arrParmSums.collect({arg item, i;
				Trig.kr(imp * HPZ1.kr(item).abs, 0.02);
			});

			arrSendTrigs = arrTrigs.collect({arg item, i;
				SendTrig.kr(Impulse.kr(1) + item, arrSendTrigIDs.at(i), arrParmSums.at(i));
			});

			// Note this synth doesn't need to write to the output bus
		};
		// End of synth def function

		this.buildGuiSpecArray;
		this.setActionSpecs;

		// initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
		this.oscActivate;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["SpacerLine", 1],
			["ActionButton", "Global Settings", {displayOption = "showGlobalSettings";
				this.buildGuiSpecArray; system.showView;}, 160,
				TXColor.white, this.getButtonColour(displayOption == "showGlobalSettings")],
			["Spacer", 3],
			["ActionButton", "Controls", {displayOption = "showAllControls";
				this.buildGuiSpecArray; system.showView;}, 160,
				TXColor.white, this.getButtonColour(displayOption == "showAllControls")],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showGlobalSettings", {
			guiSpecArray = guiSpecArray ++[
				// ["TXCheckBox", "Show Quartz Window", "i_showQuartzWindow",
				// {this.showOrHideTXVScreen; }, 200, 40],
				["SpacerLine", 8],
			];
		});

		if (displayOption == "showAllControls", {
			guiSpecArray = guiSpecArray = guiSpecArray ++ [
				["TXVArgGuiScroller", {arrInputs}, {data["arrPStrings"]}, {data["arrActivePStrings"]}, {data["arrTXVArgData"]},
					{arg item; this.sendArgValue(item);},
					{arg view; holdScrollView = view;},
					{arg view; holdVisibleOrigin = view.visibleOrigin; },
				]
			];
		});
	}

	setActionSpecs {
		var arrSpecs;

		arrSpecs = [
			// ["TXCheckBox", "Show Quartz Window", "i_showQuartzWindow",
			// {this.showOrHideTXVScreen; }, 200, 40],
		];

		// TXVArgGui arguments:
		// 	index1 is text
		//	index2 is synth arg name to be updated for the number
		//	index3 is synth arg name to be updated for the active number setting
		// 	index4 is array of all module arguments
		// 	index5 is argument index no
		// 	index6 is set argument value function
		// e.g. ["TXVArgGui", "Particle Hue", "p003", "i_active003", data["arrTXVArgData"], 4, setArgValFunc],
		arrInputs.do({arg item, i;
			arrSpecs = arrSpecs.add( ["TXVArgGui", arrInputs.at(i).asString, data["arrPStrings"].at(i),
				data["arrActivePStrings"].at(i), data["arrTXVArgData"], i, {this.sendArgValue(i);}]);
		});
		arrActionSpecs = this.buildActionSpecs(arrSpecs);
	}

	getButtonColour { arg colour2Boolean;
		if (colour2Boolean == true, {
			^TXColor.sysGuiCol4;
			},{
				^TXColor.sysGuiCol1;
		});
	}

	oscActivate {
		{
			//	remove any previous responders and add new
			this.oscDeactivate;
			arrInputs.do({arg item, i;
				var newResp, holdArgType, holdMin, holdMax, holdVal;

				//	only build responders for active inputs
				holdArgType = data["arrTXVArgData"].at(i).at(0);

				if ( (holdArgType == 1) or: (holdArgType == 2) or: (holdArgType == 4), {
					newResp = OSCFunc({arg msg, time, addr,  recvPort;
						if (msg[2] == arrSendTrigIDs.at(i),{
							{	holdMin = data["arrTXVArgData"].at(i).at(2);
								holdMax = data["arrTXVArgData"].at(i).at(3);
								if (holdArgType == 1, {
									holdVal =  holdMin + (msg[3] * (holdMax - holdMin));
								});
								if (holdArgType == 2, {
									holdVal =  (holdMin + (msg[3] * (holdMax - holdMin))).round;
								});
								if (holdArgType == 4, {
									// convert to value to boolean
									holdVal =  (msg[3].round > 0 );
								});
								if (holdTXVView.notNil,  {
									holdTXVView.setInputValue(arrInputs.at(i), holdVal);
								});
							}.defer;
						});
					}, '/tr', system.server.addr);
					arrPResponders = arrPResponders.add(newResp);
				});
			});
		}.defer(0.5); // defer to allow for building
	}

	oscDeactivate {
		//	remove responders
		arrPResponders.do({arg item, i;
			item.free;
		});
	}

	extraSaveData {
		^[data["arrTXVArgData"]];
	}

	loadExtraData {arg argData;  // override default method
		data["arrTXVArgData"] = argData.at(0);
		this.buildGuiSpecArray;
		this.setActionSpecs;
		this.oscActivate;
		{this.sendCurrentValues;}.defer(0.5);
	}

	deleteModuleExtraActions {
		//	remove responders
		this.oscDeactivate;
	}

	openTXVComp {arg keepSettings = false;
		var firstPath, holdData;
		holdData = ();
		if (keepSettings == true, {
			arrInputs.do({arg item, i;
				holdData[item] = data["arrTXVArgData"][i];
			});
		});
		// get path/filename
		Dialog.openPanel({ arg paths;
			firstPath = paths.at(0);
			// check for valid file
			if (this.isValidTXVFile(firstPath), {
				// assign name
				txvFileName = firstPath;
				// reset various things
				this.buildGuiSpecArray;
				this.setActionSpecs;
				{this.initialiseTXVParameters(holdData)}.defer(0.1);
				{system.showView}.defer(0.6);
			});
		}, {}, false);
	}

	initialiseTXVParameters { arg holdData;
		arrInputs.do({arg item, i;
			var holdInputVal, holdDataType, holdActiveValue, holdStringVal, holdMin, holdMax, holdNum;
			//  data["arrTXVArgData"]
			//  array of :  argDataType, argStringVal, argMin, argMax, argRed, argGreen, argBlue, argAlpha, argNumVal
			//  argDataType can be: [0.notPassed, 1.float, 2.integer, 3.string, 4.booleanNum(0/1), 5.colour(RGBA),
			//	6.Directory Name, 7.File Name],
			//   - e.g. [0, "", 0, 1, 0.5, 0.5, 0.5, 1, 0].dup(data["maxParameters"])

			holdInputVal = holdTXVView.getInputValue(item);
			holdActiveValue = 0;  // default

			if (holdInputVal.class == Float, {
				holdDataType = 1;
				holdNum = holdInputVal;
				holdMin = min(0, holdNum.floor);
				holdMax = max(1, holdNum.ceil);
				data["arrTXVArgData"] [i] [0] = holdDataType;
				data["arrTXVArgData"] [i] [2] = holdMin;
				data["arrTXVArgData"] [i] [3] = holdMax;
				data["arrTXVArgData"] [i] [8] = holdNum;
				holdActiveValue = 1;
			});
			if (holdInputVal.class == Integer, {
				holdDataType = 2;
				holdNum = holdInputVal;
				holdMin = min(0, holdNum);
				holdMax = max(1, holdNum);
				data["arrTXVArgData"] [i] [0] = holdDataType;
				data["arrTXVArgData"] [i] [2] = holdMin;
				data["arrTXVArgData"] [i] [3] = holdMax;
				data["arrTXVArgData"] [i] [8] = holdNum;
				holdActiveValue = 1;
			});
			if (holdInputVal.class == String, {
				holdDataType = 3;
				holdStringVal = holdInputVal;
				data["arrTXVArgData"] [i] [0] = holdDataType;
				data["arrTXVArgData"] [i] [1] = holdStringVal;
			});
			if ((holdInputVal.class == True) or: (holdInputVal.class == False) , {
				holdDataType = 4;
				holdNum = holdInputVal.binaryValue;
				holdMin = 0;
				holdMax = 1;
				data["arrTXVArgData"] [i] [0] = holdDataType;
				data["arrTXVArgData"] [i] [2] = holdMin;
				data["arrTXVArgData"] [i] [3] = holdMax;
				data["arrTXVArgData"] [i] [8] = holdNum;
				holdActiveValue = 1;
			});
			// if (holdInputVal.class == Color, {
			// 	holdDataType = 5;
			// 	data["arrTXVArgData"] [i] [0] = holdDataType;
			// 	data["arrTXVArgData"] [i] [4] = holdInputVal.red;
			// 	data["arrTXVArgData"] [i] [5] = holdInputVal.green;
			// 	data["arrTXVArgData"] [i] [6] = holdInputVal.blue;
			// 	data["arrTXVArgData"] [i] [7] = holdInputVal.alpha;
			// });

			this.setSynthValue(data["arrActivePStrings"].at(i), holdActiveValue);
			// replace with holdData if valid
			if (holdData.notNil and: {holdData[item].notNil}, {
				data["arrTXVArgData"] [i] = holdData[item];
			});
			this.sendArgValue(i);
		});

		// rebuild synth and activate osc
		{this.rebuildSynth;}.defer(0.4);
		{this.oscActivate;}.defer(0.4);

	}

	sendCurrentValues {
		arrInputs.do({arg item, i;
			{this.setSynthValue(data["arrPStrings"].at(i), 1);}.defer(0.35);
		});
		{ 	arrInputs.do({arg item, i;
			var holdMin, holdMax, holdNum;
			holdMin = data["arrTXVArgData"].at(i).at(2);
			holdMax = data["arrTXVArgData"].at(i).at(3);
			holdNum = data["arrTXVArgData"].at(i).at(8);
			this.setSynthValue(data["arrPStrings"].at(i), [holdMin, holdMax].asSpec.unmap(holdNum));
			});
		}.defer(0.5);

		arrInputs.do({arg item, i;
			{ this.sendArgValue(i);
			}.defer(0.6 + ((i div: 20) * 0.05));
		});

	}

	sendArgValue {arg argIndex;
		var holdArgs, holdVal;
		var argDataType, argStringVal, argMin, argMax, argRed, argGreen, argBlue, argAlpha, argNumVal;
		holdArgs = data["arrTXVArgData"].at(argIndex);
		argDataType = holdArgs.at(0);
		argStringVal = holdArgs.at(1);
		argMin = holdArgs.at(2);
		argMax = holdArgs.at(3);
		argRed = holdArgs.at(4);
		argGreen = holdArgs.at(5);
		argBlue = holdArgs.at(6);
		argAlpha = holdArgs.at(7);
		argNumVal = holdArgs.at(8);

		//  array of :  argDataType, argStringVal, argMin, argMax, argRed, argGreen, argBlue, argAlpha
		//  argDataType can be: [0.notPassed, 1.float, 2.integer, 3.string, 4.booleanNum(0/1), 5.colour(RGBA),
		//	6.Directory Name, 7.File Name],

		if (argDataType == 1, {
			this.setSynthValue(data["arrPStrings"].at(argIndex), [argMin, argMax].asSpec.unmap(argNumVal));
		});
		if (argDataType == 2, {
			this.setSynthValue(data["arrPStrings"].at(argIndex), [argMin, argMax].asSpec.unmap(argNumVal.asInteger));
		});

		if ( (argDataType == 3) or: (argDataType == 6) or: (argDataType == 7), {
			holdVal = argStringVal;
			try {
				holdTXVView.setInputValue(arrInputs.at(argIndex), holdVal);
			};
		});
		if (argDataType == 4, {
			this.setSynthValue(data["arrPStrings"].at(argIndex), argNumVal);
		});
/*
		if (argDataType == 5, {
			holdVal = Color.new(argRed, argGreen, argBlue, argAlpha);
			try {
				holdTXVView.setInputValue(arrInputs.at(argIndex), holdVal);
			};
		});
*/
	}

/*
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

	openGui{ arg argParent; 			 // override base class
		//	use base class initialise
		this.baseOpenGui(this, argParent);
		if (holdScrollView.notNil and: {holdScrollView.notClosed},
			{holdScrollView.visibleOrigin = (holdVisibleOrigin); });
	}

}


