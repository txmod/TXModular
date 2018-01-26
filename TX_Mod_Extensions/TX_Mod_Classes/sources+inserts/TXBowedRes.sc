// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

/*
DWGSoundBoard.ar (inp: 0, c1: 20, c3: 20, mix: 0.8, d1: 199, d2: 211, d3: 223, d4: 227, d5: 229, d6: 233, d7: 239, d8: 241)
*/

TXBowedRes : TXModuleBase {

	classvar <>classData;

	var	displayOption;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Bowed Res";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Input gain", 1, "modInGain", 0],
			["Quantise", 1, "modQuantise", 0],
			["Freq 1", 1, "modFreq1", 0],
			["Amp 1", 1, "modAmp1", 0],
			["Res 1", 1, "modRes1", 0],
			["Freq 2", 1, "modFreq2", 0],
			["Amp 2", 1, "modAmp2", 0],
			["Res 2", 1, "modRes2", 0],
			["Freq 3", 1, "modFreq3", 0],
			["Amp 3", 1, "modAmp3", 0],
			["Res 3", 1, "modRes3", 0],
			["Freq 4", 1, "modFreq4", 0],
			["Amp 4", 1, "modAmp4", 0],
			["Res 4", 1, "modRes4", 0],
			["Freq Hi-cut", 1, "modFreqHiCut", 0],
			["Output gain", 1, "modOutGain", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.specC = ControlSpec(0.01, 100, 2);
		classData.specD = ControlSpec(16, 1024, 2);
		classData.specFreqHiCut = ControlSpec(40, 22000, 'exp');
		classData.specRes = ControlSpec(0.1, 10);
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
			["freqMin", 0, defLagTime],
			["freqMax", 127, defLagTime],
			["resMin", 0.25, defLagTime],
			["resMax", 4, defLagTime],
			["quantise", 0, defLagTime],

			[ "c1", 20, \ir],
			[ "c3", 20, \ir],
			[ "mix", 0.8, \ir],
			[ "d1", 199, \ir],
			[ "d2", 211, \ir],
			[ "d3", 223, \ir],
			[ "d4", 227, \ir],
			[ "d5", 229, \ir],
			[ "d6", 233, \ir],
			[ "d7", 239, \ir],
			[ "d8", 241, \ir],

			["freq1", 46.2 / 127, defLagTime],
			["amp1", 0.75, defLagTime],
			["res1", ControlSpec(0.25, 4).unmap(1), defLagTime],
			["freq2", 68.6 / 127, defLagTime],
			["amp2", 0.75, defLagTime],
			["res2", ControlSpec(0.25, 4).unmap(1), defLagTime],
			["freq3", 70.86 / 127, defLagTime],
			["amp3", 0.75, defLagTime],
			["res3", ControlSpec(0.25, 4).unmap(1), defLagTime],
			["freq4", 77.86 / 127, defLagTime],
			["amp4", 0.25, defLagTime],
			["res4", ControlSpec(0.25, 4).unmap(1), defLagTime],
			["freqHiCut", classData.specFreqHiCut.unmap(8000), defLagTime],
			["freqHiCutMin", 40, defLagTime],
			["freqHiCutMax", 22000, defLagTime],

			["outGain", 0.5, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modInGain", 0, defLagTime],
			["modQuantise", 0, defLagTime],
			["modFreq1", 0, defLagTime],
			["modAmp1", 0, defLagTime],
			["modRes1", 0, defLagTime],
			["modFreq2", 0, defLagTime],
			["modAmp2", 0, defLagTime],
			["modRes2", 0, defLagTime],
			["modFreq3", 0, defLagTime],
			["modAmp3", 0, defLagTime],
			["modRes3", 0, defLagTime],
			["modFreq4", 0, defLagTime],
			["modAmp4", 0, defLagTime],
			["modRes4", 0, defLagTime],
			["modFreqHiCut", 0, defLagTime],
			["modOutGain", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0];
		arrOptionData = [
			TXLevelControl.arrOptionData,
		];
		synthDefFunc = { arg in, out, inGain, freqMin, freqMax, resMin, resMax, quantise,
			c1, c3, mix, d1, d2, d3, d4, d5, d6, d7, d8,
			freq1, amp1, res1, freq2, amp2, res2, freq3, amp3, res3, freq4, amp4, res4,
			freqHiCut, freqHiCutMin, freqHiCutMax, outGain, wetDryMix,
			modInGain = 0, modQuantise = 0,
			modFreq1 = 0, modAmp1 = 0, modRes1 = 0, modFreq2 = 0, modAmp2 = 0, modRes2 = 0,
			modFreq3 = 0, modAmp3 = 0, modRes3 = 0, modFreq4 = 0, modAmp4 = 0, modRes4 = 0,
			modFreqHiCut = 0, modOutGain = 0, modWetDryMix = 0;

			var input, inGainSum, outGainSum, mixSum, soundBoard, outFreqHiCut, outSound, levelControlFunc, inputDelayed;
			var arrFreqs, arrFreqsQuant, arrAmps, arrReses, outQuant;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			inGainSum = (inGain + modInGain).max(0).min(1);
			outGainSum = (outGain + modOutGain).max(0).min(1);
			mixSum = (wetDryMix + modWetDryMix).max(0).min(1);
			input = TXClean.ar(startEnv * InFeedback.ar(in,1));
			outQuant = (quantise + modQuantise).max(0).min(1);
			arrFreqs = [
				(freq1 + modFreq1).max(0).min(1),
				(freq2 + modFreq2).max(0).min(1),
				(freq3 + modFreq3).max(0).min(1),
				(freq4 + modFreq4).max(0).min(1),
				(freqHiCut + modFreqHiCut).max(0).min(1),
			];
			arrAmps = [
				(amp1 + modAmp1).max(0).min(1),
				(amp2 + modAmp2).max(0).min(1),
				(amp3 + modAmp3).max(0).min(1),
				(amp4 + modAmp4).max(0).min(1),
			];
			arrReses = [
				(res1 + modRes1).max(0).min(1),
				(res2 + modRes2).max(0).min(1),
				(res3 + modRes3).max(0).min(1),
				(res4 + modRes4).max(0).min(1),
			];
			arrReses =  arrReses.linlin(0, 1, resMin, resMax);
			arrFreqsQuant = arrFreqs.linlin(0, 1, freqMin, freqMax).round(outQuant);
			outFreqHiCut = LinExp.kr((freqHiCut + modFreqHiCut).max(0).min(1), 0, 1, freqHiCutMin, freqHiCutMax);
			// DWGSoundBoard.ar (inp: 0, c1: 20, c3: 20, mix: 0.8,
			//    d1: 199, d2: 211, d3: 223, d4: 227, d5: 229, d6: 233, d7: 239, d8: 241)
			soundBoard = DWGSoundBoard.ar(input * inGainSum, c1, c3, mix, d1, d2, d3, d4, d5, d6, d7, d8);
			outSound = LPF.ar(soundBoard
				+ BPF.ar(soundBoard, arrFreqsQuant[0], arrReses[0].reciprocal, arrAmps[0])
				+ BPF.ar(soundBoard, arrFreqsQuant[1], arrReses[1].reciprocal, arrAmps[1])
				+ BPF.ar(soundBoard, arrFreqsQuant[2], arrReses[2].reciprocal, arrAmps[2])
				+ BPF.ar(soundBoard, arrFreqsQuant[3], arrReses[3].reciprocal, arrAmps[3]),
				outFreqHiCut);
			levelControlFunc = this.getSynthOption(0);
			inputDelayed = TXLevelControl.arrDelayFunctions[arrOptions[0]].value(input);
			Out.ar(out, (startEnv * levelControlFunc.value(outSound * outGainSum, input) * mixSum) + (inputDelayed * (1-mixSum)));
		};

		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["EZslider", "In gain", ControlSpec(0, 1), "inGain"],
			["EZslider", "Out gain", ControlSpec(0, 1), "outGain"],
			["SynthOptionPopupPlusMinus", "Level control", arrOptionData, 0],
			["WetDryMixSlider"],
			["ActionButton", "Update Model - after changing parameters below", {
				this.rebuildSynth;
				this.moduleNodeStatus = "running";
				system.showView;
			}, 320, TXColor.paleYellow, TXColor.sysGuiCol2],
			["EZslider", "Model: C1", classData.specC, "c1"],
			["EZslider", "Model: C3", classData.specC, "c3"],
			["EZslider", "Model: Mix", ControlSpec(0, 1), "mix"],
			["EZslider", "Model: D1", classData.specD, "d1"],
			["EZslider", "Model: D2", classData.specD, "d2"],
			["EZslider", "Model: D3", classData.specD, "d3"],
			["EZslider", "Model: D4", classData.specD, "d4"],
			["EZslider", "Model: D5", classData.specD, "d5"],
			["EZslider", "Model: D6", classData.specD, "d6"],
			["EZslider", "Model: D7", classData.specD, "d7"],
			["EZslider", "Model: D8", classData.specD, "d8"],
			["TXNoteRangeSlider", "Freq range", "freqMin", "freqMax", {}, true],
			["TXCheckBox", "Quantise", "quantise"],
			["TextBar", "Bands", 80],
			["TextBar", "Band 1", 50],
			["TextBar", "Band 2", 50],
			["TextBar", "Band 3", 50],
			["TextBar", "Band 4", 50],
		] ++
		["freq1", "freq2", "freq3", "freq4",
			"res1", "res2", "res3", "res4",
			"amp1", "amp2", "amp3", "amp4",
		].collect({arg item, i;
			["EZslider", item, ControlSpec(0, 1), item]
		})
		++ [
			["TXMinMaxSliderSplit", "Freq hi-cut", classData.specFreqHiCut,
				"freqHiCut", "freqHiCutMin", "freqHiCutMax"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	buildGuiSpecArray {
		var holdFreqMin = this.getSynthArgSpec("freqMin");
		var holdFreqMax = this.getSynthArgSpec("freqMax");
		var holdResMin = this.getSynthArgSpec("resMin");
		var holdResMax = this.getSynthArgSpec("resMax");

		guiSpecArray = [
			["ActionButton", "Gain", {displayOption = "showGain";
				this.buildGuiSpecArray; system.showView;}, 110,
			TXColor.white, this.getButtonColour(displayOption == "showGain")],
			["Spacer", 3],
			["ActionButton", "Model", {displayOption = "showModel";
				this.buildGuiSpecArray; system.showView;}, 110,
			TXColor.white, this.getButtonColour(displayOption == "showModel")],
			["Spacer", 3],
			["ActionButton", "Band Parameters", {displayOption = "showParameters"; this.buildGuiSpecArray;
				system.showView;}, 120, TXColor.white, this.getButtonColour(displayOption == "showParameters")],
			["Spacer", 3],
			["ActionButton", "Bands & Hi-cut", {displayOption = "showBands"; this.buildGuiSpecArray;
				system.showView;}, 110, TXColor.white, this.getButtonColour(displayOption == "showBands")],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 2],
		];

		if (displayOption == "showGain", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["EZslider", "In gain", ControlSpec(0, 1), "inGain"],
				["SpacerLine", 20],
				["EZslider", "Out gain", ControlSpec(0, 1), "outGain"],
				["SpacerLine", 20],
				["SynthOptionPopupPlusMinus", "Level control", arrOptionData, 0],
				["SpacerLine", 20],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showModel", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["ActionButton", "Update Model - after changing parameters below", {
					this.rebuildSynth;
					this.moduleNodeStatus = "running";
					system.showView;
				}, 320, TXColor.paleYellow, TXColor.sysGuiCol2],
				["SpacerLine", 10],
				["EZslider", "Model: C1", classData.specC, "c1"],
				["SpacerLine", 4],
				["EZslider", "Model: C3", classData.specC, "c3"],
				["SpacerLine", 4],
				["EZslider", "Model: Mix", ControlSpec(0, 1), "mix"],
				["SpacerLine", 4],
				["EZslider", "Model: D1", classData.specD, "d1"],
				["SpacerLine", 4],
				["EZslider", "Model: D2", classData.specD, "d2"],
				["SpacerLine", 4],
				["EZslider", "Model: D3", classData.specD, "d3"],
				["SpacerLine", 4],
				["EZslider", "Model: D4", classData.specD, "d4"],
				["SpacerLine", 4],
				["EZslider", "Model: D5", classData.specD, "d5"],
				["SpacerLine", 4],
				["EZslider", "Model: D6", classData.specD, "d6"],
				["SpacerLine", 4],
				["EZslider", "Model: D7", classData.specD, "d7"],
				["SpacerLine", 4],
				["EZslider", "Model: D8", classData.specD, "d8"],
				["SpacerLine", 4],
			];
		});
		if (displayOption == "showParameters", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["TXNoteRangeSlider", "Freq range", "freqMin", "freqMax", {}, true],
				["SpacerLine", 6],
				["TXCheckBox", "Quantise", "quantise"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXRangeSlider", "Res range", classData.specRes, "resMin", "resMax"],
			];
		});
		if (displayOption == "showBands", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["TextBar", "Bands", 80],
				["TextBar", "Band 1", 50],
				["TextBar", "Band 2", 50],
				["TextBar", "Band 3", 50],
				["TextBar", "Band 4", 50],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["freq1", "freq2", "freq3", "freq4"]);}, 36],
				["TXMultiKnobMidiNote", "Freq: midi note", ["freq1", "freq2", "freq3", "freq4"],
					4, ControlSpec(holdFreqMin / 127, holdFreqMax / 127)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["res1", "res2", "res3", "res4"]);}, 36],
				["TXMultiKnobNoUnmap", "Res", ["res1", "res2", "res3", "res4"],
					4, ControlSpec(holdResMin, holdResMax)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["amp1", "amp2", "amp3", "amp4"]);}, 36],
				["TXMultiKnobNo", "Amp", ["amp1", "amp2", "amp3", "amp4"], 4, ControlSpec(0, 1)],
				["SpacerLine", 20],
				["TXMinMaxSliderSplit", "Freq hi-cut", classData.specFreqHiCut,
					"freqHiCut", "freqHiCutMin", "freqHiCutMax"],
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

