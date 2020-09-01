// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXNoiseGate : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Noise Gate";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrAudSCInBusSpecs = [
			["Side-chain", 1, "sideChain"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Threshold", 1, "modThreshold", 0],
			["Hysteresis", 1, "modHysteresis", 0],
			["Reduction", 1, "modReduction", 0],
			["Open Time", 1, "modOpenTime", 0],
			["Hold Time", 1, "modHoldTime", 0],
			["Close Time", 1, "modCloseTime", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.thresholdSpec = ControlSpec(-100, 0, \db);
		classData.hysteresisSpec = ControlSpec(0, 6, \db);
		classData.reductionSpec = ControlSpec(-100, 0, \db);
		classData.timeSpec = ControlSpec(1, 10000);
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
			["threshold", classData.thresholdSpec.unmap(-40), defLagTime],
			["thresholdMin", -100, defLagTime],
			["thresholdMax", 0, defLagTime],
			["hysteresis", classData.hysteresisSpec.unmap(3), defLagTime],
			["hysteresisMin", 0, defLagTime],
			["hysteresisMax", 6, defLagTime],
			["reduction", classData.reductionSpec.unmap(-100), defLagTime],
			["reductionMin", -100, defLagTime],
			["reductionMax", 0, defLagTime],
			["openTime", 0, defLagTime],
			["openTimeMin", 1, defLagTime],
			["openTimeMax", 500, defLagTime],
			["holdTime", ControlSpec(1, 500).unmap(40), defLagTime],
			["holdTimeMin", 1, defLagTime],
			["holdTimeMax", 500, defLagTime],
			["closeTime", ControlSpec(1, 4000).unmap(100), defLagTime],
			["closeTimeMin", 1, defLagTime],
			["closeTimeMax", 4000, defLagTime],
			["loFreq", 20, defLagTime],
			["hiFreq", 20000, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modThreshold", 0, defLagTime],
			["modHysteresis", 0, defLagTime],
			["modReduction", 0, defLagTime],
			["modOpenTime", 0, defLagTime],
			["modHoldTime", 0, defLagTime],
			["modCloseTime", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];

		arrOptions = [0, 0, 0, 0, 0];
		arrOptionData = [
			[ // 0 - side chain
				["use Main input", {
					arg input, sideChain; input;}],
				["use Side-chain", {
					arg input, sideChain; InFeedback.ar(sideChain,1) ! 2;}],
			],
			[ // 1 - bracket eq
				["Off  (no EQ on Control signal)", { arg input, loFreq, hiFreq; input;}],
				["12db Low-Cut & Hi-Cut - on Control signal", { arg input, loFreq, hiFreq;
					var chain = HPF.ar(input, loFreq);
					chain = LPF.ar(chain, hiFreq);
				}],
				["24db Low-Cut & Hi-Cut - on Control signal", { arg input, loFreq, hiFreq;
					var chain = HPF.ar(HPF.ar(input, loFreq), loFreq);
					chain = LPF.ar(LPF.ar(chain, hiFreq), hiFreq);
				}],
				["12db Low-Cut Only - on Control signal", { arg input, loFreq, hiFreq;
					HPF.ar(input, loFreq);
				}],
				["24db Low-Cut Only - on Control signal", { arg input, loFreq, hiFreq;
					HPF.ar(HPF.ar(input, loFreq), loFreq);
				}],
				["12db High-Cut Only - on Control signal", { arg input, loFreq, hiFreq;
					LPF.ar(input, hiFreq);
				}],
				["24db High-Cut Only - on Control signal", { arg input, loFreq, hiFreq;
					LPF.ar(LPF.ar(input, hiFreq), hiFreq);
				}],
			],
			[ // 2 - Lookahead Delay
				// NOTE: item[2] is delay time
				["Off", {
					arg input; input;}, 0.0],
				["3 ms delay", {
					arg input; DelayN.ar(input, 0.003, 0.003);}, 0.003],
				["6 ms delay", {
					arg input; DelayN.ar(input, 0.006, 0.006);}, 0.006],
				["9 ms delay", {
					arg input; DelayN.ar(input, 0.009, 0.009);}, 0.009],
				["12 ms delay", {
					arg input; DelayN.ar(input, 0.012, 0.012);}, 0.012],
				["18 ms delay", {
					arg input; DelayN.ar(input, 0.018, 0.018);}, 0.018],
				["25 ms delay", {
					arg input; DelayN.ar(input, 0.025, 0.025);}, 0.025],
				["33 ms delay", {
					arg input; DelayN.ar(input, 0.033, 0.033);}, 0.033],
				["50 ms delay", {
					arg input; DelayN.ar(input, 0.05, 0.05);}, 0.05],
				["75 ms delay", {
					arg input; DelayN.ar(input, 0.075, 0.075);}, 0.075],
				["100 ms delay", {
					arg input; DelayN.ar(input, 0.1, 0.1);}, 0.1],
			],
			[ // 3 - Play Mode
				["Gate", 'gate'],
				["Ducker", 'ducker'],
			],
			[ // 4 - Monitor Control Signal
				["Off", false],
				["On", true],
			],
		];
		synthDefFunc = {
			arg in, sideChain, out, threshold, thresholdMin, thresholdMax,
			hysteresis, hysteresisMin, hysteresisMax, reduction, reductionMin, reductionMax,
			openTime, openTimeMin, openTimeMax, holdTime, holdTimeMin, holdTimeMax,
			closeTime, closeTimeMin, closeTimeMax,
			loFreq, hiFreq, wetDryMix,
			modThreshold=0, modHysteresis=0, modReduction = 0,
			modOpenTime = 0, modHoldTime = 0, modCloseTime = 0, modWetDryMix=0;

			var input, inputDelayed, delayTime, controlSignal, controlSignalEQ, outSound;
			var openTimeSum, holdTimeSum, closeTimeSum, thresholdSum, hysteresisSum, reductionSum, mixSum;
			var compFunc, onThreshold, offThreshold, controlAmp, onTrig, offTrig, outGate, outGateHold;
			var dbMapFunc, monitorControl, playMode, offLevel, onLevel, time1, time2;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			dbMapFunc = {arg inVal, minVal, maxVal;
				var minValAmp = minVal.dbamp;
				(inVal.squared * (maxVal.dbamp - minValAmp) + minValAmp).ampdb;
			};
			openTimeSum = LinLin.kr((openTime + modOpenTime).max(0).min(1), 0, 1, openTimeMin, openTimeMax) * 0.001;
			holdTimeSum = LinLin.kr((holdTime + modHoldTime).max(0).min(1), 0, 1, holdTimeMin, holdTimeMax) * 0.001;
			closeTimeSum = LinLin.kr((closeTime + modCloseTime).max(0).min(1), 0, 1, closeTimeMin, closeTimeMax) * 0.001;
			thresholdSum = dbMapFunc.value((threshold + modThreshold).max(0).min(1), thresholdMin, thresholdMax);
			hysteresisSum = dbMapFunc.value((hysteresis + modHysteresis).max(0).min(1), hysteresisMin, hysteresisMax);
			reductionSum = dbMapFunc.value((reduction + modReduction).max(0).min(1), reductionMin, reductionMax);

			input = InFeedback.ar(in,1);
			inputDelayed = this.getSynthOption(2).value(input);
			delayTime = arrOptionData[2][arrOptions[2]][2];
			controlSignal = this.getSynthOption(0).value(input, sideChain);
			controlSignalEQ = this.getSynthOption(1).value(controlSignal, loFreq, hiFreq); // bracket eq
			monitorControl = this.getSynthOption(4);
			if (monitorControl == true, {
				outSound = controlSignalEQ;
			},{
				controlAmp = A2K.kr(Amplitude.ar(controlSignalEQ, 0.01, 0.01));
				onThreshold = (thresholdSum + hysteresisSum).dbamp;
				offThreshold = thresholdSum.dbamp;
				offTrig = Trig1.kr(controlAmp < offThreshold, 0.01);
				onTrig = Trig1.kr(controlAmp >= onThreshold, 0.01);
				outGate = SetResetFF.kr(onTrig, offTrig);
				outGateHold = (outGate +
					EnvGen.kr(Env([0,1,1,0], [0.01,1,1,0.01]), onTrig, 1, 0, delayTime + holdTimeSum)).min(1);
				playMode = this.getSynthOption(3);
				if (playMode == 'gate', {
					offLevel = reductionSum.dbamp;
					onLevel = 1;
					time1 = openTimeSum;
					time2 = closeTimeSum;
				},{
					// 'ducker'
					offLevel = 1;
					onLevel = reductionSum.dbamp;
					time1 = closeTimeSum;
					time2 = openTimeSum;
				});
				outSound = inputDelayed * LagUD.kr(outGateHold.linlin(0, 1, offLevel, onLevel), time1, time2);
			});
			mixSum = (wetDryMix + modWetDryMix).max(0).min(1);
			Out.ar(out, startEnv * ((outSound * mixSum) + (inputDelayed * (1-mixSum))));
		};
		guiSpecArray = [
			["SynthOptionPopupPlusMinus", "Control signal", arrOptionData, 0, 250],
			["Spacer", 4],
			["SynthOptionCheckBox", "Monitor control signal + eq", arrOptionData, 4, 186],
			["SpacerLine", 1],
			["SynthOptionPopupPlusMinus", "Bracket EQ", arrOptionData, 1],
			["NextLine"],
			["TXRangeSlider", "EQ range", ControlSpec(20, 20000, \exp), "loFreq", "hiFreq"],
			["DividingLine"],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Play mode", arrOptionData, 3, 210],
			["Spacer", 4],
			["SynthOptionPopupPlusMinus", "Lookahead", arrOptionData, 2, 230],
			["SpacerLine", 1],
			["TXMinMaxSliderSplit", "Threshold db", classData.thresholdSpec, "threshold", "thresholdMin", "thresholdMax"],
			["SpacerLine", 1],
			["TXMinMaxSliderSplit", "Hysteresis db", classData.hysteresisSpec, "hysteresis", "hysteresisMin", "hysteresisMax"],
			["SpacerLine", 1],
			["TXMinMaxSliderSplit", "Reduction db", classData.reductionSpec, "reduction", "reductionMin", "reductionMax"],
			["DividingLine"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Open Time ms", classData.timeSpec, "openTime", "openTimeMin", "openTimeMax"],
			["SpacerLine", 1],
			["TXMinMaxSliderSplit", "Hold Time ms", classData.timeSpec, "holdTime", "holdTimeMin", "holdTimeMax"],
			["SpacerLine", 1],
			["TXMinMaxSliderSplit", "Close Time ms", classData.timeSpec, "closeTime", "closeTimeMin", "closeTimeMax"],
			["DividingLine"],
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

