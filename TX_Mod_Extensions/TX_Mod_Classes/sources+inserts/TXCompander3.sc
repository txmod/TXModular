// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXCompander3 : TXModuleBase {		// Compander module

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Compander";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrAudSCInBusSpecs = [
			["Side-chain", 1, "sideChain"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Threshold", 1, "modThreshold", 0],
			["Compander Ratio", 1, "modCompressorRatio", 0],
			["Attack", 1, "modAttack", 0],
			["Release", 1, "modRelease", 0],
			["Output Gain", 1, "modOutGain", 0],
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
			["sideChain", 0, 0],
			["out", 0, 0],
			["threshold", 0.5, defLagTime],
			["expanderRatio", 0.09091, defLagTime],
			["expanderRatioMin", 0.1, defLagTime],
			["expanderRatioMax", 10, defLagTime],
			["compressorRatio", 0.09091, defLagTime],
			["compressorRatioMin", 0.1, defLagTime],
			["compressorRatioMax", 10, defLagTime],
			["attack", 0.1, defLagTime],
			["attackMin", 0.001, defLagTime],
			["attackMax", 0.1, defLagTime],
			["release", 0.1, defLagTime],
			["releaseMin", 0.01, defLagTime],
			["releaseMax", 0.3, defLagTime],
			["outGain", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modThreshold", 0, defLagTime],
			["modCompressorRatio", 0, defLagTime],
			["modAttack", 0, defLagTime],
			["modRelease", 0, defLagTime],
			["modOutGain", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0, 0];
		arrOptionData = [
			[ // 0 - sidechain
				["use main input signal to control compander", {
					arg input, sideChain; input;}],
				["use side-chain signal to control compander", {
					arg input, sideChain; InFeedback.ar(sideChain,1);}],
			],
			[ // 1 - Lookahead Delay
				["Off - No Lookahead Delay", {
					arg input; input;}],
				["Lookahead - Delay 3 ms", {
					arg input; DelayN.ar(input, 0.003, 0.003);}],
				["Lookahead - Delay 6 ms", {
					arg input; DelayN.ar(input, 0.006, 0.006);}],
				["Lookahead - Delay 9 ms", {
					arg input; DelayN.ar(input, 0.009, 0.009);}],
				["Lookahead - Delay 12 ms", {
					arg input; DelayN.ar(input, 0.012, 0.012);}],
				["Lookahead - Delay 18 ms", {
					arg input; DelayN.ar(input, 0.018, 0.018);}],
				["Lookahead - Delay 25 ms", {
					arg input; DelayN.ar(input, 0.025, 0.025);}],
				["Lookahead - Delay 33 ms", {
					arg input; DelayN.ar(input, 0.033, 0.033);}],
				["Lookahead - Delay 50 ms", {
					arg input; DelayN.ar(input, 0.05, 0.05);}],
				["Lookahead - Delay 75 ms", {
					arg input; DelayN.ar(input, 0.075, 0.075);}],
				["Lookahead - Delay 100 ms", {
					arg input; DelayN.ar(input, 0.1, 0.1);}],
			],
		];
		synthDefFunc = { arg in, sideChain, out, threshold, expanderRatio, expanderRatioMin, expanderRatioMax,
			compressorRatio, compressorRatioMin, compressorRatioMax, attack, attackMin, attackMax,
			release, releaseMin, releaseMax, outGain, wetDryMix,
			modThreshold = 0, modCompressorRatio = 0, modAttack = 0, modRelease = 0, modOutGain = 0, modWetDryMix=0;
			var input, inputDelayed, thresh, thresholdSum, attackSum, releaseSum, outGainSum;
			var compRatioSum, expRatio, controlSignal, mixCombined, outSound;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			compRatioSum = compressorRatioMin + ( (compressorRatioMax - compressorRatioMin)
				* (compressorRatio + modCompressorRatio).max(0).min(1));
			expRatio = expanderRatioMin + ( (expanderRatioMax - expanderRatioMin) * expanderRatio);
			input = startEnv * InFeedback.ar(in,1);
			thresholdSum = (threshold + modThreshold).max(0.001).min(1);
			attackSum = LinExp.kr((attack + modAttack).max(0).min(1), 0, 1, attackMin, attackMax);
			releaseSum = LinExp.kr((release + modRelease).max(0).min(1), 0, 1, releaseMin, releaseMax);
			outGainSum = (outGain + modOutGain).max(0).min(10);
			controlSignal = this.getSynthOption(0).value(input, sideChain);
			inputDelayed = this.getSynthOption(1).value(input);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outSound = Compander.ar(inputDelayed, startEnv * controlSignal, thresholdSum, expRatio, compRatioSum.reciprocal,
				attackSum, releaseSum, outGainSum);
			Out.ar(out, (startEnv * outSound * mixCombined) + (inputDelayed * (1-mixCombined)));
		};
		guiSpecArray = [
			["SynthOptionPopupPlusMinus", "Side-chain", arrOptionData, 0],
			["SpacerLine", 6],
			["SynthOptionPopupPlusMinus", "Lookahead", arrOptionData, 1],
			["SpacerLine", 6],
			["EZslider", "Threshold", ControlSpec(0.001, 1, \amp), "threshold"],
			["SpacerLine", 12],
			["TXPresetPopup", "Presets",
				TXCompPresets.arrCompPresets(this).collect({arg item, i; item.at(0)}),
				TXCompPresets.arrCompPresets(this).collect({arg item, i; item.at(1)})
			],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Expand ratio", ControlSpec(0.1, 10),
				"expanderRatio", "expanderRatioMin", "expanderRatioMax"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Comp. ratio", ControlSpec(0.1, 10),
				"compressorRatio", "compressorRatioMin", "compressorRatioMax"],
			["DividingLine"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Clamp time", ControlSpec(0.001, 1),
				"attack", "attackMin", "attackMax"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Relax time", ControlSpec(0.001, 1),
				"release", "releaseMin", "releaseMax"],
			["DividingLine"],
			["SpacerLine", 6],
			["EZslider", "Out gain", ControlSpec(0, 10), "outGain"],
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

