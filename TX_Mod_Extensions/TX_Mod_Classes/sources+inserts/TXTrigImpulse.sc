// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXTrigImpulse : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var	envView;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Trigger Impulse";
		classData.moduleRate = "control";
		classData.moduleType = "groupsource";
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
		autoModOptions = false;
		arrSynthArgSpecs = [
			["out", 0, 0],
		];
		synthDefFunc = {
			arg out;
			var outEnv;
			//		outEnv = Line.kr(1, 1, 0.1, doneAction: 2);
			outEnv = EnvGen.kr(Env.adsr(0.01, 0.01, 0, 0.01), 0.01, doneAction: 2);
			Out.kr(out, outEnv);
		};
		guiSpecArray = [
			["MIDIListenCheckBox"],
			["SpacerLine", 4],
			["MIDIChannelSelector"],
			["SpacerLine", 4],
			["MIDINoteSelector"],
			["SpacerLine", 4],
			["MIDIVelSelector"],
			["SpacerLine", 4],
			["ActionButton", "Trigger", {this.createSynthNote(60, 100, 0.02);},
				200, TXColor.white, TXColor.sysGuiCol2],
		];
		arrActionSpecs = this.buildActionSpecs([
			["MIDIListenCheckBox"],
			["MIDIChannelSelector"],
			["MIDINoteSelector"],
			["MIDIVelSelector"],
			["commandAction", "Trigger", {this.createSynthNote(60, 100, 0.02);}],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.setMonophonic;	// monophonic by default
		this.midiNoteInit;
		//	load the synthdef and create the Group for synths to belong to
		this.loadDefAndMakeGroup;
	}

}

