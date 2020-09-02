// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXWaveFold : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "WaveFold";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["In Gain", 1, "modInGain", 0],
			["Low Limit", 1, "modLowLimit", 0],
			["High Limit", 1, "modHighLimit", 0],
			["Harmonics", 1, "modHarmonics", 0],
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
			["inGain", ControlSpec(1, 10).unmap(2), defLagTime],
			["inGainMin", 1, defLagTime],
			["inGainMax", 10, defLagTime],
			["lowLimit", 0, defLagTime],
			["lowLimitMin", -1, defLagTime],
			["lowLimitMax", -0.01, defLagTime],
			["highLimit", 1, defLagTime],
			["highLimitMin", 0.01, defLagTime],
			["highLimitMax", 1, defLagTime],
			["harmonics", 0.5, defLagTime],
			["harmonicsMin", 0, defLagTime],
			["harmonicsMax", 3, defLagTime],
			["outGain", 1, defLagTime],
			["wetDryMix", 1, defLagTime],
			["modInGain", 0, defLagTime],
			["modLowLimit", 0, defLagTime],
			["modHighLimit", 0, defLagTime],
			["modHarmonics", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0, 0, 0, 0, 1];
		arrOptionData = [
			["Fold (default)",
				{arg inSound, lo, hi, amp; (amp * inSound).fold(lo, hi); }
			],
			["Wrap",
				{arg inSound, lo, hi, amp; (amp * inSound).wrap(lo, hi); }
			],
			["Clip",
				{arg inSound, lo, hi, amp; (amp * inSound).clip(lo, hi); }
			],
			["Truncate using high limit",
				{arg inSound, lo, hi, amp; (amp * inSound).trunc(hi); }
			],
			["Off (no effect)",
				{arg inSound, lo, hi, amp; inSound; }
			],
			["Fold HD4",
				{arg inSound, lo, hi, amp; OSFold4.ar(amp * inSound, lo, hi); }
			],
			["Wrap HD4",
				{arg inSound, lo, hi, amp; OSWrap4.ar(amp * inSound, lo, hi); }
			],
			["Clip HD4",
				{arg inSound, lo, hi, amp; Clipper4.ar(amp * inSound, lo, hi); }
			],
			["Truncate HD4 using high limit",
				{arg inSound, lo, hi, amp; OSTrunc4.ar(amp * inSound, hi); }
			],
			["Fold HD8",
				{arg inSound, lo, hi, amp; OSFold8.ar(amp * inSound, lo, hi); }
			],
			["Wrap HD8",
				{arg inSound, lo, hi, amp; OSWrap8.ar(amp * inSound, lo, hi); }
			],
			["Clip HD8",
				{arg inSound, lo, hi, amp; Clipper8.ar(amp * inSound, lo, hi); }
			],
			["Truncate HD8 using high limit",
				{arg inSound, lo, hi, amp; OSTrunc8.ar(amp * inSound, hi); }
			],
		] ! 4;
		arrOptionData = arrOptionData.add([
			["Off", {arg inSound, distorted; distorted; }],
			["On", {arg inSound, distorted; Balance.ar(distorted, inSound); }],
		]);
		synthDefFunc = { arg in, out, inGain, inGainMin, inGainMax, lowLimit, lowLimitMin, lowLimitMax,
			highLimit, highLimitMin, highLimitMax, harmonics, harmonicsMin, harmonicsMax, outGain, wetDryMix,
			modInGain=0, modLowLimit=0, modHighLimit=0, modHarmonics=0, modWetDryMix=0;

			var input, mixSum, inGainSum, lowLimitSum, highLimitSum, harmonicsSum;
			var layer1Out, layer2Out, layer3Out, layer4Out, balanceOut;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(InFeedback.ar(in,1));
			inGainSum = (inGain + modInGain).max(0).min(1).linlin(0, 1, inGainMin, inGainMax);
			lowLimitSum = (lowLimit + modLowLimit).max(0).min(1).linlin(0, 1, lowLimitMin, lowLimitMax);
			highLimitSum = (highLimit + modHighLimit).max(0).min(1).linlin(0, 1, highLimitMin, highLimitMax);
			harmonicsSum = (harmonics + modHarmonics).max(0).min(1).linlin(0, 1, harmonicsMin, harmonicsMax);
			mixSum = (wetDryMix + modWetDryMix).max(0).min(1);
			layer1Out =  this.getSynthOption(0).value(input * inGainSum, lowLimitSum, highLimitSum, harmonicsSum);
			layer2Out =  this.getSynthOption(1).value(layer1Out, lowLimitSum, highLimitSum, 1 + harmonicsSum);
			layer3Out =  this.getSynthOption(2).value(layer2Out, lowLimitSum, highLimitSum, 1 + harmonicsSum);
			layer4Out =  this.getSynthOption(3).value(layer3Out, lowLimitSum, highLimitSum, 1 + harmonicsSum);
			balanceOut = this.getSynthOption(4).value(input, layer4Out);
			Out.ar(out, startEnv * ((outGain * LeakDC.ar(balanceOut) * mixSum) + (input * (1-mixSum))));
		};
		guiSpecArray = [
			["SynthOptionPopupPlusMinus", "Layer 1 Mode", arrOptionData, 0, 280],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Layer 2 Mode", arrOptionData, 1, 280],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Layer 3 Mode", arrOptionData, 2, 280],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Layer 4 Mode", arrOptionData, 3, 280],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "In Gain", ControlSpec(0, 10), "inGain", "inGainMin", "inGainMax"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Low Limit", ControlSpec(-1, -0.01), "lowLimit", "lowLimitMin", "lowLimitMax"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "High Limit", ControlSpec(0.01, 1), "highLimit", "highLimitMin", "highLimitMax"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Harmonics", ControlSpec(0, 10), "harmonics", "harmonicsMin", "harmonicsMax"],
			["DividingLine"],
			["SpacerLine", 4],
			["SynthOptionCheckBox", "Match Level of Input Signal", arrOptionData, 4, 220],
			["SpacerLine", 4],
			["EZslider", "Out Gain", ControlSpec(0, 2), "outGain"],
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

