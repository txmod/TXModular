// Copyright (C) 2013  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXPatternCode : TXModuleBase {

	classvar <>arrInstances;
	classvar <defaultName;  		// default module name
	classvar <moduleRate;			// "audio" or "control"
	classvar <moduleType;			// "source", "insert", "bus",or  "channel" or "action"
	classvar <noInChannels;			// no of input channels
	classvar <arrAudSCInBusSpecs; 	// audio side-chain input bus specs
	classvar <>arrCtlSCInBusSpecs; 	// control side-chain input bus specs
	classvar <noOutChannels;		// no of output channels
	classvar <arrOutBusSpecs; 		// output bus specs
	classvar	<guiWidth=700;

	var <>testMIDINote = 69;
	var <>testMIDIVel = 100;
	var <>testMIDITime = 1;
	var	displayOption;
	var dataEvent;
	var initFuncCompileStatus, startFuncCompileStatus, stopFuncCompileStatus;
	var initFuncCompileStatusView, startFuncCompileStatusView, stopFuncCompileStatusView;
	var initFunctionString, startFunctionString, stopFunctionString;
	var initFunctionCursorPos, startFunctionCursorPos, stopFunctionCursorPos;
	var initFunctionCompResult, startFunctionCompResult, stopFunctionCompResult;
	var codeTextView, codeHeight;
	var initFunctionHasRun = false;

	*initClass{
		arrInstances = [];
		//	set class specific variables
		defaultName = "Pattern Code";
		moduleRate = "control";
		moduleType = "action";
		noInChannels = 0;
		noOutChannels = 0;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	*syncStartAll {
		arrInstances.do({ arg item, i;
			item.syncStart;
		});
	}

	*syncStopAll {
		arrInstances.do({ arg item, i;
			item.syncStop;
		});
	}
	*stopAll {
		arrInstances.do({ arg item, i;
			item.runStopFunction;
		});
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showInitialiseCode";
		dataEvent = ();
		initFunctionCursorPos = startFunctionCursorPos = stopFunctionCursorPos = 0;
		initFunctionString =
		"// Example code \n" ++
		"{arg arrTXEvents, dataEvent; \n" ++
		"    dataEvent.stepNotes = [48, 50, 61, 55, 58, 60]; \n" ++
		"    dataEvent.stepLengths = [1,1,0.5,0.5,1]; \n" ++
		"}";
		startFunctionString =
		"// Example code \n" ++
		"{arg arrTXEvents, dataEvent; \n" ++
		"    // add text here \n" ++
		"}";
		stopFunctionString =
		"// Example code \n" ++
		"{arg arrTXEvents, dataEvent; \n" ++
		"    // add text here \n" ++
		"}";

		//	n.b. this module is using arrSynthArgSpecs just as a place to store variables for use with guiSpecArray
		//  it takes advantage of the  gui objects saving values to arrSynthArgSpecs as well as it being already
		//   saved and loaded with other data
		//	it is only for convenience, since no synths are used by this module - unlike most others
		arrSynthArgSpecs = [
			["showMidiKeyboard", 0],
			["showDurations", 0],
			["showPatterns", 0],
			["showEvents", 0],
			["displayModuleInd", 0],
			["syncStart", 0],
			["syncStop", 0],
		];
		guiSpecTitleArray = [
			["TitleBar"],
			["HelpButton"],
			["DeleteModuleButton"],
			["ActionButton", "Initialise",
			{this.runInitFunction;}, 60, nil, TXColor.sysGuiCol2],
			["ActionButton", "Start",
			{this.runStartFunction;}, 50, nil, TXColor.sysGuiCol2],
			["ActionButton", "Stop",
			{this.runStopFunction; }, 50, nil, TXColor.sysGuiCol2],
			// ["TXStaticText", "Status", {this.runningStatus},
			// {arg view; runningStatusView = view.textView}, 110, 40, TXColor.paleYellow2],
			["SeqSyncStartCheckBox"],
			["SeqSyncStopCheckBox"],
			["HideModuleButton"],
			["NextLine"],
			["ModuleActionPopup", 280],
			["ModuleInfoTxt", 380],
		];
		this.buildGuiSpecArray;
		arrActionSpecs = this.buildActionSpecs([
			["commandAction", "Run Initialise Code", {this.runInitFunction}],
			["commandAction", "Run Start Code", {this.runStartFunction}],
			["commandAction", "Run Stop Code", {this.runStopFunction}],
		]);
		//	use base class initialise
		this.baseInit(this, argInstName);
	}

	buildGuiSpecArray {
		var holdArray, holdModuleID, holdSystemEvents, holdModuleEvents, showTools;
		holdSystemEvents = system.arrPatternEvents;

		guiSpecArray = [
			["ActionButton", "Edit Initialise Code", {displayOption = "showInitialiseCode";
				this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayOption == "showInitialiseCode")],
			["Spacer", 3],
			["ActionButton", "Edit Start Code", {displayOption = "showStartCode";
				this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayOption == "showStartCode")],
			["Spacer", 3],
			["ActionButton", "Edit Stop Code", {displayOption = "showStopCode";
				this.buildGuiSpecArray; system.showView;}, 130,
				TXColor.white, this.getButtonColour(displayOption == "showStopCode")],
			["NextLine"],
		];
		if ((this.getSynthArgSpec("showDurations") == 1) or:
				(this.getSynthArgSpec("showMidiKeyboard") == 1),
			{
				codeHeight = 282;
			},
			{if ((this.getSynthArgSpec("showPatterns") == 1) or:
				(this.getSynthArgSpec("showEvents") == 1),
				{
					codeHeight = 210;
				},{
					codeHeight = 370;
				});
		});

		if (displayOption == "showInitialiseCode", {
			guiSpecArray = guiSpecArray ++ [
				["TextViewDisplay", "Enter Supercollider code in the window below. This needs to be a function which initialises any data needed for the pattern code to run. The function is passed 2 arguments: arrTXEvents - all available numerical events, dataEvent - where any data can be stored and shared between functions. Click the Store & compile code button after editing.", 650, 50, "Notes"],
				["TextViewCompile", {initFunctionString}, {arg argText; this.initFuncCompile(argText);}, 650, codeHeight, "Store & compile code",
					120, {arg view; codeTextView = view; codeTextView.select(initFunctionCursorPos, 0); }],
				["TXStaticText", "Status", {initFuncCompileStatus}, {arg view; initFuncCompileStatusView = view.textView}, 320, 50],
				// ["ActionButton", "Run code now", {this.runInitFunction;},
				// 120, TXColor.white, TXColor.sysGuiCol2],
			];
		});
		if (displayOption == "showStartCode", {
			guiSpecArray = guiSpecArray ++[
				["TextViewDisplay", "Enter Supercollider code in the window below. This needs to be a function which starts patterns running and stores any event streams as data. The function is passed 2 arguments: arrTXEvents - all available system events, dataEvent - where any data can be stored and shared between functions. Click the Store & compile code button after editing.", 650, 50, "Notes"],
				["TextViewCompile", {startFunctionString}, {arg argText; this.startFuncCompile(argText);}, 650, codeHeight, "Store & compile code",
					120, {arg view; codeTextView = view; codeTextView.select(startFunctionCursorPos, 0);}],
				["TXStaticText", "Status", {startFuncCompileStatus}, {arg view; startFuncCompileStatusView = view.textView}, 320, 50],
				// ["ActionButton", "Run code now", {this.runStartFunction;},
				// 120, TXColor.white, TXColor.sysGuiCol2],
			];
		});
		if (displayOption == "showStopCode", {
			guiSpecArray = guiSpecArray ++[
				["TextViewDisplay", "Enter Supercollider code in the window below. This needs to be a function which stops any running event streams. The function will be passed 2 arguments: arrTXEvents - all available system events, dataEvent - where any data can be stored and shared between functions. Click the Store & compile code button after editing.", 650, 50, "Notes"],
				["TextViewCompile", {stopFunctionString}, {arg argText; this.stopFuncCompile(argText);}, 650, codeHeight, "Store & compile code",
					120, {arg view; codeTextView = view; codeTextView.select(stopFunctionCursorPos, 0);}],
				["TXStaticText", "Status", {stopFuncCompileStatus}, {arg view; stopFuncCompileStatusView = view.textView}, 320, 50],
				// ["ActionButton", "Run code now", {this.runStopFunction;},
				// 120, TXColor.white, TXColor.sysGuiCol2],
			];
		});
		showTools = false;
		guiSpecArray = guiSpecArray ++ [
			["DividingLine"],
			["TextBar", "Text Insert Tools:", 120],
			["TXCheckBox", "Durations", "showDurations",
				{this.storeCurrentFunctionString;
					this.setSynthArgSpec("showMidiKeyboard",0); this.setSynthArgSpec("showPatterns",0);
					this.setSynthArgSpec("showEvents",0); this.buildGuiSpecArray; system.showView;}, 80, 20, 12],
			["TXCheckBox", "Midi Keyboard", "showMidiKeyboard",
				{this.storeCurrentFunctionString;
					this.setSynthArgSpec("showDurations",0); this.setSynthArgSpec("showPatterns",0);
					this.setSynthArgSpec("showEvents",0); this.buildGuiSpecArray; system.showView;}, 100, 20, 12],
			["TXCheckBox", "Patterns", "showPatterns",
				{this.storeCurrentFunctionString;
					this.setSynthArgSpec("showMidiKeyboard",0); this.setSynthArgSpec("showDurations",0);
					this.setSynthArgSpec("showEvents",0); this.buildGuiSpecArray; system.showView;}, 80, 20, 12],
			["TXCheckBox", "TX Events", "showEvents",
				{this.storeCurrentFunctionString;
					this.setSynthArgSpec("showMidiKeyboard",0); this.setSynthArgSpec("showDurations",0);
					this.setSynthArgSpec("showPatterns",0); this.buildGuiSpecArray; system.showView;}, 80, 20, 12],
			["NextLine"],
		];
		if (this.getSynthArgSpec("showMidiKeyboard") == 1, {
			showTools = true;
	// MIDIKeyboard
	// index1 is note play function to be valued with note as argument
	// index2 is the optional number of octaves to be shown on the keyboard
	// index3 is the optional height of the keyboard
	// index4 is the optional width of the keyboard
	// index5 is the optional lowest midi note of the keyboard
	// index6 is the optional note stop function to be valued with note as argument
	// index7 is the optional label string default: "Notes: C1 - B6"
	// index8 is the optional boolean: Show velocity slider, default: true
			guiSpecArray = guiSpecArray ++ [
				["MIDIKeyboard", {arg note; this.insertNoteInCode(note);},
					7, 50, nil, 24, {arg note; /*no off function needed*/}, "Notes: C0 - B6", false]
			];
		});
		if (this.getSynthArgSpec("showDurations") == 1, {
			showTools = true;
			guiSpecArray = guiSpecArray ++
			[["NextLine"],] ++
			[ "0", "1", "1/2", "1/3", "1/4", "1/5", "1/6", "1/7", "1/8", "1/9", "1/12","1/16","1/24","1/32","1/48","1/64"].collect({arg item, i;
				var string;
				string = item ++ ",";
				["ActionButton", string , {this.insertStringInCode(string); codeTextView.focus(true);},
					36, TXColor.black, TXColor.white]
			}) ++
			[["NextLine"],] ++
			["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "12", "16", "24", "32", "48", "64"].collect({arg item, i;
				var string;
				string = item ++ ",";
				["ActionButton", string , {this.insertStringInCode(string); codeTextView.focus(true);},
					36, TXColor.black, TXColor.white]
			});
		});
		if (this.getSynthArgSpec("showPatterns") == 1, {
			showTools = true;
			guiSpecArray = guiSpecArray ++ [
				["TXMultiButton", this.arrPatternData.collect({arg item, i; item[0] ++ " - " ++ item[1]}),
					{arg index; this.insertStringInCode(this.arrPatternData[index][0]); codeTextView.focus(true);},
					650, 172, 20, TXColor.sysGuiCol1, TXColor.white];
			];
		});
		if (this.getSynthArgSpec("showEvents") == 1, {
			showTools = true;
			holdModuleID = system.arrWidgetActionModules[this.getSynthArgSpec("displayModuleInd")].moduleID;
			if (system.getModuleFromID(holdModuleID) == 0, {
				this.setSynthArgSpec("displayModuleInd", 0);
				holdModuleID = system.arrWidgetActionModules[0].moduleID;
			});
			// build array of code inserts and descriptions
			holdArray = [];
			holdModuleEvents = system.arrPatternEvents(holdModuleID);
			holdModuleEvents[holdModuleID].do({arg event, eventNo;
				var codeInsert, description;
				description = event.moduleName ++ ": " ++ event.actionName ++ "(" ++ event.valCount ++ " args)";
				codeInsert = "arrTXEvents[" ++ holdModuleID.asString ++ "][" ++ eventNo.asString ++ "]";
				holdArray = holdArray.add([codeInsert, description]);
			});
			guiSpecArray = guiSpecArray ++ [
				// ["ActionPopup", system.arrWidgetActionModules.collect({arg item, i; item.instName;}),
				// 	{arg holdView; this.setSynthArgSpec("displayModuleInd", system.arrWidgetActionModules[holdView.value].moduleID);
				// this.buildGuiSpecArray; system.showView;}],
				["TXPopupActionPlusMinus", "Module", system.arrWidgetActionModules.collect({arg item, i; item.instName;}),
					"displayModuleInd", {arg holdView;
						this.setSynthArgSpec("displayModuleInd", holdView.value);
						this.buildGuiSpecArray; system.showView;}],
				["TXMultiButton", holdArray.collect({arg item, i; item[0] ++ " - " ++ item[1]}),
					{arg index; this.insertStringInCode(holdArray[index][0] ++ "/* " ++ holdArray[index][1] ++ "*/");
						codeTextView.focus(true);},
					650, 150, 20, TXColor.sysGuiCol1, TXColor.white]
			];

		});
		if (showTools == true, {
			guiSpecArray = guiSpecArray ++ [
				["NextLine"],
				["ActionButton", "Rest", {this.insertStringInCode("Rest");codeTextView.focus(true);}, 44, TXColor.black, TXColor.white],
				["ActionButton", "inf", {this.insertStringInCode("inf"); codeTextView.focus(true);}, 30, TXColor.black, TXColor.white],
				["ActionButton", "(", {this.insertStringInCode("("); codeTextView.focus(true);}, 24, TXColor.black, TXColor.white],
				["ActionButton", ")", {this.insertStringInCode(")"); codeTextView.focus(true);}, 24, TXColor.black, TXColor.white],
				["ActionButton", "[", {this.insertStringInCode("["); codeTextView.focus(true);}, 24, TXColor.black, TXColor.white],
				["ActionButton", "]", {this.insertStringInCode("]"); codeTextView.focus(true);}, 24, TXColor.black, TXColor.white],
				["ActionButton", ".", {this.insertStringInCode("."); codeTextView.focus(true);}, 24, TXColor.black, TXColor.white],
				["ActionButton", ",", {this.insertStringInCode(","); codeTextView.focus(true);}, 24, TXColor.black, TXColor.white],
				["ActionButton", ";", {this.insertStringInCode(";"); codeTextView.focus(true);}, 24, TXColor.black, TXColor.white],
				["ActionButton", "< space >", {this.insertStringInCode(" "); codeTextView.focus(true);}, 68, TXColor.black, TXColor.white],
				["ActionButton", "< tab >", {this.insertStringInCode("\t"); codeTextView.focus(true);}, 54, TXColor.black, TXColor.white],
				["ActionButton", "< new line >", {this.insertStringInCode("\n"); codeTextView.focus(true);}, 80, TXColor.black, TXColor.white],
				["ActionButton", "<- delete last character", {this.removeLastChar; codeTextView.focus(true);}, 140, TXColor.white, TXColor.sysDeleteCol],
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

	insertNoteInCode { arg note;
		var stringNote;
		stringNote = note.value.asString ++ "/*" ++ TXGetMidiNoteString(note) ++ "*/,";
		codeTextView.setString(stringNote, codeTextView.selectionStart, 0);
	}

	insertStringInCode { arg string;
		codeTextView.setString(string, codeTextView.selectionStart, 0);
	}

	removeLastChar {
		var holdStart, holdString;
		holdStart = codeTextView.selectionStart;
		if (holdStart > 0, {
			holdString = codeTextView.string.copy;
			holdString.removeAt(holdStart-1);
			codeTextView.string = holdString;
			codeTextView.select(holdStart-1, 0);
		});
	}

	storeInitFunctionString {arg string;
		initFunctionString = string;
		if (codeTextView.notNil, {
			initFunctionCursorPos = codeTextView.selectionStart + codeTextView.selectionSize;
		});
	}

	storeStartFunctionString {arg string;
		startFunctionString = string;
		if (codeTextView.notNil, {
			startFunctionCursorPos = codeTextView.selectionStart + codeTextView.selectionSize;
		});
	}

	storeStopFunctionString {arg string;
		stopFunctionString = string;
		if (codeTextView.notNil, {
			stopFunctionCursorPos = codeTextView.selectionStart + codeTextView.selectionSize;
		});
	}

	storeCurrentFunctionString {
		if (displayOption == "showInitialiseCode", {
			this.storeInitFunctionString(codeTextView.string);
		});
		if (displayOption == "showStartCode", {
			this.storeStartFunctionString(codeTextView.string);
		});
		if (displayOption == "showStopCode", {
			this.storeStopFunctionString(codeTextView.string);
		});
	}

	initFuncCompile {arg argText, updateViews = true;
		initFunctionHasRun = false;
		this.storeInitFunctionString(argText);
		initFunctionCompResult = argText.compile;
		if (initFunctionCompResult.isNil, {
			initFuncCompileStatus = "ERROR: Code cannot compile - see post window";
			if (updateViews, {initFuncCompileStatusView.string = initFuncCompileStatus;});
			},{
				initFuncCompileStatus = "Compiled OK.";
				if (updateViews, {initFuncCompileStatusView.string = initFuncCompileStatus});
		});
	}

	startFuncCompile {arg argText, updateViews = true;
		this.storeStartFunctionString(argText);
		startFunctionCompResult = argText.compile;
		if (startFunctionCompResult.isNil, {
			startFuncCompileStatus = "ERROR: Code cannot compile - see post window.";
			if (updateViews, {startFuncCompileStatusView.string = startFuncCompileStatus;});
			},{
				startFuncCompileStatus = "Compiled OK.";
				if (updateViews, {startFuncCompileStatusView.string = startFuncCompileStatus;});
		});
	}

	stopFuncCompile {arg argText, updateViews = true;
		this.storeStopFunctionString(argText);
		stopFunctionCompResult = argText.compile;
		if (stopFunctionCompResult.isNil, {
			stopFuncCompileStatus = "ERROR: Code cannot compile - see post window.";
			if (updateViews, {stopFuncCompileStatusView.string = stopFuncCompileStatus;});
			},{
				stopFuncCompileStatus = "Compiled OK.";
				if (updateViews, {stopFuncCompileStatusView.string = stopFuncCompileStatus;});
		});
	}

	runInitFunction {
		if (initFunctionCompResult.notNil, {
			initFunctionHasRun = true;
			initFunctionCompResult.value.value(TXSystem1.arrPatternEvents, dataEvent);
		});
	}

	runStartFunction {
		if (startFunctionCompResult.notNil, {
			if (initFunctionHasRun == false, {
				this.runInitFunction;
			});
			startFunctionCompResult.value.value(TXSystem1.arrPatternEvents, dataEvent);
		});
	}

	runStopFunction {
		if (stopFunctionCompResult.notNil, {
			stopFunctionCompResult.value.value(TXSystem1.arrPatternEvents, dataEvent);
		});
	}

	syncStart {
		// if syncStart is 1 then start
		if (this.getSynthArgSpec("syncStart") == 1, {
			this.runStartFunction;
			// system.showViewIfModDisplay(this);
		});
	}

	syncStop {
		// if syncStop is 1 then stop
		if (this.getSynthArgSpec("syncStop") == 1, {
			this.runStopFunction;
			// system.showViewIfModDisplay(this);
		});
	}

	extraSaveData { // override default method
		^[initFunctionString, startFunctionString, stopFunctionString];
	}

	loadExtraData {arg argData;  // override default method
		// compile strings
		this.initFuncCompile(argData.at(0), false);
		this.runInitFunction;
		this.startFuncCompile(argData.at(1), false);
		this.stopFuncCompile(argData.at(2), false);
	}

	rebuildSynth {
		// override base class method
	}

	// pattern data - [[code string, description]...]
	arrPatternData { ^[
		// ["", ">Primary Patterns"],
		["Pseq(list, repeats, offset)", "Play through the entire list 'repeats' times. "],
		["Prand(list, repeats)", "Choose items from the list randomly."],
		["Pxrand(list, repeats)", "Choose randomly, but never repeat the same item twice."],
		["Pshuf(list, repeats)", "Shuffle list in random order, and use same random order 'repeats' times."],
		["Pwrand(list, weights, repeats)", "Choose randomly by weighted probabilities."],
		["Pseries(start, step, length)", "Arithmetic series (addition)."],
		["Pgeom(start, grow, length)", "Geometric series (multiplication)."],
		["Pfunc(nextFunc, resetFunc)", "Get stream values from a user-supplied function."],
		["Pfuncn(func, repeats)", "Get values from function, but stop after 'repeats' items."],
		["Proutine(routineFunc)", "Use function like a routine."],
		// ["", ">Additional List Patterns"],
		["Pser(list, repeats, offset)", "Play through list as needed, but output only 'repeats' items."],
		["Pslide(list, repeats, len, step, start, wrapAtEnd)", "Play overlapping segments from list."],
		["Pwalk(list, stepPattern, directionPattern, startPos)", "Random walk over list."],
		["Place(list, repeats, offset)", "Interlace any arrays found in main list."],
		["Ppatlace(list, repeats, offset)", "Interlace any patterns found in main list."],
		["Ptuple(list, repeats)", "Collect list items into an array as return value."],
		// ["", ">Random numbers and probability distributions"],
		["Pwhite(lo, hi, length)", "Produces 'length' random numbers with equal distribution."],
		["Pexprand(lo, hi, length)", "Random numbers with exponential distribution, favoring lower numbers."],
		["Pbrown(lo, hi, step, length)", "Brownian motion"],
		["Pgbrown(lo, hi, step, length)", "Brownian motion on a geometric scale."],
		["Pbeta(lo, hi, prob1, prob2, length)", "Beta distribution"],
		["Pcauchy(mean, spread, length)", "Cauchy distribution."],
		["Pgauss(mean, dev, length)", "Guassian (normal) distribution."],
		["Phprand(lo, hi, length)", "Returns greater of two equal-distribution random numbers."],
		["Plprand(lo, hi, length)", "Returns lesser of two equal-distribution random numbers."],
		["Pmeanrand(lo, hi, length)", "Returns average of two equal-distribution random numbers."],
		["Ppoisson(mean, length)", "Poisson distribution."],
		["Pprob(distribution, lo, hi, length, tableSize)", "Random numbers with an arbitrary distribution."],
		// ["", ">Miscellaneous calculation patterns"],
		["Pavaroh(pattern, aroh, avaroh, stepsPerOctave)", "Convert scale degrees to note numbers. "],
		["PdegreeToKey(pattern, scale, stepsPerOctave)", "Convert scale degree pattern to note numbers."],
		["Pdiff(pattern)", "Returns difference between source stream's latest and previous values. "],
		["Prorate(proportion, pattern)", "Splits up number from 'pattern' according to 'proportion' pattern."],
		// ["", ">Repetition and Constraint patterns"],
		["Pclutch(pattern, connected)", "Controlled repitition. "],
		["Pn(pattern, repeats)", "Embeds source pattern 'repeats' times - simple repetition."],
		["Pstutter(n, pattern)", "'n' and 'pattern' can both be patterns, values repeated 'n' times."],
		["PdurStutter(n, pattern)", "Like Pstutter, except pattern value is divided by number of repeats."],
		["Pfin(count, pattern)", "Returns exactly 'count' values from source pattern, then stops."],
		["Pconst(sum, pattern, tolerance)", "Output numbers until sum reaches a predefined limit."],
		["Pfindur(dur, pattern, tolerance)", "Like Pconst, but  'constrain' event's rhythmic values."],
		["Psync(pattern, quant, maxdur, tolerance)", "Like Pfindur, but does not have a fixed duration limit."],
		// ["", ">Time-based patterns"],
		["Ptime(repeats)", "Returns amount of time elapsed since embedding."],
		["Pstep(levels, durs, repeats)", "Repeat 'level' for corresponding duration, then move to next."],
		["Pseg(levels, durs, curves, repeats)", "Like Pstep, but interpolates to next value instead."],
		// ["", ">Related", "Use of Env as a pattern."],
		// ["", ">Adding values into event patterns (Or, 'Pattern Composition')"],
		["Pbindf(pattern, pairs)", "Adds new key-value pairs onto a pre-existing Pbind-style pattern."],
		["Pchain(patterns)", "Chains separate Pbind-style patterns together into same event."],
		["Pset(name, value, pattern)", "See help file."],
		["Padd(name, value, pattern)", "See help file."],
		["Pmul(name, value, pattern)", "See help file."],
		// ["", ">Parallelizing event patterns"],
		["Ppar(list, repeats)", "Start each of event patterns in 'list' at same time."],
		["Ptpar(list, repeats)", "See help file."],
		["Pgpar(list, repeats)", "Like Ppar, but it creates a separate group for each subpattern."],
		["Pspawner(routineFunc)", "The function is used to make a routine. "],
		["Pspawn(pattern, spawnProtoEvent)", "Like Pspawner, but uses a pattern to control Spawner."],
		// ["", ">Language control methods"],
		["Pif(condition, iftrue, iffalse, default)", "Evaluates pattern 'condition' that returns true or false."],
		["Pseed(randSeed, pattern)", "Sets the random seed before embedding 'pattern'."],
		["Pprotect(pattern, func)", "Like the 'protect' error handling method."],
		["Ptrace(pattern, key, printStream, prefix)", "For debugging, Ptrace prints every return value."],
		["Pwhile(func, pattern)", "Like while, as long as function evaluates to true, pattern is embedded."],
		// ["", ">Server control methods"],
		["Pbus(pattern, dur, fadeTime, numChannels, rate)", "Creates private group & bus for synths played by pattern."],
		["Pgroup(pattern)", "Creates a private group (without private bus) for pattern's synths."],
		["Pproto(makeFunction, pattern, cleanupFunc)", "See help file."],
		// ["", ">Data sharing"],
		["Pkey(key)", "Read 'key' in input event, making previously-calculated values available for other streams."],
		["Penvir(envir, pattern, independent)", "Run pattern inside a given environment."],
		["Pfset(func, pattern)", "Assign default values into input event before getting event out of given pattern."],
		["Plambda(pattern, scope)", "See help file."],
		];
	}
}

