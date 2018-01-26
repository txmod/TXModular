// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXLiveLooper2 : TXModuleBase {		// Live Looper

	classvar <>classData;

	var	holdTapTime, newTapTime;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Live Looper";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 1;
		classData.arrCtlSCInBusSpecs = [
			["Loop Time", 1, "modLoopTime", 0],
			["Record", 1, "modRecord", 0],
			["Input Level", 1, "modInLevel", 0],
			["Feedback", 1, "modFeedback", 0],
			["Speed Factor", 1, "modSpeedFactor", 0],
			["Dry-Wet Mix", 1, "modWetDryMix", 0]
		];
		classData.noOutChannels = 1;
		classData.arrOutBusSpecs = [
			["Out", [0]]
		];
		classData.maxLoopTime = 120;
		classData.arrBufferSpecs = [ ["bufnumLoop", defSampleRate * classData.maxLoopTime, 1],  ["bufnumDummy", 2048, 1],  ];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		var holdControlSpec, holdControlSpec2;
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["bufnumLoop", 0, \ir],
			["bufnumDummy", 0, \ir],
			["loopTime", 0.5, defLagTime],
			["loopTimeMin", 1000, defLagTime],
			["loopTimeMax", 19000, defLagTime],
			["speedFactor", 0.5, defLagTime],
			["speedFactorMin", 0.5, defLagTime],
			["speedFactorMax", 2, defLagTime],
			["inLevel", 0.5, 0.1],	// set lag time to 0.1 to stop clicks when freezing
			["feedback", 0.75, 0.1],	// set lag time to 0.1 to stop clicks when freezing
			["record", 1, 0],
			["wetDryMix", 1.0, defLagTime],
			["modLoopTime", 0, defLagTime],
			["modSpeedFactor", 0, defLagTime],
			["modInLevel", 0, defLagTime],
			["modFeedback", 0, defLagTime],
			["modRecord", 0, 0],
			["modWetDryMix", 0, defLagTime],
			// N.B. arg below not used in synthdef, just kept here for convenience
			["autoTapTempo", 0, \ir],
		];
		synthDefFunc = {
			arg in, out, bufnumLoop, bufnumDummy, loopTime=0.1, loopTimeMin, loopTimeMax,
			speedFactor, speedFactorMin, speedFactorMax, inLevel, feedback, record,
			wetDryMix, modLoopTime=0, modSpeedFactor=0, modInLevel=0, modFeedback=0, modRecord=0, modWetDryMix=0;
			var input, outSound, loopTimeCombined, speedFactorVal, inLevelCombined,
			feedbackCombined, recordCombined, mixCombined, loopOut, loopOut2, holdPhasor;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			input = TXClean.ar(startEnv * InFeedback.ar(in,1));

			loopTimeCombined = (loopTimeMin + ( (loopTimeMax-loopTimeMin)
				* (loopTime + modLoopTime).max(0).min(1) ) )/ 1000;
			//		loopTimeCombined =( (loopTimeMax/loopTimeMin) **
			//			((loopTime + modLoopTime).max(0.0001).min(1)) ) * loopTimeMin / 1000;

			speedFactorVal =( (speedFactorMax/speedFactorMin)
				** ((speedFactor + modSpeedFactor).max(0.0001).min(1)) ) * speedFactorMin;
			//		speedFactorVal = speedFactorMin + ( (speedFactorMax-speedFactorMin)
			//			* (speedFactor + modSpeedFactor).max(0).min(1) );

			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			inLevelCombined = (inLevel + modInLevel).max(0).min(1);
			feedbackCombined = (feedback + modFeedback).max(0).min(1);
			recordCombined = (record + modRecord).round(1).max(0).min(1);

			//		RecordBuf.ar(input, bufnumLoop, 0, oldNewMix, (1 - oldNewMix), 1, 1, t_reset);
			//		loopOut = PlayBuf.ar(1, bufnumLoop, speedFactorVal, t_reset, 0, 1);

			holdPhasor = Phasor.ar(0, speedFactorVal * BufRateScale.kr(bufnumLoop),
				0, (loopTimeCombined / (classData.maxLoopTime)) * BufFrames.kr(bufnumLoop));


			loopOut = BufRd.ar(1, bufnumLoop,
				holdPhasor,
				0, 4
			);

			BufWr.ar( ( recordCombined * (input * inLevelCombined) + (loopOut * feedbackCombined) ),
				((recordCombined * bufnumLoop) + ((1 - recordCombined) * bufnumDummy)),
				holdPhasor,
				0
			);
			outSound = (loopOut * mixCombined) + (input * (1-mixCombined));
			outSound = LeakDC.ar(outSound);
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		holdControlSpec = ControlSpec.new(100, 1000 * classData.maxLoopTime);
		holdControlSpec2 = ControlSpec.new(0.1, 10 , \exp );
		guiSpecArray = [
			["TXCheckBox", "Record", "record"],
			["Spacer", 10],
			["ActionButton", "Freeze Loop", {this.freezeLoop}],
			["ActionButtonDark", "Clear Loop", {this.clearLoop}],
			["SpacerLine", 2],
			["TapTempoButton", {arg argTempo; this.useTapTempo(argTempo);}],
			["Spacer", 10],
			["TXCheckBox", "Auto copy tap tempo to loop bpm ", "autoTapTempo", nil, 230],
			["SpacerLine", 2],
			["TextBarLeft", "Loop time shown in ms and bpm", 200],
			["Spacer", 3],
			["ActionButton", "time x 2", {this.delayTimeMultiply(2);}, 60],
			["ActionButton", "time x 3", {this.delayTimeMultiply(3);}, 60],
			["ActionButton", "time / 2", {this.delayTimeMultiply(0.5);}, 60],
			["ActionButton", "time / 3", {this.delayTimeMultiply(1/3);}, 60],
			["TXTimeBpmMinMaxSldr", "Loop Time", holdControlSpec, "loopTime", "loopTimeMin",
				"loopTimeMax"],
			["SpacerLine", 2],
			["EZslider", "Input Level", ControlSpec(0, 1), "inLevel"],
			["SpacerLine", 2],
			["EZslider", "Feedback", ControlSpec(0, 1), "feedback"],
			["SpacerLine", 2],
			["TXMinMaxSliderSplit", "Speed factor", holdControlSpec2, "speedFactor",
				"speedFactorMin", "speedFactorMax"],
			["SpacerLine", 2],
			["WetDryMixSlider"],
		];
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Freeze Loop", {this.freezeLoop;}],
			["commandAction", "Clear Loop", {this.clearLoop;}],
			["TXTimeBpmMinMaxSldr", "Loop time ms + bpm", holdControlSpec, "loopTime",
				"loopTimeMin", "loopTimeMax"],
			["TXCheckBox", "Record", "record"],
			["EZslider", "Input Level", ControlSpec(0, 1), "inLevel"],
			["EZslider", "Feedback", ControlSpec(0, 1), "feedback"],
			["TXMinMaxSliderSplit", "Speed factor", holdControlSpec2, "speedFactor",
				"speedFactorMin", "speedFactorMax"],
			["WetDryMixSlider"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make buffers, load the synthdef and create the synth
		this.makeBuffersAndSynth(classData.arrBufferSpecs);
	}

	clearLoop{
		this.buffers.at(0).zero;
		system.showViewIfModDisplay(this);
	}

	freezeLoop{
		this.setSynthValue("record", 0);
		this.setSynthValue("inLevel", 0);
		this.setSynthValue("feedback", 1);
		system.showViewIfModDisplay(this);
	}

	delayTimeMultiply { arg argMultiplyValue;
		var currentTime, minTime, maxTime, holdControlSpec, newTime;
		minTime = this.getSynthArgSpec("loopTimeMin");
		maxTime = this.getSynthArgSpec("loopTimeMax");
		holdControlSpec = ControlSpec.new(minTime, maxTime, \lin);
		currentTime = holdControlSpec.map(this.getSynthArgSpec("loopTime"));
		newTime = currentTime * argMultiplyValue;
		if (argMultiplyValue < 1, {
			if ( newTime >= minTime, {
				this.setSynthValue("loopTime", holdControlSpec.unmap(newTime));
			});
		},{
			if ( newTime <= maxTime, {
				this.setSynthValue("loopTime", holdControlSpec.unmap(newTime));
			});
		});
		system.flagGuiIfModDisplay(this);
	}

	useTapTempo {arg argTempo;
		var holdDelay, autoBPM, minDelay, maxDelay;
		holdDelay = 60000/argTempo;
		autoBPM = this.getSynthArgSpec("autoTapTempo");
		minDelay = this.getSynthArgSpec("loopTimeMin");
		maxDelay = this.getSynthArgSpec("loopTimeMax");
		if (autoBPM == 1,{
			if ((holdDelay >= minDelay) and: (holdDelay <= maxDelay),{
				this.setSynthArgSpec("loopTime", ControlSpec(minDelay, maxDelay).unmap(holdDelay));
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

