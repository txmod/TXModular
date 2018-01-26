// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMIDIVelocity2 : TXModuleBase {

	classvar <>classData;

	var	midiNoteOnResp;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Midi Velocity";
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
			["MIDINoteSelector"],
			["SpacerLine", 4],
			["MIDIVelSelector"],
			["DividingLine"],
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
		midiNoteOnResp = MIDIFunc.noteOn({  |val, num, chan, src|
			// set the Bus value
			if (	(outBus.class == Bus)
				and: (chan >= (midiMinChannel-1)) and: (chan <= (midiMaxChannel-1))
				and: (num >= midiMinNoteNo) and: (num <= midiMaxNoteNo)
				and: (val >= midiMinVel) and: (val <= midiMaxVel)
				, {
					outBus.value_(this.getSynthOption(0).value(val.linlin(midiMinVel, midiMaxVel, 0, 1)));
			});
		});
	}

	midiControlDeactivate {
		//	stop responding to midi.
		if (midiNoteOnResp.class == MIDIFunc, {
			midiNoteOnResp.free;
		});
}

	rebuildSynth {
		// override base class method
	}

	deleteModuleExtraActions {
		// override base class method
		this.midiControlDeactivate;
	}

	loadExtraData {arg argData, isPresetData = false;
		// override base class method
		this.midiControlActivate;
	}

}

