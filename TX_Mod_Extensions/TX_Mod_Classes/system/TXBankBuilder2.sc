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

	*makeSampleGui{arg argView;  this.makeGui(argView, "Sample"); }

	*makeLoopGui{arg argView;  this.makeGui(argView, "Loop"); }

	*makeGui{ arg argView, argBankType;
		var parent, holdSampleNo, arrSaveData, arrOutput, strBankType;

		var numBankNo, btnBankMinus, btnBankPlus, strBankNameLabel, textBankName;
		var btnSaveBank, btnOpenBank, btnEmptyBank, btnAddSamples, btnPlaySample;
		var btnStopPlay, btnDeleteSample;
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
			specFreqOrBeats = [1, 2000, \linear, 0].asSpec;
		}, {
			specFreqOrBeats = [1, 128, \linear, 1].asSpec;
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
		classData.strNoElements = StaticText(parent, 240 @ 24)
		.string_(" Total no. of Samples in Bank: 0")
		.align_(\left)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);

		// spacing
		parent.decorator.shift(10, 0);
		// button
		btnAddSamples = Button(parent, 150 @ 24);
		if (classData.bankType == "Sample", {
			btnAddSamples.states = [["Add Samples to Bank", TXColor.white, TXColor.sysGuiCol1]];
		}, {
			btnAddSamples.states = [["Add Loops to Bank", TXColor.white, TXColor.sysGuiCol1]];
		});

		// spacing
		parent.decorator.shift(10, 0);
		// button
		btnOpenBank = Button(parent, 150 @ 24).states = [["Load Bank", TXColor.white, TXColor.sysGuiCol2]];
		// spacing
		parent.decorator.shift(10, 0);
		// button
		btnSaveBank = Button(parent, 150 @ 24).states = [["Save Bank", TXColor.white, TXColor.sysGuiCol2]];
		// spacing
		parent.decorator.shift(10, 0);
		// button
		btnEmptyBank = Button(parent, 150 @ 24).states = [["Empty Bank", TXColor.white, TXColor.sysDeleteCol]];

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
			var errorText;
			errorText = "";
			if (item.at(3) == false and: {item.at(0) != "REMOVED"}, {
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
		btnPlaySample = Button(parent, 200 @ 24);
		if (classData.bankType == "Sample", {
			btnPlaySample.states = [["Play Sample", TXColor.white, TXColor.sysGuiCol1]];
		},{
			btnPlaySample.states = [["Play Loop", TXColor.white, TXColor.sysGuiCol1]];
		});

		// spacing
		parent.decorator.shift(10, 0);
		// button
		btnStopPlay = Button(parent, 200 @ 24).states = [["Stop Playing", TXColor.white, TXColor.sysGuiCol1]];
		// spacing
		parent.decorator.shift(10, 0);
		// volume slider
		sldVolume = EZSlider(parent, 190 @ 24, "Volume", labelWidth: 45, numberWidth: 40)
		.setColors(TXColor.white, TXColor.sysGuiCol1)
		.value_(0.5);

		// spacing
		parent.decorator.shift(20, 0);
		// button
		btnDeleteSample = Button(parent, 200 @ 24);
		if (classData.bankType == "Sample", {
			btnDeleteSample.states = [["Remove Sample from Bank", TXColor.white, TXColor.sysDeleteCol]];
		}, {
			btnDeleteSample.states = [["Remove Loop from Bank", TXColor.white, TXColor.sysDeleteCol]];
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

		// spacing
		parent.decorator.shift(10, 0);
		// set frequency/ no. Beats
		btnChangeValue = Button(parent, 120 @ 24);
		if (classData.bankType == "Sample", {
			btnChangeValue.states = [["Set Frequency to:", TXColor.white, TXColor.sysGuiCol1]]
		}, {
			btnChangeValue.states = [["Set Total Beats to:", TXColor.white, TXColor.sysGuiCol1]]
		});

		classData.numNewFreqOrBeats = NumberBox (parent, 76 @ 24).scroll_(false);
		if (classData.bankType == "Sample", {
			classData.numNewFreqOrBeats.value = classData.holdNewFreq;
		}, {
			classData.numNewFreqOrBeats.value = classData.holdNewBeats;
		});

		if (classData.bankType == "Sample", {
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
		});

		// spacing
		parent.decorator.nextLine.shift(0, 6);

		if (classData.bankType == "Loop", {
			// bpm text (only for loops)
			strBPMtxt = StaticText(parent, 116 @ 24)
			.align_(\center)
			.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
			strBPMtxt.string =  "Equivalent BPM: ";
			classData.strBPM = StaticText(parent, 76 @ 24).string_(" ")
			.align_(\center)
			.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
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
				errorScreen = TXInfoScreen.new("ERROR: invalid " ++ fileType ++ ": " ++ newFile.path);		});
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

		btnDeleteSample.action = {
			classData.holdArray[classData.numSampleNo.value][0] = "REMOVED";
			classData.holdArray[classData.numSampleNo.value][3] = false;
			this.updateBank;
			classData.showWaveform = 0;
			// recreate view
			system.showView;
		};

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
				this.updateBank;
				this.loadSample;
			});
		};

		btnChangeValue.action = {
			if (classData.holdArray.notEmpty, {
				classData.holdArray.at(classData.numSampleNo.value.asInteger).put(1, classData.numNewFreqOrBeats.value);
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

		btnPlaySample.action = {
			// release any synth running
			btnStopPlay.doAction;
			if (classData.holdArray.notEmpty and: classData.holdBuffer.notNil, {
				holdSynth = { sldVolume.value *
					PlayBuf.ar(1, classData.holdBuffer.bufnum, BufRateScale.kr(classData.holdBuffer.bufnum)) }.play;
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
		var holdString;
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
			var validPaths, validPathsNumChannels, invalidPaths;
			// check validity of pathnames
			paths.asArray.do({ arg item, i;
				var holdFile;
				holdFile = SoundFile.new;
				if (holdFile.openRead(item), {
					validPaths = validPaths.add(item);
					validPathsNumChannels = validPathsNumChannels.add(holdFile.numChannels);
				},{
					invalidPaths = invalidPaths.add(item);
				});
				holdFile.close;
			});
			if (invalidPaths.notNil, {
				TXInfoScreen.new(
					"Error: the following are not valid sound files.",
					arrInfoLines: invalidPaths
				);
			});

			if (argBankType == "Sample", {
				classData.holdArray = arrSampleBanks[classData.currSampleBankNo][0]
				++ validPaths.collect({ arg item, i;  [item, 440, validPathsNumChannels.at(i), true]; });
				holdString = " Total no. of  Samples in Bank:  " ++ classData.holdArray.size.asString;
			},{
				classData.holdArray = arrLoopBanks[classData.currLoopBankNo][0]
				++ validPaths.collect({ arg item, i;  [item, 4, validPathsNumChannels.at(i), true]; });
				holdString = " Total no. of  Loops in Bank:  " ++ classData.holdArray.size.asString;
			});
			this.updateBank;
			if ((system.showWindow == 'Sample bank') or:
				(system.showWindow == 'Loop bank'),
				{
					classData.strNoElements.string = holdString;
			});
			// recreate view
			system.showView;
		}, nil, true);
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
				classData.numNewFreqOrBeats.value = 8;
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
		var holdSampleNo;
		if (classData.bankType == "Sample", {
			holdSampleNo = classData.displaySampleNo;
		},{
			holdSampleNo = classData.displayLoopNo;
		});
		if (classData.holdArray.isNil or: classData.holdArray.isEmpty or: {classData.holdArray.at(holdSampleNo).at(3) == false}, {
			classData.strSampleFileName.string =  "";
			classData.strLength.string = "";
			classData.strNumChannels.string = "";
			if (classData.bankType == "Loop", {classData.strBPM.string =  ""; });
		},{
			classData.holdBuffer =
			Buffer.read(system.server,classData.holdArray.at(classData.numSampleNo.value.asInteger).at(0), action: { arg buffer;
				{
					//	if file loaded ok
					if (buffer.notNil, {
						classData.strSampleFileName.string =  classData.holdArray.at(classData.numSampleNo.value.asInteger).at(0).asString;
						classData.soundFileView.soundfile = SoundFile.new(classData.strSampleFileName.string);
						if (classData.showWaveform == 1, {
							classData.soundFileView.readWithTask( block: 16, showProgress: false);
						});
						classData.numFreqOrBeats.value = classData.holdArray.at(classData.numSampleNo.value.asInteger).at(1);
						classData.strLength.string =
						((buffer.numFrames / buffer.sampleRate).round(0.001) * 1000).asString;
						classData.strNumChannels.string = buffer.numChannels.asString;
						if (classData.bankType == "Loop", {
							classData.strBPM.string =
							((60 * buffer.sampleRate * classData.holdArray.at(classData.numSampleNo.value.asInteger).at(1))
								/ buffer.numFrames ).round(0.01).asString;
						});
					},{
						classData.strSampleFileName.string =
						classData.holdArray.at(classData.numSampleNo.value.asInteger).at(0) ++ "-  NOT FOUND";
						classData.strLength.string = "";
						classData.strNumChannels.string = "";
						if (classData.bankType == "Loop", {classData.strBPM.string =  ""; });
					});
				}.defer;	// defer because gui process
			});
			if (classData.holdBuffer.isNil, {
				TXInfoScreen.new("Invalid Sample File" ++ classData.holdArray.at(classData.numSampleNo.value.asInteger).at(0));
			});
		});
	}	// end of class method loadSample

	*checkSndFilePaths { arg argBank, bankTypeString, showMessages = true;
		var newBank, holdSoundFile, invalidPaths;
		newBank = argBank.deepCopy;
		newBank = newBank.collect({ arg item, i;
			var newData, holdPath;
			holdSoundFile = SoundFile.new;
			holdPath = item.at(0);
			// Convert path
			holdPath = TXPath.convert(holdPath);
			if (holdSoundFile.openRead(holdPath), {
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
