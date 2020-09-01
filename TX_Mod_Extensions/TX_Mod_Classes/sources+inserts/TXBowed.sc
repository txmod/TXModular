// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

/*
DWGBowed.ar(freq: 440, velb: 0.5, force: 1, gate: 1, pos: 0.14, release: 0.1,
c1: 1, c3: 3, impZ: 0.55, fB: 2);

DWGBowedTor.ar(freq: 440, velb: 0.5, force: 1, gate: 1, pos: 0.14, release: 0.1,
c1: 1, c3: 3, impZ: 0.55, fB: 2, mistune: 5.2, c1tor: 1, c3tor: 3000, iZtor: 1.8)}

DWGBowed - Digital wave guide physical model of a bowed instrument. Sound must go throught BowSoundBoard for better sound.
DWGBowedTor - Like DWGBowed but also with torsional waves.

freq - Sound frequency.
velb - Bow velocity.
force - Bow normal force.
gate - Releases synth when value changes from >0 to 0.
pos - Relative bow position from 0 to 1.
release - Release time in seconds.
c1 - Inverse of DC decay time.
c3 - High frequency loss factor.
impZ - String impedance.
fB - Inharmonicity factor.
==== extra args for DWGBowedTor:
mistune - The relative frequency of torsional waves.
c1tor - Same as c1 for torsional waves.
c3tor - Same as c3 for torsional waves.
iZtor - Torsional waves string impedance.
*/

TXBowed : TXModuleBase {

	classvar <>classData;

	var displayOption;
	var ratioView;
	var <>testMIDINote = 69;
	var <>testMIDIVel = 100;
	var <>testMIDITime = 1;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Bowed";
		classData.moduleRate = "audio";
		classData.moduleType = "groupsource";
		classData.arrCtlSCInBusSpecs = [
			["Bow force", 1, "modBowForce", 0],
			["Bow position", 1, "modBowPos", 0],
			["DC decay", 1, "modDcDecay", 0],
			["HF loss", 1, "modHFLoss", 0],
			["String imped", 1, "modStringImped", 0],
			["Inharmonicity", 1, "modInharmonicity", 0],
			["Tor freq ratio", 1, "modTorFreqRatio", 0],
			["Tor DC decay", 1, "modTorDcDecay", 0],
			["Tor HF loss", 1, "modTorHFLoss", 0],
			["Tor String imped", 1, "modTorStringImped", 0],
			["Pitch bend", 1, "modPitchbend", 0],
			["Sustain time", 1, "modSustainTime", 0],
			["Release", 1, "modRelease", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.specDefault = ControlSpec(0, 1, 'lin');
		classData.specTime = ControlSpec(0.001, 20);
		classData.specBowForce = ControlSpec(0.001, 10, 'lin');
		classData.specDcDecay = ControlSpec(0.001, 100, 'exp');
		classData.specHFLoss = ControlSpec(0.01, 10000, 'exp');
		classData.specImpedance = ControlSpec(0.001, 5, 'lin');
		classData.specInharmonicity = ControlSpec(0.001, 10, 'lin');
		classData.specTorFreqRatio = ControlSpec(0.01, 100, 'lin');
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
			["note", 0, 0],
			["velocity", 0, 0],
			["keytrack", 1, \ir],
			["transpose", 0, \ir],
			["pitchOffset", 0, \ir],
			["pitchbend", 0.5, defLagTime],
			["pitchbendMin", -2, defLagTime],
			["pitchbendMax", 2, defLagTime],

			["bowForce", classData.specBowForce.unmap(1), defLagTime],
			["bowForceMin", 0.001, defLagTime],
			["bowForceMax", 10, defLagTime],
			["bowPos", 0.14, defLagTime],
			["bowPosMin", 0, defLagTime],
			["bowPosMax", 1, defLagTime],
			["dcDecay", ControlSpec(0.01, 10, 'exp').unmap(1), defLagTime],
			["dcDecayMin", 0.01, defLagTime],
			["dcDecayMax", 10, defLagTime],
			["hFLoss", classData.specHFLoss.unmap(3), defLagTime],
			["hFLossMin", 0.01, defLagTime],
			["hFLossMax", 10000, defLagTime],
			["stringImped", 0.55, defLagTime],
			["inharmonicity", 2, defLagTime],

			["torFreqRatio", ControlSpec(0.01, 10, 'lin').unmap(5.2), defLagTime],
			["torFreqRatioMin", 0.01, defLagTime],
			["torFreqRatioMax", 10, defLagTime],
			["torDcDecay", ControlSpec(0.01, 10, 'exp').unmap(1), defLagTime],
			["torDcDecayMin", 0.01, defLagTime],
			["torDcDecayMax", 10, defLagTime],
			["torHFLoss", classData.specHFLoss.unmap(3000), defLagTime],
			["torHFLossMin", 0.01, defLagTime],
			["torHFLossMax", 10000, defLagTime],
			["torStringImped", 1.8, defLagTime],

			["level", 0.25, defLagTime],
			["sustainTime", ControlSpec(0.01, 5, 'lin').unmap(0.2), \ir],
			["sustainTimeMin", 0.01, \ir],
			["sustainTimeMax", 5, \ir],
			["release", ControlSpec(0.01, 5, 'lin').unmap(0.1), defLagTime],
			["releaseMin", 0.01, defLagTime],
			["releaseMax", 5, defLagTime],
			["intKey", 0, \ir],
			["modPitchbend", 0, defLagTime],
			["modBowForce", 0, defLagTime],
			["modBowPos", 0, defLagTime],
			["modDcDecay", 0, defLagTime],
			["modHFLoss", 0, defLagTime],
			["modStringImped", 0, defLagTime],
			["modInharmonicity", 0, defLagTime],
			["modTorFreqRatio", 0, defLagTime],
			["modTorDcDecay", 0, defLagTime],
			["modTorHFLoss", 0, defLagTime],
			["modTorStringImped", 0, defLagTime],
			["modSustainTime", 0, defLagTime],
			["modRelease", 0, defLagTime],
		];
		arrOptions = [1, 0, 0, 0];
		arrOptionData = [
			[ // Torsional Waves Option
				["Off - Torsional waves inactive",
					{arg freq, velb, force, gate, pos, release, c1, c3, impZ, fB;
						DWGBowed.ar(freq, velb, force, gate, pos, release, c1, c3, impZ, fB);
					}
				],
				["On - Torsional waves active",
					{arg freq, velb, force, gate, pos, release, c1, c3, impZ, fB, mistune, c1tor, c3tor, iZtor;
						DWGBowedTor.ar(freq, velb, force, gate, pos, release, c1, c3, impZ, fB,
							mistune, c1tor, c3tor, iZtor);
					}
				]
			],
			TXIntonation.arrOptionData,
			[ // env type
				["Sustain - sustain time ignored",
					{arg gate, sustime;
						gate;
					}
				],
				["Fixed Length - using sustain time",
					{arg gate, sustime;
						Trig.kr(gate, sustime);
					}
				]
			],
			 // amp compensation
			TXAmpComp.arrOptionData,
		];
		synthDefFunc = {
			arg out, gate, note, velocity, keytrack, transpose,
			pitchOffset,  pitchbend, pitchbendMin, pitchbendMax,
			bowForce, bowForceMin, bowForceMax,
			bowPos, bowPosMin, bowPosMax,
			dcDecay, dcDecayMin, dcDecayMax, hFLoss, hFLossMin, hFLossMax, stringImped,
			inharmonicity,

			torFreqRatio, torFreqRatioMin, torFreqRatioMax, torDcDecay, torDcDecayMin, torDcDecayMax,
			torHFLoss, torHFLossMin, torHFLossMax, torStringImped,

			level, sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax, intKey,

			modPitchbend = 0, modBowForce = 0, modBowPos = 0, modDcDecay = 0, modHFLoss = 0, modStringImped = 0, modInharmonicity = 0,
			modTorFreqRatio = 0, modTorDcDecay = 0, modTorHFLoss = 0, modTorStringImped = 0,
			modSustainTime = 0, modRelease = 0;

			var gateFunction, outGate, intonationFunc, hold_freq, pbend, sustime, rel, bowFunc, outSound, outEnv,
			hold_bowForce, hold_bowPos, hold_dcDecay, hold_hFLoss, hold_stringImped, hold_inharmonicity,
			hold_torFreqRatio, hold_torDcDecay, hold_torHFLoss, hold_torStringImped, ampCompFunction, outAmpComp;

			hold_bowForce = LinLin.kr((bowForce + modBowForce).max(0).min(1), 0, 1, bowForceMin, bowForceMax);
			hold_bowPos = LinLin.kr((bowPos + modBowPos).max(0).min(1), 0, 1, bowPosMin, bowPosMax);
			hold_dcDecay = LinExp.kr((dcDecay + modDcDecay).max(0).min(1), 0, 1, dcDecayMin, dcDecayMax);
			hold_hFLoss = LinExp.kr((hFLoss + modHFLoss).max(0).min(1), 0, 1, hFLossMin, hFLossMax);
			hold_stringImped = classData.specImpedance.map((stringImped + modStringImped).max(0).min(1));
			hold_inharmonicity = classData.specImpedance.map((inharmonicity + modInharmonicity).max(0).min(1));

			hold_torFreqRatio = LinLin.kr((torFreqRatio + modTorFreqRatio).max(0).min(1), 0, 1, torFreqRatioMin, torFreqRatioMax);
			hold_torDcDecay = LinExp.kr((torDcDecay + modTorDcDecay).max(0).min(1), 0, 1, torDcDecayMin, torDcDecayMax);
			hold_torHFLoss = LinExp.kr((torHFLoss + modTorHFLoss).max(0).min(1), 0, 1, torHFLossMin, torHFLossMax);
			hold_torStringImped = classData.specImpedance.map((torStringImped + modTorStringImped).max(0).min(1));

			// pitchbendMin + ((pitchbendMax - pitchbendMin) * (pitchbend + modPitchbend).max(0).min(1));
			pbend = LinLin.kr((pitchbend + modPitchbend).max(0).min(1), 0, 1, pitchbendMin, pitchbendMax);
			sustime = (sustainTimeMin +
				((sustainTimeMax - sustainTimeMin) * (sustainTime + modSustainTime))).max(0.001).min(20);
			rel = (releaseMin + ((releaseMax - releaseMin) * (release + modRelease))).max(0.001).min(20);
			gateFunction = this.getSynthOption(2);
			outGate = gateFunction.value(gate, sustime);
			intonationFunc = this.getSynthOption(1);
			hold_freq = (intonationFunc.value(
				(note + transpose), intKey) * keytrack) + ((48 + transpose).midicps * (1-keytrack));
			hold_freq = hold_freq *  (2 ** ((pitchOffset + pbend) /12));

			// DWGBowedTor.ar(freq: 440, velb: 0.5, force: 1, gate: 1, pos: 0.14, release: 0.1,
			// c1: 1, c3: 3, impZ: 0.55, fB: 2, mistune: 5.2, c1tor: 1, c3tor: 3000, iZtor: 1.8);
			bowFunc = this.getSynthOption(0);
			outSound = bowFunc.value(
				hold_freq,
				velocity * 0.007874, // scale 1/127
				hold_bowForce,
				outGate,
				hold_bowPos,
				rel,
				hold_dcDecay, hold_hFLoss, hold_stringImped, hold_inharmonicity,
				hold_torFreqRatio, hold_torDcDecay, hold_torHFLoss, hold_torStringImped
			);
			outEnv = EnvGen.ar(Env.asr(0, 1, rel), outGate, levelScale: 0.3, doneAction: 2);
			ampCompFunction = this.getSynthOption(3);
			outAmpComp = ampCompFunction.value(hold_freq);
			Out.ar(out, TXClean.ar(level * outAmpComp * outEnv * outSound));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TestNoteVals"],
			["SynthOptionPopup", "Env Type", arrOptionData, 2, 300],
			["TXMinMaxSliderSplit", "Sustain time", classData.specTime, "sustainTime", "sustainTimeMin",
				"sustainTimeMax"],
			["TXMinMaxSliderSplit", "Release", classData.specTime,
				"release", "releaseMin", "releaseMax"],
			["EZslider", "Out level", ControlSpec(0, 1), "level"],
			["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 3],
			["TXMinMaxSliderSplit", "Bow force", classData.specBowForce,
				"bowForce", "bowForceMin", "bowForceMax"],
			["TXMinMaxSliderSplit", "Bow  position", classData.specDefault,
				"bowPos", "bowPosMin", "bowPosMax"],
			["TXMinMaxSliderSplit", "DC decay", classData.specDcDecay, "dcDecay", "dcDecayMin", "dcDecayMax"],
			["TXMinMaxSliderSplit", "HF loss", classData.specHFLoss,
				"hFLoss", "hFLossMin", "hFLossMax"],
			["EZslider", "String imped", classData.specImpedance, "stringImped"],
			["EZslider", "Inharmonicity", classData.specInharmonicity, "inharmonicity"],
			["SynthOptionPopupPlusMinus", "Tor Waves", arrOptionData, 0],
			["TXMinMaxSliderSplit", "Tor freq ratio", classData.specTorFreqRatio, "torFreqRatio", "torFreqRatioMin", "torFreqRatioMax"],
			["TXMinMaxSliderSplit", "Tor DC decay", classData.specDcDecay, "torDcDecay", "torDcDecayMin", "torDcDecayMax"],
			["TXMinMaxSliderSplit", "Tor HF loss", classData.specHFLoss,
				"torHFLoss", "torHFLossMin", "torHFLossMax"],
			["EZslider", "Tor String imp", classData.specImpedance, "torStringImped"],
			["MIDIListenCheckBox"],
			["MIDIChannelSelector"],
			["MIDINoteSelector"],
			["MIDIVelSelector"],
			["TXCheckBox", "Keyboard tracking", "keytrack"],
			["Transpose"],
			["TXMinMaxSliderSplit", "Pitch bend", ControlSpec(-48, 48), "pitchbend",
				"pitchbendMin", "pitchbendMax" ],
			["PolyphonySelector"],
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
		//	load the synthdef and create the Group for synths to belong to
		this.loadDefAndMakeGroup;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Envelope", {displayOption = "showEnv";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showEnv")],
			["Spacer", 3],
			["ActionButton", "Bow Model", {displayOption = "showBow";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showBow")],
			["Spacer", 3],
			["ActionButton", "Torsional Waves", {displayOption = "showTorsional";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showTorsional")],
			["Spacer", 3],
			["ActionButton", "MIDI/ Note", {displayOption = "showMIDI";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showEnv", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopup", "Env Type", arrOptionData, 2, 300],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Sustain time", classData.specTime, "sustainTime", "sustainTimeMin",
					"sustainTimeMax"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Release", classData.specTime,
					"release", "releaseMin", "releaseMax"],
				["DividingLine"],
				["SpacerLine", 10],
				["EZslider", "Out level", ControlSpec(0, 1), "level"],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 3],
			];
		});
		if (displayOption == "showBow", {
			guiSpecArray = guiSpecArray ++[
				["TXMinMaxSliderSplit", "Bow force", classData.specBowForce,
					"bowForce", "bowForceMin", "bowForceMax"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Bow  position", classData.specDefault,
					"bowPos", "bowPosMin", "bowPosMax"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "DC decay", classData.specDcDecay, "dcDecay", "dcDecayMin", "dcDecayMax"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "HF loss", classData.specHFLoss,
					"hFLoss", "hFLossMin", "hFLossMax"],
				["SpacerLine", 10],
				["EZslider", "String imped", classData.specImpedance, "stringImped"],
				["SpacerLine", 10],
				["EZslider", "Inharmonicity", classData.specInharmonicity, "inharmonicity"],
			];
		});
		if (displayOption == "showTorsional", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Tor Waves", arrOptionData, 0],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Tor freq ratio", classData.specTorFreqRatio, "torFreqRatio", "torFreqRatioMin", "torFreqRatioMax"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Tor DC decay", classData.specDcDecay, "torDcDecay", "torDcDecayMin", "torDcDecayMax"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Tor HF loss", classData.specHFLoss,
					"torHFLoss", "torHFLossMin", "torHFLossMax"],
				["SpacerLine", 10],
				["EZslider", "Tor String imp", classData.specImpedance, "torStringImped"],
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
					[	["Bend Range Presets: ", [-2, 2]],
						["Range -1 to 1", [-1, 1]], ["Range -2 to 2", [-2, 2]],
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
		^[testMIDINote, testMIDIVel, testMIDITime];
	}

	loadExtraData {arg argData;  // override default method
		testMIDINote = argData.at(0);
		testMIDIVel = argData.at(1);
		testMIDITime = argData.at(2);
	}

	updateIntString{arg argIndex;
		if (ratioView.notNil, {
			if (ratioView.notClosed, {
				ratioView.string = TXIntonation.arrScalesText.at(argIndex);
			});
		});
	}

}

