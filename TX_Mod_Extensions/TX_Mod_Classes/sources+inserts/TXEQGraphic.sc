// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXEQGraphic : TXModuleBase {		// EQ Graphic module

	// Note this is based on ideas from the iXiQuarks EQ module

	classvar <>classData;

	var arrCurveValues;
	var arrSlotData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "EQ Graphic";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.arrFreqs = [20, 25, 31.5, 40, 50, 63, 80, 100, 125, 160, 200, 250,
			315, 400, 500, 630, 800, 1000, 1250, 1600, 2000, 2500, 3150, 4000,
			5000, 6300, 8000, 10000, 12500, 16000, 20000]; // 1/3 octave bands
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["band1", 0.5, defLagTime],
			["band2", 0.5, defLagTime],
			["band3", 0.5, defLagTime],
			["band4", 0.5, defLagTime],
			["band5", 0.5, defLagTime],
			["band6", 0.5, defLagTime],
			["band7", 0.5, defLagTime],
			["band8", 0.5, defLagTime],
			["band9", 0.5, defLagTime],
			["band10", 0.5, defLagTime],
			["band11", 0.5, defLagTime],
			["band12", 0.5, defLagTime],
			["band13", 0.5, defLagTime],
			["band14", 0.5, defLagTime],
			["band15", 0.5, defLagTime],
			["band16", 0.5, defLagTime],
			["band17", 0.5, defLagTime],
			["band18", 0.5, defLagTime],
			["band19", 0.5, defLagTime],
			["band20", 0.5, defLagTime],
			["band21", 0.5, defLagTime],
			["band22", 0.5, defLagTime],
			["band23", 0.5, defLagTime],
			["band24", 0.5, defLagTime],
			["band25", 0.5, defLagTime],
			["band26", 0.5, defLagTime],
			["band27", 0.5, defLagTime],
			["band28", 0.5, defLagTime],
			["band29", 0.5, defLagTime],
			["band30", 0.5, defLagTime],
			["band31", 0.5, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modWetDryMix", 0, defLagTime],
		];
		synthDefFunc = { arg in, out,
			band1, band2, band3, band4, band5, band6, band7, band8,
			band9, band10, band11, band12, band13, band14, band15, band16, band17, band18,
			band19, band20, band21, band22, band23, band24, band25, band26, band27, band28,
			band29, band30, band31,
			wetDryMix, modWetDryMix=0;

			var input, arrBands, srq, outEQ, mixCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(startEnv * InFeedback.ar(in, 1));
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			arrBands = [band1, band2, band3, band4, band5, band6, band7, band8,
				band9, band10, band11, band12, band13, band14, band15, band16, band17, band18,
				band19, band20, band21, band22, band23, band24, band25, band26, band27, band28,
				band29, band30, band31];
			//		srq = 0.5.sqrt;
			srq = 1;
			outEQ = Mix.new (
				arrBands.collect({arg band, i;
					// BPF.ar(BPF.ar(input, classData.arrFreqs.at(i), srq), classData.arrFreqs.at(i), srq, band); // double BPF
					BPF.ar(input, classData.arrFreqs.at(i), srq, band); //  BPF
				});
			);
			outEQ = LeakDC.ar(outEQ);
			Out.ar(out, (0.35 * TXClean.ar(startEnv * outEQ) * mixCombined) + (input * (1-mixCombined)) );
		};
		guiSpecArray = [
			["TextBarLeft", " EQ range 20 - 20khz,   3 bands per octave,   total 31 bands ", 450],
			["SpacerLine", 4],
			["TXEQCurveDraw", "EQ curve", {arrCurveValues},
				{arg view; arrCurveValues = view.value; arrSlotData = view.arrSlotData;
					this.updateSynthBandVals;
				},
				{arrSlotData},
				classData.arrFreqs
			],
			["SpacerLine", 4],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs(guiSpecArray);
		//	initialise buffer to linear morph
		arrCurveValues = Array.newClear(31).fill(0.5);
		//	initialise slots to linear morphs
		arrSlotData = arrCurveValues.dup(5);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	updateSynthBandVals {
		var arrBandNames;

		arrBandNames = [
			"band1", "band2", "band3", "band4, band5", "band6", "band7", "band8", "band9", "band10",
			"band11", "band12", "band13", "band14", "band15", "band16", "band17", "band18", "band19", "band20",
			"band21", "band22", "band23", "band24", "band25", "band26", "band27", "band28", "band29", "band30", "band31"
		];

		arrBandNames.do ({arg band, i;
			// set current value on node
			if (this.moduleNode.notNil, {
				this.moduleNode.set(band, arrCurveValues.at(i));
			});
			// store current data to synthArgSpecs
			this.setSynthArgSpec(band, arrCurveValues.at(i));
		});
	}

	extraSaveData { // override default method
		^[arrCurveValues, arrSlotData];
	}

	loadExtraData {arg argData;  // override default method
		arrCurveValues = argData.at(0);
		arrSlotData = argData.at(1);
		this.updateSynthBandVals;
	}

}
