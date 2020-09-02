// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXKlank : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var midiNoteOnFunc;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Klank";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Input gain", 1, "modInGain", 0],
			["Pitch bend", 1, "modPitchBend", 0],
			["Max ring time", 1, "modMaxRingTime", 0],
			["Freq 1", 1, "modFreq1", 0],
			["Amp 1", 1, "modAmp1", 0],
			["Ring time 1", 1, "modRingTime1", 0],
			["Freq 2", 1, "modFreq2", 0],
			["Amp 2", 1, "modAmp2", 0],
			["Ring time 2", 1, "modRingTime2", 0],
			["Freq 3", 1, "modFreq3", 0],
			["Amp 3", 1, "modAmp3", 0],
			["Ring time 3", 1, "modRingTime3", 0],
			["Freq 4", 1, "modFreq4", 0],
			["Amp 4", 1, "modAmp4", 0],
			["Ring time 4", 1, "modRingTime4", 0],
			["Freq 5", 1, "modFreq5", 0],
			["Amp 5", 1, "modAmp5", 0],
			["Ring time 5", 1, "modRingTime5", 0],
			["Freq 6", 1, "modFreq6", 0],
			["Amp 6", 1, "modAmp6", 0],
			["Ring time 6", 1, "modRingTime6", 0],
			["Freq 7", 1, "modFreq7", 0],
			["Amp 7", 1, "modAmp7", 0],
			["Ring time 7", 1, "modRingTime7", 0],
			["Freq 8", 1, "modFreq8", 0],
			["Amp 8", 1, "modAmp8", 0],
			["Ring time 8", 1, "modRingTime8", 0],
			["Output gain", 1, "modOutGain", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.guiWidth = 700;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showGain";
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["inGain", 1, defLagTime],
			["freqMin", 0, defLagTime],
			["freqMax", 127, defLagTime],
			["quantise", 0, defLagTime],
			["pitchBend", 0.5, defLagTime],
			["pitchBendMin", -12, defLagTime],
			["pitchBendMax", 12, defLagTime],
			["maxRingTime", 0.5, defLagTime],
			["maxRingTimeMin", 0.1, defLagTime],
			["maxRingTimeMax", 10, defLagTime],
			["freq1", 0.3, defLagTime],
			["amp1", 0.25, defLagTime],
			["ringTime1", 0.25, defLagTime],
			["freq2", 0.35, defLagTime],
			["amp2", 0.25, defLagTime],
			["ringTime2", 0.25, defLagTime],
			["freq3", 0.4, defLagTime],
			["amp3", 0.25, defLagTime],
			["ringTime3", 0.25, defLagTime],
			["freq4", 0.45, defLagTime],
			["amp4", 0.25, defLagTime],
			["ringTime4", 0.25, defLagTime],
			["freq5", 0.5, defLagTime],
			["amp5", 0.25, defLagTime],
			["ringTime5", 0.25, defLagTime],
			["freq6", 0.55, defLagTime],
			["amp6", 0.25, defLagTime],
			["ringTime6", 0.25, defLagTime],
			["freq7", 0.6, defLagTime],
			["amp7", 0.25, defLagTime],
			["ringTime7", 0.25, defLagTime],
			["freq8", 0.65, defLagTime],
			["amp8", 0.25, defLagTime],
			["ringTime8", 0.25, defLagTime],
			["outGain", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modInGain", 0, defLagTime],
			["modPitchBend", 0, defLagTime],
			["modMaxRingTime", 0, defLagTime],
			["modFreq1", 0, defLagTime],
			["modAmp1", 0, defLagTime],
			["modRingTime1", 0, defLagTime],
			["modFreq2", 0, defLagTime],
			["modAmp2", 0, defLagTime],
			["modRingTime2", 0, defLagTime],
			["modFreq3", 0, defLagTime],
			["modAmp3", 0, defLagTime],
			["modRingTime3", 0, defLagTime],
			["modFreq4", 0, defLagTime],
			["modAmp4", 0, defLagTime],
			["modRingTime4", 0, defLagTime],
			["modFreq5", 0, defLagTime],
			["modAmp5", 0, defLagTime],
			["modRingTime5", 0, defLagTime],
			["modFreq6", 0, defLagTime],
			["modAmp6", 0, defLagTime],
			["modRingTime6", 0, defLagTime],
			["modFreq7", 0, defLagTime],
			["modAmp7", 0, defLagTime],
			["modRingTime7", 0, defLagTime],
			["modFreq8", 0, defLagTime],
			["modAmp8", 0, defLagTime],
			["modRingTime8", 0, defLagTime],
			["modOutGain", 0, defLagTime],
			["modWetDryMix", 0, defLagTime],

			// N.B. the args below aren't used in the synthdef, just stored here for convenience

			["midiLearn", 0],
			["midiLearnStep", 1],
		];
		arrOptions = [3, 0, 2]; // [no layers, quantise: off, level control:limiter]
		arrOptionData = [
			[
				["1 Layer", 1],
				["2 Layers", 2],
				["3 Layers", 3],
				["4 Layers", 4],
				["5 Layers", 5],
				["6 Layers", 6],
				["7 Layers", 7],
				["8 Layers", 8],
			],
			[ // quantise
				["Off", {arg in; in;}],
				["On - frequencies will be quantised to MIDI notes", {arg in; in.round;}],
			],
			TXLevelControl.arrOptionData,
		];
		synthDefFunc = { arg in, out, inGain, freqMin, freqMax, quantise, pitchBend, pitchBendMin, pitchBendMax,
			maxRingTime, maxRingTimeMin, maxRingTimeMax,
			freq1, amp1, ringTime1, freq2, amp2, ringTime2, freq3, amp3, ringTime3, freq4, amp4, ringTime4,
			freq5, amp5, ringTime5, freq6, amp6, ringTime6, freq7, amp7, ringTime7, freq8, amp8, ringTime8,
			outGain, wetDryMix, modInGain = 0, modPitchBend = 0, modMaxRingTime = 0,
			modFreq1 = 0, modAmp1 = 0, modRingTime1 = 0, modFreq2 = 0, modAmp2 = 0, modRingTime2 = 0,
			modFreq3 = 0, modAmp3 = 0, modRingTime3 = 0, modFreq4 = 0, modAmp4 = 0, modRingTime4 = 0,
			modFreq5 = 0, modAmp5 = 0, modRingTime5 = 0, modFreq6 = 0, modAmp6 = 0, modRingTime6 = 0,
			modFreq7 = 0, modAmp7 = 0, modRingTime7 = 0, modFreq8 = 0, modAmp8 = 0, modRingTime8 = 0,
			modOutGain = 0, modWetDryMix = 0;
			var inGainSum, input, layerCount, holdSignal, quantiseFunc, levelControlFunc, inputDelayed;
			var pitchBendSum, maxRingTimeSum, mixSum, outGainSum, outSound;
			var arrAllFreqs, arrAllModFreqs, arrAllAmps, arrAllRingTimes;
			var arrFreqs, arrFreqsQuant, arrAmps, arrRingTimes;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			inGainSum = (inGain + modInGain).max(0).min(2);
			input = TXClean.ar(startEnv * InFeedback.ar(in,1));
			mixSum = (wetDryMix + modWetDryMix).max(0).min(1);
			outGainSum = (outGain + modOutGain).max(0).min(1);
			arrAllAmps = [
				(amp1 + modAmp1).max(0).min(1),
				(amp2 + modAmp2).max(0).min(1),
				(amp3 + modAmp3).max(0).min(1),
				(amp4 + modAmp4).max(0).min(1),
				(amp5 + modAmp5).max(0).min(1),
				(amp6 + modAmp6).max(0).min(1),
				(amp7 + modAmp7).max(0).min(1),
				(amp8 + modAmp8).max(0).min(1),
			];
			arrAllRingTimes = [
				(ringTime1 + modRingTime1).max(0).min(1),
				(ringTime2 + modRingTime2).max(0).min(1),
				(ringTime3 + modRingTime3).max(0).min(1),
				(ringTime4 + modRingTime4).max(0).min(1),
				(ringTime5 + modRingTime5).max(0).min(1),
				(ringTime6 + modRingTime6).max(0).min(1),
				(ringTime7 + modRingTime7).max(0).min(1),
				(ringTime8 + modRingTime8).max(0).min(1),
			];
			layerCount = this.getSynthOption(0);
			arrAmps = arrAllAmps.copy.keep(layerCount);
			arrRingTimes = arrAllRingTimes.copy.keep(layerCount);
			quantiseFunc = this.getSynthOption(1);
			pitchBendSum = (pitchBend + modPitchBend).max(0).min(1).linlin(0, 1, pitchBendMin, pitchBendMax);
			arrAllFreqs = [freq1 , freq2 , freq3 , freq4 , freq5 , freq6 , freq7 , freq8];
			arrAllModFreqs = [modFreq1 , modFreq2 , modFreq3 , modFreq4 , modFreq5 , modFreq6 , modFreq7 , modFreq8];
			arrFreqs = layerCount.collect({arg i;
				((arrAllFreqs[i] * 127) + (arrAllModFreqs[i] * (freqMax - freqMin))).max(freqMin).min(freqMax);
			});
			// arrFreqsQuant = quantiseFunc.value(arrFreqs.linlin(0, 1, freqMin, freqMax)) + pitchBendSum; // OLD
			arrFreqsQuant = quantiseFunc.value(arrFreqs) + pitchBendSum;
			maxRingTimeSum = (maxRingTime + modMaxRingTime).max(0).min(1).linexp(0, 1, maxRingTimeMin, maxRingTimeMax);
			holdSignal = DynKlank.ar(`[arrFreqsQuant.midicps, arrAmps, arrRingTimes * maxRingTimeSum], input * inGainSum);
			outSound = holdSignal * outGainSum / layerCount;
			levelControlFunc = this.getSynthOption(2);
			inputDelayed = TXLevelControl.arrDelayFunctions[arrOptions[2]].value(input);
			Out.ar(out, (startEnv * levelControlFunc.value(outSound, input) * mixSum) + (inputDelayed * (1-mixSum)));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["SynthOptionPopup", "Complexity", arrOptionData, 0],
			["EZslider", "In gain", ControlSpec(0, 2), "inGain"],
			["TXNoteRangeSlider", "Freq range", "freqMin", "freqMax", {}, true],
			["SynthOptionPopup", "Quantise", arrOptionData, 1],
			["TXMinMaxSliderSplit", "Pitch bend", ControlSpec(-48, 48), "pitchBend",
				"pitchBendMin", "pitchBendMax"],
			["TXMinMaxSliderSplit", "Max ring time", ControlSpec(0.01, 60, 'exp'), "maxRingTime",
				"maxRingTimeMin", "maxRingTimeMax"],
		]
		++ ["freq1", "freq2", "freq3", "freq4", "freq5", "freq6", "freq7", "freq8",
		].collect({arg item, i;
			var specFunc, getValFunc, setValFunc;
			specFunc = {ControlSpec(this.getSynthArgSpec("freqMin").midicps,
				this.getSynthArgSpec("freqMax").midicps, 'exp')};
			getValFunc = {
				var spec = ControlSpec(this.getSynthArgSpec("freqMin"), this.getSynthArgSpec("freqMax"));
				var holdVal = this.getSynthArgSpec(item);
				(spec.constrain(holdVal * 127)).midicps;
			};
			setValFunc = {arg argVal;
				var holdVal = argVal.cpsmidi / 127;
				this.setSynthValue(item, holdVal);
			};
			/*
			Note valueActionNumber is used here to get specific behaviour for freqs
			*/
			["valueActionNumber", "Set " ++ item, [specFunc], getValFunc, setValFunc]
		})
		++ ["ringTime1", "ringTime2", "ringTime3", "ringTime4",
			"ringTime5", "ringTime6", "ringTime7", "ringTime8",
		].collect({arg item, i;
			["EZsliderUnmapped", item, {ControlSpec(0, this.getSynthArgSpec("maxRingTime").linexp(0, 1,
				this.getSynthArgSpec("maxRingTimeMin"), this.getSynthArgSpec("maxRingTimeMax")))}, item]
		})
		++ ["amp1", "amp2", "amp3", "amp4", "amp5", "amp6", "amp7", "amp8",
		].collect({arg item, i;
			["EZslider", item, ControlSpec(0, 1), item]
		})
		++ [
			["EZslider", "Out gain", ControlSpec(0, 1), "outGain"],
			["SynthOptionPopupPlusMinus", "Level control", arrOptionData, 2],
			["WetDryMixSlider"],
		]);
	//	use base class initialise
	this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	buildGuiSpecArray {
		var holdFreqMin = this.getSynthArgSpec("freqMin");
		var holdFreqMax = this.getSynthArgSpec("freqMax");
		var holdMaxRingTime = this.getSynthArgSpec("maxRingTime").linexp(0, 1,
			this.getSynthArgSpec("maxRingTimeMin"), this.getSynthArgSpec("maxRingTimeMax"));

		guiSpecArray = [
			["ActionButton", "Gain", {displayOption = "showGain";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showGain")],
			["Spacer", 3],
			["ActionButton", "Parameters", {displayOption = "showParameters"; this.buildGuiSpecArray;
				system.showView;}, 130, TXColor.white, this.getButtonColour(displayOption == "showParameters")],
			["Spacer", 3],
			["ActionButton", "Layers", {displayOption = "showLayers"; this.buildGuiSpecArray;
				system.showView;}, 130, TXColor.white, this.getButtonColour(displayOption == "showLayers")],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 2],
		];

		if (displayOption == "showGain", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["EZslider", "In gain", ControlSpec(0, 2), "inGain"],
				["SpacerLine", 20],
				["EZslider", "Out gain", ControlSpec(0, 1), "outGain"],
				["SpacerLine", 20],
				["SynthOptionPopupPlusMinus", "Level control", arrOptionData, 2],
				["SpacerLine", 20],
				["WetDryMixSlider"],
			];
		});
		if (displayOption == "showParameters", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["TXNoteRangeSlider", "Freq range", "freqMin", "freqMax", {}, true],
				["SpacerLine", 6],
				["SynthOptionPopupPlusMinus", "Quantise", arrOptionData, 1],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Pitch bend", ControlSpec(-48, 48), "pitchBend",
				"pitchBendMin", "pitchBendMax"],
				["NextLine"],
				["DividingLine"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Max ring time", ControlSpec(0.01, 60, 'exp'), "maxRingTime",
					"maxRingTimeMin", "maxRingTimeMax"],
			];
		});
		if (displayOption == "showLayers", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Complexity", arrOptionData, 0, 210,
					{this.buildGuiSpecArray; system.showViewIfModDisplay(this)}],
				["Spacer", 20],
				["TXCheckBox", "MIDI learn notes & amps", "midiLearn", {arg view; this.setMidiLearnFunc(view.value);}, 180],
				["EZNumber", "Next Layer", ControlSpec(1, 8, step: 1), "midiLearnStep"],
				["ActionButton", "Set Next Layer: 1", {this.setSynthArgSpec("midiLearnStep", 1); system.showView;}, 100],
				["SpacerLine", 16],
				["TextBar", "Layers", 80],
				["TextBar", "1", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(1)],
				["TextBar", "2", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(2)],
				["TextBar", "3", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(3)],
				["TextBar", "4", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(4)],
				["TextBar", "5", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(5)],
				["TextBar", "6", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(6)],
				["TextBar", "7", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(7)],
				["TextBar", "8", 50, 20, TXColor.sysGuiCol1, this.getLayerCol(8)],
				["SpacerLine", 2],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["freq1", "freq2", "freq3", "freq4",
					"freq5", "freq6", "freq7", "freq8", ]);}, 36],
				["TXMultiKnobMidiNote", "Freq: midi note", ["freq1", "freq2", "freq3", "freq4",
					"freq5", "freq6", "freq7", "freq8", ], 8, ControlSpec(holdFreqMin / 127, holdFreqMax / 127)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["ringTime1", "ringTime2", "ringTime3", "ringTime4",
					"ringTime5", "ringTime6", "ringTime7", "ringTime8", ]);}, 36],
				["TXMultiKnobNoUnmap", "Ring time", ["ringTime1", "ringTime2", "ringTime3", "ringTime4",
					"ringTime5", "ringTime6", "ringTime7", "ringTime8", ], 8, ControlSpec(0, holdMaxRingTime)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["amp1", "amp2", "amp3", "amp4",
					"amp5", "amp6", "amp7", "amp8", ]);}, 36],
				["TXMultiKnobNo", "Amp", ["amp1", "amp2", "amp3", "amp4",
					"amp5", "amp6", "amp7", "amp8", ], 8, ControlSpec(0, 1)],
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

	getLayerCol { arg layerNo;
		if (this.getSynthOption(0) < layerNo, {
			^TXColor.grey(0.8);
		},{
			^TXColor.white;
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

	setMidiLearnFunc { arg argSwitch = 0;
		var arrFreqStrings = ["freq1", "freq2", "freq3", "freq4",
					"freq5", "freq6", "freq7", "freq8", ];
		var arrAmpStrings = ["amp1", "amp2", "amp3", "amp4",
					"amp5", "amp6", "amp7", "amp8", ];
		// stop any old funcs
		this.stopMidiLearn;
		// if requested start midi
		if (argSwitch == 1, {
			midiNoteOnFunc = MIDIFunc.noteOn({ |vel, num, chan|
				var stepNo;
				if (vel > 0, {
					stepNo = this.getSynthArgSpec("midiLearnStep");
					// store freq and amp
					this.setSynthValue(arrFreqStrings[stepNo - 1], num / 127);
					this.setSynthValue(arrAmpStrings[stepNo - 1], vel / 127);
					// go to next step
					this.setSynthArgSpec("midiLearnStep", stepNo + 1);
					if ((stepNo + 1) > 8, {
						this.stopMidiLearn;
						this.setSynthArgSpec("midiLearn", 0);
						this.setSynthArgSpec("midiLearnStep", 1);
					});
					// refresh view
					system.showView;
				});
			});
		}, {
			// refresh view
			system.showViewIfModDisplay(this);
		});
	}

	stopMidiLearn {
		// remove any old funcs
		if (midiNoteOnFunc.notNil, {
			midiNoteOnFunc.free;
			midiNoteOnFunc = nil;
		});
	}

	deleteModuleExtraActions { // override base module
		this.stopMidiLearn;
	}

}

