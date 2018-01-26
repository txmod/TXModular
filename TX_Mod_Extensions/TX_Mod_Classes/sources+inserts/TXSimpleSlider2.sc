// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSimpleSlider2 : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Simple Slider";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Value", 1, "modSliderVal", 0],
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
			["out", 0, 0],
			["sliderVal", 0, 0],
			["sliderValMin", 0, 0],
			["sliderValMax", 1, 0],
			["modSliderVal", 0, 0],
		];
		synthDefFunc = {
			arg out, sliderVal, sliderValMin, sliderValMax, modSliderVal=0;
			var sliderValCombined;
			sliderValCombined = sliderValMin + ((sliderValMax - sliderValMin) * (sliderVal + modSliderVal).max(0).min(1));
			Out.kr(out, sliderValCombined);
		};
		guiSpecArray = [
			["TXMinMaxSliderSplit", "Value", nil.asSpec, "sliderVal", "sliderValMin", "sliderValMax"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

