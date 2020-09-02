// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXLineWarp {		// LineWarp function module

	*initClass{
		//
		// set class specific variables
		//
	}

	*getArrLineWarps {   // FORMAT: ["name", warp, isInOut]
		^[
			["linear", 'linear', false],            // 0
			["exponential", 'exponential', false],  // 1
			["exp In-Out", 'exponential', true],  // 2
			["-exponential", 'exponential', false],  // 3
			["-exp In-Out", 'exponential', true],  // 4
			["sine", 'sine', false],
			["welch", 'welch', false],
			["welch In-Out", 'welch', true],
			// ["step", 'step'],
			["+10 ", 10, false],
			["+10  In-Out", 10, true],
			["+9 ", 9, false],
			["+9  In-Out", 9, true],
			["+8 ", 8, false],
			["+8  In-Out", 8, true],
			["+7 ", 7, false],
			["+7  In-Out", 7, true],
			["+6 ", 6, false],
			["+6  In-Out", 6, true],
			["+5 ", 5, false],
			["+5  In-Out", 5, true],
			["+4 ", 4, false],
			["+4  In-Out", 4, true],
			["+3 ", 3, false],
			["+3  In-Out", 3, true],
			["+2 ", 2, false],
			["+2  In-Out", 2, true],
			["+1 ", 1, false],
			["+1  In-Out", 1, true],
			["-1", -1, false],
			["-1  In-Out", -1, true],
			["-2 ", -2, false],
			["-2  In-Out", -2, true],
			["-3 ", -3, false],
			["-3  In-Out", -3, true],
			["-4 ", -4, false],
			["-4  In-Out", -4, true],
			["-5 ", -5, false],
			["-5  In-Out", -5, true],
			["-6 ", -6, false],
			["-6  In-Out", -6, true],
			["-7 ", -7, false],
			["-7  In-Out", -7, true],
			["-8 ", -8, false],
			["-8  In-Out", -8, true],
			["-9 ", -9, false],
			["-9  In-Out", -9, true],
			["-10 ", -10, false],
			["-10  In-Out", -10, true],
		];
	}

	*getLineWarpStrings {
		^this.getArrLineWarps.collect({arg item; item[0];});
	}

	*getLineWarpVal {arg inVal, index = 0;
		var outVal, holdVal, holdLineWarp;
		index = index.asInteger;
		case
		{index == 0} {
			// linear
			outVal = inVal;
		}
		{index == 1} {
			// exp - use 0.1 offset to prevent exp(0)
			outVal = inVal.linexp(0, 1, 0.01, 1.01) - 0.01;
		}
		{index == 2} {
			// exp in-out
			if (inVal < 0.5, {
				holdVal = inVal.linexp(0, 0.5, 0.01, 1.01) - 0.01;
				outVal = holdVal * 0.5;
			}, {
				holdVal = (1 - inVal).linexp(0, 0.5, 0.01, 1.01) - 0.01;
				outVal = 1 - (holdVal * 0.5);
			});
		}
		{index == 3} {
			// -exp
			holdVal = (1 - inVal).linexp(0, 1, 0.01, 1.01) - 0.01;
			outVal = 1 - holdVal;
		}
		{index == 4} {
			// -exp in-out
			if (inVal < 0.5, {
				holdVal = (1 - inVal).linexp(0.5, 1, 0.01, 1.01) - 0.01;
				outVal = 0.5 - (holdVal * 0.5);
			}, {
				holdVal = inVal.linexp(0.5, 1, 0.01, 1.01) - 0.01;
				outVal = (holdVal * 0.5) + 0.5;
			});
		}
		{index >= 5} {
			holdLineWarp = this.getArrLineWarps[index];
			// if type in-out
			if (holdLineWarp[2] == true, {
				if (inVal < 0.5, {
					holdVal = Env([0, 1], [1], holdLineWarp[1]).at(inVal * 2);
					outVal = holdVal * 0.5;
				}, {
					holdVal = Env([0, 1], [1], holdLineWarp[1]).at((1 - inVal) * 2);
					outVal = 1 - (holdVal * 0.5);
				});
			}, {
				outVal = Env([0, 1], [1], holdLineWarp[1]).at(inVal);
			});
		};
		^outVal;
	}

}
