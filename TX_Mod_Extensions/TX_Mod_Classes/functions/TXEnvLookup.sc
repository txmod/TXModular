// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXEnvLookup {

	*arrSlopeOptionData {
		^ [
			["linear", 'linear'],
			["sine", 'sine'],
			["welch", 'welch'],
			["slope +10 ", 10],
			["slope +9 ", 9],
			["slope +8 ", 8],
			["slope +7 ", 7],
			["slope +6 ", 6],
			["slope +5 ", 5],
			["slope +4 ", 4],
			["slope +3 ", 3],
			["slope +2 ", 2],
			["slope +1 ", 1],
			["slope -1", -1],
			["slope -2 ", -2],
			["slope -3 ", -3],
			["slope -4 ", -4],
			["slope -5 ", -5],
			["slope -6 ", -6],
			["slope -7 ", -7],
			["slope -8 ", -8],
			["slope -9 ", -9],
			["slope -10 ", -10],
			["hold", 'hold'],
			["step", 'step'],
			["square", 'sqr'],
			["cube", 'cub'],
			//invalid		["exponential", 'exponential'],
		];
	}

	*arrDADSRSlopeOptionData {
		^ [
			["linear", 'linear'],
			["sine", 'sine'],
			["welch", 'welch'],
			["slope +10 ", 10],
			["slope +9 ", 9],
			["slope +8 ", 8],
			["slope +7 ", 7],
			["slope +6 ", 6],
			["slope +5 ", 5],
			["slope +4 ", 4],
			["slope +3 ", 3],
			["slope +2 ", 2],
			["slope +1 ", 1],
			["slope -1", -1],
			["slope -2 ", -2],
			["slope -3 ", -3],
			["slope -4 ", -4],
			["slope -5 ", -5],
			["slope -6 ", -6],
			["slope -7 ", -7],
			["slope -8 ", -8],
			["slope -9 ", -9],
			["slope -10 ", -10],
			["slope +10/-10", ['linear', 10, -10, 'linear', -10]],
			["slope +9/-9", ['linear', 9, -9, 'linear', -9]],
			["slope +8/-8", ['linear', 8, -8, 'linear', -8]],
			["slope +7/-7", ['linear', 7, -7, 'linear', -7]],
			["slope +6/-6", ['linear', 6, -6, 'linear', -6]],
			["slope +5/-5", ['linear', 5, -5, 'linear', -5]],
			["slope +4/-4", ['linear', 4, -4, 'linear', -4]],
			["slope +3/-3", ['linear', 3, -3, 'linear', -3]],
			["slope +2/-2", ['linear', 2, -2, 'linear', -2]],
			["slope +1/-1", ['linear', 1, -1, 'linear', -1]],
			["slope -1/+1", ['linear', -1, 1, 'linear', 1]],
			["slope -2/+2", ['linear', -2, 2, 'linear', 2]],
			["slope -3/+3", ['linear', -3, 3, 'linear', 3]],
			["slope -4/+4", ['linear', -4, 4, 'linear', 4]],
			["slope -5/+5", ['linear', -5, 5, 'linear', 5]],
			["slope -6/+6", ['linear', -6, 6, 'linear', 6]],
			["slope -7/+7", ['linear', -7, 7, 'linear', 7]],
			["slope -8/+8", ['linear', -8, 8, 'linear', 8]],
			["slope -9/+9", ['linear', -9, 9, 'linear', 9]],
			["slope -10/+10", ['linear', -10, 10, 'linear', 10]],
			["hold", 'hold'],
			["step", 'step'],
			["square", 'sqr'],
			["cube", 'cub'],
			["exponential", 'exponential'],
		];
	}

	*arrDADSSRSlopeOptionData {
		^ [
			["linear", 'linear'],
			["sine", 'sine'],
			["welch", 'welch'],
			["slope +10 ", 10],
			["slope +9 ", 9],
			["slope +8 ", 8],
			["slope +7 ", 7],
			["slope +6 ", 6],
			["slope +5 ", 5],
			["slope +4 ", 4],
			["slope +3 ", 3],
			["slope +2 ", 2],
			["slope +1 ", 1],
			["slope -1", -1],
			["slope -2 ", -2],
			["slope -3 ", -3],
			["slope -4 ", -4],
			["slope -5 ", -5],
			["slope -6 ", -6],
			["slope -7 ", -7],
			["slope -8 ", -8],
			["slope -9 ", -9],
			["slope -10 ", -10],
			["slope +10/-10", ['linear', 10, -10, -10, -10]],
			["slope +9/-9", ['linear', 9, -9, -9, -9]],
			["slope +8/-8", ['linear', 8, -8, -8, -8]],
			["slope +7/-7", ['linear', 7, -7, -7, -7]],
			["slope +6/-6", ['linear', 6, -6, -6, -6]],
			["slope +5/-5", ['linear', 5, -5, -5, -5]],
			["slope +4/-4", ['linear', 4, -4, -4, -4]],
			["slope +3/-3", ['linear', 3, -3, -3, -3]],
			["slope +2/-2", ['linear', 2, -2, -2, -2]],
			["slope +1/-1", ['linear', 1, -1, -1, -1]],
			["slope -1/+1", ['linear', -1, 1, 1, 1]],
			["slope -2/+2", ['linear', -2, 2, 2, 2]],
			["slope -3/+3", ['linear', -3, 3, 3, 3]],
			["slope -4/+4", ['linear', -4, 4, 4, 4]],
			["slope -5/+5", ['linear', -5, 5, 5, 5]],
			["slope -6/+6", ['linear', -6, 6, 6, 6]],
			["slope -7/+7", ['linear', -7, 7, 7, 7]],
			["slope -8/+8", ['linear', -8, 8, 8, 8]],
			["slope -9/+9", ['linear', -9, 9, 9, 9]],
			["slope -10/+10", ['linear', -10, 10, 10, 10]],
			["hold", 'hold'],
			["step", 'step'],
			["square", 'sqr'],
			["cube", 'cub'],
			["exponential", 'exponential'],
		];
	}

	*arrDADSRSustainOptionData {  // returns an envelope
		^ [
			["Sustain until released",
				{arg del, att, dec, sus, sustime, rel, envCurve;
					 // + 0.001 allows for exp curve
					Env.new([0, 0, 1, sus, 0] + 0.001, [del, att, dec, rel], envCurve, 3);
				}
			],
			["Fixed length",
				{arg del, att, dec, sus, sustime, rel, envCurve;
					 // + 0.001 allows for exp curve
					Env.new([0, 0, 1, sus, sus, 0] + 0.001, [del, att, dec, sustime, rel], envCurve, nil);
				}
			]
		];
	}

	*arrDADSSRSustainOptionData {  // returns an envelope
		^ [
			["Sustain until released",
				{arg del, att, dec, sus, sus2, sustime, rel, envCurve;
					 // + 0.001 allows for exp curve
					Env.new([0, 0, 1, sus, sus2, 0] + 0.001, [del, att, dec, sustime, rel], envCurve, 4);
				}
			],
			["Fixed length",
				{arg del, att, dec, sus, sus2, sustime, rel, envCurve;
					 // + 0.001 allows for exp curve
					Env.new([0, 0, 1, sus, sus2, 0] + 0.001, [del, att, dec, sustime, rel], envCurve, nil);
				}
			]
		];
	}

}


