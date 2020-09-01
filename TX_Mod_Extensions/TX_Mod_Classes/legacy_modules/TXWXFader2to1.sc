// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXWXFader2to1 : TXModuleBase {

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

*initClass{
	arrInstances = [];
	//	set class specific variables
	defaultName = "X-Fader 2-1";
	moduleRate = "audio";
	moduleType = "source";
	arrCtlSCInBusSpecs = [
		["X-Fade", 1, "modXFade", 0],
	];
	arrAudSCInBusSpecs = [
		 ["Input 1", 1, "input1"],
		 ["Input 2", 1, "input2"]
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
	extraLatency = 0.2;	// allow extra time when recreating
	arrSynthArgSpecs = [
		["input1", 0, 0],
		["input2", 0, 0],
		["out", 0, 0],
		["level1", 1, defLagTime],
		["level2", 1, defLagTime],
		["xFade", 0.5, defLagTime],
		["modXFade", 0, defLagTime],
	];
	synthDefFunc = { arg input1, input2, out, level1, level2, xFade, modXFade=0;
		var holdInput1, holdInput2, holdXFade;
		holdInput1 = InFeedback.ar(input1, 1) * level1;
		holdInput2 = InFeedback.ar(input2, 1) * level2;
		holdXFade = (xFade + modXFade).max(0).min(1);
		Out.ar(out, Mix.ar([(holdInput1 * (1-holdXFade)), (holdInput2 * holdXFade)]));
	};
	guiSpecArray = [
		["EZslider", "Level 1", ControlSpec(0, 1), "level1"],
		["SpacerLine", 4],
		["EZslider", "Level 2", ControlSpec(0, 1), "level2"],
		["SpacerLine", 4],
		["EZslider", "X-Fade", ControlSpec(0, 1), "xFade"],
	];
	arrActionSpecs = this.buildActionSpecs(guiSpecArray);
	//	use base class initialise
	this.baseInit(this, argInstName);
	//	load the synthdef and create the synth
	this.loadAndMakeSynth;
}

}

