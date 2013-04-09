// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXStepSequencer : TXModuleBase {

	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar	<guiWidth = 810;
	classvar	<seqLatency = 0.1;	// all seqs should use same small latency for server timing

	var		<seqClock; 		// clock for sequencer
	var		<seqCurrentStep;
	var		<seqRunning = false;
//	var		<seqResetCounter;   // not used now
	var		<>noteOutModule1;
	var		<>noteOutModule2;
	var		<>noteOutModule3;
	var		<>noteOutModule4;
	var		<>noteOutModule5;
	var		<>noteOutModule6;
//	var		seqRecordStep = 0;  // not used now
	var		<>arrSlots;
	var		arrParmNames;
	var		<>parmIndex = 0;
	var		<>displayOctave = 4;
	var		<arrProcessSpecs;	// used for processing
	var		arrProcSelNames;
	var		arrProcSynthArgNames;
	var		arrProcSelGroupNames;
	var		arrProcGroupSynthArgNames;
	var 		loTargetIndex, hiTargetIndex, targetSize;
	var		slotClipboard;
	var		arrSeqRangePresets;
	var		arrBPMRangePresets;
	var		arrStepNoteLenPresets;
	var		holdKeybOctaves;
	var 		arrScrollViewsH, arrScrollViewsV, arrScrollViewsVH;
	var 		holdVisibleOrigin;
	var		holdModSeqBPM = 0;
	var		oscControlResp;
	var		sendTrigID;
	var		<>patternView;
	var		arrRandOrderNames;
	var		midiControlResp;
	var		holdPortNames, holdMIDIOutPort, holdMIDIDeviceName, holdMIDIPortName;
	var 		holdTapTime, newTapTime;
	var 		holdChainCurrentStep, holdSlotNo;
	var		seqEnded = false;
	var		<>triggerStatus;
	var		<>triggerStatusView;
	var		<>triggerModeRunning = false;
	var		<>runningStatus;
	var		<>runningStatusView;

*initClass {
	arrInstances = [];
	//	set class specific variables
	defaultName = "Step Sequencer";
	moduleRate = "control";
	moduleType = "source";
	noInChannels = 0;
	arrCtlSCInBusSpecs = [
		["BPM", 1, "modSeqBPM", 0],
	];
	noOutChannels = 6;
	arrOutBusSpecs = [
		["Note Out", [0]],
		["Velocity Out", [1]],
		["Control 1", [2]],
		["Control 2", [3]],
		["Control 3", [4]],
		["Note Trigger", [5]],
	];
}

*new { arg argInstName;
	 ^super.new.init(argInstName);
}

*restoreAllOutputs {
	 arrInstances.do({ arg item, i;
	 	item.restoreOutputs;
	 });
}

*syncStartAllSequencers {
	 arrInstances.do({ arg item, i;
	 	item.syncStartSequencer;
	 });
}

*syncStopAllSequencers {
	 arrInstances.do({ arg item, i;
	 	item.syncStopSequencer;
	 });
}
*stopAllSequencers {
	 arrInstances.do({ arg item, i;
	 	item.triggerModeRunning = false;
	 	item.stopSequencer;
	 	item.prepareToStartSeq;
	 });
}

////////////////////////////////////////////////////////////////////////////

init {arg argInstName;
	var holdControlSpecBPM;

	//	set  class specific instance variables
	holdVisibleOrigin = Point.new(0,480);
	// create unique id
	sendTrigID = UniqueID.next;
	arrSeqRangePresets = [
		["Presets: ", []],
		["1 - 64", [1, 64]],
		["1 - 4", [1, 4]],
		["1 - 6", [1, 6]],
		["1 - 8", [1, 8]],
		["1 - 9", [1, 9]],
		["1 - 12", [1, 12]],
		["1 - 16", [1, 16]],
		["1 - 24", [1, 24]],
		["1 - 32", [1, 32]],
		["5 - 64", [5, 64]],
		["9 - 64", [9, 64]],
		["17 - 32", [17, 32]],
		["17 - 64", [17, 64]],
		["33 - 48", [33, 48]],
		["49 - 64", [49, 64]],
	];
	arrBPMRangePresets = [
		["Presets: ", []],
		["30 - 300", [30, 300]],
		["60 - 170", [60, 170]],
		["60 - 90", [60, 90]],
		["70 - 100", [70, 100]],
		["80 - 110", [80, 110]],
		["90 - 120", [90, 120]],
		["100 - 130", [100, 130]],
		["110 - 140", [110, 140]],
		["120 - 150", [120, 150]],
		["130 - 160", [120, 160]],
		["140 - 170", [140, 170]],
		["150 - 180", [150, 180]],
		["160 - 190", [160, 190]],
	];
	arrStepNoteLenPresets = [
		["Select preset to set step & notes length values", []],
		["   16 | 1", [16, 1]],
		["   12 | 1", [12, 1]],
		["   9  | 1", [9, 1]],
		["   8  | 1", [8, 1]],
		["   7  | 1", [7, 1]],
		["   6  | 1", [6, 1]],
		["   5  | 1", [5, 1]],
		["   4  | 1", [4, 1]],
		["   3  | 1", [3, 1]],
		["   2  | 1", [2, 1]],
		["   1  | 1", [1, 1]],
		["   1  | 2", [1, 2]],
		["   1  | 3", [1, 3]],
		["   1  | 4", [1, 4]],
		["   1  | 5", [1, 5]],
		["   1  | 6", [1, 6]],
		["   1  | 7", [1, 7]],
		["   1  | 8", [1, 8]],
		["   1  | 9", [1, 9]],
		["   1  | 12", [1, 12]],
		["   1  | 16", [1, 16]],
	];
	arrRandOrderNames = ["randOrderOnOffSteps", "randOrderNotes",
		"randOrderRandOctaves", "randOrderVelocities",
		"randOrderRandVelocities", "randOrderStepLengthsX", "randOrderStepLengthsY",
		"randOrderNoteLengthsX", "randOrderNoteLengthsY", "randOrderProbabilities",
		"randOrderDelays", "randOrderRandDelays",
		"randOrderControl1s", "randOrderControl2s", "randOrderControl3s",
	];
	this.initProcessSpecs;
	arrSlots = Array.fill(100, nil);
	arrSynthArgSpecs = [
		["out", 0, 0],
		["seqOn", 0, 0],
		["modSeqBPM", 0, 0],

		// N.B. the args below aren't used in the synthdef, just stored here for convenience

		["seqBPM", 0.3333333], 		// default is set for 120 bpm
		["seqBPMMin", 30],
		["seqBPMMax", 300],
		["seqStartStep", 1],
		["seqEndStep", 16],
		[ "displayFirstStep", 0],
		["syncStart", 1],
		["syncStop", 1],
		["seqNoteBase", 48],

 		["arrNotes", Array.fill(64, 0)],
		["arrRandOctaves", Array.fill(64, 0)],
		["arrVelocities", Array.fill(64, 100)],
		["arrRandVelocities", Array.fill(64, 0)],
		["velMin", 0],
		["velMax", 100],
		["arrStepLengthsX", Array.fill(64, 1)],
		["arrStepLengthsY", Array.fill(64, 1)],
		["arrNoteLengthsX", Array.fill(64, 1)],
		["arrNoteLengthsY", Array.fill(64, 1)],
		["arrProbabilities", Array.fill(64, 100)],
		["arrOnOffSteps", Array.fill(64, 1)],
		["arrDelays", Array.fill(64, 0)],
		["arrRandDelays", Array.fill(64, 0)],
		["arrControl1", Array.fill(64, 0)],
		["arrControl2", Array.fill(64, 0)],
		["arrControl3", Array.fill(64, 0)],
		["arrGroup1s", Array.fill(64, 0)],
		["arrGroup2s", Array.fill(64, 0)],
		["arrGroup3s", Array.fill(64, 0)],
		["arrGroup4s", Array.fill(64, 0)],
		["arrGroup5s", Array.fill(64, 0)],
		["arrGroup6s", Array.fill(64, 0)],
		["arrGroup7s", Array.fill(64, 0)],
		["arrGroup8s", Array.fill(64, 0)],

		["noteOutModuleID1", nil],
		["noteOutModuleID2", nil],
		["noteOutModuleID3", nil],
		["noteOutModuleID4", nil],
		["noteOutModuleID5", nil],
		["noteOutModuleID6", nil],

		["midiLearn", 0],
		["midiLearnStep", 1],
		["showRandOctBars", 1],
		["showVelocityBars", 1],
		["showRandVelBars", 1],
		["showProbabilityBars", 1],
		["showDelayBars", 1],
		["showRandDelayBars", 1],
		["showControl1Bars", 1],
		["showControl2Bars", 1],
		["showControl3Bars", 1],
 		["description", ""],
 		["chainStartStep", 1],
		["chainEndStep", 1],
		["chainCurrentStep", 1],
		["chainLoop", 1],
		["arrChainSlots", Array.fill(100, 0)],
		["displayFirstChainStep", 0],
 		["slotNo", 0],
 		["displayOption", "showOutputs"],
		["nextPatternInd", 1],
		["muteSeq", 0],
		["randTrigger", 0],	// no longer used since playMode added
		["playMode", 0],
		["randomiseStepOrder", 0],
		["stepOrderRandArr", Array.series(64)],
		["stepNoteLenPresetInd", 0],

 		["randOrderOnOffSteps", 1],
		["randOrderNotes", 1],
		["randOrderRandOctaves", 1],
		["randOrderVelocities", 1],
		["randOrderRandVelocities", 1],
		["randOrderStepLengthsX", 1],
		["randOrderStepLengthsY", 1],
		["randOrderNoteLengthsX", 1],
		["randOrderNoteLengthsY", 1],
		["randOrderProbabilities", 1],
		["randOrderDelays", 1],
		["randOrderRandDelays", 1],
		["randOrderControl1s", 1],
		["randOrderControl2s", 1],
		["randOrderControl3s", 1],

		["processTypeInd", 0],
		["procTargStart", 1],
		["procTargEnd", 64],
		["procCopyTargStart", 17],
		["procCopyTargEnd", 64],
		["procSourceStart", 1],
		["procSourceEnd", 16],
		["procShiftSteps", 1],
		["procOctTranspMin", 0],
		["procOctTranspMax", 0],
		["procScaleTypeInd", 0],
		["procScaleRoot", 48],
		["procNoteOrderInd", 0],
		["procTranspose", 1],

 		["procSelOnOffSteps", 1],
		["procSelNotes", 1],
		["procSelRandOctaves", 1],
		["procSelVelocities", 1],
		["procSelRandVelocities", 1],
		["procSelStepLengthsX", 1],
		["procSelStepLengthsY", 1],
		["procSelNoteLengthsX", 1],
		["procSelNoteLengthsY", 1],
		["procSelProbabilities", 1],
		["procSelDelays", 1],
		["procSelRandDelays", 1],
		["procSelControl1s", 1],
		["procSelControl2s", 1],
		["procSelControl3s", 1],
		["procSelGroup1s", 1],
		["procSelGroup2s", 1],
		["procSelGroup3s", 1],
		["procSelGroup4s", 1],
		["procSelGroup5s", 1],
		["procSelGroup6s", 1],
		["procSelGroup7s", 1],
		["procSelGroup8s", 1],

		["procRandOnProb", 50],
		["procRandNoteMin", 24],
		["procRandNoteMax", 72],
		["procRandVelMin", 0],
		["procRandVelMax", 100],
		["procRandStepXMin", 1],
		["procRandStepXMax", 4],
		["procRandStepYMin", 1],
		["procRandStepYMax", 4],
		["procRandNoteXMin", 1],
		["procRandNoteXMax", 4],
		["procRandNoteYMin", 1],
		["procRandNoteYMax", 4],
		["procRandProbMin", 0],
		["procRandProbMax", 100],
		["procRandDelayMin", 0],
		["procRandDelayMax", 100],
		["procRandControlMin", 0],
		["procRandControlMax", 100],

		["midiPort", 0, 0],
		["midiChannel", 1, 0],
		["midiControlNo1", 21, 0],
		["midiControlNo2", 22, 0],
		["midiControlNo3", 23, 0],
		["midiNoteOn", 0, 0],
		["midiControl1On", 0, 0],
		["midiControl2On", 0, 0],
		["midiControl3On", 0, 0],

		["autoTapTempo", 0, 0],

//		["seqResetStep", 64],	// not used now
	];
	synthDefFunc = { arg out, seqOn, modSeqBPM;
		var trig, trig2, sumControl;
		modSeqBPM = modSeqBPM.max(-1).min(1);
		// trigger current value to be sent every sec and when value changes
		trig = Trig.kr((1 - Impulse.kr(20)) * HPZ1.kr(modSeqBPM).abs, 0.005);
		trig2 = Impulse.kr(1);
		SendTrig.kr((trig + trig2) * seqOn, sendTrigID, modSeqBPM);
		// Note this synth doesn't need to write to the output bus
	};

	seqCurrentStep = 0;
	seqRunning = false;
	this.updateRunningStatus(" STOPPED");
// not used now
//	seqResetCounter = 0;

	holdControlSpecBPM = ControlSpec(1, 999);
	guiSpecTitleArray = [
		["TitleBar"],
		["Spacer", 3],
		["HelpButton"],
			["Spacer", 3],
		["DeleteModuleButton"],
			["Spacer", 3],
		["ActionButton", "Start",
			{this.startSequencer;
				system.showViewIfModDisplay(this);
			}, 50, nil, TXColor.sysGuiCol2],
			["Spacer", 3],
		["ActionButton", "Stop",
			{triggerModeRunning = false; this.stopSequencer; this.prepareToStartSeq;
				system.showViewIfModDisplay(this);}, 50, nil, TXColor.sysGuiCol2],
		["Spacer", 3],
		["TXStaticText", "Status", {this.runningStatus},
			{arg view; runningStatusView = view.textView}, 110, 40, TXColor.paleYellow2],
			["Spacer", 3],
		["SeqSyncStartCheckBox"],
			["Spacer", 3],
		["SeqSyncStopCheckBox"],
			["Spacer", 3],
		["HideModuleButton"],
		["NextLine"],
		["ModuleActionPopup"],
		["ModuleInfoTxt"],
		["SpacerLine", 2],
	];
	arrActionSpecs = this.buildActionSpecs([
		["commandAction", "Start Sequencer", {this.startSequencer; system.showViewIfModDisplay(this);}],
		["commandAction", "Stop Sequencer", {triggerModeRunning = false; this.stopSequencer;
			this.prepareToStartSeq; system.showViewIfModDisplay(this);}],
		["TXStaticText", "Next step", {this.triggerStatus}, {arg view; triggerStatusView = view}, 378],
		["commandAction", "Rewind Sequencer - in Manual Trigger Mode",
			{this.stopSequencer; this.prepareToStartSeq}],
		["commandAction", "Trigger Next Step - in Manual Trigger Mode", {this.triggerNextStep;}],
		["commandAction", "Skip Next Step - in Manual Trigger Mode", {this.skipNextStep;}],
		["TXPopupActionPlusMinus", "Play Mode",
			["Normal",
				"Random Trigger - steps are randomly triggered based on BPM. Step lengths are ignored.",
				"Manual Trigger - steps are manually triggered. BPM & Step lengths are ignored."
			], "playMode",
			{triggerModeRunning = false; this.stopSequencer; this.prepareToStartSeq;},
			700
		],
		["TXMinMaxSliderSplit", "BPM", ControlSpec(1, 900), "seqBPM", "seqBPMMin", "seqBPMMax",
			{arg view; this.setTempo; this.updatePatternTime;}],
		["commandAction", "Tap Tempo", {this.actionTapTempo;}],
		["TXCheckBox", "Auto copy tap tempo to BPM ", "autoTapTempo", nil, 200],
		["TXCheckBox", "Mute - all steps are turned off", "muteSeq", nil, 450],
		["TXCheckBox", "Randomise play steps order for selected parameters:", "randomiseStepOrder",
			{this.rebuildStepOrderRandArr; this.buildGuiSpecArray; system.showViewIfModDisplay(this);},
			390],
		["TXCheckBox", "Randomise On-Off Steps", "randOrderOnOffSteps", nil, 120],
 		["TXCheckBox", "Randomise Notes", "randOrderNotes", nil, 120],
		["TXCheckBox", "Randomise Rand Octaves", "randOrderRandOctaves", nil, 120],
		["TXCheckBox", "Randomise Velocities", "randOrderVelocities", nil, 120],
		["TXCheckBox", "Randomise Rand Velocities", "randOrderRandVelocities", nil, 120],
		["TXCheckBox", "Randomise Step Lengths X", "randOrderStepLengthsX", nil, 120],
		["TXCheckBox", "Randomise Step Lengths Y", "randOrderStepLengthsY", nil, 120],
		["TXCheckBox", "Randomise Note Lengths X", "randOrderNoteLengthsX", nil, 120],
		["TXCheckBox", "Randomise Note Lengths Y", "randOrderNoteLengthsY", nil, 120],
		["TXCheckBox", "Randomise Probabilities", "randOrderProbabilities", nil, 120],
		["TXCheckBox", "Randomise Delays", "randOrderDelays", nil, 120],
		["TXCheckBox", "Randomise Rand delays", "randOrderRandDelays", nil, 120],
		["TXCheckBox", "Randomise Control 1", "randOrderControl1s", nil, 120],
		["TXCheckBox", "Randomise Control 2", "randOrderControl2s", nil, 120],
		["TXCheckBox", "Randomise Control 3", "randOrderControl3s", nil, 120],

		["SeqPlayRange", "seqStartStep", "seqEndStep", nil, 64, "Play steps",
			{this.updateSlot; this.resetSequencer; this.updatePatternTime; system.flagGuiIfModDisplay(this);}],
		["TXRangeSlider", "Vel range", ControlSpec(0, 100), "velMin", "velMax", {this.updateSlot;}],
		["TXNumberPlusMinus", "Pattern slot", ControlSpec(0, 99, step: 1), "slotNo",
			{this.setSlotData(arrSlots.at(this.getSynthArgSpec("slotNo")));
				this.updateCurrentChainStep;
				system.showViewIfModDisplay(this);
			},
			[-10,-1,1,10],
			nil,
			nil,
			false,  // don't allow scrolling
			TXColour.yellow;
		],
		["valueActionCheckBox", "Set Group Step on-off : value + group",
			[ControlSpec(0, 1, step: 1), ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrOnOffSteps");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrOnOffSteps");}],
		["valueActionNumber", "Set Group Note: value, group", [ControlSpec(0, 127, step: 1),
			ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrNotes");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrNotes");}],
		["valueActionNumber", "Set Group Rand octave: value, group", [ControlSpec(-4, 4, step: 1),
			 ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrRandOctaves");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrRandOctaves");}],
		["valueActionNumber", "Set Group Velocity: value, group", [ControlSpec(0, 100),
			ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrVelocities");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrVelocities");}],
		["valueActionNumber", "Set Group Rand vel: value, group", [ControlSpec(-99, 100),
			ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrRandVelocities");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrRandVelocities");}],
		["valueActionNumber", "Set Group Step length X: value, group", [ControlSpec(0.1, 100),
			ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrStepLengthsX");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrStepLengthsX");}],
		["valueActionNumber", "Set Group Step length Y: value, group", [ControlSpec(0.1, 100),
			ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrStepLengthsY");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrStepLengthsY");}],
		["valueActionNumber", "Set Group Note length X: value, group", [ControlSpec(0.1, 100),
			ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrNoteLengthsX");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrNoteLengthsX");}],
		["valueActionNumber", "Set Group Note length Y: value, group", [ControlSpec(0.1, 100),
			ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrNoteLengthsY");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrNoteLengthsY");}],
		["valueActionNumber", "Set Group Probability: value, group", [ControlSpec(0, 100),
			ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrProbabilities");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrProbabilities");}],
		["valueActionNumber", "Set Group Delay: value, group", [ControlSpec(0, 100),
			ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrDelays");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrDelays");}],
		["valueActionNumber", "Set Group Rand delay: value, group", [ControlSpec(-99, 100, default: 0),
			ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrRandDelays");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrRandDelays");}],
		["valueActionNumber", "Set Group Control 1: value, group", [ControlSpec(0, 100),
			ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrControl1");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrControl1");}],
		["valueActionNumber", "Set Group Control 2: value, group", [ControlSpec(0, 100),
			ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrControl2");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrControl2");}],
		["valueActionNumber", "Set Group Control 3: value, group", [ControlSpec(0, 100),
			ControlSpec(1, 8, step: 1, default: 1)],
			{arg argGroup; this.getSynthArgValByGroup(argGroup, "arrControl3");},
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrControl3");}],

	// commandAction
		// arguments- index1 is action name, index2 is action function,
		//   index3 is array of controlspec functions
		["commandAction", "Step on-off: value + group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrOnOffSteps");},
			[ControlSpec(0, 1, step: 1), ControlSpec(1, 8, step: 1, default: 1)],],
		["commandAction", "Set Group Note: value, group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrNotes");},
			[ControlSpec(0, 127, step: 1), ControlSpec(1, 8, step: 1, default: 1)]],
		["commandAction", "Set Group Rand octave: value, group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrRandOctaves");},
			[ControlSpec(-4, 4, step: 1), ControlSpec(1, 8, step: 1, default: 1)]],
		["commandAction", "Set Group Velocity: value, group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrVelocities");},
			[ControlSpec(0, 100), ControlSpec(1, 8, step: 1, default: 1)]],
		["commandAction", "Set Group Rand vel: value, group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrRandVelocities");},
			[ControlSpec(-99, 100), ControlSpec(1, 8, step: 1, default: 1)]],
		["commandAction", "Set Group Step length X: value, group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrStepLengthsX");},
			[ControlSpec(0.1, 100), ControlSpec(1, 8, step: 1, default: 1)]],
		["commandAction", "Set Group Step length Y: value, group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrStepLengthsY");},
			[ControlSpec(0.1, 100), ControlSpec(1, 8, step: 1, default: 1)]],
		["commandAction", "Set Group Note length X: value, group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrNoteLengthsX");},
			[ControlSpec(0.1, 100), ControlSpec(1, 8, step: 1, default: 1)]],
		["commandAction", "Set Group Note length Y: value, group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrNoteLengthsY");},
			[ControlSpec(0.1, 100), ControlSpec(1, 8, step: 1, default: 1)]],
		["commandAction", "Set Group Probability: value, group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrProbabilities");},
			[ControlSpec(0, 100), ControlSpec(1, 8, step: 1, default: 1)]],
		["commandAction", "Set Group Delay: value, group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrDelays");},
			[ControlSpec(0, 100), ControlSpec(1, 8, step: 1, default: 1)]],
		["commandAction", "Set Group Rand delay: value, group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrRandDelays");},
			[ControlSpec(-99, 100), ControlSpec(1, 8, step: 1, default: 1)]],
		["commandAction", "Set Group Control 1: value, group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrControl1");},
			[ControlSpec(0, 100), ControlSpec(1, 8, step: 1, default: 1)]],
		["commandAction", "Set Group Control 2: value, group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrControl2");},
			[ControlSpec(0, 100), ControlSpec(1, 8, step: 1, default: 1)]],
		["commandAction", "Set Group Control 3: value, group",
			{arg argVal, argGroup; this.setSynthArgValByGroup(argVal, argGroup, "arrControl3");},
			[ControlSpec(0, 100), ControlSpec(1, 8, step: 1, default: 1)]],
		["TXPopupAction", "MIDI Port", holdPortNames, "midiPort",
			{ arg view; this.initMidiPort(view.value); }],
		["EZSlider", "MIDI Channel", ControlSpec(1, 16, step: 1), "midiChannel"],
		["TXCheckBox", "Output MIDI notes for each step", "midiNoteOn"],
		["TXCheckBox", "Output Control 1 values for each step", "midiControl1On"],
		["EZSlider", "1 - Controller", ControlSpec(0, 127, step: 1), "midiControlNo1"],
		["TXCheckBox", "Active", "midiControl2On"],
		["EZSlider", "2 - Controller", ControlSpec(0, 127, step: 1), "midiControlNo2"],
		["TXCheckBox", "Active", "midiControl3On"],
		["EZSlider", "3 - Controller", ControlSpec(0, 127, step: 1), "midiControlNo3"],
		["TXStaticText", "Status", {this.runningStatus},
			{arg view; runningStatusView = view.textView}, 150],
	]);
	this.buildGuiSpecArray;
	//	use base class initialise
	this.baseInit(this, argInstName);
	this.oscControlActivate;
	//	load the synthdef and create the synth
	this.loadAndMakeSynth;
}

////////////////////////////////////////////////////////////////////////////

buildGuiSpecArray {
	var holdValidOptions;
	holdValidOptions = ["showOutputs", "showBPMOptions", "showPatterns", "showSteps",
		"showGroups", "showProcesses"];
	// reset variables
	this. resetScrollViewArrays;
	// ensure validity of displayOption - for older saved systems.
	if (holdValidOptions.indexOfEqual(this.getSynthArgSpec("displayOption")).isNil, {
		this.setSynthArgSpec("displayOption", "showOutputs");
	});

	// create guiSpecArray
	guiSpecArray = [
		["ActionButton", "Outputs", {this.setSynthArgSpec("displayOption", "showOutputs");
			this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(this.getSynthArgSpec("displayOption") == "showOutputs")],
		["Spacer", 1],
		["ActionButton", "BPM & Options", {this.setSynthArgSpec("displayOption", "showBPMOptions");
			this.buildGuiSpecArray; this.updateTriggerStatus; system.showView;}, 100,
			TXColor.white, this.getButtonColour(this.getSynthArgSpec("displayOption") == "showBPMOptions")],
		["Spacer", 1],
		["ActionButton", "Patterns", {this.setSynthArgSpec("displayOption", "showPatterns");
			this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(this.getSynthArgSpec("displayOption") == "showPatterns")],
		["Spacer", 1],
		["ActionButton", "Steps", {this.setSynthArgSpec("displayOption", "showSteps");
			this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(this.getSynthArgSpec("displayOption") == "showSteps")],
		["Spacer", 1],
		["ActionButton", "Groups", {this.setSynthArgSpec("displayOption", "showGroups");
			this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(this.getSynthArgSpec("displayOption") == "showGroups")],
		["Spacer", 1],
		["ActionButton", "Processes", {this.setSynthArgSpec("displayOption", "showProcesses");
			this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(this.getSynthArgSpec("displayOption") == "showProcesses")],
		["NextLine"],
	];

	////////////////////////////////////////////////////////////////////////////////
	// showOutputs
	if (this.getSynthArgSpec("displayOption") == "showOutputs", {
		holdPortNames = ["Unconnected - select MIDI Port"]
			++ MIDIClient.destinations.collect({arg item, i;
				// remove any brackets from string
				(item.device.asString + item.name.asString).replace("(", "").replace(")", "")
		});
		guiSpecArray = guiSpecArray ++[
//			["SeqSelectFirstDisplayStep", 16, "displayFirstStep", 64], // now scrolling used
			["SpacerLine", 2],
			["TextBarLeft", "Modules to be triggered directly by the sequencer:"],
			["SeqSelect3GroupModules", "noteOutModule1", "noteOutModule2", "noteOutModule3",
				"noteOutModuleID1", "noteOutModuleID2", "noteOutModuleID3"],
			["SpacerLine", 2],
			["SeqSelect3GroupModules", "noteOutModule4", "noteOutModule5", "noteOutModule6",
				"noteOutModuleID4", "noteOutModuleID5", "noteOutModuleID6"],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["TXPopupAction", "MIDI Port", holdPortNames, "midiPort",
				{ arg view; this.initMidiPort(view.value); }, 300],
			["SpacerLine", 4],
			["EZSlider", "MIDI Channel", ControlSpec(1, 16, step: 1), "midiChannel"],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["TXCheckBox", "Output MIDI notes for each step", "midiNoteOn", nil, 300],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["TXCheckBox", "Output MIDI control values for Control 1 at each step", "midiControl1On", nil, 400],
			["NextLine"],
			["EZSlider", "Control 1 MIDI", ControlSpec(0, 127, step: 1), "midiControlNo1"],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["TXCheckBox", "Output MIDI control values for Control 2 at each step", "midiControl2On", nil, 400],
			["NextLine"],
			["EZSlider", "Control 2 MIDI", ControlSpec(0, 127, step: 1), "midiControlNo2"],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["TXCheckBox", "Output MIDI control values for Control 3 at each step", "midiControl3On", nil, 400],
			["NextLine"],
			["EZSlider", "Control 3 MIDI", ControlSpec(0, 127, step: 1), "midiControlNo3"],
		];
	});

	if (this.getSynthArgSpec("displayOption") == "showBPMOptions", {
		//  show BPM
		guiSpecArray = guiSpecArray ++[
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "BPM", ControlSpec(1, 900), "seqBPM", "seqBPMMin", "seqBPMMax",
				{arg view; this.setTempo; this.updatePatternTime;}, arrBPMRangePresets ],
			["SpacerLine", 6],
			["Spacer", 80],
			["TapTempoButton", {arg argTempo; this.useTapTempo(argTempo);}],
			["Spacer", 10],
			["TXCheckBox", "Auto copy tap tempo to BPM ", "autoTapTempo", nil, 200],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
		];
		//  show Play Mode popup
		guiSpecArray = guiSpecArray ++[
			["TXPopupActionPlusMinus", "Play Mode",
				["Normal",
					"Random Trigger - steps are randomly triggered based on BPM.  Step lengths are ignored.",
					"Manual Trigger - steps are manually triggered.  BPM & Step lengths are ignored."
				], "playMode",
				{triggerModeRunning = false; this.stopSequencer; this.prepareToStartSeq;},
				700
			],
			["SpacerLine", 6],
		];
		//  Manual controls
		guiSpecArray = guiSpecArray ++[
			["Spacer", 84],
			["TextBar", "Manual Controls", 150],
			["SpacerLine", 2],
			["Spacer", 84],
			["TXStaticText", "Trigger status", {this.triggerStatus}, {arg view; triggerStatusView = view.textView}, 600],
			["SpacerLine", 2],
			["Spacer", 84],
			["ActionButton", "Rewind Sequencer",
				{if (triggerModeRunning == true, {this.stopSequencer; this.prepareToStartSeq;});}, 180],
			["Spacer", 10],
			["ActionButton", "Trigger Next Step", {this.triggerNextStep}, 180],
			["Spacer", 10],
			["ActionButton", "Skip Next Step", {this.skipNextStep}, 180],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
		];
		// other controls
		guiSpecArray = guiSpecArray ++[
			["TextBar", "Mute", 80, nil, nil, nil, \right],
			["TXCheckBox", "Mute - sequencer plays but all steps are turned off", "muteSeq", nil, 378],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["TextBar", "Randomise", 80, nil, nil, nil, \right],
			["TXCheckBox", "Randomise step order for selected parameters: ", "randomiseStepOrder",
				{this.buildGuiSpecArray; this.rebuildStepOrderRandArr; system.showView;}, 378],
			["SpacerLine", 6],
		];
		if (this.getSynthArgSpec("randomiseStepOrder") == 1, {
			guiSpecArray = guiSpecArray ++[
				["TextBar", "Select which parameters will be affected:", 250],
				["ActionButton", "All Off", {
					arrRandOrderNames.do({ arg item, i;
						this.setSynthArgSpec(item, 0);
					});
					system.showViewIfModDisplay(this);
					},
					60
				],
				["ActionButton", "All On", {
					arrRandOrderNames.do({ arg item, i;
						this.setSynthArgSpec(item, 1);
					});
					system.showViewIfModDisplay(this);
					},
					60
				],
				["NextLine"],
				["TXCheckBox", "On/Off Steps", "randOrderOnOffSteps", nil, 120],
		 		["TXCheckBox", "Notes", "randOrderNotes", nil, 120],
				["TXCheckBox", "Rand Octaves", "randOrderRandOctaves", nil, 120],
				["TXCheckBox", "Velocities", "randOrderVelocities", nil, 120],
				["TXCheckBox", "Rand Velocities", "randOrderRandVelocities", nil, 120],
				["TXCheckBox", "Step Lengths X", "randOrderStepLengthsX", nil, 120],
				["TXCheckBox", "Step Lengths Y", "randOrderStepLengthsY", nil, 120],
				["TXCheckBox", "Note Lengths X", "randOrderNoteLengthsX", nil, 120],
				["TXCheckBox", "Note Lengths Y", "randOrderNoteLengthsY", nil, 120],
				["TXCheckBox", "Probabilities", "randOrderProbabilities", nil, 120],
				["TXCheckBox", "Delays", "randOrderDelays", nil, 120],
				["TXCheckBox", "Rand delays", "randOrderRandDelays", nil, 120],
				["TXCheckBox", "Control 1", "randOrderControl1s", nil, 120],
				["TXCheckBox", "Control 2", "randOrderControl2s", nil, 120],
				["TXCheckBox", "Control 3", "randOrderControl3s", nil, 120],
			];
		});
	});


	////////////////////////////////////////////////////////////////////////////////

	// if playing a Pattern Chain replace some screens with NOTE
	if ((seqRunning == true) or: (triggerModeRunning == true)
		and: (this.getSynthArgSpec("nextPatternInd") > 1)
		and: (   this.getSynthArgSpec("displayOption") == "showPatterns"
			or: (this.getSynthArgSpec("displayOption") == "showSteps")
			or: (this.getSynthArgSpec("displayOption") == "showGroups")
			or: (this.getSynthArgSpec("displayOption") == "showProcesses")
		),
	{
		guiSpecArray = guiSpecArray ++[
			["SpacerLine", 8],
			["TextBar", "NOTE: Sequencer steps cannot be edited while Sequencer is playing a Pattern Chain.",
				780, 30, nil, TXColour.paleYellow2],
			["TextBar", "You can still edit controls on the 'Outputs' and 'BPM & Options' screens. ",
				780, 30, nil, TXColour.paleYellow2],
			["TextBar", "To edit a Pattern while playing it, stop the Sequencer & set 'Next pattern' (on the 'Patterns' screen) to 'None' or 'Repeat current pattern'.",
				780, 30, nil, TXColour.paleYellow2],
		];
	},{
	// showSteps
	if (this.getSynthArgSpec("displayOption") == "showSteps", {
		arrParmNames = ["notes", "velocity", "random velocity", "step, note length", "probability",
			"delay", "random delay", "random octave", "control 1", "control 2", "control 3"
	//		"chords, scales" // add in future - see below
			];
		if (parmIndex == 0, {holdKeybOctaves = 3;}, {holdKeybOctaves = 2;});
		guiSpecArray = guiSpecArray ++[
//			["SeqSelectFirstDisplayStep", 16, "displayFirstStep", 64],
			["SeqScrollStep", 64, {arg view; this.addScrollViewH(view);},
				/*{arg view; this.updateScrollOriginX(view, view.visibleOrigin)}*/],
			["TXMultiSwitch", "Step on/off", "arrOnOffSteps", 64, {this.updateSlot;}, "displayFirstStep",
				{arg view; this.addScrollViewH(view);}],
			["MidiNoteText", "seqNoteBase", "arrNotes", 64, "displayFirstStep",
				{"Pattern" + this.getSynthArgSpec("slotNo");}, "seqStartStep", "seqEndStep",
					{arg view; this.addScrollViewH(view);}],
			["TXMidiNoteKeybGrid", "seqNoteBase", "arrNotes", 64, "displayFirstStep", {this.updateSlot;},
				{this.displayOctave.asInteger}, {arg argOctave; this.displayOctave_(argOctave)},
				holdKeybOctaves, arrParmNames, {this.parmIndex.asInteger},
				{arg argIndex; this.parmIndex_(argIndex); this.buildGuiSpecArray; system.showView;},
				{arg arrViews; this.addScrollViewV(arrViews[0]); this.addScrollViewVH(arrViews[1]);},
				{arg view; this.updateScrollOrigin(view, view.visibleOrigin)}
			 ],
		];
		guiSpecArray = guiSpecArray ++[
			[
				["NextLine"],
				["TextBar", "MIDI Learn", 80, nil, nil, nil, \right],
				["TXCheckBox", "MIDI learn notes/velocities ",
					"midiLearn", {arg view; this.midiLearn(view.value);}, 250],
				["EZNumber", "Next step", ControlSpec(1, 64, step: 1), "midiLearnStep"],
				["ActionButton", "Set Next Step: 1", {this.setSynthArgSpec("midiLearnStep", 1); system.showView;}, 100],
			],
			[
				["TXMultiSliderNo", "Velocity", ControlSpec(0, 100), "arrVelocities", 64,
					{this.updateSlot;}, "showVelocityBars", "displayFirstStep", nil, nil,
					{arg view; this.addScrollViewH(view);}],
				["TXRangeSlider", "Vel o/p range", ControlSpec(0, 100), "velMin", "velMax",
					{this.updateSlot;}],
			],
			[
				["TXMultiSliderNo", "Rand vel", ControlSpec(-99, 100), "arrRandVelocities", 64,
					{this.updateSlot;}, "showRandVelBars", "displayFirstStep", nil, nil,
					{arg view; this.addScrollViewH(view);}],
			],
			[
				["TXMultiNumber", "Step length X", ControlSpec(0.1, 100), "arrStepLengthsX", 64,
					{this.updateSlot;}, "displayFirstStep",
					{arg view; this.addScrollViewH(view);}],
				["TXMultiNumber", " (in beats) / Y", ControlSpec(0.1, 100), "arrStepLengthsY", 64,
					{this.updateSlot;}, "displayFirstStep",
					{arg view; this.addScrollViewH(view);}],
				["SpacerLine", 1],
				["TXMultiNumber", "Note length X", ControlSpec(0.1, 100), "arrNoteLengthsX", 64,
					{this.updateSlot;}, "displayFirstStep",
					{arg view; this.addScrollViewH(view);}],
				["TXMultiNumber", " (in beats) / Y", ControlSpec(0.1, 100), "arrNoteLengthsY", 64,
					{this.updateSlot;}, "displayFirstStep",
					{arg view; this.addScrollViewH(view);}],
				["SpacerLine", 1],
				["TXPopupAction", "Presets",
					arrStepNoteLenPresets.collect({arg item, i; item.at(0);}),
					"stepNoteLenPresetInd",
					{this.runStepNoteLenPreset; system.showView;}, 400],
			],
			[
				["TXMultiSliderNo", "Probability", ControlSpec(0, 100), "arrProbabilities", 64,
					 {this.updateSlot;}, "showProbabilityBars", "displayFirstStep", nil, nil,
					 {arg view; this.addScrollViewH(view);}],
			],
			[
				["TXMultiSliderNo", "Delays", ControlSpec(0, 100), "arrDelays", 64,
					 {this.updateSlot;}, "showDelayBars", "displayFirstStep", nil, nil,
					 {arg view; this.addScrollViewH(view);}],
				["TextBarLeft", "Note: maximum delay is 50% of step length"],
			],
			[
				["TXMultiSliderNo", "Rand delay", ControlSpec(-99, 100), "arrRandDelays", 64,
					{this.updateSlot;}, "showRandDelayBars", "displayFirstStep", nil, nil,
					 {arg view; this.addScrollViewH(view);}],
				["TextBarLeft", "Note: maximum delay is 50% of step length"],
			],
			[
				["TXMultiSliderNo", "Rand octave", ControlSpec(-4, 4, step: 1), "arrRandOctaves",
					64,{this.updateSlot;}, "showRandOctBars", "displayFirstStep", 1, nil,
					{arg view; this.addScrollViewH(view);}],
			],
			[
				["TXMultiSliderNo", "Control 1", ControlSpec(0, 100), "arrControl1", 64,
					{this.updateSlot;}, "showControl1Bars", "displayFirstStep", nil, nil,
					{arg view; this.addScrollViewH(view);}],
			],
			[
				["TXMultiSliderNo", "Control 2", ControlSpec(0, 100), "arrControl2", 64,
					{this.updateSlot;}, "showControl2Bars", "displayFirstStep", nil, nil,
					{arg view; this.addScrollViewH(view);}],
			],
			[
				["TXMultiSliderNo", "Control 3", ControlSpec(0, 100), "arrControl3", 64,
					{this.updateSlot;}, "showControl3Bars", "displayFirstStep", nil, nil,
					{arg view; this.addScrollViewH(view);}],
			],
// add in future - so that scales can be shown on midi keyboard
//			[
//				["TXPopupAction", "Chord/ scale", TXScale.arrScaleNames, "xxxxxxx", nil, 400],
//				["NextLine"],
//				["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
//					"yyyyyyyyy", nil, 140],
//			],
		].at(parmIndex);
	});

	// showGroups
	if (this.getSynthArgSpec("displayOption") == "showGroups", {
		guiSpecArray = guiSpecArray ++[
			["SeqScrollStep", 64, {arg view; this.addScrollViewH(view);},
				{arg view; this.updateScrollOriginX(view, view.visibleOrigin)}],
			["TXMultiSwitch", "Step on/off", "arrOnOffSteps", 64, {this.updateSlot;}, "displayFirstStep",
				{arg view; this.addScrollViewH(view);}],
			["MidiNoteText", "seqNoteBase", "arrNotes", 64, "displayFirstStep",
				{"Pattern" + this.getSynthArgSpec("slotNo");}, "seqStartStep", "seqEndStep",
					{arg view; this.addScrollViewH(view);}],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["TXMultiSwitch", "Group 1", "arrGroup1s", 64, {this.updateSlot;}, "displayFirstStep",
				{arg view; this.addScrollViewH(view);}],
			["DividingLine"],
			["TXMultiSwitch", "Group 2", "arrGroup2s", 64, {this.updateSlot;}, "displayFirstStep",
				{arg view; this.addScrollViewH(view);}],
			["DividingLine"],
			["TXMultiSwitch", "Group 3", "arrGroup3s", 64, {this.updateSlot;}, "displayFirstStep",
				{arg view; this.addScrollViewH(view);}],
			["DividingLine"],
			["TXMultiSwitch", "Group 4", "arrGroup4s", 64, {this.updateSlot;}, "displayFirstStep",
				{arg view; this.addScrollViewH(view);}],
			["DividingLine"],
			["TXMultiSwitch", "Group 5", "arrGroup5s", 64, {this.updateSlot;}, "displayFirstStep",
				{arg view; this.addScrollViewH(view);}],
			["DividingLine"],
			["TXMultiSwitch", "Group 6", "arrGroup6s", 64, {this.updateSlot;}, "displayFirstStep",
				{arg view; this.addScrollViewH(view);}],
			["DividingLine"],
			["TXMultiSwitch", "Group 7", "arrGroup7s", 64, {this.updateSlot;}, "displayFirstStep",
				{arg view; this.addScrollViewH(view);}],
			["DividingLine"],
			["TXMultiSwitch", "Group 8", "arrGroup8s", 64, {this.updateSlot;}, "displayFirstStep",
				{arg view; this.addScrollViewH(view);}],
			["DividingLine"],
		];
	});
	if (this.getSynthArgSpec("displayOption") == "showPatterns", {
		guiSpecArray = guiSpecArray ++[
			["SeqScrollStep", 64, {arg view; this.addScrollViewH(view);},
				{arg view; this.updateScrollOriginX(view, view.visibleOrigin)}],
			["TXMultiSwitch", "Step on/off", "arrOnOffSteps", 64, {this.updateSlot;}, "displayFirstStep",
				{arg view; this.addScrollViewH(view);}],
			["MidiNoteText", "seqNoteBase", "arrNotes", 64, "displayFirstStep",
				{"Pattern" + this.getSynthArgSpec("slotNo");}, "seqStartStep", "seqEndStep",
					{arg view; this.addScrollViewH(view);}],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["SeqPlayRange", "seqStartStep", "seqEndStep", nil, 64, "Play steps",
				{this.updateSlot; this.resetSequencer; this.updatePatternTime; system.flagGuiIfModDisplay(this);},
				nil, arrSeqRangePresets],
			["SpacerLine", 2],
			["TXStaticText", "Pattern time", {this.getPatternTimeString},
				{arg view; patternView = view.textView}],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["TXTextBox", "Pattern desc.", "description", nil, 400],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["TXNumberPlusMinus", "Pattern slot", ControlSpec(0, 99, step: 1), "slotNo",
				{this.setSlotData(arrSlots.at(this.getSynthArgSpec("slotNo")));
					this.updateCurrentChainStep;
					system.showViewIfModDisplay(this);
				},
				[-10,-1,1,10],
				nil,
				nil,
				false,  // don't allow scrolling
				TXColor.paleYellow2;
			],
			["ActionButton", "Copy pattern", {slotClipboard = this.getSlotData.deepCopy;}, 80],
			["ActionButton", "Paste pattern", {if (slotClipboard.notNil,
				{this.setSlotData(slotClipboard.deepCopy)}); this.updateSlot; system.showView;}, 80],
			["ActionButtonDark", "Reset all values", {this.resetPattern; this.updateSlot;}, 100],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["TXPopupAction", "Next pattern",
				["None", "Repeat current pattern", "Use pattern chain - in chain step order",
					"Use pattern chain - in random order"],
				"nextPatternInd",
				{this.buildGuiSpecArray; this.prepareToStartSeq; system.showView;},
				400
			],
		];
		// if relevant add chain gui specs
		if (this.getSynthArgSpec("nextPatternInd") > 1, {
			guiSpecArray = guiSpecArray ++ [
				["SpacerLine", 1],
				["SeqPlayRange", "chainStartStep", "chainEndStep", "chainLoop", 64, "Chain steps",
					{this.setSynthArgSpec("displayFirstChainStep", 0);
						this.stopSequencer;
						this.prepareToStartSeq;
						this.constrainChainStep;
						system.showView; },
					false,
					nil, nil, nil,
					{this.stopSequencer; this.prepareToStartSeq;}
				],
				["NextLine"],
				["SpacerLine", 1],
				["SeqSelectChainStep", 24,"displayFirstChainStep", "chainCurrentStep", "chainStartStep",
					"chainEndStep", "arrChainSlots", {this.updateSlotNo; system.showView;}
				],
				["SpacerLine", 1],
				["TextBarLeft",
					" Pattern Chain: click on a Chain step button to display it in the Pattern slot", 400],
			];
		});
	});
	if (this.getSynthArgSpec("displayOption") == "showProcesses", {
		guiSpecArray = guiSpecArray ++[
			["SeqScrollStep", 64, {arg view; this.addScrollViewH(view);},
				{arg view; this.updateScrollOriginX(view, view.visibleOrigin)}],
			["TXMultiSwitch", "Step on/off", "arrOnOffSteps", 64, {this.updateSlot;}, "displayFirstStep",
				{arg view; this.addScrollViewH(view);}],
			["MidiNoteText", "seqNoteBase", "arrNotes", 64, "displayFirstStep",
				{"Pattern" + this.getSynthArgSpec("slotNo");}, "seqStartStep", "seqEndStep",
					{arg view; this.addScrollViewH(view);}],
			["SpacerLine", 2],
			["DividingLine"],
			["SpacerLine", 2],
			["TXPopupAction", "Process", arrProcessSpecs.collect({arg item, i; item.at(0);}), "processTypeInd",
				{this.buildGuiSpecArray; system.showView;}, 350],
			["ActionButton", "Run Process",
				{arrProcessSpecs.at(this.getSynthArgSpec("processTypeInd")).at(1).value;
				this.updateSlot; system.showView;}, 90, nil, TXColour.sysGuiCol2],
			["ActionButton", "Show All Processes", {this.setSynthArgSpec("processTypeInd", 0);
				this.buildGuiSpecArray; system.showView;}, 130, nil, TXColour.sysGuiCol1],
			["SpacerLine", 1],
			["NextLine"],
		];
		// add processing gui specs
		guiSpecArray = guiSpecArray ++ arrProcessSpecs.at(this.getSynthArgSpec("processTypeInd")).at(2);
	});
	}); // end of: // if playing a Pattern Chain replace some screens with NOTE
}

////////////////////////////////////

openGui{ arg argParent; 			 // override base class
	this.resetScrollViewArrays;
	//	use base class initialise
	this.baseOpenGui(this, argParent);
	//this.updateScrollOrigin(nil, holdVisibleOrigin);
}

//////////////////////////////////// scroll view syncing

resetScrollViewArrays {
	arrScrollViewsH = [];
	arrScrollViewsV = [];
	arrScrollViewsVH = [];
}

addScrollViewH { arg view;
	arrScrollViewsH = arrScrollViewsH.asArray.add(view);
}

addScrollViewV { arg view;
	arrScrollViewsV = arrScrollViewsV.asArray.add(view);
}

addScrollViewVH { arg view;
	arrScrollViewsVH = arrScrollViewsVH.asArray.add(view);
}

updateScrollOrigin {arg sourceView, argOrigin;
		holdVisibleOrigin = argOrigin;
		arrScrollViewsH.do({arg item, i; if (item != sourceView, {item.visibleOrigin = (argOrigin.x @ 0);}) });
		arrScrollViewsV.do({arg item, i; if (item != sourceView, {item.visibleOrigin = (0 @ argOrigin.y);}) });
		arrScrollViewsVH.do({arg item, i; if (item != sourceView, {item.visibleOrigin = (argOrigin.x @ argOrigin.y);}) });
}

updateScrollOriginX {arg sourceView, argOrigin;
	// this version only uses X value passed
	holdVisibleOrigin.x = argOrigin.x;
	this.updateScrollOrigin(sourceView, holdVisibleOrigin);
}

////////////////////////////////////

runStepNoteLenPreset {
	var presetIndex, arrNewVals;
	presetIndex = this.getSynthArgSpec("stepNoteLenPresetInd") ? 0;
	if (presetIndex > 0, {
		arrNewVals = arrStepNoteLenPresets.at(presetIndex).at(1);
		this.setSynthArgSpec("arrStepLengthsX", arrNewVals.at(0).dup(64));
		this.setSynthArgSpec("arrStepLengthsY", arrNewVals.at(1).dup(64));
		this.setSynthArgSpec("arrNoteLengthsX", arrNewVals.at(0).dup(64));
		this.setSynthArgSpec("arrNoteLengthsY", arrNewVals.at(1).dup(64));
		this.updateSlot;
		this.setSynthArgSpec("stepNoteLenPresetInd", 0);
	});
}

getButtonColour { arg colour2Boolean;
	if (colour2Boolean == true, {
		^TXColor.sysGuiCol4;
	},{
		^TXColor.sysGuiCol1;
	});
}

getCurrentBpm {
	^  (this.getSynthArgSpec("seqBPM")
		* (this.getSynthArgSpec("seqBPMMax") - this.getSynthArgSpec("seqBPMMin"))
	+ this.getSynthArgSpec("seqBPMMin"));
}

getPatternTimeString {
	var  startStep, endStep, stepLengthBeats, totalBeats, totalSecs, outString;
	totalBeats = 0;
	startStep = this.getSynthArgSpec("seqStartStep");
	endStep = this.getSynthArgSpec("seqEndStep");
	(endStep - startStep + 1).do({ arg item, i;
		stepLengthBeats = (this.getSynthArgSpec("arrStepLengthsX").at(i + startStep - 1))
			/ (this.getSynthArgSpec("arrStepLengthsY").at(i + startStep - 1));
		totalBeats = totalBeats + stepLengthBeats;
	});
	totalSecs = totalBeats * 60 / this.getCurrentBpm;
	outString = " Total length of   " ++ (endStep - startStep + 1).asString
		++  " play steps   =   " ++ totalBeats.round(0.001).asString
		++ " beats   =   " ++ totalSecs.round(0.001).asString
		++ " seconds   @   " ++ this.getCurrentBpm.round(0.001).asString ++ " BPM";
	^outString;
}

updatePatternTime {
	{	if (patternView.notNil, {
			if (patternView.notClosed, {patternView.string = this.getPatternTimeString});
		});
	}.defer;
}

////////////////////////////////////

oscControlActivate {
	//	remove any previous OSCresponderNode and add new
	this.oscControlDeactivate;
	oscControlResp = OSCresponderNode(system.server.addr,'/tr',{ arg time,responder,msg;
		if (msg[2] == sendTrigID,{
			holdModSeqBPM = msg[3];
			this.setTempo;
		});
	}).add;
}

oscControlDeactivate {
	//	remove responder
	oscControlResp.remove;
}

////////////////////////////////////

initMidiPort { arg portInd = 0;
	if (portInd == 0, {
		holdMIDIOutPort = nil;
		holdMIDIDeviceName = nil;
		holdMIDIPortName = nil;
	},{
		holdMIDIOutPort = MIDIOut(portInd, MIDIClient.destinations[portInd - 1].uid);
		holdMIDIDeviceName =  MIDIClient.destinations[portInd - 1].device;
		holdMIDIPortName =  MIDIClient.destinations[portInd - 1].name;
		// minimise MIDI out latency
		holdMIDIOutPort.latency = 0;
	});
}

sendMIDIMessage { arg outNote, outVel, gateTime, outControl1, outControl2, outControl3;
	var portInd, channel;
	portInd = this.getSynthArgSpec("midiPort");
	channel = this.getSynthArgSpec("midiChannel") - 1;
	if ( portInd > 0, {
		// "Note On & Off"
		if (this.getSynthArgSpec("midiNoteOn") == 1, {
			SystemClock.schedAbs( 0,{
				holdMIDIOutPort.noteOn(channel, outNote, outVel);
			});
			SystemClock.sched( gateTime,{
				holdMIDIOutPort.noteOff(channel, outNote, 0);
				nil;
			});
		});
		// "Controller 1"
		if (this.getSynthArgSpec("midiControl1On") == 1, {
			holdMIDIOutPort.control(channel, this.getSynthArgSpec("midiControlNo1"), outControl1 );
		});
		// "Controller 2"
		if (this.getSynthArgSpec("midiControl2On") == 1, {
			holdMIDIOutPort.control(channel, this.getSynthArgSpec("midiControlNo2"), outControl2);
		});
		// "Controller 3"
		if (this.getSynthArgSpec("midiControl3On") == 1, {
			holdMIDIOutPort.control(channel, this.getSynthArgSpec("midiControlNo3"), outControl3);
		});
	});
}

////////////////////////////////////

startSequencer {
	// if trigger mode then run startTriggerMode
	if (this.getSynthArgSpec("playMode") == 2, {
		this.startTriggerMode;
	},{
		// stop any old sequence running and reset
		this.stopSequencer;
		triggerModeRunning = false;
		// if not being deleted then start
		if (	deletedStatus != true, {
			this.prepareToStartSeq;
			// start tempo clock and play sequence
			seqClock = TempoClock.new(
				this.getCurrentBpm / 60;
			);
			seqRunning = true;
			this.setSynthValue("seqOn", 1);
			seqClock.schedAbs(seqClock.elapsedBeats,{
				this.playNextStep;
			});
		});
		this.buildGuiSpecArray;
	});
	// set variable
	this.updateRunningStatus(" RUNNING");
}

prepareToStartSeq {
		// reset variables
		seqEnded = false;
		this.rebuildStepOrderRandArr;
		// randomise first step if required
		if ((this.getSynthArgSpec("nextPatternInd") == 3), {
			holdChainCurrentStep = (this.getSynthArgSpec(
					"chainEndStep") - this.getSynthArgSpec("chainStartStep") + 1
				).asInteger.rand + 1;
			this.setSynthArgSpec("chainCurrentStep", holdChainCurrentStep);
			// get next slot no
			holdSlotNo = this.getSynthArgSpec("arrChainSlots").at(holdChainCurrentStep - 1);
			// load new slot
			this.setSynthArgSpec("slotNo", holdSlotNo);
			this.setSlotData(arrSlots.at(holdSlotNo));
			seqCurrentStep = this.getSynthArgSpec("seqStartStep") - 1;
			system.showViewIfModDisplay(this);
		});
		this.updateTriggerStatus;
}

startTriggerMode {
	this.stopSequencer;
	this.prepareToStartSeq;
	triggerModeRunning = true;
	this.updateTriggerStatus;
	this.buildGuiSpecArray;
	system.showViewIfModDisplay(this);
}

updateTriggerStatus { arg argStatus;
	if (argStatus.notNil, {
		triggerStatus = argStatus;
	},{
		if (triggerModeRunning == true, {
			triggerStatus = (" Ready to trigger next Step: "
				++ (seqCurrentStep+1).asString
				++ " in Pattern " ++ this.getSynthArgSpec("slotNo").asString);
		},{
			triggerStatus = "Cannot be triggered until Play Mode is set to Manual Trigger and Sequencer is started ";
		});
	});
	// defer
	{
		if (triggerStatusView.notNil, {
			if (triggerStatusView.notClosed, {
				triggerStatusView.string = triggerStatus;
			});
		});
	}.defer(0.2);
}

triggerNextStep{
	if (triggerModeRunning == true, {
		if (seqEnded == false, {
			this.playNextStep;
		});
	});
}

skipNextStep{
	if (triggerModeRunning == true, {
		if (seqEnded == false, {
			this.playNextStep(1);
		});
	});
}

playNextStep { arg argMuteStep = 0;
	var 	stepNoNormalRandArr, nextStepSize, envSize, outRandOcts, outAdjust, outNote, outVelMin, outVelMax, outVel;
	var	outControl1, outControl2, outControl3, outEnvTime, outDelay, randDelay, stepDelay;
	var stopSeqFlag, seqCurrentStepRand, holdChainStepCounter = 0;
	var stepNoOnOffSteps, stepNoNotes, stepNoRandOctaves, stepNoVelocities, stepNoRandVelocities,
		stepNoStepLengthsX, stepNoStepLengthsY, stepNoNoteLengthsX, stepNoNoteLengthsY, stepNoProbabilities,
		stepNoDelays, stepNoRandDelays, stepNoControl1s, stepNoControl2s, stepNoControl3s;

	// allow for random step nos
	if (this.getSynthArgSpec("randomiseStepOrder") == 1, {
		seqCurrentStepRand = this.getSynthArgSpec("stepOrderRandArr").at(seqCurrentStep);
		stepNoNormalRandArr = [seqCurrentStep, seqCurrentStepRand];
	},{
		stepNoNormalRandArr = [seqCurrentStep, seqCurrentStep];
	});
	// get parameter values
	stepNoOnOffSteps = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderOnOffSteps"));
	stepNoNotes = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderNotes"));
	stepNoRandOctaves = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderRandOctaves"));
	stepNoVelocities = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderVelocities"));
	stepNoRandVelocities = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderRandVelocities"));
	stepNoStepLengthsX = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderStepLengthsX"));
	stepNoStepLengthsY = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderStepLengthsY"));
	stepNoNoteLengthsX = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderNoteLengthsX"));
	stepNoNoteLengthsY = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderNoteLengthsY"));
	stepNoProbabilities = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderProbabilities"));
	stepNoDelays = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderDelays"));
	stepNoRandDelays = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderRandDelays"));
	stepNoControl1s = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderControl1s"));
	stepNoControl2s = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderControl2s"));
	stepNoControl3s = stepNoNormalRandArr.at(this.getSynthArgSpec("randOrderControl3s"));
	outRandOcts = this.getSynthArgSpec("arrRandOctaves").at(stepNoRandOctaves).asInteger;
	if (outRandOcts.isPositive,
		{outRandOcts = outRandOcts + 1; outAdjust = 0;},
		{outRandOcts = outRandOcts - 1; outAdjust = 12;});
	outRandOcts = outRandOcts.rand;
	outNote = this.getSynthArgSpec("seqNoteBase")
		+ this.getSynthArgSpec("arrNotes").at(stepNoNotes)
		+ (outRandOcts * 12) + outAdjust;
	outVel = this.getSynthArgSpec("arrVelocities").at(stepNoVelocities)
		+ this.getSynthArgSpec("arrRandVelocities").at(stepNoRandVelocities).rand;
	outVel = outVel.max(0).min(100);
	outVelMin = this.getSynthArgSpec("velMin");
	outVelMax = this.getSynthArgSpec("velMax");
	outVel = outVelMin + ((outVel/100) * (outVelMax-outVelMin));
	outVel = outVel * 127/ 100;
	if (this.getSynthArgSpec("playMode") == 0, {
		nextStepSize = (this.getSynthArgSpec("arrStepLengthsX").at(stepNoStepLengthsX))
			/ (this.getSynthArgSpec("arrStepLengthsY").at(stepNoStepLengthsY));
		randDelay = this.getSynthArgSpec("arrRandDelays").at(stepNoRandDelays).rand;
		stepDelay = this.getSynthArgSpec("arrDelays").at(stepNoDelays);
		outDelay = (stepDelay + randDelay).max(0).min(100)  * nextStepSize
			* (60/ this.getCurrentBpm) / 200;
	},{
		// if random triggering("playMode"==1), randomise stepsize, and don't use step delay
		nextStepSize = 2.0.rand;
		outDelay = 0;
	});
	envSize = (this.getSynthArgSpec("arrNoteLengthsX").at(stepNoNoteLengthsX))
		/ (this.getSynthArgSpec("arrNoteLengthsY").at(stepNoNoteLengthsY));

	if (seqRunning == true, {
		outEnvTime = envSize * seqClock.beatDur;
	},{
		outEnvTime = envSize * (60/this.getCurrentBpm);
	});

	outControl1 = this.getSynthArgSpec("arrControl1").at(stepNoControl1s);
	outControl2 = this.getSynthArgSpec("arrControl2").at(stepNoControl2s);
	outControl3 = this.getSynthArgSpec("arrControl3").at(stepNoControl3s);

	// make the noise
	if (	// if step is to be played
		this.getSynthArgSpec("arrOnOffSteps").at(stepNoOnOffSteps) == 1
			// and the fates allow it
			and: ((this.getSynthArgSpec("arrProbabilities").at(stepNoProbabilities)/100 - rand(1.0))
				.ceil == 1)
			// and sequencer not muted
			and: (this.getSynthArgSpec("muteSeq") == 0)
			and: (argMuteStep == 0),
	{
		// use bundle to allow for latency
		system.server.makeBundle(seqLatency + outDelay, {
			if (noteOutModule1.notNil,
				{noteOutModule1.createSynthNote(outNote, outVel, outEnvTime)});
			if (noteOutModule2.notNil,
				{noteOutModule2.createSynthNote(outNote, outVel, outEnvTime)});
			if (noteOutModule3.notNil,
				{noteOutModule3.createSynthNote(outNote, outVel, outEnvTime)});
			if (noteOutModule4.notNil,
				{noteOutModule4.createSynthNote(outNote, outVel, outEnvTime)});
			if (noteOutModule5.notNil,
				{noteOutModule5.createSynthNote(outNote, outVel, outEnvTime)});
			if (noteOutModule6.notNil,
				{noteOutModule6.createSynthNote(outNote, outVel, outEnvTime)});
			// write note & velocity as control values to output busses
			outBus.setn([(outNote/127).max(0).min(1), outVel/127, outControl1/100,
				outControl2/100, outControl3/100, 1]);
			// output midi
			this.sendMIDIMessage(outNote, outVel, outEnvTime,
				outControl1 * 127 / 100, outControl2 * 127 / 100, outControl3 * 127 / 100);
		});
		system.server.makeBundle(seqLatency + outDelay + 0.01, {
			// write note & velocity as control values to output busses
			outBus.setAt(5, 0);
		});
	});
	// go to next step
	seqCurrentStep = (seqCurrentStep + 1);
	// update status
	this.updateTriggerStatus;

	// not used now
	//	seqResetCounter = (seqResetCounter+1);

	// if past end, go to start step if looped, use chain if requested, or else stop sequencer
	if (seqCurrentStep > (this.getSynthArgSpec("seqEndStep") - 1), {

		// if nextPatternInd is 0 stop sequencer
		if ((this.getSynthArgSpec("nextPatternInd") == 0), {
			seqEnded = true;
			this.stopSequencer;
			this.updateTriggerStatus(" End of sequence - rewind before triggering");
			system.showViewIfModDisplay(this);
		});

		// rebuild step order
		this.rebuildStepOrderRandArr;

		// if nextPatternInd is 1 loop current pattern
		if ((this.getSynthArgSpec("nextPatternInd") == 1), {
			seqCurrentStep = this.getSynthArgSpec("seqStartStep") - 1;
		});

		// if nextPatternInd is > 1 get next pattern in pattern chain
		if ((this.getSynthArgSpec("nextPatternInd") > 1), {
			stopSeqFlag = false;
			// go to next chain step
			if ((this.getSynthArgSpec("nextPatternInd") == 2), {
				holdChainCurrentStep = this.getSynthArgSpec("chainCurrentStep");
				holdChainCurrentStep = holdChainCurrentStep + 1;
			});
			if ((this.getSynthArgSpec("nextPatternInd") == 3), {
				holdChainCurrentStep = (this.getSynthArgSpec(
						"chainEndStep") - this.getSynthArgSpec("chainStartStep") + 1
					).asInteger.rand + 1;
			});
			this.setSynthArgSpec("chainCurrentStep", holdChainCurrentStep);
			holdChainStepCounter = holdChainStepCounter + 1;
			// if end of chain
			if (this.getSynthArgSpec("chainCurrentStep") > this.getSynthArgSpec("chainEndStep"), {
				if (this.getSynthArgSpec("chainLoop") == 1, {
					this.setSynthArgSpec("chainCurrentStep",
						this.getSynthArgSpec("chainStartStep"));
				}, {
					stopSeqFlag = true;
				});
			});
			if ((this.getSynthArgSpec("nextPatternInd") == 3), {
				if (this.getSynthArgSpec("chainLoop") == 0, {
					if ((holdChainStepCounter + 1) > this.getSynthArgSpec("chainEndStep"), {
						stopSeqFlag = true;
					});
				});
			});
			if (stopSeqFlag == false, {
				// get next slot no
				holdSlotNo = this.getSynthArgSpec("arrChainSlots").at(holdChainCurrentStep - 1);
				// load new slot
				this.setSynthArgSpec("slotNo", holdSlotNo);
				this.setSlotData(arrSlots.at(holdSlotNo));
				seqCurrentStep = this.getSynthArgSpec("seqStartStep") - 1;

				//	gui update
				// don't change screen if seq is running - stops crashes
				if (seqRunning == false, {
					this.updatePatternTime;
					system.showViewIfModDisplay(this);
					system.flagGuiIfModDisplay(this);
				});
				// update status
				this.updateTriggerStatus;
			}, {
				seqEnded = true;
				this.stopSequencer;
				this.updateTriggerStatus(" End of sequence - rewind before triggering");
				system.showViewIfModDisplay(this);
			});
			system.showViewIfModDisplay(this);
		});
	});
// not used now
//		// check for reset
//		if (seqResetCounter > this.getSynthArgSpec("seqResetStep"), {
//			// reset variables
//			this.resetSequencer;
//		});
	^nextStepSize;	//schedule next event in beats
}

syncStartSequencer {
	// if syncStart is 1 then start sequencer
	if (this.getSynthArgSpec("syncStart") == 1, {
		this.startSequencer;
		system.showViewIfModDisplay(this);
	});
}

syncStopSequencer {
	// if syncStop is 1 then stop sequencer
	if (this.getSynthArgSpec("syncStop") == 1, {
		this.stopSequencer;
		this.prepareToStartSeq;
		system.showViewIfModDisplay(this);
	});
}

stopSequencer {
	// stop tempo clock
	if (seqRunning == true, {
		seqClock.stop;
	});
	seqRunning = false;
	this.setSynthValue("seqOn", 0);
	this.resetSequencer;
	this.updateRunningStatus(" STOPPED");
	this.buildGuiSpecArray;
}

updateRunningStatus { arg argStatus;
	runningStatus = argStatus;
	{
		if (runningStatusView.notNil, {
			if (runningStatusView.notClosed, {
				runningStatusView.string = runningStatus;
			});
		});
	}.defer;
}

resetSequencer {
	var holdSlotNo;
	// reset variables
	// if nextPatternInd is 2 or 3 (i.e. it is using the pattern chain) get first pattern in chain
	if ((this.getSynthArgSpec("nextPatternInd") > 1), {
		// set chain step to start
		this.setSynthArgSpec("chainCurrentStep",this.getSynthArgSpec("chainStartStep"));
		// load new slot
		holdSlotNo = this.getSynthArgSpec("arrChainSlots").at(this.getSynthArgSpec("chainCurrentStep") - 1);
		this.setSynthArgSpec("slotNo", holdSlotNo);
		this.setSlotData(arrSlots.at(holdSlotNo));
		seqCurrentStep = this.getSynthArgSpec("seqStartStep") - 1;
	});
	seqCurrentStep = this.getSynthArgSpec("seqStartStep") - 1;
	// update status
	this.updateTriggerStatus;
// not used now
//	seqResetCounter = 0;
}

setTempo {
	var holdBPM;
	holdBPM =  ControlSpec(this.getSynthArgSpec("seqBPMMin"), this.getSynthArgSpec("seqBPMMax"))
		.map((this.getSynthArgSpec("seqBPM") + holdModSeqBPM).max(0).min(1));
	if (seqRunning == true, {
		seqClock.tempo = holdBPM/60;
	});
}

useTapTempo {arg argTempo;
	var autoBPM, minBPM, maxBPM;
	autoBPM = this.getSynthArgSpec("autoTapTempo");
	minBPM = this.getSynthArgSpec("seqBPMMin");
	maxBPM = this.getSynthArgSpec("seqBPMMax");
	if (autoBPM == 1,{
		if ((argTempo >= minBPM) and: (argTempo <= maxBPM),{
			this.setSynthArgSpec("seqBPM", ControlSpec(minBPM, maxBPM).unmap(argTempo));
			this.setTempo;
			this.updatePatternTime;
			system.flagGuiIfModDisplay(this);
		});
	});
}

actionTapTempo {	// tap tempo function used by Tap Tempo Action
	var holdBPM;
	if (newTapTime.isNil, {
		newTapTime = Main.elapsedTime
	}, {
		holdTapTime = Main.elapsedTime;
		holdBPM = 60 / (holdTapTime - newTapTime);
		newTapTime = holdTapTime;
		this.useTapTempo(holdBPM);
	});
}

extraSaveData {
	^[arrSlots, holdMIDIDeviceName, holdMIDIPortName];
}

loadExtraData {arg argData;
	var portIndex;
 	// stop seq first
 	this.triggerModeRunning = false;
 	this.stopSequencer;
 	// load data
	if (argData[0].notNil, {
		arrSlots = argData[0].deepCopy;
		this.setTempo;
		this.buildGuiSpecArray;
	});
	portIndex = this.getSynthArgSpec("midiPort");
	holdMIDIDeviceName = argData.at(1);
	holdMIDIPortName = argData.at(2);
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
	this.buildGuiSpecArray;
	this.resetSequencer;
	this.restoreOutputs;
}

getSlotData {
	^["seqStartStep", "seqEndStep", "seqNoteBase", "arrNotes", "arrRandOctaves",
		"arrVelocities", "arrRandVelocities", "arrStepLengthsX", "arrStepLengthsY",
		"arrNoteLengthsX", "arrNoteLengthsY",
		"arrProbabilities", "arrDelays", "arrRandDelays", "arrOnOffSteps", "description",
		"arrControl1", "arrControl2", "arrControl3",
	].collect({ arg item, i;
		this.getSynthArgSpec(item);
	});
}

setSlotData {arg argSlotData;
	if (argSlotData.notNil, {
		["seqStartStep", "seqEndStep", "seqNoteBase", "arrNotes", "arrRandOctaves",
			"arrVelocities", "arrRandVelocities", "arrStepLengthsX", "arrStepLengthsY",
			"arrNoteLengthsX", "arrNoteLengthsY",
			"arrProbabilities", "arrDelays", "arrRandDelays", "arrOnOffSteps", "description",
			"arrControl1", "arrControl2", "arrControl3",
		].do({ arg item, i;
			this.setSynthArgSpec(item, argSlotData.asArray.at(i));
		});
	}, {
		this.resetPattern;
	});
}

updateSlot {
	arrSlots.put(this.getSynthArgSpec("slotNo"), this.getSlotData.deepCopy);
}

updateSlotNo {
	var holdSlotNo;
	holdSlotNo = this.getSynthArgSpec("arrChainSlots").at(this.getSynthArgSpec("chainCurrentStep") - 1);
	this.setSynthArgSpec("slotNo", holdSlotNo);
	this.setSlotData(arrSlots.at(holdSlotNo));
}

updateCurrentChainStep {
	var holdSlotNo, holdArrChainSlots;
	holdSlotNo = this.getSynthArgSpec("slotNo");
	holdArrChainSlots = this.getSynthArgSpec("arrChainSlots");
	holdArrChainSlots.put(this.getSynthArgSpec("chainCurrentStep") - 1, holdSlotNo);
	this.setSynthArgSpec("arrChainSlots",  holdArrChainSlots);
}

resetPattern {
	[	["seqStartStep", 1],
		["seqEndStep", 16],
		["seqNoteBase", 48],
 		["arrNotes", Array.fill(64, 0)],
		["arrRandOctaves", Array.fill(64, 0)],
		["arrVelocities", Array.fill(64, 100)],
		["arrRandVelocities", Array.fill(64, 0)],
		["arrStepLengthsX", Array.fill(64, 1)],
		["arrStepLengthsY", Array.fill(64, 1)],
		["arrNoteLengthsX", Array.fill(64, 1)],
		["arrNoteLengthsY", Array.fill(64, 1)],
		["arrProbabilities", Array.fill(64, 100)],
		["arrOnOffSteps", Array.fill(64, 1)],
		["arrDelays", Array.fill(64, 0)],
		["arrRandDelays", Array.fill(64, 0)],
 		["description", ""],
		["arrControl1", Array.fill(64, 0)],
		["arrControl2", Array.fill(64, 0)],
		["arrControl3", Array.fill(64, 0)],
 	].do({ arg item, i;
 		this.setSynthArgSpec(item.at(0), item.at(1));
 	});
	// refresh view
	system.showView;
}

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

midiLearn { arg argSwitch = 0;
	// stop any previous routine
 	if (midiNoteOnResp.class == NoteOnResponder, {
 		midiNoteOnResp.remove;
 	});
	// if requested start responder
	if (argSwitch == 1, {
		// start responder
		midiNoteOnResp = NoteOnResponder ({  |src, chan, num, vel|
			var holdArrNotes, holdArrVelocities, holdSeqNoteBase;
			//	check whether to store
			if (	(chan >= (midiMinChannel-1)) and: (chan <= (midiMaxChannel-1)), {
				holdArrNotes = this.getSynthArgSpec("arrNotes");
				holdArrVelocities = this.getSynthArgSpec("arrVelocities");
				holdSeqNoteBase  = this.getSynthArgSpec("seqNoteBase");
				// store note and velocity
				holdArrNotes.put(this.getSynthArgSpec("midiLearnStep") - 1, num - holdSeqNoteBase);
				holdArrVelocities.put(this.getSynthArgSpec("midiLearnStep") - 1, vel * 100/127);
				this.setSynthArgSpec("arrNotes", holdArrNotes);
				this.setSynthArgSpec("arrVelocities", holdArrVelocities);
				// go to next step
				this.setSynthArgSpec("midiLearnStep", this.getSynthArgSpec("midiLearnStep") + 1);
				if (this.getSynthArgSpec("midiLearnStep") > 64, {
					this.midiLearn(0);
					this.setSynthArgSpec("midiLearn", 0);
					this.setSynthArgSpec("midiLearnStep", 1);
					system.flagGuiIfModDisplay(this);
				});
			});
			// refresh view
			system.flagGuiIfModDisplay(this);
		});
	}, {
		// refresh view
		system.showViewIfModDisplay(this);
	});
}

rebuildStepOrderRandArr {
	var rangeStart, rangeEnd, loRangeIndex, hiRangeIndex, rangeSize, randOrder, wholeArr, partArr;
	// scramble if selected
	if (	this.getSynthArgSpec("randomiseStepOrder") == 1, {
		rangeStart = this.getSynthArgSpec("seqStartStep");
		rangeEnd = this.getSynthArgSpec("seqEndStep");
		loRangeIndex = min(rangeStart, rangeEnd) - 1;
		hiRangeIndex = max(rangeStart, rangeEnd) - 1;
		rangeSize = hiRangeIndex - loRangeIndex + 1;
		wholeArr = Array.series(64);
		randOrder = Array.series(rangeSize).scramble;
		rangeSize.do({ arg i;
			wholeArr.put(loRangeIndex + i, randOrder.at(i));
		});
	},{
		wholeArr = Array.series(64);
	});
	this.setSynthArgSpec("stepOrderRandArr", wholeArr);
}

////////////////////////////////////
getSynthArgValByGroup {arg argGroup, argSynthArgName;
	var groupName, groupArray, synthArgArray, firstGpMembInd, firstGpMembSynthArg;
	groupName = ["arrGroup1s", "arrGroup2s", "arrGroup3s", "arrGroup4s",
		"arrGroup5s", "arrGroup6s", "arrGroup7s", "arrGroup8s"]
		.clipAt(argGroup-1);
	groupArray = this.getSynthArgSpec(groupName);
	// find first step in group
	firstGpMembInd = groupArray.indexOfEqual(1);
	// if  not nil return value of first step in group, else return nil
	if (firstGpMembInd.notNil, {
		synthArgArray = this.getSynthArgSpec(argSynthArgName);
		firstGpMembSynthArg = synthArgArray.at(firstGpMembInd);
	});
	^firstGpMembSynthArg;
}

setSynthArgValByGroup {arg argVal, argGroup, argSynthArgName;
	var groupName, groupArray, synthArgArray, firstGpMembInd, firstGpMembSynthArg;
	groupName = ["arrGroup1s", "arrGroup2s", "arrGroup3s", "arrGroup4s",
		"arrGroup5s", "arrGroup6s", "arrGroup7s", "arrGroup8s"]
		.clipAt(argGroup-1);
	groupArray = this.getSynthArgSpec(groupName);
	synthArgArray = this.getSynthArgSpec(argSynthArgName).deepCopy;
	// set value of all steps in group
	groupArray.do({arg item, i;
		if (item == 1, {
			synthArgArray.put(i, argVal);
		});
	});
	// update synth arg spec
	this.setSynthArgSpec(argSynthArgName, synthArgArray);
}


////////////////////////////////////

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

constrainChainStep {
	var holdCurrent, holdStart, holdEnd;
	// contrain current chain step between start and end steps
	holdCurrent = this.getSynthArgSpec("chainCurrentStep");
	holdStart = this.getSynthArgSpec("chainStartStep");
	holdEnd = this.getSynthArgSpec("chainEndStep");
	this.setSynthArgSpec("chainCurrentStep", holdCurrent.max(holdStart).min(holdEnd));
}

////////////////////////////////////

initProcessSpecs {
	var parmSelectGui, parmGroupSelectGui;
	arrProcSelNames = ["procSelOnOffSteps", "procSelNotes", "procSelRandOctaves", "procSelVelocities",
		"procSelRandVelocities", "procSelStepLengthsX", "procSelStepLengthsY",
		"procSelNoteLengthsX", "procSelNoteLengthsY", "procSelProbabilities",
		"procSelDelays", "procSelRandDelays",
		"procSelControl1s", "procSelControl2s", "procSelControl3s",
		"procSelGroup1s", "procSelGroup2s", "procSelGroup3s", "procSelGroup4s",
		"procSelGroup5s", "procSelGroup6s", "procSelGroup7s", "procSelGroup8s",
	];
	arrProcSynthArgNames = ["arrOnOffSteps", "arrNotes",  "arrRandOctaves",  "arrVelocities",
		"arrRandVelocities",  "arrStepLengthsX",  "arrStepLengthsY",
		"arrNoteLengthsX",  "arrNoteLengthsY",  "arrProbabilities", "arrDelays", "arrRandDelays",
		"arrControl1", "arrControl2", "arrControl3",
		"arrGroup1s", "arrGroup2s", "arrGroup3s", "arrGroup4s", "arrGroup5s",
		"arrGroup6s", "arrGroup7s", "arrGroup8s"
	];
	parmSelectGui = [
		["DividingLine"],
		["SpacerLine", 1],
		["TextBar", "Select which parameters should be processed:", 304],
		["ActionButton", "All Off", {
			arrProcSelNames.do({ arg item, i;
				this.setSynthArgSpec(item, 0);
			});
			system.showView;
			},
			71
		],
		["ActionButton", "All On", {
			arrProcSelNames.do({ arg item, i;
				this.setSynthArgSpec(item, 1);
			});
			system.showView;
			},
			71
		],
		["NextLine"],
		["TXCheckBox", "On/Off Steps", "procSelOnOffSteps", nil, 120],
 		["TXCheckBox", "Notes", "procSelNotes", nil, 120],
		["TXCheckBox", "Rand Octaves", "procSelRandOctaves", nil, 120],
		["TXCheckBox", "Velocities", "procSelVelocities", nil, 120],
		["TXCheckBox", "Rand Velocities", "procSelRandVelocities", nil, 120],
		["TXCheckBox", "Step Lengths X", "procSelStepLengthsX", nil, 120],
		["TXCheckBox", "Step Lengths Y", "procSelStepLengthsY", nil, 120],
		["TXCheckBox", "Note Lengths X", "procSelNoteLengthsX", nil, 120],
		["TXCheckBox", "Note Lengths Y", "procSelNoteLengthsY", nil, 120],
		["TXCheckBox", "Probabilities", "procSelProbabilities", nil, 120],
		["TXCheckBox", "Delays", "procSelDelays", nil, 120],
		["TXCheckBox", "Rand delays", "procSelRandDelays", nil, 120],
		["TXCheckBox", "Control 1", "procSelControl1s", nil, 120],
		["TXCheckBox", "Control 2", "procSelControl2s", nil, 120],
		["TXCheckBox", "Control 3", "procSelControl3s", nil, 120],
		["TXCheckBox", "Group 1", "procSelGroup1s", nil, 120],
		["TXCheckBox", "Group 2", "procSelGroup2s", nil, 120],
		["TXCheckBox", "Group 3", "procSelGroup3s", nil, 120],
		["TXCheckBox", "Group 4", "procSelGroup4s", nil, 120],
		["TXCheckBox", "Group 5", "procSelGroup5s", nil, 120],
		["TXCheckBox", "Group 6", "procSelGroup6s", nil, 120],
		["TXCheckBox", "Group 7", "procSelGroup7s", nil, 120],
		["TXCheckBox", "Group 8", "procSelGroup8s", nil, 120],
	];
	arrProcessSpecs = [
		["select a process...",
			{ },
			["Copy and paste steps once","Copy and paste steps repeatedly","Shift steps left",
			"Shift steps right ",
			"Randomise step order","Reverse step order","Constrain notes to chord, mode, scale",
			"Generate notes from chord, mode, scale","Generate notes from selected range",
			"Transpose notes upwards",
			"Transpose notes downwards","Randomise step on-off values","Randomise velocity values",
			"Randomise step length X values","Randomise step length Y values",
			"Randomise note length X values",
			"Randomise note length Y values","Randomise probability values","Randomise delay values",
			"Randomise control 1 values", "Randomise control 2 values", "Randomise control 3 values",
			].collect({ arg item, i;
				["ActionButton", item,
					{this.setSynthArgSpec("processTypeInd", i + 1);
						this.buildGuiSpecArray;
						system.showView;},
					260, TXColor.white, TXColor.sysGuiCol2
				]
			}),
		],
		["Copy and paste steps once",
			{ 	var sourceStart, sourceEnd, loSourceIndex, hiSourceIndex, wholeArr, sourceArr, changeArr;
				this.prepareCopyProcess;
				sourceStart = this.getSynthArgSpec("procSourceStart");
				sourceEnd = this.getSynthArgSpec("procSourceEnd");
				loSourceIndex = min(sourceStart, sourceEnd) - 1;
				hiSourceIndex = max(sourceStart, sourceEnd) - 1;
				arrProcSelNames.do({arg item, i;
					// if parameter selected for processing
					if (this.getSynthArgSpec(item) == 1, {
						wholeArr = this.getSynthArgSpec(arrProcSynthArgNames.at(i));
						sourceArr = wholeArr.copyRange(loSourceIndex.asInteger, hiSourceIndex.asInteger);
						changeArr = wholeArr.copyRange(loTargetIndex.asInteger, hiTargetIndex.asInteger);
						if (sourceArr.size > targetSize, {
							targetSize.do({ arg i;
								changeArr.put(i, sourceArr.at(i));
							});
						},{
							sourceArr.size.do({ arg i;
								changeArr.put(i, sourceArr.at(i));
							});
						});
						this.updateProcTarget(arrProcSynthArgNames.at(i), changeArr);					});
				});
			},
			[
				["TXRangeSlider", "Copy steps", ControlSpec(1, 64, step:1), "procSourceStart",
				 	"procSourceEnd", nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procCopyTargStart",
					"procCopyTargEnd",
					nil, arrSeqRangePresets],
			] ++ parmSelectGui,
		],
		["Copy and paste steps repeatedly",
			{ 	var sourceStart, sourceEnd, loSourceIndex, hiSourceIndex, wholeArr, sourceArr, changeArr;
				this.prepareCopyProcess;
				sourceStart = this.getSynthArgSpec("procSourceStart");
				sourceEnd = this.getSynthArgSpec("procSourceEnd");
				loSourceIndex = min(sourceStart, sourceEnd) - 1;
				hiSourceIndex = max(sourceStart, sourceEnd) - 1;
				arrProcSelNames.do({arg item, i;
					// if parameter selected for processing
					if (this.getSynthArgSpec(item) == 1, {
						wholeArr = this.getSynthArgSpec(arrProcSynthArgNames.at(i));
						sourceArr = wholeArr.copyRange(loSourceIndex.asInteger, hiSourceIndex.asInteger);
						changeArr = wholeArr.copyRange(loTargetIndex.asInteger, hiTargetIndex.asInteger);
						targetSize.do({ arg i;
							changeArr.put(i, sourceArr.wrapAt(i));
						});
						this.updateProcTarget(arrProcSynthArgNames.at(i), changeArr);					});
				});
			},
			[
				["TXRangeSlider", "Copy steps", ControlSpec(1, 64, step:1), "procSourceStart",
					"procSourceEnd", nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procCopyTargStart",
					"procCopyTargEnd", nil, arrSeqRangePresets],
			] ++ parmSelectGui,
		],
		["Shift steps left",
			{ 	var wholeArr, changeArr;
				this.prepareProcess;
				arrProcSelNames.do({arg item, i;
					// if parameter selected for processing
					if (this.getSynthArgSpec(item) == 1, {
						wholeArr = this.getSynthArgSpec(arrProcSynthArgNames.at(i));
						changeArr = wholeArr.copyRange(loTargetIndex.asInteger, hiTargetIndex.asInteger);
						changeArr = changeArr.rotate(this.getSynthArgSpec("procShiftSteps").neg.asInteger);
						this.updateProcTarget(arrProcSynthArgNames.at(i), changeArr);					});
				});
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["EZslider", "No. steps", ControlSpec(1, 64, step:1), "procShiftSteps"],
			] ++ parmSelectGui,
		],
		["Shift steps right ",
			{ 	var wholeArr, changeArr;
				this.prepareProcess;
				arrProcSelNames.do({arg item, i;
					// if parameter selected for processing
					if (this.getSynthArgSpec(item) == 1, {
						wholeArr = this.getSynthArgSpec(arrProcSynthArgNames.at(i));
						changeArr = wholeArr.copyRange(loTargetIndex.asInteger, hiTargetIndex.asInteger);
						changeArr = changeArr.rotate(this.getSynthArgSpec("procShiftSteps").asInteger);
						this.updateProcTarget(arrProcSynthArgNames.at(i), changeArr);					});
				});
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["EZslider", "No. steps", ControlSpec(1, 64, step:1), "procShiftSteps"],
			] ++ parmSelectGui,
		],
		["Randomise step order",
			{ 	var wholeArr, partArr, changeArr, randOrder;
				this.prepareProcess;
				randOrder = Array.series(targetSize).scramble;
				arrProcSelNames.do({arg item, i;
					// if parameter selected for processing
					if (this.getSynthArgSpec(item) == 1, {
						wholeArr = this.getSynthArgSpec(arrProcSynthArgNames.at(i));
						partArr = wholeArr.copyRange(loTargetIndex.asInteger, hiTargetIndex.asInteger);
						changeArr = [];
						targetSize.do({arg item, i;
							changeArr = changeArr.add(partArr.at(randOrder.at(i)));
						});
						this.updateProcTarget(arrProcSynthArgNames.at(i), changeArr);					});
				});
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
			] ++ parmSelectGui,
		],
		["Reverse step order",
			{ 	var wholeArr, partArr, changeArr;
				this.prepareProcess;
				arrProcSelNames.do({arg item, i;
					// if parameter selected for processing
					if (this.getSynthArgSpec(item) == 1, {
						wholeArr = this.getSynthArgSpec(arrProcSynthArgNames.at(i));
						partArr = wholeArr.copyRange(loTargetIndex.asInteger, hiTargetIndex.asInteger);
						changeArr = partArr.reverse;
						this.updateProcTarget(arrProcSynthArgNames.at(i), changeArr);
					});
				});
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
			] ++ parmSelectGui,
		],
		["Constrain notes to chord, mode, scale",
			{ 	var wholeArr, partArr, changeArr, arrScaleSpec, scaleRoot, seqNoteBase, arrScaleNotes;
				this.prepareProcess;
				wholeArr = this.getSynthArgSpec("arrNotes");
				partArr = wholeArr.copyRange(loTargetIndex.asInteger, hiTargetIndex.asInteger);
				arrScaleSpec = TXScale.arrScaleNotes.at(this.getSynthArgSpec("procScaleTypeInd"));
				scaleRoot = this.getSynthArgSpec("procScaleRoot");
				seqNoteBase = this.getSynthArgSpec("seqNoteBase");
				13.do({arg octave;
					arrScaleSpec.do({arg item, i;
						arrScaleNotes = arrScaleNotes.add(((octave-1) * 12) + scaleRoot + item);
					});
				});
				arrScaleNotes = arrScaleNotes.select({arg item, i; ((item > -1) and: (item < 128)); });
				changeArr = partArr.collect({arg item, i;
					var holdInd;
					// find index of closest valid note
					holdInd = arrScaleNotes.indexIn(item + seqNoteBase);
					arrScaleNotes.at(holdInd) - seqNoteBase;
				});
				this.updateProcTarget("arrNotes", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXPopupAction", "Chord/ scale", TXScale.arrScaleNames, "procScaleTypeInd", nil, 400],
				["NextLine"],
				["SpacerLine", 1],
				["TXPopupAction", "Key / root",
					["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
					"procScaleRoot", nil, 140],
			],
		],
		["Generate notes from chord, mode, scale",
			{ 	var changeArr, arrScaleSpec, scaleRoot, seqNoteBase, noteMin, noteMax, noteOrderInd,
					arrScaleNotes;
				this.prepareProcess;
				arrScaleSpec = TXScale.arrScaleNotes.at(this.getSynthArgSpec("procScaleTypeInd"));
				scaleRoot = this.getSynthArgSpec("procScaleRoot");
				seqNoteBase = this.getSynthArgSpec("seqNoteBase");
				noteMin = this.getSynthArgSpec("procRandNoteMin");
				noteMax = this.getSynthArgSpec("procRandNoteMax");
				noteOrderInd = this.getSynthArgSpec("procNoteOrderInd");
				13.do({arg octave;
					arrScaleSpec.do({arg item, i;
						arrScaleNotes = arrScaleNotes.add(((octave-1) * 12) + scaleRoot + item);
					});
				});
				arrScaleNotes =
					arrScaleNotes.select({arg item, i; ((item >= noteMin) and: (item <= noteMax)); });
				if (noteOrderInd == 3, {arrScaleNotes = arrScaleNotes.reverse; });
				if (noteOrderInd == 4, {arrScaleNotes = arrScaleNotes.mirror1; });
				if (noteOrderInd == 5, {arrScaleNotes = arrScaleNotes.reverse.mirror1; });
				targetSize.do({ arg i;
					changeArr = changeArr.add(arrScaleNotes.wrapAt(i));
				});
				if (noteOrderInd == 0, {changeArr = changeArr.scramble; });
				if (noteOrderInd == 1, {
					changeArr = Array.rand(targetSize, 0, targetSize-1).collect({arg item, i;
						changeArr.at(item);
					});
				});
				changeArr = changeArr - seqNoteBase;
				this.updateProcTarget("arrNotes", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXPopupAction", "Chord/ scale", TXScale.arrScaleNames, "procScaleTypeInd", nil, 400],
				["NextLine"],
				["SpacerLine", 1],
				["TXPopupAction", "Key / root",
					["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
					"procScaleRoot", nil, 140],
				["NextLine"],
				["SpacerLine", 1],
				["TXPopupAction", "Note order", [ "random order", "random selection & order",
					"forwards", "backwards","forwards-backwards","backwards-forwards"],
					"procNoteOrderInd", nil, 300],
				["NextLine"],
				["SpacerLine", 1],
				["TXNoteRangeSlider", "Note range", "procRandNoteMin", "procRandNoteMax", nil, true],
			],
		],
		["Generate notes from selected range",
			{ 	var seqNoteBase, noteOrderInd, arrScaleNotes;
				var sourceStart, sourceEnd, loSourceIndex, hiSourceIndex, wholeArr, sourceArr, changeArr;
				var octTranspMin, octTranspMax, loOct, hiOct;

				this.prepareProcess;
				sourceStart = this.getSynthArgSpec("procSourceStart");
				sourceEnd = this.getSynthArgSpec("procSourceEnd");
				loSourceIndex = min(sourceStart, sourceEnd) - 1;
				hiSourceIndex = max(sourceStart, sourceEnd) - 1;
				seqNoteBase = this.getSynthArgSpec("seqNoteBase");
				noteOrderInd = this.getSynthArgSpec("procNoteOrderInd");
				octTranspMin = this.getSynthArgSpec("procOctTranspMin");
				octTranspMax = this.getSynthArgSpec("procOctTranspMax");
				loOct = min(octTranspMin, octTranspMax);
				hiOct = max(octTranspMin, octTranspMax);
				wholeArr = this.getSynthArgSpec("arrNotes");
				sourceArr = wholeArr.copyRange(loSourceIndex.asInteger, hiSourceIndex.asInteger);
				((hiOct - loOct) + 1).do({arg octave;
					sourceArr.do({arg item, i;
						arrScaleNotes = arrScaleNotes.add(((loOct + octave) * 12) + item);
					});
				});
				arrScaleNotes =
					(arrScaleNotes + seqNoteBase).select({arg item, i; ((item > -1) and: (item < 128)); });
				if (noteOrderInd == 3, {arrScaleNotes = arrScaleNotes.reverse; });
				if (noteOrderInd == 4, {arrScaleNotes = arrScaleNotes ++ arrScaleNotes.reverse; });
				if (noteOrderInd == 5, {arrScaleNotes = arrScaleNotes.reverse ++ arrScaleNotes; });
				targetSize.do({ arg i;
					changeArr = changeArr.add(arrScaleNotes.wrapAt(i));
				});
				if (noteOrderInd == 0, {changeArr = changeArr.scramble; });
				if (noteOrderInd == 1, {
					changeArr = Array.rand(targetSize, 0, targetSize-1).collect({arg item, i;
						changeArr.at(item);
					});
				});
				changeArr = changeArr - seqNoteBase;
				this.updateProcTarget("arrNotes", changeArr);			},
			[
				["TXRangeSlider", "Copy steps", ControlSpec(1, 64, step:1), "procSourceStart",
					"procSourceEnd", nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXRangeSlider", "Oct Transpose", ControlSpec(-8, 8, step:1), "procOctTranspMin",
					"procOctTranspMax"],
				["NextLine"],
				["SpacerLine", 1],
				["TXPopupAction", "Note order", [ "random order", "random selection & order",
					"forwards", "backwards","forwards-backwards","backwards-forwards"],
					"procNoteOrderInd", nil, 300],
			],
		],
		["Transpose notes upwards",
			{ 	var wholeArr, partArr, changeArr, seqNoteBase, seqNoteTranspose;
				this.prepareProcess;
				wholeArr = this.getSynthArgSpec("arrNotes");
				partArr = wholeArr.copyRange(loTargetIndex.asInteger, hiTargetIndex.asInteger);
				seqNoteBase = this.getSynthArgSpec("seqNoteBase");
				seqNoteTranspose = this.getSynthArgSpec("procTranspose");
				changeArr = (partArr + seqNoteBase + seqNoteTranspose).min(127) - seqNoteBase;
				this.updateProcTarget("arrNotes", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["EZslider", "No. semitones", ControlSpec(1, 64, step:1), "procTranspose"],
			],
		],
		["Transpose notes downwards",
			{ 	var wholeArr, partArr, changeArr, seqNoteBase, seqNoteTranspose;
				this.prepareProcess;
				wholeArr = this.getSynthArgSpec("arrNotes");
				partArr = wholeArr.copyRange(loTargetIndex.asInteger, hiTargetIndex.asInteger);
				seqNoteBase = this.getSynthArgSpec("seqNoteBase");
				seqNoteTranspose = this.getSynthArgSpec("procTranspose");
				changeArr = (partArr + seqNoteBase - seqNoteTranspose).max(0) - seqNoteBase;
				this.updateProcTarget("arrNotes", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["EZslider", "No. semitones", ControlSpec(1, 64, step:1), "procTranspose"],
			],
		],
		["Randomise step on-off values",
			{ 	var changeArr, onProb;
				this.prepareProcess;
				onProb = this.getSynthArgSpec("procRandOnProb");
				changeArr = Array.fill(targetSize,{[1,0].wchoose([onProb, (100-onProb)] / 100);});
				this.updateProcTarget("arrOnOffSteps", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["EZslider", "On probability", ControlSpec(0, 100, step:1), "procRandOnProb"],
			],
		],
		["Randomise velocity values",
			{ 	var changeArr, minVal, maxVal;
				this.prepareProcess;
				minVal = this.getSynthArgSpec("procRandVelMin");
				maxVal = this.getSynthArgSpec("procRandVelMax");
				changeArr = Array.rand(targetSize, minVal, maxVal);
				this.updateProcTarget("arrVelocities", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXRangeSlider", "Vel range", ControlSpec(0, 100, step:1), "procRandVelMin",
					"procRandVelMax"],
			],
		],
		["Randomise step length X values",
			{ 	var changeArr, minVal, maxVal;
				this.prepareProcess;
				minVal = this.getSynthArgSpec("procRandStepXMin");
				maxVal = this.getSynthArgSpec("procRandStepXMax");
				changeArr = Array.rand(targetSize, minVal.asInteger, maxVal.asInteger);
				this.updateProcTarget("arrStepLengthsX", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXRangeSlider", "X range", ControlSpec(1, 64, step: 1), "procRandStepXMin",
					"procRandStepXMax"],
			],
		],
		["Randomise step length Y values",
			{ 	var changeArr, minVal, maxVal;
				this.prepareProcess;
				minVal = this.getSynthArgSpec("procRandStepYMin");
				maxVal = this.getSynthArgSpec("procRandStepYMax");
				changeArr = Array.rand(targetSize, minVal.asInteger, maxVal.asInteger);
				this.updateProcTarget("arrStepLengthsY", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXRangeSlider", "Y range", ControlSpec(1, 64, step: 1), "procRandStepYMin",
					"procRandStepYMax"],
			],
		],
		["Randomise note length X values",
			{ 	var changeArr, minVal, maxVal;
				this.prepareProcess;
				minVal = this.getSynthArgSpec("procRandNoteXMin");
				maxVal = this.getSynthArgSpec("procRandNoteXMax");
				changeArr = Array.rand(targetSize, minVal.asInteger, maxVal.asInteger);
				this.updateProcTarget("arrNoteLengthsX", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXRangeSlider", "X range", ControlSpec(1, 64, step: 1), "procRandNoteXMin",
					"procRandNoteXMax"],
			],
		],
		["Randomise note length Y values",
			{ 	var changeArr, minVal, maxVal;
				this.prepareProcess;
				minVal = this.getSynthArgSpec("procRandNoteYMin");
				maxVal = this.getSynthArgSpec("procRandNoteYMax");
				changeArr = Array.rand(targetSize, minVal.asInteger, maxVal.asInteger);
				this.updateProcTarget("arrNoteLengthsY", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXRangeSlider", "Y range", ControlSpec(1, 64, step: 1), "procRandNoteYMin",
					"procRandNoteYMax"],
			],
		],
		["Randomise probability values",
			{ 	var changeArr, minVal, maxVal;
				this.prepareProcess;
				minVal = this.getSynthArgSpec("procRandProbMin");
				maxVal = this.getSynthArgSpec("procRandProbMax");
				changeArr = Array.rand(targetSize, minVal, maxVal);
				this.updateProcTarget("arrProbabilities", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXRangeSlider", "Prob range", ControlSpec(0, 100, step:1), "procRandProbMin",
					"procRandProbMax"],
			],
		],
		["Randomise delay values",
			{ 	var changeArr, minVal, maxVal;
				this.prepareProcess;
				minVal = this.getSynthArgSpec("procRandDelayMin");
				maxVal = this.getSynthArgSpec("procRandDelayMax");
				changeArr = Array.rand(targetSize, minVal, maxVal);
				this.updateProcTarget("arrDelays", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXRangeSlider", "Delay range", ControlSpec(0, 100, step:1), "procRandDelayMin",
					"procRandDelayMax"],
			],
		],
		["Randomise control 1 values",
			{ 	var changeArr, minVal, maxVal;
				this.prepareProcess;
				minVal = this.getSynthArgSpec("procRandControlMin");
				maxVal = this.getSynthArgSpec("procRandControlMax");
				changeArr = Array.rand(targetSize, minVal, maxVal);
				this.updateProcTarget("arrControl1", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXRangeSlider", "Control range", ControlSpec(0, 100, step:1), "procRandControlMin",
					"procRandControlMax"],
			],
		],
		["Randomise control 2 values",
			{ 	var changeArr, minVal, maxVal;
				this.prepareProcess;
				minVal = this.getSynthArgSpec("procRandControlMin");
				maxVal = this.getSynthArgSpec("procRandControlMax");
				changeArr = Array.rand(targetSize, minVal, maxVal);
				this.updateProcTarget("arrControl2", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXRangeSlider", "Control range", ControlSpec(0, 100, step:1), "procRandControlMin",
					"procRandControlMax"],
			],
		],
		["Randomise control 3 values",
			{ 	var changeArr, minVal, maxVal;
				this.prepareProcess;
				minVal = this.getSynthArgSpec("procRandControlMin");
				maxVal = this.getSynthArgSpec("procRandControlMax");
				changeArr = Array.rand(targetSize, minVal, maxVal);
				this.updateProcTarget("arrControl3", changeArr);
			},
			[
				["TXRangeSlider", "Target steps", ControlSpec(1, 64, step:1), "procTargStart", "procTargEnd",
					nil, arrSeqRangePresets],
				["NextLine"],
				["SpacerLine", 1],
				["TXRangeSlider", "Control range", ControlSpec(0, 100, step:1), "procRandControlMin",
					"procRandControlMax"],
			],
		],

	];
}

prepareProcess {
	var targStart, targEnd;
	targStart = this.getSynthArgSpec("procTargStart");
	targEnd = this.getSynthArgSpec("procTargEnd");
	loTargetIndex = min(targStart, targEnd) - 1;
	hiTargetIndex = max(targStart, targEnd) - 1;
	targetSize = hiTargetIndex - loTargetIndex + 1;
}

prepareCopyProcess {
	// processes which copy use different target start & end variables to other processes
	var targStart, targEnd;
	targStart = this.getSynthArgSpec("procCopyTargStart");
	targEnd = this.getSynthArgSpec("procCopyTargEnd");
	loTargetIndex = min(targStart, targEnd) - 1;
	hiTargetIndex = max(targStart, targEnd) - 1;
	targetSize = hiTargetIndex - loTargetIndex + 1;
}

updateProcTarget {arg argSynthArgString, argChangeArray;
	var outArray;
	outArray = this.getSynthArgSpec(argSynthArgString);
	targetSize.do({ arg i;
		outArray.put(loTargetIndex + i, argChangeArray.at(i));
	});
	this.setSynthArgSpec(argSynthArgString, outArray);
}

}

