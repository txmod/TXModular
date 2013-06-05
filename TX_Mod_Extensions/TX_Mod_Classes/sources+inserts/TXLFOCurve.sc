// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXLFOCurve : TXModuleBase {		// Same as TXLFOCurve but with a longer curve

	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar	<guiWidth=950;
	classvar	<arrBufferSpecs;

	var arrCurveValues;
	var arrSlotData;
	var arrGridPresetNames, arrGridPresetActions;

	*initClass{
		arrInstances = [];
		//	set class specific variables
		defaultName = "LFO Curve";
		moduleRate = "control";
		moduleType = "source";
		arrCtlSCInBusSpecs = [
			["Frequency", 1, "modFreq", 0],
		];
		noOutChannels = 1;
		arrOutBusSpecs = [
			["Out", [0]]
		];
		arrBufferSpecs = [ ["bufnumCurve", 700, 1] ];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var holdControlSpec;
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		arrSynthArgSpecs = [
			["out", 0, 0],
			["bufnumCurve", 0, \ir],
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
		synthDefFunc = { arg out, bufnumCurve, freq, freqMin, freqMax, modFreq = 0;
			var outFreq, outCurve, rangeFunction, outSignal;
			outFreq = ( (freqMax/freqMin) ** ((freq + modFreq).max(0.001).min(1)) ) * freqMin;
			outCurve = BufRd.kr(1, bufnumCurve,
				Phasor.kr(0, outFreq * ControlDur.ir * 700, 0, 700));

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
			["TXMinMaxSliderSplit", "Frequency", holdControlSpec, "freq", "freqMin", "freqMax",
				nil, TXLFO.arrLFOFreqRanges],
			["NextLine"],
			["TXCurveDraw", "LFO curve", {arrCurveValues},
				{arg view; arrCurveValues = view.value; arrSlotData = view.arrSlotData;
					this.bufferStore(view.value);},
				{arrSlotData}, "LFO", 700, 340, nil, "gridRows", "gridCols",
				"time", "output level"],
			["ActionButton", "Rebuild curve by mirroring ", {this.runMirror}, 250],
			["Spacer", 10],
			["ActionButton", "Rebuild curve by mirroring & inverting",
				{this.runMirrorInvert}, 250],
			["NextLine"],
			["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 99), "gridRows", {system.showView}],
			["Spacer", 10],
			["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 99), "gridCols", {system.showView}],
			["Spacer", 10],
			["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 200],
			["Spacer", 10],
			["ActionButton", "Quantise to grid", {this.quantiseToGrid}, 130],
			["NextLine"],
			["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 0, 350],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	initialise buffer to linear curve
		arrCurveValues = Array.newClear(700).seriesFill(0, 1/699);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make the buffer, load the synthdef and create the synth
		this.makeBuffersAndSynth(arrBufferSpecs);
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

	quantiseToGrid {
		var newArray, rows, cols, holdSignal;
		var maxVal = 700;
		newArray = Array.newClear(maxVal);
		rows = this.getSynthArgSpec("gridRows");
		cols = this.getSynthArgSpec("gridCols");

		cols.do({arg item, i;
			var jump, startRange, endRange, meanVal;
			// jump = (maxVal / cols);
			jump = cols.reciprocal;
			startRange = (item * jump * maxVal).round(1);
			endRange = ((item + 1) * jump * maxVal).round(1) - 1;
			meanVal = arrCurveValues.copyRange(startRange.asInteger, endRange.asInteger).mean;
			newArray[startRange.asInteger..endRange.asInteger] = meanVal.round(rows.reciprocal);
		});
		holdSignal = Signal.newFrom(newArray);
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

