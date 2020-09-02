// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXNHHall : TXModuleBase {

	var displayOption;
	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "NHHall";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Pre-Delay", 1, "modPreDelay", 0],
			["Reverb Time", 1, "modReverbTime", 0],
			["Infinite hold", 1, "modInfinite", 0],
			["Low ratio", 1, "modLowRatio", 0],
			["High ratio", 1, "modHighRatio", 0],
			["Early diffusion", 1, "modEarlyDiffusion", 0],
			["Late diffusion", 1, "modLateDiffusion", 0],
			["Mod depth", 1, "modModulationDepth", 0],
			["Mod rate", 1, "modModulationRate", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrBufferSpecs = [
			["bufnumDelay", defSampleRate * 5, 1],
		];
		classData.freqSpec = ControlSpec(20, 20000, \exponential);
		classData.ratioSpec = ControlSpec(0.1, 10);
		classData.preDelaySpec = ControlSpec(0, 5);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		displayOption = "showMain";
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["bufnumDelay", 0, \ir],
			["preDelay", 0, defLagTime],
			["preDelayMin", 0, defLagTime],
			["preDelayMax", 1, defLagTime],
			["reverbTime", 0.47979, defLagTime],
			["reverbTimeMin", 0.1, defLagTime],
			["reverbTimeMax", 10.0, defLagTime],
			["infinite", 0, defLagTime],
			["lowFreq", 200, defLagTime],
			["lowRatio", ControlSpec(0.1, 2).unmap(1), defLagTime],
			["lowRatioMin", 0.1, defLagTime],
			["lowRatioMax", 2, defLagTime],
			["highFreq", 4000, defLagTime],
			["highRatio", ControlSpec(0.1, 2).unmap(1), defLagTime],
			["highRatioMin", 0.1, defLagTime],
			["highRatioMax", 2, defLagTime],
			["earlyDiffusion", 0.5, defLagTime],
			["earlyDiffusionMin", 0, defLagTime],
			["earlyDiffusionMax", 1, defLagTime],
			["lateDiffusion", 0.5, defLagTime],
			["lateDiffusionMin", 0, defLagTime],
			["lateDiffusionMax", 1, defLagTime],
			["modulationDepth", 0.2, defLagTime],
			["modulationDepthMin", 0, defLagTime],
			["modulationDepthMax", 1, defLagTime],
			["modulationRate", 0.3, defLagTime],
			["modulationRateMin", 0, defLagTime],
			["modulationRateMax", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modPreDelay", 0, defLagTime],
			["modReverbTime", 0, defLagTime],
			["modInfinite", 0, defLagTime],
			["modLowRatio", 0, defLagTime],
			["modHighRatio", 0, defLagTime],
			["modEarlyDiffusion", 0, defLagTime],
			["modLateDiffusion", 0, defLagTime],
			["modModulationDepth", 0, defLagTime],
			["modModulationRate", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		synthDefFunc = {
			arg in, out, bufnumDelay, preDelay, preDelayMin, preDelayMax, reverbTime, reverbTimeMin, reverbTimeMax,
			infinite, lowFreq, lowRatio, lowRatioMin, lowRatioMax,
			highFreq, highRatio, highRatioMin, highRatioMax, earlyDiffusion, earlyDiffusionMin, earlyDiffusionMax,
			lateDiffusion, lateDiffusionMin, lateDiffusionMax, modulationDepth, modulationDepthMin,
			modulationDepthMax, modulationRate, modulationRateMin,  modulationRateMax, wetDryMix,
			modPreDelay=0, modReverbTime=0, modInfinite=0, modLowRatio=0, modHighRatio=0,
			modEarlyDiffusion=0, modLateDiffusion=0, modModulationDepth=0, modModulationRate=0, modWetDryMix=0;

			var input, outHall, delayedHall, outSound, mixCombined;
			var sum_preDelay, sum_reverbTime, sumInfinite, sum_lowRatio, sum_highRatio;
			var sum_earlyDiffusion, sum_lateDiffusion, sum_modulationDepth, sum_modulationRate;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(InFeedback.ar(in, 1));
			sum_preDelay = (preDelay + modPreDelay).max(0).min(1)
				.linlin(0, 1, preDelayMin, preDelayMax);
			sum_preDelay = Lag2.kr(sum_preDelay, 1); // add lag
			sum_reverbTime = (reverbTime + modReverbTime).max(0).min(1)
				.linlin(0, 1, reverbTimeMin, reverbTimeMax);
			sumInfinite = (infinite + modInfinite).max(0).min(1).round * 100000;
			sum_lowRatio = (lowRatio + modLowRatio).max(0).min(1)
				.linlin(0, 1, lowRatioMin, lowRatioMax);
			sum_highRatio = (highRatio + modHighRatio).max(0).min(1)
				.linlin(0, 1, highRatioMin, highRatioMax);
			sum_earlyDiffusion = (earlyDiffusion + modEarlyDiffusion).max(0).min(1)
				.linlin(0, 1, earlyDiffusionMin, earlyDiffusionMax);
			sum_lateDiffusion = (lateDiffusion + modLateDiffusion).max(0).min(1)
				.linlin(0, 1, lateDiffusionMin, lateDiffusionMax);
			sum_modulationDepth = (modulationDepth + modModulationDepth).max(0).min(1)
				.linlin(0, 1, modulationDepthMin, modulationDepthMax);
			sum_modulationRate = (modulationRate + modModulationRate).max(0).min(1)
				.linlin(0, 1, modulationRateMin, modulationRateMax);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outHall = NHHall.ar([input, input], sum_reverbTime + sumInfinite, 1,
				lowFreq, sum_lowRatio, highFreq, sum_highRatio,  sum_earlyDiffusion, sum_lateDiffusion,
				sum_modulationDepth, sum_modulationRate);
			delayedHall = BufDelayC.ar(bufnumDelay, outHall[0], sum_preDelay);
			outSound = (delayedHall  * mixCombined) + (input * (1-mixCombined));
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TXMinMaxSliderSplit", "Pre-delay", classData.preDelaySpec, "preDelay",
				"preDelayMin", "preDelayMax"],
			["TXMinMaxSliderSplit", "Reverb time", ControlSpec(0.1, 60), "reverbTime",
				"reverbTimeMin", "reverbTimeMax"],
			["TXCheckBox", "Infinite hold", "infinite", nil, 150, 20],
			["WetDryMixSlider"],
			["TXRangeSlider", "Freq range", classData.freqSpec, "lowFreq", "highFreq"],
			["TXMinMaxSliderSplit", "lowRatio", classData.ratioSpec, "lowRatio",
				"lowRatioMin", "lowRatioMax"],
			["TXMinMaxSliderSplit", "highRatio", classData.ratioSpec, "highRatio",
				"highRatioMin", "highRatioMax"],
			["TXMinMaxSliderSplit", "Early diffusion", ControlSpec(0, 1), "earlyDiffusion",
				"earlyDiffusionMin", "earlyDiffusionMax"],
			["TXMinMaxSliderSplit", "Late diffusion", ControlSpec(0, 1), "lateDiffusion",
				"lateDiffusionMin", "lateDiffusionMax"],
			["TXMinMaxSliderSplit", "Mod depth", ControlSpec(0, 1), "modulationDepth",
				"modulationDepthMin", "modulationDepthMax"],
			["TXMinMaxSliderSplit", "Mod rate", ControlSpec(0, 1), "modulationRate",
				"modulationRateMin", "modulationRateMax"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make buffers, load the synthdef and create the synth
		this.makeBuffersAndSynth(classData.arrBufferSpecs);
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Main", {displayOption = "showMain";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showMain")],
			["Spacer", 3],
			["ActionButton", "Character", {displayOption = "showCharacter";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showCharacter")],
			["DividingLine"],
			["SpacerLine", 6],
		];

		if (displayOption == "showMain", {
			guiSpecArray = guiSpecArray ++[
				["TXMinMaxSliderSplit", "Pre-delay", classData.preDelaySpec, "preDelay",
					"preDelayMin", "preDelayMax"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Reverb time", ControlSpec(0.1, 60), "reverbTime",
					"reverbTimeMin", "reverbTimeMax"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXCheckBox", "Infinite hold", "infinite", nil, 150, 20],
				["DividingLine"],
				["SpacerLine", 6],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showCharacter", {
			guiSpecArray = guiSpecArray ++[
				["TXRangeSlider", "Freq range", classData.freqSpec, "lowFreq", "highFreq"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Low ratio", classData.ratioSpec, "lowRatio",
					"lowRatioMin", "lowRatioMax"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "High ratio", classData.ratioSpec, "highRatio",
					"highRatioMin", "highRatioMax"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Early diffusion", ControlSpec(0, 1), "earlyDiffusion",
					"earlyDiffusionMin", "earlyDiffusionMax"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Late diffusion", ControlSpec(0, 1), "lateDiffusion",
					"lateDiffusionMin", "lateDiffusionMax"],
				["DividingLine"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Mod depth", ControlSpec(0, 1), "modulationDepth",
					"modulationDepthMin", "modulationDepthMax"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Mod rate", ControlSpec(0, 1), "modulationRate",
					"modulationRateMin", "modulationRateMax"],
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

}

