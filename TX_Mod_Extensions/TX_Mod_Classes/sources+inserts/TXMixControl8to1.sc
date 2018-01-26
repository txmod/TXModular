// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMixControl8to1 : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Mix Control 8-1";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Input 1", 1, "input1", 0],
			["Input 2", 1, "input2", 0],
			["Input 3", 1, "input3", 0],
			["Input 4", 1, "input4", 0],
			["Input 5", 1, "input5", 0],
			["Input 6", 1, "input6", 0],
			["Input 7", 1, "input7", 0],
			["Input 8", 1, "input8", 0],
			["Level 1", 1, "modLevel1", 0],
			["Level 2", 1, "modLevel2", 0],
			["Level 3", 1, "modLevel3", 0],
			["Level 4", 1, "modLevel4", 0],
			["Level 5", 1, "modLevel5", 0],
			["Level 6", 1, "modLevel6", 0],
			["Level 7", 1, "modLevel7", 0],
			["Level 8", 1, "modLevel8", 0],
			["Output Level", 1, "modOutLevel", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.guiWidth = 642;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["input1", 0, 0],
			["input2", 0, 0],
			["input3", 0, 0],
			["input4", 0, 0],
			["input5", 0, 0],
			["input6", 0, 0],
			["input7", 0, 0],
			["input8", 0, 0],
			["out", 0, 0],
			["level1", 1, defLagTime],
			["level2", 0, defLagTime],
			["level3", 0, defLagTime],
			["level4", 0, defLagTime],
			["level5", 0, defLagTime],
			["level6", 0, defLagTime],
			["level7", 0, defLagTime],
			["level8", 0, defLagTime],
			["autoLevelReduction", 1, 0],
			["outLevel", 1, defLagTime],
			["modLevel1", 0, defLagTime],
			["modLevel2", 0, defLagTime],
			["modLevel3", 0, defLagTime],
			["modLevel4", 0, defLagTime],
			["modLevel5", 0, defLagTime],
			["modLevel6", 0, defLagTime],
			["modLevel7", 0, defLagTime],
			["modLevel8", 0, defLagTime],
			["modOutLevel", 0, defLagTime],
		];
		synthDefFunc = { arg input1, input2, input3, input4, input5, input6, input7, input8,
			out, level1, level2, level3, level4, level5, level6, level7, level8,
			autoLevelReduction, outLevel,
			modLevel1=0, modLevel2=0, modLevel3=0, modLevel4=0, modLevel5=0, modLevel6=0, modLevel7=0,
			modLevel8=0, modOutLevel=0;
			var holdInput1, holdInput2, holdInput3, holdInput4, holdInput5, holdInput6, holdInput7, holdInput8;
			var holdLevel1, holdLevel2, holdLevel3, holdLevel4, holdLevel5, holdLevel6, holdLevel7, holdLevel8;
			var holdOutLevel, allInputs, allLevels, reduceScale;

			holdLevel1 = (level1 + modLevel1).max(0).min(1);
			holdLevel2 = (level2 + modLevel2).max(0).min(1);
			holdLevel3 = (level3 + modLevel3).max(0).min(1);
			holdLevel4 = (level4 + modLevel4).max(0).min(1);
			holdLevel5 = (level5 + modLevel5).max(0).min(1);
			holdLevel6 = (level6 + modLevel6).max(0).min(1);
			holdLevel7 = (level7 + modLevel7).max(0).min(1);
			holdLevel8 = (level8 + modLevel8).max(0).min(1);
			allLevels = [holdLevel1, holdLevel2, holdLevel3, holdLevel4,
				holdLevel5, holdLevel6, holdLevel7, holdLevel8];
			holdInput1 = input1 * holdLevel1;
			holdInput2 = input2 * holdLevel2;
			holdInput3 = input3 * holdLevel3;
			holdInput4 = input4 * holdLevel4;
			holdInput5 = input5 * holdLevel5;
			holdInput6 = input6 * holdLevel6;
			holdInput7 = input7 * holdLevel7;
			holdInput8 = input8 * holdLevel8;
			allInputs = [holdInput1, holdInput2, holdInput3, holdInput4,
				holdInput5, holdInput6, holdInput7, holdInput8];
			holdOutLevel = (outLevel + modOutLevel).max(0).min(1);
			reduceScale = (1 - autoLevelReduction) + (autoLevelReduction * allLevels.sum.max(1).reciprocal);
			Out.kr(out, holdOutLevel * reduceScale * Mix.new(allInputs));
		};
		guiSpecArray = [
			["MixerLevel", "Level 1", ControlSpec(0, 1), "level1"],
			["MixerLevel", "Level 2", ControlSpec(0, 1), "level2"],
			["MixerLevel", "Level 3", ControlSpec(0, 1), "level3"],
			["MixerLevel", "Level 4", ControlSpec(0, 1), "level4"],
			["MixerLevel", "Level 5", ControlSpec(0, 1), "level5"],
			["MixerLevel", "Level 6", ControlSpec(0, 1), "level6"],
			["MixerLevel", "Level 7", ControlSpec(0, 1), "level7"],
			["MixerLevel", "Level 8", ControlSpec(0, 1), "level8"],
			["SpacerLine", 10],
			["TXCheckBox", "Auto scale (output reduced if mixed level headroom exceeds 1)",
				"autoLevelReduction", nil, 370],
			["SpacerLine", 10],
			["EZslider", "Output Level", ControlSpec(0, 1), "outLevel"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

