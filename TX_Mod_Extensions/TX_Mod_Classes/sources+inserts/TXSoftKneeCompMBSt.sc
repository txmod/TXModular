// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSoftKneeCompMBSt : TXModuleBase {		// MultiBand Compressor module

	classvar <>classData;
	var	displayOption;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "SoftKnee CompMB St";
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
			["Knee 1", 1, "modKnee1", 0],
			["Knee 2", 1, "modKnee2", 0],
			["Knee 3", 1, "modKnee3", 0],
			["Knee 4", 1, "modKnee4", 0],
			["Ratio 1", 1, "modRatio1", 0],
			["Ratio 2", 1, "modRatio2", 0],
			["Ratio 3", 1, "modRatio3", 0],
			["Ratio 4", 1, "modRatio4", 0],
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
		classData.kneeSpec = ControlSpec(0, 48);
		classData.ratioSpec = ControlSpec(0.1, 10);
		classData.bandLevelSpec = ControlSpec(0, 2);
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
			["kneeMax", 24, defLagTime],
			["ratioMin", 1, defLagTime],
			["ratioMax", 10, defLagTime],
			["attackMax", 0.1, defLagTime],
			["releaseMax", 0.1, defLagTime],
			["threshold1", 0.5, defLagTime],
			["threshold2", 0.5, defLagTime],
			["threshold3", 0.5, defLagTime],
			["threshold4", 0.5, defLagTime],
			["knee1", ControlSpec(0, 24).unmap(6), defLagTime],
			["knee2", ControlSpec(0, 24).unmap(6), defLagTime],
			["knee3", ControlSpec(0, 24).unmap(6), defLagTime],
			["knee4", ControlSpec(0, 24).unmap(6), defLagTime],
			["ratio1", ControlSpec(1, 10).unmap(4), defLagTime],
			["ratio2", ControlSpec(1, 10).unmap(4), defLagTime],
			["ratio3", ControlSpec(1, 10).unmap(4), defLagTime],
			["ratio4", ControlSpec(1, 10).unmap(4), defLagTime],
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
			["modKnee1", 0, defLagTime],
			["modKnee2", 0, defLagTime],
			["modKnee3", 0, defLagTime],
			["modKnee4", 0, defLagTime],
			["modRatio1", 0, defLagTime],
			["modRatio2", 0, defLagTime],
			["modRatio3", 0, defLagTime],
			["modRatio4", 0, defLagTime],
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
		arrOptions = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
		arrOptionData = [
			[ // 0 - Band 1 side chain
				["use main input signal to control compressor", {
					arg input, sideChain; input;}],
				["use side-chain signal to control compressor", {
					arg input, sideChain; sideChain ! 2;}],
			],
			[ // 1 - Band 2 side chain
				["use main input signal to control compressor", {
					arg input, sideChain; input;}],
				["use side-chain signal to control compressor", {
					arg input, sideChain; sideChain ! 2;}],
			],
			[ // 2 - Band 3 side chain
				["use main input signal to control compressor", {
					arg input, sideChain; input;}],
				["use side-chain signal to control compressor", {
					arg input, sideChain; sideChain ! 2;}],
			],
			[ // 3 - Band 4 side chain
				["use main input signal to control compressor", {
					arg input, sideChain; input;}],
				["use side-chain signal to control compressor", {
					arg input, sideChain; sideChain ! 2;}],
			],
			[ // 4 - compression type
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
			[ //5 - Lookahead Delay
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
			[ // 6 - Compressor 1 active
				["Compressor Off", 0],
				["Compressor On", 1],
			],
			[ // 7 - Compressor 2 active
				["Compressor Off", 0],
				["Compressor On", 1],
			],
			[ // 8 - Compressor 3 active
				["Compressor Off", 0],
				["Compressor On", 1],
			],
			[ // 9 - Compressor 4 active
				["Compressor Off", 0],
				["Compressor On", 1],
			],
			[ // 10 - S-chain mode
				["Full range side chain signal", 0],
				["Band split side chain signal", 1],
			],
			[ // 11 - Channel link
				//["Linked Left & Right channels", {arg input; Mix.ar(input)}],
				["Linked Left & Right channels", {arg input; max (abs(input[0]), abs(input[1]))}], // better
				["Independent Left & Right channels", {arg input; input}],
			],
		];
		compressFunc = {arg bandIndex, input, sideChainSig, kneeMax, ratioMin, ratioMax, attackMax, releaseMax,
			threshold, knee,  ratio, attack,  release,  makeUp, wetDryMix,
			modThreshold, modKnee, modRatio, modAttack, modRelease, modWetDryMix;

			var inputDelayed, controlInput, thresholdSum, ratioSum, kneeSum, attackSum, releaseSum;
			var compFunc, controlSignal, mixSum, outSound;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			ratioSum = ratioMin + ( (ratioMax - ratioMin)
				* (ratio + modRatio).max(0).min(1));
			kneeSum = kneeMax * (knee + modKnee).max(0).min(1);
			attackSum = LinExp.kr((attack + modAttack).max(0).min(1), 0, 1, 0.001, attackMax.max(0.001));
			releaseSum = LinExp.kr((release + modRelease).max(0).min(1), 0, 1, 0.001, releaseMax.max(0.001));
			controlInput = this.getSynthOption(11).value(input);
			thresholdSum = (threshold + modThreshold).max(0).min(1);
			controlSignal = this.getSynthOption(bandIndex).value(controlInput, sideChainSig);
			compFunc =  this.getSynthOption(4);
			mixSum = (wetDryMix + modWetDryMix).max(0).min(1);
			inputDelayed = this.getSynthOption(5).value(input);
			outSound = compFunc.value(inputDelayed, startEnv * controlSignal, thresholdSum.squared.ampdb, ratioSum.reciprocal, kneeSum,
				attackSum, releaseSum, makeUp);
			(outSound * mixSum) + (inputDelayed * (1-mixSum));
		};
		synthDefFunc = { arg in, sideChain, out, splitFreq1, splitFreq2, splitFreq3,
			kneeMax, ratioMin, ratioMax, attackMax, releaseMax,
			threshold1, threshold2, threshold3, threshold4,
			knee1, knee2, knee3, knee4, ratio1, ratio2, ratio3, ratio4,
			attack1, attack2, attack3, attack4, release1, release2, release3, release4,
			makeUpGain1, makeUpGain2, makeUpGain3, makeUpGain4,
			wetDryMix1, wetDryMix2, wetDryMix3, wetDryMix4,
			bandLevel1, bandLevel2, bandLevel3, bandLevel4,
			modThreshold1 = 0, modThreshold2 = 0, modThreshold3 = 0, modThreshold4 = 0,
			modKnee1 = 0, modKnee2 = 0, modKnee3 = 0, modKnee4 = 0,
			modRatio1 = 0, modRatio2 = 0, modRatio3 = 0, modRatio4 = 0,
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
			if ((arrOptions.at(6) == 1)
				or: (arrOptions.at(7) == 1)
				or: (arrOptions.at(8) == 1)
				or: (arrOptions.at(9) == 1)
				, {
					// if sidechain needed
					if ((arrOptions.at(0) == 1)
						or: (arrOptions.at(1) == 1)
						or: (arrOptions.at(2) == 1)
						or: (arrOptions.at(3) == 1)
						, {
							sideChainSignal = InFeedback.ar(sideChain, 1);
							// if side chain is split
							if ((this.getSynthOption(10) == 1), {
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
			if (this.getSynthOption(6) == 1, {
				band1 = compressFunc.value(0, split[0], sideChain1, kneeMax, ratioMin, ratioMax, attackMax, releaseMax,
					threshold1, knee1, ratio1, attack1, release1, makeUpGain1, wetDryMix1,
					modThreshold1, modKnee1, modRatio1, modAttack1, modRelease1, modWetDryMix1);
			}, {
				// input delayed
				band1 = this.getSynthOption(5).value(split[0]);
			});
			if (this.getSynthOption(7) == 1, {
				band2 = compressFunc.value(1, split[1], sideChain2, kneeMax, ratioMin, ratioMax, attackMax, releaseMax,
					threshold2, knee2, ratio2, attack2, release2, makeUpGain2, wetDryMix2,
					modThreshold2, modKnee2, modRatio2, modAttack2, modRelease2, modWetDryMix2);
			}, {
				band2 = this.getSynthOption(5).value(split[1]);
			});
			if (this.getSynthOption(8) == 1, {
				band3 = compressFunc.value(2, split[2], sideChain3, kneeMax, ratioMin, ratioMax, attackMax, releaseMax,
					threshold3, knee3, ratio3, attack3, release3, makeUpGain3, wetDryMix3,
					modThreshold3, modKnee3, modRatio3, modAttack3, modRelease3, modWetDryMix3);
			}, {
				band3 = this.getSynthOption(5).value(split[2]);
			});
			if (this.getSynthOption(9) == 1, {
				band4 = compressFunc.value(3, split[3], sideChain4, kneeMax, ratioMin, ratioMax, attackMax, releaseMax,
					threshold4, knee4, ratio4, attack4, release4, makeUpGain4, wetDryMix4,
					modThreshold4, modKnee4, modRatio4, modAttack4, modRelease4, modWetDryMix4);
			}, {
				band4 = this.getSynthOption(5).value(split[3]);
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
			["SynthOptionPopupPlusMinus", "Channel link", arrOptionData, 11],
			["SynthOptionPopupPlusMinus", "Compression", arrOptionData, 4],
			["SynthOptionPopupPlusMinus", "Lookahead", arrOptionData, 5],
			["SynthOptionPopupPlusMinus", "S-chain mode", arrOptionData, 10],
			["EZslider", "Knee max", classData.kneeSpec, "kneeMax"],
			["TXRangeSlider", "Ratio range", classData.ratioSpec, "ratioMin", "ratioMax"],
			["EZslider", "Clamp max", ControlSpec(0.001, 1), "attackMax"],
			["EZslider", "Relax max", ControlSpec(0.001, 1), "releaseMax"],

			["SynthOptionPopupPlusMinus", "Compress 1", arrOptionData, 6],
			["SynthOptionPopupPlusMinus", "Side-chain 1", arrOptionData, 0],
			["EZsliderUnmapped", "Threshold 1", ControlSpec(0, 1), "threshold1"],
			["EZsliderUnmapped", "Knee db 1", {this.getCurrentKneeSpec}, "knee1"],
			["EZsliderUnmapped", "Ratio 1", {this.getCurrentratioSpec}, "ratio1"],
			["EZsliderUnmapped", "Clamp time 1", {this.getCurrentAttackSpec}, "attack1"],
			["EZsliderUnmapped", "Relax time 1", {this.getCurrentReleaseSpec}, "release1"],
			["EZslider", "Make-up gain 1", ControlSpec(0, 1), "makeUpGain1"],
			["EZslider", "Dry-wet mix 1", ControlSpec(0, 1), "wetDryMix1"],
			["EZsliderUnmapped", "Band Level 1", ControlSpec(0, 2), "bandLevel1"],

			["SynthOptionPopupPlusMinus", "Compress 2", arrOptionData, 7],
			["SynthOptionPopupPlusMinus", "Side-chain 2", arrOptionData, 1],
			["EZsliderUnmapped", "Threshold 2", ControlSpec(0, 1), "threshold2"],
			["EZsliderUnmapped", "Knee db 2", {this.getCurrentKneeSpec}, "knee2"],
			["EZsliderUnmapped", "Ratio 2", {this.getCurrentratioSpec}, "ratio2"],
			["EZsliderUnmapped", "Clamp time 2", {this.getCurrentAttackSpec}, "attack2"],
			["EZsliderUnmapped", "Relax time 2", {this.getCurrentReleaseSpec}, "release2"],
			["EZslider", "Make-up gain 2", ControlSpec(0, 1), "makeUpGain2"],
			["EZslider", "Dry-wet mix 2", ControlSpec(0, 1), "wetDryMix2"],
			["EZsliderUnmapped", "Band Level 2", ControlSpec(0, 2), "bandLevel2"],

			["SynthOptionPopupPlusMinus", "Compress 3", arrOptionData, 8],
			["SynthOptionPopupPlusMinus", "Side-chain 3", arrOptionData, 2],
			["EZsliderUnmapped", "Threshold 3", ControlSpec(0, 1), "threshold3"],
			["EZsliderUnmapped", "Knee db 3", {this.getCurrentKneeSpec}, "knee3"],
			["EZsliderUnmapped", "Ratio 3", {this.getCurrentratioSpec}, "ratio3"],
			["EZsliderUnmapped", "Clamp time 3", {this.getCurrentAttackSpec}, "attack3"],
			["EZsliderUnmapped", "Relax time 3", {this.getCurrentReleaseSpec}, "release3"],
			["EZslider", "Make-up gain 3", ControlSpec(0, 1), "makeUpGain3"],
			["EZslider", "Dry-wet mix 3", ControlSpec(0, 1), "wetDryMix3"],
			["EZsliderUnmapped", "Band Level 3", ControlSpec(0, 2), "bandLevel3"],

			["SynthOptionPopupPlusMinus", "Compress 4", arrOptionData, 9],
			["SynthOptionPopupPlusMinus", "Side-chain 4", arrOptionData, 3],
			["EZsliderUnmapped", "Threshold 4", ControlSpec(0, 1), "threshold4"],
			["EZsliderUnmapped", "Knee db 4", {this.getCurrentKneeSpec}, "knee4"],
			["EZsliderUnmapped", "Ratio 4", {this.getCurrentratioSpec}, "ratio4"],
			["EZsliderUnmapped", "Clamp time 4", {this.getCurrentAttackSpec}, "attack4"],
			["EZsliderUnmapped", "Relax time 4", {this.getCurrentReleaseSpec}, "release4"],
			["EZslider", "Make-up gain 4", ControlSpec(0, 1), "makeUpGain4"],
			["EZslider", "Dry-wet mix 4", ControlSpec(0, 1), "wetDryMix4"],
			["EZsliderUnmapped", "Band Level 4", ControlSpec(0, 2), "bandLevel4"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	getCurrentKneeSpec {
		^ControlSpec(this.getSynthArgSpec("kneeMin"), this.getSynthArgSpec("kneeMax"));
	}

	getCurrentRatioSpec {
		^ControlSpec(this.getSynthArgSpec("ratioMin"), this.getSynthArgSpec("ratioMax"));
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
				["SynthOptionPopupPlusMinus", "Channel link", arrOptionData, 11],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Compression", arrOptionData, 4],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Lookahead", arrOptionData, 5],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "S-chain mode", arrOptionData, 10],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Knee max", classData.kneeSpec, "kneeMax"],
				["SpacerLine", 2],
				["TXRangeSlider", "Ratio range", classData.ratioSpec, "ratioMin", "ratioMax"],
				["SpacerLine", 2],
				["EZslider", "Clamp max", ControlSpec(0.001, 1), "attackMax"],
				["SpacerLine", 2],
				["EZslider", "Relax max", ControlSpec(0.001, 1), "releaseMax"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Split Freq 1", ControlSpec(40, 20000, \exp), "splitFreq1", {arg view;
					var holdVal;
					holdVal = this.getSynthArgSpec("splitFreq2");
					if (view.value > holdVal,{
						view.value_(holdVal);
						this.setSynthArgSpec("splitFreq1", holdVal);
					});
				}],
				["SpacerLine", 2],
				["EZslider", "Split Freq 2", ControlSpec(40, 20000, \exp), "splitFreq2", {arg view;
					var myVal = view.value;
					var holdVal;
					holdVal = this.getSynthArgSpec("splitFreq1");
					if (myVal < holdVal,{
						view.value_(holdVal);
						myVal = holdVal;
						this.setSynthArgSpec("splitFreq2", holdVal);
					});
					holdVal = this.getSynthArgSpec("splitFreq3");
					if (myVal > holdVal,{
						view.value_(holdVal);
						this.setSynthArgSpec("splitFreq2", holdVal);
					});
				}],
				["SpacerLine", 2],
				["EZslider", "Split Freq 3", ControlSpec(40, 20000, \exp), "splitFreq3", {arg view;
					var holdVal;
					holdVal = this.getSynthArgSpec("splitFreq2");
					if (view.value < holdVal,{
						view.value_(holdVal);
						this.setSynthArgSpec("splitFreq3", holdVal);
					});
				}],
			];
		});
		if (displayOption == "showAllBands", {
			guiSpecArray = guiSpecArray ++[
				["TextBar", "Compress", 80, nil, nil, nil, \right],
				["SynthOptionCheckBox", "", arrOptionData, 6, 50],
				["SynthOptionCheckBox", "", arrOptionData, 7, 50],
				["SynthOptionCheckBox", "", arrOptionData, 8, 50],
				["SynthOptionCheckBox", "", arrOptionData, 9, 50],
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
				["TextBar", "Knee", 80, nil, nil, nil, \right],
				["TXScrollNumBoxUnmapped", this.getCurrentKneeSpec, "knee1", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentKneeSpec, "knee2", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentKneeSpec, "knee3", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentKneeSpec, "knee4", nil, 50, 0.1],
				["SpacerLine", 2],
				["TextBar", "Ratio", 80, nil, nil, nil, \right],
				["TXScrollNumBoxUnmapped", this.getCurrentRatioSpec, "ratio1", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentRatioSpec, "ratio2", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentRatioSpec, "ratio3", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", this.getCurrentRatioSpec, "ratio4", nil, 50, 0.1],
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
				["TXScrollNumBoxUnmapped", ControlSpec(0, 1), "makeUpGain1", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", ControlSpec(0, 1), "makeUpGain2", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", ControlSpec(0, 1), "makeUpGain3", nil, 50, 0.1],
				["TXScrollNumBoxUnmapped", ControlSpec(0, 1), "makeUpGain4", nil, 50, 0.1],
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
				["SynthOptionPopupPlusMinus", "Compress", arrOptionData, 6],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Side-chain", arrOptionData, 0],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Threshold", ControlSpec(0, 1), "threshold1"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Knee db", this.getCurrentKneeSpec, "knee1"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Ratio", this.getCurrentRatioSpec, "ratio1"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZsliderUnmapped", "Clamp time", this.getCurrentAttackSpec, "attack1"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Relax time", this.getCurrentReleaseSpec, "release1"],
				["SpacerLine", 2],
				["EZslider", "Make-up gain", ControlSpec(0, 1), "makeUpGain1"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Dry-wet mix", ControlSpec(0, 1), "wetDryMix1"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Band Level", ControlSpec(0, 2), "bandLevel1"],
			];
		});
		if (displayOption == "showBand2", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Compress", arrOptionData, 7],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Side-chain", arrOptionData, 1],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Threshold", ControlSpec(0, 1), "threshold2"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Knee db", this.getCurrentKneeSpec, "knee2"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Ratio", this.getCurrentRatioSpec, "ratio2"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZsliderUnmapped", "Clamp time", this.getCurrentAttackSpec, "attack2"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Relax time", this.getCurrentReleaseSpec, "release2"],
				["SpacerLine", 2],
				["EZslider", "Make-up gain", ControlSpec(0, 1), "makeUpGain2"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Dry-wet mix", ControlSpec(0, 1), "wetDryMix2"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Band Level", ControlSpec(0, 2), "bandLevel2"],
			];
		});
		if (displayOption == "showBand3", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Compress", arrOptionData, 8],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Side-chain", arrOptionData, 2],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Threshold", ControlSpec(0, 1), "threshold3"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Knee db", this.getCurrentKneeSpec, "knee3"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Ratio", this.getCurrentRatioSpec, "ratio3"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZsliderUnmapped", "Clamp time", this.getCurrentAttackSpec, "attack3"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Relax time", this.getCurrentReleaseSpec, "release3"],
				["SpacerLine", 2],
				["EZslider", "Make-up gain", ControlSpec(0, 1), "makeUpGain3"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Dry-wet mix", ControlSpec(0, 1), "wetDryMix3"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Band Level", ControlSpec(0, 2), "bandLevel3"],
			];
		});
		if (displayOption == "showBand4", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Compress", arrOptionData, 9],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Side-chain", arrOptionData, 3],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Threshold", ControlSpec(0, 1), "threshold4"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Knee db", this.getCurrentKneeSpec, "knee4"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Ratio", this.getCurrentRatioSpec, "ratio4"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZsliderUnmapped", "Clamp time", this.getCurrentAttackSpec, "attack4"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Relax time", this.getCurrentReleaseSpec, "release4"],
				["SpacerLine", 2],
				["EZslider", "Make-up gain", ControlSpec(0, 1), "makeUpGain4"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Dry-wet mix", ControlSpec(0, 1), "wetDryMix4"],
				["SpacerLine", 2],
				["EZsliderUnmapped", "Band Level", ControlSpec(0, 2), "bandLevel4"],
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

