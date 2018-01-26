// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXWaveSynth8 : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var ratioView;
	var	envView;
	var <>testMIDINote = 69;
	var <>testMIDIVel = 100;
	var <>testMIDITime = 1;
	var	arrEnvPresetNames, arrEnvPresetActions;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Wave Synth";
		classData.moduleRate = "audio";
		classData.moduleType = "groupsource";
		classData.arrAudSCInBusSpecs = [
			["FM input", 1, "inmodulator"],
		];
		classData.arrCtlSCInBusSpecs = [
			["Modify 1", 1, "modModify1", 0],
			["Modify 2", 1, "modModify2", 0],
			["Pitch bend", 1, "modPitchbend", 0],
			["Delay", 1, "modDelay", 0],
			["Attack", 1, "modAttack", 0],
			["Decay", 1, "modDecay", 0],
			["Sustain level", 1, "modSustain", 0],
			["Sustain level 2", 1, "modSustain2", 0],
			["Sustain time", 1, "modSustainTime", 0],
			["Release", 1, "modRelease", 0],
			["FM depth", 1, "modFMDepth", 0],
			["FM freq", 1, "modFMFreq", 0],
			["FM ratio", 1, "modFMRatio", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.timeSpec = ControlSpec(0.01, 20);
		classData.fmFreqSpec = ControlSpec(0.01, 20000, \exponential);
		classData.fmRatioSpec = ControlSpec(0.01, 10);
		classData.fmDepthSpec = ControlSpec(0, 4);
		classData.guiWidth = 540;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showWaveform";
		arrSynthArgSpecs = [
			["inmodulator", 0, 0],
			["out", 0, 0],
			["gate", 1, 0],
			["note", 0, 0],
			["velocity", 0, 0],
			["keytrack", 1, \ir],
			["transpose", 0, \ir],
			["pitchbend", 0.5, defLagTime],
			["pitchbendMin", -2, defLagTime],
			["pitchbendMax", 2, defLagTime],
			["modify1", 0.5, defLagTime],
			["modify1Min", 0, defLagTime],
			["modify1Max", 1, defLagTime],
			["modify2", 0.5, defLagTime],
			["modify2Min", 0, defLagTime],
			["modify2Max", 1, defLagTime],
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
			["sustainTimeMin", 0, \ir],
			["sustainTimeMax", 5, \ir],
			["release", 0.01, \ir],
			["releaseMin", 0.01, \ir],
			["releaseMax", 5, \ir],
			["intKey", 0, \ir],
			["fmDepth", ControlSpec(0, 4).unmap(1), defLagTime],
			["fmDepthMin", 0, defLagTime],
			["fmDepthMax", 4, defLagTime],
			["fmFreq", 0.5, defLagTime],
			["fmFreqMin", 0.midicps, defLagTime],
			["fmFreqMax", 127.midicps, defLagTime],
			["fmRatio", ControlSpec(0.01, 10).unmap(1), defLagTime],
			["fmRatioMin", 0.01, defLagTime],
			["fmRatioMax", 10, defLagTime],
			["modPitchbend", 0, defLagTime],
			["modModify1", 0, defLagTime],
			["modModify2", 0, defLagTime],
			["modDelay", 0, \ir],
			["modAttack", 0, \ir],
			["modDecay", 0, \ir],
			["modSustain", 0, \ir],
			["modSustain2", 0, \ir],
			["modSustainTime", 0, \ir],
			["modRelease", 0, \ir],
			["modFMDepth", 0, defLagTime],
			["modFMFreq", 0, defLagTime],
			["modFMRatio", 0, defLagTime],
		];
		arrOptions = [0,0,0,0,0,0];
		arrOptionData = [
			// ind 0
			TXWaveForm.arrOptionData,
			// ind 1
			TXIntonation.arrOptionData,
			// ind 2
			TXEnvLookup.arrDADSSRSlopeOptionData,
			// ind 3
			TXEnvLookup.arrDADSSRSustainOptionData,
			// ind 4
			 // amp compensation
			TXAmpComp.arrOptionData,
			// ind 5 Frequency Modulation
			[
				["Off - no Frequency Modulation (FM)", {1;}],
				["External- use FM audio input & FM depth",
					{arg inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth;
						var fmDepthSum = (fmDepth + modFMDepth).max(0).min(1).linlin(0, 1, fmDepthMin, fmDepthMax);
						2 ** (fmDepthSum * InFeedback.ar(inmodulator,1));
					}
				],
				["Internal - use sine wave, FM freq & FM depth",
					{arg inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth, fmFreq, fmFreqMin, fmFreqMax,  modFMFreq,
						fmRatio, fmRatioMin, fmRatioMax, modFMRatio, baseFreq;
						var fmDepthSum = (fmDepth + modFMDepth).max(0).min(1).linlin(0, 1, fmDepthMin, fmDepthMax);
						var fmFreqSum = (fmFreq + modFMFreq).max(0).min(1).linexp(0, 1, fmFreqMin, fmFreqMax);
						var fmWave = SinOsc.ar(fmFreqSum);
						2 ** (fmDepthSum * fmWave);
					}
				],
				["Internal - use sine wave, [FM ratio x main frequency] & FM depth",
					{arg inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth, fmFreq, fmFreqMin, fmFreqMax,  modFMFreq,
						fmRatio, fmRatioMin, fmRatioMax, modFMRatio, baseFreq;
						var fmDepthSum = (fmDepth + modFMDepth).max(0).min(1).linlin(0, 1, fmDepthMin, fmDepthMax);
						var fmRatioSum = (fmRatio + modFMRatio).max(0).min(1).linlin(0, 1, fmRatioMin, fmRatioMax);
						var fmWave = SinOsc.ar(fmRatioSum * baseFreq);
						2 ** (fmDepthSum * fmWave);
					}
				],
			],
		];
		synthDefFunc = {
			arg inmodulator, out, gate, note, velocity, keytrack, transpose, pitchbend, pitchbendMin, pitchbendMax,
			modify1, modify1Min, modify1Max, modify2, modify2Min, modify2Max,
			level, envtime=0, delay, attack, attackMin, attackMax, decay, decayMin, decayMax, sustain, sustain2,
			sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax, intKey,
			fmDepth, fmDepthMin, fmDepthMax,
			fmFreq, fmFreqMin, fmFreqMax, fmRatio, fmRatioMin, fmRatioMax,
			modPitchbend = 0, modModify1 = 0, modModify2 = 0, modDelay = 0, modAttack = 0, modDecay = 0,
			modSustain = 0, modSustain2 = 0, modSustainTime = 0, modRelease = 0, modFMDepth = 0, modFMFreq = 0, modFMRatio = 0;

			var outEnv, envFunction, intonationFunc, outFreq, pbend, outFunction, outWave, envCurve,
			mod1, mod2, del, att, dec, sus, sus2, sustime, rel, timeControlSpec, freqModFunc, fmOut, ampCompFunction, outAmpComp;

			mod1 = modify1Min + ((modify1Max - modify1Min) * (modify1 + modModify1).max(0).min(1));
			mod2 = modify2Min + ((modify2Max - modify2Min) * (modify2 + modModify2).max(0).min(1));
			pbend = pitchbendMin + ((pitchbendMax - pitchbendMin) * (pitchbend + modPitchbend).max(0).min(1));
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
			outFreq = (intonationFunc.value(
				(note + transpose), intKey) * keytrack) + ((48 + transpose).midicps * (1-keytrack));
			freqModFunc = this.getSynthOption(5);
			fmOut = freqModFunc.value(inmodulator, fmDepth, fmDepthMin, fmDepthMax, modFMDepth, fmFreq, fmFreqMin,
				fmFreqMax,  modFMFreq, fmRatio, fmRatioMin, fmRatioMax, modFMRatio, outFreq);
			outFunction = this.getSynthOption(0);
			outWave = outFunction.value(
				(outFreq * fmOut *  (2 ** (pbend /12))).max(0.midicps),
				mod1,
				mod2
			);
			ampCompFunction = this.getSynthOption(4);
			outAmpComp = ampCompFunction.value(outFreq);
			Out.ar(out, TXClean.ar(outEnv * outWave * outAmpComp * level * (velocity * 0.007874) ));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TestNoteVals"],
			["SynthOptionListPlusMinus", "Waveform", arrOptionData, 0],
			["TXMinMaxSliderSplit", "Modify 1", \unipolar, "modify1", "modify1Min", "modify1Max"],
			["TXMinMaxSliderSplit", "Modify 2", \unipolar, "modify2", "modify2Min", "modify2Max"],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
			["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 4],
			["MIDIListenCheckBox"],
			["MIDIChannelSelector"],
			["MIDINoteSelector"],
			["MIDIVelSelector"],
			["TXCheckBox", "Keyboard tracking", "keytrack"],
			["Transpose"],
			["TXMinMaxSliderSplit", "Pitch bend", ControlSpec(-48, 48), "pitchbend",
				"pitchbendMin", "pitchbendMax" ],
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
			["SynthOptionListPlusMinus", "Freq modulation", arrOptionData, 5],
			["TXMinMaxSliderSplit", "FM depth", classData.fmDepthSpec,
				"fmDepth", "fmDepthMin", "fmDepthMax",],
			["TXMinMaxSliderSplit", "FM freq", classData.fmFreqSpec,
				"fmFreq", "fmFreqMin", "fmFreqMax",],
			["TXMinMaxSliderSplit", "FM ratio", classData.fmRatioSpec,
				"fmRatio", "fmRatioMin", "fmRatioMax",],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.midiNoteInit;
		//	load the synthdef and create the Group for synths to belong to
		this.loadDefAndMakeGroup;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Waveform", {displayOption = "showWaveform";
				this.buildGuiSpecArray; system.showView;}, 110,
			TXColor.white, this.getButtonColour(displayOption == "showWaveform")],
			["Spacer", 3],
			["ActionButton", "MIDI/ Note", {displayOption = "showMIDI";
				this.buildGuiSpecArray; system.showView;}, 110,
			TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
			["Spacer", 3],
			["ActionButton", "Envelope", {displayOption = "showEnv";
				this.buildGuiSpecArray; system.showView;}, 110,
			TXColor.white, this.getButtonColour(displayOption == "showEnv")],
			["Spacer", 3],
			["ActionButton", "FM", {displayOption = "showFM";
				this.buildGuiSpecArray; system.showView;}, 110,
			TXColor.white, this.getButtonColour(displayOption == "showFM")],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showWaveform", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Waveform", arrOptionData, 0, nil, 160, {system.flagGuiUpd}],
				["TextViewDisplay", {TXWaveForm.arrDescriptions.at(arrOptions.at(0).asInteger);}],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 1", \unipolar, "modify1", "modify1Min", "modify1Max"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Modify 2", \unipolar, "modify2", "modify2Min", "modify2Max"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0, 1), "level"],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 4],
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
						["Range -24 to 24", [-24, 24]], ["Range -48 to 48", [-48, 48]] ]
				],
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
				["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax",
					{{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["SynthOptionPopup", "Curve", arrOptionData, 2, 240, {system.showView;}],
				["SpacerLine", 2],
				["SynthOptionPopup", "Env Type", arrOptionData, 3, 300],
			];
		});
		if (displayOption == "showFM", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Freq modulation", arrOptionData, 5],
				["SpacerLine", 6],
				["TXMinMaxFreqNoteSldr", "FM freq", classData.fmFreqSpec,
					"fmFreq", "fmFreqMin", "fmFreqMax", nil, TXFreqRangePresets.arrFreqRanges],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "FM ratio", classData.fmRatioSpec,
					"fmRatio", "fmRatioMin", "fmRatioMax",],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "FM depth", classData.fmDepthSpec,
					"fmDepth", "fmDepthMin", "fmDepthMax",],
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
		^[testMIDINote, testMIDIVel, testMIDITime];
	}

	loadExtraData {arg argData;  // override default method
		testMIDINote = argData.at(0);
		testMIDIVel = argData.at(1);
		testMIDITime = argData.at(2);
		// amend older data
		if (system.dataBank.savedSystemRevision < 1004, {
			this.setSynthValue("sustain2", this.getSynthArgSpec("sustain"));
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

