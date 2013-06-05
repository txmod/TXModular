// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXControlDelay4 : TXModuleBase {		// delay module

	//	Notes:
	//	This is a delay which can be set to any time up to 16 secs.
	//	This version uses BufCombC

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
	classvar	<arrBufferSpecs;

	var 		holdTapTime, newTapTime;

*initClass{
	arrInstances = [];
	//	set class specific variables
	defaultName = "Control Delay";
	moduleRate = "control";
	moduleType = "insert";
	noInChannels = 1;
	arrCtlSCInBusSpecs = [
		["Delay Time", 1, "modDelay", 0],
		["Wet-Dry Mix", 1, "modWetDryMix", 0]
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
	arrOptions = [0];
	arrOptionData = [
		[
			["5 seconds", 5],
			["15 seconds", 15],
			["30 seconds", 30],
			["1 minute", 60],
			["2 minutes", 120],
			["3 minutes", 180],
			["4 minutes", 240],
			["5 minutes", 300],
		],
	];
	arrSynthArgSpecs = [
		["in", 0, 0],
		["out", 0, 0],
		["bufnumDelay", 0, \ir],
		["delay", ControlSpec(10,1000, 'exp').unmap(1000), defLagTime],
		["delayMin", 10, defLagTime],
		["delayMax", 1000 * this.getMaxDelaytime, defLagTime],
		["wetDryMix", 1.0, defLagTime],
		["modDelay", 0, defLagTime],
		["modWetDryMix", 0, defLagTime],
		// N.B. arg below not used in synthdef, just kept here for convenience
		["autoTapTempo", 0, \ir],
	];
	synthDefFunc = {
		arg in, out, bufnumDelay, delay=0.1, delayMin, delayMax, wetDryMix,
			modDelay=0, modWetDryMix=0;
		var input, outSignal, delaytime, decaytime, mixCombined;
		input = In.kr(in,1);
		delaytime =( (delayMax/delayMin) ** ((delay + modDelay).max(0.0001).min(1)) )
			* delayMin / 1000;
		mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
		//	CombC.kr(in, maxdelaytime, delaytime, decaytime, mul, add)
		outSignal = BufDelayC.kr(bufnumDelay, input, delaytime, mixCombined,
			input * (1-mixCombined));
		Out.kr(out, TXClean.kr(outSignal));
	};
	this.buildGuiSpecArray;
	arrActionSpecs = this.buildActionSpecs(
		[["commandAction", "Tap Tempo", {this.actionTapTempo;}]]
		++ guiSpecArray);
	//	use base class initialise
	this.baseInit(this, argInstName);
	//	make buffers, load the synthdef and create the synth
	this.makeBuffersAndSynth(this.getArrBufferSpecs);
}

buildGuiSpecArray {
	var holdControlSpec;
	holdControlSpec = ControlSpec.new(10, 1000 * this.getMaxDelaytime, \exp );
	guiSpecArray = [
		["SynthOptionPopupPlusMinus", "Maximum time", arrOptionData, 0, 300,
			{this.buildGuiSpecArray;
			system.showViewIfModDisplay(this);
			this.makeBuffersAndSynth(this.getArrBufferSpecs);
			}
		],
		["SpacerLine", 6],
		["TapTempoButton", {arg argTempo; this.useTapTempo(argTempo);}],
		["Spacer", 10],
		["TXCheckBox", "Auto copy tap tempo to delay bpm ", "autoTapTempo", nil, 230],
		["SpacerLine", 6],
		["TextBarLeft", "Delay time shown in ms and bpm", 200],
		["Spacer", 3],
		["ActionButton", "time x 2", {this.delayTimeMultiply(2);}, 60],
		["ActionButton", "time x 3", {this.delayTimeMultiply(3);}, 60],
		["ActionButton", "time / 2", {this.delayTimeMultiply(0.5);}, 60],
		["ActionButton", "time / 3", {this.delayTimeMultiply(1/3);}, 60],
		["NextLine"],
		["TXTimeBpmMinMaxSldr", "Delay time", holdControlSpec, "delay", "delayMin", "delayMax"],
		["SpacerLine", 6],
		["WetDryMixSlider"],
	];
}

getMaxDelaytime {
	^arrOptionData.at(0).at(arrOptions.at(0)).at(1);
}

getArrBufferSpecs {
	arrBufferSpecs = [ ["bufnumDelay", defSampleRate * this.getMaxDelaytime, 1] ];
	^arrBufferSpecs;
}

delayTimeMultiply { arg argMultiplyValue;
	var currentTime, minTime, maxTime, holdControlSpec, newTime;
	minTime = this.getSynthArgSpec("delayMin");
	maxTime = this.getSynthArgSpec("delayMax");
	holdControlSpec = ControlSpec.new(minTime, maxTime, \exp);
	currentTime = holdControlSpec.map(this.getSynthArgSpec("delay"));
	newTime = currentTime * argMultiplyValue;
	if (argMultiplyValue < 1, {
		if ( newTime >= minTime, {
			this.setSynthValue("delay", holdControlSpec.unmap(newTime));
		});
	},{
		if ( newTime <= maxTime, {
			this.setSynthValue("delay", holdControlSpec.unmap(newTime));
		});
	});
	system.flagGuiIfModDisplay(this);
}

useTapTempo {arg argTempo;
	var holdDelay, autoBPM, minDelay, maxDelay;
	holdDelay = 60000/argTempo;
	autoBPM = this.getSynthArgSpec("autoTapTempo");
	minDelay = this.getSynthArgSpec("delayMin");
	maxDelay = this.getSynthArgSpec("delayMax");
	if (autoBPM == 1,{
		if ((holdDelay >= minDelay) and: (holdDelay <= maxDelay),{
			this.setSynthArgSpec("delay", ControlSpec(minDelay, maxDelay, \exp).unmap(holdDelay));
			system.flagGuiIfModDisplay(this);
		});
	});
}

actionTapTempo {	// tap tempo function used by module action
	var holdBPM;
	if (newTapTime.isNil, {
		newTapTime = Main.elapsedTime
	}, {
		holdTapTime = Main.elapsedTime;
		holdBPM = 60 / (holdTapTime - newTapTime);
		newTapTime = holdTapTime;
		this.useTapTempo(holdBPM);
	});
}

loadExtraData {arg argData;
	this.buildGuiSpecArray;
	system.showViewIfModDisplay(this);
	{this.makeBuffersAndSynth(this.getArrBufferSpecs);}.defer(2);
}

}

