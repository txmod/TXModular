// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).


TXWiiTrigOSC2 : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var	oscControlRoutine;
	var	<>oscString;
	var 	arrActions;
	var	oscResponder;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Wii Trig OSC";
		classData.moduleRate = "control";
		classData.moduleType = "action";
		classData.noInChannels = 0;
		classData.noOutChannels = 0;
		classData.arrWiiNames = [
			" Select..." ,
			"Wii Button A: ON" ,
			"Wii Button A: OFF" ,
			"Wii Button B: ON" ,
			"Wii Button B: OFF" ,
			"Wii Button Up: ON" ,
			"Wii Button Up: OFF" ,
			"Wii Button Down: ON" ,
			"Wii Button Down: OFF" ,
			"Wii Button Left: ON" ,
			"Wii Button Left: OFF" ,
			"Wii Button Right: ON" ,
			"Wii Button Right: OFF" ,
			"Wii Button -: ON" ,
			"Wii Button -: OFF" ,
			"Wii Button +: ON" ,
			"Wii Button +: OFF" ,
			"Wii Button Home: ON" ,
			"Wii Button Home: OFF" ,
			"Wii Button 1: ON" ,
			"Wii Button 1: OFF" ,
			"Wii Button 2: ON" ,
			"Wii Button 2: OFF" ,
			"Nunchuk Button Z: ON" ,
			"Nunchuk Button Z: OFF" ,
			"Nunchuk Button C: ON" ,
			"Nunchuk Button C: OFF" ,
		];
		classData.arrOscStrings = [
			"dummy" ,
			"/button/A" ,
			"/button/A" ,
			"/button/B" ,
			"/button/B" ,
			"/button/Up" ,
			"/button/Up" ,
			"/button/Down" ,
			"/button/Down" ,
			"/button/Left" ,
			"/button/Left" ,
			"/button/Right" ,
			"/button/Right" ,
			"/button/Minus" ,
			"/button/Minus" ,
			"/button/Plus" ,
			"/button/Plus" ,
			"/button/Home" ,
			"/button/Home" ,
			"/button/1" ,
			"/button/1" ,
			"/button/2" ,
			"/button/2" ,
			"/nunchuk/button/Z" ,
			"/nunchuk/button/Z" ,
			"/nunchuk/button/C" ,
			"/nunchuk/button/C" ,
		];
		classData.arrOscArgValues = [0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showWiiButton";
		arrActions = [99,0,0,0,0,0,0, nil].dup(10);
		arrSynthArgSpecs = [
			["wiiButton", 0, 0],
			["wiiHandsetNo", 0, 0],
			["OSCString", "/example/text", 0],
			["useOSCArgs1", 0],
			["useOSCArgs2", 0],
			["useOSCArgs3", 0],
			["useOSCArgs4", 0],
			["useOSCArgs5", 0],
			["useOSCArgs6", 0],
			["useOSCArgs7", 0],
			["useOSCArgs8", 0],
			["useOSCArgs9", 0],
			["useOSCArgs10", 0],
		];
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TXPopupAction", "Wii no.", ["1", "2", "3", "4", "5", "6",
				"7", "8"], "wiiHandsetNo", {this.updateOscString}, 140],
			["TXPopupAction", "Wii button", classData.arrWiiNames, "wiiButton", { arg view; this.updateOscString; }],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.oscControlActivate;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Wii Button", {displayOption = "showWiiButton";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showWiiButton")],
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
		if (displayOption == "showWiiButton", {
			guiSpecArray = guiSpecArray ++[
				["TXStaticText", "Note:", "Use OSCulator with document OSCulator_WiiToTX"],
				["SpacerLine", 4],
				["TXPopupAction", "Wii no.", ["1", "2", "3", "4", "5", "6",
					"7", "8"], "wiiHandsetNo", {this.updateOscString}, 140],
				["SpacerLine", 4],
				["TXPopupAction", "Wii button", classData.arrWiiNames, "wiiButton", { arg view; this.updateOscString; }],
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

	runAction {this.oscControlActivate}   //	override base class

	pauseAction {this.oscControlDeactivate}   //	override base class

	extraSaveData {
		^[oscString, arrActions];

	}
	loadExtraData {arg argData;  // override default method
		oscString = argData.at(0);
		arrActions = argData.at(1);
		this.oscControlActivate;
	}

	updateOscString {
		var holdOscString, holdHandsetNo;
		holdHandsetNo = this.getSynthArgSpec("wiiHandsetNo") + 1;
		holdOscString = "/wii/" ++ holdHandsetNo.asString
		++ classData.arrOscStrings. at(this.getSynthArgSpec("wiiButton"));
		// set current value in module
		this.oscString = holdOscString;
		// store current data to synthArgSpecs
		this.setSynthArgSpec("OSCString", holdOscString);
		// activate osc responder
		this.oscControlActivate;
	}

	oscControlActivate {
		//	stop any previous responder
		this.oscControlDeactivate;
		oscResponder = OSCFunc({arg msg, time, addr,  recvPort;
			//	For testing  - post details
			//	"TXWiiController : ".postln;
			//	[time, msg].postln;
			// if value matches, run actions
			if (msg.at(1) == classData.arrOscArgValues.at(this.getSynthArgSpec("wiiButton")), {
				this.performActions(msg);
			});
		}, oscString.asSymbol, nil);
	}

	oscControlDeactivate {
		if (oscResponder.notNil, {
			oscResponder.free;
		});
	}

	deleteModuleExtraActions {
		this.oscControlDeactivate;
	}

	rebuildSynth {
		// override base class method
	}

	performActions { arg oscMsg;
		arrActions.do({ arg item, i;
			var holdModuleID, holdModule, holdActionInd, holdArrActionItems, holdActionText,
			holdAction, holdVal1, holdVal2, holdVal3, holdVal4, actionArg1, actionArg2, actionArg3, actionArg4,
			oscArg1, oscArg2, oscArg3, oscArg4, holdIndex, holdItems;
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
				oscArg1 = oscMsg.at(1);
				oscArg2 = oscMsg.at(2);
				oscArg3 = oscMsg.at(3);
				oscArg4 = oscMsg.at(4);

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

