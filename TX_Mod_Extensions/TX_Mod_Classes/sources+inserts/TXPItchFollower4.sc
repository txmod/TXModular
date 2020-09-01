// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXPItchFollower4 : TXModuleBase {

	classvar <>classData;

	var  holdTrigID, holdTrigID2, holdOSCResp, holdOSCResp2;
	var	holdCurrPitch1, holdCurrPitch2, holdCurrPitch3, holdCurrPitch4, holdCurrPitch5, holdCurrPitch6;
	var	holdCurrPitchView1, holdCurrPitchView2, holdCurrPitchView3, holdCurrPitchView4, holdCurrPitchView5, holdCurrPitchView6;
	var	holdPitchMin, holdPitchMinView, holdPitchMax, holdPitchMaxView;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.pitchControlSpec = ControlSpec(40.0, 5000.0, 'exp');
		classData.defaultName = "Pitch Follower";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrAudSCInBusSpecs = [
			["Audio in", 1, "audioIn"]
		];
		classData.arrCtlSCInBusSpecs = [
			["Follow time", 1, "modLag", 0]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["PItch Out", [0]],
			["Pitch Found", [1]],
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		holdTrigID = UniqueID.next;
		holdTrigID2 = UniqueID.next;
		holdCurrPitch1 = 0;
		holdCurrPitch2 = 0;
		holdCurrPitch3 = 0;
		holdCurrPitch4 = 0;
		holdCurrPitch5 = 0;
		holdCurrPitch6 = 0;
		holdPitchMin = 999999;
		holdPitchMax = 0;
		arrSynthArgSpecs = [
			["out", 0, 0],
			["audioIn", 0, 0],
			["sendPitch", 0, 0],
			["i_minFreq", 60.0, \ir],
			["i_maxFreq", 4000.0, \ir],
			["lag", 0.01010101010101, defLagTime], // = ControlSpec(10, 1000).unmap(20)
			["lagMin", 10, defLagTime],
			["lagMax", 1000, defLagTime],
			["modLag", 0, defLagTime],
		];
		arrOptions = [0, 0, 0];
		arrOptionData = [
			[
				["Off", { 0; }],
				["On", {arg input, ampGate;
					var freq, hasFreq;
					# freq, hasFreq = Pitch.kr(input, 40, 40, 5000, ampThreshold: 0.02);
					SendTrig.kr((Impulse.kr(10) * ampGate), holdTrigID2, freq);
				}],
			],
			[
				// update function
				["Off", {  }],
				["On", {this.copyMinMaxPitch; }],
			],
			[
				["Type 1 - Tartini method, slower & more accurate",
					{arg input, minFreq, maxFreq;
						var holdFreq, holdHasFreq, holdValidFreq;
						# holdFreq, holdHasFreq = Tartini.kr(input);
						holdValidFreq = (holdFreq >= minFreq) * (holdFreq <= maxFreq);
						[holdFreq, holdValidFreq * holdHasFreq];
						//					[holdFreq, holdHasFreq];
					}
				],
				["Type 2 - Original method, faster & less accurate",
					{arg input, minFreq, maxFreq;
						Pitch.kr(input, minFreq, minFreq, maxFreq, ampThreshold: 0.02, median: 7);
					}
				],
			]
		];
		synthDefFunc = { arg out, audioIn, sendPitch, i_minFreq, i_maxFreq, lag, lagMin, lagMax, modLag=0;
			var input, lagtime, freq, hasFreq, freqOut, minMaxPitchFunc, amplitude, ampGate, pitchDetectFunction;
			input = InFeedback.ar(audioIn,1) * TXEnvPresets.startEnvFunc.value;
			amplitude = Amplitude.kr(input, 0.01, 0.01);
			ampGate = (amplitude - 0.01).ceil;
			lagtime = lagMin + ((lagMax - lagMin) * (lag + modLag).max(0.001).min(1));
			pitchDetectFunction = this.getSynthOption(2);
			# freq, hasFreq = pitchDetectFunction.value(input, i_minFreq, i_maxFreq);
			freqOut = (log(freq/i_minFreq) / log(i_maxFreq / i_minFreq)).max(0).min(1);
			// sends current pitch if requested
			SendTrig.kr(sendPitch * Impulse.kr(10) * ampGate, holdTrigID, freq);
			minMaxPitchFunc = this.getSynthOption(0);
			minMaxPitchFunc.value(input, ampGate);
			Out.kr(out, [
				Lag.kr(			// lag the input
					//				freqOut,
					Gate.kr(freqOut, ampGate),
					lagtime/1000		//  lag time - divide to get seconds
				),
				Lag.kr(			// lag and round the input
					hasFreq * ampGate,
					lagtime/1000		//  lag  time - divide to get seconds
				).round,
			]);
		};
		guiSpecArray = [
			["SpacerLine", 2],
			["SynthOptionPopupPlusMinus", "Pitch detector", arrOptionData, 2, nil, {system.flagGuiUpd}],
			["SpacerLine", 2],
			["TXRangeSlider", "Freq min-max", classData.pitchControlSpec, "i_minFreq", "i_maxFreq",
				{this.checkMinMax; this.rebuildSynth;},
				[
					["Presets: ", {}],
					["Piano: Midi 21 - 108", [21, 108].midicps;],
					["Bass Guitar: Midi 24 - 60", [24, 60].midicps;],
					["Harp: Midi 24 - 103", [24, 103].midicps;],
					["Double Bass: Midi 28 - 67", [28, 67].midicps;],
					["Harpsichord: Midi 29 - 89", [29, 89].midicps;],
					["Bassoon: Midi 34 - 75", [34, 75].midicps;],
					["French Horn: Midi 34 - 77", [34, 77].midicps;],
					["Organ: Midi 36 - 96", [36, 96].midicps;],
					["Cello: Midi 36 - 76", [36, 76].midicps;],
					["Timpani: Midi 40 - 55", [40, 55].midicps;],
					["Guitar: Midi 40 - 76", [40, 76].midicps;],
					["Male Voice: Midi 41 - 72", [41, 72].midicps;],
					["Clarinet: Midi 50 - 94", [50, 94].midicps;],
					["Vibraphone: Midi 53 - 89", [53, 89].midicps;],
					["Female Voice: Midi 52 - 84", [52, 84].midicps;],
					["Violin: Midi 55 - 103", [55, 103].midicps;],
					["Trumpet: Midi 55 - 82", [55, 82].midicps;],
					["Oboe: Midi 58 - 91", [58, 91].midicps;],
					["Flute: Midi 60 - 96", [60, 96].midicps;],
					["Piccolo: Midi 74 - 102", [74, 102].midicps;],
				]
			],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Follow time", this.getLagControlSpec, "lag", "lagMin", "lagMax"],
			["SpacerLine", 4],

			["ActionButton", "Calibrate On", {
				Routine.run {
					arrOptions.put(0, 1);
					arrOptions.put(1, 1);
					this.rebuildSynth;
					// pause
					0.5.wait;
					holdPitchMin = 999999; holdPitchMax = 0;
					system.flagGuiUpd;
				};
			}, 120, TXColour.white, TXColour.sysGuiCol1],
			["ActionButton", "Calibrate Off", {
				Routine.run {
					arrOptions.put(0, 0);
					arrOptions.put(1, 0);
					this.rebuildSynth;
					// pause
					0.5.wait;
					holdPitchMin = 999999; holdPitchMax = 0;
					system.flagGuiUpd;
				};
			}, 120, TXColour.white, TXColour.sysGuiCol2],
			["NextLine"],
			["SpacerLine", 2],
			["SynthOptionCheckBox", "Detect min & max", arrOptionData, 0, 150],
			["ActionButton", "Copy detected values to min/max",
				{this.copyMinMaxPitch}, 200, TXColor.white, TXColor.sysGuiCol2],
			["SpacerLine", 4],
			["SynthOptionCheckBox", "Automatically copy detected values to min/max", arrOptionData, 1, nil,
				{this.copyMinMaxPitch}],
			["SpacerLine", 4],
			["TXStaticText", "Detected min", {holdPitchMin.asString},
				{arg view; holdPitchMinView = view.textView;}, 150],
			["TXStaticText", "Detected max", {holdPitchMax.asString},
				{arg view; holdPitchMaxView = view.textView; this.resetMinMaxPitchDisplay;}, 150],
			["ActionButton", "Reset", {holdPitchMin = 999999; holdPitchMax = 0; this.resetMinMaxPitchDisplay;},
				80, TXColor.white, TXColor.sysDeleteCol],


			["SpacerLine", 6],
			["TXStaticText", "Current Pitch", {holdCurrPitch6}, {arg view; holdCurrPitchView6 = view.textView}, 200],
			["ActionButton", "Detect Current Pitch",
				{this.moduleNode.set("sendPitch", 1);
					{this.moduleNode.set("sendPitch", 0);}.defer(0.11)},
				150],
			["NextLine"],
			["TXStaticText", "Old Pitch 1", {holdCurrPitch5}, {arg view; holdCurrPitchView5 = view.textView}, 200],
			["ActionButton", "Reset", {this.resetPitchDisplay}, 150, TXColor.white, TXColor.sysDeleteCol],
			["NextLine"],
			["TXStaticText", "Old Pitch 2", {holdCurrPitch4}, {arg view; holdCurrPitchView4 = view.textView}, 200],
			["NextLine"],
			["TXStaticText", "Old Pitch 3", {holdCurrPitch3}, {arg view; holdCurrPitchView3 = view.textView}, 200],
			["NextLine"],
			["TXStaticText", "Old Pitch 4", {holdCurrPitch2}, {arg view; holdCurrPitchView2 = view.textView}, 200],
			["NextLine"],
			["TXStaticText", "Old Pitch 5", {holdCurrPitch1}, {arg view; holdCurrPitchView1 = view.textView}, 200],
		];
		arrActionSpecs = this.buildActionSpecs(
			[	["commandAction", "Calibrate All On", {
				Routine.run {
					arrOptions.put(0, 1);
					arrOptions.put(1, 1);
					this.rebuildSynth;
					// pause
					0.5.wait;
					holdPitchMin = 999999; holdPitchMax = 0;
					system.flagGuiUpd;
				};
			}],
			["commandAction", "Calibrate All Off", {
				Routine.run {
					arrOptions.put(0, 0);
					arrOptions.put(1, 0);
					this.rebuildSynth;
					// pause
					0.5.wait;
					holdPitchMin = 999999; holdPitchMax = 0;
					system.flagGuiUpd;
				};
			}],
			]
			++ guiSpecArray
		);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
		this.oscActivate;
	}

	getLagControlSpec {
		^ ControlSpec(1, 10000);
	}
	getFreqControlSpec {
		^ [this.getSynthArgSpec("i_minFreq"), this.getSynthArgSpec("i_maxFreq"), 'exp'].asSpec;
	}

	oscActivate {
		//	remove any previous responders and add new
		this.oscDeactivate;
		holdOSCResp = OSCFunc({arg msg, time, addr,  recvPort;
			if (msg[2] == holdTrigID, {this.setCurrPitch(msg[3])} );
		}, '/tr', system.server.addr);
		holdOSCResp2 = OSCFunc({arg msg, time, addr,  recvPort;
			if ((msg[2] == holdTrigID2) and: (msg[3] > 40 ), {this.setMinMaxPitch(msg[3])} );
		}, '/tr', system.server.addr);
	}

	oscDeactivate {
		//	remove responder
		if (holdOSCResp.class == OSCFunc, {holdOSCResp.free});
		if (holdOSCResp2.class == OSCFunc, {holdOSCResp2.free});
	}

	deleteModuleExtraActions {
		//	remove responders
		this.oscDeactivate;
	}

	setCurrPitch {arg argPitch;
		holdCurrPitch1 = holdCurrPitch2;
		holdCurrPitch2 = holdCurrPitch3;
		holdCurrPitch3 = holdCurrPitch4;
		holdCurrPitch4 = holdCurrPitch5;
		holdCurrPitch5 = holdCurrPitch6;
		holdCurrPitch6 = argPitch.round(0.1);
		{holdCurrPitchView1.string = holdCurrPitch1.asString;}.defer;
		{holdCurrPitchView2.string = holdCurrPitch2.asString;}.defer;
		{holdCurrPitchView3.string = holdCurrPitch3.asString;}.defer;
		{holdCurrPitchView4.string = holdCurrPitch4.asString;}.defer;
		{holdCurrPitchView5.string = holdCurrPitch5.asString;}.defer;
		{holdCurrPitchView6.string = holdCurrPitch6.asString;}.defer;
	}

	resetPitchDisplay {arg argPitch;
		holdCurrPitch1 = 0;
		holdCurrPitch2 = 0;
		holdCurrPitch3 = 0;
		holdCurrPitch4 = 0;
		holdCurrPitch5 = 0;
		holdCurrPitch6 = 0;
		{holdCurrPitchView1.string = " ";}.defer;
		{holdCurrPitchView2.string = " ";}.defer;
		{holdCurrPitchView3.string = " ";}.defer;
		{holdCurrPitchView4.string = " ";}.defer;
		{holdCurrPitchView5.string = " ";}.defer;
		{holdCurrPitchView6.string = " ";}.defer;
	}

	setMinMaxPitch {arg argPitch;
		var holdPitch = argPitch;
		if (holdPitch.notNil, {
			if (holdPitch < holdPitchMin, {
				holdPitchMin = holdPitch.round(0.1);
				if (system.isModDisplay(this) == true, {
					{holdPitchMinView.string = holdPitchMin.asString;}.defer;
				});
			});
			if (holdPitch > holdPitchMax, {
				holdPitchMax = holdPitch.round(0.1);
				if (system.isModDisplay(this) == true, {
					{holdPitchMaxView.string = holdPitchMax.asString;}.defer;
				});
			});
			// run update function
			this.getSynthOption(1).value;
		});
	}

	resetMinMaxPitchDisplay {arg argPitch;
		{	if (holdPitchMin == 999999, {holdPitchMinView.string = " ";});
			if (holdPitchMax == 0, {holdPitchMaxView.string = " ";});
		}.defer;
	}

	checkMinMax {
		var holdMin, holdMax;
		holdMin = this.getSynthArgSpec("i_minFreq");
		holdMax = this.getSynthArgSpec("i_maxFreq");
		if (holdMin > holdMax, {
			this.setSynthArgSpec("i_minFreq", holdMax);
			this.setSynthArgSpec("i_maxFreq", holdMin);
			system.flagGuiUpd;
		});
	}

	copyMinMaxPitch {
		if (holdPitchMin < 999999, {
			this.setSynthArgSpec("i_minFreq", classData.pitchControlSpec.constrain(holdPitchMin));
		});
		if (holdPitchMax > 0, {
			this.setSynthArgSpec("i_maxFreq", classData.pitchControlSpec.constrain(holdPitchMax));
		});
		this.rebuildSynth;
		system.flagGuiUpd;
	}

}

