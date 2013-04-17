// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMidiControlOut : TXModuleBase {		// Midi Out module 

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
	
	var	midiControlResp;
	var	sendTrigID;
	var	holdMidiOut;

*initClass{
	arrInstances = [];		
	//	set class specific variables
	defaultName = "Midi Control Out";
	moduleRate = "control";
	moduleType = "insert";
	noInChannels = 1;			
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
	// create unique id
	sendTrigID = UniqueID.next;
	arrSynthArgSpecs = [
		["in", 0, 0],
		["out", 0, 0],
		["on", 0, 0],
	]; 
	synthDefFunc = { arg in, out, on;
	   var trig, input;
	   input = (In.kr(in) * 127).round;
	   trig = Trig.kr(Impulse.kr(300) * on * HPZ1.kr(input).abs, 0.01); 
	   SendTrig.kr(trig, sendTrigID, input);
	   // Note this synth doesn't need to write to the output bus
	};
	guiSpecArray = [
		["MIDIOutPortSelector"], 
		["NextLine"], 
		["MIDISoloChannelSelector"], 
		["NextLine"], 
		["MIDISoloControllerSelector"], 
		["NextLine"], 
		["TXCheckBox", "Active", "on"],
	];
	arrActionSpecs = this.buildActionSpecs(guiSpecArray);
	//	use base class initialise 
	this.baseInit(this, argInstName);
	this.midiControlActivate;
	//	load the synthdef and create the synth
	this.loadAndMakeSynth;
}

midiControlActivate {
	if (MIDIClient.destinations.size > 0, {
		//  port
		this.midiPortActivate;
		//	remove any previous OSCresponderNode and add new
		this.midiControlDeactivate;
		midiControlResp = OSCresponderNode(system.server.addr,'/tr',{ arg time,responder,msg;
			if (msg[2] == sendTrigID,{
				holdMidiOut.control (midiMinChannel, midiMinControlNo, msg[3]);
			});
		}).add;
	});
}

midiPortActivate {
	holdMidiOut =  MIDIOut(0, MIDIClient.destinations.at(midiOutPort.asInteger).uid);
}

midiControlDeactivate { 
	//	remove responder 
	midiControlResp.remove;
}

deleteModuleExtraActions {     
	//	remove OSCresponderNoder
	this.midiControlDeactivate;
}

}

