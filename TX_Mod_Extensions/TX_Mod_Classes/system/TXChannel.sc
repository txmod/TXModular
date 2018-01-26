// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXChannel : TXModuleBase { //  Channel module
	classvar <>group;				// override for default group for adding channel internal groups to
	classvar <>arrInstances;
	classvar <defaultName;  		// default  name
	classvar <moduleType;			//  "channel"
	classvar <moduleRate;			// -not used for TXChannel
	classvar <noInChannels;			// -not used for TXChannel
	classvar <arrAudSCInBusSpecs; 	// -not used for TXChannel
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels=0;		// -not used for TXChannel
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar <guiLeft=100;
	classvar <guiTop=300;
	classvar <guiWidth=140;
	classvar <guiHeight=595;
	classvar <arrAllDestBusses;		// array of possible destination busses for channels
	classvar guiRowHeight = 20;		// default variable for gui
	classvar globalSoloMode = 0;

	var <channelRate;				// "audio" or "control", set for each channel
	var <internalGroup;				// for adding synths to
	var <sourceModule;
	var <sourceName;
	var <>channelNo;
	var <arrSourceBusses;
	var <sourceBusno = 0;
	var <insert1Module;
	var <insert2Module;
	var <insert3Module;
	var <insert4Module;
	var <insert5Module;
	var <destModule;
	var <destName;
	var <arrDestBusses;
	var <destBusNo = 0;
	var <chanColour, chanDestColour;
	var <>chanLabel;
	var <>chanError;
	var <chanStatus = "edit";			//  can be "edit" or "active"
	var <synthChannel, <synthFXSend1, <synthFXSend2, <synthFXSend3, <synthFXSend4;
	var <arrSourceOuts, <arrDestOuts;
	var <arrInsert1Outs, <arrInsert2Outs, <arrInsert3Outs;
	var <arrInsert4Outs, <arrInsert5Outs;
	var holdArrEditControlVals, holdArrActiveControlVals;  // Note - these 2 are no longer used
	var holdVolSlider, holdVolNumberbox, holdInvChkBox, holdOffChkBox, holdPanSldr, holdMuteChkBox;
	var holdFxSnd1Btn, holdFxSnd1Sldr, holdFxSnd2Btn, holdFxSnd2Sldr;
	var holdFxSnd3Btn, holdFxSnd3Sldr, holdFxSnd4Btn, holdFxSnd4Sldr;
	var holdFxSnd1Num, holdFxSnd2Num, holdFxSnd3Num, holdFxSnd4Num, holdPanNum;
	var deactivateOn, guiResetOn, <reactivateOn;
	var <holdMuteStatus = 0;
	var <column;
	var oscResponder, respfunc, resetfunc, meter, clip, peak, peakval;
	var <dbmax = 0.0, <dbmin = -60.0, <dbrange, <decay=60, <rate=10;

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
			{ arg in, out, pan = 0.5, vol = 0, mute = 0, modPan = 0, modVol = 0;
				var mixout, startEnv, inBus;
				var outVol, peak, trig;
				startEnv = TXEnvPresets.startEnvFunc.value;
				outVol = \amp.asSpec.map((vol + modVol).min(1).max(0));
				inBus = TXClean.ar(InFeedback.ar(in));
				mixout = TXClean.ar(startEnv * (inBus * outVol * 2 * (1-mute)));
				peak = PeakFollower.ar(mixout, 0.9999);
				trig = Impulse.ar(20);
				SendTrig.ar(trig, 0 , peak);
				Out.ar(out, mixout);
			}, [0, 0, \ir, defLagTime, defLagTime, \ir, defLagTime] // lag rates
		).add;
		SynthDef("TXChannelAudio2",
			{ arg inL, inR, outL,  outR, pan = 0.5, vol = 0, mute = 0, modPan = 0, modVol = 0;
				var startEnv, inBusL, inBusR, mixoutL, mixoutR, panSum;
				var outVol, peak, trig;
				startEnv = TXEnvPresets.startEnvFunc.value;
				outVol = \amp.asSpec.map((vol + modVol).min(1).max(0));
				inBusL = TXClean.ar(InFeedback.ar(inL));
				inBusR = TXClean.ar(InFeedback.ar(inR));
				panSum = (pan + modPan).min(1).max(0);
				mixoutL = TXClean.ar(startEnv * (inBusL * outVol * 2 * (1-mute) * (1-panSum)));
				mixoutR = TXClean.ar(startEnv * inBusR * outVol * 2 * (1-mute) * panSum);
				peak = PeakFollower.ar([mixoutL, mixoutR], 0.9999);
				trig = Impulse.ar(20);
				SendTrig.ar(trig, [0,1], peak);
				Out.ar(outL, mixoutL);
				Out.ar(outR, mixoutR);
			}, [0, 0, 0, 0, defLagTime, defLagTime, defLagTime, defLagTime, defLagTime] // lag rates
		).add;
		//	send the SynthDef for the main control channel
		SynthDef("TXChannelControl1",
			{ arg in, out, i_numInputs, i_numOutputs, vol = 0, invert = 0, mute = 0, modVol = 0;
				i_numOutputs.do({ arg item, i;
					var mixout, trig;
					mixout = TXClean.kr(In.kr(in+i) * (vol + modVol).min(1).max(0) * (1-mute) * (1-(2*invert)));
					trig = Impulse.kr(20);
					SendTrig.kr(trig, 0 , mixout);
					Out.kr((out+i), mixout);
				});
			}, [0, 0, \ir, \ir, defLagTime, defLagTime, defLagTime, defLagTime] // lag rates
		).add;
		//	send the SynthDef for FX sends
		SynthDef("TXChannelFX1",
			{ arg in, out, vol = 0, mute = 0, send = 0, modVol = 0, modSend = 0;
				var outVol, mixout, startEnv;
				startEnv = TXEnvPresets.startEnvFunc.value;
				outVol = \amp.asSpec.map((vol + modVol).min(1).max(0));
				mixout = InFeedback.ar(in) * outVol * (1-mute) * (send + modSend).min(1).max(0);
				Out.ar(out, TXClean.ar(startEnv * mixout));
			}, [0, 0, defLagTime, defLagTime, defLagTime, defLagTime, defLagTime] // lag rates
		).add;
		SynthDef("TXChannelFX2",
			{ arg inL, inR, out, vol = 0, mute = 0, send = 0, modVol = 0, modSend = 0;
				var outVol, mixin, mixout, startEnv;
				startEnv = TXEnvPresets.startEnvFunc.value;
				outVol = \amp.asSpec.map((vol + modVol).min(1).max(0));
				mixin = Mix.new([InFeedback.ar(inL), InFeedback.ar(inR)]);
				mixout = mixin * outVol * (1-mute) * (send + modSend).min(1).max(0);
				Out.ar(out, TXClean.ar(startEnv * mixout));
			}, [0, 0, 0, defLagTime, defLagTime, defLagTime, defLagTime, defLagTime] // lag rates
		).add;
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

	*adjustGuiWidth {arg wideChannelMode;
		if (wideChannelMode == true, {
			guiWidth=140;
		}, {
			guiWidth=90;
		});
	}

	*renumberInsts{
		arrInstances.do({ arg item, i;
			//	create instance name
			item.instName = defaultName ++ " " ++(i + 1).asString;
			item.channelNo = i + 1;
		});
	}

	*new { arg argSource, initLevel;
		^super.new.init(argSource, initLevel);
	}

	init {arg argSource, initLevel;
		//	use base class initialise
		this.baseInit(this);

		internalGroup = Group.new(group);

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
			["Volume", initLevel ? 0],
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
		if (channelRate == "audio", {chanColour = TXColor.sysGuiCol1}, {chanColour = TXColor.sysGuiCol2});
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
		//	meter vars
		oscResponder = nil ! 2;
		peakval = 0 ! 2;
		dbrange = dbmax - dbmin;
		if (channelRate == "audio", {
			respfunc = { arg i, x;
				if (meter[i].notNil and: {meter[i].notClosed}, {
					meter[i].lo = (x.ampdb - dbmin) / dbrange;
					if( meter[i].lo > peakval[i]) {
						peakval[i] = meter[i].lo;
						//peak[i].value = x.ampdb.round(0.1);
					};
					if(x >= 1.0) {clip[i].value = 1 };
				});
			};
		}, {
			respfunc = { arg i, x;
				if (meter[i].notNil and: {meter[i].notClosed}, {
					meter[i].value = ((x + 1) / 2).max(0).min(1);
					//peak[i].value = x.round(0.001);
				});
			};
		});
		resetfunc = { arg i;
			if (channelRate == "audio", {
				clip[i].value = 0;
				meter[i].hi = 1.0;
			});
			peakval[i] = 0.0;
			//peak[i].value = -60.0;
		};
	}

	setSourceVariables { arg argSource;
		sourceModule = argSource;
		channelRate = sourceModule.class.moduleRate;
		arrSourceBusses = sourceModule.arrOutBusChoices;
		sourceBusno = 0;
		this.setSynthArgSpec("SourceBusInd", 0);
		arrSourceOuts = arrSourceBusses.at(sourceBusno).at(1);
		sourceName = sourceModule.instDisplayName;
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
			destName = destModule.instDisplayName;
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
			// check for invalid destBusNo
			destBusNo = destBusNo ? 0;
			if (destBusNo > (arrDestBusses.size-1), {
				destBusNo = 0;
				this.setSynthArgSpec("DestBusInd", 0);
			});
			arrDestOuts = arrDestBusses.at(destBusNo).at(1);  // array of bus indices
		});
		//	set to reactivate channel
		reactivateOn = true;
		// adjust older systems
		if (system.dataBank.savedSystemRevision < 1002 and: {channelRate == "audio"}, {
			["Volume", "FXSend1Val", "FXSend2Val", "FXSend3Val", "FXSend4Val"].do({arg item, i;
				var oldVal = this.getSynthArgSpec(item);
				this.setSynthArgSpec(item, oldVal.sqrt);
			});
		});
	} // end of method loadData

	////////////////////////////////////////////////////////////////////////////////////

	*makeTitleGui{ arg argParent, argWidth = 74, argHeight = 595;
		var holdColumn, btn1, btn2, btn3, arrCols, holdInd;
		// create column for Titles
		holdColumn =  CompositeView(argParent,Rect(0, 0, argWidth, argHeight));
		holdColumn.background = TXColor.sysMainWindow;
		holdColumn.decorator = FlowLayout(holdColumn.bounds);
		holdColumn.decorator.reset;
		holdColumn.decorator.gap.x = 3;
		holdColumn.decorator.gap.y = 3;
		holdColumn.decorator.margin.x = 2;
		holdColumn.decorator.shift(-2, 0);
		[
			// ["Channels", 3],
			//["Name", 2],
			["Source:", 2],
			[" ", 2],
			["Source Bus:", 5],
			["Insert 1:", 3],
			["Insert 2:", 3],
			["Insert 3:", 3],
			["Insert 4:", 3],
			["Insert 5:", 3],
			["Destination:", 3],
			["Destination Bus:", 8],
			["FX Send 1:", 0],
			["FX Send 2:", 0],
			["FX Send 3:", 0],
			["FX Send 4:", 3],
			["Pan :", 4],
			["Level:", 0],
		]
		.do({ arg item, i;
			StaticText(holdColumn, (argWidth - 8) @ guiRowHeight)
			//.background_(TXColor.white)
			.string_(item.at(0)).align_(\center).stringColor_(TXColor.white);
			holdColumn.decorator.nextLine;
			holdColumn.decorator.shift(0, item.at(1));
		});
		holdColumn.decorator.nextLine;
		// add buttons
		arrCols = TXColor.sysGuiCol1 ! 3;
		holdInd = ["all","audio","control"].indexOfEqual(TXChannelRouting.showChannelType);
		arrCols[holdInd] = TXColor.sysGuiCol1.blend(TXColor.red, 0.03).blend(TXColor.white, 0.35);
		btn1 = Button(holdColumn, (argWidth - 4) @ guiRowHeight)
		.states_([["Show Audio & Control", TXColor.white, arrCols[0]]])
		.action_({ arg butt;
			TXChannelRouting.showChannelType = "all";
			TXChannelRouting.resetChannelsScrollerOrigin;
			// recreate view
			system.showView;
		});
		btn2 = Button(holdColumn, (argWidth - 4) @ guiRowHeight)
		.states_([["Show Audio only", TXColor.white, arrCols[1]]])
		.action_({ arg butt;
			TXChannelRouting.showChannelType = "audio";
			TXChannelRouting.resetChannelsScrollerOrigin;
			// recreate view
			system.showView;
		});
		btn3 = Button(holdColumn, (argWidth - 4) @ guiRowHeight)
		.states_([["Show Control only", TXColor.white, arrCols[2]]])
		.action_({ arg butt;
			TXChannelRouting.showChannelType = "control";
			TXChannelRouting.resetChannelsScrollerOrigin;
			// recreate view
			system.showView;
		});
		holdColumn.decorator.nextLine.shift(0, 4);
		// checkbox - showMeters
		TXCheckBox (holdColumn, (argWidth - 4) @ guiRowHeight, "Show Channel Meters",
			TXColor.sysGuiCol1, TXColor.white, TXColor.white, TXColor.sysGuiCol1)
		// get  data from synthArgSpecs
		.value_(TXChannelRouting.dataBank.showChannelMeters)
		.action_({ |view|
			if (view.value == 1, {
				TXChannelRouting.dataBank.showChannelMeters = true;
			},{
				TXChannelRouting.dataBank.showChannelMeters = false;
			});
			// recreate view
			system.showView;
		});
		holdColumn.decorator.nextLine;
		// button - wide/narrow View
		Button (holdColumn, (argWidth - 4) @ guiRowHeight)
		.states_([
			["| Narrow Channels |", TXColor.white, TXColor.sysGuiCol1],
			["|       Wide Channels       |", TXColor.white, TXColor.sysGuiCol1],
		])
		.value_(TXChannelRouting.dataBank.wideChannelMode.asInteger)
		.action_({ |view|
			if (view.value == 1, {
				TXChannelRouting.dataBank.wideChannelMode = true;
				this.adjustGuiWidth(TXChannelRouting.dataBank.wideChannelMode);
			},{
				TXChannelRouting.dataBank.wideChannelMode = false;
				this.adjustGuiWidth(TXChannelRouting.dataBank.wideChannelMode);
			});
			TXChannelRouting.setScrollToCurrentChannel;
			// recreate view
			system.showView;
		});

		^holdColumn; // return view
	}

	baseOpenGui{
		// this is a dummy method to override method in TXModuleBase.
		// the method makeChannelGui is used to build the actual gui.
	}

	*makeBlankChannelGui{ arg argParent;
		// this creates an empty channel.
		var holdColumn;
		holdColumn =  CompositeView(argParent,Rect(0,0,123,595));
		holdColumn.background = TXColor.sysChannelControl;
		// N.B. Column width is 123, max. view width is 123-8 = 115
	}

	////////////////////////////////////////////////////////////////////////////////////

	makeChannelGui{ arg argParent, argLeftIndent;
		var arrPositions, wideMode;
		var btnChannelDel, btnChannelDup, btnChannelEdit;
		var viewSource, viewSourceBus;
		var viewInsert1, viewInsert2, viewInsert3, viewInsert4, viewInsert5;
		var viewDest, viewDestBus, holdView;
		var txtChannelName, channelSourceWidth, newChanSourcePopup, newSourceModule;
		var btnInsert1Del, btnInsert2Del, btnInsert3Del, btnInsert4Del, btnInsert5Del;
		var btnDestDel, btnActivate, btnClearError, btnPanCentre;
		var arrAllPossInsertClasses, arrAllPossInsertNames;
		var arrAllDestModules, arrAllDestModNames, arrDestBusNames;
		var arrAudioSourceMods, arrAudioSourceBusses, arrAudioSourceBusNames;
		var arrControlSourceMods, arrControlSourceBusses, arrControlSourceBusNames;
		var holdCheckBoxWidth, holdSoloTxt, holdMuteTxt, holdInvertTxt, holdOffTxt;
		var holdDelBtnSpace, holdNumboxSpace, holdMeterGap, holdString, channelRateString;

		// init
		wideMode = TXChannelRouting.dataBank.wideChannelMode;
		if (wideMode, {
			holdCheckBoxWidth = 52;
			holdSoloTxt = "Solo";
			holdMuteTxt = "Mute";
			holdInvertTxt = "Invert";
			holdOffTxt = "Off";
			holdDelBtnSpace = 15;
			holdNumboxSpace = 28;
			holdMeterGap = 14;
		}, {
			holdCheckBoxWidth = 30;
			holdSoloTxt = "S";
			holdMuteTxt = "M";
			holdInvertTxt = "I";
			holdOffTxt = "O";
			holdDelBtnSpace = 2;
			holdNumboxSpace = -2;
			holdMeterGap = -2;
		});

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
		arrAudioSourceBusNames = arrAudioSourceBusses.collect({arg item, i;  item.instDisplayName; });

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
		arrControlSourceBusNames = arrControlSourceBusses.collect({arg item, i;  item.instDisplayName; });

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
		arrAllDestModNames = arrAllDestModules.collect({arg item, i;  item.instDisplayName; });

		// create column for channel
		column =  CompositeView(argParent,Rect(argLeftIndent ? 0,0, guiWidth+7, 595));
		// if channel is selected, then highlight
		if (TXChannelRouting.displayChannel == this, {
			column.background = TXColor.sysChannelHighlight.blend(chanColour, 0.25);
		},{
			if (channelRate == "audio", {
				column.background = TXColor.sysChannelAudio;
			}, {
				column.background = TXColor.sysChannelControl;
			});
		});
		column.decorator = FlowLayout(column.bounds);
		column.decorator.gap.x = 2;
		column.decorator.gap.y = 3;

		// clear arrControls
		arrControls = [];

		// popup - Channel No. & Type or Move channel
		if (channelRate == "audio", {channelRateString = " A";}, {channelRateString = " C";});
		arrPositions = [
			("Chan " ++ channelNo.asString ++ channelRateString),
			 "duplicate channel", "delete channel","delete insert module 1", "delete insert module 2",
			"delete insert module 3", "delete insert module 4", "delete insert module 5", "delete destination",
		]
		++ (1 .. arrInstances.size).collect({ arg item, i; " move to slot " ++ item.asString;});
		PopUpMenu(column, 88 @ guiRowHeight)
		.items_(arrPositions)
		.background_(TXColor.white)
		.action_({arg view;
			this.channelHighlight;
			case
			{view.value == 1} {this.duplicateChannel;}
			{view.value == 2} {this.deleteChannelRequest(view);}
			{view.value == 3} {this.deleteModuleRequest(insert1Module, view); system.showView;}
			{view.value == 4} {this.deleteModuleRequest(insert2Module, view); system.showView;}
			{view.value == 5} {this.deleteModuleRequest(insert3Module, view); system.showView;}
			{view.value == 6} {this.deleteModuleRequest(insert4Module, view); system.showView;}
			{view.value == 7} {this.deleteModuleRequest(insert5Module, view); system.showView;}
			{view.value == 8} {this.deleteDestination;}
			{view.value > 8} {
				// alter channelNo
				this.moveToPosition(view.value - 8);
				// recreate view
				system.showView;
			}
			;
		});
		// spacing
		//	column.decorator.shift(20,0);
		if (wideMode, {
			// channel duplicate button
			btnChannelDup =
			Button(column, 26 @ guiRowHeight)
			.states_([["dup", TXColor.white, chanColour]])
			.action_({
				this.duplicateChannel;
			});
			// channel delete button
			btnChannelDel =
			Button(column, 21 @ guiRowHeight)
			.states_([["del", TXColor.white, TXColor.sysDeleteCol]])
			.action_({|view|
				this.deleteChannelRequest(view);
			});
		});  // end of if wideMode
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
		sourceName = sourceModule.instDisplayName;
		viewSource = UserView(column, guiWidth@ guiRowHeight);
		viewSource.background_(chanColour);
		viewSource.drawFunc = {|view|
			var holdRect = Rect(0, 0, view.bounds.width, view.bounds.height);
			if (sourceModule.class.moduleType == "bus", {
				Pen.fillColor = Color.gray(0.65).alpha_(0.75);
				Pen.addRect(holdRect.insetBy(5));
				Pen.fill;
			});
			// text
			Pen.color = this.moduleStringColour(sourceModule);
			Pen.font = Font( "Helvetica", 12 );
			Pen.stringCenteredIn(sourceName, holdRect);
		};
		viewSource.mouseDownAction_({
			this.channelHighlight;
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
				holdView = Button(column, guiWidth-holdDelBtnSpace @ guiRowHeight);
				holdView.states = [[holdModule.instDisplayName,
					this.moduleStringColour(holdModule),
					chanColour]
				];
				holdView.action = {
					this.channelHighlight;
					this.openModuleGui(holdModule)
				};
				if (wideMode, {
					// button insert delete
					holdDelButton = Button(column, 11 @ guiRowHeight);
					holdDelButton.states = [["d", TXColor.white, TXColor.sysDeleteCol]];
					holdDelButton.action = {|view|
						this.deleteModuleRequest(holdModule, view);
						this.channelHighlight;
						// recreate view
						system.showView;
					};
				});  // end of if wideMode
			},{
				/* OLD
				holdView = PopUpMenu(column, guiWidth @ guiRowHeight)
				.stringColor_(chanColour).background_(TXColor.white);
				holdView.items = ["..."] ++ arrAllPossInsertNames;
				holdView.action = { |view|
				this.channelHighlight;
				if (view.value > 0, {
				holdModuleClass = arrAllPossInsertClasses.at(view.value - 1);
				if (holdModuleClass.notNil, {
				this.addInsertModule(holdModuleClass, i);
				});
				view.value = 0;
				});
				};
				*/
				holdView = DragSink(column, guiWidth @ guiRowHeight);
				holdView.stringColor_(chanColour).background_(TXColor.grey(0.94));
				holdView.string = "... drop " ++ channelRate ++ " insert " ++ (i + 1).asString;
				holdView.canReceiveDragHandler = { View.currentDrag.isKindOf(Event) };
				holdView.receiveDragHandler = {
					var holdEvent = View.currentDrag;
					var validClass;
					if (holdEvent.isKindOf(Event), {
						if (holdEvent.type == "audioInsert" && (channelRate == "audio"), {
							validClass = true;
						});
						if (holdEvent.type == "controlInsert" && (channelRate == "control"), {
							validClass = true;
						});
						if (validClass == true, {
							holdModuleClass = holdEvent.newModuleClass;
							if (holdModuleClass.notNil, {
								TXChannelRouting.displayChannel = this;
								this.addInsertModule(holdModuleClass, i);
							});
						});
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
			if (destModule.class.moduleType == "channel", {
				if (destModule.channelRate == "audio", {
					chanDestColour = TXColor.sysGuiCol1
				}, {
					chanDestColour = TXColor.sysGuiCol2
				});
			}, {
				if (destModule.class.moduleRate == "audio", {
					chanDestColour = TXColor.sysGuiCol1
				}, {
					chanDestColour = TXColor.sysGuiCol2;
				});
			});
			viewDest = UserView(column, guiWidth-holdDelBtnSpace @ guiRowHeight);
			viewDest.background_(chanDestColour);
			viewDest.drawFunc = {|view|
				var holdRect = Rect(0, 0, view.bounds.width, view.bounds.height);
				if (destModule.class.moduleType == "channel", {
					Pen.fillColor = Color.gray(0.7);
					4.do({arg i;
						var gap = (i + 1) * view.bounds.height / 5;
						Pen.addRect(Rect(0, gap, view.bounds.width, 1.5));
					});
					Pen.fill;
				}, {
					if (destModule.class.moduleType == "bus", {
						Pen.fillColor = Color.gray(0.65).alpha_(0.75);
						Pen.addRect(holdRect.insetBy(5));
						Pen.fill;
					});
				});
				// text
				Pen.color = this.moduleStringColour(destModule);
				Pen.font = Font( "Helvetica", 12 );
				Pen.stringCenteredIn(destModule.instDisplayName, holdRect);
			};
			viewDest.mouseDownAction_({
				this.channelHighlight;
				if (destModule.notNil, { this.openModuleGui(destModule); });
			});

			if (wideMode, {
				// button destination delete
				btnDestDel = Button(column, 11 @ guiRowHeight);
				btnDestDel.states = [["d", TXColor.white, TXColor.sysDeleteCol]];
				btnDestDel.action = {
					this.deleteDestination;
				};
			});  // end of if wideMode
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
		},{
			viewDest = ListView(column, guiWidth @ 200);
			// viewDest.stringColor_(TXColor.black).background_(TXColor.sysEditCol.blend(TXColor.white, 0.5));
			viewDest.stringColor_(TXColor.white).background_(TXColor.sysEditCol.blend(TXColor.grey(0.8), 0.4));
			viewDest.items = ["add destination"] ++ arrAllDestModNames;
			viewDest.action = { |view|
				if (view.value > 0, {  // first item ignored
					this.channelHighlight;
					this.deactivate;
					destModule = arrAllDestModules.at(view.value - 1);
					destBusNo = 0;
					this.setSynthValue("Volume", 0);
					if (channelRate ==  "audio", {
						arrDestBusses = destModule.arrAudSCInBusChoices;
					}, {
						arrDestBusses = destModule.arrCtlSCInBusChoices;
					});
					destName = destModule.instDisplayName;
					//  assign busses
					if (arrDestBusses.notNil, {
						arrDestOuts = arrDestBusses.at(destBusNo).at(1);  // array of bus indices
					});
					this.activate;
					// recreate view
					system.showView;
				});
			};
		});
		// go to next line
		column.decorator.nextLine;
		// if error display message
		if (chanError.notNil, {
			// go to next line
			column.decorator.nextLine;
			// show text
			StaticText(column, guiWidth @ 100)
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
			column.decorator.nextLine;

			if (wideMode, {
				// add volume buttons
				holdView= CompositeView(column, Rect(0, 0, 11, 150));
				11.do({ arg i;
					var blendCol;
					if ((i % 2) == 0, {
						blendCol = TXColor.white;
					},{
						blendCol = TXColor.black;
					});
					StaticText(holdView, Rect(0, 8 + (i * 12.6), 9, 7))
					.background_(chanColour.blend(blendCol, 0.1))
					.mouseDownAction_({
						this.channelHighlight;
						// store current data to synthArgSpecs
						this.setSynthArgSpec("Volume", (10-i) / 10);
						// set current value on synths
						synthChannel.set("vol", (10-i) / 10);
						// update numberview/slider
						holdVolNumberbox.value = (10-i) * 10;
						holdVolSlider.value = (10-i) / 10;
					});
				});
			});  // end of if wideMode
			// slider - volume
			arrControls = arrControls.add(
				holdVolSlider = Slider(column, 20 @ 150)
				.thumbSize_(8).knobColor_(Color.white)
				.background_(chanColour)
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
			column.decorator.shift(0,40);
			arrControls = arrControls.add(
				holdInvChkBox = TXCheckBox (column, holdCheckBoxWidth @ guiRowHeight, holdInvertTxt,
					chanColour, TXColor.white, TXColor.white, chanColour)
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
			column.decorator.shift(0 - holdCheckBoxWidth - column.decorator.gap.x,40);
			arrControls = arrControls.add(
				holdOffChkBox = TXCheckBox (column, holdCheckBoxWidth @ guiRowHeight, holdOffTxt,
					TXColor.sysDeleteCol, TXColor.white, TXColor.white, TXColor.sysDeleteCol)
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
			column.decorator.shift(0 - holdCheckBoxWidth - column.decorator.gap.x,40);
			arrControls = arrControls.add(
				holdVolNumberbox = TXScrollNumBox (column, min(holdCheckBoxWidth, 40) @ guiRowHeight, [0,100].asSpec)
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
			column.decorator.shift(holdMeterGap, -120);
			if (TXChannelRouting.dataBank.showChannelMeters == true, {
				this.makeMeterGui(column);
			});
		});	// end of if channelRate ==  "control"
		// ======================= ======================= ======================= =======================
		// if audio channel add certain controls
		if ((chanStatus == "active") and: (channelRate ==  "audio"), {
			// FX Send 1
			column.decorator.nextLine;
			// checkbox - FX Send 1 On
			arrControls = arrControls.add(
				holdFxSnd1Btn = Button(column, 10 @ guiRowHeight)
				.states_([
					[" ", chanColour, TXColor.grey7],
					["1", TXColor.white, chanColour]
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
				holdFxSnd1Sldr = Slider(column,
					(guiWidth - 10 - column.decorator.gap.x - column.decorator.margin.x - holdNumboxSpace) @ guiRowHeight)
				.background_(chanColour)
				.thumbSize_(8).knobColor_(Color.white)
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("FXSend1Val"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("FXSend1Val", view.value);
					// set current value on synth
					synthFXSend1.set("send", view.value);
					// update numberview
					if (holdFxSnd1Num.notNil, {
						holdFxSnd1Num.value = (view.value * 100).round(0.01);
					});
				});
			);
			// add screen update function
			this.createUpdFunc(holdFxSnd1Sldr,"FXSend1Val");
			if (wideMode, {
				// numbox - FX Send 1
				arrControls = arrControls.add(
					holdFxSnd1Num = TXScrollNumBox(column, 28 @ guiRowHeight, [0,1].asSpec)
					// get  data from synthArgSpecs
					.value_((this.getSynthArgSpec("FXSend1Val") * 100).round(0.01))
					.action_({ |view|
						this.channelHighlight;
						// store current data to synthArgSpecs
						this.setSynthArgSpec("FXSend1Val", view.value/100);
						// set current value on synth
						synthFXSend1.set("send", view.value/100);
						// update sliderview
						holdFxSnd1Sldr.valueAction = view.value/100;
					});
				);
				// add screen update function
				this.createUpdFunc(holdFxSnd1Num,"FXSend1Val");
			});
			// FX Send 2
			column.decorator.nextLine;
			// checkbox - FX Send 2 On
			arrControls = arrControls.add(
				holdFxSnd2Btn = Button(column, 10 @ guiRowHeight)
				.states_([
					[" ", chanColour, TXColor.grey7],
					["2 ", TXColor.white, chanColour]
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
				holdFxSnd2Sldr = Slider(column,
					(guiWidth - 10 - column.decorator.gap.x - column.decorator.margin.x - holdNumboxSpace) @ guiRowHeight)
				.background_(chanColour)
				.thumbSize_(8).knobColor_(Color.white)
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("FXSend2Val"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("FXSend2Val", view.value);
					// set current value on synth
					synthFXSend2.set("send", view.value);
					// update numberview
					if (holdFxSnd2Num.notNil, {
						holdFxSnd2Num.value = (view.value * 100).round(0.01);
					});
				});
			);
			// add screen update function
			this.createUpdFunc(holdFxSnd2Sldr,"FXSend2Val");
			if (wideMode, {
				// numbox - FX Send 2
				arrControls = arrControls.add(
					holdFxSnd2Num = TXScrollNumBox(column, 28 @ guiRowHeight, [0,1].asSpec)
					// get  data from synthArgSpecs
					.value_((this.getSynthArgSpec("FXSend2Val") * 100).round(0.01))
					.action_({ |view|
						this.channelHighlight;
						// store current data to synthArgSpecs
						this.setSynthArgSpec("FXSend2Val", view.value/100);
						// set current value on synth
						synthFXSend2.set("send", view.value/100);
						// update sliderview
						holdFxSnd2Sldr.valueAction = view.value/100;
					});
				);
				// add screen update function
				this.createUpdFunc(holdFxSnd1Num,"FXSend1Val");
			});
			// FX Send 3
			column.decorator.nextLine;
			// checkbox - FX Send 3 On
			arrControls = arrControls.add(
				holdFxSnd3Btn = Button(column, 10 @ guiRowHeight)
				.states_([
					[" ", chanColour, TXColor.grey7],
					["3 ", TXColor.white, chanColour]
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
				holdFxSnd3Sldr = Slider(column,
					(guiWidth - 10 - column.decorator.gap.x - column.decorator.margin.x - holdNumboxSpace) @ guiRowHeight)
				.background_(chanColour)
				.thumbSize_(8).knobColor_(Color.white)
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("FXSend3Val"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("FXSend3Val", view.value);
					// set current value on synth
					synthFXSend3.set("send", view.value);
					// update numberview
					if (holdFxSnd3Num.notNil, {
						holdFxSnd3Num.value = (view.value * 100).round(0.01);
					});
				});
			);
			// add screen update function
			this.createUpdFunc(holdFxSnd3Sldr,"FXSend3Val");
			if (wideMode, {
				// numbox - FX Send 3
				arrControls = arrControls.add(
					holdFxSnd3Num = TXScrollNumBox(column, 28 @ guiRowHeight, [0,1].asSpec)
					// get  data from synthArgSpecs
					.value_((this.getSynthArgSpec("FXSend3Val") * 100).round(0.01))
					.action_({ |view|
						this.channelHighlight;
						// store current data to synthArgSpecs
						this.setSynthArgSpec("FXSend3Val", view.value/100);
						// set current value on synth
						synthFXSend3.set("send", view.value/100);
						// update sliderview
						holdFxSnd3Sldr.valueAction = view.value/100;
					});
				);
				// add screen update function
				this.createUpdFunc(holdFxSnd1Num,"FXSend1Val");
			});
			// FX Send 4
			column.decorator.nextLine;
			// checkbox - FX Send 4 On
			arrControls = arrControls.add(
				holdFxSnd4Btn = Button(column, 10 @ guiRowHeight)
				.states_([
					[" ", chanColour, TXColor.grey7],
					["4", TXColor.white, chanColour]
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
				holdFxSnd4Sldr = Slider(column,
					(guiWidth - 10 - column.decorator.gap.x - column.decorator.margin.x - holdNumboxSpace) @ guiRowHeight)
				.background_(chanColour)
				.thumbSize_(8).knobColor_(Color.white)
				// get  data from synthArgSpecs
				.value_(this.getSynthArgSpec("FXSend4Val"))
				.action_({ |view|
					this.channelHighlight;
					// store current data to synthArgSpecs
					this.setSynthArgSpec("FXSend4Val", view.value);
					// set current value on synth
					synthFXSend4.set("send", view.value);
					// update numberview
					if (holdFxSnd4Num.notNil, {
						holdFxSnd4Num.value = (view.value * 100).round(0.01);
					});
				});
			);
			// add screen update function

			this.createUpdFunc(holdFxSnd4Sldr,"FXSend4Val");
			if (wideMode, {
				// numbox - FX Send 4
				arrControls = arrControls.add(
					holdFxSnd4Num = TXScrollNumBox(column, 28 @ guiRowHeight, [0,1].asSpec)
					// get  data from synthArgSpecs
					.value_((this.getSynthArgSpec("FXSend4Val") * 100).round(0.01))
					.action_({ |view|
						this.channelHighlight;
						// store current data to synthArgSpecs
						this.setSynthArgSpec("FXSend4Val", view.value/100);
						// set current value on synth
						synthFXSend4.set("send", view.value/100);
						// update sliderview
						holdFxSnd4Sldr.valueAction = view.value/100;
					});
				);
				// add screen update function
				this.createUpdFunc(holdFxSnd1Num,"FXSend1Val");
			});
			column.decorator.nextLine.shift(0, 3);

			// pan - only display if stereo channel
			if (arrDestOuts.size == 2, {
				column.decorator.nextLine;
				// button - pan centre
				btnPanCentre = Button(column, 10 @ guiRowHeight);
				btnPanCentre.states = [["=", TXColor.white, chanColour]];
				btnPanCentre.action = {
					this.channelHighlight;
					holdPanSldr.valueAction_(0.5);
				};
				// slider - pan
				arrControls = arrControls.add(
					holdPanSldr = Slider(column,
						(guiWidth - 10 - column.decorator.gap.x - column.decorator.margin.x - holdNumboxSpace) @ guiRowHeight)
					.background_(chanColour)
					.thumbSize_(8).knobColor_(Color.white)
					// get  data from synthArgSpecs
					.value_(this.getSynthArgSpec("Pan"))
					.action_({ |view|
						this.channelHighlight;
						// store current data to synthArgSpecs
						this.setSynthArgSpec("Pan", view.value);
						// set current value on synth
						synthChannel.set("pan", view.value);
						// update numberview
						if (holdPanNum.notNil, {
							holdPanNum.value = (view.value * 100).round(0.01);
						});
					});
				);
				// add screen update function
				this.createUpdFunc(holdPanSldr,"Pan");

				if (wideMode, {
					// numbox - pan
					arrControls = arrControls.add(
						holdPanNum = TXScrollNumBox(column, 28 @ guiRowHeight, [0,1].asSpec)
						// get  data from synthArgSpecs
						.value_(this.getSynthArgSpec("Pan") * 100)
						.action_({ |view|
							this.channelHighlight;
							// store current data to synthArgSpecs
							this.setSynthArgSpec("Pan", view.value * 0.01);
							// set current value on synth
							synthChannel.set("pan", view.value * 0.01);
							// update sliderview
							holdPanSldr.valueAction = view.value * 0.01;
						});
					);
					// add screen update function
					this.createUpdFunc(holdPanSldr,"Pan");
				});

			},{
				column.decorator.shift(0, guiRowHeight+4);
			});

			column.decorator.nextLine.shift(0, 3);

			if (wideMode, {
				// add volume buttons
				holdView= CompositeView(column, Rect(0, 0, 11, 150));
				11.do({ arg i;
					var blendCol;
					if ((i % 2) == 0, {
						blendCol = TXColor.white;
					},{
						blendCol = TXColor.black;
					});
					StaticText(holdView, Rect(0, 8 + (i * 12.6), 9, 7))
					.background_(chanColour.blend(blendCol, 0.1))
					.mouseDownAction_({
						var holdVal = (10-i) / 10;
						this.channelHighlight;
						// store current data to synthArgSpecs
						this.setSynthArgSpec("Volume", holdVal);
						// set current value on synths
						synthFXSend1.set("vol", holdVal);
						synthFXSend2.set("vol", holdVal);
						synthFXSend3.set("vol", holdVal);
						synthFXSend4.set("vol", holdVal);
						synthChannel.set("vol", holdVal);
						// update numberview
						holdVolNumberbox.value = (10-i) * 10;
						holdVolSlider.value = holdVal;
					});
				});
			});  // end of if wideMode
			// slider - volume
			arrControls = arrControls.add(
				holdVolSlider = Slider(column, 20 @ 150)
				.background_(chanColour)
				.thumbSize_(8).knobColor_(Color.white)
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
			column.decorator.shift(0,40);
			arrControls = arrControls.add(
				TXCheckBox (column, holdCheckBoxWidth @ guiRowHeight, holdSoloTxt,
					chanColour, TXColor.white, TXColor.white, chanColour)
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
			column.decorator.shift(0 - holdCheckBoxWidth - column.decorator.gap.x,40);
			arrControls = arrControls.add(
				holdMuteChkBox = TXCheckBox (column, holdCheckBoxWidth @ guiRowHeight, holdMuteTxt,
					TXColor.sysDeleteCol, TXColor.white, TXColor.white, TXColor.sysDeleteCol)
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
			column.decorator.shift(0 - holdCheckBoxWidth - column.decorator.gap.x,40);
			arrControls = arrControls.add(
				holdVolNumberbox = TXScrollNumBox (column, min(holdCheckBoxWidth, 40) @ guiRowHeight, [0,100].asSpec)
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
			column.decorator.shift(holdMeterGap, -120);
			if (TXChannelRouting.dataBank.showChannelMeters == true, {
				this.makeMeterGui(column);
			});
		});	// end of if channelRate ==  "audio"
	} // end of method makeChannelGui
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	makeMeterGui{arg parent;
		var meterHeight = 136;
		var size = arrDestOuts.size;
		var holdView;
		var indent;

		meter = Array.new(size);
		clip = Array.new(size);
		//peak = Array.new(size);

		if (size > 1, {
			indent = 0;
		}, {
			indent = 8;
		});
		holdView= CompositeView(parent, Rect(0, 0, indent + (size * 18), meterHeight + 15));

		size.do( { arg ix, i;
			if (channelRate == "audio", {
				clip = clip.add(Button(holdView, Rect(indent+ (i*18), 0, 15, 10)));
				clip[i].canFocus = false;
				//clip[i].font = Font("Arial",9);
				clip[i].states = [ [" ", Color.black, Color.grey(0.5)] ,
					[" ", Color.black, Color.red] ];
				clip[i].action = { arg view;
					resetfunc.(i);
				};
				meter = meter.add(RangeSlider(holdView, Rect(indent+ (i*18), 12, 15, meterHeight)));
				meter[i].knobColor = Color.grey;
				// gradient not working yet in qt
				//meter[i].background = Gradient(Color.yellow, Color(0, 0.8, 0.2), \v);
				meter[i].background = TXColor.blue.blend(Color.grey(0.65), 0.35);
				meter[i].hi = 1.0;
				meter[i].lo = 0.0;
			}, {
				meter = meter.add(Slider(holdView, Rect(indent+ (i*18), 12, 15, meterHeight)));
				meter[i].thumbSize = 8;
				meter[i].knobColor = TXColor.yellow2;
				meter[i].background = Color.grey(0.7).blend(TXColour.sysGuiCol2, 0.2);
				meter[i].acceptsMouse = false;
				UserView(meter[i], Rect( 0, (meterHeight/2)-1, 25, 2))
				.background_(Color.red.blend(Color.black, 0.15));
			});
			meter[i].canFocus = false;
			// peak = peak.add(NumberBox(holdView, Rect(indent+ (i*18), meterHeight + 45, 15, 15)));
			// peak[i].font = Font("Arial",9);
			// peak[i].value = -60.0;

			this.addResponder(i);
			resetfunc.(i);
		});
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	addInsertModule {arg holdModuleClass, insertIndex;
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
		][insertIndex].value;
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
		][insertIndex].value;
		this.reorderInsertSynths;
		this.activate;
		// update screen
		system.showView;
	}

	channelHighlight {
		if (TXChannelRouting.displayChannel.notNil and:
			{TXChannelRouting.displayChannel.column.notClosed}, {
				if (channelRate == "audio", {
					TXChannelRouting.displayChannel.column.background = TXColor.sysChannelAudio;
				}, {
					TXChannelRouting.displayChannel.column.background = TXColor.sysChannelControl;
				});
		});
		TXChannelRouting.displayChannel = this;
		column.background = TXColor.sysChannelHighlight.blend(chanColour, 0.3);
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
			if (channelRate ==  "audio", {
				// audio channels are released for fadein/out of their startenv
				[synthChannel, synthFXSend1, synthFXSend2, synthFXSend3, synthFXSend4]
				.do({arg synth;
					// release synth first, & free later
					synth.release;
					system.server.makeBundle(0.3, {
						synth.free;
					});
				});
			}, {
				// control channels freed
				synthChannel.free;
				// set dest bus to zero
				if (destModule.notNil, {
					destModule.arrCtlSCInBusses.at(destBusNo ? 0).set(0);
				});
			});
		});
	}

	reactivate {
		//	reactivate
		if (reactivateOn == true, {
			Routine.run {
				this.reorderInsertSynths;
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
				internalGroup,
				\addToTail;
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
					internalGroup,
					\addToTail;
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
					internalGroup,
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
					internalGroup,
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
					internalGroup,
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
					internalGroup,
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
					internalGroup,
					\addToTail;
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
					internalGroup,
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
					internalGroup,
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
					internalGroup,
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
					internalGroup,
					\addToTail;
				);
			});
			//	create ERROR screen until coded for more than 2 channels:
			if (arrDestOuts.size > 2, {
				TXInfoScreen.new("SYSTEM ERROR - FOR NOW TXCHANNEL CLASS CAN ONLY DEAL WITH MONO & STEREO AUDIO BUSSES");
			});
		});	//	end of if audio rate
		// make correct order of synths
		TXChannelRouting.reorderChannelSynths;
	} // end of makeSynthChannel


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	duplicateChannel {
		var newChannel;
		newChannel = TXChannelRouting.addChannel(sourceModule);
		newChannel.moveToPosition(channelNo + 1);
		TXChannelRouting.setScrollToCurrentChannel;
		// recreate view
		system.showView;
	}

	deleteChannelRequest {arg view;
		var windowPoint;
		this.channelHighlight;
		if (view.notNil, {
			windowPoint = view.mapToGlobal(view.bounds.center);
		}, {
			windowPoint = 50 @ 700;
		});
		if (system.dataBank.confirmDeletions == true, {
			TXInfoScreen.newConfirmWindow(
				{ 	this.markForDeletion;
					system.checkDeletions;  // get system to carry out deletion
					// recreate view
					system.showView;
				},
				"Are you sure you want to delete " ++ this.instDisplayName
				++ " and its Insert modules?  -  its Source module won't be deleted.",
				inLeft: windowPoint.x,
				inTop: Window.screenBounds.height - 150 - windowPoint.y
			);
		},{
			this.markForDeletion;
			system.checkDeletions;  // get system to carry out deletion
			// recreate view
			system.showView;
		});
	}

	deleteModuleRequest {arg module, view;
		var windowPoint;
		if (module.notNil, {
			if (view.notNil, {
				windowPoint = view.mapToGlobal(view.bounds.center);
			}, {
				windowPoint = 50 @ 700;
			});
			module.confirmDeleteModule(windowPoint.x,
				Window.screenBounds.height - 150 - windowPoint.y);
		});
	}

	deleteDestination {
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
	}

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
				// clear destination variables
				destModule = nil;
				arrDestBusses = nil;
				destBusNo = 0;
				arrDestOuts = nil;
				// store 0 to synthArgSpecs
				this.setSynthArgSpec("DestBusInd", 0);
			});
		});

		//	deactivate
		if (deactivateOn == true, {
			this.deactivate;
		});

		//	reactivate
		// if (reactivateOn == true and: holdChanStatus == "active", {
		// 	this.reactivate;
		// });
		if (reactivateOn == true, {
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

	sendChannelToTail {
		internalGroup.moveToTail(group);
	}

	reorderInsertSynths {
		// wait for server sync
		Routine.run {
			system.server.sync;
			// inserts
			if (insert1Module.notNil and: {insert1Module.moduleNode.notNil}, {
				insert1Module.moduleNode.moveToTail(internalGroup);
			});
			if (insert2Module.notNil and: {insert2Module.moduleNode.notNil}, {
				insert2Module.moduleNode.moveToTail(internalGroup);
			});
			if (insert3Module.notNil and: {insert3Module.moduleNode.notNil}, {
				insert3Module.moduleNode.moveToTail(internalGroup);
			});
			if (insert4Module.notNil and: {insert4Module.moduleNode.notNil}, {
				insert4Module.moduleNode.moveToTail(internalGroup);
			});
			if (insert5Module.notNil and: {insert5Module.moduleNode.notNil}, {
				insert5Module.moduleNode.moveToTail(internalGroup);
			});
		};
	}

	instSortingName {
		// adds zeros into instance no for correct sorting
		var holdZeros, newName;
		holdZeros = "0000".keep(5 - (channelNo.asString.size));
		newName = defaultName ++ " " ++ holdZeros ++ channelNo.asString;
		^newName;
	}

	addResponder { arg i;
		var commandpath, response;

		if (oscResponder[i].class == OSCFunc, {
			oscResponder[i].free;
		});
		// OLD :
		// commandpath = ['/tr', synthChannel.nodeID, i];
		// oscResponder[i] = OSCpathResponder(system.server.addr, commandpath, { arg time,responder,msg;
		// 	{ respfunc.(i, msg[3]) }.defer
		// }).add;
		commandpath = [synthChannel.nodeID, i];
		response = {arg msg; { respfunc.(i, msg[3]) }.defer; };
		oscResponder[i] = OSCFunc(response, '/tr', system.server.addr, argTemplate: commandpath);
	}

	removeAllResponders {
		2.do({arg i; oscResponder[i].free;});
	}

}
