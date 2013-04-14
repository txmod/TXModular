// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXEnvDADSR4 : TXModuleBase {

	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar	<guiWidth=500;
	classvar	timeSpec;

	var	displayOption;
	var	envView;

	*initClass{
		arrInstances = [];
		//	set class specific variables
		defaultName = "Env DADSR";
		moduleRate = "control";
		moduleType = "groupsource";
		arrCtlSCInBusSpecs = [
			["Delay", 1, "modDelay", 0],
			["Attack", 1, "modAttack", 0],
			["Decay", 1, "modDecay", 0],
			["Sustain level", 1, "modSustain", 0],
			["Sustain time", 1, "modSustainTime", 0],
			["Release", 1, "modRelease", 0],
		];
		noOutChannels = 1;
		arrOutBusSpecs = [
			["Out", [0]]
		];
		timeSpec = ControlSpec(0.001, 20);
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
			["attack", 0.001, \ir],
			["attackMin", 0, \ir],
			["attackMax", 5, \ir],
			["decay", 0.05, \ir],
			["decayMin", 0, \ir],
			["decayMax", 5, \ir],
			["sustain", 1, \ir],
			["sustainTime", 0.2, \ir],
			["sustainTimeMin", 0, \ir],
			["sustainTimeMax", 5, \ir],
			["release", 0.01, \ir],
			["releaseMin", 0, \ir],
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
			[
				["linear", 'linear'],
				//invalid		["exponential", 'exponential'],
				["sine", 'sine'],
				["welch", 'welch'],
				//invalid		["step", 'step'],
				["slope +10 ", 10],
				["slope +9 ", 9],
				["slope +8 ", 8],
				["slope +7 ", 7],
				["slope +6 ", 6],
				["slope +5 ", 5],
				["slope +4 ", 4],
				["slope +3 ", 3],
				["slope +2 ", 2],
				["slope +1 ", 1],
				["slope -1", -1],
				["slope -2 ", -2],
				["slope -3 ", -3],
				["slope -4 ", -4],
				["slope -5 ", -5],
				["slope -6 ", -6],
				["slope -7 ", -7],
				["slope -8 ", -8],
				["slope -9 ", -9],
				["slope -10 ", -10]
			],
			[
				["Sustain",
					{arg del, att, dec, sus, sustime, rel, envCurve;
						Env.dadsr(del, att, dec, sus, rel, 1, envCurve);
					}
				],
				["Fixed Length",
					{arg del, att, dec, sus, sustime, rel, envCurve;
						Env.new([0, 0, 1, sus, sus, 0], [del, att, dec, sustime, rel], envCurve, nil);
					}
				]
			],
		];
		synthDefFunc = {
			arg out, gate, note, velocity, envtime=0, delay, attack, attackMin, attackMax, decay, decayMin, decayMax,
			sustain, sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax,
			velocityScaling, modDelay, modAttack, modDecay, modSustain, modSustainTime, modRelease;
			var outEnv, outFreq, outFunction, outWave, del, att, dec, sus, sustime, rel, timeMult, envCurve, envFunction;
			del = (delay + modDelay).max(0).min(1);
			att = (attackMin + ((attackMax - attackMin) * (attack + modAttack))).max(0.001).min(20);
			dec = (decayMin + ((decayMax - decayMin) * (decay + modDecay))).max(0.001).min(20);
			sus = (sustain + modSustain).max(0).min(1);
			sustime = (sustainTimeMin +
				((sustainTimeMax - sustainTimeMin) * (sustainTime + modSustainTime))).max(0.001).min(20);
			rel = (releaseMin + ((releaseMax - releaseMin) * (release + modRelease))).max(0.001).min(20);
			envCurve = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			envFunction = this.getSynthOption(1);
			outEnv = EnvGen.kr(
				envFunction.value(del, att, dec, sus, sustime, rel, envCurve),
				gate,
				doneAction: 2
			);
			// amplitude is vel *  0.00315 approx. == 1 / 127
			Out.kr(out, outEnv * ((velocity * 0.007874) + (1-velocityScaling)).min(1));
		};
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Trigger envelope", {this.createSynthNote(60, 100, 1);}],
			["commandAction", "Trigger envelope gate on only", {this.createSynthNote(60, 100, 0);}],
			["commandAction", "Trigger envelope gate off only", {this.releaseSynthGate;}],
			["commandAction", "Plot envelope", {this.envPlot;}],
			["TXEnvDisplay", {this.envViewValues;}, {arg view; envView = view;}],
			["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Attack", timeSpec, "attack", "attackMin", "attackMax",{{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Decay", timeSpec, "decay", "decayMin", "decayMax",{{this.updateEnvView}.defer;}],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime", "sustainTimeMin",
				"sustainTimeMax",{{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Release", timeSpec, "release", "releaseMin", "releaseMax",{{this.updateEnvView}.defer;}],
			["SynthOptionPopup", "Curve", arrOptionData, 0, 200, {system.showView;}],
			["SynthOptionPopup", "Env. Type", arrOptionData, 1, 200],
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
			["ActionButton", "MIDI / Trigger", {displayOption = "showMIDI";
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
				["TXEnvDisplay", {this.envViewValues;}, {arg view; envView = view;}],
				["NextLine"],
				["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView}.defer;}],
				["TXMinMaxSliderSplit", "Attack", timeSpec, "attack", "attackMin", "attackMax",{{this.updateEnvView}.defer;}],
				["TXMinMaxSliderSplit", "Decay", timeSpec, "decay", "decayMin", "decayMax",{{this.updateEnvView}.defer;}],
				["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView}.defer;}],
				["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime", "sustainTimeMin",
					"sustainTimeMax",{{this.updateEnvView}.defer;}],
				["TXMinMaxSliderSplit", "Release", timeSpec, "release", "releaseMin", "releaseMax",{{this.updateEnvView}.defer;}],
				["SynthOptionPopup", "Curve", arrOptionData, 0, 200, {system.showView;}],
				["NextLine"],
				["SynthOptionPopup", "Env. Type", arrOptionData, 1, 200],
				["Spacer", 4],
				["ActionButton", "Plot", {this.envPlot;}],
				["SpacerLine", 3],
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

	envPlot {
		var del, att, dec, sus, sustime, rel, envCurve;
		del = this.getSynthArgSpec("delay");
		att = this.getSynthArgSpec("attack");
		dec = this.getSynthArgSpec("decay");
		sus = this.getSynthArgSpec("sustain");
		sustime = this.getSynthArgSpec("sustainTime");
		rel = this.getSynthArgSpec("release");
		envCurve = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
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

