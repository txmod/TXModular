// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXInterferenceSt : TXModuleBase {

	classvar <>classData;


	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Interference St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Wave 1 Depth", 1, "modWave1Depth", 0],
			["Wave 1 Freq", 1, "modWave1Freq", 0],
			["Wave 2 Depth", 1, "modWave2Depth", 0],
			["Wave 2 Freq", 1, "modWave2Freq", 0],
			["Wave 3 Depth", 1, "modWave3Depth", 0],
			["Wave 3 Freq", 1, "modWave3Freq", 0],
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
		var controlSpecDepth, controlSpecFreq;
		controlSpecDepth = ControlSpec.new(0, 1);
		controlSpecFreq = ControlSpec.new(1, 1000, \exp );
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["outGain", 1, defLagTime],
			["wave1Depth", 0.1, defLagTime],
			["wave1DepthMin", 0, defLagTime],
			["wave1DepthMax", 1, defLagTime],
			["wave1Freq", controlSpecFreq.unmap(50), defLagTime],
			["wave1FreqMin", 1, defLagTime],
			["wave1FreqMax", 1000, defLagTime],
			["wave2Depth", 0.1, defLagTime],
			["wave2DepthMin", 0, defLagTime],
			["wave2DepthMax", 1, defLagTime],
			["wave2Freq", controlSpecFreq.unmap(10), defLagTime],
			["wave2FreqMin", 1, defLagTime],
			["wave2FreqMax", 1000, defLagTime],
			["wave3Depth", 0.1, defLagTime],
			["wave3DepthMin", 0, defLagTime],
			["wave3DepthMax", 1, defLagTime],
			["wave3Freq", controlSpecFreq.unmap(100), defLagTime],
			["wave3FreqMin", 1, defLagTime],
			["wave3FreqMax", 1000, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modWave1Depth", 0, defLagTime],
			["modWave1Freq", 0, defLagTime],
			["modWave2Depth", 0, defLagTime],
			["modWave2Freq", 0, defLagTime],
			["modWave3Depth", 0, defLagTime],
			["modWave3Freq", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [1, 3, 3];
		arrOptionData = [
			["Noise - stepped",
				{arg freq, amp; LFDNoise0.ar(freq, amp).range(0,1);}
			],
			["Noise - linear smoothing",
				{arg freq, amp; LFDNoise1.ar(freq, amp).range(0,1);}
			],
			["Noise - cubic smoothing",
				{arg freq, amp; LFDNoise3.ar(freq, amp).range(0,1);}
			],
			["Sine",
				{arg freq, amp; SinOsc.ar(freq, 0, amp).range(0,1);}
			],
			["Sawtooth",
				{arg freq, amp; Saw.ar(freq, amp).range(0,1);}
			],
			["Triangle",
				{arg freq, amp; LFTri.ar(freq, 0, amp).range(0,1);}
			],
			["Square",
				{arg freq, amp; Pulse.ar(freq, 0.5, amp).range(0,1);}
			],
			["Chaotic - Logistic map - stepped",
				{arg freq, amp;
					var r = LFDNoise3.ar((freq * 0.2).max(1), 0.2, 3.79);
					Logistic.ar(r, freq, mul: amp * 2, add: -1);
			}],
			["Chaotic - Logistic map - linear smoothing",
				{arg freq, amp;
					var r = LFDNoise3.ar((freq * 0.2).max(1), 0.2, 3.79);
					Ramp.ar(Logistic.ar(r, freq, mul: amp * 2, add: -1), freq.reciprocal);
			}],
			["Chaotic - Logistic map - exponential smoothing",
				{arg freq, amp;
					var r = LFDNoise3.ar((freq * 0.2).max(1), 0.2, 3.79);
					Lag.ar(Logistic.ar(r, freq, mul: amp * 2, add: -1), freq.reciprocal);
			}],
			["Chaotic - Standard map - stepped",
				{arg freq, amp;
					var r = LFDNoise3.ar((freq * 0.2).max(1), 1.5, 0.9);
					StandardN.ar(freq, r, mul: amp);
			}],
			["Chaotic - Standard map - linear smoothing",
				{arg freq, amp;
					var r = LFDNoise3.ar((freq * 0.2).max(1), 1.5, 0.9);
					StandardL.ar(freq, r, mul: amp);
			}],
			["Chaotic - Quadratic map - stepped",
				{arg freq, amp;
					var r = LFDNoise3.ar((freq * 0.05).max(1), 0.2, 3.6);
					QuadN.ar(freq, r.neg, r, 0, amp * 0.5, 0.5).clip(0,1);
				}
			],
			["Chaotic - Quadratic map - linear smoothing",
				{arg freq, amp;
					var r = LFDNoise3.ar((freq * 0.05).max(1), 0.2, 3.6);
					QuadL.ar(freq, r.neg, r, 0, amp * 0.45, 0.5).clip(0,1);
				}
			],
			["Chaotic - Quadratic map - cubic smoothing",
				{arg freq, amp;
					var r = LFDNoise3.ar((freq * 0.05).max(1), 0.2, 3.6);
					QuadC.ar(freq, r.neg, r, 0, amp * 0.45, 0.5).clip(0,1);
				}
			],
		] ! 3;
		synthDefFunc = {
			arg in, out, outGain,
			wave1Depth, wave1DepthMin, wave1DepthMax, wave1Freq, wave1FreqMin, wave1FreqMax,
			wave2Depth, wave2DepthMin, wave2DepthMax, wave2Freq, wave2FreqMin, wave2FreqMax,
			wave3Depth, wave3DepthMin, wave3DepthMax, wave3Freq, wave3FreqMin, wave3FreqMax,
			wetDryMix,
			modWave1Depth = 0, modWave1Freq = 0, modWave2Depth = 0, modWave2Freq = 0,
			modWave3Depth = 0, modWave3Freq = 0, modWetDryMix = 0;

			var input, outSound, wave3Out, wave1Out, wave2Out, wave3DepthSum, wave3FreqSum;
			var wave1DepthSum, wave1FreqSum, wave2DepthSum, wave2FreqSum, mixSum, depthScale;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(InFeedback.ar(in,2));
			wave1DepthSum = (wave1Depth + modWave1Depth).max(0).min(1).linlin(0, 1, wave1DepthMin, wave1DepthMax);
			wave1FreqSum = (wave1Freq + modWave1Freq).max(0).min(1).linexp(0, 1, wave1FreqMin, wave1FreqMax);
			wave2DepthSum = (wave2Depth + modWave2Depth).max(0).min(1).linlin(0, 1, wave2DepthMin, wave2DepthMax);
			wave2FreqSum = (wave2Freq + modWave2Freq).max(0).min(1).linexp(0, 1, wave2FreqMin, wave2FreqMax);
			wave3DepthSum = (wave3Depth + modWave3Depth).max(0).min(1).linlin(0, 1, wave3DepthMin, wave3DepthMax);
			wave3FreqSum = (wave3Freq + modWave3Freq).max(0).min(1).linexp(0, 1, wave3FreqMin, wave3FreqMax);
			mixSum = (wetDryMix + modWetDryMix).max(0).min(1);

			depthScale = 0.1 * (wave1DepthSum + wave2DepthSum + wave3DepthSum).max(1).reciprocal;
			wave1Out =  this.getSynthOption(0).value(wave1FreqSum, Lag.kr(wave1DepthSum));
			wave2Out =  this.getSynthOption(1).value(wave2FreqSum, Lag.kr(wave2DepthSum));
			wave3Out =  this.getSynthOption(2).value(wave3FreqSum, Lag.kr(wave3DepthSum));
			outSound =  DelayC.ar(input, 0.2, (SampleDur.ir * 4) + (depthScale * (wave1Out + wave2Out + wave3Out)));
			Out.ar(out, (startEnv * outGain * outSound * mixSum) + (input * (1-mixSum)));
		};
		guiSpecArray = [
			["SpacerLine", 2],
			["SynthOptionPopupPlusMinus", "Wave 1", arrOptionData, 0],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Wave 1 Depth", controlSpecDepth, "wave1Depth", "wave1DepthMin", "wave1DepthMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Wave 1 Freq", controlSpecFreq, "wave1Freq", "wave1FreqMin", "wave1FreqMax"],
			["DividingLine"],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Wave 2", arrOptionData, 1],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Wave 2 Depth", controlSpecDepth, "wave2Depth", "wave2DepthMin", "wave2DepthMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Wave 2 Freq", controlSpecFreq, "wave2Freq", "wave2FreqMin", "wave2FreqMax"],
			["DividingLine"],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Wave 3", arrOptionData, 2],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Wave 3 Depth", controlSpecDepth, "wave3Depth", "wave3DepthMin", "wave3DepthMax"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Wave 3 Freq", controlSpecFreq, "wave3Freq", "wave3FreqMin", "wave3FreqMax"],
			["DividingLine"],
			["SpacerLine", 4],
			["EZslider", "Out Gain", ControlSpec(0, 1), "outGain"],
			["SpacerLine", 2],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

