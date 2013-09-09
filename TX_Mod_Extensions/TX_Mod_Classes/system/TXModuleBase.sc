// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXModuleBase {		// Base Class for all modules

/*
	// GENERAL CODING NOTES FOR MODULES:

	use the following code to show all instance methods:
		TXModuleBase.dumpMethodList;

	use the following code to show all class methods:
		TXModuleBase.class.dumpMethodList;

	// NOTE: derived modules should now include the following classvars:
	classvar <arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "action"/ "groupaction"/ "source"/ "groupsource" /
								//     "insert"/ "bus"/"channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar	<arrBufferSpecs;
	classvar	<guiWidth=500;		// 500 is default. make larger if needed.
*/

// ========================================================================
	// modules rates can be "audio" or "control"
	// module types can be "source", "groupsource", "insert", "bus", "channel" or "action"
	// groupsource is like a source except it is a spawning module for synths created within a group
	// action is for action modules that don't output any control signals
	// groupaction is like action except it is a spawning module for synths created within a group
	//
	//					groupsource
	//					& source	insert	bus		channel  action   (n is a number, x is an array)
	// noInChannels 		0		n		0		0         0
	// arrAudSCInBusSpecs  	x		x		-		-         -
	// arrCtlSCInBusSpecs  	x		x		-		x         x
	// noOutChannels 		n		n		n		0         0
	// arrOutBusSpecs  	x		-		x		-         -
	// synthDefFunc 		x		x		-		x         x

	// arrSynthArgSpecs 	x		x		-		x         x
	// guiSpecArray	 	x		x		-		-         x
	// arrBufferSpecs		x		x		-		-         x           where applicable
	//
	// only a (GROUP)SOURCE or INSERT module will have guiSpecArray
	// only an INSERT module will have input channels
	//  the SynthDef of an INSERT module must have an argument "in" which is the input bus index,  because
	//	methods setInputBusses and makeSynth use it to set the synth's input bus
	// an INSERT module has no arrOutBusSpecs
	//
	// a BUS module has no side-chain inputs or arrAudSCInBusSpecs/arrCtlSCInBusSpecs
	// a BUS module has no synthDef
	//
	// a CHANNEL module has no output channels or arrOutBusSpecs
	//
	//   arrAudSCInBusSpecs/arrCtlSCInBusSpecs - these consist of an array of arrays,
	//    	each with: ["Bus Name Text", no. channels, "synth arg string", optionDefault]
	//		a bus will only have 1st 2 items in arrays
	//		optionDefault can be of values:  nil (or not present),  0 or 1 (last 2  only valid for arrCtlSCInBusSpecs)
	//		  if optionDefault is nil, then control is not optional.
	//		  if 0 or 1 then it is optional with this default value.
	//    	e.g. [ ["Left channel level", 1, "modLeftLvl", 0], ["Right channel level", 1, "modRightLevel", 0] ]
	//
	//   arrOutBusSpecs - these consist of an array of arrays,
	//   	 each with: ["Bus Name Text", [array of channel numbers ]
	//	 N.B. the channel numbers must be consecutive [0] [2,3] [1,2,3,4] are all okay
	//   	 e.g. [ ["Out L + R", [0, 1]], ["Out L only", [0]], ["Out R only",[1]] ]
	//   	 e.g. [ ["Out All - 1,2,3,4", [0,1,2,3]], ["Out 1 + 2 only", [0, 1]], ["Out 3 + 4 only", [2, 3]],
	//			["Out 1 only", [0]], ["Out 2 only",[1]],["Out 3 only", [2]], ["Out 4 only",[3]] ]
	//
	//   guiSpecArray - an array of arrays,
	//    	each with: ["guitype", arg1, arg2 ... arg]
	//    	where the gui type string is interpreted by TXGuiBuild2 with the arguments given
	//
	//   arrSynthArgSpecs - an array of arrays,  for ALL synth arguments
	//    	each with: ["synth arg string", value, rate/lag time]
	//		rate/lag time - goes in the rates array sent when synthdef created
	//
	//   arrBufferSpecs  - an array of arrays,
	//    	each with:  ["synth arg string"(to assign buffer index to), numFrames, numChannels, optional numBuffers],
	//		if the optional number of buffers is given then multiple consecutive buffers are created using
	//		  the .allocConsecutive method, and the first buffer index number of the series is stored to the
	//		  synth arg string
	//
	//	arrActionSpecs  - an array of arrays which is created using:
	//		arrActionSpecs = TXBuildActions.from([ ]);
	//		where each array begins with either a "commandAction" array:
	// 			["commandAction", "action name", actionFunction, arrControlSpecFuncs (for action function args)]
	//    	or with a gui type array: ["guitype", arg1, arg2 ... arg]
	//    	where the gui type string is interpreted by TXBuildActions with the arguments given
	//		e.g. ["TXMinMaxSliderSplit", "Resonance", ControlSpec(0, 1), "res", "resMin", "resMax"],
	//
	//
// ========================================================================
//
//	 EXTRA NOTES on writing new modules:
//	=====================================
//
//	Synth arguments could include:
//		"in" - input bus, for insert modules only
//		"out" - output bus,
//		"gate" - used by spawning & midi synths
//		"note" - used by spawning & midi synths
//		"velocity" - used by spawning & midi synths
//		"modXxxxxx" for control side-channel inputs (e.g. modFreq or modLevel)
//		"dryWetMix" & "modDryWetMixMix" - for  audio insert FX modules
//
//	NOTE: all modules with audio input should be using InFeedback.ar rather than In.ar
//
// ========================================================================

	classvar <>defLagTime = 0.01;	// default lag time for controls - this value can be changed
	classvar <>system;				// system class
	classvar	<>group = 1;			// default group for adding  synths to
	classvar	<defSampleRate = 44100;	//	default sample rate. (also set in TXSystem1)

	var <arrSynthArgSpecs;		// synth arguments spec
	var <synthDefFunc; 		// synthdef function
	var <synthDefRates;	 	// synthdef rates
	var <guiSpecTitleArray; 	// array of title items for gui specs
	var <guiSpecArray; 		// array of gui specs
	var <>myArrCtlSCInBusSpecs;	// module instance version of array of control side-chain input bus specs
	var <inputBusses;			// array of input busses
	var <arrCtlSCInBusses;		// array control side-chain input busses
	var <arrAudSCInBusses;		// array audio side-chain input busses
	var <>arrCtlSCInBusChoices;	// array control side-chain input bus choices
	var <>arrAudSCInBusChoices;	// array audio side-chain input bus choices
	var arrAudSCInBusMappings;	// array audio side-chain input bus mappings
	var arrCtlSCInBusMappings;	// array control side-chain input bus mappings
	var <outBus;				// output bus
	var <arrOutBusChoices;		// output bus choices
	var <>instName;			// instance name
	var <>moduleID;			// moduleID
	var <>arrActionSpecs;		// specs for module actions
	var <>toBeDeletedStatus=false;	//
	var <>deletedStatus=false;	// delete status - only set to true during delete process
	var <>rebuiltStatus=false;
	var <w;					// gui window
	var <>arrControls;			// gui controls
	var <>arrControlVals; 		// gui control values
	var <>arrOptions;			// for storing and recalling synth options
	var <>arrOptionData;		// for holding option data used by module
	var <moduleNode; 			// module node - could be a synth or a group
	var <>moduleNodeStatus = "running"; 	// module node status - "running" or "paused"
	var groupNodes;			// used to collect all nodes in a group
	var <>groupPolyphony = 16;	// maximum polyphony for a groupsource
	var <>moduleInfoTxt=" ";	// info text for user to describe module
	var <buffers; 			// used by some modules - see methods below
	var <extraLatency=0;		// used to allow extra time for some modules to load
	var <autoModOptions = false;	// to automatically show modulation options on gui
								// n.b. always false - modoptions now automatic
	var <>arrPresets;			// presets
	var <>legacyModule = false; // to mark older legacy modules

	// layout gui vars:
	var <>posX = 0;			// x coord
	var <>posY = 0;			// y coord
	var <>highlight = false;	// for highlighting on layout

	// the following are used by midi triggered modules:
	var midiNoteStatus=false;		// can be false or true.
	var midiNotes;
	var midiNoteOnRoutine;			// no longer used
	var midiNoteOffRoutine;			// no longer used
	var midiNoteOnResp;
	var midiNoteOffResp;
	var <>midiMinChannel = 1;		// default is to allow all midi channels, notes & velocities
	var <>midiMaxChannel = 16;
	var <>midiMinNoteNo = 0;
	var <>midiMaxNoteNo = 127;
	var <>midiMinVel = 0;
	var <>midiMaxVel = 127;
	var <>midiMinControlNo = 0;
	var <>midiMaxControlNo = 127;
	var <>midiListen = 0;
	var <>midiOutPort = 0;
	var <>midiSustainPedalResp;
	var <>midiSustainPedalState = 0;
	var <>midiBendResp;
	var <>arrHeldMidiNotes;

	// N.B. this module uses TXGuiBuild2.sc to create gui.

*initClass{
	//
	//	in sub-class set class specific variables as appropriate
	//
}

*renumberInsts{ arg argModule;
	argModule.class.arrInstances.do({ arg item, i;
		//	create instance name
		item.instName = argModule.class.defaultName ++ " [" ++ (i + 1).asString ++ "]";
	});
}

baseInit {arg argModule, argInstName;
	//	add this to arrInstances
	argModule.class.arrInstances = argModule.class.arrInstances.add(this);
	//	create moduleID
	moduleID = system.nextModuleID;
	//	create instance name
	instName = argInstName ? (argModule.class.defaultName ++ " ["
		++ argModule.class.arrInstances.size.asString ++ "]");
	// create module instance version of arrCtlSCInBusSpecs
	myArrCtlSCInBusSpecs = argModule.class.arrCtlSCInBusSpecs.deepCopy;
	//	 allocate audio or control out busses
	if (argModule.class.noOutChannels > 0, {
		// check if already allocated (e.g. setting main output bus to index 0)
		if (outBus.isNil, {
			outBus = Bus.alloc(argModule.class.moduleRate.asSymbol, system.server,
				argModule.class.noOutChannels);
		});
		arrOutBusChoices = argModule.class.arrOutBusSpecs.collect({ arg item, i;
			[item.at(0),	// bus name text
				item.at(1).collect({ arg channel, i; // array of bus indices
					outBus.index + channel;
				})
			];
		});
	});
	//	 allocate audio and/or control side-chain input busses
	if (argModule.class.moduleType == "bus", {
		//  for bus modules only, input and output busses are the same
		if (argModule.class.moduleRate ==  "audio", {
			arrAudSCInBusses  = [outBus];
			arrAudSCInBusChoices = arrOutBusChoices;
		}, {
			arrCtlSCInBusses = [outBus];
			arrCtlSCInBusChoices = arrOutBusChoices;

		});
	}, {	//  for other module types allocate busses
		arrAudSCInBusses  = argModule.class.arrAudSCInBusSpecs.collect({ arg item, i;
			Bus.audio(system.server, item.at(1));
		});
		// for control buses set value to 0 when allocating
		arrCtlSCInBusses = argModule.class.arrCtlSCInBusSpecs.collect({ arg item, i;
			Bus.control(system.server, item.at(1)).value_(0);
		});
		// create bus choices from specs
		arrAudSCInBusChoices = argModule.class.arrAudSCInBusSpecs.collect({ arg item, ind;
			var holdArray;
			holdArray = [];	// create array of bus indices
			item.at(1).do({ arg channel, i;
				holdArray = holdArray.add(arrAudSCInBusses.at(ind).index + channel);
			});
			[	item.at(0),	// bus name text
				holdArray,	// array of bus indices
			];
		});
		arrCtlSCInBusChoices = argModule.class.arrCtlSCInBusSpecs.collect({ arg item, ind;
			var holdArray;
			holdArray = [];	// create array of bus indices
			item.at(1).do({ arg channel, i;
				holdArray = holdArray.add(arrCtlSCInBusses.at(ind).index + channel);
			});
			[	item.at(0),	// bus name text
				holdArray,	// array of bus indices
			];
		});
	});
	// if requested, make the gui
	if (system.autoOpen==true, {this.baseOpenGui(argModule)});
	//	store default preset
	this.storePreset(argModule, "Default Settings");
	// init array
	arrHeldMidiNotes = [];
}

////////////////////////////////////////////////////////////////////////////////////

openGui{ arg argParent; 			 // override if neccessary
	//	use base class initialise
	this.baseOpenGui(this, argParent);
}

baseOpenGui{  arg argModule, argParent;
	if (w.notNil, {
		if (w.class == Window,{
			if (w.isClosed.not,{
				w.front;
			}, {
				w = TXGuiBuild2.new(argModule, argParent);
			});
	 	}, {
			// if w.notNil and w.class not a Window then build
			w = TXGuiBuild2.new(argModule, argParent);
		});
 	}, {
		// if w.isNil then build
		w = TXGuiBuild2.new(argModule, argParent);
	});
}

closeGui {
	if (w.class == Window, {
		if ( w.isClosed.not,  {w.close});
	});
}

////////////////////////////////////////////////////////////////////////////////////

saveData {
	// this method returns an array of all data for saving with various components:
	// 0- string "TXModuleSaveData", 1- module class, 2- module ID, 3- arrSynthArgSpecs,
	// 4- arrControlVals, 5- arrOptions, 6- arrMidiData, 7- arrCtlSCInBusSpecs, 8 -extraSaveData,
	// 9-groupPolyphony, 10-moduleInfoTxt, 11-posX, 12-posY, 13-arrPresets
	var arrData, arrMidiData;
	arrMidiData = [midiMinChannel, midiMaxChannel, midiMinNoteNo, midiMaxNoteNo, midiMinVel,
		 midiMaxVel, midiMinControlNo, midiMaxControlNo, midiListen, midiOutPort];
	arrData = ["TXModuleSaveData", this.class.asString, moduleID, arrSynthArgSpecs, arrControlVals,
		arrOptions, arrMidiData, this.myArrCtlSCInBusSpecs, this.extraSaveData, groupPolyphony,
		moduleInfoTxt, posX, posY, arrPresets];
	^arrData;
}

loadData { arg arrData;
	// this method updates all data by loading arrData. format:
	// 0- string "TXModuleSaveData", 1- module class, 2- module ID, 3- arrSynthArgSpecs,
	// 4- arrControlVals, 5- arrOptions, 6- arrMidiData, 7- arrCtlSCInBusSpecs, 8 -extraSaveData,
	// 9-groupPolyphony, 10-moduleInfoTxt, 11-posX, 12-posY, 13-arrPresets
	var holdArrSynthArgSpecs, holdArrOptions, holdArrMidiData, arrMidiData, holdArrCtlSCInBusSpecs;

	// error check
	if (arrData.class != Array, {
		TXInfoScreen.new("Error: Invalid Data Type - not Array. Cannot load. [Called from "
			++ this.class.asString ++ " .loadData]");
		^0;
	});
	if (arrData.at(0) != "TXModuleSaveData", {
		TXInfoScreen.new("Error: Invalid Data String - not TXModuleSaveData. Cannot load. [Called from "
			++ this.class.asString ++ " .loadData]");
		^0;
	});
	if (arrData.at(1) != this.class.asString, {
		TXInfoScreen.new("Error: Invalid Data Class. Cannot load. [Called from " ++ this.class.asString ++ " .loadData]");
		^0;
	});
	// assign values
	holdArrSynthArgSpecs = arrData.at(3).deepCopy;
	arrControlVals = arrData.at(4).copy;
	holdArrOptions = arrData.at(5).copy;
	holdArrMidiData = arrData.at(6).deepCopy;
	holdArrCtlSCInBusSpecs = arrData.at(7).deepCopy;
	groupPolyphony = arrData.at(9).deepCopy ? 64;
	moduleInfoTxt = arrData.at(10).copy ? " ";
	posX = arrData.at(11).copy ? 0;
	posY =  arrData.at(12).copy ? 0;
	if (arrData.at(13).notNil, {
		arrPresets = arrData.at(13).deepCopy;
	});
	arrMidiData = [midiMinChannel, midiMaxChannel, midiMinNoteNo, midiMaxNoteNo, midiMinVel,
		midiMaxVel, midiMinControlNo, midiMaxControlNo, midiListen, midiOutPort ];
	if (holdArrMidiData != arrMidiData, {
		# midiMinChannel, midiMaxChannel, midiMinNoteNo, midiMaxNoteNo, midiMinVel,
			midiMaxVel, midiMinControlNo, midiMaxControlNo, midiListen, midiOutPort
		= holdArrMidiData;
	});
	// reactivate midi modules
	this.midiControlActivate;
	if (midiListen == 1, {
		this.midiNoteActivate;
	},{
		this.midiNoteDeactivate;
	});

	// if synth arg specs are different, recreate synthDef and possibly synth
	if ( (holdArrSynthArgSpecs != arrSynthArgSpecs) or: (holdArrOptions != arrOptions)
			or: (holdArrCtlSCInBusSpecs != this.myArrCtlSCInBusSpecs), {
 		// update variables
//		arrSynthArgSpecs = holdArrSynthArgSpecs;
//		arrOptions = holdArrOptions;
//		this.myArrCtlSCInBusSpecs = holdArrCtlSCInBusSpecs;
		holdArrSynthArgSpecs.do({arg item, i;
			var holdIndex;
			holdIndex = arrSynthArgSpecs.collect({arg spec; spec[0]}).indexOfEqual(item[0]);
			if (holdIndex.notNil, {
				arrSynthArgSpecs[holdIndex] = item;
			});
		});
		holdArrOptions.do({arg item, i;
			arrOptions[i] = item;
		});

		holdArrCtlSCInBusSpecs.do({arg item, i;
			var holdIndex;
			holdIndex = holdArrCtlSCInBusSpecs.collect({arg spec; spec[0]}).indexOfEqual(item[0]);
			if (holdIndex.notNil, {
				this.myArrCtlSCInBusSpecs[holdIndex] = item;
			});
		});

		// if node is a synth then recreate synth after loading synthdef.
		if (moduleNode.class == Synth, {
			this.loadAndMakeSynth;
		},{
			this.loadSynthDef;
		});
	});

	// load extra data
	this.loadExtraData(arrData.at(8).deepCopy);
}

extraSaveData {
	^nil;
	// this method is called by method .saveData and returns nil by default.
	// can be overriden in modules to return extra data to be saved with other module data.
	// loadExtraData method will also need to be overriden.
}

loadExtraData {arg argData;
	// this method is called by method .loadData and does nothing by default.
	// Can be overriden with action in modules to load extra data into module.
}

loadModuleID { arg arrData;
	// this method updates just moduleID by loading arrData. format:
	// 0- string "TXModule", 1- module class, 2- module ID, 3- arrSynthArgSpecs, 4- arrControlVals
	// error check
	if (arrData.class != Array, {
		TXInfoScreen.new("Error: invalid data. cannot load.");
		^0;
	});
	if (arrData.at(1) != this.class.asString, {
		TXInfoScreen.new("Error: invalid data class. cannot load.");
		^0;
	});
	moduleID = arrData.at(2);
}

////////////////////////////////////////////////////////////////////////////////////

copyToClipboard {
	system.setModuleClipboard(this.class, this.saveData.deepCopy);
}

pasteFromClipboard { arg includePresets = true;
	var holdData;
	holdData = system.getModuleClipboard(this.class).deepCopy;
	// remove arrPresets from data if required
	if (includePresets == false, {holdData[13] = nil});
	if (holdData.notNil, {this.loadData(holdData)});
}

////////////////////////////////////////////////////////////////////////////////////

storePreset { arg argModule, presetNameString;
	var holdSaveData, holdPresetData;
	holdSaveData = argModule.saveData.deepCopy;
	// remove arrPresets from data before storing
	if (holdSaveData[13].notNil, {holdSaveData[13] = nil});
	holdPresetData = [(presetNameString ? "-"), holdSaveData];
	arrPresets = arrPresets.add(holdPresetData);
}

overwritePreset { arg argModule, presetNameString, presetNo;
	var holdSaveData, holdPresetData;
	holdSaveData = argModule.saveData.deepCopy;
	// remove arrPresets from data before storing
	if (holdSaveData[13].notNil, {holdSaveData[13] = nil});
	holdPresetData = [(presetNameString ? "-"), holdSaveData];
	arrPresets[presetNo] = holdPresetData;
}

loadPreset { arg argModule, presetnoNo = 0;
	var holdPresetData;
	holdPresetData = arrPresets[presetnoNo];
	argModule.loadData(holdPresetData[1]);
}

savePresetFile {
	var newPath, newFile, newData;
	Dialog.savePanel({ arg path;
		newPath = path;
//		holdFileName = newPath;
		newData = ["TXSystemModulePreset", this.class.asString, this.saveData];
		newFile = File(newPath,"w");
		newFile << "#" <<< newData << "\n";
		//	use file as an io stream
		//	<<< means store the compile string of the object
		//	<< means store a print string of the object
		newFile.close;
	});
}

openPresetFile { arg includePresets = true;
	var newPath, newFile, newString, newData, presetData;
	Dialog.getPaths({ arg paths;
		newPath = paths.at(0);
//		holdFileName = newPath;
	//	newFile = File(newPath,"r");
	//	newString = newFile.readAllString;
	//	newFile.close;
		newData = thisProcess.interpreter.executeFile(newPath);
		// error check
		if (newData.class != Array, {
			TXInfoScreen.new("Error: invalid data - cannot load preset file.");
			^0;
		});
		if (newData.at(0) != "TXSystemModulePreset", {
			TXInfoScreen.new("Error: invalid data - cannot load preset file.");
			^0;
		});
		if (newData.at(1) != this.class.asString, {
			//
			//	Need to check if preset for an earlier version of the same module, or
			//      for a different module. error message should reflect this
			//
			TXInfoScreen.new("Error: preset is for a different module - cannot load preset file.");
			^0;
		});
		// prepare data
		presetData = newData.at(2).deepCopy;
		// remove arrPresets from data if required
		if (includePresets == false, {presetData[13] = nil});
		// load data
		this.loadData(presetData);
		// recreate view
		system.showView;
	});

}
////////////////////////////////////////////////////////////////////////////////////

loadSynthDef {
	// check for empty synnthDefFunc - some modules don't have a synthdef
	if (synthDefFunc.notNil, {
		// create arrays for mapping synth arguments to busses
		this.class.arrAudSCInBusSpecs.do({ arg item, ind;
			arrAudSCInBusMappings = arrAudSCInBusMappings.add(item.at(2));// synth arg index no
			arrAudSCInBusMappings = arrAudSCInBusMappings.add(arrAudSCInBusses.at(ind).index);// bus index no
		});
		this.myArrCtlSCInBusSpecs.do({ arg item, ind;
			if (item.at(3) != 0, {
				// create arrays for mapping synth arguments to busses
				arrCtlSCInBusMappings = arrCtlSCInBusMappings.add(item.at(2));// synth arg index no
				arrCtlSCInBusMappings = arrCtlSCInBusMappings
					.add("c" ++ arrCtlSCInBusses.at(ind).index.asString);// bus index no
			});
		});
		//	set and update synthDefRates based on module options
		synthDefRates = arrSynthArgSpecs.collect({arg item, i; item.at(2)});
	 	synthDefRates = synthDefRates ++ [0, 0, 0, 0, 0, 0, 0];   // add dummy values for safety

		//	send the SynthDef
		SynthDef(instName, synthDefFunc, synthDefRates).send(system.server);
	});
}

makeSynth {
	var arrSynthArgs;
	// build arrSynthArgs
	arrSynthArgs = [];
	arrSynthArgSpecs.do({ arg item, i;
		arrSynthArgs = arrSynthArgs.addAll([item.at(0), item.at(1)]);
	});
	// add any side chain input bus mappings
	arrSynthArgs = arrSynthArgs
		++ arrCtlSCInBusMappings
		++ arrAudSCInBusMappings;
	// if relevent, add output bus mapping
	if (outBus.notNil, {
		arrSynthArgs = arrSynthArgs ++ [\out, outBus.index];
	});
	// add any  input bus mappings
	if (inputBusses.notNil, {
		arrSynthArgs = arrSynthArgs ++ [\in, inputBusses.at(0)];
	});
	// add any buffer number assignments
	buffers.do({ arg item, i;
		arrSynthArgs = arrSynthArgs.addAll([this.class.arrBufferSpecs.at(i).at(0), item.bufnum]);
	});
	// allow for system to sync
	Routine.run {
		var holdModCondition;

		// add condition to load queue
		 holdModCondition = system.holdLoadQueue.addCondition;
		// pause
		holdModCondition.wait;
		// pause
		system.server.sync;

		// extra pause time
		this.extraLatency.wait;

		// if synth exists already, then release it first.
	//	if (moduleNode.class == Synth, {moduleNode.free});

		// if synth exists already, then use replace.
		if (moduleNode.class == Synth, {
			if (system.autoRun == true, {
				//	load and create running synth on server
				moduleNode = Synth.replace(moduleNode, instName,
					arrSynthArgs;
				);
				// set synth status
				moduleNodeStatus =  "running";
			}, {
				//	load and create paused synth on server
				moduleNode = Synth.replace(moduleNode, instName,
					arrSynthArgs;
				);
				// allow for system to sync before pausing
				system.server.sync;
				moduleNode.run(false);
				// set module node status
				moduleNodeStatus =  "paused";
			});
		},{
			if (system.autoRun == true, {
				//	load and create running synth on server
				moduleNode = Synth.new(instName,
					arrSynthArgs,
					group.nodeID,
					\addToTail ;
				);
				// set synth status
				moduleNodeStatus =  "running";
			}, {
				//	load and create paused synth on server
				moduleNode = Synth.newPaused(instName,
					arrSynthArgs,
					group.nodeID,
					\addToTail ;
				);
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

makeGroup {
	if (system.autoRun == true, {
		//	create group  on server
		moduleNode = Group.new(
			group.nodeID,
			\addToTail ;
		);
		// set synth status
		moduleNodeStatus =  "running";
	}, {
		//	load and create paused synth on server
		moduleNode = Group.new(
			group.nodeID,
			\addToTail ;
		);
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			// pause
			system.server.sync;

			moduleNode.run(false);

			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		// set module node status
		moduleNodeStatus =  "paused";
	});
}

loadAndMakeSynth {
	this.loadSynthDef;
	// allow for system to load synth def
	Routine.run {
		var holdModCondition;
		// add condition to load queue
		holdModCondition = system.holdLoadQueue.addCondition;
		// pause
		holdModCondition.wait;
		system.server.sync;
		// make synth
		this.makeSynth;
		// remove condition from load queue
		system.holdLoadQueue.removeCondition(holdModCondition);
	};
}

rebuildSynth { 	// note this method is overridden in some modules
	this.loadSynthDef;
	// if not a groupsource then make synth
	if (this.class.moduleType != "groupsource", {
		Routine.run { // allow for system to load synth def
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// make synth
			this.makeSynth;
			this.rebuiltStatus = true;
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
			// run system check rebuilds
			system.checkRebuilds;
			system.server.sync;
			this.rebuiltStatus = false;
		};
	});
}

loadDefAndMakeGroup {
	this.loadSynthDef;
	Routine.run {
		var holdModCondition;
		// add condition to load queue
		holdModCondition = system.holdLoadQueue.addCondition;
		// pause
		holdModCondition.wait;
		system.server.sync;
		// make group
		this.makeGroup;
		// remove condition from load queue
		system.holdLoadQueue.removeCondition(holdModCondition);
	};
}

////////////////////////////////////////////////////////////////////////////////////

makeBuffers { arg arrBufferSpecs;
		var holdBuf;
		// free any previous buffers before allocating
		if (buffers.notNil, { buffers.do({ arg item, i;  item.free; }); });
		buffers = nil;
		// allocate buffers and store buffer numbers to synth variables
		arrBufferSpecs.do({ arg item, i;
			// allocate multiple consecutive buffers if required
			if (item.at(3).notNil, {
				holdBuf = Buffer.allocConsecutive(item.at(3), system.server, item.at(1), item.at(2),
					{arg argBuf, i; if (i==0, {this.setSynthArgSpec(item.at(0), argBuf.bufnum);});}
				);
				buffers = buffers.addAll(holdBuf);
			},{
				holdBuf = Buffer.alloc(system.server, item.at(1), item.at(2),
					{arg argBuf, i; this.setSynthArgSpec(item.at(0), argBuf.bufnum);}
				);
				buffers = buffers.add(holdBuf);
			});
		});
}

clearBuffers {
	buffers.do({arg item, i; item.zero;});
}

makeBuffersAndSynth {  arg arrBufferSpecs;
	Routine.run {
		var holdModCondition;
		// add condition to load queue
		holdModCondition = system.holdLoadQueue.addCondition;
		// pause
		holdModCondition.wait;
		system.server.sync;

		// make buffers
		this.makeBuffers(arrBufferSpecs);
		// remove condition from load queue
		system.holdLoadQueue.removeCondition(holdModCondition);

		// add condition to load queue
		holdModCondition = system.holdLoadQueue.addCondition;
		// pause
		holdModCondition.wait;
		system.server.sync;
		// load synthdef
		this.loadSynthDef;
		// remove condition from load queue
		system.holdLoadQueue.removeCondition(holdModCondition);

		// add condition to load queue
		holdModCondition = system.holdLoadQueue.addCondition;
		// pause
		holdModCondition.wait;
		system.server.sync;

		// make synth
		this.makeSynth;

		// remove condition from load queue
		system.holdLoadQueue.removeCondition(holdModCondition);
	};
}

makeBuffersAndGroup {  arg arrBufferSpecs;
	Routine.run {
		var holdModCondition;
		// add condition to load queue
		holdModCondition = system.holdLoadQueue.addCondition;
		// pause
		holdModCondition.wait;
		system.server.sync;

		// make buffers
		this.makeBuffers(arrBufferSpecs);
		// remove condition from load queue
		system.holdLoadQueue.removeCondition(holdModCondition);

		// add condition to load queue
		holdModCondition = system.holdLoadQueue.addCondition;
		// pause
		holdModCondition.wait;
		system.server.sync;

		// make group
		this.loadDefAndMakeGroup;
		// remove condition from load queue
		system.holdLoadQueue.removeCondition(holdModCondition);
	};
}

////////////////////////////////////////////////////////////////////////////////////

setInputBusses { arg argInputBusses;
	if (argInputBusses.notNil, { inputBusses = argInputBusses; });
		if (moduleNode.notNil, {
		moduleNode.set(\in, inputBusses.at(0));
	});
}

getSynthArgSpec { arg argSynthArgString;
	//	get a value in arrSynthArgSpecs.
	 arrSynthArgSpecs.do({ arg item, i;
	 	if (item.at(0) == argSynthArgString, {
	 		^item.at(1);
	 	});
	 });
	 ^nil;
}

setSynthArgSpec { arg argSynthArgString, argVal;
	//	update a value in arrSynthArgSpecs.
	 arrSynthArgSpecs.do({ arg item, i;
	 	if ( (item.at(0) == argSynthArgString) and: (item.at(1) != argVal), {
	 		item.put(1, argVal);
	 		^0;
	 	});
	 });
}

setSynthValue { arg argSynthArgString, argVal;
	// set current value on node
	if (this.moduleNode.notNil, {
		this.moduleNode.set(argSynthArgString, argVal);
	});
	// store to synthArgSpecs
	this.setSynthArgSpec(argSynthArgString, argVal);
}

getSynthOption { arg argIndex;
	//	get a value
	^arrOptionData.at(argIndex).at(arrOptions.at(argIndex)).at(1);
}

setSynthOption { arg argIndex, argVal;
	//	update value
	 arrOptionData.at(argIndex).do({ arg item, i;
	 	if ( (item.at(1) == argVal), {
	 		arrOptions.put(argIndex, i);
	 		^0;
	 	});
	 });
}

runAction {
	if (moduleNode.notNil, {
		moduleNode.run(true);
		moduleNodeStatus = "running";
	});
}

pauseAction {
	if (moduleNode.notNil, {
		moduleNode.run(false);
		moduleNodeStatus = "paused";
	});
}

allNotesOff {
	//	release all synths at node.
	moduleNode.set(\gate, 0);
}

////////////////////////////////////////////////////////////////////////////////////

midiNoteInit {
	//	initialisation for midi modules.
	midiNoteStatus = true;
	midiNotes = [] ! 128;  // array has one array per MIDI note
}

midiControlActivate {
	// dummy method - override where needed
}

midiControlDeactivate {
	// dummy method - override where needed
}

midiNoteActivate {
	var arrSynthArgs;
	//	stop any previous routines
	this.midiNoteDeactivate;
	//	start responder
	midiNoteOnResp = NoteOnResponder ({  |src, chan, num, vel|
		var newNode, holdVal;
		//	check whether to play
		if (	(chan >= (midiMinChannel-1)) and: (chan <= (midiMaxChannel-1))
			and: (num >= midiMinNoteNo) and: (num <= midiMaxNoteNo)
			and: (vel >= midiMinVel) and: (vel <= midiMaxVel)
		, {
			// create node
			newNode = this.createSynthNote(num, vel, 0);
		});
	});
	midiNoteOffResp = NoteOffResponder({  |src, chan, num, vel|
		//	check whether to release
		if (	(chan >= (midiMinChannel-1)) and: (chan <= (midiMaxChannel-1))
			and: (num >= midiMinNoteNo) and: (num <= midiMaxNoteNo)
			and: (vel >= midiMinVel) and: (vel <= midiMaxVel)
		, {
			// release note
			this.midiNoteRelease(num);
		});
	});
	midiSustainPedalResp = CCResponder({  |src, chan, num, val|
		// set value
		if (	(chan >= (midiMinChannel-1)) and: (chan <= (midiMaxChannel-1))
			and: (num == 64)
	 	, {
			this.setMidiSustainPedalState((val/127).round);
	 	});
	});
	midiBendResp = BendResponder({  |src, chan, val|
		// set value
		if (	(chan >= (midiMinChannel-1)) and: (chan <= (midiMaxChannel-1))
	 	, {
			this.setMidiBend(val / 16384);
	 	});
	});
}

midiNoteDeactivate {
	//	stop responding to midi.
 	if (midiNoteOnResp.class == NoteOnResponder, {
 		midiNoteOnResp.remove;
 	});
 	if (midiNoteOffResp.class == NoteOffResponder, {
 		midiNoteOffResp.remove;
 	});
 	if (midiSustainPedalResp.class == CCResponder, {
 		midiSustainPedalResp.remove;
 	});
 	if (midiBendResp.class == BendResponder, {
		this.setMidiBend(0);
 		midiBendResp.remove;
 	});
 }

midiNoteRelease {arg num;
	// only release if sustain pedal is off
	if (midiSustainPedalState == 0, {
		this.releaseSynthGate(num);
	},{
		arrHeldMidiNotes = arrHeldMidiNotes.add(num);
	});
}

setMidiBend { arg inVal;
	this.setSynthValue("pitchbend", inVal);
}

setMidiSustainPedalState { arg inVal;
	if (inVal == 0, {
		// clear all held notes
		arrHeldMidiNotes.do ({arg item, i;
			this.releaseSynthGate(item);
		});
		arrHeldMidiNotes = [];
 	});
	midiSustainPedalState = inVal;
}

setMidiListen { arg inVal;
	midiListen = inVal;
	if (inVal == 1, {
		this.midiNoteActivate;
	},{
		this.midiNoteDeactivate;
	});
}


////////////////////////////////////////////////////////////////////////////////////

createSynthNote { arg argNote=60, argVeloc=100, argEnvTime=1, seqLatencyOn=1, argNoteDetune=0;
	var arrSynthArgs, holdSynth, latencyTime=0;

	if (	(argNote >= midiMinNoteNo) and: (argNote <= midiMaxNoteNo)
		and: (argVeloc >= midiMinVel) and: (argVeloc <= midiMaxVel)
	, {
		// build arrSynthArgs
		arrSynthArgs = [];
		arrSynthArgSpecs.do({ arg item, i;
			arrSynthArgs = arrSynthArgs.addAll([item.at(0), item.at(1)]);
		});
		arrSynthArgs = arrSynthArgs
			++ arrCtlSCInBusMappings
			++ arrAudSCInBusMappings
			++ [\out, outBus.index];
		if (inputBusses.notNil, {
			arrSynthArgs = arrSynthArgs ++ [\in, inputBusses.at(0)];
		});
		// check polyphony and note steal if necessary
		this.checkPolyphony;
		// return a synth that plays the note
		holdSynth = Synth.new(instName,
			arrSynthArgs ++ [\note, argNote + argNoteDetune, \velocity, argVeloc, \envtime, argEnvTime],
			moduleNode.nodeID,
			\addToTail ;
		);
		// register node
		NodeWatcher.register(holdSynth, true);
		if (midiNoteStatus == true, {
			// save it in array to free later
			midiNotes[argNote] = midiNotes[argNote].add(holdSynth);
		});
		// add it to groupNodes
		groupNodes = groupNodes.add(holdSynth);
	 	// if envtime not 0 schedule note off after Env time
		if (argEnvTime > 0, {
			if (seqLatencyOn == 1, {latencyTime = system.latency});
			SystemClock.sched(argEnvTime + latencyTime, { // allow for latency
				if (holdSynth.notNil, {
					if (holdSynth.isPlaying, {holdSynth.release; });
				});
				nil
			});
		});
		^holdSynth;
	});
}

releaseSynthGate { arg argNote=60;
	var holdSynth;
	if (midiNotes[argNote][0].notNil, {
		holdSynth = midiNotes[argNote].removeAt(0);    //  free
	});
	// release node
	if (holdSynth.notNil,{
		if (holdSynth.isPlaying, {
			holdSynth.set(\gate, 0);
		});
	});
}

activeGroupNodes {
	^groupNodes.select({arg item, i;
		var keep = 0;
		if (item.notNil, {
			if (item.isPlaying, {keep = 1;});
		});
		keep == 1;
	});
}

checkPolyphony {
	var activeNodes;
	activeNodes = this.activeGroupNodes;
	// check polyphony and release first node if necessary
	if (activeNodes.size > (groupPolyphony - 1), {
//		if (activeNodes.at(0).notNil, {
//			if (activeNodes.at(0).isPlaying, {
				activeNodes.at(0).free;
//			});
//		});
	});
}

setMonophonic {
	groupPolyphony = 1;
}


////////////////////////////////////////////////////////////////////////////////////

buildActionSpecs {arg argArrSpecs;
	^TXBuildActions.from(this, argArrSpecs);
}

////////////////////////////////////////////////////////////////////////////////////

checkDeletions {
	^nil;
	// this method is called by the system after modules have been deleted
	// it  returns nil by default and can be overriden in modules as required.
}

markForDeletion {
	toBeDeletedStatus = true;
}

confirmDeleteModule {
	if (system.dataBank.confirmDeletions == true, {
		TXInfoScreen.newConfirmWindow(
			{ this.deleteModule; system.showView;},
			"Are you sure you want to delete " ++ this.instName ++ "?"
		);
	},{
		this.deleteModule;
		system.showView;
	});
}

deleteModule {
	if (	deletedStatus != true, {
		// post message
//		("Deleting Module: " ++ this.instName).postln;
		// run any necessary actions
		this.deleteModuleExtraActions;
		// set status
		this.toBeDeletedStatus = false;
		this.deletedStatus = true;
		// remove from system.arrSystemModules
		system.arrSystemModules.remove(this);
		// remove from class's arrInstances and renumber arrInstances
		this.class.arrInstances.remove(this);
		this.class.renumberInsts(this);
		// deactivate midi
		if (midiNoteStatus == true, {
			this.midiNoteDeactivate;
		});
		this.midiControlDeactivate;
		// stop the sound
		this.moduleNode.free;
		// close window
		this.closeGui;
		// delete any busses
		if (this.outBus.notNil, {this.outBus.free;});
		if (this.arrAudSCInBusses.notNil, {this.arrAudSCInBusses.do({arg item, i; item.free;}); });
		if (this.arrCtlSCInBusses.notNil, {this.arrCtlSCInBusses.do({arg item, i; item.free;}); });
		// delete any buffers
		if (this.buffers.notNil, { this.buffers.do({ arg item, i;  item.free; }); });
		// run system.checkDeletions to remove any references to deleted modules
		system.checkDeletions;
	});
}
deleteModuleExtraActions {
	// dummy method - override where needed
}

openHelp {
		TXHelpScreen.openModule("TX_" ++ this.class.defaultName);
}

instSortingName {
	// adds zeros into instance no for correct sorting
	var index1, index2, holdZeros, newName;
	index1 = instName.findBackwards("[");
	index2 = instName.findBackwards("]");
	holdZeros = "0000".keep(5 - (index2-index1));
	newName = instName.keep(index1+1) ++ holdZeros ++
		instName.keep(1 - (instName.size - index1));
	^newName;
}

instDisplayName {
	var displayName;
		if (legacyModule == true, {
			displayName = this.instName ++ " *";
			}, {
			displayName = this.instName;
		});
	^displayName;
}

}

