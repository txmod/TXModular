// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

/*
OteyPianoStrings.ar (freq: 440, vel: 1, t_gate: 0, rmin: 0.35, rmax: 2, rampl: 4, rampr: 8, rcore: 1,
lmin: 0.07, lmax: 1.4, lampl: -4, lampr: 4, rho: 1, e: 1, zb: 1, zh: 0, mh: 1, k: 0.2,
alpha: 1, p: 1, hpos: 0.142, loss: 1, detune: 0.0003, hammer_type: 1)

freq - Sound frequency.
vel - Key pulsation velocity. Betwen 0 and 1.
t_gate - Retriggers note, use a TriggerControl for not retriggering continuosly.
rmin - minimum string ratio
rmax - maximun string ratio
rampl - radius left sigmoidal shape
rampr - radius right sigmoidal shape
rcore - core ratio.
lmin - minimum string length.
lmax - maximum string length.
lampl - length left sigmoidal shape.
lampr - length right sigmoidal shape.
rho - string density mult.
e - young modulus mult.
zb - bridge impedance mult.
zh - hammer impedance mult.
mh - mass hammer mult.
k - force hammer mult.
alpha - hysteresys hammer mult.
p - stiffness_exponent_hammer mult.
hpos - hammer position.
loss - string loss factor.
detune - detuning among 3 strings.
hammer_type - 1 is Stulov model, 2 is Banks model.
*/

TXPianoStringSt : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var ratioView;
	var <>testMIDINote = 69;
	var <>testMIDIVel = 100;
	var <>testMIDITime = 1;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Piano String St";
		classData.moduleRate = "audio";
		classData.moduleType = "groupsource";
		classData.arrCtlSCInBusSpecs = [
			["Detune cents", 1, "modDetune", 0],
			["Min string ratio", 1, "modMinStringRatio", 0],
			["Max string ratio", 1, "modMaxStringRatio", 0],
			["Min string length", 1, "modMinStringLength", 0],
			["Max string length", 1, "modMaxStringLength", 0],
			["Pitch bend", 1, "modPitchbend", 0],
			["Sustain time", 1, "modSustainTime", 0],
			["Release", 1, "modRelease", 0],
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.specDefault = ControlSpec(0, 1, 'lin', default: 0);
		classData.specHammerPos = ControlSpec(0, 0.8, 'lin', default: 0);
		classData.specDetune = ControlSpec(0.001, 100, 'exp', default: 0.03);
		classData.specRatio = ControlSpec(0.01, 5, 'lin');
		classData.specLength = ControlSpec(0.001, 20, 'exp');
		classData.specMult = ControlSpec(0, 5, 'lin');
		classData.specShape = ControlSpec(-10, 10, 'lin');
		classData.specTime = ControlSpec(0.01, 20);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showHammer";
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
			["detune", ControlSpec(0.001, 1, 'exp').unmap(0.003), defLagTime],
			["detuneMin", 0.001, defLagTime],
			["detuneMax", 1, defLagTime],
			["minStringRatio", classData.specRatio.unmap(0.35), defLagTime],
			["minStringRatioMin", 0.01, defLagTime],
			["minStringRatioMax", 100, defLagTime],
			["maxStringRatio", classData.specRatio.unmap(2), defLagTime],
			["maxStringRatioMin", 0.01, defLagTime],
			["maxStringRatioMax", 100, defLagTime],
			["minStringLength", classData.specLength.unmap(0.07), defLagTime],
			["minStringLengthMin", 0.001, defLagTime],
			["minStringLengthMax", 1000, defLagTime],
			["maxStringLength", classData.specLength.unmap(1.4), defLagTime],
			["maxStringLengthMin", 0.001, defLagTime],
			["maxStringLengthMax", 1000, defLagTime],

			["radius_L_sigm_shape", 4, defLagTime],
			["radius_R_sigm_shape", 8, defLagTime],
			["core_nucleus_ratio", 1, defLagTime],
			["length_L_sigm_shape", -4, defLagTime],
			["length_R_sigm_shape", 4, defLagTime],
			["string_density_mult", 1, defLagTime],
			["young_modulus_mult", 1, defLagTime],
			["bridge_impedance_mult", 1, defLagTime],
			["hammer_impedance_mult", 0, defLagTime],
			["mass_hammer_mult", 1, defLagTime],
			["force_hammer_mult", 0.2, defLagTime],
			["hysteresys_hammer_mult", 1, defLagTime],
			["stiffness_hammer_mult", 1, defLagTime],
			["hammer_pos", 0.142, defLagTime],
			["string_loss", 1, defLagTime],

			["level", 0.5, defLagTime],
			["panMin", -0.5, defLagTime],
			["panMax", 0.5, defLagTime],
			["sustainTime", 0.2, \ir],
			["sustainTimeMin", 0, \ir],
			["sustainTimeMax", 5, \ir],
			["release", 0.01, \ir],
			["releaseMin", 0.01, \ir],
			["releaseMax", 5, \ir],
			["intKey", 0, \ir],
			["modPitchbend", 0, defLagTime],
			["modDetune", 0, defLagTime],
			["modMinStringRatio", 0, defLagTime],
			["modMaxStringRatio", 0, defLagTime],
			["modMinStringLength", 0, defLagTime],
			["modMaxStringLength", 0, defLagTime],
			["modSustainTime", 0, \ir],
			["modRelease", 0, \ir],
		];
		arrOptions = [0,0,0,0];
		arrOptionData = [
			[ // hammer type
				["Stulov model", 1],
				["Banks model", 2]
			],
			TXIntonation.arrOptionData,
			[ // env type
				["Sustain - sustain time ignored",
					{arg sustime, rel;
						Env.asr(0, 1, rel);
					}
				],
				["Fixed Length - using sustain time",
					{arg sustime, rel;
						Env.new([0, 1, 1, 0], [0.01, sustime, rel], -4, nil);
					}
				]
			],
			 // amp compensation
			TXAmpComp.arrOptionData,
		];
		synthDefFunc = {
			arg out, gate, note, velocity, keytrack, transpose,
			pitchOffset,  pitchbend, pitchbendMin, pitchbendMax,
			detune, detuneMin, detuneMax,
			minStringRatio, minStringRatioMin, minStringRatioMax,
			maxStringRatio, maxStringRatioMin, maxStringRatioMax,
			minStringLength, minStringLengthMin, minStringLengthMax,
			maxStringLength, maxStringLengthMin, maxStringLengthMax,
			radius_L_sigm_shape, radius_R_sigm_shape, core_nucleus_ratio,
			length_L_sigm_shape, length_R_sigm_shape, string_density_mult,
			young_modulus_mult, bridge_impedance_mult, hammer_impedance_mult, mass_hammer_mult,
			force_hammer_mult, hysteresys_hammer_mult, stiffness_hammer_mult, hammer_pos, string_loss,
			level, panMin, panMax,
			sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax,
			intKey,
			modPitchbend = 0, modDetune = 0,
			modMinStringRatio = 0, modMaxStringRatio = 0, modMinStringLength = 0, modMaxStringLength = 0,
			modSustainTime = 0, modRelease = 0;

			var envFunction, outEnv, intonationFunc, outFreq, pbend, outFunction, outPianoString, hammerType,
			hold_detune, hold_minStringRatio, hold_maxStringRatio, hold_minStringLength, hold_maxStringLength,
			sustime, rel, outEnvPiano, ampCompFunction, outAmpComp, velMax;

			hold_detune = detuneMin + ((detuneMax - detuneMin) * (detune + modDetune).max(0).min(1));
			hold_detune = hold_detune * 0.01; // convert from cents
			hold_minStringRatio = LinExp.kr(minStringRatio, 0, 1, minStringRatioMin, minStringRatioMax);
			hold_maxStringRatio = LinExp.kr(maxStringRatio, 0, 1, maxStringRatioMin, maxStringRatioMax);
			hold_minStringLength = LinExp.kr(minStringLength, 0, 1, minStringLengthMin, minStringLengthMax);
			hold_maxStringLength = LinExp.kr(maxStringLength, 0, 1, maxStringLengthMin, maxStringLengthMax);
			pbend = pitchbendMin + ((pitchbendMax - pitchbendMin) * (pitchbend + modPitchbend).max(0).min(1));
			sustime = (sustainTimeMin +
				((sustainTimeMax - sustainTimeMin) * (sustainTime + modSustainTime))).max(0.01).min(20);
			rel = (releaseMin + ((releaseMax - releaseMin) * (release + modRelease))).max(0.01).min(20);
			envFunction = this.getSynthOption(2);
			outEnv = EnvGen.ar(envFunction.value(sustime, rel), gate, doneAction: 2);
			intonationFunc = this.getSynthOption(1);
			outFreq = (intonationFunc.value(
				(note + transpose), intKey) * keytrack) + ((48 + transpose).midicps * (1-keytrack));
			outFreq = outFreq *  (2 ** ((pitchOffset + pbend) /12));
			hammerType = this.getSynthOption(0);

			// limit velocity
			velMax = 1 - (0.35 * ((outFreq.max(80.midicps) - 72.midicps).max(0) / (80.midicps - 72.midicps)));

			// OteyPianoStrings.ar (freq: 440, vel: 1, t_gate: 0, rmin: 0.35, rmax: 2, rampl: 4, rampr: 8,
			// 	rcore: 1,lmin: 0.07, lmax: 1.4, lampl: -4, lampr: 4, rho: 1, e: 1, zb: 1, zh: 0,
			// mh: 1, k: 0.2, alpha: 1, p: 1, hpos: 0.142, loss: 1, detune: 0.0003, hammer_type: 1)

			outPianoString = OteyPianoStrings.ar(
				outFreq,
				(velocity * 0.007874).min(velMax) * 0.5, // scale 1/127 & scale down
				Trig.kr(gate, ControlDur.ir),
				hold_minStringRatio, hold_maxStringRatio,
				radius_L_sigm_shape, radius_R_sigm_shape, core_nucleus_ratio,
				hold_minStringLength, hold_maxStringLength,
				length_L_sigm_shape, length_R_sigm_shape, string_density_mult,
				young_modulus_mult, bridge_impedance_mult, hammer_impedance_mult, mass_hammer_mult,
				force_hammer_mult, hysteresys_hammer_mult, stiffness_hammer_mult, hammer_pos,
				string_loss, hold_detune, hammerType
			);
			outEnvPiano = outEnv * outPianoString * level;
			ampCompFunction = this.getSynthOption(3);
			outAmpComp = ampCompFunction.value(outFreq);
		    Out.ar(out, Pan2.ar(TXClean.ar(outEnvPiano * outAmpComp), LinLin.kr(outFreq, 20.midicps, 80.midicps, panMin, panMax)));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TestNoteVals"],
			["SynthOptionPopupPlusMinus", "hammer type", arrOptionData, 0],
			["EZslider", "hammer pos", classData.specHammerPos, "hammer_pos"],
			["EZslider", "ham imped x", classData.specMult, "hammer_impedance_mult"],
			["EZslider", "mass ham x", classData.specMult, "mass_hammer_mult"],
			["EZslider", "force ham x", classData.specMult, "force_hammer_mult"],
			["EZslider", "hysteresys x", classData.specMult, "hysteresys_hammer_mult"],
			["EZslider", "stiffness  x", classData.specMult, "stiffness_hammer_mult"],
			["SynthOptionPopup", "Env Type", arrOptionData, 2, 300],
			["TXMinMaxSliderSplit", "Sustain time", classData.specTime, "sustainTime", "sustainTimeMin",
				"sustainTimeMax"],
			["TXMinMaxSliderSplit", "Release", classData.specTime, "release", "releaseMin", "releaseMax"],
			["EZslider", "Out level", ControlSpec(0, 1), "level"],
			["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 3],
			["TXRangeSlider", "Stereo width", ControlSpec(-1,1), "panMin", "panMax", nil,
				[["Presets:", []], ["-1, 1 - Widest", [-1, 1]], ["-0.75, 0.75", [-0.75, 0.75]],
					["-0.5, 0.5", [-0.5, 0.5]], ["-0.25, 0.25", [-0.25, 0.25]],
					["-0.05, 0.05", [-0.05, 0.05]], ["0, 0 - Mono", [0, 0]],
			]],

			["TXMinMaxSliderSplit", "detune cents", classData.specDetune, "detune", "detuneMin", "detuneMax"],
			["TXMinMaxSliderSplit", "min str ratio", classData.specRatio,
				"minStringRatio", "minStringRatioMin", "minStringRatioMax"],
			["TXMinMaxSliderSplit", "max str ratio", classData.specRatio,
				"maxStringRatio", "maxStringRatioMin", "maxStringRatioMax"],
			["TXMinMaxSliderSplit", "min str length", classData.specLength,
				"minStringLength", "minStringLengthMin", "minStringLengthMax"],
			["TXMinMaxSliderSplit", "mx str length", classData.specLength,
				"maxStringLength", "maxStringLengthMin", "maxStringLengthMax"],
			["EZslider", "radiusL shape", classData.specShape, "radius_L_sigm_shape"],
			["EZslider", "radiusR shape", classData.specShape, "radius_R_sigm_shape"],
			["EZslider", "core ratio", classData.specRatio, "core_nucleus_ratio"],
			["EZslider", "lengthL shape", classData.specShape, "length_L_sigm_shape"],
			["EZslider", "lengthR shape", classData.specShape, "length_R_sigm_shape"],
			["EZslider", "string density x", classData.specMult, "string_density_mult"],
			["EZslider", "young mod x", classData.specMult, "young_modulus_mult"],
			["EZslider", "bridge imped x", classData.specMult, "bridge_impedance_mult"],
			["EZslider", "string loss", classData.specDefault, "string_loss"],

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
			["ActionButton", "Hammer & Env", {displayOption = "showHammer";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showHammer")],
			["Spacer", 3],
			["ActionButton", "String Model 1", {displayOption = "showString";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showString")],
			["Spacer", 3],
			["ActionButton", "String Model 2", {displayOption = "showString2";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showString2")],
			["Spacer", 3],
			["ActionButton", "MIDI/ Note", {displayOption = "showMIDI";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showHammer", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Hammer type", arrOptionData, 0],
				["SpacerLine", 2],
				["EZslider", "hammer pos", classData.specHammerPos, "hammer_pos"],
				["SpacerLine", 2],
				["EZslider", "ham imped x", classData.specMult, "hammer_impedance_mult"],
				["SpacerLine", 2],
				["EZslider", "mass ham x", classData.specMult, "mass_hammer_mult"],
				["SpacerLine", 2],
				["EZslider", "force ham x", classData.specMult, "force_hammer_mult"],
				["SpacerLine", 2],
				["EZslider", "hysteresys x", classData.specMult, "hysteresys_hammer_mult"],
				["SpacerLine", 2],
				["EZslider", "stiffness x", classData.specMult, "stiffness_hammer_mult"],
				["DividingLine"],
				["SpacerLine", 2],
				["SynthOptionPopup", "Env Type", arrOptionData, 2, 300],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Sustain time", classData.specTime, "sustainTime", "sustainTimeMin",
					"sustainTimeMax"],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Release", classData.specTime,
					"release", "releaseMin", "releaseMax"],
				["DividingLine"],
				["SpacerLine", 2],
				["EZslider", "Out level", ControlSpec(0, 1), "level"],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 3],
				["SpacerLine", 2],
				["TXRangeSlider", "Stereo width", ControlSpec(-1,1), "panMin", "panMax", nil,
					[["Presets:", []], ["-1, 1", [-1, 1]], ["-0.75, 0.75", [-0.75, 0.75]],
						["-0.5, 0.5", [-0.5, 0.5]], ["-0.25, 0.25", [-0.25, 0.25]], ["-0.05, 0.05", [-0.05, 0.05]],
				]],
			];
		});
		if (displayOption == "showString", {
			guiSpecArray = guiSpecArray ++[
				["TXMinMaxSliderSplit", "detune cents", classData.specDetune, "detune", "detuneMin", "detuneMax"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "min str length", classData.specLength,
					"minStringLength", "minStringLengthMin", "minStringLengthMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "max str length", classData.specLength,
					"maxStringLength", "maxStringLengthMin", "maxStringLengthMax"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "min str ratio", classData.specRatio,
					"minStringRatio", "minStringRatioMin", "minStringRatioMax"],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "max str ratio", classData.specRatio,
					"maxStringRatio", "maxStringRatioMin", "maxStringRatioMax"],
				["SpacerLine", 4],
			];
		});
		if (displayOption == "showString2", {
			guiSpecArray = guiSpecArray ++[
				["EZslider", "radiusL shape", classData.specShape, "radius_L_sigm_shape"],
				["SpacerLine", 4],
				["EZslider", "radiusR shape", classData.specShape, "radius_R_sigm_shape"],
				["DividingLine"],
				["SpacerLine", 10],
				["EZslider", "core ratio", classData.specRatio, "core_nucleus_ratio"],
				["DividingLine"],
				["SpacerLine", 10],
				["EZslider", "lengthL shape", classData.specShape, "length_L_sigm_shape"],
				["SpacerLine", 4],
				["EZslider", "lengthR shape", classData.specShape, "length_R_sigm_shape"],
				["DividingLine"],
				["SpacerLine", 10],
				["EZslider", "string density x", classData.specMult, "string_density_mult"],
				["SpacerLine", 4],
				["EZslider", "young modulus x", classData.specMult, "young_modulus_mult"],
				["SpacerLine", 4],
				["EZslider", "bridge imped x", classData.specMult, "bridge_impedance_mult"],
				["DividingLine"],
				["SpacerLine", 10],
				["EZslider", "string loss", classData.specDefault, "string_loss"],
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

