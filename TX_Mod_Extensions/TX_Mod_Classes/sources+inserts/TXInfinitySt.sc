// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXInfinitySt : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Infinity St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Density", 1, "modDensity", 0],
			["Feedback", 1, "modFeedback", 0],
			["Mod Speed", 1, "modModSpeed", 0],
			["Mod Depth", 1, "modModDepth", 0],
			["Gain boost", 1, "modOutGain", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
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
			["density", 0.5, defLagTime],
			["densityMin", 0, defLagTime],
			["densityMax", 10, defLagTime],
			["feedback", 0.1, defLagTime],
			["feedbackMin", 0.0, defLagTime],
			["feedbackMax", 1.0, defLagTime],
			["modSpeed", 0.05, defLagTime],
			["modSpeedMin", 0, defLagTime],
			["modSpeedMax", 10, defLagTime],
			["modDepth", 0.1, defLagTime],
			["modDepthMin", 0, defLagTime],
			["modDepthMax", 1, defLagTime],
			["outGain", 0.2, defLagTime],
			["outGainMin", 0, defLagTime],
			["outGainMax", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modDensity", 0, defLagTime],
			["modFeedback", 0, defLagTime],
			["modModSpeed", 0, defLagTime],
			["modModDepth", 0, defLagTime],
			["modOutGain", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0, 2];
		arrOptionData = [
			[ // Character - delay times
				["Character A (no modulation)", {arg modSpeed, modDepth;
					[ 0.063688278198242, 0.10931885242462, 0.13816833496094,  0.17916965484619, 0.20996558666229,
						0.2173627614975, 0.34908771514893, 0.39038562774658, 0.39166128635406, 0.51248371601105,
						0.57942581176758, 0.59853398799896, 0.82071149349213, 0.86482357978821, 0.92119491100311 ]
				}],
				["Character B  (no modulation)", {arg modSpeed, modDepth;
					[ 0.16076028347015, 0.78569638729095, 0.2820987701416, 0.13864994049072, 0.25435173511505,
						0.9283002614975, 0.6134021282196, 0.57844746112823, 0.23830676078796, 0.1970888376236,
						0.11009764671326, 0.76751637458801, 0.54695808887482, 0.46127164363861, 0.43755352497101 ]
				}],
				["Character C (with modulation)", {arg modSpeed, modDepth;
					[ 0.98628115653992, 0.113548016548157, 0.042730927467346, 0.76374709606171,
						SinOsc.kr(modSpeed * 0.1, 0, modDepth * 0.2) + 0.21333503723145, 0.57923424243927,
						0.22005033493042, 0.51011073589325, 0.46614801883698, 0.99149787425995,
						0.78647327423096, 0.94005215167999, 0.048812885284424,
						SinOsc.kr(modSpeed * 0.06181, 0, modDepth * 0.6) + 0.6326108455658, 0.3009045124054 ]
				}],
				["Character D (with modulation)", {arg modSpeed, modDepth;
					[ SinOsc.kr(modSpeed * 0.1, 0, modDepth * 0.2) + 0.20707452297211, 0.39505696296692, 0.072243928909302,
						0.09552788734436, 0.8510125875473, 0.69681096076965,
						SinOsc.kr(modSpeed * 0.16181, 0, modDepth * 0.36181) + 0.59402706623077, 0.94848668575287, 0.70984494686127,
						0.4460084438324, 0.14706468582153, 0.66398406028748, 0.73581194877625, 0.57954800128937,
						SinOsc.kr(modSpeed * 0.06181, 0, modDepth * 0.26181) + 0.41727387905121 ]
				}],
			],
			[ // feedback modes - decay multipliers
				["All positive", 1 ! 15],
				["Mixed 1 (mostly positive)", [1, 1, -1, 1, 1, 1, -1, 1, -1, -1, 1, 1, -1, 1, 1, ]],
				["Mixed 2 (equal mix)", [1, 1, -1, 1, 1, 1, -1, 1, -1, -1, 1, 1, -1, 1, 1, ]],
				["Mixed 3 (mostly negative)", [-1, -1, 1, -1, 1, -1, -1, -1, -1, 1, -1, -1, 1, -1, -1,]],
				["All negative", -1 ! 15],
			],
		];
		synthDefFunc = { arg in, out, density, densityMin, densityMax,feedback, feedbackMin, feedbackMax,
			modSpeed, modSpeedMin, modSpeedMax, modDepth, modDepthMin, modDepthMax,
			outGain, outGainMin, outGainMax, wetDryMix,
			modDensity=0, modFeedback=0, modModSpeed=0, modModDepth=0, modOutGain=0, modWetDryMix=0;

			var input, local, densitySum, feedbackSum, modSpeedSum, modDepthSum, outGainSum, mixCombined;
			var arrTimes, arrDecayMults, decay, outReverb, outSound;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(InFeedback.ar(in,2));
			local = LocalIn.ar(2) + [(input[0] * 0.55) + (input[1] * 0.45), (input[0] * 0.45) + (input[1] * 0.55), ];
			densitySum = densityMin + ( (densityMax-densityMin) * (density + modDensity).max(0).min(1));
			feedbackSum = feedbackMin + ( (feedbackMax-feedbackMin) * (feedback + modFeedback).max(0).min(1) );
			modSpeedSum = modSpeedMin + ( (modSpeedMax-modSpeedMin) * (modSpeed + modModSpeed).max(0).min(1) );
			modDepthSum = modDepthMin + ( (modDepthMax-modDepthMin) * (modDepth + modModDepth).max(0).min(1) );
			outGainSum = outGainMin + ( (outGainMax-outGainMin) * (outGain + modOutGain).max(0).min(1) );
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			arrTimes = this.getSynthOption(0).value(Lag.kr(modSpeedSum, 5), Lag.kr(modDepthSum, 5));
			arrDecayMults = this.getSynthOption(1);
			decay = 1 + (2 * Lag.kr(densitySum, 5));

			15.do{arg i; local = AllpassC.ar(local, 1, arrTimes[i] * [1, 0.99185].wrapAt([i,i+1]), decay * arrDecayMults[i])};

			local = LeakDC.ar(local);
			LocalOut.ar(local * Lag.kr(feedbackSum, 5));

			// outReverb =  Compander.ar(local, local,
			// 	thresh: 0.1, // default: 0.1
			// 	slopeBelow: 1, slopeAbove: 0.1, clampTime: 0.0, relaxTime: 0.1, mul: outGainSum * 10
			// );
			outReverb =  SoftKneeCompressor.peak(local, local, thresh: -12, ratio: 0.25, knee:6,
				attack: 0 , release: 0.5, makeUp: outGainSum);
			outSound = (outReverb * mixCombined) + (input * (1-mixCombined));
			outSound = LeakDC.ar(outSound);
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		guiSpecArray = [
			["DividingLine"],
			["SpacerLine", 6],
			["SynthOptionPopupPlusMinus", "Reverb type", arrOptionData, 0, 450],
			["SpacerLine", 6],
			["SynthOptionPopupPlusMinus", "Feedback typ", arrOptionData, 1, 450],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Feedback", ControlSpec(0, 1), "feedback", "feedbackMin", "feedbackMax"],
			["SpacerLine", 6],
			["EZslider", "Density", ControlSpec(0, 1), "density"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Mod speed", ControlSpec(0, 10), "modSpeed", "modSpeedMin", "modSpeedMax"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Mod depth", ControlSpec(0, 1), "modDepth", "modDepthMin", "modDepthMax"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Gain boost", ControlSpec(0, 1), "outGain", "outGainMin", "outGainMax"],
			["SpacerLine", 6],
			["WetDryMixSlider"],
		];
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

