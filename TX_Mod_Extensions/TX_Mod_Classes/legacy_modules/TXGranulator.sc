// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXGranulator : TXModuleBase {

	classvar <>classData;

	var <>sampleNo = 0;
	var <>bankNo = 0;
	var sampleFileName = "";
	var showWaveform = 0;
	var sampleNumChannels = 0;
	var sampleFreq = 440;
	var displayOption;
	var ratioView;
	var envView;
	var <>testMIDINote = 69;
	var <>testMIDIVel = 100;
	var <>testMIDITime = 1;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Granulator";
		classData.moduleRate = "audio";
		classData.moduleType = "groupsource";
		classData.arrCtlSCInBusSpecs = [
			["Sample start", 1, "modStart", 0],
			["Sample end", 1, "modEnd", 0],
			["Play position", 1, "modPos", 0],
			["Vary position", 1, "modRandPos", 0],
			["Vary pitch", 1, "modRandPitch", 0],
			["Vary timing", 1, "modRandTime", 0],
			["Grain time ms", 1, "modDurTime", 0],
			["Grain density", 1, "modDensity", 0],
			["Grain pan L", 1, "modGrainPanL", 0],
			["Grain pan R", 1, "modGrainPanR", 0],
			["Reverse play", 1, "modReverse", 0],
			["Pitch bend", 1, "modPitchbend", 0],
			["Delay", 1, "modDelay", 0],
			["Attack", 1, "modAttack", 0],
			["Decay", 1, "modDecay", 0],
			["Sustain level", 1, "modSustain", 0],
			["Sustain time", 1, "modSustainTime", 0],
			["Release", 1, "modRelease", 0],
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.arrBufferSpecs = [ ["bufnumSample", 2048,1] ];
		classData.timeSpec = ControlSpec(0.01, 20);
		classData.maxWavetableSize = 65536;
		classData.guiWidth = 550;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showSample";
		arrSynthArgSpecs = [
			["out", 0, 0],
			["gate", 1, 0],
			["note", 0, 0],
			["velocity", 0, 0],
			["keytrack", 0, \ir],
			["transpose", 0, \ir],
			["pitchbend", 0.5, defLagTime],
			["pitchbendMin", -2, defLagTime],
			["pitchbendMax", 2, defLagTime],
			["bufnumSample", 0, \ir],
			["bankNo", 0, \ir],
			["sampleNo", 0, \ir],
			["sampleFreq", 440, \ir],
			["start", 0, defLagTime],
			["end", 1, defLagTime],
			["pos", 0, defLagTime],
			["randPos", 0, defLagTime],
			["randPitch", 0, defLagTime],
			["randTime", 0, defLagTime],
			["durTime", 0.2, defLagTime],
			["durTimeMin", 0, defLagTime],
			["durTimeMax", 500, defLagTime],
			["density", 0.2, defLagTime],
			["densityMin", 0.1, defLagTime],
			["densityMax", 10, defLagTime],
			["grainPanL", 0, defLagTime],
			["grainPanR", 1, defLagTime],
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
			["intKey", 0, \ir],
			["modStart", 0, defLagTime],
			["modEnd", 0, defLagTime],
			["modPos", 0, defLagTime],
			["modRandPos", 0, defLagTime],
			["modRandPitch", 0, defLagTime],
			["modRandTime", 0, defLagTime],
			["modDurTime", 0, defLagTime],
			["modDensity", 0, defLagTime],
			["modGrainPanL", 0, defLagTime],
			["modGrainPanR", 0, defLagTime],
			["modReverse", 0, defLagTime],
			["modPitchbend", 0, defLagTime],
			["modDelay", 0, \ir],
			["modAttack", 0, \ir],
			["modDecay", 0, \ir],
			["modSustain", 0, \ir],
			["modSustainTime", 0, \ir],
			["modRelease", 0, \ir],
		];
		// create looping option
		arrOptions = [0,0,0,0,0];
		arrOptionData = [
			[// first Option not used now.
				["Regular",
					{arg trigRate;
						Impulse.kr(trigRate);
					}
				],
				["Random",
					{arg trigRate;
						Dust.kr(trigRate);
					}
				],
			],
			// Intonation
			TXIntonation.arrOptionData,
			TXEnvLookup.arrSlopeOptionData,
			TXEnvLookup.arrDADSRSustainOptionData,
			 // amp compensation
			TXAmpComp.arrOptionData,
		];
		synthDefFunc = {
			arg out, gate, note, velocity, keytrack, transpose, pitchbend, pitchbendMin, pitchbendMax,
			bufnumSample, bankNo, sampleNo, sampleFreq, start, end, pos, randPos, randPitch, randTime,
			durTime, durTimeMin, durTimeMax,
			density, densityMin, densityMax, grainPanL, grainPanR, reverse, level,
			envtime=0, delay, attack, attackMin, attackMax, decay, decayMin, decayMax, sustain,
			sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax, intKey,
			modStart=0, modEnd=0, modPos=0, modRandPos=0, modRandPitch=0, modRandTime=0, modDurTime=0,
			modDensity=0, modGrainPanL=0, modGrainPanR=0, modReverse=0, modPitchbend=0,
			modDelay=0, modAttack=0, modDecay=0, modSustain=0, modSustainTime=0, modRelease=0;

			var outEnv, envFunction, outFreq, outFreqPb, intonationFunc, pbend, outRate, outSample,
			envCurve, sStart, sEnd, rev, del, att, dec, sus, sustime, rel;
			var trigFunction, trigRate, outTrig, outPos, outDur, outPan, sPos, sRandPos, sRandPitch, sRandTime, sTotalPos,
			outDensity, outGrainPanL, outGrainPanR, ampCompFunction, outAmpComp;

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
			intonationFunc = this.getSynthOption(1);
			outFreq = (intonationFunc.value((note + transpose), intKey) * keytrack)
			+ ((sampleFreq.cpsmidi + transpose).midicps * (1-keytrack));
			pbend = pitchbendMin + ((pitchbendMax - pitchbendMin) * (pitchbend + modPitchbend).max(0).min(1));
			outFreqPb = outFreq *  (2 ** (pbend /12));
			sRandPitch = (randPitch + modRandPitch).max(0).min(1);
			outRate = (outFreqPb + WhiteNoise.kr(sRandPitch.squared * outFreqPb * 2) / sampleFreq) * (rev-0.5).neg.sign;
			sStart = (start + modStart).max(0).min(1);
			sEnd = (end + modEnd).max(0).min(1);
			sPos = (pos + modPos).max(0).min(1);
			sRandPos = (randPos + modRandPos).max(0).min(1);
			sTotalPos = (sPos + WhiteNoise.kr(sRandPos)).wrap(0, 1);
			outPos = (sStart + (sTotalPos * (sEnd - sStart))).abs * BufDur.kr(bufnumSample);
			outDur = (durTimeMin + ((durTimeMax - durTimeMin) * (durTime + modDurTime))).max(1).min(20000) / 1000;
			outGrainPanL = (grainPanL + modGrainPanL).max(0).min(1);
			outGrainPanR = (grainPanR + modGrainPanR).max(0).min(1);
			outPan = WhiteNoise.kr(outGrainPanR - outGrainPanL, outGrainPanL);
			outDensity = (densityMin + ((densityMax - densityMin) * (density + modDensity))).max(0.1).min(10);
			sRandTime = (randTime + modRandTime).max(0).min(1);
			trigRate = outDur.reciprocal * LFNoise1.kr(10, sRandTime, 1) * outDensity;
			outTrig = Impulse.kr(trigRate);
			outSample = TGrains.ar(2, outTrig, bufnumSample, outRate, outPos, outDur, outPan.madd(2,-1),  1, 2) * level;
			ampCompFunction = this.getSynthOption(4);
			outAmpComp = ampCompFunction.value(outFreq);
			// amplitude is vel *  0.00315 approx. == 1 / 127
			// use TXClean to stop blowups
			Out.ar(out, TXClean.ar(outEnv * outSample * outAmpComp * (velocity * 0.007874)));
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
			// array of sample filenames - beginning with blank sample  - only show mono files
			["TXListViewActionPlusMinus", "Mono sample", {["No Sample"]++system.sampleMonoFileNames(bankNo, true)},
				"sampleNo", { arg view; this.sampleNo = view.value; this.loadSample(view.value); }
			],
			["TXRangeSlider", "Play Range", ControlSpec(0, 1), "start", "end"],
			["EZslider", "Play position", ControlSpec(0, 1), "pos"],
			["EZslider", "Vary position", ControlSpec(0, 1), "randPos"],
			["TXMinMaxSliderSplit", "Grain time ms", ControlSpec(1, 20000), "durTime", "durTimeMin",  "durTimeMax"],
			["TXMinMaxSliderSplit", "Grain density", ControlSpec(0.1, 10), "density", "densityMin", "densityMax"],
			["EZslider", "Vary pitch", ControlSpec(0, 1), "randPitch"],
			["EZslider", "Vary timing", ControlSpec(0, 1), "randTime"],
			["TXRangeSlider", "Pan range", ControlSpec(0, 1), "grainPanL", "grainPanR"],
			["EZslider", "Level", ControlSpec(0, 1), "level", nil, 300],
			["TXCheckBox", "Reverse play", "reverse", 120],
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
			["TXEnvDisplay", {this.envViewValues;}, {arg view; envView = view;}],
			["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax",{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decay", "decayMin", "decayMax",{{this.updateEnvView;}.defer;}],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTime", "sustainTimeMin",
				"sustainTimeMax",{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax",{{this.updateEnvView;}.defer;}],
			["SynthOptionPopup", "Curve", arrOptionData, 2, 200, {system.showView;}],
			["SynthOptionPopup", "Env Type", arrOptionData, 3, 300],
			["SynthOptionPopupPlusMinus", "Intonation", arrOptionData, 1, nil,
				{arg view; this.updateIntString(view.value)}],
			["TXStaticText", "Note ratios",
				{TXIntonation.arrScalesText.at(arrOptions.at(1));},
				{arg view; ratioView = view}],
			["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F",
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
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showSample")],
			["Spacer", 3],
			["ActionButton", "Grain", {displayOption = "showGrain";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showGrain")],
			["Spacer", 3],
			["ActionButton", "MIDI/ Note", {displayOption = "showMIDI";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
			["Spacer", 3],
			["ActionButton", "Envelope", {displayOption = "showEnv";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showEnv")],
			["DividingLine"],
			["SpacerLine", 1],
		];
		if (displayOption == "showSample", {
			guiSpecArray = guiSpecArray ++[
				["TXPopupActionPlusMinus", "Sample bank", {system.arrSampleBankNames},
					"bankNo",
					{ arg view; this.bankNo = view.value; this.sampleNo = 0; this.loadSample(0);
						this.setSynthArgSpec("sampleNo", 0); system.showView;}
				],
				// array of sample filenames - beginning with blank sample  - only show mono files
				["TXListViewActionPlusMinus", "Mono sample",
					{["No Sample"]++system.sampleMonoFileNames(bankNo, true)},
					"sampleNo", { arg view;
						this.sampleNo = view.value;
						this.loadSample(view.value);
						{system.showView;}.defer(0.1);   //  refresh view
					}
				],
				["ActionButton", "Add Samples to Sample Bank", {TXBankBuilder2.addSampleDialog("Sample", bankNo)}, 180],
				["ActionButton", "Show", {showWaveform = 1; system.showView;},
					80, TXColor.white, TXColor.sysGuiCol2],
				["ActionButton", "Hide", {showWaveform = 0; system.showView;}, 80, TXColor.white, TXColor.sysDeleteCol],
				["TXSoundFileViewRange", {sampleFileName}, "start", "end", nil, {showWaveform}, 100],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Play position", ControlSpec(0, 1), "pos"],
				["SpacerLine", 6],
				["EZslider", "Vary position", ControlSpec(0, 1), "randPos"],
				["SpacerLine", 6],
				["TXCheckBox", "Reverse play", "reverse", nil, 140],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Level", ControlSpec(0, 1), "level"],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 4],
			];
		});
		if (displayOption == "showGrain", {
			guiSpecArray = guiSpecArray ++[
				["TXMinMaxSliderSplit", "Grain time ms", ControlSpec(1, 20000), "durTime", "durTimeMin",  "durTimeMax"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Grain density", ControlSpec(0.1, 10), "density", "densityMin", "densityMax"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Vary pitch", ControlSpec(0, 1), "randPitch"],
				["SpacerLine", 6],
				["EZslider", "Vary timing", ControlSpec(0, 1), "randTime"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXRangeSlider", "Pan range", ControlSpec(0, 1), "grainPanL", "grainPanR", nil,
					[["Pan Range Presets:", []], ["Full Stereo", [0, 1]], ["Half Stereo", [0.25, 0.75]], ["Mono", [0.5, 0.5]],
						["Left Half", [0, 0.5]], ["Left Bias", [0, 0.75]], ["Right Bias", [0.25, 1]], ["Right Half", [0.5, 1]]]
				],
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
				["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax",{{this.updateEnvView;}.defer;}],
				["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decay", "decayMin", "decayMax",{{this.updateEnvView;}.defer;}],
				["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView;}.defer;}],
				["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTime", "sustainTimeMin",
					"sustainTimeMax",{{this.updateEnvView;}.defer;}],
				["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax",{{this.updateEnvView;}.defer;}],
				["NextLine"],
				["SynthOptionPopup", "Curve", arrOptionData, 2, 200, {system.showView;}],
				["NextLine"],
				["SynthOptionPopup", "Env Type", arrOptionData, 3, 300],
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
		var holdBuffer, holdSampleInd, holdModCondition, holdPath;
		Routine.run {
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			// pause
			system.server.sync;

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
							},{
								buffers.at(0).zero;
								sampleFileName = "";
								sampleNumChannels = 0;
								sampleFreq = 440;
								// store Freq to synthArgSpecs
								this.setSynthArgSpec("sampleFreq", sampleFreq);
								TXInfoScreen.new("Invalid Sample File"
									++ system.sampleFilesMono(bankNo).at(holdSampleInd).at(0));
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


}

