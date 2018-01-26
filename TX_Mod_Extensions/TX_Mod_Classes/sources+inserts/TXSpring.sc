// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSpring : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Spring";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Trigger", 1, "extTrigger", nil],
			["Trig force", 1, "modTrigForce", 0],
			["Springiness", 1, "modSpringConst", 0],
			["Damping", 1, "modDamping", 0],
			["Output scale", 1, "modOutputScale", 0],
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
		autoModOptions = false;
		arrSynthArgSpecs = [
			["out", 0, 0],
			["t_userTrig", 0, 0],
			["extTrigger", 0, 0],
			["trigForce", 0.25, 0],
			["springConst", ControlSpec(0.1, 20).unmap(1), defLagTime],
			["springConstMin", 0.1, defLagTime],
			["springConstMax", 20, defLagTime],
			["damping", 0.5, defLagTime],
			["dampingMin", 0, defLagTime],
			["dampingMax", 0.2, defLagTime],
			["outputScale", 0.25, defLagTime],
			["outputScaleMin", 0, defLagTime],
			["outputScaleMax", 2, defLagTime],
			["modTrigForce", 0, 0],
			["modSpringConst", 0, 0],
			["modDamping", 0, 0],
			["modOutputScale", 0, 0],
		];
		arrOptions = [0];
		arrOptionData = [
			TXLFO.arrLFOOutputRanges,
		];
		synthDefFunc = { arg out, t_userTrig = 0, extTrigger = 0,
			trigForce, springConst, springConstMin, springConstMax,
			damping, dampingMin, dampingMax, outputScale, outputScaleMin, outputScaleMax,
			modTrigForce = 0, modSpringConst = 0, modDamping = 0, modOutputScale = 0;

			var trigForceSum, springConstSum, dampingSum, outputScaleSum, outSpring;
			var rangeFunction, outSignal;

			trigForceSum = (trigForce + modTrigForce).max(0).min(1);
			springConstSum = (springConst + modSpringConst).max(0).min(1)
				.linlin(0, 1, springConstMin, springConstMax);
			dampingSum = (damping + modDamping).max(0).min(1)
				.linlin(0, 1, dampingMin, dampingMax);
			outputScaleSum = (outputScale + modOutputScale).max(0).min(1)
			.linlin(0, 1, outputScaleMin, outputScaleMax);
			outSpring = Spring.kr(Trig.kr((t_userTrig + extTrigger) * trigForceSum, 0.05),
				springConstSum, 0.1 * dampingSum);
			rangeFunction = this.getSynthOption(0);
			outSignal = rangeFunction.value((outSpring * 10 * outputScaleSum).clip(-1, 1));
			Out.kr(out, TXClean.kr(outSignal));
		};
		guiSpecArray = [
			["ActionButton", "Trigger", {this.moduleNode.set("t_userTrig", 1);},
				200, TXColor.white, TXColor.sysGuiCol2],
			["SpacerLine", 4],
			["EZslider", "Trig force", ControlSpec(0, 1), "trigForce"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "Springiness", ControlSpec(0.01, 100),
				"springConst", "springConstMin", "springConstMax"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Damping", ControlSpec(0, 1),
				"damping", "dampingMin", "dampingMax"],
			["NextLine"],
			["DividingLine"],
			["SpacerLine", 10],
			["TXMinMaxSliderSplit", "Output scale", ControlSpec(0, 10),
				"outputScale", "outputScaleMin", "outputScaleMax"],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "Output range", arrOptionData, 0],
		];
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Trigger", {this.moduleNode.set("t_userTrig", 1);}],
		]
		++ guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

}

