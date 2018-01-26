// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXLFOCurve : TXModuleBase {

	classvar <>classData;

	var arrCurveValues;
	var arrSlotData, curveDataEvent;
	var arrGridPresetNames, arrGridPresetActions;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "LFO Curve";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Restart", 1, "restartTrig", nil],
			["Frequency", 1, "modFreq", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrBufferSpecs = [ ["bufnumCurve", 700, 1] ];
		classData.guiWidth = 950;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var holdControlSpec;
		curveDataEvent = ();
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		arrSynthArgSpecs = [
			["out", 0, 0],
			["bufnumCurve", 0, \ir],
			["restartTrig", 0, 0],
			["t_userRestartTrig", 0, 0],
			["freq", 0.5, defLagTime],
			["freqMin", 0.01, defLagTime],
			["freqMax", 100, defLagTime],
			["modFreq", 0, defLagTime],
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
		synthDefFunc = { arg out, bufnumCurve, restartTrig = 0, t_userRestartTrig = 0, freq, freqMin, freqMax, modFreq = 0;
			var holdRestartTrig, outFreq, outCurve, rangeFunction, outSignal;
			holdRestartTrig = TXClean.kr(restartTrig + t_userRestartTrig);
			outFreq = ( (freqMax/freqMin) ** ((freq + modFreq).max(0.001).min(1)) ) * freqMin;
			outCurve = BufRd.kr(1, bufnumCurve,
				Phasor.kr(holdRestartTrig, outFreq * ControlDur.ir * 700, 0, 700));

			// select function based on arrOptions
			rangeFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			outSignal = rangeFunction.value(outCurve);
			Out.kr(out, outSignal);
		};
		holdControlSpec = ControlSpec(0.001, 100, \exp, 0, 1, units: " Hz");
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
			["ActionButton", "Restart", {this.moduleNode.set("t_userRestartTrig", 1);},
				120, TXColor.white, TXColor.sysGuiCol2],
			["Spacer", 20],
			["ActionButton", "Freq x 2", {this.freqMultiply(2);}, 60],
			["ActionButton", "Freq x 3", {this.freqMultiply(3);}, 60],
			["ActionButton", "Freq / 2", {this.freqMultiply(0.5);}, 60],
			["ActionButton", "Freq / 3", {this.freqMultiply(1/3);}, 60],
			["NextLine"],
			["TXFreqBpmMinMaxSldr", "Frequency", holdControlSpec, "freq", "freqMin", "freqMax",
				nil, TXLFO.arrLFOFreqRanges,],
			["SpacerLine", 2],
			["TXCurveDraw", "LFO curve", {arrCurveValues},
				{arg view; arrCurveValues = view.value; arrSlotData = view.arrSlotData;
					this.bufferStore(view.value);},
				{arrSlotData}, "LFO", 706, 324, nil, "gridRows", "gridCols",
				"time", "output level", curveDataEvent],
			// ["ActionButton", "Rebuild curve by mirroring ", {this.runMirror}, 250],
			// ["Spacer", 10],
			// ["ActionButton", "Rebuild curve by mirroring & inverting",
			// {this.runMirrorInvert}, 250],
			["NextLine"],
			["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 128), "gridRows", {system.showView}, nil, nil, 40],
			["Spacer", 2],
			["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 128), "gridCols", {system.showView}, nil, nil, 40],
			["Spacer", 2],
			["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 200],
			["Spacer", 6],
			["ActionButton", "Quantise to grid", {this.quantiseToGrid(quantizeRows: true, quantizeCols: true)}, 94],
			["ActionButton", "Quantise rows", {this.quantiseToGrid(quantizeRows: true, quantizeCols: false)}, 90],
			["ActionButton", "Quantise columns", {this.quantiseToGrid(quantizeRows: false, quantizeCols: true)}, 102],
			["SpacerLine", 2],
			["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 0, 350],
		];
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Restart", {this.moduleNode.set("t_userRestartTrig", 1);}],
		]
		++ guiSpecArray);
		//	initialise buffer to linear curve
		arrCurveValues = Array.newClear(700).seriesFill(0, 1/699);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make the buffer, load the synthdef and create the synth
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

	freqMultiply { arg argMultiplyValue;
		var currentFreq, minFreq, maxFreq, holdControlSpec, newFreq;
		minFreq = this.getSynthArgSpec("freqMin");
		maxFreq = this.getSynthArgSpec("freqMax");
		holdControlSpec = ControlSpec.new(minFreq, maxFreq, \exp);
		currentFreq = holdControlSpec.map(this.getSynthArgSpec("freq"));
		newFreq = currentFreq * argMultiplyValue;
		if (argMultiplyValue < 1, {
			if ( newFreq >= minFreq, {
				this.setSynthValue("freq", holdControlSpec.unmap(newFreq));
			});
		},{
			if ( newFreq <= maxFreq, {
				this.setSynthValue("freq", holdControlSpec.unmap(newFreq));
			});
		});
		system.flagGuiIfModDisplay(this);
	}

	runMirror {
		var holdArray, startVal, midArray, endVal, holdSignal;
		holdArray = arrCurveValues.deepCopy;
		startVal = holdArray.first.asArray;
		midArray = holdArray.drop(2).drop(-2);
		midArray = midArray.clump(2).collect({arg item, i; item.sum/2;});
		endVal = holdArray.last.asArray;
		holdArray = startVal ++ midArray ++ endVal;
		holdArray = holdArray ++ holdArray.copy.reverse;
		holdSignal = Signal.newFrom(holdArray);
		arrCurveValues = Array.newFrom(holdSignal);
		this.bufferStore(arrCurveValues);
		system.showView;
	}

	runMirrorInvert {
		var holdArray, startVal, midArray, endVal, holdSignal;
		holdArray = arrCurveValues.deepCopy;
		startVal = holdArray.first.asArray;
		midArray = holdArray.drop(2).drop(-2);
		midArray = midArray.clump(2).collect({arg item, i; item.sum/2;});
		endVal = holdArray.last.asArray;
		holdArray = startVal ++ midArray ++ endVal;
		holdArray = (holdArray ++ holdArray.copy.reverse.neg).normalize(0,1);
		holdSignal = Signal.newFrom(holdArray);
		arrCurveValues = Array.newFrom(holdSignal);
		this.bufferStore(arrCurveValues);
		system.showView;
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
		arrCurveValues = argData.at(0);
		if (arrCurveValues.size != 700, {
			arrCurveValues = arrCurveValues.resamp1(700);
		});
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
	}

}

