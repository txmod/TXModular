// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXAnimateCode2 : TXModuleBase {

	classvar <>classData;

	var holdScreenSizes, holdScreenSizeTexts, holdSSPresetActions;
	var holdBorderSizes, holdBorderSizeTexts, holdBorderSizePresetActions;

	var mod1Resp, mod2Resp, mod3Resp, mod4Resp, mod5Resp, mod6Resp, mod7Resp;
	var mod8Resp, mod9Resp, mod10Resp, mod11Resp, mod12Resp, mod13Resp, mod14Resp;
	var mod15Resp, mod16Resp, mod17Resp, mod18Resp, mod19Resp, mod20Resp;

	var holdWindow, holdView;
	var screenWidth, screenHeight;
	var screenPosX, screenPosY;
	var arrResps, arrSendTrigIDs;
	var displayOption;
	var arrPassVals;
	var userFunctionString;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Animate Code";
		classData.moduleRate = "control";
		classData.moduleType = "action";
		classData.noInChannels = 2;
		classData.arrCtlSCInBusSpecs = [
			["Value 1", 1, "modVal1",  0],
			["Value 2", 1, "modVal2",  0],
			["Value 3", 1, "modVal3",  0],
			["Value 4", 1, "modVal4",  0],
			["Value 5", 1, "modVal5",  0],
			["Value 6", 1, "modVal6",  0],
			["Value 7", 1, "modVal7",  0],
			["Value 8", 1, "modVal8",  0],
			["Value 9", 1, "modVal9",  0],
			["Value 10", 1, "modVal10",  0],
			["Value 11", 1, "modVal11",  0],
			["Value 12", 1, "modVal12",  0],
			["Value 13", 1, "modVal13",  0],
			["Value 14", 1, "modVal14",  0],
			["Value 15", 1, "modVal15",  0],
			["Value 16", 1, "modVal16",  0],
			["Value 17", 1, "modVal17",  0],
			["Value 18", 1, "modVal18",  0],
			["Value 19", 1, "modVal19",  0],
			["Value 20", 1, "modVal20",  0],
		];
		classData.noOutChannels = 0;
		classData.arrOutBusSpecs = [];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showWindow";
		arrResps = [mod1Resp, mod2Resp, mod3Resp, mod4Resp, mod5Resp, mod6Resp, mod7Resp, mod8Resp,
			mod9Resp, mod10Resp, mod11Resp, mod12Resp, mod13Resp, mod14Resp, mod15Resp, mod16Resp, mod17Resp,
			mod18Resp, mod19Resp, mod20Resp];
		arrSendTrigIDs = [];
		// create unique ids
		20.do({arg item, i;
			arrSendTrigIDs = arrSendTrigIDs.add(UniqueID.next);
		});
		screenWidth = 640;
		screenHeight = 480;
		screenPosX = 0;
		screenPosY = 60;
		arrPassVals = 0.5 ! 20;
		userFunctionString = "
// Example code -  USE SLIDERS 1-4 TO CHANGE ANIMATION
{
arg animationView, arrPassVals;
var xMult, yMult, mx, my, vx, vy, vcol, frame;
xMult = 0.0021 * arrPassVals[0];
yMult = 0.0031 * arrPassVals[1];
mx = animationView.bounds.width * arrPassVals[2];
my = animationView.bounds.height * arrPassVals[3];
Pen.color = Color.white;
Pen.moveTo(Point(my, mx));
frame = animationView.frame;
vx = sin(frame/219)* 1.3;
vy = cos( frame/311) * 1.3;
vcol = sin(frame/519).abs;
Pen.color = Color.hsv(vcol, 0.8, 0.6);
100.do{|j|
var x = sin(frame * xMult.neg + j) * (5 * j) + mx;
var y = cos(frame * yMult + j) * (5 * j) + my;
Pen.addOval(Rect(x, y, j * vx, j * vy));
};
Pen.fillStroke;
}
";
		arrSynthArgSpecs = [
			["out", 0, 0],
			["active", 1, 0],

			["val1", 0.5, 0],
			["val2", 0.5, 0],
			["val3", 0.5, 0],
			["val4", 0.5, 0],
			["val5", 0.5, 0],
			["val6", 0.5, 0],
			["val7", 0.5, 0],
			["val8", 0.5, 0],
			["val9", 0.5, 0],
			["val10", 0.5, 0],
			["val11", 0.5, 0],
			["val12", 0.5, 0],
			["val13", 0.5, 0],
			["val14", 0.5, 0],
			["val15", 0.5, 0],
			["val16", 0.5, 0],
			["val17", 0.5, 0],
			["val18", 0.5, 0],
			["val19", 0.5, 0],
			["val20", 0.5, 0],

			["minVal1", 0, 0],
			["minVal2", 0, 0],
			["minVal3", 0, 0],
			["minVal4", 0, 0],
			["minVal5", 0, 0],
			["minVal6", 0, 0],
			["minVal7", 0, 0],
			["minVal8", 0, 0],
			["minVal9", 0, 0],
			["minVal10", 0, 0],
			["minVal11", 0, 0],
			["minVal12", 0, 0],
			["minVal13", 0, 0],
			["minVal14", 0, 0],
			["minVal15", 0, 0],
			["minVal16", 0, 0],
			["minVal17", 0, 0],
			["minVal18", 0, 0],
			["minVal19", 0, 0],
			["minVal20", 0, 0],

			["maxVal1", 1, 0],
			["maxVal2", 1, 0],
			["maxVal3", 1, 0],
			["maxVal4", 1, 0],
			["maxVal5", 1, 0],
			["maxVal6", 1, 0],
			["maxVal7", 1, 0],
			["maxVal8", 1, 0],
			["maxVal9", 1, 0],
			["maxVal10", 1, 0],
			["maxVal11", 1, 0],
			["maxVal12", 1, 0],
			["maxVal13", 1, 0],
			["maxVal14", 1, 0],
			["maxVal15", 1, 0],
			["maxVal16", 1, 0],
			["maxVal17", 1, 0],
			["maxVal18", 1, 0],
			["maxVal19", 1, 0],
			["maxVal20", 1, 0],

			["modVal1", 0, 0],
			["modVal2", 0, 0],
			["modVal3", 0, 0],
			["modVal4", 0, 0],
			["modVal5", 0, 0],
			["modVal6", 0, 0],
			["modVal7", 0, 0],
			["modVal8", 0, 0],
			["modVal9", 0, 0],
			["modVal10", 0, 0],
			["modVal11", 0, 0],
			["modVal12", 0, 0],
			["modVal13", 0, 0],
			["modVal14", 0, 0],
			["modVal15", 0, 0],
			["modVal16", 0, 0],
			["modVal17", 0, 0],
			["modVal18", 0, 0],
			["modVal19", 0, 0],
			["modVal20", 0, 0],

			["maxFPS", 60, 0],

			// N.B. the args below aren't used in the synthdef, just stored as synth args for convenience

			["i_screenWidth", 640, 0],
			["i_screenHeight", 480, 0],
			["i_posX", 0, 0],
			["i_posY", 60, 0],
			["i_borderX", 20, 0],
			["i_borderY", 20, 0],
			["i_winControls", 1, 0],
			["i_windowOnTop", 0, 0],
			["i_clearOnRefresh", 1, 0],

			["i_backgroundH", 0, 0],
			["i_backgroundS", 0, 0],
			["i_backgroundV", 0, 0],

			["name1", "", 0],
			["name2", "", 0],
			["name3", "", 0],
			["name4", "", 0],
			["name5", "", 0],
			["name6", "", 0],
			["name7", "", 0],
			["name8", "", 0],
			["name9", "", 0],
			["name10", "", 0],
			["name11", "", 0],
			["name12", "", 0],
			["name13", "", 0],
			["name14", "", 0],
			["name15", "", 0],
			["name16", "", 0],
			["name17", "", 0],
			["name18", "", 0],
			["name19", "", 0],
			["name20", "", 0],
		];

		synthDefFunc = {
			arg out, active,

			val1,  val2,  val3,  val4,  val5,  val6,  val7,  val8,  val9,  val10,  val11,  val12,  val13,
			val14,  val15,  val16,  val17,  val18,  val19,  val20,

			minVal1,  minVal2,  minVal3,  minVal4,  minVal5,  minVal6,  minVal7,  minVal8,  minVal9,
			minVal10,  minVal11,  minVal12,  minVal13,  minVal14,  minVal15,  minVal16,  minVal17,
			minVal18,  minVal19,  minVal20,

			maxVal1,  maxVal2,  maxVal3,  maxVal4,  maxVal5,  maxVal6,  maxVal7,  maxVal8,  maxVal9,
			maxVal10,  maxVal11,  maxVal12,  maxVal13,  maxVal14,  maxVal15,  maxVal16,  maxVal17,
			maxVal18,  maxVal19,  maxVal20,

			modVal1=0,  modVal2=0,  modVal3=0,  modVal4=0,  modVal5=0,  modVal6=0,  modVal7=0,  modVal8=0,  modVal9=0,
			modVal10=0,  modVal11=0,  modVal12=0,  modVal13=0,  modVal14=0,  modVal15=0,  modVal16=0,  modVal17=0,
			modVal18=0,  modVal19=0,  modVal20=0, maxFPS;

			var imp, backupImp, trig1, trig2, trig3, trig4, trig5, trig6, trig7, trig8, trig9, trig10, trig11, trig12, trig13,
			trig14, trig15, trig16, trig17, trig18, trig19, trig20;

			var 	holdVal1,  holdVal2,  holdVal3,  holdVal4,  holdVal5,  holdVal6,  holdVal7,
			holdVal8,  holdVal9,  holdVal10,  holdVal11,  holdVal12,  holdVal13,  holdVal14,
			holdVal15,  holdVal16,  holdVal17,  holdVal18,  holdVal19, holdVal20;

			holdVal1 = minVal1 + ((maxVal1 - minVal1) * (val1 + modVal1).max(0).min(1));
			holdVal2 = minVal2 + ((maxVal2 - minVal2) * (val2 + modVal2).max(0).min(1));
			holdVal3 = minVal3 + ((maxVal3 - minVal3) * (val3 + modVal3).max(0).min(1));
			holdVal4 = minVal4 + ((maxVal4 - minVal4) * (val4 + modVal4).max(0).min(1));
			holdVal5 = minVal5 + ((maxVal5 - minVal5) * (val5 + modVal5).max(0).min(1));
			holdVal6 = minVal6 + ((maxVal6 - minVal6) * (val6 + modVal6).max(0).min(1));
			holdVal7 = minVal7 + ((maxVal7 - minVal7) * (val7 + modVal7).max(0).min(1));
			holdVal8 = minVal8 + ((maxVal8 - minVal8) * (val8 + modVal8).max(0).min(1));
			holdVal9 = minVal9 + ((maxVal9 - minVal9) * (val9 + modVal9).max(0).min(1));
			holdVal10 = minVal10 + ((maxVal10 - minVal10) * (val10 + modVal10).max(0).min(1));
			holdVal11 = minVal11 + ((maxVal11 - minVal11) * (val11 + modVal11).max(0).min(1));
			holdVal12 = minVal12 + ((maxVal12 - minVal12) * (val12 + modVal12).max(0).min(1));
			holdVal13 = minVal13 + ((maxVal13 - minVal13) * (val13 + modVal13).max(0).min(1));
			holdVal14 = minVal14 + ((maxVal14 - minVal14) * (val14 + modVal14).max(0).min(1));
			holdVal15 = minVal15 + ((maxVal15 - minVal15) * (val15 + modVal15).max(0).min(1));
			holdVal16 = minVal16 + ((maxVal16 - minVal16) * (val16 + modVal16).max(0).min(1));
			holdVal17 = minVal17 + ((maxVal17 - minVal17) * (val17 + modVal17).max(0).min(1));
			holdVal18 = minVal18 + ((maxVal18 - minVal18) * (val18 + modVal18).max(0).min(1));
			holdVal19 = minVal19 + ((maxVal19 - minVal19) * (val19 + modVal19).max(0).min(1));
			holdVal20 = minVal20 + ((maxVal20 - minVal20) * (val20 + modVal20).max(0).min(1));

			imp = LFPulse.kr(maxFPS, 0, 0.999) * active;

			// testing
			backupImp = Impulse.kr(1);
			//	   backupImp = 0;

			trig1 = Trig.kr(imp * HPZ1.kr(holdVal1).abs, 0.02);
			trig2 = Trig.kr(imp * HPZ1.kr(holdVal2).abs, 0.02);
			trig3 = Trig.kr(imp * HPZ1.kr(holdVal3).abs, 0.02);
			trig4 = Trig.kr(imp * HPZ1.kr(holdVal4).abs, 0.02);
			trig5 = Trig.kr(imp * HPZ1.kr(holdVal5).abs, 0.02);
			trig6 = Trig.kr(imp * HPZ1.kr(holdVal6).abs, 0.02);
			trig7 = Trig.kr(imp * HPZ1.kr(holdVal7).abs, 0.02);
			trig8 = Trig.kr(imp * HPZ1.kr(holdVal8).abs, 0.02);
			trig9 = Trig.kr(imp * HPZ1.kr(holdVal9).abs, 0.02);
			trig10 = Trig.kr(imp * HPZ1.kr(holdVal10).abs, 0.02);
			trig11 = Trig.kr(imp * HPZ1.kr(holdVal11).abs, 0.02);
			trig12 = Trig.kr(imp * HPZ1.kr(holdVal12).abs, 0.02);
			trig13 = Trig.kr(imp * HPZ1.kr(holdVal13).abs, 0.02);
			trig14 = Trig.kr(imp * HPZ1.kr(holdVal14).abs, 0.02);
			trig15 = Trig.kr(imp * HPZ1.kr(holdVal15).abs, 0.02);
			trig16 = Trig.kr(imp * HPZ1.kr(holdVal16).abs, 0.02);
			trig17 = Trig.kr(imp * HPZ1.kr(holdVal17).abs, 0.02);
			trig18 = Trig.kr(imp * HPZ1.kr(holdVal18).abs, 0.02);
			trig19 = Trig.kr(imp * HPZ1.kr(holdVal19).abs, 0.02);
			trig20 = Trig.kr(imp * HPZ1.kr(holdVal20).abs, 0.02);

			SendTrig.kr(backupImp + trig1, arrSendTrigIDs.at(0), holdVal1);
			SendTrig.kr(backupImp + trig2, arrSendTrigIDs.at(1), holdVal2);
			SendTrig.kr(backupImp + trig3, arrSendTrigIDs.at(2), holdVal3);
			SendTrig.kr(backupImp + trig4, arrSendTrigIDs.at(3), holdVal4);
			SendTrig.kr(backupImp + trig5, arrSendTrigIDs.at(4), holdVal5);
			SendTrig.kr(backupImp + trig6, arrSendTrigIDs.at(5), holdVal6);
			SendTrig.kr(backupImp + trig7, arrSendTrigIDs.at(6), holdVal7);
			SendTrig.kr(backupImp + trig8, arrSendTrigIDs.at(7), holdVal8);
			SendTrig.kr(backupImp + trig9, arrSendTrigIDs.at(8), holdVal9);
			SendTrig.kr(backupImp + trig10, arrSendTrigIDs.at(9), holdVal10);
			SendTrig.kr(backupImp + trig11, arrSendTrigIDs.at(10), holdVal11);
			SendTrig.kr(backupImp + trig12, arrSendTrigIDs.at(11), holdVal12);
			SendTrig.kr(backupImp + trig13, arrSendTrigIDs.at(12), holdVal13);
			SendTrig.kr(backupImp + trig14, arrSendTrigIDs.at(13), holdVal14);
			SendTrig.kr(backupImp + trig15, arrSendTrigIDs.at(14), holdVal15);
			SendTrig.kr(backupImp + trig16, arrSendTrigIDs.at(15), holdVal16);
			SendTrig.kr(backupImp + trig17, arrSendTrigIDs.at(16), holdVal17);
			SendTrig.kr(backupImp + trig18, arrSendTrigIDs.at(17), holdVal18);
			SendTrig.kr(backupImp + trig19, arrSendTrigIDs.at(18), holdVal19);
			SendTrig.kr(backupImp + trig20, arrSendTrigIDs.at(19), holdVal20);

			// Note this synth doesn't need to write to the output bus
		};
		holdScreenSizes = [ [320, 240], [640, 480], [720, 480], [800, 500], [800, 600], [1024, 640], [1024, 768],
			[1152, 720], [1280, 800], [1440, 900]
		];
		holdScreenSizeTexts = holdScreenSizes.collect({arg item, i; item.at(0).asString + "X" + item.at(1).asString});
		holdSSPresetActions = holdScreenSizes.collect({arg item, i;
			{	this.setSynthArgSpec("i_screenWidth", item.at(0));
				this.setSynthArgSpec("i_screenHeight", item.at(1));
				screenWidth = item.at(0);
				screenHeight = item.at(1);
				this.resetScreenSize;
				this.oscActivate;
			}
		});

		holdBorderSizes = [ [0, 0], [2, 2], [5, 5], [10, 10], [20, 20], [30, 30], [50, 50] , [100, 100] , [200, 200] ];
		holdBorderSizeTexts = holdBorderSizes.collect({arg item, i; item.at(0).asString + "X" + item.at(1).asString});
		holdBorderSizePresetActions = holdBorderSizes.collect({arg item, i;
			{	this.setSynthArgSpec("i_borderX", item.at(0));
				this.setSynthArgSpec("i_borderY", item.at(1));
				this.resetScreenSize;
				this.oscActivate;
			}
		});
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Show Video Screen", {this.rebuildScreen;}],
			["commandAction", "Close Video Screen", {{if (holdWindow.notNil) {holdWindow.close};}.defer}],
			["commandAction", "Window To Front", {this.windowToFront;}],
			["commandAction", "Sample Current Window W & H", {
				var holdRect;
				if (holdWindow.notNil, {
					holdRect = holdWindow.bounds;
					screenWidth = holdRect.width;
					screenHeight = holdRect.height;
					this.setSynthValue("i_screenWidth", screenWidth);
					this.setSynthValue("i_screenHeight", screenHeight);
					system.flagGuiIfModDisplay(this);
				});
			}],
			["commandAction", "Default Window W & H", {
				screenWidth = 640;
				screenHeight = 480;
				this.setSynthValue("i_screenWidth", screenWidth);
				this.setSynthValue("i_screenHeight", screenHeight);
				this.resetScreenSize;
				this.oscActivate;
				system.flagGuiIfModDisplay(this);
			}],
			["commandAction", "Sample Current Window Position", {
				var holdRect;
				if (holdWindow.notNil, {
					holdRect = holdWindow.bounds;
					screenPosX = holdRect.left;
					screenPosY = holdRect.top;
					this.setSynthValue("i_posX", screenPosX);
					this.setSynthValue("i_posY", screenPosY);
					system.flagGuiIfModDisplay(this);
				});
			}],
			["commandAction", "Default Window Position", {
				screenPosX = 0;
				screenPosY = 60;
				this.setSynthValue("i_posX", screenPosX);
				this.setSynthValue("i_posY", screenPosY);
				{if (holdWindow.notNil) {holdWindow.close}}.defer;
				{this.showScreen(posX, posY);}.defer(0.1);
				system.flagGuiIfModDisplay(this);
			}],
			["EZNumber", "Window width", ControlSpec(0, 10000, 'lin', 1, 0), "i_screenWidth",
				{arg view; screenWidth = view.value; this.resetScreenSize; this.oscActivate;}],
			["EZNumber", "Wind. height", ControlSpec(0, 10000, 'lin', 1, 0), "i_screenHeight",
				{arg view; screenHeight = view.value; this.resetScreenSize; this.oscActivate;}],
			["EZNumber", "Wind. pos X", ControlSpec(0, 10000, 'lin', 1, 0), "i_posX",
				{arg view; screenPosX = view.value; this.resetScreenSize; this.oscActivate;}],
			["EZNumber", "Wind. pos Y", ControlSpec(0, 10000, 'lin', 1, 0), "i_posY",
				{arg view; screenPosY = view.value; this.resetScreenSize; this.oscActivate;}],

			["EZNumber", "Border X", ControlSpec(0, 2000, 'lin', 1, 0), "i_borderX",
				{arg view; this.resetScreenSize; this.oscActivate;}],
			["EZNumber", "Border Y", ControlSpec(0, 2000, 'lin', 1, 0), "i_borderY",
				{arg view; this.resetScreenSize; this.oscActivate;}],
			["EZNumber", "Max FPS", ControlSpec(2, 60, 'lin', 1, 0), "maxFPS",
				{arg view; this.setFPS(view.value);}],
			["EZslider", "Hue", ControlSpec(0,1), "i_backgroundH", {this.setBackground;}],
			["EZslider", "Saturation", ControlSpec(0,1), "i_backgroundS", {this.setBackground;}],
			["EZslider", "Brightness", ControlSpec(0,1), "i_backgroundV", {this.setBackground;}],
			["TXCheckBox", "Show window controls", "i_winControls", {this.rebuildScreen; }, 240],
			["TXCheckBox", "Window Always On Top", "i_windowOnTop", {this.rebuildScreen; }, 240],
			["TXCheckBox", "Clear screen before drawing", "i_clearOnRefresh", {this.rebuildScreen; }, 240],

			["TXTextBox", "Name", "name1", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 1", nil.asSpec, "val1", "minVal1", "maxVal1"],
			["TXTextBox", "Name", "name2", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 2", nil.asSpec, "val2", "minVal2", "maxVal2"],
			["TXTextBox", "Name", "name3", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 3", nil.asSpec, "val3", "minVal3", "maxVal3"],
			["TXTextBox", "Name", "name4", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 4", nil.asSpec, "val4", "minVal4", "maxVal4"],
			["TXTextBox", "Name", "name5", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 5", nil.asSpec, "val5", "minVal5", "maxVal5"],
			["TXTextBox", "Name", "name6", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 6", nil.asSpec, "val6", "minVal6", "maxVal6"],
			["TXTextBox", "Name", "name7", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 7", nil.asSpec, "val7", "minVal7", "maxVal7"],
			["TXTextBox", "Name", "name8", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 8", nil.asSpec, "val8", "minVal8", "maxVal8"],
			["TXTextBox", "Name", "name9", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 9", nil.asSpec, "val9", "minVal9", "maxVal9"],
			["TXTextBox", "Name", "name10", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 10", nil.asSpec, "val10", "minVal10", "maxVal10"],
			["TXTextBox", "name", "name11", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 11", nil.asSpec, "val11", "minVal11", "maxVal11"],
			["TXTextBox", "name", "name12", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 12", nil.asSpec, "val12", "minVal12", "maxVal12"],
			["TXTextBox", "name", "name13", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 13", nil.asSpec, "val13", "minVal13", "maxVal13"],
			["TXTextBox", "name", "name14", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 14", nil.asSpec, "val14", "minVal14", "maxVal14"],
			["TXTextBox", "name", "name15", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 15", nil.asSpec, "val15", "minVal15", "maxVal15"],
			["TXTextBox", "name", "name16", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 16", nil.asSpec, "val16", "minVal16", "maxVal16"],
			["TXTextBox", "name", "name17", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 17", nil.asSpec, "val17", "minVal17", "maxVal17"],
			["TXTextBox", "name", "name18", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 18", nil.asSpec, "val18", "minVal18", "maxVal18"],
			["TXTextBox", "name", "name19", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 19", nil.asSpec, "val19", "minVal19", "maxVal19"],
			["TXTextBox", "name", "name20", nil, 140, 60],
			["TXMinMaxSliderSplit", "Value 20", nil.asSpec, "val20", "minVal20", "maxVal20"],
		]);

		// initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
		this.resetScreenSize;
		this.oscActivate;
	}

	buildGuiSpecArray {
		guiSpecArray = [
			["SpacerLine", 1],
			["ActionButton", "Window", {displayOption = "showWindow";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showWindow")],
			["Spacer", 3],
			["ActionButton", "Code", {displayOption = "showCode";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showCode")],
			["Spacer", 3],
			["ActionButton", "Controls 1-5", {displayOption = "show1";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "show1")],
			["Spacer", 3],
			["ActionButton", "Controls 6-10", {displayOption = "show2";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "show2")],
			["Spacer", 3],
			["ActionButton", "Controls 11-15", {displayOption = "show3";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "show3")],
			["Spacer", 3],
			["ActionButton", "Controls 16-20", {displayOption = "show4";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "show4")],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showWindow", {
			guiSpecArray = guiSpecArray ++[

				["ActionButtonBig", "Show Window", {this.rebuildScreen;}, 120, nil, TXColour.sysGuiCol1],
				["ActionButtonBig", "Close Window",
					{{if (holdWindow.notNil) {holdWindow.close};}.defer}, 120, nil, TXColour.sysDeleteCol],
				["ActionButtonBig", "Window To Front", {this.windowToFront;}, 120, nil, TXColour.sysGuiCol2],
				["DividingLine"],
				["SpacerLine", 2],
				["EZNumber", "Window width", ControlSpec(0, 10000, 'lin', 1, 0), "i_screenWidth",
					{arg view; screenWidth = view.value; this.resetScreenSize; this.oscActivate;}],
				["EZNumber", "Wind. height", ControlSpec(0, 10000, 'lin', 1, 0), "i_screenHeight",
					{arg view; screenHeight = view.value; this.resetScreenSize; this.oscActivate;}],
				["TXPresetPopup", "Presets", holdScreenSizeTexts, holdSSPresetActions, 110],
				["SpacerLine", 2],
				["ActionButton", "Sample Current W & H", {
					var holdRect;
					if (holdWindow.notNil, {
						holdRect = holdWindow.bounds;
						screenWidth = holdRect.width;
						screenHeight = holdRect.height;
						this.setSynthValue("i_screenWidth", screenWidth);
						this.setSynthValue("i_screenHeight", screenHeight);
						system.flagGuiIfModDisplay(this);
					});
				}, 160],
				["ActionButton", "Default Window W & H", {arg view;
					screenWidth = 640;
					screenHeight = 480;
					this.setSynthValue("i_screenWidth", screenWidth);
					this.setSynthValue("i_screenHeight", screenHeight);
					this.resetScreenSize;
					this.oscActivate;
					system.flagGuiIfModDisplay(this);
				}, 160, TXColor.white, TXColor.sysGuiCol2],
				["DividingLine"],
				["SpacerLine", 2],
				["EZNumber", "Wind. pos X", ControlSpec(0, 10000, 'lin', 1, 0), "i_posX",
					{arg view; screenPosX = view.value; this.resetScreenSize; this.oscActivate;}],
				["EZNumber", "Wind. pos Y", ControlSpec(0, 10000, 'lin', 1, 0), "i_posY",
					{arg view; screenPosY = view.value; this.resetScreenSize; this.oscActivate;}],
				["SpacerLine", 2],
				["ActionButton", "Sample Current Position", {
					var holdRect;
					if (holdWindow.notNil, {
						holdRect = holdWindow.bounds;
						screenPosX = holdRect.left;
						screenPosY = holdRect.top;
						this.setSynthValue("i_posX", screenPosX);
						this.setSynthValue("i_posY", screenPosY);
						system.flagGuiIfModDisplay(this);
					});
				}, 160],
				["ActionButton", "Default Window Position", {
					screenPosX = 0;
					screenPosY = 60;
					this.setSynthValue("i_posX", screenPosX);
					this.setSynthValue("i_posY", screenPosY);
					{if (holdWindow.notNil) {holdWindow.close}}.defer;
					{this.showScreen(posX, posY);}.defer(0.1);
					system.flagGuiIfModDisplay(this);
				}, 160, TXColor.white, TXColor.sysGuiCol2],
				["DividingLine"],
				["SpacerLine", 2],
				["EZNumber", "Border X", ControlSpec(0, 2000, 'lin', 1, 0), "i_borderX",
					{arg view; this.resetScreenSize; this.oscActivate;}],
				["EZNumber", "Border Y", ControlSpec(0, 2000, 'lin', 1, 0), "i_borderY",
					{arg view; this.resetScreenSize; this.oscActivate;}],
				["TXPresetPopup", "Presets", holdBorderSizeTexts, holdBorderSizePresetActions, 110],
				["DividingLine"],
				["SpacerLine", 2],
				["EZNumber", "Max FPS", ControlSpec(2, 60, 'lin', 1, 0), "maxFPS",
					{arg view; this.setFPS(view.value);}],
				["DividingLine"],
				["SpacerLine", 2],
				["TextBarLeft", "Background Colour", 150],
				["NextLine"],
				["EZslider", "Hue", ControlSpec(0,1), "i_backgroundH", {this.setBackground;}],
				["EZslider", "Saturation", ControlSpec(0,1), "i_backgroundS", {this.setBackground;}],
				["EZslider", "Brightness", ControlSpec(0,1), "i_backgroundV", {this.setBackground;}],
				["DividingLine"],
				["SpacerLine", 2],
				["TXCheckBox", "Show window controls", "i_winControls", {this.rebuildScreen; }, 220],
				["Spacer", 10],
				["TXCheckBox", "Window Always On Top", "i_windowOnTop", {this.rebuildScreen; }, 220],
				["SpacerLine", 2],
				["TXCheckBox", "Clear screen before drawing", "i_clearOnRefresh", {this.rebuildScreen; }, 220],
			];
		});

		if (displayOption == "showCode", {
			guiSpecArray = guiSpecArray ++[
				["TextViewDisplay", "Coding Notes: Enter Supercollider 3 code in the window below. The code needs to be a function which uses Pen to draw to the screen. The function will be passed the arguments: animationView - the view itself, & arrPassVals - array of 20 modulatable sliders.
Use the Evaulate text button to evaulate the code.", 400, 70, "Notes"],
				["TextViewCompile", userFunctionString, {arg argText; this.evaluate(argText);}, 400, 330],
			];
		});

		if (displayOption == "show1", {
			guiSpecArray = guiSpecArray ++[
				["TXTextBox", "Name", "name1", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 1", nil.asSpec, "val1", "minVal1", "maxVal1"],
				["NextLine"],
				["TXTextBox", "Name", "name2", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 2", nil.asSpec, "val2", "minVal2", "maxVal2"],
				["NextLine"],
				["TXTextBox", "Name", "name3", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 3", nil.asSpec, "val3", "minVal3", "maxVal3"],
				["NextLine"],
				["TXTextBox", "Name", "name4", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 4", nil.asSpec, "val4", "minVal4", "maxVal4"],
				["NextLine"],
				["TXTextBox", "Name", "name5", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 5", nil.asSpec, "val5", "minVal5", "maxVal5"],
			];
		});
		if (displayOption == "show2", {
			guiSpecArray = guiSpecArray ++[
				["TXTextBox", "Name", "name6", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 6", nil.asSpec, "val6", "minVal6", "maxVal6"],
				["NextLine"],
				["TXTextBox", "Name", "name7", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 7", nil.asSpec, "val7", "minVal7", "maxVal7"],
				["NextLine"],
				["TXTextBox", "Name", "name8", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 8", nil.asSpec, "val8", "minVal8", "maxVal8"],
				["NextLine"],
				["TXTextBox", "Name", "name9", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 9", nil.asSpec, "val9", "minVal9", "maxVal9"],
				["NextLine"],
				["TXTextBox", "Name", "name10", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 10", nil.asSpec, "val10", "minVal10", "maxVal10"],
			];
		});
		if (displayOption == "show3", {
			guiSpecArray = guiSpecArray ++[
				["TXTextBox", "name", "name11", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 11", nil.asSpec, "val11", "minVal11", "maxVal11"],
				["NextLine"],
				["TXTextBox", "name", "name12", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 12", nil.asSpec, "val12", "minVal12", "maxVal12"],
				["NextLine"],
				["TXTextBox", "name", "name13", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 13", nil.asSpec, "val13", "minVal13", "maxVal13"],
				["NextLine"],
				["TXTextBox", "name", "name14", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 14", nil.asSpec, "val14", "minVal14", "maxVal14"],
				["NextLine"],
				["TXTextBox", "name", "name15", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 15", nil.asSpec, "val15", "minVal15", "maxVal15"],
			];
		});
		if (displayOption == "show4", {
			guiSpecArray = guiSpecArray ++[
				["TXTextBox", "name", "name16", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 16", nil.asSpec, "val16", "minVal16", "maxVal16"],
				["NextLine"],
				["TXTextBox", "name", "name17", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 17", nil.asSpec, "val17", "minVal17", "maxVal17"],
				["NextLine"],
				["TXTextBox", "name", "name18", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 18", nil.asSpec, "val18", "minVal18", "maxVal18"],
				["NextLine"],
				["TXTextBox", "name", "name19", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 19", nil.asSpec, "val19", "minVal19", "maxVal19"],
				["NextLine"],
				["TXTextBox", "name", "name20", nil, 140, 60],
				["NextLine"],
				["TXMinMaxSliderSplit", "Value 20", nil.asSpec, "val20", "minVal20", "maxVal20"],
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

	showScreen {arg posX, posY;
		var holdFunction, holdWinBorder, borderX, borderY;
		{
			//	check if window exists
			if (holdWindow.isNil) {
				// make window
				if (this.getSynthArgSpec("i_winControls") == 1,
					{holdWinBorder = true;},
					{holdWinBorder = false;
				});

				holdWindow= Window(" ", Rect(posX ? screenPosX, posY ? screenPosY, screenWidth, screenHeight),
					border: holdWinBorder).front;
				holdWindow.onClose_({
					holdWindow = nil;
					holdView = nil;
				});
				if (this.getSynthArgSpec("i_windowOnTop") == 1,
					{holdWindow.alwaysOnTop = true;},
					{holdWindow.alwaysOnTop = false;
				});
				// make Button
				Button(holdWindow, Rect(2, 2, 30, 20))
				.states_([["<-", Color.white, Color.grey(0.1)]])
				.action_({system.windowToFront});
				// make background
				StaticText(holdWindow, Rect(0 ,0, 1440, 900)).background_(TXColor.black);
				// make view
				// border settings
				borderX = this.getSynthArgSpec("i_borderX");
				borderY = this.getSynthArgSpec("i_borderY");
				holdView = UserView(holdWindow,
					Rect(borderX ,borderY, (screenWidth-(borderX*2)).max(0), (screenHeight-(borderY*2))));
				holdView.animate = true;
				holdView.resize = 5;
				// make background
				this.setBackground;
				if (this.getSynthArgSpec("i_clearOnRefresh") == 1, {
					holdView.clearOnRefresh = true;
				},{
					holdView.clearOnRefresh = false;
				});
				holdFunction = userFunctionString.compile.value;
				holdView.drawFunc = {arg animationView;
					Pen.use{
						holdFunction.value(animationView, arrPassVals);
					};
				};
			}{
				// if window exists bring to front
				holdWindow.front;
			};
		}.defer;
	}

	rebuildScreen {
		var posX, posY;
		{	//	check if window exists
			if (holdWindow.notNil) {
				posX = holdWindow.bounds.left;
				posY = holdWindow.bounds.top;
				holdWindow.close;
			};
		}.defer;
		{
			this.showScreen(posX, posY);
		}.defer(0.1);
	}

	windowToFront {
		{	//	check if window exists
			if (holdWindow.notNil,  {
				holdWindow.front;
			},{
				this.showScreen;
			});
		}.defer;
	}

	setBackground {
		if (holdView.notNil and: holdView.notClosed, {
			holdView.background = Color.hsv(
				this.getSynthArgSpec("i_backgroundH"),
				this.getSynthArgSpec("i_backgroundS"),
				this.getSynthArgSpec("i_backgroundV"),
				1
			);
		});
	}

	resetScreenSize {
		screenWidth = this.getSynthArgSpec("i_screenWidth");
		screenHeight = this.getSynthArgSpec("i_screenHeight");
		this.rebuildScreen;
	}

	oscActivate {
		//	remove any previous responders and add new
		this.oscDeactivate;
		arrResps.do({arg item, i;
			item = OSCFunc({arg msg, time, addr,  recvPort;
				if (msg[2] == arrSendTrigIDs.at(i),{
					arrPassVals[i] = msg[3];
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

	evaluate {arg argText;
		var compileResult;
		compileResult = argText.compile;
		if (compileResult.isNil, {
			TXInfoScreen.new("ERROR: code will not compile - see post window ");
		},{
			userFunctionString = argText;
			this.rebuildScreen;
		});
	}

	extraSaveData {
		^[screenWidth, screenHeight, screenPosX, screenPosY, userFunctionString];
	}

	loadExtraData {arg argData;  // override default method
		screenWidth = argData.at(0);
		screenHeight = argData.at(1);
		screenPosX = argData.at(2);
		screenPosY = argData.at(3);
		userFunctionString = argData.at(4);
		this.buildGuiSpecArray;
		this.oscActivate;
		{if (holdWindow.notNil) {holdWindow.close};}.defer;
		{this.rebuildScreen;}.defer(0.1);
	}


	deleteModuleExtraActions {
		//	remove responders
		this.oscDeactivate;
		if (holdWindow.notNil) {
			// if window exists close it
			holdWindow.close;
		};
	}

	setFPS {arg argFPS;
		var newFPS;
		newFPS = argFPS.max(1).min(60);
		thisProcess.setDeferredTaskInterval(1/newFPS);
	}

	runAction {
		super.runAction;
		if (holdView.notNil, {
			holdView.drawingEnabled = true;
		});
	}

	pauseAction {
		super.pauseAction;
		if (holdView.notNil, {
			holdView.drawingEnabled = false;
		});
	}

}


