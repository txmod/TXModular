// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXLoopPlayer : TXModuleBase {

	classvar <>classData;

	var <>loopNo = 0;
	var <>bankNo = 0;
	var loopFileName = "";
	var showWaveform = 0;
	var loopNumChannels = 0;
	var loopTotalBeats = 1;
	var loopOriginalBPM = 0;
	var displayOption;
	var <>testMIDINote = 69;
	var <>testMIDIVel = 127;
	var <>testMIDITime = 8;

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
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrBufferSpecs = [ ["bufnumLoop", 2048, 1] ];
	} // end of method initClass

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showLoop";
		arrSynthArgSpecs = [
			["out", 0, 0],
			["gate", 1, 0],
			["note", 0, 0],
			["velocity", 0, 0],
			["seqBPM", 0.3979933, \ir], 		// default BPM is set for 120 bpm
			["seqBPMMin", 1, \ir],
			["seqBPMMax", 300, \ir],
			["speedFactor", 0, \ir],
			["bufnumLoop", 0, \ir],
			["bankNo", 0, \ir],
			["loopNo", 0, \ir],
			["loopTotalBeats", 1, \ir],
			["start", 0, defLagTime],
			["end", 1, defLagTime],
			["reverse", 0, defLagTime],
			["level", 0.5, \ir],
			["envtime", 0, \ir],
			["modStart", 0, defLagTime],
			["modEnd", 0, defLagTime],
			["modReverse", 0, defLagTime],
			["windowRandomness", 0, defLagTime],
		];
		// create looping option
		arrOptions = [0, 0, 0, 0, 0];
		arrOptionData = [
			[ // loop type indexing function
				["Looped",
					{arg outRate, bufnumLoop, start, end;
						Phasor.ar(0, outRate * BufRateScale.kr(bufnumLoop),
							start * BufFrames.kr(bufnumLoop), end * BufFrames.kr(bufnumLoop));
					}
				],
				["Single shot",
					{arg outRate, bufnumLoop, start, end;
						(Sweep.ar(1, outRate * BufSampleRate.kr(bufnumLoop))
							+ (((start * outRate.sign.max(0)) + (end * outRate.sign.neg.max(0)))
								* BufFrames.kr(bufnumLoop))
						)
						.min(end * BufFrames.kr(bufnumLoop))
						.max(start * BufFrames.kr(bufnumLoop));
					}
				]
			],
			[// Time-stretching
				["Off", {arg bufnumLoop, bufIndex;
					BufRd.ar(1, bufnumLoop, bufIndex, 0);
				}],
				["On", {arg bufnumLoop, bufIndex, freqScale, windowSize, overlaps, randomness, interpolation;
					Warp1.ar(1, bufnumLoop, bufIndex/BufFrames.kr(bufnumLoop), freqScale, windowSize, -1, overlaps,
						randomness, interpolation);
				}],
			],
			[// Window size
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
			],
			[// Overlaps
				["1", 1],
				["2", 2],
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
			[// Interpolation
				["Linear", 2],
				["Cubic", 4],
			],
		];
		synthDefFunc = {
			arg out, gate, note, velocity, seqBPM, seqBPMMin, seqBPMMax, speedFactor, bufnumLoop,
			bankNo, loopNo, loopTotalBeats, start, end, reverse, level,
			envtime=0, modStart=0, modEnd=0, modReverse=0, windowRandomness;
			var outEnv, originalBPM, outBPM, outRate, outFunction, outLoop, sStart, sEnd, rev, indexFunction, outIndex,
			windowSize, overlaps, randomness, interpolation;

			sStart = (start + modStart).max(0).min(1);
			sEnd = (end + modEnd).max(0).min(1);
			rev = (reverse + modReverse).max(0).min(1);
			outEnv = EnvGen.ar(
				Env.new([0,1,1,0],[0.01,1,0.01],'linear', 2),
				gate,
				doneAction: 2
			);

			originalBPM = 60  * loopTotalBeats / BufDur.kr(bufnumLoop);
			outBPM =  seqBPMMin + (seqBPM * (seqBPMMax - seqBPMMin));
			outRate =  (2 ** speedFactor) * (outBPM / originalBPM) * (rev-0.5).neg.sign;
			indexFunction = this.getSynthOption(0);
			outIndex = indexFunction.value(outRate, bufnumLoop, sStart, sEnd);
			outFunction = this.getSynthOption(1);
			windowSize =  this.getSynthOption(2);
			overlaps =  this.getSynthOption(3);
			interpolation =  this.getSynthOption(4);
			outLoop = outFunction.value(bufnumLoop, outIndex, 1, windowSize,
				overlaps, windowRandomness, interpolation) * level * 2;
			// amplitude is vel *  0.00315 approx. == 1 / 127
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
			["Spacer", 80],
			["NextLine"],
			["TXFraction", "Loop start", ControlSpec(0, 1), "start"],
			["TXFraction", "Loop end", ControlSpec(0, 1), "end"],
			["SynthOptionPopupPlusMinus", "Loop type", arrOptionData, 0, 260],
			["TXCheckBox", "Reverse", "reverse"],
			["DividingLine"],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
			["DividingLine"],
			["TXMinMaxSliderSplit", "Play BPM", ControlSpec(1, 900), "seqBPM", "seqBPMMin", "seqBPMMax"],
			["TXNumberPlusMinus", "Speed factor", ControlSpec(-5, 5, 'lin', 1, 0), "speedFactor"],
			["DividingLine"],
			["MIDIListenCheckBox"],
			["NextLine"],
			["MIDIChannelSelector"],
			["NextLine"],
			["MIDINoteSelector"],
			["NextLine"],
			["MIDIVelSelector"],
			["DividingLine"],
		]);

		//	use base class initialise
		this.baseInit(this, argInstName);
		this.midiNoteInit;
		//	make buffers, load the synthdef and create the Group for synths to belong to
		this.makeBuffersAndGroup(classData.arrBufferSpecs);
	} // end of method init

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Loop", {displayOption = "showLoop";
				this.buildGuiSpecArray; system.showView;}, 70,
			TXColor.white, this.getButtonColour(displayOption == "showLoop")],
			["Spacer", 3],
			["ActionButton", "MIDI/ Test", {displayOption = "showMIDI";
				this.buildGuiSpecArray; system.showView;}, 90,
			TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
			["Spacer", 3],
			["ActionButton", "Time Stretch", {displayOption = "showTimeStretch";
				this.buildGuiSpecArray; system.showView;}, 90,
			TXColor.white, this.getButtonColour(displayOption == "showTimeStretch")],
			["Spacer", 13],
			["ActionButton", "Play test loop",
				{this.createSynthNote(testMIDINote, testMIDIVel, testMIDITime);},
				90, TXColor.white, TXColor.sysGuiCol2],
			["Spacer", 3],
			["ActionButton", "Stop test loop", {this.allNotesOff;},
				90, TXColor.white, TXColor.sysDeleteCol],
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
				["ActionButton", "Show", {showWaveform = 1; system.showView;}, 80, TXColor.white, TXColor.sysGuiCol2],
				["ActionButton", "Hide", {showWaveform = 0; system.showView;}, 80, TXColor.white, TXColor.sysDeleteCol],
				["NextLine"],
				["TXSoundFileViewFraction", {loopFileName}, "start", "end", nil, {showWaveform}],
				["SynthOptionPopupPlusMinus", "Loop type", arrOptionData, 0, 260],
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
		if (displayOption == "showMIDI", {
			guiSpecArray = guiSpecArray ++[
				["MIDIListenCheckBox"],
				["NextLine"],
				["MIDIChannelSelector"],
				["NextLine"],
				["MIDINoteSelector"],
				["NextLine"],
				["MIDIVelSelector"],
				["SpacerLine", 4],
				["TestLoopVals"],
			];
		});
		if (displayOption == "showTimeStretch", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionCheckBox", "Use Granular Time-Stretching", arrOptionData, 1, 300],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Window size", arrOptionData, 2],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Overlaps", arrOptionData, 3],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Interpolation", arrOptionData, 4],
				["SpacerLine", 4],
				["EZslider", "Randomness", ControlSpec(0, 1), "windowRandomness"],
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
		^[loopNo, loopFileName, loopNumChannels, loopTotalBeats, bankNo];
	}

	loadExtraData {arg argData;  // override default method
		loopNo = argData.at(0);
		loopFileName = argData.at(1);
		// Convert path
		loopFileName = TXPath.convert(loopFileName);
		loopNumChannels = argData.at(2);
		loopTotalBeats = argData.at(3);
		bankNo = argData.at(4) ? 0;
		this.loadLoop(loopNo);
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
				// if argIndex is 0, clear the current buffer & filename
				buffers.at(0).zero;
				loopFileName = "";
				loopNumChannels = 0;
				loopTotalBeats = 1;
				loopOriginalBPM = 0;
				// store Total Beats to synthArgSpecs
				this.setSynthArgSpec("loopTotalBeats", loopTotalBeats);
			},{
				// otherwise,  try to load loop.  if it fails, display error message and clear
				holdPath = system.loopFilesMono(bankNo).at(holdLoopInd).at(0);
				// Convert path
				holdPath = TXPath.convert(holdPath);
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
			});
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		}; // end of Routine.run
	} // end of method loadLoop

}

