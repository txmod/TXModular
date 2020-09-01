// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXWaveshaperX : TXModuleBase {

	classvar <>classData;

	var arrCurveValues;
	var arrCurve2Values;
	var arrSlotData, curve1DataEvent, curve2DataEvent;
	var displayOption;
	var displayCurveIndex;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "WaveshaperX";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Curve morph", 1, "modCurveMorph", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrBufferSpecs = [ ["bufnumShape1", 512, 1], ["bufnumShape2", 512, 1] ];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		curve1DataEvent = ();
		curve2DataEvent = ();
		extraLatency = 0.2;	// allow extra time when recreating
		displayOption = "showSettings";
		displayCurveIndex = 0;
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["bufnumShape1", 0, \ir],
			["bufnumShape2", 0, \ir],
			["curveMorph", 0, defLagTime],
			["curveMorphMin", 0, \ir],
			["curveMorphMax", 1, \ir],
			["inGain", 1, defLagTime],
			["outGain", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modCurveMorph", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
			["noiseMix", 10, \ir],
			["harmonicsMix", 100, \ir],
			["arrHarmonics", [1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0], \ir],
		];
		arrOptions = [0];
		arrOptionData = [
			[
				["Variable waveshaping - changes with amplitude",
					{arg inSound, inBufnum; Shaper.ar(inBufnum, inSound); }
				],
				["Constant waveshaping - no changes with amplitude",
					{arg inSound, inBufnum;
						Balance.ar(Shaper.ar(inBufnum, Normalizer.ar(inSound)), inSound);
					}
				],
			]
		];
		synthDefFunc = { arg in, out, bufnumShape1, bufnumShape2, curveMorph, curveMorphMin, curveMorphMax,
			inGain, outGain, wetDryMix,
			modCurveMorph=0.0, modWetDryMix=0.0;

			var input, outFunction, outDistorted, outDistorted2, outDistortedMix, outClean, mixCombined, holdCurveMorph;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in,1);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			outDistorted = outGain * outFunction.value(inGain * input, bufnumShape1);
			outDistorted2 = outGain * outFunction.value(inGain * input, bufnumShape2);
			holdCurveMorph = (curveMorphMin + ((curveMorphMax - curveMorphMin) * (curveMorph + modCurveMorph)));
			outDistortedMix = (outDistorted2 * holdCurveMorph) + (outDistorted * (1-holdCurveMorph));
			outDistortedMix = LeakDC.ar(outDistortedMix);
			Out.ar(out, startEnv * ((outDistortedMix * mixCombined) + (input * (1-mixCombined))));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["SynthOptionPopupPlusMinus", "Shaping type", arrOptionData, 0],
			["EZslider", "In Gain", ControlSpec(0, 10), "inGain"],
			["TXCurveDraw", "Curve 1", {arrCurveValues},
				{arg view; arrCurveValues = view.value; arrSlotData = view.arrSlotData;
					this.bufferStore(0, view.value);},
				{arrSlotData}, "Waveshaper"],
			["TXCurveDraw", "Curve 2", {arrCurve2Values},
				{arg view; arrCurve2Values = view.value; arrSlotData = view.arrSlotData;
					this.bufferStore(1, view.value);},
				{arrSlotData}, "Waveshaper"],
			["TXMinMaxSliderSplit", "Curve Morph", ControlSpec(0, 1), "curveMorph", "curveMorphMin",
				"curveMorphMax"],
			["EZslider", "Out Gain", ControlSpec(0, 1), "outGain"],
			["WetDryMixSlider"],
		]);
		//	initialise buffer to linear curve
		arrCurveValues = Array.newClear(257).seriesFill(0, 1/256);
		arrCurve2Values = Array.newClear(257).seriesFill(0, 1/256);
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
			this.bufferStore(0, arrCurveValues);
			this.bufferStore(1, arrCurve2Values);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		//	initialise slots to linear curves
		arrSlotData = arrCurveValues.deepCopy.dup(5);
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Curve 1", {displayCurveIndex = 0;
				this.buildGuiSpecArray; system.showView;}, 64,
			TXColor.white, this.getButtonColour(displayCurveIndex == 0)],
			["Spacer", 3],
			["ActionButton", "Curve 2", {displayCurveIndex = 1;
				this.buildGuiSpecArray; system.showView;}, 64,
			TXColor.white, this.getButtonColour(displayCurveIndex == 1)],
			["Spacer", 50],
			["ActionButton", "Settings", {displayOption = "showSettings";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showSettings")],
			["Spacer", 3],
			["ActionButton", "Harmonics", {displayOption = "showHarmonics";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showHarmonics")],
			["Spacer", 3],
			["ActionButton", "Noise", {displayOption = "showNoise";
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showNoise")],
			["SpacerLine", 1],
		];
		if (displayCurveIndex == 0, {
			guiSpecArray = guiSpecArray ++[
				["TXCurveDraw", "Curve 1", {arrCurveValues},
					{arg view; arrCurveValues = view.value; arrSlotData = view.arrSlotData;
						this.bufferStore(0, view.value);},
					{arrSlotData}, "Waveshaper", nil, nil, nil, nil, nil, nil, nil, curve1DataEvent]
			];
		}, {
			guiSpecArray = guiSpecArray ++[
				["TXCurveDraw", "Curve 2", {arrCurve2Values},
					{arg view; arrCurve2Values = view.value; arrSlotData = view.arrSlotData;
						this.bufferStore(1, view.value);},
					{arrSlotData}, "Waveshaper", nil, nil, nil, nil, nil, nil, nil, curve2DataEvent]
			];
		});
		if (displayOption == "showSettings", {
			guiSpecArray = guiSpecArray ++[
				["TXMinMaxSliderSplit", "Curve Morph", ControlSpec(0, 1), "curveMorph", "curveMorphMin",
					"curveMorphMax"],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Shaping type", arrOptionData, 0],
				["SpacerLine", 2],
				["EZslider", "In Gain", ControlSpec(0, 10), "inGain"],
				["SpacerLine", 2],
				["EZslider", "Out Gain", ControlSpec(0, 1), "outGain"],
				["SpacerLine", 2],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showHarmonics", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 1],
				["TXMultiSlider", "Harmonics", ControlSpec(0, 1), "arrHarmonics", 16, 0, 120],
				["NextLine"],
				["EZslider", "Harm mix", ControlSpec(0, 100), "harmonicsMix"],
				["NextLine"],
				["ActionButton", "Add harmonics to the curve", {this.addHarmonics}, 400],
			];
		});
		if (displayOption == "showNoise", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["EZslider", "Noise mix", ControlSpec(0, 100), "noiseMix"],
				["SpacerLine", 2],
				["ActionButton", "Add tapered noise to the curve", {this.addNoiseVarying}, 400],
				["SpacerLine", 2],
				["ActionButton", "Add untapered noise to the curve", {this.addNoise}, 400],
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

	bufferStore { arg argIndex, argArray;
		var holdSignal, holdArray;
		holdArray = argArray.deepCopy;
		// make array bipolar
		holdArray = (holdArray * 2) - 1;
		holdSignal = Signal.newFrom(holdArray);
		buffers.at(argIndex).sendCollection(holdSignal.asWavetableNoWrap);
	}

	addHarmonics {
		var harmonicsMix, harmonicsSignal, arrHarmonics, holdSignal;
		harmonicsMix = this.getSynthArgSpec("harmonicsMix") / 100;
		arrHarmonics = this.getSynthArgSpec("arrHarmonics");
		harmonicsSignal = (Signal.chebyFill(257, arrHarmonics) + 1) / 2;
		if (displayCurveIndex == 0, {
			holdSignal = Signal.newFrom(arrCurveValues).blend(harmonicsSignal, harmonicsMix);
			arrCurveValues = Array.newFrom(holdSignal);
			this.bufferStore(displayCurveIndex, arrCurveValues);
		}, {
			holdSignal = Signal.newFrom(arrCurve2Values).blend(harmonicsSignal, harmonicsMix);
			arrCurve2Values = Array.newFrom(holdSignal);
			this.bufferStore(displayCurveIndex, arrCurve2Values);
		});
		system.showView;
	}

	addNoise {
		var noiseMix, noiseSignal, holdSignal;
		noiseMix = this.getSynthArgSpec("noiseMix") / 100;
		noiseSignal = Signal.newFrom(Array.rand(257, 0.0, 1.0));
		if (displayCurveIndex == 0, {
			holdSignal = Signal.newFrom(arrCurveValues).blend(noiseSignal, noiseMix);
			arrCurveValues = Array.newFrom(holdSignal);
			this.bufferStore(displayCurveIndex, arrCurveValues);
		}, {
			holdSignal = Signal.newFrom(arrCurve2Values).blend(noiseSignal, noiseMix);
			arrCurve2Values = Array.newFrom(holdSignal);
			this.bufferStore(displayCurveIndex, arrCurve2Values);
		});
		system.showView;
	}

	addNoiseVarying {
		var noiseMix, noiseSignal, holdSignal, arrFullNoise, arrLinear, arrVaryNoise;
		noiseMix = this.getSynthArgSpec("noiseMix") / 100;

		arrFullNoise = Array.rand(257, 0.0, 1.0);
		arrLinear = Array.newClear(128).seriesFill(0, 1/127);
		arrVaryNoise = arrLinear.collect({arg item,i; var holdProportion;
			holdProportion = i/127;
			(item * (1 - holdProportion)) + (arrFullNoise[i] * holdProportion)
		});
		noiseSignal = Signal.newFrom((arrVaryNoise.copy.reverse.neg) ++ [0] ++ arrVaryNoise);
		noiseSignal = (noiseSignal + 1) / 2;	// adjust range
		if (displayCurveIndex == 0, {
			holdSignal = Signal.newFrom(arrCurveValues).blend(noiseSignal, noiseMix);
			arrCurveValues = Array.newFrom(holdSignal);
			this.bufferStore(displayCurveIndex, arrCurveValues);
		}, {
			holdSignal = Signal.newFrom(arrCurve2Values).blend(noiseSignal, noiseMix);
			arrCurve2Values = Array.newFrom(holdSignal);
			this.bufferStore(displayCurveIndex, arrCurve2Values);
		});
		system.showView;
	}

	extraSaveData { // override default method
		^[arrCurveValues, arrCurve2Values, arrSlotData];
	}

	loadExtraData {arg argData;  // override default method
		arrCurveValues = argData.at(0);
		arrCurve2Values = argData.at(1);
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// buffer store
			this.bufferStore(0, arrCurveValues);
			this.bufferStore(1, arrCurve2Values);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		arrSlotData = argData.at(2);
		curve1DataEvent = ();
		curve2DataEvent = ();

	}

}

