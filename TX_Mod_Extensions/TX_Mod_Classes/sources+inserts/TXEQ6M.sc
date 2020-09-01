// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXEQ6M : TXModuleBase {		// 6-band EQ

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "EQ6M";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Low Shelf Freq", 1, "modLowShelfFreq", 0],
			["Para 1 Freq", 1, "modParaFreq1", 0],
			["Para 2 Freq", 1, "modParaFreq2", 0],
			["Para 3 Freq", 1, "modParaFreq3", 0],
			["Para 4 Freq", 1, "modParaFreq4", 0],
			["Hi Shelf Freq", 1, "modHiShelfFreq", 0],
			["Low Shelf Gain", 1, "modLowShelfGain", 0],
			["Para 1 Gain", 1, "modParaGain1", 0],
			["Para 2 Gain", 1, "modParaGain2", 0],
			["Para 3 Gain", 1, "modParaGain3", 0],
			["Para 4 Gain", 1, "modParaGain4", 0],
			["Hi Shelf Gain", 1, "modHiShelfGain", 0],
			["Para 1 Res", 1, "modParaRes1", 0],
			["Para 2 Res", 1, "modParaRes2", 0],
			["Para 3 Res", 1, "modParaRes3", 0],
			["Para 4 Res", 1, "modParaRes4", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.freqSpec = ControlSpec(30, 10000, 'exp');
		classData.gainSpec = ControlSpec(-6, 6);
		classData.resSpec = ControlSpec(0.1, 1);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrOptions = [1, 1, 1, 1, 1, 1,];
		arrOptionData = [
			[
				["Low Shelf OFF",
					{arg inSound; inSound; }
				],
				["Low Shelf ON",
					{arg inSound, freq, rq, gain; BLowShelf.ar(inSound, freq, 1, gain); }
				],
			],
			[
				["Para 1 OFF",
					{arg inSound; inSound; }
				],
				["Para 1 ON",
					{arg inSound, freq, rq, gain; BPeakEQ.ar(inSound, freq, rq, gain); }
				],
			],
			[
				["Para 2 OFF",
					{arg inSound; inSound; }
				],
				["Para 2 ON",
					{arg inSound, freq, rq, gain; BPeakEQ.ar(inSound, freq, rq, gain); }
				],
			],
			[
				["Para 3 OFF",
					{arg inSound; inSound; }
				],
				["Para 3 ON",
					{arg inSound, freq, rq, gain; BPeakEQ.ar(inSound, freq, rq, gain); }
				],
			],
			[
				["Para 4 OFF",
					{arg inSound; inSound; }
				],
				["Para 4 ON",
					{arg inSound, freq, rq, gain; BPeakEQ.ar(inSound, freq, rq, gain); }
				],
			],
			[
				["Hi Shelf OFF",
					{arg inSound; inSound; }
				],
				["Hi Shelf ON",
					{arg inSound, freq, rq, gain; BHiShelf.ar(inSound, freq, 1, gain); }
				],
			],
		];
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["lowShelfFreq", classData.freqSpec.unmap(60), defLagTime],
			["para1Freq", classData.freqSpec.unmap(100), defLagTime],
			["para2Freq", classData.freqSpec.unmap(250), defLagTime],
			["para3Freq", classData.freqSpec.unmap(1000), defLagTime],
			["para4Freq", classData.freqSpec.unmap(3500), defLagTime],
			["hiShelfFreq", classData.freqSpec.unmap(6000), defLagTime],
			["lowShelfGain", 0.5, defLagTime],
			["para1Gain", 0.5, defLagTime],
			["para2Gain", 0.5, defLagTime],
			["para3Gain", 0.5, defLagTime],
			["para4Gain", 0.5, defLagTime],
			["hiShelfGain", 0.5, defLagTime],
			["para1Res", classData.resSpec.unmap(0.5), defLagTime],
			["para2Res", classData.resSpec.unmap(0.5), defLagTime],
			["para3Res", classData.resSpec.unmap(0.5), defLagTime],
			["para4Res", classData.resSpec.unmap(0.5), defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modLowShelfFreq", 0, defLagTime],
			["modParaFreq1", 0, defLagTime],
			["modParaFreq2", 0, defLagTime],
			["modParaFreq3", 0, defLagTime],
			["modParaFreq4", 0, defLagTime],
			["modHiShelfFreq", 0, defLagTime],
			["modLowShelfGain", 0, defLagTime],
			["modParaGain1", 0, defLagTime],
			["modParaGain2", 0, defLagTime],
			["modParaGain3", 0, defLagTime],
			["modParaGain4", 0, defLagTime],
			["modHiShelfGain", 0, defLagTime],
			["modParaRes1", 0, defLagTime],
			["modParaRes2", 0, defLagTime],
			["modParaRes3", 0, defLagTime],
			["modParaRes4", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		synthDefFunc = {
			arg in, out,
			lowShelfFreq, para1Freq, para2Freq, para3Freq, para4Freq, hiShelfFreq,
			lowShelfGain, para1Gain, para2Gain, para3Gain, para4Gain, hiShelfGain,
			para1Res, para2Res, para3Res, para4Res,
			wetDryMix,
			modLowShelfFreq= 0, modPara1Freq= 0, modPara2Freq= 0, modPara3Freq= 0, modPara4Freq= 0, modHiShelfFreq= 0,
			modLowShelfGain= 0, modPara1Gain= 0, modPara2Gain= 0, modPara3Gain= 0, modPara4Gain= 0, modHiShelfGain= 0,
			modPara1Res= 0, modPara2Res= 0, modPara3Res= 0, modPara4Res= 0,
			modWetDryMix= 0;

			var totLowShelfFreq = (lowShelfFreq + modLowShelfFreq).max(0).min(1);
			var totPara1Freq = (para1Freq + modPara1Freq).max(0).min(1);
			var totPara2Freq = (para2Freq + modPara2Freq).max(0).min(1);
			var totPara3Freq = (para3Freq + modPara3Freq).max(0).min(1);
			var totPara4Freq = (para4Freq + modPara4Freq).max(0).min(1);
			var totHiShelfFreq = (hiShelfFreq + modHiShelfFreq).max(0).min(1);
			var totLowShelfGain = (lowShelfGain + modLowShelfGain).max(0).min(1);
			var totPara1Gain = (para1Gain + modPara1Gain).max(0).min(1);
			var totPara2Gain = (para2Gain + modPara2Gain).max(0).min(1);
			var totPara3Gain = (para3Gain + modPara3Gain).max(0).min(1);
			var totPara4Gain = (para4Gain + modPara4Gain).max(0).min(1);
			var totHiShelfGain = (hiShelfGain + modHiShelfGain).max(0).min(1);
			var totPara1Res = (para1Res + modPara1Res).max(0.01).min(1);
			var totPara2Res = (para2Res + modPara2Res).max(0.01).min(1);
			var totPara3Res = (para3Res + modPara3Res).max(0.01).min(1);
			var totPara4Res = (para4Res + modPara4Res).max(0.01).min(1);
			var totWetDryMix = (wetDryMix + modWetDryMix).max(0).min(1);

			var funcLowShelf = this.getSynthOption(0);
			var funcPara1 = this.getSynthOption(1);
			var funcPara2 = this.getSynthOption(2);
			var funcPara3 = this.getSynthOption(3);
			var funcPara4 = this.getSynthOption(4);
			var funcHiShelf = this.getSynthOption(5);
			var input, chain;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(startEnv * InFeedback.ar(in, 1));
			chain = funcLowShelf.value( input, classData.freqSpec.map(totLowShelfFreq), 1,
				classData.gainSpec.map(totLowShelfGain));
			chain = funcPara1.value( chain, classData.freqSpec.map(totPara1Freq), totPara1Res.reciprocal,
				classData.gainSpec.map(totPara1Gain));
			chain = funcPara2.value( chain, classData.freqSpec.map(totPara2Freq), totPara2Res.reciprocal,
				classData.gainSpec.map(totPara2Gain));
			chain = funcPara3.value( chain, classData.freqSpec.map(totPara3Freq), totPara3Res.reciprocal,
				classData.gainSpec.map(totPara3Gain));
			chain = funcPara4.value( chain, classData.freqSpec.map(totPara4Freq), totPara4Res.reciprocal,
				classData.gainSpec.map(totPara4Gain));
			chain = funcHiShelf.value( chain, classData.freqSpec.map(totHiShelfFreq), 1,
				classData.gainSpec.map(totHiShelfGain));
			chain = LeakDC.ar(chain);
			Out.ar(out, TXClean.ar(startEnv * chain * totWetDryMix) + (input * (1 - totWetDryMix)));
		};
		guiSpecArray = [
			["SpacerLine", 3],
			["TextBar", "Band on/off", 80],
			["SynthOptionCheckBox", "Lo-shelf", arrOptionData, 0, 50, nil, 12],
			["SynthOptionCheckBox", "Para 1", arrOptionData, 1, 50, nil, 12],
			["SynthOptionCheckBox", "Para 2", arrOptionData, 2, 50, nil, 12],
			["SynthOptionCheckBox", "Para 3", arrOptionData, 3, 50, nil, 12],
			["SynthOptionCheckBox", "Para 4", arrOptionData, 4, 50, nil, 12],
			["SynthOptionCheckBox", "Hi-shelf", arrOptionData, 5, 50, nil, 12],
			["SpacerLine", 6],
			["Spacer", 36],
			["TXMultiKnobNoUnmap", "Freq",
				["lowShelfFreq", "para1Freq", "para2Freq", "para3Freq", "para4Freq", "hiShelfFreq", ],
				6, classData.freqSpec],
			["SpacerLine", 6],
			["ActionButtonBig", "1->all",  {this.copy1ToAll(["para1Res", "para2Res", "para3Res", "para4Res",  ]);}, 36],
			["TXMultiKnobNoUnmap", "Res", ["para1Res", "para2Res", "para3Res", "para4Res", ], 4, classData.resSpec, nil, 1],
			["SpacerLine", 6],
			["ActionButtonBig", "1->all",  {this.copy1ToAll(
				["lowShelfGain", "para1Gain", "para2Gain", "para3Gain", "para4Gain", "hiShelfGain", ]);
			}, 36],
			["TXMultiKnobNoUnmap", "Gain",
				["lowShelfGain", "para1Gain", "para2Gain", "para3Gain", "para4Gain", "hiShelfGain", ],
				6, classData.gainSpec],
			["SpacerLine", 10],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs([
			["SynthOptionCheckBox", "Lo-shelf", arrOptionData, 0, 50, nil, 12],
			["SynthOptionCheckBox", "Para 1", arrOptionData, 1, 50, nil, 12],
			["SynthOptionCheckBox", "Para 2", arrOptionData, 2, 50, nil, 12],
			["SynthOptionCheckBox", "Para 3", arrOptionData, 3, 50, nil, 12],
			["SynthOptionCheckBox", "Para 4", arrOptionData, 4, 50, nil, 12],
			["SynthOptionCheckBox", "Hi-shelf", arrOptionData, 5, 50, nil, 12],
		]
		++ ["lowShelfFreq", "para1Freq", "para2Freq", "para3Freq", "para4Freq", "hiShelfFreq",
		].collect({arg item, i;
			["EZsliderUnmapped", item, classData.freqSpec, item]
		})
		++ ["para1Res", "para2Res", "para3Res", "para4Res",
		].collect({arg item, i;
			["EZsliderUnmapped", item, classData.resSpec, item]
		})
		++ ["lowShelfGain", "para1Gain", "para2Gain", "para3Gain", "para4Gain", "hiShelfGain",
		].collect({arg item, i;
			["EZsliderUnmapped", item, classData.gainSpec, item]
		})
		++ [
			["WetDryMixSlider"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	loadExtraData {arg argData;
		system.showViewIfModDisplay(this);
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

}

