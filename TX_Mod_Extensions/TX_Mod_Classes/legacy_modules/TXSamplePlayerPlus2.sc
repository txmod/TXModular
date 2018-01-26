// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSamplePlayerPlus2 : TXModuleBase {

	// Note: TXSamplePlayerPlus is different to TXSamplePlayerPlusSt because it is not only mono, but has extra loop type "Single-Waveform"

	classvar <>classData;

	var	timeSpec, lfoFreqSpec;
	var	arrFreqRangePresets;
	var	arrMMSourceNames, arrMMDestNames, arrMMScaleNames;
	var <>sampleNo = 0;
	var <>bankNo = 0;
	var <>sampleData;
	var sampleFileName = "";
	var showWaveform = 0;
	var sampleNumChannels = 0;
	var sampleFreq = 440;
	var displayOption;
	var ratioView;
	var envView, envView2;
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
		classData.defaultName = "Sample Player+";
		classData.moduleRate = "audio";
		classData.moduleType = "groupsource";
		classData.arrCtlSCInBusSpecs = [
			["Sample Start", 1, "modStart", 0],
			["Sample End", 1, "modEnd", 0],
			["Sample Reverse", 1, "modReverse", 0],
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
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.guiWidth = 730;
		classData.maxWavetableSize = 65536;
		classData.arrBufferSpecs = [ ["bufnumSample", 2048,1], ["bufnumWavetable", classData.maxWavetableSize, 1],
			["bufnumCurve1", 512, 1], ["bufnumCurve2", 512, 1]
		];
	} // end of method initClass

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showSample";
		timeSpec = ControlSpec(0.01, 20);
		lfoFreqSpec = ControlSpec(0.001, 100, \exp, 0, 1, units: " Hz");
		curve1DataEvent = ();
		curve2DataEvent = ();
		arrFreqRangePresets = TXFilter.arrFreqRanges;
		arrMMSourceNames = ["...", "Note", "Velocity", "Amp Env", "Env 2", "LFO 1", "LFO 2",
			"Curve 1", "Curve 2", "Random val 1", "Random val 2", "Offset val", "Slider 1", "Slider 2"];
		arrMMDestNames = ["...", "Sample start", "Sample end", "Pitchbend", "Level",
			"Filter Freq", "Filter Res", "Filter Sat"];
		arrMMScaleNames = ["...", "Note", "Velocity", "Amp Env", "Env 2", "LFO 1", "LFO 2",
			"Curve 1", "Curve 2", "Random val 1", "Random val 2", "Offset val", "Slider 1", "Slider 2"];
		arrSynthArgSpecs = [
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
			["bufnumSample", 0, \ir],
			["bufnumWavetable", 0, \ir],
			["bankNo", 0, \ir],
			["sampleNo", 0, \ir],
			["sampleFreq", 440, \ir],
			["start", 0, defLagTime],
			["end", 1, defLagTime],
			["reverse", 0, defLagTime],
			["level", 0.5, defLagTime],
			["envtime", 0, \ir],
			["delay", 0, \ir],
			["attack", 0.01, \ir],
			["attackMin", 0.01, \ir],
			["attackMax", 5, \ir],
			["decay", 0.05, \ir],
			["decayMin", 0.01, \ir],
			["decayMax", 5, \ir],
			["sustain", 1, \ir],
			["sustainTime", 0.2, \ir],
			["sustainTimeMin", 0.01, \ir],
			["sustainTimeMax", 5, \ir],
			["release", 0.01, \ir],
			["releaseMin", 0.01, \ir],
			["releaseMax", 5, \ir],
			["envtime2", 0, \ir],
			["delay2", 0, \ir],
			["attack2", 0.02, \ir],
			["attackMin2", 0, \ir],
			["attackMax2", 5, \ir],
			["decay2", 0.05, \ir],
			["decayMin2", 0, \ir],
			["decayMax2", 5, \ir],
			["sustain2", 1, \ir],
			["sustainTime2", 0.05, \ir],
			["sustainTimeMin2", 0, \ir],
			["sustainTimeMax2", 5, \ir],
			["release2", 0.1, \ir],
			["releaseMin2", 0, \ir],
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

			["modStart", 0, defLagTime],
			["modEnd", 0, defLagTime],
			["modReverse", 0, defLagTime],
			["modPitchbend", 0, defLagTime],
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
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
		// create looping option
		arrOptions = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
		arrOptionData = [
			[	["Single shot",
				{arg outRate, bufnumSample, start, end;
					BufRd.ar(1, bufnumSample,
						(Sweep.ar(1, outRate * BufSampleRate.kr(bufnumSample)) +
							(((start * outRate.sign.max(0)) + (end * outRate.sign.neg.max(0)))
								* BufFrames.kr(bufnumSample))
						)
						.min(end * BufFrames.kr(bufnumSample))
						.max(start * BufFrames.kr(bufnumSample))
						,0
					);
				}
			],
			["Looped",
				{arg outRate, bufnumSample, start, end;
					BufRd.ar(1, bufnumSample,
						Phasor.ar(0, outRate * BufRateScale.kr(bufnumSample),
							start * BufFrames.kr(bufnumSample),
							end * BufFrames.kr(bufnumSample))
					);
				}
			],
			["Single-Waveform",
				{arg outRate, bufnumSample, start, end, freq, bufnumWavetable;
					Osc.ar(bufnumWavetable, freq, 0);
				}
			],

			["X-Fade Looped",
				{arg outRate, bufnumSample, start, end;
					var startFrame, endFrame, offset, bufdur;
					startFrame = start * BufFrames.kr(bufnumSample);
					endFrame = end * BufFrames.kr(bufnumSample);
					offset = (endFrame - startFrame) * 0.5;
					bufdur = abs(end-start) * BufDur.kr(bufnumSample);
					Mix.new(
						BufRd.ar(1, bufnumSample,
							(Phasor.ar(0, outRate * BufRateScale.kr(bufnumSample), startFrame, endFrame)
								+ [0, offset]
							).wrap(startFrame, endFrame);
						) * SinOsc.kr(0.5 * outRate * bufdur.reciprocal, [0, pi/2]).abs
					);
				}
			],
			],
			// Intonation
			TXIntonation.arrOptionData,
			TXEnvLookup.arrDADSRSlopeOptionData,
			TXEnvLookup.arrDADSRSustainOptionData,
			TXEnvLookup.arrDADSRSlopeOptionData,
			TXEnvLookup.arrDADSRSustainOptionData,
			TXFilter.arrOptionData,
			[
				["Off", {arg input; input; }],
				["On", TXFilter.filterFunction],
			],
			[
				["Off", { 0; }],
				["On", TXLFO.lfoFunction],
			],
			TXLFO.arrOptionData,
			TXLFO.arrLFOOutputRanges,
			[
				["Off", { 0; }],
				["On", TXLFO.lfoFunction],
			],
			TXLFO.arrOptionData,
			TXLFO.arrLFOOutputRanges,
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
			TXLevelControl.arrOptionData,
			[// curve 1
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
			[// curve 2
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
			// curve 1 o/p range
			[
				["Positive only: 0 to 1", [0, 1]],
				["Positive & Negative: -1 to 1", [-1, 1]],
				["Positive & Negative: -0.5 to 0.5", [-0.5, 0.5]],
				["Negative only: -1 to 0", [-1, 0]],
			],
			// curve 2 o/p range
			[
				["Positive only: 0 to 1", [0, 1]],
				["Positive & Negative: -1 to 1", [-1, 1]],
				["Positive & Negative: -0.5 to 0.5", [-0.5, 0.5]],
				["Negative only: -1 to 0", [-1, 0]],
			],
			 // amp compensation
			TXAmpComp.arrOptionData,
		];
		synthDefFunc = {
			arg out, bufnumCurve1, bufnumCurve2, gate, note, velocity, keytrack, transpose, pitchbend, pitchbendMin, pitchbendMax,
			bufnumSample, bufnumWavetable, bankNo, sampleNo, sampleFreq, start, end, reverse, level,
			envtime=0, delay, attack, attackMin, attackMax, decay, decayMin, decayMax, sustain,
			sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax,
			envtime2=0, delay2, attack2, attackMin2, attackMax2, decay2, decayMin2, decayMax2, sustain2,
			sustainTime2, sustainTimeMin2, sustainTimeMax2, release2, releaseMin2, releaseMax2,
			intKey,
			filterFreq, filterFreqMin, filterFreqMax, filterRes, filterResMin, filterResMax,
			filterSat, filterSatMin, filterSatMax, wetDryMix,
			freqLFO1, freqLFO1Min, freqLFO1Max, lfo1FadeIn, lfo1FadeInMin, lfo1FadeInMax,
			freqLFO2, freqLFO2Min, freqLFO2Max,  lfo2FadeIn, lfo2FadeInMin, lfo2FadeInMax,
			curve1Freq, curve1FreqMin, curve1FreqMax, curve2Freq, curve2FreqMin, curve2FreqMax,
			velModMin, velModMax, noteModMin, noteModMax, slider1, slider2,
			i_source0, i_dest0, mmValue0, i_scale0, i_source1, i_dest1, mmValue1, i_scale1,
			i_source2, i_dest2, mmValue2, i_scale2,
			i_source3, i_dest3, mmValue3, i_scale3, i_source4, i_dest4, mmValue4, i_scale4,
			i_source5, i_dest5, mmValue5, i_scale5,
			i_source6, i_dest6, mmValue6, i_scale6, i_source7, i_dest7, mmValue7, i_scale7,
			i_source8, i_dest8, mmValue8, i_scale8, i_source9, i_dest9, mmValue9, i_scale9,
			modStart = 0, modEnd = 0, modReverse = 0, modPitchbend = 0, modDelay = 0, modAttack = 0, modDecay = 0,
			modSustain = 0, modSustainTime = 0, modRelease = 0,
			modDelay2 = 0, modAttack2 = 0, modDecay2 = 0, modSustain2 = 0, modSustainTime2 = 0, modRelease2 = 0,
			modFilterFreq = 0, modFilterRes = 0, modFilterSat = 0, modWetDryMix = 0,
			modFreqLFO1 = 0, modFadeInLFO1 = 0, modFreqLFO2 = 0, modFadeInLFO2 = 0,
			modSlider1 = 0, modSlider2 = 0, modCurve1Freq = 0, modCurve2Freq = 0;
			var 	outEnv, outEnv2, envFunction, envFunction2, envCurve, envCurve2, envGenFunction2,
			outFreq, outFreqPb, intonationFunc, pbend, outRate, outFunction, outSample,
			sStart, sEnd, rev, del, att, dec, sus, sustime, rel,
			del2, att2, dec2, sus2, sustime2, rel2,
			filterProcessFunction, filterFunction, outFilter, levelControlFunc, dryInputDelayFunc,
			lfo1Function, outLFO1, lfo2Function, outLFO2, lfo1FadeInTime, lfo1FadeInCurve,
			lfo2FadeInTime, lfo2FadeInCurve, randomValue1, randomValue2,
			curve1Function, outCurve1, curve2Function, outCurve2, ampCompFunction, outAmpComp,
			timeControlSpec, sumLevel, sourceIndexArray, destIndexArray, scaleIndexArray,
			sourceArray, destArray, mmValueArray,
			scaleArray, sumSlider1, sumSlider2, sumCurve1Freq, sumCurve2Freq,
			dummyVal, mmPbend = 0, mmLevel = 0, mmFilterFreq = 0, mmFilterRes = 0, mmFilterSat = 0,
			mmSampleStart = 0, mmSampleEnd = 0,
			arrAllDestModulations, noteModulation, velModulation;

			rev = (reverse + modReverse).max(0).min(1);
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
			destArray = [dummyVal, mmSampleStart, mmSampleEnd, mmPbend, mmLevel, mmFilterFreq, mmFilterRes, mmFilterSat];
			scaleArray = [1, noteModulation, velModulation, outEnv, outEnv2, outLFO1, outLFO2, outCurve1, outCurve2,
				randomValue1, randomValue2, 1, sumSlider1, sumSlider2];
			sourceIndexArray = ["i_source0", "i_source1", "i_source2", "i_source3", "i_source4", "i_source5",
				"i_source6", "i_source7", "i_source8", "i_source9"]
			.collect({arg item, i; this.getSynthArgSpec(item);});
			destIndexArray = ["i_dest0", "i_dest1", "i_dest2", "i_dest3", "i_dest4", "i_dest5", "i_dest6", "i_dest7",
				"i_dest8", "i_dest9"]
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
						arrModulations = arrModulations.add(sourceArray[itemSourceInd] * (mmValueArray[j] * 0.01)
							* scaleArray[scaleIndexArray[j].asInteger]
					)});
				});
				(arrModulations ?  [0]).sum ;
			});

			dummyVal = arrAllDestModulations[0];
			mmSampleStart = arrAllDestModulations[1];
			mmSampleEnd = arrAllDestModulations[2];
			mmPbend = arrAllDestModulations[3];
			mmLevel = arrAllDestModulations[4];
			mmFilterFreq = arrAllDestModulations[5];
			mmFilterRes = arrAllDestModulations[6];
			mmFilterSat = arrAllDestModulations[7];

			sStart = (start + modStart + mmSampleStart).max(0).min(1);
			sEnd = (end + modEnd + mmSampleEnd).max(0).min(1);
			intonationFunc = this.getSynthOption(1);
			outFreq = (intonationFunc.value((note + transpose), intKey) * keytrack)
			+ ((sampleFreq.cpsmidi + transpose).midicps * (1-keytrack));
			pbend = pitchbendMin + ((pitchbendMax - pitchbendMin)
				* (pitchbend + modPitchbend + mmPbend).max(0).min(1));
			outFreqPb = outFreq *  (2 ** (pbend /12));
			outRate = (outFreqPb / sampleFreq) * (rev-0.5).neg.sign;
			outFunction = this.getSynthOption(0);
			sumLevel = (level + mmLevel).max(0).min(1);
			outSample = outFunction.value(outRate, bufnumSample, sStart, sEnd, outFreqPb, bufnumWavetable) * sumLevel;
			filterProcessFunction =  this.getSynthOption(6);
			filterFunction =  this.getSynthOption(7);
			levelControlFunc = this.getSynthOption(15);
			dryInputDelayFunc = TXLevelControl.arrDelayFunctions[arrOptions[15]];
			outFilter = filterFunction.value(outSample, filterProcessFunction, filterFreq, filterFreqMin, filterFreqMax,
				filterRes, filterResMin, filterResMax, filterSat, filterSatMin, filterSatMax, wetDryMix,
				modFilterFreq + mmFilterFreq, modFilterRes + mmFilterRes, modFilterSat + mmFilterSat, modWetDryMix,
				levelControlFunc, dryInputDelayFunc);
			ampCompFunction = this.getSynthOption(20);
			outAmpComp = ampCompFunction.value(outFreq);

			// use TXClean to stop blowups
			Out.ar(out, TXClean.ar(outEnv * outFilter * outAmpComp * (velocity * 0.007874)));
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
			["TXPopupActionPlusMinus", "Sample bank", {system.arrSampleBankNames},
				"bankNo",
				{ arg view; this.bankNo = view.value; this.sampleNo = 0; this.loadSample(0);
					this.setSynthArgSpec("sampleNo", 0); system.showView;}
			],
			// array of sample filenames - beginning with blank sample  - only show mono files
			["TXListViewActionPlusMinus", "Mono sample", {["No Sample"]++system.sampleMonoFileNames(bankNo, true)},
				"sampleNo", { arg view; this.sampleNo = view.value; this.loadSample(view.value); }
			],
			["TXRangeSlider", "Play Range", ControlSpec(0, 1), "start", "end"],
			["SynthOptionPopupPlusMinus", "Loop type", arrOptionData, 0, 260],
			["TXCheckBox", "Reverse", "reverse"],
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
			["SynthOptionPopup", "Intonation", arrOptionData, 1, nil,
				{arg view; this.updateIntString(view.value)}],
			["TXStaticText", "Note ratios",
				{TXIntonation.arrScalesText.at(arrOptions.at(1));},
				{arg view; ratioView = view}],
			["TXPopupActionPlusMinus", "Key / root", ["C", "C#", "D", "D#", "E","F",
				"F#", "G", "G#", "A", "A#", "B"], "intKey", nil, 140],
			["TXEnvPlot", {this.envViewValues;}, {arg view; envView = view;}],
			["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Attack", timeSpec, "attack", "attackMin", "attackMax",
				{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Decay", timeSpec, "decay", "decayMin", "decayMax",
				{{this.updateEnvView;}.defer;}],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime", "sustainTimeMin",
				"sustainTimeMax",{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Release", timeSpec, "release", "releaseMin", "releaseMax",
				{{this.updateEnvView;}.defer;}],
			["SynthOptionPopup", "Curve", arrOptionData, 2, 240, {system.showView;}],
			["SynthOptionPopup", "Env Type", arrOptionData, 3, 300],

			["SynthOptionCheckBox", "Envelope 2", arrOptionData, 14, 250],
			["EZslider", "Pre-Delay 2", ControlSpec(0,1), "delay2", {{this.updateEnvView2;}.defer;}],
			["TXMinMaxSliderSplit", "Attack 2", timeSpec, "attack2", "attackMin2", "attackMax2",
				{{this.updateEnvView2;}.defer;}],
			["TXMinMaxSliderSplit", "Decay 2", timeSpec, "decay2", "decayMin2", "decayMax2",
				{{this.updateEnvView2;}.defer;}],
			["EZslider", "Sustain level 2", ControlSpec(0, 1), "sustain2", {{this.updateEnvView2;}.defer;}],
			["TXMinMaxSliderSplit", "Sustain time 2", timeSpec, "sustainTime2", "sustainTimeMin2",
				"sustainTimeMax2",{{this.updateEnvView2;}.defer;}],
			["TXMinMaxSliderSplit", "Release 2", timeSpec, "release2", "releaseMin2", "releaseMax2",
				{{this.updateEnvView2;}.defer;}],
			["SynthOptionPopup", "Curve 2", arrOptionData, 4, 240, {system.showView;}],
			["SynthOptionPopup", "Env Type 2", arrOptionData, 5, 180],

			["SynthOptionCheckBox", "Filter", arrOptionData, 7, 250],
			["SynthOptionListPlusMinus", "Filter Type", arrOptionData, 6, 480, 140],
			["TXMinMaxSliderSplit", "Filter Frequency", ControlSpec(0.midicps, 20000, \exponential),
				"filterFreq", "filterFreqMin", "filterFreqMax", nil, arrFreqRangePresets],
			["TXMinMaxSliderSplit", "Filter Resonance", ControlSpec(0, 1), "filterRes", "filterResMin",
				"filterResMax"],
			["TXMinMaxSliderSplit", "Filter Saturation", ControlSpec(0, 1), "filterSat", "filterSatMin",
				"filterSatMax"],
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
		]);
		//	initialise buffer to linear curve
		arrCurve1Values = Array.newClear(512).seriesFill(0, 1/511);
		arrCurve2Values = Array.newClear(512).seriesFill(0, 1/511);
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.midiNoteInit;
		//	make buffers, load the synthdef and create the Group for synths to belong to
		this.makeBuffersAndGroup(classData.arrBufferSpecs);
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// buffer store
			this.bufferStore(2, arrCurve1Values);
			this.bufferStore(3, arrCurve2Values);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		//	initialise slots to linear curves
		arrSlotData = arrCurve1Values.dup(5);
	} // end of method init

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Sample", {displayOption = "showSample";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showSample")],
			["Spacer", 3],
			["ActionButton", "MIDI/ Note", {displayOption = "showMIDI";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
			["Spacer", 3],
			["ActionButton", "Filter", {displayOption = "showFilter";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showFilter")],
			["Spacer", 3],
			["ActionButton", "Amp Envelope", {displayOption = "showEnv";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showEnv")],
			["Spacer", 3],
			["ActionButton", "Envelope 2", {displayOption = "showEnv2";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showEnv2")],
			["Spacer", 3],
			["ActionButton", "LFO 1/2", {displayOption = "showLFO";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showLFO")],
			["Spacer", 3],
			["ActionButton", "Curve 1", {displayOption = "showCurve1";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showCurve1")],
			["Spacer", 3],
			["ActionButton", "Curve 2", {displayOption = "showCurve2";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showCurve2")],
			["Spacer", 3],
			["ActionButton", "Mod Matrix", {displayOption = "showModMatrix";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showModMatrix")],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showSample", {
			guiSpecArray = guiSpecArray ++[
				["TXPopupActionPlusMinus", "Sample bank", {system.arrSampleBankNames},
					"bankNo",
					{ arg view; this.bankNo = view.value; this.sampleNo = 0; this.loadSample(0);
						this.setSynthArgSpec("sampleNo", 0); system.showView;}
				],
				// array of sample filenames - beginning with blank sample  - only show mono files
				["TXListViewActionPlusMinus", "Mono sample", {["No Sample"]++system.sampleMonoFileNames(bankNo, true)},
					"sampleNo", { arg view;
						this.sampleNo = view.value;
						this.loadSample(view.value);
						{system.showView}.defer(0.1);   //  refresh view
					}
				],
				["SpacerLine", 3],
				["Spacer", 80],
				["ActionButton", "Add Samples to Sample Bank", {TXBankBuilder2.addSampleDialog("Sample", bankNo)}, 200],
				["ActionButton", "Show", {showWaveform = 1; system.showView;},
					80, TXColor.white, TXColor.sysGuiCol2],
				["ActionButton", "Hide", {showWaveform = 0; system.showView; },
					80, TXColor.white, TXColor.sysDeleteCol],
				["NextLine"],
				["TXSoundFileViewRange", {sampleFileName}, "start", "end", nil, {showWaveform}, nil, nil, nil, 680],
				["SpacerLine", 3],
				["SynthOptionPopupPlusMinus", "Loop type", arrOptionData, 0, 260],
				["SpacerLine", 3],
				["TXCheckBox", "Reverse", "reverse"],
				["SpacerLine", 3],
				["EZslider", "Level", ControlSpec(0, 1), "level"],
				["SpacerLine", 3],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 20],
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
				["TXMinMaxSliderSplit", "Attack", timeSpec, "attack2", "attackMin2", "attackMax2",{{this.updateEnvView2;}.defer;}],
				["TXMinMaxSliderSplit", "Decay", timeSpec, "decay2", "decayMin2", "decayMax2",{{this.updateEnvView2;}.defer;}],
				["EZslider", "Sustain level", ControlSpec(0, 1), "sustain2", {{this.updateEnvView2;}.defer;}],
				["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime2", "sustainTimeMin2",
					"sustainTimeMax2",{{this.updateEnvView2;}.defer;}],
				["TXMinMaxSliderSplit", "Release", timeSpec, "release2", "releaseMin2", "releaseMax2",{{this.updateEnvView2;}.defer;}],
				["NextLine"],
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
		this.bufferStore(2, arrCurve1Values);
		system.showView;
	}

	quantiseCurve2ToGrid {arg quantizeRows = true, quantizeCols = true;
		arrCurve2Values = this.quantiseToGrid(arrCurve2Values, quantizeRows, quantizeCols);
		this.bufferStore(3, arrCurve2Values);
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
		^[sampleNo, sampleFileName, sampleNumChannels, sampleFreq, testMIDINote, testMIDIVel, testMIDITime,
			bankNo, arrCurve1Values, arrCurve2Values, arrSlotData];
	}

	loadExtraData {arg argData;  // override default method
		sampleNo = argData.at(0);
		sampleFileName = argData.at(1);
		// Convert path
		sampleFileName = TXPath.convert(sampleFileName);
		sampleNumChannels = argData.at(2);
		sampleFreq = argData.at(3);
		testMIDINote = argData.at(4);
		testMIDIVel = argData.at(5);
		testMIDITime = argData.at(6);
		bankNo = argData.at(7) ? 0;
		this.loadSample(sampleNo);
		arrCurve1Values = argData.at(8);
		arrCurve2Values = argData.at(9);
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// buffer store
			this.bufferStore(2, arrCurve1Values);
			this.bufferStore(3, arrCurve2Values);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		arrSlotData = argData.at(10);
	}

	loadSample { arg argIndex; // method to load samples into buffer
		var holdBuffer, holdSampleInd, holdModCondition, holdPath;
		Routine.run {
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			// pause
			system.server.sync;
			// first reset play range
			this.resetPlayRange;
			// adjust index
			holdSampleInd = (argIndex - 1).min(system.sampleFilesMono(bankNo).size-1);
			// check for invalid samples
			if (argIndex == 0 or: {system.sampleFilesMono(bankNo).at(holdSampleInd).at(3) == false}, {
				// if argIndex is 0, clear the current buffer & filename
				buffers.at(0).zero;
				sampleFileName = "";
				sampleNumChannels = 0;
				sampleFreq = 440;
				// store Freq to synthArgSpecs
				this.setSynthArgSpec("sampleFreq", sampleFreq);
			},{
				// otherwise,  try to load sample.  if it fails, display error message and clear
				holdPath = system.sampleFilesMono(bankNo).at(holdSampleInd).at(0);
				// Convert path
				holdPath = TXPath.convert(holdPath);
				holdBuffer = Buffer.read(system.server, holdPath,
					action: { arg argBuffer;
						{
							//	if file loaded ok
							if (argBuffer.notNil, {
								this.setSynthArgSpec("bufnumSample", argBuffer.bufnum);
								sampleFileName = system.sampleFilesMono(bankNo).at(holdSampleInd).at(0);
								sampleNumChannels = argBuffer.numChannels;
								sampleFreq = system.sampleFilesMono(bankNo).at(holdSampleInd).at(1);
								// store Freq to synthArgSpecs
								this.setSynthArgSpec("sampleFreq", sampleFreq);
								this.updateWavetableBuffer(sampleFileName);
							},{
								buffers.at(0).zero;
								sampleFileName = "";
								sampleNumChannels = 0;
								sampleFreq = 440;
								// store Freq to synthArgSpecs
								this.setSynthArgSpec("sampleFreq", sampleFreq);
								TXInfoScreen.new("Invalid Sample File"
									++ system.sampleFilesMono(bankNo).at(holdSampleInd).at(0));
								this.emptyWavetableBuffer;
							});
						}.defer;	// defer because gui process
					},
					// pass buffer number
					bufnum: buffers.at(0).bufnum
				);
			});
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		}; // end of Routine.run
	} // end of method loadSample


	emptyWavetableBuffer{
		buffers.at(1).zero;
	}

	updateWavetableBuffer{ arg sampleFileName;
		buffers.at(0).loadToFloatArray( action: {arg array;
			var holdSignal, arrSize, arrKeep;
			arrSize = array.size.min(classData.maxWavetableSize/8).asInteger;
			arrKeep = array.keep(arrSize);
			holdSignal = Signal.newClear(arrSize);
			holdSignal = holdSignal.addAll(arrKeep).zeroPad;
			buffers.at(1).loadCollection(holdSignal.asWavetable);
		});
	}

	resetPlayRange {
		this.setSynthArgSpec("start", 0);
		this.setSynthArgSpec("end", 1);
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

