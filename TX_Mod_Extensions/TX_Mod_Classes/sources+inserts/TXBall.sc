// Copyright (C) 2016  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXBall : TXModuleBase {

	classvar <>classData;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Ball";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [
			["Trigger", 1, "extTrigger", nil],
			["Trig force", 1, "modTrigForce", 0],
			["Gravity", 1, "modGravity", 0],
			["Damping", 1, "modDamping", 0],
			["Friction", 1, "modFriction", 0],
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
			["gravity", ControlSpec(0.1, 20).unmap(1), defLagTime],
			["gravityMin", 0.1, defLagTime],
			["gravityMax", 20, defLagTime],
			["damping", 0.5, defLagTime],
			["dampingMin", 0, defLagTime],
			["dampingMax", 1, defLagTime],
			["friction", 0.1, defLagTime],
			["outputScale", 0.5, defLagTime],
			["outputScaleMin", 0, defLagTime],
			["outputScaleMax", 2, defLagTime],
			["modTrigForce", 0, 0],
			["modGravity", 0, 0],
			["modDamping", 0, 0],
			["modFriction", 0, 0],
			["modOutputScale", 0, 0],
		];
		synthDefFunc = { arg out, t_userTrig = 0, extTrigger = 0,
			trigForce, gravity, gravityMin, gravityMax,
			damping, dampingMin, dampingMax, friction, outputScale, outputScaleMin, outputScaleMax,
			modTrigForce = 0, modGravity = 0, modDamping = 0, modFriction = 0, modOutputScale = 0;

			var trigForceSum, gravitySum, dampingSum, frictionSum, outputScaleSum, outBall;

			trigForceSum = (trigForce + modTrigForce).max(0).min(1);
			gravitySum = (gravity + modGravity).max(0).min(1)
				.linlin(0, 1, gravityMin, gravityMax);
			dampingSum = (damping + modDamping).max(0).min(1)
				.linlin(0, 1, dampingMin, dampingMax);
			frictionSum = (friction + modFriction).max(0).min(1);
			outputScaleSum = 0.1 * (outputScale + modOutputScale).max(0).min(1)
				.linlin(0, 1, outputScaleMin, outputScaleMax);
			outBall = Ball.kr(Trig.kr((t_userTrig + extTrigger) * trigForceSum, 0.05),
				gravitySum, 0.1 * dampingSum, 0.01 * frictionSum);
			Out.kr(out, TXClean.kr(outBall * outputScaleSum));
		};
		guiSpecArray = [
			["ActionButton", "Trigger", {this.moduleNode.set("t_userTrig", 1);},
				200, TXColor.white, TXColor.sysGuiCol2],
			["SpacerLine", 4],
			["EZslider", "Trig force", ControlSpec(0, 1), "trigForce"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Gravity", ControlSpec(0.01, 100), "gravity", "gravityMin", "gravityMax"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Damping", ControlSpec(0, 1), "damping", "dampingMin", "dampingMax"],
			["SpacerLine", 4],
			["EZslider", "Friction", ControlSpec(0, 1), "friction"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Output scale", ControlSpec(0, 10), "outputScale", "outputScaleMin", "outputScaleMax"],
			["SpacerLine", 4],
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

