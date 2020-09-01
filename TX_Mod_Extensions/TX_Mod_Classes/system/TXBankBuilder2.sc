// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXBankBuilder2 {

	classvar <>system;    // system
	classvar <>arrSampleBanks, <>arrLoopBanks;
	classvar classData; //event dict for data

	*initClass{
		classData = (); //event
		arrSampleBanks = [ [], "" ]!100;
		arrLoopBanks = [ [], "" ]!100;
		classData.bankType = "Sample";
		classData.currSampleBankNo = 0;
		classData.currLoopBankNo = 0;
		classData.holdArray = [];
		classData.holdCurrentBankNo = 0;
		classData.holdCurrentBankName = "";
		classData.showWaveform = 0;
		classData.samplesVisibleOrigin = Point.new;
		classData.loopsVisibleOrigin = Point.new;
		classData.displaySampleNo = 0;
		classData.displayLoopNo = 0;
		classData.holdNewFreq = 440;
		classData.holdNewBeats = 8;
	}

	*sampleBank{ arg bankNo=0;
		// get bank
		^arrSampleBanks[bankNo];
	}

	*sampleBank_ { arg argBank, bankNo=0;
		var newBank;
		// set bank
		if (argBank.notNil, {
			newBank = this.checkSndFilePaths(argBank, "Sample");
			arrSampleBanks[bankNo] = newBank;
		});
	}

	*loopBank{ arg bankNo=0;
		// get bank
		^arrLoopBanks[bankNo];
	}

	*loopBank_ { arg argBank, bankNo=0;
		var newBank;
		// set bank
		if (argBank.notNil, {
			newBank = this.checkSndFilePaths(argBank, "Loop");
			arrLoopBanks[bankNo] = newBank;
		});
	}

	*verifyBank{arg bank;
		var invalidPaths, invalidIndices;
		invalidPaths = [];
		bank.do({ arg item, i;
			var holdPath;
			if (item.size > 0, {
				holdPath = item[0];
				if (holdPath != "REMOVED", {
					// Convert path
					holdPath = TXPath.convert(holdPath);
					if (File.exists(holdPath).not, {
						invalidPaths = invalidPaths.add(holdPath);
						invalidIndices = invalidIndices.add(i);
					});
				});
			});
		});
		^[invalidPaths, invalidIndices];
	}

	*verifySampleLoopBanks{
		var invalidPaths, holdPaths;
		invalidPaths = [];
		arrSampleBanks.do({arg bank;
			holdPaths = this.verifyBank(bank[0])[0];
			invalidPaths = invalidPaths ++ holdPaths;
		});
		if (invalidPaths.size > 0, {
			TXInfoScreen.new(
				"Sample Bank Error - the following files were not found:",
				arrInfoLines: invalidPaths
			);
		});
		invalidPaths = [];
		arrLoopBanks.do({arg bank;
			holdPaths = this.verifyBank(bank[0])[0];
			invalidPaths = invalidPaths ++ holdPaths;
		});
		if (invalidPaths.size > 0, {
			TXInfoScreen.new(
				"Loop Bank Error - the following files were not found:",
				arrInfoLines: invalidPaths
			);
		});
	}

	*makeSampleGui{arg argView;  this.makeGui(argView, "Sample"); }

	*makeLoopGui{arg argView;  this.makeGui(argView, "Loop"); }

	*makeGui{ arg argView, argBankType;
		var parent, holdSampleNo, arrSaveData, arrOutput, strBankType;

		var numBankNo, btnBankMinus, btnBankPlus, strBankNameLabel, textBankName;
		var btnSaveBank, btnOpenBank, btnEmptyBank, btnAddSamples, btnSearchFolder, btnPlaySample, dragSink;
		var btnStopPlay, btnDeleteSample, btnReplaceSample;
		var strSampleLoop, signalViewSample, strFileNameTxt;
		var freqOrBeatsTitle;
		var strBPMtxt;
		var btnSamplePlus, btnSampleMinus, btnChangeValue, sldVolume, holdSampleView;
		var specFreqOrBeats,  holdSynth, strListTitle;

		// initialise variables
		classData.bankType = argBankType ?? classData.bankType;
		if (classData.bankType == "Sample", {
			classData.holdArray = arrSampleBanks[classData.currSampleBankNo][0].deepCopy;
			classData.holdArrBankNames = arrSampleBanks.collect({arg item, i; i.asString ++ " - " ++ item[1]});
			classData.holdCurrentBankNo = classData.currSampleBankNo.copy;
			classData.holdCurrentBankName = arrSampleBanks[classData.currSampleBankNo][1];
		}, {
			classData.holdArray = arrLoopBanks[classData.currLoopBankNo][0].deepCopy;
			classData.holdArrBankNames = arrLoopBanks.collect({arg item, i; i.asString ++ " - " ++ item[1]});
			classData.holdCurrentBankNo = classData.currLoopBankNo.copy;
			classData.holdCurrentBankName = arrLoopBanks[classData.currLoopBankNo][1];
		});

		//	argView.decorator.shift(4, 0);
		parent = CompositeView(argView, Rect(0,0, 1060, 600)).background_(TXColor.sysModuleWindow);
		parent.decorator = FlowLayout(parent.bounds);

		if (classData.bankType == "Sample", {
			specFreqOrBeats = [1, 20000, \linear, 0].asSpec;
		}, {
			specFreqOrBeats = [1, 1000, \linear, 0].asSpec;
		});

		// spacing
		parent.decorator.nextLine;
		// bank type
		strBankType = StaticText(parent, 284 @ 40)
		.align_(\center)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);
		strBankType.font_(Font.new("Helvetica-Bold",18));

		if (classData.bankType == "Sample", {
			strBankType.string = "SAMPLE BANKS";
		}, {
			strBankType.string = "LOOP BANKS";
		});

		// spacing
		parent.decorator.nextLine.shift(0, 5);

		// bank no.
		classData.strBankNo = StaticText (parent, 80 @ 24)
		.background_(TXColor.white)
		.align_(\center)
		.stringColor_(TXColor.sysGuiCol1);
		classData.strBankNo.string = "Bank No.";

		classData.popBank = PopUpMenu (parent, 200 @ 24)
		.value_(classData.holdCurrentBankNo)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);

		classData.popBank.items = classData.holdArrBankNames;
		classData.popBank.value = classData.holdCurrentBankNo;

		btnBankMinus = Button(parent, 20 @ 24)
		.states = [["-", TXColor.white, TXColor.sysGuiCol1]];
		btnBankPlus = Button(parent, 20 @ 24)
		.states = [["+", TXColor.white, TXColor.sysGuiCol1]];

		// spacing
		parent.decorator.shift(10, 0);
		// Bank name
		strBankNameLabel = StaticText(parent, 100 @ 24)
		.background_(TXColor.white)
		.align_(\center)
		.stringColor_(TXColor.sysGuiCol1);
		strBankNameLabel.string =  "Bank Name";
		textBankName = TextField(parent, 300 @ 24)
		.string_(classData.holdCurrentBankName)
		.align_(\center)
		.background_(TXColor.white)
		//			.font_(Font("Gill Sans", 11))
		.stringColor_(TXColor.sysGuiCol1);

		// spacing
		parent.decorator.shift(0, 10);
		parent.decorator.nextLine;

		// no. samples
		classData.strNoElements = StaticText(parent, 210 @ 24)
		.string_(" Total no. of Samples in Bank: 0")
		.align_(\left)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);

		// spacing
		parent.decorator.shift(10, 0);
		// Drag & Drop samples/loops
		dragSink = DragSink(parent, 130 @ 24).align_(\center).background_(TXColor.paleYellow2);
		dragSink.string = "Drag & drop to add";
		dragSink.receiveDragHandler = { arg view;
			var inArray = View.currentDrag;
			if (inArray.class == String, {
				inArray = [inArray];
			});
			this.loadPaths(inArray, classData.bankType);
			system.showView;
		};

		// spacing
		parent.decorator.shift(10, 0);
		// button
		btnAddSamples = Button(parent, 130 @ 24);
		if (classData.bankType == "Sample", {
			btnAddSamples.states = [["Add Samples to Bank", TXColor.white, TXColor.sysGuiCol1]];
		}, {
			btnAddSamples.states = [["Add Loops to Bank", TXColor.white, TXColor.sysGuiCol1]];
		});
		// spacing
		parent.decorator.shift(10, 0);
		// button
		btnSearchFolder = Button(parent, 200 @ 24);
		if (classData.bankType == "Sample", {
			btnSearchFolder.states = [["Search a folder for missing samples", TXColor.white, TXColor.sysGuiCol1]];
		}, {
			btnSearchFolder.states = [["Search a folder for missing loops", TXColor.white, TXColor.sysGuiCol1]];
		});
		// spacing
		parent.decorator.shift(10, 0);
		// button
		btnOpenBank = Button(parent, 90 @ 24).states = [["Load Bank", TXColor.white, TXColor.sysGuiCol2]];
		// spacing
		parent.decorator.shift(10, 0);
		// button
		btnSaveBank = Button(parent, 90 @ 24).states = [["Save Bank", TXColor.white, TXColor.sysGuiCol2]];
		// spacing
		parent.decorator.shift(10, 0);
		// button
		btnEmptyBank = Button(parent, 90 @ 24).states = [["Empty Bank", TXColor.white, TXColor.sysDeleteCol]];

		// spacing
		parent.decorator.shift(0, 5);
		parent.decorator.nextLine;
		// divider
		StaticText(parent, 1050 @ 1) .string_("") .background_(TXColor.sysGuiCol1);

		// spacing
		parent.decorator.shift(0, 10);
		parent.decorator.nextLine;

		// sample selection
		strListTitle = StaticText(parent, 186 @ 24)
		.string_("Select:")
		.align_(\center)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);
		if (classData.bankType == "Sample", {
			strListTitle.string = "Select Sample:";
		},{
			strListTitle.string = "Select Loop:";
		});
		classData.listSamples = classData.holdArray.collect({ arg item, i;
			var errorText = "";
			var holdPath = item.at(0);
			holdPath = TXPath.convert(holdPath);
			if (File.exists(holdPath).not and: {holdPath != "REMOVED"}, {
				errorText = "** MISSING FILE: ";
			});
			if (item.at(3) == false and: {holdPath != "REMOVED"}, {
				errorText = "** INVALID FILE: ";
			});
			errorText ++ item.at(0);
		});

		// make box
		classData.sampleListBox =  CompositeView(parent, Rect(0,0, 850, 190));
		classData.sampleListBox.background = TXColour.sysChannelAudio;
		classData.sampleListBox.decorator = FlowLayout(classData.sampleListBox.bounds);
		classData.sampleListBox.decorator.margin.x = 0;
		classData.sampleListBox.decorator.margin.y = 0;
		classData.sampleListBox.decorator.gap = Point(0,0);
		classData.sampleListBox.decorator.reset;

		classData.samplesScrollView = ScrollView(classData.sampleListBox, Rect(0,0, 842, 182))
		.hasBorder_(false);
		if (GUI.current.asSymbol == \cocoa, {
			classData.samplesScrollView.autoScrolls_(false);
		});

		classData.samplesScrollView.action = {arg view;
			if (classData.bankType == "Sample", {
				classData.samplesVisibleOrigin = view.visibleOrigin;
			},{
				classData.loopsVisibleOrigin = view.visibleOrigin;
			});
		};

		classData.samplesBox = CompositeView(classData.samplesScrollView,
			Rect(0,0, 828, ((classData.listSamples.size + 1) * 22).max(192)));
		classData.samplesBox.decorator = FlowLayout(classData.samplesBox.bounds);
		classData.samplesBox.decorator.margin.x = 0;
		classData.samplesBox.decorator.margin.y = 0;
		classData.samplesBox.decorator.gap = Point(2,2);
		classData.samplesBox.decorator.reset;
		classData.samplesBox.background =TXColor.white;

		if (classData.bankType == "Sample", {
			holdSampleNo = classData.displaySampleNo;
		},{
			holdSampleNo = classData.displayLoopNo;
		});

		classData.listSamples.do({arg item, i;
			var btnSample, stringCol, backCol;
			if (holdSampleNo == i, {
				stringCol = TXColor.white;
				backCol = TXColor.sysGuiCol1;
			},{
				stringCol = TXColor.sysGuiCol1;
				backCol = TXColor.white;
			});
			btnSample = UserView(classData.samplesBox, 824 @ 20);
			btnSample.background = backCol;
			btnSample.drawFunc = {
				Pen.fillColor = stringCol;
				Pen.stringAtPoint(item, Point(2, 2));
			};
			btnSample.mouseDownAction = {
				this.setSampleNo(i);
				system.showView;
			};
		});

		if (classData.listSamples.size > 0, {
			if (classData.bankType == "Sample", {
				{classData.samplesScrollView.visibleOrigin = classData.samplesVisibleOrigin}.defer(0.05);
			},{
				{classData.samplesScrollView.visibleOrigin = classData.loopsVisibleOrigin}.defer(0.05);
			});
		});

		// spacing
		parent.decorator.nextLine;
		parent.decorator.shift(0, -32);

		// TXCheckBox: arg argParent, bounds, text, offStringColor, offBackground,
		// onStringColor, onBackground, onOffTextType=0;
		classData.displayWaveform = TXCheckBox(parent, 140 @ 24, "Show waveform", TXColor.sysGuiCol1, TXColour.white,
			TXColor.white, TXColor.sysGuiCol1);
		classData.displayWaveform.value = classData.showWaveform;

		// spacing
		parent.decorator.nextLine;

		// show soundfile
		classData.soundFileView = SoundFileView.new(parent, 1030 @ 100)
		.gridOn_(false).timeCursorOn_(false)
		// .waveColors_(Color.blue(alpha: 1.0) ! 8)
		.waveColors_(TXColor.sysGuiCol1(alpha: 1.0) ! 8)
		// .background_(Color(0.65,0.65,0.95));
		.background_(TXColor.white);

		// spacing
		parent.decorator.shift(0, 10);
		parent.decorator.nextLine;

		// button
		btnPlaySample = Button(parent, 100 @ 24);
		if (classData.bankType == "Sample", {
			btnPlaySample.states = [["Play Sample", TXColor.white, TXColor.sysGuiCol1]];
		},{
			btnPlaySample.states = [["Play Loop", TXColor.white, TXColor.sysGuiCol1]];
		});

		// spacing
		parent.decorator.shift(10, 0);
		// button
		btnStopPlay = Button(parent, 100 @ 24).states = [["Stop Playing", TXColor.white, TXColor.sysGuiCol1]];
		// spacing
		parent.decorator.shift(10, 0);
		// volume slider
		sldVolume = EZSlider(parent, 190 @ 24, "Volume", labelWidth: 45, numberWidth: 40)
		.setColors(TXColor.white, TXColor.sysGuiCol1)
		.value_(0.5);
		sldVolume.sliderView.thumbSize_(10);

		// spacing
		parent.decorator.shift(20, 0);
		// button
		btnDeleteSample = Button(parent, 160 @ 24);
		if (classData.bankType == "Sample", {
			btnDeleteSample.states = [["Remove Sample from Bank", TXColor.white, TXColor.sysDeleteCol]];
		}, {
			btnDeleteSample.states = [["Remove Loop from Bank", TXColor.white, TXColor.sysDeleteCol]];
		});

		// spacing
		parent.decorator.shift(20, 0);
		// button
		btnReplaceSample = Button(parent, 160 @ 24);
		if (classData.bankType == "Sample", {
			btnReplaceSample.states = [["Replace Selected Sample", TXColor.white, TXColor.sysGuiCol2]];
		}, {
			btnReplaceSample.states = [["Replace Selected Loop", TXColor.white, TXColor.sysGuiCol2]];
		});

		// spacing
		parent.decorator.shift(0, 5);
		parent.decorator.nextLine;
		// divider
		StaticText(parent, 1050 @ 1) .string_("") .background_(TXColor.sysGuiCol1);

		// spacing
		parent.decorator.shift(0, 5);
		parent.decorator.nextLine;
		// sample no.
		strSampleLoop = StaticText (parent, 80 @ 24)
		.background_(TXColor.white)
		.align_(\center)
		.stringColor_(TXColor.sysGuiCol1);
		if (classData.bankType == "Sample", {
			strSampleLoop.string = "Sample no.";
		}, {
			strSampleLoop.string = "Loop no.";
		});
		classData.numSampleNo = NumberBox (parent, 30 @ 24).scroll_(false).value_(0);
		btnSampleMinus = Button(parent, 20 @ 24).states = [["-", TXColor.white, TXColor.sysGuiCol1]];
		btnSamplePlus = Button(parent, 20 @ 24).states = [["+", TXColor.white, TXColor.sysGuiCol1]];

		// file name
		strFileNameTxt = StaticText(parent, 80 @ 24)
		.background_(TXColor.white)
		.align_(\center)
		.stringColor_(TXColor.sysGuiCol1);
		if (classData.bankType == "Sample", {
			strFileNameTxt.string =  "Sample File";
		}, {
			strFileNameTxt.string =  "Loop File";
		});
		classData.strSampleFileName = StaticText(parent, 500 @ 24)
		.string_(" ")
		.background_(TXColor.white)
		.align_(\left)
		.stringColor_(TXColor.sysGuiCol1)
		.font_(Font("Gill Sans", 11));

		// spacing
		parent.decorator.shift(5, 0);
		// length
		StaticText(parent, 80 @ 24)
		.string_("Length (ms)")
		.align_(\center)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);
		classData.strLength = StaticText(parent, 50 @ 24)
		.string_("")
		.align_(\center)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);

		// spacing
		parent.decorator.shift(5, 0);
		// no. channels
		StaticText(parent, 80 @ 24)
		.string_("No. Channels")
		.align_(\center)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);
		classData.strNumChannels = StaticText(parent, 50 @ 24)
		.string_("")
		.align_(\center)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);

		// spacing
		parent.decorator.shift(0, 10);
		parent.decorator.nextLine;

		// frequency/ no. beats
		freqOrBeatsTitle = StaticText(parent, 116 @ 24)
		.align_(\center)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);
		if (classData.bankType == "Sample", {
			freqOrBeatsTitle.string =  "Frequency";
		}, {
			freqOrBeatsTitle.string =  "Total Beats";
		});

		// Freq Or Beats
		classData.numFreqOrBeats = NumberBox (parent, 76 @ 24).scroll_(false);

		if (classData.bankType == "Sample", {
			// spacing
			parent.decorator.shift(10, 0);
			// set frequency/ no. Beats
			btnChangeValue = Button(parent, 120 @ 24);
			btnChangeValue.states = [["Set Frequency to:", TXColor.white, TXColor.sysGuiCol1]];

			classData.numNewFreqOrBeats = NumberBox (parent, 76 @ 24).scroll_(false);
			classData.numNewFreqOrBeats.value = classData.holdNewFreq;

			classData.popupMidiNote = PopUpMenu(parent, 50 @ 24)
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
			classData.popupMidiNote.items = ["notes"]
			++ 103.collect({arg item; TXGetMidiNoteString(item + 24);});
			classData.popupMidiNote.action = {
				if (classData.popupMidiNote.value > 0, {
					classData.numNewFreqOrBeats.valueAction = (classData.popupMidiNote.value + 23).midicps.round(0.01);
				});
			};
			classData.popupMidiNote.value = 46;
		}, {
			32.do({arg index;
				var btn;
				btn = Button(parent, 20 @ 24);
				btn.states = [[(index + 1).asString, TXColor.white, TXColor.sysGuiCol1]];
				btn.action = {
					classData.numFreqOrBeats.valueAction = (index + 1);
					system.reloadAllLoops(classData.currLoopBankNo);
				};
			});
		});

		// spacing
		parent.decorator.nextLine.shift(0, 6);

		if (classData.bankType == "Loop", {
			// bpm text (only for loops)
			strBPMtxt = StaticText(parent, 116 @ 24)
			.align_(\center)
			.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
			strBPMtxt.string =  "Equivalent BPM: ";

			classData.numBPM = NumberBox (parent, 76 @ 24).scroll_(false);
			this.updateBPM;

			classData.btnDoubleBPM = Button(parent, 64 @ 24);
			classData.btnDoubleBPM.states = [["BPM X 2", TXColor.white, TXColor.sysGuiCol1]];
			classData.btnDoubleBPM.action = {
				classData.numFreqOrBeats.valueAction = classData.numFreqOrBeats.value * 2;
				system.reloadAllLoops(classData.currLoopBankNo);
			};

			classData.btnHalfBPM = Button(parent, 64 @ 24);
			classData.btnHalfBPM.states = [["BPM / 2", TXColor.white, TXColor.sysGuiCol1]];
			classData.btnHalfBPM.action = {
				classData.numFreqOrBeats.valueAction = classData.numFreqOrBeats.value * 0.5;
				system.reloadAllLoops(classData.currLoopBankNo);
			};
		}); // end of if banktype == "Loop"

		// spacing
		parent.decorator.nextLine;

		////////////////////////////////////////////////////////////////////////////////////////
		// DEFINE GUI ACTIONS

		btnBankPlus.action =
		{
			classData.popBank.value = (classData.popBank.value + 1);
			classData.popBank.doAction;
		};

		btnBankMinus.action =
		{
			classData.popBank.value = (classData.popBank.value - 1);
			classData.popBank.doAction;
		};

		classData.popBank.action =
		{arg view;
			classData.holdCurrentBankNo = view.value.min(99).max(0);
			if (classData.bankType == "Sample", {
				classData.currSampleBankNo = classData.holdCurrentBankNo;
				classData.samplesVisibleOrigin = Point.new;
			}, {
				classData.currLoopBankNo = classData.holdCurrentBankNo;
				classData.loopsVisibleOrigin = Point.new;
			});
			// reset sampleno
			this.setSampleNo(0);
			// recreate view
			system.showView;
		};

		textBankName.action =
		{arg view;
			classData.holdCurrentBankName = view.string;
			this.updateBank;
			this.updatePopBankItems;
		};

		btnOpenBank.action =
		{var errFound, errorScreen, fileType;
			var newFile, newData;
			errFound = 0;
			if (classData.bankType == "Sample", {
				fileType = "SampleBank";
			},{
				fileType = "LoopBank";
			});
			Dialog.openPanel({ arg newPath;
				newFile = File(newPath,"r");
				newData = thisProcess.interpreter.executeFile(newPath);
				newFile.close;
				if (newData.notNil, {
					if (newData.isArray and: (newData.size > 1) and: (newData.at(0)==fileType),{
						classData.holdArray = classData.holdArray.asArray ++ newData.at(1).deepCopy;
						classData.holdArray = this.checkSndFilePaths(classData.holdArray, classData.bankType);
						this.updateBank;
					},{
						errFound = 1;
					});
				},{
					errFound = 1;
				});
				this.sizeChange;
				// recreate view
				system.showView;
			});
			// Show error screen if error found
			if (errFound==1, {
				errorScreen = TXInfoScreen.new("ERROR: invalid " ++ fileType ++ ": " ++ newFile.path);
			});
			this.sizeChange;
		};
		btnSaveBank.action =
		{
			var newPath, newFile, newData;
			if (classData.holdArray.size > 0, {
				arrSaveData = classData.holdArray;

				arrOutput = [classData.bankType++"Bank", arrSaveData];

				Dialog.savePanel({ arg path;
					newPath = path;
					newData = arrOutput;
					newFile = File(newPath,"w");
					newFile << "#" <<< newData << "\n";
					//	use file as an io stream
					//	<<< means store the compile string of the object
					//	<< means store a print string of the object
					newFile.close;
				});

			});
		};

		btnEmptyBank.action = {
			classData.holdArray = [];
			this.updateBank;
			this.sizeChange;
			classData.showWaveform = 0;
			// recreate view
			system.showView;
		};

		btnAddSamples.action = {this.addSampleDialog(classData.bankType); };

		btnSearchFolder.action = {this.searchFolderDialog; };

		btnDeleteSample.action = {
			if (classData.holdArray.size > 0, {
				classData.holdArray[classData.numSampleNo.value][0] = "REMOVED";
				classData.holdArray[classData.numSampleNo.value][3] = false;
				this.updateBank;
				classData.showWaveform = 0;
				// recreate view
				system.showView;
			});
		};

		btnReplaceSample.action = {this.replaceSampleDialog; };

		classData.displayWaveform.action = {arg view; classData.showWaveform = view.value; system.showView};

		classData.numSampleNo.action = {arg view;
			this.setSampleNo(view.value);
		};

		btnSamplePlus.action = {
			classData.numSampleNo.value = (classData.numSampleNo.value + 1).min(classData.holdArray.size-1).max(0);
			this.setSampleNo(classData.numSampleNo.value);
		};

		btnSampleMinus.action =
		{
			classData.numSampleNo.value = (classData.numSampleNo.value - 1).max(0);
			this.setSampleNo(classData.numSampleNo.value);
		};

		classData.numFreqOrBeats.action = {arg view;
			if (classData.holdArray.notEmpty, {
				classData.holdArray.at(classData.numSampleNo.value.asInteger).put(1, specFreqOrBeats.constrain(view.value));
				system.reloadAllSamples(classData.currSampleBankNo);
				this.updateBank;
				this.loadSample;
			});
		};

		if (classData.bankType == "Loop", {
			classData.numBPM.action = {arg view;
				this.updateBeatsFromBPM(view.value);
			};
		});

		if (classData.bankType == "Sample", {
			btnChangeValue.action = {
				if (classData.holdArray.notEmpty, {
					classData.holdArray.at(classData.numSampleNo.value.asInteger).put(1, classData.numNewFreqOrBeats.value);
					system.reloadAllSamples(classData.currSampleBankNo);
					this.updateBank;
					this.loadSample;
				});
			};

			classData.numNewFreqOrBeats.action = {arg view;
				if (classData.bankType == "Sample", {
					classData.holdNewFreq = specFreqOrBeats.constrain(view.value);
				}, {
					classData.holdNewBeats = specFreqOrBeats.constrain(view.value);
				});

			};
		});

		btnPlaySample.action = {
			// release any synth running
			btnStopPlay.doAction;
			if (classData.holdArray.notEmpty and: classData.holdBuffer.notNil, {
				if (classData.holdBuffer.numChannels == 1, {
					holdSynth = { sldVolume.value *
						PlayBuf.ar(1, classData.holdBuffer.bufnum, BufRateScale.kr(classData.holdBuffer.bufnum)) ! 2;
					}.play;
				},{
					holdSynth = { sldVolume.value *
						PlayBuf.ar(2, classData.holdBuffer.bufnum, BufRateScale.kr(classData.holdBuffer.bufnum));
					}.play;
				});
			});
		};

		btnStopPlay.action =
		{if (holdSynth.notNil, {
			holdSynth.free;
			holdSynth = nil;
		});
		};

		// initialise
		this.sizeChange;

	} // end of class method makeGui

	*setSampleNo { arg sampleNo = 0;
		sampleNo = sampleNo.min(classData.holdArray.size-1).max(0);
		if (classData.bankType == "Sample", {
			classData.displaySampleNo = sampleNo;
		},{
			classData.displayLoopNo = sampleNo;
		});
		classData.numSampleNo.value = sampleNo;
		this.loadSample;
	}

	*addSampleDialog { arg argBankType = "Sample", argBankNo;
		// set classData.bankType so updates are correct when adding samples
		classData.bankType = argBankType ?? classData.bankType;
		// set bank no if given
		if (argBankNo.notNil, {
			if (argBankType == "Sample", {
				classData.currSampleBankNo = argBankNo;
			},{
				classData.currLoopBankNo = argBankNo;
			});
		});

		// get path/filenames
		Dialog.openPanel({ arg paths;
			this.loadPaths(paths, argBankType);
			// recreate view
			system.showView;
		}, nil, true);
	}

	*searchFolderDialog {
		if (classData.holdArray.size > 0, {
			// get path/filename
			FileDialog.new({ arg paths;
				var holdFolder, missingFileNames, missingIndices, folderFiles, verify, folderFilesIndex, filesFound;
				// search folder for missing files
				holdFolder = PathName.new(paths.asArray[0]);
				folderFiles = holdFolder.files;
				verify = this.verifyBank(classData.holdArray);
				missingFileNames = verify[0].collect({arg item, i; PathName.new(item).fileName;});
				missingIndices = verify[1];
				folderFilesIndex = 0;
				filesFound = 0;
				while ( {(missingFileNames.size > 0) and: {folderFilesIndex < folderFiles.size}}, {
					var replaceIndex, path, pathData, validPath, validPathNumChannels;
					var holdFile = folderFiles[folderFilesIndex];
					var missingIndex = missingFileNames.indexOfEqual(holdFile.fileName);
					if (missingIndex.notNil, {
						replaceIndex = missingIndices[missingIndex];
						path = holdFile.fullPath;
						pathData = this.validatePaths([path], classData.bankType);
						validPath = pathData[0][0];
						validPathNumChannels = pathData[1][0];
						if (validPath.size > 0, {
							classData.holdArray[replaceIndex][0] = holdFile.fullPath;
							classData.holdArray[replaceIndex][2] = validPathNumChannels;
							classData.holdArray[replaceIndex][3] = true;
						});
						// remove from missing arrays
						missingFileNames.removeAt(missingIndex);
						missingIndices.removeAt(missingIndex);
						filesFound = filesFound + 1;
					});
					folderFilesIndex = folderFilesIndex + 1;
				});
				if (filesFound > 0, {
					if (classData.bankType == "Sample", {
						system.reloadAllSamples;
					},{
						system.reloadAllLoops;
					});
					this.updateBank;
					// recreate view
					system.showView;
				}, {
					TXInfoScreen.new("Error: no missing files were found in folder: " ++ paths.asArray[0]);
				});
			}, nil, 2, 0, stripResult: false);
		});
	}

	*replaceSampleDialog {
		if (classData.holdArray.size > 0, {
			// get path/filename
			Dialog.openPanel({ arg path;
				var pathData = this.validatePaths([path], classData.bankType);
				var validPaths = pathData[0];
				var validPathsNumChannels = pathData[1];
				if (validPaths.size > 0, {
					if (classData.bankType == "Sample", {
						classData.holdArray[classData.displaySampleNo] =
						[path, 440, validPathsNumChannels.at(0), true];
						system.reloadAllSamples(classData.currSampleBankNo);
					},{
						classData.holdArray[classData.displayLoopNo] =
						[path, 4, validPathsNumChannels.at(0), true];
						system.reloadAllLoops(classData.currLoopBankNo);
					});
					this.updateBank;
					// recreate view
					system.showView;
				});
			}, nil, false);
		});
	}

	*loadPaths { arg paths, argBankType;
		var holdString;
		var pathData = this.validatePaths(paths, argBankType);
		var validPaths = pathData[0];
		var validPathsNumChannels = pathData[1];
		var validPathsNumBeats; // only used for loops
		if (argBankType == "Sample", {
			classData.holdArray = arrSampleBanks[classData.currSampleBankNo][0]
			++ validPaths.collect({ arg item, i;  [item, 440, validPathsNumChannels.at(i), true]; });
			holdString = " Total no. of  Samples in Bank:  " ++ classData.holdArray.size.asString;
		},{
			validPathsNumBeats = pathData[2];
			classData.holdArray = arrLoopBanks[classData.currLoopBankNo][0]
			++ validPaths.collect({ arg item, i;  [item, validPathsNumBeats.at(i),
				validPathsNumChannels.at(i), true]; });
			holdString = " Total no. of  Loops in Bank:  " ++ classData.holdArray.size.asString;
		});
		this.updateBank;
		if ((system.showWindow == 'Sample bank') or:
			(system.showWindow == 'Loop bank'),
			{
				classData.strNoElements.string = holdString;
		});
	}

	*validatePaths { arg paths, argBankType;
		var validPaths, validPathsNumChannels, validPathsNumBeats, invalidPaths, numBeats;
		validPaths = [];
		validPathsNumChannels = [];
		validPathsNumBeats = [];
		// check validity of pathnames
		paths.asArray.do({ arg item, i;
			var holdFile;
			if (item != "REMOVED", {
				holdFile = SoundFile.new;
				if (File.exists(item) and: {holdFile.openRead(item)}, {
					validPaths = validPaths.add(item);
					validPathsNumChannels = validPathsNumChannels.add(holdFile.numChannels);
					if (argBankType == "Loop", {
						numBeats = this.guessNumBeats(holdFile.numFrames, holdFile.sampleRate);
						validPathsNumBeats = validPathsNumBeats.add(numBeats);
					});
				},{
					invalidPaths = invalidPaths.add(item);
				});
				holdFile.close;
			});
		});
		if (invalidPaths.notNil, {
			TXInfoScreen.new(
				"Error: the following are not valid sound files.",
				arrInfoLines: invalidPaths
			);
		});
		^[validPaths, validPathsNumChannels, validPathsNumBeats];
	}

	*guessNumBeats{arg numFrames, sampleRate;
		var length = numFrames / sampleRate;
		var numbeats, count = 0, scale = 1, testBpm = 0, beatTime;
		// aim for multiple of 4 beats with bpm from 80 to 160
		// 4 beats at 80 bpm = 4 * 60/80 = 3 secs
		if (length > 3, {
			while ( { (count < 100) and: (testBpm <= 80) }, {
				count = count + 1;
				scale = scale * 2;
				numbeats = 4 * scale;
				beatTime = length/numbeats;
				testBpm = 60 / beatTime;
			} );
		}, {
			numbeats = 4;
		});
		^numbeats;
	}

	*updateBank { // method to be run whenever classData.holdArray is updated
		if (classData.bankType == "Sample", {
			arrSampleBanks[classData.currSampleBankNo][0] = classData.holdArray.deepCopy;
			arrSampleBanks[classData.currSampleBankNo][1] = classData.holdCurrentBankName.copy;
		}, {
			arrLoopBanks[classData.currLoopBankNo][0] = classData.holdArray.deepCopy;
			arrLoopBanks[classData.currLoopBankNo][1] = classData.holdCurrentBankName.copy;
		});
	} // end of class method updateBank

	*updatePopBankItems { // method to be run whenever classData.holdArray is updated
		if (classData.bankType == "Sample", {
			classData.holdArrBankNames = arrSampleBanks.collect({arg item, i; i.asString ++ " - " ++ item[1]});
		}, {
			classData.holdArrBankNames = arrLoopBanks.collect({arg item, i; i.asString ++ " - " ++ item[1]});
		});
		classData.popBank.items = classData.holdArrBankNames;
		classData.popBank.value_(classData.holdCurrentBankNo);
	}

	*sizeChange { 	// method to be run whenever size changes
		var holdSampleNo;
		if (classData.bankType == "Sample", {
			holdSampleNo = classData.displaySampleNo;
		},{
			holdSampleNo = classData.displayLoopNo;
		});
		holdSampleNo = holdSampleNo ? 0;
		classData.listSamples = classData.holdArray.collect({ arg item, i;
			var errorText;
			errorText = "";
			if (item.at(3) == false and: {item.at(0) != "REMOVED"}, {
				errorText = "** INVALID FILE: ";
			});
			errorText ++ item.at(0);
		});
		if (classData.holdArray.size == 0, {
			this.setSampleNo(0);
			// for samples
			if (classData.bankType == "Sample", {
				classData.strNoElements.string = " Total no. of Samples in Bank:  0";
				classData.numFreqOrBeats.value = 440;
				classData.numNewFreqOrBeats.value = 440;
				classData.popupMidiNote.value = 46;
			}, { // else for loops
				classData.strNoElements.string = " Total no. of Loops in Bank:  0";
				classData.numFreqOrBeats.value = 8;
				//classData.numNewFreqOrBeats.value = 8;
			});
		}, { // load sample/loop details for current sample number
			this.setSampleNo(holdSampleNo.min(classData.holdArray.size-1));
			if (classData.bankType == "Sample", {
				classData.strNoElements.string = " Total no. of Samples in Bank:  " ++ classData.holdArray.size.asString;
			}, { // else for loops
				classData.strNoElements.string = " Total no. of Loops in Bank:  " ++ classData.holdArray.size.asString;
			});
		});
	} 		// end of class method sizeChange

	*loadSample { 			// method to load samples into buffer
		var holdSampleNo, holdPath;
		if (classData.bankType == "Sample", {
			holdSampleNo = classData.displaySampleNo;
		},{
			holdSampleNo = classData.displayLoopNo;
		});
		if (classData.holdArray.isNil
			or: classData.holdArray.isEmpty
			or: {classData.holdArray.at(holdSampleNo).at(3) == false}
			or: {File.exists(classData.holdArray.at(classData.numSampleNo.value.asInteger).at(0)).not},
			{
				classData.strSampleFileName.string =  "";
				classData.strLength.string = "";
				classData.strNumChannels.string = "";
			},{
				holdPath = classData.holdArray.at(classData.numSampleNo.value.asInteger).at(0);
				classData.holdBuffer = Buffer.read(system.server, holdPath, action: { arg buffer;
					{
						//	if file loaded ok
						if (buffer.notNil, {
							classData.strSampleFileName.string =
								classData.holdArray.at(classData.numSampleNo.value.asInteger).at(0).asString;
							classData.soundFileView.soundfile = SoundFile.new(classData.strSampleFileName.string);
							if (classData.showWaveform == 1, {
								classData.soundFileView.readWithTask( block: 16, showProgress: false);
							});
							classData.numFreqOrBeats.value = classData.holdArray.at(classData.numSampleNo.value.asInteger).at(1);
							classData.strLength.string =
							((buffer.numFrames / buffer.sampleRate).round(0.001) * 1000).asString;
							classData.strNumChannels.string = buffer.numChannels.asString;
							this.updateBPM;
						},{
							classData.strSampleFileName.string =
							classData.holdArray.at(classData.numSampleNo.value.asInteger).at(0) ++ "-  NOT FOUND";
							classData.strLength.string = "";
							classData.strNumChannels.string = "";
						});
					}.defer;	// defer because gui process
				});
				if (classData.holdBuffer.isNil, {
					TXInfoScreen.new("Invalid Sample File" ++ classData.holdArray.at(classData.numSampleNo.value.asInteger).at(0));
				});
		});
	}	// end of class method loadSample

	*updateBPM {
		var sampleArr, beats;
		if (classData.holdBuffer.notNil && classData.numBPM.notNil && (classData.bankType == "Loop"), {
			sampleArr = classData.holdArray.at(classData.numSampleNo.value.asInteger);
			if (sampleArr.notNil, {
				beats =  sampleArr.at(1);
				classData.numBPM.value = 60 * beats * classData.holdBuffer.sampleRate / classData.holdBuffer.numFrames;
			});
		});
	}

	*updateBeatsFromBPM {arg argBPM;
		var bufferTime;
		if (classData.holdBuffer.notNil && classData.numFreqOrBeats.notNil && (classData.bankType == "Loop"), {
			bufferTime = classData.holdBuffer.numFrames / classData.holdBuffer.sampleRate;
			classData.numFreqOrBeats.valueAction = bufferTime  * argBPM/ 60;
		});
	}

	*checkSndFilePaths { arg argBank, bankTypeString, showMessages = true;
		var newBank, holdSoundFile, invalidPaths;
		newBank = argBank.deepCopy;
		newBank = newBank.collect({ arg item, i;
			var newData, holdPath;
			holdSoundFile = SoundFile.new;
			holdPath = item.at(0);
			// Convert path
			holdPath = TXPath.convert(holdPath);
			if (File.exists(holdPath) and: {holdSoundFile.openRead(holdPath)}, {
				newData = item.keep(3).add(true);
			},{
				newData = item.keep(3).add(false);
				invalidPaths = invalidPaths.add(holdPath);
			});
			holdSoundFile.close;
			newData;
		});
		if (invalidPaths.notNil and: (showMessages == true), {
			TXInfoScreen.new(
				bankTypeString ++ " Bank Error - the following are not valid sound files:",
				arrInfoLines: invalidPaths
			);
		});
		^newBank;
	}	// end of class method checkSndFilePaths

}	// end of class
