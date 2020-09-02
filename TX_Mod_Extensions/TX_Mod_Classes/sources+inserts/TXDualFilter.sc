// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXDualFilter : TXModuleBase {

	classvar <>classData;
	var	displayOption;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Dual Filter";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Morph/Mix", 1, "modmorph", 0],
			["Smooth Time", 1, "modSmooth", 0],
			["Frequency A", 1, "modfreqA", 0],
			["Frequency B", 1, "modfreqB", 0],
			["Ratio B", 1, "modratioB", 0],
			["Resonance A", 1, "modresA", 0],
			["Resonance B", 1, "modresV", 0],
			["Saturation A", 1, "modsatA", 0],
			["Saturation B", 1, "modsatB", 0],
			["Gain A", 1, "modgainA", 0],
			["Gain B", 1, "modgainB", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.ratioSpec = ControlSpec(0.01, 100, \exponential);
		classData.freqSpec = ControlSpec(0.midicps, 20000, \exponential);
		classData.guiWidth = 540;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showGlobal";
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["gainA", 0.5, defLagTime],
			["gainAMin", 0,  defLagTime],
			["gainAMax", 2, defLagTime],
			["gainB", 0.5, defLagTime],
			["gainBMin", 0,  defLagTime],
			["gainBMax", 2, defLagTime],
			["freqA", 0.5, defLagTime],
			["freqAMin",40, defLagTime],
			["freqAMax", 20000, defLagTime],
			["freqB", 0.5, defLagTime],
			["freqBMin",40, defLagTime],
			["freqBMax", 20000, defLagTime],
			["ratioB", ControlSpec(0.1, 10, \exponential).unmap(0.5), defLagTime],
			["ratioBMin", 0.1,  defLagTime],
			["ratioBMax", 10, defLagTime],
			["smooth", 0.5, defLagTime],
			["smoothMin", 0.01, defLagTime],
			["smoothMax", 1, defLagTime],
			["resA", 0.5, defLagTime],
			["resAMin", 0,  defLagTime],
			["resAMax", 1, defLagTime],
			["resB", 0.5, defLagTime],
			["resBMin", 0,  defLagTime],
			["resBMax", 1, defLagTime],
			["satA", 0.5, defLagTime],
			["satAMin", 0,  defLagTime],
			["satAMax", 1, defLagTime],
			["satB", 0.5, defLagTime],
			["satBMin", 0,  defLagTime],
			["satBMax", 1, defLagTime],
			["morph", 0, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modgainA", 0, defLagTime],
			["modgainB", 0, defLagTime],
			["modfreqA", 0, defLagTime],
			["modfreqB", 0, defLagTime],
			["modratioB", 0, defLagTime],
			["modSmooth", 0, defLagTime],
			["modresA", 0, defLagTime],
			["modresB", 0, defLagTime],
			["modsatA", 0, defLagTime],
			["modsatB", 0, defLagTime],
			["modmorph", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = 0 ! 9;
		arrOptionData = [
			// ind0 filter A type
			TXFilter.arrOptionData,
			// ind1 filter B type
			TXFilter.arrOptionData,
			// ind2 smoothing
			[
				["None",
					{arg input, smoothtime; input;}
				],
				["Linear",
					{arg input, smoothtime; Ramp.kr(input, smoothtime); }
				],
				["Exponential 1",
					{arg input, smoothtime; Lag.kr(input, smoothtime); }
				],
				["Exponential 2",
					{arg input, smoothtime; Lag2.kr(input, smoothtime); }
				],
				["Exponential 3",
					{arg input, smoothtime; Lag3.kr(input, smoothtime); }
				],
			],
			// ind3 level control
			TXLevelControl.arrOptionData,
			// ind4 Filter Mode
			[
				["Filter Mix:  Mix between A and B", {
					arg inSound, morph, filterAFunction, filterBFunction,
					sumgainA, sumgainB, sumfreqA, sumfreqB, sumresA, sumresB, sumsatA, sumsatB;
					var outA = sumgainA * filterAFunction.value(inSound, sumfreqA, sumresA, sumsatA);
					var outB = sumgainB * filterBFunction.value(inSound, sumfreqB, sumresB, sumsatB);
					((1 - morph) * outA) + (morph * outB);
				}],
				["Filter Morph:  Morph between A and B [Filter type B ignored]", {
					arg inSound, morph, filterAFunction, filterBFunction,
					sumgainA, sumgainB, sumfreqA, sumfreqB, sumresA, sumresB, sumsatA, sumsatB;
					sumgainA.blend(sumgainB, morph) * filterAFunction.value(inSound, sumfreqA.blend(sumfreqB, morph),
						sumresA.blend(sumresB, morph), sumsatA.blend(sumsatB, morph));
				}],
				["Filters in Series:  A is filtered by B  [Morph/Mix ignored]", {
					arg inSound, morph, filterAFunction, filterBFunction,
					sumgainA, sumgainB, sumfreqA, sumfreqB, sumresA, sumresB, sumsatA, sumsatB;
					var outA = sumgainA * filterAFunction.value(inSound, sumfreqA, sumresA, sumsatA);
					sumgainB * filterBFunction.value(outA, sumfreqB, sumresB, sumsatB);
				}],
				["Subtract Filters:  Subtract B from A  [Morph/Mix ignored]", {
					arg inSound, morph, filterAFunction, filterBFunction,
					sumgainA, sumgainB, sumfreqA, sumfreqB, sumresA, sumresB, sumsatA, sumsatB;
					var outA = sumgainA * filterAFunction.value(inSound, sumfreqA, sumresA, sumsatA);
					var outB = sumgainB * filterBFunction.value(inSound, sumfreqB, sumresB, sumsatB);
					outA -outB;
				}],
			],
			// ind5 Use Gain A for Gain B
			[
				["Off", {arg inA, inB; inB;} ],
				["On", {arg inA, inB; inA;} ],
			],
			// ind6 Use Ratio B instead of Freq B
			[
				["Off", {arg inA, inB; inB;} ],
				["On", {arg inA, inB; inA;} ],
			],
			// ind7 Use Res A for Res B
			[
				["Off", {arg inA, inB; inB;} ],
				["On", {arg inA, inB; inA;} ],
			],
			// ind8 Use Saturation A for Saturation B
			[
				["Off", {arg inA, inB; inB;} ],
				["On", {arg inA, inB; inA;} ],
			],
		];
		synthDefFunc = { arg in, out, gainA, gainAMin, gainAMax, gainB, gainBMin, gainBMax,
			freqA, freqAMin, freqAMax, freqB, freqBMin, freqBMax, ratioB, ratioBMin, ratioBMax,
			smooth, smoothMin, smoothMax,
			resA, resAMin, resAMax, resB, resBMin, resBMax,
			satA, satAMin, satAMax, satB, satBMin, satBMax, morph, wetDryMix,
			modgainA = 0, modgainB = 0,  modfreqA = 0, modfreqB = 0, modratioB = 0, modSmooth = 0,
			modresA = 0, modresB = 0, modsatA = 0, modsatB = 0,  modmorph = 0, modWetDryMix = 0;

			var input, joinFunction, filterAFunction, filterBFunction, smoothFunction, outFilter,
			sumgainA, sumgainB, sumfreqA, sumfreqB, sumratioB, sumsmooth, sumresA, sumresB, sumsatA, sumsatB,
			summorph, levelControlFunc, inputDelayed, mixCombined, outGainB, outFreqB, outResB, outSatB;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(startEnv * InFeedback.ar(in, classData.noInChannels));
			sumgainA =  gainAMin + ( (gainAMax - gainAMin) * (gainA + modgainA).max(0).min(1) );
			sumgainB =  gainBMin + ( (gainBMax - gainBMin) * (gainB + modgainB).max(0).min(1) );
			sumfreqA = ( (freqAMax/ freqAMin) ** ((freqA + modfreqA).max(0.001).min(1)) ) * freqAMin;
			sumfreqB = ( (freqBMax/ freqBMin) ** ((freqB + modfreqB).max(0.001).min(1)) ) * freqBMin;
			sumratioB = ( (ratioBMax/ratioBMin) ** ((ratioB + modratioB).max(0.001).min(1)) ) * ratioBMin;
			sumsmooth = ( (smoothMax/smoothMin) ** ((smooth + modSmooth).max(0.001).min(1)) ) * smoothMin;
			sumresA =  resAMin + ( (resAMax - resAMin) * (resA + modresA).max(0).min(1) );
			sumresB =  resBMin + ( (resBMax - resBMin) * (resB + modresB).max(0).min(1) );
			sumsatA =  satAMin + ( (satAMax - satAMin) * (satA + modsatA).max(0).min(1) );
			sumsatB =  satBMin + ( (satBMax - satBMin) * (satB + modsatB).max(0).min(1) );
			summorph = (morph + modmorph).max(0).min(1);
			filterAFunction = this.getSynthOption(0);
			filterBFunction = this.getSynthOption(1);
			smoothFunction = this.getSynthOption(2);
			levelControlFunc = this.getSynthOption(3);
			joinFunction = this.getSynthOption(4);
			outGainB = this.getSynthOption(5).value(sumgainA, sumgainB);
			outFreqB = this.getSynthOption(6).value(sumratioB * sumfreqA, sumfreqB);
			outResB = this.getSynthOption(7).value(sumresA, sumresB);
			outSatB= this.getSynthOption(8).value(sumsatA, sumsatB);
			inputDelayed = TXLevelControl.arrDelayFunctions[arrOptions[2]].value(input);
			outFilter = TXClean.ar(joinFunction.value(
				input,
				morph,
				filterAFunction,
				filterBFunction,
				sumgainA,
				outGainB,
				smoothFunction.value(sumfreqA, sumsmooth).max(30),  // limit min freq to 30
				smoothFunction.value(outFreqB, sumsmooth).max(30),  // limit min freq to 30
				sumresA,
				outResB,
				sumsatA,
				outSatB
			));
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			Out.ar(out, (startEnv * levelControlFunc.value(outFilter, input) * mixCombined) + (inputDelayed * (1-mixCombined)) );
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["SynthOptionListPlusMinus", "Filter Mode", arrOptionData, 4],
			["EZslider", "Morph/Mix", ControlSpec(0, 1), "morph"],
			["SynthOptionCheckBox", "Use Ratio B instead of Freq B", arrOptionData, 6, 240],
			["SynthOptionCheckBox", "Use Resonance A for Resonance B", arrOptionData, 7, 240],
			["SynthOptionCheckBox", "Use Saturation A for Saturation B", arrOptionData, 8, 240],
			["SynthOptionCheckBox", "Use Gain A for Gain B", arrOptionData, 5, 240],
			["SynthOptionPopupPlusMinus", "Level control", arrOptionData, 3],
			["WetDryMixSlider"],
			["SynthOptionListPlusMinus", "Filter type type A", arrOptionData, 0],
			["TXMinMaxFreqNoteSldr", "Frequency A", classData.freqSpec,
				"freqA", "freqAMin", "freqAMax", nil, TXFilter.arrFreqRanges],
			["TXMinMaxSliderSplit", "Resonance A", ControlSpec(0, 1), "resA", "resAMin", "resAMax"],
			["TXMinMaxSliderSplit", "Saturation A", ControlSpec(0, 1), "satA", "satAMin", "satAMax"],
			["TXMinMaxSliderSplit", "Gain A", ControlSpec(0, 1), "gainA", "gainAMin", "gainAMax"],
			["SynthOptionListPlusMinus", "Filter type type B", arrOptionData, 1],
			["TXMinMaxFreqNoteSldr", "Frequency B", classData.freqSpec,
				"freqB", "freqBMin", "freqBMax", nil, TXFilter.arrFreqRanges],
			["TXMinMaxSliderSplit", "Ratio B", classData.ratioSpec, "ratioB", "ratioBMin", "ratioBMax"],
			["TXMinMaxSliderSplit", "Resonance B", ControlSpec(0, 1), "resB", "resBMin", "resBMax"],
			["TXMinMaxSliderSplit", "Saturation B", ControlSpec(0, 1), "satB", "satBMin", "satBMax"],
			["TXMinMaxSliderSplit", "Gain B", ControlSpec(0, 1), "gainB", "gainBMin", "gainBMax"],
			["SynthOptionPopupPlusMinus", "Freq Smooth", arrOptionData, 2],
			["TXMinMaxSliderSplit", "Smooth time", ControlSpec(0.0001, 30, \exp, 0, 1, units: " secs"),
				"smooth", "smoothMin", "smoothMax"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Global", {displayOption = "showGlobal";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showGlobal")],
			["Spacer", 3],
			["ActionButton", "Filter A", {displayOption = "showFilterA";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showFilterA")],
			["Spacer", 3],
			["ActionButton", "Filter B", {displayOption = "showFilterB";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showFilterB")],
			["Spacer", 3],
			["ActionButton", "Filters A & B", {displayOption = "showFilterAB";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showFilterAB")],
			["DividingLine"],
			["SpacerLine", 2],
		];

		if (displayOption == "showGlobal", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Filter Mode", arrOptionData, 4, nil, 60],
				["SpacerLine", 2],
				["EZslider", "Morph/Mix", ControlSpec(0, 1), "morph"],
				["DividingLine"],
				["SpacerLine", 6],
				["SynthOptionPopupPlusMinus", "Freq Smooth", arrOptionData, 2],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Smooth time", ControlSpec(0.0001, 30, \exp, 0, 1, units: " secs"),
					"smooth", "smoothMin", "smoothMax"],
				["DividingLine"],
				["SpacerLine", 6],
				["SynthOptionPopupPlusMinus", "Level control", arrOptionData, 3],
				["SpacerLine", 6],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showFilterA", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Filter Mode", arrOptionData, 4],
				["EZslider", "Morph/Mix", ControlSpec(0, 1), "morph"],
				["DividingLine"],
				["SynthOptionListPlusMinus", "Filter type A", arrOptionData, 0, nil, 160],
				["SpacerLine", 4],
				["TXMinMaxFreqNoteSldr", "Frequency A", classData.freqSpec,
					"freqA", "freqAMin", "freqAMax", nil, TXFilter.arrFreqRanges],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Resonance A", ControlSpec(0, 1), "resA", "resAMin", "resAMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Saturation A", ControlSpec(0, 1), "satA", "satAMin", "satAMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Gain A", ControlSpec(0, 2), "gainA", "gainAMin", "gainAMax"],
			];
		});
		if (displayOption == "showFilterB", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Filter Mode", arrOptionData, 4],
				["EZslider", "Morph/Mix", ControlSpec(0, 1), "morph"],
				["DividingLine"],
				["SynthOptionListPlusMinus", "Filter type B", arrOptionData, 1, nil, 116],
				["SpacerLine", 4],
				["TXMinMaxFreqNoteSldr", "Frequency B", classData.freqSpec,
					"freqB", "freqBMin", "freqBMax", nil, TXFilter.arrFreqRanges],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Ratio B", classData.ratioSpec, "ratioB", "ratioBMin", "ratioBMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Resonance B", ControlSpec(0, 1), "resB", "resBMin", "resBMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Saturation B", ControlSpec(0, 1), "satB", "satBMin", "satBMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Gain B", ControlSpec(0, 2), "gainB", "gainBMin", "gainBMax"],
				["DividingLine"],
				["SpacerLine", 1],
				["SynthOptionCheckBox", "Use Ratio B x Freq A for Freq B", arrOptionData, 6, 250],
				["SynthOptionCheckBox", "Use Resonance A for Resonance B", arrOptionData, 7, 250],
				["SynthOptionCheckBox", "Use Saturation A for Saturation B", arrOptionData, 8, 250],
				["SynthOptionCheckBox", "Use Gain A for Gain B", arrOptionData, 5, 250],
			];
		});
		if (displayOption == "showFilterAB", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Filter Mode", arrOptionData, 4],
				["EZslider", "Morph/Mix", ControlSpec(0, 1), "morph"],
				["DividingLine"],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Filter type A", arrOptionData, 0],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Filter type B", arrOptionData, 1],
				["DividingLine"],
				["SpacerLine", 4],
				["EZsliderUnmapped", "Frequency A",
					{ControlSpec(this.getSynthArgSpec("freqAMin"), this.getSynthArgSpec("freqAMax"), 'exp')},
					"freqA"],
				["EZsliderUnmapped", "Frequency B",
					{ControlSpec(this.getSynthArgSpec("freqBMin"), this.getSynthArgSpec("freqBMax"), 'exp')},
					"freqB"],
				["EZsliderUnmapped", "Ratio B",
					{ControlSpec(this.getSynthArgSpec("ratioBMin"), this.getSynthArgSpec("ratioBMax"), 'exp')},
					"ratioB"],
				["DividingLine"],
				["SpacerLine", 4],
				["EZsliderUnmapped", "Resonance A",
					{ControlSpec(this.getSynthArgSpec("resAMin"), this.getSynthArgSpec("resAMax"), 'lin')},
					"resA"],
				["EZsliderUnmapped", "Resonance B",
					{ControlSpec(this.getSynthArgSpec("resBMin"), this.getSynthArgSpec("resBMax"), 'lin')},
					"resB"],
				["DividingLine"],
				["SpacerLine", 4],
				["EZsliderUnmapped", "Saturation A",
					{ControlSpec(this.getSynthArgSpec("satAMin"), this.getSynthArgSpec("satAMax"), 'lin')},
					"satA"],
				["EZsliderUnmapped", "Saturation B",
					{ControlSpec(this.getSynthArgSpec("satBMin"), this.getSynthArgSpec("satBMax"), 'lin')},
					"satB"],
				["DividingLine"],
				["SpacerLine", 4],
				["EZsliderUnmapped", "Gain A",
					{ControlSpec(this.getSynthArgSpec("gainAMin"), this.getSynthArgSpec("gainAMax"), 'lin')},
					"gainA"],
				["EZsliderUnmapped", "Gain B",
					{ControlSpec(this.getSynthArgSpec("gainBMin"), this.getSynthArgSpec("gainBMax"), 'lin')},
					"gainB"],
				["DividingLine"],
				["SpacerLine", 4],
				["SynthOptionCheckBox", "Use Ratio B x Freq A for Freq B", arrOptionData, 6, 250],
				["SynthOptionCheckBox", "Use Resonance A for Resonance B", arrOptionData, 7, 250],
				["SynthOptionCheckBox", "Use Saturation A for Saturation B", arrOptionData, 8, 250],
				["SynthOptionCheckBox", "Use Gain A for Gain B", arrOptionData, 5, 250],
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

