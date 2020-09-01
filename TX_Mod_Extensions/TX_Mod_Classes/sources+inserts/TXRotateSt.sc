// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXRotateSt : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Rotate St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Rotation", 1, "modRotation", 0],
			["Auto-Rotate", 1, "modAutoRotate", 0],
			["Frequency", 1, "modFreq", 0],
			["Reverse", 1, "modReverse", 0],
			["Bypass", 1, "modBypass", 0],
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.freqSpec = ControlSpec(0.001, 100, 'exp');
		classData.rotationSpec = ControlSpec(-360, 360);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var arrPresets;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["rotation", 0.5, defLagTime],
			["rotationMin", -180, defLagTime],
			["rotationMax", 180, defLagTime],
			["autoRotate", 1, defLagTime],
			["freq", ControlSpec(0.01, 10, 'exp').unmap(0.25), defLagTime],
			["freqMin", 0.01, defLagTime],
			["freqMax", 10, defLagTime],
			["reverse", 0, defLagTime],
			["bypass", 0, defLagTime],
			["modRotation", 0, 0],
			["modAutoRotate", 0, 0],
			["modFreq", 0, 0],
			["modReverse", 0, 0],
			["modBypass", 0, 0],
		];
		synthDefFunc = { arg in, out, rotation, rotationMin, rotationMax, autoRotate,
			freq, freqMin, freqMax, reverse, bypass,
			modRotation = 0, modAutoRotate = 0, modFreq = 0, modReverse = 0, modBypass = 0;

			var input, rotationSum, autoRotateSum, freqSum, reverseSum, bypassSum, outPosition, outSignal;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = InFeedback.ar(in, 2);
			rotationSum = (rotation + modRotation).max(0).min(1).linlin(0, 1, rotationMin, rotationMax);
			autoRotateSum = (autoRotate + modAutoRotate).max(0).min(1).round;
			freqSum = (freq + modFreq).max(0).min(1).linexp(0, 1, freqMin, freqMax);
			reverseSum = (reverse + modReverse).max(0).min(1).round;
			bypassSum = (bypass + modBypass).max(0).min(1).round;
			outPosition = wrap2((rotationSum / 180) + (autoRotateSum * LFSaw.kr(freqSum)));

			outSignal = ((1 - bypassSum) *
				Rotate2.ar(input[0], input[1], outPosition * (1 - (2 * reverseSum))))
			+ (bypassSum * input);
			Out.ar(out, startEnv * outSignal);
		};
		guiSpecArray = [
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "Rotation", classData.rotationSpec,
				"rotation", "rotationMin", "rotationMax",],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 10],
			["TXCheckBox", "Auto-Rotate", "autoRotate", nil, 150],
			["SpacerLine", 10],
			["Spacer", 82],
			["ActionButton", "Freq x 2", {this.freqMultiply(2);}, 60],
			["ActionButton", "Freq x 3", {this.freqMultiply(3);}, 60],
			["ActionButton", "Freq / 2", {this.freqMultiply(0.5);}, 60],
			["ActionButton", "Freq / 3", {this.freqMultiply(1/3);}, 60],
			["NextLine"],
			["TXFreqBpmMinMaxSldr", "Frequency", classData.freqSpec, "freq", "freqMin", "freqMax",
				nil, TXLFO.arrLFOFreqRanges],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 10],
			["TXCheckBox", "Reverse", "reverse", nil, 150],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 10],
			["TXCheckBox", "Bypass", "bypass", nil, 150],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	freqMultiply { arg argMultiplyValue;
		var currentFreq, minFreq, maxFreq, holdControlSpec, newFreq;
		minFreq = this.getSynthArgSpec("freqMin");
		maxFreq = this.getSynthArgSpec("freqMax");
		holdControlSpec = ControlSpec.new(minFreq, maxFreq, \exp);
		currentFreq = holdControlSpec.map(this.getSynthArgSpec("freq"));
		newFreq = currentFreq * argMultiplyValue;
		if (argMultiplyValue < 1, {
			if ( newFreq >= minFreq, {
				this.setSynthValue("freq", holdControlSpec.unmap(newFreq));
			});
		},{
			if ( newFreq <= maxFreq, {
				this.setSynthValue("freq", holdControlSpec.unmap(newFreq));
			});
		});
		system.flagGuiIfModDisplay(this);
	}

}

