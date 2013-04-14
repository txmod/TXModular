// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFilterSynth2 : TXModuleBase {

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

*initClass{
	arrInstances = [];
	//	set class specific variables
	defaultName = "Filter Synth";
	moduleRate = "audio";
	moduleType = "groupsource";
	arrCtlSCInBusSpecs = [
		["Resonance", 1, "modRes", 0],
		["Saturation", 1, "modSat", 0],
		["Pitch bend", 1, "modPitchbend", 0],
		["Delay", 1, "modDelay", 0],
		["Attack", 1, "modAttack", 0],
		["Decay", 1, "modDecay", 0],
		["Sustain level", 1, "modSustain", 0],
		["Sustain time", 1, "modSustainTime", 0],
		["Release", 1, "modRelease", 0],
	];
	arrAudSCInBusSpecs = [
		 ["Input Signal", 1, "insignal"]
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
	displayOption = "showFilter";
	arrSynthArgSpecs = [
		["insignal", 0, 0],
		["out", 0, 0],
		["gate", 1, 0],
		["note", 0, 0],
		["velocity", 0, 0],
		["keytrack", 1, \ir],
		["transpose", 0, \ir],
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
		["modSustainTime", 0, \ir],
		["modRelease", 0, \ir],
  	];
	arrOptions = [0,0,0,0,0];
	arrOptionData = [
		TXFilter.arrOptionData,
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
		TXLevelControl.arrOptionData,
	];
	synthDefFunc = {
		arg insignal, out, gate, note, velocity, keytrack, transpose, pitchbend, pitchbendMin, pitchbendMax,
			res, resMin, resMax, sat, satMin, satMax,
			level, envtime=0, delay, attack, attackMin, attackMax, decay, decayMin, decayMax, sustain,
			sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax,
			intKey, modPitchbend, modRes, modSat, modDelay, modAttack, modDecay,
			modSustain, modSustainTime, modRelease;
		var outEnv, envFunction, intonationFunc, outFreq, pbend, filterFunc, outFilter, envCurve,
			input, sumres, sumsat, levelControlFunc, del, att, dec, sus, sustime, rel, timeControlSpec;
		sumres =  resMin + ( (resMax - resMin) * (res + modRes).max(0).min(0.9999) );
		sumsat =  satMin + ( (satMax - satMin) * (sat + modSat).max(0).min(1) );
		pbend = pitchbendMin + ((pitchbendMax - pitchbendMin) * (pitchbend + modPitchbend).max(0).min(1));
		del = (delay + modDelay).max(0).min(1);
		att = (attackMin + ((attackMax - attackMin) * (attack + modAttack))).max(0.001).min(20);
		dec = (decayMin + ((decayMax - decayMin) * (decay + modDecay))).max(0.001).min(20);
		sus = (sustain + modSustain).max(0).min(1);
		sustime = (sustainTimeMin +
			((sustainTimeMax - sustainTimeMin) * (sustainTime + modSustainTime))).max(0.001).min(20);
		rel = (releaseMin + ((releaseMax - releaseMin) * (release + modRelease))).max(0.001).min(20);
		envCurve = this.getSynthOption(2);
		envFunction = this.getSynthOption(3);
		outEnv = EnvGen.ar(
			envFunction.value(del, att, dec, sus, sustime, rel, envCurve),
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
			outFreq *  (2 ** (pbend /12)),
			sumres,
			sumsat
		);
		// use TXClean to stop blowups
		Out.ar(out, TXClean.ar(outEnv
			* levelControlFunc.value(outFilter* level * 4 * (velocity * 0.007874), input)
		));
	};
	this.buildGuiSpecArray;
	arrActionSpecs = this.buildActionSpecs([
		["TestNoteVals"],
		["commandAction", "Plot envelope", {this.envPlot;}],
		["SynthOptionPopup", "Filter type.", arrOptionData, 0],
		["TXMinMaxSliderSplit", "Resonance", ControlSpec(0, 1), "res", "resMin", "resMax"],
		["TXMinMaxSliderSplit", "Saturation", ControlSpec(0, 1), "sat", "satMin", "satMax"],
		["EZslider", "Level", ControlSpec(0, 1), "level"],
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
		["TXEnvDisplay", {this.envViewValues;}, {arg view; envView = view;}],
		["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView;}.defer;}],
		["TXMinMaxSliderSplit", "Attack", timeSpec, "attack", "attackMin", "attackMax",
			{{this.updateEnvView;}.defer;}],
		["TXMinMaxSliderSplit", "Decay", timeSpec, "decay", "decayMin", "decayMax",{{this.updateEnvView;}.defer;}],
		["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView;}.defer;}],
		["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime", "sustainTimeMin",
			"sustainTimeMax",{{this.updateEnvView;}.defer;}],
		["TXMinMaxSliderSplit", "Release", timeSpec, "release", "releaseMin", "releaseMax",
			{{this.updateEnvView;}.defer;}],
		["SynthOptionPopup", "Curve", arrOptionData, 2, 200, {system.showView;}],
		["SynthOptionPopup", "Env. Type", arrOptionData, 3, 200],
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
			["SynthOptionPopup", "Filter", arrOptionData, 0],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Resonance", ControlSpec(0, 1), "res", "resMin", "resMax"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Saturation", ControlSpec(0, 1), "sat", "satMin", "satMax"],
			["SpacerLine", 4],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
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
			["TXEnvDisplay", {this.envViewValues;}, {arg view; envView = view;}],
			["NextLine"],
			["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Attack", timeSpec, "attack", "attackMin", "attackMax",
				{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Decay", timeSpec, "decay", "decayMin", "decayMax",
				{{this.updateEnvView;}.defer;}],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime", "sustainTimeMin",
				"sustainTimeMax",{{this.updateEnvView;}.defer;}],
			["TXMinMaxSliderSplit", "Release", timeSpec, "release", "releaseMin", "releaseMax",
				{{this.updateEnvView;}.defer;}],
			["NextLine"],
			["SynthOptionPopup", "Curve", arrOptionData, 2, 200, {system.showView;}],
			["NextLine"],
			["SynthOptionPopup", "Env. Type", arrOptionData, 3, 200],
			["Spacer", 4],
			["ActionButton", "Plot", {this.envPlot;}],
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

envPlot {
	var del, att, dec, sus, sustime, rel, envCurve;
	del = this.getSynthArgSpec("delay");
	att = this.getSynthArgSpec("attack");
	dec = this.getSynthArgSpec("decay");
	sus = this.getSynthArgSpec("sustain");
	sustime = this.getSynthArgSpec("sustainTime");
	rel = this.getSynthArgSpec("release");
	envCurve = this.getSynthOption(2);
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

