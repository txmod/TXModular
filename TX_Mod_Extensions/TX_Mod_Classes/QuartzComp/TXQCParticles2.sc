// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXQCParticles2 : TXModuleBase {		// Quartz Composition

	classvar <>classData;

	var	hueResp;
	var	brightnessResp;
	var	saturationResp;
	var	pEnableResp;
	var	pXposResp;
	var	pYposResp;
	var	pZposResp;
	var	pMinSizeResp;
	var	pMaxSizeResp;
	var	pSizeDeltaResp;
	var	pOpacityDeltaResp;
	var	pAttractionResp;
	var	pGravityResp;
	var	pHueResp;
	var	pSaturationResp;
	var	pBrightnessResp;
	var	pXMinVelResp;
	var	pXMaxVelResp;
	var	pYMinVelResp;
	var	pYMaxVelResp;
	var	pZMinVelResp;
	var	pZMaxVelResp;
	var	pLifetime;

	var	holdQCWindow, holdQCView;
	var	screenWidth, screenHeight;
	var arrResps, arrInputs, arrSendTrigIDs;
	var	displayOption;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "QC Particles";
		classData.moduleRate = "control";
		classData.moduleType = "action";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Screen Hue", 1, "modHue", 0],
			["Screen Saturation", 1, "modSaturation", 0],
			["Screen Brightness", 1, "modBrightness", 0],
			["Particles on", 1, "modpEnable", 0],
			["X Position", 1, "modpXpos", 0],
			["Y Position", 1, "modpYpos", 0],
			["Z Position", 1, "modpZpos", 0],
			["Min Size", 1, "modpMinSize", 0],
			["Max Size", 1, "modpMaxSize", 0],
			["Size Delta", 1, "modpSizeDelta", 0],
			["Opacity Delta", 1, "modpOpacityDelta", 0],
			["Attraction", 1, "modpAttraction", 0],
			["Gravity", 1, "modpGravity", 0],
			["Part Hue", 1, "modpHue", 0],
			["Part Saturation", 1, "modpSaturation", 0],
			["Part Brightness", 1, "modpBrightness", 0],
			["X vel min", 1, "modpXMinVel", 0],
			["X vel max", 1, "modpXMaxVel", 0],
			["Y vel min", 1, "modpYMinVel", 0],
			["Y vel max", 1, "modpYMaxVel", 0],
			["Z vel min", 1, "modpZMinVel", 0],
			["Z vel max", 1, "modpZMaxVel", 0],
			["Lifetime", 1, "modpLifetime", 0],
		];
		classData.noOutChannels = 0;
		classData.arrOutBusSpecs = [];
		classData.holdScreenSizes = [ [640, 480], [720, 480], [800, 500], [800, 600], [1024, 640], [1024, 768],
			[1152, 720], [1280, 800], [1440, 900]
		];
		classData.holdScreenSizeTexts = classData.holdScreenSizes.collect({arg item, i; item.at(0).asString + "X" + item.at(1).asString});
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "show1";
		arrResps = [hueResp, saturationResp, brightnessResp, pEnableResp,pXposResp, pYposResp, pZposResp,
			pMinSizeResp, pMaxSizeResp, pSizeDeltaResp, pOpacityDeltaResp, pAttractionResp, pGravityResp, pHueResp,
			pSaturationResp, pBrightnessResp, pXMinVelResp, pXMaxVelResp, pYMinVelResp, pYMaxVelResp,
			pZMinVelResp, pZMaxVelResp, pLifetime];
		arrInputs = ['hue', 'saturation', 'brightness', 'pEnable', 'pXpos', 'pYpos', 'pZpos', 'pMinSize',
			'pMaxSize', 'pSizeDelta', 'pOpacityDelta', 'pAttraction', 'pGravity', 'pHue',
			'pSaturation', 'pBrightness', 'pXMinVel', 'pXMaxVel', 'pYMinVel', 'pYMaxVel', 'pZMinVel',
			'pZMaxVel', 'pLifetime'];
		arrSendTrigIDs = [];
		// create unique ids
		23.do({arg item, i;
			arrSendTrigIDs = arrSendTrigIDs.add(UniqueID.next);
		});
		screenWidth = 1024;
		screenHeight = 640;
		arrSynthArgSpecs = [
			["out", 0, 0],
			["active", 1, 0],
			["hue", 0, defLagTime],
			["saturation", 0, 0],
			["brightness", 0, 0],
			["pEnable", 1, 0],
			["pXpos", 0, 0],
			["pYpos", 0, 0],
			["pZpos", 0, 0],
			["pMinSize", 0.1, 0],
			["pMaxSize", 0.3, 0],
			["pSizeDelta", -0.1, 0],
			["pOpacityDelta", 0, 0],
			["pAttraction", 0, 0],
			["pGravity", 0, 0],
			["pHue", 0.5, 0],
			["pSaturation", 0.5, 0],
			["pBrightness", 0.5, 0],
			["pXMinVel", -0.3, 0],
			["pXMaxVel", 0.3, 0],
			["pYMinVel", -0.3, 0],
			["pYMaxVel", 0.3, 0],
			["pZMinVel", -0.3, 0],
			["pZMaxVel", 0.3, 0],
			["pLifetime", 0.3, 0],
			["modHue", 0, 0],
			["modSaturation", 0, 0],
			["modBrightness", 0, 0],
			["modpEnable", 0, 0],
			["modpXpos", 0, 0],
			["modpYpos", 0, 0],
			["modpZpos", 0, 0],
			["modpMinSize", 0, 0],
			["modpMaxSize", 0, 0],
			["modpSizeDelta", 0, 0],
			["modpOpacityDelta", 0, 0],
			["modpAttraction", 0, 0],
			["modpGravity", 0, 0],
			["modpHue", 0, 0],
			["modpSaturation", 0, 0],
			["modpBrightness", 0, 0],
			["modpXMinVel", 0, 0],
			["modpXMaxVel", 0, 0],
			["modpYMinVel", 0, 0],
			["modpYMaxVel", 0, 0],
			["modpZMinVel", 0, 0],
			["modpZMaxVel", 0, 0],
			["modpLifetime", 0, 0],
			["i_screenSize", 4, 0],
		];

		synthDefFunc = {
			arg out, active, hue, saturation, brightness, pEnable, pXpos, pYpos, pZpos, pMinSize, pMaxSize,
			pSizeDelta, pOpacityDelta, pAttraction, pGravity, pHue, pSaturation, pBrightness,
			pXMinVel, pXMaxVel, pYMinVel, pYMaxVel, pZMinVel, pZMaxVel, pLifetime,
			modHue=0, modSaturation=0,
			modBrightness=0, modpEnable=0, modpXpos=0, modpYpos=0, modpZpos=0, modpMinSize=0, modpMaxSize=0, modpSizeDelta=0,
			modpOpacityDelta=0, modpAttraction=0, modpGravity=0, modpHue=0, modpSaturation=0, modpBrightness=0,
			modpXMinVel=0, modpXMaxVel=0, modpYMinVel=0, modpYMaxVel=0, modpZMinVel=0, modpZMaxVel=0,
			modpLifetime=0, i_screenSize;
			var imp, trig1, trig2, trig3, trig4, trig5, trig6, trig7, trig8, trig9, trig10, trig11, trig12, trig13,
			trig14, trig15, trig16, trig17, trig18, trig19, trig20, trig21, trig22, trig23;
			var holdHue, holdSaturation, holdBrightness, holdpEnable, holdpXpos, holdpYpos, holdpZpos,
			holdpMinSize, holdpMaxSize, holdpSizeDelta, holdpOpacityDelta, holdpAttraction, holdpGravity,
			holdpHue, holdpSaturation, holdpBrightness, holdpXMinVel, holdpXMaxVel, holdpYMinVel, holdpYMaxVel,
			holdpZMinVel, holdpZMaxVel, holdpLifetime;

			holdHue = (hue + modHue).max(0).min(1);
			holdSaturation = (saturation + modSaturation).max(0).min(1);
			holdBrightness = (brightness + modBrightness).max(0).min(1);
			holdpEnable = (pEnable + modpEnable).max(0).min(1);
			holdpXpos = (pXpos + modpXpos).max(-1).min(1);
			holdpYpos = (pYpos + modpYpos).max(-1).min(1);
			holdpZpos = (pZpos + modpZpos).max(-1).min(1);
			holdpMinSize = (pMinSize + modpMinSize).max(0).min(1);
			holdpMaxSize = (pMaxSize + modpMaxSize).max(0).min(1);
			holdpSizeDelta = (pSizeDelta + modpSizeDelta).max(-1).min(1);
			holdpOpacityDelta = (pOpacityDelta + modpOpacityDelta).max(-1).min(1);
			holdpAttraction = (pAttraction + modpAttraction).max(-1).min(1);
			holdpGravity = (pGravity + modpGravity).max(-1).min(1);
			holdpHue = (pHue + modpHue).max(0).min(1);
			holdpSaturation = (pSaturation + modpSaturation).max(0).min(1);
			holdpBrightness = (pBrightness + modpBrightness).max(0).min(1);
			holdpXMinVel = (pXMinVel + modpXMinVel).max(-1).min(1);
			holdpXMaxVel = (pXMaxVel + modpXMaxVel).max(-1).min(1);
			holdpYMinVel = (pYMinVel + modpYMinVel).max(-1).min(1);
			holdpYMaxVel = (pYMaxVel + modpYMaxVel).max(-1).min(1);
			holdpZMinVel = (pZMinVel + modpZMinVel).max(-1).min(1);
			holdpZMaxVel = (pZMaxVel + modpZMaxVel).max(-1).min(1);
			holdpLifetime = (pLifetime + modpLifetime).max(0).min(1);
			imp = LFPulse.kr(20, 0, 0.999) * active;
			trig1 = Trig.kr(imp * HPZ1.kr(holdHue).abs, 0.02);
			trig2 = Trig.kr(imp * HPZ1.kr(holdSaturation).abs, 0.02);
			trig3 = Trig.kr(imp * HPZ1.kr(holdBrightness).abs, 0.02);
			trig4 = Trig.kr(imp * HPZ1.kr(holdpEnable).abs, 0.02);
			trig5 = Trig.kr(imp * HPZ1.kr(holdpXpos).abs, 0.02);
			trig6 = Trig.kr(imp * HPZ1.kr(holdpYpos).abs, 0.02);
			trig7 = Trig.kr(imp * HPZ1.kr(holdpZpos).abs, 0.02);
			trig8 = Trig.kr(imp * HPZ1.kr(holdpMinSize).abs, 0.02);
			trig9 = Trig.kr(imp * HPZ1.kr(holdpMaxSize).abs, 0.02);
			trig10 = Trig.kr(imp * HPZ1.kr(holdpSizeDelta).abs, 0.02);
			trig11 = Trig.kr(imp * HPZ1.kr(holdpOpacityDelta).abs, 0.02);
			trig12 = Trig.kr(imp * HPZ1.kr(holdpAttraction).abs, 0.02);
			trig13 = Trig.kr(imp * HPZ1.kr(holdpGravity).abs, 0.02);
			trig14 = Trig.kr(imp * HPZ1.kr(holdpHue).abs, 0.02);
			trig15 = Trig.kr(imp * HPZ1.kr(holdpSaturation).abs, 0.02);
			trig16 = Trig.kr(imp * HPZ1.kr(holdpBrightness).abs, 0.02);
			trig17 = Trig.kr(imp * HPZ1.kr(holdpXMinVel).abs, 0.02);
			trig18 = Trig.kr(imp * HPZ1.kr(holdpXMaxVel).abs, 0.02);
			trig19 = Trig.kr(imp * HPZ1.kr(holdpYMinVel).abs, 0.02);
			trig20 = Trig.kr(imp * HPZ1.kr(holdpYMaxVel).abs, 0.02);
			trig21 = Trig.kr(imp * HPZ1.kr(holdpZMinVel).abs, 0.02);
			trig22 = Trig.kr(imp * HPZ1.kr(holdpZMaxVel).abs, 0.02);
			trig23 = Trig.kr(imp * HPZ1.kr(holdpLifetime).abs, 0.02);
			SendTrig.kr(trig1, arrSendTrigIDs.at(0), holdHue);
			SendTrig.kr(trig2, arrSendTrigIDs.at(1), holdSaturation);
			SendTrig.kr(trig3, arrSendTrigIDs.at(2), holdBrightness);
			SendTrig.kr(trig4, arrSendTrigIDs.at(3), holdpEnable);
			SendTrig.kr(trig5, arrSendTrigIDs.at(4), holdpXpos);
			SendTrig.kr(trig6, arrSendTrigIDs.at(5), holdpYpos);
			SendTrig.kr(trig7, arrSendTrigIDs.at(6), holdpZpos);
			SendTrig.kr(trig8, arrSendTrigIDs.at(7), holdpMinSize);
			SendTrig.kr(trig9, arrSendTrigIDs.at(8), holdpMaxSize);
			SendTrig.kr(trig10, arrSendTrigIDs.at(9), holdpSizeDelta);
			SendTrig.kr(trig11, arrSendTrigIDs.at(10), holdpOpacityDelta);
			SendTrig.kr(trig12, arrSendTrigIDs.at(11), holdpAttraction);
			SendTrig.kr(trig13, arrSendTrigIDs.at(12), holdpGravity);
			SendTrig.kr(trig14, arrSendTrigIDs.at(13), holdpHue);
			SendTrig.kr(trig15, arrSendTrigIDs.at(14), holdpSaturation);
			SendTrig.kr(trig16, arrSendTrigIDs.at(15), holdpBrightness);
			SendTrig.kr(trig17, arrSendTrigIDs.at(16), holdpXMinVel);
			SendTrig.kr(trig18, arrSendTrigIDs.at(17), holdpXMaxVel);
			SendTrig.kr(trig19, arrSendTrigIDs.at(18), holdpYMinVel);
			SendTrig.kr(trig20, arrSendTrigIDs.at(19), holdpYMaxVel);
			SendTrig.kr(trig21, arrSendTrigIDs.at(20), holdpZMinVel);
			SendTrig.kr(trig22, arrSendTrigIDs.at(21), holdpZMaxVel);
			SendTrig.kr(trig23, arrSendTrigIDs.at(22), holdpLifetime);

			// Note this synth doesn't need to write to the output bus
		};
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Show Video Screen", {this.rebuildQCScreen;}],
			["TXPopupAction", "Screen Size", classData.holdScreenSizeTexts, "i_screenSize", {this.resetScreenSize; this.oscActivate;}, 200],
			["TXCheckBox", "Active", "active"],
			["EZslider", "Screen Hue", ControlSpec(0,1), "hue"],
			["EZslider", "Screen Saturation", ControlSpec(0,1), "saturation"],
			["EZslider", "Screen Brightness", ControlSpec(0,1), "brightness"],
			//		["TXCheckBox", "Particles on", "pEnable"],
			["EZslider", "X position", ControlSpec(-1,1), "pXpos"],
			["EZslider", "Y position", ControlSpec(-1,1), "pYpos"],
			["EZslider", "Z position", ControlSpec(-1,1), "pZpos"],
			["EZslider", "Min Size", ControlSpec(0,1), "pMinSize"],
			["EZslider", "Max Size", ControlSpec(0,1), "pMaxSize"],
			["EZslider", "Size Delta", ControlSpec(-1,1), "pSizeDelta"],
			["EZslider", "Opacity Delta", ControlSpec(-1,1), "pOpacityDelta"],
			["EZslider", "Attraction", ControlSpec(-1,1), "pAttraction"],
			["EZslider", "Gravity", ControlSpec(-1,1), "pGravity"],
			["EZslider", "Part Hue", ControlSpec(0,1), "pHue"],
			["EZslider", "Part Saturation", ControlSpec(0,1), "pSaturation"],
			["EZslider", "Part Brightness", ControlSpec(0,1), "pBrightness"],
			["EZslider", "X vel min", ControlSpec(-1,1), "pXMinVel"],
			["EZslider", "X vel max", ControlSpec(-1,1), "pXMaxVel"],
			["EZslider", "Y vel min", ControlSpec(-1,1), "pYMinVel"],
			["EZslider", "Y vel max", ControlSpec(-1,1), "pYMaxVel"],
			["EZslider", "Z vel min", ControlSpec(-1,1), "pZMinVel"],
			["EZslider", "Z vel max", ControlSpec(-1,1), "pZMaxVel"],
			["EZslider", "Lifetime", ControlSpec(0,1), "pLifetime"],
		]);
		// initialise
		this.baseInit(this, argInstName);
		this.oscActivate;
		this.resetScreenSize;

		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["TXPopupAction", "Screen Size", classData.holdScreenSizeTexts, "i_screenSize", {this.resetScreenSize; this.oscActivate;}, 200],
			["ActionButton", "Show Video Screen", {this.rebuildQCScreen;}, 200],
			//		["NextLine"],
			//		["TXCheckBox", "Active", "active"],
			["SpacerLine", 6],
			["ActionButton", "settings 1", {displayOption = "show1";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "show1")],
			["Spacer", 3],
			["ActionButton", "settings 2", {displayOption = "show2";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "show2")],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "show1", {
			guiSpecArray = guiSpecArray ++[
				["EZslider", "Screen hue", ControlSpec(0,1), "hue"],
				["EZslider", "Screen satur", ControlSpec(0,1), "saturation"],
				["EZslider", "Scr'n br'ness", ControlSpec(0,1), "brightness"],
				["DividingLine"],
				//		["TXCheckBox", "Particles on", "pEnable"],
				//		["NextLine"],
				["EZslider", "X position", ControlSpec(-1,1), "pXpos"],
				["EZslider", "Y position", ControlSpec(-1,1), "pYpos"],
				["EZslider", "Z position", ControlSpec(-1,1), "pZpos"],
				["DividingLine"],
				["EZslider", "Min size", ControlSpec(0,1), "pMinSize"],
				["EZslider", "Max size", ControlSpec(0,1), "pMaxSize"],
				["DividingLine"],
				["EZslider", "Size delta", ControlSpec(-1,1), "pSizeDelta"],
				["EZslider", "Opacity delta", ControlSpec(-1,1), "pOpacityDelta"],
			];
		});
		if (displayOption == "show2", {
			guiSpecArray = guiSpecArray ++[
				["EZslider", "Lifetime", ControlSpec(0,1), "pLifetime"],
				["DividingLine"],
				["EZslider", "Part hue", ControlSpec(0,1), "pHue"],
				["EZslider", "Part sat.", ControlSpec(0,1), "pSaturation"],
				["EZslider", "Part br'ness", ControlSpec(0,1), "pBrightness"],
				["DividingLine"],
				["EZslider", "Attraction", ControlSpec(-1,1), "pAttraction"],
				["EZslider", "Gravity", ControlSpec(-1,1), "pGravity"],
				["DividingLine"],
				["EZslider", "X vel min", ControlSpec(-1,1), "pXMinVel"],
				["EZslider", "X vel max", ControlSpec(-1,1), "pXMaxVel"],
				["EZslider", "Y vel min", ControlSpec(-1,1), "pYMinVel"],
				["EZslider", "Y vel max", ControlSpec(-1,1), "pYMaxVel"],
				["EZslider", "Z vel min", ControlSpec(-1,1), "pZMinVel"],
				["EZslider", "Z vel max", ControlSpec(-1,1), "pZMaxVel"],
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

	showQCScreen {
		{
			//	check if window exists
			if (holdQCWindow.isNil) {
				// make window
				holdQCWindow= Window(" ", Rect(0, 580, screenWidth, screenHeight)).front;
				holdQCWindow.onClose_({
					holdQCWindow = nil;
					holdQCView = nil;
				});
				// make Button
				Button(holdQCWindow, Rect(2, 2, 30, 20))
				.states_([["<-", Color.white, Color.grey(0.1)]])
				.action_({system.windowToFront});
				// make background
				StaticText(holdQCWindow, Rect(0 ,0, 1440, 900)).background_(TXColor.black);
				// make Quartz
				holdQCView = QuartzComposerView(holdQCWindow, Rect(20 ,25, screenWidth-40, screenHeight-50));
				holdQCView.path = this.class.filenameSymbol.asString.dirname ++ "/Particles2.qtz";
				holdQCView.start;
			}{
				// if window exists bring to front
				holdQCWindow.front;
			};
		}.defer;
	}

	rebuildQCScreen {
		{	//	check if window exists
			if (holdQCWindow.notNil) {holdQCWindow.close};
		}.defer;
		{
			this.showQCScreen;
		}.defer(0.1);
	}

	resetScreenSize {
		screenWidth = classData.holdScreenSizes.at(this.getSynthArgSpec("i_screenSize")).at(0);
		screenHeight = classData.holdScreenSizes.at(this.getSynthArgSpec("i_screenSize")).at(1);
		this.rebuildQCScreen;
		this.sendCurrentValues;
	}

	sendCurrentValues {
		{
			arrInputs.do({arg item, i;
				holdQCView.setInputValue(item, this.getSynthArgSpec(item.asString));
			});
		}.defer(0.4);
	}

	oscActivate {
		//	remove any previous responders and add new
		this.oscDeactivate;
		arrResps.do({arg item, i;
			item = OSCFunc({arg msg, time, addr,  recvPort;
				if (msg[2] == arrSendTrigIDs.at(i),{
					{	if (holdQCView.notNil,  {
						holdQCView.setInputValue(arrInputs.at(i), msg[3]);
					});
					}.defer;
				});
			}, '/tr', system.server.addr);
		});
	}

	oscDeactivate {
		//	remove responders
		arrResps.do({arg item, i;
			item.free;
		});
	}

	loadExtraData {
		this.resetScreenSize;
	}

	deleteModuleExtraActions {
		//	remove responders
		this.oscDeactivate;
		if (holdQCWindow.notNil) {
			// if window exists close it
			holdQCWindow.close;
		};
	}

}


