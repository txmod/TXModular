// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXClockPulse : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Clock Pulse";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Play", 1, "modPlay", 0],
			["Restart Trig", 1, "restartTrig", nil],
			["BPM", 1, "modClockBPM", 0],
			["Multiply BPM", 1, "modMultiplyBPM", 0],
			["Swing", 1, "modSwing", 0],
			["Jitter", 1, "modJitter", 0],
			["Probability", 1, "modProbability", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["out", 0, 0],
			["play", 1, 0],
			["restartTrig", 0, 0],
			["t_userRestartTrig", 0, 0],
			["t_userClockTrig", 0, 0],
			["clockBPM", 0.3333333, 0], 		// default is set for 120 bpm
			["clockBPMMin", 30, 0],
			["clockBPMMax", 300, 0],
			["multiplyBPM", 0],
			["multiplyBPMMin", 1],
			["multiplyBPMMax", 16],
			["swing", 0.5, 0],
			["swingMin", -1],
			["swingMax", 1],
			["jitter", 0, 0],
			["jitterMin", 0],
			["jitterMax", 1],
			["probability", 1, 0],
			["modClockBPM", 0, 0],
			["modMultiplyBPM", 0, 0],
			["modSwing", 0, 0],
			["modJitter", 0, 0],
			["modProbability", 0, 0],
			["modPlay", 0, 0],
		];
		arrOptions = [0];
		arrOptionData = [
			[	["Positive only: 0 to 1", {arg input; input}],
				["Positive & Negative: -1 to 1", {arg input; (input * 2) - 1}],
				["Positive & Negative: -0.5 to 0.5", {arg input; input - 0.5}],
			],
		];

		synthDefFunc = { arg out, play, restartTrig = 0, t_userRestartTrig = 0, t_userClockTrig = 0, clockBPM, clockBPMMin, clockBPMMax,
			multiplyBPM, multiplyBPMMin, multiplyBPMMax, swing, swingMin, swingMax,
			jitter, jitterMin, jitterMax, probability,
			modClockBPM = 0, modMultiplyBPM = 0, modSwing = 0, modJitter = 0, modProbability = 0, modPlay = 0;

			var holdRate, outClock, rangeFunction, outSignal, holdPhasor, staticFreq, outClockFreq, randLFO;
			var swingSum, swingPhase, jitterSum, probabilitySum, probabilityOut, clockBPMSum, multiplyBPMSum, playSum, playTrig;

			clockBPMSum = (clockBPM + modClockBPM).max(0).min(1).linlin(0, 1, clockBPMMin, clockBPMMax);
			multiplyBPMSum = (multiplyBPM + modMultiplyBPM).max(0).min(1)
				.linlin(0, 1, multiplyBPMMin, multiplyBPMMax).round;
			swingSum = (swing + modSwing).max(0).min(1).linlin(0, 1, swingMin, swingMax);
			swingPhase = 0.25 * swingSum;
			jitterSum = (jitter + modJitter).max(0).min(1).linlin(0, 1, jitterMin, jitterMax);
			probabilitySum = (probability + modProbability).max(0).min(1);
			playSum = (play + modPlay).max(0).min(1).round;
			playTrig = Trig1.kr(playSum, ControlDur.ir);
			rangeFunction = this.getSynthOption(0);

			staticFreq = clockBPMSum *  multiplyBPMSum / 60;
			randLFO = LFNoise2.kr(staticFreq * (6 + (12 * jitterSum)));
			outClockFreq = staticFreq * (jitterSum * randLFO).linexp(-1, 1, 0.1, 10);
			holdRate = 0.5 * outClockFreq / ControlRate.ir; // halve rate because 2 trigs per phase

			// IMPULSE CLOCK THAT CAN GO FAST AND BE RESET FAST

			// OK BUT MOVING SWING SLIDER NEGATIVELY CAUSES RETRIGGERS
			// holdPhasor = Phasor.kr((restartTrig + t_userRestartTrig + playTrig), holdRate);
			// outClock = Trig1.kr(Slope.kr(holdPhasor).neg, ControlDur.ir)
			// + Trig1.kr(Slope.kr((holdPhasor + 0.5 + swingPhase) % 1.0).neg, ControlDur.ir);

			// BEST SO FAR:
			holdPhasor = Phasor.kr((restartTrig + t_userRestartTrig + playTrig), holdRate) - 0.02; // neg offset 0.02 works
			outClock = Trig1.kr((holdPhasor > 0).min(holdPhasor <= 0.5), ControlDur.ir)
			+ Trig1.kr((holdPhasor > (0.5 + swingPhase)).min(holdPhasor < 1), ControlDur.ir);

			outClock = outClock.min(playSum);
			probabilityOut = (Demand.kr(outClock, 0, Dwhite(-0.499, 0.499, inf)) + probabilitySum).round.clip;

			outSignal = rangeFunction.value(t_userClockTrig + outClock.min(probabilityOut));
			Out.kr(out, TXClean.kr(outSignal));
		};
		guiSpecArray = [
			["TXCheckBox", "Play", "play", {}, 120],
			["Spacer", 10],
			["ActionButton", "Restart Clock", {this.moduleNode.set("t_userRestartTrig", 1);},
				120, TXColor.white, TXColor.sysGuiCol2],
			["Spacer", 10],
			["ActionButton", "Trigger Pulse", {this.moduleNode.set("t_userClockTrig", 1);},
				120, TXColor.white, TXColor.sysGuiCol2],
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "BPM", ControlSpec(1, 900), "clockBPM", "clockBPMMin", "clockBPMMax"],
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "Multiply BPM", ControlSpec(1, 16, step: 1), "multiplyBPM",
				"multiplyBPMMin", "multiplyBPMMax"],
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "Swing", ControlSpec(-1, 1), "swing", "swingMin", "swingMax"],
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "Jitter", ControlSpec(0, 1), "jitter", "jitterMin", "jitterMax"],
			["SpacerLine", 10],
			["EZslider", "Probability", ControlSpec(0, 1), "probability"],
			["SpacerLine", 10],
			["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 0],
		];
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Trigger Pulse", {this.moduleNode.set("t_userClockTrig", 1);}],
			["commandAction", "Restart Clock", {this.moduleNode.set("t_userRestartTrig", 1);}],
		]
		++ guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

