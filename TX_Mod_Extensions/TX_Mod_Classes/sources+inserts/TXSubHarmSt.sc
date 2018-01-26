// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSubHarmSt : TXModuleBase {

	classvar <>classData;

	var displayOption;
	var controlSpecLevel, controlSpecFreq;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Sub Harm St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Sub 1 Level", 1, "modSub1Level", 0],
			["Sub 2 Level", 1, "modSub2Level", 0],
			["Sub 3 Level", 1, "modSub3Level", 0],
			["Sub 4 Level", 1, "modSub4Level", 0],
			["Filter freq", 1, "modFreq", 0],
			["Smooth Time", 1, "modLag", 0],
			["Resonance", 1, "modRes", 0],
			["Saturation", 1, "modSat", 0],
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
		controlSpecLevel = ControlSpec.new(0, 1);
		controlSpecFreq = ControlSpec.new(1, 1000, \exp );
		displayOption = "showSubs";
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["outGain", 0.5, defLagTime],
			["sub1Level", 0.1, defLagTime],
			["sub1LevelMin", 0, defLagTime],
			["sub1LevelMax", 1, defLagTime],
			["sub2Level", 0.7, defLagTime],
			["sub2LevelMin", 0, defLagTime],
			["sub2LevelMax", 1, defLagTime],
			["sub3Level", 0.1, defLagTime],
			["sub3LevelMin", 0, defLagTime],
			["sub3LevelMax", 1, defLagTime],
			["sub4Level", 0.2, defLagTime],
			["sub4LevelMin", 0, defLagTime],
			["sub4LevelMax", 1, defLagTime],
			["freq", 0.8, defLagTime],
			["freqMin",40, defLagTime],
			["freqMax", 20000, defLagTime],
			["lag", 0.5, defLagTime],
			["lagMin", 0.01, defLagTime],
			["lagMax", 1, defLagTime],
			["res", 0, defLagTime],
			["resMin", 0,  defLagTime],
			["resMax", 1, defLagTime],
			["sat", 0.5, defLagTime],
			["satMin", 0,  defLagTime],
			["satMax", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modSub1Level", 0, defLagTime],
			["modSub2Level", 0, defLagTime],
			["modSub3Level", 0, defLagTime],
			["modSub4Level", 0, defLagTime],
			["modFreq", 0, defLagTime],
			["modLag", 0, defLagTime],
			["modRes", 0, defLagTime],
			["modSat", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [26, 0];
		arrOptionData = [
			TXFilter.arrOptionData,
			[
				["None",
					{arg input, lagtime; input;}
				],
				["Linear",
					{arg input, lagtime; Ramp.kr(input, lagtime); }
				],
				["Exp 1",
					{arg input, lagtime; Lag.kr(input, lagtime); }
				],
				["Exp 2",
					{arg input, lagtime; Lag2.kr(input, lagtime); }
				],
				["Exp 3",
					{arg input, lagtime; Lag3.kr(input, lagtime); }
				],
			],
		];
		synthDefFunc = {
			arg in, out, outGain,
			sub1Level, sub1LevelMin, sub1LevelMax, sub2Level, sub2LevelMin, sub2LevelMax,
			sub3Level, sub3LevelMin, sub3LevelMax, sub4Level, sub4LevelMin, sub4LevelMax,
			freq, freqMin, freqMax, lag, lagMin, lagMax, res, resMin, resMax, sat, satMin, satMax,
			wetDryMix,
			modSub1Level = 0, modSub2Level = 0, modSub3Level = 0, modSub4Level = 0,
			modFreq = 0, modLag = 0, modRes = 0, modSat = 0,
			modWetDryMix = 0;

			var input, inputLevel, mixSum, levelScale;
			var sub1Out, sub2Out, sub3Out, sub4Out, sub1LevelSum, sub2LevelSum, sub3LevelSum, sub4LevelSum;
			var filterFunction, lagFunction, outFilter, outClean, sumfreq, sumlag, sumres, sumsat;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(startEnv * InFeedback.ar(in, 2));
			sub1LevelSum = (sub1Level + modSub1Level).max(0).min(1).linlin(0, 1, sub1LevelMin, sub1LevelMax);
			sub2LevelSum = (sub2Level + modSub2Level).max(0).min(1).linlin(0, 1, sub2LevelMin, sub2LevelMax);
			sub3LevelSum = (sub3Level + modSub3Level).max(0).min(1).linlin(0, 1, sub3LevelMin, sub3LevelMax);
			sub4LevelSum = (sub4Level + modSub4Level).max(0).min(1).linlin(0, 1, sub4LevelMin, sub4LevelMax);
			mixSum = (wetDryMix + modWetDryMix).max(0).min(1);
			levelScale = (sub1LevelSum + sub2LevelSum + sub3LevelSum + sub4LevelSum).max(1).reciprocal;
			inputLevel = Amplitude.ar(input);

			sub1Out =  (2 * ToggleFF.ar(PulseDivider.ar(input, 1)) * sub1LevelSum * inputLevel) - 1;
			sub2Out =  (2 * ToggleFF.ar(PulseDivider.ar(input, 2)) *  sub2LevelSum * inputLevel) - 1;
			sub3Out =  (2 * ToggleFF.ar(PulseDivider.ar(input, 3)) *  sub3LevelSum * inputLevel) - 1;
			sub4Out =  (2 * ToggleFF.ar(PulseDivider.ar(input, 4)) *  sub4LevelSum * inputLevel) - 1;

			sumfreq = ( (freqMax/ freqMin) ** ((freq + modFreq).max(0.001).min(1)) ) * freqMin;
			sumlag = ( (lagMax/lagMin) ** ((lag + modLag).max(0.001).min(1)) ) * lagMin;
			sumres =  resMin + ( (resMax - resMin) * (res + modRes).max(0).min(1) );
			sumsat =  satMin + ( (satMax - satMin) * (sat + modSat).max(0).min(1) );
			filterFunction = this.getSynthOption(0);
			lagFunction = this.getSynthOption(1);
			outFilter = TXClean.ar(filterFunction.value(
				(sub1Out + sub2Out + sub3Out + sub4Out) * levelScale,
				lagFunction.value(sumfreq, sumlag).max(30),  // limit min freq to 30
				sumres,
				sumsat
			));
			Out.ar(out, startEnv * LeakDC.ar(outFilter, 0.995, outGain * mixSum,  input * (1-mixSum)));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TXMinMaxSliderSplit", "Sub 1 Level", controlSpecLevel, "sub1Level", "sub1LevelMin", "sub1LevelMax"],
			["TXMinMaxSliderSplit", "Sub 2 Level", controlSpecLevel, "sub2Level", "sub2LevelMin", "sub2LevelMax"],
			["TXMinMaxSliderSplit", "Sub 3 Level", controlSpecLevel, "sub3Level", "sub3LevelMin", "sub3LevelMax"],
			["TXMinMaxSliderSplit", "Sub 4 Level", controlSpecLevel, "sub4Level", "sub4LevelMin", "sub4LevelMax"],
			["SynthOptionPopupPlusMinus", "Filter", arrOptionData, 0],
			["TXMinMaxFreqNoteSldr", "Frequency", ControlSpec(0.midicps, 20000, \exponential),
				"freq", "freqMin", "freqMax", nil, TXFilter.arrFreqRanges],
			["SynthOptionPopupPlusMinus", "Smoothing", arrOptionData, 1],
			["TXMinMaxSliderSplit", "Smooth time", ControlSpec(0.0001, 30, \exp, 0, 1, units: " secs"),
				"lag", "lagMin", "lagMax"],
			["TXMinMaxSliderSplit", "Resonance", ControlSpec(0, 1), "res", "resMin", "resMax"],
			["TXMinMaxSliderSplit", "Saturation", ControlSpec(0, 1), "sat", "satMin", "satMax"],
			["EZslider", "Out Gain", ControlSpec(0, 1), "outGain"],
			["WetDryMixSlider"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Subs", {displayOption = "showSubs";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showSubs")],
			["Spacer", 3],
			["ActionButton", "Filter", {displayOption = "showFilter";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showFilter")],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showSubs", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Sub 1 Level", controlSpecLevel, "sub1Level", "sub1LevelMin", "sub1LevelMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Sub 2 Level", controlSpecLevel, "sub2Level", "sub2LevelMin", "sub2LevelMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Sub 3 Level", controlSpecLevel, "sub3Level", "sub3LevelMin", "sub3LevelMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Sub 4 Level", controlSpecLevel, "sub4Level", "sub4LevelMin", "sub4LevelMax"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Out Gain", ControlSpec(0, 1), "outGain"],
				["SpacerLine", 4],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showFilter", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Filter", arrOptionData, 0],
				["SpacerLine", 4],
				["TXMinMaxFreqNoteSldr", "Frequency", ControlSpec(0.midicps, 20000, \exponential),
					"freq", "freqMin", "freqMax", nil, TXFilter.arrFreqRanges],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Smoothing", arrOptionData, 1],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Smooth time", ControlSpec(0.0001, 30, \exp, 0, 1, units: " secs"),
					"lag", "lagMin", "lagMax"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Resonance", ControlSpec(0, 1), "res", "resMin", "resMax"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Saturation", ControlSpec(0, 1), "sat", "satMin", "satMax"],
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

}

