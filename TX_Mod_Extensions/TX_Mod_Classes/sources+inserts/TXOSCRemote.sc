// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXOSCRemote : TXModuleBase {

	classvar <>classData;

	var	arrOSCResponders;
	var 	holdVisibleOrigin;
	var 	currentStepID;
	var defaultOSCTrigAction;
	var arrIgnoreOSCStrings;
	var currentDataDict;
	var moduleRunning = true;
	var oscFunction;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "OSC Remote";
		classData.moduleRate = "control";
		classData.moduleType = "action";
		classData.noInChannels = 0;
		classData.noOutChannels = 0;
		classData.guiWidth = 1240;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	*syncAllModules{
		classData.arrInstances.do({ arg item, i;
			item.syncViaOSC;
		});
	}

	init {arg argInstName;
		//	set  class specific instance variables
		currentDataDict = Dictionary.new;
		arrOSCResponders = Order.new;
		defaultOSCTrigAction = [99,0,0,0,0,0,0, nil, "/example/text", 0, 0, 0, 0];
		// oscTrigAction.at(0) is ModuleID
		// oscTrigAction.at(1) is Action Index
		// oscTrigAction.at(2) is value 1
		// oscTrigAction.at(3) is value 2
		// oscTrigAction.at(4) is value 3
		// oscTrigAction.at(5) is value 4
		// oscTrigAction.at(6) is stepID
		// oscTrigAction.at(7) is Action Text
		// oscTrigAction.at(8) is OSC String
		// oscTrigAction.at(9) is Active
		// oscTrigAction.at(10) is Triggering Type
		// oscTrigAction.at(11) is Use Args
		// oscTrigAction.at(12) is First Arg
		arrIgnoreOSCStrings = [ '/status.reply', '/tr', '/done', '/synced', '/n_on', '/n_off',
			'/n_move', '/n_end', '/n_go', '/ping', '/b_info', '/b_setn'];
		arrSynthArgSpecs = [
			["address1", "0.0.0.0"],
			["port1", 0],
			["detectOSCString", 0],
			["detectIPAddress", 0],
			["OSCString", "/example/text", 0],
			["arrOSCTrigActions", [] ],
			["holdNextStepID", 1001],

		];
		guiSpecArray = [
			["EZNumber", "Remote Port", ControlSpec(0, 99999, 'lin', 1), "port1"],
			["Spacer", 30],
			["TXNetAddress","IP Address", "address1", nil, 380, 600],
			["Spacer", 20],
			["TXCheckBox", "OSC Learn - automatically detect IP Address",
				"detectIPAddress",
				{arg view; if (view.value == 1, {this.ipAddressDetectActivate; system.flagGuiUpd;},
					{this.ipAddressDetectDeactivate;}); }, 300],
			["Spacer", 40],
			//		["ActionButton", "reset port", {this.setSynthArgSpec("port1", 0); system.flagGuiUpd;},
			//			100, TXColor.white, TXColor.sysGuiCol2],

			["SpacerLine",2],
			["DividingLine", 1180],
			["SpacerLine",2],
			["TextBar", "Remote OSC strings & actions:", 200],
			["Spacer", 62],
			["ActionButton", "Sync Now - send stored values for active OSC strings",
				{this.syncViaOSC}, 308],
			["Spacer", 62],
			["TXCheckBox", "OSC Learn - automatically detect OSC strings",
				"detectOSCString",
				{arg view; if (view.value == 1, {this.oscStringDetectActivate; system.flagGuiIfModDisplay(this);},
					{this.oscStringDetectDeactivate;}); }, 300],
			["SpacerLine",2],

			["TXOSCTrigActions",
				{this.getSynthArgSpec("arrOSCTrigActions");},
				{arg argArrOSCTrigActions;  this.setSynthArgSpec("arrOSCTrigActions", argArrOSCTrigActions);},
				{this.getNextStepID;},
				{arg view; if (holdVisibleOrigin.notNil, {{view.visibleOrigin = holdVisibleOrigin}.defer(0.05);});},
				{arg view; holdVisibleOrigin = view.visibleOrigin; },
				{currentStepID;},
				{arg stepID;  currentStepID = stepID;},
				{arg argOSCTrigAction; if (argOSCTrigAction.notNil, {this.addResponder(argOSCTrigAction);}); },
				{arg argOSCTrigAction; if (argOSCTrigAction.notNil, {this.removeResponder(argOSCTrigAction);}); },
			],
		];
		arrActionSpecs = this.buildActionSpecs([
			["TXNetAddress","IP Address", "address1", nil, 380],
			["EZNumber", "Port", ControlSpec(0, 99999, 'lin', 1), "port1"],
			["commandAction", "Sync - send stored values", {this.syncViaOSC}],
			["TXCheckBox", "OSC Learn - automatically detect IP Address",
				"detectIPAddress",
				{arg view; if (view.value == 1, {this.ipAddressDetectActivate; system.flagGuiUpd;},
					{this.ipAddressDetectDeactivate;}); }, 300],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
	}

	runAction {moduleRunning = true;}   //	override base class

	pauseAction {moduleRunning = false;}   //	override base class

	extraSaveData {
		^[currentDataDict.getPairs];
	}

	loadExtraData {arg argData;  // override default method
		currentDataDict = Dictionary.new;
		currentDataDict.putPairs(argData[0] ? []);
		this.setSynthArgSpec("detectOSCString", 0);
		this.setSynthArgSpec("detectIPAddress", 0);
		// rebuild all responders
		this.rebuildResponders;
		fork {
			// pause
			system.server.sync;
			{this.syncViaOSC;}.defer(1);
		}
	}

	addResponder { arg argOSCTrigAction;
		var holdStepID, holdTriggerChoice, holdOSCResponder;
		holdStepID = argOSCTrigAction[6];
		// stop any previous responders
		this.removeResponder(argOSCTrigAction);
		// create responder
		holdOSCResponder = OSCFunc({ arg msg, time, addr, recvPort;
			//		For testing  - post details
			//		"addResponder : ".postln;
			//		[time, responder, msg, addr].postln;
			// if module running
			if (moduleRunning == true, {
				// store current data
				this.storeCurrentData(msg, argOSCTrigAction);
				//	check triggerChoice before running actions
				holdTriggerChoice = argOSCTrigAction[10];
				if (holdTriggerChoice == 0
					or: (holdTriggerChoice == 1 and: msg[1] == 1)
					or: (holdTriggerChoice == 2 and: msg[1] == 0),
					{
						// run actions
						this.performActions(msg, argOSCTrigAction);
				});
			});
		}, argOSCTrigAction[8]);
		// store responder
		arrOSCResponders[holdStepID] = holdOSCResponder;
	}

	removeResponder { arg argOSCTrigAction;
		var holdStepID, holdOSCResponder;
		holdStepID = argOSCTrigAction[6];
		holdOSCResponder = arrOSCResponders[holdStepID];
		if (holdOSCResponder.notNil, {
			holdOSCResponder.free;
			arrOSCResponders[holdStepID] = nil;
		});
	}

	rebuildResponders {
		this.getSynthArgSpec("arrOSCTrigActions").do({arg argOSCTrigAction, i;
			// if active then rebuild
			if (argOSCTrigAction[9] == 1, {
				this.addResponder(argOSCTrigAction);
			});
		});
	}

	oscStringDetectActivate {
		var arrCurrentOSCStrings;
		//	stop any previous action
		this.oscStringDetectDeactivate;
		oscFunction = { |msg, time, addr, recvPort|
			if (arrIgnoreOSCStrings.indexOfEqual(msg[0]).isNil) {
				//			For testing  - post details
				//			"TXOSCController stringDetect: ".postln;
				//			"time: % sender: %\nmessage: %\n".postf(time, addr, msg);

				// if string not already added create new trig action
				arrCurrentOSCStrings  = this.getSynthArgSpec("arrOSCTrigActions").collect({arg item, i; item[8]});
				if (arrCurrentOSCStrings.indexOfEqual(msg[0].asString).isNil, {
					this.createOSCTrigAction(msg[0].asString);
					system.flagGuiIfModDisplay(this);
				});
			}
		};
		thisProcess.addOSCRecvFunc(oscFunction);
	}

	oscStringDetectDeactivate {
		if (oscFunction.notNil, {
			thisProcess.removeOSCRecvFunc(oscFunction);
		});
		this.setSynthArgSpec("detectIPAddress", 0);
	}

	ipAddressDetectActivate {
		//	stop any previous action
		this.ipAddressDetectDeactivate;
		oscFunction = { |msg, time, addr, recvPort|
			if (arrIgnoreOSCStrings.indexOfEqual(msg[0]).isNil) {
				//			For testing  - post details
				//			"TXOSCController stringDetect: ".postln;
				//			"time: % sender: %\nmessage: %\n".postf(time, addr, msg);
				//	assign vars
				this.setSynthArgSpec("address1", addr.ip);
				this.ipAddressDetectDeactivate;
				this.setSynthArgSpec("detectIPAddress", 0);
				system.flagGuiIfModDisplay(this);
			}
		};
		thisProcess.addOSCRecvFunc(oscFunction);
	}

	ipAddressDetectDeactivate {
		if (oscFunction.notNil, {
			thisProcess.removeOSCRecvFunc(oscFunction);
		});
		this.setSynthArgSpec("detectOSCString", 0);
	}

	deleteModuleExtraActions {
		arrOSCResponders.do({arg item, i; item.remove;});
		this.oscStringDetectDeactivate;
		this.ipAddressDetectDeactivate;
	}

	getNextStepID {
		var outStepID;
		outStepID = this.getSynthArgSpec("holdNextStepID");
		this.setSynthArgSpec("holdNextStepID", outStepID + 1);
		^outStepID;
	}

	createOSCTrigAction {arg oscString, holdArrOSCTrigActions;
		// use string to create new row from default row
		var holdOSCTrigAction;
		holdOSCTrigAction = defaultOSCTrigAction.deepCopy;
		holdOSCTrigAction[8] = oscString;
		holdOSCTrigAction[6] = this.getNextStepID;
		holdArrOSCTrigActions = this.getSynthArgSpec("arrOSCTrigActions");
		holdArrOSCTrigActions = holdArrOSCTrigActions.add(holdOSCTrigAction);
		// sort by osc string and stepID
		holdArrOSCTrigActions.sort({ arg a, b; (a[8]++a[6]) < (b[8]++b[6]) });
		this.setSynthArgSpec("arrOSCTrigActions", holdArrOSCTrigActions);
		system.showViewIfModDisplay(this);
	}

	rebuildSynth {
		// override base class method
	}

	storeCurrentData {arg oscMsg, oscTrigAction;
		currentDataDict[oscTrigAction.at(8)] = oscMsg;
	}

	syncViaOSC {
		var outNetAddr, arrActions;
		// oscTrigAction.at(0) is ModuleID
		// oscTrigAction.at(1) is Action Index
		// oscTrigAction.at(2) is value 1
		// oscTrigAction.at(3) is value 2
		// oscTrigAction.at(4) is value 3
		// oscTrigAction.at(5) is value 4
		// oscTrigAction.at(6) is stepID
		// oscTrigAction.at(7) is Action Text
		// oscTrigAction.at(8) is OSC String
		// oscTrigAction.at(9) is Active
		// oscTrigAction.at(10) is Triggering Type
		// oscTrigAction.at(11) is Use Args
		// oscTrigAction.at(12) is First Arg
		// send all stored values via osc
		outNetAddr = NetAddr(this.getSynthArgSpec("address1"), this.getSynthArgSpec("port1"));
		arrActions = this.getSynthArgSpec("arrOSCTrigActions")
		// select active ones which Use OSC Args
		.select({ arg item, i; (item.at(9) == 1) and: (item.at(11) == 1); });
		arrActions.do({ arg item, i;
			var holdMsg, holdVal;
			var holdModuleID, holdModule, holdActionSpec;
			holdMsg = currentDataDict[item[8]].deepCopy;
			if (holdMsg.isNil, {
				holdMsg = [" ", nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil, nil];
				holdMsg[0] = item[8];
			});
			// get current value from module
			holdModuleID = item[0];
			holdModule = system.getModuleFromID(holdModuleID);
			if (holdModule == 0, {holdModule = system});
			holdActionSpec = holdModule.arrActionSpecs[item[1]];
			if (holdActionSpec.notNil, {
				// get current value
				holdVal = holdActionSpec.getValueFunction.value;
				// put value into message
				holdMsg[item[12] + 1] = holdVal;
				// send message to remote - defer to slow send rate
				{
					outNetAddr.sendMsg(* holdMsg);
				}.defer( (i div: 20) * 0.05);
				// copy message back to currentDataDict
				currentDataDict[item[8]] = holdMsg;
			});
		});
	}

	performActions { arg oscMsg, oscTrigAction;
		var holdModuleID, holdModule, holdActionInd, holdArrActionItems, holdActionText,
		holdAction, holdVal1, holdVal2, holdVal3, holdVal4,
		actionArg1, actionArg2, actionArg3, actionArg4,
		oscArg1, oscArg2, oscArg3, oscArg4, holdIndex, holdItems, holdOffset,
		useArgs, firstArgNum;

		// oscTrigAction.at(0) is ModuleID
		// oscTrigAction.at(1) is Action Index
		// oscTrigAction.at(2) is value 1
		// oscTrigAction.at(3) is value 2
		// oscTrigAction.at(4) is value 3
		// oscTrigAction.at(5) is value 4
		// oscTrigAction.at(6) is stepID
		// oscTrigAction.at(7) is Action Text
		// oscTrigAction.at(8) is OSC String
		// oscTrigAction.at(9) is Active
		// oscTrigAction.at(10) is Triggering Type
		// oscTrigAction.at(11) is Use Args
		// oscTrigAction.at(12) is First Arg
		holdModuleID = oscTrigAction.at(0);
		holdActionInd = oscTrigAction.at(1);
		holdVal1 = oscTrigAction.at(2);
		holdVal2 = oscTrigAction.at(3);
		holdVal3 = oscTrigAction.at(4);
		holdVal4 = oscTrigAction.at(5);
		holdActionText = oscTrigAction.at(7);
		useArgs = oscTrigAction.at(11);
		firstArgNum = oscTrigAction.at(12);
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
			holdOffset = firstArgNum;
			oscArg1 = oscMsg.at(1 + holdOffset);
			oscArg2 = oscMsg.at(2 + holdOffset);
			oscArg3 = oscMsg.at(3 + holdOffset);
			oscArg4 = oscMsg.at(4 + holdOffset);

			// if using OSC arguments
			if (useArgs == 1, {
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
		//	gui update
		system.flagGuiUpd;

	}	// end of performActions

}

