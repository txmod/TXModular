// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSmooth2 : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Smooth";
		classData.moduleRate = "control";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Time 1", 1, "modLag", 0],
			["Time 2", 1, "modLag2", 0]
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
			["lag", 0.5, defLagTime],
			["lagMin", 0.01, defLagTime],
			["lagMax", 1, defLagTime],
			["lag2", 0.5, defLagTime],
			["lag2Min", 0.01, defLagTime],
			["lag2Max", 1, defLagTime],
			["modLag", 0, defLagTime],
			["modLag2", 0, defLagTime],
		];
		arrOptions = [1];
		arrOptionData = [
			TXSmoothFunctions.arrOptionData,
		];
		synthDefFunc = { arg in, out, lag, lagMin, lagMax, lag2, lag2Min, lag2Max, modLag=0, modLag2=0;
			var inSignal, lagtime, lagtime2, outFunction, outSignal;
			inSignal = TXClean.kr(In.kr(in,1));
			lagtime = ( (lagMax/lagMin) ** ((lag + modLag).max(0.01).min(1)) ) * lagMin;
			lagtime2 = ( (lag2Max/lag2Min) ** ((lag2 + modLag2).max(0.01).min(1)) ) * lag2Min;
			outFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			outSignal = outFunction.value(inSignal, lagtime, lagtime2).clip(-1, 1); // clip for safety
			Out.kr(out, TXClean.kr(outSignal));
		};
		holdControlSpec = ControlSpec(0.01, 30, \exp, 0, 1, units: " secs");
		guiSpecArray = [
			["SynthOptionPopupPlusMinus", "Smoothing", arrOptionData, 0],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Time 1", holdControlSpec, "lag", "lagMin", "lagMax"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Time 2", holdControlSpec, "lag2", "lag2Min", "lag2Max"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

