// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXChannelRouting {	// Channel Routing

	classvar <>system;	    		// parent system - set by parent
	classvar <arrChannels;  		// array of channels created
	classvar <>startChannel;   		// starting channel for view
	classvar <>arrControlVals; 		// gui control values - (no longer used)
	classvar <>displayModule; 		// shows current module shown in single window mode
	classvar <>displayChannel; 		// shows current channel to be highlighted
	classvar <>showModuleBox;		// whether module box is shown
	classvar <>showChannelType = "all";
	classvar <>dataBank; 				// event to hold data

	*initClass{
		arrChannels = [];
		startChannel = 0;
		displayModule = nil;
		showModuleBox = true;
		dataBank = ();
		dataBank.popNewModuleCatInd = 0;
		dataBank.popNewModuleInd = 0;
		dataBank.arrScrollViews = [];
		dataBank.arrUserViews = [];
		dataBank.startPointX = 0;
		dataBank.holdViewIndex = 0;
		dataBank.allModulesBoxWidth = 186;
		dataBank.sigFlowBoxWidth = 700;
		dataBank.moduleBoxWidth = 500;
		dataBank.moduleBoxMaxWidth = 500;
		dataBank.channelTitleBoxWidth = 186;
		dataBank.channelWidth = 151;
		//dataBank.channelBoxWidth = 4 + (3 * dataBank.channelWidth);
		dataBank.channelBoxWidth = 380;
		dataBank.showChannelMeters = true;
		dataBank.boxHeight = 612;
		dataBank.wideChannelMode = true;
		dataBank.multiWindow = false;
		dataBank.newModuleCopies = 1;

		dataBank.allModulesData = ();
		dataBank.allModulesData.displayMode = 'modulesMode';
		dataBank.allModulesData.addEditSelection = "+Source";
		dataBank.allModulesData.closedSourceCats = (); // closed module categories
		dataBank.allModulesData.closedMeterCats = ();
		dataBank.allModulesData.closedEditCats = ();

		dataBank.channelTitleData = ();
		dataBank.channelTitleData.displayMode = 'channelsMode';
		dataBank.channelTitleData.addEditSelection = "Titles";
		dataBank.channelTitleData.closedInsertCats = ();
		dataBank.channelTitleData.closedChannelCats = ();

		this.resetScrollerOrigins;
	}

	////////////////////////////////////////////////////////////////////////////////////

	*resetScrollerOrigins {
		dataBank.channelTitleVisibleOrigin = Point.new(0,0);
		dataBank.channelsVisibleOrigin = Point.new(0,0);
		dataBank.moduleVisibleOrigin = Point.new(0,0);
		dataBank.allModulesVisibleOrigin = Point.new(0,0);
	}

	*resetChannelsScrollerOrigin {
		dataBank.channelsVisibleOrigin = Point.new(0,0);
	}

	////////////////////////////////////////////////////////////////////////////////////

	*saveData {
		// this method returns an array of all data for saving with various components:
		// 0- string "TXModuleSaveData", 1- module class, 2- arrControlVals, 3- arrAllChannelData
		var arrData, arrAllChannelData, dataBankSaveData, holdDisplayModuleID;
		if (displayModule.notNil, {
			holdDisplayModuleID = displayModule.moduleID;
		});
		// collect saveData from  all modules
		arrAllChannelData = arrChannels.collect({ arg item, i; item.saveData; });
		dataBankSaveData = [
			dataBank.allModulesBoxWidth,
			dataBank.sigFlowBoxWidth,
			dataBank.moduleBoxWidth,
			dataBank.moduleBoxMaxWidth,
			dataBank.channelTitleBoxWidth,
			dataBank.showChannelMeters,
			dataBank.wideChannelMode,
			dataBank.channelBoxWidth,
		];
		arrData = ["TXModuleSaveData", this.class.asString, arrControlVals,
			arrAllChannelData, dataBankSaveData, holdDisplayModuleID];
		^arrData;
	}

	*loadData { arg arrData, holdLoadQueue, holdLastChanCondition;
		// this method updates all data by loading arrData. format:
		// 0- string "TXModuleSaveData", 1- module class, 2- arrControlVals, 3- arrAllChannelData
		var arrAllChannelData, dataBankSaveData, holdSourceModuleID, holdSourceModule, newChannel, dispModuleId;
		// error check
		if (arrData.class != Array, {
			TXInfoScreen.new("Error: invalid data. cannot load.");
			^0;
		});
		if (arrData.at(1) != this.class.asString, {
			TXInfoScreen.new("Error: invalid data class. cannot load.");
			^0;
		});
		// reset variable
		showChannelType = "all";
		arrChannels = [];
		// assign values
		//	arrControlVals = arrData.at(2).copy;	// no longer used
		arrAllChannelData = arrData.at(3).deepCopy;
		dataBankSaveData = arrData.at(4).deepCopy;
		if (dataBankSaveData.notNil, {
			dataBank.allModulesBoxWidth = dataBankSaveData[0];
			dataBank.sigFlowBoxWidth = dataBankSaveData[1];
			dataBank.moduleBoxWidth = dataBankSaveData[2];
			dataBank.moduleBoxMaxWidth = dataBankSaveData[3];
			dataBank.channelTitleBoxWidth = dataBankSaveData[4];
			dataBank.showChannelMeters = dataBankSaveData[5];
			this.setWideChannelMode(dataBankSaveData[6]);
			dataBank.channelBoxWidth = dataBankSaveData[7] ? 380;
		});
		dispModuleId = arrData.at(5).copy;
		Routine.run {
			var holdCondition;

			// for each saved channel - recreate module, add to arrSystemModules and run loadData
			arrAllChannelData.do({ arg item, i;
				var holdChanCondition;

				// add condition to load queue
				holdChanCondition = holdLoadQueue.addCondition;
				// pause
				holdChanCondition.wait;
				// pause
				system.server.sync;

				// get source module data
				holdSourceModuleID = item.at(4).at(0);
				holdSourceModule = system.getModuleFromID(holdSourceModuleID);
				// create new instance of channel
				newChannel = TXChannel.new(holdSourceModule);
				// add module to array
				arrChannels = arrChannels.add(newChannel);
				// load ModuleID into new module
				newChannel.loadModuleID(item);

				// remove condition from load queue
				holdLoadQueue.removeCondition(holdChanCondition);
			});
			// load data into all channels
			arrAllChannelData.do({ arg item, i;
				var holdChanCondition;

				// add condition to load queue
				holdChanCondition = holdLoadQueue.addCondition;
				// pause
				holdChanCondition.wait;
				// pause
				system.server.sync;

				arrChannels.at(i).loadData(item);

				// remove condition from load queue
				holdLoadQueue.removeCondition(holdChanCondition);
			});

			// add condition to load queue
			holdCondition = holdLoadQueue.addCondition;
			// pause
			holdCondition.wait;
			// pause
			system.server.sync;

			if (dispModuleId.notNil, {
				displayModule = system.getModuleFromID(dispModuleId);
			});

			// remove condition from load queue
			holdLoadQueue.removeCondition(holdCondition);

			arrChannels.do({ arg item, i;
				var holdChanCondition;

				// add condition to load queue
				holdChanCondition = holdLoadQueue.addCondition;
				// pause
				holdChanCondition.wait;
				// pause
				system.server.sync;

				// reactivate channel
				item.reactivate;

				// remove condition from load queue
				holdLoadQueue.removeCondition(holdChanCondition);
			});

			// add last condition to load queue
			holdLoadQueue.addCondition(holdLastChanCondition);

			// add history
			TXSystem1.addHistoryEvent;

			// recreate view
			system.showView;
		};
	}

	*setWideChannelMode {arg mode;
		dataBank.wideChannelMode = mode;
		TXChannel.adjustGuiWidth(dataBank.wideChannelMode);
	}

	////////////////////////////////////////////////////////////////////////////////////

	*arrShowChannels{
		var holdArrShowChannels;
		//	create array of channels to be shown
		if (showChannelType == "all", {
			holdArrShowChannels = arrChannels;
			},{
				holdArrShowChannels = arrChannels.select({ arg item, i; item.channelRate == showChannelType; });
		});
		^holdArrShowChannels;
	}

	////////////////////////////////////////////////////////////////////////////////////

	*addChannel{ arg argModule;
		var newChannel;
		if (system.server.serverRunning.not, {
			TXInfoScreen.new("Error: Server not running");
			^0;
		});
		newChannel = TXChannel.new(argModule);
		arrChannels = arrChannels.add(newChannel, TXSystem1.dataBank.defaultChanLevel);
		displayChannel = newChannel;
		// set position
		TXSignalFlow.setPositionNear(newChannel, argModule);
		// make sure new channel is displayed
		if ( (showChannelType == "control") and: (newChannel.channelRate ==  "audio"), {showChannelType = "all";});
		if ( (showChannelType == "audio") and: (newChannel.channelRate ==  "control"), {showChannelType = "all";});
		// post message
		//	("Adding Channel: " ++ newChannel.instName).postln;
		^newChannel;
	}

	*checkDeletions {
		// reset variable
		if (displayModule.notNil, {
			if (displayModule.deletedStatus == true, {
				displayModule = nil;
			});
		});
		// run deletions check in all channels
		arrChannels.do({ arg item, i;  item.checkDeletions; });
		// delete any channels in  arrChannels marked for deletion
		arrChannels.do({ arg item, i;
			if (item.toBeDeletedStatus==true, {item.deleteChannel});
		});
		// recreate arrChannels without deleted ones
		arrChannels = arrChannels.select({ arg item, i; item.deletedStatus == false; });
		// reorder channels
		this.reorderChannelSynths;
	}

	*checkRebuilds {
		// run rebuilds check in all channels
		arrChannels.do({ arg item, i;  item.checkDeletions; });
		// reorder channels
		this.reorderChannelSynths;
	}

	*deleteAllChannels {
		// reset variables
		showModuleBox = false;
		displayModule = nil;
		startChannel = 0;
		// delete all channels
		arrChannels.do({ arg item, i;  item.deleteChannel; });
		// recreate arrChannels without deleted ones
		arrChannels = [];
		// recreate TXChannel.arrInstances without deleted ones
		TXChannel.arrInstances = TXChannel.arrInstances.select({ arg item, i; item.deletedStatus == false; });
		// stop meters
		TXMeterBridge.removeAllMeters;
		this.resetAllEditCategories;
		this.resetScrollerOrigins;
	}

	*resetAllEditCategories{
		dataBank.closedEditCats = ();
	}

	*removeAllMeterResponders {
		arrChannels.do({arg item; item.removeAllResponders});
	}

	*checkChannelsDest { arg argModule, argOptionNo;
		// run channel dest check on all channels
		arrChannels.do({ arg item, i;
			if (item.destModule == argModule, {
				if (item.destBusNo == argOptionNo, {
					item.deactivate;
					item.chanError =
					"error: Modulation needs to be switched on for this bus "
					++ "in Modulation Options of destination module";
				});
			});
		});
	}

	*display{ arg argModule;
		if (argModule.class.moduleType == "bus" or: (argModule.class.moduleType == "channel"), {
		},{
			if (dataBank.multiWindow == false, {
				displayModule = argModule;
				showModuleBox = true;
				system.addHistoryEvent;
				// update view
				system.showModulesAndChannels;
			}, {
				argModule.openGui;
			});
		});
	}

	*sortChannels { arg sortOption = 0;
		// sort channels
		arrChannels.sort({ arg a, b;   a.sortKey(sortOption) <= b.sortKey(sortOption);});
		// reset startChannel
		startChannel = 0;
	}

	*reorderChannelSynths {
		// wait for server sync
		Routine.run {
			system.server.sync;
			arrChannels.do({ arg item, i;
				item.sendChannelToTail;
			});
		};
	}
	*hideModuleBox {
		if ( showModuleBox == true, {
			// reset variables
			showModuleBox = false;
			displayModule = nil;
			system.addHistoryEvent;
			// update view
			system.showView;
		});
	}

	////////////////////////////////////////////////////////////////////////////////////


	*setStartChannel { arg argStartChannelNo;
		var arrChannelNos;
		if (this.arrShowChannels.size > 0, {
			// set startChannel
			arrChannelNos = this.arrShowChannels.collect({arg item, i; item.channelNo});
			startChannel = arrChannelNos.indexOf(argStartChannelNo.nearestInList(arrChannelNos));
		});
		// update view
		system.showView;
	}

	*getStartChannel {
		var arrChannelNos, holdStartChannel;
		holdStartChannel = 1;
		if (this.arrShowChannels.size > 0, {
			// set startChannel
			arrChannelNos = this.arrShowChannels.collect({arg item, i; item.channelNo});
			holdStartChannel = arrChannelNos.at(startChannel) ? 0;
		});
		^holdStartChannel;
	}

	////////////////////////////////////////////////////////////////////////////////////

	*makeGui{ arg parent, showSigFlow = false;
		var arrAllSourceModules, arrAllSourceModNames;
		var arrAllSourceModsBusses, arrAllSourceModBusNames;
		var arrAllSourceActionModules, arrAllSourceActionModNames;
		var listModules, listViewModules;
		var plusMinusString, plusMinusActionFunc, holdView;
		var moduleScrollView, moduleBox, moduleBoxBar;
		var allModulesBoxBar, btnDelete;
		var channelTitleBoxBar;
		var channelBox, channelBoxBar;
		var numStartChannel, colourBox1, colourBox2, holdColour;
		var holdIndex, holdVertBarButtonData, holdTop, holdLeft;
		var sigFlowBar, modSigFlowView, holdScrollview, holdMoveView, holdListView;

		//	initialise variables
		dataBank.arrScrollViews = [];
		dataBank.arrMoveViews = [];
		dataBank.arrUserViews = [];
		startChannel = startChannel.min(this.arrShowChannels.size-1).max(0);

		// create array of names of all system's source modules.
		arrAllSourceModules = 	system.arrSystemModules
		.select({ arg item, i;
			(item.class.moduleType == "source")
			or:
			(item.class.moduleType == "groupsource")
			or:
			(item.class.moduleType == "insert") ;    // allow inserts to be sources for channels
		})
		.sort({ arg a, b; a.instSortingName < b.instSortingName; });
		arrAllSourceModNames = arrAllSourceModules.collect({arg item, i;  item.instDisplayName; });

		// create array of names of all system's source, insert & action modules.
		arrAllSourceActionModules = system.arrSystemModules
		.select({ arg item, i;
			(item.class.moduleType == "source")
			or:
			(item.class.moduleType == "groupsource")
			or:
			(item.class.moduleType == "groupaction")
			or:
			(item.class.moduleType == "action")
			or:
			(item.class.moduleType == "insert") ;
		})
		.sort({ arg a, b; a.instSortingName < b.instSortingName; });
		arrAllSourceActionModNames = arrAllSourceActionModules
		.collect({arg item, i;  item.instDisplayName; });
		// create array of names of all system's source modules and busses.
		arrAllSourceModsBusses = (
			arrAllSourceModules
			++ system.arrFXSendBusses	// array of FX send busses
			++ system.arrAudioAuxBusses	// array of Audio Aux busses
			++ system.arrControlAuxBusses	// array of Control Aux busses
			++ system.arrMainOutBusses	// array of Main Out busses
		);
		arrAllSourceModBusNames = arrAllSourceModsBusses.collect({arg item, i;  item.instDisplayName; });

		// removed scrollview
		// dataBank.allModulesScrollView = ScrollView(parent,
		// Rect(0, 0, dataBank.allModulesBoxWidth, dataBank.boxHeight))
		// .hasBorder_(false);
		// dataBank.allModulesScrollView.background = TXColor.sysMainWindow;
		// dataBank.allModulesScrollView.action = {arg view; dataBank.allModulesVisibleOrigin = view.visibleOrigin;};
		// dataBank.arrScrollViews = dataBank.arrScrollViews.add(dataBank.allModulesScrollView);
		// dataBank.allModulesBox = CompositeView(dataBank.allModulesScrollView, Rect(0,0,
		// dataBank.allModulesBoxWidth, dataBank.boxHeight));
		dataBank.allModulesBox = CompositeView(parent, Rect(0,0, 186, dataBank.boxHeight));
		dataBank.allModulesBox.background = TXColor.sysMainWindow;
		dataBank.allModulesBox.decorator = FlowLayout(dataBank.allModulesBox.bounds);
		dataBank.allModulesBox.decorator.margin.x = 0;
		dataBank.allModulesBox.decorator.margin.y = 0;
		dataBank.allModulesBox.decorator.gap.x = 3;
		dataBank.allModulesBox.decorator.reset;

		TXChannelRoutingAddEditGui.makeGui(dataBank.allModulesBox, system, dataBank, arrAllSourceActionModules,
			arrAllSourceActionModNames, arrAllSourceModsBusses, arrAllSourceModBusNames, dataBank.allModulesData);
		//{dataBank.allModulesScrollView.visibleOrigin = dataBank.channelTitleVisibleOrigin;}.defer(0.05);

		holdVertBarButtonData = [
			[Color.black,
				{dataBank.allModulesBoxWidth = 1; system.showView;}],
			[TXColor.blue2,
				{dataBank.allModulesBoxWidth = 156; system.showView;}],
			[TXColor.orange.blend(TXColor.grey(0.6, 0.5)),
				{dataBank.allModulesBoxWidth = 186; system.showView;}]
		];
		parent.decorator.shift(dataBank.allModulesBoxWidth - 186, 0);
		allModulesBoxBar = this.addVertBar(parent, 'allModulesBoxBar', holdVertBarButtonData,
			dataBank.allModulesData.allModulesScrollView, dataBank.allModulesBox, 186);

		// decorator shift
		//parent.decorator.shift(4, 0);

		if (showSigFlow, {
			// display signal flow
			modSigFlowView = TXSignalFlow.makeGui(parent, dataBank.sigFlowBoxWidth, dataBank.boxHeight, false);
			dataBank.sigFlowScrollView = TXSignalFlow.classData.layoutsScrollView;
			dataBank.sigFlowBox = TXSignalFlow.classData.sigFlowBox;
			// dataBank.arrScrollViews = dataBank.arrScrollViews.add(dataBank.sigFlowScrollView);

			// adjust decorator
			parent.decorator.shift(dataBank.sigFlowBoxWidth - TXSignalFlow.classData.maxSigFlowBoxWidth, 0);

		}, {
			// display module gui
			// REMOVED FOR NOW:
			//if (system.arrSystemModules.size == 0, {showModuleBox = false});
			//if ((dataBank.multiWindow == false) and: (showModuleBox == true), {
			moduleScrollView = ScrollView(parent, Rect(0, 0, dataBank.moduleBoxWidth, dataBank.boxHeight))
			.hasBorder_(false);
			moduleScrollView.background = TXColor.sysMainWindow;
			moduleScrollView.action = {arg view; dataBank.moduleVisibleOrigin = view.visibleOrigin;};
			// dataBank.arrScrollViews = dataBank.arrScrollViews.add(moduleScrollView);
			if ( displayModule.notNil, {
				dataBank.moduleBoxMaxWidth = displayModule.class.guiWidth;
			});
			moduleBox = CompositeView(moduleScrollView, Rect(0, 0, dataBank.moduleBoxMaxWidth, 595));
			moduleBox.background = TXColor.sysChannelAudio;
			// defer or else doesn't work
			{moduleScrollView.visibleOrigin = dataBank.moduleVisibleOrigin;}.defer(0.05);

			// display module
			if ( displayModule.notNil, {
				if (displayModule.class.moduleRate == "control", {
					holdColour = TXColor.sysGuiCol2;
				},{
					holdColour = TXColor.sysGuiCol1;
				});
				moduleScrollView.background = TXColor.sysMainWindow;
				displayModule.openGui(moduleBox);
			},{
				// Modules list
				if (system.arrSystemModules.size > 0, {
					holdListView = TXListView(moduleBox, Rect(10, 10, 140, 500));
					holdListView.items = ["Select module..."]
					++ system.arrSystemModules.copy
					.sort({ arg a, b; a.instSortingName < b.instSortingName })
					.collect({arg item, i; item.instDisplayName;});
					holdListView.stringColor_(TXColour.sysGuiCol1).background_(TXColour.white);
					holdListView.action = { arg view;
						if (view.value > 0, {
							// change current display module to new one
							TXChannelRouting.displayModule =
							system.arrSystemModules.copy
							.sort({ arg a, b; a.instSortingName < b.instSortingName }).at(view.value - 1);
							system.addHistoryEvent; // add to history
							// refresh screen
							system.showView;
						});
					};
				});
			});
		});

		// decorator shift
		//parent.decorator.shift(4, 0);
		//});

		if (showSigFlow, {
			holdVertBarButtonData = [[Color.black, {dataBank.sigFlowBoxWidth = 1; system.showView;}],
				[TXColor.blue, {dataBank.sigFlowBoxWidth = 350; system.showView;}],
				[TXColor.blue2, {dataBank.sigFlowBoxWidth = 700; system.showView;}],
				[TXColor.orange.blend(TXColor.grey(0.6, 0.5)), {dataBank.sigFlowBoxWidth = 950; system.showView;}]
			];
			sigFlowBar = this.addVertBar(parent, 'sigFlowBar', holdVertBarButtonData,
				dataBank.sigFlowScrollView, dataBank.sigFlowBox);
		}, {
			holdVertBarButtonData = [[Color.black, {dataBank.moduleBoxWidth = 1; system.showView;}],
				[TXColor.blue, {dataBank.moduleBoxWidth = 250; system.showView;}],
				[TXColor.blue2, {dataBank.moduleBoxWidth = 500; system.showView;}],
				[TXColor.orange.blend(TXColor.grey(0.6, 0.5)),
					{dataBank.moduleBoxWidth = moduleBox.bounds.width; system.showView;}]
			];
			moduleBoxBar = this.addVertBar(parent, 'moduleBoxBar', holdVertBarButtonData,
				moduleScrollView, moduleScrollView);
		});

		// show channel row titles first
		// removed scrollview
		// dataBank.channelTitleScrollView = ScrollView(parent,
		// Rect(0, 0, dataBank.channelTitleBoxWidth, dataBank.boxHeight))
		// .hasBorder_(false);
		// dataBank.channelTitleScrollView.background = TXColor.sysMainWindow;
		// dataBank.channelTitleScrollView.action = {arg view; dataBank.channelTitleVisibleOrigin = view.visibleOrigin;};
		// dataBank.arrScrollViews = dataBank.arrScrollViews.add(dataBank.channelTitleScrollView);
		// dataBank.channelTitleBox = CompositeView(dataBank.channelTitleScrollView, Rect(0,0, dataBank.channelTitleBoxWidth, dataBank.boxHeight));
		dataBank.channelTitleBox = CompositeView(parent, Rect(0,0, 186, dataBank.boxHeight));
		dataBank.channelTitleBox.background = TXColor.sysMainWindow;
		dataBank.channelTitleBox.decorator = FlowLayout(dataBank.channelTitleBox.bounds);
		dataBank.channelTitleBox.decorator.margin.x = 0;
		dataBank.channelTitleBox.decorator.margin.y = 0;
		dataBank.channelTitleBox.decorator.gap.x = 3;
		dataBank.channelTitleBox.decorator.reset;

		//OLD: dataBank.channelTitleBox = TXChannel.makeTitleGui(dataBank.channelTitleScrollView);
		TXChannelRoutingAddEditGui.makeGui(dataBank.channelTitleBox, system, dataBank, arrAllSourceActionModules,
			arrAllSourceActionModNames, arrAllSourceModsBusses, arrAllSourceModBusNames, dataBank.channelTitleData);

		//{dataBank.channelTitleScrollView.visibleOrigin = dataBank.channelTitleVisibleOrigin;}.defer(0.05);

		holdVertBarButtonData = [
			[Color.black, {dataBank.channelTitleBoxWidth = 1; system.showView;}],
			[TXColor.blue2, {dataBank.channelTitleBoxWidth = 146; system.showView;}],
			[TXColor.orange.blend(TXColor.grey(0.6, 0.5)), {dataBank.channelTitleBoxWidth = 186; system.showView;}],
		];
		parent.decorator.shift(dataBank.channelTitleBoxWidth - 186, 0);
		channelTitleBoxBar = this.addVertBar(parent, 'channelTitleBoxBar', holdVertBarButtonData,
			dataBank.channelTitleData.channelTitleScrollView, dataBank.channelTitleBox, 186);

		dataBank.channelsScrollView = ScrollView(parent, Rect(0, 0, dataBank.channelBoxWidth, dataBank.boxHeight))
		.hasBorder_(false);
		dataBank.channelsScrollView.background = TXColor.sysMainWindow;
		dataBank.channelsScrollView.action = {arg view; dataBank.channelsVisibleOrigin = view.visibleOrigin;};
		// dataBank.arrScrollViews = dataBank.arrScrollViews.add(dataBank.channelsScrollView);
		channelBox = CompositeView(dataBank.channelsScrollView, Rect(0, 0, 4 + (this.arrShowChannels.size * (TXChannel.guiWidth + 12)), 595));
		channelBox.background = TXColor.sysMainWindow;
		channelBox.decorator = FlowLayout(channelBox.bounds);
		channelBox.decorator.margin.x = 0;
		channelBox.decorator.margin.y = 0;
		channelBox.decorator.reset;

		//totalChannels = this.arrShowChannels.size;
		this.arrShowChannels.do({ arg item, i;
			item.makeChannelGui(channelBox);
		});
		{dataBank.channelsScrollView.visibleOrigin = dataBank.channelsVisibleOrigin;}.defer(0.05);

		holdVertBarButtonData = [[Color.black, {dataBank.channelBoxWidth = 1; system.showView;}],
			[TXColor.blue, {dataBank.channelBoxWidth = 4 + (dataBank.channelWidth); system.showView;}],
			[TXColor.blue2, {dataBank.channelBoxWidth = 380; system.showView;}],
			[TXColor.orange.blend(TXColor.grey(0.6, 0.5)), {dataBank.channelBoxWidth = 4 + (5 * dataBank.channelWidth); system.showView;}]
		];
		channelBoxBar = this.addVertBar(parent, 'channelBoxBar', holdVertBarButtonData,
			dataBank.channelsScrollView, dataBank.channelsScrollView);
	} // end of method makeGui

	*addVertBar{ arg parent, name, holdVertBarButtonData, holdScrollview, holdMoveView, maxScrollViewWidth = 1000000;
		// e.g holdVertBarButtonData = [ [Color.black, {dataBank.allModulesBoxWidth = 1; system.showView;}] ];
		var backingView, bar, holdGapX;

		holdGapX = parent.decorator.gap.x;
		parent.decorator.shift(holdGapX.neg, 0); // remove gap
		dataBank.arrScrollViews = dataBank.arrScrollViews.add(holdScrollview);
		dataBank.arrMoveViews = dataBank.arrMoveViews.add(holdMoveView);
		// backing view
		backingView = UserView(parent, Rect(0, 0, 9 + (2 * holdGapX), dataBank.boxHeight)).resize_(5);
		dataBank.arrUserViews = dataBank.arrUserViews.add(backingView);
		backingView.background_(TXColor.sysMainWindow);
		parent.decorator.shift(holdGapX.neg, 0); // remove gap
		// bar on backing view
		bar = UserView(backingView, Rect(holdGapX, 0, 9, dataBank.boxHeight)).resize_(5);
		bar.background_(Color.white);
		bar.mouseDownAction = { |view,x,y|
			dataBank.startPointX = x;
			dataBank.holdViewIndex = dataBank.arrUserViews.indexOf(backingView);
		};
		bar.mouseMoveAction_({arg view, x, y;
			var newPointX, shiftX, validMove, holdScrollView, holdUserView;
			newPointX = x;
			validMove = false;
			shiftX = (newPointX - dataBank.startPointX);
			holdUserView = dataBank.arrUserViews[dataBank.holdViewIndex];

			// if negative shift
			if (shiftX < 0, {
				if ( dataBank.holdViewIndex == 0 and:
					{((holdUserView.bounds.left + shiftX) > 4)},
					{
						validMove = true;
				});
				if ( dataBank.holdViewIndex > 0
					and:
					{((holdUserView.bounds.left -
						dataBank.arrUserViews[dataBank.holdViewIndex-1].bounds.left + shiftX)) > 16},
					{
						validMove = true;
				});
			},{
				// if positive shift
				if ( dataBank.holdViewIndex == 0 and:
					{(parent.bounds.width -
						(holdUserView.bounds.left + holdUserView.bounds.width + shiftX)) < 1},
					{
						validMove = false;
					},{
						validMove = true;
				});
				// check for maxScrollViewWidth
				if (holdScrollview.notNil and: {(holdScrollview.bounds.width + shiftX) > maxScrollViewWidth}, {
						validMove = false;
				});
			});
			if (validMove, {
				dataBank.arrScrollViews.do({ arg currScrollView, currScrollViewNo;
					if (currScrollViewNo == dataBank.holdViewIndex, {
						if (currScrollView.notNil, {
							// set width  of current ScrollView bounds
							currScrollView.bounds = currScrollView.bounds.width_(
								(currScrollView.bounds.width + shiftX);
							);
						});
						// set left  of current UserView bounds
						holdUserView = dataBank.arrUserViews[currScrollViewNo];
						if (holdUserView.notNil, {
							holdUserView.bounds = holdUserView.bounds.left_(
								(holdUserView.bounds.left + shiftX)
							);
						});
					});
					//for views to the right, move left of bounds
					if (currScrollViewNo > dataBank.holdViewIndex, {
						dataBank.arrMoveViews[currScrollViewNo].bounds = dataBank.arrMoveViews[currScrollViewNo].bounds.left_(
							dataBank.arrMoveViews[currScrollViewNo].bounds.left + shiftX
						);
						holdUserView = dataBank.arrUserViews[currScrollViewNo];
						if (holdUserView.notNil, {
							holdUserView.bounds = holdUserView.bounds.left_(
								(holdUserView.bounds.left + shiftX);
							);
						});
					});
				});
				// adjust box size
				case
				{name == 'allModulesBoxBar'} {dataBank.allModulesBoxWidth = dataBank.allModulesBoxWidth + shiftX }
				{name == 'sigFlowBar'} {dataBank.sigFlowBoxWidth = dataBank.sigFlowBoxWidth + shiftX;}
				{name == 'moduleBoxBar'} {dataBank.moduleBoxWidth = dataBank.moduleBoxWidth + shiftX }
				{name == 'channelTitleBoxBar'} {dataBank.channelTitleBoxWidth = dataBank.channelTitleBoxWidth + shiftX }
				{name == 'channelBoxBar'} {dataBank.channelBoxWidth = dataBank.channelBoxWidth + shiftX }
				;
			});
		});
		// draw buttons
		// e.g holdVertBarButtonData =  [ [Color.black, {dataBank.allModulesBoxWidth = 1; system.showView;}] ];
		holdVertBarButtonData.do({arg btnData, i;
			var holdButton, holdOffset;
			holdButton = UserView(bar, Rect(1, 7 + (i * 11), 7, 7));
			holdButton.background_(btnData[0].blend(Color.grey(0.6), 0.34));
			holdButton.mouseDownAction = { |view,x,y|
				btnData[1].value;
				true; // prevent passing mouseDown to parent
			};

			holdOffset = dataBank.boxHeight - (11 * holdVertBarButtonData.size) - 7;
			holdButton = UserView(bar, Rect(1, holdOffset + (i * 11), 7, 7));
			holdButton.background_(btnData[0].blend(Color.grey(0.6), 0.34));
			holdButton.mouseDownAction = { |view,x,y|
				btnData[1].value;
				true; // prevent passing mouseDown to parent
			};
		});
		// return new bar
		^bar;

	} // end of method addVertBar

	*getSourceModulesForCurrentCategory {
		var holdInd, holdCat;
		holdInd = (dataBank.popNewModuleCatInd - 1).max(0);
		holdCat = dataBank.arrAllPossSourceCats[holdInd];
		^system.dataBank.arrSourceModulesByCategoryWithAlpha.select({arg item; item[0] == holdCat;});
	}

	*popNewModuleSetItems { arg popNewModuleView;
		var holdInd, holdCat, arrPossSourceActNames;
		holdInd = (dataBank.popNewModuleCatInd - 1).max(0);
		holdCat = dataBank.arrAllPossSourceCats[holdInd];
		arrPossSourceActNames = this.getSourceModulesForCurrentCategory.collect({arg item; item[1]});
		popNewModuleView.items = ["Select module ..."] ++ arrPossSourceActNames;
	}

	*setScrollToCurrentChannel { arg maxChannels = 0;
		var	holdIndex, holdMax, curChannelLeft;
		// if current channel not visible then move origin
		if (displayChannel.notNil, {
			holdIndex = (this.arrShowChannels.indexOf(displayChannel) ? 0).min(this.arrShowChannels.size-1);
			if (holdIndex.notNil, {
				holdMax = (this.arrShowChannels.size * (TXChannel.guiWidth + 10)) - dataBank.channelBoxWidth;
				curChannelLeft = (4 + (holdIndex * (TXChannel.guiWidth + 10))).min(holdMax);
				if ( (curChannelLeft < dataBank.channelsVisibleOrigin.x) or:
					(curChannelLeft > (dataBank.channelsVisibleOrigin.x +(maxChannels * (TXChannel.guiWidth + 10)))) , {
						//dataBank.channelsVisibleOrigin.x = curChannelLeft;
						dataBank.channelsVisibleOrigin = Point.new(curChannelLeft, 0);
				});
			});
		});
	}

	*setScrollToEndChannel {
		var indent;
		if (this.arrShowChannels.size > 0, {
			indent = ((this.arrShowChannels.size  * (TXChannel.guiWidth + 10)) - dataBank.channelBoxWidth).max(0);
			dataBank.channelsVisibleOrigin = Point.new(indent, 0);
		});
	}

	*setSelectionToEdit {
		dataBank.allModulesData.addEditSelection = "Edit Module";
	}

	*addModule {arg newModuleClass;
		var newModule, validClass;

		if ( (newModuleClass == TXFMSynth4) and: ('FM7'.asClass.isNil), {
			validClass = false;
			TXInfoScreen.new("Error - unable to find FM7 Plugin. Cannot create FM Synth module  ");
		},{
			validClass = true;
		});
		// first item has no effect
		if ( (newModuleClass.notNil) and: validClass, {
			Routine.run {
				var holdCondition;
				// create multiple copies
				dataBank.newModuleCopies.do({
					// add condition to load queue
					holdCondition = system.holdLoadQueue.addCondition;
					// pause
					holdCondition.wait;
					system.server.sync;
					// ask system to add new module
					newModule = system.addModule(newModuleClass);
					// remove condition from load queue
					system.holdLoadQueue.removeCondition(holdCondition);
				});
				// add condition to load queue
				holdCondition = system.holdLoadQueue.addCondition;
				// pause
				holdCondition.wait;
				// pause
				system.server.sync;
				// remove condition from load queue
				system.holdLoadQueue.removeCondition(holdCondition);
				// defer gui stuff
				{
					// set startChannel
					displayModule = newModule;
					showModuleBox = true;
					system.addHistoryEvent;
					// scroll to end
					this.setScrollToEndChannel;
					system.addHistoryEvent;
					// update view
					system.showView;
				}.defer;
			};
		});
	}

}
