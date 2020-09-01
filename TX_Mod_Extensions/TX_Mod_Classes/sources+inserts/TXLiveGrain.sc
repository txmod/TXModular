// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXLiveGrain : TXModuleBase {

	classvar <>classData;

	var displayOption;
	var arrCurveValues;
	var arrSlotData, curveDataEvent;
	var arrGridPresetNames, arrGridPresetActions;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Live Grain";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Grain time ms", 1, "modDurTime", 0],
			["Grain density", 1, "modDensity", 0],
			["Grain pan", 1, "modGrainPan", 0],
			["Vary triggering", 1, "modRandTrig", 0],
			["Vary grain time", 1, "modRandDur", 0],
			["Vary panning", 1, "modRandPan", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0],
			["Ext Trigger", 1, "extTrigger", nil],
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.arrBufferSpecs = [["bufnumGrainEnv", 128, 1]];
		classData.timeSpec = ControlSpec(0.01, 20);
		classData.guiWidth = 550;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		curveDataEvent = ();
		displayOption = "showGrain";
		arrSynthArgSpecs = [
			["in", 0, 0],
			["extTrigger", 0, 0],
			["out", 0, 0],
			["bufnumGrainEnv", 0, \ir],
			["randTrig", 0.03, defLagTime],
			["randDur", 0.03, defLagTime],
			["randPan", 0.1, defLagTime],
			["durTime", ControlSpec(1, 500).unmap(50), defLagTime],
			["durTimeMin", 1, defLagTime],
			["durTimeMax", 500, defLagTime],
			["density", ControlSpec(0.1,10).unmap(1), defLagTime],
			["densityMin", 0.1, defLagTime],
			["densityMax", 10, defLagTime],
			["grainPan", 0.5, defLagTime],
			["grainPanMin", 0, defLagTime],
			["grainPanMax", 1, defLagTime],
			["level", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modRandTrig", 0, defLagTime],
			["modRandDur", 0, defLagTime],
			["modRandPan", 0, defLagTime],
			["modDurTime", 0, defLagTime],
			["modDensity", 0, defLagTime],
			["modGrainPan", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
			// N.B. the args below aren't used in the synthdef, just stored here for convenience
			["gridRows", 8],
			["gridCols", 8],
		];
		arrOptions = [0, 3, 0];
		arrOptionData = [
			[// 0 - Grain envelope
				// GrainIn.ar (numChannels: 1, trigger: 0, dur: 1, in, pan: 0, envbufnum: -1, maxGrains: 512, mul: 1, add: 0)
				["Custom envelope (default)", {
					arg outTrig, outDur, inSound, outPan, bufnumGrainEnv, maxGrains, level;
					GrainIn.ar (2, outTrig, outDur, inSound, outPan, bufnumGrainEnv, maxGrains, level);
				}],
				["Built in Hann envelope (custom env ignored)", {
					arg outTrig, outDur, inSound, outPan, bufnumGrainEnv, maxGrains, level;
					GrainIn.ar (2, outTrig, outDur, inSound, outPan, -1, maxGrains, level);
				}],
			],
			[// 1 - Maximum grains
				["8", 8],
				["16", 16],
				["32", 32],
				["64 (default)", 64],
				["128", 128],
				["256", 256],
				["512", 512],
			],
			[// 2 - Triggering
				["Internal (default), uses grain time,  density & vary triggering", 0],
				["External, uses side-chain control signal", 1],
			],
		];
		synthDefFunc = {
			arg in, extTrigger = 0, out, bufnumGrainEnv, randTrig, randDur, randPan, durTime, durTimeMin, durTimeMax,
			density, densityMin, densityMax, grainPan, grainPanMin, grainPanMax, level, wetDryMix,
			modRandTrig=0, modRandDur=0, modRandPan=0, modDurTime=0, modDensity=0, modGrainPan=0,
			modDelay=0, modAttack=0, modDecay=0, modSustain=0, modSustainTime=0, modRelease=0, modWetDryMix=0;
			var inSound, trigFunction, trigRate, outTrig, sDurTime, outDur;
			var outPan, sRandPan, sRandTrig, sRandDur, outDensity, grainFunc, maxGrains, outGrain, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			inSound = TXClean.ar(startEnv * InFeedback.ar(in,1));
			sDurTime = (durTimeMin + ((durTimeMax - durTimeMin) * (durTime + modDurTime))).max(1).min(20000) / 1000;
			sRandDur = (randDur + modRandDur).max(0).min(1);
			outDur = sDurTime * (2 ** (sRandDur * WhiteNoise.kr.range(-2,2)));
			sRandPan = (randPan + modRandPan).max(0).min(1);
			outPan = (sRandPan * WhiteNoise.kr.unipolar) + ((1 - sRandPan) * grainPan).linlin(0, 1, grainPanMin, grainPanMax);

			if (arrOptions[2] == 0, {
				// internal triggering
				outDensity = (density + modDensity).max(0).min(1).linlin(0, 1, densityMin, densityMax);
				sRandTrig = (randTrig + modRandTrig).max(0).min(1);
				trigRate = outDur.reciprocal * outDensity;
				outTrig = GaussTrig.ar(trigRate, sRandTrig * 0.99);
			}, {
				// external triggering
				outTrig = extTrigger;
			});

			grainFunc = this.getSynthOption(0);
			maxGrains = this.getSynthOption(1);
			outGrain = grainFunc.value(outTrig, outDur, inSound, outPan.madd(2, -1), bufnumGrainEnv, maxGrains, level);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			Out.ar(out, TXClean.ar(startEnv * outGrain * mixCombined) + (inSound * (1-mixCombined)));
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
			["SynthOptionPopupPlusMinus", "Max grains", arrOptionData, 1],
			["SynthOptionPopupPlusMinus", "Triggering", arrOptionData, 2],
			["TXMinMaxSliderSplit", "Grain time ms", ControlSpec(1, 20000), "durTime", "durTimeMin",  "durTimeMax"],
			["TXMinMaxSliderSplit", "Grain density", ControlSpec(0.1, 32), "density", "densityMin", "densityMax"],
			["EZslider", "Vary triggering", ControlSpec(0, 1), "randTrig"],
			["EZslider", "Vary grain time", ControlSpec(0, 1), "randDur"],
			["EZslider", "Vary panning", ControlSpec(0, 1), "randPan"],
			["SynthOptionPopupPlusMinus", "Grain Env", arrOptionData, 0],
			["TXMinMaxSliderSplit", "Grain pan", ControlSpec(0, 1), "grainPan", "grainPanMin", "grainPanMax"],
			["EZslider", "Level", ControlSpec(0, 2), "level", nil, 300],
			["EZslider", "Level", ControlSpec(0, 1), "level"],
			["WetDryMixSlider"],
		]);
		//	initialise buffer
		//arrCurveValues = Array.newClear(128).seriesFill(0, 1/127);
		arrCurveValues = Signal.sineFill(128, [1.0],[1.5pi]).collect({arg item, i; (item.value + 1) * 0.5;});
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make buffers, load the synthdef and create the synth
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

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Grain", {displayOption = "showGrain";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showGrain")],
			["Spacer", 3],
			["ActionButton", "Grain Env", {displayOption = "showGrainEnv";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showGrainEnv")],
			["DividingLine"],
			["SpacerLine", 1],
		];
		if (displayOption == "showGrain", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Max grains", arrOptionData, 1, 240],
				["SpacerLine", 6],
				["SynthOptionPopupPlusMinus", "Triggering", arrOptionData, 2],
				["SpacerLine", 2],
				["EZslider", "Vary triggering", ControlSpec(0, 1), "randTrig"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Grain density", ControlSpec(0.1, 32), "density", "densityMin", "densityMax"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Grain time ms", ControlSpec(1, 20000), "durTime", "durTimeMin",  "durTimeMax"],
				["SpacerLine", 6],
				["EZslider", "Vary grain time", ControlSpec(0, 1), "randDur"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Grain pan", ControlSpec(0, 1), "grainPan", "grainPanMin", "grainPanMax", nil,
					[["Pan Range Presets:", []], ["Full Stereo", [0, 1]], ["Half Stereo", [0.25, 0.75]], ["Mono", [0.5, 0.5]],
						["Left Half", [0, 0.5]], ["Left Bias", [0, 0.75]], ["Right Bias", [0.25, 1]], ["Right Half", [0.5, 1]]]
				],
				["SpacerLine", 6],
				["EZslider", "Vary panning", ControlSpec(0, 1), "randPan"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Level", ControlSpec(0, 1), "level"],
				["SpacerLine", 6],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showGrainEnv", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Grain Env", arrOptionData, 0],
				["SpacerLine", 6],
				["TXCurveDraw", "Custom env", {arrCurveValues},
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

