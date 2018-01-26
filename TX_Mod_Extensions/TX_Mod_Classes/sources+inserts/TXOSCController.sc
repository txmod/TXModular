// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXOSCController : TXModuleBase {

	classvar <>classData;

	var	oscControlRoutine;
	var	<>oscString;
	var	oscResponder;
	var oscFunction;
	var data;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "OSC Controller";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		data = ();
		data.minVal = -1;
		data.maxVal = 1;
		data.outputMin = -1;
		data.outputMax = 1;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["out", 0, 0],
			["OSCString", "/example/text", 0],
			["detectOSCString", 0],
			["minVal", -1],
			["maxVal", 1],
			["outputMin", -1],
			["outputMax", 1],
		];
		guiSpecArray = [
			["TXStaticText", "Please note:",
				"The Network Port for receiving OSC messages is  " ++ NetAddr.langPort.asString],
			["SpacerLine", 20],
			["TXCheckBox", "OSC Learn - to automatically detect the OSC string",
				"detectOSCString",
				{arg view; if (view.value == 1, {this.oscStringDetectActivate;},
					{this.oscStringDetectDeactivate;}); }, 350],
			["SpacerLine", 4],
			["OSCString"],
			["SpacerLine", 20],
			["EZNumber", "Min value", ControlSpec(-10000000, 10000000, 'lin'), "minVal",
				{arg view; data.minVal = view.value;}, 80, 80],
			["Spacer", 10],
			["EZNumber", "Max value", ControlSpec(-10000000, 10000000, 'lin'), "maxVal",
				{arg view; data.maxVal = view.value;}, 80, 80],
			["Spacer", 10],
			["ActionPopup", ["Presets:", "-1 : 1", "0 : 1", "0 : 127", "0 : 255"],
				{arg holdView; this.valueRangePresetLoad(holdView.value);}, 80],
			["SpacerLine", 20],
			["TXRangeSlider", "Output range", ControlSpec(-1, 1), "outputMin", "outputMax",
				{this.updateMinMaxVal},
				[["Presets:", []], ["0 : 1", [0,1]], ["-1 : 1", [-1,1]], ["-0.5 : 0.5", [-0.5,0.5]], ["-1 : 0", [-1,0]]],
			],
		];
		arrActionSpecs = this.buildActionSpecs([
			["OSCString"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.oscControlActivate;
	}

	valueRangePresetLoad {arg index;
		var arrVals;
		if (index > 0, {
			arrVals = [[], [-1, 1], [0, 1], [0, 127], [0, 255]][index];
			data.minVal = arrVals[0];
			data.maxVal = arrVals[1];
			this.setSynthArgSpec( "minVal", arrVals[0]);
			this.setSynthArgSpec( "maxVal", arrVals[1]);
			system.flagGuiUpd;
		});
	}

	updateMinMaxVal {
		var min = this.getSynthArgSpec( "outputMin");
		var max = this.getSynthArgSpec( "outputMax");
		data.outputMin = min;
		data.outputMax = max;
	}

	oscControlActivate {
		//	stop any previous responder
		this.oscControlDeactivate;

		oscResponder = OSCFunc({ arg msg, time, addr, recvPort;
			var outVal;

			//	For testing  - post details
			//	"TXOSCController : ".postln;
			//	[time, responder, msg].postln;

			// set the Bus value
			if ( (outBus.class == Bus) and: (msg.at(1).isNumber), {
				outVal = msg.at(1).linlin(data.minVal, data.maxVal, data.outputMin, data.outputMax);
				outBus.set(outVal);
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

	// base class overrides

	rebuildSynth { }	// override base class method

	runAction {this.oscControlActivate}   //	override base class

	pauseAction {this.oscControlDeactivate}   //	override base class

	extraSaveData {
		^oscString;
	}
	loadExtraData {arg argData;  // override default method
		oscString = argData;
		this.oscControlActivate;
	}

	deleteModuleExtraActions {
		this.oscControlDeactivate;
		this.oscStringDetectDeactivate;
	}

}

