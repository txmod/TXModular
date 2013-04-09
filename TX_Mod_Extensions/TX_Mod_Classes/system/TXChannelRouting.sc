// Copyright (C) 2010  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXChannelRouting {	// Channel Routing

classvar	<>system;	    			// parent system - set by parent
classvar <arrChannels;  		// array of channels created
classvar <>startChannel;   		// starting channel for view
classvar chanWidth = "Narrow";	// channel width
classvar arrControls;			// gui controls
classvar <>arrControlVals; 		// gui control values - (no longer used)
classvar multiWindow=false;		// whether multi-window mode is on
classvar <>displayModule; 		// shows current module shown in single window mode
classvar <>displayChannel; 		// shows current channel to be highlighted
classvar <>showModuleBox;		// whether module box is shown
classvar <>showChannelType = "all";
classvar popNewModuleInd;
classvar newModuleCopies;
classvar popNewChannelInd;
classvar newChannelCopies;
classvar channelsVisibleOrigin;
classvar modulesVisibleOrigin;
classvar showDelButtons;

*initClass{
	arrChannels = [];
	startChannel = 0;
	chanWidth = "Narrow";
	arrControls = nil;
	multiWindow = false;
	displayModule = nil;
	showModuleBox = false;
	newModuleCopies = 1;
	newChannelCopies = 1;
	channelsVisibleOrigin = Point.new(0,0);
	modulesVisibleOrigin = Point.new(0,0);
	showDelButtons = false;
}

////////////////////////////////////////////////////////////////////////////////////

*saveData {
	// this method returns an array of all data for saving with various components:
	// 0- string "TXModuleSaveData", 1- module class, 2- arrControlVals, 3- arrAllChannelData
	var arrData, arrAllChannelData;
	// collect saveData from  all modules
	arrAllChannelData = arrChannels.collect({ arg item, i; item.saveData; });
	arrData = ["TXModuleSaveData", this.class.asString, arrControlVals, arrAllChannelData];
	^arrData;
}

*loadData { arg arrData, holdLoadQueue, holdLastChanCondition;
	// this method updates all data by loading arrData. format:
	// 0- string "TXModuleSaveData", 1- module class, 2- arrControlVals, 3- arrAllChannelData
	var arrAllChannelData, holdSourceModuleID, holdSourceModule, newChannel;
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

		// recreate view
		system.showView;
	};
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
	arrChannels = arrChannels.add(newChannel);
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
		if (multiWindow == false, {
			displayModule = argModule;
			showModuleBox = true;
			system.addHistoryEvent;
			// update view
			system.showView;
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
			item.sendSynthToTail;
		});
	};
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

*makeGui{ arg parent;
	var popNewModule, btnAddModule, chkAutoOpen;
	var popNewChannel, btnAddChannel, popDisplayMod;
	var popNewModuleCopies, popNewChannelCopies;
	var popWidthOption, popDisplayOption, popSortOption, btnSortChannels, popMultiWindow, btnHideModule;
	var maxChannels, totalChannels, arrSelectedChannels;
	var arrAllPossSourceActClasses, arrAllPossSourceActNames, arrAllSourceModules, arrAllSourceModNames;
	var arrAllSourceModsBusses, arrAllSourceModBusNames;
	var channelsScrollView;
	var arrAllSourceActionModules, arrAllSourceActionModNames;
	var modListBox, listModules, listViewModules;
	var modListBoxWidth, plusMinusString, plusMinusActionFunc, holdView;
	var modulesScrollView, modulesBox, btnDelete;
	var numStartChannel, channelBox, colourBox1, colourBox2;

	//	initialise variables
	arrControls = [];
	startChannel = startChannel.min(this.arrShowChannels.size-1).max(0);

/* // OLDER CODE:
	// create array of names of all possible source modules.
	arrAllPossSourceActClasses = system.arrAllPossModules
		.select({ arg item, i;
			(item.moduleType == "source")
				or: (item.moduleType == "groupsource")
				or: (item.moduleType == "groupaction")
				or: (item.moduleType == "action");
		});  // only show source, groupsource, groupaction or action modules
	arrAllPossSourceActNames = arrAllPossSourceActClasses.collect({ arg item, i;
		var holdType;
		if (item.moduleType == "action", {
			holdType = "action";
		},{
			if (item.moduleType == "groupaction", {
				holdType = "action";
			},{
				holdType = item.moduleRate;
			});
		});
		item.defaultName + " ["++ holdType ++ "]";
	});
*/
	// create array of names of all possible source modules.
	arrAllPossSourceActClasses =
		system.dataBank.arrSourceModulesByCategory.collect({arg item; item[1]});
	arrAllPossSourceActNames =  system.dataBank.arrSourceModulesByCategory.collect({
		arg item;
		var holdType;
		if (item[1].notNil, {
			if (item[1].moduleType == "action", {
				holdType = " [action]";
			},{
				holdType = " [" ++ item[1].moduleRate ++ "]";
			});
		},{
			holdType ="";
		});
		item[0] ++ holdType;
	});

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
	arrAllSourceModNames = arrAllSourceModules.collect({arg item, i;  item.instName; });

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
		.collect({arg item, i;  item.instName; });
	// create array of names of all system's source modules and busses.
	arrAllSourceModsBusses = (
		arrAllSourceModules
		++ system.arrFXSendBusses	// array of FX send busses
		++ system.arrAudioAuxBusses	// array of Audio Aux busses
		++ system.arrControlAuxBusses	// array of Control Aux busses
		++ system.arrMainOutBusses	// array of Main Out busses
	);
	arrAllSourceModBusNames = arrAllSourceModsBusses.collect({arg item, i;  item.instName; });

//	// spacing
//	parent.decorator.shift(0, 5);

	// create colourBox1 to display selected window
	colourBox1 = CompositeView(parent, 436 @ 32).background_(TXColor.sysLabelBackground);
	colourBox1.decorator = FlowLayout(colourBox1.bounds);
	// popup - new module
	popNewModule = PopUpMenu(colourBox1, 190 @ 24).background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);
	popNewModule.items = ["Select module ..."] ++ arrAllPossSourceActNames;
	popNewModule.action = {|view|
		// store current data
		popNewModuleInd = view.value;
	};
	popNewModule.value = popNewModuleInd ? 0;
	arrControls = arrControls.add(popNewModule);

	// popup - new module copies
	popNewModuleCopies = PopUpMenu(colourBox1, 90 @ 24).background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);
	popNewModuleCopies.items = 20.collect({ arg item, i;
		"copies X " ++ (item + 1).asString;
	});
	popNewModuleCopies.action = {|view|
		// store current data
		newModuleCopies = view.value + 1;
		if (popNewModuleCopies.value>0, {
			popNewModuleCopies.background_(TXColor.paleYellow)
		},{
			popNewModuleCopies.background_(TXColor.white)
		});
	};
	popNewModuleCopies.value = (newModuleCopies ? 1) - 1;
	if (popNewModuleCopies.value>0, {
		popNewModuleCopies.background_(TXColor.paleYellow)
	},{
		popNewModuleCopies.background_(TXColor.white)
	});
	arrControls = arrControls.add(popNewModuleCopies);

	// button - Add new Source module
	btnAddModule = Button(colourBox1, 140 @ 24);
	btnAddModule.states = [["Add new module(s)", TXColor.white, TXColor.sysGuiCol1]];
	btnAddModule.action = {
		var newModuleClass, newModule;
		// set new module class
		newModuleClass = arrAllPossSourceActClasses.at(popNewModule.value - 1);
		// first item has no effect
			if ( (popNewModule.value > 0) and: (newModuleClass.notNil), {
			Routine.run {
				var holdCondition;
				// create multiple copies
				newModuleCopies.do({
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
					TXChannelRouting.setScrollToEndChannel;
					system.addHistoryEvent;
					// update view
					system.showView;
				}.defer;
			};
		});
	};

	// spacing
	parent.decorator.shift(24, 0);

	// button - hide module box
	if (showModuleBox == true, {
		btnHideModule = Button(parent, 134 @ 32);
		btnHideModule.states = [["Hide module box", TXColor.white, TXColor.sysGuiCol2]];
		btnHideModule.action = {
			if ( showModuleBox == true, {
				// reset variables
				showModuleBox = false;
				displayModule = nil;
				system.addHistoryEvent;
				// update view
				system.showView;
			});
		};
		arrControls = arrControls.add(btnHideModule);
	}, {
		// spacer
		StaticText(parent, 134 @ 24);
	});

	// spacing
	parent.decorator.shift(24, 0);
	// create colourBox2 to display selected window
	colourBox2 = CompositeView(parent, 436 @ 32).background_(TXColor.sysLabelBackground);
	colourBox2.decorator = FlowLayout(colourBox2.bounds);
	// popup - new channel
	popNewChannel = PopUpMenu(colourBox2, 190 @ 24).background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
	popNewChannel.items = ["Select channel source ..."] ++ arrAllSourceModBusNames;
	popNewChannel.action = {|view|
		// store current data
		popNewChannelInd  = view.value;
	};
	popNewChannel.value = popNewChannelInd ? 0;
	arrControls = arrControls.add(popNewChannel);

	// popup - new channel copies
	popNewChannelCopies = PopUpMenu(colourBox2, 90 @ 24).background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
	popNewChannelCopies.items = 20.collect({ arg item, i;
		"copies X " ++ (item + 1).asString;
	});
	popNewChannelCopies.action = {|view|
		// store current data
		newChannelCopies = view.value + 1;
	if (popNewChannelCopies.value>0, {
		popNewChannelCopies.background_(TXColor.paleYellow)
	},{
		popNewChannelCopies.background_(TXColor.white)
	});
	};
	popNewChannelCopies.value = (newChannelCopies ? 1) - 1;
	if (popNewChannelCopies.value>0, {
		popNewChannelCopies.background_(TXColor.paleYellow)
	},{
		popNewChannelCopies.background_(TXColor.white)
	});
	arrControls = arrControls.add(popNewChannelCopies);

	// button - add new Channel
	btnAddChannel = Button(colourBox2, 140 @ 24);
	btnAddChannel.states = [["Add new channel(s)", TXColor.white, TXColor.sysGuiCol1]];
	btnAddChannel.action = {
		// first item has no effect
		if (popNewChannel.value > 0, {
			// creat multiple copies
			newChannelCopies.do({
				// add new channel from source module
				this.addChannel(arrAllSourceModsBusses.at(popNewChannel.value - 1));
			});
			// scroll to end
			this.setScrollToEndChannel;
			// update view
			system.showView;
		});
	};

// Removed for now
//	// popup - open modules in multiple windows
//	popMultiWindow = PopUpMenu(parent, 110 @ 24).background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
//	popMultiWindow.items = [
//		"Single Window",
//		"Multiple Windows"
//	];
//	popMultiWindow.action = {|view|
//		// store current data to arrControlVals
//		arrControlVals.put(arrControls.indexOf(view), TXViewHolder.getData(view));
//		// store current value to classvar
//		if (view.value == 0, {multiWindow = false}, {multiWindow = true});
//		// update view
//		system.showView;
//	};
//	arrControls = arrControls.add(popMultiWindow);



	// spacer
	StaticText(parent, 24 @ 20);

	// popup - display option selector
	popDisplayOption  = PopUpMenu(parent, 200 @ 32)
		.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
	popDisplayOption.items = ["Show audio & control channels",
		"Show audio channels only",
		"Show control channels only"
	];
	popDisplayOption.action = {|view|
			// store current value to classvar and reset startChannel
		if (view.value == 0, {showChannelType = "all"; startChannel = 0; });
		if (view.value == 1, {showChannelType = "audio"; startChannel = 0; });
		if (view.value == 2, {showChannelType = "control"; startChannel = 0; });
		// update view
		system.showView;
	};
	// update value
	popDisplayOption.value = ['all', 'audio', 'control'].indexOf(showChannelType.asSymbol);
	arrControls = arrControls.add(popDisplayOption);

	// spacer
	StaticText(parent, 14 @ 20);

	// spacing
	parent.decorator.shift(0, 5);

	// spacer line
	parent.decorator.nextLine;

	if ( showDelButtons == false, {
		modListBoxWidth = 166;
		plusMinusString = "->";
		plusMinusActionFunc = {showDelButtons = true; system.showView;};
	},{
		modListBoxWidth = 200;
		plusMinusString = "<-";
		plusMinusActionFunc = {showDelButtons = false; system.showView;};
	});
	// make box
	modListBox =  CompositeView(parent, Rect(0,0, modListBoxWidth, 595));
	modListBox.background = TXColour.sysChannelAudio;
	modListBox.decorator = FlowLayout(modListBox.bounds);
	// Heading
	holdView = StaticText(modListBox, Rect(0,0, 122, 24));
	holdView.string = "System Modules";
	holdView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
	holdView.setProperty(\align,\center);
	// plus minus button
	holdView = Button(modListBox, 24 @ 24);
	holdView.states = [[plusMinusString, TXColor.white, TXColor.sysGuiCol1]];
	holdView.action = {plusMinusActionFunc.value;};

	modulesScrollView = ScrollView(modListBox, Rect(0,0, modListBoxWidth-8, 595-32))
		.hasBorder_(false);
	modulesScrollView.background = TXColor.sysModuleWindow;
	if (GUI.current.asSymbol == \cocoa, {
		modulesScrollView.autoScrolls_(false);
	});
	modulesScrollView.action = {arg view; modulesVisibleOrigin = view.visibleOrigin; };
	modulesBox = CompositeView(modulesScrollView,
		Rect(0,0, modListBoxWidth-20, (arrAllSourceActionModNames.size * 26).max(20)));
	modulesBox.background = TXColour.sysChannelAudio;
		modulesBox.decorator = FlowLayout(modulesBox.bounds);
	modulesBox.decorator.margin.x = 0;
	modulesBox.decorator.margin.y = 0;
	modulesBox.decorator.reset;

	arrAllSourceActionModNames.do({arg item, i;
		var holdModule, btnModule, btnDelete, stringCol, backCol, btnModuleCol;
		holdModule = arrAllSourceActionModules.at(i);
		if (holdModule.class.moduleRate == "audio", {
			btnModuleCol = TXColor.sysGuiCol1;
		},{
			btnModuleCol = TXColor.sysGuiCol2;
		});
		if (displayModule != holdModule, {
			stringCol = TXColor.white;
			backCol = btnModuleCol;
		},{
			stringCol = btnModuleCol;
			backCol = TXColor.white;
		});
		// button -  module
		btnModule = Button(modulesBox, 140 @ 20);
		btnModule.states = [[item, stringCol, backCol]];
		btnModule.action = {
			displayModule = arrAllSourceActionModules.at(i);
			showModuleBox = true;
			system.addHistoryEvent;
			system.showView;
		};
		if ( showDelButtons == true, {
			// button -  delete
			btnDelete = Button(modulesBox, 24 @ 20);
			btnDelete.states = [["Del", TXColor.white, TXColor.sysDeleteCol]];
			btnDelete.action = {
				arrAllSourceActionModules.at(i).confirmDeleteModule;
				system.showView;
			};
		});
	});
	modulesScrollView.visibleOrigin = modulesVisibleOrigin;

	// decorator shift
	parent.decorator.shift(10, 0);

	// display module gui
	if ( (multiWindow == false) and: (showModuleBox == true), {
		if ( displayModule.notNil, {
			// display module
			displayModule.openGui(parent);
		},{
			TextView(parent,Rect(0,0, 500, 595))
				.background_(TXColor.sysModuleWindow);
		});
		// decorator shift
		parent.decorator.shift(10, 0);
	});

	if ( (multiWindow == false) and: (showModuleBox == true), {
		maxChannels = 4;		// max no of channels to be displayed at a time
	},{
		maxChannels = 7;		// max no of channels to be displayed at a time
	});
	totalChannels = (this.arrShowChannels.size - startChannel).min(maxChannels);

	// if channels to show, show channel row titles first
	if (totalChannels > 0, {
		TXChannel.makeTitleGui(parent);
	});

	// show channels

/* NEW CODE FOR CHANNEL SCROLLING */
	channelsScrollView = ScrollView(parent, Rect(0, 0, 4 + (maxChannels * 151), 612))
		.hasBorder_(false);
	channelsScrollView.background = TXColor.sysMainWindow;
	if (GUI.current.asSymbol == \cocoa, {
		channelsScrollView.autoScrolls_(false);
	});
	channelsScrollView.action = {arg view; channelsVisibleOrigin = view.visibleOrigin; };
	channelBox = CompositeView(channelsScrollView, Rect(0, 0, 4 + ((this.arrShowChannels.size+3) * 151), 595));
	channelBox.background = TXColor.sysMainWindow;
	channelBox.decorator = FlowLayout(channelBox.bounds);
	channelBox.decorator.margin.x = 0;
	channelBox.decorator.margin.y = 0;
	channelBox.decorator.reset;

	totalChannels = this.arrShowChannels.size;
	totalChannels.do({ arg item, i;
		this.arrShowChannels.at(i).makeChannelGui(channelBox);
	});

	this.setScrollToCurrentChannel(maxChannels);
	channelsScrollView.visibleOrigin = channelsVisibleOrigin;
}

*setScrollToCurrentChannel { arg maxChannels;
	var	holdIndex, curChannelLeft;
	// if current channel not visible then move origin
	if (displayChannel.notNil, {
		holdIndex = this.arrShowChannels.indexOf(displayChannel);
		if (holdIndex.notNil, {
			curChannelLeft = 4 + (holdIndex * 151);
			if ( (curChannelLeft < channelsVisibleOrigin.x) or:
				(curChannelLeft > (channelsVisibleOrigin.x +(maxChannels * 151))) , {
					channelsVisibleOrigin.x = curChannelLeft;
				channelsVisibleOrigin = Point.new(curChannelLeft, 0);
			});
		});
	});
}

*setScrollToEndChannel {
	if (this.arrShowChannels.size > 3, {
		channelsVisibleOrigin = Point.new((this.arrShowChannels.size-3).max(0) * 151, 0);
	});
}

}
