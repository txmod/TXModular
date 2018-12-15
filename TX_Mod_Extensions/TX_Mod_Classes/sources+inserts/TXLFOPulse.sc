// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXLFOPulse : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "LFO Pulse";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Freq", 1, "modFreq", 0],
			["Pulse width", 1, "modPulseWidth", 0],
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.freqSpec = ControlSpec(0.001, 100, \exp, 0, 1, units: " Hz");
		classData.pulseWidthSpec = ControlSpec(0.01, 0.99);
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		arrSynthArgSpecs = [
			["out", 0, 0],
			["freq", 0.5, defLagTime],
			["freqMin", 0.01, defLagTime],
			["freqMax", 100, defLagTime],
			["pulseWidth", 0.5, defLagTime],
			["pulseWidthMin", 0.01, defLagTime],
			["pulseWidthMax", 0.99, defLagTime],
			["modFreq", 0, defLagTime],
			["modPulseWidth", 0, defLagTime],
		];
		arrOptions = [0];
		arrOptionData = [
			TXLFO.arrLFOOutputRanges,
		];
		synthDefFunc = { arg out, freq, freqMin, freqMax, pulseWidth, pulseWidthMin, pulseWidthMax, modFreq = 0, modPulseWidth = 0;
			var sFreq, sPulseWidth, lfoSignal, rangeFunction, outSignal;
			sFreq = ( (freqMax/freqMin) ** ((freq + modFreq).max(0.001).min(1)) ) * freqMin;
			sPulseWidth = (pulseWidth + modPulseWidth).max(0).min(1).linlin(0, 1, pulseWidthMin, pulseWidthMax);
			rangeFunction =  this.getSynthOption(0);
			lfoSignal = LFPulse.kr(sFreq, 0, sPulseWidth);
			outSignal = rangeFunction.value(lfoSignal);
			Out.kr(out, TXClean.kr(outSignal));
		};
		guiSpecArray = [
			["NextLine"],
			["Spacer", 82],
			["ActionButton", "Freq x 2", {this.freqMultiply(2);}, 60],
			["ActionButton", "Freq x 3", {this.freqMultiply(3);}, 60],
			["ActionButton", "Freq / 2", {this.freqMultiply(0.5);}, 60],
			["ActionButton", "Freq / 3", {this.freqMultiply(1/3);}, 60],
			["NextLine"],
			["TXFreqBpmMinMaxSldr", "Frequency", classData.freqSpec, "freq", "freqMin", "freqMax",
				nil, TXLFO.arrLFOFreqRanges],
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "Pulse width", classData.pulseWidthSpec, "pulseWidth", "pulseWidthMin", "pulseWidthMax"],
			["SpacerLine", 10],
			["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 0],
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

