// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXNoteMultiply : TXModuleBase {

	classvar <>classData;

	var	moduleRunning;
	var	displayOption;
	var oscControlFunc;

	var sendTrigID_Active;
	var sendTrigID_BPM;
	var sendTrigID_StepBeats;
	var sendTrigID_Swing;
	var sendTrigID_Jitter;
	var sendTrigID_StartStep;
	var sendTrigID_PlaySteps;
	var sendTrigID_GlobalVelScale;
	var sendTrigID_RandVel;

	var holdModActive = 0;
	var holdModBpm = 0;
	var holdModStepBeats = 0;
	var holdModSwing = 0;
	var holdModJitter = 0;
	var holdModStartStep = 0;
	var holdModPlaySteps = 0;
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
		classData.defaultName = "Note Multiply";
		classData.moduleRate = "control";
		classData.moduleType = "groupaction";
		classData.arrCtlSCInBusSpecs = [
			["Active", 1, "modActive", 0],
			["BPM", 1, "modBpm", 0],
			["Step beats", 1, "modStepBeats", 0],
			["Swing", 1, "modSwing", 0],
			["Jitter", 1, "modJitter", 0],
			["Start step", 1, "modStartStep", 0],
			["Play steps", 1, "modPlaySteps", 0],
			["Velocity scale", 1, "modGlobalVelScale", 0],
			["Randomise vel", 1, "modRandVel", 0],
		];
		classData.noInChannels = 0;
		classData.noOutChannels = 0;
		classData.guiWidth = 1000;
		classData.gateBeatsQuantOptions = [
			["0 (off)", 0],
			["1 beat", 1],
			["1/2 beat", 1/2],
			["1/3 beat", 1/3],
			["1/4 beat", 1/4],
			["1/5 beat", 1/5],
			["1/6 beat", 1/6],
			["1/7 beat", 1/7],
			["1/8 beat", 1/8],
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
		var initGateBeats;
		//	set  class specific instance variables
		initGateBeats = ControlSpec(0.25, 4).unmap(1);
		displayOption = "showOutputs";
		moduleRunning = true;
		sendTrigID_Active = UniqueID.next;
		sendTrigID_BPM = UniqueID.next;
		sendTrigID_StepBeats = UniqueID.next;
		sendTrigID_Swing = UniqueID.next;
		sendTrigID_Jitter = UniqueID.next;
		sendTrigID_StartStep = UniqueID.next;
		sendTrigID_PlaySteps = UniqueID.next;
		sendTrigID_GlobalVelScale = UniqueID.next;
		sendTrigID_RandVel = UniqueID.next;
		arrSynthArgSpecs = [
			["modActive", 0, 0],
			["modBpm", 0, 0],
			["modStepBeats", 0, 0],
			["modSwing", 0, 0],
			["modJitter", 0, 0],
			["modStartStep", 0, 0],
			["modPlaySteps", 0, 0],
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
			["bpm", 0.3333333], 		// default is set for 120 bpm
			["bpmMin", 30],
			["bpmMax", 300],
			["stepBeats", 0.25],
			["stepBeatsMin", 0],
			["stepBeatsMax", 4],
			["swing", 0.5, 0],
			["swingMin", -1],
			["swingMax", 1],
			["jitter", 0, 0],
			["jitterMin", 0],
			["jitterMax", 1],
			["startStep", 0, 0],
			["startStepMin", 1, 0],
			["startStepMax", 16, 0],
			["playSteps", 1, 0],
			["playStepsMin", 0, 0],
			["playStepsMax", 16, 0],
			["gateBeatsMin", 0.25, 0],
			["gateBeatsMax", 4, 0],
			["gateBeatsQuantIndex", 0, 0],
			["transposeMin", -12, 0],
			["transposeMax", 12, 0],
			["transposeQuantise", 1, 0],
			["globalVelScale", 1, 0],
			["randVel", 0, 0],
			["scaleStepVelocities", 1, 0],
			["ignoreFirstStepTranspose", 1, 0],
			["overrideFirstStepVel", 1, 0],
			["overrideFirstStepProbability", 1, 0],
			["randTranspose", 0, 0],
			["randVelocity", 0, 0],
			["randProbability", 0, 0],
			["randGateBeats", 0, 0],

			["transpose1", 0.5, 0],
			["transpose2", 0.5, 0],
			["transpose3", 0.5, 0],
			["transpose4", 0.5, 0],
			["transpose5", 0.5, 0],
			["transpose6", 0.5, 0],
			["transpose7", 0.5, 0],
			["transpose8", 0.5, 0],
			["transpose9", 0.5, 0],
			["transpose10", 0.5, 0],
			["transpose11", 0.5, 0],
			["transpose12", 0.5, 0],
			["transpose13", 0.5, 0],
			["transpose14", 0.5, 0],
			["transpose15", 0.5, 0],
			["transpose16", 0.5, 0],
			["velocity1", 0.5, 0],
			["velocity2", 0.5, 0],
			["velocity3", 0.5, 0],
			["velocity4", 0.5, 0],
			["velocity5", 0.5, 0],
			["velocity6", 0.5, 0],
			["velocity7", 0.5, 0],
			["velocity8", 0.5, 0],
			["velocity9", 0.5, 0],
			["velocity10", 0.5, 0],
			["velocity11", 0.5, 0],
			["velocity12", 0.5, 0],
			["velocity13", 0.5, 0],
			["velocity14", 0.5, 0],
			["velocity15", 0.5, 0],
			["velocity16", 0.5, 0],
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
			["probability13", 1, 0],
			["probability14", 1, 0],
			["probability15", 1, 0],
			["probability16", 1, 0],
			["gateBeats1", initGateBeats, 0],
			["gateBeats2", initGateBeats, 0],
			["gateBeats3", initGateBeats, 0],
			["gateBeats4", initGateBeats, 0],
			["gateBeats5", initGateBeats, 0],
			["gateBeats6", initGateBeats, 0],
			["gateBeats7", initGateBeats, 0],
			["gateBeats8", initGateBeats, 0],
			["gateBeats9", initGateBeats, 0],
			["gateBeats10", initGateBeats, 0],
			["gateBeats11", initGateBeats, 0],
			["gateBeats12", initGateBeats, 0],
			["gateBeats13", initGateBeats, 0],
			["gateBeats14", initGateBeats, 0],
			["gateBeats15", initGateBeats, 0],
			["gateBeats16", initGateBeats, 0],
		];
		synthDefFunc = { arg modActive = 0, modBpm = 0, modStepBeats = 0, modSwing = 0, modJitter = 0,
			modStartStep = 0, modPlaySteps = 0, modGlobalVelScale = 0, modRandVel = 0;

			var trig, trig2, holdImpulse, trigActive, trigBpm, trigStepBeats, trigSwing, trigJitter;
			var trigStartStep, trigPlaySteps, trigGlobalVelScale, trigRandVel;

			modActive = modActive.max(-1).min(1);
			modBpm = modBpm.max(-1).min(1);
			modStepBeats = modStepBeats.max(-1).min(1);
			modSwing = modSwing.max(-1).min(1);
			modJitter = modJitter.max(-1).min(1);
			modStartStep = modStartStep.max(-1).min(1);
			modPlaySteps = modPlaySteps.max(-1).min(1);
			modGlobalVelScale = modGlobalVelScale.max(-1).min(1);
			modRandVel = modRandVel.max(-1).min(1);

			holdImpulse = Impulse.kr(20);

			// trigger current values to be sent when value changes
			trigActive = Trig.kr((1 - holdImpulse) * HPZ1.kr(modActive).abs, 0.05);
			SendTrig.kr(trigActive, sendTrigID_Active, modActive);

			trigBpm = Trig.kr((1 - holdImpulse) * HPZ1.kr(modBpm).abs, 0.05);
			SendTrig.kr(trigBpm, sendTrigID_BPM, modBpm);

			trigStepBeats = Trig.kr((1 - holdImpulse) * HPZ1.kr(modStepBeats).abs, 0.05);
			SendTrig.kr(trigStepBeats, sendTrigID_StepBeats, modStepBeats);

			trigSwing = Trig.kr((1 - holdImpulse) * HPZ1.kr(modSwing).abs, 0.05);
			SendTrig.kr(trigSwing, sendTrigID_Swing, modSwing);

			trigJitter = Trig.kr((1 - holdImpulse) * HPZ1.kr(modJitter).abs, 0.05);
			SendTrig.kr(trigJitter, sendTrigID_Jitter, modJitter);

			trigStartStep = Trig.kr((1 - holdImpulse) * HPZ1.kr(modStartStep).abs, 0.05);
			SendTrig.kr(trigStartStep, sendTrigID_StartStep, modStartStep);

			trigPlaySteps = Trig.kr((1 - holdImpulse) * HPZ1.kr(modPlaySteps).abs, 0.05);
			SendTrig.kr(trigPlaySteps, sendTrigID_PlaySteps, modPlaySteps);

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
				["MIDIChannelSelector", 510],
				["MIDINoteSelector"],
				["MIDIVelSelector"],
				["TXCheckBox", "Active", "active"],
				["TXCheckBox", "Ignore transpose for start step", "ignoreFirstStepTranspose", {}, 250],
				["TXCheckBox", "Use played velocity for start step", "overrideFirstStepVel", {}, 250],
				["TXCheckBox", "Use probability 100% for start step", "overrideFirstStepProbability", {}, 250],
				["EZSlider", "Random vel", ControlSpec(0, 1), "randVel"],
				["EZSlider", "Velocity scale", ControlSpec(0, 1), "globalVelScale"],
				["TXCheckBox", "Scale step velocities to played velocity", "scaleStepVelocities", {}, 250],
				["TXRangeSlider", "Transpose range", ControlSpec(-64, 64), "transposeMin", "transposeMax"],
				["TXCheckBox", "Quantise transpose to semitones", "transposeQuantise", {}, 200],
				["TXRangeSlider", "Gate range", ControlSpec(0.1, 64, 'exp'), "gateBeatsMin", "gateBeatsMax"],
				["TXPopupActionPlusMinus", "Gate quantise", classData.gateBeatsQuantOptions.collect({arg item; item[0]}),
					"gateBeatsQuantIndex",
					{arg view; this.setSynthValue("gateBeatsQuantIndex", view.value);}],
				["TXMinMaxSliderSplit", "Step beats", ControlSpec(0, 16), "stepBeats",
					"stepBeatsMin", "stepBeatsMax"],
				["TXMinMaxSliderSplit", "BPM", ControlSpec(1, 900), "bpm", "bpmMin", "bpmMax"],
				["TXMinMaxSliderSplit", "Swing", ControlSpec(-1, 1), "swing", "swingMin", "swingMax"],
				["TXMinMaxSliderSplit", "Jitter", ControlSpec(0, 1), "jitter", "jitterMin", "jitterMax"],
				["TXMinMaxSliderSplit", "Start step", ControlSpec(1, 16, step: 1), "startStep", "startStepMin", "startStepMax"],
				["TXMinMaxSliderSplit", "Play steps", ControlSpec(1, 16, step: 1), "playSteps", "playStepsMin", "playStepsMax"],
				["TXCheckBox", "randTranspose", "randTranspose"],
				["TXCheckBox", "randVelocity", "randVelocity"],
				["TXCheckBox", "randProbability", "randProbability"],
				["TXCheckBox", "randGateBeats", "randGateBeats"],
			]
			++ ["transpose1", "transpose2", "transpose3", "transpose4", "transpose5", "transpose6", "transpose7",
				"transpose8", "transpose9", "transpose10", "transpose11", "transpose12", "transpose13",
				"transpose14", "transpose15", "transpose16",
			].collect({arg item, i;
				["EZsliderUnmapped", item, {ControlSpec(this.getSynthArgSpec("transposeMin"),
					this.getSynthArgSpec("transposeMax"),
					step: this.getSynthArgSpec("transposeQuantise"))}, item]
			})
			++ ["velocity1", "velocity2", "velocity3", "velocity4", "velocity5", "velocity6", "velocity7", "velocity8",
				"velocity9", "velocity10", "velocity11", "velocity12", "velocity13", "velocity14",
				"velocity15", "velocity16",
			].collect({arg item, i;
				["EZsliderUnmapped", item, {ControlSpec(0, 100)}, item]
			})
			++ ["probability1", "probability2", "probability3", "probability4", "probability5",
				"probability6", "probability7", "probability8", "probability9", "probability10", "probability11",
				"probability12", "probability13", "probability14", "probability15", "probability16",
			].collect({arg item, i;
				["EZsliderUnmapped", item, {ControlSpec(0, 100)}, item]
			})
			++ ["gateBeats1", "gateBeats2", "gateBeats3", "gateBeats4", "gateBeats5", "gateBeats6", "gateBeats7",
				"gateBeats8", "gateBeats9", "gateBeats10", "gateBeats11", "gateBeats12", "gateBeats13", "gateBeats14",
				"gateBeats15", "gateBeats16",
			].collect({arg item, i;
				["EZsliderUnmapped", item, {ControlSpec(this.getSynthArgSpec("gateBeatsMin"),
					this.getSynthArgSpec("gateBeatsMax"),
					step: classData.gateBeatsQuantOptions[this.getSynthArgSpec("gateBeatsQuantIndex")][1])}, item]
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
		var holdGateBeatsMin = this.getSynthArgSpec("gateBeatsMin");
		var holdGateBeatsMax = this.getSynthArgSpec("gateBeatsMax");
		var holdGateBeatsQuantIndex = this.getSynthArgSpec("gateBeatsQuantIndex");
		var holdTransposeMin = this.getSynthArgSpec("transposeMin");
		var holdTransposeMax = this.getSynthArgSpec("transposeMax");

		guiSpecArray = [
			["ActionButton", "MIDI & Outputs", {displayOption = "showOutputs";
				this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayOption == "showOutputs")],
			["Spacer", 3],
			["ActionButton", "Step Parameters", {displayOption = "showStepParameters";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showStepParameters")],
			["Spacer", 3],
			["ActionButton", "Steps", {displayOption = "showSteps";
				this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayOption == "showSteps")],
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
				["TXCheckBox", "Output MIDI notes for each step", "midiNoteOn", nil, 220],
				["SpacerLine", 2],
				["TXPopupAction", "MIDI Port", holdPortNames, "midiPort",
					{ arg view; this.initMidiPort(view.value); }, 300],
				["Spacer", 10],
				["TXCheckBox", "MIDI Loop-Back", "midiLoopBack", nil, 120],
				["SpacerLine", 4],
				["EZSlider", "MIDI Channel", ControlSpec(1, 16, step: 1), "midiChannel", nil, 510],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["MIDIKeyboard", {arg note; this.createSynthNote(note, testMIDIVel, 0.5);},
					7, 80, 756, 24, {}, "C0 - B7"],
			];
		});
		if (displayOption == "showStepParameters", {
			guiSpecArray = guiSpecArray ++[
				["TXMinMaxSliderSplit", "Step beats", ControlSpec(0, 16), "stepBeats",
					"stepBeatsMin", "stepBeatsMax", nil, nil, nil, nil, 670],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "BPM", ControlSpec(1, 900), "bpm", "bpmMin", "bpmMax", nil, nil, nil, nil, 670],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Swing", ControlSpec(-1, 1), "swing", "swingMin", "swingMax", nil, nil, nil, nil, 670],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Jitter", ControlSpec(0, 1), "jitter", "jitterMin", "jitterMax", nil, nil, nil, nil, 670],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXRangeSlider", "Transp. range", ControlSpec(-64, 64), "transposeMin", "transposeMax", nil, nil, 670],
				["Spacer", 30],
				["TXCheckBox", "Quantise transpose to semitones", "transposeQuantise", {}, 240],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZSlider", "Randomise vel", ControlSpec(0, 1), "randVel", nil, 670],
				["SpacerLine", 6],
				["EZSlider", "Velocity scale", ControlSpec(0, 1), "globalVelScale", nil, 670],
				["Spacer", 30],
				["TXCheckBox", "Scale step velocities to played velocity", "scaleStepVelocities", {}, 240],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXRangeSlider", "Gate range", ControlSpec(0.1, 64, 'exp'), "gateBeatsMin", "gateBeatsMax", nil, nil, 670],
				["Spacer", 30],
				["TXPopupActionPlusMinus", "Gate quantise", classData.gateBeatsQuantOptions.collect({arg item; item[0]}),
					"gateBeatsQuantIndex",
					{arg view; this.setSynthValue("gateBeatsQuantIndex", view.value);}, 240],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXCheckBox", "Ignore transpose for start step", "ignoreFirstStepTranspose", {}, 220],
				["Spacer", 10],
				["TXCheckBox", "Use played velocity for start step", "overrideFirstStepVel", {}, 220],
				["Spacer", 10],
				["TXCheckBox", "Use probability 100% for start step", "overrideFirstStepProbability", {}, 220],
				["SpacerLine", 6],
				["TXCheckBox", "Randomise transpose step order", "randTranspose", {}, 220],
				["Spacer", 10],
				["TXCheckBox", "Randomise velocity step order", "randVelocity", {}, 220],
				["Spacer", 10],
				["TXCheckBox", "Randomise probability step order", "randProbability", {}, 220],
				["Spacer", 10],
				["TXCheckBox", "Randomise gate beats step order", "randGateBeats", {}, 220],
			];
		});
		if (displayOption == "showSteps", {
			guiSpecArray = guiSpecArray ++[
				["TXMinMaxSliderSplit", "Start step", ControlSpec(1, 16, step: 1),
					"startStep", "startStepMin", "startStepMax", nil, nil, nil, nil, 670],
				["SpacerLine", 4],
				["TXMinMaxSliderSplit", "Play steps", ControlSpec(1, 16, step: 1),
					"playSteps", "playStepsMin", "playStepsMax", nil, nil, nil, nil, 670],
				["SpacerLine", 10],
				["TextBar", "Steps", 80],
				["TextBar", "Step 1", 50],
				["TextBar", "Step 2", 50],
				["TextBar", "Step 3", 50],
				["TextBar", "Step 4", 50],
				["TextBar", "Step 5", 50],
				["TextBar", "Step 6", 50],
				["TextBar", "Step 7", 50],
				["TextBar", "Step 8", 50],
				["TextBar", "Step 9", 50],
				["TextBar", "Step 10", 50],
				["TextBar", "Step 11", 50],
				["TextBar", "Step 12", 50],
				["TextBar", "Step 13", 50],
				["TextBar", "Step 14", 50],
				["TextBar", "Step 15", 50],
				["TextBar", "Step 16", 50],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",  {this.copy1ToAll([ "transpose1", "transpose2", "transpose3", "transpose4",
					"transpose5", "transpose6", "transpose7", "transpose8", "transpose9", "transpose10", "transpose11",
					"transpose12", "transpose13", "transpose14", "transpose15", "transpose16", ]);}, 36],
				["TXMultiKnobNoUnmap", "Trans pose", [ "transpose1", "transpose2", "transpose3", "transpose4", "transpose5", "transpose6",
					"transpose7", "transpose8", "transpose9", "transpose10", "transpose11", "transpose12", "transpose13",
					"transpose14", "transpose15", "transpose16", ], 16, ControlSpec(holdTransposeMin, holdTransposeMax,
					step: this.getSynthArgSpec("transposeQuantise"))],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["velocity1", "velocity2", "velocity3", "velocity4", "velocity5",
					"velocity6", "velocity7", "velocity8", "velocity9", "velocity10", "velocity11", "velocity12", "velocity13",
					"velocity14", "velocity15", "velocity16" ]);}, 36],
				["TXMultiKnobNoUnmap", "Velo city", ["velocity1", "velocity2", "velocity3", "velocity4", "velocity5", "velocity6",
					"velocity7", "velocity8", "velocity9", "velocity10", "velocity11", "velocity12", "velocity13", "velocity14",
					"velocity15", "velocity16" ], 16, ControlSpec(0, 100)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll([ "probability1", "probability2", "probability3", "probability4",
					"probability5", "probability6", "probability7", "probability8", "probability9", "probability10", "probability11",
					"probability12", "probability13", "probability14", "probability15", "probability16", ]);}, 36],
				["TXMultiKnobNoUnmap", "Proba bility", [ "probability1", "probability2", "probability3", "probability4",
					"probability5","probability6", "probability7", "probability8", "probability9", "probability10",
					"probability11", "probability12", "probability13", "probability14", "probability15",
					"probability16", ], 16, ControlSpec(0, 100)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll([ "gateBeats1", "gateBeats2", "gateBeats3", "gateBeats4",
					"gateBeats5", "gateBeats6", "gateBeats7", "gateBeats8", "gateBeats9", "gateBeats10", "gateBeats11",
					"gateBeats12", "gateBeats13", "gateBeats14", "gateBeats15", "gateBeats16", ]);}, 36],
				["TXMultiKnobNoUnmap", "Gate beats", [ "gateBeats1", "gateBeats2", "gateBeats3", "gateBeats4", "gateBeats5",
					"gateBeats6", "gateBeats7", "gateBeats8", "gateBeats9", "gateBeats10", "gateBeats11",
					"gateBeats12", "gateBeats13", "gateBeats14", "gateBeats15", "gateBeats16", ], 16,
				ControlSpec(holdGateBeatsMin, holdGateBeatsMax,
					step: classData.gateBeatsQuantOptions[holdGateBeatsQuantIndex][1])],
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

	oscControlActivate {
		//	remove any previous OSCresponderNode and add new
		this.oscControlDeactivate;
		oscControlFunc = OSCFunc({arg msg, time;

			if (msg[2] == sendTrigID_Active,{
				holdModActive = msg[3];
			});
			if (msg[2] == sendTrigID_BPM,{
				holdModBpm = msg[3];
			});
			if (msg[2] == sendTrigID_StepBeats,{
				holdModStepBeats = msg[3];
			});
			if (msg[2] == sendTrigID_Swing,{
				holdModSwing = msg[3];
			});
			if (msg[2] == sendTrigID_Jitter,{
				holdModJitter = msg[3];
			});
			if (msg[2] == sendTrigID_StartStep,{
				holdModStartStep = msg[3];
			});
			if (msg[2] == sendTrigID_PlaySteps,{
				holdModPlaySteps = msg[3];
			});
			if (msg[2] == sendTrigID_GlobalVelScale,{
				holdModGlobalVelScale = msg[3];
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

	arrActiveModules {
		var arrModules;
		// retrigger modules
		arrModules = [noteOutModule1, noteOutModule2, noteOutModule3, noteOutModule4, noteOutModule5, noteOutModule6,
		].select({ arg item, i; item.notNil and: {item != 0}});
		^arrModules;
	}

	arrCurrStepIndices {
		var arrIndices, sumStartStep, sumPlaySteps;
		sumStartStep = (this.getSynthArgSpec("startStep") + holdModStartStep).max(0).min(1).linlin(0, 1,
			this.getSynthArgSpec("startStepMin"), this.getSynthArgSpec("startStepMax")).round;
		sumPlaySteps = (this.getSynthArgSpec("playSteps") + holdModPlaySteps).max(0).min(1).linlin(0, 1,
			this.getSynthArgSpec("playStepsMin"), this.getSynthArgSpec("playStepsMax")).round;
		arrIndices = sumPlaySteps.asInteger.collect({arg i;
			((sumStartStep - 1) + i) % 16;
		});
		^arrIndices;
	}

	getCurrGlobalVelScale {
		var globalVelScale;
		globalVelScale = (this.getSynthArgSpec("globalVelScale") + holdModGlobalVelScale).max(0).min(1);
		^globalVelScale;
	}

	getCurrBpm {
		var bpm;
		bpm = (this.getSynthArgSpec("bpm") + holdModBpm).max(0).min(1).linlin(0, 1,
			this.getSynthArgSpec("bpmMin"), this.getSynthArgSpec("bpmMax"));
		^bpm;
	}

	getCurrJitter {
		var rand;
		rand = (this.getSynthArgSpec("jitter") + holdModJitter).max(0).min(1).linlin(0, 1,
			this.getSynthArgSpec("jitterMin"), this.getSynthArgSpec("jitterMax"));
		^rand;
	}

	getCurrSwing {
		var swing;
		swing = (this.getSynthArgSpec("swing") + holdModSwing).max(0).min(1).linlin(0, 1,
			this.getSynthArgSpec("swingMin"), this.getSynthArgSpec("swingMax"));
		^swing;
	}

	getCurrStepBeats {
		var stepBeats;
		stepBeats = (this.getSynthArgSpec("stepBeats") + holdModStepBeats).max(0).min(1).linlin(0, 1,
			this.getSynthArgSpec("stepBeatsMin"), this.getSynthArgSpec("stepBeatsMax"));
		^stepBeats;
	}

	getCurrRandVel {
		var randVel;
		randVel = (this.getSynthArgSpec("randVel") + holdModRandVel).max(0).min(1);
		^randVel;
	}

	arrTransposeVals {arg arrIndices;
		var holdInds, arrStrings, arrVals;
		holdInds = arrIndices;
		if (this.getSynthArgSpec("randTranspose") == 1, {
			holdInds = holdInds.scramble;
		});
		arrStrings =  ["transpose1", "transpose2", "transpose3", "transpose4", "transpose5",
			"transpose6", "transpose7", "transpose8", "transpose9", "transpose10",
			"transpose11", "transpose12", "transpose13", "transpose14", "transpose15", "transpose16", ];
		arrVals = arrStrings[holdInds].collect({ arg item, i;
			this.getSynthArgSpec(item);
		});
		^arrVals;
	}

	arrVelocityVals {arg arrIndices;
		var holdInds, arrStrings, arrVals;
		holdInds = arrIndices;
		if (this.getSynthArgSpec("randVelocity") == 1, {
			holdInds = holdInds.scramble;
		});
		arrStrings =  ["velocity1", "velocity2", "velocity3", "velocity4", "velocity5",
			"velocity6", "velocity7", "velocity8", "velocity9", "velocity10",
			"velocity11", "velocity12", "velocity13", "velocity14", "velocity15", "velocity16", ];
		arrVals = arrStrings[holdInds].collect({ arg item, i;
			this.getSynthArgSpec(item);
		});
		^arrVals;
	}

	arrProbabilityVals {arg arrIndices;
		var holdInds, arrStrings, arrVals;
		holdInds = arrIndices;
		if (this.getSynthArgSpec("randProbability") == 1, {
			holdInds = holdInds.scramble;
		});
		arrStrings =  ["probability1", "probability2", "probability3", "probability4", "probability5",
			"probability6", "probability7", "probability8", "probability9", "probability10",
			"probability11", "probability12", "probability13", "probability14", "probability15", "probability16", ];
		arrVals = arrStrings[holdInds].collect({ arg item, i;
			this.getSynthArgSpec(item);
		});
		^arrVals;
	}

	arrGateBeatsVals {arg arrIndices;
		var holdInds, arrStrings, arrVals;
		holdInds = arrIndices;
		if (this.getSynthArgSpec("randGateBeats") == 1, {
			holdInds = holdInds.scramble;
		});
		arrStrings =  ["gateBeats1", "gateBeats2", "gateBeats3", "gateBeats4", "gateBeats5",
			"gateBeats6", "gateBeats7", "gateBeats8", "gateBeats9", "gateBeats10",
			"gateBeats11", "gateBeats12", "gateBeats13", "gateBeats14", "gateBeats15", "gateBeats16", ];
		arrVals = arrStrings[holdInds].collect({ arg item, i;
			this.getSynthArgSpec(item);
		});
		^arrVals;
	}

	////////////////////////////////////

	createSynthNote { arg note, vel, argEnvTime=1, seqLatencyOn=1, argDetune=0;
		var holdCurrIndices, holdProbVals, holdVelVals, holdGateBeatsVals, holdTransposeVals, holdScaleVels;
		var holdBpm, holdBeatTime, holdStepTime;
		var holdGateBeatsMin, holdGateBeatsMax, holdGateBeatsQuant;
		var holdTransposeMin, holdTransposeMax, holdTransposeQuant;
		var velNorm, holdGlobalVelScale, holdRandVel, holdCurrJitter, holdRandomTime, holdSwingTime;
		var sumActive, activeModules, stepCount;

		sumActive = (this.getSynthArgSpec("active") + holdModActive).round;
		activeModules = this.arrActiveModules;

		if ((moduleRunning == true) and: (sumActive > 0), {

			// prep
			holdCurrIndices = this.arrCurrStepIndices;
			stepCount = holdCurrIndices.size;
			holdTransposeVals = this.arrTransposeVals(holdCurrIndices);
			holdVelVals = this.arrVelocityVals(holdCurrIndices);
			holdProbVals = this.arrProbabilityVals(holdCurrIndices);
			holdGateBeatsVals = this.arrGateBeatsVals(holdCurrIndices);
			holdBpm = this.getCurrBpm;
			holdBeatTime = 60 / holdBpm;
			holdStepTime = holdBeatTime * this.getCurrStepBeats;
			holdSwingTime = this.getCurrSwing * holdStepTime;
			holdCurrJitter = this.getCurrJitter;
			holdRandomTime = holdCurrJitter * holdStepTime;
			// holdGlobalVelScale = this.getCurrGlobalVelScale;
			holdRandVel = this.getCurrRandVel;
			holdScaleVels = this.getSynthArgSpec("scaleStepVelocities");
			velNorm = vel/127;
			holdGateBeatsMin = this.getSynthArgSpec("gateBeatsMin");
			holdGateBeatsMax = this.getSynthArgSpec("gateBeatsMax");
			holdGateBeatsQuant = classData.gateBeatsQuantOptions[this.getSynthArgSpec("gateBeatsQuantIndex")][1];
			holdTransposeMin = this.getSynthArgSpec("transposeMin");
			holdTransposeMax = this.getSynthArgSpec("transposeMax");
			holdTransposeQuant = this.getSynthArgSpec("transposeQuantise");

			// for all active steps
			stepCount.do({arg i;
				var holdProb, holdVel, holdEnvTime, holdTranspose, swingMultiply, holdDelay, holdGateBeats;
				// if the fates allow it
				if (i == 0 and: {this.getSynthArgSpec("overrideFirstStepProbability") == 1}, {
					holdProb = 1;
				}, {
					holdProb = holdProbVals[i];
				});
				if ((holdProb - rand(1.0)).ceil == 1, {
					// vel
					if (i == 0 and: {this.getSynthArgSpec("overrideFirstStepVel") == 1}, {
						holdVel = vel;
					}, {
						holdVel = holdVelVals[i] * 127;
						if (holdScaleVels == 1, {
							// holdVel = holdVel * velNorm * holdGlobalVelScale;
							holdVel = holdVel * velNorm;
						});
					});
					if (holdRandVel > 0, {
						holdVel = (holdVel * (1-holdRandVel)) + (127.0.rand * holdRandVel);
					});
					holdGateBeats = holdGateBeatsVals[i].linlin(0, 1, holdGateBeatsMin, holdGateBeatsMax)
					.round(holdGateBeatsQuant);
					holdEnvTime = holdGateBeats * holdBeatTime;
					if (i == 0 and: {this.getSynthArgSpec("ignoreFirstStepTranspose") == 1}, {
						holdTranspose = 0;
					}, {
						holdTranspose = holdTransposeVals[i].linlin(0, 1, holdTransposeMin,
							holdTransposeMax).round(holdTransposeQuant);
					});
					// delay
					swingMultiply = [0, 1].at(i % 2) * 0.5;
					if (i == 0, {
						holdDelay = 0;
					}, {
						holdDelay = (i * holdStepTime)
						+ (swingMultiply * holdSwingTime * (1 - holdCurrJitter))
						+ (0.5.rand2 * holdRandomTime);
					});
					// for all active modules create note at scheduled time
					activeModules.do({ arg item, i;
						// use bundle
						//system.server.makeBundle(holdDelay, {
						// use clock instead
						SystemClock.sched(holdDelay, {
							item.createSynthNote(note + holdTranspose, holdVel * this.getCurrGlobalVelScale,
								holdEnvTime, seqLatencyOn, argDetune);
						});
					});
					// output midi with delay
					this.sendMIDIMessage(note + holdTranspose, holdVel, holdEnvTime, holdDelay);
				});
			});
		});
	}

	// OLD COPIED CODE NOT NEEDED FOR NOW SINCE ONLY NOTE ONS ARE USED:
	// midiNoteRelease { arg note; // override default method
	// 	var arrValidModules;
	// 	if (moduleRunning == true, {
	// 		// only release if sustain pedal is off
	// 		if (midiSustainPedalState == 0, {
	// 			// for all active modules pass data
	// 			this.arrActiveModules.do({ arg item, i;
	// 				item.midiNoteRelease(note);
	// 			});
	// 			},{
	// 				arrHeldMidiNotes = arrHeldMidiNotes.add(note);
	// 		});
	// 	});
	// }
	// releaseSynthGate { arg argNote=60; // override default method
	// 	// for all active modules pass data
	// 	this.arrActiveModules.do({ arg item, i;
	// 		item.releaseSynthGate(argNote);
	// 	});
	// }
	// setMidiSustainPedalState { arg inVal;  // override default method
	// 	// for all active modules pass data
	// 	this.arrActiveModules.do({ arg item, i;
	// 		item.setMidiSustainPedalState(inVal);
	// 	});
	// }

	setMidiBend { arg inVal;  // override default method
		// for all active modules pass data
		this.arrActiveModules.do({ arg item, i;
			item.setMidiBend(inVal);
		});
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

	sendMIDIMessage { arg outNote, outVel, gateTime, delay = 0;
		var portInd, channel, midiLoopBack, midiNoteOn;
		portInd = this.getSynthArgSpec("midiPort");
		midiLoopBack = this.getSynthArgSpec("midiLoopBack");
		channel = this.getSynthArgSpec("midiChannel") - 1;
		midiNoteOn = this.getSynthArgSpec("midiNoteOn");
		if (midiNoteOn == 1, {
			if (midiLoopBack == 1, {
				SystemClock.sched( delay,{
					MIDIIn.doNoteOnAction(0, channel, outNote, this.getCurrGlobalVelScale * outVel);
					nil;
				});
				SystemClock.sched( delay + gateTime,{
					MIDIIn.doNoteOffAction(0, channel, outNote, 0);
					nil;
				});
			});
			if (portInd > 0, {
				// "Note On & Off"
				SystemClock.sched( delay,{
					holdMIDIOutPort.noteOn(channel, outNote, this.getCurrGlobalVelScale * outVel);
					nil;
				});
				SystemClock.sched( delay + gateTime,{
					holdMIDIOutPort.noteOff(channel, outNote, 0);
					nil;
				});
			});
		});
	}

}

