// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXCompanderMBSt : TXModuleBase {		// MultiBand Compander module

	classvar <>classData;
	var	displayOption;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "CompanderMB St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrAudSCInBusSpecs = [
			["Side-chain", 1, "sideChain"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Threshold 1", 1, "modThreshold1", 0],
			["Threshold 2", 1, "modThreshold2", 0],
			["Threshold 3", 1, "modThreshold3", 0],
			["Threshold 4", 1, "modThreshold4", 0],
			["Expand ratio 1", 1, "modExpandRatio1", 0],
			["Expand ratio 2", 1, "modExpandRatio2", 0],
			["Expand ratio 3", 1, "modExpandRatio3", 0],
			["Expand ratio 4", 1, "modExpandRatio4", 0],
			["Compress ratio 1", 1, "modCompRatio1", 0],
			["Compress ratio 2", 1, "modCompRatio2", 0],
			["Compress ratio 3", 1, "modCompRatio3", 0],
			["Compress ratio 4", 1, "modCompRatio4", 0],
			["Attack 1", 1, "modAttack1", 0],
			["Attack 2", 1, "modAttack2", 0],
			["Attack 3", 1, "modAttack3", 0],
			["Attack 4", 1, "modAttack4", 0],
			["Release 1", 1, "modRelease1", 0],
			["Release 2", 1, "modRelease2", 0],
			["Release 3", 1, "modRelease3", 0],
			["Release 4", 1, "modRelease4", 0],
			["Dry-Wet Mix 1", 1, "modWetDryMix1", 0],
			["Dry-Wet Mix 2", 1, "modWetDryMix2", 0],
			["Dry-Wet Mix 3", 1, "modWetDryMix3", 0],
			["Dry-Wet Mix 4", 1, "modWetDryMix4", 0],
			["Band Level 1", 1, "modBandLevel1", 0],
			["Band Level 2", 1, "modBandLevel2", 0],
			["Band Level 3", 1, "modBandLevel3", 0],
			["Band Level 4", 1, "modBandLevel4", 0],
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.ratioSpec = ControlSpec(0.1, 10);
		classData.bandLevelSpec = ControlSpec(0, 2);
		classData.freqSpec = ControlSpec(40, 20000, \exp);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var compressFunc;
		//	set  class specific instance variables
		displayOption = "showAllBands";
		arrSynthArgSpecs = [
			["in", 0, 0],
			["sideChain", 0, 0],
			["out", 0, 0],
			["splitFreq1", 250, defLagTime],
			["splitFreq2", 2000, defLagTime],
			["splitFreq3", 6000, defLagTime],
			["expandRatioMin", 1, defLagTime],
			["expandRatioMax", 10, defLagTime],
			["compRatioMin", 1, defLagTime],
			["compRatioMax", 10, defLagTime],
			["attackMax", 0.1, defLagTime],
			["releaseMax", 0.1, defLagTime],
			["threshold1", 0.5, defLagTime],
			["threshold2", 0.5, defLagTime],
			["threshold3", 0.5, defLagTime],
			["threshold4", 0.5, defLagTime],
			["expandRatio1", ControlSpec(1, 10).unmap(1), defLagTime],
			["expandRatio2", ControlSpec(1, 10).unmap(1), defLagTime],
			["expandRatio3", ControlSpec(1, 10).unmap(1), defLagTime],
			["expandRatio4", ControlSpec(1, 10).unmap(1), defLagTime],
			["compRatio1", ControlSpec(1, 10).unmap(4), defLagTime],
			["compRatio2", ControlSpec(1, 10).unmap(4), defLagTime],
			["compRatio3", ControlSpec(1, 10).unmap(4), defLagTime],
			["compRatio4", ControlSpec(1, 10).unmap(4), defLagTime],
			["attack1", 0.01, defLagTime],
			["attack2", 0.01, defLagTime],
			["attack3", 0.01, defLagTime],
			["attack4", 0.01, defLagTime],
			["release1", 0.03, defLagTime],
			["release2", 0.03, defLagTime],
			["release3", 0.03, defLagTime],
			["release4", 0.03, defLagTime],
			["makeUpGain1", 0, defLagTime],
			["makeUpGain2", 0, defLagTime],
			["makeUpGain3", 0, defLagTime],
			["makeUpGain4", 0, defLagTime],
			["wetDryMix1", 1.0, defLagTime],
			["wetDryMix2", 1.0, defLagTime],
			["wetDryMix3", 1.0, defLagTime],
			["wetDryMix4", 1.0, defLagTime],
			["bandLevel1", 0.5, defLagTime],
			["bandLevel2", 0.5, defLagTime],
			["bandLevel3", 0.5, defLagTime],
			["bandLevel4", 0.5, defLagTime],
			["modThreshold1", 0, defLagTime],
			["modThreshold2", 0, defLagTime],
			["modThreshold3", 0, defLagTime],
			["modThreshold4", 0, defLagTime],
			["modExpandRatio1", 0, defLagTime],
			["modExpandRatio2", 0, defLagTime],
			["modExpandRatio3", 0, defLagTime],
			["modExpandRatio4", 0, defLagTime],
			["modCompRatio1", 0, defLagTime],
			["modCompRatio2", 0, defLagTime],
			["modCompRatio3", 0, defLagTime],
			["modCompRatio4", 0, defLagTime],
			["modAttack1", 0, defLagTime],
			["modAttack2", 0, defLagTime],
			["modAttack3", 0, defLagTime],
			["modAttack4", 0, defLagTime],
			["modRelease1", 0, defLagTime],
			["modRelease2", 0, defLagTime],
			["modRelease3", 0, defLagTime],
			["modRelease4", 0, defLagTime],
			["modWetDryMix1", 0, defLagTime],
			["modWetDryMix2", 0, defLagTime],
			["modWetDryMix3", 0, defLagTime],
			["modWetDryMix4", 0, defLagTime],
			["modBandLevel1", 0, defLagTime],
			["modBandLevel2", 0, defLagTime],
			["modBandLevel3", 0, defLagTime],
			["modBandLevel4", 0, defLagTime],
		];
		arrOptions = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
		arrOptionData = [
			[ // 0 - Band 1 side chain
				["use main input signal to control compander", {
					arg input, sideChain; input;}],
				["use side-chain signal to control compander", {
					arg input, sideChain; sideChain ! 2;}],
			],
			[ // 1 - Band 2 side chain
				["use main input signal to control compander", {
					arg input, sideChain; input;}],
				["use side-chain signal to control compander", {
					arg input, sideChain; sideChain ! 2;}],
			],
			[ // 2 - Band 3 side chain
				["use main input signal to control compander", {
					arg input, sideChain; input;}],
				["use side-chain signal to control compander", {
					arg input, sideChain; sideChain ! 2;}],
			],
			[ // 3 - Band 4 side chain
				["use main input signal to control compander", {
					arg input, sideChain; input;}],
				["use side-chain signal to control compander", {
					arg input, sideChain; sideChain ! 2;}],
			],
			[ //4 - Lookahead Delay
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
			[ // 5 - Compander 1 active
				["Compander Off", 0],
				["Compander On", 1],
			],
			[ // 6 - Compander 2 active
				["Compander Off", 0],
				["Compander On", 1],
			],
			[ // 7 - Compander 3 active
				["Compander Off", 0],
				["Compander On", 1],
			],
			[ // 8 - Compander 4 active
				["Compander Off", 0],
				["Compander On", 1],
			],
			[ // 9 - S-chain mode
				["Full range side chain signal", 0],
				["Band split side chain signal", 1],
			],
			[ // 10 - Channel link
				//["Linked Left & Right channels", {arg input; Mix.ar(input)}],
				["Linked Left & Right channels", {arg input; max (abs(input[0]), abs(input[1]))}], // better
				["Independent Left & Right channels", {arg input; input}],
			],
		];
		compressFunc = {arg bandIndex, input, sideChainSig, expandRatioMin, expandRatioMax,
			compRatioMin, compRatioMax, attackMax, releaseMax,
			threshold, expandRatio,  compRatio, attack,  release,  makeUpGain,  wetDryMix,
			modThreshold, modExpandRatio, modCompRatio, modAttack, modRelease, modWetDryMix;

			var inputDelayed, controlInput, thresholdSum, compRatioSum, expRatioSum, attackSum, releaseSum;
			var compFunc, controlSignal, mixSum, outSound;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			expRatioSum = expandRatioMin + ( (expandRatioMax - expandRatioMin)
				* (expandRatio + modExpandRatio).max(0).min(1));
			compRatioSum = compRatioMin + ( (compRatioMax - compRatioMin)
				* (compRatio + modCompRatio).max(0).min(1));
			attackSum = LinExp.kr((attack + modAttack).max(0).min(1), 0, 1, 0.001, attackMax.max(0.001));
			releaseSum = LinExp.kr((release + modRelease).max(0).min(1), 0, 1, 0.001, releaseMax.max(0.001));
			controlInput = this.getSynthOption(10).value(input);
			thresholdSum = (threshold + modThreshold).max(0).min(1);
			controlSignal = this.getSynthOption(bandIndex).value(controlInput, sideChainSig);
			mixSum = (wetDryMix + modWetDryMix).max(0).min(1);
			inputDelayed = this.getSynthOption(4).value(input);
			outSound = Compander.ar(inputDelayed, startEnv * controlSignal, thresholdSum, expRatioSum, compRatioSum.reciprocal,
				attackSum, releaseSum, 1 + makeUpGain);
			(outSound * mixSum) + (inputDelayed * (1-mixSum));
		};
		synthDefFunc = { arg in, sideChain, out, splitFreq1, splitFreq2, splitFreq3,
			expandRatioMin, expandRatioMax, compRatioMin, compRatioMax, attackMax, releaseMax,
			threshold1, threshold2, threshold3, threshold4,
			expandRatio1, expandRatio2, expandRatio3, expandRatio4, compRatio1, compRatio2, compRatio3, compRatio4,
			attack1, attack2, attack3, attack4, release1, release2, release3, release4,
			makeUpGain1, makeUpGain2, makeUpGain3, makeUpGain4,
			wetDryMix1, wetDryMix2, wetDryMix3, wetDryMix4,
			bandLevel1, bandLevel2, bandLevel3, bandLevel4,
			modThreshold1 = 0, modThreshold2 = 0, modThreshold3 = 0, modThreshold4 = 0,
			modExpandRatio1 = 0, modExpandRatio2 = 0, modExpandRatio3 = 0, modExpandRatio4 = 0,
			modCompRatio1 = 0, modCompRatio2 = 0, modCompRatio3 = 0, modCompRatio4 = 0,
			modAttack1 = 0, modAttack2 = 0, modAttack3 = 0, modAttack4 = 0,
			modRelease1 = 0, modRelease2 = 0, modRelease3 = 0, modRelease4 = 0,
			modWetDryMix1 = 0, modWetDryMix2 = 0, modWetDryMix3 = 0, modWetDryMix4 = 0,
			modBandLevel1 = 0, modBandLevel2 = 0, modBandLevel3 = 0, modBandLevel4 = 0;

			var input, split, outMix, band1, band2, band3, band4;
			var sideChainSignal, sideChainSplit, sideChain1, sideChain2, sideChain3, sideChain4;
			var bandLevel1Sum, bandLevel2Sum, bandLevel3Sum, bandLevel4Sum;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in,2);
			split = TXBandSplitter4.ar(input, splitFreq1, splitFreq2, splitFreq3);
			// if compression active
			if ((arrOptions.at(5) == 1)
				or: (arrOptions.at(6) == 1)
				or: (arrOptions.at(7) == 1)
				or: (arrOptions.at(8) == 1)
				, {
					// if sidechain needed
					if ((arrOptions.at(0) == 1)
						or: (arrOptions.at(1) == 1)
						or: (arrOptions.at(2) == 1)
						or: (arrOptions.at(3) == 1)
						, {
							sideChainSignal = InFeedback.ar(sideChain, 1);
							// if side chain is split
							if (this.getSynthOption(9) == 1, {
								sideChainSplit = TXBandSplitter4.ar(sideChainSignal, splitFreq1, splitFreq2, splitFreq3);
								sideChain1 = sideChainSplit[0];
								sideChain2 = sideChainSplit[1];
								sideChain3 = sideChainSplit[2];
								sideChain4 = sideChainSplit[3];
							}, {
								sideChain1 = sideChainSignal;
								sideChain2 = sideChainSignal;
								sideChain3 = sideChainSignal;
								sideChain4 = sideChainSignal;
							});
					});
			});
			// compress band if active
			if (this.getSynthOption(5) == 1, {
				band1 = compressFunc.value(0, split[0], sideChain1, expandRatioMin, expandRatioMax,
					compRatioMin, compRatioMax, attackMax, releaseMax,
					threshold1, expandRatio1, compRatio1, attack1, release1, makeUpGain1, wetDryMix1,
					modThreshold1, modExpandRatio1, modCompRatio1, modAttack1, modRelease1, modWetDryMix1);
			}, {
				// input delayed
				band1 = this.getSynthOption(4).value(split[0]);
			});
			if (this.getSynthOption(6) == 1, {
				band2 = compressFunc.value(1, split[1], sideChain2, expandRatioMin, expandRatioMax,
					compRatioMin, compRatioMax, attackMax, releaseMax,
					threshold2, expandRatio2, compRatio2, attack2, release2, makeUpGain2, wetDryMix2,
					modThreshold2, modExpandRatio2, modCompRatio2, modAttack2, modRelease2, modWetDryMix2);
			}, {
				band2 = this.getSynthOption(4).value(split[1]);
			});
			if (this.getSynthOption(7) == 1, {
				band3 = compressFunc.value(2, split[2], sideChain3, expandRatioMin, expandRatioMax,
					compRatioMin, compRatioMax, attackMax, releaseMax,
					threshold3, expandRatio3, compRatio3, attack3, release3, makeUpGain3, wetDryMix3,
					modThreshold3, modExpandRatio3, modCompRatio3, modAttack3, modRelease3, modWetDryMix3);
			}, {
				band3 = this.getSynthOption(4).value(split[2]);
			});
			if (this.getSynthOption(8) == 1, {
				band4 = compressFunc.value(3, split[3], sideChain4, expandRatioMin, expandRatioMax,
					compRatioMin, compRatioMax, attackMax, releaseMax,
					threshold4, expandRatio4, compRatio4, attack4, release4, makeUpGain4, wetDryMix4,
					modThreshold4, modExpandRatio4, modCompRatio4, modAttack4, modRelease4, modWetDryMix4);
			}, {
				band4 = this.getSynthOption(4).value(split[3]);
			});
			bandLevel1Sum = (bandLevel1 + modBandLevel1).max(0).min(1) * 2;
			bandLevel2Sum = (bandLevel2 + modBandLevel2).max(0).min(1) * 2;
			bandLevel3Sum = (bandLevel3 + modBandLevel3).max(0).min(1) * 2;
			bandLevel4Sum = (bandLevel4 + modBandLevel4).max(0).min(1) * 2;
			outMix = Mix.new([band1 * bandLevel1Sum, band2 * bandLevel2Sum,
				band3 * bandLevel3Sum, band4 * bandLevel4Sum]);
			Out.ar(out, startEnv * outMix);
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["SynthOptionPopupPlusMinus", "Channel link", arrOptionData, 9],
			["SynthOptionPopupPlusMinus", "Lookahead", arrOptionData, 4],
			["SynthOptionPopupPlusMinus", "S-chain mode", arrOptionData, 9],
			["TXRangeSlider", "Expand ratio range", classData.ratioSpec, "expandRatioMin", "expandRatioMax"],
			["TXRangeSlider", "Compress ratio range", classData.ratioSpec, "compRatioMin", "compRatioMax"],
			["EZslider", "Clamp max", ControlSpec(0.001, 1), "attackMax"],
			["EZslider", "Relax max", ControlSpec(0.001, 1), "releaseMax"],

			["SynthOptionPopupPlusMinus", "Compander 1", arrOptionData, 5],
			["SynthOptionPopupPlusMinus", "Side-chain 1", arrOptionData, 0],
			["EZsliderUnmapped", "Threshold 1", ControlSpec(0, 1), "threshold1"],
			["EZsliderUnmapped", "Expand ratio 1", {this.getCurrentExpRatioSpec}, "expandRatio1"],
			["EZsliderUnmapped", "Comp ratio 1", {this.getCurrentCompRatioSpec}, "compRatio1"],
			["EZsliderUnmapped", "Clamp time 1", {this.getCurrentAttackSpec}, "attack1"],
			["EZsliderUnmapped", "Relax time 1", {this.getCurrentReleaseSpec}, "release1"],
			["EZslider", "Make-up gain 1", ControlSpec(0, 1), "makeUpGain1"],
			["EZslider", "Dry-wet mix 1", ControlSpec(0, 1), "wetDryMix1"],
			["EZslider", "Band Level 1", ControlSpec(0, 1), "bandLevel1"],

			["SynthOptionPopupPlusMinus", "Compander 2", arrOptionData, 6],
			["SynthOptionPopupPlusMinus", "Side-chain 2", arrOptionData, 1],
			["EZsliderUnmapped", "Threshold 2", ControlSpec(0, 1), "threshold2"],
			["EZsliderUnmapped", "Expand ratio 2", {this.getCurrentExpRatioSpec}, "expandRatio2"],
			["EZsliderUnmapped", "Comp ratio 2", {this.getCurrentCompRatioSpec}, "compRatio2"],
			["EZsliderUnmapped", "Clamp time 2", {this.getCurrentAttackSpec}, "attack2"],
			["EZsliderUnmapped", "Relax time 2", {this.getCurrentReleaseSpec}, "release2"],
			["EZslider", "Make-up gain 2", ControlSpec(0, 1), "makeUpGain2"],
			["EZslider", "Dry-wet mix 2", ControlSpec(0, 1), "wetDryMix2"],
			["EZslider", "Band Level 2", ControlSpec(0, 1), "bandLevel2"],

			["SynthOptionPopupPlusMinus", "Compander 3", arrOptionData, 7],
			["SynthOptionPopupPlusMinus", "Side-chain 3", arrOptionData, 2],
			["EZsliderUnmapped", "Threshold 3", ControlSpec(0, 1), "threshold3"],
			["EZsliderUnmapped", "Expand ratio 3", {this.getCurrentExpRatioSpec}, "expandRatio3"],
			["EZsliderUnmapped", "Comp ratio 3", {this.getCurrentCompRatioSpec}, "compRatio3"],
			["EZsliderUnmapped", "Clamp time 3", {this.getCurrentAttackSpec}, "attack3"],
			["EZsliderUnmapped", "Relax time 3", {this.getCurrentReleaseSpec}, "release3"],
			["EZslider", "Make-up gain 3", ControlSpec(0, 1), "makeUpGain3"],
			["EZslider", "Dry-wet mix 3", ControlSpec(0, 1), "wetDryMix3"],
			["EZslider", "Band Level 3", ControlSpec(0, 1), "bandLevel3"],

			["SynthOptionPopupPlusMinus", "Compander 4", arrOptionData, 8],
			["SynthOptionPopupPlusMinus", "Side-chain 4", arrOptionData, 3],
			["EZsliderUnmapped", "Threshold 4", ControlSpec(0, 1), "threshold4"],
			["EZsliderUnmapped", "Expand ratio 4", {this.getCurrentExpRatioSpec}, "expandRatio4"],
			["EZsliderUnmapped", "Comp ratio 4", {this.getCurrentCompRatioSpec}, "compRatio4"],
			["EZsliderUnmapped", "Clamp time 4", {this.getCurrentAttackSpec}, "attack4"],
			["EZsliderUnmapped", "Relax time 4", {this.getCurrentReleaseSpec}, "release4"],
			["EZslider", "Make-up gain 4", ControlSpec(0, 1), "makeUpGain4"],
			["EZslider", "Dry-wet mix 4", ControlSpec(0, 1), "wetDryMix4"],
			["EZslider", "Band Level 4", ControlSpec(0, 1), "bandLevel4"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	getCurrentExpRatioSpec {
		^ControlSpec(this.getSynthArgSpec("expandRatioMin"), this.getSynthArgSpec("expandRatioMax"));
	}

	getCurrentCompRatioSpec {
		^ControlSpec(this.getSynthArgSpec("compRatioMin"), this.getSynthArgSpec("compRatioMax"));
	}

	getCurrentAttackSpec {
		^ControlSpec(this.getSynthArgSpec("attackMin"), this.getSynthArgSpec("attackMax"));
	}

	getCurrentReleaseSpec {
		^ControlSpec(this.getSynthArgSpec("releaseMin"), this.getSynthArgSpec("releaseMax"));
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "All Bands", {displayOption = "showAllBands";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showAllBands")],
			["ActionButton", "Band 1", {displayOption = "showBand1";
				this.buildGuiSpecArray; system.showView;}, 50,
			TXColor.white, this.getButtonColour(displayOption == "showBand1")],
			["ActionButton", "Band 2", {displayOption = "showBand2";
				this.buildGuiSpecArray; system.showView;}, 50,
			TXColor.white, this.getButtonColour(displayOption == "showBand2")],
			["ActionButton", "Band 3", {displayOption = "showBand3";
				this.buildGuiSpecArray; system.showView;}, 50,
			TXColor.white, this.getButtonColour(displayOption == "showBand3")],
			["ActionButton", "Band 4", {displayOption = "showBand4";
				this.buildGuiSpecArray; system.showView;}, 50,
			TXColor.white, this.getButtonColour(displayOption == "showBand4")],
			["ActionButton", "Global", {displayOption = "showGlobal";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showGlobal")],
			["DividingLine"],
			["SpacerLine", 2],
		];

		if (displayOption == "showGlobal", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Channel link", arrOptionData, 10],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Lookahead", arrOptionData, 4],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "S-chain mode", arrOptionData, 9],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXRangeSlider", "Expand range", classData.ratioSpec, "expandRatioMin", "expandRatioMax"],
				["SpacerLine", 2],
				["TXRangeSlider", "Comp range", classData.ratioSpec, "compRatioMin", "compRatioMax"],
				["SpacerLine", 2],
				["EZslider", "Clamp max", ControlSpec(0.001, 1), "attackMax"],
				["SpacerLine", 2],
				["EZslider", "Relax max", ControlSpec(0.001, 1), "releaseMax"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Split Freq 1", classData.freqSpec, "splitFreq1", {arg view;
					var holdVal;
					holdVal = this.getSynthArgSpec("splitFreq2");
					if (view.value > holdVal,{
						view.value_(holdVal);
						this.setSynthValue("splitFreq1", holdVal);
					});
				}],
				["SpacerLine", 2],
				["EZslider", "Split Freq 2", classData.freqSpec, "splitFreq2", {arg view;
					var myVal = view.value;
					var holdVal;
					holdVal = this.getSynthArgSpec("splitFreq1");
					if (myVal < holdVal,{
						view.value_(holdVal);
						myVal = holdVal;
						this.setSynthValue("splitFreq2", holdVal);
					});
					holdVal = this.getSynthArgSpec("splitFreq3");
					if (myVal > holdVal,{
						view.value_(holdVal);
						this.setSynthValue("splitFreq2", holdVal);
					});
				}],
				["SpacerLine", 2],
				["EZslider", "Split Freq 3", classData.freqSpec, "splitFreq3", {arg view;
					var holdVal;
					holdVal = this.getSynthArgSpec("splitFreq2");
					if (view.value < holdVal,{
						view.value_(holdVal);
						this.setSynthValue("splitFreq3", holdVal);
					});
				}],
			];
		});
		if (displayOption == "showAllBands", {
			guiSpecArray = guiSpecArray ++[
				["TextBar", "Compander", 80, nil, nil, nil, \right],
				["SynthOptionCheckBox", "", arrOptionData, 5, 50],
				["SynthOptionCheckBox", "", arrOptionData, 6, 50],
				["SynthOptionCheckBox", "", arrOptionData, 7, 50],
				["SynthOptionCheckBox", "", arrOptionData, 8, 50],
				["SpacerLine", 2],
				["TextBar", "Use side-chain", 80, nil, nil, nil, \right],
				["SynthOptionCheckBox", "", arrOptionData, 0, 50],
				["SynthOptionCheckBox", "", arrOptionData, 1, 50],
				["SynthOptionCheckBox", "", arrOptionData, 2, 50],
				["SynthOptionCheckBox", "", arrOptionData, 3, 50],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",
					{this.copy1ToAll(["threshold1", "threshold2", "threshold3", "threshold4",  ]);}, 36],
				["TXMultiKnobNoUnmap", "Thres-hold", ["threshold1", "threshold2", "threshold3", "threshold4", ],
					4, ControlSpec(0, 1)],
				["SpacerLine", 2],
				["TextBar", "Expand ratio", 80, nil, nil, nil, \right],
				["TXScrollNumBoxUnmapped", this.getCurrentExpRatioSpec, "expandRatio1", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentExpRatioSpec, "expandRatio2", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentExpRatioSpec, "expandRatio3", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentExpRatioSpec, "expandRatio4", nil, 50, 0.1],
				["SpacerLine", 2],
				["TextBar", "Comp ratio", 80, nil, nil, nil, \right],
				["TXScrollNumBoxUnmapped", this.getCurrentCompRatioSpec, "compRatio1", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentCompRatioSpec, "compRatio2", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentCompRatioSpec, "compRatio3", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentCompRatioSpec, "compRatio4", nil, 50, 0.1],
				["SpacerLine", 2],
				["TextBar", "Attack", 80, nil, nil, nil, \right],
				["TXScrollNumBoxUnmapped", this.getCurrentAttackSpec, "attack1", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentAttackSpec, "attack2", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentAttackSpec, "attack3", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentAttackSpec, "attack4", nil, 50, 0.1],
				["SpacerLine", 2],
				["TextBar", "Release", 80, nil, nil, nil, \right],
				["TXScrollNumBoxUnmapped", this.getCurrentReleaseSpec, "release1", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentReleaseSpec, "release2", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentReleaseSpec, "release3", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentReleaseSpec, "release4", nil, 50, 0.1],
				["SpacerLine", 2],
				["TextBar", "Make-up gain", 80, nil, nil, nil, \right],
				["TXScrollNumBoxUnmapped", ControlSpec(0, 3), "makeUpGain1", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", ControlSpec(0, 3), "makeUpGain2", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", ControlSpec(0, 3), "makeUpGain3", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", ControlSpec(0, 3), "makeUpGain4", nil, 50, 0.1],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",
					{this.copy1ToAll(["wetDryMix1", "wetDryMix2", "wetDryMix3", "wetDryMix4"]);}, 36],
				["TXMultiKnobNoUnmap", "Dry-wet mix", ["wetDryMix1", "wetDryMix2", "wetDryMix3", "wetDryMix4", ],
					4, ControlSpec(0, 1)],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",
					{this.copy1ToAll(["bandLevel1", "bandLevel2", "bandLevel3", "bandLevel4",]);}, 36],
				["TXMultiKnobNoUnmap", "Band Level", ["bandLevel1", "bandLevel2", "bandLevel3", "bandLevel4", ],
					4, ControlSpec(0, 2)],
				["SpacerLine", 2],
			];
		});
		if (displayOption == "showBand1", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Compander", arrOptionData, 5],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Side-chain", arrOptionData, 0],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Threshold", ControlSpec(0, 1), "threshold1"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Expand ratio", this.getCurrentExpRatioSpec, "expandRatio1"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Comp ratio", this.getCurrentCompRatioSpec, "compRatio1"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZsliderUnmapped", "Clamp time", this.getCurrentAttackSpec, "attack1"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Relax time", this.getCurrentReleaseSpec, "release1"],
				["SpacerLine", 2],
				["EZslider", "Make-up gain", ControlSpec(0, 3), "makeUpGain1"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Dry-wet mix", ControlSpec(0, 1), "wetDryMix1"],
				["SpacerLine", 2],
				["EZslider", "Band Level", ControlSpec(0, 1), "bandLevel1"],
			];
		});
		if (displayOption == "showBand2", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Compander", arrOptionData, 6],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Side-chain", arrOptionData, 1],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Threshold", ControlSpec(0, 1), "threshold2"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Expand ratio", this.getCurrentExpRatioSpec, "expandRatio2"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Comp ratio", this.getCurrentCompRatioSpec, "compRatio2"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZsliderUnmapped", "Clamp time", this.getCurrentAttackSpec, "attack2"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Relax time", this.getCurrentReleaseSpec, "release2"],
				["SpacerLine", 2],
				["EZslider", "Make-up gain", ControlSpec(0, 3), "makeUpGain2"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Dry-wet mix", ControlSpec(0, 1), "wetDryMix2"],
				["SpacerLine", 2],
				["EZslider", "Band Level", ControlSpec(0, 1), "bandLevel2"],
			];
		});
		if (displayOption == "showBand3", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Compander", arrOptionData, 7],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Side-chain", arrOptionData, 2],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Threshold", ControlSpec(0, 1), "threshold3"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Expand ratio", this.getCurrentExpRatioSpec, "expandRatio3"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Comp ratio", this.getCurrentCompRatioSpec, "compRatio3"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZsliderUnmapped", "Clamp time", this.getCurrentAttackSpec, "attack3"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Relax time", this.getCurrentReleaseSpec, "release3"],
				["SpacerLine", 2],
				["EZslider", "Make-up gain", ControlSpec(0, 3), "makeUpGain3"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Dry-wet mix", ControlSpec(0, 1), "wetDryMix3"],
				["SpacerLine", 2],
				["EZslider", "Band Level", ControlSpec(0, 1), "bandLevel3"],
			];
		});
		if (displayOption == "showBand4", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Compander", arrOptionData, 8],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Side-chain", arrOptionData, 3],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Threshold", ControlSpec(0, 1), "threshold4"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Expand ratio", this.getCurrentExpRatioSpec, "expandRatio4"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Comp ratio", this.getCurrentCompRatioSpec, "compRatio4"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZsliderUnmapped", "Clamp time", this.getCurrentAttackSpec, "attack4"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Relax time", this.getCurrentReleaseSpec, "release4"],
				["SpacerLine", 2],
				["EZslider", "Make-up gain", ControlSpec(0, 3), "makeUpGain4"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Dry-wet mix", ControlSpec(0, 1), "wetDryMix4"],
				["SpacerLine", 2],
				["EZslider", "Band Level", ControlSpec(0, 1), "bandLevel4"],
			];
		});

	}

	getButtonColour { arg colour2Boolean;
		if (colour2Boolean == true, {
			^TXColor.sysGuiCol4;
		},{
			^TXColor.sysGuiCol1;
		});
	}

	copy1ToAll {arg arrayStrings;
		var holdVal = this.getSynthArgSpec(arrayStrings[0]);
		arrayStrings.do({arg item, i;
			if (i > 0, {
				this.setSynthValue(item, holdVal);
			});
		});
		system.showView;
	}

}

