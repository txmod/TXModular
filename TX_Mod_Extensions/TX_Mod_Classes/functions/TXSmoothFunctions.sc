// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXSmoothFunctions {		// Smoothing functions

	*initClass{
		//
		// set class specific variables
		//
	}

	//	N.B. DO NOT CHANGE THIS ARRAY EXCEPT ADDING NEW SMOOTH TYPES AT THE END

	*arrOptionData {
		^ [
			["None",
				{arg input, lagtime; input;}
			],
			["Linear - use time 1 for up and down smoothing",
				{arg input, lagtime; Ramp.kr(input, lagtime); }
			],
			["Exponential 1 - use time 1 for up and down smoothing",
				{arg input, lagtime; Lag.kr(input, lagtime); }
			],
			["Exponential 2 - use time 1 for up and down smoothing",
				{arg input, lagtime; Lag2.kr(input, lagtime); }
			],
			["Exponential 3 - use time 1 for up and down smoothing",
				{arg input, lagtime; Lag3.kr(input, lagtime); }
			],
			["Exponential 1 - use time 1 for up, time 2 for down smoothing",
				{arg input, lagtime, lagtime2; LagUD.kr(input, lagtime, lagtime2); }
			],
			["Exponential 2 - use time 1 for up, time 2 for down smoothing",
				{arg input, lagtime, lagtime2; Lag2UD.kr(input, lagtime, lagtime2); }
			],
			["Exponential 3 - use time 1 for up, time 2 for down smoothing",
				{arg input, lagtime, lagtime2; Lag3UD.kr(input, lagtime, lagtime2); }
			],

			// TESTING XXX problems using VarLag as smoother - removed for now

			// ["Sine - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 0, Env.shapeNumber(\sine)); }
			// ],
			// ["Welch - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 0, Env.shapeNumber(\welch)); }
			// ],
			// ["Squared - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 0, Env.shapeNumber(\squared)); }
			// ],
			// ["Cubed - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 0, Env.shapeNumber(\cubed)); }
			// ],
			// ["Curve 10 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 10, 5); }
			// ],
			// ["Curve 9 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 9, 5); }
			// ],
			// ["Curve 8 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 8, 5); }
			// ],
			// ["Curve 7 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 7, 5); }
			// ],
			// ["Curve 6 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 6, 5); }
			// ],
			// ["Curve 5 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 5, 5); }
			// ],
			// ["Curve 4 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 4, 5); }
			// ],
			// ["Curve 3 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 3, 5); }
			// ],
			// ["Curve 2 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 2, 5); }
			// ],
			// ["Curve 1 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 1, 5); }
			// ],
			// ["Curve 0 (linear) - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, 0, 5); }
			// ],
			// ["Curve -1 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, -1, 5); }
			// ],
			// ["Curve -2 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, -2, 5); }
			// ],
			// ["Curve -3 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, -3, 5); }
			// ],
			// ["Curve -4 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, -4, 5); }
			// ],
			// ["Curve -5 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, -5, 5); }
			// ],
			// ["Curve -6 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, -6, 5); }
			// ],
			// ["Curve -7 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, -7, 5); }
			// ],
			// ["Curve -8 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, -8, 5); }
			// ],
			// ["Curve -9 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, -9, 5); }
			// ],
			// ["Curve -10 - use time 1 for up and down smoothing",
			// 	{arg input, lagtime, lagtime2; VarLag.kr(input, lagtime, -10, 5); }
			// ],

		];
	}

}
