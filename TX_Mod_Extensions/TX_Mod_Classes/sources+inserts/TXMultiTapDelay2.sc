// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMultiTapDelay2 : TXModuleBase {		// MultiTap Delay

	//	Notes:
	//	This is a delay which can be set to any time up to 5 minutes.

		classvar <>classData;

	var	displayOption;
	var	holdControlSpec;
	var	holdTapTime, newTapTime;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "MultiTap Delay";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Delay Time", 1, "modDelay", 0],
			["Tap 1 Ratio", 1, "modTapRatio1", 0],
			["Tap 2 Ratio", 1, "modTapRatio2", 0],
			["Tap 3 Ratio", 1, "modTapRatio3", 0],
			["Tap 4 Ratio", 1, "modTapRatio4", 0],
			["Tap 5 Ratio", 1, "modTapRatio5", 0],
			["Tap 6 Ratio", 1, "modTapRatio6", 0],
			["Tap 7 Ratio", 1, "modTapRatio7", 0],
			["Tap 8 Ratio", 1, "modTapRatio8", 0],
			["Tap 1 Level", 1, "modTapLevel1", 0],
			["Tap 2 Level", 1, "modTapLevel2", 0],
			["Tap 3 Level", 1, "modTapLevel3", 0],
			["Tap 4 Level", 1, "modTapLevel4", 0],
			["Tap 5 Level", 1, "modTapLevel5", 0],
			["Tap 6 Level", 1, "modTapLevel6", 0],
			["Tap 7 Level", 1, "modTapLevel7", 0],
			["Tap 8 Level", 1, "modTapLevel8", 0],
			["Tap 1 Pan", 1, "modTapPan1", 0],
			["Tap 2 Pan", 1, "modTapPan2", 0],
			["Tap 3 Pan", 1, "modTapPan3", 0],
			["Tap 4 Pan", 1, "modTapPan4", 0],
			["Tap 5 Pan", 1, "modTapPan5", 0],
			["Tap 6 Pan", 1, "modTapPan6", 0],
			["Tap 7 Pan", 1, "modTapPan7", 0],
			["Tap 8 Pan", 1, "modTapPan8", 0],
			["Smoothing", 1, "modSmoothing", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.guiWidth = 580;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		displayOption = "showAll";
		arrOptions = [0];
		arrOptionData = [
			[
				["15 seconds", 15],
				["30 seconds", 30],
				["1 minute", 60],
				["2 minutes", 120],
				["3 minutes", 180],
				["4 minutes", 240],
				["5 minutes", 300],
			],
		];
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["bufnumDelay", 0, \ir],
			["delay", 0.75, defLagTime],
			["delayMin", 10, defLagTime],
			["delayMax", 1000 * this.getMaxDelaytime, defLagTime],
			["tapRatio1", 0.125, defLagTime],
			["tapLevel1", 1, defLagTime],
			["tapRatio2", 0.25, defLagTime],
			["tapLevel2", 0.875, defLagTime],
			["tapRatio3", 0.375, defLagTime],
			["tapLevel3", 0.75, defLagTime],
			["tapRatio4", 0.5, defLagTime],
			["tapLevel4", 0.625, defLagTime],
			["tapRatio5", 0.625, defLagTime],
			["tapLevel5", 0.5, defLagTime],
			["tapRatio6", 0.75, defLagTime],
			["tapLevel6", 0.375, defLagTime],
			["tapRatio7", 0.875, defLagTime],
			["tapLevel7", 0.25, defLagTime],
			["tapRatio8", 1, defLagTime],
			["tapLevel8", 0.125, defLagTime],
			["tapPan1", 0.375, defLagTime],
			["tapPan2", 0.625, defLagTime],
			["tapPan3", 0.25, defLagTime],
			["tapPan4", 0.75, defLagTime],
			["tapPan5", 0.125, defLagTime],
			["tapPan6", 0.875, defLagTime],
			["tapPan7", 0, defLagTime],
			["tapPan8", 1, defLagTime],
			["smoothing", 0.2, 0],
			["wetDryMix", 1.0, defLagTime],
			["modDelay", 0, defLagTime],
			["modTapRatio1", 0, defLagTime],
			["modTapRatio2", 0, defLagTime],
			["modTapRatio3", 0, defLagTime],
			["modTapRatio4", 0, defLagTime],
			["modTapRatio5", 0, defLagTime],
			["modTapRatio6", 0, defLagTime],
			["modTapRatio7", 0, defLagTime],
			["modTapRatio8", 0, defLagTime],
			["modTapLevel1", 0, defLagTime],
			["modTapLevel2", 0, defLagTime],
			["modTapLevel3", 0, defLagTime],
			["modTapLevel4", 0, defLagTime],
			["modTapLevel5", 0, defLagTime],
			["modTapLevel6", 0, defLagTime],
			["modTapLevel7", 0, defLagTime],
			["modTapLevel8", 0, defLagTime],
			["modTapPan1", 0, defLagTime],
			["modTapPan2", 0, defLagTime],
			["modTapPan3", 0, defLagTime],
			["modTapPan4", 0, defLagTime],
			["modTapPan5", 0, defLagTime],
			["modTapPan6", 0, defLagTime],
			["modTapPan7", 0, defLagTime],
			["modTapPan8", 0, defLagTime],
			["modSmoothing", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
			// N.B. arg below not used in synthdef, just kept here for convenience
			["autoTapTempo", 0, \ir],
		];
		synthDefFunc = {
			arg in, out, bufnumDelay, delay, delayMin, delayMax,
			tapRatio1, tapLevel1, tapRatio2, tapLevel2, tapRatio3, tapLevel3, tapRatio4, tapLevel4,
			tapRatio5, tapLevel5, tapRatio6, tapLevel6, tapRatio7, tapLevel7, tapRatio8, tapLevel8,
			tapPan1, tapPan2, tapPan3, tapPan4, tapPan5, tapPan6, tapPan7, tapPan8, smoothing, wetDryMix,
			modDelay=0,
			modTapRatio1 = 0, modTapRatio2 = 0, modTapRatio3 = 0, modTapRatio4 = 0,
			modTapRatio5 = 0, modTapRatio6 = 0, modTapRatio7 = 0, modTapRatio8 = 0,
			modTapLevel1 = 0, modTapLevel2 = 0, modTapLevel3 = 0, modTapLevel4 = 0,
			modTapLevel5 = 0, modTapLevel6 = 0, modTapLevel7 = 0, modTapLevel8 = 0,
			modTapPan1 = 0, modTapPan2 = 0, modTapPan3 = 0, modTapPan4 = 0,
			modTapPan5 = 0, modTapPan6 = 0, modTapPan7 = 0, modTapPan8 = 0,
			modSmoothing = 0, modWetDryMix = 0;
			var input, outSound, delaytime, lagTime;
			var totTapRatio1, totTapRatio2, totTapRatio3, totTapRatio4,
			totTapRatio5, totTapRatio6, totTapRatio7, totTapRatio8,
			totTapLevel1, totTapLevel2, totTapLevel3, totTapLevel4,
			totTapLevel5, totTapLevel6, totTapLevel7, totTapLevel8,
			totTapPan1, totTapPan2, totTapPan3, totTapPan4,
			totTapPan5, totTapPan6, totTapPan7, totTapPan8,
			totMix, totFrames, bufPhasor, tap1, tap2, tap3, tap4, tap5, tap6, tap7, tap8;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(startEnv * InFeedback.ar(in,1));
			delaytime =( (delayMax/delayMin) ** ((delay + modDelay).max(0.0001).min(1)) ) * delayMin / 1000;
			totTapRatio1 = (tapRatio1 + modTapRatio1).max(0).min(1);
			totTapRatio2 = (tapRatio2 + modTapRatio2).max(0).min(1);
			totTapRatio3 = (tapRatio3 + modTapRatio3).max(0).min(1);
			totTapRatio4 = (tapRatio4 + modTapRatio4).max(0).min(1);
			totTapRatio5 = (tapRatio5 + modTapRatio5).max(0).min(1);
			totTapRatio6 = (tapRatio6 + modTapRatio6).max(0).min(1);
			totTapRatio7 = (tapRatio7 + modTapRatio7).max(0).min(1);
			totTapRatio8 = (tapRatio8 + modTapRatio8).max(0).min(1);
			totTapLevel1 = (tapLevel1 + modTapLevel1).max(0).min(1);
			totTapLevel2 = (tapLevel2 + modTapLevel2).max(0).min(1);
			totTapLevel3 = (tapLevel3 + modTapLevel3).max(0).min(1);
			totTapLevel4 = (tapLevel4 + modTapLevel4).max(0).min(1);
			totTapLevel5 = (tapLevel5 + modTapLevel5).max(0).min(1);
			totTapLevel6 = (tapLevel6 + modTapLevel6).max(0).min(1);
			totTapLevel7 = (tapLevel7 + modTapLevel7).max(0).min(1);
			totTapLevel8 = (tapLevel8 + modTapLevel8).max(0).min(1);
			totTapPan1 = (tapPan1 + modTapPan1).max(0).min(1).madd(2, -1);  // Pan goes from -1 to 1
			totTapPan2 = (tapPan2 + modTapPan2).max(0).min(1).madd(2, -1);
			totTapPan3 = (tapPan3 + modTapPan3).max(0).min(1).madd(2, -1);
			totTapPan4 = (tapPan4 + modTapPan4).max(0).min(1).madd(2, -1);
			totTapPan5 = (tapPan5 + modTapPan5).max(0).min(1).madd(2, -1);
			totTapPan6 = (tapPan6 + modTapPan6).max(0).min(1).madd(2, -1);
			totTapPan7 = (tapPan7 + modTapPan7).max(0).min(1).madd(2, -1);
			totTapPan8 = (tapPan8 + modTapPan8).max(0).min(1).madd(2, -1);
			lagTime = (smoothing + modSmoothing).max(0).min(1);
			totMix = (wetDryMix + modWetDryMix).max(0).min(1);
			totFrames = defSampleRate * this.getMaxDelaytime;
			bufPhasor = Phasor.ar(0, BufRateScale.ir(bufnumDelay), 0, BufFrames.ir(bufnumDelay), 0);
			BufWr.ar(input, bufnumDelay, bufPhasor);
			tap1 = Pan2.ar(BufRd.ar(1, bufnumDelay, (bufPhasor - Lag2.kr(totTapRatio1 * delaytime * defSampleRate, lagTime))
				.wrap(0, totFrames-1)),totTapPan1, totTapLevel1);
			tap2 = Pan2.ar(BufRd.ar(1, bufnumDelay, (bufPhasor - Lag2.kr(totTapRatio2 * delaytime * defSampleRate, lagTime))
				.wrap(0, totFrames-1)),totTapPan2, totTapLevel2);
			tap3 = Pan2.ar(BufRd.ar(1, bufnumDelay, (bufPhasor - Lag2.kr(totTapRatio3 * delaytime * defSampleRate, lagTime))
				.wrap(0, totFrames-1)),totTapPan3, totTapLevel3);
			tap4 = Pan2.ar(BufRd.ar(1, bufnumDelay, (bufPhasor - Lag2.kr(totTapRatio4 * delaytime * defSampleRate, lagTime))
				.wrap(0, totFrames-1)),totTapPan4, totTapLevel4);
			tap5 = Pan2.ar(BufRd.ar(1, bufnumDelay, (bufPhasor - Lag2.kr(totTapRatio5 * delaytime * defSampleRate, lagTime))
				.wrap(0, totFrames-1)),totTapPan5, totTapLevel5);
			tap6 = Pan2.ar(BufRd.ar(1, bufnumDelay, (bufPhasor - Lag2.kr(totTapRatio6 * delaytime * defSampleRate, lagTime))
				.wrap(0, totFrames-1)),totTapPan6, totTapLevel6);
			tap7 = Pan2.ar(BufRd.ar(1, bufnumDelay, (bufPhasor - Lag2.kr(totTapRatio7 * delaytime * defSampleRate, lagTime))
				.wrap(0, totFrames-1)),totTapPan7, totTapLevel7);
			tap8 = Pan2.ar(BufRd.ar(1, bufnumDelay, (bufPhasor - Lag2.kr(totTapRatio8 * delaytime * defSampleRate, lagTime))
				.wrap(0, totFrames-1)),totTapPan8, totTapLevel8);
			outSound = Mix.new([([input, input] * (1-totMix)),
				((tap1  + tap2 + tap3 + tap4 + tap5 + tap6 + tap7 + tap8) * totMix)]);
			outSound = LeakDC.ar(outSound);
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["SynthOptionPopupPlusMinus", "Max time", arrOptionData, 0, 300,
				{this.buildGuiSpecArray;
					system.showViewIfModDisplay(this);
					this.makeBuffersAndSynth(this.getArrBufferSpecs);
				}
			],
			["ActionButtonDark", "Clear delay", {this.clearBuffers}],
			["TXTimeBpmMinMaxSldr", "Delay time", holdControlSpec, "delay", "delayMin", "delayMax"],
			["EZslider", "Smoothing", ControlSpec(0, 1), "smoothing"],
			["TXPresetPopup", "Ratio presets",
				TXMultiTap8Presets.arrRatioPresets(this).collect({arg item, i; item.at(0)}),
				TXMultiTap8Presets.arrRatioPresets(this).collect({arg item, i; item.at(1)})
			],
			["commandAction", "Tap Tempo", {this.actionTapTempo;}],
			["TXCheckBox", "Auto copy tap tempo to delay bpm ", "autoTapTempo", nil, 230],
			["TXFractionSlider", "Tap 1 Ratio", ControlSpec(0, 1), "tapRatio1"],
			["TXFractionSlider", "Tap 2 Ratio", ControlSpec(0, 1), "tapRatio2"],
			["TXFractionSlider", "Tap 3 Ratio", ControlSpec(0, 1), "tapRatio3"],
			["TXFractionSlider", "Tap 4 Ratio", ControlSpec(0, 1), "tapRatio4"],
			["TXFractionSlider", "Tap 5 Ratio", ControlSpec(0, 1), "tapRatio5"],
			["TXFractionSlider", "Tap 6 Ratio", ControlSpec(0, 1), "tapRatio6"],
			["TXFractionSlider", "Tap 7 Ratio", ControlSpec(0, 1), "tapRatio7"],
			["TXFractionSlider", "Tap 8 Ratio", ControlSpec(0, 1), "tapRatio8"],
			["DividingLine"],
			["TXPresetPopup", "Level presets",
				TXMultiTap8Presets.arrLevelPresets(this).collect({arg item, i; item.at(0)}),
				TXMultiTap8Presets.arrLevelPresets(this).collect({arg item, i; item.at(1)})
			],
			["WetDryMixSlider"],
			["EZslider", "Tap 1 Level", ControlSpec(0, 1), "tapLevel1"],
			["EZslider", "Tap 2 Level", ControlSpec(0, 1), "tapLevel2"],
			["EZslider", "Tap 3 Level", ControlSpec(0, 1), "tapLevel3"],
			["EZslider", "Tap 4 Level", ControlSpec(0, 1), "tapLevel4"],
			["EZslider", "Tap 5 Level", ControlSpec(0, 1), "tapLevel5"],
			["EZslider", "Tap 6 Level", ControlSpec(0, 1), "tapLevel6"],
			["EZslider", "Tap 7 Level", ControlSpec(0, 1), "tapLevel7"],
			["EZslider", "Tap 8 Level", ControlSpec(0, 1), "tapLevel8"],
			["DividingLine"],
			["TXPresetPopup", "Pan presets",
				TXMultiTap8Presets.arrPanPresets(this).collect({arg item, i; item.at(0)}),
				TXMultiTap8Presets.arrPanPresets(this).collect({arg item, i; item.at(1)})
			],
			["EZslider", "Tap 1 Pan", ControlSpec(0, 1), "tapPan1"],
			["EZslider", "Tap 2 Pan", ControlSpec(0, 1), "tapPan2"],
			["EZslider", "Tap 3 Pan", ControlSpec(0, 1), "tapPan3"],
			["EZslider", "Tap 4 Pan", ControlSpec(0, 1), "tapPan4"],
			["EZslider", "Tap 5 Pan", ControlSpec(0, 1), "tapPan5"],
			["EZslider", "Tap 6 Pan", ControlSpec(0, 1), "tapPan6"],
			["EZslider", "Tap 7 Pan", ControlSpec(0, 1), "tapPan7"],
			["EZslider", "Tap 8 Pan", ControlSpec(0, 1), "tapPan8"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make buffers, load the synthdef and create the synth
		this.makeBuffersAndSynth(this.getArrBufferSpecs);
	}

	buildGuiSpecArray {
		holdControlSpec = ControlSpec.new(10, 1000 * this.getMaxDelaytime, \exp );
		guiSpecArray = [
			["ActionButton", "All controls", {displayOption = "showAll";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showAll")],
			["Spacer", 3],
			["ActionButton", "Ratios", {displayOption = "showRatios";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showRatios")],
			["Spacer", 3],
			["ActionButton", "Levels", {displayOption = "showLevels";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showLevels")],
			["Spacer", 3],
			["ActionButton", "Pans", {displayOption = "showPans";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showPans")],
			["Spacer", 20],
			["ActionButtonDark", "Clear delay", {this.clearBuffers}],
			["DividingLine"],
			["SpacerLine", 4],
		];

		if (displayOption == "showAll", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Max time", arrOptionData, 0, 300,
					{this.buildGuiSpecArray;
						system.showViewIfModDisplay(this);
						this.makeBuffersAndSynth(this.getArrBufferSpecs);
					}
				],
				["SpacerLine", 3],
				["TapTempoButton", {arg argTempo; this.useTapTempo(argTempo);}],
				["Spacer", 10],
				["TXCheckBox", "Auto copy tap tempo to delay bpm ", "autoTapTempo", nil, 230],
				["SpacerLine", 3],
				["TextBarLeft", "Delay time shown in ms and bpm", 200],
				["Spacer", 3],
				["ActionButton", "time x 2", {this.delayTimeMultiply(2);}, 60],
				["ActionButton", "time x 3", {this.delayTimeMultiply(3);}, 60],
				["ActionButton", "time / 2", {this.delayTimeMultiply(0.5);}, 60],
				["ActionButton", "time / 3", {this.delayTimeMultiply(1/3);}, 60],
				["NextLine"],
				["TXTimeBpmMinMaxSldr", "Delay time", holdControlSpec, "delay", "delayMin", "delayMax"],
				["SpacerLine", 3],
				["EZslider", "Smoothing", ControlSpec(0, 1), "smoothing"],
				["SpacerLine", 3],
				["TextBar", "Taps", 80],
				["TextBar", "Tap 1", 50],
				["TextBar", "Tap 2", 50],
				["TextBar", "Tap 3", 50],
				["TextBar", "Tap 4", 50],
				["TextBar", "Tap 5", 50],
				["TextBar", "Tap 6", 50],
				["TextBar", "Tap 7", 50],
				["TextBar", "Tap 8", 50],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["tapRatio1", "tapRatio2", "tapRatio3", "tapRatio4",
					"tapRatio5", "tapRatio6", "tapRatio7", "tapRatio8", ]);}, 36],
				["TXMultiKnobNo", "Ratio", ["tapRatio1", "tapRatio2", "tapRatio3", "tapRatio4",
					"tapRatio5", "tapRatio6", "tapRatio7", "tapRatio8", ], 8, ControlSpec(0, 1)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["tapLevel1", "tapLevel2", "tapLevel3", "tapLevel4",
					"tapLevel5", "tapLevel6", "tapLevel7", "tapLevel8", ]);}, 36],
				["TXMultiKnobNo", "Level", ["tapLevel1", "tapLevel2", "tapLevel3", "tapLevel4",
					"tapLevel5", "tapLevel6", "tapLevel7", "tapLevel8", ], 8, ControlSpec(0, 1)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["tapPan1", "tapPan2", "tapPan3", "tapPan4",
					"tapPan5", "tapPan6", "tapPan7", "tapPan8", ]);}, 36],
				["TXMultiKnobNo", "Pan", ["tapPan1", "tapPan2", "tapPan3", "tapPan4",
					"tapPan5", "tapPan6", "tapPan7", "tapPan8", ], 8, ControlSpec(0, 1)],
				["SpacerLine", 3],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showRatios", {
			guiSpecArray = guiSpecArray ++[
				["TXPresetPopup", "Tap presets",
					TXMultiTap8Presets.arrRatioPresets(this).collect({arg item, i; item.at(0)}),
					TXMultiTap8Presets.arrRatioPresets(this).collect({arg item, i; item.at(1)})
				],
				["SpacerLine", 2],
				["TXFractionSlider", "Tap 1 Ratio", ControlSpec(0, 1), "tapRatio1", nil, 300],
				["NextLine"],
				["TXFractionSlider", "Tap 2 Ratio", ControlSpec(0, 1), "tapRatio2", nil, 300],
				["NextLine"],
				["TXFractionSlider", "Tap 3 Ratio", ControlSpec(0, 1), "tapRatio3", nil, 300],
				["NextLine"],
				["TXFractionSlider", "Tap 4 Ratio", ControlSpec(0, 1), "tapRatio4", nil, 300],
				["NextLine"],
				["TXFractionSlider", "Tap 5 Ratio", ControlSpec(0, 1), "tapRatio5", nil, 300],
				["NextLine"],
				["TXFractionSlider", "Tap 6 Ratio", ControlSpec(0, 1), "tapRatio6", nil, 300],
				["NextLine"],
				["TXFractionSlider", "Tap 7 Ratio", ControlSpec(0, 1), "tapRatio7", nil, 300],
				["NextLine"],
				["TXFractionSlider", "Tap 8 Ratio", ControlSpec(0, 1), "tapRatio8", nil, 300],
			];
		});
		if (displayOption == "showLevels", {
			guiSpecArray = guiSpecArray ++[
				["TXPresetPopup", "Level presets",
					TXMultiTap8Presets.arrLevelPresets(this).collect({arg item, i; item.at(0)}),
					TXMultiTap8Presets.arrLevelPresets(this).collect({arg item, i; item.at(1)})
				],
				["SpacerLine", 4],
				["EZslider", "Tap 1 Level", ControlSpec(0, 1), "tapLevel1"],
				["EZslider", "Tap 2 Level", ControlSpec(0, 1), "tapLevel2"],
				["EZslider", "Tap 3 Level", ControlSpec(0, 1), "tapLevel3"],
				["EZslider", "Tap 4 Level", ControlSpec(0, 1), "tapLevel4"],
				["EZslider", "Tap 5 Level", ControlSpec(0, 1), "tapLevel5"],
				["EZslider", "Tap 6 Level", ControlSpec(0, 1), "tapLevel6"],
				["EZslider", "Tap 7 Level", ControlSpec(0, 1), "tapLevel7"],
				["EZslider", "Tap 8 Level", ControlSpec(0, 1), "tapLevel8"],
				// REMOVED OLD:
				// ["TXMultiSliderNoGroup", "Tap Levels", ControlSpec(0, 1, step: 0.001), ["tapLevel1", "tapLevel2",
				// "tapLevel3", "tapLevel4", "tapLevel5", "tapLevel6", "tapLevel7", "tapLevel8"], nil, 300],
			];
		});
		if (displayOption == "showPans", {
			guiSpecArray = guiSpecArray ++[
				["TXPresetPopup", "Pan presets",
					TXMultiTap8Presets.arrPanPresets(this).collect({arg item, i; item.at(0)}),
					TXMultiTap8Presets.arrPanPresets(this).collect({arg item, i; item.at(1)})
				],
				["SpacerLine", 4],
				["EZslider", "Tap 1 Pan", ControlSpec(0, 1), "tapPan1"],
				["EZslider", "Tap 2 Pan", ControlSpec(0, 1), "tapPan2"],
				["EZslider", "Tap 3 Pan", ControlSpec(0, 1), "tapPan3"],
				["EZslider", "Tap 4 Pan", ControlSpec(0, 1), "tapPan4"],
				["EZslider", "Tap 5 Pan", ControlSpec(0, 1), "tapPan5"],
				["EZslider", "Tap 6 Pan", ControlSpec(0, 1), "tapPan6"],
				["EZslider", "Tap 7 Pan", ControlSpec(0, 1), "tapPan7"],
				["EZslider", "Tap 8 Pan", ControlSpec(0, 1), "tapPan8"],
				// REMOVED OLD:
				// ["TXMultiSliderNoGroup", "Tap Pans", ControlSpec(0, 1, step: 0.001), ["tapPan1", "tapPan2",
				// "tapPan3", "tapPan4", "tapPan5", "tapPan6", "tapPan7", "tapPan8"], nil, 300],
				// ["SpacerLine", 2],
				// ["TextBar", "0 is panned left, 1 is panned right", 250]
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

	getMaxDelaytime {
		^arrOptionData.at(0).at(arrOptions.at(0)).at(1);
	}

	getArrBufferSpecs {
		classData.arrBufferSpecs = [ ["bufnumDelay", defSampleRate * this.getMaxDelaytime, 1] ];
		^classData.arrBufferSpecs;
	}

	delayTimeMultiply { arg argMultiplyValue;
		var currentTime, minTime, maxTime, holdControlSpec, newTime;
		minTime = this.getSynthArgSpec("delayMin");
		maxTime = this.getSynthArgSpec("delayMax");
		holdControlSpec = ControlSpec.new(minTime, maxTime, \exp);
		currentTime = holdControlSpec.map(this.getSynthArgSpec("delay"));
		newTime = currentTime * argMultiplyValue;
		if (argMultiplyValue < 1, {
			if ( newTime >= minTime, {
				this.setSynthValue("delay", holdControlSpec.unmap(newTime));
			});
		},{
			if ( newTime <= maxTime, {
				this.setSynthValue("delay", holdControlSpec.unmap(newTime));
			});
		});
		system.flagGuiIfModDisplay(this);
	}

	useTapTempo {arg argTempo;
		var holdDelay, autoBPM, minDelay, maxDelay;
		holdDelay = 60000/argTempo;
		autoBPM = this.getSynthArgSpec("autoTapTempo");
		minDelay = this.getSynthArgSpec("delayMin");
		maxDelay = this.getSynthArgSpec("delayMax");
		if (autoBPM == 1,{
			if ((holdDelay >= minDelay) and: (holdDelay <= maxDelay),{
				this.setSynthArgSpec("delay", ControlSpec(minDelay, maxDelay, \exp).unmap(holdDelay));
				system.flagGuiIfModDisplay(this);
			});
		});
	}

	actionTapTempo {	// tap tempo function used by module action
		var holdBPM;
		if (newTapTime.isNil, {
			newTapTime = Main.elapsedTime
		}, {
			holdTapTime = Main.elapsedTime;
			holdBPM = 60 / (holdTapTime - newTapTime);
			newTapTime = holdTapTime;
			this.useTapTempo(holdBPM);
		});
	}

	loadExtraData {arg argData;
		this.buildGuiSpecArray;
		system.showViewIfModDisplay(this);
		{this.makeBuffersAndSynth(this.getArrBufferSpecs);}.defer(2);
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

