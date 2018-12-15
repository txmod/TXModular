// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSplit2Band : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Split 2-Band";
		classData.moduleRate = "audio";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Split Freq", 1, "modSplitFreq", 0],
			["Output Level", 1, "modLevel", 0],
		];
		classData.arrAudSCInBusSpecs = [
			["Input", 1, "input"],
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Band 1", [0]],
			["Band 2", [1]],
		];
		classData.guiWidth = 600;
		classData.freqSpec = ControlSpec(5, 20000, \exp);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["input", 0, 0],
			["out", 0, 0],
			["splitFreq", classData.freqSpec.unmap(500), defLagTime],
			["splitFreqMin", 5, defLagTime],
			["splitFreqMax", 20000, defLagTime],
			["outLevel", 1, defLagTime],
			["modSplitFreq", 0, defLagTime],
			["modLevel", 0, defLagTime],
		];
		synthDefFunc = { arg input, out, splitFreq, splitFreqMin, splitFreqMax, outLevel, modSplitFreq=0, modLevel=0;

			var holdInput, holdLevel, holdFreq;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			holdLevel = (outLevel + modLevel).max(0).min(1);
			holdInput = InFeedback.ar(input, 2) * holdLevel;
			holdFreq = (splitFreq + modSplitFreq).max(0).min(1).linexp(0, 1, splitFreqMin, splitFreqMax);
			Out.ar(out, startEnv * TXBandSplitter2.ar(holdInput, holdFreq));
		};
		guiSpecArray = [
			["TXMinMaxSliderSplit", "Split Freq", classData.freqSpec, "splitFreq", "splitFreqMin", "splitFreqMax"],
			["SpacerLine", 10],
			["EZslider", "Output Level", ControlSpec(0, 1), "outLevel"],
		];
		arrActionSpecs = this.buildActionSpecs([
			["EZslider", "Output Level", ControlSpec(0, 1), "outLevel"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}


}

