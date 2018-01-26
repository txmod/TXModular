// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).

TXSoundFile {
	var <>soundFileView, <>sliderView;
	var <>controlSpec, <>action, <lo, <hi;

	*new { arg window, dimensions, action, initMinVal, initMaxVal,
			initAction=false, fileName, showFile=1, soundDataFunc, setSoundDataFunc;
		^super.new.init(window, dimensions, action, initMinVal, initMaxVal,
			initAction, fileName, showFile, soundDataFunc, setSoundDataFunc);
	}
	init { arg window, dimensions, argAction, initMinVal, initMaxVal,
			initAction, fileName, showFile, soundDataFunc, setSoundDataFunc;
		var height, spacingY;
		var zin, zout, lScroll, rScroll, totSamples, zoomOne;
		var holdData;

		zout = 1.1;
		zin = zout.reciprocal;
		rScroll = 0.1;
		lScroll = rScroll * -1;

		controlSpec = ControlSpec(0, 1);

		if (window.class == Window, {
			spacingY = window.view.decorator.gap.y;
		}, {
			spacingY = window.decorator.gap.y;
		});
		height = dimensions.y;

		initMinVal = initMinVal ? 0;
		initMaxVal = initMaxVal ? 1;

		action = argAction;

		soundFileView = SoundFileView.new(window, (dimensions.x) @ (height - 30 - (2 * spacingY)) )
			.gridOn_(false).timeCursorOn_(false)
			.waveColors_(Color.blue(alpha: 1.0) ! 8)
			//.background_(Color(0.65,0.65,0.95));
			.background_(Color(0.95,0.95,0.95));
		soundFileView.setSelectionColor(0, TXColor.yellow2);

		// set actions
		/// OLD:
		// soundFileView.action = { arg view;
		// 	this.lo = view.selectionStart(0) / view.numFrames;
		// 	this.range = view.selectionSize(0) / view.numFrames;
		// 	action.value(this);
		// };
		// NEW:
		soundFileView.mouseMoveAction = { arg view;
			this.lo = view.selectionStart(0) / view.numFrames;
			this.range = view.selectionSize(0) / view.numFrames;
			action.value(this);
		};
		soundFileView.mouseUpAction = {|view|
			soundFileView.timeCursorPosition_(soundFileView.selectionStart(0));
		};


		if ((showFile == 1), {

// setData not being set correctly for some reason - needs debugging
//			holdData = soundDataFunc.value;
//			if (holdData.notNil, {
//				soundFileView.soundfile = SoundFile.new(fileName);
//				soundFileView.setData(holdData.at(0), 1, 0, holdData.at(1), holdData.at(2));
//				soundFileView.soundfile.numChannels = holdData.at(1);
//				soundFileView.soundfile.sampleRate = holdData.at(2);
//				soundFileView.refresh;
//			},{
			if (fileName != "", {
				soundFileView.soundfile = SoundFile.new(fileName);
				soundFileView.readWithTask( block: 1, showProgress: false, doneAction: {
					//totSamples = soundFileView.dataNumSamples/soundFileView.soundfile.numChannels;
					totSamples = soundFileView.soundfile.numFrames/soundFileView.soundfile.numChannels;
					zoomOne = (soundFileView.bounds.width - 2) / totSamples;
					this.lo = initMinVal;
					this.hi = initMaxVal;
					// no longer used with Qt
					// // store data
					// if (setSoundDataFunc.notNil, {
					// 	setSoundDataFunc.value([soundFileView.data, soundFileView.soundfile.numChannels,
					// 	soundFileView.soundfile.sampleRate]);
					// });
				});
			});
			// });
		});

		// decorator next line
		if (window.class == Window, {
			window.view.decorator.nextLine;
		}, {
			window.decorator.nextLine;
		});

		sliderView = Slider(window, (dimensions.x) @ 10).action_({|slider| soundFileView.scrollTo(slider.value) });
		//sliderView.thumbSize_(sliderView.bounds.width - 6);
		sliderView.thumbSize_(20);
		sliderView.knobColor_(Color.white);

		// decorator next line
		if (window.class == Window, {
			window.view.decorator.nextLine;
		}, {
			window.decorator.nextLine;
		});

		//create buttons
		[
			["no zoom", { soundFileView.zoomAllOut} ],  // view all - zoom all out
			["zoom out", { soundFileView.zoom(zout) } ],  // zoom out
			["zoom in", { soundFileView.zoom(zin) } ], // zoom in
			["zoom select", { soundFileView.zoomSelection(0); this.valueBoth = [lo,hi]} ],  // fit selection to view
			["select all", { soundFileView.zoomAllOut; this.valueBoth = [0,1]; action.value(this);} ],  // select all
			["select none", { this.valueBoth = [0,0]; action.value(this);} ],  // select none
		].do({ arg item, i;
			Button(window,  70 @ 20)
			.states_([
				[item.at(0), TXColor.white, TXColor.sysGuiCol1]
			])
			.action_({|view|
				if ((fileName != "")  and: (showFile == 1), {
					// run action function
					item.at(1).value;
					sliderView.value = soundFileView.scrollPos;   // update scrollbar position
					// sliderView.thumbSize =
					// 12.max((sliderView.bounds.width - 2) * soundFileView.xZoom * zoomOne);
				});
			});
		});
		lo = initMinVal;
		hi = initMaxVal;
		if (initAction) {
			action.value(this);
		};
	}

//	value {
//		^lo;
//	}
//
	valueBoth {
		^[lo, hi];
	}

	range {
		^hi - lo;
	}

//	value_ { arg value;
//		lo = controlSpec.constrain(value);
//	}
//
	valueBoth_ { arg valueArray;
		this.lo = controlSpec.constrain(valueArray.at(0));
		this.hi = controlSpec.constrain(valueArray.at(1));
	}

	lo_ {arg value;
		lo = controlSpec.constrain(value);
		soundFileView.tryPerform(\setSelectionStart, 0 , lo * (soundFileView.numFrames ? 0));

	}

	hi_ {  arg value;
		hi = controlSpec.constrain(value);
		soundFileView.tryPerform(\setSelectionSize, 0 , (hi - lo) * (soundFileView.numFrames ? 0));
	}

	range_ {arg value;
		this.hi = lo + value.abs;
	}
}



