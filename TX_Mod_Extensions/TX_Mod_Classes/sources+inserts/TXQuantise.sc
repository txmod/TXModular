// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXQuantise : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Quantise";
		classData.moduleRate = "control";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Quantise", 1, "modQuantise", 0],
			["Steps", 1, "modSteps", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.holdControlSpec = ControlSpec(2, 1024, step: 1);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	*arrRanges {
		^ [
			["Presets: ", [0, 1]],
			["Full range -1 to 1", [-1, 1]],
			["Half range -0.5 to 0.5", [-0.5, 0.5]],
			["Positive range 0 to 1", [0, 1]],
			["Negative range -1 to 0", [-1, 0]],
		];
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["quantise", 1, 0],
			["inputMin", 0, defLagTime],
			["inputMax", 1, defLagTime],
			["outputMin", 0, defLagTime],
			["outputMax", 1, defLagTime],
			["steps", ControlSpec(2, 128, step: 1).unmap(4), 0],
			["stepsMin", 2, 0],
			["stepsMax", 128, 0],
			["modQuantise", 0, 0],
			["modSteps", 0, 0],
		];
		synthDefFunc = { arg in, out, quantise, inputMin, inputMax, outputMin, outputMax, steps, stepsMin, stepsMax,
			modQuantise = 0, modSteps = 0;

			var inSignal, inNorm, roundVal, holdQuant, holdSteps, quantOn, outQuant, outSignal;

			inSignal = TXClean.kr(In.kr(in,1));
			// normalise
			inNorm = inSignal.linlin(inputMin, inputMax, 0, 1, clip: 'minmax');

			// quantise
			quantOn = (quantise + modQuantise).max(0).min(1);
			holdSteps = (steps + modSteps).max(0).min(1).linlin(0, 1, stepsMin, stepsMax).round;
			roundVal = quantOn * (holdSteps - 1).reciprocal;
			outQuant = inNorm.round(roundVal);

			// map to o/p range
			outSignal = outQuant.linlin(0, 1, outputMin, outputMax);

			Out.kr(out, TXClean.kr(outSignal));
		};
		guiSpecArray = [
			["SpacerLine", 4],
			["TXRangeSlider", "Input range", \bipolar, "inputMin", "inputMax", nil,
				this.class.arrRanges],
			["DividingLine"],
			["SpacerLine", 10],
			["TXRangeSlider", "Output range", \bipolar, "outputMin", "outputMax", nil, this.class.arrRanges],
			["DividingLine"],
			["SpacerLine", 10],
			["TXCheckBox", "Quantise", "quantise"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Steps", classData.holdControlSpec, "steps", "stepsMin", "stepsMax"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

