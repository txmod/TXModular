// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMIDIRemote : TXModuleBase {


	// THIS IS VERY UNFINISHED!!
	// based on TXOSCRemote, not much work done on it






	// Message Types: \control, \noteOn, \noteOff, \bend, \touch, \polytouch, \program.

	classvar <>classData;

	var	arrMIDIResponders;
	var holdVisibleOrigin;
	var currentStepID;
	var defaultMIDITrigAction;
	var arrIgnoreMIDIStrings;
	var currentDataDict;
	var moduleRunning = true;
	var midiFunction;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "MIDI Remote";
		classData.moduleRate = "control";
		classData.moduleType = "action";
		classData.noInChannels = 0;
		classData.noOutChannels = 0;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	*syncAllModules{
		classData.arrInstances.do({ arg item, i;
			item.syncViaMIDI;
		});
	}

	init {arg argInstName;
		//	set  class specific instance variables
		currentDataDict = Dictionary.new;
		arrMIDIResponders = Order.new;
		defaultMIDITrigAction = [99,0,0,0,0,0,0, nil, \control, 0, 0, 0, 0];
		// midiTrigAction.at(0) is ModuleID
		// midiTrigAction.at(1) is Action Index
		// midiTrigAction.at(2) is value 1
		// midiTrigAction.at(3) is value 2
		// midiTrigAction.at(4) is value 3
		// midiTrigAction.at(5) is value 4
		// midiTrigAction.at(6) is stepID
		// midiTrigAction.at(7) is Action Text
		// midiTrigAction.at(8) is MIDI Type
		// midiTrigAction.at(9) is Active
		// midiTrigAction.at(10) is Triggering Type
		// midiTrigAction.at(11) is Use Args
		// midiTrigAction.at(12) is MIDI Number
		arrSynthArgSpecs = [
			["address1", "0.0.0.0"],
			["port1", 0],
			["detectMIDIMessage", 0],
			["detectMIDIPort", 0],
			["arrMIDITrigActions", [] ],
			["holdNextStepID", 1001],

		];
		guiSpecArray = [
			["EZNumber", "Remote Port", ControlSpec(0, 99999, 'lin', 1), "port1"],
			["Spacer", 30],
			["TXNetAddress","IP Address", "address1", nil, 380, 600],
			["Spacer", 20],
			["TXCheckBox", "MIDI Learn - automatically detect MIDI Port",
				"detectMIDIPort",
				{arg view; if (view.value == 1, {this.midiPortDetectActivate; system.flagGuiUpd;},
					{this.midiPortDetectDeactivate;}); }, 300],
			["Spacer", 40],
			//		["ActionButton", "reset port", {this.setSynthArgSpec("port1", 0); system.flagGuiUpd;},
			//			100, TXColor.white, TXColor.sysGuiCol2],

			["SpacerLine",2],
			["DividingLine", 1180],
			["SpacerLine",2],
			["TextBar", "Remote MIDI messages & actions:", 200],
			["Spacer", 62],
			["ActionButton", "Sync Now - send stored values for active MIDI controls",
				{this.syncViaMIDI}, 308],
			["Spacer", 62],
			["TXCheckBox", "MIDI Learn - automatically detect MIDI message",
				"detectMIDIMessage",
				{arg view; if (view.value == 1, {this.midiInputDetectActivate; system.flagGuiIfModDisplay(this);},
					{this.midiInputDetectDeactivate;}); }, 300],
			["SpacerLine",2],

			["TXMIDITrigActions",
				{this.getSynthArgSpec("arrMIDITrigActions");},
				{arg argArrMIDITrigActions;  this.setSynthArgSpec("arrMIDITrigActions", argArrMIDITrigActions);},
				{this.getNextStepID;},
				{arg view; if (holdVisibleOrigin.notNil, {{view.visibleOrigin = holdVisibleOrigin}.defer(0.05);});},
				{arg view; holdVisibleOrigin = view.visibleOrigin; },
				{currentStepID;},
				{arg stepID;  currentStepID = stepID;},
				{arg argMIDITrigAction; if (argMIDITrigAction.notNil, {this.addResponder(argMIDITrigAction);}); },
				{arg argMIDITrigAction; if (argMIDITrigAction.notNil, {this.removeResponder(argMIDITrigAction);}); },
			],
		];
		arrActionSpecs = this.buildActionSpecs([
			["TXNetAddress","IP Address", "address1", nil, 380],
			["EZNumber", "Port", ControlSpec(0, 99999, 'lin', 1), "port1"],
			["commandAction", "Sync - send stored values", {this.syncViaMIDI}],
			["TXCheckBox", "MIDI Learn - automatically detect MIDI controls",
				"detectMIDIPort",
				{arg view; if (view.value == 1, {this.midiPortDetectActivate; system.flagGuiUpd;},
					{this.midiPortDetectDeactivate;}); }, 300],
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
		this.setSynthArgSpec("detectMIDIMessage", 0);
		this.setSynthArgSpec("detectMIDIPort", 0);
		// rebuild all responders
		this.rebuildResponders;
		fork {
			// pause
			system.server.sync;
			{this.syncViaMIDI;}.defer(1);
		}
	}

	addResponder { arg argMIDITrigAction;
		var holdStepID, holdTriggerChoice, holdMIDIResponder;
		holdStepID = argMIDITrigAction[6];
		// stop any previous responders
		this.removeResponder(argMIDITrigAction);
		// create responder
		holdMIDIResponder = MIDIFunc({ arg msg, time, addr, recvPort;
			//		For testing  - post details
			//		"addResponder : ".postln;
			//		[time, responder, msg, addr].postln;
			// if module running
			if (moduleRunning == true, {
				// store current data
				this.storeCurrentData(msg, argMIDITrigAction);
				//	check triggerChoice before running actions
				holdTriggerChoice = argMIDITrigAction[10];
				if (holdTriggerChoice == 0
					or: (holdTriggerChoice == 1 and: msg[1] == 1)
					or: (holdTriggerChoice == 2 and: msg[1] == 0),
					{
						// run actions
						this.performActions(msg, argMIDITrigAction);
				});
			});
		}, argMIDITrigAction[8]);
		// store responder
		arrMIDIResponders[holdStepID] = holdMIDIResponder;
	}

	removeResponder { arg argMIDITrigAction;
		var holdStepID, holdMIDIResponder;
		holdStepID = argMIDITrigAction[6];
		holdMIDIResponder = arrMIDIResponders[holdStepID];
		if (holdMIDIResponder.notNil, {
			holdMIDIResponder.free;
			arrMIDIResponders[holdStepID] = nil;
		});
	}

	rebuildResponders {
		this.getSynthArgSpec("arrMIDITrigActions").do({arg argMIDITrigAction, i;
			// if active then rebuild
			if (argMIDITrigAction[9] == 1, {
				this.addResponder(argMIDITrigAction);
			});
		});
	}

	midiInputDetectActivate {
		var arrCurrentMIDIStrings;
		//	stop any previous action
		this.midiInputDetectDeactivate;
		midiFunction = { |msg, time, addr, recvPort|
			//			For testing  - post details
			//			"TXMIDIController stringDetect: ".postln;
			//			"time: % sender: %\nmessage: %\n".postf(time, addr, msg);

			// if string not already added create new trig action
			arrCurrentMIDIStrings  = this.getSynthArgSpec("arrMIDITrigActions").collect({arg item, i; item[8]});
			if (arrCurrentMIDIStrings.indexOfEqual(msg[0].asString).isNil, {
				this.createMIDITrigAction(msg[0].asString);
				system.flagGuiIfModDisplay(this);
			});
		};
		thisProcess.addMIDIRecvFunc(midiFunction);
	}

	midiInputDetectDeactivate {
		if (midiFunction.notNil, {
			thisProcess.removeMIDIRecvFunc(midiFunction);
		});
		this.setSynthArgSpec("detectMIDIMessage", 0);
	}

	midiPortDetectActivate {
		//	stop any previous action
		this.midiPortDetectDeactivate;
		midiFunction = { |msg, time, addr, recvPort|
			//			For testing  - post details
			//			"TXMIDIController stringDetect: ".postln;
			//			"time: % sender: %\nmessage: %\n".postf(time, addr, msg);
			//	assign vars
			this.setSynthArgSpec("address1", addr.ip);
			this.midiPortDetectDeactivate;
			this.setSynthArgSpec("detectMIDIPort", 0);
			system.flagGuiIfModDisplay(this);
		};
		thisProcess.addMIDIRecvFunc(midiFunction);
	}

	midiPortDetectDeactivate {
		if (midiFunction.notNil, {
			thisProcess.removeMIDIRecvFunc(midiFunction);
		});
		this.setSynthArgSpec("detectMIDIPort", 0);
	}

	deleteModuleExtraActions {
		arrMIDIResponders.do({arg item, i; item.remove;});
		this.midiInputDetectDeactivate;
		this.midiPortDetectDeactivate;
	}

	getNextStepID {
		var outStepID;
		outStepID = this.getSynthArgSpec("holdNextStepID");
		this.setSynthArgSpec("holdNextStepID", outStepID + 1);
		^outStepID;
	}

	createmidiTrigAction {arg oscString, holdArrMIDITrigActions;
		// use string to create new row from default row
		var holdMIDITrigAction;
		holdMIDITrigAction = defaultMIDITrigAction.deepCopy;
		holdMIDITrigAction[8] = oscString;
		holdMIDITrigAction[6] = this.getNextStepID;
		holdArrMIDITrigActions = this.getSynthArgSpec("arrMIDITrigActions");
		holdArrMIDITrigActions = holdArrMIDITrigActions.add(holdMIDITrigAction);
		// sort by MIDI type, midi number, and stepID
		holdArrMIDITrigActions.sort({ arg a, b; (a[8]++a[6]) < (b[8]++b[6]) });
		this.setSynthArgSpec("arrMIDITrigActions", holdArrMIDITrigActions);
		system.showViewIfModDisplay(this);
	}

	rebuildSynth {
		// override base class method
	}

	storeCurrentData {arg oscMsg, midiTrigAction;
		currentDataDict[midiTrigAction.at(8)] = oscMsg;
	}

	syncViaMIDI {

		//
		// OLD CODE:

		var outNetAddr, arrActions;
		// midiTrigAction.at(0) is ModuleID
		// midiTrigAction.at(1) is Action Index
		// midiTrigAction.at(2) is value 1
		// midiTrigAction.at(3) is value 2
		// midiTrigAction.at(4) is value 3
		// midiTrigAction.at(5) is value 4
		// midiTrigAction.at(6) is stepID
		// midiTrigAction.at(7) is Action Text
		// midiTrigAction.at(8) is MIDI Type
		// midiTrigAction.at(9) is Active
		// midiTrigAction.at(10) is Triggering Type
		// midiTrigAction.at(11) is Use Args
		// midiTrigAction.at(12) is MIDI Number
		// send all stored values via osc
		outNetAddr = NetAddr(this.getSynthArgSpec("address1"), this.getSynthArgSpec("port1"));
		arrActions = this.getSynthArgSpec("arrmidiTrigActions")
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

	performActions { arg oscMsg, midiTrigAction;
		var holdModuleID, holdModule, holdActionInd, holdArrActionItems, holdActionText,
		holdAction, holdVal1, holdVal2, holdVal3, holdVal4,
		actionArg1, actionArg2, actionArg3, actionArg4,
		oscArg1, oscArg2, oscArg3, oscArg4, holdIndex, holdItems, holdOffset,
		useArgs, firstArgNum;

		// midiTrigAction.at(0) is ModuleID
		// midiTrigAction.at(1) is Action Index
		// midiTrigAction.at(2) is value 1
		// midiTrigAction.at(3) is value 2
		// midiTrigAction.at(4) is value 3
		// midiTrigAction.at(5) is value 4
		// midiTrigAction.at(6) is stepID
		// midiTrigAction.at(7) is Action Text
		// midiTrigAction.at(8) is MIDI Type
		// midiTrigAction.at(9) is Active
		// midiTrigAction.at(10) is Triggering Type
		// midiTrigAction.at(11) is Use Args
		// midiTrigAction.at(12) is MIDI Number
		holdModuleID = midiTrigAction.at(0);
		holdActionInd = midiTrigAction.at(1);
		holdVal1 = midiTrigAction.at(2);
		holdVal2 = midiTrigAction.at(3);
		holdVal3 = midiTrigAction.at(4);
		holdVal4 = midiTrigAction.at(5);
		holdActionText = midiTrigAction.at(7);
		useArgs = midiTrigAction.at(11);
		firstArgNum = midiTrigAction.at(12);
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

