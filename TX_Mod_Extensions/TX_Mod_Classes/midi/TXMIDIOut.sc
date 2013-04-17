// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMIDIOut : TXModuleBase {

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

	var	holdMIDIOutTypes;
	var	holdPortNames;
	var	holdMIDIOutPort, holdMIDIDeviceName, holdMIDIPortName;
	
*initClass{
	arrInstances = [];		
	//	set class specific variables
	defaultName = "Midi Out";
	moduleRate = "control";
	moduleType = "action";
	noInChannels = 0;
	noOutChannels = 0;
} 

*new{ arg argInstName;
	 ^super.new.init(argInstName);
} 

init {arg argInstName;
	//	set  class specific instance variables
	moduleNodeStatus = "running";

	arrSynthArgSpecs = [
		["midiPort", 0],
		["midiChannel", 1],
		["messageType", 0],
		["midiNote", 60],
		["midiVelocity", 100],
		["gateTime", 1.0],
		["midiControlNo", 0],
		["midiControlValue", 0],
		["midiProgramNo", 0],
		["midiPitchBend", 0],
		["midiPolyTouch", 0],
		["midiTouch", 0],
	]; 

	holdMIDIOutTypes = ["Note On", "Note Off", "Note On & Off", "Controller", "Program", "Pitch Bend", 
		"All Notes Off", "Poly Touch", "Touch"];
	
	holdPortNames = ["Unconnected - select MIDI Port"]
		++ MIDIClient.destinations.collect({arg item, i; 
			// remove any brackets from string
			(item.device.asString + item.name.asString).replace("(", "").replace(")", "")
		});
	
	this.buildGuiSpecArray;
	arrActionSpecs = this.buildActionSpecs([
		["commandAction", "Send MIDI Message Now", {this.sendMIDIMessage}],
		["TXPopupAction", "Port", holdPortNames, "midiPort", { arg view; this.initMidiPort(view.value); }], 
		["EZSlider", "Channel", ControlSpec(1, 16, step: 1), "midiChannel"], 
		["TXPopupAction", "MIDI Message", holdMIDIOutTypes, "messageType", 
			{ arg view; this.buildGuiSpecArray; system.showViewIfModDisplay(this); }], 
		["EZSlider", "Note", ControlSpec(0, 127, step: 1), "midiNote"], 
		["EZSlider", "Velocity", ControlSpec(0, 127, step: 1), "midiVelocity"], 
		["EZSlider", "Gate Time", ControlSpec(0.01, 20), "gateTime"], 
		["EZSlider", "Control no", ControlSpec(0, 127, step: 1), "midiControlNo"], 
		["EZSlider", "Control val", ControlSpec(0, 127, step: 1), "midiControlValue"], 
		["EZSlider", "Program No.", ControlSpec(0, 127, step: 1), "midiProgramNo"], 
		["EZSlider", "Pitch Bend", ControlSpec(-8192, 8192, step: 1), "midiPitchBend"], 
		["EZSlider", "Poly Touch", ControlSpec(0, 127, step: 1), "midiPolyTouch"], 
		["EZSlider", "Touch", ControlSpec(0, 127, step: 1), "midiTouch"], 
	]);
	//	use base class initialise 
	this.baseInit(this, argInstName);
}

buildGuiSpecArray {
	guiSpecArray = [
		["TXPopupAction", "Port", holdPortNames, "midiPort", { arg view; this.initMidiPort(view.value); }], 
		["NextLine"], 
		["EZSlider", "Channel", ControlSpec(1, 16, step: 1), "midiChannel"], 
		["SpacerLine", 8], 
		["TXPopupAction", "MIDI Message", holdMIDIOutTypes, "messageType", 
			{ arg view; this.buildGuiSpecArray; system.showViewIfModDisplay(this); }], 
		["NextLine"], 
	];
	// "Note On"
	if (this.getSynthArgSpec("messageType") == 0, {
		guiSpecArray = guiSpecArray ++[
			["EZSlider", "Note", ControlSpec(0, 127, step: 1), "midiNote"], 
			["EZSlider", "Velocity", ControlSpec(0, 127, step: 1), "midiVelocity"], 
		];
	});
	// "Note Off"
	if (this.getSynthArgSpec("messageType") == 1, {
		guiSpecArray = guiSpecArray ++[
			["EZSlider", "Note", ControlSpec(0, 127, step: 1), "midiNote"], 
			["EZSlider", "Velocity", ControlSpec(0, 127, step: 1), "midiVelocity"], 
		];
	});
	// "Note On & Off"
	if (this.getSynthArgSpec("messageType") == 2, {
		guiSpecArray = guiSpecArray ++[
			["EZSlider", "Note", ControlSpec(0, 127, step: 1), "midiNote"], 
			["EZSlider", "Velocity", ControlSpec(0, 127, step: 1), "midiVelocity"], 
			["EZSlider", "Gate Time", ControlSpec(0.01, 20), "gateTime"], 
		];
	});
	// "Controller"
	if (this.getSynthArgSpec("messageType") == 3, {
		guiSpecArray = guiSpecArray ++[
			["EZSlider", "Control no", ControlSpec(0, 127, step: 1), "midiControlNo"], 
			["EZSlider", "Control val", ControlSpec(0, 127, step: 1), "midiControlValue"], 
		];
	});
	// "Program"
	if (this.getSynthArgSpec("messageType") == 4, {
		guiSpecArray = guiSpecArray ++[
			["EZSlider", "Program No.", ControlSpec(0, 127, step: 1), "midiProgramNo"], 
		];
	});
	// "Pitch Bend"
	if (this.getSynthArgSpec("messageType") == 5, {
		guiSpecArray = guiSpecArray ++[
			["EZSlider", "Pitch Bend", ControlSpec(-8192, 8192, step: 1), "midiPitchBend"], 
		];
	});
	// "Poly Touch"
	if (this.getSynthArgSpec("messageType") == 7, {
		guiSpecArray = guiSpecArray ++[
			["EZSlider", "Poly Touch", ControlSpec(0, 127, step: 1), "midiPolyTouch"], 
		];
	});
	// "Touch"
	if (this.getSynthArgSpec("messageType") == 8, {
		guiSpecArray = guiSpecArray ++[
			["EZSlider", "Touch", ControlSpec(0, 127, step: 1), "midiTouch"], 
		];
	});
	// Action button
	guiSpecArray = guiSpecArray ++[
		["SpacerLine", 8], 
		["ActionButton", "Send MIDI Message Now", {this.sendMIDIMessage;}, 250, TXColor.white, TXColor.sysGuiCol2],
	];
}

initMidiPort { arg portInd = 0;
	if (portInd == 0, {
		holdMIDIOutPort = nil;
		holdMIDIDeviceName = nil;
		holdMIDIPortName = nil;
	},{
		holdMIDIOutPort = MIDIOut(portInd, MIDIClient.destinations[portInd - 1].uid);
		holdMIDIDeviceName =  MIDIClient.destinations[portInd - 1].device;
		holdMIDIPortName =  MIDIClient.destinations[portInd - 1].name;
		// minimise MIDI out latency
		holdMIDIOutPort.latency = 0;
	});
}

sendMIDIMessage {
	var portInd, channel;
	portInd = this.getSynthArgSpec("midiPort");
	channel = this.getSynthArgSpec("midiChannel") - 1;
	if ( (portInd > 0) and: (moduleNodeStatus == "running"), {
		// "Note On"
		if (this.getSynthArgSpec("messageType") == 0, {
			holdMIDIOutPort.noteOn(channel, this.getSynthArgSpec("midiNote"), this.getSynthArgSpec("midiVelocity"));
		});
		// "Note Off"
		if (this.getSynthArgSpec("messageType") == 1, {
			holdMIDIOutPort.noteOff(channel, this.getSynthArgSpec("midiNote"), this.getSynthArgSpec("midiVelocity"));
		});
		// "Note On & Off"
		if (this.getSynthArgSpec("messageType") == 2, {
			SystemClock.schedAbs( 0,{ 
				holdMIDIOutPort.noteOn(channel, this.getSynthArgSpec("midiNote"), this.getSynthArgSpec("midiVelocity"));
			});
			SystemClock.sched( this.getSynthArgSpec("gateTime"),{ 
				holdMIDIOutPort.noteOff(channel, this.getSynthArgSpec("midiNote"), 0);
				nil;
			});
		});
		// "Controller"
		if (this.getSynthArgSpec("messageType") == 3, {
			holdMIDIOutPort.control(channel, this.getSynthArgSpec("midiControlNo"), this.getSynthArgSpec("midiControlValue"));
		});
		// "Program"
		if (this.getSynthArgSpec("messageType") == 4, {
			holdMIDIOutPort.program(channel, this.getSynthArgSpec("midiProgramNo"));
		});
		// "Pitch Bend"
		if (this.getSynthArgSpec("messageType") == 5, {
			holdMIDIOutPort.bend(channel, this.getSynthArgSpec("midiPitchBend"));
		});
		// All Notes Off
		if (this.getSynthArgSpec("messageType") == 6, {
			holdMIDIOutPort.allNotesOff(channel);
		});
		// "Poly Touch"
		if (this.getSynthArgSpec("messageType") == 7, {
			holdMIDIOutPort.polyTouch(channel, this.getSynthArgSpec("midiPolyTouch"));
		});
		// "Touch"
		if (this.getSynthArgSpec("messageType") == 8, {
			holdMIDIOutPort.touch(channel, this.getSynthArgSpec("midiTouch"));
		});
	});	
}
extraSaveData { // override default method
	^[holdMIDIDeviceName, holdMIDIPortName];
}

loadExtraData {arg argData;  // override default method
	var portIndex;
	portIndex = this.getSynthArgSpec("midiPort");
	holdMIDIDeviceName = argData.at(0);
	holdMIDIPortName = argData.at(1);
	// if names given, find correct port from names
	if ( holdMIDIDeviceName.notNil and: holdMIDIPortName.notNil, {
		// default to 0 in case device/port names not found
		portIndex = 0;
		this.setSynthArgSpec("midiPort", 0);
		MIDIClient.destinations.do({arg item, i;
			if ( (holdMIDIDeviceName == item.device) and: (holdMIDIPortName == item.name), {
				portIndex = i + 1;
				this.setSynthArgSpec("midiPort", portIndex);
			});
		});
	});
	this.initMidiPort(portIndex);
	this.buildGuiSpecArray;
}

runAction {   //	override base class
	moduleNodeStatus = "running";
}

pauseAction {   //	override base class
	moduleNodeStatus = "paused";
}

}

