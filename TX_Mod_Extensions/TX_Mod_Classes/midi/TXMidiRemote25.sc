// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).


//	THIS MODULE IS UNFINISHED



TXMidiRemote25 : TXModuleBase {

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
	
	var	midiControlRoutine;

*initClass{
	arrInstances = [];		
	//	set class specific variables
	defaultName = "Midi Remote25";
	moduleRate = "control";
	moduleType = "source";
	arrCtlSCInBusSpecs = [];	
	noOutChannels = 24;
	arrOutBusSpecs = [ 
		["Out 0", [0]],
		["Out 1", [1]],
		["Out 2", [2]],
		["Out 3", [3]],
		["Out 4", [4]],
		["Out 5", [5]],
		["Out 6", [6]],
		["Out 7", [7]],
		["Out 8", [8]],
		["Out 9", [9]],
		["Out 10", [10]],
		["Out 11", [11]],
		["Out 12", [12]],
		["Out 13", [13]],
		["Out 14", [14]],
		["Out 15", [15]],
		["Out 16", [16]],
		["Out 17", [17]],
		["Out 18", [18]],
		["Out 19", [19]],
		["Out 20", [20]],
		["Out 21", [21]],
		["Out 22", [22]],
		["Out 23", [23]],
		["Out 2", [2]],
		["Out 2", [2]],
		["Out 2", [2]],
		["Out 2", [2]],
	];	
} 

*new{ arg argInstName;
	 ^super.new.init(argInstName);
} 

init {arg argInstName;
	//	set  class specific instance variables
	arrSynthArgSpecs = [
		["out", 0, 0],
		["controller0", 0, 0],
		["controller1", 0, 0],
		["controller2", 0, 0],
		["controller3", 0, 0],
		["controller4", 0, 0],
		["controller5", 0, 0],
		["controller6", 0, 0],
		["controller7", 0, 0],
		["controller8", 0, 0],
		["controller9", 0, 0],
		["controller10", 0, 0],
		["controller11", 0, 0],
		["controller12", 0, 0],
		["controller13", 0, 0],
		["controller14", 0, 0],
		["controller15", 0, 0],
		["controller16", 0, 0],
		["controller17", 0, 0],
		["controller18", 0, 0],
		["controller19", 0, 0],
		["controller20", 0, 0],
		["controller21", 0, 0],
		["controller22", 0, 0],
		["controller23", 0, 0],
	]; 
	guiSpecArray = [
		["MIDIChannelSelector"], 
		["NextLine"], 
		["MIDISoloControllerSelector"], 
	];
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
	midiControlRoutine = Routine({
		var event;
		loop {
			event = MIDIIn.waitControl;
			// set the Bus value
		 	if ( (outBus.class == Bus) and: (event.ctlnum == midiMinControlNo), {
		 		outBus.value_(event.ctlval/127); 
		 	});
		}
	}).play;
}

midiControlDeactivate { 
	//	stop responding to midi. 
 	if (midiControlRoutine.class == Routine, {
 		midiControlRoutine.stop; 
 	});
 }

}

