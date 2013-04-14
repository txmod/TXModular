// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXOSCController : TXModuleBase {

	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar	<guiWidth=500;

	var	oscControlRoutine;
	var	<>oscString;
	var	oscResponder;
	var oscFunction;

	*initClass{
		arrInstances = [];
		//	set class specific variables
		defaultName = "OSC Controller";
		moduleRate = "control";
		moduleType = "source";
		arrCtlSCInBusSpecs = [];
		noOutChannels = 1;
		arrOutBusSpecs = [
			["Out", [0]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["out", 0, 0],
			["OSCString", "/example/text", 0],
			["detectOSCString", 0],
		];
		guiSpecArray = [
			["TXCheckBox", "OSC Learn - to automatically detect the OSC string",
				"detectOSCString",
				{arg view; if (view.value == 1, {this.oscStringDetectActivate;},
					{this.oscStringDetectDeactivate;}); }, 350],
			["SpacerLine", 4],
			["OSCString"],
			["SpacerLine", 4],
			["TXStaticText", "Please note:",
				"The Network Port for receiving OSC messages is  " ++ NetAddr.langPort.asString],
		];
		arrActionSpecs = this.buildActionSpecs([
			["OSCString"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.oscControlActivate;
	}

	runAction {this.oscControlActivate}   //	override base class

	pauseAction {this.oscControlDeactivate}   //	override base class

	extraSaveData {
		^oscString;
	}
	loadExtraData {arg argData;  // override default method
		oscString = argData;
		this.oscControlActivate;
	}

	oscControlActivate {
		//	stop any previous responder
		this.oscControlDeactivate;

		oscResponder = OSCFunc({ arg msg, time, addr, recvPort;

			//	For testing  - post details
			//	"TXOSCController : ".postln;
			//	[time, responder, msg].postln;

			// set the Bus value
			if ( (outBus.class == Bus) and: (msg.at(1).isNumber), {
				outBus.value_(msg.at(1).max(-1).min(1));
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
			'/n_move', '/n_end', '/n_go', '/ping'];
		//	stop any previous action
		this.oscStringDetectDeactivate;
		oscFunction = { |msg, time, addr, recvPort|
			if (arrInvalidStrings.indexOfEqual(msg[0]).isNil) {

				// For testing  - post details
				// "TXOSCController stringDetect: ".postln;
				// "time: % sender: %\nmessage: %\n".postf(time, addr, msg);

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

}

