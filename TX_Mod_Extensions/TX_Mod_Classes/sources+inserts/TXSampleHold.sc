// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSampleHold : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Sample & Hold";
		classData.moduleRate = "control";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Trigger", 1, "extTrigger", nil]
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
			["t_userTrig", 0, 0],
			["extTrigger", 0, 0],
		];
		arrOptions = [0];
		arrOptionData = [
			[
				["Sample & Hold",
					{arg inSignal, inTrigger; Latch.kr(inSignal, inTrigger); }
				],
				["Gate & Hold",
					{arg inSignal, inTrigger; Gate.kr(inSignal, inTrigger); }
				],
			]
		];
		synthDefFunc = { arg in, out, t_userTrig=0, extTrigger=0;
			var inSignal, outFunction, outSignal;
			inSignal = TXClean.kr(In.kr(in,1));
			outFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			outSignal = outFunction.value(inSignal, t_userTrig + extTrigger);
			Out.kr(out, TXClean.kr(outSignal));
		};
		holdControlSpec = ControlSpec(0.0001, 30, \exp, 0, 1, units: " secs");
		guiSpecArray = [
			["SynthOptionPopup", "Type", arrOptionData, 0],
			["SpacerLine", 2],
			["ActionButton", "Trigger", {this.moduleNode.set("t_userTrig", 1);},
				200, TXColor.white, TXColor.sysGuiCol2]
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

