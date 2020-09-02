// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFMPresets {

	classvar <arrFMPresets;

	*initClass{
		//
		// The following presets are based on the operator algorithms used in the Yamaha DX7
		//
		// 1 row per operator: modulation indices for operators 1-6 and output level
		// operators with level of 1 are carriers (labelled Car)
		//
		// Note - in each preset, feedback is set to 0.2 by default
		//
		arrFMPresets = [
			[	"[Algo 1]  2 carriers: op 1,3  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 1, 0, 0, 1], // op 3 Car
						[0, 0, 0, 0, 1, 0, 0], // op 4
						[0, 0, 0, 0, 0, 1, 0], // op 5
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 2]  2 carriers: op 1,3  feedback: op 2->2",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, fb, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 1, 0, 0, 1], // op 3 Car
						[0, 0, 0, 0, 1, 0, 0], // op 4
						[0, 0, 0, 0, 0, 1, 0], // op 5
						[0, 0, 0, 0, 0, 0, 0], // op 6
				]},
			],
			[	"[Algo 3]  2 carriers: op 1,4  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 1, 0, 0, 0], // op 3
						[0, 0, 0, 0, 1, 0, 1], // op 4 Car
						[0, 0, 0, 0, 0, 1, 0], // op 5
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 4]  2 carriers: op 1,4  feedback: op 4->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 1, 0, 0, 0, 0], // op 2
						[0, 0, 0, 0, 0, 0, 0], // op 3
						[0, 0, 0, 0, 1, 0, 1], // op 4 Car
						[0, 0, 0, 0, 0, 1, 0], // op 5
						[0, 0, 0, fb, 0, 0, 0], // op 6
				]},
			],
			[	"[Algo 5]  3 carriers: op 1,3,5  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 1, 0, 0, 1], // op 3 Car
						[0, 0, 0, 0, 0, 0, 0], // op 4
						[0, 0, 0, 0, 0, 1, 1], // op 5
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 6]  3 carriers: op 1,3,5  feedback: op 5->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 1, 0, 0, 1], // op 3 Car
						[0, 0, 0, 0, 0, 0, 0], // op 4
						[0, 0, 0, 0, 0, 1, 1], // op 5
						[0, 0, 0, 0, fb, 0, 0], // op 6
				]},
			],
			[	"[Algo 7]  2 carriers: op 1,3  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 1, 1, 0, 1], // op 3 Car
						[0, 0, 0, 0, 0, 0, 0], // op 4
						[0, 0, 0, 0, 0, 0, 0], // op 5
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 8]  2 carriers: op 1,3  feedback: op 4->4",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 1, 1, 0, 1], // op 3 Car
						[0, 0, 0, fb, 0, 0, 0], // op 4
						[0, 0, 0, 0, 0, 1, 0], // op 5
						[0, 0, 0, 0, 0, 0, 0], // op 6
				]},
			],
			[	"[Algo 9]  2 carriers: op 1,3  feedback: op 2->2",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, fb, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 1, 1, 0, 1], // op 3 Car
						[0, 0, 0, 0, 0, 0, 0], // op 4
						[0, 0, 0, 0, 0, 1, 0], // op 5
						[0, 0, 0, 0, 0, 0, 0], // op 6
				]},
			],
			[	"[Algo 10]  2 carriers: op 1,4  feedback: op 3->3",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 1, 0, 0, 0, 0], // op 2
						[0, 0, fb, 0, 0, 0, 0], // op 3 Car
						[0, 0, 0, 0, 1, 1, 1], // op 4
						[0, 0, 0, 0, 0, 0, 0], // op 5
						[0, 0, 0, 0, 0, 0, 0], // op 6
				]},
			],
			[	"[Algo 11]  2 carriers: op 1,3  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 1, 0, 0, 0, 0], // op 2
						[0, 0, 0, 0, 0, 0, 0], // op 3 Car
						[0, 0, 0, 0, 1, 1, 1], // op 4
						[0, 0, 0, 0, 0, 0, 0], // op 5
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 12]  2 carriers: op 1,3  feedback: op 2->2",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, fb, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 1, 1, 1, 1], // op 3 Car
						[0, 0, 0, 0, 0, 0, 0], // op 4
						[0, 0, 0, 0, 0, 0, 0], // op 5
						[0, 0, 0, 0, 0, 0, 0], // op 6
				]},
			],
			[	"[Algo 13]  2 carriers: op 1,3  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 1, 1, 1, 1], // op 3 Car
						[0, 0, 0, 0, 0, 0, 0], // op 4
						[0, 0, 0, 0, 0, 0, 0], // op 5
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 14]  2 carriers: op 1,3  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 1, 0, 0, 1], // op 3 Car
						[0, 0, 0, 0, 1, 1, 0], // op 4
						[0, 0, 0, 0, 0, 0, 0], // op 5
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 15]  2 carriers: op 1,3  feedback: op 2->2",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, fb, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 1, 0, 0, 1], // op 3 Car
						[0, 0, 0, 0, 1, 1, 0], // op 4
						[0, 0, 0, 0, 0, 0, 0], // op 5
						[0, 0, 0, 0, 0, 0, 0], // op 6
				]},
			],
			[	"[Algo 16]  1 carrier (op 1  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 1, 0, 1, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 1, 0, 0, 0], // op 3
						[0, 0, 0, 0, 0, 0, 0], // op 4
						[0, 0, 0, 0, 0, 1, 0], // op 5
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 17]  1 carrier (op 1  feedback: op 2->2",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 1, 0, 1, 0, 1], // op 1 Car
						[0, fb, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 1, 0, 0, 0], // op 3
						[0, 0, 0, 0, 0, 0, 0], // op 4
						[0, 0, 0, 0, 0, 1, 0], // op 5
						[0, 0, 0, 0, 0, 0, 0], // op 6
				]},
			],
			[	"[Algo 18]  1 carrier (op 1  feedback: op 3->3",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 1, 1, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 0], // op 2
						[0, 0, fb, 0, 0, 0, 0], // op 3
						[0, 0, 0, 0, 1, 0, 0], // op 4
						[0, 0, 0, 0, 0, 1, 0], // op 5
						[0, 0, 0, 0, 0, 0, 0], // op 6
				]},
			],
			[	"[Algo 19]  3 carriers: op 1,4,5  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 1, 0, 0, 0, 0], // op 2
						[0, 0, 0, 0, 0, 0, 0], // op 3
						[0, 0, 0, 0, 0, 0, 1], // op 4 Car
						[0, 0, 0, 0, 0, 1, 1], // op 5 Car
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 20]  3 carriers: op 1,2,4  feedback: op 3->3",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 0, 1, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 1], // op 2 Car
						[0, 0, fb, 0, 0, 0, 0], // op 3
						[0, 0, 0, 0, 1, 1, 1], // op 4 Car
						[0, 0, 0, 0, 0, 0, 0], // op 5
						[0, 0, 0, 0, 0, 0, 0], // op 6
				]},
			],
			[	"[Algo 21]  4 carriers: op 1,2,4,5  feedback: op 3->3",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 0, 1, 0, 0, 0, 1], // op 1 Car
						[0, 0, 1, 0, 0, 0, 1], // op 2 Car
						[0, 0, fb, 0, 0, 0, 0], // op 3
						[0, 0, 0, 0, 0, 1, 1], // op 4 Car
						[0, 0, 0, 0, 0, 1, 1], // op 5 Car
						[0, 0, 0, 0, 0, 0, 0], // op 6
				]},
			],
			[	"[Algo 22]  4 carriers: op 1,3,4,5  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 0], // op 2
						[0, 0, 0, 0, 0, 1, 1], // op 3 Car
						[0, 0, 0, 0, 0, 1, 1], // op 4 Car
						[0, 0, 0, 0, 0, 1, 1], // op 5 Car
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 23]  4 carriers: op 1,2,4,5  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 0, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 1, 0, 0, 0, 1], // op 2 Car
						[0, 0, 0, 0, 0, 0, 0], // op 3
						[0, 0, 0, 0, 0, 1, 1], // op 4 Car
						[0, 0, 0, 0, 0, 1, 1], // op 5 Car
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 24]  5 carriers: op 1,2,3,4,5  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 0, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 1], // op 2 Car
						[0, 0, 0, 0, 0, 1, 1], // op 3 Car
						[0, 0, 0, 0, 0, 1, 1], // op 4 Car
						[0, 0, 0, 0, 0, 1, 1], // op 5 Car
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 25]  5 carriers: op 1,2,3,4,5  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 0, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 1], // op 2 Car
						[0, 0, 0, 0, 0, 0, 1], // op 3 Car
						[0, 0, 0, 0, 0, 1, 1], // op 4 Car
						[0, 0, 0, 0, 0, 1, 1], // op 5 Car
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 26]  3 carriers: op 1,2,4  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 0, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 1, 0, 0, 0, 1], // op 2 Car
						[0, 0, 0, 0, 0, 0, 0], // op 3 Car
						[0, 0, 0, 0, 1, 1, 1], // op 4 Car
						[0, 0, 0, 0, 0, 0, 0], // op 5 Car
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 27]  3 carriers: op 1,2,4  feedback: op 3->3",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 0, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 1, 0, 0, 0, 1], // op 2 Car
						[0, 0, fb, 0, 0, 0, 0], // op 3 Car
						[0, 0, 0, 0, 1, 1, 1], // op 4 Car
						[0, 0, 0, 0, 0, 0, 0], // op 5 Car
						[0, 0, 0, 0, 0, 0, 0], // op 6
				]},
			],
			[	"[Algo 28]  3 carriers: op 1,3,6  feedback: op 5->5",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 1, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 0], // op 2 Car
						[0, 0, 0, 1, 0, 0, 1], // op 3 Car
						[0, 0, 0, 0, 1, 0, 0], // op 4 Car
						[0, 0, 0, 0, fb, 0, 0], // op 5 Car
						[0, 0, 0, 0, 0, 0, 1], // op 6
				]},
			],
			[	"[Algo 29]  4 carriers: op 1,2,3,5  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 0, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 1], // op 2 Car
						[0, 0, 0, 1, 0, 0, 1], // op 3 Car
						[0, 0, 0, 0, 0, 0, 0], // op 4 Car
						[0, 0, 0, 0, 0, 1, 1], // op 5 Car
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 30]  4 carriers: op 1,2,3,6  feedback: op 5->5",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 0, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 1], // op 2 Car
						[0, 0, 0, 1, 0, 0, 1], // op 3 Car
						[0, 0, 0, 0, 1, 0, 0], // op 4 Car
						[0, 0, 0, 0, fb, 0, 0], // op 5 Car
						[0, 0, 0, 0, 0, 0, 1], // op 6
				]},
			],
			[	"[Algo 31]  5 carriers: op 1,2,3,4,5  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 0, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 1], // op 2 Car
						[0, 0, 0, 0, 0, 0, 1], // op 3 Car
						[0, 0, 0, 0, 0, 0, 1], // op 4 Car
						[0, 0, 0, 0, 0, 1, 1], // op 5 Car
						[0, 0, 0, 0, 0, fb, 0], // op 6
				]},
			],
			[	"[Algo 32]  6 carriers: op 1,2,3,4,5,6  feedback: op 6->6",
				{ arg fb = 0.2;
					[
						//1, 2, 3, 4, 5, 6, level
						[0, 0, 0, 0, 0, 0, 1], // op 1 Car
						[0, 0, 0, 0, 0, 0, 1], // op 2 Car
						[0, 0, 0, 0, 0, 0, 1], // op 3 Car
						[0, 0, 0, 0, 0, 0, 1], // op 4 Car
						[0, 0, 0, 0, 0, 0, 1], // op 5 Car
						[0, 0, 0, 0, 0, fb, 1], // op 6 Car
				]},
			],
			[	"[Randomise]  connections: 7",
				{ arg fb = 0.2;
					this.getRandomArray(7, fb);
				},
			],
			[	"[Randomise]  connections: 8",
				{ arg fb = 0.2;
					this.getRandomArray(8, fb);
				},
			],
			[	"[Randomise]  connections: 9",
				{ arg fb = 0.2;
					this.getRandomArray(9, fb);
				},
			],
			[	"[Randomise]  connections: 10",
				{ arg fb = 0.2;
					this.getRandomArray(10, fb);
				},
			],
		];
	}
	*getRandomArray {arg connections = 8, fb = 0.2;
		var algoArr, arrOps, numCarriers, arrCarriers, arrModulators, remaining, tries;
		algoArr =  [ // empty array
			//1, 2, 3, 4, 5, 6, level
			[0, 0, 0, 0, 0, 0, 0], // op 1
			[0, 0, 0, 0, 0, 0, 0], // op 2
			[0, 0, 0, 0, 0, 0, 0], // op 3
			[0, 0, 0, 0, 0, 0, 0], // op 4
			[0, 0, 0, 0, 0, 0, 0], // op 5
			[0, 0, 0, 0, 0, 0, 0], // op 6
		];
		arrOps = [0, 1, 2, 3, 4, 5].scramble; // randomise order
		numCarriers = 1 + 6.rand; // 1-6 carriers
		arrCarriers = arrOps.keep(numCarriers);
		arrModulators = arrOps.keep(numCarriers - 6);
		// set carrier levels
		arrCarriers.do({arg item;
			if (arrCarriers.size == 1, {
				algoArr[item][6] = 1;
			}, {
				algoArr[item][6] = rrand(0.1, 1); // range 0.1 - 1
			});
		});
		// set modulator connections
		arrModulators.do({arg modIndex;
			var arrChoices = arrOps.select({ arg item, i; item != modIndex; });
			algoArr[arrChoices.choose][modIndex] = rrand(0.01, 1); // range 0.01 - 1
		});
		// set remaining connections
		remaining = connections - 6;
		tries = 0;
		while ( { (remaining > 0) and: {tries < 100}}, {
			var x = 6.rand;
			var y = 6.rand;
			if (algoArr[x][y] == 0, {
				if (x == y, { // assign feedback if self-modulation
					algoArr[x][y] = fb;
				}, {
					algoArr[x][y] = (0.1 + gauss(0.0, 0.3).abs).clip(0,1);
				});
				remaining = remaining - 1;
			});
			tries = tries + 1;
		});
		^algoArr;
	}

}