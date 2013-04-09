// Copyright (C) 2008  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXMidiNoteKeybGrid {	// midi note keyboard and selection grid module
	var <>scrollView, <>scrollView2, <>noteGrid, <>midiKeyboard, <>labelView, <>popupOctave, holdButton, <>action, <value;
	var <size, system, holdOctave, numKeybOctaves, heightPerOctave;

	*new { arg window, dimensions, action, initVal, initAction=false, labelWidth=80, numberWidth = 20,
			system, getOctaveFunction, setOctaveFunction, numKeybOctaves, arrParmNames,
			getParmIndexFunction, setParmIndexFunction, scrollViewAction, scrollViewWidth;
		^super.new.init(window, dimensions, action, initVal, initAction, labelWidth, numberWidth,
				system, getOctaveFunction, setOctaveFunction, numKeybOctaves, arrParmNames,
				getParmIndexFunction, setParmIndexFunction, scrollViewAction, scrollViewWidth);
	}
	init { arg window, dimensions, argAction, initVal, initAction, labelWidth, numberWidth,
			argsystem, getOctaveFunction, setOctaveFunction, argnumKeybOctaves, arrParmNames,
			getParmIndexFunction, setParmIndexFunction, scrollViewAction, scrollViewWidth;
		var holdNumberBox, holdParmIndex, holdButtonColour;
		var left, top;
		var scrollBox, scrollBox2;
		var holdMidiKeyHeight, holdGridWidth, holdGridHeight;

		initVal = initVal ? Array.fill(16, 0);
		action = argAction;

		value = initVal;
		// size of array of number is derived from initVal size
		size = initVal.size;
		left = 0;
		top = 0;
		system = argsystem;
		holdOctave = getOctaveFunction.value.asInteger;
		numKeybOctaves = argnumKeybOctaves;
		heightPerOctave = dimensions.y / numKeybOctaves;
		holdParmIndex = getParmIndexFunction.value;

		if (scrollViewAction.notNil, {
			// add ScrollView
			scrollView = ScrollView(window, Rect(0, 0, 78, dimensions.y)).hasBorder_(false);
			if (GUI.current.asSymbol == \cocoa, {
				scrollView.autoScrolls_(false);
			});
			scrollView.hasHorizontalScroller = false;
			scrollView.hasVerticalScroller = false;
			scrollBox = CompositeView(scrollView, Rect(0, 0, 78, heightPerOctave * 10.3));
//			scrollBox.decorator = FlowLayout(scrollBox.bounds);
//			scrollBox.decorator.margin.x = 0;
//			scrollBox.decorator.margin.y = 0;
//			scrollBox.decorator.reset;
			holdOctave = 0;
			numKeybOctaves = 10;
			holdMidiKeyHeight = heightPerOctave * 10;
		},{
			holdMidiKeyHeight = dimensions.y;
		});
		// create midiKeyboard
		midiKeyboard = TXMIDIKeyboard.new(scrollBox?window, Rect(0, 0, 78, holdMidiKeyHeight), numKeybOctaves, 48, horizontal: false);


		if (scrollViewAction.notNil, {
			// add note numbers to keyboard
			10.do({ arg item;
				StaticText(scrollBox, Rect(0, ((item+1) * heightPerOctave)-(heightPerOctave/8), 70, 20))
					.align_(\right)
					.string_("C " ++ (7-item).asString);
			});
			// add ScrollView
			holdGridHeight = heightPerOctave * 10;
			holdGridWidth = 384 * 4;
			scrollView2 = ScrollView(window,
				Rect(0, 0, scrollViewWidth, dimensions.y+12)).hasBorder_(false);
			if (GUI.current.asSymbol == \cocoa, {
				scrollView2.autoScrolls_(false);
			});
			scrollView2.hasHorizontalScroller = true;
			scrollView2.hasVerticalScroller = true;
			scrollView2.action = scrollViewAction;
			scrollBox2 = CompositeView(scrollView2, Rect(0, 0, 4*384, heightPerOctave * 10.3));
//			scrollBox2.decorator = FlowLayout(scrollBox2.bounds);
//			scrollBox2.decorator.margin.x = 0;
//			scrollBox2.decorator.margin.y = 0;
//			scrollBox2.decorator.reset;
		},{
			holdGridHeight = dimensions.y;
			holdGridWidth = 384;
		});
		// if scrollview not used, use left and top because  TXBoxGrid doesn't work properly with decorator
		if (scrollViewAction.isNil and: window.decorator.notNil, {
			left = window.decorator.left;
			top = window.decorator.top;
		});
		//  grid
		noteGrid = TXBoxGrid.new(scrollBox2?window, Rect(left, top, holdGridWidth, holdGridHeight),
			columns: size, rows: (12 * numKeybOctaves))
			.setBackgrColor_(TXColor.white)
			.setNodeBorder_(0)
			.setFillMode_(true)
			.setFillColor_(TXColor.blue);
		noteGrid.nodeUpAction_({arg nodeloc;
			// set target
			this.updValueFromNodeloc(nodeloc);
			// update gui
			system.flagGuiUpd;
		});
		noteGrid.nodeDownAction_(noteGrid.nodeUpAction);
		noteGrid.nodeTrackAction_(noteGrid.nodeUpAction);
//		noteGrid.setNodeStates_(Array.fill2D(noteGrid.rows, noteGrid.columns, 0));

		// update grid
		this.updNoteGridFromValue;

		// adjust spacing
//		if (window.class == Window, {
//			window.view.decorator.nextLine;
//		}, {
//			window.decorator.nextLine;
//		});


// No longer used
//		// add octave popup if not scrolling
//		if (scrollViewAction.isNil, {
//			// create octave popup//			popupOctave = PopUpMenu(window, 110 @ 20);//			popupOctave.items = (12 - numKeybOctaves).collect({arg item, i; //				"Show C" ++ (item - 2).asString ++ " to B" ++ (item - 2 + (numKeybOctaves - 1)).asString; //			});//			popupOctave.stringColor_(TXColour.black).background_(TXColor.paleVioletRed);//			popupOctave.action = { arg view;//				setOctaveFunction.value(view.value);//				// recreate view//				system.showView;//			};//			popupOctave.value = holdOctave;//
//			// adjust spacing//			if (window.class == Window, {//				window.view.decorator.shift(-74, 24);//			}, {//				window.decorator.shift(-74, 24);//			});
//			Button(window, 18 @ 18)
//			.states_([["-", TXColor.white, TXColor.sysGuiCol1]])//			.action_({|view|//				popupOctave.valueAction = (popupOctave.value - 1).max(0);//			});//			Button(window, 18 @ 18)//			.states_([["+", TXColor.white, TXColor.sysGuiCol1]])//			.action_({|view|//				popupOctave.valueAction = (popupOctave.value + 1).min(popupOctave.items.size - 1);//			});//
//			// adjust spacing//			if (window.class == Window, {//				window.view.decorator.shift(-74, -24);//			}, {//				window.decorator.shift(-74, -24);//			});//		});

		// optional parameter buttons
		if (arrParmNames.notNil, {
			arrParmNames.do({ arg item, i;
				if (holdParmIndex == i,
					{holdButtonColour = TXColor.sysGuiCol4;},
					{holdButtonColour = TXColor.sysGuiCol1;}
				);
				holdButton = Button(window, 100 @ 18)
					.states_([
						[item, TXColor.white, holdButtonColour]
					])
					.action_({|view|
						setParmIndexFunction.value(i);
					});
				// adjust spacing
				if (window.class == Window, {
					window.view.decorator.shift(-104, 20);
				}, {
					window.decorator.shift(-104, 20);
				});
			});

			// adjust spacing
			if (window.class == Window, {
				window.view.decorator.shift(-104, (20 * arrParmNames.size).neg );
			}, {
				window.decorator.shift(-104, (20 * arrParmNames.size).neg );
			});
		});
		if (initAction) {
			action.value(this);
		};
	}

	value_ { arg argValue;
		value = argValue;
		this.updNoteGridFromValue;
	}

	valueAction_ { arg argValue;
		value = argValue;
		this.updNoteGridFromValue;
		action.value(this);
	}

	updValueFromNodeloc { arg nodeloc;
		var row, col, holdMidiNote;
		col = nodeloc.at(0);
		row = nodeloc.at(1);
		holdMidiNote = (holdOctave * 12) + ((numKeybOctaves * 12) - 1 - row);
		value.put(col, holdMidiNote);
		action.value(this);
		this.updNoteGridFromValue;
	}

	updNoteGridFromValue {
		var arrBlackNotes;
		noteGrid.reconstruct({
			// clear grid and show black notes
			noteGrid.clearGrid;
			arrBlackNotes = numKeybOctaves.collect({arg i; [1, 3, 6, 8, 10] + (i * 12);}).flatten;
			size.do({arg item, argCol;
				arrBlackNotes.do({arg argRow, i;
					noteGrid.setState_(argCol, ((numKeybOctaves * 12) - 1 - argRow), 1);
					noteGrid.setBoxColor_(argCol, ((numKeybOctaves * 12) - 1 - argRow), TXColor.grey(0.5));
				});
			});
			// show playable notes on grid
			value.do({arg item, argCol;
				var holdRow;
				holdRow = item - (holdOctave * 12);
				if ((holdRow > -1) and: (holdRow < (numKeybOctaves * 12)), {
					noteGrid.setState_(argCol, ((numKeybOctaves * 12) - 1 - holdRow), 1);
					noteGrid.setBoxColor_(argCol, ((numKeybOctaves * 12) - 1 - holdRow), TXColor.blue);
				});
			});
		});
	}

}
