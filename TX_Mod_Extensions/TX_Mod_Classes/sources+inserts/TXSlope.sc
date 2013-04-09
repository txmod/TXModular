// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSlope : TXModuleBase {

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
	defaultName = "Slope";
	moduleRate = "control";
	moduleType = "insert";
	noInChannels = 1;
	arrCtlSCInBusSpecs = [
		["Sensitivity", 1, "modSense", 0],
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
	var holdControlSpec;
	holdControlSpec = ControlSpec(0.001, 100, \exp);
	//	set  class specific instance variables
	arrSynthArgSpecs = [
		["in", 0, 0],
		["out", 0, 0],
		["sense", holdControlSpec.unmap(1), defLagTime],
		["senseMin", 0.001, defLagTime],
		["senseMax", 100, defLagTime],
		["modSense", 0, defLagTime],
	];
	arrOptions = [0, 0];
	arrOptionData = [
		[
			["velocity - slope of input signal",
				{arg input; Slope.kr(input); }
			],
			["Acceleration - slope of slope of input signal",
				{arg input; Slope.kr(Slope.kr(input)); }
			],
		],
		[
			["Output can be positive or negative: range -1 to +1",
				{arg input; input; }
			],
			["Force positive output - range 0 to +1",
				{arg input; input.abs; }
			],
		]
	];
	synthDefFunc = { arg in, out, sense, senseMin, senseMax, modSense;
		var inSignal, sensitivity, outFunction, slopeFunction, outSignal;
		inSignal = TXClean.kr(In.kr(in,1));
		sensitivity = ( (senseMax/senseMin) ** ((sense + modSense).max(0.0001).min(1)) ) * senseMin;
		slopeFunction = this.getSynthOption(0);
		outFunction = this.getSynthOption(1);
		outSignal = outFunction.value((slopeFunction.value(inSignal) * sensitivity).max(-1).min(1));
		Out.kr(out, TXClean.kr(outSignal));
	};
	guiSpecArray = [
		["SynthOptionPopupPlusMinus", "Slope type", arrOptionData, 0],
		["SpacerLine", 4],
		["TXMinMaxSliderSplit", "Sensitivity", holdControlSpec, "sense", "senseMin", "senseMax"],
		["SpacerLine", 4],
		["SynthOptionPopupPlusMinus", "Output", arrOptionData, 1],
	];
	arrActionSpecs = this.buildActionSpecs(guiSpecArray);
	//	use base class initialise
	this.baseInit(this, argInstName);
	//	load the synthdef and create the synth
	this.loadAndMakeSynth;
}

}

