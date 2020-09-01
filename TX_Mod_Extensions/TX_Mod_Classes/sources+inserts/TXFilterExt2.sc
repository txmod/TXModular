// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFilterExt2 : TXModuleBase {		// Filter module

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Filter";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Frequency", 1, "modfreq", 0],
			["Smooth Time", 1, "modLag", 0],
			["Resonance", 1, "modres", 0],
			["Saturation", 1, "modsat", 0],
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
			["freq", 0.5, defLagTime],
			["freqMin",40, defLagTime],
			["freqMax", 20000, defLagTime],
			["lag", 0.5, defLagTime],
			["lagMin", 0.01, defLagTime],
			["lagMax", 1, defLagTime],
			["res", 0.5, defLagTime],
			["resMin", 0,  defLagTime],
			["resMax", 1, defLagTime],
			["sat", 0.5, defLagTime],
			["satMin", 0,  defLagTime],
			["satMax", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modfreq", 0, defLagTime],
			["modLag", 0, defLagTime],
			["modres", 0, defLagTime],
			["modsat", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0, 0, 0];
		arrOptionData = [
			TXFilter.arrOptionData,
			[
				["None",
					{arg input, lagtime; input;}
				],
				["Linear",
					{arg input, lagtime; Ramp.kr(input, lagtime); }
				],
				["Exponential 1",
					{arg input, lagtime; Lag.kr(input, lagtime); }
				],
				["Exponential 2",
					{arg input, lagtime; Lag2.kr(input, lagtime); }
				],
				["Exponential 3",
					{arg input, lagtime; Lag3.kr(input, lagtime); }
				],
			],
			TXLevelControl.arrOptionData,
		];
		synthDefFunc = { arg in, out, freq, freqMin, freqMax, lag, lagMin, lagMax,
			res, resMin, resMax, sat, satMin, satMax, wetDryMix, modfreq = 0.0, modLag = 0.0,
			modres = 0.0, modsat = 0.0, modWetDryMix = 0.0;
			var input, filterFunction, lagFunction, outFilter, outClean, sumfreq, sumlag, sumres,
			sumsat, levelControlFunc, inputDelayed, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(startEnv * InFeedback.ar(in,1));
			sumfreq = ( (freqMax/ freqMin) ** ((freq + modfreq).max(0.001).min(1)) ) * freqMin;
			sumlag = ( (lagMax/lagMin) ** ((lag + modLag).max(0.001).min(1)) ) * lagMin;
			sumres =  resMin + ( (resMax - resMin) * (res + modres).max(0).min(1) );
			sumsat =  satMin + ( (satMax - satMin) * (sat + modsat).max(0).min(1) );
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			filterFunction = this.getSynthOption(0);
			lagFunction = this.getSynthOption(1);
			levelControlFunc = this.getSynthOption(2);
			inputDelayed = TXLevelControl.arrDelayFunctions[arrOptions[2]].value(input);
			outFilter = TXClean.ar(filterFunction.value(
				input,
				lagFunction.value(sumfreq, sumlag).max(30),  // limit min freq to 30
				sumres,
				sumsat
			));
			Out.ar(out, (startEnv * levelControlFunc.value(outFilter, input) * mixCombined) + (inputDelayed * (1-mixCombined)) );
		};
		guiSpecArray = [
			["SynthOptionListPlusMinus", "Filter", arrOptionData, 0, nil, 120],
			["SpacerLine", 6],
			["TXMinMaxFreqNoteSldr", "Frequency", ControlSpec(0.midicps, 20000, \exponential),
				"freq", "freqMin", "freqMax", nil, TXFilter.arrFreqRanges],
			["SpacerLine", 6],
			["SynthOptionPopupPlusMinus", "Smoothing", arrOptionData, 1],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Smooth time", ControlSpec(0.0001, 30, \exp, 0, 1, units: " secs"),
				"lag", "lagMin", "lagMax"],
			["DividingLine"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Resonance", ControlSpec(0, 1), "res", "resMin", "resMax"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Saturation", ControlSpec(0, 1), "sat", "satMin", "satMax"],
			["DividingLine"],
			["SpacerLine", 6],
			["SynthOptionPopupPlusMinus", "Level control", arrOptionData, 2],
			["SpacerLine", 6],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	loadExtraData {arg argData;
		// override default method
		// for older systems, set level control function to tanh
		if (system.dataBank.savedSystemRevision < 1001, {
			fork {
				system.server.sync;
				arrOptions.put(2, 4);
				this.rebuildSynth;
			}
		});
	}

}
