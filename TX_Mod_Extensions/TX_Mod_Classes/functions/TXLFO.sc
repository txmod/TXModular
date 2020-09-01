// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXLFO {

	*initClass{
		//
		// set class specific variables
		//
	}

	*arrOptionData {
		^[
			["Sine", {arg lfoFreq; SinOsc.kr(lfoFreq)}],
			["Square", {arg lfoFreq; LFPulse.kr(lfoFreq)}],
			["Triangular", {arg lfoFreq; LFTri.kr(lfoFreq)}],
			["Sawtooth", {arg lfoFreq; LFSaw.kr(lfoFreq)}],
			["Sawtooth reversed", {arg lfoFreq; (LFSaw.kr(lfoFreq, 0, -1))}],
			// OLD METHODS - these sometimes failed to start
			//		["Random - stepped", {arg lfoFreq; LFDNoise0.kr(lfoFreq)}],
			//		["Random - linear smoothing", {arg lfoFreq; LFDNoise1.kr(lfoFreq)}],
			//		["Random - quadratic smoothing", {arg lfoFreq; LFDNoise3.kr(lfoFreq)}],
			//		["Random - clipped", {arg lfoFreq; LFClipNoise.kr(lfoFreq)}],
			["Random values and timing - stepped",
				{arg lfoFreq; Demand.kr(Dust.kr(lfoFreq), 0, Dwhite(-1, 1, inf))}],
			["Random values and timing - linear smoothing",
				{arg lfoFreq; Ramp.kr(Demand.kr(Dust.kr(lfoFreq), 0, Dwhite(-1, 1, inf)),
					2 * lfoFreq.reciprocal)}],
			["Random values and timing - exponential smoothing",
				{arg lfoFreq; Lag.kr(Demand.kr(Impulse.kr(lfoFreq), 0, Dwhite(-1, 1, inf)),
					2 * lfoFreq.reciprocal)}],
			["Random values and timing - clipped",
				{arg lfoFreq; Demand.kr(Dust.kr(lfoFreq), 0, (Diwhite(0, 1, inf)*2)-1)}],
			["Random values - stepped",
				{arg lfoFreq; Demand.kr(Impulse.kr(lfoFreq), 0, Dwhite(-1, 1, inf))}],
			["Random values - linear smoothing",
				{arg lfoFreq; Ramp.kr(Demand.kr(Impulse.kr(lfoFreq), 0, Dwhite(-1, 1, inf)),
					lfoFreq.reciprocal);}],
			["Random values - exponential smoothing",
				{arg lfoFreq; Lag.kr(Demand.kr(Impulse.kr(lfoFreq), 0, Dwhite(-1, 1, inf)),
					lfoFreq.reciprocal);}],
			["Chaotic - Logistic map - stepped",
				{arg lfoFreq;
					var r = 3.79;
					Logistic.kr(r, lfoFreq.max(0.3), mul: 2, add: -1); // restrict minimum freq
			}],
			["Chaotic - Logistic map - linear smoothing",
				{arg lfoFreq;
					var r = 3.79;
					var freq = lfoFreq.max(0.3); // restrict minimum freq
					Ramp.kr(Logistic.kr(r, freq, mul: 2, add: -1), freq.reciprocal);
			}],
			["Chaotic - Logistic map - exponential smoothing",
				{arg lfoFreq;
					var r = 3.79;
					var freq = lfoFreq.max(0.3); // restrict minimum freq
					Lag.kr(Logistic.kr(r, freq, mul: 2, add: -1), freq.reciprocal);
			}],
			["Chaotic - Standard map - stepped",
				{arg lfoFreq;
					var r = LFDNoise3.kr((lfoFreq * 0.2).max(0.1), 1.5, 0.9);
					A2K.kr(StandardN.ar(lfoFreq, r));
			}],
			["Chaotic - Standard map - linear smoothing",
				{arg lfoFreq;
					var r = LFDNoise3.kr((lfoFreq * 0.2).max(0.1), 1.5, 0.9);
					A2K.kr(StandardL.ar(lfoFreq, r));
			}],
			["Chaotic - Quadratic map - stepped",
				{arg lfoFreq, amp;
					var r = 3.79;
					A2K.kr(QuadN.ar(lfoFreq, r.neg, r, 0, 0.1)).madd(2, -1);
				}
			],
			["Chaotic - Quadratic map - linear smoothing",
				{arg lfoFreq, amp;
					var r = 3.79;
					A2K.kr(QuadL.ar(lfoFreq, r.neg, r, 0, 0.1)).madd(2, -1);
				}
			],
			["Chaotic - Quadratic map - cubic smoothing",
				{arg lfoFreq, amp;
					var r = 3.79;
					A2K.kr(QuadC.ar(lfoFreq, r.neg, r, 0, 0.1)).madd(2, -1);
				}
			],
		];
	}

	*arrLFOFreqRanges {
		^ [
			["Frequency Range Presets: ", [0.01, 100]],
			["Full range 0.001 - 100 hz", [0.001, 100]],
			["Very Slow range 0.001 - 0.1 hz", [0.001, 0.1]],
			["Slow range 0.01 - 1 hz", [0.01, 1]],
			["Medium range 0.1 - 10 hz", [0.1, 10]],
			["Fast range 1 - 100 hz", [1, 100]],
		];
	}

	*arrLFOOutputRanges {
		^ [
			["Positive only: 0 to 1", {arg input; input.range(0, 1)}],
			["Positive & Negative: -1 to 1", {arg input; input.range(-1, 1)}],
			["Positive & Negative: -0.5 to 0.5", {arg input; input.range(-0.5, 0.5)}],
			["Negative only: -1 to 0", {arg input; input.range(-1, 0)}],
		];
	}

	*lfoFunction {
		^ {	arg argLFOFunction, rangeFunction, freq, freqMin, freqMax, modFreq;
			var outFreq;
			outFreq = ( (freqMax/freqMin) ** ((freq + modFreq).max(0.001).min(1)) ) * freqMin;
			// select function based on arrOptions
			rangeFunction.value(argLFOFunction.value(outFreq));
		}
	}

}

