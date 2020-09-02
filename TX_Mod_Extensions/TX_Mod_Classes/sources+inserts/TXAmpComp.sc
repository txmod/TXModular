// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXAmpComp  : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Amp Comp";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Freq", 1, "modFreq", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	*arrOptionData {
		^[ // amp compensation
			["Off",
				{arg freq; 1;}
			],
			["A-weighted amp compensation, minAmp: 0.75",
				{arg freq; AmpCompA.kr(freq.max(60), 60, 0.5);}
			],
			["A-weighted amp compensation, minAmp: 0.6",
				{arg freq; AmpCompA.kr(freq.max(60), 60, 0.6);}
			],
			["A-weighted amp compensation, minAmp: 0.5",
				{arg freq; AmpCompA.kr(freq.max(60), 60, 0.5);}
			],
			["A-weighted amp compensation, minAmp: 0.4",
				{arg freq; AmpCompA.kr(freq.max(60), 60, 0.5);}
			],
			["A-weighted amp compensation, minAmp: 0.32 (default)",
				{arg freq; AmpCompA.kr(freq.max(60), 60, 0.6);}
			],
			["A-weighted amp compensation, minAmp: 0.25",
				{arg freq; AmpCompA.kr(freq.max(60), 60, 0.22);}
			],
			["Amp compensation, exponent: 0.1",
				{arg freq; AmpComp.kr(freq.max(60), 60, 0.1);}
			],
			["Amp compensation, exponent: 0.2",
				{arg freq; AmpComp.kr(freq.max(60), 60, 0.2);}
			],
			["Amp compensation, exponent: 0.3333 (default)",
				{arg freq; AmpComp.kr(freq.max(60), 60, 0.3333);}
			],
			["Amp compensation, exponent: 0.4",
				{arg freq; AmpComp.kr(freq.max(60), 60, 0.4);}
			],
			["Amp compensation, exponent: 0.5",
				{arg freq; AmpComp.kr(freq.max(60), 60, 0.5);}
			],
		];
	}

	*arrFreqRanges {
		^ [
			["Presets: ", [40, 127.midicps]],
			["MIDI Note Range 8.17 - 12543 hz", [0.midicps, 127.midicps]],
			["Full Audible range 40 - 20k hz", [40, 20000]],
			["Wide range 40 - 8k hz", [40, 8000]],
			["Low range 40 - 250 hz", [40, 250]],
			["Mid range 100 - 2k hz", [100, 2000]],
			["High range 1k - 6k hz", [1000, 6000]],
		];
	}

	init {arg argInstName, arrPresets;
		//	set  class specific instance variables
		arrOptions = [5];
		arrOptionData = [
			// amp compensation
			TXAmpComp.arrOptionData,
		];
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["freq", 0.5, defLagTime],
			["freqMin", 0.midicps, defLagTime],
			["freqMax", 127.midicps, defLagTime],
			["modFreq", 0, defLagTime],
		];
		synthDefFunc = { arg in, out, freq, freqMin, freqMax, modFreq=0;
			var input, outFreqSum, ampCompFunction, outAmpComp;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in, 1);
			outFreqSum = (freq + modFreq).max(0).min(1).linexp(0, 1, freqMin, freqMax);
			ampCompFunction = this.getSynthOption(0);
			outAmpComp = ampCompFunction.value(outFreqSum);
			Out.ar(out, startEnv * input * outAmpComp);
		};
		guiSpecArray = [
			["SynthOptionListPlusMinus", "Amp Comp", arrOptionData, 0, nil, 150],
			["SpacerLine", 6],
			["TXMinMaxFreqNoteSldr", "Freq", ControlSpec(0.midicps, 20000, \exponential),
				"freq", "freqMin", "freqMax", nil, TXAmpComp.arrFreqRanges],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

	