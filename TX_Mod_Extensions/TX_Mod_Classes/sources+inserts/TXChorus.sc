// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXChorus : TXModuleBase {		// Chorus module

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Chorus";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["LFO rate", 1, "modLFOFreq", 0],
			["LFO depth", 1, "modLFODepth", 0],
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
		var holdControlSpec, holdControlSpec2;
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["lfoFreq", 0.2, defLagTime],
			["lfoFreqMin", 0.01, defLagTime],
			["lfoFreqMax", 10, defLagTime],
			["lfoDepth", 0.1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modLFOFreq", 0, defLagTime],
			["modLfoDepth", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0, 2];
		arrOptionData = [
			TXLFO.arrOptionData,
			[
				["2 layers", 2],
				["3 layers", 3],
				["4 layers", 4],
				["5 layers", 5],
				["6 layers", 6],
				["7 layers", 7],
			],
		];
		synthDefFunc = {
			arg in, out, lfoFreq, lfoFreqMin, lfoFreqMax, lfoDepth, wetDryMix,
			modLFOFreq = 0, modLfoDepth = 0, modWetDryMix = 0;
			var outLfo, outLFOFreq, outLfoDepth, outLFOFunction;
			var input, numLayers, outSound, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			numLayers = this.getSynthOption(1);
			outLFOFreq = ( (lfoFreqMax/lfoFreqMin) ** ((lfoFreq + modLFOFreq).max(0.001).min(1)) ) * lfoFreqMin;
			outLfoDepth = (lfoDepth + modLfoDepth).max(0).min(1);
			// select function based on arrOptions
			outLFOFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			outLfo = {arg i;
				outLFOFunction.value(outLFOFreq * [1, 0.923, 1.087, 0.982, 1.021, 0.959, 1.047].at(i)).range(0, 1)
				* Lag.kr(outLfoDepth, 1)
			} ! numLayers;
			input = TXClean.ar(startEnv * InFeedback.ar(in,1));
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outSound = DelayC.ar(input, 0.4, [0.02, 0.04, 0.06, 0.08, 0.1] + (outLfo*0.3));
			outSound = Mix.new(outSound).madd(mixCombined * (0.9 ** numLayers), input * (1-mixCombined));
			Out.ar(out, TXClean.ar(startEnv * LeakDC.ar(outSound)));
		};
		holdControlSpec = ControlSpec.new(0.midicps, 127.midicps, \exp );
		holdControlSpec2 = ControlSpec.new(0.01, 1.0, \exp );
		guiSpecArray = [
			["SynthOptionPopupPlusMinus", "No. Layers", arrOptionData, 1, 300],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "LFO Wave", arrOptionData, 0],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "LFO Rate", ControlSpec(0.01, 100, \exp, 0, 1, units: " Hz"),
				"lfoFreq", "lfoFreqMin", "lfoFreqMax", nil, TXLFO.arrLFOFreqRanges],
			["SpacerLine", 4],
			["EZslider", "LFO Depth", ControlSpec(0, 1), "lfoDepth"],
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

