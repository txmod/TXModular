// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMIDIController2 : TXModuleBase {

	classvar <>classData;

	var	midiControlResp;
	var	midiLearnResp;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Midi Controller";
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
						this.midiLearnDeactivate;
					},{
						this.midiLearnActivate;
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

	midiLearnActivate {
		this.midiLearnDeactivate;
		midiLearnResp = MIDIFunc.cc({arg val, num, chan, src;
			// stop responder
			this.midiLearnDeactivate;
			//  set min/max channel and controller no
			midiMinChannel = chan + 1;
			midiMaxChannel = chan + 1;
			midiMinControlNo = num;
			midiMaxControlNo = num;
			this.setSynthArgSpec("midiLearn", 0);
			this.midiControlActivate;
			// update view
			system.showViewIfModDisplay(this);
		});
	}

	midiLearnDeactivate {
		if (midiLearnResp.class == MIDIFunc, {
			midiLearnResp.free;
		});
	}

	midiControlActivate {
		//	stop any previous routine
		this.midiControlDeactivate;
		midiControlResp = MIDIFunc.cc({arg val, num, chan, src;

			// TESTING XXX
			// "=== TXMIDIController2: midiControlActivate === ".postln;
			// ["val, num, chan: ", val, num, chan].postln;

			// set the Bus value
			if ((outBus.class == Bus)
				and: {(chan >= (midiMinChannel-1)) and: (chan <= (midiMaxChannel-1))}
				and: {num == midiMinControlNo}
				, {
					outBus.value_(this.getSynthOption(0).value(val/127););
			});
		});
	}

	midiControlDeactivate {
		if (midiControlResp.class == MIDIFunc, {
			midiControlResp.free;
		});
	}

	rebuildSynth {
		// override base class method
	}

	loadExtraData {arg argData;  // override default method
		// override value
		this.setSynthArgSpec("midiLearn", 0);
	}

	deleteModuleExtraActions {
		// override base class method
		this.midiLearnDeactivate;
		this.midiControlDeactivate;
	}

}

