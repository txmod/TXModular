// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXVocoderFX2 : TXModuleBase {		// Vocoder FX module

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "VocoderFX";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrAudSCInBusSpecs = [
			["Modulator", 1, "inmodulator"]
		];
		classData.arrCtlSCInBusSpecs = [
			["stretch", 1, "modStretch", 0],
			["shift", 1, "modShift", 0],
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
			["stretch", 0.5, defLagTime],
			["stretchMin", 0.25, defLagTime],
			["stretchMax", 4, defLagTime],
			["shift", 0.5, defLagTime],
			["shiftMin", -50,  defLagTime],
			["shiftMax", 50, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modStretch", 0, defLagTime],
			["modShift", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		// vocode 2 inputs
		synthDefFunc = { arg in, inmodulator, out, bufnumCarrier, bufnumModulator, stretch, stretchMin, stretchMax,
			shift = 0.5, shiftMin= -50, shiftMax= 50,  wetDryMix,
			modStretch = 0.0, modShift = 0.0, modWetDryMix=0;

			var input, inputMod, chain, fftCarrier, fftModulator, fftModulator2, sumStretch, sumShift, mixCombined, outSound;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			sumStretch = ( (stretchMax/ stretchMin) ** ((stretch + modStretch).max(0.001).min(1)) ) * stretchMin;
			sumShift =  shiftMin + ( (shiftMax - shiftMin) * (shift + modShift).max(0).min(1) );
			input = TXClean.ar(InFeedback.ar(in,1));
			inputMod = TXClean.ar(InFeedback.ar(inmodulator,1));
			fftCarrier = FFT(bufnumCarrier, input);
			fftModulator = FFT(bufnumModulator, inputMod);
			fftModulator2 = PV_BinShift(fftModulator, sumStretch, sumShift-1);
			chain = PV_MagMul(fftCarrier, fftModulator2);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outSound = (IFFT(chain) * mixCombined) + (input * (1-mixCombined));
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		guiSpecArray = [
			["TXMinMaxSliderSplit", "Stretch", ControlSpec(0.25, 4, \exp, 0, 1), "stretch", "stretchMin", "stretchMax"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Shift", ControlSpec(-50, 50, default: 0), "shift", "shiftMin", "shiftMax"],
			["SpacerLine", 4],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make buffers, load the synthdef and create the synth
		this.makeBuffersAndSynth(classData.arrBufferSpecs);
	}

}

