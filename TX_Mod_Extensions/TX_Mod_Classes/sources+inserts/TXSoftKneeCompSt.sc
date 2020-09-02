// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSoftKneeCompSt : TXModuleBase {

	// ====== From SoftKneeCompressor Help file:======
	//
	// SoftKneeCompressor.ar( in, control ? in, thresh, ratio, knee, attack, release, rms)
	//
	// thresh and knee args are in dB
	// knee: 0-xx knee range; amount of dB's under *and* above thresh; 0 means no knee
	// makeUp: 0-1; make up gain - 0 means no gain, 1 means max gain
	// rms: 0-xx amount of rms samples (runs in peak mode if 0 (default) )
	// ratio: dB ratio above thresh; 1 (default means no compression)

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "SoftKnee Comp St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrAudSCInBusSpecs = [
			["Side-chain", 1, "sideChain"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Threshold", 1, "modThreshold", 0],
			["Knee", 1, "modKnee", 0],
			["Compressor Ratio", 1, "modCompRatio", 0],
			["Attack", 1, "modAttack", 0],
			["Release", 1, "modRelease", 0],
			["Make-Up Gain", 1, "modMakeUp", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.kneeSpec = ControlSpec(0, 48);
		classData.compRatioSpec = ControlSpec(0.1, 10);
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
			["knee", ControlSpec(0, 24).unmap(6), defLagTime],
			["kneeMin", 0, defLagTime],
			["kneeMax", 24, defLagTime],
			["compRatio", ControlSpec(1, 10).unmap(4), defLagTime],
			["compRatioMin", 1, defLagTime],
			["compRatioMax", 10, defLagTime],
			["attack", 0.01, defLagTime],
			["attackMin", 0.001, defLagTime],
			["attackMax", 0.1, defLagTime],
			["release", 0.03, defLagTime],
			["releaseMin", 0.001, defLagTime],
			["releaseMax", 0.1, defLagTime],
			["makeUp", 0, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modThreshold", 0, defLagTime],
			["modKnee", 0, defLagTime],
			["modCompRatio", 0, defLagTime],
			["modAttack", 0, defLagTime],
			["modRelease", 0, defLagTime],
			["modMakeUp", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0, 0, 0, 0];
		arrOptionData = [
			[ // 0 - side chain
				["use main input signal to control compressor", {
					arg input, sideChain; input;}],
				["use side-chain signal to control compressor", {
					arg input, sideChain; InFeedback.ar(sideChain,1) ! 2;}],
			],
			[ // 1 - compression type
				["Peak Compression (default)", {
					arg in, control, thresh, ratio, knee, attack, release, makeUp, rms;
					SoftKneeCompressor.peak(in, control, thresh, ratio, knee, attack, release, makeUp);
				}],
				["RMS Compression - averaging time: 12 ms", {
					arg in, control, thresh, ratio, knee, attack, release, makeUp;
					SoftKneeCompressor.rms(in, control, thresh, ratio, knee, attack, release, makeUp,
						(0.012 * system.server.sampleRate).asInteger);
				}],
				["RMS Compression - averaging time: 25 ms", {
					arg in, control, thresh, ratio, knee, attack, release, makeUp;
					SoftKneeCompressor.rms(in, control, thresh, ratio, knee, attack, release, makeUp,
						(0.025 * system.server.sampleRate).asInteger);
				}],
				["RMS Compression - averaging time: 50 ms (default)", {
					arg in, control, thresh, ratio, knee, attack, release, makeUp;
					SoftKneeCompressor.rms(in, control, thresh, ratio, knee, attack, release, makeUp,
						(0.050 * system.server.sampleRate).asInteger);
				}],
				["RMS Compression - averaging time: 100 ms", {
					arg in, control, thresh, ratio, knee, attack, release, makeUp;
					SoftKneeCompressor.rms(in, control, thresh, ratio, knee, attack, release, makeUp,
						(0.1 * system.server.sampleRate).asInteger);
				}],
				["RMS Compression - averaging time: 250 ms", {
					arg in, control, thresh, ratio, knee, attack, release, makeUp;
					SoftKneeCompressor.rms(in, control, thresh, ratio, knee, attack, release, makeUp,
						(0.25 * system.server.sampleRate).asInteger);
				}],
				["RMS Compression - averaging time: 500 ms", {
					arg in, control, thresh, ratio, knee, attack, release, makeUp;
					SoftKneeCompressor.rms(in, control, thresh, ratio, knee, attack, release, makeUp,
						(0.5 * system.server.sampleRate).asInteger);
				}],
				["RMS Compression - averaging time: 1000 ms", {
					arg in, control, thresh, ratio, knee, attack, release, makeUp;
					SoftKneeCompressor.rms(in, control, thresh, ratio, knee, attack, release, makeUp,
						(system.server.sampleRate).asInteger);
				}],
			],
			[ // 2 - Channel link
				//["Linked Left & Right channels", {arg input; Mix.ar(input)}],
				["Linked Left & Right channels", {arg input; max (abs(input[0]), abs(input[1]))}], // better
				["Independent Left & Right channels", {arg input; input}],
			],
			[ //3 - Lookahead Delay
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
		synthDefFunc = { arg in, sideChain, out, threshold, knee, kneeMin, kneeMax,
			compRatio, compRatioMin, compRatioMax, attack, attackMin, attackMax,
			release, releaseMin, releaseMax, makeUp, wetDryMix,
			modThreshold = 0, modKnee = 0, modCompRatio = 0, modAttack = 0, modRelease = 0,
			modMakeUp = 0, modWetDryMix=0;

			var input, inputDelayed, controlInput, thresholdSum, makeUpSum, compRatioSum, kneeSum, attackSum, releaseSum;
			var compFunc, controlSignal, mixCombined, outSound;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			compRatioSum = compRatioMin + ( (compRatioMax - compRatioMin)
				* (compRatio + modCompRatio).max(0).min(1));
			kneeSum = kneeMin + ( (kneeMax - kneeMin) * (knee + modKnee).max(0).min(1));
			attackSum = LinExp.kr((attack + modAttack).max(0).min(1), 0, 1, attackMin.max(0.001), attackMax.max(0.001));
			releaseSum = LinExp.kr((release + modRelease).max(0).min(1), 0, 1, releaseMin.max(0.001), releaseMax.max(0.001));
			input = startEnv * InFeedback.ar(in,2);
			controlInput = this.getSynthOption(2).value(input);
			thresholdSum = (threshold + modThreshold).max(0).min(1);
			makeUpSum = (makeUp + modMakeUp).max(0).min(1);
			controlSignal = this.getSynthOption(0).value(controlInput, sideChain);
			compFunc =  this.getSynthOption(1);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			inputDelayed = this.getSynthOption(3).value(input);
			outSound = compFunc.value(inputDelayed, startEnv * controlSignal, thresholdSum.squared.ampdb, compRatioSum.reciprocal, kneeSum,
				attackSum, releaseSum, makeUpSum);
			Out.ar(out, startEnv * ((outSound * mixCombined) + (inputDelayed * (1-mixCombined))));
		};
		guiSpecArray = [
			["SynthOptionPopupPlusMinus", "Channel link", arrOptionData, 2],
			["SpacerLine", 2],
			["SynthOptionPopupPlusMinus", "Side-chain", arrOptionData, 0],
			["SpacerLine", 2],
			["SynthOptionPopupPlusMinus", "Compression", arrOptionData, 1],
			["SpacerLine", 2],
			["SynthOptionPopupPlusMinus", "Lookahead", arrOptionData, 3],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 6],
			["EZsliderUnmapped", "Threshold", classData.thresholdSpec, "threshold"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Knee db", classData.kneeSpec,
				"knee", "kneeMin", "kneeMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Comp ratio", classData.compRatioSpec,
				"compRatio", "compRatioMin", "compRatioMax"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Clamp time", ControlSpec(0.001, 1),
				"attack", "attackMin", "attackMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Relax time", ControlSpec(0.001, 1),
				"release", "releaseMin", "releaseMax"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 6],
			["EZsliderUnmapped", "Make-up gain", ControlSpec(0, 1), "makeUp"],
			["SpacerLine", 2],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

