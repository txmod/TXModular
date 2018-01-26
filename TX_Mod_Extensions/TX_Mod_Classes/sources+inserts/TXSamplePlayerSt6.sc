// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSamplePlayerSt6 : TXModuleBase {

	// Note: TXSamplePlayerSt6 is different to TXSamplePlayer5a because it doesn't have the extra loop type "Single-Waveform"

	classvar <>classData;

	var	timeSpec;
	var <>sampleNo = 0;
	var <>bankNo = 0;
	var <>sampleData;
	var sampleFileName = "";
	var showWaveform = 0;
	var sampleNumChannels = 0;
	var sampleFreq = 440;
	var displayOption;
	var ratioView;
	var	envView;
	var <>testMIDINote = 69;
	var <>testMIDIVel = 100;
	var <>testMIDITime = 1;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Sample Player St";
		classData.moduleRate = "audio";
		classData.moduleType = "groupsource";
		classData.arrCtlSCInBusSpecs = [
			["Sample Start", 1, "modStart", 0],
			["Sample End", 1, "modEnd", 0],
			["Sample Reverse", 1, "modReverse", 0],
			["Pitch bend", 1, "modPitchbend", 0],
			["Delay", 1, "modDelay", 0],
			["Attack", 1, "modAttack", 0],
			["Decay", 1, "modDecay", 0],
			["Sustain level", 1, "modSustain", 0],
			["Sustain level 2", 1, "modSustain2", 0],
			["Sustain time", 1, "modSustainTime", 0],
			["Release", 1, "modRelease", 0],
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
		timeSpec = ControlSpec(0.01, 20);
		displayOption = "showSample";
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
			["attack", 0.01, \ir],
			["attackMin", 0.01, \ir],
			["attackMax", 5, \ir],
			["decay", 0.05, \ir],
			["decayMin", 0.01, \ir],
			["decayMax", 5, \ir],
			["sustain", 1, \ir],
			["sustain2", 1, \ir],
			["sustainTime", 0.2, \ir],
			["sustainTimeMin", 0.01, \ir],
			["sustainTimeMax", 5, \ir],
			["release", 0.01, \ir],
			["releaseMin", 0.01, \ir],
			["releaseMax", 5, \ir],
			["intKey", 0, \ir],
			["modStart", 0, defLagTime],
			["modEnd", 0, defLagTime],
			["modReverse", 0, defLagTime],
			["modPitchbend", 0, defLagTime],
			["modDelay", 0, \ir],
			["modAttack", 0, \ir],
			["modDecay", 0, \ir],
			["modSustain", 0, \ir],
			["modSustain2", 0, \ir],
			["modSustainTime", 0, \ir],
			["modRelease", 0, \ir],
		];
		// create options
		arrOptions = [0,0,0,0,0];
		arrOptionData = [
			[
				["Single shot",
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
				],
			],
			// Intonation
			TXIntonation.arrOptionData,
			TXEnvLookup.arrDADSSRSlopeOptionData,
			TXEnvLookup.arrDADSSRSustainOptionData,
			 // amp compensation
			TXAmpComp.arrOptionData,
		];
		synthDefFunc = {
			arg out, gate, note, velocity, keytrack, transpose, pitchbend, pitchbendMin, pitchbendMax,
			bufnumSampleStereo, bufnumSampleMono, sampleIsStereo,
			bankNo, sampleNo, sampleFreq, start, end, reverse, level,
			envtime=0, delay, attack, attackMin, attackMax, decay, decayMin, decayMax, sustain, sustain2,
			sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax, intKey,
			modStart=0, modEnd=0, modReverse=0, modPitchbend=0, modDelay=0, modAttack=0, modDecay=0,
			modSustain=0, modSustain2=0, modSustainTime=0, modRelease=0;
			var outEnv, envFunction, outFreq, intonationFunc, pbend, outRate, outFunction, outSample,
			envCurve, sStart, sEnd, rev, del, att, dec, sus, sus2, sustime, rel, ampCompFunction, outAmpComp;

			sStart = (start + modStart).max(0).min(1);
			sEnd = (end + modEnd).max(0).min(1);
			rev = (reverse + modReverse).max(0).min(1);
			del = (delay + modDelay).max(0).min(1);
			att = (attackMin + ((attackMax - attackMin) * (attack + modAttack))).max(0.01).min(20);
			dec = (decayMin + ((decayMax - decayMin) * (decay + modDecay))).max(0.01).min(20);
			sus = (sustain + modSustain).max(0).min(1);
			sus2 = (sustain2 + modSustain2).max(0).min(1);
			sustime = (sustainTimeMin +
				((sustainTimeMax - sustainTimeMin) * (sustainTime + modSustainTime))).max(0.01).min(20);
			rel = (releaseMin + ((releaseMax - releaseMin) * (release + modRelease))).max(0.01).min(20);
			envCurve = this.getSynthOption(2);
			envFunction = this.getSynthOption(3);
			outEnv = EnvGen.ar(
				envFunction.value(del, att, dec, sus, sus2, sustime, rel, envCurve),
				gate,
				doneAction: 2
			);
			intonationFunc = this.getSynthOption(1);
			outFreq = (intonationFunc.value((note + transpose), intKey) * keytrack) + ((sampleFreq.cpsmidi
				+ transpose).midicps * (1-keytrack));
			pbend = pitchbendMin + ((pitchbendMax - pitchbendMin) * (pitchbend + modPitchbend).max(0).min(1));
			outRate = ((outFreq *  (2 ** (pbend /12))) / sampleFreq) * (rev-0.5).neg.sign;
			outFunction = this.getSynthOption(0);
			outSample = outFunction.value(outRate, bufnumSampleStereo, bufnumSampleMono, sStart, sEnd) * level * 2;
			// amplitude is vel *  0.00315 approx. == 1 / 127
			// use TXClean to stop blowups
			ampCompFunction = this.getSynthOption(4);
			outAmpComp = ampCompFunction.value(outFreq);

			Out.ar(out, TXClean.ar(outEnv * outSample * outAmpComp * (velocity * 0.007874)));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TestNoteVals"],
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
			["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 4],
			["MIDIListenCheckBox"],
			["MIDIChannelSelector"],
			["MIDINoteSelector"],
			["MIDIVelSelector"],
			["TXCheckBox", "Keyboard tracking", "keytrack"],
			["Transpose"],
			["TXMinMaxSliderSplit", "Pitch bend",
				ControlSpec(-48, 48), "pitchbend", "pitchbendMin", "pitchbendMax"],
			["PolyphonySelector"],
			["TXEnvPlot", {this.envViewValues;}, {arg view; envView = view;}],
			["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Attack", timeSpec, "attack", "attackMin", "attackMax",
				{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Decay", timeSpec, "decay", "decayMin", "decayMax",
				{{this.updateEnvView;}.defer;}],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView;}.defer;}],
			["EZslider", "Sustain level 2", ControlSpec(0, 1), "sustain2", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime", "sustainTimeMin",
				"sustainTimeMax",{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Release", timeSpec, "release", "releaseMin", "releaseMax",
				{{this.updateEnvView;}.defer;}],
			["SynthOptionPopup", "Curve", arrOptionData, 2, 240, {system.showView;}],
			["SynthOptionPopup", "Env Type", arrOptionData, 3, 300],
			["SynthOptionPopup", "Intonation", arrOptionData, 1, nil,
				{arg view; this.updateIntString(view.value)}],
			["TXStaticText", "Note ratios",
				{TXIntonation.arrScalesText.at(arrOptions.at(1));},
				{arg view; ratioView = view}],
			["TXPopupActionPlusMinus", "Key / root", ["C", "C#", "D", "D#", "E","F",
				"F#", "G", "G#", "A", "A#", "B"], "intKey", nil, 140],
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
			["ActionButton", "Envelope", {displayOption = "showEnv";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showEnv")],
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
				["ActionButton", "Show", {showWaveform = 1; system.showView;},
					80, TXColor.white, TXColor.sysGuiCol2],
				["ActionButton", "Hide", {showWaveform = 0; system.showView;  this.sampleData_(nil);},
					80, TXColor.white, TXColor.sysDeleteCol],
				["NextLine"],
				["TXSoundFileViewRange", {sampleFileName}, "start", "end", nil, {showWaveform}],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Loop type", arrOptionData, 0, 260],
				["SpacerLine", 4],
				["TXCheckBox", "Reverse", "reverse"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0, 1), "level"],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 4],
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
				["TXMinMaxSliderSplit", "Attack", timeSpec, "attack", "attackMin", "attackMax",
					{{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Decay", timeSpec, "decay", "decayMin", "decayMax",
					{{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["EZslider", "Sustain level 2", ControlSpec(0, 1), "sustain2", {{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime", "sustainTimeMin",
					"sustainTimeMax",{{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Release", timeSpec, "release", "releaseMin", "releaseMax",
					{{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["SynthOptionPopup", "Curve", arrOptionData, 2, 240, {system.showView;}],
				["SpacerLine", 2],
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
					5, 60, nil, 36, {arg note; this.releaseSynthGate(note);}],
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
		// amend older data
		if (system.dataBank.savedSystemRevision < 1004, {
			this.setSynthValue("sustain2", this.getSynthArgSpec("sustain"));
		});
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

	envViewValues {
		var attack, attackMin, attackMax, decay, decayMin, decayMax, sustain;
		var sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax;
		var del, att, dec, sus, sus2, sustime, rel, envCurve;

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
		sus2 = this.getSynthArgSpec("sustain2");
		sustainTime = this.getSynthArgSpec("sustainTime");
		sustainTimeMin = this.getSynthArgSpec("sustainTimeMin");
		sustainTimeMax = this.getSynthArgSpec("sustainTimeMax");
		sustime = sustainTimeMin + ((sustainTimeMax - sustainTimeMin) * sustainTime);
		release = this.getSynthArgSpec("release");
		releaseMin = this.getSynthArgSpec("releaseMin");
		releaseMax = this.getSynthArgSpec("releaseMax");
		rel = releaseMin + ((releaseMax - releaseMin) * release);
		envCurve = this.getSynthOption(2);
		^[[0, 0, 1, sus, sus2, 0].max(0.001), [del, att, dec, sustime, rel], envCurve];
	}

	updateEnvView {
		if (envView.respondsTo('notClosed'), {
			if (envView.notClosed, {
				envView.value = this.envViewValues;
			});
		});
	}

}

