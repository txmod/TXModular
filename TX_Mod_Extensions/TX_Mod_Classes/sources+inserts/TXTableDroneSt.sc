// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXTableDroneSt : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var ratioView;
	var noteListTextView;
	var arrWaveSpecs;
	var arrSlotData;
	var arrSignals;
	var dataEvent;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Table Drone St";
		classData.moduleRate = "audio";
		classData.moduleType = "source";
		classData.arrAudSCInBusSpecs = [
			["FM input", 1, "inmodulator"],
			["Ext Sync input", 1, "inExtSync"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Table position", 1, "modTablePosition", 0],
			["Base freq", 1, "modFreq", 0],
			["Beats frequency", 1, "modBeatsFreq", 0],
			["Note select", 1, "modFreqSelector", 0],
			["Smoothtime 1", 1, "modLag", 0],
			["Smoothtime 2", 1, "modLag2", 0],
			["Sync freq", 1, "modSyncFreq", 0],
			["Sync ratio", 1, "modSyncRatio", 0],
			["Sync threshold", 1, "modSyncThreshold", 0],
			["Phase distort", 1, "modPhaseDistort", 0],
			["FM depth", 1, "modFMDepth", 0],
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.arrBufferSpecs = [ ["bufnumFirstTable", 1024, 1, 8] ]; // allocate 8 consecutive mono buffers
		classData.timeSpec = ControlSpec(0.01, 30, \exp, 0, 1, units: " secs");
		classData.freqSpec = ControlSpec(0.midicps, 20000, \exponential);
		classData.syncRatioSpec = ControlSpec(0.01, 20);
		classData.syncFreqSpec = ControlSpec(0.1, 10000, \exp);
		classData.phaseDistortSpec = ControlSpec(-5, 5);
		classData.fmDepthSpec = ControlSpec(0, 8);
		classData.guiWidth = 600;
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
			["maxHarmonicsInd", 1, \ir],
			["harmonicGap", 1, \ir],
			["scaling", 1, \ir],
			["freq", 0.5, defLagTime],
			["freqMin", 0.midicps, defLagTime],
			["freqMax", 127.midicps, defLagTime],
			["beatsFreq", 0.2, defLagTime],
			["beatsFreqMin", 0, defLagTime],
			["beatsFreqMax", 5, defLagTime],
			["i_noteListTypeInd",12, \ir],
			["i_noteListRoot", 0, \ir],
			["i_noteListMin", 36, \ir],
			["i_noteListMax", 72, \ir],
			["i_noteListSize", 1, \ir],
			["freqSelector", 0.5, defLagTime],
			["lag", 0.5, defLagTime],
			["lagMin", 0.01, defLagTime],
			["lagMax", 1, defLagTime],
			["lag2", 0.5, defLagTime],
			["lag2Min", 0.01, defLagTime],
			["lag2Max", 1, defLagTime],
			["syncFreq", classData.syncFreqSpec.unmap(60.midicps), defLagTime],
			["syncFreqMin", 0.1, defLagTime],
			["syncFreqMax", 10000, defLagTime],
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
			["modTablePosition", 0, defLagTime],
			["modFreq", 0, defLagTime],
			["modBeatsFreq", 0, defLagTime],
			["modFreqSelector", 0, defLagTime],
			["modLag", 0, defLagTime],
			["modLag2", 0, defLagTime],
			["modSyncFreq", 0, defLagTime],
			["modSyncRatio", 0, defLagTime],
			["modSyncThreshold", 0, defLagTime],
			["modPhaseDistort", 0, defLagTime],
			["modFMDepth", 0, defLagTime],
		];
		arrOptions = 0 ! 9;
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
			[
				["Off", {arg freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector;
					//( (freqMax/freqMin) ** ((freq + modFreq).max(0.0001).min(1)) ) * freqMin;
					(freq + modFreq).max(0).min(1).linexp(0, 1, freqMin, freqMax)
				}],
				["On", {arg freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector;
					var holdArray;
					// convert to cps
					holdArray = this.getNoteArray.midicps;
					Select.kr( (((freqSelector + modFreqSelector).max(0).min(1)) * holdArray.size), holdArray);
				}],
			],
			// ind2
			[
				["None",
					{arg input, lagtime; input;}
				],
				["Linear - use time 1 for up and down smoothing",
					{arg input, lagtime; Ramp.kr(input, lagtime); }
				],
				["Exponential 1 - use time 1 for up and down smoothing",
					{arg input, lagtime; Lag.kr(input, lagtime); }
				],
				["Exponential 2 - use time 1 for up and down smoothing",
					{arg input, lagtime; Lag2.kr(input, lagtime); }
				],
				["Exponential 3 - use time 1 for up and down smoothing",
					{arg input, lagtime; Lag3.kr(input, lagtime); }
				],
				["Exponential 1 - use time 1 for up, time 2 for down smoothing",
					{arg input, lagtime, lagtime2; LagUD.kr(input, lagtime, lagtime2); }
				],
				["Exponential 2 - use time 1 for up, time 2 for down smoothing",
					{arg input, lagtime, lagtime2; Lag2UD.kr(input, lagtime, lagtime2); }
				],
				["Exponential 3 - use time 1 for up, time 2 for down smoothing",
					{arg input, lagtime, lagtime2; Lag3UD.kr(input, lagtime, lagtime2); }
				],
			],
			// ind3 Frequency Modulation
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
			// ind4 amp compensation
			TXAmpComp.arrOptionData,
			// ind5 - sync triggering
			[
				["Internal - use sync frequency",
					{arg inExtSync, syncFreq, syncFreqMin, syncFreqMax, modSyncFreq,
						syncRatio, syncRatioMin, syncRatioMax, modSyncRatio, outFreq;
						var syncFreqSum;
						syncFreqSum = (syncFreq + modSyncFreq).linexp(0, 1, syncFreqMin, syncFreqMax, 'minmax');
						Impulse.ar(syncFreqSum);
					}
				],
				["Internal - use [sync ratio x base freq] as sync frequency",
					{arg inExtSync, syncFreq, syncFreqMin, syncFreqMax, modSyncFreq,
						syncRatio, syncRatioMin, syncRatioMax, modSyncRatio, outFreq;
						var syncRatioSum;
						syncRatioSum = (syncRatio + modSyncRatio).linlin(0, 1, syncRatioMin, syncRatioMax, 'minmax');
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
			// ind6 - sync mode - returns synced Phasor
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
			// ind7 - smoothing
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
			// ind8 Phase distort
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
			arg inmodulator, inExtSync, out, maxHarmonicsInd, harmonicGap, scaling,
			freq, freqMin, freqMax,
			beatsFreq, beatsFreqMin, beatsFreqMax,
			i_noteListTypeInd, i_noteListRoot, i_noteListMin,  i_noteListMax, i_noteListSize, freqSelector,
			lag, lagMin, lagMax, lag2, lag2Min, lag2Max,
			syncFreq, syncFreqMin, syncFreqMax, syncRatio, syncRatioMin, syncRatioMax, syncThreshold,
			phaseDistort, phaseDistortMin, phaseDistortMax, fmDepth, fmDepthMin, fmDepthMax,
			bufnumFirstTable, tablePosition, tablePositionMin, tablePositionMax, level,
			modTablePosition=0, modFreq = 0,
			modBeatsFreq = 0,
			modFreqSelector = 0, modLag = 0, modLag2 = 0,
			modSyncFreq=0, modSyncRatio=0, modSyncThreshold=0, modPhaseDistort=0, modFMDepth=0;

			var	stepFunc, intonationFunc, freqModFunc, outWave, tablePos, ampCompFunction, outAmpComp,
			outFreqFunc, outFreq, outBeatsFreq, outFreqLag, waveFunction, lagtime, lagtime2, outLagFunction,
			currentPhase, outPhasor, syncTrig, syncTrigFunc, syncedPhasorFunc, smoothingFunc, outSmoothed,
			phaseDistortFunc, outDistortedPhase;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			tablePos = tablePositionMin + ((tablePositionMax - tablePositionMin)
				* (tablePosition + modTablePosition).max(0).min(1));
			stepFunc = this.getSynthOption(0);
			freqModFunc = this.getSynthOption(3);
			outFreqFunc = this.getSynthOption(1);
			outFreq = outFreqFunc.value(freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector);
			outBeatsFreq = 0.5 * (beatsFreq + modBeatsFreq).linlin(0, 1, beatsFreqMin, beatsFreqMax, 'minmax');
			lagtime = ( (lagMax/lagMin) ** ((lag + modLag).max(0.001).min(1)) ) * lagMin;
			lagtime2 = ( (lag2Max/lag2Min) ** ((lag2 + modLag2).max(0.001).min(1)) ) * lag2Min;
			outLagFunction = this.getSynthOption(2);
			outFreqLag = outLagFunction.value(outFreq + [outBeatsFreq, outBeatsFreq.neg], lagtime, lagtime2);

			// OLD
			// outWave = VOsc.ar(
			// 	bufnumFirstTable + (stepFunc.value(tablePos) - 1).max(0.0001).min(6.999),
			// 	outFreq,
			// 	freqModFunc.value(inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth) * 2pi
			// );

			// feedback current phase for threshold compare
			currentPhase = LocalIn.ar(2);

			// if Sync is on
			if (arrOptions[6] != 0, {
				syncTrigFunc = this.getSynthOption(5);
				syncTrig = syncTrigFunc.value(inExtSync, syncFreq, syncFreqMin, syncFreqMax, modSyncFreq,
					syncRatio, syncRatioMin, syncRatioMax, modSyncRatio, outFreqLag);
			},{
				syncTrig = 0;
			});
			syncedPhasorFunc = this.getSynthOption(6);
			outPhasor = syncedPhasorFunc.value(syncTrig, outFreqLag, syncThreshold, modSyncThreshold, currentPhase);

			// send current phase back to phasorFunc input
			LocalOut.ar(outPhasor);

			// phase distortion
			phaseDistortFunc = this.getSynthOption(8);
			outDistortedPhase = phaseDistortFunc.value(outPhasor, phaseDistort, phaseDistortMin, phaseDistortMax, modPhaseDistort);

			outWave = VOsc.ar(
				bufnumFirstTable + (stepFunc.value(tablePos) - 1).max(0.0001).min(6.999),
				0,  // all movement in phase input, so freq is 0
				((outDistortedPhase
					+ freqModFunc.value(inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth)) * 2pi
				).mod(2pi)
			);
			// apply smoothing
			smoothingFunc = this.getSynthOption(7);
			outSmoothed = smoothingFunc.value(outWave, syncTrig);

			ampCompFunction = this.getSynthOption(4);
			outAmpComp = ampCompFunction.value(outFreqLag);
			// amplitude is vel *  0.00315 approx. == 1 / 127
			// use TXClean to stop blowups
			Out.ar(out, TXClean.ar(startEnv * outSmoothed * outAmpComp * level));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TestNoteVals"],
			["TXMinMaxSliderSplit", "Table position", ControlSpec(1, 8),
				"tablePosition", "tablePositionMin", "tablePositionMax"],
			["SynthOptionPopup", "Stepping", arrOptionData, 0, 200],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
			["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 4],
			["TXMinMaxFreqNoteSldr", "Freq", ControlSpec(0.midicps, 20000, \exponential),
				"freq", "freqMin", "freqMax", nil, TXFreqRangePresets.arrFreqRanges],
			["SynthOptionCheckBox", "Use note list instead of variable frequency", arrOptionData, 1, 400],
			["EZslider", "Note select", ControlSpec(0, 1), "freqSelector"],
			["TXPopupAction", "Chord/ scale", TXScale.arrScaleNames, "i_noteListTypeInd", {this.updateSynth;}, 400],
			["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
				"i_noteListRoot", {this.updateSynth;}, 140],
			["TXNoteRangeSlider", "Note range", "i_noteListMin", "i_noteListMax", {this.updateSynth;}, true],
			["TXStaticText", "Note count", {this.getNoteTotalText}, {arg view; noteListTextView = view;}],
			["TXMinMaxSliderSplit", "Beats Freq", ControlSpec(0,100),
				"beatsFreq", "beatsFreqMin", "beatsFreqMax"],
			["SynthOptionPopupPlusMinus", "Smoothing", arrOptionData, 2],
			["TXMinMaxSliderSplit", "Time 1", classData.timeSpec, "lag", "lagMin", "lagMax"],
			["TXMinMaxSliderSplit", "Time 2", classData.timeSpec, "lag2", "lag2Min", "lag2Max"],
			["SynthOptionPopupPlusMinus", "Sync mode", arrOptionData, 6, 300],
			["SynthOptionPopupPlusMinus", "Sync trigger", arrOptionData, 5, 300],
			["TXMinMaxSliderSplit", "Sync ratio", classData.syncRatioSpec, "syncRatio", "syncRatioMin", "syncRatioMax",  ],
			["EZslider", "Sync threshold", ControlSpec(0, 1), "syncThreshold"],
			["SynthOptionCheckBox", "Add Phase Distortion (PD)", arrOptionData, 8, 200],
			["TXMinMaxSliderSplit", "Phase distort", classData.phaseDistortSpec,
				"phaseDistort", "phaseDistortMin", "phaseDistortMax",],
			["SynthOptionCheckBox", "Frequency Modulation using FM audio input", arrOptionData, 3, 300],
			["TXMinMaxSliderSplit", "FM depth", classData.fmDepthSpec,
				"fmDepth", "fmDepthMin", "fmDepthMax",],
			["SynthOptionPopupPlusMinus", "Smooth filter", arrOptionData, 7, 300],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make buffers, load the synthdef and create the Group for synths to belong to
		this.makeBuffersAndSynth(classData.arrBufferSpecs);
		this.getNoteArray; // initialise
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
			["ActionButton", "Base Frequency", {displayOption = "showFreq";
				this.buildGuiSpecArray; system.showView;}, 110,
			TXColor.white, this.getButtonColour(displayOption == "showFreq")],
			["ActionButton", "Base Freq Smooth", {displayOption = "showSmoothing";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showSmoothing")],
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
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 4],
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
		if (displayOption == "showFreq", {
			guiSpecArray = guiSpecArray ++[
				["TXMinMaxFreqNoteSldr", "Base Freq", classData.freqSpec,
					"freq", "freqMin", "freqMax", nil, TXFreqRangePresets.arrFreqRanges],
				["DividingLine"],
				["SpacerLine", 2],
				["SynthOptionCheckBox", "Use note list instead of variable frequency", arrOptionData, 1, 400],
				["SpacerLine", 2],
				["TXListViewActionPlusMinus", "Chord/ scale", TXScale.arrScaleNames, "i_noteListTypeInd",
					{this.updateSynth;}, 400, 120],
				["SpacerLine", 2],
				["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
					"i_noteListRoot", {this.updateSynth;}, 140],
				["SpacerLine", 2],
				["TXNoteRangeSlider", "Note range", "i_noteListMin", "i_noteListMax", {this.updateSynth;}, true],
				["SpacerLine", 2],
				["TXStaticText", "Note count", {this.getNoteTotalText}, {arg view; noteListTextView = view;}],
				["SpacerLine", 2],
				["EZslider", "Note select", ControlSpec(0, 1), "freqSelector"],
				["DividingLine"],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Beats Freq", ControlSpec(0,100),
					"beatsFreq", "beatsFreqMin", "beatsFreqMax"],
			];
		});
		if (displayOption == "showSmoothing", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Smoothing", arrOptionData, 2],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Time 1", classData.timeSpec, "lag", "lagMin", "lagMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Time 2", classData.timeSpec, "lag2", "lag2Min", "lag2Max"],
			];
		});
		if (displayOption == "showModOptions", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Sync mode", arrOptionData, 6, 470],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Sync trigger", arrOptionData, 5, 470],
				["SpacerLine", 4],
				["TXMinMaxFreqNoteSldr", "Sync Freq", classData.syncFreqSpec,
					"syncFreq", "syncFreqMin", "syncFreqMax", nil, TXFreqRangePresets.arrFreqRanges],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Sync ratio", classData.syncRatioSpec, "syncRatio",
					"syncRatioMin", "syncRatioMax",  ],
				["SpacerLine", 4],
				["EZslider", "Sync threshold", ControlSpec(0, 1), "syncThreshold"],
				["DividingLine"],
				["SpacerLine", 6],
				["SynthOptionCheckBox", "Add Phase Distortion (PD)", arrOptionData, 8, 200],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Phase distort", classData.phaseDistortSpec,
					"phaseDistort", "phaseDistortMin", "phaseDistortMax",],
				["DividingLine"],
				["SpacerLine", 6],
				["SynthOptionCheckBox", "Frequency Modulation using FM audio input", arrOptionData, 3, 300],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "FM depth", classData.fmDepthSpec,
				"fmDepth", "fmDepthMin", "fmDepthMax",],
				["DividingLine"],
				["SpacerLine", 6],
				["SynthOptionPopupPlusMinus", "Smooth filter", arrOptionData, 7, 340],
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
		^[arrWaveSpecs, arrSlotData];
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
	}

	getNoteArray {
		var arrScaleSpec, scaleRoot, noteMin, noteMax, arrScaleNotes;
		// Generate array of notes from chord, mode, scale
		arrScaleSpec = TXScale.arrScaleNotes.at(this.getSynthArgSpec("i_noteListTypeInd"));
		scaleRoot = this.getSynthArgSpec("i_noteListRoot");
		noteMin = this.getSynthArgSpec("i_noteListMin");
		noteMax = this.getSynthArgSpec("i_noteListMax");
		arrScaleNotes = [];
		13.do({arg octave;
			arrScaleSpec.do({arg item, i;
				arrScaleNotes = arrScaleNotes.add((octave * 12) + scaleRoot + item);
			});
		});
		arrScaleNotes = arrScaleNotes.select({arg item, i; ((item >= noteMin) and: (item <= noteMax)); });
		this.setSynthArgSpec("i_noteListSize", arrScaleNotes.size);
		if (arrScaleNotes.size == 0, {
			arrScaleNotes = [noteMin];
		});
		^arrScaleNotes;
	}

	getNoteTotalText {
		var noteListSize, outText;
		noteListSize = this.getSynthArgSpec("i_noteListSize");
		if (noteListSize == 0, {
			outText = "ERROR: No notes in note list - need to widen range ";
		}, {
			outText = "Total no. of notes:  " ++ noteListSize.asString;
		});
		^outText;
	}

	updateSynth {
		this.getNoteArray;
		this.rebuildSynth;
		if (noteListTextView.notNil, {
			{noteListTextView.string = this.getNoteTotalText;}.defer();
		});
	}

}

