// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXEQPara : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "EQ Para";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Frequency", 1, "modfreq", 0],
			["Smooth Time", 1, "modLag", 0],
			["Resonance", 1, "modres", 0],
			["Cut-Boost", 1, "modcutboost", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrFreqRangePresets = TXFilter.arrFreqRanges;
		classData.arrGainRangePresets = TXFilter.arrGainRanges;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["freq", 0.5, defLagTime],
			["freqMin",40, defLagTime],
			["freqMax", 20000, defLagTime],
			["lag", 0.5, defLagTime],
			["lagMin", 0.01, defLagTime],
			["lagMax", 1, defLagTime],
			["res", 0.5, defLagTime],
			["resMin", 0,  defLagTime],
			["resMax", 1, defLagTime],
			["cutboost", 0.25, defLagTime],
			["cutboostMin", -18,  defLagTime],
			["cutboostMax", 18, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modfreq", 0, defLagTime],
			["modLag", 0, defLagTime],
			["modres", 0, defLagTime],
			["modcutboost", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0, 0];
		arrOptionData = [
			[ // NOTE: option removed
				["EQ Parametric",
					// {arg inSound, inFreq, inRes, indb; MidEQ.ar(inSound, inFreq, inRes, indb); }  // bad
					{arg inSound, inFreq, inRes, indb; BPeakEQ.ar(inSound, inFreq, inRes, indb); }
				],
				["EQ Parametric Biquad",
					{arg inSound, inFreq, inRes, indb; BPeakEQ.ar(inSound, inFreq, inRes, indb); }
				],
			],
			[
				["None",
					{arg input, lagtime; input;}
				],
				["Linear",
					{arg input, lagtime; Ramp.kr(input, lagtime); }
				],
				["Exp 1",
					{arg input, lagtime; Lag.kr(input, lagtime); }
				],
				["Exp 2",
					{arg input, lagtime; Lag2.kr(input, lagtime); }
				],
				["Exp 3",
					{arg input, lagtime; Lag3.kr(input, lagtime); }
				],
			]
		];
		synthDefFunc = { arg in, out, freq, freqMin, freqMax, lag, lagMin, lagMax,
			res, resMin, resMax, cutboost, cutboostMin, cutboostMax, wetDryMix, modfreq = 0.0, modLag = 0.0,
			modres = 0.0, modcutboost = 0.0, modWetDryMix = 0.0;
			var input, outFunction, lagFunction, outFilter, outClean;
			var sumfreq, sumlag, sumres, sumcutboost, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(startEnv * InFeedback.ar(in, 1));
			sumfreq = ( (freqMax/ freqMin) ** ((freq + modfreq).max(0.001).min(1)) ) * freqMin;
			sumlag = ( (lagMax/lagMin) ** ((lag + modLag).max(0.001).min(1)) ) * lagMin;
			sumres =  resMin + ( (resMax - resMin) * (res + modres).max(0).min(1) );
			sumcutboost =  cutboostMin + ( (cutboostMax - cutboostMin) * (cutboost + modcutboost).max(0).min(1) );
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			lagFunction = arrOptionData.at(1).at(arrOptions.at(1)).at(1);
			outFilter = outFunction.value(
				input,
				lagFunction.value(sumfreq.min(20000), sumlag),
				sumres.max(0.1).reciprocal,
				sumcutboost
			);
			outFilter = LeakDC.ar(outFilter);
			Out.ar(out, (TXClean.ar(startEnv * outFilter) * mixCombined) + (input * (1-mixCombined)) );
		};
		guiSpecArray = [
			// option removed:
			//["SynthOptionPopupPlusMinus", "Filter", arrOptionData, 0],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Frequency", ControlSpec(0.midicps, 20000, \exponential),
				"freq", "freqMin", "freqMax", nil, classData.arrFreqRangePresets],
			["SpacerLine", 6],
			["SynthOptionPopupPlusMinus", "Smoothing", arrOptionData, 1],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Smooth time", ControlSpec(0.0001, 30, \exp, 0, 1, units: " secs"),
				"lag", "lagMin", "lagMax"],
			["DividingLine"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Resonance", ControlSpec(0.1, 1), "res", "resMin", "resMax"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Cut-Boost", ControlSpec(-48, 18), "cutboost", "cutboostMin", "cutboostMax",
				nil, classData.arrGainRangePresets],
			["DividingLine"],
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
