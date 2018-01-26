// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXEQMorphSt : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var arrSnapshots, arrSnapshotNames;
	var arrLoadSnapshotItems, arrStoreSnapshotItems;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "EQ Morph St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Morph X", 1, "modSliderXVal", 0],
			["Morph Y", 1, "modSliderYVal", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.freqSpec = ControlSpec(30, 10000, 'exp');
		classData.gainSpec = ControlSpec(-15, 15);
		classData.resSpec = ControlSpec(0.1, 1);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var filterFuncData;
		//	set  class specific instance variables
		displayOption = "showEQMorph";
		arrSnapshots = nil ! 30; // empty
		arrSnapshotNames = "[no data]"!30;
		filterFuncData = [
			["OFF",
				{arg inSound; inSound; }
			],
			["Para",
				{arg inSound, freq, rq, gain; BPeakEQ.ar(inSound, freq, rq, gain); }
			],
			["Lo-Sh",
				{arg inSound, freq, rq, gain; BLowShelf.ar(inSound, freq, 1, gain); }
			],
			["Hi-Sh",
				{arg inSound, freq, rq, gain; BHiShelf.ar(inSound, freq, 1, gain); }
			],
		];
		arrOptions = [0,0,2,1,1,1,1,3];
		arrOptionData = [
			[ // ind0 - Morph output
				["EQ Morph",
					{arg eqA, eqB, eqC, eqD, eqE, sliderXTotal, sliderYTotal, morphFunction;
						morphFunction.value(eqA, eqB, eqC, eqD, eqE, sliderXTotal, sliderYTotal);
					}
				],
				["Solo EQ A ",
					{arg eqA, eqB, eqC, eqD, eqE; eqA; }
				],
				["Solo EQ B ",
					{arg eqA, eqB, eqC, eqD, eqE; eqB; }
				],
				["Solo EQ C ",
					{arg eqA, eqB, eqC, eqD, eqE; eqC; }
				],
				["Solo EQ D ",
					{arg eqA, eqB, eqC, eqD, eqE; eqD; }
				],
				["Solo EQ E ",
					{arg eqA, eqB, eqC, eqD, eqE; eqE; }
				],
			],
			[ // ind1 - Morph type
				["2-D Morph - 4 EQs A - D",
					{arg eqA, eqB, eqC, eqD, eqE, sliderXTotal, sliderYTotal;
						var eqLeft, eqRight, eqOut;
						//		---eqs A-D on X/Y area:---
						//		eqA = top left / x=0,y=0
						//		eqB = top right / x=1,y=0
						//		eqC = bottom left / x=0,y=1
						//		eqD = bottom right / x=1,y=1
						eqLeft = (eqA * (1-sliderYTotal)) + (eqC * sliderYTotal);
						eqRight = (eqB * (1-sliderYTotal)) + (eqD * sliderYTotal);
						eqOut = (eqLeft * (1-sliderXTotal)) + (eqRight * sliderXTotal);
					}
				],
				["2-D Morph - 5 EQs A - E",
					{arg eqA, eqB, eqC, eqD, eqE, sliderXTotal, sliderYTotal;
						//		---eqs A-E on X/Y area:---
						//		eqA = top left / x=0,y=0
						//		eqB = top right / x=1,y=0
						//		eqC = bottom left / x=0,y=1
						//		eqD = bottom right / x=1,y=1
						//		eqE = centre / x=0.5,y=0.5
						var eqLeft, eqRight, eqsAtoD, middleBias;
						eqLeft = (eqA * (1-sliderYTotal)) + (eqC * sliderYTotal);
						eqRight = (eqB * (1-sliderYTotal)) + (eqD * sliderYTotal);
						eqsAtoD = (eqLeft * (1-sliderXTotal)) + (eqRight * sliderXTotal);
						middleBias =  ((1-((sliderYTotal-0.5).abs*2)) * (1-((sliderXTotal-0.5).abs*2))).sqrt;
						(eqsAtoD * (1 - middleBias) ) + (eqE * middleBias );
					}
				],
				["Linear Morph - 2 EQs A - B",
					{arg eqA, eqB, eqC, eqD, eqE, sliderXTotal, sliderYTotal;
						var total, holdScalar;
						total = 2;
						holdScalar = (total-1).reciprocal;
						[eqA, eqB].collect({arg item, i;
							item  *
							(1 - ((sliderXTotal .min((i+1) * holdScalar) .max((i-1) * holdScalar)
								- (i * holdScalar) ).abs * (total-1)) )
						}).sum;
					}
				],
				["Linear Morph - 3 EQs A - C",
					{arg eqA, eqB, eqC, eqD, eqE, sliderXTotal, sliderYTotal;
						var total, holdScalar;
						total = 3;
						holdScalar = (total-1).reciprocal;
						[eqA, eqB, eqC].collect({arg item, i;
							item  *
							(1 - ((sliderXTotal .min((i+1) * holdScalar) .max((i-1) * holdScalar)
								- (i * holdScalar) ).abs * (total-1)) )
						}).sum;
					}
				],
				["Linear Morph - 4 EQs A - D",
					{arg eqA, eqB, eqC, eqD, eqE, sliderXTotal, sliderYTotal;
						var total, holdScalar;
						total = 4;
						holdScalar = (total-1).reciprocal;
						[eqA, eqB, eqC, eqD].collect({arg item, i;
							item  *
							(1 - ((sliderXTotal .min((i+1) * holdScalar) .max((i-1) * holdScalar)
								- (i * holdScalar) ).abs * (total-1)) )
						}).sum;
					}
				],
				["Linear Morph - 5 EQs A - E",
					{arg eqA, eqB, eqC, eqD, eqE, sliderXTotal, sliderYTotal;
						var total, holdScalar;
						total = 5;
						holdScalar = (total-1).reciprocal;
						[eqA, eqB, eqC, eqD, eqE].collect({arg item, i;
							item  *
							(1 - ((sliderXTotal .min((i+1) * holdScalar) .max((i-1) * holdScalar)
								- (i * holdScalar) ).abs * (total-1)) )
						}).sum;
					}
				],
			],
			// ind2 - filter 1
			filterFuncData,
			// ind3 - filter 2
			filterFuncData,
			// ind4 - filter 3
			filterFuncData,
			// ind5 - filter 4
			filterFuncData,
			// ind6 - filter 5
			filterFuncData,
			// ind7 - filter 6
			filterFuncData,
		];
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],

			["eqAFilt1Freq", classData.freqSpec.unmap(60), defLagTime],
			["eqAFilt2Freq", classData.freqSpec.unmap(100), defLagTime],
			["eqAFilt3Freq", classData.freqSpec.unmap(250), defLagTime],
			["eqAFilt4Freq", classData.freqSpec.unmap(750), defLagTime],
			["eqAFilt5Freq", classData.freqSpec.unmap(2000), defLagTime],
			["eqAFilt6Freq", classData.freqSpec.unmap(5000), defLagTime],
			["eqAFilt1Gain", 0.5, defLagTime],
			["eqAFilt2Gain", 0.5, defLagTime],
			["eqAFilt3Gain", 0.5, defLagTime],
			["eqAFilt4Gain", 0.5, defLagTime],
			["eqAFilt5Gain", 0.5, defLagTime],
			["eqAFilt6Gain", 0.5, defLagTime],
			["eqAFilt1Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqAFilt2Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqAFilt3Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqAFilt4Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqAFilt5Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqAFilt6Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqBFilt1Freq", classData.freqSpec.unmap(60), defLagTime],
			["eqBFilt2Freq", classData.freqSpec.unmap(100), defLagTime],
			["eqBFilt3Freq", classData.freqSpec.unmap(250), defLagTime],
			["eqBFilt4Freq", classData.freqSpec.unmap(750), defLagTime],
			["eqBFilt5Freq", classData.freqSpec.unmap(2000), defLagTime],
			["eqBFilt6Freq", classData.freqSpec.unmap(5000), defLagTime],
			["eqBFilt1Gain", 0.5, defLagTime],
			["eqBFilt2Gain", 0.5, defLagTime],
			["eqBFilt3Gain", 0.5, defLagTime],
			["eqBFilt4Gain", 0.5, defLagTime],
			["eqBFilt5Gain", 0.5, defLagTime],
			["eqBFilt6Gain", 0.5, defLagTime],
			["eqBFilt1Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqBFilt2Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqBFilt3Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqBFilt4Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqBFilt5Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqBFilt6Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqCFilt1Freq", classData.freqSpec.unmap(60), defLagTime],
			["eqCFilt2Freq", classData.freqSpec.unmap(100), defLagTime],
			["eqCFilt3Freq", classData.freqSpec.unmap(250), defLagTime],
			["eqCFilt4Freq", classData.freqSpec.unmap(750), defLagTime],
			["eqCFilt5Freq", classData.freqSpec.unmap(2000), defLagTime],
			["eqCFilt6Freq", classData.freqSpec.unmap(5000), defLagTime],
			["eqCFilt1Gain", 0.5, defLagTime],
			["eqCFilt2Gain", 0.5, defLagTime],
			["eqCFilt3Gain", 0.5, defLagTime],
			["eqCFilt4Gain", 0.5, defLagTime],
			["eqCFilt5Gain", 0.5, defLagTime],
			["eqCFilt6Gain", 0.5, defLagTime],
			["eqCFilt1Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqCFilt2Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqCFilt3Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqCFilt4Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqCFilt5Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqCFilt6Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqDFilt1Freq", classData.freqSpec.unmap(60), defLagTime],
			["eqDFilt2Freq", classData.freqSpec.unmap(100), defLagTime],
			["eqDFilt3Freq", classData.freqSpec.unmap(250), defLagTime],
			["eqDFilt4Freq", classData.freqSpec.unmap(750), defLagTime],
			["eqDFilt5Freq", classData.freqSpec.unmap(2000), defLagTime],
			["eqDFilt6Freq", classData.freqSpec.unmap(5000), defLagTime],
			["eqDFilt1Gain", 0.5, defLagTime],
			["eqDFilt2Gain", 0.5, defLagTime],
			["eqDFilt3Gain", 0.5, defLagTime],
			["eqDFilt4Gain", 0.5, defLagTime],
			["eqDFilt5Gain", 0.5, defLagTime],
			["eqDFilt6Gain", 0.5, defLagTime],
			["eqDFilt1Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqDFilt2Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqDFilt3Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqDFilt4Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqDFilt5Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqDFilt6Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqEFilt1Freq", classData.freqSpec.unmap(60), defLagTime],
			["eqEFilt2Freq", classData.freqSpec.unmap(100), defLagTime],
			["eqEFilt3Freq", classData.freqSpec.unmap(250), defLagTime],
			["eqEFilt4Freq", classData.freqSpec.unmap(750), defLagTime],
			["eqEFilt5Freq", classData.freqSpec.unmap(2000), defLagTime],
			["eqEFilt6Freq", classData.freqSpec.unmap(5000), defLagTime],
			["eqEFilt1Gain", 0.5, defLagTime],
			["eqEFilt2Gain", 0.5, defLagTime],
			["eqEFilt3Gain", 0.5, defLagTime],
			["eqEFilt4Gain", 0.5, defLagTime],
			["eqEFilt5Gain", 0.5, defLagTime],
			["eqEFilt6Gain", 0.5, defLagTime],
			["eqEFilt1Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqEFilt2Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqEFilt3Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqEFilt4Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqEFilt5Res", classData.resSpec.unmap(0.5), defLagTime],
			["eqEFilt6Res", classData.resSpec.unmap(0.5), defLagTime],

			["sliderXVal", 0, 0],
			["sliderYVal", 0, 0],
			["wetDryMix", 1.0, defLagTime],
			["modSliderXVal", 0, 0],
			["modSliderYVal", 0, 0],
			["modWetDryMix", 0, defLagTime],

			// N.B. the args below aren't used in the synthdef, just stored as synth args for convenience
			["holdString", " "],
		];
		synthDefFunc = {
			arg in, out,

			eqAFilt1Freq, eqAFilt2Freq, eqAFilt3Freq, eqAFilt4Freq, eqAFilt5Freq, eqAFilt6Freq,
			eqAFilt1Gain, eqAFilt2Gain, eqAFilt3Gain, eqAFilt4Gain, eqAFilt5Gain, eqAFilt6Gain,
			eqAFilt1Res, eqAFilt2Res, eqAFilt3Res, eqAFilt4Res, eqAFilt5Res, eqAFilt6Res,
			eqBFilt1Freq, eqBFilt2Freq, eqBFilt3Freq, eqBFilt4Freq, eqBFilt5Freq, eqBFilt6Freq,
			eqBFilt1Gain, eqBFilt2Gain, eqBFilt3Gain, eqBFilt4Gain, eqBFilt5Gain, eqBFilt6Gain,
			eqBFilt1Res, eqBFilt2Res, eqBFilt3Res, eqBFilt4Res, eqBFilt5Res, eqBFilt6Res,
			eqCFilt1Freq, eqCFilt2Freq, eqCFilt3Freq, eqCFilt4Freq, eqCFilt5Freq, eqCFilt6Freq,
			eqCFilt1Gain, eqCFilt2Gain, eqCFilt3Gain, eqCFilt4Gain, eqCFilt5Gain, eqCFilt6Gain,
			eqCFilt1Res, eqCFilt2Res, eqCFilt3Res, eqCFilt4Res, eqCFilt5Res, eqCFilt6Res,
			eqDFilt1Freq, eqDFilt2Freq, eqDFilt3Freq, eqDFilt4Freq, eqDFilt5Freq, eqDFilt6Freq,
			eqDFilt1Gain, eqDFilt2Gain, eqDFilt3Gain, eqDFilt4Gain, eqDFilt5Gain, eqDFilt6Gain,
			eqDFilt1Res, eqDFilt2Res, eqDFilt3Res, eqDFilt4Res, eqDFilt5Res, eqDFilt6Res,
			eqEFilt1Freq, eqEFilt2Freq, eqEFilt3Freq, eqEFilt4Freq, eqEFilt5Freq, eqEFilt6Freq,
			eqEFilt1Gain, eqEFilt2Gain, eqEFilt3Gain, eqEFilt4Gain, eqEFilt5Gain, eqEFilt6Gain,
			eqEFilt1Res, eqEFilt2Res, eqEFilt3Res, eqEFilt4Res, eqEFilt5Res, eqEFilt6Res,

			sliderXVal, sliderYVal,  wetDryMix,
			modSliderXVal=0, modSliderYVal=0, modWetDryMix= 0;

			var arrFilterFuncs, outMorph, input, chain;
			var groupA = [eqAFilt1Freq, eqAFilt2Freq, eqAFilt3Freq, eqAFilt4Freq, eqAFilt5Freq, eqAFilt6Freq,
				eqAFilt1Gain, eqAFilt2Gain, eqAFilt3Gain, eqAFilt4Gain, eqAFilt5Gain, eqAFilt6Gain,
				eqAFilt1Res, eqAFilt2Res, eqAFilt3Res, eqAFilt4Res, eqAFilt5Res, eqAFilt6Res];
			var groupB = [eqBFilt1Freq, eqBFilt2Freq, eqBFilt3Freq, eqBFilt4Freq, eqBFilt5Freq, eqBFilt6Freq,
				eqBFilt1Gain, eqBFilt2Gain, eqBFilt3Gain, eqBFilt4Gain, eqBFilt5Gain, eqBFilt6Gain,
				eqBFilt1Res, eqBFilt2Res, eqBFilt3Res, eqBFilt4Res, eqBFilt5Res, eqBFilt6Res];
			var groupC = [eqCFilt1Freq, eqCFilt2Freq, eqCFilt3Freq, eqCFilt4Freq, eqCFilt5Freq, eqCFilt6Freq,
				eqCFilt1Gain, eqCFilt2Gain, eqCFilt3Gain, eqCFilt4Gain, eqCFilt5Gain, eqCFilt6Gain,
				eqCFilt1Res, eqCFilt2Res, eqCFilt3Res, eqCFilt4Res, eqCFilt5Res, eqCFilt6Res];
			var groupD = [eqDFilt1Freq, eqDFilt2Freq, eqDFilt3Freq, eqDFilt4Freq, eqDFilt5Freq, eqDFilt6Freq,
				eqDFilt1Gain, eqDFilt2Gain, eqDFilt3Gain, eqDFilt4Gain, eqDFilt5Gain, eqDFilt6Gain,
				eqDFilt1Res, eqDFilt2Res, eqDFilt3Res, eqDFilt4Res, eqDFilt5Res, eqDFilt6Res];
			var groupE = [eqEFilt1Freq, eqEFilt2Freq, eqEFilt3Freq, eqEFilt4Freq, eqEFilt5Freq, eqEFilt6Freq,
				eqEFilt1Gain, eqEFilt2Gain, eqEFilt3Gain, eqEFilt4Gain, eqEFilt5Gain, eqEFilt6Gain,
				eqEFilt1Res, eqEFilt2Res, eqEFilt3Res, eqEFilt4Res, eqEFilt5Res, eqEFilt6Res];

			var totSliderX = (sliderXVal + modSliderXVal).max(0).min(1);
			var totSliderY = (sliderYVal + modSliderYVal).max(0).min(1);
			var totWetDryMix = (wetDryMix + modWetDryMix).max(0).min(1);

			var funcMorphOut = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			var funcMorphType = arrOptionData.at(1).at(arrOptions.at(1)).at(1);
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(startEnv * InFeedback.ar(in, classData.noInChannels));

			outMorph = funcMorphOut.value(groupA, groupB, groupC, groupD, groupE,
					totSliderX, totSliderY, funcMorphType);
			arrFilterFuncs = [this.getSynthOption(2), this.getSynthOption(3), this.getSynthOption(4),
				this.getSynthOption(5), this.getSynthOption(6), this.getSynthOption(7)];
			chain = input;
			arrFilterFuncs.do({arg item, i;
				chain = item.value( chain, classData.freqSpec.map(outMorph[i]),
					classData.resSpec.map(outMorph[i+12]).reciprocal, classData.gainSpec.map(outMorph[i+6]));
			});
			chain = LeakDC.ar(chain);
			Out.ar(out, TXClean.ar(startEnv * chain * totWetDryMix) + (input * (1 - totWetDryMix)));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs(
			[
				["SynthOptionPopupPlusMinus", "Output", arrOptionData, 0, 230],
				["SpacerLine", 6],
				["TextBar", "*EQ bands 1-6", 80],
				["SynthOptionPopup", "EQ band 1", arrOptionData, 2, 60, nil, 0],
				["SynthOptionPopup", "EQ band  2", arrOptionData, 3, 60, nil, 0],
				["SynthOptionPopup", "EQ band  3", arrOptionData, 4, 60, nil, 0],
				["SynthOptionPopup", "EQ band  4", arrOptionData, 5, 60, nil, 0],
				["SynthOptionPopup", "EQ band  5", arrOptionData, 6, 60, nil, 0],
				["SynthOptionPopup", "EQ band  6", arrOptionData, 7, 60, nil, 0],
				["SynthOptionPopupPlusMinus", "Morph Type", arrOptionData, 1, 310,
					{this.buildGuiSpecArray; system.showView;}],
				["EZslider", "Morph X", ControlSpec(0, 1), "sliderXVal", nil, 450],
				["EZslider", "Morph y", ControlSpec(0, 1), "sliderYVal", nil, 450],
				["WetDryMixSlider"],
			]
			++ ["eqAFilt1Freq", "eqAFilt2Freq", "eqAFilt3Freq", "eqAFilt4Freq", "eqAFilt5Freq", "eqAFilt6Freq",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.freqSpec, item]
			})
			++ ["eqAFilt1Res", "eqAFilt2Res", "eqAFilt3Res", "eqAFilt4Res", "eqAFilt5Res", "eqAFilt6Res",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.resSpec, item]
			})
			++ ["eqAFilt1Gain", "eqAFilt2Gain", "eqAFilt3Gain", "eqAFilt4Gain", "eqAFilt5Gain", "eqAFilt6Gain",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.gainSpec, item]
			})
			++ ["eqBFilt1Freq", "eqBFilt2Freq", "eqBFilt3Freq", "eqBFilt4Freq", "eqBFilt5Freq", "eqBFilt6Freq",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.freqSpec, item]
			})
			++ ["eqBFilt1Res", "eqBFilt2Res", "eqBFilt3Res", "eqBFilt4Res", "eqBFilt5Res", "eqBFilt6Res",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.resSpec, item]
			})
			++ ["eqBFilt1Gain", "eqBFilt2Gain", "eqBFilt3Gain", "eqBFilt4Gain", "eqBFilt5Gain", "eqBFilt6Gain",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.gainSpec, item]
			})
			++ ["eqCFilt1Freq", "eqCFilt2Freq", "eqCFilt3Freq", "eqCFilt4Freq", "eqCFilt5Freq", "eqCFilt6Freq",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.freqSpec, item]
			})
			++ ["eqCFilt1Res", "eqCFilt2Res", "eqCFilt3Res", "eqCFilt4Res", "eqCFilt5Res", "eqCFilt6Res",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.resSpec, item]
			})
			++ ["eqCFilt1Gain", "eqCFilt2Gain", "eqCFilt3Gain", "eqCFilt4Gain", "eqCFilt5Gain", "eqCFilt6Gain",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.gainSpec, item]
			})
			++ ["eqDFilt1Freq", "eqDFilt2Freq", "eqDFilt3Freq", "eqDFilt4Freq", "eqDFilt5Freq", "eqDFilt6Freq",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.freqSpec, item]
			})
			++ ["eqDFilt1Res", "eqDFilt2Res", "eqDFilt3Res", "eqDFilt4Res", "eqDFilt5Res", "eqDFilt6Res",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.resSpec, item]
			})
			++ ["eqDFilt1Gain", "eqDFilt2Gain", "eqDFilt3Gain", "eqDFilt4Gain", "eqDFilt5Gain", "eqDFilt6Gain",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.gainSpec, item]
			})
			++ ["eqEFilt1Freq", "eqEFilt2Freq", "eqEFilt3Freq", "eqEFilt4Freq", "eqEFilt5Freq", "eqEFilt6Freq",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.freqSpec, item]
			})
			++ ["eqEFilt1Res", "eqEFilt2Res", "eqEFilt3Res", "eqEFilt4Res", "eqEFilt5Res", "eqEFilt6Res",]
			.collect({arg item, i;
				["EZsliderUnmapped", item, classData.resSpec, item]
			})
			++ ["eqEFilt1Gain", "eqEFilt2Gain", "eqEFilt3Gain", "eqEFilt4Gain", "eqEFilt5Gain", "eqEFilt6Gain",]
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
		guiSpecArray = [
			["ActionButton", "EQ Morph", {displayOption = "showEQMorph";
				arrOptions[0] = 0; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 90,
			TXColor.white, this.getButtonColour(displayOption == "showEQMorph")],
			["ActionButton", "EQ A", {displayOption = "showEQA";
				arrOptions[0] = 1; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 70,
			TXColor.white, this.getButtonColour(displayOption == "showEQA")],
			["ActionButton", "EQ B", {displayOption = "showEQB";
				arrOptions[0] = 2; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 70,
			TXColor.white, this.getButtonColour(displayOption == "showEQB")],
			["ActionButton", "EQ C", {displayOption = "showEQC";
				arrOptions[0] = 3; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 70,
			TXColor.white, this.getButtonColour(displayOption == "showEQC")],
			["ActionButton", "EQ D", {displayOption = "showEQD";
				arrOptions[0] = 4; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 70,
			TXColor.white, this.getButtonColour(displayOption == "showEQD")],
			["ActionButton", "EQ E", {displayOption = "showEQE";
				arrOptions[0] = 5; this.rebuildSynth;
				this.buildGuiSpecArray; system.showView;}, 70,
			TXColor.white, this.getButtonColour(displayOption == "showEQE")],
			["DividingLine"],
			["SpacerLine", 6],
			["SynthOptionPopupPlusMinus", "Output", arrOptionData, 0, 230],
			["SpacerLine", 6],
			["TextBar", "*EQ bands 1-6", 80],
			["SynthOptionPopup", "", arrOptionData, 2, 60, nil, 0],
			["SynthOptionPopup", "", arrOptionData, 3, 60, nil, 0],
			["SynthOptionPopup", "", arrOptionData, 4, 60, nil, 0],
			["SynthOptionPopup", "", arrOptionData, 5, 60, nil, 0],
			["SynthOptionPopup", "", arrOptionData, 6, 60, nil, 0],
			["SynthOptionPopup", "", arrOptionData, 7, 60, nil, 0],
			["DividingLine"],
			["SpacerLine", 6],
		];

		arrLoadSnapshotItems = ["Load Snapshot..."]
		++ 30.collect ({ arg item, i; "Load Snapshot " ++ (item+1).asString +"-"+ arrSnapshotNames[item]; });
		arrStoreSnapshotItems = ["Store Snapshot..."] ++ 30.collect ({ arg item, i; "Store Snapshot " ++ (item+1).asString; });

		if (displayOption == "showEQMorph", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Morph Type", arrOptionData, 1, 310,
					{this.buildGuiSpecArray; system.showView;}],
				["SpacerLine", 10],
			];
			if (arrOptions[1] < 2, {
				guiSpecArray = guiSpecArray ++[
					["TextBarLeft", "EQ C", 80, 20, TXColor.paleYellow],
					["Spacer", 250],
					["TextBarLeft", "EQ D", 80, 20, TXColor.paleYellow],
					["SpacerLine", 2],
					["TX2DSlider", "Morph X-Y", ControlSpec(0, 1),
						"sliderXVal", "sliderYVal", nil, 250, 334, true],
					["SpacerLine", 2],
				];
			});
			if (arrOptions[1] == 0, {
				guiSpecArray = guiSpecArray ++[
					["TextBarLeft", "EQ A", 80, 20, TXColor.paleYellow],
					["Spacer", 250],
					["TextBarLeft", "EQ B", 80, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 1, {
				guiSpecArray = guiSpecArray ++[
					["TextBarLeft", "EQ A", 80, 20, TXColor.paleYellow],
					["Spacer", 20],
					["TextBarLeft", "EQ E is in the centre of the box", 200,
						20, TXColor.paleYellow],
					["Spacer", 20],
					["TextBarLeft", "EQ B", 80, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] > 1, {
				guiSpecArray = guiSpecArray ++[
					//["SpacerLine", 6],
					["Spacer", 60],
					["TextBarLeft", "EQ A", 69, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 2, {
				guiSpecArray = guiSpecArray ++[
					["Spacer", 212],
					["TextBarLeft", "EQ B", 69, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 3, {
				guiSpecArray = guiSpecArray ++[
					["Spacer", 69],
					["TextBarLeft", "EQ B", 69, 20, TXColor.paleYellow],
					["Spacer", 69],
					["TextBarLeft", "EQ C", 69, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 4, {
				guiSpecArray = guiSpecArray ++[
					["Spacer", 21],
					["TextBarLeft", "EQ B", 69, 20, TXColor.paleYellow],
					["Spacer", 21],
					["TextBarLeft", "EQ C", 69, 20, TXColor.paleYellow],
					["Spacer", 21],
					["TextBarLeft", "EQ D", 69, 20, TXColor.paleYellow],
				];
			});
			if (arrOptions[1] == 5, {
				guiSpecArray = guiSpecArray ++[
					["TextBarLeft", "EQ B", 69, 20, TXColor.paleYellow],
					["TextBarLeft", "EQ C", 69, 20, TXColor.paleYellow],
					["TextBarLeft", "EQ D", 69, 20, TXColor.paleYellow],
					["TextBarLeft", "EQ E", 69, 20, TXColor.paleYellow],
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
				["SpacerLine", 6],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showEQA", {
			guiSpecArray = guiSpecArray ++[
				["Spacer", 36],
				["TXMultiKnobNoUnmap", "Freq",
					["eqAFilt1Freq", "eqAFilt2Freq", "eqAFilt3Freq", "eqAFilt4Freq", "eqAFilt5Freq", "eqAFilt6Freq",],
					6, classData.freqSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqAFilt1Res", "eqAFilt2Res", "eqAFilt3Res", "eqAFilt4Res", "eqAFilt5Res", "eqAFilt6Res",]);}, 36],
				["TXMultiKnobNoUnmap", "Res",
					["eqAFilt1Res", "eqAFilt2Res", "eqAFilt3Res", "eqAFilt4Res", "eqAFilt5Res", "eqAFilt6Res",],
					6, classData.resSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqAFilt1Gain", "eqAFilt2Gain", "eqAFilt3Gain", "eqAFilt4Gain", "eqAFilt5Gain", "eqAFilt6Gain",]);}, 36],
				["TXMultiKnobNoUnmap", "Gain",
					["eqAFilt1Gain", "eqAFilt2Gain", "eqAFilt3Gain", "eqAFilt4Gain", "eqAFilt5Gain", "eqAFilt6Gain",],
					6, classData.gainSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionPopup", arrStoreSnapshotItems,
					{arg holdView; if (holdView.value>0,{
						this.storeSnapshot(
							["eqAFilt1Freq", "eqAFilt2Freq", "eqAFilt3Freq", "eqAFilt4Freq", "eqAFilt5Freq", "eqAFilt6Freq",
								"eqAFilt1Gain", "eqAFilt2Gain", "eqAFilt3Gain", "eqAFilt4Gain", "eqAFilt5Gain", "eqAFilt6Gain",
								"eqAFilt1Res", "eqAFilt2Res", "eqAFilt3Res", "eqAFilt4Res", "eqAFilt5Res", "eqAFilt6Res",],
							holdView.value-1, this.getSynthArgSpec("holdString"););
						this.setSynthArgSpec("holdString", " ");
						system.flagGuiUpd;})},
					158, TXColor.white, TXColor.sysGuiCol2],
				["TXTextBox", "Name", "holdString",nil, 210, 60],
				["ActionPopup", arrLoadSnapshotItems,
					{arg holdView; if (holdView.value>0,{this.loadSnapshot(
						["eqAFilt1Freq", "eqAFilt2Freq", "eqAFilt3Freq", "eqAFilt4Freq", "eqAFilt5Freq", "eqAFilt6Freq",
							"eqAFilt1Gain", "eqAFilt2Gain", "eqAFilt3Gain", "eqAFilt4Gain", "eqAFilt5Gain", "eqAFilt6Gain",
							"eqAFilt1Res", "eqAFilt2Res", "eqAFilt3Res", "eqAFilt4Res", "eqAFilt5Res", "eqAFilt6Res",],
						holdView.value-1); }); },
					438, TXColor.white, TXColor.sysGuiCol1],
				["DividingLine"],
				["SpacerLine", 6],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showEQB", {
			guiSpecArray = guiSpecArray ++[
				["Spacer", 36],
				["TXMultiKnobNoUnmap", "Freq",
					["eqBFilt1Freq", "eqBFilt2Freq", "eqBFilt3Freq", "eqBFilt4Freq", "eqBFilt5Freq", "eqBFilt6Freq",],
					6, classData.freqSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqBFilt1Res", "eqBFilt2Res", "eqBFilt3Res", "eqBFilt4Res", "eqBFilt5Res", "eqBFilt6Res",]);}, 36],
				["TXMultiKnobNoUnmap", "Res",
					["eqBFilt1Res", "eqBFilt2Res", "eqBFilt3Res", "eqBFilt4Res", "eqBFilt5Res", "eqBFilt6Res",],
					6, classData.resSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqBFilt1Gain", "eqBFilt2Gain", "eqBFilt3Gain", "eqBFilt4Gain", "eqBFilt5Gain", "eqBFilt6Gain",]);}, 36],
				["TXMultiKnobNoUnmap", "Gain",
					["eqBFilt1Gain", "eqBFilt2Gain", "eqBFilt3Gain", "eqBFilt4Gain", "eqBFilt5Gain", "eqBFilt6Gain",],
					6, classData.gainSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionPopup", arrStoreSnapshotItems,
					{arg holdView; if (holdView.value>0,{
						this.storeSnapshot(
							["eqBFilt1Freq", "eqBFilt2Freq", "eqBFilt3Freq", "eqBFilt4Freq", "eqBFilt5Freq", "eqBFilt6Freq",
								"eqBFilt1Gain", "eqBFilt2Gain", "eqBFilt3Gain", "eqBFilt4Gain", "eqBFilt5Gain", "eqBFilt6Gain",
								"eqBFilt1Res", "eqBFilt2Res", "eqBFilt3Res", "eqBFilt4Res", "eqBFilt5Res", "eqBFilt6Res",],
							holdView.value-1, this.getSynthArgSpec("holdString"););
						this.setSynthArgSpec("holdString", " ");
						system.flagGuiUpd;})},
					158, TXColor.white, TXColor.sysGuiCol2],
				["TXTextBox", "Name", "holdString",nil, 210, 60],
				["ActionPopup", arrLoadSnapshotItems,
					{arg holdView; if (holdView.value>0,{this.loadSnapshot(
						["eqBFilt1Freq", "eqBFilt2Freq", "eqBFilt3Freq", "eqBFilt4Freq", "eqBFilt5Freq", "eqBFilt6Freq",
							"eqBFilt1Gain", "eqBFilt2Gain", "eqBFilt3Gain", "eqBFilt4Gain", "eqBFilt5Gain", "eqBFilt6Gain",
							"eqBFilt1Res", "eqBFilt2Res", "eqBFilt3Res", "eqBFilt4Res", "eqBFilt5Res", "eqBFilt6Res",],
						holdView.value-1); }); },
					438, TXColor.white, TXColor.sysGuiCol1],
				["DividingLine"],
				["SpacerLine", 6],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showEQC", {
			guiSpecArray = guiSpecArray ++[
				["Spacer", 36],
				["TXMultiKnobNoUnmap", "Freq",
					["eqCFilt1Freq", "eqCFilt2Freq", "eqCFilt3Freq", "eqCFilt4Freq", "eqCFilt5Freq", "eqCFilt6Freq",],
					6, classData.freqSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqCFilt1Res", "eqCFilt2Res", "eqCFilt3Res", "eqCFilt4Res", "eqCFilt5Res", "eqCFilt6Res",]);}, 36],
				["TXMultiKnobNoUnmap", "Res",
					["eqCFilt1Res", "eqCFilt2Res", "eqCFilt3Res", "eqCFilt4Res", "eqCFilt5Res", "eqCFilt6Res",],
					6, classData.resSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqCFilt1Gain", "eqCFilt2Gain", "eqCFilt3Gain", "eqCFilt4Gain", "eqCFilt5Gain", "eqCFilt6Gain",]);}, 36],
				["TXMultiKnobNoUnmap", "Gain",
					["eqCFilt1Gain", "eqCFilt2Gain", "eqCFilt3Gain", "eqCFilt4Gain", "eqCFilt5Gain", "eqCFilt6Gain",],
					6, classData.gainSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionPopup", arrStoreSnapshotItems,
					{arg holdView; if (holdView.value>0,{
						this.storeSnapshot(
							["eqCFilt1Freq", "eqCFilt2Freq", "eqCFilt3Freq", "eqCFilt4Freq", "eqCFilt5Freq", "eqCFilt6Freq",
								"eqCFilt1Gain", "eqCFilt2Gain", "eqCFilt3Gain", "eqCFilt4Gain", "eqCFilt5Gain", "eqCFilt6Gain",
								"eqCFilt1Res", "eqCFilt2Res", "eqCFilt3Res", "eqCFilt4Res", "eqCFilt5Res", "eqCFilt6Res",],
							holdView.value-1, this.getSynthArgSpec("holdString"););
						this.setSynthArgSpec("holdString", " ");
						system.flagGuiUpd;})},
					158, TXColor.white, TXColor.sysGuiCol2],
				["TXTextBox", "Name", "holdString",nil, 210, 60],
				["ActionPopup", arrLoadSnapshotItems,
					{arg holdView; if (holdView.value>0,{this.loadSnapshot(
						["eqCFilt1Freq", "eqCFilt2Freq", "eqCFilt3Freq", "eqCFilt4Freq", "eqCFilt5Freq", "eqCFilt6Freq",
							"eqCFilt1Gain", "eqCFilt2Gain", "eqCFilt3Gain", "eqCFilt4Gain", "eqCFilt5Gain", "eqCFilt6Gain",
							"eqCFilt1Res", "eqCFilt2Res", "eqCFilt3Res", "eqCFilt4Res", "eqCFilt5Res", "eqCFilt6Res",],
						holdView.value-1); }); },
					438, TXColor.white, TXColor.sysGuiCol1],
				["DividingLine"],
				["SpacerLine", 6],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showEQD", {
			guiSpecArray = guiSpecArray ++[
				["Spacer", 36],
				["TXMultiKnobNoUnmap", "Freq",
					["eqDFilt1Freq", "eqDFilt2Freq", "eqDFilt3Freq", "eqDFilt4Freq", "eqDFilt5Freq", "eqDFilt6Freq",],
					6, classData.freqSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqDFilt1Res", "eqDFilt2Res", "eqDFilt3Res", "eqDFilt4Res", "eqDFilt5Res", "eqDFilt6Res",]);}, 36],
				["TXMultiKnobNoUnmap", "Res",
					["eqDFilt1Res", "eqDFilt2Res", "eqDFilt3Res", "eqDFilt4Res", "eqDFilt5Res", "eqDFilt6Res",],
					6, classData.resSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqDFilt1Gain", "eqDFilt2Gain", "eqDFilt3Gain", "eqDFilt4Gain", "eqDFilt5Gain", "eqDFilt6Gain",]);}, 36],
				["TXMultiKnobNoUnmap", "Gain",
					["eqDFilt1Gain", "eqDFilt2Gain", "eqDFilt3Gain", "eqDFilt4Gain", "eqDFilt5Gain", "eqDFilt6Gain",],
					6, classData.gainSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionPopup", arrStoreSnapshotItems,
					{arg holdView; if (holdView.value>0,{
						this.storeSnapshot(
							["eqDFilt1Freq", "eqDFilt2Freq", "eqDFilt3Freq", "eqDFilt4Freq", "eqDFilt5Freq", "eqDFilt6Freq",
								"eqDFilt1Gain", "eqDFilt2Gain", "eqDFilt3Gain", "eqDFilt4Gain", "eqDFilt5Gain", "eqDFilt6Gain",
								"eqDFilt1Res", "eqDFilt2Res", "eqDFilt3Res", "eqDFilt4Res", "eqDFilt5Res", "eqDFilt6Res",],
							holdView.value-1, this.getSynthArgSpec("holdString"););
						this.setSynthArgSpec("holdString", " ");
						system.flagGuiUpd;})},
					158, TXColor.white, TXColor.sysGuiCol2],
				["TXTextBox", "Name", "holdString",nil, 210, 60],
				["ActionPopup", arrLoadSnapshotItems,
					{arg holdView; if (holdView.value>0,{this.loadSnapshot(
						["eqDFilt1Freq", "eqDFilt2Freq", "eqDFilt3Freq", "eqDFilt4Freq", "eqDFilt5Freq", "eqDFilt6Freq",
							"eqDFilt1Gain", "eqDFilt2Gain", "eqDFilt3Gain", "eqDFilt4Gain", "eqDFilt5Gain", "eqDFilt6Gain",
							"eqDFilt1Res", "eqDFilt2Res", "eqDFilt3Res", "eqDFilt4Res", "eqDFilt5Res", "eqDFilt6Res",],
						holdView.value-1); }); },
					438, TXColor.white, TXColor.sysGuiCol1],
				["DividingLine"],
				["SpacerLine", 6],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showEQE", {
			guiSpecArray = guiSpecArray ++[
				["Spacer", 36],
				["TXMultiKnobNoUnmap", "Freq",
					["eqEFilt1Freq", "eqEFilt2Freq", "eqEFilt3Freq", "eqEFilt4Freq", "eqEFilt5Freq", "eqEFilt6Freq",],
					6, classData.freqSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqEFilt1Res", "eqEFilt2Res", "eqEFilt3Res", "eqEFilt4Res", "eqEFilt5Res", "eqEFilt6Res",]);}, 36],
				["TXMultiKnobNoUnmap", "Res",
					["eqEFilt1Res", "eqEFilt2Res", "eqEFilt3Res", "eqEFilt4Res", "eqEFilt5Res", "eqEFilt6Res",],
					6, classData.resSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all", {this.copy1ToAll(
					["eqEFilt1Gain", "eqEFilt2Gain", "eqEFilt3Gain", "eqEFilt4Gain", "eqEFilt5Gain", "eqEFilt6Gain",]);}, 36],
				["TXMultiKnobNoUnmap", "Gain",
					["eqEFilt1Gain", "eqEFilt2Gain", "eqEFilt3Gain", "eqEFilt4Gain", "eqEFilt5Gain", "eqEFilt6Gain",],
					6, classData.gainSpec, nil, nil, 60],
				["SpacerLine", 6],
				["ActionPopup", arrStoreSnapshotItems,
					{arg holdView; if (holdView.value>0,{
						this.storeSnapshot(
							["eqEFilt1Freq", "eqEFilt2Freq", "eqEFilt3Freq", "eqEFilt4Freq", "eqEFilt5Freq", "eqEFilt6Freq",
								"eqEFilt1Gain", "eqEFilt2Gain", "eqEFilt3Gain", "eqEFilt4Gain", "eqEFilt5Gain", "eqEFilt6Gain",
								"eqEFilt1Res", "eqEFilt2Res", "eqEFilt3Res", "eqEFilt4Res", "eqEFilt5Res", "eqEFilt6Res",],
							holdView.value-1, this.getSynthArgSpec("holdString"););
						this.setSynthArgSpec("holdString", " ");
						system.flagGuiUpd;})},
					158, TXColor.white, TXColor.sysGuiCol2],
				["TXTextBox", "Name", "holdString",nil, 210, 60],
				["ActionPopup", arrLoadSnapshotItems,
					{arg holdView; if (holdView.value>0,{this.loadSnapshot(
						["eqEFilt1Freq", "eqEFilt2Freq", "eqEFilt3Freq", "eqEFilt4Freq", "eqEFilt5Freq", "eqEFilt6Freq",
							"eqEFilt1Gain", "eqEFilt2Gain", "eqEFilt3Gain", "eqEFilt4Gain", "eqEFilt5Gain", "eqEFilt6Gain",
							"eqEFilt1Res", "eqEFilt2Res", "eqEFilt3Res", "eqEFilt4Res", "eqEFilt5Res", "eqEFilt6Res",],
						holdView.value-1); }); },
					438, TXColor.white, TXColor.sysGuiCol1],
				["DividingLine"],
				["SpacerLine", 6],
				["WetDryMixSlider"],
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

	extraSaveData {
		^[arrSnapshots, arrSnapshotNames];
	}
	loadExtraData {arg argData;
		arrSnapshots = argData[0];
		arrSnapshotNames = argData[1] ?? (" "!30);
		this.buildGuiSpecArray;
		system.showViewIfModDisplay(this);
	}

	loadSnapshot { arg arrStrings, snapshotNum;
		var arrVals;
		arrVals = arrSnapshots[snapshotNum];
		if (arrVals.notNil, {
			arrStrings.do({ arg item, i; this.setSynthValue(item, arrVals[i]);});
			// update view
			system.showViewIfModDisplay(this);
		});
	}

	storeSnapshot { arg arrStrings, snapshotNum, nameString;
		var arrVals;
		arrVals = arrStrings.collect({ arg item; this.getSynthArgSpec(item);});
		arrSnapshots[snapshotNum] = arrVals;
		if (nameString.isNil or: (nameString == " "), {nameString = "Stored " ++ (snapshotNum+1).asString});
		arrSnapshotNames[snapshotNum] = nameString;
		this.buildGuiSpecArray;
		// update view
		system.showViewIfModDisplay(this);
	}


}

