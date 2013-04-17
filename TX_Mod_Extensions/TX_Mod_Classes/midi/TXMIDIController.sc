// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMIDIController : TXModuleBase {

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

*initClass{
	arrInstances = [];		
	//	set class specific variables
	defaultName = "Midi Controller";
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
		["controller", 0, 0],
	]; 
	guiSpecArray = [
		["MIDIChannelSelector"], 
		["NextLine"], 
		["MIDISoloControllerSelector"], 
	];
	arrActionSpecs = this.buildActionSpecs(guiSpecArray);
	//	use base class initialise 
	this.baseInit(this, argInstName);
	this.midiControlActivate;
}

runAction {this.midiControlActivate}   //	override base class

pauseAction {this.midiControlDeactivate}   //	override base class

midiControlActivate { 
	//	stop any previous routine 
	this.midiControlDeactivate;
	//	start routine 
	midiControlResp = CCResponder({ |src, chan, num, val|
		// set the Bus value
		if (	(outBus.class == Bus)
			and: (chan >= (midiMinChannel-1)) and: (chan <= (midiMaxChannel-1))
			and: (num == midiMinControlNo)
	 	, {
	 		outBus.value_(val/127); 
	 	});
	});
}

midiControlDeactivate { 
	//	stop responding to midi. 
 	if (midiControlResp.class == CCResponder, {
 		midiControlResp.remove; 
 	});
 }

}

