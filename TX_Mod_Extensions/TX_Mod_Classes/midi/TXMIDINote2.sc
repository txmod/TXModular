// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMIDINote2 : TXModuleBase {

	classvar <>classData;

	var	midiNoteOnResp, midiNoteOffResp;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Midi Note";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [];
		classData.noOutChannels = 3;
		classData.arrOutBusSpecs = [
			["Note value", [0]],
			["Note On trigger", [1]],
			["Note Off trigger", [2]],
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

	midiControlActivate { //	override base class
		//	stop any previous routine
		this.midiControlDeactivate;
		//	start routine
		midiNoteOnResp = MIDIFunc.noteOn({  |val, num, chan, src|
			var bundle;
			//	check whether to set bus
			if (	(outBus.class == Bus)
				and: (chan >= (midiMinChannel-1)) and: (chan <= (midiMaxChannel-1))
				and: (num >= midiMinNoteNo) and: (num <= midiMaxNoteNo)
				and: (val >= midiMinVel) and: (val <= midiMaxVel)
				, {
					// set the Bus values
					bundle = system.server.makeBundle(nil, {
						outBus.setAt(0, this.getSynthOption(0).value(num.linlin(midiMinNoteNo, midiMaxNoteNo, 0, 1)));
						outBus.setAt(1, 1);
					});
					system.server.makeBundle(0.05, {
						outBus.setAt(1, 0);
					},
					bundle
					);
			});
		});
		midiNoteOffResp = MIDIFunc.noteOff({  |val, num, chan, src|
			var bundle;
			//	check whether to set bus
			if (	(outBus.class == Bus)
				and: (chan >= (midiMinChannel-1)) and: (chan <= (midiMaxChannel-1))
				and: (num >= midiMinNoteNo) and: (num <= midiMaxNoteNo)
				and: (val >= midiMinVel) and: (val <= midiMaxVel)
				, {
					// set the Bus value
					bundle = system.server.makeBundle(nil, {
						outBus.setAt(2, 1);
					});
					system.server.makeBundle(0.05, {
						outBus.setAt(2, 0);
					},
					bundle
					);
			});
		});
	}

	midiControlDeactivate { //	override base class
		//	stop responding to midi.
		if (midiNoteOnResp.class == MIDIFunc, {
			midiNoteOnResp.free;
		});
		if (midiNoteOffResp.class == MIDIFunc, {
			midiNoteOffResp.free;
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

