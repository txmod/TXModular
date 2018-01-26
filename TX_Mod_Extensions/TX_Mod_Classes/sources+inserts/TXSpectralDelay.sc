// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSpectralDelay : TXModuleBase {

	/*
	NOTE - THIS MODULE SOMETIMES GLITCHES/CRASHES ON OPENING SAVED SYSTEMS
	SO DELAY WITH FEEDBACK & DECAY TIME REMOVED FOR NOW - SEEMS TO HELP
	LONG INITIAL ENVELOPE HIDES INITIAL GLITCHES
	LESS CONTROLS SO GUI SIMPLIFIED TO 1 PAGE - SEE COMMENTING OUT
	*/

	classvar <>classData;

	var arrCurveValues;
	var arrSlotData, curveDataEvent;
	var displayOption;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Spectral Delay";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Max Delay", 1, "modDelayTime", 0],
			//["Decay", 1, "modDecayTime", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrBufferSpecs = [ ["bufnumDelay1", 256, 1], ["bufnumDelay2", 256, 1] ];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		curveDataEvent = ();
		//	set  class specific instance variables
		extraLatency = 0.5;	// allow extra time when recreating
		displayOption = "showSettings";
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["bufnumDelay1", 0, \ir],
			["bufnumDelay2", 0, \ir],
			["delayTime", 1, defLagTime],
			["delayTimeMin", 0, defLagTime],
			["delayTimeMax", 1, defLagTime],
			// ["decayTime", 0.4, defLagTime],
			// ["decayTimeMin", 0, defLagTime],
			// ["decayTimeMax", 5, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modDelayTime", 0, defLagTime],
			// ["modDecayTime", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [0];
		arrOptionData = [
			[
				["Delay only",
					{arg mag, delay; mag + DelayN.kr(mag, 1, delay); }
				],
				["Delay with feedback",
					{arg mag, delay, decay; mag + CombN.kr(mag, 1, delay, decay); }
				],
			]
		];
		synthDefFunc = { arg in, out, bufnumDelay1, bufnumDelay2, delayTime, delayTimeMin, delayTimeMax,
			// decayTime, decayTimeMin, decayTimeMax,
			wetDryMix,
			modDelayTime=0.0,
			// modDecayTime=0.0,
			modWetDryMix=0.0;

			var input, chain, outChain, mixCombined, outSound;
			var holdDelayTime, holdDecayTime, delayFunction;
			var recip255 = 255.reciprocal;
			var startEnv = TXEnvPresets.startEnvFunc.value;
			var slowEnv = EnvGen.kr(Env.new([0, 0, 1], [0.1,0.3]), 1);

			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			holdDelayTime = (delayTime + modDelayTime).max(0).min(1)
				.linlin(0, 1, delayTimeMin, delayTimeMax);
			// holdDecayTime = (decayTime + modDecayTime).max(0).min(1)
			// .linlin(0, 1, decayTimeMin, decayTimeMax);
			delayFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			input = slowEnv * TXClean.ar(InFeedback.ar(in,1));
			chain = FFT(LocalBuf(1024), input);
			chain = chain.pvcollect(1024, {|mag, phase, index|
				var binDelay, delay, decay, holdMag;
				if (index > 255, {
					holdMag = 0;
				}, {
					binDelay = BufRd.kr(1, bufnumDelay1, index);
					delay = holdDelayTime * binDelay;
					//decay = 0.05 + holdDecayTime;
					//holdMag = delayFunction.value(mag, max(0.05, delay), decay);
					holdMag = delayFunction.value(mag, max(0.05, delay));
				});
				[holdMag, phase];
			}, frombin: 0, tobin: 255, zeroothers: 1);
			outChain = LeakDC.ar(TXClean.ar(IFFT(chain)).clip);
			outSound = (outChain * mixCombined) + (input * (1-mixCombined));
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			// Removed for now
			//["SynthOptionPopupPlusMinus", "Delay Type", arrOptionData, 0],
			["TXMinMaxSliderSplit", "Max Delay Time", ControlSpec(0, 1), "delayTime", "delayTimeMin",
				"delayTimeMax"],
			// Removed for now
			// ["TXMinMaxSliderSplit", "Decay", ControlSpec(0, 20), "decayTime", "decayTimeMin",
			// "decayTimeMax"],
			["TXCurveDraw", "Curve", {arrCurveValues},
				{arg view; arrCurveValues = view.value; arrSlotData = view.arrSlotData;
					this.bufferStore(0, view.value);},
				{arrSlotData}, "Curve", nil, nil, nil, nil, nil, nil, curveDataEvent],
			["WetDryMixSlider"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make buffers, load the synthdef and create the synth
		this.makeBuffersAndSynth(classData.arrBufferSpecs);
		//	initialise buffer to linear curve
		arrCurveValues = Array.newClear(256).seriesFill(0, 1/255);
		Routine.run {
			var holdModCondition;
			// add condition to load queue
			holdModCondition = system.holdLoadQueue.addCondition;
			// pause
			holdModCondition.wait;
			system.server.sync;
			// buffer store
			this.bufferStore(0, arrCurveValues);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		//	initialise slots to linear curves
		arrSlotData = arrCurveValues.deepCopy.dup(5);
	}

	buildGuiSpecArray {
		guiSpecArray = [
			// ["ActionButton", "Settings", {displayOption = "showSettings";
			// 	this.buildGuiSpecArray; system.showView;}, 130,
			// TXColor.white, this.getButtonColour(displayOption == "showSettings")],
			// ["Spacer", 3],
			// ["ActionButton", "Delay curve", {displayOption = "showDelayCurve";
			// 	this.buildGuiSpecArray; system.showView;}, 130,
			// TXColor.white, this.getButtonColour(displayOption == "showDelayCurve")],
			// ["DividingLine"],
			// ["SpacerLine", 6],
		];
		// if (displayOption == "showDelayCurve", {
			guiSpecArray = guiSpecArray ++[
				["TXCurveDraw", "Delay Curve", {arrCurveValues},
					{arg view; arrCurveValues = view.value; arrSlotData = view.arrSlotData;
						this.bufferStore(0, view.value);},
				{arrSlotData}, "Curve", nil, nil, nil, nil, nil, "Frequency", "Delay Time", curveDataEvent],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
			];
		// });
		// if (displayOption == "showSettings", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				// Removed Delay Type for now
				// ["SynthOptionPopupPlusMinus", "Delay Type", arrOptionData, 0],
				// ["NextLine"],
				// ["DividingLine"],
				// ["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Max Delay", ControlSpec(0, 1), "delayTime", "delayTimeMin",
					"delayTimeMax"],
				["SpacerLine", 6],
				// Removed for now
				// ["TXMinMaxSliderSplit", "Decay", ControlSpec(0, 20), "decayTime", "decayTimeMin",
				// "decayTimeMax"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 6],
				["WetDryMixSlider"],
			];
		// });
	}

	getButtonColour { arg colour2Boolean;
		if (colour2Boolean == true, {
			^TXColor.sysGuiCol4;
		},{
			^TXColor.sysGuiCol1;
		});
	}

	bufferStore { arg argIndex, argArrayVals;
		buffers.at(argIndex).sendCollection(argArrayVals);
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
			this.bufferStore(0, arrCurveValues);
			// remove condition from load queue
			system.holdLoadQueue.removeCondition(holdModCondition);
		};
		arrSlotData = argData.at(2);
	}

}

