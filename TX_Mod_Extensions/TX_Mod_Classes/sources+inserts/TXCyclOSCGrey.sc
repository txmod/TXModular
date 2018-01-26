// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXCyclOSCGrey : TXModuleBase {

	//	Based on CyclOSC by Steve Symons:  http://muio.org

	classvar <>classData;

	var arrRunActionNames;
	var	displayOption;
	var  arrActions;
	var	oscResponder;
	var	analyseRoutine;
	var	gridGrey;
	var	<noGridRows;
	var	<noGridCols;
	var  arrMatches;
	var holduseZone, holdtolerance, holdtargetGrey, holdarrActiveGridCells, holdminTargetCount;
	var holdTargetFound, holdXTarget, holdYTarget, holdSubArrayX, holdSubArrayY, holdTargetFoundState;

	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "CyclOSC Grey";
		classData.moduleRate = "control";
		classData.moduleType = "source";
		classData.arrCtlSCInBusSpecs = [];
		classData.noOutChannels = 3;
		classData.arrOutBusSpecs = [
			["Out X", [0]],
			["Out Y", [1]],
			["Target Found", [2]]
		];
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showTarget";
		arrRunActionNames = ["runAction1", "runAction2", "runAction3", "runAction4", "runAction5", "runAction6",
			"runAction7", "runAction8", "runAction9", "runAction10"];
		arrActions = [99,0,0,0,0,0,0, nil].dup(10);
		arrSynthArgSpecs = [
			["useZone",  0], // gives the option not to use zone
			["tolerance",  5],
			["resolution",  1],
			["arrActiveGridCells",  Array.fill2D(32, 32, 0.6)],
			["targetGrey",  0.5],
			["analysisRate",  10],
			["minTargetCount",  1],
			["runAction1", 0],
			["runAction2", 0],
			["runAction3", 0],
			["runAction4", 0],
			["runAction5", 0],
			["runAction6", 0],
			["runAction7", 0],
			["runAction8", 0],
			["runAction9", 0],
			["runAction10", 0],
		];
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["TXCheckBox", "Use Zone", "useZone"], // gives the option not to use zone
			["EZslider", "Tolerance %", ControlSpec(0,100), "tolerance"],
			["EZslider", "Min Target Count", ControlSpec(1,20, step:1), "minTargetCount"],
			["EZslider", "Analysis rate", ControlSpec(1,10), "analysisRate"],
			["EZslider", "Target grey (0-1)", ControlSpec(0,1), "targetGrey"],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
		// other inits
		this.setResolution(this.getSynthArgSpec("resolution"));
		this.initGridGrey(32, 32);
		this.oscActivate;
		this.startRoutine;
	}

	buildGuiSpecArray {
		var arrActionTypes, arrResolutions;
		arrActionTypes = ["Never run action", "Run action when target is found", "Run action when target no longer found"];
		arrResolutions = ["8 X 8", "16 X 16", "32 X 32", ];
		guiSpecArray = [
			["ActionButton", "Target settings", {displayOption = "showTarget";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showTarget")],
			["Spacer", 3],
			["ActionButton", "Zone settings", {displayOption = "showZone";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showZone")],
			["Spacer", 3],
			["ActionButton", "Actions 1-4", {displayOption = "showActions1-4";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showActions1-4")],
			["Spacer", 3],
			["ActionButton", "Actions 5-8", {displayOption = "showActions5-8";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showActions5-8")],
			["Spacer", 3],
			["ActionButton", "Actions 9-10", {displayOption = "showActions9-10";
				this.buildGuiSpecArray; system.showView;}, 130,
			TXColor.white, this.getButtonColour(displayOption == "showActions9-10")],
			//		["Spacer", 3],
			//		["ActionButton", "Test Actions", {this.performActions},
			//			130, TXColor.white, TXColor.sysGuiCol2],
			["DividingLine"],
			["SpacerLine", 6],
		];
		if (displayOption == "showTarget", {
			guiSpecArray = guiSpecArray ++[
				["TXPopupAction", "Grid size", arrResolutions, "resolution",  {arg view; this.setResolution(view.value); system.showView}, 180],
				["TXGridGreyTarget", "Video Grid", {gridGrey.deepCopy}, "targetGrey", noGridRows, noGridCols],
				["DividingLine"],
				["EZslider", "Tolerance %", ControlSpec(0,20), "tolerance"],
				["EZslider", "Min Target", ControlSpec(1,20, step:1), "minTargetCount"],
				["EZslider", "Analysis rate", ControlSpec(1,10), "analysisRate"],
				["DividingLine"],
			];
		});
		if (displayOption == "showZone", {
			guiSpecArray = guiSpecArray ++[
				["TextBar", "If Zone is used, the target shade will be looked for only in the zone", 450],
				["TXCheckBox", "Use Zone", "useZone"],
				["DividingLine"],
				["TXGridGreyZone", "Video Grid", {gridGrey.deepCopy}, "arrActiveGridCells", noGridRows, noGridCols],
				["DividingLine"],
			];
		});
		if (displayOption == "showActions1-4", {
			guiSpecArray = guiSpecArray ++[
				["TXActionView", arrActions, 0],
				["NextLine"],
				["TXPopupAction", "Action type", arrActionTypes, "runAction1", nil, 350],
				["DividingLine"],
				["TXActionView", arrActions, 1],
				["NextLine"],
				["TXPopupAction", "Action type", arrActionTypes, "runAction2", nil, 350],
				["DividingLine"],
				["TXActionView", arrActions, 2],
				["NextLine"],
				["TXPopupAction", "Action type", arrActionTypes, "runAction3", nil, 350],
				["DividingLine"],
				["TXActionView", arrActions, 3],
				["NextLine"],
				["TXPopupAction", "Action type", arrActionTypes, "runAction4", nil, 350],
				["DividingLine"],
			];
		});
		if (displayOption == "showActions5-8", {
			guiSpecArray = guiSpecArray ++[
				["TXActionView", arrActions, 4],
				["NextLine"],
				["TXPopupAction", "Action type", arrActionTypes, "runAction5", nil, 350],
				["DividingLine"],
				["TXActionView", arrActions, 5],
				["NextLine"],
				["TXPopupAction", "Action type", arrActionTypes, "runAction6", nil, 350],
				["DividingLine"],
				["TXActionView", arrActions, 6],
				["NextLine"],
				["TXPopupAction", "Action type", arrActionTypes, "runAction7", nil, 350],
				["DividingLine"],
				["TXActionView", arrActions, 7],
				["NextLine"],
				["TXPopupAction", "Action type", arrActionTypes, "runAction8", nil, 350],
				["DividingLine"],
			];
		});
		if (displayOption == "showActions9-10", {
			guiSpecArray = guiSpecArray ++[
				["TXActionView", arrActions, 8],
				["NextLine"],
				["TXPopupAction", "Action type", arrActionTypes, "runAction9", nil, 350],
				["DividingLine"],
				["TXActionView", arrActions, 9],
				["NextLine"],
				["TXPopupAction", "Action type", arrActionTypes, "runAction10", nil, 350],
				["DividingLine"],
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

	setResolution{ arg res;
		if (res == 0, {
			noGridRows = 8;
			noGridCols = 8;
		});
		if (res == 1, {
			noGridRows = 16;
			noGridCols = 16;
		});
		if (res == 2, {
			noGridRows = 32;
			noGridCols = 32;
		});
		this.buildGuiSpecArray;
	}

	runAction {   //	override base class
		this.oscActivate;
		this.startRoutine;
	}

	pauseAction {   //	override base class
		this.oscDeactivate;
		this.stopRoutine;
	}

	extraSaveData {
		^arrActions;
	}
	loadExtraData {arg argData;  // override default method
		arrActions = argData;
		this.setResolution(this.getSynthArgSpec("resolution"));
	}

	initGridGrey{arg arg_gridX, arg_gridY;
		var col;
		gridGrey = Array.new(arg_gridX);
		arg_gridY.do{arg j;
			col =  Array.new(arg_gridY);
			arg_gridX.do{arg i;col.add(0);};
			gridGrey.add(col);
		};
	}

	getGridGreyCell{arg i,j;
		^gridGrey[i][j];
	}

	setActiveCell{arg i,j, val;
		gridGrey[i][j] = val;
	}

	getGridGreyRow{arg i;
		^gridGrey[i];
	}

	oscActivate {
		//	stop any previous responder
		this.oscDeactivate;
		oscResponder = OSCFunc({arg msg, time, addr,  recvPort;
			var row = msg[1];
			noGridRows.do{arg j; gridGrey[row][j] = msg[j+2] ? 0;};
			//	// For testing  - post details
			//	("TXCyclOSC : ").postln;
			//	[time, msg].postln;
		}, '/txcyclosc/grayrow', nil);
	}

	oscDeactivate {
		if (oscResponder.notNil, {
			oscResponder.free;
		});
	}

	startRoutine{
		analyseRoutine = Routine { arg inval;
			loop {
				holdtolerance = this.getSynthArgSpec("tolerance") * 2.55;
				holdtargetGrey = this.getSynthArgSpec("targetGrey") * 255;
				holdarrActiveGridCells = this.getSynthArgSpec("arrActiveGridCells");
				holdminTargetCount = this.getSynthArgSpec("minTargetCount");
				arrMatches = [];

				if (this.getSynthArgSpec("useZone") == 1, {
					noGridRows.do({ arg row, i;
						noGridCols.do({arg col, j;
							// if value matches target within tolerance and cell is active
							if ( ((gridGrey.at(i).at(j) - holdtargetGrey).abs < holdtolerance)
								and: (holdarrActiveGridCells.at(i).at(j) == 1),
								{
									// add to array of matches
									arrMatches = arrMatches.add([i, j]);
							});
						});
					});
				},{
					noGridRows.do({ arg row, i;
						noGridCols.do({arg col, j;
							// if value matches target within tolerance
							if ((gridGrey.at(i).at(j) - holdtargetGrey).abs < holdtolerance, {
								// add to array of matches
								arrMatches = arrMatches.add([i, j]);
							});
						});
					});
				});
				// get output values
				if (arrMatches.size >= holdminTargetCount, {
					holdTargetFound = 1;
					holdSubArrayX = arrMatches.collect({arg item, i; item.at(0)});
					holdSubArrayY = arrMatches.collect({arg item, i; item.at(1)});
					holdXTarget = (holdSubArrayX.sum/ holdSubArrayX.size)/ noGridRows;
					holdYTarget = (holdSubArrayY.sum/ holdSubArrayY.size) / noGridCols;
				}, {
					holdTargetFound = 0;
				});
				// set the Bus values
				if ( outBus.class == Bus, {
					outBus.setn([holdXTarget, holdYTarget, holdTargetFound]);
				});
				// run actions once when target found
				this.onOffStateChange(holdTargetFound);
				// pause time based on 1/anaylsis rate
				this.getSynthArgSpec("analysisRate").reciprocal.yield;
			}
		}.play;
	}

	stopRoutine{
		analyseRoutine.stop;

	}
	deleteModuleExtraActions {
		this.oscDeactivate;
		this.stopRoutine;
	}

	onOffStateChange { arg newState;
		// If state has changed, then run actions
		if ((newState == 1) and: (holdTargetFoundState == 0), {this.performActions(1)});
		if ((newState == 0) and: (holdTargetFoundState == 1), {this.performActions(0)});
		holdTargetFoundState = newState;
	}

	performActions {arg argTargetFound;
		arrActions.do({ arg item, i;
			var holdModuleID, holdModule, holdActionInd, holdArrActionItems, holdActionText,
			holdAction, holdVal1, holdVal2, holdVal3;
			// if action should be run
			if ([9, 1, 0].at(this.getSynthArgSpec(arrRunActionNames.at(i))) == argTargetFound, {
				holdModuleID = item.at(0);
				holdActionInd = item.at(1);
				holdVal1 = item.at(2);
				holdVal2 = item.at(3);
				holdVal3 = item.at(4);
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
					// if action type is commandAction then value it with arguments
					if (holdAction.actionType == \commandAction, {
						holdAction.actionFunction.value(holdVal1, holdVal2, holdVal3);
					});
					// if action type is valueAction then value it with arguments
					if (holdAction.actionType == \valueAction, {
						holdAction.setValueFunction.value(holdVal1, holdVal2, holdVal3);
					});
				});
			});
		});
		//	gui update
		//		system.flagGuiUpd;
	}	// end of performActions


}

