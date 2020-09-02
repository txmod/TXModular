// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXFormlets : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var midiNoteOnFunc;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Formlets";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Input gain", 1, "modInGain", 0],
			["Pitch bend", 1, "modPitchBend", 0],
			["Max attack", 1, "modMaxAttack", 0],
			["Max decay", 1, "modMaxDecay", 0],
			["Freq 1", 1, "modFreq1", 0],
			["Amp 1", 1, "modAmp1", 0],
			["Attack 1", 1, "modAttack1", 0],
			["Decay 1", 1, "modDecay1", 0],
			["Freq 2", 1, "modFreq2", 0],
			["Amp 2", 1, "modAmp2", 0],
			["Attack 2", 1, "modAttack2", 0],
			["Decay 2", 1, "modDecay2", 0],
			["Freq 3", 1, "modFreq3", 0],
			["Amp 3", 1, "modAmp3", 0],
			["Attack 3", 1, "modAttack3", 0],
			["Decay 3", 1, "modDecay3", 0],
			["Freq 4", 1, "modFreq4", 0],
			["Amp 4", 1, "modAmp4", 0],
			["Attack 4", 1, "modAttack4", 0],
			["Decay 4", 1, "modDecay4", 0],
			["Freq 5", 1, "modFreq5", 0],
			["Amp 5", 1, "modAmp5", 0],
			["Attack 5", 1, "modAttack5", 0],
			["Decay 5", 1, "modDecay5", 0],
			["Freq 6", 1, "modFreq6", 0],
			["Amp 6", 1, "modAmp6", 0],
			["Attack 6", 1, "modAttack6", 0],
			["Decay 6", 1, "modDecay6", 0],
			["Freq 7", 1, "modFreq7", 0],
			["Amp 7", 1, "modAmp7", 0],
			["Attack 7", 1, "modAttack7", 0],
			["Decay 7", 1, "modDecay7", 0],
			["Freq 8", 1, "modFreq8", 0],
			["Amp 8", 1, "modAmp8", 0],
			["Attack 8", 1, "modAttack8", 0],
			["Decay 8", 1, "modDecay8", 0],
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
			["freqMin", 40, defLagTime],
			["freqMax", 100, defLagTime],
			["quantise", 0, defLagTime],
			["pitchBend", 0.5, defLagTime],
			["pitchBendMin", -12, defLagTime],
			["pitchBendMax", 12, defLagTime],
			["maxAttack", 0.5, defLagTime],
			["maxAttackMin", 0.1, defLagTime],
			["maxAttackMax", 10, defLagTime],
			["maxDecay", 0.5, defLagTime],
			["maxDecayMin", 0.1, defLagTime],
			["maxDecayMax", 10, defLagTime],
			["freq1", 0.3, defLagTime],
			["amp1", 0.25, defLagTime],
			["attack1", 0.01, defLagTime],
			["decay1", 0.25, defLagTime],
			["freq2", 0.35, defLagTime],
			["amp2", 0.25, defLagTime],
			["attack2", 0.01, defLagTime],
			["decay2", 0.25, defLagTime],
			["freq3", 0.4, defLagTime],
			["amp3", 0.25, defLagTime],
			["attack3", 0.01, defLagTime],
			["decay3", 0.25, defLagTime],
			["freq4", 0.45, defLagTime],
			["amp4", 0.25, defLagTime],
			["attack4", 0.01, defLagTime],
			["decay4", 0.25, defLagTime],
			["freq5", 0.5, defLagTime],
			["amp5", 0.25, defLagTime],
			["attack5", 0.01, defLagTime],
			["decay5", 0.25, defLagTime],
			["freq6", 0.55, defLagTime],
			["amp6", 0.25, defLagTime],
			["attack6", 0.01, defLagTime],
			["decay6", 0.25, defLagTime],
			["freq7", 0.6, defLagTime],
			["amp7", 0.25, defLagTime],
			["attack7", 0.01, defLagTime],
			["decay7", 0.25, defLagTime],
			["freq8", 0.65, defLagTime],
			["amp8", 0.25, defLagTime],
			["attack8", 0.01, defLagTime],
			["decay8", 0.25, defLagTime],
			["outGain", 1, defLagTime],
			["wetDryMix", 1.0, defLagTime],
			["modInGain", 0, defLagTime],
			["modPitchBend", 0, defLagTime],
			["modMaxAttack", 0, defLagTime],
			["modMaxDecay", 0, defLagTime],
			["modFreq1", 0, defLagTime],
			["modAmp1", 0, defLagTime],
			["modAttack1", 0, defLagTime],
			["modDecay1", 0, defLagTime],
			["modFreq2", 0, defLagTime],
			["modAmp2", 0, defLagTime],
			["modAttack2", 0, defLagTime],
			["modDecay2", 0, defLagTime],
			["modFreq3", 0, defLagTime],
			["modAmp3", 0, defLagTime],
			["modAttack3", 0, defLagTime],
			["modDecay3", 0, defLagTime],
			["modFreq4", 0, defLagTime],
			["modAmp4", 0, defLagTime],
			["modAttack4", 0, defLagTime],
			["modDecay4", 0, defLagTime],
			["modFreq5", 0, defLagTime],
			["modAmp5", 0, defLagTime],
			["modAttack5", 0, defLagTime],
			["modDecay5", 0, defLagTime],
			["modFreq6", 0, defLagTime],
			["modAmp6", 0, defLagTime],
			["modAttack6", 0, defLagTime],
			["modDecay6", 0, defLagTime],
			["modFreq7", 0, defLagTime],
			["modAmp7", 0, defLagTime],
			["modAttack7", 0, defLagTime],
			["modDecay7", 0, defLagTime],
			["modFreq8", 0, defLagTime],
			["modAmp8", 0, defLagTime],
			["modAttack8", 0, defLagTime],
			["modDecay8", 0, defLagTime],
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
		synthDefFunc = { arg in, out, inGain, freqMin, freqMax, quantise,
			pitchBend, pitchBendMin, pitchBendMax,
			maxAttack, maxAttackMin, maxAttackMax,
			maxDecay, maxDecayMin, maxDecayMax,
			freq1, amp1, attack1, decay1, freq2, amp2, attack2, decay2,
			freq3, amp3, attack3, decay3, freq4, amp4, attack4, decay4,
			freq5, amp5, attack5, decay5, freq6, amp6, attack6, decay6,
			freq7, amp7, attack7, decay7, freq8, amp8, attack8, decay8,
			outGain, wetDryMix, modInGain = 0, modPitchBend = 0, modMaxAttack = 0, modMaxDecay = 0,
			modFreq1 = 0, modAmp1 = 0, modAttack1 = 0, modDecay1 = 0,
			modFreq2 = 0, modAmp2 = 0, modAttack2 = 0, modDecay2 = 0,
			modFreq3 = 0, modAmp3 = 0, modAttack3 = 0, modDecay3 = 0,
			modFreq4 = 0, modAmp4 = 0, modAttack4 = 0, modDecay4 = 0,
			modFreq5 = 0, modAmp5 = 0, modAttack5 = 0, modDecay5 = 0,
			modFreq6 = 0, modAmp6 = 0, modAttack6 = 0, modDecay6 = 0,
			modFreq7 = 0, modAmp7 = 0, modAttack7 = 0, modDecay7 = 0,
			modFreq8 = 0, modAmp8 = 0, modAttack8 = 0, modDecay8 = 0,
			modOutGain = 0, modWetDryMix = 0;
			var inGainSum, input, layerCount, holdSignal, quantiseFunc, levelControlFunc, inputDelayed;
			var pitchBendSum, maxAttackSum, maxDecaySum, mixSum, outGainSum, outSound;
			var arrAllFreqs, arrAllModFreqs, arrAllAmps, arrAllAttacks, arrAllDecays;
			var arrFreqs, arrFreqsQuant, arrAmps, arrAttacks, arrDecays, arrLayers;
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
			arrAllAttacks = [
				(attack1 + modAttack1).max(0).min(1),
				(attack2 + modAttack2).max(0).min(1),
				(attack3 + modAttack3).max(0).min(1),
				(attack4 + modAttack4).max(0).min(1),
				(attack5 + modAttack5).max(0).min(1),
				(attack6 + modAttack6).max(0).min(1),
				(attack7 + modAttack7).max(0).min(1),
				(attack8 + modAttack8).max(0).min(1),
			];
			arrAllDecays = [
				(decay1 + modDecay1).max(0).min(1),
				(decay2 + modDecay2).max(0).min(1),
				(decay3 + modDecay3).max(0).min(1),
				(decay4 + modDecay4).max(0).min(1),
				(decay5 + modDecay5).max(0).min(1),
				(decay6 + modDecay6).max(0).min(1),
				(decay7 + modDecay7).max(0).min(1),
				(decay8 + modDecay8).max(0).min(1),
			];
			layerCount = this.getSynthOption(0);
			arrAmps = arrAllAmps.copy.keep(layerCount);
			arrAttacks = arrAllAttacks.copy.keep(layerCount);
			arrDecays = arrAllDecays.copy.keep(layerCount);
			quantiseFunc = this.getSynthOption(1);
			pitchBendSum = (pitchBend + modPitchBend).max(0).min(1).linlin(0, 1, pitchBendMin, pitchBendMax);
			arrAllFreqs = [freq1 , freq2 , freq3 , freq4 , freq5 , freq6 , freq7 , freq8];
			arrAllModFreqs = [modFreq1 , modFreq2 , modFreq3 , modFreq4 , modFreq5 , modFreq6 , modFreq7 , modFreq8];
			arrFreqs = layerCount.collect({arg i;
				((arrAllFreqs[i] * 127) + (arrAllModFreqs[i] * (freqMax - freqMin))).max(freqMin).min(freqMax);
			});
			// arrFreqsQuant = quantiseFunc.value(arrFreqs.linlin(0, 1, freqMin, freqMax)) + pitchBendSum; // OLD
			arrFreqsQuant = quantiseFunc.value(arrFreqs) + pitchBendSum;
			maxAttackSum = (maxAttack + modMaxAttack).max(0).min(1).linexp(0, 1, maxAttackMin, maxAttackMax);
			maxDecaySum = (maxDecay + modMaxDecay).max(0).min(1).linexp(0, 1, maxDecayMin, maxDecayMax);
			arrLayers = layerCount.collect({arg i;
				arrAmps[i] *
				Formlet.ar(input * inGainSum, arrFreqsQuant[i].midicps, arrAttacks[i] * maxAttackSum,
					arrDecays[i] * maxDecaySum);
			});
			holdSignal = Mix.new(arrLayers);
			outSound = holdSignal * outGainSum / layerCount;
			levelControlFunc = this.getSynthOption(2);
			inputDelayed = TXLevelControl.arrDelayFunctions[arrOptions[2]].value(input);
			Out.ar(out, startEnv * (levelControlFunc.value(outSound, input) * mixSum) + (inputDelayed * (1-mixSum)));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["SynthOptionPopup", "Complexity", arrOptionData, 0],
			["EZslider", "In gain", ControlSpec(0, 2), "inGain"],
			["TXNoteRangeSlider", "Freq range", "freqMin", "freqMax", {}, true],
			["SynthOptionPopup", "Quantise", arrOptionData, 1],
			["TXMinMaxSliderSplit", "Pitch bend", ControlSpec(-48, 48), "pitchBend",
				"pitchBendMin", "pitchBendMax"],
			["TXMinMaxSliderSplit", "Max attack", ControlSpec(0.01, 60, 'exp'), "maxAttack",
				"maxAttackMin", "maxAttackMax"],
			["TXMinMaxSliderSplit", "Max decay", ControlSpec(0.01, 60, 'exp'), "maxDecay",
				"maxDecayMin", "maxDecayMax"],
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
		++ ["attack1", "attack2", "attack3", "attack4", "attack5", "attack6", "attack7", "attack8",
		].collect({arg item, i;
			["EZsliderUnmapped", item, {ControlSpec(0, this.getSynthArgSpec("maxAttack").linexp(0, 1,
				this.getSynthArgSpec("maxAttackMin"), this.getSynthArgSpec("maxAttackMax")))}, item]
		})
		++ ["decay1", "decay2", "decay3", "decay4", "decay5", "decay6", "decay7", "decay8",
		].collect({arg item, i;
			["EZsliderUnmapped", item, {ControlSpec(0, this.getSynthArgSpec("maxDecay").linexp(0, 1,
				this.getSynthArgSpec("maxDecayMin"), this.getSynthArgSpec("maxDecayMax")))}, item]
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
		var holdMaxAttack = this.getSynthArgSpec("maxAttack").linexp(0, 1,
			this.getSynthArgSpec("maxAttackMin"), this.getSynthArgSpec("maxAttackMax"));
		var holdMaxDecay = this.getSynthArgSpec("maxDecay").linexp(0, 1,
			this.getSynthArgSpec("maxDecayMin"), this.getSynthArgSpec("maxDecayMax"));

		guiSpecArray = [
			["ActionButton", "Gain", {displayOption = "showGain";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showGain")],
			["Spacer", 3],
			["ActionButton", "Parameters", {displayOption = "showParameters"; this.buildGuiSpecArray;
				system.showView;}, 130, TXColor.white, this.getButtonColour(displayOption == "showParameters")],
			["Spacer", 3],
			["ActionButton", "Layers", {displayOption = "showLayers";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showLayers")],
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
				["TXMinMaxSliderSplit", "Max attack", ControlSpec(0.01, 60, 'exp'), "maxAttack",
					"maxAttackMin", "maxAttackMax"],
				["SpacerLine", 10],
				["TXMinMaxSliderSplit", "Max decay", ControlSpec(0.01, 60, 'exp'), "maxDecay",
					"maxDecayMin", "maxDecayMax"],
			];
		});
		if (displayOption == "showLayers", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 2],
				["SynthOptionPopupPlusMinus", "Complexity", arrOptionData, 0, 210,
					{this.buildGuiSpecArray; system.showViewIfModDisplay(this)}],
				["Spacer", 20],
				//["TextBar", "MIDI Learn", 80, nil, nil, nil, \right],
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
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["attack1", "attack2", "attack3", "attack4",
					"attack5", "attack6", "attack7", "attack8", ]);}, 36],
				["TXMultiKnobNoUnmap", "Attack", ["attack1", "attack2", "attack3", "attack4",
					"attack5", "attack6", "attack7", "attack8", ], 8, ControlSpec(0, holdMaxAttack)],
				["NextLine"],
				["ActionButtonBig", "1->all",  {this.copy1ToAll(["decay1", "decay2", "decay3", "decay4",
					"decay5", "decay6", "decay7", "decay8", ]);}, 36],
				["TXMultiKnobNoUnmap", "Decay", ["decay1", "decay2", "decay3", "decay4",
					"decay5", "decay6", "decay7", "decay8", ], 8, ControlSpec(0, holdMaxDecay)],
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

