// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
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
//		["Random - stepped", {arg lfoFreq; LFNoise0.kr(lfoFreq)}],
//		["Random - smooth linear", {arg lfoFreq; LFNoise1.kr(lfoFreq)}],
//		["Random - smooth quadratic", {arg lfoFreq; LFNoise2.kr(lfoFreq)}],
//		["Random - clipped", {arg lfoFreq; LFClipNoise.kr(lfoFreq)}],
		["Random values and timing - stepped",
			{arg lfoFreq; Demand.kr(Dust.kr(lfoFreq), 0, Dwhite(-1, 1, inf))}],
		["Random values and timing- smooth linear",
			{arg lfoFreq; Ramp.kr(Demand.kr(Dust.kr(lfoFreq), 0, Dwhite(-1, 1, inf)), 2 * lfoFreq.reciprocal)}],
		["Random values and timing- smooth quadratic",
			{arg lfoFreq; Lag.kr(Demand.kr(Impulse.kr(lfoFreq), 0, Dwhite(-1, 1, inf)), 2 * lfoFreq.reciprocal)}],
		["Random values and timing - clipped",
			{arg lfoFreq; Demand.kr(Dust.kr(lfoFreq), 0, (Diwhite(0, 1, inf)*2)-1)}],
		["Random values - stepped",
			{arg lfoFreq; Demand.kr(Impulse.kr(lfoFreq), 0, Dwhite(-1, 1, inf))}],
		["Random values- smooth linear",
			{arg lfoFreq; Ramp.kr(Demand.kr(Impulse.kr(lfoFreq), 0, Dwhite(-1, 1, inf)), lfoFreq.reciprocal)}],
		["Random values- smooth quadratic",
			{arg lfoFreq; Lag.kr(Demand.kr(Impulse.kr(lfoFreq), 0, Dwhite(-1, 1, inf)), lfoFreq.reciprocal)}],
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

