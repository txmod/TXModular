// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMIDITestTone : TXModuleBase {

	classvar <>arrInstances;	
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "groupsource", "insert", "bus",or "channel"
	classvar <noInChannels;			// no of input channels 
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs 
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels 
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar	<guiWidth=500;

*initClass{
	arrInstances = [];		
	//	set class specific variables
	defaultName = "Midi Test Tone";
	moduleRate = "audio";
	moduleType = "groupsource";
	arrCtlSCInBusSpecs = [ 
		["Shape", 1, "modShape", 0]
	];	
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
		["gate", 1, 0],
		["note", 0, 0],
		["velocity", 0, 0],
		["shape", 0.5, defLagTime],
		["modShape", 0, defLagTime],
	]; 
	synthDefFunc = { arg out, gate, note, velocity, shape, modShape = 0;
		var mixOut;
		// amplitude is vel *  0.00315 approx. == 1 / 127 * 0.4
		mixOut =  EnvGen.kr(Env.adsr, gate, doneAction: 2) * 
			VarSaw.ar (note.midicps, 0, (shape + modShape).max(0).min(1), (velocity * 0.00315));
		Out.ar(out, mixOut);
	};
	guiSpecArray = [
		["allNotesOffButton"], 
		["NextLine"], 
		["MIDIChannelSelector"], 
		["NextLine"], 
		["EZslider", "Shape", \unipolar, "shape"], 
	];
	//	use base class initialise 
	this.baseInit(this, argInstName);
	this.midiNoteInit;
	//	load the synthdef and create the Group for synths to belong to
	this.loadDefAndMakeGroup;
}

}

