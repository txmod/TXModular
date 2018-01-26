// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXMIDIKeyboardWindow {
	classvar <>keyboardWindow;
	classvar classData;

	*initClass {
		classData = ();
		classData.windowWidth = 1100;
		classData.windowHeight = 300;
		classData.windowVisibleOrigin = Point.new(0,0);
		classData.alwaysOnTop = 1;
		classData.lastMIDINote = 60;

		this.resetValues;
		this.clearViews;
	}

	*closeWindow {
		if (keyboardWindow.notNil, {
			keyboardWindow.close;
		});
		this.clearViews;
	}

	*resetValues {
		classData.midiNoteVel = 100;
		classData.midiChannel = 1;
		classData.showScale = 0;
		classData.keyIndex = 0; // key C
		classData.scaleIndex = 89; // scale Major
	}

	*rebuildWindow {
		this.closeWindow;
		{this.showWindow;}.defer(0.05);
	}

	*clearViews {
		classData.viewBox = nil;
	}
	//======================24

	*showWindow {
		var holdView;
		// var plusButton, minusButton;

		// create window if needed
		if (keyboardWindow.isNil, {
			keyboardWindow = Window.new(
				"MIDI Keyboard - trigger MIDI notes in the TX Modular",
				Rect(400, 800, classData.windowWidth + 8, classData.windowHeight + 8),
				resizable: true, scroll: true
			).front;
			keyboardWindow.view.background = TXColor.grey(0.8);
			keyboardWindow.view.action = {arg view; classData.windowVisibleOrigin = view.visibleOrigin;};
			keyboardWindow.view.mouseWheelAction = {arg view; classData.windowVisibleOrigin = view.visibleOrigin;};
			keyboardWindow.onClose_({this.keyboardWindow = nil;});
			keyboardWindow.alwaysOnTop = classData.alwaysOnTop.asBoolean;
		});
		keyboardWindow.front;

		// defer visible origin
		// {keyboardWindow.view.visibleOrigin = classData.windowVisibleOrigin;}.defer(0.1); // not needed

		if (classData.viewBox.notNil, {
			TXSystem1GUI.deferRemoveView(classData.viewBox);
		});

		classData.viewBox = CompositeView(keyboardWindow, Rect(0, 0, classData.windowWidth, classData.windowHeight));
		classData.viewBox.decorator = FlowLayout(classData.viewBox.bounds);
		classData.viewBox.decorator.gap = 4@4;

		holdView = TXCheckBox(classData.viewBox, Rect(0, 0, 120, 24), "window on top", TXColor.sysGuiCol2,
			TXColor.white, TXColor.white, TXColor.sysGuiCol1);
		holdView.value =  classData.alwaysOnTop;
		holdView.action = {|view|
			classData.alwaysOnTop = view.value;
			keyboardWindow.alwaysOnTop = classData.alwaysOnTop.asBoolean;
		};

		// spacer
		classData.viewBox.decorator.shift(40, 0);

		holdView = Button(classData.viewBox, Rect(0, 0, 110, 24));
		holdView.states = [["Open Clipboard", TXColor.white, TXColor.sysGuiCol2]];
		holdView.action = {TXClipboardWindow.showWindow;};

		// new line
		classData.viewBox.decorator.nextLine.shift(0, 8);

		// label
		holdView = StaticText.new(classData.viewBox, 300 @ 24)
		.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white)
		.string_("MIDI Notes: C0 - B6   Middle C (C3) is highlighted pink")
		.align_('center');

		// spacer
		classData.viewBox.decorator.shift(40, 0);

		// Velocity slider
		holdView = TXSlider(classData.viewBox, 240 @ 24, "MIDI Velocity",  ControlSpec(0, 127, step: 1),
			{|view|
				// store current data
				classData.midiNoteVel = view.value;
			},
			// get starting value
			classData.midiNoteVel,
			false, 86, 30
		);
		holdView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		holdView.sliderView.background_(TXColour.sysGuiCol1);

		// spacer
		classData.viewBox.decorator.shift(40, 0);

		// Note Time slider
		// holdView = TXSlider(classData.viewBox, 180 @ 24, "Note time",  ControlSpec(0.05, 20,),
		// 	{|view|
		// 		// store current data
		// 		classData.midiNoteTime = view.value;
		// 	},
		// 	// get starting value
		// 	classData.midiNoteTime,
		// 	false, 60, 30
		// );
		// holdView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		// holdView.sliderView.background_(TXColour.sysGuiCol1);

		// spacer
		//classData.viewBox.decorator.shift(40, 0);

		// MIDI Channel slider
		holdView = TXSlider(classData.viewBox, 200 @ 24, "MIDI Channel",  ControlSpec(1, 16, step: 1),
			{|view|
				// store current data
				classData.midiChannel = view.value;
			},
			// get starting value
			classData.midiChannel,
			false, 80, 30
		);
		holdView.labelView.stringColor_(TXColour.sysGuiCol1).background_(TXColor.white);
		holdView.sliderView.background_(TXColour.sysGuiCol1);

		classData.viewBox.decorator.shift(40, 0);

		holdView = Button(classData.viewBox, Rect(0, 0, 190, 24));
		holdView.states = [["Reset controls to default values", TXColor.white, TXColor.sysDeleteCol]];
		holdView.action = {this.resetValues; this.showWindow;};

		// new line
		classData.viewBox.decorator.nextLine.shift(0, 8);

		// Midi Keyboard
		classData.keyboardView = TXMIDIKeyboard.new(classData.viewBox,
			Rect(0, 0, classData.windowWidth - 10, classData.windowHeight - 200), 7, 24);
		classData.keyboardView.keyDownAction_({arg newNote;
			MIDIIn.doNoteOnAction(0, classData.midiChannel - 1, newNote, classData.midiNoteVel);
			classData.lastMIDINote = newNote;
			this.updateLastNotePlayed;
		});
		classData.keyboardView.keyUpAction_({arg newNote;
			MIDIIn.doNoteOffAction(0, classData.midiChannel - 1, newNote, 0);
			classData.lastMIDINote = newNote;
			this.updateLastNotePlayed;

		});
		classData.keyboardView.keyTrackAction_({arg newNote, oldNote;
			MIDIIn.doNoteOffAction(0, classData.midiChannel - 1, oldNote, 0);
			MIDIIn.doNoteOnAction(0, classData.midiChannel - 1, newNote, classData.midiNoteVel);
			classData.lastMIDINote = newNote;
			this.updateLastNotePlayed;
		});
		// show middle C
		classData.keyboardView.setColor(60, classData.keyboardView.getColor(60).blend(TXColour.red, 0.5));

		// new line + shift
		classData.viewBox.decorator.nextLine.shift(0, 8);

		// Last Note Number draggable
		classData.lastNoteNumber = DragSource.new(classData.viewBox, 236 @ 24)
		.stringColor_(TXColour.grey(0.2)).background_(TXColor.paleYellow)
		.align_('left');
		classData.lastNoteNumber.setBoth_(false);

		// spacer
		classData.viewBox.decorator.shift(10, 0);

		// Last Note Freq draggable
		classData.lastNoteFreq = DragSource.new(classData.viewBox, 210 @ 24)
		.stringColor_(TXColour.grey(0.2)).background_(TXColor.paleYellow)
		.align_('left');
		classData.lastNoteFreq.setBoth_(false);

		// spacer
		classData.viewBox.decorator.shift(30, 0);

		// Show Scale
		holdView = TXCheckBox(classData.viewBox, Rect(0, 0, 90, 24), "Show Scale", TXColor.sysGuiCol2,
			TXColor.white, TXColor.white, TXColor.sysGuiCol1);
		holdView.value =  classData.showScale;
		holdView.action = {|view|
			classData.showScale = view.value;
			this.updateScale;
		};

		// spacer
		classData.viewBox.decorator.shift(6, 0);

		// Key
		classData.keyView = TXListViewLabelPlusMinus (classData.viewBox, 150 @ 94, "Key/Root",
			["C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", ],
			{|view|
				// store current data
				classData.keyIndex = view.value;
				this.updateScale;
			}, classData.keyIndex, false, 50
		);

		// spacer
		classData.viewBox.decorator.shift(10, 0);

		// ScaleIndex
		classData.scaleIndexView = TXListViewLabelPlusMinus(classData.viewBox, 330 @ 94, "Scale", TXScale.arrScaleNames,
			{|view|
			// store current data
			classData.scaleIndex = view.value;
			this.updateScale;
		}, classData.scaleIndex, false, 60);
		classData.scaleIndexView.listView.resize = 5;

		//classData.viewBox.decorator.shift(40, 0);

		// initialise
		this.updateLastNotePlayed;
		this.updateScale;
	}

	*updateLastNotePlayed {
		classData.lastNoteNumber.object = classData.lastMIDINote;
		classData.lastNoteNumber.string = "  Last MIDI Note:  "
		++ classData.lastMIDINote ++ " [" ++ TXGetMidiNoteString(classData.lastMIDINote) + "]  (click to drag)";
		classData.lastNoteFreq.object = classData.lastMIDINote.midicps.round(0.01);
		classData.lastNoteFreq.string = "= Frequency:  " ++ classData.lastMIDINote.midicps.round(0.01) ++ "  (click to drag)";
	}


	*updateScale {
		var scaleArray;
		if (classData.keyboardView.notNil, {
			if (classData.showScale == 1, {
				scaleArray = TXScale.arrScaleNotes[classData.scaleIndex];
				classData.keyboardView.showScale (scaleArray, classData.keyIndex, TXColour.yellow2);
			},{
				classData.keyboardView.showScale ([], classData.keyIndex, TXColour.white);
			});
		});
		// show middle C
		classData.keyboardView.setColor(60, classData.keyboardView.getColor(60).blend(TXColour.red, 0.5));
		// post scale
		//[classData.scaleIndex, TXScale.arrScaleNames[classData.scaleIndex]].postln;
	}

}
