// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXTableSynth4 : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var ratioView;
	var	envView, envView2;
	var arrWaveSpecs;
	var arrSlotData;
	var arrSignals;
	var dataEvent;
	var <>testMIDINote = 69;
	var <>testMIDIVel = 100;
	var <>testMIDITime = 1;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Table Synth";
		classData.moduleRate = "audio";
		classData.moduleType = "groupsource";
		classData.arrAudSCInBusSpecs = [
			["FM input", 1, "inmodulator"],
			["Ext Sync input", 1, "inExtSync"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Table position", 1, "modTablePosition", 0],
			["Pitch bend", 1, "modPitchbend", 0],
			["Vol env delay", 1, "modDelay", 0],
			["Vol env attack", 1, "modAttack", 0],
			["Vol env decay", 1, "modDecay", 0],
			["Vol env sustain level", 1, "modSustain", 0],
			["Vol env sustain time", 1, "modSustainTime", 0],
			["Vol env release", 1, "modRelease", 0],
			["Table env amount", 1, "modEnv2Amount", 0],
			["Table env delay", 1, "modDelay2", 0],
			["Table env attack", 1, "modAttack2", 0],
			["Table env decay", 1, "modDecay2", 0],
			["Table env sustain level", 1, "modSustain2", 0],
			["Table env sustain time", 1, "modSustainTime2", 0],
			["Table env release", 1, "modRelease2", 0],
			["Sync ratio", 1, "modSyncRatio", 0],
			["Sync threshold", 1, "modSyncThreshold", 0],
			["Phase distort", 1, "modPhaseDistort", 0],
			["FM depth", 1, "modFMDepth", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrBufferSpecs = [ ["bufnumFirstTable", 1024, 1, 8] ]; // allocate 8 consecutive mono buffers
		classData.timeSpec = ControlSpec(0.01, 20);
		classData.pitchbendSpec = ControlSpec(-48, 48);
		classData.syncRatioSpec = ControlSpec(0.01, 20);
		classData.phaseDistortSpec = ControlSpec(-5, 5);
		classData.fmDepthSpec = ControlSpec(0, 8);
		classData.guiWidth = 670;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showWavetables";
		arrSynthArgSpecs = [
			["inmodulator", 0, 0],
			["inExtSync", 0, 0],
			["out", 0, 0],
			["gate", 1, 0],
			["note", 0, 0],
			["velocity", 0, 0],
			["keytrack", 1, \ir],
			["transpose", 0, \ir],
			["maxHarmonicsInd", 1, \ir],
			["harmonicGap", 1, \ir],
			["scaling", 1, \ir],
			["pitchbend", 0.5, defLagTime],
			["pitchbendMin", -2, defLagTime],
			["pitchbendMax", 2, defLagTime],
			["syncRatio", ControlSpec(0.25, 4).unmap(2), defLagTime],
			["syncRatioMin", 0.25, defLagTime],
			["syncRatioMax", 4, defLagTime],
			["syncThreshold", 0, defLagTime],
			["phaseDistort", 0.5, defLagTime],
			["phaseDistortMin", -10, defLagTime],
			["phaseDistortMax", 10, defLagTime],
			["fmDepth", ControlSpec(0, 8).unmap(1), defLagTime],
			["fmDepthMin", 0, defLagTime],
			["fmDepthMax", 8, defLagTime],
			["bufnumFirstTable", 0, \ir],
			["tablePosition", 0, defLagTime],
			["tablePositionMin", 1, defLagTime],
			["tablePositionMax", 8, defLagTime],
			["level", 0.5, defLagTime],
			["envtime", 0, \ir],
			["delay", 0, \ir],
			["attack", 0.01, \ir],
			["attackMin", 0, \ir],
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
			["env2Amount", 1, \ir],
			["envtime2", 0, \ir],
			["delay2", 0, \ir],
			["attack2", 0.005, \ir],
			["attackMin2", 0, \ir],
			["attackMax2", 5, \ir],
			["decay2", 0.05, \ir],
			["decayMin2", 0, \ir],
			["decayMax2", 5, \ir],
			["sustain2", 1, \ir],
			["sustainTime2", 0.2, \ir],
			["sustainTimeMin2", 0, \ir],
			["sustainTimeMax2", 5, \ir],
			["release2", 0.01, \ir],
			["releaseMin2", 0, \ir],
			["releaseMax2", 5, \ir],
			["intKey", 0, \ir],
			["modPitchbend", 0, defLagTime],
			["modTablePosition", 0, defLagTime],
			["modDelay", 0, \ir],
			["modAttack", 0, \ir],
			["modDecay", 0, \ir],
			["modSustain", 0, \ir],
			["modSustainTime", 0, \ir],
			["modRelease", 0, \ir],
			["modEnv2Amount", 0, \ir],
			["modDelay2", 0, \ir],
			["modAttack2", 0, \ir],
			["modDecay2", 0, \ir],
			["modSustain2", 0, \ir],
			["modSustainTime2", 0, \ir],
			["modRelease2", 0, \ir],
			["modSyncRatio", 0, defLagTime],
			["modSyncThreshold", 0, defLagTime],
			["modPhaseDistort", 0, defLagTime],
			["modFMDepth", 0, defLagTime],
		];
		arrOptions = [0,0,0,0,0,0,0,0,0,0,0,0,0];
		arrOptionData = [
			// ind0
			[
				["None", {arg val; val;}],
				["Step size 0.2", {arg val; Lag.kr(val.round(0.2), 0.03);}],
				["Step size 0.25", {arg val; Lag.kr(val.round(0.25), 0.03);}],
				["Step size 0.333", {arg val; Lag.kr(val.round(0.333333333), 0.03);}],
				["Step size 0.5", {arg val; Lag.kr(val.round(0.5), 0.03);}],
				["Step size 1", {arg val; Lag.kr(val.round(1), 0.03);}],
			],
			// ind1
			TXIntonation.arrOptionData,
			// ind2
			TXEnvLookup.arrDADSRSlopeOptionData,
			// ind3
			TXEnvLookup.arrDADSRSustainOptionData,
			// ind4 Frequency Modulation
			[
				["No Frequency Modulation",
					{arg inmodulator;
						0;
					}
				],
				["Frequency Modulation using FM input",
					{arg inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth;
						var fmDepthSum;
						fmDepthSum = fmDepthMin + ((fmDepthMax - fmDepthMin) * (fmDepth + modFMDepth).max(0).min(1));
						fmDepthSum * InFeedback.ar(inmodulator,1);
					}
				],
			],
			// ind5 table env
			TXEnvLookup.arrDADSRSlopeOptionData,
			// ind6 table env
			TXEnvLookup.arrDADSRSustainOptionData,
			// ind7
			[	["Off", { 0; }],
				["On", {arg envFunction, del, att, dec, sus, sustime, rel, envCurve, gate;
					EnvGen.kr(
						envFunction.value(del, att, dec, sus, sustime, rel, envCurve),
						gate,
						doneAction: 0
					);
				}],
			],
			 // ind8 amp compensation
			TXAmpComp.arrOptionData,
			// ind9 - sync triggering
			[
				["Internal - use [Sync ratio x MIDI note] as sync frequency",
					{arg inExtSync, syncRatio, syncRatioMin, syncRatioMax, modSyncRatio, outFreq;
						var syncRatioSum;
						syncRatioSum = syncRatioMin + ((syncRatioMax - syncRatioMin) * (syncRatio + modSyncRatio).max(0).min(1));
						Impulse.ar(syncRatioSum * outFreq);
					}
				],
				["External Sync audio input on upwards slope",
					{arg inExtSync;
						Trig1.ar(InFeedback.ar(inExtSync, 1), SampleDur.ir);
					}
				],
				["External Sync audio input on downwards slope",
					{arg inExtSync;
						Trig1.ar(InFeedback.ar(inExtSync, 1).neg, SampleDur.ir);
					}
				],
				["External Sync audio input on upwards or downwards slope",
					{arg inExtSync;
						var input = InFeedback.ar(inExtSync, 1);
						Trig1.ar(input, SampleDur.ir) + Trig1.ar(input.neg, SampleDur.ir);
					}
				],
			],
			// ind10 - sync mode - returns synced Phasor
			[
				["Sync Off",
					{arg syncTrig, outFreq;
						//  Phasor
						Phasor.ar(0, outFreq / SampleRate.ir, 0, 1);
					}
				],
				["Reverse phase on cycle start & on sync trigger",
					{arg syncTrig, outFreq, syncThreshold, modSyncThreshold, currentPhase;
						var outDirection, sumThreshold, holdTrig;
						// use threshold for soft sync
						sumThreshold = (syncThreshold + modSyncThreshold).max(0).min(1);
						holdTrig = syncTrig * (currentPhase > sumThreshold);
						outDirection = ToggleFF.ar(Impulse.ar(outFreq) + holdTrig).madd(-2, 1); // off/on = +1/-1
						//  Sweep
						Sweep.ar(0, outDirection * outFreq).wrap(0, 1);
					}
				],
				["Reset phase on cycle start, reverse phase on sync trigger",
					{arg syncTrig, outFreq, syncThreshold, modSyncThreshold, currentPhase;
						var outDirection, sumThreshold, holdTrig;
						// use threshold for soft sync
						sumThreshold = (syncThreshold + modSyncThreshold).max(0).min(1);
						holdTrig = syncTrig * (currentPhase > sumThreshold);
						outDirection = ToggleFF.ar(holdTrig).madd(-2, 1); // off/on = +1/-1
						//  Sweep
						Sweep.ar(Impulse.ar(outFreq), outDirection * outFreq).wrap(0, 1);
					}
				],
				["Reset phase on cycle start & on sync trigger",
					{arg syncTrig, outFreq, syncThreshold, modSyncThreshold, currentPhase;
						var sumThreshold, holdTrig;
						// use threshold for soft sync
						sumThreshold = (syncThreshold + modSyncThreshold).max(0).min(1);
						holdTrig = syncTrig * (currentPhase > sumThreshold);
						//  Phasor
						Phasor.ar(Impulse.ar(outFreq) + holdTrig, outFreq / SampleRate.ir, 0, 1);
					}
				],
			],
			// ind11 - smoothing
			[
				["Smoothing Off (default)", {arg sig; sig;}],
				["Median filter - 3 samples", {arg sig; Median.ar(3, sig);}],
				["Median filter - 5 samples", {arg sig; Median.ar(5, sig);}],
				["Median filter - 7 samples", {arg sig; Median.ar(7, sig);}],
				["Median filter - 9 samples", {arg sig; Median.ar(9, sig);}],
				["Median filter - 11 samples", {arg sig; Median.ar(11, sig);}],
				["Median filter - 13 samples", {arg sig; Median.ar(13, sig);}],
				["Median filter - 15 samples", {arg sig; Median.ar(15, sig);}],
				["Median filter - 17 samples", {arg sig; Median.ar(17, sig);}],
				["Median filter - 19 samples", {arg sig; Median.ar(19, sig);}],
				["Median filter - 21 samples", {arg sig; Median.ar(21, sig);}],
				["Median filter - 23 samples", {arg sig; Median.ar(23, sig);}],
				["Median filter - 25 samples", {arg sig; Median.ar(25, sig);}],
				["Median filter - 27 samples", {arg sig; Median.ar(27, sig);}],
				["Median filter - 29 samples", {arg sig; Median.ar(29, sig);}],
				["Median filter - 31 samples", {arg sig; Median.ar(31, sig);}],
				["Smoothing filter - 3 samples", {arg sig;
					var slope = SampleRate.ir * 3.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 5 samples", {arg sig;
					var slope = SampleRate.ir * 5.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 7 samples", {arg sig;
					var slope = SampleRate.ir * 7.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 9 samples", {arg sig;
					var slope = SampleRate.ir * 9.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 11 samples", {arg sig;
					var slope = SampleRate.ir * 11.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 13 samples", {arg sig;
					var slope = SampleRate.ir * 13.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 15 samples", {arg sig;
					var slope = SampleRate.ir * 15.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 17 samples", {arg sig;
					var slope = SampleRate.ir * 17.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 19 samples", {arg sig;
					var slope = SampleRate.ir * 19.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 21 samples", {arg sig;
					var slope = SampleRate.ir * 21.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 23 samples", {arg sig;
					var slope = SampleRate.ir * 23.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 25 samples", {arg sig;
					var slope = SampleRate.ir * 25.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 27 samples", {arg sig;
					var slope = SampleRate.ir * 27.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 29 samples", {arg sig;
					var slope = SampleRate.ir * 29.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
				["Smoothing filter - 31 samples", {arg sig;
					var slope = SampleRate.ir * 31.reciprocal;
					Slew.ar(sig, slope, slope);
				}],
			],
			// ind12 Phase distort
			[	["Off", {arg sig; sig;}],
				["On", {arg sig, phaseDistort, phaseDistortMin, phaseDistortMax, modPhaseDistort;
					var phaseDistortSum;
					phaseDistortSum = phaseDistortMin
					+ ((phaseDistortMax - phaseDistortMin) * (phaseDistort + modPhaseDistort).max(0).min(1));
					sig ** (2 ** phaseDistortSum);
				}],
			],
		];
		synthDefFunc = {
			arg inmodulator, inExtSync, out, gate, note, velocity, keytrack, transpose,
			maxHarmonicsInd, harmonicGap, scaling, pitchbend, pitchbendMin, pitchbendMax,
			syncRatio, syncRatioMin, syncRatioMax, syncThreshold,
			phaseDistort, phaseDistortMin, phaseDistortMax, fmDepth, fmDepthMin, fmDepthMax,
			bufnumFirstTable, tablePosition, tablePositionMin, tablePositionMax, level,
			envtime=0, delay, attack, attackMin, attackMax, decay, decayMin, decayMax,
			sustain, sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax,
			env2Amount, envtime2=0, delay2, attack2, attackMin2, attackMax2, decay2, decayMin2, decayMax2,
			sustain2, sustainTime2, sustainTimeMin2, sustainTimeMax2, release2, releaseMin2, releaseMax2,
			intKey,
			modPitchbend=0, modTablePosition=0,
			modDelay=0, modAttack=0, modDecay=0, modSustain=0, modSustainTime=0, modRelease=0,
			modEnv2Amount=0, modDelay2=0, modAttack2=0, modDecay2=0, modSustain2=0, modSustainTime2=0, modRelease2=0,
			modSyncRatio=0, modSyncThreshold=0, modPhaseDistort=0, modFMDepth=0;

			var	outEnv, outEnv2, envFunction, envFunction2, envCurve, envCurve2, envGenFunction2,
			del, att, dec, sus, sustime, rel, del2, att2, dec2, sus2, sustime2, rel2, holdEnv2Amount,
			stepFunc, intonationFunc, freqModFunc, outFreq, pbend, outWave, tablePos, ampCompFunction, outAmpComp,
			currentPhase, outPhasor, syncTrig, syncTrigFunc, syncedPhasorFunc, smoothingFunc, outSmoothed,
			phaseDistortFunc, outDistortedPhase;

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

			holdEnv2Amount = (env2Amount + modEnv2Amount).max(-1).min(1);
			del2 = (delay2 + modDelay2).max(0).min(1);
			att2 = (attackMin2 + ((attackMax2 - attackMin2) * (attack2 + modAttack2))).max(0.01).min(20);
			dec2 = (decayMin2 + ((decayMax2 - decayMin2) * (decay2 + modDecay2))).max(0.01).min(20);
			sus2 = (sustain2 + modSustain2).max(0).min(1);
			sustime2 = (sustainTimeMin2 +
				((sustainTimeMax2 - sustainTimeMin2) * (sustainTime2 + modSustainTime2))).max(0.01).min(20);
			rel2 = (releaseMin2 + ((releaseMax2 - releaseMin2) * (release2 + modRelease2))).max(0.01).min(20);
			envCurve2 = this.getSynthOption(5);
			envFunction2 = this.getSynthOption(6);
			envGenFunction2 = this.getSynthOption(7);
			outEnv2 = holdEnv2Amount * envGenFunction2.value(
				envFunction2, del2, att2, dec2, sus2, sustime2, rel2, envCurve2, gate);

			tablePos = tablePositionMin + ((tablePositionMax - tablePositionMin)
				* (tablePosition + modTablePosition + outEnv2).max(0).min(1));
			pbend = pitchbendMin + ((pitchbendMax - pitchbendMin) * (pitchbend + modPitchbend).max(0).min(1));

			stepFunc = this.getSynthOption(0);
			intonationFunc = this.getSynthOption(1);
			freqModFunc = this.getSynthOption(4);
			outFreq = (intonationFunc.value((note + transpose), intKey) * keytrack)
			+ ((48 + transpose).midicps * (1-keytrack));
			outFreq = outFreq * (2 ** (pbend /12)); // apply pitchbend

			// OLD
			// outWave = VOsc.ar(
			// 	bufnumFirstTable + (stepFunc.value(tablePos) - 1).max(0.0001).min(6.999),
			// 	outFreq,
			// 	freqModFunc.value(inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth) * 2pi
			// );

			// NEW:

			// feedback current phase for threshold compare
			currentPhase = LocalIn.ar;

			// if Sync is on
			if (arrOptions[10] != 0, {
				syncTrigFunc = this.getSynthOption(9);
				syncTrig = syncTrigFunc.value(inExtSync, syncRatio, syncRatioMin, syncRatioMax, modSyncRatio, outFreq);
			},{
				syncTrig = 0;
			});
			syncedPhasorFunc = this.getSynthOption(10);
			outPhasor = syncedPhasorFunc.value(syncTrig, outFreq, syncThreshold, modSyncThreshold, currentPhase);


			// send current phase back to phasorFunc input
			LocalOut.ar(outPhasor);

			// phase distortion
			phaseDistortFunc = this.getSynthOption(12);
			outDistortedPhase = phaseDistortFunc.value(outPhasor, phaseDistort, phaseDistortMin, phaseDistortMax, modPhaseDistort);

			outWave = VOsc.ar(
				bufnumFirstTable + (stepFunc.value(tablePos) - 1).max(0.0001).min(6.999),
				0,  // all movement in phase input, so freq is 0
				((outDistortedPhase
					+ freqModFunc.value(inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth)) * 2pi
				).mod(2pi)
			);
			// apply smoothing
			smoothingFunc = this.getSynthOption(11);
			outSmoothed = smoothingFunc.value(outWave, syncTrig);

			ampCompFunction = this.getSynthOption(8);
			outAmpComp = ampCompFunction.value(outFreq);
			// amplitude is vel *  0.00315 approx. == 1 / 127
			// use TXClean to stop blowups
			Out.ar(out, TXClean.ar(outEnv * outSmoothed * outAmpComp * level * (velocity * 0.007874)));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TestNoteVals"],
			["TXMinMaxSliderSplit", "Table position", ControlSpec(1, 8),
				"tablePosition", "tablePositionMin", "tablePositionMax"],
			["SynthOptionPopup", "Stepping", arrOptionData, 0, 200],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
			["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 8],
			["MIDIListenCheckBox"],
			["MIDIChannelSelector"],
			["MIDINoteSelector"],
			["MIDIVelSelector"],
			["TXCheckBox", "Keyboard tracking", "keytrack"],
			["Transpose"],
			["TXMinMaxSliderSplit", "Pitch bend", classData.pitchbendSpec, "pitchbend", "pitchbendMin", "pitchbendMax"],
			["PolyphonySelector"],
			["TXEnvPlot", {this.envViewValues;}, {arg view; envView = view;}],
			["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView2}.defer;}],
			["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax",
				{{this.updateEnvView2}.defer;}],
			["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decay", "decayMin", "decayMax",
				{{this.updateEnvView2}.defer;}],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView2}.defer;}],
			["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTime", "sustainTimeMin",
				"sustainTimeMax",{{this.updateEnvView2}.defer;}],
			["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax",
				{{this.updateEnvView2}.defer;}],
			["SynthOptionPopup", "Curve", arrOptionData, 2, 200, {this.updateEnvView;}],
			["SynthOptionPopup", "Env Type", arrOptionData, 3, 300],
			["SynthOptionCheckBox", "Table Env", arrOptionData, 7, 120],
			["EZslider", "Env amount", ControlSpec(-1, 1), "env2Amount", nil, 260],
			["TXEnvPlot", {this.envViewValues2;}, {arg view; envView2 = view;}],
			["EZslider", "Pre-Delay2", ControlSpec(0,1), "delay2", {{this.updateEnvView2}.defer;}],
			["TXMinMaxSliderSplit", "Attack2", classData.timeSpec, "attack2", "attackMin2", "attackMax2",
				{this.updateEnvView2;}],
			["TXMinMaxSliderSplit", "Decay2", classData.timeSpec, "decay2", "decayMin2", "decayMax2",
				{this.updateEnvView2;}],
			["EZslider", "Sustain level2", ControlSpec(0, 1), "sustain2", {this.updateEnvView2;}],
			["TXMinMaxSliderSplit", "Sustain time2", classData.timeSpec, "sustainTime2", "sustainTimeMin2",
				"sustainTimeMax2",{this.updateEnvView2;}],
			["TXMinMaxSliderSplit", "Release2", classData.timeSpec, "release2", "releaseMin2", "releaseMax2",
				{this.updateEnvView2;}],
			["SynthOptionPopup", "Curve2", arrOptionData, 5, 180, {this.updateEnvView2;}],
			["SynthOptionPopup", "Env Type2", arrOptionData, 6, 180],
			["SynthOptionPopup", "Intonation", arrOptionData, 1, nil,
				{arg view; this.updateIntString(view.value)}],
			["TXStaticText", "Note ratios",
				{TXIntonation.arrScalesText.at(arrOptions.at(1));},
				{arg view; ratioView = view}],
			["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F",
				"F#", "G", "G#", "A", "A#", "B"], "intKey", nil, 140],
			["SynthOptionPopupPlusMinus", "Sync mode", arrOptionData, 10, 300],
			["SynthOptionPopupPlusMinus", "Sync trigger", arrOptionData, 9, 300],
			["TXMinMaxSliderSplit", "Sync ratio", classData.syncRatioSpec, "syncRatio", "syncRatioMin", "syncRatioMax",  ],
			["EZslider", "Sync threshold", ControlSpec(0, 1), "syncThreshold"],
			["SynthOptionCheckBox", "Add Phase Distortion (PD)", arrOptionData, 12, 200],
			["TXMinMaxSliderSplit", "Phase distort", classData.phaseDistortSpec,
				"phaseDistort", "phaseDistortMin", "phaseDistortMax",],
			["SynthOptionCheckBox", "Frequency Modulation using FM audio input", arrOptionData, 4, 300],
			["TXMinMaxSliderSplit", "FM depth", classData.fmDepthSpec,
				"fmDepth", "fmDepthMin", "fmDepthMax",],
			["SynthOptionPopupPlusMinus", "Smooth filter", arrOptionData, 11, 300],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.midiNoteInit;
		//	make buffers, load the synthdef and create the Group for synths to belong to
		this.makeBuffersAndGroup(classData.arrBufferSpecs);
		//	initialise buffers
		8.do({arg item, i;
			arrWaveSpecs = arrWaveSpecs.add(
				(	(Harmonics(16).decay.copyRange(0, i * 2)
					++ Array.newClear(32).fill(0)
				).keep(32)
				).max(0).min(1)
			);
		});
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// update buffers
			this.updateBuffers(arrWaveSpecs);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		//	initialise slots
		arrSlotData = arrWaveSpecs.dup(5);
		dataEvent = ();
		//	overwrite default preset
		this.overwritePreset(this, "Default Settings", 0);
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Wavetables", {displayOption = "showWavetables";
				this.buildGuiSpecArray; system.showView;}, 90,
			TXColor.white, this.getButtonColour(displayOption == "showWavetables")],
			["ActionButton", "Processes", {displayOption = "showProcesses";
				this.buildGuiSpecArray; system.showView;}, 84,
			TXColor.white, this.getButtonColour(displayOption == "showProcesses")],
			["ActionButton", "MIDI/ Note", {displayOption = "showMIDI";
				this.buildGuiSpecArray; system.showView;}, 86,
			TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
			["ActionButton", "Vol Env", {displayOption = "showEnv";
				this.buildGuiSpecArray; system.showView;}, 66,
			TXColor.white, this.getButtonColour(displayOption == "showEnv")],
			["ActionButton", "Table Env", {displayOption = "showEnv2";
				this.buildGuiSpecArray; system.showView;}, 72,
			TXColor.white, this.getButtonColour(displayOption == "showEnv2")],
			["ActionButton", "Sync PD FM Smooth", {displayOption = "showModOptions";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showModOptions")],
			["DividingLine"],
			["SpacerLine", 2],
		];
		if (displayOption == "showWavetables", {
			guiSpecArray = guiSpecArray ++[
				["TXWaveTableSpecs", "Wavetables", {arrWaveSpecs},
					{arg view; arrWaveSpecs = view.value;
						arrSlotData = view.arrSlotData; this.updateBuffers(view.value);},
					{arrSlotData}, {[8, 16, 24, 32].at(this.getSynthArgSpec("maxHarmonicsInd"))}, 0, 300,
					{this.getCurrentDataEvent;},
				],
				["NextLine"],
				["TXPopupAction", "No. harmonics", ["8", "16", "24", "32"], "maxHarmonicsInd",
					{this.updateBuffers(arrWaveSpecs); system.showView;}, 160],
				["SpacerLine", 2],
				["EZNumber", "Harmonic gap", ControlSpec(0, 3), "harmonicGap",
					{this.updateBuffers(arrWaveSpecs);  system.showViewIfModDisplay(this);}, nil, 76, 0.01],
				["ActionButton", "Set to 1 (default)",
					{this.setSynthArgSpec("harmonicGap", 1); system.showViewIfModDisplay(this)}, 100],
				["Spacer", 20],
				["EZNumber", "Scaling", ControlSpec(1, 5), "scaling",
					{this.updateBuffers(arrWaveSpecs); system.showViewIfModDisplay(this);}, nil, 76, 0.1],
				["ActionButton", "Set to 1 (default)",
					{this.setSynthArgSpec("scaling", 1); system.showViewIfModDisplay(this)}, 100],
				["DividingLine"],
				["NextLine"],
				["Spacer", 82],
				["ActionButton", "1", {this.setTablePosition(1);}, 24,],
				["Spacer", 13],
				["ActionButton", "2", {this.setTablePosition(2);}, 24,],
				["Spacer", 13],
				["ActionButton", "3", {this.setTablePosition(3);}, 24,],
				["Spacer", 13],
				["ActionButton", "4", {this.setTablePosition(4);}, 24,],
				["Spacer", 13],
				["ActionButton", "5", {this.setTablePosition(5);}, 24,],
				["Spacer", 13],
				["ActionButton", "6", {this.setTablePosition(6);}, 24,],
				["Spacer", 13],
				["ActionButton", "7", {this.setTablePosition(7);}, 24,],
				["Spacer", 13],
				["ActionButton", "8", {this.setTablePosition(8);}, 24,],
				["NextLine"],
				["TXMinMaxSliderSplit", "Table position", ControlSpec(1, 8),
					"tablePosition", "tablePositionMin", "tablePositionMax"],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Stepping", arrOptionData, 0, 320],
				["DividingLine"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0, 1), "level"],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 8],
			];
		});
		if (displayOption == "showProcesses", {
			guiSpecArray = guiSpecArray ++[
				["TXWaveTableSpecs", "Wavetables", {arrWaveSpecs},
					{arg view; arrWaveSpecs = view.value;
						arrSlotData = view.arrSlotData; this.updateBuffers(view.value);},
					{arrSlotData}, {[8, 16, 24, 32].at(this.getSynthArgSpec("maxHarmonicsInd"))}, 1, 300,
					{this.getCurrentDataEvent;},
				],
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
				["TXMinMaxSliderSplit", "Pitch bend", classData.pitchbendSpec, "pitchbend",
					"pitchbendMin", "pitchbendMax", nil,
					[	["Bend Range Presets: ", [-2, 2]], ["Range -1 to 1", [-1, 1]], ["Range -2 to 2", [-2, 2]],
						["Range -7 to 7", [-7, 7]], ["Range -12 to 12", [-12, 12]],
						["Range -24 to 24", [-24, 24]], ["Range -48 to 48", [-48, 48]] ] ],
				["DividingLine"],
				["PolyphonySelector"],
				["DividingLine"],
				//			["TestNoteVals"],
				["SynthOptionPopupPlusMinus", "Intonation", arrOptionData, 1, 300,
					{arg view; this.updateIntString(view.value)}],
				["Spacer", 10],
				["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F",
					"F#", "G", "G#", "A", "A#", "B"], "intKey", nil, 140],
				["NextLine"],
				["TXStaticText", "Note ratios",
					{TXIntonation.arrScalesText.at(arrOptions.at(1));},
					{arg view; ratioView = view}],
				["DividingLine"],
				["MIDIKeyboard", {arg note; this.createSynthNote(note, testMIDIVel, 0);},
					5, 60, nil, 36, {arg note; this.releaseSynthGate(note);}],
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
				["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView}.defer;}],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax",
					{{this.updateEnvView}.defer;}],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decay", "decayMin", "decayMax",
					{{this.updateEnvView}.defer;}],
				["SpacerLine", 4],
				["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView}.defer;}],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTime", "sustainTimeMin",
					"sustainTimeMax",{{this.updateEnvView}.defer;}],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax",
					{{this.updateEnvView}.defer;}],
				["SpacerLine", 4],
				["SynthOptionPopup", "Curve", arrOptionData, 2, 230, {this.updateEnvView;}],
				["Spacer", 20],
				["SynthOptionPopup", "Env Type", arrOptionData, 3, 288],
			];
		});
		if (displayOption == "showEnv2", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionCheckBox", "Table Env", arrOptionData, 7, 120],
				["Spacer", 20],
				["EZslider", "Env amount", ControlSpec(-1, 1), "env2Amount", nil, 260],
				["SpacerLine", 4],
				["TXPresetPopup", "Env presets",
					TXEnvPresets.arrEnvPresets2(this, 5, 6).collect({arg item, i; item.at(0)}),
					TXEnvPresets.arrEnvPresets2(this, 5, 6).collect({arg item, i; item.at(1)})
				],
				["TXEnvPlot", {this.envViewValues2;}, {arg view; envView2 = view;}],
				["NextLine"],
				["EZslider", "Pre-Delay", ControlSpec(0,1), "delay2", {this.updateEnvView2;}],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack2", "attackMin2", "attackMax2",
					{this.updateEnvView2;}],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decay2", "decayMin2", "decayMax2",
					{this.updateEnvView2;}],
				["SpacerLine", 4],
				["EZslider", "Sustain level", ControlSpec(0, 1), "sustain2", {this.updateEnvView2;}],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTime2", "sustainTimeMin2",
					"sustainTimeMax2",{this.updateEnvView2;}],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release2", "releaseMin2", "releaseMax2",
					{this.updateEnvView2;}],
				["SpacerLine", 4],
				["SynthOptionPopup", "Curve", arrOptionData, 5, 230, {this.updateEnvView2;}],
				["Spacer", 20],
				["SynthOptionPopup", "Env Type", arrOptionData, 6, 288],
			];
		});
		if (displayOption == "showModOptions", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Sync mode", arrOptionData, 10, 470],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Sync trigger", arrOptionData, 9, 470],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Sync ratio", classData.syncRatioSpec, "syncRatio",
					"syncRatioMin", "syncRatioMax",  ],
				["SpacerLine", 4],
				["EZslider", "Sync threshold", ControlSpec(0, 1), "syncThreshold"],
				["DividingLine"],
				["SpacerLine", 6],
				["SynthOptionCheckBox", "Add Phase Distortion (PD)", arrOptionData, 12, 200],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Phase distort", classData.phaseDistortSpec,
					"phaseDistort", "phaseDistortMin", "phaseDistortMax",],
				["DividingLine"],
				["SpacerLine", 6],
				["SynthOptionCheckBox", "Frequency Modulation using FM audio input", arrOptionData, 4, 300],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "FM depth", classData.fmDepthSpec,
				"fmDepth", "fmDepthMin", "fmDepthMax",],
				["DividingLine"],
				["SpacerLine", 6],
				["SynthOptionPopupPlusMinus", "Smooth filter", arrOptionData, 11, 340],
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

	setTablePosition {arg val;
		var min = this.getSynthArgSpec("tablePositionMin");
		var max = this.getSynthArgSpec("tablePositionMax");
		if ((val >= min) and: {(val <= max)}, {
			this.setSynthValue("tablePosition", ControlSpec(min, max).unmap(val));
		});
		system.flagGuiUpd;
	}

	getCurrentDataEvent {
		dataEvent.harmonicGap = this.getSynthArgSpec("harmonicGap");
		dataEvent.scaling = this.getSynthArgSpec("scaling");
		^dataEvent;
	}

	updateBuffers { arg arrSpecs;
		var holdNoHarmonics, holdScaling;
		if (arrSpecs.notNil, {
			// only generate required no of harmonics
			holdNoHarmonics = [8, 16, 24, 32].at(this.getSynthArgSpec("maxHarmonicsInd"));
			// use scaling
			holdScaling = this.getSynthArgSpec("scaling");
			// generate wavetables in buffers
			buffers.do({arg item, i;
				var holdSpec, holdFreqs;
				holdSpec = ((arrSpecs.at(i).keep(holdNoHarmonics.asInteger) ++ (0!32)).keep(32))
				// first harmonic is > 0 as all zero's would crash method
				.max([0.0001] ++  (0!31));
				// apply scaling
				holdSpec = holdSpec ** holdScaling;
				// generate wavetables
				holdFreqs = 32.collect({arg item, i; 1 + (item * this.getSynthArgSpec("harmonicGap"));});
				item.sine2(holdFreqs, holdSpec);
			});
		});
	}

	extraSaveData { // override default method
		^[arrWaveSpecs, arrSlotData, testMIDINote, testMIDIVel, testMIDITime];
	}

	loadExtraData {arg argData;  // override default method
		arrWaveSpecs = argData.at(0);
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// update buffers
			this.updateBuffers(arrWaveSpecs);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		arrSlotData = argData.at(1);
		testMIDINote = argData.at(2);
		testMIDIVel = argData.at(3);
		testMIDITime = argData.at(4);
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
		envCurve = this.getSynthOption(5);
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

