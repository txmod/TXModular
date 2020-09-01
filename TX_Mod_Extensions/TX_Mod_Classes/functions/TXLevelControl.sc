// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXLevelControl {	// Level control functions

	*initClass{
		//
		// set class specific variables
		//
	}

	*arrOptionData {
		^ [
			["None - level unchanged",
				{arg inSound; inSound; }
			],
			["Balance - match level to dry signal level",
				{arg inSound, inDry; Balance.ar(inSound, inDry); }
			],
			["Limiter - keep the signal level below 0db",
				{arg inSound; Limiter.ar(inSound, 0.988); }
			],
			["Normalize - force the signal level to just below 0db",
				{arg inSound; Normalizer.ar(inSound, 0.988); }
			],
			["Tanh Clipping - uses hyperbolic tangent of signal",
				{arg inSound; inSound.tanh; }
			],
		];
	}

	*arrDelayFunctions {
		// function to delay input if the matching leveling function uses delay
		^ [
			// for "None - level unchanged"
			{arg inSound; inSound; },
			// for "Balance - match level to dry signal level",
			{arg inSound; inSound; },
			// for "Limiter - keep the signal level below 0db",
			{arg inSound; DelayN.ar(inSound, 0.02, 0.02); },
			// for "Normalize - force the signal level to just below 0db",
			{arg inSound; DelayN.ar(inSound, 0.02, 0.02); },
			// for "Tanh Clipping - uses hyperbolic tangent of signal",
			{arg inSound; inSound; },
		];
	}

}

