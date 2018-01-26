// Copyright (C) 2010 Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXAnalyser3 : TXModuleBase {

	classvar <>classData;

	var	displayOption;
	var holdTrigID, holdTrigID2, holdTrigID3, holdTrigID4, holdTrigID5;
	var holdOSCResp, holdOSCResp2, holdOSCResp3, holdOSCResp4, holdOSCResp5;
	var	holdAmpMin, holdAmpMinView, holdAmpMax, holdAmpMaxView;
	var	holdFlatnessMin, holdFlatnessMinView, holdFlatnessMax, holdFlatnessMaxView;
	var	holdPercentileMin, holdPercentileMinView, holdPercentileMax, holdPercentileMaxView;
	var	holdCentroidMin, holdCentroidMinView, holdCentroidMax, holdCentroidMaxView;
	var arrActions;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.holdControlSpec = ControlSpec(60, 22000, 'lin', 1, 0);
		classData.defaultName = "Analyser";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrAudSCInBusSpecs = [
			["Audio in", 1, "audioIn"]
		];
		classData.arrCtlSCInBusSpecs = [];
		classData.noOutChannels = 5;
		classData.arrOutBusSpecs = [
			["Amplitude", [0]],
			["Flatness", [1]],
			["Percentile", [2]],
			["Centroid", [3]],
			["Onsets", [4]],
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set class specific instance variables
		displayOption = "showInfo1";
		holdTrigID = UniqueID.next;
		holdTrigID2 = UniqueID.next;
		holdTrigID3 = UniqueID.next;
		holdTrigID4 = UniqueID.next;
		holdTrigID5 = UniqueID.next;
		holdPercentileMin = 999999;
		holdPercentileMax = 0;
		holdCentroidMin = 999999;
		holdCentroidMax = 0;
		holdFlatnessMin = 1;
		holdFlatnessMax = 0;
		holdAmpMin = 1;
		holdAmpMax = 0;
		arrActions = [99,0,0,0,0,0,0, nil].dup(5);

		arrSynthArgSpecs = [
			["out", 0, 0],
			["audioIn", 0, 0],
			["ampMin", 0, 0],
			["ampMax", 1, 0],
			["flatnessMin", 0, 0],
			["flatnessMax", 1, 0],
			["percentileMin", 60, 0],
			["percentileMax", 22000, 0],
			["centroidMin", 60, 0],
			["centroidMax", 22000, 0],
			["threshold", 0.5, 0],
			["gateThreshold", 0.01, 0],
		];

		arrOptions = [1, 1, 1, 18, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0];
		arrOptionData = [
			[
				// 0 - spectral flatness detection on/off
				["Off", {DC.kr(0);}],
				["On", {arg chain, flatnessMin, flatnessMax;
					SpecFlatness.kr(chain).max(flatnessMin).min(flatnessMax);
				}],
			],
			[
				// 1 - spectral percentile detection on/off
				["Off", {DC.kr(0);}],
				["On", {arg chain, percentileMin, percentileMax, percentile;
					SpecPcile.kr(chain, percentile, 1).max(percentileMin).min(percentileMax);
				}],
			],
			[
				// 2 - spectral centroid detection on/off
				["Off", {DC.kr(0);}],
				["On", {arg chain, centroidMin, centroidMax;
					SpecCentroid.kr(chain).max(centroidMin).min(centroidMax);
				}],
			],
			[
				// 3 - percentile value
				["5 %", 0.5 ],
				["10 %", 0.1 ],
				["15 %", 0.15 ],
				["20 %", 0.2 ],
				["25 %", 0.25 ],
				["30 %", 0.3 ],
				["35 %", 0.35 ],
				["40 %", 0.4 ],
				["45 %", 0.45 ],
				["50 %", 0.5 ],
				["55 %", 0.55 ],
				["60 %", 0.6 ],
				["65 %", 0.65 ],
				["70 %", 0.7 ],
				["75 %", 0.75 ],
				["80 %", 0.8 ],
				["85 %", 0.85 ],
				["90 %", 0.9 ],
				["95 % - default", 0.95 ],
			],
			[
				// 4 - onset detection type
				["power", \power  ],
				["magsum", \magsum  ],
				["complex ", \complex  ],
				["rcomplex - default ", \rcomplex  ],
				["phase", \phase  ],
				["wphase", \wphase  ],
				["mkl ", \mkl  ],
			],
			[
				// 5 - onset detection on/off
				["Off", {DC.kr(0);}],
				["On", {arg chain, threshold, type;
					var onsetOut;
					onsetOut = Onsets.kr(chain, threshold, type);
					SendTrig.kr(onsetOut, holdTrigID5, 1);
					onsetOut;
				}],
			],
			[
				// 6 - spectral percentile analysis on/off
				["Off", {DC.kr(0);}],
				["On", {arg chain, trigImpulse, percentile, ampGate;
					var freq;
					freq = SpecPcile.kr(chain, percentile, 1).max(60).min(22000);
					SendTrig.kr((trigImpulse * ampGate), holdTrigID, freq);
				}],

			],
			[
				// 7 - spectral centroid analysis on/off
				["Off", {DC.kr(0);}],
				["On", {arg chain, trigImpulse, ampGate;
					var freq;
					freq = SpecCentroid.kr(chain).max(60).min(22000);
					SendTrig.kr((trigImpulse * ampGate), holdTrigID2, freq);
				}],
			],
			[
				// 8 - spectral percentile update function
				["Off", {  }],
				["On", {this.copyMinMaxPercentile; }],
			],
			[
				// 9 - spectral centroid update function
				["Off", {  }],
				["On", {this.copyMinMaxCentroid;}],
			],
			[
				// 10 - flatness analysis on/off
				["Off", {DC.kr(0);}],
				["On", {arg chain, trigImpulse, ampGate;
					var holdFlatness;
					holdFlatness = SpecFlatness.kr(chain).max(0).min(1);
					SendTrig.kr((trigImpulse * ampGate), holdTrigID3, holdFlatness);
				}],
			],
			[
				// 11 - flatness update function
				["Off", {  }],
				["On", {this.copyMinMaxFlatness; }],
			],
			[
				// 12 - amplitude analysis on/off
				["Off", {DC.kr(0);}],
				["On", {arg in, trigImpulse;
					var holdAmp;
					// replace Amplitude with PeakFollower - now reverted but
					//    with A2K now used with Amplitude.ar
					//  holdAmp = A2K.kr(PeakFollower.ar(in, 0.999).max(0).min(1));
					holdAmp = LagUD.kr(A2K.kr(Amplitude.ar(in, 0.001, 0.1)), 0.001, 0.1).max(0).min(1);
					SendTrig.kr(trigImpulse, holdTrigID4, holdAmp);
				}],
			],
			[
				// 13 - amplitude update function
				["Off", {  }],
				["On", {this.copyMinMaxAmp; }],
			],
		];

		synthDefFunc = { arg out, audioIn, ampMin, ampMax, flatnessMin, flatnessMax,
			percentileMin, percentileMax, centroidMin, centroidMax, threshold, gateThreshold;
			var lagtime, imp;
			var in, chain, ampOut, flatness, flatnessOut, percentileVal, centroid, percentile, percentileOut,
			centroidOut, amplitude, ampGate, detectType, onsets;
			var	ampAnalysisFunc, flatnessAnalysisFunc, percentileAnalysisFunc, centroidAnalysisFunc, trigImpulse;

			in = TXClean.ar(InFeedback.ar(audioIn,1));

			// replace Amplitude with PeakFollower - now reverted but  A2K now used with Amplitude.ar
			//		amplitude = LagUD.kr(Amplitude.kr(in, 0.1, 0.1), 0.01, 0.1);
			//		amplitude = PeakFollower.kr(in, 0.999);
			amplitude = LagUD.kr(A2K.kr(Amplitude.ar(in, 0.001, 0.1)), 0.001, 0.1);
			ampGate = (amplitude - gateThreshold).ceil;
			trigImpulse = Impulse.kr(20);

			// FFT set to hop 0.25 with hanning window
			// chain = FFT(LocalBuf(2048), in, 0.25, 1); //
			chain = FFT(LocalBuf(2048), in); // alt

			// select function based on arrOptions
			flatness = Gate.kr(this.getSynthOption(0).value(chain, flatnessMin, flatnessMax), ampGate);
			percentileVal = this.getSynthOption(3);
			percentile = this.getSynthOption(1).value(chain, percentileMin, percentileMax, percentileVal);
			centroid = this.getSynthOption(2).value(chain, centroidMin, centroidMax);

			// send current amplitude, flatness, percentile & centroid if requested
			ampAnalysisFunc = this.getSynthOption(12);
			ampAnalysisFunc.value(in, trigImpulse);
			flatnessAnalysisFunc = this.getSynthOption(10);
			flatnessAnalysisFunc.value(chain, trigImpulse, ampGate);
			percentileAnalysisFunc = this.getSynthOption(6);
			percentileAnalysisFunc.value(chain, trigImpulse, percentileVal, ampGate);
			centroidAnalysisFunc = this.getSynthOption(7);
			centroidAnalysisFunc.value(chain, trigImpulse, ampGate);

			ampOut = (amplitude.max(ampMin).min(ampMax) - ampMin) / (ampMax - ampMin);
			flatnessOut = (flatness - flatnessMin) / (flatnessMax - flatnessMin);
			percentileOut = Gate.kr(log(percentile/percentileMin) / log(percentileMax / percentileMin), ampGate);
			centroidOut = Gate.kr(log(centroid/centroidMin) / log(centroidMax / centroidMin), ampGate);

			detectType = this.getSynthOption(4);
			onsets =  this.getSynthOption(5).value(chain, threshold, detectType);

			Out.kr(out, [ampOut, flatnessOut, percentileOut, centroidOut, onsets]);
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Calibrate All On", {
				this.calibrateAllOn;
			}],
			["commandAction", "Calibrate All Off", {
				this.calibrateAllOff;
			}],
			["EZslider", "Gate threshold", ControlSpec(0.01, 1), "gateThreshold"],
			["EZNumber", "Amp min", ControlSpec(0, 1), "ampMin", nil, 80, 66],
			["EZNumber", "Amp max", ControlSpec(0, 1), "ampMax", nil, 80, 66],
			["SynthOptionCheckBox", "Detect amplitude min & max", arrOptionData, 12, 150],
			["SynthOptionCheckBox", "Automatically copy detected amplitude values to min/max", arrOptionData, 13],
			["SynthOptionCheckBox", "Spectral Flatness", arrOptionData, 0],
			["EZNumber", "Flatness min", ControlSpec(0, 1), "flatnessMin", nil, 80, 66],
			["EZNumber", "Flatness max", ControlSpec(0, 1), "flatnessMax", nil, 80, 66],
			["SynthOptionCheckBox", "Detect flatness min & max", arrOptionData, 10, 150],
			["SynthOptionCheckBox", "Automatically copy detected flatness values to min/max", arrOptionData, 11],
			["SynthOptionCheckBox", "Spectral Percentile", arrOptionData, 1],
			["SynthOptionPopup", "Percentile", arrOptionData, 3, 290],
			["EZNumber", "Min freq", classData.holdControlSpec, "percentileMin", nil, 80, 66],
			["EZNumber", "Max freq", classData.holdControlSpec, "percentileMax", nil, 80, 66],
			["SynthOptionCheckBox", "Detect percentile min & max", arrOptionData, 6, 150],
			["SynthOptionCheckBox", "Automatically copy detected percentile values to min/max", arrOptionData, 8],
			["SynthOptionCheckBox", "Spectral Centroid", arrOptionData, 2],
			["EZNumber", "Centroid min", classData.holdControlSpec, "centroidMin", nil, 80, 66],
			["EZNumber", "Centroid max", classData.holdControlSpec, "centroidMax", nil, 80, 66],
			["SynthOptionCheckBox", "Detect centroid min & max", arrOptionData, 7, 150],
			["SynthOptionCheckBox", "Automatically copy detected centroid values to min/max", arrOptionData, 9],
			["SynthOptionCheckBox", "Onset Detection", arrOptionData, 5],
			["EZslider", "Threshold", ControlSpec(0, 1), "threshold"],
			["SynthOptionPopup", "Detection type", arrOptionData, 4, 250],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
		this.oscActivate;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["ActionButton", "Amp / Flatness", {displayOption = "showInfo1";
				this.buildGuiSpecArray; system.showView;}, 100,
			TXColor.white, this.getButtonColour(displayOption == "showInfo1")],
			["Spacer", 3],
			["ActionButton", "Percentile / Centroid", {displayOption = "showInfo2";
				this.buildGuiSpecArray; system.showView;}, 116,
			TXColor.white, this.getButtonColour(displayOption == "showInfo2")],
			["Spacer", 3],
			["ActionButton", "Onsets 1", {displayOption = "showOnsets1";
				this.buildGuiSpecArray; system.showView;}, 66,
			TXColor.white, this.getButtonColour(displayOption == "showOnsets1")],
			["Spacer", 3],
			["ActionButton", "Onsets 2", {displayOption = "showOnsets2";
				this.buildGuiSpecArray; system.showView;}, 66,
			TXColor.white, this.getButtonColour(displayOption == "showOnsets2")],
			["Spacer", 3],
			["ActionButton", "Onsets 3", {displayOption = "showOnsets3";
				this.buildGuiSpecArray; system.showView;}, 66,
			TXColor.white, this.getButtonColour(displayOption == "showOnsets3")],
			["DividingLine"],
			["SpacerLine", 4],
		];
		if (displayOption == "showInfo1", {
			guiSpecArray = guiSpecArray ++[
				["TextBar", "To save CPU, turn off any unused analysis processes", 350],
				["SpacerLine", 4],
				["ActionButtonBig", "Calibrate All On", {
					this.calibrateAllOn;
				}, 120, TXColour.white, TXColour.sysGuiCol1],
				["ActionButtonBig", "Calibrate All Off", {
					this.calibrateAllOff;
				}, 120, TXColour.white, TXColour.sysGuiCol2],
				["DividingLine"],
				["SpacerLine", 4],
				["EZslider", "Gate threshold", ControlSpec(0.01, 1), "gateThreshold"],
				["DividingLine"],
				["SpacerLine", 4],
				["TextBar", "Amplitude", 350, nil, TXColor.white, TXColour.sysGuiCol1],
				["NextLine"],
				["EZNumber", "Amp min", ControlSpec(0, 1), "ampMin", nil, 80, 66],
				["EZNumber", "Amp max", ControlSpec(0, 1), "ampMax", nil, 80, 66],
				["ActionButton", "Restore Defaults", {
					this.setSynthValue("ampMin", 0);
					this.setSynthValue("ampMax", 1);
					system.flagGuiUpd;
				}, 120, TXColour.white, TXColour.sysDeleteCol],

				["SpacerLine", 4],
				["TextBarLeft", " Calibration:", 90],
				["NextLine"],
				["SynthOptionCheckBox", "Detect min & max", arrOptionData, 12, 150],
				["ActionButton", "Copy detected values to min/max",
					{this.copyMinMaxAmp}, 200, TXColor.white, TXColor.sysGuiCol2],
				["NextLine"],
				["SynthOptionCheckBox", "Automatically copy detected values to min/max", arrOptionData, 13, nil,
					{this.copyMinMaxAmp}],
				["NextLine"],
				["TXStaticText", "Detected min", {holdAmpMin.asString},
					{arg view; holdAmpMinView = view.textView; }, 150],
				["TXStaticText", "Detected max", {holdAmpMax.asString},
					{arg view; holdAmpMaxView = view.textView; this.resetMinMaxAmpDisplay;}, 150],
				["ActionButton", "Reset",
					{holdAmpMin = 1; holdAmpMax = 0; this.resetMinMaxAmpDisplay;},
					120, TXColor.white, TXColor.sysDeleteCol],

				["DividingLine"],
				["SpacerLine", 4],

				["SynthOptionCheckBox", "Spectral Flatness", arrOptionData, 0],
				["NextLine"],
				["EZNumber", "Flatness min", ControlSpec(0, 1), "flatnessMin", nil, 80, 66],
				["EZNumber", "Flatness max", ControlSpec(0, 1), "flatnessMax", nil, 80, 66],
				["ActionButton", "Restore Defaults", {
					this.setSynthValue("flatnessMin", 0);
					this.setSynthValue("flatnessMax", 1);
					system.flagGuiUpd;
				}, 120, TXColour.white, TXColour.sysDeleteCol],

				["SpacerLine", 4],
				["TextBarLeft", " Calibration:", 90],
				["NextLine"],
				["SynthOptionCheckBox", "Detect min & max", arrOptionData, 10, 150],
				["ActionButton", "Copy detected values to min/max",
					{this.copyMinMaxFlatness}, 200, TXColor.white, TXColor.sysGuiCol2],
				["NextLine"],
				["SynthOptionCheckBox", "Automatically copy detected values to min/max", arrOptionData, 11, nil,
					{this.copyMinMaxFlatness}],
				["NextLine"],
				["TXStaticText", "Detected min", {holdFlatnessMin.asString},
					{arg view; holdFlatnessMinView = view.textView;}, 150],
				["TXStaticText", "Detected max", {holdFlatnessMax.asString},
					{arg view; holdFlatnessMaxView = view.textView; this.resetMinMaxFlatnessDisplay;}, 150],
				["ActionButton", "Reset",
					{holdFlatnessMin = 1; holdFlatnessMax = 0; this.resetMinMaxFlatnessDisplay;},
					120, TXColor.white, TXColor.sysDeleteCol],
			];
		});
		if (displayOption == "showInfo2", {
			guiSpecArray = guiSpecArray ++[
				["TextBar", "To save CPU, turn off any unused analysis processes", 350],
				["SpacerLine", 4],
				["ActionButtonBig", "Calibrate All On", {
					this.calibrateAllOn;
				}, 120, TXColour.white, TXColour.sysGuiCol1],
				["ActionButtonBig", "Calibrate All Off", {
					this.calibrateAllOff;
				}, 120, TXColour.white, TXColour.sysGuiCol2],
				["DividingLine"],
				["SpacerLine", 4],

				["SynthOptionCheckBox", "Spectral Percentile", arrOptionData, 1],
				["NextLine"],
				["SynthOptionPopup", "Percentile", arrOptionData, 3, 290],
				["NextLine"],
				["EZNumber", "Min freq", classData.holdControlSpec, "percentileMin", nil, 80, 66],
				["EZNumber", "Max freq", classData.holdControlSpec, "percentileMax", nil, 80, 66],
				["ActionButton", "Restore Defaults", {
					this.setSynthValue("percentileMin", 60);
					this.setSynthValue("percentileMax", 22000);
					this.setSynthOption(4, 0.95);
					system.flagGuiUpd;
				}, 120, TXColour.white, TXColour.sysDeleteCol],

				["SpacerLine", 4],
				["TextBarLeft", " Calibration:", 90],
				["NextLine"],
				["SynthOptionCheckBox", "Detect min & max", arrOptionData, 6, 150],
				["ActionButton", "Copy detected values to min/max",
					{this.copyMinMaxPercentile}, 200, TXColor.white, TXColor.sysGuiCol2],
				["NextLine"],
				["SynthOptionCheckBox", "Automatically copy detected values to min/max", arrOptionData, 8, nil,
					{this.copyMinMaxPercentile}],
				["NextLine"],
				["TXStaticText", "Detected min", {holdPercentileMin.asString},
					{arg view; holdPercentileMinView = view.textView;}, 150],
				["TXStaticText", "Detected max", {holdPercentileMax.asString},
					{arg view; holdPercentileMaxView = view.textView; this.resetMinMaxPercentileDisplay;}, 150],
				["ActionButton", "Reset",
					{holdPercentileMin = 999999; holdPercentileMax = 0; this.resetMinMaxPercentileDisplay;},
					120, TXColor.white, TXColor.sysDeleteCol],

				["DividingLine"],
				["SpacerLine", 4],

				["SynthOptionCheckBox", "Spectral Centroid", arrOptionData, 2],
				["NextLine"],
				["EZNumber", "Centroid min", classData.holdControlSpec, "centroidMin", nil, 80, 66],
				["EZNumber", "Centroid max", classData.holdControlSpec, "centroidMax", nil, 80, 66],
				["ActionButton", "Restore Defaults", {
					this.setSynthValue("centroidMin", 60);
					this.setSynthValue("centroidMax", 22000);
					system.flagGuiUpd;
				}, 120, TXColour.white, TXColour.sysDeleteCol],

				["SpacerLine", 4],
				["TextBarLeft", " Calibration:", 90],
				["NextLine"],
				["SynthOptionCheckBox", "Detect min & max", arrOptionData, 7, 150],
				["ActionButton", "Copy detected values to min/max",
					{this.copyMinMaxCentroid}, 200, TXColor.white, TXColor.sysGuiCol2],
				["NextLine"],
				["SynthOptionCheckBox", "Automatically copy detected values to min/max", arrOptionData, 9, nil,
					{this.copyMinMaxCentroid}],
				["NextLine"],
				["TXStaticText", "Detected min", {holdCentroidMin.asString},
					{arg view; holdCentroidMinView = view.textView;}, 150],
				["TXStaticText", "Detected max", {holdCentroidMax.asString},
					{arg view; holdCentroidMaxView = view.textView; this.resetMinMaxCentroidDisplay;}, 150],
				["ActionButton", "Reset",
					{holdCentroidMin = 999999; holdCentroidMax = 0; this.resetMinMaxCentroidDisplay;},
					120, TXColor.white, TXColor.sysDeleteCol],
			];
		});
		if (displayOption == "showOnsets1", {
			guiSpecArray = guiSpecArray ++[
				["SynthOptionCheckBox", "Onset Detection", arrOptionData, 5],
				["NextLine"],
				["EZslider", "Threshold", ControlSpec(0, 1), "threshold"],
				["NextLine"],
				["SynthOptionPopup", "Detect type", arrOptionData, 4, 250],
				["ActionButton", "Restore Defaults", {
					this.setSynthValue("threshold", 0.5);
					this.setSynthOption(4, \rcomplex );
					system.flagGuiUpd;
				}, 120, TXColour.white, TXColour.sysDeleteCol],
				["DividingLine"],
				["SpacerLine", 4],
				["TXActionView", arrActions, 0],
				["DividingLine"],
				["SpacerLine", 4],
				["TXActionView", arrActions, 1],
			];
		});
		if (displayOption == "showOnsets2", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 4],
				["TXActionView", arrActions, 2],
				["DividingLine"],
				["SpacerLine", 4],
				["TXActionView", arrActions, 3],
			];
		});
		if (displayOption == "showOnsets3", {
			guiSpecArray = guiSpecArray ++[
				["SpacerLine", 4],
				["TXActionView", arrActions, 4],
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

	extraSaveData {
		^[arrActions];

	}
	loadExtraData {arg argData;  // override default method
		var holdData;
		holdData = argData ? [[99,0,0,0,0,0,0, nil].dup(5)];
		arrActions = holdData.at(0);
		this.calibrateAllOff;
	}

	oscActivate {
		//	remove any previous OSCFuncs and add new
		this.oscDeactivate;
		holdOSCResp = OSCFunc({ arg msg, time, addr, recvPort;
			if (msg[2] == holdTrigID, {this.setMinMaxPercentile(msg[3])} );
		},'/tr', system.server.addr);
		holdOSCResp2 = OSCFunc({ arg msg, time, addr, recvPort;
			if (msg[2] == holdTrigID2, {this.setMinMaxCentroid(msg[3])} );
		},'/tr', system.server.addr);
		holdOSCResp3 = OSCFunc({ arg msg, time, addr, recvPort;
			if (msg[2] == holdTrigID3, {this.setMinMaxFlatness(msg[3])} );
		},'/tr', system.server.addr);
		holdOSCResp4 = OSCFunc({ arg msg, time, addr, recvPort;
			if (msg[2] == holdTrigID4, {this.setMinMaxAmp(msg[3])} );
		},'/tr', system.server.addr);
		holdOSCResp5 = OSCFunc({ arg msg, time, addr, recvPort;
			if (msg[2] == holdTrigID5, {this.performActions} );
		},'/tr', system.server.addr);
	}

	oscDeactivate {
		//	free responders
		if (holdOSCResp.class == OSCFunc, {holdOSCResp.free});
		if (holdOSCResp2.class == OSCFunc, {holdOSCResp2.free});
		if (holdOSCResp3.class == OSCFunc, {holdOSCResp3.free});
		if (holdOSCResp4.class == OSCFunc, {holdOSCResp4.free});
		if (holdOSCResp5.class == OSCFunc, {holdOSCResp5.free});
	}

	setMinMaxAmp {arg argAmp;
		var holdAmp = argAmp;
		if (holdAmp.notNil, {
			if ((holdAmp != 0) and: (holdAmp < holdAmpMin), {
				holdAmpMin = holdAmp.round(0.001);
				if ((system.isModDisplay(this) == true) and: (displayOption == "showInfo1"), {
					{holdAmpMinView.string = holdAmpMin.asString;}.defer;
				});
			});
			if (holdAmp > holdAmpMax, {
				holdAmpMax = holdAmp.round(0.001);
				if ((system.isModDisplay(this) == true) and: (displayOption == "showInfo1"), {
					{holdAmpMaxView.string = holdAmpMax.asString;}.defer;
				});
			});
			// run update function
			this.getSynthOption(13).value;
		});
	}

	setMinMaxFlatness {arg argFlatness;
		var holdFlatness = argFlatness;
		if (holdFlatness.notNil, {
			if ((holdFlatness != 0) and: (holdFlatness < holdFlatnessMin), {
				holdFlatnessMin = holdFlatness.round(0.001);
				if ((system.isModDisplay(this) == true) and: (displayOption == "showInfo1"), {
					{holdFlatnessMinView.string = holdFlatnessMin.asString;}.defer;
				});
			});
			if (holdFlatness > holdFlatnessMax, {
				holdFlatnessMax = holdFlatness.round(0.001);
				if ((system.isModDisplay(this) == true) and: (displayOption == "showInfo1"), {
					{holdFlatnessMaxView.string = holdFlatnessMax.asString;}.defer;
				});
			});
			// run update function
			this.getSynthOption(11).value;
		});
	}

	setMinMaxPercentile {arg argPercentile;
		var holdPercentile = argPercentile;
		if (holdPercentile.notNil, {
			if ((holdPercentile != 60) and: (holdPercentile < holdPercentileMin), {
				holdPercentileMin = holdPercentile.round(1);
				if ((system.isModDisplay(this) == true) and: (displayOption == "showInfo2"), {
					{holdPercentileMinView.string = holdPercentileMin.asString;}.defer;
				});
			});
			if (holdPercentile > holdPercentileMax, {
				holdPercentileMax = holdPercentile.round(1);
				if ((system.isModDisplay(this) == true) and: (displayOption == "showInfo2"), {
					{holdPercentileMaxView.string = holdPercentileMax.asString;}.defer;
				});
			});
			// run update function
			this.getSynthOption(8).value;
		});
	}

	setMinMaxCentroid {arg argCentroid;
		var holdCentroid = argCentroid;
		if (holdCentroid.notNil, {
			if ((holdCentroid != 60) and: (holdCentroid < holdCentroidMin), {
				holdCentroidMin = holdCentroid.round(1);
				if ((system.isModDisplay(this) == true) and: (displayOption == "showInfo2"), {
					{holdCentroidMinView.string = holdCentroidMin.asString;}.defer;
				});
			});
			if (holdCentroid > holdCentroidMax, {
				holdCentroidMax = holdCentroid.round(1);
				if ((system.isModDisplay(this) == true) and: (displayOption == "showInfo2"), {
					{holdCentroidMaxView.string = holdCentroidMax.asString;}.defer;
				});
			});
			// run update function
			this.getSynthOption(9).value;
		});
	}

	resetMinMaxAmpDisplay {arg argAmp;
		{	if (holdAmpMin == 1, {holdAmpMinView.string = " ";});
			if (holdAmpMax == 0, {holdAmpMaxView.string = " ";});
		}.defer;
	}

	resetMinMaxFlatnessDisplay {arg argFlatness;
		{	if (holdFlatnessMin == 1, {holdFlatnessMinView.string = " ";});
			if (holdFlatnessMax == 0, {holdFlatnessMaxView.string = " ";});
		}.defer;
	}

	resetMinMaxPercentileDisplay {arg argPercentile;
		{	if (holdPercentileMin == 999999, {holdPercentileMinView.string = " ";});
			if (holdPercentileMax == 0, {holdPercentileMaxView.string = " ";});
		}.defer;
	}

	resetMinMaxCentroidDisplay {arg argCentroid;
		{	if (holdCentroidMin == 999999, {holdCentroidMinView.string = " ";});
			if (holdCentroidMax == 0, {holdCentroidMaxView.string = " ";});
		}.defer;
	}

	copyMinMaxAmp {
		if (holdAmpMin < 1, {
			this.setSynthValue("ampMin", ControlSpec(0, 1).constrain(holdAmpMin));
		});
		if (holdAmpMax > 0, {
			this.setSynthValue("ampMax", ControlSpec(0, 1).constrain(holdAmpMax));
		});
		system.flagGuiUpd;
	}

	copyMinMaxFlatness {
		if (holdFlatnessMin < 1, {
			this.setSynthValue("flatnessMin", ControlSpec(0, 1).constrain(holdFlatnessMin));
		});
		if (holdFlatnessMax > 0, {
			this.setSynthValue("flatnessMax", ControlSpec(0, 1).constrain(holdFlatnessMax));
		});
		system.flagGuiUpd;
	}

	copyMinMaxPercentile {
		if (holdPercentileMin < 999999, {
			this.setSynthValue("percentileMin", classData.holdControlSpec.constrain(holdPercentileMin));
		});
		if (holdPercentileMax > 0, {
			this.setSynthValue("percentileMax", classData.holdControlSpec.constrain(holdPercentileMax));
		});
		system.flagGuiUpd;
	}

	copyMinMaxCentroid {
		if (holdCentroidMin < 999999, {
			this.setSynthValue("centroidMin", classData.holdControlSpec.constrain(holdCentroidMin));
		});
		if (holdCentroidMax > 0, {
			this.setSynthValue("centroidMax", classData.holdControlSpec.constrain(holdCentroidMax));
		});
		system.flagGuiUpd;
	}

	performActions {
		arrActions.do({ arg item, i;
			var holdModuleID, holdModule, holdActionInd, holdArrActionItems, holdActionText,
			holdAction, holdVal1, holdVal2, holdVal3, holdVal4, actionArg1, actionArg2, actionArg3, actionArg4,
			holdIndex, holdItems;
			holdModuleID = item.at(0);
			holdActionInd = item.at(1);
			holdVal1 = item.at(2);
			holdVal2 = item.at(3);
			holdVal3 = item.at(4);
			holdVal4 = item.at(5);
			holdActionText = item.at(7);
			holdModule = system.getModuleFromID(holdModuleID);
			if (holdModule != 0, {
				holdArrActionItems = holdModule.arrActionSpecs.collect({arg item, i; item.actionName;});
				// if text found, match action string with text, else use numerical value
				if (holdActionText.notNil, {
					holdActionInd = holdArrActionItems.indexOfEqual(holdActionText) ? holdActionInd;
					holdAction = holdModule.arrActionSpecs.at(holdActionInd);
				},{
					// if text not found, use number but only select older actions with legacyType == 1
					holdAction = holdModule.arrActionSpecs
					.select({arg item, i; item.legacyType == 1}).at(holdActionInd);
				});

				actionArg1 = holdVal1;
				actionArg2 = holdVal2;
				actionArg3 = holdVal3;
				actionArg4 = holdVal4;

				// if action type is commandAction then value it with arguments
				if (holdAction.actionType == \commandAction, {
					holdAction.actionFunction.value(actionArg1, actionArg2, actionArg3, actionArg4);
				});
				// if action type is valueAction then value it with arguments
				if (holdAction.actionType == \valueAction, {
					holdAction.setValueFunction.value(actionArg1, actionArg2, actionArg3, actionArg4);
				});
			});
		});
		//	gui update
		//	system.flagGuiUpd;
	}	// end of performActions

	calibrateAllOn {
		Routine.run {
			arrOptions.put(6, 1);
			arrOptions.put(7, 1);
			arrOptions.put(8, 1);
			arrOptions.put(9, 1);
			arrOptions.put(10, 1);
			arrOptions.put(11, 1);
			arrOptions.put(12, 1);
			arrOptions.put(13, 1);
			this.rebuildSynth;
			// pause
			system.server.sync;
			0.5.wait;
			holdAmpMin = 1; holdAmpMax = 0;
			holdFlatnessMin = 1; holdFlatnessMax = 0;
			holdPercentileMin = 999999; holdPercentileMax = 0;
			holdCentroidMin = 999999; holdCentroidMax = 0;
			system.flagGuiUpd;
		};

	}

	calibrateAllOff {
		Routine.run {
			arrOptions.put(6, 0);
			arrOptions.put(7, 0);
			arrOptions.put(8, 0);
			arrOptions.put(9, 0);
			arrOptions.put(10, 0);
			arrOptions.put(11, 0);
			arrOptions.put(12, 0);
			arrOptions.put(13, 0);
			this.rebuildSynth;
			// pause
			system.server.sync;
			0.5.wait;
			holdAmpMin = 1; holdAmpMax = 0;
			holdFlatnessMin = 1; holdFlatnessMax = 0;
			holdPercentileMin = 999999; holdPercentileMax = 0;
			holdCentroidMin = 999999; holdCentroidMax = 0;
			system.flagGuiUpd;
		};
	}

}

