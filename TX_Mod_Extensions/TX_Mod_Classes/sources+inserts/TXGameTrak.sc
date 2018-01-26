// Copyright (C) 2017  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXGameTrak : TXModuleBase {

	classvar <>classData;
	var data;
	var arrActions;
	var	displayOption, deviceStatusView;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Gametrak";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [];
		classData.noOutChannels = 13;
		classData.arrOutBusSpecs = [
			["Left Angle X", [0]],
			["Left Angle Y", [1]],
			["Left Extend Z", [2]],
			["Right Angle X", [3]],
			["Right Angle Y", [4]],
			["Right Extend Z", [5]],
			["Foot Switch", [6]],
			["Left Pos X", [7]],
			["Left Pos Y", [8]],
			["Left Pos Z", [9]],
			["Right Pos X", [10]],
			["Right Pos Y", [11]],
			["Right Pos Z", [12]],

		];
		classData.guiWidth = 700;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		data = ();
		data.leftAngleXMin = 0;
		data.leftAngleXMax = 1;
		data.leftAngleYMin = 0;
		data.leftAngleYMax = 1;
		data.leftExtendZMin = 0;
		data.leftExtendZMax = 1;
		data.rightAngleXMin = 0;
		data.rightAngleXMax = 1;
		data.rightAngleYMin = 0;
		data.rightAngleYMax = 1;
		data.rightExtendZMin = 0;
		data.rightExtendZMax = 1;
		data.footSwitch = 0;
		data.calibrate = 0;
		data.calibrateFS = 0;
		data.leftPosXMin = 0;
		data.leftPosXMax = 1;
		data.leftPosYMin = 0;
		data.leftPosYMax = 1;
		data.leftPosZMin = 0;
		data.leftPosZMax = 1;
		data.rightPosXMin = 0;
		data.rightPosXMax = 1;
		data.rightPosYMin = 0;
		data.rightPosYMax = 1;
		data.rightPosZMin = 0;
		data.rightPosZMax = 1;
		//	set  class specific instance variables
		arrActions = [99,0,0,0,0,0,0, nil].dup(4);
		displayOption = "showCalibration";
		arrSynthArgSpecs = [
			["out", 0, 0], // Dummy
			["leftAngleXMin", 0, 0],
			["leftAngleXMax", 1, 0],
			["leftAngleYMin", 0, 0],
			["leftAngleYMax", 1, 0],
			["leftExtendZMin", 0, 0],
			["leftExtendZMax", 1, 0],
			["rightAngleXMin", 0, 0],
			["rightAngleXMax", 1, 0],
			["rightAngleYMin", 0, 0],
			["rightAngleYMax", 1, 0],
			["rightExtendZMin", 0, 0],
			["rightExtendZMax", 1, 0],
			["calibrate", 0, 0],
			["calibrateFS", 0, 0],
			["leftPosXMin", 0, 0],
			["leftPosXMax", 1, 0],
			["leftPosYMin", 0, 0],
			["leftPosYMax", 1, 0],
			["leftPosZMin", 0, 0],
			["leftPosZMax", 1, 0],
			["rightPosXMin", 0, 0],
			["rightPosXMax", 1, 0],
			["rightPosYMin", 0, 0],
			["rightPosYMax", 1, 0],
			["rightPosZMin", 0, 0],
			["rightPosZMax", 1, 0],
		];
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([

			// ["TXCheckBox", "Calibrate", "calibrate",
			// {this.setCalibration; }, 80, 20],
			// ["TXCheckBox", "Use FootSwitch to Calibrate", "calibrateFS",
			// {this.setCalibrationFS; }, 180, 20],
			// ["SpacerLine", 4],
			// ["TXRangeSlider", "L X", ControlSpec(0, 1), "leftAngleXMin", "leftAngleXMax",
			// {this.updateMinMaxVal("leftAngleXMin", "leftAngleXMax")}],
			// ["TXRangeSlider", "L Y", ControlSpec(0, 1), "leftAngleYMin", "leftAngleYMax",
			// {this.updateMinMaxVal("leftAngleYMin", "leftAngleYMax")}],
			// ["TXRangeSlider", "L Z", ControlSpec(0, 1), "leftExtendZMin", "leftExtendZMax",
			// {this.updateMinMaxVal("leftExtendZMin", "leftExtendZMax")}],
			// ["SpacerLine", 4],
			// ["TXRangeSlider", "R X", ControlSpec(0, 1), "rightAngleXMin", "rightAngleXMax",
			// {this.updateMinMaxVal("rightAngleXMin", "rightAngleXMax")}],
			// ["TXRangeSlider", "R Y", ControlSpec(0, 1), "rightAngleYMin", "rightAngleYMax",
			// {this.updateMinMaxVal("rightAngleYMin", "rightAngleYMax")}],
			// ["TXRangeSlider", "R Z", ControlSpec(0, 1), "rightExtendZMin", "rightExtendZMax",
			// {this.updateMinMaxVal("rightExtendZMin", "rightExtendZMax")}],
			//
			// TO BE REPLACED








		]);
		data.deviceStatus = " Device Not Found";
		//	use base class initialise
		this.baseInit(this, argInstName);
		this.resetAllOutputs;
		this.reconnect;
	}

	// gui methods

	buildGuiSpecArray {
		guiSpecArray = [
			["TXStaticText", "Device status", {data.deviceStatus}, {arg view; deviceStatusView = view.textView}, 280],
			["Spacer", 3],
			["ActionButton", "Reconnect", { this.reconnect; }, 80, TXColor.white, TXColor.sysGuiCol2],
			["Spacer", 30],
			["ActionButton", "Reset All Outputs", {this.resetAllOutputs;}, 120, TXColour.white, TXColour.sysDeleteCol],
			["SpacerLine", 6],
			["ActionButton", "Active Ranges", {displayOption = "showCalibration";
				this.buildGuiSpecArray; system.showView;}, 90,
			TXColor.white, this.getButtonColour(displayOption == "showCalibration")],
			["Spacer", 3],
			["ActionButton", "Foot Switch Down Actions", {displayOption = "showFSDownActions";
				this.buildGuiSpecArray; system.showView;}, 150,
			TXColor.white, this.getButtonColour(displayOption == "showFSDownActions")],
			["Spacer", 3],
			["ActionButton", "Foot Switch Up Actions", {displayOption = "showFSUpActions";
				this.buildGuiSpecArray; system.showView;}, 150,
			TXColor.white, this.getButtonColour(displayOption == "showFSUpActions")],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showCalibration", {
			guiSpecArray = guiSpecArray ++[
				["TXCheckBox", "Calibrate", "calibrate",
					{arg view; this.setCalibration;}, 80, 20],
				["Spacer", 3],
				["TXCheckBox", "Use FootSwitch to Calibrate", "calibrateFS",
					{this.setCalibrationFS; }, 180, 20],
				["Spacer", 3],
				["ActionButton", "Calibrate Off", {
					this.setCalibrationOff;
					system.flagGuiIfModDisplay(this);
				}, 90, TXColour.white, TXColour.sysGuiCol2],
				["Spacer", 30],
				["ActionButton", "Restore Maximum Ranges", {
					this.setDefaultRanges;
					system.flagGuiIfModDisplay(this);
				}, 160, TXColour.white, TXColour.sysDeleteCol],
				["SpacerLine", 6],
				["TXRangeSlider", "Left Angle X", ControlSpec(0, 1), "leftAngleXMin", "leftAngleXMax",
					{this.updateMinMaxVal("leftAngleXMin", "leftAngleXMax")}],
				["SpacerLine", 1],
				["TXRangeSlider", "Left Angle Y", ControlSpec(0, 1), "leftAngleYMin", "leftAngleYMax",
					{this.updateMinMaxVal("leftAngleYMin", "leftAngleYMax")}],
				["SpacerLine", 1],
				["TXRangeSlider", "Left Extend Z", ControlSpec(0, 1), "leftExtendZMin", "leftExtendZMax",
					{this.updateMinMaxVal("leftExtendZMin", "leftExtendZMax")}],
				["SpacerLine", 10],
				["TXRangeSlider", "Right Angle X", ControlSpec(0, 1), "rightAngleXMin", "rightAngleXMax",
					{this.updateMinMaxVal("rightAngleXMin", "rightAngleXMax")}],
				["SpacerLine", 2],
				["TXRangeSlider", "Right Angle Y", ControlSpec(0, 1), "rightAngleYMin", "rightAngleYMax",
					{this.updateMinMaxVal("rightAngleYMin", "rightAngleYMax")}],
				["SpacerLine", 2],
				["TXRangeSlider", "Right Extend Z", ControlSpec(0, 1), "rightExtendZMin", "rightExtendZMax",
					{this.updateMinMaxVal("rightExtendZMin", "rightExtendZMax")}],
				["DividingLine"],
				["SpacerLine", 6],
				["TXRangeSlider", "Left Pos X", ControlSpec(0, 1), "leftPosXMin", "leftPosXMax",
					{this.updateMinMaxVal("leftPosXMin", "leftPosXMax")}],
				["SpacerLine", 1],
				["TXRangeSlider", "Left Pos Y", ControlSpec(0, 1), "leftPosYMin", "leftPosYMax",
					{this.updateMinMaxVal("leftPosYMin", "leftPosYMax")}],
				["SpacerLine", 1],
				["TXRangeSlider", "Left Pos Z", ControlSpec(0, 1), "leftPosZMin", "leftPosZMax",
					{this.updateMinMaxVal("leftPosZMin", "leftPosZMax")}],
				["SpacerLine", 10],
				["TXRangeSlider", "Right Pos X", ControlSpec(0, 1), "rightPosXMin", "rightPosXMax",
					{this.updateMinMaxVal("rightPosXMin", "rightPosXMax")}],
				["SpacerLine", 2],
				["TXRangeSlider", "Right Pos Y", ControlSpec(0, 1), "rightPosYMin", "rightPosYMax",
					{this.updateMinMaxVal("rightPosYMin", "rightPosYMax")}],
				["SpacerLine", 2],
				["TXRangeSlider", "Right Pos Z", ControlSpec(0, 1), "rightPosZMin", "rightPosZMax",
					{this.updateMinMaxVal("rightPosZMin", "rightPosZMax")}],
			];
		});
		if (displayOption == "showFSDownActions", {
			guiSpecArray = guiSpecArray ++[
				["TXActionView", arrActions, 0],
				["DividingLine"],
				["SpacerLine", 4],
				["TXActionView", arrActions, 1],
			];
		});
		if (displayOption == "showFSUpActions", {
			guiSpecArray = guiSpecArray ++[
				["TXActionView", arrActions, 2],
				["DividingLine"],
				["SpacerLine", 4],
				["TXActionView", arrActions, 3],
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

	performActions {arg direction = 'down';
		var holdInds;
		if (direction == 'down', {
			holdInds = [0, 1];
		},{
			holdInds = [2, 3];
		});
		arrActions[holdInds].do({ arg item, i;
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
	}	// end of performActions


	// HID methods

	hidActivate {
		// open device
		this.openGameTrak;
		// if open
		if (classData.gameTrak.notNil and: {classData.gameTrak.isOpen}, {
			classData.gameTrak.elements.at(0).action = { |value,element|
				var outVal;
				// X:
				data.leftAngleX = value;
				this.calculatePosition('left');
				if (data.calibrate == 1 or: {(data.calibrateFS == 1) and: (data.footSwitch == 1)}, {
					this.calibrateMinMax("leftAngleXMin", "leftAngleXMax", value);
				});
				if ( outBus.class == Bus, {
					outVal = value.linlin(data.leftAngleXMin, data.leftAngleXMax, 0, 1);
					outBus.set(outVal);
				});
			};
			classData.gameTrak.elements.at(1).action = { |value,element|
				var outVal;
				// Y:
				data.leftAngleY = value;
				this.calculatePosition('left');
				if (data.calibrate == 1 or: {(data.calibrateFS == 1) and: (data.footSwitch == 1)}, {
					this.calibrateMinMax("leftAngleYMin", "leftAngleYMax", value);
				});
				if ( outBus.class == Bus, {
					outVal = value.linlin(data.leftAngleYMin, data.leftAngleYMax, 0, 1);
					outBus.setAt(1, outVal);
				});
			};
			classData.gameTrak.elements.at(2).action = { |value,element|
				var outVal;
				// Z: - inverted
				value = 1 - value; // invert
				data.leftExtendZ = value;
				this.calculatePosition('left');
				if (data.calibrate == 1 or: {(data.calibrateFS == 1) and: (data.footSwitch == 1)}, {
					this.calibrateMinMax("leftExtendZMin", "leftExtendZMax", value);
				});
				if ( outBus.class == Bus, {
					outVal = value.linlin(data.leftExtendZMin, data.leftExtendZMax, 0, 1);
					outBus.setAt(2, outVal);
				});
			};
			classData.gameTrak.elements.at(3).action = { |value,element|
				var outVal;
				// Rx:
				data.rightAngleX = value;
				this.calculatePosition('right');
				if (data.calibrate == 1 or: {(data.calibrateFS == 1) and: (data.footSwitch == 1)}, {
					this.calibrateMinMax("rightAngleXMin", "rightAngleXMax", value);
				});
				if ( outBus.class == Bus, {
					outVal = value.linlin(data.rightAngleXMin, data.rightAngleXMax, 0, 1);
					outBus.setAt(3, outVal);
				});
			};
			classData.gameTrak.elements.at(4).action = { |value,element|
				var outVal;
				// Ry:
				data.rightAngleY = value;
				this.calculatePosition('right');
				if (data.calibrate == 1 or: {(data.calibrateFS == 1) and: (data.footSwitch == 1)}, {
					this.calibrateMinMax("rightAngleYMin", "rightAngleYMax", value);
				});
				if ( outBus.class == Bus, {
					outVal = value.linlin(data.rightAngleYMin, data.rightAngleYMax, 0, 1);
					outBus.setAt(4, outVal);
				});
			};
			classData.gameTrak.elements.at(5).action = { |value,element|
				var outVal;
				// Rz: - inverted
				value = 1 - value; // invert
				data.rightExtendZ = value;
				this.calculatePosition('right');
				if (data.calibrate == 1 or: {(data.calibrateFS == 1) and: (data.footSwitch == 1)}, {
					this.calibrateMinMax("rightExtendZMin", "rightExtendZMax", value);
				});
				if ( outBus.class == Bus, {
					outVal = value.linlin(data.rightExtendZMin, data.rightExtendZMax, 0, 1);
					outBus.setAt(5, outVal);
				});
			};
			classData.gameTrak.elements.at(6).action = { |value,element|
				// b1:
				data.footSwitch = value;
				if ( outBus.class == Bus, {
					outBus.setAt(6, value);
				});
				if (value == 1, {
					this.performActions('down');
				}, {
					this.performActions('up');
				});
			};
		});
	}

	openGameTrak {
		var dictOpenDevices, dictFoundDevices;
		if (classData.gameTrak.isNil or: {classData.gameTrak.isOpen.not}, {
			classData.gameTrak = nil;
			HID.findAvailable;
			//HID.postAvailable;
			// check if open device found
			dictOpenDevices = HID.openDevices;
			dictOpenDevices.do({arg item, i;
				if ((item.info.vendorID == 5303) and: (item.info.productID == 2434), {
					classData.gameTrak = item;
				});
			});
			if (classData.gameTrak.isNil , {
				dictFoundDevices = HID.findBy(5303, 2434);
				if (dictFoundDevices.size > 0, {
					"TX: Opening Gametrak... ".postln;
					classData.gameTrak = HID.open(5303, 2434); // Gametrak
					//("Gametrak.isOpen  " ++ h.isOpen).postln;
				});
			});
		});
		if (classData.gameTrak.notNil and: {classData.gameTrak.isOpen}, {
			data.deviceStatus = " GameTrak Connected";
		}, {
			data.deviceStatus = " GameTrak Not Found";
		});
	}

	hidDeactivate {
		//if (classData.gameTrak.notNil and: {classData.gameTrak.isOpen}, {
		if (classData.gameTrak.notNil, {
			classData.gameTrak.elements.at(0).action = {};
			classData.gameTrak.elements.at(1).action = {};
			classData.gameTrak.elements.at(2).action = {};
			classData.gameTrak.elements.at(3).action = {};
			classData.gameTrak.elements.at(4).action = {};
			classData.gameTrak.elements.at(5).action = {};
			classData.gameTrak.elements.at(6).action = {};
		});
	}

	reconnect {
		data.deviceStatus = " Trying to Connect...";
		system.showViewIfModDisplay(this);
		{
			this.hidActivate;
			2.wait;
			system.showViewIfModDisplay(this);
		}.fork;
	}

	updateMinMaxVal {arg minString, maxString;
		data[minString.asSymbol] = this.getSynthArgSpec(minString);
		data[maxString.asSymbol] = this.getSynthArgSpec(maxString);
	}

	setCalibration{
		var holdVal;
		holdVal = this.getSynthArgSpec("calibrate");
		data.calibrate = holdVal;
		if (holdVal == 1, {
			this.setSynthValue("calibrateFS", 0);
			data.calibrateFS = 0;
			this.prepareCalibration;
			system.flagGuiIfModDisplay(this);
		});
	}

	setCalibrationFS{
		var holdVal;
		holdVal = this.getSynthArgSpec("calibrateFS");
		data.calibrateFS = holdVal;
		if (holdVal == 1, {
			this.setSynthValue("calibrate", 0);
			data.calibrate = 0;
			this.prepareCalibration;
			system.flagGuiIfModDisplay(this);
		});
	}

	setCalibrationOff{
		this.setSynthValue("calibrate", 0);
		this.setSynthValue("calibrateFS", 0);
		data.calibrate = 0;
		data.calibrateFS = 0;
	}

	calibrateMinMax {arg minString, maxString, value;
		var holdVal = value;
		var detect_symbMin, detect_symbMax, symbMin, symbMax;
		if (holdVal.notNil, {
			detect_symbMin = ("detect_" ++ minString).asSymbol;
			detect_symbMax = ("detect_" ++ maxString).asSymbol;
			symbMin = minString.asSymbol;
			symbMax = maxString.asSymbol;
			if (holdVal < data[detect_symbMin], {
				data[detect_symbMin] = holdVal;
				data[symbMin] = holdVal;
				this.setSynthArgSpec(minString, holdVal);
				system.flagGuiIfModDisplay(this);
			});
			if (holdVal > data[detect_symbMax], {
				data[detect_symbMax] = holdVal;
				data[symbMax] = holdVal;
				this.setSynthArgSpec(maxString, holdVal);
				system.flagGuiIfModDisplay(this);
			});
		});
	}

	calculatePosition {arg hand;
		var maxAngle;
		var rotate_leftX;
		var rotate_leftY;
		var rotate_rightX;
		var rotate_rightY;
		var shift_leftX;
		var shift_leftY;
		var shift_rightX;
		var shift_rightY;
		var distOffset, dist_left, dist_right;
		var outVals = ();

		// init
		// max angle is +/- 32.5 deg - from //github.com/vrpn/vrpn/blob/master/vrpn_Tracker_GameTrak.C
		maxAngle = 1.1344640137963; // in radians = 2 * (32.5).degrad
		distOffset = 0.02; // since minimum thread extension is a few cm from centre of measuring ball

		if (hand == 'left', {
			rotate_leftX = (data.leftAngleX - 0.5) * maxAngle;
			rotate_leftY = (data.leftAngleY - 0.5) * maxAngle;
			dist_left = distOffset + data.leftExtendZ;
			shift_leftX = (dist_left * rotate_leftX.sin);
			shift_leftY = (dist_left * rotate_leftY.sin);
			data.leftPosX = 0.5 + shift_leftX;
			data.leftPosY = 0.5 + shift_leftY;
			data.leftPosZ = (dist_left.squared - (shift_leftX.squared + shift_leftY.squared)).sqrt;
		}, {
			rotate_rightX = (data.rightAngleX - 0.5) * maxAngle;
			rotate_rightY = (data.rightAngleY - 0.5) * maxAngle;
			dist_right = distOffset + data.rightExtendZ;
			shift_rightX = (dist_right * rotate_rightX.sin);
			shift_rightY = (dist_right * rotate_rightY.sin);
			data.rightPosX = 0.5 + shift_rightX;
			data.rightPosY = 0.5 + shift_rightY;
			data.rightPosZ = (dist_right.squared - (shift_rightX.squared + shift_rightY.squared)).sqrt;
		});
		// callibrate
		if (data.calibrate == 1 or: {(data.calibrateFS == 1) and: (data.footSwitch == 1)}, {
			if (hand == 'left', {
				this.calibrateMinMax("leftPosXMin", "leftPosXMax", data.leftPosX);
				this.calibrateMinMax("leftPosYMin", "leftPosYMax", data.leftPosY);
				this.calibrateMinMax("leftPosZMin", "leftPosZMax", data.leftPosZ);
			}, {
				this.calibrateMinMax("rightPosXMin", "rightPosXMax", data.rightPosX);
				this.calibrateMinMax("rightPosYMin", "rightPosYMax", data.rightPosY);
				this.calibrateMinMax("rightPosZMin", "rightPosZMax", data.rightPosZ);
			});
		});
		// create outVals
		if (hand == 'left', {
			outVals.leftPosX = data.leftPosX.linlin(data.leftPosXMin, data.leftPosXMax, 0, 1);
			outVals.leftPosY = data.leftPosY.linlin(data.leftPosYMin, data.leftPosYMax, 0, 1);
			outVals.leftPosZ = data.leftPosZ.linlin(data.leftPosZMin, data.leftPosZMax, 0, 1);
			outBus.setAt(7, outVals.leftPosX);
			outBus.setAt(8, outVals.leftPosY);
			outBus.setAt(9, outVals.leftPosZ);
		}, {
			outVals.rightPosX = data.rightPosX.linlin(data.rightPosXMin, data.rightPosXMax, 0, 1);
			outVals.rightPosY = data.rightPosY.linlin(data.rightPosYMin, data.rightPosYMax, 0, 1);
			outVals.rightPosZ = data.rightPosZ.linlin(data.rightPosZMin, data.rightPosZMax, 0, 1);
			outBus.setAt(10, outVals.rightPosX);
			outBus.setAt(11, outVals.rightPosY);
			outBus.setAt(12, outVals.rightPosZ);
		});
	}

	prepareCalibration {
		data.detect_leftAngleXMin = 1.1;
		data.detect_leftAngleXMax = -0.1;
		data.detect_leftAngleYMin = 1.1;
		data.detect_leftAngleYMax = -0.1;
		data.detect_leftExtendZMin = 1.1;
		data.detect_leftExtendZMax = -0.1;
		data.detect_rightAngleXMin = 1.1;
		data.detect_rightAngleXMax = -0.1;
		data.detect_rightAngleYMin = 1.1;
		data.detect_rightAngleYMax = -0.1;
		data.detect_rightExtendZMin = 1.1;
		data.detect_rightExtendZMax = -0.1;

		data.detect_leftPosXMin = 1.1;
		data.detect_leftPosXMax = -0.1;
		data.detect_leftPosYMin = 1.1;
		data.detect_leftPosYMax = -0.1;
		data.detect_leftPosZMin = 1.1;
		data.detect_leftPosZMax = -0.1;
		data.detect_rightPosXMin = 1.1;
		data.detect_rightPosXMax = -0.1;
		data.detect_rightPosYMin = 1.1;
		data.detect_rightPosYMax = -0.1;
		data.detect_rightPosZMin = 1.1;
		data.detect_rightPosZMax = -0.1;
	}

	setDefaultRanges{
		this.setSynthArgSpec("leftAngleXMin", 0);
		this.setSynthArgSpec("leftAngleXMax", 1);
		this.setSynthArgSpec("leftAngleYMin", 0);
		this.setSynthArgSpec("leftAngleYMax", 1);
		this.setSynthArgSpec("leftExtendZMin", 0);
		this.setSynthArgSpec("leftExtendZMax", 1);
		this.setSynthArgSpec("rightAngleXMin", 0);
		this.setSynthArgSpec("rightAngleXMax", 1);
		this.setSynthArgSpec("rightAngleYMin", 0);
		this.setSynthArgSpec("rightAngleYMax", 1);
		this.setSynthArgSpec("rightExtendZMin", 0);
		this.setSynthArgSpec("rightExtendZMax", 1);

		this.setSynthArgSpec("leftPosXMin", 0);
		this.setSynthArgSpec("leftPosXMax", 1);
		this.setSynthArgSpec("leftPosYMin", 0);
		this.setSynthArgSpec("leftPosYMax", 1);
		this.setSynthArgSpec("leftPosZMin", 0);
		this.setSynthArgSpec("leftPosZMax", 1);
		this.setSynthArgSpec("rightPosXMin", 0);
		this.setSynthArgSpec("rightPosXMax", 1);
		this.setSynthArgSpec("rightPosYMin", 0);
		this.setSynthArgSpec("rightPosYMax", 1);
		this.setSynthArgSpec("rightPosZMin", 0);
		this.setSynthArgSpec("rightPosZMax", 1);
}

	copyMinMaxVal {arg minString, maxString, minVal, maxVal;
		if (minVal < 1, {
			this.setSynthValue(minString, ControlSpec(0, 1).constrain(minVal));
		});
		if (maxVal > 0, {
			this.setSynthValue(maxString, ControlSpec(0, 1).constrain(maxVal));
		});
		system.flagGuiIfModDisplay(this);
	}

	resetAllOutputs {
		outBus.setn((0 ! 7) ++ [0.5, 0.5, 0, 0.5, 0.5, 0]);
		data.leftAngleX = 0;
		data.leftAngleY = 0;
		data.leftExtendZ = 0;
		data.rightAngleX = 0;
		data.rightAngleY = 0;
		data.rightExtendZ = 0;
		data.footSwitch = 0;
		data.leftPosX = 0.5;
		data.leftPosY = 0.5;
		data.leftPosZ = 0;
		data.rightPosX = 0.5;
		data.rightPosY = 0.5;
		data.rightPosZ = 0;
	}

	// base class overrides

	rebuildSynth { }	// override base class method

	runAction {this.reconnect}   //	override base class

	pauseAction {this.hidDeactivate}   //	override base class

	extraSaveData {
		var dataAsPairs;
		dataAsPairs = data.asKeyValuePairs;
		^[arrActions, dataAsPairs];
	}

	loadExtraData {arg argData;  // override default method
		var dataAsPairs;
		arrActions = argData.at(0);
		dataAsPairs = argData.at(1);
		if (dataAsPairs.notNil, {
			data.putPairs(dataAsPairs);
		});
		this.reconnect;
		this.setCalibrationOff;
	}

	deleteModuleExtraActions {
		this.hidDeactivate;
	}

}

