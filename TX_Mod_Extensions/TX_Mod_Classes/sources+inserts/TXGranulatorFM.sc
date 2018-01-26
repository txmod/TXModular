// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXGranulatorFM : TXModuleBase {

	classvar <>classData;

	var <>sampleNo = 0;
	var <>bankNo = 0;
	var sampleFileName = "";
	var showWaveform = 0;
	var sampleNumChannels = 0;
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
		classData.defaultName = "Granulator FM";
		classData.moduleRate = "audio";
		classData.moduleType = "groupsource";
		classData.arrCtlSCInBusSpecs = [
			["Carrier ratio", 1, "modCarrierRatio", 0],
			["Carrier freq", 1, "modCarrierFreq", 0],
			["Modulator ratio", 1, "modModulatorRatio", 0],
			["Modulator freq", 1, "modModulatorFreq", 0],
			["Modulator index", 1, "modModulatorIndex", 0],
			["Grain time ms", 1, "modDurTime", 0],
			["Grain density", 1, "modDensity", 0],
			["Grain pan", 1, "modGrainPan", 0],
			["Pitch bend", 1, "modPitchbend", 0],
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
		classData.arrBufferSpecs = [ ["bufnumGrainEnv", 128, 1]];
		classData.timeSpec = ControlSpec(0.01, 20);
		classData.guiWidth = 550;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		curveDataEvent = ();
		displayOption = "showFM";
		arrSynthArgSpecs = [
			["extTrigger", 0, 0],
			["out", 0, 0],
			["gate", 1, 0],
			["note", 0, 0],
			["velocity", 0, 0],
			["keytrack", 1, 0],
			["transpose", 0, 0],
			["pitchbend", 0.5, defLagTime],
			["pitchbendMin", -2, defLagTime],
			["pitchbendMax", 2, defLagTime],

			["bufnumGrainEnv", 0, \ir],

			["carrierFreq", 0.5, defLagTime],
			["carrierFreqMin", 0.midicps, defLagTime],
			["carrierFreqMax", 127.midicps, defLagTime],
			["carrierRatio", ControlSpec(0.01, 10).unmap(1), defLagTime],
			["carrierRatioMin", 0.01, defLagTime],
			["carrierRatioMax", 10, defLagTime],
			["modulatorFreq", 0.5, defLagTime],
			["modulatorFreqMin", 0.01, defLagTime],
			["modulatorFreqMax", 127.midicps, defLagTime],
			["modulatorRatio", ControlSpec(0.01, 10).unmap(1), defLagTime],
			["modulatorRatioMin", 0.01, defLagTime],
			["modulatorRatioMax", 10, defLagTime],
			["modulatorIndex", 0.5, defLagTime],
			["modulatorIndexMin", 0, defLagTime],
			["modulatorIndexMax", 10, defLagTime],

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

			["modCarrierFreq", 0, defLagTime],
			["modCarrierRatio", 0, defLagTime],
			["modModulatorFreq", 0, defLagTime],
			["modModulatorRatio", 0, defLagTime],
			["modModulatorIndex", 0, defLagTime],
			["modRandTrig", 0, defLagTime],
			["modRandDur", 0, defLagTime],
			["modRandPan", 0, defLagTime],
			["modDurTime", 0, defLagTime],
			["modDensity", 0, defLagTime],
			["modGrainPan", 0, defLagTime],
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
		arrOptions = [0,0,0,0,0,0,3,0,0,0];
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
				// GrainFM.ar (numChannels: 1, trigger: 0, dur: 1, carfreq: 440, modfreq: 200, index: 1,
				//      pan: 0, envbufnum: -1, maxGrains: 512, mul: 1, add: 0)
				["Custom envelope (default)", {arg outTrig, outDur, carfreq, modfreq, fmIndex,
						outPan, bufnumGrainEnv, maxGrains, level;
					GrainFM.ar (2, outTrig, outDur, carfreq, modfreq, fmIndex, outPan, bufnumGrainEnv,
						maxGrains, level);
				}],
				["Built in Hann envelope (custom env ignored)", {arg outTrig, outDur, carfreq, modfreq, fmIndex,
						outPan, bufnumGrainEnv, maxGrains, level;
					GrainFM.ar (2, outTrig, outDur, carfreq, modfreq, fmIndex, outPan, -1,
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
			[// 7 - Carrier mode
				["Use Carrier ratio multiplied by the MIDI note played",
					{arg carrierFreq, modCarrierFreq, carrierFreqMin, carrierFreqMax,
						carrierRatio, modCarrierRatio, carrierRatioMin, carrierRatioMax, midiNoteFreqPb;
						var carRatio = (carrierRatio + modCarrierRatio).max(0).min(1).linlin(0, 1, carrierRatioMin, carrierRatioMax);
						carRatio * midiNoteFreqPb;}
				],
				["Use Carrier freq",
					{arg carrierFreq, modCarrierFreq, carrierFreqMin, carrierFreqMax,
						carrierRatio, modCarrierRatio, carrierRatioMin, carrierRatioMax, midiNoteFreqPb;
						var carFreq = (carrierFreq + modCarrierFreq).max(0).min(1).linexp(0, 1, carrierFreqMin, carrierFreqMax);
						carFreq;}
				],
			],
			[// 8 - Modulator mode
				["Use Modulator ratio multiplied by the MIDI note played",
					{arg modulatorFreq, modModulatorFreq, modulatorFreqMin, modulatorFreqMax,
						modulatorRatio, modModulatorRatio, modulatorRatioMin, modulatorRatioMax, midiNoteFreqPb;
						var modRatio = (modulatorRatio + modModulatorRatio).max(0).min(1).linlin(0, 1, modulatorRatioMin, modulatorRatioMax);
						modRatio * midiNoteFreqPb;}
				],
				["Use Modulator freq",
					{arg modulatorFreq, modModulatorFreq, modulatorFreqMin, modulatorFreqMax,
						modulatorRatio, modModulatorRatio, modulatorRatioMin, modulatorRatioMax, midiNoteFreqPb;
						var modFreq = (modulatorFreq + modModulatorFreq).max(0).min(1).linexp(0, 1, modulatorFreqMin, modulatorFreqMax);
						modFreq;}
				],
			],
			[// 9 - Triggering
				["Internal (default), uses grain time,  density & vary triggering", 0],
				["External, uses side-chain control signal", 1],
			],
		];
		synthDefFunc = {
			arg extTrigger = 0, out, gate, note, velocity, keytrack, transpose, pitchbend, pitchbendMin, pitchbendMax, bufnumGrainEnv,
			carrierFreq, carrierFreqMin, carrierFreqMax, carrierRatio, carrierRatioMin, carrierRatioMax,
			modulatorFreq, modulatorFreqMin, modulatorFreqMax, modulatorRatio, modulatorRatioMin, modulatorRatioMax,
			modulatorIndex, modulatorIndexMin, modulatorIndexMax,
			randTrig, randDur, randPan, durTime, durTimeMin, durTimeMax,
			density, densityMin, densityMax, grainPan, grainPanMin, grainPanMax, level,
			envtime=0, delay, attack, attackMin, attackMax, decay, decayMin, decayMax,
			sustain, sustain2, sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax, intKey,
			modCarrierFreq=0, modCarrierRatio=0, modModulatorFreq=0, modModulatorRatio=0, modModulatorIndex=0,
			modRandTrig=0, modRandDur=0, modRandPan=0,
			modDurTime=0, modDensity=0, modGrainPan=0, modPitchbend=0,
			modDelay=0, modAttack=0, modDecay=0, modSustain=0, modSustain2=0, modSustainTime=0, modRelease=0;

			var outEnv, envFunction, midiNoteFreq, midiNoteFreqPb, intonationFunc, pbend,
			carrierModeFunc, modulatorModeFunc, envCurve, del, att, dec, sus, sus2, sustime, rel;
			var trigFunction, trigRate, outTrig, sDurTime, outDur,
			outCarFreq, outModFreq, fmIndex, outFM,
			outPan, sRandPan, sRandTrig, sRandDur, outDensity, ampCompFunction, outAmpComp, grainFunc, maxGrains;

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
			pbend = pitchbendMin + ((pitchbendMax - pitchbendMin) * (pitchbend + modPitchbend).max(0).min(1));
			midiNoteFreq = ((intonationFunc.value((note + transpose), intKey) * keytrack)
				+ ((48 + transpose).midicps * (1-keytrack)));
			midiNoteFreqPb = midiNoteFreq * (2 ** (pbend /12));
			sDurTime = (durTimeMin + ((durTimeMax - durTimeMin) * (durTime + modDurTime))).max(1).min(20000) / 1000;
			sRandDur = (randDur + modRandDur).max(0).min(1);
			outDur = sDurTime * (2 ** (sRandDur * WhiteNoise.kr.range(-2,2)));
			sRandPan = (randPan + modRandPan).max(0).min(1);
			outPan = (sRandPan * WhiteNoise.kr.unipolar) + ((1 - sRandPan) * grainPan).linlin(0, 1, grainPanMin, grainPanMax);

			if (arrOptions[9] == 0, {
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
			carrierModeFunc = this.getSynthOption(7);
			modulatorModeFunc = this.getSynthOption(8);
			outCarFreq = carrierModeFunc.value(carrierFreq, modCarrierFreq, carrierFreqMin, carrierFreqMax,
				carrierRatio, modCarrierRatio, carrierRatioMin, carrierRatioMax, midiNoteFreqPb);
			outModFreq = modulatorModeFunc.value(modulatorFreq, modModulatorFreq, modulatorFreqMin, modulatorFreqMax,
				modulatorRatio, modModulatorRatio, modulatorRatioMin, modulatorRatioMax, midiNoteFreqPb);
			fmIndex = (modulatorIndex + modModulatorIndex).max(0).min(1).linlin(0, 1, modulatorIndexMin, modulatorIndexMax);
			outFM = grainFunc.value(outTrig, outDur, outCarFreq, outModFreq, fmIndex, outPan.madd(2, -1),
				bufnumGrainEnv, maxGrains, level);
			ampCompFunction = this.getSynthOption(4);
			outAmpComp = ampCompFunction.value(midiNoteFreq);
			// amplitude is vel / 127
			Out.ar(out, TXClean.ar(outEnv * LeakDC.ar(outFM) * outAmpComp * (velocity * 0.007874)));
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
			["SynthOptionPopup", "Carrier mode", arrOptionData, 7],
			["TXMinMaxFreqNoteSldr", "Carrier freq", ControlSpec(0.midicps, 127.midicps, \exponential),
				"carrierFreq", "carrierFreqMin", "carrierFreqMax", nil, TXFreqRangePresets.arrFreqRanges],
			["TXMinMaxSliderSplit", "Carrier ratio", ControlSpec(0.01, 10), "carrierRatio", "carrierRatioMin", "carrierRatioMax"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 6],
			["SynthOptionPopup", "Modulator mode", arrOptionData, 8],
			["TXMinMaxFreqNoteSldr", "Modulator freq", ControlSpec(0.01, 127.midicps, \exponential),
				"modulatorFreq", "modulatorFreqMin", "modulatorFreqMax", nil, TXFreqRangePresets.arrFreqRanges],
			["TXMinMaxSliderSplit", "Modulator ratio", ControlSpec(0.01, 10), "modulatorRatio", "modulatorRatioMin", "modulatorRatioMax"],
			["TXMinMaxSliderSplit", "Modulator index", ControlSpec(0, 10), "modulatorIndex", "modulatorIndexMin", "modulatorIndexMax"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 6],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 4],
			["SynthOptionPopupPlusMinus", "Max grains", arrOptionData, 6],
			["SynthOptionPopupPlusMinus", "Triggering", arrOptionData, 9],
			["TXMinMaxSliderSplit", "Grain time ms", ControlSpec(1, 20000), "durTime", "durTimeMin",  "durTimeMax"],
			["TXMinMaxSliderSplit", "Grain density", ControlSpec(0.1, 32), "density", "densityMin", "densityMax"],
			["EZslider", "Vary triggering", ControlSpec(0, 1), "randTrig"],
			["EZslider", "Vary grain time", ControlSpec(0, 1), "randDur"],
			["EZslider", "Vary panning", ControlSpec(0, 1), "randPan"],
			["SynthOptionPopupPlusMinus", "Grain Env", arrOptionData, 5],
			["TXMinMaxSliderSplit", "Grain pan", ControlSpec(0, 1), "grainPan", "grainPanMin", "grainPanMax"],
			["EZslider", "Level", ControlSpec(0, 2), "level", nil, 300],
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
			["ActionButton", "FM", {displayOption = "showFM";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showFM")],
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
		if (displayOption == "showFM", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Carrier mode", arrOptionData, 7],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Carrier ratio", ControlSpec(0.01, 10), "carrierRatio", "carrierRatioMin", "carrierRatioMax"],
				["SpacerLine", 2],
				["TXMinMaxFreqNoteSldr", "Carrier freq", ControlSpec(0.midicps, 20000, \exponential),
					"carrierFreq", "carrierFreqMin", "carrierFreqMax", nil, TXFreqRangePresets.arrFreqRanges],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Modul. mode", arrOptionData, 8],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Modul. ratio", ControlSpec(0.01, 10), "modulatorRatio", "modulatorRatioMin", "modulatorRatioMax"],
				["SpacerLine", 2],
				["TXMinMaxFreqNoteSldr", "Modul. freq", ControlSpec(0.01, 20000, \exponential),
					"modulatorFreq", "modulatorFreqMin", "modulatorFreqMax", nil, TXFreqRangePresets.arrFreqRanges],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Modul. index", ControlSpec(0, 10), "modulatorIndex", "modulatorIndexMin", "modulatorIndexMax"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0, 1), "level"],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 4],
			];
		});
		if (displayOption == "showGrain", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Max grains", arrOptionData, 6, 240],
				["SpacerLine", 6],
				["SynthOptionPopupPlusMinus", "Triggering", arrOptionData, 9],
				["SpacerLine", 6],
				["EZslider", "Vary triggering", ControlSpec(0, 1), "randTrig"],
				["SpacerLine", 2],
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

	updateIntString{arg argIndex;
		if (ratioView.notNil, {
			if (ratioView.notClosed, {
				ratioView.string = TXIntonation.arrScalesText.at(argIndex);
			});
		});
	}

	envViewValues {
		var attack, attackMin, attackMax, decay, decayMin, decayMax, sustain, sustain2;
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
		buffers.at(0).sendCollection(argArray);
	}

	extraSaveData { // override default method
		^[testMIDINote, testMIDIVel, testMIDITime, arrCurveValues, arrSlotData];
	}

	loadExtraData {arg argData;  // override default method
		testMIDINote = argData.at(0);
		testMIDIVel = argData.at(1);
		testMIDITime = argData.at(2);
		arrCurveValues = argData.at(3);
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
		arrSlotData = argData.at(4);
		// amend older data
		if (system.dataBank.savedSystemRevision < 1004, {
			this.setSynthValue("sustain2", this.getSynthArgSpec("sustain"));
		});
	}

}

