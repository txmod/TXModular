// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXOSCControlOut : TXModuleBase {

	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar <guiWidth=500;

	var	oscControlResp;
	var	sendTrigID;
	// var	holdPortNames;
	// var	holdMIDIOutPort, holdMIDIDeviceName, holdMIDIPortName;
	var	displayOption;
	var	arrNetAddresses;

*initClass{
	arrInstances = [];
	//	set class specific variables
	defaultName = "OSC Control Out";
	moduleRate = "control";
	moduleType = "action";
	noInChannels = 0;
	arrCtlSCInBusSpecs = [
		["Controller val", 1, "modControlVal", 0],
	];
	noOutChannels = 0;
	arrOutBusSpecs = [];
}

*new{ arg argInstName;
	 ^super.new.init(argInstName);
}

*addressesCopyAll { arg argData;
	arrInstances.do({arg module, i;
		module.loadArrAddressData(argData);
	});
}

init {arg argInstName;

	//	set  class specific instance variables
	displayOption = "showMessage";
	// create unique id
	sendTrigID = UniqueID.next;
	arrSynthArgSpecs = [
		["out", 0, 0],
		["on", 0, 0],
		["controlValue", 0, 0],
		["controlValMin", 0, 0],
		["controlValMax", 1, 0],
		["modControlVal", 0, 0],

		// N.B. the args below aren't used in the synthdef, just stored as synth args for convenience

		["oscString", "/example/text"],
		["latency", 0.0],

		["i_address1", "0.0.0.0"],
		["i_port1", 57120],
		["i_notes1", ""],
		["i_activate1", 0],
		["i_address2", "0.0.0.0"],
		["i_port2", 57120],
		["i_notes2", ""],
		["i_activate2", 0],
		["i_address3", "0.0.0.0"],
		["i_port3", 57120],
		["i_notes3", ""],
		["i_activate3", 0],
		["i_address4", "0.0.0.0"],
		["i_port4", 57120],
		["i_notes4", ""],
		["i_activate4", 0],
		["i_address5", "0.0.0.0"],
		["i_port5", 57120],
		["i_notes5", ""],
		["i_activate5", 0],
		["i_address6", "0.0.0.0"],
		["i_port6", 57120],
		["i_notes6", ""],
		["i_activate6", 0],
		["i_address7", "0.0.0.0"],
		["i_port7", 57120],
		["i_notes7", ""],
		["i_activate7", 0],
		["i_address8", "0.0.0.0"],
		["i_port8", 57120],
		["i_notes8", ""],
		["i_activate8", 0],
		["i_address9", "0.0.0.0"],
		["i_port9", 57120],
		["i_notes9", ""],
		["i_activate9", 0],
		["i_address10", "0.0.0.0"],
		["i_port10", 57120],
		["i_notes10", ""],
		["i_activate10", 0],
	];
	arrOptions = [1, 0];
	arrOptionData = [
		[	["10 times per second", 10],
			["20 times per second - default", 20],
			["40 times per second", 40],
			["80 times per second", 80],
			["5 times per second", 5],
			["once every second", 1],
			["once every 2 seconds", 0.5],
			["once every 4 seconds", 0.25],
		],
		[	["Number: Float", {arg argNum; argNum}],
			["Number: Integer - floats are rounded to nearest integer", {arg argNum; argNum.round}],
		],
	];
	synthDefFunc = { arg out, on, controlValue, controlValMin, controlValMax, modControlVal;
	   var trig, trig2, sumControl, dataRate, numberFunc;
	   sumControl = controlValMin + ((controlValMax - controlValMin) * (controlValue + modControlVal).max(0).min(1));
	   // select datarate based on arrOptions
	   dataRate = this.getSynthOption(0);
	   // trigger current value to be sent when value changes
	   trig = Trig.kr((1 - Impulse.kr(dataRate)) * HPZ1.kr(sumControl).abs, 0.02);
	   // trig2 no longer used
	   //trig2 = Impulse.kr(1);
	   numberFunc = this.getSynthOption(1);
	   SendTrig.kr(trig * on, sendTrigID, numberFunc.value(sumControl));
	   // Note this synth doesn't need to write to the output bus
	};
	this.buildGuiSpecArray;
	arrActionSpecs = this.buildActionSpecs([
			["TXTextBox", "OSC String", "oscString"],
			["EZNumber", "Latency", ControlSpec(0, 1), "latency"],
			["SynthOptionPopup", "Data rate", arrOptionData, 0],
			["TXMinMaxSliderSplit", "Value", ControlSpec(-1000000.0, 1000000.0), "controlValue",
				"controlValMin", "controlValMax", nil,
				[["Presets:", []], ["0 / 1", [0,1]], ["0 / 127", [0, 127]],
					["0 / 255", [0, 255]], ["-1  /  1", [-1, 1]], ]],
			["SynthOptionPopup", "Output type", arrOptionData, 1],
			["TXCheckBox", "Active", "on"],
			["TXNetAddress","Address 1", "i_address1", {this.buildArrNetAddresses;}],
			["EZNumber", "Port 1", ControlSpec(0, 99999, 'lin', 1), "i_port1"],
			["TXTextBox","Notes", "i_notes1"],
			["TXCheckBox", "Activate", "i_activate1"],
			["TXNetAddress","Address 2", "i_address2", {this.buildArrNetAddresses;}],
			["EZNumber", "Port 2", ControlSpec(0, 99999, 'lin', 1), "i_port2"],
			["TXTextBox","Notes", "i_notes2"],
			["TXCheckBox", "Activate", "i_activate2"],
			["TXNetAddress","Address 3", "i_address3", {this.buildArrNetAddresses;}],
			["EZNumber", "Port 3", ControlSpec(0, 99999, 'lin', 1), "i_port3"],
			["TXTextBox","Notes", "i_notes3"],
			["TXCheckBox", "Activate", "i_activate3"],
			["TXNetAddress","Address 4", "i_address4", {this.buildArrNetAddresses;}],
			["EZNumber", "Port 4", ControlSpec(0, 99999, 'lin', 1), "i_port4"],
			["TXTextBox","Notes", "i_notes4"],
			["TXCheckBox", "Activate", "i_activate4"],
			["TXNetAddress","Address 5", "i_address5", {this.buildArrNetAddresses;}],
			["EZNumber", "Port 5", ControlSpec(0, 99999, 'lin', 1), "i_port5"],
			["TXTextBox","Notes", "i_notes5"],
			["TXCheckBox", "Activate", "i_activate5"],
			["TXNetAddress","Address 6", "i_address6", {this.buildArrNetAddresses;}],
			["EZNumber", "Port 6", ControlSpec(0, 99999, 'lin', 1), "i_port6"],
			["TXTextBox","Notes", "i_notes6"],
			["TXCheckBox", "Activate", "i_activate6"],
			["TXNetAddress","Address 7", "i_address7", {this.buildArrNetAddresses;}],
			["EZNumber", "Port 7", ControlSpec(0, 99999, 'lin', 1), "i_port7"],
			["TXTextBox","Notes", "i_notes7"],
			["TXCheckBox", "Activate", "i_activate7"],
			["TXNetAddress","Address 8", "i_address8", {this.buildArrNetAddresses;}],
			["EZNumber", "Port 8", ControlSpec(0, 99999, 'lin', 1), "i_port8"],
			["TXTextBox","Notes", "i_notes8"],
			["TXCheckBox", "Activate", "i_activate8"],
			["TXNetAddress","Address 9", "i_address9", {this.buildArrNetAddresses;}],
			["EZNumber", "Port 9", ControlSpec(0, 99999, 'lin', 1), "i_port9"],
			["TXTextBox","Notes", "i_notes9"],
			["TXCheckBox", "Activate", "i_activate9"],
			["TXNetAddress","Address 10", "i_address10", {this.buildArrNetAddresses;}],
			["EZNumber", "Port 10", ControlSpec(0, 99999, 'lin', 1), "i_port10"],
			["TXTextBox","Notes", "i_notes10"],
			["TXCheckBox", "Activate", "i_activate10"],
			["commandAction", "Copy Addresses 1-10 to all OSC Out modules",
				{this.copyAddsToAllOSCOuts}],
			["commandAction", "Copy Addresses 1-10 to all OSC Control Out modules",
				{this.copyAddsToAllOSCControlOuts}],
	]);
	//	use base class initialise
	this.baseInit(this, argInstName);
	this.oscControlActivate;
	//	load the synthdef and create the synth
	this.loadAndMakeSynth;
}

buildGuiSpecArray {
	guiSpecArray = [
		["ActionButton", "Message", {displayOption = "showMessage";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showMessage")],
		["Spacer", 3],
		["ActionButton", "Addresses 1-4", {displayOption = "showAddresses";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showAddresses")],
		["Spacer", 3],
		["ActionButton", "Addresses 5-8", {displayOption = "showAddresses2";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showAddresses2")],
		["Spacer", 3],
		["ActionButton", "Addresses 9-10", {displayOption = "showAddresses3";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showAddresses3")],
		["DividingLine"],
		["SpacerLine", 6],
	];
	if (displayOption == "showMessage", {
		guiSpecArray = guiSpecArray ++[
			["TXTextBox", "OSC String", "oscString", nil, 380],
			["SpacerLine", 4],
			["EZNumber", "Latency", ControlSpec(0, 1), "latency"],
			["SpacerLine", 4],
			["SynthOptionPopup", "Data rate", arrOptionData, 0],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Value", ControlSpec(-1000000.0, 1000000.0), "controlValue",
				"controlValMin", "controlValMax", nil,
				[["Presets:", []], ["0 : 1", [0,1]], ["0 : 127", [0, 127]],
					["0 : 255", [0, 255]], ["-1  :  1", [-1, 1]], ]],
			["SpacerLine", 4],
			["SynthOptionPopup", "Output type", arrOptionData, 1],
			["SpacerLine", 6],
			["TXCheckBox", "Active", "on"],
		];
	});
	if (displayOption == "showAddresses", {
		guiSpecArray = guiSpecArray ++[
			["TXNetAddress","Address 1", "i_address1", {this.buildArrNetAddresses;}, 380],
			["NextLine"],
			["EZNumber", "Port 1", ControlSpec(0, 99999, 'lin', 1), "i_port1"],
			["ActionButton", "default port", {this.setSynthArgSpec("i_port1", 57120); system.flagGuiUpd;},
				100, TXColor.white, TXColor.sysGuiCol2],
			["Spacer"],
			["TXCheckBox", "Activate", "i_activate1", {this.buildArrNetAddresses;}],
			["NextLine"],
			["TXTextBox","Notes", "i_notes1", nil, 380],
			["DividingLine"],
			["SpacerLine", 2],
			["TXNetAddress","Address 2", "i_address2", {this.buildArrNetAddresses;}, 380],
			["NextLine"],
			["EZNumber", "Port 2", ControlSpec(0, 99999, 'lin', 1), "i_port2"],
			["ActionButton", "default port", {this.setSynthArgSpec("i_port2", 57120); system.flagGuiUpd;},
				100, TXColor.white, TXColor.sysGuiCol2],
			["Spacer"],
			["TXCheckBox", "Activate", "i_activate2", {this.buildArrNetAddresses;}],
			["NextLine"],
			["TXTextBox","Notes", "i_notes2", nil, 380],
			["DividingLine"],
			["SpacerLine", 2],
			["TXNetAddress","Address 3", "i_address3", {this.buildArrNetAddresses;}, 380],
			["NextLine"],
			["EZNumber", "Port 3", ControlSpec(0, 99999, 'lin', 1), "i_port3"],
			["ActionButton", "default port", {this.setSynthArgSpec("i_port3", 57120); system.flagGuiUpd;},
				100, TXColor.white, TXColor.sysGuiCol2],
			["Spacer"],
			["TXCheckBox", "Activate", "i_activate3", {this.buildArrNetAddresses;}],
			["NextLine"],
			["TXTextBox","Notes", "i_notes3", nil, 380],
			["DividingLine"],
			["SpacerLine", 2],
			["TXNetAddress","Address 4", "i_address4", {this.buildArrNetAddresses;}, 380],
			["NextLine"],
			["EZNumber", "Port 4", ControlSpec(0, 99999, 'lin', 1), "i_port4"],
			["ActionButton", "default port", {this.setSynthArgSpec("i_port4", 57120); system.flagGuiUpd;},
				100, TXColor.white, TXColor.sysGuiCol2],
			["Spacer"],
			["TXCheckBox", "Activate", "i_activate4", {this.buildArrNetAddresses;}],
			["NextLine"],
			["TXTextBox","Notes", "i_notes4", nil, 380],
			["DividingLine"],
			["SpacerLine", 2],
			["ActionButton", "Copy Addresses 1-10 to all OSC Out modules",
				{this.copyAddsToAllOSCOuts}, 400],
			["SpacerLine", 2],
			["ActionButton", "Copy Addresses 1-10 to all OSC Control Out modules",
				{this.copyAddsToAllOSCControlOuts}, 400],
		];
	});
	if (displayOption == "showAddresses2", {
		guiSpecArray = guiSpecArray ++[
			["TXNetAddress","Address 5", "i_address5", {this.buildArrNetAddresses;}, 380],
			["NextLine"],
			["EZNumber", "Port 5", ControlSpec(0, 99999, 'lin', 1), "i_port5"],
			["ActionButton", "default port", {this.setSynthArgSpec("i_port5", 57120); system.flagGuiUpd;},
				100, TXColor.white, TXColor.sysGuiCol2],
			["Spacer"],
			["TXCheckBox", "Activate", "i_activate5", {this.buildArrNetAddresses;}],
			["NextLine"],
			["TXTextBox","Notes", "i_notes5", nil, 380],
			["DividingLine"],
			["SpacerLine", 2],
			["TXNetAddress","Address 6", "i_address6", {this.buildArrNetAddresses;}, 380],
			["NextLine"],
			["EZNumber", "Port 6", ControlSpec(0, 99999, 'lin', 1), "i_port6"],
			["ActionButton", "default port", {this.setSynthArgSpec("i_port6", 57120); system.flagGuiUpd;},
				100, TXColor.white, TXColor.sysGuiCol2],
			["Spacer"],
			["TXCheckBox", "Activate", "i_activate6", {this.buildArrNetAddresses;}],
			["NextLine"],
			["TXTextBox","Notes", "i_notes6", nil, 380],
			["DividingLine"],
			["SpacerLine", 2],
			["TXNetAddress","Address 7", "i_address7", {this.buildArrNetAddresses;}, 380],
			["NextLine"],
			["EZNumber", "Port 7", ControlSpec(0, 99999, 'lin', 1), "i_port7"],
			["ActionButton", "default port", {this.setSynthArgSpec("i_port7", 57120); system.flagGuiUpd;},
				100, TXColor.white, TXColor.sysGuiCol2],
			["Spacer"],
			["TXCheckBox", "Activate", "i_activate7", {this.buildArrNetAddresses;}],
			["NextLine"],
			["TXTextBox","Notes", "i_notes7", nil, 380],
			["DividingLine"],
			["SpacerLine", 2],
			["TXNetAddress","Address 8", "i_address8", {this.buildArrNetAddresses;}, 380],
			["NextLine"],
			["EZNumber", "Port 8", ControlSpec(0, 99999, 'lin', 1), "i_port8"],
			["ActionButton", "default port", {this.setSynthArgSpec("i_port8", 57120); system.flagGuiUpd;},
				100, TXColor.white, TXColor.sysGuiCol2],
			["Spacer"],
			["TXCheckBox", "Activate", "i_activate8", {this.buildArrNetAddresses;}],
			["NextLine"],
			["TXTextBox","Notes", "i_notes8", nil, 380],
			["DividingLine"],
			["SpacerLine", 2],
			["ActionButton", "Copy Addresses 1-10 to all OSC Out modules",
				{this.copyAddsToAllOSCOuts}, 400],
			["SpacerLine", 2],
			["ActionButton", "Copy Addresses 1-10 to all OSC Control Out modules",
				{this.copyAddsToAllOSCControlOuts}, 400],
		];
	});
	if (displayOption == "showAddresses3", {
		guiSpecArray = guiSpecArray ++[
			["TXNetAddress","Address 9", "i_address9", {this.buildArrNetAddresses;}, 380],
			["NextLine"],
			["EZNumber", "Port 9", ControlSpec(0, 99999, 'lin', 1), "i_port9"],
			["ActionButton", "default port", {this.setSynthArgSpec("i_port9", 57120); system.flagGuiUpd;},
				100, TXColor.white, TXColor.sysGuiCol2],
			["Spacer"],
			["TXCheckBox", "Activate", "i_activate9", {this.buildArrNetAddresses;}],
			["NextLine"],
			["TXTextBox","Notes", "i_notes9", nil, 380],
			["DividingLine"],
			["SpacerLine", 2],
			["TXNetAddress","Address 10", "i_address10", {this.buildArrNetAddresses;}, 380],
			["NextLine"],
			["EZNumber", "Port 10", ControlSpec(0, 99999, 'lin', 1), "i_port10"],
			["ActionButton", "default port", {this.setSynthArgSpec("i_port10", 57120); system.flagGuiUpd;},
				100, TXColor.white, TXColor.sysGuiCol2],
			["Spacer"],
			["TXCheckBox", "Activate", "i_activate10", {this.buildArrNetAddresses;}],
			["NextLine"],
			["TXTextBox","Notes", "i_notes10", nil, 380],
			["DividingLine"],
			["SpacerLine", 2],
			["ActionButton", "Copy Addresses 1-10 to all OSC Out modules",
				{this.copyAddsToAllOSCOuts}, 400],
			["SpacerLine", 2],
			["ActionButton", "Copy Addresses 1-10 to all OSC Control Out modules",
				{this.copyAddsToAllOSCControlOuts}, 400],
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

oscControlActivate {
	//	remove any previous OSCresponderNode and add new
	this.oscControlDeactivate;
	oscControlResp = OSCresponderNode(system.server.addr,'/tr',{ arg time,responder,msg;
		if (msg[2] == sendTrigID,{
			this.sendMessage(msg[3]);
		});
	}).add;
}

oscControlDeactivate {
	//	remove responder
	oscControlResp.remove;
}

buildArrNetAddresses{
	var holdAddress, holdPort;
	arrNetAddresses = [];
	10.do({arg item, i;
		if (this.getSynthArgSpec("i_activate" ++ (i+1).asString) == 1, {
			holdAddress = this.getSynthArgSpec("i_address" ++ (i+1).asString);
			holdPort = this.getSynthArgSpec("i_port" ++ (i+1).asString);
			arrNetAddresses = arrNetAddresses.add(NetAddr(holdAddress, holdPort));
		});
	});
}

sendMessage {arg holdArg;
	var holdMessage, holdTimestamp;
	// build message
	holdMessage = [this.getSynthArgSpec("oscString"), holdArg];
	// get latency
	holdTimestamp = this.getSynthArgSpec("latency");
	// send message to addresses
	arrNetAddresses.do({arg item, i;
		item.sendBundle(holdTimestamp, holdMessage);
	});
}

copyAddsToAllOSCOuts {
	TXOSCOut.addressesCopyAll(this.arrAddressData);
}

copyAddsToAllOSCControlOuts {
	TXOSCControlOut.addressesCopyAll(this.arrAddressData);
}

arrAddressData {
	^[ "i_address1", "i_port1", "i_notes1", "i_activate1", "i_address2", "i_port2", "i_notes2", "i_activate2", "i_address3", "i_port3", "i_notes3", "i_activate3", "i_address4", "i_port4", "i_notes4", "i_activate4", "i_address5", "i_port5", "i_notes5", "i_activate5", "i_address6", "i_port6", "i_notes6", "i_activate6", "i_address7", "i_port7", "i_notes7", "i_activate7", "i_address8", "i_port8", "i_notes8", "i_activate8", "i_address9", "i_port9", "i_notes9", "i_activate9", "i_address10", "i_port10", "i_notes10", "i_activate10"
	].collect ({arg item, i; this.getSynthArgSpec(item);});
}

loadArrAddressData { arg argData;
	[ "i_address1", "i_port1", "i_notes1", "i_activate1", "i_address2", "i_port2", "i_notes2", "i_activate2", "i_address3", "i_port3", "i_notes3", "i_activate3", "i_address4", "i_port4", "i_notes4", "i_activate4", "i_address5", "i_port5", "i_notes5", "i_activate5", "i_address6", "i_port6", "i_notes6", "i_activate6", "i_address7", "i_port7", "i_notes7", "i_activate7", "i_address8", "i_port8", "i_notes8", "i_activate8", "i_address9", "i_port9", "i_notes9", "i_activate9", "i_address10", "i_port10", "i_notes10", "i_activate10"
	].do ({arg item, i; this.setSynthValue(item, argData[i]);});
	this.buildArrNetAddresses;
}

loadExtraData {arg argData;  // override default method
	this.buildArrNetAddresses;
}

deleteModuleExtraActions {
	//	remove OSCresponderNode
	this.oscControlDeactivate;
}

}

