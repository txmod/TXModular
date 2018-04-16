// Copyright (C) 2018  James Harkins. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXTrig2Gate : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Trig to Gate";
		classData.moduleRate = "control";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Hold time", 1, "modHoldTime", 0]
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
			["in", 0, 0],
			["out", 0, 0],
			["holdTime", 0.1, defLagTime],
			["holdMin", 0.01, defLagTime],
			["holdMax", 30, defLagTime],
			["modHoldTime", 0, defLagTime]
		];
		arrOptions = [1];
		arrOptionData = [
			[ // Re-trig mode
				["Retrigger Off - won't retrigger when gate is on", {
					arg holdTime, inTrig;
					Trig1.kr(inTrig, holdTime);
				}],
				["Retrigger On - retriggers when gate is on", {
					arg holdTime, inTrig;
					EnvGen.kr(Env([0, 0, 1, 1, 0], [0, 0, holdTime, 0], \lin), inTrig);  // added stage to force start level 0
				}],
				["Slurrable - retrigger resets time but keeps gate open", {
					arg holdTime, inTrig;
					EnvGen.kr(Env([0, 1, 1, 0], [0, holdTime, 0], \lin), inTrig); // env doesn't retrigger
				}],
			],
		];
		synthDefFunc = { arg in, out, holdTime, holdMin, holdMax, modHoldTime = 0;
			var inTrig, gateFunc, outGate;
			inTrig = TXClean.kr(In.kr(in, 1));
			holdTime = (holdTime + modHoldTime).linexp(0.01, 1.0, holdMin, holdMax, \minmax);
			gateFunc = this.getSynthOption(0);
			outGate = gateFunc.value(holdTime, inTrig);
			Out.kr(out, TXClean.kr(outGate));
		};
		holdControlSpec = ControlSpec(0.01, 30, \exp, 0, 1, units: " secs");
		guiSpecArray = [
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Retrig mode", arrOptionData, 0, 450],
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "Hold time", holdControlSpec, "holdTime", "holdMin", "holdMax"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}
