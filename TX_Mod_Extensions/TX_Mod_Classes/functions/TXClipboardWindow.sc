// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
TXClipboardWindow {
	classvar <>clipboardWindow;
	classvar classData;

	*initClass {
		classData = ();
		classData.windowVisibleOrigin = Point.new(0,0);
		classData.alwaysOnTop = 1;
		this.resetValues;
		this.clearViews;
	}

	*closeWindow {
		if (clipboardWindow.notNil, {
			clipboardWindow.close;
		});
		this.clearViews;
	}

	*resetValues {
		classData.holdNumbers = 0 ! 10;
		//	1 beat at 120 bpm, or  2 cps, = 0.5 secs
		classData.holdBpm = 120;
		classData.holdCps = 2;
		classData.holdBeats = 1;
		classData.holdSecs = 0.5;
		classData.holdMidiNote = 60;
		classData.holdNoteFreq = 60.midicps;
		classData.holdStrings = "" ! 5;
		classData.holdColors = [TXColor.sysDeleteCol, TXColor.sysInterfaceButton, TXColor.sysGuiCol1, TXColor.sysGuiCol2, TXColor.sysGuiCol4,
			TXColor.sysChannelControl,
			TXColor.sysHelpCol, TXColor.sysRecordCol, TXColor.sysGuiCol3, TXColor.sysSelectedModString,
		] ++ (TXColor.white ! 11);
		classData.holdCurves = Array.newClear(256).seriesFill(0, 255.reciprocal) ! 10;
	}

	*rebuildWindow {
		this.closeWindow;
		{this.showWindow;}.defer(0.05);
	}

	*clearViews {
		classData.viewBox = nil;
	}
	//======================

	*showWindow {
		var windowWidth = 800;
		var windowHeight = 390;
		var holdView, holdBpmView, holdCpsView, holdBeatsView, holdSecsView;
		var holdMidiNoteView, holdNoteFreqView, holdMidiNotePopup;
		var calcViewUpdateFunc, bpmChangedFunc, cpsChangedFunc, beatsChangedFunc, timeChangedFunc;
		var midiNoteChangedFunc, noteFreqChangedFunc;
		var plusButton, minusButton;

		// create window if needed
		if (clipboardWindow.isNil, {
			clipboardWindow = Window.new(
				"Clipboard - drag & drop (use command key)",
				Rect(400, 800, windowWidth + 10, windowHeight + 10),
				resizable: true, scroll: true
			).front;
			clipboardWindow.view.background = TXColor.grey(0.8);
			clipboardWindow.view.action = {arg view; classData.windowVisibleOrigin = view.visibleOrigin;};
			clipboardWindow.view.mouseWheelAction = {arg view; classData.windowVisibleOrigin = view.visibleOrigin;};
			clipboardWindow.onClose_({this.clipboardWindow = nil;});
			clipboardWindow.alwaysOnTop = classData.alwaysOnTop.asBoolean;
		});
		clipboardWindow.front;

		// defer visible origin
		{clipboardWindow.view.visibleOrigin = classData.windowVisibleOrigin;}.defer(0.1);

		if (classData.viewBox.notNil, {
			TXSystem1GUI.deferRemoveView(classData.viewBox);
		});

		classData.viewBox = CompositeView(clipboardWindow, Rect(0, 0, windowWidth, windowHeight));
		classData.viewBox.decorator = FlowLayout(classData.viewBox.bounds);
		classData.viewBox.decorator.gap = 8@8;

		holdView = TXCheckBox(classData.viewBox, Rect(0, 0, 120, 20), "window on top", TXColor.sysGuiCol2,
			TXColor.white, TXColor.white, TXColor.sysGuiCol1);
		holdView.value =  classData.alwaysOnTop;
		holdView.action = {|view|
			classData.alwaysOnTop = view.value;
			clipboardWindow.alwaysOnTop = classData.alwaysOnTop.asBoolean;
		};

		classData.viewBox.decorator.shift(20, 0);

		holdView = Button(classData.viewBox, Rect(0, 0, 120, 20));
		holdView.states = [["Open Colour Picker", TXColor.white, TXColor.sysGuiCol2]];
		holdView.action = {TXColor.showPicker;};

		classData.viewBox.decorator.shift(10, 0);

		holdView = Button(classData.viewBox, Rect(0, 0, 120, 20));
		holdView.states = [["Open Curve Builder", TXColor.white, TXColor.sysGuiCol2]];
		holdView.action = {TXCurveBuilder.showWindow;};

		classData.viewBox.decorator.shift(10, 0);

		holdView = Button(classData.viewBox, Rect(0, 0, 120, 20));
		holdView.states = [["Open MIDI Keyboard", TXColor.white, TXColor.sysGuiCol2]];
		holdView.action = {TXMIDIKeyboardWindow.showWindow;};

		classData.viewBox.decorator.shift(20, 0);

		holdView = Button(classData.viewBox, Rect(0, 0, 120, 20));
		holdView.states = [["Reset all controls", TXColor.white, TXColor.sysDeleteCol]];
		holdView.action = {this.resetValues; this.showWindow;};

		classData.viewBox.decorator.nextLine.shift(0, 10);

		// change gap for this row
		classData.viewBox.decorator.gap = 3@8;

		//	Calculate: e.g. 120 “bpm =” 2 “cps" 1 “beats = " 0.5 "secs"  &  [Reset] button
		// define update functions
		calcViewUpdateFunc = {
			holdBpmView.value = classData.holdBpm;
			holdCpsView.value = classData.holdCps;
			holdBeatsView.value = classData.holdBeats;
			holdSecsView.value = classData.holdSecs;
			holdMidiNoteView.value = classData.holdMidiNote;
			holdMidiNotePopup.value = classData.holdMidiNote.round.asInteger;
			holdNoteFreqView.value = classData.holdNoteFreq;
		};
		bpmChangedFunc = {arg newBpm;
			classData.holdBpm = newBpm;
			classData.holdCps = newBpm / 60;
			classData.holdSecs = classData.holdCps.reciprocal * classData.holdBeats;
			calcViewUpdateFunc.value;
		};
		cpsChangedFunc = {arg newCps;
			classData.holdCps = newCps;
			classData.holdBpm = newCps * 60;
			classData.holdSecs = classData.holdCps.reciprocal * classData.holdBeats;
			calcViewUpdateFunc.value;
		};
		beatsChangedFunc = {arg newBeats;
			classData.holdBeats = newBeats;
			classData.holdSecs = classData.holdCps.reciprocal * classData.holdBeats;
			calcViewUpdateFunc.value;
		};
		timeChangedFunc = {arg newTime;
			classData.holdSecs = newTime;
			classData.holdCps =  classData.holdSecs.reciprocal * classData.holdBeats;
			classData.holdBpm = classData.holdCps * 60;
			calcViewUpdateFunc.value;
		};
		midiNoteChangedFunc = {arg newMidiNote;
			classData.holdMidiNote = newMidiNote;
			classData.holdNoteFreq = classData.holdMidiNote.midicps;
			calcViewUpdateFunc.value;
		};
		noteFreqChangedFunc = {arg newNoteFreq;
			classData.holdNoteFreq = newNoteFreq;
			classData.holdMidiNote = classData.holdNoteFreq.cpsmidi;
			calcViewUpdateFunc.value;
		};
		holdView = StaticText(classData.viewBox, Rect(0, 0, 76, 20));
		holdView.string = "Calculate:   At";
		holdView.align = \center;
		// Bpm classData.holdBpm
		holdBpmView = NumberBox(classData.viewBox, Rect(0, 0, 50, 20));
		holdBpmView.font = (Font.new("Helvetica",12));
		holdBpmView.stringColor = Color.black;
		holdBpmView.normalColor = Color.black;
		holdBpmView.typingColor = Color.blue;
		holdBpmView.action = ({arg view;
			bpmChangedFunc.value(view.value);
		});
		holdBpmView.maxDecimals = 3;
		holdBpmView.beginDragAction_({ arg view, x, y;
			view.value;
		});
		holdBpmView.canReceiveDragHandler = {
			View.currentDrag.isKindOf( Number )
		};
		holdBpmView.receiveDragHandler = {arg view;
			view.value = View.currentDrag;
			bpmChangedFunc.value(view.value);
		};
		holdView = StaticText(classData.viewBox, Rect(0, 0, 36, 20));
		holdView.string = "bpm =";
		holdView.align = \left;
		// Cps classData.holdCps
		holdCpsView = NumberBox(classData.viewBox, Rect(0, 0, 50, 20));
		holdCpsView.font = (Font.new("Helvetica",12));
		holdCpsView.stringColor = Color.black;
		holdCpsView.normalColor = Color.black;
		holdCpsView.typingColor = Color.blue;
		holdCpsView.action = ({arg view;
			cpsChangedFunc.value(view.value);
		});
		holdCpsView.maxDecimals = 4;
		holdCpsView.beginDragAction_({ arg view, x, y;
			view.value;
		});
		holdCpsView.canReceiveDragHandler = {
			View.currentDrag.isKindOf( Number )
		};
		holdCpsView.receiveDragHandler = {arg view;
			view.value = View.currentDrag;
			cpsChangedFunc.value(view.value);
		};
		holdView = StaticText(classData.viewBox, Rect(0, 0, 28, 20));
		holdView.string = "cps,";
		holdView.align = \left;
		// Beats classData.holdBeats
		holdBeatsView = NumberBox(classData.viewBox, Rect(0, 0, 50, 20));
		holdBeatsView.font = (Font.new("Helvetica",12));
		holdBeatsView.stringColor = Color.black;
		holdBeatsView.normalColor = Color.black;
		holdBeatsView.typingColor = Color.blue;
		holdBeatsView.action = ({arg view;
			beatsChangedFunc.value(view.value);
		});
		holdBeatsView.maxDecimals = 4;
		holdBeatsView.beginDragAction_({ arg view, x, y;
			view.value;
		});
		holdBeatsView.canReceiveDragHandler = {
			View.currentDrag.isKindOf( Number )
		};
		holdBeatsView.receiveDragHandler = {arg view;
			view.value = View.currentDrag;
			beatsChangedFunc.value(view.value);
		};
		holdView = StaticText(classData.viewBox, Rect(0, 0, 43, 20));
		holdView.string = "beats =";
		holdView.align = \left;
		// Secs classData.holdSecs
		holdSecsView = NumberBox(classData.viewBox, Rect(0, 0, 50, 20));
		holdSecsView.font = (Font.new("Helvetica",12));
		holdSecsView.stringColor = Color.black;
		holdSecsView.normalColor = Color.black;
		holdSecsView.typingColor = Color.blue;
		holdSecsView.action = ({arg view;
			timeChangedFunc.value(view.value);
		});
		holdSecsView.maxDecimals = 4;
		holdSecsView.beginDragAction_({ arg view, x, y;
			view.value;
		});
		holdSecsView.canReceiveDragHandler = {
			View.currentDrag.isKindOf( Number )
		};
		holdSecsView.receiveDragHandler = {arg view;
			view.value = View.currentDrag;
			timeChangedFunc.value(view.value);
		};
		holdView = StaticText(classData.viewBox, Rect(0, 0, 94, 20));
		holdView.string = "secs  //  midi note";
		holdView.align = \left;

		// MidiNote classData.holdMidiNote
		holdMidiNotePopup = PopUpMenu(classData.viewBox, Rect(0, 0, 74, 20))
		.background_(Color.white)
		.items_(TXGetMidiNoteString.arrAllNoteNumberNames)
		.action_({arg view;
			midiNoteChangedFunc.value(view.value);
		});
		holdMidiNotePopup.font = (Font.new("Helvetica",12));

		plusButton = Button(classData.viewBox, Rect(0, 0, 14, 20))
			.states_([
            ["+", Color.white, TXColor.sysGuiCol1],
        ])
		.action_({arg view;
			holdMidiNotePopup.valueAction = (holdMidiNotePopup.value + 1).min(holdMidiNotePopup.items.size - 1);
		});
		minusButton = Button(classData.viewBox, Rect(0, 0, 14, 20))
			.states_([
            ["-", Color.white, TXColor.sysGuiCol1],
        ]).action_({arg view;
			holdMidiNotePopup.valueAction = (holdMidiNotePopup.value - 1).max(0);
		});

		holdMidiNoteView = NumberBox(classData.viewBox, Rect(0, 0, 50, 20));
		holdMidiNoteView.font = (Font.new("Helvetica",12));
		holdMidiNoteView.stringColor = Color.black;
		holdMidiNoteView.normalColor = Color.black;
		holdMidiNoteView.typingColor = Color.blue;
		holdMidiNoteView.action = ({arg view;
			midiNoteChangedFunc.value(view.value);
		});
		holdMidiNoteView.maxDecimals = 3;
		holdMidiNoteView.beginDragAction_({ arg view, x, y;
			view.value;
		});
		holdMidiNoteView.canReceiveDragHandler = {
			View.currentDrag.isKindOf( Number )
		};
		holdMidiNoteView.receiveDragHandler = {arg view;
			view.value = View.currentDrag;
			midiNoteChangedFunc.value(view.value);
		};
		holdView = StaticText(classData.viewBox, Rect(0, 0, 33, 20));
		holdView.string = "= freq";
		holdView.align = \left;

		// NoteFreq classData.holdNoteFreq
		holdNoteFreqView = NumberBox(classData.viewBox, Rect(0, 0, 50, 20));
		holdNoteFreqView.font = (Font.new("Helvetica",12));
		holdNoteFreqView.stringColor = Color.black;
		holdNoteFreqView.normalColor = Color.black;
		holdNoteFreqView.typingColor = Color.blue;
		holdNoteFreqView.action = ({arg view;
			noteFreqChangedFunc.value(view.value);
		});
		holdNoteFreqView.maxDecimals = 2;
		holdNoteFreqView.beginDragAction_({ arg view, x, y;
			view.value;
		});
		holdNoteFreqView.canReceiveDragHandler = {
			View.currentDrag.isKindOf( Number )
		};
		holdNoteFreqView.receiveDragHandler = {arg view;
			view.value = View.currentDrag;
			noteFreqChangedFunc.value(view.value);
		};
		holdView = StaticText(classData.viewBox, Rect(0, 0, 30, 20));
		holdView.string = "cps";
		holdView.align = \left;
		// set values for all calcuation views
		calcViewUpdateFunc.value;

		// set values for all calcuation views
		calcViewUpdateFunc.value;

		// change gap back
		classData.viewBox.decorator.gap = 8@8;

		classData.viewBox.decorator.nextLine;
		classData.viewBox.decorator.shift(0, 6);

		// numbers
		holdView = StaticText(classData.viewBox, Rect(0, 0, 56, 20));
		holdView.string = "Numbers:";
		holdView.align = \center;
		8.do({arg i;
			holdView = NumberBox(classData.viewBox, Rect(0, 0, 80, 20));
			holdView.font = (Font.new("Helvetica",12));
			holdView.stringColor = Color.black;
			holdView.normalColor = Color.black;
			holdView.typingColor = Color.blue;
			holdView.action = ({arg view;
				classData.holdNumbers[i] = view.value;
			});
			holdView.maxDecimals = 5;
			holdView.value = classData.holdNumbers[i];
			holdView.beginDragAction_({ arg view, x, y;
				view.value;
			});
			holdView.canReceiveDragHandler = {
				View.currentDrag.isKindOf( Number )
			};
			holdView.receiveDragHandler = {arg view;
				view.value = View.currentDrag;
				classData.holdNumbers[i] = view.value;
			};
		});
		classData.viewBox.decorator.nextLine;
		classData.viewBox.decorator.shift(0, 6);

		// text fields
		5.do({arg i;
			holdView = StaticText(classData.viewBox, Rect(0, 0, 56, 20));
			holdView.string = "Text:";
			holdView.align = \center;

			holdView = TextField(classData.viewBox, Rect(0, 0, 700, 20));
			holdView.action = ({arg view;
				classData.holdStrings[i] = view.string;
			});
			holdView.string = classData.holdStrings[i];
			holdView.beginDragAction_({ arg view, x, y;
				view.dragLabel_("String");
				view.string;
			});
			holdView.canReceiveDragHandler = {
				View.currentDrag.isKindOf( String )
			};
			holdView.receiveDragHandler = {arg view;
				view.string = View.currentDrag;
				classData.holdStrings[i] = view.string;
			};
			classData.viewBox.decorator.nextLine;
		});
		classData.viewBox.decorator.nextLine;
		classData.viewBox.decorator.shift(0, 6);

		holdView = StaticText(classData.viewBox, Rect(0, 0, 56, 20));
		holdView.string = "Colors:";
		holdView.align = \center;
		classData.holdColors.size.do({arg i;
			holdView = DragBoth(classData.viewBox, Rect(0, 0, 25, 25));
			holdView.background = classData.holdColors[i];
			holdView.beginDragAction_({ arg view, x, y;
				var holdColour;
				view.dragLabel_("Colour");
				holdColour = view.background;
				// return colour
				holdColour;
			});
			holdView.canReceiveDragHandler = {
				View.currentDrag.isKindOf( Color )
			};
			holdView.receiveDragHandler = {arg view;
				var holdDragObject;
				holdDragObject = View.currentDrag;
				view.background_(holdDragObject);
				classData.holdColors[i] = holdDragObject;
			};
		});
		classData.viewBox.decorator.nextLine;
		classData.viewBox.decorator.shift(0, 6);

		holdView = StaticText(classData.viewBox, Rect(0, 0, 56, 20));
		holdView.string = "Curves:";
		holdView.align = \center;
		8.do({arg i;
			// create curve
			var curveWidth = 80;
			var curveHeight = 60;
			var curveView = UserView(classData.viewBox, Rect(0, 0, curveWidth, curveHeight));
			curveView.background = TXColor.white;
			curveView.drawFunc = {
				var holdArray = classData.holdCurves[i];
				var holdArraySize = holdArray.size;
				var holdX, holdY;
				Pen.use {
					Pen.width = 1.0;
					Pen.strokeColor = TXColor.black.blend(TXColor.red, 0.35);
					Pen.beginPath;
					Pen.moveTo(0 @ (curveHeight * (1 - holdArray[0])));
					holdArray.do({arg item, i;
						if (i> 0, {
							Pen.lineTo((curveWidth * i/(holdArraySize - 1)) @ (curveHeight * (1 - holdArray[i])));
						});
					});
					Pen.stroke;
					Pen.strokeColor = TXColor.sysGuiCol1.blend(Color.grey, 0.5);
					holdArray.do({arg item, i;
						var p1, p2;
						p1 = (curveWidth * i/(holdArraySize - 1)) @ (curveHeight * (1 - holdArray[i]));
						p2 = (curveWidth * i/(holdArraySize - 1)) @ curveHeight;
						Pen.line(p1, p2);
						Pen.stroke;
					});
				};
			};
			curveView.refresh;

			holdView = DragBoth(curveView, Rect(0, 0, curveWidth, curveHeight));
			holdView.background = TXColor.white.alpha_(0);
			holdView.beginDragAction_({ arg view, x, y;
				view.dragLabel = "Curve (size: " ++ classData.holdCurves[i].size.asString ++")" ;
				classData.holdCurves[i];
			});
			holdView.canReceiveDragHandler = {
				View.currentDrag.isKindOf( Array ) and: {View.currentDrag[0].isKindOf( Number )};
			};
			holdView.receiveDragHandler = {arg view;
				var holdDragObject;
				holdDragObject = View.currentDrag;
				classData.holdCurves[i] = holdDragObject;
				curveView.refresh;
			};
		});
	}

}
