// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXGainCurveSt : TXModuleBase {

	classvar <>classData;

	var arrCurveValues;
	var arrSlotData, curveDataEvent;
	var arrGridPresetNames, arrGridPresetActions;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Gain Curve St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Gate", 1, "modGate", 0],
			["Gate close time", 1, "modGateCloseTime", 0],
			["Level scale", 1, "modLevelScale", 0],
			["Env Time", 1, "modEnvTotalTime", 0],
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
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
			["in", 0, 0],
			["out", 0, 0],
			["bufnumCurve", 0, \ir],
			["gate", 0, 0],
			["gateCloseTime", 0.2, 0],
			["gateCloseTimeMin", 0, 0],
			["gateCloseTimeMax", 5, 0],
			["envTotalTime", ControlSpec(0.01, 20).unmap(3.0), 0],
			["envTotalTimeMin", 0.01, 0],
			["envTotalTimeMax", 20, 0],
			["levelScale", 1, defLagTime],
			["modGate", 0, 0],
			["modGateCloseTime", 0, 0],
			["modLevelScale", 0, defLagTime],
			["modEnvTotalTime", 0, 0],
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
		arrOptions = [0];
		arrOptionData = [
			[ //gate mode
				["Fixed length - triggered by gate, final value set by curve",
					{arg gate;
						gate;
					}
				],
				["Forced release - triggered & ended by gate using close time, final value is zero",
					{arg gate, gateCloseTime, gateCloseTimeMin, gateCloseTimeMax, modGateCloseTime;
						var gtCloseTime;
						gtCloseTime= (gateCloseTimeMin + ((gateCloseTimeMax - gateCloseTimeMin)
							* (gateCloseTime + modGateCloseTime))).max(0.01).min(20);
						gate - ((1 - gate) * (1 + gateCloseTime));
					}
				]
			],
		];
		synthDefFunc = {arg in, out, bufnumCurve, gate, gateCloseTime, gateCloseTimeMin, gateCloseTimeMax,
			envTotalTime, envTotalTimeMin, envTotalTimeMax, levelScale,
			modGate = 0, modGateCloseTime = 0, modLevelScale = 0, modEnvTotalTime = 0;
			var envTime, gt, levelGate, gateFunction, outCurve, levelSc, levelFunc;
			var input = InFeedback.ar(in,2);

			envTime = (envTotalTime + modEnvTotalTime).linlin(0, 1, envTotalTimeMin, envTotalTimeMax);
			gt = (gate + modGate).max(0).min(1);
			gateFunction = this.getSynthOption(0);
			levelGate = gateFunction.value(gt, gateCloseTime, gateCloseTimeMin, gateCloseTimeMax, modGateCloseTime);
			outCurve = BufRd.kr(1, bufnumCurve,
				699 * EnvGen.kr( Env([0,0, 1], [0,1]), gt, timeScale: envTime, doneAction: 0);
			);
			levelFunc = [
				{EnvGen.kr( Env([0, 1, 1], [0, 1]), levelGate, timeScale: envTime, doneAction: 0);},
				{EnvGen.kr( Env([0, 1, 1, 0], [0, 1, 0]), levelGate, timeScale: envTime, doneAction: 0);},
			] [arrOptions[0]];
			levelSc = (levelScale + modLevelScale).max(0).min(1) * levelFunc.value;
			Out.ar(out, input * outCurve * levelSc);
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
			["NextLine"],
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
			["DividingLine"],
			["SpacerLine", 1],
			["EZslider", "Level scale", ControlSpec(0, 1), "levelScale"],
			["DividingLine"],
			["SpacerLine", 1],
			["TXCheckBox", "Gate", "gate"],
			["Spacer", 4],
			["SynthOptionPopupPlusMinus", "Gate Mode", arrOptionData, 0, 600],
			["SpacerLine", 1],
			["TXMinMaxSliderSplit", "Close time", ControlSpec(0, 20),
				"gateCloseTime", "gateCloseTimeMin", "gateCloseTimeMax",],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	initialise buffer
		arrCurveValues = Signal.sineFill(700, [1.0],[1.5pi]).collect({arg item, i; (item.value + 1) * 0.5;});
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.makeBuffersAndSynth(classData.arrBufferSpecs);

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

