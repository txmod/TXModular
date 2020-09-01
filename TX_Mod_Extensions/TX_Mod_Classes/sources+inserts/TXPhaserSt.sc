// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXPhaserSt : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Phaser St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Phase Time", 1, "modDelay", 0],
			["Feedback", 1, "modFeedback", 0],
			["LFO rate", 1, "modFreq", 0],
			["LFO depth", 1, "modLFODepth", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.maxDelaytime = 0.5;	//	delay time up to 0.5 secs.
		classData.arrBufferSpecs = [
			["bufnumDelayL", defSampleRate * classData.maxDelaytime * 0.5, 1],
			["bufnumDelayR", defSampleRate * classData.maxDelaytime * 0.5, 1],
			["bufnumDelayL2", defSampleRate * classData.maxDelaytime, 1],
			["bufnumDelayR2", defSampleRate * classData.maxDelaytime, 1]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	*arrLFOFreqRanges {
		^ [
			["Presets: ", [0.01, 100]],
			["Full range 0.01 - 100 hz", [0.01, 100]],
			["Slow range 0.01 - 1 hz", [0.01, 1]],
			["Medium range 0.1 - 10 hz", [0.1, 10]],
			["Fast range 1 - 100 hz", [1, 100]],
		];
	}

	init {arg argInstName;
		var holdControlSpec, holdControlSpec2, arrTimeRanges;
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["bufnumDelayL", 0, \ir],
			["bufnumDelayR", 0, \ir],
			["bufnumDelayL2", 0, \ir],
			["bufnumDelayR2", 0, \ir],
			["delay", 0.66, defLagTime],
			["delayMin", 1, defLagTime],
			["delayMax", 100, defLagTime],
			["feedback", 0, defLagTime],
			["feedbackMin", 0.01, defLagTime],
			["feedbackMax", 1.0, defLagTime],
			["freq", 0.2, defLagTime],
			["freqMin", 0.01, defLagTime],
			["freqMax", 100, defLagTime],
			["lfoDepth", 0.1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modDelay", 0, defLagTime],
			["modFeedback", 0, defLagTime],
			["modFreq", 0, defLagTime],
			["modLfoDepth", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [2];
		arrOptionData = [TXLFO.arrOptionData];
		synthDefFunc = {
			arg in, out, bufnumDelayL, bufnumDelayR, bufnumDelayL2, bufnumDelayR2, delay, delayMin, delayMax,
			feedback, feedbackMin, feedbackMax, freq, freqMin, freqMax, lfoDepth, wetDryMix,
			modDelay = 0, modFeedback = 0, modFreq = 0, modLfoDepth = 0, modWetDryMix = 0;
			var outLfo, outFreq, outLfoDepth, outFunction;
			var input, inputLDelayed, inputRDelayed, outSound, delaytime, delaytimeMod, feedbackVal, decaytime, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			outFreq = ( (freqMax/freqMin) ** ((freq + modFreq).max(0.001).min(1)) ) * freqMin;
			outLfoDepth = (lfoDepth + modLfoDepth).max(0).min(1);
			// select function based on arrOptions
			outFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			outLfo = outFunction.value(outFreq) * outLfoDepth;
			delaytime = ( (delayMax/delayMin) ** ((delay + modDelay).max(0.0001).min(1)) ) * delayMin / 1000;
			delaytime = Lag.kr(delaytime, 0.1);
			delaytimeMod = delaytime + (outLfo * delaytime * 0.5);
			feedbackVal = feedbackMin + ( (feedbackMax-feedbackMin) * (feedback + modFeedback).max(0).min(1) );
			decaytime = 0.1 + (delaytime * (1 + (128 * feedbackVal)) );
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			input = TXClean.ar(InFeedback.ar(in,2));
			inputLDelayed = BufDelayC.ar(bufnumDelayL, input[0], delaytime * 0.5);
			inputRDelayed = BufDelayC.ar(bufnumDelayR, input[1], delaytime * 0.5);
			outSound = BufAllpassC.ar([bufnumDelayL2, bufnumDelayR2], input, delaytimeMod, decaytime,
				mixCombined, [inputLDelayed, inputRDelayed] * (1-mixCombined));
			Out.ar(out, startEnv * TXClean.ar(LeakDC.ar(outSound, 0.995)));
		};
		holdControlSpec = ControlSpec.new(0.1, 100, \exp );
		holdControlSpec2 = ControlSpec.new(0.01, 1.0, \exp );
		arrTimeRanges = [
			["Presets: ", [0.1, 100]],
			["Full range 0.1 - 100 ms", [0.1, 100]],
			["Low range 0.1 - 10 ms", [0.1, 10]],
			["Medium range 1 - 30 ms", [1, 30]],
			["High range 10 - 100 ms", [10, 100]],
		];
		guiSpecArray = [
			["TXMinMaxSliderSplit", "Phase time", holdControlSpec,"delay", "delayMin", "delayMax", nil, arrTimeRanges],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Feedback", holdControlSpec2, "feedback", "feedbackMin", "feedbackMax"],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Waveform", arrOptionData, 0],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "LFO rate", ControlSpec(0.01, 100, \exp, 0, 1, units: " Hz"),
				"freq", "freqMin", "freqMax", nil, TXLFO.arrLFOFreqRanges],
			["SpacerLine", 4],
			["EZslider", "LFO depth", ControlSpec(0, 1), "lfoDepth"],
			["SpacerLine", 4],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make buffers, load the synthdef and create the synth
		this.makeBuffersAndSynth(classData.arrBufferSpecs);
	}

}

