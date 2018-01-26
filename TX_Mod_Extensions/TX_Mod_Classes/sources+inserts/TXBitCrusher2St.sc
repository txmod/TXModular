// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXBitCrusher2St : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Bit Crusher St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Sample rate", 1, "modSamplerate", 0],
			["Bit size", 1, "modBitSize", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var controlSpecSample, controlSpecBit;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["inGain", 2, defLagTime],
			["outGain", 0.5, defLagTime],
			["samplerate", 0.5, defLagTime],
			["samplerateMin", 100, defLagTime],
			["samplerateMax", 22050, defLagTime],
			["bitSize", 8, defLagTime],
			["smoothing", 0.5, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modSamplerate", 0, defLagTime],
			["modBitSize", 0, defLagTime],
			["modSmoothing", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		controlSpecSample = ControlSpec.new(100, 22050, \exp );
		controlSpecBit = ControlSpec.new(1, 24, \exp, 1 );
		arrOptions = [0];
		arrOptionData = [
			[
				["Bit Crusher - uses Sample rate & Bit size",
					{arg input, samplerate, bitSize, smoothing;
						var downsamp, bitRedux;
						downsamp = Latch.ar(input, Impulse.ar(samplerate));
						bitRedux = downsamp.round(0.5 ** bitSize);
					}
				],
				["Decimator - uses Sample rate & Bit size",
					{arg input, samplerate, bitSize, smoothing;
						Decimator.ar(input, samplerate, bitSize);
					}
				],
				["Smooth Decimator - uses Sample rate & Smoothing",
					{arg input, samplerate, bitSize, smoothing;
						SmoothDecimator.ar(input, samplerate, smoothing);
					}
				],
			]
		];
		synthDefFunc = {
			arg in, out, inGain, outGain, samplerate, samplerateMin, samplerateMax, bitSize, smoothing,
			wetDryMix, modSamplerate=0, modBitSize=0, modSmoothing=0, modWetDryMix=0;
			var input, outSound, outFunction, samplerateCombined, bitSizeCombined,
			smoothingCombined, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in,2);
			samplerateCombined = ( (samplerateMax/samplerateMin) ** ((samplerate + modSamplerate).max(0.001).min(1)) )
			* samplerateMin;
			bitSizeCombined = controlSpecBit.map (
				(controlSpecBit.unmap(bitSize) + modBitSize).max(0).min(1)
			);
			smoothingCombined = (smoothing + modSmoothing).max(0).min(1);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);

			outFunction = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			outSound =  outFunction.value(inGain * input, samplerateCombined, bitSizeCombined,
				smoothingCombined);
			Out.ar(out, (startEnv * outGain * outSound * mixCombined) + (input * (1-mixCombined)));
		};
		guiSpecArray = [
			["SynthOptionPopupPlusMinus", "Process", arrOptionData, 0],
			["SpacerLine", 6],
			["EZslider", "In Gain", ControlSpec(0, 10), "inGain"],
			["DividingLine"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Sample rate", controlSpecSample, "samplerate", "samplerateMin", "samplerateMax"],
			["SpacerLine", 6],
			["EZslider", "Bit size", controlSpecBit, "bitSize"],
			["SpacerLine", 6],
			["EZslider", "Smoothing", ControlSpec(0, 1), "smoothing"],
			["DividingLine"],
			["SpacerLine", 6],
			["EZslider", "Out Gain", ControlSpec(0, 1), "outGain"],
			["SpacerLine", 6],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

