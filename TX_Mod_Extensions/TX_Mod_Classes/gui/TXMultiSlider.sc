// Copyright (C) 2005  Paul Miller. This file is part of TX Modular system distributed under the terms of the GNU General Public License (see file LICENSE).
		
TXMultiSlider {	// MultiSlider module with label
	var <>labelView, <>scrollView, <>multiSliderView, <>controlSpec, <>action, <value, userView;
	
	*new { arg window, dimensions, label, controlSpec, action, initVal, 
			initAction=false, labelWidth=80, showClone1=1, scrollViewWidth, 
			gridRows=0, gridCols=0;
		^super.new.init(window, dimensions, label, controlSpec, action, initVal, 
			initAction, labelWidth, showClone1, scrollViewWidth, gridRows, gridCols);
	}
	init { arg window, dimensions, label, argControlSpec, argAction, initVal, 
			initAction, labelWidth, showClone1, scrollViewWidth, gridRows, gridCols;
		var holdMSVWidth, scrollBox;
		
		labelView = StaticText(window, labelWidth @ 20);
		labelView.string = label;
		labelView.align = \right;
		gridRows = gridRows ? 0;
		gridCols = gridCols ? 0;
		
		controlSpec = argControlSpec.asSpec;
		initVal = initVal ? Array.fill(8, controlSpec.default);
		action = argAction;
		holdMSVWidth = dimensions.x - labelWidth - 4;
		
		if (scrollViewWidth.notNil, {
			scrollView = ScrollView(window, Rect(0, 0, scrollViewWidth, dimensions.y))
				.hasBorder_(false);
			if (GUI.current.asSymbol == \cocoa, {
				scrollView.autoScrolls_(false);
			});
			scrollView.hasHorizontalScroller = false;
			scrollView.hasVerticalScroller = false;
			scrollBox = CompositeView(scrollView, Rect(0, 0, (initVal.size * 24), dimensions.y));
			scrollBox.decorator = FlowLayout(scrollBox.bounds);
			scrollBox.decorator.margin.x = 0;
			scrollBox.decorator.margin.y = 0;
			scrollBox.decorator.reset;
			holdMSVWidth = (initVal.size+1) * 24;
		});		

		if (gridRows > 0 or: {gridCols > 0}, {
			// create grid
			userView = UserView(scrollBox?window, holdMSVWidth @ dimensions.y);
			userView.drawFunc = {	
				Pen.strokeColor = Color.white;
				gridRows.do({arg item, i; 
					var holdHeight;
					holdHeight = ((dimensions.y * (i + 1)) / gridRows).asInteger;
					Pen.line(0 @ holdHeight, holdMSVWidth @ holdHeight);
				});
				gridCols.do({arg item, i; 
					var holdWidth;
					holdWidth = ((holdMSVWidth * (i + 1)) / gridCols).asInteger;
					Pen.line(holdWidth @ 0, holdWidth @ dimensions.y);
				});
				Pen.stroke
			};
			// decorator shift 
			if (scrollBox.notNil, {
					scrollBox.decorator.shift(0-holdMSVWidth-3, 0);
			},{
				if (window.class == Window, {
					window.view.decorator.shift(0-holdMSVWidth-3, 0);
				}, {
					window.decorator.shift(0-holdMSVWidth-3, 0);
				});
			});
		});

		multiSliderView = MultiSliderView(scrollBox?window, holdMSVWidth @ dimensions.y-2);
		multiSliderView.action = {
			value = controlSpec.map(multiSliderView.value);
			action.value(this);
		};

		if (showClone1 == 1, {
			Button(window, 50 @ 20)
			.states_([["clone 1", TXColor.white, TXColor.sysGuiCol1]])
			.action_({|view|
				value = Array.fill(this.value.size, this.value.at(0));
				multiSliderView.value = controlSpec.unmap(value);
				action.value(this);
			});
		});
		
		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			multiSliderView.value = controlSpec.unmap(value);
		};
	}
	value_ { arg value; multiSliderView.value = controlSpec.unmap(value) }
	set { arg label, spec, argAction, initVal, initAction=false;
		labelView.string = label;
		controlSpec = spec.asSpec;
		action = argAction;
		initVal = initVal ? Array.fill(8, controlSpec.default);
		if (initAction) {
			this.value = initVal;
		}{
			value = initVal;
			multiSliderView.value = controlSpec.unmap(value);
		};
	}
}
