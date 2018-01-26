// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXEnvCurve : TXModuleBase {

	classvar <>classData;

	var arrCurveValues;
	var arrSlotData, curveDataEvent;
	var arrGridPresetNames, arrGridPresetActions;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Env Curve";
		classData.moduleRate = "control";
		classData.moduleType = "groupsource";
		classData.arrCtlSCInBusSpecs = [
			["Env Time", 1, "modEnvTotalTime", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrBufferSpecs = [ ["bufnumCurve", 700, 1] ];
		classData.guiWidth = 950;
		classData.timeSpec = ControlSpec(0.01, 1600);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		curveDataEvent = ();
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		arrSynthArgSpecs = [
			["out", 0, 0],
			["bufnumCurve", 0, \ir],
			["note", 0, \ir],
			["velocity", 0, \ir],
			["envTotalTime", ControlSpec(0.01, 20).unmap(3.0), \ir],
			["envTotalTimeMin", 0.01, \ir],
			["envTotalTimeMax", 20, \ir],
			["velocityScaling", 1, \ir],
			["modEnvTotalTime", 0, \ir],
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
		arrOptions = [0];
		arrOptionData = [
			[	["Positive only: 0 to 1", {arg input; input}],
				["Positive & Negative: -1 to 1", {arg input; (input * 2) - 1}],
				["Positive & Negative: -0.5 to 0.5", {arg input; input - 0.5}],
			],
		];
		synthDefFunc = {
			arg out, bufnumCurve, note, velocity, envTotalTime, envTotalTimeMin, envTotalTimeMax, velocityScaling,
			modEnvTotalTime = 0;
			var envTime, outCurve, rangeFunction, outSignal;
			// adjust endpoint so BufRd doesn't go back to start of buffer
			envTime = (envTotalTime + modEnvTotalTime).linlin(0, 1, envTotalTimeMin, envTotalTimeMax);
			outCurve = BufRd.kr(1, bufnumCurve,
				Line.kr(0, 699, envTime, doneAction: 2));
			// select function based on arrOptions
			rangeFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			// amplitude is vel *  0.007874 approx. == 1 / 127
			outSignal = rangeFunction.value(
				outCurve * ((velocity * 0.007874) + (1-velocityScaling)).min(1)
			);
			Out.kr(out, outSignal);
		};
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
			["TXCurveDraw", "Env curve", {arrCurveValues},
				{arg view; arrCurveValues = view.value; arrSlotData = view.arrSlotData;
					this.bufferStore(view.value);},
				{arrSlotData}, nil, 706, 250, "Sine", "gridRows", "gridCols",
				"time", "output level", curveDataEvent],
			["SpacerLine", 1],
			["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 128), "gridRows", {system.showView}, nil, nil, 40],
			["Spacer", 2],
			["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 128), "gridCols", {system.showView}, nil, nil, 40],
			["Spacer", 2],
			["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 200],
			["Spacer", 6],
			["ActionButton", "Quantise to grid", {this.quantiseToGrid(quantizeRows: true, quantizeCols: true)}, 94],
			["ActionButton", "Quantise rows", {this.quantiseToGrid(quantizeRows: true, quantizeCols: false)}, 90],
			["ActionButton", "Quantise columns", {this.quantiseToGrid(quantizeRows: false, quantizeCols: true)}, 102],
			["DividingLine"],
			["SpacerLine", 1],
			["TXMinMaxSliderSplit", "Env Time", classData.timeSpec, "envTotalTime", "envTotalTimeMin", "envTotalTimeMax"],
			["SpacerLine", 1],
			["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 0, 350],
			["Spacer", 20],
			["TXCheckBox", "Scale level to velocity", "velocityScaling"],
			["Spacer", 20],
			["PolyphonySelector"],
			["Spacer", 20],
			["ActionButton", "Trigger Envelope", {this.createSynthNote(60, 100, 0.1);},
				150, TXColor.white, TXColor.sysGuiCol2],
			["DividingLine"],
			["SpacerLine", 1],
			["MIDIListenCheckBox"],
			["NextLine"],
			["MIDIChannelSelector"],
			["NextLine"],
			["MIDINoteSelector"],
			["NextLine"],
			["MIDIVelSelector"],
		];
		arrActionSpecs = this.buildActionSpecs(
			[["commandAction", "Trigger Envelope", {this.createSynthNote(60, 100, 0.1);}]]
			++ guiSpecArray.deepCopy
		);
		//	initialise buffer
		arrCurveValues = Signal.sineFill(700, [1.0],[1.5pi]).collect({arg item, i; (item.value + 1) * 0.5;});
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.setMonophonic;	// monophonic by default
		this.midiNoteInit;
		//	make the buffer, load the synthdef and create the group
		this.makeBuffersAndGroup(classData.arrBufferSpecs);

		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// buffer store
			this.bufferStore(arrCurveValues);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		//	initialise slots to linear curves
		arrSlotData = arrCurveValues.dup(5);
	}

	quantiseToGrid {arg quantizeRows = true, quantizeCols = true;
		var holdArray, holdSignal, outArray, holdCols;
		var rows, cols;
		var maxVal = 700;
		holdArray = Array.newClear(maxVal);
		rows = this.getSynthArgSpec("gridRows");
		cols = this.getSynthArgSpec("gridCols");

		if (quantizeCols, {
			cols.do({arg item;
				var jump, startRange, endRange, meanVal;
				jump = cols.reciprocal;
				startRange = (item * jump * maxVal).round(1);
				endRange = ((item + 1) * jump * maxVal).round(1) - 1;
				meanVal = arrCurveValues.copyRange(startRange.asInteger, endRange.asInteger).mean;
				if (quantizeRows, {
					meanVal = meanVal.round(rows.reciprocal);
				});
				holdArray[startRange.asInteger..endRange.asInteger] = meanVal;
			});
		},{
			holdArray = arrCurveValues.collect({arg item;
				var outVal = item;
				if (quantizeRows, {
					outVal = outVal.round(rows.reciprocal);
				});
				outVal;
			});
		});
		holdSignal = Signal.newFrom(holdArray);
		arrCurveValues = Array.newFrom(holdSignal);
		this.bufferStore(arrCurveValues);
		system.showView;
	}

	bufferStore { arg argArray;
		buffers.at(0).sendCollection(argArray);
	}

	extraSaveData { // override default method
		^[arrCurveValues, arrSlotData];
	}

	loadExtraData {arg argData;  // override default method
		var holdVal, holdMax;
		arrCurveValues = argData.at(0);
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// buffer store
			this.bufferStore(arrCurveValues);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		arrSlotData = argData.at(1);
		// adjust older data
		if (system.dataBank.savedSystemRevision < 1003, {
			holdVal = this.getSynthArgSpec("envTotalTime");
			holdMax = 20.max((holdVal + 0.5).round);
			this.setSynthArgSpec("envTotalTimeMin", 0.01);
			this.setSynthArgSpec("envTotalTimeMax", holdMax);
			this.setSynthArgSpec("envTotalTime", ControlSpec(0.01, holdMax).unmap(holdVal));
		});
	}

}

