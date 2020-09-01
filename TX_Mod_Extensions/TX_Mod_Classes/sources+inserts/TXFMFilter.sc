// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFMFilter : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "FM Filter";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrAudSCInBusSpecs = [
			["FM Input", 1, "sideChain"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Centre frequency", 1, "modFreq", 0],
			["Ring time ms", 1, "modRingTime", 0],
			["FM depth", 1, "modFMDepth", 0],
			["FM freq", 1, "modFMFreq", 0],
			["FM ratio", 1, "modFMRatio", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.freqSpec = ControlSpec(0.midicps, 20000, \exp);
		classData.timeSpec = ControlSpec(0.01, 200, \exp);
		classData.fmFreqSpec = ControlSpec(0.01, 20000, \exp);
		classData.fmRatioSpec = ControlSpec(0.01, 10);
		classData.fmDepthSpec = ControlSpec(0, 8);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var controlSpec;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["sideChain", 0, 0],
			["out", 0, 0],
			["freq", 0.5, defLagTime],
			["freqMin", 0.midicps, defLagTime],
			["freqMax", 127.midicps, defLagTime],
			["ringTime", ControlSpec(0.1, 20, 'exp').unmap(5), defLagTime],
			["ringTimeMin", 0.1, defLagTime],
			["ringTimeMax", 20, defLagTime],
			["fmDepth", ControlSpec(0, 8).unmap(1), defLagTime],
			["fmDepthMin", 0, defLagTime],
			["fmDepthMax", 8, defLagTime],
			["fmFreq", 0.5, defLagTime],
			["fmFreqMin", 0.midicps, defLagTime],
			["fmFreqMax", 127.midicps, defLagTime],
			["fmRatio", ControlSpec(0.01, 10).unmap(1), defLagTime],
			["fmRatioMin", 0.01, defLagTime],
			["fmRatioMax", 10, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["outGain", 1.0, defLagTime],
			["modFreq", 0, defLagTime],
			["modRingTime", 0, defLagTime],
			["modFMDepth", 0, defLagTime],
			["modFMFreq", 0, defLagTime],
			["modFMRatio", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0, ];
		arrOptionData = [
			// ind 0 Frequency Modulation
			[
				["Internal - use sine wave, FM freq, FM depth",
					{arg sideChain, fmDepth, fmDepthMin, fmDepthMax, modFMDepth, fmFreq,
						fmFreqMin, fmFreqMax,  modFMFreq, fmRatio, fmRatioMin, fmRatioMax, modFMRatio, baseFreq;
						var fmDepthSum = (fmDepth + modFMDepth).linlin(0, 1, fmDepthMin, fmDepthMax, 'minmax');
						var fmFreqSum = (fmFreq + modFMFreq).linexp(0, 1, fmFreqMin, fmFreqMax, 'minmax');
						var fmWave = SinOsc.ar(fmFreqSum);
						2 ** (4 * fmDepthSum * fmWave);
					}
				],
				["Internal - use sine wave, [FM ratio x Centre freq], FM depth",
					{arg sideChain, fmDepth, fmDepthMin, fmDepthMax, modFMDepth, fmFreq,
						fmFreqMin, fmFreqMax,  modFMFreq, fmRatio, fmRatioMin, fmRatioMax, modFMRatio, baseFreq;
						var fmDepthSum = (fmDepth + modFMDepth).linlin(0, 1, fmDepthMin, fmDepthMax, 'minmax');
						var fmRatioSum = (fmRatio + modFMRatio).linlin(0, 1, fmRatioMin, fmRatioMax, 'minmax');
						var fmWave = SinOsc.ar(fmRatioSum * baseFreq);
						2 ** (4 * fmDepthSum * fmWave);
					}
				],
				["External - use FM audio input, FM depth",
					{arg sideChain, fmDepth, fmDepthMin, fmDepthMax, modFMDepth;
						var fmDepthSum = (fmDepth + modFMDepth).linlin(0, 1, fmDepthMin, fmDepthMax, 'minmax');
						2 ** (4 * fmDepthSum * TXClean.ar(InFeedback.ar(sideChain, 1)));
					}
				],
			],
		];

		synthDefFunc = {
			arg in, sideChain, out, freq, freqMin, freqMax, ringTime, ringTimeMin, ringTimeMax,
			fmDepth, fmDepthMin, fmDepthMax, fmFreq, fmFreqMin, fmFreqMax, fmRatio, fmRatioMin, fmRatioMax,
			wetDryMix, outGain,
			modFreq=0, modRingTime=0, modFMDepth=0, modFMFreq=0, modFMRatio=0, modWetDryMix=0;

			var input, freqSum, ringTimeSum, fmDepthSum, freqModFunc, fmOut, outSound, mixCombined, modulatedFreq;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(InFeedback.ar(in, 1));
			freqSum = (freq + modFreq).linexp(0, 1, freqMin, freqMax, 'minmax');
			ringTimeSum = (ringTime + modRingTime).linexp(0, 1, ringTimeMin, ringTimeMax, 'minmax');
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);

			freqModFunc = this.getSynthOption(0);
			fmOut = freqModFunc.value(sideChain, fmDepth, fmDepthMin, fmDepthMax, modFMDepth, fmFreq, fmFreqMin,
				fmFreqMax,  modFMFreq, fmRatio, fmRatioMin, fmRatioMax, modFMRatio, freqSum);
			modulatedFreq = freqSum * fmOut;
			outSound = ComplexRes.ar(input, modulatedFreq, ringTimeSum * 0.001) * outGain;
			outSound = (outSound  * mixCombined) + (input * (1-mixCombined));
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		guiSpecArray = [
			["TXMinMaxFreqNoteSldr", "Centre Freq", classData.freqSpec,
				"freq", "freqMin", "freqMax", nil, TXFreqRangePresets.arrFreqRanges],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Ring time ms", classData.timeSpec,
				"ringTime", "ringTimeMin", "ringTimeMax", nil, [
					["Range presets: ", [0.1, 20]],
					["Default Range 0.1 - 20 ms", [0.1, 20]],
					["Full range 0.01 - 200 ms", [0.01, 200]],
					["Low Range 0.01 - 5 ms", [0.01, 5]],
					["High Range 1 - 200 ms", [1, 200]],
				]
			],
			["DividingLine"],
			["SpacerLine", 6],
			["SynthOptionListPlusMinus", "Freq modulation", arrOptionData, 0],
			["SpacerLine", 6],
			["TXMinMaxFreqNoteSldr", "FM freq", classData.fmFreqSpec,
				"fmFreq", "fmFreqMin", "fmFreqMax", nil, TXFreqRangePresets.arrFreqRanges],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "FM ratio", classData.fmRatioSpec,
				"fmRatio", "fmRatioMin", "fmRatioMax",],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "FM depth", classData.fmDepthSpec,
				"fmDepth", "fmDepthMin", "fmDepthMax",],
			["DividingLine"],
			["SpacerLine", 6],
			["EZslider", "Out gain", ControlSpec(0, 3, 'lin'), "outGain"],
			["SpacerLine", 6],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

