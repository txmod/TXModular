// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXGranulator2 : TXModuleBase {

	classvar <>classData;

	var <>sampleNo = 0;
	var <>bankNo = 0;
	var sampleFileName = "";
	var showWaveform = 0;
	var sampleNumChannels = 0;
	var sampleFreq = 440;
	var displayOption;
	var arrCurveValues;
	var arrSlotData, curveDataEvent;
	var arrGridPresetNames, arrGridPresetActions;
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
			["Grain time ms", 1, "modDurTime", 0],
			["Grain density", 1, "modDensity", 0],
			["Grain pan", 1, "modGrainPan", 0],
			["Reverse play", 1, "modReverse", 0],
			["Pitch bend", 1, "modPitchbend", 0],
			["Vary position", 1, "modRandPos", 0],
			["Vary pitch", 1, "modRandPitch", 0],
			["Vary triggering", 1, "modRandTrig", 0],
			["Vary grain time", 1, "modRandDur", 0],
			["Vary panning", 1, "modRandPan", 0],
			["Delay", 1, "modDelay", 0],
			["Attack", 1, "modAttack", 0],
			["Decay", 1, "modDecay", 0],
			["Sustain level", 1, "modSustain", 0],
			["Sustain level 2", 1, "modSustain2", 0],
			["Sustain time", 1, "modSustainTime", 0],
			["Release", 1, "modRelease", 0],
			["Ext Trigger", 1, "extTrigger", nil],
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.arrBufferSpecs = [ ["bufnumSample", 2048, 1], ["bufnumGrainEnv", 128, 1]];
		classData.timeSpec = ControlSpec(0.01, 20);
		classData.guiWidth = 550;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		curveDataEvent = ();
		displayOption = "showSample";
		arrSynthArgSpecs = [
			["extTrigger", 0, 0],
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
			["bufnumGrainEnv", 0, \ir],
			["bankNo", 0, \ir],
			["sampleNo", 0, \ir],
			["sampleFreq", 440, \ir],
			["start", 0, defLagTime],
			["end", 1, defLagTime],
			["pos", 0, defLagTime],
			["randPos", 0.1, defLagTime],
			["randPitch", 0, defLagTime],
			["randTrig", 0.03, defLagTime],
			["randDur", 0.03, defLagTime],
			["randPan", 0.1, defLagTime],
			["durTime", ControlSpec(1, 500).unmap(50), defLagTime],
			["durTimeMin", 0, defLagTime],
			["durTimeMax", 500, defLagTime],
			["density", ControlSpec(0.1,10).unmap(1), defLagTime],
			["densityMin", 0.1, defLagTime],
			["densityMax", 10, defLagTime],
			["grainPan", 0.5, defLagTime],
			["grainPanMin", 0, defLagTime],
			["grainPanMax", 1, defLagTime],
			["reverse", 0, defLagTime],
			["level", 1, defLagTime],
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
			["modPos", 0, defLagTime],
			["modRandPos", 0, defLagTime],
			["modRandPitch", 0, defLagTime],
			["modRandTrig", 0, defLagTime],
			["modRandDur", 0, defLagTime],
			["modRandPan", 0, defLagTime],
			["modDurTime", 0, defLagTime],
			["modDensity", 0, defLagTime],
			["modGrainPan", 0, defLagTime],
			["modReverse", 0, defLagTime],
			["modPitchbend", 0, defLagTime],
			["modDelay", 0, \ir],
			["modAttack", 0, \ir],
			["modDecay", 0, \ir],
			["modSustain", 0, \ir],
			["modSustain2", 0, \ir],
			["modSustainTime", 0, \ir],
			["modRelease", 0, \ir],
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
		arrOptions = [0,0,0,0,0,0,3,0];
		arrOptionData = [
			[// 0 - first Option not used now.
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
			// 1 - Intonation
			TXIntonation.arrOptionData,
			// 2
			TXEnvLookup.arrDADSSRSlopeOptionData,
			// 3
			TXEnvLookup.arrDADSSRSustainOptionData,
			 // 4 - amp compensation
			TXAmpComp.arrOptionData,
			[// 5 - Grain envelope
				// GrainBuf.ar (numChannels: 1, trigger: 0, dur: 1, sndbuf, rate: 1, pos: 0, interp: 2, pan: 0,
				// envbufnum: -1, maxGrains: 512, mul: 1, add: 0)
				["Custom envelope (default)", {arg outTrig, outDur, bufnumSample, outRate, outPos,
						outPan, bufnumGrainEnv, maxGrains, level;
					GrainBuf.ar (2, outTrig, outDur, bufnumSample, outRate, outPos, 4, outPan, bufnumGrainEnv,
						maxGrains, level);
				}],
				["Built in Hann envelope (custom env ignored)", {arg outTrig, outDur, bufnumSample, outRate, outPos,
						outPan, bufnumGrainEnv, maxGrains, level;
					GrainBuf.ar (2, outTrig, outDur, bufnumSample, outRate, outPos, 4, outPan, -1,
						maxGrains, level);
				}],
			],
			[// 6 - Maximum grains
				["8", 8],
				["16", 16],
				["32", 32],
				["64 (default)", 64],
				["128", 128],
				["256", 256],
				["512", 512],
			],
			[// 7 - Triggering
				["Internal (default), uses grain time,  density & vary triggering", 0],
				["External, uses side-chain control signal", 1],
			],
		];
		synthDefFunc = {
			arg extTrigger = 0, out, gate, note, velocity, keytrack, transpose, pitchbend, pitchbendMin, pitchbendMax,
			bufnumSample, bufnumGrainEnv, bankNo, sampleNo, sampleFreq, start, end,
			pos, randPos, randPitch, randTrig, randDur, randPan, durTime, durTimeMin, durTimeMax,
			density, densityMin, densityMax, grainPan, grainPanMin, grainPanMax, reverse, level,
			envtime=0, delay, attack, attackMin, attackMax, decay, decayMin, decayMax, sustain, sustain2,
			sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax, intKey,
			modStart=0, modEnd=0, modPos=0, modRandPos=0, modRandPitch=0, modRandTrig=0, modRandDur=0, modRandPan=0,
			modDurTime=0, modDensity=0, modGrainPan=0, modReverse=0, modPitchbend=0,
			modDelay=0, modAttack=0, modDecay=0, modSustain=0, modSustain2=0, modSustainTime=0, modRelease=0;

			var outEnv, envFunction, outFreq, outFreqPb, intonationFunc, pbend, outRate, outSample,
			envCurve, sStart, sEnd, rev, del, att, dec, sus, sus2, sustime, rel;
			var trigFunction, trigRate, outTrig, outPos, sDurTime, outDur, outPan, sPos, sRandPos, sRandPitch, sRandPan,
			sRandTrig, sRandDur, sTotalPos, outDensity, ampCompFunction, outAmpComp, grainFunc, maxGrains;

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
			sDurTime = (durTimeMin + ((durTimeMax - durTimeMin) * (durTime + modDurTime))).max(1).min(20000) / 1000;
			sRandDur = (randDur + modRandDur).max(0).min(1);
			outDur = sDurTime * (2 ** (sRandDur * WhiteNoise.kr.range(-2,2)));
			sRandPan = (randPan + modRandPan).max(0).min(1);
			outPan = (sRandPan * WhiteNoise.kr.unipolar) + ((1 - sRandPan) * grainPan).linlin(0, 1, grainPanMin, grainPanMax);

			if (arrOptions[7] == 0, {
				// internal triggering
				outDensity = (density + modDensity).max(0).min(1).linlin(0, 1, densityMin, densityMax);
				sRandTrig = (randTrig + modRandTrig).max(0).min(1);
				trigRate = outDur.reciprocal * outDensity;
				outTrig = GaussTrig.ar(trigRate, sRandTrig * 0.99);
			}, {
				// external triggering
				outTrig = extTrigger;
			});

			grainFunc = this.getSynthOption(5);
			maxGrains = this.getSynthOption(6);
			outSample = grainFunc.value(outTrig, outDur, bufnumSample, outRate, outPos, outPan.madd(2, -1),
				bufnumGrainEnv, maxGrains, level);
			ampCompFunction = this.getSynthOption(4);
			outAmpComp = ampCompFunction.value(outFreq);
			// amplitude is vel / 127
			Out.ar(out, TXClean.ar(outEnv * outSample * outAmpComp * (velocity * 0.007874)));
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
			["EZslider", "Play position", ControlSpec(0, 1), "pos"],
			["EZslider", "Vary position", ControlSpec(0, 1), "randPos"],
			["SynthOptionPopupPlusMinus", "Max grains", arrOptionData, 6],
			["SynthOptionPopupPlusMinus", "Triggering", arrOptionData, 7],
			["TXMinMaxSliderSplit", "Grain time ms", ControlSpec(1, 20000), "durTime", "durTimeMin",  "durTimeMax"],
			["TXMinMaxSliderSplit", "Grain density", ControlSpec(0.1, 32), "density", "densityMin", "densityMax"],
			["EZslider", "Vary pitch", ControlSpec(0, 1), "randPitch"],
			["EZslider", "Vary triggering", ControlSpec(0, 1), "randTrig"],
			["EZslider", "Vary grain time", ControlSpec(0, 1), "randDur"],
			["EZslider", "Vary panning", ControlSpec(0, 1), "randPan"],
			["SynthOptionPopupPlusMinus", "Grain Env", arrOptionData, 5],
			["TXMinMaxSliderSplit", "Grain pan", ControlSpec(0, 1), "grainPan", "grainPanMin", "grainPanMax"],
			["EZslider", "Level", ControlSpec(0, 2), "level", nil, 300],
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
			["TXEnvPlot", {this.envViewValues;}, {arg view; envView = view;}],
			["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax",{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decay", "decayMin", "decayMax",{{this.updateEnvView;}.defer;}],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView;}.defer;}],
			["EZslider", "Sustain level 2", ControlSpec(0, 1), "sustain2", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTime", "sustainTimeMin",
				"sustainTimeMax",{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax",{{this.updateEnvView;}.defer;}],
			["SynthOptionPopup", "Curve", arrOptionData, 2, 240, {system.showView;}],
			["SynthOptionPopup", "Env Type", arrOptionData, 3, 300],
			["SynthOptionPopupPlusMinus", "Intonation", arrOptionData, 1, nil,
				{arg view; this.updateIntString(view.value)}],
			["TXStaticText", "Note ratios",
				{TXIntonation.arrScalesText.at(arrOptions.at(1));},
				{arg view; ratioView = view}],
			["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F",
				"F#", "G", "G#", "A", "A#", "B"], "intKey", nil, 140],
		]);
		//	initialise buffer
		arrCurveValues = Signal.sineFill(128, [1.0],[1.5pi]).collect({arg item, i; (item.value + 1) * 0.5;});
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
			this.bufferStore(arrCurveValues);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		//	initialise slots to linear curves
		arrSlotData = arrCurveValues.dup(5);
	} // end of method init

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Sample", {displayOption = "showSample";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showSample")],
			["Spacer", 3],
			["ActionButton", "Grain", {displayOption = "showGrain";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showGrain")],
			["Spacer", 3],
			["ActionButton", "Grain Env", {displayOption = "showGrainEnv";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showGrainEnv")],
			["Spacer", 3],
			["ActionButton", "MIDI/ Note", {displayOption = "showMIDI";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
			["Spacer", 3],
			["ActionButton", "Main Env", {displayOption = "showEnv";
				this.buildGuiSpecArray; system.showView;}, 80,
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
				["SynthOptionPopupPlusMinus", "Max grains", arrOptionData, 6, 240],
				["SpacerLine", 6],
				["SynthOptionPopupPlusMinus", "Triggering", arrOptionData, 7],
				["SpacerLine", 2],
				["EZslider", "Vary triggering", ControlSpec(0, 1), "randTrig"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Grain density", ControlSpec(0.1, 32), "density", "densityMin", "densityMax"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Grain time ms", ControlSpec(1, 20000), "durTime", "durTimeMin",  "durTimeMax"],
				["SpacerLine", 2],
				["EZslider", "Vary grain time", ControlSpec(0, 1), "randDur"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Grain pan", ControlSpec(0, 1), "grainPan", "grainPanMin", "grainPanMax", nil,
					[["Pan Range Presets:", []], ["Full Stereo", [0, 1]], ["Half Stereo", [0.25, 0.75]], ["Mono", [0.5, 0.5]],
						["Left Half", [0, 0.5]], ["Left Bias", [0, 0.75]], ["Right Bias", [0.25, 1]], ["Right Half", [0.5, 1]]]
				],
				["SpacerLine", 2],
				["EZslider", "Vary panning", ControlSpec(0, 1), "randPan"],
				["SpacerLine", 6],
				["EZslider", "Vary pitch", ControlSpec(0, 1), "randPitch"],
				["NextLine"],
			];
		});
		if (displayOption == "showGrainEnv", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Grain Env", arrOptionData, 5],
				["SpacerLine", 6],
				["TXCurveDraw", "Custom env", {arrCurveValues},
					{arg view; arrCurveValues = view.value; arrSlotData = view.arrSlotData;
						this.bufferStore(view.value);},
					{arrSlotData}, "Warp", nil, nil, nil, "gridRows", "gridCols", nil, nil, curveDataEvent],
				["SpacerLine", 2],
				["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 128), "gridRows", {system.showView}],
				["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 128), "gridCols", {system.showView}],
				["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 182],
				["ActionButton", "Quantise to grid", {this.quantiseToGrid(quantizeRows: true, quantizeCols: true)}, 94],
				["ActionButton", "Quantise rows", {this.quantiseToGrid(quantizeRows: true, quantizeCols: false)}, 88],
				["ActionButton", "Quantise columns", {this.quantiseToGrid(quantizeRows: false, quantizeCols: true)}, 102],
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
				["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax",{{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decay", "decayMin", "decayMax",{{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["EZslider", "Sustain level 2", ControlSpec(0, 1), "sustain2", {{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTime", "sustainTimeMin",
					"sustainTimeMax",{{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax",{{this.updateEnvView;}.defer;}],
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

	runMirror {
		var holdArray, startVal, midArray, endVal, holdSignal;
		holdArray = arrCurveValues.deepCopy;
		startVal = holdArray.first.asArray;
		midArray = holdArray.drop(2).drop(-2);
		midArray = midArray.clump(2).collect({arg item, i; item.sum/2;});
		endVal = holdArray.last.asArray;
		holdArray = startVal ++ midArray ++ endVal;
		holdArray = holdArray ++ holdArray.copy.reverse;
		holdSignal = Signal.newFrom(holdArray);
		arrCurveValues = Array.newFrom(holdSignal);
		this.bufferStore(arrCurveValues);
		system.showView;
	}

	runMirrorInvert {
		var holdArray, startVal, midArray, endVal, holdSignal;
		holdArray = arrCurveValues.deepCopy;
		startVal = holdArray.first.asArray;
		midArray = holdArray.drop(2).drop(-2);
		midArray = midArray.clump(2).collect({arg item, i; item.sum/2;});
		endVal = holdArray.last.asArray;
		holdArray = startVal ++ midArray ++ endVal;
		holdArray = (holdArray ++ holdArray.copy.reverse.neg).normalize(0,1);
		holdSignal = Signal.newFrom(holdArray);
		arrCurveValues = Array.newFrom(holdSignal);
		this.bufferStore(arrCurveValues);
		system.showView;
	}

	quantiseToGrid {arg quantizeRows = true, quantizeCols = true;
		var holdArray, holdSignal, outArray, holdCols;
		var rows, cols;
		var maxVal = 128;
		holdArray = Array.newClear(maxVal);
		rows = this.getSynthArgSpec("gridRows");
		cols = this.getSynthArgSpec("gridCols");

		if (quantizeCols, {
			cols.do({arg item;
				var jump, startRange, endRange, meanVal;
				jump = cols.reciprocal;
				startRange = (item * jump * maxVal).round(1);
				endRange = ((item + 1) * jump * maxVal).round(1) - 1;
				meanVal = arrCurveValues.copyRange(startRange.asInteger, endRange.asInteger).mean;
				if (quantizeRows, {
					meanVal = meanVal.round(rows.reciprocal);
				});
				holdArray[startRange.asInteger..endRange.asInteger] = meanVal;
			});
		},{
			holdArray = arrCurveValues.collect({arg item;
				var outVal = item;
				if (quantizeRows, {
					outVal = outVal.round(rows.reciprocal);
				});
				outVal;
			});
		});
		holdSignal = Signal.newFrom(holdArray);
		arrCurveValues = Array.newFrom(holdSignal);
		this.bufferStore(arrCurveValues);
		system.showView;
	}

	bufferStore { arg argArray;
		buffers.at(1).sendCollection(argArray);
	}

	extraSaveData { // override default method
		^[sampleNo, sampleFileName, sampleNumChannels, sampleFreq, testMIDINote, testMIDIVel, testMIDITime, bankNo,
			arrCurveValues, arrSlotData,
		];
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
		arrCurveValues = argData.at(8);
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// buffer store
			this.bufferStore(arrCurveValues);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		arrSlotData = argData.at(9);
		// amend older data
		if (system.dataBank.savedSystemRevision < 1004, {
			this.setSynthValue("sustain2", this.getSynthArgSpec("sustain"));
		});
	}

}

