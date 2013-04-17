// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMIDIController2 : TXModuleBase {

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
	var	midiCCNoResponder;

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
		["midiLearn", 0, \ir],
	]; 
	arrOptions = [0];
	arrOptionData = [
		[	
			["Positive only: 0 to 1", {arg input; input;}],
			["Positive & Negative: -1 to 1", {arg input; (input * 2) - 1; }],
			["Positive & Negative: -0.5 to 0.5", {arg input; input - 0.5 }],
		];
	];
	guiSpecArray = [
		["MIDIChannelSelector"], 
		["SpacerLine", 4], 
		["MIDISoloControllerSelector"], 
		["Spacer", 20], 
		["TXCheckBox", "MIDI Learn", "midiLearn", 
			{arg view; 
				if (view.value == 0, {
					if (midiCCNoResponder.notNil, {midiCCNoResponder.remove;});
					midiCCNoResponder = nil;
				},{
					midiCCNoResponder = CCResponder({ |src, chan, num, val|
						// stop responder
						midiCCNoResponder.remove;
						//  set min/max channel and controller no
						midiMinChannel = chan + 1;
						midiMaxChannel = chan + 1;
						midiMinControlNo = num;
						midiMaxControlNo = num;
						this.setSynthArgSpec("midiLearn", 0);
						// update view
						system.showView;
					});
				});
			}, 
			180],
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
	midiControlResp = CCResponder({ |src, chan, num, val|
		// set the Bus value
		if (	(outBus.class == Bus)
			and: (chan >= (midiMinChannel-1)) and: (chan <= (midiMaxChannel-1))
			and: (num == midiMinControlNo)
	 	, {
	 		outBus.value_(this.getSynthOption(0).value(val/127);); 
	 	});
	});
}

midiControlDeactivate { 
	//	stop responding to midi. 
 	if (midiControlResp.class == CCResponder, {
 		midiControlResp.remove; 
 	});
 }

rebuildSynth { 
	// override base class method
}

loadExtraData {arg argData;  // override default method
	// override value
	this.setSynthArgSpec("midiLearn", 0);
}

}

