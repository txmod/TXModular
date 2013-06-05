// Copyright (C) 2011  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXBankBuilder2 {	// Channel Routing

classvar	<>system;	    			// system
classvar	<>arrSampleBanks, <>arrLoopBanks;
classvar	currentSampleBankNo, currentLoopBankNo;
classvar	displayBankType, holdArray, holdArrBankNames, holdCurrentBankNo, holdCurrentBankName, showWaveform;
classvar path, txtQuestion, errorScreen;
classvar strBankType, strNoElements, strNo, btnChangeBank;
classvar strBankNo, popBank, numBankNo, btnBankMinus, btnBankPlus, strBankNameLabel, textBankName;
classvar arrSaveData, bankFile, arrOutput;
classvar btnSaveBank, btnOpenBank, btnEmptyBank, btnAddSamples, btnPlaySample;
classvar btnStopPlay, btnDeleteSample;
classvar strSampleLoop, numSampleNo, signalViewSample, strFileNameTxt, strSampleFileName;
classvar strFreqOrBeatsTxt, strFreqOrBeats, numNewFreqOrBeats, popupMidiNote;
classvar strBPMtxt, strBPM, strLength, strNumChannels;
classvar btnSamplePlus, btnSampleMinus, btnChangeValue, sldVolume, holdSampleView;
classvar specNewFreqOrBeats, holdSynth, holdBuffer, soundFileView, strListTitle, listSamples;

classvar classData; //event dict for data

*initClass{
	classData = ();
	displayBankType = "Sample";
	arrSampleBanks = [ [], "" ]!100;
	arrLoopBanks = [ [], "" ]!100;
	currentSampleBankNo = 0;
	currentLoopBankNo = 0;
	holdArray = [];
	holdCurrentBankNo = 0;
	holdCurrentBankName = "";
	showWaveform = 0;
	classData.samplesVisibleOrigin = Point.new;
	classData.loopsVisibleOrigin = Point.new;
	classData.displaySampleNo = 0;
	classData.displayLoopNo = 0;
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

*makeGui{ arg argView, argDisplayBankType;
	 var parent, holdSampleNo;

	// initialise variables
	displayBankType = argDisplayBankType ?? displayBankType;
	if (displayBankType == "Sample", {
		holdArray = arrSampleBanks[currentSampleBankNo][0].deepCopy;
		holdArrBankNames = arrSampleBanks.collect({arg item, i; i.asString ++ " - " ++ item[1]});
		holdCurrentBankNo = currentSampleBankNo.copy;
		holdCurrentBankName = arrSampleBanks[currentSampleBankNo][1];
	}, {
		holdArray = arrLoopBanks[currentLoopBankNo][0].deepCopy;
		holdArrBankNames = arrLoopBanks.collect({arg item, i; i.asString ++ " - " ++ item[1]});
		holdCurrentBankNo = currentLoopBankNo.copy;
		holdCurrentBankName = arrLoopBanks[currentLoopBankNo][1];
	});

//	argView.decorator.shift(4, 0);
	parent = CompositeView(argView, Rect(0,0, 1060, 600)).background_(TXColor.sysModuleWindow);
	parent.decorator = FlowLayout(parent.bounds);

	if (displayBankType == "Sample", {
		specNewFreqOrBeats = [1, 2000, \linear, 0].asSpec;
	}, {
		specNewFreqOrBeats = [1, 128, \linear, 1].asSpec;
	});

	// spacing
	parent.decorator.nextLine;
	// bank type
	strBankType = StaticText(parent, 300 @ 40)
			.align_(\centre)
			.background_(TXColor.white)
			.stringColor_(TXColor.sysGuiCol1);
	strBankType.font_(Font.new("Helvetica-Bold",18));

	if (displayBankType == "Sample", {
		strBankType.string = "SAMPLE BANKS";
	}, {
		strBankType.string = "LOOP BANKS";
	});

	// spacing
	parent.decorator.nextLine;
	parent.decorator.shift(10, 0);

	// spacing
	parent.decorator.shift(0, 5);
	parent.decorator.nextLine;

	// divider
	StaticText(parent, 1050 @ 1) .string_("") .background_(TXColor.sysGuiCol1);

	// bank no.
	strBankNo = StaticText (parent, 80 @ 24)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);
	strBankNo.string = "Bank No.";

	popBank = PopUpMenu (parent, 14 @ 24).value_(holdCurrentBankNo)
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);
	popBank.items = holdArrBankNames;
	popBank.value = holdCurrentBankNo;

	numBankNo = NumberBox (parent, 30 @ 24).scroll_(false).value_(holdCurrentBankNo);
	btnBankMinus = Button(parent, 20 @ 24)
		.states = [["-", TXColor.white, TXColor.sysGuiCol1]];
	btnBankPlus = Button(parent, 20 @ 24)
		.states = [["+", TXColor.white, TXColor.sysGuiCol1]];

	// spacing
	parent.decorator.shift(10, 0);
	// Bank name
	strBankNameLabel = StaticText(parent, 100 @ 24)
			.background_(TXColor.white)
			.stringColor_(TXColor.sysGuiCol1);
	strBankNameLabel.string =  "Bank Name";
	textBankName = TextField(parent, 300 @ 24)
			.string_(holdCurrentBankName)
		//	.align_(\right)
			.background_(TXColor.white)
//			.font_(Font("Gill Sans", 11))
			.stringColor_(TXColor.sysGuiCol1);

	// spacing
	parent.decorator.shift(0, 5);
	parent.decorator.nextLine;

	// no. samples
	strNoElements = StaticText(parent, 200 @ 24)
		.string_("Total no. of Samples in Bank: 0")
		.background_(TXColor.white)
		.stringColor_(TXColor.sysGuiCol1);

	// spacing
	parent.decorator.shift(10, 0);
	// button
	btnAddSamples = Button(parent, 150 @ 24);
	if (displayBankType == "Sample", {
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
	parent.decorator.shift(0, 5);
	parent.decorator.nextLine;

	// sample selection
	strListTitle = StaticText(parent, 186 @ 24)
			.string_("Select:")
			.background_(TXColor.white)
			.stringColor_(TXColor.sysGuiCol1);
	if (displayBankType == "Sample", {
		strListTitle.string = "Select Sample:";
	},{
		strListTitle.string = "Select Loop:";
	});
	listSamples = holdArray.collect({ arg item, i;
		var errorText;
		errorText = "";
		if (item.at(3) == false and: {item.at(0) != "REMOVED"}, {
			errorText = "** INVALID FILE: ";
		});
		errorText ++ item.at(0);
	});

	// make box
	classData.sampleListBox =  CompositeView(parent, Rect(0,0, 850, 200));
	classData.sampleListBox.background = TXColour.sysChannelAudio;
	classData.sampleListBox.decorator = FlowLayout(classData.sampleListBox.bounds);
	classData.sampleListBox.decorator.margin.x = 0;
	classData.sampleListBox.decorator.margin.y = 0;
	classData.sampleListBox.decorator.gap = Point(0,0);
	classData.sampleListBox.decorator.reset;

	classData.samplesScrollView = ScrollView(classData.sampleListBox, Rect(0,0, 842, 192))
		.hasBorder_(false);
	if (GUI.current.asSymbol == \cocoa, {
		classData.samplesScrollView.autoScrolls_(false);
	});

	classData.samplesScrollView.action = {arg view;
		if (displayBankType == "Sample", {
			classData.samplesVisibleOrigin = view.visibleOrigin;
		},{
			classData.loopsVisibleOrigin = view.visibleOrigin;
		});
	};

	classData.samplesBox = CompositeView(classData.samplesScrollView,
		Rect(0,0, 828, ((listSamples.size + 1) * 22).max(192)));
	classData.samplesBox.decorator = FlowLayout(classData.samplesBox.bounds);
	classData.samplesBox.decorator.margin.x = 0;
	classData.samplesBox.decorator.margin.y = 0;
	classData.samplesBox.decorator.gap = Point(2,2);
	classData.samplesBox.decorator.reset;
	classData.samplesBox.background =TXColor.white;

	if (displayBankType == "Sample", {
		holdSampleNo = classData.displaySampleNo;
	},{
		holdSampleNo = classData.displayLoopNo;
	});

	listSamples.do({arg item, i;
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

	if (listSamples.size > 0, {
		if (displayBankType == "Sample", {
			classData.samplesScrollView.visibleOrigin = classData.samplesVisibleOrigin;
		},{
			classData.samplesScrollView.visibleOrigin = classData.loopsVisibleOrigin;
		});
	});

	// spacing
	parent.decorator.nextLine;
	parent.decorator.shift(0, -32);

	// TXCheckBox: arg argParent, bounds, text, offStringColor, offBackground,
	// onStringColor, onBackground, onOffTextType=0;
	classData.displayWaveform = TXCheckBox(parent, 140 @ 24, "Show waveform", TXColor.sysGuiCol1, TXColour.white,
		TXColor.white, TXColor.sysGuiCol1);
	classData.displayWaveform.value = showWaveform;

	// spacing
	parent.decorator.nextLine;

	// show soundfile
	soundFileView = SoundFileView.new(parent, 1020 @ 100)
		.gridOn_(false).timeCursorOn_(false)
		.waveColors_(Color.blue(alpha: 1.0) ! 8)
		.background_(Color(0.65,0.65,0.95));

	// spacing
	parent.decorator.shift(0, 10);
	parent.decorator.nextLine;

	// button
	btnPlaySample = Button(parent, 200 @ 24);
	if (displayBankType == "Sample", {
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
	if (displayBankType == "Sample", {
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
		.stringColor_(TXColor.sysGuiCol1);
	if (displayBankType == "Sample", {
		strSampleLoop.string = "Sample no.";
	}, {
		strSampleLoop.string = "Loop no.";
	});
	numSampleNo = NumberBox (parent, 30 @ 24).scroll_(false).value_(0);
	btnSampleMinus = Button(parent, 20 @ 24).states = [["-", TXColor.white, TXColor.sysGuiCol1]];
	btnSamplePlus = Button(parent, 20 @ 24).states = [["+", TXColor.white, TXColor.sysGuiCol1]];

	// file name
	strFileNameTxt = StaticText(parent, 100 @ 24)
			.background_(TXColor.white)
			.stringColor_(TXColor.sysGuiCol1);
	if (displayBankType == "Sample", {
		strFileNameTxt.string =  "Sample File";
	}, {
		strFileNameTxt.string =  "Loop File";
	});
	strSampleFileName = StaticText(parent, 475 @ 24)
			.string_(" ")
		//	.align_(\right)
			.background_(TXColor.white)
			.stringColor_(TXColor.sysGuiCol1)
			.font_(Font("Gill Sans", 11));

	// spacing
	parent.decorator.shift(5, 0);
	// length
	StaticText(parent, 80 @ 24)
			.string_("Length (ms)")
			.background_(TXColor.white)
			.stringColor_(TXColor.sysGuiCol1);
	strLength = StaticText(parent, 50 @ 24)
			.string_("")
			.background_(TXColor.white)
			.stringColor_(TXColor.sysGuiCol1);

	// spacing
	parent.decorator.shift(5, 0);
	// no. channels
	StaticText(parent, 80 @ 24)
			.string_("No. Channels")
			.background_(TXColor.white)
			.stringColor_(TXColor.sysGuiCol1);
	strNumChannels = StaticText(parent, 50 @ 24)
			.string_("")
			.background_(TXColor.white)
			.stringColor_(TXColor.sysGuiCol1);

	// spacing
	parent.decorator.shift(0, 10);
	parent.decorator.nextLine;

	// frequency/ no. beats
	strFreqOrBeatsTxt = StaticText(parent, 116 @ 24)
			.background_(TXColor.white)
			.stringColor_(TXColor.sysGuiCol1);
	if (displayBankType == "Sample", {
		strFreqOrBeatsTxt.string =  "Frequency";
	}, {
		strFreqOrBeatsTxt.string =  "Total Beats";
	});
	strFreqOrBeats = StaticText(parent, 80 @ 24)
			.string_("")
			.background_(TXColor.white)
			.stringColor_(TXColor.sysGuiCol1);

	// spacing
	parent.decorator.shift(10, 0);
	// set frequency/ no. Beats
	btnChangeValue = Button(parent, 146 @ 24);
	if (displayBankType == "Sample", {
		btnChangeValue.states = [["Set Frequency to:", TXColor.white, TXColor.sysGuiCol1]]
	}, {
		btnChangeValue.states = [["Set Total Beats to:", TXColor.white, TXColor.sysGuiCol1]]
	});

	numNewFreqOrBeats = NumberBox (parent, 50 @ 24).scroll_(false);
	if (displayBankType == "Sample", {
		numNewFreqOrBeats.value = 440;
	}, {
		numNewFreqOrBeats.value = 1;
	});

	if (displayBankType == "Sample", {
		popupMidiNote = PopUpMenu(parent, 50 @ 24)
			.stringColor_(TXColor.sysGuiCol1).background_(TXColor.white);
		popupMidiNote.items = ["notes"]
			++ 103.collect({arg item; TXGetMidiNoteString(item + 24);});
		popupMidiNote.action = {
			if (popupMidiNote.value > 0, {
				numNewFreqOrBeats.value = (popupMidiNote.value + 23).midicps.round(0.01);
			});
		};
		popupMidiNote.value = 46;
	});

	// spacing
	parent.decorator.shift(10, 0);

	if (displayBankType == "Loop", {
		// bpm text (only for loops)
		strBPMtxt = StaticText(parent, 120 @ 24)
			.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
		strBPMtxt.string =  "Equivalent BPM: ";
		strBPM = StaticText(parent, 76 @ 24).string_(" ")
			.background_(TXColor.white).stringColor_(TXColor.sysGuiCol1);
	}); // end of if banktype == "Loop"

	// spacing
	parent.decorator.nextLine;

	////////////////////////////////////////////////////////////////////////////////////////
	// DEFINE GUI ACTIONS

	numBankNo.action =
		{arg view;
			holdCurrentBankNo = view.value.min(99).max(0);
			if (displayBankType == "Sample", {
				currentSampleBankNo = holdCurrentBankNo;
				classData.samplesVisibleOrigin = Point.new;
			}, {
				currentLoopBankNo = holdCurrentBankNo;
				classData.loopsVisibleOrigin = Point.new;
			});
			// reset sampleno
			this.setSampleNo(0);
			// recreate view
			system.showView;
		};

	btnBankPlus.action =
		{
		numBankNo.value = (numBankNo.value + 1);
		numBankNo.doAction;
		};

	btnBankMinus.action =
		{
		numBankNo.value = (numBankNo.value - 1);
		numBankNo.doAction;
		};

	popBank.action =
		{arg view;
		numBankNo.value = view.value;
		numBankNo.doAction;
		};

	textBankName.action =
		{arg view;
			holdCurrentBankName = view.string;
			this.updateBank;
		};

	btnOpenBank.action =
		{var errFound, errorScreen, fileType;
		var newPath, newFile, newData;
		errFound = 0;
		if (displayBankType == "Sample", {
			fileType = "SampleBank";
		},{
			fileType = "LoopBank";
		});
		Dialog.getPaths({ arg paths;
			newPath = paths.at(0);
			newFile = File(newPath,"r");
			newData = thisProcess.interpreter.executeFile(newPath);
			newFile.close;
			if (newData.notNil, {
				if (newData.isArray and: (newData.size > 1) and: (newData.at(0)==fileType),{
					holdArray = holdArray.asArray ++ newData.at(1).deepCopy;
					holdArray = this.checkSndFilePaths(holdArray, displayBankType);
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
		if (holdArray.size > 0, {
			arrSaveData = holdArray;

			arrOutput = [displayBankType++"Bank", arrSaveData];

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
		holdArray = [];
		this.updateBank;
		this.sizeChange;
		showWaveform = 0;
		// recreate view
		system.showView;
		};

	btnAddSamples.action = {this.addSampleDialog(displayBankType); };

	btnDeleteSample.action = {
		holdArray[numSampleNo.value][0] = "REMOVED";
		holdArray[numSampleNo.value][3] = false;
		this.updateBank;
		showWaveform = 0;
		// recreate view
		system.showView;
	};

	classData.displayWaveform.action = {arg view; showWaveform = view.value; system.showView};

	numSampleNo.action = {arg view;
		this.setSampleNo(view.value);
	};

	btnSamplePlus.action = {
		numSampleNo.value = (numSampleNo.value + 1).min(holdArray.size-1).max(0);
		this.setSampleNo(numSampleNo.value);
	};

	btnSampleMinus.action =
		{
		numSampleNo.value = (numSampleNo.value - 1).max(0);
		this.setSampleNo(numSampleNo.value);
		};

	btnChangeValue.action =
		{if (holdArray.notEmpty, {
			holdArray.at(numSampleNo.value.asInteger).put(1, numNewFreqOrBeats.value);
			this.updateBank;
			this.loadSample;
			});
		};

	numNewFreqOrBeats.action = { |view| view.value = specNewFreqOrBeats.constrain(view.value)};

	btnPlaySample.action = {
		// release any synth running
		btnStopPlay.doAction;
		if (holdArray.notEmpty and: holdBuffer.notNil, {
			holdSynth = { sldVolume.value *
				PlayBuf.ar(1, holdBuffer.bufnum, BufRateScale.kr(holdBuffer.bufnum)) }.play;
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
	sampleNo = sampleNo.min(holdArray.size-1).max(0);
	if (displayBankType == "Sample", {
		classData.displaySampleNo = sampleNo;
	},{
		classData.displayLoopNo = sampleNo;
	});
	numSampleNo.value = sampleNo;
	this.loadSample;
}

*addSampleDialog { arg argBankType = "Sample", argBankNo;
	var holdString;
		// set displayBankType so updates are correct when adding samples
		displayBankType = argBankType ?? displayBankType;
		// set bank no if given
		if (argBankNo.notNil, {
			if (argBankType == "Sample", {
				currentSampleBankNo = argBankNo;
			},{
				currentLoopBankNo = argBankNo;
			});
		});

		// get path/filenames
		Dialog.getPaths({ arg paths;
			var validPaths, validPathsNumChannels, invalidPaths;
			// check validity of pathnames
			paths.do({ arg item, i;
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
				holdArray = arrSampleBanks[currentSampleBankNo][0]
				 ++ validPaths.collect({ arg item, i;  [item, 440, validPathsNumChannels.at(i), true]; });
				holdString = "Total no. of  Samples in Bank:  " ++ holdArray.size.asString;
			},{
				holdArray = arrLoopBanks[currentLoopBankNo][0]
				 ++ validPaths.collect({ arg item, i;  [item, 4, validPathsNumChannels.at(i), true]; });				 holdString = "Total no. of  Loops in Bank:  " ++ holdArray.size.asString;
			});
			this.updateBank;
			if ((system.showWindow == "Sample bank") or:
				(system.showWindow == "Loop bank"),
			{
				strNoElements.string = holdString;
			});
			// recreate view
			system.showView;
		}, nil, true);
}

*updateBank { // method to be run whenever holdArray is updated
	if (displayBankType == "Sample", {
		arrSampleBanks[currentSampleBankNo][0] = holdArray.deepCopy;
		arrSampleBanks[currentSampleBankNo][1] = holdCurrentBankName.copy;
	}, {
		arrLoopBanks[currentLoopBankNo][0] = holdArray.deepCopy;
		arrLoopBanks[currentLoopBankNo][1] = holdCurrentBankName.copy;
	});
} // end of class method updateBank

*sizeChange { 	// method to be run whenever size changes
	var holdSampleNo;
	if (displayBankType == "Sample", {
		holdSampleNo = classData.displaySampleNo;
	},{
		holdSampleNo = classData.displayLoopNo;
	});
	holdSampleNo = holdSampleNo ? 0;
	listSamples = holdArray.collect({ arg item, i;
		var errorText;
		errorText = "";
		if (item.at(3) == false and: {item.at(0) != "REMOVED"}, {
			errorText = "** INVALID FILE: ";
		});
		errorText ++ item.at(0);
	});
	if (holdArray.size == 0, {
		this.setSampleNo(0);
		 // for samples
		if (displayBankType == "Sample", {
			strNoElements.string = "Total no. of Samples in Bank:  0";
			numNewFreqOrBeats.value = 440;
			popupMidiNote.value = 46;
		}, { // else for loops
			strNoElements.string = "Total no. of Loops in Bank:  0";
			numNewFreqOrBeats.value = 1;
		});
	}, { // load sample/loop details for current sample number
		this.setSampleNo(holdSampleNo.min(holdArray.size-1));
		if (displayBankType == "Sample", {
				strNoElements.string = "Total no. of Samples in Bank:  " ++ holdArray.size.asString;
		}, { // else for loops
			strNoElements.string = "Total no. of Loops in Bank:  " ++ holdArray.size.asString;
		});
	});
} 		// end of class method sizeChange

*loadSample { 			// method to load samples into buffer
	var holdSampleNo;
	if (displayBankType == "Sample", {
		holdSampleNo = classData.displaySampleNo;
	},{
		holdSampleNo = classData.displayLoopNo;
	});
	if (holdArray.isNil or: holdArray.isEmpty or: {holdArray.at(holdSampleNo).at(3) == false}, {
		strSampleFileName.string =  "";
		strFreqOrBeats.string = "";
		strLength.string = "";
		strNumChannels.string = "";
		if (displayBankType == "Loop", {strBPM.string =  ""; });
	},{
		holdBuffer =
			Buffer.read(system.server,holdArray.at(numSampleNo.value.asInteger).at(0), action: { arg buffer;
			{
			//	if file loaded ok
				if (buffer.notNil, {
					strSampleFileName.string =  holdArray.at(numSampleNo.value.asInteger).at(0).asString;
					soundFileView.soundfile = SoundFile.new(strSampleFileName.string);
					if (showWaveform == 1, {
						soundFileView.readWithTask( block: 16, showProgress: false);
					});
					strFreqOrBeats.string = holdArray.at(numSampleNo.value.asInteger).at(1).asString;
					strLength.string =
						((buffer.numFrames / buffer.sampleRate).round(0.001) * 1000).asString;
					strNumChannels.string = buffer.numChannels.asString;
					if (displayBankType == "Loop", {
						strBPM.string =
							((60 * buffer.sampleRate * holdArray.at(numSampleNo.value.asInteger).at(1))
								/ buffer.numFrames ).round(0.01).asString;
					});
				},{
					strSampleFileName.string =
						holdArray.at(numSampleNo.value.asInteger).at(0) ++ "-  NOT FOUND";
					strLength.string = "";
					strNumChannels.string = "";
					if (displayBankType == "Loop", {strBPM.string =  ""; });
				});
			}.defer;	// defer because gui process
		});
		if (holdBuffer.isNil, {
			TXInfoScreen.new("Invalid Sample File" ++ holdArray.at(numSampleNo.value.asInteger).at(0));
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
