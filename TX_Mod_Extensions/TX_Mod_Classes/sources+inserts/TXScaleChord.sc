// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXScaleChord : TXModuleBase {

	classvar <>classData;

	var	moduleRunning;
	var	displayOption;
	var oscControlFunc;

	var sendTrigID_Active;
	var sendTrigID_PlayChord;
	var sendTrigID_Inversion;
	var sendTrigID_OpenChord;
	var sendTrigID_TransposeOcts;
	var sendTrigID_GlobalVelScale;
	var sendTrigID_RandVel;

	var holdModActive = 0;
	var holdModPlayChord = 0;
	var holdModInversion = 0;
	var holdModOpenChord = 0;
	var holdModTransposeOcts = 0;
	var holdModGlobalVelScale = 0;
	var holdModRandVel = 0;

	var <>noteOutModule1;
	var <>noteOutModule2;
	var <>noteOutModule3;
	var <>noteOutModule4;
	var <>noteOutModule5;
	var <>noteOutModule6;
	var	<>testMIDIVel = 100;
	var midiControlResp;
	var holdPortNames, holdMIDIOutPort, holdMIDIDeviceName, holdMIDIPortName;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Scale Chord";
		classData.moduleRate = "control";
		classData.moduleType = "groupaction";
		classData.arrCtlSCInBusSpecs = [
			["Active", 1, "modActive", 0],
			["Play Chord", 1, "modPlayChord", 0],
			["Inversion", 1, "modInversion", 0],
			["Open Chord", 1, "modOpenChord", 0],
			["Transp octs", 1, "modTransposeOcts", 0],
			["Velocity scale", 1, "modGlobalVelScale", 0],
			["Randomise vel", 1, "modRandVel", 0],
		];
		classData.noInChannels = 0;
		classData.noOutChannels = 0;
		classData.guiWidth = 860;
		classData.scaleNames = [ 'aeolian', 'ahirbhairav', 'augmented', 'augmented2', 'bartok', 'bhairav',
			'chinese', 'chromatic', 'diminished', 'diminished2', 'dorian', 'egyptian', 'enigmatic', 'gong',
			'harmonicMajor', 'harmonicMinor', 'hexAeolian', 'hexDorian', 'hexMajor6', 'hexMajor7', 'hexPhrygian',
			'hexSus', 'hindu', 'hirajoshi', 'hungarianMinor', 'indian', 'ionian', 'iwato', 'jiao', 'kumoi' ,
			'leadingWhole', 'locrian', 'locrianMajor', 'lydian', 'lydianMinor', 'major', 'majorPentatonic', 'marva',
			'melodicMajor', 'melodicMinor', 'melodicMinorDesc', 'minor', 'minorPentatonic', 'mixolydian',
			'neapolitanMajor', 'neapolitanMinor', 'pelog', 'phrygian', 'prometheus', 'purvi', 'ritusen',
			'romanianMinor', 'scriabin', 'shang', 'spanish', 'superLocrian', 'todi', 'whole', 'yu', 'zhi'
		];
		classData.chordPresets = [
			["select notes...", []],
			["Triad [1,3,5]",  ["note1On", "note3On", "note5On",]],
			["Triad + 2nd [1,2,3,5]",  ["note1On", "note2On", "note3On", "note5On",]],
			["Triad + 4th [1,3,4,5]",  ["note1On", "note3On", "note4On", "note5On",]],
			["Triad + 6th [1,3,4,5,6]",  ["note1On", "note3On", "note5On", "note6On", ]],
			["Triad + 7th[1,3,5,7],",  ["note1On", "note3On", "note5On", "note7On", ]],
			["Triad + Oct Up [1,3,5,octUp]",  ["note1On", "note3On", "note5On", "noteOctUpOn",]],
			["Triad + Oct Down [1,3,5,octDown]",  ["note1On", "note3On", "note5On", "noteOctDownOn",]],
			["OctUp [1,octUp]",  ["note1On", "noteOctUpOn",]],
			["OctDown [1,octDown]",  ["note1On", "noteOctDownOn",]],
			["OctUpDown [1,octUp,octDown]",  ["note1On", "noteOctUpOn", "noteOctDownOn",]],
			["5th [1,5]",  ["note1On", "note5On", ]],
			["5th + Oct Up [1,5,octUp]",  ["note1On", "note5On", "noteOctUpOn",]],
			["5th + Oct Down [1,5,octDown]",  ["note1On", "note5On", "noteOctDownOn",]],
			["All notes",  ["note1On", "note2On", "note3On", "note4On", "note5On", "note6On", "note7On",
				"note8On", "note9On", "note10On", "note11On", "note12On", "noteOctUpOn", "noteOctDownOn"]],
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	*restoreAllOutputs {
		classData.arrInstances.do({ arg item, i;
			item.restoreOutputs;
		});
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showOutputs";
		moduleRunning = true;
		sendTrigID_Active = UniqueID.next;
		sendTrigID_PlayChord = UniqueID.next;
		sendTrigID_Inversion = UniqueID.next;
		sendTrigID_OpenChord = UniqueID.next;
		sendTrigID_TransposeOcts = UniqueID.next;
		sendTrigID_GlobalVelScale = UniqueID.next;
		sendTrigID_RandVel = UniqueID.next;
		arrSynthArgSpecs = [
			["modActive", 0, 0],
			["modActive", 0, 0],
			["modActive", 0, 0],
			["modActive", 0, 0],
			["modTransposeOcts", 0, 0],
			["modGlobalVelScale", 0, 0],
			["modRandVel", 0, 0],

			// N.B. the args below aren't used in the synthdef, just stored here for convenience

			["noteOutModuleID1", nil],
			["noteOutModuleID2", nil],
			["noteOutModuleID3", nil],
			["noteOutModuleID4", nil],
			["noteOutModuleID5", nil],
			["noteOutModuleID6", nil],

			["midiPort", 0, 0],
			["midiLoopBack", 0, 0],
			["midiChannel", 1, 0],
			["midiNoteOn", 0, 0],

			["active", 1, 0, 0],
			["key", 0, 0],
			["ignoreKey", 0, 0],
			["scale", 35, 0],
			["correction", 0, 0],
			["playChord", 1, 0],
			["inversion", 0.5, 0],
			["inversionMin", -5, 0],
			["inversionMax", 5, 0],
			["openChord", 0, 0],
			["transposeOcts", 0.5, 0],
			["transposeOctsMin", -4, 0],
			["transposeOctsMax", 4, 0],
			["globalVelScale", 1, 0],
			["randVel", 0, 0],
			["scaleNoteVelocities", 1, 0],
			["overrideFirstNoteVel", 1, 0],
			["overrideFirstNoteProb", 1, 0],
			["randVelOrder", 0, 0],
			["randProbOrder", 0, 0],
			["note1On", 1, 0],
			["note2On", 0, 0],
			["note3On", 1, 0],
			["note4On", 0, 0],
			["note5On", 1, 0],
			["note6On", 0, 0],
			["note7On", 0, 0],
			["note8On", 0, 0],
			["note9On", 0, 0],
			["note10On", 0, 0],
			["note11On", 0, 0],
			["note12On", 0, 0],
			["noteOctUpOn", 0, 0],
			["noteOctDownOn", 0, 0],
			["note1Transp", 0, 0],
			["note2Transp", 1, 0],
			["note3Transp", 1, 0],
			["note4Transp", 1, 0],
			["note5Transp", 1, 0],
			["note6Transp", 1, 0],
			["note7Transp", 1, 0],
			["note8Transp", 1, 0],
			["note9Transp", 1, 0],
			["note10Transp", 1, 0],
			["note11Transp", 1, 0],
			["note12Transp", 1, 0],
			["noteOctUpTransp", 1, 0],
			["noteOctDownTransp", 1, 0],
			["velocity1", 1, 0],
			["velocity2", 1, 0],
			["velocity3", 1, 0],
			["velocity4", 1, 0],
			["velocity5", 1, 0],
			["velocity6", 1, 0],
			["velocity7", 1, 0],
			["velocity8", 1, 0],
			["velocity9", 1, 0],
			["velocity10", 1, 0],
			["velocity11", 1, 0],
			["velocity12", 1, 0],
			["velocityOctUp", 1, 0],
			["velocityOctDown", 1, 0],
			["probability1", 1, 0],
			["probability2", 1, 0],
			["probability3", 1, 0],
			["probability4", 1, 0],
			["probability5", 1, 0],
			["probability6", 1, 0],
			["probability7", 1, 0],
			["probability8", 1, 0],
			["probability9", 1, 0],
			["probability10", 1, 0],
			["probability11", 1, 0],
			["probability12", 1, 0],
			["probabilityOctUp", 1, 0],
			["probabilityOctDown", 1, 0],
		];
		synthDefFunc = { arg modActive = 0, modPlayChord = 0, modInversion = 0, modOpenChord = 0,
			modTransposeOcts = 0, modGlobalVelScale = 0, modRandVel = 0;

			var holdImpulse, trigActive, trigPlayChord, trigInversion, trigOpenChord, trigTransposeOcts;
			var trigGlobalVelScale, trigRandVel;

			modActive = modActive.max(-1).min(1);
			modPlayChord = modPlayChord.max(-1).min(1);
			modInversion = modInversion.max(-1).min(1);
			modOpenChord = modOpenChord.max(-1).min(1);
			modTransposeOcts = modTransposeOcts.max(-1).min(1);
			modGlobalVelScale = modGlobalVelScale.max(-1).min(1);
			modRandVel = modRandVel.max(-1).min(1);

			holdImpulse = Impulse.kr(20);

			// trigger current values to be sent when value changes
			trigActive = Trig.kr((1 - holdImpulse) * HPZ1.kr(modActive).abs, 0.05);
			SendTrig.kr(trigActive, sendTrigID_Active, modActive);

			trigPlayChord = Trig.kr((1 - holdImpulse) * HPZ1.kr(modPlayChord).abs, 0.05);
			SendTrig.kr(trigPlayChord, sendTrigID_PlayChord, modPlayChord);

			trigInversion = Trig.kr((1 - holdImpulse) * HPZ1.kr(modInversion).abs, 0.05);
			SendTrig.kr(trigInversion, sendTrigID_Inversion, modInversion);

			trigOpenChord = Trig.kr((1 - holdImpulse) * HPZ1.kr(modOpenChord).abs, 0.05);
			SendTrig.kr(trigOpenChord, sendTrigID_OpenChord, modOpenChord);

			trigTransposeOcts = Trig.kr((1 - holdImpulse) * HPZ1.kr(modTransposeOcts).abs, 0.05);
			SendTrig.kr(trigTransposeOcts, sendTrigID_TransposeOcts, modTransposeOcts);

			trigGlobalVelScale = Trig.kr((1 - holdImpulse) * HPZ1.kr(modGlobalVelScale).abs, 0.05);
			SendTrig.kr(trigGlobalVelScale, sendTrigID_GlobalVelScale, modGlobalVelScale);

			trigRandVel = Trig.kr((1 - holdImpulse) * HPZ1.kr(modRandVel).abs, 0.05);
			SendTrig.kr(trigRandVel, sendTrigID_RandVel, modRandVel);
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs(
			[
				["TestNoteVals"],
				["MIDIListenCheckBox"],
				["NextLine"],
				["MIDIChannelSelector"],
				["MIDINoteSelector"],
				["MIDIVelSelector"],
				["TXCheckBox", "Active", "active"],
				["TXPopupAction", "Key", ["C", "C#", "D", "D#", "E","F",
					"F#", "G", "G#", "A", "A#", "B"], "key", nil, 130],
				["TXListViewAction", "Scale", this.getScaleNameStrings, "scale", nil, 400, 200],
				["TXCheckBox", "Silence non-scale notes", "correction", nil, 300],
				["TXMinMaxSliderSplit", "Transp octs", ControlSpec(-8, 8, step: 1), "transposeOcts", "transposeOctsMin", "transposeOctsMax"],
				["TXCheckBox", "Play chord", "playChord"],
				["TXCheckBox", "Open chord", "openChord"],
				["TXMinMaxSliderSplit", "Inversion", ControlSpec(-12, 12, step: 1), "inversion", "inversionMin", "inversionMax"],
				["TXCheckBox", "Use played velocity for root", "overrideFirstNoteVel", {}, 250],
				["TXCheckBox", "Use probability 100% for root", "overrideFirstNoteProb", {}, 250],
				["EZSlider", "Velocity scale", ControlSpec(0, 1), "globalVelScale"],
				["EZSlider", "Random vel", ControlSpec(0, 1), "randVel"],
				["TXCheckBox", "Scale note velocities to played velocity", "scaleNoteVelocities", {}, 250],
				["TXCheckBox", "Randomise velocity note order", "randVelOrder", {}, 220],
				["TXCheckBox", "Randomise probability note order", "randProbOrder", {}, 220],
				["TXCheckBox", "Note 1 on", "note1On", nil, 50],
				["TXCheckBox", "Note 2 on", "note2On", nil, 50],
				["TXCheckBox", "Note 3 on", "note3On", nil, 50],
				["TXCheckBox", "Note 4 on", "note4On", nil, 50],
				["TXCheckBox", "Note 5 on", "note5On", nil, 50],
				["TXCheckBox", "Note 6 on", "note6On", nil, 50],
				["TXCheckBox", "Note 7 on", "note7On", nil, 50],
				["TXCheckBox", "Note 8 on", "note8On", nil, 50],
				["TXCheckBox", "Note 9 on", "note9On", nil, 50],
				["TXCheckBox", "Note 10 on", "note10On", nil, 50],
				["TXCheckBox", "Note 11 on", "note11On", nil, 50],
				["TXCheckBox", "Note 12 on", "note12On", nil, 50],
				["TXCheckBox", "Oct + on", "noteOctUpOn", nil, 50],
				["TXCheckBox", "Oct - on", "noteOctDownOn", nil, 50],
				["TXCheckBox", "Note 1 transp oct +", "note1Transp", nil, 50],
				["TXCheckBox", "Note 2 transp oct +", "note2Transp", nil, 50],
				["TXCheckBox", "Note 3 transp oct +", "note3Transp", nil, 50],
				["TXCheckBox", "Note 4 transp oct +", "note4Transp", nil, 50],
				["TXCheckBox", "Note 5 transp oct +", "note5Transp", nil, 50],
				["TXCheckBox", "Note 6 transp oct +", "note6Transp", nil, 50],
				["TXCheckBox", "Note 7 transp oct +", "note7Transp", nil, 50],
				["TXCheckBox", "Note 8 transp oct +", "note8Transp", nil, 50],
				["TXCheckBox", "Note 9 transp oct +", "note9Transp", nil, 50],
				["TXCheckBox", "Note 10 transp oct +", "note10Transp", nil, 50],
				["TXCheckBox", "Note 11 transp oct +", "note11Transp", nil, 50],
				["TXCheckBox", "Note 12 transp oct +", "note12Transp", nil, 50],
				["TXCheckBox", "Oct + transp oct +", "noteOctUpTransp", nil, 50],
				["TXCheckBox", "Oct - transp oct -", "noteOctDownTransp", nil, 50],
			]
			++ ["velocity1", "velocity2", "velocity3", "velocity4", "velocity5", "velocity6", "velocity7", "velocity8",
				"velocity9", "velocity10", "velocity11", "velocity12", "velocityOctUp", "velocityOctDown",
			].collect({arg item, i;
				["EZsliderUnmapped", item, ControlSpec(0, 100), item]
			})
			++ ["probability1", "probability2", "probability3", "probability4", "probability5",
				"probability6", "probability7", "probability8", "probability9", "probability10", "probability11",
				"probability12", "probabilityOctUp", "probabilityOctDown",
			].collect({arg item, i;
				["EZsliderUnmapped", item, ControlSpec(0, 100), item]
			})
		);
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.midiNoteInit;
		this.oscControlActivate;
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	buildGuiSpecArray {
		var holdFreqMin = this.getSynthArgSpec("freqMin");
		var holdFreqMax = this.getSynthArgSpec("freqMax");

		guiSpecArray = [
			["ActionButton", "MIDI & Outputs", {displayOption = "showOutputs";
				this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayOption == "showOutputs")],
			["Spacer", 3],
			["ActionButton", "Key & Scale", {displayOption = "showKeyScale";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showKeyScale")],
			["Spacer", 3],
			["ActionButton", "Chord", {displayOption = "showChordParameters";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showChordParameters")],
			["Spacer", 80],
			["TXCheckBox", "Active", "active", {}, 100],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 6],
		];

		if (displayOption == "showOutputs", {
			holdPortNames = ["Unconnected - select MIDI Port"]
			++ MIDIClient.destinations.collect({arg item, i;
				// remove any brackets from string
				(item.device.asString + item.name.asString).replace("(", "").replace(")", "")
			});
			guiSpecArray = guiSpecArray ++[
				["MIDIListenCheckBox"],
				["SpacerLine", 2],
				["MIDIChannelSelector", 510],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["TextBarLeft", "Modules to be triggered:", 140],
				["NextLine"],
				["SeqSelect3GroupModules", "noteOutModule1", "noteOutModule2", "noteOutModule3",
					"noteOutModuleID1", "noteOutModuleID2", "noteOutModuleID3"],
				["SpacerLine", 2],
				["SeqSelect3GroupModules", "noteOutModule4", "noteOutModule5", "noteOutModule6",
					"noteOutModuleID4", "noteOutModuleID5", "noteOutModuleID6"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXCheckBox", "Output MIDI notes", "midiNoteOn", nil, 140],
				["SpacerLine", 2],
				["TXPopupActionPlusMinus", "MIDI Port", holdPortNames, "midiPort",
					{ arg view; this.initMidiPort(view.value); }, 300],
				["Spacer", 10],
				["TXCheckBox", "MIDI Loop-Back", "midiLoopBack", nil, 120],
				["SpacerLine", 4],
				["EZSlider", "MIDI Channel", ControlSpec(1, 16, step: 1), "midiChannel", nil, 510],
				["SpacerLine", 30],
				["MIDIKeyboard", {arg note; this.createSynthNote(note, testMIDIVel, 0);},
					7, 80, 756, 24, {arg note; this.releaseSynthGate(note);}, "C0 - B7", true,
					{this.getScaleSemitones}, {this.getSynthArgSpec("key")}],
			];
		});
		if (displayOption == "showKeyScale", {
			guiSpecArray = guiSpecArray ++[
				["TXPopupActionPlusMinus", "Key", ["C", "C#", "D", "D#", "E","F",
					"F#", "G", "G#", "A", "A#", "B"], "key", {system.showView}, 200],
				["Spacer", 10],
				["TXCheckBox", "Ignore key, scale follows played note - for fixed chord shapes", "ignoreKey", nil, 360],
				["SpacerLine", 10],
				["TXListViewActionPlusMinus", "Scale", this.getScaleNameStrings, "scale",
					{system.showView}, 420, 100],
				["Spacer", 10],
				["TXCheckBox", "Silence notes played outside the scale", "correction", nil, 260],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Transp octs", ControlSpec(-8, 8, step: 1),
					"transposeOcts", "transposeOctsMin", "transposeOctsMax", nil, nil, nil, nil, 510],
				["SpacerLine", 30],
				["MIDIKeyboard", {arg note; this.createSynthNote(note, testMIDIVel, 0);},
					7, 80, 756, 24, {arg note; this.releaseSynthGate(note);}, "C0 - B7", true,
					{this.getScaleSemitones}, {this.getSynthArgSpec("key")}],
			];
		});
		if (displayOption == "showChordParameters", {
			guiSpecArray = guiSpecArray ++[
				["TXCheckBox", "Play chord", "playChord", {}, 100],
				["Spacer", 10],
				["TXCheckBox", "Open chord", "openChord", {}, 100],
				["Spacer", 10],
				["TXMinMaxSliderSplit", "Inversion", ControlSpec(-12, 12, step: 1),
					"inversion", "inversionMin", "inversionMax", nil, nil, nil, nil, 440],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["EZSlider", "Randomise vel", ControlSpec(0, 1), "randVel", nil, 510],
				["SpacerLine", 10],
				["EZSlider", "Velocity scale", ControlSpec(0, 1), "globalVelScale", nil, 510],
				["Spacer", 30],
				["TXCheckBox", "Scale note velocities to played velocity", "scaleNoteVelocities", {}, 240],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXCheckBox", "Use played velocity for note 1", "overrideFirstNoteVel", {}, 194],
				["Spacer", 10],
				["TXCheckBox", "Use probability 100% for note 1", "overrideFirstNoteProb", {}, 194],
				["Spacer", 10],
				["TXCheckBox", "Randomise velocity note order", "randVelOrder", {}, 200],
				["Spacer", 10],
				["TXCheckBox", "Randomise probability note order", "randProbOrder", {}, 204],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],

				//classData.chordPresets
				["TXPopupAction", "Chord presets", classData.chordPresets.collect({arg item; item[0]}), nil,
					{arg view; this.loadChordPreset(view.value);}, 300],
				["SpacerLine", 6],
				["TextBar", "Notes", 80],
				["TXCheckBox", "N 1", "note1On", nil, 50, 20, 0, this.getNoteStringColour(1), this.getNoteColour(1)],
				["TXCheckBox", "N 2", "note2On", nil, 50, 20, 0, this.getNoteStringColour(2), this.getNoteColour(2)],
				["TXCheckBox", "N 3", "note3On", nil, 50, 20, 0, this.getNoteStringColour(3), this.getNoteColour(3)],
				["TXCheckBox", "N 4", "note4On", nil, 50, 20, 0, this.getNoteStringColour(4), this.getNoteColour(4)],
				["TXCheckBox", "N 5", "note5On", nil, 50, 20, 0, this.getNoteStringColour(5), this.getNoteColour(5)],
				["TXCheckBox", "N 6", "note6On", nil, 50, 20, 0, this.getNoteStringColour(6), this.getNoteColour(6)],
				["TXCheckBox", "N 7", "note7On", nil, 50, 20, 0, this.getNoteStringColour(7), this.getNoteColour(7)],
				["TXCheckBox", "N 8", "note8On", nil, 50, 20, 0, this.getNoteStringColour(8), this.getNoteColour(8)],
				["TXCheckBox", "N 9", "note9On", nil, 50, 20, 0, this.getNoteStringColour(9), this.getNoteColour(9)],
				["TXCheckBox", "N 10", "note10On", nil, 50, 20, 0, this.getNoteStringColour(10), this.getNoteColour(10)],
				["TXCheckBox", "N 11", "note11On", nil, 50, 20, 0, this.getNoteStringColour(11), this.getNoteColour(11)],
				["TXCheckBox", "N 12", "note12On", nil, 50, 20, 0, this.getNoteStringColour(12), this.getNoteColour(12)],
				["TXCheckBox", "Oct +", "noteOctUpOn", nil, 50],
				["TXCheckBox", "Oct -", "noteOctDownOn", nil, 50],
				["SpacerLine", 2],
				["TextBar", "Open transp", 80],
				["TXCheckBox", "Oct +", "note1Transp", nil, 50, 20, 0, this.getNoteStringColour(1), this.getNoteColour(1)],
				["TXCheckBox", "Oct +", "note2Transp", nil, 50, 20, 0, this.getNoteStringColour(2), this.getNoteColour(2)],
				["TXCheckBox", "Oct +", "note3Transp", nil, 50, 20, 0, this.getNoteStringColour(3), this.getNoteColour(3)],
				["TXCheckBox", "Oct +", "note4Transp", nil, 50, 20, 0, this.getNoteStringColour(4), this.getNoteColour(4)],
				["TXCheckBox", "Oct +", "note5Transp", nil, 50, 20, 0, this.getNoteStringColour(5), this.getNoteColour(5)],
				["TXCheckBox", "Oct +", "note6Transp", nil, 50, 20, 0, this.getNoteStringColour(6), this.getNoteColour(6)],
				["TXCheckBox", "Oct +", "note7Transp", nil, 50, 20, 0, this.getNoteStringColour(7), this.getNoteColour(7)],
				["TXCheckBox", "Oct +", "note8Transp", nil, 50, 20, 0, this.getNoteStringColour(8), this.getNoteColour(8)],
				["TXCheckBox", "Oct +", "note9Transp", nil, 50, 20, 0, this.getNoteStringColour(9), this.getNoteColour(9)],
				["TXCheckBox", "Oct +", "note10Transp", nil, 50, 20, 0, this.getNoteStringColour(10), this.getNoteColour(10)],
				["TXCheckBox", "Oct +", "note11Transp", nil, 50, 20, 0, this.getNoteStringColour(11), this.getNoteColour(11)],
				["TXCheckBox", "Oct +", "note12Transp", nil, 50, 20, 0, this.getNoteStringColour(12), this.getNoteColour(12)],
				["TXCheckBox", "Oct +", "noteOctUpTransp", nil, 50],
				["TXCheckBox", "Oct -", "noteOctDownTransp", nil, 50],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["velocity1", "velocity2", "velocity3", "velocity4",
					"velocity5", "velocity6", "velocity7", "velocity8", "velocity9", "velocity10", "velocity11", "velocity12",
					"velocityOctUp", "velocityOctDown" ]);}, 36],
				["TXMultiKnobNoUnmap", "Vel ocity", ["velocity1", "velocity2", "velocity3", "velocity4", "velocity5",
					"velocity6", "velocity7", "velocity8", "velocity9", "velocity10", "velocity11", "velocity12",
					"velocityOctUp", "velocityOctDown" ], 14, ControlSpec(0, 100)],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",  {this.copy1ToAll([ "probability1", "probability2", "probability3", "probability4",
					"probability5", "probability6", "probability7", "probability8", "probability9", "probability10",
					"probability11", "probability12", "probabilityOctUp", "probabilityOctDown", ]);}, 36],
				["TXMultiKnobNoUnmap", "Proba bility", [ "probability1", "probability2", "probability3", "probability4",
					"probability5","probability6", "probability7", "probability8", "probability9", "probability10",
					"probability11", "probability12", "probabilityOctUp", "probabilityOctDown", ], 14, ControlSpec(0, 100)],
				["NextLine"],
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

	getNoteStringColour { arg noteNo;
		if (this.getNotesPerOct < noteNo, {
			^TXColor.grey(0.8);
		},{
			^TXColor.white;
		});
	}

	getNoteColour { arg noteNo;
		if (this.getNotesPerOct < noteNo, {
			^TXColor.grey(0.6);
		},{
			if (((noteNo - 1) % 2) == 1, {
				^TXColor.sysGuiCol2;
			},{
				^TXColor.sysGuiCol1;
			});
		});
	}

	copy1ToAll {arg arrayStrings;
		var holdVal = this.getSynthArgSpec(arrayStrings[0]);
		arrayStrings.do({arg item, i;
			if (i > 0, {
				this.setSynthValue(item, holdVal);
			});
		});
		system.showView;
	}

	////////////////////////////////////

	loadChordPreset {arg index;
		var arrAllStrings, arrChordStrings;
		if (index > 0, {
			arrAllStrings = ["note1On", "note2On", "note3On", "note4On", "note5On", "note6On", "note7On",
				"note8On", "note9On", "note10On", "note11On", "note12On", "noteOctUpOn", "noteOctDownOn"];
			arrChordStrings = classData.chordPresets[index][1];
			arrAllStrings.do({arg item, i;
				var holdVal;
				if (arrChordStrings.indexOfEqual(item).notNil, {
					holdVal = 1;
				},{
					holdVal = 0;
				});
				this.setSynthArgSpec(item, holdVal);
			});
			system.showView;
		});
	}

	oscControlActivate {
		//	remove any previous OSCresponderNode and add new
		this.oscControlDeactivate;
		oscControlFunc = OSCFunc({arg msg, time;
			if (msg[2] == sendTrigID_Active,{
				holdModActive = msg[3];
			});
			if (msg[2] == sendTrigID_PlayChord,{
				holdModPlayChord = msg[3];
			});
			if (msg[2] == sendTrigID_Inversion,{
				holdModInversion = msg[3];
			});
			if (msg[2] == sendTrigID_OpenChord,{
				holdModOpenChord = msg[3];
			});
			if (msg[2] == sendTrigID_TransposeOcts,{
				holdModTransposeOcts = msg[3];
			});
			if (msg[2] == sendTrigID_GlobalVelScale,{
				holdModGlobalVelScale = msg[3];
			});
			if (msg[2] == sendTrigID_RandVel,{
				holdModRandVel = msg[3];
			});
		}, '/tr', system.server.addr);
	}

	oscControlDeactivate {
		//	remove responder
		oscControlFunc.free;
	}

	deleteModuleExtraActions {
		this.oscControlDeactivate;
	}


	////////////////////////////////////

	getScaleNameStrings {
		if (classData.scaleNameStrings.isNil, {
			classData.scaleNameStrings = classData.scaleNames.collect({arg item;
				Scale.at(item).name ++ " - " ++ Scale.at(item).semitones.size ++ " notes per octave";
			});
		})
		^classData.scaleNameStrings;
	}

	getScaleSemitones {
		var semitones;
		semitones = Scale.at(classData.scaleNames[this.getSynthArgSpec("scale")]).semitones;
		^semitones;
	}

	getNotesPerOct {
		^this.getScaleSemitones.size;
	}

	arrActiveModules {
		var arrModules;
		// retrigger modules
		arrModules = [noteOutModule1, noteOutModule2, noteOutModule3, noteOutModule4, noteOutModule5, noteOutModule6,
		].select({ arg item, i; item.notNil and: {item != 0}});
		^arrModules;
	}

	arrCurrNoteIndices {
		var arrIndices, arrStrings;
		var noteCount = this.getNotesPerOct;
		arrIndices = [];
		arrStrings =  ["note1On", "note2On", "note3On", "note4On", "note5On",
			"note6On", "note7On", "note8On", "note9On", "note10On",
			"note11On", "note12On",];
		noteCount.do({ arg i;
			if (this.getSynthArgSpec(arrStrings[i]) == 1, {
				arrIndices = arrIndices.add(i);
			});
		});
		// octs treated separately
		["noteOctUpOn", "noteOctDownOn",].do({ arg item, i;
			if (this.getSynthArgSpec(item) == 1, {
				arrIndices = arrIndices.add(noteCount + i);
			});
		});
		^arrIndices;
	}

	getCurrGlobalVelScale {
		var globalVelScale;
		globalVelScale = (this.getSynthArgSpec("globalVelScale") + holdModGlobalVelScale).max(0).min(1);
		^globalVelScale;
	}

	getCurrRandVel {
		var randVel;
		randVel = (this.getSynthArgSpec("randVel") + holdModRandVel).max(0).min(1);
		^randVel;
	}

	getCurrOpenChord {
		var openChord;
		openChord = (this.getSynthArgSpec("openChord") + holdModOpenChord).max(0).min(1);
		^openChord;
	}

	getCurrPlayChord {
		var playChord;
		playChord = (this.getSynthArgSpec("playChord") + holdModPlayChord).max(0).min(1);
		^playChord;
	}

	getCurrOctTranspose {
		var sumTransposeOcts;
		sumTransposeOcts = (this.getSynthArgSpec("transposeOcts") + holdModTransposeOcts).max(0).min(1).linlin(0, 1,
			this.getSynthArgSpec("transposeOctsMin"), this.getSynthArgSpec("transposeOctsMax")).round;
		^(sumTransposeOcts * 12);
	}

	getCurrInversion {
		var sumInversion;
		sumInversion = (this.getSynthArgSpec("inversion") + holdModInversion).max(0).min(1).linlin(0, 1,
			this.getSynthArgSpec("inversionMin"), this.getSynthArgSpec("inversionMax")).round;
		^sumInversion;
	}

	arrVelocityVals {arg arrIndices;
		var holdInds, arrStrings, arrVals;
		holdInds = arrIndices;
		if (this.getSynthArgSpec("randVelOrder") == 1, {
			holdInds = holdInds.scramble;
		});
		arrStrings =  ["velocity1", "velocity2", "velocity3", "velocity4", "velocity5",
			"velocity6", "velocity7", "velocity8", "velocity9", "velocity10",
			"velocity11", "velocity12", "velocityOctUp", "velocityOctDown", ];
		arrVals = arrStrings[holdInds].collect({ arg item, i;
			this.getSynthArgSpec(item);
		});
		^arrVals;
	}

	arrProbabilityVals {arg arrIndices;
		var holdInds, arrStrings, arrVals;
		holdInds = arrIndices;
		if (this.getSynthArgSpec("randProbOrder") == 1, {
			holdInds = holdInds.scramble;
		});
		arrStrings =  ["probability1", "probability2", "probability3", "probability4", "probability5",
			"probability6", "probability7", "probability8", "probability9", "probability10",
			"probability11", "probability12", "probabilityOctUp", "probabilityOctDown", ];
		arrVals = arrStrings[holdInds].collect({ arg item, i;
			this.getSynthArgSpec(item);
		});
		^arrVals;
	}

	arrNoteTranspVals {arg arrIndices, arrSemitones;
		var holdScaleSemis, arrVals;
		holdScaleSemis = arrSemitones;
		holdScaleSemis = holdScaleSemis ++ [12, -12];  // add +/- octave
		arrVals = arrIndices.collect({ arg item;
			holdScaleSemis[item];
		});
		^arrVals;
	}

	arrOpenTranspVals {arg arrIndices, arrSemitones;
		var arrStrings, arrTranspVals, arrVals;
		if (this.getCurrOpenChord == 0, {
			arrVals = 0 ! arrIndices.size;
			^arrVals;
		});
		arrStrings =  ["note1Transp", "note2Transp", "note3Transp", "note4Transp", "note5Transp",
			"note6Transp", "note7Transp", "note8Transp", "note9Transp", "note10Transp",
			"note11Transp", "note12Transp", "noteOctUpTransp", "noteOctDownTransp", ];
		arrTranspVals = ((12 ! arrSemitones.size) ++ [12, -12]);
		arrVals = arrIndices.collect({ arg i;
			this.getSynthArgSpec(arrStrings[i]) * arrTranspVals[i];
		});
		^arrVals;
	}

	arrInversionTranspVals {arg arrIndices, arrSemitones;
		var semiCount, noteCount, transpCount, holdInv, arrTransps, arrVals, octUpTranspose;
		semiCount = arrSemitones.size;
		noteCount = arrIndices.select({arg item; item < semiCount}).size; // ignore octaveUp/Down
		holdInv = this.getCurrInversion;
		// positive inversion
		if (holdInv > 0, {
			transpCount = holdInv % noteCount;
			arrVals = arrIndices.collect({arg item, i;
				var out;
				// if octUp/Down
				if (item >= semiCount, {
					if ((transpCount > 0) and: {item == semiCount}, {
						out = 12;
					}, {
						out = 0;
					});
				}, {
					if (i < transpCount, {
						out = 12;
					}, {
						out = 0;
					});
				});
				out;
			});
		},{
			// negative inversion
			if (holdInv < 0, {
				transpCount = holdInv.abs % noteCount;
				arrVals = arrIndices.collect({arg item, i;
					var out;
					// if octUp/Down
					if (item >= semiCount, {
							out = 0;
					}, {
						if ((noteCount - 1 - i) < transpCount, {
							out = -12;
						}, {
							out = 0;
						});
					});
					out;
				});
			},{
				// i.e. no inversion
				arrVals = 0 ! arrIndices.size;
				^arrVals; // return now
			});
		});
		^arrVals;

	}

	////////////////////////////////////

	createSynthNote { arg note, vel, argEnvTime=1, seqLatencyOn=1;
		var holdCurrIndices, holdScaleSemis, holdKeySemis, holdNoteSemis, holdOctTranspose;
		var holdNoteTranspVals, holdOpenTranspVals, holdInvTranspVals, holdProbVals;
		var holdKey, holdIgnoreKey, holdVelVals, adjustedNote, velNorm, holdGlobalVelScale, holdRandVel, holdScaleVels;
		var validNote, holdNote, noteMod12, sumActive, activeModules, noteCount, scaleNoteIndex;

		sumActive = (this.getSynthArgSpec("active") + holdModActive).round;
		if ((moduleRunning == true) and: (sumActive > 0), {
			holdScaleSemis = this.getScaleSemitones;
			holdIgnoreKey = this.getSynthArgSpec("ignoreKey");
			validNote = true;
			if (holdIgnoreKey == 1, {
				adjustedNote = note;
				holdNoteSemis = holdScaleSemis;
			}, {
				holdKey = this.getSynthArgSpec("key");
				noteMod12 = note % 12;
				holdKeySemis = ((holdScaleSemis + holdKey) % 12).sort;
				scaleNoteIndex = holdKeySemis.indexInBetween(noteMod12).round.asInteger;
				if ((this.getSynthArgSpec("correction") == 1) and: {noteMod12 != holdKeySemis[scaleNoteIndex]}, {
					validNote = false;
				}, {
					adjustedNote = ((note div: 12) * 12) + holdKeySemis[scaleNoteIndex];
					holdNoteSemis = ((holdScaleSemis + 12 - (holdKey - adjustedNote).abs) % 12).sort;
				});
			});
			if (validNote == true, {
				activeModules = this.arrActiveModules;
				holdOctTranspose = this.getCurrOctTranspose;
				// if not chord, play note
				if (this.getCurrPlayChord == 0, {
					holdNote = adjustedNote + holdOctTranspose;
					// for all active modules create note
					activeModules.do({ arg item, i;
						item.createSynthNote(holdNote, vel, argEnvTime, seqLatencyOn);
					});
					// output midi
					this.sendMIDINoteOn(holdNote, vel, argEnvTime);
				}, {
					holdCurrIndices = this.arrCurrNoteIndices;
					noteCount = holdCurrIndices.size;
					holdNoteTranspVals = this.arrNoteTranspVals(holdCurrIndices, holdNoteSemis);
					holdOpenTranspVals = this.arrOpenTranspVals(holdCurrIndices, holdNoteSemis);
					holdInvTranspVals = this.arrInversionTranspVals(holdCurrIndices, holdNoteSemis);
					holdVelVals = this.arrVelocityVals(holdCurrIndices);
					holdProbVals = this.arrProbabilityVals(holdCurrIndices);
					holdGlobalVelScale = this.getCurrGlobalVelScale;
					holdRandVel = this.getCurrRandVel;
					holdScaleVels = this.getSynthArgSpec("scaleNoteVelocities");
					velNorm = vel/127;
					// for all active steps
					noteCount.do({arg i;
						var holdProb, holdVel;
						// if the fates allow it
						if (i == 0 and: {this.getSynthArgSpec("overrideFirstNoteProb") == 1}, {
							holdProb = 1;
						}, {
							holdProb = holdProbVals[i];
						});
						if ((holdProb - rand(1.0)).ceil == 1, {
							// vel
							if (i == 0 and: {this.getSynthArgSpec("overrideFirstNoteVel") == 1}, {
								holdVel = vel;
							}, {
								holdVel = holdVelVals[i] * 127;
								if (holdScaleVels == 1, {
									holdVel = holdVel * velNorm;
								});
							});
							if (holdRandVel > 0, {
								holdVel = (holdVel * (1-holdRandVel)) + (127.0.rand * holdRandVel);
							});
							holdVel = holdVel * holdGlobalVelScale;
							holdNote = (adjustedNote + holdOctTranspose + holdNoteTranspVals[i]
								+ holdOpenTranspVals[i] + holdInvTranspVals[i]
							).clip(0, 127);
							// for all active modules create note
							activeModules.do({ arg item, i;
								item.createSynthNote(holdNote, holdVel, argEnvTime, seqLatencyOn);
							});
							// output midi
							this.sendMIDINoteOn(holdNote, holdVel, argEnvTime);
						});
					});
				});
			});
		});
	}

	midiNoteRelease { arg note; // override default method
		var sumActive, holdCurrIndices, holdScaleSemis, holdKeySemis, holdNoteSemis, holdOctTranspose;
		var holdNoteTranspVals, holdOpenTranspVals, holdInvTranspVals;
		var holdKey, holdIgnoreKey, adjustedNote;
		var validNote, holdNote, noteMod12, activeModules, noteCount, scaleNoteIndex;

		sumActive = (this.getSynthArgSpec("active") + holdModActive).round;
		if ((moduleRunning == true) and: (sumActive > 0), {
			holdScaleSemis = this.getScaleSemitones;
			holdIgnoreKey = this.getSynthArgSpec("ignoreKey");
			validNote = true;
			if (holdIgnoreKey == 1, {
				adjustedNote = note;
				holdNoteSemis = holdScaleSemis;
			}, {
				holdKey = this.getSynthArgSpec("key");
				noteMod12 = note % 12;
				holdKeySemis = ((holdScaleSemis + holdKey) % 12).sort;
				scaleNoteIndex = holdKeySemis.indexInBetween(noteMod12).round.asInteger;
				if ((this.getSynthArgSpec("correction") == 1) and: {noteMod12 != holdKeySemis[scaleNoteIndex]}, {
					validNote = false;
				}, {
					adjustedNote = ((note div: 12) * 12) + holdKeySemis[scaleNoteIndex];
					holdNoteSemis = ((holdScaleSemis + 12 - (holdKey - adjustedNote).abs) % 12).sort;
				});
			});
			if (validNote == true, {
				activeModules = this.arrActiveModules;
				holdOctTranspose = this.getCurrOctTranspose;
				// if not chord, play note
				if (this.getCurrPlayChord == 0, {
					holdNote = adjustedNote + holdOctTranspose;
					// for all active modules create note
					activeModules.do({ arg item, i;
						item.midiNoteRelease(holdNote);
					});
					// output midi
					this.sendMIDINoteOff(holdNote);
				}, {
					holdCurrIndices = this.arrCurrNoteIndices;
					noteCount = holdCurrIndices.size;
					holdNoteTranspVals = this.arrNoteTranspVals(holdCurrIndices, holdNoteSemis);
					holdOpenTranspVals = this.arrOpenTranspVals(holdCurrIndices, holdNoteSemis);
					holdInvTranspVals = this.arrInversionTranspVals(holdCurrIndices, holdNoteSemis);
					// for all active steps
					noteCount.do({arg i;
						holdNote = (adjustedNote + holdOctTranspose + holdNoteTranspVals[i]
							+ holdOpenTranspVals[i] + holdInvTranspVals[i]
						).clip(0, 127);
						// only release if sustain pedal is off
						if (midiSustainPedalState == 0, {
							// for all active modules pass data
							this.arrActiveModules.do({ arg item, i;
								item.midiNoteRelease(holdNote);
							});
							// output midi
							this.sendMIDINoteOff(holdNote);
						},{
							arrHeldMidiNotes = arrHeldMidiNotes.add(holdNote);
						});
					});
				});
			});
		});
	}
	releaseSynthGate { arg note=60; // override default method
		var sumActive, holdCurrIndices, holdScaleSemis, holdKeySemis, holdNoteSemis, holdOctTranspose;
		var holdNoteTranspVals, holdOpenTranspVals, holdInvTranspVals;
		var holdKey, holdIgnoreKey, adjustedNote;
		var validNote, holdNote, noteMod12, activeModules, noteCount, scaleNoteIndex;

		sumActive = (this.getSynthArgSpec("active") + holdModActive).round;
		if ((moduleRunning == true) and: (sumActive > 0), {
			holdScaleSemis = this.getScaleSemitones;
			holdIgnoreKey = this.getSynthArgSpec("ignoreKey");
			validNote = true;
			if (holdIgnoreKey == 1, {
				adjustedNote = note;
				holdNoteSemis = holdScaleSemis;
			}, {
				holdKey = this.getSynthArgSpec("key");
				noteMod12 = note % 12;
				holdKeySemis = ((holdScaleSemis + holdKey) % 12).sort;
				scaleNoteIndex = holdKeySemis.indexInBetween(noteMod12).round.asInteger;
				if ((this.getSynthArgSpec("correction") == 1) and: {noteMod12 != holdKeySemis[scaleNoteIndex]}, {
					validNote = false;
				}, {
					adjustedNote = ((note div: 12) * 12) + holdKeySemis[scaleNoteIndex];
					holdNoteSemis = ((holdScaleSemis + 12 - (holdKey - adjustedNote).abs) % 12).sort;
				});
			});
			if (validNote == true, {
				activeModules = this.arrActiveModules;
				holdOctTranspose = this.getCurrOctTranspose;
				// if not chord, play note
				if (this.getCurrPlayChord == 0, {
					holdNote = adjustedNote + holdOctTranspose;
					// for all active modules create note
					activeModules.do({ arg item, i;
						item.releaseSynthGate(holdNote);
					});
					// output midi
					this.sendMIDINoteOff(holdNote);
				}, {
					holdCurrIndices = this.arrCurrNoteIndices;
					noteCount = holdCurrIndices.size;
					holdNoteTranspVals = this.arrNoteTranspVals(holdCurrIndices, holdNoteSemis);
					holdOpenTranspVals = this.arrOpenTranspVals(holdCurrIndices, holdNoteSemis);
					holdInvTranspVals = this.arrInversionTranspVals(holdCurrIndices, holdNoteSemis);
					// for all active steps
					noteCount.do({arg i;
						holdNote = (adjustedNote + holdOctTranspose + holdNoteTranspVals[i]
							+ holdOpenTranspVals[i] + holdInvTranspVals[i]
						).clip(0, 127);
						// only release if sustain pedal is off
						if (midiSustainPedalState == 0, {
							// for all active modules pass data
							this.arrActiveModules.do({ arg item, i;
								item.releaseSynthGate(holdNote);
							});
							// output midi
							this.sendMIDINoteOff(holdNote);
						},{
							arrHeldMidiNotes = arrHeldMidiNotes.add(holdNote);
						});
					});
				});
			});
		});
	}

	setMidiBend { arg inVal;  // override default method
		// for all active modules pass data
		this.arrActiveModules.do({ arg item, i;
			item.setMidiBend(inVal);
		});
	}

	setMidiSustainPedalState { arg inVal;  // override default method
		if (inVal == 0, {
			// clear all held notes
			arrHeldMidiNotes.do ({arg item, i;
				this.releaseSynthGate(item);
				// for all active modules pass release note
				this.arrActiveModules.do({ arg module, i;
					module.releaseSynthGate(item);
				});
			});
			arrHeldMidiNotes = [];
		});
		midiSustainPedalState = inVal;
	}

	////////////////////////////////////

	runAction { moduleRunning = true; }   //	override base class

	pauseAction { moduleRunning = false;}   //	override base class

	extraSaveData {
		^[holdMIDIDeviceName, holdMIDIPortName];
	}

	loadExtraData {arg argData = [];
		var portIndex;
		portIndex = this.getSynthArgSpec("midiPort");
		holdMIDIDeviceName = argData.at(0) ? nil;
		holdMIDIPortName = argData.at(1) ? nil;
		// if names given, find correct port from names
		if ( holdMIDIDeviceName.notNil and: holdMIDIPortName.notNil, {
			// default to 0 in case device/port names not found
			portIndex = 0;
			this.setSynthArgSpec("midiPort", 0);
			MIDIClient.destinations.do({arg item, i;
				if ( (holdMIDIDeviceName == item.device) and: (holdMIDIPortName == item.name), {
					portIndex = i + 1;
					this.setSynthArgSpec("midiPort", portIndex);
				});
			});
		});
		this.initMidiPort(portIndex);
		this.restoreOutputs;
		this.buildGuiSpecArray;
	}
	////////////////////////////////////

	restoreOutputs {
		var holdID;
		holdID = this.getSynthArgSpec("noteOutModuleID1");
		if (holdID.notNil, {noteOutModule1 = system.getModuleFromID(holdID)}, {noteOutModule1 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleID2");
		if (holdID.notNil, {noteOutModule2 = system.getModuleFromID(holdID)}, {noteOutModule2 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleID3");
		if (holdID.notNil, {noteOutModule3 = system.getModuleFromID(holdID)}, {noteOutModule3 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleID4");
		if (holdID.notNil, {noteOutModule4 = system.getModuleFromID(holdID)}, {noteOutModule4 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleID5");
		if (holdID.notNil, {noteOutModule5 = system.getModuleFromID(holdID)}, {noteOutModule5 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleID6");
		if (holdID.notNil, {noteOutModule6 = system.getModuleFromID(holdID)}, {noteOutModule6 = nil});
	}

	checkDeletions {
		// check if any note out modules are going to be deleted - if so remove them as outputs
		if (noteOutModule1.notNil, {
			if (noteOutModule1.deletedStatus == true, {
				noteOutModule1 = nil;
				this.setSynthArgSpec("noteOutModuleID1", nil);
			});
		});
		if (noteOutModule2.notNil, {
			if (noteOutModule2.deletedStatus == true, {
				noteOutModule2 = nil;
				this.setSynthArgSpec("noteOutModuleID2", nil);
			});
		});
		if (noteOutModule3.notNil, {
			if (noteOutModule3.deletedStatus == true, {
				noteOutModule3 = nil;
				this.setSynthArgSpec("noteOutModuleID3", nil);
			});
		});
		if (noteOutModule4.notNil, {
			if (noteOutModule4.deletedStatus == true, {
				noteOutModule4 = nil;
				this.setSynthArgSpec("noteOutModuleID4", nil);
			});
		});
		if (noteOutModule5.notNil, {
			if (noteOutModule5.deletedStatus == true, {
				noteOutModule5 = nil;
				this.setSynthArgSpec("noteOutModuleID5", nil);
			});
		});
		if (noteOutModule6.notNil, {
			if (noteOutModule6.deletedStatus == true, {
				noteOutModule6 = nil;
				this.setSynthArgSpec("noteOutModuleID6", nil);
			});
		});
	}

	////////////////////////////////////

	initMidiPort { arg portInd = 0;
		if (portInd == 0, {
			holdMIDIOutPort = nil;
			holdMIDIDeviceName = nil;
			holdMIDIPortName = nil;
		},{
			holdMIDIOutPort = MIDIOut(portInd - 1, MIDIClient.destinations[portInd - 1].uid);
			holdMIDIDeviceName =  MIDIClient.destinations[portInd - 1].device;
			holdMIDIPortName =  MIDIClient.destinations[portInd - 1].name;
			// minimise MIDI out latency
			holdMIDIOutPort.latency = 0;
		});
	}

	sendMIDINoteOn { arg outNote, outVel, gateTime, delay = 0;
		var portInd, channel, midiLoopBack, midiNoteOn;
		midiNoteOn = this.getSynthArgSpec("midiNoteOn");
		if (midiNoteOn == 1, {
			portInd = this.getSynthArgSpec("midiPort");
			midiLoopBack = this.getSynthArgSpec("midiLoopBack");
			channel = this.getSynthArgSpec("midiChannel") - 1;
			if (midiLoopBack == 1, {
				SystemClock.sched( delay,{
					MIDIIn.doNoteOnAction(0, channel, outNote, outVel);
					nil;
				});
				if (gateTime > 0, {
					SystemClock.sched( delay + gateTime,{
						MIDIIn.doNoteOffAction(0, channel, outNote, 0);
						nil;
					});
				});
			});
			if (portInd > 0, {
				// "Note On & Off"
				SystemClock.sched( delay,{
					holdMIDIOutPort.noteOn(channel, outNote, outVel);
					nil;
				});
				if (gateTime > 0, {
					SystemClock.sched( delay + gateTime,{
						holdMIDIOutPort.noteOff(channel, outNote, 0);
						nil;
					});
				});
			});
		});
	}

	sendMIDINoteOff { arg outNote, outVel, gateTime, delay = 0;
		var portInd, channel, midiLoopBack, midiNoteOn;
		midiNoteOn = this.getSynthArgSpec("midiNoteOn");
		if (midiNoteOn == 1, {
			portInd = this.getSynthArgSpec("midiPort");
			midiLoopBack = this.getSynthArgSpec("midiLoopBack");
			channel = this.getSynthArgSpec("midiChannel") - 1;
			if (midiLoopBack == 1, {
				SystemClock.sched( delay,{
					MIDIIn.doNoteOnAction(0, channel, outNote, outVel);
					nil;
				});
				if (gateTime > 0, {
					SystemClock.sched( delay + gateTime,{
						MIDIIn.doNoteOffAction(0, channel, outNote, 0);
						nil;
					});
				});
			});
			if (portInd > 0, {
				// "Note On & Off"
				SystemClock.sched( delay,{
					holdMIDIOutPort.noteOn(channel, outNote, outVel);
					nil;
				});
				if (gateTime > 0, {
					SystemClock.sched( delay + gateTime,{
						holdMIDIOutPort.noteOff(channel, outNote, 0);
						nil;
					});
				});
			});
		});
	}

}

