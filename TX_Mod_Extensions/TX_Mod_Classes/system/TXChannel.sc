// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXChannel : TXModuleBase { //  Channel module
	classvar	<>group;				// override for default group for adding channel synths to
	classvar <>arrInstances;
	classvar <defaultName;  		// default  name
	classvar <moduleType;			//  "channel"
	classvar <moduleRate;			// -not used for TXChannel
	classvar <noInChannels;			// -not used for TXChannel
	classvar <arrAudSCInBusSpecs; 	// -not used for TXChannel
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels=0;		// -not used for TXChannel
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar	<guiLeft=100;
	classvar	<guiTop=300;
	classvar	<guiWidth=140;
	classvar	<guiHeight=595;
	classvar	<arrAllDestBusses;		// array of possible destination busses for channels
	classvar	guiRowHeight = 20;		// default variable for gui
	classvar	globalSoloMode = 0;

	var  <channelRate;				// "audio" or "control", set for each channel
	var  <sourceModule;
	var  <sourceName;
	var  <>channelNo;
	var  <arrSourceBusses;
	var  <sourceBusno = 0;
	var  <insert1Module;
	var  <insert2Module;
	var  <insert3Module;
	var  <insert4Module;
	var  <insert5Module;
	var  <destModule;
	var  <destName;
	var  <arrDestBusses;
	var  <destBusNo = 0;
	var  <chanColour;
	var  <>chanLabel;
	var  <>chanError;
	var  <chanStatus = "edit";			//  can be "edit" or "active"
	var  <synthChannel, <synthFXSend1, <synthFXSend2, <synthFXSend3, <synthFXSend4;
	var  <arrSourceOuts, <arrDestOuts;
	var	<arrInsert1Outs, <arrInsert2Outs, <arrInsert3Outs;
	var	<arrInsert4Outs, <arrInsert5Outs;
	var 	holdArrEditControlVals, holdArrActiveControlVals;  // Note - these 2 are no longer used
	var 	holdVolSlider, holdVolNumberbox, holdInvChkBox, holdOffChkBox, holdPanSldr, holdMuteChkBox;
	var	holdFxSnd1Btn, holdFxSnd1Sldr, holdFxSnd2Btn, holdFxSnd2Sldr;
	var 	holdFxSnd3Btn, holdFxSnd3Sldr, holdFxSnd4Btn, holdFxSnd4Sldr;
	var	deactivateOn, guiResetOn, <reactivateOn;
	var  <holdMuteStatus = 0;
	var	<column;

	*initClass{
		arrInstances = [];
		//	set class specific variables
		defaultName = "Channel";
		moduleType = "channel";
		arrCtlSCInBusSpecs = [
			["Level", 1, "modVol"],
			["Pan", 1, "modPan"]
		];
	}

	*systemInit{
		//	send the SynthDef for the main audio channel
		SynthDef("TXChannelAudio1",
			{ arg in, out, pan = 0.5, vol = 0.5, mute = 0, modPan = 0, modVol = 0;
				var mixout, startEnv;
				startEnv = TXEnvPresets.startEnvFunc.value;
				mixout = InFeedback.ar(in) * (vol + modVol).min(1).max(0) * 2 * (1-mute);
				Out.ar(out, TXClean.ar(startEnv * mixout));
			}, [0, 0, \ir, defLagTime, defLagTime, \ir, defLagTime] // lag rates
		).send(system.server);
		SynthDef("TXChannelAudio2",
			{ arg inL, inR, outL,  outR, pan = 0.5, vol = 0.5, mute = 0, modPan = 0, modVol = 0;
				var startEnv, inBusL, inBusR, mixoutL, mixoutR, panSum;
				startEnv = TXEnvPresets.startEnvFunc.value;
				inBusL = InFeedback.ar(inL);
				inBusR = InFeedback.ar(inR);
				panSum = (pan + modPan).min(1).max(0);
				// TESTING:
				// NOTE: UNTESTED CODE: equal power mix below needs testing.
				// mixoutL = inBusL * (vol + modVol).min(1).max(0) * (1-mute) * ((1-panSum) * pi/2).sin;
				// mixoutR = inBusR * (vol + modVol).min(1).max(0) * (1-mute) * (panSum * pi / 2).sin;
				// KEEP LINEAR PAN FOR NOW:
				mixoutL = inBusL * (vol + modVol).min(1).max(0) * 2 * (1-mute) * (1-panSum);
				mixoutR = inBusR * (vol + modVol).min(1).max(0) * 2 * (1-mute) * panSum;
				Out.ar(outL, TXClean.ar(startEnv * mixoutL));
				Out.ar(outR, TXClean.ar(startEnv * mixoutR));
			}, [0, 0, 0, 0, defLagTime, defLagTime, defLagTime, defLagTime, defLagTime] // lag rates
		).send(system.server);
		//	send the SynthDef for the main control channel
		SynthDef("TXChannelControl1",
			{ arg in, out, i_numInputs, i_numOutputs, vol = 0, invert = 0, mute = 0, modVol = 0;
				i_numOutputs.do({ arg item, i;
					var mixout;
					mixout = In.kr(in+i) * (vol + modVol).min(1).max(0) * (1-mute) * (1-(2*invert));
					Out.kr((out+i), TXClean.kr(mixout));
				});
			}, [0, 0, \ir, \ir, defLagTime, defLagTime, defLagTime, defLagTime] // lag rates
		).send(system.server);
		//	send the SynthDef for FX sends
		SynthDef("TXChannelFX1",
			{ arg in, out, vol = 0.5, mute = 0, send = 0, modVol = 0, modSend = 0;
				var mixout, startEnv;
				startEnv = TXEnvPresets.startEnvFunc.value;
				mixout = InFeedback.ar(in) * (vol + modVol).min(1).max(0) * (1-mute)
				* (send + modSend).min(1).max(0);
				Out.ar(out, TXClean.ar(startEnv * mixout));
			}, [0, 0, defLagTime, defLagTime, defLagTime, defLagTime, defLagTime] // lag rates
		).send(system.server);
		SynthDef("TXChannelFX2",
			{ arg inL, inR, out, vol = 0.5, mute = 0, send = 0, modVol = 0, modSend = 0;
				var mixin, mixout, startEnv;
				startEnv = TXEnvPresets.startEnvFunc.value;
				mixin = Mix.new([InFeedback.ar(inL), InFeedback.ar(inR)]);
				mixout = mixin * (vol + modVol).min(1).max(0) * (1-mute)
				* (send + modSend).min(1).max(0);
				Out.ar(out, TXClean.ar(startEnv * mixout));
			}, [0, 0, 0, defLagTime, defLagTime, defLagTime, defLagTime, defLagTime] // lag rates
		).send(system.server);
	}

	*allAudioChannels {
		^arrInstances.select({arg item, i; item.channelRate == "audio"});
	}

	*setGlobalSoloOn {
		// check global solo mode not already on
		if (globalSoloMode == 0, {
			this.allAudioChannels.do({ arg currentChannel, i;
				currentChannel.globSoloModeOn;
			});
			globalSoloMode = 1;
		});
	}

	*setGlobalSoloOff {
		globalSoloMode = 0;
		// make sure no channels are solo'd first
		this.allAudioChannels.do({ arg currentChannel, i;
			if (currentChannel.getSynthArgSpec("Solo") == 1, {
				globalSoloMode = 1;
			});
		});
		if (globalSoloMode == 0, {
			this.allAudioChannels.do({ arg currentChannel, i;
				currentChannel.globSoloModeOff;
			});
		});
	}

	*renumberInsts{
		arrInstances.do({ arg item, i;
			//	create instance name
			item.instName = defaultName ++ " " ++(i + 1).asString;
			item.channelNo = i + 1;
		});
	}

	*new { arg argSource;
		^super.new.init(argSource);
	}

	init {arg argSource;
		//	use base class initialise
		this.baseInit(this);
		//	set instance name
		this.class.renumberInsts;
		chanLabel = "";
		channelNo = arrInstances.size;
		//	set variable
		// Note: arrSynthArgSpecs is used here as a convenient storage space for variables, but, unlike
		//		most modules, it isn't used directly for synth args as they are defined above with
		// 		each different synth def.
		arrSynthArgSpecs = [
			["SourceBusInd", 0],
			["DestBusInd", 0],
			["Volume", 0.1],
			["Pan", 0.5],
			["Mute", 0],
			["Solo", 0],
			["Invert", 0],
			["FXSend1On", 0],
			["FXSend1Val", 0],
			["FXSend2On", 0],
			["FXSend2Val", 0],
			["FXSend3On", 0],
			["FXSend3Val", 0],
			["FXSend4On", 0],
			["FXSend4Val", 0],
		];
		this.setSourceVariables(argSource);
		//	set chanColour
		if (channelRate == "audio", {chanColour = TXColour.sysGuiCol1}, {chanColour = TXColour.sysGuiCol2});
		//	set arrActionSpecs
		if (channelRate == "audio", {
			arrActionSpecs = this.buildActionSpecs([
				["EZslider", "Level", \unipolar,"Volume"],
				["TXCheckBox", "Mute", "Mute"],
				["EZslider", "Pan", \unipolar,"Pan"],
				["TXCheckBox", "FX Send 1 On", "FXSend1On"],
				["EZslider", "FX Send 1 Level", \unipolar,"FXSend1Val"],
				["TXCheckBox", "FX Send 2 On", "FXSend2On"],
				["EZslider", "FX Send 2 Level", \unipolar,"FXSend2Val"],
				["TXCheckBox", "FX Send 3 On", "FXSend3On"],
				["EZslider", "FX Send 3 Level", \unipolar,"FXSend3Val"],
				["TXCheckBox", "FX Send 4 On", "FXSend4On"],
				["EZslider", "FX Send 4 Level", \unipolar,"FXSend4Val"],
			]);
			}, {
				arrActionSpecs = this.buildActionSpecs([
					["EZslider", "Level", \unipolar,"Volume"],
					["TXCheckBox", "Off", "Mute"],
					["TXCheckBox", "Invert", "Invert"],
				]);
		});
	}

	setSourceVariables { arg argSource;
		sourceModule = argSource;
		channelRate = sourceModule.class.moduleRate;
		arrSourceBusses = sourceModule.arrOutBusChoices;
		sourceBusno = 0;
		this.setSynthArgSpec("SourceBusInd", 0);
		arrSourceOuts = arrSourceBusses.at(sourceBusno).at(1);
		sourceName = sourceModule.instName;
	}

	getSourceBusName {
		var sourceBus;
		sourceBus = this.getSynthArgSpec("SourceBusInd") ? 0;
		^arrSourceBusses[sourceBus][0].asString;
	}

	////////////////////////////////////////////////////////////////////////////////////

	saveData {
		// this method returns an array of all data for saving with various components:
		// 0- string "TXModuleSaveData", 1- module class, 2- moduleID, 3- arrControlVals, 4- arrModuleIDs,
		// 5- chanStatus, 6- sourceBusno, 7- destBusNo, 8- holdArrEditControlVals, 9- arrSynthArgSpecs,
		// 10- chanLabel, 11-posX, 12-posY
		var arrData, arrModuleIDs;
		// arrChannelData consists of moduledIDs for all modules used
		arrModuleIDs = [
			sourceModule,
			insert1Module,
			insert2Module,
			insert3Module,
			destModule,
			insert4Module, // insert 4 & 5 are newer
			insert5Module,
		]
		.collect({ arg item, i;
			if (item.notNil, {item.moduleID});
		});
		arrData = ["TXModuleSaveData", this.class.asString, this.moduleID, arrControlVals, arrModuleIDs, chanStatus,
			sourceBusno, destBusNo, holdArrEditControlVals, arrSynthArgSpecs, chanLabel, posX, posY];
		^arrData;
	}

	loadData { arg arrData;
		// this method updates all data by loading arrData. format:
		// 0- string "TXModuleSaveData", 1- module class, 2- moduleID, 3- arrControlVals, 4- arrModuleIDs,
		// 5- chanStatus, 6- sourceBusno, 7- destBusNo, 8- holdArrEditControlVals, 9- arrSynthArgSpecs,
		// 10- chanLabel, 11-posX, 12-posY
		var holdModuleIDs, holdChanStatus, holdModules;
		if (arrData.class != Array, {
			TXInfoScreen.new("Error: invalid data. cannot load.");
			^0;
		});
		if (arrData.at(1) != this.class.asString, {
			TXInfoScreen.new("Error: invalid data class. cannot load.");
			^0;
		});
		holdModuleIDs = arrData.at(4).deepCopy;
		holdChanStatus = arrData.at(5).deepCopy;
		sourceBusno = arrData.at(6).copy;
		destBusNo = arrData.at(7).copy;
		arrSynthArgSpecs = arrData.at(9).deepCopy;
		if (arrData.at(10).size > 0, {
			chanLabel = arrData.at(10).deepCopy;
		});
		posX = arrData.at(11).copy ? 0;
		posY =  arrData.at(12).copy ? 0;
		holdModules = [];
		6.do({ arg item, i;
			if (holdModuleIDs.at(i+1).notNil, {
				holdModules = holdModules.add( system.getModuleFromID(holdModuleIDs.at(i+1)) );
				},{
					holdModules = holdModules.add(nil);
			});
		});
		// restore insert and dest modules
		insert1Module = holdModules.at(0);
		insert2Module = holdModules.at(1);
		insert3Module = holdModules.at(2);
		destModule = holdModules.at(3);
		insert4Module = holdModules.at(4); // insert 4 & 5 are newer
		insert5Module = holdModules.at(5);
		if (destModule.notNil, {
			destName = destModule.instName;
		});
		// update busses
		arrSourceBusses = sourceModule.arrOutBusChoices;
		if (sourceBusno.notNil and: arrSourceBusses.notNil, {
			arrSourceOuts = arrSourceBusses.at(sourceBusno).at(1);  // array of bus indices
		});
		if (insert1Module.notNil, {
			arrInsert1Outs = [];
			insert1Module.class.noOutChannels.do({ arg item, i;
				arrInsert1Outs = arrInsert1Outs.add(insert1Module.outBus.index + i);
			});
		});
		if (insert2Module.notNil, {
			arrInsert2Outs =  [];
			insert2Module.class.noOutChannels.do({ arg item, i;
				arrInsert2Outs = arrInsert2Outs.add(insert2Module.outBus.index + i);
			});
		});
		if (insert3Module.notNil, {
			arrInsert3Outs =  [];
			insert3Module.class.noOutChannels.do({ arg item, i;
				arrInsert3Outs = arrInsert3Outs.add(insert3Module.outBus.index + i);
			});
		});
		if (insert4Module.notNil, {
			arrInsert4Outs =  [];
			insert4Module.class.noOutChannels.do({ arg item, i;
				arrInsert4Outs = arrInsert4Outs.add(insert4Module.outBus.index + i);
			});
		});
		if (insert5Module.notNil, {
			arrInsert5Outs =  [];
			insert5Module.class.noOutChannels.do({ arg item, i;
				arrInsert5Outs = arrInsert5Outs.add(insert5Module.outBus.index + i);
			});
		});
		if (destModule.notNil, {
			if (channelRate ==  "audio", {
				arrDestBusses = destModule.arrAudSCInBusChoices;
				}, {
					arrDestBusses = destModule.arrCtlSCInBusChoices;
			});
			arrDestOuts = arrDestBusses.at(destBusNo ? 0).at(1);  // array of bus indices
		});
		//	set to reactivate channel
		reactivateOn = true;
	} // end of method loadData

	////////////////////////////////////////////////////////////////////////////////////

	*makeTitleGui{ arg argParent;
		var holdColumn;
		// create column for Titles
		holdColumn =  CompositeView(argParent,Rect(0,0, 74, 595));
		holdColumn.background = TXColour.sysChannelAudio;
		holdColumn.decorator = FlowLayout(holdColumn.bounds);
		holdColumn.decorator.gap.y = 3;
		[	 ["Channel", 4],
			["Name", 2],
			["Source", 2],
			[" ", 2],
			["Source Bus", 3],
			["Insert 1", 3],
			["Insert 2", 3],
			["Insert 3", 3],
			["Insert 4", 3],
			["Insert 5", 3],
			["Destination", 3],
			["Dest. Bus", 8],
			["FX Send 1", 0],
			["FX Send 2", 0],
			["FX Send 3", 0],
			["FX Send 4", 0],
			["Pan ", 64],
			["Level", 0],
		]
		.do({ arg item, i;
			StaticText(holdColumn, 64 @ guiRowHeight)
			.string_(item.at(0)).align_(\right).stringColor_(TXColor.white);
			holdColumn.decorator.nextLine;
			holdColumn.decorator.shift(0, item.at(1));
		});
	}

	baseOpenGui{
		// this is a dummy method to override method in TXModuleBase.
		// the method makeChannelGui is used to build the actual gui.
	}

	*makeBlankChannelGui{ arg argParent;
		// this creates an empty channel.
		var holdColumn;
		holdColumn =  CompositeView(argParent,Rect(0,0,123,595));
		// N.B. Column width is 123, max. view width is 123-8 = 115
	}

	////////////////////////////////////////////////////////////////////////////////////

	makeChannelGui{ arg argParent, argLeftIndent;
		var arrPositions;
		var btnChannelDel, btnChannelDup, btnChannelEdit;
		var viewSource, viewSourceBus;
		var viewInsert1, viewInsert2, viewInsert3, viewInsert4, viewInsert5;
		var viewDest, viewDestBus;
		var txtChannelName, channelSourceWidth, newChanSourcePopup, newSourceModule;
		var btnInsert1Del, btnInsert2Del, btnInsert3Del, btnInsert4Del, btnInsert5Del;
		var btnDestDel, btnActivate, btnClearError, btnPanCentre;
		var arrAllPossInsertClasses, arrAllPossInsertNames;
		var arrAllDestModules, arrAllDestModNames, arrDestBusNames;
		var  arrAudioSourceMods, arrAudioSourceBusses, arrAudioSourceBusNames;
		var  arrControlSourceMods, arrControlSourceBusses, arrControlSourceBusNames;

		// create array of names of system's audio source modules.
		arrAudioSourceMods = system.arrSystemModules
		.select({ arg item, i; item.class.moduleRate == "audio";})
		.select({ arg item, i;
			(item.class.moduleType == "source")
			or:
			(item.class.moduleType == "groupsource")
			or:
			(item.class.moduleType == "insert") ;
		})
		.sort({ arg a, b; a.instSortingName < b.instSortingName; });

		// create array of names of system's audio source modules and busses.
		arrAudioSourceBusses = (
			arrAudioSourceMods
			++ system.arrFXSendBusses	// array of FX send busses
			++ system.arrAudioAuxBusses	// array of Audio Aux busses
			++ system.arrMainOutBusses	// array of Main Out busses
		);
		arrAudioSourceBusNames = arrAudioSourceBusses.collect({arg item, i;  item.instName; });

		// create array of names of all system's control source modules.
		arrControlSourceMods = system.arrSystemModules
		.select({ arg item, i; item.class.moduleRate == "control";})
		.select({ arg item, i;
			(item.class.moduleType == "source")
			or:
			(item.class.moduleType == "groupsource")
			or:
			(item.class.moduleType == "insert") ;
		})
		.sort({ arg a, b; a.instSortingName < b.instSortingName; });

		// create array of names of system's control source modules and busses.
		arrControlSourceBusses = (
			arrControlSourceMods
			++ system.arrControlAuxBusses	// array of Control Aux busses
		);
		arrControlSourceBusNames = arrControlSourceBusses.collect({arg item, i;  item.instName; });

		/* // OLDER CODE:
		// set variables depending on rate
		if (channelRate ==  "audio", {
		arrAllPossInsertClasses = system.arrAllPossModules
		// only show insert modules
		.select({ arg item, i; (item.moduleType == "insert") and: (item.moduleRate == "audio"); });
		arrAllDestModules = (
		// array of modules which have audio inputs
		system.arrSystemModules.select({ arg item, i; item.arrAudSCInBusChoices.size > 0; })
		.sort({ arg a, b; a.instSortingName < b.instSortingName; })
		// array of FX send bus modules
		++ system.arrMainOutBusses
		// array of Audio Aux bus modules
		++ system.arrAudioAuxBusses
		)
		}, {
		arrAllPossInsertClasses = system.arrAllPossModules
		// only show source modules
		.select({ arg item, i; (item.moduleType == "insert") and: (item.moduleRate == "control"); });
		arrAllDestModules = (
		// array of modules which have control inputs
		system.arrSystemModules.select({ arg item, i; item.arrCtlSCInBusChoices.size > 0; })
		.sort({ arg a, b; a.instSortingName < b.instSortingName; })
		// array of channels
		++ arrInstances
		// array of Control Aux busses
		++ system.arrControlAuxBusses
		);
		});
		arrAllPossInsertNames = arrAllPossInsertClasses.collect({ arg item, i; item.defaultName; });
		*/
		if (channelRate ==  "audio", {
			arrAllPossInsertClasses = system.dataBank.arrAudioInsertModulesByCategory.collect({arg item; item[1]});
			arrAllPossInsertNames = system.dataBank.arrAudioInsertModulesByCategory.collect({arg item; item[0]});
			arrAllDestModules = (
				// array of modules which have audio inputs
				system.arrSystemModules.select({ arg item, i; item.arrAudSCInBusChoices.size > 0; })
				.sort({ arg a, b; a.instSortingName < b.instSortingName; })
				// array of FX send bus modules
				++ system.arrMainOutBusses
				// array of Audio Aux bus modules
				++ system.arrAudioAuxBusses
			)
			}, {
				arrAllPossInsertClasses = system.dataBank.arrControlInsertModulesByCategory.collect({arg item; item[1]});
				arrAllPossInsertNames = system.dataBank.arrControlInsertModulesByCategory.collect({arg item; item[0]});
				arrAllDestModules = (
					// array of modules which have control inputs
					system.arrSystemModules.select({ arg item, i; item.arrCtlSCInBusChoices.size > 0; })
					.sort({ arg a, b; a.instSortingName < b.instSortingName; })
					// array of channels
					++ arrInstances
					// array of Control Aux busses
					++ system.arrControlAuxBusses
				);
		});
		arrAllDestModNames = arrAllDestModules.collect({arg item, i;  item.instName; });

		// create column for channel
		column =  CompositeView(argParent,Rect(argLeftIndent ? 0,0, guiWidth+7, 595));
		// N.B. Column width is 123, max. view width is 123-8 = 115
		// if channel is selected, then highlight
		if (TXChannelRouting.displayChannel == this, {
			column.background = TXColour.sysChannelHighlight;
			},{
				if (channelRate == "audio", {
					column.background = TXColour.sysChannelAudio;
					}, {
						column.background = TXColour.sysChannelControl;
				});
		});
		column.decorator = FlowLayout(column.bounds);
		column.decorator.gap.y = 3;

		// clear arrControls
		arrControls = [];

		// popup - Channel No. & Type or Move channel
		arrPositions = [(channelNo.asString ++ " - " ++ channelRate)] ++
		(1 .. arrInstances.size).collect({ arg item, i; " move to " ++ item.asString;});
		PopUpMenu(column, 84 @ guiRowHeight)
		.items_(arrPositions)
		.background_(TXColor.white)
		.action_({arg view;
			this.channelHighlight;
			// alter channelNo
			if (view.value > 0, {
				this.moveToPosition(view.value);
				// recreate view
				system.showView;
			});
		});
		// spacing
		//	column.decorator.shift(20,0);
		// channel duplicate button
		btnChannelDup =
		Button(column, 26 @ guiRowHeight)
		.states_([["dup", TXColor.white, chanColour]])
		.action_({
			var newChannel;
			newChannel = TXChannelRouting.addChannel(sourceModule);
			newChannel.moveToPosition(channelNo + 1);
			//			newChannel.channelHighlight;
			// recreate view
			system.showView;

		});
		// channel delete button
		btnChannelDel =
		Button(column, 21 @ guiRowHeight)
		.states_([["del", TXColor.white, TXColor.sysDeleteCol]])
		.action_({
			this.channelHighlight;
			if (system.dataBank.confirmDeletions == true, {
				TXInfoScreen.newConfirmWindow(
					{ 	this.markForDeletion;
						system.checkDeletions;  // get system to carry out deletion
						// recreate view
						system.showView;
					},
					"Are you sure you want to delete " ++ this.instName
					++ " and its Insert modules?  -  its Source module won't be deleted."
				);
				},{
					this.markForDeletion;
					system.checkDeletions;  // get system to carry out deletion
					// recreate view
					system.showView;
			});

		});
		// go to next line
		column.decorator.nextLine;

		// Channel Name
		txtChannelName = TextField(column, guiWidth @ guiRowHeight);
		txtChannelName.stringColor_(chanColour);
		txtChannelName.string = (chanLabel);
		txtChannelName.action = ({ arg view;
			chanLabel = view.value;
		});

		// go to next line
		column.decorator.nextLine;
		// view - source
		sourceName = sourceModule.instName;
		viewSource = Button(column, guiWidth@ guiRowHeight)
		.states_([[sourceName, this.moduleStringColour(sourceModule), chanColour]])
		.action_({this.channelHighlight;
			if (sourceModule.notNil, { this.openModuleGui(sourceModule); });
		});
		// go to next line
		column.decorator.nextLine;
		// change source
		newChanSourcePopup = PopUpMenu(column, guiWidth @ guiRowHeight)
		.stringColor_(TXColor.white).background_(chanColour);
		if (channelRate ==  "audio", {
			newChanSourcePopup.items = ["... change source to..."] ++ arrAudioSourceBusNames;
			newChanSourcePopup.action = { |view|
				this.channelHighlight;
				if (view.value > 0, {
					this.deactivate;
					newSourceModule = arrAudioSourceBusses.at(view.value - 1);
					this.setSourceVariables(newSourceModule);
					this.activate;
					// update screen
					system.showView;
				});
			};
			}, {
				newChanSourcePopup.items = ["... change source to..."] ++ arrControlSourceBusNames;
				newChanSourcePopup.action = { |view|
					this.channelHighlight;
					if (view.value > 0, {
						this.deactivate;
						newSourceModule = arrControlSourceBusses.at(view.value - 1);
						this.setSourceVariables(newSourceModule);
						this.activate;
						// update screen
						system.showView;
					});
				};
		});

		// go to next line
		column.decorator.nextLine;
		// view - source bus
		viewSourceBus = PopUpMenu(column, guiWidth @ guiRowHeight)
		.stringColor_(TXColor.white).background_(chanColour)
		.items_(arrSourceBusses.collect({ arg item, i; item.at(0); })); // show all bus names
		// get  data from synthArgSpecs
		viewSourceBus.value = this.getSynthArgSpec("SourceBusInd") ? 0;
		viewSourceBus.action = { |view|
			this.channelHighlight;
			this.deactivate;
			// store current data to synthArgSpecs
			this.setSynthArgSpec("SourceBusInd", view.value);
			// store current value to sourceBusno
			sourceBusno = view.value;
			// assign busses
			arrSourceOuts = arrSourceBusses.at(view.value).at(1);  // array of bus indices
			this.activate;
			// recreate view
			system.showView;
		};
		// add to arrControls
		arrControls = arrControls.add(viewSourceBus);
		// create inserts
		[ 	[insert1Module, viewInsert1, btnInsert1Del],
			[insert2Module, viewInsert2, btnInsert2Del],
			[insert3Module, viewInsert3, btnInsert3Del],
			[insert4Module, viewInsert4, btnInsert4Del],
			[insert5Module, viewInsert5, btnInsert5Del],
		].do({ arg item, i;
			var holdModule, holdModuleClass, holdView, holdDelButton;
			holdModule = item.at(0);
			holdView = item.at(1);
			holdDelButton= item.at(2);
			// go to next line
			column.decorator.nextLine;
			// view - insert
			if (holdModule.notNil, {
				// shift decorator
				column.decorator.shift(0,4);
				holdView = Button(column, guiWidth-16 @ guiRowHeight);
				holdView.states = [[holdModule.instName,
					this.moduleStringColour(holdModule),
					chanColour]
				];
				holdView.action = {
					this.channelHighlight;
					this.openModuleGui(holdModule)
				};
				// button insert  delete
				holdDelButton = Button(column, 11 @ guiRowHeight);
				holdDelButton.states = [["d", TXColor.white, TXColor.sysDeleteCol]];
				holdDelButton.action = {
					this.channelHighlight;
					this.deactivate;
					holdModule.confirmDeleteModule;
					this.activate;
					// recreate view
					system.showView;
				};
				},{
					holdView = PopUpMenu(column, guiWidth @ guiRowHeight)
					.stringColor_(chanColour).background_(TXColor.white);
					holdView.items = ["..."] ++ arrAllPossInsertNames;
					holdView.action = { |view|
						this.channelHighlight;
						if (view.value > 0, {
							holdModuleClass = arrAllPossInsertClasses.at(view.value - 1);
							if (holdModuleClass.notNil, {
								this.deactivate;
								// ask system to add new module and put into instance variable
								[	{insert1Module = system.addModule(holdModuleClass);
									TXChannelRouting.displayModule = insert1Module;
									TXChannelRouting.showModuleBox = true;
									TXChannelRouting.setStartChannel(this.channelNo);
									TXSignalFlow.setPositionNear(insert1Module, this);
									system.addHistoryEvent;
									},
									{insert2Module = system.addModule(holdModuleClass);
										TXChannelRouting.displayModule = insert2Module;
										TXChannelRouting.showModuleBox = true;
										TXChannelRouting.setStartChannel(this.channelNo);
										TXSignalFlow.setPositionNear(insert2Module, insert1Module ? this);
										system.addHistoryEvent;
									},
									{insert3Module = system.addModule(holdModuleClass);
										TXChannelRouting.displayModule = insert3Module;
										TXChannelRouting.showModuleBox = true;
										TXChannelRouting.setStartChannel(this.channelNo);
										TXSignalFlow.setPositionNear(insert3Module,
											insert2Module ? insert1Module ? this);
										system.addHistoryEvent;
									},
									{insert4Module = system.addModule(holdModuleClass);
										TXChannelRouting.displayModule = insert4Module;
										TXChannelRouting.showModuleBox = true;
										TXChannelRouting.setStartChannel(this.channelNo);
										TXSignalFlow.setPositionNear(insert4Module,
											insert3Module ? insert2Module ? insert1Module ? this);
										system.addHistoryEvent;
									},
									{insert5Module = system.addModule(holdModuleClass);
										TXChannelRouting.displayModule = insert5Module;
										TXChannelRouting.showModuleBox = true;
										TXChannelRouting.setStartChannel(this.channelNo);
										TXSignalFlow.setPositionNear(insert5Module,
											insert4Module ? insert3Module ? insert2Module
											? insert1Module ? this);
										system.addHistoryEvent;
									}
								].at(i).value;
								// assign busses
								[	{ arrInsert1Outs = [];
									insert1Module.class.noOutChannels.do({ arg item, i;
										arrInsert1Outs = arrInsert1Outs.add(insert1Module.outBus.index + i);
									});
									},
									{arrInsert2Outs =  [];
										insert2Module.class.noOutChannels.do({ arg item, i;
											arrInsert2Outs = arrInsert2Outs.add(insert2Module.outBus.index + i);
										});
									},
									{arrInsert3Outs =  [];
										insert3Module.class.noOutChannels.do({ arg item, i;
											arrInsert3Outs = arrInsert3Outs.add(insert3Module.outBus.index + i);
										});
									},
									{arrInsert4Outs =  [];
										insert4Module.class.noOutChannels.do({ arg item, i;
											arrInsert4Outs = arrInsert4Outs.add(insert4Module.outBus.index + i);
										});
									},
									{arrInsert5Outs =  [];
										insert5Module.class.noOutChannels.do({ arg item, i;
											arrInsert5Outs = arrInsert5Outs.add(insert5Module.outBus.index + i);
										});
									},
								].at(i).value;
								this.activate;
								// update screen
								system.showView;
							});
							view.value = 0;
						});
					};
			});

		});    // end of create inserts .do
		// view - - destination
		column.decorator.nextLine;
		if (destModule.notNil, {
			// shift decorator
			column.decorator.shift(0,4);
			// change button width
			viewDest = Button(column, guiWidth-16 @ guiRowHeight);
			viewDest.states = [[destModule.instName, this.moduleStringColour(destModule),
				chanColour]
			];
			viewDest.action = {
				this.channelHighlight;
				this.openModuleGui(destModule)
			};
			// button destination delete
			btnDestDel = Button(column, 11 @ guiRowHeight);
			btnDestDel.states = [["d", TXColor.white, TXColor.sysDeleteCol]];
			btnDestDel.action = {
				this.channelHighlight;
				this.deactivate;
				// clear destination variables
				destModule = nil;
				arrDestBusses = nil;
				destBusNo = 0;
				arrDestOuts = nil;
				// store 0 to synthArgSpecs
				this.setSynthArgSpec("DestBusInd", 0);
				// recreate view
				system.showView;
			};
			},{
				viewDest = PopUpMenu(column, guiWidth @ guiRowHeight)
				.stringColor_(TXColor.white).background_(TXColor.sysEditCol);
				viewDest.items = ["add destination"] ++ arrAllDestModNames;
				viewDest.action = { |view|
					this.channelHighlight;
					this.deactivate;
					destModule = arrAllDestModules.at(view.value - 1);
					if (channelRate ==  "audio", {
						arrDestBusses = destModule.arrAudSCInBusChoices;
						}, {
							arrDestBusses = destModule.arrCtlSCInBusChoices;
					});
					destName = destModule.instName;
					//  assign busses
					if (arrDestBusses.notNil, {
						arrDestOuts = arrDestBusses.at(destBusNo ? 0).at(1);  // array of bus indices
					});
					this.activate;
					// recreate view
					system.showView;
				};
		});
		// go to next line
		column.decorator.nextLine;
		//  get destination bus names
		if (destModule.notNil, {
			arrDestBusNames = arrDestBusses.collect({ arg item, i; item.at(0); });
			}, {
				arrDestBusNames = [" - "];
		});
		// view - destination bus selection
		viewDestBus = PopUpMenu(column, guiWidth @ guiRowHeight)
		.stringColor_(TXColor.white).background_(chanColour)
		.items_(arrDestBusNames); // show all destination bus names
		// get  data from synthArgSpecs
		viewDestBus.value = this.getSynthArgSpec("DestBusInd") ? 0;
		viewDestBus.action = { |view|
			this.channelHighlight;
			this.deactivate;
			// store current data to synthArgSpecs
			this.setSynthArgSpec("DestBusInd", view.value);
			// store current value to destBusNo
			destBusNo = view.value;
			// assign busses
			arrDestOuts = arrDestBusses.at(view.value).at(1);  // array of bus indices
			this.activate;
			// recreate view
			system.showView;
		};
		// add to arrControls
		arrControls = arrControls.add(viewDestBus);
		// go to next line
		column.decorator.nextLine;
		// if error display message
		if (chanError.notNil, {
			// go to next line
			column.decorator.nextLine;
			// show text
			StaticText(column, guiWidth @ 150)
			.stringColor_(TXColor.sysEditCol)
			.background_(TXColor.white)
			.string_(chanError);
		});
		// go to next line
		column.decorator.nextLine;
		// ======================= ======================= ======================= =======================
		// if control channel, add certain controls
		if ((chanStatus == "active") and: (channelRate ==  "control"), {
			// adjust position
			column.decorator.shift(0,122);
			// slider - volume
			column.decorator.nextLine;
			column.decorator.shift(0, 10);
			// add volume buttons
			[90, 70, 50, 30, 10].do({ arg item, i;
				Button(column, 20 @ guiRowHeight)
				.states_([[item.asString, TXColor.white, chanColour]])
				.action_({
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("Volume", item / 100);
					// set current value on synths
					synthChannel.set("vol", item / 100);
					// update numberview
					holdVolNumberbox.value = item;
					// recreate view
					system.showView;
				});
				column.decorator.nextLine;
				column.decorator.shift(0, 3);
			});
			column.decorator.shift(0, -10 - (5 * (guiRowHeight + 7)));
			column.decorator.shift(24,0);
			arrControls = arrControls.add(
				holdVolSlider = Slider(column, 25 @ 150)
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("Volume"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("Volume", view.value);
					// set current value on synths
					synthChannel.set("vol", view.value);
					// update numberview
					holdVolNumberbox.value = (view.value * 100).round(0.01);
				});
			);
			// add screen update function
			this.createUpdFunc(holdVolSlider,"Volume");
			// checkbox - invert
			column.decorator.shift(5,40);
			arrControls = arrControls.add(
				holdInvChkBox = TXCheckBox (column, 54 @ guiRowHeight, "Invert",
					chanColour, TXColour.white, TXColour.white, chanColour)
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("Invert"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("Invert", view.value);
					// set current value on synth
					synthChannel.set("invert", view.value);
				});
			);
			// add screen update function
			this.createUpdFunc(holdInvChkBox,"Invert");
			// checkbox - off
			column.decorator.shift(-58,40);
			arrControls = arrControls.add(
				holdOffChkBox = TXCheckBox (column, 54 @ guiRowHeight, "Off", TXColour.sysDeleteCol, TXColour.white,
					TXColour.white, TXColour.sysDeleteCol)
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("Mute"))
				.action_({ |view|
					this.channelHighlight;
					// set current value on synth
					synthChannel.set("mute", view.value);
					// store current data to synthArgSpecs
					this.setSynthArgSpec("Mute", view.value);
				});
			);
			// add screen update function
			this.createUpdFunc(holdOffChkBox,"Mute");
			// numberbox - volume
			column.decorator.shift(-58,40);
			arrControls = arrControls.add(
				holdVolNumberbox = TXScrollNumBox (column, 40 @ guiRowHeight, [0,100].asSpec)
				// get  data from synthArgSpecs
				.value_((this.getSynthArgSpec("Volume") * 100).round(0.01) )
				.action_({ |view|
					this.channelHighlight;
					view.value = view.value.max(0).min(100);
					view.focus(false);
					// update sliderview
					holdVolSlider.valueAction = view.value/100;
				});
			);
			// add screen update function
			system.addScreenUpdFunc(
				[holdVolNumberbox],
				{ arg argArray;
					var argView = argArray.at(0);
					argView.value_((this.getSynthArgSpec("Volume") * 100).round(0.01) );
				}
			);
		});	// end of if channelRate ==  "control"
		// ======================= ======================= ======================= =======================
		// if audio channel add certain controls
		if ((chanStatus == "active") and: (channelRate ==  "audio"), {
			// FX Send 1
			column.decorator.nextLine;
			// checkbox - FX Send 1 On
			arrControls = arrControls.add(
				holdFxSnd1Btn = Button(column, 18 @ guiRowHeight)
				.states_([
					[" ", chanColour, TXColour.white],
					["1", TXColour.white, chanColour]
				])
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("FXSend1On"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("FXSend1On", view.value);
					// turn synth on/off
					if (view.value == 1, {synthFXSend1.run(true)}, {synthFXSend1.run(false)});
				});
			);
			// add screen update function
			this.createUpdFunc(holdFxSnd1Btn,"FXSend1On");
			// slider - FX Send 1
			arrControls = arrControls.add(
				holdFxSnd1Sldr = Slider(column, 116 @ guiRowHeight)
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("FXSend1Val"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("FXSend1Val", view.value);
					// set current value on synth
					synthFXSend1.set("send", view.value);
				});
			);
			// add screen update function
			this.createUpdFunc(holdFxSnd1Sldr,"FXSend1Val");
			// FX Send 2
			column.decorator.nextLine;
			// checkbox - FX Send 2 On
			arrControls = arrControls.add(
				holdFxSnd2Btn = Button(column, 18 @ guiRowHeight)
				.states_([
					[" ", chanColour, TXColour.white],
					["2 ", TXColour.white, chanColour]
				])
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("FXSend2On"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("FXSend2On", view.value);
					// turn synth on/off
					if (view.value == 1, {synthFXSend2.run(true)}, {synthFXSend2.run(false)});
				});
			);
			// add screen update function
			this.createUpdFunc(holdFxSnd2Btn,"FXSend2On");
			// slider - FX Send 2
			arrControls = arrControls.add(
				holdFxSnd2Sldr = Slider(column, 116 @ guiRowHeight)
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("FXSend2Val"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("FXSend2Val", view.value);
					// set current value on synth
					synthFXSend2.set("send", view.value);
				});
			);
			// add screen update function
			this.createUpdFunc(holdFxSnd2Sldr,"FXSend2Val");
			// FX Send 3
			column.decorator.nextLine;
			// checkbox - FX Send 3 On
			arrControls = arrControls.add(
				holdFxSnd3Btn = Button(column, 18 @ guiRowHeight)
				.states_([
					[" ", chanColour, TXColour.white],
					["3 ", TXColour.white, chanColour]
				])
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("FXSend3On"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("FXSend3On", view.value);
					// turn synth on/off
					if (view.value == 1, {synthFXSend3.run(true)}, {synthFXSend3.run(false)});
				});
			);
			// add screen update function
			this.createUpdFunc(holdFxSnd3Btn,"FXSend3On");
			// slider - FX Send 3
			arrControls = arrControls.add(
				holdFxSnd3Sldr = Slider(column, 116 @ guiRowHeight)
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("FXSend3Val"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("FXSend3Val", view.value);
					// set current value on synth
					synthFXSend3.set("send", view.value);
				});
			);
			// add screen update function
			this.createUpdFunc(holdFxSnd3Sldr,"FXSend3Val");
			// FX Send 4
			column.decorator.nextLine;
			// checkbox - FX Send 4 On
			arrControls = arrControls.add(
				holdFxSnd4Btn = Button(column, 18 @ guiRowHeight)
				.states_([
					[" ", chanColour, TXColour.white],
					["4", TXColour.white, chanColour]
				])
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("FXSend4On"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("FXSend4On", view.value);
					// turn synth on/off
					if (view.value == 1, {synthFXSend4.run(true)}, {synthFXSend4.run(false)});
				});
			);
			// add screen update function
			this.createUpdFunc(holdFxSnd4Btn,"FXSend4On");
			// slider - FX Send 4
			arrControls = arrControls.add(
				holdFxSnd4Sldr = Slider(column, 116 @ guiRowHeight)
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("FXSend4Val"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("FXSend4Val", view.value);
					// set current value on synth
					synthFXSend4.set("send", view.value);
				});
			);
			// add screen update function
			this.createUpdFunc(holdFxSnd4Sldr,"FXSend4Val");

			// pan - only display if stereo channel
			if (arrDestOuts.size == 2, {
				column.decorator.nextLine;
				// button - pan centre
				btnPanCentre = Button(column, 18 @ guiRowHeight);
				btnPanCentre.states = [["=", TXColor.white, chanColour]];
				btnPanCentre.action = {
					this.channelHighlight;
					arrControls.at(10).valueAction_(0.5);
				};
				// slider - pan
				arrControls = arrControls.add(
					holdPanSldr = Slider(column, 116 @ guiRowHeight)
					// get  data from synthArgSpecs
					.value_(this.getSynthArgSpec("Pan"))
					.action_({ |view|
						this.channelHighlight;
						// store current data to synthArgSpecs
						this.setSynthArgSpec("Pan", view.value);
						// set current value on synth
						synthChannel.set("pan", view.value);
					});
				);
				// add screen update function
				this.createUpdFunc(holdPanSldr,"Pan");
				},{
					column.decorator.shift(0, guiRowHeight+4);
			});

			column.decorator.nextLine;
			column.decorator.shift(0, 17);
			// add volume buttons
			[90, 70, 50, 30, 10].do({ arg item, i;
				Button(column, 20 @ guiRowHeight)
				.states_([[item.asString, TXColor.white, chanColour]])
				.action_({
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("Volume", item / 100);
					// set current value on synth
					synthChannel.set("vol", item / 100);
					synthFXSend1.set("vol", item / 100);
					synthFXSend2.set("vol", item / 100);
					synthFXSend3.set("vol", item / 100);
					synthFXSend4.set("vol", item / 100);
					// update numberview
					holdVolNumberbox.value = item;
					// recreate view
					system.showView;
				});
				column.decorator.nextLine;
				column.decorator.shift(0, 3);
			});
			column.decorator.shift(0, -10 - (5 * (guiRowHeight + 7)));
			column.decorator.shift(24, 0);
			// slider - volume
			arrControls = arrControls.add(
				holdVolSlider = Slider(column, 25 @ 150)
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("Volume"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("Volume", view.value);
					// set current value on synth
					synthChannel.set("vol", view.value);
					synthFXSend1.set("vol", view.value);
					synthFXSend2.set("vol", view.value);
					synthFXSend3.set("vol", view.value);
					synthFXSend4.set("vol", view.value);
					// update numberview
					holdVolNumberbox.value = (view.value * 100).round(0.01);
				});
			);
			// add screen update function
			this.createUpdFunc(holdVolSlider,"Volume");
			// checkbox - solo
			column.decorator.shift(5,40);
			arrControls = arrControls.add(
				TXCheckBox (column, 54 @ guiRowHeight, "Solo",
					chanColour, TXColour.white, TXColour.white, chanColour)
				.value_(this.getSynthArgSpec("Solo"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("Solo", view.value);
					if (view.value == 1, {
						if (globalSoloMode.value == 0, {
							holdMuteStatus = this.getSynthArgSpec("Mute");
						});
						// set channel mute
						this.setMute(0);
						this.class.setGlobalSoloOn;
						}, {
							// set channel mute
							this.setMute(1);
							this.class.setGlobalSoloOff;
					});
					// recreate view
					system.showView;
				});
			);
			// checkbox - mute
			column.decorator.shift(-58,40);
			arrControls = arrControls.add(
				holdMuteChkBox = TXCheckBox (column, 54 @ guiRowHeight, "Mute",
					TXColour.sysDeleteCol, TXColour.white, TXColour.white, TXColour.sysDeleteCol)
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("Mute"))
				.action_({ |view|
					this.channelHighlight;
					// set channel mute
					this.setMute(view.value);
					// refresh view
					//		view.parent.refresh;
				});
			);
			// add screen update function
			this.createUpdFunc(holdMuteChkBox,"Mute");
			// numberbox - volume
			column.decorator.shift(-58,40);
			arrControls = arrControls.add(
				holdVolNumberbox = TXScrollNumBox (column, 40 @ guiRowHeight, [0,100].asSpec)
				// get  data from synthArgSpecs
				.value_((this.getSynthArgSpec("Volume") * 100).round(0.01) )
				.action_({ |view|
					this.channelHighlight;
					view.value = view.value.max(0).min(100).round(0.01);
					view.focus(false);
					// update sliderview
					holdVolSlider.valueAction = view.value/100;
				});
			);
			// add screen update function
			system.addScreenUpdFunc(
				[holdVolNumberbox],
				{ arg argArray;
					var argView = argArray.at(0);
					argView.value_((this.getSynthArgSpec("Volume") * 100).round(0.01) );
				}
			);
		});	// end of if channelRate ==  "audio"
	} // end of method makeChannelGui

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	channelHighlight {
		if (TXChannelRouting.displayChannel.notNil and:
			{TXChannelRouting.displayChannel.column.notClosed}, {
				TXChannelRouting.displayChannel.column.background = TXColour.sysChannelAudio;
		});
		TXChannelRouting.displayChannel = this;
		column.background = TXColour.sysChannelHighlight;
	}

	moduleStringColour { arg argModule;
		if (TXChannelRouting.displayModule == argModule, {
			^TXColor.sysSelectedModString;
			},{
				^TXColor.white;
		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	createUpdFunc {arg argView, argSynthArgSpec;
		// add screen update function
		system.addScreenUpdFunc(
			[argView],
			{ arg argArray;
				var argView = argArray.at(0);
				argView.value_(this.getSynthArgSpec(argSynthArgSpec));
			}
		);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	activate {
		chanError = nil;
		// adjust for empty insert slots by assigning busses
		if (insert1Module.isNil, {arrInsert1Outs = arrSourceOuts});
		if (insert2Module.isNil, {arrInsert2Outs = arrInsert1Outs});
		if (insert3Module.isNil, {arrInsert3Outs = arrInsert2Outs});
		if (insert4Module.isNil, {arrInsert4Outs = arrInsert3Outs});
		if (insert5Module.isNil, {arrInsert5Outs = arrInsert4Outs});
		// check insert modules
		if (insert1Module.notNil, {
			if (arrSourceOuts.size != insert1Module.class.noInChannels, {
				^chanError = "Error: Insert 1 must have" + arrSourceOuts.size.asString
				+ "input channel(s)";
				},{
					// update module's data
					insert1Module.setInputBusses(arrSourceOuts);
			});
		});
		if (insert2Module.notNil, {
			if (arrInsert1Outs.size != insert2Module.class.noInChannels, {
				^chanError = "Error: Insert 2 must have" + arrInsert1Outs.size.asString
				+ "input channel(s)";
				},{
					// update module's data
					insert2Module.setInputBusses(arrInsert1Outs);
			});
		});
		if (insert3Module.notNil, {
			if (arrInsert2Outs.size != insert3Module.class.noInChannels, {
				^chanError = "Error: Insert 3 must have" + arrInsert2Outs.size.asString
				+ "input channel(s)";
				},{
					// update module's data
					insert3Module.setInputBusses(arrInsert2Outs);
			});
		});
		if (insert4Module.notNil, {
			if (arrInsert3Outs.size != insert4Module.class.noInChannels, {
				^chanError = "Error: Insert 4 must have" + arrInsert3Outs.size.asString
				+ "input channel(s)";
				},{
					// update module's data
					insert4Module.setInputBusses(arrInsert3Outs);
			});
		});
		if (insert5Module.notNil, {
			if (arrInsert4Outs.size != insert5Module.class.noInChannels, {
				^chanError = "Error: Insert 5 must have" + arrInsert4Outs.size.asString
				+ "input channel(s)";
				},{
					// update module's data
					insert5Module.setInputBusses(arrInsert4Outs);
			});
		});
		// check destination module
		if (destModule.isNil, {
			^0;
		});
		if ( (arrInsert5Outs.size != arrDestOuts.size) // if unequal no. of channels & not mono to stereo
			and: ((arrInsert5Outs.size != 1) or: (arrDestOuts.size != 2)), {
				^chanError = "Error: destination bus does not have enough channels";
		});
		if ((channelRate ==  "control") and: (destModule.notNil), {
			if ((destModule.myArrCtlSCInBusSpecs.size > 0), {
				if ((destModule.myArrCtlSCInBusSpecs.at(destBusNo).at(3) == 0), {
					// instead of flagging error, turn modulation option on
					destModule.myArrCtlSCInBusSpecs.at(destBusNo).put(3, 1);
					// rebuild synth
					system.requestModuleRebuild(destModule);
				});
			});
		});
		// if no errors, build channel synth
		this.makeSynthChannel;
		// set channel status
		chanStatus = "active";
		^0;
	} 	// end of .activate

	/////////////////////////////////////////////////////////////////////////////////


	deactivate {
		chanError = nil;
		if (chanStatus == "active", {
			// set channel status
			chanStatus = "edit";
			// free synths
			synthChannel.free;
			if (channelRate ==  "audio", {
				synthFXSend1.free;
				synthFXSend2.free;
				synthFXSend3.free;
				synthFXSend4.free;
			});
			// if control channel, set dest bus to zero
			if ((channelRate ==  "control") and: (destModule.notNil), {
				destModule.arrCtlSCInBusses.at(destBusNo ? 0).set(0);
			});
		});
	}

	reactivate {
		//	reactivate
		if (reactivateOn == true, {
			Routine.run {
				system.server.sync;
				this.activate;
			}
		});
	}

	////////////////////////////////////////////////////////////////////////////////

	openModuleGui{ arg argModule;
		TXChannelRouting.display(argModule);
		system.addHistoryEvent;
	}

	///////////////////////////////////////////////////////////////////////////////

	makeSynthChannel {
		var synthDefName, holdL, holdR, holdMethod;
		//	build side chain mappings
		arrCtlSCInBusSpecs.do({ arg item, i;
			if (item.at(3) != 0, {
				// create arrays for mapping synth arguments to busses
				arrCtlSCInBusMappings = arrCtlSCInBusMappings.add(item.at(2));// synth arg index no
				arrCtlSCInBusMappings = arrCtlSCInBusMappings
				.add("c" ++ arrCtlSCInBusses.at(i).index.asString);// bus index no
			});
		});
		//	if control rate
		if (channelRate ==  "control", {
			//	create main synth on server
			synthChannel = Synth("TXChannelControl1",
				[	"in", arrInsert5Outs.at(0),
					"out", arrDestOuts.at(0),
					"i_numInputs", arrInsert5Outs.size,
					"i_numOutputs", arrDestOuts.size,
					"vol", this.getSynthArgSpec("Volume"),
					"invert", this.getSynthArgSpec("Invert"),
					"mute", this.getSynthArgSpec("Mute"),
				] ++ arrCtlSCInBusMappings, // add side chain input bus mappings
				group.nodeID,
				\addToTail ;
			);
		});

		//	if audio rate
		if (channelRate ==  "audio", {
			if (arrDestOuts.size == 1, {
				//	create main synth on server
				synthChannel = Synth("TXChannelAudio1",
					[	"in", arrInsert5Outs.at(0),
						"out", arrDestOuts.at(0),
						"pan", this.getSynthArgSpec("Pan"),
						"vol", this.getSynthArgSpec("Volume"),
						"mute", this.getSynthArgSpec("Mute"),
					] ++ arrCtlSCInBusMappings, // add side chain input bus mappings
					group.nodeID,
					\addToTail ;
				);
				//	create FX synths on server
				if (this.getSynthArgSpec("FXSend1On") == 0, {
					holdMethod = \newPaused;
					}, {
						holdMethod = \new;
				});
				synthFXSend1 = Synth.perform(holdMethod, "TXChannelFX1",
					[	"in", arrInsert5Outs.at(0),
						"out", system.arrFXSendBusses.at(0).outBus.index,
						"vol", this.getSynthArgSpec("Volume"),
						"mute", this.getSynthArgSpec("Mute"),
						"send", this.getSynthArgSpec("FXSend1Val"),
					] ++ arrCtlSCInBusMappings, // add side chain input bus mappings
					group.nodeID,
					\addToTail;
				);
				if (this.getSynthArgSpec("FXSend2On") == 0, {
					holdMethod = \newPaused;
					}, {
						holdMethod = \new;
				});
				synthFXSend2 = Synth.perform(holdMethod, "TXChannelFX1",
					[	"in", arrInsert5Outs.at(0),
						"out", system.arrFXSendBusses.at(1).outBus.index,
						"vol", this.getSynthArgSpec("Volume"),
						"mute", this.getSynthArgSpec("Mute"),
						"send", this.getSynthArgSpec("FXSend2Val"),
					] ++ arrCtlSCInBusMappings, // add side chain input bus mappings
					group.nodeID,
					\addToTail;
				);
				if (this.getSynthArgSpec("FXSend3On") == 0, {
					holdMethod = \newPaused;
					}, {
						holdMethod = \new;
				});
				synthFXSend3 = Synth.perform(holdMethod, "TXChannelFX1",
					[	"in", arrInsert5Outs.at(0),
						"out", system.arrFXSendBusses.at(2).outBus.index,
						"vol", this.getSynthArgSpec("Volume"),
						"mute", this.getSynthArgSpec("Mute"),
						"send", this.getSynthArgSpec("FXSend3Val"),
					] ++ arrCtlSCInBusMappings, // add side chain input bus mappings
					group.nodeID,
					\addToTail;
				);
				if (this.getSynthArgSpec("FXSend4On") == 0, {
					holdMethod = \newPaused;
					}, {
						holdMethod = \new;
				});
				synthFXSend4 = Synth.perform(holdMethod, "TXChannelFX1",
					[	"in", arrInsert5Outs.at(0),
						"out", system.arrFXSendBusses.at(3).outBus.index,
						"vol", this.getSynthArgSpec("Volume"),
						"mute", this.getSynthArgSpec("Mute"),
						"send", this.getSynthArgSpec("FXSend4Val"),
					] ++ arrCtlSCInBusMappings, // add side chain input bus mappings
					group.nodeID,
					\addToTail;
				);
			});
			if (arrDestOuts.size == 2, {
				if (arrInsert5Outs.size == 1, {
					holdL = arrInsert5Outs.at(0);
					holdR = holdL;
					}, {
						holdL = arrInsert5Outs.at(0);
						holdR = arrInsert5Outs.at(1);
				});
				//	create main synth on server
				synthChannel = Synth("TXChannelAudio2",
					[	"inL", holdL,
						"inR", holdR,
						"outL", arrDestOuts.at(0),
						"outR", arrDestOuts.at(1),
						"vol", this.getSynthArgSpec("Volume"),
						"pan", this.getSynthArgSpec("Pan"),
						"mute", this.getSynthArgSpec("Mute"),
					] ++ arrCtlSCInBusMappings, // add side chain input bus mappings
					group.nodeID,
					\addToTail ;
				);
				//	create FX synths on server
				if (this.getSynthArgSpec("FXSend1On") == 0, {
					holdMethod = \newPaused;
					}, {
						holdMethod = \new;
				});
				synthFXSend1 = Synth.perform(holdMethod, "TXChannelFX2",
					[	"inL", holdL,
						"inR", holdR,
						"out", system.arrFXSendBusses.at(0).outBus.index,
						"vol", this.getSynthArgSpec("Volume"),
						"mute", this.getSynthArgSpec("Mute"),
						"send", this.getSynthArgSpec("FXSend1Val"),
					] ++ arrCtlSCInBusMappings, // add side chain input bus mappings
					group.nodeID,
					\addToTail;
				);
				if (this.getSynthArgSpec("FXSend2On") == 0, {
					holdMethod = \newPaused;
					}, {
						holdMethod = \new;
				});
				synthFXSend2 = Synth.perform(holdMethod, "TXChannelFX2",
					[	"inL", holdL,
						"inR", holdR,
						"out", system.arrFXSendBusses.at(1).outBus.index,
						"vol", this.getSynthArgSpec("Volume"),
						"mute", this.getSynthArgSpec("Mute"),
						"send", this.getSynthArgSpec("FXSend2Val"),
					] ++ arrCtlSCInBusMappings, // add side chain input bus mappings
					group.nodeID,
					\addToTail;
				);
				if (this.getSynthArgSpec("FXSend3On") == 0, {
					holdMethod = \newPaused;
					}, {
						holdMethod = \new;
				});
				synthFXSend3 = Synth.perform(holdMethod, "TXChannelFX2",
					[	"inL", holdL,
						"inR", holdR,
						"out", system.arrFXSendBusses.at(2).outBus.index,
						"vol", this.getSynthArgSpec("Volume"),
						"mute", this.getSynthArgSpec("Mute"),
						"send", this.getSynthArgSpec("FXSend3Val"),
					] ++ arrCtlSCInBusMappings, // add side chain input bus mappings
					group.nodeID,
					\addToTail;
				);
				if (this.getSynthArgSpec("FXSend4On") == 0, {
					holdMethod = \newPaused;
					}, {
						holdMethod = \new;
				});
				synthFXSend4 = Synth.perform(holdMethod, "TXChannelFX2",
					[	"inL", holdL,
						"inR", holdR,
						"out", system.arrFXSendBusses.at(3).outBus.index,
						"vol", this.getSynthArgSpec("Volume"),
						"mute", this.getSynthArgSpec("Mute"),
						"send", this.getSynthArgSpec("FXSend4Val"),
					] ++ arrCtlSCInBusMappings, // add side chain input bus mappings
					group.nodeID,
					\addToTail;
				);
			});
			//	create ERROR screen until coded for more than 2 channels:
			if (arrDestOuts.size > 2, {
				TXInfoScreen.new("SYSTEM ERROR - FOR NOW TXCHANNEL CLASS CAN ONLY DEAL WITH MONO & STEREO AUDIO BUSSES");
			});
		});	//	end of if audio rate
	} // end of makeSynthChannel

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	sortKey { arg argSortOption;
		var holdRateKey, holdKey;
		//  from TXChannelRouting:
		//	popSortOption.items = [
		//		"Order: Channel No.",
		//		"Order: Control by Source, Audio by Source",
		//		"Order: Control by Source, Audio by Destination",
		//		"Order: Control by Destination, Audio by Source",
		//		"Order: Control by Destination, Audio by Destination"
		//	];
		if (channelRate ==  "audio", {
			holdRateKey = "a";
			holdKey = [channelNo, sourceName.asSymbol, destName.asSymbol, sourceName.asSymbol, destName.asSymbol]
			.at(argSortOption) ? \zzz;
			}, {
				holdRateKey = "c";
				holdKey = [channelNo, sourceName.asSymbol, sourceName.asSymbol, destName.asSymbol, destName.asSymbol]
				.at(argSortOption) ? \zzz;
		});
		if (argSortOption == 0, {
			^holdKey;
			},{
				^(holdKey.asString ++ holdRateKey).asSymbol;
		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	moveToPosition { arg newPos;
		if (newPos != channelNo, {
			if (newPos < channelNo, {
				channelNo = (newPos - 0.5)
				}, {
					channelNo = (newPos + 0.5)
			});
			// sort channels
			arrInstances.sort({ arg a, b;   a.sortKey(0) <= b.sortKey(0);});
			this.class.renumberInsts;
			TXChannelRouting.sortChannels;
			TXChannelRouting.reorderChannelSynths;
		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	markForDeletion {
		toBeDeletedStatus = true;
	}

	deleteChannel {
		// post message
		//	("Deleting Channel: " ++ this.instName).postln;
		// set status
		toBeDeletedStatus = false;
		deletedStatus = true;
		// remove from class's arrInstances and renumber arrInstances
		arrInstances.remove(this);
		this.class.renumberInsts;
		// stop synths
		this.deactivate;
	}

	checkDeletions {
		var holdChanStatus;
		holdChanStatus = chanStatus;
		deactivateOn = false;
		reactivateOn = false;
		//	if toBeDeletedStatus is true or source deleted, delete inserts &  channel
		if ( (toBeDeletedStatus == true) or: (sourceModule.deletedStatus == true), {
			this.toBeDeletedStatus = true; // set channel to be deleted
			if (insert1Module.notNil, {
				if (insert1Module.deletedStatus == false, {
					insert1Module.toBeDeletedStatus = true; // set insert to be deleted
				});
			});
			if (insert2Module.notNil, {
				if (insert2Module.deletedStatus == false, {
					insert2Module.toBeDeletedStatus = true; // set insert to be deleted
				});
			});
			if (insert3Module.notNil, {
				if (insert3Module.deletedStatus == false, {
					insert3Module.toBeDeletedStatus = true; // set insert to be deleted
				});
			});
			if (insert4Module.notNil, {
				if (insert4Module.deletedStatus == false, {
					insert4Module.toBeDeletedStatus = true; // set insert to be deleted
				});
			});
			if (insert5Module.notNil, {
				if (insert5Module.deletedStatus == false, {
					insert5Module.toBeDeletedStatus = true; // set insert to be deleted
				});
			});
			^0; // return 0 - forced return
		});

		//	check inserts - if deleted deactivate and reactivate
		if (insert1Module.notNil, {
			if (insert1Module.deletedStatus == true, {
				deactivateOn = true;
				reactivateOn = true;
				insert1Module = nil;
			});
		});
		if (insert2Module.notNil, {
			if (insert2Module.deletedStatus == true, {
				deactivateOn = true;
				reactivateOn = true;
				insert2Module = nil;
			});
		});
		if (insert3Module.notNil, {
			if (insert3Module.deletedStatus == true, {
				deactivateOn = true;
				reactivateOn = true;
				insert3Module = nil;
			});
		});
		if (insert4Module.notNil, {
			if (insert4Module.deletedStatus == true, {
				deactivateOn = true;
				reactivateOn = true;
				insert4Module = nil;
			});
		});
		if (insert5Module.notNil, {
			if (insert5Module.deletedStatus == true, {
				deactivateOn = true;
				reactivateOn = true;
				insert5Module = nil;
			});
		});

		//	check dest - if deleted, deactivate
		if (destModule.notNil, {
			if ((destModule.toBeDeletedStatus == true) or: (destModule.deletedStatus == true), {
				deactivateOn = true;
				reactivateOn = false;
				destModule = nil;
			});
		});

		//	deactivate
		if (deactivateOn == true, {
			this.deactivate;
		});

		//	reactivate if stored channel status is "active"
		if (reactivateOn == true and: holdChanStatus == "active", {
			this.reactivate;
		});
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	checkRebuilds {
		var holdChanStatus;
		holdChanStatus = chanStatus;
		deactivateOn = false;
		reactivateOn = false;

		//	check source - if rebuilt deactivate and reactivate
		if (sourceModule.rebuiltStatus == true, {
			deactivateOn = true;
			reactivateOn = true;
		});
		//	check inserts  & dest - if rebuilt deactivate and reactivate
		if (insert1Module.notNil, {
			if (insert1Module.rebuiltStatus == true, {
				deactivateOn = true;
				reactivateOn = true;
			});
		});
		if (insert2Module.notNil, {
			if (insert2Module.rebuiltStatus == true, {
				deactivateOn = true;
				reactivateOn = true;
			});
		});
		if (insert3Module.notNil, {
			if (insert3Module.rebuiltStatus == true, {
				deactivateOn = true;
				reactivateOn = true;
			});
		});
		if (insert4Module.notNil, {
			if (insert4Module.rebuiltStatus == true, {
				deactivateOn = true;
				reactivateOn = true;
			});
		});
		if (insert5Module.notNil, {
			if (insert5Module.rebuiltStatus == true, {
				deactivateOn = true;
				reactivateOn = true;
			});
		});
		if (destModule.notNil, {
			if (destModule.rebuiltStatus == true, {
				deactivateOn = true;
				reactivateOn = true;
			});
		});

		//	deactivate
		if (deactivateOn == true, {
			this.deactivate;
		});

		//	reactivate if stored channel status is "active"
		if (reactivateOn == true and: holdChanStatus == "active", {
			this.reactivate;
		});
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	setMute {	arg argMute;  // set mute
		// set current value on synths
		synthChannel.set("mute", argMute);
		synthFXSend1.set("mute", argMute);
		synthFXSend2.set("mute", argMute);
		synthFXSend3.set("mute", argMute);
		synthFXSend4.set("mute", argMute);
		// store current data to synthArgSpecs
		this.setSynthArgSpec("Mute", argMute);
	}

	globSoloModeOn {	// run various actions when global solo mode is on
		if (this.getSynthArgSpec("Solo") == 0, {		// ignore if this channel is solo'd
			holdMuteStatus = this.getSynthArgSpec("Mute");
			// set channel mute
			this.setMute(1);
		});
	}

	globSoloModeOff {	// run various actions when global solo mode is off
		this.setMute(holdMuteStatus);
	}

	// override TXModuleBase method
	setSynthValue { arg argSynthArgString, argVal;

		// if active control channel
		if ((chanStatus == "active") and: (channelRate ==  "control"), {
			// set current value on node
			if (argSynthArgString == "Volume", {
				synthChannel.set("vol", argVal);
			});
			if (argSynthArgString == "Invert", {
				synthChannel.set("invert", argVal);
			});
			if (argSynthArgString == "Mute", {
				synthChannel.set("mute", argVal);
			});
		});

		// if active audio channel
		if ((chanStatus == "active") and: (channelRate ==  "audio"), {

			if (argSynthArgString == "Volume", {
				// set current value on synth
				synthChannel.set("vol", argVal);
				synthFXSend1.set("vol", argVal);
				synthFXSend2.set("vol", argVal);
				synthFXSend3.set("vol", argVal);
				synthFXSend4.set("vol", argVal);
			});
			if (argSynthArgString == "Mute", {
				// set channel mute
				this.setMute(argVal);
			});
			if (argSynthArgString == "Pan", {
				// set current value on synth
				synthChannel.set("pan", argVal);
			});

			if (argSynthArgString == "FXSend1On", {
				// turn synth on/off
				if (argVal == 1, {synthFXSend1.run(true)}, {synthFXSend1.run(false)});
			});
			if (argSynthArgString == "FXSend1Val", {
				// set current value on synth
				synthFXSend1.set("send", argVal);
			});
			if (argSynthArgString == "FXSend2On", {
				// turn synth on/off
				if (argVal == 1, {synthFXSend2.run(true)}, {synthFXSend2.run(false)});
			});
			if (argSynthArgString == "FXSend2Val", {
				// set current value on synth
				synthFXSend2.set("send", argVal);
			});
			if (argSynthArgString == "FXSend3On", {
				// turn synth on/off
				if (argVal == 1, {synthFXSend3.run(true)}, {synthFXSend3.run(false)});
			});
			if (argSynthArgString == "FXSend3Val", {
				// set current value on synth
				synthFXSend3.set("send", argVal);
			});
			if (argSynthArgString == "FXSend4On", {
				// turn synth on/off
				if (argVal == 1, {synthFXSend4.run(true)}, {synthFXSend4.run(false)});
			});
			if (argSynthArgString == "FXSend4Val", {
				// set current value on synth
				synthFXSend4.set("send", argVal);
			});
		});
		// store to synthArgSpecs
		this.setSynthArgSpec(argSynthArgString, argVal);
	}

	sendSynthToTail {
		if (chanStatus == "active", {
			synthChannel.moveToTail(group);
		});
	}

	instSortingName {
		// adds zeros into instance no for correct sorting
		var holdZeros, newName;
		holdZeros = "0000".keep(5 - (channelNo.asString.size));
		newName = defaultName ++ " " ++ holdZeros ++ channelNo.asString;
		^newName;
	}
}
