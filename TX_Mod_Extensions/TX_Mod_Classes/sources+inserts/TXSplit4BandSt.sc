// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSplit4BandSt : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Split 4-Band St";
		classData.moduleRate = "audio";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["outLevel", 1, "modLevel", 0],
		];
		classData.arrAudSCInBusSpecs = [
			["Inputs L+R", 2, "input"],
		];
		classData.noOutChannels = 8;
		classData.arrOutBusSpecs = [
			["Band 1 L+R", [0,1]],
			["Band 2 L+R", [2,3]],
			["Band 3 L+R", [4,5]],
			["Band 4 L+R", [6,7]],
		];
		classData.guiWidth = 600;
		classData.freqSpec = ControlSpec(5, 20000, \exp);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["input", 0, 0],
			["out", 0, 0],
			["splitFreq1", 250, defLagTime],
			["splitFreq2", 2000, defLagTime],
			["splitFreq3", 6000, defLagTime],
			["outLevel", 1, defLagTime],
			["modLevel", 0, defLagTime],
		];
		synthDefFunc = { arg input, out, splitFreq1, splitFreq2, splitFreq3, outLevel, modLevel=0;

			var holdInput, holdLevel, holdSplit;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			holdLevel = (outLevel + modLevel).max(0).min(1);
			holdInput = InFeedback.ar(input, 2) * holdLevel;
			holdSplit = startEnv * TXBandSplitter4.ar(holdInput, splitFreq1, splitFreq2, splitFreq3);
			Out.ar(out, [holdSplit[0][0], holdSplit[0][1], holdSplit[1][0], holdSplit[1][1],
				holdSplit[2][0], holdSplit[2][1], holdSplit[3][0], holdSplit[3][1],
			]);
		};
		guiSpecArray = [
			["EZslider", "Split Freq 1", classData.freqSpec, "splitFreq1", {arg view;
				var holdVal;
				holdVal = this.getSynthArgSpec("splitFreq2");
				if (view.value > holdVal,{
					view.value_(holdVal);
					this.setSynthValue("splitFreq1", holdVal);
				});
			}],
			["EZslider", "Split Freq 2", classData.freqSpec, "splitFreq2", {arg view;
				var myVal = view.value;
				var holdVal;
				holdVal = this.getSynthArgSpec("splitFreq1");
				if (myVal < holdVal,{
					view.value_(holdVal);
					myVal = holdVal;
					this.setSynthValue("splitFreq2", holdVal);
				});
				holdVal = this.getSynthArgSpec("splitFreq3");
				if (myVal > holdVal,{
					view.value_(holdVal);
					this.setSynthValue("splitFreq2", holdVal);
				});
			}],
			["EZslider", "Split Freq 3", classData.freqSpec, "splitFreq3", {arg view;
				var holdVal;
				holdVal = this.getSynthArgSpec("splitFreq2");
				if (view.value < holdVal,{
					view.value_(holdVal);
					this.setSynthValue("splitFreq3", holdVal);
				});
			}],
			["SpacerLine", 10],
			["EZslider", "Output Level", ControlSpec(0, 1), "outLevel"],
		];
		arrActionSpecs = this.buildActionSpecs([
			["EZslider", "Output Level", ControlSpec(0, 1), "outLevel"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}


}

