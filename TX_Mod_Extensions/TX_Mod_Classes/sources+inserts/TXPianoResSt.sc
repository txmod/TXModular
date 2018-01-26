// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXPianoResSt : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Piano Res St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Out Level", 1, "modLevel", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.specDefault = ControlSpec(0, 1, 'lin');
		classData.specC = ControlSpec(0.01, 100, 2);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			[ "c1", 20, \ir],
			[ "c3", 20, \ir],
			[ "mix", 0.8, \ir],
			["out", 0, 0],
			["level", 0.5, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modLevel", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		synthDefFunc = { arg in, c1, c3, mix, out, level, wetDryMix, modLevel = 0, modWetDryMix = 0;
			var input, levelSum, mixSum, outSound;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(startEnv * InFeedback.ar(in,2));
			levelSum = (level + modLevel).max(0).min(1);
			mixSum = (wetDryMix + modWetDryMix).max(0).min(1);
			// OteySoundBoard.ar (inp: 0, c1: 20, c3: 20, mix: 0.8)
			outSound = OteySoundBoard.ar(input, c1, c3, mix) * levelSum;
			Out.ar(out, startEnv * ((outSound * mixSum) + (input * (1-mixSum))));
		};

		guiSpecArray = [
			["SpacerLine", 2],
			["ActionButton", "Update Model - after changing parameters below", {
				this.rebuildSynth;
				this.moduleNodeStatus = "running";
				system.showView;
			}, 320, TXColor.paleYellow, TXColor.sysGuiCol2],
			["SpacerLine", 10],
			["EZslider", "Model: C1", classData.specC, "c1"],
			["SpacerLine", 4],
			["EZslider", "Model: C3", classData.specC, "c3"],
			["SpacerLine", 4],
			["EZslider", "Model: Mix", ControlSpec(0, 1), "mix"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 10],
			["EZslider", "Out level", classData.specDefault, "level"],
			["SpacerLine", 10],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

