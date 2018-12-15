// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

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
		classData.arrOutputRanges = [
			["Range presets: ", [0, 1]],
			["Full range -1 to 1", [-1, 1]],
			["Half range -0.5 to 0.5", [-0.5, 0.5]],
			["Positive range 0 to 1", [0, 1]],
			["Negative range -1 to 0", [-1, 0]],
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["out", 0, 0],
			["sliderVal", 0, defLagTime],
			["sliderValMin", 0, defLagTime],
			["sliderValMax", 1, defLagTime],
			["modSliderVal", 0, defLagTime],
		];
		synthDefFunc = {
			arg out, sliderVal, sliderValMin, sliderValMax, modSliderVal=0;
			var sliderValCombined;
			sliderValCombined = sliderValMin + ((sliderValMax - sliderValMin) * (sliderVal + modSliderVal).max(0).min(1));
			Out.kr(out, sliderValCombined);
		};
		guiSpecArray = [
			["TXMinMaxSliderSplit", "Value", ControlSpec.new(-1, 1), "sliderVal", "sliderValMin", "sliderValMax", nil, classData.arrOutputRanges],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

