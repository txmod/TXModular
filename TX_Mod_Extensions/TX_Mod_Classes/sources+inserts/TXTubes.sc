// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXTubes : TXModuleBase {

	classvar <>classData;

	var	displayOption;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Tubes";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Input gain", 1, "modInGain", 0],
			["Max length", 1, "modMaxLength", 0],
			["Loss 1", 1, "modLoss1", 0],
			["Length 1", 1, "modLength1", 0],
			["Scatter 1", 1, "modScatter1", 0],
			["Loss 2", 1, "modLoss2", 0],
			["Length 2", 1, "modLength2", 0],
			["Scatter 2", 1, "modScatter2", 0],
			["Loss 3", 1, "modLoss3", 0],
			["Length 3", 1, "modLength3", 0],
			["Scatter 3", 1, "modScatter3", 0],
			["Loss 4", 1, "modLoss4", 0],
			["Length 4", 1, "modLength4", 0],
			["Scatter 4", 1, "modScatter4", 0],
			["Loss 5", 1, "modLoss5", 0],
			["Length 5", 1, "modLength5", 0],
			["Scatter 5", 1, "modScatter5", 0],
			["Loss 6", 1, "modLoss6", 0],
			["Length 6", 1, "modLength6", 0],
			["Scatter 6", 1, "modScatter6", 0],
			["Loss 7", 1, "modLoss7", 0],
			["Length 7", 1, "modLength7", 0],
			["Scatter 7", 1, "modScatter7", 0],
			["Loss 8", 1, "modLoss8", 0],
			["Length 8", 1, "modLength8", 0],
			["Loss 9", 1, "modLoss9", 0],
			["Output gain", 1, "modOutGain", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.guiWidth = 580;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showGain";
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["inGain", 1, defLagTime],
			["lossMin", 0, defLagTime],
			["lossMax", 1, defLagTime],
			["scatterMin", -1, defLagTime],
			["scatterMax", 1, defLagTime],
			["maxLength", 0.15, defLagTime],
			["maxLengthMin", 0.001, defLagTime],
			["maxLengthMax", 1, defLagTime],
			["loss1", 0, defLagTime],
			["length1", 0.1, defLagTime],
			["scatter1", 0.5, defLagTime],
			["loss2", 0, defLagTime],
			["length2", 0.1, defLagTime],
			["scatter2", 0.5, defLagTime],
			["loss3", 0, defLagTime],
			["length3", 0.1, defLagTime],
			["scatter3", 0.5, defLagTime],
			["loss4", 0, defLagTime],
			["length4", 0.1, defLagTime],
			["scatter4", 0.5, defLagTime],
			["loss5", 0, defLagTime],
			["length5", 0.1, defLagTime],
			["scatter5", 0.5, defLagTime],
			["loss6", 0, defLagTime],
			["length6", 0.1, defLagTime],
			["scatter6", 0.5, defLagTime],
			["loss7", 0, defLagTime],
			["length7", 0.1, defLagTime],
			["scatter7", 0.5, defLagTime],
			["loss8", 0, defLagTime],
			["length8", 0.1, defLagTime],
			["loss9", 0, defLagTime],
			["outGain", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modInGain", 0, defLagTime],
			["modMaxLength", 0, defLagTime],
			["modLoss1", 0, defLagTime],
			["modLength1", 0, defLagTime],
			["modScatter1", 0, defLagTime],
			["modLoss2", 0, defLagTime],
			["modLength2", 0, defLagTime],
			["modScatter2", 0, defLagTime],
			["modLoss3", 0, defLagTime],
			["modLength3", 0, defLagTime],
			["modScatter3", 0, defLagTime],
			["modLoss4", 0, defLagTime],
			["modLength4", 0, defLagTime],
			["modScatter4", 0, defLagTime],
			["modLoss5", 0, defLagTime],
			["modLength5", 0, defLagTime],
			["modScatter5", 0, defLagTime],
			["modLoss6", 0, defLagTime],
			["modLength6", 0, defLagTime],
			["modScatter6", 0, defLagTime],
			["modLoss7", 0, defLagTime],
			["modLength7", 0, defLagTime],
			["modScatter7", 0, defLagTime],
			["modLoss8", 0, defLagTime],
			["modLength8", 0, defLagTime],
			["modLoss9", 0, defLagTime],
			["modOutGain", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [2, 2]; // init: no tubes: 4, level control: limiter
		arrOptionData = [
			[
				["2 Tubes = 3 Losses, 2 Lengths, 1 Scatter", 2],
				["3 Tubes = 4 Losses, 3 Lengths, 2 Scatters", 3],
				["4 Tubes = 5 Losses, 4 Lengths, 3 Scatters", 4],
				["5 Tubes = 6 Losses, 5 Lengths, 4 Scatters", 5],
				["6 Tubes = 7 Losses, 6 Lengths, 5 Scatters", 6],
				["7 Tubes = 8 Losses, 7 Lengths, 6 Scatters", 7],
				["8 Tubes = 9 Losses, 8 Lengths, 7 Scatters", 8],
			],
			TXLevelControl.arrOptionData,
		];
		synthDefFunc = { arg in, out, inGain, lossMin, lossMax,
			scatterMin, scatterMax, maxLength, maxLengthMin, maxLengthMax,
			loss1, length1, scatter1, loss2, length2, scatter2,
			loss3, length3, scatter3, loss4, length4, scatter4,
			loss5, length5, scatter5, loss6, length6, scatter6,
			loss7, length7, scatter7, loss8, length8, loss9,
			outGain, wetDryMix,
			modInGain = 0, modMaxLength = 0,
			modLoss1 = 0, modLength1 = 0, modScatter1 = 0,
			modLoss2 = 0, modLength2 = 0, modScatter2 = 0,
			modLoss3 = 0, modLength3 = 0, modScatter3 = 0,
			modLoss4 = 0, modLength4 = 0, modScatter4 = 0,
			modLoss5 = 0, modLength5 = 0, modScatter5 = 0,
			modLoss6 = 0, modLength6 = 0, modScatter6 = 0,
			modLoss7 = 0, modLength7 = 0, modScatter7 = 0,
			modLoss8 = 0, modLength8 = 0, modLoss9 = 0,
			modOutGain = 0, modWetDryMix = 0;
			var inGainSum, input, layerCount, holdSignal, levelControlFunc, inputDelayed;
			var maxLengthSum, mixSum, outGainSum, outSound;
			var arrAllLosses, arrAllLengths, arrAllScatters;
			var arrLosses, arrLengths, arrScatters, arrTubes;
			var minTubeLength = SampleDur.ir * 2; // according to NTube help file
			var startEnv = TXEnvPresets.startEnvFunc.value;

			inGainSum = (inGain + modInGain).max(0).min(2);
			input = TXClean.ar(startEnv * InFeedback.ar(in,1));
			maxLengthSum = (maxLength + modMaxLength).max(0.01).min(1).linexp(0.01, 1, maxLengthMin, maxLengthMax);
			mixSum = (wetDryMix + modWetDryMix).max(0).min(1);
			outGainSum = (outGain + modOutGain).max(0).min(1);
			layerCount = this.getSynthOption(0);
			arrAllLosses = [
				(loss1 + modLoss1).max(0).min(1),
				(loss2 + modLoss2).max(0).min(1),
				(loss3 + modLoss3).max(0).min(1),
				(loss4 + modLoss4).max(0).min(1),
				(loss5 + modLoss5).max(0).min(1),
				(loss6 + modLoss6).max(0).min(1),
				(loss7 + modLoss7).max(0).min(1),
				(loss8 + modLoss8).max(0).min(1),
				(loss9 + modLoss9).max(0).min(1),
			];
			arrAllScatters = [
				(scatter1 + modScatter1).max(0).min(1),
				(scatter2 + modScatter2).max(0).min(1),
				(scatter3 + modScatter3).max(0).min(1),
				(scatter4 + modScatter4).max(0).min(1),
				(scatter5 + modScatter5).max(0).min(1),
				(scatter6 + modScatter6).max(0).min(1),
				(scatter7 + modScatter7).max(0).min(1),
			];
			arrAllLengths = [
				(length1 + modLength1).max(0).min(1),
				(length2 + modLength2).max(0).min(1),
				(length3 + modLength3).max(0).min(1),
				(length4 + modLength4).max(0).min(1),
				(length5 + modLength5).max(0).min(1),
				(length6 + modLength6).max(0).min(1),
				(length7 + modLength7).max(0).min(1),
				(length8 + modLength8).max(0).min(1),
			];
			arrLosses = 1 - (lossMin + (arrAllLosses.copy.keep(layerCount + 1) * (lossMax - lossMin)));
			arrScatters = scatterMin + (arrAllScatters.copy.keep(layerCount - 1) * (scatterMax - scatterMin));
			arrLengths = (minTubeLength + (arrAllLengths.copy.keep(layerCount) * maxLengthSum));
			holdSignal = NTube.ar(input * inGainSum, `arrLosses, `arrScatters, `arrLengths);
			outSound = holdSignal * outGainSum / layerCount;
			levelControlFunc = this.getSynthOption(1);
			inputDelayed = TXLevelControl.arrDelayFunctions[arrOptions[1]].value(input);
			Out.ar(out, startEnv * ((levelControlFunc.value(outSound, input) * mixSum) + (inputDelayed * (1-mixSum))));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["SynthOptionPopup", "Complexity", arrOptionData, 0],
			["EZslider", "In gain", ControlSpec(0, 2), "inGain"],
			["TXRangeSlider", "Loss range", ControlSpec(0, 1), "lossMin", "lossMax"],
			["TXMinMaxSliderSplit", "Max length", ControlSpec(0.01, 1, 'exp'), "maxLength",
				"maxLengthMin", "maxLengthMax"],
			["TXRangeSlider", "Scatter range", ControlSpec(-1, 1), "scatterMin", "scatterMax"],
		]
		++ ["loss1", "loss2", "loss3", "loss4", "loss5", "loss6", "loss7", "loss8", "loss9",].collect({arg item, i;
			["EZsliderUnmapped", item, {ControlSpec(this.getSynthArgSpec("lossMin"), this.getSynthArgSpec("lossMax"))}, item]
		})
		++ ["length1", "length2", "length3", "length4", "length5", "length6", "length7", "length8",].collect({arg item, i;
			["EZsliderUnmapped", item, {ControlSpec(0, this.getSynthArgSpec("maxLength").linexp(0, 1,
				this.getSynthArgSpec("maxLengthMin"), this.getSynthArgSpec("maxLengthMax")))}, item]
		})
		++ ["scatter1", "scatter2", "scatter3", "scatter4", "scatter5", "scatter6", "scatter7",].collect({arg item, i;
			["EZsliderUnmapped", item, {ControlSpec(this.getSynthArgSpec("scatterMin"), this.getSynthArgSpec("scatterMax"))}, item]
		})
		++ [
			["EZslider", "Out gain", ControlSpec(0, 1), "outGain"],
			["SynthOptionPopupPlusMinus", "Level control", arrOptionData, 1],
			["WetDryMixSlider"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	buildGuiSpecArray {
		var holdLossMin = this.getSynthArgSpec("lossMin");
		var holdLossMax = this.getSynthArgSpec("lossMax");
		var holdScatterMin = this.getSynthArgSpec("scatterMin");
		var holdScatterMax = this.getSynthArgSpec("scatterMax");
		var holdMaxLength = this.getSynthArgSpec("maxLength").linexp(0, 1,
			this.getSynthArgSpec("maxLengthMin"), this.getSynthArgSpec("maxLengthMax"));

		guiSpecArray = [
			["ActionButton", "Gain", {displayOption = "showGain";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showGain")],
			["Spacer", 3],
			["ActionButton", "Parameters", {displayOption = "showParameters"; this.buildGuiSpecArray;
				system.showView;}, 130, TXColor.white, this.getButtonColour(displayOption == "showParameters")],
			["Spacer", 3],
			["ActionButton", "Tubes", {displayOption = "showTubes";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showTubes")],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 2],
		];

		if (displayOption == "showGain", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["EZslider", "In gain", ControlSpec(0, 2), "inGain"],
				["SpacerLine", 20],
				["EZslider", "Out gain", ControlSpec(0, 1), "outGain"],
				["SpacerLine", 20],
				["SynthOptionPopupPlusMinus", "Level control", arrOptionData, 1],
				["SpacerLine", 20],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showParameters", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["TXRangeSlider", "Loss range", ControlSpec(0, 1), "lossMin", "lossMax"],
				["SpacerLine", 20],
				["TXMinMaxSliderSplit", "Max length", ControlSpec(0.01, 1, 'exp'), "maxLength",
					"maxLengthMin", "maxLengthMax"],
				["SpacerLine", 20],
				["TXRangeSlider", "Scatter range", ControlSpec(-1, 1), "scatterMin", "scatterMax"],
			];
		});
		if (displayOption == "showTubes", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Complexity", arrOptionData, 0],
				["SpacerLine", 20],
				["TextBar", "Count", 80],
				["TextBar", "1", 50],
				["TextBar", "2", 50],
				["TextBar", "3", 50],
				["TextBar", "4", 50],
				["TextBar", "5", 50],
				["TextBar", "6", 50],
				["TextBar", "7", 50],
				["TextBar", "8", 50],
				["TextBar", "9", 50],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["loss1", "loss2", "loss3", "loss4",
					"loss5", "loss6", "loss7", "loss8", "loss9", ]);}, 36],
				["TXMultiKnobNoUnmap", "Loss", ["loss1", "loss2", "loss3", "loss4",
					"loss5", "loss6", "loss7", "loss8", "loss9", ], 9, ControlSpec(holdLossMin, holdLossMax)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["length1", "length2", "length3", "length4",
					"length5", "length6", "length7", "length8", ]);}, 36],
				["TXMultiKnobNoUnmap", "Length", ["length1", "length2", "length3", "length4",
					"length5", "length6", "length7", "length8", ], 8, ControlSpec(0.001, holdMaxLength)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["scatter1", "scatter2", "scatter3", "scatter4",
					"scatter5", "scatter6", "scatter7", ]);}, 36],
				["TXMultiKnobNoUnmap", "Scatter", ["scatter1", "scatter2", "scatter3", "scatter4",
					"scatter5", "scatter6", "scatter7", ], 7, ControlSpec(holdScatterMin, holdScatterMax)],
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

