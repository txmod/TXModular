// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFilterSynth2 : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var ratioView;
	var	envView;
	var <>testMIDINote = 69;
	var <>testMIDIVel = 100;
	var <>testMIDITime = 1;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Filter Synth";
		classData.moduleRate = "audio";
		classData.moduleType = "groupsource";
		classData.arrCtlSCInBusSpecs = [
			["Resonance", 1, "modRes", 0],
			["Saturation", 1, "modSat", 0],
			["Pitch bend", 1, "modPitchbend", 0],
			["Delay", 1, "modDelay", 0],
			["Attack", 1, "modAttack", 0],
			["Decay", 1, "modDecay", 0],
			["Sustain level", 1, "modSustain", 0],
			["Sustain level 2", 1, "modSustain2", 0],
			["Sustain time", 1, "modSustainTime", 0],
			["Release", 1, "modRelease", 0],
		];
		classData.arrAudSCInBusSpecs = [
			["Input Signal", 1, "insignal"]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.timeSpec = ControlSpec(0.01, 20);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showFilter";
		arrSynthArgSpecs = [
			["insignal", 0, 0],
			["out", 0, 0],
			["gate", 1, 0],
			["note", 0, 0],
			["velocity", 0, 0],
			["keytrack", 1, \ir],
			["transpose", 0, \ir],
			["pitchOffset", 0, \ir],
			["pitchbend", 0.5, defLagTime],
			["pitchbendMin", -2, defLagTime],
			["pitchbendMax", 2, defLagTime],
			["res", 0.5, defLagTime],
			["resMin", 0,  defLagTime],
			["resMax", 1, defLagTime],
			["sat", 0.5, defLagTime],
			["satMin", 0,  defLagTime],
			["satMax", 1, defLagTime],
			["level", 0.25, defLagTime],
			["envtime", 0, \ir],
			["delay", 0, \ir],
			["attack", 0.005, \ir],
			["attackMin", 0, \ir],
			["attackMax", 5, \ir],
			["decay", 0.05, \ir],
			["decayMin", 0, \ir],
			["decayMax", 5, \ir],
			["sustain", 1, \ir],
			["sustain2", 1, \ir],
			["sustainTime", 0.2, \ir],
			["sustainTimeMin", 0, \ir],
			["sustainTimeMax", 5, \ir],
			["release", 0.01, \ir],
			["releaseMin", 0, \ir],
			["releaseMax", 5, \ir],
			["intKey", 0, \ir],
			["modPitchbend", 0, defLagTime],
			["modRes", 0, defLagTime],
			["modSat", 0, defLagTime],
			["modDelay", 0, \ir],
			["modAttack", 0, \ir],
			["modDecay", 0, \ir],
			["modSustain", 0, \ir],
			["modSustain2", 0, \ir],
			["modSustainTime", 0, \ir],
			["modRelease", 0, \ir],
		];
		arrOptions = [0,0,0,0,0,0];
		arrOptionData = [
			TXFilter.arrOptionData,
			TXIntonation.arrOptionData,
			TXEnvLookup.arrDADSSRSlopeOptionData,
			TXEnvLookup.arrDADSSRSustainOptionData,
			TXLevelControl.arrOptionData,
			 // amp compensation
			TXAmpComp.arrOptionData,
		];
		synthDefFunc = {
			arg insignal, out, gate, note, velocity, keytrack, transpose,
			pitchOffset,  pitchbend, pitchbendMin, pitchbendMax,
			res, resMin, resMax, sat, satMin, satMax,
			level, envtime=0, delay, attack, attackMin, attackMax, decay, decayMin, decayMax, sustain, sustain2,
			sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax, intKey,
			modPitchbend=0, modRes=0, modSat=0, modDelay=0, modAttack=0, modDecay=0,
			modSustain=0, modSustain2=0, modSustainTime=0, modRelease=0;

			var outEnv, envFunction, intonationFunc, outFreq, pbend, filterFunc, outFilter, envCurve,
			input, sumres, sumsat, levelControlFunc, del, att, dec, sus, sus2, sustime, rel, timeControlSpec,
			ampCompFunction, outAmpComp;

			sumres =  resMin + ( (resMax - resMin) * (res + modRes).max(0).min(0.9999) );
			sumsat =  satMin + ( (satMax - satMin) * (sat + modSat).max(0).min(1) );
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
			filterFunc = this.getSynthOption(0);
			levelControlFunc = this.getSynthOption(4);
			input = InFeedback.ar(insignal,1);
			outFilter = filterFunc.value(
				input,
				outFreq *  (2 ** ((pitchOffset + pbend) /12)),
				sumres,
				sumsat
			);
			ampCompFunction = this.getSynthOption(5);
			outAmpComp = ampCompFunction.value(outFreq);
			// use TXClean to stop blowups
			Out.ar(out,
				TXClean.ar(outEnv * levelControlFunc.value(outFilter * 4, input) * outAmpComp * level  * (velocity * 0.007874))
			);
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TestNoteVals"],
			["SynthOptionListPlusMinus", "Filter type.", arrOptionData, 0],
			["TXMinMaxSliderSplit", "Resonance", ControlSpec(0, 1), "res", "resMin", "resMax"],
			["TXMinMaxSliderSplit", "Saturation", ControlSpec(0, 1), "sat", "satMin", "satMax"],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
			["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 5],
			["SynthOptionPopupPlusMinus", "Level control", arrOptionData, 4],
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
			["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax",
				{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decay", "decayMin", "decayMax",{{this.updateEnvView;}.defer;}],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView;}.defer;}],
			["EZslider", "Sustain level 2", ControlSpec(0, 1), "sustain2", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTime", "sustainTimeMin",
				"sustainTimeMax",{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax",
				{{this.updateEnvView;}.defer;}],
			["SynthOptionPopup", "Curve", arrOptionData, 2, 240, {system.showView;}],
			["SynthOptionPopup", "Env Type", arrOptionData, 3, 300],
			["SynthOptionPopup", "Intonation", arrOptionData, 1, nil,
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
		//	load the synthdef and create the Group for synths to belong to
		this.loadDefAndMakeGroup;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Filter", {displayOption = "showFilter";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showFilter")],
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
		if (displayOption == "showFilter", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Filter", arrOptionData, 0],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Resonance", ControlSpec(0, 1), "res", "resMin", "resMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Saturation", ControlSpec(0, 1), "sat", "satMin", "satMax"],
				["SpacerLine", 4],
				["EZslider", "Level", ControlSpec(0, 1), "level"],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 5],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Level control", arrOptionData, 4],
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
				["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax",
					{{this.updateEnvView;}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decay", "decayMin", "decayMax",
					{{this.updateEnvView;}.defer;}],
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

