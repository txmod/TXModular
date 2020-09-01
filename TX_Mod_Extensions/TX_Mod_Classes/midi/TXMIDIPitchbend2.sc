// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMIDIPitchbend2 : TXModuleBase {

	classvar <>classData;

	var	midiControlResp;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Midi Pitchbend";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
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
		midiControlResp = MIDIFunc.bend({  |val, chan, src|
			// set the Bus value
			if (	(outBus.class == Bus)
				and: (chan >= (midiMinChannel-1)) and: (chan <= (midiMaxChannel-1))
				, {
					outBus.value_(this.getSynthOption(0).value((val - 8192) / 16384) );
			});
		});
	}

	midiControlDeactivate {
		//	stop responding to midi.
		if (midiControlResp.class == MIDIFunc, {
			midiControlResp.free;
		});
	}

	rebuildSynth {
		// override base class method
	}

	deleteModuleExtraActions {
		// override base class method
		this.midiControlDeactivate;
	}

}

