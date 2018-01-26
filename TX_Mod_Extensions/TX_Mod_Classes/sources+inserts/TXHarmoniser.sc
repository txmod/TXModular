// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXHarmoniser : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Harmoniser";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Modify 1", 1, "modChange1", 0],
			["Modify 2", 1, "modChange2", 0],
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
		var holdControlSpec, holdControlSpec2;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["change1", 0, defLagTime],
			["change1Min", 0, defLagTime],
			["change1Max", 1, defLagTime],
			["change2", 0, defLagTime],
			["change2Min", 0, defLagTime],
			["change2Max", 1, defLagTime],
			["transpose", 0, 0],
			["smoothTime", 0.1, 0],
			["lag", 0.05, defLagTime],
			["lagMin", 0.001, defLagTime],
			["lagMax", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modChange1", 0, defLagTime],
			["modChange2", 0, defLagTime],
			["modLag", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0, 0];	// [waveform type, pitch detector]
		arrOptionData = [
			TXWaveForm.arrOptionData,
			[
				["Type 1 - Tartini method, slower & more accurate",
					{arg input; Tartini.kr(input); }],
				["Type 2 - Original method, faster & less accurate",
					{arg input; Pitch.kr(input, ampThreshold: 0.02, median: 7); }],
			]
		];
		synthDefFunc = {
			arg in, out, change1, change1Min, change1Max, change2, change2Min, change2Max,
			transpose, smoothTime, lag, lagMin, lagMax, wetDryMix, modChange1=0, modChange2=0,  modLag=0, modWetDryMix=0;
			var input, att, rel, freq, hasFreq, outFreq, trans, outFunction, outChange1, outChange2,
			lagtime, outAmp, outWave, mixCombined, outSound, pitchDetectFunction;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in,1) * startEnv;
			pitchDetectFunction = this.getSynthOption(1);
			# freq, hasFreq = pitchDetectFunction.value(input);
			trans = 2 ** (transpose /12);
			lagtime = lagMin + ((lagMax - lagMin) * (lag + modLag).max(0.001).min(1));
			outFreq = Lag.kr(			// lag the freq
				freq * trans,
				smoothTime		//  lag time
			);

			outFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			outChange1 = change1Min + ((change1Max - change1Min) * (change1 + modChange1).max(0).min(1));
			outChange2 = change2Min + ((change2Max - change2Min) * (change2 + modChange2).max(0).min(1));
			outAmp = Lag.kr(			// lag the input
				hasFreq.round(1.0) *
				Amplitude.kr(InFeedback.ar(in,1), 0.1, 0.1),
				lagtime		//  lag time
			);
			outWave = outFunction.value(outFreq, outChange1, outChange2) * outAmp;
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outSound = (outWave * mixCombined) + (input * (1-mixCombined));
			Out.ar(out, startEnv * outSound);
		};
		holdControlSpec = ControlSpec(0.001, 10, \lin, 0, 1, units: " secs");
		holdControlSpec2 = ControlSpec(0.01, 1, \lin, 0, 1, units: " secs");
		guiSpecArray = [
			["SynthOptionPopupPlusMinus", "Pitch detector", arrOptionData, 1, nil, {system.flagGuiUpd}],
			["SpacerLine", 4],
			["SynthOptionListPlusMinus", "Waveform", arrOptionData, 0, nil, 90, {system.showView;}],
			["NextLine"],
			["TextViewDisplay", {TXWaveForm.arrDescriptions.at(arrOptions.at(0).asInteger);}],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Modify 1", \unipolar, "change1", "change1Min", "change1Max"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Modify 2", \unipolar, "change2", "change2Min", "change2Max"],
			["SpacerLine", 4],
			["Transpose"],
			["SpacerLine", 4],
			["EZSlider", "Smooth time", holdControlSpec2,"smoothTime"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Att. + Decay", holdControlSpec, "lag", "lagMin", "lagMax"],
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

