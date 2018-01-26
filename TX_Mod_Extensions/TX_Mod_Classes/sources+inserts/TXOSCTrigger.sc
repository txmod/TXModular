// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXOSCTrigger : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var	oscControlRoutine;
	var	<>oscString;
	var  <arrActions;
	var	oscResponder;
	var	oscStringDetectResponder;
	var oscFunction;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "OSC Trigger";
		classData.moduleRate = "control";
		classData.moduleType = "action";
		classData.noInChannels = 0;
		classData.noOutChannels = 0;
		classData.arrUseOSCArgNames = ["useOSCArgs1", "useOSCArgs2", "useOSCArgs3",
			"useOSCArgs4", "useOSCArgs5", "useOSCArgs6",
			"useOSCArgs7", "useOSCArgs8", "useOSCArgs9", "useOSCArgs10"];
		classData.arrOffsetOSCArgNames = ["offsetOSCArgs1", "offsetOSCArgs2", "offsetOSCArgs3",
			"offsetOSCArgs4", "offsetOSCArgs5", "offsetOSCArgs6",
			"offsetOSCArgs7", "offsetOSCArgs8", "offsetOSCArgs9", "offsetOSCArgs10"];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showOSCString";
		arrActions = [99,0,0,0,0,0,0, nil].dup(10);
		arrSynthArgSpecs = [
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
			["triggerChoice", 0],
			["detectOSCString", 0],
			["offsetOSCArgs1", 0],
			["offsetOSCArgs2", 0],
			["offsetOSCArgs3", 0],
			["offsetOSCArgs4", 0],
			["offsetOSCArgs5", 0],
			["offsetOSCArgs6", 0],
			["offsetOSCArgs7", 0],
			["offsetOSCArgs8", 0],
			["offsetOSCArgs9", 0],
			["offsetOSCArgs10", 0],
		];
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["OSCString"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.oscControlActivate;
	}

	buildGuiSpecArray {
		var holdControlSpec, holdPopupItems;
		holdControlSpec = ControlSpec(1,16, step: 0 );
		holdPopupItems = 16.collect({ arg item, i; (item+1).asString; });
		guiSpecArray = [
			["ActionButton", "OSC String", {displayOption = "showOSCString";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showOSCString")],
			["Spacer", 3],
			["ActionButton", "Actions 1-2", {displayOption = "showActions1-2";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showActions1-2")],
			["Spacer", 3],
			["ActionButton", "Actions 3-4", {displayOption = "showActions3-4";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showActions3-4")],
			["Spacer", 3],
			["ActionButton", "Actions 5-6", {displayOption = "showActions5-6";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showActions5-6")],
			["Spacer", 3],
			["ActionButton", "Actions 7-8", {displayOption = "showActions7-8";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showActions7-8")],
			["Spacer", 3],
			["ActionButton", "Actions 9-10", {displayOption = "showActions9-10";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showActions9-10")],
			["Spacer", 3],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showOSCString", {
			guiSpecArray = guiSpecArray ++[
				["TXCheckBox", "OSC Learn - to automatically detect the OSC string",
					"detectOSCString",
					{arg view; if (view.value == 1, {this.oscStringDetectActivate;},
						{this.oscStringDetectDeactivate;}); }, 350],
				["SpacerLine", 4],
				["OSCString"],
				["SpacerLine", 4],
				["TXStaticText", "Please note:",
					"The Network Port for receiving OSC messages is  " ++ NetAddr.langPort.asString],
				["SpacerLine", 4],
				[	"TXPopupActionPlusMinus", "Triggering",
					["Always trigger", "Trigger when next OSC argument is 1",
						"Trigger when next OSC argument is 0"
					],
					"triggerChoice"]
			];
		});
		if (displayOption == "showActions1-2", {
			guiSpecArray = guiSpecArray ++[
				["TXActionView", arrActions, 0],
				["NextLine"],
				["TXCheckBox", "Use OSC arguments for value settings", "useOSCArgs1", nil, 300],
				["TXPopupAction", "First argument", holdPopupItems, "offsetOSCArgs1", nil, 140],
				["DividingLine"],
				["SpacerLine", 4],
				["TXActionView", arrActions, 1],
				["NextLine"],
				["TXCheckBox", "Use OSC arguments for value settings", "useOSCArgs2", nil, 300],
				["TXPopupAction", "First argument", holdPopupItems, "offsetOSCArgs2", nil, 140],
				["DividingLine"],
			];
		});
		if (displayOption == "showActions3-4", {
			guiSpecArray = guiSpecArray ++[
				["TXActionView", arrActions, 2],
				["NextLine"],
				["TXCheckBox", "Use OSC arguments for value settings", "useOSCArgs3", nil, 300],
				["TXPopupAction", "First argument", holdPopupItems, "offsetOSCArgs3", nil, 140],
				["DividingLine"],
				["SpacerLine", 4],
				["TXActionView", arrActions, 3],
				["NextLine"],
				["TXCheckBox", "Use OSC arguments for value settings", "useOSCArgs4", nil, 300],
				["TXPopupAction", "First argument", holdPopupItems, "offsetOSCArgs4", nil, 140],
				["DividingLine"],
			];
		});
		if (displayOption == "showActions5-6", {
			guiSpecArray = guiSpecArray ++[
				["TXActionView", arrActions, 4],
				["NextLine"],
				["TXCheckBox", "Use OSC arguments for value settings", "useOSCArgs5", nil, 300],
				["TXPopupAction", "First argument", holdPopupItems, "offsetOSCArgs5", nil, 140],
				["DividingLine"],
				["SpacerLine", 4],
				["TXActionView", arrActions, 5],
				["NextLine"],
				["TXCheckBox", "Use OSC arguments for value settings", "useOSCArgs6", nil, 300],
				["TXPopupAction", "First argument", holdPopupItems, "offsetOSCArgs6", nil, 140],
				["DividingLine"],
			];
		});
		if (displayOption == "showActions7-8", {
			guiSpecArray = guiSpecArray ++[
				["TXActionView", arrActions, 6],
				["NextLine"],
				["TXCheckBox", "Use OSC arguments for value settings", "useOSCArgs7", nil, 300],
				["TXPopupAction", "First argument", holdPopupItems, "offsetOSCArgs7", nil, 140],
				["DividingLine"],
				["SpacerLine", 4],
				["TXActionView", arrActions, 7],
				["NextLine"],
				["TXCheckBox", "Use OSC arguments for value settings", "useOSCArgs8", nil, 300],
				["TXPopupAction", "First argument", holdPopupItems, "offsetOSCArgs8", nil, 140],
				["DividingLine"],
			];
		});
		if (displayOption == "showActions9-10", {
			guiSpecArray = guiSpecArray ++[
				["TXActionView", arrActions, 8],
				["NextLine"],
				["TXCheckBox", "Use OSC arguments for value settings", "useOSCArgs9", nil, 300],
				["TXPopupAction", "First argument", holdPopupItems, "offsetOSCArgs9", nil, 140],
				["DividingLine"],
				["SpacerLine", 4],
				["TXActionView", arrActions, 9],
				["NextLine"],
				["TXCheckBox", "Use OSC arguments for value settings", "useOSCArgs10", nil, 300],
				["TXPopupAction", "First argument", holdPopupItems, "offsetOSCArgs10", nil, 140],
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
		this.setSynthArgSpec("detectOSCString", 0);
	}

	oscControlActivate {
		var holdTriggerChoice;
		//	stop any previous responder
		this.oscControlDeactivate;

		oscResponder = OSCFunc({ arg msg, time, addr, recvPort;

			//	For testing  - post details
			//	"TXOSCController : ".postln;
			//	[time, responder, msg].postln;

			//	check triggerChoice before running actions
			holdTriggerChoice = this.getSynthArgSpec("triggerChoice");
			if (holdTriggerChoice == 0
				or: (holdTriggerChoice == 1 and: msg[1] == 1)
				or: (holdTriggerChoice == 2 and: msg[1] == 0),
				{
					// run actions
					this.performActions(msg);
			});
		}, oscString.asSymbol);
	}

	oscControlDeactivate {
		if (oscResponder.notNil, {
			oscResponder.free;
		});
	}

	oscStringDetectActivate {
		var arrInvalidStrings;
		arrInvalidStrings = [ '/status.reply', '/tr', '/done', '/synced', '/n_on', '/n_off',
			'/n_move', '/n_end', '/n_go', '/ping', '/b_info', '/b_setn'];
		//	stop any previous action
		this.oscStringDetectDeactivate;
		oscFunction = { |msg, time, addr, recvPort|
			if (arrInvalidStrings.indexOfEqual(msg[0]).isNil) {

				//			For testing  - post details
				//			"TXOSCController stringDetect: ".postln;
				//			"time: % sender: %\nmessage: %\n".postf(time, addr, msg);

				//	assign string
				this.setSynthArgSpec("OSCString", msg[0].asString);
				oscString = msg[0].asString;
				this.oscControlActivate;
				this.oscStringDetectDeactivate;
				this.setSynthArgSpec("detectOSCString", 0);
				system.flagGuiIfModDisplay(this);
			}
		};
		thisProcess.addOSCRecvFunc(oscFunction);
	}

	oscStringDetectDeactivate {
		if (oscFunction.notNil, {
			thisProcess.removeOSCRecvFunc(oscFunction);
		});
	}

	deleteModuleExtraActions {
		this.oscControlDeactivate;
		this.oscStringDetectDeactivate;
	}

	rebuildSynth {
		// override base class method
	}

	performActions { arg oscMsg;
		arrActions.do({ arg item, i;
			var holdModuleID, holdModule, holdActionInd, holdArrActionItems, holdActionText,
			holdAction, holdVal1, holdVal2, holdVal3, holdVal4,
			actionArg1, actionArg2, actionArg3, actionArg4,
			oscArg1, oscArg2, oscArg3, oscArg4, holdIndex, holdItems, holdOffset;
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
					holdAction = holdModule.arrActionSpecs
					.select({arg item, i; item.legacyType == 1}).at(holdActionInd);
				});

				actionArg1 = holdVal1;
				actionArg2 = holdVal2;
				actionArg3 = holdVal3;
				actionArg4 = holdVal4;
				holdOffset = this.getSynthArgSpec(classData.arrOffsetOSCArgNames.at(i)).asInteger;
				oscArg1 = oscMsg.at(1 + holdOffset);
				oscArg2 = oscMsg.at(2 + holdOffset);
				oscArg3 = oscMsg.at(3 + holdOffset);
				oscArg4 = oscMsg.at(4 + holdOffset);

				// if using OSC arguments
				if (this.getSynthArgSpec(classData.arrUseOSCArgNames.at(i)) == 1, {
					// if object type is number
					if (holdAction.guiObjectType == \number, {
						if (oscArg1.isNumber, {
							actionArg1 = (holdAction.arrControlSpecFuncs.at(0).value ?? ControlSpec(0,1))
							.constrain(oscArg1);
						});
						if (oscArg2.isNumber, {
							actionArg2 = (holdAction.arrControlSpecFuncs.at(1).value ?? ControlSpec(0,1))
							.constrain(oscArg2);
						});
						if (oscArg3.isNumber, {
							actionArg3 = (holdAction.arrControlSpecFuncs.at(2).value ?? ControlSpec(0,1))
							.constrain(oscArg3);
						});
						if (oscArg4.isNumber, {
							actionArg4 = (holdAction.arrControlSpecFuncs.at(3).value ?? ControlSpec(0,1))
							.constrain(oscArg4);
						});
					});
					// if object type is number
					holdItems = holdAction.getItemsFunction.value;
					if (holdAction.guiObjectType == \popup, {
						if (oscArg1.isNumber, {
							actionArg1 = ControlSpec(0, holdItems.size).constrain(oscArg1);
						});
						if (oscArg1.isSymbolWS, {
							holdIndex = holdItems.indexOfEqual(oscArg1.asString);
							if (holdIndex.notNil, {
								actionArg1 = holdIndex;
							});
						});
					});
					// if object type is checkbox
					if (holdAction.guiObjectType == \checkbox, {
						if (oscArg1.isNumber, {
							actionArg1 = ControlSpec(0, 1, step: 1).constrain(oscArg1);
						});
						if (oscArg1.isSymbolWS, {
							holdIndex = ["ON", "On", "on"].indexOfEqual(oscArg1.asString);
							if (holdIndex.notNil, {
								actionArg1 = 1;
							},{
								holdIndex = ["OFF", "Off", "off"].indexOfEqual(oscArg1.asString);
								if (holdIndex.notNil, {
									actionArg1 = 0;
								});
							});
						});
					});
					// if object type is textedit
					if (holdAction.guiObjectType == \textedit, {
						if (oscArg1.isSymbolWS, {
							actionArg1 = oscArg1.asString;
						});
					});
				});
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

