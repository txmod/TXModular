// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSamplePlayerPlusSt : TXModuleBase {

	// Note: TXSamplePlayerPlusSt is different to TXSamplePlayerPlus because it is does not have extra loop type "Single-Waveform"

	classvar <>classData;

	var	timeSpec;
	var	arrFreqRangePresets;
	var	arrMMSourceNames, arrMMDestNames, arrMMScaleNames;
	var <>sampleNo = 0;
	var <>bankNo = 0;
	var sampleFileName = "";
	var showWaveform = 0;
	var sampleNumChannels = 0;
	var sampleFreq = 440;
	var displayOption;
	var ratioView;
	var	envView, envView2;
	var <>testMIDINote = 69;
	var <>testMIDIVel = 100;
	var <>testMIDITime = 1;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Sample Player+ St";
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
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.arrBufferSpecs = [ ["bufnumSampleStereo", 2048, 2],  ["bufnumSampleMono", 2048, 1] ];
	} // end of method initClass

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showSample";
		timeSpec = ControlSpec(0.001, 20);
		arrFreqRangePresets = TXFilter.arrFreqRanges;
		arrMMSourceNames = ["Set Source...", "Note", "Velocity", "Amp Env", "Env 2", "LFO 1",
			"LFO 2", "Random val 1", "Random val 2", "Offset val", "Slider 1", "Slider 2"];
		arrMMDestNames = ["Set Dest...", "Sample start", "Sample end", "Pitchbend", "Level",
			"Filter Freq", "Filter Res", "Filter Sat"];
		arrMMScaleNames = ["...", "Note", "Velocity", "Slider 1", "Slider 2"];
		arrSynthArgSpecs = [
			["out", 0, 0],
			["gate", 1, 0],
			["note", 0, 0],
			["velocity", 0, 0],
			["keytrack", 1, \ir],
			["transpose", 0, \ir],
			["pitchbend", 0.5, defLagTime],
			["pitchbendMin", -2, defLagTime],
			["pitchbendMax", 2, defLagTime],
			["bufnumSampleStereo", 0, \ir],
			["bufnumSampleMono", 0, \ir],
			["sampleIsStereo", 1, \ir],
			["bankNo", 0, \ir],
			["sampleNo", 0, \ir],
			["sampleFreq", 440, \ir],
			["start", 0, defLagTime],
			["end", 1, defLagTime],
			["reverse", 0, defLagTime],
			["level", 0.5, defLagTime],
			["envtime", 0, \ir],
			["delay", 0, \ir],
			["attack", 0.001, \ir],
			["attackMin", 0, \ir],
			["attackMax", 5, \ir],
			["decay", 0.05, \ir],
			["decayMin", 0, \ir],
			["decayMax", 5, \ir],
			["sustain", 1, \ir],
			["sustainTime", 0.2, \ir],
			["sustainTimeMin", 0, \ir],
			["sustainTimeMax", 5, \ir],
			["release", 0.01, \ir],
			["releaseMin", 0, \ir],
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
			["velModMin", 0, defLagTime],
			["velModMax", 127, defLagTime],
			["noteModMin", 0, defLagTime],
			["noteModMax", 127, defLagTime],
			["slider1", 0, defLagTime],
			["slider2", 0, defLagTime],

			["i_Source0", 0, \ir],
			["i_Dest0", 0, \ir],
			["mmValue0", 0, defLagTime],
			["i_Scale0", 0, \ir],
			["i_Source1", 0, \ir],
			["i_Dest1", 0, \ir],
			["mmValue1", 0, defLagTime],
			["i_Scale1", 0, \ir],
			["i_Source2", 0, \ir],
			["i_Dest2", 0, \ir],
			["mmValue2", 0, defLagTime],
			["i_Scale2", 0, \ir],
			["i_Source3", 0, \ir],
			["i_Dest3", 0, \ir],
			["mmValue3", 0, defLagTime],
			["i_Scale3", 0, \ir],
			["i_Source4", 0, \ir],
			["i_Dest4", 0, \ir],
			["mmValue4", 0, defLagTime],
			["i_Scale4", 0, \ir],
			["i_Source5", 0, \ir],
			["i_Dest5", 0, \ir],
			["mmValue5", 0, defLagTime],
			["i_Scale5", 0, \ir],
			["i_Source6", 0, \ir],
			["i_Dest6", 0, \ir],
			["mmValue6", 0, defLagTime],
			["i_Scale6", 0, \ir],
			["i_Source7", 0, \ir],
			["i_Dest7", 0, \ir],
			["mmValue7", 0, defLagTime],
			["i_Scale7", 0, \ir],
			["i_Source8", 0, \ir],
			["i_Dest8", 0, \ir],
			["mmValue8", 0, defLagTime],
			["i_Scale8", 0, \ir],
			["i_Source9", 0, \ir],
			["i_Dest9", 0, \ir],
			["mmValue9", 0, defLagTime],
			["i_Scale9", 0, \ir],

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
		];
		// create options
		arrOptions = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
		arrOptionData = [
			[	["Single shot",
				{arg outRate, bufnumSampleStereo, bufnumSampleMono, start, end;
					var output;
					if (this.getSynthArgSpec("sampleIsStereo") == 1, {
						output = BufRd.ar(2, bufnumSampleStereo,
							(Sweep.ar(1, outRate * BufSampleRate.kr(bufnumSampleStereo))
								+ (((start * outRate.sign.max(0)) + (end * outRate.sign.neg.max(0)))
									* BufFrames.kr(bufnumSampleStereo))
							)
							.min(end * BufFrames.kr(bufnumSampleStereo))
							.max(start * BufFrames.kr(bufnumSampleStereo))
							,0
						);
					}, {
						output = BufRd.ar(1, bufnumSampleMono,
							(Sweep.ar(1, outRate * BufSampleRate.kr(bufnumSampleMono))
								+ (((start * outRate.sign.max(0)) + (end * outRate.sign.neg.max(0)))
									* BufFrames.kr(bufnumSampleMono))
							)
							.min(end * BufFrames.kr(bufnumSampleMono))
							.max(start * BufFrames.kr(bufnumSampleMono))
							,0
						).dup;
					});
					output;
				}
			],
			["Looped",
				{arg outRate, bufnumSampleStereo, bufnumSampleMono, start, end;
					var output;
					if (this.getSynthArgSpec("sampleIsStereo") == 1, {
						output = BufRd.ar(2, bufnumSampleStereo,
							Phasor.ar(0, outRate * BufRateScale.kr(bufnumSampleStereo),
								start * BufFrames.kr(bufnumSampleStereo),
								end * BufFrames.kr(bufnumSampleStereo)
							)
						);
					}, {
						output = BufRd.ar(1, bufnumSampleMono,
							Phasor.ar(0, outRate * BufRateScale.kr(bufnumSampleMono),
								start * BufFrames.kr(bufnumSampleMono),
								end * BufFrames.kr(bufnumSampleMono)
							)
						).dup;
					});
					output;
				}
			],
			["X-Fade Looped",
				{arg outRate, bufnumSampleStereo, bufnumSampleMono, start, end;
					var startFrame, endFrame, offset, bufdur, output;
					if (this.getSynthArgSpec("sampleIsStereo") == 1, {
						startFrame = start * BufFrames.kr(bufnumSampleStereo);
						endFrame = end * BufFrames.kr(bufnumSampleStereo);
						offset = (endFrame - startFrame) * 0.5;
						bufdur = abs(end-start) * BufDur.kr(bufnumSampleStereo);
						output = Mix.new(
							BufRd.ar(2, bufnumSampleStereo,
								(Phasor.ar(0, outRate * BufRateScale.kr(bufnumSampleStereo), startFrame, endFrame)
									+ [0, offset]
								).wrap(startFrame, endFrame);
							) * SinOsc.kr(0.5 * outRate * bufdur.reciprocal, [0, pi/2]).abs;
						);
					}, {
						startFrame = start * BufFrames.kr(bufnumSampleMono);
						endFrame = end * BufFrames.kr(bufnumSampleMono);
						offset = (endFrame - startFrame) * 0.5;
						bufdur = abs(end-start) * BufDur.kr(bufnumSampleMono);
						output = Mix.new(
							BufRd.ar(1, bufnumSampleMono,
								(Phasor.ar(0, outRate * BufRateScale.kr(bufnumSampleMono), startFrame, endFrame)
									+ [0, offset]
								).wrap(startFrame, endFrame);
							) * SinOsc.kr(0.5 * outRate * bufdur.reciprocal, [0, pi/2]).abs;
						);
						output = output.dup;
					});
					output;
				}
			]
			],
			// Intonation
			TXIntonation.arrOptionData,
			TXEnvLookup.arrSlopeOptionData,
			TXEnvLookup.arrDADSRSustainOptionData,
			TXEnvLookup.arrSlopeOptionData,
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
		];
		synthDefFunc = {
			arg out, gate, note, velocity, keytrack, transpose, pitchbend, pitchbendMin, pitchbendMax,
			bufnumSampleStereo, bufnumSampleMono, sampleIsStereo,
			bankNo, sampleNo, sampleFreq, start, end, reverse, level,
			envtime=0, delay, attack, attackMin, attackMax, decay, decayMin, decayMax, sustain,
			sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax,
			envtime2=0, delay2, attack2, attackMin2, attackMax2, decay2, decayMin2, decayMax2, sustain2,
			sustainTime2, sustainTimeMin2, sustainTimeMax2, release2, releaseMin2, releaseMax2,
			intKey,
			filterFreq, filterFreqMin, filterFreqMax, filterRes, filterResMin, filterResMax,
			filterSat, filterSatMin, filterSatMax, wetDryMix,
			freqLFO1, freqLFO1Min, freqLFO1Max, lfo1FadeIn, lfo1FadeInMin, lfo1FadeInMax,
			freqLFO2, freqLFO2Min, freqLFO2Max,  lfo2FadeIn, lfo2FadeInMin, lfo2FadeInMax,
			velModMin, velModMax, noteModMin, noteModMax, slider1, slider2,
			i_Source0, i_Dest0, mmValue0, i_Scale0, i_Source1, i_Dest1, mmValue1, i_Scale1,
			i_Source2, i_Dest2, mmValue2, i_Scale2,
			i_Source3, i_Dest3, mmValue3, i_Scale3, i_Source4, i_Dest4, mmValue4, i_Scale4,
			i_Source5, i_Dest5, mmValue5, i_Scale5,
			i_Source6, i_Dest6, mmValue6, i_Scale6, i_Source7, i_Dest7, mmValue7, i_Scale7,
			i_Source8, i_Dest8, mmValue8, i_Scale8, i_Source9, i_Dest9, mmValue9, i_Scale9,
			modStart=0, modEnd=0, modReverse=0, modPitchbend=0, modDelay=0, modAttack=0, modDecay=0,
			modSustain=0, modSustainTime=0, modRelease=0,
			modDelay2=0, modAttack2=0, modDecay2=0, modSustain2=0, modSustainTime2=0, modRelease2=0,
			modFilterFreq=0, modFilterRes=0, modFilterSat=0, modWetDryMix=0,
			modFreqLFO1=0, modFadeInLFO1=0, modFreqLFO2=0, modFadeInLFO2=0, modSlider1=0, modSlider2=0;

			var outEnv, outEnv2, envFunction, envFunction2, envCurve, envCurve2, envGenFunction2,
			outFreq, intonationFunc, pbend, outRate, outFunction, outSample,
			sStart, sEnd, rev, del, att, dec, sus, sustime, rel,
			del2, att2, dec2, sus2, sustime2, rel2,
			filterProcessFunction, filterFunction, outFilter, levelControlFunc,
			lfo1Function, outLFO1, lfo2Function, outLFO2, lfo1FadeInTime, lfo1FadeInCurve,
			lfo2FadeInTime, lfo2FadeInCurve, randomValue1, randomValue2,
			timeControlSpec, sumVelocity, sourceIndexArray, destIndexArray, scaleIndexArray,
			sourceArray, destArray, mmValueArray,
			scaleArray, sumSlider1, sumSlider2,
			dummyVal, mmPbend = 0, mmLevel = 0, mmFilterFreq = 0, mmFilterRes = 0, mmFilterSat = 0,
			mmSampleStart = 0, mmSampleEnd = 0, arrAllDestModulations, noteModulation, velModulation;

			rev = (reverse + modReverse).max(0).min(1);
			del = (delay + modDelay).max(0).min(1);
			att = (attackMin + ((attackMax - attackMin) * (attack + modAttack))).max(0.001).min(20);
			dec = (decayMin + ((decayMax - decayMin) * (decay + modDecay))).max(0.001).min(20);
			sus = (sustain + modSustain).max(0).min(1);
			sustime = (sustainTimeMin +
				((sustainTimeMax - sustainTimeMin) * (sustainTime + modSustainTime))).max(0.001).min(20);
			rel = (releaseMin + ((releaseMax - releaseMin) * (release + modRelease))).max(0.001).min(20);
			envCurve = this.getSynthOption(2);
			envFunction = this.getSynthOption(3);
			outEnv = EnvGen.ar(
				envFunction.value(del, att, dec, sus, sustime, rel, envCurve),
				gate,
				doneAction: 2
			);
			del2 = (delay2 + modDelay2).max(0).min(1);
			att2 = (attackMin2 + ((attackMax2 - attackMin2) * (attack2 + modAttack2))).max(0.001).min(20);
			dec2 = (decayMin2 + ((decayMax2 - decayMin2) * (decay2 + modDecay2))).max(0.001).min(20);
			sus2 = (sustain2 + modSustain2).max(0).min(1);
			sustime2 = (sustainTimeMin2 +
				((sustainTimeMax2 - sustainTimeMin2) * (sustainTime2 + modSustainTime2))).max(0.001).min(20);
			rel2 = (releaseMin2 + ((releaseMax2 - releaseMin2) * (release2 + modRelease2))).max(0.001).min(20);
			envCurve2 = this.getSynthOption(4);
			envFunction2 = this.getSynthOption(5);
			envGenFunction2 = this.getSynthOption(14);
			outEnv2 = envGenFunction2.value(envFunction2, del2, att2, dec2, sus2, sustime2, rel2, envCurve2, gate);

			lfo1FadeInTime = (lfo1FadeInMin +
				((lfo1FadeInMax - lfo1FadeInMin) * (lfo1FadeIn + modFadeInLFO1))).max(0.001).min(20);
			lfo1FadeInCurve = XLine.kr(0.01, 1, lfo1FadeInTime);
			lfo1Function =  this.getSynthOption(8);
			outLFO1 = lfo1FadeInCurve *
			lfo1Function.value(this.getSynthOption(9), this.getSynthOption(10),
				freqLFO1, freqLFO1Min, freqLFO1Max, modFreqLFO1);

			lfo2FadeInTime = (lfo2FadeInMin +
				((lfo2FadeInMax - lfo2FadeInMin) * (lfo2FadeIn + modFadeInLFO2))).max(0.001).min(20);
			lfo2FadeInCurve = XLine.kr(0.01, 1, lfo2FadeInTime);
			lfo2Function =  this.getSynthOption(11);
			outLFO2 = lfo2FadeInCurve *
			lfo2Function.value(this.getSynthOption(12), this.getSynthOption(13),
				freqLFO2, freqLFO2Min, freqLFO2Max, modFreqLFO2);

			sumSlider1 = (slider1 + modSlider1).max(0).min(1);
			sumSlider2 = (slider2 + modSlider2).max(0).min(1);

			randomValue1 = Rand(0, 1);
			randomValue2 = Rand(0, 1);

			noteModulation = note.max(noteModMin).min(noteModMax) -  noteModMin / (noteModMax - noteModMin);
			velModulation = velocity.max(velModMin).min(velModMax) - velModMin / (velModMax - velModMin);

			sourceArray = [0, noteModulation, velModulation, outEnv, outEnv2, outLFO1, outLFO2,
				randomValue1, randomValue2, 1, sumSlider1, sumSlider2];
			destArray = [dummyVal, mmSampleStart, mmSampleEnd, mmPbend, mmLevel, mmFilterFreq, mmFilterRes, mmFilterSat];
			scaleArray = [1, noteModulation, velModulation, sumSlider1, sumSlider2];
			sourceIndexArray = ["i_Source0", "i_Source1", "i_Source2", "i_Source3", "i_Source4", "i_Source5",
				"i_Source6", "i_Source7", "i_Source8", "i_Source9"]
			.collect({arg item, i; this.getSynthArgSpec(item);});
			destIndexArray = ["i_Dest0", "i_Dest1", "i_Dest2", "i_Dest3", "i_Dest4", "i_Dest5", "i_Dest6", "i_Dest7",
				"i_Dest8", "i_Dest9"]
			.collect({arg item, i; this.getSynthArgSpec(item);});
			mmValueArray = [mmValue0, mmValue1, mmValue2, mmValue3, mmValue4, mmValue5, mmValue6, mmValue7,
				mmValue8, mmValue9];
			scaleIndexArray = ["i_Scale0", "i_Scale1", "i_Scale2", "i_Scale3", "i_Scale4", "i_Scale5",
				"i_Scale6", "i_Scale7", "i_Scale8", "i_Scale9"]
			.collect({arg item, i; this.getSynthArgSpec(item);});

			// build mod matrix modulations
			arrAllDestModulations = destArray.collect ({arg item, i;
				var arrModulations;
				arrModulations = [];
				sourceIndexArray.do({arg itemSourceInd, j;
					if (destIndexArray[j] == i, {
						arrModulations = arrModulations.add(sourceArray[itemSourceInd] * (mmValueArray[j] / 100)
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
			outFreq = (intonationFunc.value((note + transpose), intKey) * keytrack) + ((sampleFreq.cpsmidi
				+ transpose).midicps * (1-keytrack));
			pbend = pitchbendMin + ((pitchbendMax - pitchbendMin)
				* (pitchbend + modPitchbend + mmPbend).max(0).min(1));
			outRate = ((outFreq *  (2 ** (pbend /12))) / sampleFreq) * (rev-0.5).neg.sign;
			outFunction = this.getSynthOption(0);
			outSample = outFunction.value(outRate, bufnumSampleStereo, bufnumSampleMono, sStart, sEnd) * level * 2;
			filterProcessFunction =  this.getSynthOption(6);
			filterFunction =  this.getSynthOption(7);
			outFilter = filterFunction.value(outSample, filterProcessFunction, filterFreq, filterFreqMin, filterFreqMax,
				filterRes, filterResMin, filterResMax, filterSat, filterSatMin, filterSatMax, wetDryMix,
				modFilterFreq + mmFilterFreq, modFilterRes + mmFilterRes, modFilterSat + mmFilterSat, modWetDryMix);
			levelControlFunc = this.getSynthOption(15);
			sumVelocity = ((velocity * 0.007874) + mmLevel).max(0).min(1);
			// use TXClean to stop blowups
			Out.ar(out, TXClean.ar(outEnv * levelControlFunc.value(outFilter, outSample) * sumVelocity));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TestNoteVals"],
			["commandAction", "Plot envelope", {this.envPlot;}],
			["TXPopupActionPlusMinus", "Sample bank", {system.arrSampleBankNames},
				"bankNo",
				{ arg view; this.bankNo = view.value; this.sampleNo = 0; this.loadSample(0);
					this.setSynthArgSpec("sampleNo", 0); system.showView;}
			],
			// array of sample filenames - beginning with blank sample  - mono & stereo files
			["TXListViewActionPlusMinus", "Sample", {["No Sample"]++system.sampleFileNames(bankNo, true)},
				"sampleNo", { arg view; this.sampleNo = view.value; this.loadSample(view.value); }
			],
			["TXRangeSlider", "Play Range", ControlSpec(0, 1), "start", "end"],
			["SynthOptionPopupPlusMinus", "Loop type", arrOptionData, 0, 260],
			["TXCheckBox", "Reverse", "reverse"],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
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
			["TXEnvDisplay", {this.envViewValues;}, {arg view; envView = view;}],
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
			["SynthOptionPopup", "Curve", arrOptionData, 2, 200, {system.showView;}],
			["SynthOptionPopup", "Env. Type", arrOptionData, 3, 200],
			["commandAction", "Plot amp envelope", {this.envPlot;}],

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
			["SynthOptionPopup", "Curve 2", arrOptionData, 4, 180, {system.showView;}],
			["SynthOptionPopup", "Env. Type 2", arrOptionData, 5, 180],
			["commandAction", "Plot envelope 2", {this.envPlot2;}],

			["SynthOptionCheckBox", "Filter", arrOptionData, 7, 250],
			["SynthOptionPopupPlusMinus", "Type", arrOptionData, 6],
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
			["NextLine"],
			["TXMinMaxSliderSplit", "LFO 2 Freq", ControlSpec(0.01, 100, \exp, 0, 1, units: " Hz"),
				"freqLFO2", "freqLFO2Min", "freqLFO2Max", nil, TXLFO.arrLFOFreqRanges],
			["SynthOptionPopupPlusMinus", "LFO 2 Waveform", arrOptionData, 12],
			["SynthOptionPopupPlusMinus", "LFO 2 Output range", arrOptionData, 13],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.midiNoteInit;
		//	make buffers, load the synthdef and create the Group for synths to belong to
		this.makeBuffersAndGroup(classData.arrBufferSpecs);
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
				// array of sample filenames - beginning with blank sample - show mono and stereo files
				["TXListViewActionPlusMinus", "Sample", {["No Sample"]++system.sampleFileNames(bankNo, true)},
					"sampleNo", { arg view;
						this.sampleNo = view.value;
						this.loadSample(view.value);
						{system.showView;}.defer(0.2);   //  refresh view
					}
				],
				["SpacerLine", 4],
				["Spacer", 80],
				["ActionButton", "Add Samples to Sample Bank", {TXBankBuilder2.addSampleDialog("Sample", bankNo)}, 200],
				["ActionButton", "Show", {showWaveform = 1; system.showView; {showWaveform = 0}.defer(0.5);},
					80, TXColor.white, TXColor.sysGuiCol2],
				["ActionButton", "Hide", {showWaveform = 0; system.showView;},
					80, TXColor.white, TXColor.sysDeleteCol],
				["NextLine"],
				["TXSoundFileViewRange", {sampleFileName}, "start", "end", nil, {showWaveform}],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Loop type", arrOptionData, 0, 260],
				["SpacerLine", 4],
				["TXCheckBox", "Reverse", "reverse"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0, 1), "level"],
			];
		});
		if (displayOption == "showEnv", {
			guiSpecArray = guiSpecArray ++[
				["TXPresetPopup", "Env presets",
					TXEnvPresets.arrEnvPresets(this, 2, 3).collect({arg item, i; item.at(0)}),
					TXEnvPresets.arrEnvPresets(this, 2, 3).collect({arg item, i; item.at(1)})
				],
				["TXEnvDisplay", {this.envViewValues;}, {arg view; envView = view;}],
				["NextLine"],
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
				["NextLine"],
				["SynthOptionPopup", "Curve", arrOptionData, 2, 200, {system.showView;}],
				["NextLine"],
				["SynthOptionPopup", "Env. Type", arrOptionData, 3, 200],
				["Spacer", 4],
				["ActionButton", "Plot", {this.envPlot;}],
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
					5, 60, nil, 36, {arg note; this.releaseSynthGate(note);}],
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
				["TXEnvDisplay", {this.envViewValues2;}, {arg view; envView2 = view;}],
				["NextLine"],
				["EZslider", "Pre-Delay", ControlSpec(0,1), "delay2", {{this.updateEnvView2;}.defer;}],
				["TXMinMaxSliderSplit", "Attack", timeSpec, "attack2", "attackMin2", "attackMax2",{{this.updateEnvView2;}.defer;}],
				["TXMinMaxSliderSplit", "Decay", timeSpec, "decay2", "decayMin2", "decayMax2",{{this.updateEnvView2;}.defer;}],
				["EZslider", "Sustain level", ControlSpec(0, 1), "sustain2", {{this.updateEnvView2;}.defer;}],
				["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime2", "sustainTimeMin2",
					"sustainTimeMax2",{{this.updateEnvView2;}.defer;}],
				["TXMinMaxSliderSplit", "Release", timeSpec, "release2", "releaseMin2", "releaseMax2",{{this.updateEnvView2;}.defer;}],
				["NextLine"],
				["SynthOptionPopup", "Curve", arrOptionData, 4, 180, {system.showView;}],
				["SynthOptionPopup", "Env. Type", arrOptionData, 5, 180],
				["ActionButton", "Plot", {this.envPlot2;}],
			];
		});
		if (displayOption == "showFilter", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionCheckBox", "Filter", arrOptionData, 7, 250],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Type", arrOptionData, 6],
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
				["SynthOptionCheckBox", "LFO 1", arrOptionData, 8, 250],
				["NextLine"],
				["TXMinMaxSliderSplit", "Fade-in time", ControlSpec(0.01, 20, \exp),
					"lfo1FadeIn", "lfo1FadeInMin", "lfo1FadeInMax"],
				["TXMinMaxSliderSplit", "Frequency", ControlSpec(0.01, 100, \exp, 0, 1, units: " Hz"),
					"freqLFO1", "freqLFO1Min", "freqLFO1Max", nil, TXLFO.arrLFOFreqRanges],
				["SynthOptionPopupPlusMinus", "Waveform", arrOptionData, 9],
				["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 10],
				["SpacerLine", 4],
				["DividingLine"],
				["SpacerLine", 4],
				["SynthOptionCheckBox", "LFO 2", arrOptionData, 11, 250],
				["NextLine"],
				["TXMinMaxSliderSplit", "Fade-in time", ControlSpec(0.01, 20, \exp),
					"lfo2FadeIn", "lfo2FadeInMin", "lfo2FadeInMax"],
				["TXMinMaxSliderSplit", "Frequency", ControlSpec(0.01, 100, \exp, 0, 1, units: " Hz"),
					"freqLFO2", "freqLFO2Min", "freqLFO2Max", nil, TXLFO.arrLFOFreqRanges],
				["SynthOptionPopupPlusMinus", "Waveform", arrOptionData, 12],
				["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 13],
			];
		});
		if (displayOption == "showModMatrix", {
			guiSpecArray = guiSpecArray ++[
				["NoteRangeSelector", "Note range", "noteModMin", "noteModMax"],
				["TXRangeSlider", "Vel range", ControlSpec(0, 127, step: 1), "velModMin", "velModMax"],
				["DividingLine"],
				["SpacerLine", 2],
				["EZslider", "Slider 1", ControlSpec(0,1), "slider1"],
				["EZslider", "Slider 2", ControlSpec(0,1), "slider2"],
				["DividingLine"],
				["SpacerLine", 2],
				["TextBarLeft", "  Source", 100],
				["TextBarLeft", "  Destination", 100],
				["TextBarLeft", "  Modulation amount", 154],
				["TextBarLeft", "  Scale", 80],
				["SpacerLine", 4],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames, "i_Source0", "i_Dest0", "mmValue0", arrMMScaleNames, "i_Scale0"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames, "i_Source1", "i_Dest1", "mmValue1", arrMMScaleNames, "i_Scale1"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames, "i_Source2", "i_Dest2", "mmValue2", arrMMScaleNames, "i_Scale2"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames, "i_Source3", "i_Dest3", "mmValue3", arrMMScaleNames, "i_Scale3"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames, "i_Source4", "i_Dest4", "mmValue4", arrMMScaleNames, "i_Scale4"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames, "i_Source5", "i_Dest5", "mmValue5", arrMMScaleNames, "i_Scale5"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames, "i_Source6", "i_Dest6", "mmValue6", arrMMScaleNames, "i_Scale6"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames, "i_Source7", "i_Dest7", "mmValue7", arrMMScaleNames, "i_Scale7"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames, "i_Source8", "i_Dest8", "mmValue8", arrMMScaleNames, "i_Scale8"],
				["ModMatrixRowScale", arrMMSourceNames, arrMMDestNames, "i_Source9", "i_Dest9", "mmValue9", arrMMScaleNames, "i_Scale9"],
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
	extraSaveData { // override default method
		^[sampleNo, sampleFileName, sampleNumChannels, sampleFreq, testMIDINote, testMIDIVel, testMIDITime, bankNo];
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
	}

	loadSample { arg argIndex; // method to load samples into buffer
		var holdBuffer, holdBufferNum, holdBufferString, holdSampleInd, holdModCondition, holdPath;
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
			holdSampleInd = (argIndex - 1).min(system.sampleFiles(bankNo).size-1);
			// check for invalid samples
			if (argIndex == 0 or: {system.sampleFiles(bankNo).at(holdSampleInd).at(3) == false}, {
				// if argIndex is 0, clear the current buffer & filename
				buffers.at(0).zero;
				buffers.at(1).zero;
				sampleFileName = "";
				sampleNumChannels = 0;
				sampleFreq = 440;
				// store Freq to synthArgSpecs
				this.setSynthArgSpec("sampleFreq", sampleFreq);
				this.setSynthArgSpec("sampleIsStereo", 1);
				//	rebuild synth to update stereo/mono
				this.rebuildSynth;
			},{
				// otherwise,  try to load sample.  if it fails, display error message and clear
				// check for stereo
				if (system.sampleFiles(bankNo).at(holdSampleInd).at(2) == 2, {
					holdBufferNum = buffers.at(0).bufnum;
					holdBufferString = "bufnumSampleStereo";
					this.setSynthArgSpec("sampleIsStereo", 1);
				},{
					holdBufferNum = buffers.at(1).bufnum;
					holdBufferString = "bufnumSampleMono";
					this.setSynthArgSpec("sampleIsStereo", 0);
				});
				holdPath = system.sampleFiles(bankNo).at(holdSampleInd).at(0);
				// Convert path
				holdPath = TXPath.convert(holdPath);
				holdBuffer = Buffer.read(system.server, holdPath,
					action: { arg argBuffer;
						{
							//	if file loaded ok
							if (argBuffer.notNil, {
								this.setSynthArgSpec(holdBufferString, argBuffer.bufnum);
								sampleFileName = system.sampleFiles(bankNo).at(holdSampleInd).at(0);
								sampleNumChannels = argBuffer.numChannels;
								sampleFreq = system.sampleFiles(bankNo).at(holdSampleInd).at(1);
								// store Freq to synthArgSpecs
								this.setSynthArgSpec("sampleFreq", sampleFreq);
							},{
								buffers.at(0).zero;
								buffers.at(1).zero;
								sampleFileName = "";
								sampleNumChannels = 0;
								sampleFreq = 440;
								// store Freq to synthArgSpecs
								this.setSynthArgSpec("sampleFreq", sampleFreq);
								TXInfoScreen.new("Invalid Sample File"
									++ system.sampleFiles(bankNo).at(holdSampleInd).at(0));
							});
							//	rebuild synth to update stereo/mono
							this.rebuildSynth;
						}.defer;	// defer because gui process
					},
					// pass buffer number
					bufnum: holdBufferNum
				);
			});
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		}; // end of Routine.run
	} // end of method loadSample

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

	envPlot {
		var del, att, dec, sus, sustime, rel, envCurve;
		del = this.getSynthArgSpec("delay");
		att = this.getSynthArgSpec("attack");
		dec = this.getSynthArgSpec("decay");
		sus = this.getSynthArgSpec("sustain");
		sustime = this.getSynthArgSpec("sustainTime");
		rel = this.getSynthArgSpec("release");
		envCurve = this.getSynthOption(2);
		Env.new([0, 0, 1, sus, sus, 0], [del, att, dec, sustime, rel], envCurve, nil).plot;
	}

	envPlot2 {
		var del, att, dec, sus, sustime, rel, envCurve;
		del = this.getSynthArgSpec("delay2");
		att = this.getSynthArgSpec("attack2");
		dec = this.getSynthArgSpec("decay2");
		sus = this.getSynthArgSpec("sustain2");
		sustime = this.getSynthArgSpec("sustainTime2");
		rel = this.getSynthArgSpec("release2");
		envCurve = this.getSynthOption(4);
		Env.new([0, 0, 1, sus, sus, 0], [del, att, dec, sustime, rel], envCurve, nil).plot;
	}

	envViewValues {
		var attack, attackMin, attackMax, decay, decayMin, decayMax, sustain;
		var sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax;
		var del, att, dec, sus, sustime, rel;
		var arrTimesNorm, arrTimesNormedSummed;

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

		arrTimesNorm = [0, del, att, dec, sustime, rel].normalizeSum;
		arrTimesNorm.size.do({ arg i;
			arrTimesNormedSummed = arrTimesNormedSummed.add(arrTimesNorm.copyRange(0, i).sum);
		});
		^[arrTimesNormedSummed, [0, 0, 1, sus, sus, 0]].asFloat;
	}

	envViewValues2 {
		var attack, attackMin, attackMax, decay, decayMin, decayMax, sustain;
		var sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax;
		var del, att, dec, sus, sustime, rel;
		var arrTimesNorm, arrTimesNormedSummed;

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

		arrTimesNorm = [0, del, att, dec, sustime, rel].normalizeSum;
		arrTimesNorm.size.do({ arg i;
			arrTimesNormedSummed = arrTimesNormedSummed.add(arrTimesNorm.copyRange(0, i).sum);
		});
		^[arrTimesNormedSummed, [0, 0, 1, sus, sus, 0]].asFloat;
	}

	updateEnvView {
		if (envView.respondsTo('notClosed'), {
			if (envView.notClosed, {
				6.do({arg i;
					envView.setEditable(i, true);
				});
				envView.value = this.envViewValues;
				6.do({arg i;
					envView.setEditable(i, false);
				});
			});
		});
	}

	updateEnvView2 {
		if (envView2.respondsTo('notClosed'), {
			if (envView2.notClosed, {
				6.do({arg i;
					envView2.setEditable(i, true);
				});
				envView2.value = this.envViewValues2;
				6.do({arg i;
					envView2.setEditable(i, false);
				});
			});
		});
	}

}

