// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMIDIPitchbend2 : TXModuleBase {

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
	defaultName = "Midi Pitchbend";
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
	arrOptions = [2];
	arrOptionData = [
		[	
			["Positive only: 0 to 1", {arg input; input + 0.5;}],
			["Positive & Negative: -1 to 1", {arg input; input * 2; }],
			["Positive & Negative: -0.5 to 0.5", {arg input; input }],
		];
	];
	guiSpecArray = [
		["MIDIChannelSelector"], 
		["SpacerLine", 4], 
		["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 0], 
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
	midiControlResp = BendResponder({|src, chan, bend|
		// set the Bus value
		if (	(outBus.class == Bus)
			and: (chan >= (midiMinChannel-1)) and: (chan <= (midiMaxChannel-1))
	 	, {
	 		outBus.value_(this.getSynthOption(0).value((bend - 8192) / 16384) ); 
	 	});
	});
}

midiControlDeactivate { 
	//	stop responding to midi. 
 	if (midiControlResp.class == BendResponder, {
 		midiControlResp.remove; 
 	});
 }

rebuildSynth { 
	// override base class method
}
}

