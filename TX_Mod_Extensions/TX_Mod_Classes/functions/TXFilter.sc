// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXFilter {		// Filter module

*initClass{
	//
	// set class specific variables
	//
}

*arrOptionData {
	^ [
		["No Filter",
			{arg inSound, inFreq; inSound; }
		],
		["Low Pass 12db ",
			{arg inSound, inFreq; LPF.ar(inSound, inFreq); }
		],
		["Low Pass 24db",
			{arg inSound, inFreq; LPF.ar(LPF.ar(inSound, inFreq), inFreq); }
		],
		["Resonant Low Pass 12db",
			{arg inSound, inFreq, inRes; RLPF.ar(inSound, inFreq, (1 - inRes)); }
		],
		["Resonant Low Pass 24db",
			{arg inSound, inFreq, inRes; RLPF.ar(RLPF.ar(inSound, inFreq, (1 - inRes)), inFreq, (1 - inRes)); }
		],
		["High Pass 12db",
			{arg inSound, inFreq; HPF.ar(inSound, inFreq); }
		],
		["High Pass 24db",
			{arg inSound, inFreq; HPF.ar(HPF.ar(inSound, inFreq), inFreq); }
		],
		["Resonant High Pass 12db",
			{arg inSound, inFreq, inRes; RHPF.ar(inSound, inFreq, (1 - inRes)); }
		],
		["Resonant High Pass 24db",
			{arg inSound, inFreq, inRes; RHPF.ar(RHPF.ar(inSound, inFreq, (1 - inRes)), inFreq, (1 - inRes)); }
		],
		["Resonant Band Pass 12db",
			{arg inSound, inFreq, inRes; 2* BPF.ar(inSound, inFreq, (1 - inRes)); }  // boost vol * 2
		],
		["Resonant Band Pass 24db",
			{arg inSound, inFreq, inRes;
				2* BPF.ar(BPF.ar(inSound, inFreq, (1 - inRes)), inFreq, (1 - inRes)); }  // boost vol * 2
		],
		// Resonz - resonant Band Pass filter with uniform amplitude
		["Resonant Band Pass - Uniform Amplitude",
			{arg inSound, inFreq, inRes; Resonz.ar(inSound, inFreq, (1 - inRes)); }
		],
		["Resonant Band Reject 12db",
			{arg inSound, inFreq, inRes; BRF.ar(inSound, inFreq, (1 - inRes)); }
		],
		["Resonant Band Reject 24db",
			{arg inSound, inFreq, inRes; BRF.ar(BRF.ar(inSound, inFreq, (1 - inRes)), inFreq, (1 - inRes)); }
		],

// next 2 commented out - too explosive
//		["One pole 6db",
//			{arg inSound, inFreq; OnePole.ar(inSound, (inFreq * 2) - 1); }
//		],
//		["One zero 6db",
//			{arg inSound, inFreq; OneZero.ar(inSound, (inFreq) - 0.5); }
//		],


		["IIRFilter - Resonant Low Pass 24db ",
			{arg inSound, inFreq, inRes, inSat; IIRFilter.ar(inSound, inFreq, inRes); }
		],
		["RLPFD - TB303 style Resonant Filter + Saturation",
			{arg inSound, inFreq, inRes, inSat; RLPFD.ar(inSound, inFreq, inRes, inSat); }
		],
		["Streson - String Resonant Filter",
			{arg inSound, inFreq, inRes, inSat; Streson.ar(inSound, inFreq.reciprocal, inRes); }
		],
		["MoogVCF - Moog style Resonant Low Pass",
			{arg inSound, inFreq, inRes; MoogVCF.ar(inSound, inFreq, inRes, 2); } // boost vol * 2
		],
		["MoogFF - Moog emulation, Resonant Low Pass",
			{arg inSound, inFreq, inRes; 2 * MoogFF.ar(inSound, inFreq, inRes * 4); }// boost vol * 2
		],
		["MoogLadder - Moog emulation, Resonant Low Pass",
			{arg inSound, inFreq, inRes; MoogLadder.ar(inSound, inFreq, inRes, 2); } // boost vol * 2
		],
		["BMoog LP - Moog style 24db Res. Low Pass + Satur.",
			{arg inSound, inFreq, inRes, inSat; BMoog.ar(inSound, inFreq, inRes, 0, inSat); }
		],
		["BMoog HP - Moog style 24db Res. High Pass + Satur.",
			{arg inSound, inFreq, inRes, inSat; BMoog.ar(inSound, inFreq, inRes, 1, inSat); }
		],
		["BMoog BP - Moog style 24db Res. Band Pass + Satur.",
			{arg inSound, inFreq, inRes, inSat; BMoog.ar(inSound, inFreq, inRes, 2, inSat); }
		],
		["Formlet - Resonant FOF-like Filter",
			{arg inSound, inFreq, inRes; Formlet.ar(inSound, inFreq, 0.05, 0.1 + (inRes * 2)); }
		],
		["One-Pole Low Pass 6db",
			{arg inSound, inFreq, inRes; OnePole.ar(inSound, exp(-2pi * (inFreq * SampleDur.ir))); }
		],
		["One-Pole High Pass 6db",
			{arg inSound, inFreq, inRes; (inSound - OnePole.ar(inSound, exp(-2pi * (inFreq * SampleDur.ir)))); }
		],
		["Resonant Low Pass 12db Biquad",
			{arg inSound, inFreq, inRes; BLowPass.ar(inSound, inFreq, (1 - inRes)); }
		],
		["Resonant Low Pass 24db Biquad",
			{arg inSound, inFreq, inRes; BLowPass4.ar(inSound, inFreq, (1 - inRes)); }
		],
		["Resonant High Pass 12db Biquad",
			{arg inSound, inFreq, inRes; BHiPass.ar(inSound, inFreq, (1 - inRes)); }
		],
		["Resonant High Pass 24db Biquad",
			{arg inSound, inFreq, inRes; BHiPass4.ar(inSound, inFreq, (1 - inRes)); }
		],
		["Resonant Band Pass Biquad",
			{arg inSound, inFreq, inRes; BBandPass.ar(inSound, inFreq, (1 - inRes)); }
		],
		["Resonant Band Reject Biquad",
			{arg inSound, inFreq, inRes; BBandStop.ar(inSound, inFreq, (1 - inRes)); }
		],
		["All Pass Biquad",
			{arg inSound, inFreq, inRes; BAllPass.ar(inSound, inFreq, (1 - inRes)); }
		],
	];
}

*arrFreqRanges {
	^ [
		["Frequency Range Presets: ", [40, 20000]],
		["Full Audio range 40 - 20k hz", [40, 20000]],
		["MIDI Note Range - 8.17 - 12543 hz", [0.midicps, 127.midicps]],
		["Filter Sweep Wide  150 - 8k hz", [150, 8000]],
		["Filter Sweep Low 150 - 1k hz", [150, 1000]],
		["Filter Sweep Mid 1k - 5k hz", [1000, 5000]],
		["Filter Sweep High 5k - 8k hz", [5000, 8000]],
		["EQ Sub-Bass 40 - 80 hz", [40, 80]],
		["EQ Bass 80 - 250 hz: ", [80, 250]],
		["EQ Low Mid 250 - 500 hz", [250, 500]],
		["EQ Mid 500 - 2k hz: ", [500, 2000]],
		["EQ Upper Mid 2k - 4k hz", [2000, 4000]],
		["EQ Presence 4k - 6k hz", [4000, 6000]],
		["EQ Treble 6k - 20k hz", [6000, 20000]],
		["EQ Brilliance 10k - 20k hz", [10000, 20000]],
	];
}

*filterFunction {
	^ {	arg input, argFilterProcessFunc, freq, freqMin, freqMax, res, resMin, resMax,
			sat, satMin, satMax, wetDryMix,
			modfreq, modres, modsat, modWetDryMix, levelControlFunc;

		var outFilter, sumfreq, sumres, sumsat, mixCombined;

		sumfreq = ( (freqMax/ freqMin) ** ((freq + modfreq).max(0.001).min(1)) ) * freqMin;
		sumres =  resMin + ( (resMax - resMin) * (res + modres).max(0).min(1) );
		sumsat =  satMin + ( (satMax - satMin) * (sat + modsat).max(0).min(1) );
		mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
		outFilter = argFilterProcessFunc.value(
			input,
			sumfreq,
			sumres,
			sumsat
		);
		if (levelControlFunc.isNil, { levelControlFunc = {arg in; in.tanh;} });

		(levelControlFunc.value(outFilter, input) * mixCombined) + (input * (1-mixCombined));
	}
}

}

