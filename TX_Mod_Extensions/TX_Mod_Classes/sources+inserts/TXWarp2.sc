// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXWarp2 : TXModuleBase {		// Warp module

	classvar <>classData;

	var displayOption;
	var arrCurveValues;
	var arrSlotData, curveDataEvent;
	var arrGridPresetNames, arrGridPresetActions;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Warp";
		classData.moduleRate = "control";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Quantise", 1, "modQuantise", 0],
			["Quant steps", 1, "modSteps", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrBufferSpecs = [ ["bufnumWarp", 128, 1] ];
		classData.specSteps = ControlSpec(2, 128, step: 1);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	*arrOutputRanges {
		^ [
			["Range presets: ", [0, 1]],
			["Full range -1 to 1", [-1, 1]],
			["Half range -0.5 to 0.5", [-0.5, 0.5]],
			["Positive range 0 to 1", [0, 1]],
			["Negative range -1 to 0", [-1, 0]],
		];
	}

	init {arg argInstName;
		curveDataEvent = ();
		extraLatency = 0.05;	// allow extra time when recreating
		displayOption = "showInputOutput";
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["bufnumWarp", 0, \ir],
			["inputMin", 0, defLagTime],
			["inputMax", 1, defLagTime],
			["outputMin", 0, defLagTime],
			["outputMax", 1, defLagTime],
			["quantise", 0, 0],
			["steps", classData.specSteps.unmap(4), 0],
			["stepsMin", 2, 0],
			["stepsMax", 128, 0],
			["modQuantise", 0, 0],
			["modSteps", 0, 0],
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
		arrOptions = [1];
		arrOptionData = [
			// output range
			[
				["Quantise input, before warping", {arg mode, signal, round;
					var outSig;
					if (mode == 'out', {
						outSig = signal;
					}, {
						outSig = signal.round(round);
					});
				}],
				["Quantise output, after warping (default)", {arg mode, signal, round;
					var outSig;
					if (mode == 'in', {
						outSig = signal;
					}, {
						outSig = signal.round(round);
					});
				}],
			],
		];
		synthDefFunc = { arg in, out, bufnumWarp, inputMin, inputMax, outputMin, outputMax,
			quantise, steps, stepsMin, stepsMax, modQuantise = 0, modSteps = 0;

			var quantFunction, holdQuant, holdSteps, holdRound;
			var inSignal, inQuant, inLimit, scaleWarp, invWarp, curveWarp, curveQuant, outWarp;

			quantFunction = this.getSynthOption(0);
			holdQuant = (quantise + modQuantise).max(0).min(1);
			holdSteps = (steps + modSteps).max(0).min(1).linlin(0, 1, stepsMin, stepsMax).round;
			holdRound = holdQuant * (holdSteps - 1).reciprocal;

			inSignal = TXClean.kr(In.kr(in,1));

			// apply limits
			inLimit = inSignal.max(inputMin).min(inputMax);
			// scale to limits
			scaleWarp = (inLimit - inputMin)  / (inputMax - inputMin);

			// quantise
			inQuant = quantFunction.value('in', scaleWarp, holdRound);

			// apply curve by indexing into buffer
			curveWarp =  BufRd.kr(1, bufnumWarp, inQuant * 127, 0, 0);

			// quantise
			curveQuant = quantFunction.value('out', curveWarp, holdRound);

			// map to o/p range
			outWarp = outputMin + (curveQuant * (outputMax - outputMin));

			Out.kr(out, TXClean.kr(outWarp));
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
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TXRangeSlider", "Input range", \bipolar, "inputMin", "inputMax", nil,
				this.class.arrOutputRanges],
			["TXRangeSlider", "Output range", \bipolar, "outputMin", "outputMax", nil, this.class.arrOutputRanges],
			["TXCheckBox", "Quantise", "quantise"],
			["SynthOptionPopupPlusMinus", "Quant type", arrOptionData, 0],
			["TXMinMaxSliderSplit", "Quant steps", classData.specSteps, "steps", "stepsMin", "stepsMax"],
		]);
		//	initialise buffer to linear curve
		arrCurveValues = Array.newClear(128).seriesFill(0, 1/127);
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

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "In/Out & Quantise", {displayOption = "showInputOutput";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showInputOutput")],
			["Spacer", 3],
			["ActionButton", "Curve", {displayOption = "showCurve";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showCurve")],
			["DividingLine"],
			["SpacerLine", 6],
		];

		if (displayOption == "showInputOutput", {
			guiSpecArray = guiSpecArray ++[
				["TXRangeSlider", "Input range", \bipolar, "inputMin", "inputMax", nil,
					this.class.arrOutputRanges],
				["DividingLine"],
				["SpacerLine", 10],
				["TXRangeSlider", "Output range", \bipolar, "outputMin", "outputMax", nil, this.class.arrOutputRanges],
				["DividingLine"],
				["SpacerLine", 10],
				["TXCheckBox", "Quantise", "quantise"],
				["SpacerLine", 6],
				["SynthOptionPopupPlusMinus", "Quant type", arrOptionData, 0],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Quant steps", classData.specSteps, "steps", "stepsMin", "stepsMax"],
			];
		});
		if (displayOption == "showCurve", {
			guiSpecArray = guiSpecArray ++[
				["TXCurveDraw", "Warp curve", {arrCurveValues},
					{arg view; arrCurveValues = view.value; arrSlotData = view.arrSlotData;
						this.bufferStore(view.value);},
					{arrSlotData}, "Warp", nil, nil, nil, "gridRows", "gridCols", nil, nil, curveDataEvent],
				["SpacerLine", 2],
				["TXNumberPlusMinus", "Grid rows", ControlSpec(1, 128), "gridRows", {system.showView}],
				["TXNumberPlusMinus", "Grid columns", ControlSpec(1, 128), "gridCols", {system.showView}],
				["TXPresetPopup", "Grid presets", arrGridPresetNames, arrGridPresetActions, 182],
				["ActionButton", "Quantise to grid", {this.quantiseToGrid(quantizeRows: true, quantizeCols: true)}, 94],
				["ActionButton", "Quantise rows", {this.quantiseToGrid(quantizeRows: true, quantizeCols: false)}, 88],
				["ActionButton", "Quantise columns", {this.quantiseToGrid(quantizeRows: false, quantizeCols: true)}, 102],
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
		var maxVal = 128;
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

