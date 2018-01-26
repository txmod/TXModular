// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXWiiControllerOSC3 : TXModuleBase {

	classvar <>classData;

	var	oscControlRoutine;
	var	<>oscString;
	var	oscResponder;
	var testTime = 0, testMin=10000, testMax = -10000; // for testing

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Wii Ctrl OSC";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrWiiNames = [
			" Select..." ,
			" Wii angle pitch - tilt forward-back" ,
			" Wii angle roll - tilt left-right" ,
			" Wii angle yaw - spin clockwise-anti-clockwise" ,
			" Wii velocity pitch - tilt forward-back" ,
			" Wii velocity roll - tilt left-right" ,
			" Wii velocity yaw - spin clockwise-anti-clockwise" ,
			" Wii acceleration pitch - tilt forward-back" ,
			" Wii acceleration roll - tilt left-right" ,
			" Wii acceleration yaw - spin clockwise-anti-clockwise" ,
			" Wii acceleration total" ,
			" Wii ir X" ,
			" Wii ir Y" ,
			" Wii button A" ,
			" Wii button B" ,
			" Wii button Up" ,
			" Wii button Down" ,
			" Wii button Left" ,
			" Wii button Right" ,
			" Wii button Minus" ,
			" Wii button Plus" ,
			" Wii button Home" ,
			" Wii button 1" ,
			" Wii button 2" ,
			" Nunchuk pitch - tilt forward-back" ,
			" Nunchuk roll - tilt left-right" ,
			" Nunchuk yaw - rotate clockwise-anti-clockwise" ,
			" Nunchuk acceleration" ,
			" Nunchuk joystick X" ,
			" Nunchuk joystick Y" ,
			" Nunchuk button Z" ,
			" Nunchuk button C" ,
		];
		classData.arrOscStrings = [
			"dummy" ,
			"/motion/angles" ,
			"/motion/angles" ,
			"/motion/angles" ,
			"/motion/velo" ,
			"/motion/velo" ,
			"/motion/velo" ,
			"/accel/pry" ,
			"/accel/pry" ,
			"/accel/pry" ,
			"/accel/pry" ,
			"/ir" ,
			"/ir" ,
			"/button/A" ,
			"/button/B" ,
			"/button/Up" ,
			"/button/Down" ,
			"/button/Left" ,
			"/button/Right" ,
			"/button/Minus" ,
			"/button/Plus" ,
			"/button/Home" ,
			"/button/1" ,
			"/button/2" ,
			"/nunchuk/accel/pry" ,
			"/nunchuk/accel/pry" ,
			"/nunchuk/accel/pry" ,
			"/nunchuk/accel/pry" ,
			"/nunchuk/joy" ,
			"/nunchuk/joy" ,
			"/nunchuk/button/Z" ,
			"/nunchuk/button/C" ,
		];
		classData.arrOscArgOffsets = [0, 0,1,2, 0,1,2, 0,1,2,3, 0,1, 0,  0,  0,  0,  0,  0,  0,  0,  0,
			0,  0, 0,1,2,3, 0,1,  0,  0];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["out", 0, 0],
			["wiiHandsetNo", 0, 0],
			["wiiControl", 0, 0],
			["oscString", "/example/text", 0],
		];
		guiSpecArray = [
			//		["TXStaticText", "Note:", "Use OSCulator with document OSCulator_WiiToTX"],
			//		["SpacerLine", 4],
			["TXPopupAction", "Wii no.", ["1", "2", "3", "4", "5", "6",
				"7", "8"], "wiiHandsetNo", {this.updateOscString}, 140],
			["SpacerLine", 4],
			["TXPopupAction", "Wii control", classData.arrWiiNames, "wiiControl",
				{ arg view; this.updateOscString; }],
		];
		arrActionSpecs = this.buildActionSpecs([
			["TXPopupAction", "Wii no.", ["1", "2#", "3", "4#", "5", "6",
				"7#", "8"], "wiiHandsetNo", {this.updateOscString}, 140],
			["TXPopupAction", "Wii control", classData.arrWiiNames, "wiiControl",
				{ arg view; this.updateOscString; }],
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

	updateOscString {
		var holdOscString;
		holdOscString = "/wii/" ++ (this.getSynthArgSpec("wiiHandsetNo") + 1).asString
		++ classData.arrOscStrings.at(this.getSynthArgSpec("wiiControl"));
		// set current value in module
		this.oscString = holdOscString;
		// store current data to synthArgSpecs
		this.setSynthArgSpec("oscString", holdOscString);
		// activate osc responder
		this.oscControlActivate;
	}

	oscControlActivate {
		var indexOffset, holdControlVal, holdHandsetNo;
		indexOffset = classData.arrOscArgOffsets.at(this.getSynthArgSpec("wiiControl") ? 0);
		//	stop any previous responder
		this.oscControlDeactivate;
		oscResponder = OSCFunc({arg msg, time, addr,  recvPort;
			//	For testing  - post details
			//	//	if (msg.at(0).asString.keep("/wii/irdata".size) == "/wii/irdata", {
			//		//	[time, msg].round(0.01).postln;
			//			if (msg.at(1) > testMax, {
			//				testMax = msg.at(1);
			//				(msg.at(0) ++ "    Max: " ++ testMax.round(0.01).asString).postln;
			//			});
			//			if (msg.at(1) < testMin, {
			//				testMin = msg.at(1);
			//				(msg.at(0) ++ "    Min: " ++ testMin.round(0.01).asString).postln;
			//			});
			//	//		if (testTime < time.round(0.25), {
			//	//			"TXWiiController : ".postln;
			//	//			[time.round, responder, msg.round(0.01)].postln;
			//	//			testTime = time.round(0.25);
			//	//		});
			//	//
			//	//	});
			holdControlVal = msg.at(1+indexOffset);
			//  if ir and value X or Y is 1, ignore
			holdHandsetNo = this.getSynthArgSpec("wiiHandsetNo") + 1;
			if (((msg.at(0).asString.keep("/wii/1/ir".size) == ("/wii/" ++ holdHandsetNo.asString ++ "/ir"))
				and: (holdControlVal == 1)
			).not, {
				// set the Bus value within range -1 / +1
				if ( (outBus.class == Bus) and: (holdControlVal.isNumber), {
					outBus.value_(holdControlVal.max(-1).min(1));
				});
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

}

