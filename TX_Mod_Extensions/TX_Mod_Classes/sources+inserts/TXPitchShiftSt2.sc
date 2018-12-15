// Copyright (C) 2018  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXPitchShiftSt2 : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Pitch Shift St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Pitch Shift", 1, "modPitchShift", 0],
			["Stereo Shift", 1, "modStereoShift", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.shiftRangePresets = [
			["Range presets: ", [0, 12]],
			["0.1 semitone [0, 0.1]", [0, 0.1]],
			["0.3 semitone [0, 0.3]", [0, 0.3]],
			["1 semitone [0, 1]", [0, 1]],
			["7 semitones [0, 7]", [0, 7]],
			["1 octave [0, 12]", [0, 12]],
			["2 octaves [0, 24]", [0, 24]],
			["3 octaves [0, 36]", [0, 36]],
			["+- 0.1 semitone [-0.1, 0.1]", [-0.1, 0.1]],
			["+- 0.3 semitone [-0.3, 0.3]", [-0.3, 0.3]],
			["+- 1 semitone [-1, 1]", [-1, 1]],
			["+- 7 semitones [-7, 7]", [-7, 7]],
			["+- 1 octave [-12, 12]", [-12, 12]],
			["+- 2 octaves [-24, 24]", [-24, 24]],
			["+- 3 octaves [-36, 36]", [-36, 36]],
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["pitchShift", 0.5, defLagTime],
			["pitchShiftMin", -12, defLagTime],
			["pitchShiftMax", 12, defLagTime],
			["stereoShift", 0.501, defLagTime],
			["stereoShiftMin", -12, defLagTime],
			["stereoShiftMax", 12, defLagTime],
			["randPitch", 0, defLagTime],
			["randTime", 0.05, defLagTime],
			["gainBoost", 0.5, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modPitchShift", 0, defLagTime],
			["modStereoShift", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		arrOptions = [5];
		arrOptionData = [
			[ // 0 - window size
				["0.05 s", 0.05],
				["0.1 s", 0.1],
				["0.15 s", 0.15],
				["0.2 s", 0.2],
				["0.25 s", 0.25],
				["0.3 s", 0.3],
				["0.35 s", 0.25],
				["0.4 s", 0.3],
				["0.45 s", 0.25],
				["0.5 s", 0.3],
				["0.55 s", 0.25],
				["0.6 s", 0.3],
			],
		];
		synthDefFunc = {
			arg in, out, pitchShift, pitchShiftMin, pitchShiftMax, stereoShift, stereoShiftMin, stereoShiftMax,
			randPitch, randTime, gainBoost, wetDryMix,
			modPitchShift=0, modStereoShift=0, modWetDryMix=0;

			var input, grainSize, outWet, outRatio, pitchShiftCombined, stereoShiftCombined, mixCombined, outSound;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(InFeedback.ar(in,2));
			grainSize = arrOptionData.at(0).at(arrOptions.at(0)).at(1);
			pitchShiftCombined = pitchShiftMin + ((pitchShiftMax - pitchShiftMin) * (pitchShift + modPitchShift).max(0).min(1));
			pitchShiftCombined = pitchShiftCombined;
			stereoShiftCombined = stereoShiftMin + ((stereoShiftMax - stereoShiftMin) * (stereoShift + modStereoShift).max(0).min(1));
			stereoShiftCombined = stereoShiftCombined;
			outRatio = 2 ** ((pitchShiftCombined + [stereoShiftCombined, stereoShiftCombined.neg]) / 12);
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outWet = PitchShift.ar(input, grainSize, outRatio, randPitch.squared, randTime.squared * grainSize, 1 + gainBoost);
			outSound = (outWet * mixCombined) + (input * (1-mixCombined));
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		guiSpecArray = [
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "Pitch Shift", ControlSpec(-48, 48), "pitchShift", "pitchShiftMin", "pitchShiftMax",
				nil, classData.shiftRangePresets],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Stereo Shift", ControlSpec(-48, 48), "stereoShift", "stereoShiftMin", "stereoShiftMax",
				nil, classData.shiftRangePresets],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 10],
			["SynthOptionPopupPlusMinus", "Grain size", arrOptionData, 0],
			["SpacerLine", 6],
			["EZSlider", "Random pitch", \unipolar, "randPitch"],
			["SpacerLine", 6],
			["EZSlider", "Random time", \unipolar, "randTime"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 10],
			["EZSlider", "Gain boost", ControlSpec(0, 2), "gainBoost"],
			["NextLine"],
			["DividingLine"],
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

