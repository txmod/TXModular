// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXAudioTrigger3 : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var	oscResponder;
	var  arrActions;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Audio Trigger";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrAudSCInBusSpecs = [
			["Audio in", 1, "audioIn"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Threshold", 1, "modThreshold", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Level Out", [0]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var holdControlSpec;
		//	set  class specific instance variables
		displayOption = "showTrigger";
		arrActions = [99,0,0,0,0,0,0, nil].dup(10);
		arrSynthArgSpecs = [
			["out", 0, 0],
			["audioIn", 0, 0],
			["threshold", 0.05, defLagTime],
			["trigHoldTime", 0.1, defLagTime],
			["modThreshold", 0, defLagTime],
		];

		synthDefFunc = {
			arg out, audioIn, threshold, trigHoldTime, modThreshold=0;
			var threshlevel, holdTrig, holdAmp;
			threshlevel = (threshold + modThreshold).max(0.001).min(1);
			holdAmp =  LagUD.kr(Amplitude.kr(InFeedback.ar(audioIn,1), 0.05, 0.05), trigHoldTime, trigHoldTime);
			holdTrig = Trig.kr(holdAmp - threshlevel, trigHoldTime);
			SendTrig.kr(holdTrig, 0, 1);
			Out.kr(out, holdAmp);
		};
		holdControlSpec = ControlSpec(0.01, 10, \lin, 0, 1, units: " secs");
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["EZslider", "Trig Threshold", ControlSpec(0.001, 1), "threshold"],
			["EZslider", "Reload time", ControlSpec(0.01, 1), "trigHoldTime"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
		this.oscControlActivate;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Trigger", {displayOption = "showTrigger";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showTrigger")],
			["Spacer", 3],
			["ActionButton", "Actions 1-5", {displayOption = "showActions1-5";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showActions1-5")],
			["Spacer", 3],
			["ActionButton", "Actions 6-10", {displayOption = "showActions6-10";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showActions6-10")],
			["Spacer", 3],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showTrigger", {
			guiSpecArray = guiSpecArray ++[
				["EZslider", "Trig Threshold", ControlSpec(0.001, 1), "threshold"],
				["SpacerLine", 2],
				["EZslider", "Reload time", ControlSpec(0.01, 1), "trigHoldTime"],
			];
		});
		if (displayOption == "showActions1-5", {
			guiSpecArray = guiSpecArray ++[
				["TXActionView", arrActions, 0],
				["DividingLine"],
				["TXActionView", arrActions, 1],
				["DividingLine"],
				["TXActionView", arrActions, 2],
				["DividingLine"],
				["TXActionView", arrActions, 3],
				["DividingLine"],
				["TXActionView", arrActions, 4],
				["DividingLine"],
			];
		});
		if (displayOption == "showActions6-10", {
			guiSpecArray = guiSpecArray ++[
				["TXActionView", arrActions, 5],
				["DividingLine"],
				["TXActionView", arrActions, 6],
				["DividingLine"],
				["TXActionView", arrActions, 7],
				["DividingLine"],
				["TXActionView", arrActions, 8],
				["DividingLine"],
				["TXActionView", arrActions, 9],
				["DividingLine"],
			];
		});
	}

	getButtonColour { arg colour2Boolean;
		if (colour2Boolean == true, {
			^TXColor.sysGuiCol4;
		},{
			^TXColor.sysGuiCol1;
		});
	}

	extraSaveData {
		^[arrActions];
	}

	loadExtraData {arg argData;  // override default method
		arrActions = argData.at(0);
		this.oscControlActivate;
	}

	oscControlActivate {
		//	stop any previous responder
		this.oscControlDeactivate;
		oscResponder = OSCFunc({arg msg, time, addr,  recvPort;
			var holdNodeID;
			//		For testing  - post details
			//		"TXAudioTrigger : ".postln;
			//		[time, msg].postln;
			holdNodeID = msg.at(1);
			if (moduleNode.notNil and: {holdNodeID == moduleNode.nodeID}, {
				// run actions
				this.performActions;
			});
		}, '/tr', system.server.addr);
	}

	oscControlDeactivate {
		if (oscResponder.notNil, {
			oscResponder.free;
		});
	}

	deleteModuleExtraActions {
		this.oscControlDeactivate;
	}

	performActions {
		arrActions.do({ arg item, i;
			var holdModuleID, holdModule, holdActionInd, holdArrActionItems, holdActionText,
			holdAction, holdVal1, holdVal2, holdVal3, holdVal4, actionArg1, actionArg2, actionArg3, actionArg4,
			holdIndex, holdItems;
			holdModuleID = item.at(0);
			holdActionInd = item.at(1);
			holdVal1 = item.at(2);
			holdVal2 = item.at(3);
			holdVal3 = item.at(4);
			holdVal4 = item.at(5);
			holdActionText = item.at(7);
			holdModule = system.getModuleFromID(holdModuleID);
			if (holdModule != 0, {
				holdArrActionItems = holdModule.arrActionSpecs.collect({arg item, i; item.actionName;});
				// if text found, match action string with text, else use numerical value
				if (holdActionText.notNil, {
					holdActionInd = holdArrActionItems.indexOfEqual(holdActionText) ? holdActionInd;
					holdAction = holdModule.arrActionSpecs.at(holdActionInd);
				},{
					// if text not found, use number but only select older actions with legacyType == 1
					holdAction = holdModule.arrActionSpecs.select({arg item, i; item.legacyType == 1}).at(holdActionInd);
				});

				actionArg1 = holdVal1;
				actionArg2 = holdVal2;
				actionArg3 = holdVal3;
				actionArg4 = holdVal4;

				// if action type is commandAction then value it with arguments
				if (holdAction.actionType == \commandAction, {
					holdAction.actionFunction.value(actionArg1, actionArg2, actionArg3, actionArg4);
				});
				// if action type is valueAction then value it with arguments
				if (holdAction.actionType == \valueAction, {
					holdAction.setValueFunction.value(actionArg1, actionArg2, actionArg3, actionArg4);
				});
			});
		});
		//	gui update
		//	system.flagGuiUpd;
	}	// end of performActions

}

