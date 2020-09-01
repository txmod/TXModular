// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXPerlinNoise : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Perlin Noise";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Rate 1", 1, "modFreq1", 0],
			["Rate 2", 1, "modFreq2", 0],
			["Rate 3", 1, "modFreq3", 0],
			["Rate Multiply", 1, "modFreqMultiply", 0],
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
		var freqControlSpec, freqRangePresets;
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		freqControlSpec = ControlSpec(0.01, 100, \exp, 0, 1, units: " Hz");
		arrSynthArgSpecs = [
			["out", 0, 0],
			["freq1", 0.5, defLagTime],
			["freq1Min", 0.1, defLagTime],
			["freq1Max", 30, defLagTime],
			["freq2", 0.5, defLagTime],
			["freq2Min", 0.1, defLagTime],
			["freq2Max", 30, defLagTime],
			["freq3", 0.5, defLagTime],
			["freq3Min", 0.1, defLagTime],
			["freq3Max", 30, defLagTime],
			["freqMultiply", freqControlSpec.unmap(1), defLagTime],
			["freqMultiplyMin", 0.1, defLagTime],
			["freqMultiplyMax", 10, defLagTime],
			["modFreq1", 0, defLagTime],
			["modFreq2", 0, defLagTime],
			["modFreq3", 0, defLagTime],
			["modFreqMultiply", 0, defLagTime],
		];
		arrOptions = [0];
		arrOptionData = [
			[
				["Positive & Negative: -1 to 1, centered on 0", {arg input; input.range(-1, 1)}],
				["Positive & Negative: -0.5 to 0.5, centered on 0", {arg input; input.range(-0.5, 0.5)}],
				["Positive only: 0 to 1, centered on 0.5", {arg input; input.range(0, 1)}],
				["Positive only: 0 to 1, using absolute values", {arg input; input.abs}],
			],
		];
		synthDefFunc = { arg out, freq1, freq1Min, freq1Max, freq2, freq2Min, freq2Max,
			freq3, freq3Min, freq3Max,
			freqMultiply, freqMultiplyMin, freqMultiplyMax,
			modFreq1=0, modFreq2=0, modFreq3=0, modFreqMultiply=0;
			var outFreq1, outFreq2, outFreq3, outFreqMultiply, outPerlin3, rangeFunction, outSignal;
			var outLFTri1, outLFTri2, outLFTri3;

			outFreqMultiply = LinExp.kr(freqMultiply, 0, 1, freqMultiplyMin, freqMultiplyMax);
			outFreq1 = outFreqMultiply * LinExp.kr(freq1, 0, 1, freq1Min, freq1Max);
			outFreq2 = outFreqMultiply * LinExp.kr(freq2, 0, 1, freq2Min, freq2Max);
			outFreq3 = outFreqMultiply * LinExp.kr(freq3, 0, 1, freq3Min, freq3Max);
			outLFTri1 = TXClean.kr(LFTri.kr(outFreq1/8000000, 0, 1000000));
			outLFTri2 = TXClean.kr(LFTri.kr(outFreq2/8000000, 0, 1000000));
			outLFTri3 = TXClean.kr(LFTri.kr(outFreq3/8000000, 0, 1000000));

			outPerlin3 = Perlin3.kr(outLFTri1,outLFTri2, outLFTri3);

			rangeFunction = this.getSynthOption(0);
			outSignal = rangeFunction.value(outPerlin3);
			Out.kr(out, TXClean.kr(outSignal));
		};
		freqRangePresets = 	[
			["Presets: ", [0.01, 100]],
			["Full range 0.001 - 100 hz", [0.001, 100]],
			["Very Slow range 0.001 - 0.1 hz", [0.001, 0.1]],
			["Slow range 0.01 - 1 hz", [0.01, 1]],
			["Medium range 0.1 - 30 hz", [0.1, 30]],
			["Fast range 1 - 100 hz", [1, 100]],
		];
		guiSpecArray = [
			["TXMinMaxSliderSplit", "Rate 1", freqControlSpec, "freq1", "freq1Min", "freq1Max",
				nil, freqRangePresets],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Rate 2", freqControlSpec, "freq2", "freq2Min", "freq2Max",
				nil, freqRangePresets],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Rate 3", freqControlSpec, "freq3", "freq3Min", "freq3Max",
				nil, freqRangePresets],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Rate Multiply", freqControlSpec, "freqMultiply", "freqMultiplyMin", "freqMultiplyMax",
				nil, freqRangePresets],
			["SpacerLine", 6],
			["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 0],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

