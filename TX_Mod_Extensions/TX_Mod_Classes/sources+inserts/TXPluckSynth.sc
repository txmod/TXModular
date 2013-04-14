// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXPluckSynth : TXModuleBase {

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
	var ratioView;
	var	envView;
	var <>testMIDINote = 69;
	var <>testMIDIVel = 100;
	var <>testMIDITime = 1;
	var	arrEnvPresetNames, arrEnvPresetActions;

*initClass{
	arrInstances = [];
	//	set class specific variables
	defaultName = "Pluck Synth";
	moduleRate = "audio";
	moduleType = "groupsource";
	arrCtlSCInBusSpecs = [
		["Pick noise", 1, "modNoiseshape", 0],
		["Filtering", 1, "modFiltershape", 0],
		["Pitch bend", 1, "modPitchbend", 0],
		["Decay", 1, "modDecay", 0],
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
	displayOption = "showPluck";
	arrSynthArgSpecs = [
		["out", 0, 0],
		["gate", 1, 0],
		["note", 0, 0],
		["velocity", 0, 0],
		["keytrack", 1, \ir],
		["transpose", 0, \ir],
		["pitchbend", 0.5, defLagTime],
		["pitchbendMin", -2, defLagTime],
		["pitchbendMax", 2, defLagTime],
		["filtershape", 0.1, defLagTime],
		["filtershapeMin", 0, defLagTime],
		["filtershapeMax", 0.9, defLagTime],
		["noiseshape", 0.5, defLagTime],
		["noiseshapeMin", 0, defLagTime],
		["noiseshapeMax", 1, defLagTime],
		["level", 0.5, defLagTime],
		["envtime", 0, \ir],
		["decay", 0.5, \ir],
		["decayMin", 0, \ir],
		["decayMax", 5, \ir],
		["release", 0.05, \ir],
		["releaseMin", 0, \ir],
		["releaseMax", 5, \ir],
		["intKey", 0, \ir],
		["modPitchbend", 0, defLagTime],
		["modFiltershape", 0, defLagTime],
		["modNoiseshape", 0, defLagTime],
		["modDecay", 0, \ir],
		["modRelease", 0, \ir],
  	];
	arrOptions = [0,0,0,0,0];
	arrOptionData = [
		[[ "off", 1 ],  [ "on", -1 ]],
		TXIntonation.arrOptionData,
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
		[
			["Noise:    Pink Noise <-> White Noise",
				{arg outFreq, outNoiseshape;
					(PinkNoise.ar(0.75) * (1-outNoiseshape))
						+ (WhiteNoise.ar(0.5) * outNoiseshape);
				}
			],
			["Waveform:    Square <-> Sawtooth",
				{arg outFreq, outNoiseshape;
					(Pulse.ar(outFreq, 0.5, 0.5) * (1-outNoiseshape))
						+ (Saw.ar(outFreq, 0.5) * outNoiseshape);
				}
			],
			["Waveform + distortion:    Square <-> Sawtooth",
				{arg outFreq, outNoiseshape;
					((Pulse.ar(outFreq, 0.5, 0.5) * (1-outNoiseshape))
						+ (Saw.ar(outFreq, 0.5) * outNoiseshape)).distort;
				}
			],
			["Waveform + soft clipping:    Square <-> Sawtooth",
				{arg outFreq, outNoiseshape;
					((Pulse.ar(outFreq, 0.5, 0.5) * (1-outNoiseshape))
						+ (Saw.ar(outFreq, 0.5) * outNoiseshape)).softclip;
				}
			]
		],

	];
	synthDefFunc = {
		arg out, gate, note, velocity, keytrack, transpose, pitchbend, pitchbendMin, pitchbendMax,
			filtershape, filtershapeMin, filtershapeMax, noiseshape, noiseshapeMin, noiseshapeMax,
			level, envtime=0, decay, decayMin, decayMax,
			release, releaseMin, releaseMax,
			intKey, modPitchbend, modFiltershape, modNoiseshape, modDecay, modRelease;
		var outEnv, envFunction, intonationFunc, outFreq, pbend, outPluck, envCurve,
			outFiltershape, outNoiseshape, dec, rel, pickFunction, outPick, oddharms;
		outFiltershape = filtershapeMin +
			((filtershapeMax - filtershapeMin) * (filtershape + modFiltershape).max(-1).min(1));
		outNoiseshape = noiseshapeMin +
			((noiseshapeMax - noiseshapeMin) * (noiseshape + modNoiseshape).max(0).min(1));
		pbend = pitchbendMin + ((pitchbendMax - pitchbendMin) * (pitchbend +
			modPitchbend).max(0).min(1));
		dec = (decayMin + ((decayMax - decayMin) * (decay + modDecay))).max(0.001).min(20);
		rel = (releaseMin + ((releaseMax - releaseMin) * (release + modRelease))).max(0.001).min(20);
		envCurve = this.getSynthOption(2);
		envFunction = this.getSynthOption(3);
		outEnv = EnvGen.ar(
			envFunction.value(0, 0, dec, 0, 1, rel, envCurve),
			gate,
			doneAction: 2
		);
		intonationFunc = this.getSynthOption(1);
		outFreq = (intonationFunc.value(
			(note + transpose), intKey) * keytrack) + ((48 + transpose).midicps * (1-keytrack));
		pickFunction = this.getSynthOption(4);
		outPick = (pickFunction.value(outFreq, outNoiseshape));
		oddharms = this.getSynthOption(0);
		outPluck = Pluck.ar(outPick, 1, 8.reciprocal,
			(outFreq *  (2 ** (pbend /12))).reciprocal, dec * oddharms, outFiltershape);
		Out.ar(out, TXClean.ar(outEnv * outPluck * level * (velocity * 0.007874) ));
	};
	this.buildGuiSpecArray;
	arrActionSpecs = this.buildActionSpecs([
		["TestNoteVals"],
		["SynthOptionPopupPlusMinus", "Pick type", arrOptionData, 4, nil, {system.flagGuiUpd}],
		["TXMinMaxSliderSplit", "Pick morph", \unipolar, "noiseshape", "noiseshapeMin", "noiseshapeMax"],
		["TXMinMaxSliderSplit", "Filtering", ControlSpec(-1, 1), "filtershape", "filtershapeMin", "filtershapeMax", nil,
			[["Presets:", []], ["range 0 to 1", [0, 1]], ["range 0 to 0.9", [0, 0.9]], ["range 0 to 0.1", [0, 0.1]],
			["range -0.1 to 0", [-0.1, 0]], ]
		],
		["SynthOptionCheckBox", "Boost odd partials", arrOptionData, 0],
		["EZslider", "Level", ControlSpec(0, 1), "level"],
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
		["TXMinMaxSliderSplit", "Decay", timeSpec, "decay", "decayMin", "decayMax",
			{{this.updateEnvView;}.defer;}],
		["TXMinMaxSliderSplit", "Release", timeSpec, "release", "releaseMin", "releaseMax",
			{{this.updateEnvView;}.defer;}],
		["SynthOptionPopup", "Curve", arrOptionData, 2, 200, {system.showView;}],
		["SynthOptionPopup", "Env. Type", arrOptionData, 3, 200],
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
		["ActionButton", "Pluck/ Env", {displayOption = "showPluck";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showPluck")],
		["Spacer", 3],
		["ActionButton", "MIDI/ Note", {displayOption = "showMIDI";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
		["DividingLine"],
		["SpacerLine", 6],
	];
	if (displayOption == "showPluck", {
		guiSpecArray = guiSpecArray ++[
			["SynthOptionPopupPlusMinus", "Pick type", arrOptionData, 4, nil, {system.flagGuiUpd}],
			["TXMinMaxSliderSplit", "Pick morph", \unipolar, "noiseshape", "noiseshapeMin", "noiseshapeMax"],
			["TXMinMaxSliderSplit", "Filtering", ControlSpec(-1, 1), "filtershape", "filtershapeMin", "filtershapeMax", nil,
				[["Presets:", []], ["range 0 to 1", [0, 1]], ["range 0 to 0.9", [0, 0.9]], ["range 0 to 0.1", [0, 0.1]],
				["range -0.1 to 0", [-0.1, 0]], ]
			],
			["SynthOptionCheckBox", "Boost odd partials", arrOptionData, 0],
			["DividingLine"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Decay", timeSpec, "decay", "decayMin", "decayMax",
				{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Release", timeSpec, "release", "releaseMin", "releaseMax",
				{{this.updateEnvView;}.defer;}],
			["NextLine"],
			["SynthOptionPopup", "Curve", arrOptionData, 2, 200, {system.showView;}],
			["NextLine"],
			["SynthOptionPopup", "Env. Type", arrOptionData, 3, 200],
			["DividingLine"],
			["SpacerLine", 4],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
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
	if (envView.class == EnvelopeView, {
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

