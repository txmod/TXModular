// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

// NOTE - Sung (SATB) formant data taken from the SuperCollider Quark: Vowel (2010 - 2012, Florian Grond and Till Bovermann)
// which used formant data taken from the Csound manual:
// http://ecmc.rochester.edu/onlinedocs/Csound/Appendices/table3.html

TXVowelMorphSt : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var	noteListTextView;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.guiWidth = 570;
		classData.arrInstances = [];
		classData.defaultName = "Vowel Morph St";
		classData.moduleRate = "audio";
		classData.moduleType = "source";
		classData.arrAudSCInBusSpecs = [
			["External Inputs L+R", 2, "extInput"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Frequency", 1, "modFreq", 0],
			["Note select", 1, "modFreqSelector", 0],
			["Beats frequency", 1, "modBeatsFreq", 0],
			["Smoothtime 1", 1, "modLag", 0],
			["Smoothtime 2", 1, "modLag2", 0],
			["Morph X", 1, "modSliderXVal", 0],
			["Morph Y", 1, "modSliderYVal", 0],
			["Out Level", 1, "modOutLevel", 0]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.lagSpec = ControlSpec(0.01, 30, \exp, 0, 1, units: " secs");
		classData.freqSpec = ControlSpec(30, 10000, 'exp');
		classData.bandwidthSpec = ControlSpec(5, 1000, 'exp');
		classData.gainSpec = ControlSpec(0, 1);
		this.initLib;
	}

	*initLib{
	// this method is evaluated the first time formLib is used
		classData.formLib = Library.new;
		classData.formLib
			.put( 'beat', 'man', 'freq', [ 270, 2300, 3000, 3000, 3500 ])
			.put( 'beat', 'man', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'beat', 'man', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'bit', 'man', 'freq', [ 400, 2000, 2550, 3000, 3500 ])
			.put( 'bit', 'man', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'bit', 'man', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'bet', 'man', 'freq', [ 530, 1850, 2500, 3000, 3500 ])
			.put( 'bet', 'man', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'bet', 'man', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'bat', 'man', 'freq', [ 660, 1700, 2400, 3000, 3500 ])
			.put( 'bat', 'man', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'bat', 'man', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'part', 'man', 'freq', [ 730, 1100, 2450, 3000, 3500 ])
			.put( 'part', 'man', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'part', 'man', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'pot', 'man', 'freq', [ 570, 850, 2400, 3000, 3500 ])
			.put( 'pot', 'man', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'pot', 'man', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'boot', 'man', 'freq', [ 440, 1000, 2250, 3000, 3500 ])
			.put( 'boot', 'man', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'boot', 'man', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'book', 'man', 'freq', [ 300, 850, 2250, 3000, 3500 ])
			.put( 'book', 'man', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'book', 'man', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'but', 'man', 'freq', [ 640, 1200, 2400, 3000, 3500 ])
			.put( 'but', 'man', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'but', 'man', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'pert', 'man', 'freq', [ 490, 1350, 1700, 3000, 3500 ])
			.put( 'pert', 'man', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'pert', 'man', 'bw',   [ 50, 60, 170, 180, 200 ])


			.put( 'beat', 'woman', 'freq', [ 300, 2800, 3300, 3000, 3500 ])
			.put( 'beat', 'woman', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'beat', 'woman', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'bit', 'woman', 'freq', [ 430, 2500, 3100, 3000, 3500 ])
			.put( 'bit', 'woman', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'bit', 'woman', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'bet', 'woman', 'freq', [ 600, 2350, 3000, 3000, 3500 ])
			.put( 'bet', 'woman', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'bet', 'woman', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'bat', 'woman', 'freq', [ 860, 2050, 2850, 3000, 3500 ])
			.put( 'bat', 'woman', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'bat', 'woman', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'part', 'woman', 'freq', [ 850, 1200, 2800, 3000, 3500 ])
			.put( 'part', 'woman', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'part', 'woman', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'pot', 'woman', 'freq', [ 590, 900, 2700, 3000, 3500 ])
			.put( 'pot', 'woman', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'pot', 'woman', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'boot', 'woman', 'freq', [ 470, 1150, 2700, 3000, 3500 ])
			.put( 'boot', 'woman', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'boot', 'woman', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'book', 'woman', 'freq', [ 370, 950, 2650, 3000, 3500 ])
			.put( 'book', 'woman', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'book', 'woman', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'but', 'woman', 'freq', [ 760, 1400, 2800, 3000, 3500 ])
			.put( 'but', 'woman', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'but', 'woman', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'pert', 'woman', 'freq', [ 500, 1650, 1950, 3000, 3500 ])
			.put( 'pert', 'woman', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'pert', 'woman', 'bw',   [ 50, 60, 170, 180, 200 ])


			.put( 'beat', 'child', 'freq', [ 370, 3200, 3700, 3000, 3500 ])
			.put( 'beat', 'child', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'beat', 'child', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'bit', 'child', 'freq', [ 530, 2750, 3600, 3000, 3500 ])
			.put( 'bit', 'child', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'bit', 'child', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'bet', 'child', 'freq', [ 700, 2600, 3550, 3000, 3500 ])
			.put( 'bet', 'child', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'bet', 'child', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'bat', 'child', 'freq', [ 1000, 2300, 3300, 3000, 3500 ])
			.put( 'bat', 'child', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'bat', 'child', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'part', 'child', 'freq', [ 1030, 1350, 3200, 3000, 3500 ])
			.put( 'part', 'child', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'part', 'child', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'pot', 'child', 'freq', [ 680, 1050, 3200, 3000, 3500 ])
			.put( 'pot', 'child', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'pot', 'child', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'boot', 'child', 'freq', [ 560, 1400, 3300, 3000, 3500 ])
			.put( 'boot', 'child', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'boot', 'child', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'book', 'child', 'freq', [ 430, 1150, 3250, 3000, 3500 ])
			.put( 'book', 'child', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'book', 'child', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'but', 'child', 'freq', [ 850, 1600, 3350, 3000, 3500 ])
			.put( 'but', 'child', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'but', 'child', 'bw',   [ 50, 60, 170, 180, 200 ])

			.put( 'pert', 'child', 'freq', [ 560, 1650, 2150, 3000, 3500 ])
			.put( 'pert', 'child', 'db',   [ 0, -6, -12, -inf, -inf ])
			.put( 'pert', 'child', 'bw',   [ 50, 60, 170, 180, 200 ])


			.put( 'a', 'soprano', 'freq',[ 800, 1150, 2900, 3900, 4950 ])
			.put( 'a', 'soprano', 'db', [ 0, -6, -32, -20, -50 ])
			.put( 'a', 'soprano', 'bw',	 [ 80, 90, 120, 130, 140 ])

			.put( 'e', 'soprano', 'freq',[ 350, 2000, 2800, 3600, 4950 ])
			.put( 'e', 'soprano', 'db', [ 0, -20, -15, -40, -56 ])
			.put( 'e', 'soprano', 'bw',	 [ 60, 100, 120, 150, 200 ])

			.put( 'i', 'soprano', 'freq',[270, 2140, 2950, 3900, 4950])
			.put( 'i', 'soprano', 'db', [0, -12, -26, -26, -44])
			.put( 'i', 'soprano', 'bw',	 [60, 90, 100, 120, 120])

			.put( 'o', 'soprano', 'freq',[450, 800, 2830, 3800, 4950])
			.put( 'o', 'soprano', 'db', [0, -11, -22, -22, -50])
			.put( 'o', 'soprano', 'bw',	 [70, 80, 100, 130, 135])

			.put( 'u', 'soprano', 'freq',[325, 700, 2700, 3800, 4950])
			.put( 'u', 'soprano', 'db', [0, -16, -35, -40, -60])
			.put( 'u', 'soprano', 'bw',	 [50, 60, 170, 180, 200])


			.put( 'a', 'alto', 'freq',	[800, 1150, 2800, 3500, 4950])
			.put( 'a', 'alto', 'db',	[0, -4, -20, -36, -60])
			.put( 'a', 'alto', 'bw',	[80, 90, 120, 130, 140])

			.put( 'e', 'alto', 'freq',	[400, 1600, 2700, 3300, 4950])
			.put( 'e', 'alto', 'db',	[0, -24, -30, -35, -60])
			.put( 'e', 'alto', 'bw',	[60, 80, 120, 150, 200])

			.put( 'i', 'alto', 'freq',	[350, 1700, 2700, 3700, 4950])
			.put( 'i', 'alto', 'db',	[0, -20, -30, -36, -60])
			.put( 'i', 'alto', 'bw',	[50, 100, 120, 150, 200])

			.put( 'o', 'alto', 'freq',	[450, 800, 2830, 3500, 4950])
			.put( 'o', 'alto', 'db',	[0, -9, -16, -28, -55])
			.put( 'o', 'alto', 'bw',	[70, 80, 100, 130, 135])

			.put( 'u', 'alto', 'freq',	[325, 700, 2530, 3500, 4950])
			.put( 'u', 'alto', 'db',	[0, -12, -30, -40, -64])
			.put( 'u', 'alto', 'bw',	[50, 60, 170, 180, 200])


			.put( 'a', 'tenor', 'freq',	[650, 1080, 2650, 2900, 3250])
			.put( 'a', 'tenor', 'db',	[0, -6, -7, -8, -22])
			.put( 'a', 'tenor', 'bw',	[80, 90, 120, 130, 140])

			.put( 'e', 'tenor', 'freq',	[400, 1700, 2600, 3200, 3580])
			.put( 'e', 'tenor', 'db',	[0, -14, -12, -14, -20])
			.put( 'e', 'tenor', 'bw',	[70, 80, 100, 120, 120])

			.put( 'i', 'tenor', 'freq',	[290, 1870, 2800, 3250, 3540])
			.put( 'i', 'tenor', 'db',	[0, -15, -18, -20, -30])
			.put( 'i', 'tenor', 'bw',	[40, 90, 100, 120, 120])

			.put( 'o', 'tenor', 'freq',	[400, 800, 2600, 2800, 3000])
			.put( 'o', 'tenor', 'db',	[0, -10, -12, -12, -26])
			.put( 'o', 'tenor', 'bw',	[40, 80, 100, 120, 120])

			.put( 'u', 'tenor', 'freq',	[350, 600, 2700, 2900, 3300])
			.put( 'u', 'tenor', 'db',	[0, -20, -17, -14, -26])
			.put( 'u', 'tenor', 'bw',	[40, 60, 100, 120, 120])


			.put( 'a', 'bass', 'freq',	[600, 1040, 2250, 2450, 2750])
			.put( 'a', 'bass', 'db',	[0, -7, -9, -9, -20])
			.put( 'a', 'bass', 'bw',	[60, 70, 110, 120, 130])

			.put( 'e', 'bass', 'freq',	[400, 1620, 2400, 2800, 3100])
			.put( 'e', 'bass', 'db',	[0, -12, -9, -12, -18])
			.put( 'e', 'bass', 'bw',	[40, 80, 100, 120, 120])

			.put( 'i', 'bass', 'freq',	[250, 1750, 2600, 3050, 3340])
			.put( 'i', 'bass', 'db',	[0, -30, -16, -22, -28])
			.put( 'i', 'bass', 'bw',	[60, 90, 100, 120, 120])

			.put( 'o', 'bass', 'freq',	[400, 750, 2400, 2600, 2900])
			.put( 'o', 'bass', 'db',	[0, -11, -21, -20, -40])
			.put( 'o', 'bass', 'bw',	[40, 80, 100, 120, 120])

			.put( 'u', 'bass', 'freq',	[350, 600, 2400, 2675, 2950])
			.put( 'u', 'bass', 'db',	[0, -20, -32, -28, -36])
			.put( 'u', 'bass', 'bw',	[40, 80, 100, 120, 120])


			.put( 'a', 'counterTenor', 'freq',	[660, 1120, 2750, 3000, 3350])
			.put( 'a', 'counterTenor', 'db',		[0, -6, -23, -24, -38])
			.put( 'a', 'counterTenor', 'bw',		[80, 90, 120, 130, 140])

			.put( 'e', 'counterTenor', 'freq',	[440, 1800, 2700, 3000, 3300])
			.put( 'e', 'counterTenor', 'db',		[0, -14, -18, -20, -20])
			.put( 'e', 'counterTenor', 'bw',		[70, 80, 100, 120, 120])

			.put( 'i', 'counterTenor', 'freq',	[270, 1850, 2900, 3350, 3590])
			.put( 'i', 'counterTenor', 'db',		[0, -24, -24, -36, -36])
			.put( 'i', 'counterTenor', 'bw',		[40, 90, 100, 120, 120])

			.put( 'o', 'counterTenor', 'freq',	[430, 820, 2700, 3000, 3300])
			.put( 'o', 'counterTenor', 'db',		[0, -10, -26, -22, -34])
			.put( 'o', 'counterTenor', 'bw',		[40, 80, 100, 120, 120])

			.put( 'u', 'counterTenor', 'freq',	[370, 630, 2750, 3000, 3400])
			.put( 'u', 'counterTenor', 'db',		[0, -20, -23, -30, -34])
			.put( 'u', 'counterTenor', 'bw',		[40, 60, 100, 120, 120]);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showVowelMorph";
		arrOptions = [0,1,0,0,0, 0];
		arrOptionData = [
			[ // ind0 - Morph output
				["Vowel Morph",
					{arg vowelA, vowelB, vowelC, vowelD, vowelE, sliderXTotal, sliderYTotal, morphFunction;
						morphFunction.value(vowelA, vowelB, vowelC, vowelD, vowelE, sliderXTotal, sliderYTotal);
					}
				],
				["Solo Vowel 1 ",
					{arg vowelA, vowelB, vowelC, vowelD, vowelE; vowelA; }
				],
				["Solo Vowel 2 ",
					{arg vowelA, vowelB, vowelC, vowelD, vowelE; vowelB; }
				],
				["Solo Vowel 3 ",
					{arg vowelA, vowelB, vowelC, vowelD, vowelE; vowelC; }
				],
				["Solo Vowel 4 ",
					{arg vowelA, vowelB, vowelC, vowelD, vowelE; vowelD; }
				],
				["Solo Vowel 5 ",
					{arg vowelA, vowelB, vowelC, vowelD, vowelE; vowelE; }
				],
			],
			[ // ind1 - Morph type
				["2-D Morph - Vowels 1 - 4",
					{arg vowelA, vowelB, vowelC, vowelD, vowelE, sliderXTotal, sliderYTotal;
						var vowelLeft, vowelRight, vowelOut;
						//		---vowels 1-4 on X/Y area:---
						//		vowelA = top left / x=0,y=0
						//		vowelB = top right / x=1,y=0
						//		vowelC = bottom left / x=0,y=1
						//		vowelD = bottom right / x=1,y=1
						vowelLeft = (vowelA * (1-sliderYTotal)) + (vowelC * sliderYTotal);
						vowelRight = (vowelB * (1-sliderYTotal)) + (vowelD * sliderYTotal);
						vowelOut = (vowelLeft * (1-sliderXTotal)) + (vowelRight * sliderXTotal);
					}
				],
				["2-D Morph - Vowels 1 - 5",
					{arg vowelA, vowelB, vowelC, vowelD, vowelE, sliderXTotal, sliderYTotal;
						//		---vowels 1-5 on X/Y area:---
						//		vowelA = top left / x=0,y=0
						//		vowelB = top right / x=1,y=0
						//		vowelC = bottom left / x=0,y=1
						//		vowelD = bottom right / x=1,y=1
						//		vowelE = centre / x=0.5,y=0.5
						var vowelLeft, vowelRight, vowelsAll, middleBias;
						vowelLeft = (vowelA * (1-sliderYTotal)) + (vowelC * sliderYTotal);
						vowelRight = (vowelB * (1-sliderYTotal)) + (vowelD * sliderYTotal);
						vowelsAll = (vowelLeft * (1-sliderXTotal)) + (vowelRight * sliderXTotal);
						middleBias =  ((1-((sliderYTotal-0.5).abs*2)) * (1-((sliderXTotal-0.5).abs*2))).sqrt;
						(vowelsAll * (1 - middleBias) ) + (vowelE * middleBias );
					}
				],
				["Linear Morph - Vowels 1 - 2",
					{arg vowelA, vowelB, vowelC, vowelD, vowelE, sliderXTotal, sliderYTotal;
						var total, holdScalar;
						total = 2;
						holdScalar = (total-1).reciprocal;
						[vowelA, vowelB].collect({arg item, i;
							item  *
							(1 - ((sliderXTotal .min((i+1) * holdScalar) .max((i-1) * holdScalar)
								- (i * holdScalar) ).abs * (total-1)) )
						}).sum;
					}
				],
				["Linear Morph - Vowels 1 - 3",
					{arg vowelA, vowelB, vowelC, vowelD, vowelE, sliderXTotal, sliderYTotal;
						var total, holdScalar;
						total = 3;
						holdScalar = (total-1).reciprocal;
						[vowelA, vowelB, vowelC].collect({arg item, i;
							item  *
							(1 - ((sliderXTotal .min((i+1) * holdScalar) .max((i-1) * holdScalar)
								- (i * holdScalar) ).abs * (total-1)) )
						}).sum;
					}
				],
				["Linear Morph - Vowels 1 - 4",
					{arg vowelA, vowelB, vowelC, vowelD, vowelE, sliderXTotal, sliderYTotal;
						var total, holdScalar;
						total = 4;
						holdScalar = (total-1).reciprocal;
						[vowelA, vowelB, vowelC, vowelD].collect({arg item, i;
							item  *
							(1 - ((sliderXTotal .min((i+1) * holdScalar) .max((i-1) * holdScalar)
								- (i * holdScalar) ).abs * (total-1)) )
						}).sum;
					}
				],
				["Linear Morph - Vowels 1 - 5",
					{arg vowelA, vowelB, vowelC, vowelD, vowelE, sliderXTotal, sliderYTotal;
						var total, holdScalar;
						total = 5;
						holdScalar = (total-1).reciprocal;
						[vowelA, vowelB, vowelC, vowelD, vowelE].collect({arg item, i;
							item  *
							(1 - ((sliderXTotal .min((i+1) * holdScalar) .max((i-1) * holdScalar)
								- (i * holdScalar) ).abs * (total-1)) )
						}).sum;
					}
				],
			],
			// ind2 - sound function
			[
				["Formant",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps;
						0.1 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								Formant.ar(baseFreq, holdFreq, holdWidth, arrAmps[i]);
							})
						);
					}
				],
				["External Inputs",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps, extInput;
						var sound = TXClean.ar(InFeedback.ar(extInput, 2));
						2 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								BPF.ar(sound, holdFreq, holdWidth.reciprocal, arrAmps[i]);
							})
						);
					}
				],
				["Sawtooth Wave",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps;
						var sound = Saw.ar(baseFreq);
						3 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								BPF.ar(sound, holdFreq, holdWidth.reciprocal, arrAmps[i]);
							})
						);
					}
				],
				["Pulse Width 3%",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps;
						var sound = Pulse.ar(baseFreq, 0.03);
						3 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								BPF.ar(sound, holdFreq, holdWidth.reciprocal, arrAmps[i]);
							})
						);
					}
				],
				["Pulse Width 5%",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps;
						var sound = Pulse.ar(baseFreq, 0.05);
						3 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								BPF.ar(sound, holdFreq, holdWidth.reciprocal, arrAmps[i]);
							})
						);
					}
				],
				["Pulse Width 10%",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps;
						var sound = Pulse.ar(baseFreq, 0.1);
						3 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								BPF.ar(sound, holdFreq, holdWidth.reciprocal, arrAmps[i]);
							})
						);
					}
				],
				["Pulse Width 20%",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps;
						var sound = Pulse.ar(baseFreq, 0.2);
						3 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								BPF.ar(sound, holdFreq, holdWidth.reciprocal, arrAmps[i]);
							})
						);
					}
				],
				["Pulse Width 30%",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps;
						var sound = Pulse.ar(baseFreq, 0.3);
						3 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								BPF.ar(sound, holdFreq, holdWidth.reciprocal, arrAmps[i]);
							})
						);
					}
				],
				["Pulse Width 40%",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps;
						var sound = Pulse.ar(baseFreq, 0.4);
						3 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								BPF.ar(sound, holdFreq, holdWidth.reciprocal, arrAmps[i]);
							})
						);
					}
				],
				["Pulse Width 50%",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps;
						var sound = Pulse.ar(baseFreq, 0.5);
						3 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								BPF.ar(sound, holdFreq, holdWidth.reciprocal, arrAmps[i]);
							})
						);
					}
				],
				["White Noise",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps;
						var sound = [WhiteNoise.ar, WhiteNoise.ar];
						2 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								BPF.ar(sound, holdFreq, holdWidth.reciprocal, arrAmps[i]);
							})
						);
					}
				],
				["Pink Noise",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps;
						var sound = [PinkNoise.ar, PinkNoise.ar];
						3 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								BPF.ar(sound, holdFreq, holdWidth.reciprocal, arrAmps[i]);
							})
						);
					}
				],
				["Brown Noise",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps;
						var sound = [BrownNoise.ar, BrownNoise.ar];
						2 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								BPF.ar(sound, holdFreq, holdWidth.reciprocal, arrAmps[i]);
							})
						);
					}
				],
				["Clip Noise",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps;
						var sound = [ClipNoise.ar, ClipNoise.ar];
						2 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								BPF.ar(sound, holdFreq, holdWidth.reciprocal, arrAmps[i]);
							})
						);
					}
				],
				["Gray Noise",
					{arg baseFreq, arrFreqs, arrWidths, arrAmps;
						var sound = [GrayNoise.ar, GrayNoise.ar];
						2 * // scale
						Mix.new(
							arrFreqs.collect({arg freq, i;
								var holdFreq = classData.freqSpec.map(freq);
								var holdWidth = classData.bandwidthSpec.map(arrWidths[i]);
								BPF.ar(sound, holdFreq, holdWidth.reciprocal, arrAmps[i]);
							})
						);
					}
				],
			],
			[ //ind3 freq function
				["Off", {arg freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector;
					//( (freqMax/freqMin) ** ((freq + modFreq).max(0.0001).min(1)) ) * freqMin;
					(freq + modFreq).max(0).min(1).linexp(0, 1, freqMin, freqMax)
				}],
				["On", {arg freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector;
					var holdArray;
					// convert to cps
					holdArray = this.getNoteArray.midicps;
					Select.kr( (((freqSelector + modFreqSelector).max(0).min(1)) * holdArray.size), holdArray);
				}],
			],
			[// ind4 lag function
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
			],
			// ind5 amp compensation
			TXAmpComp.arrOptionData,
		];
		arrSynthArgSpecs = [
			["extInput", 0, 0],
			["out", 0, 0],

			["freq", ControlSpec(0.midicps, 127.midicps, 'exp').unmap(48.midicps), defLagTime],
			["freqMin", 0.midicps, defLagTime],
			["freqMax", 127.midicps, defLagTime],
			["beatsFreq", 0.2, defLagTime],
			["beatsFreqMin", 0, defLagTime],
			["beatsFreqMax", 5, defLagTime],
			["i_noteListTypeInd",12, \ir],
			["i_noteListRoot", 0, \ir],
			["i_noteListMin", 36, \ir],
			["i_noteListMax", 72, \ir],
			["i_noteListSize", 1, \ir],
			["freqSelector", 0.5, defLagTime],
			["lag", ControlSpec(0.01, 3, 'exp').unmap(0.1), defLagTime],
			["lagMin", 0.01, defLagTime],
			["lagMax", 3, defLagTime],
			["lag2",  ControlSpec(0.01, 3, 'exp').unmap(0.1), defLagTime],
			["lag2Min", 0.01, defLagTime],
			["lag2Max", 3, defLagTime],

			["eqAFilt1Freq", classData.freqSpec.unmap(270), defLagTime],
			["eqAFilt2Freq", classData.freqSpec.unmap(2300), defLagTime],
			["eqAFilt3Freq", classData.freqSpec.unmap(3000), defLagTime],
			["eqAFilt4Freq", classData.freqSpec.unmap(3000), defLagTime],
			["eqAFilt5Freq", classData.freqSpec.unmap(3500), defLagTime],
			["eqAFilt1Gain", 0.dbamp, defLagTime],
			["eqAFilt2Gain", -6.dbamp, defLagTime],
			["eqAFilt3Gain", -12.dbamp, defLagTime],
			["eqAFilt4Gain", 0, defLagTime],
			["eqAFilt5Gain", 0, defLagTime],
			["eqAFilt1Width", classData.bandwidthSpec.unmap(50), defLagTime],
			["eqAFilt2Width", classData.bandwidthSpec.unmap(60), defLagTime],
			["eqAFilt3Width", classData.bandwidthSpec.unmap(170), defLagTime],
			["eqAFilt4Width", classData.bandwidthSpec.unmap(180), defLagTime],
			["eqAFilt5Width", classData.bandwidthSpec.unmap(200), defLagTime],

			["eqBFilt1Freq", classData.freqSpec.unmap(730), defLagTime],
			["eqBFilt2Freq", classData.freqSpec.unmap(1100), defLagTime],
			["eqBFilt3Freq", classData.freqSpec.unmap(2450), defLagTime],
			["eqBFilt4Freq", classData.freqSpec.unmap(3000), defLagTime],
			["eqBFilt5Freq", classData.freqSpec.unmap(3500), defLagTime],
			["eqBFilt1Gain", 0.dbamp, defLagTime],
			["eqBFilt2Gain", -6.dbamp, defLagTime],
			["eqBFilt3Gain", -12.dbamp, defLagTime],
			["eqBFilt4Gain", 0, defLagTime],
			["eqBFilt5Gain", 0, defLagTime],
			["eqBFilt1Width", classData.bandwidthSpec.unmap(50), defLagTime],
			["eqBFilt2Width", classData.bandwidthSpec.unmap(60), defLagTime],
			["eqBFilt3Width", classData.bandwidthSpec.unmap(170), defLagTime],
			["eqBFilt4Width", classData.bandwidthSpec.unmap(180), defLagTime],
			["eqBFilt5Width", classData.bandwidthSpec.unmap(200), defLagTime],

			["eqCFilt1Freq", classData.freqSpec.unmap(490), defLagTime],
			["eqCFilt2Freq", classData.freqSpec.unmap(1350), defLagTime],
			["eqCFilt3Freq", classData.freqSpec.unmap(1700), defLagTime],
			["eqCFilt4Freq", classData.freqSpec.unmap(3000), defLagTime],
			["eqCFilt5Freq", classData.freqSpec.unmap(3500), defLagTime],
			["eqCFilt1Gain", 0.dbamp, defLagTime],
			["eqCFilt2Gain", -6.dbamp, defLagTime],
			["eqCFilt3Gain", -12.dbamp, defLagTime],
			["eqCFilt4Gain", 0, defLagTime],
			["eqCFilt5Gain", 0, defLagTime],
			["eqCFilt1Width", classData.bandwidthSpec.unmap(50), defLagTime],
			["eqCFilt2Width", classData.bandwidthSpec.unmap(60), defLagTime],
			["eqCFilt3Width", classData.bandwidthSpec.unmap(170), defLagTime],
			["eqCFilt4Width", classData.bandwidthSpec.unmap(180), defLagTime],
			["eqCFilt5Width", classData.bandwidthSpec.unmap(200), defLagTime],

			["eqDFilt1Freq", classData.freqSpec.unmap(530), defLagTime],
			["eqDFilt2Freq", classData.freqSpec.unmap(1850), defLagTime],
			["eqDFilt3Freq", classData.freqSpec.unmap(2500), defLagTime],
			["eqDFilt4Freq", classData.freqSpec.unmap(3000), defLagTime],
			["eqDFilt5Freq", classData.freqSpec.unmap(3500), defLagTime],
			["eqDFilt1Gain", 0.dbamp, defLagTime],
			["eqDFilt2Gain", -6.dbamp, defLagTime],
			["eqDFilt3Gain", -12.dbamp, defLagTime],
			["eqDFilt4Gain", 0, defLagTime],
			["eqDFilt5Gain", 0, defLagTime],
			["eqDFilt1Width", classData.bandwidthSpec.unmap(50), defLagTime],
			["eqDFilt2Width", classData.bandwidthSpec.unmap(60), defLagTime],
			["eqDFilt3Width", classData.bandwidthSpec.unmap(170), defLagTime],
			["eqDFilt4Width", classData.bandwidthSpec.unmap(180), defLagTime],
			["eqDFilt5Width", classData.bandwidthSpec.unmap(200), defLagTime],

			["eqEFilt1Freq", classData.freqSpec.unmap(440), defLagTime],
			["eqEFilt2Freq", classData.freqSpec.unmap(1000), defLagTime],
			["eqEFilt3Freq", classData.freqSpec.unmap(2250), defLagTime],
			["eqEFilt4Freq", classData.freqSpec.unmap(3000), defLagTime],
			["eqEFilt5Freq", classData.freqSpec.unmap(3500), defLagTime],
			["eqEFilt1Gain", 0.dbamp, defLagTime],
			["eqEFilt2Gain", -6.dbamp, defLagTime],
			["eqEFilt3Gain", -12.dbamp, defLagTime],
			["eqEFilt4Gain", 0, defLagTime],
			["eqEFilt5Gain", 0, defLagTime],
			["eqEFilt1Width", classData.bandwidthSpec.unmap(50), defLagTime],
			["eqEFilt2Width", classData.bandwidthSpec.unmap(60), defLagTime],
			["eqEFilt3Width", classData.bandwidthSpec.unmap(170), defLagTime],
			["eqEFilt4Width", classData.bandwidthSpec.unmap(180), defLagTime],
			["eqEFilt5Width", classData.bandwidthSpec.unmap(200), defLagTime],

			["sliderXVal", 0, 0],
			["sliderYVal", 0, 0],
			["outLevel", 0.5, defLagTime],

			["modFreq", 0, defLagTime],
			["modBeatsFreq", 0, defLagTime],
			["modFreqSelector", 0, defLagTime],
			["modLag", 0, defLagTime],
			["modLag2", 0, defLagTime],

			["modSliderXVal", 0, 0],
			["modSliderYVal", 0, 0],
			["modOutLevel", 0, defLagTime],

			// N.B. the args below aren't used in the synthdef, just stored as synth args for convenience
			["eqAPresetNo", 20],
			["eqBPresetNo", 24],
			["eqCPresetNo", 29],
			["eqDPresetNo", 22],
			["eqEPresetNo", 26],
		];
		synthDefFunc = {
			arg extInput, out,

			freq, freqMin, freqMax, beatsFreq, beatsFreqMin, beatsFreqMax,
			i_noteListTypeInd, i_noteListRoot, i_noteListMin,  i_noteListMax, i_noteListSize, freqSelector,
			lag, lagMin, lagMax, lag2, lag2Min, lag2Max,

			eqAFilt1Freq, eqAFilt2Freq, eqAFilt3Freq, eqAFilt4Freq, eqAFilt5Freq,
			eqAFilt1Gain, eqAFilt2Gain, eqAFilt3Gain, eqAFilt4Gain, eqAFilt5Gain,
			eqAFilt1Width, eqAFilt2Width, eqAFilt3Width, eqAFilt4Width, eqAFilt5Width,
			eqBFilt1Freq, eqBFilt2Freq, eqBFilt3Freq, eqBFilt4Freq, eqBFilt5Freq,
			eqBFilt1Gain, eqBFilt2Gain, eqBFilt3Gain, eqBFilt4Gain, eqBFilt5Gain,
			eqBFilt1Width, eqBFilt2Width, eqBFilt3Width, eqBFilt4Width, eqBFilt5Width,
			eqCFilt1Freq, eqCFilt2Freq, eqCFilt3Freq, eqCFilt4Freq, eqCFilt5Freq,
			eqCFilt1Gain, eqCFilt2Gain, eqCFilt3Gain, eqCFilt4Gain, eqCFilt5Gain,
			eqCFilt1Width, eqCFilt2Width, eqCFilt3Width, eqCFilt4Width, eqCFilt5Width,
			eqDFilt1Freq, eqDFilt2Freq, eqDFilt3Freq, eqDFilt4Freq, eqDFilt5Freq,
			eqDFilt1Gain, eqDFilt2Gain, eqDFilt3Gain, eqDFilt4Gain, eqDFilt5Gain,
			eqDFilt1Width, eqDFilt2Width, eqDFilt3Width, eqDFilt4Width, eqDFilt5Width,
			eqEFilt1Freq, eqEFilt2Freq, eqEFilt3Freq, eqEFilt4Freq, eqEFilt5Freq,
			eqEFilt1Gain, eqEFilt2Gain, eqEFilt3Gain, eqEFilt4Gain, eqEFilt5Gain,
			eqEFilt1Width, eqEFilt2Width, eqEFilt3Width, eqEFilt4Width, eqEFilt5Width,

			sliderXVal, sliderYVal,  outLevel,

			modFreq = 0, modBeatsFreq = 0, modFreqSelector = 0, modLag = 0, modLag2 = 0,
			modSliderXVal=0, modSliderYVal=0, modOutLevel= 0;

			var soundFunc, outMorph, outsound;
			var groupA = [eqAFilt1Freq, eqAFilt2Freq, eqAFilt3Freq, eqAFilt4Freq, eqAFilt5Freq,
				eqAFilt1Gain, eqAFilt2Gain, eqAFilt3Gain, eqAFilt4Gain, eqAFilt5Gain,
				eqAFilt1Width, eqAFilt2Width, eqAFilt3Width, eqAFilt4Width, eqAFilt5Width];
			var groupB = [eqBFilt1Freq, eqBFilt2Freq, eqBFilt3Freq, eqBFilt4Freq, eqBFilt5Freq,
				eqBFilt1Gain, eqBFilt2Gain, eqBFilt3Gain, eqBFilt4Gain, eqBFilt5Gain,
				eqBFilt1Width, eqBFilt2Width, eqBFilt3Width, eqBFilt4Width, eqBFilt5Width];
			var groupC = [eqCFilt1Freq, eqCFilt2Freq, eqCFilt3Freq, eqCFilt4Freq, eqCFilt5Freq,
				eqCFilt1Gain, eqCFilt2Gain, eqCFilt3Gain, eqCFilt4Gain, eqCFilt5Gain,
				eqCFilt1Width, eqCFilt2Width, eqCFilt3Width, eqCFilt4Width, eqCFilt5Width];
			var groupD = [eqDFilt1Freq, eqDFilt2Freq, eqDFilt3Freq, eqDFilt4Freq, eqDFilt5Freq,
				eqDFilt1Gain, eqDFilt2Gain, eqDFilt3Gain, eqDFilt4Gain, eqDFilt5Gain,
				eqDFilt1Width, eqDFilt2Width, eqDFilt3Width, eqDFilt4Width, eqDFilt5Width];
			var groupE = [eqEFilt1Freq, eqEFilt2Freq, eqEFilt3Freq, eqEFilt4Freq, eqEFilt5Freq,
				eqEFilt1Gain, eqEFilt2Gain, eqEFilt3Gain, eqEFilt4Gain, eqEFilt5Gain,
				eqEFilt1Width, eqEFilt2Width, eqEFilt3Width, eqEFilt4Width, eqEFilt5Width];

			var totSliderX = (sliderXVal + modSliderXVal).max(0).min(1);
			var totSliderY = (sliderYVal + modSliderYVal).max(0).min(1);
			var totOutLevel = (outLevel + modOutLevel).max(0).min(1);

			var funcMorphOut = this.getSynthOption(0);
			var funcMorphType = this.getSynthOption(1);
			var outFreqFunc, outFreq, outBeatsFreq, outFreqLag, lagtime, lagtime2;
			var outLagFunction, ampCompFunction, outAmpComp;
			var arrFreqs, arrWidths, arrAmps;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			outFreqFunc = this.getSynthOption(3);
			outFreq = outFreqFunc.value(freq, freqMin, freqMax, modFreq, freqSelector, modFreqSelector);
			outBeatsFreq = 0.5 * (beatsFreqMin + ((beatsFreqMax - beatsFreqMin) * (beatsFreq + modBeatsFreq).max(0).min(1)));
			lagtime = ( (lagMax/lagMin) ** ((lag + modLag).max(0.001).min(1)) ) * lagMin;
			lagtime2 = ( (lag2Max/lag2Min) ** ((lag2 + modLag2).max(0.001).min(1)) ) * lag2Min;
			outLagFunction = this.getSynthOption(4);
			outFreqLag = outLagFunction.value(outFreq, lagtime, lagtime2);

			outMorph = funcMorphOut.value(groupA, groupB, groupC, groupD, groupE,
					totSliderX, totSliderY, funcMorphType);
			arrFreqs = outMorph[0..4];
			arrAmps = outMorph[5..9];
			arrWidths = outMorph[10..14];
			soundFunc = this.getSynthOption(2);
			outsound = startEnv * soundFunc.value([outFreqLag + outBeatsFreq, outFreqLag - outBeatsFreq],
				arrFreqs, arrWidths, arrAmps, extInput);
			ampCompFunction = this.getSynthOption(5);
			outAmpComp = ampCompFunction.value(outFreqLag);
			// apply starting env and LeadDC
			outsound = LeakDC.ar(totOutLevel * outAmpComp * outsound);
			Out.ar(out, startEnv * TXClean.ar(outsound));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs(
			[
				["SynthOptionPopupPlusMinus", "Output", arrOptionData, 0, 250],
				["SpacerLine", 6],
				["SynthOptionPopupPlusMinus", "Source sound", arrOptionData, 2,],
				["SynthOptionPopupPlusMinus", "Morph Type", arrOptionData, 1, 310,
					{this.buildGuiSpecArray; system.showView;}],
				["EZslider", "Morph X", ControlSpec(0, 1), "sliderXVal", nil, 450],
				["EZslider", "Morph y", ControlSpec(0, 1), "sliderYVal", nil, 450],
				["EZslider", "Out Level", ControlSpec(0, 1), "outLevel", nil, 450],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 5],
				["TXMinMaxFreqNoteSldr", "Frequency", ControlSpec(0.midicps, 20000, \exponential),
					"freq", "freqMin", "freqMax", nil, TXFreqRangePresets.arrFreqRanges],
				["SynthOptionCheckBox", "Use note list instead of variable frequency", arrOptionData, 3, 400],
				["EZslider", "Note select", \unipolar.asSpec, "freqSelector"],
				["TXPopupAction", "Chord/ scale", TXScale.arrScaleNames, "i_noteListTypeInd", {this.updateSynth;}, 400],
				["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
					"i_noteListRoot", {this.updateSynth;}, 140],
				["TXNoteRangeSlider", "Note range", "i_noteListMin", "i_noteListMax", {this.updateSynth;}, true],
				["TXStaticText", "Note count", {this.getNoteTotalText}, {arg view; noteListTextView = view;}],
				["TXMinMaxSliderSplit", "Beats Freq", ControlSpec(0,100),
					"beatsFreq", "beatsFreqMin", "beatsFreqMax"],
				["SynthOptionPopupPlusMinus", "Smoothing", arrOptionData, 4],
				["TXMinMaxSliderSplit", "Time 1", classData.lagSpec, "lag", "lagMin", "lagMax"],
				["TXMinMaxSliderSplit", "Time 2", classData.lagSpec, "lag2", "lag2Min", "lag2Max"],
			]
			++ ["eqAFilt1Freq", "eqAFilt2Freq", "eqAFilt3Freq", "eqAFilt4Freq", "eqAFilt5Freq",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.freqSpec, item]
			})
			++ ["eqAFilt1Width", "eqAFilt2Width", "eqAFilt3Width", "eqAFilt4Width", "eqAFilt5Width", ]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.bandwidthSpec, item]
			})
			++ ["eqAFilt1Gain", "eqAFilt2Gain", "eqAFilt3Gain", "eqAFilt4Gain", "eqAFilt5Gain", ]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.gainSpec, item]
			})
			++ ["eqBFilt1Freq", "eqBFilt2Freq", "eqBFilt3Freq", "eqBFilt4Freq", "eqBFilt5Freq", ]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.freqSpec, item]
			})
			++ ["eqBFilt1Width", "eqBFilt2Width", "eqBFilt3Width", "eqBFilt4Width", "eqBFilt5Width", ]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.bandwidthSpec, item]
			})
			++ ["eqBFilt1Gain", "eqBFilt2Gain", "eqBFilt3Gain", "eqBFilt4Gain", "eqBFilt5Gain", ]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.gainSpec, item]
			})
			++ ["eqCFilt1Freq", "eqCFilt2Freq", "eqCFilt3Freq", "eqCFilt4Freq", "eqCFilt5Freq", ]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.freqSpec, item]
			})
			++ ["eqCFilt1Width", "eqCFilt2Width", "eqCFilt3Width", "eqCFilt4Width", "eqCFilt5Width",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.bandwidthSpec, item]
			})
			++ ["eqCFilt1Gain", "eqCFilt2Gain", "eqCFilt3Gain", "eqCFilt4Gain", "eqCFilt5Gain",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.gainSpec, item]
			})
			++ ["eqDFilt1Freq", "eqDFilt2Freq", "eqDFilt3Freq", "eqDFilt4Freq", "eqDFilt5Freq",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.freqSpec, item]
			})
			++ ["eqDFilt1Width", "eqDFilt2Width", "eqDFilt3Width", "eqDFilt4Width", "eqDFilt5Width", ]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.bandwidthSpec, item]
			})
			++ ["eqDFilt1Gain", "eqDFilt2Gain", "eqDFilt3Gain", "eqDFilt4Gain", "eqDFilt5Gain",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.gainSpec, item]
			})
			++ ["eqEFilt1Freq", "eqEFilt2Freq", "eqEFilt3Freq", "eqEFilt4Freq", "eqEFilt5Freq",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.freqSpec, item]
			})
			++ ["eqEFilt1Width", "eqEFilt2Width", "eqEFilt3Width", "eqEFilt4Width", "eqEFilt5Width", ]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.bandwidthSpec, item]
			})
			++ ["eqEFilt1Gain", "eqEFilt2Gain", "eqEFilt3Gain", "eqEFilt4Gain", "eqEFilt5Gain",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.gainSpec, item]
			})
		);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	////////////////////////////////

	buildGuiSpecArray {
		var arrVowelNames = [];
		['child','woman', 'man',].do({arg register;
			['beat', 'bit','bet','bat','part','pot','boot','book','but','pert'].do({arg vowel;
				arrVowelNames = arrVowelNames.add(
					register.asString ++ ", vowel in: " ++ vowel.asString
				);
			});
		});
		['soprano','alto', 'tenor', 'bass', 'counterTenor', ].do({arg register;
			['a','e', 'i', 'o', 'u', ].do({arg vowel;
				arrVowelNames = arrVowelNames.add(
					register.asString ++ ", vowel: " ++ vowel.asString
				);
			});
		});
		guiSpecArray = [
			["SpacerLine", 2],
			["ActionButton", "Vowel Morph", {displayOption = "showVowelMorph";
				if (arrOptions[0] != 0, {arrOptions[0] = 0; this.rebuildSynth; });
				this.buildGuiSpecArray; system.showView;}, 84,
			TXColor.white, this.getButtonColour(displayOption == "showVowelMorph")],
			["ActionButton", "Vowel 1", {displayOption = "showVowel1";
				arrOptions[0] = 1; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 56,
			TXColor.white, this.getButtonColour(displayOption == "showVowel1")],
			["ActionButton", "Vowel 2", {displayOption = "showVowel2";
				arrOptions[0] = 2; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 56,
			TXColor.white, this.getButtonColour(displayOption == "showVowel2")],
			["ActionButton", "Vowel 3", {displayOption = "showVowel3";
				arrOptions[0] = 3; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 56,
			TXColor.white, this.getButtonColour(displayOption == "showVowel3")],
			["ActionButton", "Vowel 4", {displayOption = "showVowel4";
				arrOptions[0] = 4; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 56,
			TXColor.white, this.getButtonColour(displayOption == "showVowel4")],
			["ActionButton", "Vowel 5", {displayOption = "showVowel5";
				arrOptions[0] = 5; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 56,
			TXColor.white, this.getButtonColour(displayOption == "showVowel5")],
			["Spacer", 6],
			["ActionButton", "Freq", {displayOption = "showFreq";
				if (arrOptions[0] != 0, {arrOptions[0] = 0; this.rebuildSynth; });
				this.buildGuiSpecArray; system.showView;}, 54,
			TXColor.white, this.getButtonColour(displayOption == "showFreq")],
			["ActionButton", "Freq Smooth", {displayOption = "showSmoothing";
				if (arrOptions[0] != 0, {arrOptions[0] = 0; this.rebuildSynth; });
				this.buildGuiSpecArray; system.showView;}, 80,
			TXColor.white, this.getButtonColour(displayOption == "showSmoothing")],
			["DividingLine"],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Output", arrOptionData, 0, 250],
			["Spacer", 20],
			["SynthOptionPopupPlusMinus", "Source sound", arrOptionData, 2, 250],
			["DividingLine"],
			["SpacerLine", 4],
		];

		if (displayOption == "showVowelMorph", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Morph Type", arrOptionData, 1, 310,
					{this.buildGuiSpecArray; system.showView;}],
				["SpacerLine", 10],
			];
			if (arrOptions[1] < 2, {
				guiSpecArray = guiSpecArray ++[
					["TextBarLeft", "Vowel 3", 80, 20, TXColor.paleYellow],
					["Spacer", 246],
					["TextBarLeft", "Vowel 4", 80, 20, TXColor.paleYellow],
					["SpacerLine", 2],
					["TX2DSlider", "Morph X-Y", ControlSpec(0, 1),
						"sliderXVal", "sliderYVal", nil, 250, 334, true],
					["SpacerLine", 2],
				];
			});
			if (arrOptions[1] == 0, {
				guiSpecArray = guiSpecArray ++[
					["TextBarLeft", "Vowel 1", 80, 20, TXColor.paleYellow],
					["Spacer", 246],
					["TextBarLeft", "Vowel 2", 80, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 1, {
				guiSpecArray = guiSpecArray ++[
					["TextBarLeft", "Vowel 1", 80, 20, TXColor.paleYellow],
					["Spacer", 19],
					["TextBarLeft", "Vowel 5 is in the centre of the box", 200,
						20, TXColor.paleYellow],
					["Spacer", 19],
					["TextBarLeft", "Vowel 2", 80, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] > 1, {
				guiSpecArray = guiSpecArray ++[
					//["SpacerLine", 6],
					["Spacer", 60],
					["TextBarLeft", "Vowel 1", 69, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 2, {
				guiSpecArray = guiSpecArray ++[
					["Spacer", 212],
					["TextBarLeft", "Vowel 2", 69, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 3, {
				guiSpecArray = guiSpecArray ++[
					["Spacer", 69],
					["TextBarLeft", "Vowel 2", 69, 20, TXColor.paleYellow],
					["Spacer", 69],
					["TextBarLeft", "Vowel 3", 69, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 4, {
				guiSpecArray = guiSpecArray ++[
					["Spacer", 21],
					["TextBarLeft", "Vowel 2", 69, 20, TXColor.paleYellow],
					["Spacer", 21],
					["TextBarLeft", "Vowel 3", 69, 20, TXColor.paleYellow],
					["Spacer", 21],
					["TextBarLeft", "Vowel 4", 69, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 5, {
				guiSpecArray = guiSpecArray ++[
					["TextBarLeft", "Vowel 2", 69, 20, TXColor.paleYellow],
					["TextBarLeft", "Vowel 3", 69, 20, TXColor.paleYellow],
					["TextBarLeft", "Vowel 4", 69, 20, TXColor.paleYellow],
					["TextBarLeft", "Vowel 5", 69, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] > 1, {
				guiSpecArray = guiSpecArray ++[
					["NextLine"],
					["EZslider", "Morph X", ControlSpec(0, 1), "sliderXVal", nil, 450],
					//["SpacerLine", 240],
				];
			});
			guiSpecArray = guiSpecArray ++[
				["DividingLine"],
				["SpacerLine", 4],
				["EZslider", "Out Level", ControlSpec(0, 1), "outLevel", nil, 450],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 5],
			];
		});
		if (displayOption == "showVowel1", {
			guiSpecArray = guiSpecArray ++[
				["Spacer", 80],
				["TextBar", "Vowel 1 Formants", 316],
				["SpacerLine", 6],
				["Spacer", 36],
				["TXMultiKnobNoUnmap", "Freq",
					["eqAFilt1Freq", "eqAFilt2Freq", "eqAFilt3Freq", "eqAFilt4Freq", "eqAFilt5Freq",],
					5, classData.freqSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqAFilt1Width", "eqAFilt2Width", "eqAFilt3Width", "eqAFilt4Width", "eqAFilt5Width", ]);}, 36],
				["TXMultiKnobNoUnmap", "Width",
					["eqAFilt1Width", "eqAFilt2Width", "eqAFilt3Width", "eqAFilt4Width", "eqAFilt5Width",],
					5, classData.bandwidthSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqAFilt1Gain", "eqAFilt2Gain", "eqAFilt3Gain", "eqAFilt4Gain", "eqAFilt5Gain", ]);}, 36],
				["TXMultiKnobNoUnmap", "Gain",
					["eqAFilt1Gain", "eqAFilt2Gain", "eqAFilt3Gain", "eqAFilt4Gain", "eqAFilt5Gain", ],
					5, classData.gainSpec, nil, nil, 60],
				["SpacerLine", 6],
				["TXPopupActionPlusMinus", "Presets", arrVowelNames, "eqAPresetNo", nil, 300],
				["Spacer", 6],
				["ActionButton", "Load", {this.setVowelData("eqAPresetNo",
					["eqAFilt1Freq", "eqAFilt2Freq", "eqAFilt3Freq", "eqAFilt4Freq", "eqAFilt5Freq", ],
					["eqAFilt1Width", "eqAFilt2Width", "eqAFilt3Width", "eqAFilt4Width", "eqAFilt5Width", ],
					["eqAFilt1Gain", "eqAFilt2Gain", "eqAFilt3Gain", "eqAFilt4Gain", "eqAFilt5Gain",],
				);}, 60],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Out Level", ControlSpec(0, 1), "outLevel", nil, 450],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 5],
			];
		});
		if (displayOption == "showVowel2", {
			guiSpecArray = guiSpecArray ++[
				["Spacer", 80],
				["TextBar", "Vowel 2 Formants", 316],
				["SpacerLine", 6],
				["Spacer", 36],
				["TXMultiKnobNoUnmap", "Freq",
					["eqBFilt1Freq", "eqBFilt2Freq", "eqBFilt3Freq", "eqBFilt4Freq", "eqBFilt5Freq",],
					5, classData.freqSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqBFilt1Width", "eqBFilt2Width", "eqBFilt3Width", "eqBFilt4Width", "eqBFilt5Width", ]);}, 36],
				["TXMultiKnobNoUnmap", "Width",
					["eqBFilt1Width", "eqBFilt2Width", "eqBFilt3Width", "eqBFilt4Width", "eqBFilt5Width",],
					5, classData.bandwidthSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqBFilt1Gain", "eqBFilt2Gain", "eqBFilt3Gain", "eqBFilt4Gain", "eqBFilt5Gain", ]);}, 36],
				["TXMultiKnobNoUnmap", "Gain",
					["eqBFilt1Gain", "eqBFilt2Gain", "eqBFilt3Gain", "eqBFilt4Gain", "eqBFilt5Gain", ],
					5, classData.gainSpec, nil, nil, 60],
				["SpacerLine", 6],
				["TXPopupActionPlusMinus", "Presets", arrVowelNames, "eqBPresetNo", nil, 300],
				["Spacer", 6],
				["ActionButton", "Load", {this.setVowelData("eqBPresetNo",
					["eqBFilt1Freq", "eqBFilt2Freq", "eqBFilt3Freq", "eqBFilt4Freq", "eqBFilt5Freq", ],
					["eqBFilt1Width", "eqBFilt2Width", "eqBFilt3Width", "eqBFilt4Width", "eqBFilt5Width", ],
					["eqBFilt1Gain", "eqBFilt2Gain", "eqBFilt3Gain", "eqBFilt4Gain", "eqBFilt5Gain",],
				);}, 60],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Out Level", ControlSpec(0, 1), "outLevel", nil, 450],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 5],
			];
		});
		if (displayOption == "showVowel3", {
			guiSpecArray = guiSpecArray ++[
				["Spacer", 80],
				["TextBar", "Vowel 3 Formants", 316],
				["SpacerLine", 6],
				["Spacer", 36],
				["TXMultiKnobNoUnmap", "Freq",
					["eqCFilt1Freq", "eqCFilt2Freq", "eqCFilt3Freq", "eqCFilt4Freq", "eqCFilt5Freq", ],
					5, classData.freqSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqCFilt1Width", "eqCFilt2Width", "eqCFilt3Width", "eqCFilt4Width", "eqCFilt5Width",]);}, 36],
				["TXMultiKnobNoUnmap", "Width",
					["eqCFilt1Width", "eqCFilt2Width", "eqCFilt3Width", "eqCFilt4Width", "eqCFilt5Width",],
					5, classData.bandwidthSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqCFilt1Gain", "eqCFilt2Gain", "eqCFilt3Gain", "eqCFilt4Gain", "eqCFilt5Gain",]);}, 36],
				["TXMultiKnobNoUnmap", "Gain",
					["eqCFilt1Gain", "eqCFilt2Gain", "eqCFilt3Gain", "eqCFilt4Gain", "eqCFilt5Gain", ],
					5, classData.gainSpec, nil, nil, 60],
				["SpacerLine", 6],
				["TXPopupActionPlusMinus", "Presets", arrVowelNames, "eqCPresetNo", nil, 300],
				["Spacer", 6],
				["ActionButton", "Load", {this.setVowelData("eqCPresetNo",
					["eqCFilt1Freq", "eqCFilt2Freq", "eqCFilt3Freq", "eqCFilt4Freq", "eqCFilt5Freq", ],
					["eqCFilt1Width", "eqCFilt2Width", "eqCFilt3Width", "eqCFilt4Width", "eqCFilt5Width", ],
					["eqCFilt1Gain", "eqCFilt2Gain", "eqCFilt3Gain", "eqCFilt4Gain", "eqCFilt5Gain",],
				);}, 60],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Out Level", ControlSpec(0, 1), "outLevel", nil, 450],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 5],
			];
		});
		if (displayOption == "showVowel4", {
			guiSpecArray = guiSpecArray ++[
				["Spacer", 80],
				["TextBar", "Vowel 5 Formants", 316],
				["SpacerLine", 6],
				["Spacer", 36],
				["TXMultiKnobNoUnmap", "Freq",
					["eqDFilt1Freq", "eqDFilt2Freq", "eqDFilt3Freq", "eqDFilt4Freq", "eqDFilt5Freq",],
					5, classData.freqSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqDFilt1Width", "eqDFilt2Width", "eqDFilt3Width", "eqDFilt4Width", "eqDFilt5Width", ]);}, 36],
				["TXMultiKnobNoUnmap", "Width",
					["eqDFilt1Width", "eqDFilt2Width", "eqDFilt3Width", "eqDFilt4Width", "eqDFilt5Width",],
					5, classData.bandwidthSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqDFilt1Gain", "eqDFilt2Gain", "eqDFilt3Gain", "eqDFilt4Gain", "eqDFilt5Gain", ]);}, 36],
				["TXMultiKnobNoUnmap", "Gain",
					["eqDFilt1Gain", "eqDFilt2Gain", "eqDFilt3Gain", "eqDFilt4Gain", "eqDFilt5Gain", ],
					5, classData.gainSpec, nil, nil, 60],
				["SpacerLine", 6],
				["TXPopupActionPlusMinus", "Presets", arrVowelNames, "eqDPresetNo", nil, 300],
				["Spacer", 6],
				["ActionButton", "Load", {this.setVowelData("eqDPresetNo",
					["eqDFilt1Freq", "eqDFilt2Freq", "eqDFilt3Freq", "eqDFilt4Freq", "eqDFilt5Freq", ],
					["eqDFilt1Width", "eqDFilt2Width", "eqDFilt3Width", "eqDFilt4Width", "eqDFilt5Width", ],
					["eqDFilt1Gain", "eqDFilt2Gain", "eqDFilt3Gain", "eqDFilt4Gain", "eqDFilt5Gain",],
				);}, 60],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Out Level", ControlSpec(0, 1), "outLevel", nil, 450],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 5],
			];
		});
		if (displayOption == "showVowel5", {
			guiSpecArray = guiSpecArray ++[
				["Spacer", 80],
				["TextBar", "Vowel 5 Formants", 316],
				["SpacerLine", 6],
				["Spacer", 36],
				["TXMultiKnobNoUnmap", "Freq",
					["eqEFilt1Freq", "eqEFilt2Freq", "eqEFilt3Freq", "eqEFilt4Freq", "eqEFilt5Freq", ],
					5, classData.freqSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqEFilt1Width", "eqEFilt2Width", "eqEFilt3Width", "eqEFilt4Width", "eqEFilt5Width", ]);}, 36],
				["TXMultiKnobNoUnmap", "Width",
					["eqEFilt1Width", "eqEFilt2Width", "eqEFilt3Width", "eqEFilt4Width", "eqEFilt5Width", ],
					5, classData.bandwidthSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqEFilt1Gain", "eqEFilt2Gain", "eqEFilt3Gain", "eqEFilt4Gain", "eqEFilt5Gain",]);}, 36],
				["TXMultiKnobNoUnmap", "Gain",
					["eqEFilt1Gain", "eqEFilt2Gain", "eqEFilt3Gain", "eqEFilt4Gain", "eqEFilt5Gain",],
					5, classData.gainSpec, nil, nil, 60],
				["SpacerLine", 6],
				["TXPopupActionPlusMinus", "Presets", arrVowelNames, "eqEPresetNo", nil, 300],
				["Spacer", 6],
				["ActionButton", "Load", {this.setVowelData("eqEPresetNo",
					["eqEFilt1Freq", "eqEFilt2Freq", "eqEFilt3Freq", "eqEFilt4Freq", "eqEFilt5Freq", ],
					["eqEFilt1Width", "eqEFilt2Width", "eqEFilt3Width", "eqEFilt4Width", "eqEFilt5Width", ],
					["eqEFilt1Gain", "eqEFilt2Gain", "eqEFilt3Gain", "eqEFilt4Gain", "eqEFilt5Gain",],
				);}, 60],
				["DividingLine"],
				["SpacerLine", 6],
				["EZslider", "Out Level", ControlSpec(0, 1), "outLevel", nil, 450],
				["SpacerLine", 4],
				["SynthOptionPopupPlusMinus", "Amp Comp", arrOptionData, 5],
			];
		});

		if (displayOption == "showFreq", {
			guiSpecArray = guiSpecArray ++[
				["TXMinMaxFreqNoteSldr", "Frequency", ControlSpec(0.midicps, 20000, \exponential),
					"freq", "freqMin", "freqMax", nil, TXFreqRangePresets.arrFreqRanges],
				["DividingLine"],
				["SpacerLine", 2],
				["SynthOptionCheckBox", "Use note list instead of variable frequency", arrOptionData, 3, 400],
				["SpacerLine", 1],
				["TXListViewActionPlusMinus", "Chord/ scale", TXScale.arrScaleNames, "i_noteListTypeInd",
					{this.updateSynth;}, 400, 106],
				["NextLine"],
				["TXPopupAction", "Key / root", ["C", "C#", "D", "D#", "E","F", "F#", "G", "G#", "A", "A#", "B"],
					"i_noteListRoot", {this.updateSynth;}, 140],
				["NextLine"],
				["TXNoteRangeSlider", "Note range", "i_noteListMin", "i_noteListMax", {this.updateSynth;}, true],
				["NextLine"],
				["TXStaticText", "Note count", {this.getNoteTotalText}, {arg view; noteListTextView = view;}],
				["SpacerLine", 1],
				["EZslider", "Note select", \unipolar.asSpec, "freqSelector"],
				["DividingLine"],
				["SpacerLine", 2],
				["TXMinMaxSliderSplit", "Beats Freq", ControlSpec(0,100),
					"beatsFreq", "beatsFreqMin", "beatsFreqMax"],
			];
		});
		if (displayOption == "showSmoothing", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionListPlusMinus", "Smoothing", arrOptionData, 4],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Time 1", classData.lagSpec, "lag", "lagMin", "lagMax"],
				["SpacerLine", 6],
				["TXMinMaxSliderSplit", "Time 2", classData.lagSpec, "lag2", "lag2Min", "lag2Max"],
			];
		});
	}

	getButtonColour { arg colour2Boolean;
		if (colour2Boolean == true, {
			^TXColor.sysGuiCol4;
		},{
			^TXColor.sysGuiCol1;
		});
	}

	copy1ToAll {arg arrayStrings;
		var holdVal = this.getSynthArgSpec(arrayStrings[0]);
		arrayStrings.do({arg item, i;
			if (i > 0, {
				this.setSynthValue(item, holdVal);
			});
		});
		system.showView;
	}

	setVowelData {arg arrKeysIndexString, arrFreqStrings, arrWidthStrings, arrGainStrings;
		var arrAllVowelKeys, arrKeys, vowel, register, data, arrFreqs, arrWidths, arrGains;

		arrAllVowelKeys = [];
		['child','woman', 'man',].do({arg register;
			['beat', 'bit','bet','bat','part','pot','boot','book','but','pert'].do({arg vowel;
				arrAllVowelKeys = arrAllVowelKeys.add([vowel,register]);
			});
		});
		['soprano','alto', 'tenor', 'bass', 'counterTenor', ].do({arg register;
			['a','e', 'i', 'o', 'u', ].do({arg vowel;
				arrAllVowelKeys = arrAllVowelKeys.add([vowel,register]);
			});
		});
		arrKeys = arrAllVowelKeys[this.getSynthArgSpec(arrKeysIndexString)];
		vowel = arrKeys[0];
		register = arrKeys[1];
		data = classData.formLib[vowel, register];
		arrFreqs =  data[\freq];
		arrWidths =  data[\bw];
		arrGains =  data[\db];
		arrFreqStrings.do({arg item, i;
			this.setSynthValue(item, classData.freqSpec.unmap(arrFreqs[i]));
		});
		arrWidthStrings.do({arg item, i;
			this.setSynthValue(item, classData.bandwidthSpec.unmap(arrWidths[i]));
		});
		arrGainStrings.do({arg item, i;
			this.setSynthValue(item, classData.gainSpec.unmap(arrGains[i].dbamp));
		});
		system.showViewIfModDisplay(this);
	}

	getNoteArray {
		var arrScaleSpec, scaleRoot, noteMin, noteMax, arrScaleNotes;
		// Generate array of notes from chord, mode, scale
		arrScaleSpec = TXScale.arrScaleNotes.at(this.getSynthArgSpec("i_noteListTypeInd"));
		scaleRoot = this.getSynthArgSpec("i_noteListRoot");
		noteMin = this.getSynthArgSpec("i_noteListMin");
		noteMax = this.getSynthArgSpec("i_noteListMax");
		arrScaleNotes = [];
		13.do({arg octave;
			arrScaleSpec.do({arg item, i;
				arrScaleNotes = arrScaleNotes.add((octave * 12) + scaleRoot + item);
			});
		});
		arrScaleNotes = arrScaleNotes.select({arg item, i; ((item >= noteMin) and: (item <= noteMax)); });
		this.setSynthArgSpec("i_noteListSize", arrScaleNotes.size);
		if (arrScaleNotes.size == 0, {
			arrScaleNotes = [noteMin];
		});
		^arrScaleNotes;
	}

	getNoteTotalText {
		var noteListSize, outText;
		noteListSize = this.getSynthArgSpec("i_noteListSize");
		if (noteListSize == 0, {
			outText = "ERROR: No notes in note list - need to widen range ";
		}, {
			outText = "Total no. of notes:  " ++ noteListSize.asString;
		});
		^outText;
	}

	updateSynth {
		this.getNoteArray;
		this.rebuildSynth;
		if (noteListTextView.notNil, {
			{noteListTextView.string = this.getNoteTotalText;}.defer();
		});
	}

}

