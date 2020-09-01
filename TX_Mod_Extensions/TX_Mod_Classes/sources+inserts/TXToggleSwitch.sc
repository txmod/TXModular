// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXToggleSwitch : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Toggle Switch";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Trigger", 1, "modTrigger", nil]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var holdControlSpec;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["out", 0, 0],
			["t_userTrig", 0, 0],
			["modTrigger", 0, 0],
		];
		synthDefFunc = { arg out, t_userTrig=0, modTrigger=0;
			var outSignal = ToggleFF.kr(TXClean.kr(t_userTrig + modTrigger));
			Out.kr(out, TXClean.kr(outSignal));
		};
		guiSpecArray = [
			["ActionButton", "Trigger", {this.moduleNode.set("t_userTrig", 1);},
				200, TXColor.white, TXColor.sysGuiCol2]
		];
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Trigger", {this.moduleNode.set("t_userTrig", 1);}],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

