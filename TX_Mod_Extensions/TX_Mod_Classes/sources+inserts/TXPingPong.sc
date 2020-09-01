// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXPingPong : TXModuleBase {		// PingPong Delay mono in, stereo out

	classvar <>classData;

	var	holdTapTime, newTapTime;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Ping Pong";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Delay Time", 1, "modDelay", 0],
			["Feedback", 1, "modFeedback", 0],
			["Freeze", 1, "modFreeze", 0],
			["Smooth time", 1, "modSmoothTime", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 2;
		classData.arrOutBusSpecs = [
			["Out L + R", [0,1]],
			["Out L only", [0]],
			["Out R only", [1]]
		];
		classData.maxDelaytime = 16;	//	delay time up to 16 secs.
		classData.arrBufferSpecs = [ ["bufnumDelay", defSampleRate * classData.maxDelaytime, 2] ];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var holdControlSpec, holdControlSpec2;
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		arrOptions = [0];
		arrOptionData = [
			[
				["Positive", 1],
				["Negative", -1],
			],
		];
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["bufnumDelay", 0, \ir],
			["initialPan", 0, defLagTime],
			["delay", 0.5, defLagTime],
			["delayMin", 10, defLagTime],
			["delayMax", 1000 * classData.maxDelaytime, defLagTime],
			["feedback", 0.5, defLagTime],
			["feedbackMin", 0.001, defLagTime],
			["feedbackMax", 1.0, defLagTime],
			["freeze", 0, 0],
			["smoothTime", 0.2, 0],
			["wetDryMix", 1.0, defLagTime],
			["modDelay", 0, defLagTime],
			["modFeedback", 0, defLagTime],
			["modFreeze", 0, 0],
			["modSmoothTime", 0, 0],
			["modWetDryMix", 0, defLagTime],
			// N.B. arg below not used in synthdef, just kept here for convenience
			["autoTapTempo", 0, \ir],
		];
		synthDefFunc = {
			arg in, out, bufnumDelay, initialPan, delay=0.1, delayMin, delayMax,
			feedback, feedbackMin, feedbackMax,
			freeze, smoothTime, wetDryMix,
			modDelay=0, modFeedback=0, modFreeze=0,
			modSmoothTime=0, modWetDryMix=0;
			var input, inputPanned, outSound, delaytime, feedbackVal, feedbackType,
			mixCombined, freezeCombined, invLagFreeze, smoothTimeCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			freezeCombined = (freeze + modFreeze).max(0).min(1).round(1);
			invLagFreeze = 1 - Lag.kr(freezeCombined, 0.1);
			smoothTimeCombined = (smoothTime + modSmoothTime).max(0).min(1);
			input = TXClean.ar(startEnv * InFeedback.ar(in,1));
			inputPanned = Pan2.ar(input, (initialPan * 2)-1);
			delaytime = Lag2.kr(
				(( (delayMax/delayMin) ** ((delay + modDelay).max(0.0001).min(1)) ) * delayMin / 1000),
				smoothTimeCombined
			);
			feedbackType = this.getSynthOption(0);
			feedbackVal = feedbackMin + ( (feedbackMax-feedbackMin) * (feedback + modFeedback
				+ (freezeCombined * 2)).max(0).min(1) );
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outSound = PingPong.ar(bufnumDelay, inputPanned * invLagFreeze, delaytime, feedbackType * feedbackVal);
			outSound = LeakDC.ar(outSound);
			Out.ar(out, (TXClean.ar(startEnv * outSound) * mixCombined) + (input * (1-mixCombined)));
		};
		holdControlSpec = ControlSpec.new(10, 1000 * classData.maxDelaytime, \exp );
		holdControlSpec2 = ControlSpec.new(0.001, 1.0, \lin );
		guiSpecArray = [
			["SpacerLine", 4],
			["EZslider", "Initial Pan", ControlSpec(0, 1), "initialPan"],
			["SpacerLine", 4],
			["TapTempoButton", {arg argTempo; this.useTapTempo(argTempo);}],
			["Spacer", 10],
			["TXCheckBox", "Auto copy tap tempo to delay bpm ", "autoTapTempo", nil, 230],
			["SpacerLine", 4],
			["TextBarLeft", "Delay time shown in ms and bpm", 200],
			["Spacer", 3],
			["ActionButton", "time x 2", {this.delayTimeMultiply(2);}, 60],
			["ActionButton", "time x 3", {this.delayTimeMultiply(3);}, 60],
			["ActionButton", "time / 2", {this.delayTimeMultiply(0.5);}, 60],
			["ActionButton", "time / 3", {this.delayTimeMultiply(1/3);}, 60],
			["NextLine"],
			["TXTimeBpmMinMaxSldr", "Delay time", holdControlSpec, "delay", "delayMin", "delayMax"],
			["SpacerLine", 4],
			["EZslider", "Smooth time", ControlSpec(0, 1), "smoothTime"],
			["SpacerLine", 4],
			["TXMinMaxSliderSplit", "Feedback", holdControlSpec2, "feedback", "feedbackMin", "feedbackMax"],
			["SpacerLine", 4],
			["SynthOptionPopupPlusMinus", "FB type", arrOptionData, 0, 300],
			["SpacerLine", 4],
			["WetDryMixSlider"],
			["SpacerLine", 4],
			["TXCheckBox", "Freeze", "freeze"],
			["Spacer", 20],
			["ActionButtonDark", "Clear", {this.clearBuffers}],
		];
		arrActionSpecs = this.buildActionSpecs(
			[["commandAction", "Tap Tempo", {this.actionTapTempo;}]]
			++ guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make buffers, load the synthdef and create the synth
		this.makeBuffersAndSynth(classData.arrBufferSpecs);
	}

	delayTimeMultiply { arg argMultiplyValue;
		var currentTime, minTime, maxTime, holdControlSpec, newTime;
		minTime = this.getSynthArgSpec("delayMin");
		maxTime = this.getSynthArgSpec("delayMax");
		holdControlSpec = ControlSpec.new(minTime, maxTime, \exp);
		currentTime = holdControlSpec.map(this.getSynthArgSpec("delay"));
		newTime = currentTime * argMultiplyValue;
		if (argMultiplyValue < 1, {
			if ( newTime >= minTime, {
				this.setSynthValue("delay", holdControlSpec.unmap(newTime));
			});
		},{
			if ( newTime <= maxTime, {
				this.setSynthValue("delay", holdControlSpec.unmap(newTime));
			});
		});
		system.flagGuiIfModDisplay(this);
	}

	useTapTempo {arg argTempo;
		var holdDelay, autoBPM, minDelay, maxDelay;
		holdDelay = 60000/argTempo;
		autoBPM = this.getSynthArgSpec("autoTapTempo");
		minDelay = this.getSynthArgSpec("delayMin");
		maxDelay = this.getSynthArgSpec("delayMax");
		if (autoBPM == 1,{
			if ((holdDelay >= minDelay) and: (holdDelay <= maxDelay),{
				this.setSynthArgSpec("delay", ControlSpec(minDelay, maxDelay, \exp).unmap(holdDelay));
				system.flagGuiIfModDisplay(this);
			});
		});
	}

	actionTapTempo {	// tap tempo function used by module action
		var holdBPM;
		if (newTapTime.isNil, {
			newTapTime = Main.elapsedTime
		}, {
			holdTapTime = Main.elapsedTime;
			holdBPM = 60 / (holdTapTime - newTapTime);
			newTapTime = holdTapTime;
			this.useTapTempo(holdBPM);
		});
	}

}

