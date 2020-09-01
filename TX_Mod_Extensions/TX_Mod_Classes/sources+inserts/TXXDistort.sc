// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXXDistort : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "X Distort";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["X Amp", 1, "modXAmp", 0],
			["Smoothing", 1, "modSmoothing", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
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
			["xamp", 0.5, defLagTime],
			["xampMin", 0, defLagTime],
			["xampMax", 1.0, defLagTime],
			["smoothing", 0.5, defLagTime],
			["smoothingMin", 0, defLagTime],
			["smoothingMax", 1.0, defLagTime],
			["outGain", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modXAmp", 0, defLagTime],
			["modSmoothing", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];

		synthDefFunc = {
			arg in, out, xamp, xampMin, xampMax, smoothing, smoothingMin, smoothingMax,
			outGain, wetDryMix,
			modXAmp=0, modSmoothing=0, modWetDryMix=0;

			var input, outSound, xampVal, smoothingVal, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in,1);

			xampVal = xampMin + ( (xampMax-xampMin) * (xamp + modXAmp).max(0).min(1) );
			smoothingVal = smoothingMin + ( (smoothingMax-smoothingMin)
				* (smoothing + modSmoothing).max(-1).min(1) );

			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);

			outSound = outGain * CrossoverDistortion.ar(input, xampVal, smoothingVal);

			outSound = LeakDC.ar(outSound, 0.995);

			Out.ar(out, startEnv * ((outSound * mixCombined) + (input * (1-mixCombined))));
		};
		guiSpecArray = [
			["TXMinMaxSliderSplit", "X Amp", ControlSpec.new(0.0, 1.0, \lin ),
				"xamp", "xampMin", "xampMax"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Smoothing", ControlSpec.new(0.0, 1.0, \lin ),
				"smoothing", "smoothingMin", "smoothingMax"],
			["SpacerLine", 4],
			["EZslider", "Out Gain", ControlSpec(0, 1), "outGain"],
			["SpacerLine", 4],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

