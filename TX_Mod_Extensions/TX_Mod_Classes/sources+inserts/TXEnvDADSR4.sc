// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXEnvDADSR4 : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var	envView;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Env DADSR";
		classData.moduleRate = "control";
		classData.moduleType = "groupsource";
		classData.arrCtlSCInBusSpecs = [
			["Delay", 1, "modDelay", 0],
			["Attack", 1, "modAttack", 0],
			["Decay", 1, "modDecay", 0],
			["Sustain level", 1, "modSustain", 0],
			["Sustain time", 1, "modSustainTime", 0],
			["Release", 1, "modRelease", 0],
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Env Out", [0]],
			["Env End Trig", [1]],
		];
		classData.timeSpec = ControlSpec(0.01, 20);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showEnv";
		arrSynthArgSpecs = [
			["out", 0, 0],
			["gate", 1, 0],
			["note", 0, \ir],
			["velocity", 0, \ir],
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
			["velocityScaling", 1, \ir],
			["modDelay", 0, \ir],
			["modAttack", 0, \ir],
			["modDecay", 0, \ir],
			["modSustain", 0, \ir],
			["modSustainTime", 0, \ir],
			["modRelease", 0, \ir],
		];
		arrOptions = [0,0];
		arrOptionData = [
			TXEnvLookup.arrDADSRSlopeOptionData,
			TXEnvLookup.arrDADSRSustainOptionData,
		];
		synthDefFunc = {
			arg out, gate, note, velocity, envtime=0, delay, attack, attackMin, attackMax, decay, decayMin, decayMax,
			sustain, sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax,
			velocityScaling,
			modDelay = 0, modAttack = 0, modDecay = 0, modSustain = 0, modSustainTime = 0, modRelease = 0;

			var outEnv, outFreq, outFunction, outWave, del, att, dec, sus, sustime, rel, timeMult, envCurve, envFunction, envEnd;
			del = (delay + modDelay).max(0).min(1);
			att = (attackMin + ((attackMax - attackMin) * (attack + modAttack).max(0).min(1))).max(0.001).min(20);
			dec = (decayMin + ((decayMax - decayMin) * (decay + modDecay).max(0).min(1))).max(0.001).min(20);
			sus = (sustain + modSustain).max(0).min(1);
			sustime = (sustainTimeMin +
				((sustainTimeMax - sustainTimeMin) * (sustainTime + modSustainTime).max(0).min(1))).max(0.01).min(20);
			rel = (releaseMin + ((releaseMax - releaseMin) * (release + modRelease).max(0).min(1))).max(0.01).min(20);
			envCurve = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			envFunction = this.getSynthOption(1);
			outEnv = EnvGen.kr(
				envFunction.value(del, att, dec, sus, sustime, rel, envCurve),
				gate,
				doneAction: 2
			);
			envEnd = Trig1.kr(Done.kr(outEnv), ControlDur.ir);
			// outEnv - 0.001 : remove exponential offset in env function
			Out.kr(out, [(outEnv - 0.001) * ((velocity * 0.007874) + (1-velocityScaling)).min(1), envEnd]);
		};
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Trigger envelope", {this.createSynthNote(60, 100, 1);}],
			["commandAction", "Trigger envelope gate on only", {this.createSynthNote(60, 100, 0);}],
			["commandAction", "Trigger envelope gate off only", {this.releaseSynthGate;}],
			["TXEnvPlot", {this.envViewValues;}, {arg view; envView = view;}],
			["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax",{{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decay", "decayMin", "decayMax",{{this.updateEnvView}.defer;}],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTime", "sustainTimeMin",
				"sustainTimeMax",{{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax",{{this.updateEnvView}.defer;}],
			["SynthOptionPopup", "Curve", arrOptionData, 0, 240, {system.showView;}],
			["SynthOptionPopup", "Env Type", arrOptionData, 1, 300],
			["TXCheckBox", "Scale level to velocity", "velocityScaling"],
			["MIDIListenCheckBox"],
			["MIDIChannelSelector"],
			["MIDINoteSelector"],
			["MIDIVelSelector"],
			["PolyphonySelector"],
		]);
		this.buildGuiSpecArray;
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.setMonophonic;	// monophonic by default
		this.midiNoteInit;
		//	load the synthdef and create the Group for synths to belong to
		this.loadDefAndMakeGroup;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Envelope", {displayOption = "showEnv";
				this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayOption == "showEnv")],
			["Spacer", 3],
			["ActionButton", "MIDI", {displayOption = "showMIDI";
				this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
			["Spacer", 3],
			["ActionButton", "Trigger Envelope", {this.createSynthNote(60, 100, 1);},
				130, TXColor.white, TXColor.sysGuiCol2],
			["SpacerLine", 6],
		];
		if (displayOption == "showEnv", {
			guiSpecArray = guiSpecArray ++[
				["TXPresetPopup", "Env presets",
					TXEnvPresets.arrEnvPresets(this, 0, 1).collect({arg item, i; item.at(0)}),
					TXEnvPresets.arrEnvPresets(this, 0, 1).collect({arg item, i; item.at(1)})
				],
				["TXEnvPlot", {this.envViewValues;}, {arg view; envView = view;}],
				["NextLine"],
				["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax",{{this.updateEnvView}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decay", "decayMin", "decayMax",{{this.updateEnvView}.defer;}],
				["SpacerLine", 2],
				["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTime", "sustainTimeMin",
					"sustainTimeMax",{{this.updateEnvView}.defer;}],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax",{{this.updateEnvView}.defer;}],
				["SpacerLine", 2],
				["SynthOptionPopup", "Curve", arrOptionData, 0, 240, {system.showView;}],
				["SpacerLine", 2],
				["SynthOptionPopup", "Env Type", arrOptionData, 1, 300],
				["SpacerLine", 2],
				["TXCheckBox", "Scale level to velocity", "velocityScaling"],
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
				["NextLine"],
				["PolyphonySelector"],
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
		envCurve = this.getSynthOption(0);
		^[[0, 0, 1, sus, sus, 0].max(0.001), [del, att, dec, sustime, rel], envCurve];
	}

	updateEnvView {
		if (envView.respondsTo('notClosed'), {
			if (envView.notClosed, {
				envView.value = this.envViewValues;
			});
		});
	}

}

