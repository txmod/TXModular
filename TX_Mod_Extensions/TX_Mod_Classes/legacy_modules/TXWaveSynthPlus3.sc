// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXWaveSynthPlus3 : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var	timeSpec, lfoFreqSpec;
	var	arrFreqRangePresets;
	var	arrMMSourceNames, arrMMDestNames, arrMMScaleNames;
	var	ratioView;
	var	envView, envView2;
	var arrCurve1Values;
	var arrCurve2Values;
	var arrSlotData, curve1DataEvent, curve2DataEvent;
	var arrGridPresetNames, arrGridPresetActions;
	var <>testMIDINote = 69;
	var <>testMIDIVel = 100;
	var <>testMIDITime = 1;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Wave Synth+";
		classData.moduleRate = "audio";
		classData.moduleType = "groupsource";
		classData.arrAudSCInBusSpecs = [
			["FM input", 1, "inmodulator"],
		];
		classData.arrCtlSCInBusSpecs = [
			["Modify 1", 1, "modModify1", 0],
			["Modify 2", 1, "modModify2", 0],
			["Pitch bend", 1, "modPitchbend", 0],
			["Filter Freq", 1, "modFilterFreq", 0],
			["Filter Res", 1, "modFilterRes", 0],
			["Filter Sat", 1, "modFilterSat", 0],
			["Delay", 1, "modDelay", 0],
			["Attack", 1, "modAttack", 0],
			["Decay", 1, "modDecay", 0],
			["Sustain level", 1, "modSustain", 0],
			["Sustain time", 1, "modSustainTime", 0],
			["Release", 1, "modRelease", 0],
			["Delay2", 1, "modDelay2", 0],
			["Attack2", 1, "modAttack2", 0],
			["Decay2", 1, "modDecay2", 0],
			["Sustain level2", 1, "modSustain2", 0],
			["Sustain time2", 1, "modSustainTime2", 0],
			["Release2", 1, "modRelease2", 0],
			["LFO 1 Freq", 1, "modFreqLFO1", 0],
			["LFO 1 Fade-in", 1, "modFadeInLFO1", 0],
			["LFO 2 Freq", 1, "modFreqLFO2", 0],
			["LFO 2 Fade-in", 1, "modFadeInLFO2", 0],
			["Slider 1", 1, "modSlider1", 0],
			["Slider 2", 1, "modSlider2", 0],
			["Curve 1 Freq", 1, "modCurve1Freq", 0],
			["Curve 2 Freq", 1, "modCurve2Freq", 0],
			["FM depth", 1, "modFMDepth", 0],
			["FM freq", 1, "modFMFreq", 0],
			["FM ratio", 1, "modFMRatio", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrBufferSpecs = [ ["bufnumCurve1", 512, 1], ["bufnumCurve2", 512, 1] ];
		classData.fmFreqSpec = ControlSpec(0.01, 20000, \exponential);
		classData.fmRatioSpec = ControlSpec(0.01, 10);
		classData.fmDepthSpec = ControlSpec(0, 4);
		classData.guiWidth = 730;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showWaveform";
		timeSpec = ControlSpec(0.01, 20);
		lfoFreqSpec = ControlSpec(0.01, 100, \exp, 0, 1, units: " Hz");
		curve1DataEvent = ();
		curve2DataEvent = ();
		arrFreqRangePresets = TXFilter.arrFreqRanges;
		arrMMSourceNames = ["...", "Note", "Velocity", "Amp Env", "Env 2", "LFO 1", "LFO 2",
			"Curve 1", "Curve 2", "Random val 1", "Random val 2", "Offset val", "Slider 1", "Slider 2"];
		arrMMDestNames = ["...", "Modify 1", "Modify 2", "Pitchbend", "Level",
			"Filter Freq", "Filter Res", "Filter Sat", "FM Freq", "FM Ratio", "FM Depth"];
		arrMMScaleNames = ["...", "Note", "Velocity", "Amp Env", "Env 2", "LFO 1", "LFO 2",
			"Curve 1", "Curve 2", "Random val 1", "Random val 2", "Offset val", "Slider 1", "Slider 2"];
		arrSynthArgSpecs = [
			["inmodulator", 0, 0],
			["out", 0, 0],
			["bufnumCurve1", 0, \ir],
			["bufnumCurve2", 0, \ir],
			["gate", 1, 0],
			["note", 0, 0],
			["velocity", 0, 0],
			["keytrack", 1, \ir],
			["transpose", 0, \ir],
			["pitchbend", 0.5, defLagTime],
			["pitchbendMin", -2, defLagTime],
			["pitchbendMax", 2, defLagTime],
			["modify1", 0.5, defLagTime],
			["modify1Min", 0, defLagTime],
			["modify1Max", 1, defLagTime],
			["modify2", 0.5, defLagTime],
			["modify2Min", 0, defLagTime],
			["modify2Max", 1, defLagTime],
			["level", 0.5, defLagTime],
			["envtime", 0, \ir],
			["delay", 0, \ir],
			["attack", 0.02, \ir],
			["attackMin", 0.01, \ir],
			["attackMax", 5, \ir],
			["decay", 0.05, \ir],
			["decayMin", 0, \ir],
			["decayMax", 5, \ir],
			["sustain", 1, \ir],
			["sustainTime", 0.2, \ir],
			["sustainTimeMin", 0, \ir],
			["sustainTimeMax", 5, \ir],
			["release", 0.05, \ir],
			["releaseMin", 0.01, \ir],
			["releaseMax", 5, \ir],
			["envtime2", 0, \ir],
			["delay2", 0, \ir],
			["attack2", 0.02, \ir],
			["attackMin2", 0.01, \ir],
			["attackMax2", 5, \ir],
			["decay2", 0.05, \ir],
			["decayMin2", 0, \ir],
			["decayMax2", 5, \ir],
			["sustain2", 1, \ir],
			["sustainTime2", 0.05, \ir],
			["sustainTimeMin2", 0, \ir],
			["sustainTimeMax2", 5, \ir],
			["release2", 0.1, \ir],
			["releaseMin2", 0.01, \ir],
			["releaseMax2", 5, \ir],
			["intKey", 0, \ir],
			["filterFreq", 0.5, defLagTime],
			["filterFreqMin",40, defLagTime],
			["filterFreqMax", 20000, defLagTime],
			["filterRes", 0.5, defLagTime],
			["filterResMin", 0,  defLagTime],
			["filterResMax", 1, defLagTime],
			["filterSat", 0.5, defLagTime],
			["filterSatMin", 0,  defLagTime],
			["filterSatMax", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["freqLFO1", 0.5, defLagTime],
			["freqLFO1Min", 0.01, defLagTime],
			["freqLFO1Max", 100, defLagTime],
			["lfo1FadeIn", 0, \ir],
			["lfo1FadeInMin", 0.01, \ir],
			["lfo1FadeInMax", 5, \ir],
			["freqLFO2", 0.5, defLagTime],
			["freqLFO2Min", 0.01, defLagTime],
			["freqLFO2Max", 100, defLagTime],
			["lfo2FadeIn", 0, \ir],
			["lfo2FadeInMin", 0.01, \ir],
			["lfo2FadeInMax", 5, \ir],
			["curve1Freq", 0.5, defLagTime],
			["curve1FreqMin", 0.01, defLagTime],
			["curve1FreqMax", 100, defLagTime],
			["curve2Freq", 0.5, defLagTime],
			["curve2FreqMin", 0.01, defLagTime],
			["curve2FreqMax", 100, defLagTime],
			["velModMin", 0, defLagTime],
			["velModMax", 127, defLagTime],
			["noteModMin", 0, defLagTime],
			["noteModMax", 127, defLagTime],
			["slider1", 0, defLagTime],
			["slider2", 0, defLagTime],
			["fmDepth", ControlSpec(0, 4).unmap(1), defLagTime],
			["fmDepthMin", 0, defLagTime],
			["fmDepthMax", 4, defLagTime],
			["fmFreq", 0.5, defLagTime],
			["fmFreqMin", 0.midicps, defLagTime],
			["fmFreqMax", 127.midicps, defLagTime],
			["fmRatio", ControlSpec(0.01, 10).unmap(1), defLagTime],
			["fmRatioMin", 0.01, defLagTime],
			["fmRatioMax", 10, defLagTime],

			["i_source0", 0, \ir],
			["i_dest0", 0, \ir],
			["mmValue0", 0, defLagTime],
			["i_scale0", 0, \ir],
			["i_source1", 0, \ir],
			["i_dest1", 0, \ir],
			["mmValue1", 0, defLagTime],
			["i_scale1", 0, \ir],
			["i_source2", 0, \ir],
			["i_dest2", 0, \ir],
			["mmValue2", 0, defLagTime],
			["i_scale2", 0, \ir],
			["i_source3", 0, \ir],
			["i_dest3", 0, \ir],
			["mmValue3", 0, defLagTime],
			["i_scale3", 0, \ir],
			["i_source4", 0, \ir],
			["i_dest4", 0, \ir],
			["mmValue4", 0, defLagTime],
			["i_scale4", 0, \ir],
			["i_source5", 0, \ir],
			["i_dest5", 0, \ir],
			["mmValue5", 0, defLagTime],
			["i_scale5", 0, \ir],
			["i_source6", 0, \ir],
			["i_dest6", 0, \ir],
			["mmValue6", 0, defLagTime],
			["i_scale6", 0, \ir],
			["i_source7", 0, \ir],
			["i_dest7", 0, \ir],
			["mmValue7", 0, defLagTime],
			["i_scale7", 0, \ir],
			["i_source8", 0, \ir],
			["i_dest8", 0, \ir],
			["mmValue8", 0, defLagTime],
			["i_scale8", 0, \ir],
			["i_source9", 0, \ir],
			["i_dest9", 0, \ir],
			["mmValue9", 0, defLagTime],
			["i_scale9", 0, \ir],

			["modPitchbend", 0, defLagTime],
			["modModify1", 0, defLagTime],
			["modModify2", 0, defLagTime],
			["modDelay", 0, \ir],
			["modAttack", 0, \ir],
			["modDecay", 0, \ir],
			["modSustain", 0, \ir],
			["modSustainTime", 0, \ir],
			["modRelease", 0, \ir],
			["modDelay2", 0, \ir],
			["modAttack2", 0, \ir],
			["modDecay2", 0, \ir],
			["modSustain2", 0, \ir],
			["modSustainTime2", 0, \ir],
			["modRelease2", 0, \ir],
			["modFilterFreq", 0, defLagTime],
			["modFilterRes", 0, defLagTime],
			["modFilterSat", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
			["modFreqLFO1", 0, defLagTime],
			["modFadeInLFO1", 0, defLagTime],
			["modFreqLFO2", 0, defLagTime],
			["modFadeInLFO2", 0, defLagTime],
			["modSlider1", 0, defLagTime],
			["modSlider2", 0, defLagTime],
			["modCurve1Freq", 0, defLagTime],
			["modCurve2Freq", 0, defLagTime],
			["modFMDepth", 0, defLagTime],
			["modFMFreq", 0, defLagTime],
			["modFMRatio", 0, defLagTime],
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
		arrOptions = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
		arrOptionData = [
			// ind 0
			TXWaveForm.arrOptionData,
			// ind 1
			TXIntonation.arrOptionData,
			// ind 2
			TXEnvLookup.arrDADSRSlopeOptionData,
			// ind 3
			TXEnvLookup.arrDADSRSustainOptionData,
			// ind 4
			TXEnvLookup.arrDADSRSlopeOptionData,
			// ind 5
			TXEnvLookup.arrDADSRSustainOptionData,
			// ind 6
			TXFilter.arrOptionData,
			// ind 7
			[
				["Off", {arg input; input; }],
				["On", TXFilter.filterFunction],
			],
			// ind 8
			[
				["Off", { 0; }],
				["On", TXLFO.lfoFunction],
			],
			// ind 9
			TXLFO.arrOptionData,
			// ind 10
			TXLFO.arrLFOOutputRanges,
			// ind 11
			[
				["Off", { 0; }],
				["On", TXLFO.lfoFunction],
			],
			// ind 12
			TXLFO.arrOptionData,
			// ind 13
			TXLFO.arrLFOOutputRanges,
			// ind 14
			[
				["Off", { 0; }],
				["On", {arg envFunction, del, att, dec, sus, sustime, rel, envCurve, gate;
					EnvGen.ar(
						envFunction.value(del, att, dec, sus, sustime, rel, envCurve),
						gate,
						doneAction: 0
					);
				}],
			],
			// ind 15
			TXLevelControl.arrOptionData,
			// ind 16 - curve 1
			[
				["Off", { 0; }],
				["On - Play Once", {arg bufnumCurve, freq, freqMin, freqMax;
					var outFreq = freq.linexp(0, 1, freqMin, freqMax);
					BufRd.kr(1, bufnumCurve,
						Line.kr(0, 511, outFreq.reciprocal, doneAction: 0));
				}],
				["On - Play Looped", {arg bufnumCurve, freq, freqMin, freqMax;
					var outFreq = freq.linexp(0, 1, freqMin, freqMax);
					BufRd.kr(1, bufnumCurve,
						Phasor.kr(0, outFreq * ControlDur.ir * 512, 0, 512));
				}],
			],
			// ind 17 - curve 2
			[
				["Off", { 0; }],
				["On - Play Once", {arg bufnumCurve, freq, freqMin, freqMax;
					var outFreq = freq.linexp(0, 1, freqMin, freqMax);
					BufRd.kr(1, bufnumCurve,
						Line.kr(0, 511, outFreq.reciprocal, doneAction: 0));
				}],
				["On - Play Looped", {arg bufnumCurve, freq, freqMin, freqMax;
					var outFreq = freq.linexp(0, 1, freqMin, freqMax);
					BufRd.kr(1, bufnumCurve,
						Phasor.kr(0, outFreq * ControlDur.ir * 512, 0, 512));
				}],
			],
			// ind 18 - curve 1 o/p range
			[
				["Positive only: 0 to 1", [0, 1]],
				["Positive & Negative: -1 to 1", [-1, 1]],
				["Positive & Negative: -0.5 to 0.5", [-0.5, 0.5]],
				["Negative only: -1 to 0", [-1, 0]],
			],
			// ind 19 - curve 2 o/p range
			[
				["Positive only: 0 to 1", [0, 1]],
				["Positive & Negative: -1 to 1", [-1, 1]],
				["Positive & Negative: -0.5 to 0.5", [-0.5, 0.5]],
				["Negative only: -1 to 0", [-1, 0]],
			],
			// ind 20 - amp compensation
			TXAmpComp.arrOptionData,
			// ind 21 - Frequency Modulation
			[
				["Off - no Frequency Modulation (FM)", {1;}],
				["Internal - use sine wave, FM freq & FM depth",
					{arg inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth, fmFreq, fmFreqMin, fmFreqMax,  modFMFreq,
						fmRatio, fmRatioMin, fmRatioMax, modFMRatio, baseFreq;
						var fmDepthSum = (fmDepth + modFMDepth).max(0).min(1).linlin(0, 1, fmDepthMin, fmDepthMax);
						var fmFreqSum = (fmFreq + modFMFreq).max(0).min(1).linexp(0, 1, fmFreqMin, fmFreqMax);
						var fmWave = SinOsc.ar(fmFreqSum);
						2 ** (fmDepthSum * fmWave);
					}
				],
				["Internal - use sine wave, [FM ratio x main frequency] & FM depth",
					{arg inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth, fmFreq, fmFreqMin, fmFreqMax,  modFMFreq,
						fmRatio, fmRatioMin, fmRatioMax, modFMRatio, baseFreq;
						var fmDepthSum = (fmDepth + modFMDepth).max(0).min(1).linlin(0, 1, fmDepthMin, fmDepthMax);
						var fmRatioSum = (fmRatio + modFMRatio).max(0).min(1).linlin(0, 1, fmRatioMin, fmRatioMax);
						var fmWave = SinOsc.ar(fmRatioSum * baseFreq);
						2 ** (fmDepthSum * fmWave);
					}
				],
				["External- use FM audio input & FM depth",
					{arg inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth;
						var fmDepthSum = (fmDepth + modFMDepth).max(0).min(1).linlin(0, 1, fmDepthMin, fmDepthMax);
						2 ** (fmDepthSum * InFeedback.ar(inmodulator,1));
					}
				],
			],
		];
		synthDefFunc = {
			arg inmodulator, out, bufnumCurve1, bufnumCurve2, gate, note, velocity, keytrack, transpose,
			pitchbend, pitchbendMin, pitchbendMax,
			modify1, modify1Min, modify1Max, modify2, modify2Min, modify2Max,
			level,
			envtime, delay, attack, attackMin, attackMax, decay, decayMin, decayMax, sustain,
			sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax,
			envtime2, delay2, attack2, attackMin2, attackMax2, decay2, decayMin2, decayMax2, sustain2,
			sustainTime2, sustainTimeMin2, sustainTimeMax2, release2, releaseMin2, releaseMax2,
			intKey,
			filterFreq, filterFreqMin, filterFreqMax, filterRes, filterResMin, filterResMax,
			filterSat, filterSatMin, filterSatMax, wetDryMix,
			freqLFO1, freqLFO1Min, freqLFO1Max, lfo1FadeIn, lfo1FadeInMin, lfo1FadeInMax,
			freqLFO2, freqLFO2Min, freqLFO2Max,  lfo2FadeIn, lfo2FadeInMin, lfo2FadeInMax,
			curve1Freq, curve1FreqMin, curve1FreqMax, curve2Freq, curve2FreqMin, curve2FreqMax,
			velModMin, velModMax, noteModMin, noteModMax, slider1, slider2,
			fmDepth, fmDepthMin, fmDepthMax,
			fmFreq, fmFreqMin, fmFreqMax, fmRatio, fmRatioMin, fmRatioMax,
			i_source0, i_dest0, mmValue0, i_scale0, i_source1, i_dest1, mmValue1, i_scale1,
			i_source2, i_dest2, mmValue2, i_scale2,
			i_source3, i_dest3, mmValue3, i_scale3, i_source4, i_dest4, mmValue4, i_scale4,
			i_source5, i_dest5, mmValue5, i_scale5,
			i_source6, i_dest6, mmValue6, i_scale6, i_source7, i_dest7, mmValue7, i_scale7,
			i_source8, i_dest8, mmValue8, i_scale8, i_source9, i_dest9, mmValue9, i_scale9,
			modPitchbend = 0, modModify1 = 0, modModify2 = 0,
			modDelay = 0, modAttack = 0, modDecay = 0, modSustain = 0, modSustainTime = 0, modRelease = 0,
			modDelay2 = 0, modAttack2 = 0, modDecay2 = 0, modSustain2 = 0, modSustainTime2 = 0, modRelease2 = 0,
			modFilterFreq = 0, modFilterRes = 0, modFilterSat = 0, modWetDryMix = 0,
			modFreqLFO1 = 0, modFadeInLFO1 = 0, modFreqLFO2 = 0, modFadeInLFO2 = 0,
			modSlider1 = 0, modSlider2 = 0, modCurve1Freq = 0, modCurve2Freq = 0, modFMDepth=0, modFMFreq=0, modFMRatio=0;

			var outEnv, outEnv2, envFunction, envFunction2, envCurve, envCurve2, envGenFunction2,
			intonationFunc, outFreq, pbend,
			waveFunction, outWave, mod1, mod2,
			del, att, dec, sus, sustime, rel,
			del2, att2, dec2, sus2, sustime2, rel2,
			filterProcessFunction, filterFunction, outFilter, levelControlFunc, dryInputDelayFunc,
			lfo1Function, outLFO1, lfo2Function, outLFO2, lfo1FadeInTime, lfo1FadeInCurve,
			lfo2FadeInTime, lfo2FadeInCurve, randomValue1, randomValue2,
			curve1Function, outCurve1, curve2Function, outCurve2, ampCompFunction, outAmpComp,
			timeControlSpec, sumLevel, sourceIndexArray, destIndexArray, scaleIndexArray,
			sourceArray, destArray, mmValueArray,
			scaleArray, sumSlider1, sumSlider2, sumCurve1Freq, sumCurve2Freq, freqModFunc, fmOut,
			arrAllDestModulations, noteModulation, velModulation, dummyVal;
			var mmMod1 = 0, mmMod2 = 0, mmPbend = 0, mmLevel = 0, mmFilterFreq = 0, mmFilterRes = 0, mmFilterSat = 0;
			var mmFMFreq = 0, mmFMRatio = 0, mmFMDepth = 0;

			del = (delay + modDelay).max(0).min(1);
			att = (attackMin + ((attackMax - attackMin) * (attack + modAttack))).max(0.01).min(20);
			dec = (decayMin + ((decayMax - decayMin) * (decay + modDecay))).max(0.01).min(20);
			sus = (sustain + modSustain).max(0).min(1);
			sustime = (sustainTimeMin +
				((sustainTimeMax - sustainTimeMin) * (sustainTime + modSustainTime))).max(0.01).min(20);
			rel = (releaseMin + ((releaseMax - releaseMin) * (release + modRelease))).max(0.01).min(20);
			envCurve = this.getSynthOption(2);
			envFunction = this.getSynthOption(3);
			outEnv = EnvGen.ar(
				envFunction.value(del, att, dec, sus, sustime, rel, envCurve),
				gate,
				doneAction: 2
			);

			del2 = (delay2 + modDelay2).max(0).min(1);
			att2 = (attackMin2 + ((attackMax2 - attackMin2) * (attack2 + modAttack2))).max(0.01).min(20);
			dec2 = (decayMin2 + ((decayMax2 - decayMin2) * (decay2 + modDecay2))).max(0.01).min(20);
			sus2 = (sustain2 + modSustain2).max(0).min(1);
			sustime2 = (sustainTimeMin2 +
				((sustainTimeMax2 - sustainTimeMin2) * (sustainTime2 + modSustainTime2))).max(0.01).min(20);
			rel2 = (releaseMin2 + ((releaseMax2 - releaseMin2) * (release2 + modRelease2))).max(0.01).min(20);
			envCurve2 = this.getSynthOption(4);
			envFunction2 = this.getSynthOption(5);
			envGenFunction2 = this.getSynthOption(14);
			outEnv2 = envGenFunction2.value(envFunction2, del2, att2, dec2, sus2, sustime2, rel2, envCurve2, gate);

			lfo1FadeInTime = (lfo1FadeInMin +
				((lfo1FadeInMax - lfo1FadeInMin) * (lfo1FadeIn + modFadeInLFO1))).max(0.01).min(20);
			lfo1FadeInCurve = XLine.kr(0.01, 1, lfo1FadeInTime);
			lfo1Function =  this.getSynthOption(8);
			outLFO1 = lfo1FadeInCurve *
			lfo1Function.value(this.getSynthOption(9), this.getSynthOption(10),
				freqLFO1, freqLFO1Min, freqLFO1Max, modFreqLFO1);

			lfo2FadeInTime = (lfo2FadeInMin +
				((lfo2FadeInMax - lfo2FadeInMin) * (lfo2FadeIn + modFadeInLFO2))).max(0.01).min(20);
			lfo2FadeInCurve = XLine.kr(0.01, 1, lfo2FadeInTime);
			lfo2Function =  this.getSynthOption(11);
			outLFO2 = lfo2FadeInCurve *
			lfo2Function.value(this.getSynthOption(12), this.getSynthOption(13),
				freqLFO2, freqLFO2Min, freqLFO2Max, modFreqLFO2);

			sumCurve1Freq = (curve1Freq + modCurve1Freq).max(0).min(1);
			sumCurve2Freq = (curve2Freq + modCurve2Freq).max(0).min(1);
			curve1Function = this.getSynthOption(16);
			outCurve1 = curve1Function.value(bufnumCurve1, sumCurve1Freq, curve1FreqMin, curve1FreqMax)
			.linlin(0, 1, this.getSynthOption(18)[0], this.getSynthOption(18)[1]);
			curve2Function = this.getSynthOption(17);
			outCurve2 =  curve2Function.value(bufnumCurve2, sumCurve2Freq, curve2FreqMin, curve2FreqMax)
			.linlin(0, 1, this.getSynthOption(19)[0], this.getSynthOption(19)[1]);

			sumSlider1 = (slider1 + modSlider1).max(0).min(1);
			sumSlider2 = (slider2 + modSlider2).max(0).min(1);

			randomValue1 = Rand(0, 1);
			randomValue2 = Rand(0, 1);

			noteModulation = note.max(noteModMin).min(noteModMax) -  noteModMin / (noteModMax - noteModMin);
			velModulation = velocity.max(velModMin).min(velModMax) - velModMin / (velModMax - velModMin);

			sourceArray = [0, noteModulation, velModulation, outEnv, outEnv2, outLFO1, outLFO2, outCurve1, outCurve2,
				randomValue1, randomValue2, 1, sumSlider1, sumSlider2];
			destArray = [dummyVal, mmMod1, mmMod2, mmPbend, mmLevel, mmFilterFreq, mmFilterRes, mmFilterSat,
				mmFMFreq, mmFMRatio, mmFMDepth];
			scaleArray = [1, noteModulation, velModulation, outEnv, outEnv2, outLFO1, outLFO2, outCurve1, outCurve2,
				randomValue1, randomValue2, 1, sumSlider1, sumSlider2];
			sourceIndexArray = ["i_source0", "i_source1", "i_source2", "i_source3", "i_source4", "i_source5",
				"i_source6", "i_source7", "i_source8", "i_source9"]
			.collect({arg item, i; this.getSynthArgSpec(item);});
			destIndexArray = ["i_dest0", "i_dest1", "i_dest2", "i_dest3", "i_dest4", "i_dest5",
				"i_dest6", "i_dest7", "i_dest8", "i_dest9"]
			.collect({arg item, i; this.getSynthArgSpec(item);});
			mmValueArray = [mmValue0, mmValue1, mmValue2, mmValue3, mmValue4, mmValue5, mmValue6, mmValue7,
				mmValue8, mmValue9];
			scaleIndexArray = ["i_scale0", "i_scale1", "i_scale2", "i_scale3", "i_scale4", "i_scale5",
				"i_scale6", "i_scale7", "i_scale8", "i_scale9"]
			.collect({arg item, i; this.getSynthArgSpec(item);});

			// build mod matrix modulations
			arrAllDestModulations = destArray.collect ({arg item, i;
				var arrModulations;
				arrModulations = [];
				sourceIndexArray.do({arg itemSourceInd, j;
					if (destIndexArray[j] == i, {
						arrModulations = arrModulations.add(sourceArray[itemSourceInd]
							* (mmValueArray[j] * 0.01)
							* scaleArray[scaleIndexArray[j].asInteger]
					)});
				});
				(arrModulations ?  [0]).sum ;
			});

			dummyVal = arrAllDestModulations[0];
			mmMod1 = arrAllDestModulations[1];
			mmMod2 = arrAllDestModulations[2];
			mmPbend = arrAllDestModulations[3];
			mmLevel = arrAllDestModulations[4];
			mmFilterFreq = arrAllDestModulations[5];
			mmFilterRes = arrAllDestModulations[6];
			mmFilterSat = arrAllDestModulations[7];
			mmFMFreq = arrAllDestModulations[8];
			mmFMRatio = arrAllDestModulations[9];
			mmFMDepth = arrAllDestModulations[10];

			mod1 = modify1Min + ((modify1Max - modify1Min) * (modify1 + modModify1 + mmMod1).max(0).min(1));
			mod2 = modify2Min + ((modify2Max - modify2Min) * (modify2 + modModify2 + mmMod2).max(0).min(1));
			pbend = pitchbendMin + ((pitchbendMax - pitchbendMin)
				* (pitchbend + modPitchbend + mmPbend).max(0).min(1));

			intonationFunc = this.getSynthOption(1);
			outFreq = (intonationFunc.value(
				(note + transpose), intKey) * keytrack) + ((48 + transpose).midicps * (1-keytrack));

			freqModFunc = this.getSynthOption(21);
			fmOut = freqModFunc.value(inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth + mmFMDepth,
				fmFreq, fmFreqMin, fmFreqMax,  modFMFreq + mmFMFreq,
				fmRatio, fmRatioMin, fmRatioMax, modFMRatio + mmFMRatio, outFreq);

			waveFunction = this.getSynthOption(0);
			outWave = waveFunction.value(
				(outFreq * fmOut *  (2 ** (pbend /12))).max(0.midicps),
				mod1,
				mod2
			);
			filterProcessFunction =  this.getSynthOption(6);
			filterFunction =  this.getSynthOption(7);
			levelControlFunc = this.getSynthOption(15);
			dryInputDelayFunc = TXLevelControl.arrDelayFunctions[arrOptions[15]];
			outFilter = filterFunction.value(outWave, filterProcessFunction, filterFreq, filterFreqMin, filterFreqMax,
				filterRes, filterResMin, filterResMax, filterSat, filterSatMin, filterSatMax, wetDryMix,
				modFilterFreq + mmFilterFreq, modFilterRes + mmFilterRes, modFilterSat + mmFilterSat, modWetDryMix,
				levelControlFunc, dryInputDelayFunc);

			sumLevel = (level + mmLevel).max(0).min(1);
			ampCompFunction = this.getSynthOption(20);
			outAmpComp = ampCompFunction.value(outFreq);

			// use TXClean to stop blowups
			Out.ar(out, TXClean.ar(outEnv * outFilter * outAmpComp * sumLevel * (velocity * 0.007874)));
		};
		arrGridPresetNames = ["1 x 1", "2 x 2", "3 x 3", "4 x 4", "5 x 5", "6 x 6", "8 x 8", "9 x 9",
			"10 x 10", "12 x 12", "16 x 16", "24 x 24", "32 x 32"];
		arrGridPresetActions = [
			{this.setSynthArgSpec("gridRows", 1); this.setSynthArgSpec("gridCols", 1); },
			{this.setSynthArgSpec("gridRows", 2); this.setSynthArgSpec("gridCols", 2); },
			{this.setSynthArgSpec("gridRows", 3); this.setSynthArgSpec("gridCols", 3); },
			{this.setSynthArgSpec("gridRows", 4); this.setSynthArgSpec("gridCols", 4); },
			{this.setSynthArgSpec("gridRows", 5); this.setSynthArgSpec("gridCols", 5); },
			{this.setSynthArgSpec("gridRows", 6); this.setSynthArgSpec("gridCols", 6); },
			{this.setSynthArgSpec("gridRows", 8); this.setSynthArgSpec("gridCols", 8); },
			{this.setSynthArgSpec("gridRows", 9); this.setSynthArgSpec("gridCols", 9); },
			{this.setSynthArgSpec("gridRows", 10); this.setSynthArgSpec("gridCols", 10); },
			{this.setSynthArgSpec("gridRows", 12); this.setSynthArgSpec("gridCols", 12); },
			{this.setSynthArgSpec("gridRows", 16); this.setSynthArgSpec("gridCols", 16); },
			{this.setSynthArgSpec("gridRows", 24); this.setSynthArgSpec("gridCols", 24); },
			{this.setSynthArgSpec("gridRows", 32); this.setSynthArgSpec("gridCols", 32); },
		];
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TestNoteVals"],
			["SynthOptionListPlusMinus", "Waveform", arrOptionData, 0],
			["TXMinMaxSliderSplit", "Modify 1", \unipolar, "modify1", "modify1Min", "modify1Max"],
			["TXMinMaxSliderSplit", "Modify 2", \unipolar, "modify2", "modify2Min", "modify2Max"],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
			["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 20],
			["MIDIListenCheckBox"],
			["MIDIChannelSelector"],
			["MIDINoteSelector"],
			["MIDIVelSelector"],
			["TXCheckBox", "Keyboard tracking", "keytrack"],
			["Transpose"],
			["TXMinMaxSliderSplit", "Pitch bend",
				ControlSpec(-48, 48), "pitchbend", "pitchbendMin", "pitchbendMax"],
			["PolyphonySelector"],
			["SynthOptionPopupPlusMinus", "Intonation", arrOptionData, 1, nil,
				{arg view; this.updateIntString(view.value)}],
			["TXStaticText", "Note ratios",
				{TXIntonation.arrScalesText.at(arrOptions.at(1));},
				{arg view; ratioView = view}],
			["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F",
				"F#", "G", "G#", "A", "A#", "B"], "intKey", nil, 140],

			["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Attack", timeSpec, "attack", "attackMin", "attackMax",{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Decay", timeSpec, "decay", "decayMin", "decayMax",{{this.updateEnvView;}.defer;}],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime", "sustainTimeMin",
				"sustainTimeMax",{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Release", timeSpec, "release", "releaseMin", "releaseMax",{{this.updateEnvView;}.defer;}],
			["SynthOptionPopup", "Curve", arrOptionData, 2, 240, {system.showView;}],
			["SynthOptionPopup", "Env Type", arrOptionData, 3, 300],

			["SynthOptionCheckBox", "Envelope 2", arrOptionData, 14, 250],
			["EZslider", "Pre-Delay 2", ControlSpec(0,1), "delay2", {{this.updateEnvView2;}.defer;}],
			["TXMinMaxSliderSplit", "Attack 2", timeSpec, "attack2", "attackMin2", "attackMax2",
				{{this.updateEnvView2;}.defer;}],
			["TXMinMaxSliderSplit", "Decay 2", timeSpec, "decay2", "decayMin2", "decayMax2",{{this.updateEnvView2;}.defer;}],
			["EZslider", "Sustain level 2", ControlSpec(0, 1), "sustain2", {{this.updateEnvView2;}.defer;}],
			["TXMinMaxSliderSplit", "Sustain time 2", timeSpec, "sustainTime2", "sustainTimeMin2",
				"sustainTimeMax2",{{this.updateEnvView2;}.defer;}],
			["TXMinMaxSliderSplit", "Release 2", timeSpec, "release2", "releaseMin2", "releaseMax2",
				{{this.updateEnvView2;}.defer;}],
			["SynthOptionPopup", "Curve 2", arrOptionData, 4, 240, {system.showView;}],
			["SynthOptionPopup", "Env Type 2", arrOptionData, 5, 300],

			["SynthOptionCheckBox", "Filter", arrOptionData, 7, 250],
			["SynthOptionListPlusMinus", "Filter Type", arrOptionData, 6, 480, 140],
			["TXMinMaxSliderSplit", "Filter Frequency", ControlSpec(0.midicps, 20000, \exponential),
				"filterFreq", "filterFreqMin", "filterFreqMax", nil, arrFreqRangePresets],
			["TXMinMaxSliderSplit", "Filter Resonance", ControlSpec(0, 1), "filterRes", "filterResMin", "filterResMax"],
			["TXMinMaxSliderSplit", "Filter Saturation", ControlSpec(0, 1), "filterSat", "filterSatMin", "filterSatMax"],
			["SynthOptionPopupPlusMinus", "Filter Level control", arrOptionData, 15],
			["WetDryMixSlider"],

			["SynthOptionCheckBox", "LFO 1", arrOptionData, 8, 250],
			["TXMinMaxSliderSplit", "LFO 1 Freq", ControlSpec(0.01, 100, \exp, 0, 1, units: " Hz"),
				"freqLFO1", "freqLFO1Min", "freqLFO1Max", nil, TXLFO.arrLFOFreqRanges],
			["SynthOptionPopupPlusMinus", "LFO 1 Waveform", arrOptionData, 9],
			["SynthOptionPopupPlusMinus", "LFO 1 Output range", arrOptionData, 10],

			["SynthOptionCheckBox", "LFO 2", arrOptionData, 11, 250],
			["TXMinMaxSliderSplit", "LFO 2 Freq", ControlSpec(0.01, 100, \exp, 0, 1, units: " Hz"),
				"freqLFO2", "freqLFO2Min", "freqLFO2Max", nil, TXLFO.arrLFOFreqRanges],
			["SynthOptionPopupPlusMinus", "LFO 2 Waveform", arrOptionData, 12],
			["SynthOptionPopupPlusMinus", "LFO 2 Output range", arrOptionData, 13],

			["SynthOptionPopupPlusMinus", "Curve 1 Type", arrOptionData, 16],
			["TXMinMaxSliderSplit", "Curve 1 Freq", ControlSpec(0.01, 100, \exp, 0, 1, units: " Hz"),
				"curve1Freq", "curve1FreqMin", "curve1FreqMax", nil, TXLFO.arrLFOFreqRanges],
			["SynthOptionPopupPlusMinus", "Curve 2 Type", arrOptionData, 17],
			["TXMinMaxSliderSplit", "Curve 2 Freq", ControlSpec(0.01, 100, \exp, 0, 1, units: " Hz"),
				"curve2Freq", "curve2FreqMin", "curve2FreqMax", nil, TXLFO.arrLFOFreqRanges],
			["SynthOptionListPlusMinus", "Freq modulation", arrOptionData, 21],
			["TXMinMaxSliderSplit", "FM depth", classData.fmDepthSpec,
				"fmDepth", "fmDepthMin", "fmDepthMax",],
			["TXMinMaxSliderSplit", "FM freq", classData.fmFreqSpec,
				"fmFreq", "fmFreqMin", "fmFreqMax",],
			["TXMinMaxSliderSplit", "FM ratio", classData.fmRatioSpec,
				"fmRatio", "fmRatioMin", "fmRatioMax",],
		]);
		//	initialise buffer to linear curve
		arrCurve1Values = Array.newClear(512).seriesFill(0, 1/511);
		arrCurve2Values = Array.newClear(512).seriesFill(0, 1/511);
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.midiNoteInit;
		//	make the buffer, load the synthdef and create the group
		this.makeBuffersAndGroup(classData.arrBufferSpecs);
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// buffer store
			this.bufferStore(0, arrCurve1Values);
			this.bufferStore(1, arrCurve2Values);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		//	initialise slots to linear curves
		arrSlotData = arrCurve1Values.dup(5);
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Waveform", {displayOption = "showWaveform";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showWaveform")],
			["Spacer", 3],
			["ActionButton", "MIDI/ Note", {displayOption = "showMIDI";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
			["Spacer", 3],
			["ActionButton", "Filter", {displayOption = "showFilter";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showFilter")],
			["Spacer", 3],
			["ActionButton", "Amp Envelope", {displayOption = "showEnv";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showEnv")],
			["Spacer", 3],
			["ActionButton", "Envelope 2", {displayOption = "showEnv2";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showEnv2")],
			["Spacer", 3],
			["ActionButton", "LFO 1/2", {displayOption = "showLFO";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showLFO")],
			["Spacer", 3],
			["ActionButton", "Curve 1", {displayOption = "showCurve1";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showCurve1")],
			["Spacer", 3],
			["ActionButton", "Curve 2", {displayOption = "showCurve2";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showCurve2")],
			["Spacer", 3],
			["ActionButton", "FM", {displayOption = "showFM";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showFM")],
			["Spacer", 3],
			["ActionButton", "Mod Matrix", {displayOption = "showModMatrix";
				this.buildGuiSpecArray; system.showView;}, 120,
			TXColor.white, this.getButtonColour(displayOption == "showModMatrix")],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showWaveform", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Waveform", arrOptionData, 0, 480, 140, {system.flagGuiUpd}],
				["TextViewDisplay", {TXWaveForm.arrDescriptions.at(arrOptions.at(0).asInteger);}, 432],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 1", \unipolar, "modify1", "modify1Min", "modify1Max"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 2", \unipolar, "modify2", "modify2Min", "modify2Max"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0, 1), "level"],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 20],
			];
		});
		if (displayOption == "showMIDI", {
			guiSpecArray = guiSpecArray ++[
				["MIDIListenCheckBox"],
				["NextLine"],
				["MIDIChannelSelector"],
				["NextLine"],
				["MIDINoteSelector"],
				["NextLine"],
				["MIDIVelSelector"],
				["DividingLine"],
				["TXCheckBox", "Keyboard tracking", "keytrack"],
				["DividingLine"],
				["Transpose"],
				["DividingLine"],
				["TXMinMaxSliderSplit", "Pitch bend", ControlSpec(-48, 48), "pitchbend",
					"pitchbendMin", "pitchbendMax", nil,
					[	["Bend Range Presets: ", [-2, 2]], ["Range -1 to 1", [-1, 1]], ["Range -2 to 2", [-2, 2]],
						["Range -7 to 7", [-7, 7]], ["Range -12 to 12", [-12, 12]],
						["Range -24 to 24", [-24, 24]], ["Range -48 to 48", [-48, 48]] ] ],
				["DividingLine"],
				["PolyphonySelector"],
				["DividingLine"],
				["SynthOptionPopupPlusMinus", "Intonation", arrOptionData, 1, 300,
					{arg view; this.updateIntString(view.value)}],
				["Spacer", 10],
				["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F",
					"F#", "G", "G#", "A", "A#", "B"], "intKey", nil, 130],
				["NextLine"],
				["TXStaticText", "Note ratios",
					{TXIntonation.arrScalesText.at(arrOptions.at(1));},
					{arg view; ratioView = view}],
				["DividingLine"],
				["MIDIKeyboard", {arg note; this.createSynthNote(note, testMIDIVel, 0);},
					6, 60, nil, 24, {arg note; this.releaseSynthGate(note);}, "Notes: C0 - B6"],
			];
		});
		if (displayOption == "showEnv", {
			guiSpecArray = guiSpecArray ++[
				["TXPresetPopup", "Env presets",
					TXEnvPresets.arrEnvPresets(this, 2, 3).collect({arg item, i; item.at(0)}),
					TXEnvPresets.arrEnvPresets(this, 2, 3).collect({arg item, i; item.at(1)})
				],
				["TXEnvPlot", {this.envViewValues;}, {arg view; envView = view;}],
				["NextLine"],
				["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Attack", timeSpec, "attack", "attackMin", "attackMax",{{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Decay", timeSpec, "decay", "decayMin", "decayMax",{{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime", "sustainTimeMin",
					"sustainTimeMax",{{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Release", timeSpec, "release", "releaseMin", "releaseMax",{{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["SynthOptionPopup", "Curve", arrOptionData, 2, 240, {system.showView;}],
				["Spacer", 20],
				["SynthOptionPopup", "Env Type", arrOptionData, 3, 300],
			];
		});
		if (displayOption == "showEnv2", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionCheckBox", "Envelope 2", arrOptionData, 14, 250],
				["NextLine"],
				["TXPresetPopup", "Env presets",
					TXEnvPresets.arrEnvPresets2(this, 4, 5).collect({arg item, i; item.at(0)}),
					TXEnvPresets.arrEnvPresets2(this, 4, 5).collect({arg item, i; item.at(1)})
				],
				["TXEnvPlot", {this.envViewValues2;}, {arg view; envView2 = view;}],
				["NextLine"],
				["EZslider", "Pre-Delay", ControlSpec(0,1), "delay2", {{this.updateEnvView2;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Attack", timeSpec, "attack2", "attackMin2", "attackMax2",{{this.updateEnvView2;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Decay", timeSpec, "decay2", "decayMin2", "decayMax2",{{this.updateEnvView2;}.defer;}],
				["SpacerLine", 2],
				["EZslider", "Sustain level", ControlSpec(0, 1), "sustain2", {{this.updateEnvView2;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime2", "sustainTimeMin2",
					"sustainTimeMax2",{{this.updateEnvView2;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Release", timeSpec, "release2", "releaseMin2", "releaseMax2",{{this.updateEnvView2;}.defer;}],
				["SpacerLine", 2],
				["SynthOptionPopup", "Curve", arrOptionData, 4, 240, {system.showView;}],
				["Spacer", 20],
				["SynthOptionPopup", "Env Type", arrOptionData, 5, 300],
			];
		});
		if (displayOption == "showFilter", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionCheckBox", "Filter", arrOptionData, 7, 250],
				["SpacerLine", 4],
				["SynthOptionListPlusMinus", "Filter Type", arrOptionData, 6, 480, 140],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Frequency", ControlSpec(0.midicps, 20000, \exponential),
					"filterFreq", "filterFreqMin", "filterFreqMax", nil, arrFreqRangePresets],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Resonance", ControlSpec(0, 1), "filterRes", "filterResMin", "filterResMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Saturation", ControlSpec(0, 1), "filterSat", "filterSatMin", "filterSatMax"],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Level control", arrOptionData, 15],
				["SpacerLine", 4],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showLFO", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionCheckBox", "LFO 1", arrOptionData, 8, 150],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Waveform", arrOptionData, 9, 420],
				["Spacer", 20],
				["ActionButton", "Freq x 2", {this.freqMultiply(2, "freqLFO1", "freqLFO1Min", "freqLFO1Max");}, 60],
				["ActionButton", "Freq x 3", {this.freqMultiply(3, "freqLFO1", "freqLFO1Min", "freqLFO1Max");}, 60],
				["ActionButton", "Freq / 2", {this.freqMultiply(0.5, "freqLFO1", "freqLFO1Min", "freqLFO1Max");}, 60],
				["ActionButton", "Freq / 3", {this.freqMultiply(1/3, "freqLFO1", "freqLFO1Min", "freqLFO1Max");}, 60],
				["NextLine"],
				["TXFreqBpmMinMaxSldr", "Freq / BPM", lfoFreqSpec,
					"freqLFO1", "freqLFO1Min", "freqLFO1Max", nil, TXLFO.arrLFOFreqRanges],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Fade-in time", ControlSpec(0.01, 20, \exp),
					"lfo1FadeIn", "lfo1FadeInMin", "lfo1FadeInMax"],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 10, 340],
				["SpacerLine", 4],
				["DividingLine"],
				["SpacerLine", 4],
				["SynthOptionCheckBox", "LFO 2", arrOptionData, 11, 150],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Waveform", arrOptionData, 12, 420],
				["Spacer", 20],
				["ActionButton", "Freq x 2", {this.freqMultiply(2, "freqLFO2", "freqLFO2Min", "freqLFO2Max");}, 60],
				["ActionButton", "Freq x 3", {this.freqMultiply(3, "freqLFO2", "freqLFO2Min", "freqLFO2Max");}, 60],
				["ActionButton", "Freq / 2", {this.freqMultiply(0.5, "freqLFO2", "freqLFO2Min", "freqLFO2Max");}, 60],
				["ActionButton", "Freq / 3", {this.freqMultiply(1/3, "freqLFO2", "freqLFO2Min", "freqLFO2Max");}, 60],
				["NextLine"],
				["TXFreqBpmMinMaxSldr", "Freq / BPM", lfoFreqSpec,
					"freqLFO2", "freqLFO2Min", "freqLFO2Max", nil, TXLFO.arrLFOFreqRanges],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Fade-in time", ControlSpec(0.01, 20, \exp),
					"lfo2FadeIn", "lfo2FadeInMin", "lfo2FadeInMax"],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 13, 340],
			];
		});
		if (displayOption == "showCurve1", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Curve 1 Type", arrOptionData, 16, 270],
				["Spacer", 20],
				["ActionButton", "Freq x 2", {this.freqMultiply(2, "curve1Freq", "curve1FreqMin", "curve1FreqMax");}, 60],
				["ActionButton", "Freq x 3", {this.freqMultiply(3, "curve1Freq", "curve1FreqMin", "curve1FreqMax");}, 60],
				["ActionButton", "Freq / 2", {this.freqMultiply(0.5, "curve1Freq", "curve1FreqMin", "curve1FreqMax");}, 60],
				["ActionButton", "Freq / 3", {this.freqMultiply(1/3, "curve1Freq", "curve1FreqMin", "curve1FreqMax");}, 60],
				["NextLine"],
				["TXFreqBpmMinMaxSldr", "Freq / BPM", lfoFreqSpec, "curve1Freq", "curve1FreqMin", "curve1FreqMax",
					nil, TXLFO.arrLFOFreqRanges, ],
				["SpacerLine", 2],
				["TXCurveDraw", "Curve 1", {arrCurve1Values},
					{arg view; arrCurve1Values = view.value; arrSlotData = view.arrSlotData;
						this.bufferStore(0, view.value);},
					{arrSlotData}, "Curve", 518, 256, nil, "gridRows", "gridCols", nil, nil, curve1DataEvent],
				["SpacerLine", 2],
				["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 99), "gridRows", {system.showView}],
				["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 99), "gridCols", {system.showView}],
				["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 182],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 18, 340],
				["Spacer", 20],
				["ActionButton", "Quantise to grid", {this.quantiseCurve1ToGrid(quantizeRows: true, quantizeCols: true)}, 94],
				["ActionButton", "Quantise rows", {this.quantiseCurve1ToGrid(quantizeRows: true, quantizeCols: false)}, 88],
				["ActionButton", "Quantise columns", {this.quantiseCurve1ToGrid(quantizeRows: false, quantizeCols: true)}, 102],
			];
		});
		if (displayOption == "showCurve2", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Curve 2 Type", arrOptionData, 17, 270],
				["Spacer", 20],
				["ActionButton", "Freq x 2", {this.freqMultiply(2, "curve2Freq", "curve2FreqMin", "curve2FreqMax");}, 60],
				["ActionButton", "Freq x 3", {this.freqMultiply(3, "curve2Freq", "curve2FreqMin", "curve2FreqMax");}, 60],
				["ActionButton", "Freq / 2", {this.freqMultiply(0.5, "curve2Freq", "curve2FreqMin", "curve2FreqMax");}, 60],
				["ActionButton", "Freq / 3", {this.freqMultiply(1/3, "curve2Freq", "curve2FreqMin", "curve2FreqMax");}, 60],
				["NextLine"],
				["TXFreqBpmMinMaxSldr", "Freq / BPM", lfoFreqSpec, "curve2Freq", "curve2FreqMin", "curve2FreqMax",
					nil, TXLFO.arrLFOFreqRanges, ],
				["SpacerLine", 2],
				["TXCurveDraw", "Curve 2", {arrCurve2Values},
					{arg view; arrCurve2Values = view.value; arrSlotData = view.arrSlotData;
						this.bufferStore(1, view.value);},
					{arrSlotData}, "Curve", 518, 256, nil, "gridRows", "gridCols", nil, nil, curve2DataEvent],
				["SpacerLine", 2],
				["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 99), "gridRows", {system.showView}],
				["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 99), "gridCols", {system.showView}],
				["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 182],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 19, 340],
				["Spacer", 20],
				["ActionButton", "Quantise to grid", {this.quantiseCurve2ToGrid(quantizeRows: true, quantizeCols: true)}, 94],
				["ActionButton", "Quantise rows", {this.quantiseCurve2ToGrid(quantizeRows: true, quantizeCols: false)}, 88],
				["ActionButton", "Quantise columns", {this.quantiseCurve2ToGrid(quantizeRows: false, quantizeCols: true)}, 102],
			];
		});
		if (displayOption == "showFM", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Freq modulation", arrOptionData, 21],
				["SpacerLine", 6],
				["TXMinMaxFreqNoteSldr", "FM freq", classData.fmFreqSpec,
					"fmFreq", "fmFreqMin", "fmFreqMax", nil, TXFreqRangePresets.arrFreqRanges],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "FM ratio", classData.fmRatioSpec,
					"fmRatio", "fmRatioMin", "fmRatioMax",],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "FM depth", classData.fmDepthSpec,
					"fmDepth", "fmDepthMin", "fmDepthMax",],
			];
		});
		if (displayOption == "showModMatrix", {
			guiSpecArray = guiSpecArray ++[
				["NoteRangeSelector", "Note range", "noteModMin", "noteModMax"],
				["SpacerLine", 2],
				["TXRangeSlider", "Vel range", ControlSpec(0, 127, step: 1), "velModMin", "velModMax"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Slider 1", ControlSpec(0,1), "slider1"],
				["SpacerLine", 2],
				["EZslider", "Slider 2", ControlSpec(0,1), "slider2"],
				["DividingLine"],
				["SpacerLine", 6],
				["TextBar", "Source", 100],
				["TextBar", "Destination", 100],
				["TextBar", "Modulation amount", 390],
				["TextBar", "Scale", 97],
				["NextLine"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames,
					"i_source0", "i_dest0", "mmValue0", arrMMScaleNames, "i_scale0", 350],
				["NextLine"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames,
					"i_source1", "i_dest1", "mmValue1", arrMMScaleNames, "i_scale1", 350],
				["NextLine"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames,
					"i_source2", "i_dest2", "mmValue2", arrMMScaleNames, "i_scale2", 350],
				["NextLine"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames,
					"i_source3", "i_dest3", "mmValue3", arrMMScaleNames, "i_scale3", 350],
				["NextLine"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames,
					"i_source4", "i_dest4", "mmValue4", arrMMScaleNames, "i_scale4", 350],
				["NextLine"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames,
					"i_source5", "i_dest5", "mmValue5", arrMMScaleNames, "i_scale5", 350],
				["NextLine"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames,
					"i_source6", "i_dest6", "mmValue6", arrMMScaleNames, "i_scale6", 350],
				["NextLine"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames,
					"i_source7", "i_dest7", "mmValue7", arrMMScaleNames, "i_scale7", 350],
				["NextLine"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames,
					"i_source8", "i_dest8", "mmValue8", arrMMScaleNames, "i_scale8", 350],
				["NextLine"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames,
					"i_source9", "i_dest9", "mmValue9", arrMMScaleNames, "i_scale9", 350],
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

	quantiseCurve1ToGrid {arg quantizeRows = true, quantizeCols = true;
		arrCurve1Values = this.quantiseToGrid(arrCurve1Values, quantizeRows, quantizeCols);
		this.bufferStore(0, arrCurve1Values);
		system.showView;
	}

	quantiseCurve2ToGrid {arg quantizeRows = true, quantizeCols = true;
		arrCurve2Values = this.quantiseToGrid(arrCurve2Values, quantizeRows, quantizeCols);
		this.bufferStore(1, arrCurve2Values);
		system.showView;
	}

	quantiseToGrid {arg inputArray, quantizeRows = true, quantizeCols = true;
		var holdArray, holdSignal, outArray, holdCols;
		var rows, cols;
		var maxVal = 512;
		holdArray = Array.newClear(maxVal);
		rows = this.getSynthArgSpec("gridRows");
		cols = this.getSynthArgSpec("gridCols");

		if (quantizeCols, {
			cols.do({arg item;
				var jump, startRange, endRange, meanVal;
				jump = cols.reciprocal;
				startRange = (item * jump * maxVal).round(1);
				endRange = ((item + 1) * jump * maxVal).round(1) - 1;
				meanVal = arrCurve1Values.copyRange(startRange.asInteger, endRange.asInteger).mean;
				if (quantizeRows, {
					meanVal = meanVal.round(rows.reciprocal);
				});
				holdArray[startRange.asInteger..endRange.asInteger] = meanVal;
			});
		},{
			holdArray = arrCurve1Values.collect({arg item;
				var outVal = item;
				if (quantizeRows, {
					outVal = outVal.round(rows.reciprocal);
				});
				outVal;
			});
		});
		holdSignal = Signal.newFrom(holdArray);
		outArray = Array.newFrom(holdSignal);
		^outArray;
	}

	freqMultiply { arg argMultiplyValue, argFreqString, argFreqMinString, argFreqMaxString;
		var currentFreq, minFreq, maxFreq, holdControlSpec, newFreq;
		minFreq = this.getSynthArgSpec(argFreqMinString);
		maxFreq = this.getSynthArgSpec(argFreqMaxString);
		holdControlSpec = ControlSpec.new(minFreq, maxFreq, \exp);
		currentFreq = holdControlSpec.map(this.getSynthArgSpec(argFreqString));
		newFreq = currentFreq * argMultiplyValue;
		if (argMultiplyValue < 1, {
			if ( newFreq >= minFreq, {
				this.setSynthValue(argFreqString, holdControlSpec.unmap(newFreq));
			});
		},{
			if ( newFreq <= maxFreq, {
				this.setSynthValue(argFreqString, holdControlSpec.unmap(newFreq));
			});
		});
		system.flagGuiIfModDisplay(this);
	}

	bufferStore { arg argIndex, argArrayVals;
		buffers.at(argIndex).sendCollection(argArrayVals);
	}

	extraSaveData { // override default method
		^[testMIDINote, testMIDIVel, testMIDITime, arrCurve1Values, arrCurve2Values, arrSlotData];
	}

	loadExtraData {arg argData;  // override default method
		testMIDINote = argData.at(0);
		testMIDIVel = argData.at(1);
		testMIDITime = argData.at(2);
		arrCurve1Values = argData.at(3);
		arrCurve2Values = argData.at(4);
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// buffer store
			this.bufferStore(0, arrCurve1Values);
			this.bufferStore(1, arrCurve2Values);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		arrSlotData = argData.at(5);
	}

	updateIntString{arg argIndex;
		if (ratioView.notNil, {
			if (ratioView.notClosed, {
				ratioView.string = TXIntonation.arrScalesText.at(argIndex);
			});
		});
	}

	envViewValues {
		var attack, attackMin, attackMax, decay, decayMin, decayMax, sustain;
		var sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax;
		var del, att, dec, sus, sustime, rel, envCurve;

		del = this.getSynthArgSpec("delay");
		attack = this.getSynthArgSpec("attack");
		attackMin = this.getSynthArgSpec("attackMin");
		attackMax = this.getSynthArgSpec("attackMax");
		att = attackMin + ((attackMax - attackMin) * attack);
		decay = this.getSynthArgSpec("decay");
		decayMin = this.getSynthArgSpec("decayMin");
		decayMax = this.getSynthArgSpec("decayMax");
		dec = decayMin + ((decayMax - decayMin) * decay);
		sus = this.getSynthArgSpec("sustain");
		sustainTime = this.getSynthArgSpec("sustainTime");
		sustainTimeMin = this.getSynthArgSpec("sustainTimeMin");
		sustainTimeMax = this.getSynthArgSpec("sustainTimeMax");
		sustime = sustainTimeMin + ((sustainTimeMax - sustainTimeMin) * sustainTime);
		release = this.getSynthArgSpec("release");
		releaseMin = this.getSynthArgSpec("releaseMin");
		releaseMax = this.getSynthArgSpec("releaseMax");
		rel = releaseMin + ((releaseMax - releaseMin) * release);
		envCurve = this.getSynthOption(2);
		^[[0, 0, 1, sus, sus, 0].max(0.001), [del, att, dec, sustime, rel], envCurve];
	}

	envViewValues2 {
		var attack, attackMin, attackMax, decay, decayMin, decayMax, sustain;
		var sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax;
		var del, att, dec, sus, sustime, rel, envCurve;

		del = this.getSynthArgSpec("delay2");
		attack = this.getSynthArgSpec("attack2");
		attackMin = this.getSynthArgSpec("attackMin2");
		attackMax = this.getSynthArgSpec("attackMax2");
		att = attackMin + ((attackMax - attackMin) * attack);
		decay = this.getSynthArgSpec("decay2");
		decayMin = this.getSynthArgSpec("decayMin2");
		decayMax = this.getSynthArgSpec("decayMax2");
		dec = decayMin + ((decayMax - decayMin) * decay);
		sus = this.getSynthArgSpec("sustain2");
		sustainTime = this.getSynthArgSpec("sustainTime2");
		sustainTimeMin = this.getSynthArgSpec("sustainTimeMin2");
		sustainTimeMax = this.getSynthArgSpec("sustainTimeMax2");
		sustime = sustainTimeMin + ((sustainTimeMax - sustainTimeMin) * sustainTime);
		release = this.getSynthArgSpec("release2");
		releaseMin = this.getSynthArgSpec("releaseMin2");
		releaseMax = this.getSynthArgSpec("releaseMax2");
		rel = releaseMin + ((releaseMax - releaseMin) * release);
		envCurve = this.getSynthOption(4);
		^[[0, 0, 1, sus, sus, 0].max(0.001), [del, att, dec, sustime, rel], envCurve];
	}

	updateEnvView {
		if (envView.respondsTo('notClosed'), {
			if (envView.notClosed, {
				envView.value = this.envViewValues;
			});
		});
	}

	updateEnvView2 {
		if (envView2.respondsTo('notClosed'), {
			if (envView2.notClosed, {
				envView2.value = this.envViewValues2;
			});
		});
	}

}

