// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXGainDADSR : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var	envView;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Gain DADSR";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Gate", 1, "modGate", 0],
			["Delay", 1, "modDelay", 0],
			["Attack", 1, "modAttack", 0],
			["Decay", 1, "modDecay", 0],
			["Sustain level", 1, "modSustain", 0],
			["Sustain time", 1, "modSustainTime", 0],
			["Release", 1, "modRelease", 0],
			["Level scale", 1, "modLevelScale", 0],
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
		displayOption = "showEnv";
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["gate", 0, 0],
			["note", 0, 0],
			["envtime", 0, 0],
			["delay", 0, 0],
			["attack", 0.01, 0],
			["attackMin", 0.01, 0],
			["attackMax", 5, 0],
			["decay", 0.05, 0],
			["decayMin", 0.01, 0],
			["decayMax", 5, 0],
			["sustain", 1, 0],
			["sustainTime", 0.2, 0],
			["sustainTimeMin", 0.01, 0],
			["sustainTimeMax", 5, 0],
			["release", 0.01, 0],
			["releaseMin", 0.01, 0],
			["releaseMax", 5, 0],
			["levelScale", 1, 0],
			["modGate", 0, 0],
			["modGateCloseTime", 0, 0],
			["modDelay", 0, 0],
			["modAttack", 0, 0],
			["modDecay", 0, 0],
			["modSustain", 0, 0],
			["modSustainTime", 0, 0],
			["modRelease", 0, 0],
			["modLevelScale", 1, 0],
		];
		arrOptions = [0, 1];
		arrOptionData = [
			TXEnvLookup.arrDADSRSlopeOptionData,
			TXEnvLookup.arrDADSRSustainOptionData,
		];
		synthDefFunc = {arg in, out, gate, gateCloseTime, gateCloseTimeMin, gateCloseTimeMax, note, envtime=0,
			delay, attack, attackMin, attackMax, decay, decayMin, decayMax,
			sustain, sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax, levelScale,
			modGate = 0, modGateCloseTime = 0, modDelay = 0, modAttack = 0, modDecay = 0, modSustain = 0, modSustainTime = 0,
			modRelease = 0, modLevelScale = 0;

			var outEnv, outFreq, outFunction, outWave, gt;
			var del, att, dec, sus, sustime, rel, timeMult;
			var levelSc, envCurve, envFunction;
			var input = InFeedback.ar(in,1);

			envCurve = this.getSynthOption(0);
			envFunction = this.getSynthOption(1);
			gt = (gate + modGate).max(0).min(1);
			del = (delay + modDelay).max(0).min(1);
			att = (attackMin + ((attackMax - attackMin) * (attack + modAttack).max(0).min(1))).max(0.001).min(20);
			dec = (decayMin + ((decayMax - decayMin) * (decay + modDecay).max(0).min(1))).max(0.001).min(20);
			sus = (sustain + modSustain).max(0).min(1);
			sustime = (sustainTimeMin +
				((sustainTimeMax - sustainTimeMin) * (sustainTime + modSustainTime).max(0).min(1))).max(0.01).min(20);
			rel = (releaseMin + ((releaseMax - releaseMin) * (release + modRelease).max(0).min(1))).max(0.01).min(20);
			outEnv = EnvGen.kr(
				envFunction.value(del, att, dec, sus, sustime, rel, envCurve),
				gt,
				doneAction: 0
			)
			- 0.001; // remove exponential offset in env function
			levelSc = (levelScale + modLevelScale).max(0).min(1);
			Out.ar(out, input * outEnv * levelSc);
		};
		guiSpecArray = [
			["NextLine"],
			["TXPresetPopup", "Env presets",
				TXEnvPresets.arrMonoEnvPresets(this, 0, 1).collect({arg item, i; item.at(0)}),
				TXEnvPresets.arrMonoEnvPresets(this, 0, 1).collect({arg item, i; item.at(1)})
			],
			["TXEnvPlot", {this.envViewValues;}, {arg view; envView = view;}],
			["NextLine"],
			["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView}.defer;}],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax",
				{{this.updateEnvView}.defer;}],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decay", "decayMin", "decayMax",
				{{this.updateEnvView}.defer;}],
			["SpacerLine", 2],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView}.defer;}],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTime", "sustainTimeMin",
				"sustainTimeMax",{{this.updateEnvView}.defer;}],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax",
				{{this.updateEnvView}.defer;}],
			["SpacerLine", 2],
			["SynthOptionPopup", "Curve", arrOptionData, 0, 190, {system.showView;}],
			["SpacerLine", 2],
			["SynthOptionPopup", "Env. Type", arrOptionData, 1, 288],
			["SpacerLine", 2],
			["EZslider", "Level scale", ControlSpec(0, 1), "levelScale"],
			["SpacerLine", 2],
			["TXCheckBox", "Gate", "gate"],
		];
		arrActionSpecs = this.buildActionSpecs([
			["TXEnvPlot", {this.envViewValues;}, {arg view; envView = view;}],
			["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Attack", classData.timeSpec, "attack", "attackMin", "attackMax",
				{{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Decay", classData.timeSpec, "decay", "decayMin", "decayMax",
				{{this.updateEnvView}.defer;}],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Sustain time", classData.timeSpec, "sustainTime", "sustainTimeMin",
				"sustainTimeMax",{{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Release", classData.timeSpec, "release", "releaseMin", "releaseMax",
				{{this.updateEnvView}.defer;}],
			["SynthOptionPopup", "Curve", arrOptionData, 0, 230, {system.showView;}],
			["SynthOptionPopup", "Env. Type", arrOptionData, 1, 288],
			["EZslider", "Level scale", ControlSpec(0, 1), "levelScale"],
			["TXCheckBox", "Gate", "gate"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
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

