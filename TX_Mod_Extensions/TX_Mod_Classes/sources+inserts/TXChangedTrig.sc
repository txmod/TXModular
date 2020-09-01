// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXChangedTrig : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Changed Trig";
		classData.moduleRate = "control";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Trigger", 1, "extTrigger", nil],
			["Threshold", 1, "modThreshold", 0],
			["Trig Time", 1, "modTrigTime", 0],
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
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["t_userTrig", 0, 0],
			["extTrigger", 0, 0],
			["threshold", 0, defLagTime],
			["thresholdMin", 0, defLagTime],
			["thresholdMax", 0.1, defLagTime],
			["trigTime", ControlSpec(0.01, 1, \exp).unmap(0.05), defLagTime],
			["trigTimeMin", 0.01, defLagTime],
			["trigTimeMax", 1, defLagTime],
			["modThreshold", 0, defLagTime],
			["modTrigTime", 0, defLagTime],
		];
		synthDefFunc = { arg in, out, t_userTrig, extTrigger,
			threshold, thresholdMin, thresholdMax, trigTime, trigTimeMin, trigTimeMax,
			modThreshold = 0, modTrigTime = 0;

			var inSignal, thresholdSum, trigTimeSum, changedTrig, outSignal;

			thresholdSum = (threshold + modThreshold).max(0).min(1)
				.linlin(0, 1, thresholdMin, thresholdMax);
			trigTimeSum = (trigTime + modTrigTime).max(0).min(1)
				.linlin(0, 1, trigTimeMin, trigTimeMax);

			inSignal = TXClean.kr(In.kr(in,1));

			changedTrig = Trig.kr(Changed.kr(inSignal, thresholdSum), trigTimeSum);

			outSignal = (t_userTrig + extTrigger + changedTrig);

			Out.kr(out, TXClean.kr(outSignal));
		};
		guiSpecArray = [
			["SpacerLine", 2],
			["ActionButton", "Trigger", {this.moduleNode.set("t_userTrig", 1);},
				100, TXColor.white, TXColor.sysGuiCol2],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Threshold", ControlSpec(0, 0.1), "threshold", "thresholdMin", "thresholdMax"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Trig Time", ControlSpec(0.01, 60, \exp), "trigTime", "trigTimeMin", "trigTimeMax"],
		];
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Trigger", {this.moduleNode.set("t_userTrig", 1);}],
		]
		++ guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

