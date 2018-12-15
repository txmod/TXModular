// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXDistortionMBSt : TXModuleBase {		// MultiBand Distortion module

	classvar <>classData;
	var	displayOption;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "DistortionMB St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["In Gain 1", 1, "modInGain1", 0],
			["In Gain 2", 1, "modInGain2", 0],
			["In Gain 3", 1, "modInGain3", 0],
			["In Gain 4", 1, "modInGain4", 0],
			["Depth 1", 1, "modDepth1", 0],
			["Depth 2", 1, "modDepth2", 0],
			["Depth 3", 1, "modDepth3", 0],
			["Depth 4", 1, "modDepth4", 0],
			["Dry-Wet Mix 1", 1, "modWetDryMix1", 0],
			["Dry-Wet Mix 2", 1, "modWetDryMix2", 0],
			["Dry-Wet Mix 3", 1, "modWetDryMix3", 0],
			["Dry-Wet Mix 4", 1, "modWetDryMix4", 0],
			["Band Level 1", 1, "modBandLevel1", 0],
			["Band Level 2", 1, "modBandLevel2", 0],
			["Band Level 3", 1, "modBandLevel3", 0],
			["Band Level 4", 1, "modBandLevel4", 0],
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.guiWidth = 560;
		classData.inGainSpec = ControlSpec(0, 10);
		classData.depthSpec = ControlSpec(0, 1);
		classData.distortGainSpec = ControlSpec(0, 1);
		classData.bandLevelSpec = ControlSpec(0, 2);
		classData.freqSpec = ControlSpec(40, 20000, \exp);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var distortFunc;
		//	set  class specific instance variables
		displayOption = "showBands";
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["splitFreq1", 250, defLagTime],
			["splitFreq2", 2000, defLagTime],
			["splitFreq3", 6000, defLagTime],
			["inGain1", 0.1, defLagTime],
			["inGain2", 0.1, defLagTime],
			["inGain3", 0.1, defLagTime],
			["inGain4", 0.1, defLagTime],
			["depth1", 0.5, defLagTime],
			["depth2", 0.5, defLagTime],
			["depth3", 0.5, defLagTime],
			["depth4", 0.5, defLagTime],
			["distortGain1", 1, defLagTime],
			["distortGain2", 1, defLagTime],
			["distortGain3", 1, defLagTime],
			["distortGain4", 1, defLagTime],
			["wetDryMix1", 1.0, defLagTime],
			["wetDryMix2", 1.0, defLagTime],
			["wetDryMix3", 1.0, defLagTime],
			["wetDryMix4", 1.0, defLagTime],
			["bandLevel1", 0.5, defLagTime],
			["bandLevel2", 0.5, defLagTime],
			["bandLevel3", 0.5, defLagTime],
			["bandLevel4", 0.5, defLagTime],
			["modInGain1", 0, defLagTime],
			["modInGain2", 0, defLagTime],
			["modInGain3", 0, defLagTime],
			["modInGain4", 0, defLagTime],
			["modDepth1", 0, defLagTime],
			["modDepth2", 0, defLagTime],
			["modDepth3", 0, defLagTime],
			["modDepth4", 0, defLagTime],
			["modWetDryMix1", 0, defLagTime],
			["modWetDryMix2", 0, defLagTime],
			["modWetDryMix3", 0, defLagTime],
			["modWetDryMix4", 0, defLagTime],
			["modBandLevel1", 0, defLagTime],
			["modBandLevel2", 0, defLagTime],
			["modBandLevel3", 0, defLagTime],
			["modBandLevel4", 0, defLagTime],
		];
		arrOptions = [0, 0, 0, 0];
		arrOptionData = [
			TXDistort.arrOptionData,
			TXDistort.arrOptionData,
			TXDistort.arrOptionData,
			TXDistort.arrOptionData,
		];
		distortFunc = {arg bandIndex, input, inGain, depth, distortGain, wetDryMix, bandLevel,
			modInGain, modDepth, modWetDryMix, modBandLevel;
			var inGainSum, depthSum, distortGainSum, mixSum, bandLevelSum, outFunction, outDistorted, outBand;
			inGainSum = (inGain + modInGain).max(0).min(1);
			mixSum = (wetDryMix + modWetDryMix).max(0).min(1);
			depthSum = (depth + modDepth).max(0).min(1);
			bandLevelSum = (bandLevel + modBandLevel).max(0).min(1);
			outFunction = this.getSynthOption(bandIndex);
			outDistorted = distortGain * outFunction.value(inGainSum * 10 * input, depthSum);
			outBand = (outDistorted * mixSum) + (input * (1-mixSum));
			outBand * bandLevelSum * 2;
		};
		synthDefFunc = { arg in, out, splitFreq1, splitFreq2, splitFreq3, inGain1, inGain2, inGain3, inGain4,
			depth1, depth2, depth3, depth4,
			distortGain1, distortGain2, distortGain3, distortGain4,
			wetDryMix1, wetDryMix2, wetDryMix3, wetDryMix4,
			bandLevel1, bandLevel2, bandLevel3, bandLevel4,
			modInGain1=0, modInGain2=0, modInGain3=0, modInGain4=0,
			modDepth1=0, modDepth2=0, modDepth3=0, modDepth4=0,
			modWetDryMix1=0, modWetDryMix2=0, modWetDryMix3=0, modWetDryMix4=0,
			modBandLevel1=0, modBandLevel2=0, modBandLevel3=0, modBandLevel4=0;

			var input, split, outMix;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in,2);
			split = TXBandSplitter4.ar(input, splitFreq1, splitFreq2, splitFreq3);

			outMix = Mix.new([
				distortFunc.value(0, split[0], inGain1, depth1, distortGain1, wetDryMix1, bandLevel1,
					modInGain1, modDepth1, modWetDryMix1, modBandLevel1 ),
				distortFunc.value(1, split[1], inGain2, depth2, distortGain2, wetDryMix2, bandLevel2,
					modInGain2, modDepth2, modWetDryMix2, modBandLevel2 ),
				distortFunc.value(2, split[2], inGain3, depth3, distortGain3, wetDryMix3, bandLevel3,
					modInGain3, modDepth3, modWetDryMix3, modBandLevel3 ),
				distortFunc.value(3, split[3], inGain4, depth4, distortGain4, wetDryMix4, bandLevel4,
					modInGain4, modDepth4, modWetDryMix4, modBandLevel4 )
			]);
			Out.ar(out, startEnv * outMix);
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["SynthOptionPopupPlusMinus", "Distortion 1", arrOptionData, 0, 270],
			["SynthOptionPopupPlusMinus", "Distortion 2", arrOptionData, 1, 270],
			["SpacerLine", 6],
			["SynthOptionPopupPlusMinus", "Distortion 3", arrOptionData, 2, 270],
			["SynthOptionPopupPlusMinus", "Distortion 4", arrOptionData, 3, 270],
		]
		++ ["inGain1", "inGain2", "inGain3", "inGain4", ].collect({arg item, i;
			["EZsliderUnmapped", item, classData.inGainSpec, item]
		})
		++ ["depth1", "depth2", "depth3", "depth4",  ].collect({arg item, i;
			["EZsliderUnmapped", item, classData.depthSpec, item]
		})
		++ ["distortGain1", "distortGain2", "distortGain3", "distortGain4",  ].collect({arg item, i;
			["EZsliderUnmapped", item, classData.distortGainSpec, item]
		})
		++ ["wetDryMix1", "wetDryMix2", "wetDryMix3", "wetDryMix4",  ].collect({arg item, i;
			["EZsliderUnmapped", item, ControlSpec(0, 1), item]
		})
		++ ["bandLevel1", "bandLevel2", "bandLevel3", "bandLevel4",  ].collect({arg item, i;
			["EZsliderUnmapped", item, classData.bandLevelSpec, item]
		})
		);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Bands", {displayOption = "showBands";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showBands")],
			["Spacer", 3],
			["ActionButton", "Band-Split Frequencies", {displayOption = "showSplitFreqs";
				this.buildGuiSpecArray; system.showView;}, 160,
			TXColor.white, this.getButtonColour(displayOption == "showSplitFreqs")],
			["DividingLine"],
			["SpacerLine", 2],
		];

		if (displayOption == "showSplitFreqs", {
			guiSpecArray = guiSpecArray ++[
				["EZslider", "Split Freq 1", classData.freqSpec, "splitFreq1", {arg view;
					var holdVal;
					holdVal = this.getSynthArgSpec("splitFreq2");
					if (view.value > holdVal,{
						view.value_(holdVal);
						this.setSynthArgSpec("splitFreq1", holdVal);
					});
				}],
				["SpacerLine", 2],
				["EZslider", "Split Freq 2", classData.freqSpec, "splitFreq2", {arg view;
					var myVal = view.value;
					var holdVal;
					holdVal = this.getSynthArgSpec("splitFreq1");
					if (myVal < holdVal,{
						view.value_(holdVal);
						myVal = holdVal;
						this.setSynthArgSpec("splitFreq2", holdVal);
					});
					holdVal = this.getSynthArgSpec("splitFreq3");
					if (myVal > holdVal,{
						view.value_(holdVal);
						this.setSynthArgSpec("splitFreq2", holdVal);
					});
				}],
				["SpacerLine", 2],
				["EZslider", "Split Freq 3", classData.freqSpec, "splitFreq3", {arg view;
					var holdVal;
					holdVal = this.getSynthArgSpec("splitFreq2");
					if (view.value < holdVal,{
						view.value_(holdVal);
						this.setSynthArgSpec("splitFreq3", holdVal);
					});
				}],
				["SpacerLine", 10],
			];
		});
		if (displayOption == "showBands", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionPopupPlusMinus", "Distortion 1", arrOptionData, 0, 270],
				["SynthOptionPopupPlusMinus", "Distortion 2", arrOptionData, 1, 270],
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Distortion 3", arrOptionData, 2, 270],
				["SynthOptionPopupPlusMinus", "Distortion 4", arrOptionData, 3, 270],
				["SpacerLine", 6],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["inGain1", "inGain2", "inGain3", "inGain4",]);}, 36],
				["TXMultiKnobNoUnmap", "In Gain", ["inGain1", "inGain2", "inGain3", "inGain4", ], 4, classData.inGainSpec],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["depth1", "depth2", "depth3", "depth4",  ]);}, 36],
				["TXMultiKnobNoUnmap", "Depth", ["depth1", "depth2", "depth3", "depth4", ], 4, classData.depthSpec],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["distortGain1", "distortGain2", "distortGain3", "distortGain4",  ]);}, 36],
				["TXMultiKnobNoUnmap", "Distort gain", ["distortGain1", "distortGain2", "distortGain3", "distortGain4", ],
					4, classData.distortGainSpec],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["wetDryMix1", "wetDryMix2", "wetDryMix3", "wetDryMix4",  ]);}, 36],
				["TXMultiKnobNoUnmap", "Distort Mix", ["wetDryMix1", "wetDryMix2", "wetDryMix3", "wetDryMix4", ],
					4, ControlSpec(0, 1)],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["bandLevel1", "bandLevel2", "bandLevel3", "bandLevel4",  ]);}, 36],
				["TXMultiKnobNoUnmap", "Band Level", ["bandLevel1", "bandLevel2", "bandLevel3", "bandLevel4", ],
					4, classData.bandLevelSpec],
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

}

