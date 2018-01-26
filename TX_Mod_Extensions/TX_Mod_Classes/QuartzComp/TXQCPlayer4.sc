// Copyright (C) 2008  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXQCPlayer4 : TXModuleBase {		// Quartz Composition Player
	classvar <>classData;

	var	<>qcFileNameView;
	var	<>qcFileName = "";
	var	arrPResponders;
	var	holdQCWindow, holdQCView;
	var	screenWidth, screenHeight;
	var	screenPosX, screenPosY;
	var arrInputs, arrSendTrigIDs, arrQCArgData, arrNumArgNames;
	var displayOption;
	var holdScreenSizes, holdScreenSizeTexts, holdSSPresetActions;
	var holdBorderSizes, holdBorderSizeTexts, holdBorderSizePresetActions;
	var holdVisibleOrigin;
	var holdScrollView;
	var currenti_screenWidth, currenti_screenHeight, currenti_posX, currenti_posY, currenti_borderX,
	currenti_borderY, currenti_maxFPS, currenti_winControls, currentwindowName, currentscreenWidth, currentscreenHeight;


	*initClass{
		//	set class specific variables
		classData = ();
		classData.arrInstances = [];
		classData.defaultName = "Quartz Player";
		classData.moduleRate = "control";
		classData.moduleType = "action";
		classData.noInChannels = 2;
		classData.maxParameters = 80;    // allow for 80 input parameters for a QC composition
		classData.arrPStrings = classData.maxParameters.collect({arg item; "p" ++ ("00" ++ item.asString).keep(-3)});
		classData.arrModPStrings = classData.arrPStrings.deepCopy.collect({arg item, i; "mod" ++ item});
		classData.arrActivePStrings = classData.arrPStrings.deepCopy.collect({arg item, i; "i_active" ++ item});
		classData.arrCtlSCInBusSpecs = classData.arrModPStrings.deepCopy.collect({arg item, i; [item.asString, 1, item, 0]});

		classData.noOutChannels = 0;
		classData.arrOutBusSpecs = [];
		classData.guiWidth = 700;
	}

	*new{ arg argInstName;
		^super.new.init(argInstName);
	}

	init {arg argInstName;
		//	set  class specific instance variables
		displayOption = "showGlobalSettings";
		arrInputs = [];
		holdVisibleOrigin = Point.new(0,0);

		arrQCArgData = [0, "", 0, 1, 0.5, 0.5, 0.5, 1, 0].dup(classData.maxParameters);
		//  array of :  argDataType, argStringVal, argMin, argMax, argRed, argGreen, argBlue, argAlpha, argNumVal
		//  argDataType can be: [0.notPassed, 1.float, 2.integer, 3.string, 4.booleanNum(0/1), 5.colour(RGBA),
		//	6.Directory Name, 7.File Name],

		arrSendTrigIDs = [];
		// create unique ids
		classData.maxParameters.do({arg item, i;
			arrSendTrigIDs = arrSendTrigIDs.add(UniqueID.next);
		});
		screenWidth = 1024;
		screenHeight = 768;
		screenPosX = 0;
		screenPosY = 60;

		arrSynthArgSpecs =
		[	["out", 0, 0],
			["i_screenWidth", 1024, 0],
			["i_screenHeight", 768, 0],
			["i_posX", 0, 0],
			["i_posY", 60, 0],
			["i_borderX", 20, 0],
			["i_borderY", 20, 0],
			["i_maxFPS", 0, 0],
			["i_winControls", 1, 0],
			["i_showQuartzWindow", 1, 0],
			["windowName", " ", 0],		]
		++ classData.arrActivePStrings.collect({arg item, i; [item, 0, 0]})
		++ classData.arrPStrings.collect({arg item, i; [item, 0, 0]})
		++ classData.arrModPStrings.collect({arg item, i; [item, 0, 0]});

		synthDefFunc = {
			arg out, i_screenWidth, i_screenHeight, i_posX, i_posY, i_borderX, i_borderY, i_maxFPS, i_winControls, i_showQuartzWindow,
			windowName,
			i_activep000, i_activep001, i_activep002, i_activep003, i_activep004, i_activep005, i_activep006, i_activep007, i_activep008, i_activep009, i_activep010, i_activep011, i_activep012, i_activep013, i_activep014, i_activep015, i_activep016, i_activep017, i_activep018, i_activep019, i_activep020, i_activep021, i_activep022, i_activep023, i_activep024, i_activep025, i_activep026, i_activep027, i_activep028, i_activep029, i_activep030, i_activep031, i_activep032, i_activep033, i_activep034, i_activep035, i_activep036, i_activep037, i_activep038, i_activep039, i_activep040, i_activep041, i_activep042, i_activep043, i_activep044, i_activep045, i_activep046, i_activep047, i_activep048, i_activep049, i_activep050, i_activep051, i_activep052, i_activep053, i_activep054, i_activep055, i_activep056, i_activep057, i_activep058, i_activep059, i_activep060, i_activep061, i_activep062, i_activep063, i_activep064, i_activep065, i_activep066, i_activep067, i_activep068, i_activep069, i_activep070, i_activep071, i_activep072, i_activep073, i_activep074, i_activep075, i_activep076, i_activep077, i_activep078, i_activep079,
			p000, p001, p002, p003, p004, p005, p006, p007, p008, p009, p010, p011, p012, p013, p014, p015, p016, p017, p018, p019, p020, p021, p022, p023, p024, p025, p026, p027, p028, p029, p030, p031, p032, p033, p034, p035, p036, p037, p038, p039, p040, p041, p042, p043, p044, p045, p046, p047, p048, p049, p050, p051, p052, p053, p054, p055, p056, p057, p058, p059, p060, p061, p062, p063, p064, p065, p066, p067, p068, p069, p070, p071, p072, p073, p074, p075, p076, p077, p078, p079,
			modp000=0, modp001=0, modp002=0, modp003=0, modp004=0, modp005=0, modp006=0, modp007=0, modp008=0, modp009=0, modp010=0, modp011=0, modp012=0, modp013=0, modp014=0, modp015=0, modp016=0, modp017=0, modp018=0, modp019=0, modp020=0, modp021=0, modp022=0, modp023=0, modp024=0, modp025=0, modp026=0, modp027=0, modp028=0, modp029=0, modp030=0, modp031=0, modp032=0, modp033=0, modp034=0, modp035=0, modp036=0, modp037=0, modp038=0, modp039=0, modp040=0, modp041=0, modp042=0, modp043=0, modp044=0, modp045=0, modp046=0, modp047=0, modp048=0, modp049=0, modp050=0, modp051=0, modp052=0, modp053=0, modp054=0, modp055=0, modp056=0,  modp057=0, modp058=0, modp059=0, modp060=0, modp061=0, modp062=0, modp063=0, modp064=0, modp065=0, modp066=0, modp067=0, modp068=0, modp069=0, modp070=0, modp071=0, modp072=0, modp073=0, modp074=0, modp075=0, modp076=0, modp077=0, modp078=0, modp079=0;

			var arrParmArgs, arrModParmArgs, arrActiveArgs;
			var arrParmSums, arrTrigs, arrSendTrigs, imp;

			arrParmArgs = [
				p000, p001, p002, p003, p004, p005, p006, p007, p008, p009, p010, p011, p012, p013, p014, p015, p016, p017, p018, p019, p020, p021, p022, p023, p024, p025, p026, p027, p028, p029, p030, p031, p032, p033, p034, p035, p036, p037, p038, p039, p040, p041, p042, p043, p044, p045, p046, p047, p048, p049, p050, p051, p052, p053, p054, p055, p056, p057, p058, p059, p060, p061, p062, p063, p064, p065, p066, p067, p068, p069, p070, p071, p072, p073, p074, p075, p076, p077, p078, p079];

			arrModParmArgs = [
				modp000, modp001, modp002, modp003, modp004, modp005, modp006, modp007, modp008, modp009, modp010, modp011, modp012, modp013, modp014, modp015, modp016, modp017, modp018, modp019, modp020, modp021, modp022, modp023, modp024, modp025, modp026, modp027, modp028, modp029, modp030, modp031, modp032, modp033, modp034, modp035, modp036, modp037, modp038, modp039, modp040, modp041, modp042, modp043, modp044, modp045, modp046, modp047, modp048, modp049, modp050, modp051, modp052, modp053, modp054, modp055, modp056,  modp057, modp058, modp059, modp060, modp061, modp062, modp063, modp064, modp065, modp066, modp067, modp068, modp069, modp070, modp071, modp072, modp073, modp074, modp075, modp076, modp077, modp078, modp079];

			arrActiveArgs = [
				i_activep000, i_activep001, i_activep002, i_activep003, i_activep004, i_activep005, i_activep006, i_activep007, i_activep008, i_activep009, i_activep010, i_activep011, i_activep012, i_activep013, i_activep014, i_activep015, i_activep016, i_activep017, i_activep018, i_activep019, i_activep020, i_activep021, i_activep022, i_activep023, i_activep024, i_activep025, i_activep026, i_activep027, i_activep028, i_activep029, i_activep030, i_activep031, i_activep032, i_activep033, i_activep034, i_activep035, i_activep036, i_activep037, i_activep038, i_activep039, i_activep040, i_activep041, i_activep042, i_activep043, i_activep044, i_activep045, i_activep046, i_activep047, i_activep048, i_activep049, i_activep050, i_activep051, i_activep052, i_activep053, i_activep054, i_activep055, i_activep056, i_activep057, i_activep058, i_activep059, i_activep060, i_activep061, i_activep062, i_activep063, i_activep064, i_activep065, i_activep066, i_activep067, i_activep068, i_activep069, i_activep070, i_activep071, i_activep072, i_activep073, i_activep074, i_activep075, i_activep076, i_activep077, i_activep078, i_activep079];

			arrParmSums = arrParmArgs.collect({arg item, i;
				arrActiveArgs.at(i) * (item + arrModParmArgs.at(i)).max(0).min(1);
			});

			// testing - try lower data rate for lower cpu load on complex quartz comps
			imp = (1 - Impulse.kr(30));
			//		imp = (1 - Impulse.kr(20));
			//		imp = (1 - Impulse.kr(10));

			arrTrigs = arrParmSums.collect({arg item, i;
				Trig.kr(imp * HPZ1.kr(item).abs, 0.02);
			});

			arrSendTrigs = arrTrigs.collect({arg item, i;
				SendTrig.kr(Impulse.kr(1) + item, arrSendTrigIDs.at(i), arrParmSums.at(i));
			});

			// Note this synth doesn't need to write to the output bus
		};
		// End of synth def function

		holdScreenSizes = [ [320, 240, " - 4:3 ratio"],  [640, 480, " - 4:3 ratio"],
			[720, 480, ""], [800, 500, ""],
			[800, 600, " - 4:3 ratio"], [1024, 640, ""], [1024, 768, " - 4:3 ratio"],
			[1152, 720, ""], [1280, 720, " - 16:9 ratio"], [1280, 800, ""], [1440, 900, ""],
			[1600, 900, " - 16:9 ratio"], [1920, 1080, " - 16:9 ratio"],
		];
		holdScreenSizeTexts = holdScreenSizes.collect({arg item, i;
			item.at(0).asString + "X" + item.at(1).asString + item.at(2)
		});
		holdSSPresetActions = holdScreenSizes.collect({arg item, i;
			{	this.setSynthArgSpec("i_screenWidth", item.at(0));
				this.setSynthArgSpec("i_screenHeight", item.at(1));
				screenWidth = item.at(0);
				screenHeight = item.at(1);
				this.resetScreenSize;
				this.oscActivate;
			}
		});

		holdBorderSizes = [ [0, 0], [2, 2], [5, 5], [10, 10], [20, 20],
			[30, 30], [50, 50] , [100, 100] , [200, 200] ];
		holdBorderSizeTexts = holdBorderSizes
		.collect({arg item, i; item.at(0).asString + "X" + item.at(1).asString});
		holdBorderSizePresetActions = holdBorderSizes.collect({arg item, i;
			{	this.setSynthArgSpec("i_borderX", item.at(0));
				this.setSynthArgSpec("i_borderY", item.at(1));
				this.resetScreenSize;
				this.oscActivate;
			}
		});

		this.buildGuiSpecArray;
		this.setActionSpecs;

		// initialise
		this.baseInit(this, argInstName);
		//	load the synthdef and create the synth
		this.loadAndMakeSynth;
		this.resetScreenSize;
		this.oscActivate;
	}

	buildGuiSpecArray {
		if (qcFileName == "", {
			guiSpecArray = [
				["TextBar", "Please select Quartz Composer file to be displayed", 450],
				["SpacerLine", 6],
				["ActionButtonBig", "Open file", {this.openQCComp; system.showView}],
			];
		}, {
			guiSpecArray = [
				["SpacerLine", 1],
				["ActionButton", "Global Settings", {displayOption = "showGlobalSettings";
					this.buildGuiSpecArray; system.showView;}, 160,
				TXColor.white, this.getButtonColour(displayOption == "showGlobalSettings")],
				["Spacer", 3],
				["ActionButton", "Controls", {displayOption = "showAllControls";
					this.buildGuiSpecArray; system.showView;}, 160,
				TXColor.white, this.getButtonColour(displayOption == "showAllControls")],
				["DividingLine"],
				["SpacerLine", 6],
			];
			if (displayOption == "showGlobalSettings", {
				guiSpecArray = guiSpecArray ++[
					["TXCheckBox", "Show Quartz Window", "i_showQuartzWindow",
						{this.showOrHideQCScreen; }, 200, 40],
					["SpacerLine", 8],
					["TXStaticText", "Quartz File", {this.qcFileName},
						{arg view; qcFileNameView = view.textView}],
					["SpacerLine", 2],
					["ActionButtonDark", "Replace current Quartz file", {
						// set window on
						this.setSynthArgSpec("i_showQuartzWindow", 1);
						// close current window first
						this.closeQCWindow;
						this.openQCComp;
					}, 160],
					["ActionButtonDark", "Replace Quartz file but keep current control settings", {
						// set window on
						this.setSynthArgSpec("i_showQuartzWindow", 1);
						// close current window first
						this.closeQCWindow;
						this.openQCComp(keepSettings: true);
					}, 320],
					["SpacerLine", 8],
					["TXTextBox", "Window name", "windowName", {this.resetScreenSize; this.oscActivate;}, 322],
					["SpacerLine", 8],
					["EZNumber", "Wind. width", ControlSpec(0, 10000, 'lin', 1, 0), "i_screenWidth",
						{arg view; screenWidth = view.value; this.resetScreenSize; this.oscActivate;}],
					["EZNumber", "Wind. height", ControlSpec(0, 10000, 'lin', 1, 0), "i_screenHeight",
						{arg view; screenHeight = view.value; this.resetScreenSize; this.oscActivate;}],
					["TXPresetPopup", "Presets", holdScreenSizeTexts, holdSSPresetActions, 110],
					["SpacerLine", 8],
					["EZNumber", "Wind. pos X", ControlSpec(0, 10000, 'lin', 1, 0), "i_posX",
						{arg view; screenPosX = view.value; this.resetScreenSize; this.oscActivate;}],
					["EZNumber", "Wind. pos Y", ControlSpec(0, 10000, 'lin', 1, 0), "i_posY",
						{arg view; screenPosY = view.value; this.resetScreenSize; this.oscActivate;}],
					["ActionButton", "Sample Current Position", {
						var holdRect;
						if (holdQCWindow.notNil, {
							holdRect = holdQCWindow.bounds;
							screenPosX = holdRect.left;
							screenPosY = holdRect.top;
							this.setSynthValue("i_posX", screenPosX);
							this.setSynthValue("i_posY", screenPosY);
							system.flagGuiUpd;
						});
					}, 160],
					["ActionButton", "Default Window Position", {arg view;
						screenPosX = 0; screenPosY = 60;
						this.setSynthValue("i_posX", 0); this.setSynthValue("i_posY", 60);
						this.resetScreenSize; this.oscActivate;
						system.flagGuiUpd;
					}, 160, TXColor.white, TXColor.sysGuiCol2],
					["SpacerLine", 8],
					["EZNumber", "Border X", ControlSpec(0, 2000, 'lin', 1, 0), "i_borderX",
						{arg view; this.resetScreenSize; this.oscActivate;}],
					["EZNumber", "Border Y", ControlSpec(0, 2000, 'lin', 1, 0), "i_borderY",
						{arg view; this.resetScreenSize; this.oscActivate;}],
					["TXPresetPopup", "Presets", holdBorderSizeTexts, holdBorderSizePresetActions, 110],
					["SpacerLine", 8],
					["EZNumber", "Max FPS", ControlSpec(0, 80, 'lin', 1, 0), "i_maxFPS",
						{arg view; this.setFPS(view.value);}],
					["TextBarLeft", " set to 0 for unrestricted Frames Per Second", 256],
					["SpacerLine", 8],
					["TXCheckBox", "Show Window Controls", "i_winControls", {this.rebuildQCScreen; }, 250],
				];
			});

			if (displayOption == "showAllControls", {
				guiSpecArray = guiSpecArray = guiSpecArray ++ [
					["TXQCArgGuiScroller", {arrInputs}, {classData.arrPStrings}, {classData.arrActivePStrings}, {arrQCArgData},
						{arg item; this.sendArgValue(item);},
						{arg view; holdScrollView = view;},
						{arg view; holdVisibleOrigin = view.visibleOrigin; },
					]
				];
			});


		});

	}

	setActionSpecs {
		var arrSpecs;

		arrSpecs = [
			["TXCheckBox", "Show Quartz Window", "i_showQuartzWindow",
				{this.showOrHideQCScreen; }, 200, 40],
			["EZNumber", "Window width", ControlSpec(0, 10000, 'lin', 1, 0), "i_screenWidth",
				{arg view; screenWidth = view.value; this.resetScreenSize; this.oscActivate;}],
			["EZNumber", "Wind. height", ControlSpec(0, 10000, 'lin', 1, 0), "i_screenHeight",
				{arg view; screenHeight = view.value; this.resetScreenSize; this.oscActivate;}],
			["TXPresetPopup", "Presets", holdScreenSizeTexts, holdSSPresetActions, 20],
			["EZNumber", "Wind. Pos X", ControlSpec(0, 10000, 'lin', 1, 0), "i_posX",
				{arg view; screenPosX = view.value; this.resetScreenSize; this.oscActivate;}],
			["EZNumber", "Wind. Pos Y", ControlSpec(0, 10000, 'lin', 1, 0), "i_posY",
				{arg view; screenPosY = view.value; this.resetScreenSize; this.oscActivate;}],
			["commandAction", "Sample Current Position", {
				var holdRect;
				if (holdQCWindow.notNil, {
					holdRect = holdQCWindow.bounds;
					screenPosX = holdRect.left;
					screenPosY = holdRect.top;
					system.flagGuiUpd;
				});
			}],
			["commandAction", "Default Window Position", {arg view;
				screenPosX = 0; screenPosY = 60;
				this.setSynthValue("i_posX", 0); this.setSynthValue("i_posY", 60);
				this.resetScreenSize; this.oscActivate;
				this.sendCurrentValues;
				system.flagGuiUpd;
			}],
			["EZNumber", "Border X", ControlSpec(0, 300, 'lin', 1, 0), "i_borderX",
				{arg view; this.resetScreenSize; this.oscActivate;}],
			["EZNumber", "Border Y", ControlSpec(0, 300, 'lin', 1, 0), "i_borderY",
				{arg view; this.resetScreenSize; this.oscActivate;}],
			["TXCheckBox", "Window controls", "i_winControls", {this.rebuildQCScreen; }],
		];

		// TXQCArgGuiarguments:
		// 	index1 is text
		//	index2 is synth arg name to be updated for the number
		//	index3 is synth arg name to be updated for the active number setting
		// 	index4 is array of all module arguments
		// 	index5 is argument index no
		// 	index6 is set argument value function
		// e.g. ["TXQCArgGui", "Particle Hue", "p003", "i_active003", arrQCArgData, 4, setArgValFunc],
		arrInputs.do({arg item, i;
			arrSpecs = arrSpecs.add( ["TXQCArgGui", arrInputs.at(i).asString, classData.arrPStrings.at(i),
				classData.arrActivePStrings.at(i), arrQCArgData, i, {this.sendArgValue(i);}]);
		});
		arrActionSpecs = this.buildActionSpecs(arrSpecs);
	}

	getButtonColour { arg colour2Boolean;
		if (colour2Boolean == true, {
			^TXColor.sysGuiCol4;
		},{
			^TXColor.sysGuiCol1;
		});
	}

	showQCScreen { arg argPath;
		var holdWinBorder, holdPath, borderX, borderY;
		// only show if i_showQuartzWindow is on
		if (this.getSynthArgSpec("i_showQuartzWindow") == 1, {
			if ( (qcFileName == "" and: argPath.isNil).not, {   // only show screen if valid path
				{
					//	check if window exists
					if (holdQCWindow.isNil) {
						if (argPath.notNil, {
							holdPath = argPath;
						},{
							holdPath = qcFileName;
						});
						// Convert path
						holdPath = TXPath.convert(holdPath);
						if (File.exists(holdPath), {
							// make window
							if (this.getSynthArgSpec("i_winControls") == 1, {holdWinBorder = true;},
								{holdWinBorder = false;});
							holdQCWindow= Window(this.getSynthArgSpec("windowName") ? " ",
								Rect(screenPosX, screenPosY, screenWidth, screenHeight), border: holdWinBorder).front;
							holdQCWindow.onClose_({
								holdQCWindow = nil;
								holdQCView = nil;
							});
							// make Button
							Button(holdQCWindow, Rect(2, 2, 30, 20))
							.states_([["<-", Color.white, Color.grey(0.1)]])
							.action_({system.windowToFront});
							// make background
							StaticText(holdQCWindow, Rect(0 ,0, screenWidth, screenHeight)).background_(TXColor.black);
							// border settings
							borderX = this.getSynthArgSpec("i_borderX");
							borderY = this.getSynthArgSpec("i_borderY");
							// make QuartzComposerView
							holdQCView = QuartzComposerView(holdQCWindow,
								Rect(borderX ,borderY, (screenWidth-(borderX*2)).max(0),
									(screenHeight-(borderY*2)).max(0)));
							holdQCView.path = holdPath;
							holdQCView.start;
							arrInputs = holdQCView.inputKeys;
							this.buildGuiSpecArray;
							this.setActionSpecs;
							// replace names in arrCtlSCInBusChoices
							arrInputs.do({arg item, i;
								var holdString, holdInputVal;
								holdInputVal = holdQCView.getInputValue(item);
								// only use if Float /Integer /True /False
								if ((holdInputVal.class == Float) or: (holdInputVal.class == Integer)
									or:(holdInputVal.class == True) or: (holdInputVal.class == False) , {
										holdString = item.asString;
									}, {
										holdString = "(not used";
								});
								this.arrCtlSCInBusChoices.at(i).put(0, holdString);
							});
							// store values
							currenti_screenWidth = this.getSynthArgSpec("i_screenWidth");
							currenti_screenHeight = this.getSynthArgSpec("i_screenHeight");
							currenti_posX = this.getSynthArgSpec("i_posX");
							currenti_posY = this.getSynthArgSpec("i_posY");
							currenti_borderX = this.getSynthArgSpec("i_borderX");
							currenti_borderY = this.getSynthArgSpec("i_borderY");
							currenti_maxFPS = this.getSynthArgSpec("i_maxFPS");
							currenti_winControls = this.getSynthArgSpec("i_winControls");
							currentwindowName = this.getSynthArgSpec("windowName");
							currentscreenWidth = screenWidth;
							currentscreenHeight = screenHeight;
						});
					}{
						// if window exists bring to front
						holdQCWindow.front;
					};
				}.defer;
			});
		});
	}

	closeQCWindow {
		//	check if window exists
		if (holdQCWindow.notNil) {holdQCWindow.close};
		holdQCWindow = nil;
		holdQCView = nil;
		currenti_screenWidth = nil;
		currenti_screenHeight = nil;
		currenti_posX = nil;
		currenti_posY = nil;
		currenti_borderX = nil;
		currenti_borderY = nil;
		currenti_maxFPS = nil;
		currenti_winControls = nil;
		currentwindowName = nil;
		currentscreenWidth = nil;
		currentscreenHeight = nil;
	}
	rebuildQCScreen {
		if ( (
			currenti_screenWidth == this.getSynthArgSpec("i_screenWidth") and:
			currenti_screenHeight == this.getSynthArgSpec("i_screenHeight") and:
			currenti_posX == this.getSynthArgSpec("i_posX") and:
			currenti_posY == this.getSynthArgSpec("i_posY") and:
			currenti_borderX == this.getSynthArgSpec("i_borderX") and:
			currenti_borderY == this.getSynthArgSpec("i_borderY") and:
			currenti_maxFPS == this.getSynthArgSpec("i_maxFPS") and:
			currenti_winControls == this.getSynthArgSpec("i_winControls") and:
			currentwindowName == this.getSynthArgSpec("windowName") and:
			currentscreenWidth == screenWidth and:
			currentscreenHeight == screenHeight
		).not, {
			{
				this.closeQCWindow;
			}.defer;
			{
				this.showQCScreen;
			}.defer(0.2);
			{
				this.sendCurrentValues;
			}.defer(0.5);
		});
	}

	showOrHideQCScreen {
		var showScreen;
		showScreen = this.getSynthArgSpec("i_showQuartzWindow");
		if (showScreen == 1, {this.rebuildQCScreen;{this.checkQCFileExists;}.defer(0.6);});
		if (showScreen == 0, {{this.closeQCWindow;}.defer});
	}

	resetScreenSize {
		screenWidth = this.getSynthArgSpec("i_screenWidth");
		screenHeight = this.getSynthArgSpec("i_screenHeight");
		this.rebuildQCScreen;
	}

	oscActivate {
		{ // defer to allow for screen building
			//	remove any previous responders and add new
			this.oscDeactivate;
			arrInputs.do({arg item, i;
				var newResp, holdArgType, holdMin, holdMax, holdVal;

				//	only build responders for active inputs
				holdArgType = arrQCArgData.at(i).at(0);

				if ( (holdArgType == 1) or: (holdArgType == 2) or: (holdArgType == 4), {
					newResp = OSCFunc({arg msg, time, addr,  recvPort;
						if (msg[2] == arrSendTrigIDs.at(i),{
							{	holdMin = arrQCArgData.at(i).at(2);
								holdMax = arrQCArgData.at(i).at(3);
								if (holdArgType == 1, {
									holdVal =  holdMin + (msg[3] * (holdMax - holdMin));
								});
								if (holdArgType == 2, {
									holdVal =  (holdMin + (msg[3] * (holdMax - holdMin))).round;
								});
								if (holdArgType == 4, {
									// convert to value to boolean
									holdVal =  (msg[3].round > 0 );
								});
								if (holdQCView.notNil,  {
									holdQCView.setInputValue(arrInputs.at(i), holdVal);
								});
							}.defer;
						});
					}, '/tr', system.server.addr);
					arrPResponders = arrPResponders.add(newResp);
				});
			});
		}.defer(0.5);
	}

	oscDeactivate {
		//	remove responders
		arrPResponders.do({arg item, i;
			item.free;
		});
	}

	extraSaveData {
		^[arrQCArgData, qcFileName, screenWidth, screenHeight, screenPosX, screenPosY];
	}

	loadExtraData {arg argData;  // override default method
		arrQCArgData = argData.at(0);
		qcFileName = argData.at(1);
		// Convert path
		qcFileName = TXPath.convert(qcFileName);
		screenWidth = argData.at(2);
		screenHeight = argData.at(3);
		screenPosX = argData.at(4);
		screenPosY = argData.at(5);
		// force window to be shown initially so controls are found
		if (	this.getSynthArgSpec("i_showQuartzWindow") == 0, {
			this.setSynthArgSpec("i_showQuartzWindow", 1);
			{	this.setSynthArgSpec("i_showQuartzWindow", 0);
				this.showOrHideQCScreen;
				system.flagGuiIfModDisplay(this);
			}.defer(3);
		});
		this.buildGuiSpecArray;
		this.setActionSpecs;
		this.oscActivate;
		{this.resetScreenSize;}.defer(0.3); // open window
		{this.sendCurrentValues;}.defer(0.5);
		{this.checkQCFileExists;}.defer(0.6);
	}

	deleteModuleExtraActions {
		//	remove responders
		this.oscDeactivate;
		this.closeQCWindow;
	}

	checkQCFileExists {
		if (File.exists(qcFileName).not, {
			// if invalid filename
			TXInfoScreen.new(
				"Error: Could not open Quartz Composer file: ",
				arrInfoLines: ["File not found:  " ++ qcFileName]
			);
		});
	}

	isValidQCFile {arg argPath;
		var extension;
		extension = argPath.splitext.at(1);
		^((extension.size == 3) and: extension.containsi("qtz"));
	}

	openQCComp {arg keepSettings = false;
		var
		holdData;
		holdData = ();
		if (keepSettings == true, {
			arrInputs.do({arg item, i;
				holdData[item] = arrQCArgData[i];
			});
		});
		// get path/filename
		Dialog.openPanel({ arg newPath;
			// check for valid file
			if (this.isValidQCFile(newPath), {
				// assign name
				qcFileName = newPath;
				if (qcFileNameView.notNil, {
					if (qcFileNameView.notClosed, {qcFileNameView.string = qcFileName;});
				});
				// reset various things
				this.buildGuiSpecArray;
				this.setActionSpecs;
				this.showQCScreen(newPath);
				{this.initialiseQCParameters(holdData)}.defer(0.5);
				{system.showView}.defer(0.9);
			});
		});
	}


	initialiseQCParameters { arg holdData;
		arrInputs.do({arg item, i;
			var holdInputVal, holdDataType, holdActiveValue, holdStringVal, holdMin, holdMax, holdNum;
			//  arrQCArgData
			//  array of :  argDataType, argStringVal, argMin, argMax, argRed, argGreen, argBlue, argAlpha, argNumVal
			//  argDataType can be: [0.notPassed, 1.float, 2.integer, 3.string, 4.booleanNum(0/1), 5.colour(RGBA),
			//	6.Directory Name, 7.File Name],
			//   - e.g. [0, "", 0, 1, 0.5, 0.5, 0.5, 1, 0].dup(classData.maxParameters)

			holdInputVal = holdQCView.getInputValue(item);
			holdActiveValue = 0;  // default

			if (holdInputVal.class == Float, {
				holdDataType = 1;
				holdNum = holdInputVal;
				holdMin = min(0, holdNum.floor);
				holdMax = max(1, holdNum.ceil);
				arrQCArgData [i] [0] = holdDataType;
				arrQCArgData [i] [2] = holdMin;
				arrQCArgData [i] [3] = holdMax;
				arrQCArgData [i] [8] = holdNum;
				holdActiveValue = 1;
			});
			if (holdInputVal.class == Integer, {
				holdDataType = 2;
				holdNum = holdInputVal;
				holdMin = min(0, holdNum);
				holdMax = max(1, holdNum);
				arrQCArgData [i] [0] = holdDataType;
				arrQCArgData [i] [2] = holdMin;
				arrQCArgData [i] [3] = holdMax;
				arrQCArgData [i] [8] = holdNum;
				holdActiveValue = 1;
			});
			if (holdInputVal.class == String, {
				holdDataType = 3;
				holdStringVal = holdInputVal;
				arrQCArgData [i] [0] = holdDataType;
				arrQCArgData [i] [1] = holdStringVal;
			});
			if ((holdInputVal.class == True) or: (holdInputVal.class == False) , {
				holdDataType = 4;
				holdNum = holdInputVal.binaryValue;
				holdMin = 0;
				holdMax = 1;
				arrQCArgData [i] [0] = holdDataType;
				arrQCArgData [i] [2] = holdMin;
				arrQCArgData [i] [3] = holdMax;
				arrQCArgData [i] [8] = holdNum;
				holdActiveValue = 1;
			});
			if (holdInputVal.class == Color, {
				holdDataType = 5;
				arrQCArgData [i] [0] = holdDataType;
				arrQCArgData [i] [4] = holdInputVal.red;
				arrQCArgData [i] [5] = holdInputVal.green;
				arrQCArgData [i] [6] = holdInputVal.blue;
				arrQCArgData [i] [7] = holdInputVal.alpha;
			});

			this.setSynthValue(classData.arrActivePStrings.at(i), holdActiveValue);
			// replace with holdData if valid
			if (holdData.notNil and: {holdData[item].notNil}, {
				arrQCArgData [i] = holdData[item];
			});
			this.sendArgValue(i);
		});

		// rebuild synth and activate osc
		{this.rebuildSynth;}.defer(0.4);
		{this.oscActivate;}.defer(0.4);

	}

	setFPS {arg argFPS;
		holdQCView.maxFPS_(argFPS);
	}

	sendCurrentValues {
		arrInputs.do({arg item, i;
			{this.setSynthValue(classData.arrPStrings.at(i), 1);}.defer(0.35);
		});
		{ 	arrInputs.do({arg item, i;
			var holdMin, holdMax, holdNum;
			holdMin = arrQCArgData.at(i).at(2);
			holdMax = arrQCArgData.at(i).at(3);
			holdNum = arrQCArgData.at(i).at(8);
			this.setSynthValue(classData.arrPStrings.at(i), [holdMin, holdMax].asSpec.unmap(holdNum));
		});
		}.defer(0.5);

		arrInputs.do({arg item, i;
			{ this.sendArgValue(i);
			}.defer(0.75 + ((i div: 20) * 0.1));
		});

	}

	sendArgValue {arg argIndex;
		var holdArgs, holdVal;
		var argDataType, argStringVal, argMin, argMax, argRed, argGreen, argBlue, argAlpha, argNumVal;
		holdArgs = arrQCArgData.at(argIndex);
		argDataType = holdArgs.at(0);
		argStringVal = holdArgs.at(1);
		argMin = holdArgs.at(2);
		argMax = holdArgs.at(3);
		argRed = holdArgs.at(4);
		argGreen = holdArgs.at(5);
		argBlue = holdArgs.at(6);
		argAlpha = holdArgs.at(7);
		argNumVal = holdArgs.at(8);

		//  array of :  argDataType, argStringVal, argMin, argMax, argRed, argGreen, argBlue, argAlpha
		//  argDataType can be: [0.notPassed, 1.float, 2.integer, 3.string, 4.booleanNum(0/1), 5.colour(RGBA),
		//	6.Directory Name, 7.File Name],

		if (argDataType == 1, {
			this.setSynthValue(classData.arrPStrings.at(argIndex), [argMin, argMax].asSpec.unmap(argNumVal));
		});
		if (argDataType == 2, {
			this.setSynthValue(classData.arrPStrings.at(argIndex), [argMin, argMax].asSpec.unmap(argNumVal.asInteger));
		});

		if ( (argDataType == 3) or: (argDataType == 6) or: (argDataType == 7), {
			holdVal = argStringVal;
			try {
				holdQCView.setInputValue(arrInputs.at(argIndex), holdVal);
			};
		});
		if (argDataType == 4, {
			this.setSynthValue(classData.arrPStrings.at(argIndex), argNumVal);
		});
		if (argDataType == 5, {
			holdVal = Color.new(argRed, argGreen, argBlue, argAlpha);
			try {
				holdQCView.setInputValue(arrInputs.at(argIndex), holdVal);
			};
		});
	}

	runAction {
		super.runAction;
		if (qcFileName != "", {
			holdQCView.start;
		});
	}

	pauseAction {
		super.pauseAction;
		if (qcFileName != "", {
			holdQCView.stop;
		});
	}

	openGui{ arg argParent; 			 // override base class
		//	use base class initialise
		this.baseOpenGui(this, argParent);
		if (holdScrollView.notNil and: {holdScrollView.notClosed},
			{holdScrollView.visibleOrigin = (holdVisibleOrigin); });
	}

}


