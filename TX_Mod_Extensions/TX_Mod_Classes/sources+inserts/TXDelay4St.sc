// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXDelay4St : TXModuleBase {		// delay stereo

	//	Notes:
	//	This is a delay which can be set to any time up to 5 minutes.
	//	This version uses BufCombC

	classvar <>classData;

	var 		holdTapTime, newTapTime;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Delay St";
		classData.moduleRate = "audio";
		classData.moduleType = "insert";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Delay Time L", 1, "modDelayL", 0],
			["Delay Time R", 1, "modDelayR", 0],
			["Feedback L", 1, "modFeedbackL", 0],
			["Feedback R", 1, "modFeedbackR", 0],
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
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		extraLatency = 0.2;	// allow extra time when recreating
		arrOptions = [0, 0, 0, 0];
		arrOptionData = [
			[ // 0 - max delay
				["15 seconds", 15],
				["30 seconds", 30],
				["1 minute", 60],
				["2 minutes", 120],
				["3 minutes", 180],
				["4 minutes", 240],
				["5 minutes", 300],
			],
			[ // 1 - feedback type
				["Positive Feedback", 1],
				["Negative Feedback", -1],
			],
			[ // 2 - use Delay Time L for R
				["Off", 0],
				["On", 1],
			],
			[ // 3 - use Feedback L for R
				["Off", 0],
				["On", 1],
			],
		];
		arrSynthArgSpecs = [
			["in", 0, 0],
			["out", 0, 0],
			["bufnumDelayL", 0, \ir],
			["bufnumDelayR", 1, \ir],
			["delayL", 0.5, defLagTime],
			["delayMinL", 10, defLagTime],
			["delayMaxL", 1000 * this.getMaxDelaytime, defLagTime],
			["delayR", 0.5, defLagTime],
			["delayMinR", 10, defLagTime],
			["delayMaxR", 1000 * this.getMaxDelaytime, defLagTime],
			["feedbackL", 0.1, defLagTime],
			["feedbackMinL", 0.001, defLagTime],
			["feedbackMaxL", 1.0, defLagTime],
			["feedbackR", 0.1, defLagTime],
			["feedbackMinR", 0.001, defLagTime],
			["feedbackMaxR", 1.0, defLagTime],
			["freeze", 0, 0],
			["smoothTime", 0.2, 0],
			["wetDryMix", 1.0, defLagTime],
			["modDelayL", 0, 0],
			["modDelayR", 0, 0],
			["modFeedbackL", 0, 0],
			["modFeedbackR", 0, 0],
			["modFreeze", 0, 0],
			["modSmoothTime", 0, 0],
			["modWetDryMix", 0, 0],
			// N.B. arg below not used in synthdef, just kept here for convenience
			["autoTapTempo", 0, \ir],
		];
		synthDefFunc = {
			arg in, out, bufnumDelayL, bufnumDelayR, delayL, delayMinL, delayMaxL, delayR, delayMinR, delayMaxR,
			feedbackL, feedbackMinL, feedbackMaxL, feedbackR, feedbackMinR, feedbackMaxR,
			freeze, smoothTime, wetDryMix, modDelayL=0, modDelayR=0, modFeedbackL=0, modFeedbackR=0,
			modFreeze=0, modSmoothTime=0, modWetDryMix=0;
			var inputL, inputR, outSound, delaytimeL, feedbackValL, decaytimeL, delaytimeR, feedbackValR, decaytimeR,
			feedbackType, mixCombined, freezeCombined, invLagFreeze, smoothTimeCombined;
			var startEnv = TXEnvPresets.startEnvFunc.value;

			freezeCombined = (freeze + modFreeze).max(0).min(1).round(1);
			invLagFreeze = 1 - Lag.kr(freezeCombined, 0.1);
			smoothTimeCombined = (smoothTime + modSmoothTime).max(0).min(1);
			inputL = TXClean.ar(startEnv * InFeedback.ar(in,1));
			inputR = TXClean.ar(startEnv * InFeedback.ar(in+1,1));
			delaytimeL = Lag2.kr(
				( (delayMaxL/delayMinL) ** (delayL + modDelayL).max(0.0001).min(1) ) * delayMinL / 1000,
				// (delayL + modDelayL).max(0.0001).min(1).linexp(0, 1, delayMinL, delayMaxL);
				smoothTimeCombined
			);
			if (arrOptions[2] == 1, { // SynthOption 2 - use Delay Time L for R
				delaytimeR = delaytimeL;
			}, {
				delaytimeR = Lag2.kr(
					( (delayMaxR/delayMinR) ** (delayR + modDelayR).max(0.0001).min(1) ) * delayMinR / 1000,
					// (delayR + modDelayR).max(0.0001).min(1).linexp(0, 1, delayMinR, delayMaxR);
					smoothTimeCombined
				);
			});
			feedbackType = this.getSynthOption(1);
			feedbackValL = feedbackMinL + ( (feedbackMaxL-feedbackMinL) * (feedbackL + modFeedbackL).max(0).min(1) );
			if (arrOptions[3] == 1, { // SynthOption 3 - use Feedback L for R
				feedbackValR = feedbackValL;
			}, {
				feedbackValR = feedbackMinR + ( (feedbackMaxR-feedbackMinR) * (feedbackR + modFeedbackR).max(0).min(1) );
			});
			decaytimeL = (( (100/delaytimeL) ** feedbackValL.sin) * delaytimeL)
			+ (freezeCombined * 3600); // if freeze is on add 60 minutes to decaytimeL
			decaytimeR = (( (100/delaytimeR) ** feedbackValR.sin) * delaytimeR)
			+ (freezeCombined * 3600); // if freeze is on add 60 minutes to decaytimeR
			mixCombined = (wetDryMix + modWetDryMix).max(0).min(1);
			outSound = [
				BufCombC.ar(bufnumDelayL, inputL * invLagFreeze, delaytimeL, feedbackType * decaytimeL, mixCombined,
					inputL * (1-mixCombined)),
				BufCombC.ar(bufnumDelayR, inputR * invLagFreeze, delaytimeR, feedbackType * decaytimeR, mixCombined,
					inputR * (1-mixCombined))
			];
			outSound = LeakDC.ar(outSound);
			Out.ar(out, TXClean.ar(startEnv * outSound));
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs(
			[["commandAction", "Tap Tempo", {this.actionTapTempo;}]]
			++ guiSpecArray);
		//	use base class initialise
		this.baseInit(this, argInstName);
		//	make buffers, load the synthdef and create the synth
		this.makeBuffersAndSynth(this.getArrBufferSpecs);
	}

	buildGuiSpecArray {
		var holdControlSpec, holdControlSpec2;
		holdControlSpec = ControlSpec.new(10, 1000 * this.getMaxDelaytime, \exp );
		holdControlSpec2 = ControlSpec.new(0.001, 1.0, \lin );
		guiSpecArray = [
			["SynthOptionPopupPlusMinus", "Max time", arrOptionData, 0, 300,
				{this.buildGuiSpecArray;
					system.showViewIfModDisplay(this);
					this.makeBuffersAndSynth(this.getArrBufferSpecs);
				}
			],
			["SpacerLine", 6],
			["TapTempoButton", {arg argTempo; this.useTapTempo(argTempo);}],
			["Spacer", 10],
			["TXCheckBox", "Auto copy tap tempo to delay bpm ", "autoTapTempo", nil, 230],
			["SpacerLine", 6],
			["TextBarLeft", "Delay time shown in ms and bpm", 200],
			["NextLine"],
			["TXTimeBpmMinMaxSldr", "Del. time L", holdControlSpec, "delayL", "delayMinL", "delayMaxL"],
			["NextLine"],
			["ActionButton", "time x 2", {this.delayTimeMultiplyL(2);}, 60],
			["ActionButton", "time x 3", {this.delayTimeMultiplyL(3);}, 60],
			["ActionButton", "time / 2", {this.delayTimeMultiplyL(0.5);}, 60],
			["ActionButton", "time / 3", {this.delayTimeMultiplyL(1/3);}, 60],
			["SpacerLine", 6],
			["TXTimeBpmMinMaxSldr", "Del. time R", holdControlSpec, "delayR", "delayMinR", "delayMaxR"],
			["NextLine"],
			["ActionButton", "time x 2", {this.delayTimeMultiplyR(2);}, 60],
			["ActionButton", "time x 3", {this.delayTimeMultiplyR(3);}, 60],
			["ActionButton", "time / 2", {this.delayTimeMultiplyR(0.5);}, 60],
			["ActionButton", "time / 3", {this.delayTimeMultiplyR(1/3);}, 60],
			["Spacer", 10],
			["SynthOptionCheckBox", "use Delay Time L for R", arrOptionData, 2, 190],
			["SpacerLine", 6],
			["EZslider", "Smooth time", ControlSpec(0, 1), "smoothTime"],
			["SpacerLine", 6],
			["TXMinMaxSliderSplit", "Feedback L", holdControlSpec2, "feedbackL", "feedbackMinL", "feedbackMaxL"],
			["TXMinMaxSliderSplit", "Feedback R ", holdControlSpec2, "feedbackR", "feedbackMinR", "feedbackMaxR"],
			["SpacerLine", 6],
			["SynthOptionPopupPlusMinus", "FB type", arrOptionData, 1, 260],
			["Spacer", 10],
			["SynthOptionCheckBox", "use Feedback L for R", arrOptionData, 3, 180],
			["SpacerLine", 4],
			["WetDryMixSlider"],
			["SpacerLine", 6],
			["TXCheckBox", "Freeze", "freeze"],
			["Spacer", 20],
			["ActionButtonDark", "Clear", {this.clearBuffers}],
		];
	}

	getMaxDelaytime {
		^arrOptionData.at(0).at(arrOptions.at(0)).at(1);
	}

	getArrBufferSpecs {
		classData.arrBufferSpecs = [
			["bufnumDelayL", defSampleRate * this.getMaxDelaytime, 1],
			["bufnumDelayR", defSampleRate * this.getMaxDelaytime, 1]
		];
		^classData.arrBufferSpecs;
	}

	delayTimeMultiplyL { arg argMultiplyValue;
		var currentTime, minTime, maxTime, holdControlSpec, newTime;
		minTime = this.getSynthArgSpec("delayMinL");
		maxTime = this.getSynthArgSpec("delayMaxL");
		holdControlSpec = ControlSpec.new(minTime, maxTime, \exp);
		currentTime = holdControlSpec.map(this.getSynthArgSpec("delayL"));
		newTime = currentTime * argMultiplyValue;
		if (argMultiplyValue < 1, {
			if ( newTime >= minTime, {
				this.setSynthValue("delayL", holdControlSpec.unmap(newTime));
			});
		},{
			if ( newTime <= maxTime, {
				this.setSynthValue("delayL", holdControlSpec.unmap(newTime));
			});
		});
		system.flagGuiIfModDisplay(this);
	}

	delayTimeMultiplyR { arg argMultiplyValue;
		var currentTime, minTime, maxTime, holdControlSpec, newTime;
		minTime = this.getSynthArgSpec("delayMinR");
		maxTime = this.getSynthArgSpec("delayMaxR");
		holdControlSpec = ControlSpec.new(minTime, maxTime, \exp);
		currentTime = holdControlSpec.map(this.getSynthArgSpec("delayR"));
		newTime = currentTime * argMultiplyValue;
		if (argMultiplyValue < 1, {
			if ( newTime >= minTime, {
				this.setSynthValue("delayR", holdControlSpec.unmap(newTime));
			});
		},{
			if ( newTime <= maxTime, {
				this.setSynthValue("delayR", holdControlSpec.unmap(newTime));
			});
		});
		system.flagGuiIfModDisplay(this);
	}

	useTapTempo {arg argTempo;
		var holdDelay, autoBPM, minDelay, maxDelay;
		holdDelay = 60000/argTempo;
		autoBPM = this.getSynthArgSpec("autoTapTempo");
		// set left
		minDelay = this.getSynthArgSpec("delayMinL");
		maxDelay = this.getSynthArgSpec("delayMaxL");
		if (autoBPM == 1,{
			if ((holdDelay >= minDelay) and: (holdDelay <= maxDelay),{
				this.setSynthArgSpec("delayL", ControlSpec(minDelay, maxDelay, \exp).unmap(holdDelay));
				system.flagGuiIfModDisplay(this);
			});
		});
		// set right
		minDelay = this.getSynthArgSpec("delayMinR");
		maxDelay = this.getSynthArgSpec("delayMaxR");
		if (autoBPM == 1,{
			if ((holdDelay >= minDelay) and: (holdDelay <= maxDelay),{
				this.setSynthArgSpec("delayR", ControlSpec(minDelay, maxDelay, \exp).unmap(holdDelay));
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

	loadExtraData {arg argData;
		this.buildGuiSpecArray;
		system.showViewIfModDisplay(this);
		{this.makeBuffersAndSynth(this.getArrBufferSpecs);}.defer(2);
	}

}

