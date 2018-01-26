// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXReverbA : TXModuleBase {

	/* 	this is a slightly altered version of AdCverb, created by Alberto de Campo:

	AdCVerb: Simple reverb class, based on MoorerLoyReverb as given in Pope, Sc1 Tutorial.
	input is filtered,
	dense reverb is done with a bank of comb filters with prime ratio delaytimes;
	hfDamping uses side CombL side effect for freq dependent decay.
	*/

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "ReverbA";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Pre-Delay", 1, "modDelay", 0],
			["Reverb Time", 1, "modReverbTime", 0],
			["Damping", 1, "modDamping", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["delay", 0, defLagTime],
			["delayMin", 0.01, defLagTime],
			["delayMax", 1, defLagTime],
			["reverbTime", 0.25, defLagTime],
			["reverbTimeMin", 0.1, defLagTime],
			["reverbTimeMax", 10.0, defLagTime],
			["damping", 0.1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modDelay", 0, defLagTime],
			["modReverbTime", 0, defLagTime],
			["modDamping", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		synthDefFunc = {
			arg in, out,
			delay=0.1, delayMin, delayMax, reverbTime, reverbTimeMin,
			reverbTimeMax, damping, wetDryMix,
			modDelay=0, modReverbTime=0, modDamping=0, modWetDryMix=0;

			var input, outSound, revTime, predelay, sumdamping, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = startEnv * TXClean.ar(InFeedback.ar(in,1));
			predelay = ((delayMax/delayMin) ** ((delay + modDelay).max(0.1).min(1)) ) * delayMin;
			revTime = reverbTimeMin + ( (reverbTimeMax-reverbTimeMin) * (reverbTime + modReverbTime).max(0).min(1) );
			sumdamping = (damping + modDamping).max(0).min(1);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);

			outSound = this.buildAdCverb(input, revTime, sumdamping, 2, predelay);

			outSound = (outSound  * mixCombined) + (input * (1-mixCombined));
			Out.ar(out, startEnv * TXClean.ar(outSound));
		};
		guiSpecArray = [
			["TXMinMaxSliderSplit", "Pre-Delay", ControlSpec.new(0.01, 1, \exp ), "delay", "delayMin", "delayMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Reverb time", ControlSpec.new(0.1, 10.0, \exp ), "reverbTime",
				"reverbTimeMin", "reverbTimeMax"],
			["SpacerLine", 2],
			["EZsliderUnmapped", "Damping", ControlSpec(0.001, 1), "damping"],
			["SpacerLine", 2],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	buildAdCverb { arg in, revTime = 3, hfDamping = 0.1, nOuts = 2, predelay = 0.02,
		numCombs = 8, numAllpasses = 4, inFilter = 0.6, combScale = 1, apScale = 1;

		var timeOneSample;	// used for comb average-filtering;
		var primeRange;

		var combTimes,	// Table of combtimes
		allpassTimes,		// delayTimes for allpasses
		combsOut, apDecay;

		timeOneSample = SampleDur.ir;

		combTimes = [
			0.0797949, 			// new prime Numbers
			0.060825,
			0.0475902,
			0.0854197,
			0.0486931,
			0.0654572,
			0.0717437,
			0.0826624,
			0.0707511,
			0.0579574,
			0.0634719,
			0.0662292
		];

		combTimes = combTimes.copyRange(0, numCombs - 1);
		// Initialize allpass delay times:
		primeRange = 250 div: numAllpasses;

		allpassTimes = ({
			{ |i| rrand(i + 1 * primeRange, i + 2 * primeRange).nthPrime } ! numAllpasses
		} ! nOuts) / 44000;

		// pre-delay reverb input.
		in = DelayN.ar(OnePole.ar(in, inFilter), 0.2, predelay);

		// Create an array of combs:
		if (numCombs > 0) {
			combsOut = CombL.ar(in, 0.2,
				(combTimes * combScale).round(timeOneSample)
				+ (timeOneSample * 0.5 * hfDamping),
				revTime
			).sum
		} { combsOut = 0 };

		// Put the output through two parallel chains of allpass delays
		apDecay = 1.min(revTime * 0.6);

		^allpassTimes.collect({ |timesCh| var out;
			out = combsOut + in;
			timesCh.do { |time| out = AllpassN.ar(out, 0.2, time * apScale, apDecay) };
			out;
		});
	}

}

