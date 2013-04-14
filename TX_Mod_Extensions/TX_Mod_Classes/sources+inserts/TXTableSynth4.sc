// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXTableSynth4 : TXModuleBase {

	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar	<arrBufferSpecs;
	classvar	<guiWidth=500;
	classvar	timeSpec;

	var	displayOption;
	var ratioView;
	var	envView, envView2;
	var arrWaveSpecs;
	var arrSlotData;
	var <>testMIDINote = 69;
	var <>testMIDIVel = 100;
	var <>testMIDITime = 1;

*initClass{
	arrInstances = [];
	//	set class specific variables
	defaultName = "Table Synth";
	moduleRate = "audio";
	moduleType = "groupsource";
	arrAudSCInBusSpecs = [
		 ["FM input", 1, "inmodulator"]
	];
	arrCtlSCInBusSpecs = [
		["Table position", 1, "modTablePosition", 0],
		["Pitch bend", 1, "modPitchbend", 0],
		["Vol env delay", 1, "modDelay", 0],
		["Vol env attack", 1, "modAttack", 0],
		["Vol env decay", 1, "modDecay", 0],
		["Vol env sustain level", 1, "modSustain", 0],
		["Vol env sustain time", 1, "modSustainTime", 0],
		["Vol env release", 1, "modRelease", 0],
		["Table env amount", 1, "modEnv2Amount", 0],
		["Table env delay", 1, "modDelay2", 0],
		["Table env attack", 1, "modAttack2", 0],
		["Table env decay", 1, "modDecay2", 0],
		["Table env sustain level", 1, "modSustain2", 0],
		["Table env sustain time", 1, "modSustainTime2", 0],
		["Table env release", 1, "modRelease2", 0],
	];
	noOutChannels = 1;
	arrOutBusSpecs = [
		["Out", [0]]
	];
	arrBufferSpecs = [ ["bufnumFirstTable", 1024, 1, 8] ]; // allocate 8 consecutive mono buffers
	timeSpec = ControlSpec(0.001, 20);
}

*new{ arg argInstName;
	 ^super.new.init(argInstName);
}

init {arg argInstName;
	//	set  class specific instance variables
	displayOption = "showWavetables";
	arrSynthArgSpecs = [
		["inmodulator", 0, 0],
		["out", 0, 0],
		["gate", 1, 0],
		["note", 0, 0],
		["velocity", 0, 0],
		["keytrack", 1, \ir],
		["transpose", 0, \ir],
		["maxHarmonicsInd", 1, \ir],
		["harmonicGap", 1, \ir],
		["scaling", 1, \ir],
		["pitchbend", 0.5, defLagTime],
		["pitchbendMin", -2, defLagTime],
		["pitchbendMax", 2, defLagTime],
		["bufnumFirstTable", 0, \ir],
		["tablePosition", 0, defLagTime],
		["tablePositionMin", 0, defLagTime],
		["tablePositionMax", 16, defLagTime],
		["level", 0.5, defLagTime],
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
		["env2Amount", 1, \ir],
		["envtime2", 0, \ir],
		["delay2", 0, \ir],
		["attack2", 0.005, \ir],
		["attackMin2", 0, \ir],
		["attackMax2", 5, \ir],
		["decay2", 0.05, \ir],
		["decayMin2", 0, \ir],
		["decayMax2", 5, \ir],
		["sustain2", 1, \ir],
		["sustainTime2", 0.2, \ir],
		["sustainTimeMin2", 0, \ir],
		["sustainTimeMax2", 5, \ir],
		["release2", 0.01, \ir],
		["releaseMin2", 0, \ir],
		["releaseMax2", 5, \ir],
		["intKey", 0, \ir],
		["modPitchbend", 0, defLagTime],
		["modTablePosition", 0, defLagTime],
		["modDelay", 0, \ir],
		["modAttack", 0, \ir],
		["modDecay", 0, \ir],
		["modSustain", 0, \ir],
		["modSustainTime", 0, \ir],
		["modRelease", 0, \ir],
		["modEnv2Amount", 0, \ir],
		["modDelay2", 0, \ir],
		["modAttack2", 0, \ir],
		["modDecay2", 0, \ir],
		["modSustain2", 0, \ir],
		["modSustainTime2", 0, \ir],
		["modRelease2", 0, \ir],
  	];
	arrOptions = [0,0,0,0,0,0,0,0];
	arrOptionData = [
		[
			["None", {arg val; val;}],
			["Step size 0.2", {arg val; Lag.kr(val.round(0.2), 0.03);}],
			["Step size 0.25", {arg val; Lag.kr(val.round(0.25), 0.03);}],
			["Step size 0.333", {arg val; Lag.kr(val.round(0.333333333), 0.03);}],
			["Step size 0.5", {arg val; Lag.kr(val.round(0.5), 0.03);}],
			["Step size 1", {arg val; Lag.kr(val.round(1), 0.03);}],
		],
		TXIntonation.arrOptionData,
		TXEnvLookup.arrSlopeOptionData,
		TXEnvLookup.arrSustainOptionData,
		[
			["No Frequency Modulation",
				{arg inmodulator;
					0;
				}
			],
			["Frequency Modulation using FM input",
				{arg inmodulator;
					InFeedback.ar(inmodulator,1);
				}
			]
		],
		// table env
		TXEnvLookup.arrSlopeOptionData,
		TXEnvLookup.arrSustainOptionData,
		[	["Off", { 0; }],
			["On", {arg envFunction, del, att, dec, sus, sustime, rel, envCurve, gate;
				EnvGen.kr(
					envFunction.value(del, att, dec, sus, sustime, rel, envCurve),
					gate,
					doneAction: 0
				);
			}],
		],
	];
	synthDefFunc = {
		arg inmodulator, out, gate, note, velocity, keytrack, transpose, maxHarmonicsInd, harmonicGap, scaling,
			pitchbend, pitchbendMin, pitchbendMax,
			bufnumFirstTable, tablePosition, tablePositionMin, tablePositionMax, level,
			envtime=0, delay, attack, attackMin, attackMax, decay, decayMin, decayMax, sustain,
			sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax,
			env2Amount, envtime2=0, delay2, attack2, attackMin2, attackMax2, decay2, decayMin2, decayMax2, sustain2,
			sustainTime2, sustainTimeMin2, sustainTimeMax2, release2, releaseMin2, releaseMax2,
			intKey, modPitchbend, modTablePosition,
			modDelay, modAttack, modDecay, modSustain, modSustainTime, modRelease,
			modEnv2Amount, modDelay2, modAttack2, modDecay2, modSustain2, modSustainTime2, modRelease2;
		var	outEnv, outEnv2, envFunction, envFunction2, envCurve, envCurve2, envGenFunction2,
			del, att, dec, sus, sustime, rel, del2, att2, dec2, sus2, sustime2, rel2, holdEnv2Amount,
			stepFunc, intonationFunc, freqModFunc, outFreq, pbend, outWave, tablePos;

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


		holdEnv2Amount = (env2Amount + modEnv2Amount).max(-1).min(1);
		del2 = (delay2 + modDelay2).max(0).min(1);
		att2 = (attackMin2 + ((attackMax2 - attackMin2) * (attack2 + modAttack2))).max(0.001).min(20);
		dec2 = (decayMin2 + ((decayMax2 - decayMin2) * (decay2 + modDecay2))).max(0.001).min(20);
		sus2 = (sustain2 + modSustain2).max(0).min(1);
		sustime2 = (sustainTimeMin2 +
			((sustainTimeMax2 - sustainTimeMin2) * (sustainTime2 + modSustainTime2))).max(0.001).min(20);
		rel2 = (releaseMin2 + ((releaseMax2 - releaseMin2) * (release2 + modRelease2))).max(0.001).min(20);
		envCurve2 = this.getSynthOption(5);
		envFunction2 = this.getSynthOption(6);
		envGenFunction2 = this.getSynthOption(7);
		outEnv2 = holdEnv2Amount * envGenFunction2.value(envFunction2, del2, att2, dec2, sus2, sustime2, rel2, envCurve2, gate);

		tablePos = tablePositionMin + ((tablePositionMax - tablePositionMin)
			* (tablePosition + modTablePosition + outEnv2).max(0).min(1));
		pbend = pitchbendMin + ((pitchbendMax - pitchbendMin) * (pitchbend + modPitchbend).max(0).min(1));

		stepFunc = this.getSynthOption(0);
		intonationFunc = this.getSynthOption(1);
		freqModFunc = this.getSynthOption(4);
		outFreq = (intonationFunc.value((note + transpose), intKey) * keytrack) + ((48 + transpose).midicps * (1-keytrack));
		outWave = VOsc.ar(
			bufnumFirstTable + (stepFunc.value(tablePos) - 1).max(0.0001).min(6.999),
			outFreq * (2 ** (pbend /12)),
			freqModFunc.value(inmodulator) * 2pi
		);
		// amplitude is vel *  0.00315 approx. == 1 / 127
		// use TXClean to stop blowups
		Out.ar(out, TXClean.ar(outEnv * outWave * level * (velocity * 0.007874)));
	};
	this.buildGuiSpecArray;
	arrActionSpecs = this.buildActionSpecs([
		["TestNoteVals"],
		["commandAction", "Plot envelope", {this.envPlot;}],
		["TXMinMaxSliderSplit", "Table position", ControlSpec(1, 8),
			"tablePosition", "tablePositionMin", "tablePositionMax"],
		["SynthOptionPopup", "Stepping", arrOptionData, 0, 200],
		["TXWaveTableSpecs", "Wavetables", {arrWaveSpecs},
			{arg view; arrWaveSpecs = view.value;
				arrSlotData = view.arrSlotData; this.updateBuffers(view.value);},
			{arrSlotData}],
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
		["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView2}.defer;}],
		["TXMinMaxSliderSplit", "Attack", timeSpec, "attack", "attackMin", "attackMax",{{this.updateEnvView2}.defer;}],
		["TXMinMaxSliderSplit", "Decay", timeSpec, "decay", "decayMin", "decayMax",{{this.updateEnvView2}.defer;}],
		["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView2}.defer;}],
		["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime", "sustainTimeMin",
			"sustainTimeMax",{{this.updateEnvView2}.defer;}],
		["TXMinMaxSliderSplit", "Release", timeSpec, "release", "releaseMin", "releaseMax",{{this.updateEnvView2}.defer;}],
		["SynthOptionPopup", "Curve", arrOptionData, 2, 200, {system.showView;}],
		["SynthOptionPopup", "Env. Type", arrOptionData, 3, 200],
		["SynthOptionCheckBox", "Table Env", arrOptionData, 7, 120],
		["EZslider", "Env amount", ControlSpec(-1, 1), "env2Amount", nil, 260],
		["TXEnvDisplay", {this.envViewValues2;}, {arg view; envView2 = view;}],
		["EZslider", "Pre-Delay2", ControlSpec(0,1), "delay2", {{this.updateEnvView2}.defer;}],
		["TXMinMaxSliderSplit", "Attack2", timeSpec, "attack2", "attackMin2", "attackMax2",{this.updateEnvView2;}],
		["TXMinMaxSliderSplit", "Decay2", timeSpec, "decay2", "decayMin2", "decayMax2",{this.updateEnvView2;}],
		["EZslider", "Sustain level2", ControlSpec(0, 1), "sustain2", {this.updateEnvView2;}],
		["TXMinMaxSliderSplit", "Sustain time2", timeSpec, "sustainTime2", "sustainTimeMin2",
			"sustainTimeMax2",{this.updateEnvView2;}],
		["TXMinMaxSliderSplit", "Release2", timeSpec, "release2", "releaseMin2", "releaseMax2",{this.updateEnvView2;}],
		["SynthOptionPopup", "Curve2", arrOptionData, 5, 180, {system.showView;}],
		["SynthOptionPopup", "Env. Type2", arrOptionData, 6, 180],
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
	//	make buffers, load the synthdef and create the Group for synths to belong to
	this.makeBuffersAndGroup(arrBufferSpecs);
	//	initialise buffers
	8.do({arg item, i;
		arrWaveSpecs = arrWaveSpecs.add(
			(	(Harmonics(32).decay .copyRange(0, i * 3)
					++ Array.newClear(32).fill(0)
				).copyRange(0, 31)
			).max(0).min(1)
		);
	});
	Routine.run {
		var holdModCondition;
		// add condition to load queue
		holdModCondition = system.holdLoadQueue.addCondition;
		// pause
		holdModCondition.wait;
		system.server.sync;
		// update buffers
		this.updateBuffers(arrWaveSpecs);
		// remove condition from load queue
		system.holdLoadQueue.removeCondition(holdModCondition);
	};
	//	initialise slots
	arrSlotData = arrWaveSpecs.dup(5);
	//	overwrite default preset
	this.overwritePreset(this, "Default Settings", 0);
}

buildGuiSpecArray {
	guiSpecArray = [
		["ActionButton", "Wavetables", {displayOption = "showWavetables";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showWavetables")],
		["Spacer", 3],
		["ActionButton", "Processes", {displayOption = "showProcesses";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showProcesses")],
		["Spacer", 3],
		["ActionButton", "MIDI/ Note", {displayOption = "showMIDI";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
		["Spacer", 3],
		["ActionButton", "Vol Env", {displayOption = "showEnv";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showEnv")],
		["Spacer", 3],
		["ActionButton", "Table Env", {displayOption = "showEnv2";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showEnv2")],
		["Spacer", 3],
		["ActionButton", "Frequency Mod", {displayOption = "showModOptions";
			this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showModOptions")],
		["DividingLine"],
		["SpacerLine", 6],
	];
	if (displayOption == "showWavetables", {
		guiSpecArray = guiSpecArray ++[
			["TXMinMaxSliderSplit", "Table position", ControlSpec(1, 8),
				"tablePosition", "tablePositionMin", "tablePositionMax"],
			["SynthOptionPopup", "Stepping", arrOptionData, 0, 320],
			["SpacerLine", 4],
			["EZNumber", "Harmonic gap", ControlSpec(0, 3), "harmonicGap",
				{this.updateBuffers(arrWaveSpecs);}, nil, 76, 0.01],
			["Spacer", 10],
			["ActionButton", "Set to 1 (default)",
				{this.setSynthArgSpec("harmonicGap", 1); system.flagGuiIfModDisplay(this)}, 100],
			["SpacerLine", 4],
			["EZNumber", "Scaling", ControlSpec(1, 5), "scaling",
				{this.updateBuffers(arrWaveSpecs);}, nil, 76, 0.1],
			["Spacer", 10],
			["ActionButton", "Set to 1 (default)",
				{this.setSynthArgSpec("scaling", 1); system.flagGuiIfModDisplay(this)}, 100],
			["SpacerLine", 4],
			["TXPopupAction", "No. harmonics", ["8", "16", "24", "32"], "maxHarmonicsInd",
				{this.updateBuffers(arrWaveSpecs); system.showView;}, 160],
			["NextLine"],
			["SpacerLine", 2],
			["TXWaveTableSpecs", "Wavetables", {arrWaveSpecs},
				{arg view; arrWaveSpecs = view.value;
					arrSlotData = view.arrSlotData; this.updateBuffers(view.value);},
				{arrSlotData}, {[8, 16, 24, 32].at(this.getSynthArgSpec("maxHarmonicsInd"))}, 0],
			["SpacerLine", 4],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
		];
	});
	if (displayOption == "showProcesses", {
		guiSpecArray = guiSpecArray ++[
			["TXWaveTableSpecs", "Wavetables", {arrWaveSpecs},
				{arg view; arrWaveSpecs = view.value;
					arrSlotData = view.arrSlotData; this.updateBuffers(view.value);},
				{arrSlotData}, {[8, 16, 24, 32].at(this.getSynthArgSpec("maxHarmonicsInd"))}, 1],
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
//			["TestNoteVals"],
			["SynthOptionPopupPlusMinus", "Intonation", arrOptionData, 1, 300,
				{arg view; this.updateIntString(view.value)}],
			["Spacer", 10],
			["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F",
				"F#", "G", "G#", "A", "A#", "B"], "intKey", nil, 140],
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
			["EZslider", "Pre-Delay", ControlSpec(0,1), "delay", {{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Attack", timeSpec, "attack", "attackMin", "attackMax",{{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Decay", timeSpec, "decay", "decayMin", "decayMax",{{this.updateEnvView}.defer;}],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain", {{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime", "sustainTimeMin",
				"sustainTimeMax",{{this.updateEnvView}.defer;}],
			["TXMinMaxSliderSplit", "Release", timeSpec, "release", "releaseMin", "releaseMax",{{this.updateEnvView}.defer;}],
			["NextLine"],
			["SynthOptionPopup", "Curve", arrOptionData, 2, 200, {system.showView;}],
			["NextLine"],
			["SynthOptionPopup", "Env. Type", arrOptionData, 3, 200],
			["Spacer", 4],
			["ActionButton", "Plot", {this.envPlot;}],
		];
	});
	if (displayOption == "showEnv2", {
		guiSpecArray = guiSpecArray ++[
			["SynthOptionCheckBox", "Table Env", arrOptionData, 7, 120],
			["NextLine"],
			["EZslider", "Env amount", ControlSpec(-1, 1), "env2Amount", nil, 260],
			["NextLine"],
			["TXPresetPopup", "Env presets",
				TXEnvPresets.arrEnvPresets2(this, 5, 6).collect({arg item, i; item.at(0)}),
				TXEnvPresets.arrEnvPresets2(this, 5, 6).collect({arg item, i; item.at(1)})
			],
			["TXEnvDisplay", {this.envViewValues2;}, {arg view; envView2 = view;}],
			["NextLine"],
			["EZslider", "Pre-Delay", ControlSpec(0,1), "delay2", {this.updateEnvView2;}],
			["TXMinMaxSliderSplit", "Attack", timeSpec, "attack2", "attackMin2", "attackMax2",{this.updateEnvView2;}],
			["TXMinMaxSliderSplit", "Decay", timeSpec, "decay2", "decayMin2", "decayMax2",{this.updateEnvView2;}],
			["EZslider", "Sustain level", ControlSpec(0, 1), "sustain2", {this.updateEnvView2;}],
			["TXMinMaxSliderSplit", "Sustain time", timeSpec, "sustainTime2", "sustainTimeMin2",
				"sustainTimeMax2",{this.updateEnvView2;}],
			["TXMinMaxSliderSplit", "Release", timeSpec, "release2", "releaseMin2", "releaseMax2",{this.updateEnvView2;}],
			["NextLine"],
			["SynthOptionPopup", "Curve", arrOptionData, 5, 180, {system.showView;}],
			["SynthOptionPopup", "Env. Type", arrOptionData, 6, 180],
			["ActionButton", "Plot", {this.envPlot2;}],
		];
	});
	if (displayOption == "showModOptions", {
		guiSpecArray = guiSpecArray ++[
			["SynthOptionPopup", "FM option", arrOptionData, 4],
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

updateBuffers { arg arrSpecs;
	var holdNoHarmonics, holdScaling;
	if (arrSpecs.notNil, {
		// only generate required no of harmonics
		holdNoHarmonics = [8, 16, 24, 32].at(this.getSynthArgSpec("maxHarmonicsInd"));
		// use scaling
		holdScaling = this.getSynthArgSpec("scaling");
		// generate wavetables in buffers
		buffers.do({arg item, i;
			var holdSpec, holdFreqs;
			holdSpec = ((arrSpecs.at(i).keep(holdNoHarmonics.asInteger) ++ (0!32)).keep(32))
				// first harmonic is > 0 as all zero's would crash method
				.max(([0.0001] ++  (0!32)).keep(32));
			// apply scaling
			holdSpec = holdSpec ** holdScaling;
			// generate wavetables
			holdFreqs = 32.collect({arg item, i; 1 + (item * this.getSynthArgSpec("harmonicGap"));});
			item.sine2(holdFreqs, holdSpec);
		});
	});
}

extraSaveData { // override default method
	^[arrWaveSpecs, arrSlotData, testMIDINote, testMIDIVel, testMIDITime];
}

loadExtraData {arg argData;  // override default method
	arrWaveSpecs = argData.at(0);
	Routine.run {
		var holdModCondition;
		// add condition to load queue
		holdModCondition = system.holdLoadQueue.addCondition;
		// pause
		holdModCondition.wait;
		system.server.sync;
		// update buffers
		this.updateBuffers(arrWaveSpecs);
		// remove condition from load queue
		system.holdLoadQueue.removeCondition(holdModCondition);
	};
	arrSlotData = argData.at(1);
	testMIDINote = argData.at(2);
	testMIDIVel = argData.at(3);
	testMIDITime = argData.at(4);
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

envPlot2 {
	var del, att, dec, sus, sustime, rel, envCurve;
	del = this.getSynthArgSpec("delay2");
	att = this.getSynthArgSpec("attack2");
	dec = this.getSynthArgSpec("decay2");
	sus = this.getSynthArgSpec("sustain2");
	sustime = this.getSynthArgSpec("sustainTime2");
	rel = this.getSynthArgSpec("release2");
	envCurve = this.getSynthOption(5);
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

envViewValues2 {
	var attack, attackMin, attackMax, decay, decayMin, decayMax, sustain;
	var sustainTime, sustainTimeMin, sustainTimeMax, release, releaseMin, releaseMax;
	var del, att, dec, sus, sustime, rel;
	var arrTimesNorm, arrTimesNormedSummed;

	del = this.getSynthArgSpec("delay2");
	attack = this.getSynthArgSpec("attack2");
	attackMin = this.getSynthArgSpec("attackMin2");
	attackMax = this.getSynthArgSpec("attackMax2");
	att = attackMin + ((attackMax - attackMin) * attack);
	decay = this.getSynthArgSpec("decay2");
	decayMin = this.getSynthArgSpec("decayMin2");
	decayMax = this.getSynthArgSpec("decayMax2");
	dec = decayMin + ((decayMax - decayMin) * decay);
	sus = this.getSynthArgSpec("sustain2");
	sustainTime = this.getSynthArgSpec("sustainTime2");
	sustainTimeMin = this.getSynthArgSpec("sustainTimeMin2");
	sustainTimeMax = this.getSynthArgSpec("sustainTimeMax2");
	sustime = sustainTimeMin + ((sustainTimeMax - sustainTimeMin) * sustainTime);
	release = this.getSynthArgSpec("release2");
	releaseMin = this.getSynthArgSpec("releaseMin2");
	releaseMax = this.getSynthArgSpec("releaseMax2");
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

updateEnvView2 {
	if (envView2.respondsTo('notClosed'), {
		if (envView2.notClosed, {
			6.do({arg i;
				envView2.setEditable(i, true);
			});
			envView2.value = this.envViewValues2;
			6.do({arg i;
				envView2.setEditable(i, false);
			});
		});
	});
}
}

