// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXOnOffSwitch : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "On Off Switch";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Trigger On", 1, "modOnTrig", nil],
			["Trigger Off", 1, "modOffTrig", nil],
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
			["t_userOnTrig", 0, 0],
			["modOnTrig", 0, 0],
			["t_userOffTrig", 0, 0],
			["modOffTrig", 0, 0],
		];
		synthDefFunc = { arg out, t_userOnTrig=0, modOnTrig=0, t_userOffTrig=0, modOffTrig=0;
			var outSignal = SetResetFF.kr(TXClean.kr(t_userOnTrig + modOnTrig), TXClean.kr(t_userOffTrig + modOffTrig));
			Out.kr(out, TXClean.kr(outSignal));
		};
		guiSpecArray = [
			["ActionButton", "Trigger On", {this.moduleNode.set("t_userOnTrig", 1);},
				200, TXColor.white, TXColor.sysGuiCol2],
			["Spacer", 36],
			["ActionButton", "Trigger Off", {this.moduleNode.set("t_userOffTrig", 1);},
				200, TXColor.white, TXColor.sysGuiCol2],
		];
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Trigger On", {this.moduleNode.set("t_userOnTrig", 1);}],
			["commandAction", "Trigger Off", {this.moduleNode.set("t_userOffTrig", 1);}],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

