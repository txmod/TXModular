// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXVocoder2 : TXModuleBase {		// Vocoder module

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Vocoder";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrAudSCInBusSpecs = [
			["Modulator", 1, "inmodulator"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrBufferSpecs = [ ["bufnumCarrier", 2048,1],  ["bufnumModulator", 2048,1] ];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		arrSynthArgSpecs = [
			["in", 0, 0],
			["inmodulator", 0, 0],
			["out", 0, 0],
			["bufnumCarrier", 0, 0],
			["bufnumModulator", 0, 0],
			["wetDryMix", 1.0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		// vocode 2 inputs
		synthDefFunc = { arg in, inmodulator, out, bufnumCarrier, bufnumModulator, wetDryMix, modWetDryMix=0;
			var input, inputMod, chain, fftCarrier, fftModulator, mixCombined, outSound;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(InFeedback.ar(in,1));
			inputMod = TXClean.ar(InFeedback.ar(inmodulator,1));
			fftCarrier = FFT(bufnumCarrier, input);
			fftModulator = FFT(bufnumModulator, inputMod);
			chain = PV_MagMul(fftCarrier,fftModulator);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outSound = (IFFT(chain) * mixCombined) + (input * (1-mixCombined));
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		guiSpecArray = [
			["WetDryMixSlider"]
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make buffers, load the synthdef and create the synth
		this.makeBuffersAndSynth(classData.arrBufferSpecs);
	}

}

