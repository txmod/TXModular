// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXNoteStacker : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var	displayLayer;
	var	midiNoteOnResp;
	var	midiNoteOffResp;
	var	moduleActive;
	var	<>testMIDIVel = 100;
	var	linearCurve;
	var curveDataEvent;
	var	arrVelocityCurves;
	var	arrSlotData;
	var	arrNoteCurveStrings;
	var	arrDetuneCurveStrings;
	var	arrDetuneMaxStrings;
	var	arrRandomDetuneStrings;
	var	arrRandomVelStrings;
	var	arrTransposeStrings;
	var	holdLayerNo;
	var	clipBoardNoteCurve;
	var	clipBoardDetuneCurve;
	var	rotateIndex = 0;
	var	<>noteOutModuleL1M1;
	var	<>noteOutModuleL1M2;
	var	<>noteOutModuleL1M3;
	var	<>noteOutModuleL2M1;
	var	<>noteOutModuleL2M2;
	var	<>noteOutModuleL2M3;
	var	<>noteOutModuleL3M1;
	var	<>noteOutModuleL3M2;
	var	<>noteOutModuleL3M3;
	var	<>noteOutModuleL4M1;
	var	<>noteOutModuleL4M2;
	var	<>noteOutModuleL4M3;
	var	<>noteOutModuleL5M1;
	var	<>noteOutModuleL5M2;
	var	<>noteOutModuleL5M3;
	var	<>noteOutModuleL6M1;
	var	<>noteOutModuleL6M2;
	var	<>noteOutModuleL6M3;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Note Stacker";
		classData.moduleRate = "control";
		classData.moduleType = "groupaction";
		classData.noInChannels = 0;
		classData.noOutChannels = 0;
		classData.guiWidth = 800;
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
		curveDataEvent = ();
		displayOption = "showMIDI";
		displayLayer = "showLayer1";
		holdLayerNo = 1;
		moduleActive = true;
		clipBoardNoteCurve = 1 ! 84;
		clipBoardDetuneCurve = 0 ! 84;
		arrNoteCurveStrings = ["arrNoteLevels1", "arrNoteLevels2", "arrNoteLevels3",
			"arrNoteLevels4", "arrNoteLevels5", "arrNoteLevels6"];
		arrDetuneCurveStrings = ["arrDetuneValsL1", "arrDetuneValsL2", "arrDetuneValsL3",
			"arrDetuneValsL4", "arrDetuneValsL5", "arrDetuneValsL6"];
		arrDetuneMaxStrings = ["arrDetuneMaxL1", "arrDetuneMaxL2", "arrDetuneMaxL3",
			"arrDetuneMaxL4", "arrDetuneMaxL5", "arrDetuneMaxL6"];
		arrRandomDetuneStrings = ["arrRandomDetuneL1", "arrRandomDetuneL2", "arrRandomDetuneL3",
			"arrRandomDetuneL4", "arrRandomDetuneL5", "arrRandomDetuneL6"];
		arrRandomVelStrings = ["arrRandomVelL1", "arrRandomVelL2", "arrRandomVelL3",
			"arrRandomVelL4", "arrRandomVelL5", "arrRandomVelL6"];
		arrTransposeStrings = ["arrTransposeL1", "arrTransposeL2", "arrTransposeL3",
			"arrTransposeL4", "arrTransposeL5", "arrTransposeL6"];
		arrSynthArgSpecs = [
			["noteOutModuleL1MID1", nil],
			["noteOutModuleL1MID2", nil],
			["noteOutModuleL1MID3", nil],
			["noteOutModuleL2MID1", nil],
			["noteOutModuleL2MID2", nil],
			["noteOutModuleL2MID3", nil],
			["noteOutModuleL3MID1", nil],
			["noteOutModuleL3MID2", nil],
			["noteOutModuleL3MID3", nil],
			["noteOutModuleL4MID1", nil],
			["noteOutModuleL4MID2", nil],
			["noteOutModuleL4MID3", nil],
			["noteOutModuleL5MID1", nil],
			["noteOutModuleL5MID2", nil],
			["noteOutModuleL5MID3", nil],
			["noteOutModuleL6MID1", nil],
			["noteOutModuleL6MID2", nil],
			["noteOutModuleL6MID3", nil],
			["trigTypeL1", 1],
			["trigTypeL2", 1],
			["trigTypeL3", 1],
			["trigTypeL4", 1],
			["trigTypeL5", 1],
			["trigTypeL6", 1],
			["gridRows", 2],
			["gridCols", 2],
			["velCurveBuildType", 0],
			["velCurveLayersIndex", 0],
			["velCurveXfade", 0.5],
			["arrNoteLevels1", 1 ! 84],
			["arrNoteLevels2", 1 ! 84],
			["arrNoteLevels3", 1 ! 84],
			["arrNoteLevels4", 1 ! 84],
			["arrNoteLevels5", 1 ! 84],
			["arrNoteLevels6", 1 ! 84],
			["arrDetuneValsL1", 0 ! 84],
			["arrDetuneValsL2", 0 ! 84],
			["arrDetuneValsL3", 0 ! 84],
			["arrDetuneValsL4", 0 ! 84],
			["arrDetuneValsL5", 0 ! 84],
			["arrDetuneValsL6", 0 ! 84],
			["arrDetuneMaxL1", 1],
			["arrDetuneMaxL2", 1],
			["arrDetuneMaxL3", 1],
			["arrDetuneMaxL4", 1],
			["arrDetuneMaxL5", 1],
			["arrDetuneMaxL6", 1],
			["arrRandomDetuneL1", 0],
			["arrRandomDetuneL2", 0],
			["arrRandomDetuneL3", 0],
			["arrRandomDetuneL4", 0],
			["arrRandomDetuneL5", 0],
			["arrRandomDetuneL6", 0],
			["arrRandomVelL1", 0],
			["arrRandomVelL2", 0],
			["arrRandomVelL3", 0],
			["arrRandomVelL4", 0],
			["arrRandomVelL5", 0],
			["arrRandomVelL6", 0],
			["arrTransposeL1", 0],
			["arrTransposeL2", 0],
			["arrTransposeL3", 0],
			["arrTransposeL4", 0],
			["arrTransposeL5", 0],
			["arrTransposeL6", 0],
		];
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TestNoteVals"],
			["MIDIListenCheckBox"],
			["MIDIChannelSelector"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.midiNoteInit;
		//	initialise slots to linear curves
		linearCurve = Array.newClear(128).seriesFill(0, 1/127);
		arrVelocityCurves = linearCurve.deepCopy.dup(6);
		arrSlotData = linearCurve.deepCopy.dup(5);
		//	overwrite default preset
		this.overwritePreset(this, "Default Settings", 0);
	}

	buildGuiSpecArray {
		var holdControlSpec, holdPopupItems, arrTrigTypeNames, transposeSpec;
		var	arrGridPresetNames, arrGridPresetActions;
		holdControlSpec = ControlSpec(1,16, step: 0 );
		transposeSpec = ControlSpec(-128, 127);
		holdPopupItems = 16.collect({ arg item, i; (item+1).asString; });
		arrTrigTypeNames = ["trigger nothing - layer disabled", "trigger all output modules",
			"trigger a random output module", "rotate trigger between outputs" ];
		arrGridPresetNames = ["1 x 1", "2 x 2", "3 x 3", "4 x 4", "5 x 5", "6 x 6", "8 x 8", "9 x 9",
			"10 x 10", "12 x 12", "16 x 16", "24 x 24", "32 x 32"];
		arrGridPresetActions = [
			{this.setSynthArgSpec("gridRows", 1); this.setSynthArgSpec("gridCols", 1); },
			{this.setSynthArgSpec("gridRows", 2); this.setSynthArgSpec("gridCols", 2); },
			{this.setSynthArgSpec("gridRows", 3); this.setSynthArgSpec("gridCols", 3); },
			{this.setSynthArgSpec("gridRows", 4); this.setSynthArgSpec("gridCols", 4); },
			{this.setSynthArgSpec("gridRows", 5); this.setSynthArgSpec("gridCols", 5); },
			{this.setSynthArgSpec("gridRows", 6); this.setSynthArgSpec("gridCols", 6); },
			{this.setSynthArgSpec("gridRows", 8); this.setSynthArgSpec("gridCols", 8); },
			{this.setSynthArgSpec("gridRows", 9); this.setSynthArgSpec("gridCols", 9); },
			{this.setSynthArgSpec("gridRows", 10); this.setSynthArgSpec("gridCols", 10); },
			{this.setSynthArgSpec("gridRows", 12); this.setSynthArgSpec("gridCols", 12); },
			{this.setSynthArgSpec("gridRows", 16); this.setSynthArgSpec("gridCols", 16); },
			{this.setSynthArgSpec("gridRows", 24); this.setSynthArgSpec("gridCols", 24); },
			{this.setSynthArgSpec("gridRows", 32); this.setSynthArgSpec("gridCols", 32); },
		];
		guiSpecArray = [
			["ActionButton", "MIDI / Outputs", {displayOption =  "showMIDI";
				this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayOption == "showMIDI")],
			["Spacer", 3],
			["ActionButton", "Velocity Curve", {displayOption = "showVelocityCurve";
				this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayOption == "showVelocityCurve")],
			["Spacer", 3],
			["ActionButton", "Note Levels", {displayOption = "showNoteCurve";
				this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayOption == "showNoteCurve")],
			["Spacer", 3],
			["ActionButton", "Note Detune", {displayOption = "showDetuneCurve";
				this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayOption == "showDetuneCurve")],
			["Spacer", 3],
			["SpacerLine", 4],
		];
		if (displayOption == "showMIDI", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["MIDIListenCheckBox"],
				["SpacerLine", 2],
				["MIDIChannelSelector"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["TextBarLeft", "Output modules and triggering types for all layers:", 300],
				["SpacerLine", 4],
				["SeqSelect3GroupModules", "noteOutModuleL1M1", "noteOutModuleL1M2", "noteOutModuleL1M3",
					"noteOutModuleL1MID1", "noteOutModuleL1MID2", "noteOutModuleL1MID3", "Layer 1 Outs"],
				["TXPopupAction", "Trigger", arrTrigTypeNames, "trigTypeL1", nil, 210, 0],
				["SpacerLine", 2],

				["SeqSelect3GroupModules", "noteOutModuleL2M1", "noteOutModuleL2M2", "noteOutModuleL2M3",
					"noteOutModuleL2MID1", "noteOutModuleL2MID2", "noteOutModuleL2MID3", "Layer 2 Outs"],
				["TXPopupAction", "Trigger", arrTrigTypeNames, "trigTypeL2", nil, 210, 0],
				["SpacerLine", 2],

				["SeqSelect3GroupModules", "noteOutModuleL3M1", "noteOutModuleL3M2", "noteOutModuleL3M3",
					"noteOutModuleL3MID1", "noteOutModuleL3MID2", "noteOutModuleL3MID3", "Layer 3 Outs"],
				["TXPopupAction", "Trigger", arrTrigTypeNames, "trigTypeL3", nil, 210, 0],
				["SpacerLine", 2],

				["SeqSelect3GroupModules", "noteOutModuleL4M1", "noteOutModuleL4M2", "noteOutModuleL4M3",
					"noteOutModuleL4MID1", "noteOutModuleL4MID2", "noteOutModuleL4MID3", "Layer 4 Outs"],
				["TXPopupAction", "Trigger", arrTrigTypeNames, "trigTypeL4", nil, 210, 0],
				["SpacerLine", 2],

				["SeqSelect3GroupModules", "noteOutModuleL5M1", "noteOutModuleL5M2", "noteOutModuleL5M3",
					"noteOutModuleL5MID1", "noteOutModuleL5MID2", "noteOutModuleL5MID3", "Layer 5 Outs"],
				["TXPopupAction", "Trigger", arrTrigTypeNames, "trigTypeL5", nil, 210, 0],
				["SpacerLine", 2],

				["SeqSelect3GroupModules", "noteOutModuleL6M1", "noteOutModuleL6M2", "noteOutModuleL6M3",
					"noteOutModuleL6MID1", "noteOutModuleL6MID2", "noteOutModuleL6MID3", "Layer 6 Outs"],
				["TXPopupAction", "Trigger", arrTrigTypeNames, "trigTypeL6", nil, 210, 0],
				["SpacerLine", 10],
				["MIDIKeyboard", {arg note; this.createSynthNote(note, testMIDIVel, 0);},
					7, 80, 750, 24, {arg note; this.midiNoteRelease(note);}, "C0 - B7"],
			];
		});
		if (displayOption == "showVelocityCurve" or:
			(displayOption == "showNoteCurve") or:
			(displayOption == "showDetuneCurve"), {
				guiSpecArray = guiSpecArray ++[
					["ActionButton", "Layer 1", {displayLayer = "showLayer1"; holdLayerNo = 1;
						this.buildGuiSpecArray; system.showView;}, 80,
						TXColor.white, this.getButtonColour(displayLayer == "showLayer1")],
					["Spacer", 3],
					["ActionButton", "Layer 2", {displayLayer = "showLayer2";  holdLayerNo = 2;
						this.buildGuiSpecArray; system.showView;}, 80,
						TXColor.white, this.getButtonColour(displayLayer == "showLayer2")],
					["Spacer", 3],
					["ActionButton", "Layer 3", {displayLayer = "showLayer3";  holdLayerNo = 3;
						this.buildGuiSpecArray; system.showView;}, 80,
						TXColor.white, this.getButtonColour(displayLayer == "showLayer3")],
					["Spacer", 3],
					["ActionButton", "Layer 4", {displayLayer = "showLayer4";  holdLayerNo = 4;
						this.buildGuiSpecArray; system.showView;}, 80,
						TXColor.white, this.getButtonColour(displayLayer == "showLayer4")],
					["Spacer", 3],
					["ActionButton", "Layer 5", {displayLayer = "showLayer5";  holdLayerNo = 5;
						this.buildGuiSpecArray; system.showView;}, 80,
						TXColor.white, this.getButtonColour(displayLayer == "showLayer5")],
					["Spacer", 3],
					["ActionButton", "Layer 6", {displayLayer = "showLayer6";  holdLayerNo = 6;
						this.buildGuiSpecArray; system.showView;}, 80,
						TXColor.white, this.getButtonColour(displayLayer == "showLayer6")],
					//			["Spacer", 3],
					//			["ActionButton", "All Layers", {displayLayer = "showAllLayers";  holdLayerNo = 99;
					//				this.buildGuiSpecArray; system.showView;}, 120,
					//				TXColor.white, this.getButtonColour(displayLayer == "showAllLayers")],
					["SpacerLine", 2],
				]
				++ [
					[
						["SeqSelect3GroupModules", "noteOutModuleL1M1", "noteOutModuleL1M2", "noteOutModuleL1M3",
							"noteOutModuleL1MID1", "noteOutModuleL1MID2", "noteOutModuleL1MID3", "Layer 1 Outs"],
						["TXPopupAction", "Trigger", arrTrigTypeNames, "trigTypeL1", nil, 210, 0],
					],
					[
						["SeqSelect3GroupModules", "noteOutModuleL2M1", "noteOutModuleL2M2", "noteOutModuleL2M3",
							"noteOutModuleL2MID1", "noteOutModuleL2MID2", "noteOutModuleL2MID3", "Layer 2 Outs"],
						["TXPopupAction", "Trigger", arrTrigTypeNames, "trigTypeL2", nil, 210, 0],
					],
					[
						["SeqSelect3GroupModules", "noteOutModuleL3M1", "noteOutModuleL3M2", "noteOutModuleL3M3",
							"noteOutModuleL3MID1", "noteOutModuleL3MID2", "noteOutModuleL3MID3", "Layer 3 Outs"],
						["TXPopupAction", "Trigger", arrTrigTypeNames, "trigTypeL3", nil, 210, 0],
					],
					[
						["SeqSelect3GroupModules", "noteOutModuleL4M1", "noteOutModuleL4M2", "noteOutModuleL4M3",
							"noteOutModuleL4MID1", "noteOutModuleL4MID2", "noteOutModuleL4MID3", "Layer 4 Outs"],
						["TXPopupAction", "Trigger", arrTrigTypeNames, "trigTypeL4", nil, 210, 0],
					],
					[
						["SeqSelect3GroupModules", "noteOutModuleL5M1", "noteOutModuleL5M2", "noteOutModuleL5M3",
							"noteOutModuleL5MID1", "noteOutModuleL5MID2", "noteOutModuleL5MID3", "Layer 5 Outs"],
						["TXPopupAction", "Trigger", arrTrigTypeNames, "trigTypeL5", nil, 210, 0],
					],
					[
						["SeqSelect3GroupModules", "noteOutModuleL6M1", "noteOutModuleL6M2", "noteOutModuleL6M3",
							"noteOutModuleL6MID1", "noteOutModuleL6MID2", "noteOutModuleL6MID3", "Layer 6 Outs"],
						["TXPopupAction", "Trigger", arrTrigTypeNames, "trigTypeL6", nil, 210, 0],
					],
				].at(holdLayerNo-1);
		});
		if (displayOption == "showVelocityCurve", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 6],
				["TXCurveDraw", "Vel curve", {arrVelocityCurves[holdLayerNo-1]},
					{arg view; arrVelocityCurves[holdLayerNo-1] = view.value; arrSlotData = view.arrSlotData;},
					{arrSlotData}, "Velocity", nil, nil, nil, "gridRows", "gridCols",
					"input level", "output level", curveDataEvent],
				["SpacerLine", 2],
				["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 99), "gridRows", {system.showView}, nil, nil, 40],
				["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 99), "gridCols", {system.showView}, nil, nil, 40],
				["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 200],
				["SpacerLine", 2],
				["EZslider", "Random velocity mix", ControlSpec(0, 1), arrRandomVelStrings[holdLayerNo-1], nil, 400, 140],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["TextBarLeft", "Curve Auto-Build:", 100],
				["TXPopupAction", "Target: ", ["Linear curve", "Current Layer 1", ], "velCurveBuildType",
					nil, 200, 60],
				["TXPopupAction", "Across: ",
					["Layers 1-2", "Layers 1-3", "Layers 1-4", "Layers 1-5", "Layers 1-6"], "velCurveLayersIndex",
					nil, 180, 60],
				["EZslider", "X-fade", ControlSpec(0, 1), "velCurveXfade", nil, 180, 60, 40],
				["ActionButton", "Build now", {this.buildVelCurves}, 100],
			];
		});
		if (displayOption == "showNoteCurve", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 6],
				["TextBarLeft", "Note levels:", 110],
				["ActionButton", "Copy curve",
					{clipBoardNoteCurve = this.getSynthArgSpec(arrNoteCurveStrings[holdLayerNo-1]).deepCopy}, 104],
				["ActionButton", "Paste curve",
					{this.setSynthArgSpec(arrNoteCurveStrings[holdLayerNo-1],
						clipBoardNoteCurve.deepCopy); system.showView; }, 104],
				["ActionButton", "Smooth curve",
					{var inCurve, outCurve; inCurve = this.getSynthArgSpec(arrNoteCurveStrings[holdLayerNo-1]).deepCopy;
						outCurve = [];
						inCurve.size.do({arg item, i;
							if ((i > 0) and: (i < (inCurve.size-1)), {
								outCurve = outCurve.add((inCurve.at(i) + inCurve.at(i-1)+ inCurve.at(i+1)) / 3);
							},{
								outCurve = outCurve.add(inCurve.at(i));
							});
						});
						this.setSynthArgSpec(arrNoteCurveStrings[holdLayerNo-1], outCurve);
						system.showView;},
					104, nil, TXColor.sysGuiCol2],
				["ActionButton", "Invert curve",
					{var curve; curve = this.getSynthArgSpec(arrNoteCurveStrings[holdLayerNo-1]).deepCopy;
						this.setSynthArgSpec(arrNoteCurveStrings[holdLayerNo-1], 1 - curve);
						system.showView;},
					104, nil, TXColor.sysGuiCol2],
				["ActionButton", "Randomise curve",
					{this.setSynthArgSpec(arrNoteCurveStrings[holdLayerNo-1], Array.rand(84, 0.0, 1.0););
						system.showView;},
					104, nil, TXColor.sysGuiCol2],
				["ActionButton", "Default curve",
					{this.setSynthArgSpec(arrNoteCurveStrings[holdLayerNo-1], 1 ! 84);
						system.showView; },
					104, nil, TXColor.sysDeleteCol],
				["SpacerLine", 2],
				["TXMultiSlider", "Level", ControlSpec(0, 1), arrNoteCurveStrings[holdLayerNo-1],
					84, nil, 160, nil, nil, 0, 9, 6, 7],
				["NextLine"],
				["Spacer", 0],
				["MIDIKeyboard", {arg note; this.createSynthNote(note, testMIDIVel, 0);},
					7, 80, 758, 24, {arg note; this.midiNoteRelease(note);},
					"Notes: C0 - B7"],
				["SpacerLine", 2],
			];
		});
		if (displayOption == "showDetuneCurve", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 6],
				["EZslider", "Detune range semitones +/-", ControlSpec(0, 1), arrDetuneMaxStrings[holdLayerNo-1], nil, 360, 160],
				["Spacer", 30],
				["EZNumber", "Transpose semitones", transposeSpec, arrTransposeStrings[holdLayerNo-1], nil, 120, 60],
				["ActionButton", "=0", {this.setSynthArgSpec(arrTransposeStrings[holdLayerNo-1],
					transposeSpec.constrain(0)); system.showView;}, 30],
				["ActionButton", "+1", {this.setSynthArgSpec(arrTransposeStrings[holdLayerNo-1],
					transposeSpec.constrain(this.getSynthArgSpec(arrTransposeStrings[holdLayerNo-1]) + 1)); system.showView;}, 30],
				["ActionButton", "-1", {this.setSynthArgSpec(arrTransposeStrings[holdLayerNo-1],
					transposeSpec.constrain(this.getSynthArgSpec(arrTransposeStrings[holdLayerNo-1]) - 1)); system.showView;}, 30],
				["ActionButton", "+12", {this.setSynthArgSpec(arrTransposeStrings[holdLayerNo-1],
					transposeSpec.constrain(this.getSynthArgSpec(arrTransposeStrings[holdLayerNo-1]) + 12)); system.showView;}, 30],
				["ActionButton", "-12", {this.setSynthArgSpec(arrTransposeStrings[holdLayerNo-1],
					transposeSpec.constrain(this.getSynthArgSpec(arrTransposeStrings[holdLayerNo-1]) - 12)); system.showView;}, 30],
				["SpacerLine", 4],
				["EZslider", "Random detune mix", ControlSpec(0, 1), arrRandomDetuneStrings[holdLayerNo-1], nil, 360, 160],
				["SpacerLine", 4],
				["TextBarLeft", "Note detune:", 110],
				["ActionButton", "Copy curve",
					{clipBoardDetuneCurve = this.getSynthArgSpec(arrDetuneCurveStrings[holdLayerNo-1]).deepCopy},
					104],
				["ActionButton", "Paste curve",
					{this.setSynthArgSpec(arrDetuneCurveStrings[holdLayerNo-1],
						clipBoardDetuneCurve.deepCopy); system.showView; }, 104],
				["ActionButton", "Smooth curve",
					{var inCurve, outCurve; inCurve = this.getSynthArgSpec(arrDetuneCurveStrings[holdLayerNo-1]).deepCopy;
						outCurve = [];
						inCurve.size.do({arg item, i;
							if ((i > 0) and: (i < (inCurve.size-1)), {
								outCurve = outCurve.add((inCurve.at(i) + inCurve.at(i-1)+ inCurve.at(i+1)) / 3);
								},{
									outCurve = outCurve.add(inCurve.at(i));
							});
						});
						this.setSynthArgSpec(arrDetuneCurveStrings[holdLayerNo-1], outCurve);
						system.showView;},
					104, nil, TXColor.sysGuiCol2],
				["ActionButton", "Invert curve",
					{var curve; curve = this.getSynthArgSpec(arrDetuneCurveStrings[holdLayerNo-1]).deepCopy;
						this.setSynthArgSpec(arrDetuneCurveStrings[holdLayerNo-1], curve.neg);
						system.showView;},
					104, nil, TXColor.sysGuiCol2],
				["ActionButton", "Randomise curve",
					{this.setSynthArgSpec(arrDetuneCurveStrings[holdLayerNo-1], Array.rand(84, -1.0, 1.0););
						system.showView;},
					104, nil, TXColor.sysGuiCol2],
				["ActionButton", "Default curve",
					{this.setSynthArgSpec(arrDetuneCurveStrings[holdLayerNo-1], 0 ! 84);
						system.showView; },
					104, nil, TXColor.sysDeleteCol],
				["NextLine"],
				["TXMultiSlider", "Detune", ControlSpec(-1, 1), arrDetuneCurveStrings[holdLayerNo-1],
					84, nil, 160, nil, nil, 0, 9, 10, 7],
				["NextLine"],
				["Spacer", 0],
				["MIDIKeyboard", {arg note; this.createSynthNote(note, testMIDIVel, 0);},
					7, 80, 758, 24, {arg note; this.midiNoteRelease(note);}, "C0 - B7"],
				["SpacerLine", 2],
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

	buildVelCurves {
		var buildType, numLayers, curveXfade, targetCurve, arrMaskCurves, arrMaskEnvLevels, arrMaskEnvTimes;
		var arrStart, arrMid, arrEnd, arrVals;
		buildType = this.getSynthArgSpec("velCurveBuildType");
		numLayers = this.getSynthArgSpec("velCurveLayersIndex") + 2;
		curveXfade = this.getSynthArgSpec("velCurveXfade");
		if (buildType == 0, {
			targetCurve = linearCurve.copy;
			},{
				targetCurve = arrVelocityCurves[0].copy;
		});
		// create mask curves using envs
		arrMaskEnvLevels =  ( (0 ! (numLayers * 4)) ++ [0])! numLayers;
		arrStart = [1, 1, 1, 1, 0.5];
		arrMid = [0.5, 1, 1, 1, 0.5];
		arrEnd = [0.5, 1, 1, 1, 1];
		numLayers.do({arg layer;
			if (layer == 0, {
				arrVals = arrStart;
				},{
					if (layer == (numLayers-1), {
						arrVals = arrEnd;
						},{
							arrVals = arrMid;
					});
			});
			5.do({arg i;
				arrMaskEnvLevels[layer][(layer * 4) + i] = arrVals[i];
			});
		});
		arrMaskEnvTimes = ([curveXfade, (1-curveXfade), (1-curveXfade), curveXfade] ! numLayers).flatten;
		arrMaskCurves = [];
		numLayers.do({arg i;
			arrMaskCurves = arrMaskCurves.add(Env.new(arrMaskEnvLevels[i], arrMaskEnvTimes).discretize(128));
		});
		// replace vel curves
		numLayers.do({arg i;
			arrVelocityCurves[i] = targetCurve * arrMaskCurves[i];
		});
		system.showView;
	}

	restoreOutputs {
		var holdID;
		holdID = this.getSynthArgSpec("noteOutModuleL1MID1");
		if (holdID.notNil, {noteOutModuleL1M1 = system.getModuleFromID(holdID)}, {noteOutModuleL1M1 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleL1MID2");
		if (holdID.notNil, {noteOutModuleL1M2 = system.getModuleFromID(holdID)}, {noteOutModuleL1M2 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleL1MID3");
		if (holdID.notNil, {noteOutModuleL1M3 = system.getModuleFromID(holdID)}, {noteOutModuleL1M3 = nil});

		holdID = this.getSynthArgSpec("noteOutModuleL2MID1");
		if (holdID.notNil, {noteOutModuleL2M1 = system.getModuleFromID(holdID)}, {noteOutModuleL2M1 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleL2MID2");
		if (holdID.notNil, {noteOutModuleL2M2 = system.getModuleFromID(holdID)}, {noteOutModuleL2M2 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleL2MID3");
		if (holdID.notNil, {noteOutModuleL2M3 = system.getModuleFromID(holdID)}, {noteOutModuleL2M3 = nil});

		holdID = this.getSynthArgSpec("noteOutModuleL3MID1");
		if (holdID.notNil, {noteOutModuleL3M1 = system.getModuleFromID(holdID)}, {noteOutModuleL3M1 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleL3MID2");
		if (holdID.notNil, {noteOutModuleL3M2 = system.getModuleFromID(holdID)}, {noteOutModuleL3M2 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleL3MID3");
		if (holdID.notNil, {noteOutModuleL3M3 = system.getModuleFromID(holdID)}, {noteOutModuleL3M3 = nil});

		holdID = this.getSynthArgSpec("noteOutModuleL4MID1");
		if (holdID.notNil, {noteOutModuleL4M1 = system.getModuleFromID(holdID)}, {noteOutModuleL4M1 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleL4MID2");
		if (holdID.notNil, {noteOutModuleL4M2 = system.getModuleFromID(holdID)}, {noteOutModuleL4M2 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleL4MID3");
		if (holdID.notNil, {noteOutModuleL4M3 = system.getModuleFromID(holdID)}, {noteOutModuleL4M3 = nil});

		holdID = this.getSynthArgSpec("noteOutModuleL5MID1");
		if (holdID.notNil, {noteOutModuleL5M1 = system.getModuleFromID(holdID)}, {noteOutModuleL5M1 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleL5MID2");
		if (holdID.notNil, {noteOutModuleL5M2 = system.getModuleFromID(holdID)}, {noteOutModuleL5M2 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleL5MID3");
		if (holdID.notNil, {noteOutModuleL5M3 = system.getModuleFromID(holdID)}, {noteOutModuleL5M3 = nil});

		holdID = this.getSynthArgSpec("noteOutModuleL6MID1");
		if (holdID.notNil, {noteOutModuleL6M1 = system.getModuleFromID(holdID)}, {noteOutModuleL6M1 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleL6MID2");
		if (holdID.notNil, {noteOutModuleL6M2 = system.getModuleFromID(holdID)}, {noteOutModuleL6M2 = nil});
		holdID = this.getSynthArgSpec("noteOutModuleL6MID3");
		if (holdID.notNil, {noteOutModuleL6M3 = system.getModuleFromID(holdID)}, {noteOutModuleL6M3 = nil});
	}

	arrLayerParms {
		^[
			["trigTypeL1", [noteOutModuleL1M1, noteOutModuleL1M2, noteOutModuleL1M3],
				"arrNoteLevels1", "arrDetuneValsL1", "arrDetuneMaxL1", "arrRandomDetuneL1", "arrRandomVelL1", "arrTransposeL1",],
			["trigTypeL2", [noteOutModuleL2M1, noteOutModuleL2M2, noteOutModuleL2M3],
				"arrNoteLevels2", "arrDetuneValsL2", "arrDetuneMaxL2", "arrRandomDetuneL2", "arrRandomVelL2", "arrTransposeL2",],
			["trigTypeL3", [noteOutModuleL3M1, noteOutModuleL3M2, noteOutModuleL3M3],
				"arrNoteLevels3", "arrDetuneValsL3", "arrDetuneMaxL3", "arrRandomDetuneL3", "arrRandomVelL3", "arrTransposeL3",],
			["trigTypeL4", [noteOutModuleL4M1, noteOutModuleL4M2, noteOutModuleL4M3],
				"arrNoteLevels4", "arrDetuneValsL4", "arrDetuneMaxL4", "arrRandomDetuneL4", "arrRandomVelL4", "arrTransposeL4",],
			["trigTypeL5", [noteOutModuleL5M1, noteOutModuleL5M2, noteOutModuleL5M3],
				"arrNoteLevels5", "arrDetuneValsL5", "arrDetuneMaxL5", "arrRandomDetuneL5", "arrRandomVelL5", "arrTransposeL5",],
			["trigTypeL6", [noteOutModuleL6M1, noteOutModuleL6M2, noteOutModuleL6M3],
				"arrNoteLevels6", "arrDetuneValsL6", "arrDetuneMaxL6", "arrRandomDetuneL6", "arrRandomVelL6", "arrTransposeL6",],
		];
	}

	createSynthNote { arg note, vel, argEnvTime=1, seqLatencyOn=1, argDetune=0;
		var transpose, outDetune, outVel, noteIndex, trigtype, randDetune, randVel;
		var arrValidModules, arrPlayModules, latencyTime=0;
		// adjust noteIndex to note range C0-b7 for table lookups
		noteIndex = (note - 24).max(0).min(83);
		if (moduleActive == true, {
			// retrigger modules
			this.arrLayerParms.do({ arg item, i;
				trigtype = this.getSynthArgSpec(item[0]);
				if (trigtype > 0, {
					arrValidModules = item[1].select({arg mod, i; mod.notNil;});
					if (arrValidModules.size > 0, {
						arrPlayModules = [];
						// trigtype selects which modules to play
						if (trigtype == 1, {
							arrPlayModules = arrValidModules;
						});
						if (trigtype == 2, {
							arrPlayModules = [arrValidModules.choose];
						});
						if (trigtype == 3, {
							rotateIndex = rotateIndex + 1;
							arrPlayModules = [arrValidModules.wrapAt(rotateIndex)];
						});
						if (arrPlayModules.notEmpty, {
							// transpose
							transpose = this.getSynthArgSpec(item[7]);
							// read vel from curve * individual noteLevel
							outVel = arrVelocityCurves[i][vel] * 127
							* this.getSynthArgSpec(item[2])[noteIndex];
							randVel = this.getSynthArgSpec(item[6]);
							if (randVel > 0, {
								outVel = ((1-randVel) * outVel) + (randVel * 127.rand);
							});
							// individual detune + rand * detune max
							outDetune = this.getSynthArgSpec(item[3])[noteIndex];
							randDetune = this.getSynthArgSpec(item[5]);
							if (randDetune > 0, {
								outDetune = ((1-randDetune) * outDetune) + (randDetune * 1.0.rand2);
							});
							outDetune = outDetune * this.getSynthArgSpec(item[4]);
							outDetune = argDetune + outDetune;
							arrPlayModules.do({arg module, k;
								// create note in module
								module.createSynthNote(note, outVel, argEnvTime, seqLatencyOn,
									outDetune + transpose);
							});
						});
					});
				});
			});
		});
	}

	midiNoteRelease { arg note; // override default method
		var arrValidModules;
		if (moduleActive == true, {
			// only release if sustain pedal is off
			if (midiSustainPedalState == 0, {
				this.arrLayerParms.do({ arg item, i;
					if (this.getSynthArgSpec(item[0]) > 0, {
						arrValidModules = item[1].select({arg item, i; item.notNil;});
						arrValidModules.do({arg module, k;
							module.midiNoteRelease(note);
						});
					});
				});
			},{
				arrHeldMidiNotes = arrHeldMidiNotes.add(note);
			});
		});
	}

	releaseSynthGate { arg argNote=60; // override default method
		var arrValidModules;
			this.arrLayerParms.do({ arg item, i;
				if (this.getSynthArgSpec(item[0]) > 0, {
					arrValidModules = item[1].select({arg item, i; item.notNil;});
					arrValidModules.do({arg module, k;
						module.releaseSynthGate(argNote);
					});
				});
			});
	}

	arrActiveModules {
		var arrModules;
		arrModules = [];
		// retrigger modules
		this.arrLayerParms.do({ arg item, i;
			if (this.getSynthArgSpec(item[0]) > 0, {
				item[1].do({arg module, k;
					if (module.notNil, {
						arrModules = arrModules.add(module);
					});
				});
			});
		});
		^arrModules;
	}

	setMidiBend { arg inVal;  // override default method
		// for all active modules pass bend data
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

	rebuildSynth {
		// override base class method
	}

	runAction { moduleActive = true; }   //	override base class

	pauseAction { moduleActive = false;}   //	override base class

	extraSaveData { // override default method
		^[arrVelocityCurves, arrSlotData];
	}

	loadExtraData {arg argData;  // override default method
		arrVelocityCurves = argData.at(0);
		arrSlotData = argData.at(1);
	}

}

