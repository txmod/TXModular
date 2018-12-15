// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXLoopPlayer : TXModuleBase {

	classvar <>classData;

	var <>loopNo = 0;
	var <>bankNo = 0;
	var loopFileName = "";
	var showWaveform = 0;
	var sampleDataEvent;
	var loopNumChannels = 0;
	var loopTotalBeats = 1;
	var loopOriginalBPM = 0;
	var displayOption;
	var <>testMIDINote = 69;
	var <>testMIDIVel = 127;
	var <>testMIDITime = 8;
	var arrFeel1Values;
	var arrFeel2Values;
	var arrFeelSlotData, feel1DataEvent, feel2DataEvent;
	var arrGroove1Values;
	var arrGroove2Values;
	var arrGrooveSlotData, groove1DataEvent, groove2DataEvent;
	var arrGridPresetNames, arrGridPresetActions;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Loop Player";
		classData.moduleRate = "audio";
		classData.moduleType = "groupsource";
		classData.arrCtlSCInBusSpecs = [
			["Loop Start", 1, "modStart", 0],
			["Loop End", 1, "modEnd", 0],
			["Loop Reverse", 1, "modReverse", 0],
			["Pitch Shift", 1, "modPitchShift", 0],
			["Feel morph", 1, "modFeelMorph", 0],
			["Groove morph", 1, "modGrooveMorph", 0],
			["Attack", 1, "modAttack", 0],
			["Release", 1, "modRelease", 0],
			["Play BPM", 1, "modSeqBPM", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrBufferSpecs = [ ["bufnumLoop", 2048, 1], ["bufnumFeel1", 512, 1], ["bufnumFeel2", 512, 1],
			["bufnumGroove1", 512, 1], ["bufnumGroove2", 512, 1],];
		classData.guiWidth = 750;
		classData.timeSpec = ControlSpec(0.01, 30);
	} // end of method initClass

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	*reloadAllLoops{arg argbankNo, argindex;
		classData.arrInstances.do({ arg item, i;
			if (argbankNo.isNil or: {argbankNo == item.bankNo}, {
				item.loadLoop(item.loopNo);
			});
		});
	}

	init {arg argInstName;
		//	set  class specific instance variables
		feel1DataEvent = ();
		feel2DataEvent = ();
		groove1DataEvent = ();
		groove2DataEvent = ();
		displayOption = "showLoop";
		arrSynthArgSpecs = [
			["out", 0, 0],
			["gate", 1, 0],
			["note", 0, 0],
			["velocity", 0, 0],
			["seqBPM", 0.3979933, 0], 		// default BPM is set for 120 bpm
			["seqBPMMin", 1, 0],
			["seqBPMMax", 300, 0],
			["speedFactor", 0, 0],
			["bufnumLoop", 0, \ir],
			["bufnumFeel1", 0, \ir],
			["bufnumFeel2", 0, \ir],
			["bufnumGroove1", 0, \ir],
			["bufnumGroove2", 0, \ir],
			["bankNo", 0, \ir],
			["loopNo", 0, \ir],
			["loopTotalBeats", 1, \ir],
			["start", 0, defLagTime],
			["end", 1, defLagTime],
			["reverse", 0, defLagTime],
			["level", 0.5, defLagTime],
			["envtime", 0, defLagTime],
			["attack", 0, defLagTime],
			["attackMin", 0.01, defLagTime],
			["attackMax", 5, defLagTime],
			["release", 0, defLagTime],
			["releaseMin", 0.01, defLagTime],
			["releaseMax", 5, defLagTime],
			["pitchShift", 0.5, defLagTime],
			["pitchShiftMin", -12, defLagTime],
			["pitchShiftMax", 12, defLagTime],
			["windowRandomness", 0, defLagTime],
			["feelMulti", 1, 0],
			["feelMorph", 0, defLagTime],
			["feelMorphMin", 0, defLagTime],
			["feelMorphMax", 1, defLagTime],
			["grooveMulti", 1, 0],
			["grooveMorph", 0, defLagTime],
			["grooveMorphMin", 0, defLagTime],
			["grooveMorphMax", 1, defLagTime],
			["modStart", 0, defLagTime],
			["modEnd", 0, defLagTime],
			["modReverse", 0, defLagTime],
			["modPitchShift", 0, defLagTime],
			["modFeelMorph", 0, defLagTime],
			["modGrooveMorph", 0, defLagTime],
			["modAttack", 0, defLagTime],
			["modRelease", 0, defLagTime],
			["modSeqBPM", 0, defLagTime],
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
		// create looping option
		arrOptions = [0, 0, 0, 1, 1, 0, 0, 0, 0];
		arrOptionData = [
			[ // 0 - loop type indexing function
				["Looped - play while gate is on",
					{arg outRate, bufnumLoop, sStart, sEnd;
						Phasor.ar(0, outRate * SampleDur.ir/
							((sEnd - sStart).max(0.0001) * BufDur.kr(bufnumLoop)));
					}
				],
				["Single shot - play while gate is on",
					{arg outRate, bufnumLoop, sStart, sEnd;
						var outSig = Sweep.ar(1, outRate /
							((sEnd - sStart).max(0.00001) * BufDur.kr(bufnumLoop)));
						FreeSelf.kr(outSig - 1);
						outSig;
					}
				],
				["Single shot - play whole loop",
					// note - function is same as "Single shot - play while gated"
					{arg outRate, bufnumLoop, sStart, sEnd;
						var outSig = Sweep.ar(1, outRate /
							((sEnd - sStart).max(0.00001) * BufDur.kr(bufnumLoop)));
						FreeSelf.kr(outSig - 1);
						outSig;
					}
				]
			],
			[// 1 - Time-stretching
				["Off", {arg bufnumLoop, bufIndex;
					BufRd.ar(1, bufnumLoop, bufIndex * BufFrames.kr(bufnumLoop), 0);
				}],
				["Granular time-stretch", {
					arg bufnumLoop, bufIndex, freqScale, windowSize, overlaps, randomness, interpolation;
					Warp1.ar(1, bufnumLoop, bufIndex, freqScale, windowSize,
						-1, overlaps, randomness, interpolation);
				}],
				["Granular time-stretch with zero-crossing search (best)", {
					arg bufnumLoop, bufIndex, freqScale, windowSize, overlaps, randomness, interpolation;
					WarpZ.ar(1, bufnumLoop, bufIndex, freqScale, windowSize,
						-1, overlaps, randomness, interpolation, 0.02, 0.1);
				}],
			],
			[// 2 - Window size
				["50 ms", 0.05],
				["100 ms", 0.1],
				["150 ms", 0.15],
				["200 ms", 0.2],
				["250 ms", 0.25],
				["300 ms", 0.3],
				["350 ms", 0.35],
				["400 ms", 0.4],
				["450 ms", 0.45],
				["500 ms", 0.5],
				["10 ms", 0.01],
				["20 ms", 0.02],
				["30 ms", 0.03],
				["40 ms", 0.04],
				["50 ms", 0.05],
				["60 ms", 0.06],
				["70 ms", 0.07],
				["80 ms", 0.08],
				["90 ms", 0.09],
			],
			[// 3 - Overlaps
				["1", 1],
				["2 (default)", 2],
				["3", 3],
				["4", 4],
				["5", 5],
				["6", 6],
				["7", 7],
				["8", 8],
				["9", 9],
				["10", 10],
				["11", 11],
				["12", 12],
				["13", 13],
				["14", 14],
				["15", 15],
				["16", 16],
			],
			[// 4 - Interpolation
				["Linear", 2],
				["Cubic (best)", 4],
			],
			[ // 5 - groove curve
				["Off",
					{arg index; index;}
				],
				["On - use Groove curve 1",
					{arg index, bufnumGroove1, bufnumGroove2, grooveMulti;
						var grooveindScaled = index * grooveMulti;
						var grooveMinInd = grooveindScaled.floor / grooveMulti;
						var groovelookupInd = grooveindScaled.frac * 511;
						var lookupVal = BufRd.ar(1, bufnumGroove1, groovelookupInd);
						grooveMinInd + (lookupVal / grooveMulti);
					}
				],
				["On - use Groove curve 2",
					{arg index, bufnumGroove1, bufnumGroove2, grooveMulti;
						var grooveindScaled = index * grooveMulti;
						var grooveMinInd = grooveindScaled.floor / grooveMulti;
						var groovelookupInd = grooveindScaled.frac * 511;
						var lookupVal = BufRd.ar(1, bufnumGroove2, groovelookupInd);
						grooveMinInd + (lookupVal / grooveMulti);
					}
				],
				["On - morph Groove curves 1 & 2",
					{arg index, bufnumGroove1, bufnumGroove2, grooveMulti, grooveMorph, modGrooveMorph,
						grooveMorphMin, grooveMorphMax;
						var grooveindScaled = index * grooveMulti;
						var grooveMinInd = grooveindScaled.floor / grooveMulti;
						var groovelookupInd = grooveindScaled.frac * 511;
						var outGrooveMorph = (grooveMorph + modGrooveMorph).max(0).min(1).linlin(
							0, 1, grooveMorphMin, grooveMorphMax, nil);
						var arGrooveMorph = K2A.ar(outGrooveMorph);
						var sig1 = BufRd.ar(1, bufnumGroove1, groovelookupInd);
						var sig2 = BufRd.ar(1, bufnumGroove2, groovelookupInd);
						var lookupVal = (sig1 * (1 - arGrooveMorph)) + (sig2 * arGrooveMorph);
						grooveMinInd + (lookupVal / grooveMulti);
					}
				],
			],
			// 6 - Env Curve
			TXEnvLookup.arrDADSRSlopeOptionData,
			[ // 7 - feel curve
				["Off",
					{1;}
				],
				["On - use Feel curve 1",
					{arg index, bufnumFeel1, bufnumFeel2, feelMulti;
						var feelIndScaled = index * feelMulti;
						var feelLookupInd = feelIndScaled.frac * 511;
						BufRd.ar(1, bufnumFeel1, feelLookupInd);
					}
				],
				["On - use Feel curve 2",
					{arg index, bufnumFeel1, bufnumFeel2, feelMulti;
						var feelIndScaled = index * feelMulti;
						var feelLookupInd = feelIndScaled.frac * 511;
						BufRd.ar(1, bufnumFeel2, feelLookupInd);
					}
				],
				["On - morph Feel curves 1 & 2",
					{arg index, bufnumFeel1, bufnumFeel2, feelMulti, feelMorph, modFeelMorph, feelMorphMin, feelMorphMax;
						var feelIndScaled = index * feelMulti;
						var feelLookupInd = feelIndScaled.frac * 511;
						var sig1 = BufRd.ar(1, bufnumFeel1, feelLookupInd);
						var sig2 = BufRd.ar(1, bufnumFeel2, feelLookupInd);
						var outFeelMorph = (feelMorph + modFeelMorph).max(0).min(1).linlin(0, 1, feelMorphMin, feelMorphMax, nil);
						(sig1 * (1 - outFeelMorph)) + (sig2 * outFeelMorph);
					}
				],
			],
			[ // 8 - add feel after groove (selects index used for feel curve)
				["Off",
					{arg index, grooveIndex; grooveIndex;}
				],
				["On",
					{arg index, grooveIndex; index;}
				],
			],
		];
		synthDefFunc = {
			arg out, gate, note, velocity, seqBPM, seqBPMMin, seqBPMMax, speedFactor,
			bufnumLoop, bufnumFeel1, bufnumFeel2, bufnumGroove1, bufnumGroove2,
			bankNo, loopNo, loopTotalBeats, start, end, reverse, level,
			envtime=0, attack, attackMin, attackMax, release, releaseMin, releaseMax,
			pitchShift, pitchShiftMin, pitchShiftMax, windowRandomness,
			feelMulti, feelMorph, feelMorphMin, feelMorphMax, grooveMulti,
			grooveMorph, grooveMorphMin, grooveMorphMax,
			modStart=0, modEnd=0, modReverse=0, modPitchShift=0, modFeelMorph=0, modGrooveMorph=0,
			modAttack=0, modRelease=0, modSeqBPM=0;

			var outEnv, originalBPM, outBPM, outRate, outFunction, outLoop, sStart, sEnd, rev,
			indexFunction, outIndex, att, rel, envCurve, feelIndex, feelIndexFunction, feelFunction, outFeel,
			grooveFunction, outGroove, grooveScaled,
			windowSize, overlaps, randomness, interpolation, freqScale;

			indexFunction = this.getSynthOption(0);
			outFunction = this.getSynthOption(1);
			windowSize =  this.getSynthOption(2);
			overlaps =  this.getSynthOption(3);
			interpolation =  this.getSynthOption(4);
			grooveFunction =  this.getSynthOption(5);
			envCurve =  this.getSynthOption(6).value;
			feelFunction =  this.getSynthOption(7);
			feelIndexFunction = this.getSynthOption(8);

			sStart = (start + modStart).max(0).min(1);
			sEnd = (end + modEnd).max(0).min(1);
			rev = (reverse + modReverse).max(0).min(1);
			att = (attack + modAttack).max(0).min(1).linlin(0, 1, attackMin, attackMax, nil);
			rel = (release + modRelease).max(0).min(1).linlin(0, 1, releaseMin, releaseMax, nil);
			// check loop type
			if (arrOptions[0] == 2, {
				outEnv = EnvGen.ar(
					Env.new([0, 1], [att], envCurve, nil),
					gate,
					doneAction: 0
				);
			}, {
				outEnv = EnvGen.ar(
					Env.new([0, 1, 1, 0], [att, 1, rel], envCurve, 2),
					gate,
					doneAction: 2
				);
			});

			originalBPM = 60  * loopTotalBeats / BufDur.kr(bufnumLoop);
			outBPM = (seqBPM + modSeqBPM).max(0).min(1).linlin(0, 1, seqBPMMin, seqBPMMax, nil);
			outRate =  (2 ** speedFactor) * (outBPM / originalBPM);

			outIndex = indexFunction.value(outRate, bufnumLoop, sStart, sEnd);
			outIndex = (outIndex * (1 - rev)) + (rev * (1 - outIndex));
			outGroove = grooveFunction.value(outIndex, bufnumGroove1, bufnumGroove2, grooveMulti,
				grooveMorph, modGrooveMorph, grooveMorphMin, grooveMorphMax);
			grooveScaled = outGroove.linlin(0, 1, sStart, sEnd);
			feelIndex = feelIndexFunction.value(outIndex, outGroove);
			outFeel = feelFunction.value(feelIndex, bufnumFeel1, bufnumFeel2, feelMulti,
				feelMorph, modFeelMorph, feelMorphMin, feelMorphMax);
			if (arrOptions[1] > 0, {
				freqScale = pitchShiftMin + ((pitchShiftMax - pitchShiftMin) * (pitchShift + modPitchShift).max(0).min(1));
				freqScale = 2 ** (freqScale/12);
			}, {
				freqScale = 1;
			});
			outLoop = outFunction.value(bufnumLoop, grooveScaled, freqScale, windowSize,
				overlaps, windowRandomness, interpolation) * outFeel.squared * level * 2;
			outLoop = LeakDC.ar(outLoop); // remove DC
			// amplitude is vel *  0.0.007874 approx. == 1 / 127
			// use TXClean to stop blowups
			Out.ar(out, TXClean.ar(outEnv * outLoop * (velocity * 0.007874)));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TestNoteVals"],
			["TXPopupActionPlusMinus", "Loop bank", {system.arrLoopBankNames},
				"bankNo",
				{ arg view; this.bankNo = view.value; this.loopNo = 0; this.loadLoop(0);
					this.setSynthArgSpec("loopNo", 0); system.showView;}
			],
			// array of loop filenames - beginning with blank loop  - only show mono files
			["TXPopupActionPlusMinus",
				"Loop", {["No Loop"]++system.loopMonoFileNames(bankNo, true)},
				"loopNo", { arg view; this.loopNo = view.value; this.loadLoop(view.value); }
			],
			["TXFraction", "Loop start", ControlSpec(0, 1), "start"],
			["TXFraction", "Loop end", ControlSpec(0, 1), "end"],
			["SynthOptionPopupPlusMinus", "Loop type", arrOptionData, 0, 350],
			["TXCheckBox", "Reverse", "reverse"],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
			["TXMinMaxSliderSplit", "Play BPM", ControlSpec(1, 900), "seqBPM", "seqBPMMin", "seqBPMMax"],
			["TXNumberPlusMinus", "Speed factor", ControlSpec(-5, 5, 'lin', 1, 0), "speedFactor"],
			["SynthOptionPopup", "Env Curve", arrOptionData, 6, 240,],
			["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax"],
			["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax"],
			["MIDIListenCheckBox"],
			["MIDIChannelSelector"],
			["MIDINoteSelector"],
			["MIDIVelSelector"],
			["SynthOptionPopupPlusMinus", "Time-stretch", arrOptionData, 1],
			["SynthOptionPopupPlusMinus", "Window size", arrOptionData, 2],
			["SynthOptionPopupPlusMinus", "Overlaps", arrOptionData, 3],
			["SynthOptionPopupPlusMinus", "Interpolation", arrOptionData, 4],
			["EZslider", "Randomness", ControlSpec(0, 1), "windowRandomness"],
			["TXMinMaxSliderSplit", "Pitch shift", ControlSpec(-48, 48), "pitchShift",
				"pitchShiftMin", "pitchShiftMax", nil, ],
			["SynthOptionPopupPlusMinus", "Feel Mode", arrOptionData, 7, 230],
			["EZslider", "Feel multi", ControlSpec(1, 32, step: 1), "feelMulti", nil, 340],
			["TXMinMaxSliderSplit", "Feel morph", ControlSpec(0, 1), "feelMorph", "feelMorphMin",
				"feelMorphMax"],
			["SynthOptionPopupPlusMinus", "Groove Mode", arrOptionData, 5, 230],
			["EZslider", "Groove multi", ControlSpec(1, 32, step: 1), "grooveMulti", nil, 340],
			["TXMinMaxSliderSplit", "Groove morph", ControlSpec(0, 1), "grooveMorph", "grooveMorphMin",
				"grooveMorphMax"],
		]);
		//	initialise buffer to linear curve
		arrFeel1Values = 0.5 ! 512;
		arrFeel2Values = 0.5 ! 512;
		arrGroove1Values = Array.newClear(512).seriesFill(0, 1/511);
		arrGroove2Values = Array.newClear(512).seriesFill(0, 1/511);
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
			this.bufferStore(0, arrFeel1Values);
			this.bufferStore(1, arrFeel2Values);
			this.bufferStore(2, arrGroove1Values);
			this.bufferStore(3, arrGroove2Values);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		//	initialise slots
		arrFeelSlotData = arrFeel1Values.dup(5);
		arrGrooveSlotData = arrGroove1Values.dup(5);
	} // end of method init

	buildGuiSpecArray {
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
		guiSpecArray = [
			["ActionButton", "Loop", {displayOption = "showLoop";
				this.buildGuiSpecArray; system.showView;}, 60,
			TXColor.white, this.getButtonColour(displayOption == "showLoop")],
			["Spacer", 3],
			["ActionButton", "Env, MIDI, Test", {displayOption = "showEnvMIDI";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showEnvMIDI")],
			["Spacer", 3],
			["ActionButton", "Time Stretch", {displayOption = "showTimeStretch";
				this.buildGuiSpecArray; system.showView;}, 90,
			TXColor.white, this.getButtonColour(displayOption == "showTimeStretch")],
			["Spacer", 3],
			["ActionButton", "Feel 1", {displayOption = "showFeel1";
				this.buildGuiSpecArray; system.showView;}, 64,
			TXColor.white, this.getButtonColour(displayOption == "showFeel1")],
			["Spacer", 3],
			["ActionButton", "Feel 2", {displayOption = "showFeel2";
				this.buildGuiSpecArray; system.showView;}, 64,
			TXColor.white, this.getButtonColour(displayOption == "showFeel2")],
			["Spacer", 3],
			["ActionButton", "Groove 1", {displayOption = "showGroove1";
				this.buildGuiSpecArray; system.showView;}, 70,
			TXColor.white, this.getButtonColour(displayOption == "showGroove1")],
			["Spacer", 3],
			["ActionButton", "Groove 2", {displayOption = "showGroove2";
				this.buildGuiSpecArray; system.showView;}, 70,
			TXColor.white, this.getButtonColour(displayOption == "showGroove2")],
			["Spacer", 3],
			["ActionButton", "Play",
				{this.createSynthNote(testMIDINote, testMIDIVel, testMIDITime);},
				60, TXColor.white, TXColor.sysGuiCol2],
			["Spacer", 3],
			["ActionButton", "Stop", {this.allNotesOff;},
				60, TXColor.white, TXColor.sysDeleteCol],
			["SpacerLine", 4],
		];
		if (displayOption == "showLoop", {
			guiSpecArray = guiSpecArray ++[
				["TXPopupActionPlusMinus", "Loop bank", {system.arrLoopBankNames},
					"bankNo",
					{ arg view; this.bankNo = view.value; this.loopNo = 0; this.loadLoop(0);
						this.setSynthArgSpec("loopNo", 0); system.showView;}
				],
				// array of loop filenames - beginning with blank loop  - only show mono files
				["TXListViewActionPlusMinus", "Mono loop",
					{["No Loop"]++system.loopMonoFileNames(bankNo, true)},
					"loopNo", { arg view;
						this.loopNo = view.value;
						this.loadLoop(view.value);
						{system.showView;}.defer(0.1);	//  refresh view
					}
				],
				["TextBarLeft", "Loopbank Settings: ", 140],
				["TextBarLeft", {"Original BPM = " ++ loopOriginalBPM.round(0.01).asString}, 140],
				["TextBarLeft", {"Total beats = " ++ loopTotalBeats.asString}, 140],
				["ActionButton", "Add Loops to Loop Bank", {TXBankBuilder2.addSampleDialog("Loop", bankNo)}, 200],
				["NextLine"],
				["ActionButton", "Show Waveform", {this.setWaveformVisible(1); system.showView;}, 90,
					TXColor.white, TXColor.sysGuiCol2],
				["ActionButton", "Hide Waveform", {this.setWaveformVisible(0); system.showView;}, 90,
					TXColor.white, TXColor.sysDeleteCol],
				["NextLine"],
				["TXSoundFileViewFraction", {loopFileName}, "start", "end", nil, {showWaveform},
					{sampleDataEvent}, nil, 650],
				["NextLine"],
				["SynthOptionPopupPlusMinus", "Loop type", arrOptionData, 0, 350],
				["Spacer", 10],
				["TXCheckBox", "Reverse", "reverse"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0, 1), "level"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Play BPM", ControlSpec(1, 900), "seqBPM", "seqBPMMin", "seqBPMMax"],
				["NextLine"],
				["TXNumberPlusMinus", "Speed factor", ControlSpec(-5, 5, 'lin', 1, 0), "speedFactor"],
			];
		});
		if (displayOption == "showEnvMIDI", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopup", "Env Curve", arrOptionData, 6, 240,],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax"],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax"],
				["DividingLine"],
				["SpacerLine", 10],
				["MIDIListenCheckBox"],
				["NextLine"],
				["MIDIChannelSelector"],
				["NextLine"],
				["MIDINoteSelector"],
				["NextLine"],
				["MIDIVelSelector"],
				["DividingLine"],
				["SpacerLine", 10],
				["TestLoopVals"],
			];
		});
		if (displayOption == "showTimeStretch", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Time-stretch", arrOptionData, 1],
				["SpacerLine", 6],
				["SynthOptionListPlusMinus", "Window size", arrOptionData, 2],
				["SpacerLine", 6],
				["SynthOptionListPlusMinus", "Overlaps", arrOptionData, 3],
				["SpacerLine", 6],
				["SynthOptionPopupPlusMinus", "Interpolation", arrOptionData, 4],
				["SpacerLine", 6],
				["EZslider", "Randomness", ControlSpec(0, 1), "windowRandomness"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Pitch shift", ControlSpec(-48, 48), "pitchShift",
					"pitchShiftMin", "pitchShiftMax", nil,
					[	["Range Presets: ", [-2, 2]], ["Range -1 to 1 semitones", [-1, 1]],
						["Range -2 to 2 semitones", [-2, 2]],
						["Range -7 to 7 semitones", [-7, 7]], ["Range -12 to 12 semitones", [-12, 12]],
						["Range -24 to 24 semitones", [-24, 24]], ["Range -48 to 48 semitones", [-48, 48]] ]
				],
			];
		});
		if (displayOption == "showFeel1", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Feel mode", arrOptionData, 7, 340],
				["Spacer", 10],
				["EZslider", "Feel multi", ControlSpec(1, 32, step: 1), "feelMulti", nil, 340],
				["SpacerLine", 6],
				["SynthOptionCheckBox", "Add Feel after Groove", arrOptionData, 8, 180],
				["Spacer", 10],
				["TXMinMaxSliderSplit", "Feel morph", ControlSpec(0, 1), "feelMorph", "feelMorphMin",
					"feelMorphMax", nil, nil, nil, nil, 500],
				["DividingLine"],
				["SpacerLine", 6],
				["ActionButton", "Show Waveform", {this.setWaveformVisible(1); system.showView;}, 100,
					TXColor.white, TXColor.sysGuiCol2],
				["ActionButton", "Hide", {this.setWaveformVisible(0); system.showView;}, 40,
					TXColor.white, TXColor.sysDeleteCol],
				["Spacer", 2],
				["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 128), "gridRows", {system.showView}, nil, nil, 40],
				["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 128), "gridCols", {system.showView}, nil, nil, 40],
				["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 182],
				["NextLine"],
				["TXCurveDraw", "Feel 1", {arrFeel1Values},
					{arg view; arrFeel1Values = view.value; arrFeelSlotData = view.arrSlotData;
						this.bufferStore(0, view.value);},
					{arrFeelSlotData}, "Curve", 518, 320, "Flat50", "gridRows", "gridCols",
					"position", "level", {
						feel1DataEvent.showSoundFile = showWaveform;
						if (showWaveform == 1, {
							feel1DataEvent.soundFileName = loopFileName;
							feel1DataEvent.soundFileStartPos = this.getSynthArgSpec("start");
							feel1DataEvent.soundFileEndPos = this.getSynthArgSpec("end");
						});
						feel1DataEvent;}
				],
			];
		});
		if (displayOption == "showFeel2", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Feel mode", arrOptionData, 7, 340],
				["Spacer", 10],
				["EZslider", "Feel multi", ControlSpec(1, 32, step: 1), "feelMulti", nil, 340],
				["SpacerLine", 6],
				["SynthOptionCheckBox", "Add Feel after Groove", arrOptionData, 8, 180],
				["Spacer", 10],
				["TXMinMaxSliderSplit", "Feel morph", ControlSpec(0, 1), "feelMorph", "feelMorphMin",
					"feelMorphMax", nil, nil, nil, nil, 500],
				["DividingLine"],
				["SpacerLine", 6],
				["ActionButton", "Show Waveform", {this.setWaveformVisible(1); system.showView;}, 100,
					TXColor.white, TXColor.sysGuiCol2],
				["ActionButton", "Hide", {this.setWaveformVisible(0); system.showView;}, 40,
					TXColor.white, TXColor.sysDeleteCol],
				["Spacer", 2],
				["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 128), "gridRows", {system.showView}, nil, nil, 40],
				["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 128), "gridCols", {system.showView}, nil, nil, 40],
				["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 182],
				["NextLine"],
				["TXCurveDraw", "Feel 2", {arrFeel2Values},
					{arg view; arrFeel2Values = view.value; arrFeelSlotData = view.arrSlotData;
						this.bufferStore(1, view.value);},
					{arrFeelSlotData}, "Curve", 518, 320, "Flat50", "gridRows", "gridCols",
					"position", "level", {
						feel2DataEvent.showSoundFile = showWaveform;
						if (showWaveform == 1, {
							feel2DataEvent.soundFileName = loopFileName;
							feel2DataEvent.soundFileStartPos = this.getSynthArgSpec("start");
							feel2DataEvent.soundFileEndPos = this.getSynthArgSpec("end");
						});
						feel2DataEvent;}],
			];
		});
		if (displayOption == "showGroove1", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Groove mode", arrOptionData, 5, 340],
				["Spacer", 10],
				["EZslider", "Groove multi", ControlSpec(1, 32, step: 1), "grooveMulti", nil, 340],
				["SpacerLine", 6],
				["SynthOptionCheckBox", "Add Feel after Groove", arrOptionData, 8, 180],
				["Spacer", 10],
				["TXMinMaxSliderSplit", "Groove morph", ControlSpec(0, 1), "grooveMorph", "grooveMorphMin",
					"grooveMorphMax", nil, nil, nil, nil, 500],
				["DividingLine"],
				["SpacerLine", 6],
				["ActionButton", "Show Waveform", {this.setWaveformVisible(1); system.showView;}, 100,
					TXColor.white, TXColor.sysGuiCol2],
				["ActionButton", "Hide", {this.setWaveformVisible(0); system.showView;}, 40,
					TXColor.white, TXColor.sysDeleteCol],
				["Spacer", 2],
				["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 128), "gridRows", {system.showView}, nil, nil, 40],
				["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 128), "gridCols", {system.showView}, nil, nil, 40],
				["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 182],
				["NextLine"],
				["TXCurveDraw", "Groove 1", {arrGroove1Values},
					{arg view; arrGroove1Values = view.value; arrGrooveSlotData = view.arrSlotData;
						this.bufferStore(2, view.value);},
					{arrGrooveSlotData}, "Warp", 518, 320, nil, "gridRows", "gridCols",
					"position", "time", {
						groove1DataEvent.showSoundFile = showWaveform;
						if (showWaveform == 1, {
							groove1DataEvent.soundFileName = loopFileName;
							groove1DataEvent.soundFileStartPos = this.getSynthArgSpec("start");
							groove1DataEvent.soundFileEndPos = this.getSynthArgSpec("end");
						});
						groove1DataEvent;}, true],
			];
		});
		if (displayOption == "showGroove2", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Groove mode", arrOptionData, 5, 340],
				["Spacer", 10],
				["EZslider", "Groove multi", ControlSpec(1, 32, step: 1), "grooveMulti", nil, 340],
				["SpacerLine", 6],
				["SynthOptionCheckBox", "Add Feel after Groove", arrOptionData, 8, 180],
				["Spacer", 10],
				["TXMinMaxSliderSplit", "Groove morph", ControlSpec(0, 1), "grooveMorph", "grooveMorphMin",
					"grooveMorphMax", nil, nil, nil, nil, 500],
				["DividingLine"],
				["SpacerLine", 6],
				["ActionButton", "Show Waveform", {this.setWaveformVisible(1); system.showView;}, 100,
					TXColor.white, TXColor.sysGuiCol2],
				["ActionButton", "Hide", {this.setWaveformVisible(0); system.showView;}, 40,
					TXColor.white, TXColor.sysDeleteCol],
				["Spacer", 2],
				["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 128), "gridRows", {system.showView}, nil, nil, 40],
				["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 128), "gridCols", {system.showView}, nil, nil, 40],
				["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 182],
				["NextLine"],
				["TXCurveDraw", "Groove 2", {arrGroove2Values},
					{arg view; arrGroove2Values = view.value; arrGrooveSlotData = view.arrSlotData;
						this.bufferStore(3, view.value);},
					{arrGrooveSlotData}, "Warp", 518, 320, nil, "gridRows", "gridCols",
					"position", "time", {
						groove2DataEvent.showSoundFile = showWaveform;
						if (showWaveform == 1, {
							groove2DataEvent.soundFileName = loopFileName;
							groove2DataEvent.soundFileStartPos = this.getSynthArgSpec("start");
							groove2DataEvent.soundFileEndPos = this.getSynthArgSpec("end");
						});
						groove2DataEvent;}, true],
			];
		});
	}  // end of buildGuiSpecArray

	getButtonColour { arg colour2Boolean;
		if (colour2Boolean == true, {
			^TXColor.sysGuiCol4;
		},{
			^TXColor.sysGuiCol1;
		});
	}


	bufferStore { arg argIndex, argArray;
		buffers.at(1 + argIndex).sendCollection(argArray);
	}

	extraSaveData { // override default method
		^[loopNo, loopFileName, loopNumChannels, loopTotalBeats, bankNo, arrFeel1Values, arrFeel2Values, arrFeelSlotData,
			arrGroove1Values, arrGroove2Values, arrGrooveSlotData];
	}

	loadExtraData {arg argData;  // override default method
		loopNo = argData.at(0);
		loopFileName = argData.at(1);
		// Convert path
		loopFileName = TXPath.convert(loopFileName);
		loopNumChannels = argData.at(2);
		loopTotalBeats = argData.at(3);
		bankNo = argData.at(4) ? 0;
		if (argData.at(5).notNil, {
			arrFeel1Values = argData.at(5);
			arrFeel2Values = argData.at(6);
			Routine.run {
				var holdModCondition;
				// add condition to load queue
				holdModCondition = system.holdLoadQueue.addCondition;
				// pause
				holdModCondition.wait;
				system.server.sync;
				// buffer store
				this.bufferStore(0, arrFeel1Values);
				this.bufferStore(1, arrFeel2Values);
				// remove condition from load queue
				system.holdLoadQueue.removeCondition(holdModCondition);
			};
		});
		if (argData.at(7).notNil, {
			arrFeelSlotData = argData.at(7);
		});
		if (argData.at(8).notNil, {
			arrGroove1Values = argData.at(8);
			arrGroove2Values = argData.at(9);
			Routine.run {
				var holdModCondition;
				// add condition to load queue
				holdModCondition = system.holdLoadQueue.addCondition;
				// pause
				holdModCondition.wait;
				system.server.sync;
				// buffer store
				this.bufferStore(2, arrGroove1Values);
				this.bufferStore(3, arrGroove2Values);
				// remove condition from load queue
				system.holdLoadQueue.removeCondition(holdModCondition);
			};
		});
		if (argData.at(10).notNil, {
			arrGrooveSlotData = argData.at(10);
		});
		this.loadLoop(loopNo);
	}

	clearBuffer {
		// clear the current buffer & filename
		buffers.at(0).zero;
		loopFileName = "";
		loopNumChannels = 0;
		loopTotalBeats = 1;
		loopOriginalBPM = 0;
		this.clearSampleData;
		// store Total Beats to synthArgSpecs
		this.setSynthArgSpec("loopTotalBeats", loopTotalBeats);
	}

	loadLoop { arg argIndex; // method to load loops into buffer
		var holdBuffer, holdLoopInd, holdModCondition, holdPath;
		Routine.run {
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			// pause
			system.server.sync;
			// adjust index
			holdLoopInd = (argIndex - 1).min(system.loopFilesMono(bankNo).size-1);
			// check for invalid samples
			if (argIndex == 0 or: {system.loopFilesMono(bankNo).at(holdLoopInd).at(3) == false}, {
				this.clearBuffer;
			},{
				// otherwise,  try to load loop.  if it fails, display error message and clear
				holdPath = system.loopFilesMono(bankNo).at(holdLoopInd).at(0);
				// Convert path
				holdPath = TXPath.convert(holdPath);
				if (File.exists(holdPath), {
					holdBuffer = Buffer.read(system.server, holdPath,
						action: { arg argBuffer;
							{
								//	if file loaded ok
								if (argBuffer.notNil, {
									this.setSynthArgSpec("bufnumLoop", argBuffer.bufnum);
									loopFileName = holdPath;
									loopNumChannels = argBuffer.numChannels;
									loopTotalBeats = system.loopFilesMono(bankNo).at(holdLoopInd).at(1);
									loopOriginalBPM =
									(60 * argBuffer.sampleRate * loopTotalBeats)/ argBuffer.numFrames;
									// store Total Beats to synthArgSpecs
									this.setSynthArgSpec("loopTotalBeats", loopTotalBeats);
									this.prepareSampleData(clearOldData: true);
								},{
									buffers.at(0).zero;
									loopFileName = "";
									loopNumChannels = 0;
									loopTotalBeats = 1;
									loopOriginalBPM = 0;
									// store Total Beats to synthArgSpecs
									this.setSynthArgSpec("loopTotalBeats", loopTotalBeats);
									TXInfoScreen.new("Invalid Loop File"
										++ holdPath);
								});
							}.defer;	// defer because gui process
						},
						// pass buffer number
						bufnum: buffers.at(0).bufnum
					);
				},{
					// if file not found, clear the current buffer & filename
					this.clearBuffer;
				});
			});
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		}; // end of Routine.run
	} // end of method loadLoop

	setWaveformVisible {arg isVisible;
		showWaveform = isVisible;
		this.prepareSampleData;
	}

	clearSampleData {
		sampleDataEvent = nil;
		// clear data in other events
		[feel1DataEvent, feel2DataEvent, groove1DataEvent, groove2DataEvent]
		.do({arg dataEvent, i;
			dataEvent.soundFileNumChannels = nil;
			dataEvent.soundFileSampleRate = nil;
			dataEvent.soundFileData = nil;
		});
	}

	prepareSampleData {arg clearOldData = false;
		var holdSoundFile, holdSFData;
		var soundFilePath = loopFileName;
		if (clearOldData, {
			this.clearSampleData;
		});
		if (showWaveform == 1 and: sampleDataEvent.isNil and: soundFilePath.notNil
			and: {File.existsCaseSensitive(soundFilePath)},
			{
				holdSoundFile = SoundFile.openRead(soundFilePath);
				if (holdSoundFile.notNil, {
					holdSFData = FloatArray.newClear(holdSoundFile.numFrames * holdSoundFile.numChannels);
					holdSoundFile.readData(holdSFData);
					sampleDataEvent = ();
					sampleDataEvent.soundFileNumChannels = holdSoundFile.numChannels;
					sampleDataEvent.soundFileSampleRate = holdSoundFile.sampleRate;
					sampleDataEvent.soundFileData = holdSFData;
					// copy data to other events
					[feel1DataEvent, feel2DataEvent, groove1DataEvent, groove2DataEvent]
					.do({arg dataEvent, i;
						dataEvent = dataEvent.merge(sampleDataEvent);
					});
				});
		});
	}

}

